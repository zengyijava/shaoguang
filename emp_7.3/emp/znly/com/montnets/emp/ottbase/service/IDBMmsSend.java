package com.montnets.emp.ottbase.service;

import com.montnets.emp.ottbase.param.MmsWGParams;

public interface IDBMmsSend
{
	/**
	 * 发送彩信的方法
	 * @param mmsWGParams
	 * @return
	 */
	public String sendMms(MmsWGParams mmsWGParams); 
}
