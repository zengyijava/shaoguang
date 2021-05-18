package com.montnets.emp.gateway.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.gateway.AgwParamConf;
import com.montnets.emp.entity.gateway.AgwParamValue;
import com.montnets.emp.entity.gateway.AgwSpBind;
import com.montnets.emp.entity.gateway.AprotocolTmpl;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.gateway.biz.GatewayBiz;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.biz.PageFieldConfigBiz;
import com.montnets.emp.servmodule.txgl.entity.*;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通道账户管理
 *
 * @author liaojirong <ljr0300@163.com>
 * @project p_txgl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-3-5 上午09:50:20
 * @description
 */
@SuppressWarnings("serial")
public class gat_userdataSvt extends BaseServlet {
    final ErrorLoger errorLoger = new ErrorLoger();
    //操作模块
    final String opModule = StaticValue.GATE_CONFIG;
    final String opSper = StaticValue.OPSPER;

    // sp账号具有短信能力
    private static final int DX_ABILITY =  0x00000001;
    // sp账号取消短信能力
    private static final int DX_DEBILITY =  0xFFFFFFFE;
    // sp账号具有彩信能力
    private static final int CX_ABILITY =  0x00000002;
    // sp账号取消彩信能力
    private static final int CX_DEBILITY =  0xFFFFFFFD;
    // sp账号具有富信能力
    private static final int FX_ABILITY =  0x00000004;
    // sp账号取消富信能力
    private static final int FX_DEBILITY =  0xFFFFFFFB;

    private final String empRoot = "txgl";

    private final String basePath = "/gateway";

    private final GatewayBiz gatewayBiz = new GatewayBiz();

    /**
     * 通道账户管理查询
     *
     * @param request
     * @param response
     */
    public void find(HttpServletRequest request, HttpServletResponse response) {
        long stime = System.currentTimeMillis();
        PageInfo pageInfo = new PageInfo();
        BaseBiz baseBiz = new BaseBiz();
        //获取系统定义的短彩类型值
        PageFieldConfigBiz gcBiz = new PageFieldConfigBiz();
        List<LfPageField> pagefileds = gcBiz.getPageFieldById("100004");
        try {
            List<AgwSpBind> spBindList = baseBiz.getEntityList(AgwSpBind.class);
            List<XtGateQueue> gateList = baseBiz.getEntityList(XtGateQueue.class);

            LinkedHashMap<String, String> spBindMap = new LinkedHashMap<String, String>();
            if (spBindList != null && spBindMap != null) {
                for (int s = 0; s < spBindList.size(); s++) {
                    AgwSpBind spBind = spBindList.get(s);
                    String htmStr = "";
                    for (int g = 0; g < gateList.size(); g++) {
                        XtGateQueue gate = gateList.get(g);
                        if (spBind.getGateId() - gate.getId() == 0) {
                            htmStr = gate.getSpgate() + "；";
                        }
                    }
                    String mapKey = spBind.getPtAccUid().toString();
                    spBindMap.put(mapKey, spBindMap.get(mapKey) == null ? htmStr : spBindMap.get(mapKey) + htmStr);
                }
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //conditionMap.put("uid&>", "100001");
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            //通道账号
            String gt_userid = null;
            //账户类型
            String accouttype = null;
            //账户状态
            String status = null;
            //通道
            String gt_spgate = null;
            //通道id
            String gate_id = null;
            //通道账户名称
            String staffName = null;
            //运营商账户id
            String spaccid = null;
            //网关编号
            String gwno = null;
            //网关状态
            String gwstate = null;
            //主备网关
            String gwbak = null;

            boolean isBack = request.getParameter("isback") == null ? false : true;//是否返回操作
            HttpSession session = request.getSession(false);
            if (isBack) {
                pageInfo = (PageInfo) session.getAttribute("gat_pageinfo");
                gt_userid = session.getAttribute("gat_gt_userid") == null ? "" : String.valueOf(session.getAttribute("gat_gt_userid"));
                accouttype = session.getAttribute("gat_accouttype") == null ? "" : String.valueOf(session.getAttribute("gat_accouttype"));
                status = session.getAttribute("gat_status") == null ? "" : String.valueOf(session.getAttribute("gat_status"));
                gate_id = session.getAttribute("gat_gate_id") == null ? "" : String.valueOf(session.getAttribute("gat_gate_id"));
                staffName = session.getAttribute("gat_staffName") == null ? "" : String.valueOf(session.getAttribute("gat_staffName"));
                spaccid = session.getAttribute("gat_spaccid") == null ? "" : String.valueOf(session.getAttribute("gat_spaccid"));
                gt_spgate = session.getAttribute("gat_gt_spgate") == null ? "" : String.valueOf(session.getAttribute("gat_gt_spgate"));
                gwno = session.getAttribute("gat_gwno") == null ? "" : String.valueOf(session.getAttribute("gat_gwno"));
                gwstate = session.getAttribute("gat_gwstate") == null ? "" : String.valueOf(session.getAttribute("gat_gwstate"));
                gwbak = session.getAttribute("gat_gwbak") == null ? "" : String.valueOf(session.getAttribute("gat_gwbak"));
                request.setAttribute("gt_userid", gt_userid);
                request.setAttribute("accouttype", accouttype);
                request.setAttribute("status", status);
                request.setAttribute("gate_id", gate_id);
                request.setAttribute("staffName", staffName);
                request.setAttribute("spaccid", spaccid);
                request.setAttribute("gt_spgate", gt_spgate);
                request.setAttribute("gwno", gwno);
                request.setAttribute("gwstate", gwstate);
                request.setAttribute("gwbak", gwbak);

            } else {
                pageSet(pageInfo, request);
                gt_userid = request.getParameter("gt_userid");
                accouttype = request.getParameter("accouttype");
                status = request.getParameter("status");
                gate_id = request.getParameter("gate_id");
                staffName = request.getParameter("staffName");
                spaccid = request.getParameter("spaccid");
                gt_spgate = request.getParameter("gt_spgate");
                gwno = request.getParameter("gwno");
                gwstate = request.getParameter("gwstate");
                gwbak = request.getParameter("gwbak");
            }
            session.setAttribute("gat_pageinfo", pageInfo);
            session.setAttribute("gat_gt_userid", gt_userid);
            session.setAttribute("gat_accouttype", accouttype);
            session.setAttribute("gat_status", status);
            session.setAttribute("gat_gate_id", gate_id);
            session.setAttribute("gat_staffName", staffName);
            session.setAttribute("gat_spaccid", spaccid);
            session.setAttribute("gat_gt_spgate", gt_spgate);
            session.setAttribute("gat_gwno", gwno);
            session.setAttribute("gat_gwstate", gwstate);
            session.setAttribute("gat_gwbak", gwbak);
            if (gt_userid != null && !"".equals(gt_userid)) {
                conditionMap.put("userId", gt_userid);
            }
/*            if (accouttype != null && !"".equals(accouttype)) {
                conditionMap.put("accouttype", accouttype);
            }*/
            if("1".equals(accouttype)){
                conditionMap.put("accouttype", "1");
                conditionMap.put("accability", "0x00000001=1");
            }else if("2".equals(accouttype)){
                conditionMap.put("accouttype", "2");
                conditionMap.put("accability", "0x00000002=2");
            }else if("4".equals(accouttype)){
                conditionMap.put("accouttype", "1");
                conditionMap.put("accability", "0x00000004=4");
            }
            if (status != null && !"".equals(status)) {
                conditionMap.put("status", status);
            }
            if (staffName != null && !"".equals(staffName)) {
                conditionMap.put("staffName", staffName);
            }
            if (spaccid != null && !"".equals(spaccid)) {
                conditionMap.put("spaccid", spaccid);
            }
            if (gate_id != null && !"".equals(gate_id)) {
                conditionMap.put("gateid", gate_id);
            }
            if (gwno != null && !"".equals(gwno)) {
                conditionMap.put("gwno", gwno);
            }
            if (gwstate != null && !"".equals(gwstate)) {
                conditionMap.put("gwstate", gwstate);
            }
            if (gwbak != null && !"".equals(gwbak)) {
                conditionMap.put("gwbak", gwbak);
            }

            List<DynaBean> userList = new GatewayBiz().getAccountList(conditionMap, pageInfo);
            orderMap.clear();
            conditionMap.clear();
            orderMap.put("userId", StaticValue.ASC);
            conditionMap.put("userType", "1");
            List<Userdata> userListCon = baseBiz.getByCondition(Userdata.class, conditionMap, orderMap);

            List<AgwAccount> accountList = baseBiz.getEntityList(AgwAccount.class);

            orderMap.clear();
            orderMap.put("ptaccuid", StaticValue.DESC);
            orderMap.put("gweight", StaticValue.ASC);
            orderMap.put("gwno", StaticValue.ASC);
            List<GWCluSpBind> gwCluSpBinds = baseBiz.findListByCondition(GWCluSpBind.class, null, orderMap);
            Map<String, String> clusMap = new HashMap<String, String>();
            //遍历中的主网关
            String gwNo = null;
            //遍历中的uid
            Integer uid = null;
            for (GWCluSpBind cluSpBind : gwCluSpBinds) {
                if (!cluSpBind.getPtaccuid().equals(uid)) {
                    uid = cluSpBind.getPtaccuid();
                    gwNo = cluSpBind.getGwno().toString();
                } else {
                    String _gwno = cluSpBind.getGwno().toString();
                    clusMap.put(_gwno, gwNo + "");
                    String tmp = clusMap.get(gwNo) == null ? _gwno : (clusMap.get(gwNo) + "、" + _gwno);
                    clusMap.put(gwNo, tmp);
                }
            }

            Map<String, AgwAccount> accountMap = new LinkedHashMap<String, AgwAccount>();
            for (AgwAccount agwAccount : accountList) {
                accountMap.put(agwAccount.getPtAccUid().toString(), agwAccount);
            }
            //加密后的集合
            Map<String, String> keyIdMap = new HashMap<String, String>();
            //ID加密
            if (userList != null && userList.size() > 0) {
                //加密对象
                ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
                //加密对象不为空
                if (encryptOrDecrypt != null) {
                    boolean result = encryptOrDecrypt.batchEncryptByDynaBeanToMap(userList, "uid", keyIdMap);
                    if (!result) {
                        EmpExecutionContext.error("查询通道账户管理列表，参数加密失败。");
                        throw new Exception("查询通道账户管理列表，参数加密失败。");
                    }
                } else {
                    EmpExecutionContext.error("查询通道账户管理列表，从session中获取加密对象为空！");
                    userList.clear();
                    throw new Exception("查询通道账户管理列表，获取加密对象失败。");
                }
            }
            request.setAttribute("keyIdMap", keyIdMap);
            request.setAttribute("userList", userList);
            request.setAttribute("userListCon", userListCon);
            request.setAttribute("gateList", gateList);
            request.setAttribute("spBindMap", spBindMap);
            request.setAttribute("accountMap", accountMap);
            request.setAttribute("clusMap", clusMap);
            request.getSession(false).setAttribute("gat_pagefileds", pagefileds);
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(this.empRoot + this.basePath + "/gat_userdata.jsp")
                    .forward(request, response);
            DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String sDate = sdf.format(stime);
            setLog(request, "通道账户管理", "(" + sDate + ")查询", StaticValue.GET);
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "通道账户管理查询异常"));
            request.setAttribute("findresult", "-1");
            request.setAttribute("pageInfo", pageInfo);
            try {
                request.getSession(false).setAttribute("gat_pagefileds", pagefileds);
                request.getRequestDispatcher(this.empRoot + this.basePath + "/gat_userdata.jsp")
                        .forward(request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(errorLoger.getErrorLog(e1, "通道账户管理查询servlet异常！"));
            } catch (IOException e1) {
                EmpExecutionContext.error(errorLoger.getErrorLog(e1, "通道账户管理查询servlet跳转异常！"));
            }
        }
    }

