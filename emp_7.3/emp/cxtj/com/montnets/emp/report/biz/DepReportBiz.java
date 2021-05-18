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
import com.montnets.emp.report.dao.GenericDepReportVoDAO;
import com.montnets.emp.report.dao.ReportDAO;
import com.montnets.emp.report.vo.CountReportVo;
import com.montnets.emp.report.vo.DepAreaRptVo;
import com.montnets.emp.report.vo.DepRptVo;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.GlobalConst;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;

/**
 * 机构统计报表BIZ
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:20:34
 * @description
 */
public class DepReportBiz extends BaseBiz
{

	// 机构报表DAO
	protected GenericDepReportVoDAO	reportVoDao;

	// 报表DAO
	protected ReportDAO				reportDao;

	private final SpecialDAO				specialDao;

	// 构造函数
	public DepReportBiz()
	{
		// 实例化三个报表DAO类
		specialDao = new SpecialDAO();

		reportDao = new ReportDAO();

		reportVoDao = new GenericDepReportVoDAO();

	}

	/**
	 * 根据机构ID以及起始时间查询机构报表数据
	 * 
	 * @param depid
	 *        机构ID
	 * @param deprptvo
	 *        查询条件对象
	 * @param curUserId
	 *        当前登录操作员
	 * @param corpCode
	 *        企业编码
	 * @param pageInfo
	 *        分页对象
	 * @return
	 * @throws Exception
	 */
	
    public List<DepRptVo> getDepReportList(String depid, DepRptVo deprptvo, Long curUserId, String corpCode, PageInfo pageInfo) throws Exception
	{
		// 如果没有选择任何机构赋值当前登录人的部门id
		if(depid == null || depid.trim().length() == 0)
		{
			// 查找当前登录人员的管辖范围机构。
			List<LfDomination> lfdom = specialDao.findDomDepIdByUserID(curUserId.toString());
			if(lfdom != null && lfdom.size() > 0)
			{
				LfDomination lfd = lfdom.get(0);
				depid = lfd.getDepId().toString();
			}
			else
			{
				EmpExecutionContext.error("机构统计报表，获取不到管辖机构。curUserId="+curUserId);
				return new ArrayList<DepRptVo>();
			}
		}
		List<DepRptVo> mtReportList;
		// 调用 查询方法
		if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE && deprptvo.getMstype() != null && deprptvo.getMstype() == 0)
		{
			mtReportList = reportVoDao.getDepRptVoListMsSql(curUserId, depid, deprptvo, corpCode, pageInfo);// 获取操作员报表的相关信息
		}
		else
		{
			mtReportList = reportVoDao.getDepReportList(curUserId, depid, deprptvo, corpCode, pageInfo);// 获取操作员报表的相关信息
		}

		return mtReportList;
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
	 * @param deprptvo
	 * @param corpCode
	 *        企业编码
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public long[] findSumCount(Long curUserId, String ownDepIds, DepRptVo deprptvo, String corpCode) throws Exception
	{
		String depid = "";
		// 如果没有选择任何机构赋值当前登录人的部门id
		if(ownDepIds != null && !"".equals(ownDepIds.trim()))
		{
			depid = ownDepIds.trim();
		}
		else
		{
			long[] returnLong = new long[5];
			returnLong[0] = 0;
			returnLong[1] = 0;
			returnLong[2] = 0;
			returnLong[3] = 0;
			returnLong[4] = 0;
			
			List<LfDomination> lfdom = specialDao.findDomDepIdByUserID(curUserId.toString());
			if(lfdom != null && lfdom.size() > 0)
			{
				LfDomination lfd = lfdom.get(0);
				depid = lfd.getDepId().toString();
			}
			else
			{
				EmpExecutionContext.error("机构统计报表，合计，获取不到管辖机构。curUserId="+curUserId);
				return returnLong;
			}
		}
		return reportVoDao.findSumCount(curUserId, depid, deprptvo, corpCode);
	}

