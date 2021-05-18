package com.montnets.emp.rms.report.biz;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.context.ErrorLoger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class QueryBiz extends BaseBiz 
{
	ErrorLoger errorLoger = new ErrorLoger();
	/**
	 * 获取sp账号
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<Userdata> getAllUserdata(int type,String userids) throws Exception { //
		List<Userdata> userDatasList = null;
		//条件map
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		//排序map
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String,String>();
		try{
			conditionMap.put("uid&>", "100001");
			conditionMap.put("accouttype", "1");//只查询短信发送账号
			if("".equals(userids))
				conditionMap.put("userId", "-1");
			else
				
				conditionMap.put("userId&in", userids);
			
			if(type==1)
			{
				conditionMap.put("userType", "0");
			}else if(type==2)
			{
				conditionMap.put("userType", "1");
			}
			orderMap.put("userId",StaticValue.ASC);
			//调用查询方法
            userDatasList = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
		}catch(Exception e){
			EmpExecutionContext.error(e,"获取SP账号异常");
			//抛出异常
			throw e;
			
		}
		//返回查询结果
		return userDatasList;
	}
	

	/**
	 * 短彩SP账号List
	 * @param mstype
	 * 0短信 1彩信
	 * @param corpcode
	 * 企业编码
	 * @param corptype
	 * 0 单企业 1多企业
	 * @return
	 * @throws Exception
	 */
	public List<String> getSpuserList(String mstype,String corpcode,Integer corptype)throws Exception{
		List<String> user_List = new ArrayList<String>(); // 大写发送账号
		if(corpcode==null){
			return user_List;
		}
		//多企业
		if(corptype==1){
			LinkedHashMap<String, String> conditionMMap = new LinkedHashMap<String, String>();
			//不是十万的企业
			if(!"100000".equals(corpcode))
			{
				// 如果是梦网则查询所有企业发送账号
				conditionMMap.put("corpCode", corpcode);
			}
			if("0".equals(mstype)){
				// 短信账号
				List<LfSpDepBind> lfsp = new ArrayList<LfSpDepBind>();
				//短信
				lfsp = this.getByCondition(LfSpDepBind.class, conditionMMap, null);
				if(lfsp != null)
				{
					for (int i = 0; i < lfsp.size(); i++)
					{
						user_List.add(lfsp.get(i).getSpUser().toUpperCase());
					}
				}
			}else if("1".equals(mstype)){
				// 彩信账号
				List<LfMmsAccbind> lfmmssp = new ArrayList<LfMmsAccbind>();
				//彩信
				lfmmssp = this.getByCondition(LfMmsAccbind.class, conditionMMap, null);
				if(lfmmssp != null)
				{
					for (int i = 0; i < lfmmssp.size(); i++)
					{
						user_List.add(lfmmssp.get(i).getMmsUser().toUpperCase());
					}
				}
			}
		}else{
			//单企业
			List<Userdata> usersp = new ArrayList<Userdata>();
			//条件map
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
			//排序map
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String,String>();
			conditionMap.put("uid&>", "100001");
			String accouttype="1";
			if("0".equals(mstype)){
				accouttype="1";
			}else if("1".equals(mstype)){
				accouttype="2";
			}
			conditionMap.put("accouttype", accouttype);//只查询短信发送账号
			conditionMap.put("userType", "0");
			orderMap.put("userId",StaticValue.ASC);
			
			usersp=empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
			if(usersp != null)
			{
				for (int i = 0; i < usersp.size(); i++)
				{
					user_List.add(usersp.get(i).getUserId().toUpperCase());
				}
			}
		}
		return user_List;
		

	
	}
	
	/**
	 * 写日志
	 * 
	 * @param request
	 *        请求对象
	 * @param opModule
	 *        菜单名称
	 * @param opContent
	 *        操作内容
	 * @param opType
	 */
	public void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,opModule+opType+opContent+"日志写入异常"));
		}
	}
	
	/**
	 * 获取企业编码
	 * @param request
	 * @return
	 */
	public String getCorpCode(HttpServletRequest request){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				if(loginSysuser!=null&&!"".equals(loginSysuser.getCorpCode())){
					return loginSysuser.getCorpCode();
				}else{
					return "-9999";
				}
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error("SESSION获取企业编码失败");
			return "-9999";
		}
		return "-9999";
	}
	
	
	
	
	
	
	
	
	
	
	
	
}