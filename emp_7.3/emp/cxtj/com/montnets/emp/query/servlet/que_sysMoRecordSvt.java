package com.montnets.emp.query.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.query.biz.SysMoHistoryRecordBiz;
import com.montnets.emp.query.biz.SysMoRealTimeRecordBiz;
import com.montnets.emp.query.vo.MoTask01_12Vo;
import com.montnets.emp.query.vo.MoTaskVo;
import com.montnets.emp.query.vo.SysMoMtSpgateVo;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime
 * @description
 */
@SuppressWarnings("serial")
public class que_sysMoRecordSvt extends BaseServlet {

    /**
     * 系统上行记录查询方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    protected final BaseBiz baseBiz = new BaseBiz();

    protected final String empRoot = "cxtj";

    protected final String base = "/query";
    //时分秒格式化
    private final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");

    /**
     * 分页初始化方法
     *
     * @param param
     * @param defaultValue
     * @param request
     * @return
     */
    protected int getIntParameter(String param, int defaultValue, HttpServletRequest request) {
        try {
            if (request.getParameter(param) != null && !"".equals(request.getParameter(param))) {
                return Integer.parseInt(request.getParameter(param));
            } else {
                return defaultValue;
            }
        } catch (NumberFormatException e) {
            EmpExecutionContext.error(e, "获取分页信息异常");
            return defaultValue;
        }
    }

    public void find(HttpServletRequest request, HttpServletResponse response) throws Exception {

    	/*LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
    	if(null == lfSysuser){
    		return;
    	}*/

        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);

        // 记录类型（实时记录，历史记录）
        String recordType = request.getParameter("recordType");
//		// 企业编码
        String corpCode = "";
        // 开始时间
        String sendtime = request.getParameter("sendtime");
        // 结束时间
        String recvtime = request.getParameter("recvtime");
        // 上行回复人姓名
        String addbookname = request.getParameter("addbookname");
        // 回复内容
        String msgContent = request.getParameter("msgContent");
        // 通道号
        String spgate = request.getParameter("spgate");
        // 手机号码
        String phone = request.getParameter("phone");
        // 帐号
        String spuserid = request.getParameter("userid");
        //运营商
        String unicom = request.getParameter("spunicom");
        if (StringUtils.isNotBlank(unicom) && !isNumber(unicom)) {
            unicom="";
        }

