package com.montnets.emp.rms.meditor.biz;

import com.montnets.emp.rms.meditor.entity.LfTempSynch;

import java.util.List;

public interface ILfTempSynchBiz {

    /**
     * 模板同步状态信息入库
     * @param spTemplateid 模板审核id
     * @return true 已同步或达到同步次数上限; false 未同步或未达到同步次数上限
     */
    boolean syncTempInfo(Long spTemplateid,Integer isMaterial);

    /**
     * 修改同步状态
     * @param spTemplateid 模板审核id
     * @param synStatus 0 失败 ；1 成功
     * @return true 修改成功; false 失败
     */
    boolean changeSynStatus(Long spTemplateid,Integer synStatus);

    /**
     * 查询：1 未同步成功 2 同步次数未达到上限 3 达到重试间隔时间
     * @return
     */
    List<LfTempSynch> findSynFailTemp(int isMaterial);

    long findMaxFailTempId(String isMaterial,Integer synCounts);

    //解析模板资源出错处理
    void analysisTempError(Long spTemplateid);

    //下次同步时先判断模板是否同步成功，成功则同步下个模板，不成功判断试错次数超过三次则跳过同步下个模板
    Long  syncTempBeforeDeal(Long spTemplateid);


    //模板同步成功后修改状态为1
    void syncTempSucceedDeal(String spTemplateid);
}
