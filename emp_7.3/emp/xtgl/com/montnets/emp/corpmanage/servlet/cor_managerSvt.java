package com.montnets.emp.corpmanage.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.JsonReceviceUtil;
import com.montnets.emp.common.tools.JsonReturnUtil;
import com.montnets.emp.common.tools.MessageUtils;
import com.montnets.emp.common.tools.TemplateUtil;
import com.montnets.emp.corpmanage.biz.CorManagerBiz;
import com.montnets.emp.corpmanage.biz.EnterpriseManageBiz;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;


public class cor_managerSvt extends BaseServlet {

    //操作模块
    private final String opModule = "企业管理";

    private static final long serialVersionUID = 1L;
    protected static List<LfCorp> list = null;
    protected final EnterpriseManageBiz enterpriseBiz = new EnterpriseManageBiz();
    private static final GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();

    private final String empRoot = "xtgl";
    private final String basePath = "/corpmanage";
    private final BaseBiz baseBiz = new BaseBiz();
    private final EnterpriseManageBiz enterpriseManageBiz = new EnterpriseManageBiz();

    private final CorManagerBiz corManagerBiz = new CorManagerBiz();

    /**
     * 查询方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void find(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            //登录操作员信息
            LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //加载排序条件
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            //查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            PageInfo pageInfo = new PageInfo();
            //操作员企业编码
            String corpCode = loginSysuser.getCorpCode();
            //不为100000的企业编码不允许查询
            if (corpCode != null && "100000".equals(corpCode)) {
                //非第一次登录时加载查询条件
                String code = request.getParameter("code");
                if (code != null && !"".equals(code)) {
                    code = URLDecoder.decode(code, "UTF-8");
                    conditionMap.put("corpCode&like", code);
                }
                String name = request.getParameter("condcorpname");
                if (name != null && !"".equals(name)) {
                    name = URLDecoder.decode(name, "UTF-8");
                    conditionMap.put("corpName&like", name);
                }
                String addr = request.getParameter("addr");
                if (addr != null && !"".equals(addr)) {
                    addr = URLDecoder.decode(addr, "UTF-8");
                    conditionMap.put("address&like", addr);
                }
                String corpState = request.getParameter("corpState");
                if (corpState != null && !"".equals(corpState) && corpState != "-1" && !"-1".equals(corpState.trim())) {
                    //corpState = URLDecoder.decode(corpState, "UTF-8");
                    conditionMap.put("corpState", corpState);
                }

                orderbyMap.put("corpCode", "desc");
                //查询获取结果
                try {
                    pageSet(pageInfo, request);
                    list = baseBiz.getByConditionNoCount(LfCorp.class, null, conditionMap,
                            orderbyMap, pageInfo);
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "查询企业信息异常！");
                }
            } else {
                //不为100000企业，查询结果为空
                list.clear();
                EmpExecutionContext.error("查询企业信息异常，操作员企业编码非管理级企业，操作员企业编码：" + corpCode);
            }

            //返回前台结果
            request.setAttribute("conditionMap", conditionMap);
            request.setAttribute("list", list);
            request.setAttribute("pageinfo", pageInfo);
            //跳转页面
            request.getRequestDispatcher(empRoot + basePath + "/cor_manager.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询企业信息异常！");
        }
    }

    /**
     * 添加的方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void add(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LfCorp lc = new LfCorp();
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        orderbyMap.put("corpCode", "desc");
        List<LfCorp> gvsList;
        try {
            //当前登录用户
            LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //操作员企业编码
            String lgCorpCode = sysuser.getCorpCode();
            //不为100000的企业编码不允许查询
            if (lgCorpCode != null && "100000".equals(lgCorpCode)) {
                //获取企业信息
                gvsList = baseBiz.getByCondition(LfCorp.class, null, orderbyMap);
                //得到将要添加一条企业对象的企业编码
                Long corpCode = Long.parseLong(gvsList.get(0).getCorpCode()) + 1;
                lc.setCorpCode(corpCode + "");

                //String dirUrl =this.getWebRoot()+this.empRoot+ "\\" + SystemGlobals.getValue(StaticValue.EMP_WEB_FRAME) + StaticValue.LOGOPATH;
                String imgUrl = lc.getLgoUrl();
                //返回结果到前台
                request.setAttribute("corp", lc);
                request.setAttribute("logoUrl", imgUrl);
                request.setAttribute("type", "add");

                EmpExecutionContext.info(opModule, corpCode.toString(), sysuser.getUserId().toString(),
                        sysuser.getUserName(), "新增企业，企业编码：" + corpCode, StaticValue.ADD);
            } else {
                EmpExecutionContext.error("查询企业信息异常，操作员企业编码非管理级企业，操作员企业编码：" + lgCorpCode);
            }
            //跳转页面
            request.getRequestDispatcher(empRoot + basePath + "/cor_editCorp.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "新增企业信息异常！");
        }
    }

    /**
     * 编辑企业信息的方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void toEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //当前登录用户
            LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //操作员企业编码
            String lgCorpCode = sysuser.getCorpCode();
            //不为100000的企业编码不允许修改
            if (lgCorpCode != null && "100000".equals(lgCorpCode)) {
                List<LfCorp> corp = new ArrayList<LfCorp>();
                List<LfDep> depList = new ArrayList<LfDep>();
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
                try {
                    //根据过滤条件获取企业信息
                    conditionMap.put("corpCode", request.getParameter("code"));
                    corp = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
                    orderbyMap.put("depId", "asc");
                    //获取此企业机构信息
                    depList = baseBiz.getByCondition(LfDep.class, conditionMap,
                            orderbyMap);
                    String depName = "";
                    if (depList != null && depList.size() > 0) {
                        depName = depList.get(0).getDepName();
                    }
                    //获取此顶级机构的机构名称
                    request.setAttribute("depName", depName);
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "跳转到编辑企业信息异常！");
                }
                //结果返回前台页面
                request.setAttribute("corp", corp.get(0));
                request.setAttribute("type", "update");
            } else {
                EmpExecutionContext.error("编辑企业信息异常，操作员企业编码非管理级企业，操作员企业编码：" + lgCorpCode);
            }
            //跳转页面
            request.getRequestDispatcher(empRoot + basePath + "/cor_editCorp.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "编辑企业信息异常！");
        }
    }

    /**
     * 修改方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void update(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            //当前登录用户
            LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //操作员企业编码
            String lgCorpCode = sysuser.getCorpCode();
            //不为100000的企业编码不允许修改
            if (lgCorpCode != null && "100000".equals(lgCorpCode)) {
                LfCorp corp = new LfCorp();
                //企业编码
                String corpCode = request.getParameter("ccode");
                //企业名称
                String corpName = request.getParameter("corpName");
                //手机号码
                String mobile = request.getParameter("mobile");
                //电话号码
                String phone = request.getParameter("phone");
                //地址
                String address = request.getParameter("address");
                //联系人
                String linkman = request.getParameter("linkman");
                //email
                String email = request.getParameter("email");
                //机构名称
                String depName = request.getParameter("depName");
                //是否计费
                String isBalance = request.getParameter("IsCharging");
                //企业状态 1启用  0禁用
                String corpState = request.getParameter("corpState");

                String loginCorpCode = "";
                String loginUserName = "";
                String loginUserId = "";

                if ("setParam".equals(request.getParameter("action"))) {
                    String rpt = request.getParameter("rpt");
                    String down = request.getParameter("down");
                    String rptFlag = "1";
                    // 0:表示不需要;1:需要通知状态报告;2:需要下载状态报告;3:通知、下载状态报告都要;(默认通知状态报告必须)
                    if (rpt != null && "1".equals(rpt.trim()) && down != null && "1".equals(down.trim())) {
                        rptFlag = "3";
                    } else if (rpt != null && "1".equals(rpt.trim())) {
                        rptFlag = "1";
                    } else if (down != null && "1".equals(down.trim())) {
                        rptFlag = "2";
                    } else {
                        rptFlag = "0";
                    }
                    boolean setParamRs = enterpriseBiz.setParam(rptFlag, Long.valueOf(request.getParameter("corpId")));
                    request.setAttribute("setParam", setParamRs);
                }

                if ("update".equals(request.getParameter("action"))) {
                    try {
                        //当前登录操作员信息
                        LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
                        loginCorpCode = lfSysuser.getCorpCode();
                        loginUserName = lfSysuser.getUserName();
                        loginUserId = String.valueOf(lfSysuser.getUserId());
                        corp.setCorpID(Long.valueOf(request.getParameter("corpId")));
                        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
                        objectMap.put("address", address);
                        objectMap.put("corpCode", corpCode);
                        objectMap.put("corpName", corpName);
                        objectMap.put("emails", email);
                        objectMap.put("linkman", linkman);
                        objectMap.put("mobile", mobile);
                        objectMap.put("phone", phone);
                        objectMap.put("lgoUrl", request.getParameter("lgoUrl"));
                        if (isBalance != null && isBalance != "") {
                            objectMap.put("isBalance", isBalance);
                        }
                        //修改企业设置企业状态
                        //如果企业编码不为100000，才修改企业状态
                        if (!"100000".equals(corpCode)) {
                            objectMap.put("corpState", corpState);
                        }
                        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                        conditionMap.put("corpID", request.getParameter("corpId"));
                        //调用修改方法
                        boolean updateRs = enterpriseBiz.updateLfCorp(objectMap, conditionMap, depName);

                        if (updateRs) {
                            EmpExecutionContext.info(opModule, loginCorpCode, lfSysuser.getUserId().toString(),
                                    loginUserName, "修改企业，企业名称：" + corpName + ",企业编码：" + corpCode, StaticValue.UPDATE);
                            //EmpExecutionContext.info("模块名称：企业管理，企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员修改一个企业成功,修改的企业编码为："+corpCode+",企业名称为："+corpName+"。");
                        } else {
                            EmpExecutionContext.error("模块名称：企业管理，企业编码[" + loginCorpCode + "],登录账号[" + loginUserName + "]的操作员修改一个企业失败,修改的企业编码为：" + corpCode + ",企业名称为：" + corpName + "。");
                        }

                        request.setAttribute("update", updateRs);
                    } catch (Exception e) {
                        EmpExecutionContext.error(e, "修改企业信息异常！");
                    }
                }
                if ("add".equals(request.getParameter("action"))) {
                    //设置需要添加企业的信息
                    corp.setAddress(address);
                    corp.setCorpCode(corpCode);
                    corp.setCorpName(corpName);
                    corp.setEmails(email);
                    corp.setLinkman(linkman);
                    corp.setMobile(mobile);
                    corp.setPhone(phone);
                    //默认尾号位数为4
                    corp.setSubnoDigit(4);
                    corp.setLgoUrl(request.getParameter("lgoUrl"));
                    //是否启用计费
                    if (isBalance != null && isBalance != "") {
                        corp.setIsBalance(Integer.parseInt(isBalance));
                    }
                    //新增企业设置企业状态
                    corp.setCorpState(Integer.parseInt(corpState));
                    try {
                        LfDep dep = new LfDep();
                        dep.setDepName(depName);
                        dep.setCorpCode(corpCode);
                        dep.setDepResp("");
                        dep.setSuperiorId(0l);
                        dep.setDepLevel(1);
                        //默认新增的状态为正常.
                        dep.setDepState(1);
                        corp.setCurSubno("0");
                        //添加的方法
                        boolean addRs = enterpriseBiz.addLfCorp(corp, dep);

                        if (addRs) {
                            EmpExecutionContext.info(opModule, loginCorpCode, loginUserId,
                                    loginUserName, "新增企业，企业名称：" + corpName + ",企业编码：" + corpCode, StaticValue.ADD);
                            //EmpExecutionContext.info("模块名称：企业管理，企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员新增一个企业成功,新增的企业编码为："+corpCode+",企业名称为："+corpName+"。");
                        } else {
                            EmpExecutionContext.error("模块名称：企业管理，企业编码[" + loginCorpCode + "],登录账号[" + loginUserName + "]的操作员新增一个企业失败,新增的企业编码为：" + corpCode + ",企业名称为：" + corpName + "。");
                        }

                        request.setAttribute("corpCode", corpCode);
                        request.setAttribute("add", addRs);
                    } catch (Exception e) {
                        EmpExecutionContext.error(e, "新增企业信息异常异常！");
                    }
                }
            } else {
                EmpExecutionContext.error("新增或修改企业信息异常，操作员企业编码非管理级企业，操作员企业编码：" + lgCorpCode);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "新增或修改企业信息异常!");
        } finally {
            find(request, response);
        }
    }

    /**
     * 跳转添加企业管理员页面
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void toAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //当前登录用户
            LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //操作员企业编码
            String lgCorpCode = sysuser.getCorpCode();
            //不为100000的企业编码不允许修改
            if (lgCorpCode != null && "100000".equals(lgCorpCode)) {
                LfSysuser user = new LfSysuser();
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                //企业编码
                String corpCode = request.getParameter("corpCode");
                String action = request.getParameter("action");
                LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
                LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
                conMap.put("corpCode", corpCode);
                orderbyMap.put("depId", "asc");
                List<LfDep> depList = null;
                LfDep dep = null;
                try {
                    //获取当前企业机构信息
                    depList = baseBiz.getByCondition(LfDep.class, conMap, orderbyMap);
                    //获取顶级机构
                    if (depList != null && depList.size() > 0) {
                        dep = depList.get(0);
                    }
                    if (dep == null) {
                        dep = new LfDep();
                    }
                    request.setAttribute("dep", dep);
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "获取当前企业机构信息异常！");
                }
                if ("update".equals(action)) {
                    List<LfSysuser> userList = new ArrayList();
                    conditionMap.put("userName", "admin");
                    conditionMap.put("corpCode", corpCode);
                    try {
                        //获取此企业admin用户的信息
                        userList = baseBiz.getByCondition(LfSysuser.class,
                                conditionMap, null);

                        if (userList.size() > 0) {
                            user = userList.get(0);
                            request.setAttribute("user", user);
                            request.setAttribute("action", "update");
                            request.getRequestDispatcher(
                                    empRoot + basePath + "/cor_corpAdmin.jsp").forward(
                                    request, response);
                        } else {
                            action = "add";
                        }
                    } catch (Exception e) {
                        EmpExecutionContext.error(e, "跳转到企业管理员编辑页面异常！");
                    }
                }
                if ("add".equals(action)) {
                    request.setAttribute("action", action);
                    request.setAttribute("user", user);
                    request.getRequestDispatcher(empRoot + basePath + "/cor_addAdmin.jsp")
                            .forward(request, response);
                }
            } else {
                EmpExecutionContext.error("添加企业管理员异常，操作员企业编码非管理级企业，操作员企业编码：" + lgCorpCode);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "添加企业管理员异常!");
        }

    }

    /**
     * 修改企业管理员
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description 修改添加企业管理员时的逻辑
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-10-15 上午09:21:13
     */
    public void updateAdmin(HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException {

        try {
            //当前登录操作员信息
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            String loginCorpCode = lfSysuser.getCorpCode();
            String loginUserName = lfSysuser.getUserName();

            //不为100000的企业编码不允许修改
            if (loginCorpCode != null && "100000".equals(loginCorpCode)) {
                LfSysuser user = new LfSysuser();
                //获取前台页面控件值
                String name = request.getParameter("name");
                //修改时放用户账号的值
                String username1 = request.getParameter("username1");
                String userName = request.getParameter("username");
                //手机
                String mobile = request.getParameter("mobile");
                //性别
                String sex = request.getParameter("sex");
                //状态
                String state = request.getParameter("state");
                //密码
                String pwd = request.getParameter("newPwd");
                //企业编号
                String corpCode = request.getParameter("corpCode");
                String lgusername = request.getParameter("lgusername");
                user.setName(name);
                user.setUserName(userName);
                user.setMobile(mobile);
                user.setSex(Integer.valueOf(sex));
                user.setHolder(lgusername);
                user.setUserState(Integer.valueOf(state));
                //企业管理员权限为机构权限
                user.setPermissionType(2);
                user.setControlFlag("0000");
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("corpCode", corpCode);
                LfCorp corp = null;
                String loginUserId = "";
                try {
                    loginUserId = String.valueOf(lfSysuser.getUserId());
                    //获取企业信息
                    corp = baseBiz.getByCondition(LfCorp.class, conditionMap, null).get(0);
                    if (corp != null) {
                        corp.setUserName(userName);
                    }
                } catch (Exception e1) {
                    EmpExecutionContext.error(e1, "获取企业信息异常！");
                }
                //管理
                if ("update".equals(request.getParameter("action"))) {
                    try {
                        String id = request.getParameter("id");
                        user.setUserId(Long.valueOf(id));
                        if (pwd != null && !"".equals(pwd)) {
                            //设置密码为 密码+用户名
                            user.setPassword(MD5.getMD5Str(pwd + username1.toLowerCase()));
                        }
//						boolean updateRs = baseBiz.updateObj(user);
                        //解绑IP/MAC地址
                        String ipMac = request.getParameter("ipmac");
                        boolean updateRs = enterpriseManageBiz.updateAdmin(user, ipMac, corpCode);
                        if (updateRs) {
                            EmpExecutionContext.info(opModule, loginCorpCode, loginUserId,
                                    loginUserName, "修改企业管理员，企业编码：" + corpCode + ",管理员的登录账号ID为：" + id,
                                    StaticValue.UPDATE);
                            //EmpExecutionContext.info("模块名称：企业管理，企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员修改管理员成功,修改的企业编码为："+corpCode+",管理员的登录账号ID为："+id+"。");
                        } else {
                            EmpExecutionContext.error("模块名称：企业管理，企业编码[" + loginCorpCode + "],登录账号[" + loginUserName + "]的操作员修改管理员失败,修改的企业编码为：" + corpCode + ",管理员的登录账号ID为：" + id + "。");
                        }

                        request.setAttribute("updateAdmin", updateRs);
                        if (corp != null) {
                            baseBiz.updateObj(corp);
                        }
                    } catch (Exception e) {
                        EmpExecutionContext.error(e, "修改企业管理员信息异常！");
                    } finally {
                        find(request, response);
                    }
                }
                //添加
                if ("add".equals(request.getParameter("action"))) {
                    boolean result = false;
                    try {
                        user.setCorpCode(corpCode);
                        user.setDepId(Long.parseLong(request.getParameter("depId")));
                        user.setGuId(globalBiz.getValueByKey("guid", 1L));
                        String npwd = request.getParameter("nPwd");
                        //设置密码为 密码+用户名
                        user.setPassword(MD5.getMD5Str(npwd + userName.toLowerCase()));
                        user.setUserCode(user.getGuId() + "");
                        //设置默认无尾号
                        user.setIsExistSubNo(0);

                        String depId = request.getParameter("depId");
                        result = enterpriseBiz.addCorpManager(user, depId, corp);

                        if (result) {
                            EmpExecutionContext.info(opModule, loginCorpCode, loginUserId,
                                    loginUserName, "新增企业，企业编码：" + corpCode + ",管理员的登录账号为：" + userName, StaticValue.ADD);

                            //EmpExecutionContext.info("模块名称：企业管理，企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员新增管理员成功,新增的企业编码为："+corpCode+",管理员的登录账号为："+userName+"。");
                        } else {
                            EmpExecutionContext.error("模块名称：企业管理，企业编码[" + loginCorpCode + "],登录账号[" + loginUserName + "]的操作员新增管理员失败,新增的企业编码为：" + corpCode + ",管理员的登录账号为：" + userName + "。");
                        }

                    } catch (Exception e) {
                        EmpExecutionContext.error(e, "添加企业管理员信息异常！");
                    } finally {
                        response.sendRedirect("cor_manager.htm?method=find&addAdmin=" + result);
                    }
                }
            } else {
                EmpExecutionContext.error("添加、修改企业管理员异常，操作员企业编码非管理级企业，操作员企业编码：" + loginCorpCode);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "添加、修改企业管理员异常！");
        }

    }

    /**
     * 检查此企业是否已存在
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void check(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String result;
        try {
            //当前登录用户
            LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //操作员企业编码
            String lgCorpCode = sysuser.getCorpCode();
            //不为100000的企业编码不允许修改
            if (lgCorpCode != null && "100000".equals(lgCorpCode)) {
                //获取企业编码及企业名称
                String corpCode = request.getParameter("corpCode");
                String corpName = request.getParameter("corpName");

                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("corpCode", corpCode);

                try {
                    //获取企业信息
                    list = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
                    if (list.size() > 0) {
                        result = "corpCode";
                        response.getWriter().print(result);
                        return;
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "获取企业信息异常！");
                }
                conditionMap.clear();
                conditionMap.put("corpName", corpName);
                list.clear();
                result = null;
                try {
                    //获取企业信息
                    list = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
                    if (list.size() > 0) {
                        result = "corpName";
                        response.getWriter().print(result);
                        return;
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "验证企业是否已存在异常！");
                }
            } else {
                EmpExecutionContext.error("验证企业是否已存在异常，操作员企业编码非管理级企业，操作员企业编码：" + lgCorpCode);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "验证企业是否已存在异常！");
        }
    }

    /**
     * 检查用户是否存在
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void checkId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //当前登录用户
            LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //操作员企业编码
            String lgCorpCode = sysuser.getCorpCode();
            //不为100000的企业编码不允许修改
            if (lgCorpCode != null && "100000".equals(lgCorpCode)) {
                //获取用户信息及企业编码
                String userId = request.getParameter("userId");
                String corpCode = request.getParameter("corpCode");

                //根据过滤条件查找此用户
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("userName", userId);
                conditionMap.put("corpCode", corpCode);
                List<LfSysuser> list = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
                if (list != null && list.size() > 0) {
                    response.getWriter().print(list.get(0).getUserId() + "");
                    return;
                }
            } else {
                EmpExecutionContext.error("检查企业用户是否已存在异常，操作员企业编码非管理级企业，操作员企业编码：" + lgCorpCode);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "检查企业用户是否已存在异常！");
        }
    }

    /**
     * 企业logo上传
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //当前登录用户
            LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //操作员企业编码
            String lgCorpCode = sysuser.getCorpCode();
            //不为100000的企业编码不允许修改
            if (lgCorpCode != null && "100000".equals(lgCorpCode)) {
                //路径
                String dirUrl = new TxtFileUtil().getWebRoot() + empRoot + "/" + SystemGlobals.getValue(StaticValue.EMP_WEB_FRAME) + StaticValue.LOGOPATH;
                String imgUrl = "";

                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(100 * 1024);
                factory.setRepository(new File(dirUrl));
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(request);
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem fileItem = (FileItem) iter.next();
                    if (fileItem.getSize() - (50 * 1024) > 0) {
                        //超过设置文件的大小限制
                        response.getWriter().print("overSize");
                    } else {
                        if (!fileItem.isFormField() && fileItem.getName().length() > 0 && "fileInput".equals(fileItem.getFieldName())) {
                            imgUrl = "logo_" + System.currentTimeMillis() + fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
                            String name = dirUrl + imgUrl;
                            //存到相应路径下
                            fileItem.write(new File(name));
                        }
                        response.getWriter().print(imgUrl);
                    }
                }
            } else {
                EmpExecutionContext.error("企业logo上传异常，操作员企业编码非管理级企业，操作员企业编码：" + lgCorpCode);
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业logo上传异常！");
            response.getWriter().print(ERROR);
        }
    }


    /**
     * 企业参数设置方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void toParamSet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //当前登录用户
            LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //操作员企业编码
            String lgCorpCode = sysuser.getCorpCode();
            //不为100000的企业编码不允许修改
            if (lgCorpCode != null && "100000".equals(lgCorpCode)) {
                List<LfCorp> corp = new ArrayList<LfCorp>();
                List<LfDep> depList = new ArrayList<LfDep>();
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
                try {
                    //根据过滤条件获取企业信息
                    conditionMap.put("corpCode", request.getParameter("code"));
                    corp = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
                    orderbyMap.put("depId", "asc");
                    //获取此企业机构信息
                    depList = baseBiz.getByCondition(LfDep.class, conditionMap,
                            orderbyMap);
                    String depName = "";
                    if (depList != null && depList.size() > 0) {
                        depName = depList.get(0).getDepName();
                    }
                    //获取此顶级机构的机构名称
                    request.setAttribute("depName", depName);
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "跳转到编辑企业信息异常！");
                }
                //结果返回前台页面
                request.setAttribute("corp", corp.get(0));
                request.setAttribute("type", "update");
            } else {
                EmpExecutionContext.error("编辑企业信息异常，操作员企业编码非管理级企业，操作员企业编码：" + lgCorpCode);
            }
            //跳转页面
            request.getRequestDispatcher(empRoot + basePath + "/cor_ParamSet.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "编辑企业信息异常！");
        }
    }

    /**
     *
     * @throws ServletException
     * @throws IOException
     */
/*	public void setProxyAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String code = request.getParameter("ecid");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("corpId", code);
		map.put("bindAccount", request.getParameter("bindAccount"));
		int count = enterpriseBiz.updateLfCorpBind(map);
		response.getWriter().println(count);
	}*/

    /**
     * 设置富信模块权限
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void setMultimedia(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接收参数
        JSONObject jsonObject = null;
        boolean flag = false;
        try {
            //接收参数
            jsonObject = JsonReceviceUtil.parseToJson(request);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "获取模板列表中参数接收异常");
            response.getWriter().print(flag);
        }

        flag = corManagerBiz.setMultimedia(jsonObject, request, response);
        response.getWriter().print(flag);
    }

    /**
     * 查询富信模块权限
     *
     * @param request
     * @param response
     */
    public Map<String, String> getMultimedia(String corpCode, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<String, String>();
        //接收参数
        map = corManagerBiz.getMultimedia(corpCode, request, response);
        return map;
    }

    public void getMultimedia_orig(HttpServletRequest request, HttpServletResponse response) {
        //接收参数
        JSONObject jsonObject = null;
        try {
            //接收参数
            jsonObject = JsonReceviceUtil.parseToJson(request);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "获取模板列表中参数接收异常");
        }
        corManagerBiz.getMultimedia_orig(jsonObject, request, response);
    }

