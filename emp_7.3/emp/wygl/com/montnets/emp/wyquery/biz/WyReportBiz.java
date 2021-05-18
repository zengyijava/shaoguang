package com.montnets.emp.wyquery.biz;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.GlobalConst;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;
import com.montnets.emp.wyquery.bean.RptWyConfInfo;
import com.montnets.emp.wyquery.bean.RptWyStaticValue;
import com.montnets.emp.wyquery.dao.GenericSpMtDataReportVoDAO;
import com.montnets.emp.wyquery.vo.WyReportVo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送账号下行统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:45:50
 * @description
 */
public class WyReportBiz {
	
	BaseBiz baseBiz = new BaseBiz();
	
	
    public List<WyReportVo> getReportInfosByDate(
			WyReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {
		//发送账号下行统计报表集合
		List<WyReportVo> mtDRVosList;

		try {
			//判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericSpMtDataReportVoDAO().findWyRptListByVo(mtDataReportVo);
			} else {
				mtDRVosList = new GenericSpMtDataReportVoDAO().findWyRptListByVoPage(mtDataReportVo, pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"网优统计报表日报表查询失败！");
			throw e;
		}
		
		return mtDRVosList;
	}
	

	
	public List<DynaBean> getReportInfosDetailByDate(
			WyReportVo wyrptvo,String reporttype, PageInfo pageInfo) throws Exception {
		//发送账号下行统计报表集合
		List<DynaBean> wyrptvolist;
		try {
			//判断是否需要分页
			if (pageInfo == null) {
				wyrptvolist = new GenericSpMtDataReportVoDAO().findWyDetailByDate(wyrptvo,reporttype,null);
			} else {
				wyrptvolist = new GenericSpMtDataReportVoDAO().findWyDetailByDate(wyrptvo,reporttype, pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"网优统计报表详情查询失败！");
			throw e;
		}
		
		return wyrptvolist;
	}
	

	/**
	 * 发送账号下行统计报   合计
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	
    public long[] findSumCount(WyReportVo operatorsMtDataReportVo) throws Exception {
		//获取合计数存数数组中
		long[] count = new GenericSpMtDataReportVoDAO().findSumCount(
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

	
    public Map<String, String> createSpMtReportExcel(
			WyReportVo wyshowdate,
			List<WyReportVo> mtdList, String faill, String succc,String wyshowdatestr)
			throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"wygl/wyquery/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "SpReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "wyreport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("网优统计报表生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("网优统计报表无统计报表数据！");
			throw new Exception("网优统计报表无统计报表数据！");
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
						+ "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
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

					WyReportVo mt = (WyReportVo) mtdList.get(k);

					// 时间
					String showdate =wyshowdatestr;
					//账户名称
					String gateName=mt.getGateName()!=null?mt.getGateName():"--";
					//成功数
					String chenggong = mt.getIcount() != null ? mt.getIcount().toString() : "";
					// 接收失败数
					
					long rfail=mt.getIcount()-mt.getRfail1();
					
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
					cell1[0].setCellValue(showdate);
					cell1[1].setCellValue(mt.getSpgate());
					cell1[2].setCellValue(gateName);
					cell1[3].setCellValue(chenggong);
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
				cell2[2].setCellValue(name);
				cell2[3].setCellValue(succc);
				cell2[4].setCellValue(faill.equals("") ? "0" : faill);

			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "网优统计报表" + sdf.format(curDate)+"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			boolean status = FileUtils.deleteDir(fileTemp);
			if (!status) {
			    EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网优统计统计报表生成excel导出异常");
		} finally {
			// 清除对象
			workbook = null;
            if(os != null){
                try {
                    os.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "流关闭异常！");
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "流关闭异常！");
                }
            }
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}
	
    public Map<String, String> createSpMtReportExcel(String langName, List<WyReportVo> mtdList, WyReportVo wyshowdate, String wyshowdatestr) throws Exception
	{
		String BASEDIR=new TxtFileUtil().getWebRoot()+"wygl/wyquery/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "SpReport";

		//String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
		//		+ File.separator + "wyreport.xlsx";

		String voucherTemplatePath=null;
		if(StaticValue.ZH_HK.equals(langName)){
			voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
			+ File.separator + "wyreport-zh_HK.xlsx";
		}else if(StaticValue.ZH_TW.equals(langName)){
			voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
			+ File.separator + "wyreport-zh_TW.xlsx";
		}else{
			voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
			+ File.separator + "wyreport.xlsx";
		}


		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("网优统计报表生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("网优统计报表无统计报表数据！");
			throw new Exception("网优统计报表无统计报表数据！");
		}
		

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if(!fileTemp.exists())
		{
			fileTemp.mkdirs();
		}
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;

		OutputStream os = null;
		InputStream in = null;
		try
		{
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			for (int j = 1; j <= size; j++)
			{
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
				List<RptWyConfInfo> rptConList = RptWyConfBiz.rptConfMap.get(RptWyStaticValue.WY_RPT_CONF_MENU_ID);
		
				String tjzs="提交总数";
				String fscgs="发送成功数";
				String fssbs="发送失败数";
				String jssbs="接收失败数";
				String jscgs="接收成功数";
				String wfs="未返数";
				String fscgl="发送成功率";
				String fssbl="发送失败率";
				String jscgl="接收成功率";
				String jssbl="接收失败率";
				String wfl="未返率";
				
				if(StaticValue.ZH_HK.equals(langName)){
					tjzs = "total number of submissions";
					fscgs = "send successfully";
					fssbs = "failed to send";
					jssbs = "failed to receive";
					jscgs = "Number of successes received";
					wfs = "no return";
					fscgl = "send success rate";
					fssbl = "send failure rate";
					jscgl = "Receiving success rate";
					jssbl = "reception failure rate";
					wfl = "non-return rate";
				}else if(StaticValue.ZH_TW.equals(langName)){
					tjzs="提交總數";
					fscgs="發送成功數";
					fssbs="發送失敗數";
					jssbs="接收失敗數";
					jscgs="接收成功數";
					wfs="未返數";
					fscgl="發送成功率";
					fssbl="發送失敗率";
					jscgl="接收成功率";
					jssbl="接收失敗率";
					wfl="未返率";
				}
				
				
				//标题单元格索引，从2开始，因为已经有3个单元格
				int cellTitleIndex = 2;
				for(RptWyConfInfo rptConf : rptConList)
				{
					cellTitleIndex++;
					if(rowTitle.getCell(cellTitleIndex)==null){
						Cell ctitle=rowTitle.createCell(cellTitleIndex);
						sheetTemp.setColumnWidth(cellTitleIndex,3000);
						// 设置单元格样式
						ctitle.setCellStyle(cellStyle[1]);
					}
					//提交总数
					if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(tjzs);
					}
					else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(fscgs);
					}
					else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(fssbs);
					}
					else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(jssbs);
					}
					else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(jscgs);
					}
					else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
					{//未返数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(wfs);
					}
					else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(fscgl);
					}
					else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(fssbl);
					}
					else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(jscgl);
					}
					else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(jssbl);
					}
					else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//未返率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(wfl);
					}
				}
				
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
				int index = 0;

				Cell[] cell1 = new Cell[4+rptConList.size()];
				Cell[] cell2 = new Cell[4+rptConList.size()];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					WyReportVo mt = (WyReportVo) mtdList
							.get(k);
				
					// 时间
					String showdate =wyshowdatestr;
					//账户名称
					String gateName=mt.getGateName()!=null?mt.getGateName():"--";
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
					Map<String, String> map=ReportWyBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptWyStaticValue.WY_RPT_CONF_MENU_ID);
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					
					
					// 设置单元格内容
					cell1[0].setCellValue(showdate);
					cell1[1].setCellValue(mt.getSpgate());
					cell1[2].setCellValue(gateName);
					//标题单元格索引，从1开始，因为已经有2个单元格
					int cellDataIndex = 2;
					for(RptWyConfInfo rptConf : rptConList)
					{
						cellDataIndex++;
						//提交总数
						if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
						{
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(icount);
						}
						else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//发送成功数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
						{//发送失败数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(rfail1);
						}
						else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
						{//接收失败数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(rfail2);
						}
						else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//接收成功数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
						{//未返数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(rnret);
						}
						else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
						{//发送成功率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
						{//发送失败率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//接收成功率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//接收失败率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//未返率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):"0");
						}
					}

					// 一页里的行数
					index++;
				}

				row = sheet.createRow(k + 1);
				// 生成四个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(1);
			

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				//cell2[2].setCellValue("合计:");
				if(StaticValue.ZH_HK.equals(langName)){
					cell2[2].setCellValue("total:");
				}else if(StaticValue.ZH_TW.equals(langName)){
					cell2[2].setCellValue("合計:");
				}else{
					cell2[2].setCellValue("合计:");
				}

				
				
				//标题单元格索引，从2开始，因为已经有3个单元格
				int cellTotalIndex = 2;
				//根据原始值返回计算值
				Map<String, String> map=ReportWyBiz.getRptNums(wyshowdate.getIcount(), wyshowdate.getRsucc(), wyshowdate.getRfail1(), wyshowdate.getRfail2(), wyshowdate.getRnret(), RptWyStaticValue.WY_RPT_CONF_MENU_ID);
				for(RptWyConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(wyshowdate.getIcount());
					}
					else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(wyshowdate.getRfail1());
					}
					else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(wyshowdate.getRfail2());
					}
					else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
					{//未返数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(wyshowdate.getRnret());
					}
					else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//未返率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):"0");
					}
				}

			}

			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);

			//fileName = "网优统计报表" + sdf.format(curDate) + ".zip";

			String name="网优统计报表";
			if(StaticValue.ZH_HK.equals(langName)){
				name="NetworkOptimizationStatisticsReport";
			}else if(StaticValue.ZH_TW.equals(langName)){
				name="網優統計報表";
			}
			fileName = name + sdf.format(curDate) + ".zip";

			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"网优统计报表生成excel导出异常");
		}
		finally
		{
			// 清除对象
			workbook = null;
			if(os != null){
			os.close();
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

	public Map<String, String> createDetailSpMtReportExcel(
			List<DynaBean> mtdList, String faill, String succc,String reporttype)
			throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"wygl/wyquery/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "SpReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "wyreport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("网优报表详情生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("网优报表详情无统计报表数据！");
			throw new Exception("网优报表详情无统计报表数据！");
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
						+ "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
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

					DynaBean mt = (DynaBean) mtdList.get(k);
					// 时间
					String showdate ="";
					SimpleDateFormat sdfd = new SimpleDateFormat("yyyy年M月d日");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
					if("2".equals(reporttype)){
						showdate=(mt.get("y")!=null?mt.get("y").toString():"2015")+"年"+(mt.get("imonth")!=null?mt.get("imonth").toString():"01")+"月";
					}else{
						showdate=sdfd.format(sdf1.parse(mt.get("iymd")!=null?mt.get("iymd").toString():"20150101"));
					}
					//通道号
					String spgate=mt.get("spgate")==null?"未知":mt.get("spgate").toString().toUpperCase();
					//通道名称
					String gateName=mt.get("gatename")!=null?mt.get("gatename").toString():"--";
					//成功数
					String succ = mt.get("succ")!=null?mt.get("succ").toString():"";
					// 接收失败数
					String rfail = mt.get("rfail2")!=null?mt.get("rfail2").toString():"";


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
					cell1[0].setCellValue(showdate);
					cell1[1].setCellValue(spgate);
					cell1[2].setCellValue(gateName);
					cell1[3].setCellValue(succ);
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
				cell2[2].setCellValue(name);
				cell2[3].setCellValue(succc);
				cell2[4].setCellValue(faill.equals("") ? "0" : faill);

			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "网优统计报表" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网优统计统计报表生成excel导出异常");
		} finally {
			// 清除对象
			workbook = null;
			try {
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e) {
			    EmpExecutionContext.error("流关闭异常！");
            }
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}
	
	public Map<String, String> createDetailSpMtReportExcel(
			String langName,List<DynaBean> mtdList,WyReportVo wyshowdate,String reporttype)
			throws Exception
	{
		String BASEDIR=new TxtFileUtil().getWebRoot()+"wygl/wyquery/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		//String reportName = "SP账号统计报表";
		String reportName = "SpReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
				+ File.separator + "wyreport.xlsx";

		if(StaticValue.ZH_HK.equals(langName)){
			voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
			+ File.separator + "wyreport-zh_HK.xlsx";
			
		}else if(StaticValue.ZH_TW.equals(langName)){
			voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES
			+ File.separator + "wyreport-zh_TW.xlsx";
		}

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("网优统计报表生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("网优统计报表无统计报表数据！");
			throw new Exception("网优统计报表无统计报表数据！");
		}
		

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if(!fileTemp.exists())
		{
			fileTemp.mkdirs();
		}
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;

		OutputStream os = null;
		InputStream in = null;
		try
		{
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			for (int j = 1; j <= size; j++)
			{
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
				List<RptWyConfInfo> rptConList = RptWyConfBiz.rptConfMap.get(RptWyStaticValue.WY_RPT_CONF_MENU_ID);

				String tjzs="提交总数";
				String fscgs="发送成功数";
				String fssbs="发送失败数";
				String jssbs="接收失败数";
				String jscgs="接收成功数";
				String wfs="未返数";
				String fscgl="发送成功率";
				String fssbl="发送失败率";
				String jscgl="接收成功率";
				String jssbl="接收失败率";
				String wfl="未返率";

				if(StaticValue.ZH_HK.equals(langName)){
					tjzs = "total number of submissions";
					fscgs = "send successfully";
					fssbs = "failed to send";
					jssbs = "failed to receive";
					jscgs = "Number of successes received";
					wfs = "no return";
					fscgl = "send success rate";
					fssbl = "send failure rate";
					jscgl = "Receiving success rate";
					jssbl = "reception failure rate";
					wfl = "non-return rate";
				}else if(StaticValue.ZH_TW.equals(langName)){
					tjzs="提交總數";
					fscgs="發送成功數";
					fssbs="發送失敗數";
					jssbs="接收失敗數";
					jscgs="接收成功數";
					wfs="未返數";
					fscgl="發送成功率";
					fssbl="發送失敗率";
					jscgl="接收成功率";
					jssbl="接收失敗率";
					wfl="未返率";
				}
				
				
				//标题单元格索引，从2开始，因为已经有3个单元格
				int cellTitleIndex = 2;
				for(RptWyConfInfo rptConf : rptConList)
				{
					cellTitleIndex++;
					if(rowTitle.getCell(cellTitleIndex)==null){
						Cell ctitle=rowTitle.createCell(cellTitleIndex);
						sheetTemp.setColumnWidth(cellTitleIndex,3000);
						// 设置单元格样式
						ctitle.setCellStyle(cellStyle[1]);
					}
					//提交总数
					if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(tjzs);
					}
					else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(fscgs);
					}
					else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(fssbs);
					}
					else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(jssbs);
					}
					else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(jscgs);
					}
					else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
					{//未返数
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(wfs);
					}
					else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(fscgl);
					}
					else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(fssbl);
					}
					else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(jscgl);
					}
					else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(jssbl);
					}
					else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//未返率
						//rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
						rowTitle.getCell(cellTitleIndex).setCellValue(wfl);
					}
				}
				
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
				int index = 0;

				Cell[] cell1 = new Cell[4+rptConList.size()];
				Cell[] cell2 = new Cell[4+rptConList.size()];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					DynaBean mt = (DynaBean) mtdList.get(k);
					// 时间
					String showdate ="";
					//SimpleDateFormat sdfd = new SimpleDateFormat("yyyy年M月d日");
					SimpleDateFormat sdfd = null;

					if(StaticValue.ZH_HK.equals(langName)){
						sdfd = new SimpleDateFormat("yyyy-M-d");
					}else{
						sdfd = new SimpleDateFormat("yyyy年M月d日");
					}
					
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
					if("2".equals(reporttype)){
						//showdate=(mt.get("y")!=null?mt.get("y").toString():"2015")+"年"+(mt.get("imonth")!=null?mt.get("imonth").toString():"01")+"月";
						if(StaticValue.ZH_HK.equals(langName)){
							showdate=(mt.get("y")!=null?mt.get("y").toString():"2015")+"year"+(mt.get("imonth")!=null?mt.get("imonth").toString():"01")+"month";
						}else{//繁体和简体一样  年月
							showdate=(mt.get("y")!=null?mt.get("y").toString():"2015")+"年"+(mt.get("imonth")!=null?mt.get("imonth").toString():"01")+"月";
						}
					}else{
						showdate=sdfd.format(sdf1.parse(mt.get("iymd")!=null?mt.get("iymd").toString():"20150101"));
					}
					//通道号
					//String spgate=mt.get("spgate")==null?"未知":mt.get("spgate").toString().toUpperCase();
					String spgate=null;
					if(StaticValue.ZH_HK.equals(langName)){
						spgate=mt.get("spgate")==null?"unknown":mt.get("spgate").toString().toUpperCase();
					}else{//繁体和简体一样  未知
						spgate=mt.get("spgate")==null?"未知":mt.get("spgate").toString().toUpperCase();
					}
					//通道名称
					String gateName=mt.get("gatename")!=null?mt.get("gatename").toString():"--";
					String icountStr = mt.get("icount")!=null?mt.get("icount").toString():"";
					String rsuccStr = mt.get("rsucc")!=null?mt.get("rsucc").toString():"";
					String rfail1Str = mt.get("rfail1")!=null?mt.get("rfail1").toString():"";
					String rfail2Str = mt.get("rfail2")!=null?mt.get("rfail2").toString():"";
					String rnretStr = mt.get("rnret")!=null?mt.get("rnret").toString():"";
					Long icount=icountStr.equals("")?0L:Long.parseLong(icountStr);
					Long rsucc=icountStr.equals("")?0L:Long.parseLong(rsuccStr);
					Long rfail1=icountStr.equals("")?0L:Long.parseLong(rfail1Str);
					Long rfail2=icountStr.equals("")?0L:Long.parseLong(rfail2Str);
					Long rnret=icountStr.equals("")?0L:Long.parseLong(rnretStr);
					//根据原始值返回计算值
					Map<String, String> map=ReportWyBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptWyStaticValue.WY_RPT_CONF_MENU_ID);
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					
					
					// 设置单元格内容
					cell1[0].setCellValue(showdate);
					cell1[1].setCellValue(spgate);
					cell1[2].setCellValue(gateName);
					//标题单元格索引，从1开始，因为已经有2个单元格
					int cellDataIndex = 2;
					for(RptWyConfInfo rptConf : rptConList)
					{
						cellDataIndex++;
						//提交总数
						if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
						{
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(icount);
						}
						else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//发送成功数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
						{//发送失败数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(rfail1);
						}
						else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
						{//接收失败数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(rfail2);
						}
						else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//接收成功数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
						{//未返数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(rnret);
						}
						else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
						{//发送成功率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
						{//发送失败率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//接收成功率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//接收失败率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):"0");
						}
						else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{//未返率
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):"0");
						}
					}

					// 一页里的行数
					index++;
				}

				row = sheet.createRow(k + 1);
				// 生成四个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(1);
			

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				//cell2[2].setCellValue("合计:");
				if(StaticValue.ZH_HK.equals(langName)){
					cell2[2].setCellValue("total:");
				}else if(StaticValue.ZH_TW.equals(langName)){
					cell2[2].setCellValue("合計:");
				}else{
					cell2[2].setCellValue("合计:");
				}
					
				
				//标题单元格索引，从2开始，因为已经有3个单元格
				int cellTotalIndex = 2;
				//根据原始值返回计算值
				Map<String, String> map=ReportWyBiz.getRptNums(wyshowdate.getIcount(), wyshowdate.getRsucc(), wyshowdate.getRfail1(), wyshowdate.getRfail2(), wyshowdate.getRnret(), RptWyStaticValue.WY_RPT_CONF_MENU_ID);
				for(RptWyConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(wyshowdate.getIcount());
					}
					else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(wyshowdate.getRfail1());
					}
					else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(wyshowdate.getRfail2());
					}
					else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
					{//未返数
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(wyshowdate.getRnret());
					}
					else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送成功率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{//发送失败率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收成功率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//接收失败率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID):"0");
					}
					else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{//未返率
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID)!=null?map.get(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID):"0");
					}
				}

			}

			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);

			//fileName = "网优统计报表" + sdf.format(curDate) + ".zip";

			String name="网优统计报表";
			if(StaticValue.ZH_HK.equals(langName)){
				name="NetworkOptimizationStatisticsReport";
			}else if(StaticValue.ZH_TW.equals(langName)){
				name="網優統計報表";
			}
			fileName = name + sdf.format(curDate) + ".zip";

			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"网优统计报表生成excel导出异常");
		}
		finally
		{
			// 清除对象
			workbook = null;
			if(os != null){
			os.close();
			}
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}


	
	
}
