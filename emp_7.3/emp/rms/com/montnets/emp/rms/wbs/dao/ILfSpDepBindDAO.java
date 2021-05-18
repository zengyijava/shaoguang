package com.montnets.emp.rms.wbs.dao;

import java.util.Map;

public interface ILfSpDepBindDAO {
	/*
	 * 获取sp账号和企业编码的关系，将两则的关系保存到Map中保存
	 */
	public Map<String,String> findSpAndCorpCode()throws Exception;
}
