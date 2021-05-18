package com.montnets.emp.shorturl.surlmanage.biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.shorturl.comm.constant.UrlGlobals;
import com.montnets.emp.shorturl.surlmanage.dao.NoticeStopDao;
import com.montnets.emp.shorturl.surlmanage.entity.Message;
import com.montnets.emp.shorturl.surlmanage.entity.StopNeturl;
import com.montnets.emp.shorturl.surlmanage.util.HttpClientHandler;
import com.montnets.emp.util.MD5;

public class NoticeStopBiz {
	
	public static  final String			ERROR_310099			= "-310099";
	public static  final String 		SUCCESS_RESULT			= "0";
	private SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
	
	NoticeStopDao stopDao = new NoticeStopDao();
	
	Gson gson = new Gson();
	
	SmsBiz smsBiz = new SmsBiz();
	
	/**
	 * 调用接口禁止url地址，并更新数据库
	 * @param urlliList
	 * @param command -2运营商    -1用户
	 * @return
	 */
	public Integer setUrlStop(List<StopNeturl> urlliList,String command){
		Integer count = 0;
		try {
			if (urlliList.size()<1) {
				//禁用0条
				return 0;
			}
			//需要禁用的url对象
			StopNeturl stopNeturl = null;
			//接口信息对象
			Message message = null;
			//http报文体
			String body = "";
			//报文头
			Map<String, String> headers = getHeader();
			//短地址中心url地址
			String url = UrlGlobals.getSHORT_CENTER_URL();
			if (url==null||"".equals(url)) {
				EmpExecutionContext.info("短地址中心地址未配置。");
				return 0;
			}
			
			//http://ip:port/sms/v2/std/set_status
			url += "set_la_status";
			
			//获取httpclient单例执行对象
			HttpClientHandler handler = HttpClientHandler.getInstance();
			
			//接口返回结果
			String result = "";
			
			boolean stopFlag = false;
			String pass = "";
			for (int i = 0; i < urlliList.size(); i++) {

				stopNeturl = urlliList.get(i);
				
				message = stopDao.getSpuser(stopNeturl.getCorpcode());
				
				if (message==null) {
					//当前url对应企业无sp账号，下一次执行再禁用
					EmpExecutionContext.info("URL扫描禁用任务线程,扫描到当前URL:"+stopNeturl.getUrl()+"对应企业无sp账号，将跳过调用禁用接口，下次任务启动禁用。");
					continue;
				}
				pass = message.getPwd();
				String timestramp = sdf.format(new Date());
				pass = MD5.getMD5Str(message.getUserid().toUpperCase()+"00000000"+pass+timestramp);
				message.setPwd(pass);
				message.setTimestamp(timestramp);
				//设置为禁用
				message.setStatus("2");
				message.setLongaddr(stopNeturl.getUrl());
				//拼接报文
				body = gson.toJson(message);
				
				result = handler.execute(url, body, headers, null, null);
//				result = "0";
				
				if (ERROR_310099.equals(result)) {
					//请求失败
					EmpExecutionContext.info("调用短地址中心禁用地址接口失败，返回结果为"+result);
					continue;
				}
				JSONObject reObject =null;
				try {
					 reObject = JSON.parseObject(result);
				} catch (Exception e) {
					EmpExecutionContext.error(e, "解析短链禁用接口响应异常,响应值："+result);
					continue;
				}
				String re = reObject.getString("result");
				if (SUCCESS_RESULT.equals(re)){
					//请求成功
					//更新数据库
					EmpExecutionContext.info("调用禁用短地址接口成功，请求值："+body);
					stopFlag = updateSopUrl(stopNeturl.getId(),command);
					if (stopFlag) {
						//执行成功  +1
						count++;
					}
				}else {
					EmpExecutionContext.error("调用禁用短地址接口失败，返回值："+result+",请求值："+body);
				}
				
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "调用短地址中心禁用地址Biz异常");
		}
		return count;
		
	}
	
	/**
	 * 更新已经禁止的url状态
	 * @param message
	 * @param command -2运营商    -1用户
	 */
	private boolean updateSopUrl(Long id,String command) {
		return  stopDao.updateStopUrl(id,command);
	}


	//获取请求头
	private Map<String, String> getHeader(){
		//设置请求报文头
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Connection", "keep-alive");
		headers.put("Content-Type", "text/json");
		
		return headers;
	}
	
}
