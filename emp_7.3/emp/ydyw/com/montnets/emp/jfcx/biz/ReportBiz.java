package com.montnets.emp.jfcx.biz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.jfcx.dao.ReportDAO;

public class ReportBiz extends BaseBiz
{
	
	//定义报表SpecialDAO
	protected ReportDAO  reportDao=new ReportDAO();
	
	protected SpecialDAO specialDao=new SpecialDAO();
	
	/**
	 * 获取所有sp账号
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<Userdata> getAllUserdata(int type) throws Exception {
		List<Userdata> userDatasList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String,String>();
		try{
			conditionMap.put("uid&>", "100001");
			if(type==1)
			{
				conditionMap.put("userType", "0");
			}else if(type==2)
			{
				conditionMap.put("userType", "1");
			}
			orderMap.put("userId",StaticValue.ASC);
			//调用查询方法并返回结果list
            userDatasList = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
		}catch(Exception e){
			EmpExecutionContext.error(e," 获取所有sp账号异常");
			//抛出异常
			throw e;
		}
		//返回查询结果
		return userDatasList;
	}
	
	
	/**
	 * 通过操作员ID获取获取所有管辖范围内的机构
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> getAllDeps(Long userId) throws Exception
	{
		List<LfDep> depList = new ArrayList<LfDep>();
		try
		{
			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>(); 
			orderbyMap.put("depId","asc");
			 //调用dao方法
			depList = reportDao.findDomDepBySysuserID(userId.toString(), orderbyMap);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e," 通过操作员ID获取获取所有管辖范围内的机构异常");
			throw e;
		}
		return depList;
	}
	
	/**
	 * 
	 * 根据当前的企业编码，查询出这个企业下的所有机构
	 * 
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> getAllDepsByCorpCode(String corpCode) throws Exception
	{
		//机构集合
		List<LfDep> depList = new ArrayList<LfDep>();
		try
		{
			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 

			//企业编码
			conditionMap.put("corpCode", corpCode);
			//部门状态
			conditionMap.put("depState", "1");
			orderbyMap.put("depId","asc");
			//判断是否是多企业
			if( null != corpCode)
			{
				depList = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
			}
			

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"  根据当前的企业编码，查询出这个企业下的所有机构异常");
			throw e;
		}
		return depList;
	}
	
	
	/**
	 * 通过企业编码和父及机构ID查出所有的下级机构
	 * @param corpCode
	 * @param superiorDepId
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> getAllDepsByCorpCode(String corpCode,Long superiorDepId) throws Exception
	{
		//机构集合对象
		List<LfDep> depList = new ArrayList<LfDep>();
		try
		{
			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			//企业编码
			conditionMap.put("corpCode", corpCode);
			//父及机构id
			conditionMap.put("superiorId", superiorDepId.toString());
			//机构状态
			conditionMap.put("depState", "1");
			
			orderbyMap.put("depId","asc");
			//判断企业编码不等于null
			if( null != corpCode)
			{
				depList = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
			}
			

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过企业编码和父及机构ID查出所有的下级机构异常");
			throw e;
		}
		return depList;
	}
	
	/**
	 * 通过用户ID查出在其管辖范围内的所有操作员
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusers(Long userId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			//判断用户id不能为空
			if (null != userId)
				lfSysuserList = specialDao.findDomUserBySysuserID(userId
						.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过用户ID查出在其管辖范围内的所有操作员异常");
			throw e;
		}
		return lfSysuserList;
	}
	
	/**
	 * 通过企业编码获取所有操作员
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusersByCorpCode(String corpCode) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			//判断企业编码
			if (null != corpCode)
			{
				conditionMap.put("corpCode", corpCode);
				orderbyMap.put("name","asc");
				lfSysuserList = empDao.findListByCondition(LfSysuser.class, conditionMap, orderbyMap);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过企业编码获取所有操作员异常");
			throw e;
		}
		return lfSysuserList;
	}
	
	/**
	 * 查找操作员信息
	 * @param corpCode
	 * @param superdepId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusersByCorpCode(String corpCode,Long superdepId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			//判断企业编码不能为空
			if (null != corpCode)
			{
				conditionMap.put("corpCode&=", corpCode);
				conditionMap.put("depId&=", superdepId.toString());
				//conditionMap.put("userState&<","2");
				orderbyMap.put("name","asc");
				
				lfSysuserList = empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, orderbyMap);
			}
				 

		} catch (Exception e) {
			EmpExecutionContext.error(e,"查找操作员信息异常");
			throw e;
		}
		return lfSysuserList;
	}
	
	/**
	 * 查找操作员信息，排除被删除的操作员
	 * @param corpCode
	 * @param superdepId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusersWhitoutDelByCorpCode(String corpCode,Long superdepId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>(); 
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			//判断企业编码不能为空
			if (null != corpCode)
			{
				conditionMap.put("corpCode&=", corpCode);
				conditionMap.put("depId&=", superdepId.toString());
				conditionMap.put("userState&<","2");
				orderbyMap.put("name","asc");
				
				lfSysuserList = empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, orderbyMap);
			}
				 

		} catch (Exception e) {
			EmpExecutionContext.error(e,"查找操作员信息，排除被删除的操作员异常");
			throw e;
		}
		return lfSysuserList;
	}
	
	
	/**
	 * 获取所有的通道号
	 * @return
	 * @throws Exception
	 */
	public List<XtGateQueue> getAllDistinctGates() throws Exception
	{
		//通道号集合
		List<XtGateQueue> gatesList = null;
		List<String> distinctFieldList = new ArrayList<String>();
		try
		{
			//排序集合
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			distinctFieldList.add("spgate");
			orderMap.put("spgate", StaticValue.ASC);
			//查询
			gatesList = empDao.findDistinctListBySymbolsCondition(XtGateQueue.class, distinctFieldList, null, orderMap);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取所有的通道号异常");
			throw e;
		}

		return gatesList;
	}
	/**
	 * 获取sp账号
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<Userdata> getAllUserdata(int type,String userids) throws Exception { //
		List<Userdata> userDatasList = null;
		//条件map
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		//排序map
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String,String>();
		try{
			conditionMap.put("uid&>", "100001");
			conditionMap.put("accouttype", "1");//只查询短信发送账号
			if("".equals(userids))
				conditionMap.put("userId", "-1");
			else
				
				conditionMap.put("userId&in", userids);
			
			if(type==1)
			{
				conditionMap.put("userType", "0");
			}else if(type==2)
			{
				conditionMap.put("userType", "1");
			}
			orderMap.put("userId",StaticValue.ASC);
			//调用查询方法
            userDatasList = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
		}catch(Exception e){
 
			EmpExecutionContext.error(e,"获取sp账号异常");
			//抛出异常
			throw e;
		}
		//返回查询结果
		return userDatasList;
	}
	
	/**
	 * 通过DEPID获取管辖范围内的操作员
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getDomSysuserByDepId(String depId)throws Exception
	{
		//操作员集合
		List<LfSysuser> userList = null;
		try
		{
			  userList = reportDao.findDomSysuserByDepID(depId.toString(), null);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过DEPID获取管辖范围内的操作员异常");
			throw e;
		}
		return userList;
	}
	
	/**
	 *通过操作员id获取操作员vO 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public LfSysuserVo getSysuserVoByUserId(Long userId) throws Exception {
		//判断操作员id不能为空
		if (userId == null) {
			return null;
		}
		//操作员Vo
		LfSysuserVo sysuserVo = null;
		try {
			sysuserVo =specialDao
					.findLfSysuserVoByID(userId.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过操作员id获取操作员vO 异常");
			throw e;
		}
		return sysuserVo;
	}
	
	/**
	 * 获取错误编码统计表总数
	 * @param conditionMap
	 * @return
	 * @throws Exception
	 */
//	public Long getEcReportSumCount(LinkedHashMap<String, String> conditionMap) throws Exception
//	{
//		
//		return reportSpecialDAO.getEcReportSumCount(conditionMap);
//	}
	
