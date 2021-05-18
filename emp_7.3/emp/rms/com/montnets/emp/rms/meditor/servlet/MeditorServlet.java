package com.montnets.emp.rms.meditor.servlet;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTmplRela;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.meditor.base.BaseServlet;
import com.montnets.emp.rms.meditor.biz.MeditorBiz;
import com.montnets.emp.rms.meditor.biz.imp.MeditorBizImp;
import com.montnets.emp.rms.meditor.biz.imp.TemplateBiz;
import com.montnets.emp.rms.meditor.entity.TempData;
import com.montnets.emp.rms.meditor.tools.JsonReturnUtil;
import com.montnets.emp.rms.meditor.tools.MessageUtils;
import com.montnets.emp.rms.meditor.tools.String2JsonTool;
import com.montnets.emp.rms.meditor.tools.TemplateUtil;
import com.montnets.emp.rms.meditor.vo.TempHFiveVo;
import com.montnets.emp.rms.meditor.vo.TempsVo;
import com.montnets.emp.rms.rmsapi.util.DegreeCountUtil;
import com.montnets.emp.rms.vo.LfTemplateChartVo;
import com.montnets.emp.util.TxtFileUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

public class MeditorServlet extends BaseServlet {
    //公共biz类实例化
    final BaseBiz baseBiz=new BaseBiz();
    //模版管理biz类实例化
    final TemplateBiz tempBiz = new TemplateBiz();
    static Map<String, String> infoMap=new HashMap<String, String>();
    static {
        infoMap.put("1", "短信模板");
        infoMap.put("2", "彩信模板");
        infoMap.put("3", "网讯模板");
        infoMap.put("4", "富信模板");
    }

    /**
     * 查询素材列表
     *
     * @param request
     * @param response
     */
    public void getFodder(HttpServletRequest request, HttpServletResponse response) {
        try {
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.getFodder(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
        }
    }

    /**
     * 素材保存
     *
     * @param request
     * @param response
     */
    public void saveFodder(HttpServletRequest request, HttpServletResponse response) {
        try {
            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.saveFodder(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
        }
    }

    /**
     * 图片,视频,音频上传
     *
     * @param request
     * @param response
     */
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.uploadFile(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
        }
    }


    /**
     * 素材删除
     *
     * @param request
     * @param response
     */
    public void deleteFodder(HttpServletRequest request, HttpServletResponse response) {
        List<Integer> fodderIds = null;
        try {
            //接收参数
            JSONObject receiveJson = String2JsonTool.parseToJson(request);
            fodderIds = (List<Integer>) receiveJson.get("fodderIds");
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "删除素材中参数接收异常");
        }
        try {
            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.deleteFodder(fodderIds, request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
        }
    }

    /**
     * 查询模板列表
     *
     * @param request
     * @param response
     */
    public void getTemps(HttpServletRequest request, HttpServletResponse response) {
        TempsVo tempsVo = null;
        try {
            //接收参数
            JSONObject receiveJson = String2JsonTool.parseToJson(request);
            tempsVo = JSONObject.toJavaObject(receiveJson, TempsVo.class);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "获取模板列表中参数接收异常");
        }
        try {
            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.getTemps(tempsVo, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "系统异常");
            return;
        }
    }

