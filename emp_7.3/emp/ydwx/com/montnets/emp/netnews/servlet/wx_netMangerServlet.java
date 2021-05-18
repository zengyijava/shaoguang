package com.montnets.emp.netnews.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.common.StaticValue;
import com.montnets.emp.netnews.daoImpl.Wx_netMangerDaoImpl;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.netnews.entity.LfWXSORT;
import com.montnets.emp.netnews.util.wx_FileUtil;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.wxmanger.dao.WxManagerDao;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Vincent <vincent1219@21cn.com>
 * @project montnets_emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-11-4
 * @description 网讯管理
 */
@SuppressWarnings("serial")
public class wx_netMangerServlet extends BaseServlet {
    /**
     * 查询方法
     *
     * @param request
     * @param response
     */
    private final Wx_netMangerDaoImpl netMangerDao = new Wx_netMangerDaoImpl();
    private final WxManagerDao managerDao = new WxManagerDao();
    private final BaseBiz baseBiz = new BaseBiz();

    public void find(HttpServletRequest request, HttpServletResponse response) {
        //String lguserid  =request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        //为了提交审核后，返回到之前列表页面
        String skip = request.getParameter("skip");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        long begin_time = System.currentTimeMillis();
        String corp = AllUtil.toStringValue(request.getParameter("corp"), "");
        PageInfo pageInfo = new PageInfo();
        LfSysuser curUser = null;
        boolean isFirstEnter;
        try {
            request.getSession(false).setAttribute("topMonu", "12");
            curUser = baseBiz.getById(LfSysuser.class, lguserid);
            isFirstEnter = pageSet(pageInfo, request);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            HttpSession session = request.getSession(false);
            String url = "falge=1";//这个falge=1 唯一用于增加条件的时候的拼接
            if (!isFirstEnter || "true".equals(skip)) {
                String wxid = request.getParameter("wxid");
                String wxname = request.getParameter("wxname");
                String temptype = request.getParameter("temptype");
                String zhuangtai = request.getParameter("rState");
                String startdate = request.getParameter("startdate");
                String enddate = request.getParameter("enddate");
                String operStatus = request.getParameter("operStatus");
                //在编辑中，点击审核按钮返回进入时候的那页，对应Bug ID:  36772
                if ("true".equals(skip)) {
                    if (session.getAttribute("wx_zhuangtai") != null) {
                        zhuangtai = (String) session.getAttribute("wx_zhuangtai");
                    }
                    if (session.getAttribute("temptype") != null) {
                        temptype = (String) session.getAttribute("temptype");
                    }
                    if (session.getAttribute("operStatus") != null) {
                        operStatus = (String) session.getAttribute("operStatus");
                    }
                    request.setAttribute("zhuangtai", zhuangtai);
                    request.setAttribute("temptype", session.getAttribute("temptype"));
                    request.setAttribute("operStatus", session.getAttribute("operStatus"));
                }

                if (wxid != null && !"".equals(wxid.trim())) {
                    conditionMap.put("wxid", wxid.trim());
                    request.setAttribute("wxid", wxid);
                    url = url + "&wxid=" + wxid;
                }
                if (wxname != null && !"".equals(wxname.trim())) {
                    conditionMap.put("wxname", wxname.trim());
                    request.setAttribute("wxname", wxname);
                    url = url + "&wxname=" + wxname;
                }
                if (temptype != null && !"".equals(temptype.trim())) {
                    conditionMap.put("temptype", temptype.trim());
                    request.setAttribute("temptype", temptype);
                    session.setAttribute("temptype", temptype);
                }
                if (zhuangtai != null && !"".equals(zhuangtai)) {
                    conditionMap.put("status", zhuangtai);
                    request.setAttribute("zhuangtai", zhuangtai);
                    session.setAttribute("wx_zhuangtai", zhuangtai);
                }
                //添加运营商审批的条件查询

                conditionMap.put("operStatus", operStatus);
                request.setAttribute("operStatus", operStatus);
                session.setAttribute("operStatus", operStatus);

                if (startdate != null && !"".equals(startdate)) {
                    request.setAttribute("startdate", startdate);
                    conditionMap.put("startdate", startdate + " 00:00:00");
                    url = url + "&startdate=" + startdate;
                }
                if (enddate != null && !"".equals(enddate)) {
                    request.setAttribute("enddate", enddate);
                    conditionMap.put("enddate", enddate + " 23:59:59");
                    url = url + "&enddate=" + enddate;
                }
                if (curUser.getCorpCode() != null && !"".equals(curUser.getCorpCode())) {
                    conditionMap.put("corpcode", curUser.getCorpCode());
                }


            } else {
                //防止重新打开查询界面还显示上次内容
                request.setAttribute("operStatus", "");
            }
            String flowId = request.getParameter("flowId");
            //审批流程跳转 查询该审批流下待审批的任务
            if (flowId != null && !"".equals(flowId)) {
                conditionMap.put("flowId", flowId);
            }
            List<DynaBean> beans = managerDao.getBaseInfos(conditionMap, pageInfo, lguserid);
            if ("true".equals(skip)) {
                //从发送界面上的点击新增链接，不需要从session里面取值
                if (request.getSession(false).getAttribute("pageIn") != null) {
                    pageInfo = (PageInfo) request.getSession(false).getAttribute("pageIn");
                }
                conditionMap = (LinkedHashMap<String, String>) request.getSession(false).getAttribute("netMangerConditionMap");
                if (conditionMap != null && pageInfo != null) {
                    beans = managerDao.getBaseInfos(conditionMap, pageInfo, lguserid);
                }

            }
            if (pageInfo != null) {
                long end_time = System.currentTimeMillis();
                String opContent = "查询开始时间：" + format.format(begin_time) + ",耗时:" + (end_time - begin_time) + "ms，查询总数：" + pageInfo.getTotalRec();
                Object sysObj = request.getSession(false).getAttribute("loginSysuser");
                if (sysObj != null) {
                    LfSysuser lfSysuser = (LfSysuser) sysObj;
                    EmpExecutionContext.info("网讯编辑", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "", lfSysuser.getUserName(), opContent, "GET");
                }
            }

            request.setAttribute("pagebaseinfo", beans);
            request.setAttribute("pageInfo", pageInfo);
            // 查询条件，存到session，保存审核之后用
            session.setAttribute("netMangerConditionMap", conditionMap);
            session.setAttribute("pageIn", pageInfo);
            request.setAttribute("userId", curUser.getUserId().toString());
            request.setAttribute("corp", corp);

            //如果保存审核之后，就传值
            if ("true".equals(skip)) {
                if (session.getAttribute("netMangerCondition") != null) {
                    String url_net = (String) session.getAttribute("netMangerCondition");
                    request.getRequestDispatcher("ydwx/manageWX/netManger.jsp?" + url_net).forward(request, response);
                } else {
                    request.getRequestDispatcher("ydwx/manageWX/netManger.jsp").forward(request, response);
                }

            } else {
                session.setAttribute("netMangerCondition", url);
                request.getRequestDispatcher("ydwx/manageWX/netManger.jsp").forward(request, response);
            }


        } catch (Exception e) {
            String str = "";
            if (curUser != null) {
                str = "(corpCode：" + curUser.getCorpCode() + "，opt：" + curUser.getUserId() + "[" + curUser.getUserName() + "])";
            }
            EmpExecutionContext.error(e, "查询网讯信息" + str + "时，产生异常!");
        }
    }

