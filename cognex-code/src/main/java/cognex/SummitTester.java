package cognex;


import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Robinson Levin [robinson.levin@cognex.com]
 */
public class SummitTester {
	
	
	public static String readerName = "DM200_117C04.pc.cognex.com"; // change this to the host name of your reader (or IP)
	public static int readerTCPPort = 23;
	
	public static void main(String[] args) {
		
		ExceptionHandler exHandler = new ExceptionHandler();
		
		Socket datamanSocket;
		PrintWriter datamanWriter;
		BufferedReader datamanReader; 
		DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
		
		boolean waitingForResult = true;
		int cycles = 0;
		
		System.out.println("Type \"exit\" to stop the test");
		
		BackgroundReader consoleBackgroundReader = new BackgroundReader(new BufferedReader(new InputStreamReader(System.in)));
		consoleBackgroundReader.start();
		
		while(true) {
			
			// reset the socket connection
			datamanSocket = null;
			datamanWriter = null;
			datamanReader = null; 
			
			// OPEN SOCKET
			try {
				datamanSocket = new Socket(readerName, readerTCPPort);
				datamanWriter = new PrintWriter(datamanSocket.getOutputStream(),true);
				datamanReader = new BufferedReader(new InputStreamReader(datamanSocket.getInputStream()));
				
				datamanSocket.setSoTimeout(60000);
				
			} catch (Exception e) {
				exHandler.handleException(e);
			}
			
			// progress tracking, you could set the printout to 1000 or 10000 as well
			if(cycles % 100 == 0) {
				System.out.println(df.format(new Date()) + "   Cycles so far: " + cycles);
			}
			
			
			// TRIGGER READER
			datamanWriter.println("||>TRIGGER ON");
			
			
			// WAIT FOR RESULT
			waitingForResult = true;
			while(waitingForResult) {
				try {
					String response = datamanReader.readLine();
					waitingForResult = false;
					
					// log to a file
					try {
					    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("DataLog.txt", true)));
					    out.println("[" + df.format(new Date()) + "]  " + response);
					    out.close();
					} catch (Exception e) {
						exHandler.handleException(e);
					}
					
				} catch (Exception e) {
					exHandler.handleException(e);
				}
			}
			
			
			// CLOSE THE SOCKET
			try {
				datamanWriter.close();
				datamanReader.close();
				datamanSocket.close();
			} catch (Exception e) {
				exHandler.handleException(e);
			}
			
			
			if(consoleBackgroundReader.exitSignal) {
				break;
			}
			
			cycles++;
		}
		
		
		System.out.print("Quitting... ");
		
		consoleBackgroundReader.stop();

		System.out.println("Done.");
		
		System.exit(0);
	}
}