package com.montnets.emp.statecode.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.statecode.LfStateCode;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.util.ZipUtil;

/***
 * 状态码数据导出工具类 
 * @author zhouxiangxian
 *
 */
public class StateCodeExcelTool {
    // 报表模板目录
    private static String BASEDIR = "";
    // 操作员报表文件路径
    private static String voucherTemplatePath = "";

    private final String TEMP = "temp";
    // 产生下行报表路径
    private final String voucherPath = "download";

//	private CommonVariables cv;

    public StateCodeExcelTool(String context) {

        BASEDIR = context;
//		cv = new CommonVariables();

    }

    /**
     * @param fileName 文件名
     * @param mtdList  状态码数据
     * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
     * @description：生成群发历史的excel
     * @author zhouxiangxian
     * @see zhouxiangxian 2018-12-02 创建
     */

    public Map<String, String> createMtReportExcel(List<LfStateCode> mtdList, String exportType, HttpServletRequest request)
            throws Exception {

        //String execlName = "群发历史";
        String execlName = "StateCode";
        HttpSession session = request.getSession();
        String langName = (String) session.getAttribute(StaticValue.LANG_KEY);

        voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator + "statecode_Template_" + langName + ".xlsx";//statecode_Template_zh_CN
//		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, String> resultMap = new HashMap<String, String>();

        // 当前每页分页条数
        int intRowsOfPage = 500000;

        // 当前每页显示条数
        int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
                .size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

        int size = intPagesCount; // 生成的工作薄个数
        //中文内容国际化 标识变量
        String ztm = MessageUtils.extractMessage("xtgl", "xtgl_cswh_zmtgl_ztm", request);
        String ysm = MessageUtils.extractMessage("xtgl", "xtgl_cswh_zmtgl_ysm", request);
        String ztmsm = MessageUtils.extractMessage("xtgl", "xtgl_cswh_zmtgl_ztmsm", request);


        EmpExecutionContext.info("工作表个数：" + size);
        if (size == 0) {
            EmpExecutionContext.info("无状态码数据！");
            throw new Exception("无状态码数据！");
        }

        // 产生报表文件的存储路径
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
        String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "groupHistory" + File.separator + sdf.format(curDate);
        File fileTemp = new File(voucherFilePath);
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }

