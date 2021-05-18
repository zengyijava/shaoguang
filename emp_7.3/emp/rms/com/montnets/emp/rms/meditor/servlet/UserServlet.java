package com.montnets.emp.rms.meditor.servlet;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.meditor.base.BaseServlet;
import com.montnets.emp.rms.meditor.biz.UserBiz;
import com.montnets.emp.rms.meditor.biz.imp.UserBizImp;
import com.montnets.emp.rms.meditor.tools.JsonReturnUtil;
import com.montnets.emp.rms.meditor.tools.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class UserServlet extends BaseServlet {
    final UserBiz userBiz = new UserBizImp();

    /**
     * 获取当前登录用户信息
     *
     * @param request
     * @param response
     */
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        LfSysuser sysuser = UserUtil.getUser(request);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("corpCode", sysuser.getCorpCode());
        jsonObject.put("userId", sysuser.getUserId());
        if (1 == StaticValue.getCORPTYPE()) {//多企业版(托管版)
            if (sysuser.getCorpCode().equals("100000")) {
                jsonObject.put("isSysmanager", 1);
            } else {
                jsonObject.put("isSysmanager", 0);
            }
        } else {//标准版
            boolean result = userBiz.hasCreatePublicTemp(sysuser.getUserId(), request, response);
            if (result) {
                jsonObject.put("isSysmanager", 1);
            } else {
                jsonObject.put("isSysmanager", 0);
            }
        }
        String authority = userBiz.getAuthority(sysuser.getUserId(), request, response);
        if (authority != null) {
            jsonObject.put("roleArr", com.alibaba.fastjson.JSONObject.parse(authority));
        } else {
            jsonObject.put("roleArr", 0);
        }

        jsonObject.put("userName", sysuser.getUserName());
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("loginCorp", jsonObject);

        //增加编辑器模块权限
        String configName = "modulePer";
        String modulePer = "";
        modulePer = userBiz.getModulePer(sysuser.getUserId(), request, sysuser.getCorpCode(), configName);
        jsonObject1.put("type", "1");
        jsonObject1.put("modulePer", com.alibaba.fastjson.JSONObject.parse(modulePer));
        JsonReturnUtil.success(jsonObject1, request, response);
        return;
    }

    /**
     * 获取可选企业信息
     *
     * @param request
     * @param response
     */
    public void getCorpInfo(HttpServletRequest request, HttpServletResponse response) {
        LfSysuser sysuser = UserUtil.getUser(request);
        JSONObject jsonObject = new JSONObject();
        ArrayList<LfCorp> corps = new ArrayList<LfCorp>();
        LfCorp corp = new LfCorp();
        corp.setCorpCode("100000");
        corp.setCorpName("梦网EMP");
        corp.setIsOpenTD(null);
        corp.setRptflag(null);
        corps.add(corp);
        jsonObject.put("corps", corps);
        JsonReturnUtil.success(jsonObject, request, response);
        return;
    }

}