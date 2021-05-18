package com.montnets.emp.rms.templmanage.listener;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.dao.UserDataDAO;
import com.montnets.emp.rms.meditor.biz.SynTemplateBiz;
import com.montnets.emp.rms.meditor.biz.imp.SynTemplateBizImp;
import com.montnets.emp.rms.rmsapi.biz.RMSApiBiz;
import com.montnets.emp.rms.rmsapi.biz.impl.IRMSApiBiz;
import com.montnets.emp.rms.templmanage.dao.MbglTemplateDAO;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.rms.vo.UserDataVO;
import com.montnets.emp.util.StringUtils;

/**
 * @author xuty
 * @ClassName: TemplateCheckThread
 * @Description: 模板状态读取线程类
 * @date 2018-1-12 上午10:07:35
 */
public class TemplateCheckThread extends Thread {

    private boolean isExit = false;
    private MbglTemplateDAO ltDao = new MbglTemplateDAO();
    private final SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
    //定时间隔
    private int internal = 5 * 60 * 1000; //默认5分钟

    public TemplateCheckThread(int internal) {
        this.internal = internal;
        setName("模板状态读取线程");
    }

    public void run() {
        // 未进行审核的模板id
        while (!isExit) {
            try {
                Map<String, String> rMap = ltDao.getNocheckTemlateList();
                if (rMap != null) {
                    for (Entry<String, String> en : rMap.entrySet()) {
                        String tmids = en.getValue();
                        String loginOrgCode = en.getKey();
                        if (null != tmids && !tmids.equals("")) {
                            if (tmids.endsWith(",")) {
                                tmids = tmids.substring(0, tmids.length() - 1);
                            }
                        } else {
                            continue;
                        }
                        RMSApiBiz rmsBiz = new IRMSApiBiz();
                        // 获取审核中心返回的模板ID对应的审核状态
                        UserDataDAO userDataDAO = new UserDataDAO();
                        UserDataVO userData = userDataDAO.getSPUser(loginOrgCode);
                        if (null != userData) {
                            Map<String, Object> tmStatusMap = rmsBiz.queryTemplateStatus(userData.getUserId().toUpperCase(), userData.getPassWord(), tmids.toString());
                            // 0 表示请求成功
                            if ("0".equals(tmStatusMap.get("result"))) {
                                LfTemplateVo vo = new LfTemplateVo();
                                @SuppressWarnings("unchecked")
                                List<Map<String, String>> list = (List<Map<String, String>>) tmStatusMap.get("status");
                                if (list != null) {
                                    for (int i = 0; i < list.size(); i++) {
                                        Map<String, String> map = list.get(i);
                                        vo.setSptemplid(Long.parseLong(map.get("tmplid")));
                                        vo.setAuditstatus(parseGwStatus2Audistatus(Integer.parseInt(map.get("status"))));
                                        String spTemplateId = map.get("tmplid");
                                        //3-RCOS同步的企业侧模板
                                        LfTemplate template = synTemplateBiz.getTemplate(spTemplateId, "");
                                        //RCOS平台同步的模板状态启用
                                        long rcosTmpState = 1L;
                                        //EMP模板状态 0-禁用，1-启用，2-草稿，3-删除
                                        long tmState = 1L;
                                        //RCOS企业侧定制模板,3-禁用，4-请求模板被删除或无权查看
                                        if (null != template && ("3".equals(map.get("status")))) {
                                            //逻辑删除从RCOS侧同步过来的企业侧定制场景
                                            //禁用
                                            rcosTmpState = 0L;
                                            tmState = 0L;
                                            deleteRcosEcTemplate(spTemplateId,rcosTmpState,tmState);
                                        } else if(null != template && "4".equals(map.get("status"))){
                                            //删除
                                            rcosTmpState = 2L;
                                            tmState = 3L;
                                            deleteRcosEcTemplate(spTemplateId,rcosTmpState,tmState);
                                        }else {//企业自己建的模板
                                            //更新审核中心返回的模板ID对应的审核状态
                                            deleteRcosEcTemplate(spTemplateId, rcosTmpState,tmState);
                                        }
                                        int exeCount = 0;
                                        while (!ltDao.updateTemplateStatus(vo) && exeCount < 3) {
                                            Thread.sleep(1000L);
                                            exeCount++;
                                            EmpExecutionContext.error("更新LF_TEMPLATE 表出错，TM_CODE:" + vo.getTmCode());

                                        }

                                    }
                                }
                            } else {
                                EmpExecutionContext.info("调用网关查询模板返回出错,ID:" + tmids + ",错误码：" + tmStatusMap.get("result"));
                            }
                        } else {
                            EmpExecutionContext.info("没有发送网关的SP账号");
                        }
                    }
                }

            } catch (Exception e) {
                EmpExecutionContext.error("更新LF_TEMPLATE 表出错");
            }

            // 每次间隔多长时间更新状态，时间可配
            try {
                Thread.sleep(internal);
            } catch (InterruptedException e) {
                EmpExecutionContext.error("线程暂停出行异常" + e.toString());
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 网关审核状态与EMP审核状态转换
     *
     * @param status
     * @return
     */
    public int parseGwStatus2Audistatus(int status) {
        switch (status) {
            //审核通过
            case 0:
                return 1;
            //审核中
            case 1:
                return 3;
            //审核不通过
            case 2:
                return 2;
            //禁用
            case 3:
                return 4;
            //模板已被删除或模板不存在
            case 4:
                return 5;
            default:
                return -1;
        }

    }

    /**
     * 删除RCOS企业侧已删除的模板
     * @param spTemplateId RSC 返回的模板ID
     * @param rcosTmpState RCOS同步过来的模板状态 0-禁用，1-启用
     *
     */
    public void deleteRcosEcTemplate(String spTemplateId,long rcosTmpState,long tmState) {
        if(StringUtils.isNotEmpty(spTemplateId)){
            synTemplateBiz.deleteTemplate(spTemplateId,rcosTmpState,tmState);
        }
    }
}
