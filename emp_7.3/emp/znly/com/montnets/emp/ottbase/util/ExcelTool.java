package com.montnets.emp.ottbase.util;

import com.montnets.emp.ottbase.constant.WXStaticValue;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExcelTool
{

    // 报表模板目录
    private String  BASEDIR               = "";

    // 操作员报表文件路径
    private final String  voucherTemplatePath   = "";

    // 发送账号下行报表文件路径
    private final String  spvoucherTemplatePath = "";

    // 产生下行报表路径
    private final String  voucherPath           = "voucher";

    private final String  SAMPLES               = "temp";

    // 写文件时候要的换行符
    private final String line  = WXStaticValue.getSystemProperty().getProperty(WXStaticValue.LINE_SEPARATOR);

    public ExcelTool()
    {

    }

    public ExcelTool(String context)
    {

        BASEDIR = context;

    }

    // 将设置单元格属性提取出来成一个方法，方便其他模块调用
    public XSSFCellStyle setCellStyle(XSSFWorkbook workbook)
    {
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
    public XSSFCellStyle[] setLastCellStyle(XSSFWorkbook workbook)
    {

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

        return new XSSFCellStyle[] {cellStyle1,cellStyle2};
    }

    /**
     * 解析EXCEL，并读取内容
     * 
     * @param FileStr
     *        文件目录
     * @param Newfile
     *        新文件对象
     * @param ins
     *        文件流对象
     * @param fileIndex
     *        压缩文件内序号
     * @param exceltype
     *        excel类型。1-xls，et；2-xlsx
     * @return
     * @throws OttException
     */
   /* public BufferedReader jxExcel(String FileStr, InputStream ins, String fileIndex, int exceltype, PreviewParams params) throws OttException
    {
        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + fileIndex + ".txt";
        // 添加待删除的文件名
        params.getDelFilePath().add(FileStrTemp);
        File Newfile = new File(FileStrTemp);
        if(Newfile.exists())
        {
            Newfile.delete();
        }

        try
        {
            FileOutputStream fos = new FileOutputStream(Newfile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
            BufferedWriter bw = new BufferedWriter(osw);
            String phoneNum = "";
            String Context = "";

            if(exceltype == 1)
            {
                HSSFWorkbook workbook = new HSSFWorkbook(ins);
                // 循环每张表
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++)
                {
                    HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                    // 循环每一行
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++)
                    {
                        HSSFRow row = sheet.getRow(rowNum);
                        if(row == null)
                        {
                            continue;
                        }
                        // 得到第一列的电话号码
                        phoneNum = getCellFormatValue(row.getCell(0));
                        Context = "";
                        if(params.getSendType() > 1)
                        {
                            // 循环每一列（内容以,分隔开来）
                            for (int k = 1; k < row.getLastCellNum(); k++)
                            {
                                HSSFCell cell = row.getCell(k);
                                if(cell != null && cell.toString().length() > 0)
                                {
                                    Context += "," + getCellFormatValue(cell);
                                }

                            }
                        }
                        // 一行一行的将内容写入到txt文件中。
                        bw.write(phoneNum + Context + line);
                    }
                }
            }
            else
            {

                XSSFWorkbook workbook = new XSSFWorkbook(ins);
                // 循环每张表
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++)
                {
                    XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                    // 循环每一行
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++)
                    {
                        XSSFRow row = sheet.getRow(rowNum);
                        if(row == null)
                        {
                            continue;
                        }
                        phoneNum = getCellFormatValue(row.getCell(0));
                        Context = "";
                        if(params.getSendType() > 1)
                        {
                            for (int k = 1; k < row.getLastCellNum(); k++)
                            {
                                XSSFCell cell = row.getCell(k);
                                if(cell != null && cell.toString().length() > 0)
                                {
                                    Context += "," + getCellFormatValue(cell);
                                }
                            }
                        }
                        bw.write(phoneNum + Context + line);
                    }
                }
            }

            bw.close();
            osw.close();
            fos.close();

            FileInputStream fis = new FileInputStream(Newfile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));

            return br;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "解析EXCEL异常！");
            throw new OttException(IErrorCode.B10004, e);
        }
    }

    private static String getCellFormatValue(XSSFCell cell)
    {
        String cellvalue = "";
        if(cell != null)
        {
            // 判断当前Cell的Type
            switch (cell.getCellType())
            {
                // 如果当前Cell的Type为NUMERIC
                case XSSFCell.CELL_TYPE_NUMERIC:
                case XSSFCell.CELL_TYPE_FORMULA:
                    {
                        // 判断当前的cell是否为Date
                        if(DateUtil.isCellDateFormatted(cell))
                        {
                            // 如果是Date类型则，取得该Cell的Date值
                            Date date = cell.getDateCellValue();
                            // 把Date转换成本地格式的字符串
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);
                            if(c.get(Calendar.HOUR) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0)
                            {
                                cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                            }
                            else
                            {
                                cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                            }
                        }
                        // 如果是纯数字
                        else
                        {
                            // 取得当前Cell的数值
                            // 是否有小数部分（分开处理）
                            if(Math.floor(cell.getNumericCellValue()) == cell.getNumericCellValue())
                            {
                                cellvalue = String.valueOf((long) cell.getNumericCellValue());
                            }
                            else
                            {
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
*/
    /**
     * 判断单元格格式，返回字符串Excel2003
     * 
     * @param cell
     * @return
     */
    private static String getCellFormatValue(HSSFCell cell)
    {
        String cellvalue = "";
        if(cell != null)
        {
            // 判断当前Cell的Type
            switch (cell.getCellType())
            {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    // 判断当前的cell是否为Date
                    if(DateUtil.isCellDateFormatted(cell))
                    {
                        // 如果是Date类型则，取得该Cell的Date值
                        Date date = cell.getDateCellValue();
                        // 把Date转换成本地格式的字符串
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        if(c.get(Calendar.HOUR) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0)
                        {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        }
                        else
                        {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        }
                    }
                    // 如果是纯数字
                    else
                    {
                        // 是否有小数部分（分开处理）
                        if(Math.floor(cell.getNumericCellValue()) == cell.getNumericCellValue())
                        {
                            cellvalue = String.valueOf((long) cell.getNumericCellValue());
                        }
                        else
                        {
                            cellvalue = String.valueOf(cell.getNumericCellValue());
                        }
                        // System.out.println(cellvalue);
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
        }
        else
        {
            cellvalue = "";
        }
        return cellvalue;
    }

}
