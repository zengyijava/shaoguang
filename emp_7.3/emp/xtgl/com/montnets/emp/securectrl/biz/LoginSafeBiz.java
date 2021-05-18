package com.montnets.emp.securectrl.biz;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.GenericEmpTransactionDAO;
import com.montnets.emp.entity.securectrl.LfMacIp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.securectrl.dao.GenericLfMacIpVoDAO;
import com.montnets.emp.securectrl.vo.LfMacIpVo;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 *
 */
public class LoginSafeBiz extends SuperBiz{

	private IEmpTransactionDAO genericEmpTranDao = new GenericEmpTransactionDAO();

	/**
	 * 
	 * @param lfSysuser
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfMacIpVo> getMacIpVo(LfSysuser lfSysuser, Map<String,String> conditionMap, PageInfo pageInfo)
			throws Exception {
		List<LfMacIpVo> macIpVosList;
		try {
			macIpVosList = new GenericLfMacIpVoDAO().findLfMacIpVo(lfSysuser,conditionMap, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取操作员绑定信息发生异常！");
			throw e;
		}
		return macIpVosList;
	}
	
	//更新MAC-IP
	public boolean updateMacIpList(List<LfMacIp> addList,List<LfMacIp> updateList) throws Exception{
		Connection conn = null;
		try {
			//获取数据库连接
			conn = genericEmpTranDao.getConnection();
			//开启事务
			genericEmpTranDao.beginTransaction(conn);
			if(addList != null && addList.size()>0){
				genericEmpTranDao.save(conn, addList, LfMacIp.class);
			}
			if(updateList != null && updateList.size()>0){
				for (LfMacIp lfMacIp : updateList) {
					genericEmpTranDao.update(conn, lfMacIp);
				}
			}
			//提交事务
			genericEmpTranDao.commitTransaction(conn);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "更新MAC-IP发生异常！");
			//事务回滚
			genericEmpTranDao.rollBackTransaction(conn);
			throw e;
		} finally {
			//关闭连接
			genericEmpTranDao.closeConnection(conn);
		}
		return true;
	}
	
	
	/**
	 * 获取操作员对象
	 * @param request
	 * @return
	 */
	public LfSysuser getCurrenUser(HttpServletRequest request){
		try
		{
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj == null)
			{
				return null;
			}
			
			return (LfSysuser)loginSysuserObj;
			
		}catch(Exception e)
		{
			EmpExecutionContext.error("SESSION获取操作员对象失败。");
			return null;
		}
	}
	
	
}
