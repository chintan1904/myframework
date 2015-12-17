package utillity;

import java.io.FileInputStream;
import java.util.Properties;
import executionEngine.DriverScript;

public class PropertyFile {
	
	private static Properties properties = null;
	
	public static Properties load(String filePath) {

		FileInputStream fs;
		try {
			fs = new FileInputStream(filePath);
			properties = new Properties(System.getProperties());
			properties.load(fs);
		} catch (Exception e) {
			Log.error("Could not load OR.properties file");
			Log.error(e.toString());
			DriverScript.configFileLoad = false;
		}

		return properties;
	}

}
