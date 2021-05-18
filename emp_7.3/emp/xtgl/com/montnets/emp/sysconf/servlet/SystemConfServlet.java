package com.montnets.emp.sysconf.servlet;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.sysconf.biz.SysConfBiz;
import com.montnets.emp.sysconf.vo.ParamConfVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * web运行参数配置
 *
 * @author :  tanjy
 * @version :  V1.0
 * @date :  2021-02-02 09:31
 */
public class SystemConfServlet extends BaseServlet {
    private SysConfBiz sysConfBiz = new SysConfBiz();
    private final String empRoot = "xtgl";
    private final String basePath = "/configmanage";
    /**
     * LF_CORP_CONF表数据
     */
    private static final int CORP_CONF_TYPE = 0;
    /**
     * LF_SYS_PARAM表数据
     */
    private static final int SYS_PARAM_TYPE = 1;
    /**
     * LF_GLOBAL_VARIABLE表数据
     */
    private static final int GLOBAL_VARIABLE_TYPE = 2;

    public void find(HttpServletRequest request, HttpServletResponse response) {
        LfSysuser sysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        // 获取企业编码
        String corCode = sysUser.getCorpCode();
        try {
            List<ParamConfVo> allParams = new ArrayList<ParamConfVo>();
            //获取企业配置参数信息
            List<ParamConfVo> corpConfVoList = sysConfBiz.corpConfs();
            // 获取系统配置参数信息
            List<ParamConfVo> sysParamVoList = sysConfBiz.sysParams();
            //获取全局变量参数信息
            List<ParamConfVo> globalVariableList = sysConfBiz.globalParams();

            allParams.addAll(corpConfVoList);
            allParams.addAll(globalVariableList);
            allParams.addAll(sysParamVoList);

            // 从数据库获取对应配置参数值
            sysConfBiz.setParamValues(corCode, allParams);

            request.setAttribute("corpConfVoList", corpConfVoList);
            request.setAttribute("sysParamList", sysParamVoList);
            request.setAttribute("globalVariableList", globalVariableList);
            request.setAttribute("dataSourceConfList", sysConfBiz.getDateSourceConf());
            request.setAttribute("webConfList", sysConfBiz.getWebConf(request));

            request.getRequestDispatcher(this.empRoot + basePath + "/configManage.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "跳转至WEB运行参数配置页面异常！");
            request.setAttribute("findresult", "-1");
            try {
                request.getRequestDispatcher(this.empRoot + basePath + "/configManage.jsp")
                        .forward(request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1, "WEB运行参数配置页面servlet查询异常");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "WEB运行参数配置页面servlet查询跳转异常");
            }
        }
    }

    /**
     * 修改参数配置
     */
    public void saveConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {

        LfSysuser sysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        // 获取企业编码
        String corCode = sysUser.getCorpCode();
        String updateList = request.getParameter("updateList");
        boolean result;
        try {
            // 获取参数配置类型
            int paramTtype = Integer.parseInt(request.getParameter("paramType"));

            Map map = JSONObject.parseObject(updateList, Map.class);
         
            // 检查请求参数值是否为空
            result = sysConfBiz.checkValue(map);
            if (!result) {
                EmpExecutionContext.error("WEB运行参数页面保存异常，存在参数值为空！");
                response.getWriter().print("paramError");
                return;
            }

            // 更新LF_CORP_CONF表数据
            if (paramTtype == CORP_CONF_TYPE) {
                result = sysConfBiz.saveCorpConf(map, corCode);
            }
            //更新LF_SYS_PARAM表数据
            else if (paramTtype == SYS_PARAM_TYPE) {
                result = sysConfBiz.saveSysParam(map);
            }
            //更新LF_GLOBAL_VARIABLE表数据
            else if (paramTtype == GLOBAL_VARIABLE_TYPE) {
                result = sysConfBiz.saveGlobalVariable(map);
            }
            
        } catch (Exception e) {
            EmpExecutionContext.error(e, "WEB运行参数配置页面保存异常，参数:" + updateList);
            result = false;
        }
        response.getWriter().print(result);
    }
}

