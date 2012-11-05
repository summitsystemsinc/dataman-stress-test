package cognex;

import java.io.*;

/**
 * @author Robinson Levin [robinson.levin@cognex.com]
 */
public class BackgroundReader extends Thread {
	
	BufferedReader reader;
	public boolean exitSignal = false;
	
	
	public BackgroundReader(BufferedReader reader) {
		this.reader = reader;
	}

	public void run() {
		while (true) {
			try {
				String userInput = reader.readLine();

				if(userInput.equalsIgnoreCase("exit")) {
					exitSignal = true;
				}
			} catch (IOException e) {
				System.err.println("IOException reading in BackgroundReader");
			}
		}
	}
}