        // 查询条件对象
        SysMoMtSpgateVo sysMoMtSpgateVo = new SysMoMtSpgateVo();
        // 通道号LIST
        List<SysMoMtSpgateVo> spList = null;
        // 发送账户
        String spUsers;
        //是否第一次访问
        boolean isFirstEnter = false;
        // 初始化分页数据
        boolean isError = true;
        // 当前登录用户企业绑定帐号LIST
        List<String> lfsp = null;
        PageInfo pageInfo = new PageInfo();
        try {
            QueryBiz qbiz = new QueryBiz();
            //获取登录 sysUser
            LfSysuser sysUser = qbiz.getCurrentUser(request);
            if (sysUser == null) {
                EmpExecutionContext.error("上行记录查询,session中获取当前登录对象出现异常");
                return;
            }
            corpCode = sysUser.getCorpCode();
            //判断企业编码获取
            if (StringUtils.isBlank(corpCode)) {
                EmpExecutionContext.error("上行记录查询,session中获取企业编码出现异常");
                return;
            }
            //判断SP账号是否是属于本企业的
            if (spuserid != null && !"".equals(spuserid.trim()) && !"100000".equals(corpCode)) {
                //多企业才处理
                if (StaticValue.getCORPTYPE() == 1) {
                    boolean checkFlag = new CheckUtil().checkSysuserInCorp(sysUser, corpCode, spuserid, null);
                    if (!checkFlag) {
                        EmpExecutionContext.error("上行记录查询,检查操作员，企业编码，发送账号不通过，spuserid：" + spuserid + ",corpcode=" + corpCode);
                        return;
                    }
                }
            }

            // 查询当前登录用户企业绑定的通道号
            lfsp = qbiz.getSpUserList("0", corpCode, StaticValue.getCORPTYPE());
            spUsers = qbiz.getSpUsers("0", corpCode, StaticValue.getCORPTYPE());

            //十万系统管理员是所有SP账户则不需要SP企业绑定账户条件
            if ("100000".equals(corpCode)) {
                spUsers = null;
            }
            //默认实时
            if (recordType == null) {
                recordType = "realTime";
            }

            // 企业编号
            sysMoMtSpgateVo.setCropCode(corpCode);
            // 查询通道号
            spList = new SysMoRealTimeRecordBiz().getDownHisVos(sysMoMtSpgateVo);
            // 操作员机构修改，多次点击出现org.apache.catalina.connector.Request.parseParameters异常
            isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
            if (isError) {
                isFirstEnter = true;
            } else {
                // 设置分页
                pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
                pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
                isFirstEnter = false;
            }
            //查询类型
            String selecttype = "";
            // 历史
            if ("history".equals(recordType)) {
                selecttype = "历史";
                SysMoHistoryRecordBiz downBiz = new SysMoHistoryRecordBiz();
                MoTask01_12Vo moTask = new MoTask01_12Vo();
                List<MoTask01_12Vo> moTaskList = null;
                // 非第一次查询
                if (!isFirstEnter) {
                    // 通道号
                    moTask.setSpnumber(spgate);
                    // 手机号码
                    moTask.setPhone(phone);
                    // 开始时间
                    moTask.setStartSubmitTime(sendtime);
                    // 结束时间
                    moTask.setEndSubmitTime(recvtime);
                    // 回复人姓名
                    moTask.setName(addbookname);
                    // 回复内容
                    moTask.setMsgContent(msgContent);
                    // sp帐号
                    moTask.setUserId(spuserid);
                    // 企业绑定的SP帐号
                    moTask.setSpUser(spUsers);
                    //运营商
                    if (unicom != null && !"".equals(unicom)) {
                        moTask.setUnicom(Integer.parseInt(unicom));
                    }
                }

                // 获取系统上行历史数据
                moTaskList = downBiz.getMotask01_12Vos(moTask, pageInfo, corpCode);
                request.setAttribute("moHisTaskList", moTaskList);
                request.setAttribute("moTaskHis", moTask);
                request.setAttribute("pageInfo", pageInfo);
            } else if ("realTime".equals(recordType)) {
                selecttype = "实时";
                // 实时
                SysMoRealTimeRecordBiz downBiz = new SysMoRealTimeRecordBiz();
                MoTaskVo moTask = new MoTaskVo();
                List<MoTaskVo> moTaskList = null;
                // 非第一次登录
                if (!isFirstEnter) {
                    // 通道号
                    moTask.setSpnumber(spgate);
                    // 电话号吗
                    moTask.setPhone(phone);
                    // 开始时间
                    moTask.setStartSubmitTime(sendtime);
                    // 结束时间
                    moTask.setEndSubmitTime(recvtime);
                    // 回复人姓名
                    moTask.setName(addbookname);
                    // 回复内容
                    moTask.setMsgContent(msgContent);
                    // 账户
                    moTask.setUserId(spuserid);
                    // 企业绑定的所有账户
                    moTask.setSpUser(spUsers);
                    //运营商
                    if (unicom != null && !"".equals(unicom)) {
                        moTask.setUnicom(Integer.parseInt(unicom));
                    }
                    // 查询实时记录
                    moTaskList = downBiz.getUpInfo(moTask, pageInfo, corpCode);
                }

                // 获取系统上行实时数据
                request.setAttribute("moRealTimeTaskList", moTaskList);
                request.setAttribute("moTask", moTask);
                request.setAttribute("pageInfo", pageInfo);
            }
            request.setAttribute("isFirstEnter", isFirstEnter);
            request.setAttribute("mrUserList", lfsp);
            request.setAttribute("spList", spList);
            long count = 0l;
            //页数
            int pagenum = 1;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
                pagenum = pageInfo.getPageIndex();
            }
            String conditionstr = "spgate:" + spgate + ",spuserid:" + spuserid + ",addbookname:" + addbookname + ",msgContent:" + msgContent
                    + ",phone:" + phone + ",sendtime:" + sendtime + ",recvtime:" + recvtime + ",unicom:" + unicom + ",pageindex:" + pagenum;
            String opContent = "查询上行" + selecttype + "记录：" + count + "条 ,开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms,条件：" + conditionstr + "";
            new QueryBiz().setLog(request, "系统上行记录", opContent, StaticValue.GET);
            request.getRequestDispatcher(empRoot + base + "/que_sysMoRecord.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "系统上行记录查询异常");
            // 异常错误
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("error", e);
            request.setAttribute("findresult", "-1");
            // 分页信息
            request.setAttribute("pageInfo", pageInfo);
            try {
                request.getRequestDispatcher(empRoot + base + "/que_sysMoRecord.jsp").forward(request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1, "系统上行记录查询后跳转异常");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "系统上行记录查询后跳转IO异常");
            }
        }
    }

    private Boolean isNumber(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 系统上行记录excel导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        // 记录类型（实时记录，历史记录）
        String recordType = request.getParameter("recordType");
        // 企业编码
        String corpcode = request.getParameter("lgcorpcode");
        // 用户id
        //String userId = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String userId = SysuserUtil.strLguserid(request);


        // 发送账户
        String spUsers;
        //运营商
        String unicom = request.getParameter("spunicom");
        if (StringUtils.isNotBlank(unicom)) {
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(unicom);
            if (m.matches()) {
                unicom = m.replaceAll("").trim();
            } else {
                throw new NumberFormatException("输入的参数形式不正确");
            }
        }

        String sendtime = request.getParameter("sendtime");
        String recvtime = request.getParameter("recvtime");
//		// 动态拼接SP帐号变量
//		StringBuffer spuser = new StringBuffer();
        // 分页对象
        PageInfo pageInfo = new PageInfo();
        try {
            QueryBiz qbiz = new QueryBiz();
            //当企业编码传入为空则从session中取
            if (corpcode == null || "".equals(corpcode) || "null".equals(corpcode)) {
                corpcode = qbiz.getCorpCode(request);
            }
            spUsers = qbiz.getSpUsers("0", corpcode, StaticValue.getCORPTYPE());

            // 如果是十万号则查询不需要帐号条件
            if ("100000".equals(corpcode)) {
                spUsers = null;
            }

            if (recordType == null) {
                recordType = "realTime";
            }
            response.setContentType("html/text");
            HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
            // 查询历史记录
            if ("history".equals(recordType)) {
                MoTask01_12Vo moTask = new MoTask01_12Vo();
                // 通道号
                moTask.setSpnumber(request.getParameter("spgate"));
                // 手机号
                moTask.setPhone(request.getParameter("phone").trim());
                //时间长度为19位
                if (sendtime != null && sendtime.trim().length() > 0 && sendtime.length() < 20) {
                    moTask.setStartSubmitTime(sendtime);
                }
                if (recvtime != null && recvtime.trim().length() > 0 && recvtime.length() < 20) {
                    moTask.setEndSubmitTime(recvtime);
                }
                if (StringUtils.isNotBlank(request.getParameter("addbookname"))) {

                    moTask.setName(request.getParameter("addbookname"));
                }

                if (StringUtils.isNotBlank(request.getParameter("msgContent"))) {
                    moTask.setMsgContent(request.getParameter("msgContent"));
                }

                if (unicom != null && !"".equals(unicom)) {
                    moTask.setUnicom(Integer.parseInt(unicom));
                }
                moTask.setUserId(request.getParameter("userid"));
                // sp账号
                moTask.setSpUser(spUsers);
                // 获取系统上行历史数据
                Map<String, String> btnMap = (Map<String, String>) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("btnMap");
                int ishidephome = 0;
                if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                    ishidephome = 1;
                }
                Map<String, String> resultMap = new SysMoHistoryRecordBiz().createMoTaskExcel(moTask, pageInfo, ishidephome, corpcode, request);

                if (resultMap != null && resultMap.size() > 0) {
                    // 文件名称
                    String fileName = (String) resultMap.get("FILE_NAME");
                    if (resultMap.get("SIZE") != null) {
                        String conditionstr = "spgate:" + request.getParameter("spgate") + ",spuserid:" + request.getParameter("userid") + ",addbookname:" + request.getParameter("addbookname") + ",msgContent:" + request.getParameter("msgContent")
                                + ",phone:" + request.getParameter("phone") + ",sendtime:" + request.getParameter("sendtime") + ",recvtime:" + request.getParameter("recvtime") + ",unicom:" + unicom;
                        String opContent = "导出上行历史记录：" + resultMap.get("SIZE") + "条,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms,条件：" + conditionstr + " ";
                        new QueryBiz().setLog(request, "系统上行记录", opContent, StaticValue.GET);
                    }
                    session.setAttribute("quemo_export_map", resultMap);
                    response.getWriter().print("true");
                } else {
                    response.getWriter().print("false");
                }

            } else if ("realTime".equals(recordType)) {
                // 查询实时数据
                MoTaskVo moTask = new MoTaskVo();

                moTask.setSpnumber(request.getParameter("spgate"));
                moTask.setPhone(request.getParameter("phone").trim());
                //时间长度为19位
                if (sendtime != null && sendtime.trim().length() > 0 && sendtime.length() < 20) {
                    moTask.setStartSubmitTime(sendtime);
                }
                if (recvtime != null && recvtime.trim().length() > 0 && recvtime.length() < 20) {
                    moTask.setEndSubmitTime(recvtime);
                }
                if (null != request.getParameter("addbookname") && !"".equals(request.getParameter("addbookname"))) {

                    moTask.setName(request.getParameter("addbookname"));
                }

                if (null != request.getParameter("msgContent") && !"".equals(request.getParameter("msgContent"))) {
                    moTask.setMsgContent(request.getParameter("msgContent"));
                }

                if (unicom != null && !"".equals(unicom)) {
                    moTask.setUnicom(Integer.parseInt(unicom));
                }
                
                moTask.setSpUser(spUsers);
                moTask.setUserId(request.getParameter("userid"));

                Map<String, String> btnMap = (Map<String, String>) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("btnMap");
                int ishidephome = 0;
                if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                    ishidephome = 1;
                }
                // 调用创建excel的方法
                Map<String, String> resultMap = new SysMoRealTimeRecordBiz().createMoTaskTimeExcel(moTask, pageInfo, ishidephome, corpcode, request);
                if (resultMap != null && resultMap.size() > 0) {
                    // 文件名称
                    String fileName = (String) resultMap.get("FILE_NAME");
                    if (resultMap.get("SIZE") != null) {
                        String conditionstr = "spgate:" + request.getParameter("spgate") + ",spuserid:" + request.getParameter("userid") + ",addbookname:" + request.getParameter("addbookname") + ",msgContent:" + request.getParameter("msgContent")
                                + ",phone:" + request.getParameter("phone") + ",sendtime:" + request.getParameter("sendtime") + ",recvtime:" + request.getParameter("recvtime") + ",unicom:" + unicom;
                        String opContent = "导出上行实时记录：" + resultMap.get("SIZE") + "条,文件名：" + fileName + " ,开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms,条件：" + conditionstr + "";
                        new QueryBiz().setLog(request, "系统上行记录", opContent, StaticValue.GET);
                    }
                    session.setAttribute("quemo_export_map", resultMap);
                    response.getWriter().print("true");
                } else {
                    response.getWriter().print("false");
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "系统上行记录导出异常");
            response.getWriter().print("false");
        }

    }


    /**
     * 下载导出文件
     *
     * @param request
     * @param response
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("quemo_export_map");
        session.removeAttribute("quemo_export_map");
        if (obj == null) {
            return;
        }

        // 弹出下载页面。
        DownloadFile dfs = new DownloadFile();
        Map<String, String> resultMap = (Map<String, String>) obj;
        String fileName = (String) resultMap.get("FILE_NAME");
        String filePath = (String) resultMap.get("FILE_PATH");
        if (fileName == null || filePath == null) {
            return;
        }

        dfs.downFile(request, response, filePath, fileName);
    }


    protected int getIntParameter(HttpServletRequest request, String param, int defaultValue) {
        try {
            if (StringUtils.isNotBlank(request.getParameter(param))) {
                return Integer.parseInt(request.getParameter(param));
            } else {
                return defaultValue;
            }
        } catch (NumberFormatException e) {
            EmpExecutionContext.error(e, "获取分页信息异常");
            // 异常处理
            return defaultValue;
        }
    }

}