	/**
	 * 获取错误编码统计表信息
	 * @param spuser 发送账户
	 * @param spgate 通道号码
	 * @param imonth 月份
	 * @param sumcount 总数
	 * @param pageInfo 分页信息
	 * @return 错误编码统计表信息
	 * @throws Exception
	 */
//	public List<ErrorCodeDataReportVo> getEcReport(String spuser,String spgate,String spisuncm ,String imonth,String sumcount,PageInfo pageInfo) throws Exception
//	{
//		return reportSpecialDAO.getEcReport(spuser, spgate,spisuncm, imonth, sumcount, pageInfo);
//	}
	/**
	 * 获取对账报表信息
	 * @param spuser 发送账户
	 * @param spgate 通道号码
	 * @param imonth 月份
	 * @param pageInfo 分页信息
	 * @return 对账报表信息
	 * @throws Exception
	 */
//	public List<AccountCheckReportVo> getAccountCheckReoport(String spuser,String spgate ,String spisuncm,String imonth,PageInfo pageInfo) throws Exception
//	{
//		return reportSpecialDAO.getAccountCheckReoport(spuser, spgate,spisuncm, imonth,pageInfo);
//	}
	
	/**
	 * 获取对账报表总数信息
	 * @param conditionMap 查询条件
	 * @return 个总数信息数组
	 * @throws Exception
	 */
//	public String[] getAccountCheckSum(LinkedHashMap<String, String> conditionMap) throws Exception
//	{
//		return reportSpecialDAO.getAccountCheckSum(conditionMap);
//	}
}
