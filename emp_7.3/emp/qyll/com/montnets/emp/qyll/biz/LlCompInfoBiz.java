package com.montnets.emp.qyll.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.dao.LlCompInfoDao;
import com.montnets.emp.qyll.vo.LlCompInfoVo;

public class LlCompInfoBiz extends SuperBiz{

	protected LlCompInfoDao llCompInfoDao;
	//构造函数
	public LlCompInfoBiz(){
		llCompInfoDao = new LlCompInfoDao();	
	}
	public LlCompInfoVo getLlCompInfoBean()
			throws Exception {
		LlCompInfoVo returnBean = new LlCompInfoVo();
		try {
			returnBean = llCompInfoDao.getllCompInfoSql();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过账号配置对象获取企业信息数据异常");
			throw e;
		}
		return returnBean;
	}
	public boolean addLlCompInfo(LlCompInfoVo llCompInfoBean) {
		boolean returnFlag = llCompInfoDao.addLlCompInfo(llCompInfoBean);
		return returnFlag;
	}
	public boolean updateLlCompInfo(LlCompInfoVo llCompInfoBean) {
		boolean returnFlag = llCompInfoDao.updateLlCompInfo(llCompInfoBean);
		return returnFlag;
	}

}
