package com.montnets.emp.template.servlet;

import com.montnets.emp.common.biz.BadWordsFilterBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.LfTmplRela;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.template.biz.TemplateBiz;
import com.montnets.emp.template.vo.LfTemplateVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 短信模板管理类
 *
 * @功能概要：
 * @项目名称： p_xtgl
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-9-21 上午09:05:56
 * <p>修改记录1：</p>
 * <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
@SuppressWarnings("serial")
public class tem_smsTemplateSvt extends BaseServlet {
    //模版管理biz类实例化
    final TemplateBiz tempBiz = new TemplateBiz();
    //操作员biz类实例化
    final SysuserBiz SysuserBiz = new SysuserBiz();
    //关键字类biz实例化
    final BadWordsFilterBiz badFilter = new BadWordsFilterBiz();
    //操作模块名称
    final String opModule = StaticValue.TEMP_MANAGE;
    //操作员登录用户名称
    final String opSper = StaticValue.OPSPER;
    //页面模块文件名称
    private final String empRoot = "xtgl";
    //页面功能文件名称
    private final String basePath = "/template";
    //公共biz类实例化
    private final BaseBiz baseBiz = new BaseBiz();
    final IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
    //操作日志类实例化
    final SuperOpLog spLog = new SuperOpLog();
    static Map<String, String> infoMap = new HashMap<String, String>();

    static {
        infoMap.put("1", "短信模板");
        infoMap.put("2", "彩信模板");
        infoMap.put("3", "网讯模板");
    }

