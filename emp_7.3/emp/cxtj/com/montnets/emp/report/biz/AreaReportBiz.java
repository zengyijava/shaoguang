package com.montnets.emp.report.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.report.AprovinceCity;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.report.bean.RptConfInfo;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.dao.GenericAreaReportVoDAO;
import com.montnets.emp.report.vo.AreaReportVo;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.GlobalConst;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;


/**
 *  区域统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:37:48
 * @description
 */

public class AreaReportBiz {
	/**
	 * 通过区域对象获取区域报表数据
	 * @param areaRptvo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<AreaReportVo> getAreaReportsByVO(AreaReportVo areaRptvo, PageInfo pageInfo) throws Exception {
		
		//定义运营商统计报表集合
		List<AreaReportVo> areaVosList;

		try {
			//判断是否需要分页
			if (pageInfo == null) {
				areaVosList = new GenericAreaReportVoDAO().findAreaReportsByVo(areaRptvo);
			} else {
				areaVosList = new GenericAreaReportVoDAO().findAreaReportsByVo(areaRptvo,
								pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过区域对象获取区域报表数据");
			throw e;
		}
		//返回数据集合
		return areaVosList;
	}



	
	
	
/**
 * 
 * @param type
 * @return
 * @throws Exception
 */
	
    public List<AprovinceCity> getProvinceList() throws Exception {

		//定义区域集合
		List<AprovinceCity> acitys=new ArrayList<AprovinceCity>();
		String sql = "select * from A_PROVINCECITY where ID in (select Max(ID) from A_PROVINCECITY group by PROVINCE)";
		try {
				acitys = new SuperDAO().findEntityListBySQL(AprovinceCity.class, sql, null);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过区域对象获取区域省份数据");
			throw e;
		}
		//返回数据集合
		return acitys;
	}
	
	
	
	
	
	/**
	 * 获取总合计
	 * @param areareportvo
	 * @return
	 * @throws Exception
	 */
	
    public long[] findSumCount(AreaReportVo areareportvo) throws Exception {
		//获取总合计
		long[] count = new GenericAreaReportVoDAO().findSumCount(areareportvo);
		//返回合计总数数组
		return count;
	}

	/**
	 * 生成区域统计报表Excel
	 * 
	 * @param mtdList
	 * @param queryTime
	 * @param count
	 * @param fail
	 * @param showTime
	 * @return
	 * @throws Exception
	 */
	
