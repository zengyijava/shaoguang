package com.montnets.emp.monitor.util;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.email.Aes;
import com.montnets.emp.email.MailInfo;
import com.montnets.emp.email.SendMail;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.monitor.constant.MonEmailParams;
import com.montnets.emp.util.SuperOpLog;

/**
 * 
 * @功能概要：监控平台公共方法
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-6 下午03:21:54
 */
public class EmailSendServlet
{
	/**
	 * list对象发送邮件(由于邮件的类型不一样，分开发送)
	 * @description           			 
	 * @author zhangsan <zhangsan@126.com>
	 * @datetime 2016-4-6 下午04:06:02
	 */
	public void ListSendMail(List<MonEmailParams> paraList){
		if(paraList!=null&&paraList.size()>0){
			for(int i=0;i<paraList.size();i++){
				MonEmailParams para=paraList.get(i);
				try
				{
					sendMail(para.getContent(),para.getCorpCode(),para.getEmail());
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e,"平台监控邮件发送异常！");
				}
			}
		}
	}
	
	
	/***
	 * 监控平台邮件处理
	 * @description    
	 * @param content
	 * @param corpCode
	 * @param email
	 * @throws Exception       			 
	 * @author zhangsan <zhangsan@126.com>
	 * @datetime 2016-4-6 下午03:29:38
	 */
	public void sendMail(String content,String corpCode,String email) throws Exception{
		if(email!=null&&!"".equals(email)){
		LfSysuser AdminUser = null; 
		//给下级发送的邮箱地址列表
		LinkedHashMap<String, String> condition  = new LinkedHashMap<String, String>();
		condition.put("corpCode", corpCode);
		BaseBiz	baseBiz	= new BaseBiz();
		List<LfCorpConf> lfCorpConfList  =baseBiz.getByCondition(LfCorpConf.class,condition,null);
		LinkedHashMap<String, String> conditionMap  = new LinkedHashMap<String, String>();
    	conditionMap.put("userName", "admin");//admin用户
		conditionMap.put("corpCode", corpCode);//当前登录用户的企业编码
		List<LfSysuser> AdminLfSysuerList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null) ;
		if(AdminLfSysuerList!=null && AdminLfSysuerList.size()>0)
		{
			//系统admin
			AdminUser = AdminLfSysuerList.get(0);
		}else{
			String str = " 获取该企业的admin失败。";
			EmpExecutionContext.error(str);
			return;
		}
		String protocol="";
		String host="";
		String port="";
		String username="";
		String passw="";
		if(lfCorpConfList!=null){
			for(int i=0;i<lfCorpConfList.size();i++){
				LfCorpConf conf=lfCorpConfList.get(i);{
					if("email.protocol".equals(conf.getParamKey())){
						protocol=conf.getParamValue();
					}
					if("email.host".equals(conf.getParamKey())){
						host=conf.getParamValue();
					}
					if("email.port".equals(conf.getParamKey())){
						port=conf.getParamValue();
					}							
					if("email.username".equals(conf.getParamKey())){
						username=conf.getParamValue();
					}							
					if("email.password".equals(conf.getParamKey())){
						passw=conf.getParamValue();
					}
				}
			}
		}else{
				String str =   "模块参数配置中相应的邮件配置不存在！";
				EmpExecutionContext.error(str);
	    		return;
			}
		MailInfo mi = new MailInfo();
		mi.setFrom(username);
		mi.setUsername(username);
		//解密
		mi.setPassword(Aes.deString(passw));
		mi.setHost(host);
		mi.setPort(port);
		mi.setProtocol(protocol);
		mi.setTo(email);
		mi.setSubject("【监控告警提醒】");
		mi.setName("");
		mi.setPriority("1");
		mi.setContent(content);
		
		SendMail sm = new SendMail(mi);
		
		try{
			sm.sendMultipleEmail();
		}catch (Exception e) {
			String str =  " 网络原因，邮箱提醒失败！";
			String msessage=e.getMessage();
			if(msessage!=null&&msessage.indexOf("authentication failed")>-1){
				str="地址无效，邮箱提醒失败！";
			}
			EmpExecutionContext.error(str);
			this.dailyFlowRecord(AdminUser,str, "fail");
		}
	 }
	}
	
	/**
	 * 监控告警提醒日志记录
	 * @param flowrecord
	 * @return
	 */
	private void dailyFlowRecord(LfSysuser sysuser,String buffer,String type){
		try{
			if("fail".equals(type)){
			 	new SuperOpLog().logFailureString(sysuser.getUserName(),"监控告警提醒","其他",buffer,null,sysuser.getCorpCode());
			}else if("success".equals(type)){
				new SuperOpLog().logSuccessString(sysuser.getUserName(),"监控告警提醒","其他",buffer,sysuser.getCorpCode());
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "监控告警提醒日志保存失败！");
		}
	}
}
