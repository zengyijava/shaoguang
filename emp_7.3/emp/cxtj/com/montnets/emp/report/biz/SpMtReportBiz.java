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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.report.bean.RptConfInfo;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.dao.GenericSpMtDataReportVoDAO;
import com.montnets.emp.report.vo.CountReportVo;
import com.montnets.emp.report.vo.SpMtDataDetailVo;
import com.montnets.emp.report.vo.SpMtDataNationVo;
import com.montnets.emp.report.vo.SpMtDataReportVo;
import com.montnets.emp.report.vo.SpMtNationVo;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.GlobalConst;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;

/**
 * 发送账号下行统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:45:50
 * @description
 */
public class SpMtReportBiz{
	
	BaseBiz baseBiz = new BaseBiz();

	/**
	 * 根据当前用户ID获取下发过信息的SP账号
	 * @param userID
	 * @return
	 */
	public String getlFmtTaskSpuser(String userID){
		StringBuffer tempSpUserId = new StringBuffer();
		String sql = null;
		if("2".equals(userID)){//如果是系统总管理员
			sql = "SELECT USERID AS SP_USER  FROM USERDATA";
		}else{
			sql = "SELECT SP_USER FROM LF_MTTASK WHERE USER_ID = "+userID+" GROUP BY SP_USER";
		}
		List<DynaBean> dynaBeanList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		for(DynaBean dynaBean : dynaBeanList){
			//需要加上单引号
			tempSpUserId.append("'");
			tempSpUserId.append(dynaBean.get("sp_user"));
			tempSpUserId.append("'");
			tempSpUserId.append(",");
		}
		String spUserId = tempSpUserId.toString();
		spUserId = spUserId.substring(0,spUserId.lastIndexOf(","));
		return spUserId;
	}

	/**
	 * 发送账号下行统计报表   月报表
	 * @param mtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<SpMtDataReportVo> getReportInfosByMonth(
			SpMtDataReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {
		//发送账号下行统计报表集合
		List<SpMtDataReportVo> mtDRVosList;

		try {
			//判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericSpMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByMonth(mtDataReportVo);
			} else {
				mtDRVosList = new GenericSpMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByMonth(mtDataReportVo,pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送账号下行统计报表   月报表异常");
			throw e;
		}
		return mtDRVosList;
	}
	
	/**
	 * 发送账号下行统计报表   月报表(国家类型列表)
	 * @param mtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<SpMtNationVo> getReportInfosByMonthNation(
			SpMtDataReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {
		//发送账号下行统计报表集合
		List<SpMtNationVo> mtDRVosList;

		try {
			//判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericSpMtDataReportVoDAO()
						.findMtDataVoByMonthNation(mtDataReportVo);
			} else {
				mtDRVosList = new GenericSpMtDataReportVoDAO()
						.findMtDataByMonthNation(mtDataReportVo,pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送账号下行统计报表   月报表异常");
			throw e;
		}
		return mtDRVosList;
	}

	/**
	 * 发送账号下行统计报表 年报表
	 * @param mtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<SpMtDataReportVo> getReportInfosByYear(
			SpMtDataReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {

		//发送账号下行统计报表集合
		List<SpMtDataReportVo> mtDRVosList;
		try {

			//判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericSpMtDataReportVoDAO().findOperatorsMtDataReportVoByYear(mtDataReportVo);
			} else {
				mtDRVosList = new GenericSpMtDataReportVoDAO().findOperatorsMtDataReportVoByYear(mtDataReportVo,pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送账号下行统计报表 年报表异常");
			throw e;
		}
		//返回sp账号结果集
		return mtDRVosList;
	}
	
	/**
	 * 发送账号下行统计报表 日报表
	 * @param mtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<SpMtDataReportVo> getReportInfosByDay(
			SpMtDataReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {
		

		//发送账号下行统计报表集合
		List<SpMtDataReportVo> mtDRVosList;
		
		
		try {
			if(pageInfo==null){
				mtDRVosList = new GenericSpMtDataReportVoDAO().findOperatorsMtDataReportVoByDay(mtDataReportVo);
			}else{
				mtDRVosList = new GenericSpMtDataReportVoDAO().findOperatorsMtDataReportVoByDay(mtDataReportVo,pageInfo);
			}


		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送账号下行统计报表 日报表异常");
			throw e;
		}
		
		
		
		//返回sp账号结果集
		return mtDRVosList;
	}
	
	
	
	
	
	/**
	 * 发送账号下行统计报表 日,月报表
	 * @param mtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<SpMtDataDetailVo> getReportInfoDetail(
			SpMtDataReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {

		//发送账号下行统计报表集合
		List<SpMtDataDetailVo> mtDRVosList;
		try {
			//报表的导出需要用
			if(pageInfo==null){
				mtDRVosList = new GenericSpMtDataReportVoDAO().findMtDataReportDayDetailUnpage(mtDataReportVo);
			}else{
				mtDRVosList = new GenericSpMtDataReportVoDAO().findMtDataReportDayDetail(mtDataReportVo,pageInfo);
			}

			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送账号下行统计报表 日报表异常");
			throw e;
		}
		//返回sp账号结果集
		return mtDRVosList;
	}
	
	/**
	 * 发送账号下行统计报表 日,月报表
	 * @param mtDataReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<SpMtDataNationVo> getReportInfoNation(
			SpMtDataReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {

		//发送账号下行统计报表集合
		List<SpMtDataNationVo> mtDRVosList;
		try {
			//报表的导出需要用
			if(pageInfo==null){
				mtDRVosList = new GenericSpMtDataReportVoDAO().findMtDataReportDayNationUnpage(mtDataReportVo);
			}else{
				mtDRVosList = new GenericSpMtDataReportVoDAO().findMtDataReportDayNation(mtDataReportVo,pageInfo);
			}

			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送账号下行统计报表 日报表异常");
			throw e;
		}
		//返回sp账号结果集
		return mtDRVosList;
	}
	
	
	

	/**
	 * 发送账号下行统计报   合计
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	
    public long[] findSumCount(SpMtDataReportVo operatorsMtDataReportVo) throws Exception {
		//获取合计数存数数组中
		long[] count = new GenericSpMtDataReportVoDAO().findSumCount(
				operatorsMtDataReportVo);
		return count;
	}
	
	/**
	 * 发送账号下行统计报   合计 --增加国家类型
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCountNation(SpMtDataReportVo operatorsMtDataReportVo) throws Exception {
		//获取合计数存数数组中
		long[] count = new GenericSpMtDataReportVoDAO().findSumCountNation(
				operatorsMtDataReportVo);
		return count;
	}
	
	/**
	 * @description：生成SP发送账号下行统计报表Excel
	 * @param fileName
	 *            文件名
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-19 创建
	 */