	/**
	 * 详情查看
	 * @param depid
	 * @param deprptvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getDetailListByIdtype(String depid, DepRptVo deprptvo, String corpCode, PageInfo pageInfo) throws Exception
	{
		// 如果没有选择任何机构赋值当前登录人的部门id
		if(depid != null && !"".equals(depid.trim()))
		{
			depid = depid.trim();
		}
		else
		{
			return null;
		}
		
		List<DynaBean> resultlist =reportVoDao.getDepDetailReportList(depid, deprptvo, corpCode, pageInfo);
		
		return resultlist;
	}
	
	
	/**
	 * 各国详情查看
	 * @param depid
	 * @param deprptvo
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAreaDetailListByIdtype(String depid, DepAreaRptVo deparearptvo, String corpCode, PageInfo pageInfo) throws Exception
	{
		// 如果没有选择任何机构赋值当前登录人的部门id
		if(depid != null && !"".equals(depid.trim()))
		{
			depid = depid.trim();
		}
		else
		{
			return null;
		}
		List<DynaBean> resultlist = reportVoDao.getAreaDepDetailReportList(depid, deparearptvo, corpCode, pageInfo);
		
		return resultlist;
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
	 * @param deparearptvo
	 * @param corpCode
	 *        企业编码
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public long[] findAreaDetailSumCount(String ownDepIds, DepAreaRptVo deparearptvo, String corpCode) throws Exception
	{
		String depid = "";
		// 如果没有选择任何机构赋值当前登录人的部门id
		if(ownDepIds != null && !"".equals(ownDepIds.trim()))
		{
			depid = ownDepIds.trim();
		}
		else
		{
			long[] returnLong = new long[5];
			returnLong[0] = 0;
			returnLong[1] = 0;
			returnLong[2] = 0;
			returnLong[3] = 0;
			returnLong[4] = 0;
			return returnLong;
		}
		return reportVoDao.findAreaDetailSumCount( depid, deparearptvo, corpCode);
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
	 * @param deprptvo
	 * @param corpCode
	 *        企业编码
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public long[] findDetailSumCount(String ownDepIds, DepRptVo deprptvo, String corpCode) throws Exception
	{
		String depid = "";
		// 如果没有选择任何机构赋值当前登录人的部门id
		if(ownDepIds != null && !"".equals(ownDepIds.trim()))
		{
			depid = ownDepIds.trim();
		}
		else
		{
			long[] returnLong = new long[5];
			returnLong[0] = 0;
			returnLong[1] = 0;
			returnLong[2] = 0;
			returnLong[3] = 0;
			returnLong[4] = 0;
			return returnLong;
		}
		return reportVoDao.findDetailSumCount( depid, deprptvo, corpCode);
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
		// 组装过滤条件
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("depId", "asc");

		List<LfDep> depList = new ArrayList<LfDep>();
		// 调用查询方法
		depList = reportDao.findDomDepBySysuserID(curUserId.toString(), orderbyMap);
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
		// 返回结果
		return depIds;
	}

	
	/**
	 * 导出使用 根据机构ID以及查询条件对象汇总表里面的相关信息 (个人权限或者机构权限)
	 * 
	 * @param permissionType
	 *        权限控制 1-个人权限 2-机构权限
	 * @param ownDepIds
	 *        查询条件机构id
	 * @param deprptvo
	 *        查询条件对象
	 * @param curUserId
	 *        当前登录操作员ID
	 * @param corpCode
	 *        企业编码
	 * @return 结果集
	 * @throws Exception
	 */
	
