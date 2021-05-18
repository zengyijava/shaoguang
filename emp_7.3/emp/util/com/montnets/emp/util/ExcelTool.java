package com.montnets.emp.util;


import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import jxl.*;
import jxl.read.biff.BiffException;
import jxl.read.biff.PasswordException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class ExcelTool {

	// 报表模板目录
	private String BASEDIR = "";

	// 操作员报表文件路径
    private final String voucherTemplatePath = "";

	// 发送账号下行报表文件路径
    private final String spvoucherTemplatePath = "";

	// 产生下行报表路径
    private final String voucherPath = "voucher";

	//写文件时候要的换行符
	private final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
	
	//格式化规则
	private final NumberFormat format = new DecimalFormat("#.##########");


	public ExcelTool()
	{
		
	}
	public ExcelTool(String context) {
		BASEDIR = context;
	}


	// 将设置单元格属性提取出来成一个方法，方便其他模块调用
	public XSSFCellStyle setCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();

		XSSFFont font = workbook.createFont();
		// 字体名称
		font.setFontName("TAHOMA");
		// 粗体
		font.setBold(false);
		// 下环线
		font.setUnderline(FontUnderline.NONE);
		// 字体大小
		font.setFontHeight(11);
		cellStyle.setFont(font);
		// 水平对齐
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		// 竖直对齐
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setWrapText(true);

		return cellStyle;
	}

	// 将设置单元格属性提取出来成一个方法，方便其他模块调用
	public XSSFCellStyle[] setLastCellStyle(XSSFWorkbook workbook) {

		XSSFCellStyle cellStyle1 = workbook.createCellStyle();
		XSSFCellStyle cellStyle2 = workbook.createCellStyle();

		XSSFFont font1 = workbook.createFont();
		XSSFFont font2 = workbook.createFont();
		// 字体名称
		font1.setFontName("TAHOMA");
		font2.setFontName("TAHOMA");
		// 粗体
		font1.setBold(false);
		font2.setBold(true);
		// 字体大小
		font1.setFontHeight(11);
		font2.setFontHeight(11);
		cellStyle1.setFont(font1);
		cellStyle2.setFont(font2);
		// 水平对齐
		cellStyle1.setAlignment(HorizontalAlignment.CENTER);
		cellStyle2.setAlignment(HorizontalAlignment.CENTER);
		// 竖直对齐
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle1.setWrapText(true);
		cellStyle2.setWrapText(true);

		return new XSSFCellStyle[] { cellStyle1, cellStyle2 };
	}

	/**
	 * 解析EXCEL，并读取内容
	 * @param FileStr 文件目录
	 * @param Newfile 新文件对象
	 * @param ins 文件流对象
	 * @param fileIndex 压缩文件内序号
	 * @param exceltype excel类型。1-xls，et；2-xlsx
	 * @return
	 * @throws EMPException 
	 */
	public BufferedReader jxExcel(String FileStr, InputStream ins,String fileIndex,int exceltype, PreviewParams params) throws EMPException
	{
		String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt"))+"_temp"+fileIndex+".txt";
		//添加待删除的文件名
		params.getDelFilePath().add(FileStrTemp);
		File Newfile = new File(FileStrTemp);
		if(Newfile.exists())
		{
			if(!Newfile.delete())
			{
				EmpExecutionContext.error("解析xls、et文件内容时，删除重复号码文件失败！");
			}
				
		}
		FileOutputStream fos = null;  
		OutputStreamWriter osw = null;    
		BufferedWriter bw = null;
		try
		{
			fos=new FileOutputStream(Newfile);  
			osw=new OutputStreamWriter(fos, "GBK");    
			bw=new BufferedWriter(osw);
			String phoneNum="";
			String Context="";
			
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
							if (row == null) {
								continue;
							}
							//得到第一列的电话号码
	                        phoneNum = getCellFormatValue(row.getCell(0));
	                        Context="";
	                        if(params.getSendType() > 1)
	                        {
		                        //循环每一列（内容以,分隔开来）
								for (int k = 1; k < row.getLastCellNum(); k++) {   
				                        HSSFCell cell = row.getCell(k); 
				                        if(cell !=null && cell.toString().length() >0)
				                        {
										  Context +=","+getCellFormatValue(cell);
				                        }
										
								 }	
	                        }
	                        //一行一行的将内容写入到txt文件中。
							bw.write(phoneNum+Context+line);
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
						if (row == null) {
							continue;
						}
	                    phoneNum =  getCellFormatValue(row.getCell(0));
	                    Context="";
	                    if(params.getSendType() > 1)
                        {
	                    	for (int k = 1; k < row.getLastCellNum(); k++) {   
		                        XSSFCell cell = row.getCell(k); 
		                        if(cell !=null && cell.toString().length() >0)
		                        {
								  Context +=","+getCellFormatValue(cell);		
		                        }
	                    	}
                        }
						bw.write(phoneNum+Context+line);
					}
				}
			}
	        FileInputStream fis = new FileInputStream(Newfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis,"GBK"));

			return br;
		}catch (Exception e) {
			EmpExecutionContext.error(e,"解析EXCEL异常！");
			throw new EMPException(IErrorCode.B20004,e);
		}finally
		{
			if(bw != null)
			{
				try
				{
					bw.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析EXCEL关闭字符输出流异常！");
				}   
			}
			if(osw != null)
			{
				try
				{
					osw.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析EXCEL关闭字符输出流通道异常！");
				}    
			}
			if(fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析EXCEL关闭文件输出流异常！");
				}
			}
		}
	}
	
	/**
	 * 
	 * @description    解析excel2007文件
	 * @param fileUrl 上传文件路径
	 * @param FileStr 文件存在目录
	 * @param fileIndex 压缩文件内序号
	 * @param params  预览对象
	 * @param sheetId 遍历的sheet索引，-1为遍历所有sheet
	 * @return
	 * @throws EMPException       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-6 上午10:16:25
	 */
	public BufferedReader jxExcel2007(String FileStr, InputStream ins, String fileIndex, PreviewParams params, int sheetId) throws EMPException
	{
		try
		{
			return new Excel2007Reader().fileParset(FileStr, ins, fileIndex, params, sheetId);
		}
		catch(Exception e) 
		{
			//add by denglj 2018.11.29
		    if(ins!=null){
		        try {
                    ins.close();
                }
                catch (IOException e1) {
                    EmpExecutionContext.error(e1,"解析EXCEL2007文件关闭流异常！文件名："+ params.getFileName());
                }
		    }
			EmpExecutionContext.error(e,"解析EXCEL2007文件异常！文件名："+ params.getFileName());
			throw new EMPException(IErrorCode.B20004,e);
		}
		
	}
	
	/**
	 * 
	 * @description    解析excel2007文件，InvalidFormatException异常后返回null
	 * @param fileUrl 上传文件路径
	 * @param FileStr 文件存在目录
	 * @param fileIndex 压缩文件内序号
	 * @param params  预览对象
	 * @param sheetId 遍历的sheet索引，-1为遍历所有sheet
	 * @return 
	 * @throws EMPException       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-6 上午10:16:25
	 */
	public BufferedReader jxExcel2007_V1(String FileStr, InputStream ins, String fileIndex, PreviewParams params, int sheetId) throws EMPException
	{
		try
		{
			return new Excel2007Reader().fileParset(FileStr, ins, fileIndex, params, sheetId);
		}
		catch(Exception e) 
		{
			  //add by denglj 2018.11.29,不能在fianlly里面关
            if(ins!=null){
                try {
                    ins.close();
                }
                catch (IOException e1) {
                    EmpExecutionContext.error(e1,"解析EXCEL2007文件关闭流异常！文件名："+ params.getFileName());
                }
            }
            
			EmpExecutionContext.error(e,"解析EXCEL2007文件异常！文件名："+ params.getFileName());
			if(e instanceof InvalidFormatException)
			{
				return null;
			}
			else
			{
				throw new EMPException(IErrorCode.B20004,e);
			}
		}
		
	}
	
	/**
	 * 解析excel2003文件
	 * @description    
	 * @param FileStr  文件存在目录
	 * @param ins	上传文件流
	 * @param fileIndex  压缩文件内序号
	 * @param params  预览对象
	 * @return
	 * @throws EMPException       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-9-26 下午02:12:51
	 */
	public BufferedReader jxExcel2003(String FileStr, InputStream ins, String fileIndex, PreviewParams params) throws EMPException
	{
		String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt"))+"_temp"+fileIndex+".txt";
		//添加待删除的文件名
		params.getDelFilePath().add(FileStrTemp);
		File Newfile = new File(FileStrTemp);
		String fileName = params.getFileName();
		if(Newfile.exists())
		{
			if(!Newfile.delete())
			{
				EmpExecutionContext.error("解析xls、et文件内容时，删除重复号码文件失败！fileName:"+fileName);
			}
		}
		FileOutputStream fos = null;  
		OutputStreamWriter osw = null;    
		BufferedWriter bw = null;
		Workbook workbook = null; 
		try
		{
			fos=new FileOutputStream(Newfile);  
			osw=new OutputStreamWriter(fos, "GBK");    
			bw=new BufferedWriter(osw);
			StringBuilder ssc=new StringBuilder();
			workbook = Workbook.getWorkbook(ins); 
			if(workbook != null)
			{
				String phoneNum="";
				String Context = "";
				int sheetNumber = workbook.getNumberOfSheets();
				//遍历sheet
				for(int k=0; k<sheetNumber;k++)
				{
					Sheet sheet = workbook.getSheet(k); 
					Cell cell = null; 
					int columnCount=sheet.getColumns(); 
					int rowCount=sheet.getRows(); 
					//遍历行
					for (int i = 0; i <rowCount; i++) 
					{ 
						//获取第一列的电话号码
						phoneNum=getCellFormatValueToJXL(sheet.getCell(0, i)); 
						Context = "";
						//不同内容发送
						if(params.getSendType() > 1)
						{
							//遍历列
							for (int j = 1; j <columnCount; j++)
							{ 
								//这里的两个参数，第一个是表示列的，第二才表示行 
								cell=sheet.getCell(j, i); 
								//Context += "," + cell.getContents();
								Context += "," + getCellFormatValueToJXL(cell).replaceAll("•", "·").replace("¥", "￥");
	
							} 
						}
						ssc.append(phoneNum + Context + line);
						//1000行写一次文件
						if(i % 1000 == 0)
						{
							bw.write(ssc.toString());
							ssc.setLength(0);
						}
					}
				}
				if(ssc.length() > 0)
				{
					bw.write(ssc.toString());
					ssc.setLength(0);				
				}
			}
			FileInputStream fis = new FileInputStream(Newfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis,"GBK"));
			return br;
		}catch (Exception e) {
			if (e instanceof PasswordException)
			{
				EmpExecutionContext.error(e,"解析EXCEL2003或et文件失败！文件'"+fileName+"'为加密文件，无法解析。");
				throw new EMPException(IErrorCode.B20038+fileName,e);
			}
			else
			{
				EmpExecutionContext.error(e,"解析EXCEL2003或et文件异常！fileName:"+fileName);
				throw new EMPException(IErrorCode.B20004,e);
			}
		}finally
		{
			if(workbook != null)
			{
				workbook.close();
			}
			if(bw != null)
			{
				try
				{
					bw.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析EXCEL关闭字符输出流异常！fileName:"+fileName);
				}   
			}
			if(osw != null)
			{
				try
				{
					osw.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析EXCEL关闭字符输出流通道异常！fileName:"+fileName);
				}    
			}
			if(fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析EXCEL关闭文件输出流异常！fileName:"+fileName);
				}
			}
		}
	}
	
	/**
	 * 使用解析excel2003文件，BiffException异常返回NULL
	 * @description    
	 * @param FileStr  文件存在目录
	 * @param ins	上传文件流
	 * @param fileIndex  压缩文件内序号
	 * @param params  预览对象
	 * @return
	 * @throws EMPException       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-9-26 下午02:12:51
	 */
	public BufferedReader jxExcel2003_V1(String FileStr, InputStream ins, String fileIndex, PreviewParams params) throws EMPException
	{
		String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt"))+"_temp"+fileIndex+".txt";
		//添加待删除的文件名
		params.getDelFilePath().add(FileStrTemp);
		File Newfile = new File(FileStrTemp);
		String fileName = params.getFileName();
		if(Newfile.exists())
		{
			if(!Newfile.delete())
			{
				EmpExecutionContext.error("解析xls、et文件内容时，删除重复号码文件失败！fileName:"+fileName);
			}
		}
		FileOutputStream fos = null;  
		OutputStreamWriter osw = null;    
		BufferedWriter bw = null;
		Workbook workbook = null; 
		try
		{
			fos=new FileOutputStream(Newfile);  
			osw=new OutputStreamWriter(fos, "GBK");    
			bw=new BufferedWriter(osw);
			StringBuffer ssc=new StringBuffer();
			workbook = Workbook.getWorkbook(ins); 
			if(workbook != null)
			{
				String phoneNum="";
				String Context = "";
				int sheetNumber = workbook.getNumberOfSheets();
				//遍历sheet
				for(int k=0; k<sheetNumber;k++)
				{
					Sheet sheet = workbook.getSheet(k); 
					Cell cell = null; 
					int columnCount=sheet.getColumns(); 
					int rowCount=sheet.getRows(); 
					//遍历行
					for (int i = 0; i <rowCount; i++) 
					{ 
						//获取第一列的电话号码
						phoneNum=getCellFormatValueToJXL(sheet.getCell(0, i)); 
						Context = "";
						//不同内容发送
						if(params.getSendType() > 1)
						{
							//遍历列
							for (int j = 1; j <columnCount; j++)
							{ 
								//这里的两个参数，第一个是表示列的，第二才表示行 
								cell=sheet.getCell(j, i); 
								//Context += "," + cell.getContents();
								Context += "," + getCellFormatValueToJXL(cell).replaceAll("•", "·").replace("¥", "￥");
	
							} 
						}
						ssc.append(phoneNum + Context + line);
						//1000行写一次文件
						if(i % 1000 == 0)
						{
							bw.write(ssc.toString());
							ssc.setLength(0);
						}
					}
				}
				if(ssc.length() > 0)
				{
					bw.write(ssc.toString());
					ssc.setLength(0);				
				}
			}
			FileInputStream fis = new FileInputStream(Newfile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis,"GBK"));
			return br;
		}catch (Exception e) {
			if (e instanceof PasswordException)
			{
				EmpExecutionContext.error(e,"解析EXCEL2003或et文件失败！文件'"+fileName+"'为加密文件，无法解析。");
				throw new EMPException(IErrorCode.B20038+fileName,e);
			}
			else if(e instanceof BiffException)
			{
				if(e.getMessage().indexOf("Unable to recognize OLE stream") >= 0)
				{
					EmpExecutionContext.error(e,"解析EXCEL2003或et文件失败，文件名:"+fileName);
					return null;
				}
				else if(e.getMessage().indexOf("The input file was not found") >= 0)
				{
					EmpExecutionContext.error(e,"解析EXCEL2003或et文件失败！'"+fileName+"'文件不存在。");
					throw new EMPException(IErrorCode.B20042+fileName,e);
				}
				else
				{
					EmpExecutionContext.error(e,"解析EXCEL2003或et文件异常！文件名:"+fileName);
					throw new EMPException(IErrorCode.B20004,e);
				}
			}
			else
			{
				EmpExecutionContext.error(e,"解析EXCEL2003或et文件异常！文件名:"+fileName);
				throw new EMPException(IErrorCode.B20004,e);
			}
		}finally
		{
			if(workbook != null)
			{
				workbook.close();
			}
			if(bw != null)
			{
				try
				{
					bw.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析EXCEL关闭字符输出流异常！文件名:"+fileName);
				}   
			}
			if(osw != null)
			{
				try
				{
					osw.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析EXCEL关闭字符输出流通道异常！文件名:"+fileName);
				}    
			}
			if(fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析EXCEL关闭文件输出流异常！文件名:"+fileName);
				}
			}
		}
	}
	
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
                  cellvalue = cell.getStringCellValue().replaceAll(",", StaticValue.EXECL_SPLID);
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
	                cellvalue = cell.getStringCellValue().replaceAll(",", StaticValue.EXECL_SPLID); 	                       
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
	
	/**
	 * 判断单元格格式，返回字符串Excel2003
	 * @description    
	 * @param cell
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-10-27 上午08:44:53
	 */
	private String getCellFormatValueToJXL(Cell cell)
	{
		String cellvalue = "";
		CellType cellType= cell.getType();
		
        //要根据单元格的类型分别做处理，否则格式化过的内容可能会不正确 
        if(cellType==CellType.NUMBER || cellType==CellType.NUMBER_FORMULA){ 
        	cellvalue = format.format(Double.valueOf(((NumberCell)cell).getValue()), new StringBuffer(), new FieldPosition(0)).toString(); 
        } 
        else if(cellType==CellType.DATE||cellType==CellType.DATE_FORMULA)
        {
        	cellvalue = cell.getContents().replace("\"", ""); 
        }
        else{ 
        	
        	cellvalue = cell.getContents(); 
        }

		//单元格内容不为空，则将\n替换为空格
		if(cellvalue!=null&&cellvalue.trim().length()>0){
			//Excel2003文件上传，将\n替换为空格
			cellvalue=cellvalue.replace("\n"," ");
		}
		
		return cellvalue.replaceAll(",", StaticValue.EXECL_SPLID);
	}
	
	/**
	 * 
	 * 是否Excel2003文件
	 * @description    
	 * @param is  文件流
	 * @param fileName  文件名
	 * @return  true: Excel2003文件;false:不是Excel2003文件    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-8-29 下午07:30:58
	 */
	public boolean isExcel2003(InputStream is, String fileName) 
	{
	    try {
	    	new HSSFWorkbook(is);
	    } 
	    catch (Exception e) 
	    {
	      EmpExecutionContext.info("检查文件excel文件类型，文件'"+fileName+"'为非Excel2003格式");	
	      return false;
	    }
	    return true;
  }


	/**
	 * 是否Excel2007文件
	 * @description    
	 * @param is  文件流
	 * @param fileName  文件名
	 * @return  true: Excel2007文件;false:不是Excel2007文件    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-8-29 下午07:30:58
	 */
	public boolean isExcel2007(InputStream is, String fileName) 
	{
	    try {
	      new XSSFWorkbook(is);
	    } 
	    catch (Exception e)
	    {
	      EmpExecutionContext.info("检查文件excel文件类型，文件'"+fileName+"'为非Excel2003格式");	
	      return false;
	    }
	    return true;
	  }
}