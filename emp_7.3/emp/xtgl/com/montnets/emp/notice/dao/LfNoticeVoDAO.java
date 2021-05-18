package com.montnets.emp.notice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.notice.vo.LfNoticeVo;
import com.montnets.emp.table.notice.TableLfNotice;
import com.montnets.emp.util.PageInfo;

/**
 * 通知公告
 * @project montnets_dao
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-9-27 上午11:37:20
 * @description
 */
public class LfNoticeVoDAO extends SuperDAO
{

	/**
	 * 获取更多通知公告的方法
	 * @param pageInfo
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<LfNoticeVo> findLfNoticeVos(PageInfo pageInfo,String corpCode) throws Exception
	{
		//获取查询列
		String fieldSql = LfNoticeVoSQL.getFieldSql();
		//获取查询表
		String tableSql = LfNoticeVoSQL.getTableSql();
		//排序
		String orderBySql = LfNoticeVoSQL.getOrderBySql();
		//组装过滤条件
		LfNoticeVo v=new LfNoticeVo();
		v.setCorpCode(corpCode);
		String conditionsql=LfNoticeVoSQL.getConditionSql(v);
		conditionsql = conditionsql.replaceFirst("^(\\s*)(?i)and", "$1where");
		//组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionsql).append(
				orderBySql).toString();
		//组装统计语句
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionsql).toString();
		//调用查询方法
		List<LfNoticeVo> returnVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfNoticeVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnVoList;
	}
	
	public List<LfNoticeVo> findLfNoticeVos(LfNoticeVo lfNoticeVo, PageInfo pageInfo) throws Exception
	{
		String fieldSql = LfNoticeVoSQL.getFieldSql();
		String tableSql = LfNoticeVoSQL.getTableSql();
		String conditionSql = LfNoticeVoSQL.getConditionSql(lfNoticeVo);
		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		String orderBySql = LfNoticeVoSQL.getOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).append(
				orderBySql).toString();
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql).toString();
		List<LfNoticeVo> returnVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfNoticeVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnVoList;
	}

	/**
	 * 查询公告信息
	 * @param size  查询条数
	 * @param corpCode  企业编码
	 * @return
	 * @throws Exception
	 */
	public List<LfNoticeVo> findLatestLfNoticeVos(int size,String corpCode) throws Exception
	{
		//查询列
		String fieldSql = LfNoticeVoSQL.getFieldSql();
		//查询表名
		String tableSql = LfNoticeVoSQL.getTableSql();
		//排序
		String orderBySql = LfNoticeVoSQL.getOrderBySql();
		//组装过滤条件
		LfNoticeVo lfNoticeVo=new LfNoticeVo();
		lfNoticeVo.setCorpCode(corpCode);
		String conditionSql=LfNoticeVoSQL.getConditionSql(lfNoticeVo);
		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		//组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).append(
				orderBySql).toString();
		//分页处理
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageIndex(1);
		pageInfo.setPageSize(size);
		//查询方法
		List<LfNoticeVo> returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						LfNoticeVo.class, sql, pageInfo,
						StaticValue.EMP_POOLNAME, null);
		return returnVoList;
	}

	public List<LfNoticeVo> findLatestLfNoticeVos(int size, LfNoticeVo lfNoticeVo) throws Exception
	{
		String fieldSql = LfNoticeVoSQL.getFieldSql();
		String tableSql = LfNoticeVoSQL.getTableSql();
		String conditionSql = LfNoticeVoSQL.getConditionSql(lfNoticeVo);
		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		String orderBySql = LfNoticeVoSQL.getOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).append(
				orderBySql).toString();
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageIndex(1);
		pageInfo.setPageSize(size);
		List<LfNoticeVo> returnVoList =new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						LfNoticeVo.class, sql, pageInfo,
						StaticValue.EMP_POOLNAME,null);
		return returnVoList;
	}
	/**
	 * 查询单个通知公告
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public LfNoticeVo getNoticeVo(Long id) throws Exception{
		LfNoticeVo returnObj = null;
		//获取对象列
		Map<String, String> columns = getVoORMMap(LfNoticeVo.class);
		//查询列
		String fieldSql = LfNoticeVoSQL.getFieldSql();
		//查询表名
		String tableSql = LfNoticeVoSQL.getTableSql();
		//过滤条件
		String conditinSql = new StringBuffer(" where lfnotice.").append(TableLfNotice.NOTICE_ID).append("=").append(id).toString();
		//组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditinSql).toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			List<LfNoticeVo> returnList = rsToList(rs, LfNoticeVo.class, columns);
			if (returnList != null && returnList.size() > 0)
			{
				returnObj = returnList.get(0);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询单个通知公告异常。");
			//异常处理
			throw e;
		} finally
		{
			//关闭连接
			super.close(rs, ps, conn);
		}
		return returnObj;
	}
	
}
