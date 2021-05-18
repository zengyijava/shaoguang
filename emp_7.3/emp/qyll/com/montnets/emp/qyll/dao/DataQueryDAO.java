package com.montnets.emp.qyll.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.qyll.dao.sql.DataQuerySQL;
import com.montnets.emp.qyll.dao.sql.DataQuerySQLFactory;
import com.montnets.emp.qyll.vo.OrderDetailVO;
import com.montnets.emp.qyll.vo.OrderTaskVO;
import com.montnets.emp.qyll.vo.ProductVO;
import com.montnets.emp.util.PageInfo;

/**
 * @author Jason Huang
 * @date 2017年10月30日 上午10:13:14
 */

public class DataQueryDAO extends SuperDAO {
	// 通过SQL工厂生产对应数据库的SQL实例
	DataQuerySQL dataQuerySQL = new DataQuerySQLFactory().buildSQL(StaticValue.DBTYPE);

	public List<OrderDetailVO> getOrderDetailList(OrderDetailVO orderDetail, PageInfo pageInfo) throws Exception {
		// 查询字段
		String fieldSQL = dataQuerySQL.getOrderDetailFiledSQL();
		// 查询表名
		String tableSQL;
		if (orderDetail.getEcid() != null && orderDetail.getEcid().length() > 0) {
			tableSQL = dataQuerySQL.getOrderDetailTableSQL(orderDetail.getEcid());
		} else {
			tableSQL = dataQuerySQL.getOrderDetailTableSQL();
		}
		// 查询条件
		String conditionSQL = dataQuerySQL.getOrderDetailConditionSQL(orderDetail);
		// 排序条件
		String orderBySQL = dataQuerySQL.getOrderDetailOrderBySQL();
		// 记录总数
		String countSQL = new StringBuffer("SELECT	COUNT(*) totalcount").append(tableSQL).append(conditionSQL)
				.toString();
		// 查询语句
		String sql = new StringBuffer(fieldSQL).append(tableSQL).append(conditionSQL).append(orderBySQL).toString();
		// System.out.println("DataQueryDAO.getOrderDetailList() sql = " + sql);
		// 调用查询语句
		List<OrderDetailVO> resultList;
		if (pageInfo == null) {
			resultList = findVoListBySQL(OrderDetailVO.class, sql, StaticValue.EMP_POOLNAME);
		} else {
			resultList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(OrderDetailVO.class, sql, countSQL,
					pageInfo, StaticValue.EMP_POOLNAME);
		}
		if (resultList == null) {
			EmpExecutionContext.error("套餐订购详情查询异常！SQL：" + sql);
		}
		return resultList;
	}

	public List<OrderTaskVO> getOrderTaskList(OrderTaskVO orderTask, PageInfo pageInfo) throws Exception {
		// 查询字段
		String fieldSQL = dataQuerySQL.getOrderTaskFiledSQL();
		// 查询表名
		String tableSQL = dataQuerySQL.getOrderTaskTableSQL();
		// 查询条件
		String conditionSQL = dataQuerySQL.getOrderTaskConditionSQL(orderTask);
		// 排序条件
		String orderBySQL = dataQuerySQL.getOrderTaskOrderBySQL();
		// 记录总数
		String countSQL = new StringBuffer("SELECT	COUNT(*) totalcount").append(tableSQL).append(conditionSQL)
				.toString();
		// 查询语句
		String sql = new StringBuffer(fieldSQL).append(tableSQL).append(conditionSQL).append(orderBySQL).toString();
		// System.out.println("DataQueryDAO.getOrderTaskList() sql = " + sql);
		// 调用查询语句
		List<OrderTaskVO> resultList;
		if (pageInfo == null) {
			resultList = findVoListBySQL(OrderTaskVO.class, sql, StaticValue.EMP_POOLNAME);
		} else {
			resultList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(OrderTaskVO.class, sql, countSQL,
					pageInfo, StaticValue.EMP_POOLNAME);
		}
		if (resultList == null) {
			EmpExecutionContext.error("套餐订购任务查询异常！SQL：" + sql);
		}
		return resultList;
	}

