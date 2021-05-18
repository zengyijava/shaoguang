package com.montnets.emp.qyll.servlet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.biz.LlProductBiz;
import com.montnets.emp.qyll.entity.EMI1004;
import com.montnets.emp.qyll.entity.EMI1005;
import com.montnets.emp.qyll.entity.LlProduct;
import com.montnets.emp.qyll.entity.Products;
import com.montnets.emp.qyll.utils.EncryptOrDecrypt;
import com.montnets.emp.qyll.utils.HttpClientUtil;
import com.montnets.emp.qyll.utils.LldgUtil;
import com.montnets.emp.qyll.vo.LlCompInfoVo;

public class FlowSynchronizeTimerTask extends TimerTask{

	@Override
	public void run() {
		try {
			FlowSynchronizeData();
       } catch (Exception e) {
    	   EmpExecutionContext.error("-------------解析信息发生异常--------------");
       }
	}

	private void FlowSynchronizeData() {
		boolean flag = false;
		try {
			LlProduct llProduct = new LlProduct();
			LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
			//获取Bean数据
			LlCompInfoVo  llCompInfoBean= llCompInfoBiz.getLlCompInfoBean();
			
			if(llCompInfoBean==null){
				EmpExecutionContext.error("流量套餐同步时，查询企业编号异常");
				return;
			}else{
				llProduct.setEcid(Integer.parseInt(llCompInfoBean.getCorpCode()==null?"0":llCompInfoBean.getCorpCode()));	
				Gson gson = new Gson();
				String url = "http://"+llCompInfoBean.getIp()+":"+llCompInfoBean.getPort()+"/mdgg/MdosEcHttp.hts";
				JsonObject json = new JsonObject();
				json.addProperty("CorpCode", llCompInfoBean.getCorpCode());
				String result = EncryptOrDecrypt.encryptString(json.toString(), llCompInfoBean.getPassword());
				JsonObject reqJson = new JsonObject();
				reqJson.addProperty("BCode", "EMI1005");
				reqJson.addProperty("Ack", "1");
				reqJson.addProperty("SqId", LldgUtil.createSqId());
				reqJson.addProperty("ECID", llCompInfoBean.getCorpCode());
				reqJson.addProperty("Cnxt", result);
				String msg = HttpClientUtil.doPostClient(reqJson,url);
				EncryptOrDecrypt encryptOrDecrypt = new EncryptOrDecrypt();
				//String decryptMsg = encryptOrDecrypt.decryptString(msg, llCompInfoBean.getPassword());
				EMI1004 resultParms = gson.fromJson(msg, EMI1004.class);
				String Cnxt = resultParms.getCnxt();
				String products = encryptOrDecrypt.decryptString(Cnxt, llCompInfoBean.getPassword());
				EMI1005 resultList = gson.fromJson(products, EMI1005.class);
				//获取接口返回的list
				List<Products> list = resultList.getProducts();
				Map<String,Products> mapI = new HashMap<String, Products>();
				if(list==null){
					EmpExecutionContext.info("流量套餐定时同步时，调用EMI1005接口未获取到数据");
					return;
				}else{
					//遍历list，当接口传值的折扣为空时 ，删除该条bean
					Iterator<Products> it = list.iterator();
					while(it.hasNext()){
						Products product = it.next();
					    if((product.getDiscountPrice()).isEmpty()){
					        it.remove();
					    }
					}
					for(int i = 0; i < list.size(); i++){
						String productId = list.get(i).getProductId();
						mapI.put(productId,list.get(i));
					}
				}
				//获取数据库的Map
				LlProductBiz llProductBiz = new LlProductBiz();
				Map<String,Products> mapS = llProductBiz.getLlProductMap(llProduct);
				//如果数据库没有数据则直接新增
				if(mapS.isEmpty()){
					for (String keyi : mapI.keySet()) {
						if(mapI.get(keyi).getProductType()==0){
							flag = llProductBiz.addProducts(mapI.get(keyi),llCompInfoBean.getCorpCode(),"0");
						}
					}
				}else{
					/*先判断是否存在相同的产品编号，不存在的就新增。
					 * 存在相同的产品编号就对比价格，价格一致就不变，价格不一致就新增一条，将原有的状态修改为停用*/
					for (String keyi : mapI.keySet()) {
						if(mapS.containsKey(keyi)){  
							if((mapS.get(keyi).getPrice()) != mapI.get(keyi).getPrice() || (mapS.get(keyi).getDiscountPrice()) !=mapI.get(keyi).getDiscountPrice()){
								//先更新状态，再新增
								boolean upDateFlag =llProductBiz.upDateProducts(keyi,"0",llProduct);
								if(upDateFlag){
									if(mapI.get(keyi).getProductType()==0){
										flag = llProductBiz.addProducts(mapI.get(keyi),llCompInfoBean.getCorpCode(),"0");
									}
								}
							}
						}else{
							if(mapI.get(keyi).getProductType()==0){
								flag = llProductBiz.addProducts(mapI.get(keyi),llCompInfoBean.getCorpCode(),"0");
							}
						}
					}
					//还需将查询数据库有而同步报文中没有的数据的状态进行更改
					String pros ="";
					for(String keys : mapS.keySet()){
						if(!mapI.containsKey(keys)){ 
							pros += keys+",";
						} 
					}
					if(!pros.equals("")){
						pros = pros.substring(0, pros.length()-1);
						flag = llProductBiz.updateProduct(pros,llProduct,"0");
					}
				}
				if(flag){
					EmpExecutionContext.info("流量套餐同步，定时同步成功");
				}else{
					EmpExecutionContext.info("流量套餐同步，定时同步失败");
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error("-----流量套餐同步，定时同步失败-----");
		}
	}
}
