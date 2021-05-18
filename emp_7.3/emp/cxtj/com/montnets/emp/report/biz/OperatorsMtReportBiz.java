package com.montnets.emp.report.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.report.bean.RptConfInfo;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.dao.GenericOperatorsMtDataReportVoDAO;
import com.montnets.emp.report.vo.OperatorsAreaMtDataReportVo;
import com.montnets.emp.report.vo.OperatorsMtDataReportVo;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.GlobalConst;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;


/**
 * 运营商统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:45:04
 * @description
 */

public class OperatorsMtReportBiz{
	/**
	 * 通过查询条件获取运营商统计报表集合 月报表
	 * @param mtDataReportVo
	 * 查询条件
	 * @param pageInfo
	 * 分页对象
	 * @return
	 * 返回运营商统计报表集合
	 * @throws Exception
	 */
	
    public List<OperatorsMtDataReportVo> getReportInfosByMonth(
			OperatorsMtDataReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {
		
		//定义运营商统计报表集合
		List<OperatorsMtDataReportVo> mtDRVosList;

		try {
			//判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByMonth(mtDataReportVo);
			} else {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByMonth(mtDataReportVo,
								pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询运营商报表出错");
			throw e;
		}
		//返回数据集合
		return mtDRVosList;
	}

	/**
	 * 通过查询条件获取运营商统计报表集合 年报表
	 * @param mtDataReportVo
	 * 查询条件
	 * @param pageInfo
	 * 分页对象
	 * @return
	 * 返回运营商统计报表集合
	 * @throws Exception
	 */
	
    public List<OperatorsMtDataReportVo> getReportInfosByYear(
			OperatorsMtDataReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {

		//定义运营商统计报表集合
		List<OperatorsMtDataReportVo> mtDRVosList;

		try {
			//判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByYear(mtDataReportVo);
			} else {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByYear(mtDataReportVo,
								pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过查询条件获取运营商统计报表集合 年报表异常");
			throw e;
		}
		//返回数据集合
		return mtDRVosList;
	}

	
	/**
	 * 通过查询条件获取运营商统计报表集合 日报表
	 * @param mtDataReportVo
	 * 查询条件
	 * @param pageInfo
	 * 分页对象
	 * @return
	 * 返回运营商统计报表集合
	 * @throws Exception
	 */
	
    public List<OperatorsMtDataReportVo> getReportInfosByDays(
			OperatorsMtDataReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {
		
		//定义运营商统计报表集合
		List<OperatorsMtDataReportVo> mtDRVosList;

		try {
			//判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByDays(mtDataReportVo);
			} else {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByDays(mtDataReportVo,
								pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询运营商报表出错");
			throw e;
		}
		//返回数据集合
		return mtDRVosList;
	}
	
	
	/**
	 * 通过查询条件获取运营商统计报表集合 年报表
	 * @param mtDataReportVo
	 * 查询条件
	 * @param pageInfo
	 * 分页对象
	 * @return
	 * 返回运营商统计报表集合
	 * @throws Exception
	 */
	
    public List<OperatorsMtDataReportVo> getListByMsType(int type) throws Exception {

		//定义运营商统计报表集合
		List<OperatorsMtDataReportVo> mtDRVosList=new ArrayList<OperatorsMtDataReportVo>();
		String smssql = "select t.spid from mt_datareport t group by t.spid";
		String mmssql = "select t.spid from MMS_DATAREPORT t group by t.spid";

		try {
			//判断短彩类型
			if (type == 0) {
				mtDRVosList = new SuperDAO().findEntityListBySQL(OperatorsMtDataReportVo.class, smssql, null);
			} else {
				mtDRVosList = new SuperDAO().findEntityListBySQL(OperatorsMtDataReportVo.class, mmssql, null);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过查询条件获取运营商统计报表集合 年报表异常");
			throw e;
		}
		//返回数据集合
		return mtDRVosList;
	}
	
	/**
	 * 通过查询条件获取运营商统计报表集合 各国详情
	 * @param mtDataReportVo
	 * 查询条件
	 * @param pageInfo
	 * 分页对象
	 * @return
	 * 返回运营商统计报表集合
	 * @throws Exception
	 */
	
    public List<OperatorsAreaMtDataReportVo> getListByAreadetail(
			OperatorsAreaMtDataReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {
		
		//定义运营商统计报表集合
		List<OperatorsAreaMtDataReportVo> mtDRVosList;

		try {
			//判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
				.findOperatorsMtDataReportVoByArea(mtDataReportVo);
			} else {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByArea(mtDataReportVo,
								pageInfo);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询运营商报表出错");
			throw e;
		}
		//返回数据集合
		return mtDRVosList;
	}
	
	
	
	/**
	 * 获取总合计
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	
    public long[] findSumCount(OperatorsMtDataReportVo operatorsMtDataReportVo) throws Exception {
		//获取总合计
		long[] count = new GenericOperatorsMtDataReportVoDAO().findSumCount(
				operatorsMtDataReportVo);
		//返回合计总数数组
		return count;
	}

	/**
	 * 获取总合计  各国详情
	 * @param operatorsAreaMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCount(OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo) throws Exception {
		//获取总合计
		long[] count = new GenericOperatorsMtDataReportVoDAO().findSumCount(
				operatorsAreaMtDataReportVo);
		//返回合计总数数组
		return count;
	}

	
	
	/**
	 * 生成运营商报表Excel
	 * 
	 * @param mtdList
	 * @param queryTime
	 * @param count
	 * @param fail
	 * @param showTime
	 * @return
	 * @throws Exception
	 */
	
    public Map<String, String> createSpReportExcel(
			List<OperatorsMtDataReportVo> mtdList, String countTime,
			String tCount, String tFail,OperatorsMtDataReportVo operatorsMtDataReportVo) throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "运营商统计报表";
		String reportName = "SpMtReport";
		
		
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spMtReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("运营商报表导出工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无运营商报表数据！");
			throw new Exception("无运营商报表数据！");
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
			int reportType = operatorsMtDataReportVo.getReporttype();
			boolean isDes = operatorsMtDataReportVo.getIsDes();
			String startTime = operatorsMtDataReportVo.getStartTime();
			String endTime = operatorsMtDataReportVo.getEndTime();

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
			// 读取模板
			in = new FileInputStream(file);
			for (int f = 0; f < size; f++) {
				fileName = reportName + "_" + sdf.format(curDate) + "_["
						+ (f + 1) + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";

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

				XSSFCell[] cell1 = new XSSFCell[5];
				XSSFCell[] cell2 = new XSSFCell[5];
				int k;
				for (k = f * intRowsOfPage; k < intCnt; k++) {
					OperatorsMtDataReportVo mt = (OperatorsMtDataReportVo) mtdList
							.get(k);

					// 时间

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
					// sp账号
					String spId = mt.getSpID() != null ? (!"".equals(mt
							.getSpID().trim()) ? mt.getSpID() : "未知") : "未知";
					// 发送总数
					String icount = mt.getIcount().toString();
					// 接收失败数
					String rfail = mt.getRfail().toString();
					String spisuncm = "";
					if (mt.getSpisuncm() - 0 == 0) {
						spisuncm = "移动";
					} else if (mt.getSpisuncm() - 1 == 0) {
						spisuncm = "联通";
					} else if (mt.getSpisuncm() - 21 == 0) {
						spisuncm = "电信";
					} else if (mt.getSpisuncm() - 5 == 0) {
						spisuncm = "国外";
					}
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);
					cell1[3] = row.createCell(3);
					cell1[4] = row.createCell(4);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					cell1[3].setCellStyle(cellStyle[0]);
					cell1[4].setCellStyle(cellStyle[0]);
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(spId);
					cell1[2].setCellValue(spisuncm);
					cell1[3].setCellValue(icount);
					cell1[4].setCellValue(rfail);

					// 一页里的行数
					index++;
				}

				row = sheet.createRow(k + 1);
				// 生成四个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(2);
				cell2[3] = row.createCell(3);
				cell2[4] = row.createCell(4);

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);
				cell2[4].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue("合计:");
				cell2[3].setCellValue(tCount);
				cell2[4].setCellValue(tFail == "" ? "0" : tFail);

			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "运营商统计报表" + sdf.format(curDate)+"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"运营商统计报表生成excel导出异常");
		} finally {
			// 清除对象
			workbook = null;
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常");
				}
			}
			if(in != null){
				try{
					in.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常");
				}
			}
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}

	/**
	 * 生成运营商报表Excel
	 * 
	 * @param mtdList
	 * @param queryTime
	 * @param totalIcount
	 * @param totalRfail1
	 * @param totalRfail2
	 * @param totalRnret
	 * @param totalRsucc
	 * @param showTime
	 * @return
	 * @throws Exception
	 */
	
	public Map<String, String> createSpReportExcel(
			List<OperatorsMtDataReportVo> mtdList, String countTime,
			long totalIcount, long totalRfail1,long totalRfail2,long totalRnret,
			long totalRsucc,OperatorsMtDataReportVo operatorsMtDataReportVo,HttpServletRequest request) throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "运营商统计报表";
		String reportName = "SpMtReport";
		
		
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spMtReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("运营商报表导出工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无运营商报表数据！");
			throw new Exception("无运营商报表数据！");
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
			int reportType = operatorsMtDataReportVo.getReporttype();
			String startTime = operatorsMtDataReportVo.getStartTime();
			String endTime = operatorsMtDataReportVo.getEndTime();
			boolean isDes = operatorsMtDataReportVo.getIsDes();

			//SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy年M月d日");
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-M-d");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			String timestr = "";
			if(reportType == 2&&!isDes){
				String startDate="";
				String endDate="";
				if(!"".equals(startTime) && null != startTime && 0 != startTime.length())
				{
					String btemp[] = startTime.split("-");
					//startDate = btemp[0] + "年" + btemp[1] + "月" + btemp[2] + "日";
					startDate = startTime;
				}

				if(!"".equals(endTime) && null != endTime && 0 != endTime.length())
				{
					String etemp[] = endTime.split("-");
					//endDate = etemp[0] + "年" + etemp[1] + "月" + etemp[2] + "日";
					endDate = endTime;
				}
				timestr = startDate + "  "+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)+"  " + endDate;
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
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_sj", request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_yyszhid", request));
				rowTitle.getCell(2).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_yyslx", request));
				//加载配置列
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
				//标题单元格索引，从2开始，因为已经有3个单元格
				int cellTitleIndex = 2;
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

				Cell[] cell1 = new Cell[3+rptConList.size()];
				Cell[] cell2 = new Cell[3+rptConList.size()];
				int k;
				for (k = f * intRowsOfPage; k < intCnt; k++) {
					OperatorsMtDataReportVo mt = (OperatorsMtDataReportVo) mtdList
							.get(k);

					// 时间
					// 时间
					if(!isDes){
						if(reportType == 2){
						}else if(reportType == 1){
							//timestr = mt.getY()+"年";
							timestr = mt.getY();
						}else if(reportType == 0){
							//timestr = mt.getY()+"年"+mt.getImonth()+"月";
							timestr = mt.getY()+"-"+mt.getImonth();
						}else{
							timestr = "";
						}
					}else{
						if(reportType == 2){
							timestr = sdf0.format(sdf1.parse(mt.getIymd()));
						}else if(reportType == 1){
							//timestr = mt.getY()+"年"+mt.getImonth()+"月";
							timestr = mt.getY()+"-"+mt.getImonth();
						}else  if(reportType == 0){
							timestr = sdf0.format(sdf1.parse(mt.getIymd()));
						}else{
							timestr = "";
						}
					}
					String sendTime = timestr;
					
					// sp账号
					String spId = mt.getSpID() != null ? (!"".equals(mt
							.getSpID().trim()) ? mt.getSpID() : MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_wz",request)) : MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_wz",request);
					String spisuncm = "";
					if (mt.getSpisuncm() - 0 == 0) {
						spisuncm = MessageUtils.extractMessage("cxtj","cxtj_sjcx_xtsxjl_yd",request);
					} else if (mt.getSpisuncm() - 1 == 0) {
						spisuncm = MessageUtils.extractMessage("cxtj","cxtj_sjcx_xtsxjl_lt",request);
					} else if (mt.getSpisuncm() - 21 == 0) {
						spisuncm = MessageUtils.extractMessage("cxtj","cxtj_sjcx_xtsxjl_dx",request);
					} else if (mt.getSpisuncm() - 5 == 0) {
						spisuncm = MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_gw",request);
					}
					
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
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
					
					row = sheet.createRow(k + 1);
					// 生成3个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(spId);
					cell1[2].setCellValue(spisuncm);
					
					//标题单元格索引，从2开始，因为已经有3个单元格
					int cellDataIndex = 2;
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
				// 生成3个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(2);

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_hj",request));
				
				//标题单元格索引，从2开始，因为已经有3个单元格
				int cellTotalIndex = 2;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(totalIcount, totalRsucc, totalRfail1, totalRfail2, totalRnret, RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
				for(RptConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(totalIcount);
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
						cell1[cellTotalIndex].setCellValue(totalRfail1);
					}
					else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(totalRfail2);
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
						cell1[cellTotalIndex].setCellValue(totalRnret);
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
			
			//fileName = "运营商统计报表" + sdf.format(curDate) +"_"+StaticValue.SERVER_NUMBER + ".zip";
			fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_yystjbb", request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"运营商统计报表生成excel导出异常");
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
	/**
	 * 生成运营商报表Excel--各国详情
	 * 
	 * @param mtdList
	 * @param queryTime
	 * @param count
	 * @param fail
	 * @param showTime
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createSpReportExcelByArea(
			List<OperatorsAreaMtDataReportVo> mtdList, String countTime,
			String tCount, String tFail,OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo) throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "运营商统计报表";
		String reportName = "SpMtReportArea";
		
		
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spMtReportArea.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("运营商报表各国详情导出工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无运营商报表各国详情数据！");
			throw new Exception("无运营商报表各国详情数据！");
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
			int reportType = operatorsAreaMtDataReportVo.getReporttype();
			String startTime = operatorsAreaMtDataReportVo.getStartTime();
			String endTime = operatorsAreaMtDataReportVo.getEndTime();

			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy年M月d日");
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);
			String timestr = "";
			if(reportType == 2){
				timestr = sdf0.format(df.parse(startTime))+"至"+sdf0.format(df.parse(endTime));
			}
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

				XSSFCell[] cell1 = new XSSFCell[9];
				XSSFCell[] cell2 = new XSSFCell[9];
				int k;
				for (k = f * intRowsOfPage; k < intCnt; k++) {
					OperatorsAreaMtDataReportVo mt = (OperatorsAreaMtDataReportVo) mtdList
							.get(k);

					// 时间
					if(reportType == 2){
					}else if(reportType == 1){
						timestr = mt.getY()+"年";
					}else if(reportType == 0){
						timestr = mt.getY()+"年"+mt.getImonth()+"月";
					}else{
						timestr = "";
					}
					String sendTime = timestr;
					
					//国家地区代码
					String areacode = mt.getAreacode();
					//国家地区名称
					String areaname = mt.getAreaname();
					//通道账号
					String spgate = mt.getSpgate();
					//通道名称
					String gatename = mt.getGatename();
					//发送类型
					String sendtypename="";
					if(mt.getSpType()!=null){
						sendtypename=mt.getSpType()==1?"EMP应用":"EMP接入";
						if(mt.getSendType()!=null){
							if(mt.getSendType()==1){
								sendtypename=sendtypename+"(EMP发送)";
							}else if(mt.getSendType()==2){
								sendtypename=sendtypename+"(HTTP接入)";
							}else if(mt.getSendType()==3){
								sendtypename=sendtypename+"(DB接入)";
							}else if(mt.getSendType()==4){
								sendtypename=sendtypename+"(直连接入)";
							}
						}
					}
					String sendtype = sendtypename;
					// sp账号
					String spId = mt.getSpID() != null ? (!"".equals(mt
							.getSpID().trim()) ? mt.getSpID().replaceAll(mt.getSpID(), "******") : "未知") : "未知";
					
					// 发送总数
					String icount = mt.getIcount().toString();
					// 接收失败数
					String rfail = mt.getRfail().toString();
					
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);
					cell1[3] = row.createCell(3);
					cell1[4] = row.createCell(4);
					cell1[5] = row.createCell(5);
					cell1[6] = row.createCell(6);
					cell1[7] = row.createCell(7);
					cell1[8] = row.createCell(8);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					cell1[3].setCellStyle(cellStyle[0]);
					cell1[4].setCellStyle(cellStyle[0]);
					cell1[5].setCellStyle(cellStyle[0]);
					cell1[6].setCellStyle(cellStyle[0]);
					cell1[7].setCellStyle(cellStyle[0]);
					cell1[8].setCellStyle(cellStyle[0]);
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(areacode);
					cell1[2].setCellValue(areaname);
					cell1[3].setCellValue(spId);
					cell1[4].setCellValue(spgate);
					cell1[5].setCellValue(gatename);
					cell1[6].setCellValue(sendtype);
					cell1[7].setCellValue(icount);
					cell1[8].setCellValue(rfail);

					// 一页里的行数
					index++;
				}

				row = sheet.createRow(k + 1);
				// 生成四个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(2);
				cell2[3] = row.createCell(3);
				cell2[4] = row.createCell(4);
				cell2[5] = row.createCell(5);
				cell2[6] = row.createCell(6);
				cell2[7] = row.createCell(7);
				cell2[8] = row.createCell(8);

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);
				cell2[4].setCellStyle(cellStyle[1]);
				cell2[5].setCellStyle(cellStyle[1]);
				cell2[6].setCellStyle(cellStyle[1]);
				cell2[7].setCellStyle(cellStyle[1]);
				cell2[8].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue("");
				cell2[3].setCellValue("");
				cell2[4].setCellValue("");
				cell2[5].setCellValue("");
				cell2[6].setCellValue("合计：");
				cell2[7].setCellValue(tCount);
				cell2[8].setCellValue(tFail == "" ? "0" : tFail);
				
				

			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "运营商各国发送详情统计报表" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"运营商统计报表生成excel导出异常");
		} finally {
			// 清除对象
			workbook = null;
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常");
				}
			}
			if(in != null){
				try{
					in.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常");
				}
			}
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}

	/**
	 * 生成运营商报表Excel--各国详情
	 * 
	 * @param mtdList
	 * @param queryTime
	 * @param count
	 * @param fail
	 * @param showTime
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createSpReportExcelByArea(
			List<OperatorsAreaMtDataReportVo> mtdList, String countTime,
			long totalIcount, long totalRfail1,long totalRfail2,long totalRnret,
			long totalRsucc,OperatorsAreaMtDataReportVo operatorsAreaMtDataReportVo,HttpServletRequest request) throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "运营商统计报表";
		String reportName = "SpMtReportArea";
		
		
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spMtReportArea.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("运营商报表各国详情导出工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无运营商报表各国详情数据！");
			throw new Exception("无运营商报表各国详情数据！");
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
			int reportType = operatorsAreaMtDataReportVo.getReporttype();
			String startTime = operatorsAreaMtDataReportVo.getStartTime();
			String endTime = operatorsAreaMtDataReportVo.getEndTime();

			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-M-d");
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);
			String timestr = "";
			if(reportType == 2){
				timestr = sdf0.format(df.parse(startTime))+"  "+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_z", request)+"  "+sdf0.format(df.parse(endTime));
			}
			
			for (int f = 0; f < size; f++) {
				
				fileName = reportName + "_" + sdf.format(curDate) + "_["
						+ (f + 1) + "]"+"_"+ StaticValue.getServerNumber() + ".xlsx";
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				workbook.setSheetName(0, reportName);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook);
//				workbook.removeSheetAt(0);
				
				//获取模板第一个工作簿
				Sheet sheetTemp = workbook.getSheetAt(0);
				//获取模板第一行标题
				Row rowTitle = sheetTemp.getRow(0);

				//加载配置列
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
				//标题单元格索引，从6开始，因为已经有7个单元格
				int cellTitleIndex = 6;
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

				Cell[] cell1 = new Cell[7+rptConList.size()];
				Cell[] cell2 = new Cell[7+rptConList.size()];
				int k;
				for (k = f * intRowsOfPage; k < intCnt; k++) {
					OperatorsAreaMtDataReportVo mt = (OperatorsAreaMtDataReportVo) mtdList
							.get(k);

					// 时间
					if(reportType == 2){
					}else if(reportType == 1){
						timestr = mt.getY();
					}else if(reportType == 0){
						timestr = mt.getY()+"-"+mt.getImonth();
					}else{
						timestr = "";
					}
					String sendTime = timestr;
					
					//国家地区代码
					String areacode = mt.getAreacode()!=null?mt.getAreacode():"";
					//国家地区名称
					String areaname = mt.getAreaname()!=null?mt.getAreaname():"";
					//通道账号
					String spgate = mt.getSpgate()!=null?mt.getSpgate():"";
					//通道名称
					String gatename = mt.getGatename()!=null?mt.getGatename():"";
					//发送类型
					String sendtypename="";
					if(mt.getSpType()!=null){
						sendtypename=mt.getSpType()==1?MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empyy",request):MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empjr",request);
						if(mt.getSendType()!=null){
							if(mt.getSendType()==1){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_fs",request)+")";
							}else if(mt.getSendType()==2){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_httpjr",request)+")";
							}else if(mt.getSendType()==3){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_dbjr",request)+")";
							}else if(mt.getSendType()==4){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_zljr",request)+")";
							}
						}
					}
					String sendtype = sendtypename;
					// sp账号
					String spId = mt.getSpID() != null ? (!"".equals(mt
							.getSpID().trim()) ? mt.getSpID().replaceAll(mt.getSpID(), "******") : MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_wz",request)) : MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_wz",request);
					
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
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
					
					row = sheet.createRow(k + 1);
					// 生成7个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);
					cell1[3] = row.createCell(3);
					cell1[4] = row.createCell(4);
					cell1[5] = row.createCell(5);
					cell1[6] = row.createCell(6);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					cell1[3].setCellStyle(cellStyle[0]);
					cell1[4].setCellStyle(cellStyle[0]);
					cell1[5].setCellStyle(cellStyle[0]);
					cell1[6].setCellStyle(cellStyle[0]);
					
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(areacode);
					cell1[2].setCellValue(areaname);
					cell1[3].setCellValue(spId);
					cell1[4].setCellValue(spgate);
					cell1[5].setCellValue(gatename);
					cell1[6].setCellValue(sendtype);
					
					//标题单元格索引，从6开始，因为已经有7个单元格
					int cellDataIndex = 6;
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
				// 生成7个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(2);
				cell2[3] = row.createCell(3);
				cell2[4] = row.createCell(4);
				cell2[5] = row.createCell(5);
				cell2[6] = row.createCell(6);

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);
				cell2[4].setCellStyle(cellStyle[1]);
				cell2[5].setCellStyle(cellStyle[1]);
				cell2[6].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue("");
				cell2[3].setCellValue("");
				cell2[4].setCellValue("");
				cell2[5].setCellValue("");
				cell2[6].setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_yystjbb_hj",request));
				
				//标题单元格索引，从6开始，因为已经有7个单元格
				int cellTotalIndex = 6;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(totalIcount, totalRsucc, totalRfail1, totalRfail2, totalRnret, RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
				for(RptConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(totalIcount);
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
						cell1[cellTotalIndex].setCellValue(totalRfail1);
					}
					else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(totalRfail2);
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
						cell1[cellTotalIndex].setCellValue(totalRnret);
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
			
			fileName = MessageUtils.extractMessage("cxtj","cxtj_new_yysgj",request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() +  ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"运营商统计报表生成excel导出异常");
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
