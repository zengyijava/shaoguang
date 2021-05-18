package com.montnets.emp.security.wgmsgconfig;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.segnumber.PbServicetype;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.security.numsegment.OprNumSegmentBiz;

/**
 * @project sf_new
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-7-21 下午02:02:59o
 * @description
 */
public class WgMsgConfigBiz extends SuperBiz
{

	BaseBiz					baseBiz			= new BaseBiz();

	// 号段集合
	private static String[]	haoduan			= new String[3];

	// 号段更新时间
	private static long		hdUpdateTime	= System.currentTimeMillis();
	
	ErrorLoger errorLoger = new ErrorLoger();

	//重新同步内存值的时间间隔(单位:分钟)
	private static final int										SYNC_TIME		= 30 * 60 * 1000;
	/**
	 * @param spgate
	 * @param spisuncm
	 * @return
	 * @throws Exception
	 */
	public boolean isGateExist(String spgate, String spisuncm, String gatetype) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("gateType", gatetype);
			List<XtGateQueue> gatesList = empDao.findListByCondition(XtGateQueue.class, conditionMap, null);
			if(gatesList == null || gatesList.size() == 0)
			{
				result = false;
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "查询通道账户异常！"));
			throw e;
		}
		return result;
	}

	/**
	 * @param gateName
	 * @return
	 * @throws Exception
	 */
	public boolean isGateNameExist(String gateName, String gatetype) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			conditionMap.put("gateName", gateName);
			conditionMap.put("gateType", gatetype);
			List<XtGateQueue> gatesList = empDao.findListByCondition(XtGateQueue.class, conditionMap, null);
			if(gatesList == null || gatesList.size() == 0)
			{
				result = false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "查询通道账户异常。"));
			throw e;
		}
		return result;
	}

	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer changeState(Long id) throws Exception
	{
		if(id == null)
		{
			return null;
		}
		Integer resultState = null;
		Connection conn = empTransDao.getConnection();
		try
		{
			XtGateQueue gate = baseBiz.getById(XtGateQueue.class, id);
			if(gate.getStatus() == 0)
			{
				gate.setStatus(1);
			}
			else if(gate.getStatus() == 1)
			{
				gate.setStatus(0);
			}

			empTransDao.beginTransaction(conn);
			empTransDao.update(conn, gate);
			if(gate.getStatus() == 1)
			{
				this.updatePortState(conn, gate);
			}

			empTransDao.commitTransaction(conn);
			resultState = gate.getStatus();
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "修改通道账户状态异常。"));
			throw e;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
		return resultState;
	}

	/**
	 * @param conn
	 * @param gate
	 * @return
	 * @throws Exception
	 */
	private Integer updatePortState(Connection conn, XtGateQueue gate) throws Exception
	{
		Integer resultState = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			LinkedHashMap<String, String> portMap = new LinkedHashMap<String, String>();
			portMap.put("status", gate.getStatus().toString());

			conditionMap.put("spgate", gate.getSpgate());
			conditionMap.put("spisuncm", gate.getSpisuncm().toString());
			boolean resultUpdate = empTransDao.update(conn, GtPortUsed.class, portMap, conditionMap);
			if(resultUpdate)
			{
				resultState = Integer.valueOf(portMap.get("status"));
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "更新通道账户端口号状态异常。"));
			throw e;
		}
		return resultState;
	}

	/**
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
			portsList = empDao.findListBySymbolsCondition(GtPortUsed.class, conditionMap, orderbyMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "获取通道异常，userId:" + userId));
			throw e;
		}
		return portsList;
	}

	/**
	 * 获取短信账号根据账户id
	 * 
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public Userdata getSmsUserdataByUserid(String userid) throws Exception
	{
		return getUserdataByUseridandActype(userid, 1);
	}

	/**
	 * 通过账号名称和账号类型获取账户信息
	 * 
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public Userdata getUserdataByUseridandActype(String userid, Integer accouttype) throws Exception
	{
		Userdata user = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("userId", userid);
			conditionMap.put("accouttype", accouttype == null ? "" : accouttype.toString());
			List<Userdata> tempList = empDao.findListByCondition(Userdata.class, conditionMap, null);
			if(tempList != null && tempList.size() > 0)
			{
				user = tempList.get(0);
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "获取通道账户信息异常，userid：" + userid + "，accouttype：" + accouttype));
			throw e;
		}
		return user;
	}

	/**
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<Userdata> getAllUserdata(int type) throws Exception
	{
		List<Userdata> userDatasList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("uid&>", "100001");
			if(type == 1)
			{
				conditionMap.put("userType", "0");
			}
			else if(type == 2)
			{
				conditionMap.put("userType", "1");
			}
			orderMap.put("userId", StaticValue.ASC);
			userDatasList = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "查询所有SP账户失败！"));
			throw e;
		}
		return userDatasList;
	}

	/**
	 * @description 获取所有的sp账户
	 * @param usertype
	 *        账号类型 0-企业用户，1-spgate用户
	 * @param accountType
	 *        SP账号类型 (1短信SP账号，2彩信SP账号)
	 * @return sp账户的集合
	 * @throws Exception
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-28 下午05:01:40
	 */
	public List<Userdata> getAllUserdata(String usertype, String accountType) throws Exception
	{
		List<Userdata> userDatasList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("uid&>", "100001");
			conditionMap.put("accouttype", accountType);
			conditionMap.put("userType", usertype);
			orderMap.put("userId", StaticValue.ASC);
			userDatasList = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "查询所有SP账户失败！"));
			throw e;
		}
		return userDatasList;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public List<Userdata> getAllRoutUserdata() throws Exception
	{
		return new SpecialDAO().findBindUserdataForRute();
	}

	/**
	 * 获取当前系统的号段
	 * 
	 * @return
	 * @throws Exception
	 */
	public void setHaoduan()
	{
		try
		{
			String[] phoneNo = new String[3];
			String[] numSegment;
			// 查询条件map
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 查询
			List<PbServicetype> haoduanList = empDao.findListByCondition(PbServicetype.class, conditionMap, null);
			// 循环
			for (int i = 0; i < haoduanList.size(); i++)
			{
				PbServicetype pbSer = haoduanList.get(i);
				// 移动
				if(pbSer.getSpisuncm() == 0)
				{
					phoneNo[0] = pbSer.getServiceno();
				}
				// 联通
				else if(pbSer.getSpisuncm() == 1)
				{
					phoneNo[1] = pbSer.getServiceno();
				}
				// 电信
				else if(pbSer.getSpisuncm() == 21)
				{
					phoneNo[2] = pbSer.getServiceno();
				}

				// 设置运营商类型
				numSegment = pbSer.getServiceno().split(",");
				setNumSegmentType(numSegment, pbSer.getSpisuncm());
			}
			haoduan = phoneNo;
			// 号段更新时间赋值
			hdUpdateTime = System.currentTimeMillis();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "获取系统中的合法号段失败。"));
		}

	}

	/**
	 * 设置运营商号码类型
	 * 
	 * @description
	 * @param phoneNo
	 *        号码列表
	 * @param spisuncm
	 *        运营商
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-5-7 上午11:45:52
	 */
	private void setNumSegmentType(String[] phoneNo, int spisuncm)
	{
		String subscript = "";
		String numSegment = "";
		synchronized (OprNumSegmentBiz.getNumsegment())
		{
			for (int i = 0; i < phoneNo.length; i++)
			{
				try
				{
					// 号段为三位
					if(phoneNo[i].length() == 3)
					{
						// 截取后两位,拼接第三位0-9的数字,以此下标更新运营商类型
						numSegment = phoneNo[i].substring(1, 3);
						for (int j = 0; j < 10; j++)
						{
							subscript = numSegment + j;
							OprNumSegmentBiz.getNumsegment()[Integer.parseInt(subscript)] = spisuncm;
						}
					}
					// 号段为四位
					else if(phoneNo[i].length() == 4)
					{
						// 截取后三位,以此下标更新运营商类型
						numSegment = phoneNo[i].substring(1, 4);
						OprNumSegmentBiz.getNumsegment()[Integer.parseInt(numSegment)] = spisuncm;
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(errorLoger.getErrorLog(e, "设置运营商号码类型异常!号段:" + phoneNo[i]));
				}
			}
		}
	}

	/**
	 * 初始化运营商号段类型
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-5-6 下午08:06:31
	 */
	public void initNumSegmentType()
	{
		synchronized (OprNumSegmentBiz.getNumsegment())
		{
			int len = OprNumSegmentBiz.getNumsegment().length;
			// 初始化运营商号段类型为-1
			for (int i = 0; i < len; i++)
			{
				OprNumSegmentBiz.getNumsegment()[i] = -1;
			}
		}
	}

	/**
	 * 初始化号段信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-5-7 下午01:04:19
	 */
	public void initNumSegmentInfo()
	{
		// 初始化运营商号段
		initNumSegmentType();
		// 获取当前系统的号段
		setHaoduan();
	}

	/**
	 * 获取系统号段
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] getHaoduan()
	{
		if(haoduan == null || haoduan[0] == null || haoduan[1] == null || haoduan[2] == null)
		{
			setHaoduan();
		}
		// 如果当前时间减去上次同步的时间大于同步时间间隔
		else if(System.currentTimeMillis() - hdUpdateTime > SYNC_TIME)
		{
			setHaoduan();
		}

		return haoduan;
	}

	/**
	 * 清空号段内存的值
	 */
	public void cleanHaoduan()
	{
		// 如果当前时间与上次更新时间大于同步时间间隔，则清空haoduan
		if(System.currentTimeMillis() - hdUpdateTime > SYNC_TIME)
		{
			haoduan = null;
		}
	}

	/**
	 * 检查手机号是否合法
	 * 
	 * @param mobile
	 * @param spiscumu
	 * @param haoduan
	 * @return
	 * @throws Exception
	 */
	public int checkMobile(String mobile, Integer spiscumu, String[] haoduan) throws Exception
	{
		// 检查手机长度
		if(mobile.length() != 11) return 0;
		//普通号码首位必须为1
		if(!"1".equals(mobile.substring(0, 1)))
		{
			return 0;
		}
		for (int k = mobile.length(); --k >= 0;)
		{
			if(!Character.isDigit(mobile.charAt(k)))
			{
				return 0;
			}
		}
		// 获取号码归属运营商
		int phoneType = new OprNumSegmentBiz().getphoneType(mobile);
		if(phoneType == -1)
		{
			// 不合法
			return 0;
		}
		// 返回1 合法
		return 1;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobile
	 * @param haoduan
	 * @return
	 * @throws Exception
	 */
	public int checkMobile(String mobile, String[] haoduan) throws Exception
	{
		if(mobile.length() != 11) return 0;
		//普通号码首位必须为1
		if(!"1".equals(mobile.substring(0, 1)))
		{
			return 0;
		}
		for (int k = mobile.length(); --k >= 0;)
		{
			if(!Character.isDigit(mobile.charAt(k)))
			{
				return 0;
			}
		}
		// 获取号码归属运营商
		int phoneType = new OprNumSegmentBiz().getphoneType(mobile);
		if(phoneType == -1)
		{
			// 不合法
			return 0;
		}
		// 返回1 合法
		return 1;
	}

	/**
	 * 获取手机号码的号段
	 * 
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public Integer getPhoneSpisuncm(String number) throws Exception
	{
		// 截取手机号前三位
		number = number.substring(number.length() - 11, number.length());
		if(number.length() >= 7 && number.length() <= 21)
		{
			// 获取号码归属运营商
			return (new OprNumSegmentBiz().getphoneType(number));
		}
		return null;

	}
}
