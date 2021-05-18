package com.montnets.emp.appwg.initappplat;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;

import com.montnets.EMPException;
import com.montnets.XMPPServer;
import com.montnets.app.EMessage;
import com.montnets.app.PMessage;
import com.montnets.app.PMessageModel;
import com.montnets.app.StateReportMessage;
import com.montnets.app.StateReportModel;
import com.montnets.app.UserInfoMessage;
import com.montnets.app.UserInfoModel;
import com.montnets.app.style.PMessageStyle;
import com.montnets.app.style.PMessageStyle1;
import com.montnets.emp.appwg.bean.AppStaticValue;
import com.montnets.emp.appwg.biz.SynUserThread;
import com.montnets.emp.appwg.biz.WgMwFileBiz;
import com.montnets.emp.appwg.dao.WgDAO;
import com.montnets.emp.appwg.initbuss.HandleMsgBiz;
import com.montnets.emp.appwg.initbuss.ReadMoQueDBThread;
import com.montnets.emp.appwg.initbuss.ReadRptQueDBThread;
import com.montnets.emp.appwg.initbuss.SendMoThread;
import com.montnets.emp.appwg.initbuss.SendRptThread;
import com.montnets.emp.appwg.util.RandomCount;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.appmage.LfAppAccount;
import com.montnets.emp.entity.appmage.LfAppMtmsg;
import com.montnets.emp.entity.appmage.LfAppMwClient;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfClientDepSp;
import com.montnets.emp.entity.client.LfClientMultiPro;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.listener.ConnectionListener;
import com.montnets.listener.EMessageReceiptListener;
import com.montnets.listener.PMessageListener;
import com.montnets.listener.PMessageReceiptListener;
import com.montnets.listener.StateReportMessageListener;
import com.montnets.listener.SynUserInfoListener;

/**
 * 
 * @project p_appwg
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-6-11 下午04:28:41
 * @description app网关与App梦网平台通信逻辑类
 */
public class AppSdkPackage extends SuperBiz
{
	private AppSdkPackage(){
		
	}
	
	private static AppSdkPackage instance = null;
	
	private WgDAO wgDao = new WgDAO();
	
	public static synchronized AppSdkPackage getInstance(){
		if(instance == null){
			instance = new AppSdkPackage();
		}
		return instance;
	}
	
	/**
	 * 登录状态。1-未登录；2-登录成功。
	 */
	//public static int LoginState = 1;
	
	private XMPPServer server = null;
	
	//是否允许重连
	protected static  boolean reconnectionAllowed = false;

	
	public static final String splitSymbol = ";";
	
	//企业编码
	private String ecode = null;
	//app平台ip
	private String host = null;
	//app平台端口
	private Integer port = null;
	//企业用户
	private String corpUser = null;
	//企业用户密码
	private String password = null;
	
	//定时重连
	private Timer tExit;
	/**
	 * 定时重连等待时间，5分钟
	 */
	private static int loginWaitTime = 10*60*1000;
	/**
	 * 定时重连间隔，5秒
	 */
	private static int baseLoginTime = 5*1000;
	/**
	 * 定时重连间隔倍数，3倍
	 */
	private static int baseLoginMult = 3;
	//定时重连间隔，每次重连该数值乘以3，重连5次后初始化为5秒
	private int loginTime = 5*1000;
	/**
	 * 定时重连次数，5次
	 */
	private static int baseLoginCount = 5;
	//定时重连次数，每次重连该数值加1
	private int loginCount = 0;
	
	//用户账号集合，key为用户账号，value为用户账号
	public final Map<String,String> userCodeMap = new HashMap<String,String>();
	
	//定时重连状态。1：未定时重连；2：定时重连中
	private int reConnState = 1;
	
	//未知机构id，默认为-10
	private Long unKnowDepId = null;
	
	private static long depId = -10l;
	
	private ConnectionListener connListenner = null;
	private EMessageReceiptListener eMsgListener = null;
	private PMessageListener pMsgListener = null;
	private PMessageReceiptListener pMsgReceiptListener = null;
	private SynUserInfoListener synUserInfoListener = null;
	private StateReportMessageListener stateReportMessageListener = null;
	
	private SynUserThread synUserThread = null;
	private SendMtThread sendThread = null;
	private WinMtThread winThread = null;
	private ReadMtQueDBThread readMtQueDBThread = null;
	private SendMoThread sendMoThread = null;
	private ReadMoQueDBThread readMoQueDBThread = null;
	private SendRptThread sendRptThread = null;
	private ReadRptQueDBThread readRptQueDBThread = null;
	
	/**
	 * 设置重连参数为初始值
	 */
	private void setReConnParamInit(){
		//初始化重连间隔
		loginTime = baseLoginTime;
		//初始化重试次数为1
		loginCount = 1;
		EmpExecutionContext.info("重连参数初始化。重连时间间隔为"+(baseLoginTime/1000)+"秒；重连重试次数为第"+loginCount+"次。");
	}
	
	/**
	 * 设置定时重连状态为定时重连中
	 */
	private void setReConnStateRuning(){
		reConnState = 2;
	}
	
	/**
	 * 设置定时重连状态为停止
	 */
	private void setReConnStateStop(){
		reConnState = 1;
	}
	
	/**
	 * 获取定时重连状态
	 * @return 返回定时重连状态。1：未定时重连；2：定时重连中
	 */
	private int getReConnState(){
		return reConnState;
	}
	
