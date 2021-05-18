/**
 * 
 */
package com.montnets.emp.common.servlet;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.advice.SubToUnionInterfaceClient;
import com.montnets.emp.common.advice.SubToUnionInterfaceSoap;
import com.montnets.emp.common.advice.Request;
import com.montnets.emp.common.advice.Response;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfSysuser;
/**
 * @author guodawei 2013-04-25
 *
 */
@SuppressWarnings("serial")
public class ComplainSvt extends BaseServlet {
	
	public void find(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		BaseBiz baseBiz = new BaseBiz();
		try {
			//反馈标题，暂时不用
			String ctitle = request.getParameter("ctitle");
			//反馈类型1：建议2：投诉3：咨询
			String ctype = request.getParameter("ctype");
			//反馈内容
			String ccontent = request.getParameter("ccontent");
			//电子邮件
			String cemail = request.getParameter("cemail");
			//联系电话
			String cphone = request.getParameter("cphone");
			//操作员id
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			LfSysuser loginSysuser= null;
			LfCorp corp = null;
			List<LfCorp> corpList = null;
			if(lguserid!=null && !"undefined".equals(lguserid))
			{
				loginSysuser =  baseBiz.getById(LfSysuser.class, lguserid);
			}
			if(loginSysuser!=null && loginSysuser.getCorpCode()!=null)
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("corpCode", loginSysuser.getCorpCode());
				corpList = baseBiz.getByCondition(LfCorp.class, conditionMap, null);		
			}
			if(corpList!=null && corpList.size()>0)
			{
				corp = corpList.get(0);
			}
			String version = SystemGlobals.getValue(StaticValue.EMP_WEB_VERSION);
			
			String result = reportComplain(ctitle,version, corp != null? corp.getCorpName() : "", loginSysuser == null ? "" : loginSysuser.getName(), cphone, cemail, ctype, ccontent);
			if(result!=null && "S004".equals(result))
			{
				response.getWriter().print("true");
			}else {
				response.getWriter().print("false");
			}
			
		} catch (Exception e) {
			response.getWriter().print("false");
			EmpExecutionContext.error(e, "反馈find方法异常。");
		}
	}
	
	private String reportComplain(String Title,String EmpVersion,String EcName,String UserName,String Phone,String Email,String Type,String Message) throws Exception
	{
		//消息内容xml格式
		String SvcCont = new StringBuffer("<CustomerSugRequest><Body><Title>").append(Title).append("</Title><EmpVersion>").append(EmpVersion)
							.append("</EmpVersion><EcName>").append(EcName).append("</EcName><UserName>")
							.append(UserName).append("</UserName><Phone>").append(Phone).append("</Phone><Email>")
							.append(Email).append("</Email><Type>").append(Type).append("</Type><Message>")
							.append(Message).append("</Message></Body></CustomerSugRequest>")
							.toString();
		SubToUnionInterfaceClient client = new SubToUnionInterfaceClient();
	        
        SubToUnionInterfaceSoap service = client.getSubToUnionInterfaceSoap();
        
        Request req = new Request();
        req.setBizCode("S004");
        req.setSvcCont(SvcCont);
	        
	    Response resp=  client.getSubToUnionInterfaceSoap().subUnionInterface(req);
	    return resp.getBizCode();
		
	}

}