        // 报表文件名
        String fileName = null;
        String filePath = null;
        XSSFWorkbook workbook1 = null;
        SXSSFWorkbook workbook = null;
        FileInputStream in = null;
        FileOutputStream os = null;
        //导出excel列数
        int cellLen = 3;
        try {
            // 创建只读的Excel工作薄的对象, 此为模板文件
            File file = new File(voucherTemplatePath);

            //判断模板文件是否存在
            if (!file.exists()) {
                // 创建Excel的工作书册 Workbook,对应到一个excel文档
                XSSFWorkbook wb = new XSSFWorkbook();
                // 创建Excel的工作sheet,对应到一个excel文档的tab

                XSSFSheet xsheet = wb.createSheet("sheet1");
                XSSFCellStyle cellStyle2 = wb.createCellStyle();
                XSSFFont font2 = wb.createFont();
                // 字体名称
                font2.setFontName("TAHOMA");
                font2.setBold(true);
                // 字体大小
                font2.setFontHeight(11);
                cellStyle2.setFont(font2);
                // 水平对齐
                cellStyle2.setAlignment(HorizontalAlignment.CENTER);
                // 竖直对齐
                cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
                cellStyle2.setWrapText(true);
                // 创建Excel的sheet的一行
                XSSFRow xrow = xsheet.createRow(0);
                XSSFCell[] cell = new XSSFCell[cellLen];

                for (int i = 0; i < cell.length; i++) {
                    // 创建一个Excel的单元格
                    cell[i] = xrow.createCell(i);
                    // 设置单元格样式
                    cell[i].setCellStyle(cellStyle2);
                }

                // 给Excel的单元格设置样式和赋值
                cell[0].setCellValue(ztm);
                cell[1].setCellValue(ysm);
                cell[2].setCellValue(ztmsm);
                FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
                wb.write(xos);
                xos.close();
            }

            for (int j = 1; j <= size; j++) {
                //文件名
                fileName = execlName + "_" + sdf.format(curDate) + "_[" + j + "]_" + StaticValue.getServerNumber() + ".xlsx";

                XSSFCellStyle cellStyle = null;
                Sheet sheet = null;
                //如果文件存在
                if (file.exists()) {
                    // 读取模板
                    in = new FileInputStream(file);
                    // 工作表
                    workbook1 = new XSSFWorkbook(in);
                    workbook1.setSheetName(0, execlName);
                    EmpExecutionContext.info("工作薄创建与模板报表移除成功!");
                    workbook = new SXSSFWorkbook(workbook1, 10000);
                    sheet = workbook.getSheetAt(0);
                    // 表格样式
                    cellStyle = setCellStyle(workbook1);
                    in.close();
                } else {
                    //文件不存在，则创建
                    workbook = new SXSSFWorkbook();
                    sheet = workbook.createSheet(execlName);

                    Row topRow = sheet.createRow(0);
                    Cell[] cell = new Cell[cellLen];
                    for (int i = 0; i < cell.length; i++) {
                        cell[i] = topRow.createCell(i);
                        cell[i].setCellStyle(cellStyle);
                    }
                    cell[0].setCellValue(ztm);
                    cell[1].setCellValue(ysm);
                    cell[2].setCellValue(ztmsm);

                }
                // 读取模板工作表
                Row row = null;
                // 总体流程
                // 根据记录，计算总的页数
                // 每页的处理 一页一个sheet
                // 写入页标题
                // 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
                // 写页尾
                int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;

                Cell[] cell = new Cell[cellLen];

                for (int k = 0; k < intCnt; k++) {

                    LfStateCode mt = (LfStateCode) mtdList.get(k);

                    // 状态码
                    String stateCode = mt.getStateCode();
                    // 映射码
                    String mappingCode = mt.getMappingCode() == null ? "" : mt.getMappingCode();
                    // 状态码描述
                    String stateDes = mt.getStateDes() == null ? "" : mt.getStateDes();


                    row = sheet.createRow(k + 2);
                    for (int i = 0; i < cell.length; i++) {
                        cell[i] = row.createCell(i);
                        cell[i].setCellStyle(cellStyle);
                    }

                    cell[0].setCellValue(stateCode);
                    cell[1].setCellValue(mappingCode);
                    cell[2].setCellValue(stateDes);


                }
                os = new FileOutputStream(voucherFilePath + File.separator + fileName);
                // 写入Excel对象
                workbook.write(os);
            }
            if ("1".equals(exportType)) {
                fileName = ztm + sdf.format(curDate) + "_" + StaticValue.getServerNumber() + ".zip";
            }
            filePath = BASEDIR + File.separator + voucherPath + File.separator
                    + "groupHistory" + File.separator + fileName;

            ZipUtil.compress(voucherFilePath, filePath);
            deleteDir(fileTemp);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "导出状态码为.xlsx格式时出现异常！");
        } finally {
            if (os != null) {
                // 清除对象
                os.close();
            }
            workbook = null;
        }
        resultMap.put("FILE_NAME", fileName);
        resultMap.put("FILE_PATH", filePath);
        return resultMap;
    }
    
    /**
     * <pre>导出状态码数据为xls文件格式
     * @param mtdList       需要导出的状态码数据
     * @param exportType    需要解压的类型 <p>exportType=1表示解压为zip文件格式</p>
     * @param request
     * @return              返回Map<String,String>
     * @throws Exception
     */
    public Map<String, String> createExcelByXls(List<LfStateCode> mtdList, String exportType, HttpServletRequest request)
    		throws Exception {
    	String execlName = "StateCode";
    	HttpSession session = request.getSession();
    	String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
    	
    	voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator + "statecode_Template_" + langName + ".xls";//statecode_Template_zh_CN
//		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	Map<String, String> resultMap = new HashMap<String, String>();
    	
    	// 当前每页分页条数
    	int intRowsOfPage = 500000;
    	
    	// 当前每页显示条数
    	int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
    			.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);
    	
    	int size = intPagesCount; // 生成的工作薄个数
    	//中文内容国际化 标识变量
    	String ztm = MessageUtils.extractMessage("xtgl", "xtgl_cswh_zmtgl_ztm", request);
    	String ysm = MessageUtils.extractMessage("xtgl", "xtgl_cswh_zmtgl_ysm", request);
    	String ztmsm = MessageUtils.extractMessage("xtgl", "xtgl_cswh_zmtgl_ztmsm", request);
    	
    	
    	EmpExecutionContext.info("工作表个数：" + size);
    	if (size == 0) {
    		EmpExecutionContext.info("无状态码数据！");
    		throw new Exception("无状态码数据！");
    	}
    	
    	// 产生报表文件的存储路径
    	Date curDate = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
    	String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "stateCode" + File.separator + sdf.format(curDate);
    	File fileTemp = new File(voucherFilePath);
    	if (!fileTemp.exists()) {
    		fileTemp.mkdirs();
    	}
    	
    	// 报表文件名
    	String fileName = null;
    	String filePath = null;
    	HSSFWorkbook workbook1 = null;
