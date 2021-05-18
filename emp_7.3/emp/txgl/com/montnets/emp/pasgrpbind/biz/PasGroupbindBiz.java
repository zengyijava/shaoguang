package com.montnets.emp.pasgrpbind.biz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.pasgrpbind.dao.PasgrpbindDao;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.util.PageInfo;
/**
 * SP账户绑定
 * @author Administrator
 *
 */
public class PasGroupbindBiz extends SuperBiz{
	
	ErrorLoger errorLoger = new ErrorLoger();
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
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"根据当前的企业编码查询出这个企业下的所有机构异常！"));
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
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通过企业编码和父及机构ID查出所有的下级机构异常！"));
			throw e;
		}
		return depList;
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
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找操作员信息排除被删除的操作员异常！"));
		}
		return lfSysuserList;
	}
	
	
	
	
	/**
	 * 获取企业账户绑定表中发送账号为激活的数据
	 * @param conditionMap 传入查询条件
	 * @param orderbyMap  传入排序条件
	 * @param pageInfo	分页信息
	 * @return 企业账户绑定关系表的集合List<LfSpDepBind>
	 * @throws Exception
	 */
	public List<LfSpDepBind> getSpDepBindWhichUserdataIsOk(LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {
		
		List<LfSpDepBind> xx = null;
		try
		{
			xx = new PasgrpbindDao().getSpDepBindWhichUserdataIsOk( conditionMap, orderbyMap,
					pageInfo);
		} catch (Exception e)
		{
			// TODO: handle exception
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"获取企业账户绑定表中发送账号为激活的数据异常！"));
			throw new Exception();
		}
		return xx;
	}
	

}
