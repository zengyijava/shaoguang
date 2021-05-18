package com.montnets.emp.wzgl.form.biz;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.form.LfFomField;
import com.montnets.emp.entity.form.LfFomFieldvalue;
import com.montnets.emp.entity.form.LfFomInfo;
import com.montnets.emp.entity.form.LfFomQuestion;
import com.montnets.emp.ottbase.constant.OttException;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.TxtFileUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author yejiangmin <282905282@qq.com>
 * @description 表单biz
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-12-24 下午03:24:35
 */
public class WzglFormBiz extends SuperBiz {

    /**
     * @param fomInfo 表单的对象
     * @return 字符串ID或者空
     * @description 查询表单所应问题的选项string
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-27 上午11:19:12
     */
    private String toFormSearchQuestion(LfFomInfo fomInfo) {
        try {
            StringBuffer buffer = new StringBuffer();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            // 这里判断是否可以删除该表单权限
            conditionMap.put("fId", fomInfo.getFId().toString());
            conditionMap.put("corpCode", fomInfo.getCorpCode());
            // 查询该表单 所对应的问题
            List<LfFomQuestion> questionList = empDao.findListByCondition(LfFomQuestion.class, conditionMap, null);
            if (questionList != null && questionList.size() > 0) {
                LfFomQuestion formQuestion = null;
                for (int i = 0; i < questionList.size(); i++) {
                    formQuestion = questionList.get(i);
                    buffer.append(formQuestion.getQId());
                    if (i != questionList.size() - 1) {
                        buffer.append(",");
                    }
                }
                return buffer.toString();
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "WzglFormBiz.toEditFormSearchQuestion is error");
        }
        return "";
    }

