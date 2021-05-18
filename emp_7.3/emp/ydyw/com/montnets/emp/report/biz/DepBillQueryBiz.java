package com.montnets.emp.report.biz;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.mobilebus.constant.MobileBusStaticValue;
import com.montnets.emp.report.dao.GenericDepBillQueryVoDAO;
import com.montnets.emp.report.vo.DepBillQueryVo;
import com.montnets.emp.util.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 机构计费统计报表BIZ
 * @project p_ydyw
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-15 下午01:55:27
 * @description
 */
public class DepBillQueryBiz extends BaseBiz
{
	// 机构计费报表DAO
	protected GenericDepBillQueryVoDAO	reportVoDao;
	//公共方法
	private final SpecialDAO				specialDao;

	// 构造函数
	public DepBillQueryBiz()
	{
		// 实例化三个报表DAO类
		specialDao = new SpecialDAO();
		reportVoDao = new GenericDepBillQueryVoDAO();
	}

	/**
	 * 根据机构ID以及年月查询机构计费报表数据
	 * @param depid
	 * @param depbillqueryvo
	 * @param curUserId
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DepBillQueryVo> getDepBillQueryVoList(String depid, DepBillQueryVo depbillqueryvo, Long curUserId, String corpCode, PageInfo pageInfo) throws Exception
	{
		List<DepBillQueryVo> mtReportList = new ArrayList<DepBillQueryVo>();
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
		// 调用 查询方法
		mtReportList = reportVoDao.getDepBillQueryVoList(curUserId, depid, depbillqueryvo, corpCode, pageInfo);// 获取操作员报表的相关信息
		return mtReportList;
	}


	/**
	 * 合计汇总数量
	 * @param curUserId
	 * @param depids
	 * @param depbillqueryvo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public Long findSumCount(Long curUserId, String depids, DepBillQueryVo depbillqueryvo, String corpCode) throws Exception
	{
		String depid = "";
		// 如果没有选择任何机构赋值当前登录人的部门id
		if(depids != null && !"".equals(depids.trim()))
		{
			depid = depids.trim();
		}
		else
		{
			long returnLong =0;
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
					return returnLong;
				}
			}
			else
			{
				return returnLong;
			}

		}
		return reportVoDao.findSumCount(curUserId, depid, depbillqueryvo, corpCode);
	}

	/**
	 * 导出使用 根据机构ID以及查询条件对象汇总表里面的相关信息
	 * 
	 * @param depids
	 * @param mtDataReportVo
	 * @param curUserId
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<DepBillQueryVo> getDepBillQueryVoDepUnPage(String depids, DepBillQueryVo mtDataReportVo, Long curUserId, String corpCode) throws Exception
	{
		List<DepBillQueryVo> mtReportList = new ArrayList<DepBillQueryVo>();
		String depid = "";
		// 如果没有选择任何机构赋值当前登录人的部门id
		if(depids != null && !"".equals(depids.trim()))
		{
			depid = depids.trim();
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
		mtReportList = reportVoDao.getDepBillQueryVoDepUnPage(curUserId, depid, mtDataReportVo, corpCode);// 获取操作员报表的相关信息
		// 返回结果集
		return mtReportList;
	}


	/**
	 * 生成机构计费统计报表Excel
	 * @param mtdList
	 * @param countTime
	 * @param sumtotoalmoney
	 * @return Map<String, String>
	 * @throws Exception
	 */
	public Map<String, String> createDepBillQueryExcel(List<DepBillQueryVo> mtdList, String countTime,String sumtotoalmoney,String empLangName) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "ydyw/rpt/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		String reportName = "DepBillQuery";

		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "depBillQuery_"+empLangName+".xlsx";
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

				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j + "]_"+ StaticValue.getServerNumber() +".xlsx";
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
				XSSFCell[] cell1 = new XSSFCell[11];
				XSSFCell[] cell2 = new XSSFCell[11];
				int k;
				String name = "zh_HK".equals(empLangName)?"Total:":"zh_CN".equals(empLangName)?"合计:":"合計";
				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					DepBillQueryVo dbqv = (DepBillQueryVo) mtdList.get(k);
					// 时间
					String showTime = countTime;
					if (countTime.contains("-")) {
						String[] showTimeArray = countTime.split("-");
						Integer monthtime = Integer.parseInt(showTimeArray[1].toString());// 显示月份
						showTime = "zh_HK".equals(empLangName)? showTimeArray[0] + "-" + monthtime : showTimeArray[0] + "年" + monthtime + "月";
					} else {
						showTime = "zh_HK".equals(empLangName)? countTime : countTime + "年";
					}

