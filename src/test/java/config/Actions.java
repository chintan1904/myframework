package config;

import static executionEngine.DriverScript.OR;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import static executionEngine.DriverScript.testResult;

import utillity.Log;

public class Actions {
	
	public static WebDriver driver;
	
	public static void openBrowser(String object) {
		
		try {
			Log.info("Open Firefox browser");
			driver = new FirefoxDriver();
		}
		catch(Exception e) {
			Log.error("Not able to initialize webdriver");
			Log.error(e.toString());
			testResult = false;
			
		}
		
		try {
			Log.info("Set Implicite wait as 10 Seconds");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		catch(Exception e) {
			Log.error("Not able to Implicite timeout for webdriver");
			Log.error(e.toString());
			testResult = false;
		}
		
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
	
	public static void navigateTo(String object) {
		
		try {
			Log.info("Navigate to URL :  "+Constants.URL);
			driver.navigate().to(Constants.URL);
		}
		catch(Exception e) {
			Log.error("Not able to navigate to URL :"+Constants.URL);
			Log.error(e.toString());
			testResult = false;
		}
		
	}
	
	public static void enterUserName(String object) {
		
		try {
			Log.info("Enter username : "+"german");
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys("german");
		}
		catch(Exception e) {
			Log.error("Not able to enter username : ");
			Log.error(e.toString());
			testResult = false;
		}
	}
	
	public static void enterPassword(String object) {
		
		try {
			Log.info("Enter username : "+"password");
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys("password");
		}
		catch(Exception e) {
			Log.error("Not able to enter password : ");
			Log.error(e.toString());
			testResult = false;
		}
	}
	
	public static void click(String object) {
		
		try {
			Log.info("Click button : "+object);
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		}
		catch(Exception e) {
			Log.error("Not able to click the button :"+object);
			Log.error(e.toString());
			testResult = false;
		}
	}
	
	public static void waitFor(String object) throws InterruptedException {
		
		try { 
			Log.info("Wait for 5 Seconds");
			Thread.sleep(5000);
		}
		catch(Exception  e) {
			Log.error("Not able to wait for 5 seconds");
			Log.error(e.toString());
			testResult = false;
		}
	}
	
	public static void close(String object) {

		try {
			Log.info("Quit driver session");
			driver.quit();
		}
		catch(Exception e) {
			Log.error("Could quit webdriver session");
			Log.error(e.toString());
			testResult = false;
		}
	}

}
