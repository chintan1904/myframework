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
	
	public static void beforeTestStart(String sheetName) {
		Log.info("----------- Before Test execution start : Sheet Name = "+sheetName);
	}
	
	public static void  beforeTestend() {
		Log.info("------------------- Before Test execution End ----------------------");
	}
	
	public static void afterTestStart(String sheetName) {
		Log.info("----------- After Test execution start : Sheet Name = "+sheetName);
	}
	
	public static void  afterTestend() {
		Log.info("------------------- After Test execution End ----------------------");
	}
	
	public static void beforeSuiteStart() {

		Log.info("*********************************************************************");
		Log.info("*********************** Before Suite Started ************************");
		Log.info("*********************************************************************");
	}
	
	public static void beforeSuiteEnd() {

		Log.info("*********************************************************************");
		Log.info("************************* Before Suite End **************************");
		Log.info("*********************************************************************");
	}


	public static void afterSuiteStart() {

		Log.info("*********************************************************************");
		Log.info("************************ After Suite Started ************************");
		Log.info("*********************************************************************");
	}
	
	public static void afterSuiteEnd() {

		Log.info("*********************************************************************");
		Log.info("************************** After Suite End **************************");
		Log.info("*********************************************************************");
	}

	
	
	
	public static void info(String message) { 

		if(DriverScript.test != null ) {
			DriverScript.test.append(message);
			DriverScript.test.append("<br>");
		}
		log.info(message);
	}
	
	public static void error(String message) {
		if(DriverScript.test != null ) {
			DriverScript.test.append(message);
			DriverScript.test.append("<br>");
		}
		log.error(message);
	}
	
	public static void warn(String message) {
		if(DriverScript.test != null ) {
			DriverScript.test.append(message);
			DriverScript.test.append("<br>");
		}
		log.warn(message);
	}
	
	public static void debug(String message) {
		if(DriverScript.test != null ) {
			DriverScript.test.append(message);
			DriverScript.test.append("<br>");
		}
		log.debug(message);
	}
	
	public static void fatal(String message) {
		if(DriverScript.test != null ) {
			DriverScript.test.append(message);
			DriverScript.test.append("<br>");
		}
		log.fatal(message);
	}

}