    public Map<String, String> createareaReportExcel(List<AreaReportVo> mtdList, String countTime,
                                                     String tCount, String tFail) throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		String reportName = "AreaReport";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "areaReport.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("区域统计报表导出工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无区域统计报表数据！");
			throw new Exception("无区域统计报表数据！");
		}
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		// 产生报表文件的存储路径
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);

		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook = null;
		OutputStream os = null;
		InputStream in = null;
		try {

			File fileTemp = new File(voucherFilePath);
			if (!fileTemp.exists()) {
				fileTemp.mkdirs();
			}

			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			// 读取模板
			in = new FileInputStream(file);
			for (int f = 0; f < size; f++) {
				fileName = reportName + "_" + sdf.format(curDate) + "_["
						+ (f + 1) + "].xlsx";

				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook);
				// ------------------------------------------------------------

				// ------------------------------------------------------------

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, reportName);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < (f + 1) * intRowsOfPage ? mtdList
						.size() : (f + 1) * intRowsOfPage;
				int index = 0;

				XSSFCell[] cell1 = new XSSFCell[4];
				XSSFCell[] cell2 = new XSSFCell[4];
				int k;
				for (k = f * intRowsOfPage; k < intCnt; k++) {
					AreaReportVo mt = (AreaReportVo) mtdList
							.get(k);

					// 时间
					String showTime = countTime;

					if (countTime.contains("-")) {

						String[] showTimeArray = countTime.split("-");

						Integer monthtime = Integer.parseInt(showTimeArray[1]
								.toString());// 显示月份

						showTime = showTimeArray[0] + "年" + monthtime + "月";

					} else {
						showTime = countTime + "年";
					}

					String sendTime = showTime;
					// 区域
					String area =mt.getProvince()!=null?mt.getProvince():"未知";
					// 发送总数
					String icount = mt.getRsucc().toString();
					// 接收失败数
					String rfail = mt.getRfail2().toString();
					
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);
					cell1[3] = row.createCell(3);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					cell1[3].setCellStyle(cellStyle[0]);
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(area);
					cell1[2].setCellValue(icount);
					cell1[3].setCellValue(rfail);

					// 一页里的行数
					index++;
				}

				row = sheet.createRow(k + 1);
				// 生成四个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(2);
				cell2[3] = row.createCell(3);

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("合计:");
				cell2[2].setCellValue(tCount);
				cell2[3].setCellValue("".equals(tFail) ? "0" : tFail);

			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "区域统计报表" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"区域统计报表生成excel导出异常");
		} finally {
			// 清除对象
			workbook = null;
			SysuserUtil.closeStream(os);
			SysuserUtil.closeStream(in);
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}
/*
	*//**
	 * @description    生成区域报表新方法
	 * @param mtdList
	 * @param areaReportVo
	 * @return
	 * @throws Exception       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-4-10 上午09:30:45
	 *//*
	public Map<String, String> createareaReportExcel(List<AreaReportVo> mtdList, AreaReportVo areaReportVo) throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		String reportName = "AreaReport";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "areaReport.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 50000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("区域统计报表导出工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无区域统计报表数据！");
			throw new Exception("无区域统计报表数据！");
		}
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		// 产生报表文件的存储路径
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);

		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook = null;
		OutputStream os = null;
		InputStream in = null;
		try {

			File fileTemp = new File(voucherFilePath);
			if (!fileTemp.exists()) {
				fileTemp.mkdirs();
			}

			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			int reportType = areaReportVo.getReporttype();
			boolean isDes = areaReportVo.isDes();
			String startTime = areaReportVo.getStartdate();
			String endTime = areaReportVo.getEnddate();
			String tCount = String.valueOf(areaReportVo.getSucccount());
			String tFail = String.valueOf(areaReportVo.getRfail2());
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy年M月d日");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			//DateFormat df = DateFormat.getDateInstance();
			String timestr = "";
			if(reportType == 2&&!isDes){
				String startDate="";
				String endDate="";
				if(!"".equals(startTime) && null != startTime && 0 != startTime.length())
				{
					String btemp[] = startTime.split("-");
					startDate = btemp[0] + "年" + btemp[1] + "月" + btemp[2] + "日";
				}

				if(!"".equals(endTime) && null != endTime && 0 != endTime.length())
				{
					String etemp[] = endTime.split("-");
					endDate = etemp[0] + "年" + etemp[1] + "月" + etemp[2] + "日";
				}
				timestr = startDate + " 至 " + endDate;
				//timestr = sdf0.format(df.parse(startTime))+"至"+sdf0.format(df.parse(endTime));
			}
			
			for (int f = 0; f < size; f++) {
				fileName = reportName + "_" + sdf.format(curDate) + "_["
						+ (f + 1) + "]"+"_"+StaticValue.SERVER_NUMBER +".xlsx";
				in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook);
				// ------------------------------------------------------------

				// ------------------------------------------------------------

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, reportName);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < (f + 1) * intRowsOfPage ? mtdList
						.size() : (f + 1) * intRowsOfPage;
				int index = 0;

				XSSFCell[] cell1 = new XSSFCell[4];
				XSSFCell[] cell2 = new XSSFCell[4];
				int k;
				int rowNum = 0;
				for (k = f * intRowsOfPage; k < intCnt; k++) {
					AreaReportVo mt = (AreaReportVo) mtdList
							.get(k);
					if(!isDes){
						if(reportType == 2){
						}else if(reportType == 1){
							timestr = mt.getY()+"年";
						}else if(reportType == 0){
							timestr = mt.getY()+"年"+mt.getImonth()+"月";
						}else{
							timestr = "";
						}
					}else{
						if(reportType == 2){
							timestr = sdf0.format(sdf1.parse(mt.getIymd()));
						}else if(reportType == 1){
							timestr = mt.getY()+"年"+mt.getImonth()+"月";
						}else  if(reportType == 0){
							timestr = sdf0.format(sdf1.parse(mt.getIymd()));
						}else{
							timestr = "";
						}
					}
					String sendTime = timestr;
					// 区域
					String area =mt.getProvince()!=null?mt.getProvince():"未知";
					// 发送总数
					String icount = mt.getSucccount().toString();
					// 接收失败数
					String rfail = mt.getRfail2().toString();
					
					row = sheet.createRow((rowNum++) + 1);
					// 生成四个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);
					cell1[3] = row.createCell(3);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					cell1[3].setCellStyle(cellStyle[0]);
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(area);
					cell1[2].setCellValue(icount);
					cell1[3].setCellValue(rfail);

					// 一页里的行数
					index++;
				}

				row = sheet.createRow((rowNum++) + 1);
				// 生成四个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(2);
				cell2[3] = row.createCell(3);

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("合计:");
				cell2[2].setCellValue(tCount);
				cell2[3].setCellValue(tFail == "" ? "0" : tFail);
				// 输出到xlsx文件
				os = new FileOutputStream(voucherFilePath + File.separator
						+ fileName);
				// 写入Excel对象
				workbook.write(os);
			}

			fileName = "区域统计报表" + sdf.format(curDate) +"_"+StaticValue.SERVER_NUMBER + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"区域统计报表生成excel导出异常");
		} finally {
			// 清除对象
			workbook = null;
			os.close();
			in.close();
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}*/
	
	
	/**
	 * @description：生成区域统计报表Excel
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */
	public Map<String, String> createareaReportExcel(List<AreaReportVo> mtdList, AreaReportVo areaReportVo,HttpServletRequest request) throws Exception 
	{
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "区域统计报表";
		String reportName = "AreaReport";
		
		
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "areaReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("区域统计报表导出工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无区域统计报表数据！");
			throw new Exception("无区域统计报表数据！");
		}
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		// 产生报表文件的存储路径
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);

		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook = null;
		SXSSFWorkbook sworkbook = null;
		OutputStream os = null;
		InputStream in = null;
		try {

			File fileTemp = new File(voucherFilePath);
			if (!fileTemp.exists()) {
				fileTemp.mkdirs();
			}

			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			int reportType = areaReportVo.getReporttype();
			String startTime = areaReportVo.getStartdate();
			String endTime = areaReportVo.getEnddate();
			boolean isDes = areaReportVo.isDes();
			
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-M-d");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			String timestr = "";
			if(reportType == 2&&!isDes){
				String startDate="";
				String endDate="";
				if(!"".equals(startTime) && null != startTime && 0 != startTime.length())
				{
					String btemp[] = startTime.split("-");
					startDate = btemp[0] + "-" + btemp[1] + "-" + btemp[2];
				}

				if(!"".equals(endTime) && null != endTime && 0 != endTime.length())
				{
					String etemp[] = endTime.split("-");
					endDate = etemp[0] + "-" + etemp[1] + "-" + etemp[2];
				}
				timestr = startDate + "  "+MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_z",request)+"  " + endDate;
				//timestr = sdf0.format(df.parse(startTime))+"至"+sdf0.format(df.parse(endTime));
			}
			
			for (int f = 0; f < size; f++) {
				
				fileName = reportName + "_" + sdf.format(curDate) + "_["
						+ (f + 1) + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				workbook.setSheetName(0, reportName);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook);
				
				//获取模板第一个工作簿
				Sheet sheetTemp = workbook.getSheetAt(0);
				//获取模板第一行标题
				Row rowTitle = sheetTemp.getRow(0);

				//加载配置列
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.AREA_RPT_CONF_MENU_ID);
				//标题单元格索引，从1开始，因为已经有2个单元格
				int cellTitleIndex = 1;
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_sj", request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_rpt_qy", request));
				for(RptConfInfo rptConf : rptConList)
				{
					cellTitleIndex++;
					String temp = rptConf.getName();
					if(rowTitle.getCell(cellTitleIndex)==null){
						Cell ctitle=rowTitle.createCell(cellTitleIndex);
						sheetTemp.setColumnWidth(cellTitleIndex,3000);
						// 设置单元格样式
						ctitle.setCellStyle(cellStyle[1]);
					}
					//提交总数
					if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
					else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
					else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
					else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
					else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
					else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
					{//未返数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
					else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
					else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
					else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
					else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
					else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//未返率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(MessageUtils.extractMessage("cxtj",temp,request));
					}
				}
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				sworkbook = new SXSSFWorkbook(workbook,10000);
				Sheet sheet=workbook.getSheetAt(0);
				
				if(in!=null){
					in.close();
				}
				// 读取模板工作表
				Row row = null;

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < (f + 1) * intRowsOfPage ? mtdList
						.size() : (f + 1) * intRowsOfPage;
				int index = 0;

				Cell[] cell1 = new Cell[2+rptConList.size()];
				Cell[] cell2 = new Cell[2+rptConList.size()];
				int k;
				for (k = f * intRowsOfPage; k < intCnt; k++) {
					AreaReportVo mt = (AreaReportVo) mtdList.get(k);

					// 时间
					if(!isDes){
						if(reportType == 2){
						}else if(reportType == 1){
							timestr = mt.getY();
						}else if(reportType == 0){
							timestr = mt.getY()+"-"+mt.getImonth();
						}else{
							timestr = "";
						}
					}else{
						if(reportType == 2){
							timestr = sdf0.format(sdf1.parse(mt.getIymd()));
						}else if(reportType == 1){
							timestr = mt.getY()+"-"+mt.getImonth();
						}else  if(reportType == 0){
							timestr = sdf0.format(sdf1.parse(mt.getIymd()));
						}else{
							timestr = "";
						}
					}
					String sendTime = timestr;
					// 区域
					String area =mt.getProvince()!=null?mt.getProvince():MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
					
					//提交总数
					long icount=mt.getIcount() != null ? mt.getIcount() : 0;
					//接收成功数
					long rsucc=mt.getRsucc() != null ? mt.getRsucc() : 0;
					//发送失败数
					long rfail1=mt.getRfail1() != null ? mt.getRfail1() : 0;
					//接收失败数
					long rfail2=mt.getRfail2() != null ? mt.getRfail2() : 0;
					//未返数
					long rnret=mt.getRnret() != null ? mt.getRnret() : 0;
					//根据原始值返回计算值
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.AREA_RPT_CONF_MENU_ID);
					
					row = sheet.createRow(k + 1);
					// 生成2个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(area);
					
					//标题单元格索引，从1开始，因为已经有2个单元格
					int cellDataIndex = 1;
					for(RptConfInfo rptConf : rptConList)
					{
						cellDataIndex++;
						//提交总数
						if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
						{
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(icount);
						}
						else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//发送成功数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID):"0");
						}
						else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
						{//发送失败数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(rfail1);
						}
						else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
						{//接收失败数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(rfail2);
						}
						else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//接收成功数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID):"0");
						}
						else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
						{//未返数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(rnret);
						}
						else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
						{//发送成功率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):"0");
						}
						else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
						{//发送失败率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):"0");
						}
						else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//接收成功率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):"0");
						}
						else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//接收失败率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):"0");
						}
						else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//未返率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):"0");
						}
					}
					// 一页里的行数
					index++;
				}

				row = sheet.createRow(k + 1);
				// 生成2个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_hj", request));
				
				//标题单元格索引，从1开始，因为已经有2个单元格
				int cellTotalIndex = 1;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(areaReportVo.getIcount(),areaReportVo.getRsucc(), areaReportVo.getRfail1(), areaReportVo.getRfail2(), areaReportVo.getRnret(), RptStaticValue.USER_RPT_CONF_MENU_ID);
				for(RptConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(areaReportVo.getIcount());
					}
					else if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_COLUMN_ID):"0");
					}
					else if(RptStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(areaReportVo.getRfail1());
					}
					else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(areaReportVo.getRfail2());
					}
					else if(RptStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_COLUMN_ID):"0");
					}
					else if(RptStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
					{//未返数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(areaReportVo.getRnret());
					}
					else if(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):"0");
					}
					else if(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):"0");
					}
					else if(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):"0");
					}
					else if(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):"0");
					}
					else if(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//未返率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):"0");
					}
				}
				
			}
			
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			sworkbook.write(os);
			
			fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_rpt_qybb", request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"区域统计报表生成excel导出异常");
		} finally {
			// 清除对象
			workbook = null;
			if(os!=null){
				os.close();
			}
			if(in!=null){
				in.close();
			}

		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
}

