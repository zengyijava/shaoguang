package com.montnets.emp.appmage.biz;



import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.appmage.dao.app_acctManagerDao;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.appmage.LfAppAccount;
import com.montnets.emp.util.PageInfo;

public class app_acctManagerBiz extends BaseBiz
{
	/**
	 * 企业公众帐号列表
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> findAllAccountByCorpCode(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		List<DynaBean> accounts = null;
		try
		{
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			return new app_acctManagerDao().findAllAccountByCorpCode(corpCode, conditionMap, orderbyMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "公众帐号查询失败！");
		}
		return accounts;
	}
	/***
	 * 查询数据总共记录数
	 * @param corpCode 公司编码
	 * @return
	 */
	public int getRecordCount(String corpCode){
		return new app_acctManagerDao().getRecordCount(corpCode);
	}
	

	/**
	 * 更新公众帐号信息
	 * @param objectMap
	 * @param conditionMap
	 * @return1
	 */
	public boolean updateAccount(LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap)
	{
		boolean result = true;
		try
		{
			update(LfAppAccount.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			result = false;
			EmpExecutionContext.error(e, "更新公众帐号信息出现错误！");
		}
		finally
		{
		}
		return result;
	}
	
	/**
	 * 获取app企业账户对象
	 * @param lgcorpcode emp企业编码
	 * @return 返回app企业账户对象
	 */
	public LfAppAccount getAppAccount(String lgcorpcode){
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpcode", lgcorpcode);
			//再去数据库查下，防止重复添加导致问题
			List<LfAppAccount> appAccountsList = empDao.findListByCondition(LfAppAccount.class, conditionMap, null);
			if(appAccountsList != null && appAccountsList.size() > 0){
				return appAccountsList.get(0);
			}
			return null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取app企业账户对象异常。");
			return null;
		}
	}
	
}