    /**
     * 获取信息发送查询里的详细
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void getSmsTplDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            //模板ID
            String tmpid = request.getParameter("tmpid");
            //操作员用户ID
            //String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);


            LfSysuser user = baseBiz.getById(LfSysuser.class, lguserid);
            //获取对应的彩信任务
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //LfTemplate template = baseBiz.getById(LfTemplate.class, tmpid);
            JSONObject jsonObject = new JSONObject();
            //设置条件的MAP
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
            //conditionMap.put("ProUserCode",String.valueOf(template.getUserId()));
            conditionMap.put("infoType", "6");
            conditionMap.put("mtId", tmpid);
            conditionMap.put("RState&in", "1,2");
            conditionMap.put("isComplete", "1");
            orderByMap.put("RLevel", StaticValue.ASC);
            orderByMap.put("preRv", StaticValue.DESC);
            List<LfFlowRecord> flowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
            JSONArray members = new JSONArray();
            if (flowRecords != null && flowRecords.size() > 0) {
                LinkedHashMap<Long, String> nameMap = new LinkedHashMap<Long, String>();
                conditionMap.clear();
                String auditName = "";
                for (int j = 0; j < flowRecords.size(); j++) {
                    LfFlowRecord temp = flowRecords.get(j);
                    auditName = auditName + temp.getUserCode() + ",";
                }
                List<LfSysuser> sysuserList = null;
                if (auditName != null && !"".equals(auditName)) {
                    auditName = auditName.substring(0, auditName.length() - 1);
                    conditionMap.put("userId&in", auditName);
                    conditionMap.put("corpCode", user.getCorpCode());
                    sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
                    if (sysuserList != null && sysuserList.size() > 0) {
                        for (LfSysuser sysuser : sysuserList) {
                            nameMap.put(sysuser.getUserId(), sysuser.getName());
                        }
                    }
                }

                //是否有审批信息1有  2 没有
                jsonObject.put("haveRecord", "1");
                JSONObject member = null;
                LfFlowRecord record = null;
                for (int i = 0; i < flowRecords.size(); i++) {
                    member = new JSONObject();
                    record = flowRecords.get(i);
                    member.put("smsRlevel", record.getRLevel().toString() + "/" + record.getRLevelAmount().toString());
                    //审批人
                    if (nameMap != null && nameMap.size() > 0 && nameMap.containsKey(record.getUserCode())) {
                        member.put("smsReviname", nameMap.get(record.getUserCode()));
                    } else {
                        member.put("smsReviname", "-");
                    }
                    if (record.getRTime() == null) {
                        member.put("smsrtime", "-");
                    } else {
                        member.put("smsrtime", df.format(record.getRTime()));
                    }
                    //审批结果
                    int state = record.getRState();
                    switch (state) {
                        case -1:
                            member.put("smsexstate", "未审批");
                            break;
                        case 1:
                            member.put("smsexstate", "审批通过");
                            break;
                        case 2:
                            member.put("smsexstate", "审批不通过");
                            break;
                        default:
                            member.put("smsexstate", "[无效的标示]");
                    }

                    if ("".equals(record.getComments()) || record.getComments() == null) {
                        member.put("smsComments", "");
                    } else {
                        member.put("smsComments", record.getComments());
                    }
                    members.add(member);
                }
                jsonObject.put("members", members);
            } else {
                jsonObject.put("haveRecord", "2");
            }

            conditionMap.clear();
            String firstshowname = "";
            String firstcondition = "";
            //一级都没有审核
            //获取下一级审核
            conditionMap.put("infoType", "6");
            conditionMap.put("mtId", String.valueOf(tmpid));
            conditionMap.put("RState", "-1");
            conditionMap.put("isComplete", "2");
            List<LfFlowRecord> unflowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
            LfFlowRecord lastrecord = null;
            Long depId = null;
            String[] recordmsg = new String[2];
            recordmsg[0] = "";
            recordmsg[1] = "";
            String isshow = "2";
            if (unflowRecords != null && unflowRecords.size() > 0) {
                isshow = "1";
                StringBuffer useridstr = new StringBuffer();
                for (LfFlowRecord temp : unflowRecords) {
                    useridstr.append(temp.getUserCode()).append(",");
                }
                if (lastrecord == null) {
                    lastrecord = unflowRecords.get(0);
                }
                List<LfSysuser> sysuserList = null;
                if (useridstr != null && !"".equals(useridstr.toString())) {
                    String str = useridstr.toString().substring(0, useridstr.toString().length() - 1);
                    conditionMap.clear();
                    conditionMap.put("userId&in", str);
                    conditionMap.put("corpCode", user.getCorpCode());
                    sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
                    if (sysuserList != null && sysuserList.size() > 0) {
                        for (LfSysuser sysuser : sysuserList) {
                            firstshowname = firstshowname + sysuser.getName() + "&nbsp;&nbsp;";
                            if (depId == null) {
                                depId = sysuser.getDepId();
                            }
                        }
                    }
                }
                if (lastrecord != null) {
                    //审核类型  1操作员  4机构  5逐级审核
                    Integer rtype = lastrecord.getRType();
                    firstcondition = lastrecord.getRCondition() + "";
                    ReviewBiz reviewBiz = new ReviewBiz();
                    //当是逐步审批的时候
                    if (rtype == 5) {
                        //获取逐级审批
                        boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lastrecord.getPreRv().intValue(), lastrecord.getProUserCode());
                        //逐级审批的最后一级
                        if (isLastLevel) {
                            if (lastrecord.getRLevelAmount() != 1) {
                                lastrecord.setRLevel(1);
                                recordmsg = reviewBiz.getNextFlowRecord(lastrecord, user.getCorpCode());
                            }
                        } else {
                            LfDep dep = baseBiz.getById(LfDep.class, depId);
                            if (dep != null) {
                                LfDep pareDep = baseBiz.getById(LfDep.class, dep.getSuperiorId());
                                if (pareDep != null) {
                                    recordmsg[0] = pareDep.getDepName();
                                    recordmsg[1] = lastrecord.getRCondition() + "";
                                }
                            }
                        }
                    } else {
                        //该流程审批的最后一级
//						if(lastrecord.getRLevel() != lastrecord.getRLevelAmount()){
                        if (lastrecord.getRLevel() != null && (!lastrecord.getRLevel().equals(lastrecord.getRLevelAmount()))) {
                            recordmsg = reviewBiz.getNextFlowRecord(lastrecord, user.getCorpCode());
                        }
                    }
                }
            }
            jsonObject.put("isshow", isshow);
            jsonObject.put("firstshowname", firstshowname);
            jsonObject.put("firstcondition", firstcondition);
            jsonObject.put("secondshowname", recordmsg[0]);
            jsonObject.put("secondcondition", recordmsg[1]);

            response.getWriter().print(jsonObject.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取网讯审批信息异常!");
        }
    }


    //根据网讯ID获取网讯所有页面
    public void showNetById(HttpServletRequest request, HttpServletResponse response) {
        List<LfWXPAGE> pageList = new ArrayList<LfWXPAGE>();
        String netID = request.getParameter("netId");
        try {
            //根据netid获取page集合
            pageList = netMangerDao.getnetByID(netID);
            //判断下服务器是是否存在该文件

            for (int i = 0; i < pageList.size(); i++) {
                CommonBiz biz = new CommonBiz();
                LfWXPAGE page = pageList.get(i);
                if (page.getNAME().matches("Default web page|默认网讯页面|默認網訊頁面")) {
                    page.setNAME(MessageUtils.extractMessage("ydwx", "ydwx_survey_7", request));
                }
                //分布式 ：如果存在就下载该文件到本地服务器上
                if (com.montnets.emp.common.constant.StaticValue.getISCLUSTER() == 1) {
                    //获取JSP相对路径
                    long before = System.currentTimeMillis();
                    String strRelativeSrc = "file/wx/PAGE/wx_" + page.getID() + ".jsp";
                    //处理乱码显示
                    String result = biz.downFileFromFileCenWhitZipWX(strRelativeSrc);
                    long after = System.currentTimeMillis();
                    EmpExecutionContext.info("查看预览结果：" + result + ",耗费时间：" + (after - before) + "ms");
                    if ("error".equals(result)) {
                        if (!isStaticFileExist(page)) {//
                            page.setCONTENT("notexists");//用于页面显示错误页面
                        }

                    }
                } else if (!isStaticFileExist(page)) {
                    page.setCONTENT("notexists");//用于页面显示错误页面
                }
                //处理链接失效的问题
                if (!"notexists".equals(page.getCONTENT())) {
                    wx_FileUtil obj = new wx_FileUtil();
                    String tempPath = new TxtFileUtil().getWebRoot() + "/file/wx/PAGE/wx_" + page.getID() + ".jsp";
                    File file = new File(tempPath);
                    if (file.exists()) {
                        String conetxt = obj.read(tempPath);
                        obj.write(tempPath, conetxt);
                    }
                }

            }

            response.getWriter().print(pageList);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取网讯所有页面异常!");
        }

    }


    /**
     * 单个删除
     *
     * @param request
     * @param response
     */

