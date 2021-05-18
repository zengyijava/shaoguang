package com.montnets.emp.tailmanage.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailmanage.GwMsgtail;
import com.montnets.emp.entity.tailmanage.GwTailbind;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.tailmanage.biz.MsgTailMgrBiz;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

public class tai_msgTailMgrSvt extends BaseServlet {

    private static final String PATH = "/xtgl/msgTailMgr";
    private final BaseBiz baseBiz = new BaseBiz();
    private final MsgTailMgrBiz tailMgrBiz = new MsgTailMgrBiz();

    /**
     * 查询业务贴尾
     *
     * @param request
     * @param response
     */
    public void find(HttpServletRequest request, HttpServletResponse response) {

        try {

            //添加与日志相关
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
            long startTimeByLog = System.currentTimeMillis();  //开始时间

            //分页对象
            PageInfo pageInfo = new PageInfo();
            //是否第一次打�?
            boolean isFirstEnter = false;
            isFirstEnter = pageSet(pageInfo, request);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            List<DynaBean> recordList = null;
           // String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);

            String lgcorpcode = request.getParameter("lgcorpcode");
            LinkedHashMap<String, LinkedHashMap> tailListByBus = new LinkedHashMap<String, LinkedHashMap>();
            LinkedHashMap<String, LinkedHashMap> tailListBySp = new LinkedHashMap<String, LinkedHashMap>();
            //是否包含子机构
            String isContainsSun = request.getParameter("isContainsSun");
            conditionMap.put("corpcode", lgcorpcode);
            //贴尾名称
            String name = request.getParameter("tailname");
            if (name != null && !"".equals(name)) {
                conditionMap.put("tailname", name.trim());
            }
            /*备份*/
            //业务名称
            String buss = request.getParameter("buss");
            if (buss != null && !"".equals(buss)) {
                conditionMap.put("buss", buss.trim());
            }

            //部门
            String deptid = request.getParameter("deptid");
            deptid = (deptid != null && deptid.length() > 0) ? deptid : "";
            if (!"".equals(deptid)) {
                conditionMap.put("deptid", deptid.trim());
            }

            if (isFirstEnter) {
                //默认是选择的
                conditionMap.put("isContainsSun", "1");
            } else {
                if (isContainsSun == null || "".equals(isContainsSun)) {
                    conditionMap.put("isContainsSun", "0");
                } else {
                    conditionMap.put("isContainsSun", "1");
                }
            }
            //创建人
            String userName = request.getParameter("userName");
            if (userName != null && !"".equals(userName)) {
                conditionMap.put("userName", userName.trim());
            }
            //提交起始时间
            String submitSartTime = request.getParameter("startSubmitTime");
            if (submitSartTime != null && !"".equals(submitSartTime)) {
                conditionMap.put("startSubmitTime", submitSartTime);
            }
            //提交结束时间
            String submitEndTime = request.getParameter("endSubmitTime");
            if (submitEndTime != null && !"".equals(submitEndTime)) {
                conditionMap.put("endSubmitTime", submitEndTime);
            }
            // 获取当前登录操作员对象
            LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
            //当前操作员所拥有的权限
            Integer permissionType = (sysuser != null && sysuser.getPermissionType() != null) ? sysuser.getPermissionType() : null;
            if (permissionType != null && permissionType == 1) {
                conditionMap.put("permissionType", "1");
                conditionMap.put("permissionUserName", sysuser.getUserName().toString());
            }
            conditionMap.put("userId", sysuser == null ? null : String.valueOf(sysuser.getUserId()));


            /**EMP标准版6.1 新需求*/
            //贴尾内容
            String content = request.getParameter("content");
            if (content != null && !"".equals(content)) {
                conditionMap.put("content", content);
            }
            //贴尾类型
            String tailtype = request.getParameter("tailtype");
            if (tailtype != null && !"".equals(tailtype)) {
                conditionMap.put("tailtype", tailtype);
            }
            //SP账号
            String spuserid = request.getParameter("spuserid");
            if (spuserid != null && !"".equals(spuserid)) {
                conditionMap.put("spuserid", spuserid.trim());
            }
            //业务编码
            String buscode = request.getParameter("buscode");
            if (buscode != null && !"".equals(buscode)) {
                conditionMap.put("buscode", buscode.trim());
            }
            /**end*/


            recordList = tailMgrBiz.getTailRecord(conditionMap, pageInfo);


            //业务列表记录
            if (recordList != null && recordList.size() > 0) {
                for (int i = 0; i < recordList.size(); i++) {
                    DynaBean db = recordList.get(i);
                    String tail_id = db.get("tail_id").toString();
                    LinkedHashMap<String, String> list = tailMgrBiz.busList(tail_id);
                    tailListByBus.put(tail_id, list);
                }

            }

            //SP账号列表记录
            if (recordList != null && recordList.size() > 0) {
                for (int i = 0; i < recordList.size(); i++) {
                    DynaBean db = recordList.get(i);
                    String tail_id = db.get("tail_id").toString();
                    LinkedHashMap<String, String> list = tailMgrBiz.spList(tail_id);
                    tailListBySp.put(tail_id, list);
                }

            }


            if (isFirstEnter) {
                //默认是选择的
                request.setAttribute("isContainsSun", "1");
            } else {
                if (isContainsSun == null || "".equals(isContainsSun)) {
                    request.setAttribute("isContainsSun", "0");
                } else {
                    request.setAttribute("isContainsSun", "1");
                }
            }
            List<DynaBean> busList = tailMgrBiz.getBusinessV1(null, lgcorpcode, StaticValue.getCORPTYPE());
            List<DynaBean> spList = tailMgrBiz.getSpV1(null, lgcorpcode, StaticValue.getCORPTYPE());
            request.setAttribute("lguserid", lguserid);
            request.setAttribute("lgcorpcode", lgcorpcode);
            request.setAttribute("tailListByBus", tailListByBus);
            request.setAttribute("tailListBySp", tailListBySp);
            request.setAttribute("busList", busList);
            request.setAttribute("spList", spList);
            request.setAttribute("recordList", recordList);
            request.setAttribute("conditionMap", conditionMap);
            request.setAttribute("pageInfo", pageInfo);


            //添加与日志相关
            long endTimeByLog = System.currentTimeMillis();  //查询结束时间
            long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时

            //增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent = "查询开始时间：" + sdf.format(startTimeByLog) + "，耗时：" + consumeTimeByLog + "ms" + "，查询总数：" + pageInfo.getTotalRec();

                EmpExecutionContext.info("业务贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                        loginSysuser.getUserName(), opContent, "GET");
            }


            request.getRequestDispatcher(tai_msgTailMgrSvt.PATH + "/tai_msgTailMgr.jsp?lguserid=" + lguserid).forward(request, response);


        } catch (Exception e1) {
            EmpExecutionContext.error(e1, "业务贴尾管理页面跳转出现异常");
        }
    }

