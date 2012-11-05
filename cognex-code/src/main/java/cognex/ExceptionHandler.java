package cognex;

/**
 * @author Robinson Levin [robinson.levin@cognex.com]
 */
public class ExceptionHandler {
	
	public int exceptionCount;
	
	public ExceptionHandler() {
		exceptionCount = 0;
	}
	
	public void handleException(Exception ex) {
		exceptionCount++;
		
		ex.printStackTrace();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
