package com.montnets.emp.menuParam.biz;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.entity.sysuser.LfReviewSwitch;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailmanage.GwMsgtail;
import com.montnets.emp.entity.tailmanage.GwTailctrl;
import com.montnets.emp.entity.tailmanage.GwTdcmd;
import com.montnets.emp.menuParam.dao.MenuParamConfigDAO;
import com.montnets.emp.util.PageInfo;

public class MenuParamConfgBiz extends SuperBiz{

    /**
     * @param conditionMap 传入查询条件
     * @param orderbyMap   传入排序条件
     * @param pageInfo     分页信息
     * @return 企业账户绑定关系表的集合List<LfSpDepBind>
     * @throws Exception
     */
    
    public List<LfSpDepBind> getSpDepBindWhichNoBind(LinkedHashMap<String, String> conditionMap,
                                                     LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
            throws Exception {

        List<LfSpDepBind> xx = null;
        try {
            xx = new MenuParamConfigDAO().getSpDepBindWhichUserdataNoBind(conditionMap, orderbyMap,
                    pageInfo);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询部门绑定相关信息异常。");
        }
        return xx;
    }

    /**
     * 修改模块参数配置
     *
     * @param corp   机构信息实体类
     * @param params 审批全局配置（配置的字符串形式）
     * @return
     */
    
    @Deprecated
    public boolean change(LfCorp corp, String params) {
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            String[] codeSplit = params.split("%");
            if (codeSplit.length != 2) {
                return false;
            }

            String[] paramArr = codeSplit[0].split("&");
            for (int i = 0; i < paramArr.length; i++) {
                String param = paramArr[i];
                if (param != null && param.indexOf("@") > 0) {
                    String[] parArr = param.split("@");
                    LfReviewSwitch swrich = new LfReviewSwitch();
                    swrich.setCorpCode(corp.getCorpCode());
                    swrich.setId(Long.valueOf(parArr[0]));
                    //审批范围
                    swrich.setInfoType(Integer.valueOf(parArr[1]));
                    //是否启用
                    swrich.setSwitchType(Integer.valueOf(parArr[2]));
                    swrich.setMsgCount(Integer.valueOf(parArr[3]));
                    //修改审批全局配置
                    empTransDao.update(conn, swrich);
                }
            }

            //查询数据库，是否存在默认值

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corp.getCorpCode());
            BaseBiz baseBiz = new BaseBiz();
            CommonBiz commonBiz = new CommonBiz();


            try {
                List<LfCorpConf> lfCorpConfList = baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);
                conditionMap.clear();

                String[] setPass = codeSplit[1].split("@");
                conditionMap.put("pwd.count", setPass[0].trim());
                conditionMap.put("pwd.combtype", setPass[1].trim() + "," + setPass[2].trim() + "," + setPass[3].trim());
                conditionMap.put("pwd.upcycle", setPass[4].trim());
                conditionMap.put("pwd.pastalarm", setPass[5].trim());
                conditionMap.put("pwd.errlimit", setPass[6].trim());
                //conditionMap.put("pwd.logtime", setPass[7].trim());
                if (!"undefined".equals(setPass[7].trim()) && !"".equals(setPass[7].trim())) {
                    LfGlobalVariable globaV = null;
                    globaV = commonBiz.getGlobalVariable("LOGRETENTIONTIME");
                    globaV.setGlobalValue(Long.valueOf(setPass[7].trim()));
                    empTransDao.update(conn, globaV);
                }
                //更新模板是否可编辑参数,仅单企业更新
                if (StaticValue.getCORPTYPE() == 0) {
                    if (!"".equals(setPass[9].trim()) && setPass[9].trim() != null && !"undefined".equals(setPass[9].trim())) {
                        LfGlobalVariable glVariable = commonBiz.getGlobalVariable("TMPEDITORFLAG");
                        glVariable.setGlobalValue(Long.valueOf(setPass[9].trim()));
                        empTransDao.update(conn, glVariable);
                        //更新静态变量
                        StaticValue.setTMPEDITORFLAG(Integer.valueOf(setPass[9].trim()));
                    }
                }
                String dynPwd = URLDecoder.decode(setPass[8].trim(), "utf-8").trim();
                dynPwd = dynPwd.replace("{#参数#}", "#P_1#");
                conditionMap.put("pwd.dynpwd", dynPwd);

                for (int i = 0; i < lfCorpConfList.size(); i++) {
                    LfCorpConf conf = lfCorpConfList.get(i);

                    //位数
                    if ("pwd.count".equals(conf.getParamKey())) {
                        //需要对密码进行转换，防止出现0000000的情况
                        if (!"".equals(setPass[0].trim()) && !"null".equals(setPass[0].trim())) {
                            conf.setParamValue(Integer.parseInt(setPass[0].trim()) + "");
                        } else {
                            conf.setParamValue(setPass[0].trim());
                        }
                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.count");
                    }
                    //组合形式
                    if ("pwd.combtype".equals(conf.getParamKey())) {
                        conf.setParamValue(setPass[1].trim() + "," + setPass[2].trim() + "," + setPass[3].trim());
                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.combtype");
                    }
                    //修改周期
                    if ("pwd.upcycle".equals(conf.getParamKey())) {
                        if (!"".equals(setPass[4].trim()) && !"null".equals(setPass[4].trim())) {
                            conf.setParamValue(Integer.parseInt(setPass[4].trim()) + "");
                        } else {
                            conf.setParamValue(setPass[4].trim());
                        }
                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.upcycle");

                    }
                    //过期提醒
                    if ("pwd.pastalarm".equals(conf.getParamKey())) {
                        if (!"".equals(setPass[5].trim()) && !"null".equals(setPass[5].trim())) {
                            conf.setParamValue(Integer.parseInt(setPass[5].trim()) + "");
                        } else {
                            conf.setParamValue(setPass[5].trim());
                        }

                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.pastalarm");

                    }
                    //错误上限
                    if ("pwd.errlimit".equals(conf.getParamKey())) {
                        String limt = setPass[6].trim();
                        if (!"".equals(limt) && !"null".equals(limt)) {
                            conf.setParamValue(Integer.parseInt(limt) + "");
                        } else {
                            conf.setParamValue(limt);
                        }
                        //当错误上线为空时候，用户输入的错误次数清除掉
                        if ("".equals(limt) || "0".equals(limt)) {
                            String corpCode = "100001";
                            if (conf.getCorpCode() != null && !"".equals(conf.getCorpCode())) {
                                corpCode = conf.getCorpCode();
                            }
                            LinkedHashMap<String, String> contionMap = new LinkedHashMap<String, String>();
                            LinkedHashMap<String, String> updateMap = new LinkedHashMap<String, String>();
                            contionMap.put("corpCode", corpCode);
                            updateMap.put("pwderrortimes", "0");
                            empTransDao.update(conn, LfSysuser.class, updateMap, contionMap);
                        }
                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.errlimit");

                    }
					
					/*if("pwd.logtime".equals(conf.getParamKey())){
						//需要对密码进行转换，防止出现0000000的情况
						if(!"".equals(setPass[7].trim())&&!"null".equals(setPass[7].trim())){
							conf.setParamValue(Integer.parseInt(setPass[7].trim())+"");
						}else{
							conf.setParamValue(setPass[7].trim());
						}
						empTransDao.update(conn, conf);
						conditionMap.remove("pwd.logtime");
					}*/

                    if ("pwd.dynpwd".equals(conf.getParamKey())) {
                        //需要对密码进行转换，防止出现0000000的情况
                        if (!"".equals(dynPwd) && !"null".equals(dynPwd)) {
                            conf.setParamValue(dynPwd + "");
                        } else {
                            conf.setParamValue(dynPwd);
                        }
                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.dynpwd");
                    }
                }

                Set<String> set = conditionMap.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    LfCorpConf lf = new LfCorpConf();
                    lf.setCorpCode(corp.getCorpCode());
                    lf.setParamKey(key);
                    lf.setParamValue(conditionMap.get(key));
                    empTransDao.save(conn, lf);
                }


            } catch (Exception e) {
                EmpExecutionContext.error(e, "根据企业编码和key获得对应企业配置的机构限制信息异常。");
            }

            //修改全局参数
            empTransDao.update(conn, corp);
            empTransDao.commitTransaction(conn);
            return true;
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "修改模块参数配置失败！");
            return false;
        } finally {
            //关闭链接
            empTransDao.closeConnection(conn);
        }


    }

    /**
     * 修改模块参数配置
     *
     * @param corp        机构信息实体类
     * @param params      审批全局配置（配置的字符串形式）
     * @param msgtail     贴尾内容信息
     * @param tailctrl    全局贴尾控制
     * @param actsyncflag 签名同步开关
     * @return 是否成功
     */
    
    public boolean change(LfCorp corp, String params, GwMsgtail msgtail, GwTailctrl tailctrl, LinkedHashMap<String, String> dataCondition) {

        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            String[] codeSplit = escapeReplace(params).split("%");
            if (codeSplit.length != 2) {
                return false;
            }

            String[] paramArr = codeSplit[0].split("&");
            for (int i = 0; i < paramArr.length; i++) {
                String param = paramArr[i];
                if (param != null && param.indexOf("@") > 0) {
                    String[] parArr = param.split("@");
                    LfReviewSwitch swrich = new LfReviewSwitch();
                    swrich.setCorpCode(corp.getCorpCode());
                    swrich.setId(Long.valueOf(parArr[0]));
                    //审批范围
                    swrich.setInfoType(Integer.valueOf(parArr[1]));
                    if("请输入数字".equals(parArr[2]) || "请输入数字".equals(parArr[3])){
                        continue;
                    }
                    //是否启用
                    swrich.setSwitchType(Integer.valueOf(parArr[2]));
                    swrich.setMsgCount(Integer.valueOf(parArr[3]));
                    //修改审批全局配置
                    empTransDao.update(conn, swrich);
                }
            }

            //查询数据库，是否存在默认值

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corp.getCorpCode());
            BaseBiz baseBiz = new BaseBiz();
            CommonBiz commonBiz = new CommonBiz();


            try {
                List<LfCorpConf> lfCorpConfList = baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);
                conditionMap.clear();

                String[] setPass = codeSplit[1].split("@");
                conditionMap.put("pwd.count", setPass[0].trim());
                conditionMap.put("pwd.combtype", setPass[1].trim() + "," + setPass[2].trim() + "," + setPass[3].trim());
                conditionMap.put("pwd.upcycle", setPass[4].trim());
                conditionMap.put("pwd.pastalarm", setPass[5].trim());
                conditionMap.put("pwd.errlimit", setPass[6].trim());
                //conditionMap.put("pwd.logtime", setPass[7].trim());

                if (!"undefined".equals(setPass[7].trim()) && !"".equals(setPass[7].trim())) {
                    LfGlobalVariable globaV = null;
                    globaV = commonBiz.getGlobalVariable("LOGRETENTIONTIME");
                    globaV.setGlobalValue(Long.valueOf(setPass[7].trim()));
                    empTransDao.update(conn, globaV);
                }
                //更新模板是否可编辑参数,仅单企业更新
                if (StaticValue.getCORPTYPE() == 0) {
                    if (!"".equals(setPass[9].trim()) && setPass[9].trim() != null && !"undefined".equals(setPass[9].trim())) {
                        LfGlobalVariable glVariable = commonBiz.getGlobalVariable("TMPEDITORFLAG");
                        glVariable.setGlobalValue(Long.valueOf(setPass[9].trim()));
                        empTransDao.update(conn, glVariable);
                        //更新静态变量
                        StaticValue.setTMPEDITORFLAG(Integer.valueOf(setPass[9].trim()));
                    }
                }
                String dynPwd = URLDecoder.decode(setPass[8].trim(), "utf-8").trim();
                dynPwd = dynPwd.replace("{#P#}", "#P_1#");
                conditionMap.put("pwd.dynpwd", dynPwd);

                for (int i = 0; i < lfCorpConfList.size(); i++) {
                    LfCorpConf conf = lfCorpConfList.get(i);

                    //位数
                    if ("pwd.count".equals(conf.getParamKey())) {
                        //需要对密码进行转换，防止出现0000000的情况
                        if (!"".equals(setPass[0].trim()) && !"null".equals(setPass[0].trim())) {
                            conf.setParamValue(Integer.parseInt(setPass[0].trim()) + "");
                        } else {
                            conf.setParamValue(setPass[0].trim());
                        }
                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.count");
                    }
                    //组合形式
                    if ("pwd.combtype".equals(conf.getParamKey())) {
                        conf.setParamValue(setPass[1].trim() + "," + setPass[2].trim() + "," + setPass[3].trim());
                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.combtype");
                    }
                    //修改周期
                    if ("pwd.upcycle".equals(conf.getParamKey())) {
                        if (!"".equals(setPass[4].trim()) && !"null".equals(setPass[4].trim())) {
                            conf.setParamValue(Integer.parseInt(setPass[4].trim()) + "");
                        } else {
                            conf.setParamValue(setPass[4].trim());
                        }
                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.upcycle");

                    }
                    //过期提醒
                    if ("pwd.pastalarm".equals(conf.getParamKey())) {
                        if (!"".equals(setPass[5].trim()) && !"null".equals(setPass[5].trim())) {
                            conf.setParamValue(Integer.parseInt(setPass[5].trim()) + "");
                        } else {
                            conf.setParamValue(setPass[5].trim());
                        }

                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.pastalarm");

                    }
                    //错误上限
                    if ("pwd.errlimit".equals(conf.getParamKey())) {
                        String limt = setPass[6].trim();
                        if (!"".equals(limt) && !"null".equals(limt)) {
                            conf.setParamValue(Integer.parseInt(limt) + "");
                        } else {
                            conf.setParamValue(limt);
                        }
                        //当错误上线为空时候，用户输入的错误次数清除掉
                        if ("".equals(limt) || "0".equals(limt)) {
                            String corpCode = "100001";
                            if (conf.getCorpCode() != null && !"".equals(conf.getCorpCode())) {
                                corpCode = conf.getCorpCode();
                            }
                            LinkedHashMap<String, String> contionMap = new LinkedHashMap<String, String>();
                            LinkedHashMap<String, String> updateMap = new LinkedHashMap<String, String>();
                            contionMap.put("corpCode", corpCode);
                            updateMap.put("pwderrortimes", "0");
                            empTransDao.update(conn, LfSysuser.class, updateMap, contionMap);
                        }
                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.errlimit");

                    }
					
					/*if("pwd.logtime".equals(conf.getParamKey())){
						//需要对密码进行转换，防止出现0000000的情况
						if(!"".equals(setPass[7].trim())&&!"null".equals(setPass[7].trim())){
							conf.setParamValue(Integer.parseInt(setPass[7].trim())+"");
						}else{
							conf.setParamValue(setPass[7].trim());
						}
						empTransDao.update(conn, conf);
						conditionMap.remove("pwd.logtime");
					}*/

                    if ("pwd.dynpwd".equals(conf.getParamKey())) {
                        //需要对密码进行转换，防止出现0000000的情况
                        if (!"".equals(dynPwd) && !"null".equals(dynPwd)) {
                            conf.setParamValue(dynPwd + "");
                        } else {
                            conf.setParamValue(dynPwd);
                        }
                        empTransDao.update(conn, conf);
                        conditionMap.remove("pwd.dynpwd");
                    }
                }

                Set<String> set = conditionMap.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    LfCorpConf lf = new LfCorpConf();
                    lf.setCorpCode(corp.getCorpCode());
                    lf.setParamKey(key);
                    lf.setParamValue(conditionMap.get(key));
                    empTransDao.save(conn, lf);
                }

                //*******签名同步开关处理开始***************
                //有值则更新
                if (dataCondition != null && dataCondition.get("actsyncflag") != null) {
                    String actsyncflag = dataCondition.get("actsyncflag");

                    LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
                    objectMap.put("globalValue", actsyncflag);

                    LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
                    condition.put("globalKey", "ACTSYNCFLAG");
                    empTransDao.update(conn, LfGlobalVariable.class, objectMap, condition);
                }
                //*******签名同步开关处理结束***************

                //****此处处理贴尾处理***
                String tailctrlFlag = dataCondition.get("updateTailctrlFlag");
                String msgtailFlag = dataCondition.get("updateMsgtailFlag");
                if ("1".equals(tailctrlFlag)) {
                    empTransDao.update(conn, tailctrl);
                }
                if ("1".equals(msgtailFlag)) {
                    empTransDao.update(conn, msgtail);
                }

                //*****邮件参数更改*********
                LfCorpConf lfCorpConf;
                String protocol = "";
                String host = "";
                String port = "";
                String username = "";
                String name = "";
                String pazzwd = "";
                String corpCode = dataCondition.get("lgcorpcode");
                for (int i = 0; i < lfCorpConfList.size(); i++) {
                    lfCorpConf = lfCorpConfList.get(i);
                    if ("email.protocol".equals(lfCorpConf.getParamKey())) {
                        protocol = lfCorpConf.getParamValue();
                        if (!dataCondition.get("email.protocol").equals(protocol)) {
                            lfCorpConf.setParamValue(dataCondition.get("email.protocol"));
                            empTransDao.update(conn, lfCorpConf);
                        }
                        //防止数据库取出空值
                        if ("".equals(protocol) || protocol == null) {
                            protocol = "sign";
                        }
                    }
                    if ("email.host".equals(lfCorpConf.getParamKey())) {
                        host = lfCorpConf.getParamValue();
                        if (!dataCondition.get("email.host").equals(host)) {
                            lfCorpConf.setParamValue(dataCondition.get("email.host"));
                            empTransDao.update(conn, lfCorpConf);
                        }
                        if ("".equals(host) || host == null) {
                            host = "sign";
                        }
                    }
                    if ("email.port".equals(lfCorpConf.getParamKey())) {
                        port = lfCorpConf.getParamValue();
                        if (!dataCondition.get("email.port").equals(port)) {
                            lfCorpConf.setParamValue(dataCondition.get("email.port"));
                            empTransDao.update(conn, lfCorpConf);
                        }
                        if ("".equals(port) || port == null) {
                            port = "sign";
                        }
                    }
                    if ("email.username".equals(lfCorpConf.getParamKey())) {
                        username = lfCorpConf.getParamValue();
                        if (!dataCondition.get("email.username").equals(username)) {
                            lfCorpConf.setParamValue(dataCondition.get("email.username"));
                            empTransDao.update(conn, lfCorpConf);
                        }
                        if ("".equals(username) || username == null) {
                            username = "sign";
                        }
                    }
                    if ("email.password".equals(lfCorpConf.getParamKey())) {
                        pazzwd = lfCorpConf.getParamValue();
                        if (!dataCondition.get("email.password").equals(pazzwd)) {
                            lfCorpConf.setParamValue(dataCondition.get("email.password"));
                            empTransDao.update(conn, lfCorpConf);
                        }
                        if ("".equals(pazzwd) || pazzwd == null) {
                            pazzwd = "sign";
                        }
                    }
                    if ("email.name".equals(lfCorpConf.getParamKey())) {
                        name = lfCorpConf.getParamValue();
                        if (!dataCondition.get("email.name").equals(name)) {
                            lfCorpConf.setParamValue(dataCondition.get("email.name"));
                            empTransDao.update(conn, lfCorpConf);
                        }
                        if ("".equals(name) || name == null) {
                            name = "sign";
                        }
                    }
                    lfCorpConf = null;
                }
                //值为空则新增
                List<LfCorpConf> emailList = new LinkedList<LfCorpConf>();

                if (protocol.equals("")) {
                    lfCorpConf = new LfCorpConf();
                    lfCorpConf.setCorpCode(corpCode);
                    lfCorpConf.setParamKey("email.protocol");
                    lfCorpConf.setParamValue(dataCondition.get("email.protocol"));
                    emailList.add(lfCorpConf);
                    lfCorpConf = null;
                }
                if (port.equals("")) {
                    lfCorpConf = new LfCorpConf();
                    lfCorpConf.setCorpCode(corpCode);
                    lfCorpConf.setParamKey("email.port");
                    lfCorpConf.setParamValue(dataCondition.get("email.port"));
                    emailList.add(lfCorpConf);
                    lfCorpConf = null;
                }
                if (host.equals("")) {
                    lfCorpConf = new LfCorpConf();
                    lfCorpConf.setCorpCode(corpCode);
                    lfCorpConf.setParamKey("email.host");
                    lfCorpConf.setParamValue(dataCondition.get("email.host"));
                    emailList.add(lfCorpConf);
                    lfCorpConf = null;
                }
                if (username.equals("")) {
                    lfCorpConf = new LfCorpConf();
                    lfCorpConf.setCorpCode(corpCode);
                    lfCorpConf.setParamKey("email.username");
                    lfCorpConf.setParamValue(dataCondition.get("email.username"));
                    emailList.add(lfCorpConf);
                    lfCorpConf = null;
                }
                if (pazzwd.equals("")) {
                    lfCorpConf = new LfCorpConf();
                    lfCorpConf.setCorpCode(corpCode);
                    lfCorpConf.setParamKey("email.password");
                    lfCorpConf.setParamValue(dataCondition.get("email.password"));
                    emailList.add(lfCorpConf);
                    lfCorpConf = null;
                }
                if (name.equals("")) {
                    lfCorpConf = new LfCorpConf();
                    lfCorpConf.setCorpCode(corpCode);
                    lfCorpConf.setParamKey("email.name");
                    lfCorpConf.setParamValue(dataCondition.get("email.name"));
                    emailList.add(lfCorpConf);
                    lfCorpConf = null;
                }
                if (emailList.size() > 0) {
                    baseBiz.addList(LfCorpConf.class, emailList);
                }
                EmpExecutionContext.info("邮件参数配置修改成功");
                String sendcmdflag = dataCondition.get("sendcmdflag");


