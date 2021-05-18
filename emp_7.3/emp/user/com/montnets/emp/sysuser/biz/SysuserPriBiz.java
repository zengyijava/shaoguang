package com.montnets.emp.sysuser.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SendBirthDateBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.LoginInfo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.client.LfCliDepConn;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.securectrl.LfMacIp;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfBalancePri;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfReviewer2level;
import com.montnets.emp.entity.sysuser.LfRoles;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.sysuser.LfUser2role;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.sysuser.dao.SysuserDAO;
import com.montnets.emp.sysuser.vo.LfSysuser2Vo;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.ZipUtil;

public class SysuserPriBiz extends SuperBiz  {
	/**
	 * 	更新操作员的状态  当将操作员失效的时候，删除该操作员的固定信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	
    public Integer changeState(Long userId) throws Exception {
		//判断传入要更改状态的操作员id 是否为空
		if (userId == null) {
			return null;
		}
		//定义返回值
		Integer result = null;
		//修改操作状态
		boolean updateResult = false;
		try {
			//通过操作员ID查询操作员
			LfSysuser sysuser = empDao.findObjectByID(LfSysuser.class, userId);
			//判断操作员是否存在
			if (sysuser == null) {
				return null;
			}
			//不管是禁用，还是 注销，该用户的错误次数归为0 may add
			sysuser.setPwderrortimes(0);
			//启用状态和锁定状态切换为禁用
			if (sysuser.getUserState() == 1||sysuser.getUserState() == 3) {
				//使用户失效	
				sysuser.setUserState(0);
				//获得连接
				Connection conn = empTransDao.getConnection();
				try {
					//开启事物
					empTransDao.beginTransaction(conn);
					//查询条件
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					//排序条件
					LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();	
					//使用户失效的时候，将尾号删除
					if(sysuser.getIsExistSubNo() == 1){
							conditionMap.put("corpCode", sysuser.getCorpCode());
							conditionMap.put("loginId", String.valueOf(sysuser.getGuId()));
							empTransDao.delete(conn, LfSubnoAllotDetail.class, conditionMap);
							empTransDao.delete(conn, LfSubnoAllot.class, conditionMap);
							//将操作员设置为没有尾号状态
							sysuser.setIsExistSubNo(2);								
					}
					//查找此用户是否还有定时中的短信任务
					conditionMap.clear();				
					//发送用户
					conditionMap.put("userId", sysuser.getUserId().toString());
					//未发送
					conditionMap.put("sendstate", "0");
					//审批被拒绝的除外
					conditionMap.put("reState&<>", "2");
					//未被撤销的
					conditionMap.put("subState&<>", "3");
					//orderbyMap.put("submitTime", StaticValue.DESC);
					List<LfMttask> mtList = empDao.findListBySymbolsCondition(LfMttask.class, conditionMap, null);
					//判断查询结果是否有值
					if(mtList !=null && mtList.size()>0)
					{
						//现改为撤消所有任务
						String sub = "";
						//循环遍历list
						for(int j=0;j<mtList.size();j++)
						{
							LfMttask mt = mtList.get(j);
							sub+=mt.getMtId()+",";
						}
						//去除字符串拼接最后一个逗号
						sub = sub.substring(0,sub.length()-1);
						//改成已冻结
						//objectMap.put("subState", "4");
						//现改为撤消所有任务
						objectMap.put("subState", "3");
						//清空条件
						conditionMap.clear();
						//设值条件
						conditionMap.put("mtId", sub);
						//更新任务表
						empTransDao.update(conn, LfMttask.class, objectMap, conditionMap);  
						
					}
                    calcelUserTask(conn, sysuser);
					//修改操作员状态
					updateResult = empTransDao.update(conn, sysuser);	
					//提交事务
					empTransDao.commitTransaction(conn);
					//修改成功后移除该操作员的登录状态
					removeLoginInfoMapByUserid(sysuser.getUserId());
				} catch (Exception e) {
					EmpExecutionContext.error(e,"更新操作员的状态 出现异常！");
					//事务回滚
					empTransDao.rollBackTransaction(conn);
					result = null;
				}finally{
					//判断连接是否为空  不为空则关闭连接
					if (conn != null) {
						empTransDao.closeConnection(conn);
					}
				}
			} else if (sysuser.getUserState() == 0) {
				//使用户 激活
				sysuser.setUserState(1);	
				//定义连接对象
				Connection conn = null;
				try {
					//获取连接
					conn = empTransDao.getConnection();
					//开启事物
					empTransDao.beginTransaction(conn);
					//查询条件
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					//排序map
					LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
					//查找此用户所以已冻结的任务(所有已冻结的任务变成已提交)
					//发送用户
					conditionMap.put("userId", sysuser.getUserId().toString());
					//已冻结的
					conditionMap.put("subState", "4");
					//orderbyMap.put("submitTime", StaticValue.DESC);
					List<LfMttask> mtList = empDao.findListBySymbolsCondition(LfMttask.class, conditionMap, orderbyMap);
					//判断任务list 是否有值
					if (mtList != null && mtList.size() > 0) {
						//冻结所有的这些任务
						for (int j = 0; j < mtList.size(); j++) {
							LfMttask mt = mtList.get(j);
							//设为已提交状态
							mt.setSubState(2);
							//更新
							empTransDao.update(conn,mt);
						}
					}
					//更新操作员
					updateResult = empTransDao.update(conn, sysuser);	
					//提交事务
					empTransDao.commitTransaction(conn);
			
				}catch (Exception e) {
					//异常写日志
					EmpExecutionContext.error(e,"更新操作员的状态 出现异常！");
					//回滚
					empTransDao.rollBackTransaction(conn);
					result = null;
				}finally{
					//关闭连接
					if (conn != null) {
						empTransDao.closeConnection(conn);
					}
				}
			}
			//操作员状态
			result = sysuser.getUserState();
			if (!updateResult) {
				result = null;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"更新操作员的状态 出现异常！");
			result = null;
		}
		return result;
	}
	
	/**
	 * 
	 *   处理 注销 禁用操作员  ，把该操作员所提交的审核实时记录中未完成的任务改为已撤消
	 * @param conn	数据库连接
	 * @param user	被注销或禁用的操作员
	 */
	
