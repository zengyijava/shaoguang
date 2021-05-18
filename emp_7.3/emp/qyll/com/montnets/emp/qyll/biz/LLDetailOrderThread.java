package com.montnets.emp.qyll.biz;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.biz.cache.LlOrderCache;
import com.montnets.emp.qyll.biz.cache.RptReceiveCache;
import com.montnets.emp.qyll.dao.CommonDao;
import com.montnets.emp.qyll.dao.LldgDao;
import com.montnets.emp.qyll.entity.EMI1001;
import com.montnets.emp.qyll.entity.LlOrderDetail;
import com.montnets.emp.qyll.entity.Message;
import com.montnets.emp.qyll.entity.OrderList;
import com.montnets.emp.qyll.entity.ReqMsgHeader;
import com.montnets.emp.qyll.entity.ResMsgHeader;
import com.montnets.emp.qyll.utils.EncryptOrDecrypt;
import com.montnets.emp.qyll.utils.HttpRequest;
import com.montnets.emp.qyll.utils.HttpUtil;
import com.montnets.emp.qyll.utils.LldgUtil;
import com.montnets.emp.qyll.utils.StringUtil;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.util.Logger;

/**
 * 流量套餐订购线程
 * @author xiebk
 *
 */
public class LLDetailOrderThread extends Thread{
	
	LldgDao llDao = new LldgDao();
	CommonDao conmonDao = new CommonDao();
	LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
	Gson gson = new Gson();
	private boolean isExit = false;
	
	int count = 500;
	
	private LlOrderCache orderCache = LlOrderCache.INSTANCE;
	
	private String passWord;//秘钥
	private String ecid;//企业编码
	private String pushAddr; //推送地址
	private String postAddr;//请求地址
	
	private String smsAddr;//短信发送地址
	//构造方法
	public LLDetailOrderThread() {
		this.setName("[流量订购线程]");
		init();
	}
	
	@Override
	public void run() {
		
			List<LlOrderDetail> orderDetais = null;
			while(!isExit){
				try {
					//从队列里读取count条记录
					orderDetais = orderCache.consumeOrders(count);
					
					//判断：没有缓存的可发送的流量任务
					if(null == orderDetais || orderDetais.size() == 0){
						Thread.sleep(1000L);
						continue;
					}
					
					//合并相同订单编号和相同套餐编码的订购任务
					merge(orderDetais);
//				Map<String,EMI1001> emi1001 = merge(orderDetais);
//				order(emi1001);
				} catch (Exception e) {
					EmpExecutionContext.errorLog(e, "流量订购读发送程被异常中断",Logger.LEVEL_ERROR );
				}
				
			}
		
	}
	
	private void init(){
		/*if(!StringUtil.isNullOrEmpty(ecid) && !StringUtil.isNullOrEmpty(pushAddr) 
				&& !StringUtil.isNullOrEmpty(passWord)){
			return;
		}*/
		
		//获取Bean数据
		try {
			LlCompInfoVo  llCompInfoBean= llCompInfoBiz.getLlCompInfoBean();
			if( llCompInfoBean == null ){
				//查询数据库失败或者无数据，直接返回
				return;
			}
			
			this.ecid = llCompInfoBean.getCorpCode();
			this.pushAddr = llCompInfoBean.getPushAddr();
			this.passWord = llCompInfoBean.getPassword();
			this.postAddr = "http://"+llCompInfoBean.getIp()+":"+llCompInfoBean.getPort()+"/mdgg/MdosEcHttp.hts";
			
			ResourceBundle RB = ResourceBundle.getBundle("SystemGlobals");
			String webgate = RB.getString("montnets.webgate");
			this.smsAddr = webgate.substring(0,webgate.indexOf("/", webgate.indexOf("//")+2))+"/sms/v2/std/batch_send";
		} catch (Exception e) {
			EmpExecutionContext.errorLog(e,"初始化流量订购线程：获取企业基本信息失败！",Logger.LEVEL_ERROR);
		}

	}
	
	public void setIsExit(boolean isExit){
		this.isExit = isExit;
	}
	