    /**
     * 点击查询按钮，根据名称查询业务
     *
     * @param request
     * @param response
     */

    public void search(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("epname");
        String nameSp = request.getParameter("epnameSp");
        String theTailtype = request.getParameter("theTailtype");


        if (name == null) {
            name = "";
        }
        if (nameSp == null) {
            nameSp = "";
        }
        //增加操作日志
        LfSysuser loginSysuserObj = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        List<DynaBean> nameList = tailMgrBiz.getBusinessV1(name, loginSysuserObj.getCorpCode(), StaticValue.getCORPTYPE());
        List<DynaBean> nameListSp = tailMgrBiz.getSpV1(nameSp, loginSysuserObj.getCorpCode(), StaticValue.getCORPTYPE());

        if ("1".equals(theTailtype.trim())) {
            StringBuffer sb = new StringBuffer("[");
            if (nameList != null) {
                for (int i = 0; i < nameList.size(); i++) {
                    DynaBean db = nameList.get(i);
                    String bus_code = db.get("bus_code").toString();
                    String bus_name = db.get("bus_name").toString();
                    String bus_id = db.get("bus_id").toString();
                    String tailnameBus = "";
                    Object tail_nameBus = db.get("tail_name");
                    if (tail_nameBus != null) {
                        tailnameBus = tail_nameBus.toString();
                    }
                    String tablelink = "";
                    if (db.get("tablelink") != null) {
                        tablelink = db.get("tablelink").toString();
                    }

                    sb.append("{bus_code:'" + bus_code + "',bus_name:'" + bus_name + "',bus_id:'" + bus_id + "',tail_nameBus:'" + tailnameBus + "', tablelink:'" + tablelink + "'}");
                    if (i != nameList.size() - 1) {
                        sb.append(",");
                    }
                }
            }
            sb.append("]");
            response.getWriter().print(sb.toString());

        }
        if ("2".equals(theTailtype.trim())) {
            StringBuffer sbSp = new StringBuffer("[");
            if (null != nameListSp) {
                for (int i = 0; i < nameListSp.size(); i++) {
                    DynaBean db = nameListSp.get(i);
                    String userid = "";
                    String staffname = "";
                    String tailnameSp = "";
                    Object tail_nameSp = null;
                    String tablelink = "";
                    if (null != db) {
                        userid = db.get("userid") != null ? db.get("userid").toString() : "";
                        staffname = db.get("staffname") != null ? db.get("staffname").toString() : "";
                        tail_nameSp = db.get("tail_name");
                        tablelink = db.get("tablelink") != null ? db.get("tablelink").toString() : "";
                    }

                    if (null != tail_nameSp) {
                        tailnameSp = tail_nameSp.toString();
                    }

                    sbSp.append("{userid:'" + userid + "',staffname:'" + staffname + "',tail_nameSp:'" + tailnameSp + "', tablelink:'" + tablelink + "'}");
                    if (i != nameListSp.size() - 1) {
                        sbSp.append(",");
                    }
                }
            }
            sbSp.append("]");
            response.getWriter().print(sbSp.toString());
        }


    }

