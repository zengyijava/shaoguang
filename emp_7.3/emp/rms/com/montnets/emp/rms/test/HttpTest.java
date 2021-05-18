package com.montnets.emp.rms.test;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.biz.MBOSSApiBiz;
import com.montnets.emp.rms.rmsapi.biz.RMSApiBiz;
import com.montnets.emp.rms.rmsapi.biz.impl.IMBOSSApiBiz;
import com.montnets.emp.rms.rmsapi.biz.impl.IRMSApiBiz;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;
import com.montnets.emp.rms.rmsapi.model.QueryHisRecordParams;
import com.montnets.emp.rms.rmsapi.model.SendTempParams;
import com.montnets.emp.rms.rmsapi.model.TempParams;
import com.montnets.emp.webservice.balance.RMSBalanceWebservice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTest extends HttpServlet
{
	private static final long serialVersionUID = 8389701167469408005L;
	private final RMSApiBiz biz = new IRMSApiBiz();
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String temp = request.getParameter("method");
		String method = "find";
		if(temp!=null){
			method=temp;
		}
		
		try
		{
			Class c = getClass();
			Object Objparams[] =
			{ request, response };
			Method clazzMethod = c.getMethod(method, new Class[]
			{ HttpServletRequest.class, HttpServletResponse.class });
			clazzMethod.invoke(this, Objparams);
		} catch (Exception e1)
		{
            EmpExecutionContext.error(e1, "发现异常");
		}
	}

	public void subTemplate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		
		List<TempParams> tempList = new ArrayList<TempParams>();
		try
		{
			req.setCharacterEncoding("utf-8");
			resp.setContentType("text/html;charset=utf-8;");
			TempParams temp=new TempParams();
			temp.setType(1);
			temp.setSize(100);
			temp.setPnum(10);
			temp.setDegree(1);
			temp.setContent("<html><body>dddddddd我爱你中国</body></html");
			tempList.add(temp);
			Map<String,String> map= biz.subTemplate(RMSHttpConstant.RMS_USERID, RMSHttpConstant.RMS_PASS, tempList);
			resp.getWriter().write(map.toString());
		} catch (Exception e)
		{
            EmpExecutionContext.error(e, "发现异常");
		}
	}
	
	public void deleteTemplates(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		
		try
		{
			Map<String,String> map= biz.deleteTemplates( RMSHttpConstant.RMS_USERID, RMSHttpConstant.RMS_PASS, "810000001");
			resp.getWriter().write(map.toString());
		} catch (Exception e)
		{
            EmpExecutionContext.error(e, "发现异常");
		}
	}
	public void sendTemplate(HttpServletRequest request, HttpServletResponse respponse) throws ServletException, IOException
	{
		
		try
		{
			String sp=request.getParameter("sp");
			String password=request.getParameter("pwd");
			String tmplid=request.getParameter("tmplid");
			request.setCharacterEncoding("utf-8");
			respponse.setContentType("text/html;charset=utf-8;");
			SendTempParams tempParams=new SendTempParams();
			tempParams.setContent("");
			tempParams.setTmplid(tmplid);
			tempParams.setMobile("15112605627");
			tempParams.setUserid(sp);
			tempParams.setPwd(password);
			Map<String,String> map= biz.sendTemplate(tempParams);
			respponse.getWriter().write(map.toString());
		} catch (Exception e)
		{
            EmpExecutionContext.error(e, "发现异常");
		}
	}
	public void queryTemplateStatus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		
		try
		{
			req.setCharacterEncoding("utf-8");
			resp.setContentType("text/html;charset=utf-8;");
			String tempIds="810000001";
			Map<String,Object> map= biz.queryTemplateStatus(RMSHttpConstant.RMS_USERID, RMSHttpConstant.RMS_PASS, tempIds);
			resp.getWriter().write(map.toString());
		} catch (Exception e)
		{
            EmpExecutionContext.error(e, "发现异常");
		}
	}
	public void queryHisRecord(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		
		try
		{
			req.setCharacterEncoding("utf-8");
			resp.setContentType("text/html;charset=utf-8;");
			String userid=req.getParameter("userid");
			String pageSize=req.getParameter("pageSize");
			MBOSSApiBiz mboss=new IMBOSSApiBiz();
			
			QueryHisRecordParams params=new QueryHisRecordParams();
			
			params.setUserid(userid);
			params.setStarttime("2018-02-02 00:55:06");
			params.setEndtime("2018-02-02 20:55:06");
			params.setPagesize(Integer.parseInt(pageSize));
			params.setPageindex(1);
			params.setChgrade(-1);
			params.setErrcode(-1);
			Map<String,Object> map= mboss.queryHisRecord(params);
			
			if(map.get("scont")!=null){
				List<Map<String,String>> queryList=(List<Map<String, String>>) map.get("scont");
				
				if (queryList!=null&&!queryList.isEmpty())
				{
					for (Map<String, String> map2 : queryList)
					{
						for (String key : map2.keySet())
						{
							System.out.print(map2.get(key) + "  ");
						}
						System.out.println("  ");
					}
					System.out.println("记录数：" + queryList.size());
				}
			}
			resp.getWriter().write(map.toString());
		} catch (Exception e)
		{
            EmpExecutionContext.error(e, "发现异常");
		}
	}
	
	public void getBlance(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		try
		{
			String sp=request.getParameter("sp");
			String password=request.getParameter("pwd");
			String type=request.getParameter("type");
			
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8;");
		RMSBalanceWebservice balanceUtils=new RMSBalanceWebservice();
		String []result=balanceUtils.getAccountBalance(sp, password, "http://192.169.2.203:8080/WebService/SubToUnionInterface.asmx?WSDL", type);
		Map<String,String> map=new HashMap<String, String>();
		/*for (String string : result)
		{
			
		}*/
		if(result!=null&&result.length>=2){
			map.put("result", result[0]);
			map.put("error", result[1]);
		}
		response.getWriter().write(map.toString());
		} catch (Exception e)
		{
            EmpExecutionContext.error(e, "发现异常");
		}
	}


}
