package com.montnets.emp.engine.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.engine.bean.ZnyqParamValue;
import com.montnets.emp.engine.biz.BaseAppBiz;
import com.montnets.emp.engine.biz.FetchMsgTimerTaskBiz;
import com.montnets.emp.engine.biz.FetchSendMsgBiz;
import com.montnets.emp.engine.biz.ServiceBiz;
import com.montnets.emp.engine.vo.LfServiceVo;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

@SuppressWarnings("serial")
public class eng_mtServiceSvt extends BaseServlet
{

	//模块
	private final String opModule = "智能引擎";
	//操作员
	private final String opSper = StaticValue.OPSPER;
	private final SuperOpLog spLog = new SuperOpLog();
	private final BaseBiz baseBiz = new BaseBiz();
	private final String empRoot = "znyq";
	//模块名称
	private final String moduleName = "下行业务管理";
	
	
	public void find(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//业务名
		String serName = request.getParameter("serName");
		String sOpName = request.getParameter("sOpName");
		String serState = request.getParameter("serState");
		//创建人姓名
		String ownerName = request.getParameter("ownerName");
		
		try
		{
			// 当前登录操作员id
			//String lguserid = request.getParameter("lguserid");
			//String lgcorpcode = request.getParameter("lgcorpcode");
			//String lgusername = request.getParameter("lgusername");
			PageInfo pageInfo = new PageInfo();
			//设置分页
			boolean isFirstEnter = pageSet(pageInfo,request);
			
			//查询条件封装对象
			LfServiceVo lsv = new LfServiceVo();
			
			if (!isFirstEnter)
			{
				if (serName != null && !"".equals(serName))
				{
					lsv.setSerName(serName.trim());
				}
				if (sOpName != null && !"".equals(sOpName))
				{
					lsv.setName(sOpName.trim());
				}
				if (serState != null && !"".equals(serState))
				{
					lsv.setRunState(Integer.parseInt(serState.trim()));
				}

				if (ownerName != null && !"".equals(ownerName))
				{
					lsv.setOwnerName(ownerName.trim());
				}
			}
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务查询，session获取当前登录操作员对象为空。");
				return;
			}
			//企业编码
			lsv.setCorpCode(curUser.getCorpCode());
			//下行业务
			lsv.setSerType(2);
			BaseAppBiz serBiz = new BaseAppBiz();
			//查询服务里列表
			List<LfServiceVo> serList = serBiz.getServiceVos(lsv, curUser.getUserId(), pageInfo);
			
			//加密参数
			encryptionParam(serList, request);

			request.setAttribute("LfSerVo", lsv);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("serList", serList);
			
			String content = "下行业务管理查询。" 
				+"条件serName="+serName
				+",sOpName="+sOpName
				+",serState="+serState
				+",ownerName="+ownerName
				+",结果数量："+(serList==null?null:serList.size());
		
			EmpExecutionContext.info(opModule, curUser.getCorpCode(), curUser.getUserId().toString(), curUser.getUserName(), content, StaticValue.GET);
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务svt的find方法异常。");
		}
		finally{
			request.getRequestDispatcher(this.empRoot + "/engine/eng_mtService.jsp")
					.forward(request, response);
		}
	}
	
	/**
	 * 
	 * @description 加密参数
	 * @param serList 页面显示列表数据对象
	 * @param request 请求对象
	 */
	private void encryptionParam(List<LfServiceVo> serList, HttpServletRequest request)
	{
		try
		{
			if(serList == null || serList.size() < 1)
			{
				return;
			}
			//从session中获取加密解密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密解密对象不能为null
			if(encryptOrDecrypt == null)
			{
				EmpExecutionContext.error("查询下行业务管理列表，从session中获取加密对象为空。");
				return;
			}
			
			String keyId;
			//遍历操作员列表，对ID进行加密
			for(LfServiceVo service : serList)
			{
				keyId = encryptOrDecrypt.encrypt(String.valueOf(service.getSerId()));
				service.setSerIdCipher(keyId);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("查询下行业务管理列表，加密参数，异常。");
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
				EmpExecutionContext.error("从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "从session获取加密对象异常。");
			return null;
		}
	}

	/**
	 * 从session获取当前登录操作员对象
	 * @param request 请求对象
	 * @return 返回当前登录操作员对象，为空则返回null
	 */
	private LfSysuser getCurUserInSession(HttpServletRequest request)
	{
		Object loginSysuserObj = request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
		if(loginSysuserObj == null)
		{
			return null;
		}
		return (LfSysuser)loginSysuserObj;
	}
	
	public void toAdd(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			LfService service = new LfService();
			//业务id
			String serIdCipher = request.getParameter("serId");
			//根据id获取下行业务
			if(serIdCipher != null && !"".equals(serIdCipher))
			{
				//解密，获取明文业务id
				String serId = decryptionParam(serIdCipher, request);
				//解密失败，返回了null
				if(serId == null)
				{
					EmpExecutionContext.error("下行业务跳转到新建，解密serId失败。密文：" + serIdCipher);
					return;
				}
				service = baseBiz.getById(LfService.class, serId);
			}
			
			if(request.getParameter("prId") != null && !"".equals(request.getParameter("prId")))
			{
				request.setAttribute("prId", request.getParameter("prId"));
			}
			
			if(request.getParameter("repPrId") != null && !"".equals(request.getParameter("repPrId")))
			{
				request.setAttribute("repPrId", request.getParameter("repPrId"));
			}
			
			request.setAttribute("service", service);
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务跳转到新建，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			Long lguserid = curUser.getUserId();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			
			/*// 当前登录操作员id
			Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");*/
			// 当前登录操作员对象
			//LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			
			SysuserBiz sysBiz = new SysuserBiz();
			//获取当前操作员管辖范围的所有操作员列表
			List<LfSysuser> userList = sysBiz.getAllSysusers(lguserid);

			request.setAttribute("sysuserList", userList);
			request.setAttribute("curSysuser", curUser);

			LinkedHashMap<String, String> conp = new LinkedHashMap<String, String>();
			conp.put("corpCode&in", "0," + lgcorpcode);
			
			//设置启用查询条件
			conp.put("state", "0");
			//设置查询手动和手动+触发
			conp.put("busType&in", "0,2");
			
			//根据企业编码查询出所有的业务类型
			List<LfBusManager> busList = baseBiz.getByCondition(
					LfBusManager.class, conp, null);
			request.setAttribute("busList", busList);
			
			SmsBiz smsBiz = new SmsBiz();
			//根据操作员权限查找当前操作员有权限的sp账号
			List<Userdata> spUserList = smsBiz.getSpUserList(curUser);
			request.setAttribute("spUserList", spUserList);
			
		} catch (Exception e)
		{
			//保存和打印错误信息
			EmpExecutionContext.error(e, "下行业务的toAdd方法异常。");
		}finally{
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_addMt1.jsp").forward(
					request, response);
		}
	}

	public void toEdit(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			//从session获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务修改，session获取当前登录操作员对象为空。");
				return;
			}
			
			//业务id密文
			String serIdCipher = request.getParameter("serId");
			
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务删除，解密serId失败。密文：" + serIdCipher);
				return;
			}
			
			ServiceBiz serBiz = new ServiceBiz();
			//根据id获取下行业务
			LfService service = serBiz.getService(serId, curUser.getCorpCode());
			
			SysuserBiz sysBiz = new SysuserBiz();
			//获取又权限的操作员
			List<LfSysuser> userList = sysBiz.getAllSysusers(curUser.getUserId());

			request.setAttribute("sysuserList", userList);
			request.setAttribute("curSysuser", curUser);
			request.setAttribute("lgcorpcode", curUser.getCorpCode());
			request.setAttribute("service", service);

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0," + curUser.getCorpCode());
			
			//设置启用查询条件
			conditionMap.put("state", "0");
			//设置查询手动和手动+触发
			conditionMap.put("busType&in", "0,2");
			
			//根据企业编码获取业务类型
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, null);
			if (busList == null || busList.size() == 0)
			{
				busList = new ArrayList<LfBusManager>();
				conditionMap.put("busCode", service.getBusCode());
				busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, null);
			}
			request.setAttribute("busList", busList);

			SmsBiz smsBiz = new SmsBiz();
			//获取发送账号
			List<Userdata> spUserList = smsBiz.getSpUserList(curUser);
			boolean flag =false ;
			for (int i = 0; i <spUserList.size(); i++)
			{
				if(spUserList.get(i).getUserId().equals(service.getSpUser()))
				{
					flag = true;
					break;
				}
			}
			if(!flag)
			{
				conditionMap.clear();
				conditionMap.put("userId", service.getSpUser());
				List<Userdata> spUserListTemp =baseBiz.getByCondition(Userdata.class, conditionMap, null);
				if(spUserListTemp!=null && spUserListTemp.size()>0)
				{
					request.setAttribute("staffName", spUserListTemp.get(0).getStaffName());
				}
			}
			request.setAttribute("sendUserList", spUserList);
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务修改，异常。");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot + "/engine/eng_editMtService.jsp")
					.forward(request, response);
		}
	}
	
	/**
	 * 
	 * @description 解密参数
	 * @param paramCipher 参数密文
	 * @param request 请求对象
	 * @return 参数明文
	 */
	private String decryptionParam(String paramCipher, HttpServletRequest request)
	{
		if(paramCipher == null || paramCipher.length() < 1)
		{
			EmpExecutionContext.error("下行业务管理列表，传入的参数密文为空。密文："+paramCipher);
			return null;
		}
		//加密解密对象
		ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
		//加密解密对象不能为null
		if(encryptOrDecrypt == null)
		{
			EmpExecutionContext.error("下行业务管理列表，从session中获取加密对象为空。密文："+paramCipher);
			return null;
		}
		
		//解密
		String param = encryptOrDecrypt.decrypt(paramCipher);
		return param;
	}

	public void toAddTrigger(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			//从session获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务执行时间跳转到修改，session获取当前登录操作员对象为空。");
				return;
			}
			
			//业务id密文
			String serIdCipher = request.getParameter("serId");
			
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务执行时间，解密serId失败。密文：" + serIdCipher);
				return;
			}
			
			ServiceBiz serBiz = new ServiceBiz();
			//根据id的获取服务
			LfService service = serBiz.getService(serId, curUser.getCorpCode());
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("taskExpression", "ser|" + serId);
			//根据id查询出定时信息
			List<LfTimer> timerList = baseBiz.getByCondition(LfTimer.class, conditionMap, null);
			LfTimer timer = null;
			if (timerList != null && timerList.size() > 0)
			{
				timer = timerList.get(0);
			}
			//获取时间间隔
			if (timer != null && timer.getCustomInterval() != null)
			{
				Long hours = (timer.getCustomInterval() / 1000L) / 3600;
				Long minites = (timer.getCustomInterval() / 1000L - hours * 3600L) / 60;

				request.setAttribute("hours", hours);
				request.setAttribute("minites", minites);
			}
			
			request.setAttribute("curSysuser", curUser);
			request.setAttribute("lgcorpcode", curUser.getCorpCode());
			request.setAttribute("service", service);
			request.setAttribute("timer", timer);
		} 
		catch (Exception e)
		{
			//输出和打印错误信息
			EmpExecutionContext.error(e, "下行业务执行时间跳转到修改，异常。");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot + "/engine/eng_addTrigger.jsp")
					.forward(request, response);
		}
	}
	
	public void toAddTime(HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			//下行业务id密文
			String serIdCipher = request.getParameter("serId");
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务跳转到设置定时，解密serId失败。密文：" + serIdCipher);
				return;
			}
			//根据id的获取服务
			LfService service = baseBiz.getById(LfService.class, serId);
			
			if(request.getParameter("repPrId") != null && !"".equals(request.getParameter("repPrId")))
			{
				request.setAttribute("repPrId", request.getParameter("repPrId"));
			}
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务跳转到时间新建，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			//Long lguserid = curUser.getUserId();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			/*// 当前登录操作员id
			Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");*/
			//获取当前操作员对象
			//LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			request.setAttribute("curSysuser", curUser);
			request.setAttribute("lgcorpcode", lgcorpcode);
			request.setAttribute("service", service);
			//request.setAttribute("timer", timer);
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_addMt4.jsp").forward(
					request, response);
		} catch (Exception e)
		{
			//输出和打印错误信息
			EmpExecutionContext.error(e, "下行业务的toAddTime方法异常。");
		}
	}

	public void addTime(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		try
		{
			//服务id密文
			String serIdCipher = request.getParameter("serId");
			//发送时间
			String sendTime = request.getParameter("sendtime");
			//执行频率。0-一次性；1-每天；4-每周；2-每月
			String runway = request.getParameter("runway");
			//有效期-小时
			String hours = request.getParameter("hours");
			//有效期-分钟
			String minites = request.getParameter("minites");

			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			
			if(serIdCipher == null || serIdCipher.length() == 0 || sendTime == null || sendTime.length() == 0 || runway == null || runway.length() == 0){
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("addserResult", "0");
				EmpExecutionContext.error("下行业务的addTime方法异常，获取不到页面参数。"
						+ "serId：" + serIdCipher
						+ ",sendTime：" + sendTime
						+ ",runway：" + runway);
				return;
			}
			
			//执行间隔，0为只执行一次
			Integer runInterval = null;
			//执行间隔单位。1-天；2-月 ;
			Integer intervalUnit = null;
			//执行频率为每月，则单位设置为2-月
			if("2".equals(runway.trim())){
				intervalUnit = 2;
				//执行间隔设置为1，即每月执行一次
				runInterval = 1;
			}
			//执行频率为每周，则单位设置为1-天
			else if("4".equals(runway.trim())){
				intervalUnit = 1;
				//执行间隔设置为7，即每7天执行一次
				runInterval = 7;
			}
			//执行频率为每天，则单位设置为1-天
			else if("1".equals(runway.trim())){
				intervalUnit = 1;
				//执行间隔设置为1，即每天执行一次
				runInterval = 1;
			}else{
				intervalUnit = 1;
				//执行间隔设置为0，即只执行一次
				runInterval = 0;
			}
			
			Long tmpTime = 0L;
			if (hours != null && !"".equals(hours))
			{
				Integer tempHours = Integer.valueOf(hours);
				tmpTime = tempHours * 3600 * 1000L;
			}
			if (minites != null && !"".equals(minites))
			{
				Integer tempMinites = Integer.valueOf(minites);
				tmpTime = tmpTime + tempMinites * 60 * 1000L;
			}
			//设置有效期
			tmpTime = (tmpTime == 0L) ? 1 * 3600 * 1000L : tmpTime;
			
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务跳转到新建reply步骤，解密serId失败。密文：" + serIdCipher);
				return;
			}
			
			//获取业务对象
			LfService lfService = baseBiz.getById(LfService.class, serId);
			String taskName = lfService.getSerName();
			
			//格式化日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			// 1-表示需要重发；tmpTime-表示有效期
			FetchMsgTimerTaskBiz sendSmsTimerTask = new FetchMsgTimerTaskBiz(
					taskName, lfService.getRunState(), sdf.parse(sendTime
							+ ":59"), runInterval, "ser|"
							+ serId, tmpTime, 1, intervalUnit);
			
			TaskManagerBiz tm = new TaskManagerBiz();
			//设置定时任务
			boolean result = tm.setJob(sendSmsTimerTask);
			
			String opType = StaticValue.ADD;
			//操作日志内容
			String opContent = "设置（任务名：" + taskName + "）发送时间";
			LfSysuser sysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			String opUser = sysuser.getUserName();
			//操作结果
			String opResult = "";
			if (result)
			{
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("addserResult", "1");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				opResult = "设置发送时间成功。";
			} else
			{
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("addserResult", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
				opResult = "设置发送时间失败。";
			}
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务id，开始时间，发送频率，有效期](").append(serId).append("，").append(sendTime).append("，")
			.append(runway).append("，").append("".equals(hours)?"0":hours).append("小时").append("".equals(minites)?"0":minites).append("分钟").append(")");
			//操作员
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, lguserid, opUser, opResult + content.toString(), opType);
			
		} catch (Exception e)
		{
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("addserResult", "0");
			EmpExecutionContext.error(e, "下行业务的addTime方法异常。");
		} finally
		{
			//通过find方法跳转
			response.sendRedirect("eng_mtService.htm?method=find&lguserid="+request.getParameter("lguserid"));
		}
	}
	
	public void getUserDataList(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		//获取sp发送账号
		WgMsgConfigBiz wb = new WgMsgConfigBiz();

		if (request.getParameter("ownerUser") != null
				&& !"".equals(request.getParameter("ownerUser")))
		{
			StringBuilder sb = new StringBuilder();
			sb.append("");
			try
			{
				//获取发送账号信息列表
				List<Userdata> ulist = wb.getAllRoutUserdata();
				String accId = request.getParameter("accId");
				String selected = "";
				if (ulist != null && ulist.size() > 0)
				{
					for (Userdata ud : ulist)
					{
						if (accId != null && !"".equals(accId)
								&& ud.getUserId().equals(accId))
						{
							selected = "selected";
						}
						//将sp账号信息拼成html代码
						sb
								.append("<option value='" + ud.getUserId()
										+ "' " + selected + ">"
										+ ud.getUserId() + "</option>");
						selected = "";
					}
				}
				//异步返回结果
				response.getWriter().print(sb);
			} catch (Exception e)
			{
				response.getWriter().print("error");
				//打印和保存异常信息
				EmpExecutionContext.error(e, "下行业务的getUserDataList方法异常。");
			}
			return;
		}
	}

	public void add(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		//修改前的业务名称
		String oldSerName = "";
		//修改前的业务类型
		String oldBusCode = "";
		//修改前的SP账号
		String oldSpUser = "";
		//修改前的尾号状态
		String oldIdentifyMode = "";
		//业务id密文
		String strSerIdCipher = request.getParameter("serId");
		LfService service = new LfService();
		try {
			if(strSerIdCipher != null && !"".equals(strSerIdCipher))
			{
				//解密，获取明文业务id
				String strSerId = decryptionParam(strSerIdCipher, request);
				//解密失败，返回了null
				if(strSerId == null)
				{
					EmpExecutionContext.error("下行业务新建，解密serId失败。密文：" + strSerIdCipher);
					return;
				}
				service = baseBiz.getById(LfService.class, strSerId);
			}
			//获取修改前参数
			if(service != null)
			{
				oldSerName = service.getSerName();
				oldBusCode = service.getBusCode();
				oldSpUser = service.getSpUser();
				oldIdentifyMode = service.getIdentifyMode()==null?null:String.valueOf(service.getIdentifyMode());
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取下行业务信息异常！");
		}
		
		//描述
		String comments = request.getParameter("comments");
		//发送账号
		String spUser = request.getParameter("spUser");
		//创建人
		String owner = request.getParameter("ownerList");
		//业务名
		String serName = request.getParameter("serName");
		//运行状态
		Integer runState = Integer.parseInt(request.getParameter("runState"));
		//业务编码
		String busCode = request.getParameter("busCode");
		//识别模式。1-使用尾号；
		String identifyMode = request.getParameter("identifyMode");
		//设置描述
		service.setCommnets(comments);
		//设置业务名
		service.setSerName(serName);
		//设置sp账号
		service.setSpUser(spUser);
		//设置运行状态
		service.setRunState(runState);
		//设置业务类型
		service.setSerType(2);
		//设置拥有者的id
		service.setOwnerId(Long.valueOf(owner));
		//设置业务编码
		service.setBusCode(busCode);
		service.setIdentifyMode(identifyMode==null?null:Integer.valueOf(identifyMode));
		
		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("下行业务新建，session获取当前登录操作员对象为空。");
			return;
		}
		// 当前登录操作员id
		Long lguserid = curUser.getUserId();
		// 当前登录企业
		String lgcorpcode = curUser.getCorpCode();
		/*// 当前登录操作员id
		Long lguserid = Long.valueOf(request.getParameter("lguserid"));
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");*/
		//操作日志类型
		String opType = "";
		//操作日志内容
		String opContent = "";
		LfSysuser sysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
		String opUser = sysuser.getUserName();
		try
		{
			//设置操作员id
			service.setUserId(lguserid);
			//设置企业编码
			service.setCorpCode(lgcorpcode);
			//操作结果
			String opResult = "";
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务ID，业务名称，业务类型，SP账号，尾号状态](");
			if(service.getSerId() == null)
			{
				//执行新增操作
				Long serId = baseBiz.addObjReturnId(service);
				service.setSerId(serId);
				//操作日志类型
				opType = StaticValue.ADD;
				//操作日志内容
				opContent = "新建下行业务（业务名称：" + service.getSerName() + "）";
				//操作日志信息
				content.append(service.getSerId()).append("，").append(service.getSerName()).append("，").append(service.getBusCode()).append("，").append(service.getSpUser())
				.append("，").append(service.getIdentifyMode()).append(")");
				if(serId !=null && serId > 0)
				{
					opResult = "新建下行业务成功。";
				}
				else
				{
					opResult = "新建下行业务失败。";
				}
			}
			else
			{
				service.setBusId(null);
				//执行修改操作
				boolean result = baseBiz.updateObj(service);
				//操作日志类型
				opType = StaticValue.UPDATE;
				//操作日志内容
				opContent = "修改下行业务（业务名称：" + service.getSerName() + "）";
				//操作日志信息
				content.append(service.getSerId()).append("，").append(oldSerName).append("，").append(oldBusCode).append("，").append(oldSpUser)
				.append("，").append(oldIdentifyMode).append(")-->(")
				.append(service.getSerId()).append("，").append(service.getSerName()).append("，").append(service.getBusCode()).append("，")
				.append(service.getSpUser()).append("，").append(service.getIdentifyMode()).append(")");
				if(result)
				{
					opResult = "修改下行业务成功。";
				}
				else
				{
					opResult = "修改下行业务失败。";
				}
			}
			
			//从session中获取加密解密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密解密对象不能为null
			if(encryptOrDecrypt == null)
			{
				EmpExecutionContext.error("下行业务新建，从session中获取加密对象为空。");
				return;
			}
			//业务id密文
			String serIdCipher = encryptOrDecrypt.encrypt(service.getSerId().toString());
			
			request.setAttribute("serId", serIdCipher);

			//成功新增，添加服务日志
			//request.setAttribute("mtSerResult", "1");
			
			spLog.logSuccessString(opUser, opModule, opType, opContent, lgcorpcode);
			
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, String.valueOf(lguserid), opUser, opResult + content.toString(), opType);
		} catch (Exception e)
		{
			//request.setAttribute("mtSerResult", "0");
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, null, lgcorpcode);
			EmpExecutionContext.error(e, "下行业务的add方法异常。");
		} finally
		{
			//从session中获取加密解密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//业务id密文
			String serIdCipher = "";
			//加密解密对象不能为null
			if(encryptOrDecrypt == null)
			{
				EmpExecutionContext.error("下行业务新建，从session中获取加密对象为空。");
			}else{
				//业务id密文
				serIdCipher = encryptOrDecrypt.encrypt(service.getSerId().toString());
			}
			
			
			//获取select步骤id
			String prId = request.getParameter("prId")==null?"":request.getParameter("prId");
			//获取reply步骤id
			String repPrId = request.getParameter("repPrId")==null?"":request.getParameter("repPrId");
			//request.getRequestDispatcher("eng_mtProcess.htm?method=toAddSelect&serId="+service.getSerId()).forward(request, response);
			response.sendRedirect("eng_mtProcess.htm?method=toAddSelect&serId="+serIdCipher+"&prId="+prId+"&repPrId="+repPrId+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode);
		}
	}

	public void edit(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		LfService oldService = new LfService();
		//修改前的业务名称
		String oldSerName = "";
		//修改前的业务类型
		String oldBusCode = "";
		//修改前的SP账号
		String oldSpUser = "";
		//修改前的尾号状态,默认为异常数字-99
		int oldIdentifyMode = -99;
		//业务id密文
		String strSerIdCipher = request.getParameter("serId");
		try {
			if(strSerIdCipher != null && !"".equals(strSerIdCipher))
			{
				//解密，获取明文业务id
				String strSerId = decryptionParam(strSerIdCipher, request);
				//解密失败，返回了null
				if(strSerId == null)
				{
					EmpExecutionContext.error("下行业务信息修改，解密serId失败。密文：" + strSerIdCipher);
					return;
				}
				
				oldService = baseBiz.getById(LfService.class, strSerId);
			}
			//获取修改前参数
			if(oldService != null)
			{
				oldSerName = oldService.getSerName();
				oldBusCode = oldService.getBusCode();
				oldSpUser = oldService.getSpUser();
				oldIdentifyMode = oldService.getIdentifyMode()==null?-99:oldService.getIdentifyMode();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取下行业务信息异常！");
		}
		LfService service = new LfService();
		//描述
		String comments = request.getParameter("comments");
		//sp账号
		String spUser = request.getParameter("spUser");
		//拥有者
		String owner = request.getParameter("ownerList");
		//业务名称
		String serName = request.getParameter("serName");
		//运行状态
		Integer runState = Integer.parseInt(request.getParameter("runState"));
		//创建时间
		long createTime = Long.parseLong(request.getParameter("createTime"));
		//业务id密文
		String serIdCipher = request.getParameter("serId");
		//解密，获取明文业务id
		String serId = decryptionParam(serIdCipher, request);
		//解密失败，返回了null
		if(serId == null)
		{
			EmpExecutionContext.error("下行业务信息修改，解密serId失败。密文：" + serIdCipher);
			return;
		}
		
		String busCode = request.getParameter("busCode");
		String identifyMode = request.getParameter("identifyMode");

		service.setCommnets(comments);
		service.setSerName(serName);
		service.setSpUser(spUser);
		service.setRunState(runState);
		service.setSerType(2);
		service.setOwnerId(Long.valueOf(owner));
		service.setSerId(Long.valueOf(serId));
		service.setCreateTime(new Timestamp(createTime));
		service.setBusCode(busCode);
		
		service.setIdentifyMode(identifyMode==null?null:Integer.valueOf(identifyMode));
		
		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("下行业务修改，session获取当前登录操作员对象为空。");
			return;
		}
		// 当前登录操作员id
		Long lguserid = curUser.getUserId();
		// 当前登录企业
		String lgcorpcode = curUser.getCorpCode();
		/*// 当前登录操作员id
		Long lguserid = Long.valueOf(request.getParameter("lguserid"));
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");*/
		//日志操作类型
		String opType = StaticValue.UPDATE;
		//日志操作内容
		String opContent = "修改下行业务（业务名称：" + service.getSerName() + "）";
		LfSysuser sysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
		String opUser = sysuser.getUserName();
		try
		{
			//设置业务所有者
			//service.setUserId(lguserid);
			//更新操作
			boolean result = baseBiz.updateObj(service);
			LfTimer timer = new LfTimer();
			FetchSendMsgBiz fetBiz = new FetchSendMsgBiz();
			//根据id获取定时信息
			timer = fetBiz.getTaskByExpression(serId);
			if (runState == 0 && timer != null && timer.getRunState() != 0)
			{
				//如果状态为停止运行，则停止定时器
				fetBiz.stopTaskByTaskId(timer.getTimerTaskId());
			} else if (runState == 1 && timer != null
					&& timer.getRunState() != 0)
			{
				//如果是运行中则开启定时器
				fetBiz.startTaskByTaskId(timer.getTimerTaskId());
			}
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务ID，业务名称，业务类型，SP账号，尾号状态](");
			//操作结果
			String opResult = "";
			if (result)
			{
				//成功记录日志信息
				request.setAttribute("mtSerResult", "1");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				
				opResult = "修改上行业务成功。";
					
			} else
			{
				//失败保存日志信息
				request.setAttribute("mtSerResult", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
				opResult = "修改上行业务失败。";
			}
			//操作日志信息
			content.append(service.getSerId()).append("，").append(oldSerName).append("，").append(oldBusCode).append("，").append(oldSpUser)
			.append("，").append(oldIdentifyMode).append(")-->(")
			.append(service.getSerId()).append("，").append(service.getSerName()).append("，").append(service.getBusCode()).append("，")
			.append(service.getSpUser()).append("，").append(service.getIdentifyMode()).append(")");		
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, String.valueOf(lguserid), opUser, opResult + content.toString(), opType);
			
			SysuserBiz sysBiz = new SysuserBiz();
			//获取有权限的所有操作员
			List<LfSysuser> userList = sysBiz.getAllSysusers(lguserid);

			request.setAttribute("sysuserList", userList);
			LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			request.setAttribute("curSysuser", curSysuser);
			request.setAttribute("service", service);
		} catch (Exception e)
		{
			request.setAttribute("mtSerResult", "0");
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, null, lgcorpcode);
			EmpExecutionContext.error(e, "下行业务的edit方法异常。");
		} finally
		{
			/*request.getRequestDispatcher(
					this.empRoot + "/engine/eng_editMtService.jsp").forward(
					request, response);*/
			this.toEdit(request, response);
		}
	}

	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		//从session获取当前登录操作员对象
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("删除下行业务，session获取当前登录操作员对象为空。");
			response.getWriter().print("false");
			return;
		}
		
		//业务名称
		String serName = request.getParameter("serName");
		//操作内容
		String opContent = "删除下行短信业务（业务名称：" + serName + "）";
		//业务id密文
		String serIdCipher = request.getParameter("serId");
		//解密，获取明文业务id
		String serId = null;
		try
		{
			//解密，获取明文业务id
			serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务删除，解密serId失败。密文：" + serIdCipher);
				response.getWriter().print("false");
				return;
			}
			BaseAppBiz baBiz = new BaseAppBiz();
			//删除操作
			int result = baBiz.deleteService(serId, curUser.getCorpCode());
			if (result > 0)
			{
				response.getWriter().print("true");
				
				//记录成功日志
				spLog.logSuccessString(curUser.getUserName(), opModule, StaticValue.DELETE, opContent, curUser.getCorpCode());
				
				TaskManagerBiz tmBiz = new TaskManagerBiz();
				tmBiz.delTaskByExpression("ser|" + serId);
				
				EmpExecutionContext.info("模块名称：下行业务管理，企业："+curUser.getCorpCode()+"，"
						+"操作员："+curUser.getUserId()+"["+curUser.getUserName()+"]，"
						+"删除(业务ID："+serId+"，业务业务名称："+serName+")成功。");
			} 
			else
			{
				response.getWriter().print("false");
				//记录失败日志
				spLog.logFailureString(curUser.getUserName(), opModule, StaticValue.DELETE, opContent, null, curUser.getCorpCode());
				
				EmpExecutionContext.info("模块名称：下行业务管理，企业："+curUser.getCorpCode()+"，"
						+"操作员："+curUser.getUserId()+"["+curUser.getUserName()+"]，"
						+"删除(业务ID："+serId+"，业务业务名称："+serName+")失败。");
			}
		} 
		catch (Exception e)
		{
			response.getWriter().print("false");
			//记录失败日志
			spLog.logFailureString(curUser.getUserName(), opModule, StaticValue.DELETE, opContent, e, curUser.getCorpCode());
			EmpExecutionContext.error(e, "删除下行业务，异常。serName="+serName+",serId="+serId+",corpCode"+curUser.getCorpCode());
		}
	}

	public void getRunState(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		//业务id密文
		String serIdCipher = request.getParameter("serId");
		try
		{
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务获取运行状态，解密serId失败。密文：" + serIdCipher);
				response.getWriter().print(ERROR);
				return;
			}
			
			//根据id获取业务
			LfService service = baseBiz.getById(LfService.class, serId);
			//输出业务运行状态
			response.getWriter().print(service.getRunState());
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			//保存异常信息
			EmpExecutionContext.error(e, "下行业务的getRunState方法异常。");
		}
	}

	public void updateStatus(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		//操作类型
		String opType = StaticValue.UPDATE;
		//服务id
		String serId = request.getParameter("serId");
		//运行状态
		String runState = request.getParameter("runState");
		String runStateName = runState.equals("0") ? "禁用" : "运行";
		//服务名称
		String serName = request.getParameter("serName");

		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
        //日志内容
		String opContent = "修改下行业务服务（业务名称：" + serName + "）的业务状态（状态为：" + runStateName
				+ "）";
		LfSysuser sysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
		String opUser = sysuser.getUserName();
		try
		{
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("runState", runState);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("serId", serId);
			//操作结果
			String opResult = "";
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务ID，状态](");
			//更新状态
			if (baseBiz.update(LfService.class, objectMap, conditionMap))
			{
				FetchSendMsgBiz fsmBiz = new FetchSendMsgBiz();
				TaskManagerBiz taskManagerBiz = new TaskManagerBiz();
				LfTimer timer = null;
				List<LfTimer> timerList = taskManagerBiz
						.getTaskByExpression("ser|" + serId);
				if (timerList != null && timerList.size() > 0)
				{
					timer = timerList.get(0);
				}
				// timer=baseBiz.getById(LfTimer.class, serId);
				//如果运行状态为停止
				if (runState.equals("0") && timer != null
						&& timer.getRunState() != 0)
				{
					//暂停定时器
					fsmBiz.pauseTaskByTaskId(timer.getTimerTaskId());
				} else if (runState.equals("1") && timer != null
						&& timer.getRunState() != 0)
				{
					//开启定时器
					fsmBiz.startTaskByTaskId(timer.getTimerTaskId());
				}
				response.getWriter().print(true);
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				opResult = "修改状态成功。";
			}
			else
			{
				opResult = "修改状态失败。";
			}
			//操作日志信息
			content.append(serId).append("，").append(runStateName).append(")");
			//操作员
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, lguserid, sysuser.getUserName(), opResult + content.toString(), opType);
		} catch (Exception e)
		{
			response.getWriter().print(false);
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);
			EmpExecutionContext.error(e, "下行业务的updateStatus方法异常。");
		}
	}

	public void addTrigger(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			//业务id密文
			String serIdCipher = request.getParameter("serId");
			//发送时间
			String sendTime = request.getParameter("sendtime");
			//执行频率。0-一次性；1-每天；4-每周；2-每月
			String runway = request.getParameter("runway");
			//有效期-小时
			String hours = request.getParameter("hours");
			//有效期-分钟
			String minites = request.getParameter("minites");

			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			
			if(serIdCipher == null || serIdCipher.length() == 0 || sendTime == null || sendTime.length() == 0 || runway == null || runway.length() == 0){
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("addserResult", "0");
				EmpExecutionContext.error("下行业务的addTime方法异常，获取不到页面参数。"
						+ "serId：" + serIdCipher
						+ ",sendTime：" + sendTime
						+ ",runway：" + runway);
				return;
			}
			
			//执行间隔，0为只执行一次
			Integer runInterval = null;
			//执行间隔单位。1-天；2-月 ;
			Integer intervalUnit = null;
			//执行频率为每月，则单位设置为2-月
			if("2".equals(runway.trim())){
				intervalUnit = 2;
				//执行间隔设置为1，即每月执行一次
				runInterval = 1;
			}
			//执行频率为每周，则单位设置为1-天
			else if("4".equals(runway.trim())){
				intervalUnit = 1;
				//执行间隔设置为7，即每7天执行一次
				runInterval = 7;
			}
			//执行频率为每天，则单位设置为1-天
			else if("1".equals(runway.trim())){
				intervalUnit = 1;
				//执行间隔设置为1，即每天执行一次
				runInterval = 1;
			}else{
				intervalUnit = 1;
				//执行间隔设置为0，即只执行一次
				runInterval = 0;
			}
			
			Long tmpTime = 0L;
			if (hours != null && !"".equals(hours))
			{
				Integer tempHours = Integer.valueOf(hours);
				tmpTime = tempHours * 3600 * 1000L;
			}
			if (minites != null && !"".equals(minites))
			{
				Integer tempMinites = Integer.valueOf(minites);
				tmpTime = tmpTime + tempMinites * 60 * 1000L;
			}
			//设置有效期
			tmpTime = (tmpTime == 0L) ? 1 * 3600 * 1000L : tmpTime;
			
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务步骤管理，解密serId失败。密文：" + serIdCipher);
				return;
			}
			
			//获取业务对象
			LfService lfService = baseBiz.getById(LfService.class, serId);
			String taskName = lfService.getSerName();
			
			//格式化日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			// 1-表示需要重发；tmpTime-表示有效期
			FetchMsgTimerTaskBiz sendSmsTimerTask = new FetchMsgTimerTaskBiz(
					taskName, lfService.getRunState(), sdf.parse(sendTime
							+ ":59"), runInterval, "ser|"
							+ serId, tmpTime, 1, intervalUnit);
			
			TaskManagerBiz tm = new TaskManagerBiz();
			//设置定时任务
			boolean result = tm.setJob(sendSmsTimerTask);
			
			String opType = StaticValue.ADD;
			//操作日志内容
			String opContent = "设置（任务名：" + taskName + "）发送时间";
			LfSysuser sysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			String opUser = sysuser.getUserName();
			//操作结果
			String opResult = "";
			if (result)
			{
				request.setAttribute("serviceResult", "3");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				opResult = "设置发送时间成功。";
			} else
			{
				request.setAttribute("serviceResult", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
				opResult = "设置发送时间失败。";
			}
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务id，开始时间，发送频率，有效期](").append(serId).append("，").append(sendTime).append("，")
			.append(runway).append("，").append("".equals(hours)?"0":hours).append("小时").append("".equals(minites)?"0":minites).append("分钟").append(")");
			//操作员
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, lguserid, opUser, opResult + content.toString(), opType);	
		} catch (Exception e)
		{
			response.getWriter().print("0");
			EmpExecutionContext.error(e, "下行业务的addTrigger方法异常。");
		} finally
		{
			//通过find方法跳转
			this.toAddTrigger(request, response);
		}
	}

	// 获取当前系统时间
	public void getServerTime(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		//格式化日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String serverTime = format.format(date);
		//异步输入时间
		response.getWriter().print(serverTime);
	}

}
