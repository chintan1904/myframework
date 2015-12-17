package utillity;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static config.Actions.driver;

public class Helper {

	public static WebElement locateElement(String property) {
		
		String[] locator;
		locator = property.split("\\$\\$");
		
		WebElement e = null;

		switch(locator[0]) {
		case "ID":
			e = driver.findElement(By.id(locator[1]));
			break;
		case "XPATH":
			e = driver.findElement(By.xpath(locator[1]));
			break;
		case "CSS":
			e = driver.findElement(By.cssSelector(locator[1]));
			break;
		default:
			Log.error("Could not locate element : "+property);
			break;
		}
		
		return e;
	}
	
}
