package com.agileach.httpclient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelProcess {
	public static Object[][] proessExcelLessThan2010(String filePath, int sheetId) throws IOException {
		// 数据流读入excel
		File file = new File(System.getProperty("user.dir") + filePath);
		FileInputStream fis = new FileInputStream(file);
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		// 读取特定表单并计算行列数
		HSSFSheet sheet = wb.getSheetAt(sheetId);
		int numberOfRow = sheet.getPhysicalNumberOfRows();
		int numberOfCell = sheet.getRow(0).getLastCellNum();
		// 将表单数据处理存入dtt对象
		Object[][] dttData = new Object[numberOfRow-1][numberOfCell];
		for (int i = 1; i < numberOfRow; i++) {
			if (null == sheet.getRow(i) || "".equals(sheet.getRow(i))) {
				continue;
			}
			for (int j = 0; j < numberOfCell; j++) {
				if (null == sheet.getRow(i).getCell(j) || "".equals(sheet.getRow(i).getCell(j))) {
					continue;
				}
				HSSFCell cell = sheet.getRow(i).getCell(j);
				cell.setCellType(CellType.STRING);
				dttData[i-1][j] = cell.getStringCellValue();
			}
		}
		return dttData;
	}
	
	public static Iterator<String[]> proessExcel(String filePath, int sheetId) throws IOException {			
		InputStream is = new FileInputStream(filePath);
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		// 获得工作表
		XSSFSheet sheet = workbook.getSheetAt(sheetId);
		XSSFCell cell = null;
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		List<String[]> records = new ArrayList<String[]>();
		for (int i = 1; i <= rowNum; i++) {
			// 当前行
			XSSFRow row = sheet.getRow(i);
			//得到总列数
			int colNum = row.getLastCellNum();
			String[] data = new String[colNum];
			for (int j = 0; j < colNum; j++) {
				cell = row.getCell(j);
				data[j] = cell.getCellType() == CellType.STRING ? cell.getStringCellValue()
						: String.valueOf(cell.getNumericCellValue());				
			}
			records.add(data);
		}
		workbook.close();
		return records.iterator();	
	}	
	
	public static Object[][] proessExcelGreatThan2010(String filePath, int sheetId) throws IOException {
		InputStream fis = new FileInputStream(filePath);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sh = wb.getSheetAt(sheetId);
		int numberrow = sh.getPhysicalNumberOfRows();
		int numbercell = sh.getRow(0).getLastCellNum();

		Object[][] dttData = new Object[numberrow - 1][numbercell];
		for (int i = 1; i < numberrow; i++) {
			if (null == sh.getRow(i) || "".equals(sh.getRow(i))) {
				continue;
			}
			for (int j = 0; j < numbercell; j++) {
				if (null == sh.getRow(i).getCell(j) || "".equals(sh.getRow(i).getCell(j))) {
					continue;
				}
				XSSFCell cell = sh.getRow(i).getCell(j);
				cell.setCellType(CellType.STRING);
				dttData[i - 1][j] = cell.getStringCellValue();
			}
		}
		wb.close();
		return dttData;
	}
}