    /**
     * 获取通讯协议
     *
     * @param request
     * @param response
     */
    public void getPrTmplOption(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String accouttype = request.getParameter("accouttype");
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("protocol", "asc");
            List<AprotocolTmpl> tmplList = new BaseBiz().getByCondition(AprotocolTmpl.class, null, orderbyMap);
            String empLangName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
            String pleaseSelect = "zh_HK".equals(empLangName) ? "Please select" : "zh_TW".equals(empLangName) ? "請選擇" : "请选择";
            String html = "<option value=''>===" + pleaseSelect + "===</option>";
            String otherStr = "";
            for (int i = 0; i < tmplList.size(); i++) {
                int proCode = tmplList.get(i).getProtocolCode();
                if (accouttype != null && "2".equals(accouttype)) {
                    if (proCode == 50) {
                        otherStr += "<option  value='" + tmplList.get(i).getProtocolCode() + "'>" + tmplList.get(i).getProtocol() + "</option>";
                    }
                    if (proCode > 100) {
                        html += "<option  value='" + tmplList.get(i).getProtocolCode() + "'>" + tmplList.get(i).getProtocol() + "</option>";
                    }
                } else {
                    if (proCode < 100) {
                        if (proCode == 50) {
                            otherStr += "<option  value='" + tmplList.get(i).getProtocolCode() + "'>" + tmplList.get(i).getProtocol() + "</option>";
                        } else {
                            html += "<option  value='" + tmplList.get(i).getProtocolCode() + "'>" + tmplList.get(i).getProtocol() + "</option>";
                        }
                    }
                }
            }
            writer.print(html + otherStr);
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "获取通讯协议异常！"));
            writer.print("");
        }
    }

    /**
     * 获取通讯协议配置属性
     *
     * @param request
     * @param response
     */
    public void getPrTmplContent(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String protocolCode = request.getParameter("protocolCode");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("protocolCode", protocolCode);
            List<AprotocolTmpl> tmplList = new BaseBiz().getByCondition(AprotocolTmpl.class, conditionMap, null);

            if (tmplList != null && tmplList.size() > 0) {
                writer.print(tmplList.get(0).getProtocolParam() == null ? "" : tmplList.get(0).getProtocolParam());
            } else {
                writer.print("");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "通讯协议配置属性异常！"));
            if (writer != null) {
                writer.print("");
            }
        }
    }

    /**
     * 通道账户管理修改跳转页面
     *
     * @param request
     * @param response
     */
    public void toEdit(HttpServletRequest request, HttpServletResponse response) {
        //String uid = request.getParameter("uid");
        String uid;
        String accouTtype = request.getParameter("accouTtype");
        BaseBiz baseBiz = new BaseBiz();
        try {
            String keyId = request.getParameter("keyId");
            //加密对象
            ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
            //加密对象不为空
            if (encryptOrDecrypt != null) {
                //解密
                uid = encryptOrDecrypt.decrypt(keyId);
                if (uid == null) {
                    EmpExecutionContext.error("修改短彩sp账户，参数解密码失败，keyId:" + keyId);
                    throw new Exception("修改短彩sp账户，参数解密码失败。");
                }
            } else {
                EmpExecutionContext.error("修改短彩sp账户，从session中获取加密对象为空！");
                throw new Exception("修改短彩sp账户，获取加密对象失败。");
            }
            Userdata userdata = baseBiz.getById(Userdata.class, uid);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("ptAccUid", uid);
            List<AgwAccount> accountList = baseBiz.getByCondition(AgwAccount.class, conditionMap, null);
            AgwAccount account = null;
            if (accountList != null && accountList.size() > 0) {
                account = accountList.get(0);
            }
            LfSpFee spFee = null;
            if (account != null) {
                conditionMap.clear();
                conditionMap.put("spUser", account.getSpAccid());
                conditionMap.put("accountType", accouTtype);
                List<LfSpFee> spFeeList = baseBiz.getByCondition(LfSpFee.class, conditionMap, null);
                if (spFeeList != null && spFeeList.size() > 0) {
                    spFee = spFeeList.get(0);
                }
            }
            request.setAttribute("spFee", spFee);

            request.setAttribute("userdata", userdata);
            request.setAttribute("account", account);
            //短信sp账号则查出主备 多连路连接
            if ("1".equals(accouTtype) && account != null) {
                conditionMap.clear();
                conditionMap.put("ptaccid", account.getPtAccId());
                List<GwGateconninfo> gwgateconinfos = baseBiz.getByCondition(GwGateconninfo.class, conditionMap, null);
                request.setAttribute("gwgcinfos", gwgateconinfos);
            }

            //获取运营商备用URL
            List<String> bakUrlList = new ArrayList<String>();
            conditionMap.clear();
            conditionMap.put("globalKey", "BALANCEBAKURL");
            List<LfGlobalVariable> globalVariableList = new BaseBiz().getByCondition(LfGlobalVariable.class, conditionMap, null);
            if (globalVariableList != null && globalVariableList.size() > 0) {
                String balanceBakRrl = globalVariableList.get(0).getGlobalStrValue();
                if (balanceBakRrl != null && balanceBakRrl.trim().length() > 0) {
                    EmpExecutionContext.info("跳转通道账户管理修改页面，获取查询运营商备用URL为:" + balanceBakRrl);
                    //有多个查询运营商余额备用地址
                    if (balanceBakRrl.indexOf("@") >= 0) {
                        //获取查询运营商余额备用地址
                        String[] url = balanceBakRrl.split("@");
                        //设置查询运营商余额备用地址
                        for (int i = 0; i < url.length; i++) {
                            bakUrlList.add(url[i]);
                        }
                    }
                    //一个查询运营商余额备用地址
                    else {
                        bakUrlList.add(balanceBakRrl);
                    }
                } else {
                    EmpExecutionContext.info("跳转通道账户管理修改页面，获取查询运营商备用URL为空。");
                }
            }
            request.setAttribute("bakUrlList", bakUrlList);
            request.getRequestDispatcher(this.empRoot + this.basePath + "/gat_editUserdata.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "通道账户管理修改跳转异常！"));
        }
    }

    /**
     * 通道账户绑定
     *
     * @param request
     * @param response
     */
    public void toSpBind(HttpServletRequest request, HttpServletResponse response) {
        BaseBiz baseBiz = new BaseBiz();
        try {
            List<AgwSpBind> bindList = baseBiz.getEntityList(AgwSpBind.class);

            String gateIds = "";
            if (bindList != null && bindList.size() > 0) {
                for (int a = 0; a < bindList.size(); a++) {
                    gateIds += bindList.get(a).getGateId().toString() + ",";
                }
            }

            gateIds = gateIds.length() > 0 ? gateIds.substring(0, gateIds.length() - 1) : "";
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("id&not in", gateIds);
            String accounttype = request.getParameter("accounttype");
            if ("1".equals(accounttype) || "2".equals(accounttype)) {
                conditionMap.put("gateType", accounttype);
            }

            List<XtGateQueue> nogateList = baseBiz.getByCondition(XtGateQueue.class, conditionMap, null);

            //未绑定的通道号
            request.setAttribute("NogateList", nogateList);

            //获取当前通道账户已绑定的所有的通道
            String uid = request.getParameter("uid");
            conditionMap.clear();
            conditionMap.put("ptAccUid", uid);

            List<AgwSpBind> bindAgw = baseBiz.getByCondition(AgwSpBind.class, conditionMap, null);
            gateIds = "";
            if (bindAgw != null && bindAgw.size() > 0) {
                for (int a = 0; a < bindAgw.size(); a++) {
                    gateIds += bindAgw.get(a).getGateId().toString() + ",";
                }

                gateIds = gateIds.length() > 0 ? gateIds.substring(0, gateIds.length() - 1) : "";
                conditionMap.clear();
                conditionMap.put("id&in", gateIds);
                List<XtGateQueue> gateList = baseBiz.getByCondition(XtGateQueue.class, conditionMap, null);
                request.setAttribute("gateList", gateList);
            }

            request.getRequestDispatcher(this.empRoot + this.basePath + "/gat_spBind.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "通道账户绑定跳转异常！"));
        }
    }

    /**
     * 根据通道号获取通道信息信息详情
     *
     * @param request
     * @param response
     */
    public void getPassage(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String id = request.getParameter("id");
            XtGateQueue gate = new BaseBiz().getById(XtGateQueue.class, id);
            String html = "";
            if (gate != null) {
                html = "";
                html = "<table style='width:200px;border: 0px;' ><tr><td>通道名称：" + gate.getGateName() + "</td></tr>";
                int spisuncm = gate.getSpisuncm();
                html += "<tr><td>运营商：" + (spisuncm == 0 ? "移动" : spisuncm == 1 ? "联通" : spisuncm == 21 ? "电信" : "国外") + "</td></tr>";
                html += "<tr><td>单条短信长度：" + gate.getSingleLen() + "</td></tr>";
                html += "<tr><td>最大短信长度：" + gate.getMaxWords() + "</td></tr></tr>";
                html += "<tr><td>第一条短信长度：" + gate.getMultilen1() + "</td></tr>";
                html += "<tr><td>最后一条短信长度：" + gate.getMultilen2() + "</td></tr>";
                html += "<tr><td>短信签名：" + gate.getSignstr() + "</td></tr>";
                html += "<tr><td>最大扩展子号位数：" + gate.getSublen() + "</td></tr></table>";
            }
            writer.print(html);
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "根据通道号获取通道信息详情异常！"));
            if (writer != null) {
                writer.print("");
            }
        }
    }


    /**
     * 绑定通道时获取通道账户
     *
     * @param request
     * @param response
     */
    public void getMsGate(HttpServletRequest request, HttpServletResponse response) {
        BaseBiz baseBiz = new BaseBiz();
        PrintWriter writer = null;
        try {
            String mobile = MessageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_yd", request);
            String telecom = MessageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_dx", request);
            String unicom = MessageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_lt", request);
            String foreign = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_gw", request);
            String binded = "移动".equals(mobile) ? "已绑" : "Mobile".equals(mobile) ? "bound to&nbsp;" : "已綁";
            writer = response.getWriter();
            String uid = request.getParameter("uid");
            String gatetype = request.getParameter("gatetype");
            String getenum = request.getParameter("getenum");
            //运营商
            String bind_spisuncm = request.getParameter("bind_spisuncm");
            //获取当前通道账户已绑定的所有的通道
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //conditionMap.put("ptAccUid", uid);
            LinkedHashMap<String, String> orderbyMap1 = new LinkedHashMap<String, String>();
            List<AgwSpBind> bindList = baseBiz.getByCondition(AgwSpBind.class, conditionMap, null);
            //与此账号已绑定的
            List<Long> listselect = new ArrayList<Long>();
            //与其他账号绑定的 key为通道id value为账号名称
            Map<Long, String> listbind = new HashMap<Long, String>();
            if (bindList != null && bindList.size() > 0) {
                AgwSpBind spBind = null;
                for (int a = 0; a < bindList.size(); a++) {
                    spBind = bindList.get(a);
                    if (spBind != null && spBind.getGateId() != null && spBind.getPtAccUid() != null) {
                        //如果与徐绑定的账户uid一致则是选中的帐号 否则是别的帐号绑定的通道
                        if (spBind.getPtAccUid().toString().equals(uid)) {
                            listselect.add(Long.parseLong(spBind.getGateId().toString()));
                        } else {
                            //key为通道id
                            Userdata userdata = baseBiz.getById(Userdata.class, Long.parseLong(spBind.getPtAccUid().toString()));
                            if (userdata != null && userdata.getStaffName() != null) {
                                listbind.put(Long.parseLong(spBind.getGateId().toString()), userdata.getUserId());
                            }
                        }
                    }
                }
            }

            conditionMap.clear();
            conditionMap.put("gateType", gatetype);
            if (getenum != null && !"".equals(getenum)) {
                conditionMap.put("spgate&like", getenum);
            }
            if (bind_spisuncm != null && !"".equals(bind_spisuncm)) {
                conditionMap.put("spisuncm", bind_spisuncm);
            }
            orderbyMap1.clear();
            orderbyMap1.put("spisuncm", StaticValue.ASC);
            List<XtGateQueue> msgateList = baseBiz.getByCondition(XtGateQueue.class, conditionMap, orderbyMap1);

            String html = "";
            String rightHtml = "";
            //写一个排序方法
            Map map = getListbySwitch(listselect, listbind, msgateList);
            //获取通道绑定的信息，用来判断通道是否被sp账号绑定了
            List<GtPortUsed> gpuList = baseBiz.getEntityList(GtPortUsed.class);
            LinkedHashMap<String, String> gpuMap = new LinkedHashMap<String, String>();
            if (gpuList != null && gpuList.size() > 0) {
                for (GtPortUsed gpu : gpuList) {
                    gpuMap.put(gpu.getSpgate(), "");
                }
            }
            if (map != null) {
                for (XtGateQueue gate : (List<XtGateQueue>) map.get("keyuserbind")) {
//					html+="<input type='checkbox' name='checkSpgate' id='s"+gate.getId()+"' checked='checked' ";
//					html+="value='"+gate.getId()+"' />";
//					html+="<label style='cursor:pointer;font-size:14px;' for='s"+gate.getId()+"'  sect='1' ";
//					html+=" gateid='"+gate.getId()+"'>"+gate.getSpgate()+"</label>";
//					html+="<br id='"+gate.getId()+"'>";
                    /*移动*/
                    String suncmStr = mobile;
                    if (0 == gate.getSpisuncm()) {
                        /*移动*/
                        suncmStr = mobile;
                    } else if (1 == gate.getSpisuncm()) {
                        /*联通*/
                        suncmStr = unicom;
                    } else if (21 == gate.getSpisuncm()) {
                        /*电信*/
                        suncmStr = telecom;
                    } else if (5 == gate.getSpisuncm()) {
                        /*国外*/
                        suncmStr = foreign;
                    }
                    String nameStr = gate.getGateName() != null ? gate.getGateName() : "";
                    String gtname = nameStr.replaceAll("<", "&lt").replaceAll(">", "&gt").replaceAll("'", "&acute;");
                    String gtnameStr = gtname.length() > 4 ? (gtname.substring(0, 4) + "...") : gtname;
                    gtnameStr = gtnameStr.replaceAll("<", "&lt").replaceAll(">", "&gt").replaceAll("'", "&acute;");
                    String spgate = gate.getSpgate().length() > 9 ? (gate.getSpgate().substring(0, 9) + "...") : gate.getSpgate();

                    html += "<div class='div_con con_unbind con_usebind' suncm='" + gate.getSpisuncm() + "' gate_id='" + gate.getId() + "' id='div_" + gate.getId() + "' title='" + gtname + "'><span class='span_gt' title='" + gate.getSpgate() + "'>" + spgate + "</span>";
                    html += "<span class='span_sp'>" + suncmStr + "</span>";
                    html += "<span class='span_gtname'>" + gtnameStr + "</span></div> ";
                    String usebind = " con_usebind";
                    //判断该通道是否已经被sp账号绑定了
                    if (!gpuMap.containsKey(gate.getSpgate())) {
                        usebind = "";
                    }
                    rightHtml += "<div class='div_con con_unbind" + usebind + "' suncm='" + gate.getSpisuncm() + "' gate_id='" + gate.getId() + "' id='div_" + gate.getId() + "' title='" + gtname + "'><span class='span_gt'>" + gate.getSpgate() + "</span>";
                    rightHtml += "<span class='span_sp'>" + suncmStr + "</span>";
                    rightHtml += "<span class='span_gtname'>" + gtnameStr + "</span></div> ";
                }
                for (XtGateQueue gate : (List<XtGateQueue>) map.get("keynobind")) {
//					html+="<input type='checkbox' name='checkSpgate' id='s"+gate.getId()+"'";
//					html+="value='"+gate.getId()+"' />";
//					html+="<label style='cursor:pointer;font-size:14px;' for='s"+gate.getId()+"'  sect='2' ";
//					html+=" gateid='"+gate.getId()+"'>"+gate.getSpgate()+"</label>";
//					html+="<br id='"+gate.getId()+"'>";
                    /*移动*/
                    String suncmStr = mobile;
                    if (0 == gate.getSpisuncm()) {
                        /*移动*/
                        suncmStr = mobile;
                    } else if (1 == gate.getSpisuncm()) {
                        /*联通*/
                        suncmStr = unicom;
                    } else if (21 == gate.getSpisuncm()) {
                        /*电信*/
                        suncmStr = telecom;
                    } else if (5 == gate.getSpisuncm()) {
                        /*国外*/
                        suncmStr = foreign;
                    }
                    String nameStr = gate.getGateName() != null ? gate.getGateName() : "";
                    String gtname = nameStr.replaceAll("<", "&lt").replaceAll(">", "&gt").replaceAll("'", "&acute;");
                    String gtnameStr = gtname.length() > 4 ? (gtname.substring(0, 4) + "...") : gtname;
                    gtnameStr = gtnameStr.replaceAll("<", "&lt").replaceAll(">", "&gt").replaceAll("'", "&acute;");
                    String spgate = gate.getSpgate().length() > 9 ? (gate.getSpgate().substring(0, 9) + "...") : gate.getSpgate();

                    html += "<div class='div_con con_unbind' suncm='" + gate.getSpisuncm() + "' gate_id='" + gate.getId() + "' id='div_" + gate.getId() + "' title='" + gtname + "'><span class='span_gt' title='" + gate.getSpgate() + "'>" + spgate + "</span>";
                    html += "<span class='span_sp'>" + suncmStr + "</span>";
                    html += "<span class='span_gtname'>" + gtnameStr + "</span></div> ";
                }
                for (XtGateQueue gate : (List<XtGateQueue>) map.get("keybind")) {
//					html+="<input type='checkbox' name='checkSpgate' disabled='disabled' id='s"+gate.getId()+"'";
//					html+="value='"+gate.getId()+"' />";
//					html+="<label style='cursor:pointer;font-size:14px;color:#ccc;' for='s"+gate.getId()+"'  sect='2' ";
//					html+=" gateid='"+gate.getId()+"'>"+gate.getSpgate()+"(已绑"+gate.getGateName()+")</label>";
//					html+="<br id='"+gate.getId()+"'>";
                    /*移动*/
                    String suncmStr = mobile;
                    if (0 == gate.getSpisuncm()) {
                        /*移动*/
                        suncmStr = mobile;
                    } else if (1 == gate.getSpisuncm()) {
                        suncmStr = unicom;
                    } else if (21 == gate.getSpisuncm()) {
                        suncmStr = telecom;
                    } else if (5 == gate.getSpisuncm()) {
                        suncmStr = foreign;
                    }
                    String nameStr = gate.getGateName() != null ? gate.getGateName() : "";
                    String gtname = nameStr.replaceAll("<", "&lt").replaceAll(">", "&gt").replaceAll("'", "&acute;");
                    String gtnameStr = gtname.length() > 4 ? (gtname.substring(0, 4) + "...") : gtname;
                    gtnameStr = gtnameStr.replaceAll("<", "&lt").replaceAll(">", "&gt").replaceAll("'", "&acute;");
                    String spgate = gate.getSpgate().length() > 9 ? (gate.getSpgate().substring(0, 9) + "...") : gate.getSpgate();


                    html += "<div class='div_con con_bind' suncm='" + gate.getSpisuncm() + "' gate_id='" + gate.getId() + "' id='div_" + gate.getId() + "' title='" + binded + gate.getSignstr() + "'><span class='span_gt' title='" + gate.getSpgate() + "'>" + spgate + "</span>";
                    html += "<span class='span_sp'>" + suncmStr + "</span>";
                    html += "<span class='span_gtname'>" + gtnameStr + "</span></div> ";
                }
            }

            writer.print(html + "【MWFG】" + rightHtml);
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "绑定通道时获取通道账户异常！"));
            writer.print("");
        }
    }

    /**
     * 2013-7-19
     *
     * @param listselect
     * @param msgateList
     * @return Map
     */
    private Map<String, List<XtGateQueue>> getListbySwitch(List<Long> listselect, Map<Long, String> listbind, List<XtGateQueue> msgateList) {
        Map<String, List<XtGateQueue>> map = new HashMap<String, List<XtGateQueue>>();
        //与此账号已绑通道
        List<XtGateQueue> keyuserbind;
        //未绑定过的通道
        List<XtGateQueue> keynobind;
        //已绑定过但未与此账号绑定的通道
        List<XtGateQueue> keybind;
        if (msgateList != null && msgateList.size() > 0) {
            keyuserbind = new ArrayList<XtGateQueue>();
            keynobind = new ArrayList<XtGateQueue>();
            keybind = new ArrayList<XtGateQueue>();
            for (XtGateQueue gate : msgateList) {
                String gtStr = gate.getSpgate();
                //网优通道处理掉
//				if(gtStr.length()>3&&"200".equals(gtStr.substring(0,3)))
//				{
//					continue;
//				}
                if (listselect.contains(gate.getId())) {
                    keyuserbind.add(gate);
                } else if (listbind.containsKey(gate.getId())) {
                    //设置绑定账户名称
                    //gate.setGateName(listbind.get(gate.getId()));
                    //用签名存放下已绑定的通道账号名称
                    gate.setSignstr(listbind.get(gate.getId()));
                    keybind.add(gate);
                } else {
                    keynobind.add(gate);
                }
                map.put("keyuserbind", keyuserbind);
                map.put("keynobind", keynobind);
                map.put("keybind", keybind);
            }
            return map;
        }
        return null;
    }

    /**
     * 通道账户绑定
     *
     * @param request
     * @param response
     */
    public void addSpBind(HttpServletRequest request, HttpServletResponse response) {
        String uid = request.getParameter("uid");
        String gateid = request.getParameter("gateid");
        BaseBiz baseBiz = new BaseBiz();
        PrintWriter writer = null;
        String aaName = null;
        String corpcode = "";
        String opUser = "";
        SuperOpLog spLog = new SuperOpLog();
        try {
            writer = response.getWriter();
            List<AgwSpBind> AllAddagwsp = new ArrayList<AgwSpBind>();

            //得到当前通道账户已绑定的通道
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("ptAccUid", uid);
            List<AgwSpBind> bindAgw = baseBiz.getByCondition(AgwSpBind.class, conditionMap, null);
            String gateidstr = gateid;
            if (gateid != null && !"".equals(gateid)) {
                for (AgwSpBind asb : bindAgw) {
                    gateidstr = gateidstr.replace("s" + asb.getGateId() + ",", "");
                }
                if (gateidstr.length() != 0 && gateidstr.charAt(gateidstr.length() - 1) == ',') {
                    gateidstr = gateidstr.substring(0, gateidstr.length() - 1);
                }
                gateidstr = gateidstr.replace("s", "");
                //判断需绑定的通道是否已绑定账号
                if (!"".equals(gateidstr)) {
                    LinkedHashMap<String, String> conditionMapb = new LinkedHashMap<String, String>();
                    conditionMapb.put("gateId&in", gateidstr);
                    List<AgwSpBind> bindAgwb = baseBiz.getByCondition(AgwSpBind.class, conditionMapb, null);
                    if (bindAgwb != null && bindAgwb.size() > 0) {
                        writer.print("false");
                        return;
                    }
                }
            }

            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            corpcode = lfSysuser.getCorpCode();
            opUser = lfSysuser.getUserName();
            //获取通道账号的名称
            conditionMap.clear();
            conditionMap.put("ptAccUid", uid);
            List<AgwAccount> aaList = baseBiz.findListByCondition(AgwAccount.class, conditionMap, null);
            aaName = aaList.get(0).getPtAccId();

            GatewayBiz gatewayBiz = new GatewayBiz();

            //先删除掉它原来所有已绑定的
            if (bindAgw.size() > 0) {
                int a = baseBiz.deleteByCondition(AgwSpBind.class, conditionMap);
                if (a <= 0) {
                    writer.print("false");
                    return;
                }
            }

            gateid = gateid.length() > 0 ? gateid.substring(0, gateid.length() - 1) : "";
            //添加它需要绑定的
            if (gateid.length() > 0) {
                String[] gateids = gateid.split(",");
                for (int j = 0; j < gateids.length; j++) {
                    AgwSpBind agw = new AgwSpBind();
                    agw.setPtAccUid(Integer.parseInt(uid));
                    agw.setGateId(Integer.parseInt(gateids[j].replace("s", "")));
                    AllAddagwsp.add(agw);
                }
            }

            int result = baseBiz.addList(AgwSpBind.class, AllAddagwsp);
            if (result < 0) {
                writer.print("false");
                new GatewayBiz().setLog(request, "通道账户绑定", "通道账户绑定失败", StaticValue.ADD);
            } else {
                try {
                    String addXgqStr = "";
                    if ((bindAgw == null || bindAgw.size() < 1) && gateid.length() > 0) {
                        //之前没绑定通道,新增绑定
                        addXgqStr = gatewayBiz.getXtGateQueueStrByGateID(gateid.replace("s", ""));
                        String opContent = "通道账号(" + aaName + ")绑定" + addXgqStr + "通道。";
                        spLog.logSuccessString(opUser, opModule, StaticValue.ADD, opContent, corpcode);
                        new GatewayBiz().setLog(request, "通道账户绑定", opContent + "成功", StaticValue.ADD);
                        //EmpExecutionContext.info("企业：" +corpcode+",操作员：" +opUser+opContent+"成功");
                    } else if ((bindAgw != null && bindAgw.size() > 0) && gateid.length() < 1) {
                        //之前绑了通道，删除绑定
                        String gateids = "";
                        for (int i = 0; i < bindAgw.size(); i++) {
                            gateids += bindAgw.get(i).getGateId() + ",";
                        }
                        gateids = gateids.substring(0, gateids.length() - 1);
                        addXgqStr = gatewayBiz.getXtGateQueueStrByGateID(gateids);
                        String opContent = "通道账号(" + aaName + ")取消绑定" + addXgqStr + "通道。";
                        spLog.logSuccessString(opUser, opModule, StaticValue.DELETE, opContent, corpcode);
                        new GatewayBiz().setLog(request, "通道账户绑定", opContent + "成功", StaticValue.DELETE);
                        //EmpExecutionContext.info("企业：" +corpcode+",操作员：" +opUser+opContent+"成功");
                    } else {
                        String addXgqStr1 = "";
                        String addXgqStr2 = "";
                        String opContent = "";
                        //修改绑定
                        if (gateidstr != null && gateidstr.length() > 0) {
                            addXgqStr1 = gatewayBiz.getXtGateQueueStrByGateID(gateidstr);
                        }
                        if (gateid != null && gateid.length() > 0) {
                            String bindgateids = "";
                            for (int i = 0; i < bindAgw.size(); i++) {
                                bindgateids += "s" + bindAgw.get(i).getGateId() + ",";
                            }
                            String[] gateids = gateid.split(",");
                            for (int j = 0; j < gateids.length; j++) {
                                bindgateids = bindgateids.replaceAll(gateids[j] + ",", "");
                            }
                            bindgateids = bindgateids.replaceAll("s", "");
                            bindgateids = bindgateids.length() > 0 ? bindgateids.substring(0, bindgateids.length() - 1) : "";
                            if (bindgateids != null && bindgateids.length() > 0) {
                                addXgqStr2 = gatewayBiz.getXtGateQueueStrByGateID(bindgateids);
                            }
                        }
                        if ((addXgqStr1 == null || addXgqStr1.equals("")) && (addXgqStr2 != null && !addXgqStr2.equals(""))) {
                            opContent = "通道账号(" + aaName + ")取消绑定" + addXgqStr2 + "通道。";
                            spLog.logSuccessString(opUser, opModule, StaticValue.DELETE, opContent, corpcode);
                            new GatewayBiz().setLog(request, "通道账户绑定", opContent + "成功", StaticValue.DELETE);
                            //EmpExecutionContext.info("企业：" +corpcode+",操作员：" +opUser+opContent+"成功");
                        } else if ((addXgqStr1 != null && !addXgqStr1.equals("")) && (addXgqStr2 == null || addXgqStr2.equals(""))) {
                            opContent = "通道账号(" + aaName + ")绑定" + addXgqStr1 + "通道。";
                            spLog.logSuccessString(opUser, opModule, StaticValue.ADD, opContent, corpcode);
                            new GatewayBiz().setLog(request, "通道账户绑定", opContent + "成功", StaticValue.ADD);
                            //EmpExecutionContext.info("企业：" +corpcode+",操作员：" +opUser+opContent+"成功");
                        } else if (addXgqStr1 != null && !addXgqStr1.equals("") && addXgqStr2 != null && !addXgqStr2.equals("")) {
                            opContent = "通道账号(" + aaName + ")绑定" + addXgqStr1 + "通道，取消绑定" + addXgqStr2 + "通道。";
                            spLog.logSuccessString(opUser, opModule, StaticValue.UPDATE, opContent, corpcode);
                            new GatewayBiz().setLog(request, "通道账户绑定", opContent + "成功", StaticValue.UPDATE);
                            //EmpExecutionContext.info("企业：" +corpcode+",操作员：" +opUser+opContent+"成功");
                        }
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error(errorLoger.getErrorLog(e, "企业：" + corpcode + ",操作员：" + opUser + "记录通道账号绑定通道的操作日志出现异常！"));
                }
                writer.print("true");
            }
        } catch (Exception e) {
            spLog.logFailureString(opUser, opModule, StaticValue.UPDATE, "通道账号(" + aaName + ")绑定通道失败", e, corpcode);
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "企业：" + corpcode + ",操作员：" + opUser + "通道账户绑定异常！"));
            writer.print(ERROR);
        }
    }

    /**
     * 删除通道账户绑定
     *
     * @param request
     * @param response
     */
    public void delSpBind(HttpServletRequest request, HttpServletResponse response) {
        String uid = request.getParameter("uid");
        String gateid = request.getParameter("gateid");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("ptAccUid", uid);
            conditionMap.put("gateId", gateid);
            writer.print(new BaseBiz().deleteByCondition(AgwSpBind.class, conditionMap) > 0 ? "true" : "false");
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "删除通道账户绑定！"));
            if (writer != null) {
                writer.print(ERROR);
            }
        }
    }

    /**
     * 新建通道账户
     *
     * @param request
     * @param response
     */
    public void add(HttpServletRequest request, HttpServletResponse response) {
        String corpcode = "";
        String opUser = "";
        SuperOpLog spLog = new SuperOpLog();
        //日志添加内容
        String opContent = "";
        //添加字符串数据
        StringBuffer newStr = new StringBuffer("");

        Userdata userdata = new Userdata();
        BaseBiz baseBiz = new BaseBiz();
        PrintWriter writer = null;

        try {
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            corpcode = lfSysuser.getCorpCode();
            opUser = lfSysuser.getUserName();
            writer = response.getWriter();
            String userid = request.getParameter("userid");
            //先转大写
            userid = userid.toUpperCase();
            String staffname = request.getParameter("staffname");
            String accouttype = request.getParameter("accouttype");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userId", userid);
            conditionMap.put("accouttype", accouttype);
            List<Userdata> userList = baseBiz.getByCondition(Userdata.class, conditionMap, null);
            if (userList != null && userList.size() > 0) {
                writer.print("itemExists");
                return;
            }

            String dxval =  request.getParameter("dxval");
            String cxval =  request.getParameter("cxval");
            String fxval =  request.getParameter("fxval");
            int accability= 0;
            if("1".equals(dxval)){
                accability = accability | DX_ABILITY;
            }else{
                accability = accability & DX_DEBILITY;
            }
            if("2".equals(cxval)){
                accability = accability | CX_ABILITY;
            }else{
                accability = accability & CX_DEBILITY;
            }
            if("4".equals(fxval)){
                accability = accability | FX_ABILITY;
                // 富信发送在托管版7.2走的是短信发送方式，所以暂时存成短信
            }else{
                accability = accability & FX_DEBILITY;
            }
            userdata.setAccability(accability);

            userdata.setUserId(userid);
            userdata.setUserPassword(request.getParameter("userpassword"));
            userdata.setStaffName(staffname);
            userdata.setUserType(1l);
            userdata.setLoginId(userid);
            userdata.setCorpAccount("200001");
            userdata.setStatus(Integer.valueOf(request.getParameter("status")));
            userdata.setFeeFlag(1l);
            userdata.setRiseLevel(0l);
            userdata.setMoUrl(" ");
            userdata.setRptUrl(" ");
            userdata.setAccouttype(Integer.parseInt(accouttype));
            userdata.setSptype(1);
            userdata.setSpbindurl(" ");
            userdata.setSendtmspan("00:00:00-23:59:59");
            userdata.setTransmotype(0L);
            userdata.setTransrptype(0L);

            Long uid = new GatewayBiz().addUserdataReturnId(userdata);
            //为0表示出现异常，或保存失败
            if (uid - 0 == 0) {
                writer.print("dbError");
                return;
            }
            String feeUrl = request.getParameter("feeUrl");
            //技术合作商
            String spType = request.getParameter("spType");
            //账户计费类型
            int spFeeFlag = Integer.parseInt(request.getParameter("spFeeFlag"));
            //技术合作商为其他时,计费类型为2:后付费
            if ("10".equals(spType)) {
                spFeeFlag = 2;
            }
            //将转义的字符进行回转处理
            String spUserpassword = request.getParameter("SPACCPWD");//可能包含'''等符号
            spUserpassword = StringEscapeUtils.unescapeHtml(spUserpassword);
            if (StaticValue.getCORPTYPE() == 0) {
                LfSpFee lfSpFee = new LfSpFee();
                //运营商余额
                lfSpFee.setBalance(0);
                //运营商余额
                lfSpFee.setBalanceth(0);
                //更新时间
                lfSpFee.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                // SP账号/后端账号 将账号转大写
                //lfSpFee.setSpUser(request.getParameter("SPACCID").toUpperCase());
                lfSpFee.setSpUser(request.getParameter("SPACCID"));
                //账号密码
                lfSpFee.setSpUserpassword(spUserpassword);
                //账号类型(1代表后端账号，2代表SP账号)
                lfSpFee.setSpType(1);
                //账号类型（1.短信   2.彩信）
                lfSpFee.setAccountType(Integer.parseInt(accouttype));
                //运营商余额查询URL
                lfSpFee.setSpFeeUrl(feeUrl != null && !"".equals(feeUrl) ? feeUrl : " ");
                //扣费类型(1代表预付费，2代表后付费)
                lfSpFee.setSpFeeFlag(spFeeFlag);
                //余额查询时间(当前时间减5分钟)
                lfSpFee.setQueryTime(new Timestamp(System.currentTimeMillis() - 5 * 60 * 1000));
                baseBiz.addObj(lfSpFee);
            }

            AgwAccount account = new AgwAccount();

            String ptIp = request.getParameter("ptIp");
            String ptPort = request.getParameter("ptPort");

            //网关集群地址数组
            String[] clusterAddrs = request.getParameterValues("clusterAddr[]");
            String ptNode = gatewayBiz.getPtNode(ptIp, ptPort, clusterAddrs);

            account.setPtNode(ptNode);
            account.setPtAccUid(Integer.valueOf(uid.toString()));
            account.setPtAccName(staffname);
            account.setPtAccId(userid);
            //SPACCID转大写
            //account.setSpAccid(request.getParameter("SPACCID").toUpperCase());
            account.setSpAccid(request.getParameter("SPACCID"));
            account.setSpAccPwd(spUserpassword);
            account.setPtAccpwd(userdata.getUserPassword());
            account.setServiceType(request.getParameter("serviceType"));
            account.setFeeUserType(Integer.valueOf(request.getParameter("feeUserType")));
            account.setPtIp(ptIp);
            account.setPtPort(Integer.valueOf(ptPort));
            account.setSpPort(Integer.valueOf(request.getParameter("spPort")));
            account.setSpIp(request.getParameter("spIp"));
            //发送速率不起作用了，默认填0
            account.setSpeedLimit(Integer.valueOf(request.getParameter("speedLimit")));
            account.setProtocolCode(Integer.valueOf(request.getParameter("protocolCode")));

            String protocolParam = request.getParameter("protocolParam");
            //处理过滤器的漏洞问题：将<li>&#59;</li>转回为<li>;</li>
            if (protocolParam != null && !"".equals(protocolParam)) {
                protocolParam = StringEscapeUtils.unescapeHtml(protocolParam);
            }
            String byIp = request.getParameter("byIp");
            if (protocolParam == null || protocolParam.length() == 0) {
                protocolParam = " ";
            }
//			else
//			{
//				String [] proArr = protocolParam.split(";");
//				for(int i=0;i<proArr.length;i++)
//				{
//					if(proArr)
//				}
//			}
            if (byIp != null && !"".equals(byIp)) {
                String[] proArr = protocolParam.split(";");
                String proStr = "";
                for (int i = 0; i < proArr.length; i++) {
                    if (proArr[i].indexOf(";backupip=") != -1) {
                        proArr[i] = "";
                    }
                    proStr = proStr + proArr[i] + ";";
                }
                protocolParam = proStr + "backupip=" + byIp.substring(0, byIp.lastIndexOf(","));
            }
            account.setProtocolParam(protocolParam);
            //含有主备信息的ip端口字符串
            String zbIP = request.getParameter("zbIP");
            if (zbIP != null && !"".equals(zbIP)) {
                if ("1".equals(accouttype)) {
                    /*
                     * 将运营商ip端口  存于连路表
                     */
                    String resultstr = gatewayBiz.addGWGateconninfo(zbIP, spType, userid);
                    if (!"true".equals(resultstr)) {
                        opContent = "新建通道账户（通道账户账号：" + userid + "，运营商账户ID：" + request.getParameter("SPACCID") + "）运营商IP及端口传入的参数处理异常 zbIP:" + zbIP;
                        EmpExecutionContext.error("企业：" + corpcode + ",操作员：" + opUser + opContent);
                        writer.print(resultstr);
                    }
                }
            } else {
                //保存日志
                opContent = "新建通道账户（通道账户账号：" + userid + "，运营商账户ID：" + request.getParameter("SPACCID") + "）运营商IP及端口未填";
                EmpExecutionContext.error("企业：" + corpcode + ",操作员：" + opUser + opContent);
                writer.print("nobyip");
                return;
            }
            account.setSpId(request.getParameter("spid"));
            account.setSpType(Integer.valueOf(spType));
            account.setSpFeeFlag(spFeeFlag);
            account.setFeeUrl((feeUrl == null || feeUrl.equals("")) ? " " : feeUrl);
            account.setBalance(0l);
            account.setBalanceTh(0l);
            account.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            Integer gwNo = 100;
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("gwno", StaticValue.DESC);
            List<GWCluSpBind> gwCluSpBinds = baseBiz.getByCondition(GWCluSpBind.class, null, orderbyMap);
            if (gwCluSpBinds != null && gwCluSpBinds.size() > 0) {
                gwNo = gwCluSpBinds.get(0).getGwno() + 1;
            } else {
                conditionMap.clear();
                conditionMap.put("gwType", "4000");
                this.paramConfToParamValue(99, conditionMap);
            }

            account.setGwNo(gwNo);

            if (gatewayBiz.addGWAccount(account)) {
                conditionMap.clear();
                conditionMap.put("gwType", "3000");
                this.paramConfToParamValue(gwNo, conditionMap);

                //增加操作日志
                newStr.append(userdata.getAccouttype()).append("，").append(userdata.getStaffName()).append("，").append(userdata.getUserId()).append("，").append(userdata.getUserPassword())
                        .append("，").append(account.getPtIp()).append("，").append(account.getPtPort()).append("，").append(account.getSpAccid()).append("，").append(account.getSpAccPwd())
                        .append("，").append(account.getSpIp()).append("，").append(account.getSpPort()).append("，").append(account.getServiceType()).append("，").append(account.getSpId())
                        .append("，").append(account.getFeeUserType()).append("，").append(account.getSpType()).append("，").append(account.getProtocolCode()).append("，").append(account.getProtocolParam())
                        .append("，").append(account.getSpFeeFlag()).append("，").append(account.getFeeUrl());
                opContent = "新建通道账户成功[账户类型，通道账户名称，通道账号，账户密码，EMP网关IP地址，EMP网关IP地址端口，运营商账户ID，运营商账户密码，运营商IP地址及端口，业务类型，SP企业代码，计费用户类型，技术合作商，通讯协议，通讯协议参数，账户计费类型，余额查看URL]"
                        + "（" + newStr + "）";
                new GatewayBiz().setLog(request, "通道账户管理", opContent, StaticValue.ADD);
                spLog.logSuccessString(opUser, opModule, StaticValue.ADD, opContent, corpcode);
                writer.print(gwNo);
            } else {
                opContent = "新建通道账户失败[账户类型，通道账户名称，通道账号，账户密码，EMP网关IP地址，EMP网关IP地址端口，运营商账户ID，运营商账户密码，运营商IP地址及端口，业务类型，SP企业代码，计费用户类型，技术合作商，通讯协议，通讯协议参数，账户计费类型，余额查看URL]"
                        + "（" + newStr + "）";
                new GatewayBiz().setLog(request, "通道账户管理", opContent, StaticValue.ADD);
                writer.print("false");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "企业：" + corpcode + ",操作员：" + opUser + "新建通道账户异常！"));
            spLog.logFailureString(opUser, opModule, StaticValue.ADD, opContent, e, corpcode);
            writer.print(ERROR);
        }

    }

    /**
     * 修改通道账户
     *
     * @param request
     * @param response
     */
    public void edit(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        GatewayBiz gatewayBiz = new GatewayBiz();
        try {
            writer = response.getWriter();
            writer.print(gatewayBiz.editUserdata(request));
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "修改通道账户异常！"));
            if (writer != null) {
                writer.print(ERROR);
            }
        }

    }


    /**
     * 修改通道账户状态
     *
     * @param request
     * @param response
     */
    public void ChangeSate(HttpServletRequest request, HttpServletResponse response) {
        //企业编码
        String corpcode = "";
        String opUser = "";
//		opUser = opUser==null?"":opUser;
        SuperOpLog spLog = new SuperOpLog();
        BaseBiz baseBiz = new BaseBiz();
        PrintWriter writer = null;
        String opContent = null;
        try {
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            corpcode = lfSysuser.getCorpCode();
            opUser = lfSysuser.getUserName();
            opUser = opUser == null ? "" : opUser;
            writer = response.getWriter();
            //获取SP账户ID
            String uid = request.getParameter("uid");
            //SP账户状态
            String status = request.getParameter("status");
            LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            objectMap.put("status", status);
            conditionMap.put("uid", uid);
            Userdata userdata = baseBiz.getById(Userdata.class, uid);
            String type = userdata.getAccouttype() == 1 ? "短信" : "彩信";
            if (status.equals("0")) {

                opContent = "修改" + type + "通道账户：(" + userdata.getUserId() + ")的状态为激活";
            } else {
                opContent = "修改" + type + "通道账户：(" + userdata.getUserId() + ")的状态为失效";
            }

            //调用修关键字对象的方法，并返回结果
            boolean userresult = baseBiz.update(Userdata.class, objectMap, conditionMap);
            if (userresult) {
                writer.print("true");
                //保存日志
                spLog.logSuccessString(opUser, opModule, StaticValue.UPDATE, opContent, corpcode);
                //EmpExecutionContext.error("企业：" +corpcode+",操作员：" +opUser+opContent+"成功");
                //增加操作日志
                new GatewayBiz().setLog(request, "通道账户管理", opContent + "成功", StaticValue.UPDATE);

            } else {
                new GatewayBiz().setLog(request, "通道账户管理", opContent + "失败", StaticValue.UPDATE);
            }
        } catch (Exception e) {
            //异常错误
            if (writer != null) {
                writer.print("error");
            }
            spLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent + opSper, e, corpcode);
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "企业：" + corpcode + ",操作员：" + opUser + "修改通道账户状态异常！"));
        }
        return;
    }


    /**
     * 通过网关编号获取通道配置信息
     *
     * @param gwNo
     * @param conditionMap
     */
    private void paramConfToParamValue(Integer gwNo, LinkedHashMap<String, String> conditionMap) {
        List<AgwParamValue> paramList = new ArrayList<AgwParamValue>();
        BaseBiz baseBiz = new BaseBiz();
        try {
            List<AgwParamConf> confList = baseBiz.getByCondition(
                    AgwParamConf.class, conditionMap, null);
            if (confList != null && confList.size() > 0) {
                for (AgwParamConf conf : confList) {
                    AgwParamValue agwParam = new AgwParamValue();
                    agwParam.setGwNo(gwNo);
                    agwParam.setGwType(conf.getGwType());
                    agwParam.setParamItem(conf.getParamItem());
                    agwParam.setParamValue(conf.getDefaultValue());
                    paramList.add(agwParam);
                }
            }
            baseBiz.addList(AgwParamValue.class, paramList);
        } catch (Exception e) {
            //writer.print("error");
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "通过网关编号获取通道配置信息异常！"));
        }
    }

    private void setLog(HttpServletRequest request, String opModule, String opContent, String opType) {
        try {
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), opContent, opType);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, opModule + opType + opContent + "日志写入异常"));
        }
    }

    /**
     * 添加备用网关
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void addBak(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String result = "false";
        try {
            String uid = request.getParameter("uid");
            LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
            condMap.put("ptaccuid", uid);
            condMap.put("gweight&<>", "0");
            List<GWCluSpBind> gwCluSpBinds = new BaseBiz().getByCondition(GWCluSpBind.class, condMap, null);
            int count = gwCluSpBinds.size();
            if (count < 1) {
                result = String.valueOf(gatewayBiz.addBak(uid));
            } else {
                result = "overmax";
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "添加备用网关异常！");
        }
        out.print(result);
    }

    /**
     * 检查备用后端账号网关数
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void checkBakNum(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        int count = -1;
        try {
            String uid = request.getParameter("uid");
            LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
            condMap.put("ptaccuid", uid);
            condMap.put("gweight&<>", "0");
            List<GWCluSpBind> gwCluSpBinds = new BaseBiz().getByCondition(GWCluSpBind.class, condMap, null);
            count = gwCluSpBinds.size();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询对应主后端账号的备用网关数目异常！");
        }
        out.print(count);
    }

    /**
     * 获取加密对象
     *
     * @param request
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-10-26 下午07:29:28
     */
    public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request) {
        try {
            ParamsEncryptOrDecrypt encryptOrDecrypt = null;
            //加密对象
            Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
            //加密对象不为空
            if (encrypOrDecrypttobject != null) {
                //强转类型
                encryptOrDecrypt = (ParamsEncryptOrDecrypt) encrypOrDecrypttobject;
            } else {
                EmpExecutionContext.error("通道账户管理,从session获取加密对象为空。");
            }
            return encryptOrDecrypt;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "通道账户管理,从session获取加密对象异常。");
            return null;
        }
    }

    /**
     * 获取运营商余额查询备用URL
     *
     * @param request
     * @param response
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2017-1-10 下午03:04:41
     */
    public void getBalanceBakUrl(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        String balanceBakRrl = "";
        try {
            response.setContentType("text/html");
            out = response.getWriter();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("globalKey", "BALANCEBAKURL");
            List<LfGlobalVariable> globalVariableList = new BaseBiz().getByCondition(LfGlobalVariable.class, conditionMap, null);
            if (globalVariableList != null && globalVariableList.size() > 0) {
                balanceBakRrl = globalVariableList.get(0).getGlobalStrValue();
            }
            out.print(balanceBakRrl);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "通道账户管理,获取运营商余额查询备用URL失败。");
            if (out != null) {
                out.print("");
            }
        }
    }
}
