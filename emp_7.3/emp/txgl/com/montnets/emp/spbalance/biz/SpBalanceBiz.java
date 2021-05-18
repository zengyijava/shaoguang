package com.montnets.emp.spbalance.biz;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SpUserBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasgroup.Userfee;
import com.montnets.emp.entity.sysuser.LfDepUserBalance;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.servmodule.txgl.entity.LfSpFeeAlarm;
import com.montnets.emp.servmodule.txgl.entity.LfSpFeeLog;
import com.montnets.emp.servmodule.txgl.table.TableUserdata;
import com.montnets.emp.spbalance.dao.SpBalanceDAO;
import com.montnets.emp.spbalance.vo.LfSpFeeAlarmVo;
import com.montnets.emp.spbalance.vo.UserfeeVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.SuperOpLog;

public class SpBalanceBiz extends SuperBiz
{

	protected SuperOpLog	spLog		= new SuperOpLog();

	protected String		opModule	= StaticValue.SMS_BOX;

	protected String		opType		= StaticValue.OTHER;

	private final SpUserBiz spUserBiz = new SpUserBiz();

	public List<DynaBean> getDynaBeanList(List<DynaBean> ufdynlist, Long userId){
		List<DynaBean> dynaBeanList = new ArrayList<DynaBean>();
		try {
			String spUserStr = spUserBiz.getAllSpUser(userId.toString());
			if(StringUtils.IsNullOrEmpty(spUserStr)){
				EmpExecutionContext.info("当前操作员无关联的SP账号");
				return dynaBeanList;
			}
			for(String tempSpUser:spUserStr.split(",")){
				for(DynaBean dynaBean:ufdynlist){
					if(tempSpUser.equalsIgnoreCase("'"+dynaBean.get("userid").toString()+"'")){
						dynaBeanList.add(dynaBean);
					}
				}
			}
		}catch (Exception e){
			EmpExecutionContext.error(e,"根据SP账号绑定关系更改查询结果");
		}
		return dynaBeanList;
	}

	/**
	 * 新增充值/回收日志
	 *
	 * @description
	 * @param log
	 * @return
	 * @throws Exception
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-13 下午02:49:06
	 */
	public boolean addBalanceLog(LfSpFeeLog log) throws Exception
	{
		boolean returnFlag = false;
		try
		{
			log.setOprtime(new Timestamp(System.currentTimeMillis()));
			returnFlag = empDao.save(log);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号充值操作日志入库失败！");
		}
		return returnFlag;
	}

