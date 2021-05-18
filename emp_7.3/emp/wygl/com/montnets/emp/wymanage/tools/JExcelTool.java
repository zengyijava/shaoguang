package com.montnets.emp.wymanage.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.montnets.emp.util.ChangeCharset;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.PhoneUtil;
public class JExcelTool {
	
	/****
	 * 解析TXT文本
	* @Description: TODO 文本处理
	* @param @param ins 输入流
	* @param @param charset
	* @param @return
	* @param @throws Exception
	* @return Map<String,String>
	 */
	public static Map<String,String> jxTxt(InputStream ins, InputStream inCopy) throws Exception{
		Map<String,String> phoneMap = new HashMap<String, String>();
		//BufferedReader reader = new BufferedReader(new InputStreamReader(ins, charset));
		BufferedReader reader = new ChangeCharset().getReader(ins,inCopy);
		PhoneUtil phoneUtil = new PhoneUtil();
		/*if(charset.startsWith("UTF-"))
		{
			reader.read(new char[1]);
		}*/
		String tmp="";
		String phone ="";
		String type = "";
		Pattern pattern = Pattern.compile("^\\s*(1\\d{10})\\s+(\\d*)\\D*$");
		int phoneType = -1;
		while ((tmp = reader.readLine()) != null)
		{
		    Matcher matcher = pattern.matcher(tmp);
			if(matcher.find()){
				phone = matcher.group(1);
				phoneType = phoneUtil.getPhoneType(phone, null);
				if(phoneType<0||phoneType>2){
					continue;
				}
				type = matcher.group(2);
				if(!type.matches("^(0001|0021|0100|0121|2100|2101)$")){
					type = "-1";
				}
				phoneMap.put(phone, type);
			}
		}
		return phoneMap;
	}

