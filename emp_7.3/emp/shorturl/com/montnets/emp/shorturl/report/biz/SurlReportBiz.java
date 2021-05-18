package com.montnets.emp.shorturl.report.biz;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.accountpower.LfMtPri;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.report.dao.MtRecordRealDAO;
import com.montnets.emp.shorturl.report.dao.MtTaskGenericDAO;
import com.montnets.emp.shorturl.report.dao.MtTaskHistoryDAO;
import com.montnets.emp.shorturl.report.dao.SmstaskDAO;
import com.montnets.emp.shorturl.report.dao.SurlAccessStatisticsDao;
import com.montnets.emp.shorturl.report.dao.VstDetailDao;
import com.montnets.emp.shorturl.report.vo.BatchVisitVo;
import com.montnets.emp.shorturl.report.vo.LfMttaskVo;
import com.montnets.emp.shorturl.report.vo.ReplyDetailVo;
import com.montnets.emp.shorturl.report.vo.SendDetailMttaskVo;
import com.montnets.emp.shorturl.report.vo.VstDetailVo;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SurlReportBiz extends SuperBiz{
    private SurlAccessStatisticsDao dao = new SurlAccessStatisticsDao();
    private SmstaskDAO smstaskDao = new SmstaskDAO();
    /**
     * 企业顶级机构map，查询到的企业顶级机构放这里，key为企业编码，value为企业的顶级机构对象。
     */
    private static ConcurrentHashMap<String,LfDep> topDepMap = new ConcurrentHashMap<String,LfDep>();

    private VstDetailDao vstDetailDao = new VstDetailDao();

    public List<LfSysuser> getAllSysusersOfSmsTaskRecordByDep(Long userId,Long depId) throws Exception {
        List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
        try {

            if (null != userId)
                lfSysuserList = new SmstaskDAO().findDomUserBySysuserIDOfSmsTaskRecordByDep(userId
                        .toString(),depId.toString());

        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询用户编号及机构编号对应关系异常。");
            throw e;
        }
        return lfSysuserList;
    }
    /**
     * 群发历史与群发任务查询的方法
     * @param lfMttaskVo
     * @param pageInfo
     * @return
     * @throws Exception
     */
    public List<LfMttaskVo> getLfMttaskVoWithoutDomination(LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception {
        List<LfMttaskVo> mtTaskVosList;
        try {
            mtTaskVosList = smstaskDao.findLfMttaskVoWithoutDomination(lfMttaskVo, pageInfo);

            //拼装VO
            mtTaskVosList=completeLfMttaskVo(mtTaskVosList);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "群发历史与群发任务查询异常。");
            throw e;
        }
        return mtTaskVosList;
    }

    private List<BatchVisitVo> completeBatchVisitVo(List<BatchVisitVo> batchVisitVoList) throws Exception{
        //mtTaskVosList大于0，则进行Vo拼装。
        if(batchVisitVoList!=null&& batchVisitVoList.size()>0){
            //查询操作员
            StringBuilder userids= new StringBuilder();
            for (BatchVisitVo vo : batchVisitVoList) {
                userids.append(vo.getUserId()).append(",");
            }
            if(userids.length() > 0 && userids.toString().endsWith(","))
            {
                userids = new StringBuilder(userids.substring(0, userids.length() - 1));
            }
            LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
            conditionMap.put("userId&in", userids.toString());
            List<LfSysuser>  lfSysusers= empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);
            Map<String,LfSysuser> lfSysuserMap=new LinkedHashMap<String, LfSysuser>();
            for (LfSysuser lfSysuser1 : lfSysusers) {
                lfSysuserMap.put(lfSysuser1.getUserId().toString(), lfSysuser1);
            }

            //查询操作员机构
            StringBuilder depids= new StringBuilder();
            for (LfSysuser lfSysuser1 : lfSysusers) {
                depids.append(lfSysuser1.getDepId()).append(",");
            }
            if(depids.length() > 0 && depids.toString().endsWith(",")){
                depids = new StringBuilder(depids.substring(0, depids.length() - 1));
            }
            Map<String,LfDep>  lfDepMap=new LinkedHashMap<String, LfDep>();
            if(!"".equals(depids.toString())){
                conditionMap.clear();
                conditionMap.put("depId&in", depids.toString());
                List<LfDep>  lfDeps=empDao.findListBySymbolsCondition(LfDep.class, conditionMap, null);
                for (LfDep lfDep : lfDeps) {
                    lfDepMap.put(lfDep.getDepId().toString(), lfDep);
                }
            }
            //拼装VO
            LfSysuser lfSysuser=null;
            LfDep lfDep=null;
            for (BatchVisitVo vo : batchVisitVoList) {
                //计算短链失效时间
                vo.setInvalidTm(new Timestamp((long) (vo.getCreateTm().getTime() + (vo.getValidDays() * 24 * 60 * 60 *1000))));
                //设置访问次数与访问人数
                HashMap<String, Integer> map = getVisitStatistByTaskId(vo.getTaskId(),vo.getPlanTime());
                if(map != null && map.size() == 2){
                    vo.setVisitCount(map.get("visitCount"));
                    vo.setVisitorCount(map.get("visitorCount"));
                }
                lfSysuser = lfSysuserMap.get(String.valueOf(vo.getUserId()));
                if (lfSysuser != null) {
                    //vo.setName(lfSysuser.getName());
                    vo.setUserName(lfSysuser.getUserName());
                    vo.setUserState(lfSysuser.getUserState());
                    lfDep = lfDepMap.get(String.valueOf(lfSysuser.getDepId()));
                    if (lfDep != null) {
                        vo.setDepName(lfDep.getDepName());
                        vo.setDepId(lfDep.getDepId());
                    } else {
                        vo.setDepName("-");
                        vo.setDepId(0L);
                    }
                } else {
                    //vo.setName("-");
                    vo.setUserName("-");
                    //找不到操作员
                    vo.setUserState(3);
                    vo.setDepName("-");
                    vo.setDepId(0L);
                }
                lfSysuser = null;
                lfDep = null;
            }
        }
        return batchVisitVoList;
    }


    private List<LfMttaskVo> completeLfMttaskVo(List<LfMttaskVo> mtTaskVosList) throws Exception{
        //mtTaskVosList大于0，则进行Vo拼装。
        if(mtTaskVosList!=null&& mtTaskVosList.size()>0){
            //查询操作员
            StringBuilder userids= new StringBuilder();
            for (LfMttaskVo aMtTaskVosList : mtTaskVosList) {
                userids.append(aMtTaskVosList.getUserId()).append(",");
            }
            if(userids.length() > 0 && userids.toString().endsWith(","))
            {
                userids = new StringBuilder(userids.substring(0, userids.length() - 1));
            }
            LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
            conditionMap.put("userId&in", userids.toString());
            List<LfSysuser>  lfSysusers= empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);
            Map<String,LfSysuser> lfSysuserMap=new LinkedHashMap<String, LfSysuser>();
            for (LfSysuser lfSysuser1 : lfSysusers) {
                lfSysuserMap.put(lfSysuser1.getUserId().toString(), lfSysuser1);
            }

            //查询操作员机构
            StringBuilder depids= new StringBuilder();
            for (LfSysuser lfSysuser1 : lfSysusers) {
                depids.append(lfSysuser1.getDepId()).append(",");
            }
            if(depids.length() > 0 && depids.toString().endsWith(",")){
                depids = new StringBuilder(depids.substring(0, depids.length() - 1));
            }
            Map<String,LfDep>  lfDepMap=new LinkedHashMap<String, LfDep>();
            if(!"".equals(depids.toString())){
                conditionMap.clear();
                conditionMap.put("depId&in", depids.toString());
                List<LfDep>  lfDeps=empDao.findListBySymbolsCondition(LfDep.class, conditionMap, null);
                for (LfDep lfDep : lfDeps) {
                    lfDepMap.put(lfDep.getDepId().toString(), lfDep);
                }
            }
            //拼装VO
            LfSysuser lfSysuser=null;
            LfDep lfDep=null;
            for (LfMttaskVo aMtTaskVosList : mtTaskVosList) {
                lfSysuser = lfSysuserMap.get(String.valueOf(aMtTaskVosList.getUserId()));
                if (lfSysuser != null) {
                    aMtTaskVosList.setName(lfSysuser.getName());
                    aMtTaskVosList.setUserName(lfSysuser.getUserName());
                    aMtTaskVosList.setUserState(lfSysuser.getUserState());
                    lfDep = lfDepMap.get(String.valueOf(lfSysuser.getDepId()));
                    if (lfDep != null) {
                        aMtTaskVosList.setDepName(lfDep.getDepName());
                        aMtTaskVosList.setDepId(lfDep.getDepId());
                    } else {
                        aMtTaskVosList.setDepName("-");
                        aMtTaskVosList.setDepId(0L);
                    }
                } else {
                    aMtTaskVosList.setName("-");
                    aMtTaskVosList.setUserName("-");
                    //找不到操作员
                    aMtTaskVosList.setUserState(3);
                    aMtTaskVosList.setDepName("-");
                    aMtTaskVosList.setDepId(0L);
                }
                lfSysuser = null;
                lfDep = null;
            }
        }
        return mtTaskVosList;
    }
    /**
     * 群发历史与群发任务查询的方法
     * @param curLoginedUser
     * @param lfMttaskVo
     * @param pageInfo
     * @return
     * @throws Exception
     */
    public List<LfMttaskVo> getLfMttaskVo(Long curLoginedUser, LfMttaskVo lfMttaskVo, PageInfo pageInfo) throws Exception {
        List<LfMttaskVo> mtTaskVosList;
        try {
            mtTaskVosList = smstaskDao.findLfMttaskVo(curLoginedUser, lfMttaskVo, pageInfo);

            //拼装VO
            mtTaskVosList=completeLfMttaskVo(mtTaskVosList);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "群发历史与群发任务查询异常。");
            //异常处理
            throw e;
        }
        return mtTaskVosList;
    }

    /**
     * 企业短链-批次访问查询
     * @param visitVo
     * @param pageInfo
     * @return
     * @throws Exception
     */
    public List<BatchVisitVo> getBatchVisitVo(BatchVisitVo visitVo, PageInfo pageInfo) throws Exception {
        List<BatchVisitVo> batchVisitVos;
        try {
            batchVisitVos = dao.findBatchVisitVo(visitVo, pageInfo);

            //拼装VO
            batchVisitVos = completeBatchVisitVo(batchVisitVos);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业短链报表查询-批次访问统计查询异常。");
            //异常处理
            throw e;
        }
        return batchVisitVos;
    }

    public Map<Long, Long> getTaskRemains(String taskids){
        return smstaskDao.getTaskRemains(taskids);
    }
    /**
     * 获取树的方法(重载方法)
     * @param depId 机构ID
     * @param userId 操作员ID
     * @param loginSysuser 当前登录操作员
     * @return 返回生成树的字符串
     */
    public String getDepartmentJosnData2(Long depId,Long userId,LfSysuser loginSysuser){
        StringBuffer tree = null;
        LfSysuser sysuser = null;
        try {
            //当前登录操作员
            sysuser = loginSysuser;
        } catch (Exception e) {
            EmpExecutionContext.error(e,"群发历史或群发任务查询操作员信息异常！");
            tree = new StringBuffer("[]");
        }
        //判断当前登录操作员的权限，个人权限不能显示机构树
        if(sysuser.getPermissionType()==1) {
            tree = new StringBuffer("[]");
        }else {
            DepBiz depBiz = new DepBiz();
            List<LfDep> lfDeps;

            try {
                lfDeps = null;
                //10000企业查询
                if ((sysuser != null) && (sysuser.getCorpCode().equals("100000"))) {
                    if (depId == null) {
                        //设置查询条件
                        LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
                        LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();

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
                    LfDep lfDep = (LfDep)depBiz.getAllDepByUserIdAndCorpCode(userId,sysuser.getCorpCode()).get(0);
                    lfDeps.add(lfDep);
                } else {
                    //根据机构ID、企业编码查询出所有子机构
                    lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,sysuser.getCorpCode());
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
                    tree.append(",depId:'").append(lfDep.getDepId()+"'");
                    tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if(i != lfDeps.size()-1){
                        tree.append(",");
                    }
                }
                tree.append("]");

            } catch (Exception e) {
                //异常信息记录日志
                EmpExecutionContext.error(e,"群发历史或群发任务操作员树加载biz层异常！");
                tree = new StringBuffer("[]");
            }
        }
        //返回值
        return tree.toString();
    }

    /**
     * 获取操作员对象
     * @param request
     * @return
     */
    public LfSysuser getCurrenUser(HttpServletRequest request){
        try
        {
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if(loginSysuserObj == null) {
                return null;
            }

            return (LfSysuser)loginSysuserObj;

        }catch(Exception e) {
            EmpExecutionContext.error("SESSION获取操作员对象失败。");
            return null;
        }
    }
    /**
     * 获取通道号集合
     * @param corpCode 企业编码
     * @return 返回通道号动态bean集合
     */
    public List<DynaBean> getSpgateList(String corpCode) {
        MtTaskGenericDAO mtDao = new MtTaskGenericDAO();
        return mtDao.findSpgateList(corpCode);
    }
    /**
     * 获取发送账号集合
     * @param corpCode 企业编码
     * @return 发送账号集合（大写）
     */
    public List<String> getSpUserList(String corpCode) {
        if(corpCode == null || corpCode.trim().length() < 1) {
            EmpExecutionContext.error("获取企业发送账号，企业编码为空。");
            return null;
        }
        try {
            //发送账号（大写）
            List<String> spUserList = new ArrayList<String>();

            //多企业
            if(StaticValue.getCORPTYPE() == 1) {
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                //不是十万的企业。如果是100000则查询所有企业发送账号
                if(!"100000".equals(corpCode)) {
                    conditionMap.put("corpCode", corpCode);
                }

                List<LfSpDepBind> lfsp = empDao.findListByCondition(LfSpDepBind.class, conditionMap, null);
                if(lfsp == null || lfsp.size() < 1) {
                    return null;
                }

                for (LfSpDepBind aLfsp : lfsp) {
                    spUserList.add(aLfsp.getSpUser().toUpperCase());
                }
                return spUserList;
            }
            //单企业
            else {
                //条件map
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
                conditionMap.put("uid&>", "100001");
                //只查询短信发送账号
                conditionMap.put("accouttype", "1");
                conditionMap.put("userType", "0");

                //排序map
                LinkedHashMap<String, String> orderMap = new LinkedHashMap<String,String>();
                orderMap.put("userId",StaticValue.ASC);

                List<Userdata> usersp = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
                if(usersp == null || usersp.size() < 1) {
                    return null;
                }

                for (Userdata anUsersp : usersp) {
                    spUserList.add(anUsersp.getUserId().toUpperCase());
                }
                return spUserList;
            }
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "获取企业发送账号，异常。corpCode="+corpCode);
            return null;
        }
    }
    /**
     * 获取下行记录集合
     * @param conditionMap  查询条件集合
     * @param pageInfo 分页信息对象
     */
    public List<DynaBean> getMtRecords(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
        try {
            // 企业绑定的发送账户，多企业限制查询范围用到
            String spUsers = getCorpBindSpusers(conditionMap.get("lgcorpcode"));
            //sp账号条件
            conditionMap.put("spUsers", spUsers);
            //查询类型
            String recordType = conditionMap.get("recordType");
            //定义查询历史结果集
            List<DynaBean> mtTaskList;
            if("history".equals(recordType)) {
                // 查询历史记录
                //设置时间查询条件
                setTimeCondition(conditionMap);
                MtTaskHistoryDAO mtDao = new MtTaskHistoryDAO();
                mtTaskList = mtDao.findMtTasksHis(conditionMap, pageInfo);
            } else if("realTime".equals(recordType)) {
                // 查询实时记录
                MtRecordRealDAO mtDao = new MtRecordRealDAO();
                mtTaskList = mtDao.findMtTasksReal(conditionMap, pageInfo);
            } else {
                //类型传递错误
                mtTaskList = null;
            }
            return mtTaskList;
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "系统下行记录，获取下行记录，异常。");
            return null;
        }
    }
    /**
     * 获取企业绑定发送账号。多企业时会去获取企业绑定发送账号，单企业则会返回null
     * @param corpCode 企业编码
     * @return 返回企业绑定的发送账号，格式如sp1,sp2,sp3...。单企业直接返回null
     */
    private String getCorpBindSpusers(String corpCode) {
        //单企业则不需要拿绑定账号
        if(StaticValue.getCORPTYPE() != 1) {
            return null;
        }
        if(corpCode == null || corpCode.trim().length() < 1) {
            EmpExecutionContext.error("获取企业绑定发送账号，企业编码为空。");
            return null;
        }
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //不是十万的企业。如果是100000则查询所有企业发送账号
            if(!"100000".equals(corpCode)) {
                conditionMap.put("corpCode", corpCode);
            }

            // 短信账号
            List<LfSpDepBind> lfsp = empDao.findListByCondition(LfSpDepBind.class, conditionMap, null);
            if(lfsp == null || lfsp.size() < 1)
            {
                EmpExecutionContext.info("下行记录查询，企业未绑定SP账号。corpcode="+corpCode);
                return null;
            }

            StringBuffer bufpusers = new StringBuffer();
            for (int i = 0; i < lfsp.size(); i++) {
                bufpusers.append("'").append(lfsp.get(i).getSpUser().toUpperCase()).append("'");

                if(i < lfsp.size() - 1)
                {
                    bufpusers.append(",");
                }
            }
            return bufpusers.toString();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取企业绑定发送账号，异常。corpCode="+corpCode);
            return null;
        }
    }

    /**
     * 设置查询条件的时间
     * @param conditionMap 查询条件map集合
     */
    public void setTimeCondition(LinkedHashMap<String, String> conditionMap) {
        try {
            //开始时间
            String sendtime = conditionMap.get("sendtime");
            //结束时间
            String recvtime = conditionMap.get("recvtime");

            //查询日期格式化：yyyy-MM-dd HH:mm:ss
            SimpleDateFormat sdfSeachTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //查询时间段都为空
            if((sendtime == null || sendtime.length() == 0) && (recvtime == null || recvtime.length() == 0)) {
                //开始时间设置为当前月1号0时0分0秒
                Calendar caStartTime = Calendar.getInstance();
                caStartTime.set(Calendar.DAY_OF_MONTH, 1);//设置天
                caStartTime.set(Calendar.HOUR_OF_DAY, 0);//设置小时
                caStartTime.set(Calendar.MINUTE, 0);//设值分钟
                caStartTime.set(Calendar.SECOND, 0);
                caStartTime.set(Calendar.MILLISECOND, 0);
                sendtime = sdfSeachTime.format(caStartTime.getTime());

                //结束时间设置为当前月最后一天
                Calendar caEndTime = Calendar.getInstance();
                caEndTime.set(Calendar.DAY_OF_MONTH, caEndTime.getActualMaximum(Calendar.DAY_OF_MONTH));
                caEndTime.set(Calendar.HOUR_OF_DAY, 23);
                caEndTime.set(Calendar.MINUTE, 59);
                caEndTime.set(Calendar.SECOND, 59);
                caEndTime.set(Calendar.MILLISECOND, 999);
                recvtime = sdfSeachTime.format(caEndTime.getTime());
            } else if(sendtime == null || sendtime.length() == 0) {
                //没开始时间
                Date dateEndTime = sdfSeachTime.parse(recvtime);
                //开始时间设置为结束时间月的1号0时0分0秒
                Calendar caStartTime = Calendar.getInstance();
                caStartTime.setTimeInMillis(dateEndTime.getTime());
                caStartTime.set(Calendar.DAY_OF_MONTH, 1);
                caStartTime.set(Calendar.HOUR_OF_DAY, 0);
                caStartTime.set(Calendar.MINUTE, 0);
                caStartTime.set(Calendar.SECOND, 0);
                caStartTime.set(Calendar.MILLISECOND, 0);
                sendtime = sdfSeachTime.format(caStartTime.getTime());
            } else if(recvtime == null || recvtime.length() == 0) {
                //没结束时间
                Date dateStartTime = sdfSeachTime.parse(sendtime);
                //结束时间设置为开始时间月的最后一天
                Calendar caEndTime = Calendar.getInstance();
                caEndTime.setTimeInMillis(dateStartTime.getTime());
                caEndTime.set(Calendar.DAY_OF_MONTH, caEndTime.getActualMaximum(Calendar.DAY_OF_MONTH));
                caEndTime.set(Calendar.HOUR_OF_DAY, 23);
                caEndTime.set(Calendar.MINUTE, 59);
                caEndTime.set(Calendar.SECOND, 59);
                caEndTime.set(Calendar.MILLISECOND, 999);
                recvtime = sdfSeachTime.format(caEndTime.getTime());
            }
            else {
                //开始时间和结束时间都有，不用再设置
                return;
            }
            //开始时间
            conditionMap.put("sendtime", sendtime);
            //结束时间
            conditionMap.put("recvtime", recvtime);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "系统下行记录，设置下行历史记录查询条件，异常。");
        }
    }
    /**
     *
     * @description 获取有权限看的操作员编码
     * @param sysUser 操作员对象
     * @return 返回有权限看的操作员编码，格式为code1,code2；不需要考虑权限则返回空字符串，目前只有100000企业、admin和管辖范围是顶级机构不需要考虑权限；编码超过1000个、异常返回null
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-18 下午03:22:57
     */
    public String getPermissionUserCode(LfSysuser sysUser) {
        if("100000".equals(sysUser.getCorpCode()))
        {
            return "";
        }
        //如果是admin管理员，则默认查全部
        else if("admin".equals(sysUser.getUserName()))
        {
            return "";
        }
        //如果是个人权限，则只能查自己的。权限类型 1：个人权限  2：机构权限
        else if(sysUser.getPermissionType() == 1)
        {
            //返回当前操作员的编码
            return "'" + sysUser.getUserCode() + "'";
        }

        //机构权限，则需要查询出当前操作员可管辖的所有操作员。
        try
        {
            //如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码
            boolean domIsTopDep = checkDomIsTopDep(sysUser);
            if(domIsTopDep)
            {
                return "";
            }

            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("userCode", StaticValue.ASC);
            //查询有权限看的操作员
            List<LfSysuser> userList = empDao.findListBySymbolsCondition(sysUser.getUserId(), LfSysuser.class, null, orderbyMap);
            //没找到其他操作员，那就只能看他自己的
            if(userList == null || userList.size() < 1)
            {
                //返回当前操作员的编码
                return "'" + sysUser.getUserCode() + "'";
            }
            //操作员编码超过1000，则不使用in方式拼接查询
            if(userList.size() > 1000)
            {
                return null;
            }

            StringBuffer sbUserCode = new StringBuffer();
            for(int i = 0;i < userList.size(); i++)
            {
                sbUserCode.append("'").append(userList.get(i).getUserCode()).append("'");
                if(i < userList.size() - 1)
                {
                    sbUserCode.append(",");
                }
            }

            return sbUserCode.toString();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "系统下行记录，获取有权限看的操作员编码，异常"
                    + "。userCode=" + sysUser.getUserCode()
                    + ",userId=" + sysUser.getUserId()
                    + ",userName=" + sysUser.getUserName()
                    + ",corpCode=" + sysUser.getCorpCode()
            );
            //返回当前操作员的编码
            return null;
        }
    }
    /**
     *
     * @description 检查管辖机构是否就是企业顶级机构
     * @param sysUser 操作员对象
     * @return true：管辖的机构即为企业顶级机构；false：不是顶级机构，或者没能找到，或者异常。
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-18 下午02:56:42
     */
    private boolean checkDomIsTopDep(LfSysuser sysUser)
    {
        //如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码

        //获取企业最顶级机构
        LfDep topDep = getTopDep(sysUser.getCorpCode());
        //没能找到企业顶级机构
        if(topDep == null)
        {
            EmpExecutionContext.error("系统下行记录，检查管辖机构是否就是企业顶级机构，获取不到顶级机构对象。corpCode="+sysUser.getCorpCode());
            return false;
        }

        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userId", sysUser.getUserId().toString());
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("depId", StaticValue.ASC);
            List<LfDomination> domList = empDao.findListByCondition(LfDomination.class, conditionMap, orderbyMap);
            if(domList == null || domList.size() < 1)
            {
                return false;
            }

            for(LfDomination lfDom : domList)
            {
                //管辖的机构即为企业顶级机构
                if(lfDom.getDepId() == topDep.getDepId())
                {
                    return true;
                }
            }
            return false;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "系统下行记录，检查管辖机构是否就是企业顶级机构，异常。");
            return false;
        }
    }
    /**
     *
     * @description 获取企业顶级机构
     * @param corpCode 企业编码
     * @return 企业顶级机构对象，没找到或者异常则返回null
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-18 下午02:49:50
     */
    private LfDep getTopDep(String corpCode) {
        try
        {
            //企业顶级机构先从内存中拿
            if(topDepMap.get(corpCode) != null)
            {
                return topDepMap.get(corpCode);
            }

            //获取企业最顶级机构
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corpCode);
            //顶级机构
            conditionMap.put("depLevel", "1");
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("depId", StaticValue.ASC);
            List<LfDep> depList = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
            if(depList == null || depList.size() < 1)
            {
                EmpExecutionContext.error("系统下行记录，获取企业顶级机构，查询为空。corpCode="+corpCode);
                return null;
            }

            //企业顶级机构放到内存中
            topDepMap.put(corpCode, depList.get(0));

            return depList.get(0);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "系统下行记录，获取企业顶级机构，异常。");
            return null;
        }
    }
    /**
     *
     * @description 获取有权限看的SP账号
     * @param sysUser 操作员对象
     * @return 返回有权限看的SP账号，格式为spuser1,spuser2；不需要考虑权限则返回空字符串，目前只有100000企业、admin和管辖范围是顶级机构不需要考虑权限；编码超过1000个、异常返回null
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-18 下午03:22:57
     */
    public String getPermissionSpuserMtpri(LfSysuser sysUser)
    {
        if("100000".equals(sysUser.getCorpCode()))
        {
            return "";
        }
        //如果是admin管理员，则默认查全部
        else if("admin".equals(sysUser.getUserName()))
        {
            return "";
        }
        //机构权限，则需要查询出当前操作员可管辖的所有操作员。
        try
        {
            //如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码
            boolean domIsTopDep = checkDomIsTopDep(sysUser);
            if(domIsTopDep)
            {
                return "";
            }
            LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
            conditionmap.put("userid", sysUser.getUserId()+"");
            conditionmap.put("corpcode", sysUser.getCorpCode());
            //查询有权限看的sp账号
            List<LfMtPri> mtpris = empDao.findListByCondition(LfMtPri.class, conditionmap, null);
            //没找到其他操作员，那就只能看他自己的
            if(mtpris == null || mtpris.size() < 1)
            {
                //返回当前操作员的编码
                return "";
            }
            //操作员编码超过1000，则不使用in方式拼接查询
            if(mtpris.size() > 1000)
            {
                return null;
            }

            StringBuffer spusers = new StringBuffer();
            for(int i = 0;i < mtpris.size(); i++)
            {
                spusers.append("'").append(mtpris.get(i).getSpuserid()).append("'");
                if(i < mtpris.size() - 1)
                {
                    spusers.append(",");
                }
            }

            return spusers.toString();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "系统下行记录，获取有权限看的SP账号，异常"
                    + "。userCode=" + sysUser.getUserCode()
                    + ",userId=" + sysUser.getUserId()
                    + ",userName=" + sysUser.getUserName()
                    + ",corpCode=" + sysUser.getCorpCode()
            );
            //返回当前操作员的编码
            return null;
        }
    }
    /**
     *
     * 获取错误码说明map
     * @param mtTaskList 下行记录对象集合
     * @param corpCode 企业编码
     */
    public Map<String,String> getErrCodeDisMap(List<DynaBean> mtTaskList, String corpCode) {
        if(mtTaskList == null || mtTaskList.size() < 1)
        {
            EmpExecutionContext.error("系统下行记录，获取错误码说明map，传入记录为空。");
            return null;
        }
        if(corpCode == null || corpCode.length() < 1)
        {
            EmpExecutionContext.error("系统下行记录，获取错误码说明map，传入企业编码为空。");
            return null;
        }
        try {
            //错误状态码map，key为错误状态码，value为错误状态码
            Map<String,String> errcodeMap = new HashMap<String,String>();

            //状态码
            String stateCode;
            for(DynaBean bean : mtTaskList) {
                if(bean.get("errorcode") == null) {
                    continue;
                }
                stateCode = bean.get("errorcode").toString().trim();
                //成功的跳过
                if("DELIVRD".equals(stateCode) || "0".equals(stateCode)) {
                    continue;
                }
                //把错误的状态码放入map中
                errcodeMap.put(stateCode, stateCode);
            }
            //没错误状态码
            if(errcodeMap.size() < 1) {
                return null;
            }

            StringBuffer sbErrCode = new StringBuffer();
            for(String key : errcodeMap.keySet()) {
                sbErrCode.append("'").append(key).append("'").append(",");
            }
            //删除最后的,
            if(sbErrCode.length() > 0 && sbErrCode.lastIndexOf(",") == sbErrCode.length()-1) {
                sbErrCode.deleteCharAt(sbErrCode.length() - 1);
            }

            //错误码说明map，key为错误码，value为说明
            Map<String,String> errCodeDisMap = new HashMap<String,String>();

            MtTaskGenericDAO mtDao = new MtTaskGenericDAO();
            List<DynaBean> beanList = mtDao.getErrCodeDis(sbErrCode.toString(), corpCode);
            if(beanList == null || beanList.size() < 1) {
                return null;
            }

            for(DynaBean bean : beanList) {
                if(bean.get("state_code") == null || bean.get("state_des") == null) {
                    continue;
                }
                errCodeDisMap.put(bean.get("state_code").toString(), bean.get("state_des").toString());
            }
            return errCodeDisMap;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "系统下行记录，获取错误码说明map，异常。");
            return null;
        }
    }

    /**
     * 根据TaskId获取访问人数与访问次数
     * @param taskId
     * @return
     */
    public HashMap<String, Integer> getVisitStatistByTaskId(Long taskId,Timestamp sendTime) {
        if(taskId == null){
            EmpExecutionContext.error("批次访问查询，方法：getVisitStatistByTaskId(Long taskId)。传入参数为空，taskId为null。");
            return new HashMap<String, Integer>();
        }
        return vstDetailDao.findVisitCountBySQL(taskId,sendTime);
    }

    public LfMttask getLfMttaskByTaskId(Long taskId) throws Exception{
        if(taskId == null){
            EmpExecutionContext.error("批次访问查询，方法：getLfMttaskByTaskId(Long taskId)。传入参数为空，taskId为null。");
            return null;
        }
        LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("taskId",taskId.toString());
        List<LfMttask> lfMttasks = empDao.findListByCondition(LfMttask.class, conditionMap, null);
        if(lfMttasks == null || lfMttasks.size() == 0){
            EmpExecutionContext.error("企业短链。报表查询异常，根据TaskId查询LfMttask结果为null");
            return null;
        }
        return lfMttasks.get(0);
    }

    /**
     * 获取所有的批次访问详情，包括未访问的
     * @param vstDetailVo
     * @param pageInfo
     * @return
     */
    public List<VstDetailVo> getAllVstDetail(VstDetailVo vstDetailVo,PageInfo pageInfo) {
        List<VstDetailVo> vstDetailList = new ArrayList<VstDetailVo>();
        if(vstDetailVo == null){
            EmpExecutionContext.error("批次访问查询，方法：getAllVstDetail(VstDetailVo vstDetailVo,PageInfo pageInfo)。传入参数为空，vstDetailVo为null。");
            return null;
        }
        try {
            vstDetailList = vstDetailDao.getAllVstDetail(vstDetailVo,pageInfo);
            vstDetailList = completeVstDetailVo(vstDetailList);
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业短链报表查询-批次访问统计-号码详情查询异常");
        }
        return vstDetailList;
    }

    private List<VstDetailVo> completeVstDetailVo(List<VstDetailVo> vstDetailList){
        if(vstDetailList != null){
            for(VstDetailVo detailVo:vstDetailList){
                //根据有没有访问时间及IP判断是否访问
                Boolean hasVisited = detailVo.getVsttm() != null && detailVo.getSrcAddress() != null;
                if(hasVisited){
                    //如果不包含端口，则直接传
                    if(detailVo.getSrcAddress().contains(":")){
                        detailVo.setVisitIP(detailVo.getSrcAddress().substring(0,detailVo.getSrcAddress().lastIndexOf(":")));
                    }else {
                        detailVo.setVisitIP(detailVo.getSrcAddress());
                    }
                    String areName = queryAreaName(detailVo.getMobileArea());
                    detailVo.setAreaName(areName);
                }
                detailVo.setVisitStatus(hasVisited ? "1":"0");
            }
            return vstDetailList;
        }
        return new ArrayList<VstDetailVo>();
    }

    /**
     * 根据条件筛选号码详情记录
     * @param detailVo
     * @param pageInfo
     * @return
     */
    public List<VstDetailVo> findPhoneNumDetailByCondition(VstDetailVo detailVo, PageInfo pageInfo) {
        List<VstDetailVo> vstDetailList = new ArrayList<VstDetailVo>();
        try {
            //如果查询条件中包含已访问，区域，访问IP，访问时间则只查询详单表，不查询未访问
            if ((detailVo.getVisitStatus() != null && !"".equals(detailVo.getVisitStatus()) && "1".equals(detailVo.getVisitStatus()))
                    || (detailVo.getVisitIP() != null && !"".equals(detailVo.getVisitIP()))
                    || (detailVo.getMobileArea() != null && !"".equals(detailVo.getMobileArea()))
                    || (detailVo.getVsttm() != null && !"".equals(detailVo.getVsttm()))
                    || (detailVo.getEndTime() != null && !"".equals(detailVo.getEndTime()))) {
                vstDetailList = vstDetailDao.findVisitedDetail(detailVo, pageInfo);
                vstDetailList = completeVstDetailVo(vstDetailList);
            } else {
                //需要查询未访问
                vstDetailList = vstDetailDao.getAllVstDetail(detailVo,pageInfo);
                vstDetailList = completeVstDetailVo(vstDetailList);
            }
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业短链报表查询-批次发送统计-号码详情，调用Dao层查询方法异常");
        }
        return vstDetailList;
    }
    //TODO 查询IP对应地区
    private String queryAreaName(String mobileArea) {
        if(mobileArea != null && "".equals(mobileArea)){
            return "";
        }
        return "";
    }

    public List<SendDetailMttaskVo> findAllSendMttask(SendDetailMttaskVo detailMttaskVo,PageInfo pageInfo) throws Exception {
        List<SendDetailMttaskVo> mttaskVoList = new ArrayList<SendDetailMttaskVo>();
        MtTaskGenericDAO mtTaskGenericDAO = new MtTaskGenericDAO();
        mttaskVoList = mtTaskGenericDAO.findAllSendMttask(detailMttaskVo,pageInfo);
        return mttaskVoList;
    }

    public List<ReplyDetailVo> findAllReplyDetail(ReplyDetailVo replyDetailVo, PageInfo pageInfo) throws Exception{
        List<ReplyDetailVo> replyDetailVos = new ArrayList<ReplyDetailVo>();
        MtTaskGenericDAO mtTaskGenericDAO = new MtTaskGenericDAO();
        replyDetailVos = mtTaskGenericDAO.findAllReplyDetail(replyDetailVo,pageInfo);
        return replyDetailVos;
    }
}
