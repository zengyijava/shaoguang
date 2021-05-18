package com.montnets.emp.pasgroup.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasgroup.MrSpgateWatch;
import com.montnets.emp.entity.pasgroup.Userfee;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.pasgroup.dao.UserDataDao;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.GwUserproperty;
import com.montnets.emp.servmodule.txgl.entity.LfSpFee;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.util.PageInfo;

public class UserDataBiz extends SuperBiz{
	ErrorLoger errorLoger = new ErrorLoger();
	public List<Userdata> findSpUser(LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo) throws Exception{
		List<Userdata> userdatas = null;
		try
		{
			userdatas = new UserDataDao().findSpUser(conditionMap, orderbyMap,
					pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找账户异常！"));
			throw e;
		}
		return userdatas;
	}
	
	public List<Userdata> findSpUserByCorp(String corp,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<Userdata> xx = null;
		try
		{
			xx = new UserDataDao().findSpUserByCorp(corp, conditionMap, orderbyMap,
					pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找账户异常！"));
			// TODO: handle exception
			throw e;
		}
		return xx;
	}
	
	/**
	 *   进入新增短彩SP账户
	 * @return
	 * @throws Exception
	 */
	public List<Userdata> getAgentUserdata() throws Exception
	{
		return new UserDataDao().findAgentUserdata();
	}
	
	
	/**
	 * 通过账号名称和账号类型获取账户信息
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public Userdata getUserdataByUseridandActype(String userid,Integer accouttype) throws Exception
	{
		Userdata user = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("userId", userid);
			conditionMap.put("accouttype", accouttype==null?"":accouttype.toString());
			List<Userdata> tempList = empDao.findListByCondition(
					Userdata.class, conditionMap, null);
			if (tempList != null && tempList.size() > 0)
			{
				user = tempList.get(0);
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找账户异常！userid:"+userid+",accouttype:"+accouttype));
			throw e;
		}
		return user;
	}
	
	/**
	 * 通过账号获取SP账户信息
	 * @description    
	 * @param userid
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-9-9 上午08:46:27
	 */
	public boolean getUserdataByUserid(String userid) throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			//以账号为条件查询
			conditionMap.put("userId", userid);
			List<Userdata> tempList = empDao.findListByCondition(
					Userdata.class, conditionMap, null);
			//查询到记录，返回TRUE
			if (tempList != null && tempList.size() > 0)
			{
				return true;
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通过账号获取SP账户信息异常！userid:"+userid));
			throw e;
		}
		return false;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<GtPortUsed> getPortByUserId(String userId) throws Exception
	{
		List<GtPortUsed> portsList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("userId", userId);
			conditionMap.put("routeFlag&<>", "2");
			orderbyMap.put("spisuncm", StaticValue.ASC);
			portsList = empDao.findListBySymbolsCondition(GtPortUsed.class,
					conditionMap, orderbyMap);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找路由异常！"));
			throw e;
		}
		return portsList;
	}
	
	
	
	/**
	 * 
	 * @param spUserId
	 * @return
	 * @throws Exception
	 */
	public Integer delUserdataBySpUserId(String spUserId) throws Exception
	{
		Integer result = null;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);

			this.delDomDepBandByUserId(conn, spUserId);
			this.deleteUserfee(conn, spUserId);
			result = this.delUserdataBySpUserId(conn, spUserId);

			empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"删除账户异常！"));
			throw e;
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 
	 * @param conn
	 * @param spUsers
	 * @return
	 * @throws Exception
	 */
	private Integer delDomDepBandByUserId(Connection conn, String spUsers)
			throws Exception
	{
		Integer result = null;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("spUser", spUsers);
			result = empTransDao.delete(conn, LfSpDepBind.class, conditionMap);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"删除异常！"));
			throw e;
		}
		return result;
	}
	
	
	private Integer deleteUserfee(Connection conn, String spUserId)
	throws Exception
	{
		if (spUserId == null || "".equals(spUserId.trim()))
		{
			return null;
		}
		
		Integer result = 0;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("userId", spUserId);
			result = empTransDao.delete(conn, Userfee.class, conditionMap);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"删除计费异常！"));
			throw e;
		}
		return result;
	}
	
	private Integer delUserdataBySpUserId(Connection conn, String spUserId)
	throws Exception
	{
		if (spUserId == null || "".equals(spUserId.trim()))
		{
			return null;
		}
		
		Integer delnum = 0;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("userId", spUserId);
			delnum = empTransDao.delete(conn, Userdata.class, conditionMap);
		
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找账户异常！"));
			throw e;
		}
		return delnum;
	}
	
	/**
	 * 
	 * @param userdata
	 * @return
	 * @throws Exception
	 */
	public boolean updateUserdata(Userdata userdata,LfSpFee lfSpFee) throws Exception
	{

		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("feeFlag", userdata.getFeeFlag().toString());
			conditionMap.put("userId", userdata.getUserId());
			empTransDao.update(conn, userdata);
			empTransDao.update(conn, GtPortUsed.class, objectMap, conditionMap);
			
			//多企业时才要修改lf_spfee
			if(StaticValue.getCORPTYPE() ==1 && lfSpFee!=null)
			{
				if(lfSpFee.getSfId()!=null)
				{
					
					empTransDao.update(conn, lfSpFee);
				}
				else
				{
					EmpExecutionContext.error("修改短彩sp账户时lfspfee获取失败，spuser:"+lfSpFee.getSpUser());
				}
			}

			empTransDao.commitTransaction(conn);
			result = true;

		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"修改账户异常！"));
			throw e;
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	/**
	 * 添加sp账户
	 * @description    
	 * @param user userdata实体类
	 * @param useIP 用户ip
	 * @param lfSpFee 运营商余额表（多企业时才会设值）
	 * @return
	 * @throws Exception       			 
	 * @author zhangmin
	 * @datetime 2013-10-29 下午01:50:56
	 */
	public boolean addUserdata(Userdata user, String useIP,LfSpFee lfSpFee) throws Exception
	{

		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);

			empTransDao.save(conn, user);
