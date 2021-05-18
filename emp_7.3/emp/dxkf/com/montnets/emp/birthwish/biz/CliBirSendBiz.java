/**
 * 
 */
package com.montnets.emp.birthwish.biz;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.birthwish.dao.CliBirSendDao;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.clientsms.LfDfadvanced;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.util.PageInfo;

/**
 * @author Administrator
 *
 */
public class CliBirSendBiz extends SuperBiz
{
	
	private AddrBookSpecialDAO empSpecialDAO = new AddrBookSpecialDAO();
	
	/**
	 * 通过客户机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfClientDep> deps = null;
		try{
			deps = empSpecialDAO.getCliSecondDepTreeByUserIdorDepId(userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"通过客户机构id查找机构树失败！");
		}
		return deps;
	}
	
	/**
	 * 通过客户机构id查找树(重载方法)
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @param corpCode 企业编码
	 * @return
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,String depId,String corpCode) throws Exception {
		List<LfClientDep> deps = null;
		try{
			//通过操作员ID，机构ID，企业编码查询客户机构
			deps = empSpecialDAO.getCliSecondDepTreeByUserIdorDepId(userId,depId,corpCode);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"通过客户机构id查找机构树失败！");
		}
		return deps;
	}
	
	
	/**
	 *   获取客户机构下的客户 
	 * @param clientDep	客户机构
	 * @param LfClient	机构
	 * @param containType	包含1不包含2
	 * @param pageInfo	分页
	 * @return
	 */
	public List<DynaBean> getClientsByDepId(LfClientDep clientDep,LfClient LfClient,Integer containType,PageInfo pageInfo){
		List<DynaBean>  beanList = null;
		try{
			beanList = new CliBirSendDao().findClientsByDepId(clientDep, LfClient, containType, pageInfo);
		}catch (Exception e) {
			beanList = null;
			EmpExecutionContext.error(e,"获取客户机构下的客户失败！");
		}
		return beanList;
	}
	
	/**
	 *  查询机构人数
	 * @param clientDep	查询机构对象
	 * @param containType	包含不包含 
	 * @return
	 */
	public Integer getClientDepCount(LfClientDep clientDep,Integer containType){
		Integer oount = 0;
		try{
			oount = new CliBirSendDao().findClientsCountByDepId(clientDep, containType);
		}catch (Exception e) {
			oount = 0;
			EmpExecutionContext.error(e,"查询客户机构下的人数失败！");
		}
		return oount;
	}
	
	
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
			lfSpDepBindList = new CliBirSendDao().getSpDepBindWhichUserdataIsOk( conditionMap, orderbyMap,
					pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "查找企业SP账号出错！");
			throw new Exception();
		}
		return lfSpDepBindList;
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

	public HashMap<Long, String> getBirthMembersPhone(String ids) {
		HashMap<Long, String> map = new HashMap<Long, String>(16);
		if(StringUtils.isBlank(ids)){
			return map;
		}
		String sql = "SELECT GUID,MOBILE from lf_client WHERE GUID IN (" + ids + ")";
		List<DynaBean> dynaBeanBySql = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		for(DynaBean bean : dynaBeanBySql){
			Long guid = Long.valueOf(bean.get("guid").toString());
			String mobile = (String)bean.get("mobile");
			map.put(guid, mobile);
		}
		return map;
	}
}