    /**
     * @param formtitle     表单标题
     * @param formnote      表单描述
     * @param questionmsg   表单的问题
     * @param corpCode      企业编码
     * @param handletype    add 自定义新增 apply 套用模板新增 edit编辑表单
     * @param updatefominfo 套用表单对象 / 编辑的表单
     * @return success 成功 error 异常 paramisnull 参数不合法
     * @description 创建 / 编辑 表单
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-26 下午05:10:19
     */
    public String saveForm(String questionmsg, String handletype, LfFomInfo fominfo_New, LfFomInfo fominfo_Old, String pathUrl) {
        // 返回值
        String returnmsg = "fail";
        // 编辑状态使用该字符存放选项ids
        String optionBuffer = "";
        String corpCode = "";
        // 当是编辑的状态时 ,则需要查询该表单对象问题的所有选项 出来，用于删除
        if ("edit".equals(handletype)) {
            optionBuffer = toFormSearchQuestion(fominfo_Old);
        }

        if (questionmsg.indexOf("&ott&") < 0) {
            return "paramisnull";
        }

        Connection conn = empTransDao.getConnection();
        try {
            // 获取其html文件路径
            String formHtmlUrl = getFromHtmlUrl();
            if (formHtmlUrl == null || "".equals(formHtmlUrl)) {
                EmpExecutionContext.error("WzglFormBiz.saveForm.getFromHtmlUrl().formHtmlUrl:" + formHtmlUrl);
                return "fail";
            }
            empTransDao.beginTransaction(conn);
            Long formId = 0L;
            // 这里指的是 自定义新增 和套用模板新增
            if ("add".equals(handletype) || "apply".equals(handletype) || "defined".equals(handletype)) {
                corpCode = fominfo_New.getCorpCode();
                // 获取表单的唯一 url
                fominfo_New.setUrl(formHtmlUrl);
                // 如果是套用模板新增 ,增需要将模板+1 ,并且将模板的表单类型赋值
                // 将系统模板表单被使用+1
                fominfo_Old.setSubmitCount(fominfo_Old.getSubmitCount() + 1);
                empTransDao.update(conn, fominfo_Old);
                formId = empTransDao.saveObjProReturnID(conn, fominfo_New);
                fominfo_New.setFId(formId);
            } else if ("edit".equals(handletype)) {
                corpCode = fominfo_Old.getCorpCode();
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                formId = fominfo_Old.getFId();
                // 将老问题删除掉 删除这些问题所对应的选项
                if (optionBuffer.length() > 0) {
                    conditionMap.clear();
                    conditionMap.put("qid", optionBuffer);
                    empTransDao.delete(conn, LfFomField.class, conditionMap);
                } else {
                    // 选项为空
                    EmpExecutionContext.error("WzglFormBiz.saveForm.toEditFormSearchQuestion().optionBuffer:" + optionBuffer);
                }
                conditionMap.clear();
                conditionMap.put("fId", formId.toString());
                conditionMap.put("corpCode", corpCode);
                // 删除问题
                empTransDao.delete(conn, LfFomQuestion.class, conditionMap);
            }

            // 问题对象
            LfFomQuestion fomQuestion = null;
            // 选项值对象
            LfFomField fomField = null;
            // 问题ID
            Long questionId = 0L;
            // 选项ID
            Long fieldId = 0L;
            // 获取所有问题
            List<LfFomQuestion> fomQuestionList = new ArrayList<LfFomQuestion>();
            // 问题ID所对应的问题选项
            LinkedHashMap<Long, List<LfFomField>> questionFieldMap = new LinkedHashMap<Long, List<LfFomField>>();
            List<LfFomField> fomFieldList = null;
            // 单个问题信息
            String question = "";
            // 单个问题的input 输入值
            String[] inputValues = null;
            // 单个问题的选项数组
            String[] options = null;
            // 单个选项
            String option = "";
            String[] questions = questionmsg.split("&ott&");
            for (int i = 0; i < questions.length; i++) {
                // 取出一个问题信息
                question = questions[i];
                if (question == null || question.length() == 0) {
                    continue;
                }
                // 分割输入框中的信息,包含 问题类型 + 问题标题 + 选项值
                inputValues = question.split("#ott#");
                if (inputValues == null || inputValues.length == 0) {
                    continue;
                }
                // 类型 1单选 2多选
                String widget_type = inputValues[0];
                // 问题名称
                String title_num = inputValues[1];
                if (title_num != null && !"".equals(title_num)) {
                    // 转码
                    title_num = URLDecoder.decode(title_num, "UTF-8");
                } else {
                    title_num = " ";
                }
                // 实例化问题
                fomQuestion = new LfFomQuestion();
                // 企业编码
                fomQuestion.setCorpCode(corpCode);
                // 创建时间
                fomQuestion.setCreatetime(new Timestamp(System.currentTimeMillis()));
                // 标签类型
                fomQuestion.setFiledType(widget_type);
                // 表单ID
                fomQuestion.setFId(formId);
                // 问题序列
                fomQuestion.setSeqNum(i + 1);
                // 问题标题
                fomQuestion.setTitle(title_num);
                questionId = empTransDao.saveObjProReturnID(conn, fomQuestion);
                fomQuestion.setQId(questionId);
                // 存放该表单的所有问题
                fomQuestionList.add(fomQuestion);
                // 选项值 分割符为"," ,每个选项的内容
                String optionmsg = inputValues[2];
                // 判断是否有选项内容
                if (optionmsg == null || "".equals(optionmsg)) {
                    continue;
                }
                // 用,分割符做解析
                options = optionmsg.split(",");
                if (options != null && options.length > 0) {
                    // 对每个内容做循环
                    fomFieldList = new ArrayList<LfFomField>();
                    for (int j = 0; j < options.length; j++) {
                        option = options[j];
                        // 实例化选项值
                        fomField = new LfFomField();
                        if (option != null && !"".equals(option)) {
                            // 转码
                            option = URLDecoder.decode(option, "UTF-8");
                        } else {
                            option = " ";
                        }
                        // 选项值
                        fomField.setFieldValue(option);
                        // 标签类型
                        fomField.setFiledType(widget_type);
                        // 所属问题
                        fomField.setQid(questionId);
                        fieldId = empTransDao.saveObjectReturnID(conn, fomField);
                        fomField.setFieldId(fieldId);
                        fomFieldList.add(fomField);
                        // option = "";
                    }
                    if (fomFieldList != null && fomFieldList.size() > 0) {
                        // 一个问题所对应的选项
                        questionFieldMap.put(questionId, fomFieldList);
                    }
                }
            }


            // 表单对象区分 编辑 以及 新增      处理写html文件
            String formHtmlMsg = "";
            if ("edit".equals(handletype)) {
                formHtmlMsg = getFormHtmlMsg(fominfo_Old, fomQuestionList, questionFieldMap, pathUrl);
            } else {
                formHtmlMsg = getFormHtmlMsg(fominfo_New, fomQuestionList, questionFieldMap, pathUrl);
            }

            if (formHtmlMsg == null || "".equals(formHtmlMsg)) {
                empTransDao.rollBackTransaction(conn);
                EmpExecutionContext.error("WzglFormBiz.saveForm.getFormHtmlMsg().formHtmlMsg:" + formHtmlMsg);
                // 默认fail
                return returnmsg;
            }

            TxtFileUtil txtFileUtil = new TxtFileUtil();
            String fileallpath = txtFileUtil.getWebRoot() + formHtmlUrl;
            boolean isSuccess = writeToTxtFile(fileallpath, formHtmlMsg);
            if (!isSuccess) {
                // 写文件失败
                empTransDao.rollBackTransaction(conn);
                EmpExecutionContext.error("WzglFormBiz.saveForm.writeToTxtFile is fail,formId:" + formId);
                return returnmsg;
            }
            // 判断是否使用集群 上传文件到文件服务器 文件的相对路径
            if (WXStaticValue.ISCLUSTER == 1 && !"success".equals(new CommonBiz().uploadFileToFileCenter(fileallpath))) {
                // 是集群并且上传到文件服务器失败，则提示上传失败
                EmpExecutionContext.error("WzglFormBiz.saveForm.uploadToServer.uploadFile is fail");
                return returnmsg;
            }

            EmpExecutionContext.error("WzglFormBiz.saveForm.writeToTxtFile is success,formId:" + formId);

            if ("edit".equals(handletype)) {
                // 删除已存在的html文件
                deleteFormHtml(fominfo_Old, txtFileUtil.getWebRoot());
                // 将url换成新的html地址
                fominfo_Old.setUrl(formHtmlUrl);
                empTransDao.update(conn, fominfo_Old);
            }
            empTransDao.commitTransaction(conn);
            returnmsg = "success";
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            returnmsg = "error";
            EmpExecutionContext.error(e, "WzglFormBiz.saveForm is error");
        } finally {
            empTransDao.closeConnection(conn);
        }
        return returnmsg;
    }

