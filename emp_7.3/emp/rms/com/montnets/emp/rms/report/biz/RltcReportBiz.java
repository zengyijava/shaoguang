package com.montnets.emp.rms.report.biz;

import com.montnets.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.report.vo.LfRmsReportVo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RltcReportBiz {
	//日报表
	public static final int DAY_REPORT=0;
	//月报表
	public static final int MONTH_REPORT=1;
	//年报表
	public static final int YEAR_REPORT=2;

    public static HashMap<String,String> createRptExcelFile(List<LfRmsReportVo> reportList, LfRmsReportVo vo, Map<String, String> langMap) throws Exception{
		//excel名字
		String excelName = "RmsDegreeRpt";
		// 创建Excel的工作书册 Workbook,对应到一个excel文档
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFCellStyle cellStyle2 = wb.createCellStyle();
		String baseDir = new TxtFileUtil().getWebRoot() + "rms/report/file";
		HashMap<String, String> resultMap = new HashMap<String, String>(16);
		// 当前每页分页条数
		int rowsOfPage = 500000;
		// 生成的工作薄个数
		int sheetSize = (reportList.size() % rowsOfPage == 0) ? (reportList.size() / rowsOfPage) : (reportList.size() / rowsOfPage + 1);
		// 产生报表文件的存储路径
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date();
		//实际生成文件路径
		String voucherFilePath = baseDir + File.separator + "download" + File.separator+"degreeRpt"+File.separator + sdf.format(curDate);
		//模板文件路径
		String voucherTemplatePath = baseDir + File.separator + "temp" + File.separator + "rltcReport_"+langMap.get("langName")+".xlsx";
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			boolean mkdirs = fileTemp.mkdirs();
			if(!mkdirs){
				throw new EMPException("企业富信>数据查询>档位统计报表>导出功能实现异常，无法创建对应文件夹！");
			}
		}
		// 创建只读的Excel工作薄的对象, 此为模板文件
		File file = new File(voucherTemplatePath);
		if(!file.exists()){
			// 创建Excel的工作sheet,对应到一个excel文档的tab
			XSSFSheet xsheet = wb.createSheet("sheet1");
			XSSFFont font2 = wb.createFont();
			// 字体名称
			font2.setFontName("TAHOMA");
			font2.setBold(true);
			// 字体大小
			font2.setFontHeight(11);
			cellStyle2.setFont(font2);
			// 竖直对齐
			cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
			// 水平对齐
			cellStyle2.setAlignment(HorizontalAlignment.CENTER);
			cellStyle2.setWrapText(true);
			// 创建Excel的sheet的一行
			XSSFRow xrow = xsheet.createRow(0);
			XSSFCell[] cell = new XSSFCell[7];

			for (int i = 0; i < cell.length; i++) {
				// 创建一个Excel的单元格
				cell[i]=xrow.createCell(i);
				// 设置单元格样式
				cell[i].setCellStyle(cellStyle2);
			}

			// 给Excel的单元格设置样式和赋值
			cell[0].setCellValue(langMap.get("rms_fxapp_degreerep_time"));
			cell[1].setCellValue(langMap.get("rms_fxapp_degreerep_range"));
			cell[2].setCellValue(langMap.get("rms_fxapp_fsmx_operator2"));
			cell[3].setCellValue(langMap.get("rms_fxapp_fxsend_spact"));
			cell[4].setCellValue(langMap.get("rms_fxapp_degreerep_tjhms"));
			cell[5].setCellValue(langMap.get("rms_fxapp_degreerep_fscgs"));
			cell[6].setCellValue(langMap.get("rms_fxapp_degreerep_jssbs"));
			FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
			wb.write(xos);
			xos.close();
		}
		// 报表文件名
		String fileName = "";
		String zipName = null;
		String filePath = null;
		OutputStream os = null;
		XSSFWorkbook xssfWorkbook;
		SXSSFWorkbook sxssfWorkbook;
		for(int i = 0;i< sheetSize;i++){
			//文件名
			fileName = excelName +"_"+ sdf.format(curDate) + "_" + (i+1) + "_"+ StaticValue.getServerNumber() +".xlsx";
			//读取模板
			InputStream in = new FileInputStream(file);
			// 工作表
			xssfWorkbook = new XSSFWorkbook(in);
			sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook,10000);
			Sheet sheet = sxssfWorkbook.getSheetAt(i);
			sxssfWorkbook.setSheetName(i, excelName);
			// 表格样式
			XSSFCellStyle cellStyle = setCellStyle(xssfWorkbook);
			in.close();
			// 读取模板工作表
			Row row;
			Cell[] cells = new Cell[7];
			Integer k = 0;
			Long icountSum = 0L;
			Long rsuccSum = 0L;
			Long rfailSum = 0L;
			for(LfRmsReportVo rpt:reportList){
				row = sheet.createRow(k++ + 1);
				for(int j = 0;j < cells.length;j++){
					cells[j] = row.createCell(j);
					cells[j].setCellStyle(cellStyle);
				}
				String timeStr = setTimeByReportTypeAndIsDes(rpt, vo);
				cells[0].setCellValue(timeStr);
				cells[1].setCellValue(rpt.getDegree() != null ? rpt.getDegree().toString()+langMap.get("rms_fxapp_myscene_levelunit"):"");
				Long spisuncm = rpt.getSpisuncm();
				String spisuncmName = "";
				if(spisuncm != null){
					if(spisuncm == 0){
//						spisuncmName = "移动";
						spisuncmName = langMap.get("rms_fxapp_fsmx_yidong");
					}else if(spisuncm == 1){
//						spisuncmName = "联通";
						spisuncmName = langMap.get("rms_fxapp_fsmx_liantong");
					}else if(spisuncm == 21){
//						spisuncmName = "电信";
						spisuncmName = langMap.get("rms_fxapp_fsmx_dianxin");
					}else if(spisuncm == 5){
//						spisuncmName = "国外";
						spisuncmName = langMap.get("rms_fxapp_fsmx_guowai");
					}else {
//						spisuncmName = "未知";
						spisuncmName = langMap.get("rms_report_unknown");
					}
				}
				Long icount = rpt.getIcount() == null ? 0:rpt.getIcount();
				Long rsucc = rpt.getRsucc() == null ? 0:rpt.getRsucc();
				Long rfail = rpt.getIcount() == null ? 0:rpt.getRfail();
				cells[2].setCellValue(spisuncmName);
				cells[3].setCellValue(StringUtils.defaultIfEmpty(rpt.getSpID(),""));
				cells[4].setCellValue(icount);
				cells[5].setCellValue(rsucc);
				cells[6].setCellValue(rfail);
				icountSum += icount;
				rsuccSum += rsucc;
				rfailSum += rfail;
			}
			//增加合计项
			row = sheet.createRow(k + 1);
			for(int j = 0;j < cells.length;j++){
				cells[j] = row.createCell(j);
				cells[j].setCellStyle(cellStyle2);
			}
			//合并前四个单元格(起始行号，终止行号， 起始列号，终止列号)
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 0, 3));
			cells[0].setCellValue("合计：");
			cells[4].setCellValue(icountSum);
			cells[5].setCellValue(rsuccSum);
			cells[6].setCellValue(rfailSum);

			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			sxssfWorkbook.write(os);
		}
		zipName  = "档位统计报表" + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() + ".zip";
		filePath = baseDir + File.separator + "download" + File.separator + "degreeRpt" + File.separator + zipName;
		ZipUtil.compress(voucherFilePath, filePath);
		//递归删除文件夹
		boolean flag = deleteDir(fileTemp);
		if (!flag) {
            EmpExecutionContext.error("刪除文件失敗！");
        }
		//关闭资源
		if(os != null){
			os.close();
		}
		sxssfWorkbook = null;
		xssfWorkbook = null;
		resultMap.put("fileName", zipName);
		resultMap.put("filePath", filePath);
		return resultMap;
    }

	/**
	 * 根据报表类型与是否为详情页输出时间字符串
	 * @param rpt vo对象(遍历对象)
	 * @param vo vo对象(查询条件组成的对象)
	 * @return 时间字符串
	 */
	private static String setTimeByReportTypeAndIsDes(LfRmsReportVo rpt, LfRmsReportVo vo) {
		String result = "";
    	//如果是日报表且不为详情时则显示为查询的开始与结束时间
		if(vo.getReporttype() == DAY_REPORT && !vo.getIsDes()){
			result = vo.getStartTime().replaceFirst("-","年").
										replaceFirst("-","月").
										replaceFirst("-","日")
					+ "至" +
					vo.getEndTime().replaceFirst("-","年").
									replaceFirst("-","月").
									replaceFirst("-","日");
		}else if((vo.getReporttype() == DAY_REPORT || vo.getReporttype() == MONTH_REPORT) && vo.getIsDes()){
			//月报表或日报表且为详情时则显示为IYMD(xxxx年xx月xx日)
			result = rpt.getIymd().substring(0,4) + "年" + rpt.getIymd().substring(4,6) + "月" + rpt.getIymd().substring(6) + "日";
		}else if(vo.getReporttype() == MONTH_REPORT && !vo.getIsDes()){
			//月报表且不为详情时则显示对应的年月
			result = rpt.getY() + "年" + rpt.getImonth() + "月";
		}else if(vo.getReporttype() == YEAR_REPORT && !vo.getIsDes()){
			//年报表且不为详情时则显示对应的年份
			result = rpt.getY() + "年";
		}else if(vo.getReporttype() == YEAR_REPORT && vo.getIsDes()){
			//年报表且为详情时则显示对应的月份
			result = rpt.getImonth() + "月";
		}
		return result;
	}

	/**
	 * 递归删除
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] subFiles = dir.list();
			for (String file:subFiles) {
				boolean success = deleteDir(new File(dir, file));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	/**
	 * 将设置单元格属性提取出来成一个方法
	 * @param workbook excel对象
	 * @return 单元格属性
	 */
	public static XSSFCellStyle setCellStyle(XSSFWorkbook workbook) {
		// 表格样式
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		// 字体名称
		font.setFontName("TAHOMA");
		// 下环线
		font.setUnderline(FontUnderline.NONE);
		// 粗体
		font.setBold(false);
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
}