    /**
     * 根据模板id(主键)删除模板
     *
     * @param request
     * @param response
     */
    public void deleteTemp(HttpServletRequest request, HttpServletResponse response) {
        TempsVo tempsVo = null;
        try {
            //获取参数
            JSONObject receiveJson = String2JsonTool.parseToJson(request);
            tempsVo = JSONObject.toJavaObject(receiveJson, TempsVo.class);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "删除模板中参数接收异常");
            return;
        }
        try {
            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.deleteTemp(tempsVo, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "系统异常");
            return;
        }
    }

    /**
     * 模板启禁用
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void changeTmState(HttpServletRequest request, HttpServletResponse response) {

        TempsVo tempsVoReceive = null;
        try {
            //获取参数
            JSONObject receviceJson = String2JsonTool.parseToJson(request);
            tempsVoReceive = JSONObject.toJavaObject(receviceJson, TempsVo.class);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "模板启禁用中参数接收异常");
            return;
        }
        try {
            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.changeTmState(tempsVoReceive, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "系统异常");
            return;
        }
    }

    /**
     * 查询模板详情
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getTempDetail(HttpServletRequest request, HttpServletResponse response) {
        TempsVo tempsVoReceive = null;
        try {
            //获取参数
            JSONObject receviceJson = String2JsonTool.parseToJson(request);
            tempsVoReceive = JSONObject.toJavaObject(receviceJson, TempsVo.class);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "获取模板详情中参数接收异常");
            return;
        }
        try {
            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.getTempDetail(tempsVoReceive, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "系统异常");
            return;
        }


    }

    /**
     * 添加模板
     *
     * @param request
     * @param response
     */
    public void addTemplate(HttpServletRequest request, HttpServletResponse response) {
        //获取参数
        JSONObject receviceJson = String2JsonTool.parseToJson(request);
        TempData tmpData = JSONObject.toJavaObject(receviceJson, TempData.class);

        //调用业务层方法
        MeditorBiz meditorBiz = new MeditorBizImp();
        try {
            meditorBiz.addTemplate(tmpData, request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "模板添加出现异常！");
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            return;
        }
    }


    /**
     * 行业用途--新增
     *
     * @param request
     * @param response
     */
    public void addIndustryUse(HttpServletRequest request, HttpServletResponse response) {
        //获取参数
        JSONObject industryUse = String2JsonTool.parseToJson(request);
        String name = industryUse.getString("name");
        String type = industryUse.getString("type");
        String tmpType = industryUse.getString("tmpType");
        //调用业务层方法
        MeditorBiz meditorBiz = new MeditorBizImp();
        try {
            meditorBiz.addIndustryUse(name, type, tmpType, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "增加行业用途异常");
            return;
        }
    }

    /**
     * 行业用途--修改
     *
     * @param request
     * @param response
     */
    public void updateIndustryUse(HttpServletRequest request, HttpServletResponse response) {
        //获取参数
        LfIndustryUse lfIndustryUse = null;
        try {
            JSONObject receviceJson = String2JsonTool.parseToJson(request);
            lfIndustryUse = JSONObject.toJavaObject(receviceJson, LfIndustryUse.class);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "修改行业用途参数接收异常");
            return;
        }
        //调用业务层方法
        MeditorBiz meditorBiz = new MeditorBizImp();
        try {
            meditorBiz.updateIndustryUse(lfIndustryUse, request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "修改行业用途出现异常！");
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            return;
        }

    }

    /**
     * 行业用途--查询
     *
     * @param request
     * @param response
     */
    public void getIndustryUses(HttpServletRequest request, HttpServletResponse response) {
        //获取参数
        JSONObject industryUse = String2JsonTool.parseToJson(request);
        String name = industryUse.getString("name");
        String type = industryUse.getString("type");
        String tmpType = industryUse.getString("tmpType");
        //调用业务层方法
        MeditorBiz meditorBiz = new MeditorBizImp();
        try {
            meditorBiz.getIndustryUses(name, type, tmpType, request, response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            EmpExecutionContext.error(e, "发现异常");
        }
    }

    /**
     * 行业用途--删除
     *
     * @param request
     * @param response
     */
    public void deleteIndustryUse(HttpServletRequest request, HttpServletResponse response) {
        //获取参数
        JSONObject industryUse = String2JsonTool.parseToJson(request);
        String id = industryUse.getString("id");
        //调用业务层方法
        MeditorBiz meditorBiz = new MeditorBizImp();
        try {
            meditorBiz.deleteIndustryUse(id, request, response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            EmpExecutionContext.error(e, "发现异常");
        }
    }

    /**
     * 图表生成
     *
     * @param request
     * @param response
     */
    public void createPicture(HttpServletRequest request, HttpServletResponse response) {
        //获取参数
        JSONObject pictureJson = String2JsonTool.parseToJson(request);
        LfTemplateChartVo templateChartVo = JSONObject.toJavaObject(pictureJson, LfTemplateChartVo.class);
        //调用业务层方法
        MeditorBiz meditorBiz = new MeditorBizImp();
        try {
            meditorBiz.createPicture(templateChartVo, request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
        }
    }

    /**
     * 快捷场景设置
     *
     * @param request
     * @param response
     */
    public void setShotCutTem(HttpServletRequest request, HttpServletResponse response) {
        TempsVo tempsVo = null;
        try {
            //获取数据
            JSONObject jsonObject = String2JsonTool.parseToJson(request);
            tempsVo = JSONObject.toJavaObject(jsonObject, TempsVo.class);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "获取模板详情中参数接收异常");
            return;
        }
        try {
            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.setShotCutTem(tempsVo, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "系统异常");
            return;
        }
    }


    /**
     * H5-新增
     *
     * @param request
     * @param response
     */
    public void addH5(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取数据
            JSONObject jsonObject = String2JsonTool.parseToJson(request);
            TempHFiveVo tempHFiveVo = JSONObject.toJavaObject(jsonObject, TempHFiveVo.class);

            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.addH5(tempHFiveVo, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, "系统异常", request, response);
            EmpExecutionContext.error(e, "H5新增异常");
        }
    }

    /**
     * H5查询
     *
     * @param request
     * @param response
     */
    public void getH5s(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取数据
            JSONObject jsonObject = String2JsonTool.parseToJson(request);
            TempHFiveVo tempHFiveVo = JSONObject.toJavaObject(jsonObject, TempHFiveVo.class);

            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.getH5s(tempHFiveVo, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, "系统异常", request, response);
            EmpExecutionContext.error(e, "H5查询异常");
        }
    }

    /**
     * H5删除
     *
     * @param request
     * @param response
     */
    public void deleteH5(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取数据
            JSONObject jsonObject = String2JsonTool.parseToJson(request);
            String hId = jsonObject.getString("hId");

            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.deleteH5(hId, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, "系统异常", request, response);
            EmpExecutionContext.error(e, "H5删除异常");
        }
    }

    /**
     * H5-详情查看
     *
     * @param request
     * @param response
     */
    public void getH5Detail(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取数据
            JSONObject jsonObject = String2JsonTool.parseToJson(request);
            String hId = jsonObject.getString("hId");

            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.getH5Detail(hId, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, "系统异常", request, response);
            EmpExecutionContext.error(e, "获取H5详细信息失败");
        }
    }


    /**
     * H5-修改
     *
     * @param request
     * @param response
     */
    public void updateH5(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取数据
            JSONObject jsonObject = String2JsonTool.parseToJson(request);
            TempHFiveVo tempHFiveVo = JSONObject.toJavaObject(jsonObject, TempHFiveVo.class);

            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.updateH5(tempHFiveVo, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, "系统异常", request, response);
            EmpExecutionContext.error(e, "修改失败");
        }
    }

    /**
     * 获取容量档位数据
     *
     * @param request
     * @param response
     */
    public void getDegreeData(HttpServletRequest request, HttpServletResponse response) {

        DegreeCountUtil countUtil = DegreeCountUtil.getInstance();
        Map<String, String> queryDegree = null;
        try {
            queryDegree = countUtil.queryDegree();
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            EmpExecutionContext.error(e, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION));
        }
        // 设置服务器名称
        JsonReturnUtil.success(queryDegree, request, response);
        return;
    }

    /**
     * 获取卡片样式列表
     *
     * @param request
     * @param response
     */
    public void getCardStyles(HttpServletRequest request, HttpServletResponse response) {
        //TODO
        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("style", "简约");
        jsonObject1.put("name", "别针");
        jsonObject1.put("url", "rms/meditor/style/1.png");
        jsonObjects.add(jsonObject1);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("style", "简约");
        jsonObject2.put("name", "胶片");
        jsonObject2.put("url", "rms/meditor/style/2.png");
        jsonObjects.add(jsonObject2);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("style", "简约");
        jsonObject3.put("name", "扣夹");
        jsonObject3.put("url", "rms/meditor/style/3.png");
        jsonObjects.add(jsonObject3);

        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("style", "简约");
        jsonObject4.put("name", "卡纸");
        jsonObject4.put("url", "rms/meditor/style/4.png");
        jsonObjects.add(jsonObject4);

        JSONObject jsonObject5 = new JSONObject();
        jsonObject5.put("style", "简约");
        jsonObject5.put("name", "记事本");
        jsonObject5.put("url", "rms/meditor/style/5.png");
        jsonObjects.add(jsonObject5);


        JSONObject jsonObject6 = new JSONObject();
        jsonObject6.put("style", "简约");
        jsonObject6.put("name", "锦旗");
        jsonObject6.put("url", "rms/meditor/style/6.png");
        jsonObjects.add(jsonObject6);


        JSONObject jsonObject7 = new JSONObject();
        jsonObject7.put("style", "简约");
        jsonObject7.put("name", "信封");
        jsonObject7.put("url", "rms/meditor/style/7.png");
        jsonObjects.add(jsonObject7);


        JSONObject jsonObject8 = new JSONObject();
        jsonObject8.put("style", "简约");
        jsonObject8.put("name", "图钉");
        jsonObject8.put("url", "rms/meditor/style/8.png");
        jsonObjects.add(jsonObject8);


        JSONObject jsonObject9 = new JSONObject();
        jsonObject9.put("style", "简约");
        jsonObject9.put("name", "卡夹");
        jsonObject9.put("url", "rms/meditor/style/9.png");
        jsonObjects.add(jsonObject9);

        JSONObject jsonObject10 = new JSONObject();
        jsonObject10.put("style", "简约");
        jsonObject10.put("name", "台历");
        jsonObject10.put("url", "rms/meditor/style/10.png");
        jsonObjects.add(jsonObject10);

        JSONObject jsonObject11 = new JSONObject();
        jsonObject11.put("style", "简约");
        jsonObject11.put("name", "色块");
        jsonObject11.put("url", "rms/meditor/style/11.png");
        jsonObjects.add(jsonObject11);

        JSONObject jsonObject12 = new JSONObject();
        jsonObject12.put("style", "简约");
        jsonObject12.put("name", "便签");
        jsonObject12.put("url", "rms/meditor/style/12.png");
        jsonObjects.add(jsonObject12);

        JSONObject jsonObject13 = new JSONObject();
        jsonObject13.put("style", "卡通");
        jsonObject13.put("name", "小马");
        jsonObject13.put("url", "rms/meditor/style/13.png");
        jsonObjects.add(jsonObject13);

        JSONObject jsonObject14 = new JSONObject();
        jsonObject14.put("style", "卡通");
        jsonObject14.put("name", "书籍");
        jsonObject14.put("url", "rms/meditor/style/14.png");
        jsonObjects.add(jsonObject14);

        JSONObject jsonObject15 = new JSONObject();
        jsonObject15.put("style", "卡通");
        jsonObject15.put("name", "圣诞树");
        jsonObject15.put("url", "rms/meditor/style/15.png");
        jsonObjects.add(jsonObject15);

        JSONObject jsonObject16 = new JSONObject();
        jsonObject16.put("style", "卡通");
        jsonObject16.put("name", "气球");
        jsonObject16.put("url", "rms/meditor/style/16.png");
        jsonObjects.add(jsonObject16);

        JSONObject jsonObject17 = new JSONObject();
        jsonObject17.put("style", "卡通");
        jsonObject17.put("name", "彩珠");
        jsonObject17.put("url", "rms/meditor/style/17.png");
        jsonObjects.add(jsonObject17);

        JSONObject jsonObject18 = new JSONObject();
        jsonObject18.put("style", "卡通");
        jsonObject18.put("name", "星星");
        jsonObject18.put("url", "rms/meditor/style/18.png");
        jsonObjects.add(jsonObject18);

        JSONObject jsonObject19 = new JSONObject();
        jsonObject19.put("style", "中国风");
        jsonObject19.put("name", "水墨");
        jsonObject19.put("url", "rms/meditor/style/19.png");
        jsonObjects.add(jsonObject19);

        JSONObject jsonObject20 = new JSONObject();
        jsonObject20.put("style", "中国风");
        jsonObject20.put("name", "梅花");
        jsonObject20.put("url", "rms/meditor/style/20.png");
        jsonObjects.add(jsonObject20);

        JSONObject jsonObject21 = new JSONObject();
        jsonObject21.put("style", "中国风");
        jsonObject21.put("name", "荷花");
        jsonObject21.put("url", "rms/meditor/style/21.png");
        jsonObjects.add(jsonObject21);

        JSONObject jsonObject22 = new JSONObject();
        jsonObject22.put("style", "中国风");
        jsonObject22.put("name", "画框");
        jsonObject22.put("url", "rms/meditor/style//22.png");
        jsonObjects.add(jsonObject22);

        JSONObject jsonObject23 = new JSONObject();
        jsonObject23.put("style", "商务");
        jsonObject23.put("name", "玻璃");
        jsonObject23.put("url", "rms/meditor/style/23.png");
        jsonObjects.add(jsonObject23);

        JSONObject jsonObject24 = new JSONObject();
        jsonObject24.put("style", "商务");
        jsonObject24.put("name", "档案袋");
        jsonObject24.put("url", "rms/meditor/style/24.png");
        jsonObjects.add(jsonObject24);

        JSONObject jsonObject25 = new JSONObject();
        jsonObject25.put("style", "商务");
        jsonObject25.put("name", "渐变");
        jsonObject25.put("url", "rms/meditor/style/25.png");
        jsonObjects.add(jsonObject25);

        JSONObject jsonObject26 = new JSONObject();
        jsonObject26.put("style", "商务");
        jsonObject26.put("name", "炫彩");
        jsonObject26.put("url", "rms/meditor/style/26.png");
        jsonObjects.add(jsonObject26);

        JSONObject jsonObject27 = new JSONObject();
        jsonObject27.put("style", "商务");
        jsonObject27.put("name", "科技");
        jsonObject27.put("url", "rms/meditor/style/27.png");
        jsonObjects.add(jsonObject27);

        JSONObject jsonObject28 = new JSONObject();
        jsonObject28.put("style", "商务");
        jsonObject28.put("name", "黑板");
        jsonObject28.put("url", "rms/meditor/style/28.png");
        jsonObjects.add(jsonObject28);

        JsonReturnUtil.success(jsonObjects, request, response);

    }

    /**
     * 导出文件
     */
    public void exportFile(HttpServletRequest request, HttpServletResponse response) {
    	InputStream inStream = null;
        try {
            String fileUrl = request.getParameter("fileUrl");
            String dirUrl = new TxtFileUtil().getWebRoot();
            File file = new File(dirUrl + fileUrl);
           
            if (file.exists()) {
                String filename = URLEncoder.encode(file.getName(), "utf-8");
                response.reset();
                response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                int fileLength = (int) file.length();
                response.setContentLength(fileLength);
                if (fileLength != 0) {
                    inStream = new FileInputStream(file);
                    byte[] buf = new byte[4096];
                    ServletOutputStream servletOS = response.getOutputStream();
                    int readLength;
                    while (((readLength = inStream.read(buf)) != -1)) {
                        servletOS.write(buf, 0, readLength);
                    }
                    inStream.close();
                    servletOS.flush();
                    servletOS.close();
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "导出文件出现异常！");
        } finally{
        	if(inStream != null){
        		try {
					inStream.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "文件关闭异常！");
				}
        	}
        }
    }

    /**
     * 根据主键id获取模板信息
     *
     * @param request
     * @param response
     */
    public void getTempById(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取数据
            Long tmId = Long.valueOf(request.getParameter("tmId"));

            //调用业务方法
            MeditorBiz meditorBiz = new MeditorBizImp();
            meditorBiz.getTempById(tmId, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, "系统异常", request, response);
            EmpExecutionContext.error(e, "修改失败");
        }
    }
    /**
     *   设置模板共享对象
     * @param request
     * @param response
     * @throws Exception
     */
    public void updateShareTemp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LfSysuser lfsysuser = null;
        String tempId=null;
        String infoType="1";
        try{
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
            conditionMap.put("templId",tempId);
            conditionMap.put("templType",infoType);
            conditionMap.put("shareType","1");
            List<LfTmplRela> bindobjList = baseBiz.getByCondition(LfTmplRela.class, conditionMap, null);
            //保存操作员的USERID
            HashSet<Long> userSet = new HashSet<Long>();
            //保存机构的USERID
            HashSet<Long> depSet = new HashSet<Long>();
            if(bindobjList != null && bindobjList.size()>0){
                for(int i=0;i<bindobjList.size();i++){
                    LfTmplRela temp = bindobjList.get(i);
                    //，1-机构，2--操作员
                    if(temp.getToUserType() == 2 && !userSet.contains(temp.getToUser())){
                        userSet.add(temp.getToUser());
                    }else if(temp.getToUserType() == 1 && !depSet.contains(temp.getToUser())){
                        depSet.add(temp.getToUser());
                    }
                }
            }

            //设置共享
            String returnmsg = tempBiz.updateShareTemp(depidstr, useridstr, tempId, infoType,lfsysuser);
            response.getWriter().print(returnmsg);

            //增加操作日志
            Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
            if(loginSysuserObj!=null){
                LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
                String opContent1 = "设置共享短信模板状态"+("success".equals(returnmsg)?"成功":"失败")+"。[模板Id，机构对象，操作员对象]" +
                        "("+tempId+"，"+depSet.toString()+"，"+userSet.toString()+")->" +
                        "("+tempId+"，["+(depidstr.indexOf(",")>-1?depidstr.substring(0,depidstr.length()-1):depidstr)+"]，["+(useridstr.indexOf(",")>-1?useridstr.substring(0,useridstr.length()-1):useridstr)+"])";
                EmpExecutionContext.info("短信模板管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                        loginSysuser.getUserName(), opContent1, "UPDATE");
            }

        }catch (Exception e) {
            EmpExecutionContext.error(e,"更新模板共享对象出错，"+infoMap.get(infoType)+"ID:"+tempId);
            response.getWriter().print("");
        }
    }
    /**
     * 列表页面获取已设置模板共享对象
     * @param request
     * @param response
     * @throws Exception
     */
    public void getSel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try{
            String tempId = request.getParameter("tempid");
            String infoType = request.getParameter("infoType");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("templId",tempId);
            conditionMap.put("templType",infoType);
            conditionMap.put("shareType","1");
            List<LfTmplRela> bindobjList = baseBiz.getByCondition(LfTmplRela.class, conditionMap, null);
            StringBuffer sb = new StringBuffer();
            //保存操作员的USERID
            HashSet<Long> userSet = new HashSet<Long>();
            //保存机构的USERID
            HashSet<Long> depSet = new HashSet<Long>();
            if(bindobjList != null && bindobjList.size()>0){
                for(int i=0;i<bindobjList.size();i++){
                    LfTmplRela temp = bindobjList.get(i);
                    //，1-机构，2--操作员
                    if(temp.getToUserType() == 2 && !userSet.contains(temp.getToUser())){
                        userSet.add(temp.getToUser());
                        LfSysuser user = baseBiz.getById(LfSysuser.class, temp.getToUser());
                        sb.append("<option value=\'"+temp.getToUser()+"\' isdeporuser='2'>[操作员]"+user.getName());
                        sb.append("</option>");
                    }else if(temp.getToUserType() == 1 && !depSet.contains(temp.getToUser())){
                        depSet.add(temp.getToUser());
                        LfDep dep = baseBiz.getById(LfDep.class, temp.getToUser());
                        sb.append("<option value=\'"+temp.getToUser()+"\' isdeporuser='1'>[机构]"+dep.getDepName()+"</option>");
                    }
                }
            }
            response.getWriter().print(sb.toString());
        }catch (Exception e) {
            EmpExecutionContext.error(e,"列表页面获取设置模板共享对象出现异常！");
            response.getWriter().print("");
        }
    }


    public void compoundImageText(HttpServletRequest request, HttpServletResponse response){
        //获取参数
        JSONObject receiveJson = String2JsonTool.parseToJson(request);
        //调用业务层方法
        MeditorBiz meditorBiz = new MeditorBizImp();
        meditorBiz.compoundImageText(receiveJson,request,response);
    }

}