    /**
     * 短信模板管理进入查询页面方法
     *
     * @param request
     * @param response
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2016-9-21 上午09:23:00
     */
    public void find(HttpServletRequest request, HttpServletResponse response) {

        //模版查询条件类vo
        LfTemplateVo lfTemplateVo = new LfTemplateVo();
        //查询结果模版volist
        List<LfTemplateVo> temList = null;
        //操作员list
        List<LfSysuser> sysList = null;
        //分页对象
        PageInfo pageInfo = new PageInfo();
        //日志开始时间格式化对象
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        long begin_time = System.currentTimeMillis();
        try {
            //初始化分页信息
            pageSet(pageInfo, request);
            //模板名称
            String tmName = request.getParameter("tmName");
            //模板内容
            String tmMsg = request.getParameter("tmMsg");
            //模板状态
            String tmState = request.getParameter("state");
            //模板编号
            String tmCode = request.getParameter("tmCode");
            //当前登录操作员id
            //Long lguserid = Long.valueOf(request.getParameter("lguserid"));
            //漏洞修复 session里获取操作员信息
            Long lguserid = SysuserUtil.longLguserid(request);

            //审核流程id
            String flowIdStr = request.getParameter("flowId");
            //如果模板名称不为空 则去除两侧空格
            if (tmName != null) {
                tmName = tmName.trim();
            }
            //如果模板内容不为空 则去除两侧空格
            if (tmMsg != null) {
                tmMsg = tmMsg.trim();
            }
            //设值模版名称条件
            lfTemplateVo.setTmName(tmName);
            //设值模版内容条件
            lfTemplateVo.setTmMsg(tmMsg);
            //设值模板类型（3-短信模板;4-彩信模板）
            lfTemplateVo.setTmpType(3);
            //判断模版编号是否为空，不为空则去除两侧空格设值到查询条件对象
            if (tmCode != null) {
                tmCode = tmCode.trim();
                //查询条件：模板编号
                lfTemplateVo.setTmCode(tmCode);
            }
            //判断模版状态是否为空，不为空则设置查询条件
            if (request.getParameter("tmState") != null && !"".equals(request.getParameter("tmState"))) {
                //查询条件：模板状态
                lfTemplateVo.setIsPass(Integer.parseInt(request.getParameter("tmState")));
            }
            //判断模板类型(1.通用动态模块;0.通用静态模块;2.智能抓取模块;3.移动财务模块4-系统接入模板)是否为空，不为空则设置查询条件
            if (request.getParameter("dsflag") != null && !"".equals(request.getParameter("dsflag"))) {
                lfTemplateVo.setDsflag(Long.valueOf(request.getParameter("dsflag")));
            }
            //判断创建人名称是否为空，不为空则设置查询条件
            if (request.getParameter("username") != null && !"".equals(request.getParameter("username"))) {
                //查询条件：操作员名称
                lfTemplateVo.setName(request.getParameter("username"));
            }
            if (tmState != null && !"".equals(tmState)) {
                lfTemplateVo.setTmState(Long.valueOf(tmState));
            }
            if (flowIdStr != null && !"".equals(flowIdStr)) {
                lfTemplateVo.setFlowID(Long.valueOf(flowIdStr));
            }
            //条件查询结果集
            temList = tempBiz.getTemplateByCondition(lguserid, lfTemplateVo, pageInfo);
            //获取当前操作员管辖机构下的所有操作员
            sysList = SysuserBiz.getAllSysusers(lguserid);
            request.getSession(false).setAttribute("sysList", sysList);

            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("lfTemplateVo", lfTemplateVo);
            request.setAttribute("temList", temList);
            //增加查询日志
            if (pageInfo != null) {
                long end_time = System.currentTimeMillis();
                String opContent = "查询开始时间：" + format.format(begin_time) + ",耗时:" + (end_time - begin_time) + "ms，数量：" + pageInfo.getTotalRec();
                opSucLog(request, "短信模板", opContent, "GET");
            }
            request.getRequestDispatcher(this.empRoot + this.basePath + "/tem_smsTemplate.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "短信模板查询异常！");
        }
    }

    /**
     * 短信模板新建或修改
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LfTemplate temp = new LfTemplate();
        String opContent;
        String opUser = getOpUser(request);
        //当前登录企业
        String lgcorpcode = SysuserUtil.getCorpcode(request);
        //当前登录操作员id
        Long lguserid = SysuserUtil.longLguserid(request);
        //模板名称
        String tmName = request.getParameter("tmName");
        //模板编号
        String tmCode = request.getParameter("tmCode");
        //模板内容
        String tmMsg = request.getParameter("tmMsg").trim();
        //模板状态
        Long tmState = Long.valueOf(request.getParameter("tmState"));

        Long dsflag = Long.valueOf(request.getParameter("dsflag"));
        //操作类型是新增还是修改
        String opType = request.getParameter("OpType");

        //特殊符号发送回乱码，特殊处理下
        String canshu = MessageUtils.extractMessage("xtgl", "xtgl_spgl_mbsp_cs", request);
        if (tmMsg.length() > 0) {
            tmMsg = tmMsg.replaceAll("•", "·").replace("¥", "￥").replaceAll("\\{#" + canshu + "([1-9][0-9]*)#\\}", "#p_$1#");
        }

        if (tmMsg.length() > 990) {
            tmMsg = tmMsg.substring(0, 990);
        }
        temp.setTmpType(3);
        temp.setTmName(tmName);
        temp.setTmCode(tmCode == null ? "" : tmCode.trim());
        temp.setTmState(tmState);
        temp.setDsflag(dsflag);
        temp.setCorpCode(lgcorpcode);
        temp.setExlJson(" ");
        temp.setVer(" ");

        if (dsflag == 3) {
            temp.setBizCode(request.getParameter("cwcode"));
        }
        Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");

        //新增模板
        if ("add".equals(opType)) {
            try {
                //判断从哪发出的请求，为1是发送模块,3是移动业务，其余说明是从短信模板管理
                String fromState = request.getParameter("fromState");
                request.setAttribute("fromState", fromState);
                opType = StaticValue.ADD;
                opContent = "新建短信模板（模板名称：" + temp.getTmName() + "）";
                String tmmsg = tempBiz.changeParam(tmMsg);
                temp.setTmMsg(tmmsg);
                if (temp.getDsflag() == 4) {
                    int paramcnt = tempBiz.getMsgParamNum(tmmsg);
                    temp.setParamcnt(paramcnt);
                    if (tmCode == null || "".equals(tmCode)) {
                        long tempcode = new SuperDAO().getIdByPro(71, 1);
                        temp.setTmCode(tempcode + "");
                    } else {
                        try {
                            long tempcode = Long.parseLong(tmCode);
                            if (tempcode >= 1000000000) {
                                tempcode = new SuperDAO().getIdByPro(71, 1);
                                temp.setTmCode(tempcode + "");
                            } else {
                                temp.setTmCode(tempcode + "");
                            }
                        } catch (Exception e) {
                            long tempcode = new SuperDAO().getIdByPro(71, 1);
                            temp.setTmCode(tempcode + "");
                        }
                    }
                }
                temp.setUserId(lguserid);
                temp.setIsPass(-1);
                long addTemp = tempBiz.addTemplate(null, temp);
                // 审核信息
                ReviewBiz reviewBiz = new ReviewBiz();
                LfFlow flow = reviewBiz.checkUserFlow(lguserid, temp.getCorpCode(), temp.getTmpType().toString());
                //操作日志内容
                String chgContent = "[模板名称，模板类型，模板内容，模板状态]（" + temp.getTmName() + "，" + temp.getDsflag() + "，" + temp.getTmMsg() + "，" + temp.getTmState() + "）";

                if (addTemp > 0) {
                    //新增成功,判断是否需要审核信息
                    if (flow == null) {
                        request.setAttribute("tmresult", "true");
                    } else {
                        request.setAttribute("tmresult", "pstrue");
                    }

                    //添加操作成功日志
                    spLog.logSuccessString(opUser, opModule, opType, opContent, lgcorpcode);

                } else if (addTemp == -1) {
                    request.setAttribute("tmresult", "noFlower");
                    //添加操作失败日志
                    spLog.logFailureString(opUser, opModule, opType, opContent + opSper, null, lgcorpcode);

                } else {
                    //新增失败
                    request.setAttribute("tmresult", "false");
                    //添加操作失败日志
                    spLog.logFailureString(opUser, opModule, opType, opContent + opSper, null, lgcorpcode);
                }
                //增加操作日志
                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    String opContent1 = "新建短信模板" + (addTemp > 0 ? "成功" : "失败") + "。" + chgContent;
                    EmpExecutionContext.info("短信模板管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                            loginSysuser.getUserName(), opContent1, "ADD");
                }

            } catch (EMPException ex) {
                String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(ex.getMessage());
                request.setAttribute("tmresult", desc);
                EmpExecutionContext.error(ex, "短信模板新建异常！");
            } catch (Exception e) {
                request.setAttribute("tmresult", "error");
                EmpExecutionContext.error(e, "短信模板新建异常！");
            } finally {
                request.getRequestDispatcher(this.empRoot + this.basePath + "/tem_addSmsTemplate.jsp")
                        .forward(request, response);
            }
        }
        //修改模板
        else if ("edit".equals(opType)) {
            try {
                opType = StaticValue.UPDATE;
                opContent = "修改短信模板（模板名称：" + temp.getTmName() + "）";
                String tmmsg = tempBiz.changeParam(tmMsg);
                temp.setTmMsg(tmmsg);
                Long tmId = Long.valueOf(request.getParameter("tmId"));
                temp.setTmid(tmId);
                LfTemplate oddTemp = baseBiz.getById(LfTemplate.class, tmId);
                if (temp.getDsflag() == 4) {
                    int paramcnt = tempBiz.getMsgParamNum(tmmsg);
                    temp.setParamcnt(paramcnt);
                    temp.setTmCode(oddTemp.getTmCode());
                }
                //操作日志内容
                String chgContent = "[模板名称，模板类型，模板内容，模板状态]" +
                        "（" + oddTemp.getTmName() + "，" + oddTemp.getDsflag() + "，" + oddTemp.getTmMsg() + "，" + oddTemp.getTmState() + "）->" +
                        "（" + temp.getTmName() + "，" + temp.getDsflag() + "，" + temp.getTmMsg() + "，" + temp.getTmState() + "）";

                boolean upTemp = baseBiz.updateObj(temp);
                //修改成功
                if (upTemp) {
                    request.setAttribute("tmresult", "true");
                    //添加操作成功日志
                    spLog.logSuccessString(opUser, opModule, opType, opContent, lgcorpcode);

                } else {
                    //修改失败
                    request.setAttribute("tmresult", "false");
                    //添加操作失败日志
                    spLog.logFailureString(opUser, opModule, opType, opContent + opSper, null, lgcorpcode);
                }

                //增加操作日志
                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    String opContent1 = "修改短信模板" + (upTemp ? "成功" : "失败") + "。" + chgContent;
                    EmpExecutionContext.info("短信模板管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                            loginSysuser.getUserName(), opContent1, "UPDATE");
                }
            } catch (Exception e) {
                request.setAttribute("tmresult", "error");
                EmpExecutionContext.error(e, "短信模板修改异常！");
            } finally {
                doEdit(request, response);
            }
        }
    }

    /**
     * 检查编码是否重复
     *
     * @param request
     * @param response
     */
    public void checkRepeat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        String tmCode = request.getParameter("tmCode");
        String corpCode = request.getParameter("corpCode");
        String hiddenCode = request.getParameter("hiddenCode");
        tmCode = tmCode == null ? "" : tmCode.trim();
        if (tmCode != null && tmCode.equals(hiddenCode)) {
            response.getWriter().print("");
        } else {
            conditionMap.put("tmCode", tmCode);
            conditionMap.put("corpCode", corpCode);
            //添加必要类型
            conditionMap.put("tmpType", "3");
            //状态为启用
            try {
                List<LfTemplate> tempList = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
                if (tempList != null && tempList.size() > 0) {
                    response.getWriter().print("repeat");
                }

            } catch (Exception e) {
                EmpExecutionContext.error(e, "检查编码是否重复异常！");
                //异步返回结果
                response.getWriter().print("");
            }
        }
    }


