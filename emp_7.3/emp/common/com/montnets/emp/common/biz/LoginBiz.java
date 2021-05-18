package com.montnets.emp.common.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.common.constant.PropertiesLoader;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.dao.impl.GenericEmpTransactionDAO;
import com.montnets.emp.common.vo.LfPrivilegeAndMenuVo;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.monitoronline.LfMonOnluser;
import com.montnets.emp.entity.securectrl.LfDynPhoneWord;
import com.montnets.emp.entity.securectrl.LfMacIp;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.MD5;

/**
 * 用户登录biz
 * @author Administrator
 *
 */
public class LoginBiz extends SuperBiz
{
	
	IEmpDAO genericBaseDAO= new DataAccessDriver().getEmpDAO();
	private SpecialDAO specialDAO = new SpecialDAO();
	/**
	 * 操作员登录
	 * @param userName
	 * @param pass
	 * @param corpCode
	 * @return
	 */
	public LfSysuser login(String userName, String pass, String corpCode) {
 
		LfSysuser lfsysuser = null;
		String md5Str = "";
		try{
			//根据用户名，密码，企业编码查找操作员对象
			List<LfSysuser> lfSysuserList=null;

			//先按新密码规则查询
			md5Str = MD5.getMD5Str(pass + userName.toLowerCase());
			lfSysuserList= specialDAO.getLfSysUserByUP(userName.toUpperCase(),(pass==null?null:md5Str),corpCode);
			if((lfSysuserList==null||lfSysuserList.size()<1) && "admin".equals(userName))
			{
				//按照老密码查询
				lfSysuserList= specialDAO.getLfSysUserByUP(userName.toUpperCase(),(pass==null?null:MD5.getMD5Str(pass)),corpCode);
				//根据老密码查询到记录
				//	正确密码+admin 的格式会通过老密码的方式查询到用户，所以这里排除掉以admin结尾的密码
				if(lfSysuserList != null && lfSysuserList.size() > 0 && !pass.endsWith("admin"))
				{
					lfsysuser = lfSysuserList.get(0);
					//老密码更改为新密码
					lfsysuser.setPassword(md5Str);
					//调用公共修改方法
					genericBaseDAO.update(lfsysuser);
				}
			}else if(lfSysuserList!=null&&lfSysuserList.size()>0){
				lfsysuser = lfSysuserList.get(0);
			}
			if(lfsysuser == null ){
				EmpExecutionContext.error("登录账户和密码不匹配，u:"+userName+"，p:"+md5Str+"，c:"+corpCode);
			}

 		}catch(Exception e){
 			EmpExecutionContext.error(e, "操作员登录异常。u:"+userName+"，p:"+md5Str+"，c:"+corpCode);
		}
 		//返回用户信息
		return lfsysuser;
	}
	
