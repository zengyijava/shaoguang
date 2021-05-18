package com.montnets.emp.netnews.vo.view;

import com.montnets.emp.netnews.table.TableLfWXBASEINFO;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-17 下午05:16:24
 * @description
 */

public class ViewVisitDATAvo
{

	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		
		columns.put("ID",TableLfWXBASEINFO.ID);					//自动编号
		columns.put("NETID",TableLfWXBASEINFO.NETID);				//网讯编号
		columns.put("NETNAME","NETNAME");			//网讯名称
		columns.put("NETMSG",TableLfMttask.MSG);				//短信消息/网讯消息
		
		columns.put("UNAME",TableLfSysuser.NAME);			//操作员
		columns.put("UDEP",TableLfDep.DEP_NAME);			//隶属机构
		columns.put("CREATDATE","CREATEDATE");		//创建时间/发送时间
		columns.put("TIMEOUT","TIMEOUT");			//其他：过期时间/有效时间
		columns.put("subState", TableLfMttask.SUB_STATE);
		columns.put("reState", TableLfMttask.RE_STATE);
		columns.put("sendstate", TableLfMttask.SEND_STATE);
		columns.put("timerStatus", TableLfMttask.TIMER_STATUS);		
		columns.put("mtid", TableLfMttask.MT_ID);		
		columns.put("taskid", TableLfMttask.TASKID);	
	
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM()
	{
		return columns;
	}
}
