package utillity;

import config.Constants;

public class SingleTest {
	
	private String testStatus;
	private StringBuffer testLog;
	private String testName;
	
	public SingleTest() {
		// TODO Auto-generated constructor stub
		testStatus = Constants.KEYWORD_STARTED;
		testLog = new StringBuffer();
	}
	
	public String getTestStatus() {
		return testStatus;
	}
	
	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}
	
	public String getTestLog() {
		return testLog.toString();
	}
	
	public void append(String message) {
		testLog.append(message);
		testLog.append(System.lineSeparator());
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
	
}
