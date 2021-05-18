package com.montnets.emp.qyll.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.qyll.dao.OperatorReportDao;
import com.montnets.emp.qyll.vo.LlOperatorReportVo;
import com.montnets.emp.util.PageInfo;
/**
 * 操作员统计报表
 * @project p_qyll
 * @author pengyc
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2017-10-23 下午03:01:14
 * @description
 */

public class OperatorReportBiz extends BaseBiz{
	// 套餐报表DAO
	protected OperatorReportDao operatorReportDao;
	//构造函数
	public OperatorReportBiz(){
		operatorReportDao = new OperatorReportDao();	
	}
	/**
	 * 根据条件获取操作员统计报表信息
	 * 
	 * @param llOperatorReport
	 *        查询信息Bean
	 * @param pageInfo
	 *        分页对象
	 * @return
	 * @throws Exception
	 */
	public List<LlOperatorReportVo> getLlOperatorReportList(
			LlOperatorReportVo llOperatorReport, PageInfo pageInfo) throws Exception{
		
		List<LlOperatorReportVo> returnList = null;
		try {
			returnList = operatorReportDao.getllOperatorReportSql(llOperatorReport,pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过操作员对象获取操作员报表数据失败");
			throw e;
		}
		//返回数据集合
		return returnList;
	}
	/**
	 * 获取表单合计数据
	 * 
	 * @param llOrderReport
	 *        查询信息Bean
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCount(LlOperatorReportVo llOperatorReport)
			throws Exception {
		return operatorReportDao.findSumCount(llOperatorReport);
	}
	/**
	 * 获取机构操作员树json
	 * @param depId 机构id
	 * @param curUser 当前登录操作员对象
	 * @return 返回机构操作员树json
	 */
	public String getDepUserJosn(String depId, LfSysuser curUser,String replaceStr)
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
				depsList = operatorReportDao.findTopDepByUserId(curUser.getUserId().toString(), curUser.getCorpCode());
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
			String depUserJson = createDepUserJosn(depsList, sysUsersList,replaceStr);
			return depUserJson;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计，获取机构操作员树json，异常。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
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
	 * 生成机构操作员树json字符串
	 * @param depsList 机构对象集合
	 * @param sysUsersList 操作员对象集合
	 * @return 返回机构操作员树json字符串
	 */
	private String createDepUserJosn(List<LfDep> depsList, List<LfSysuser> sysUsersList,String replaceStr)
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
					tree.append(",name:'").append(lfSysuser.getName()).append("("+replaceStr+")'");
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
				depsList = operatorReportDao.findTopDepByUserId(curUser.getUserId().toString(), curUser.getCorpCode());
			}
			//根据机构id加载其下级机构
			else
			{
				depsList = getDepsByDepSuperId(depId, curUser.getCorpCode());
			}
			if(depsList == null || depsList.size() < 1)
			{
				EmpExecutionContext.info("查询统计，获取下级机构树JSON，该机构已无下级机构。depId=" + depId + ",corpCode=" + curUser.getCorpCode());
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
}
