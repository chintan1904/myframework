package utillity;

import org.apache.log4j.Logger;

import executionEngine.DriverScript;

public class Log {
	
	public static Logger log = Logger.getLogger(Log.class.getName());
	
	public static void startTestCase(String testCaseName) {

		log.info("########################################################################");
		log.info("###################       "+testCaseName+"      ########################");
		log.info("########################################################################");
	}
	
	public static void endTestCase(String testCaseName) {

		log.info("########################################################################");
		log.info("###################       +E     N     D+        #######################");
		log.info("########################################################################");
	}
	
	public static void info(String message) { 
		
		DriverScript.test.append(message);
		log.info(message);
	}
	
	public static void error(String message) {
		if(DriverScript.test != null )
			DriverScript.test.append(message);
		log.error(message);
	}
	
	public static void warn(String message) {
		if(DriverScript.test != null )
			DriverScript.test.append(message);
		log.warn(message);
	}
	
	public static void debug(String message) {
		if(DriverScript.test != null )
			DriverScript.test.append(message);
		log.debug(message);
	}
	
	public static void fatal(String message) {
		if(DriverScript.test != null )
			DriverScript.test.append(message);
		log.fatal(message);
	}

}
