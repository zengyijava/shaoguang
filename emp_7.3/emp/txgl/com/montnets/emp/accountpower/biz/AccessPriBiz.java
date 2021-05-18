package com.montnets.emp.accountpower.biz;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.accountpower.dao.AccessPriDao;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.util.PageInfo;

public class AccessPriBiz extends SuperBiz{

	/**
	 * 获取企业账户绑定表中发送账号为激活的数据
	 * @param conditionMap 传入查询条件
	 * @param orderbyMap  传入排序条件
	 * @param pageInfo	分页信息
	 * @return 企业账户绑定关系表的集合List<LfSpDepBind>
	 * @throws Exception
	 */
	public List<LfSpDepBind> getSpDepBindWhichUserdataIsOk(LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {
		
		List<LfSpDepBind> xx = null;
		try
		{
			xx = new AccessPriDao().getSpDepBindWhichUserdataIsOk( conditionMap, orderbyMap,
					pageInfo);
		} catch (Exception e)
		{
			// TODO: handle exception
			EmpExecutionContext.error(e,"获取企业账户绑定表中发送账号为激活的数据异常！");
			throw new Exception();
		}
		return xx;
	}
}
