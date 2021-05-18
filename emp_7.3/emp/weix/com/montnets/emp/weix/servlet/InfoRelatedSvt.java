package com.montnets.emp.weix.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.weix.LfWcUlink;
import com.montnets.emp.entity.weix.LfWcUserInfo;
import com.montnets.emp.entity.weix.LfWcVerify;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.weix.biz.WeixSendSmsBiz;

@SuppressWarnings("serial")
public class InfoRelatedSvt extends BaseServlet
{

	private final BaseBiz	baseBiz	= new BaseBiz();

	/**
	 * 跳到EMP信息关联页面
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String wcId = request.getParameter("wcId");
			String lgcorpcode = request.getParameter("lgcorpcode");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"跳转至信息关联页面失败！");
		}finally{
			request.getRequestDispatcher("/weix/common/cwc_infoRelated.jsp").forward(request, response);
		}
	}

	/**
	 * 保存微信用户与emp关联的信息
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void saveInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		response.setContentType("text/html;charset=utf-8");

		String result = "fail";
		try
		{

			String phone = request.getParameter("phone");
			String wcId = request.getParameter("wcId");
			String lgcorpcode = request.getParameter("lgcorpcode");
			String descr = request.getParameter("other");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

			// 找到对应的信息
			conditionMap.put("corpCode", lgcorpcode);
			conditionMap.put("wcId", wcId.toString());
			List<LfWcUserInfo> userInfoList = baseBiz.getByCondition(LfWcUserInfo.class, conditionMap, null);

			if(userInfoList != null && userInfoList.size() > 0)
			{

				LfWcUserInfo userInfo = userInfoList.get(0);
				userInfo.setPhone(phone);
				userInfo.setDescr(descr);
				userInfo.setVerifyTime(new Timestamp(System.currentTimeMillis()));
				// 1代表客户类型
				userInfo.setType(1);
				// 获取名称
				LfClient lfClient = getUname(phone, lgcorpcode);
				LfWcUlink ulink = null;
				if(lfClient != null)
				{
					// userInfo.setUname(lfClient.getName());
					// ulink = new LfWcUlink();
					// ulink.setUId(lfClient.getGuId());
					// ulink.setWcId(Long.parseLong(wcId));
					userInfo.setUId(lfClient.getGuId());
					userInfo.setUname(lfClient.getName());
				}
				// 填写关联表和保存信息(由于不用中间的关联表了，此方法暂不用了)
				// new WeixSendSmsBiz().saveInfo(userInfo, ulink);
				baseBiz.updateObj(userInfo);
				Object sysObj=request.getSession().getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					EmpExecutionContext.info("模块名称：企业微信，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，保存微信用户与emp关联的信息（电话号码："+phone+"）成功");
				}
				result = "success";
			}
			else
			{
				result = "notUserInfo";
			}
		}
		catch (Exception e)
		{
			result = "error";
			EmpExecutionContext.error(e,"保存关联信息失败！");
		}finally{
			request.setAttribute("result", result);
			request.getRequestDispatcher("/weix/common/cwc_infoRelated.jsp").forward(request, response);
		}
	}

	/**
	 * 检查手机号和验证码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void checkPC(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		response.setContentType("text/html;charset=utf-8");

		String result = "fail";
		try
		{

			String phone = request.getParameter("phone");
			String wcId = request.getParameter("wcId");
			String verifyCode = request.getParameter("verifyCode");
			String lgcorpcode = request.getParameter("lgcorpcode");
			if(wcId == null || "".equals(wcId) || lgcorpcode == null || "".equals(lgcorpcode))
			{
				// 参数传递不正确，信息出错
				response.getWriter().print("infoError");
				return;
			}
			WgMsgConfigBiz wgMsgConfigBiz = new WgMsgConfigBiz();
			// 拿到当前系统配置的号段
			String[] haoduans = wgMsgConfigBiz.getHaoduan();
			// 过滤号段
			if(new PhoneUtil().checkMobile(phone, haoduans) == 1)
			{
				// 检验验证码是否正确(验证码当天有效)
				String checkYzm = checkYzm(verifyCode, phone, wcId, lgcorpcode);
				result = checkYzm;
			}
			else
			{
				result = "phoneError";
			}
		}
		catch (Exception e)
		{
			result = "error";
			EmpExecutionContext.error(e,"手机号和验证码校验失败！");
		}
		response.getWriter().write(result);
	}

	/**
	 * 检验验证码是否正确,
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public String checkYzm(String verifyCode, String phone, String wcId, String corpCode) throws Exception
	{
		try
		{

			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			map.put("wcId", wcId.toString());
			map.put("corpCode", corpCode);
			map.put("phone", phone);
			orderbyMap.put("limitTime", "desc");
			List<LfWcVerify> verifies = baseBiz.getByCondition(LfWcVerify.class, map, orderbyMap);
			if(verifies != null && verifies.size() > 0)
			{
				LfWcVerify verify = verifies.get(0);

				if(verify.getVerifyCode() == null)
				{
					return "codeNull";
				}
				String result = checkVerifyCount(verify);
				if("countOut".equals(result))
				{
					return result;
				}
				
				Calendar curDate = Calendar.getInstance();
				curDate.add(curDate.HOUR, -1);
				Calendar upDate = Calendar.getInstance();
				Timestamp codeTime = verify.getCodeTime();
//findbugs:		if(codeTime!=null && !"".equals(codeTime))
				if(codeTime!=null)
				{
					
					upDate.setTimeInMillis(verify.getCodeTime().getTime());
				}

				// 验证码一个小时内有效
				if(curDate.before(upDate))
				{
					// 检验是否匹配
					if(verifyCode.equals(verify.getVerifyCode()))
					{
						return "true";
					}
					else
					{
						return "false";
					}
				}
				else
				{
					// 验证码失效
					return "codeOutTime";
				}
			}
			else
			{
				return "codeNull";
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"检验验证码验证失败！");
			throw e;
		}
	}

	/**
	 * 检验是否超过提交次数，一天十次
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkVerifyCount(LfWcVerify verify) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp curTime = new Timestamp(System.currentTimeMillis());
		String curD = sdf.format(curTime);
		Timestamp codeTime = verify.getCodeTime();
		if(codeTime != null)
		{

			String codeDate = sdf.format(codeTime);
			if(curD.equals(codeDate))
			{
				if(verify.getVerifyCount() > 10)
				{
					return "countOut";
				}
				else
				{
					verify.setVerifyCount(verify.getVerifyCount() != null ? verify.getVerifyCount() + 1 : 1);
					baseBiz.updateObj(verify);
				}
			}
			// 更新时间且次数清零用于下次判断次数用，用于做一天最多提交十次限制
			else
			{
				verify.setVerifyCount(1);
				verify.setCodeTime(new Timestamp(System.currentTimeMillis()));
				baseBiz.updateObj(verify);
			}
		}
		else
		{
			verify.setVerifyCount(1);
			verify.setCodeTime(new Timestamp(System.currentTimeMillis()));
			baseBiz.updateObj(verify);
		}
		return "true";
	}

	/**
	 * 根据手机号获取客户表的对应信息
	 * 
	 * @param phone
	 *        手机号
	 * @param lgcorpcode
	 *        企业编码
	 * @return
	 * @throws Exception
	 */
	public LfClient getUname(String phone, String lgcorpcode) throws Exception
	{
		try
		{
			LfClient client = null;
			LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
			conMap.put("mobile", phone);
			conMap.put("corpCode", lgcorpcode);
			// ----------------这里的逻辑是通过手机号找姓名-------------------------
			// 去客户表找对应信息
			List<LfClient> clientList = baseBiz.getByCondition(LfClient.class, conMap, null);
			if(clientList != null && clientList.size() > 0)
			{
				client = clientList.get(0);
			}
			return client;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"根据手机号获取客户表的对应信息失败！");
			throw e;
		}
	}

	/**
	 * 获取验证码的业务，5分钟内只能取一次
	 * 
	 * @throws Exception
	 */
	public boolean checkGetYzm(String wcId, String corpCode, String phone)
	{
		LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		conMap.put("corpCode", corpCode);
		conMap.put("wcId", wcId);
		orderbyMap.put("limitTime", "desc");
		try
		{
			List<LfWcVerify> verifieList = baseBiz.getByCondition(LfWcVerify.class, conMap, orderbyMap);
			if(verifieList != null && verifieList.size() > 0)
			{
				LfWcVerify verifie = verifieList.get(0);
				Calendar curDate = Calendar.getInstance();
				Calendar upDate = Calendar.getInstance();
				upDate.setTimeInMillis(verifie.getLimitTime().getTime());
				upDate.add(upDate.MINUTE, 5);
				// 判断是否五分钟之内
				if(upDate.after(curDate))
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取验证码失败！");
			return false;
		}
	}

	/**
	 * 数据存库处理
	 * 
	 * @param verify
	 * @param state
	 *        1.添加 2.修改
	 * @throws Exception
	 */
	public void saveData(LfWcVerify verify, int state) throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", verify.getCorpCode());
		conditionMap.put("wcId", verify.getWcId().toString());
		// conditionMap.put("phone", verify.getPhone());
		// 没有发送成功不保存验证码
		if(state == 1)
		{
			verify.setVerifyCode(null);
		}
		
		verify.setCodeTime(new Timestamp(System.currentTimeMillis()));
		// 设置验证码获取时间
		verify.setLimitTime(new Timestamp(System.currentTimeMillis()));
		List<LfWcVerify> verifieList = baseBiz.getByCondition(LfWcVerify.class, conditionMap, null);
		// 添加
		if(verifieList != null && verifieList.size() > 0)
		{
			LfWcVerify vf = verifieList.get(0);
			verify.setVfId(vf.getVfId());
			//这里判断提交次数是否大于10是判断 10之内就更新codetime，不改次数
			//超过10次，看是否为当天，当天的话也只更新codetiem不更新次数,不是当天更新codeTime和将允许提价次数清零
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp curTime = new Timestamp(System.currentTimeMillis());
			String curD = sdf.format(curTime);
			Timestamp codeTime = vf.getCodeTime();
			String codeDate = sdf.format(codeTime);
			//这里做判断若果是
			if(!curD.equals(codeDate))
			{
				verify.setVerifyCount(0);
			}
			baseBiz.updateObj(verify);
		}
		// 修改
		else
		{
			verify.setCodeTime(new Timestamp(System.currentTimeMillis()));
			verify.setVerifyCount(0);
			baseBiz.addObj(verify);
		}
	}

	/**
	 * 发送验证码
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void sendYzm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try
		{
			String wcId = request.getParameter("wcId");
			String corpCode = request.getParameter("lgcorpcode");
			String phone = request.getParameter("phone");
			if(wcId == null || "".equals(wcId) || corpCode == null || "".equals(corpCode))
			{
				// 参数传递不正确，信息出错
				out.print("infoError");
				return;
			}
			boolean isOver = checkGetYzm(wcId, corpCode, phone);
			if(!isOver)
			{
				// 验证码5分钟只能获取一次
				out.print("codeTiemInFive");
				return;
			}
			SmsBiz smsbiz = new SmsBiz();
			SubnoManagerBiz subnoManagerBiz = new SubnoManagerBiz();
			WgMsgConfigBiz wgMsgConfigBiz = new WgMsgConfigBiz();
			WeixSendSmsBiz wsBiz = new WeixSendSmsBiz();

			// 生成一个5位的随机数做为验证码
			int count = 5;
			StringBuffer sb = new StringBuffer();
			String str = "0123456789";
			Random r = new Random();
			for (int i = 0; i < count; i++)
			{
				int num = r.nextInt(str.length());
				sb.append(str.charAt(num));
				str = str.replace((str.charAt(num) + ""), "");
			}

			String verifyCode = sb.toString();
			LfWcVerify verify = new LfWcVerify();
			verify.setCorpCode(corpCode);
			verify.setWcId(Long.parseLong(wcId));
			verify.setPhone(phone);
			verify.setVerifyCode(verifyCode);
			// 发送短信内容
			String msg = verifyCode;
			if(phone != null && !"".equals(phone))
			{
				// 获取当前登录企业的admin用户信息
				LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
				conditionMap1.put("userName", "admin");// admin用户
				conditionMap1.put("corpCode", corpCode);// 当前登录用户的企业编码
				List<LfSysuser> AdminLfSysuerList = baseBiz.getByCondition(LfSysuser.class, conditionMap1, null);
				LfSysuser AdminUser = null;
				if(AdminLfSysuerList != null && AdminLfSysuerList.size() > 0)
				{
					AdminUser = AdminLfSysuerList.get(0);
				}
				// sp帐号
				String sp = "";
				String isUsable = "";
				ErrorCodeParam errorCode = new ErrorCodeParam();
				// 扩展尾号
				String subno = GlobalVariableBiz.getInstance().getValidSubno(StaticValue.VERIFYREMIND_MENUCODE, 0, corpCode, errorCode);
				// 获取sp帐号(根据admin用户绑定的)
				List<Userdata> spUserList = smsbiz.getSpUserList(AdminUser);
				// spuserlist为空说明没有可用的帐号
				// 验证该发送帐号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
				// 发送帐号
				// isUsable 1通过 2失败 没有绑定一条上下行路由/下行路由 3失败 发送帐号没有绑定路由 4
				// 该发送帐号的全通道号有一个超过20位
				if(spUserList.size() > 0)
				{
					for (Userdata spUser : spUserList)
					{
						if(subno != null)
						{
							isUsable = subnoManagerBiz.checkPortUsed(spUser.getUserId(), subno);
							if("1".equals(isUsable))
							{
								sp = spUser.getUserId();
								break;
							}
						}
						else
						{
							// throw new Exception("子号已使用完！");
							saveData(verify, 1);
							out.print("Nosubno");
							return;
						}
					}
				}

				if(!"".equals(sp) && sp != null)
				{
					// 拿到当前系统配置的号段
					String[] haoduans = wgMsgConfigBiz.getHaoduan();
					// 过滤号段
					if(new PhoneUtil().checkMobile(phone, haoduans) == 1)
					{
						// 是否计费
						boolean flag = SystemGlobals.isDepBilling(corpCode);
						int messageCount = 1;// 发送条数
						String Rptflag = "0";// 是否需要返回状态报告 0不需要
						// 调用发送方法
						String isReturn = wsBiz.sendOneSms(phone, msg, sp, subno, messageCount, AdminUser, flag, Rptflag, StaticValue.VERIFYREMIND_MENUCODE);

						if("2".equals(isReturn))
						{
							saveData(verify, 1);
							// 发送手机验证码到网关失败
							Object sysObj=request.getSession().getAttribute("loginSysuser");
							if(sysObj!=null){
								LfSysuser lfSysuser=(LfSysuser)sysObj;
								EmpExecutionContext.error("模块名称：企业微信，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，发送手机验证码（手机号码："+phone+"）到网关失败");
							}
							out.print("sendfalse");
							return;
						}
						else
							if("6".equals(isReturn) || "9".equals(isReturn))
							{
								saveData(verify, 1);
								Object sysObj=request.getSession().getAttribute("loginSysuser");
								if(sysObj!=null){
									LfSysuser lfSysuser=(LfSysuser)sysObj;
									EmpExecutionContext.error("模块名称：企业微信，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，手机号码："+phone+"，开启了记费，但短信余额不足");
								}
								// 开启了记费，但短信余额不足
								out.print("NoMoney");
								return;
							}
							else if("7".equals(isReturn))
								{
									saveData(verify, 1);
									// 扣费失败
									Object sysObj=request.getSession().getAttribute("loginSysuser");
									if(sysObj!=null){
										LfSysuser lfSysuser=(LfSysuser)sysObj;
										EmpExecutionContext.error("模块名称：企业微信，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，手机号码："+phone+"，扣费失败");
									}
									out.print("Moneyerror");
									return;
								}
								else if(isReturn.contains("1,"))
									{
										saveData(verify, 2);
										// 发送验证码到网关成功
										Object sysObj=request.getSession().getAttribute("loginSysuser");
										if(sysObj!=null){
											LfSysuser lfSysuser=(LfSysuser)sysObj;
											EmpExecutionContext.info("模块名称：企业微信，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，手机号码："+phone+"，发送验证码到网关成功");
										}
										out.print("success");
									}
									else
									{
										saveData(verify, 1);
										Object sysObj=request.getSession().getAttribute("loginSysuser");
										if(sysObj!=null){
											LfSysuser lfSysuser=(LfSysuser)sysObj;
											EmpExecutionContext.error("模块名称：企业微信，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，手机号码："+phone+"，发送验证码到网关失败");
										}
										out.print("error");
										return;
									}
					}
					else
					{
						// saveData(verify,1);
						// 手机号码错误
						Object sysObj=request.getSession().getAttribute("loginSysuser");
						if(sysObj!=null){
							LfSysuser lfSysuser=(LfSysuser)sysObj;
							EmpExecutionContext.error("模块名称：企业微信，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，手机号码："+phone+"，手机号码错误");
						}
						out.print("errorphone");
						return;
					}
				}
				else
				{
					saveData(verify, 1);
					// 没有可用的发送账户则不发
					Object sysObj=request.getSession().getAttribute("loginSysuser");
					if(sysObj!=null){
						LfSysuser lfSysuser=(LfSysuser)sysObj;
						EmpExecutionContext.error("模块名称：企业微信，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，手机号码："+phone+"，没有可用的发送账户");
					}
					out.print("Nospid");
					return;
				}

			}
			else
			{
				saveData(verify, 1);
				// 手机号码错误
				Object sysObj=request.getSession().getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					EmpExecutionContext.error("模块名称：企业微信，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，手机号码错误");
				}
				out.print("NouserPhone");
				return;
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"发送验证码失败！");
			out.print("error");
		}
	}
}
