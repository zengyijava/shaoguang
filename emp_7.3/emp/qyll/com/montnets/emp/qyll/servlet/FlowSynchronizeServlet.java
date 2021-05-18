package com.montnets.emp.qyll.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.biz.LlProductBiz;
import com.montnets.emp.qyll.entity.EMI1004;
import com.montnets.emp.qyll.entity.EMI1005;
import com.montnets.emp.qyll.entity.LlProduct;
import com.montnets.emp.qyll.entity.Products;
import com.montnets.emp.qyll.utils.EncryptOrDecrypt;
import com.montnets.emp.qyll.utils.HttpUtil;
import com.montnets.emp.qyll.utils.LldgUtil;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.util.PageInfo;

public class FlowSynchronizeServlet extends BaseServlet{

	private static final long serialVersionUID = 1L;
	
	private static final String PATH = "qyll/lldg";
	
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//起始ms数
		PageInfo pageInfo = new PageInfo();
		// 是否第一次访问
		boolean isFirstEnter = pageSet(pageInfo, request);

		LlProduct llProduct = getParm(request,pageInfo,isFirstEnter);
		request.setAttribute("llProduct", llProduct);
		try {
			//第一次进入
			if(("0").equals(llProduct.getIsFirstEnter()))
			{
				//第一次进入，清空session查询条件
				clearSearchCondition(request);
				return;
			}else{
				LlProductBiz llProductBiz = new LlProductBiz();
				List<LlProduct> llProductList =  llProductBiz.getLlProductList(llProduct,pageInfo);
				//分页对象
				request.setAttribute("pageInfo", pageInfo);
				//套餐订购结果
				request.setAttribute("resultList", llProductList);
			}
		}catch(Exception e){
			//分页对象
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "套餐流量同步查询异常。");
		}finally
		{
			request.getRequestDispatcher(PATH +"/ll_flowSynchronize.jsp").forward(request, response);
		}
	}
	public void handSyn(HttpServletRequest request, HttpServletResponse response) 
	{
		boolean flag = true;
		PrintWriter pw = null;
		try {
			//起始ms数
			PageInfo pageInfo = new PageInfo();
			// 是否第一次访问
			boolean isFirstEnter = pageSet(pageInfo, request);
			LlProduct llProduct = getParm(request,pageInfo,isFirstEnter);
			
			pw = response.getWriter();
			LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
			//获取Bean数据
			LlCompInfoVo  llCompInfoBean= llCompInfoBiz.getLlCompInfoBean();
			// 登录操作员id
			String lguserid = "";
			LfSysuser user = getLoginUser(request);
			lguserid = user.getUserId().toString();
			if(llCompInfoBean==null){
				pw.write("falses");
				pw.flush();
				EmpExecutionContext.error("查询企业编号异常");
				return;
			}else{
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
				String msg = HttpUtil.sendPost(url, reqJson.toString());
				//String decryptMsg = encryptOrDecrypt.decryptString(msg, llCompInfoBean.getPassword());
				EMI1004 resultParms = gson.fromJson(msg, EMI1004.class);
				String Cnxt = resultParms.getCnxt();
				String products = EncryptOrDecrypt.decryptString(Cnxt, llCompInfoBean.getPassword());
				EmpExecutionContext.info("====================解压后："+products);
				EMI1005 resultList = gson.fromJson(products, EMI1005.class);
				//获取接口返回的list
				List<Products> list = resultList.getProducts();
				Map<String,Products> mapI = new HashMap<String, Products>();
				if(list==null){
					pw.print("true");
					EmpExecutionContext.info("调用EMI1005接口未获取到数据");
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
				//1.如果数据库没有数据则直接新增
				if(mapS.isEmpty()){
					for (String keyi : mapI.keySet()) {
						if(mapI.get(keyi).getProductType()==0){
							flag = llProductBiz.addProducts(mapI.get(keyi),llCompInfoBean.getCorpCode(),lguserid);
						}
					}
				}else{
					/*2.先判断是否存在相同的产品编号，不存在的就新增。
					 * 存在相同的产品编号就对比价格，价格和折扣一致就不变，价格或折扣不一致就新增一条，将原有的状态修改为停用*/
					for (String keyi : mapI.keySet()) {
						if(mapS.containsKey(keyi)){  
							if((mapS.get(keyi).getPrice()) != mapI.get(keyi).getPrice() || (mapS.get(keyi).getDiscountPrice()) !=mapI.get(keyi).getDiscountPrice()){
								//先更新状态，再新增
								boolean upDateFlag =llProductBiz.upDateProducts(keyi,lguserid,llProduct);
								if(upDateFlag){
									if(mapI.get(keyi).getProductType()==0){
										flag = llProductBiz.addProducts(mapI.get(keyi),llCompInfoBean.getCorpCode(),lguserid);
									}
								}
							}
						}else{
							if(mapI.get(keyi).getProductType()==0){
								flag = llProductBiz.addProducts(mapI.get(keyi),llCompInfoBean.getCorpCode(),lguserid);
							}
						}
					}
					
					//3.将查询数据库有而同步报文中没有的数据的状态进行更改
					String pros ="";
					for(String keys : mapS.keySet()){
						if(!mapI.containsKey(keys)){ 
							pros += keys+",";
						} 
					}
					if(!pros.equals("")){
						pros = pros.substring(0, pros.length()-1);
						flag = llProductBiz.updateProduct(pros,llProduct,lguserid);
					}
					
				}
				
				if(flag){
					pw.print("true");
					EmpExecutionContext.info("流量套餐同步，手动同步成功");
				}else{
					pw.print("false");
					EmpExecutionContext.info("流量套餐同步，手动同步失败");
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error("流量套餐同步，手动同步失败:"+e);
		}finally{
			if(pw!= null){
				pw.close();
			}
		}
	}

	private LlProduct getParm(HttpServletRequest request, PageInfo pageInfo,
			boolean isFirstEnter) {
		LlProduct resultBean = new LlProduct();
		try {
			//获取Bean数据
			LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
			LlCompInfoVo  llCompInfoBean= llCompInfoBiz.getLlCompInfoBean();
			String productid = request.getParameter("productid")==null?request.getParameter("productid"):request.getParameter("productid").trim();
			String productname = request.getParameter("productname")==null?request.getParameter("productname"):request.getParameter("productname").trim();
			String isp = request.getParameter("isp")==null?"9999":request.getParameter("isp");
			resultBean.setProductid(productid);
			resultBean.setProductname(productname);
			resultBean.setIsp(isp);
			if(llCompInfoBean !=null){
				resultBean.setEcid(Integer.parseInt(llCompInfoBean.getCorpCode()==null?"0":llCompInfoBean.getCorpCode()));
			}
			if(!isFirstEnter){
				resultBean.setIsFirstEnter("1");
			}else{
				resultBean.setIsFirstEnter("0");
			}
			int status = 1;
			resultBean.setStatus(status);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"LlProduct获取Bean数据异常");
		}
		return resultBean;
	}
	private void clearSearchCondition(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("llProduct");
	}
}