	/**
	 * 获取money
	 * 
	 * @description
	 * @param lgcorpcode
	 *        企业编码
	 * @return
	 * @throws Exception
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-13 下午04:23:24
	 */
	public HashMap<String, Long> getMoney(String lgcorpcode) throws Exception
	{
		HashMap<String, Long> hm = new HashMap<String, Long>();
		// 查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		// 企业编码
		conditionMap.put("corpCode", lgcorpcode);
		// 机构余额
		List<LfDepUserBalance> list = new BaseBiz().getByCondition(LfDepUserBalance.class, conditionMap, orderbyMap);
		// 短信发送条数
		Long smsCount = 0L;
		// 彩信发送条数
		Long mmsCount = 0L;
		if(list != null && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				LfDepUserBalance ba = list.get(i);
				Long sms = ba.getSmsBalance();
				smsCount = sms + smsCount;
				Long mms = ba.getMmsBalance();
				mmsCount = mms + mmsCount;
			}
		}
		// 短信条数
		hm.put("sms", smsCount);
		// 彩信条数
		hm.put("mms", mmsCount);
		return hm;
	}

	/**
	 * 查询预付费SP账号充值与回收信息
	 * 
	 * @description
	 * @param userfeevo
	 *        查询条件
	 * @param pageInfo
	 *        分页对象
	 * @return
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-12 上午09:20:58
	 */
	public List<DynaBean> getSpBalanceByYff(UserfeeVo userfeevo, PageInfo pageInfo) throws Exception
	{
		List<DynaBean> list = null;
		try
		{
			list = new SpBalanceDAO().findSpBalancesByYff(userfeevo, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询预付费SP账号充值与回收信息biz异常");
			throw e;
		}
		return list;
	}

	/**
	 * 设置阀值通知人，获取告警次数
	 * 
	 * @description
	 * @param useridstr
	 * @return
	 * @throws Exception
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-14 下午12:54:29
	 */
	public int getAlermCountBySpuser(String useridstr) throws Exception
	{
		// 定义告警次数初始值
		int alermcount = 0;
		try
		{
			// 查询sp账号相关通知人list 获得告警次数
			LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
			conditionmap.put("spuser", useridstr);
			LinkedHashMap<String, String> ordermap = new LinkedHashMap<String, String>();
			ordermap.put("alarmedcount", "desc");
			List<LfSpFeeAlarm> spfealarms = empDao.findListByCondition(LfSpFeeAlarm.class, conditionmap, ordermap);
			// 判断查询结果是否有数据
			if(spfealarms != null && spfealarms.size() > 0 && spfealarms.get(0) != null && spfealarms.get(0).getAlarmedcount() != null)
			{
				alermcount = spfealarms.get(0).getAlarmedcount();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置阀值通知人，获取SP账号余额biz异常");
			throw e;
		}
		return alermcount;
	}

	
	/**
	 * 设置阀值通知人，获取余额
	 * @description    
	 * @param useridstr
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-26 上午09:27:31
	 */
	public long getSendBalanceBySpuser(String useridstr) throws Exception
	{
		// 定义余额初始值
		long balancecount = 0;
		try
		{
			// 查询sp账号相关通知人list 获得告警次数
			LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
			conditionmap.put("userId", useridstr);
			LinkedHashMap<String, String> ordermap = new LinkedHashMap<String, String>();
			ordermap.put("sendNum", "desc");
			List<Userfee> userfees = empDao.findListByCondition(Userfee.class, conditionmap, ordermap);
			// 判断查询结果是否有数据
			if(userfees != null && userfees.size() > 0 && userfees.get(0) != null && userfees.get(0).getSendNum() != null)
			{
				balancecount = userfees.get(0).getSendNum();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置阀值通知人，获取告警次数biz异常");
			throw e;
		}
		return balancecount;
	}

	
	
	/**
	 * @description
	 * @param useridstr
	 * @return
	 * @throws Exception
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-13 下午09:00:05
	 */
	public List<DynaBean> getSpfeealearmBySpuser(String useridstr) throws Exception
	{
		List<DynaBean> list = null;
		try
		{
			list = new SpBalanceDAO().getSpfeealearmBySpuser(useridstr);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询sp账号告警相关通知人信息对应biz异常");
			throw e;
		}
		return list;
	}

	/**
	 * 获取账号对应的 告警人以及手机号码
	 * 
	 * @description
	 * @return
	 * @throws Exception
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-12 下午07:44:01
	 */
	public Map<String, List<LfSpFeeAlarmVo>> getSpFeeAlarmUserMap(String corpcode) throws Exception
	{
		// 定义查询返回的动态beanlist
		List<DynaBean> list = null;
		// 定义将构造key sp账号 values 通知人list 返回的map
		Map<String, List<LfSpFeeAlarmVo>> map = new HashMap<String, List<LfSpFeeAlarmVo>>();
		try
		{
			// 调用dao查询方法
			list = new SpBalanceDAO().findSpFeeAlarmUserMap(corpcode);
			List<LfSpFeeAlarmVo> spfvos = null;
			LfSpFeeAlarmVo spfvo = null;
			// 循环遍历告警人及手机好list 将其存入map
			for (DynaBean dynb : list)
			{
				// 判断sp账号 通知人姓名 通知人手机号 是否为空
				String spUser = dynb.get("spuser")!=null?dynb.get("spuser").toString().trim():"";
				if(dynb != null && spUser.length()>0 && dynb.get("noticename") != null && dynb.get("alarmphone") != null)
				{
					// 实例化通知人信息对象
					spfvo = new LfSpFeeAlarmVo();
					// 设置通知人号码
					spfvo.setAlarmphone(dynb.get("alarmphone").toString());
					// 设置通知人姓名
					spfvo.setNoticename(dynb.get("noticename").toString());
					// 判断map中是否已经存过key 为此账号
					if(map.containsKey(spUser))
					{
						spfvos = map.get(spUser);
					}
					else
					{
						spfvos = new ArrayList<LfSpFeeAlarmVo>();
					}
					spfvos.add(spfvo);
					map.put(spUser, spfvos);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询预付费SP账号通知人信息biz异常");
			throw e;
		}
		return map;
	}

	/**
	 * 充值
	 * 
	 * @param lfSysuser
	 *        操作员
	 * @param spuser
	 *        充值目的SPUSER
	 * @param count
	 *        充值总数
	 * @return 返回值信息
	 *         0:充值成功
	 *         -1:充值失败
	 *         -2:SP账号充值数不能为空或者为0
	 *         -9999:短信充值接口调用异常
	 */
	public int addSpUserBalance(LfSysuser lfSysuser, String spuser, Long count) throws Exception {
		return setBalanceNumber(lfSysuser, spuser, count, 1);
	}

	/**
	 * 回收
	 * 
	 * @param lfSysuser 操作员
	 * @param spuser sp账号
	 * @param count 充值总数
	 * @return 返回值信息
	 *         0:回收成功
	 *         -1:回收失败
	 *         -2 SP账号回收数不能为空或者为0
	 *         -5 回收余额数大于SP账号可分配数
	 *         -6 获取SP账号余额记录失败
	 *         -7 SP账号没有进行充值过
	 *         -9999:短信回收接口调用异常
	 */
	public int recSpuserBalance(LfSysuser lfSysuser, String spuser, Long count) throws Exception {
		return setBalanceNumber(lfSysuser, spuser, count, 2);

	}

	/**
	 * SP账号充值、回收接口
	 * 
	 * @param lfSysuser
	 *        操作员
	 * @param spuser
	 *        充值目的spuser
	 * @param count
	 *        充值总数
	 * @param oprType
	 *        操作类型:1:充值 2:回收
	 * @return
	 */
	private int setBalanceNumber(LfSysuser lfSysuser, String spuser, Long count, int oprType) throws Exception
	{
		// 返回值,默认为-1:短信充值失败
		int result = -1;
		// 操作类型参数合法,lfSysuser对象不为空,调用存储过程, 操作类型1:充值2:回收
		if(lfSysuser != null && (oprType == 1 || oprType == 2))
		{
			String corpCode = lfSysuser.getCorpCode();
			String userName = lfSysuser.getUserName();
			Connection connection = null;
			CallableStatement cs = null;
			// 记录日志信息
			String oprInfo = "";
			// 操作状态
			String oprInfoStatu = "";
			switch (oprType)
			{
				case 1:
					oprInfo = "充值";
					oprInfoStatu = "成功，共充值费用：";
					break;
				case 2:
					oprInfo = "回收";
					oprInfoStatu = "成功，共回收费用：";
					break;
				default:
					break;
			}
			try
			{
				// 创建连接
				connection = empTransDao.getConnection();
				try
				{
					cs = connection.prepareCall("{call LF_SPUSERBALANCE(?,?,?,?)}");
					// sp账号
					cs.setString(1, spuser);
					// 充值或回收 条数
					cs.setLong(2, count);
					// 标识,1:充值 2:回收
					cs.setInt(3, oprType);
					// 返回状态码
					cs.registerOutParameter(4, java.sql.Types.INTEGER);
					cs.execute();
					// 返回值
					result = cs.getInt(4);
					// 充值成功
					if(oprType == 1 && result == 0)
					{
						//充值后的余额是否大于阀值
						int countnum = new SuperDAO().getInt("COUNT", "SELECT COUNT(*) COUNT FROM USERFEE WHERE SENDNUM>THRESHOLD AND USERID='" + spuser + "'", StaticValue.EMP_POOLNAME);
						if(countnum > 0)
						{
							boolean falg1 = false;
							LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
							LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
							objectMap.put("alarmedcount", "0");
							conditionMap.put("spuser", spuser);
							conditionMap.put("corpcode", corpCode);
							falg1 = empDao.update(LfSpFeeAlarm.class, objectMap, conditionMap);
							if(falg1)
							{
								EmpExecutionContext.info("更新告警次数为0成功！spuser:" + spuser + ",企业编码：" + corpCode);
							}
							else
							{
								EmpExecutionContext.error("更新告警次数为0失败！spuser:" + spuser + ",企业编码：" + corpCode);
							}
						}

					}

				}
				catch (SQLException e)
				{
					EmpExecutionContext.error(e, oprInfo + "失败!");
					result = -1;
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, oprInfo + "接口调用异常!");
				result = -9999;
			}
			finally
			{
				if(cs != null)
				{
					try
					{
						cs.close();
					}
					catch (SQLException e)
					{
						EmpExecutionContext.error(e, oprInfo + "接口关闭存储过程异常!");

					}
				}
				empTransDao.closeConnection(connection);

				try
				{
					// 写操作日志
					String opContent;
					if(result == 0)
					{
						opContent = oprInfo + oprInfoStatu + count;
						spLog.logSuccessString(userName, oprInfo, opType, opContent, corpCode);
						EmpExecutionContext.info("SP账号充值/回收", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "", lfSysuser.getUserName(), oprInfo, "OTHER");
					}
					else
					{
						opContent = "充值、回收接口调用异常。错误码：" + result;
						spLog.logFailureString(userName, oprInfo, opType, opContent, null, corpCode);
						EmpExecutionContext.info("SP账号充值/回收", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "", lfSysuser.getUserName(), oprInfo, "OTHER");
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "SP账号充值、回收接口调用写操作日志失败");
				}
			}
			return result;
		}
		else
		{
			String opContent = "充值、回收接口获取参数异常。错误码：" + result;
			spLog.logFailureString(lfSysuser.getUserName(), "SP账号充值、回收", opType, opContent, null, lfSysuser.getCorpCode());
			EmpExecutionContext.error("充值、回收接口接口获取参数异常，oprType:" + oprType + ";lfSysuser:" + lfSysuser);
			return result;
		}
	}

	/**
	 * 设置Sp账号告警阀值以及通知人信息
	 * 
	 * @description
	 * @param useridstr
	 *        SP账号
	 * @param lgcorpcode
	 *        企业编码
	 * @param threshold
	 *        告警阀值
	 * @param spfeealarms
	 *        通知人信息
	 * @return
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-14 下午02:00:41
	 */
	public int setAlarmAndNotice(String useridstr, String lgcorpcode, Integer threshold, List<LfSpFeeAlarm> spfeealarms)
	{
		// 创建连接
		Connection connection = null;
		int result = 0;
		try
		{
			connection = empTransDao.getConnection();
			empTransDao.beginTransaction(connection);
			// 修改值
			LinkedHashMap<String, String> setupdate = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
			setupdate.put("thresHold", threshold + "");
			conditionmap.put("userId", useridstr);
			empTransDao.update(connection, Userfee.class, setupdate, conditionmap);
			conditionmap.clear();
			conditionmap.put("spuser", useridstr);
			conditionmap.put("corpcode", lgcorpcode);
			// 先删除
			empTransDao.delete(connection, LfSpFeeAlarm.class, conditionmap);
			empTransDao.save(connection, spfeealarms, LfSpFeeAlarm.class);
			empTransDao.commitTransaction(connection);
			result = 1;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置Sp账号告警阀值以及通知人信息异常. useridstr" + useridstr + ",threshold=" + threshold + ",lgcorpcode=" + lgcorpcode);
			empTransDao.rollBackTransaction(connection);
		}
		finally
		{
			empTransDao.closeConnection(connection);
		}
		return result;
	}

	/**
	 * 批量充值/回收
	 * 
	 * @description
	 * @param sysuser
	 *        当前登录信息
	 * @param useridstrs
	 * @param addCount
	 *        充值或回收条数 充值正数 回收负数
	 * @return 返回信息
	 *         0:充值/回收成功
	 *         -1:充值/回收失败
	 *         -2 SP账号充值/回收数不能为空或者为0
	 *         -5 回收余额数大于SP账号可分配数
	 *         -6 获取SP账号余额记录失败
	 *         -7 SP账号没有进行充值过
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-14 下午07:23:45
	 */
	public int addordellBalanceAll(LfSysuser sysuser, String[] useridstrs, Long addCount, String bltype)
	{
		int returnNum = -1;
		// 获得一个链接
		Connection conn = null;
		String bltypestr = "";
		try
		{
			conn = empTransDao.getConnection();
			// 开启事务
			empTransDao.beginTransaction(conn);
			// 判断充值/回收数量大于0
			if(addCount == 0)
			{
				// 回滚事务
				empTransDao.rollBackTransaction(conn);
				returnNum = -2;
				EmpExecutionContext.error("SP账号扣费/回收条数不能为0，returnNum:" + returnNum);
				return returnNum;
			}
			// 充值
			if("1".equals(bltype))
			{
				bltypestr = "充值";
				returnNum = stratAddBalance(conn, sysuser, addCount, useridstrs);
			}
			else if("2".equals(bltype))
			{
				// 回收
				bltypestr = "回收";
				returnNum = stratDelBalance(conn, sysuser, addCount, useridstrs);
			}
			if(returnNum < 0)
			{
				// 回滚事务
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("批量" + bltypestr + "失败，returnNum:" + returnNum);
			}
			else
			{
				// 提交事务
				empTransDao.commitTransaction(conn);
			}

		}
		catch (Exception e)
		{
			// 回滚事务
			empTransDao.rollBackTransaction(conn);
			returnNum = -1;
			EmpExecutionContext.error(e, "SP账号批量"+bltypestr+"失败！");
		}
		finally
		{
			// 添加充值日志
			BalanceLog(sysuser, addCount, useridstrs, returnNum, bltype);
			// 关闭数据链接
			empTransDao.closeConnection(conn);
		}
		return returnNum;
	}

	/**
	 * 开始批量充值
	 * 
	 * @description
	 * @param conn
	 * @param sysuser
	 * @param addCount
	 * @param useridstrs
	 * @return
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-14 下午08:03:04
	 */
	private int stratAddBalance(Connection conn, LfSysuser sysuser, Long addCount, String[] useridstrs)
	{
		// 企业编码
		String corpCode = "";
		try
		{
			corpCode = sysuser.getCorpCode();
			for (String useridstr : useridstrs)
			{
				String sql = "UPDATE USERFEE SET SENDNUM=SENDNUM+" + addCount + " WHERE USERID=UPPER('" + useridstr + "')";
				// 0表示余额信息表无记录,新增记录
				if(empTransDao.executeBySQLReturnCount(conn, sql) == 0)
				{
					LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
					condition.put("userId", useridstr.toUpperCase());
					List<Userdata> userfees = empDao.findListByCondition(Userdata.class, condition, null);
					if(userfees != null && userfees.size() > 0 && userfees.get(0) != null)
					{
						sql = "INSERT INTO USERFEE (" + TableUserdata.UID + ",USERID,SENDNUM) VALUES (" + userfees.get(0).getUid() + "," + useridstr + "," + addCount + ")";
						if(empTransDao.executeBySQLReturnCount(conn, sql) == 0)
						{
							return -1;
						}
					}
					else
					{
						return -1;
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "批量充值异常！企业编码：" + corpCode);
			return -1;

		}
		return 0;
	}

	/**
	 * 开始批量回收
	 * 
	 * @description
	 * @param conn
	 * @param sysuser
	 * @param addCount
	 * @param useridstrs
	 * @return
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-14 下午08:03:04
	 */
	private int stratDelBalance(Connection conn, LfSysuser sysuser, Long addCount, String[] useridstrs)
	{
		// 企业编码
		String corpCode = "";
		try
		{
			corpCode = sysuser.getCorpCode();
			for (String useridstr : useridstrs)
			{
				String sql = "UPDATE USERFEE SET SENDNUM=SENDNUM-" + addCount + " WHERE SENDNUM>=" + addCount + " AND USERID=UPPER('" + useridstr + "') ";
				// 0表示余额信息表无记录,新增记录
				if(empTransDao.executeBySQLReturnCount(conn, sql) == 0)
				{
					LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
					condition.put("userId", useridstr.toUpperCase());
					List<Userdata> userfees = empDao.findListByCondition(Userdata.class, condition, null);
					if(userfees != null && userfees.size() > 0)
					{
						// 回收余额数大于SP账号可分配数
						EmpExecutionContext.error("SP账号回收额数大于SP账号可分配数!userid:"+useridstr);
						return -5;
					}
					else
					{
						// SP账号没有进行充值过
						EmpExecutionContext.error("回收SP账号没有进行充值过!userid:"+useridstr);
						return -7;
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "批量回收异常！企业编码：" + corpCode);
			return -1;

		}
		return 0;
	}

	/**
	 * 添加批量操作日志
	 * 
	 * @description
	 * @param sysuser
	 * @param addCount
	 * @param useridstrs
	 * @param returnmsg
	 * @param bltype
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-14 下午08:22:25
	 */
	public void BalanceLog(LfSysuser sysuser, Long addCount, String[] useridstrs, int returnmsg, String bltype)
	{
		// 企业编码
		String corpCode = "";
		String bltypestr = "";
		try
		{
			// 企业编码
			corpCode = sysuser.getCorpCode();
			int num = 0;
			// 充值数量
			if("1".equals(bltype))
			{
				num = 1;
				bltypestr = "充值";
			}
			else if("2".equals(bltype))
			{
				num = -1;
				bltypestr = "回收";
			}
			for (String useridstr : useridstrs)
			{
				LfSpFeeLog lfspfeelog = new LfSpFeeLog();
				// 企业编码
				lfspfeelog.setCorpcode(sysuser.getCorpCode());
				// 数量
				lfspfeelog.setIcount(addCount * num);
				// 备注
				lfspfeelog.setMemo(" ");
				// SPuser SP账号
				lfspfeelog.setSpuser(useridstr);
				// 当前登录操作员id
				lfspfeelog.setUserid(sysuser.getUserId());
				// 设值结果
				if(returnmsg == 0)
				{
					lfspfeelog.setResult(0);
				}
				else
				{
					lfspfeelog.setResult(1);
				}
				if(!addBalanceLog(lfspfeelog)){
					EmpExecutionContext.error("SP账号批量" + bltypestr + "操作日志保存失败！corpCode："+corpCode);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "添加SP账号批量"+bltypestr+"日志异常！corpCode："+corpCode);
		}
	}
	
	/**
	 * 查询sp账号的动态bean的list集合
	 * @description 
	 * @param conditionMap 查询条件
	 * @param pageInfo PageInfo分页对象
	 * @return List<DynaBean>
	 * @author lx <lxisno1@163.com>
	 * @throws Exception 
	 * @datetime 2016-10-15 下午12:00:14
	 */
	public List<DynaBean> getSPBalanceBean(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception{
		List<DynaBean> beanList = null;
		try {
			beanList = new SpBalanceDAO().findSPBalanceLogBean(conditionMap, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询SP账号充值日志异常！");
		}
		return	beanList;
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