    public void calcelUserTask(Connection conn, LfSysuser user){
		try{
			//查询条件
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			//排序条件
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String, String>();
			//标识已完成
			orderbyMap.put("isComplete","1");
			//标识已撤消
			orderbyMap.put("RState","4");
			//标识未完成
			conditionMap.put("isComplete", "2");
			//短信彩信发送
			conditionMap.put("infoType", "1,2");
			//任务提交人
			conditionMap.put("ProUserCode",String.valueOf(user.getUserId()));
			//更新操作
			empTransDao.update(conn, LfFlowRecord.class, orderbyMap, conditionMap);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "更新操作员提交的审批实时记录失败！");
		}
	}
	
	/**
	 *  判断该操作员是否还有审批记录
	 * @param lguserid	操作员对应的userid
	 * @return
	 */
	
    public String judgeIsExFlowRecord(Long lguserid){
		//返回消息串
		String returnmsg = "";
		try{
			//查询条件
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			//标识未完成
			conditionMap.put("isComplete", "2");
			//操作员编码
			conditionMap.put("userCode", String.valueOf(lguserid));
			//审批记录
			List<LfFlowRecord> recordList = empDao.findListBySymbolsCondition(LfFlowRecord.class, conditionMap, null);
			//判断记录结果list
			if(recordList != null && recordList.size()>0){
				returnmsg = "exist";
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询操作员审批实时记录失败！");
		}
		return returnmsg;
	}
	
	
	

	/**
	 * 注销操作员的方法
	 * @param sysuser
	 * @return
	 * @throws Exception
	 */
	
    public boolean deleteUser(LfSysuser sysuser) throws Exception
	{
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//排序
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();	
		boolean flag = true;
		boolean flag2 = true;
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		//短信发送数
		Long smsCount =0L;
		//彩信发送数
		Long mmsCount =0L;
		try {	
			//***注销时候，错误次数归0
			sysuser.setPwderrortimes(0);
			//修改操作员
			empTransDao.update(conn, sysuser);
			//开启事务
			empTransDao.beginTransaction(conn);
			//查找此用户是否还有定时中的任务		
			//发送用户
			conditionMap.put("userId", sysuser.getUserId().toString());	
			//未发送
			conditionMap.put("sendstate", "0");
			//审批被拒绝的除外
			conditionMap.put("reState&<>", "2");
			//未被撤销的
			conditionMap.put("subState&<>", "3");
			//查询任务list
			List<LfMttask> mtList =  empDao.findListBySymbolsCondition(LfMttask.class, conditionMap, null);	
			//判断任务表记录是否为空
			if (mtList != null && mtList.size() > 0) {
				TaskManagerBiz timerBiz = new TaskManagerBiz();
				//撤销所有的这些任务
				for (int j = 0; j < mtList.size(); j++) {
					//获取一条短信任务
					LfMttask mt = mtList.get(j);
					//设为撤销状态
					mt.setSubState(3);
					//发送状态
					if (mt.getSendstate() == 0 && mt.getTimerStatus() == 1) {
						//修改任务表
						boolean flag1 = empTransDao.update(conn,mt);
						//审批状态
						if (flag1 && mt.getReState() != -1) {
							//删除定时器
							timerBiz.stopTimerTask(conn, mt.getMtId().toString());
						}
						if (!flag1) {
							flag2 = false;
							break;
						}
					} else {
						//修改任务表
						empTransDao.update(conn, mt);
					}
					//代表彩信
					if (mt.getMsType() == 2) {
						mmsCount = mmsCount - mt.getEffCount();
					} else {
						smsCount = smsCount - Long.parseLong(mt.getIcount());
					}			
				}
			}
			if (!flag2) {
				//如果撤销任务中有撤销操作出现了问题，则直接回滚事务，并返回false
				empTransDao.rollBackTransaction(conn);
				return false;
			} else {
				// 如果是撤消操作,则需要补回计费记录(先判断当前用户是否启用计费)
				BalanceLogBiz b = new BalanceLogBiz();
				if(b.IsChargings(sysuser.getUserId()))
				{	
					//如果是彩信则补回给彩信,如果是短信补回给短信
					if (mmsCount < 0 || smsCount < 0) {
						flag = b.changeSmsAndMmsAmount(conn,sysuser, smsCount,mmsCount);
						if (!flag) {
							//补回费用失败
							empTransDao.rollBackTransaction(conn);
							return false;
						}
					}						
				}
			}			
			//生日祝福的处理
			new SendBirthDateBiz().CanceledBirthDay(conn,sysuser.getUserId());
			//下行业务判断
			new SysuserDAO().CanceledMtService(conn,sysuser.getUserId());
			//删除子号分配记录
/*			conditionMap.clear();
			conditionMap.put("loginId", sysuser.getGuId().toString());
			conditionMap.put("corpCode", sysuser.getCorpCode());
			conditionMap.put("menuCode&is null", "isnull");
			conditionMap.put("busCode&is null", "isnull");
			*//**
			 * null in ('isnull') and null in ('isnull')
			 * 该条件不被支持 导致最后拼接sql语句 不被DB2支持 且 在支持的数据库 中 由于 条件不可能满足 不会删除任何记录 暂时取消子号的删除
			 *//*
			empTransDao.delete(conn, LfSubnoAllotDetail.class, conditionMap);
			empTransDao.delete(conn, LfSubnoAllot.class, conditionMap);*/
			//更新状态
			conditionMap.clear();
			//
			objectMap.clear();
			conditionMap.put("userId", sysuser.getUserId().toString());
			objectMap.put("userState", "2");
			//设置成无尾号
			objectMap.put("isExistSubNo","2");
			flag = empTransDao.update(conn, LfSysuser.class, objectMap, conditionMap);
            calcelUserTask(conn, sysuser);
			empTransDao.commitTransaction(conn);
			//修改成功后移除该操作员的登录状态
			removeLoginInfoMapByUserid(sysuser.getUserId());
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"注销操作员失败！");
			flag = false;
		} finally {
			if (conn != null) {
				empTransDao.closeConnection(conn);
			}
		}
		return flag;
	}
	
	/**
	 * 踢出操作员的登录信息
	 */
	
    public void removeLoginInfoMapByUserid(Long userid)
	{
		//获取在线用户map
		Map<String,LoginInfo> loginMap = StaticValue.getLoginInfoMap();
		//迭代循环登录用户map
		Iterator<String> itr = loginMap.keySet().iterator();
		//map key
		String key;
		try {
			//移除登录用户key
			String removeKey = null;
			//循环对比取出踢出用户
			while (itr.hasNext()) {
				//用户key
				key = itr.next();
				//登录用户
				LoginInfo logininfo = loginMap.get(key);
				//判断是否与当前登录用户一致
				if (userid.equals(logininfo.getUserId())) {
					removeKey = key;
					break;
				}
			}
			if (removeKey != null) {
				StaticValue.getLoginInfoMap().remove(removeKey);
			}
		} catch (Exception e) {
			EmpExecutionContext.error( e,"踢出操作员的登录信息，userid="+userid.toString());
		}
	}
	
	/**
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	//判断电话号码是否存在
	
    public boolean isTelExists(String mobile) throws Exception {
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try {
			conditionMap.put("mobile", mobile);
			List<LfSysuser> tempList = empDao.findListByCondition(
					LfSysuser.class, conditionMap, null);
			if (tempList == null || tempList.size() == 0) {
				result = false;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "判断电话号码存在出现异常！");
			throw e;
		}
		return result;
	}

	/**
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	//判断usercode是否存在
	
    public boolean isUserCodeExists(String corpCode, String userCode) throws Exception {
		boolean result = true;
		
		try {
			List<LfSysuser> tempList= new SysuserDAO().getLfSysUserByUserCode(corpCode,userCode);
			if (tempList == null || tempList.size() == 0) {
				result = false;
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "判断usercode是否存在发生异常！");
			throw e;
		}
		return result;
	}
	
	/**
	 *   尾号管理 中删除尾号的，  并且处理是否操作员存在 并且将操作员的尾号状态进行更新
	 * @param guid
	 * @param corpCode
	 * @return
	
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
			EmpExecutionContext.error(e);
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			returnMsg = "errer";
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return returnMsg;
	}
	 */
	
	/**
	 * 
	 * @param username
	 * @param phone
	 * @return
	 * @throws Exception
	
	public boolean modifyMobile(String username, String phone) throws Exception {

		boolean result = false;
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			conditionMap.put("userName", username);
			objectMap.put("mobile", phone);
			result = empDao.update(LfSysuser.class, objectMap, conditionMap);

		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	 */
	
	/**
	 *  在修改 操作员的时候 处理回收固定尾号
	 * @param guId	GUID	
	 * @param corpCode	企业编码
	 * @return
	*/ 
	
    public String delUserSubno(LfSysuser user){
		//返回消息串
		String returnMsg = "";
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		try {
			//开启事务
			empTransDao.beginTransaction(conn);
			//企业编码
			conditionMap.put("corpCode", user.getCorpCode());
			//登录ID
			conditionMap.put("loginId", String.valueOf(user.getGuId()));
			//删除子号详情
			empTransDao.delete(conn, LfSubnoAllotDetail.class, conditionMap);
			//删除子号
			empTransDao.delete(conn, LfSubnoAllot.class, conditionMap);
			//提交事务
			empTransDao.commitTransaction(conn);
			returnMsg = "1";
		} catch (Exception e) {
			//记录异常日志
			EmpExecutionContext.error(e,"删除操作员尾号失败！");
			returnMsg = "errer";
			//事务回滚
			empTransDao.rollBackTransaction(conn);
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回消息体
		return returnMsg;
	}
	
	/**
	 *   在新增操作员的时候  处理回收操作员尾号的情况
	 * @param guid
	 * @param corpCode
	 * @return
	 */
	
    public String delUserSubno(Long guid, String corpCode){
		//返回消息体
		String returnMsg = "";
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		try {
			//开启事务
			empTransDao.beginTransaction(conn);
			//企业编码
			conditionMap.put("corpCode", corpCode);
			//登录guid
			conditionMap.put("loginId", String.valueOf(guid));
			//删除子号详情
			empTransDao.delete(conn, LfSubnoAllotDetail.class, conditionMap);
			//删除子号
			empTransDao.delete(conn, LfSubnoAllot.class, conditionMap);
			//提交事务
			empTransDao.commitTransaction(conn);
			//返回消息体
			returnMsg = "1";
		} catch (Exception e) {
			EmpExecutionContext.error(e,"新增操作员处理尾号！");
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			returnMsg = "errer";
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回消息体
		return returnMsg;
	}
	
	
	/**
	 *     更新操作员       并且加尾号
	 * @param curLoginedUserId
	 * @param domId
	 * @param roleIds
	 * @param lfSysuser
	 * @param wgempsList
	 * @param flow
	 * @param flowUsersMap
	 * @param isdepor
	 * @return
	 * @throws Exception
	*/
	
    public String updateRelationshipAndSubno(Long curLoginedUserId, Long domId,
                                             Long[] roleIds, LfSysuser lfSysuser,
                                             LfFlow flow, Map<String, String> flowUsersMap) throws Exception {
		//消息体
		String resultMsg = "0";
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		//查询条件
		LinkedHashMap<String, String> conditionMap2 = new LinkedHashMap<String, String>();
		try {
			//开启事务
			empTransDao.beginTransaction(conn);
			//修改操作员
			empTransDao.update(conn, lfSysuser);
            updateEmployeeInUser(conn, lfSysuser);

			conditionMap2.put("userId", String.valueOf(lfSysuser.getUserId()));
			int delDominNum = empTransDao.delete(conn, LfDomination.class,
					conditionMap2);
			
			if(domId != null){
				DepBiz depBiz = new DepBiz();
				LfDep dep=empDao.findObjectByID(LfDep.class, domId);
				List<Long> depIdList = depBiz.getChildDepIds(domId,dep.getCorpCode());
				
				if (delDominNum >= 0 ) {
					for (int index = 0; index < depIdList.size(); index++) {
						LfDomination lfDomination = new LfDomination();
						lfDomination.setDepId(depIdList.get(index));
						lfDomination.setUserId(lfSysuser.getUserId());
						empTransDao.save(conn, lfDomination);
					}
				}
			}
			
			if (roleIds[0] != -1) {
				conditionMap2 = new LinkedHashMap<String, String>();

				RoleBiz roleBiz = new RoleBiz();
				String strRoleIds = roleBiz
						.getRoleIdByUserIdNoAdmin(lfSysuser
								.getUserId());
				conditionMap2.put("userId", String.valueOf(lfSysuser
						.getUserId()));
				conditionMap2.put("roleId", strRoleIds);
				int delU2Num = empTransDao.delete(conn, LfUser2role.class,
						conditionMap2);
				if (delU2Num >= 0) {
					for (int i = 0; i < roleIds.length; i++) {
						LfRoles lfRoles = empDao.findObjectByID(
								LfRoles.class, roleIds[i]);
						LfUser2role user2role = new LfUser2role();
						user2role.setRoleId(lfRoles.getRoleId());
						user2role.setUserId(lfSysuser.getUserId());
						empTransDao.save(conn, user2role);
					}
				}
			}

		
			LinkedHashMap<String, String> conditionMap3 = new LinkedHashMap<String, String>();
			conditionMap3.put("userId", lfSysuser.getUserName());
			List<LfFlow> lflow = empDao.findListByCondition(LfFlow.class,
					conditionMap3, null);
			if (lflow != null && lflow.size() > 0) {
				long Fid = lflow.get(0).getFId();
				conditionMap3.clear();
				conditionMap3.put("FId", String.valueOf(Fid));
				empTransDao.delete(conn, LfReviewer2level.class, conditionMap3);
				empTransDao.delete(conn, LfFlow.class, conditionMap3);
			}
			if (flow != null && flowUsersMap != null) {
				Long fId = empTransDao.saveObjectReturnID(conn, flow);
				for (int k = 1; k <= flow.getRLevelAmount(); k++) {
					String flowUserId = flowUsersMap.get(String.valueOf(k));
					LfReviewer2level r2l = new LfReviewer2level();
					r2l.setFId(fId);
					r2l.setUserId(Long.valueOf(flowUserId));
					r2l.setRLevel(k);
					empTransDao.save(conn, r2l);
				}
			}

			DepSpUserBindBiz domSpUserBiz = new DepSpUserBindBiz();
			domSpUserBiz.delDomSpUserByUserId(conn, lfSysuser.getUserId()
					.toString(), 2);
			//提交事务
			empTransDao.commitTransaction(conn);
			resultMsg = "3";
		} catch (Exception e) {
			resultMsg = "0";
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "更新操作员信息发生异常！");
			throw e;
		} finally {
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return resultMsg;
	}
	 
	
	
	/**
	 * 验证是否重复使用其尾号   重复返回，不重复就修改 
	 * @param guId
	 * @param oldusedSubno
	 * @param newusedSubno
	 * @return
	*/
	
    public String updateUserSubno(Long guId, String oldUsedSubno, String newUsedSubno, String corpCode){
			String returnMsg = "";
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("usedExtendSubno", newUsedSubno);
			conditionMap.put("corpCode", corpCode);
			List<LfSubnoAllotDetail> detailList = empDao.findListByCondition(LfSubnoAllotDetail.class, conditionMap, null);
			SubnoManagerBiz biz = new SubnoManagerBiz();
			//没有重复
			if((detailList == null || detailList.size() == 0 ) && biz.isContainGtPortCpno(newUsedSubno)){
					LfSubnoAllot allot = null;
					LfSubnoAllotDetail allotDetail = null;
					conditionMap.clear();
					conditionMap.put("corpCode", corpCode);
					conditionMap.put("loginId", String.valueOf(guId));
					conditionMap.put("usedExtendSubno",oldUsedSubno);
				
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
							allot.setValidity(0L);
							allot.setIsValid(1);
							allot.setUpdateTime(new Timestamp(System.currentTimeMillis()));
							empTransDao.update(conn, allot);
						}
						if(allotDetail != null){
							allotDetail.setUsedExtendSubno(newUsedSubno);
							allotDetail.setValidity(0L);
							allotDetail.setIsValid(1);
							allotDetail.setUpdateTime(new Timestamp(System.currentTimeMillis()));
							empTransDao.update(conn, allotDetail);
						}
						//提交事务
						empTransDao.commitTransaction(conn);
						//成功
						returnMsg = "1";		
					}catch (Exception e) {
						EmpExecutionContext.error(e,"验证是否重复使用其尾号出现异常！");
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
			
			EmpExecutionContext.error(e,"验证是否重复使用其尾号出现异常！");
			returnMsg = "errer";
		}
		return returnMsg;
	} 
	/*
	 @param conn
	 * @param lfSysuser
	 * @return
	 * @throws Exception
	 */
	private boolean updateEmployeeInUser(Connection conn, LfSysuser lfSysuser) throws Exception {
		//定义返回值
		boolean result = false;
		try{
			//定义biz
			CommonBiz commonBiz = new CommonBiz();
			//通过guid查询员工对象
			LfEmployee employee = commonBiz.getObjByGuid(LfEmployee.class, lfSysuser.getGuId());
			//判断员工对象查出来是否为空
			if(employee == null){
				return false;
			}
			//邮件地址
			employee.setEmail(lfSysuser.getEMail());
			//msn地址
			employee.setMsn(lfSysuser.getMsn());
			//qq号码
			employee.setQq(lfSysuser.getQq());
			//员工姓名
			employee.setName(lfSysuser.getName());
			//员工性别
			employee.setSex(lfSysuser.getSex());
			//电话号码
			employee.setMobile(lfSysuser.getMobile());
			//员工生日
			employee.setBirthday(lfSysuser.getBirthday());
			//办公电话
			employee.setOph(lfSysuser.getOph());
			//修改员工信息
			result = empTransDao.update(conn, employee);
		}catch(Exception e){
			//写入日志
			EmpExecutionContext.error(e,"更新操作员中的员工信息出现异常 ！");
			throw e;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 修改操作员权限范围以及相关
	 * @param curLoginedUserId
	 * 		当前登录用户id
	 * @param domId
	 * 		权限返回id
	 * @param roleIds
	 * 		角色id
	 * @param lfSysuser
	 * 		操作员
	 * @param wgempsList
	 * 		sp账号绑定表集合
	 * @param flow
	 * 		流程对象
	 * @param flowUsersMap
	 * @return
	 * @throws Exception
	 */
	
    public boolean updateRelationship(Long curLoginedUserId, Long domId,
                                      Long[] roleIds, LfSysuser lfSysuser, List<LfSpDepBind> wgempsList,
                                      LfFlow flow, Map<String, String> flowUsersMap, Integer isdepor) throws Exception {
		boolean updateok = false;
		//定义连接
		Connection conn = null;
		LinkedHashMap<String, String> conditionMap2 = new LinkedHashMap<String, String>();
		try {
			//获取数据库连接
			conn = empTransDao.getConnection();
			//开启事务
			empTransDao.beginTransaction(conn);
			//判断是否是机构负责人
				/*if(lfSysuser.getDepId()!=null){
					LfDep lfd=empDao.findObjectByID(LfDep.class, lfSysuser.getDepId());
					if(lfd!=null){
						if(isdepor==1){
							lfd.setDepDirect(lfSysuser.getGuId());
						}else{
							if(lfd.getDepDirect().longValue()==lfSysuser.getGuId().longValue()){
								lfd.setDepDirect(null);
							}
						}
						empTransDao.update(conn, lfd);
					}
					
				}*/
			//更新操作员
			empTransDao.update(conn, lfSysuser);
			//更新操作员关联的员工
            updateEmployeeInUser(conn, lfSysuser);
			//操作员ID
			conditionMap2.put("userId", String.valueOf(lfSysuser.getUserId()));
			//删除权限范围数据
			int delDominNum = empTransDao.delete(conn, LfDomination.class,
					conditionMap2);
			
			if(domId != null){
				DepBiz depBiz = new DepBiz();
				//权限范围顶级机构
				LfDep dep=empDao.findObjectByID(LfDep.class, domId);
				//获取机构的所有子级机构
				List<Long> depIdList = depBiz.getChildDepIds(domId,dep.getCorpCode());
				//判断删除是否成功
				if (delDominNum >= 0 ) {
					//判断权限范围list有无数据
					for (int index = 0; index < depIdList.size(); index++) {
						LfDomination lfDomination = new LfDomination();
						lfDomination.setDepId(depIdList.get(index));
						lfDomination.setUserId(lfSysuser.getUserId());
						empTransDao.save(conn, lfDomination);
					}
				}
			}
			
			if (roleIds[0] != -1) {
				conditionMap2 = new LinkedHashMap<String, String>();
				//角色biz
				RoleBiz roleBiz = new RoleBiz();
				//获取当前登录用户的 相关角色id
				String strRoleIds = roleBiz
						.getRoleIdByUserIdNoAdmin(curLoginedUserId);
				conditionMap2.put("userId", String.valueOf(lfSysuser
						.getUserId()));
				conditionMap2.put("roleId", strRoleIds);
				int delU2Num = empTransDao.delete(conn, LfUser2role.class,
						conditionMap2);
				if (delU2Num >= 0) {
					for (int i = 0; i < roleIds.length; i++) {
						LfRoles lfRoles = empDao.findObjectByID(
								LfRoles.class, roleIds[i]);
						LfUser2role user2role = new LfUser2role();
						user2role.setRoleId(lfRoles.getRoleId());
						user2role.setUserId(lfSysuser.getUserId());
						empTransDao.save(conn, user2role);
					}
				}
			}

		
			LinkedHashMap<String, String> conditionMap3 = new LinkedHashMap<String, String>();
			conditionMap3.put("userId", lfSysuser.getUserName());
			List<LfFlow> lflow = empDao.findListByCondition(LfFlow.class,
					conditionMap3, null);
			if (lflow != null && lflow.size() > 0) {
				long Fid = lflow.get(0).getFId();
				conditionMap3.clear();
				conditionMap3.put("FId", String.valueOf(Fid));
				empTransDao.delete(conn, LfReviewer2level.class, conditionMap3);
				empTransDao.delete(conn, LfFlow.class, conditionMap3);
			}
			if (flow != null && flowUsersMap != null) {
				Long fId = empTransDao.saveObjectReturnID(conn, flow);
				for (int k = 1; k <= flow.getRLevelAmount(); k++) {
					String flowUserId = flowUsersMap.get(String.valueOf(k));
					LfReviewer2level r2l = new LfReviewer2level();
					r2l.setFId(fId);
					r2l.setUserId(Long.valueOf(flowUserId));
					r2l.setRLevel(k);
					empTransDao.save(conn, r2l);
				}
			}

			DepSpUserBindBiz domSpUserBiz = new DepSpUserBindBiz();
			domSpUserBiz.delDomSpUserByUserId(conn, lfSysuser.getUserId()
					.toString(), 2);
			if (wgempsList != null && wgempsList.size() > 0) {

				empTransDao.save(conn, wgempsList, LfSpDepBind.class);
			}
			//提交事务
			empTransDao.commitTransaction(conn);
			updateok = true;
		} catch (Exception e) {
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"更新操作员相关信息发生异常！");
			throw e;
		} finally {
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return updateok;
	}
	
	/**
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	
    public int updateOperRole(Long userId, Long[] roleId) throws Exception
	{
		int updatenum = 0;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("userId&=", String.valueOf(userId));
		if(StaticValue.getCORPTYPE() ==0){
			conditionMap.put("roleId&"+StaticValue.NOT_IN, "1,2");
		}else{
			conditionMap.put("roleId&"+StaticValue.NOT_IN, "1,2,3");
		}
		List<LfUser2role> userRoleList=empDao.findListBySymbolsCondition(null, LfUser2role.class, conditionMap, null);
		StringBuffer userids=new StringBuffer("");
		StringBuffer roleIds=new StringBuffer("");
		if(userRoleList!=null&&userRoleList.size()>0){
			for (int i = 0; i < userRoleList.size(); i++)
			{
				userids.append(userRoleList.get(i).getUserId()).append(",");
				roleIds.append(userRoleList.get(i).getRoleId()).append(",");
			}
			userids.deleteCharAt(userids.lastIndexOf(","));
			roleIds.deleteCharAt(roleIds.lastIndexOf(","));
		}
		//获取数据库连接
		Connection conn=empTransDao.getConnection();
		try {
			//开启事务
			empTransDao.beginTransaction(conn);
			if(!"".equals(userids.toString())){
				conditionMap.clear();
				conditionMap.put("userId", userids.toString());
				conditionMap.put("roleId", roleIds.toString());
				empTransDao.delete(conn, LfUser2role.class, conditionMap);
			}
				List<LfUser2role> user2roles=new ArrayList<LfUser2role>();
				for (int index = 0; index < roleId.length; index++)
				{
					if (roleId[index] != null)
					{
						LfUser2role user2role = new LfUser2role();
						user2role.setUserId(userId);
						user2role.setRoleId(roleId[index]);
						user2roles.add(user2role);
					}
				}
				if(user2roles!=null&&user2roles.size()>0){
					updatenum=empTransDao.save(conn, user2roles, LfUser2role.class);
				}
				//提交事务
				empTransDao.commitTransaction(conn);
		} catch (Exception e) {
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "修改操作员角色信息发生异常！");
			throw e;
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return updatenum;

	}
	
	/**
	 * 通过员工机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	
    public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId, String depId) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			deps = new SysuserDAO().getEmpSecondDepTreeByUserIdorDepId( userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取员工机构出现异常！");
		}
		return deps;
	}
	
	
    public List<LfSysuser2Vo> getUsersByRoleId(Long roleId){
		try {
			return new SysuserDAO().findAllLfSysuserByRoleId(roleId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取操作员的角色信息失败！");
		}
		return null;
	}
	
	
    public List<LfReviewer2level> getFlowInfosByOperId(long oid)
	throws Exception {
		LfSysuser curSysuser = empDao.findObjectByID(LfSysuser.class, oid);
		LfSysuserVo sysuserVo = null;
		try {
			sysuserVo = new SysuserDAO()
					.findLfSysuserVoByID(String.valueOf(oid));
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取操作员信息异常！");
			throw e;
		}
		if(sysuserVo == null){
			return null;
		}
		String operId = sysuserVo.getUserName();
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		List<LfReviewer2level> list2 = new ArrayList<LfReviewer2level>();
		try {
			conditionMap.put("userId", operId);
			conditionMap.put("FType", "1");
			conditionMap.put("corpCode", curSysuser.getCorpCode());
			List<LfFlow> list = empDao.findListByCondition(LfFlow.class,
					conditionMap, orderMap);
			if (list != null && list.size() > 0) {
				conditionMap.clear();
				conditionMap.put("FId", list.get(0).getFId().toString());
				orderMap.put("RLevel", StaticValue.ASC);
				list2 = empDao.findListByCondition(LfReviewer2level.class,
						conditionMap, orderMap);
			}
		
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取操作员审核流信息发生异常！");
			throw e;
		}
		return list2;
	}
	
	
	
    public List<LfSysuser> getAllUsedSysusers(Long userId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {

			if (null != userId) {
                lfSysuserList = new SysuserDAO().findDomUsedUserBySysuserID(userId
                        .toString());
            }

		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取操作员列表发生异常！");
			throw e;
		}
		return lfSysuserList;
	}
	
	/**
	 * 
	 * @param sysuserVo
	 * @param curLoginedUser
	 * @param depId
	 * @param roleId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<LfSysuserVo> getSysuserVo(LfSysuserVo sysuserVo,
                                          Long curLoginedUser, Long depId, Long roleId, PageInfo pageInfo)
			throws Exception {
		List<LfSysuserVo> userVosList;
		try {
			userVosList = new SysuserDAO().findLfSysuserVo(
					sysuserVo, curLoginedUser, depId, roleId, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取操作员信息异常！");
			throw e;
		}
		return userVosList;
	}
	
	/**
	 * 操作员导出
	 * @param lsv
	 * @param userId
	 * @param depId
	 * @param roleId
	 * @param pageInfo
	 * @return
	 */
	
    public Map<String, String> createSysUserExcel(String excelPath, LfSysuserVo lsv, Long userId,
                                                  Long depId, Long roleId, PageInfo pageInfo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
		
		String voucherTemplatePath = excelPath +File.separator +"temp"+File.separator+ "u_sysUser_"+langName+".xlsx";
		String voucherPath = "voucher";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();
		List<LfSysuserVo> userList = new ArrayList<LfSysuserVo>();
		// 设置每个book(sheet)的行数
		pageInfo.setPageSize(500000);
		try {
			userList =getSysuserVo(lsv, userId, depId, roleId,
					pageInfo);
		} catch (Exception e1) {
			EmpExecutionContext.error(e1,"获取操作员列表信息异常！");
		}

		if (userList == null || userList.size() == 0) {
			return null;
		}

		// 计算出文件数
		int size = pageInfo.getTotalPage();
		// 当前每页条数
		// int intRowsOfPage = 20000;

		// 当前每页显示条数
		/*
		 * int intPagesCount = (userList.size() % intRowsOfPage == 0) ?
		 * (userList .size() / intRowsOfPage) : (userList.size() / intRowsOfPage
		 * + 1);
		 */

		// int size = intPagesCount; // 生成的工作薄个数

		// EmpExecutionContext.info("工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无操作员数据！");
		}
		
		//国际化
		String oper = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_109", request);
		String grqx = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_101", request);
		String qiyong = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_18", request);
		String zhuxiao = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_84", request);
		String suoding = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_85", request);
		String jinyong = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_19", request);
		String yes = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_34", request);
		String no = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_33", request);
		String wu = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_83", request);
		
		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = excelPath + File.separator + voucherPath
			+ File.separator + "sysUser" + File.separator
			+ sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if(!fileTemp.exists()){
			fileTemp.mkdirs();
		}
		String filePath = null;
		String fileName = "";
		
		XSSFWorkbook workbook = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			for (int f = 0; f < size; f++) {
				// 报表文件名
				fileName = "Sysuser_" + sdf.format(curDate)+"[" +(f+1)+ "]_"+ StaticValue.getServerNumber() +".xlsx";
				InputStream in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle =setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, oper);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				

				int index = 0;
				XSSFCell[] cell = new XSSFCell[12];

				if (f > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
				{
					userList = getSysuserVo(lsv, userId, depId, roleId,
							pageInfo);
				}

				pageInfo.setPageIndex(f + 2);// 定位下一页
				for (int k = 0; k < userList.size(); k++) {

					LfSysuserVo sysVo = userList.get(k);

					// 操作员ID
					String userName = sysVo.getUserName();
					// 操作员
					String uName = sysVo.getName();
					// 操作员编号
					String userCode = sysVo.getUserCode();
					// 角色
					String roleName = "";
					for (int r = 0; r < sysVo.getRoleList().size(); r++) {

						LfRoles rol2 = (LfRoles) sysVo.getRoleList().get(r);

						roleName += rol2.getRoleName() + ",";
					}
					if (!"".equals(roleName)) {
						roleName = roleName.substring(0, roleName
								.lastIndexOf(","));
					}
					// 所属机构
					String depName = sysVo.getDepName();
					// 数据权限
					String permissionType = "";
					if (sysVo.getPermissionType() == 1) {
						permissionType = grqx;
					} else {
						if (sysVo.getDomDepList() != null
								&& sysVo.getDomDepList().size() > 0) {
							LfDep dep = (LfDep) sysVo.getDomDepList().get(0);
							permissionType += dep.getDepName();
						}
					}
					Integer userType = sysVo.getUserType();
					String isEmployee = userType==2?yes:no;
					// 创建时间
					String createTime = "";
					if (sysVo.getRegTime() == null) {
						createTime = "-";
					} else {
						createTime = df.format(sysVo.getRegTime());
					}
					// 开户人
					String Holder = null == sysVo.getHolder() ? "" : sysVo
							.getHolder();
					// 状态
					String status = "";
					if (sysVo.getUserState() - 1 == 0) {
						status = qiyong;
					} else if (sysVo.getUserState() - 0 == 0) {
						status = jinyong;
					} else if (sysVo.getUserState() - 2 == 0) {
						status = zhuxiao;
					}else if (sysVo.getUserState() - 3 == 0) {//2013-12-27日新增加上锁定状态 may add
						status = suoding;
					}
					// 尾号
					String subNo = wu;
					if (sysVo.getIsExistSubno() != null
							&& sysVo.getIsExistSubno() == 1) {
						subNo = sysVo.getUsedSubno() != null ? sysVo
								.getUsedSubno() : wu;
					}
					String mobile = sysVo.getMobile();
					mobile = mobile == null?"":mobile;
					row = sheet.createRow(k+1);
					
					cell[0] = row.createCell(0);
					cell[1] = row.createCell(1);
					cell[2] = row.createCell(2);
					cell[3] = row.createCell(3);
					cell[4] = row.createCell(4);
					cell[5] = row.createCell(5);
					cell[6] = row.createCell(6);
					cell[7] = row.createCell(7);
					cell[8] = row.createCell(8);
					cell[9] = row.createCell(9);
					cell[10] = row.createCell(10);
					cell[11] = row.createCell(11);
					
					
					cell[0].setCellStyle(cellStyle);
					cell[1].setCellStyle(cellStyle);
					cell[2].setCellStyle(cellStyle);
					cell[3].setCellStyle(cellStyle);
					cell[4].setCellStyle(cellStyle);
					cell[5].setCellStyle(cellStyle);
					cell[6].setCellStyle(cellStyle);
					cell[7].setCellStyle(cellStyle);
					cell[8].setCellStyle(cellStyle);
					cell[9].setCellStyle(cellStyle);
					cell[10].setCellStyle(cellStyle);
					cell[11].setCellStyle(cellStyle);
					
					cell[0].setCellValue(userName);
					cell[1].setCellValue(uName);
					cell[2].setCellValue(mobile);
					cell[3].setCellValue(userCode);
					cell[4].setCellValue(depName);
					cell[5].setCellValue(permissionType);
					cell[6].setCellValue(isEmployee);
					cell[7].setCellValue(createTime);
					cell[8].setCellValue(Holder);
					cell[9].setCellValue(subNo);
					cell[10].setCellValue(roleName);
					cell[11].setCellValue(status);
				
					// 一页里的行数
					index++;
				}
				OutputStream os = new FileOutputStream(voucherFilePath
						+ File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
				// 清除对象
				os.close();
				in.close();
				workbook = null;
			}

			fileName = oper + sdf.format(curDate) + ".zip";
			filePath = excelPath + File.separator + voucherPath + File.separator
					+ "sysUser" + File.separator + fileName;
			ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = FileUtils.deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"导出操作员信息失败！");
		} 
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	// 将设置单元格属性提取出来成一个方法，方便其他模块调用
	public static XSSFCellStyle setCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();

		XSSFFont font = workbook.createFont();
		// 字体名称
		font.setFontName("TAHOMA");
		// 粗体
		font.setBold(false);
		// 下环线
		font.setUnderline(FontUnderline.NONE);
		// 字体大小
		font.setFontHeight(11);
		cellStyle.setFont(font);
		// 水平对齐
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		// 竖直对齐
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setWrapText(true);

		return cellStyle;
	}
	
	/**
	 *   新增 操作员  绑定关系， 并且绑定子号
	 * @param domId
	 * @param roleIds
	 * @param lfsysuser
	 * @param flow
	 * @param flowUsersMap
	 * @return
	 * @throws Exception
	 */
	
    public boolean add(Long domId, Long[] roleIds,
                       LfSysuser lfsysuser, LfEmployee employee, String[] employDepId, String[] clientDepId) throws Exception {
		boolean resultMsg = false;
		Long userId = 0L;
		//获取数据库连接
		Connection conn=null;
		try {
			conn = empTransDao.getConnection();
			//开启事务
			empTransDao.beginTransaction(conn);
			userId = empTransDao.saveObjProReturnID(conn, lfsysuser);
			if(userId==null){
				return false;
			}
			lfsysuser.setUserId(userId);
			if(domId != null){
				DepBiz depBiz = new DepBiz();
				LfDep dep=empDao.findObjectByID(LfDep.class, domId);
				List<Long> depIdList = depBiz.getChildDepIds(domId,dep.getCorpCode());
				for (int index = 0; index < depIdList.size(); index++) {
					LfDomination lfDomination = new LfDomination();
					lfDomination.setDepId(depIdList.get(index));
					lfDomination.setUserId(userId);
					empTransDao.save(conn, lfDomination);
				}
			}
			
			for (int i = 0; i < roleIds.length; i++) {
				LfRoles lfRoles = empDao.findObjectByID(
						LfRoles.class, roleIds[i]);
				LfUser2role user2role = new LfUser2role();
				user2role.setRoleId(lfRoles.getRoleId());
				user2role.setUserId(userId);
				empTransDao.save(conn, user2role);
			}
			if(employee != null){
				empTransDao.save(conn, employee);
			}
		
			if(employDepId != null && employDepId.length >0){
				List<LfEmpDepConn> empList = new ArrayList<LfEmpDepConn>();
				LfEmpDepConn empDepConn = null;
				for (int i = 0; i < employDepId.length; i++){
					if(!"".equals(employDepId[i])){
						empDepConn = new LfEmpDepConn();
						empDepConn.setDepId(Long.valueOf(employDepId[i]));
						empDepConn.setUserId(userId);
						empDepConn.setDepCodeThird(employDepId[i]);
						empList.add(empDepConn);
					}
				}
				empTransDao.save(conn, empList, LfEmpDepConn.class);
			}
			if(clientDepId != null && clientDepId.length >0){
				List<LfCliDepConn> clientList = new ArrayList<LfCliDepConn>();
				LfCliDepConn clientDepConn = null;
				for (int i = 0; i < clientDepId.length; i++){
					if(!"".equals(clientDepId[i])){
						clientDepConn = new LfCliDepConn();
						clientDepConn.setDepId(Long.valueOf(clientDepId[i]));
						clientDepConn.setUserId(userId);
						clientDepConn.setDepCodeThird(clientDepId[i]);
						clientList.add(clientDepConn);
					}
				}
				empTransDao.save(conn, clientList, LfCliDepConn.class);
			}
			//提交事务
			empTransDao.commitTransaction(conn);
			resultMsg = true;
		} catch (Exception e) {
			resultMsg = false;
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			throw e;
		} finally {
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return resultMsg;
	}
	
	/**
	 *   修改 操作员  绑定关系， 并且绑定子号
	 * @param domId
	 * @param roleIds
	 * @param lfsysuser
	 * @param flow
	 * @param flowUsersMap
	 * @return
	 * @throws Exception
	 */
	
    public boolean update(Long domId,
                          Long[] roleIds, LfSysuser lfSysuser, LfEmployee employee, String[] employDepId, String[] clientDepId) throws Exception {
		boolean result = false;
		//获取数据库连接
		Connection conn = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		Long userId = null;
		try {
			conn = empTransDao.getConnection();
			LfDep dep = null;
			List<Long> depIdList = null;
			if(domId != null){
				//添加操作员机构管辖权限
				DepBiz  depBiz = new DepBiz();
				dep = empDao.findObjectByID(LfDep.class, domId);
				if(dep != null){
					depIdList= depBiz.getChildDepIds(domId,dep.getCorpCode());
				}
			}
			//开启事务
			empTransDao.beginTransaction(conn);
			if(lfSysuser != null &&(lfSysuser.getUserId()==null || lfSysuser.getUserId() == 0L)){
				userId = empTransDao.saveObjProReturnID(conn, lfSysuser);
				lfSysuser.setUserId(userId);
			}else if(lfSysuser != null){
				empTransDao.update(conn, lfSysuser);
				userId = lfSysuser.getUserId();
			}
			
			if(employee != null && (employee.getEmployeeId() == null || employee.getEmployeeId() == 0L) ){
				//如果之前不是员工，则新增记录
				empTransDao.save(conn, employee);
			//更新已关联的员工信息
			}else if(employee != null ){
				if(lfSysuser!=null&&employee.getIsOperator().intValue()==0){
					LinkedHashMap<String, String> cond=new LinkedHashMap<String, String>();
					cond.put("guId", lfSysuser.getGuId().toString());
					empTransDao.delete(conn, LfEmployee.class, cond);
				}else{
					empTransDao.update(conn, employee);
				}
			}
			if(userId != null){
				conditionMap.put("userId", userId.toString());
				//删除操作员机构管辖权限
				int delDominNum = empTransDao.delete(conn, LfDomination.class,conditionMap);
				empTransDao.delete(conn, LfEmpDepConn.class,conditionMap);
				empTransDao.delete(conn, LfCliDepConn.class,conditionMap);
				if(domId != null){
					//添加操作员机构管辖权限
					/*DepBiz depBiz = new DepBiz();
					LfDep dep=empDao.findObjectByID(LfDep.class, domId);
					List<Long> depIdList = depBiz.getChildDepIds(domId,dep.getCorpCode());*/
					if (delDominNum >= 0 ) {
						for (int index = 0; index < depIdList.size(); index++) {
							LfDomination lfDomination = new LfDomination();
							lfDomination.setDepId(depIdList.get(index));
							lfDomination.setUserId(userId);
							empTransDao.save(conn, lfDomination);
						}
					}
				}
				if(employDepId != null && employDepId.length >0){
					List<LfEmpDepConn> empList = new ArrayList<LfEmpDepConn>();
					LfEmpDepConn empDepConn = null;
					for (int i = 0; i < employDepId.length; i++){
						if(!"".equals(employDepId[i])){
							empDepConn = new LfEmpDepConn();
							empDepConn.setDepId(Long.valueOf(employDepId[i]));
							empDepConn.setUserId(userId);
							empDepConn.setDepCodeThird(employDepId[i]);
							empList.add(empDepConn);
						}
					}
					empTransDao.save(conn, empList, LfEmpDepConn.class);
				}
				if(clientDepId != null && clientDepId.length >0){
					List<LfCliDepConn> clientList = new ArrayList<LfCliDepConn>();
					LfCliDepConn clientDepConn = null;
					for (int i = 0; i < clientDepId.length; i++){
						if(!"".equals(clientDepId[i])){
							clientDepConn = new LfCliDepConn();
							clientDepConn.setDepId(Long.valueOf(clientDepId[i]));
							clientDepConn.setUserId(userId);
							clientDepConn.setDepCodeThird(clientDepId[i]);
							clientList.add(clientDepConn);
						}
					}
					empTransDao.save(conn, clientList, LfCliDepConn.class);
				}
				
				if (roleIds[0] != -1) {
					if(lfSysuser.getUserId() != null){
						conditionMap.clear();
		
						RoleBiz roleBiz = new RoleBiz();
						String strRoleIds = roleBiz.getRoleIdByUserIdNoAdmin(userId);
						//当操作员存在角色列表时 删除原有角色
						if(strRoleIds!=null&&!"".equals(strRoleIds.trim())){
							conditionMap.put("userId", String
									.valueOf(userId));
							conditionMap.put("roleId", strRoleIds);
							//删除用户角色表中数据
							int delU2Num = empTransDao.delete(conn, LfUser2role.class,conditionMap);
						}
					}
					//添加角色
						for (int i = 0; i < roleIds.length; i++) {
							LfRoles lfRoles = empDao.findObjectByID(
									LfRoles.class, roleIds[i]);
							LfUser2role user2role = new LfUser2role();
							user2role.setRoleId(lfRoles.getRoleId());
							user2role.setUserId(userId);
							empTransDao.save(conn, user2role);
						}
				}
			}
			//提交事务
			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e) {
			result = false;
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "修改操作员绑定关系发生异常！");
			throw e;
		} finally {
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
	/**
	 *   新增 操作员  绑定关系， 并且绑定子号  带有存入充值回收权限机构表的重载方法
	 * @param domId
	 * @param roleIds
	 * @param lfsysuser
	 * @param flow
	 * @param flowUsersMap
	 * @return
	 * @throws Exception
	 */
	public boolean add(Long domId, Long[] roleIds,
			LfSysuser lfsysuser,LfEmployee employee,String[] employDepId,String[] clientDepId,String[] balanceDepId,Long opUserId) throws Exception {
		boolean resultMsg = false;
		Long userId = 0L;
		//充值权限类型  默认为0
		Integer type = 0;
		//获取数据库连接
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			//开启事务
			empTransDao.beginTransaction(conn);
			userId = empTransDao.saveObjProReturnID(conn, lfsysuser);
			if(userId==null){
				return false;
			}
			lfsysuser.setUserId(userId);
			if(domId != null){
				DepBiz depBiz = new DepBiz();
				LfDep dep=empDao.findObjectByID(LfDep.class, domId);
				List<Long> depIdList = depBiz.getChildDepIds(domId,dep.getCorpCode());
				for (int index = 0; index < depIdList.size(); index++) {
					LfDomination lfDomination = new LfDomination();
					lfDomination.setDepId(depIdList.get(index));
					lfDomination.setUserId(userId);
					empTransDao.save(conn, lfDomination);
				}
			}
			
			for (int i = 0; i < roleIds.length; i++) {
				LfRoles lfRoles = empDao.findObjectByID(
						LfRoles.class, roleIds[i]);
				LfUser2role user2role = new LfUser2role();
				user2role.setRoleId(lfRoles.getRoleId());
				user2role.setUserId(userId);
				empTransDao.save(conn, user2role);
			}
			if(employee != null){
				empTransDao.save(conn, employee);
			}
		
			if(employDepId != null && employDepId.length >0){
				List<LfEmpDepConn> empList = new ArrayList<LfEmpDepConn>();
				LfEmpDepConn empDepConn = null;
				for (int i = 0; i < employDepId.length; i++){
					if(!"".equals(employDepId[i])){
						empDepConn = new LfEmpDepConn();
						empDepConn.setDepId(Long.valueOf(employDepId[i]));
						empDepConn.setUserId(userId);
						empDepConn.setDepCodeThird(employDepId[i]);
						empList.add(empDepConn);
					}
				}
				empTransDao.save(conn, empList, LfEmpDepConn.class);
			}
			if(clientDepId != null && clientDepId.length >0){
				List<LfCliDepConn> clientList = new ArrayList<LfCliDepConn>();
				LfCliDepConn clientDepConn = null;
				for (int i = 0; i < clientDepId.length; i++){
					if(!"".equals(clientDepId[i])){
						clientDepConn = new LfCliDepConn();
						clientDepConn.setDepId(Long.valueOf(clientDepId[i]));
						clientDepConn.setUserId(userId);
						clientDepConn.setDepCodeThird(clientDepId[i]);
						clientList.add(clientDepConn);
					}
				}
				empTransDao.save(conn, clientList, LfCliDepConn.class);
			}
			
			// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
			//存入充值回收权限表
			if(balanceDepId != null && balanceDepId.length >0){
				List<LfBalancePri> balanceList = new ArrayList<LfBalancePri>();
				LfBalancePri balancPri = null;
				for (int i = 0; i < balanceDepId.length; i++){
					if(!"".equals(balanceDepId[i])){
						balancPri = new LfBalancePri();
						balancPri.setUserId(userId);
						balancPri.setDepId(Long.valueOf(balanceDepId[i].trim()));
						balancPri.setType(type);
						balancPri.setCreateUserId(opUserId);
						balancPri.setCorpCode(lfsysuser.getCorpCode());
						balancPri.setCreateTime(new Timestamp(System.currentTimeMillis()));
						balanceList.add(balancPri);
					}
				}
				empTransDao.save(conn, balanceList, LfBalancePri.class);
			}
			// end
			
			// 100000企业操作员设置开启手机登录验证
			setPhoneVerify(lfsysuser, conn);
			
			//提交事务
			empTransDao.commitTransaction(conn);
			resultMsg = true;
		} catch (Exception e) {
			resultMsg = false;
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			throw e;
		} finally {
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return resultMsg;
	}

	/**
	 * 
	 * @description 多企业,则100000企业操作员设置默认开启手机登录验证
	 * @param sysUser 新建的操作员对象
	 * @param conn 数据库连接
	 * @return 成功返回true；非100000企业的操作员返回true；异常返回false，但不做回滚，不影响新建流程
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2015-12-28 上午10:00:35
	 */
	private boolean setPhoneVerify(LfSysuser sysUser, Connection conn)
	{
		//只有多企业才干这个事
		if(StaticValue.getCORPTYPE() != 1)
		{
			return true;
		}
		if(sysUser == null)
		{
			EmpExecutionContext.error("新建操作员，设置默认开启手机登录验证，传入的操作员为空。");
			return false;
		}
		if(sysUser.getCorpCode() == null || sysUser.getCorpCode().trim().length() < 1)
		{
			EmpExecutionContext.error("新建操作员，设置默认开启手机登录验证，传入的操作员的企业编码为空。guid="+sysUser.getGuId()+",userId="+sysUser.getUserId());
			return false;
		}
		//100000企业新建操作员才默认开启动态口令
		if(!"100000".equals(sysUser.getCorpCode()))
		{
			return true;
		}
		if(sysUser.getGuId() == null)
		{
			EmpExecutionContext.error("新建操作员，设置默认开启手机登录验证，传入的操作员的GUID为空。corpCode="+sysUser.getCorpCode()+",userId="+sysUser.getUserId());
			return false;
		}
		
		try
		{
			LfMacIp lfMacIp = new LfMacIp();
			lfMacIp.setGuid(sysUser.getGuId());
			//是否启用动态口令(0是不启用,1是启用)
			lfMacIp.setDtpwd(1);
			
			boolean result = empTransDao.save(conn, lfMacIp);
			if(result)
			{
				EmpExecutionContext.info("新建操作员，设置默认开启手机登录验证，成功。guid="+sysUser.getGuId()+",corpCode="+sysUser.getCorpCode()+",userId="+sysUser.getUserId());
			}
			else
			{
				EmpExecutionContext.error("新建操作员，设置默认开启手机登录验证，失败。guid="+sysUser.getGuId()+",corpCode="+sysUser.getCorpCode()+",userId="+sysUser.getUserId());
			}
			return result;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "新建操作员，设置默认开启手机登录验证，异常。guid="+sysUser.getGuId()+",corpCode="+sysUser.getCorpCode()+",userId="+sysUser.getUserId());
			return false;
		}
	}
	
	/**
	 *   修改 操作员  绑定关系， 并且绑定子号    带有修改充值回收权限机构表的重载方法
	 * @param domId
	 * @param roleIds
	 * @param lfsysuser
	 * @param flow
	 * @param flowUsersMap
	 * @return
	 * @throws Exception
	 */
	public boolean update(Long domId,
			Long[] roleIds, LfSysuser lfSysuser,LfEmployee employee,String[] employDepId,String[] clientDepId, String[] balanceDepId, Long opUserId) throws Exception {
		boolean result = false;
		//充值权限类型  默认为0
		Integer type = 0;
		//获取数据库连接
		Connection conn = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		Long userId = null;
		try {
			conn = empTransDao.getConnection();
			LfDep dep = null;
			List<Long> depIdList = null;
			if(domId != null){
				//添加操作员机构管辖权限
				DepBiz  depBiz = new DepBiz();
				dep = empDao.findObjectByID(LfDep.class, domId);
				if(dep != null){
					depIdList= depBiz.getChildDepIds(domId,dep.getCorpCode());
				}
			}
			//开启事务
			empTransDao.beginTransaction(conn);
			if(lfSysuser != null &&(lfSysuser.getUserId()==null || lfSysuser.getUserId() == 0L)){
				userId = empTransDao.saveObjProReturnID(conn, lfSysuser);
				lfSysuser.setUserId(userId);
			}else if(lfSysuser != null){
				empTransDao.update(conn, lfSysuser);
				userId = lfSysuser.getUserId();
			}
			
			if(employee != null && (employee.getEmployeeId() == null || employee.getEmployeeId() == 0L) ){
				//如果之前不是员工，则新增记录
				empTransDao.save(conn, employee);
			//更新已关联的员工信息
			}else if(employee != null ){
				if(lfSysuser!=null&&employee.getIsOperator().intValue()==0){
					LinkedHashMap<String, String> cond=new LinkedHashMap<String, String>();
					cond.put("guId", lfSysuser.getGuId().toString());
					empTransDao.delete(conn, LfEmployee.class, cond);
				}else{
					empTransDao.update(conn, employee);
				}
			}
			if(userId != null){
				conditionMap.put("userId", userId.toString());
				//删除操作员机构管辖权限
				int delDominNum = empTransDao.delete(conn, LfDomination.class,conditionMap);
				empTransDao.delete(conn, LfEmpDepConn.class,conditionMap);
				empTransDao.delete(conn, LfCliDepConn.class,conditionMap);
				empTransDao.delete(conn, LfBalancePri.class,conditionMap);
				if(domId != null){
					//添加操作员机构管辖权限
					/*DepBiz depBiz = new DepBiz();
					LfDep dep=empDao.findObjectByID(LfDep.class, domId);
					List<Long> depIdList = depBiz.getChildDepIds(domId,dep.getCorpCode());*/
					if (delDominNum >= 0 ) {
						for (int index = 0; index < depIdList.size(); index++) {
							LfDomination lfDomination = new LfDomination();
							lfDomination.setDepId(depIdList.get(index));
							lfDomination.setUserId(userId);
							empTransDao.save(conn, lfDomination);
						}
					}
				}
				if(employDepId != null && employDepId.length >0){
					List<LfEmpDepConn> empList = new ArrayList<LfEmpDepConn>();
					LfEmpDepConn empDepConn = null;
					for (int i = 0; i < employDepId.length; i++){
						if(!"".equals(employDepId[i])){
							empDepConn = new LfEmpDepConn();
							empDepConn.setDepId(Long.valueOf(employDepId[i]));
							empDepConn.setUserId(userId);
							empDepConn.setDepCodeThird(employDepId[i]);
							empList.add(empDepConn);
						}
					}
					empTransDao.save(conn, empList, LfEmpDepConn.class);
				}
				if(clientDepId != null && clientDepId.length >0){
					List<LfCliDepConn> clientList = new ArrayList<LfCliDepConn>();
					LfCliDepConn clientDepConn = null;
					for (int i = 0; i < clientDepId.length; i++){
						if(!"".equals(clientDepId[i])){
							clientDepConn = new LfCliDepConn();
							clientDepConn.setDepId(Long.valueOf(clientDepId[i]));
							clientDepConn.setUserId(userId);
							clientDepConn.setDepCodeThird(clientDepId[i]);
							clientList.add(clientDepConn);
						}
					}
					empTransDao.save(conn, clientList, LfCliDepConn.class);
				}
				
				// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
				//存入充值回收权限表
				if(balanceDepId != null && balanceDepId.length >0){
					List<LfBalancePri> balanceList = new ArrayList<LfBalancePri>();
					LfBalancePri balancPri = null;
					for (int i = 0; i < balanceDepId.length; i++){
						if(!"".equals(balanceDepId[i])){
							balancPri = new LfBalancePri();
							balancPri.setUserId(userId);
							balancPri.setDepId(Long.valueOf(balanceDepId[i].trim()));
							balancPri.setType(type);
							balancPri.setCreateUserId(opUserId);
							balancPri.setCorpCode(lfSysuser.getCorpCode());
							balancPri.setCreateTime(new Timestamp(System.currentTimeMillis()));
							balanceList.add(balancPri);
						}
					}
					empTransDao.save(conn, balanceList, LfBalancePri.class);
				}
				// end
				
				
				if (roleIds[0] != -1) {
					if(lfSysuser.getUserId() != null){
						conditionMap.clear();
		
						RoleBiz roleBiz = new RoleBiz();
						String strRoleIds = roleBiz.getRoleIdByUserIdNoAdmin(userId);
						//当操作员存在角色列表时 删除原有角色
						if(strRoleIds!=null&&!"".equals(strRoleIds.trim())){
							conditionMap.put("userId", String
									.valueOf(userId));
							conditionMap.put("roleId", strRoleIds);
							//删除用户角色表中数据
							int delU2Num = empTransDao.delete(conn, LfUser2role.class,conditionMap);
						}
					}
					//添加角色
						for (int i = 0; i < roleIds.length; i++) {
							LfRoles lfRoles = empDao.findObjectByID(
									LfRoles.class, roleIds[i]);
							LfUser2role user2role = new LfUser2role();
							user2role.setRoleId(lfRoles.getRoleId());
							user2role.setUserId(userId);
							empTransDao.save(conn, user2role);
						}
				}
			}
			//提交事务
			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e) {
			result = false;
			//事务回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "修改操作员绑定关系发生异常！");
			throw e;
		} finally {
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	public String getFirstLevelChildDepIds(Long depId){
		try {
			LfDep dep=empDao.findObjectByID(LfDep.class, depId);
			//获得所属机构的所有第一级子机构
			List<LfDep> firstLevelChildDeps = new SysuserDAO().getFirstLevelChildDeps(depId,dep.getCorpCode());
			StringBuffer sb = new StringBuffer();
			for (LfDep firstLevelChildDep : firstLevelChildDeps) {
				sb.append(firstLevelChildDep.getDepId()).append(",");
			}
			//去掉最后一个逗号
			if(sb.length() > 0){
				sb.deleteCharAt(sb.length()-1); 
			}
			return sb.toString();
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"获取操作员所属机构子机构异常!");
			return null;
		}
	}
	
	// end
}
