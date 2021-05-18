/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-14 上午10:59:42
 */
package com.montnets.emp.mobilebus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.table.ydyw.TableLfBusTaoCan;
import com.montnets.emp.table.ydyw.TableLfContractTaocan;
import com.montnets.emp.table.ydyw.TableLfDepTaocan;
import com.montnets.emp.table.ydyw.TableLfProCharges;

/**
 * @description 
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-14 上午10:59:42
 */

public class MobileBusMoOrderHandleDAO extends SuperDAO
{
	/**
	 * 获取套餐关系表信息
	 * @description    
	 * @param id  指令ID
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-14 上午11:08:49
	 */
	public List<DynaBean> getTaoCanCmd(LfMotask moTask)
	{
		try
		{
			String sql = "SELECT * FROM LF_TAOCAN_CMD WHERE STRUCTCODE = (SELECT STRUCTCODE FROM A_CMD_ROUTE WHERE TRUCTTYPE='03' " +
					"AND ID="+moTask.getMoOrder()+") AND CORP_CODE='"+moTask.getCorpCode()+"'";

			List<DynaBean> lfTaoCanCmdList = this.getListDynaBeanBySql(sql);
			return lfTaoCanCmdList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取套餐关系表信息异常！指令ID："+moTask.getMoOrder()+"，企业编码："+moTask.getCorpCode());
			return null;
		}
	}
	
	/**
	 * 获取已开通套餐签约ID
	 * @description    
	 * @param contractId  签约ID
	 * @param taoCanCode  套餐编码
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-15 下午04:36:49
	 */
	public List<DynaBean> getContractBind(String contractId, String taoCanCode)
	{
		try
		{
			String sql = "SELECT CONTRACT_ID AS CONTRACTID FROM LF_CONTRACT_TAOCAN WHERE CONTRACT_ID IN ("+contractId+") " +
					"AND IS_VALID ='0' AND TAOCAN_CODE = '"+taoCanCode+"'";

			List<DynaBean> lfContractBindList = this.getListDynaBeanBySql(sql);
			return lfContractBindList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询签约套餐关系表异常！签约ID："+contractId+"套餐编码："+taoCanCode);
			return null;
		}
	}
	

	/**
	 * 更新签约套餐关系表
	 * @description    
	 * @param contractId
	 * @param taoCanCode
	 * @param structType
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-2-2 下午01:57:12
	 */
	public int updateContractTaocan(String contractId, String taoCanCode, String structType)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long timeMillis = System.currentTimeMillis();
			String timestamp = new DataAccessDriver().getGenericDAO().getTimeCondition(format.format(timeMillis));
			//  拼接修改的SQL语句
			StringBuffer sqlSb = new StringBuffer("update ").append(TableLfContractTaocan.TABLE_NAME).append(" set ").append(TableLfContractTaocan.IS_VALID)
			.append("='1', ").append(TableLfContractTaocan.UPDATE_TIME).append(" = ").append(timestamp).append(" where ").append(TableLfContractTaocan.IS_VALID).append("='0'").append(" and ")
			.append(TableLfContractTaocan.CONTRACT_ID).append(" in (").append(contractId).append(")");
			if("1".equals(structType))
			{
				sqlSb.append(" and ").append(TableLfContractTaocan.TAOCAN_CODE).append("='").append(taoCanCode).append("'");
			}
			
			String sql = sqlSb.toString();
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行sql
			int count = ps.executeUpdate();
			return count;

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"退订指令更新签约套餐表异常！");
			return -1;
		} finally
		{
			// 关闭数据库资源
			try
			{
				super.close(null, ps, conn);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "退订指令更新签约套餐表关闭连接异常！");
			}
		}
	}

	
	/**
	 * 获取机构套餐表操作员ID
	 * @description    
	 * @param userId
	 * @param taoCanCode
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-2-2 下午12:04:23
	 */
	public List<DynaBean> getDepTaocan(String userId,String taoCanCode)
	{
		try
		{
			String sql = "SELECT "+TableLfDepTaocan.CONTRACT_USER+" FROM "+ TableLfDepTaocan.TABLE_NAME+" WHERE "
			+TableLfDepTaocan.TAOCAN_CODE +" = '"+ taoCanCode +"' AND "+TableLfDepTaocan.CONTRACT_USER +" IN ("+userId+")";
			List<DynaBean> lfDepTaocanList = this.getListDynaBeanBySql(sql);
			return lfDepTaocanList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取机构套餐表操作员ID异常！");
			return null;
		}
	}
	
	/**
	 * 获取套餐信息
	 * @description    
	 * @param taoCanCode
	 * @param corpCode
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-2-6 上午10:32:40
	 */
	public List<DynaBean> getBusTaoCan(String taoCanCode, String corpCode)
	{
		try
		{
			List<DynaBean> lfBusTaoCanList = null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String Timestamp = new DataAccessDriver().getGenericDAO().getTimeCondition(format.format(System.currentTimeMillis()));
			StringBuffer sql = new StringBuffer().append("SELECT ").append("bt.").append(TableLfBusTaoCan.TAOCAN_NAME).append(", ")
						.append("pc.").append(TableLfProCharges.BUCKUP_MAXTIMER).append(" FROM ").append(TableLfBusTaoCan.TABLE_NAME)
						.append(" bt, ").append(TableLfProCharges.TABLE_NAME).append(" pc ").append(" WHERE ").append("bt.").append(TableLfBusTaoCan.TAOCAN_CODE)
						.append(" = '").append(taoCanCode).append("' AND ").append("bt.").append(TableLfBusTaoCan.STATE).append(" = 0 AND ")
						.append("bt.").append(TableLfBusTaoCan.CORP_CODE).append(" = '").append(corpCode).append("' AND ")
						.append("bt.").append(TableLfBusTaoCan.START_DATE).append(" <= ").append(Timestamp).append(" AND ")
						.append("bt.").append(TableLfBusTaoCan.END_DATE).append(" >= ").append(Timestamp).append(" AND ")
						.append(" bt.").append(TableLfBusTaoCan.TAOCAN_CODE).append(" = ").append("pc.").append(TableLfProCharges.TAOCAN_CODE);
			lfBusTaoCanList = this.getListDynaBeanBySql(sql.toString());
			return lfBusTaoCanList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "");
			return null;
		}
	}
}