    /***
     * 保存短息贴尾信息
     * @param request
     * @param response
     */
    public void save(HttpServletRequest request, HttpServletResponse response) {
        boolean add = false;
        String tmMsg = request.getParameter("tmMsg");
       // String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        String lgcorpcode = request.getParameter("lgcorpcode");
        String tmTitle = request.getParameter("tmTitle");
        String bussids = request.getParameter("bussids");
        String busscodes = request.getParameter("busscodes");
        String spsids = request.getParameter("spsids");
        String theTailtype = request.getParameter("theTailtype");
        int tailtype = Integer.parseInt(theTailtype);


        //判断是否包含关键字
        String keyWord = checkBadWord(tmMsg);
        if (keyWord != null && !"".equals(keyWord)) {
            try {
                response.getWriter().print("outOfLogin" + keyWord);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "判断是否包含关键字异常");
            }
            return;
        }


        GwMsgtail tail = new GwMsgtail();
        try {
            if (tailtype == 2) {
                tail.setTailname(tmTitle);
                tail.setContent(tmMsg);
                tail.setCreatetime(new Timestamp(System.currentTimeMillis()));
                tail.setUpdatetime(new Timestamp(System.currentTimeMillis()));
                tail.setCorpcode(lgcorpcode);
                tail.setUserid(Long.parseLong(lguserid));
                add = tailMgrBiz.save(tail, spsids, tailtype);
            }
            if (tailtype == 1) {
                tail.setTailname(tmTitle);
                tail.setContent(tmMsg);
                tail.setCreatetime(new Timestamp(System.currentTimeMillis()));
                tail.setUpdatetime(new Timestamp(System.currentTimeMillis()));
                tail.setCorpcode(lgcorpcode);
                tail.setUserid(Long.parseLong(lguserid));
                add = tailMgrBiz.save(tail, busscodes, tailtype);
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "保存贴尾信息异常");
        }
        try {
            if (add) {
                response.getWriter().print("success");
            } else {
                response.getWriter().print("fail");
            }
            //增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                if (tailtype == 2) {
                    String opContent1 = "新建SP账号贴尾内容" + (add == true ? "成功" : "失败") + "。[贴尾名称，适用SP账号]" +
                            "(" + tmTitle + "，[" + spsids + "])";
                    EmpExecutionContext.info("贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                            loginSysuser.getUserName(), opContent1, "ADD");
                } else if (tailtype == 1) {
                    String opContent1 = "新建业务贴尾内容" + (add == true ? "成功" : "失败") + "。[贴尾名称，适用业务]" +
                            "(" + tmTitle + "，[" + bussids + "])";
                    EmpExecutionContext.info("贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                            loginSysuser.getUserName(), opContent1, "ADD");
                } else {
                    String opContent1 = "新建业务及SP账号贴尾内容" + (add == true ? "成功" : "失败") + "。[贴尾名称，适用业务]" +
                            "(" + tmTitle + "，[" + spsids + "]，[" + bussids + "])";
                    EmpExecutionContext.info("贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                            loginSysuser.getUserName(), opContent1, "ADD");
                }

            }
        } catch (IOException e) {
            EmpExecutionContext.error(e, "跳转贴尾信息异常");
        }
    }

