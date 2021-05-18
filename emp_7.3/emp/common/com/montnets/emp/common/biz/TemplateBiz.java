/**
 * Program  : TemplateBiz.java
 * Author   : zousy
 * Create   : 2013-11-29 上午11:41:43
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.common.biz;

import java.util.List;

import com.montnets.emp.common.dao.TemplateDAO;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author   zousy
 * @version  1.0.0
 * @2013-11-29 上午11:41:43
 */
public class TemplateBiz
{
	private TemplateDAO templateDao = new TemplateDAO();
	/**
	 * @description 通用模板查询方法    
	 * @param template 查询条件封装
	 * @param templType 1 短信  2 为彩信
	 * @param userId  操作员Id
	 * @param pageInfo 
	 * @param dsflag 模板类型
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-11-29 下午03:12:11
	 */
	public List<LfTemplate> getTemplateByCondition(LfTemplate template,int templType,Long userId,String dsflag,PageInfo pageInfo){
		return templateDao.findLfTemplateVoList(userId, template, templType,dsflag, pageInfo);
	}
}

