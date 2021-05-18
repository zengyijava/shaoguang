package com.montnets.emp.rms.wbs.dao.impl;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.rms.wbs.dao.ISvcContDAO;
import com.montnets.emp.rms.wbs.model.SvcCont;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * 数据层实现类，完成数据的批量插入
 * 
 * @ClassName SvcContDAOImpl
 * @Description 数据层实现类
 * @author zhouxiangxian 203492752@qq.com
 * @date 2018年1月11日
 */
public class SvcContDAOImpl implements ISvcContDAO {
	private IConnectionManager connManager;

	public SvcContDAOImpl() {
		connManager = new ConnectionManagerImp();// 获取实例对象，为了得到Connection
	}

	/**
	 * 数据的批量插入处理，插入成功返回true，否则返回false
	 */
	public boolean doInsertBatch(List<SvcCont> svcConts) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			// 获取数据库连接对象
			conn = connManager.getDBConnection("EMP");
			// 定义sql语句
			String sql = "INSERT INTO LF_RMS_SYN_TEMP(SPISUNCM,USERID,IYMD,ICOUNT,RSUCC,RFAIL,REQ_TIME,CORP_CODE,DEGREE)"
					+ "VALUES(?,?,?,?,?,?,?,?,?)";
			// 获取PreparedStatement对象，准备执行
			pstmt = conn.prepareStatement(sql);
			Iterator<SvcCont> iter = svcConts.iterator();
			conn.setAutoCommit(false);
			// 批处理准备，声明批处理变量
			long count = 1;
			while (iter.hasNext()) {
				SvcCont svcCont = iter.next();
				// 标记为0允许插入，否则拒绝,年月不是正确的格式不允许插入;同时只有在resultCode=0的情况下才允许更新
				if (svcCont.getResultCode() == 0) {
					// 设置内容
					// 设置运营商
					pstmt.setString(1, svcCont.getSpisuncm());
					// 设置sp账号
					pstmt.setString(2, svcCont.getUserid());
					// 设置年月日
					pstmt.setString(3, svcCont.getIymd());
					// 设置提交号码数量内容
					pstmt.setLong(4, Long.parseLong(svcCont.getIcount().trim()));
					// 设置发送成功数量内容
					pstmt.setLong(5, Long.parseLong(svcCont.getRsucc().trim()));
					// 设置接收失败内容
					pstmt.setLong(6, Long.parseLong(svcCont.getRfail().trim()));
					// 设置请求时间
					pstmt.setObject(7, new java.sql.Timestamp(System.currentTimeMillis()));
					// 设置企业编码------->企业编码由sp账号确定
					pstmt.setString(8, svcCont.getCorpCode().trim());
					// 设置档案
					pstmt.setInt(9, Integer.parseInt(svcCont.getDegree()));
					pstmt.addBatch();
					// 50000条数据执行一次批处理
					if (count % 50000 == 0) {
						pstmt.executeBatch();
						conn.commit();
					}
					count++;
				}
			}
			// 不足50000一组的
			pstmt.executeBatch();
			// 事务提交
			conn.commit();
		} catch (SQLException e) {
			// 事务回滚
			conn.rollback();
			return false;
		} finally {
			// 进行数据库连接关闭
			if (conn != null) {
				conn.close();
			}
			if(pstmt != null){
				pstmt.close();
			}
		}
		// 没抛异常默认执行正确
		return true;
	}

	/**
	 * 调用存储过程将临时表中数据进行转移处理，存储过程名称 PRO_RMS_TRANSFER
	 */
	public boolean doTransferData() throws Exception {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connManager.getDBConnection("EMP");
			String sql = "exec PRO_RMS_TRANSFER";
			pstmt = conn.prepareStatement(sql);
			flag = pstmt.executeUpdate() > 0;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"Mboss富信同步接口,调用存储过程PRO_RMS_TRANSFER将临时表中数据进行转移处理出现异常");
			return false;
		} finally {
			if (conn != null) {
				conn.close();
			}
			if(pstmt != null){
				pstmt.close();
			}
		}
		return flag;
	}

	/**
	 * 数据的单条插入
	 */
	public boolean doInsert(SvcCont svcCont) throws Exception {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = connManager.getDBConnection("EMP");// 获取数据库连接对象那个
			// 定义sql语句
			String sql = "INSERT INTO LF_RMS_SYN_TEMP(SPISUNCM,USERID,IYMD,ICOUNT,RSUCC,RFAIL,REQ_TIME,CORP_CODE,DEGREE)"
					+ "VALUES(?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);// 获取PreparedStatement对象，准备执行
			// 判断年月日格式是否正确
			pstmt.setString(1, svcCont.getSpisuncm());// 设置运营商
			pstmt.setString(2, svcCont.getUserid());// 设置sp账号
			pstmt.setString(3, svcCont.getIymd());// 设置年月日
			pstmt.setLong(4, Long.parseLong(svcCont.getIcount().trim()));// 设置提交号码数量内容
			pstmt.setLong(5, Long.parseLong(svcCont.getRsucc().trim()));// 设置发送成功数量内容
			pstmt.setLong(6, Long.parseLong(svcCont.getRfail().trim()));// 设置接收失败内容
			pstmt.setObject(7, new java.sql.Timestamp(System.currentTimeMillis()));// 设置请求时间
			pstmt.setString(8, svcCont.getCorpCode().trim());// 设置企业编码------->企业编码由sp账号确定
			pstmt.setInt(9, Integer.parseInt(svcCont.getDegree().trim()));// 设置档案
			flag = pstmt.executeUpdate() > 0;
		} catch (Exception e) {
			return false;
		} finally {
			// 数据连接关闭
			if (conn != null) {
				conn.close();
			}
			if(pstmt != null){
				pstmt.close();
			}
		}
		return flag;
	}
}