    public void del(HttpServletRequest request, HttpServletResponse response) {

        boolean blResult = false;
        try {
            Integer result = 0;
            String wxid = request.getParameter("id");

            String name = "";
            if (wxid != null) {
                String[] ids = wxid.split(",");
                for (int i = 0; i < ids.length; i++) {
                    if ("".equals(ids[i])) {
                        continue;
                    }
                    LfWXBASEINFO info = baseBiz.getById(LfWXBASEINFO.class, ids[i]);
                    if (info != null) {
                        name = info.getNAME() + "," + name;
                    }
                }
            }
            result = baseBiz.deleteByIds(LfWXBASEINFO.class, wxid);
            if (result > 0) {
                Object sysObj = request.getSession(false).getAttribute("loginSysuser");
                if (sysObj != null) {
                    LfSysuser lfSysuser = (LfSysuser) sysObj;
                    EmpExecutionContext.info("网讯编辑", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "", lfSysuser.getUserName(), "网讯删除成功。[网讯名称](" + name + "）", "DELETE");
                    //EmpExecutionContext.info("模块名称：网讯编辑，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，删除网讯（id："+wxid+"）成功");
                }
                blResult = true;
            } else {
                Object sysObj = request.getSession(false).getAttribute("loginSysuser");
                if (sysObj != null) {
                    LfSysuser lfSysuser = (LfSysuser) sysObj;
                    EmpExecutionContext.info("网讯编辑", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "", lfSysuser.getUserName(), "网讯删除失败。[网讯名称](" + name + "）", "DELETE");
                    //EmpExecutionContext.info("模块名称：网讯编辑，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，删除网讯（id："+wxid+"）成功");
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "网讯单个删除异常!");
        } finally {
            try {
                response.getWriter().print(blResult);
            } catch (IOException ioex) {
                EmpExecutionContext.error(ioex, "响应网讯删除请求异常!");
            }
        }
    }


    /**
     * 设置模板时  得到模板类型
     *
     * @param request
     * @param response
     */

    public void getSort(HttpServletRequest request, HttpServletResponse response) {
        try {
            String lgcorpcode = request.getParameter("lgcorpcode");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            conditionMap.put("TYPE", "0");
            List<LfWXSORT> list = baseBiz.getByCondition(LfWXSORT.class, conditionMap, null);
            StringBuffer buffer = new StringBuffer("");
            if (list != null && list.size() > 0) {
                buffer.append("[");
                for (int i = 0; i < list.size(); i++) {
                    buffer.append("{sortid:").append(list.get(i).getID()).append(",");
                    buffer.append("name:'").append(list.get(i).getNAME()).append("'}");
                    if (i != list.size() - 1) {
                        buffer.append(",");
                    }
                }
                buffer.append("]");
            }
            response.getWriter().print(buffer.toString());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取互动项信息异常!");
        }
    }


    /**
     * 复制
     *
     * @param request
     * @param response
     */

    public void Copy(HttpServletRequest request, HttpServletResponse response) {
        try {
            //String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);

            String coid = request.getParameter("coid");

            boolean b = netMangerDao.copyManager(lguserid, coid, request);//传值到实现类中，返回一个blooean值
            Object sysObj = request.getSession(false).getAttribute("loginSysuser");
            if (sysObj != null) {
                LfSysuser lfSysuser = (LfSysuser) sysObj;
                //EmpExecutionContext.info("模块名称：网讯编辑，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，复制网讯（被拷贝的模板ID："+coid+"）成功");
                String name = "";
                LfWXBASEINFO baseInfo = baseBiz.getById(LfWXBASEINFO.class, Long.parseLong(coid));
                if (baseInfo != null) {
                    name = baseInfo.getNAME();
                }
                String log = "失败";
                if (b) {
                    log = "成功";
                }
                EmpExecutionContext.info("网讯编辑", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "", lfSysuser.getUserName(), "网讯复制" + log + "。[网讯名称](" + name + "）", "OTHER");
            }
            response.getWriter().print("true");
        } catch (Exception e) {
            EmpExecutionContext.error(e, "网讯复制异常！");
            try {
                response.getWriter().print("false");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "IO异常！");
            }
        }
    }

