package com.montnets.emp.shorturl.surlmanage.biz;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;

import com.montnets.emp.shorturl.surlmanage.dao.UrlOperUrlAuditDao;
import com.montnets.emp.shorturl.surlmanage.vo.LfOperNeturlVo;
import com.montnets.emp.util.PageInfo;

public class UrlOperUrlAuditBiz extends SuperBiz{

	/**
	 * 获取审核人名
	 * @return
	 */
	public Map<String, String> getAlluser() {
		
		return new UrlOperUrlAuditDao().getAllAuditer();
	}
	
	/**
	 * 获取连接地址名称下拉框
	 * @return
	 */
	public List<String> getUrllist() {
		
		return new UrlOperUrlAuditDao().getAllUrlName();
	}

	/**
	 * 查询链接
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<LfOperNeturlVo> findUrllist(
			LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
		List<LfOperNeturlVo> urLists = null;
		try {
			if (pageInfo != null) {
				//按条件查询，分页
				urLists = new UrlOperUrlAuditDao().findurlLists(conditionMap, pageInfo);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"运营商审核链接查询url地址集合biz异常！");
		}
		return urLists;
	}
	
	/**
	 * 审核提交
	 * @param id
	 * @param remarks
	 * @param ispass
	 * @param urserid
	 * @return
	 */
	public boolean update(String id, String remarks, String ispass,String urserid) {
		
		return new UrlOperUrlAuditDao().update(id,remarks,ispass,urserid);
	}
	
	/**
	 * 审核提交
	 * @param id
	 * @param remarks
	 * @param ispass
	 * @param urserid
	 * @return
	 */
	public boolean stopUrl(String id, String remarks, String ispass,String urserid) {
		
		return new UrlOperUrlAuditDao().stop(id,remarks,ispass,urserid);
	}
	
	
}
