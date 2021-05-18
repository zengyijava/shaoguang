package com.montnets.emp.common.biz;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.receive.ReceiveInit;
import com.montnets.emp.common.biz.receive.TimerReceiveHandle;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.entity.LfNodeBaseInfo;
import com.montnets.emp.common.thread.CommDataDealThread;
import com.montnets.emp.common.timer.TimerManagerBiz;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.security.blacklist.BlackListAtom;

public class SystemInitBiz extends SuperBiz
{
	public boolean checkSendState() throws Exception
	{

		// 判断当前时间是否已经超过定时发送时间，如果是则不发送
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		BalanceLogBiz biz = new BalanceLogBiz();
		// 未发送
		conditionMap.put("sendstate", "0");
		// 定时了
		conditionMap.put("timerStatus", "1");
		// 不查审批拒绝和待审批的任务
		conditionMap.put("reState&not in", "-1,2");
		// 不查撤销了的任务
		conditionMap.put("subState&<>", "3");
		conditionMap.put("msType&in", "1,2,5,6");
		StringBuffer idStr = new StringBuffer("");
		boolean result = false;
		List<LfMttask> taskList = new ArrayList<LfMttask>();
		LfMttask mttask;
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		try
		{
			taskList = empDao.findListBySymbolsCondition(LfMttask.class, conditionMap, null);
			if(taskList != null && taskList.size() > 0)
			{
				for (int i = 0, len = taskList.size(); i < len; i++)
				{
					mttask = taskList.get(i);
					if((mttask.getTimerTime().getTime() + 120 * 1000) < System.currentTimeMillis())
					{
						idStr.append(mttask.getMtId().toString()).append(",");
						if(mttask.getMsType().intValue() == 1 || mttask.getMsType().intValue() == 5)// 短信或移动财务
						{
							biz.sendSmsAmountByUserId(null, mttask.getUserId(), -1 * Integer.parseInt(mttask.getIcount()));
							//运营商退费
							biz.huishouFee(Integer.parseInt(mttask.getIcount()), mttask.getSpUser(), mttask.getMsgType());
						}
						else
						if(mttask.getMsType().intValue() == 2)
						{// 彩信
							biz.sendMmsAmountByUserId(null, mttask.getUserId(), -1 * mttask.getEffCount());
							//运营商退费
							biz.huishouFee(mttask.getEffCount().intValue(), mttask.getSpUser(), mttask.getMsgType());
						}
					
					}
				}
				if(idStr.length() != 0)
				{
					idStr.deleteCharAt(idStr.length() - 1);
					objectMap.put("sendstate", "5");
					conditionMap.clear();
					conditionMap.put("mtId", idStr.toString());
					result = empDao.update(LfMttask.class, objectMap, conditionMap);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "检查发送状态异常。");
			throw e;
		}
		return result;
	}

	/*
	 * 初使化,获取所有(短信，彩信)黑名单的方法
	 */
	public void GetAllBlackList()
	{
		BlackListAtom biz = new BlackListAtom();
		biz.SetAllBlackList();
		biz.SetMmsBlackList();
	}

	/**
	 * 获取所有要显示的控件
	 */
	public void getPageField()
	{
		GlobConfigBiz gcBiz = new GlobConfigBiz();
		gcBiz.getPageFieldList();
	}

	/**
	 * 初始化定时框架
	 */
	public void initTimer()
	{
		CommonBiz commBiz = new CommonBiz();
		//未配置数据库
		if(commBiz.isConfigDB() == 0){
			EmpExecutionContext.info("定时任务框架，未配置数据库信息，不初始化");
			return;
		}
		
		//是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB==1){
			
			TimerManagerBiz timerBiz = TimerManagerBiz.getTMInstance();
			//初始化并启动定时器
			timerBiz.StartTimer();
		}
	}
	
