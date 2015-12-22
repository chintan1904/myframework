package executionEngine;

import java.lang.reflect.Method;
import java.util.Properties;
import org.apache.log4j.xml.DOMConfigurator;
import config.Actions;
import config.Constants;
import utillity.ExcelUtils;
import utillity.Log;
import utillity.PropertyFile;
import utillity.SingleTest;
import utillity.TestSuite;

public class DriverScript {
	
	public static Method method[];
	public static Actions actions;
	public static Properties OR;
	public static Properties CONFIG;
	public static String actionKeyword;
	public static String pageElement;
	public static String data;
	public static boolean testResult = true;
	public static boolean configFileLoad = true;
	public static SingleTest test;
	private static String beforeTestSheetName;
	private static String afterTestSheetName;
	private static String beforeSuiteSheetName;
	private static String afterSuiteSheetName;
	private static boolean beforeSuiteResult;
	
	public static void main(String[] args) {
		
		DOMConfigurator.configure("log4j.xml");
		actions = new Actions();
		method = actions.getClass().getMethods();
		
		CONFIG = PropertyFile.load("src/main/resources/setup/config.properties");
		TestSuite.suite.clear();

		if(configFileLoad) {
			OR=PropertyFile.load(CONFIG.getProperty("OR_FILE_PATH"));

			if(configFileLoad) {
				String excelFilePath = CONFIG.getProperty("TEST_SUITE_FILE_PATH");
				String excelSheetName = CONFIG.getProperty("TEST_SUITE_SHEET_NAME");
				ExcelUtils.setExcelFile(excelFilePath);
				
				runTestSuite(excelFilePath, excelSheetName);
			}
		}
		TestSuite.generateReport();
	}

	/**
	 * Matches the action name retrieved from excel with each methods available in Actions.class
	 * If match is found then method is executed.
	 * If match is not found, or any exception occurs while executing the action then step is marked as failed.
	 * 
	 * @author Chintan Shah
	 */
	private static void executeActions() {
		
		boolean methodExecuted = false;
		try { 
			for(int i = 0 ; i < method.length ; i++) {
				if(method[i].getName().equals(actionKeyword)) {
					method[i].invoke(actions,pageElement,data);
					methodExecuted = true;
					break;
				}
			}
		}
		catch(Exception e) {

			Log.error("Exception occured while executing the method : " +actionKeyword);
			Log.error(e.toString());
		}

		if(!methodExecuted) { //TODO || testResult == false
			Log.error("Could not find required action :"+actionKeyword);
			testResult = false;
		}
	}
	
	
	
	/**
	 * Method executes all steps given in a test sheet.
	 * 
	 * @author Chintan Shah
	 * @param excelFilePath 
	 * @param testCase = Sheet name
	 */
	private static void executeTestCase(String excelFilePath, String testCase) { 
		
		for(int i = 1; i < ExcelUtils.getLastRowNumber(testCase) ; i++) {
			
			pageElement = ExcelUtils.getCellData(i, Constants.COL_PAGEELEMENT , testCase);
			actionKeyword = ExcelUtils.getCellData(i, Constants.COL_ACTIONKEYWORD, testCase);
			data = ExcelUtils.getCellData(i, Constants.COL_TESTSTEPDATA, testCase);
			testResult = true;
			executeActions();

			if(testResult) {
				if(!(testCase.equalsIgnoreCase(beforeTestSheetName) || testCase.equalsIgnoreCase(afterTestSheetName) 
						|| testCase.equalsIgnoreCase(beforeSuiteSheetName) || testCase.equalsIgnoreCase(afterSuiteSheetName)))
					ExcelUtils.setTestResultInExcel(excelFilePath, testCase, i, Constants.COL_TESTSTEPRESULT, Constants.KEYWORD_PASS);
			}
			else {
				if(!(testCase.equalsIgnoreCase(beforeTestSheetName) || testCase.equalsIgnoreCase(afterTestSheetName) 
						|| testCase.equalsIgnoreCase(beforeSuiteSheetName) || testCase.equalsIgnoreCase(afterSuiteSheetName)))
					ExcelUtils.setTestResultInExcel(excelFilePath, testCase, i, Constants.COL_TESTSTEPRESULT, Constants.KEYWORD_FAIL);
				break;
			}
		}
	}
	
	/**
	 * Method to execute Before Test sheet.
	 * System will check if sheet name is provided in config.properties file and execute the same.
	 * If sheet name is not provided then this step will be skipped.
	 * 
	 * @author Chintan Shah
	 * @param excelFilePath
	 */
	private static void executeBeforeTest(String excelFilePath) {

		beforeTestSheetName = CONFIG.getProperty("BEFORE_TEST_SHEET_NAME");
		if(beforeTestSheetName != null) {
			if(beforeSuiteResult) {
				if(ExcelUtils.isSheetPresent(beforeTestSheetName)) {
					Log.beforeTestStart(beforeTestSheetName);
					executeTestCase(excelFilePath, beforeTestSheetName);
					Log.beforeTestend();
				}
				else {
					Log.error("Could not find sheet : "+beforeTestSheetName);
					testResult = false;
				}
			}
			else {
				Log.error("Before Suite execution failed, Thus skipped before test execution");
				testResult = false;
			}
		}
	}
	
