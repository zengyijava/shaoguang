package com.montnets.emp.shorturl.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLfMttaskVo {
	protected static final Map<String, String> columns = new LinkedHashMap();

    static {
        columns.put("userId", "USER_ID");
        columns.put("title", "TITLE");
        columns.put("sendstate", "SENDSTATE");
        columns.put("errorCodes", "ERROR_CODES");
        columns.put("spUser", "SP_USER");
        columns.put("mobileUrl", "MOBILE_URL");
        columns.put("subCount", "SUB_COUNT");
        columns.put("effCount", "EFF_COUNT");
        columns.put("sucCount", "SUC_COUNT");
        columns.put("faiCount", "FAI_COUNT");
        columns.put("msg", "MSG");
        columns.put("isReply", "ISREPLY");
        columns.put("icount", "ICOUNT");
        columns.put("icount2", "ICOUNT2");
        columns.put("rFail2", "RFAIL2");
        columns.put("isRetry", "ISRETRY");
        columns.put("timerTime", "TIMER_TIME");
        columns.put("taskId", "TASKID");
        columns.put("taskType", "TASKTYPE");
        columns.put("netUrl", "NETURL");
        columns.put("domainUrl", "DOMAIN_URL");
    }

    public ViewLfMttaskVo() {
    }

    public static Map<String, String> getORM() {
        return columns;
    }
}
