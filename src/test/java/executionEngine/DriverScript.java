package executionEngine;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;

import config.Actions;
import config.Constants;
import utillity.ExcelUtils;
import utillity.Log;
import utillity.SingleTest;
import utillity.TestSuite;

public class DriverScript {
	
	public static Method method[];
	public static Actions actions;
	public static Properties OR;
	public static String actionKeyword;
	public static String pageElement;
	public static String data;
	public static boolean testResult = true;
	public static SingleTest test;
	
	
	public DriverScript() {
	}
	
	public static void main(String[] args) throws Exception {
		
		String excelFilePath = Constants.TESTSUITEFILEPATH;
		
		DOMConfigurator.configure("log4j.xml");
		actions = new Actions();
		method = actions.getClass().getMethods();
		
		
		FileInputStream fs;
		try {
			fs = new FileInputStream(Constants.ORFILEPATH);
			OR = new Properties(System.getProperties());
			OR.load(fs);
		} catch (Exception e) {
			Log.error("Could not load OR.properties file");
			Log.error(e.toString());
			testResult = false;
		}
		ExcelUtils.setExcelFile(excelFilePath);
		
		//Clear Test Suite :
		TestSuite.suite.clear();
		
		for(int j = 1 ; j < ExcelUtils.getLastRowNumber(Constants.TESTSUITESHEETNAME); j++) {
			
			testResult = true;
			String testCase = ExcelUtils.getCellData(j, Constants.COL_TESTCASEID, Constants.TESTSUITESHEETNAME);
			String run = ExcelUtils.getCellData(j, Constants.COL_RUNMODE, Constants.TESTSUITESHEETNAME);
			
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
						ExcelUtils.setTestResultInExcel(testCase, i, Constants.COL_TESTSTEPRESULT, Constants.KEYWORD_PASS);
					else {
						ExcelUtils.setTestResultInExcel(testCase, i, Constants.COL_TESTSTEPRESULT, Constants.KEYWORD_FAIL);
						break;
					}
				}
				if(testResult) {
					test.setTestStatus(Constants.KEYWORD_PASS);
					ExcelUtils.setTestResultInExcel(Constants.TESTSUITESHEETNAME, j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_PASS);
				}
				else {
					test.setTestStatus(Constants.KEYWORD_FAIL);
					ExcelUtils.setTestResultInExcel(Constants.TESTSUITESHEETNAME, j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_FAIL);
				}
				Log.endTestCase(testCase);
			}
			else {
				test.setTestStatus(Constants.KEYWORD_SKIPPED);
			}
			TestSuite.suite.add(test);
		}
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

}
