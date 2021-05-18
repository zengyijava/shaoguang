package com.montnets.emp.common.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-17 下午05:16:24
 * @description
 */

public class ViewLfMttaskVo2
{

	protected static  final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{

		columns.put("mtId", TableLfMttask.MT_ID);
		columns.put("userId", TableLfMttask.USER_ID);
		columns.put("title", TableLfMttask.TITLE);
		columns.put("msgType", TableLfMttask.MSG_TYPE);
		columns.put("submitTime", TableLfMttask.SUBMIT_TIME);
		columns.put("subState", TableLfMttask.SUB_STATE);
		columns.put("reState", TableLfMttask.RE_STATE);
		columns.put("sendstate", TableLfMttask.SEND_STATE);
		columns.put("sendLevel", TableLfMttask.SEND_LEVEL);
		columns.put("bmtType", TableLfMttask.BMT_TYPE);
		//columns.put("spUser", TableLfMttask.SP_USER);
		columns.put("staffName", TableLfMttask.STAFF_NAME);
		columns.put("mobileUrl", TableLfMttask.MOBILE_URL);
		columns.put("subCount", TableLfMttask.SUB_COUNT);
		columns.put("effCount", TableLfMttask.EFF_COUNT);
		columns.put("sucCount", TableLfMttask.SUC_COUNT);
		columns.put("faiCount", TableLfMttask.FAI_COUNT);
		columns.put("msg", TableLfMttask.MSG);
		//columns.put("spPwd", TableLfMttask.SP_PWD);
		columns.put("comments", TableLfMttask.COMMENTS);
		columns.put("icount", TableLfMttask.ICOUNT);
		columns.put("errorCodes", TableLfMttask.ERROR_CODES);
		columns.put("name", TableLfSysuser.NAME);
		columns.put("userName", TableLfSysuser.USER_NAME);
		columns.put("depName", TableLfDep.DEP_NAME);
		columns.put("depId", TableLfDep.DEP_ID);		
		columns.put("timerTime", TableLfMttask.TIMER_TIME);
		//columns.put("busCode", TableLfMttask.BUS_CODE);
		//columns.put("busName", TableLfBusManager.BUS_NAME);
		columns.put("timerStatus", TableLfMttask.TIMER_STATUS);
		columns.put("isReply", TableLfMttask.ISREPLY);
		//columns.put("spNumber", TableLfMttask.SPNUMBER);
		columns.put("subNo", TableLfMttask.SUBNO);
		columns.put("msType", TableLfMttask.MS_TYPE);
		columns.put("isRetry", TableLfMttask.ISRETRY);
		columns.put("rFail2", TableLfMttask.RFAIL2);
		columns.put("userState", TableLfSysuser.USER_STATE);
		columns.put("icount2", TableLfMttask.ICOUNT2);
		columns.put("taskName", TableLfMttask.TASK_NAME);
		columns.put("tmplPath", TableLfMttask.TMPL_PATH);
		columns.put("taskId", TableLfMttask.TASKID);
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
