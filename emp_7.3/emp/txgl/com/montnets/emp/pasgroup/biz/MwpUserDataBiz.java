package com.montnets.emp.pasgroup.biz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.pasgroup.dao.MwpUserDataDao;
import com.montnets.emp.pasgroup.vo.UserPropertyVo;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.GwUserproperty;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.servmodule.txgl.table.TableUserdata;
import com.montnets.emp.util.PageInfo;

public class MwpUserDataBiz extends SuperBiz
{
	ErrorLoger	errorLoger	= new ErrorLoger();

	public List<Userdata> findSpUser(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{
		List<Userdata> userdatas = null;
		try
		{
			userdatas = new MwpUserDataDao().findSpUser(conditionMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "查找账户异常！"));
			throw e;
		}
		return userdatas;
	}

	public List<Userdata> findSpUserByCorp(String corp, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{
		List<Userdata> xx = null;
		try
		{
			xx = new MwpUserDataDao().findSpUserByCorp(corp, conditionMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "查找账户异常！"));
			// TODO: handle exception
			throw e;
		}
		return xx;
	}

	/**
	 * 查询SP账号
	 */
	public UserPropertyVo findUserproperty(String userid) throws Exception
	{
		UserPropertyVo upvo = new UserPropertyVo();
		upvo.setUserid(userid);
		// 拼接sql
		String sql = "SELECT CACERTNAME,VERIFYPEER,VERIFYHOST FROM GW_USERPROPERTY WHERE USERID='" + userid + "'";
		try
		{// 查询出所有的信息
			List<DynaBean> dybns = new SuperDAO().getListDynaBeanBySql(sql);
			if(dybns != null && dybns.size() > 0)
			{
				DynaBean dybn = dybns.get(0);
				if(dybn != null && dybn.get("cacertname") != null && dybn.get("verifypeer") != null && dybn.get("verifyhost") != null)
				{
					upvo.setCacertname(dybn.get("cacertname").toString());
					upvo.setVerifypeer(dybn.get("verifypeer").toString());
					upvo.setVerifyhost(dybn.get("verifyhost").toString());
				}
			}
		}
		catch (Exception e1)
		{
			EmpExecutionContext.error(e1, "更新账号参数设置失败");
		}
		return upvo;
	}

	/**
	 * 查询SP账号
	 */
	public boolean updateHttps(String userid, String verifypeer, String verifyhost, String cacertname)
	{
		boolean result = false;
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{
			cacertname = cacertname.trim() == "" ? " " : cacertname;
		}
		Connection conn = null;
		PreparedStatement ps = null;
		// 拼接sql
		String sql = " UPDATE GW_USERPROPERTY SET CACERTNAME=?,VERIFYPEER=?,VERIFYHOST=? WHERE USERID=?";
		try
		{// 查询出所有的信息
			IConnectionManager connectionManager = new ConnectionManagerImp();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			ps.setString(1, cacertname);
			ps.setInt(2, Integer.parseInt(verifypeer));
			ps.setInt(3, Integer.parseInt(verifyhost));
			ps.setString(4, userid);
			int count = ps.executeUpdate();
			if(count > 0)
			{
				result = true;
			}
		}
		catch (Exception e1)
		{
			EmpExecutionContext.error(e1, "更新账号参数设置失败");
		}finally
		{
			try
			{
				new SuperDAO().close(null, ps, conn);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源出错！");
			}
		}
		return result;

	}

	/**
	 * 更新账号参数设置
	 * 
	 * @param perty
	 * @return
	 */
	public boolean update(GwUserproperty perty)
	{
		boolean flag = false;
		Connection conn = empTransDao.getConnection();
		try
		{// 查询出所有的信息
			empTransDao.update(conn, perty);
		}
		catch (Exception e1)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e1, "更新账号参数设置失败");
		}
		finally
		{
			flag = true;
			empTransDao.closeConnection(conn);
		}

		return flag;
	}

	/**
	 * 更新账号参数设置
	 * 
	 * @param perty
	 * @return
	 */
	public boolean save(GwUserproperty perty)
	{
		boolean flag = false;
		Connection conn = empTransDao.getConnection();
		try
		{// 查询出所有的信息
			empTransDao.saveObjectReturnID(conn, perty);
		}
		catch (Exception e1)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e1, "保存账号参数设置失败");
		}
		finally
		{
			flag = true;
			empTransDao.closeConnection(conn);
		}

		return flag;
	}
}