	/**
	 * 停止定时框架
	 */
	public void stopTimer()
	{
		//是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB==1){
			EmpExecutionContext.info("停止定时框架。");
			TimerManagerBiz timerBiz = TimerManagerBiz.getTMInstance();
			//初始化并启动定时器
			timerBiz.StopTimer();
		}

	}
	
	/**
	 * 启动处理尾号定时任务
	 */
	public void startDealSubno(){
		CommonBiz commBiz = new CommonBiz();
		//未配置数据库
		if(commBiz.isConfigDB() == 0){
			EmpExecutionContext.info("定时处理尾号任务，未配置数据库信息，不启动");
			return;
		}
		
		//是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB==1){
			new SubnoManagerBiz().executeDelSubno();
		}
	}
	
	/**
	 * 停止处理尾号定时任务
	 */
	public void stopDealSubno()
	{
		//是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB==1){
			EmpExecutionContext.info("停止处理尾号定时任务。");
			SubnoManagerBiz.stopDelSubno();
		}
	}

	/**
	 * 上行接收
	 */
	public void registerMoReceive()
	{
		try
		{
			ReceiveInit register = new ReceiveInit();

			// register.RegisteBus(0, StaticValue.SUV_MENUCODE, 0, new
			// SurveyStart().getClass().getName());
			// com.montnets.emp.engine.biz.MoServiceStart
			register.RegisteBus(0, StaticValue.MOBUSCODE, 0, "com.montnets.emp.engine.biz.MoServiceStart");

			// 手机上行审批
			register.RegisteBus(0, StaticValue.VERIFYREMIND_MENUCODE, 0, "com.montnets.emp.msgflow.biz.ReviewRemindMOStart");

			register.RegisteBus(0, StaticValue.WXREMIND_MENUCODE, 0, "com.montnets.emp.wxmanager.biz.NetAuditOStart");

			// register.RegisteBus(0, StaticValue.ELECPOWER_MENUCODE, 0, new
			// SgccSmsSendStart().getClass().getName());

			register.RegisteBus(1, StaticValue.WMNOTICECODE, 0, "com.montnets.emp.perfect.biz.PrefectNoticeStateStart");

			register.RegisteBus(0, StaticValue.WMNOTICECODE, 0, "com.montnets.emp.perfect.biz.PrefectNoticeUpMsgStart");
			//告警短信
			register.RegisteBus(1, StaticValue.MONITORSMS_MENUCODE, 0, "com.montnets.emp.monitor.biz.MonSmsStateStrat");

			register.RegisteBus(1, StaticValue.LOGINSENDMESSAGE_MENUCODE, 0, new LoginSendMessageStateStart().getClass().getName());

			// register.RegisteBus(0, StaticValue.OA_MENUCODE, 0, new
			// OAStart().getClass().getName());

		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "上行接收异常。");
		}
	}

	/**
	 * 初始化接收框架
	 */
	public void initReveive()
	{
		TimerReceiveHandle.getTMoHandleInstance();
	}
	
	public void initRptReveive()
	{
		TimerReceiveHandle.getTMoHandleInstance();
	}
	
	/**
	 * 初始化接收框架
	 */
	public void stopReveive()
	{
		TimerReceiveHandle.getTMoHandleInstance().stopTimerReceive();
	}
	
	/**
	 * 初始化全局变量
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-7-10 上午09:59:24
	 */
	public void initGlobalVariable()
	{
		try
		{
			//查询SQL
			String sql = "SELECT GLOBALKEY, GLOBALVALUE, GLOBALSTRVALUE FROM LF_GLOBAL_VARIABLE";
			List<DynaBean> monConfigList = new SuperDAO().getListDynaBeanBySql(sql);
			//KEY
			String globalKey = "";
			//字符串参数
			String globalStrValue = "";
			//数字参数，long类型
			long globalLongValue = -1;
			//数字参数，Int类型
			int globalIntValue = -1;
			for(DynaBean monConfig : monConfigList)
			{
				//KEY
				globalKey = monConfig.get("globalkey")==null?"":monConfig.get("globalkey").toString().trim();
				//字符串参数
				globalStrValue = monConfig.get("globalstrvalue")==null?"":monConfig.get("globalstrvalue").toString().trim();
				//数字参数，long类型
				globalLongValue = monConfig.get("globalvalue")==null?-1:Long.parseLong(monConfig.get("globalvalue").toString());
				//数字参数，Int类型
				globalIntValue = monConfig.get("globalvalue")==null?-1:Integer.parseInt(monConfig.get("globalvalue").toString());
				if(!"".equals(globalKey))
				{
					//生日祝福姓名签名，左边符号
					if("BIRTHWISHNAMESIGNLEFT".equals(globalKey) && !"".equals(globalStrValue))
					{
						StaticValue.setBirthwishNameSignLeft(globalStrValue);
					}
					//生日祝福姓名签名，右边符号
					else if("BIRTHWISHNAMESIGNRIGHT".equals(globalKey) && !"".equals(globalStrValue))
					{
						StaticValue.setBirthwishNameSignRight(globalStrValue);
					}
					//英文短信特殊字符,一个字符按2个计算
					else if("SPECIALCHAR".equals(globalKey) && !"".equals(globalStrValue))
					{
						//配置英文短信特殊字符
						if(globalStrValue.indexOf(",") > 0)
						{
							//StaticValue.SMSCONTENT_SPECIALCHAR_STR = globalStrValue;
							StaticValue.setSmscontentSpecialcharStr(globalStrValue);
							String[] specialCharList = globalStrValue.split(",");
							for(String special:specialCharList)
							{
								if(special != null && !"".equals(special))
								{
									StaticValue.SMSCONTENT_SPECIALCHAR.put(Integer.parseInt(special), null);
								}
							}
						}
					}
					//短信异常字符,需要替换为空格
					else if("REPLACECHAR".equals(globalKey) && !"".equals(globalStrValue))
					{
						//配置短信异常字符
						if(globalStrValue.indexOf(",") > 0)
						{
							String[] replaceCharList = globalStrValue.split(",");
							for(String replace:replaceCharList)
							{
								if(replace != null && !"".equals(replace))
								{
									StaticValue.SMSCONTENT_REPLACECHAR.put(Integer.parseInt(replace), null);
								}
							}
						}
					}
					//HTTP请求超时时间（毫秒）
					else if("HTTP_REQUEST_TIMEOUT".equals(globalKey) && globalLongValue != -1)
					{
						StaticValue.HTTP_REQUEST_TIMEOUT=globalIntValue;
					}
					//HTTP响应超时时间（毫秒）
					else if("HTTP_RESPONSE_TIMEOUT".equals(globalKey) && globalLongValue != -1)
					{
						StaticValue.HTTP_RESPONSE_TIMEOUT=globalIntValue;
					}
					//查询运营商余额MBOSS接口加密密钥
					else if("MBOSS_KEY".equals(globalKey) && !"".equals(globalStrValue))
					{
						StaticValue.MBOSS_KEY=globalStrValue;
					}
					//运营商余额请求间隔（毫秒）
					else if("BALANCE_REQ_INTERVAL".equals(globalKey) && globalLongValue != -1)
					{
						StaticValue.setBalanceRequestInterval(globalLongValue);
					}
					//消息体加密标志，0 不加密，1 加密
					else if("BALANCE_MSG_ENCRYFLAG".equals(globalKey) && globalIntValue != -1)
					{
						StaticValue.BALANCE_MSG_ENCRYFLAG=globalIntValue;
					}
					//日志打印间隔（毫秒）
					else if("LOG_PRINT_INTERVAL".equals(globalKey) && globalLongValue != -1)
					{
						StaticValue.setLogPrintInterval(globalLongValue);
					}
					//企业黑名单最大数
					else if("BLACK_MAXCOUNT".equals(globalKey) && globalLongValue != -1)
					{
						StaticValue.setBlackMaxcount(globalLongValue);
					}
					//模板可编辑标识
					else if("TMPEDITORFLAG".equals(globalKey) && globalLongValue != -1)
					{
						StaticValue.setTMPEDITORFLAG(globalLongValue);
					}
					//是否生成客户端监控文件
					else if("ISCLIENTMONFILE".equals(globalKey) && globalIntValue != -1)
					{
						StaticValue.setISCLIENTMONFILE(globalIntValue);
					}
					//MBOSS平台WEBSERVICES地址
					else if("ACTSYNCURL".equals(globalKey) && !"".equals(globalStrValue))
					{
						StaticValue.setMbossWebservicesUrl(globalStrValue);
					}
					//SP账号类型开关0:所有1:EMP应用
					else if("SPTYPEFLAG".equals(globalKey) &&  globalIntValue != -1)
					{
						StaticValue.setSPTYPEFLAG(globalIntValue);
					}
					//超过内存总量百分比时间
					else if("EXCEEDMEMORYTIME".equals(globalKey) &&  globalLongValue != -1)
					{
						StaticValue.setExceedMemoryTime(globalLongValue);
					}
					//超过内存总量百分比时间
					else if("MEMORYPERCENTAGE".equals(globalKey) &&  !"".equals(globalStrValue))
					{
						StaticValue.setMemoryPercentage(Double.parseDouble(globalStrValue));
					}
					//查询运营商余额备用URL地址
					else if("BALANCEBAKURL".equals(globalKey) &&  !"".equals(globalStrValue))
					{
						//有多个查询运营商余额备用地址
						if(globalStrValue.indexOf("@")>=0)
						{
							//获取查询运营商余额备用地址
							String[] url = globalStrValue.split("@");
							//设置查询运营商余额备用地址
							for(int i=0; i<url.length; i++)
							{
								StaticValue.getBalanceBakUrl().add(url[i]);
							}
						}
						//一个查询运营商余额备用地址
						else
						{
							StaticValue.getBalanceBakUrl().add(globalStrValue);
						}
					}
					//实时数据保留实时库时间,-1:关，其他为实时库保留实时数据的月
					else if("READDATASAVETIME".equals(globalKey) &&  globalIntValue != -1)
					{
						StaticValue.setReadDataSaveTime(globalIntValue);
					}
					//使用历史库时间
					else if("USEHISTORYDBTIME".equals(globalKey) &&  !"".equals(globalStrValue))
					{
						StaticValue.setUseHistoryDBTime(globalStrValue);
					}
					//上行黑名单查询单次总量
					else if("MOTDCOUNT".equals(globalKey) &&  globalIntValue != -1)
					{
						StaticValue.setMoTdCount(globalIntValue);
					}
					//使用EMP服务器URL标识，0：不使用，1：使用
					else if("USESERVERURLFLAG".equals(globalKey) &&  globalIntValue != -1)
					{
						StaticValue.setUseServerUrlFlag(globalIntValue);
					}
					//EMP服务器URL
					else if("SERVERURL".equals(globalKey) &&  !"".equals(globalStrValue))
					{
						StaticValue.setServerUrl(","+globalStrValue+",");
					}
				}
			}
			Runtime runtime = Runtime.getRuntime();
			//最大内存空间
			long max = runtime.maxMemory() / 1024 / 1024;
			//内存信息，下标0:总内存；1：首次超过总内存指定百分比的时间
			StaticValue.getMemoryInfo()[0] = max;
			StaticValue.getMemoryInfo()[1] = 0L;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "初始化全局变量异常！");
		}
		try
		{
			//设置EMP版本号信息
			Map<String,String[]> versions = new FrameControlBiz().getVersionInfos(null);
			if(versions != null && versions.size() > 0 && versions.get("1000")[0] != null)
			{
				StaticValue.setJspImpVersion(versions.get("1000")[0]);
				EmpExecutionContext.info("初始化设置JSP_IMP_VERSION版本号为："+ StaticValue.getJspImpVersion());
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "初始化设置EMP版本号信息异常！");
		}
	}
	
	/**
	 * 设置全局变量
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-11-17 上午11:41:50
	 */
	public void setGlobalVariable()
	{
		try
		{
			//查询SQL
			String sql = "SELECT GLOBALKEY, GLOBALVALUE, GLOBALSTRVALUE FROM LF_GLOBAL_VARIABLE";
			List<DynaBean> monConfigList = new SuperDAO().getListDynaBeanBySql(sql);
			//KEY
			String globalKey = "";
			//字符串参数
			String globalStrValue = "";
			//数字参数，long类型
			//long globalLongValue = -1;
			//数字参数，Int类型
			int globalIntValue = -1;
			for(DynaBean monConfig : monConfigList)
			{
				//KEY
				globalKey = monConfig.get("globalkey")==null?"":monConfig.get("globalkey").toString().trim();
				//字符串参数
				globalStrValue = monConfig.get("globalstrvalue")==null?"":monConfig.get("globalstrvalue").toString().trim();
				//数字参数，long类型
				//globalLongValue = monConfig.get("globalvalue")==null?-1:Long.parseLong(monConfig.get("globalvalue").toString());
				//数字参数，Int类型
				globalIntValue = monConfig.get("globalvalue")==null?-1:Integer.parseInt(monConfig.get("globalvalue").toString());
				if(!"".equals(globalKey))
				{
					
					//使用EMP服务器URL标识，0：不使用，1：使用
					if("USESERVERURLFLAG".equals(globalKey) &&  globalIntValue != -1)
					{
						StaticValue.setUseServerUrlFlag(globalIntValue);
					}
					//EMP服务器URL
					else if("SERVERURL".equals(globalKey) &&  !"".equals(globalStrValue))
					{
						StaticValue.setServerUrl(","+globalStrValue+",");
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置全局变量异常！");
		}
	}
	
	/**
	 * 设置服务器操作系统
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-1-28 下午08:09:16
	 */
	public void setServerSystemType()
	{
		String serverSystemType = System.getProperties().getProperty("os.name");
		if(serverSystemType.toLowerCase().indexOf("windows") < 0)
		{
			StaticValue.setServerSystemType(1);
		}

	}
	
	/**
	 * 
	 * @description 节点基础信息记录
	 * @return 成功返回true
	 */
	public boolean UpdateNodeInfo()
	{
		try
		{
			//节点id
			String nodeId = StaticValue.getServerNumber();
			if(nodeId == null || nodeId.trim().length() < 1)
			{
				EmpExecutionContext.error("节点基础信息记录，获取不到节点ID。");
				return false;
			}
			
			LfNodeBaseInfo nodeInfo = new LfNodeBaseInfo();
			nodeInfo.setNodeId(nodeId);
			
			String serverIP = GetSerIP();
			//有ip
			if(serverIP != null && serverIP.length() > 0)
			{
				nodeInfo.setServerIP(serverIP);
			}
			else
			{
				nodeInfo.setServerIP(" ");
			}
			
			nodeInfo.setServerPort(0);
			String serLocalURL = SystemGlobals.getValue("montnets.thisUrl");
			nodeInfo.setSerLocalURL(serLocalURL);
			nodeInfo.setSerInternetURL(" ");
			nodeInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("nodeId", nodeId);
			List<LfNodeBaseInfo> nodeInfoList = empDao.findListByCondition(LfNodeBaseInfo.class, conditionMap, null);
			//有记录，则更新
			if(nodeInfoList != null && nodeInfoList.size() > 0)
			{
				return updateNodeBaseInfo(nodeInfo);
			}
			//没记录则插入
			else
			{
				return empDao.save(nodeInfo);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "节点基础信息记录，异常。");
			return false;
		}
	}
	
	/**
	 * 
	 * @description 更新节点基础信息
	 * @param nodeInfo 节点信息对象
	 * @return 成功返回true
	 */
	private boolean updateNodeBaseInfo(LfNodeBaseInfo nodeInfo)
	{
		try
		{
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			
			objectMap.put("serverIP", nodeInfo.getServerIP());
			objectMap.put("serverPort", Integer.toString(nodeInfo.getServerPort()));
			objectMap.put("serLocalURL", nodeInfo.getSerLocalURL());
			objectMap.put("serInternetURL", nodeInfo.getSerInternetURL());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMap.put("updateTime", sdf.format(new Date()));
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("nodeId", nodeInfo.getNodeId());
			
			return empDao.update(LfNodeBaseInfo.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新节点基础信息，异常。");
			return false;
		}
	}
	
	/**
	 * 
	 * @description 获取服务器IP
	 * @return 返回IP，异常返回null
	 */
	private String GetSerIP()
	{
		try
		{
			InetAddress adress = InetAddress.getLocalHost();
			return adress.getHostAddress();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取服务器IP，异常。");
			return null;
		}
	}
	
	private CommDataDealThread dataDealThread;
	
	/**
	 * 
	 * @description 启动公共数据处理线程
	 * @return 成功返回ture
	 */
	public boolean StartCommDataDealThread()
	{
		dataDealThread = new CommDataDealThread();
		return dataDealThread.StartThread();
	}
	
	/**
	 * 
	 * @description 关闭公共数据处理线程
	 */
	public void StopCommDataDealThread()
	{
		if(dataDealThread == null)
		{
			return;
		}
		dataDealThread.StopThread();
	}
	
}
