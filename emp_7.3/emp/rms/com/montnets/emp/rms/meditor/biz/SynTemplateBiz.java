package com.montnets.emp.rms.meditor.biz;

import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.meditor.entity.LfSubTemplate;
import com.montnets.emp.rms.meditor.entity.LfTempContent;
import com.montnets.emp.rms.meditor.entity.LfTempParam;
import com.montnets.emp.rms.vo.CorpUserVo;

import java.util.List;

/**
 * @ProjectName: (TG)EMP7.2$
 * @Package: com.montnets.emp.rms.meditor.biz.imp$
 * @ClassName: SynTemplateBiz$
 * @Description: java类作用描述
 * @Author: xuty
 * @CreateDate: 2018/11/1$ 10:28$
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/1$ 10:28$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public interface SynTemplateBiz{
    LfTemplate getTemplateFromRcos(String corpCode);
    LfTemplate getEcTemplateFromRcos(String corpCode);
    long addTemplate(LfTemplate lfTemplate);
    boolean addLfSubTemplate(LfSubTemplate lfSubTemplate);
    boolean addLfTempParam(LfTempParam lfTempParam);
    boolean addLfTempContent(LfTempContent lfTempContent);
    LfTemplate getTemplate(String spTemplateId,String source);
    LfTemplate getTemplateByTmid(String tmId);
    boolean updateTemplate(LfTemplate lfTemplate);
    void deleteTemplate(String sptempId,long rcosTmpState,long tmState);
    List<CorpUserVo> getCorps();
    List<LfTemplate> getCommonTemplateFromRcos(String corpCode);
}
