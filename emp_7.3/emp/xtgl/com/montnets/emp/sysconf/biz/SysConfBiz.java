package com.montnets.emp.sysconf.biz;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.entity.system.LfSysParam;
import com.montnets.emp.sysconf.vo.ParamConfVo;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.*;

/**
 * web参数配置业务类
 *
 * @author :  tanjy
 * @version :  V1.0
 * @date :  2021-02-02 09:31
 */
public class SysConfBiz extends BaseBiz {

    /**
     * 参数值为字符串类型
     */
    private static final int GLOBAL_STRVALUE = 2;
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

    /**
     * 获取Web运行配置参数值
     *
     * @param paramConfVoList 需要查询的参数信息
     */
    public void setParamValues(String corCode, List<ParamConfVo> paramConfVoList) {
        try {
            //从LF_CORP_PARAM表中查询企业配置参数数据
            Map<String, String> corpConfMap = getCorpConfMap(corCode);
            // 从LF_SYSPARAM表中查询系统参数配置数据
            Map<String, String> sysParamMap = getSysParamMap();
            // 从LF_GLOBAL_VARIABLE表中查询系统参数配置数据
            Map<String, String> globalMap = getGlobalMap();

            for (ParamConfVo paramConfVo : paramConfVoList) {
                if (paramConfVo.getParamType() == CORP_CONF_TYPE) {
                    paramConfVo.setParamValue(corpConfMap.get(paramConfVo.getParamKey()));

                } else if (paramConfVo.getParamType() == SYS_PARAM_TYPE) {
                    paramConfVo.setParamValue(sysParamMap.get(paramConfVo.getParamKey()));

                } else if (paramConfVo.getParamType() == GLOBAL_VARIABLE_TYPE) {
                    // values[0]存放数字类型值，values[1]存放字符串类型值
                    String[] values = new String[2];
                    if (globalMap.get(paramConfVo.getParamKey()) != null) {
                        values = globalMap.get(paramConfVo.getParamKey()).split(";;");
                    }
                    // 如果参数值的类型为字符串 
                    if ((paramConfVo.getDataType()) == GLOBAL_STRVALUE) {
                        paramConfVo.setParamValue(values[1]);
                    } else {
                        paramConfVo.setParamValue(values[0]);
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "从数据库查询Web运行配置参数值异常!");
        }
    }

    /**
     * 更新表LF_CORP_CONF
     *
     * @param corCode 企业编码
     * @param map     更新参数信息，key为参数项，value为参数值
     * @return 更新结果
     */
    public boolean saveCorpConf(Map map, String corCode) {
        // 获取所有需要保存的参数项键
        Set<String> keySet = map.keySet();
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        Connection conn = null;
        try {
            conn = empTransDao.getConnection();
            empTransDao.beginTransaction(conn);
            for (String key : keySet) {
                conditionMap.put("paramKey", key);
                conditionMap.put("corpCode", corCode);
                objectMap.put("paramValue", map.get(key).toString());
                empTransDao.update(conn, LfCorpConf.class, objectMap, conditionMap);
                conditionMap.clear();
                objectMap.clear();
            }
            empTransDao.commitTransaction(conn);
            return true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "WEB运行参数配置保存LF_CORP_CONF表数据异常！");
            empTransDao.rollBackTransaction(conn);
            return false;
        } finally {
            empTransDao.closeConnection(conn);
        }
    }

    /**
     * 更新表LF_SYS_PARAM
     *
     * @param map 更新参数信息，key为参数项，value为参数值
     * @return 更新结果
     */
    public boolean saveSysParam(Map map) {
        // 获取所有需要保存的参数项键
        Set<String> keySet = map.keySet();
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        Connection conn = null;
        try {
            conn = empTransDao.getConnection();
            empTransDao.beginTransaction(conn);
            for (String key : keySet) {
                conditionMap.put("paramItem", key);
                objectMap.put("paramValue", map.get(key).toString());
                empTransDao.update(conn, LfSysParam.class, objectMap, conditionMap);
                conditionMap.clear();
                objectMap.clear();
            }
            empTransDao.commitTransaction(conn);
            return true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "WEB运行参数配置保存LF_SYS_PARAM表数据异常！");
            empTransDao.rollBackTransaction(conn);
            return false;
        } finally {
            empTransDao.closeConnection(conn);
        }
    }