	/**
	 * 保存用户数据
	 * @param pass 密码
	 * @param sysuser 用户实体
	 */
	public void setErrorTimes(String pass,LfSysuser sysuser){
		String md5Pass = MD5.getMD5Str(pass);
		String oldmd5Pass = MD5.getMD5Str(pass+sysuser.getUserName().toLowerCase());
		//状态为停用的用户账户不会被锁定
		try{
		if(!md5Pass.equals(sysuser.getPassword())||!oldmd5Pass.equals(sysuser.getPassword())){
			String limit=getCorpConfByLimt(sysuser);
			//如果密码输入错误，累加错误次数 admin 不做数据处理
			if(!"".equals(limit)&&!"0".equals(limit)&&!"admin".equals(sysuser.getUserName())){
			if(sysuser.getPwderrortimes()<=Integer.parseInt(limit)-1){
				sysuser.setPwderrortimes(sysuser.getPwderrortimes()+1);
			}
			if(sysuser.getPwderrortimes()==Integer.parseInt(limit)){
				sysuser.setUserState(3);//表示锁定
			}
			genericBaseDAO.update(sysuser);
			}
		}
		}catch(Exception e){
 			EmpExecutionContext.error(e, "保存用户数据异常。");
		}
		
	}
	
	
	/***
	 *  获得系统中配置信息
	 * @param sysuser
	 * @return
	 */
	public String getCorpConfByLimt(LfSysuser sysuser){
		String result="";
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", sysuser.getCorpCode());

		String errorlimt="0";

		try
		{
			BaseBiz baseBiz = new BaseBiz();
			List<LfCorpConf> lfCorpConfList= baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);

			for(int i=0;i<lfCorpConfList.size();i++){
			LfCorpConf conf=lfCorpConfList.get(i);
			//错误上限
			if("pwd.errlimit".equals(conf.getParamKey())){
                String value = conf.getParamValue();
				if(value != null && !"".equals(value)&&!"0".equals(value))
				 errorlimt=conf.getParamValue();

			}
		}
			result=errorlimt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询密码设置规则异常！");
		}
		return result;
	}
	
	/**
	 * 获取登录用户操作页面的当前位置
	 * @return
	 * @throws Exception
	 */
	Map<String, List<LfPrivilege>> prMap=new HashMap<String, List<LfPrivilege>>();
	public Map<String,String> getTitleMap(String sysuserId) throws Exception
	{
		List<LfPrivilege> prList=specialDAO.findPrivilegesBySysuserId(sysuserId, "MENU");
		prMap.put(sysuserId, prList);
		Map<String, String> titleMap=new HashMap<String, String>();
		String modName="";
		String modCode="";
		int size=prList.size();
		for(int i=0;i<size;i++)
		{
			LfPrivilege pri=prList.get(i);
			String modCode2 = pri.getMenuCode().substring(0,pri.getMenuCode().indexOf("-"));
			if(!modCode2.equals(modCode))
			{
				modCode = modCode2;
				modName=pri.getModName();
				titleMap.put(modCode, modName);
			}
			titleMap.put(pri.getMenuCode(), pri.getMenuName());
			String menuSite = pri.getMenuSite();
			if(menuSite == null || "".equals(menuSite))
			{
				continue;
			}
			if(menuSite.indexOf("/") == menuSite.lastIndexOf("/"))
			{
				
				titleMap.put(menuSite.substring(menuSite.indexOf("_")+1,
						menuSite.lastIndexOf(".")), pri.getMenuCode());
			}else
			{
			
				titleMap.put(menuSite.substring(menuSite.lastIndexOf("/")+1,
					menuSite.lastIndexOf(".")), pri.getMenuCode());
			}
		}
		return titleMap;
	}
	
	/**
	 * 查询当前登录用户的模块权限
	 * @param userId
	 * @param menuMap
	 * @return
	 * @throws Exception
	 */
	public List<LfPrivilege> getMenuByPrivCodeAsc(String userId)throws Exception{
		List<LfPrivilege> privileges = new ArrayList<LfPrivilege>();
		try{
			List<LfPrivilege> lfPrivilegeList =null;
			if(prMap!=null&&prMap.containsKey(userId)){
				lfPrivilegeList=prMap.get(userId);
			}else{
				 lfPrivilegeList =  specialDAO.findPrivilegesBySysuserId(userId, "MENU");
			}
			String modName2="";
			for(int i=0;i<lfPrivilegeList.size();i++)
			{
				LfPrivilege pri=lfPrivilegeList.get(i);
				if(!modName2.equals(pri.getMenuName()) && pri.getMenuSite()!=null)
				{
					modName2=pri.getMenuName();
					privileges.add(pri);
				}
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "查询当前登录用户的模块权限异常！");
		}
		return privileges;
	}
	
	/**
	 * 查询当前登录用户按钮权限
	 * @param userId
	 * @return btnMap
	 * @throws Exception
	 */
	public Map<String,String> getBtnFuntionMap(String langName, String userId)throws Exception{
		LinkedHashMap<String,String> btnMap = new LinkedHashMap<String,String>();
 		List<LfPrivilege> privlegeList = new ArrayList<LfPrivilege>();
		try{
		
			privlegeList = specialDAO.findPrivilegesBySysuserId(userId, "BUTTON");
			for(int index=0;index<privlegeList.size();index++){
				LfPrivilege lfPrivilege =privlegeList.get(index);
				if(StaticValue.ZH_HK.equals(langName)){
					btnMap.put(lfPrivilege.getPrivCode(),lfPrivilege.getZhHkComments());
				}else if(StaticValue.ZH_TW.equals(langName)){
					btnMap.put(lfPrivilege.getPrivCode(),lfPrivilege.getZhTwComments());
				}else{
					btnMap.put(lfPrivilege.getPrivCode(),lfPrivilege.getComments());
				}
			}
	 
		}catch(Exception e){
			EmpExecutionContext.error(e, "查询当前登录用户按钮权限异常！");
		}
		return btnMap;
	}
	
	/**
	 *根据登录用户 id查询此用户绑定权限的所有模块信息
	 * @param userId
	 * prType 0 ,1
	 * @return
	 * @throws Exception
	 */
	public List<LfPrivilegeAndMenuVo> getAllMenuByUserId(String corpCode,String userId,String prType) throws Exception
	{
		List<LfPrivilegeAndMenuVo> lfprivilegeAndMenuVoList = new ArrayList<LfPrivilegeAndMenuVo>();
		try {
			lfprivilegeAndMenuVoList = specialDAO.findAllMenuVo(corpCode,userId,prType);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据登录用户 id查询此用户绑定权限的所有模块信息失败！");
		}
		return lfprivilegeAndMenuVoList;
	}
	
	public List<LfSysuser> getLfSysUserByUP(String userName, String passWord,String corpCode) throws Exception
	{
		return specialDAO.getLfSysUserByUP(userName,passWord,corpCode);
	}
	
	/**
	 * 初始化读取配置文件信息
	 */
	public void initMenu(String path)
	{
		try
		{
			
			PropertiesLoader propertiesLoader = new PropertiesLoader();
			Properties properties =null;
			try
			{
				
				properties = propertiesLoader.getProperties(path);
			}
			catch (Exception e) {
				EmpExecutionContext.error(e, "当前文件找不到！");
			}
			if(properties!=null)
			{
				
				Set keyValue = properties.keySet();
				for (Iterator it = keyValue.iterator(); it.hasNext();)
				{
					String key = (String) it.next();
					String value=(String)properties.get(key);
					StaticValue.getInniMenuMap().put(value, value);
				}
				String menu_num=properties.getProperty("menu_num").toString();
				//StaticValue.menu_num.append(menu_num+",");
				StaticValue.getMenu_num().append(menu_num+",");
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "初始化读取配置文件信息异常。");
		}
	}

	/**
	 * 判断是否为配置用户
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean isConfigAdmin(String username,String password){
	    //托管版不处理mwadmin的判断
	    if(StaticValue.getCORPTYPE() == 1)
        {
            return false;
        }
		String configAdmin = SystemGlobals.getValue("configAdmin");
		String configAdminPwd = SystemGlobals.getValue("configAdminPwd");
		String configpwd = password == null ? "" : MD5.getMD5Str(password);
		return configAdmin.equals(username) && configpwd.equals(configAdminPwd);
	}

	/**
	 * 安全验证 ip mac 以及动态口令
	 */
	public String securityValidation(String ipAddr,String macAddr,String phoneword,LfSysuser sysuser) throws Exception {
		// 根据当前用户的guid查找此用户是否绑定了mac-ip和是否启用了动态口令登录
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("guid", sysuser.getGuId().toString());
		List<LfMacIp> lfMacIps = empDao.findListByCondition(LfMacIp.class, conditionMap, null);
		if (lfMacIps == null || lfMacIps.size() == 0) {
			// 未启用直接登录
			return "0";
		}
		LfMacIp macIp = lfMacIps.get(0);
		boolean isIpNull = macIp.getIpaddr() == null || "".equals(macIp.getIpaddr().trim());// 是否绑定ip
		boolean isMacNull = macIp.getMacaddr() == null || "".equals(macIp.getMacaddr().trim());// 是否绑定mac
		if (isIpNull && isMacNull && macIp.getDtpwd() == 0) {
			// 未绑定直接登录
			return "0";
		}
		if (!isIpNull) {
			// ip地址验证
			if (ipAddr == null || "".equals(ipAddr)) {
				return "nogetIP";
			}
			boolean canLogin = false;
			String ips[] = ipAddr.split(",");
			for (String str : ips) {
				if (macIp.getIpaddr().indexOf(str) != -1) {
					canLogin = true;
					break;
				}
			}
			if (!canLogin) {
				return "IPAddrerror";
			}
		}
		if (!isMacNull) {

			// 如查获取mac地址失败
			if (macAddr == null || "".equals(macAddr)) {
				return "nogetMacAddr";
			}
			macAddr = macAddr.replaceAll(":", "-");
			String macs[] = macAddr.split(",");
			boolean canLogin = false;
			for (String str : macs) {
				if (macIp.getMacaddr().indexOf(str) != -1) {
					canLogin = true;
					break;
				}
			}
			if (!canLogin) {
				return "MacAddrerror";
			}
		}
		// 启用了动态口令登录
		if (macIp.getDtpwd() == 1) {
			if(phoneword == null || "".equals(phoneword.trim())){
				return "nophoneword";
			}
			// 清空map
			conditionMap.clear();
			// 用户id
			conditionMap.put("userId", sysuser.getUserId().toString());
			conditionMap.put("phoneWord", phoneword);
			List<LfDynPhoneWord> PhoneWordList = empDao.findListByCondition(LfDynPhoneWord.class, conditionMap, null);
			// 动态口令输入正确
			if(PhoneWordList != null && PhoneWordList.size() > 0)
			{
				/*// 格式化时间
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				// 日历
				// 当前时间
				Calendar c = Calendar.getInstance();
				String nowDate = format.format(c.getTime());
				LfDynPhoneWord pword = PhoneWordList.get(0);
				// 如果输入的动态口令与当前登录时间不在同一天，则此动态口令已过期
				if(!nowDate.equals(format.format(pword.getCreateTime())))
				{
					return "overtimes";
				}*/
				LfDynPhoneWord pword = PhoneWordList.get(0);
				if(Calendar.getInstance().getTime().getTime()-pword.getCreateTime().getTime()>120*1000){
					return "overtimes";
				}
			}
			else
			{
				return "phoneworderror";
			}
			LinkedHashMap<String,String> objectMap = new LinkedHashMap<String, String>();
			// 设置为登录过一次
			objectMap.put("isLogin", "1");
			empDao.update(LfDynPhoneWord.class, objectMap, conditionMap);
		}
		return "0";
	}
	
	/**
	 *  更新在线用户人数
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-12 下午04:52:34
	 */
	public void setMonOnlcfg()
	{
		specialDAO.setMonOnlcfg();
	}
	
	/**
	 * 设置在线用户详细信息
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-12 下午05:46:34
	 */
	public void setOnlineUser()
	{
		try
		{
			List<LfMonOnluser> lfMonOnlcfgList = new BaseBiz().getByCondition(LfMonOnluser .class, null, null);
			Iterator<LfMonOnluser> itr = lfMonOnlcfgList.iterator();
			LfMonOnluser lfMonOnluser;
			StaticValue.mon_OnlineUserMap.clear();
			while(itr.hasNext())
			{
				lfMonOnluser = itr.next();
				// 主机基本信息写入缓存
				StaticValue.mon_OnlineUserMap.put(lfMonOnluser.getSesseionid(), lfMonOnluser);
			}
			//设置临时缓存
			StaticValue.mon_OnlineUserMapTemp.clear();
			StaticValue.mon_OnlineUserMapTemp.putAll(StaticValue.mon_OnlineUserMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置在线用户详细信息异常!");
		}
	}
	
	/**
     * 更新对象信息到数据库
     * @param obj 实体对象
     * @param key update条件中key值
     */
    public void saveOrUpdate(Object obj,String key){
    	SuperBiz superBiz = new SuperBiz();
        GenericEmpTransactionDAO genericEmpTransactionDAO = new GenericEmpTransactionDAO();
        Connection connection = superBiz.empTransDao.getConnection();
        try {
        	superBiz.empTransDao.beginTransaction(connection);
            boolean up = genericEmpTransactionDAO.updateConOld(connection,obj,key);
            if(!up)
            {
                genericEmpTransactionDAO.saveConOld(connection,obj);
            }
            superBiz.empTransDao.commitTransaction(connection);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"更新对象信息异常！key:"+key+"，className:"+obj.getClass().getName());
            superBiz.empTransDao.rollBackTransaction(connection);
        } finally {
        	superBiz.empTransDao.closeConnection(connection);
        }
    }

	public List<LfPrivilege> getLfShortTempList(String corpcode,Long userId,HttpServletRequest req) {
		return specialDAO.getLfShortTempList(corpcode,userId,req);
	}

	public List<LfPrivilege> getLfShortTempList(String corpcode,Long userId) {
		return specialDAO.getLfShortTempList(corpcode,userId);
	}
	/**
	 * 根据企业编码查询企业信息
	 * @param corpCode
	 * @return
	 */
	public LfCorp getCorp(String corpCode){
		try {
			return specialDAO.getCorp(corpCode);
		} catch (Exception e) {
			EmpExecutionContext.error("登录查询企业失败，企业编码："+corpCode);
			return null;
		}
	}
}