    /**
     * @param fominfo_Old 表单对象
     * @param webRoot     项目路径
     * @description 删除 原表单html文件
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-27 下午03:29:38
     */
    private void deleteFormHtml(LfFomInfo fominfo_Old, String webRoot) {
        try {
            if (fominfo_Old.getUrl() != null && !"".equals(fominfo_Old.getUrl())) {
                File file = new File(webRoot + fominfo_Old.getUrl());
                if (file.exists() && file.delete()) {
                    EmpExecutionContext.error("WzglFormBiz.deleteFormHtml delete file success,filename:" + fominfo_Old.getUrl() + ",formid:" + fominfo_Old.getFId());
                } else {
                    EmpExecutionContext.error("WzglFormBiz.deleteFormHtml delete fail or no exists,filename:" + fominfo_Old.getUrl() + ",formid:" + fominfo_Old.getFId());
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "WzglFormBiz.deleteFormHtml delete file error,filename:" + fominfo_Old.getUrl() + ",formid:" + fominfo_Old.getFId());
        }

    }

    /**
     * @param formid     表单ID
     * @param lgcorpcode 企业编码
     * @return success 删除成功 fail 删除失败 error删除异常
     * @description 删除表单
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-25 上午11:21:32
     */
    public String delForm(LfFomInfo fomInfo) {
        // 返回值
        String returnmsg = "fail";
        try {
            // 查询该表单对象问题的所有选项 出来，用于删除
            String optionBuffer = toFormSearchQuestion(fomInfo);
            Connection conn = empTransDao.getConnection();
            try {
                empTransDao.beginTransaction(conn);
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                // 删除这些问题所对应的选项
                if (optionBuffer != null && optionBuffer.length() > 0) {
                    conditionMap.clear();
                    conditionMap.put("qid", optionBuffer);
                    empTransDao.delete(conn, LfFomField.class, conditionMap);
                }
                conditionMap.clear();
                conditionMap.put("fId", fomInfo.getFId().toString());
                conditionMap.put("corpCode", fomInfo.getCorpCode());
                // 删除问题
                empTransDao.delete(conn, LfFomQuestion.class, conditionMap);
                // 删除表单
                empTransDao.delete(conn, LfFomInfo.class, fomInfo.getFId().toString());
                // 删除jsp文件
                deleteFormHtml(fomInfo, new TxtFileUtil().getWebRoot());
                // 提交事务
                empTransDao.commitTransaction(conn);
                returnmsg = "success";
            } catch (Exception e) {
                returnmsg = "error";
                empTransDao.rollBackTransaction(conn);
                EmpExecutionContext.error(e, "WzglFormBiz.delForm is error");
            } finally {
                empTransDao.closeConnection(conn);
            }
        } catch (Exception e) {
            returnmsg = "error";
            EmpExecutionContext.error(e, "WzglFormBiz.delForm is error");
        }
        return returnmsg;
    }

    /**
     * @param fomInfo 老表单信息
     * @param pathUrl 项目路径 ,例如 /ott ,为了文件中的隐藏路径
     * @return success 删除成功 fail删除失败 error删除异常
     * @description 复制表单
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-25 下午03:38:01
     */
    public String copyForm(LfFomInfo oldFomInfo, String pathUrl) {
        // 返回值
        String returnmsg = "fail";
        try {
            // 获取其html文件路径
            String formHtmlUrl = getFromHtmlUrl();
            if (formHtmlUrl == null || "".equals(formHtmlUrl)) {
                EmpExecutionContext.error("WzglFormBiz.copyForm.getFromHtmlUrl().formHtmlUrl:" + formHtmlUrl);
                return returnmsg;
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            conditionMap.put("fId", oldFomInfo.getFId().toString());
            conditionMap.put("corpCode", oldFomInfo.getCorpCode());
            orderbyMap.put("qId", WXStaticValue.ASC);
            orderbyMap.put("seqNum", WXStaticValue.ASC);
            // 查询该表单 所对应的问题 按顺序来
            List<LfFomQuestion> questionList = empDao.findListByCondition(LfFomQuestion.class, conditionMap, orderbyMap);
            StringBuffer buffer = new StringBuffer();
            // 老问题对象
            LfFomQuestion fomQuestion = null;
            // 循环获取问题的ID
            if (questionList != null && questionList.size() > 0) {
                for (int i = 0; i < questionList.size(); i++) {
                    fomQuestion = questionList.get(i);
                    buffer.append(fomQuestion.getQId());
                    if (i != questionList.size() - 1) {
                        buffer.append(",");
                    }
                }
            }

            // 查询出该复制模板表单的所属系统表单
            LfFomInfo sysFomInfo = empDao.findObjectByID(LfFomInfo.class, oldFomInfo.getParentId());
            if (sysFomInfo == null) {
                EmpExecutionContext.error("WzglFormBiz.copyForm..sysFomInfo is null");
                return returnmsg;
            }

            // 老的问题所对应的选项
            List<LfFomField> fomFieldList = null;
            // 查询问题所对应的选项 按新增的顺序
            if (buffer.toString().length() > 0) {
                conditionMap.clear();
                orderbyMap.clear();
                conditionMap.put("qid&in", buffer.toString());
                orderbyMap.put("fieldId", WXStaticValue.ASC);
                fomFieldList = empDao.findListBySymbolsCondition(LfFomField.class, conditionMap, orderbyMap);
            }
            Connection conn = empTransDao.getConnection();
            try {
                empTransDao.beginTransaction(conn);
                // 实例化新表单对象
                LfFomInfo newFomInfo = new LfFomInfo();
                // 企业编码
                newFomInfo.setCorpCode(oldFomInfo.getCorpCode());
                // 表单描述
                newFomInfo.setNote(oldFomInfo.getNote());
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
                // 表单标题
                String oldTitle = oldFomInfo.getTitle();
                if (oldTitle != null && oldTitle.contains("_")) {
                    oldTitle = oldTitle.split("_")[0];
                }
                newFomInfo.setTitle(oldTitle + "_" + formatter.format(System.currentTimeMillis()));
                // 表单类型
                newFomInfo.setTypeId(oldFomInfo.getTypeId());
                // 复制 ,则父ID的表单ParentId
                newFomInfo.setParentId(oldFomInfo.getParentId());
                // .jsp文件名称
                newFomInfo.setUrl(formHtmlUrl);
                // 创建表单
                Long newFormId = empTransDao.saveObjProReturnID(conn, newFomInfo);
                newFomInfo.setFId(newFormId);

                // 获取所有问题
                List<LfFomQuestion> fomQuestionList = new ArrayList<LfFomQuestion>();
                // 生成key为问题ID,value为所对应的选项list
                LinkedHashMap<Long, List<LfFomField>> questionFieldMap = new LinkedHashMap<Long, List<LfFomField>>();
                if (questionList != null && questionList.size() > 0) {
                    // 老选项对象
                    LfFomField oldFomField = null;
                    // 新表单新问题对象
                    LfFomQuestion newFomQuestion = null;
                    // 新问题的新选项对象
                    LfFomField newfomField = null;
                    // 新问题ID
                    Long newQuestionId = 0L;
                    // 存放问题的list,未实例化
                    List<LfFomField> newfomFieldList = null;
                    for (int i = 0; i < questionList.size(); i++) {
                        fomQuestion = questionList.get(i);
                        // 实例化问题
                        newFomQuestion = new LfFomQuestion();
                        // 企业编码
                        newFomQuestion.setCorpCode(fomQuestion.getCorpCode());
                        // 创建时间
                        newFomQuestion.setCreatetime(new Timestamp(System.currentTimeMillis()));
                        // 标签类型
                        newFomQuestion.setFiledType(fomQuestion.getFiledType());
                        // 表单ID
                        newFomQuestion.setFId(newFormId);
                        // 问题序列
                        newFomQuestion.setSeqNum(fomQuestion.getSeqNum());
                        // 问题标题
                        newFomQuestion.setTitle(fomQuestion.getTitle());
                        // 新增问题
                        newQuestionId = empTransDao.saveObjProReturnID(conn, newFomQuestion);
                        // 将表单问题ID设值
                        newFomQuestion.setQId(newQuestionId);
                        // 存放该表单的所有问题 用于生成.jsp
                        fomQuestionList.add(newFomQuestion);
                        if (fomFieldList != null && fomFieldList.size() > 0) {
                            newfomFieldList = new ArrayList<LfFomField>();
                            Long fieldId = 0L;
                            for (int m = 0; m < fomFieldList.size(); m++) {
                                oldFomField = fomFieldList.get(m);
                                if (fomQuestion.getQId().toString().equals(oldFomField.getQid().toString())) {
                                    // 实例化选项值
                                    newfomField = new LfFomField();
                                    // 选项值
                                    newfomField.setFieldValue(oldFomField.getFieldValue());
                                    // 标签类型
                                    newfomField.setFiledType(oldFomField.getFiledType());
                                    // 所属问题
                                    newfomField.setQid(newQuestionId);
                                    fieldId = empTransDao.saveObjectReturnID(conn, newfomField);
                                    newfomField.setFieldId(fieldId);
                                    newfomFieldList.add(newfomField);

                                    fieldId = 0L;
                                }
                            }
                            if (newfomFieldList.size() > 0) {
                                questionFieldMap.put(newQuestionId, newfomFieldList);
                            }
                        }
                    }
                }
                // 表单对象区分 编辑 以及 新增
                String formHtmlMsg = getFormHtmlMsg(newFomInfo, fomQuestionList, questionFieldMap, pathUrl);
                if (formHtmlMsg == null || "".equals(formHtmlMsg)) {
                    empTransDao.rollBackTransaction(conn);
                    EmpExecutionContext.error("WzglFormBiz.copyForm.getFormHtmlMsg().formHtmlMsg:" + formHtmlMsg);
                    return returnmsg;
                }
                TxtFileUtil txtFileUtil = new TxtFileUtil();
                // 文件全路径
                String fileallpath = txtFileUtil.getWebRoot() + formHtmlUrl;
                // 生成jsp文件
                boolean isSuccess = writeToTxtFile(fileallpath, formHtmlMsg);
                if (!isSuccess) {
                    // 写文件失败
                    empTransDao.rollBackTransaction(conn);
                    EmpExecutionContext.error("WzglFormBiz.copyForm.writeToTxtFile is fail,formId:" + newFormId);
                    return returnmsg;
                }
                // 判断是否使用集群 上传文件到文件服务器 文件的相对路径
                if (WXStaticValue.ISCLUSTER == 1 && !"success".equals(new CommonBiz().uploadFileToFileCenter(fileallpath))) {
                    // 是集群并且上传到文件服务器失败，则提示上传失败
                    empTransDao.rollBackTransaction(conn);
                    EmpExecutionContext.error("WzglFormBiz.copyForm.uploadToServer.uploadFile is fail");
                    return returnmsg;
                }

                // 将系统表单使用次数加1
                sysFomInfo.setSubmitCount(sysFomInfo.getSubmitCount() + 1);
                empTransDao.update(conn, sysFomInfo);

                // 提交事务
                empTransDao.commitTransaction(conn);
                returnmsg = "success";
                EmpExecutionContext.error("WzglFormBiz.copyForm.writeToTxtFile is success,formId:" + newFormId);
            } catch (Exception e) {
                returnmsg = "error";
                empTransDao.rollBackTransaction(conn);
                EmpExecutionContext.error(e, "WzglFormBiz.copyForm is error");
            } finally {
                empTransDao.closeConnection(conn);
            }
        } catch (Exception e) {
            returnmsg = "error";
            EmpExecutionContext.error(e, "WzglFormBiz.copyForm is error");
        }
        return returnmsg;
    }

    /**
     * @return 返回html 文件路径或者空字符
     * @description 获取该表单的html路径
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-27 上午10:55:35
     */
    private String getFromHtmlUrl() {
        String returnmsg = "";
        try {
            TxtFileUtil txtFileUtil = new TxtFileUtil();
            String strNYR = txtFileUtil.getCurNYR();
            SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmssS");
            Calendar c = Calendar.getInstance();
            // 获取顺序号单例对象
            GetSxCount sx = GetSxCount.getInstance();
            // 拼接文件名
            String saveName = "wzgl_form_" + sd.format(c.getTime()) + sx.getCount() + ".jsp";
            String fileDirUrl = WXStaticValue.WZGL_FORMHTML_URL + strNYR;
            String filePath = fileDirUrl + saveName;
            String webRoot = txtFileUtil.getWebRoot();
            File file = new File(webRoot + fileDirUrl);
            if (!file.exists()) {
                file.mkdirs();
            }
            File filee = new File(webRoot + filePath);
            if (!filee.exists()) {
                boolean flag = filee.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
            } else {
                boolean delete = filee.delete();
                if (!delete) {
                    EmpExecutionContext.error("删除文件失败！");
                }
                boolean flag = filee.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
            }
            returnmsg = filePath;
        } catch (Exception e) {
            returnmsg = "";
            EmpExecutionContext.error(e, "WzglFormBiz.getFromHtmlUrl is error");
        }
        return returnmsg;
    }

    /**
     * @param fomInfo          表单信息
     * @param questionList     问题集合
     * @param questionFieldMap key 问题 ID value 选项集合
     * @return 字符串 或者空
     * @description 将表单信息写成字符串
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-27 下午03:00:36
     */
    private String getFormHtmlMsg(LfFomInfo fomInfo, List<LfFomQuestion> questionList, LinkedHashMap<Long, List<LfFomField>> questionFieldMap, String pathUrl) {
        StringBuffer buffer = new StringBuffer();
        try {
            String line = WXStaticValue.getSystemProperty().getProperty(WXStaticValue.LINE_SEPARATOR);
            buffer.append("<%@ page language='java' import='java.util.*' pageEncoding='UTF-8'%>").append(line);
            buffer.append("<%@ page import='java.util.LinkedHashMap' %>").append(line);


            buffer.append("<% String aid = request.getParameter(\"aid\"); %>").append(line);
            buffer.append("<% String wcid = request.getParameter(\"wcid\"); %>").append(line);
            buffer.append("<% String publicstate = request.getParameter(\"publicstate\"); %>").append(line);

            buffer.append("<% ").append(line);
            buffer.append(" @SuppressWarnings(\"unchecked\")").append(line);
            buffer.append(" LinkedHashMap<String,String> filedMap = (LinkedHashMap<String,String>)request.getAttribute(\"filedMap\"); %>").append(line);

            // html页面头部
            buffer.append("<!DOCTYPE HTML PUBLIC '-//W3C//DTD XHTML 4.01 Transitional//EN'> ").append(line);
            buffer.append("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' />").append(line);

            buffer.append("<meta name='viewport' content='width=device-width, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0'/>").append(line);
            buffer.append("<meta name='apple-mobile-web-app-status-bar-style' content='black'/>").append(line);
            buffer.append("<meta name='apple-mobile-web-app-capable' content='yes'/>").append(line);

            buffer.append("<link rel='stylesheet' type='text/css' href='" + pathUrl + "/common/css/frame.css' />").append(line);
            buffer.append("<link rel='stylesheet' type='text/css' href='" + pathUrl + "/frame/frame3/skin/default/frame.css' />").append(line);
            buffer.append("<link rel='stylesheet' type='text/css' href='" + pathUrl + "/frame/frame3/skin/default/addMo.css' />").append(line);
            buffer.append("<link rel='stylesheet' type='text/css' href='" + pathUrl + "/wzgl/form/css/saveForm.css' />").append(line);
            buffer.append("<script type='text/javascript' src='" + pathUrl + "/common/js/myjquery-a.js'></script>").append(line);

            buffer.append("<script>$(document).ready(function() {").append(line);
            buffer.append("document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {").append(line);
            buffer.append("WeixinJSBridge.call('hideToolbar'); }); }); </script></head>").append(line);

            // 隐藏域
            buffer.append("<body><div id='container' class='container' style='height: auto;'>").append(line);
            buffer.append("<div id='rContent' class='rContent' style='height: auto;'>").append(line);
            buffer.append("<form name='pageForm' action='' method='post' id='pageForm'>").append(line);
            buffer.append("<input type='hidden' id='pathUrl' value='" + pathUrl + "'/>").append(line);
            buffer.append("<input type='hidden' id='formid' value='" + fomInfo.getFId() + "'/>").append(line);
            buffer.append("<input type='hidden' id='lgcorpcode' value='" + fomInfo.getCorpCode() + "'/>").append(line);

            // 处理公众ID 以及 用户ID ,表单状态
            buffer.append("<input type='hidden' id='aid' value='<%=aid%>'/>").append(line);
            buffer.append("<input type='hidden' id='wcid' value='<%=wcid%>'/>").append(line);
            buffer.append("<input type='hidden' id='publicstate' value='<%=publicstate%>'/>").append(line);

            buffer.append("<div class='itemDiv' id='delwarndiv' style='margin-top:15px;display:none;'>").append(line);
            buffer.append("<table style='height:23px;width:device-width;'>").append(line);
            // buffer.append("<table class='div_bd' style='height:23px;width:340px;'>").append(line);
            buffer.append("<tr><td width='device-width;'  colspan='2' style='padding-left: 10px;'>").append(line);
            // buffer.append("<tr><td width='340px;'  colspan='2' style='padding-left: 10px;'>").append(line);
            buffer.append("<font color='red' id='warninfo'></font> </td></tr></table></div>").append(line);

            buffer.append("<div class='itemDiv' style='margin-top:10px;width:device-width;'>").append(line);
            // buffer.append("<div class='itemDiv' style='margin-top:10px;width:340px;'>").append(line);
            buffer.append("<table  style='height:23px;width:100%;;'>").append(line);
            buffer.append("<tr><td  colspan='2' align='center' style='font-size: 14px;'>").append(line);
            buffer.append("<label>" + fomInfo.getTitle() + "</label> </td></tr></table></div>").append(line);

            //处理按钮有效无效
            buffer.append("<% boolean isDisable = false;%>");
            //判断是否选中
            buffer.append("<% boolean isChoose = false; %>");
            buffer.append("<% if(publicstate != null && !\"\".equals(publicstate) && !\"1\".equals(publicstate)){isDisable = true;}%>");
            if (questionList != null && questionList.size() > 0) {
                LfFomQuestion question = null;
                int num = 0;
                int count = 0;
                List<LfFomField> fomFieldList = null;
                LfFomField fomField = null;
                for (int i = 0; i < questionList.size(); i++) {
                    question = questionList.get(i);
                    num = num + 1;
                    //buffer.append("<div class='itemDiv' sign='question' style='padding-top: 10px;height: auto;width:340px;' id='question_div_" + num + "' questionnum='" + num + "'>").append(line);
                    buffer.append("<div class='itemDiv' sign='question' style='padding-top: 10px;height: auto;width:device-width;' id='question_div_" + num + "' questionnum='" + num + "'>").append(line);
                    // 问题的类型
                    buffer.append("<input type='hidden' value='" + question.getFiledType() + "' id='widget_type_" + num + "'>").append(line);
                    // 问题ID
                    buffer.append("<input type='hidden' id='question_id_" + num + "' value='" + question.getQId() + "'/>").append(line);
                    // 问题所对应的选项的个数
                    buffer.append("<input type='hidden' id='question_option_count_" + num + "' value='" + questionList.size() + "'/>").append(line);
                    buffer.append("<table  style='height: 120px; width:100%;'>").append(line);
                    buffer.append("<tr tr_count='-1'><td style='height:35px;' colspan='2'>" + num + "").append(line);
                    buffer.append("<label id='title_" + num + "'>" + question.getTitle() + "</label></td></tr>").append(line);
                    if (questionFieldMap != null && questionFieldMap.get(question.getQId()) != null) {
                        fomFieldList = questionFieldMap.get(question.getQId());
                        if (fomFieldList != null && fomFieldList.size() > 0) {
                            for (int j = 0; j < fomFieldList.size(); j++) {
                                fomField = fomFieldList.get(j);
                                count = count + 1;
                                buffer.append("<tr id='tr_widget_option_" + count + "' tr_count='" + count + "'   style='margin-top: 10px;'> <td style='height:18px;padding-left: 10px;' align='left'>");

                                buffer.append("<% isChoose = false; %>");
                                //判断按钮是否选中
                                buffer.append(" <%if(filedMap != null && filedMap.get(\"" + fomField.getFieldId().toString() + "\") != null){isChoose = true;}%> ");

                                if ("1".equals(question.getFiledType())) {
                                    buffer.append("<input name='radio_option_name_" + num + "'  id='radio_option_" + count + "' type='radio' value='" + fomField.getFieldId() + "'  ");
                                    // buffer.append(" <%if(filedMap != null && filedMap.get(\"" + fomField.getFieldId().toString() + "\") != null){%> checked='checked' <% }%> />");
                                    //处理按钮 的选中  以及是否有效
                                    buffer.append(" <%if(isChoose){%> checked='checked' <% }%>  <%if(isDisable){%> disabled='disabled' <% }%> />");
                                    buffer.append("&nbsp;&nbsp;&nbsp;" + transFormLetter(j)).append("&nbsp;&nbsp;&nbsp;</td>").append(line);

                                    buffer.append("<td width='80%'><label name='r_input_option_" + count + "' id='r_input_option_" + count + "'  <%if(isChoose){%> style='color: blue;'<% }%> >" + fomField.getFieldValue() + "</label>").append(line);
                                } else if ("2".equals(question.getFiledType())) {
                                    buffer.append("<input name='checkbox_option_" + num + "' type='checkbox' class='select_check' value='" + fomField.getFieldId() + "' ");
                                    //buffer.append(" <%if(filedMap != null && filedMap.get(\"" + fomField.getFieldId().toString() + "\") != null){%> checked='checked' <% }%> />");
                                    buffer.append(" <%if(isChoose){%> checked='checked' <% }%>  <%if(isDisable){%> disabled='disabled' <% }%>/>");
                                    buffer.append("&nbsp;&nbsp;&nbsp;" + transFormLetter(j)).append("&nbsp;&nbsp;&nbsp;</td>").append(line);
                                    buffer.append("<td width='80%'><label name='c_input_option_" + count + "' id='c_input_option_" + count + "'  <%if(isChoose){%> style='color: blue;'<% }%> >" + fomField.getFieldValue() + "</label>").append(line);
                                }
                                buffer.append("</td></tr>").append(line);
                            }
                            fomFieldList.clear();
                        }
                    }
                    buffer.append("</table></div>");

                }
            }

            buffer.append("<div class='itemDiv' style='height: 30px;padding-top:25px;width:device-width;'>").append(line);
            //buffer.append("<div class='itemDiv' style='height: 30px;padding-top:25px;width:340px;'>").append(line);
            buffer.append("<table style='width: 100%;height: 35px;'><tr><td align='center'>").append(line);
            buffer.append("<input type='button' id='submitbtn' value='提交'  class='btnClass5 mr10' onclick='auestionnaire()'/></td></tr></table></div>").append(line);

            buffer.append("</form></div></div>");

            buffer.append("<script type='text/javascript' src='" + pathUrl + "/common/widget/artDialog/artDialog.js?skin=default'></script>").append(line);
            buffer.append("<script type='text/javascript' src='" + pathUrl + "/common/widget/artDialog/iframeTools.js'></script>").append(line);
            buffer.append("<script type='text/javascript' src='" + pathUrl + "/common/js/common.js'></script>").append(line);
            buffer.append("<script type='text/javascript' src='" + pathUrl + "/wzgl/form/js/questionForm.js'></script>").append(line);

            buffer.append("</body></html>").append(line);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "WzglFormBiz.getFormHtmlMsg is error");
            return "";
        }
        return buffer.toString();
    }

    /**
     * @param formid      表单ID
     * @param corpCode    企业编码
     * @param questionmsg 问题msg
     * @return success 成功 fail 失败 error 异常 paramisnull参数不合法
     * @description 提交表单
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-28 下午04:50:41
     */
    public String formQuestionnaire(LfFomInfo fomInfo, String corpCode, Long aid, String wcid, String questionmsg) {
        String returnmsg = "fail";
        try {
            // 分割问题
            String[] questions = questionmsg.split("&ott&");
            if (questions == null || questions.length == 0) {
                return "paramisnull";
            }
            // 单个问题
            String question = "";
            // 选项数组
            String[] optValues = null;
            // 保存选项的值 集合
            List<LfFomFieldvalue> fomFieldvalueList = new ArrayList<LfFomFieldvalue>();
            // 选项值数组
            String[] itemaArray = null;
            // 单个选项
            String item = "";
            // 循环
            for (int i = 0; i < questions.length; i++) {
                // 取出一个问题信息
                question = questions[i];
                if (question == null || question.length() == 0) {
                    EmpExecutionContext.error("WzglFormBiz.formQuestionnaire question:" + question);
                    continue;
                }
                // 分割输入框中的信息,包含 问题类型 + 问题标题 + 选项值
                optValues = question.split("#ott#");
                if (optValues == null || optValues.length == 0) {
                    EmpExecutionContext.error("WzglFormBiz.formQuestionnaire optValues is null");
                    continue;
                }
                // 问题类型
                String fieldType = optValues[0];
                // 问题ID
                String questionid = optValues[1];
                // 问题所对应的选项信息
                String items = optValues[2];
                if (items == null || "".equals(items) || questionid == null || "".equals(questionid)) {
                    EmpExecutionContext.error("WzglFormBiz.formQuestionnaire questionid:" + questionid + ",items:" + items);
                    continue;
                }
                // 获取单选选择
                if ("1".equals(fieldType)) {
                    // 将选项值放入fomFieldvalueList中
                    addOtFomFieldvalue(corpCode, fomInfo.getFId(), aid, wcid, items, fieldType, questionid, fomFieldvalueList);
                } else if ("2".equals(fieldType)) {
                    // 多选
                    itemaArray = items.split(",");
                    if (itemaArray != null && itemaArray.length > 0) {
                        for (int j = 0; j < itemaArray.length; j++) {
                            item = itemaArray[j];
                            if (item == null || "".equals(item)) {
                                continue;
                            }
                            // 将选项值放入fomFieldvalueList中
                            addOtFomFieldvalue(corpCode, fomInfo.getFId(), aid, wcid, item, fieldType, questionid, fomFieldvalueList);
                        }
                        itemaArray = null;
                    }
                } else {
                    EmpExecutionContext.error("WzglFormBiz.formQuestionnaire fieldType:" + fieldType);
                    continue;
                }
            }

            if (fomFieldvalueList.size() > 0) {
                Connection conn = empTransDao.getConnection();
                try {
                    empTransDao.beginTransaction(conn);
                    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                    LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
                    conditionMap.put("submitCount", String.valueOf(fomInfo.getSubmitCount()));
                    conditionMap.put("fId", String.valueOf(fomInfo.getFId()));
                    conditionMap.put("corpCode", fomInfo.getCorpCode());
                    // 将改表单的提交次数加1
                    objectMap.put("submitCount", String.valueOf(fomInfo.getSubmitCount() + 1));
                    empTransDao.update(conn, LfFomInfo.class, objectMap, conditionMap);
                    empTransDao.save(conn, fomFieldvalueList, LfFomFieldvalue.class);
                    empTransDao.commitTransaction(conn);
                    returnmsg = "success";
                } catch (Exception e) {
                    returnmsg = "error";
                    empTransDao.rollBackTransaction(conn);
                    EmpExecutionContext.error(e, "WzglFormBiz.formQuestionnaire1 is error");
                } finally {
                    empTransDao.closeConnection(conn);
                }
            } else {
                EmpExecutionContext.error("WzglFormBiz.formQuestionnaire List<LfFomFieldvalue>.size() == 0");
            }
        } catch (Exception e) {
            returnmsg = "error";
            EmpExecutionContext.error(e, "WzglFormBiz.formQuestionnaire2 is error");
        }
        return returnmsg;
    }

    /**
     * @param corpCode          企业编码
     * @param fid               表单ID
     * @param fieldId           选项ID
     * @param fieldType         问题类型 1单选 2多选
     * @param qid               问题ID
     * @param fomFieldvalueList 选择结果集合
     * @description 将问题的选项添加到集合里去
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-28 下午04:51:38
     */
    private void addOtFomFieldvalue(String corpCode, Long fid, Long aid, String wcid, String fieldId, String fieldType, String qid, List<LfFomFieldvalue> fomFieldvalueList) {
        try {
            LfFomFieldvalue fomFieldvalue = new LfFomFieldvalue();
            // 企业编码
            fomFieldvalue.setCorpCode(corpCode);
            fomFieldvalue.setCreatetime(new Timestamp(System.currentTimeMillis()));
            // 表单ID
            fomFieldvalue.setFid(fid);
            // 选项值
            fomFieldvalue.setFieldId(Long.valueOf(fieldId));
            // 选项类型
            fomFieldvalue.setFieldType(fieldType);
            // 问题ID
            fomFieldvalue.setQid(Long.valueOf(qid));
            // 用户ID
            fomFieldvalue.setWcId(wcid);
            // 公众帐号ID
            fomFieldvalue.setAid(aid);
            fomFieldvalueList.add(fomFieldvalue);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "WzglFormBiz.addOtFomFieldvalue is error,qid:" + qid + ",fieldId:" + fieldId);
        }
    }