    /***
     *  根据贴尾ID查询出单个记录
     *  <li>已json串的形式返回给前端页面</li>
     *  <li>为避免过滤器之后的转义导致前端显示出现问题，使用StringEscapeUtils工具类处理转义后的字符，确保给前端的数据是正常的，不是转义后的数据</li>
     * @param request   内置请求对象
     * @param response	内置相应对象
     * @throws Exception  出现异常捕捉后在向上抛出
     */
    public void getTailByID(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String tailid = request.getParameter("tailid");
        String theTailtype = request.getParameter("theTailtype");

        try {
            String show = "";
            int tailtype = Integer.parseInt(theTailtype);
            GwMsgtail tail = baseBiz.getById(GwMsgtail.class, tailid);
            String updateSelectBus = tailMgrBiz.busStr(tail.getTailid() + "");
            String updateSelectSp = tailMgrBiz.spStr(tail.getTailid() + "");
            if (tailtype == 1) {
                //show = "{tail_type: '1',tail_name: '" + tail.getTailname() + "', content: '" + string2json(StringEscapeUtils.unescapeHtml(tail.getContent())) + "',tailid: '" + tailid + "',updateSelectBus:" + updateSelectBus + "}";
                show = "{tail_type: '1',tail_name: '" + tail.getTailname() + "', content: '" + string2json(tail.getContent()) + "',tailid: '" + tailid + "',updateSelectBus:" + updateSelectBus + "}";
            }
            if (tailtype == 2) {
                //show = "{tail_type: '2',tail_name: '" + tail.getTailname() + "', content: '" + string2json(StringEscapeUtils.unescapeHtml(tail.getContent())) + "',tailid: '" + tailid + "',updateSelectSp:" + updateSelectSp + "}";
                show = "{tail_type: '2',tail_name: '" + tail.getTailname() + "', content: '" + string2json(tail.getContent()) + "',tailid: '" + tailid + "',updateSelectSp:" + updateSelectSp + "}";
            }
            response.getWriter().print(show);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据ID查询出单个记录异常");
        }
    }

