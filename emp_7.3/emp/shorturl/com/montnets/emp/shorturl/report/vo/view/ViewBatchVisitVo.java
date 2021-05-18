package com.montnets.emp.shorturl.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewBatchVisitVo {
	protected static final Map<String, String> columns = new LinkedHashMap();

    static {
        //columns.put("mtId", "MT_ID");
        columns.put("userId", "USER_ID");
        columns.put("title", "TITLE");
        //columns.put("msgType", "MSG_TYPE");
        //columns.put("submitTime", "SUBMITTIME");
        //columns.put("subState", "SUB_STATE");
        //columns.put("reState", "RE_STATE");
        //columns.put("sendstate", "SENDSTATE");
        //columns.put("sendLevel", "SENDLEVEL");
        //columns.put("bmtType", "BMTTYPE");
        //columns.put("spUser", "SP_USER");
        //columns.put("mobileUrl", "MOBILE_URL");
        //columns.put("subCount", "SUB_COUNT");
        columns.put("effCount", "EFF_COUNT");
        //columns.put("sucCount", "SUC_COUNT");
        //columns.put("faiCount", "FAI_COUNT");
        columns.put("msg", "MSG");
        //columns.put("spPwd", "SP_PWD");
        //columns.put("comments", "COMMENTS");
        //columns.put("icount", "ICOUNT");
        //columns.put("errorCodes", "ERROR_CODES");
        //columns.put("timerTime", "TIMER_TIME");
        //columns.put("busCode", "BUS_CODE");
        //columns.put("timerStatus", "TIMER_STATUS");
        //columns.put("isReply", "ISREPLY");
        //columns.put("spNumber", "SPNUMBER");
        //columns.put("subNo", "SUBNO");
        //columns.put("msType", "MS_TYPE");
        //columns.put("isRetry", "ISRETRY");
        //columns.put("rFail2", "RFAIL2");
        //columns.put("icount2", "ICOUNT2");
        columns.put("taskId", "TASKID");
        //columns.put("taskType", "TASKTYPE");
        //columns.put("batchID", "BATCHID");
        //columns.put("validtm", "VALIDTM");
        //长地址
        columns.put("netUrl", "NETURL");
        //短地址
        columns.put("domainUrl", "DOMAIN_URL");
        //有效期
        columns.put("validDays", "VALID_DAYS");
        //发送时间
        columns.put("planTime", "PLAN_TIME");
        //短链生成时间
        columns.put("createTm", "CREATE_TM");
    }

    public ViewBatchVisitVo() {
    }

    public static Map<String, String> getORM() {
        return columns;
    }
}
