package com.montnets.emp.qyll.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.StringUtils;
/**
 * 
* @ClassName: ll_ClientRptReceiveSvt 
* @Description: 模拟客户端返回收到状态报告请求返回 
* @author xuty  
* @date 2017-12-25 下午1:53:14 
*
 */
public class ll_ClientRptReceiveSvt extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 987813933869588056L;
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
	    JsonObject paramJSON = new JsonObject();
	    PrintWriter pw  = null;
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
	        }
				pw = response.getWriter();
				pw.write(paramJSON.toString());
			
			
		} catch (Exception e) {
			EmpExecutionContext.error("送状态报告JSON返回出现异常：" + e.toString());
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
	
}
