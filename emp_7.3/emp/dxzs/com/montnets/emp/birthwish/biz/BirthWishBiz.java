package com.montnets.emp.birthwish.biz;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.birthwish.dao.BirthWishDao;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.dxzs.LfDfadvanced;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.util.PageInfo;

public class BirthWishBiz extends SuperBiz
{
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
		
		List<LfSpDepBind> lfSpDepBindList = null;
		try
		{
			lfSpDepBindList = new BirthWishDao().getSpDepBindWhichUserdataIsOk( conditionMap, orderbyMap,
					pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "查找企业SP账号出错！");
			throw new Exception();
			
		}
		return lfSpDepBindList;
	}

	public HashMap<Long, String> getBirthMembersPhone(String ids) {
		HashMap<Long, String> map = new HashMap<Long, String>(16);
		if(StringUtils.isBlank(ids)){
			return map;
		}
		String sql = "SELECT GUID,MOBILE from lf_employee WHERE GUID IN (" + ids + ")";
		List<DynaBean> dynaBeanBySql = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		for(DynaBean bean : dynaBeanBySql){
			Long guid = Long.valueOf(bean.get("guid").toString());
			String mobile = (String)bean.get("mobile");
			map.put(guid, mobile);
		}
		return map;
	}

	/**
	 * 高级设置存为默认
	 * @param conditionMap 删除原来设置条件
	 * @param lfDfadvanced 更新默认高级设置对象
	 * @return
	 */
	public String setDefault(LinkedHashMap<String, String> conditionMap, LfDfadvanced lfDfadvanced){
		String result = "fail";
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			
			//删除原有的设置
			empTransDao.delete(conn, LfDfadvanced.class, conditionMap);
			
			//新增默认高级设置信息
			boolean saveResult = empTransDao.save(conn, lfDfadvanced);
			
			//成功
			if(saveResult){
				result = "seccuss";
				empTransDao.commitTransaction(conn);
			}
			else{
				empTransDao.rollBackTransaction(conn);
			}
			return result;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "高级设置存为默认异常！");
			empTransDao.rollBackTransaction(conn);
			return result;
		}
		finally{
			// 关闭连接
			if(conn != null){
				empTransDao.closeConnection(conn);
			}
		}
	}
}
