package com.montnets.emp.common.tools;

import com.montnets.emp.common.constant.StaticValue;

import javax.servlet.http.HttpServletRequest;


public class TemplateUtil {



    public static String showRmsMsg(HttpServletRequest request, MessageUtils.RmsMessage rmsMessage) {
        //MessageUtils.getMsg(StaticValue.ZH_HK, MessageUtils.RmsMessage.TMPLATE_DELETE);

        Object lang = request.getSession().getAttribute(StaticValue.LANG_KEY);
        if (null == lang) {
            lang = StaticValue.ZH_CN;
        }
        return MessageUtils.getMsg(String.valueOf(lang), rmsMessage);
    }


}
