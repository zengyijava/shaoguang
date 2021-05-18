package com.montnets.emp.authen.servlet;

import com.montnets.emp.authen.atom.AuthenAtom;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.Message;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.securectrl.LfDynPhoneWord;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public class DynGetPhoneWord extends BaseServlet
{
	final BaseBiz				baseBiz		= new BaseBiz();

	// 操作模块
	public static final String	auditModule	= "登录";

	// 操作用户
	final String				opSper		= StaticValue.OPSPER;

	private final SuperOpLog	spLog		= new SuperOpLog();

	public void find(HttpServletRequest request, HttpServletResponse response) throws Exception
	{

	}

	/**
	 * 忘记密码 输入用户名称 姓名获取其手机号码
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getAutoUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String autoUserName = request.getParameter("autoUserName");
		AuthenAtom authen = new AuthenAtom();
		// ****增加是否存在sql注入漏洞
		if(!authen.holesProcessing(autoUserName))
		{
			response.getWriter().print("tingyong");
			return;
		}
		String autoName = request.getParameter("autoName");
		// ****增加是否存在sql注入漏洞
		if(!authen.holesProcessing(autoName))
		{
			response.getWriter().print("tingyong");
			return;
		}
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LfSysuser sysuser = null;
		StringBuffer strbuffer = new StringBuffer();
		try
		{
			conditionMap.put("userName", autoUserName);
			conditionMap.put("name", autoName);
			List<LfSysuser> userLIst = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
			if(userLIst != null && userLIst.size() > 0)
			{
				sysuser = userLIst.get(0);
				if(sysuser.getUserState() != null && (sysuser.getUserState() == 0 || sysuser.getUserState() == 2))
				{
					response.getWriter().print("tingyong");
					return;
				}
				// 2013-12-18 去掉密码接收人的逻辑，改为当前人号码
				// Long depId = sysuser.getDepId();
				conditionMap.clear();
				// conditionMap.put("depid",String.valueOf(depId));
				//				
				// List<LfDeppwdReceiver> receiverList =
				// baseBiz.getByCondition(LfDeppwdReceiver.class, conditionMap,
				// null);
				// if(receiverList != null && receiverList.size()>0){
				// String idstr = "";
				// for(int i =0 ;i<receiverList.size();i++){
				// LfDeppwdReceiver receiver = receiverList.get(i);
				// idstr = idstr + receiver.getUserid() + ",";
				// }
				//					
				// if(idstr != null && !"".equals(idstr.toString())){
				// idstr = idstr.substring(0,idstr.length()-1);
				// }
				// conditionMap.clear();
				// conditionMap.put("userId&in", idstr);
				// List<LfSysuser> userList =
				// baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
				// LinkedHashMap<Long, LfSysuser> nameMap = new
				// LinkedHashMap<Long, LfSysuser>();
				// if(userList != null && userList.size() > 0){
				// for(LfSysuser temp:userList){
				// nameMap.put(temp.getUserId(), temp);
				// }
				// }
				// for(int i =0 ;i<receiverList.size();i++){
				// LfDeppwdReceiver receiver = receiverList.get(i);
				// LfSysuser a = null;
				// a = nameMap.get(receiver.getUserid());
				// String moblie = a.getMobile();
				// if(!"".equals(moblie)){
				// moblie = moblie.substring(0,3)+ "****" +
				// moblie.substring(moblie.length()-4,moblie.length());
				// }else{
				// moblie = "-";
				// }
				// strbuffer.append("<option value='").append(receiver.getUserid()).append("'").append(" moblie='").append(a.getMobile()).append("'")
				// .append(" hiddenMoblie='").append(moblie).append("'")
				// .append(" name='").append(a.getName().trim().replace("<","&lt;").replace(">","&gt;")).append("' >");
				// strbuffer.append(a.getName().trim().replace("<","&lt;").replace(">","&gt;")).append("(").append(moblie).append(")</option>");
				// }
				// }else{
				String moblie = sysuser.getMobile();
				if(!"".equals(moblie))
				{
					moblie = moblie.substring(0, 3) + "****" + moblie.substring(moblie.length() - 4, moblie.length());
				}
				else
				{
					moblie = "-";
				}
				strbuffer.append("<option value='").append(sysuser.getUserId()).append("'").append(" moblie='").append(sysuser.getMobile()).append("'").append(" hiddenMoblie='").append(moblie).append("'").append(" name='").append(sysuser.getName().trim().replace("<", "&lt;").replace(">", "&gt;")).append("' >");
				strbuffer.append(sysuser.getName().trim().replace("<", "&lt;").replace(">", "&gt;")).append("(").append(moblie).append(")</option>");
			}
			// }
			response.getWriter().print(strbuffer.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "姓名获取其手机号码异常！ ");
		}
	}

	/**
	 * 获取发送手机动态口令的状态报告
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getSendState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		String username = request.getParameter("username");
		String enpCode = request.getParameter("enpCode");
		LoginBiz loginBiz = new LoginBiz();
		try
		{
			// 根据用户名及企业编码获取用户信息
			List<LfSysuser> lfSysuserList = loginBiz.getLfSysUserByUP(username.toUpperCase(), null, enpCode);

			LfSysuser sysuser = new LfSysuser();
			if(lfSysuserList != null && lfSysuserList.size() > 0)
			{

				sysuser = lfSysuserList.get(0);
			}
			// 如果此用户的状态报告返回的成功则动态口令发送成功，否则动态口令发送到运营商失败
			conditionMap.put("userId", sysuser.getUserId().toString());
			// conditionMap.put("errorCode", "0000000");
			List<LfDynPhoneWord> PhoneWordList = baseBiz.getByCondition(LfDynPhoneWord.class, conditionMap, null);

			if(PhoneWordList != null && PhoneWordList.size() > 0)
			{
				LfDynPhoneWord dp = PhoneWordList.get(0);
				if("0000000".equals(dp.getErrorCode()))
				{
					// 网关发送成功
					out.print("true");
				}
				else if(dp.getErrorCode() == null || "".equals(dp.getErrorCode().trim()))
				{
					// 动态口令接收状态未知！可重新获取。
					out.print("norpt");
				}
				return;
			}
			else
			{
				out.print("false");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取发送手机动态口令的状态报告异常！ ");
			// 异常错误
			out.print("false");
			return;
		}
		return;
	}

	/**
	 * 下发短信密码
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void sendAutoPass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 密码接收人的手机号码
		String moblie = request.getParameter("moblie");
		// 密码接收人的用户ID
		// String autoUserId = request.getParameter("autoUserId");
		// 申请人的用户账号
		String autoUserName = request.getParameter("autoUserName");
		// 申请人的名字
		String autoName = request.getParameter("autoName");
		// 重置密码原因 1密码遗忘 2 输错次数过多 3系统原因
		String autoReason = request.getParameter("autoReason");
		// 公司编码
		String corpCode = request.getParameter("corpCode");
		if("".equals(corpCode) || corpCode == null)
		{
			corpCode = "100001";
		}
		// 用于验证作弊情况
		String cheating = preventCheating(request, response, autoUserName, autoName, moblie);
		if(!"".equals(cheating))
		{
			response.getWriter().print(cheating);
			return;
		}
		String oppType = StaticValue.UPDATE;
		String opModule = StaticValue.USERS_MANAGER;
		String opSper = StaticValue.OPSPER;
		String opContent = "密码遗忘";
		if("2".equals(autoReason))
		{
			opContent = "输错次数过多";
		}
		else if("3".equals(autoReason))
		{
			opContent = "系统原因";
		}
		String opUser = autoUserName;

		LfSysuser sysuser = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			// sysuser = baseBiz.getById(LfSysuser.class, autoUserId);
			conditionMap.put("userName", autoUserName);
			conditionMap.put("name", autoName);
			List<LfSysuser> userLIst = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
			if(userLIst != null && userLIst.size() > 0)
			{
				sysuser = userLIst.get(0);
			}
			else
			{
				response.getWriter().print("fail");
				return;
			}
			// 生成一个4位的随机数做为动态口令
			int count = 6;
			StringBuffer sb = new StringBuffer();
			String str = "0123456789";
			Random r = new Random();
			for (int i = 0; i < count; i++)
			{
				int num = r.nextInt(str.length());
				sb.append(str.charAt(num));
				str = str.replace((str.charAt(num) + ""), "");
			}
			// 获得找回密码的次数（不能超过3次）
			// SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
			// String today=sdf.format(new Date());
			// int times=sysuser.getFindPwdtimes();
			// String date=sysuser.getFindPwddate();
			// if(date!=null&&!"".equals(date)){
			// if(today.equals(date)){
			// if(times>=3){
			// response.getWriter().print("allow3times");
			// return;
			// }else{
			// sysuser.setFindPwdtimes(times+1);
			// }
			// }else{
			// sysuser.setFindPwdtimes(1);
			// }
			// }
			// sysuser.setFindPwddate(today);
			// boolean result=baseBiz.updateObj(sysuser);

			String phoneword = sb.toString();

			// 获取当前登录企业的admin用户信息
			conditionMap.clear();
			WgMsgConfigBiz wgMsgConfigBiz = new WgMsgConfigBiz();
			ReviewRemindBiz rrBiz = new ReviewRemindBiz();
			// ********通过admin 查询通道号*********
			LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
			conditionMap1.put("userName", "admin");// admin用户
			conditionMap1.put("corpCode", corpCode);// 企业编码
			List<LfSysuser> AdminLfSysuerList = baseBiz.findListByCondition(LfSysuser.class, conditionMap1, null);
			LfSysuser AdminUser = null;
			if(AdminLfSysuerList != null && AdminLfSysuerList.size() > 0)
			{
				AdminUser = AdminLfSysuerList.get(0);
			}
			else
			{
				response.getWriter().print("noUserdSpuser");
				return;
			}
			// 获取sp账号
			List<Userdata> spUserList = new SmsBiz().getSpUserList(AdminUser);
			// ********通过admin 查询通道号**end*******

			SubnoManagerBiz subnoManagerBiz = new SubnoManagerBiz();
			// spuserlist为空说明没有可用的账号
			// 验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
			// 发送账号
			// isUsable 1通过 2失败 没有绑定一条上下行路由/下行路由 3失败 发送账号没有绑定路由 4
			// 该发送账号的全通道号有一个超过20位
			String isUsable = "";
			String sp = "";
			if(spUserList != null && spUserList.size() > 0)
			{
				for (Userdata spUser : spUserList)
				{

					isUsable = subnoManagerBiz.checkPortUsed(spUser.getUserId(), "");
					if("1".equals(isUsable))
					{
						sp = spUser.getUserId();
						break;
					}
				}
			}
			else
			{
				response.getWriter().print("noUserdSpuser");
				return;
			}

			String returnMsg = "fail";
			if(!"".equals(sp) && sp != null)
			{
				// 拿到当前系统配置的号段
				String[] haoduans = wgMsgConfigBiz.getHaoduan();
				// 过滤号段
				PhoneUtil phoneUtil = new PhoneUtil();
				if(phoneUtil.checkMobile(moblie, haoduans) == 1)
				{
					// 调用发送方法
					Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);

					String msg = "用户名" + autoUserName + "的新密码为" + phoneword;
					String isReturn = rrBiz.sendAutoPassMsgByOne(sp, msg, moblie, sysuser.getUserCode(), taskId);
					// 修改密码并且设置其密码强制状态
					if("1".equals(isReturn) && selfHelpModifyPwdChoice(sysuser, phoneword))
					{
						returnMsg = "success";
					}
					else
					{
						// spLog.logFailureString(opUser, opModule, oppType,
						// opContent+opSper, null,sysuser.getCorpCode());
					}
				}
			}
			else
			{
				response.getWriter().print("noUserdSpuser");
				return;
			}
			response.getWriter().print(returnMsg);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下发短信密码异常！");
		}

	}

	/**
	 * 登录时获取动态口令的方法
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getPhoneWord(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		SmsBiz smsbiz = new SmsBiz();
		SubnoManagerBiz subnoManagerBiz = new SubnoManagerBiz();
		WgMsgConfigBiz wgMsgConfigBiz = new WgMsgConfigBiz();
		ReviewRemindBiz rrBiz = new ReviewRemindBiz();
		BalanceLogBiz balanceBiz = new BalanceLogBiz();

		// 生成一个4位的随机数做为动态口令
		int count = 4;
		StringBuffer sb = new StringBuffer();
		String str = "0123456789";
		Random r = new Random();
		for (int i = 0; i < count; i++)
		{
			int num = r.nextInt(str.length());
			sb.append(str.charAt(num));
			str = str.replace((str.charAt(num) + ""), "");
		}

		String phoneword = sb.toString();

		// 获取当前登录用户的信息
		String username = request.getParameter("username");
		String enpCode = request.getParameter("enpCode");
		AuthenAtom authen = new AuthenAtom();
		// 请求参数进行sql注入校验
		if(!authen.holesProcessing(username) || !authen.holesProcessing(enpCode))
		{
			// 未查找到当前登录用户信息
			out.print("Nosysuser");
			EmpExecutionContext.error("登录动态口令，传入异常参数，username:"+username+"，enpCode:"+enpCode);
			return;
		}
		
		List<LfSysuser> lfSysuserList;
		try
		{
			// 根据username与corpcode获取当前登录用户的数据库信息
			LoginBiz loginBiz = new LoginBiz();
			lfSysuserList = loginBiz.getLfSysUserByUP(username.toUpperCase(), null, enpCode);
			LfSysuser sysuser = null;
			if(lfSysuserList != null && lfSysuserList.size() > 0)
			{

				sysuser = lfSysuserList.get(0);

				// 获取当前登录用户手机号码
				String phone = sysuser.getMobile();
				String corpCode = sysuser.getCorpCode();
				// 发送短信内容
				String msg = "您正在登录企业移动信息平台，手机动态口令为：" + phoneword.toString();
				//动态从数据库取出提示语
				LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
				condition.put("corpCode", corpCode);// 当前登录用户的企业编码
				condition.put("paramKey", "pwd.dynpwd");
				List<LfCorpConf> confList = baseBiz.getByCondition(LfCorpConf.class, condition, null);
				if(confList!=null&&confList.size()>0){
					String value= confList.get(0).getParamValue();
					if(value!=null){
						if(value.indexOf("#P_1#")>-1){
							msg=value.replace("#P_1#", phoneword.toString());
						}else if(value.indexOf("#p_1#")>-1){
							msg=value.replace("#p_1#", phoneword.toString());
						}
					}
					
				}
				
				if(phone != null && !"".equals(phone))
				{
					// 获取当前登录用户企业编码

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
					else
					{
						AdminUser = sysuser;
					}
					// sp账号
					String sp = "";
					String isUsable = "";
					ErrorCodeParam errorCode = new ErrorCodeParam();
					// 扩展尾号
					String subno = GlobalVariableBiz.getInstance().getValidSubno(StaticValue.VERIFYREMIND_MENUCODE, 0, corpCode, errorCode);
					// 获取sp账号(根据admin用户绑定的)
					List<Userdata> spUserList = smsbiz.getSpUserList(AdminUser);
					// spuserlist为空说明没有可用的账号
					// 验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
					// 发送账号
					// isUsable 1通过 2失败 没有绑定一条上下行路由/下行路由 3失败 发送账号没有绑定路由 4
					// 该发送账号的全通道号有一个超过20位
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
						PhoneUtil phoneUtil = new PhoneUtil();
						if(phoneUtil.getPhoneType(phone, haoduans) != -1)
						{
							// 是否计费
							boolean flag = SystemGlobals.isDepBilling(corpCode);
							int messageCount = 1;// 发送条数
							Message lfImMessage = new Message();
							lfImMessage.setContent(msg);
							lfImMessage.setMenuCode(StaticValue.LOGINSENDMESSAGE_MENUCODE);// 发动态口令模块id:12002
							lfImMessage.setTelPhoneNo(phone);
							String Rptflag = "1";// 是否需要返回状态报告
							// 调用发送方法
							String isReturn = rrBiz.LoginSendMsgOneByOne(null, lfImMessage, corpCode, sp, subno, balanceBiz, messageCount, AdminUser, flag, Rptflag);
							if(isReturn.equals("2"))
							{
								// 发送手机动态口令到网关失败
								out.print("sendfalse");
								return;
							}
							else if(isReturn.equals("6") || isReturn.equals("9"))
							{
								// 开启了记费，但短信余额不足
								out.print("NoMoney");
								return;
							}
							else if(isReturn.equals("7"))
							{
								// 扣费失败
								out.print("Moneyerror");
								return;
							}
							else if(isReturn.contains("1,"))
							{
								// 发送到网关，网关返回的msgid用于获取状态报告
								String ptMsgId = isReturn.split(",")[1];
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Timestamp createtime = new Timestamp(System.currentTimeMillis());

								LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
								LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
								conditionMap.put("userId", sysuser.getUserId().toString());
								List<LfDynPhoneWord> PhoneWordList = baseBiz.getByCondition(LfDynPhoneWord.class, conditionMap, null);
								// 当前登录用户原来已获取过动态口令，则只需要修改
								if(PhoneWordList != null && PhoneWordList.size() > 0)
								{
									objectMap.put("phoneWord", phoneword);
									objectMap.put("createTime", format.format(createtime.getTime()));
									objectMap.put("isLogin", "0");
									objectMap.put("ptMsgId", ptMsgId);
									objectMap.put("errorCode", " ");
									baseBiz.update(LfDynPhoneWord.class, objectMap, conditionMap);
								}
								else
								{
									LfDynPhoneWord PWord = new LfDynPhoneWord();
									// 存入数据库中lf_dynphoneword
									PWord.setErrorCode(" ");
									PWord.setUserId(sysuser.getUserId());
									PWord.setPhoneWord(phoneword);
									PWord.setCreateTime(Timestamp.valueOf(format.format(createtime.getTime())));
									PWord.setIsLogin(0);
									PWord.setPtMsgId(ptMsgId);
									baseBiz.addObj(PWord);
								}
								// 发送手机动态口令到网关成功
								out.print("true");
							}
							else
							{
								out.print("error");
								return;
							}
						}
						else
						{
							// 手机号码错误
							out.print("errorphone");
							return;
						}
					}
					else
					{
						// 没有可用的发送账户则不发
						out.print("Nospid");
						return;
					}

				}
				else
				{
					// 当前登录用户未填写手机号码
					out.print("NouserPhone");
					return;
				}
			}
			else
			{
				// 未查找到当前登录用户信息
				out.print("Nosysuser");
				return;
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "登录时获取动态口令异常。");
		}
	}

	/**
	 * 防止作弊行为（防止页面上修改数据把密码发送给任意一个人）
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String preventCheating(HttpServletRequest request, HttpServletResponse response, String autoUserName, String autoName, String phone) throws ServletException, IOException
	{
		String cheating = "";
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LfSysuser sysuser = null;
		try
		{
			conditionMap.put("userName", autoUserName);
			conditionMap.put("name", autoName);
			List<LfSysuser> userLIst = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
			if(userLIst != null && userLIst.size() > 0)
			{
				sysuser = userLIst.get(0);
				// 2013-12-18 去掉密码接收人的逻辑，改为当前人号码
				// Long depId = sysuser.getDepId();
				// conditionMap.clear();
				// conditionMap.put("depid",String.valueOf(depId));
				// List<LfDeppwdReceiver> receiverList =
				// baseBiz.getByCondition(LfDeppwdReceiver.class, conditionMap,
				// null);
				// if(receiverList != null && receiverList.size()>0){
				// String idstr = "";
				// for(int i =0 ;i<receiverList.size();i++){
				// LfDeppwdReceiver receiver = receiverList.get(i);
				// idstr = idstr + receiver.getUserid() + ",";
				// }
				//					
				// if(idstr != null && !"".equals(idstr.toString())){
				// idstr = idstr.substring(0,idstr.length()-1);
				// }
				// conditionMap.clear();
				// conditionMap.put("userId&in", idstr);
				// List<LfSysuser> userList =
				// baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
				// LinkedHashMap<Long, LfSysuser> nameMap = new
				// LinkedHashMap<Long, LfSysuser>();
				// if(userList != null && userList.size() > 0){
				// for(LfSysuser temp:userList){
				// nameMap.put(temp.getUserId(), temp);
				// }
				// }
				// boolean flag=false;
				// for(int i =0 ;i<receiverList.size();i++){
				// LfDeppwdReceiver receiver = receiverList.get(i);
				// LfSysuser a = null;
				// a = nameMap.get(receiver.getUserid());
				// String moblie = a.getMobile();
				// if(!"".equals(moblie)){
				// if(phone.equals(moblie)){
				// flag=true;
				//
				// }
				// }
				// }
				// if(!flag){
				// cheating="cheating";
				// }
				// }else{
				String moblie = sysuser.getMobile();
				if(!"".equals(moblie))
				{
					if(!phone.equals(moblie))
					{
						cheating = "cheating";
					}
				}
				else
				{
					cheating = "cheating";
				}
				// }
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "姓名获取其手机号码异常！ ");
		}
		return cheating;
	}

	/**
	 * 自助重置密码
	 * 修改用户的密码以及用户的密码状态
	 * 
	 * @param sysuser
	 *        需修改的对象
	 * @param pass
	 *        所修改的密码
	 * @param pass
	 *        密码状态标志 1是不强制 2
	 * @return
	 * @throws Exception
	 */
	public boolean selfHelpModifyPwdChoice(LfSysuser sysuser, String pass) throws Exception
	{
		boolean result = false;
		try
		{
			 sysuser.setPassword(
			 MD5.getMD5Str(pass+sysuser.getUserName().toLowerCase()));

//			sysuser.setPassword(MD5.getMD5Str(pass));

			sysuser.setPwdupdatetime(new Timestamp(System.currentTimeMillis()));
			// 将用户解锁
			sysuser.setUserState(1);
			// 将密码错误次数清零
			sysuser.setPwderrortimes(0);
			result = baseBiz.updateObj(sysuser);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "自助重置密码异常！");
			result = false;

		}
		return result;
	}
}
