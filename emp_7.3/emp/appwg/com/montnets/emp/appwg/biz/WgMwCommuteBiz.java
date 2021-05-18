package com.montnets.emp.appwg.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.app.UserInfoMessage;
import com.montnets.app.UserInfoModel;
import com.montnets.emp.appwg.initappplat.AppSdkPackage;
import com.montnets.emp.appwg.wginterface.IWgMwCommuteBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.appmage.LfAppMwClient;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfClientDepSp;
import com.montnets.emp.entity.client.LfClientMultiPro;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 
 * @project p_appwg
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-6-11 下午04:28:41
 * @description app网关与App梦网平台通信逻辑类
 */
public class WgMwCommuteBiz extends SuperBiz implements IWgMwCommuteBiz
{
	
	
	//未知机构id，默认为-10
	private static Long unKnowDepId = null;
	
	private static long depId = -10l;
	
	
	/**
	 * 从数据库加载用户账号
	 * @return 成功返回true
	 */
	private boolean loadUserCodeFromDB(){
		if(AppSdkPackage.getInstance().getEcode() == null || AppSdkPackage.getInstance().getEcode().trim().length() == 0){
			EmpExecutionContext.error("从数据库加载用户账号失败，企业编码为空。");
			return false;
		}
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("ecode", AppSdkPackage.getInstance().getEcode());
			List<LfAppMwClient> usersList = empDao.findListByCondition(LfAppMwClient.class, conditionMap, null);
			if(usersList == null || usersList.size() == 0){
				EmpExecutionContext.info("从数据库加载用户账号，无用户。");
				return true;
			}
			for(LfAppMwClient user : usersList){
				AppSdkPackage.getInstance().getUserCodeMap().put(user.getAppcode(), user.getAppcode());
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
		AppSdkPackage.getInstance().InitMwApp();
	}
	
	/**
	 * 停止app网关
	 */
	public void StopAppWg(){
		AppSdkPackage.getInstance().StopAppWg();
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
		int initRes = AppSdkPackage.getInstance().initAppLoginInfo();
		
		EmpExecutionContext.info("外部调用Emp网关登录接口。初始化登录参数结果："+initRes);
		
		if(initRes != 2){
			return initRes;
		}
		
		int loginRes = AppSdkPackage.getInstance().login(AppSdkPackage.getInstance().getHost(), AppSdkPackage.getInstance().getPort(), AppSdkPackage.getInstance().getEcode(), AppSdkPackage.getInstance().getCorpUser(), AppSdkPackage.getInstance().getPassword());
		
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
			boolean startRes = AppSdkPackage.getInstance().startAutoLogin();
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
	 * 获取App平台企业编码
	 * @return 返回当前登录的企业编码
	 */
	public String getAppECode(){
		if(AppSdkPackage.getInstance().getEcode() == null){
			int res = AppSdkPackage.getInstance().initAppLoginInfo();
			EmpExecutionContext.info("获取App平台企业编码为空，重新初始化结果："+res);
		}
		return AppSdkPackage.getInstance().getEcode();
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
				AppSdkPackage.getInstance().getUserCodeMap().put(user.getAppcode(), user.getAppcode());
				EmpExecutionContext.info("获取并同步用户。用户已存在。appcode="+user.getAppcode()+"，ecode="+user.getEcode());
				return user;
			}
			
			EmpExecutionContext.info("获取并同步用户。用户不存在，新增。appcode="+user.getAppcode()+"，ecode="+user.getEcode());
			boolean addRes = addUser(user);
			if(addRes){
				//已同步的用户加入内存
				AppSdkPackage.getInstance().getUserCodeMap().put(user.getAppcode(), user.getAppcode());
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
			if(usersList == null || usersList.size() > 0){
				return true;
			}
			return false;
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
			String serial =AppSdkPackage.getInstance().getSerial(uName);
			
			EmpExecutionContext.appRequestInfoLog("请求获取用户信息:ecode="+ecode+",username="+uName+",serial="+serial);
			
			UserInfoMessage userInfoMsg = AppSdkPackage.getInstance().getXMPPServer().getSynUserInfo(ecode, uName, serial);
			if(userInfoMsg == null){
				EmpExecutionContext.appRequestInfoLog("响应获取用户信息:null");
				EmpExecutionContext.error("获取用户全部信息失败，用户信息对象UserInfoMessage为null。");
				return null;
			}
			EmpExecutionContext.appRequestInfoLog("响应获取用户信息:"+userInfoMsg.toXML());
			
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
	
	
	

}