	/**
	 * Method to execute After Test sheet.
	 * System will check if sheet name is provided in config.properties file and execute the same.
	 * If sheet name is not provided then this step will be skipped.
	 * 
	 * @author Chintan Shah
	 * @param excelFilePath
	 */
	private static void executeAfterTest(String excelFilePath) {
		
		afterTestSheetName = CONFIG.getProperty("AFTER_TEST_SHEET_NAME");
		if(afterTestSheetName != null) {
			if(beforeSuiteResult) {
				if(ExcelUtils.isSheetPresent(afterTestSheetName)) {
					Log.afterTestStart(afterTestSheetName);
					executeTestCase(excelFilePath, afterTestSheetName);
					Log.afterTestend();
				}
				else {
					Log.error("Could not find sheet : "+afterTestSheetName);
					testResult = false;
				}
			}
			else {
				Log.error("Before Suite execution failed, Thus skipped after test execution");
				testResult = false;
			}
		}
	}

	/**
	 * Method to execute Before Suite sheet.
	 * System will check is sheet name is provided in config.properties file and execute the same.
	 * If sheet name is not provided in config.properties then this method will be skipped.
	 * 
	 * @author Chintan Shah
	 * @param excelFilePath
	 */
	private static void executeBeforeSuite(String excelFilePath) {
		
		beforeSuiteSheetName = CONFIG.getProperty("BEFORE_SUITE_SHEET_NAME");
		if(beforeSuiteSheetName != null) {
			if(ExcelUtils.isSheetPresent(beforeSuiteSheetName)) {
				Log.beforeSuiteStart();
				executeTestCase(excelFilePath, beforeSuiteSheetName);
				Log.beforeSuiteEnd();
			}
			else {
				Log.error("Could not find Before suite sheet : "+beforeSuiteSheetName);
				testResult = false;
			}
		}
		beforeSuiteResult = testResult == true?true:false;
	}
	
	/**
	 * Method to execute After Suite sheet.
	 * System will check is sheet name is provided in config.properties file and execute the same.
	 * If sheet name is not provided in config.properties then this method will be skipped.
	 * 
	 * @author Chintan Shah
	 * @param excelFilePath
	 */
	private static void executeAfterSuite(String excelFilePath) {
		
		afterSuiteSheetName = CONFIG.getProperty("AFTER_SUITE_SHEET_NAME");
		if(afterSuiteSheetName !=  null) {
			if(ExcelUtils.isSheetPresent(afterSuiteSheetName)) {
				Log.afterSuiteStart();
				executeTestCase(excelFilePath, afterSuiteSheetName);
				Log.afterSuiteEnd();
			}
			else {
				Log.error("Could not find after suite sheet : "+afterSuiteSheetName);
				testResult = false;
			}
		}
	}
	
	/**
	 * Method to execute complete test suite
	 * 
	 * @author Chintan Shah
	 * @param excelFilePath
	 * @param excelSheetName
	 */
	private static void runTestSuite(String excelFilePath, String excelSheetName) {
		
		testResult = true;
		executeBeforeSuite(excelFilePath);
		for(int j = 1 ; j < ExcelUtils.getLastRowNumber(excelSheetName); j++) {
			
			testResult = true;
			String testCase = ExcelUtils.getCellData(j, Constants.COL_TESTCASEID, excelSheetName);
			String run = ExcelUtils.getCellData(j, Constants.COL_RUNMODE, excelSheetName);
			
			//Create Object for Single Test
			test = new SingleTest();
			test.setTestName(testCase);
			
			//Mark all tests as skipped if before suite method fails
			if(beforeSuiteResult == false) { 
				test.setTestStatus(Constants.KEYWORD_SKIPPED);
				ExcelUtils.setTestResultInExcel(excelFilePath, "Test Cases", j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_SKIPPED);
			}
			else {
				if(run.equalsIgnoreCase("Yes")) {
					Log.startTestCase(testCase);
					executeBeforeTest(excelFilePath);
					if(testResult == false) {
						test.setTestStatus(Constants.KEYWORD_SKIPPED);
						ExcelUtils.setTestResultInExcel(excelFilePath, "Test Cases", j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_SKIPPED);
					}
					else {
						if(ExcelUtils.isSheetPresent(testCase)) {
							executeTestCase(excelFilePath, testCase);
							if(testResult) {
								test.setTestStatus(Constants.KEYWORD_PASS);
								ExcelUtils.setTestResultInExcel(excelFilePath, "Test Cases", j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_PASS);
							}
							else {
								test.setTestStatus(Constants.KEYWORD_FAIL);
								ExcelUtils.setTestResultInExcel(excelFilePath, "Test Cases", j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_FAIL);
							}
						}
						else {
							Log.error("Could not find sheet : "+testCase);
							test.setTestStatus(Constants.KEYWORD_SKIPPED);
							ExcelUtils.setTestResultInExcel(excelFilePath, "Test Cases", j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_SKIPPED);
						}
					}
					executeAfterTest(excelFilePath);
					Log.endTestCase(testCase);
				}
				else {
					test.setTestStatus(Constants.KEYWORD_SKIPPED);
					ExcelUtils.setTestResultInExcel(excelFilePath, "Test Cases", j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_SKIPPED);
				}
			}
			TestSuite.suite.add(test);
			test = null;
		}
		executeAfterSuite(excelFilePath);
	}
	
	
}
