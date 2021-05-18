package com.montnets.emp.shorturl.surlmanage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.shorturl.surlmanage.entity.Message;
import com.montnets.emp.shorturl.surlmanage.entity.StopNeturl;


/**
 * 通知禁用dao方法
 * @author Administrator
 *
 */
public class NoticeStopDao extends SuperDAO{

	/**
	 * 获取对应企业的sp账号密码
	 * @param cropcode
	 * @return
	 */
	public Message getSpuser(String cropcode){
		String sql = "select acc.SPUSER,us.USERPASSWORD from LF_SP_DEP_BIND acc " +
				"left join USERDATA us on acc.SPUSER = us.USERID " +
				"where us.STATUS = 0 and acc.CORP_CODE = ? " +
				"order by us.ORDERTIME desc";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Message result = null;
		try {
			
			conn =connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setString(1, cropcode);
			rs = ps.executeQuery();
			if (rs!=null && rs.next()) {
				result = new Message();
				result.setUserid(rs.getString(1));
				result.setPwd(rs.getString(2));
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "禁用时获取sp账号密码异常！");
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "禁用时获取sp账号密码-关闭数据库连接异常");
			}
		}
		return result;
	}
	
	
	
	/**
	 * 查客户禁用的url
	 * @return
	 */
	public List<StopNeturl> findCusUrl(){
		String sql = "select id,corp_code,src_url from LF_NETURL where URLSTATE = -2";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<StopNeturl> result = new ArrayList<StopNeturl>();
		StopNeturl url = null;
		try {
			
			conn =connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs!=null && rs.next()) {
				 url = new StopNeturl();
				 url.setId(rs.getLong(1));
				 url.setCorpcode(rs.getString(2));
				 url.setUrl(rs.getString(3));
				 result.add(url);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取需要被禁用的长地址异常！");
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取需要被禁用的长地址-关闭数据库连接异常");
			}
		}
		return result;
	}
	
	/**
	 * 查运营商禁用的url
	 * @return
	 */
	public List<StopNeturl> findOperUrl(){
		String sql = "select id,corp_code,src_url from LF_NETURL where ISPASS=-3";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<StopNeturl> result = new ArrayList<StopNeturl>();
		StopNeturl url = null;
		try {
			conn =connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs!=null && rs.next()) {
				 url = new StopNeturl();
				 url.setId(rs.getLong(1));
				 url.setCorpcode(rs.getString(2));
				 url.setUrl(rs.getString(3));
				 result.add(url);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取需要被禁用的长地址异常！");
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取需要被禁用的长地址-关闭数据库连接异常");
			}
		}
		return result;
	}


	/**
	 * 更新url禁用状态
	 * @param id
	 * @param command  -2运营商    -1用户
	 */
	public boolean updateStopUrl(Long id, String command) {
		String sql;
		if ("-2".equals(command)) {
			sql = "update LF_NETURL set ISPASS=? where ID=?";
		}else {
			sql = "update LF_NETURL set URLSTATE=? where ID=?";
		}
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = -1;
		boolean result = false;
		try {
			conn =connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(command));
			ps.setLong(2, id);
			rs = ps.executeUpdate();
			if (rs>0) {
				result = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "更新长地址禁用状态异常！");
		}finally{
			try {
				close(null, ps,conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "更新长地址禁用状态-关闭数据库连接异常");
			}
		}
		return result;
	}
	
	

}
