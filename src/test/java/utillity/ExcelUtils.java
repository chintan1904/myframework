package utillity;

import static executionEngine.DriverScript.testResult;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import config.Constants;

public class ExcelUtils {
	
	private static XSSFWorkbook workbook;
	private static XSSFSheet sheet;
	private static XSSFCell cell;
	
	public static void setExcelFile(String filePath) {
		
		try {
			FileInputStream excelFile = new FileInputStream(filePath);
			workbook = new XSSFWorkbook(excelFile);
		}
		catch(Exception e) {
			Log.error("Could not load Excel file : "+filePath);
			Log.error(e.toString());
			testResult = false;
		}
	}
	
	public static String getCellData(int rowNum, int colNum, String sheetName) {

		String value;
		try {
			setWorkSheet(sheetName);
			cell = sheet.getRow(rowNum).getCell(colNum);
			value = cell.getStringCellValue();
		}
		catch(Exception e) {
			Log.error("Could not get cell value, Sheet Name : "+sheetName+" Row No : "+rowNum+"Column No : "+colNum);
			Log.error(e.toString());
			value = "";
			testResult = false;
		}
		return value;
	}
	
	private static void setWorkSheet(String sheetName) {

		sheet = workbook.getSheet(sheetName);
	}
	
	public static int getLastRowNumber(String sheetName) {

		int rowNum;
		try {
			setWorkSheet(sheetName);
			rowNum = sheet.getLastRowNum()+1;
		}
		catch(Exception e) {
			Log.error("Could not get last no from sheet : "+sheetName);
			Log.error(e.toString());
			rowNum = 0;
			testResult = false;
		}
		return rowNum;
	}
	
	public static void setTestResultInExcel(String sheetName, int rowNum, int colNum, String result) {
		
		try {
			sheet = workbook.getSheet(sheetName);
			Row row = sheet.getRow(rowNum);
			Cell cell = row.getCell(colNum,Row.RETURN_BLANK_AS_NULL);
			
			if(cell == null) {
				cell = row.createCell(colNum);
				cell.setCellValue(result);
			}
			else {
				cell.setCellValue(result);
			}
			
			FileOutputStream fout = new FileOutputStream(Constants.TESTSUITEFILEPATH);
			workbook.write(fout);
			fout.close();
		}
		catch(Exception e) {
			Log.error("Could not write result to a file, Sheet name: "+sheetName+" Row Num :"+rowNum+" Col Num :"+colNum+" Result :"+result);
			testResult = false;
		}
		
		
	}
	

}