    public List<DepRptVo> getDepRptVoListUnPage(Integer permissionType, String ownDepIds, DepRptVo deprptvo, Long curUserId, String corpCode) throws Exception
	{
		List<DepRptVo> mtReportList = new ArrayList<DepRptVo>();
		String depid = "";
		// 如果没有选择任何机构赋值当前登录人的部门id
		if(ownDepIds != null && !"".equals(ownDepIds.trim()))
		{
			depid = ownDepIds.trim();
		}
		else
		{
			// 查找当前登录人员的管辖范围机构。
			if(curUserId != null && !"".equals(curUserId.toString()))
			{
				List<LfDomination> lfdom = specialDao.findDomDepIdByUserID(curUserId.toString());
				if(lfdom != null && lfdom.size() > 0)
				{
					LfDomination lfd = lfdom.get(0);
					depid = lfd.getDepId().toString();
				}
				else
				{
					return mtReportList;
				}
			}
			else
			{
				return mtReportList;
			}

		}
		// 调用查询方法
		if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE && deprptvo.getMstype() != null && deprptvo.getMstype() == 0)
		{
			mtReportList = reportVoDao.getDepRptVoListMsSql(curUserId, depid, deprptvo, corpCode, null);// 获取操作员报表的相关信息
			mtReportList.remove(mtReportList.size() - 1);
		}
		else
		{
			mtReportList = reportVoDao.getDeprptVoListUnPage(curUserId, depid, deprptvo, corpCode);// 获取操作员报表的相关信息
		}
		// 返回结果集
		return mtReportList;
	}

	/**
	 * @description：生成机构统计报表Excel
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */

	
    public Map<String, String> createSysDepReportExcel(List<DepRptVo> mtdList, String queryTime, String count, String fail, String showTime, String datasourcename, String spnumtype) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "机构统计报表";
		String reportName = "SysDepReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysDepReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("机构统计报表导出生成报表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无机构统计报表数据！");
			throw new Exception("无机构统计报表数据！");
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
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.DEP_RPT_CONF_MENU_ID);
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

				String name = "合计:";

				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{

					DepRptVo mt = (DepRptVo) mtdList.get(k);

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
					// String userName = mt.getUserName() != null ?
					// mt.getUserName() : "";
					// 机构
					String depName = mt.getDepName() != null ? mt.getDepName() : "";
					if(mt.getDepName() != null && mt.getUserState() != null && mt.getUserState() == 2)
					{
						depName += "(已注销)";
					}
					//数据源
					String dsname=datasourcename;
					//运营商  国内 国外 全部
					String sptypename=spnumtype;
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
					cell1[1].setCellValue(depName);
					cell1[2].setCellValue(dsname);
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
				cell2[3].setCellValue(name);
				
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
			fileName = "机构统计报表" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗");
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"机构统计报表生成excel导出异常");
		}
		finally
		{
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
	 * @description：生成机构统计报表Excel  当前使用的
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */

	public Map<String, String> createSysDepReportExcel(List<DepRptVo> mtdList, String queryTime, CountReportVo countrptvo, 
			String showTime,String datasourcename,String spnumtype,HttpServletRequest request) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "机构统计报表";
		String reportName = "SysDepReport";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysDepReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("机构统计报表导出生成报表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无机构统计报表数据！");
			throw new Exception("无机构统计报表数据！");
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
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.DEP_RPT_CONF_MENU_ID);
				//标题单元格索引，从3开始，因为已经有4个单元格
				int cellTitleIndex = 3;
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_sj",request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_jgczy",request));
				rowTitle.getCell(2).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_fslx",request));
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

				String name = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_hj",request);

				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{

					DepRptVo mt = (DepRptVo) mtdList.get(k);

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
					// String userName = mt.getUserName() != null ?
					// mt.getUserName() : "";
					// 机构
					String depName = mt.getDepName() != null ? mt.getDepName() : "";
					if(mt.getDepName() != null && mt.getUserState() != null && mt.getUserState() == 2)
					{
						depName = depName.replaceAll("未知机构", MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzjg", request));
						depName = depName.replaceAll("机构", MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_jg", request));
						depName = depName.replaceAll("未知操作员", MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzczy", request));
						depName = depName.replaceAll("操作员", MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_czy", request));
						depName += "("+MessageUtils.extractMessage("cxtj","cxtj_sjcx_jgtjbb_yzx",request)+")";
					}else{
						depName = depName.replaceAll("未知机构", MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzjg", request));
						depName = depName.replaceAll("机构", MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_jg", request));
						depName = depName.replaceAll("未知操作员", MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzczy", request));
						depName = depName.replaceAll("操作员", MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_czy", request));
					}
					//数据源
					String dsname=datasourcename;
					//运营商  国内 国外 全部
					String sptypename=spnumtype;
					
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
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.DEP_RPT_CONF_MENU_ID);
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
					cell1[1].setCellValue(depName);
					cell1[2].setCellValue(dsname);
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
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):"0");
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
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):"0");
						}
						else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
						{//接收失败数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID):"0");
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
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_RNRET_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_COLUMN_ID):"0");
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
				cell2[3].setCellValue(name);
				
				//标题单元格索引，从3开始，因为已经有4个单元格
				int cellTotalIndex = 3;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(countrptvo.getIcount(), countrptvo.getRsucc(), countrptvo.getRfail1(), countrptvo.getRfail2(), countrptvo.getRnret(), RptStaticValue.DEP_RPT_CONF_MENU_ID);
				for(RptConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):"0");
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
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):"0");
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
			fileName = MessageUtils.extractMessage("cxtj","cxtj_new_rpt_jgtjbb",request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗");
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"机构统计报表生成excel导出异常");
		}
		finally
		{
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
	 * @description：生成机构统计报表详情Excel
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */

	public Map<String, String> createSysDepDetailReportExcel(List<DynaBean> mtdList,int reportType,String count, String fail,String datasourcename,String userdepname,String spnumtypename) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "机构统计报表";
		String reportName = "SysDepDetailReport";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysDepReportDetail.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("机构统计报表详情生成报表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无机构统计报表详情数据！");
			throw new Exception("无机构统计报表详情数据！");
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

				String name = "合计:";

				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{

					DynaBean mt = (DynaBean) mtdList.get(k);

					// 时间
					String sendTime = "";
					SimpleDateFormat sdfz = new SimpleDateFormat("yyyy年M月d日");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
//					DateFormat df = DateFormat.getDateInstance();
					if(reportType == 2){
						sendTime=(mt.get("y")!=null?mt.get("y").toString():"2015")+"年"+(mt.get("imonth")!=null?mt.get("imonth").toString():"01")+"月";
					}else{
						sendTime=sdfz.format(sdf1.parse(mt.get("iymd")!=null?mt.get("iymd").toString():"20150101"));
					}
					
					// 机构
					String depName = userdepname!=null?userdepname:"--";
					//数据源
					String dsname=datasourcename!=null?datasourcename:"--";
					//运营商
					String sptypename=spnumtypename!=null?spnumtypename:"--";
					// 发送总数
					String icount = mt.get("succ")!=null?mt.get("succ").toString():"";
					// 接收失败数
					String rfail = mt.get("rfail2")!=null?mt.get("rfail2").toString():"";

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
					cell1[1].setCellValue(depName);
					cell1[2].setCellValue(dsname);
					cell1[3].setCellValue(sptypename);
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
				cell2[3].setCellValue(name);
				cell2[4].setCellValue(count);
				cell2[5].setCellValue("".equals(fail) ? "0" : fail);

			}

			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "机构详情统计报表" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() +".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗");
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"机构详情统计报表生成excel导出异常");
		}
		finally
		{
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
	 * @description：生成机构统计报表详情Excel 当前使用
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */

	public Map<String, String> createSysDepDetailReportExcel(List<DynaBean> mtdList,int reportType,CountReportVo countrptvo,
			String datasourcename,String userdepname,String spnumtypename,HttpServletRequest request) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "机构统计报表";
		String reportName = "SysDepDetailReport";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysDepReportDetail.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("机构统计报表详情生成报表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无机构统计报表详情数据！");
			throw new Exception("无机构统计报表详情数据！");
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
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.DEP_RPT_CONF_MENU_ID);
				//标题单元格索引，从3开始，因为已经有4个单元格
				int cellTitleIndex = 3;
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_sj",request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_jgczy",request));
				rowTitle.getCell(2).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_fslx",request));
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

				String name = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_hj", request);

				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{

					DynaBean mt = (DynaBean) mtdList.get(k);

					// 时间
					String sendTime = "";
					//SimpleDateFormat sdfz = new SimpleDateFormat("yyyy年M月d日");
					SimpleDateFormat sdfz = new SimpleDateFormat("yyyy-M-d");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
//					DateFormat df = DateFormat.getDateInstance();
					if(reportType == 2){
						//sendTime=(mt.get("y")!=null?mt.get("y").toString():"2015")+"年"+(mt.get("imonth")!=null?mt.get("imonth").toString():"01")+"月";
						sendTime=(mt.get("y")!=null?mt.get("y").toString():"2015")+"-"+(mt.get("imonth")!=null?mt.get("imonth").toString():"01");
					}else{
						sendTime=sdfz.format(sdf1.parse(mt.get("iymd")!=null?mt.get("iymd").toString():"20150101"));
					}
					
					// 机构
					String depName = userdepname!=null?userdepname:"--";
					//数据源
					String dsname=datasourcename!=null?datasourcename:"--";
					//运营商
					String sptypename=spnumtypename!=null?spnumtypename:"--";
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
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.DEP_RPT_CONF_MENU_ID);
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
					cell1[1].setCellValue(depName);
					cell1[2].setCellValue(dsname);
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
				cell2[3].setCellValue(name);
				//标题单元格索引，从3开始，因为已经有4个单元格
				int cellTotalIndex = 3;
				//根据原始值返回计算值
				Map<String, String> map=ReportBiz.getRptNums(countrptvo.getIcount(), countrptvo.getRsucc(), countrptvo.getRfail1(), countrptvo.getRfail2(), countrptvo.getRnret(), RptStaticValue.DEP_RPT_CONF_MENU_ID);
				for(RptConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):"0");
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
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):"0");
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

			fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_rpt_jgxqbb", request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() +".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗");
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"机构详情统计报表生成excel导出异常");
		}
		finally
		{
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
	 * @description：生成机构统计报表详情Excel
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */

	public Map<String, String> createSysDepAreaDetailReportExcel(List<DynaBean> mtdList,int reportType,String count, String fail,String datasourceareaname,String userareadepname,String showareatime) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "机构统计报表";
		String reportName = "SysDepDetailReport";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysDepAreaReport.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("机构统计报表国家详情生成报表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无机构统计报表国家详情数据！");
			throw new Exception("无机构统计报表国家详情数据！");
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
				//读取文件模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, reportName);
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook1);
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

				String name = "合计:";

				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					DynaBean mt = (DynaBean) mtdList.get(k);
					// 时间
					String sendTime = showareatime;
					//国家/地区代码
					String areacode=mt.get("areacode")!=null?mt.get("areacode").toString():"";
					//国家名称
					String areaname=mt.get("areaname")!=null?mt.get("areaname").toString():"";
					// 机构
					String depName = userareadepname!=null?userareadepname:"--";
					//通道号码
					String spgate=mt.get("spgate")!=null?mt.get("spgate").toString():"";
					//通道名称
					String gatename=mt.get("gatename")!=null?mt.get("gatename").toString():"";
					//数据源
					String dsname=datasourceareaname!=null?datasourceareaname:"--";
					// 发送总数
					String icount = mt.get("succ")!=null?mt.get("succ").toString():"";
					// 接收失败数
					String rfail = mt.get("rfail2")!=null?mt.get("rfail2").toString():"";

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
					cell1[5].setCellStyle(cellStyle[0]);
					cell1[6].setCellStyle(cellStyle[0]);
					cell1[7].setCellStyle(cellStyle[0]);
					cell1[8].setCellStyle(cellStyle[0]);
					
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(areacode);
					cell1[2].setCellValue(areaname);
					cell1[3].setCellValue(depName);
					cell1[4].setCellValue(spgate);
					cell1[5].setCellValue(gatename);
					cell1[6].setCellValue(datasourceareaname);
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
				cell2[6].setCellValue(name);
				cell2[7].setCellValue(count);
				cell2[8].setCellValue("".equals(fail) ? "0" : fail);

			}

			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "机构统计各国详情报表" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗");
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"机构统计各国详情报表生成excel导出异常");
		}
		finally
		{
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
	 * @description：生成机构统计报表详情Excel
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */

	public Map<String, String> createSysDepAreaDetailReportExcel(List<DynaBean> mtdList,int reportType,
			CountReportVo countrptvo,String datasourceareaname,String userareadepname,String showareatime,HttpServletRequest request) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "机构统计报表";
		String reportName = "SysDepDetailReport";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "sysDepAreaReport.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("机构统计报表国家详情生成报表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无机构统计报表国家详情数据！");
			throw new Exception("无机构统计报表国家详情数据！");
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
				//读取文件模板
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
				List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.DEP_RPT_CONF_MENU_ID);
				//标题单元格索引，从6开始，因为已经有7个单元格
				int cellTitleIndex = 6;
				rowTitle.getCell(0).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_sj",request));
				rowTitle.getCell(1).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_gjdqdm",request));
				rowTitle.getCell(2).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_new_rpt_gjmc",request));
				rowTitle.getCell(3).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_jgczy",request));
				rowTitle.getCell(4).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_tdhm",request));
				rowTitle.getCell(5).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_tdmc",request));
				rowTitle.getCell(6).setCellValue(MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_fslx",request));
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

				String name = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_hj",request);

				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					DynaBean mt = (DynaBean) mtdList.get(k);
					// 时间
					String sendTime = showareatime;
					//国家/地区代码
					String areacode=mt.get("areacode")!=null?mt.get("areacode").toString():"";
					//国家名称
					String areaname=mt.get("areaname")!=null?mt.get("areaname").toString():"";
					areaname = areaname.equals("")?areaname:MessageUtils.extractMessage("cxtj", "cxtj_country_"+areacode, request);
					// 机构
					String depName = userareadepname!=null?userareadepname:"--";
					//通道号码
					String spgate=mt.get("spgate")!=null?mt.get("spgate").toString():"";
					//通道名称
					String gatename=mt.get("gatename")!=null?mt.get("gatename").toString():"";
					//数据源
					String dsname=datasourceareaname!=null?datasourceareaname:"--";

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
					Map<String, String> map=ReportBiz.getRptNums(icount, rsucc, rfail1, rfail2, rnret, RptStaticValue.DEP_RPT_CONF_MENU_ID);

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
					cell1[5].setCellStyle(cellStyle[0]);
					cell1[6].setCellStyle(cellStyle[0]);
					
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(areacode);
					cell1[2].setCellValue(areaname);
					cell1[3].setCellValue(depName);
					cell1[4].setCellValue(spgate);
					cell1[5].setCellValue(gatename);
					cell1[6].setCellValue(datasourceareaname);
					
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
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):"0");
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
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):"0");
						}
						else if(RptStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
						{//接收失败数
							cell1[cellDataIndex] = row.createCell(cellDataIndex);
							cell1[cellDataIndex].setCellStyle(cellStyle[0]);
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL2_COLUMN_ID):"0");
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
							cell1[cellDataIndex].setCellValue(map.get(RptStaticValue.RPT_RNRET_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RNRET_COLUMN_ID):"0");
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
				Map<String, String> map=ReportBiz.getRptNums(countrptvo.getIcount(), countrptvo.getRsucc(), countrptvo.getRfail1(), countrptvo.getRfail2(), countrptvo.getRnret(), RptStaticValue.DEP_RPT_CONF_MENU_ID);
				for(RptConfInfo rptConf : rptConList)
				{
					cellTotalIndex++;
					//提交总数
					if(RptStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
					{
						cell1[cellTotalIndex] = row.createCell(cellTotalIndex);
						cell1[cellTotalIndex].setCellStyle(cellStyle[0]);
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_ICOUNT_COLUMN_ID):"0");
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
						cell1[cellTotalIndex].setCellValue(map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID)!=null?map.get(RptStaticValue.RPT_RFAIL1_COLUMN_ID):"0");
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

			fileName = MessageUtils.extractMessage("cxtj","cxtj_new_rpt_jgggbb",request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗");
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"机构统计各国详情报表生成excel导出异常");
		}
		finally
		{
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
	
	
	
}
