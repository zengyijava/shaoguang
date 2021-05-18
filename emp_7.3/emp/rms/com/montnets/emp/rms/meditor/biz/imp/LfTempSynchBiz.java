package com.montnets.emp.rms.meditor.biz.imp;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.meditor.biz.ILfTempSynchBiz;
import com.montnets.emp.rms.meditor.dao.LfTempSynchBizDao;
import com.montnets.emp.rms.meditor.dao.imp.LfTempSynchBizDaoImp;
import com.montnets.emp.rms.meditor.entity.LfTempSynch;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

public class LfTempSynchBiz extends BaseBiz implements ILfTempSynchBiz {

    LfTempSynchBizDao lfTempSynchBizDao = new LfTempSynchBizDaoImp();

    @Override
    public boolean syncTempInfo(Long spTemplateid,Integer isMaterial) {
        if (null == spTemplateid) {
            return false;
        }
        //根据审核id查询同步信息
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("spTemplateid", String.valueOf(spTemplateid));
        List<LfTempSynch> lfTempSynches = null;
        try {
            lfTempSynches = empDao.findListByCondition(LfTempSynch.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询模板同步状态异常");
        }

        // 若表中没有此模板的同步信息，则插入一条该模板信息
        if (lfTempSynches==null||lfTempSynches.size() == 0) {
            LfTempSynch lfTempSynch = new LfTempSynch();
            lfTempSynch.setSpTemplateid(spTemplateid);
            lfTempSynch.setCount(0);
            lfTempSynch.setSynstatus(0);
            lfTempSynch.setCause(" ");
            lfTempSynch.setIsMaterial(isMaterial);
            try {
                empDao.saveObjectReturnID(lfTempSynch);
                return false;
            } catch (Exception e) {
                EmpExecutionContext.error(e, "保存模板同步信息异常");
            }

        } else {//若已有数据，则判断重试次数。若没达到重试次数，则重试次数+1
            LfTempSynch lfTempSynch = lfTempSynches.get(0);//审核id在表中有唯一性约束，只可能有一条数据
            Integer sycCounts = SystemGlobals.getIntValue("montnets.rcos.template.syn.counts", 3);
            if (lfTempSynch.getCount() >= sycCounts) {
                return true;
            }
            lfTempSynch.setCount(lfTempSynch.getCount() + 1);
            lfTempSynch.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            try {
                empDao.update(lfTempSynch);
                return false;
            } catch (Exception e) {
                EmpExecutionContext.error(e, "更新同步状态表失败");
            }
        }
        return true;
    }

    @Override
    public boolean changeSynStatus(Long spTemplateid, Integer synStatus) {
        if (null == spTemplateid || null == synStatus) {
            return false;
        }
        //根据审核id查询同步信息
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("spTemplateid", String.valueOf(spTemplateid));
        List<LfTempSynch> lfTempSynches = null;
        try {
            lfTempSynches = empDao.findListByCondition(LfTempSynch.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询模板同步状态异常");
        }
        if (lfTempSynches==null||lfTempSynches.size() <= 0) {
            return false;
        }
        LfTempSynch lfTempSynch = lfTempSynches.get(0);
        lfTempSynch.setSynstatus(synStatus);
        lfTempSynch.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        try {
            return empDao.update(lfTempSynch);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "更新模板同步信息异常");
        }
        return false;
    }

    @Override
    public List<LfTempSynch> findSynFailTemp(int isMaterial) {
        List<LfTempSynch> lfTempSynches = lfTempSynchBizDao.findSynFailTemp(isMaterial);
        return lfTempSynches;
    }

    @Override
    public long findMaxFailTempId(String isMaterial,Integer synCounts) {
        //根据审核id查询同步信息
        LfTempSynch lfTempSynch = null;
        try {
            return lfTempSynchBizDao.findMaxFailTempId(isMaterial,synCounts);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常！");
        }
        return 0;
    }

    //解析模板资源出错处理
    @Override
    public void  analysisTempError(Long spTemplateid){
        //根据审核id查询同步信息
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("spTemplateid", String.valueOf(spTemplateid));
        conditionMap.put("isMaterial","0");
        List<LfTempSynch> lfTempSynches = null;
        try {
            lfTempSynches = empDao.findListByCondition(LfTempSynch.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询模板同步状态异常");
        }

        // 若表中没有此模板的同步信息，则插入一条该模板信息
        if (lfTempSynches==null||lfTempSynches.size() == 0) {
            LfTempSynch lfTempSynch = new LfTempSynch();
            lfTempSynch.setSpTemplateid(spTemplateid);
            lfTempSynch.setCount(0);
            lfTempSynch.setSynstatus(0);
            lfTempSynch.setCause(" ");
            lfTempSynch.setIsMaterial(0);
            try {
                empDao.saveObjectReturnID(lfTempSynch);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "保存模板同步信息异常");
            }

        } else {//若已有数据，则判断重试次数。若没达到重试次数，则重试次数+1
            LfTempSynch lfTempSynch = lfTempSynches.get(0);//审核id在表中有唯一性约束，只可能有一条数据
            Integer sycCounts = SystemGlobals.getIntValue("montnets.rcos.template.syn.counts", 3);
            if (lfTempSynch.getCount() <sycCounts) {
                lfTempSynch.setCount(lfTempSynch.getCount() + 1);
                lfTempSynch.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            }
            try {
                empDao.update(lfTempSynch);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "更新同步状态表失败");
            }
        }
    }
    @Override
    public Long syncTempBeforeDeal(Long spTemplateid){
        Long spTemplateids=spTemplateid;
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
        conditionMap.put("isMaterial","0");
        orderMap.put("spTemplateid","desc");
        LfTempSynch lfTempSynche = null;

        try {
            List<LfTempSynch> lfTempSyncheList = empDao.findListByCondition(LfTempSynch.class, conditionMap, orderMap);
            if(lfTempSyncheList != null && lfTempSyncheList.size() > 0){
                //获取同步来最新的一条记录进行下一次的同步操作
                lfTempSynche = lfTempSyncheList.get(0);
                if(lfTempSynche!=null){
                    Long templateids= lfTempSynche.getSpTemplateid();
                    if(spTemplateids.equals(templateids)){
                        spTemplateids=spTemplateids;
                    }
                    else  if(spTemplateids>templateids){
                        spTemplateids=spTemplateids;
                    }
                    else  if(spTemplateids<templateids){
                        spTemplateids=templateids;
                    }
                }
            }
        }catch (Exception e) {
            EmpExecutionContext.error(e, "查询模板同步表中最大的模板号异常");
        }
        return spTemplateids;
    }
    @Override
    public void syncTempSucceedDeal(String spTemplateId){
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("spTemplateid", String.valueOf(spTemplateId));
        conditionMap.put("isMaterial","0");
        List<LfTempSynch> lfTempSynches = null;
        try {
            lfTempSynches = empDao.findListByCondition(LfTempSynch.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询模板同步状态异常");
        }
        // 若表中没有此模板的同步信息，表示第一次同步就成功的数据，则插入一条该模板信息
        if (lfTempSynches==null||lfTempSynches.size() == 0) {
            LfTempSynch lfTempSynch = new LfTempSynch();
            lfTempSynch.setSpTemplateid(Long.parseLong(spTemplateId));
            lfTempSynch.setCount(0);
            lfTempSynch.setSynstatus(1);
            lfTempSynch.setCause(" ");
            lfTempSynch.setIsMaterial(0);
            try {
                empDao.saveObjectReturnID(lfTempSynch);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "保存模板同步信息异常");
            }
        }else {//若已有数据，则表示不是第一次就同步成功的数据
            LfTempSynch lfTempSynch = lfTempSynches.get(0);//审核id在表中有唯一性约束，只可能有一条数据
            lfTempSynch.setSynstatus(1);
            lfTempSynch.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            try {
                empDao.update(lfTempSynch);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "更新同步状态表失败");
            }
        }

    }
}
