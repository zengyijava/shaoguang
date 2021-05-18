package com.montnets.emp.wzgl.form.svt;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.form.LfFomField;
import com.montnets.emp.entity.form.LfFomFieldvalue;
import com.montnets.emp.entity.form.LfFomInfo;
import com.montnets.emp.entity.form.LfFomQuestion;
import com.montnets.emp.entity.form.LfFomType;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.ottbase.param.WeixParams;
import com.montnets.emp.ottbase.service.WeixService;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wzgl.form.biz.WzglFormBiz;

/**
 * @description 微站的表单设计
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-12-23 下午02:58:58
 */
public class wzgl_formManagerSvt extends BaseServlet
{

    /**
     * @description
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-23 下午02:49:15
     */
    private static final long   serialVersionUID = 470319449802655536L;

    private static final String FORM_PATH        = "/wzgl/form";

    private final BaseBiz             baseBiz          = new BaseBiz();

    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        // 是否是弹出框显示列表
        String isArtDialog = request.getParameter("isArtDialog");
        try
        {
            boolean isFirstEnter = pageSet(pageInfo, request);
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            if(lgcorpcode == null || "".equals(lgcorpcode))
            {
                EmpExecutionContext.error("wzgl_formManagerSvt.find.lgcorpcode:" + lgcorpcode);
                return;
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            // 自定义表单
            conditionMap.put("isSystem", "0");
            // 正常表单状态
            conditionMap.put("publishState", "1");
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("fId", StaticValue.DESC);
            if(!isFirstEnter)
            {
                // 表单标题
                String formtitle = request.getParameter("formtitle");
                if(formtitle != null && !"".equals(formtitle))
                {
                    conditionMap.put("title&like", formtitle);
                }
                // 开始时间
                String startdate = request.getParameter("startdate");
                if(startdate != null && !"".equals(startdate))
                {
                    conditionMap.put("createtime&>", startdate);
                }
                // 结束时间
                String enddate = request.getParameter("enddate");
                if(enddate != null && !"".equals(enddate))
                {
                    conditionMap.put("createtime&<", enddate);
                }
            }
            List<LfFomInfo> otFomInfoList = baseBiz.getByConditionNoCount(LfFomInfo.class, null, conditionMap, orderbyMap, pageInfo);
            request.setAttribute("otFomInfoList", otFomInfoList);
            request.setAttribute("conditionMap", conditionMap);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "wzgl_formManagerSvt.find Is Error");
        }
        finally
        {
            request.setAttribute("pageInfo", pageInfo);
            if("true".equals(isArtDialog))
            {
                request.getRequestDispatcher(FORM_PATH + "/wzgl_artDialogForm.jsp").forward(request, response);
            }
            else
            {
                request.getRequestDispatcher(FORM_PATH + "/wzgl_formManager.jsp").forward(request, response);
            }

        }
    }

    /**
     * @description 新建 /修改/查看/套用模板 /统计 表单的跳转
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-23 下午04:56:37
     */
    public void toSavePageForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 修改 套用 查看 跳的页面
        String pageUrl = "/wzgl_saveForm.jsp";
        try
        {
            // 提交类型 add跳转选择模板 defined自定义创建 edit编辑 preview 查看 apply 套用模板创建
            String handletype = request.getParameter("handletype");
            if(handletype == null || "".equals(handletype))
            {
                EmpExecutionContext.error("wzgl_formManagerSvt.toSavePageForm.handletype:" + handletype);
                return;
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            // 创建表单页面跳转到选择模板类型页面
            if("add".equals(handletype))
            {
                pageUrl = "/wzgl_formType.jsp";
                // 表单风格类型属于系统: 1是 0否
                conditionMap.put("isSystem", "1");
                orderbyMap.put("typeId", StaticValue.ASC);
                // 表单风格类型
                List<LfFomType> fomTypeList = baseBiz.getByCondition(LfFomType.class, conditionMap, orderbyMap);

                if(fomTypeList != null && fomTypeList.size() > 0)
                {
                    LfFomType fomType = fomTypeList.get(0);
                    String typeId = String.valueOf(fomType.getTypeId());
                    String parentId = String.valueOf(fomType.getParentId());
                    request.setAttribute("typeId", typeId);
                    request.setAttribute("parentId", parentId);
                }
                request.setAttribute("fomTypeList", fomTypeList);

            }
            else if("edit".equals(handletype) || "preview".equals(handletype) || "apply".equals(handletype) || "statistics".equals(handletype))
            {
                // 表单ID
                String formid = request.getParameter("formid");
                if(formid == null || "".equals(formid))
                {
                    EmpExecutionContext.error("wzgl_formManagerSvt.toSavePageForm.formid:" + formid);
                    return;
                }
                // 获取表单信息对象
                LfFomInfo fomInfo = baseBiz.getById(LfFomInfo.class, formid);
                if(fomInfo == null)
                {
                    EmpExecutionContext.error("wzgl_formManagerSvt.toSavePageForm.LfFomInfo is null");
                    return;
                }

                conditionMap.put("fId", fomInfo.getFId().toString());
                // conditionMap.put("corpCode", fomInfo.getCorpCode());
                orderbyMap.put("seqNum", StaticValue.ASC);
                // 查询该表单 所对应的问题 按顺序来
                List<LfFomQuestion> questionList = baseBiz.getByCondition(LfFomQuestion.class, conditionMap, orderbyMap);
                StringBuffer buffer = new StringBuffer();
                // 老问题对象
                LfFomQuestion fomQuestion = null;
                // 循环获取问题的ID
                if(questionList != null && questionList.size() > 0)
                {
                    for (int i = 0; i < questionList.size(); i++)
                    {
                        fomQuestion = questionList.get(i);
                        buffer.append(fomQuestion.getQId());
                        if(i != questionList.size() - 1)
                        {
                            buffer.append(",");
                        }
                    }
                }
                // 老的问题所对应的选项
                List<LfFomField> fomFieldList = null;
                // 查询问题所对应的选项 按新增的顺序
                if(buffer.toString().length() > 0)
                {
                    conditionMap.clear();
                    orderbyMap.clear();
                    conditionMap.put("qid&in", buffer.toString());
                    orderbyMap.put("fieldId", StaticValue.ASC);
                    fomFieldList = baseBiz.getByCondition(LfFomField.class, conditionMap, orderbyMap);
                    buffer.setLength(0);
                }
                if("statistics".equals(handletype))
                {
                    // 跳转到统计页面
                    pageUrl = "/wzgl_statisticsForm.jsp";
                    // 选项对应的被选次数
                    // 问题所对应的选项LIST
                    LinkedHashMap<String, List<LfFomField>> fomFieldMap = new LinkedHashMap<String, List<LfFomField>>();
                    // 循环问题
                    List<LfFomField> tempFomFieldList = null;
                    LfFomField fomField = null;
                    for (int m = 0; m < questionList.size(); m++)
                    {
                        fomQuestion = questionList.get(m);
                        if(fomFieldList != null && fomFieldList.size() > 0)
                        {
                            tempFomFieldList = new ArrayList<LfFomField>();
                            for (int n = 0; n < fomFieldList.size(); n++)
                            {
                                fomField = fomFieldList.get(n);
                                if(fomQuestion.getQId().toString().equals(fomField.getQid().toString()))
                                {
                                    tempFomFieldList.add(fomField);
                                }
                            }
                            fomFieldMap.put(fomQuestion.getQId().toString(), tempFomFieldList);
                        }
                    }
                    request.setAttribute("fomFieldMap", fomFieldMap);
                    // 循环选项.获取其值
                    if(fomFieldList != null && fomFieldList.size() > 0)
                    {
                        conditionMap.clear();
                        conditionMap.put("fId", fomInfo.getFId().toString());
                        conditionMap.put("corpCode", fomInfo.getCorpCode());
                        List<LfFomFieldvalue> fieldValueList = baseBiz.getByCondition(LfFomFieldvalue.class, conditionMap, null);
                        if(fieldValueList != null && fieldValueList.size() > 0)
                        {
                            LinkedHashMap<String, Integer> fieldCountMap = new LinkedHashMap<String, Integer>();
                            LfFomFieldvalue fomFieldValue = null;
                            String fieldid = null;
                            for (int m = 0; m < fomFieldList.size(); m++)
                            {
                                fomField = fomFieldList.get(m);
                                fieldid = fomField.getFieldId().toString();
                                if(fieldValueList != null && fieldValueList.size() > 0)
                                {
                                    for (int n = 0; n < fieldValueList.size(); n++)
                                    {
                                        fomFieldValue = fieldValueList.get(n);
                                        if(fieldid.equals(fomFieldValue.getFieldId().toString()))
                                        {
                                            Integer count = fieldCountMap.get(fieldid);
                                            if(count == null)
                                            {
                                                fieldCountMap.put(fieldid, 1);
                                            }
                                            else
                                            {
                                                fieldCountMap.put(fieldid, count + 1);
                                            }
                                        }
                                    }
                                }
                            }
                            request.setAttribute("fieldCountMap", fieldCountMap);
                        }
                    }
                }

                request.setAttribute("otFomInfo", fomInfo);
                request.setAttribute("questionList", questionList);
                request.setAttribute("fomFieldList", fomFieldList);
            }
            else if("defined".equals(handletype))
            {
                // 自定义跳转
                conditionMap.clear();
                // s属于系统 1 是 0否
                conditionMap.put("isSystem", "1");
                // 查询出除出热门之外的表单类型
                conditionMap.put("parentId", "1");
                orderbyMap.put("typeId", StaticValue.ASC);
                List<LfFomType> fomTypeList = baseBiz.getByCondition(LfFomType.class, conditionMap, orderbyMap);
                request.setAttribute("fomTypeList", fomTypeList);
            }
            request.setAttribute("handletype", handletype);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "wzgl_formManagerSvt.toSavePageForm Is Error");
        }
        finally
        {
            request.getRequestDispatcher(FORM_PATH + pageUrl).forward(request, response);
        }
    }

    /**
     * @description 表单新增跳转系统表单列表
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2014-2-12 下午05:23:10
     */
    public void toFormTempalte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        try
        {
            boolean isFirstEnter = pageSet(pageInfo, request);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            if(!isFirstEnter)
            {
                // 表示是选择的右侧的 表单类型按纽 click 操作 还是分页操作 page
                String type = request.getParameter("type");
                // 点分页页码
                String pageIndex = request.getParameter("pageIndex");
                if("page".equals(type) && pageIndex != null && !"".equals(pageIndex))
                {
                    pageInfo.setPageIndex(Integer.valueOf(pageIndex));
                }
                else
                {
                    // 设置默认查询的是第一页的信息
                    pageInfo.setPageIndex(1);
                }
            }

            // 表单类型ID
            String typeId = request.getParameter("typeId");
            // 表单父ID 为0则为热门,
            String parentId = request.getParameter("parentId");
            if(parentId != null && !"".equals(parentId) && !"0".equals(parentId))
            {
                // 为 0则表示是选择热门 则表示的是对应类型
                conditionMap.put("typeId", typeId);
            }

            // s属于系统 1 是 0否
            conditionMap.put("isSystem", "1");
            // 查询出除了自定义类型(-1)以外的系统表单
            conditionMap.put("parentId", "0");
            // 被当作模板使用次数
            orderbyMap.put("submitCount", StaticValue.DESC);
            orderbyMap.put("fId", StaticValue.ASC);
            pageInfo.setPageSize(4);
            List<LfFomInfo> otFomInfoList = baseBiz.getByConditionNoCount(LfFomInfo.class, null, conditionMap, orderbyMap, pageInfo);
            request.setAttribute("otFomInfoList", otFomInfoList);
            request.setAttribute("pageInfo", pageInfo);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "wzgl_formManagerSvt.toFormTempalte Is Error");
        }
        finally
        {
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(FORM_PATH + "/wzgl_formTemplate.jsp").forward(request, response);
        }
    }

    /**
     * @description 自定义创建/编辑/套用模板创建表单
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-24 下午03:20:37
     */
    public void saveForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            // 描述
            String form_note = request.getParameter("form_note");
            // 标题
            String form_title = request.getParameter("form_title");
            // 问题信息
            String questionmsg = request.getParameter("questionmsg");
            // 企业编码
            String corpCode = request.getParameter("corpCode");
            // 提交类型 add创建 edit编辑
            String handletype = request.getParameter("handletype");
            // 表单ID
            String formid = request.getParameter("formid");

            String pathUrl = request.getParameter("path");

            if(form_title == null || "".equals(form_title) || questionmsg == null || "".equals(questionmsg) || corpCode == null || "".equals(corpCode) || handletype == null || "".equals(handletype))
            {
                EmpExecutionContext.error("wzgl_formManagerSvt.saveForm.form_title:" + form_title + ",questionmsg:" + questionmsg + ",corpCode:" + corpCode + ",handletype:" + handletype);
                response.getWriter().print("paramisnull");
                return;
            }
            LfFomInfo fominfo_Old = null;
            LfFomInfo fominfo_New = null;
            // 修改
            if("edit".equals(handletype))
            {
                fominfo_Old = baseBiz.getById(LfFomInfo.class, formid);
                // 表单名称
                fominfo_Old.setTitle(form_title);
                // 表单描述
                fominfo_Old.setNote(form_note);
                fominfo_Old.setModitytime(new Timestamp(System.currentTimeMillis()));
            }
            else if("apply".equals(handletype))
            {
                // 套用模板应用
                fominfo_Old = baseBiz.getById(LfFomInfo.class, formid);
                if(fominfo_Old == null)
                {
                    EmpExecutionContext.error("wzgl_formManagerSvt.saveForm.fominfo_Old is null");
                    response.getWriter().print("fail");
                    return;
                }
                fominfo_New = new LfFomInfo();
                fominfo_New.setCorpCode(corpCode);
                // 应用新增模板时的模板类型
                fominfo_New.setTypeId(fominfo_Old.getTypeId());
                // 新增时 所选择的系统表单的ID
                fominfo_New.setParentId(fominfo_Old.getFId());
                // 表单名称
                fominfo_New.setTitle(form_title);
                // 表单描述
                fominfo_New.setNote(form_note);
            }
            else if("add".equals(handletype) || "defined".equals(handletype))
            {
                // 这里获取的是表单类型
                String formtype = request.getParameter("formtype");
                if(formtype == null || "".equals(formtype))
                {
                    EmpExecutionContext.error("wzgl_formManagerSvt.saveForm.formtype is null");
                    response.getWriter().print("fail");
                    return;
                }
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("typeId", "0");
                conditionMap.put("publishState", "1");
                conditionMap.put("isSystem", "1");
                List<LfFomInfo> fomInfoList = baseBiz.getByCondition(LfFomInfo.class, conditionMap, null);
                Long fid = 7L;
                if(fomInfoList != null && fomInfoList.size() > 0)
                {
                    fominfo_Old = fomInfoList.get(0);
                    fid = fominfo_Old.getFId();
                }
                else
                {
                    EmpExecutionContext.error("wzgl_formManagerSvt.saveForm.sysFormInfo is null");
                    response.getWriter().print("fail");
                    return;
                }

                // 默认新增
                fominfo_New = new LfFomInfo();
                fominfo_New.setCorpCode(corpCode);
                // 表单名称
                fominfo_New.setTitle(form_title);
                // 表单描述
                fominfo_New.setNote(form_note);
                // 表单类型
                fominfo_New.setTypeId(Long.valueOf(formtype));
                // 新增时 系统自定义ID
                fominfo_New.setParentId(fid);
            }
            String returnmsg = new WzglFormBiz().saveForm(questionmsg, handletype, fominfo_New, fominfo_Old, pathUrl);
            EmpExecutionContext.error("wzgl_formManagerSvt." + handletype + " form,code:" + returnmsg);
            response.getWriter().print(returnmsg);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "wzgl_formManagerSvt.saveForm is error");
            response.getWriter().print("error");
        }
    }

    /**
     * @description 删除表单
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-25 上午10:43:46
     */
    public void delForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            // 表单ID
            String formid = request.getParameter("fid");
            // 企业编码
            String lgcorpcode = request.getParameter("lgcorpcode");
            if(formid == null || "".equals(formid) || lgcorpcode == null || "".equals(lgcorpcode))
            {
                EmpExecutionContext.error("wzgl_formManagerSvt.delForm.formid:" + formid + ",lgcorpcode:" + lgcorpcode);
                response.getWriter().print("paramisnull");
                return;
            }
            // 进行表单删除
            LfFomInfo fomInfo = baseBiz.getById(LfFomInfo.class, formid);
            if(fomInfo == null)
            {
                response.getWriter().print("objisnull");
                return;
            }
            // 进行表单删除
            // String returnmsg = new WzglFormBiz().delForm(fomInfo);
            fomInfo.setPublishState(3);
            String returnmsg = "fail";
            if(baseBiz.updateObj(fomInfo))
            {
                returnmsg = "success";
            }
            EmpExecutionContext.error("wzgl_formManagerSvt.delete form,formid:" + formid + ",code:" + returnmsg);
            response.getWriter().print(returnmsg);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "wzgl_formManagerSvt.delForm Is Error");
            response.getWriter().print("error");
        }
    }

    /**
     * @description 复制表单
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-25 下午02:17:43
     */
    public void copyForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            // 表单ID
            String formid = request.getParameter("fid");
            // 项目路径,例如/ott
            String pathUrl = request.getParameter("path");
            if(formid == null || "".equals(formid) || pathUrl == null || "".equals(pathUrl))
            {
                EmpExecutionContext.error("wzgl_formManagerSvt.copyForm.formid:" + formid + ",pathUrl:" + pathUrl);
                response.getWriter().print("paramisnull");
                return;
            }
            // 获取被复制的表单信息对象
            LfFomInfo fomInfo = baseBiz.getById(LfFomInfo.class, formid);
            if(fomInfo == null)
            {
                response.getWriter().print("objisnull");
                return;
            }
            // 进行表单删除
            String returnmsg = new WzglFormBiz().copyForm(fomInfo, pathUrl);
            EmpExecutionContext.error("wzgl_formManagerSvt.copy form,formid:" + formid + ",code:" + returnmsg);
            response.getWriter().print(returnmsg);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "wzgl_formManagerSvt.copyForm Is Error");
            response.getWriter().print("error");
        }
    }

    /**
     * @description 通过链接访问表单
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-28 上午10:49:02
     */
    public void toAccessForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 开发者传递值(aid10004tid1003)
        String state = request.getParameter("state");
        // 用户同意授权，获取code
        String code = request.getParameter("code");
        // 当前微信公众帐号
        LfWeiAccount account = null;
        // 当前微信用户
        List<LfWeiUserInfo> userList = null;
        LfWeiUserInfo userInfo = null;
        // 当前表单对象
        LfFomInfo otFomInfo = null;
        // 发布状态 1发布 2草稿 3 删除
        String publicstate = "";
        try
        {
            // 获取当前表单的id
            String formid = state.substring(state.indexOf("tid") + 3);
            // 当前访问的公众帐号id
            String aid = state.substring(state.indexOf("aid") + 3, state.indexOf("tid"));

            if(formid != null && !"".equals(formid) && aid != null && !"".equals(aid))
            {
                BaseBiz baseBiz = new BaseBiz();
                account = baseBiz.getById(LfWeiAccount.class, aid);
                // 查询当前表单
                otFomInfo = baseBiz.getById(LfFomInfo.class, formid);
                if(otFomInfo == null)
                {
                    EmpExecutionContext.error("wzgl_formManagerSvt.toAccessForm.LfFomInfo is null");
                    return;
                }
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                // 调用授权接口
                WeixParams weixParams = new WeixParams();
                weixParams.setAppid(account.getAppId());
                weixParams.setAppsecret(account.getSecret());
                weixParams.setCode(code);
                weixParams = new WeixService().getOauth2AccessToken(weixParams);
                JSONObject jsonObj = weixParams.getJsonObj();
                if(null != jsonObj && "000".equals(weixParams.getErrCode()))
                {
                    // 获取当前微信用户
                    conditionMap.put("openId", (String) jsonObj.get("openid"));
                    conditionMap.put("AId", String.valueOf(aid));
                    userList = baseBiz.getByCondition(LfWeiUserInfo.class, conditionMap, null);
                    if(userList != null && userList.size() > 0)
                    {
                        userInfo = userList.get(0);
                    }
                    publicstate = String.valueOf(otFomInfo.getPublishState());
                    conditionMap.clear();
                    conditionMap.put("fid", formid);
                    if(userInfo!=null){
                    	conditionMap.put("wcId", String.valueOf(userInfo.getWcId()));
                    }
                    conditionMap.put("aid", String.valueOf(account.getAId()));
                    List<LfFomFieldvalue> fieldvalueList = baseBiz.getByCondition(LfFomFieldvalue.class, conditionMap, null);
                    if(fieldvalueList != null && fieldvalueList.size() > 0)
                    {
                        // 该用户已经参与该表单问答
                        publicstate = "4";
                        LinkedHashMap<String,String> filedMap = new LinkedHashMap<String, String>();
                        LfFomFieldvalue filedvalue = null;
                        String filedStr = null;
                        for(int i=0;i<fieldvalueList.size();i++){
                            filedvalue = fieldvalueList.get(i);
                            filedStr = String.valueOf(filedvalue.getFieldId());
                            filedMap.put(filedStr, filedStr);
                        }
                        request.setAttribute("filedMap",filedMap);
                    }
                }else{
                    //页面刷新
                    publicstate = "4";
                }
            }
            else
            {
                EmpExecutionContext.error("wzgl_formManagerSvt.toAccessForm formid or aid is null");
                return;
            }

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "wzgl_formManagerSvt.toAccessForm is error");
        }
        finally
        {
            if(account != null && otFomInfo != null && userInfo !=  null)
            {
               request.getRequestDispatcher(otFomInfo.getUrl() + "?aid=" + String.valueOf(account.getAId()) + "&wcid=" + String.valueOf(userInfo.getWcId()) + "&publicstate=" + publicstate + "&t="+System.currentTimeMillis()).forward(request, response);
               //request.getRequestDispatcher("/file/wzgl/formhtml/2014/3/7/wzgl_form_2014030708525875010001.jsp" + "?aid=" + String.valueOf(account.getAId()) + "&wcid=10003" + "&publicstate=" + publicstate).forward(request, response);
            }
            else
            {
                request.getRequestDispatcher("/common/weixerror.jsp").forward(request, response);
            }
        }
    }

    /**
     * @description 接收提交的表单问题
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-28 下午04:00:34
     */
    public void accessFormQuestionnaire(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            // 表单
            String formid = request.getParameter("formid");
            // 企业编码
            String corpCode = request.getParameter("corpCode");
            // 选项信息
            String questionmsg = request.getParameter("questionmsg");
            // 公众帐号ID
            String aid = request.getParameter("aid");
            // 用户ID
            String wcid = request.getParameter("wcid");
            if(aid == null || "".equals(aid) || wcid == null || "".equals(wcid) || formid == null || "".equals(formid) || corpCode == null || "".equals(corpCode) || questionmsg == null || "".equals(questionmsg))
            {
                EmpExecutionContext.error("wzgl_formManagerSvt.accessFormQuestionnaire.formid:" + formid + ",aid:" + aid + ",wcid:" + wcid + ",corpCode:" + corpCode + ",questionmsg:" + questionmsg);
                return;
            }
            LfFomInfo fomInfo = baseBiz.getById(LfFomInfo.class, formid);

            if(fomInfo == null)
            {
                EmpExecutionContext.error("wzgl_formManagerSvt.accessFormQuestionnaire.LfFomInfo is null");
                return;
            }

            String returnmsg = new WzglFormBiz().formQuestionnaire(fomInfo, corpCode, Long.valueOf(aid), wcid, questionmsg);
            EmpExecutionContext.error("accessFormQuestionnaire form,formid:" + formid + ",code:" + returnmsg);
            response.getWriter().print(returnmsg);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "wzgl_formManagerSvt.accessFormQuestionnaire is error");
            response.getWriter().print("error");
        }
    }

    /**
     * 获取表单的信息，放回json格式数据
     * 
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-16 上午10:15:21
     */
    @SuppressWarnings("unchecked")
    public void getFormInfo(HttpServletRequest request, HttpServletResponse response)
    {
        // 微站ID
        String formid = request.getParameter("formid");
        response.setContentType("text/html");
        JSONObject formObj = new JSONObject();
        try
        {
            // 查找对应的微站信息
            LfFomInfo form = baseBiz.getById(LfFomInfo.class, formid);
            if(form != null)
            {
                formObj.put("formid", form.getFId());
                formObj.put("name", form.getTitle());
                formObj.put("url", form.getUrl());
            }
            // 返回名称
            response.getWriter().print(formObj.toString());
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取表单信息失败！");
        }
    }
    
    
    /**
     * 
     * @description    测试手机端跳入提交表单页面处理
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException       			 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2014-3-6 下午04:59:23
     */
    public void toAccessFormAtest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 当前表单对象
        LfFomInfo otFomInfo = null;
        try
        {
                // 查询当前表单
            otFomInfo = baseBiz.getById(LfFomInfo.class, "1019");
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "wzgl_formManagerSvt.toAccessForm is error");
        }
        finally
        {
            request.getRequestDispatcher(otFomInfo!=null?otFomInfo.getUrl():"" + "?aid=10003&wcid=10001&publicstate=1").forward(request, response);
        }
    }
    
    
    
    
    
    
    
    
    
    
}