    public void rmsAuthManage(HttpServletRequest request, HttpServletResponse response) {
        try {
            String pageIndex = request.getParameter("pageIndex");
            String corpCode = request.getParameter("corpCode");
            request.setAttribute("corpCode", corpCode);
            Map<String, String> map = new HashMap<String, String>();
            //获取已存在表的数据
            map = getMultimedia(corpCode, request, response);
            String multimedia = map.get("multimedia");
            String jsonConfig = map.get("jsonConfig");

            List<Object> arr = new ArrayList<Object>();
            if (jsonConfig != null) {
                arr = JSON.parseArray(jsonConfig);
            }
            Map<String, String> mapConfig = new HashMap<String, String>();
            for (int i = 0; i < arr.size(); i++) {
                Object obj = arr.get(i);
                Map<?, ?> m = (Map<?, ?>) obj;
                String type = m.get("type").toString();
                String suppStyle = m.get("suppStyle").toString();
                mapConfig.put(type, suppStyle);
            }
            request.setAttribute("pageIndex", pageIndex);
            request.setAttribute("multimedia", multimedia);
            request.setAttribute("mapConfig", mapConfig);

            request.getRequestDispatcher(empRoot + basePath + "/cor_authConfig.jsp")
                    .forward(request, response);
        } catch (ServletException e) {
            EmpExecutionContext.error(e, "发现异常！");
        } catch (IOException e) {
            EmpExecutionContext.error(e, "发现异常！");
        }
    }


}
