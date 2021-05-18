package com.montnets.emp.netnews.vo.view;

import com.montnets.emp.netnews.table.TableLfWXBASEINFO;
import com.montnets.emp.netnews.table.TableLfWXData;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sysuser.TableLfDep;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-17 下午05:16:24
 * @description
 */

public class ViewTRUSTDATAvo
{

	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		
		//操作员名称
		columns.put("UNAME","UNAME");
		//机构名称
		columns.put("UDEP",TableLfDep.DEP_NAME);

		//任务Mtid
		columns.put("mtid",TableLfMttask.MT_ID);
		//网讯主题
		columns.put("title",TableLfMttask.TITLE);	
		
		//网讯编号
		columns.put("NETID",TableLfWXBASEINFO.NETID);		
		//网讯名称
		columns.put("NETNAME","WNAME");		
		//短信消息
		columns.put("NETMSG",TableLfMttask.MSG);			
		
		columns.put("ID",TableLfWXData.DID);			//自动编号
		columns.put("CODE",TableLfWXData.CODE);			//互动名称
		columns.put("NAME","WDNAME");			//互动名称
		columns.put("NAMETYPE","WDTNAME");			//互动类别
		columns.put("SENDDATE",TableLfMttask.TIMER_TIME);		//发送时间
		
		columns.put("spcount","SPCOUNT");//接收人数   成功数+未反数
		columns.put("TABLENAME",TableLfWXBASEINFO.DATA_TABLENAME);
		columns.put("colname",TableLfWXData.COLNAME);
		columns.put("subState", TableLfMttask.SUB_STATE);
		columns.put("reState", TableLfMttask.RE_STATE);
		columns.put("sendstate", TableLfMttask.SEND_STATE);
		columns.put("timerStatus", TableLfMttask.TIMER_STATUS);		
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
