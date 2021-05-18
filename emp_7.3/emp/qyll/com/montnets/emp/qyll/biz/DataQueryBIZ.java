package com.montnets.emp.qyll.biz;

import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.dao.DataQueryDAO;
import com.montnets.emp.qyll.vo.OrderDetailVO;
import com.montnets.emp.qyll.vo.OrderTaskVO;
import com.montnets.emp.qyll.vo.ProductVO;
import com.montnets.emp.util.PageInfo;

/**
 * @author Jason Huang
 * @date 2017年10月30日 下午4:45:24
 */

public class DataQueryBIZ  {
	private DataQueryDAO dataQueryDAO;

	public DataQueryBIZ() {
		dataQueryDAO = new DataQueryDAO();
	}

	public List<OrderDetailVO> getOrderDetailList(OrderDetailVO orderDetail, PageInfo pageInfo) {
		List<OrderDetailVO> resultList = null;
		try {
			resultList = dataQueryDAO.getOrderDetailList(orderDetail, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据条件查询套餐订购详情列表异常。");
		}
		return resultList;
	}

	public List<OrderDetailVO> getOrderDetailList(OrderDetailVO orderDetail, int orderType, PageInfo pageInfo) {
		List<OrderDetailVO> resultList = null;
		try {
			resultList = dataQueryDAO.getOrderDetailList(orderDetail, orderType, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据条件查询套餐订购详情列表异常。");
		}
		return resultList;
	}

	public List<OrderTaskVO> getOrderTaskList(OrderTaskVO orderTask, PageInfo pageInfo) throws Exception {
		List<OrderTaskVO> resultList = null;
		try {
			resultList = dataQueryDAO.getOrderTaskList(orderTask, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据条件查询套餐订购任务列表异常。");
		}
		return resultList;
	}

	public List<ProductVO> getProductList(String ids, String orderNo) throws Exception {
		return dataQueryDAO.getProductList(ids, orderNo);
	}
	
	public List<ProductVO> getProductList(String ecid) throws Exception {
		return dataQueryDAO.getProductList(ecid);
	}

	public boolean updateTaskById(String id) {
		return dataQueryDAO.updateTaskById(id);
	}

	public boolean updateReSendById(int id) {
		return dataQueryDAO.updateReSendById(id);
	}

}
