package com.montnets.emp.rms.meditor.thread;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.dao.UserDataDAO;
import com.montnets.emp.rms.meditor.biz.SynTemplateBiz;
import com.montnets.emp.rms.meditor.biz.imp.SynTemplateBizImp;
import com.montnets.emp.rms.rmsapi.biz.impl.IRMSApiBiz;
import com.montnets.emp.rms.vo.UserDataVO;
import com.montnets.emp.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * RCOS 公共模板状态同步线程
 */
public class SynRcosTemplateStatusThread implements Runnable {
    private IRMSApiBiz irmsApiBiz = new IRMSApiBiz();
    private SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
    //模板状态同步时间间隔
    private final int INTERNAL = SystemGlobals.getIntValue("emp.rms.templateInternal", 3 * 60);

    @Override
    public void run() {
        /*try {
            while(true){
                //获取账号
                IUserDataDAO userDataDAO = new UserDataDAO();
                UserDataVO userData = userDataDAO.getCommonSPUser();
                if(null != userData){
                    String userId = userData.getUserId();
                    String pwd = userData.getPassWord();
                    //获取当前模板记录中从RCOS平台同步下来的公共模板
                    String corpCode = "100000";
                    if(StaticValue.getCORPTYPE() == 0){//标准版
                        corpCode  = "100001";
                    }
                    String ids = getRcosCommonTemplateIds(corpCode);
                    if(StringUtils.isNotEmpty(ids)){
                        synTemplateStatus(userId,pwd,ids);
                    }
                }else{
                    EmpExecutionContext.error("没有可同步RCOS公共模板状态的账号！！！");
                    Thread.sleep(INTERNAL*1000L);//没有账号，休息一分钟，等等别人配好账号再同步
                }
                //同步一次状态休息
                Thread.sleep(INTERNAL*1000L);//没有账号，休息一分钟，等等别人配好账号再同步
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"RCOS 公共模板状态同步线程出现异常！");
        }*/

        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    UserDataDAO userDataDAO = new UserDataDAO();
                    UserDataVO userData = userDataDAO.getCommonSPUser();
                    if (null != userData) {
                        String userId = userData.getUserId();
                        String pwd = userData.getPassWord();
                        //获取当前模板记录中从RCOS平台同步下来的公共模板
                        String corpCode = "100000";
                        if (StaticValue.getCORPTYPE() == 0) {//标准版
                            corpCode = "100001";
                        }
                        String ids = getRcosCommonTemplateIds(corpCode);
                        if (StringUtils.isNotEmpty(ids)) {
                            synTemplateStatus(userId, pwd, ids);
                        }
                    } else {
                        EmpExecutionContext.error("没有可同步RCOS公共模板状态的账号！！！");
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "RCOS 公共模板状态同步线程出现异常！");
                }
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(runnable, 0, INTERNAL, TimeUnit.SECONDS);

    }

    /**
     * 获取从RCOS平台同步的公共模板ID
     *
     * @return 模板ID，多个用英文逗号分隔
     */
    public String getRcosCommonTemplateIds(String corpCode) {
        StringBuilder ids = new StringBuilder();
        List<LfTemplate> lfTemplates = synTemplateBiz.getCommonTemplateFromRcos(corpCode);
        for (LfTemplate lfTemplate : lfTemplates) {
            if (null != lfTemplate.getSptemplid()) {
                ids.append(lfTemplate.getSptemplid()).append(",");
            }
        }
        //去掉最后一个逗号
        if (StringUtils.isNotEmpty(ids.toString())) {
            return ids.substring(0, ids.lastIndexOf(","));
        }
        return "";
    }

    /**
     * 获取模板同步状态
     */
    public void synTemplateStatus(String userId, String pwd, String tempIds) {
        try {
            Map<String, Object> statusMap = irmsApiBiz.queryTemplateStatusFromRCOS(userId, pwd, tempIds);
            if (null != statusMap && statusMap.size() > 0) {
                //请求成功
                if (null != statusMap.get("result") && "0".equals(String.valueOf(statusMap.get("result")))) {
                    List<Map<String, String>> statusArray = (List<Map<String, String>>) statusMap.get("status");
                    for (int i = 0; i < statusArray.size(); i++) {
                        Map<String, String> tempMap = statusArray.get(i);
                        String spTemplateId = tempMap.get("tmplid");
                        // status 0-模板不存在，1-模板存在
                        int status = Integer.parseInt(tempMap.get("status"));
                        //0-禁用，1-启用,2-删除
                        long rcosTmpState = status == 1 ? 1L : 2L;
                        //0-禁用，1-启用,2-草稿，3-删除
                        //公共模板禁用和删除的模板在EMP这边的状态都为删除
                        long tmState = status == 1 ? 1L : 3L;
                        deleteTemplate(spTemplateId, rcosTmpState, tmState);
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "同步RCOS平台创建的公共模板状态出现异常！");
        }
    }

    /**
     * 逻辑删除本地RCOS 平台已经删除的模板记录
     *
     * @param spTemplateId 模板ID
     * @param tmState      模板状态
     */
    public void deleteTemplate(String spTemplateId, long rcosTmpState, long tmState) {
        if (StringUtils.isNotEmpty(spTemplateId)) {
            synTemplateBiz.deleteTemplate(spTemplateId, rcosTmpState, tmState);
        }
    }
}
