package com.montnets.emp.jfcx.biz;

import java.io.File;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.ydyw.LfDeductionsDisp;
import com.montnets.emp.entity.ydyw.LfDeductionsList;
import com.montnets.emp.jfcx.dao.GenericCrmRfMgrVoDAO;
import com.montnets.emp.jfcx.dao.ReportDAO;
import com.montnets.emp.jfcx.vo.CrmRfMgrVo;
import com.montnets.emp.mobilebus.biz.BuckleFeeBiz;
import com.montnets.emp.mobilebus.biz.BuckleFeeParams;
import com.montnets.emp.util.GlobalConst;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 客户退费管理查询
 * @project p_ydyw
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-12-29 下午04:15:00
 * @description
 */
public class CrmRfMgrBiz extends BaseBiz
{

	// 客户计费查询DAO
	protected GenericCrmRfMgrVoDAO	reportVoDao;

	// 报表DAO
	protected ReportDAO				reportDao;

	private SpecialDAO				specialDao;
	
	private IEmpTransactionDAO empTransactionDAO;
	
	private BuckleFeeBiz feeBiz = new BuckleFeeBiz();
	
	private BaseBiz baseBiz = new BaseBiz();
	
	// 构造函数
	public CrmRfMgrBiz()
	{
		specialDao = new SpecialDAO();

		reportDao = new ReportDAO();

		reportVoDao = new GenericCrmRfMgrVoDAO();
		
		empTransactionDAO=new DataAccessDriver().getEmpTransDAO();

	}

