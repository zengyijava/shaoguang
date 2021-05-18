package com.montnets.emp.rms.meditor.dao;

import com.montnets.emp.rms.vo.CorpUserVo;

import java.util.List;

/**
 * @ProjectName: EMP7.2$
 * @Package: com.montnets.emp.rms.meditor.dao$
 * @ClassName: SynRcosTemplateDao$
 * @Description:  同步RCOS平台模板DAO
 * @Author: xuty
 * @CreateDate: 2018/11/21$ 8:49$
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/21$ 8:49$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public interface SynRcosTemplateDao  {
    List<CorpUserVo> getCorps();
}
