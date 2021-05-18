package com.montnets.emp.template.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.template.vo.LfTemplateVo;
import com.montnets.emp.util.PageInfo;

/**
 * @project sinolife
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-6-30 下午02:57:55
 * @description
 */
//彩信素材处理DAO
public class TemplateDAO extends SuperDAO
{
	/**
	 *   查询所有的彩信素材列表VO
	 * @param loginUserId	操作员用户ID
	 * @param lfTemplateVo	素材VO
	 * @param pageInfo	分页信息
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> findLfTemplateVoList(Long loginUserId,
			LfTemplateVo lfTemplateVo, PageInfo pageInfo) throws Exception
	{
		//获取查询SQL
		String fieldSql = TemplateSQL.getFieldSql();
		//获取表SQL
		String tableSql = TemplateSQL.getTableSql();
		//获取whereSQL
		String whereSql = TemplateSQL.getWhereSql();
		//获取审核的SQL
		String dominationSql = TemplateSQL
				.getDominationSql(loginUserId);
		//获取共享的SQL
		String shareSql = TemplateSQL
		.getShareSql(loginUserId);
		//获取查询SQL
		String conditionSql = TemplateSQL
				.getConditionSql(lfTemplateVo);
		//获取groupSQL
		String groupSql = TemplateSQL
		.getGroupBySql();
		//获取排序SQL
		String orderBySql = TemplateSQL.getOrderBySql();
		String innerSql=new StringBuffer("select lfalltemplate.ID,lftemplate.TM_ID")
		.append(tableSql).append(dominationSql).append(shareSql).append(")").append(conditionSql)
		.toString();
		String outerSql= new StringBuffer("select MIN(temp.ID) tm_id from (").
		append(innerSql).append(")").append(groupSql).toString();
		//拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).
		append(whereSql).append("(")
		.append(outerSql)
		.append(
				")").append(orderBySql)
				.toString();
		
		//获取总计
		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append(outerSql)
				.append(
						") mytemp")
				.toString();
		//查询数据
		List<LfTemplateVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfTemplateVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//返回
		return returnList;
	}
	/**
	 *  获取所有彩信素材列表
	 * @param loginUserId	操作员用户ID
	 * @param lfTemplateVo	素材VO
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> findLfTemplateVoList(Long loginUserId,
			LfTemplateVo lfTemplateVo) throws Exception
	{
		//获取查询SQL
		String fieldSql = TemplateSQL.getFieldSql();
		//获取表SQL
		String tableSql = TemplateSQL.getTableSql();
		//获取whereSQL
		String whereSql = TemplateSQL.getWhereSql();
		//获取审核的SQL
		String dominationSql = TemplateSQL
				.getDominationSql(loginUserId);
		//获取共享的SQL
		String shareSql = TemplateSQL
		.getShareSql(loginUserId);
		//获取查询SQL
		String conditionSql = TemplateSQL
				.getConditionSql(lfTemplateVo);
		//获取groupSQL
		String groupSql = TemplateSQL
		.getGroupBySql();
		//获取排序SQL
		String orderBySql = TemplateSQL.getOrderBySql();
		String innerSql=new StringBuffer("select lfalltemplate.ID,lftemplate.TM_ID")
		.append(tableSql).append(dominationSql).append(shareSql).append(conditionSql)
		.toString();
		String outerSql= new StringBuffer("select MIN(temp.ID) tm_id from (").
		append(innerSql).append(")").append(groupSql).toString();
		//拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).
		append(whereSql).append("(")
		.append(outerSql)
		.append(
				")").append(orderBySql)
				.toString();
		
		//查询数据
		List<LfTemplateVo> returnVoList = findVoListBySQL(LfTemplateVo.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}

}