    /*设置模板*/
    @SuppressWarnings({"unchecked", "deprecation"})
    public void SetType(HttpServletRequest request, HttpServletResponse response) {

        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);


        String pageIndex = request.getParameter("pageIndex");
        String pagesize = request.getParameter("pagesize");
        String fType = AllUtil
                .toStringValue(request.getParameter("fType"), "0");
        String netid = request.getParameter("netid");

        String msg = "图片上传异常，请重新上传！";
        long MAX_SIZE = 1024L * 1024L; // 设置上传文件最大值
        response.setContentType("text/html");

        // 设置字符编码为UTF-8, 统一编码，处理出现乱码问题
        response.setCharacterEncoding("UTF-8");

        // 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
        DiskFileItemFactory dfif = new DiskFileItemFactory();

        dfif.setSizeThreshold(100 * 1024);
        String tempPath = new TxtFileUtil().getWebRoot() + StaticValue.WX_TEMP_PATH + "temp/";
        new TxtFileUtil().makeDir(tempPath);
        dfif.setRepository(new File(tempPath));// 设置存放临时文件的目录,
        // 用以上工厂实例化上传组件
        ServletFileUpload sfu = new ServletFileUpload(dfif);
        // 设置最大上传大小
        sfu.setSizeMax(MAX_SIZE);
        // 从request得到所有上传域的列表
        List<FileItem> fileList = null;
        try {
            fileList = sfu.parseRequest(request);

            // 得到所有上传的文件
            Iterator<FileItem> fileItr = fileList.iterator();
            // 循环处理所有文件
            List<FileItem> list = new ArrayList<FileItem>();
            while (fileItr.hasNext()) {
                FileItem fileItem = null;
                fileItem = (FileItem) fileItr.next();
                // 得到当前文件
                if (fileItem == null || fileItem.isFormField()) {
                    continue;
                }
                list.add(fileItem);
            }
            if (list.size() >= 1) {
                FileItem file1 = (FileItem) list.get(0);
                // 根据系统时间生成上传后保存的文件名
                long now = System.currentTimeMillis();
                // 保存文件路径的文件名称
                String filename = String.valueOf(lguserid) + "_"
                        + String.valueOf(now) + "." + "jpg";
                String uploadpath = StaticValue.WX_TEMP_PATH;
                String basePath12 = new TxtFileUtil().getWebRoot() + uploadpath;
                new TxtFileUtil().makeDir(basePath12);
                file1.write(new File(basePath12 + filename));
                //**************如果出现集群的方式,也要上传到服务器上***************
                //获取是否为集群常量
                int iIsCluster = com.montnets.emp.common.constant.StaticValue.getISCLUSTER();
                boolean isFTPUpload = true;
                if (iIsCluster == 1) {// 必须先生成本地文件，然后执行远程数据的COPY 上传
                    CommonBiz com = new CommonBiz();
                    String path = uploadpath.substring(1, uploadpath.length());
                    isFTPUpload = com.upFileToFileServer(path + filename);
                    if (!isFTPUpload) {
                        msg = "图片上传异常！";
                    }
                }
                // 设置模板
                if (isFTPUpload) {
                    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                    conditionMap.put("NETID", netid);
                    List<LfWXBASEINFO> baseInfos = baseBiz.getByCondition(
                            LfWXBASEINFO.class, conditionMap, null);
                    boolean result = false;
                    if (baseInfos != null && baseInfos.size() > 0) {
                        LfWXBASEINFO info = baseInfos.get(0);
                        info.setSORT(Long.valueOf(fType));
                        info.setWxTYPE(0);
                        info.setIMAGE(uploadpath + filename);
                        result = baseBiz.updateObj(info);
                    }
                    if (result) {
                        msg = "网讯模板设置成功";


                        File fa = new File(basePath12 + netid + ".jpg");
                        if (fa.exists()) {
                            if (!fa.delete()) {
                                throw new FileUploadException();
                            }
                        }
                        //file2.write(new File(basePath12 + netid + ".jpg"));
                    }
                }
            } else {
                msg = "需上传小图与大图两张图片！";
            }
        } catch (FileUploadException e) {// 处理文件尺寸过大异常
            msg = "图片尺寸超过规定大小:" + MAX_SIZE + "字节！";
        } catch (Exception e) {
            EmpExecutionContext.error(e, "网讯模板设置异常！");
            msg = "程序处理异常！";
        } finally {
            try {
                request.getSession(false).setAttribute("msg", msg);
                String s = request.getRequestURI();
                s = s + "?method=find&lguserid=" + lguserid + "&pageIndex=" + pageIndex + "&pagesize=" + pagesize;
                response.sendRedirect(s);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "网讯模板设置响应重定向异常!");
            }
        }
    }

    /***
     * 取消模板设置
     * @description
     * @param request  请求参数
     * @param response 回传参数
     * @throws ServletException 异常
     * @throws IOException
     * @datetime 2016-3-23 下午02:59:07
     */
    public void cancelType(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String netid = request.getParameter("netid");
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("NETID", netid);
            LfSysuser user = getLoginUser(request);
            String lgcorpcode = user.getCorpCode();
            if (lgcorpcode != null && !"".equals(lgcorpcode)) {
                conditionMap.put("CORPCODE", lgcorpcode);
            }
            List<LfWXBASEINFO> baseInfos = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);
            if (baseInfos != null && baseInfos.size() > 0) {
                LfWXBASEINFO info = baseInfos.get(0);
                info.setSORT(0L);
                info.setWxTYPE(1);
                String name = info.getNAME();
                boolean result = baseBiz.updateObj(info);
                if (result) {
                    Object sysObj = request.getSession(false).getAttribute("loginSysuser");
                    if (sysObj != null) {
                        LfSysuser lfSysuser = (LfSysuser) sysObj;
                        EmpExecutionContext.info("网讯模板", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "", lfSysuser.getUserName(), "取消模板设置成功。[网讯编号，类型名称](" + netid + "，" + name + "）", "UPDATE");
                        //EmpExecutionContext.info("模块名称：网讯编辑，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，修改网讯类型（NETID："+netid+"）成功");
                    }
                    response.getWriter().print("true");
                } else {
                    Object sysObj = request.getSession(false).getAttribute("loginSysuser");
                    if (sysObj != null) {
                        LfSysuser lfSysuser = (LfSysuser) sysObj;
                        //EmpExecutionContext.error("模块名称：网讯编辑，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，修改网讯类型（NETID："+netid+"）失败");
                        EmpExecutionContext.info("网讯模板", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "", lfSysuser.getUserName(), "取消模板设置失败。[网讯编号，类型名称](" + netid + "，" + name + "）", "UPDATE");
                    }
                    response.getWriter().print("false");
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "取消网讯异常！");
            response.getWriter().print("false");
        }
    }

    /**
     * 通过ID查询网讯页面相关信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws NumberFormatException
     * @throws SQLException
     * @description
     * @datetime 2016-3-23 下午02:59:52
     */
    public void findCreatid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException, SQLException {
        String netid = request.getParameter("netid");
        Long creid = 0L;
        List<LfWXPAGE> list = netMangerDao.getnetByID(netid);
        if (list.size() > 0) {
            LfWXPAGE page = (LfWXPAGE) list.get(0);
            creid = page.getCREATID();
        }
        response.getWriter().print(creid);
    }

    /**
     * 生成对应的字符串类型
     */

    public String getStrings(List<LfSysuser> list) {
        String a = "(";
        if (list.size() == 1) {
            a += list.get(0).getGuId() + ")";
        } else {
            for (int i = 0; i < list.size(); i++) {
                if ((list.size() - 1) == i) {
                    a += list.get(i).getGuId() + ")";
                } else {
                    a += list.get(i).getGuId() + ",";
                }
            }
        }
        return a;
    }


    /**
     * DESC: 判断网讯静态文件是否存在
     *
     * @param objWxpage 网讯页面
     * @return true = 存在页面文件
     */
    public boolean isStaticFileExist(LfWXPAGE objWxpage) {

        //获取JSP相对路径
        String strRelativeSrc = "file/wx/PAGE/wx_" + objWxpage.getID() + ".jsp";
        boolean blisExist = true;
        String strPath = new TxtFileUtil().getWebRoot() + strRelativeSrc;
        File objFile = new File(strPath);
        if (!objFile.exists()) {
            blisExist = false;
        }

        return blisExist;

    }
}