    /**
     * 短信模板删除
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String opType = null, opContent = null;
        String opUser = getOpUser(request);
        opType = StaticValue.DELETE;
        //当前登录企业
        String lgcorpcode = request.getParameter("lgcorpcode");

        try {
            //删除模板id字符串
            String idstr = request.getParameter("ids");
            if (idstr != null && !"".equals(idstr)) {
                idstr = idstr.toString();
            } else {
                response.getWriter().print(0);
                opContent = "模板Id为空！";
                EmpExecutionContext.error("模块名称：" + opModule + "，企业编码：" + lgcorpcode + "，操作员：" + opUser + "，删除模板失败！" + opContent);
                return;
            }
            String[] ids = idstr.split(",");

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("tmid&in", idstr);
            List<LfTemplate> tempList = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
            //操作日志内容
            String chgContent = "[模板ID，模板名称，模板类型，模板内容，模板状态]";
            if (tempList.size() > 0) {
                for (int i = 0; i < tempList.size(); i++) {
                    chgContent += "(" + tempList.get(i).getTmid() + "，" + tempList.get(i).getTmName() + "，"
                            + tempList.get(i).getDsflag() + "，" + tempList.get(i).getTmMsg() + "，" + tempList.get(i).getTmState() + ")";
                }
            } else {
                //当模板已被其他删除
                response.getWriter().print(-1);
                opContent = "传入的ID在库中已删除！";
                EmpExecutionContext.error("模块名称：" + opModule + "，企业编码：" + lgcorpcode + "，操作员：" + opUser + "，删除模板失败！" + opContent);
                return;
            }

            int count = tempBiz.delTempByTmId(ids);
            opContent = "删除" + ids.length + "条短信模板";
            //异步返回删除条数
            response.getWriter().print(count);
            //操作日志
            spLog.logSuccessString(opUser, opModule, opType, opContent, lgcorpcode);

            //增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            //增加操作日志
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent1 = "删除" + ids.length + "条短信模板" + (count > 0 ? "成功" : "失败") + "。" + chgContent;
                EmpExecutionContext.info("短信模板管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                        loginSysuser.getUserName(), opContent1, "DELETE");
            }

        } catch (Exception e) {
            response.getWriter().print(0);
            //添加操作失败日志
            spLog.logFailureString(opUser, opModule, opType, opContent + opSper, e, lgcorpcode);
            EmpExecutionContext.error(e, "短信模板删除异常！");
        }
    }

    /**
     * 获取短信模板信息
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getTmMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //模板id
        String tmId = request.getParameter("tmId");
        try {
            if ("".equals(tmId)) {
                response.getWriter().print("");
            } else {
                LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
                //异步返回模板内容
                response.getWriter().print(template.getTmMsg());
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取短信模板信息异常！");
        }
    }

    /**
     * 获取短信模板信息
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
                LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
                result = template.getTmMsg();
            }
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "获取短信模板信息异常！");
        } finally {
            //异步返回操作结果
            response.getWriter().print("@" + result);
        }
    }

    /**
     * 检查关键字
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void checkBadWord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //过滤关键字
        String words = "";
        try {
            //模板内容
            String tmMsg = request.getParameter("tmMsg");
            //模板名称
            //String tmName=request.getParameter("tmName");
//			String checkName=request.getParameter("checkName");
            if (tmMsg != null && !"".equals(tmMsg)) {
                tmMsg = tmMsg.toUpperCase();
            }

            words = badFilter.checkText(tmMsg);

        } catch (Exception e) {
            words = "error";
            EmpExecutionContext.error(e, "短信模板检查关键字异常！");
        } finally {
            //异步返回结果
            response.getWriter().print(words);
        }
    }


    /**
     * 检查关键字
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void checkBadWord1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //过滤关键字
        String words = "";
        try {
            //模板内容
            String tmMsg = request.getParameter("tmMsg");
            //模板名称
            //String tmName=request.getParameter("tmName");
//			String checkName=request.getParameter("checkName");
            if (tmMsg != null && !"".equals(tmMsg)) {
                //转化大写
                tmMsg = tmMsg.toUpperCase();
            }

            words = badFilter.checkText(tmMsg);

        } catch (Exception e) {
            words = "error";
            EmpExecutionContext.error(e, "短信模板检查关键字异常！");
        } finally {
            //异步返回结果
            response.getWriter().print("@" + words);
        }
    }


    /**
     * 短信模板新建页面跳转方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //判断从哪发出的请求，为1是发送模块,3是移动业务模块，其余说明是从短信模板管理
        String fromState = request.getParameter("fromState");
        try {
            //获取业务类型
            List<LfBusManager> busList = baseBiz.getEntityList(LfBusManager.class);
            request.setAttribute("busList", busList);
            request.setAttribute("fromState", fromState);
            //跳转到短信模板、发送模块界面
            request.getRequestDispatcher(this.empRoot + this.basePath + "/tem_addSmsTemplate.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "短信模板新建页面跳转异常！");
            request.setAttribute("fromState", fromState);
            request.getRequestDispatcher(this.empRoot + this.basePath + "/tem_addSmsTemplate.jsp")
                    .forward(request, response);
        }
    }

    /**
     * 短信模板修改页面跳转方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throw
     */
    public void doEdit(HttpServletRequest request, HttpServletResponse response) {
        //编辑模板
        try {
            //模板id
            String tmId = request.getParameter("tmId");
            if (tmId != null && !"".equals(tmId)) {
                //根据id查询模板
                LfTemplate tem = baseBiz.getById(LfTemplate.class, tmId);
                request.setAttribute("temp", tem);
                //获取业务类型
                List<LfBusManager> busList = baseBiz.getEntityList(LfBusManager.class);
                request.setAttribute("busList", busList);
                request.getRequestDispatcher(this.empRoot + this.basePath + "/tem_editSmsTemplate.jsp")
                        .forward(request, response);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "短信模板修改页面跳转异常！");
        }
    }

