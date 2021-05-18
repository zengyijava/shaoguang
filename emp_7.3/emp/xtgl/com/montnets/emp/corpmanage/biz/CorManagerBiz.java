package com.montnets.emp.corpmanage.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.JsonReturnUtil;
import com.montnets.emp.common.tools.MessageUtils;
import com.montnets.emp.common.tools.TemplateUtil;
import com.montnets.emp.corpmanage.dto.CorpMultimediaDto;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.corp.LfThirCorp;
import com.montnets.emp.entity.corp.LfWebconfig;
import com.montnets.emp.entity.system.LfThiMenuControl;
import com.montnets.emp.util.StringUtils;

public class CorManagerBiz extends SuperBiz  {
    public static final String RECEIVE_CORPCODE = "corpCode";//前端传入josn的企业编码的参数名
    public static final String RECEIVE_CONFIGNAME_MODULEPER = "modulePer";//前端传入josn的富信模块权限的参数名
    public static final String RECEIVE_MULTIMEDIA = "multimedia";//前端传入josn的富信模块操作类型的参数名
    public static final String MULTIMEDIA_MENUNUM = "25";//企业富信菜单标识

    
    public boolean setMultimedia(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {

        //查询企业编码信息
        String corpCode = "";
        if (1 == StaticValue.getCORPTYPE()) {//多企业版
            corpCode = (String) jsonObject.get(RECEIVE_CORPCODE);
            if (null == corpCode) {
                EmpExecutionContext.info("前端传入数据为null,corpCode:" + corpCode);
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            }
        } else if (0 == StaticValue.getCORPTYPE()) {//单企业版 单企业时并非一定要有，查询时不作为条件
            corpCode = (String) jsonObject.get(RECEIVE_CORPCODE);
            if (StringUtils.isEmpty(corpCode)) {
                corpCode = " ";
            }
        }

        //1赋予权限 0取消权限
        Integer multimedia = Integer.valueOf((String) jsonObject.get(RECEIVE_MULTIMEDIA));
        if (null == multimedia) {
            EmpExecutionContext.info("前端传入数据为null,multimedia:" + multimedia);
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            return false;
        }

        //设置菜单权限
        String menuNum = MULTIMEDIA_MENUNUM;//企业富信模块
        Connection connection = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(connection);
        } catch (Exception e1) {
            EmpExecutionContext.error(e1, "");
        }
        boolean setMenuResult = setMenuPermissions(connection, corpCode, multimedia, menuNum, request, response);//设置菜单-企业关系
        //boolean setMenuResult =true;
        //设置web配置
        String configName = RECEIVE_CONFIGNAME_MODULEPER;
        boolean setWebResult = setWebConfig(connection, corpCode, configName, (String) jsonObject.get(configName), request, response);//设置前端配置

        try {
            if (setMenuResult && setWebResult) {//若都成功,返回成功
                empTransDao.commitTransaction(connection);
                return true;
            } else {//否则回滚
                empTransDao.rollBackTransaction(connection);
                return false;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "设置富信模块权限中事物提交失败");
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            empTransDao.rollBackTransaction(connection);
        }
        return true;

    }

    
    public boolean setMenuPermissions(String corpCode, Integer multimedia, String menuNum, HttpServletRequest request, HttpServletResponse response) {
        Connection connection = empTransDao.getConnection();
        return setMenuPermissions(connection, corpCode, multimedia, menuNum, request, response);
    }

    
    public boolean setMenuPermissions(Connection connection, String corpCode, Integer multimedia, String menuNum, HttpServletRequest request, HttpServletResponse response) {
        //验证菜单标识是否存在
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("menuNum", menuNum);//企业富信
        List<LfThiMenuControl> lfThiMenuControls = null;
        try {
            lfThiMenuControls = empDao.findListByCondition(LfThiMenuControl.class, conditionMap, null);
            if (null == lfThiMenuControls || lfThiMenuControls.size() < 1) {
                EmpExecutionContext.info("未查询到菜单信息 menuNum:" + menuNum);
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SELECT_FAIL), request, response);
            }
        } catch (Exception e) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SELECT_FAIL), request, response);
            EmpExecutionContext.error(e, "设置富信模块权限时查询菜单表异常");
            empTransDao.rollBackTransaction(connection);
        }

        //验证企业是否存在
        LinkedHashMap<String, String> corpConditionMap = new LinkedHashMap<String, String>();
        corpConditionMap.put("corpCode", corpCode);
        List<LfCorp> lfCorps = null;
        try {
            lfCorps = empDao.findListByCondition(LfCorp.class, corpConditionMap, null);
            if (null == lfCorps || lfCorps.size() < 1) {
                EmpExecutionContext.info("未查询到当前企业信息");
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SELECT_FAIL), request, response);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "设置富信模块权限时查询企业表异常");
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SELECT_FAIL), request, response);
            empTransDao.rollBackTransaction(connection);
        }

        //关联字段
        LinkedHashMap<String, String> thirCorpConditionMap = new LinkedHashMap<String, String>();
        thirCorpConditionMap.put("menuNum", menuNum);
        thirCorpConditionMap.put("corpCode", corpCode);
        //查询菜单-企业关联信息
        List<LfThirCorp> lfThirCorps = new ArrayList<LfThirCorp>();
        try {
            lfThirCorps = empDao.findListByCondition(LfThirCorp.class, thirCorpConditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询到菜单-企业关联信息异常");
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            empTransDao.rollBackTransaction(connection);
        }

        //组关联表数据
        LfThirCorp lfThirCorp = new LfThirCorp();
        lfThirCorp.setMenuNum(Integer.valueOf(menuNum));//菜单标识
        lfThirCorp.setCorpCode(corpCode);//企业编码
        //添加权限
        if (1 == multimedia) {
            try {
                if (lfThirCorps.size() < 1) {
                    //若表中无此数据，保存数据
                    Long returnId = empTransDao.saveObjectReturnID(connection, lfThirCorp);
                    if (returnId > 0) {
                        return true;
                    } else {
                        EmpExecutionContext.info("保存菜单-企业关系表返回值异常 returnId:" + returnId);
                        return false;
                    }
                } else {
                    //若表中已有此数据，作更新操作，防止以后扩展字段
                    lfThirCorp.setId(lfThirCorps.get(0).getId());
                    boolean updateResult = empTransDao.update(connection, lfThirCorp);
                    if (updateResult) {
                        return true;
                    } else {
                        EmpExecutionContext.info("更新菜单-企业关系表返回值异常 updateResult:" + updateResult);
                        return false;
                    }
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "设置富信模块权限时保存菜单-企业关系表异常");
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
                empTransDao.rollBackTransaction(connection);
                return false;
            }
            //删除权限
        } else if (0 == multimedia) {
            try {
                if (lfThirCorps.size() > 0) {
                    int deleteResult = empTransDao.delete(connection, LfThirCorp.class, thirCorpConditionMap);
                    if (deleteResult > 0) {
                        return true;
                    } else {
                        EmpExecutionContext.info("删除菜单-企业关系表返回值异常 deleteResult:" + deleteResult);
                        return false;
                    }
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "设置富信模块权限时保存菜单-企业关系表异常");
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
                empTransDao.rollBackTransaction(connection);
                return false;
            }
        } else {
            EmpExecutionContext.info("参数异常,multimedia:" + multimedia);
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            return false;
        }
        return true;
    }


    
    public boolean setWebConfig(Connection connection, String corpCode, String configName, String jsonConfig, HttpServletRequest request, HttpServletResponse response) {
        //传入数据非空校验
        if (StringUtils.isEmpty(configName)) {
            EmpExecutionContext.info("传入数据为空,configName:" + configName);
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            return false;
        }
        if (StringUtils.isEmpty(jsonConfig)) {
            EmpExecutionContext.info("传入数据为空,jsonConfig:" + jsonConfig);
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            return false;
        }

        //设置web配置表数据
        LfWebconfig lfWebconfig = new LfWebconfig();
        lfWebconfig.setConfigName(configName);
        lfWebconfig.setJsonConfig(jsonConfig);
        lfWebconfig.setCorpCode(corpCode);

        //查询条件
        LinkedHashMap<String, String> webConfigConditionMap = new LinkedHashMap<String, String>();
        webConfigConditionMap.put("configName", configName);
        webConfigConditionMap.put("corpCode", corpCode);

        try {
            List<LfWebconfig> lfWebconfigs = empDao.findListByCondition(LfWebconfig.class, webConfigConditionMap, null);
            if (lfWebconfigs.size() > 0) {//若已有数据，则更新
                lfWebconfig.setId(lfWebconfigs.get(0).getId());
                empTransDao.update(connection, lfWebconfig);
            } else {//否则增加
                empTransDao.saveObjectReturnID(connection, lfWebconfig);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询前端web配置异常");
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            empTransDao.rollBackTransaction(connection);
            return false;
        }
        return true;
    }

    
    public List<LfWebconfig> getWebConfig(String configName, String corpCode, HttpServletRequest request, HttpServletResponse response) {

        LinkedHashMap<String, String> ConditionMap = new LinkedHashMap<String, String>();
        ConditionMap.put("configName", configName);
        ConditionMap.put("corpCode", corpCode);
        List<LfWebconfig> lfWebconfigs = null;
        try {
            lfWebconfigs = empDao.findListByCondition(LfWebconfig.class, ConditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询前端web配置异常");
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            return null;
        }
        return lfWebconfigs;
    }

    
    public Map<String, String> getMultimedia(String corpCode, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> resultMap = new HashMap<String, String>();
        String jsonConfig = null;
        String modulePer = RECEIVE_CONFIGNAME_MODULEPER;
        List<LfWebconfig> lfWebconfigs = getWebConfig(modulePer, corpCode, request, response);
        if (lfWebconfigs != null && lfWebconfigs.size() > 0) {
            LfWebconfig lfweb = lfWebconfigs.get(0);
            jsonConfig = lfweb.getJsonConfig();
        }
        resultMap.put("jsonConfig", jsonConfig);

        //关联字段
        LinkedHashMap<String, String> ConditionMap = new LinkedHashMap<String, String>();
        ConditionMap.put("menuNum", MULTIMEDIA_MENUNUM);
        ConditionMap.put("corpCode", corpCode);
        //查询菜单-企业关联信息
        List<LfThirCorp> lfThirCorps = new ArrayList<LfThirCorp>();
        try {
            lfThirCorps = empDao.findListByCondition(LfThirCorp.class, ConditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询前端web配置异常");
            return null;
        }
        if (lfThirCorps.size() > 0) {
            resultMap.put("multimedia", "1");
        } else {
            resultMap.put("multimedia", "0");
        }

        return resultMap;

    }

    
    public void getMultimedia_orig(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {

        CorpMultimediaDto corpMultimediaDto = new CorpMultimediaDto();
        //获取企业编码
        String corpCode = (String) jsonObject.get(RECEIVE_CORPCODE);
        String modulePer = RECEIVE_CONFIGNAME_MODULEPER;
        List<LfWebconfig> lfWebconfigs = getWebConfig(modulePer, corpCode, request, response);
        if (null == lfWebconfigs) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            return;
        }
        corpMultimediaDto.setModulePer(lfWebconfigs);

        //关联字段
        LinkedHashMap<String, String> ConditionMap = new LinkedHashMap<String, String>();
        ConditionMap.put("menuNum", MULTIMEDIA_MENUNUM);
        ConditionMap.put("corpCode", corpCode);
        //查询菜单-企业关联信息
        List<LfThirCorp> lfThirCorps = new ArrayList<LfThirCorp>();
        try {
            lfThirCorps = empDao.findListByCondition(LfThirCorp.class, ConditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询前端web配置异常");
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            return;
        }
        if (lfThirCorps.size() > 0) {
            corpMultimediaDto.setMultimedia(1);
        } else {
            corpMultimediaDto.setMultimedia(0);
        }
        JsonReturnUtil.success(corpMultimediaDto, request, response);
        return;
    }
}
