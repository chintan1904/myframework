package utillity;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import static executionEngine.DriverScript.driver;
import static executionEngine.DriverScript.testCase;
import static executionEngine.DriverScript.CONFIG;

public class ScreenCapture {
	
	public static String rootFolderName = "Screens";
	public static String currentTestFolderName = "";
	public static String currentTestStepName;
	public static int step = 0;
	
	private static void createRootFolder() {
		File f = new File(rootFolderName);
		if(!f.exists())
			f.mkdir();
	}
	
	private static void createTestFolder() {
		File f = new File(rootFolderName +"\\"+currentTestFolderName);
		if(!f.exists())
			f.mkdir();
	}
	
	private static void captureScreenShot() {
		TakesScreenshot tsDriver = (TakesScreenshot)driver;
		File screen = tsDriver.getScreenshotAs(OutputType.FILE);
		File dest = new File(rootFolderName+"\\"+currentTestFolderName+"\\"+currentTestStepName);
		try {
			FileUtils.copyFile(screen, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void take() {
		
		if(CONFIG.getProperty("TAKE_SCREENSHOT").equalsIgnoreCase("Yes"))
		{
			createRootFolder();
			if(currentTestFolderName != testCase) {
				currentTestFolderName = testCase; 
				createTestFolder();
				step = 1;
			}
			else 
				step++;
			currentTestStepName = step+".jpg";
			captureScreenShot();
			}
		}
}
