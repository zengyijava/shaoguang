package com.montnets.emp.rms.rmstask.vo.view;

import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.rms.TableLfRmsTaskCtrl;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 群发任务、群发历史View对象
 * @author Cheng
 * @date  2018-08-03 17:03:50
 */

public class ViewLfMttaskVo {

	protected final static Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static {
		columns.put("mtId", TableLfMttask.MT_ID);
		columns.put("spUser", TableLfMttask.SP_USER);
		columns.put("mobileUrl", TableLfMttask.MOBILE_URL);
		columns.put("title", TableLfMttask.TITLE);
		columns.put("busCode", TableLfMttask.BUS_CODE);
		columns.put("userId", TableLfMttask.USER_ID);
		columns.put("tempId",  TableLfMttask.TEMP_ID);
		columns.put("effCount", TableLfMttask.EFF_COUNT);
		columns.put("icount2", TableLfMttask.ICOUNT2);
		columns.put("icount", TableLfMttask.ICOUNT);
		columns.put("rfail2", TableLfMttask.RFAIL2);
		columns.put("faiCount", TableLfMttask.FAI_COUNT);
		columns.put("timerTime", TableLfMttask.TIMER_TIME);
		columns.put("submitTime", TableLfMttask.SUBMIT_TIME);
		columns.put("taskType", TableLfMttask.TASKTYPE);
		columns.put("timerStatus", TableLfMttask.TIMER_STATUS);
		columns.put("subState", TableLfMttask.SUB_STATE);
		columns.put("sendState", TableLfMttask.SEND_STATE);
		columns.put("isRetry", TableLfMttask.ISRETRY);
		columns.put("taskId", TableLfMttask.TASKID);
		columns.put("tmplPath", TableLfMttask.TMPL_PATH);
		columns.put("busName", TableLfBusManager.BUS_NAME);
		columns.put("name", TableLfSysuser.NAME);
		columns.put("userState", TableLfSysuser.USER_STATE);
		columns.put("depName", TableLfDep.DEP_NAME);
		columns.put("currentCount",  TableLfRmsTaskCtrl.CURRENT_COUNT);
		columns.put("degree",  TableLfTemplate.DEGREE);
		columns.put("tmName",  TableLfTemplate.TM_NAME);
		columns.put("tmId", TableLfTemplate.TM_ID);
		columns.put("msgType", TableLfMttask.MSG_TYPE);
	}

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}
}
