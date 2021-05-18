package com.montnets.emp.common.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.approveflow.TableLfFlowRecord;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;

/**
 * 
 * @project emp
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-19 下午03:17:51
 * @description
 */
public class ViewLfFlowRecordVo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("frId", TableLfFlowRecord.FR_ID);
		columns.put("mtId", TableLfFlowRecord.MT_ID);
		columns.put("FId", TableLfFlowRecord.F_ID);
		columns.put("RTime", TableLfFlowRecord.R_TIME);
		columns.put("reviewType", TableLfFlowRecord.REVIEW_TYPE);
		columns.put("RLevel", TableLfFlowRecord.R_LEVEL);
		columns.put("RLevelAmount", TableLfFlowRecord.R_LEVELAMOUNT);
		columns.put("RContent", TableLfFlowRecord.R_CONTENT);
		columns.put("RState", TableLfFlowRecord.R_STATE);
		columns.put("comments", TableLfFlowRecord.COMMENTS);
		columns.put("preReviName", "preReviName");
		columns.put("reviName", "reviName");
		columns.put("name", TableLfSysuser.NAME);
		columns.put("userName", TableLfSysuser.USER_NAME);
		columns.put("depName", TableLfDep.DEP_NAME);
		columns.put("title", TableLfMttask.TITLE);
		columns.put("taskName", TableLfMttask.TASK_NAME);
		columns.put("bmtType", TableLfMttask.BMT_TYPE);
		columns.put("spUser", TableLfMttask.SP_USER);
		columns.put("staffName", TableLfMttask.STAFF_NAME);
		columns.put("submitTime", TableLfMttask.SUBMIT_TIME);
		columns.put("mobileUrl", TableLfMttask.MOBILE_URL);
		columns.put("effCount", TableLfMttask.EFF_COUNT);
		columns.put("msg", TableLfMttask.MSG);
		columns.put("msgType", TableLfMttask.MSG_TYPE);
		columns.put("timerTime", TableLfMttask.TIMER_TIME);
		columns.put("timerStatus", TableLfMttask.TIMER_STATUS);
		columns.put("userState", TableLfSysuser.USER_STATE);	
		columns.put("tmplPath", TableLfMttask.TMPL_PATH);	
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
