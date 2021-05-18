package com.montnets.emp.common.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfReviewer2level;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.util.MD5;

/**
 * 
 * @author Administrator
 *
 */
public class SysuserBiz extends SuperBiz {

	/**
	 * 通过userid获取所在机构
	 * @param userId
	 * @return
	 * @throws Exception
	 */ 
	public LfDep getDepByUserId(Long userId) throws Exception {

		LfSysuser user = empDao.findObjectByID(LfSysuser.class, userId);

		LfDep dep = empDao.findObjectByID(LfDep.class, user.getDepId());

		return dep;
	}
	
	/**
	 * 获得所有用户
	 * @param userId
	 * @return
	 * @throws Exception
	 */ 
	public List<LfSysuser> getAllSysusers(Long userId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			if (null != userId)
				lfSysuserList = new SpecialDAO().findDomUserBySysuserID(userId
						.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e,"获得所有用户异常！");
		}
		return lfSysuserList;
	}
	
	/**
	 * 获取操作员异常
	 * @description    
	 * @param userId
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-25 下午06:11:25
	 */
	public String getUserCode(Long userId)
	{
		String userCode = "";
		try
		{
			List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userId.toString());
			
			lfSysuserList = empDao.findListBySymbolsCondition(LfSysuser.class,conditionMap, null);
			if(lfSysuserList != null && lfSysuserList.size() > 0)
			{
				userCode = lfSysuserList.get(0).getUserCode();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取操作员账号异常!");
		}
		return userCode;
		
	}
	
	/**
	 * 获得所有用户
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusers1(Long depId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			if (null != depId){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				//状态1表示启用
				conditionMap.put("userState", "1");
				conditionMap.put("depId", depId.toString());
				
				LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
				orderByMap.put("name", StaticValue.ASC);
				
				lfSysuserList = empDao.findListBySymbolsCondition(LfSysuser.class,
						conditionMap, orderByMap);
				
				//去掉无效用户
				if(lfSysuserList != null){
					LfSysuser lfSysuser = null;
					for (int i = 0; i < lfSysuserList.size(); i++) {
						lfSysuser = lfSysuserList.get(i);
						if(lfSysuser.getGuId() == -1L){
							lfSysuserList.remove(i);
						}
					}
				}
				
				return lfSysuserList;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获得所有用户异常！");
		}
		return lfSysuserList;
	}
	
	/**
	 * 通过部门ID获得用户列表
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getSysuserByDepId(Long depId) throws Exception {

		if (depId == null) {
			throw new NullPointerException("根据机构id加载该机构操作员出错，depId为null");
		}

		List<LfSysuser> sysusersList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			conditionMap.put("depId", depId.toString());
			sysusersList = empDao.findListByCondition(LfSysuser.class,
					conditionMap, null);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过部门ID获得用户列表异常！");
			throw e;
		}
		return sysusersList;
	}
	/**
	 * 查找未删除的所有操作员
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getSysuserByDepIdNoDelete(Long depId) throws Exception {

		if (depId == null) {
			throw new NullPointerException("根据机构id加载该机构操作员出错，depId为null");
		}

		List<LfSysuser> sysusersList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			conditionMap.put("depId", depId.toString());
			//conditionMap.put("userState&in", "0,1");
			//让包含注销操作员的机构不允许删除
			sysusersList = empDao.findListBySymbolsCondition(LfSysuser.class,
					conditionMap, null);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查找未删除的所有操作员异常！");
			throw e;
		}
		return sysusersList;
	}
	
	/**
	 * 获得流程信息
	 * @param oid
	 * @return
	 * @throws Exception
	 */
	public String getFlowInfoByOperId(long oid) throws Exception {
		String info = "";
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			conditionMap.put("userId", oid + "");
			List<LfReviewer2level> list2 = empDao.findListByCondition(
					LfReviewer2level.class, conditionMap, orderMap);
			if (list2 != null && list2.size() > 0) {
				for (LfReviewer2level lr : list2) {
					conditionMap.clear();
					conditionMap.put("FType", "1");
					conditionMap.put("FId", lr.getFId() + "");
					List<LfFlow> lflow = empDao.findListByCondition(
							LfFlow.class, conditionMap, orderMap);
					if (lflow != null && lflow.size() > 0) {
						info = info + lflow.get(0).getUserId() + "的第"
								+ lr.getRLevel().toString() + "级审批人;";
					}
				}
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"获得流程信息异常！");
			throw e;
		}
		return info;
	}
	
	/**
	 *   获取该企业的尾号长度
	 * @param corpCode
	 * @return
	 */
	public Integer getSubnoDidit(String corpCode){
		Integer gidit = 4;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			LfCorp corp = null;
			conditionMap.put("corpCode",corpCode);
			List<LfCorp> lfcorpList = empDao.findListByCondition(LfCorp.class, conditionMap, null);
			if(lfcorpList != null && lfcorpList.size() > 0){
				corp = lfcorpList.get(0);
				gidit = corp.getSubnoDigit();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取该企业的尾号长度异常。");
		}
		return gidit;
	}
	
	
	/**
	 *   处理尾号管理中  对尾号进行修改的功能
	 * @param guId
	 * @param newUsedSubno
	 * @param corpCode
	 * @param type
	 * @return
	 */
	public String updateSubnoAllot(String guId,String newUsedSubno,String corpCode,String type){
		String returnMsg = "";
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("usedExtendSubno", newUsedSubno);
			conditionMap.put("corpCode", corpCode);
			List<LfSubnoAllotDetail> detailList = empDao.findListByCondition(LfSubnoAllotDetail.class, conditionMap, null);
			//没有重复
			if(detailList == null || detailList.size() == 0 ){
				LfSubnoAllot allot = null;
				LfSubnoAllotDetail allotDetail = null;
				conditionMap.clear();
				conditionMap.put("corpCode", corpCode);
				if("1".equals(type)){
					conditionMap.put("loginId", guId);
				}else if("2".equals(type)){
					conditionMap.put("menuCode", guId);
				}
				List<LfSubnoAllot> allots = empDao.findListByCondition(LfSubnoAllot.class, conditionMap, null);
				if(allots != null && allots.size()>0){
					allot = allots.get(0);
				}
				List<LfSubnoAllotDetail> allotDetails = empDao.findListByCondition(LfSubnoAllotDetail.class, conditionMap, null);
				if(allotDetails != null && allotDetails.size()>0){
					allotDetail = allotDetails.get(0);
				}
				//获取数据库连接
				Connection conn = empTransDao.getConnection();
				try{
					//开启事务
					empTransDao.beginTransaction(conn);
					if(allot != null){
						allot.setUsedExtendSubno(newUsedSubno);
						allot.setUpdateTime(new Timestamp(System.currentTimeMillis()));
						empTransDao.update(conn, allot);
					}
					if(allotDetail != null){
						allotDetail.setUsedExtendSubno(newUsedSubno);
						allotDetail.setUpdateTime(new Timestamp(System.currentTimeMillis()));
						empTransDao.update(conn, allotDetail);
					}
					//提交事务
					empTransDao.commitTransaction(conn);
					//成功
					returnMsg = "1";		
				}catch (Exception e) {
					EmpExecutionContext.error(e, "处理尾号管理中  对尾号进行修改的功能异常。");
					//事务回滚
					empTransDao.rollBackTransaction(conn);
					returnMsg = "errer";
				}finally{
					//关闭连接
					empTransDao.closeConnection(conn);
				}
			}else{
				//重复
				returnMsg = "2";
			}
		} catch (Exception e) {
			
			EmpExecutionContext.error(e, "处理尾号管理中  对尾号进行修改的功能异常。");
			returnMsg = "errer";
		}
		return returnMsg;
	}
	/**
	 * 删除操作员尾号
	 * @param user
	 * @param guid
	 * @param corpCode
	 * @return
	 */
	public String delSubno(LfSysuser user,Long guid,String corpCode){
		String returnMsg = "";
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		try {
			//开启事务
			empTransDao.beginTransaction(conn);
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("loginId", String.valueOf(guid));
			empTransDao.delete(conn, LfSubnoAllotDetail.class, conditionMap);
			empTransDao.delete(conn, LfSubnoAllot.class, conditionMap);
			if(user != null){
				user.setIsExistSubNo(2);
			}
			empTransDao.update(conn, user);
			//提交事务
			empTransDao.commitTransaction(conn);
			returnMsg = "1";
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除操作员尾号异常。");
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			returnMsg = "errer";
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return returnMsg;
	}
	

	/**
	 * 修改密码
	 * @param username
	 * @param pass
	 * @return
	 * @throws Exception
	 */
	public boolean modifyPassword(LfSysuser sysuser, String pass)
			throws Exception {

		boolean result = false;
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try {
			objectMap.put("password", MD5.getMD5Str(pass+sysuser.getUserName().toLowerCase()));
			conditionMap.put("guId", sysuser.getGuId()+"");
			conditionMap.put("corpCode", sysuser.getCorpCode());
			
			//oracle 数据库
			if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
				
				String time=new Timestamp(System.currentTimeMillis())+"";
				time=time.substring(0, time.lastIndexOf("."));
				objectMap.put("pwdupdatetime", time);
				
			}else{
				//其他数据库类型
				objectMap.put("pwdupdatetime", new Timestamp(System.currentTimeMillis())+"");
			}
			//若为admin首次修改密码
            if("admin".equals(sysuser.getUserName())&& StringUtils.isBlank(sysuser.getComments())){
                objectMap.put("comments", System.currentTimeMillis()+"");
            }
			result = empDao.update(LfSysuser.class, objectMap, conditionMap);

		} catch (Exception e) {
			EmpExecutionContext.error(e,"修改密码异常！");
			result = false;

		}
		return result;
	}

	/**
	 * 查询用户相关信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public LfSysuserVo getSysuserVoByUserId(Long userId) throws Exception {
		if (userId == null) {
			return null;
		}
		LfSysuserVo sysuserVo = null;
		try {
			sysuserVo = new SpecialDAO()
					.findLfSysuserVoByID(userId.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询用户相关信息异常！");
			throw e;
		}
		return sysuserVo;
	}
	
	/**
	 * 通过id和企业编码获取操作员对象
	 * @description    
	 * @param userId
	 * @param corpCode
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午08:33:25
	 */
	public LfSysuserVo getSysuserVoByUserIdAndCorpCode(Long userId, String corpCode) throws Exception {
		if (userId == null) {
			return null;
		}
		LfSysuserVo sysuserVo = null;
		try {
			sysuserVo = new SpecialDAO()
					.findLfSysuserVoByIDAndCorpCode(userId.toString(), corpCode);

		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询用户相关信息异常！");
			throw e;
		}
		return sysuserVo;
	}
	
	
	/**
	 * 通过短信任务查询所有用户相关信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusersOfSmsTaskRecord(Long userId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			if (null != userId)
				lfSysuserList = new SpecialDAO().findDomUserBySysuserIDOfSmsTaskRecord(userId
						.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过短信任务查询所有用户相关信息异常！");
			throw e;
		}
		return lfSysuserList;
	}
	
}
