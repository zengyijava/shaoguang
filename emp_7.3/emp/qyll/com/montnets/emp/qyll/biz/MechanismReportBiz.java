package com.montnets.emp.qyll.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.qyll.dao.MechanismReportDao;
import com.montnets.emp.qyll.vo.LlMechanismReportVo;
import com.montnets.emp.util.PageInfo;

public class MechanismReportBiz extends BaseBiz{
	protected MechanismReportDao mechanismReportDao;
	//构造函数
	public MechanismReportBiz(){
		mechanismReportDao = new MechanismReportDao();	
	}

	public List<LlMechanismReportVo> getLlMechanismReportList(
			LlMechanismReportVo llMechanismReport, PageInfo pageInfo)
			throws Exception {
		List<LlMechanismReportVo> returnList = null;
		try {
			returnList = mechanismReportDao.getllMechanismReportSql(llMechanismReport,pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过机构对象获取机构报表数据失败");
			throw e;
		}
		//返回数据集合
		return returnList;
	}

	public long[] findSumCount(LlMechanismReportVo llMechanismReport)
			throws Exception {
		return mechanismReportDao.findSumCount(llMechanismReport);
	}

	public String getDepJosn(String depId, LfSysuser curUser) {
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
				depsList = mechanismReportDao.findTopDepByUserId(curUser.getUserId().toString(), curUser.getCorpCode());
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
}