	public Map<String, String> createSpExcel(
			SpMtDataReportVo spisuncmMtDataReportVo,
			List<SpMtDataReportVo> mtdList, String faill, String succc)
			throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "spExcelReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spExcelReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("SP账号统计报表导出生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无SP账号统计报表数据！");
			throw new Exception("无SP账号统计报表数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;

		OutputStream os = null;
		InputStream in = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			for (int j = 1; j <= size; j++) {
				//文件名
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j
						+ "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				//读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, reportName);
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook1);
				in.close();
				// 读取模板工作表
				Row row = null;

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
				int index = 0;
				String name = "合计:";

				Cell[] cell1 = new Cell[8];
				Cell[] cell2 = new Cell[8];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

					SpMtDataReportVo mt = (SpMtDataReportVo) mtdList.get(k);

					// 时间
					String showTime = "";
					String begintime=spisuncmMtDataReportVo.getSendTime();
					String endtime=spisuncmMtDataReportVo.getEndTime();
					if("2".equals(spisuncmMtDataReportVo.getReportType())){
					String btime="";
					String etime="";
					if(!"".equals(begintime) && null != begintime && 0 != begintime.length())
					{
						String btemp[] = begintime.split("-");
						btime = btemp[0] + "年" + btemp[1] + "月" + btemp[2] + "日";
					}

					if(!"".equals(endtime) && null != endtime && 0 != endtime.length())
					{
						String etemp[] = endtime.split("-");
						etime = etemp[0] + "年" + etemp[1] + "月" + etemp[2] + "日";
					}
					showTime = btime + " 至 " + etime;
					}else if("0".equals(spisuncmMtDataReportVo.getReportType())){
						showTime=mt.getY()+"年"+mt.getImonth()+"月"	;
					}else {
						showTime=mt.getY()+"年";
					}
					// 发送账号ID
					String userid = mt.getUserid() != null ? mt.getUserid(): "";
					//账户名称
					String staffname=mt.getStaffname()!=null?mt.getStaffname():"--";
					//账户类型
					String sptypename=mt.getSptype()!=null?(mt.getSptype()==1?"EMP应用":"EMP接入"):"--";
					//发送类型
					String sendtypename="";
					if(mt.getSptype()!=null){
						sendtypename=mt.getSptype()==1?"EMP应用":"EMP接入";
						if(mt.getSendtype()!=null){
							if(mt.getSendtype()==1){
								sendtypename=sendtypename+"(EMP发送)";
							}else if(mt.getSendtype()==2){
								sendtypename=sendtypename+"(HTTP接入)";
							}else if(mt.getSendtype()==3){
								sendtypename=sendtypename+"(DB接入)";
							}else if(mt.getSendtype()==4){
								sendtypename=sendtypename+"(直连接入)";
							}
						}
					}
					//成功数
					String chenggong = mt.getIcount() != null ? mt.getIcount().toString() : "";
					// 接收失败数
					String rfail = mt.getRfail2() != null ? mt.getRfail2().toString() : "";
					String spisuncm=spisuncmMtDataReportVo.getSpisuncm();
					
					if("".equals(spisuncmMtDataReportVo.getSpisuncm())){
						spisuncm="全部";
					}else if(mt.getSpisuncm()!=null){
						if("5".equals(mt.getSpisuncm())){
							spisuncm="国外";
						}else if("0".equals(mt.getSpisuncm())||"1".equals(mt.getSpisuncm())||"21".equals(mt.getSpisuncm())){
							spisuncm="国内";
						}else{
							spisuncm="未知";
						}
					}else{
						spisuncm="未知";
					}
					
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

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					cell1[3].setCellStyle(cellStyle[0]);
					cell1[4].setCellStyle(cellStyle[0]);
					cell1[5].setCellStyle(cellStyle[0]);
					cell1[6].setCellStyle(cellStyle[0]);
					cell1[7].setCellStyle(cellStyle[0]);
					
					// 设置单元格内容
					cell1[0].setCellValue(showTime);
					cell1[1].setCellValue(userid);
					cell1[2].setCellValue(staffname);
					cell1[3].setCellValue(sptypename);
					cell1[4].setCellValue(sendtypename);
					cell1[5].setCellValue(chenggong);
					cell1[6].setCellValue(rfail);
					cell1[7].setCellValue(spisuncm);
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

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);
				cell2[4].setCellStyle(cellStyle[1]);
				cell2[5].setCellStyle(cellStyle[1]);
				cell2[6].setCellStyle(cellStyle[1]);
				cell2[7].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue("");
				cell2[3].setCellValue("");
				cell2[4].setCellValue(name);
				cell2[5].setCellValue(succc);
				cell2[6].setCellValue(faill == "" ? "0" : faill);
				cell2[7].setCellValue("-");
			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "SP账号统计报表" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"SP帐号统计报表生成excel导出异常");
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
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}
	
	
	
	/**
	 * @description：生成SP发送账号下行统计报表Excel
	 * @param fileName
	 *            文件名
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-19 创建
	 */

	public Map<String, String> createSpExcelV1(
			SpMtDataReportVo spisuncmMtDataReportVo,
			List<SpMtDataReportVo> mtdList, CountReportVo countrptvo,HttpServletRequest request)
			throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "spExcelReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spExcelReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("SP账号统计报表导出生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无SP账号统计报表数据！");
			throw new Exception("无SP账号统计报表数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;

		OutputStream os = null;
		InputStream in = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			for (int j = 1; j <= size; j++) {
				//文件名
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j
						+ "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				//读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, reportName);
				
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook1);
				//获取模板第一个工作簿
				Sheet sheetTemp = workbook1.getSheetAt(0);
				//获取模板第一行标题
				Row rowTitle = sheetTemp.getRow(0);
				//加载配置列
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
				//标题单元格索引，从5开始，因为已经有6个单元格
				int cellTitleIndex = 5;
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_sj", request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_spzh", request));
				rowTitle.getCell(2).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zhmc", request));
				rowTitle.getCell(3).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zhlx", request));
				rowTitle.getCell(4).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_fslx", request));
				rowTitle.getCell(5).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_yys", request));
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
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				in.close();
				// 读取模板工作表
				Row row = null;

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
				int index = 0;
				String name = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_hj",request);

				Cell[] cell1 = new Cell[6+rptConList.size()];
				Cell[] cell2 = new Cell[6+rptConList.size()];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

					SpMtDataReportVo mt = (SpMtDataReportVo) mtdList.get(k);

					// 时间
					String showTime = "";
					String begintime=spisuncmMtDataReportVo.getSendTime();
					String endtime=spisuncmMtDataReportVo.getEndTime();
					if("2".equals(spisuncmMtDataReportVo.getReportType())){
					String btime="";
					String etime="";
					if(!"".equals(begintime) && null != begintime && 0 != begintime.length())
					{
						String btemp[] = begintime.split("-");
						btime = btemp[0] + "-" + btemp[1] + "-" + btemp[2] ;
					}

					if(!"".equals(endtime) && null != endtime && 0 != endtime.length())
					{
						String etemp[] = endtime.split("-");
						etime = etemp[0] + "-" + etemp[1] + "-" + etemp[2];
					}
					showTime = btime + "  "+MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_z",request)+"  " + etime;
					}else if("0".equals(spisuncmMtDataReportVo.getReportType())){
						showTime=mt.getY()+"-"+mt.getImonth();
					}else {
						showTime=mt.getY();
					}
					// 发送账号ID
					String userid = mt.getUserid() != null ? mt.getUserid(): "";
					//账户名称
					String staffname=mt.getStaffname()!=null?mt.getStaffname():"--";
					//账户类型
					String sptypename=mt.getSptype()!=null?(mt.getSptype()==1?MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empyy",request):MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empjr",request)):"--";
					//发送类型
					String sendtypename="";
					if(mt.getSptype()!=null){
						sendtypename=mt.getSptype()==1?MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empyy",request):MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empjr",request);
						if(mt.getSendtype()!=null){
							if(mt.getSendtype()==1){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_fs",request)+")";
							}else if(mt.getSendtype()==2){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_httpjr",request)+")";
							}else if(mt.getSendtype()==3){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_dbjr",request)+")";
							}else if(mt.getSendtype()==4){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_zljr",request)+")";
							}
						}
					}
					String spisuncm=spisuncmMtDataReportVo.getSpisuncm();
					if("".equals(spisuncmMtDataReportVo.getSpisuncm())){
						spisuncm=MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_qb",request);
					}else if(mt.getSpisuncm()!=null){
						if("5".equals(mt.getSpisuncm())){
							spisuncm=MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_gw",request);
						}else if("0".equals(mt.getSpisuncm())||"1".equals(mt.getSpisuncm())||"21".equals(mt.getSpisuncm())){
							spisuncm=MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_gn",request);
						}else{
							spisuncm=MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_wz",request);
						}
					}else{
						spisuncm=MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_wz",request);
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
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);
					cell1[3] = row.createCell(3);
					cell1[4] = row.createCell(4);
					cell1[5] = row.createCell(5);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					cell1[3].setCellStyle(cellStyle[0]);
					cell1[4].setCellStyle(cellStyle[0]);
					cell1[5].setCellStyle(cellStyle[0]);
					
					// 设置单元格内容
					cell1[0].setCellValue(showTime);
					cell1[1].setCellValue(userid);
					cell1[2].setCellValue(staffname);
					cell1[3].setCellValue(sptypename);
					cell1[4].setCellValue(sendtypename);
					cell1[5].setCellValue(spisuncm);
					
					//标题单元格索引，从5开始，因为已经有6个单元格
					int cellDataIndex = 5;
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
				// 生成四个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(2);
				cell2[3] = row.createCell(3);
				cell2[4] = row.createCell(4);
				cell2[5] = row.createCell(5);

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);
				cell2[4].setCellStyle(cellStyle[1]);
				cell2[5].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue("");
				cell2[3].setCellValue("");
				cell2[4].setCellValue("");
				cell2[5].setCellValue(name);
				
				//标题单元格索引，从5开始，因为已经有6个单元格
				int cellTotalIndex = 5;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(countrptvo.getIcount(), countrptvo.getRsucc(), countrptvo.getRfail1(), countrptvo.getRfail2(), countrptvo.getRnret(), RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
				for(RptConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(countrptvo.getIcount());
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
						cell1[cellTotalIndex].setCellValue(countrptvo.getRfail1());
					}
					else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(countrptvo.getRfail2());
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
						cell1[cellTotalIndex].setCellValue(countrptvo.getRnret());
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
			workbook.write(os);

			fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_spzhtjbb", request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"SP帐号统计报表生成excel导出异常");
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
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}
	
	
	
	
	/**
	 * @description：生成SP发送账号下行统计报表Excel
	 * @param fileName
	 *            文件名
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-19 创建
	 */

	
    public Map<String, String> createSpMtReportExcel(
			SpMtDataReportVo spisuncmMtDataReportVo,
			List<SpMtDataReportVo> mtdList, String faill, String succc)
			throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "SpReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("SP账号统计报表导出生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无SP账号统计报表数据！");
			throw new Exception("无SP账号统计报表数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook = null;

		OutputStream os = null;
		InputStream in = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			in = new FileInputStream(file);

			for (int j = 1; j <= size; j++) {

				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j
						+ "].xlsx";
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook);
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
				int index = 0;
				String name = "合计:";

				XSSFCell[] cell1 = new XSSFCell[7];
				XSSFCell[] cell2 = new XSSFCell[7];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

					SpMtDataReportVo mt = (SpMtDataReportVo) mtdList.get(k);

					// 时间
					String iymd = spisuncmMtDataReportVo.getImonth() == null ? mt.getY()+ "年": mt.getY()+ "年"
							+ (mt.getImonth().length() < 2 ? mt.getImonth().substring(0, 1) : mt.getImonth()) + "月";
					// 发送账号ID
					String userid = mt.getUserid() != null ? mt.getUserid(): "";
					//账户名称
					String staffname=mt.getStaffname()!=null?mt.getStaffname():"--";
					//账户类型
					String sptypename=mt.getSptype()!=null?(mt.getSptype()==1?"EMP应用":"EMP接入"):"--";
					//发送类型
					String sendtypename="";
					if(mt.getSptype()!=null){
						sendtypename=mt.getSptype()==1?"EMP应用":"EMP接入";
						if(mt.getSendtype()!=null){
							if(mt.getSendtype()==1){
								sendtypename=sendtypename+"(EMP发送)";
							}else if(mt.getSendtype()==2){
								sendtypename=sendtypename+"(HTTP接入)";
							}else if(mt.getSendtype()==3){
								sendtypename=sendtypename+"(DB接入)";
							}else if(mt.getSendtype()==4){
								sendtypename=sendtypename+"(直连接入)";
							}
						}
					}
					//成功数
					String chenggong = mt.getIcount() != null ? mt.getIcount().toString() : "";
					// 接收失败数
					String rfail = mt.getRfail2() != null ? mt.getRfail2().toString() : "";


					row = sheet.createRow(k + 1);
					// 生成四个单元格
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
					cell1[0].setCellValue(iymd);
					cell1[1].setCellValue(userid);
					cell1[2].setCellValue(staffname);
					cell1[3].setCellValue(sptypename);
					cell1[4].setCellValue(sendtypename);
					cell1[5].setCellValue(chenggong);
					cell1[6].setCellValue(rfail);

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
				cell2[4].setCellValue(name);
				cell2[5].setCellValue(succc);
				cell2[6].setCellValue("".equals(faill) ? "0" : faill);

			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "SP账号统计报表" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"SP帐号统计报表生成excel导出异常");
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
	 * @description：生成SP下行统计报表Excel(国家类型详细)
	 * @param fileName
	 *            文件名
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-19 创建
	 */
	public Map<String, String> createSpMtReportNationExcel(List<SpMtDataNationVo> mtdList,String begintime,String endtime,int reportType,String faill, String succc)
			throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "SpReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spNationReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("SP账号统计报表国家详情生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无SP账号统计报表国家详情数据！");
			throw new Exception("无SP账号统计报表国家详情数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;

		OutputStream os = null;
		InputStream in = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			for (int j = 1; j <= size; j++) {
				//文件名
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j
						+ "].xlsx";
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, reportName);
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook1);
				in.close();
				// 读取模板工作表
				Row row = null;

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
				int index = 0;
				String name = "合计:";

				Cell[] cell1 = new Cell[9];
				Cell[] cell2 = new Cell[9];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

					SpMtDataNationVo mt = (SpMtDataNationVo) mtdList.get(k);
					
					// 时间

					String showTime = "";
					if(reportType==2){
					String btime="";
					String etime="";
					if(!"".equals(begintime) && null != begintime && 0 != begintime.length())
					{
						String btemp[] = begintime.split("-");
						btime = btemp[0] + "年" + btemp[1] + "月" + btemp[2] + "日";
					}

					if(!"".equals(endtime) && null != endtime && 0 != endtime.length())
					{
						String etemp[] = endtime.split("-");
						etime = etemp[0] + "年" + etemp[1] + "月" + etemp[2] + "日";
					}
					showTime = btime + " 至 " + etime;
					}else if(reportType==0){
						showTime=mt.getY()+"年"+mt.getImonth()+"月"	;
					}else {
						showTime=mt.getY()+"年";
					}
					

					// 国家/地区代码
					String nationcode = mt.getNationcode()==null?"未知":mt.getNationcode();
					//国家名称
					String nationname=mt.getNationname()!=null?mt.getNationname():"未知";
					//SP账号
					String userid=mt.getUserid()==null?"未知":mt.getUserid();
					//通道号码
					String spgatecode=   mt.getSpgatecode()==null?"未知":mt.getSpgatecode();
					//通道名称
					String spgatename=  mt.getSpgatename()==null?"未知":mt.getSpgatename();
					//发送类型
					String sendtypename="";
					if(mt.getSptype()!=null){
						sendtypename=mt.getSptype()==1?"EMP应用":"EMP接入";
						if(mt.getSendtype()!=null){
							if(mt.getSendtype()==1){
								sendtypename=sendtypename+"(EMP发送)";
							}else if(mt.getSendtype()==2){
								sendtypename=sendtypename+"(HTTP接入)";
							}else if(mt.getSendtype()==3){
								sendtypename=sendtypename+"(DB接入)";
							}else if(mt.getSendtype()==4){
								sendtypename=sendtypename+"(直连接入)";
							}
						}
					}
					//成功数
					String chenggong = mt.getIcount() != null ? mt.getIcount().toString() : "";
					// 接收失败数
					String rfail = mt.getRfail2() != null ? mt.getRfail2().toString() : "";


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
					cell1[0].setCellValue(showTime);
					cell1[1].setCellValue(nationcode);
					cell1[2].setCellValue(nationname);
					cell1[3].setCellValue(userid);
					cell1[4].setCellValue(spgatecode);
					cell1[5].setCellValue(spgatename);
					cell1[6].setCellValue(sendtypename);
					cell1[7].setCellValue(chenggong);
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
				cell2[6].setCellValue(name);
				cell2[7].setCellValue(succc);
				cell2[8].setCellValue(faill == "" ? "0" : faill);

			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "SP账号各国详情统计报表" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"SP账号各国详情统计报表生成excel导出异常");
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
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}
	
	
	
	/**
	 * @description：生成SP下行统计报表Excel(国家类型详细)
	 * @param fileName
	 *            文件名
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-19 创建
	 */
	public Map<String, String> createSpMtReportNationExcelV1(List<SpMtDataNationVo> mtdList,String begintime,String endtime,
							int reportType,CountReportVo countrptvo,HttpServletRequest request)
			throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "SpReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spNationReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("SP账号统计报表国家详情生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无SP账号统计报表国家详情数据！");
			throw new Exception("无SP账号统计报表国家详情数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;

		OutputStream os = null;
		InputStream in = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			for (int j = 1; j <= size; j++) {
				//文件名
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j
						+ "].xlsx";
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, reportName);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook1);
				//获取模板第一个工作簿
				Sheet sheetTemp = workbook1.getSheetAt(0);
				//获取模板第一行标题
				Row rowTitle = sheetTemp.getRow(0);
				//加载配置列
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
				//标题单元格索引，从6开始，因为已经有7个单元格
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_sj", request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gjdqdm", request));
				rowTitle.getCell(2).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_new_rpt_gjmc", request));
				rowTitle.getCell(3).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_spzh", request));
				rowTitle.getCell(4).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_tdhm", request));
				rowTitle.getCell(5).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_tdmc", request));
				rowTitle.getCell(6).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_fslx", request));
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
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				in.close();
				// 读取模板工作表
				Row row = null;

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
				int index = 0;
				String name = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_hj",request);

				Cell[] cell1 = new Cell[7+rptConList.size()];
				Cell[] cell2 = new Cell[7+rptConList.size()];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

					SpMtDataNationVo mt = (SpMtDataNationVo) mtdList.get(k);
					
					// 时间

					String showTime = "";
					if(reportType==2){
					String btime="";
					String etime="";
					if(!"".equals(begintime) && null != begintime && 0 != begintime.length())
					{
						String btemp[] = begintime.split("-");
						btime = btemp[0] + "-" + btemp[1] + "-" + btemp[2];
					}

					if(!"".equals(endtime) && null != endtime && 0 != endtime.length())
					{
						String etemp[] = endtime.split("-");
						etime = etemp[0] + "-" + etemp[1] + "-" + etemp[2] ;
					}
					showTime = btime + "  "+MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_z",request)+"  " + etime;
					}else if(reportType==0){
						showTime=mt.getY()+"-"+mt.getImonth()	;
					}else {
						showTime=mt.getY();
					}
					String nationName = MessageUtils.extractMessage("cxtj", "cxtj_country_"+mt.getNationcode(), request);

					// 国家/地区代码
					String nationcode = mt.getNationcode()==null?MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_wz",request):mt.getNationcode();
					//国家名称
					String nationname=mt.getNationname()!=null?nationName:MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_wz",request);
					//SP账号
					String userid=mt.getUserid()==null?MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_wz",request):mt.getUserid();
					//通道号码
					String spgatecode=   mt.getSpgatecode()==null?MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_wz",request):mt.getSpgatecode();
					//通道名称
					String spgatename=  mt.getSpgatename()==null?MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_wz",request):mt.getSpgatename();
					//发送类型
					String sendtypename="";
					if(mt.getSptype()!=null){
						sendtypename=mt.getSptype()==1?MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empyy",request):MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empjr",request);
						if(mt.getSendtype()!=null){
							if(mt.getSendtype()==1){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_fs",request)+")";
							}else if(mt.getSendtype()==2){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_httpjr",request)+")";
							}else if(mt.getSendtype()==3){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_dbjr",request)+")";
							}else if(mt.getSendtype()==4){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_zljr",request)+")";
							}
						}
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
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
					row = sheet.createRow(k + 1);
					// 生成四个单元格
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
					cell1[0].setCellValue(showTime);
					cell1[1].setCellValue(nationcode);
					cell1[2].setCellValue(nationname);
					cell1[3].setCellValue(userid);
					cell1[4].setCellValue(spgatecode);
					cell1[5].setCellValue(spgatename);
					cell1[6].setCellValue(sendtypename);
					
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
				// 生成四个单元格
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
				cell2[6].setCellValue(name);
				
				//标题单元格索引，从6开始，因为已经有7个单元格
				int cellTotalIndex = 6;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(countrptvo.getIcount(), countrptvo.getRsucc(), countrptvo.getRfail1(), countrptvo.getRfail2(), countrptvo.getRnret(), RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
				for(RptConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(countrptvo.getIcount());
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
						cell1[cellTotalIndex].setCellValue(countrptvo.getRfail1());
					}
					else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(countrptvo.getRfail2());
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
						cell1[cellTotalIndex].setCellValue(countrptvo.getRnret());
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
			workbook.write(os);

			fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_spzhgj", request)  + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"SP账号各国详情统计报表生成excel导出异常");
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
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}
	
	
	
	
	
	/**
	 * @description：生成SP发送账号下行统计报表Excel
	 * @param fileName
	 *            文件名
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-19 创建
	 */
	public Map<String, String> createSpMtReportExcel(List<SpMtDataDetailVo> mtdList,int reportType,String faill, String succc)
			throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "SpReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("SP账号统计报表导出生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无SP账号统计报表数据！");
			throw new Exception("无SP账号统计报表数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;

		OutputStream os = null;
		InputStream in = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			for (int j = 1; j <= size; j++) {
				//文件名
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j
						+ "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, reportName);
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook1);
				in.close();
				// 读取模板工作表
				Row row = null;

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
				int index = 0;
				String name = "合计:";

				Cell[] cell1 = new Cell[7];
				Cell[] cell2 = new Cell[7];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

					SpMtDataDetailVo mt = (SpMtDataDetailVo) mtdList.get(k);
					
					// 时间
					String iymd = "";
					if("1".equals(reportType+"")){//如果选择的是年
						if(mt.getY()!=null&&mt.getImonth()!=null){
							iymd=mt.getY()+"年"+mt.getImonth()+"月";
						}else if(mt.getY()!=null){
							iymd=mt.getY()+"年";
						}else{
							iymd="未知";
						}
					}else if(mt.getIymd()!=null&&mt.getIymd().length()==8){
						String date=mt.getIymd();
						iymd=date.substring(0,4)+"年"+date.substring(4,6)+"月"+date.substring(6,8)+"日";
					}else {
						iymd="未知";
					}

					// 发送账号ID
					String userid = mt.getUserid() != null ? mt.getUserid(): "";
					//账户名称
					String staffname=mt.getStaffname()!=null?mt.getStaffname():"--";
					//账户类型
					String sptypename=mt.getSptype()!=null?(mt.getSptype()==1?"EMP应用":"EMP接入"):"--";
					//发送类型
					String sendtypename="";
					if(mt.getSptype()!=null){
						sendtypename=mt.getSptype()==1?"EMP应用":"EMP接入";
						if(mt.getSendtype()!=null){
							if(mt.getSendtype()==1){
								sendtypename=sendtypename+"(EMP发送)";
							}else if(mt.getSendtype()==2){
								sendtypename=sendtypename+"(HTTP接入)";
							}else if(mt.getSendtype()==3){
								sendtypename=sendtypename+"(DB接入)";
							}else if(mt.getSendtype()==4){
								sendtypename=sendtypename+"(直连接入)";
							}
						}
					}
					//成功数
					String chenggong = mt.getIcount() != null ? mt.getIcount().toString() : "";
					// 接收失败数
					String rfail = mt.getRfail2() != null ? mt.getRfail2().toString() : "";


					row = sheet.createRow(k + 1);
					// 生成四个单元格
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
					cell1[0].setCellValue(iymd);
					cell1[1].setCellValue(userid);
					cell1[2].setCellValue(staffname);
					cell1[3].setCellValue(sptypename);
					cell1[4].setCellValue(sendtypename);
					cell1[5].setCellValue(chenggong);
					cell1[6].setCellValue(rfail);

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
				cell2[4].setCellValue(name);
				cell2[5].setCellValue(succc);
				cell2[6].setCellValue(faill == "" ? "0" : faill);

			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "SP账号统计报表" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"SP帐号统计报表生成excel导出异常");
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
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}
	
	
	/**
	 * @description：生成SP发送账号下行统计报表Excel
	 * @param fileName
	 *            文件名
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-19 创建
	 */
	public Map<String, String> createSpMtReportExcelV1(List<SpMtDataDetailVo> mtdList,
					int reportType,CountReportVo countrptvo,HttpServletRequest request)
			throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "SpReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "spReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("SP账号统计报表导出生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无SP账号统计报表数据！");
			throw new Exception("无SP账号统计报表数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;

		OutputStream os = null;
		InputStream in = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			for (int j = 1; j <= size; j++) {
				//文件名
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j
						+ "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, reportName);
				
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook1);
				//获取模板第一个工作簿
				Sheet sheetTemp = workbook1.getSheetAt(0);
				//获取模板第一行标题
				Row rowTitle = sheetTemp.getRow(0);
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_sj", request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_spzh", request));
				rowTitle.getCell(2).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zhmc", request));
				rowTitle.getCell(3).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zhlx", request));
				rowTitle.getCell(4).setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_fslx", request));
				
				//加载配置列
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
				//标题单元格索引，从4开始，因为已经有5个单元格
				int cellTitleIndex = 4;
				
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
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				in.close();
				// 读取模板工作表
				Row row = null;

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
				int index = 0;
				String name = MessageUtils.extractMessage("cxtj","cxtj_sjcx_yystjbb_hj",request);

				Cell[] cell1 = new Cell[5+rptConList.size()];
				Cell[] cell2 = new Cell[5+rptConList.size()];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

					SpMtDataDetailVo mt = (SpMtDataDetailVo) mtdList.get(k);
					
					// 时间
					String iymd = "";
					if("1".equals(reportType+"")){//如果选择的是年
						if(mt.getY()!=null&&mt.getImonth()!=null){
							iymd=mt.getY()+"-"+mt.getImonth();
						}else if(mt.getY()!=null){
							iymd=mt.getY();
						}else{
							iymd=MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_wz",request);
						}
					}else if(mt.getIymd()!=null&&mt.getIymd().length()==8){
						String date=mt.getIymd();
						iymd=date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
					}else {
						iymd=MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_wz",request);
					}

					// 发送账号ID
					String userid = mt.getUserid() != null ? mt.getUserid(): "";
					//账户名称
					String staffname=mt.getStaffname()!=null?mt.getStaffname():"--";
					//账户类型
					String sptypename=mt.getSptype()!=null?(mt.getSptype()==1?MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empyy",request):MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empjr",request)):"--";
					//发送类型
					String sendtypename="";
					if(mt.getSptype()!=null){
						sendtypename=mt.getSptype()==1?MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empyy",request):MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_empjr",request);
						if(mt.getSendtype()!=null){
							if(mt.getSendtype()==1){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_fs",request)+")";
							}else if(mt.getSendtype()==2){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_httpjr",request)+")";
							}else if(mt.getSendtype()==3){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_dbjr",request)+")";
							}else if(mt.getSendtype()==4){
								sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_zhtjbb_zljr",request)+")";
							}
						}
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
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
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
					cell1[0].setCellValue(iymd);
					cell1[1].setCellValue(userid);
					cell1[2].setCellValue(staffname);
					cell1[3].setCellValue(sptypename);
					cell1[4].setCellValue(sendtypename);
					
					//标题单元格索引，从4开始，因为已经有5个单元格
					int cellDataIndex = 4;
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
				cell2[2].setCellValue("");
				cell2[3].setCellValue("");
				cell2[4].setCellValue(name);
				
				//标题单元格索引，从4开始，因为已经有5个单元格
				int cellTotalIndex = 4;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(countrptvo.getIcount(), countrptvo.getRsucc(), countrptvo.getRfail1(), countrptvo.getRfail2(), countrptvo.getRnret(), RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
				for(RptConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(countrptvo.getIcount());
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
						cell1[cellTotalIndex].setCellValue(countrptvo.getRfail1());
					}
					else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(countrptvo.getRfail2());
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
						cell1[cellTotalIndex].setCellValue(countrptvo.getRnret());
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
			workbook.write(os);

			fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_spzhtjbb", request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"SP帐号统计报表生成excel导出异常");
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
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}

    
    public String getPriUserCode(LfSysuser sysUser, String depIdStr, String userIdStr, boolean containSubDep) {
		StringBuilder result = new StringBuilder("(");
		//如果是个人权限，则只能查自己的。权限类型 1：个人权限  2：机构权限
        if(sysUser.getPermissionType() == 1) {
			//返回当前操作员的编码
			return sysUser.getUserCode();
		}
		List<String> userCodeByIds = new ArrayList<String>();
		if(!StringUtils.isEmpty(userIdStr)){
			userIdStr = userIdStr.substring(0, userIdStr.length() - 1);
			//通过userId获取操作员编码
			userCodeByIds = new GenericSpMtDataReportVoDAO().findUserCodeByIds(userIdStr);
		}
		List<String> userCodeBydepIds = new ArrayList<String>();
		if(!StringUtils.isEmpty(depIdStr)){
			//根据机构查询该机构下所有的操作员编码
			userCodeBydepIds = new GenericSpMtDataReportVoDAO().findUserCodeBydepIds(depIdStr, sysUser.getCorpCode(), containSubDep);
		}
		//合并去重
		userCodeBydepIds.addAll(userCodeByIds);
		HashSet h = new HashSet(userCodeBydepIds);
		userCodeBydepIds.clear();
		userCodeBydepIds.addAll(h);
		if(userCodeBydepIds.size() == 0){
			return "";
		}else {
			for(String res : userCodeBydepIds){
				result.append("'").append(res).append("',");
			}
			result = result.deleteCharAt(result.lastIndexOf(",")).append(")");
			return result.toString();
		}
    }
}
