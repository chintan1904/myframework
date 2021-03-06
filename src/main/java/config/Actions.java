package config;

import static executionEngine.DriverScript.OR;
import static executionEngine.DriverScript.driver;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.firefox.FirefoxDriver;
import static executionEngine.DriverScript.testResult;

import utillity.Helper;
import utillity.Log;
import utillity.ScreenCapture;

public class Actions {
	
	
	public static void openBrowser(String object, String data) {
		
		try {
			Log.info("Open Firefox browser");
			driver = new FirefoxDriver();
			ScreenCapture.take();
		}
		catch(Exception e) {
			Log.error("Not able to initialize webdriver");
			Log.error(e.toString());
			testResult = false;
			
		}
	}
	
	public static void setImpliciteWait(String object, String data) {

		try {
			Log.info("Set Implicite wait as "+data+" Seconds");
			int time = Integer.parseInt(data);
			driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
		}
		catch(Exception e) {
			Log.error("Not able to Implicite timeout for webdriver");
			Log.error(e.toString());
			testResult = false;
		}
	}
	
	public static void maximizeWindow(String object, String data) {
		
		try {
			Log.info("Maximize browser window");
			driver.manage().window().maximize();
		}
		catch(Exception e) {
			Log.error("Not able to maximize browser window");
			Log.error(e.toString());
			testResult = false;
		}
	}
	
	public static void navigateTo(String object, String data) {
		
		try {
			Log.info("Navigate to URL :  "+data);
			driver.navigate().to(data);
			ScreenCapture.take();
		}
		catch(Exception e) {
			Log.error("Not able to navigate to URL :"+data);
			Log.error(e.toString());
			testResult = false;
		}
		
	}
	
	public static void enterText(String object, String data) {
		
		try {
			Log.info("Enter Text : "+data+" in field : "+object);
			Helper.locateElement(OR.getProperty(object)).sendKeys(data);
			//driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
			ScreenCapture.take();
		}
		catch(Exception e) {
			Log.error("Not able to enter text : "+data+" in field : "+object);
			Log.error(e.toString());
			testResult = false;
		}
	}
	
	public static void click(String object, String data) {
		
		try {
			Log.info("Click button : "+object);
			Helper.locateElement(OR.getProperty(object)).click();
			//driver.findElement(By.xpath(OR.getProperty(object))).click();
			ScreenCapture.take();
		}
		catch(Exception e) {
			Log.error("Not able to click the button :"+object);
			Log.error(e.toString());
			testResult = false;
		}
	}
	
	public static void waitFor(String object, String data) throws InterruptedException {
		
		try { 
			Log.info("Wait for 5 Seconds");
			Thread.sleep(5000);
			ScreenCapture.take();
		}
		catch(Exception  e) {
			Log.error("Not able to wait for 5 seconds");
			Log.error(e.toString());
			testResult = false;
		}
	}
	
	public static void close(String object, String data) {

		try {
			Log.info("Quit driver session");
			driver.manage().deleteAllCookies();
			driver.quit();
		}
		catch(Exception e) {
			Log.error("Could not quit webdriver session");
			Log.error(e.toString());
			testResult = false;
		}
	}
}