    /**
     * 获取短信模板下拉列表
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getTmAsOption(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //只能引擎模板
            conditionMap.put("tmpType", "3");
            //状态为启用
            conditionMap.put("tmState", "1");
            //无需审核或者通过审核
            conditionMap.put("isPass&in", "0,1");
            //conditionMap.put("userId", getUserId().toString());
            conditionMap.put("dsflag", request.getParameter("dsflag"));

            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("tmid", StaticValue.ASC);
            //当前登录操作员userid
            //Long lguserid = Long.valueOf(request.getParameter("lguserid"));//当前登录操作员id
            //漏洞修复 session里获取操作员信息
            Long lguserid = SysuserUtil.longLguserid(request);


            List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class, lguserid, conditionMap, orderbyMap);
            response.getWriter().print("<option value='' class='optionFirst'>请选择短信模板</option>");
            if (tmpList != null) {
                for (LfTemplate temp : tmpList) {
                    if (temp.getIsPass() == 0 || temp.getIsPass() == 1) {
                        //生成select控件的html代码
                        response.getWriter().print("<option value='" + temp.getTmid() + "'>" + temp.getTmName().replace("<", "&lt;").replace(">", "&gt;") + "(ID:" + temp.getTmid() + ")</option>");

                    }
                }
            } else {
                response.getWriter().print("");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取短信模板下拉列表异常！");
            response.getWriter().print("");
        }
    }

    /**
     * 修改短信模板状态
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void changeState(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String tempId = request.getParameter("tempId");
            //1.启用，0.禁用
            String tempState = request.getParameter("tempState");
            String lgcorpcode = request.getParameter("lgcorpcode");

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();

            conditionMap.put("corpCode", lgcorpcode);
            conditionMap.put("tmid", tempId);

            objectMap.put("tmState", tempState);
            boolean result = baseBiz.update(LfTemplate.class, objectMap, conditionMap);
            response.getWriter().print(result);

            //增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent1 = "更改短信模板状态" + (result == true ? "成功" : "失败") + "。[模板状态](" + tempState + ")";
                EmpExecutionContext.info("短信模板管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                        loginSysuser.getUserName(), opContent1, "UPDATE");
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "修改短信模板状态异常！");
            response.getWriter().print("false");
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
            conditionMap.put("infoType", "3");
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
                            member.put("smsexstate", MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_wsp", request));
                            break;
                        case 1:
                            member.put("smsexstate", MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_sptg", request));
                            break;
                        case 2:
                            member.put("smsexstate", MessageUtils.extractMessage("xtgl", "xtgl_spgl_xxsp_spbtg", request));
                            break;
                        default:
                            member.put("smsexstate", "[" + MessageUtils.extractMessage("xtgl", "xgtl_spgl_xxsp_wxdbs", request) + "]");
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
            conditionMap.put("infoType", "3");
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
                        if (lastrecord.getRLevel() != null && !lastrecord.getRLevel().equals(lastrecord.getRLevelAmount())) {
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
            EmpExecutionContext.error(e, "获取信息发送查询里的详细异常！");
        }
    }

    //创建机构树
    public void createinstalltree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //机构id
            String depId = request.getParameter("depId");
            //String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);


//			String tempId = request.getParameter("tempId");
            String tree = getinstallTree(depId, lguserid);
            response.getWriter().print(tree);
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "创建共享模板机构树出现异常！");
        }
    }

    //获取机构树
    private String getinstallTree(String depId, String lguserid) {
        StringBuffer tree = new StringBuffer("[");
        try {
            LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
            String corpCode = lfsysuser.getCorpCode();
            //排序集合
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            //查询条件集合
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //机构状态
            conditionMap.put("depState", "1");
            orderbyMap.put("depId", StaticValue.ASC);
            //企业编码
            conditionMap.put("corpCode", corpCode);
            //DEPID为空
            if (depId != null && !"".equals(depId)) {
                //父及机构id
                conditionMap.put("superiorId", depId);
            }
            List<LfDep> lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
            LfDep lfDep = null;
            for (int i = 0; i < lfDeps.size(); i++) {
                lfDep = lfDeps.get(i);
                tree.append("{");
                tree.append("id:").append(lfDep.getDepId());
                tree.append(",name:'").append(lfDep.getDepName()).append("'");
                tree.append(",pId:").append(lfDep.getSuperiorId());
                tree.append(",depId:'").append(lfDep.getDepId()).append("'");
                tree.append(",isParent:").append(true);
                tree.append(",checked:").append(false);
                tree.append("}");
                if (i != lfDeps.size() - 1) {
                    tree.append(",");
                }
            }
            tree.append("]");
        } catch (Exception e) {
            EmpExecutionContext.error(e, "创建共享模板的机构树出现异常！");
            tree = new StringBuffer("");
        }
        return tree.toString();

    }

    /**
     * 获取该机构下的操作员
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSysuserByDepId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //企业编码
            String lgcorpcode = request.getParameter("lgcorpcode");
            //机构ID
            String depId = request.getParameter("depId");
            //查询名称
            String searchname = request.getParameter("searchname");

            StringBuffer sql = new StringBuffer();
            sql.append("select * from LF_SYSUSER where CORP_CODE='");
            sql.append(lgcorpcode).append("'");
            sql.append(" and USER_STATE<2");
            if (depId != null && !"".equals(depId)) {
                sql.append(" and DEP_ID=").append(depId);
            }
            if (searchname != null && !"".equals(searchname)) {
                sql.append(" and (NAME like '%").append(searchname).append("%'");
                sql.append(" or MOBILE like '%").append(searchname).append("%')");
            }
            sql.append(" order by NAME asc");

            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageSize(Integer.MAX_VALUE);
            List<LfSysuser> lfsysuserList = genericDAO.findPageEntityListBySQLNoCount(LfSysuser.class, String.valueOf(sql), null, pageInfo, StaticValue.EMP_POOLNAME);

            StringBuffer sb = new StringBuffer("suceess#");
           /* //排序集合
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            //查询条件集合
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            if (depId != null && !"".equals(depId)) {
                conditionMap.put("depId", depId);
            }
            conditionMap.put("userState&<", "2");
            orderbyMap.put("name", "asc");
            if (searchname != null && !"".equals(searchname)) {
                conditionMap.put("name&like", searchname);
            }*/