    /**
     * @param fileName 文件的绝对路径
     * @param content  文件内容
     * @return
     * @throws OttException
     * @description 写文件, 用UTF-8形式
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-30 下午01:59:20
     */
    private boolean writeToTxtFile(String fileName, String content) throws OttException {
        FileOutputStream foss = null;
        try {
            File filee = new File(fileName);
            // 判断文件是否存在，不存在就新建一个
            if (!filee.exists() != false) {
                if (!filee.createNewFile()) {
                    throw new Exception();
                }
            }
            // 输出流
            foss = new FileOutputStream(fileName, true);
            // 将要输入的信息转换成流
            ByteArrayInputStream baInput = new ByteArrayInputStream(content.getBytes("UTF-8"));

            byte[] buffer = new byte[8192];
            int ch = 0;
            while ((ch = baInput.read(buffer)) != -1) {
                foss.write(buffer, 0, ch);
            }
            // 关闭流
            baInput.close();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "WzglFormBiz.writeToTxtFile is error");
        } finally {
            if (foss != null) {
                try {
                    foss.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "WzglFormBiz.writeToTxtFile.FileOutputStream close is error");
                }
            }
        }
        return true;

    }

    /**
     * @param m 数字
     * @return 字母
     * @description 数字转换成字母
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-31 上午11:07:49
     */
    private String transFormLetter(int m) {
        String letter = "";
        if (m == 0) {
            letter = "A";
        } else if (m == 1) {
            letter = "B";
        } else if (m == 2) {
            letter = "C";
        } else if (m == 3) {
            letter = "D";
        } else if (m == 4) {
            letter = "E";
        } else if (m == 5) {
            letter = "F";
        }
        return letter;
    }

}
