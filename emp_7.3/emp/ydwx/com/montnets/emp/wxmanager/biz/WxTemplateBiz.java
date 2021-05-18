package com.montnets.emp.wxmanager.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxmanger.dao.WxTemplateDao;
public class WxTemplateBiz extends SuperBiz{
	
	/***
	 * 查询网讯模板信息
	 */
	public List<DynaBean> getTemplates(LfWXBASEINFO baseInfo,String corpcode,PageInfo pageInfo) {
		return new WxTemplateDao().getTemplates(baseInfo,corpcode, pageInfo);
	}
	
	/***
	 *通过ID删除模板
	 *@return 返回是否成功删除 
	 */
	public boolean deleteTemplate(String id){
		boolean result = false;
		Connection conn = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("NETID", id);
		try {
			
			conn =   empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			empTransDao.delete(conn, LfWXBASEINFO.class, id);
			empTransDao.delete(conn, LfWXPAGE.class, conditionMap);
			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"删除模板");
			empTransDao.rollBackTransaction(conn);
		}finally{
			empTransDao.closeConnection(conn);
		}
		
		return result;
	}
}
