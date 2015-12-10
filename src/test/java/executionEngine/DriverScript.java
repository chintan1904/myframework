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

public class DriverScript {
	
	public static Method method[];
	public static Actions actions;
	public static Properties OR;
	public static String actionKeyword;
	public static String pageElement;
	public static boolean testResult = true;
	
	
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
		 
		for(int j = 1 ; j < ExcelUtils.getLastRowNumber(Constants.TESTSUITESHEETNAME); j++) {
			
			testResult = true;
			String testCase = ExcelUtils.getCellData(j, Constants.COL_TESTCASEID, Constants.TESTSUITESHEETNAME);
			String run = ExcelUtils.getCellData(j, Constants.COL_RUNMODE, Constants.TESTSUITESHEETNAME);
			if(run.equalsIgnoreCase("Yes")) {
				Log.startTestCase(testCase);
				for(int i = 1; i < ExcelUtils.getLastRowNumber(testCase) ; i++) {
					pageElement = ExcelUtils.getCellData(i, Constants.COL_PAGEELEMENT , testCase);
					actionKeyword = ExcelUtils.getCellData(i, Constants.COL_ACTIONKEYWORD, testCase);
					testResult = true;
					executeActions();

					if(testResult)
						ExcelUtils.setTestResultInExcel(testCase, i, Constants.COL_TESTSTEPRESULT, Constants.KEYWORD_PASS);
					else {
						ExcelUtils.setTestResultInExcel(testCase, i, Constants.COL_TESTSTEPRESULT, Constants.KEYWORD_FAIL);
						break;
					}
				}
				if(testResult)
					ExcelUtils.setTestResultInExcel(Constants.TESTSUITESHEETNAME, j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_PASS);
				else 
					ExcelUtils.setTestResultInExcel(Constants.TESTSUITESHEETNAME, j, Constants.COL_TESTCASERESULT, Constants.KEYWORD_FAIL);
				Log.endTestCase(testCase);
			}

			
		}
		
	}

	private static void executeActions() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		boolean methodExecuted = false;
		for(int i = 0 ; i < method.length ; i++) {
			if(method[i].getName().equals(actionKeyword)) {
				method[i].invoke(actions,pageElement);
				methodExecuted = true;
				break;
			}
		}
		if(!methodExecuted) {
			Log.error("Could not find required action :"+actionKeyword);
			Actions.close("Called forcefully");
			testResult = false;
		}
			
	}

}
