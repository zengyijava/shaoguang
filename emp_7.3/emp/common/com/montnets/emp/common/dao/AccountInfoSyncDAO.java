/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-1-22 下午07:29:28
 */
package com.montnets.emp.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.gateway.AgwAccount;
import com.montnets.emp.entity.passage.XtGateQueue;

/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-1-22 下午07:29:28
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class AccountInfoSyncDAO extends SuperDAO
{
	/**
	 * 获取需要同步的账号信息
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-1-23 下午01:27:04
	 */
	public List<AgwAccount> getSyncAccount()
	{
		try
		{
			//查询语句
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM A_GWACCOUNT WHERE SPTYPE=0 AND PTACCUID IN (SELECT PTACCUID FROM A_GWSPBIND)");
			
			//查询结果
			return findEntityListBySQL(AgwAccount.class, sql.toString(), StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取需要同步的账号信息DAO异常！");
			return null;
		}
	}

    /**
     * 获取A_GWACCOUNT绑定的对应spgate
     * @param spaccid
     * @param spgate
     * @return
     */
	public XtGateQueue getBindXtGate(String spaccid, String spgate,String spisuncm)
	{
		XtGateQueue xtGateQueue = null;
		try
		{
			// 查询语句
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT GATE.* FROM A_GWSPBIND BIND INNER JOIN A_GWACCOUNT GWACCOUNT ON BIND.PTACCUID = GWACCOUNT.PTACCUID INNER JOIN XT_GATE_QUEUE GATE ON BIND.GATEID = GATE.ID");
			sql.append(" WHERE UPPER(GWACCOUNT.SPACCID) = '" + spaccid.toUpperCase() + "'");
            sql.append(" AND GATE.SPGATE = '" + spgate + "'");
            sql.append(" AND GATE.SPISUNCM = "+spisuncm);

			// 查询结果
			List<XtGateQueue> xtGateQueues = findEntityListBySQL(XtGateQueue.class, sql.toString(), StaticValue.EMP_POOLNAME);
			if(xtGateQueues != null && xtGateQueues.size() > 0)
			{
				xtGateQueue = xtGateQueues.get(0);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取对应通道信息失败！spaccid："+spaccid+",spgate："+spgate+"spisuncm："+spisuncm);
		}
		return xtGateQueue;
	}

	/**
	 * 根据key获取全局表中对应的值
	 * 
	 * @param keys
	 * @return
	 */
	public Map<String,String> getGlobalValByKey(String[] keys)
	{
        for (int i = 0; i < keys.length; i++) {
            keys[i] = "'" + keys[i] +"'";
        }
        Map<String,String> map = new HashMap<String, String>();

        String sql = "SELECT GLOBALKEY , GLOBALVALUE FROM LF_GLOBAL_VARIABLE WHERE GLOBALKEY in (" + StringUtils.join(keys,",") + ")";
		List<DynaBean> list = getListDynaBeanBySql(sql);
		if(list.size() > 0)
		{
            for (DynaBean dynaBean : list) {
                map.put(dynaBean.get("globalkey").toString(),dynaBean.get("globalvalue").toString());
            }
        }
		return map;
	}
}