/*			if(user.getAccouttype()==1){
				PbWebzzcmdQueue pwq = new PbWebzzcmdQueue();
				pwq.setCmdType(113l);
				pwq.setUserId(user.getUserId());
				pwq.setMemo("新增账户触发的同步");
				pwq.setFee(0.0);
				pwq.setCmdParam("0");
				pwq.setUseIp(useIP);
				empTransDao.save(conn, pwq);
			}*/
			//单企业在添加账号的时候，添加GwUserproperty
			if(StaticValue.getCORPTYPE() ==0){
			GwUserproperty gwUserproperty=new GwUserproperty();
			//设置默认值
			gwUserproperty.setEcid(100001);
			gwUserproperty.setUserid(user.getUserId());

			// 如果是EMP应用账户，默认采用加密模式(sptype=1表示EMP应用账户)
			if(1 == user.getSptype()) {
                gwUserproperty.setPwdencode(1);
            } else {
                gwUserproperty.setPwdencode(0);
            }

			gwUserproperty.setPwdencodestr("00000000");
			gwUserproperty.setMsgcode(1);
			gwUserproperty.setMsgencode(1);
			gwUserproperty.setPushmofmt(2);
			gwUserproperty.setPushrptfmt(2);
			gwUserproperty.setPushpwdencode(0);
			gwUserproperty.setPushpwdencodestr("00000000");
			gwUserproperty.setPushmsgcode(1);
			gwUserproperty.setPushmsgencode(1);
			gwUserproperty.setPushfailcnt(3);
			gwUserproperty.setPushslidewnd(5);
			gwUserproperty.setPushmomaxcnt(100);
			gwUserproperty.setPushrptmaxcnt(100);
			gwUserproperty.setGetmomaxcnt(100);
			gwUserproperty.setGetrptmaxcnt(100);
			gwUserproperty.setReserve("");
			//保存用户属性
			empTransDao.save(conn, gwUserproperty);
			}
			
			//多企业才存lf_spfee表
			if(StaticValue.getCORPTYPE() ==1 && lfSpFee!=null)
			{
				empTransDao.save(conn, lfSpFee);
			}
			empTransDao.commitTransaction(conn);

			result = true;
		} catch (Exception e)
		{
			result = false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"添加账户异常！"));
			throw e;
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	public boolean delAllMrSpgateWatch() throws Exception
	{
		boolean result = false;
		try
		{
			result = empDao.delete(MrSpgateWatch.class);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"删除spgate异常！"));
			throw e;
		}
		return result;
	}
	
	
	/**
	 * 获取彩信账号根据账户id
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public Userdata getMmsUserdataByUserid(String userid) throws Exception
	{
		return getUserdataByUseridandActype(userid,2);
	}
	
	
	/**
	 * 获取短信账号根据账户id
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public Userdata getSmsUserdataByUserid(String userid) throws Exception
	{
		return getUserdataByUseridandActype(userid,1);
	}
	
	
}
