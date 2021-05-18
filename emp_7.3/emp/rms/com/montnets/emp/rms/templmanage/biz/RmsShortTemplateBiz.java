package com.montnets.emp.rms.templmanage.biz;

import java.util.List;

import com.montnets.emp.rms.templmanage.dao.RmsShortTemplateDao;
import com.montnets.emp.rms.vo.LfShortTemplateVo;
import com.montnets.emp.util.PageInfo;

public class RmsShortTemplateBiz{
	private RmsShortTemplateDao rstDao = new RmsShortTemplateDao();
		
	public List<LfShortTemplateVo> getLfShortTemplate(PageInfo pageInfo,LfShortTemplateVo bean) throws Exception {
		return rstDao.getLfShortTemplate(pageInfo,bean);
	}
	public List<LfShortTemplateVo> getLfShortTemList(LfShortTemplateVo bean) throws Exception {
		return rstDao.getLfShortTempList(bean);
	}
	public boolean deleteShortTemp(LfShortTemplateVo bean) {
		return rstDao.deleteShortTemp(bean);
	}

	public boolean addShortTemp(LfShortTemplateVo bean) {
		return rstDao.addShortTemp(bean);
	}

	public LfShortTemplateVo getLfShortTemplate(LfShortTemplateVo bean) {
		return rstDao.getLfShortTemplate(bean);
	}

	public int getNumber(LfShortTemplateVo bean) {
		return rstDao.getNumber(bean);
	}

	public String getPrivilegeId(String menusite,LfShortTemplateVo bean) {
		return rstDao.getPrivilegeId(menusite,bean);
	}

	public boolean updateLfTemplate(long id,int param) {
		return rstDao.updateLfTemplate(id,param);
	}

}
