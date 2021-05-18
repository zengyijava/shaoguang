/**
 * 
 */
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.report.bean.RptConfInfo;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.dao.GenericOperatorReportVoDAO;
import com.montnets.emp.report.dao.ReportDAO;
import com.montnets.emp.report.vo.CountReportVo;
import com.montnets.emp.report.vo.MtDataReportVo;
import com.montnets.emp.report.vo.UserAreaRptVo;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.GlobalConst;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;

/**
 * 操作员统计报表BIZ
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:44:54
 * @description
 */
public class OperatorReportBiz extends BaseBiz
{

	// 定义操作员统计报表的DAO
	protected GenericOperatorReportVoDAO	reportVoDao;

	// protected IEmpTransactionDAO empTransDao ;
	// 定义SpecialDao
	protected ReportDAO						reportDao;

	// 构造函数
	public OperatorReportBiz()
	{
		// empTransDao = new EmpTransactionDAO();
		reportDao = new ReportDAO();
		reportVoDao = new GenericOperatorReportVoDAO();
	}

	/**
	 * 根据发送账号ID获取汇总表里面的相关信息 (个人权限或者机构权限)
	 * 
	 * @param permissionType
	 *        权限控制 1-个人权限 2-机构权限
	 * @param ownDepIds
	 * @param mtDataReportVo
	 * @param curUserId
	 *        当前登录操作员的登录帐号
	 * @param corpCode
	 *        当期企业编码
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<MtDataReportVo> getMtDataReportList(Integer permissionType, String ownDepIds, String ownUserIds, MtDataReportVo mtDataReportVo, Long curUserId, String corpCode, PageInfo pageInfo) throws Exception
	{
		String depIds = null;
		switch (permissionType)
		{
			case 1:// 个人权限
				ownUserIds = curUserId.toString();
				// 14:45根据领导指示，操作员报表不做机构权限，
				// 只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
				break;
			case 2:// 机构权限
				// ownUserIds = null;
				if(null == ownDepIds || 0 == ownDepIds.length())
				{
					depIds = getDepIds(curUserId);
					// 2012-5-14 14:45根据领导指示，操作员报表不做机构权限，
					// 只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
					// 查找当前登录人员的管辖范围机构。
					List<LfDomination> lfdom = new SpecialDAO().findDomDepIdByUserID(curUserId.toString());
					if(lfdom != null && lfdom.size() > 0)
					{
						LfDomination lfd = lfdom.get(0);
						mtDataReportVo.setDepName(lfd.getDepId().toString());
					}
					else
					{
						EmpExecutionContext.error("机构统计报表，获取不到管辖机构。curUserId="+curUserId);
						mtDataReportVo.setDepName("0");
					}
				}
				else
				{
					depIds = ownDepIds;
					mtDataReportVo.setDepName(ownDepIds);
					
				}
				curUserId = null;
				/*
				 * ownUserIds = null;//2012-5-14 14:45根据领导指示，操作员报表不做机构权限，
				 * //只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
				 */
				break;
			default:
				break;
		}
		List<MtDataReportVo> mtReportList = reportVoDao.getMtDataReportInfoDep(curUserId, depIds, ownUserIds, mtDataReportVo, corpCode, pageInfo);// 获取操作员报表的相关信息

		return mtReportList;
	}
	
	
	/**
	 * 日报表详情
	 * @param userid
	 * @param mtDataReportVo
	 * @param corpCode
	 * @param pageInfo
	 * @param reportType
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getMtDataReportInfoDetailList(String userid, MtDataReportVo mtDataReportVo, String corpCode, PageInfo pageInfo,String reportType) throws Exception
	{
		List<DynaBean> mtReportList = reportVoDao.getMtDataReportInfoDetailList(userid, mtDataReportVo, corpCode,reportType, pageInfo);// 获取操作员报表的相关信息
		
		return mtReportList;
	}
	
	
	/**
	 * 
	 */
	public long[] findSumDetailCount( String userid,MtDataReportVo mtDataReportVo, String corpCode,String reportType) throws Exception
	{
		return reportVoDao.findDetailSumCount(userid, mtDataReportVo, corpCode,reportType);
	}
	
	
	
	/**
	 * 日报国家表详情
	 * @param userid
	 * @param userareavo
	 * @param corpCode
	 * @param pageInfo
	 * @param reportType
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAreaMtDataReportInfoDetailList(String userid, UserAreaRptVo userareavo, String corpCode, PageInfo pageInfo,String reportType) throws Exception
	{
		// 获取操作员报表的相关信息
		List<DynaBean> mtReportList = reportVoDao.getAreaMtDataReportInfoDetailList(userid, userareavo, corpCode,reportType, pageInfo);
		
		return mtReportList;
	}
	
	
	/**
	 * 国家详情合计
	 */
	public long[] findAreaSumDetailCount( String userid,UserAreaRptVo userareavo, String corpCode,String reportType) throws Exception
	{
		return reportVoDao.findAreaDetailSumCount(userid, userareavo, corpCode,reportType);
	}
	
	/**
	 * 获取当期操作员所管辖的部门机构
	 * 
	 * @param curUserId
	 *        当期操作员登录账号
	 * @return
	 * @throws Exception
	 */
	
    public String getDepIds(Long curUserId) throws Exception
	{
		String depIds = "";
		// 通用查询方法排序字段集合
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		// 排序字段
		orderbyMap.put("depId", "asc");
		// 部门集合
		List<LfDep> depList = new ArrayList<LfDep>();

		// 通过当前操作员的机构获取所有下级机构
		depList = reportDao.findDomDepBySysuserID(curUserId.toString(), orderbyMap);

		// 判断下级机构集合是否为空
		if(null != depList && 0 != depList.size())
		{
			for (LfDep dep : depList)
			{
				depIds += dep.getDepId().toString() + ",";
			}

			if(depIds.contains(","))
			{
				depIds = depIds.substring(0, depIds.lastIndexOf(","));
			}
		}

		return depIds;
	}

	/**
	 * 合计汇总数量
	 * 
	 * @param permissionType
	 *        权限控制 1-个人权限 2-机构权限
	 * @param curUserId
	 *        当前登录操作员ID
	 * @param depIds
	 *        当前操作员所管辖范围内的机构列表，如果为个人权限，则该值为空
	 * @param userIds
	 *        当前操作员所管辖范围内的操作员列表，下拉框列表多选择用
	 * @param mtDataReportVo
	 * @param corpCode
	 *        企业编码
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public long[] findSumCount(Integer permissionType, Long curUserId, String ownDepIds, String ownUserIds, MtDataReportVo mtDataReportVo, String corpCode, PageInfo pageInfo) throws Exception
	{

		String depIds = null;
		// permissionType = 2;
		switch (permissionType)
		{
			case 1:// 个人权限
				/*
				 * depIds = null;
				 * curUserId = null;
				 */
				ownUserIds = curUserId.toString();// 2012-5-14
													// 14:45根据领导指示，操作员报表不做机构权限，
				// 只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
				break;
			case 2:// 机构权限
				// ownUserIds = null;

				if(null == ownDepIds || 0 == ownDepIds.length())
				{
					depIds = getDepIds(curUserId);
					// 2012-5-14 14:45根据领导指示，操作员报表不做机构权限，
					// 只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
				}
				else
				{
					depIds = ownDepIds;
				}
				curUserId = null;
				/*
				 * ownUserIds = null;//2012-5-14 14:45根据领导指示，操作员报表不做机构权限，
				 * //只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
				 */
				break;
			default:
				break;
		}
		return reportVoDao.findSumCount(curUserId, depIds, ownUserIds, mtDataReportVo, corpCode, pageInfo);
	}
	
	/**
	 * 根据发送账号ID获取汇总表里面的相关信息 (个人权限或者机构权限)
	 * 
	 * @param permissionType2
	 *        权限控制 1-个人权限 2-机构权限
	 * @param ownDepIds
	 * @param mtDataReportVo
	 * @param curUserId
	 *        当前登录操作员的登录帐号
	 * @param corpCode
	 *        当期企业编码
	 * @return
	 * @throws Exception
	 */
	
    public List<MtDataReportVo> getMtDataReportListUnPage(Integer permissionType1, String ownDepIds, String ownUserIds, MtDataReportVo mtDataReportVo, Long curUserId, String corpCode) throws Exception
	{
		List<MtDataReportVo> mtReportList = new ArrayList<MtDataReportVo>();
		String depIds = null;
		Integer permissionType2 = 2;
		switch (permissionType2)
		{
			case 1:// 个人权限
				/*
				 * depIds = null;
				 * curUserId = null;
				 */
				/*
				 * ownUserIds = null;//2012-5-14 14:45根据领导指示，操作员报表不做机构权限，
				 * //只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
				 */
				break;
			case 2:// 机构权限
				// ownUserIds = null;

				if(null == ownDepIds || 0 == ownDepIds.length())
				{
					// depIds = this.getDepIds(curUserId);
					// 2012-5-14 14:45根据领导指示，操作员报表不做机构权限，
					// 只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
				}
				else
				{
					depIds = ownDepIds;
				}
				curUserId = null;
				/*
				 * ownUserIds = null;//2012-5-14 14:45根据领导指示，操作员报表不做机构权限，
				 * //只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
				 */
				break;
			default:
				break;
		}

		mtReportList = reportVoDao.getMtDataReportInfoDepUnPage(curUserId, depIds, ownUserIds, mtDataReportVo, corpCode);// 获取操作员报表的相关信息

		return mtReportList;
	}

	/**
	 * @description：生成操作员统计报表Excel
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */

	
    public Map<String, String> createSysUserReportExcel(List<MtDataReportVo> mtdList, String queryTime, String count, String fail, String showTime, String spnumtypename) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "操作员统计报表";
		String reportName = "SysUserReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysUserReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("操作员统计报表导出工作表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无操作员统计报表数据！");
			throw new Exception("无操作员统计报表数据！");
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
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				//读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, reportName);
				
				//获取模板第一个工作簿
				Sheet sheetTemp = workbook1.getSheetAt(0);
				//获取模板第一行标题
				Row rowTitle = sheetTemp.getRow(0);
				//加载配置列
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.USER_RPT_CONF_MENU_ID);
				//标题单元格索引，从3开始，因为已经有4个单元格
				int cellTitleIndex = 3;
				for(RptConfInfo rptConf : rptConList)
				{
					cellTitleIndex++;
					if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{
						rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
					}
					else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{
						rowTitle.getCell(cellTitleIndex).setCellValue(rptConf.getName());
					}
				}
				
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
				int index = 0;
				Cell[] cell1 = new Cell[6];
				Cell[] cell2 = new Cell[6];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					MtDataReportVo mt = (MtDataReportVo) mtdList.get(k);
					
					// 时间
					// String showTime = "-";
					/*
					 * if( null != mt.getIymd() && 0 != mt.getIymd().length() &&
					 * mt.getIymd().contains("-")) { String[] showTimeArray =
					 * mt.getIymd().split("-"); Integer dTime =
					 * Integer.parseInt(
					 * showTimeArray[2].substring(0,2).toString());//显示天数
					 * Integer monthtime =
					 * Integer.parseInt(showTimeArray[1].toString());//显示月份
					 * showTime = showTimeArray[0]+"年"+monthtime+"月"+dTime+"日";
					 * }
					 */
					String sendTime = showTime;
					// 发送者
					String userName = mt.getUserName() != null ? mt.getUserName() : "";
					if(mt.getUserName() != null && mt.getUserState() != null && mt.getUserState() == 2)
					{
						userName += "(已注销)";
					}
					// 机构
					String depName = mt.getDepName() != null ? mt.getDepName() : "";
					//运营商
					String sptypename=spnumtypename;
					// 发送总数
					String icount = mt.getRsucc() != null ? mt.getRsucc().toString() : "";
					// 接收失败数
					String rfail = mt.getRfail2() != null ? mt.getRfail2().toString() : "";

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
					cell1[1].setCellValue(userName);
					cell1[2].setCellValue(depName);
					cell1[3].setCellValue(sptypename);
					
					//标题单元格索引，从3开始，因为已经有4个单元格
					int cellDataIndex = 3;
					for(RptConfInfo rptConf : rptConList)
					{
						cellDataIndex++;
						if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
						{
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(icount);
						}
						else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
						{
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(rfail);
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

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue("");
				cell2[3].setCellValue("合计:");
				
				//标题单元格索引，从3开始，因为已经有4个单元格
				int cellTotalIndex = 3;
				for(RptConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					if(RptStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell2[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell2[cellTotalIndex].setCellStyle(cellStyle[1]);
						cell2[cellTotalIndex].setCellValue(count);
					}
					else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell2[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell2[cellTotalIndex].setCellStyle(cellStyle[1]);
						cell2[cellTotalIndex].setCellValue("".equals(fail) ? "0" : fail);
					}
				}

			}

			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "操作员统计报表" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"操作员统计报表生成excel导出异常");
		}
		finally
		{
			// 清除对象
			workbook = null;
			if(os!=null){
				os.close();
			}
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	
	
	/**
	 * @description：生成操作员统计报表Excel
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */
	public Map<String, String> createSysUserReportExcelV1(List<MtDataReportVo> mtdList, String queryTime, 
			CountReportVo countrptvo, String showTime,String spnumtypename,HttpServletRequest request) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "操作员统计报表";
		String reportName = "SysUserReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysUserReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("操作员统计报表导出工作表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无操作员统计报表数据！");
			throw new Exception("无操作员统计报表数据！");
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
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
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
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.USER_RPT_CONF_MENU_ID);
				//标题单元格索引，从3开始，因为已经有4个单元格
				int cellTitleIndex = 3;
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_sj",request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_czy",request));
				rowTitle.getCell(2).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_jg",request));
				rowTitle.getCell(3).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_yys",request));
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
					MtDataReportVo mt = (MtDataReportVo) mtdList.get(k);
					
					// 时间
					// String showTime = "-";
					/*
					 * if( null != mt.getIymd() && 0 != mt.getIymd().length() &&
					 * mt.getIymd().contains("-")) { String[] showTimeArray =
					 * mt.getIymd().split("-"); Integer dTime =
					 * Integer.parseInt(
					 * showTimeArray[2].substring(0,2).toString());//显示天数
					 * Integer monthtime =
					 * Integer.parseInt(showTimeArray[1].toString());//显示月份
					 * showTime = showTimeArray[0]+"年"+monthtime+"月"+dTime+"日";
					 * }
					 */
					String sendTime = showTime;
					// 发送者
					String userName = mt.getUserName() != null ? mt.getUserName() : "";
					if(mt.getUserName() != null && mt.getUserState() != null && mt.getUserState() == 2)
					{
						userName += "("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_jgtjbb_yzx",request)+")";
					}
					// 机构
					String depName = mt.getDepName() != null ? mt.getDepName() : "";
					//运营商
					String sptypename=spnumtypename;
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
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.USER_RPT_CONF_MENU_ID);
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
					cell1[1].setCellValue(userName);
					cell1[2].setCellValue(depName);
					cell1[3].setCellValue(sptypename);
					
					//标题单元格索引，从3开始，因为已经有4个单元格
					int cellDataIndex = 3;
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

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue("");
				cell2[3].setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_hj",request));
				
				//标题单元格索引，从3开始，因为已经有4个单元格
				int cellTotalIndex = 3;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(countrptvo.getIcount(), countrptvo.getRsucc(), countrptvo.getRfail1(), countrptvo.getRfail2(), countrptvo.getRnret(), RptStaticValue.USER_RPT_CONF_MENU_ID);
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
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_rpt_czytjbb", request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"操作员统计报表生成excel导出异常");
		}
		finally
		{
			// 清除对象
			workbook = null;
			if(os!=null){
				os.close();
			}
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	
	
	
	/**
	 * 操作员详细报表导出
	 * @param mtdList
	 * @param count
	 * @param fail
	 * @param reportType
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createdetailSysUserReportExcel(List<DynaBean> mtdList, String count, String fail, String reportType,String username,String depname,String spnumtypename) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "操作员统计报表";
		String reportName = "SysUserReport";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysUserReportDetail.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("操作员统计报表详情导出工作表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无操作员统计报表详情数据！");
			throw new Exception("无操作员统计报表详情数据！");
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
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
				int index = 0;
				Cell[] cell1 = new Cell[6];
				Cell[] cell2 = new Cell[6];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					DynaBean mt =  mtdList.get(k);
					//时间
					String sendTime = null;
					SimpleDateFormat sdfd = new SimpleDateFormat("yyyy年M月d日");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
					if("2".equals(reportType)){
						sendTime=(mt.get("y")!=null?mt.get("y").toString():"2015")+"年"+(mt.get("imonth")!=null?mt.get("imonth").toString():"01")+"月";
					}else{
						sendTime=sdfd.format(sdf1.parse(mt.get("iymd")!=null?mt.get("iymd").toString():"20150101"));
					}
					//操作员名称
					// 发送者
					String uname =username;
					// 机构
					String dname = depname;
					//运营商
					String spname=spnumtypename;
					// 发送总数
					String icount =String.valueOf((mt.get("succ") != null ? mt.get("succ") : ""));
					// 接收失败数
					String rfail = String.valueOf((mt.get("rfail2") != null ? mt.get("rfail2") : ""));
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
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(uname);
					cell1[2].setCellValue(dname);
					cell1[3].setCellValue(spname);
					cell1[4].setCellValue(icount);
					cell1[5].setCellValue(rfail);

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
				cell2[3].setCellValue("合计:");
				cell2[4].setCellValue(count);
				cell2[5].setCellValue(fail == "" ? "0" : fail);
			}

			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "操作员统计详情报表" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"操作员统计详情报表生成excel导出异常");
		}
		finally
		{
			// 清除对象
			workbook = null;
			if(os!=null){
				os.close();
			}
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	
	
	/**
	 * 操作员详细报表导出
	 * @param mtdList
	 * @param count
	 * @param fail
	 * @param reportType
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createdetailSysUserReportExcelV1(List<DynaBean> mtdList,CountReportVo countrptvo, String reportType,
			String username,String depname,String spnumtypename,HttpServletRequest request) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "操作员统计报表";
		String reportName = "SysUserReport";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysUserReportDetail.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("操作员统计报表详情导出工作表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无操作员统计报表详情数据！");
			throw new Exception("无操作员统计报表详情数据！");
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
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
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
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.USER_RPT_CONF_MENU_ID);
				//标题单元格索引，从3开始，因为已经有4个单元格
				int cellTitleIndex = 3;
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_sj",request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_czy",request));
				rowTitle.getCell(2).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_jg",request));
				rowTitle.getCell(3).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_yys",request));
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
				int index = 0;
				Cell[] cell1 = new Cell[4+rptConList.size()];
				Cell[] cell2 = new Cell[4+rptConList.size()];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					DynaBean mt =  mtdList.get(k);
					//时间
					String sendTime = null;
					SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-M-d");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
					if("2".equals(reportType)){
						sendTime=(mt.get("y")!=null?mt.get("y").toString():"2015")+"-"+(mt.get("imonth")!=null?mt.get("imonth").toString():"01");
					}else{
						sendTime=sdfd.format(sdf1.parse(mt.get("iymd")!=null?mt.get("iymd").toString():"20150101"));
					}
					//操作员名称
					// 发送者
					String uname =username;
					// 机构
					String dname = depname;
					//运营商
					String spname=spnumtypename;
					//提交总数
					long icount=mt.get("icount") != null ? Long.valueOf(mt.get("icount").toString()) : 0;
					//接收成功数
					long rsucc=mt.get("rsucc") != null ? Long.valueOf(mt.get("rsucc").toString()) : 0;
					//发送失败数
					long rfail1=mt.get("rfail1") != null ? Long.valueOf(mt.get("rfail1").toString()) : 0;
					//接收失败数
					long rfail2=mt.get("rfail2") != null ? Long.valueOf(mt.get("rfail2").toString()) : 0;
					//未返数
					long rnret=mt.get("rnret") != null ? Long.valueOf(mt.get("rnret").toString()) : 0;
					//根据原始值返回计算值
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.USER_RPT_CONF_MENU_ID);
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
					cell1[1].setCellValue(uname);
					cell1[2].setCellValue(dname);
					cell1[3].setCellValue(spname);
					
					//标题单元格索引，从3开始，因为已经有4个单元格
					int cellDataIndex = 3;
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

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue("");
				cell2[3].setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_hj",request));
				//标题单元格索引，从3开始，因为已经有4个单元格
				int cellTotalIndex = 3;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(countrptvo.getIcount(), countrptvo.getRsucc(), countrptvo.getRfail1(), countrptvo.getRfail2(), countrptvo.getRnret(), RptStaticValue.USER_RPT_CONF_MENU_ID);
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
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = MessageUtils.extractMessage("cxtj","cxtj_new_rpt_czyxqbb",request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"操作员统计详情报表生成excel导出异常");
		}
		finally
		{
			// 清除对象
			workbook = null;
			if(os!=null){
				os.close();
			}
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	
	
	
	/**
	 * 操作员国家详情详细报表导出
	 * @param mtdList
	 * @param count
	 * @param fail
	 * @param reportType
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createAreadetailSysUserReportExcel(List<DynaBean> mtdList, String count, String fail, String reportType,String username,String depname,String spnumtypename,String showuserareatime) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "操作员统计报表";
		String reportName = "SysUserReport";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysUserAreaReport.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("操作员国家详情导出工作表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无操作员国家详情数据！");
			throw new Exception("无操作员国家详情数据！");
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
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
				int index = 0;
				Cell[] cell1 = new Cell[9];
				Cell[] cell2 = new Cell[9];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					DynaBean mt =  mtdList.get(k);
					//时间
					String sendTime = showuserareatime;
					//国家区域代码
					String areacode=mt.get("areacode")!=null?mt.get("areacode").toString():"";
					//国家名称
					String areaname=mt.get("areaname")!=null?mt.get("areaname").toString():"";
					//操作员名称
					String uname =username;
					// 机构
					String dname = depname;
					//通道号码
					String spgate=mt.get("spgate")!=null?mt.get("spgate").toString():"";
					//通道名称mt
					String gatename=mt.get("gatename")!=null?mt.get("gatename").toString():"";
					// 发送总数
					String icount =String.valueOf((mt.get("succ") != null ? mt.get("succ") : ""));
					// 接收失败数
					String rfail = String.valueOf((mt.get("rfail2") != null ? mt.get("rfail2") : ""));
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
					cell1[3].setCellValue(uname);
					cell1[4].setCellValue(dname);
					cell1[5].setCellValue(spgate);
					cell1[6].setCellValue(gatename);
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
				cell2[6].setCellValue("合计:");
				cell2[7].setCellValue(count);
				cell2[8].setCellValue( "".equals(fail) ? "0" : fail);
			}

			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "操作员统计国家详情报表" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"操作员统计报表国家详情生成excel导出异常");
		}
		finally
		{
			// 清除对象
			workbook = null;
			if(os!=null){
				os.close();
			}
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	
	/**
	 * 操作员国家详情详细报表导出
	 * @param mtdList
	 * @param count
	 * @param fail
	 * @param reportType
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createAreadetailSysUserReportExcelV1(List<DynaBean> mtdList, CountReportVo countrptvo, String reportType,String username,
			String depname,String spnumtypename,String showuserareatime,HttpServletRequest request) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "操作员统计报表";
		String reportName = "SysUserReport";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysUserAreaReport.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("操作员国家详情导出工作表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无操作员国家详情数据！");
			throw new Exception("无操作员国家详情数据！");
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
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
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
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.USER_RPT_CONF_MENU_ID);
				//标题单元格索引，从6开始，因为已经有7个单元格
				int cellTitleIndex = 6;
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_sj",request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_gjdqdm",request));
				rowTitle.getCell(2).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_new_rpt_gjmc",request));
				rowTitle.getCell(3).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_czy",request));
				rowTitle.getCell(4).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_jg",request));
				rowTitle.getCell(5).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_tdhm",request));
				rowTitle.getCell(6).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_tdmc",request));
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
				int index = 0;
				Cell[] cell1 = new Cell[7+rptConList.size()];
				Cell[] cell2 = new Cell[7+rptConList.size()];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					DynaBean mt =  mtdList.get(k);
					//时间
					String sendTime = showuserareatime;
					//国家区域代码
					String areacode=mt.get("areacode")!=null?mt.get("areacode").toString():"";
					//国家名称
					String areaname=mt.get("areaname")!=null?mt.get("areaname").toString():"";
					//操作员名称
					String uname =username;
					// 机构
					String dname = depname;
					//通道号码
					String spgate=mt.get("spgate")!=null?mt.get("spgate").toString():"";
					//通道名称mt
					String gatename=mt.get("gatename")!=null?mt.get("gatename").toString():"";
					//提交总数
					long icount=mt.get("icount") != null ? Long.valueOf(mt.get("icount").toString()) : 0;
					//接收成功数
					long rsucc=mt.get("rsucc") != null ? Long.valueOf(mt.get("rsucc").toString()) : 0;
					//发送失败数
					long rfail1=mt.get("rfail1") != null ? Long.valueOf(mt.get("rfail1").toString()) : 0;
					//接收失败数
					long rfail2=mt.get("rfail2") != null ? Long.valueOf(mt.get("rfail2").toString()) : 0;
					//未返数
					long rnret=mt.get("rnret") != null ? Long.valueOf(mt.get("rnret").toString()) : 0;
					//根据原始值返回计算值
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.USER_RPT_CONF_MENU_ID);
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
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(areacode);
					cell1[2].setCellValue(areaname);
					cell1[3].setCellValue(uname);
					cell1[4].setCellValue(dname);
					cell1[5].setCellValue(spgate);
					cell1[6].setCellValue(gatename);
					
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
				cell2[6].setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_hj",request));
				//标题单元格索引，从6开始，因为已经有7个单元格
				int cellTotalIndex = 6;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(countrptvo.getIcount(), countrptvo.getRsucc(), countrptvo.getRfail1(), countrptvo.getRfail2(), countrptvo.getRnret(), RptStaticValue.USER_RPT_CONF_MENU_ID);
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
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = MessageUtils.extractMessage("cxtj","cxtj_new_rpt_czygjbb",request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"操作员统计报表国家详情生成excel导出异常");
		}
		finally
		{
			// 清除对象
			workbook = null;
			if(os!=null){
				os.close();
			}
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	

}