	public Map<String,EMI1001> merge(List<LlOrderDetail> orderDetais){
		//Map<ECOrderId,EMI1001>
		Map<String,EMI1001> emiMap = new HashMap<String, EMI1001>();
		StringBuffer phones = new StringBuffer("");
		Map<String,Message> msgMap = new HashMap<String, Message>();
		
		try {
			for(LlOrderDetail d : orderDetais){
				//获取订单编号
				String ECOrderId = d.getOrderno();
				EMI1001 emi1001 = emiMap.get(ECOrderId);
				//没有匹配到相同订单编号
				if(null == emi1001){
					//新建一个流量订单请求
					emi1001 = new EMI1001();
					emiMap.put(ECOrderId, emi1001);
					emi1001.setECOrderId(ECOrderId);
					
					List<OrderList> orderList = new ArrayList<OrderList>();
					OrderList order = new OrderList();
					//设置产品编号
					order.setPID(d.getProductId());
					//设置订购手机号列表
					order.setPhones(d.getMobile());
					orderList.add(order);
					//订购手机号列表
					emi1001.setOrderList(orderList);
				}else{
					//Map中已有相同的订单编号 进行合并
					List<OrderList> orderList = emi1001.getOrderList();
					boolean flag = false;
					for(OrderList o : orderList){
						//判断是否存在相应的套餐编号
						if(o.getPID() != null && o.getPID().equals(d.getProductId())){
							//存在，进行号码的累加
							phones.delete(0, phones.length())
							.append(o.getPhones())
							.append(",")
							.append(d.getMobile());
							//设置订购手机号列表
							o.setPhones(phones.toString());
							flag = true;
							break;
						}
					}
					
					//如果未匹配到相同的套餐编号
					if(!flag){
						//新建订购手机号列表
						OrderList order = new OrderList();
						order.setPID(d.getProductId());
						order.setPhones(d.getMobile());
						
						orderList.add(order);
					}
				}
				
				//判断是否需要发送短信
				if(!StringUtil.isNullOrBlank(d.getMsg()) && !StringUtil.isNullOrBlank(d.getSp_user())
						&& !StringUtil.isNullOrBlank(d.getSp_pwd())){
					
					//需要短信发送
					Message msg = msgMap.get(ECOrderId);
					if(null == msg){
						msg = new Message();
						msg.setUserid(d.getSp_user());
						msg.setPwd(d.getSp_pwd());
						msg.setMobile(d.getMobile());
						msg.setCustid(ECOrderId);
						msg.setSvrtype("M00000");
						msg.setContent(encodeContent(d.getMsg()));
						msgMap.put(ECOrderId, msg);
					}else{
						//拼接电话号码
						phones.delete(0, phones.length())
						.append(msg.getMobile())
						.append(",")
						.append(d.getMobile());
						msg.setMobile(phones.toString());
					}
				}
			}//数据处理完成
		} catch (Exception e) {
			EmpExecutionContext.error(e,"流量订购：数据处理错误");
		}
		
		boolean isSuccess = false;
		//进行流量订购
		init();//每次进行流量订购前，同步一次企业配置信息（企业编码，推送地址和秘钥）
		try {
			isSuccess = orderFlow(emiMap);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"调用接口进行流量订购时出错！");
		}
		
		//进行短信订购
		try {
			if(isSuccess){
				sendMsg(msgMap);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"调用接口进行短信下发时出错！");
		}
		
