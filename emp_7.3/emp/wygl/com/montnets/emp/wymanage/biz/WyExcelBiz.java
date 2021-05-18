package com.montnets.emp.wymanage.biz;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.context.EmpExecutionContext;

public class WyExcelBiz  {
 
    /**
     * 解析EXCEL，并读取内容
     *
     * @param ins       文件流对象
     * @param fileIndex 压缩文件内序号
     * @param exceltype excel类型。1-xls，et；2-xlsx
     * @return
     * @throws EMPException
     */
   
    public List<String> jxExcel(InputStream ins, String fileIndex, int exceltype) throws Exception {
        List<String> phoneList = new ArrayList<String>();
        try {
            String phoneNum = "";
            if (exceltype == 1) {
                HSSFWorkbook workbook = new HSSFWorkbook(ins);
                //循环每张表
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                    HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                    // 循环每一行
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        HSSFRow row = sheet.getRow(rowNum);
                        if (row == null) {
                            continue;
                        }
                        //得到第一列的电话号码
                        phoneNum = getCellFormatValue(row.getCell(0));
                        phoneList.add(phoneNum);
                    }
                }
            } else {

                XSSFWorkbook workbook = new XSSFWorkbook(ins);
                //循环每张表
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                    XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                    // 循环每一行
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        XSSFRow row = sheet.getRow(rowNum);
                        if (row == null) {
                            continue;
                        }
                        phoneNum = getCellFormatValue(row.getCell(0));
                        phoneList.add(phoneNum);
                    }
                }
            }


            return phoneList;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "解析EXCEL异常！");
            throw e;
        }
    }

    private static String getCellFormatValue(XSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case XSSFCell.CELL_TYPE_NUMERIC:
                case XSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，取得该Cell的Date值
                        Date date = cell.getDateCellValue();
                        // 把Date转换成本地格式的字符串
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        if (c.get(Calendar.HOUR) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0) {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        }
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        // 是否有小数部分（分开处理）
                        if (Math.floor(cell.getNumericCellValue()) == cell.getNumericCellValue()) {
                            cellvalue = String.valueOf((long) cell.getNumericCellValue());
                        } else {
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
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }


    /**
     * 判断单元格格式，返回字符串Excel2003
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，取得该Cell的Date值
                        Date date = cell.getDateCellValue();
                        // 把Date转换成本地格式的字符串
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        if (c.get(Calendar.HOUR) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0) {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        }
                    }
                    // 如果是纯数字
                    else {
                        // 是否有小数部分（分开处理）
                        if (Math.floor(cell.getNumericCellValue()) == cell.getNumericCellValue()) {
                            cellvalue = String.valueOf((long) cell.getNumericCellValue());
                        } else {
                            cellvalue = String.valueOf(cell.getNumericCellValue());
                        }
                        //System.out.println(cellvalue);
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING: // 字符串
                    cellvalue = cell.getStringCellValue();
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
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }
}
