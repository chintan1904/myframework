package utillity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestSuite {
	
	public static List<SingleTest> suite = new ArrayList<SingleTest>();
	
	public static void generateReport() {
		
		File f = new File("Result.html");
		FileWriter fout;
		BufferedWriter bw;
		if(f.exists())
			f.delete();
		try {
			f.createNewFile();
		} catch (IOException e) {
		
		}
		
		try {
			fout = new FileWriter(f.getAbsoluteFile());
			bw = new BufferedWriter(fout);
			
			bw.write("<html>");
			bw.append("<head></head>");
			bw.append("<body>");
			bw.append("<table border = '1'>");
			bw.append("<tr><th><B>Test Case</B></th><th><B>Test Status</B></th><th><B>Test Log</B></th></tr>");
			for (SingleTest singleTest : suite) {
				String content = "<tr><td>"+singleTest.getTestName()+"</td><td>"+singleTest.getTestStatus()+"</td><td>"+singleTest.getTestLog()+"</td></tr>";
				bw.append(content);
			}
			bw.append("</table>");
			bw.append("</body>");
			bw.append("</html>");
			
			bw.close();
			fout.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
