package com.montnets.emp.qyll.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.dao.LlProductDao;
import com.montnets.emp.qyll.dao.LldgDao;
import com.montnets.emp.qyll.entity.LLOrderTask;
import com.montnets.emp.qyll.entity.LlOrderDetail;
import com.montnets.emp.qyll.utils.CharacterEncodeUtil;
import com.montnets.emp.qyll.utils.EncryptOrDecrypt;
import com.montnets.emp.qyll.utils.HttpClientUtil;
import com.montnets.emp.qyll.utils.HttpUtil;
import com.montnets.emp.qyll.utils.StaticValue;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.util.StringUtils;




/** 
* @ClassName: EntFlowDataServlet 
* @Description: 企业流量相关操作接口类 
* @author xuty  
* @date 2017-10-10 上午9:11:59 
*  
*/
public class EntFlowDataServlet extends HttpServlet {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 4449882101294328638L;
	ResourceBundle RB = ResourceBundle.getBundle("SystemGlobals");
	Logger LOG = Logger.getLogger(EntFlowDataServlet.class);
	LldgDao LldgDao = new LldgDao();
	private CommonBiz commonBiz = new CommonBiz();
	private LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
	private String sysCharCode = System.getProperty("file.encoding");
	//获取企业信息表信息
	LlCompInfoVo  llCompInfoBean =  null;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			EmpExecutionContext.info("不支持GET请求方式，请使用POST方式提交！");
	}

 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletInputStream input = null; 
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter pw =  response.getWriter();
		BufferedReader reader  = null;
		StringBuffer sb = new StringBuffer();
        String line = null;
        JsonObject paramJSON = null;
        //返回给客户的信息
        String result   = null;
		try {
				llCompInfoBean =  llCompInfoBiz.getLlCompInfoBean();
				if(null ==llCompInfoBean){
            		EmpExecutionContext.info("企业信息表获取数据为空，请检查企业流量账号管理 配置是否正确!");
            		return;
            	}
				input = request.getInputStream();
		        reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
		        //流读取传入参数字符串
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	            if (StringUtils.isNotEmpty(sb.toString())) {
	            	//记录客户请求相关信息
	            	  EmpExecutionContext.info("客户IP为:"+getIp(request)+",请求的URL为："+request.getRequestURL().toString()+"?"+sb.toString());
	                //把请求的参数放入JSON
	                  paramJSON = new JsonParser().parse(sb.toString()).getAsJsonObject();
	            }
	            if(paramJSON == null){
	            	  EmpExecutionContext.error("请求参数为空，请检查必填参数是否合法！");
	            	  return;
	            }
	            
            	if(StaticValue.EMI1001.equals(paramJSON.get("BCode").getAsString())){//订购流量包
            		//---入订单任务表
            		LLOrderTask llOrderTask = getLlOrderTask(paramJSON,pw);
            		//判断该订单编号是否已存在，存在则直接返回客户，该订单已存在
            		boolean isExist = false;
            		boolean boolTask = false;
            		boolean boolDetail = false;
            		if(null != llOrderTask){
            			isExist = LldgDao.getLLOrderTask(llOrderTask.getOrderNo().trim()); 
            			if(isExist){
            				result  =errRespJson(llOrderTask.getOrderNo(),0);
            				pw.print(result);
            				return ;
            			}
            			//---入订单详情表
            			List<LlOrderDetail>  orderDetailList = getLlOrderDetailList(paramJSON);
            			
            			boolDetail = LldgDao.insertLlOrderDetail(orderDetailList);
            			
            			boolTask = LldgDao.insertLlTask(llOrderTask);
        				//调用MDOS接口
        				String  mdosUrl =StaticValue.HTTP + StaticValue.COLON +StaticValue.DOUBLE_SLASH +llCompInfoBean.getIp()+StaticValue.COLON + llCompInfoBean.getPort() + StaticValue.SLASH+"mdgg/MdosEcHttp.hts";	
        				if(boolTask && boolDetail){
        					result = HttpClientUtil.doPostClient(paramJSON,mdosUrl);
        				}else{
        					result  = "插入EMP订单任务、详情 表出错";
        				}
        				EmpExecutionContext.info("MDGG返回信息：" +result);
        				pw.print(result);
        				JsonObject json = new JsonParser().parse(result).getAsJsonObject();
        				if("0".equals(json.get("RtState").getAsString()) && //请求成功
        						"0".equals(json.get("RtErrCode").getAsString())){//正常返回
        					//---调用短信发送接口
        					smsSend(paramJSON);
        				}
            		}
            	}else{
            		//调用MDOS接口
            		String  mdosUrl =StaticValue.HTTP + StaticValue.COLON +StaticValue.DOUBLE_SLASH +llCompInfoBean.getIp()+StaticValue.COLON + llCompInfoBean.getPort() + StaticValue.SLASH+"mdgg/MdosEcHttp.hts";	
            		result = HttpClientUtil.doPostClient(paramJSON,mdosUrl);
            		EmpExecutionContext.info("MDGG返回信息：" +result);
            		pw.print(result);
            	}
             
		} catch (Exception e) {
			EmpExecutionContext.error("EMP中转MDGG接口异常："+ e.toString());
		}finally{
			//----关闭流操作
			if(pw!=null){
				pw.close();
			}
			if(reader!=null){
				reader.close();
			}
			if(input!=null){
				input.close();
			}
		}
		 
	}
	/**
	 * 获取主机IP
	 * @param req
	 * @return
	 */
	  private String getIp(HttpServletRequest req){
	    	String ip = req.getHeader("x-forwarded-for");     
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	           ip = req.getHeader("Proxy-Client-IP");     
	       }     
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	           ip = req.getHeader("WL-Proxy-Client-IP");     
	        }     
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	            ip = req.getRemoteAddr();     
	       }     
	       return ip;
	    }
	  
	  
	  /**
	   * 组装客户请求参数入LlOrderDetail实体Bean
	   * @param paraMap
	   * @return
	 * @throws Exception 
	   */
	  public List<LlOrderDetail> getLlOrderDetailList(JsonObject paramJSON) throws Exception{
		  if(null == paramJSON){
			  return null;
		  }
		  List<LlOrderDetail> llODetailList = new ArrayList<LlOrderDetail>(); 
		  String cnxt  = paramJSON.get("Cnxt").getAsString();
		  //将进行加密的Cnxt字段的进行解密
		  JsonObject jObject1  = new JsonParser().parse(EncryptOrDecrypt.decryptString(cnxt, llCompInfoBean.getPassword())).getAsJsonObject();
		  if(jObject1.has("OrderList")){
			 JsonArray  jsonArray =jObject1.getAsJsonArray("OrderList");
			 JsonObject jObject = null;
			 for(int i = 0 ;i < jsonArray.size();i++){
				jObject = jsonArray.get(i).getAsJsonObject();
				//产品IDS
				String phones  = jObject.get("Phones").getAsString();
				if(null !=phones && phones.split(",").length > 0){
					for(String phone : phones.split(",")){
						 LlOrderDetail  llOrderDetail = new LlOrderDetail();
						 llOrderDetail.setMobile(phone);//电话号码
						 llOrderDetail.setPro_id(0);//产品编号
						 llOrderDetail.setProductId(jObject.get("PID").getAsString());
						 if(jObject1.has("ECOrderId")){//订单编号
							 llOrderDetail.setOrderno(jObject1.get("ECOrderId").getAsString());
						 }
						 if(jObject1.has("OrderTime")){
							 llOrderDetail.setOrdertm(new Timestamp(System.currentTimeMillis()));
						 }
						 if(jObject1.has("Activity")){
							 //llOrderDetail.set
						 }
						 if(jObject1.has("Operator")){//操作员ID
							// llOrderDetail.seto
						 }
						 if(jObject1.has("UserID")){//发送短信的SP账号
							 //llOrderDetail.set
						 }
						 if(jObject1.has("PWD")){//发送短信的SP账号密码
							 
						 }
						 if(jObject1.has("MSG")){//短信内容
							llOrderDetail.setMsg(jObject1.get("MSG").getAsString());
							sysCharCode = CharacterEncodeUtil.getEncode(jObject1.get("MSG").getAsString());
							if("GB2312".equals(sysCharCode) || "GBK".equals(sysCharCode) || "GB18030".equals(sysCharCode)){
							}else{
								llOrderDetail.setMsg(new String(jObject1.get("MSG").getAsString().getBytes(sysCharCode),"UTF-8"));
							}
						 }
						 llOrderDetail.setOrdertm(new Timestamp(System.currentTimeMillis()));
						 llOrderDetail.setCreatetm(new Timestamp(System.currentTimeMillis()));
						 llOrderDetail.setRpttm(new Timestamp(System.currentTimeMillis()));
						 llOrderDetail.setUpdatetm(new Timestamp(System.currentTimeMillis()));
						 llOrderDetail.setLlrpt("0");//状态报告,0：发起成功
						 llOrderDetail.setStatus(1);//接口直接将读取状态设置为已读，这样读取线程将不会再读取订购
						 llODetailList.add(llOrderDetail);
					}
			}
			 
			    }
				 
			 }
			 
		 return llODetailList;
		 }
		  
	  
	  /**
	   * 组装订单任务实体
	   * @param paramJSON
	   * @return
	 * @throws Exception 
	 * @throws JsonSyntaxException 
	   */
	  public LLOrderTask getLlOrderTask(JsonObject paramJSON,PrintWriter pw) throws Exception{
		  if(null == paramJSON){
			  return null;
		  }
		  LLOrderTask  llTask  = null;
		  if(paramJSON.has("Cnxt")){//包含body
			  String cnxt  = paramJSON.get("Cnxt").getAsString();
			  //{}
			 JsonObject jObject1  = new JsonParser().parse(EncryptOrDecrypt.decryptString(cnxt, llCompInfoBean.getPassword())).getAsJsonObject();
//			 if(jObject1.get("OrderList") != null && !"".equals(jObject1.get("OrderList"))){
			 if(jObject1.get("OrderList") != null ){
				 JsonArray  jsonArray =jObject1.getAsJsonArray("OrderList");
				 StringBuffer proidsBuf = new StringBuffer();
				 StringBuffer pidsBuf = new StringBuffer();
				 int count = 0;
				 JsonObject jObject = null;
				 for(int i = 0 ;i < jsonArray.size();i++){
					jObject = jsonArray.get(i).getAsJsonObject();
					//产品IDS-LL_product中的PRODUCTID
					String pid = jObject.get("PID").getAsString();
					proidsBuf.append(pid).append(",");
					
					//ID,LL_product中的ID
					String id = new LlProductDao().queryIdByProID(pid);
					pidsBuf.append(id).append(",");
					String phones  = jObject.get("Phones").getAsString();
						count += phones.split(",").length;
				    }
				//限制最大号码数为1000个
				 	if(count > 1000){
				 		String result  =errRespJson(null,count);
				 		pw.print(result);
				 		return null;
				 	}
					llTask = new LLOrderTask();
					String taskId = commonBiz.getAvailableTaskId().toString();
					//任务批次号
					llTask.setTaskid(Integer.parseInt(taskId));
					//企业ID
					if(paramJSON.has("ECID")){
						llTask.setEcid(Integer.parseInt(paramJSON.get("ECID").getAsString()));
					}
					//操作员ID
					llTask.setUser_id(0);
					//机构ID
					llTask.setOrg_id(0);
					//ECOrderId
					 if(jObject1.has("ECOrderId")){
						 llTask.setOrderNo(jObject1.get("ECOrderId").getAsString());
					 }
					//主题
					 if(jObject1.has("Activity")){
						 String Activity =jObject1.get("Activity").getAsString();
						 llTask.setTopic(Activity);
						 sysCharCode = CharacterEncodeUtil.getEncode(Activity);
						 if("GB2312".equals(sysCharCode) || "GBK".equals(sysCharCode) || "GB18030".equals(sysCharCode)){
							}else{
								llTask.setTopic(new String(Activity.getBytes(sysCharCode),"UTF-8"));
							}
					 }
					 //MSG
					 if(jObject1.has("MSG")){
						 String MSG =jObject1.get("MSG").getAsString();
						 llTask.setMsg(MSG);
						 sysCharCode = CharacterEncodeUtil.getEncode(MSG);
						 if("GB2312".equals(sysCharCode) || "GBK".equals(sysCharCode) || "GB18030".equals(sysCharCode)){
						 }else{
							 llTask.setMsg(new String(MSG.getBytes(sysCharCode),"UTF-8"));
						 }
					 }
					//订购产品ID(产品编码)，多个以逗号分隔
					 String  proids = "";
					 if(proidsBuf.length() > 0){
						 proids = proidsBuf.substring(0, proidsBuf.length() -1);
					 }
					 llTask.setPro_ids(proids);
					 //订购产品ID(自增ID)，多个以逗号分隔
					 String  pids = "";
					 if(proidsBuf.length() > 0){
						 pids = pidsBuf.substring(0, pidsBuf.length() -1);
					 }
					 //ll_product 自增ID
					 llTask.setP_ids(pids);
					 
					//短信类型 :1-相同，2-不同，3-动态模板
					 llTask.setMsgtype("1");
					 //MSGTYPE!=3 取默认值 0
					 llTask.setTemp_id(0);
					 
					 if(jObject1.has("UserID")){//发送短信的SP 账号
							llTask.setSp_user(jObject1.get("UserID").getAsString());
						}
					 if(jObject1.has("PWD")){//发送短信的SP账号密码
						 llTask.setSp_pwd(jObject1.get("PWD").getAsString());
					 }
					 //提交号码总数
					 llTask.setSubcount(count);
					 //有效号码总数
					 llTask.setEffcount(count);
					// 成功总数
					 llTask.setSuccount(0);
					 //失败总数
					 llTask.setFaicount(0);
					// 是否定时发送 :1-是，0-否
					 llTask.setTimer_status("0");
					// 短信定时发送时间
					 llTask.setTimer_time(new Timestamp(System.currentTimeMillis()));
					// 审核状态 0 - 无需审核
					 llTask.setRe_status("0");
					 // 订购状态，-2：正在入库中，-1:未订购 0：已订购,1：待审核,2：定时中,3：撤销
					 llTask.setOrderstatus("-1");
					 
					 //是否重发 0:否，1：是
					 llTask.setIsretry("0");
					 //短息提醒状态 0：已发送
					 llTask.setSmsstatus("0");
					 //提交时间
					 llTask.setSubmittm(new Timestamp(System.currentTimeMillis()));
					 
					 //订购时间 --无需审核、无序定时，订购时间=提交时间
					 llTask.setOrdertm(llTask.getSubmittm());
					 //更新时间
					 llTask.setUpdatetm(new Timestamp(System.currentTimeMillis()));
					 //创建时间
					 llTask.setCreatetm(new Timestamp(System.currentTimeMillis()));
					 //节点编号
					 llTask.setServernum(StaticValue.getServerNumber());
								 
				}
			 }
		  	   return llTask;
	  }
	  
	  /**
	   * 短信发送
	   * @param paramJson 客户请求JSON参数
	   */
	  public void smsSend(JsonObject paramJSON){
		  String smsURL = RB.getString("montnets.webgate");
		  smsURL = smsURL.substring(0,smsURL.indexOf("/", smsURL.indexOf("//")+2))+"/sms/v2/std/batch_send";
		  JsonObject jObject1 = null;
		  if(null == paramJSON){
			 EmpExecutionContext.error("请求参数为空，请检查请求参数！");
		  }else if(paramJSON.has("Cnxt")){//包含body
			  String cnxt  = paramJSON.get("Cnxt").getAsString();
			try {
				String KEY = llCompInfoBean.getPassword();
				jObject1 = new JsonParser().parse(EncryptOrDecrypt.decryptString(cnxt, KEY)).getAsJsonObject();
				StringBuffer phoneBuf = new StringBuffer();
//				if(jObject1.get("OrderList") != null && !"".equals(jObject1.get("OrderList"))){
				if(jObject1.get("OrderList") != null ){
					JsonArray  jsonArray =jObject1.getAsJsonArray("OrderList");
					JsonObject jObject = null;
					for(int i = 0 ;i < jsonArray.size();i++){
						jObject = jsonArray.get(i).getAsJsonObject();
						String phones  = jObject.get("Phones").getAsString();
						phoneBuf.append(phones).append(",");
				 }
				    //UserID 、PWD 、 MSG 、Phone都不为空时才发送短信	
					if(!StringUtils.IsNullOrEmpty(jObject1.get("UserID").getAsString()) &&
					   !StringUtils.IsNullOrEmpty(jObject1.get("PWD").getAsString()) &&	
					   !StringUtils.IsNullOrEmpty(jObject1.get("MSG").getAsString())){
						
						 
						String userID = jObject1.get("UserID").getAsString();
						String pwd = jObject1.get("PWD").getAsString();
						if(phoneBuf.toString().split(",").length > 0){
							phoneBuf = new StringBuffer(phoneBuf.substring(0, phoneBuf.length() -1));
						}
						 String msg = jObject1.get("MSG").getAsString();
						 String ecOrderId = jObject1.get("ECOrderId").getAsString();
						 JsonObject json = new JsonObject();
						 //
						 json.addProperty("userid", userID);
						 json.addProperty("pwd", pwd);
						 json.addProperty("mobile", phoneBuf.toString());
						 json.addProperty("content", encodeContent(msg));
						 json.addProperty("svrtype", "M00000");
						 //用户自定义流水号
						 json.addProperty("custid", ecOrderId);
						 String strJson = new Gson().toJson(json);
						 //----调用短信发送接口
						 EmpExecutionContext.info("smsURL:"+smsURL+",发送数据："+strJson);
						 HttpUtil.sendPost(smsURL,strJson);
					}
					
				}
		   } catch (Exception e) {
				EmpExecutionContext.error("调用网关短信接口发送异常：" +e.toString());
			}
	  }
		  
}
	  /**
	   * 订单编号已存在返回的JSON
	   * @param orderNo
	   * @return
	   */
	  public String errRespJson(String orderNo,int phoneNumber){
		  Map<String,String> map = new HashMap<String,String>();
		  map.put("BCode", "EMI1001");
		  map.put("Ack", "2");
		  map.put("SqId", "");
		  map.put("ECID", "");
		  try {
			if(StringUtils.IsNullOrEmpty(orderNo)){
				map.put("Cnxt", EncryptOrDecrypt.encryptString(orderNo +"已存在", llCompInfoBean.getPassword()));
			}
			if(phoneNumber > 1000){
				map.put("Cnxt","订购号码数不能超过1000个。");
			}
		} catch (Exception e) {
			EmpExecutionContext.error("返回异常JSON编码出现异常" +e.toString());
		}
		  return new Gson().toJson(map);
	  }
		/**
		 * 短信内容进行encode编码
		 * @param content 待编码短信内容
		 * @return content 编码后的短信内容
		 * @throws Exception 
		 */
		public String encodeContent(String content){
			if(StringUtils.IsNullOrEmpty(content)){
				return null;
			}
			
			try {
				if(!content.contains("￥")){
					content = URLEncoder.encode(content,"GBK");
				}else{
					content = content.replace("￥", "￥");
					content = URLEncoder.encode(content,"GBK");
				}
			} catch (Exception e) {
				EmpExecutionContext.error("短信内容encode编码失败！content["+content+"]");
			}
			return content;
		}
		
		 
}