					String sendTime = showTime;
					// 机构
					String depName = dbqv.getDepname() != null ? dbqv.getDepname() : "";
					String operator = "zh_HK".equals(empLangName)?"Operator":"zh_CN".equals(empLangName)?"操作员":"操作員";
					String organization = "zh_HK".equals(empLangName)?"Organization":"zh_CN".equals(empLangName)?"机构":"機構";
					depName = depName.replace("[操作员]","["+operator+"]");
					depName = depName.replace("[机构]","["+organization+"]");
					if(dbqv.getDepname() != null && dbqv.getUserstate() != null && dbqv.getUserstate() == 2)
					{
						depName += "zh_HK".equals(empLangName)?"(Cancelled)":"zh_CN".equals(empLangName)?"（已注销）":"（已註銷）";
					}
					// 业务套餐名称
					String nullValue = "zh_HK".equals(empLangName)?"-":"zh_CN".equals(empLangName)?"无":"無";
					String taocanname = dbqv.getTaocanname() != null && !"".equals(dbqv.getTaocanname()) ? dbqv.getTaocanname() : nullValue;
					// 计费类型
					Map<String,String> tctypemap=MobileBusStaticValue.getTaoCanType();
					for (String typeName: tctypemap.keySet()){
						if(!"zh_CN".equals(empLangName)){
							switch (Integer.parseInt(typeName)){
								case -1:break;
								case 1:tctypemap.put("1","zh_HK".equals(empLangName)?"VIP":"VIP免費");break;
								case 2:tctypemap.put("2","zh_HK".equals(empLangName)?"Monthly":"包月");break;
								case 3:tctypemap.put("3","zh_HK".equals(empLangName)?"Quarterly":"包季");break;
								default:tctypemap.put("4","zh_HK".equals(empLangName)?"Yearly":"包年");break;
							}
						}
					}
					String taocantype="无";
					if(tctypemap!=null&&tctypemap.get("-1")!=null){
						taocantype = dbqv.getTaocantype()!=null?tctypemap.get(dbqv.getTaocantype().toString())!=null?tctypemap.get(dbqv.getTaocantype().toString()):nullValue:nullValue;
					}
					// 资费
					String taocanmoney = dbqv.getTaocanmoney() != null ? dbqv.getTaocanmoney().toString() : "0";
					//签约人数
					String contractcount=dbqv.getContractcount()!=null?dbqv.getContractcount().toString():"0";
					//扣费成功数
					String deductioncount=dbqv.getDeductioncount()!=null?dbqv.getDeductioncount().toString():"0";
					//扣费失败数
					String deductionfailcount=dbqv.getDeductionfailcount()!=null?dbqv.getDeductionfailcount().toString():"0";
					//扣费总金额
					String deductiontotalcount=dbqv.getDeductiontotalcount()!=null?dbqv.getDeductiontotalcount().toString():"0";
					//退费总金额
					String backmoney=dbqv.getBackmoney()!=null?dbqv.getBackmoney().toString():"0";
					//实际总收入
					String totalmoney = dbqv.getTotalmoney() != null ? dbqv.getTotalmoney().toString() : "";
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
					cell1[9] = row.createCell(9);
					cell1[10] = row.createCell(10);
					

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
					cell1[9].setCellStyle(cellStyle[0]);
					cell1[10].setCellStyle(cellStyle[0]);
					
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);//日期
					cell1[1].setCellValue(depName); //机构
					cell1[2].setCellValue(taocanname); //套餐名称
					cell1[3].setCellValue(taocantype);//计费类型
					cell1[4].setCellValue(taocanmoney);//资费
					cell1[5].setCellValue(contractcount);//签约人数
					cell1[6].setCellValue(deductioncount);//扣费成功数
					cell1[7].setCellValue(deductionfailcount);//扣费失败数
					cell1[8].setCellValue(deductiontotalcount);//扣费总金额
					cell1[9].setCellValue(backmoney);//退费总金额
					cell1[10].setCellValue(totalmoney);//实际总收入
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
				cell2[9] = row.createCell(9);
				cell2[10] = row.createCell(10);

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
				cell2[9].setCellStyle(cellStyle[1]);
				cell2[10].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue(name);
				cell2[3].setCellValue("");
				cell2[4].setCellValue("");
				cell2[5].setCellValue("");
				cell2[6].setCellValue("");
				cell2[7].setCellValue("");
				cell2[8].setCellValue("");
				cell2[9].setCellValue("");
				cell2[10].setCellValue(sumtotoalmoney != null && !"".equals(sumtotoalmoney) ? sumtotoalmoney : "0");
			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);
			String OrganizationBillingStatisticsReport = "zh_HK".equals(empLangName)?"OrganizationBillingStatisticsReport_":"zh_CN".equals(empLangName)?"机构计费统计报表":"機構計費統計報表";
			fileName = OrganizationBillingStatisticsReport + sdf.format(curDate) + ".zip";
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
			EmpExecutionContext.error(e,"机构计费统计报表生成excel导出异常");
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