	/**
	 * 根据机构ID以及起始时间查询客户退费管理数据
	 * 
	 * @param depid
	 *        机构ID
	 * @param crmrfmgrvo
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
	public List<CrmRfMgrVo> getCrmRfMgrVoList(String depid, CrmRfMgrVo crmrfmgrvo, Long curUserId, String corpCode, PageInfo pageInfo) throws Exception
	{
		List<CrmRfMgrVo> billQueryList = new ArrayList<CrmRfMgrVo>();
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
				}
			}
			else
			{
				return billQueryList;
			}
		}
		billQueryList = reportVoDao.getCrmRfMgrVoList(curUserId, depid, crmrfmgrvo, corpCode, pageInfo);// 获取操作员报表的相关信息
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
	public List<CrmRfMgrVo> getCrmRfMgrVoListUnPage(Integer permissionType, String ownDepIds, CrmRfMgrVo mtDataReportVo, Long curUserId, String corpCode) throws Exception
	{
		List<CrmRfMgrVo> mtReportList = new ArrayList<CrmRfMgrVo>();
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
			mtReportList = reportVoDao.getCrmRfMgrVoDepUnPage(curUserId, depid, mtDataReportVo, corpCode);// 获取操作员报表的相关信息
		// 返回结果集
		return mtReportList;
	}

	
	/**
	 * 申请单个退费
	 * @param defuctionlist
	 * 		      客户计费信息
	 * @param money
	 * 		     退费金额
	 * @param userid
	 * 		     操作员id
	 * @return String 返回消息
	 */
	public String singleRefund(LfDeductionsList list,String money,String userid)
	{
		String result = "";
		Connection conn =null;
		//流水表
		LfDeductionsDisp disp = null;
		//操作员
		LfSysuser user = null;
		//流水号
		String msgid = "";
		boolean flag = false;
		//更新条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//
		LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
		try 
		{
			int intMoney = Integer.parseInt(money);
			//通过id获取操作员信息
			user = this.getLfSysuserByUserId(userid) ;
			//生成流水号
			msgid = new BuckleFeeBiz().getMsgId(user.getCorpCode());
			//写文件
			flag =	this.saveRefundFiel(intMoney, list, msgid);	
			if(flag)
			{
				conn = empTransactionDAO.getConnection();
				empTransactionDAO.beginTransaction(conn);
				//（未曾退费）
				conditionMap.put("deductionstype", "1");
				objectMap = initObjectmap(list, intMoney, user, msgid, objectMap);
				flag = empTransactionDAO.update(conn, LfDeductionsList.class, objectMap,list.getId()+"", conditionMap);
				if(flag)
				{
					//通过id获取操作员信息
					user = this.getLfSysuserByUserId(userid) ;
					//流水表
					disp = initLfDeductionsDisp(list,intMoney,user,msgid);
					//保存流水表
					flag = empTransactionDAO.save(conn, disp);
				}
			}
			if(flag)
			{
				result = "success";
				empTransactionDAO.commitTransaction(conn);
			}
			else
			{
				result = "fail";
				empTransactionDAO.rollBackTransaction(conn);
			}
		} 
		catch (Exception e)
		{
			result = "fail";
			empTransactionDAO.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"申请单个退费异常!");
		}
		finally
		{
			empTransactionDAO.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 批量退费
	 * @param lists
	 * 			计费表集合
	 * @param money
	 * 			退费金额
	 * @param userid
	 * 			操作员ID
	 * @return
	 */
	public String muliteRefund(List<LfDeductionsList> lists,String money,String userid)
	{
		String result = "";
		Connection conn =null;
		//流水表
		LfDeductionsDisp disp = null;
		//计费表
		LfDeductionsList list = null;
		//操作员
		LfSysuser user = null;
		boolean flag = true;
		//流水号
		String msgid = "";
		//更新条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//
		//（未曾退费）
		conditionMap.put("deductionstype", "1");
		LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
		try
		{
			conn = empTransactionDAO.getConnection();
			empTransactionDAO.beginTransaction(conn);
			//通过id获取操作员信息
			user = this.getLfSysuserByUserId(userid) ;
			int intMoney = Integer.parseInt(money);
			for(int i=0;i<lists.size();i++)
			{
				list = lists.get(i);
				msgid =feeBiz.getMsgId(user.getCorpCode());
				flag =	this.saveRefundFiel(intMoney, list, msgid);	
				if(flag)
				{
					objectMap = initObjectmap(list, intMoney, user, msgid, objectMap);
					flag = empTransactionDAO.update(conn, LfDeductionsList.class, objectMap,list.getId()+"", conditionMap);
					if(flag)
					{
						disp = initLfDeductionsDisp(list,intMoney,user,msgid);
						//保存流水表
						flag = empTransactionDAO.save(conn, disp);
					}
					//保存流水表失败
					else
					{
						throw new RuntimeException("批量退费异常");
					}
				}
			}
			result = "success";
			empTransactionDAO.commitTransaction(conn);
		}
		catch (Exception e)
		{
			result = "fail";
			empTransactionDAO.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"批量退费异常!");
		}
		finally
		{
			empTransactionDAO.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 处理退费结果
	 * @param recedeFeeMap
	 */
	public void dealRefundResult(Map<String, BuckleFeeParams> recedeFeeMap)
	{
		Iterator<Entry<String, BuckleFeeParams>> iterator = recedeFeeMap.entrySet().iterator();
		Entry<String, BuckleFeeParams> entry = null;
		BuckleFeeParams buckleFeeParams = null;
		while(iterator.hasNext())
		{
			entry = iterator.next();
			buckleFeeParams = entry.getValue();
			try
			{
				dealRefundUpdate(buckleFeeParams);
			}
			catch (Exception e2)
			{
				EmpExecutionContext.error(e2, "处理退费结果失败！");
			}
		}
	}
	/**
	 * 处理退费结果
	 * @param buckleFeeParams
	 * @return
	 */
	private void dealRefundUpdate(BuckleFeeParams buckleFeeParams)
	{
		boolean flag=false;
		Connection conn = null;
		LfDeductionsList list = null;
		List<LfDeductionsList> lists = null;
		LfDeductionsDisp disp = null;
		List<LfDeductionsDisp> disps = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			//更新计费表
			conditionMap.put("msgid", buckleFeeParams.getMsgId());
			lists = baseBiz.getByCondition(LfDeductionsList.class, conditionMap, null);
			if(lists!=null&&lists.size()>0)
			{
				list = lists.get(0);
			}
			//更新时间
			Timestamp updatetime = new Timestamp(new Date().getTime());
			//扣费时间
			Timestamp refundtime = new Timestamp(buckleFeeParams.getTime());
			if(list!=null){
				list.setUdpatetime(updatetime);
				list.setBuckleupfeetime(refundtime);
				//退费状态 0 - 成功 1- 失败
				if("0".equals(buckleFeeParams.getState()))
				{
					list.setDeductionstype(3);
				}
				else
				{
					list.setDeductionstype(4);
				}
			}
			
			flag = baseBiz.updateObj(conn, list);
			//跟新计费表成功后更新流水表
			if(flag)
			{
				//更新流水
				disps = baseBiz.getByCondition(LfDeductionsDisp.class, conditionMap, null);
				if(disps!=null&&disps.size()>0)
				{
					disp = disps.get(0);
					//操作状态 0- 成功  1 -失败
					if("0".equals(buckleFeeParams.getState()))
					{
						disp.setOprstate(3);
					}
					else
					{
						disp.setOprstate(4);
					}
					//操作时间
					disp.setOprtime(refundtime);
					//更新时间
					disp.setUpdatetime(updatetime);
				}
				flag = baseBiz.updateObj(conn, disp);
			}
			if(flag)
			{
				empTransDao.commitTransaction(conn);
			}
			else
			{
				empTransDao.rollBackTransaction(conn);
			}
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"处理退费结果失败！");
			empTransDao.rollBackTransaction(conn);
		} 
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 初始化流水表
	 * @param list
	 * 		     客户计费信息
	 * @param money
	 * 		     金额
	 * @param user
	 * 		     操作员
	 * @return LfDeductionsDisp
	 * 		
	 */
	public static LfDeductionsDisp initLfDeductionsDisp(LfDeductionsList list,int money,LfSysuser user,String msgid)
	{
		LfDeductionsDisp disp = new LfDeductionsDisp();
		//签约账号
		disp.setAcctno(list.getAcctno());
		//签约机构ID
		disp.setContractdep(list.getContractdep());
		//签约ID 
		disp.setContractid(list.getContractid());
		//签约状态（0:：已签约；1：已取消签约；2：已冻结）
		disp.setContractstate(list.getContractstate());
		//签约操作员ID
		disp.setContractuser(list.getContractuser());
		disp.setCorpcode(list.getCorpcode());
		//客户姓名
		disp.setCustomname(list.getCustomname());
		//扣款账号
		disp.setDebitaccount(list.getDebitaccount());
		//扣费金额
		disp.setDeductionsmoney(money);
		//DEDUCTIONS_TYPE	操作类型（0：扣费；1：退费）
		disp.setDeductionstype(1);
		//操作员机构ID
		disp.setDepid(user.getDepId());
		//签约机构名称
		disp.setDepname(list.getDepname());
		//电话号码
		disp.setMobile(list.getMobile());
		//扣费状态-5 等待扣费
		disp.setOprstate(5);
		//套餐编号
		disp.setTaocancode(list.getTaocancode());
		//套餐资费金额
		disp.setTaocanmoney(list.getTaocanmoney());
		//套餐名称
		disp.setTaocanname(list.getTaocanname());
		//套餐类型
		disp.setTaocantype(list.getTaocantype());
		//操作员ID
		disp.setUserid(user.getUserId());
		//签约操作员名称
		disp.setUsername(user.getName());
		Timestamp time = new Timestamp(new Date().getTime());
		//跟新时间
		disp.setUpdatetime(time);
		//流水号
		disp.setMsgid(msgid);
		return disp;
	}
	 
	 /**
	  * 设置更新的LfDeductionsList map
	  * @param list
	  * @param money
	  * @param user
	  * @param msgid
	  * @param Objectmap
	  * @return Objectmap
	  */
	public static LinkedHashMap<String, Object> initObjectmap(LfDeductionsList list,int money,LfSysuser user,String msgid,LinkedHashMap<String, Object> objectMap)
	{
		//状态 5 - 申请退费中
		objectMap.put("deductionstype", 5);
		objectMap.put("udpatetime", new Timestamp(new Date().getTime()));
		//操作员机构id
		objectMap.put("depid", user.getDepId());
		//操作员id
		objectMap.put("userid",user.getUserId());
		//流水号
		objectMap.put("msgid",msgid);
		//退费总金额
		objectMap.put("bupsummoney",money);
		//退费总次数
		int bupTime = list.getBuptimer()==null?0:list.getBuptimer();
		bupTime += 1;
		objectMap.put("buptimer",bupTime);
		return objectMap;
		
	}
	
	
	/**
	 * 保存退费文件
	 * @param money
	 * @param list
	 * @param msgid
	 * @return true or false
	 */
	public  boolean saveRefundFiel(int money,LfDeductionsList list,String msgid)
	{
		boolean result =  false;
		String msg = msgid+"|"+list.getMobile()+"|"+list.getDebitaccount()+"|"+list.getTaocancode()+"|"+money+"|"+2+"|0|"+System.currentTimeMillis()+"&";
		result = feeBiz.saveBuckleFeeFile(msg);	
		return result;
	}
	
	/**
	 * @description：生成客户退费管理Excel
	 * @param fileName
	 *        文件名
	 * @param mtdList
	 *        报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */
	public Map<String, String> createCrmRfMgrVoExcel(List<CrmRfMgrVo> mtdList, String queryTime, String count, String fail, String showTime,String datasourcename) throws Exception
	{
		String BASEDIR = new TxtFileUtil().getWebRoot() + "ydyw/jfcx/file";
		String voucherPath = "download";
		String reportFileName = "Ydyw";
		String reportName = "crmBillQuery";
		String voucherTemplatePath = BASEDIR + File.separator + GlobalConst.SAMPLES + File.separator + "crmBillQuery.xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
//		int intRowsOfPage = 500000;
//		// 当前每页显示条数
//		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);
//		int size = intPagesCount; // 生成的工作薄个数
//		EmpExecutionContext.info("生成报表个数：" + size);
//		if(size == 0)
//		{
//			EmpExecutionContext.info("无统计报表数据！");
//			throw new Exception("无统计报表数据！");
//		}
//		// 产生报表文件的存储路径
//		Date curDate = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
//		String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + sdf.format(curDate);
//		File fileTemp = new File(voucherFilePath);
//		if(!fileTemp.exists())
//		{
//			fileTemp.mkdirs();
//		}
//		// 报表文件名
//		String fileName = null;
//		String filePath = null;
//		XSSFWorkbook workbook = null;
//		OutputStream os = null;
//		InputStream in = null;
//		try
//		{
//			// 创建只读的Excel工作薄的对象, 此为模板文件
//			File file = new File(voucherTemplatePath);
//			in = new FileInputStream(file);
//			for (int j = 1; j <= size; j++)
//			{
//				fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j + "].xlsx";
//				// 工作表
//				workbook = new XSSFWorkbook(in);
//				// 表格样式
//				XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook);
//				XSSFSheet sheet = workbook.cloneSheet(0);
//				workbook.removeSheetAt(0);
//				workbook.setSheetName(0, reportName);
//				// 读取模板工作表
//				XSSFRow row = null;
//				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
//
//				// 总体流程
//				// 根据记录，计算总的页数
//				// 每页的处理 一页一个sheet
//				// 写入页标题
//				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
//				// 写页尾
//
//				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
//
//				int index = 0;
//				XSSFCell[] cell1 = new XSSFCell[9];
//				int k;
//				for (k = (j - 1) * intRowsOfPage; k < intCnt; k++)
//				{
//					CrmRfMgrVo crmrfmgrvo = (CrmRfMgrVo) mtdList.get(k);
//					//手机号
//					String mobile= crmrfmgrvo.getMobile()!=null?crmrfmgrvo.getMobile():"";
//					//客户姓名
//					String custoname=crmrfmgrvo.getCustoname()!=null?crmrfmgrvo.getCustoname():"";
//					//证件号
//					String identno =crmrfmgrvo.getIdentno()!=null?crmrfmgrvo.getIdentno():"";
//					// 计费类型
//					String chargestypename="";
//					if(crmrfmgrvo.getChargestype()!=null&&crmrfmgrvo.getChargestype()==1){
//						chargestypename="包年";
//					}else if(crmrfmgrvo.getChargestype()!=null&&crmrfmgrvo.getChargestype()==2){
//						chargestypename="包月";
//					}else if(crmrfmgrvo.getChargestype()!=null&&crmrfmgrvo.getChargestype()==3){
//						chargestypename="VIP免费";
//					}else{
//						chargestypename="--";
//					}
//					//资费
//					String chargesmoney=crmrfmgrvo.getChargesmoney()!=null?crmrfmgrvo.getChargesmoney().toString():"";
//					//扣费账号
//					String debitaccount=crmrfmgrvo.getDebitaccount()!=null?crmrfmgrvo.getDebitaccount():"";
//					// 扣费状态
//					String deductionstypename = "";
//					if(crmrfmgrvo.getDeductionstype() != null && crmrfmgrvo.getDeductionstype() == 0){
//						deductionstypename = "成功";
//					}else if(crmrfmgrvo.getDeductionstype() != null && crmrfmgrvo.getDeductionstype() == 1){
//						deductionstypename = "失败";
//					}else{
//						deductionstypename = "--";
//					}
//					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					//扣费时间
//					String updatetime=crmrfmgrvo.getUpdatetime()!=null?df.format(crmrfmgrvo.getUpdatetime()):"";
//					//隶属机构
//					String depname=crmrfmgrvo.getDepname()!=null?crmrfmgrvo.getDepname():"";
//					row = sheet.createRow(k + 1);
//					// 生成四个单元格
//					cell1[0] = row.createCell(0);
//					cell1[1] = row.createCell(1);
//					cell1[2] = row.createCell(2);
//					cell1[3] = row.createCell(3);
//					cell1[4] = row.createCell(4);
//					cell1[5] = row.createCell(5);
//					cell1[6] = row.createCell(6);
//					cell1[7] = row.createCell(7);
//					cell1[8] = row.createCell(8);
//					// 设置单元格样式
//					cell1[0].setCellStyle(cellStyle[0]);
//					cell1[1].setCellStyle(cellStyle[0]);
//					cell1[2].setCellStyle(cellStyle[0]);
//					cell1[3].setCellStyle(cellStyle[0]);
//					cell1[4].setCellStyle(cellStyle[0]);
//					cell1[5].setCellStyle(cellStyle[0]);
//					cell1[6].setCellStyle(cellStyle[0]);
//					cell1[7].setCellStyle(cellStyle[0]);
//					cell1[8].setCellStyle(cellStyle[0]);
//					// 设置单元格内容
//					cell1[0].setCellValue(mobile);
//					cell1[1].setCellValue(custoname);
//					cell1[2].setCellValue(identno);
//					cell1[3].setCellValue(chargestypename);
//					cell1[4].setCellValue(chargesmoney);
//					cell1[5].setCellValue(debitaccount);
//					cell1[6].setCellValue(deductionstypename);
//					cell1[7].setCellValue(updatetime);
//					cell1[8].setCellValue(depname);
//					// 一页里的行数
//					index++;
//				}
//			}
//			// 输出到xlsx文件
//			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
//			// 写入Excel对象
//			workbook.write(os);
//			fileName = "客户退费管理" + sdf.format(curDate) + ".zip";
//			filePath = BASEDIR + File.separator + voucherPath + File.separator + reportFileName + File.separator + fileName;
//			// 压缩文件夹
//			ZipUtil.compress(voucherFilePath, filePath);
//			// 删除文件夹
//			FileUtils.deleteDir(fileTemp);
//		}
//		catch (Exception e)
//		{
//			EmpExecutionContext.error(e,"客户退费管理生成excel导出异常");
//		}
//		finally
//		{
//			// 清除对象
//			workbook = null;
//			os.close();
//			in.close();
//		}
//		resultMap.put("FILE_NAME", fileName);
//		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}

	
}
