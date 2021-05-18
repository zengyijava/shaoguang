package com.montnets.emp.rms.commontempl.biz;

import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.rms.commontempl.dao.CommonTemplateDao;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.commontempl.entity.LfTemplate;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.util.PageInfo;

public class CommonTemplateBiz extends BaseBiz {

	public List<LfTemplateVo> getCommonTempalateList(LfTemplateVo lfTemplate,PageInfo pageInfo,String key ) {
		List<LfTemplateVo> templateList = new CommonTemplateDao().getCommonTempalateList(lfTemplate, pageInfo,key);
		return templateList; 
	}
	public List<LfIndustryUse> getIndustryUseList(LfIndustryUse lfIndustryUse,
			PageInfo pageInfo) {
		return  new CommonTemplateDao().getIndustryUseList(lfIndustryUse, pageInfo );
	}
	
	public boolean addIndustryOrUse(LfIndustryUse lfIndustryUse) {
		return  new CommonTemplateDao().addIndustryOrUse(lfIndustryUse);
	}
	public boolean updateIndustryOrUse(LfIndustryUse lfIndustryUse) {
		return  new CommonTemplateDao().updateIndustryOrUse(lfIndustryUse);
	}
	public boolean updateUseCount(LfTemplate lfTemplate) {
		return  new CommonTemplateDao().updateUseCount(lfTemplate);
	}

}
