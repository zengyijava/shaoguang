package com.montnets.emp.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Properties;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfOpratelog;

 
/**
 * 
 * @author Administrator
 *
 */
public class SuperOpLog extends SuperBiz {


	private String outLevel= StaticValue.NOSEARCH ;

    private CommonBiz commonBiz = new CommonBiz();
    
	public SuperOpLog(){
			getProInfo();
	}
	/**
	 * 获取配置文件信息
	 * @throws Exception
	 */
	private void getProInfo() {
    	Properties p = new Properties();   
    	String fileName="/SystemGlobals.properties";
    	String errorMsg = "";
    	String leveltoLowerCase="";
        try {
        	InputStream in = getClass().getResourceAsStream(fileName);
			p.load(in);
			in.close();
			String level=p.getProperty("opLog.out.logLevel");
			if(null != level){
				leveltoLowerCase = level.toLowerCase();
			}
			if(level==null || "".equals(level) && (level.toLowerCase()== "debug" || level.toLowerCase()== "info" || level.toLowerCase()== "warn"  || level.toLowerCase()!= "nolog")){
				errorMsg="读取配置文件出错，属性opLog.out.logLevel不存在该值："+leveltoLowerCase+"，将使用默认值NOSEARCH。";
			}
			outLevel=leveltoLowerCase;
		} catch (IOException e) {
			
			EmpExecutionContext.error(e, errorMsg);
		}
        
	}
	
	/**
	 * 获取异常信息
	 * @param e
	 * @return
	 */
	private String getExceptionString(Exception e) {
		if (e != null) {
			return e.toString();
		} else {
			return null;
		}

	}

	/**
	 * 操作日志
	 * @param opUsername
	 * @param opModule
	 * @param opType
	 * @param opContent
	 */
	public void logSuccessString(String opUsername,String opModule, String opType,
			String opContent,String corpCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(corpCode);
		sb.append("&");
		sb.append(opUsername);
		sb.append("&");
		sb.append(opModule);
		sb.append("&");
		sb.append(opType);
		sb.append("&");
		sb.append(opContent);
		sb.append("&");
		sb.append("1");
		this.insLogLevel(sb.toString(),opType);
	}

	/**
	 * 操作失败日志
	 * @param opUsername
	 * @param opModule
	 * @param opType
	 * @param opContent
	 * @param e
	 */
	public void logFailureString(String opUsername,String opModule, String opType,
			String opContent, Exception e,String corpCode){
		StringBuffer sb = new StringBuffer();
		sb.append(corpCode);
		sb.append("&");
		sb.append(opUsername);
		sb.append("&");
		sb.append(opModule);
		sb.append("&");
		sb.append(opType);
		sb.append("&");
		sb.append(opContent);
		sb.append("&");
		sb.append("0");
		this.insLogLevel(sb.toString(),opType);
	}

	/**
	 * 
	 * @param sql
	 * @param opType
	 */
	private void insLogLevel (String sql, String opType){
		
		if(outLevel.equals(StaticValue.ALLOUT)){
			this.insertLog(sql);
		}else if(outLevel.equals(StaticValue.CURDOUT)){
			if(opType==StaticValue.GET || opType==StaticValue.ADD || opType==StaticValue.UPDATE || opType==StaticValue.DELETE){
				this.insertLog(sql);
			}
			
		}else if(outLevel.equals(StaticValue.NOSEARCH)){
			if(opType==StaticValue.ADD || opType==StaticValue.UPDATE || opType==StaticValue.DELETE){
				this.insertLog(sql);
			}
			
		}
	}

	
 
	/**
	 * 保存日志
	 * @param sql
	 */
	private void insertLog(String sql){
		
		try{
			String corpCode="";
			String opUser = "";
			String opModule = "";
			String opAction = "";
			String opContent = "";
			String  opResult = "";
			int len;
			
			String[] columns = sql.split("&");
			len = columns.length;
			for (int i = 0; i < len; i++) {
				switch (i) {
				case 0:
					corpCode = columns[i];
					break;
				case 1:
					opUser = columns[i];
					break;
				case 2:
					opModule = columns[i];
					break;
				case 3:
					opAction = columns[i];
					break;
				case 4:
					opContent = columns[i];
					break;
				case 5:
					opResult=columns[i];
					break;
	
				}
			}
	 
		    LfOpratelog lfOperatelog = new LfOpratelog();
		    lfOperatelog.setCorpCode(corpCode);
		    lfOperatelog.setOpUser(opUser);
		    lfOperatelog.setOpTime( new Timestamp(System.currentTimeMillis()));
		    lfOperatelog.setOpModule(opModule);
		    lfOperatelog.setOpAction(opAction);
		    lfOperatelog.setOpContent(opContent);
		    lfOperatelog.setOpResult(new Integer(opResult));
//		    empDao.save(lfOperatelog);
            commonBiz.asyncSaveEntity(lfOperatelog);
 
		}catch(Exception e){
			EmpExecutionContext.error("操作日志入库失败！");
		}
	    
		 
		 
	}

}

