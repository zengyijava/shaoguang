package com.montnets.emp.finance.util;

import com.montnets.emp.i18n.util.MessageUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class YdcwErrorCode {

    public static Map<String,String> getErrorStr(HttpServletRequest request){
        Map<String,String> ErrorCode = new HashMap<String, String>();
        ErrorCode.put("ECWD100", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWD100",request));
        ErrorCode.put("ECWV101", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV101",request));
        ErrorCode.put("ECWB102", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWB102",request));
        ErrorCode.put("ECWB103", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWB103",request));
        ErrorCode.put("ECWV104", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV104",request));
        ErrorCode.put("ECWV105", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV105",request));
        ErrorCode.put("ECWV106", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV106",request));
        ErrorCode.put("ECWV107", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV107",request));
        ErrorCode.put("ECWV108", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV108",request));
        ErrorCode.put("ECWB109", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWB109",request));
        ErrorCode.put("ECWB110", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWB110",request));
        ErrorCode.put("ECWB111", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWB111",request));
        ErrorCode.put("ECWV112", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV112",request));
        ErrorCode.put("ECWV113", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV113",request));
        ErrorCode.put("ECWV114", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV114",request));
        ErrorCode.put("ECWV115", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV115",request));
        ErrorCode.put("RESULT_FORMAT", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_RESULT_FORMAT",request));
        ErrorCode.put("RESULT_BLACKLIST", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_RESULT_BLACKLIST",request));
        ErrorCode.put("RESULT_KEYWORDS", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_RESULT_KEYWORDS",request));
        ErrorCode.put("RESULT_EFFS", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_RESULT_EFFS",request));
        ErrorCode.put("CONNECT_EXEC", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_CONNECT_EXEC",request));
        ErrorCode.put("TEMPLATE_ERROR", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_TEMPLATE_ERROR",request));
        ErrorCode.put("PREVIEW_ERROR", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_PREVIEW_ERROR",request));
        ErrorCode.put("error_code", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_error_code",request));
        ErrorCode.put("repeat_submit_stirng", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_repeat_submit_stirng",request));
        ErrorCode.put("SMSBalance", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_SMSBalance",request));
        ErrorCode.put("ExpandedTail", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ExpandedTail",request));
        ErrorCode.put("GetOperatorBalance", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_GetOperatorBalance",request));
        ErrorCode.put("OperatorBalance", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_OperatorBalance",request));
        ErrorCode.put("SPBalance", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_OperatorBalance",request));
        ErrorCode.put("SMSTimingTask", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_SMSTimingTask",request));

        ErrorCode.put("ECWV106", MessageUtils.extractMessage("ydcw","ydcw_ErrorCode_ECWV106",request));
        return ErrorCode;
    }
}