    public String string2json(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);

            }

        }
        return sb.toString();
    }

    /**
     * 根据ID删除单条记录
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void deleteByID(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String tailid = request.getParameter("id");

        try {
            //查询操作之前记录
            GwMsgtail befchgEntity = baseBiz.getById(GwMsgtail.class, tailid);
            String befchgCont = befchgEntity.getTailid() + "，" + befchgEntity.getTailname();

            String retvalue = tailMgrBiz.deleteSingle(tailid);
            response.getWriter().print(retvalue);

            //增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent1 = "删除业务贴尾内容" + ("success".equals(retvalue) ? "成功" : "失败") + "。[贴尾Id，贴尾名称]" +
                        "(" + befchgCont + ")";
                EmpExecutionContext.info("业务贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                        loginSysuser.getUserName(), opContent1, "DELETE");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据Id删除单条记录异常！");
        }
    }

    /**
     * 根据ID删除多条记录
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void deleteSelect(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String ids = request.getParameter("id");

        try {
            //查询操作之前记录
            String idss = "";
            String befchgCont = "";
            if (ids.indexOf(",") > -1) {
                if (",".equals(ids.substring(0, 1).toString())) {
                    idss = ids.substring(1, ids.length() - 1);
                } else {
                    idss = ids.substring(0, ids.length() - 1);
                }
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("tailid&in", idss);
                List<GwMsgtail> msgtailList = baseBiz.getByCondition(GwMsgtail.class, conditionMap, null);
                if (msgtailList.size() > 0) {
                    for (int i = 0; i < msgtailList.size(); i++) {
                        befchgCont += "(" + msgtailList.get(i).getTailid() + "，" + msgtailList.get(i).getTailname() + ")";
                    }
                }
            }

            String ret = tailMgrBiz.deleteSelect(ids);
            response.getWriter().print(ret);

            //增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent1 = "删除业务贴尾内容" + ("success".equals(ret) ? "成功" : "失败") + "。[贴尾Id，贴尾名称]" + befchgCont;
                EmpExecutionContext.info("业务贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                        loginSysuser.getUserName(), opContent1, "DELETE");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据Id删除多条记录异常！");
        }
    }

    //修改贴尾的内容
    public void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean add = false;
        String tmMsg = request.getParameter("tmMsg");
        String tmTitle = request.getParameter("tmTitle");
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        String bussids = request.getParameter("bussids");
        String busscodes = request.getParameter("busscodes");
        String spsids = request.getParameter("spsids");
        String theTailtype = request.getParameter("theTailtype");
        int tailtype = Integer.parseInt(theTailtype);


        //判断是否包含关键字
        String keyWord = checkBadWord(tmMsg);
        if (keyWord != null && !"".equals(keyWord)) {
            try {
                response.getWriter().print("outOfLogin" + keyWord);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "判断是否包含关键字异常");
            }
            return;
        }

        String modfid = request.getParameter("modfid");
        GwMsgtail tail = baseBiz.getById(GwMsgtail.class, modfid);

        //查询操作之前记录
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("tailid", modfid);
        List<GwTailbind> tailbindList = baseBiz.getByCondition(GwTailbind.class, conditionMap, null);
        String busIds = "";
        if (tailbindList.size() > 0) {
            for (int i = 0; i < tailbindList.size(); i++) {
                busIds += tailbindList.get(i).getBuscode() + ",";
            }
        }
        String spIds = "";
        if (tailbindList.size() > 0) {
            for (int i = 0; i < tailbindList.size(); i++) {
                spIds += tailbindList.get(i).getSpuserid() + ",";
            }
        }
        String befchgContBus = tail.getTailname() + "，[" + busIds + "]";
        String befchgContSp = tail.getTailname() + "，[" + spIds + "]";

        tail.setContent(tmMsg);
        tail.setTailname(tmTitle);
        tail.setUpdatetime(new Timestamp(System.currentTimeMillis()));
        //防止修改时，创建人和创建机构也跟着更新
        //tail.setUserid(Long.parseLong(lguserid));

        if (modfid == null || "".equals(modfid)) {
            try {
                response.getWriter().print("fail");
                return;
            } catch (IOException e) {
                EmpExecutionContext.error(e, "贴尾信息异常");
            }

        }
        try {
            if (tailtype == 2) {
                add = tailMgrBiz.update(tail, spsids, modfid, tailtype);
            }
            if (tailtype == 1) {
                add = tailMgrBiz.update(tail, busscodes, modfid, tailtype);
            }


        } catch (Exception e) {
            EmpExecutionContext.error(e, "贴尾信息异常");
        }
        try {
            if (add) {
                response.getWriter().print("success");
            } else {
                response.getWriter().print("fail");
            }
            //增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                if (tailtype == 0) {
                    String opContent1 = "修改SP账号贴尾内容" + (add == true ? "成功" : "失败") + "。[贴尾名称，适用业务]" +
                            "(" + befchgContSp + ")->(" + tmTitle + "，[" + spsids + "])";
                    EmpExecutionContext.info("贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                            loginSysuser.getUserName(), opContent1, "UPDATE");
                } else {
                    String opContent1 = "修改业务贴尾内容" + (add == true ? "成功" : "失败") + "。[贴尾名称，适用业务]" +
                            "(" + befchgContBus + ")->(" + tmTitle + "，[" + bussids + "])";
                    EmpExecutionContext.info("贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                            loginSysuser.getUserName(), opContent1, "UPDATE");
                }


            }
        } catch (IOException e) {
            EmpExecutionContext.error(e, "跳转贴尾信息异常");
        }
    }

    /**
     * 查询条件中的机构树加载方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    //输出机构代码数据
    public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Long depId = null;
            Long userid = null;
            //部门iD
            String depStr = request.getParameter("depId");
            //操作员账号
           // String userStr = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String userStr = SysuserUtil.strLguserid(request);

            if (depStr != null && !"".equals(depStr.trim())) {
                depId = Long.parseLong(depStr);
            }
            if (userStr != null && !"".equals(userStr.trim())) {
                userid = Long.parseLong(userStr);
            }
            //从session中获取当前操作员对象
            LfSysuser lfSysuser = getLoginUser(request);
            String corpCode = lfSysuser.getCorpCode();
            String departmentTree = tailMgrBiz.getDepartmentJosnData2(depId, userid, corpCode);
            response.getWriter().print(departmentTree);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "网优群发历史或群发任务查询条件中的机构树加载方法异常");
        }
    }

    /**
     * 检查关键字
     *
     */
    public String checkBadWord(String tmMsg) {
        KeyWordAtom keyWordAtom = new KeyWordAtom();
        //内容
        String words = new String();
        try {
            //调用检查关键字的方法，并返回结果

            words = keyWordAtom.checkText(tmMsg);
        } catch (Exception e) {
            words = "error";
            EmpExecutionContext.error(e, "检查发送内容关键字异常!");
        }
        return words;
    }

}
