/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-7-29 下午06:42:54
 */
package com.montnets.emp.security.blacklist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.SysConfValue;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;

/**
 * @description 
 * @project emp_std_185
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-7-29 下午06:42:54
 */

public class BlackListAtomDAO extends SuperDAO
{
	
	/**
	 *  同步短信所有黑名单信息
	 * @description    
	 * @param AllBlackList
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-30 下午01:46:47
	 */
	public Long setAllBlackList(HashMap<String, HashMap<Long, Byte>> AllBlackList)
	{
		String sql = "select PHONE, SVRTYPE, CORPCODE FROM PB_LIST_BLACK WHERE OPTYPE=1 AND BLTYPE=1";
		String sqlByID = "select MAX(ID) AS ID FROM PB_LIST_BLACK WHERE OPTYPE=1 AND BLTYPE=1";
		Long maxId = 0L;
		StringBuffer key=new StringBuffer();
		HashMap<Long, Byte> valueMap = null;
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		int blackCount = 0;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sqlByID);
			rs = ps.executeQuery();
			//黑名单同步最大ID
			if(rs.next())
			{
				maxId = rs.getLong("ID");
			}
			else
			{
				return maxId;
			}
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				key.setLength(0);
/*				buffer.append(rs.getLong("PHONE")).append(",")
				.append(" ".equals(rs.getString("SVRTYPE")) ? "@" : rs.getString("SVRTYPE")).append(",")
				.append(rs.getString("CORPCODE"));
				AllBlackList.put(buffer.toString(), null);*/
				key.append(rs.getString("SVRTYPE")).append(rs.getString("CORPCODE"));
				if(AllBlackList.containsKey(key.toString().trim()))
				{
					AllBlackList.get(key.toString().trim()).put(rs.getLong("PHONE"), null);
				}
				else
				{
					valueMap = new HashMap<Long, Byte>();
					valueMap.put(rs.getLong("PHONE"), null);
					AllBlackList.put(key.toString().trim(), valueMap);
				}
				blackCount++;
			}
			EmpExecutionContext.info("短信黑名单全量加载成功!共加载:" + blackCount);
			return maxId;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取所有短信黑名单信息异常！");
			return maxId;
		} finally {
			try
			{
				close(rs, ps, conn);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取所有短信黑名单信息关闭连接异常！");
			}
		}
	}
	
	/**
	 * 同步彩信所有黑名单信息
	 * @description    
	 * @param AllBlackList
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-30 下午03:53:40
	 */
	public Long setAllSmsBlackList(HashMap<String, String> AllMMsBlackList)
	{
		String sql = "select PHONE, CORPCODE FROM PB_LIST_BLACK WHERE OPTYPE=1 AND BLTYPE=2";
		String sqlByID = "select MAX(ID) AS ID FROM PB_LIST_BLACK WHERE OPTYPE=1 AND BLTYPE=2";
		Long maxId = 0L;
		StringBuffer buffer=new StringBuffer();
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sqlByID);
			rs = ps.executeQuery();
			//黑名单同步最大ID
			if(rs.next())
			{
				maxId = rs.getLong("ID");
			}
			else
			{
				return maxId;
			}
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				buffer.setLength(0);
				buffer.append(rs.getLong("PHONE")).append(",")
				.append(rs.getString("CORPCODE"));
				AllMMsBlackList.put(buffer.toString(), null);
			}
			return maxId;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取所有彩信黑名单信息异常！");
			return maxId;
		} finally {
			try
			{
				close(rs, ps, conn);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取所有彩信黑名单信息关闭连接异常！");
			}
		}
	}
	
	
	/**
	 * 根据key获取全局表中对应的值
	 * @description    
	 * @param keys
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-2-1 下午03:51:13
	 */
	public Map<String,String> getGlobalValByKey(String[] keys)
	{
        try
		{
//			for (int i = 0; i < keys.length; i++) {
//			    keys[i] = "'" + keys[i] +"'";
//			}
//			Map<String,String> map = new HashMap<String, String>();
//
//			String sql = "SELECT GLOBALKEY , GLOBALVALUE , GLOBALSTRVALUE FROM LF_GLOBAL_VARIABLE WHERE GLOBALKEY in (" + StringUtils.join(keys,",") + ")";
//			List<DynaBean> list = getListDynaBeanBySql(sql);
//			if(list.size() > 0)
//			{
//			    for (DynaBean dynaBean : list)
//			    {
//			    	if("ADDBLAMOORDER".equals(dynaBean.get("globalkey").toString())
//			    			|| "DELBLAMOORDER".equals(dynaBean.get("globalkey").toString())
//			    			|| "BLACKCORPCODE".equals(dynaBean.get("globalkey").toString()))
//			    	{
//			    		map.put(dynaBean.get("globalkey").toString(),dynaBean.get("globalstrvalue").toString());
//			    	}
//			    	else
//			    	{
//			    		map.put(dynaBean.get("globalkey").toString(),dynaBean.get("globalvalue")==null||"".equals(dynaBean.get("globalvalue"))?"0":dynaBean.get("globalvalue").toString());
//		    		}
//		    	}
//			}
//			return map;

			Map<String,String> map = new HashMap<String, String>();
			for (int i = 0; i < keys.length; i++){
				if ("ADDBLABYMOORDERFLAG".equals(keys[i])) {
					map.put("ADDBLABYMOORDERFLAG",String.valueOf(SysConfValue.getADDBLABYMOORDERFLAG()));
				}else if("ADDBLAMOORDER".equals(keys[i])){
					map.put("ADDBLAMOORDER",SysConfValue.getADDBLAMOORDER());
				}else if("DELBLAMOORDER".equals(keys[i])){
					map.put("DELBLAMOORDER",SysConfValue.getDELBLAMOORDER());
				}else if("BLACKCORPCODE".equals(keys[i])){
					map.put("BLACKCORPCODE",SysConfValue.getBLACKCORPCODE());
				}else if("ADDBLAMOMAXID".equals(keys[i])){
					map.put("ADDBLAMOMAXID",String.valueOf(SysConfValue.getADDBLAMOMAXID()));
				}
			}
			return map;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据key获取全局缓存中对应的值异常！");
			//EmpExecutionContext.error(e, "根据key获取全局表中对应的值异常！");
			return null;
		}
	}
	
	/**
	 * 根据最大ID和指令查询下行记录
	 * @description    
	 * @param maxId
	 * @param order
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-2-1 下午06:37:56
	 */
	public List<DynaBean> getMoTask(Integer maxId, String order, String corpCode)
	{
		try
		{
			String sql = "SELECT MO.*,SP.CORP_CODE FROM LF_SP_DEP_BIND SP LEFT JOIN MO_TASK MO ON " +
						"MO.USERID=SP.SPUSER WHERE MO.ID > "+maxId+" AND UPPER(MSGCONTENT) IN ("+order+") AND SP.CORP_CODE IN("+corpCode+") ORDER BY ID ASC";
			return getListDynaBeanBySql(sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据最大ID和指令查询下行记录异常！");
			return null;
		}
	}
	
	/**
	 * 根据最大ID和指令查询下行记录
	 * @description    
	 * @param maxId
	 * @param order
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-2-1 下午06:37:56
	 */
	public List<DynaBean> getMoTaskByMaxId(Integer maxId, String order, String corpCode)
	{
		try
		{
			String sql = "SELECT  MO.ID, MO.PHONE, MO.MSGCONTENT, SP.CORP_CODE FROM LF_SP_DEP_BIND SP LEFT JOIN MO_TASK MO ON " +
			"MO.USERID=SP.SPUSER WHERE MO.ID > "+maxId+" AND UPPER(MSGCONTENT) IN ("+order+") AND SP.CORP_CODE IN("+corpCode+")";
			
			String sortSql = " ORDER BY ID ASC";
			String countSql = new StringBuffer("select count(*) totalcount from (").append(sql).append(") A").toString();
			PageInfo pageInfo = new PageInfo();
			//设置一次查询的数量
			pageInfo.setPageSize(StaticValue.getMoTdCount());
			sql += sortSql;
			return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据最大ID和指令查询下行记录异常！");
			return null;
		}
	}
	
	/**
	 * 删除黑名单
	 * @description    
	 * @param conn
	 * @param corpCode
	 * @param phones
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-25 下午03:27:43
	 */
	public boolean deleteBlackList(Connection conn, String corpCode, String phones)
	{
		try
		{
			String sql = "DELETE FROM PB_LIST_BLACK WHERE CORPCODE = '"+corpCode
					+"' AND SVRTYPE = ' ' AND BLTYPE = 1 AND PHONE IN("+phones+")";
			executeBySQL(conn, sql);
			return true;
		}
		catch (Exception e)  
		{
			EmpExecutionContext.error(e, "根据上行内容设置黑名单,删除黑名单失败,corpCode:"+corpCode+",phones:"+phones);
			return false;
		}
	}
}
