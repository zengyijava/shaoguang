package com.montnets.emp.translog.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.translog.entity.LfTransLog;
import com.montnets.emp.util.PageInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author lianggp
 * @datetime 2021-1-27
 * @description 汇总日志servlet
 */
public class TransLogSvt extends BaseServlet {

    private final String empRoot="xtgl";
    private final String basePath="/translog";
    private final BaseBiz baseBiz=new BaseBiz();

    /**
     * @param request
     * @param response
     * @description 汇总日志查询
     */
    public void find(HttpServletRequest request, HttpServletResponse response) {
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LfSysuser lfSysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
        if (lfSysuser == null){
            return;
        }
        // 日志开始时间
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        long begin_time=System.currentTimeMillis();
        PageInfo pageInfo=new PageInfo();
        // 加请求日志
        EmpExecutionContext.logRequestUrl(request, "后台请求");
        try {
            // 判断是否第一次打开
            boolean isFirstEnter = false;
            isFirstEnter=pageSet(pageInfo, request);
            // 设置查询条件
            if (!isFirstEnter) {
                // 时间
                conditionMap.put("createtime&>=", request.getParameter("sendtime"));
                conditionMap.put("createtime&<=", request.getParameter("recvtime"));
                // 汇总类型
                conditionMap.put("usetype", request.getParameter("usetype"));
                // 日志内容
                conditionMap.put("tstatus&like", request.getParameter("tstatus"));
                // 汇总名称
                conditionMap.put("transname&like",request.getParameter("transname"));
                //执行标志
                conditionMap.put("runflag", request.getParameter("runflag"));
            }

            // 查询结果按照id降序
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("id","desc");
            List<LfTransLog> logList = baseBiz.getByConditionNoCount(LfTransLog.class, null, conditionMap,orderbyMap,pageInfo);

            if (!isFirstEnter) {
                // 查询条件回填
                conditionMap.put("tstatus", request.getParameter("tstatus"));
                conditionMap.put("transname",request.getParameter("transname"));
                conditionMap.put("sendtime", request.getParameter("sendtime"));
                conditionMap.put("recvtime", request.getParameter("recvtime"));
            }

            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("logList", logList);
            request.setAttribute("conditionMap", conditionMap);
            request.setAttribute("beforeTime", (String)request.getParameter("sendtime"));
            request.setAttribute("afterTime", (String)request.getParameter("recvtime"));
            //增加查询日志
            if(pageInfo!=null){
                long end_time=System.currentTimeMillis();
                String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
                opSucLog(request, "汇总转移日志", opContent, "GET");
            }
            request.getRequestDispatcher(this.empRoot + basePath + "/transLog.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"汇总日志跳转失败！");
            request.setAttribute("findresult", "-1");
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("conditionMap", conditionMap);
            try {
                request.getRequestDispatcher(this.empRoot + basePath+"/transLog.jsp")
                        .forward(request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1,"汇总转移日志servlet查询异常");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1,"汇总转移日志servlet查询跳转异常");
            }
        }
    }

    /**
     *
     * @param request
     * @param modName
     * @param opContent
     * @param opType
     * @description 写日志
     */
    private void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
        LfSysuser lfSysuser = null;
        try {
            Object obj = request.getSession(false).getAttribute("loginSysuser");
            if(obj == null) return;
            lfSysuser = (LfSysuser)obj;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "记录操作日志异常,session为空！");
        }
        if(lfSysuser!=null){
            EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
        }else{
            EmpExecutionContext.info(modName,"","","",opContent,opType);
        }
    }

}