	/***
	*  Excel处理
	* @Description: TODO
	* @param @param ins
	* @param @param colum
	* @param @param exceltype
	* @param @return
	* @param @throws Exception
	* @return Map<String,String>
	 */
	public static Map<String,String> jxExcel(InputStream ins,int colum,int exceltype) throws Exception
	{
		Map<String,String> phoneMap = new HashMap<String, String>();
		PhoneUtil phoneUtil = new PhoneUtil();
		try
		{
			String phone ="";
			String type = "";
			int phoneType = -1;
			if(exceltype == 1)
			{
				HSSFWorkbook workbook = new HSSFWorkbook(ins);
				//循环每张表
				for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
					 HSSFSheet sheet = workbook.getSheetAt(sheetNum);
					// 循环每一行
					 for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++)
					 {
							HSSFRow row = sheet.getRow(rowNum);
							if (row == null||row.getLastCellNum()<2) {
								continue;
							}
							phone = getCellFormatValue(row.getCell(0));
							phoneType = phoneUtil.getPhoneType(phone, null);
							if(phoneType<0||phoneType>2){
								continue;
							}
							type = getCellFormatValue(row.getCell(1));
							if(!type.matches("^(0001|0021|0100|0121|2100|2101)$")){
								type = "-1";
							}
							phoneMap.put(phone, type);
					}
				}
			}else
			{
			
				XSSFWorkbook workbook = new XSSFWorkbook(ins);
				//循环每张表
				for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
					XSSFSheet sheet = workbook.getSheetAt(sheetNum);
					// 循环每一行
					for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++)
					{
						XSSFRow row = sheet.getRow(rowNum);
						if (row == null||row.getLastCellNum()<2) {
							continue;
						}
						phone = getCellFormatValue(row.getCell(0));
						phoneType = phoneUtil.getPhoneType(phone, null);
						if(phoneType<0||phoneType>2){
							continue;
						}
						type = getCellFormatValue(row.getCell(1));
						if(!type.matches("^(0001|0021|0100|0121|2100|2101)$")){
							type = "-1";
						}
						phoneMap.put(phone, type);
					}
				}
			}
				

			return phoneMap;
		}catch (Exception e) {
			EmpExecutionContext.error(e,"解析EXCEL异常！");
			throw e;
		}
	}
	
	/***
	 * 获得excel的单元格
	* @Description: TODO
	* @param @param cell
	* @param @return
	* @return String
	 */
	private static String getCellFormatValue(XSSFCell cell)
    {
        String cellvalue = "";
        if (cell != null) 
         {
            // 判断当前Cell的Type
            switch (cell.getCellType()) 
            {
               // 如果当前Cell的Type为NUMERIC
               case XSSFCell.CELL_TYPE_NUMERIC: 
               case XSSFCell.CELL_TYPE_FORMULA: 
               {
                  // 判断当前的cell是否为Date
                  if (DateUtil.isCellDateFormatted(cell)) 
                  {
                     // 如果是Date类型则，取得该Cell的Date值
                     Date date = cell.getDateCellValue();
                     // 把Date转换成本地格式的字符串
                     Calendar c = Calendar.getInstance();
                     c.setTime(date);	                     
                     if(c.get(Calendar.HOUR)==0 && c.get(Calendar.MINUTE)==0 && c.get(Calendar.SECOND) ==0){
                     	cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                      }else {
                     	 cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
 					 }
                  }
                  // 如果是纯数字
                  else
                  {
                     // 取得当前Cell的数值
                	  // 是否有小数部分（分开处理）
                	  if(Math.floor(cell.getNumericCellValue())==cell.getNumericCellValue())
                	  {
                		  cellvalue=String.valueOf((long)cell.getNumericCellValue());
                	  }else {
                		  cellvalue = cell.getRawValue();
					  }
                   
                  }
                  break;
               }
               // 如果当前Cell的Type为STRIN
               case XSSFCell.CELL_TYPE_STRING:
                  // 取得当前的Cell字符串
                  cellvalue = cell.getStringCellValue();
                  break;
               // 默认的Cell值
               default:
                  cellvalue = " ";
            }
         }
         else 
         {
            cellvalue = "";
         }
        return cellvalue;
    }
	
	
	/**
	 * 判断单元格格式，返回字符串Excel2003
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(HSSFCell cell)
    {
        String cellvalue = "";
        if (cell != null) 
         {
            // 判断当前Cell的Type
            switch (cell.getCellType()) 
            {
	            case HSSFCell.CELL_TYPE_NUMERIC: // 数字   
	                // 判断当前的cell是否为Date
	                  if (DateUtil.isCellDateFormatted(cell)) 
	                  {
	                     // 如果是Date类型则，取得该Cell的Date值
	                     Date date = cell.getDateCellValue();
	                     // 把Date转换成本地格式的字符串
	                     Calendar c = Calendar.getInstance();
	                     c.setTime(date);	                     
	                     if(c.get(Calendar.HOUR)==0 && c.get(Calendar.MINUTE)==0 && c.get(Calendar.SECOND) ==0){
	                    	cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
	                     }else {
	                    	 cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
						 }
	                  }
	                  // 如果是纯数字
	                  else
	                  {
	                     // 是否有小数部分（分开处理）
	                	  if(Math.floor(cell.getNumericCellValue())==cell.getNumericCellValue())
	                	  {
	                		  cellvalue=String.valueOf((long)cell.getNumericCellValue());
	                	  }else {
	                		  cellvalue = String.valueOf(cell.getNumericCellValue());
						  }
	                	  //System.out.println(cellvalue);
	                  }
	                break;   
	            case HSSFCell.CELL_TYPE_STRING: // 字符串   
	                cellvalue = cell.getStringCellValue() ; 	                       
	                break;    
	            case HSSFCell.CELL_TYPE_FORMULA: // 公式   
	                cellvalue = cell.getCellFormula();   
	                break;   
	            case HSSFCell.CELL_TYPE_BLANK: // 空值   
	            	cellvalue = " "; 
	                break;   
	            case HSSFCell.CELL_TYPE_ERROR: // 故障   
	            	cellvalue = " ";
	                break;   
	            default:   
	            	cellvalue = " ";
	                break;  
            }
         }
         else 
         {
            cellvalue = "";
         }
        return cellvalue;
    }
}
