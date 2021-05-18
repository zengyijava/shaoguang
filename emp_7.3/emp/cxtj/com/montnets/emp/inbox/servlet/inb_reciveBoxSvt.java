package com.montnets.emp.inbox.servlet;


import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.inbox.biz.ReciveBoxBiz;
import com.montnets.emp.inbox.biz.ReciveBoxExcelTool;
import com.montnets.emp.inbox.vo.LfMotaskVo1;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.util.*;
import org.apache.commons.beanutils.DynaBean;
import org.apache.http.conn.HttpHostConnectException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class inb_reciveBoxSvt extends BaseServlet {

    private final String empRoot = "cxtj";
    private final String basePath = "/inbox";
    private final String path = new TxtFileUtil().getWebRoot();
    /**
     * 模板路径
     */
    protected final String excelPath = path + "cxtj/inbox/file/";

    private final SmsBiz smsBiz = new SmsBiz();
    /**
     * 时分秒格式化
     */
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
            EmpExecutionContext.error(e, "获取分页参数异常");
            return defaultValue;
        }
    }


    /**
     * 个人收件箱查询方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void find(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        ReciveBoxBiz mtBiz = new ReciveBoxBiz();
        List<LfMotaskVo1> moTaskList = null;
        LfSysuser cursysuser = null;
        Map<String, String> phoneNameMap = new LinkedHashMap<String, String>();
        //分页信息类
        boolean isFirstEnter = false;
        PageInfo pageInfo = new PageInfo();
        try {
            boolean isError = true;
            try {
                //操作员机构修改，多次点击出现org.apache.catalina.connector.Request.parseParameters异常
                isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
            } catch (Exception e) {
                EmpExecutionContext.error(e, "获取分页pageIndex异常");
            }
            if (isError) {
                isFirstEnter = true;
            } else {
                //设置分页
                pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
                pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
                isFirstEnter = false;
            }


            String spUser = request.getParameter("spUser");
            String spgate = request.getParameter("spgate");
            String phone = request.getParameter("phone");
            String addrbookname = request.getParameter("addrbookname");
            String msgContent = request.getParameter("msgContent");
            String startTime = request.getParameter("sendtime");
            String endTime = request.getParameter("recvtime");
            //获取当前登录用户的企业编码
            String lgcorpcode = "";
            //String lguserid =request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);

            String lgguid = request.getParameter("lgguid");
            //获取当前登录用户所在企业绑定的sp账号
            QueryBiz qbiz = new QueryBiz();
            //获取登录sysuser
            LfSysuser sysuser = qbiz.getCurrentUser(request);
            if (sysuser == null) {
                EmpExecutionContext.error("个人收件箱,session中获取当前登录对象出现异常");
                return;
            }
            lgcorpcode = sysuser.getCorpCode();
            //判断企业编码获取
            if (lgcorpcode == null || "".equals(lgcorpcode) || "null".equals(lgcorpcode)) {
                EmpExecutionContext.error("个人收件箱,session中获取企业编码出现异常");
                return;
            }
            lguserid = sysuser.getUserId() + "";
            //判断传入的当前登录操作员ID为空则去空
            if (lguserid == null || "".equals(lguserid) || "null".equals(lguserid)) {
                EmpExecutionContext.error("个人收件箱,session中获取当前登录操作员USERID出现异常,lgcorpcode:" + lgcorpcode);
                return;
            }
            lgguid = sysuser.getGuId() + "";
            //判断传入的当前登录操作员GUID为空则去空
            if (lgguid == null || "".equals(lgguid) || "null".equals(lgguid)) {
                EmpExecutionContext.error("个人收件箱,session中获取当前登录操作员GUID出现异常,lguserid:" + lguserid + ",lgcorpcode:" + lgcorpcode);
                return;
            }

            //判断SP账号是否是属于本企业的
            if (spUser != null && !"".equals(spUser.trim())) {
                //多企业才处理
                if (StaticValue.getCORPTYPE() == 1) {
                    boolean checkFlag = new CheckUtil().checkSysuserInCorp(sysuser, lgcorpcode, spUser, null);
                    if (!checkFlag) {
                        EmpExecutionContext.error("个人收件箱,检查操作员，企业编码，发送账号不通过，spuserid：" + spUser + ",corpcode=" + lgcorpcode);
                        return;
                    }
                }
            }

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            LinkedHashMap<Long, String> lfdepMap = new LinkedHashMap<Long, String>();


            BaseBiz baseBiz = new BaseBiz();
            List<String> userList = qbiz.getSpUserList("0", lgcorpcode, StaticValue.getCORPTYPE());

            //获取当前登录用户的数据访问权限
            cursysuser = baseBiz.getById(LfSysuser.class, lguserid);
            //非第一次访问
            if (!isFirstEnter) {

                conditionMap.clear();
                orderbyMap.clear();
                conditionMap.put("corpCode", lgcorpcode);
                orderbyMap.put("deliverTime", "desc");
                conditionMap.put("userGuid", lgguid);
                conditionMap.put("userId", lguserid);

                LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
                if (!lgcorpcode.equals("100000")) {
                    conditionMap1.put("corpCode", lgcorpcode);
                }
                //获取所有的机构信息
                List<LfDep> lfdepList = baseBiz.getByCondition(LfDep.class, conditionMap1, null);

                if (lfdepList != null) {
                    for (LfDep lfdep : lfdepList) {
                        lfdepMap.put(lfdep.getDepId(), lfdep.getDepName());
                    }
                }
                //加载过滤条件
                if (startTime != null && !"".equals(startTime)) {
                    conditionMap.put("deliverTime&>=", startTime);
                }
                if (endTime != null && !"".equals(endTime)) {
                    conditionMap.put("deliverTime&<=", endTime);
                }
                if (phone != null && !"".equals(phone)) {
                    conditionMap.put("phone&like", phone);
                }
                if (addrbookname != null && !"".equals(addrbookname)) {
                    conditionMap.put("employeeName", addrbookname);
                }

                if (msgContent != null && !"".equals(msgContent)) {
                    conditionMap.put("msgContent", msgContent);
                }

                if (spgate != null && !"".equals(spgate)) {
                    conditionMap.put("spnumber", spgate);
                }
                if (spUser != null && !"".equals(spUser)) {
                    conditionMap.put("spUser", spUser);
                }

                //根据条件查询当前用户的所有的上行记录信息
                moTaskList = mtBiz.findLfmotaskRecive_V1(cursysuser, lgcorpcode, conditionMap, orderbyMap, pageInfo);
            }
            LinkedHashMap<String, String> conditionbusMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
            conditionbusMap.put("corpCode&in", "0," + lgcorpcode);
            orconp.put("corpCode", "asc");
            //根据企业编码查询业务类型
            List<LfBusManager> busList = baseBiz.getByCondition(
                    LfBusManager.class, conditionbusMap, orconp);

            request.setAttribute("busList", busList);

            //------------------------------begin查询静态模板
            conditionMap.clear();
            //短信模板
            conditionMap.put("tmpType", "3");
            //有效
            conditionMap.put("tmState", "1");
            //无需审核或审核通过
            conditionMap.put("isPass&in", "0,1");
            //静态模板
            conditionMap.put("dsflag", "0");

            orderbyMap.clear();
            //模板id
            orderbyMap.put("tmid", StaticValue.ASC);
            //查询相同内容静态模板
            List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class, Long.parseLong(lguserid), conditionMap, orderbyMap);
            if (tmpList != null && tmpList.size() > 0) {
                request.setAttribute("tmpList", tmpList);
            }
            //------------------------------end
            //将结果返回前台页面
            request.setAttribute("isFirstEnter", isFirstEnter);
            request.setAttribute("sendUserList", userList);

            if (moTaskList != null && moTaskList.size() > 0) {
                StringBuffer bf = new StringBuffer();
                for (int i = 0; i < moTaskList.size(); i++) {
                    bf.append("'" + moTaskList.get(i).getPhone() + "'");
                    if (i != moTaskList.size() - 1) {
                        bf.append(",");
                    }
                }
//				LinkedHashMap<String, String> conditionMap2 = new LinkedHashMap<String, String>();
//				conditionMap2.put("mobile&in", bf.toString());
//				List<LfEmployee> employees = baseBiz.getByCondition(LfEmployee.class, conditionMap2, null);

                mtBiz.getEmpMapByMobiles(phoneNameMap, bf.toString(), lgcorpcode);
                mtBiz.getCliMapByMobiles(phoneNameMap, bf.toString(), lgcorpcode);

//				List<LfClient> liclients = baseBiz.getByCondition(LfClient.class, conditionMap2, null);
//				if(liclients!=null && liclients.size()>0){
//					for (int j = 0; j < liclients.size(); j++) 
//					{
//						phoneNameMap.put(liclients.get(j).getMobile(), liclients.get(j).getName());
//					}
//				}

            }
            request.setAttribute("phoneNameMap", phoneNameMap);
            request.setAttribute("moTaskList", moTaskList);
            request.setAttribute("lfdepMap", lfdepMap);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("currUser", cursysuser);
            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            String opContent = "收件箱：" + count + "条 ,开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "个人收件箱", opContent, StaticValue.GET);
            request.getRequestDispatcher(empRoot + basePath + "/inb_reciveBox.jsp").forward(request, response);
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "个人收件箱查询异常");
            request.setAttribute("findresult", "-1");
            request.setAttribute("isFirstEnter", true);//回到页面第一次加载时的状态
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(empRoot + basePath + "/inb_reciveBox.jsp").forward(request, response);
        }
    }

    /**
     * 快捷回复功能
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void ksReplay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SuperOpLog spLog = new SuperOpLog();
        //日志信息
        String opModule = StaticValue.SMS_BOX;
        String opSper = StaticValue.OPSPER;
        String opType = StaticValue.ADD;
        //日志内容
        String opContent = "快捷回复";
        PrintWriter out = response.getWriter();
        SubnoManagerBiz subnoManagerBiz = new SubnoManagerBiz();
        SmsBiz smsBiz = new SmsBiz();
        TxtFileUtil txtfileutil = new TxtFileUtil();
        //企业编码
        String corpCode = "";
        //发送者
        String userid = "";
        //当前登录操作员名称
        String opUser = "";
        BaseBiz baseBiz = new BaseBiz();
        try {

            QueryBiz qbiz = new QueryBiz();
            //获取登录sysuser
            LfSysuser sysuser = qbiz.getCurrentUser(request);
            if (sysuser == null) {
                EmpExecutionContext.error("个人收件箱快捷回复,session中获取当前登录对象出现异常");
                return;
            }
            corpCode = sysuser.getCorpCode();
            //判断企业编码获取
            if (corpCode == null || "".equals(corpCode)) {
                EmpExecutionContext.error("个人收件箱快捷回复,session中获取企业编码出现异常");
                return;
            }

            userid = sysuser.getUserId() + "";

            //判断企业编码获取
            if (userid == null || "".equals(userid)) {
                EmpExecutionContext.error("个人收件箱快捷回复,session中获取USERID出现异常");
                return;
            }

            opUser = sysuser.getUserName();

            LfMttask mttask = new LfMttask();
            String moId = request.getParameter("moId");
            //信息内容
            String msg = request.getParameter("msg");
            //替换短信内的特殊字符
            msg = smsBiz.smsContentFilter(msg);
            //操作员签名
            String signStr = request.getParameter("signStr");
            msg = signStr + msg;
            //业务类型
            //String busCode = request.getParameter("busCode");
            String busCode = "M00000";
            //发送级别
            //String priority=request.getParameter("priority");
            String priority = "0";


            LfMotask moTask = baseBiz.getById(LfMotask.class, moId);
            //sp账号
            String sp = moTask.getSpUser();
            //判断SP账号是否是属于本企业的
            if (userid != null && !"".equals(userid.trim())) {
                //多企业才处理
                if (StaticValue.getCORPTYPE() == 1) {
                    boolean checkFlag = new CheckUtil().checkSysuserInCorp(sysuser, corpCode, sp, null);
                    if (!checkFlag) {
                        EmpExecutionContext.error("个人收件箱快捷回复,检查操作员，企业编码，发送账号不通过，spuserid：" + sp + ",corpcode=" + corpCode);
                        return;
                    }
                }
            }


            Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);

            //获取尾号
            String subno = "";
            LfSubnoAllotDetail subnoAllotDetail = null;
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //该用户有绑定尾号
            if (sysuser.getIsExistSubNo() == 1) {
                mttask.setIsReply(2);//回复设置到我的尾号
                // 获取操作员固定尾号
                conditionMap.put("loginId", sysuser.getGuId().toString());
                conditionMap.put("corpCode", corpCode);
                conditionMap.put("menuCode&is null", "isnull");
                conditionMap.put("busCode&is null", "isnull");
                List<LfSubnoAllotDetail> details = baseBiz.getByCondition(LfSubnoAllotDetail.class, conditionMap, null);
                subnoAllotDetail = details != null && details.size() > 0 ? details.get(0) : null;
                if (subnoAllotDetail == null || subnoAllotDetail.getUsedExtendSubno() == null || "".equals(subnoAllotDetail.getUsedExtendSubno())) {
                    //获取尾号失败
                    out.print("noSubNo");
                    return;
                }
                subno = subnoAllotDetail.getUsedExtendSubno();
            } else {
                mttask.setIsReply(1);//回复设置到本次任务
                //重新获取尾号
                SMParams smParams = new SMParams();
                // 编码（0模块编码1业务编码2产品编码3机构id4操作员guid,5任务id）
                smParams.setCodes(taskId.toString());
                //编码类别
                smParams.setCodeType(5);
                smParams.setCorpCode(corpCode);
                //(分配类型0固定1自动有效期7天，null表是不设有效期)
                smParams.setAllotType(1);
                //尾号是否确定插入表
                smParams.setSubnoVali(false);
                smParams.setTaskId(taskId);
                smParams.setLoginId(sysuser.getGuId().toString());
                ErrorCodeParam errorCodeParam = new ErrorCodeParam();
                subnoAllotDetail = GlobalVariableBiz.getInstance().getSubnoDetail(smParams, errorCodeParam);
                if (errorCodeParam.getErrorCode() != null && "EZHB237".equals(errorCodeParam.getErrorCode())) {
                    //没有可用的尾号（尾号已经用完）
                    out.print("noUsedSubNo");
                    return;
                } else if (errorCodeParam.getErrorCode() != null && "EZHB238".equals(errorCodeParam.getErrorCode())) {
                    //获取尾号失败
                    out.print("noSubNo");
                    return;
                }
                subno = subnoAllotDetail != null ? subnoAllotDetail.getUsedExtendSubno() : null;
                if (subno == null || "".equals(subno)) {
                    //获取尾号失败
                    out.print("noSubNo");
                    return;
                }

            }
            String phone = moTask.getPhone();
            //将号码写入文件中
            String[] url = new TxtFileUtil().getSaveUrl(Long.valueOf(userid));
            txtfileutil.writeToTxtFile(url[0], phone);
            //提交号码总数
            mttask.setSubCount(1L);
            //有效号码总数
            mttask.setEffCount(1L);
            //号码文件地址
            mttask.setMobileUrl(url[1]);
            //任务id
            mttask.setTaskId(taskId);
            //主题
            mttask.setTitle("快捷回复");
            //发送账号
            mttask.setSpUser(sp);
            mttask.setBmtType(1);
            //非定时
            mttask.setTimerStatus(0);
            mttask.setTimerTime(mttask.getSubmitTime());
            //信息类型：1－短信  2－彩信
            mttask.setMsType(1);
            mttask.setSubState(2);
            mttask.setBusCode(busCode);
            mttask.setMsg(msg);
            //根据发送类型去判断 短信类型
            mttask.setMsgType(1);
            mttask.setSendstate(0);
            mttask.setCorpCode(corpCode);
            //发送优先级
            mttask.setSendLevel(Integer.parseInt(priority));
            //尾号
            mttask.setSubNo(subno);
            //统计预发送条数
            int yct = 0;
            try {
                yct = new SmsBiz().countAllOprSmsNumber(sp, msg, 1, null, phone);

            } catch (Exception e) {
                EmpExecutionContext.error(e, "预发送条数为零");
                yct = 0;
            }
            //发送短信总条数(网关发送总条数)
            mttask.setIcount(String.valueOf(yct));
            mttask.setUserId(Long.parseLong(userid));

            if (sp != null && !"".equals(sp)) {
                //是否计费
                boolean flag = SystemGlobals.isDepBilling(corpCode);
                // 如果为回复到本次任务，设置尾号的有效期为7天
                if (mttask.getIsReply() == 1) {
                    boolean isSuccess = subnoManagerBiz.updateSubnoStat(mttask.getSubNo(), mttask.getCorpCode(), StaticValue.VALIDITY);
                    if (!isSuccess) {
                        out.print("subnoFailed");
                        return;
                    }
                }
                //调用发送方法
                ReciveBoxBiz mtBiz = new ReciveBoxBiz();
                String isReturn = mtBiz.SendMsgByOne(mttask, flag);
                if (isReturn.equals("2")) {
                    //发送网关失败
                    out.print("sendfalse");
                    return;
                } else if (isReturn.equals("6") || isReturn.equals("9")) {
                    //开启了记费，但短信余额不足
                    out.print("NoMoney");
                    return;
                } else if (isReturn.equals("7")) {
                    //扣费失败
                    out.print("Moneyerror");
                    return;
                } else if (isReturn.equals("uploadFileFail")) {
                    //文件上传失败
                    out.print("uploadFileFail");
                    return;
                } else if (isReturn.equals("1")) {
                    spLog.logSuccessString(opUser, opModule, opType, opContent, corpCode);
                    //发送网关成功
                    out.print("true");
                    return;
                } else if ("noSpInfo".equals(isReturn)) {
                    //查询不到sp账号信息！
                    out.print("noSpInfo");
                } else if ("noSuffiSpFee".equals(isReturn)) {
                    //sp账号余额不足！
                    out.print("noSuffiSpFee");
                } else if ("spFail".equals(isReturn)) {
                    //查询sp账号信息异常！
                    out.print("spFail");
                }

            } else {
                //没有可用的发送账户则不发
                out.print("Nospid");
                return;
            }

        } catch (Exception e) {
            String result;
            if (e instanceof HttpHostConnectException) {
                result = e.getLocalizedMessage();
                //成功日志
                spLog.logSuccessString(opUser, opModule, opType, opContent, corpCode);
            } else {
                result = "error";
                //失败日志
                spLog.logFailureString(opUser, opModule, opType, opContent + opSper, e, corpCode);
            }

            EmpExecutionContext.error(e, "快捷回复操作异常");
            out.print(result);
        }
    }

    /**
     * 个人收件箱导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void exportCurrPageExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        ReciveBoxBiz reciveBoxBiz = new ReciveBoxBiz();
        //获取页面上的查询条件
        //获取发送账号
        String spUser = request.getParameter("spUser");
        String phone = request.getParameter("phone");
        String addrbookname = request.getParameter("addrbookname");
        String msgContent = request.getParameter("msgContent");
        String startTime = request.getParameter("sendtime");
        String endTime = request.getParameter("recvtime");
        String lgcorpcode = request.getParameter("lgcorpcode");
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);


        String lgguid = request.getParameter("lgguid");
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        LinkedHashMap<Long, String> lfdepMap = new LinkedHashMap<Long, String>();
        BaseBiz baseBiz = new BaseBiz();
        try {
            QueryBiz qbiz = new QueryBiz();
            //当传入企业编码为空则从session中取
            if (lgcorpcode == null || "".equals(lgcorpcode) || "null".equals(lgcorpcode)) {
                lgcorpcode = qbiz.getCorpCode(request);
            }
            //判断传入的当前登录操作员ID为空则去空
            if (lguserid == null || "".equals(lguserid) || "null".equals(lguserid)) {
                lguserid = qbiz.getUserId(request);
            }

            //判断传入的当前登录操作员GUID为空则去空
            if (lgguid == null || "".equals(lgguid) || "null".equals(lgguid)) {
                lgguid = qbiz.getGuid(request);
            }

            //企业编码
            conditionMap.put("corpCode", lgcorpcode);
            orderbyMap.put("deliverTime", "desc");
            conditionMap.put("userGuid", lgguid);
            conditionMap.put("userId", lguserid);
            //得到机构
            List<LfDep> lfdepList = baseBiz.getEntityList(LfDep.class);
            //如果机构不为空
            if (lfdepList != null) {
                for (LfDep lfdep : lfdepList) {
                    lfdepMap.put(lfdep.getDepId(), lfdep.getDepName());
                }
            }
            //发送时间
            if (startTime != null && !"".equals(startTime)) {
                conditionMap.put("deliverTime&>=", startTime);
            }
            if (endTime != null && !"".equals(endTime)) {
                conditionMap.put("deliverTime&<=", endTime);
            }
            if (phone != null && !"".equals(phone)) {
                conditionMap.put("phone&like", phone);
            }
            if (addrbookname != null && !"".equals(addrbookname)) {
                conditionMap.put("employeeName", addrbookname);
            }

            if (msgContent != null && !"".equals(msgContent)) {
                conditionMap.put("msgContent", msgContent);
            }
            if (spUser != null && !"".equals(spUser)) {
                conditionMap.put("spUser", spUser);
            }
            //获取当前登录用户的数据访问权限
            LfSysuser cursysuser = baseBiz.getById(LfSysuser.class, lguserid);

            List<LfMotaskVo1> moTaskList = reciveBoxBiz.findLfmotaskRecive_V1(cursysuser, lgcorpcode, conditionMap, orderbyMap, null);
            response.setContentType("html/text");
            PrintWriter out = response.getWriter();
            if (moTaskList != null && moTaskList.size() > 0) {
                //LfSysuser currUser = baseBiz.getById(LfSysuser.class, lguserid);
                ReciveBoxExcelTool et = new ReciveBoxExcelTool(excelPath);
                Map<String, String> btnMap = (Map<String, String>) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("btnMap");
                int ishidephome = 0;
                if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                    ishidephome = 1;
                }
                //导出excel
                Map<String, String> resultMap = et.createReciveBoxExcel(moTaskList, lfdepMap, ishidephome, lgcorpcode, request);
                String fileName = (String) resultMap.get("FILE_NAME");
                String opContent = "导出个人收件箱记录：" + moTaskList.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
                new QueryBiz().setLog(request, "个人收件箱", opContent, StaticValue.GET);
                //EmpExecutionContext.info("企业：" +corpcode+",操作员：" +opUser+opContent+"成功");
                moTaskList.clear();
                moTaskList = null;
                lfdepMap.clear();
                lfdepMap = null;
                HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
                session.setAttribute("inbrev_export_map", resultMap);
                out.print("true");
//		        String filePath=(String)resultMap.get("FILE_PATH");
//		        //下载导出文件
//		        DownloadFile dfs=new DownloadFile();
//		        dfs.downFile(request, response, filePath, fileName);
            } else {
                //如果无导出内容,则跳转到find方法
                out.print("false");
                //response.sendRedirect(request.getContextPath()+"/inb_reciveBox.htm?lguserid="+lguserid+"&lgcorpcode="+lgcorpcode);
            }
        } catch (Exception e) {
            //打印错误信息
            //如果无导出内容,则跳转到find方法
            EmpExecutionContext.error(e, "个人收件箱导出servlet异常");
            response.sendRedirect(request.getContextPath() + "/inb_reciveBox.htm?lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode);
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
        Object obj = session.getAttribute("inbrev_export_map");
        session.removeAttribute("inbrev_export_map");
        if (obj != null) {
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String fileName = (String) resultMap.get("FILE_NAME");
            String filePath = (String) resultMap.get("FILE_PATH");
            dfs.downFile(request, response, filePath, fileName);
        }
    }


    /**
     * 根据账户获取账户绑定的路由信息拆分规则
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getGtInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String spUser = request.getParameter("spUser");
        String[] gtInfos = new String[]{"", "", ""};
        int index = 0;
        int maxLen;
        int totalLen;
        int lastLen;
        int signLen;

        String info = "infos:";
        try {
            List<GtPortUsed> gtPortsList = new SmsBiz().getPortByUserId(spUser);
            for (GtPortUsed gtPort : gtPortsList) {
                index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();

                maxLen = gtPort.getMaxwords();
                totalLen = gtPort.getMultilen1();
                lastLen = gtPort.getMultilen2();
                signLen = gtPort.getSignlen() == 0 ? gtPort.getSignstr().trim().length() : gtPort.getSignlen();

                gtInfos[index] = String.valueOf(maxLen) + "," + String.valueOf(totalLen) +
                        "," + String.valueOf(lastLen) + "," + String.valueOf(signLen);
            }
            info += gtInfos[0] + "&" + gtInfos[1] + "&" + gtInfos[2] + "&";
        } catch (Exception e) {
            info = "error";
            EmpExecutionContext.error(e, "根据账户获取账户绑定的路由信息拆分规则异常");
        } finally {
            response.getWriter().print(info);
            //writer.print(info);
        }
    }

    /**
     * 获取通道信息,包括英文短信配置参数
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getSpGateConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String spUser = request.getParameter("spUser");
        String[] gtInfos = new String[]{"", "", "", ""};
        int index = 0;
        int maxLen;
        int totalLen;
        int lastLen;
        int signLen;
        int enmaxLen;
        int entotalLen;
        int enlastLen;
        int ensignLen;
        int gateprivilege = 0;
        int gatepri;
        //签名位置,1:前置;0:后置
        int signLocation = 0;
        int ensinglelen;
        int msgLen = 990;
        String info = "infos:";
        try {
            // 根据发送账号获取路由信息
            List<DynaBean> spGateList = smsBiz.getSpGateInfo(spUser);
            for (DynaBean spGate : spGateList) {
                gateprivilege = 0;
                //中文短信配置参数
                maxLen = Integer.parseInt(spGate.get("maxwords").toString());
                totalLen = Integer.parseInt(spGate.get("multilen1").toString());
                lastLen = Integer.parseInt(spGate.get("multilen2").toString());
                signLen = Integer.parseInt(spGate.get("signlen").toString());
                // 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
                if (signLen == 0) {
                    signLen = spGate.get("signstr").toString().trim().length();
                }
                //英文短信配置参数
                enmaxLen = Integer.parseInt(spGate.get("enmaxwords").toString());
                entotalLen = Integer.parseInt(spGate.get("enmultilen1").toString());
                enlastLen = Integer.parseInt(spGate.get("enmultilen2").toString());
                ensinglelen = Integer.parseInt(spGate.get("ensinglelen").toString());

                //是否支持英文短信，1：支持；0：不支持
                gatepri = Integer.parseInt(spGate.get("gateprivilege").toString());
                index = Integer.parseInt(spGate.get("spisuncm").toString());
                //电信
                if (index == 21) {
                    index = 2;
                    //国外通道
                } else if (index == 5) {
                    index = 3;
                    //是否支持英文短信，1：支持；0：不支持
                    if ((gatepri & 2) == 2) {
                        gateprivilege = 1;
                        //国外通道英文短信最大长度
                        msgLen = enmaxLen - 20;
                    } else {
                        gateprivilege = 0;
                        //国外通道中文短信最大长度
                        msgLen = maxLen - 10;
                    }
                }
                //签名位置,1:前置;0:后置
                if ((gatepri & 4) == 4) {
                    signLocation = 1;
                } else {
                    signLocation = 0;
                }
                //英文短信签名长度
                ensignLen = Integer.parseInt(spGate.get("ensignlen").toString());
                // 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
                if (ensignLen == 0) {
                    if (index == 3) {
                        //国外通道英文短信签名长度
                        ensignLen = smsBiz.getenSmsSignLen(spGate.get("ensignstr").toString().trim());
                    } else {
                        //国内通道英文短信签名长度
                        ensignLen = spGate.get("ensignstr").toString().trim().length();
                    }
                }
                gtInfos[index] = new StringBuffer().append(maxLen).append(",").append(totalLen).append(",")
                        .append(lastLen).append(",").append(signLen).append(",").append(enmaxLen).append(",")
                        .append(entotalLen).append(",").append(enlastLen).append(",").append(ensignLen)
                        .append(",").append(gateprivilege).append(",").append(ensinglelen).append(",").append(signLocation).toString();
            }
            info += gtInfos[0] + "&" + gtInfos[1] + "&" + gtInfos[2] + "&" + gtInfos[3] + "&" + msgLen + "&";
        } catch (Exception e) {
            info = "error";
            EmpExecutionContext.error(e, "获取发送账户绑定的通道信息异常！");
        } finally {
            response.getWriter().print(info);
        }
    }

    /**
     * 获取短信模板内容
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getTmMsg1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //发送模块获取模板（解决断网断库session超时用）

        String result = null;
        //模板id
        String tmId = request.getParameter("tmId");
        try {
            if ("".equals(tmId)) {
                result = "";
            } else {
                //根据id查询模板记录
                BaseBiz baseBiz = new BaseBiz();
                LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
                result = template.getTmMsg();
            }
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "获取模板内容异常");
        } finally {
            //异步返回操作结果
            response.getWriter().print("@" + result);
        }
    }

}
