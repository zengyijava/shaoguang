package com.montnets.emp.qyll.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.biz.cache.RptReceiveCache;
import com.montnets.emp.qyll.entity.LlOrderDetail;
import com.montnets.emp.qyll.utils.EncryptOrDecrypt;
import com.montnets.emp.qyll.utils.HttpClientUtil;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.StringUtils;

public class ll_RptReceiveSvt extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 987813933869588056L;
	ResourceBundle RB = ResourceBundle.getBundle("SystemGlobals");
	private LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
	//获取企业信息表信息
	LlCompInfoVo  llCompInfoBean =  null;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletInputStream input = null; 
		BufferedReader reader  = null;
		StringBuffer sb = new StringBuffer();
	    String line = null;
	    JsonObject paramJSON = null;
	    PrintWriter pw  = null;
	    String cutomerUrl ="";
	    try {
		    input = request.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(input));
	    	llCompInfoBean =  llCompInfoBiz.getLlCompInfoBean();
	    	if(null == llCompInfoBean){
	    		EmpExecutionContext.info("企业信息表获取为空，请检查企业流量账号管理是否正常！");
	    		return ;
	    	}
	        //流读取传入参数字符串
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	        if (StringUtils.isNotEmpty(sb.toString())) {
	            //把请求的参数放入JSON
	        	paramJSON = new JsonParser().parse(sb.toString()).getAsJsonObject();
	        	EmpExecutionContext.info("MDOS返回状态报告JSON:"+paramJSON);
	        }
	        if(null == paramJSON){
	        	EmpExecutionContext.error("MDOS返回状态报告为空");
	        	return;
	        }
	        
	        //组装入队列的实体
	        LlOrderDetail lDetail = getLlRpt(paramJSON);
	        //将返回的状态报告入队列
			RptReceiveCache.INSTANCE.produce(lDetail);
			//给MDOS发送确认报文 --- 等客户返回了主动推送的状态报告后，才向MDOS推送
			cutomerUrl = llCompInfoBean.getPushAddr();
//			String cutomerUrl = "http://192.169.1.96:8080/p_qyll/ll_ClientRptReceivevt";
			if(null != lDetail){//--判断订单编号前缀：EMP开头（通过页面订购的）直接入队列，否则（通过接口订购）还需推送给客户
				if(!lDetail.getOrderno().toUpperCase().contains("EMP")){
					//给客户推送状态报告回应报文
					String jsonstr = pushCustomer1003(paramJSON,cutomerUrl);
					EmpExecutionContext.info("客户端状态报告返回信息：" + jsonstr);
					paramJSON = new JsonParser().parse(jsonstr).getAsJsonObject();
					EmpExecutionContext.info("客户端状态报告返回信息 Cnxt明文：" + EncryptOrDecrypt.decryptString(paramJSON.get("Cnxt").getAsString(),llCompInfoBean.getPassword()));
				
				} 
				//给MDOS推送已收到状态报告返回
				pw = response.getWriter();
				pw.write(paramJSON.toString());
			}
			
			
		} catch (Exception e) {
			EmpExecutionContext.error("客户推送状态报告JSON返回出现异常：" + e.toString()+",推送地址："+cutomerUrl);
		} finally{ 
			if(null != pw ){
				pw.flush();
				pw.close();
			}
			try{
				IOUtils.closeIOs(input, null, reader, null, this.getClass());
			}catch(IOException e){
				EmpExecutionContext.error(e, "doPost()");
			}
		}
	}
	/**
	 * 获取MDOS返回的状态报告
	 * @param paramJSON
	 */
	public LlOrderDetail  getLlRpt(JsonObject paramJSON){
		LlOrderDetail lDetail = new LlOrderDetail();
		try {
			if(null != paramJSON){
				 String cnxt  = paramJSON.get("Cnxt").getAsString();
				 JsonObject jObject  = new JsonParser().parse(EncryptOrDecrypt.decryptString(cnxt, llCompInfoBean.getPassword())).getAsJsonObject();
				 //电话号码
				 lDetail.setMobile(jObject.get("Phone").getAsString());
				 //订单编号
				 lDetail.setOrderno(jObject.get("ecOrderId").getAsString());
				 //订购状态
				 lDetail.setLlrpt(jObject.get("OrderState").getAsString());
				 lDetail.setErrCode(jObject.get("ErrCode").getAsString());
				 //产品ID
				 lDetail.setPro_id(Integer.parseInt(jObject.get("ProductId").getAsString())); 
				 //流量报告返回时间
				 lDetail.setRpttm(new Timestamp(System.currentTimeMillis()));
			}
		} catch (Exception e) {
			EmpExecutionContext.error("流量状态报告返回组装参数异常：" + e.toString());
		}
		return lDetail;
	}
	
	/**
	 * 给MDOS推送确定状态报告返回报文
	 * @param paramJSON
	 * @param mdosUrl
	 */
	public void pushEMI1003(JsonObject paramJSON,String mdosUrl){
			HttpClientUtil.doPostClient(paramJSON, mdosUrl);
	}
	/**
	 * 给客户推送MDOS主动推送的状态报告
	 * @param paramJSON
	 * @param customerUrl
	 * @return
	 */
	public String pushCustomer1003(JsonObject paramJSON,String customerUrl){
			return HttpClientUtil.doPostClient(paramJSON, customerUrl);
	}
}