	/**
	 * 从数据库加载用户账号
	 * @return 成功返回true
	 */
	private boolean loadUserCodeFromDB(){
		if(ecode == null || ecode.trim().length() == 0){
			EmpExecutionContext.error("从数据库加载用户账号失败，企业编码为空。");
			return false;
		}
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("ecode", ecode);
			List<LfAppMwClient> usersList = empDao.findListByCondition(LfAppMwClient.class, conditionMap, null);
			if(usersList == null || usersList.size() == 0){
				EmpExecutionContext.info("从数据库加载用户账号，无用户。");
				return true;
			}
			for(LfAppMwClient user : usersList){
				userCodeMap.put(user.getAppcode(), user.getAppcode());
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "从数据库加载用户账号异常。");
			return false;
		}
	}
	
	/**
	 * 初始化梦网app平台接口
	 */
	public void InitMwApp(){
		
		//把数据库中已读状态的更新为未读
		updateDBCache();
		
		if(server != null){
			EmpExecutionContext.info("App网关服务已初始化，不需要再初始化。");
			return;
		}
		
		server = new XMPPServer();
		
		//设置连接监听器
		setConnectionListener();
		//设置公众消息回执监听器
		setEMessageReceiptListener();
		//设置个人消息监听器
		setPMessageListener();
		//设置个人消息回执监听器
		setPMessageReceiptListener();
		//设置用户信息同步监听器
		setSynUserInfoListener();
		//设置状态报告监听
		setStateReportMessageListener();
		
		//关掉这个代理，因为使用了7777端口作为文件上传。这里没用到文件上传，但在调用disconnection的时候会开启
		SmackConfiguration.setLocalSocks5ProxyEnabled(false);
		
	}
	
	/**
	 * 关闭App网关
	 */
	public void StopAppWg(){
		
		try
		{
			if(server != null && server.getConnection() != null){
				//断开连接
				server.getConnection().disconnect();
			}
			
			if(server != null){
				server.logout();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "App网关，断开连接，退出登录异常");
		}
		
		if(server != null){
			//移除监听器
			removeListener();
		}
		
		//关闭线程
		stopThread();
		
		server = null;
		
		//instance = null;
	}
	
	private void removeListener(){
		try
		{
			if(server == null){
				EmpExecutionContext.info("App网关，移除监听器，server对象为null。");
				return;
			}
			server.removeListener(connListenner);
			server.removeListener(eMsgListener);
			server.removeListener(pMsgListener);
			server.removeListener(pMsgReceiptListener);
			server.removeListener(synUserInfoListener);
			server.removeListener(stateReportMessageListener);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "App网关，移除监听器异常。");
		}
	}
	
	/**
	 * 启动线程
	 */
	private void startThread(){
		//启动同步用户线程
		startSynUserThread();
		
		//启动发送线程，用于处理接收mt缓冲队列
		startSendThread();
		
		//启动发送窗口处理线程
		startWinThread();
		
		//启动处理数据库mt消息线程
		startReadMtQueDBThread();
		
		//启动mo处理线程，用于处理接收mo缓冲队列
		startSendMoThread();
		
		//启动处理数据库mo消息线程
		startReadMoQueDBThread();
		
		//启动rpt处理线程，用于处理接收rpt缓冲队列
		startSendRptThread();
		
		//启动处理数据库mo消息线程
		startReadRptQueDBThread();
	}
	
	/**
	 * 关闭线程
	 */
	private void stopThread(){
		if(readMtQueDBThread != null){
			//先关闭读取线程
			readMtQueDBThread.stopReadMtQueDBThread();
		}

		if(sendThread != null){
			//关闭发送线程
			sendThread.stopSendMtThread();
		}
		
		if(winThread != null){
			//关闭窗口线程
			winThread.StopWinMtThread();
		}
		
		if(readMoQueDBThread != null){
			//关闭读DB mo线程
			readMoQueDBThread.stopReadMoQueDBThread();
		}
		
		if(readRptQueDBThread != null){
			//关闭读DB rpt线程
			readRptQueDBThread.stopReadRptQueDBThread();
		}
		
		if(sendMoThread != null){
			//关闭mo处理线程
			sendMoThread.stopSendMoThread();
		}
		
		if(sendRptThread != null){
			//关闭rpt处理线程
			sendRptThread.stopSendRptThread();
		}
		
		if(synUserThread != null){
			//关闭用户处理线程
			synUserThread.stopSynUserThread();
		}
		
	}
	
	/**
	 * 把数据库中已读状态的更新为未读
	 */
	private void updateDBCache(){
		updateNoRead();
	}
	
	private void updateNoRead(){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			//更新为未读
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_NOREAD));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			//把已读更新为未读
			conditionMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_WAIT_SEND));
			
			wgDao.updateMtDBNoRead(objectMap, conditionMap);
			wgDao.updateMoDBNoRead(objectMap, conditionMap);
			wgDao.updateRptDBNoRead(objectMap, conditionMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动服务器时，更新数据库消息状态异常。");
		}
	}
	
	/**
	 * 启动同步用户线程
	 */
	private void startSynUserThread(){
		//线程已启动
		if(synUserThread != null && synUserThread.isThreadStart()){
			EmpExecutionContext.info("启动APP网关同步用户线程，线程已启动，不再启动。");
		}
		else{
			//启动用户同步线程
			synUserThread = new SynUserThread();
			synUserThread.StartThread();
		}
	}
	
	/**
	 * 启动发送线程，用于处理接收mt缓冲队列
	 */
	private void startSendThread(){
		try
		{
			if(sendThread != null && sendThread.isThreadStart()){
				EmpExecutionContext.info("启动APP网关发送线程，线程已启动，不再启动。");
			}
			else{
				sendThread = new SendMtThread();
				sendThread.start();
				//设置线程已启动
				sendThread.setThreadStart(true);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动App网关发送线程异常");
		}
	}
	
	/**
	 * 启动发送窗口处理线程
	 */
	private void startWinThread(){
		try
		{
			if(winThread != null && winThread.isThreadStart()){
				EmpExecutionContext.info("启动APP网关发送窗口处理线程，线程已启动，不再启动。");
			}
			else{
				winThread = new WinMtThread();
				winThread.start();
				//设置线程已启动
				winThread.setThreadStart(true);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动APP网关发送窗口处理线程异常");
		}
	}
	
	/**
	 * 启动处理数据库mt消息线程
	 */
	private void startReadMtQueDBThread(){
		try
		{
			if(readMtQueDBThread != null && readMtQueDBThread.isThreadStart()){
				EmpExecutionContext.info("启动处理数据库mt消息线程，线程已启动，不再启动。");
			}
			else{
				readMtQueDBThread = new ReadMtQueDBThread();
				readMtQueDBThread.start();
				//设置线程已启动
				readMtQueDBThread.setThreadStart(true);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动处理数据库mt消息线程异常");
		}
	}
	
	/**
	 * 启动mo处理线程，用于处理接收mo缓冲队列
	 */
	private void startSendMoThread(){
		try
		{
			if(sendMoThread != null && sendMoThread.isThreadStart()){
				EmpExecutionContext.info("启动mo处理线程，线程已启动，不再启动。");
			}
			else{
				sendMoThread = new SendMoThread();
				sendMoThread.start();
				//设置线程已启动
				sendMoThread.setThreadStart(true);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动mo处理线程异常");
		}
	}
	
	/**
	 * 启动处理数据库mo消息线程
	 */
	private void startReadMoQueDBThread(){
		try
		{
			if(readMoQueDBThread != null && readMoQueDBThread.isThreadStart()){
				EmpExecutionContext.info("启动处理数据库mo消息线程，线程已启动，不再启动。");
			}
			else{
				readMoQueDBThread = new ReadMoQueDBThread();
				readMoQueDBThread.start();
				//设置线程已启动
				readMoQueDBThread.setThreadStart(true);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动处理数据库mo消息线程异常");
		}
	}
	
	/**
	 * 启动rpt处理线程，用于处理接收rpt缓冲队列
	 */
	private void startSendRptThread(){
		try
		{
			if(sendRptThread != null && sendRptThread.isThreadStart()){
				EmpExecutionContext.info("启动rpt处理线程，线程已启动，不再启动。");
			}
			else{
				sendRptThread = new SendRptThread();
				sendRptThread.start();
				//设置线程已启动
				sendRptThread.setThreadStart(true);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动rpt处理线程异常");
		}
	}
	
	/**
	 * 启动处理数据库rpt消息线程
	 */
	private void startReadRptQueDBThread(){
		try
		{
			if(readRptQueDBThread != null && readRptQueDBThread.isThreadStart()){
				EmpExecutionContext.info("启动处理数据库rpt消息线程，线程已启动，不再启动。");
			}
			else{
				readRptQueDBThread = new ReadRptQueDBThread();
				readRptQueDBThread.start();
				//设置线程已启动
				readRptQueDBThread.setThreadStart(true);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动处理数据库rpt消息线程异常");
		}
	}
	
	/**
	 * 初始化重连
	 */
	private void initReconnect(){
		if(tExit == null){
			tExit = new Timer();
		}
		//初始化重连参数
		setReConnParamInit();
	}
	
	/**
	 * 启动app网关自动重连
	 * @return 成功返回true
	 */
	public boolean startAutoLogin(){
		try
		{
			if(getReConnState() == 2){
				EmpExecutionContext.info("不需要再启动App网关自动重连，重连已开启。");
				return true;
			}
			
			//设置状态为定时重连中
			setReConnStateRuning();
			
			initReconnect();
			//设置重连定时任务
			tExit.schedule(new timetask(), baseLoginTime);
			EmpExecutionContext.info("App网关自动重连。设置定时任务成功。将在"+(baseLoginTime/1000)+"秒后重试第"+loginCount+"次。");
			return true;
		}
		catch (Exception e)
		{
			//异常则设置状态为未定时重连
			setReConnStateStop();
			
			EmpExecutionContext.error(e, "启动app网关自动重连异常。");
			return false;
		}
	}
	
	/**
	 * 登录App平台
	 * @return 
	 		 1	登录成功。
	 		-1	未配置企业账户。
	 		-2	host或port为空。
	 		-3	用户名或密码为空。
	 		-4	初始化登录xmpp参数异常。
	 		-5	连接并登录xmpp异常。
	 		-8	未定义异常。
			401	用户名密码无效。
			408	用户请求超时。
			409	冲突错误。
			501	服务器不提供此功能。
			502	服务器内部错误。
			504	连接服务器超时。
			其他	不可恢复错误。
	 * 
	 */
	public int Login(){
		
		EmpExecutionContext.info("外部调用Emp网关登录接口。");
		
		//初始化登录参数
		int initRes = initAppLoginInfo();
		
		EmpExecutionContext.info("外部调用Emp网关登录接口。初始化登录参数结果："+initRes);
		
		if(initRes != 2){
			return initRes;
		}
		
		int loginRes = login(host, port, ecode, corpUser, password);
		
		EmpExecutionContext.info("外部调用Emp网关登录接口。登录结果："+loginRes);
		
		//登录成功，则加载用户账号
		if(loginRes == 1){
			loadUserCodeFromDB();
		}
		
		//调用app平台登录接口
		return loginRes;
	}
	
	/**
	 * 服务器启动时登录
	 * @return
			 1	登录成功。
	 		-1	未配置企业账户。
	 		-2	host或port为空。
	 		-3	用户名或密码为空。
	 		-4	初始化登录xmpp参数异常。
	 		-5	连接并登录xmpp异常。
	 		-8	未定义异常。
			401	用户名密码无效。
			408	用户请求超时。
			409	冲突错误。
			501	服务器不提供此功能。
			502	服务器内部错误。
			504	连接服务器超时。
			其他	不可恢复错误。
	 */
	public int LoginForServiceStart(){
		
		try
		{
			int loginRes = Login();
			//登录成功
			if(loginRes == 1){
				return loginRes;
			}
			
			//由于配置不正确导致的失败，则直接返回，不做重连
			if(loginRes == -1 || loginRes == -2 || loginRes == -3 || loginRes == -4){
				return loginRes;
			}
			
			//登录失败，启动重连
			boolean startRes = startAutoLogin();
			EmpExecutionContext.info("服务器启动时登录失败，启动自动重连结果："+startRes);
			
			return loginRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "服务器启动时登录异常。");
			return -8;
		}
	}
	
	/**
	 * 初始化登录参数
	 * @return 
	 		 2	初始化成功。
	 		-1	未配置企业账户。
	 		-2	host或port为空。
	 		-3	用户名或密码为空。
	 		-4	初始化登录参数异常。
	 * 
	 */
	public int initAppLoginInfo(){
		
		try
		{
			//查询出企业账户信息
			List<LfAppAccount> appAccoutsList =  empDao.findListByCondition(LfAppAccount.class, null, null);
			if(appAccoutsList == null || appAccoutsList.size() == 0){
				EmpExecutionContext.error("APP初始化登录参数失败，未配置企业账户信息。");
				return -1;
			}
			
			host = appAccoutsList.get(0).getUrl();
			port = appAccoutsList.get(0).getPort();
			if(host == null || host.length() == 0 || port == null){
				EmpExecutionContext.error("APP初始化登录参数失败，host或port为空。");
				return -2;
			}
			
			ecode = appAccoutsList.get(0).getFakeid();
			corpUser = appAccoutsList.get(0).getCode();
			password = appAccoutsList.get(0).getPwd();
			if(ecode == null || ecode.length() == 0 || corpUser == null || corpUser.length() == 0 || password == null || password.length() == 0){
				
				EmpExecutionContext.error("APP初始化登录参数失败，企业编码、用户名或密码为空。ecode="+ecode+";corpUser="+corpUser+";password="+password);
				return -3;
			}
			
			//更新文件服务器地址
			WgMwFileBiz.setFileSvrUrl(appAccoutsList.get(0).getFileSvrUrl());
			
			EmpExecutionContext.info("APP初始化登录参数成功。");
			return 2;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "APP初始化登录参数异常。");
			return -4;
		}
		
	}
	
	/**
	 * 
	 * @param host app平台ip
	 * @param port app平台端口
	 * @param corpUser 企业账户
	 * @param password 企业账户密码
	 * @return
			 1	登录成功。
			-2	host或port为空。
	 		-3	用户名或密码为空。
			-5	连接并登录xmpp异常。
	 		-8	未定义异常。
			401	用户名密码无效。
			408	用户请求超时。
			409	冲突错误。
			501	服务器不提供此功能。
			502	服务器内部错误。
			504	连接服务器超时。
			其他	不可恢复错误。
	 */
	public int login(String host, Integer port, String ecode, String corpUser, String password){
		
		if(host == null || host.length() == 0 || port == null){
			EmpExecutionContext.error("登录梦网平台失败，host或port为空。host="+host+";port="+port);
			return -2;
		}
		if(ecode == null || ecode.length() == 0 || corpUser == null || corpUser.length() == 0 || password == null || password.length() == 0){
			EmpExecutionContext.error("登录梦网平台失败，企业编码、用户名或密码为空。ecode="+ecode+";corpUser="+corpUser+";password="+password);
			return -3;
		}
		
		try {
			
			//已登录，则退出，因为ip和端口等信息可能改，需要初始化连接和重新登录
			if(getXmppConnState() == 1){
				server.getConnection().disconnect();
			}
			//未登录，但连接过，这要断开连接，回收资源
			else if(server != null && server.getConnection() != null){
				server.getConnection().disconnect();
			}
			
			//初始化并连接
			if(server != null){
				server.init(host, port, reconnectionAllowed);
				//登录
				server.login(ecode, corpUser, password);
			}

			//登录成功
			if(getXmppConnState() == 1){
				EmpExecutionContext.info("调用内部登录，登录App平台成功。");
				//启动线程
				startThread();
				return 1;
			}
			else{
				EmpExecutionContext.error("登录App平台，连接并登录失败。");
				
				if(server != null && server.getConnection() != null){
					server.getConnection().disconnect();
				}
				
				return -5;
			}
			
		} 
		catch (EMPException e) {
			EmpExecutionContext.error(e, "连接并登录xmpp失败。");
			if(e !=null && e.getXMPPError() != null){
				return e.getXMPPError().getCode();
			}
			if(server != null && server.getConnection() != null){
				server.getConnection().disconnect();
			}
			return -5;
		}
		catch(Exception e){
			EmpExecutionContext.error(e, "登录App平台异常。");
			return -8;
		}
	}
	
	
	
	/**
	 * 获取xmpp连接登录状态
	 * @return 1：已连接且已登录；-1:未连接；-2：未登录；-3：状态未知；-4：连接未初始化；-5：异常；-6：未初始化
	 */
	public int getXmppConnState(){
		
		try
		{
			if(server == null){
				EmpExecutionContext.debug("获取xmpp连接登录状态：服务未初始化。");
				return -6;
			}
			//连接未初始化
			else if(server.getConnection() == null){
				EmpExecutionContext.debug("获取xmpp连接登录状态：连接未初始化。");
				return -4;
			}
			//已连接且已登录
			else if(server.getConnection().isConnected() && server.getConnection().isAuthenticated()){
				return 1;
			}
			//未连接
			else if(!server.getConnection().isConnected()){
				EmpExecutionContext.debug("获取xmpp连接登录状态：未连接。");
				return -1;
			}
			//未登录
			else if(!server.getConnection().isAuthenticated()){
				EmpExecutionContext.debug("获取xmpp连接登录状态：未登录。");
				return -2;
			}
			else{
				EmpExecutionContext.debug("获取xmpp连接登录状态：未知状态。");
				return -3;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取xmpp连接登录状态异常。");
			return -5;
		}
		

	}

	/**
	 * 获取App平台企业编码
	 * @return 返回当前登录的企业编码
	 */
	public String getAppECode(){
		if(ecode == null){
			int res = initAppLoginInfo();
			EmpExecutionContext.info("获取App平台企业编码为空，重新初始化结果："+res);
		}
		return ecode;
	}
	
	/**
	 * 获取流水号。 (系统时间戳,格式为: yyMMddHHmmss_发送者用户账号(11位)_XXXXXXX（7位随机码）
	 * @param fromUserName 发送者用户账号
	 * @return 返回流水号字符串
	 */
	public String getSerial(String fromUserName){
		RandomCount count = RandomCount.getInstance();
		//不足11位，则在前面补足0
		if(fromUserName.length() < 11){
			int size = 11-fromUserName.length();
			for(int i=0; i < size; i++){
				
				fromUserName = "0" + fromUserName;
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String serial = sdf.format(new Date()) + "_" + fromUserName + "_" + count.getCount();
		return serial;
	}
	
	
	/**
	 * 设置连接监听器
	 */
	private void setConnectionListener(){
		connListenner = new ConnectionListener() {
			
			public void reconnectionSuccessful() {
				EmpExecutionContext.error("Xmpp重连成功。");
			}
			
			public void reconnectionFailed(Exception e) {
				EmpExecutionContext.error(e, "Xmpp自动重连失败。");
				//重连失败，则重新登录
				/*if(getXmppConnState() != 1){
					int loginRes = login(host, port, ecode, corpUser, password);
					if(loginRes != 1){
						EmpExecutionContext.error("Xmpp重连失败。重连失败，错误码："+loginRes);
					}
					else{
						EmpExecutionContext.info("Xmpp重连失败。尝试重连成功。");
					}
				}*/
			}
			
			public void reconnectingIn(int seconds) {
				EmpExecutionContext.error("Xmpp重连接时间："+seconds);
			}
			
			public void connectionClosedOnError(Exception e) {
				try
				{
					EmpExecutionContext.error(e, "Xmpp发生错误连接被关闭。");
					//获取是否由于重复登录导致被服务器踢下线，true为账号重复登录
					boolean error = e.getMessage().equals("stream:error (conflict)");
					//账号重复登录，则不重连
					if(error){
						EmpExecutionContext.error(e, "Xmpp发生错误连接被关闭。重复登录导致，不重连。");
						return;
					}
					
					//已连接则不需要做处理
					if(getXmppConnState() == 1){
						EmpExecutionContext.info("Xmpp发生错误连接被关闭。但当前已连接，不需要重连。");
						return;
					}
					
					int loginRes = login(host, port, ecode, corpUser, password);
					if(loginRes != 1){
						//马上重新登录不上，则定时登录
						startAutoLogin();
						EmpExecutionContext.error("Xmpp发生错误连接被关闭。重连失败。错误码："+loginRes+"，"+(baseLoginTime/1000)+"秒后重试。");
					}
					else{
						EmpExecutionContext.info("Xmpp发生错误连接被关闭。重连成功。");
					}
				}
				catch (Exception e1)
				{
					EmpExecutionContext.error(e1, "Xmpp发生错误连接被关闭。处理异常。");
				}
				
			}
			
			public void connectionClosed() {
				EmpExecutionContext.error("Xmpp连接被关闭。");
			}
		};
		
		server.addListener(connListenner);
	}
	
	class timetask extends TimerTask {

		public void run() {
			
			//已连接登录，则不需要再重连
			if(getXmppConnState() == 1){
				//已连接登录，则设置状态为未重连
				setReConnStateStop();
				EmpExecutionContext.info("App网关自动重连。已连接，不需要重连，重连任务退出。");
				return;
			}
			
			//重新登录
			int loginRes = login(host, port, ecode, corpUser, password);
			
			//登录失败，继续定时下一次
			if(loginRes != 1){
				EmpExecutionContext.error("App网关自动重连。重连失败。错误码："+loginRes+"。已重试第"+loginCount+"次。");
				//如果重试次数值超过5，或者重试间隔超过10分钟，则可能前面计算有异常，这里初始化恢复
				if(loginCount > baseLoginCount || loginTime > 600000){
					EmpExecutionContext.error("App网关自动重连。重连参数不正确，初始化重连参数。当前重试次数="+loginCount+",当前重试间隔="+loginTime);
					//重试参数设置为初始值
					setReConnParamInit();
				}
				//重连次数加1
				loginCount++;
				try
				{
					//已尝试5次仍失败，则5分钟后继续
					if(loginCount > baseLoginCount){
						tExit.schedule(new timetask(), loginWaitTime);
						//一轮重连完成，重试参数设置为初始值
						setReConnParamInit();
						EmpExecutionContext.info("App网关自动重连。设置定时任务成功。将在"+(loginWaitTime/1000)+"秒后进行下一轮重试的第"+loginCount+"次。");
					}
					else{
						//每重试一次，就乘以3倍时间
						loginTime = loginTime * baseLoginMult;
						//防止异常情况
						if(loginTime < 0 || tExit == null){
							//初始化定时参数
							initReconnect();
						}
						tExit.schedule(new timetask(), loginTime);
						EmpExecutionContext.info("App网关自动重连。设置定时任务成功。将在"+(loginTime/1000)+"秒后重试第"+loginCount+"次。");
					}
				}
				catch (Exception e)
				{
					//设置重连失败，则设置状态为未重连
					setReConnStateStop();
					//设置重连失败，重试参数设置为初始值
					setReConnParamInit();
					EmpExecutionContext.error(e, "App网关自动重连。设置重连定时任务异常。重连终止。");
				}
			}
			else{
				//重连成功，则设置状态为未重连
				setReConnStateStop();
				//重连成功，重试参数设置为初始值
				setReConnParamInit();
				EmpExecutionContext.info("App网关自动重连。重连成功。");
			}
			
			
		}
	}
	
	/**
	 * 设置公众消息回执监听器
	 */
	private void setEMessageReceiptListener(){
		eMsgListener = new EMessageReceiptListener(){

			public void processEMessageReceipt(EMessage eMsg)
			{
				new HandleRespMsgBIz().RespEMRecHandle(eMsg);
			}
			
		};
		
		server.addListener(eMsgListener);
	}
	
	/**
	 * 更新app消息下行记录回执状态
	 * @param sendstate 更新的状态
	 * @param appMsgId 消息流水号
	 * @return 成功返回true
	 */
	public boolean updateMtMsg(String sendstate, String appMsgId, String tousername){
		try
		{
			if(sendstate == null || sendstate.trim().length() ==0) {
				EmpExecutionContext.error("更新App消息下行记录状态失败，状态为空。");
				return false;
			}
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("rptState", sendstate);
			
			SimpleDateFormat sdfT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String recTime = sdfT.format(new Date());
			objectMap.put("recRPTTime", recTime);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("appMsgId", appMsgId);
			
			//把分号换成逗号
			tousername = tousername.replace(splitSymbol, ",");
			
			conditionMap.put("tousername&in", tousername);
			
			Integer res = empDao.updateBySymbolsCondition(LfAppMtmsg.class, objectMap, conditionMap);
			return res != null && res > 0;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新App消息下行记录状态异常。");
			return false;
		}
	}
	
	/**
	 * 设置个人消息监听器
	 */
	private void setPMessageListener(){
		
		pMsgListener = new PMessageListener(){

			public void processPMessage(PMessage pmessage)
			{
				new HandleMsgBiz().MoRecHandle(pmessage);
			}

		};
		
		server.addListener(pMsgListener);
	}
	
	
	public int testUpMsg(String appconde, String nickName, String content, int outline){
		PMessage pmessage = new PMessage();
		pmessage.setEcode(ecode);
		String serial = getSerial(ecode);
		pmessage.setSerial(serial);
		pmessage.setIcode(207);
		pmessage.setRcode(0);
		
		if(outline == 1){
			
			pmessage.addExtension(new PacketExtension() {
				
				public String toXML() {
					StringBuffer sb = new StringBuffer();
					sb.append("<").append(getElementName()).append(" xmlns=\"")
					.append(getNamespace()).append("\" from=\"").append("msgserver")
					.append("\" stamp=\"").append("2014-09-04T02:58:57.305+0000").append("\">");
					return sb.toString();
				}
				
				public String getNamespace() {
					return "jabber:x:delay";
				}
				
				public String getElementName() {
					return "x";
				}
			});
		}
		
		PMessageModel model = new PMessageModel();
		model.setFrom(appconde);
		model.setFname(nickName);
		model.setTo(ecode);
		model.setTname(ecode);
		model.setMsg_src(11);
		
		List<PMessageStyle> stylesList = new ArrayList<PMessageStyle>();
		PMessageStyle1 style = new PMessageStyle1();
		style.setContent(content);
		stylesList.add(style);
		
		model.setBody(stylesList);
		
		pmessage.addPModel(model);
		
		new HandleMsgBiz().MoRecHandle(pmessage);
		return 2;
	}
	
	public int testRptMsg(String appconde, Integer icode){
		String serial = getSerial(appconde);
		StateReportMessage pmessage = new StateReportMessage(serial, ecode, icode);
		pmessage.setRcode(0);
		StateReportModel model = new StateReportModel();
		model.setErrcode("DELIVRD");
		model.setState(0);
		model.setUsername(appconde);
		
		pmessage.addStateReportModel(model);
		
		new HandleMsgBiz().RptMsgRecHandle(pmessage);
		return 0;
	}
	
	
	
	/**
	 * 获取用户信息消息回执消息对象
	 * @param userInfomessage 接收到的用户信息消息对象
	 * @param rcode 状态，0 成功   1 失败
	 * @return 返回用户信息消息回执消息对象
	 */
	private UserInfoMessage getUserInfoReceiptMsg(UserInfoMessage userInfomessage, Integer rcode){
		
		try
		{
			UserInfoMessage r = new UserInfoMessage(userInfomessage.getSerial(), userInfomessage.getEcode(), userInfomessage.getIcode()*10, rcode, "");
			r.setPacketID(userInfomessage.getPacketID());
			r.setType(userInfomessage.getType());
			r.setFrom(userInfomessage.getTo());
			r.setTo(userInfomessage.getFrom());
			
			PacketExtension extension = userInfomessage.getExtension("x", "jabber:x:delay");
			if (extension != null) {
				r.addExtension(extension);
			}
			/*String subject = userInfomessage.getSubject();
			if (subject != null) {
				r.setSubject(subject);
			}
			String body = userInfomessage.getBody();
			if (body != null) {
				r.setBody(body);
			}
			Collection<PacketExtension> extensions = userInfomessage.getExtensions();
			if (extensions != null) {
				r.addExtensions(extensions);
			}
			XMPPError error = userInfomessage.getError();
			if (error != null) {
				r.setError(error);
			}*/
			r.addUserInfo(userInfomessage.getuserInfo());
			return r;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取用户信息消息回执消息对象异常。");
			return null;
		}
	}
	
	
	/**
	 * 设置个人消息回执监听器
	 */
	private void setPMessageReceiptListener(){
		
		pMsgReceiptListener = new PMessageReceiptListener(){

			public void processPMessageReceipt(PMessage pMsg)
			{
				new HandleRespMsgBIz().RespPMRecHandle(pMsg);
			}

		};
		
		server.addListener(pMsgReceiptListener);
	}
	
	/**
	 * 设置状态报告监听器
	 */
	private void setStateReportMessageListener(){
		stateReportMessageListener = new StateReportMessageListener(){

			public void processMessage(StateReportMessage rptMsg)
			{
				new HandleMsgBiz().RptMsgRecHandle(rptMsg);
				
			}

		};
		
		server.addListener(stateReportMessageListener);
	}
	
	/**
	 * 设置用户信息同步监听器
	 */
	private void setSynUserInfoListener(){
		
		synUserInfoListener = new SynUserInfoListener(){

			public void processSynUserInfo(UserInfoMessage userInfoMsg)
			{
				EmpExecutionContext.appRequestInfoLog("接收用户同步消息:"+userInfoMsg.toXML());
				EmpExecutionContext.info("接收用户同步消息:"+userInfoMsg.toXML());
				//处理用户
				boolean res = dealUserInfoMsg(userInfoMsg.getuserInfo(), userInfoMsg.getEcode());
				
				//发送回执
				sendRecSynUserInfoReceipt(userInfoMsg, res);
				
				EmpExecutionContext.info("用户信息同步结果:"+res+"。流水号："+userInfoMsg.getSerial());
			}

		};
		
		server.addListener(synUserInfoListener);
	}
	
	public boolean testSynuser(){
		List<UserInfoModel> userInfoModelList = new ArrayList<UserInfoModel>();
		UserInfoModel userModel = new UserInfoModel();
		
		userModel.setUtype(4);
		userModel.setUname("13800000009");
		userModel.setName("aa2");
		userModel.setNname("aa2");
		userModel.setSex(1);
		userModel.setMobile("13800000009");
		userModel.setQq("3015421472");
		userModel.setEmail("2@qq.com");
		userModel.setCdate(new Date().getTime());
		userModel.setMdate(new Date().getTime());
		//操作类型1：新增; 2：修改; 3：删除
		userModel.setOptype(1);

		userInfoModelList.add(userModel);
		
		boolean res = dealUserInfoMsg(userInfoModelList, "SZ10001");
		return res;
	}
	
	public void testBatchSynuser(){
		int iCount = 100;
		for(int i=10; i<iCount; i++)
		{
			String temp = String.valueOf(i);
			while(temp.length() < 8)
			{
				temp = "0" + temp;
			}
			String name = "130" + temp;
			List<UserInfoModel> userInfoModelList = new ArrayList<UserInfoModel>();
			UserInfoModel userModel = new UserInfoModel();
			
			userModel.setUtype(4);
			userModel.setUname(name);
			userModel.setName("APP" +temp);
			userModel.setNname("APP" +temp);
			userModel.setSex(1);
			userModel.setMobile(name);
			userModel.setQq("3015421472");
			userModel.setEmail("2@qq.com");
			userModel.setCdate(new Date().getTime());
			userModel.setMdate(new Date().getTime());
			//操作类型1：新增; 2：修改; 3：删除
			userModel.setOptype(1);
			
			userInfoModelList.add(userModel);
			
			boolean res = dealUserInfoMsg(userInfoModelList, "SZ10001");
			System.out.println(res);
		}
	}	
	
	/**
	 * 发送同步用户信息回执
	 * @param userInfoMsg 消息对象
	 * @param res 结果，true表示成功
	 * @return 发送成功返回true
	 */
	private boolean sendRecSynUserInfoReceipt(UserInfoMessage userInfoMsg, boolean res){
		
		try
		{
			UserInfoMessage receiptMsg = getUserInfoReceiptMsg(userInfoMsg, res?0:1);
			
			if(receiptMsg == null){
				EmpExecutionContext.error("发送同步用户回执失败，用户信息消息回执对象为空。");
				return false;
			}
			
			
			EmpExecutionContext.info("发送同步用户回执消息xml："+receiptMsg.toXML());
			
			//返回回执给app平台
			server.pushUserInfoMessage(receiptMsg);
			
			EmpExecutionContext.appRequestInfoLog("回应用户同步消息:"+userInfoMsg.toXML());
			
			EmpExecutionContext.info("发送同步用户回执成功。流水号："+receiptMsg.getSerial());
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送同步用户信息回执异常");
			return false;
		}
		
	}

	/**
	 * 处理用户信息同步
	 * @param userInfoModelList 用户信息集合
	 * @param ecode 企业编码
	 * @return 成功返回true
	 */
	private boolean dealUserInfoMsg(List<UserInfoModel> userInfoModelList, String ecode){
		
		if(userInfoModelList == null || userInfoModelList.size() == 0){
			EmpExecutionContext.error("用户信息同步失败，用户信息集合为空。");
			return false;
		}
		
		try
		{
			//批量新增集合
			List<LfAppMwClient> addUsersList = new ArrayList<LfAppMwClient>();
			//修改集合
			List<LfAppMwClient> updateUsersList = new ArrayList<LfAppMwClient>();
			//删除集合
			List<LfAppMwClient> delUsersList = new ArrayList<LfAppMwClient>();
			//手机号集合，用户获取客户通讯录guid
			List<String> phonesList = new ArrayList<String>();
			
			LfAppMwClient user = null;
			int size = userInfoModelList.size();
			for(int i=0; i<size; i++){
				user = this.getClientInfo(userInfoModelList.get(i), ecode);
				
				//操作类型1：新增; 2：修改; 3：删除
				if(userInfoModelList.get(i).getOptype() == 1 && user != null){
					//新增
					addUsersList.add(user);
					//把手机号放入
					phonesList.add(user.getPhone());
				}
				else if(userInfoModelList.get(i).getOptype() == 2 && user != null){
					//修改
					updateUsersList.add(user);
					//把手机号放入
					phonesList.add(user.getPhone());
				}
				else if(userInfoModelList.get(i).getOptype() == 3){
					//删除
					delUsersList.add(user);
				}
			}
			
			Map<String,Long> phoneGuidMap = getUserGuidByPhone(phonesList);
			
			//持久化
			boolean res = this.dealUser(phoneGuidMap, addUsersList, updateUsersList, delUsersList, ecode);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理用户信息同步异常。");
			return false;
		}
	}
	
	/**
	 * 根据手机号获取用户guid
	 * @param phonesList 手机号集合
	 * @return 返回手机号guidmap集合，key为手机号，value为guid
	 */
	private Map<String,Long> getUserGuidByPhone(List<String> phonesList){
		Map<String,Long> phoneGuidMap = new HashMap<String,Long>();
		try
		{
			if(phonesList == null || phonesList.size() == 0){
				return phoneGuidMap;
			}
			
			StringBuffer phonesSb = new StringBuffer();
			int size = phonesList.size();
			int count = 0;
			for(int i = 0; i < size; i++){
				count++;
				phonesSb.append(phonesList.get(i));
				if(i < size -1){
					phonesSb.append(",");
				}
				//每1千个手机号查询一次
				if(count == 1000){
					phoneGuidMap.putAll(getClientGuidByPhone(phonesSb.toString()));
					count = 0;
					phonesSb = new StringBuffer();
				}
			}
			//有剩余的，也要处理
			if(phonesSb.length() > 0){
				phoneGuidMap.putAll(getClientGuidByPhone(phonesSb.toString()));
				count = 0;
				phonesSb.setLength(0);
			}
			
			return phoneGuidMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据手机号获取用户guid异常。");
			return phoneGuidMap;
		}
	}
	
	/**
	 * 通过手机号获取客户GUID
	 * @param phones 手机号，格式：手机号1,手机号2,手机号3
	 * @return 返回手机号guidmap集合，key为手机号，value为guid
	 */
	private Map<String,Long> getClientGuidByPhone(String phones){
		Map<String,Long> phoneGuidMap = new HashMap<String,Long>();
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("mobile&in", phones);
			
			List<LfClient> clientsList = empDao.findListBySymbolsCondition(LfClient.class, conditionMap, null);
			
			if(clientsList == null || clientsList.size() == 0){
				return phoneGuidMap;
			}
			int size = clientsList.size();
			
			for(int i = 0; i < size; i++){
				if(clientsList.get(i).getMobile() == null || clientsList.get(i).getGuId() == null){
					continue;
				}
				phoneGuidMap.put(clientsList.get(i).getMobile(), clientsList.get(i).getGuId());
			}
			return phoneGuidMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过手机号获取客户GUID异常。");
			return phoneGuidMap;
		}
	}
	
	/**
	 * 数据库操作用户信息
	 * @param phoneGuidMap 手机号guid集合
	 * @param addUsersList 新增的用户信息集合
	 * @param updateUsersList 更新的用户信息集合
	 * @param delUsersList 删除的用户信息集合
	 * @return 成功返回true
	 */
	private boolean dealUser(Map<String,Long> phoneGuidMap, List<LfAppMwClient> addUsersList, List<LfAppMwClient> updateUsersList, List<LfAppMwClient> delUsersList, String ecode){
		
		Connection conn = empTransDao.getConnection();
		try
		{
			//过滤重复号码
			addUsersList = this.filterRepeatAddUser(addUsersList, ecode);
			
			empTransDao.beginTransaction(conn);
			
			this.addUser(phoneGuidMap, addUsersList, conn);
			this.updateUser(phoneGuidMap, updateUsersList, conn, ecode);
			this.delUser(delUsersList, conn);
			
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "处理用户信息异常。");
			return false;
		}
		finally{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 过滤数据库中已存在用户
	 * @param addUsersList 用户信息集合
	 * @param ecode 企业编码
	 * @return 返回已过滤重复用户信息集合
	 */
	private List<LfAppMwClient> filterRepeatAddUser(List<LfAppMwClient> addUsersList, String ecode){
		
		if(addUsersList == null || addUsersList.size() == 0){
			EmpExecutionContext.info("无用户信息要新增，无需去重复。");
			return addUsersList;
		}
		
		try
		{
			int size = addUsersList.size();
			
			String appcodes = "";
			for(int i = 0; i<size; i++){
				appcodes += addUsersList.get(i).getAppcode();
				if(i < size - 1 ){
					appcodes += ",";
				}
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("ecode", ecode);
			conditionMap.put("appcode&in", appcodes);
			//查询是否已存在
			List<LfAppMwClient> clientsList = empDao.findListBySymbolsCondition(LfAppMwClient.class, conditionMap, null);
			//没重复
			if(clientsList == null || clientsList.size() == 0){
				return addUsersList;
			}
			//过滤掉重复
			for(LfAppMwClient rpClient : clientsList){
				for(int i = 0; i<addUsersList.size(); i++){
					//有重复
					if(addUsersList.get(i).getAppcode().equals(rpClient.getAppcode())){
						EmpExecutionContext.info("过滤重复新增用户："+addUsersList.get(i).getAppcode());
						addUsersList.remove(i);
					}
				}
			}
			
			return addUsersList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "保存用户信息异常。");
			return addUsersList;
		}
	}
	
	/**
	 * 保存用户信息
	 * @param phoneGuidMap 手机号guid集合
	 * @param addUsersList 用户信息集合
	 * @return 成功返回true
	 */
	private boolean addUser(Map<String,Long> phoneGuidMap, List<LfAppMwClient> addUsersList, Connection conn){
		
		if(addUsersList == null || addUsersList.size() == 0){
			EmpExecutionContext.info("无用户信息要新增。");
			return true;
		}
		
		try
		{
			int size = addUsersList.size();
			Long guid = GlobalVariableBiz.getInstance().getValueByKey("guid", size);
			if(guid == null){
				EmpExecutionContext.error("保存用户信息失败，guid为null");
				return false;
			}
			//用户账号map
			Map<String,String> userCodeMap = new HashMap<String,String>();
			
			Long clientGuid = null;
			
			List<LfClientMultiPro> addClientList = new ArrayList<LfClientMultiPro>();
			List<LfClientDepSp> clientDepspList = new ArrayList<LfClientDepSp>();
			
			for(int i = 0; i<size; i++){
				userCodeMap.put(addUsersList.get(i).getAppcode(), addUsersList.get(i).getAppcode());
				
				clientGuid = phoneGuidMap.get(addUsersList.get(i).getPhone());
				//已有对应的客户，则填入客户的guid
				if(clientGuid != null){
					addUsersList.get(i).setGuid(clientGuid);
				}
				//没对应的客户，则新增
				else{
					LfClientMultiPro client = getClient(addUsersList.get(i), guid-i, 2L, "100001");
					addClientList.add(client);
					
					addUsersList.get(i).setGuid(guid-i);
					LfClientDepSp clientDepsp = null;
					if(client != null){
						clientDepsp = getClientDepSp(client.getClientId());
					}

					if(clientDepsp != null)
					{
						clientDepspList.add(clientDepsp);
					}
				}
			}
			
			int res = empTransDao.save(conn, addUsersList, LfAppMwClient.class);
			//保存到客户通讯录
			if(addClientList != null && addClientList.size() > 0){
				empTransDao.save(conn, addClientList, LfClientMultiPro.class);
				empTransDao.save(conn, clientDepspList, LfClientDepSp.class);
			}
			if(res > 0){
				//已成功同步的，则加入内存
				userCodeMap.putAll(userCodeMap);
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "保存用户信息异常。");
			return false;
		}
	}
	
	/**
	 * 获取获取客户通讯录对象
	 * @param appUser
	 * @param guid
	 * @param userId
	 * @param corpCode
	 * @return
	 */
	private LfClientMultiPro getClient(LfAppMwClient appUser, Long guid, Long userId, String corpCode){
		try
		{
			LfClientMultiPro client = new LfClientMultiPro();
			client.setCorpCode(corpCode);
			//没姓名则用昵称
			if(appUser.getUname() == null || appUser.getUname().length() == 0){
				client.setName(appUser.getNickname());
			}
			else{
				client.setName(appUser.getUname());
			}
			// 手机
			client.setMobile(appUser.getPhone());
			// 性别
			client.setSex(Integer.parseInt(appUser.getSex()));
			
			//这里是新增
			client.setGuId(guid);
			client.setClientId(guid);
			// 转换成大写插入数据库
			//client.setRecState(null);
			//client.setShareState(null);
			//client.setHideState(null);
			client.setDepId(0L);
			client.setClientCode(String.valueOf(guid));

			//client.setJob(job);
			//client.setProfession(pro);
			//client.setEname(eName);
			client.setQq(appUser.getQq());
			//client.setArea(area);
			//client.setMsn(msn);
			//client.setComments(comm);
			client.setEMail(appUser.getEmail());
			//client.setOph(oph);
			//client.setBirthday(DateFormatter.swicthSqltring(appUser.getb +" 00:00:00"));
			client.setUserId(userId);
			return client;
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "获取客户通讯录对象异常。用户账号："+appUser.getAppcode());
			return null;
		}
	}
	
	/**
	 * 获取客户通讯录机构绑定对象
	 * @return
	 */
	private LfClientDepSp getClientDepSp(Long clientId){
		try
		{
			LfClientDepSp clientDepsp = new LfClientDepSp();
			clientDepsp.setClientId(clientId);
			
			Long depId = null;
			//如果已经有未知机构id，则直接使用
			if(unKnowDepId != null){
				depId = unKnowDepId;
			}
			//没这id则获取一个
			else{
				depId = getUnknowCLientDepId();
				unKnowDepId = depId;
			}
			
			
			if(depId == null){
				EmpExecutionContext.error("获取客户通讯录机构绑定对象。机构id为null。");
				return null;
			}
			clientDepsp.setDepId(depId);
			return clientDepsp;
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "获取客户通讯录机构绑定对象异常。clientId="+clientId);
			return null;
		}
	}
	
	/**
	 * 获取未知机构id
	 * @return 返回机构id，异常返回null
	 */
	private Long getUnknowCLientDepId(){
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("depId", "-10");
			List<LfClientDep> LfClientDepList = empDao.findListByCondition(LfClientDep.class, conditionMap, null);
			if(LfClientDepList != null && LfClientDepList.size() > 0)
			{
				return depId;
			}
			EmpExecutionContext.error("获取客户通讯录APP未挂接用户机构，未获取到机构id，机构不存在。");
			return null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取客户通讯录APP未挂接用户机构异常。");
			return null;
		}
	}
	
	
	
	/**
	 * 更新用户信息
	 * @param phoneGuidMap 手机号guid集合
	 * @param updateUsersList 用户信息集合
	 * @return 成功返回true
	 */
	private boolean updateUser(Map<String,Long> phoneGuidMap, List<LfAppMwClient> updateUsersList, Connection conn, String ecode){
		
		if(updateUsersList == null || updateUsersList.size() == 0){
			EmpExecutionContext.info("无用户信息要更新。");
			return true;
		}
		try
		{
			//更新列
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			//更新条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			//客户通讯录修改值
			LinkedHashMap<String, String> cliObjectMap = new LinkedHashMap<String, String>();
			//客户通讯录修改条件
			LinkedHashMap<String, String> cliConditionMap = new LinkedHashMap<String, String>();
			
			int size = updateUsersList.size();
			Long clientGuid = null;
			
			boolean updateRes = false;
			
			List<LfClientMultiPro> addClientList = new ArrayList<LfClientMultiPro>();
			List<LfClientDepSp> clientDepspList = new ArrayList<LfClientDepSp>();
			
			for(int i=0; i<size; i++){
				
				conditionMap.put("ecode", updateUsersList.get(i).getEcode());
				conditionMap.put("appcode", updateUsersList.get(i).getAppcode());
				
				//objectMap.put("synid", updateUsersList.get(i).getSynid());
				if(updateUsersList.get(i).getUtype() != null){
					objectMap.put("utype", String.valueOf(updateUsersList.get(i).getUtype()));
				}
				if(updateUsersList.get(i).getUname() != null ){
					objectMap.put("uname", updateUsersList.get(i).getUname());
					cliObjectMap.put("name", updateUsersList.get(i).getUname());
				}
				//没姓名则更新昵称作为姓名
				if(updateUsersList.get(i).getUname() == null && updateUsersList.get(i).getNickname() != null){
					cliObjectMap.put("name", updateUsersList.get(i).getNickname());
				}
				if(updateUsersList.get(i).getNickname() != null ){
					objectMap.put("nickname", updateUsersList.get(i).getNickname());
				}
				if(updateUsersList.get(i).getSex() != null ){
					objectMap.put("sex", updateUsersList.get(i).getSex());
					cliObjectMap.put("sex", updateUsersList.get(i).getSex());
				}
				if(updateUsersList.get(i).getPhone() != null ){
					objectMap.put("phone", updateUsersList.get(i).getPhone());
				}
				if(updateUsersList.get(i).getQq() != null ){
					objectMap.put("qq", updateUsersList.get(i).getQq());
					cliObjectMap.put("qq", updateUsersList.get(i).getQq());
				}
				if(updateUsersList.get(i).getEmail() != null ){
					objectMap.put("email", updateUsersList.get(i).getEmail());
					cliObjectMap.put("EMail", updateUsersList.get(i).getEmail());
				}
				if(updateUsersList.get(i).getHeadimgurl() != null ){
					objectMap.put("headimgurl", updateUsersList.get(i).getHeadimgurl());
				}
				if(updateUsersList.get(i).getLocalimgurl() != null ){
					objectMap.put("localimgurl", updateUsersList.get(i).getLocalimgurl());
				}
				
				Long guid = null;
				clientGuid = phoneGuidMap.get(updateUsersList.get(i).getPhone());
				//如果有guid，则更新进去
				if(clientGuid != null){
					guid = clientGuid;
					objectMap.put("guid", clientGuid.toString());
					cliConditionMap.put("guId", clientGuid.toString());
					//有该客户则更新
					//empTransDao.update(conn, LfClient.class, cliObjectMap, cliConditionMap);
				}
				//没对应的客户，则新增
				else{
					guid = GlobalVariableBiz.getInstance().getValueByKey("guid", 1);
					LfClientMultiPro client = getClient(updateUsersList.get(i), guid, 2L, "100001");
					addClientList.add(client);
					
					objectMap.put("guid", guid.toString());
					
					LfClientDepSp clientDepsp = null;
					if(client != null){
						clientDepsp = getClientDepSp(client.getClientId());
					}

					if(clientDepsp != null)
					{
						clientDepspList.add(clientDepsp);
					}
				}
				
				if(objectMap.size() == 0){
					continue;
				}
				
				updateRes = empTransDao.update(conn, LfAppMwClient.class, objectMap, conditionMap);
				
				//如果没更新到，则是不存在的用户，新增进去
				if(updateRes){
					//已成功同步的，则加入内存
					userCodeMap.put(updateUsersList.get(i).getAppcode(), updateUsersList.get(i).getAppcode());
					EmpExecutionContext.error("更新用户信息成功。appcode="+updateUsersList.get(i).getAppcode()+"，ecode="+updateUsersList.get(i).getEcode());
				}
				else{
					EmpExecutionContext.error("更新用户信息失败。用户不存在。appcode="+updateUsersList.get(i).getAppcode()+"，ecode="+updateUsersList.get(i).getEcode());
					LfAppMwClient user = this.getUserAllInfo(updateUsersList.get(i).getAppcode(), ecode);
					if(user != null){
						user.setGuid(guid);
						EmpExecutionContext.error("更新用户信息失败。用户不存在，保存用户。appcode="+updateUsersList.get(i).getAppcode()+"，ecode="+updateUsersList.get(i).getEcode());
						empTransDao.save(conn, user);
						//已成功同步的，则加入内存
						userCodeMap.put(updateUsersList.get(i).getAppcode(), updateUsersList.get(i).getAppcode());
						EmpExecutionContext.error("更新用户信息失败。用户不存在，保存用户成功。appcode="+updateUsersList.get(i).getAppcode()+"，ecode="+updateUsersList.get(i).getEcode());
					}
				}
				
				objectMap.clear();
				cliObjectMap.clear();
				//清楚条件
				conditionMap.clear();
				cliConditionMap.clear();
			}
			
			//保存到客户通讯录
			if(addClientList != null && addClientList.size() > 0){
				empTransDao.save(conn, addClientList, LfClientMultiPro.class);
				empTransDao.save(conn, clientDepspList, LfClientDepSp.class);
			}
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新用户信息异常。");
			return false;
		}
	}
	
	/**
	 * 获取并同步用户
	 * @param uName 用户账号
	 * @param ecode 企业编码
	 * @return 成功则返回用户对象，异常返回null
	 */
	public LfAppMwClient SynUserInfo(String uName, String ecode){
		try
		{
			LfAppMwClient user = this.getUserAllInfo(uName, ecode);
			if(user == null){
				EmpExecutionContext.error("获取并同步用户失败，用户对象为null。appcode="+uName);
				return null;
			}
			
			//检查用户是否存在
			boolean checkRes = checkUserExits(user.getAppcode(), user.getEcode());
			if(checkRes){
				//已同步的用户加入内存
				userCodeMap.put(user.getAppcode(), user.getAppcode());
				EmpExecutionContext.info("获取并同步用户。用户已存在。appcode="+user.getAppcode()+"，ecode="+user.getEcode());
				return user;
			}
			
			EmpExecutionContext.info("获取并同步用户。用户不存在，新增。appcode="+user.getAppcode()+"，ecode="+user.getEcode());
			boolean addRes = addUser(user);
			if(addRes){
				//已同步的用户加入内存
				userCodeMap.put(user.getAppcode(), user.getAppcode());
				EmpExecutionContext.error("获取并同步用户。新增成功。appcode="+user.getAppcode());
			}else{
				EmpExecutionContext.error("获取并同步用户。新增失败。appcode="+user.getAppcode());
				//同步失败
				return null;
			}
			
			return user;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取并同步用户异常。appcode="+uName);
			return null;
		}
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	private boolean addUser(LfAppMwClient user){
		
		Connection conn = empTransDao.getConnection();
		try
		{
			//手机号集合，用户获取客户通讯录guid
			List<String> phonesList = new ArrayList<String>();
			phonesList.add(user.getPhone());
			Map<String,Long> phoneGuidMap = getUserGuidByPhone(phonesList);
			
			empTransDao.beginTransaction(conn);
			Long clientGuid = phoneGuidMap.get(user.getPhone());
			//已有对应的客户，则填入客户的guid
			if(clientGuid != null){
				user.setGuid(clientGuid);
			}
			//没对应的客户，则新增
			else{
				Long guid = GlobalVariableBiz.getInstance().getValueByKey("guid", 1);
				LfClientMultiPro client = getClient(user, guid, 2L, "100001");
				empTransDao.save(conn, client);
				user.setGuid(guid);
				LfClientDepSp clientDepsp = null;
				if(client != null){
					clientDepsp = getClientDepSp(client.getClientId());
				}

				if(clientDepsp != null)
				{
					empTransDao.save(conn, clientDepsp);
				}
			}
			empTransDao.save(conn, user);
			
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "保存用户和客户信息异常。");
			return false;
		}
		finally{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 检查用户是否存在
	 * @param appcode
	 * @param ecode
	 * @return true为存在
	 */
	private boolean checkUserExits(String appcode, String ecode){
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("ecode", ecode);
			conditionMap.put("appcode", appcode);
			List<LfAppMwClient> usersList = empDao.findListByCondition(LfAppMwClient.class, conditionMap, null);
			return usersList == null || usersList.size() > 0;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "检查用户是否存在异常。");
			return true;
		}
	}
	
	/**
	 * 根据用户名获取该用户全部信息
	 * @param uName 用户名
	 * @return 返回客户对象，没找到或异常返回null
	 */
	private LfAppMwClient getUserAllInfo(String uName, String ecode){
		try
		{
			EmpExecutionContext.info("获取用户全部信息。ecode="+ecode+"，username="+uName);
			String serial = getSerial(uName);
			UserInfoMessage userInfoMsg = server.getSynUserInfo(ecode, uName, serial);
			if(userInfoMsg == null){
				EmpExecutionContext.error("获取用户全部信息失败，用户信息对象UserInfoMessage为null。");
				return null;
			}
			
			EmpExecutionContext.info("获取用户全部信息接口返回消息xml："+userInfoMsg.toXML());
			
			List<UserInfoModel> userModelsList = userInfoMsg.getuserInfo();
			if(userModelsList == null || userModelsList.size() == 0){
				EmpExecutionContext.error("获取用户全部信息失败，用户对象集合为空。");
				return null;
			}

			LfAppMwClient user = null;

			for(UserInfoModel userModel : userModelsList){
				//获取指定的用户
				if(uName.equals(userModel.getUname())){
					user = getClientInfo(userModel, ecode);
					return user;
				}
			}
			//没找到就返回null
			return null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据用户获取该用户全部信息异常。");
			return null;
		}
	}
	
	/**
	 * 根据用户信息同步对象获取客户对象
	 * @param userModel 用户信息同步对象
	 * @return 返回客户对象，异常返回null
	 */
	private LfAppMwClient getClientInfo(UserInfoModel userModel, String ecode){
		
		try
		{
			LfAppMwClient user = new LfAppMwClient();
			user.setEcode(ecode.toUpperCase());
			//user.setSynid(userInfoModelList.get(i).getSyn_id());
			user.setUtype(userModel.getUtype());
			user.setAppcode(userModel.getUname());
			user.setUname(userModel.getName());
			user.setNickname(userModel.getNname());
			user.setSex(String.valueOf(userModel.getSex()));
			user.setPhone(userModel.getMobile());
			user.setQq(userModel.getQq());
			user.setEmail(userModel.getEmail());
			if(userModel.getAvatar() != null && userModel.getAvatar().trim().length() > 0){
				//个人头像地址
				user.setHeadimgurl(userModel.getAvatar());
				//下载头像
				String localUrl = downloadHeadPic(userModel.getAvatar());
				user.setLocalimgurl(localUrl);
			}
			
			if(userModel.getCdate() != null){
				user.setCreatetime(new Timestamp(userModel.getCdate()));
				user.setVerifytime(new Timestamp(userModel.getCdate()));
			}
			if(userModel.getMdate() != null){
				user.setModifytime(new Timestamp(userModel.getMdate()));
			}
			return user;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据用户信息同步对象获取客户对象异常。");
			return null;
		}
	}
	
	/**
	 * 下载用户头像
	 * @param headUrl 头像app文件服务器url
	 * @return 成功下载返回本地url，失败返回null
	 */
	private String downloadHeadPic(String headUrl){
		try
		{
			String webUrl = new TxtFileUtil().getWebRoot();
			
			String downloadDir = new WgMwFileBiz().getFileSvrDownSvt();
			int index = 0;
			if(headUrl.indexOf(downloadDir)!=-1){
				index = headUrl.indexOf(downloadDir)+downloadDir.length()+1;
			}
			String fileUrl = headUrl.substring(index);
			String picUrl = "file/app/"+fileUrl;
			
			boolean downRes = new WgMwFileBiz().downloadFromMwFileSer(webUrl+picUrl, headUrl);
			if(downRes){
				return picUrl;
			}
			else{
				return null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下载用户头像失败。");
			return null;
		}
	}
	
	/**
	 * 删除用户信息
	 * @param delUsersList 用户信息集合
	 * @return 成功返回true
	 */
	private boolean delUser(List<LfAppMwClient> delUsersList, Connection conn){
		
		if(delUsersList == null || delUsersList.size() == 0){
			EmpExecutionContext.info("无用户信息要删除。");
			return true;
		}
		try
		{
			//更新条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			int size = delUsersList.size();

			for(int i=0; i<size; i++){
				conditionMap.put("ecode", delUsersList.get(i).getEcode());
				conditionMap.put("appcode", delUsersList.get(i).getAppcode());
				
				WgDAO wgDao = new WgDAO();
				//删除群组的成员关系
				if(wgDao.delGroupUser(conn, delUsersList.get(i).getCorpcode(), delUsersList.get(i).getAppcode())){
					EmpExecutionContext.error("删除用户信息失败，删除群组关系失败。");
					return false;
				}
				
				//删除用户
				empTransDao.delete(conn, LfAppMwClient.class, conditionMap);
				
				//清楚条件
				conditionMap.clear();
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新用户信息异常。");
			return false;
		}
	}
	
	public XMPPServer getXMPPServer(){
		return server;
	}

	public Map<String, String> getUserCodeMap()
	{
		return userCodeMap;
	}

	public String getEcode()
	{
		return ecode;
	}

	public String getHost()
	{
		return host;
	}

	public Integer getPort()
	{
		return port;
	}

	public String getPassword()
	{
		return password;
	}

	public String getCorpUser()
	{
		return corpUser;
	}

	public void pushMessage(Message message) {
		message.setFrom(server.getConnection().getUser());
		message.setTo(server.getConnection().getServiceName());
		message.setType(Message.Type.normal);
		server.getConnection().sendPacket(message);
	}
	
	

	
}
