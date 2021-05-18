package com.montnets.emp.rms.rmsapi.dao;

public interface  QueryHisRecordDAO {
	/**
	 * 通过sp账号获取密码
	 * @param userid sp账号
	 * @return 密码
	 */
	public String findPwdByUserId(String userid);
}
