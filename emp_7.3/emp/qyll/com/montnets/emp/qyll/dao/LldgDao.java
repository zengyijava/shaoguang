package com.montnets.emp.qyll.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.qyll.dao.sql.FlowOrderDB2Sql;
import com.montnets.emp.qyll.dao.sql.FlowOrderMysqlSql;
import com.montnets.emp.qyll.dao.sql.FlowOrderOracleSql;
import com.montnets.emp.qyll.dao.sql.FlowOrderSqlserverSql;
import com.montnets.emp.qyll.entity.LLOrderTask;
import com.montnets.emp.qyll.entity.LlOrderDetail;
import com.montnets.emp.qyll.entity.LlProduct;
import com.montnets.emp.qyll.utils.StringUtil;
import com.montnets.emp.util.Logger;
import com.montnets.emp.util.StringUtils;

public class LldgDao extends SuperDAO  {
	
	private SimpleDateFormat sdf_date = new SimpleDateFormat("yyyyMMdd");
	
	protected static IFlowOrderSql flowOrderSql = null;
	
	static{
		//根据不同的数据库类型，实例化不同的sql类 1 oracle ; 2 SQL Server2005 ; 3 MySQL ; 4 DB2    
		switch (StaticValue.DBTYPE) {
		case 1:
			flowOrderSql = new FlowOrderOracleSql();
			break;
		case 2:
			flowOrderSql = new FlowOrderSqlserverSql();
			break;
		case 3:
			flowOrderSql = new FlowOrderMysqlSql();
			break;
		case 4:
			flowOrderSql = new FlowOrderDB2Sql();
			break;
		default:
			EmpExecutionContext.error("实例化IFlowOrderSql失败:未知的数据库类型,DBTYPE["+StaticValue.DBTYPE+"]");
			break;
		}
	}
	
