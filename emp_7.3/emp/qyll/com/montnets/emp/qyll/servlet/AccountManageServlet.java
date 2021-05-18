package com.montnets.emp.qyll.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.dao.LlCompInfoDao;
import com.montnets.emp.qyll.entity.EMI1004;
import com.montnets.emp.qyll.utils.EncryptOrDecrypt;
import com.montnets.emp.qyll.utils.HttpClientUtil;
import com.montnets.emp.qyll.utils.LldgUtil;
import com.montnets.emp.qyll.vo.LlCompInfoVo;

public class AccountManageServlet extends BaseServlet{

	private static final long serialVersionUID = 1L;
	private static final String PATH = "qyll/zhgl";
	
	public void find(HttpServletRequest request,HttpServletResponse response)  throws ServletException, IOException{
		try {
			LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
			//获取Bean数据
			LlCompInfoVo  llCompInfoBean= llCompInfoBiz.getLlCompInfoBean();
//			if(llCompInfoBean!=null&&!"".equals(llCompInfoBean)&&llCompInfoBean.getIp()!=null&&!"".equals(llCompInfoBean.getIp().trim())){
			if(llCompInfoBean!=null&&llCompInfoBean.getIp()!=null&&!"".equals(llCompInfoBean.getIp().trim())){
				String ipOne = llCompInfoBean.getIp().split("\\.")[0];
				String ipTwo = llCompInfoBean.getIp().split("\\.")[1];
				String ipThree = llCompInfoBean.getIp().split("\\.")[2];
				String ipFour = llCompInfoBean.getIp().split("\\.")[3];
				llCompInfoBean.setIpOne(ipOne);
				llCompInfoBean.setIpTwo(ipTwo);
				llCompInfoBean.setIpThree(ipThree);
				llCompInfoBean.setIpFour(ipFour);
			}
			request.setAttribute("llCompInfoBean", llCompInfoBean);
			request.getRequestDispatcher(PATH+"/ll_accountManage.jsp").forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "账号配置查询异常。");
		}
	}
	public void toConnection(HttpServletRequest request,HttpServletResponse response)  throws ServletException, IOException{
		try {
			Gson gson = new Gson();
			LlCompInfoVo llCompInfoBean = getParm(request);
			request.setAttribute("llCompInfoBean", llCompInfoBean);
			String url = "http://"+llCompInfoBean.getIp()+":"+llCompInfoBean.getPort()+"/mdgg/MdosEcHttp.hts";
			JsonObject json = new JsonObject();
	    	json.addProperty("CorpCode", llCompInfoBean.getCorpCode());
	    	String result = EncryptOrDecrypt.encryptString(json.toString(), llCompInfoBean.getPassword());
	    	JsonObject reqJson = new JsonObject();
	    	reqJson.addProperty("BCode", "EMI1004");
	    	reqJson.addProperty("Ack", "1");
	    	reqJson.addProperty("SqId", LldgUtil.createSqId());
	    	reqJson.addProperty("ECID", llCompInfoBean.getCorpCode());
	    	reqJson.addProperty("Cnxt", result);
	    	String msg = HttpClientUtil.doPostClient(reqJson,url);
	    	EMI1004 resultParam = gson.fromJson(msg, EMI1004.class);
	    	if("0".equals(llCompInfoBean.getIdent())){
//	    		if(resultParam != null && !("").equals(resultParam) && "0".equals(resultParam.getRtState())){
	    		if(resultParam != null &&  "0".equals(resultParam.getRtState())){
	    			response.getWriter().print("true");
	    		}else{
	    			response.getWriter().print("false");
	    		}
	    	}else{
//	    		if(resultParam != null && !("").equals(resultParam) && "0".equals(resultParam.getRtState())){
	    		if(resultParam != null && "0".equals(resultParam.getRtState())){
	    			synchronized(LlCompInfoDao.class) {
	    				LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
	    				LlCompInfoVo bean= llCompInfoBiz.getLlCompInfoBean();
	    				boolean flag =false;
	    				if(bean != null){
	    					//存在更新
	    					flag = llCompInfoBiz.updateLlCompInfo(llCompInfoBean);
	    				}else{
	    					//不存在插入
	    					flag = llCompInfoBiz.addLlCompInfo(llCompInfoBean);
	    				}
	    				if(flag){
	    					response.getWriter().print("1");
	    				}else{
	    					response.getWriter().print("0");
	    				}
	    			}
	    		}else{
	    			response.getWriter().print("0");
	    		}
	    	}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "账号配置连接异常。");
			LlCompInfoVo llCompInfoBean = getParm(request);
			if("0".equals(llCompInfoBean.getIdent())){
				response.getWriter().print("false");
			}else{
				response.getWriter().print("0");
			}
		}
		
	}
	private LlCompInfoVo getParm(HttpServletRequest request) {
		LlCompInfoVo returnBean = new LlCompInfoVo();
		String ecName = request.getParameter("ecName");
		String corpCode = request.getParameter("ecId");
		String password = request.getParameter("password");
		String ip = request.getParameter("ip");
		String port = request.getParameter("port");
		String ident = request.getParameter("ident");
		String pushAddr = request.getParameter("pushAddr");
		String reMark = request.getParameter("reMark")==""?"null":request.getParameter("reMark");
		returnBean.setEcName(ecName);
		returnBean.setCorpCode(corpCode);
		returnBean.setPassword(password);
		returnBean.setIp(ip);
		returnBean.setPort(port);
		returnBean.setIdent(ident);
		returnBean.setPushAddr(pushAddr);
		returnBean.setReMark(reMark);
		return returnBean;
	}
}
