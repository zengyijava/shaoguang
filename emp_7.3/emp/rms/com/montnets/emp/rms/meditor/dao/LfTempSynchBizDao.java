package com.montnets.emp.rms.meditor.dao;

import com.montnets.emp.rms.meditor.entity.LfTempSynch;

import java.util.List;

public interface LfTempSynchBizDao {
    /**
     * 查询满足下列条件的模板同步信息:
     *                      1. 同步失败
     *                      2. 同步次数未达到上限
     *                      3. 距上次同步时间 大于 设定的同步时间间隔
     * @return
     */
    List<LfTempSynch> findSynFailTemp(int isMaterial);

    /**
     * 获取当前同步到的最大模板ID
     * @param isMaterial
     * @return
     */
    long findMaxFailTempId(String isMaterial,Integer synCounts);
}
