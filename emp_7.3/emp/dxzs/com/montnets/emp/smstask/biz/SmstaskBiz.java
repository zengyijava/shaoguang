package com.montnets.emp.smstask.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.SendMessage;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.WGStatus;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TaskBiz;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.common.vo.LfMttaskVo;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.smstask.dao.SmstaskDAO;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;


public class SmstaskBiz extends SuperBiz {

    private final SmstaskDAO smstaskDao;

    private final SmsSendBiz smsSendBiz = new SmsSendBiz();

    private final BaseBiz baseBiz = new BaseBiz();

    private final KeyWordAtom keyWordAtom = new KeyWordAtom();
    private final SendMessage sendMessage = new SendMessage();
    private final BalanceLogBiz balanceBiz = new BalanceLogBiz();
    private final TaskBiz taskBiz = new TaskBiz();
    /*群发历史汇总 线程安全判断*/
    private final Map<String, String> taskIdMap = new ConcurrentHashMap<String, String>();

    public SmstaskBiz() {
        smstaskDao = new SmstaskDAO();
    }

    /**
     * 机构树
     *
     * @param userid
     * @return
     * @throws Exception
     */

    public String getDepartmentJosnData(Long userid) throws Exception {
        StringBuffer tree = null;
        //根据用户id获取用户信息
        LfSysuser currUser = empDao.findObjectByID(LfSysuser.class, userid);
        if (currUser.getPermissionType() == 1) {
            tree = new StringBuffer("[]");
        } else {
            //机构biz
            DepBiz depBiz = new DepBiz();
            List<LfDep> lfDeps;

            try {
                //如果是100000的企业
                if (currUser.getCorpCode().equals("100000")) {
                    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                    LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
                    conditionMap.put("depState", "1");
                    orderbyMap.put("depId", StaticValue.ASC);

                    lfDeps = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
                } else {
                    //获取管辖范围内的所有机构
                    lfDeps = depBiz.getAllDeps(userid);
                }
                LfDep lfDep = null;
                //机构树的json数据拼写
                tree = new StringBuffer("[");
                for (int i = 0; i < lfDeps.size(); i++) {
                    lfDep = lfDeps.get(i);
                    tree.append("{");
                    tree.append("id:").append(lfDep.getDepId());
                    tree.append(",name:'").append(lfDep.getDepName()).append("'");
                    tree.append(",pId:").append(lfDep.getSuperiorId());
                    tree.append(",depId:'").append(lfDep.getDepId() + "'");
                    //tree.append(",level:").append(lfDep.getDepLevel());
                    //tree.append(",dlevel:").append(lfDep.getDepLevel());
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != lfDeps.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");

            } catch (Exception e) {
                //异常处理
                EmpExecutionContext.error(e, "机构树加载biz层异常！");
            }
        }
        if (tree != null) {
            return tree.toString();
        } else {
            return new StringBuffer("[]").toString();
        }
    }

    /**
     * 机构树(重载方法)
     *
     * @param userid
     * @return
     * @throws Exception
     */
    public String getDepartmentJosnData(Long userid, LfSysuser loginSysuser) throws Exception {
        StringBuffer tree = null;
        //根据用户id获取用户信息
        LfSysuser currUser = loginSysuser;
        //个人权限
        if (currUser.getPermissionType() == 1) {
            tree = new StringBuffer("[]");
        } else {
            //机构权限
            //机构biz
            DepBiz depBiz = new DepBiz();
            List<LfDep> lfDeps;

            try {
                //如果是100000的企业
                if (currUser.getCorpCode().equals("100000")) {
                    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                    LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
                    conditionMap.put("depState", "1");
                    orderbyMap.put("depId", StaticValue.ASC);
                    //orderbyMap.put("depId", StaticValue.ASC);
                    //查询所有机构
                    lfDeps = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
                } else {
                    //获取管辖范围内的所有机构
                    lfDeps = depBiz.getAllDepByUserIdAndCorpCode(userid, currUser.getCorpCode());
                }
                LfDep lfDep = null;
                //机构树的json数据拼写
                tree = new StringBuffer("[");
                for (int i = 0; i < lfDeps.size(); i++) {
                    lfDep = lfDeps.get(i);
                    tree.append("{");
                    tree.append("id:").append(lfDep.getDepId());
                    tree.append(",name:'").append(lfDep.getDepName()).append("'");
                    tree.append(",pId:").append(lfDep.getSuperiorId());
                    tree.append(",depId:'").append(lfDep.getDepId() + "'");
                    //tree.append(",level:").append(lfDep.getDepLevel());
                    //tree.append(",dlevel:").append(lfDep.getDepLevel());
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != lfDeps.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");

            } catch (Exception e) {
                //异常处理
                EmpExecutionContext.error(e, "机构树加载biz层异常！");
            }
        }
        if (tree != null) {
            return tree.toString();
        } else {
            return new StringBuffer("[]").toString();
        }
    }

    /**
     * 树
     *
     * @return
     */

    public String getDepartmentJosnData2(Long depId, Long userId) {
        StringBuffer tree = null;
        LfSysuser sysuser = null;
        try {
            sysuser = empDao.findObjectByID(LfSysuser.class, userId);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "群发历史或群发任务查询操作员信息异常！");
            tree = new StringBuffer("[]");
        }
        if (sysuser.getPermissionType() == 1) {
            tree = new StringBuffer("[]");
        } else {
            DepBiz depBiz = new DepBiz();
            List<LfDep> lfDeps;

            try {
                lfDeps = null;

                if ((sysuser != null) && (sysuser.getCorpCode().equals("100000"))) {
                    if (depId == null) {
                        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();

                        conditionMap.put("superiorId", "0");

                        conditionMap.put("depState", "1");

                        orderbyMap.put("depId", "ASC");
                        orderbyMap.put("deppath", "ASC");
                        lfDeps = new BaseBiz().getByCondition(LfDep.class, conditionMap, orderbyMap);
                    } else {
                        lfDeps = new DepBiz().getDepsByDepSuperId(depId);
                    }

                } else if (depId == null) {
                    lfDeps = new ArrayList();
                    LfDep lfDep = (LfDep) depBiz.getAllDeps(userId).get(0);
                    lfDeps.add(lfDep);
                } else {
                    lfDeps = new DepBiz().getDepsByDepSuperId(depId);
                }


                LfDep lfDep = null;
                tree = new StringBuffer("[");
                for (int i = 0; i < lfDeps.size(); i++) {
                    lfDep = lfDeps.get(i);
                    tree.append("{");
                    tree.append("id:").append(lfDep.getDepId());
                    tree.append(",name:'").append(lfDep.getDepName()).append("'");
                    tree.append(",pId:").append(lfDep.getSuperiorId());
                    tree.append(",depId:'").append(lfDep.getDepId() + "'");
                    tree.append(",dlevel:'").append(lfDep.getDepLevel() + "'");
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != lfDeps.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");

            } catch (Exception e) {
                EmpExecutionContext.error(e, "群发历史或群发任务操作员树加载biz层异常！");
                tree = new StringBuffer("[]");
            }
        }
        return tree.toString();
    }

    /**
     * 获取树的方法(重载方法)
     *
     * @param depId        机构ID
     * @param userId       操作员ID
     * @param loginSysuser 当前登录操作员
     * @return 返回生成树的字符串
     */
    public String getDepartmentJosnData2(Long depId, Long userId, LfSysuser loginSysuser) {
        StringBuffer tree = null;
        LfSysuser sysuser = null;
        try {
            //当前登录操作员
            sysuser = loginSysuser;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "群发历史或群发任务查询操作员信息异常！");
            tree = new StringBuffer("[]");
        }
        //判断当前登录操作员的权限，个人权限不能显示机构树
        if (sysuser.getPermissionType() == 1) {
            tree = new StringBuffer("[]");
        } else {
            DepBiz depBiz = new DepBiz();
            List<LfDep> lfDeps;

            try {
                lfDeps = null;
                //10000企业查询
                if ((sysuser != null) && (sysuser.getCorpCode().equals("100000"))) {
                    if (depId == null) {
                        //设置查询条件
                        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();

                        conditionMap.put("superiorId", "0");

                        conditionMap.put("depState", "1");

                        orderbyMap.put("depId", "ASC");
                        orderbyMap.put("deppath", "ASC");
                        lfDeps = new BaseBiz().getByCondition(LfDep.class, conditionMap, orderbyMap);
                    } else {
                        lfDeps = new DepBiz().getDepsByDepSuperId(depId);
                    }

                } else if (depId == null) {
                    lfDeps = new ArrayList();
                    //根据操作员ID和企业编码，查询操作员所管辖的机构
                    LfDep lfDep = (LfDep) depBiz.getAllDepByUserIdAndCorpCode(userId, sysuser.getCorpCode()).get(0);
                    lfDeps.add(lfDep);
                } else {
                    //根据机构ID、企业编码查询出所有子机构
                    lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, sysuser.getCorpCode());
                }

                //组装字符串，返回前台
                LfDep lfDep = null;
                tree = new StringBuffer("[");
                for (int i = 0; i < lfDeps.size(); i++) {
                    lfDep = lfDeps.get(i);
                    tree.append("{");
                    tree.append("id:").append(lfDep.getDepId());
                    tree.append(",name:'").append(lfDep.getDepName()).append("'");
                    tree.append(",pId:").append(lfDep.getSuperiorId());
                    tree.append(",depId:'").append(lfDep.getDepId() + "'");
                    tree.append(",dlevel:'").append(lfDep.getDepLevel() + "'");
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != lfDeps.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");

            } catch (Exception e) {
                //异常信息记录日志
                EmpExecutionContext.error(e, "群发历史或群发任务操作员树加载biz层异常！");
                tree = new StringBuffer("[]");
            }
        }
        //返回值
        return tree.toString();
    }

    /**
     * 获取审批信息
     *
     * @param mtID
     * @param rLevel
     * @return
     * @throws Exception
     */

    public List<LfFlowRecordVo> getFlowRecordVos(String mtID, String rLevel, String reviewType)
            throws Exception {
        List<LfFlowRecordVo> frVosList;
        try {
            frVosList = smstaskDao
                    .findLfFlowRecordVo(mtID, rLevel, reviewType);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取审批信息失败。");
            throw e;
        }
        return frVosList;
    }

    /**
     * 发送详情
     *
     * @param conditionMap
     * @param orderMap
     * @param pageInfo
     * @param tableName
     * @return
     * @throws Exception
     */

    public List<SendedMttaskVo> getMtTask(LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderMap, PageInfo pageInfo, String tableName)
            throws Exception {
        List<SendedMttaskVo> mttaskList = null;
        try {
            //如果分页信息为空则表名是导出数据的查询
            if (pageInfo == null) {
                mttaskList = smstaskDao.findMtTaskVo(conditionMap, orderMap, tableName);
            }
            //如果分页信息为不为空时表示是详情页面的查询
            else {
                mttaskList = smstaskDao.findMtTaskVo(conditionMap, orderMap, pageInfo, tableName);

            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "完美通短发送详情查询异常。");
            throw e;
        }
        return mttaskList;
    }

    /**
     * 群发历史任务查询发送详情
     *
     * @param conditionMap
     * @param orderMap
     * @param pageInfo
     * @param tableName
     * @param isBackDb
     * @return
     * @throws Exception
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-10-22 下午02:38:49
     */
    public List<SendedMttaskVo> getMtTask(LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderMap, PageInfo pageInfo, String tableName, boolean isBackDb, PageInfo realDbpageInfo)
            throws Exception {
        List<SendedMttaskVo> mttaskList = null;
        try {
            //如果分页信息为空则表名是导出数据的查询
            if (pageInfo == null) {
                mttaskList = smstaskDao.findMtTaskVo(conditionMap, orderMap, tableName, isBackDb);
            }
            //如果分页信息为不为空时表示是详情页面的查询
            else {
                mttaskList = smstaskDao.findMtTaskVo(conditionMap, orderMap, pageInfo, tableName, isBackDb, realDbpageInfo);

            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "群发任务送详情查询异常。");
            throw e;
        }
        return mttaskList;
    }

    /**
     * 群发历史与群发任务查询的方法
     *
     * @param curLoginedUser
     * @param lfMttaskVo
     * @param pageInfo
     * @return
     * @throws Exception
     */

    public List<LfMttaskVo> getLfMttaskVo(Long curLoginedUser,
                                          LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception {
        List<LfMttaskVo> mtTaskVosList;
        try {
            if (pageInfo == null) {
                mtTaskVosList = smstaskDao.findLfMttaskVo(curLoginedUser,
                        lfMttaskVo);
            } else {
                mtTaskVosList = smstaskDao.findLfMttaskVo(curLoginedUser,
                        lfMttaskVo, pageInfo);
            }

            //拼装VO
            mtTaskVosList = completeLfMttaskVo(mtTaskVosList);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "群发历史与群发任务查询异常。");
            //异常处理
            throw e;
        }
        return mtTaskVosList;
    }

    /**
     * 发送详情（查两个表示时调用）
     *
     * @param conditionMap
     * @param orderMap
     * @param pageInfo
     * @param tableName
     * @return
     * @throws Exception
     */

    public List<SendedMttaskVo> getMtTaskTwo(LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderMap, PageInfo pageInfo, String tableName)
            throws Exception {
        List<SendedMttaskVo> mttaskList = null;
        try {
            //如果分页信息为空则表名是导出数据的查询
            if (pageInfo == null) {
                mttaskList = smstaskDao.findMtTaskVoTwoTable(conditionMap, orderMap, tableName);
            }
            //如果分页信息为不为空时表示是详情页面的查询
            else {
                mttaskList = smstaskDao.findMtTaskVoTwoTable(conditionMap, orderMap, pageInfo, tableName);

            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "完美通知发送详情（查两个表示调用）查询异常。");
            throw e;
        }
        return mttaskList;
    }


    /**
     * 跟据taskid查询mtdatareport表中任务的icount和
     *
     * @param conditionMap
     * @return
     * @throws Exception
     */

    public long getSumIcount(LinkedHashMap<String, String> conditionMap)
            throws Exception {
        return smstaskDao.findSumIcount(conditionMap);

    }

    /**
     * 群发历史与群发任务查询的方法
     *
     * @param lfMttaskVo
     * @param pageInfo
     * @return
     * @throws Exception
     */

    public List<LfMttaskVo> getLfMttaskVoWithoutDomination(
            LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception {
        List<LfMttaskVo> mtTaskVosList;
        try {
            if (pageInfo == null) {
                mtTaskVosList = smstaskDao.findLfMttaskVoWithoutDomination(
                        lfMttaskVo);
            } else {
                mtTaskVosList = smstaskDao.findLfMttaskVoWithoutDomination(
                        lfMttaskVo, pageInfo);
            }
            //拼装VO
            mtTaskVosList = completeLfMttaskVo(mtTaskVosList);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "群发历史与群发任务查询异常。");
            throw e;
        }
        return mtTaskVosList;
    }


    public List<LfSysuser> getAllSysusersOfSmsTaskRecordByDep(Long userId, Long depId) throws Exception {
        List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
        try {

            if (null != userId) {
                lfSysuserList = new SmstaskDAO().findDomUserBySysuserIDOfSmsTaskRecordByDep(userId
                        .toString(), depId.toString());
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询用户编号及机构编号对应关系异常。");
            throw e;
        }
        return lfSysuserList;
    }


    /**
     * 异步加载机构的主方法
     *
     * @param depId
     * @param userid
     * @return
     */
    private String getDepartmentJosnData(Long depId, Long userid) {

        BaseBiz baseBiz = new BaseBiz();
        StringBuffer tree = null;
        try {
            // 当前登录操作员
            LfSysuser curUser = baseBiz.getLfSysuserByUserId(userid);
            // 判断是否个人权限
            if (curUser.getPermissionType() == 1) {
                tree = new StringBuffer("[]");
            } else {
                // 机构biz
                DepBiz depBiz = new DepBiz();
                List<LfDep> lfDeps;

                lfDeps = null;

                if (curUser.getCorpCode().equals("100000")) {
                    if (depId == null) {
                        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
                        // 只查询顶级机构
                        conditionMap.put("superiorId", "0");
                        // 查询未删除的机构
                        conditionMap.put("depState", "1");
                        // 排序
                        orderbyMap.put("depId", StaticValue.ASC);
                        orderbyMap.put("deppath", StaticValue.ASC);
                        lfDeps = baseBiz.getByCondition(LfDep.class,
                                conditionMap, orderbyMap);
                    } else {
                        lfDeps = new DepBiz().getDepsByDepSuperId(depId);
                    }

                } else {
                    if (depId == null) {
                        lfDeps = new ArrayList<LfDep>();
                        LfDep lfDep = depBiz.getAllDeps(userid).get(0);
                        lfDeps.add(lfDep);
                    } else {
                        lfDeps = new DepBiz().getDepsByDepSuperId(depId);
                    }
                }

                LfDep lfDep = null;
                tree = new StringBuffer("[");
                for (int i = 0; i < lfDeps.size(); i++) {
                    lfDep = lfDeps.get(i);
                    tree.append("{");
                    tree.append("id:").append(lfDep.getDepId());
                    tree.append(",name:'").append(lfDep.getDepName()).append(
                            "'");
                    tree.append(",pId:").append(lfDep.getSuperiorId());
                    tree.append(",depId:'").append(lfDep.getDepId() + "'");
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != lfDeps.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "异步加载机构树数据biz层查询异常！");
            tree = new StringBuffer("[]");
        }
        return tree.toString();
    }


    private List<LfMttaskVo> completeLfMttaskVo(List<LfMttaskVo> mtTaskVosList) throws Exception {
        //mtTaskVosList大于0，则进行Vo拼装。
        if (mtTaskVosList != null && mtTaskVosList.size() > 0) {
            //查询操作员
            String userids = "";
            for (int i = 0; i < mtTaskVosList.size(); i++) {
                userids += mtTaskVosList.get(i).getUserId() + ",";
            }
            if (userids.length() > 0 && userids.endsWith(",")) {
                userids = userids.substring(0, userids.length() - 1);
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userId&in", userids);
            List<LfSysuser> lfSysusers = empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);
            Map<String, LfSysuser> lfSysuserMap = new LinkedHashMap<String, LfSysuser>();
            for (int i = 0; i < lfSysusers.size(); i++) {
                lfSysuserMap.put(lfSysusers.get(i).getUserId().toString(), lfSysusers.get(i));
            }

            //查询操作员机构
            String depids = "";
            for (int i = 0; i < lfSysusers.size(); i++) {
                depids += lfSysusers.get(i).getDepId() + ",";
            }
            if (depids.length() > 0 && depids.endsWith(",")) {
                depids = depids.substring(0, depids.length() - 1);
            }
            Map<String, LfDep> lfDepMap = new LinkedHashMap<String, LfDep>();
            if (depids != null && !"".equals(depids)) {
                conditionMap.clear();
                conditionMap.put("depId&in", depids);
                List<LfDep> lfDeps = empDao.findListBySymbolsCondition(LfDep.class, conditionMap, null);
                for (int i = 0; i < lfDeps.size(); i++) {
                    lfDepMap.put(lfDeps.get(i).getDepId().toString(), lfDeps.get(i));
                }
            }
            //拼装VO
            LfSysuser lfSysuser = null;
            LfDep lfDep = null;
            for (int i = 0; i < mtTaskVosList.size(); i++) {
                lfSysuser = lfSysuserMap.get(String.valueOf(mtTaskVosList.get(i).getUserId()));
                if (lfSysuser != null) {
                    mtTaskVosList.get(i).setName(lfSysuser.getName());
                    mtTaskVosList.get(i).setUserName(lfSysuser.getUserName());
                    mtTaskVosList.get(i).setUserState(lfSysuser.getUserState());
                    lfDep = lfDepMap.get(String.valueOf(lfSysuser.getDepId()));
                    if (lfDep != null) {
                        mtTaskVosList.get(i).setDepName(lfDep.getDepName());
                        mtTaskVosList.get(i).setDepId(lfDep.getDepId());
                    } else {
                        mtTaskVosList.get(i).setDepName("-");
                        mtTaskVosList.get(i).setDepId(0l);
                    }
                } else {
                    mtTaskVosList.get(i).setName("-");
                    mtTaskVosList.get(i).setUserName("-");
                    //找不到操作员
                    mtTaskVosList.get(i).setUserState(3);
                    mtTaskVosList.get(i).setDepName("-");
                    mtTaskVosList.get(i).setDepId(0l);
                }
                lfSysuser = null;
                lfDep = null;
            }
        }
        return mtTaskVosList;
    }


    public Map<Long, Long> getTaskRemains(String taskids) {
        return smstaskDao.getTaskRemains(taskids);
    }


    /**
     * 获取群发历史回复信息
     *
     * @param conditionMap 条件
     * @param pageInfo     分页
     * @return
     */
    public List<DynaBean> getReplyDetailList(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
        try {

            //查询回复记录
            List<DynaBean> replyDetailList = smstaskDao.getReplyDetailList(conditionMap, pageInfo);
            //设置回复姓名
            if (replyDetailList != null && replyDetailList.size() > 0) {
                //回复号码
                StringBuffer phoneSb = new StringBuffer();
                //回复号码
                String phones = "";
                //KEY:回复号码;value:姓名
                Map<String, String> phoneNameMap = new LinkedHashMap<String, String>();
                //获取所有回复号码
                for (DynaBean replyDetail : replyDetailList) {
                    if (replyDetail.get("phone") != null && !"".equals(replyDetail.get("phone").toString().trim())) {
                        phoneSb.append(replyDetail.get("phone").toString().trim()).append(",");
                    }
                }
                //存在回复号码
                if (phoneSb.length() > 0 && phoneSb.indexOf(",") > -1) {
                    phones = phoneSb.substring(0, phoneSb.length() - 1);
                    //查询条件存在姓名,则直接使用
                    if (conditionMap.get("replyName") == null || "".equals(conditionMap.get("replyName"))) {
                        //获取回复详情用户姓名,匹配优先级:员工>客户通讯录>操作员
                        smstaskDao.getReplyDetailName(phones, conditionMap.get("corpCode"), phoneNameMap);
                        //存在回复号码姓名
                        if (phoneNameMap.size() > 0) {
                            String phone = "";
                            for (int i = 0; i < replyDetailList.size(); i++) {
                                if (replyDetailList.get(i) != null) {
                                    phone = replyDetailList.get(i).get("phone") == null ? "" : replyDetailList.get(i).get("phone").toString();
                                    if (phone != null && !"".equals(phone)) {
                                        replyDetailList.get(i).set("name", phoneNameMap.get(phone));
                                    } else {
                                        replyDetailList.get(i).set("name", "-");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return replyDetailList;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取群发历史回复信息失败！");
            return null;
        }
    }


    public String changeTiming(String mtId, String timerTime, String mtMsg, String showtailcontent, Long lguserid, String lgcorpcode, HttpServletRequest request) {

        LinkedHashMap<String, String> mttaskConditionMap = new LinkedHashMap<String, String>();
        mttaskConditionMap.put("mtId", mtId);
        LinkedHashMap<String, String> timeersConditionMap = new LinkedHashMap<String, String>();

        //是否需要重新审核标识字段
        boolean needAudit = false;

        List<LfTimer> lfTimers = null;
        LfMttask lfMttask = null;
        LfMttask lfMttaskBak = null;
        try {
            lfMttask = empDao.findObjectByID(LfMttask.class, Long.valueOf(mtId));
            //lfMttaskBak = empDao.findObjectByID(LfMttask.class, Long.valueOf(mtId));
            lfMttaskBak = (LfMttask) BeanUtils.cloneBean(lfMttask);
            timeersConditionMap.put("taskExpression", String.valueOf(lfMttask.getTaskId()));
            lfTimers = empDao.findListByCondition(LfTimer.class, timeersConditionMap, null);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "任务表查询异常");
            return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_0", request);
        }

        if (lfTimers.size() > 0) {//未审核的定时任务，在定时任务表中不会有数据
            if ("now".equals(timerTime)) {
                Date dateDb = lfTimers.get(0).getNextTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateDb);
                calendar.add(Calendar.SECOND, -300);
                if (calendar.getTime().before(new Date())) {
                    return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_1", request);
                }
            } else {
                Date dateDb = lfTimers.get(0).getNextTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateDb);
                calendar.add(Calendar.SECOND, -60 * 15);
                if (calendar.getTime().before(new Date())) {
                    return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_2", request);
                }
            }
        }

        //关键字检查
        if (StringUtils.isNotBlank(mtMsg) && !(mtMsg.equals(lfMttask.getMsg()))) {
            //包含的关键字，为""时内容无关键字
            String words = null;
            try {
                words = keyWordAtom.checkText(mtMsg, lgcorpcode);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "关键字检查异常");
            }
            String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
            String strlguserid = SysuserUtil.strLguserid(request);
            //包含关键字
            if (!"".equals(words)) {
                EmpExecutionContext.error("相同内容预览，关键字检查未通过，发送内容包含违禁词组:'" + words + "'，taskid:" + lfMttask.getTaskId()
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + strlguserid
                        + "，errCode:" + IErrorCode.V10016);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(IErrorCode.V10016);
                return desc + MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_15", request) + words;
            }
        }

        Connection connection = empTransDao.getConnection();
        boolean updateLfMt = true;
        boolean updateLfTi = true;
        //发送结果
        String result = "000";
        try {
            empTransDao.beginTransaction(connection);

            //立即发送
            if ("now".equals(timerTime)) {
                //未审批通过禁止发送  -1未审批  2拒绝
                if (lfMttask.getReState() == (-1) || lfMttask.getReState() == 2) {
                    return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_14", request);
                }

                if (StringUtils.isNotBlank(mtMsg) && !(mtMsg.equals(lfMttask.getMsg()))) {
                    lfMttask.setMsg(mtMsg + showtailcontent);
                    if (lfMttask.getReState() == 1 || lfMttask.getReState() == 2) {//已审批过的,如果修改了内容需要重新审核
                        needAudit = true;//是否需要重新审核 true 需要； false 不需要
                        lfMttask.setReState(-1);
                    }
                }
                if (!needAudit) {
                    lfMttask.setTimerTime(new Timestamp(Calendar.getInstance()
                            .getTime().getTime()));//更新时间为当前时间
                }

                //修改定时或修改内容
            } else {
                if (StringUtils.isNotBlank(timerTime) && !("now").equals(timerTime)) {
                    lfMttask.setTimerTime(Timestamp.valueOf(timerTime));//定时时间
                }
                if (StringUtils.isNotBlank(mtMsg) && !(mtMsg.equals(lfMttask.getMsg()))) {//内容
                    lfMttask.setMsg(mtMsg + showtailcontent);
                    if (lfMttask.getReState() == 1 || lfMttask.getReState() == 2) {//已审批过的,如果修改了内容需要重新审核
                        needAudit = true;//是否需要重新审核 true 需要； false 不需要
                        lfMttask.setReState(-1);
                    }
                }
            }

            updateLfMt = empTransDao.update(connection, lfMttask);
            if (lfTimers.size() > 0) {//定时任务审批未通过的时候，不会有定时任务数据
                LfTimer lfTimer = lfTimers.get(0);
                if (!("now".equals(timerTime)) && StringUtils.isNotBlank(timerTime)) {
                    lfTimer.setStartTime(Timestamp.valueOf(timerTime));
                    lfTimer.setNextTime(Timestamp.valueOf(timerTime));
                    updateLfTi = empTransDao.update(connection, lfTimer);
                }
            }
            if (needAudit) {//重新提交审核
                boolean auditRes = commitAudit(connection, lfMttask, request);
                if (!auditRes) {
                    return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_3", request);
                }
                if (lfTimers.size() > 0) {
                    LfTimer lfTimer = lfTimers.get(0);
                    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                    conditionMap.put("timerTaskId", String.valueOf(lfTimer.getTimerTaskId()));
                    int delNum = empTransDao.delete(connection, LfTimer.class, conditionMap);
                    if (delNum > 0) {
                        updateLfTi = true;
                    }
                }
            }

            empTransDao.commitTransaction(connection);
            //立即发送
            if (lfMttask.getReState() == 1 || lfMttask.getReState() == 0) {//审批通过或无需审批 才能发送
                if ("now".equals(timerTime)) {
                    result = sendTimerTask(lfMttask, request);
                    if (!"000".equals(result)) {
                        empDao.update(lfMttaskBak);
                        empDao.saveObjectReturnID(lfTimers.get(0));
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "事物提交异常");
            result = MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_0", request);
            empTransDao.rollBackTransaction(connection);
            return result;
        } finally {
            if (!"000".equals(result)) {
                try {
                    empDao.update(lfMttaskBak);
                    empDao.saveObjectReturnID(lfTimers.get(0));
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "更新任务表、定时表异常");
                }
            }
            empTransDao.closeConnection(connection);
        }
        if (updateLfMt && updateLfTi) {
            result = MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_5", request);
        } else {
            result = MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_6", request);
        }
        return result;

    }


    public LfMttask findLfMttaskById(Long mtId) {
        if (null == mtId) {
            return null;
        }
        LfMttask lfMttask = null;
        try {
            lfMttask = empDao.findObjectByID(LfMttask.class, mtId);
        } catch (Exception e) {
            EmpExecutionContext.error("出现异常");
        }
        return lfMttask;
    }


    public JSONObject getProgress(String taskIdCondition) {
        JSONObject rsObject = new JSONObject();
        if (taskIdCondition == null) {
            return null;
        }
        String[] taskIdArr = taskIdCondition.split(",");
        for (String taskId : taskIdArr) {
            if (StringUtils.isEmpty(taskId)) {
                continue;
            }
            LfMttask lfMttask = getLfMtTaskByTaskId(taskId);
            String percent = "";
            if (lfMttask != null) {
                percent = getPercent(lfMttask.getSendNum(), lfMttask.getPhoneNum());
            }
            String endSendTime = "";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            //如果是历史数据,或者当前未下发完,则不显示完成时间
            if (!"100%".equals(percent) || lfMttask.getSendNum() == -1) {
                endSendTime = "-";
                //如果已经下发完成
            } else if ("100%".equals(percent)) {
                endSendTime = df.format(lfMttask.getFinishTime());
            }
            rsObject.put(taskId, percent + "&" + endSendTime);
        }

        return rsObject;
    }

    /**
     * 根据分子和分母获得百分值
     *
     * @param x
     * @param total
     * @return
     */
    private String getPercent(int x, int total) {
        //如果历史数据或发送数大于总数,则为100%,-1表示未历史数据
        if (total == -1 || x > total) {
            return "100%";
        } else if (total == 0) {
            return "0%";
        }
        String result = "";
        double x_double = x * 1.0;
        double tempResult = x_double / total;
        DecimalFormat df1 = new DecimalFormat("0%");
        result = df1.format(tempResult);
        return result;
    }

    private LfMttask getLfMtTaskByTaskId(String taskId) {
        List<LfMttask> list = null;
        LfMttask lfMttask = null;
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            conditionMap.put("taskId", taskId);
            list = empDao.findListByCondition(LfMttask.class, conditionMap, null);
            if (list != null && list.size() > 0) {
                lfMttask = list.get(0);
            } else {
                EmpExecutionContext.info("获取进度条时,查询不到该条数据");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "出现异常");
        }
        return lfMttask;
    }

    /**
     * 立即发送定时任务
     *
     * @param lfMttask
     * @return
     */
    public String sendTimerTask(LfMttask lfMttask, HttpServletRequest request) {

        //1.判断该操作员或企业是否还有发送权限
        String checkRes = checkUserCorp(lfMttask, request);
        if (!("true".equals(checkRes))) {
            return checkRes;
        }
        //2.处理定时任务表数据
        deleteTimer(lfMttask);

        //3.立即发送
        String sendRes = "";
        try {
            sendRes = sendMsg(lfMttask, request);
            // 根据错误编码从网关定义查找错误信息
            String reultClong = sendRes;
            String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
            sendRes = new WGStatus(langName).getInfoMap().get(sendRes);
            // 如果返回状态网关中未定义，则重置为之前状态
            if (sendRes == null) {
                sendRes = reultClong;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "短信发送异常");
        }

        return sendRes;
    }


    /**
     * 判断用户和企业是否有发送权限
     *
     * @param mt
     * @return
     */
    private String checkUserCorp(LfMttask mt, HttpServletRequest request) {
        SendMessage sendMessage = new SendMessage();
        //重置条件
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        //设置企业编码查询条件
        conditionMap.put("corpCode", mt.getCorpCode());
        //获取企业信息
        List<LfCorp> corpList = null;
        try {
            corpList = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询企业信息异常");
            return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_7", request);
        }
        if (corpList == null || corpList.size() == 0) {
            EmpExecutionContext.error("立即发送定时任务时，通过corpCode=" + mt.getCorpCode() + "获取不到任务对象");
            return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_8", request);
        }
        //企业状态为禁用
        if (corpList.get(0).getCorpState() == 0) {
            sendMessage.ChangeSendState(mt);
            EmpExecutionContext.error("立即发送定时任务时，企业状态为禁用，corpCode:" + mt.getCorpCode());
            return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_9", request);
        }

        //如果此条发送记录的操作员已禁用，则不进行发送操作
        LfSysuser user = null;
        try {
            user = baseBiz.getById(LfSysuser.class, mt.getUserId());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询用户信息异常。");
            return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_10", request);
        }
        boolean checkFlag = new CheckUtil().checkSysuserInCorp(user, mt.getCorpCode(), mt.getSpUser(), null);
        if (!checkFlag) {
            EmpExecutionContext.error("立即发送定时任务时，检查操作员和发送账号是否是当前企业下，checkFlag:" + checkFlag
                    + "，userid:" + user.getUserId()
                    + "，spuser:" + mt.getSpUser());
            return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_11", request);
        }
        //如果此用户的状态是0：禁用
        if (user.getUserState() == 0) {
            sendMessage.ChangeSendState(mt);
            EmpExecutionContext.error("立即发送定时任务时，用户状态为禁用，userState:" + user.getUserState());
            return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_12", request);
        }
        // 如果发送状态不为未发送
        if (mt.getSendstate() != 0) {
            EmpExecutionContext.error("发送任务状态不为未发送,定时任务执行失败！sendstate:" +
                    mt.getSendstate().toString() + ";taskid:" +
                    mt.getTaskId().toString() + ";mtId:" + mt.getMtId().toString());
            return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_13", request);
        }
        return "true";
    }

    /**
     * 审核
     *
     * @return
     */
    private boolean commitAudit(Connection connection, LfMttask mt, HttpServletRequest request) {
        LfFlow flow = null;
        int sendCount = Integer.parseInt(mt.getIcount());
        // 默认无需审批:0
        mt.setReState(0);
        try {
            flow = smsSendBiz.checkUserFlow(mt.getUserId(), mt.getCorpCode(), "1", Long.valueOf(mt.getIcount()));
        } catch (EMPException e) {
            //不是异常，不需要记录日志，在抛出前已记录INFO日志
            EmpExecutionContext.error(e, "检查是否绑定了审核流程出现异常");
            return false;
        }

        // 需要审核
        if (flow != null) {
            // 设置审核状态为未审核
            mt.setReState(-1);
            ReviewBiz revBiz = new ReviewBiz();
            // 保存短信审批记录
            //判断是否是网讯发送，修改类型，为了处理发送短信后，短信提示中的字
            int infotype = 1;
            if (mt != null && mt.getMsType() == 6) {//网讯发送
                infotype = 5;
            }
            boolean saveReviewResult = false;
            try {
                Long taskId = mt.getTaskId();
                LinkedHashMap<String, String> conditioMap = new LinkedHashMap<String, String>();
                conditioMap.put("mtId", String.valueOf(taskId));
                List<LfFlowRecord> lfFlowRecords = empDao.findListByCondition(LfFlowRecord.class, conditioMap, null);
                if (lfFlowRecords.size() > 0) {
                    for (LfFlowRecord lfFlowRecord : lfFlowRecords) {
                        if (new Integer(4).equals(lfFlowRecord.getRState())
                                && new Integer(1).equals(lfFlowRecord.getIsComplete())) {
                            continue;
                        }
                        lfFlowRecord.setRState(4);
                        lfFlowRecord.setIsComplete(1);
                        empTransDao.update(connection, lfFlowRecord);
                    }

                    saveReviewResult = revBiz.addFlowRecords(connection, mt.getTaskId(), mt.getTitle(), mt.getSubmitTime(), infotype, flow, mt.getUserId(), "1");
                    return saveReviewResult;
                } else {
                    saveReviewResult = revBiz.addFlowRecords(connection, mt.getTaskId(), mt.getTitle(), mt.getSubmitTime(), infotype, flow, mt.getUserId(), "1");
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "审核异常");
                return false;
            }
            if (saveReviewResult) {
                //empTransDao.commitTransaction(conn);
                // 提交事务
                return true;
            } else {
                // 运营商余额回收
                balanceBiz.huishouFee(sendCount, mt.getSpUser(), mt.getMsType());
                Map<String, String> infoMap = null;
                LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
                infoMap = new CommonBiz().checkMapNull(infoMap, lfSysuser.getUserId(), lfSysuser.getCorpCode());
                //如果开启机构计费，则补回机构费用
                if ("true".equals(infoMap.get("feeFlag"))) {
                    // 机构回收
                    int recResult = balanceBiz.sendSmsAmountByUserId(null, mt.getUserId(), sendCount * -1);
                    if (recResult != 1) {
                        EmpExecutionContext.error("补回机构费用失败，taskid:" + mt.getTaskId() + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
                        return false;
                    }
                }
                ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.B20020);
                EmpExecutionContext.error("添加审核任务失败，taskid:" + mt.getTaskId() + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
                return false;
            }
        }
        return true;
    }


    /**
     * 发送
     *
     * @param mt
     * @return
     */
    private String sendMsg(LfMttask mt, HttpServletRequest request) {
        String recResult = "";
        mt.setTimerTime(new Timestamp(Calendar.getInstance()
                .getTime().getTime()));
        try {
            //获取文件所在的服务器地址
            CommonBiz commonBiz = new CommonBiz();
            String fileUrl = commonBiz.checkServerFile(mt.getMobileUrl());
            if (fileUrl != null && !"".equals(fileUrl)) {
                mt.setFileuri(fileUrl);
                if (mt.getMsType().intValue() == 5) {
                    LfSysuser user = empDao.findObjectByID(LfSysuser.class, mt.getUserId());
                    recResult = sendSms(mt, user);
                } else {
                    recResult = sendMessage.sendSms(mt, null);
                }
            } else {
                EmpExecutionContext.error("立即发送短信定时任务时,获取发送文件地址失败！fileUrl:" + fileUrl + ";mobileUrl:" + mt.getMobileUrl()
                        + ";taskid:" + mt.getTaskId().toString() + ";mtId:" + mt.getMtId().toString());
                return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_0", request);
            }
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "立即发送定时任务异常");
            return MessageUtils.extractMessage("dxzs", "dxzs_changeTimer_title_0", request);
        }
        return recResult;
    }

    /**
     * 删除定时任务表数据
     *
     * @param lfMttask
     * @return
     */
    private boolean deleteTimer(LfMttask lfMttask) {

        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        //设置企业编码查询条件
        conditionMap.put("taskExpression", String.valueOf(lfMttask.getTaskId()));
        int delRes = 0;
        try {
            List<LfTimer> lfTimers = empDao.findListByCondition(LfTimer.class, conditionMap, null);
            if (lfTimers.size() > 0) {
                delRes = empDao.delete(LfTimer.class, conditionMap);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "删除定时任务表信息异常");
            return false;
        }
        if (delRes > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 定时短信发送
     *
     * @return
     * @throws Exception
     */
    public String sendSms(LfMttask mt, LfSysuser user) {
        // 模板ID
        String moduleid = "";
        if ("MF0001".equals(mt.getBusCode())) {
            moduleid = "10260";
        } else if ("MF0002".equals(mt.getBusCode())) {
            moduleid = "10270";
        } else if ("MF0003".equals(mt.getBusCode())) {
            moduleid = "10280";
        } else {
            moduleid = "";
        }
        String returnStr = "";
        mt.setSendstate(2);
        try {
            String result = "";
            // 非定时短信发送(0-不定时发送)
            result = createbatchMtRequestWithCode(mt.getSpUser(), mt.getSpPwd(), mt.getSubNo(), "2", mt.getTaskId().toString(), mt.getTitle(), mt.getMsg(), "1", (mt.getFileuri() + mt.getMobileUrl()), mt.getParams(), mt.getBusCode(), user.getUserCode(), moduleid, "0");
            if (!result.equals("")) {
                int index = result.indexOf("mterrcode");
                returnStr = result.substring(index + 10, index + 13);
                if (returnStr.equals("000")) {
                    mt.setSendstate(1);
                    returnStr = "000";
                } else {
                    BalanceLogBiz b = new BalanceLogBiz();
                    b.huishouFee(Integer.parseInt(mt.getIcount()), mt.getSpUser(), 1);
                    b.sendSmsAmountByUserId(null, mt.getUserId(), -1 * Integer.parseInt(mt.getIcount()));
                    returnStr = result.substring(index - 8, index - 1);
                }
            }
            mt.setErrorCodes(returnStr);
            if (mt.getTimerStatus().intValue() == 0) {
                mt.setTimerTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "移动财务发送异常！");
            mt.setErrorCodes("sendError");
        } finally {
            //成功数
            mt.setSucCount(null);
            //失败数
            mt.setFaiCount(null);

            SendMessage sendMess = new SendMessage();
            int a = 1;
            //如果多次更新失败，则重复更新
            while (a < 4) {
                try {
                    if (sendMess.updateMttaskByTaskid(mt)) {
                        a = 4;
                    } else {
                        a++;
                    }
                } catch (Exception e2) {
                    try {
                        Thread.sleep(500L);
                    } catch (Exception e3) {
                    } finally {
                        EmpExecutionContext.error(e2, "移动财务更新任务表失败：第" + a + "次");
                        a++;
                    }
                }
            }
        }
        return returnStr;
    }

    /**
     * 群发接口。群发参数未封转前传入
     *
     * @param spUserid   发送账户
     * @param spPassword 发送账户密码
     * @param sa         下行源地址
     * @param bmttype    提交类型
     * @param taskId     任务id
     * @param title      标题
     * @param content    内容
     * @param priority   优先级
     * @param url        群发文件地址
     * @param verifycode 8字节验证码
     * @param svrtype    业务类型
     * @param userCode   用户编码
     * @param moduleid   模块id
     * @param rptflag    是否需要状态报告
     * @return 请求网关返回的字符串
     * @throws Exception
     */
    public String createbatchMtRequestWithCode(String spUserid, String spPassword,
                                               String sa, String bmttype, String taskId, String title, String content,
                                               String priority, String url, String verifycode, String svrtype, String userCode,
                                               String moduleid, String rptflag) throws Exception {

        //创建发送对象
        WGParams wgParams = new WGParams();
        wgParams.setSpid(spUserid);
        wgParams.setSppassword(spPassword);
        if (sa != null && !"".equals(sa.trim())) {
            wgParams.setSa(sa);
        }
        wgParams.setBmttype("2");
        wgParams.setTaskid(taskId);
        wgParams.setPriority(priority);
        wgParams.setTitle(title);

        wgParams.setUrl(url);


        wgParams.setParam1(userCode);
        wgParams.setSvrtype(svrtype);
        wgParams.setRptflag(rptflag);
        wgParams.setModuleid(moduleid);

        wgParams.setVerifycode(verifycode);
        wgParams.setDc("25");
        //当是相同内容群发时，需要设置发送内容
        switch (Integer.parseInt(bmttype)) {
            case 1:
                wgParams.setContent(content);
                break;
            case 2:
                break;
            case 3:
                break;
        }
        HttpSmsSend httpSmsSend = new HttpSmsSend();
        //调用发送接口
        return httpSmsSend.createbatchMtRequest(wgParams);
    }

    /**
     * 群发历史汇总
     *
     * @return smstaskDao
     * @throws Exception
     */
    public boolean updateLfmttask(String taskId,String mtid) {

        try {
            String tableName = queryTable(mtid);
            boolean result = smstaskDao.updateLfmttask(tableName, taskId);
            EmpExecutionContext.info("当前查询的表名为：" + tableName +"当前批次号为：" + taskId);
            return result;
        }catch (Exception e){
            EmpExecutionContext.error(e, "群发历史任务汇总异常！");
            return false;
        }
    }

    public String queryTable(String mtid) throws Exception {

        //将下行实时表改成GW_MT_TASK_BAK
        String tableName = "GW_MT_TASK_BAK";
        //根据mtid获取任务信息
        LfMttask Lfmttask = baseBiz.getById(LfMttask.class, mtid);
        //获取当前任务的发送时间
        Timestamp sendTime = Lfmttask.getTimerTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date1 = df.parse(df.format(new Date()));
        Date date2 = df.parse(df.format(sendTime.getTime()));
        //当前时间
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        //发送时间
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        //计算时间，当前时间减去发送时间小于3天,查实时表，否则查对应月的历史表 add(c2.DATE,4):表示对发送时间加4天操作
        c2.add(Calendar.DATE, 4);
        if (c2.after(c1)) {
            return tableName;
        } else {
            //计算获得历史表的表名（发送时间的月份）
            int month = sendTime.getMonth() + 1;
            String year = df.format(sendTime.getTime()).substring(0, 4);
            String date = year;
            if (month < 10) {
                //日期
                date += "0" + month;
            } else {
                //日期
                date += month;
            }
            //历史表名
            tableName = "MTTASK" + date;
            return tableName;
        }
    }

}
