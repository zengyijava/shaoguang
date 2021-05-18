package com.montnets.emp.pasgroup.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.pasgroup.biz.MwpUserDataBiz;
import com.montnets.emp.pasgroup.vo.UserPropertyVo;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.GwUserproperty;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.util.PageInfo;

/**
 * MWADMIN 接入SP账号参数设置
 * 
 * @功能概要：
 * @项目名称： p_txgl
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2017-4-27 上午10:42:38
 *        <p>
 *        修改记录1：
 *        </p>
 * 
 *        <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
@SuppressWarnings("serial")
public class mwp_userDataSvt extends BaseServlet
{
	final ErrorLoger				errorLoger	= new ErrorLoger();

	private final MwpUserDataBiz	mwuserbiz	= new MwpUserDataBiz();

	// 操作模块
	final String					opModule	= StaticValue.GATE_CONFIG;

	// 操作用户
	final String					opSper		= StaticValue.OPSPER;

	private final String			empRoot		= "txgl";

	private final String			basePath	= "/mwpasgroup";
	
	private final BaseBiz baseBiz=new BaseBiz();

	/**
	 * 接入SP账号参数列表查询
	 * 
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		long stime = System.currentTimeMillis();
		boolean isFirstEnter;
		PageInfo pageInfo = new PageInfo();
		try
		{
			List<Userdata> userdataList = null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			isFirstEnter = pageSet(pageInfo, request);
			if(!isFirstEnter)
			{
				// 账户名称
				conditionMap.put("staffName", request.getParameter("staffName").toUpperCase());
				conditionMap.put("userId", request.getParameter("userId").toUpperCase());
				// 账户状态
				conditionMap.put("status", request.getParameter("status"));
			}
			int corpType = StaticValue.getCORPTYPE();
			// 当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			// 企业编码
			String corpCode = lfSysuser.getCorpCode();
			if("100000".equals(corpCode))
			{
				userdataList = mwuserbiz.findSpUser(conditionMap, pageInfo);
			}
			else
			{
				if(corpType == 0)
				{
					// 单企业
					userdataList = mwuserbiz.findSpUser(conditionMap, pageInfo);
				}
				else
				{
					// 多企业
					userdataList = mwuserbiz.findSpUserByCorp(corpCode, conditionMap, pageInfo);
				}
			}
			// 加密后的集合(SP账号)
			Map<String, String> keyIdMap = new HashMap<String, String>();
			// 加密后的集合(UID)
			Map<Long, String> keyIdUidMap = new HashMap<Long, String>();
			// ID加密
			if(userdataList != null && userdataList.size() > 0)
			{
				// 加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				// 加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMapKeyStr(userdataList, "UserId", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询短彩SP账户列表，参数加密失败。corpCode:" + corpCode);
						throw new Exception("查询短彩SP账户列表，参数加密失败。");
					}
					result = encryptOrDecrypt.batchEncryptToMap(userdataList, "Uid", keyIdUidMap);
					if(!result)
					{
						EmpExecutionContext.error("查询短彩SP账户列表，参数UID加密失败。corpCode:" + corpCode);
						throw new Exception("查询短彩SP账户列表，参数UID加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询短彩SP账户列表，从session中获取加密对象为空！");
					userdataList.clear();
					throw new Exception("查询短彩SP账户列表，获取加密对象失败。");
				}
			}
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("userdataList", userdataList);
			request.setAttribute("keyIdMap", keyIdMap);
			request.setAttribute("keyIdUidMap", keyIdUidMap);
			request.setAttribute("conditionMap", conditionMap);
			if(StaticValue.getCORPTYPE() ==1){
				request.getRequestDispatcher(empRoot + basePath + "/mwp_userData1.jsp").forward(request, response);
			}else{
				request.getRequestDispatcher(empRoot + basePath + "/mwp_userData.jsp").forward(request, response);
			}
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request, "接入SP账号参数设置", "(" + sDate + ")查询", StaticValue.GET);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "接入SP账号参数设置查询异常！"));
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try
			{
				if(StaticValue.getCORPTYPE() ==1){
					request.getRequestDispatcher(empRoot + basePath + "/mwp_userData1.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher(empRoot + basePath + "/mwp_userData.jsp").forward(request, response);
				}
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(errorLoger.getErrorLog(e1, "接入SP账号参数设置查询servlet异常！"));
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(errorLoger.getErrorLog(e1, "接入SP账号参数设置查询servlet跳转异常！"));
			}
		}
	}

	/**
	 * 写日志
	 * 
	 * @param request
	 *        请求对象
	 * @param opModule
	 *        菜单名称
	 * @param opContent
	 *        操作内容
	 * @param opType
	 */
	private void setLog(HttpServletRequest request, String opModule, String opContent, String opType)
	{
		try
		{
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj != null)
			{
				LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), opContent, opType);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, opModule + opType + opContent + "日志写入异常"));
		}
	}

	/**
	 * 获取加密对象
	 * 
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
			// 加密对象
			Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
			// 加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				// 强转类型
				encryptOrDecrypt = (ParamsEncryptOrDecrypt) encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("短彩SP账户，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "短彩SP账户，从session获取加密对象异常。");
			return null;
		}
	}
	/**
	 * 去编辑
	 * 
	 * @param request
	 *        请求对象
	 * @param opModule
	 *        菜单名称
	 * @param opContent
	 *        操作内容
	 * @param opType
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response)
	{
		//帐号ID 唯一索引
		String userid=request.getParameter("userid");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		try
		{
			conditionMap.put("userid", userid);
			List<GwUserproperty> userProperty = baseBiz.getByCondition(GwUserproperty.class, conditionMap, null);
			
			request.setAttribute("userProperty", userProperty);
			request.getRequestDispatcher(empRoot + basePath + "/mwp_editData.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "跳转接入SP账号参数设置"));
		}
	}
	
	/**
	 * 去编辑 htts
	 * 
	 * @param request
	 *        请求对象
	 * @param opModule
	 *        菜单名称
	 * @param opContent
	 *        操作内容
	 * @param opType
	 */
	public void toEditHttps(HttpServletRequest request, HttpServletResponse response)
	{
		//帐号ID 唯一索引
		String userid=request.getParameter("userid");
		try
		{
			UserPropertyVo upvo=mwuserbiz.findUserproperty(userid);
			request.setAttribute("upvo", upvo);
			request.getRequestDispatcher(empRoot + basePath + "/mwp_editHttps.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "跳转接入SP账号HTTPS设置"));
		}
	}
	
	/**
	 * https设置
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2017-7-7 上午11:57:40
	 */
	public void sethttps(HttpServletRequest request, HttpServletResponse response)
	{
		String userid=request.getParameter("userid");
		String verifypeer=request.getParameter("verifypeer");
		String verifyhost=request.getParameter("verifyhost");
		String cacertname=request.getParameter("cacertname");
		boolean ret =false;
		PrintWriter writer=null;
		try
		{
			writer=response.getWriter();
			//SP账号
			if(userid==null||"".equals(userid)){
				writer.print("nouserid");
			}
			//服务端证书校验  1 不校验  2校验
			if(verifypeer==null||"".equals(verifypeer)){
				writer.print("noverifypeer");	
			}
			//不校验则  将服务器证书名称 设为空   且证书校验方式设置为  1 不检查 
			if("1".equals(verifypeer)){
				cacertname="";
				verifyhost="1";
			}else{
				//1 不检查 2 检查证书中是否有CN(common name)字段3 校验当前的域名是否与common name匹配
				if(verifyhost==null||"".equals(verifyhost)){
					writer.print("noverifyhost");	
				}
				if(cacertname==null){
					cacertname="";
				}
			}
			ret = this.mwuserbiz.updateHttps(userid,verifypeer,verifyhost,cacertname);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "跳转接入SP账号参数设置"));
		}
		if(writer!=null){
			writer.print(ret);
		}
	}
	
	
	/**
	 * 保存
	 * 
	 * @param request
	 *        请求对象
	 * @param opModule
	 *        菜单名称
	 * @param opContent
	 *        操作内容
	 * @param opType
	 */
	public void save(HttpServletRequest request, HttpServletResponse response)
	{
		//帐号ID 唯一索引
		String userpropertyID=request.getParameter("userpropertyID");
		String userid=request.getParameter("userid");
		String pathUrl=request.getParameter("pathUrl");
		GwUserproperty perty=new GwUserproperty();
		LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("spUser", userid);
		int corpint=100000;
		List<LfSpDepBind> deplist;
		try {
			deplist = baseBiz.getByCondition(LfSpDepBind.class,
					conditionMap, null);
			if(deplist!=null&&deplist.size()>0){
				
				corpint=Integer.parseInt(deplist.get(0).getCorpCode());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			EmpExecutionContext.error(e1, "查询sp账号异常");
		}
//		if(lfSysuser.getCorpCode()==null||"".equals(lfSysuser.getCorpCode())){
//			perty.setEcid(100000);
//		}else{
//			perty.setEcid(Integer.parseInt(lfSysuser.getCorpCode()));
//		}


		perty.setEcid(corpint);
		perty.setUserid(userid);
		
		//密码加密方式
		String pwdencode=request.getParameter("pwdencode");
		perty.setPwdencode(Integer.parseInt(pwdencode));
		//加密固定串
		String pwdEncodeStr=request.getParameter("pwdEncodeStr");
		perty.setPwdencodestr(pwdEncodeStr);
		//下行内容编码方式
		String msgCode=request.getParameter("msgCode");
		perty.setMsgcode(Integer.parseInt(msgCode));
		//下行内容加密方式
		String msgEnCode=request.getParameter("msgEnCode");
		perty.setMsgencode(Integer.parseInt(msgEnCode));
		//推送密码加密方式
		String pushPwdEncode=request.getParameter("pushPwdEncode");
		perty.setPushpwdencode(Integer.parseInt(pushPwdEncode));
		//推送加密固定串
		String pushPwdEncodeStr=request.getParameter("pushPwdEncodeStr");
		perty.setPushpwdencodestr(pushPwdEncodeStr);
		//推送内容编码方式
		String pushMsgCode=request.getParameter("pushMsgCode");
		if(pushMsgCode!=null&&!"".equals(pushMsgCode)){
			perty.setPushmsgcode(Integer.parseInt(pushMsgCode));
		}else{
			perty.setPushmsgcode(1);//明文
		}

		
		String pushMsgEnCode=request.getParameter("pushMsgEnCode");
		if(pushMsgEnCode!=null&&!"".equals(pushMsgEnCode)){
			perty.setPushmsgencode(Integer.parseInt(pushMsgEnCode));
		}else{
			perty.setPushmsgencode(1);//明文
		}
		
		
		//推送上行格式
		String pushMoFmt=request.getParameter("pushMoFmt");
		perty.setPushmofmt(Integer.parseInt(pushMoFmt));
		//推送状态报告格式
		String pushRptFmt=request.getParameter("pushRptFmt");
		perty.setPushrptfmt(Integer.parseInt(pushRptFmt));
		//重推次数
		String pushFailcnt=request.getParameter("pushFailcnt");
		if(pushFailcnt==null){
			pushFailcnt="0";
		}
		perty.setPushfailcnt(Integer.parseInt(pushFailcnt));
		//推送窗口大小
		String pushSlideWnd=request.getParameter("pushSlideWnd");
		perty.setPushslidewnd(Integer.parseInt(pushSlideWnd));
		//推送MO最大条数
		String pushMomaxCnt=request.getParameter("pushMomaxCnt");
		perty.setPushmomaxcnt(Integer.parseInt(pushMomaxCnt));
		//推送RPT最大条数
		String pushrptmaxcnt=request.getParameter("pushrptmaxcnt");
		perty.setPushrptmaxcnt(Integer.parseInt(pushrptmaxcnt));
		//获得MO最大条数
		String getmomaxcnt=request.getParameter("getmomaxcnt");
		perty.setGetmomaxcnt(Integer.parseInt(getmomaxcnt));
		//获得MO最大条数
		String getrptmaxcnt=request.getParameter("getrptmaxcnt");
		perty.setGetrptmaxcnt(Integer.parseInt(getrptmaxcnt));
		boolean ret =false;
		try
		{
			if(userpropertyID!=null&&!"".equals(userpropertyID)){
				perty.setId(Integer.parseInt(userpropertyID));
				ret = mwuserbiz.update(perty);
			}else{
				ret=mwuserbiz.save(perty);
			}

			
			response.sendRedirect(request.getContextPath()+"/mwp_userData.htm?method=toEdit&userid="+userid);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "跳转接入SP账号参数设置"));
		}
	}
	
	
	
}
