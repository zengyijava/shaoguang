package com.montnets.emp.segnumber.servlet;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.segnumber.PbServicetype;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PageInfo;
/**
 * 号段管理
 * @project p_txgl_3.9
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-8-1 下午05:26:57
 * @description
 */
@SuppressWarnings("serial")
public class seg_phoneNoSvt extends BaseServlet 
{
	final String empRoot = "txgl";
	final String basePath = "/segnumber";
	final ErrorLoger errorLoger = new ErrorLoger();
	
	/**
	 * 网关号段查询方法
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		
		PageInfo pageInfo = new PageInfo();
		try {
			//初始化页面
			pageSet(pageInfo,request);
			//得到网关号段列表
			List<PbServicetype> psList = new BaseBiz().getByCondition(PbServicetype.class, null, null);
			//加密后的集合
			Map<Long, String> keyIdMap = new HashMap<Long, String>();
			//ID加密
			if(psList != null && psList.size() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMap(psList, "Spisuncm", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询运营商号段列表，参数加密失败。");
						throw new Exception("查询运营商号段列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询运营商号段列表，从session中获取加密对象为空！");
					psList.clear();
					throw new Exception("查询运营商号段列表，获取加密对象失败。");
				}
			}
			//传到页面的分页面信息
			request.setAttribute("pageInfo", pageInfo);
			//传到页面的网关号段列表信息
			request.setAttribute("psList", psList);
			//加密对象
			request.setAttribute("keyIdMap", keyIdMap);
			//跳转到号段页面
			request.getRequestDispatcher(this.empRoot  + this.basePath +"/seg_phoneNo.jsp").forward(request, response);
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"网关号段查询异常！"));
			//如果报错，则在页面上提示
			request.setAttribute("findresult", "-1");
			try {
				request.getRequestDispatcher(this.empRoot + this.basePath +"/seg_phoneNo.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"网关号段查询异常！"));
			} catch (IOException e1) {				
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"网关号段查询异常！"));
			}
		}
	}
	//更新号段号码
	public void update(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		String corpcode="";
		String opUser="";
		try
		{
			//response.setContentType("text/html;charset=UTF-8");
			LfSysuser lfSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpcode=lfSysuser.getCorpCode();
			opUser=lfSysuser.getUserName();
			writer = response.getWriter();
			//获取页面SPID信息
			//String spid = request.getParameter("spid");
			String spid;
			String keyId=request.getParameter("keyId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				spid = encryptOrDecrypt.decrypt(keyId);
				if(spid == null)
				{
					EmpExecutionContext.error("修改运营商号段信息，参数解密码失败，keyId:"+keyId);
					throw new Exception("修改运营商号段信息，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("修改运营商号段信息，从session中获取加密对象为空！");
				throw new Exception("修改运营商号段信息，获取加密对象失败。");
			}
			//获取修改后的号码
			String phoneNo = request.getParameter("phoneNo");
			//把修改后的信息放入一个map里更新
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//要更新的对象信息
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			conditionMap.put("spisuncm", spid);
			objectMap.put("serviceno", phoneNo);
			
			//记录日志总字符串
			String opContent="";
			//修改前数据字符串
			String oldStr="";
			//修改后数据字符串
			String newStr="";
			List<PbServicetype> pblist=new BaseBiz().getByCondition(PbServicetype.class, conditionMap, null);
			if(pblist!=null&&pblist.size()>0){
				PbServicetype pbst=pblist.get(0);
				oldStr=pbst.getSpisuncm()+"，"+pbst.getServiceno();
			}
			newStr=spid+"，"+phoneNo;
			boolean result=new BaseBiz().update(PbServicetype.class, objectMap, conditionMap);
			String cgsb="";
			if(result==true){
				cgsb = "成功";
			}else{
				cgsb = "失败";
			}
			opContent="修改号段"+cgsb+"。[运营商ID，运营商号段]("+oldStr+"-->"+newStr+")";
			writer.print(result);
			//同步号段值 
			new WgMsgConfigBiz().initNumSegmentInfo();
			//EmpExecutionContext.error("企业：" +corpcode+",操作员：" +opUser+"修改号段成功！");
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info("号段管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, "UPDATE");
//				EmpExecutionContext.info("模块名称：号段管理，企业："+loginSysuser.getCorpCode()+"，"
//				+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
//				+opContent);
			}

		}
		//获取异常
		catch (Exception e) 
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"企业：" +corpcode+",操作员：" +opUser+"修改号段异常！"));
		}
	}
	
	/**
	 * 获取加密对象
	 * @description    
	 * @param request
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午07:29:28
	 */
	public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request)
	{
		try
		{
			ParamsEncryptOrDecrypt encryptOrDecrypt = null;
			//加密对象
			Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("运营商号段，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "运营商号段，从session获取加密对象异常。");
			return null;
		}
	}
}