	public List<LlProduct> findLlProductList(String sql) {
		List<LlProduct> list = new ArrayList<LlProduct>();
		if(sql == null || "".equals(sql)){
			 return list;
		}
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				LlProduct product = new LlProduct();
				product.setId(rs.getInt("id"));
				product.setEcid(rs.getInt("ecid"));
				product.setProductid(rs.getString("PRODUCTID"));
				product.setProductname(rs.getString("productname"));
				product.setIsp(rs.getString("isp"));
				product.setVolume(rs.getString("volume"));
				product.setPrice(rs.getDouble("price"));
				product.setDiscprice(rs.getDouble("discprice"));
				product.setArea(rs.getString("discprice"));
				product.setPtype(rs.getInt("ptype"));
				product.setPmold(rs.getInt("pmold"));
				product.setRtype(rs.getInt("rtype"));
				product.setStatus(rs.getInt("status"));
				product.setUpdatetm(rs.getTimestamp("updatetm"));
				product.setCreatetm(rs.getTimestamp("createtm"));
				product.setOperatorid(rs.getInt("operatorid"));
				list.add(product);
			}
			return list;
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "查询流量套餐失败",Logger.LEVEL_ERROR );
			return list;
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "查询流量套餐:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
	}

	public boolean insertLlTask(LLOrderTask orderTask) {
		String sql_ = flowOrderSql.getInsertLlTaskSql();
		boolean boolUpdate = false;
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//当前时间
			Date date = new Date();
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			conn.setAutoCommit(true);
			ps = conn.prepareStatement(sql_);
			ps.setInt(1, orderTask.getTaskid());
			ps.setInt(2, orderTask.getEcid());
			ps.setInt(3, orderTask.getUser_id());
			ps.setLong(4, orderTask.getOrg_id());
			ps.setString(5, orderTask.getTopic() == null ? " " : orderTask.getTopic());
			ps.setString(6, orderTask.getPro_ids() == null ? " " : orderTask.getPro_ids());
			ps.setString(7, orderTask.getMsgtype() == null ? " " : orderTask.getMsgtype());
			ps.setInt(8, orderTask.getTemp_id());
			ps.setString(9, orderTask.getSp_user() == null ? " " : orderTask.getSp_user());
			ps.setString(10, orderTask.getSp_pwd() == null ? " " : orderTask.getSp_pwd());
			ps.setInt(11, orderTask.getSubcount());
			ps.setInt(12,orderTask.getEffcount());
			ps.setInt(13,orderTask.getSuccount());
			ps.setInt(14, orderTask.getFaicount());
			ps.setString(15, orderTask.getTimer_status() == null ? " " : orderTask.getTimer_status());
			ps.setTimestamp(16,orderTask.getTimer_time() == null ? new Timestamp(date.getTime()) : orderTask.getTimer_time());
			ps.setString(17, orderTask.getRe_status() == null ? " " : orderTask.getRe_status());
			ps.setString(18, orderTask.getOrderstatus() == null ? " " : orderTask.getOrderstatus());
			ps.setString(19, orderTask.getSmsstatus() == null ? " " : orderTask.getSmsstatus());
			ps.setString(20, orderTask.getIsretry() == null ? " " : orderTask.getIsretry());
			ps.setTimestamp(21, new Timestamp(date.getTime()));
			ps.setTimestamp(22, orderTask.getOrdertm() == null ? new Timestamp(date.getTime()) : orderTask.getOrdertm());
			ps.setTimestamp(23, new Timestamp(date.getTime()));
			ps.setTimestamp(24, new Timestamp(date.getTime()));
			ps.setString(25, orderTask.getOrderNo() == null ? " " : orderTask.getOrderNo());
			ps.setDouble(26, orderTask.getSummoney() == null ? 0.0:orderTask.getSummoney());
			ps.setString(27, sdf_date.format(date));
			ps.setString(28, StringUtils.IsNullOrEmpty(orderTask.getMsg()) ? " " : orderTask.getMsg());
			ps.setString(29, orderTask.getP_ids());
			if( ps.executeUpdate() > 0){
				boolUpdate = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "插入流量订购任务表失败",Logger.LEVEL_ERROR );
			return false;
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "插入流量订购任务表:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
		return boolUpdate;
	}

	public boolean insertLlOrderDetail(List<LlOrderDetail> orderDetail) {
		if(orderDetail == null || orderDetail.size() == 0){
			return true;
		}
		
		String sql_ = flowOrderSql.getInsertLlOrderDetailsql();
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql_);
			int count = 0;
			String dateStr = sdf_date.format(new Date());
			for(LlOrderDetail d : orderDetail){
				ps.setString(1,d.getMobile());
				ps.setString(2, (d.getMsg() == null || "".equals(d.getMsg())) ? " " : d.getMsg());
				ps.setInt(3, d.getBatchId());
				ps.setString(4, d.getOrderno());
				ps.setString(5, d.getLlrpt() == null ? "-1" : d.getLlrpt());
				ps.setInt(6, d.getUser_id());
				ps.setLong(7, d.getOrg_id());
				ps.setInt(8, d.getPro_id());
				ps.setTimestamp(9, d.getOrdertm());
				ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
				ps.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
				ps.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
				ps.setString(13, d.getProductId());
				ps.setInt(14, d.getStatus());
				ps.setString(15, dateStr);
				ps.addBatch();
				//每一千条处理一次
				if(++count % 1000 == 0){
					ps.executeBatch();
					conn.commit();
					count = 0;
				}
			}
			if(count > 0){
				ps.executeBatch();
				conn.commit();
				count = 0;
			}
			return true;
		}  catch (Exception e) {
			EmpExecutionContext.errorLog(e, "插入流量订购任务详情表失败",Logger.LEVEL_ERROR );
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					EmpExecutionContext.errorLog(e, "插入流量订购任务详情表回滚失败",Logger.LEVEL_ERROR );
				}
			}
			return false;
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "插入流量订购任务详情表:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
		
		
	}
	
	public List<LlOrderDetail> findOrderDetail(String serverNum,String orderNum,int row){
		List<LlOrderDetail> list = new ArrayList<LlOrderDetail>();
		String sql_ = flowOrderSql.getFindOrderDetailSql(row);
		
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql_);
			ps.setString(1, orderNum);
			ps.setString(2, serverNum);
			rs = ps.executeQuery();
			//遍历结果集
			while(rs.next()){
				LlOrderDetail detail = new LlOrderDetail();
				detail.setId(rs.getInt("id"));
				detail.setMobile(rs.getString("mobile"));
				detail.setOrderno(rs.getString("orderno"));
				detail.setProductId(rs.getString("productid"));
				detail.setMsg(rs.getString("msg"));
				detail.setSp_user(rs.getString("sp_user"));
				detail.setSp_pwd(rs.getString("sp_pwd"));
				list.add(detail);
			}
			//返回订单详情对象
			return list;
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "读取流量订购详情表失败",Logger.LEVEL_ERROR );
			return list;
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "读取流量订购详情:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
	}
	
	public void executeCall(String call){
		Connection conn =null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			cs = conn.prepareCall(call);
			cs.execute();
			conn.commit();
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "执行存储过程失败："+call,Logger.LEVEL_ERROR );
		}finally{
			try {
				close(rs, cs,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "执行存储过程:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}		
		
	}
	
	public boolean updateFlowRptOne(LlOrderDetail det){
		//为空不处理
		if(det == null || det.getOrderno() == null || det.getLlrpt() == null || det.getMobile() == null ){
			return true;
		}
		
		String year = StringUtil.subOrderNoYear(det.getOrderno());
		
		String sql_ = "UPDATE LL_ORDER_DETAIL"+year+"  SET LLRPT = ?,ERRCODE = ?,RPTTM = ?,UPDATETM = ? , STATUS = ?  WHERE ORDERNO = ? AND MOBILE = ?";
		
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql_);
			conn.setAutoCommit(false);
			ps.setString(1, det.getLlrpt());
			ps.setString(2, det.getErrCode() == null ? " " : det.getErrCode());
			ps.setTimestamp(3, det.getRpttm() == null ? new Timestamp(System.currentTimeMillis()) : det.getRpttm() );
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			ps.setInt(5, 3);
			String orderNo = "";
			if(null != det.getOrderno()){
				orderNo = det.getOrderno().contains("EMP")?det.getOrderno().substring(0,det.getOrderno().indexOf("_")):det.getOrderno();
			}
			ps.setString(6, orderNo);
			ps.setString(7, det.getMobile());
			
			EmpExecutionContext.info("更新状态报告：Llrpt："+det.getLlrpt()+" ErrCode:"+det.getErrCode() +" orderNo:"+orderNo +" Mobile:"+det.getMobile());
			if(ps.executeUpdate() > 0){
				conn.commit();
				return true;
			}
			return false;
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "更新流量订购状态报告失败！",Logger.LEVEL_ERROR );
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					EmpExecutionContext.errorLog(e, "更新流量订购状态报告:数据库回滚失败！",Logger.LEVEL_ERROR );
				}
			}
			return false;
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "更新流量订购状态报告:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
	}
	
	public void insertStatRpt(LlOrderDetail det){
		//为空不处理
		if(det == null || det.getOrderno() == null || det.getLlrpt() == null || det.getMobile() == null ){
			return;
		}
		String sql_insert =flowOrderSql.getInsertLlStatusrptSql();
		
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql_insert);
			conn.setAutoCommit(false);
			ps.setString(1, det.getMobile());
			ps.setString(2, det.getProductId() == null ? " " : det.getProductId());
			ps.setString(3, det.getLlrpt());
			ps.setString(4, " ");
			ps.setString(5, det.getErrCode() == null ? " " : det.getErrCode());
			ps.setString(6, det.getOrderno());
			ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			ps.execute();
			conn.commit();
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "插入流量订购状态报告表失败！",Logger.LEVEL_ERROR );
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					EmpExecutionContext.errorLog(e, "插入流量订购状态报告:数据库回滚失败！",Logger.LEVEL_ERROR );
				}
			}
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "插入流量订购状态报告:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
	}
	
	public boolean updateFlowRpt(List<LlOrderDetail> orderDetail){
		//为空不处理
		if(orderDetail == null || orderDetail.isEmpty()){
			return true;
		}
		
		String year = StringUtil.subOrderNoYear(orderDetail.get(0).getOrderno());
		
		String sql_ = "UPDATE LL_ORDER_DETAIL"+year+"  SET LLRPT = ?,ERRCODE = ?,RPTTM = ?,UPDATETM = ? , STATUS = ?  WHERE ORDERNO = ? AND MOBILE = ?";
		
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql_);
			conn.setAutoCommit(false);
			for(LlOrderDetail det : orderDetail){
				ps.setString(1, det.getLlrpt());
				ps.setString(2, det.getErrCode());
				
				ps.setTimestamp(3, det.getRpttm());
				ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				ps.setInt(5, 3);
				String orderNo = "";
				if(null != det.getOrderno()){
					orderNo = det.getOrderno().contains("EMP")?det.getOrderno().substring(0,det.getOrderno().indexOf("_")):det.getOrderno();
				}
				ps.setString(6, orderNo);
				ps.setString(7, det.getMobile());
				ps.addBatch();
				EmpExecutionContext.info("更新状态报告：Llrpt："+det.getLlrpt()+" ErrCode:"+det.getErrCode() +" orderNo:"+orderNo +" Mobile:"+det.getMobile());
			}
			ps.executeBatch();
			conn.commit();
			return true;
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "批量更新流量订购状态报告失败！",Logger.LEVEL_ERROR );
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					EmpExecutionContext.errorLog(e, "批量更新流量订购状态报告:数据库回滚失败！",Logger.LEVEL_ERROR );
				}
			}
			return false;
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "批量更新流量订购状态报告:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
	}
	
	public int getMaxBatchid(String orderno,String year) {
		String sql = "select max(batchid) + 1 as maxBatchId from ll_order_detail"+year+" where  orderNo = ?";
		
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setString(1, orderno);
			rs = ps.executeQuery();
			if(rs.next()){
				return rs.getInt("maxBatchId");
			}
			
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "查询最大batchid失败",Logger.LEVEL_ERROR );
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "查询最大batchid:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
		return 0;
	}
	
	public int updateSql(String sql) {
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			int reCount = ps.executeUpdate();
			conn.commit();
			return reCount;
		} catch (Exception e) {
			if(conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					EmpExecutionContext.errorLog(e, "执行更新sql失败："+sql+",数据库回滚失败！",Logger.LEVEL_ERROR );
				}
			}
			EmpExecutionContext.errorLog(e, "执行更新sql失败："+sql,Logger.LEVEL_ERROR );
			return -1;
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "查询最大batchid:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
	}
	
	/**
	 * 判断订单编号是否存在
	 * @param orderno
	 * @return
	 */
	public boolean getLLOrderTask(String orderno) {
		String sql = "SELECT ORDERNO FROM LL_ORDER_TASK  WHERE  ORDERNO = ?";
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//订单编号是否存在标识
		boolean isExist  = false;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setString(1, orderno);
			rs = ps.executeQuery();
			if(rs.next()){
				isExist = true;
			}
			
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "查询ORDERNO失败",Logger.LEVEL_ERROR );
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "查询ORDERNO:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
		return isExist;
	}
	public List<LLOrderTask> findOrderNumUnsent() {
		String sql = flowOrderSql.findOrderNumUnsentSql();
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LLOrderTask> list = new ArrayList<LLOrderTask>();
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
			rs = ps.executeQuery();
			while(rs.next()){
				//ORDERNO,TIMER_TIME,RE_STATUS,TIMER_STATUS,ISRETRY
				LLOrderTask task = new LLOrderTask();
				task.setOrderNo(rs.getString("ORDERNO"));
				task.setUser_id(rs.getInt("USER_ID"));
				task.setTimer_time(rs.getTimestamp("TIMER_TIME"));
				task.setRe_status(rs.getString("RE_STATUS"));
				task.setTimer_status(rs.getString("TIMER_STATUS"));
				task.setIsretry(rs.getString("ISRETRY"));
				list.add(task);
			}
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "查询ORDERNO失败",Logger.LEVEL_ERROR );
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) { 
				EmpExecutionContext.errorLog(e, "查询ORDERNO:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
		return list;
	}
	
	public Boolean updateServerNumByOrdernum(String serverNum,String orderNum) {
		String sql = "UPDATE LL_ORDER_TASK SET SERVERNUM = ? WHERE SERVERNUM = '0' AND  ORDERNO = ?" ;
		Connection conn =null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn=connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			conn.setAutoCommit(true);
			ps = conn.prepareStatement(sql);
			ps.setString(1, serverNum);
			ps.setString(2, orderNum);
			if(ps.executeUpdate() > 0){
				return true;
			}
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e, "更新ServerNum失败",Logger.LEVEL_ERROR );
			return false;
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.errorLog(e, "更新ServerNum失败:关闭流操作失败",Logger.LEVEL_ERROR );
			}
		}
		return false;
	}
}


