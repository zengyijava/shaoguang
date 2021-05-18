package com.montnets.emp.jfcx.biz;

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

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.jfcx.dao.GenericCrmBillQueryVoDAO;
import com.montnets.emp.jfcx.dao.ReportDAO;
import com.montnets.emp.jfcx.vo.CrmBillQueryVo;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.GlobalConst;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;

/**
 * 客户计费查询BIZ
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:20:34
 * @description
 */
public class CrmBillQueryBiz extends BaseBiz
{

	// 客户计费查询DAO
	protected GenericCrmBillQueryVoDAO	crmbillquerydao;
	// 报表DAO
	protected ReportDAO				reportDao;

	private final SpecialDAO				specialDao;

	// 构造函数
	public CrmBillQueryBiz()
	{
		// 实例化三个报表DAO类
		specialDao = new SpecialDAO();

		reportDao = new ReportDAO();

		crmbillquerydao = new GenericCrmBillQueryVoDAO();

	}

	/**
	 * 根据机构ID以及起始时间查询客户计费查询数据
	 * 
	 * @param depid
	 *        机构ID
	 * @param crmbillqueryvo
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
    public List<CrmBillQueryVo> getCrmBillQueryVoList(String depid, CrmBillQueryVo crmbillqueryvo, Long curUserId, String corpCode, PageInfo pageInfo) throws Exception
	{
		List<CrmBillQueryVo> billQueryList = new ArrayList<CrmBillQueryVo>();
		// 如果没有选择任何机构赋值当前登录人的部门id
		if(depid != null && !"".equals(depid.trim()))
		{
			depid = depid.trim();
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
			}
			else
			{
				return billQueryList;
			}
		}
		billQueryList = crmbillquerydao.getCrmBillQueryVoList(curUserId, depid, crmbillqueryvo, corpCode, pageInfo);// 获取操作员报表的相关信息

		return billQueryList;
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
	 * @param mtDataReportVo
	 *        查询条件对象
	 * @param curUserId
	 *        当前登录操作员ID
	 * @param corpCode
	 *        企业编码
	 * @return 结果集
	 * @throws Exception
	 */
    public List<CrmBillQueryVo> getCrmBillQueryVoListUnPage(Integer permissionType, String ownDepIds, CrmBillQueryVo mtDataReportVo, Long curUserId, String corpCode) throws Exception
	{
		List<CrmBillQueryVo> mtReportList = new ArrayList<CrmBillQueryVo>();
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
			}
			else
			{
				return mtReportList;
			}
		}
			mtReportList = crmbillquerydao.getCrmBillQueryVoDepUnPage(curUserId, depid, mtDataReportVo, corpCode);// 获取操作员报表的相关信息
		// 返回结果集
		return mtReportList;
	}

	/**
	 * @description：生成客户计费查询Excel
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */
    public Map<String, String> createCrmBillQueryExcel(List<CrmBillQueryVo> mtdList, String queryTime, String count, String fail, String showTime, String datasourcename) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "ydyw/jfcx/file";
		String voucherPath = "download";
		String reportFileName = "Ydyw";
		String reportName = "crmBillQuery";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "crmBillQuery.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);
		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("生成报表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无统计报表数据！");
			throw new Exception("无统计报表数据！");
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
		XSSFWorkbook workbook = null;
		OutputStream os = null;
		InputStream in = null;
		try
		{
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			in = new FileInputStream(file);
			for (int j = 1; j <= size; j++)
			{
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j + "].xlsx";
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;

				int index = 0;
				XSSFCell[] cell1 = new XSSFCell[9];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					CrmBillQueryVo crmbillqueryvo = (CrmBillQueryVo) mtdList.get(k);
					//手机号
					String mobile= crmbillqueryvo.getMobile()!=null?crmbillqueryvo.getMobile():"";
					//客户姓名
					String custoname=crmbillqueryvo.getCustomname()!=null?crmbillqueryvo.getCustomname():"";
					//证件号
					// 计费类型
					String chargestypename="";
					//资费
					//扣费账号
					String debitaccount=crmbillqueryvo.getDebitaccount()!=null?crmbillqueryvo.getDebitaccount():"";
					// 扣费状态
					String deductionstypename = "";
					if(crmbillqueryvo.getOprstate() != null && crmbillqueryvo.getOprstate() == 0){
						deductionstypename = "成功";
					}else if(crmbillqueryvo.getOprstate() != null && crmbillqueryvo.getOprstate() == 1){
						deductionstypename = "失败";
					}else{
						deductionstypename = "--";
					}
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//扣费时间
					String updatetime=crmbillqueryvo.getOprtime()!=null?df.format(crmbillqueryvo.getOprtime()):"";
					//隶属机构
					String depname=crmbillqueryvo.getDepname()!=null?crmbillqueryvo.getDepname():"";
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
					cell1[0].setCellValue(mobile);
					cell1[1].setCellValue(custoname);
					//cell1[2].setCellValue(identno);
					cell1[3].setCellValue(chargestypename);
					//cell1[4].setCellValue(chargesmoney);
					cell1[5].setCellValue(debitaccount);
					cell1[6].setCellValue(deductionstypename);
					cell1[7].setCellValue(updatetime);
					cell1[8].setCellValue(depname);
					// 一页里的行数
					index++;
				}
			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);
			fileName = "客户计费查询" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
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
			EmpExecutionContext.error(e,"客户计费查询生成excel导出异常");
		}
		finally
		{
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

	/**
	 * 生成报表的重载方法，加入语言格式参数
	 * @param mtdList
	 * @param queryTime
	 * @param count
	 * @param fail
	 * @param showTime
	 * @param datasourcename
	 * @param empLangName  语言格式
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createCrmBillQueryExcel(List<CrmBillQueryVo> mtdList, String queryTime, String count, String fail, String showTime,String datasourcename,String empLangName) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "ydyw/jfcx/file";
		String voucherPath = "download";
		String reportFileName = "Ydyw";
		String reportName = "crmBillQuery";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "crmBillQuery_"+empLangName+".xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);
		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("生成报表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无统计报表数据！");
			throw new Exception("无统计报表数据！");
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
		XSSFWorkbook workbook = null;
		OutputStream os = null;
		InputStream in = null;
		try
		{
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			in = new FileInputStream(file);
			for (int j = 1; j <= size; j++)
			{
				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j + "].xlsx";
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;

				int index = 0;
				XSSFCell[] cell1 = new XSSFCell[9];
				int k;
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					CrmBillQueryVo crmbillqueryvo =  mtdList.get(k);
					//手机号
					String mobile= crmbillqueryvo.getMobile()!=null?crmbillqueryvo.getMobile():"";
					//客户姓名
					String custoname=crmbillqueryvo.getCustomname()!=null?crmbillqueryvo.getCustomname():"";
					//证件号
					//计费类型
					String chargestypename;
					Integer taocantype = crmbillqueryvo.getTaocantype();
					switch (taocantype){
						case 1:chargestypename = "zh_HK".equals(empLangName)?"VIP":"zh_TW".equals(empLangName)?"VI免費":"VIP免费";break;
						case 2:chargestypename = "zh_HK".equals(empLangName)?"Monthly":"包月";break;
						case 3:chargestypename = "zh_HK".equals(empLangName)?"Quarterly":"包季";break;
						case 4:chargestypename = "zh_HK".equals(empLangName)?"Yearly":"包年";break;
						default:chargestypename = "--";
					}
					//资费
					Integer chargesmoney = crmbillqueryvo.getTaocanmoney();
					//扣费账号
					String debitaccount=crmbillqueryvo.getDebitaccount()!=null?crmbillqueryvo.getDebitaccount():"";
					// 扣费状态
					String deductionstypename;
					Integer oprstate = crmbillqueryvo.getOprstate();
					switch (oprstate){
						case 0:deductionstypename = "zh_HK".equals(empLangName)?"Waiting for billing":"zh_TW".equals(empLangName)?"等待扣費":"等待扣费";break;
						case 1:deductionstypename = "zh_HK".equals(empLangName)?"Billed successfully":"zh_TW".equals(empLangName)?"扣費成功":"扣费成功";break;
						case 2:deductionstypename = "zh_HK".equals(empLangName)?"Billed failed":"zh_TW".equals(empLangName)?"扣費失敗":"扣费失败";break;
						case 3:deductionstypename = "zh_HK".equals(empLangName)?"Refund successfully":"zh_TW".equals(empLangName)?"退費成功":"退费成功";break;
						case 4:deductionstypename = "zh_HK".equals(empLangName)?"Failed to refund":"zh_TW".equals(empLangName)?"退費失敗":"退费失败";break;
						case 5:deductionstypename = "zh_HK".equals(empLangName)?"Refund is in application":"zh_TW".equals(empLangName)?"退費申請中":"退费申请中";break;
						default:deductionstypename = "-";
					}
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//扣费时间
					String updatetime=crmbillqueryvo.getOprtime()!=null?df.format(crmbillqueryvo.getOprtime()):"";
					//隶属机构
					String depname=crmbillqueryvo.getDepname()!=null?crmbillqueryvo.getDepname():"";
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
					cell1[0].setCellValue(mobile);
					cell1[1].setCellValue(custoname);
					cell1[2].setCellValue(chargestypename);
					cell1[3].setCellValue(chargesmoney);
					cell1[4].setCellValue(debitaccount);
					cell1[5].setCellValue(deductionstypename);
					cell1[6].setCellValue(updatetime);
					cell1[7].setCellValue(depname);
					// 一页里的行数
					index++;
				}
			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);
			String CustomerBillingQuery = "zh_HK".equals(empLangName)?"CustomerBillingQuery":"zh_TW".equals(empLangName)?"客戶計費查詢":"客户计费查询";
			fileName = CustomerBillingQuery + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
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
			EmpExecutionContext.error(e,"客户计费查询生成excel导出异常");
		}
		finally
		{
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
}
