package executionEngine;

import java.lang.reflect.InvocationTargetException;
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
	
	public static void main(String[] args) throws Exception {
		
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

	
	private static void executeActions() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		boolean methodExecuted = false;
		for(int i = 0 ; i < method.length ; i++) {
			if(method[i].getName().equals(actionKeyword)) {
				method[i].invoke(actions,pageElement,data);
				methodExecuted = true;
				break;
			}
		}
		if(!methodExecuted) {
			Log.error("Could not find required action :"+actionKeyword);
			Actions.close("Called forcefully",data);
			testResult = false;
		}
			
	}
	
	private static void runTestSuite(String excelFilePath, String excelSheetName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for(int j = 1 ; j < ExcelUtils.getLastRowNumber(excelSheetName); j++) {
			
			testResult = true;
			String testCase = ExcelUtils.getCellData(j, Constants.COL_TESTCASEID, excelSheetName);
			String run = ExcelUtils.getCellData(j, Constants.COL_RUNMODE, excelSheetName);
			
			//Create Object for Single Test
			test = new SingleTest();
			test.setTestName(testCase);
			
			if(run.equalsIgnoreCase("Yes")) {
				Log.startTestCase(testCase);
				for(int i = 1; i < ExcelUtils.getLastRowNumber(testCase) ; i++) {
					pageElement = ExcelUtils.getCellData(i, Constants.COL_PAGEELEMENT , testCase);
					actionKeyword = ExcelUtils.getCellData(i, Constants.COL_ACTIONKEYWORD, testCase);
					data = ExcelUtils.getCellData(i, Constants.COL_TESTSTEPDATA, testCase);
					testResult = true;
					executeActions();

					if(testResult)
						ExcelUtils.setTestResultInExcel(excelFilePath, testCase, i, Constants.COL_TESTSTEPRESULT, Constants.KEYWORD_PASS);
					else {
						ExcelUtils.setTestResultInExcel(excelFilePath, testCase, i, Constants.COL_TESTSTEPRESULT, Constants.KEYWORD_FAIL);
						break;
					}
				}
				if(testResult) {
					test.setTestStatus(Constants.KEYWORD_PASS);
					ExcelUtils.setTestResultInExcel(excelFilePath, excelSheetName, j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_PASS);
				}
				else {
					test.setTestStatus(Constants.KEYWORD_FAIL);
					ExcelUtils.setTestResultInExcel(excelFilePath, excelSheetName, j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_FAIL);
				}
				Log.endTestCase(testCase);
			}
			else {
				test.setTestStatus(Constants.KEYWORD_SKIPPED);
			}
			TestSuite.suite.add(test);
		}
	}

}
