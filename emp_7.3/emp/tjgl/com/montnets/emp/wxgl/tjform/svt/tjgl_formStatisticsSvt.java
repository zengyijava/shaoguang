package com.montnets.emp.wxgl.tjform.svt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.form.LfFomField;
import com.montnets.emp.entity.form.LfFomFieldvalue;
import com.montnets.emp.entity.form.LfFomInfo;
import com.montnets.emp.entity.form.LfFomQuestion;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.util.PageInfo;

public class tjgl_formStatisticsSvt extends BaseServlet
{

    /**
     * @description
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2014-1-16 下午06:32:58
     */
    private static final long   serialVersionUID = 2318293767316304598L;

    private static final String FORM_PATH        = "/tjgl/form";

    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        try
        {
            boolean isFirstEnter = pageSet(pageInfo, request);
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            if(lgcorpcode == null || "".equals(lgcorpcode))
            {
                EmpExecutionContext.error("tjgl_formStatisticsSvt.find.lgcorpcode:" + lgcorpcode);
                return;
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            // 自定义表单
            conditionMap.put("isSystem", "1");
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
            }
            List<LfFomInfo> otFomInfoList = new BaseBiz().getByCondition(LfFomInfo.class, null, conditionMap, orderbyMap, pageInfo);
            request.setAttribute("otFomInfoList", otFomInfoList);
            request.setAttribute("conditionMap", conditionMap);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "tjgl_formStatisticsSvt.find is error");
        }
        finally
        {
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(FORM_PATH + "/tjgl_formStatisticsMgr.jsp").forward(request, response);
        }
    }

    /**
     * @description 跳转统计明细
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2014-1-17 上午11:29:31
     */
    public void toPageStatistics(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            //摸板ＩＤ
            String parentId = request.getParameter("typeId");
            if(lgcorpcode == null || "".equals(lgcorpcode) || parentId == null || "".equals(parentId))
            {
                EmpExecutionContext.error("tjgl_formStatisticsSvt.toPageStatistics.lgcorpcode:" + lgcorpcode + ",parentId:"+parentId);
                return;
            }
        
            // 表单ID
            String fid = request.getParameter("fid");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            BaseBiz baseBiz = new BaseBiz();
            List<LfFomInfo> otFomInfoList = null;
            LfFomInfo otFomInfo = null;
            if(parentId != null && !"".equals(parentId)){
                conditionMap.put("corpCode", lgcorpcode);
                // 自定义表单
                conditionMap.put("isSystem", "0");
                conditionMap.put("parentId", parentId);
                orderbyMap.put("submitCount", StaticValue.DESC);
                orderbyMap.put("fId", StaticValue.DESC);
                otFomInfoList = baseBiz.getByCondition(LfFomInfo.class, conditionMap, orderbyMap);
            }
            if(fid != null && !"".equals(fid))
            {
                otFomInfo = baseBiz.getById(LfFomInfo.class, fid);
            }
            else if(otFomInfoList != null && otFomInfoList.size() > 0)
            {
                otFomInfo = otFomInfoList.get(0);
            }

            // 获取表单信息对象
            if(otFomInfo == null)
            {
                EmpExecutionContext.error("wzgl_formManagerSvt.toSavePageForm.LfFomInfo is null");
                return;
            }
            conditionMap.clear();
            orderbyMap.clear();
            conditionMap.put("fId", otFomInfo.getFId().toString());
            conditionMap.put("corpCode", otFomInfo.getCorpCode());
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
                conditionMap.put("fid", otFomInfo.getFId().toString());
                conditionMap.put("corpCode", otFomInfo.getCorpCode());
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

            //所选择的表单
            request.setAttribute("otFomInfo", otFomInfo);
            //所对应的问题
            request.setAttribute("questionList", questionList);
            //问题所对应的选项
            request.setAttribute("fomFieldList", fomFieldList);
            //表单类型ID
            request.setAttribute("typeId", parentId);
            //该表单模板所对应的表单
            request.setAttribute("otFomInfoList", otFomInfoList);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "tjgl_formStatisticsSvt.toPageStatistics is error");
        }
        finally
        {
            request.getRequestDispatcher(FORM_PATH + "/tjgl_formStatistics.jsp").forward(request, response);
        }

    }

}