	public List<ProductVO> getProductList(String ecid) throws Exception {
		// 查询语句
		String sql = "SELECT PRODUCTID,PRODUCTNAME,ISP,1 AS COUNT FROM LL_PRODUCT WHERE ECID = "  + ecid + " AND STATUS = 1 ORDER BY PRODUCTNAME DESC";
		// System.out.println("DataQueryDAO.getProductList() sql = " + sql);
		// 调用查询语句
		List<ProductVO> resultList = findVoListBySQL(ProductVO.class, sql, StaticValue.EMP_POOLNAME);
		if (resultList == null) {
			EmpExecutionContext.error("根据ECID查询套餐产品异常！SQL：" + sql);
		}
		return resultList;
	}
	
	public List<ProductVO> getProductList(String ids, String orderNo) throws Exception {
		// 查询字段
		String fieldSQL = dataQuerySQL.getProductFiledSQL();
		// 查询表名
		String tableSQL = dataQuerySQL.getProductTableSQL();
		// 查询条件
		String conditionSQL = dataQuerySQL.getProductConditionSQL(ids, orderNo);
		// 查询语句
		String sql = new StringBuffer(fieldSQL).append(tableSQL).append(conditionSQL).toString();
		// System.out.println("DataQueryDAO.getProductList() sql = " + sql);
		// 调用查询语句
		List<ProductVO> resultList = findVoListBySQL(ProductVO.class, sql, StaticValue.EMP_POOLNAME);
		if (resultList == null) {
			EmpExecutionContext.error("根据ID查询套餐产品异常！SQL：" + sql);
		}
		return resultList;
	}

	public boolean updateTaskById(String id) {
		String sql = "UPDATE LL_ORDER_TASK SET ORDERSTATUS = '3',SMSSTATUS = '3' WHERE ID = '" + id + "'";
		try {
			return executeBySQL(sql, StaticValue.EMP_POOLNAME);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "DAO层-Task数据更新失败");
			return false;
		}
	}

	public boolean updateReSendById(int id) {
		String sql = "UPDATE LL_ORDER_TASK SET ISRETRY = '1',ORDERSTATUS = '1',UPDATETM = CURRENT_TIMESTAMP WHERE ID = "
				+ id;
		try {
			return executeBySQL(sql, StaticValue.EMP_POOLNAME);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "DAO层-Task数据更新失败");
			return false;
		}
	}

	public List<OrderDetailVO> getOrderDetailList(OrderDetailVO orderDetail, int orderType, PageInfo pageInfo)
			throws Exception {
		// 查询字段
		String fieldSQL = dataQuerySQL.getOrderDetailFiledSQL();
		// 查询表名
		String tableSQL;
		if (orderDetail.getEcid() != null && orderDetail.getEcid().length() > 0) {
			tableSQL = dataQuerySQL.getOrderDetailTableSQL(orderDetail.getEcid());
		} else {
			tableSQL = dataQuerySQL.getOrderDetailTableSQL();
		}
		// 查询条件
		String conditionSQL = dataQuerySQL.getOrderDetailConditionSQL(orderDetail);
		// 排序条件
		String orderBySQL;
		if (orderType == 2) {
			orderBySQL = dataQuerySQL.getOrderDetailOrderByMobileSQL();
		} else {
			orderBySQL = dataQuerySQL.getOrderDetailOrderBySQL();
		}
		// 记录总数
		String countSQL = new StringBuffer("SELECT	COUNT(*) totalcount").append(tableSQL).append(conditionSQL)
				.toString();
		// 查询语句
		String sql = new StringBuffer(fieldSQL).append(tableSQL).append(conditionSQL).append(orderBySQL).toString();
		// System.out.println("DataQueryDAO.getOrderDetailList() sql = " + sql);
		// 调用查询语句
		List<OrderDetailVO> resultList;
		if (pageInfo == null) {
			resultList = findVoListBySQL(OrderDetailVO.class, sql, StaticValue.EMP_POOLNAME);
		} else {
			resultList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(OrderDetailVO.class, sql, countSQL,
					pageInfo, StaticValue.EMP_POOLNAME);
		}
		if (resultList == null) {
			EmpExecutionContext.error("套餐订购详情查询异常！SQL：" + sql);
		}
		return resultList;
	}
}
