package com.montnets.emp.rms.meditor.biz.imp;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.CheckUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author:yangdl
 * @Data:Created in 16:01 2018.8.8 008
 */
public class DiffMmsBiz {

    private OTTTaskBiz ottTaskBiz = new OTTTaskBiz();

    public String send(HttpServletRequest request) throws Exception {
        String result = "";
        String corpCode = "";
        //获取服务器当前时间
        Date date = Calendar.getInstance().getTime();
        //获取定时状态
        String timerStatus = request.getParameter("timerStatus");

        //发送时间
        String timerTime = request.getParameter("timerTime");
        //特殊处理，如果sendType为空时，则为即时发送。
        if (timerStatus == null || "".equals(timerStatus)) {
            timerStatus = "0";
        }
        //判断定时时间是否合法
        if ("1".equals(timerStatus)) {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
            String serverTime = format1.format(date);
            timerTime = timerTime + ":00";
            if (!format1.parse(serverTime).before(format1.parse(timerTime)) && !format1.parse(serverTime).equals(format1.parse(timerTime))) {
                EmpExecutionContext.error("企业富信相同内容发送，定时发送时间小于服务器当前时间。");
                throw new EMPException(IErrorCode.RM0005);
            }
        }


        //任务ID
        String taskId = request.getParameter("taskId");

        //企业编码
        corpCode = request.getParameter("lgcorpcode");
        //彩信发送账号
        String spUser = request.getParameter("spUser");

        //登录操作员信息
        LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        //操作员、企业编码、SP账号检查
        boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, corpCode, spUser, null);
        if (!checkFlag) {
            EmpExecutionContext.error("静态彩信发送，检查操作员、企业编码、发送账号不通过，taskid:" + taskId
                    + "，corpCode:" + corpCode
                    + "，userid：" + lfSysuser.getUserId()
                    + "，spUser：" + spUser
                    + "，errCode:" + IErrorCode.B20007);
            throw new EMPException(IErrorCode.B20007);
        }

        LfMttask mt = ottTaskBiz.getLfMttask(request);

        if (mt.getTimerStatus() == 1) {
            result = ottTaskBiz.addRmsLfMttaskSend(mt);
        } else {
            // 分隔任务需不需要用异步处理
            if (mt.getEffCount() <= 1000) {

                //调用发送方法，存表（LF_MTTASK）与发送
                result = ottTaskBiz.addRmsLfMttaskSend(mt);
            } else {
                //不同内容发送，超过1000，就由实时改用定时发送。定时状态设置为2.
                mt.setTimerStatus(2);
                mt.setTimerTime(new Timestamp(System.currentTimeMillis() + (30 * 1000)));
                //调用发送方法，存表（LF_MTTASK）与发送
                String timerStr = ottTaskBiz.addRmsLfMttaskSend(mt);
                if (null != timerStr && timerStr.equals("timerSuccess")) {
                    result = "handled";
                } else {
                    result = timerStr;
                }
            }
        }
        return result;
    }
}
