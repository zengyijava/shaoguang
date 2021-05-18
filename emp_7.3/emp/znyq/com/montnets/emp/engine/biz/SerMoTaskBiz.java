package com.montnets.emp.engine.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.engine.dao.SerMoTaskDao;
import com.montnets.emp.engine.vo.LfMoServiceVo;
import com.montnets.emp.util.PageInfo;

/**
 * 上行业务记录biz
 * @project p_znyq
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-16 上午10:12:11
 * @description
 */
public class SerMoTaskBiz extends SuperBiz
{
	public List<LfMoServiceVo> getSerMoTaskList(LinkedHashMap<String, String> conditionMap, String lguserid, String corpCode, PageInfo pageInfo)
	{
		return new SerMoTaskDao().getSerMoTaskList(conditionMap, lguserid, corpCode, pageInfo);
	}
}