		return emiMap;
	}
	
	
	/**
	 * 流量订购
	 * @param emiMap
	 * @throws Exception
	 */
	public boolean orderFlow(Map<String,EMI1001> emiMap) throws Exception{
		ReqMsgHeader req = new ReqMsgHeader();
		
		req.setBCode("EMI1001");
		req.setAck("1");
		req.setSqId(LldgUtil.createSqId());
		req.setECID(this.ecid);
		
		try {
			//遍历
			for(Map.Entry<String,EMI1001> entry : emiMap.entrySet()){
				StringBuffer phones = new StringBuffer();
				//获取一个订购流量包报文实体
				EMI1001 emi1001 = entry.getValue();
				List<OrderList> orderLists = emi1001.getOrderList();
				for(OrderList ord : orderLists){
					phones.append(ord.getPhones()).append(",");
				}
				if(phones != null && phones.length() != 0){
					phones.deleteCharAt(phones.length()-1);
				}
				
				String orderNo = emi1001.getECOrderId();
				String year = StringUtil.subOrderNoYear(orderNo);
				
				int batchId = llDao.getMaxBatchid(orderNo,year);
				while(!conmonDao.updateFlowDetailBatchId(phones.toString(), batchId, orderNo,year)){
					EmpExecutionContext.error("更新订购详情表批次号失败：year"+year+",Phones:"+phones+",orderNo:"+orderNo);
					//重新获取批次号
					batchId = llDao.getMaxBatchid(orderNo,year);
				}
				
				emi1001.setECOrderId(orderNo + "_" + batchId);
				//获取JSon字符串
				String cnxt = gson.toJson(emi1001);
				//加密
				cnxt = EncryptOrDecrypt.encryptString(cnxt, this.passWord);
				req.setCnxt(cnxt);
				String reqStr = gson.toJson(req);
				String returnStr = HttpRequest.sendPost(this.postAddr, reqStr);
				ResMsgHeader res = gson.fromJson(returnStr, ResMsgHeader.class);
				res.setRtMsg(EncryptOrDecrypt.base64Decoder(res.getRtMsg()));
				EmpExecutionContext.info("订单编号"+orderNo + "_" + batchId+"提交Mdoss返回信息："+res.getRtMsg());
				//判断是否提交成功
				if("1".equals(res.getRtState())){//RtState	返回状态	0-成功 1-失败
					
					String[] phonArr = phones.toString().split(","); 
					
					//如果提交失败，更新详情表状态为提交失败
					for(String phon : phonArr){
						LlOrderDetail det = new LlOrderDetail();
						det.setOrderno(orderNo);
						det.setLlrpt("3");
						det.setErrCode(res.getRtErrCode());
						det.setMobile(phon);
						RptReceiveCache.INSTANCE.produce(det);//放入状态报告缓冲队列
					}
					
				}
				
				//更新订单任务表的订购状态为已订购
				conmonDao.updateReadTaskFlowStat(orderNo,"0");
				
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"流量订购失败");
			return false;
		}
		
		return true;
	}
	
	public boolean orderOne(EMI1001 emi1001){
		ReqMsgHeader req = new ReqMsgHeader();
		
		req.setBCode("EMI1001");
		req.setAck("1");
		req.setSqId(LldgUtil.createSqId());
		req.setECID(this.ecid);
		
		StringBuffer phones = new StringBuffer();
		List<OrderList> orderLists = emi1001.getOrderList();
		for(OrderList ord : orderLists){
			phones.append(ord.getPhones()).append(",");
		}
		if(phones != null && phones.length() != 0){
			phones.deleteCharAt(phones.length()-1);
		}
		
		String orderNo = emi1001.getECOrderId();
		String year = StringUtil.subOrderNoYear(orderNo);
		
		int batchId = llDao.getMaxBatchid(orderNo,year);
		while(!conmonDao.updateFlowDetailBatchId(phones.toString(), batchId, orderNo,year)){
			EmpExecutionContext.error("更新订购详情表批次号失败：year"+year+",Phones:"+phones+",orderNo:"+orderNo);
			//重新获取批次号
			batchId = llDao.getMaxBatchid(orderNo,year);
		}
		
		emi1001.setECOrderId(orderNo + "_" + batchId);
		//转换为JSon字符串
		String cnxt = gson.toJson(emi1001);
		//加密
		try {
			cnxt = EncryptOrDecrypt.encryptString(cnxt, this.passWord);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"流量订购包加密失败："+cnxt);
			return false;
		}
		
		req.setCnxt(cnxt);
		String reqStr = gson.toJson(req);
		String returnStr = HttpRequest.sendPost(this.postAddr, reqStr);
		ResMsgHeader res = gson.fromJson(returnStr, ResMsgHeader.class);
		//记录发送日志
		try {
			res.setRtMsg(EncryptOrDecrypt.base64Decoder(res.getRtMsg()));
		} catch (IOException e) {
			EmpExecutionContext.error("发送日志报错"+e);
		}
		EmpExecutionContext.info("订单编号"+orderNo + "_" + batchId+"提交Mdoss返回信息："+res.getRtMsg());

		//判断是否提交成功
		if("1".equals(res.getRtState())){//RtState	返回状态	0-成功 1-失败
			//如果提交失败，更新详情表状态为提交失败
			
			
		}
		
		//更新订单任务表的订购状态为已订购
		conmonDao.updateReadTaskFlowStat(orderNo,"0");
		
		
		
		return true;
	}
	
	
	
	/**
	 * 发送短信
	 * @param msgMap
	 */
	public void sendMsg(Map<String,Message> msgMap){
		//遍历
		for(Map.Entry<String,Message> entry : msgMap.entrySet()){
			//获取JSon字符串
			String msgJson = gson.toJson(entry.getValue());
			
			String msg =  HttpUtil.sendPost(this.smsAddr, msgJson);
			//更新短信发送状态
			EmpExecutionContext.info("订单编号" + entry.getKey() +"提交短信网关返回信息："+msg);
			conmonDao.updateReadTaskSMSStat(entry.getKey(),"0");
			
		}
		
	}
	
	
	/**
	 * 短信内容进行encode编码
	 * @param content 待编码短信内容
	 * @return content 编码后的短信内容
	 * @throws Exception 
	 */
	public String encodeContent(String content) throws Exception{
		if(StringUtil.isNullOrEmpty(content)){
			return null;
		}
		
		try {
			if(!content.contains("¥")){
				content = URLEncoder.encode(content,"GBK");
			}else{
				content = content.replace("¥", "￥");
				content = URLEncoder.encode(content,"GBK");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"流量订购短信发送：在进行短信内容encode（gbk）时出错");
		}
		
		return content;
	}
	
	
	public static void main(String[] args) throws Exception {
		Gson gson = new Gson();
		
		ReqMsgHeader req = new ReqMsgHeader();
		
		req.setBCode("EMI1001");
		req.setAck("1");
		req.setSqId(LldgUtil.createSqId());
		req.setECID("100229");
		
		String reqStr = gson.toJson(req);
		
		String returnStr = HttpRequest.sendPost("http://192.169.1.8:8088/p_qyll/ll_RptReceive.htm", reqStr);
	}
	
}


