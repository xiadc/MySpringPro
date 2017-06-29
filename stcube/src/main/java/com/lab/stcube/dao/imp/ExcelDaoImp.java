package com.lab.stcube.dao.imp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.lab.stcube.dao.DataSourceDao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
public class ExcelDaoImp implements DataSourceDao{

	public List<Map<String, String>> queryAllData(String filepath) {
		// TODO Auto-generated method stub
		String fileType = filepath.substring(filepath.lastIndexOf(".")+1, filepath.length());
		InputStream is = null;
		Workbook wb = null;
		try{
			is = new FileInputStream(filepath);
			
			if(fileType.equals("xls")){
				wb = new HSSFWorkbook(is);
			}else if(fileType.equals("xlsx")){
				wb = new XSSFWorkbook(is);
			}else{
				throw new Exception("读取的不是excel文件！");
			}
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			int sheetSize = wb.getNumberOfSheets();	
			for (int i = 0; i < sheetSize; i++){      //遍历sheet页
				Sheet sheet = wb.getSheetAt(i);
				List<String> titles = new ArrayList<String>();
				int rowSize = sheet.getLastRowNum() + 1;
				for (int j = 0; j < rowSize; j++){    //遍历行
					Row row = sheet.getRow(j);
					if (row == null){                 //略过空行
						continue;
					}
					int cellSize = row.getLastCellNum();  //行中有多少个单元格
					if (j == 0){                       //标题行
						for (int k = 0; k < cellSize; k++){
							Cell cell = row.getCell(k);
							titles.add(cell.toString().trim());
						}
					}else{                             //其余行为数据行
						Map<String, String> rowMap = new HashMap<String, String>();
						for (int k=0; k < cellSize; k++){
							Cell cell = row.getCell(k);
 							String key = titles.get(k);
							String value = getCell(cell);						
							rowMap.put(key, value.trim());
						}
						list.add(rowMap);
					}
				}
			}
			return list;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (wb != null){
					wb.close();
				}
				if (is != null){
					is.close();
				}
			}catch(Exception e){	
				e.printStackTrace();
			}		
		}
		return null;
	}
	
	private String getCell(Cell cell){
		DecimalFormat df = new DecimalFormat("#");
		if (cell == null){
			return "";
		}
		switch (cell.getCellType()){
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())).toString();
			}
			return df.format(cell.getNumericCellValue());	
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue()+"";
		case Cell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue()+"";
		}
		return "";
		
	}

}