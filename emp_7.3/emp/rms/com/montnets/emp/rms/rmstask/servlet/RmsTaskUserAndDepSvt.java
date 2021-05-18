package com.montnets.emp.rms.rmstask.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.rmstask.biz.RmsTaskBiz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 处理选择操作员以及机构
 * @author cheng
 * @date 2018-08-08 14:38:53
 */
public class RmsTaskUserAndDepSvt extends BaseServlet {

    private final RmsTaskBiz smstaskBiz=new RmsTaskBiz();
    private final BaseBiz baseBiz = new BaseBiz();
    /**
     * 获取操作员树的方法
     * @param request
     * @param response
     * @throws Exception
     */
    public void createUserTree2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try
        {
            //加请求日志
            EmpExecutionContext.logRequestUrl(request, null);
            Long depId = null;

            String depStr = request.getParameter("depId");
            //String userid = request.getParameter("lguserid");
            if(depStr != null && !"".equals(depStr.trim())){
                depId = Long.parseLong(depStr);
            }
            //如果传入登录操作员ID为空或者undefined，则从Session获取。
//			if(userid==null||"".equals(userid.trim())||"undefined".equals(userid.trim())){
//				EmpExecutionContext.error("群发历史查询获取lguserid参数异常！lguserid="+userid+",depStr="+depStr+"。改成从Session获取。");
//				LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
//				userid=String.valueOf(loginSysuser.getUserId());
//			}
            String requestPath = request.getRequestURI();
            String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));//   比如/p_base/smt_smsTaskRecord.jsp->smsTaskRecord

            //当前登录操作员ID
            LfSysuser currentSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员ID
            Long userid=currentSysuser.getUserId();
            //调用公用创建树的方法
            String departmentTree = getDeptUserJosnData(titlePath,depId,userid,currentSysuser,request);
            response.getWriter().print(departmentTree);
        }
        catch (Exception e)
        {
            response.getWriter().print("");
            EmpExecutionContext.error(e,"群发历史页面获取操作员树的方法 异常！");
        }
    }

    /**
     * 操作员树的加载
     * @param titlePath
     * @param depId
     * @param userid
     * @return
     * @throws Exception
     */
    private  String getDeptUserJosnData(String titlePath,Long depId,Long userid,LfSysuser loginSysuser,HttpServletRequest request){
        StringBuffer tree = new StringBuffer();
        //已注销
        String yzx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_139", request);
        //根据userid获取当前操作员信息
        if(loginSysuser.getPermissionType()==1) {
            tree=new StringBuffer("[]");
        }else {
            List<LfDep> lfDeps;
            List<LfSysuser> lfSysusers = null;

            DepBiz depBiz = new DepBiz();
            try {
                //如果企业编码是10000的用户登录
                if("100000".equals(loginSysuser.getCorpCode()))
                {
                    if(depId == null)
                    {
                        LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
                        LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
                        //只查询顶级机构
                        conditionMap.put("superiorId", "0");
                        //查询未删除的机构
                        conditionMap.put("depState", "1");
                        //排序
                        orderbyMap.put("depId", StaticValue.ASC);
                        orderbyMap.put("deppath", StaticValue.ASC);
                        lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
                    }
                    else
                    {
                        lfDeps = new DepBiz().getDepsByDepSuperId(depId);
                    }
                }else
                {
                    if(depId == null){
                        lfDeps = new ArrayList<LfDep>();
                        LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userid, loginSysuser.getCorpCode()).get(0);
                        lfDeps.add(lfDep);
                    }else{
                        lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, loginSysuser.getCorpCode());
                    }
                }
                //拼结机构树
                LfDep lfDep = null;
                tree = new StringBuffer("[");
                for (int i = 0; i < lfDeps.size(); i++) {
                    lfDep = lfDeps.get(i);
                    tree.append("{");
                    tree.append("id:").append(lfDep.getDepId());
                    tree.append(",name:'").append(lfDep.getDepName()).append("'");
                    tree.append(",pId:").append(lfDep.getSuperiorId());
                    tree.append(",depId:'").append(lfDep.getDepId()).append("'");
                    tree.append(",isParent:").append(true);
                    tree.append(",nocheck:").append(true);
                    tree.append("}");
                    if(i != lfDeps.size()-1){
                        tree.append(",");
                    }
                }

                //	SysuserBiz sysBiz = new SysuserBiz();
                if(depId != null)
                {
                    //如果当前登录用户的企业编码是10000
                    if("100000".equals(loginSysuser.getCorpCode()))
                    {
                        LinkedHashMap<String,String> conMap = new LinkedHashMap<String,String>();
                        conMap.put("userId&<>","1" );
                        conMap.put("depId", depId.toString());
                        lfSysusers = baseBiz.getByCondition(LfSysuser.class, conMap, null);
                    }else
                    {
                        //获取所有状态操作员信息
                        /*lfSysusers = sysBiz.getAllSysusersOfSmsTaskRecordByDep(userid,depId);*/
                        lfSysusers = smstaskBiz.getAllSysusersOfSmsTaskRecordByDep(userid,depId);

                    }
                }
                //拼结操作员信息
                LfSysuser lfSysuser = null;
                if(lfSysusers != null && !lfSysusers.isEmpty()){
                    if(lfDeps.size()>0) {
                        tree.append(",");
                    }
                    for (int i = 0; i < lfSysusers.size(); i++) {
                        //操作员信息
                        lfSysuser = lfSysusers.get(i);
                        tree.append("{");
                        tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
                        tree.append(",name:'").append(lfSysuser.getName()).append("'");
                        if(lfSysuser.getUserState()==2)
                        {
                            tree.append(",name:'").append(lfSysuser.getName()).append("(").append(yzx).append(")'");
                        }else
                        {
                            tree.append(",name:'").append(lfSysuser.getName()).append("'");
                        }
                        tree.append(",pId:").append(lfSysuser.getDepId());
                        tree.append(",depId:'").append(lfSysuser.getDepId()).append("'");
                        tree.append(",isParent:").append(false);
                        tree.append("}");
                        if(i != lfSysusers.size()-1){
                            tree.append(",");
                        }
                    }
                }

                tree.append("]");
            } catch (Exception e) {
                EmpExecutionContext.error(e,"群发历史或群发任务操作员树的加载方法异常！");
            }
        }
        return tree.toString();
    }

    /**
     * 查询条件中的机构树加载方法
     * @param request
     * @param response
     * @throws Exception
     */
    public void createDeptTree(HttpServletRequest request, HttpServletResponse response){
        try {
            //加请求日志
            EmpExecutionContext.logRequestUrl(request, null);
            Long depId = null;
            Long userid=null;
            //部门iD
            String depStr = request.getParameter("depId");
            //操作员账号
            if(depStr != null && !"".equals(depStr.trim())){
                depId = Long.parseLong(depStr);
            }
            //登录操作员ID
            LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员ID
            userid=loginSysuser.getUserId();
            String departmentTree = smstaskBiz.getDepartmentJosnData2(depId, userid,loginSysuser);
            response.getWriter().print(departmentTree);
        }
        catch (Exception e) {
            EmpExecutionContext.error(e,"群发历史或群发任务查询条件中的机构树加载方法异常");
        }
    }
}
