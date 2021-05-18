package com.montnets.emp.wyquery.biz;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.wyquery.bean.RptWyConfInfo;
import com.montnets.emp.wyquery.bean.RptWyStaticValue;
import com.montnets.emp.wyquery.dao.ReportDAO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportWyBiz extends BaseBiz {
	
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
	 * 获取机构树json
	 * @param depId 选择的机构id，为null则查全部
	 * @param curUser 当前登录操作员对象
	 * @return 返回机构树json
	 */
	public String getDepJosn(String depId, LfSysuser curUser)
	{
		if(curUser == null)
		{
			EmpExecutionContext.error("查询统计，获取机构树json，传入的当前登录操作员对象为空。depId=" + depId);
			return null;
		}
		// 权限类型。 1：个人权限 2：机构权限
		if(curUser.getPermissionType() == 1)
		{
			// 个人权限则不需要加载机构树
			return "";
		}
		try
		{
			List<LfDep> depsList;

			//没选择机构，则加载当前操作员管辖的顶级机构
			if(depId == null || depId.trim().length() < 1)
			{
				depsList = reportDao.findTopDepByUserId(curUser.getUserId().toString(), curUser.getCorpCode());
			}
			//根据机构id加载其下级机构
			else
			{
				depsList = getDepsByDepSuperId(depId, curUser.getCorpCode());
			}
			if(depsList == null || depsList.size() < 1)
			{
				EmpExecutionContext.error("查询统计，获取机构树json，获取机构集合为空。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
				return null;
			}
			
			//生成json字符串
			String depJson = createDepJson(depsList);
			return depJson;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计，获取机构树json，异常。depId="+depId + ",corpCode=" + curUser.getCorpCode());
			return null;
		}
	}
	
	/**
	 * 获取机构操作员树json
	 * @param depId 机构id
	 * @param curUser 当前登录操作员对象
	 * @return 返回机构操作员树json
	 */
	public String getDepUserJosn(String depId, LfSysuser curUser)
	{
		if(curUser == null)
		{
			EmpExecutionContext.error("查询统计，获取机构操作员树json，传入的当前登录操作员对象为空。depId=" + depId);
			return null;
		}
		// 权限类型。 1：个人权限 2：机构权限
		if(curUser.getPermissionType() == 1)
		{
			// 个人权限则不需要加载机构树
			return "";
		}
		
		try
		{
			List<LfDep> depsList;
			List<LfSysuser> sysUsersList;
			//没选择机构，则加载当前操作员管辖的顶级机构
			if(depId == null || depId.trim().length() < 1)
			{
				depsList = reportDao.findTopDepByUserId(curUser.getUserId().toString(), curUser.getCorpCode());
				sysUsersList = null;
			}
			//根据机构id加载其下级机构
			else
			{
				//获取机构下的直属机构
				depsList = getDepsByDepSuperId(depId, curUser.getCorpCode());
				//获取机构下的直属操作员
				sysUsersList = getUserInDepId(depId, curUser.getCorpCode());
			}
			//都为空则直接返回
			if((depsList == null || depsList.size() < 1) && (sysUsersList == null || sysUsersList.size() < 1))
			{
				EmpExecutionContext.error("查询统计，获取机构操作员树json，获取机构集合和操作员集合都为空。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
				return null;
			}
			
			//生成json字符串
			String depUserJson = createDepUserJosn(depsList, sysUsersList);
			return depUserJson;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计，获取机构操作员树json，异常。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
			return null;
		}
	}
	
	/**
	 * 生成机构操作员树json字符串
	 * @param depsList 机构对象集合
	 * @param sysUsersList 操作员对象集合
	 * @return 返回机构操作员树json字符串
	 */
	private String createDepUserJosn(List<LfDep> depsList, List<LfSysuser> sysUsersList)
	{
		try
		{
			LfDep lfDep = null;
			StringBuffer tree = new StringBuffer("[");
			//有机构
			if(depsList != null && depsList.size() > 0)
			{
				for (int i = 0; i < depsList.size(); i++)
				{
					lfDep = depsList.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()).append("'");
					tree.append(",isParent:").append(true);
					tree.append(",nocheck:").append(true);
					tree.append("}");
					if(i < depsList.size() - 1)
					{
						tree.append(",");
					}
				}
			}
			//操作员没记录，不需要构造json
			if(sysUsersList == null || sysUsersList.size() < 1)
			{
				tree.append("]");
				return tree.toString();
			}
			
			LfSysuser lfSysuser;
		
			//前面有机构，这里要加一个,
			if(depsList != null && depsList.size() > 0)
			{
				tree.append(",");
			}
			for (int i = 0; i < sysUsersList.size(); i++)
			{
				lfSysuser = sysUsersList.get(i);
				tree.append("{");
				tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
				tree.append(",name:'").append(lfSysuser.getName()).append("'");
				if(lfSysuser.getUserState() == 2)
				{
					tree.append(",name:'").append(lfSysuser.getName()).append("(已注销)'");
				}
				else
				{
					tree.append(",name:'").append(lfSysuser.getName()).append("'");
				}
				tree.append(",pId:").append(lfSysuser.getDepId());
				tree.append(",depId:'").append(lfSysuser.getDepId() + "'");
				tree.append(",isParent:").append(false);
				tree.append("}");
				if(i < sysUsersList.size() - 1)
				{
					tree.append(",");
				}
			}
			
			tree.append("]");
			return tree.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计，生成机构操作员树json字符串，异常。");
			return null;
		}
	}
	
	/**
	 * 获取机构下的操作员对象集合
	 * @param corpCode 企业编码
	 * @param depId 机构id
	 * @return 返回机构下的操作员对象集合
	 */
	private List<LfSysuser> getUserInDepId(String depId, String corpCode)
	{
		if(corpCode == null || corpCode.trim().length() < 1 || depId == null || depId.trim().length() < 1)
		{
			EmpExecutionContext.error("查询统计，获取机构下的操作员，传入参数为空。depId="+depId+",corpCode="+corpCode);
			return null;
		}
		try 
		{
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			conditionMap.put("depId", depId);
			conditionMap.put("corpCode", corpCode);
				
			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			orderbyMap.put("name","asc");
				
			List<LfSysuser> lfSysuserList = empDao.findListByCondition(LfSysuser.class, conditionMap, orderbyMap);
			return lfSysuserList;
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "查询统计，获取机构下的操作员，异常。");
			return null;
		}
		
	}
	
	/**
	 * 通过父机构id获取父机构下的所有子机构
	 * @param superiorId 父机构id
	 * @return 父机构下的所有子机构对象集合
	 */
	private List<LfDep> getDepsByDepSuperId(String superiorId, String corpCode)
	{
		if (superiorId == null || superiorId.trim().length() < 1 || corpCode == null || corpCode.trim().length() < 1)
		{
			EmpExecutionContext.error("查询统计，通过父机构id获取父机构下的所有子机构，父机构id或企业编码为空。superiorId="+superiorId+",corpCode="+corpCode);
			return null;
		}
		
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//父机构id
			conditionMap.put("superiorId", superiorId);
			//机构状态。1正常；2删除
			conditionMap.put("depState", "1");
			//机构所属企业
			conditionMap.put("corpCode", corpCode);
			
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			orderByMap.put("depName", StaticValue.ASC);
			
			List<LfDep> tempList = empDao.findListByCondition(LfDep.class, conditionMap, orderByMap);
			//返回结果
			return tempList;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计，通过父机构id获取父机构下的所有子机构，异常。superiorId="+superiorId+",corpCode="+corpCode);
			return null;
		}
	}
	
	/**
	 * 生成机构树json字符串
	 * @param depsList 机构对象集合
	 * @return 返回机构树json字符串
	 */
	private String createDepJson(List<LfDep> depsList)
	{
		try
		{
			LfDep lfDep;
			StringBuffer tree = new StringBuffer("[");
			for (int i = 0; i < depsList.size(); i++)
			{
				lfDep = depsList.get(i);
				tree.append("{");
				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",depId:'").append(lfDep.getDepId() + "'");
				tree.append(",isParent:").append(true);
				tree.append("}");
				if(i < depsList.size() - 1)
				{
					tree.append(",");
				}
			}
			tree.append("]");
			return tree.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计，生成机构树json字符串，异常。");
			return null;
		}
	}
	
	
	/**
	 * 通过传入原始数据计算  发送成功数  接收成功数 发送成功率 发送失败率 接收成功率 接收失败率  未返率
	 * @description    
	 * @param icount
	 * 			提交总数
	 * @param rsucc
	 * 		          接收成功数（不包含未返）
	 * @param rfail1
	 * 		          发送失败数
	 * @param rfail2
	 * 			接收失败数
	 * @param rnret
	 * 			未返数
	 * @param menuid
	 * 			报表菜单编号
	 * @return    
	 * 		返回MAP key为列的id编号	 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-4-12 下午04:04:01
	 */
	public static Map<String, String> getRptNums(long icount,long rsucc,
												 long rfail1,long rfail2,
												 long rnret,String menuid){
		//定义返回map
		Map<String, String> resultmap=new HashMap<String, String>();
		//判断除数为0可能的错误
		List<RptWyConfInfo> rptConList = RptWyConfBiz.rptConfMap.get(menuid);
		try{
			for(RptWyConfInfo rptConf : rptConList)
			{	
				//提交总数icount
				if(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID.equals(rptConf.getColId())){
					resultmap.put(RptWyStaticValue.RPT_ICOUNT_COLUMN_ID, icount+"");
				}else if(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
				{//发送成功数
					//icount-rfail1
					resultmap.put(RptWyStaticValue.RPT_FSSUCC_COLUMN_ID, (icount-rfail1)+"");
				}else if(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID.equals(rptConf.getColId()))
				{//发送失败数
					//rfail1
					resultmap.put(RptWyStaticValue.RPT_RFAIL1_COLUMN_ID, rfail1+"");
				}else if(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID.equals(rptConf.getColId()))
				{//接收失败数
					//rfail2
					resultmap.put(RptWyStaticValue.RPT_RFAIL2_COLUMN_ID, rfail2+"");
				}				
				else if(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID.equals(rptConf.getColId()))
				{
					//接收成功数  type 1 succ  2 succ+rnret
					long jssucc=0;
					if(rptConf.getType()!=null&&"1".equals(rptConf.getType())){
						jssucc=rsucc;
					}else if(rptConf.getType()!=null&&"2".equals(rptConf.getType())){
						jssucc=rsucc+rnret;
					}
					resultmap.put(RptWyStaticValue.RPT_JSSUCC_COLUMN_ID, jssucc+"");
				}else if(RptWyStaticValue.RPT_RNRET_COLUMN_ID.equals(rptConf.getColId()))
				{//未返数
				 //rnret
				 resultmap.put(RptWyStaticValue.RPT_RNRET_COLUMN_ID, rnret+"");
				}	
				else if(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
				{
					//发送成功率  保留2为小数 ((icount-rfail1)*100f)/icount %
					if(icount!=0){
						String propertion=new DecimalFormat("#0.00")
						.format(((icount-rfail1)*100f)/icount)+"%";
						resultmap.put(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID,propertion );
					}else{
						resultmap.put(RptWyStaticValue.RPT_FSSUCC_ICOUNT_COLUMN_ID,"0.00%" );
					}
					
				}else if(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID.equals(rptConf.getColId()))
				{
					//发送失败率  保留2为小数 (rfail1)*100f/icount %
					if(icount!=0){
						String propertion=new DecimalFormat("#0.00")
						.format((rfail1*100f)/icount)+"%";
						resultmap.put(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID,propertion );
					}else{
						resultmap.put(RptWyStaticValue.RPT_RFAIL1_ICOUNT_COLUMN_ID,"0.00%" );
					}
				}else if(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
				{
					//接收成功率  保留2为小数 (jssucc*100f)/(icount-rfail1) %
					long jssucc=0;
					if(rptConf.getType()!=null&&"1".equals(rptConf.getType())){
						jssucc=rsucc;
					}else if(rptConf.getType()!=null&&"2".equals(rptConf.getType())){
						jssucc=rsucc+rnret;
					}
					if((icount-rfail1)!=0){
						String propertion=new DecimalFormat("#0.00")
						.format((jssucc*100f)/(icount-rfail1))+"%";
						resultmap.put(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID,propertion );
					}else{
						resultmap.put(RptWyStaticValue.RPT_JSSUCC_FSSUCC_COLUMN_ID,"0.00%" );
					}
				}else if(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
				{
					//接收失败率  保留2为小数 (rfail2*100f)/(icount-rfail1) %
					if((icount-rfail1)!=0){
						String propertion=new DecimalFormat("#0.00")
						.format((rfail2*100f)/(icount-rfail1))+"%";
						resultmap.put(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID,propertion );
					}else{
						resultmap.put(RptWyStaticValue.RPT_RFAIL2_FSSUCC_COLUMN_ID,"0.00%" );
					}
				}else if(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID.equals(rptConf.getColId()))
				{
					//未返率  保留2为小数 (rnret*100f)/(icount-rfail1) %
					if((icount-rfail1)!=0){
						String propertion=new DecimalFormat("#0.00")
						.format((rnret*100f)/(icount-rfail1))+"%";
						resultmap.put(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID,propertion);
					}else{
						resultmap.put(RptWyStaticValue.RPT_RNRET_FSSUCC_COLUMN_ID,"0.00%");
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"报表计算结果异常");
		}
		return resultmap;
	}
	
	
	
	/**
	 * 根据操作员id获取操作员所有管辖机构的顶级机构
	 * @param sysuserID 操作员id
	 * @param corpCode 操作员所属企业
	 * @return 返回操作员所有管辖机构的顶级机构
	 */
	/*private List<LfDep> getTopDepByUserId(String sysuserID, String corpCode)
	{
		if (sysuserID == null || sysuserID.trim().length() < 1 || corpCode == null || corpCode.trim().length() < 1)
		{
			EmpExecutionContext.error("查询统计，根据操作员id获取管辖机构的顶级机构对象，操作员id或企业编码为空。sysuserID="+sysuserID+",corpCode="+corpCode);
			return null;
		}
		
		try
		{
			List<LfDep> depsList = reportDao.findTopDepByUserId(sysuserID, corpCode);
			
			if(depsList == null || depsList.size() < 1)
			{
				return null;
			}
			
			List<LfDep> tempDepsList = new ArrayList<LfDep>();
			tempDepsList.add(depsList.get(0));
			return tempDepsList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计，根据操作员id获取管辖机构的顶级机构对象，异常。sysuserID="+sysuserID+",corpCode="+corpCode);
			return null;
		}
	}*/
	
}