    /**
     * 更新表LF_GLOBAL_VARLABLE
     *
     * @param map 更新参数信息，key为参数项，value为参数值
     * @return 更新结果
     */
    public boolean saveGlobalVariable(Map map) {

        // 获取所有需要保存的参数项键
        Set<String> keySet = map.keySet();
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

        List<ParamConfVo> paramConfVos = globalParams();
        //获取LF_GLOBAL_VARIABLE表参数项值的数据类型
        Map<String, Integer> globalTypeMap = getGlobalTypeMap(paramConfVos);
        Connection conn = null;
        try {
            conn = empTransDao.getConnection();
            empTransDao.beginTransaction(conn);
            for (String key : keySet) {
                conditionMap.put("globalKey", key);
                // 类型值为2，代表参数值为字符串类型，则更新GLOBALSTRVALUE字段值
                if (globalTypeMap.get(key) == GLOBAL_STRVALUE) {
                    objectMap.put("globalStrValue", map.get(key).toString());
                } else {
                    objectMap.put("globalValue", map.get(key).toString());
                }
                empTransDao.update(conn, LfGlobalVariable.class, objectMap, conditionMap);
                conditionMap.clear();
                objectMap.clear();
            }
            empTransDao.commitTransaction(conn);
            return true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "WEB运行参数配置保存LF_GLOBAL_VARIABLE表数据异常！");
            empTransDao.rollBackTransaction(conn);
            return false;
        } finally {
            empTransDao.closeConnection(conn);
        }
    }

    /**
     * 检查前端传来的参数值是否为空字符串
     *
     * @param map key为参数项，value为参数值
     * @return 校验结果
     */
    public boolean checkValue(Map map) {
        Set keySet = map.keySet();
        for (Object key : keySet) {
            if (map.get(key) == null || "".equals(String.valueOf(map.get(key)).trim())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取全局变量参数值数据类型
     *
     * @param globalVoList 全局变量详细信息list表
     * @return key为参数项 value为参数值数据类型
     */
    private Map<String, Integer> getGlobalTypeMap(List<ParamConfVo> globalVoList) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (ParamConfVo paramConfVo : globalVoList) {
            map.put(paramConfVo.getParamKey(), paramConfVo.getDataType());
        }
        return map;
    }

    /**
     * 从数据库查询LF_CORP_CONF表数据
     *
     * @param corpCode 企业编码
     * @return key为参数项 value为参数值
     */
    private Map<String, String> getCorpConfMap(String corpCode) {
        Map<String, String> map = null;
        try {
            LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
            condition.put("corpCode", corpCode);
            List<LfCorpConf> lfCorpConfList = findListByCondition(LfCorpConf.class, condition, null);
            map = new HashMap<String, String>();
            for (LfCorpConf lfCorpConf : lfCorpConfList) {
                map.put(lfCorpConf.getParamKey(), lfCorpConf.getParamValue());
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "从数据库查询LF_CORP_CONF表数据异常！");
        }
        return map;
    }

    /**
     * 从数据库查询LF_SYS_PARAM表数据
     *
     * @return key为参数项 value为参数值
     */
    private Map<String, String> getSysParamMap() {
        Map<String, String> map = null;
        try {
            List<LfSysParam> lfSysParamList = findListByCondition(LfSysParam.class, null, null);
            map = new HashMap<String, String>();
            for (LfSysParam lfSysParam : lfSysParamList) {
                map.put(lfSysParam.getParamItem(), lfSysParam.getParamValue());
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "从数据库查询LF_SYS_PARAM表数据异常！");
        }
        return map;
    }

    /**
     * 查询LF_GLOBAL_VARIABLE表数据
     *
     * @return key为参数项 value为参数值
     */
    private Map<String, String> getGlobalMap() {
        Map<String, String> map = null;
        try {
            List<LfGlobalVariable> lfGlobalVariables = findListByCondition(LfGlobalVariable.class, null, null);
            map = new HashMap<String, String>();
            for (LfGlobalVariable lfGlobalVariable : lfGlobalVariables) {
                // LF_GLOBALVARIABLE表中有两个字段存放参数值，分别存放数字型和字符串型
                map.put(lfGlobalVariable.getGlobalKey(), lfGlobalVariable.getGlobalValue() + ";;" + lfGlobalVariable.getGlobalStrValue());
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询LF_GLOBAL_VARIABLE表数据异常！");
        }
        return map;
    }
    /**
     * 组装LF_CORP_CONF企业配置参数信息
     *
     * @return 企业配置详细信息list集合
     */
    public List<ParamConfVo> corpConfs() {
        List<ParamConfVo> corpConfVoList = new ArrayList<ParamConfVo>();
        //封装企业配置需要查询的参数
        ParamConfVo paramConfVo1 = new ParamConfVo(0, "dep.maxlevel", "机构最大层级", "机构最大层级（默认10，修改的机构最大层级不能小于当前值）", null, 0);
        ParamConfVo paramConfVo2 = new ParamConfVo(0, "dep.maxchild", "最大子机构数", "每个机构最大直接子机构数（默认100，修改的最大机构数不能小于当前值）", null, 0);
        // 最后一项参数描述里添加表名，方便定位表
        ParamConfVo paramConfVo3 = new ParamConfVo(0, "dep.maxdep", "最大全部机构数", "每个机构下最大全部机构数（默认10000，修改的最大全部机构数不能小于当前值）[LF_CORP_CONF]", null, 0);

        corpConfVoList.add(paramConfVo1);
        corpConfVoList.add(paramConfVo2);
        corpConfVoList.add(paramConfVo3);
        return corpConfVoList;
    }

    /**
     * 组装LF_SYS_PARAM系统配置参数信息
     *
     * @return 系统配置参数详细信息集合
     */
    public List<ParamConfVo> sysParams() {
        List<ParamConfVo> sysParamVoList = new ArrayList<ParamConfVo>();
        //封装系统配置需要查询的参数
        ParamConfVo paramConfVo1 = new ParamConfVo(1, "isRemind", " 是否开启短信审批提醒", " 是否开启短信审批提醒（默认是，是：开启审批提醒；否：关闭审批提醒）", null, 1);
        ParamConfVo paramConfVo2 = new ParamConfVo(1, "isHdata", "是否执行EMP汇总", "是否执行EMP汇总（默认是，是：执行；否：不执行。每天凌晨执行一次）", null, 1);
        ParamConfVo paramConfVo3 = new ParamConfVo(1, "isSumm", "是否固定时间间隔执行EMP汇总", "是否固定时间间隔执行EMP汇总（默认是，是：执行；否：不执行。按照设置的时间间隔从下行实时表汇总数据）", null, 1);
        ParamConfVo paramConfVo4 = new ParamConfVo(0, "SummTimeInterval", "EMP汇总的固定时间间隔", "EMP汇总的固定时间间隔，单位小时（取值范围1~12小时，默认6小时，此参数修改后需重启WEB才能生效）", null, 1);
        ParamConfVo paramConfVo5 = new ParamConfVo(2, "ENDHOUR", "检查网关是否汇总完成截止时间", "取值范围5:00-8:00，默认6:00。网关超过此时间点未汇总完成，则每天凌晨一次的EMP汇总不执行", null, 1);
        ParamConfVo paramConfVo6 = new ParamConfVo(1, "SPUSER_ISLWR", "是否支持小写账号", "ORACLE DB2 是否支持小写账号（默认否，是：支持；否：不支持）", null, 1);
        ParamConfVo paramConfVo7 = new ParamConfVo(1, "loginYanZhengMa", "是否需要验证码", "登录页面是否需要验证码（默认是，是：需要；否：不需要）", null, 1);
        // 最后一项参数描述里添加表名，方便定位表
        ParamConfVo paramConfVo8 = new ParamConfVo(0, "cxtjMtExportLimit", "下行记录导出最大条数", "系统下行记录导出的最大记录条数，单位万（取值范围1~1500，默认100万）[LF_SYS_PARAM]", null, 1);

        sysParamVoList.add(paramConfVo1);
        sysParamVoList.add(paramConfVo2);
        sysParamVoList.add(paramConfVo3);
        sysParamVoList.add(paramConfVo4);
        sysParamVoList.add(paramConfVo5);
        sysParamVoList.add(paramConfVo6);
        sysParamVoList.add(paramConfVo7);
        sysParamVoList.add(paramConfVo8);
        return sysParamVoList;
    }

    /**
     * 封装LF_GLOBAL_VARIABLE全局变量参数信息
     *
     * @return 全局变量详细信息集合
     */
    public List<ParamConfVo> globalParams() {
        List<ParamConfVo> globalVoList = new ArrayList<ParamConfVo>();
        //封装全局变量需要查询的参数
        ParamConfVo paramConfVo1 = new ParamConfVo(0, "HTTP_REQUEST_TIMEOUT", "HTTP请求超时时间", "HTTP请求超时时间，单位秒（取值范围60~240，默认150秒）", null, 2);
        ParamConfVo paramConfVo2 = new ParamConfVo(0, "HTTP_RESPONSE_TIMEOUT", "HTTP响应超时时间", "HTTP响应超时时间，单位秒（取值范围60~240，默认150秒）", null, 2);
        ParamConfVo paramConfVo3 = new ParamConfVo(0, "BALANCE_REQ_INTERVAL", "运营商余额请求间隔", "运营商余额请求间隔，单位秒（取值范围120~600，默认300秒）", null, 2);
        ParamConfVo paramConfVo4 = new ParamConfVo(0, "LOG_PRINT_INTERVAL", "日志写文件时间间隔", "日志写文件时间间隔，单位秒（取值范围10~60，默认20秒）", null, 2);
        ParamConfVo paramConfVo5 = new ParamConfVo(0, "BLACK_MAXCOUNT", "黑名单支持最大数量", "黑名单支持最大数量，单位万（取值范围1000~5000，默认1000万，修改黑名单支持的最大数不能小于当前值）", null, 2);
        // 最后一项参数描述里添加表名，方便定位表
        ParamConfVo paramConfVo6 = new ParamConfVo(1, "GWFEE_CHECK", "是否检查运营商余额", "是否检查运营商余额（默认是，是：检查；否：不检查）[LF_GLOBAL_VARIABLE]", null, 2);

        globalVoList.add(paramConfVo1);
        globalVoList.add(paramConfVo2);
        globalVoList.add(paramConfVo3);
        globalVoList.add(paramConfVo4);
        globalVoList.add(paramConfVo5);
        globalVoList.add(paramConfVo6);

        return globalVoList;
    }

    /**
     * 获取配置文件中数据源配置信息
     *
     * @return key：配置项  value：配置值
     */
    public Map<String, String> getDateSourceConf() {
        Map<String, String> map = new HashMap<String, String>();
        //数据库类型 
        map.put("DBType", SystemGlobals.getValue("DBType"));
        //连接类型 Oracle数据库专用
        map.put("connType", SystemGlobals.getValue("montnets.emp.connType"));
        //数据库连接池
        map.put("poolType", SystemGlobals.getValue("poolType"));
        //数据库IP地址
        map.put("databaseIp", SystemGlobals.getValue("montnets.emp.databaseIp"));
        //数据库端口号
        map.put("databasePort", SystemGlobals.getValue("montnets.emp.databasePort"));
        //数据库名称/实例名
        map.put("databaseName", SystemGlobals.getValue("montnets.emp.databaseName"));
        //数据库用户名
        map.put("user", SystemGlobals.getValue("montnets.emp.user"));
        //*****备用数据库配置****
        //是否启用备用
        map.put("use_backup_server", SystemGlobals.getValue("montnets.emp.use_backup_server"));
        //备用数据库IP地址
        map.put("databaseIp2", SystemGlobals.getValue("montnets.emp.databaseIp2"));
        //备用数据库端口号
        map.put("databasePort2", SystemGlobals.getValue("montnets.emp.databasePort2"));
        //备用数据库名称/实例名
        map.put("databaseName2", SystemGlobals.getValue("montnets.emp.databaseName2"));
        //备用数据库用户名
        map.put("user2", SystemGlobals.getValue("montnets.emp.user2"));
        //数据连接池最大连接数
        map.put("maxPoolSize", SystemGlobals.getValue("montnets.emp.maxPoolSize"));
        //数据连接池最小连接数
        map.put("minPoolSize", SystemGlobals.getValue("montnets.emp.minPoolSize"));
        //数据连接池初始连接数
        map.put("InitialPoolSize", SystemGlobals.getValue("montnets.emp.InitialPoolSize"));
        return map;
    }

    /**
     * 获取配置文件中web配置信息
     *
     * @return key：配置项  value：配置值
     */
    public Map<String, String> getWebConf(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        //是否集群
        map.put("isCluster", SystemGlobals.getValue("cluster.iscluster"));
        //集群节点编号
        map.put("serverNumber", SystemGlobals.getValue("cluster.server.number"));
        //文件内网地址
        map.put("innerUrl", SystemGlobals.getValue("montnets.fileserver.innerUrl"));
        //文件外网地址
        map.put("outerUrl", SystemGlobals.getValue("montnets.fileserver.outerUrl"));
        //网关通讯地址
        map.put("webgate", SystemGlobals.getValue("montnets.webgate"));
        //EMP外网访问地址
        map.put("EMPaddress", SystemGlobals.getValue("montnets.thisUrl"));
        //EMP外网访问地址
        map.put("EMPOuterAddress", SystemGlobals.getValue("montnets.outerUrl"));
        //网讯站点访问地址
        map.put("EMPwxaddress", SystemGlobals.getValue("wx.pageurl"));
        //EMP日志文件保存路径
        String loggeraddress = SystemGlobals.getValue("montnets.emp.LogSavePath");
        //获取根目录地址
        String defaultloggeraddress = request.getSession().getServletContext().getRealPath("/");
        //如果配置文件的地址为-1或者为空，则界面显示日志的默认地址
        if (loggeraddress == null || "null".equals(loggeraddress.trim()) || "-1".equals(loggeraddress.trim()) || "".equals(loggeraddress.trim())) {
            loggeraddress = defaultloggeraddress;
        }
        map.put("loggeraddress", loggeraddress);
        //文件备用内网地址
        map.put("bakinnerUrl", SystemGlobals.getValue("montnets.fileserver.bak.innerUrl"));
        //文件备用外网地址
        map.put("bakouterUrl", SystemGlobals.getValue("montnets.fileserver.bak.outerUrl"));
        //多语言开关
        map.put("multiLanguageEnable", SystemGlobals.getValue("multiLanguageEnable"));
        //默认页面大小
        map.put("defaultPageSize", SystemGlobals.getValue("emp.pageInfo.defaultPageSize"));
        //启用短信分批
        map.put("smsSplit", SystemGlobals.getValue("emp.sms.split"));
        //肤色版本
        map.put("frame", SystemGlobals.getValue("emp.web.frame"));
        //选择的语言
        map.put("selectedLanguage", SystemGlobals.getValue("selectedLanguage"));
        return map;
    }


}