//					LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
//					objectMap.put("paramValue", sendcmdflag);
//					
//					LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
//					condition.put("gwType", "4000");
//					condition.put("paramItem", "FILTERB01TDMO");
//					//是否开启上行回复退订，手机号自动添加黑名单功能 (0不启用、1启用)。
//					empTransDao.update(conn, AgwParamValue.class, objectMap, condition);
//					//同时修改指令表（先查询网关，如果集群是多个网关编号）
//					//查询网关
//					LinkedHashMap<String, String> hashmap = new LinkedHashMap<String, String>();
//					//设置查询条件
//					hashmap.put("gwType", "4000");
//					//设置排序条件
//					String cmdParam="MOCMDROUTE=1";//默认启动
//					if("1".equals(sendcmdflag)){//如果为启动
//						cmdParam="MOCMDROUTE=1";
//					}else if("0".equals(sendcmdflag)){//如果为关闭
//						cmdParam="MOCMDROUTE=0";
//					}
//					
//					List<GwCluStatus> gwCluStatusList =baseBiz.getByCondition(GwCluStatus.class, hashmap, null);
//					if(gwCluStatusList!=null&&gwCluStatusList.size()>0){
//						for(int k=0;k<gwCluStatusList.size();k++){
//							GwCluStatus gs=gwCluStatusList.get(k);
//							int gw=gs.getPriGwNo();
//							AcmdQueue queue = new AcmdQueue();
//							
//							queue.setGwNo(gw);
//							queue.setGwType(4000);//网关类型：3000：SPGATE 4000：网关
//							/***
//							 * 命令类型（目前用到的有:5000,6004）
//								帐号属性等变动类(1000)：密码、绑定IP、权限、类型、流速、单天发送量、状态、时段控制、扣费类型、充值/回收、同步等
//								发送帐号通道变动类(2000)：绑定删除、禁用、新增、改变、扣费类型、时段控制、签名、规则、同步等
//								通道帐号通道变动类(3000)：绑定删除、禁用、新增、改变、属性、规则、同步等
//								系统参数变动类(4000)：号段新增、删除、改变、同步；关键字新增、删除、改变、同步；黑名单新增、删除、同步等
//								系统运行配置数变动类(5000)：CMPP/WBS/SPGATE配置参数改变.
//								实时控制类(6000)：服务器暂停、重启、关闭等操作；帐号踢出、IP黑名单、按帐号控制其发送暂停等；界面发送测试信息；向平台索要指定的监控信息等；暂停DB写入读取更新等操作
//								6001：帐号踢出，需将帐号列表以英文逗号分隔的形式，做为命令参数
//								6002：帐号发送暂停，需将帐号列表以英文逗号分隔的形式，做为命令参数
//								6003：帐号发送恢复，需将帐号列表以英文逗号分隔的形式，做为命令参数
//								6004:停止指定任务发送
//								6005:恢复6004指令中的发送
//								6101：服务器暂停
//								6102：服务器关闭
//								6103：服务器重新启动，由暂停转至启动
//								6104：启动服务器，由关闭状态转至启动
//							 */
//							queue.setCmdType(5000);
//							queue.setCmdParam(cmdParam);
//							queue.setId(0);
//							empTransDao.save(conn, queue);
//						}
//					}


                //如果是修改值成功，并且是开启的情况下
                if ("1".equals(sendcmdflag)) {
                    //开启
                    corp.setIsOpenTD(1);
                    String sendcmdcontent = dataCondition.get("sendcmdcontent");
                    String[] itcmd = sendcmdcontent.split(",");
                    String strcorp = "";
                    int intcorp = 0;
                    if (corp.getCorpCode() != null && !"".equals(corp.getCorpCode())) {
                        strcorp = corp.getCorpCode();
                        intcorp = Integer.parseInt(strcorp);
                    }

                    //值为空则新增
                    List<GwTdcmd> gwlist = new LinkedList<GwTdcmd>();
                    if (itcmd != null && itcmd.length > 0) {
                        for (int k = 0; k < itcmd.length; k++) {
                            if ("".equals(itcmd[k].trim())) {
                                continue;
                            }
                            GwTdcmd cmd = new GwTdcmd();
                            cmd.setPbsvrtype(" ");
                            cmd.setMatchtype(0);//0精确匹配、1模糊匹配(信息内容包含即可)都不区分大小写
                            cmd.setOptype(1);//	操作类型：0 解除、1退订
                            cmd.setPbcropcode(strcorp);
                            cmd.setPbspnumber(" ");
                            cmd.setPbsvrtype(" ");
                            cmd.setPbuserid("000000");
                            //默认是启用
                            cmd.setStatus(0);
                            cmd.setTdecid(intcorp);
                            cmd.setTdspnumber(" ");
                            cmd.setTdtimes(1);
                            cmd.setTduserid("000000");
                            cmd.setTdcmd(itcmd[k]);
                            gwlist.add(cmd);
                        }
                    }
                    if (gwlist != null && gwlist.size() > 0) {
                        LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
                        condition.put("pbcropcode", corp.getCorpCode());
                        //先删除，后增加
                        empTransDao.delete(conn, GwTdcmd.class, condition);
                        //保存
                        empTransDao.save(conn, gwlist, GwTdcmd.class);
                    }
                } else {
                    //关闭
                    corp.setIsOpenTD(0);
                    LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
                    condition.put("pbcropcode", corp.getCorpCode());
                    LinkedHashMap<String, String> object = new LinkedHashMap<String, String>();
                    object.put("status", "1");
                    empTransDao.update(conn, GwTdcmd.class, object, condition);


                }


            } catch (Exception e) {
                EmpExecutionContext.error(e, "根据企业编码和key获得对应企业配置的机构限制信息异常。");
            }

            //修改全局参数
            empTransDao.update(conn, corp);
            empTransDao.commitTransaction(conn);
            return true;
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "修改模块参数配置失败！");
            return false;
        } finally {
            //关闭链接
            empTransDao.closeConnection(conn);
        }
    }

    private String escapeReplace(String param){
        param = param.replaceAll("&amp;","&");
        param = param.replaceAll("#37;","%");
        return param;
    }


    public List<GwMsgtail> getMsgtailList(String corpCode) {
        List<GwMsgtail> msgtailList = null;
        try {
            msgtailList = new MenuParamConfigDAO().getMsgtailList(corpCode);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取全局贴尾管理对象异常。");
        }
        return msgtailList;

    }

    public List<GwTailctrl> getTailctrlList(String corpCode) {
        List<GwTailctrl> tailctrlList = null;
        try {
            tailctrlList = new MenuParamConfigDAO().getTailctrlList(corpCode);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取贴尾控制管理对象异常。");
        }
        return tailctrlList;
    }
}
