package com.montnets.emp.common.biz.receive;

import com.montnets.emp.entity.system.LfReport;


public interface IReportStart
{
	//开始方法
	public boolean start(LfReport report) throws Exception;
}