            //List<LfSysuser> lfsysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, orderbyMap);
            if (lfsysuserList != null && lfsysuserList.size() > 0) {
                StringBuffer buffer = new StringBuffer();
                for (LfSysuser user : lfsysuserList) {
                    buffer.append(user.getUserId()).append(",");
                    sb.append("<option value='").append(user.getUserId()).append("' isdeporuser='2' >");
                    sb.append(user.getName().trim().replace("<", "&lt;").replace(">", "&gt;"));
                    sb.append("</option>");
                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "获取该机构下的操作员失败！");
        }
    }

    /**
     * 设置模板共享对象
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void updateShareTemp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LfSysuser lfsysuser = null;
        String tempId = null;
        String infoType = "1";
        try {
            //用户ID
            //String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);

            lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
            //所设置的机构对象
            String depidstr = request.getParameter("depidstr");
            //所设置的操作员对象
            String useridstr = request.getParameter("useridstr");
            //该模板Id
            tempId = request.getParameter("tempid");
            //模板类型 1:短信模板  2：彩信模板
            infoType = request.getParameter("infotype");

            //查询原来记录
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("templId", tempId);
            conditionMap.put("templType", infoType);
            conditionMap.put("shareType", "1");
            List<LfTmplRela> bindobjList = baseBiz.getByCondition(LfTmplRela.class, conditionMap, null);
            //保存操作员的USERID
            HashSet<Long> userSet = new HashSet<Long>();
            //保存机构的USERID
            HashSet<Long> depSet = new HashSet<Long>();
            if (bindobjList != null && bindobjList.size() > 0) {
                for (int i = 0; i < bindobjList.size(); i++) {
                    LfTmplRela temp = bindobjList.get(i);
                    //，1-机构，2--操作员
                    if (temp.getToUserType() == 2 && !userSet.contains(temp.getToUser())) {
                        userSet.add(temp.getToUser());
                    } else if (temp.getToUserType() == 1 && !depSet.contains(temp.getToUser())) {
                        depSet.add(temp.getToUser());
                    }
                }
            }

            //设置共享
            String returnmsg = tempBiz.updateShareTemp(depidstr, useridstr, tempId, infoType, lfsysuser);
            response.getWriter().print(returnmsg);

            //增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent1 = "设置共享短信模板状态" + ("success".equals(returnmsg) ? "成功" : "失败") + "。[模板Id，机构对象，操作员对象]" +
                        "(" + tempId + "，" + depSet.toString() + "，" + userSet.toString() + ")->" +
                        "(" + tempId + "，[" + (depidstr.indexOf(",") > -1 ? depidstr.substring(0, depidstr.length() - 1) : depidstr) + "]，[" + (useridstr.indexOf(",") > -1 ? useridstr.substring(0, useridstr.length() - 1) : useridstr) + "])";
                EmpExecutionContext.info("短信模板管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                        loginSysuser.getUserName(), opContent1, "UPDATE");
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "更新模板共享对象出错，" + infoMap.get(infoType) + "ID:" + tempId);
            response.getWriter().print("");
        }
    }

    /**
     * 列表页面获取已设置模板共享对象
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String tempId = request.getParameter("tempid");
            String infoType = request.getParameter("infoType");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("templId", tempId);
            conditionMap.put("templType", infoType);
            conditionMap.put("shareType", "1");
            List<LfTmplRela> bindobjList = baseBiz.getByCondition(LfTmplRela.class, conditionMap, null);
            StringBuffer sb = new StringBuffer();
            //保存操作员的USERID
            HashSet<Long> userSet = new HashSet<Long>();
            //保存机构的USERID
            HashSet<Long> depSet = new HashSet<Long>();
            if (bindobjList != null && bindobjList.size() > 0) {
                for (int i = 0; i < bindobjList.size(); i++) {
                    LfTmplRela temp = bindobjList.get(i);
                    //，1-机构，2--操作员
                    if (temp.getToUserType() == 2 && !userSet.contains(temp.getToUser())) {
                        userSet.add(temp.getToUser());
                        LfSysuser user = baseBiz.getById(LfSysuser.class, temp.getToUser());
                        sb.append("<option value=\'" + temp.getToUser() + "\' isdeporuser='2'>[操作员]" + user.getName());
                        sb.append("</option>");
                    } else if (temp.getToUserType() == 1 && !depSet.contains(temp.getToUser())) {
                        depSet.add(temp.getToUser());
                        LfDep dep = baseBiz.getById(LfDep.class, temp.getToUser());
                        sb.append("<option value=\'" + temp.getToUser() + "\' isdeporuser='1'>[机构]" + dep.getDepName() + "</option>");
                    }
                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "列表页面获取设置模板共享对象出现异常！");
            response.getWriter().print("");
        }
    }

    /**
     * 获取操作员名称
     *
     * @param request
     * @return
     * @description
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-4-2 上午09:17:49
     */
    public String getOpUser(HttpServletRequest request) {
        String opUser = "";
        try {
            Object sysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (sysuserObj != null) {
                LfSysuser sysuser = (LfSysuser) sysuserObj;
                opUser = sysuser.getUserName();
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "session获取操作员名称异常！");
        }
        return opUser;
    }

    /**
     * @param request
     * @param modName   模块名称
     * @param opContent 操作详情
     * @param opType    操作类型 ADD UPDATE DELETE GET OTHER
     * @description 记录操作成功日志
     * @author zousy <zousy999@qq.com>
     * @datetime 2015-3-3 上午11:29:50
     */
    public void opSucLog(HttpServletRequest request, String modName, String opContent, String opType) {
        LfSysuser lfSysuser = null;
        try {
            Object obj = request.getSession(false).getAttribute("loginSysuser");
            if (obj == null) return;
            lfSysuser = (LfSysuser) obj;
            EmpExecutionContext.info(modName, lfSysuser.getCorpCode(), String.valueOf(lfSysuser.getUserId()), lfSysuser.getUserName(), opContent, opType);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "记录操作日志异常！");
        }

    }

}