//    	HSSFWorkbook workbook = null;
    	FileInputStream in = null;
    	FileOutputStream os = null;
    	//导出excel列数
    	int cellLen = 3;
    	try {
    		// 创建只读的Excel工作薄的对象, 此为模板文件
    		File file = new File(voucherTemplatePath);
    		
    		//判断模板文件是否存在
    		if (!file.exists()) {
    			// 创建Excel的工作书册 Workbook,对应到一个excel文档
    			HSSFWorkbook wb = new HSSFWorkbook();
    			// 创建Excel的工作sheet,对应到一个excel文档的tab
    			
    			HSSFSheet xsheet = wb.createSheet("sheet1");
    			HSSFCellStyle cellStyle2 = wb.createCellStyle();
    			HSSFFont font2 = wb.createFont();
    			// 字体名称
    			font2.setFontName("TAHOMA");
    			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    			// 字体大小
    			font2.setFontHeightInPoints((short)11);
    			cellStyle2.setFont(font2);
    			// 水平对齐
    			cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	    	// 竖直对齐
    			cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
    			cellStyle2.setWrapText(true);
    			// 创建Excel的sheet的一行
    			HSSFRow xrow = xsheet.createRow(0);
    			HSSFCell[] cell = new HSSFCell[cellLen];
    			
    			for (int i = 0; i < cell.length; i++) {
    				// 创建一个Excel的单元格
    				cell[i] = xrow.createCell(i);
    				// 设置单元格样式
    				cell[i].setCellStyle(cellStyle2);
    			}
    			
    			// 给Excel的单元格设置样式和赋值
    			cell[0].setCellValue(ztm);
    			cell[1].setCellValue(ysm);
    			cell[2].setCellValue(ztmsm);
    			FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
    			wb.write(xos);
    			xos.close();
    		}
    		
    		for (int j = 1; j <= size; j++) {
    			//文件名
    			fileName = execlName + "_" + sdf.format(curDate) + "_[" + j + "]_" + StaticValue.getServerNumber() + ".xls";
    			
    			HSSFCellStyle cellStyle = null;
    			HSSFSheet  sheet = null;
    			//如果文件存在
    			if (file.exists()) {
    				// 读取模板
    				in = new FileInputStream(file);
    				POIFSFileSystem fs = new POIFSFileSystem(in);
    				// 工作表
    				workbook1 = new HSSFWorkbook(fs);
    				workbook1.setSheetName(0, execlName);
    				EmpExecutionContext.info("工作薄创建与模板报表移除成功!");
    				//workbook = new HSSFWorkbook();
    				sheet = workbook1.getSheetAt(0);
    				// 表格样式
    				cellStyle = setCellStyle(workbook1);
    				in.close();
    				
    			} else {
    				//文件不存在，则创建
    				workbook1 = new HSSFWorkbook();
    				sheet = workbook1.createSheet(execlName);
    				
    				HSSFRow topRow = sheet.createRow(0);
    				HSSFCell[] cell = new HSSFCell[cellLen];
    				for (int i = 0; i < cell.length; i++) {
    					cell[i] = topRow.createCell(i);
    					cell[i].setCellStyle(cellStyle);
    				}
    				cell[0].setCellValue(ztm);
    				cell[1].setCellValue(ysm);
    				cell[2].setCellValue(ztmsm);
    				
    			}
    			// 读取模板工作表
    			HSSFRow row = null;
    			// 总体流程
    			// 根据记录，计算总的页数
    			// 每页的处理 一页一个sheet
    			// 写入页标题
    			// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
    			// 写页尾
    			int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
    			
    			HSSFCell[] cell = new HSSFCell[cellLen];
    			
    			for (int k = 0; k < intCnt; k++) {
    				
    				LfStateCode mt = (LfStateCode) mtdList.get(k);
    				
    				// 状态码
    				String stateCode = mt.getStateCode();
    				// 映射码
    				String mappingCode = mt.getMappingCode() == null ? "" : mt.getMappingCode();
    				// 状态码描述
    				String stateDes = mt.getStateDes() == null ? "" : mt.getStateDes();
    				
    				
    				row = sheet.createRow(k + 2);
    				for (int i = 0; i < cell.length; i++) {
    					cell[i] = row.createCell(i);
    					cell[i].setCellStyle(cellStyle);
    				}
    				
    				cell[0].setCellValue(stateCode);
    				cell[1].setCellValue(mappingCode);
    				cell[2].setCellValue(stateDes);
    				
    				
    			}
    			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
    			// 写入Excel对象
    			workbook1.write(os);
    		}
    		if ("1".equals(exportType)) {
    			fileName = ztm + sdf.format(curDate) + "_" + StaticValue.getServerNumber() + ".zip";
    		}
    		filePath = BASEDIR + File.separator + voucherPath + File.separator
    				+ "stateCode" + File.separator + fileName;
    		
    		ZipUtil.compress(voucherFilePath, filePath);
    		deleteDir(fileTemp);
    	} catch (Exception e) {
    		EmpExecutionContext.error(e, "导出状态码为.xls格式时出现异常！");
    	} finally {
    		if (os != null) {
    			// 清除对象
    			os.close();
    		}
    		workbook1 = null;
    	}
    	resultMap.put("FILE_NAME", fileName);
    	resultMap.put("FILE_PATH", filePath);
    	return resultMap;
    }

    /**
     * 将设置单元格属性提取出来成一个方法，方便其他模块调用
     *
     * @param workbook
     * @return
     */
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
    public HSSFCellStyle setCellStyle(HSSFWorkbook workbook) {
    	HSSFCellStyle cellStyle = workbook.createCellStyle();
    	
    	HSSFFont font = workbook.createFont();
    	// 字体名称
    	font.setFontName("TAHOMA");
    	// 粗体
    	font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    	// 下环线
    	font.setUnderline((byte)0);
    	// 字体大小
    	font.setFontHeightInPoints((short)11);
    	cellStyle.setFont(font);
    	// 水平对齐
    	cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	// 竖直对齐
    	cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

    	cellStyle.setWrapText(true);
    	
    	return cellStyle;
    }

    /**
     * 删除文件目录
     *
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}
