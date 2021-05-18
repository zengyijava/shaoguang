package com.montnets.emp.engine.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.TemplateBiz;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.engine.bean.ZnyqParamValue;
import com.montnets.emp.engine.biz.BaseAppBiz;
import com.montnets.emp.engine.biz.MoServiceBiz;
import com.montnets.emp.engine.biz.ProcessConfigBiz;
import com.montnets.emp.engine.biz.ServiceBiz;
import com.montnets.emp.engine.vo.LfServiceVo;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.datasource.LfDBConnect;
import com.montnets.emp.entity.engine.LfAutoreply;
import com.montnets.emp.entity.engine.LfProCon;
import com.montnets.emp.entity.engine.LfProcess;
import com.montnets.emp.entity.engine.LfReply;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.pasgroup.AcmdRoute;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

/**
 * 
 * 
 * @project emp
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime
 * @description
 * @description
 */
@SuppressWarnings("serial")
public class eng_moServiceSvt extends BaseServlet
{
	private final String empRoot = "znyq";
	//操作日志模块
	private final String opModule = "智能引擎";
	
	//操作员
	private final String opSper = StaticValue.OPSPER;

	private final MoServiceBiz serBiz = new MoServiceBiz();

	private final ProcessConfigBiz proBiz = new ProcessConfigBiz();

	private final BaseAppBiz bab = new BaseAppBiz();

	private final SmsBiz smsBiz = new SmsBiz();
	
	private final BaseBiz baseBiz = new BaseBiz();
	
	private final TemplateBiz templBiz = new TemplateBiz();
	
	private final String basePath = "/engine";
	
	//模块名称
	private final String moduleName = "上行业务管理";
 
	
	
	public void find(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PageInfo pageInfo = new PageInfo();
		try {
			pageSet(pageInfo,request);
			List<LfServiceVo> serList = new ArrayList<LfServiceVo>();
			//查询参数封装对象
			LfServiceVo lsv = new LfServiceVo();
			//业务名称
			String serName = request.getParameter("serName");
			//创建人姓名
			String sOpName = request.getParameter("sOpName");
			//状态
			String serState = request.getParameter("serState");
			
			String orderCode = request.getParameter("orderCode");

			String identifyMode = request.getParameter("identifyMode");
			
			lsv.setSerType(1);
			if (serName != null && !"".equals(serName))
			{
				//设置业务名
				lsv.setSerName(serName.trim());
			}
			if (sOpName != null && !"".equals(sOpName))
			{
				//设置操作员名
				lsv.setName(sOpName.trim());
			}
			if (serState != null && !"".equals(serState))
			{
				//设置运行状态
				lsv.setRunState(Integer.parseInt(serState.trim()));
			}
			if(orderCode != null && orderCode.length() > 0){
				lsv.setOrderCode(orderCode);
			}
			if(identifyMode != null && identifyMode.length() > 0){
				lsv.setIdentifyMode(Integer.valueOf(identifyMode));
			}
			// 当前登录操作员id
			//String lguserid = request.getParameter("lguserid");
			//String lgcorpcode = request.getParameter("lgcorpcode");
			//String lgusername = request.getParameter("lgusername");
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务查询，session获取当前登录操作员对象为空。");
				return;
			}
			//企业编码
			lsv.setCorpCode(curUser.getCorpCode());
			//获取上行业务对象
			serList = bab.getServiceVos(lsv, curUser.getUserId(), pageInfo);
			
			//加密参数
			encryptionParam(serList, request);
			
			request.setAttribute("LfSerVo", lsv);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("serList", serList);
			
			String content = "上行业务管理查询。" 
					+"条件serName="+serName
					+",sOpName="+sOpName
					+",serState="+serState
					+",orderCode="+orderCode
					+",identifyMode="+identifyMode
					+",结果数量："+(serList==null?null:serList.size());
			
			EmpExecutionContext.info(opModule, curUser.getCorpCode(), curUser.getUserId().toString(), curUser.getUserName(), content, StaticValue.GET);
		} 
		catch (Exception e)
		{
			//打印和保存异常信息
			EmpExecutionContext.error(e, "上行业务的find方法异常。");
		}
		finally
		{
			//跳转到页面
			request.getRequestDispatcher(this.empRoot + "/engine/eng_moService.jsp")
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
				EmpExecutionContext.error("查询上行业务管理列表，从session中获取加密对象为空。");
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
			EmpExecutionContext.error("查询上行业务管理列表，加密参数，异常。");
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

	public void update(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		SuperOpLog spLog = new SuperOpLog();
		String opType = "";
		String opContent = "";
		//业务id
		String serId = null;
		try
		{
			//更新业务信息
			LfService service = new LfService();
			//业务id密文
			String serIdCipher = request.getParameter("serId");
			//描述
			String comments = request.getParameter("comments");
			//业务名称
			String serName3 = request.getParameter("serName");
			//指令代码
			String orderCode = request.getParameter("orderCode");
			//分隔符
			String msgSeparated = request.getParameter("msgSeparated");
			//发送账号
			String spUser = request.getParameter("spUser");
			//运行状态
			Integer runState = Integer.parseInt(request
					.getParameter("runState"));
			//业务编码
			String busCode = request.getParameter("busCode");
			
			//指令类型。1-动态指令（模糊）；0-静态指令（精确）
			String orderType = request.getParameter("orderType");
			
			//识别模式。空和1-使用尾号；2-使用指令
			String identifyMode = request.getParameter("identifyMode");
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务新建/修改，session获取当前登录操作员对象为空。");
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

			service.setCommnets(comments);
			service.setSerName(serName3);
			service.setRunState(runState);
			service.setSerType(1);
			service.setSpUser(spUser);
			service.setBusCode(busCode);
			service.setOrderCode(orderCode == null ? orderCode : orderCode
					.trim().toLowerCase());
			service.setMsgSeparated(msgSeparated);
			//为空则使用尾号
			if(identifyMode == null || identifyMode.length() == 0)
			{
				identifyMode = "1";
			}
			service.setIdentifyMode(Integer.valueOf(identifyMode));
			service.setOrderType(orderType==null?1:Integer.valueOf(orderType));
			
			//设置新修改的指令。指令类型。1-动态指令（模糊）；0-静态指令（精确）
			if(service.getOrderType() == 1)
			{
				//动态指令，则把指令和分隔符合在一起
				service.setStructcode(orderCode+msgSeparated);
			}
			else
			{
				//静态指令则不需要分隔符
				service.setStructcode(orderCode);
			}

			String opUser;
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			//新增上行业务
			opUser = sysuser==null?"":sysuser.getUserName();
			//操作结果
			String opResult = "";
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务ID，业务名称，指令代码，指令匹配方式，分隔符号，业务类型，SP账号，尾号状态，运行状态](");
			//新建上行业务
			if (serIdCipher == null || serIdCipher.trim().length() == 0)
			{
				opType = StaticValue.ADD;
				opContent = "新增上行业务（业务名称：" + service.getSerName() + "）";
				service.setCorpCode(lgcorpcode);
				service.setUserId(lguserid);
				
				//新增操作
				Long result = null;
				
				//使用指令模式，则需插入网关指令表。识别模式。空和1-使用尾号；2-使用指令
				if(identifyMode != null && identifyMode.length() != 0 && "2".equals(identifyMode))
				{
					result = serBiz.addMoService(service, sysuser);
				}
				else
				{
					//使用尾号模式，则只需要保存上行业务记录
					result = baseBiz.addObjReturnId(service);
				}
				
				if (result!=null && result>0)
				{
					//成功日志
					serId = result.toString();
					request.setAttribute("serviceResult", "true");
					spLog.logSuccessString(opUser, opModule, opType, opContent,
							lgcorpcode);
					
					//操作日志
					opResult = "新建上行业务成功。";
					
					EmpExecutionContext.info("新建上行业务，成功" 
							+ ",serId=" + serId
							+ ",userId=" + service.getUserId()
							+ ",corpCode=" + service.getCorpCode()
							+ ",serName=" + service.getSerName()
							+ ",spUser=" + service.getSpUser()
							+ ",busCode=" + service.getBusCode()
							+ ",orderCode=" + service.getOrderCode()
							+ ",msgSeparated=" + service.getMsgSeparated()
							+ ",identifyMode=" + service.getIdentifyMode()
							+ ",orderType=" + service.getOrderType()
					);
				} 
				else
				{
					//失败日志
					request.setAttribute("serviceResult", "false");
					spLog.logFailureString(opUser, opModule, opType, opContent
							+ opSper, null, lgcorpcode);
					opResult = "新建上行业务失败。";
					
					EmpExecutionContext.error("新建上行业务，失败" 
							+ ",serId=" + serId
							+ ",userId=" + service.getUserId()
							+ ",corpCode=" + service.getCorpCode()
							+ ",serName=" + service.getSerName()
							+ ",spUser=" + service.getSpUser()
							+ ",busCode=" + service.getBusCode()
							+ ",orderCode=" + service.getOrderCode()
							+ ",msgSeparated=" + service.getMsgSeparated()
							+ ",identifyMode=" + service.getIdentifyMode()
							+ ",orderType=" + service.getOrderType()
					);
				}
				content.append(result).append("，").append(service.getSerName()).append("，").append(service.getOrderCode()).append("，").append(service.getOrderType())
				.append("，").append(service.getMsgSeparated()).append("，").append(service.getBusCode()).append("，").append(service.getSpUser())
				.append("，").append(service.getIdentifyMode()).append("，").append(service.getRunState()).append(")");
			}
			//编辑上行业务
			else
			{
				opType = StaticValue.UPDATE;
				
				//解密
				serId = decryptionParam(serIdCipher, request);
				//解密失败，返回了null
				if(serId == null)
				{
					EmpExecutionContext.error("上行业务信息修改，解密serId失败。密文：" + serIdCipher);
					return;
				}
				
				service.setSerId(Long.valueOf(serId));
				service.setCreateTime(null);
				opContent = "编辑上行业务（业务名称：" + service.getSerName() + "）";
				//编辑操作
				boolean result = false;
				
				LfService oldSer = baseBiz.getById(LfService.class, service.getSerId());
				//根据spid和service.structcode来获取，而service.structcode在下面重新赋值，这里先用旧的拿原来的记录
				AcmdRoute cmd = serBiz.getCmdRoute(oldSer);
				
				//识别模式。空和1-使用尾号；2-使用指令
				if(service.getIdentifyMode() == 2)
				{
					//使用指令，可能是原来是使用尾号，或者是原来就是使用指令
					
					AcmdRoute newCmd = null;
					if(cmd != null)
					{
						//有网关指令记录，则原来使用指令，所以这里更新
						//获取更新后的对象改动
						newCmd = serBiz.setCmdRoute(service, sysuser);
						newCmd.setId(cmd.getId());
						result = serBiz.updateMoSer(service, newCmd);
					}
					else
					{
						//没网关指令记录，则原来是使用尾号，所以这里新增指令记录
						result = serBiz.updateMoSerToAdd(service, sysuser);
					}
					
				}
				else
				{
					//使用尾号，则可能是原来是使用指令改为使用尾号，或者原来就是使用尾号
					
					if(cmd != null)
					{
						//有网关指令记录，则原来使用指令，现在改为使用尾号，所以要删除网关指令
						result = serBiz.updateMoSerToDel(service, cmd);
					}
					else
					{
						//没网关指令记录，则原来使用尾号，只需要更新上行业务记录
						result = baseBiz.updateObj(service);
					}
				}
				
				if (result)
				{
					request.setAttribute("serviceResult", "true");
					LfService ls = baseBiz.getById(LfService.class, serId);
					//查询条件回填
					request.setAttribute("ls", ls);
					//成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent, lgcorpcode);
					
					opResult = "修改上行业务成功。";
				} 
				else
				{
					request.setAttribute("serviceResult", "false");
					spLog.logFailureString(opUser, opModule, opType, opContent + opSper, null, lgcorpcode);
					opResult = "修改上行业务失败。";
				}
				//操作日志信息
				content.append(serId).append("，").append(oldSer.getSerName()).append("，").append(oldSer.getOrderCode()).append("，").append(oldSer.getOrderType()).append("，")
				.append(oldSer.getMsgSeparated()).append("，").append(oldSer.getBusCode()).append("，").append(oldSer.getSpUser()).append("，")
				.append(oldSer.getIdentifyMode()).append("，").append(oldSer.getRunState()).append(")-->(")
				.append(serId).append("，").append(service.getSerName()).append("，").append(service.getOrderCode()).append("，").append(service.getOrderType())
				.append("，").append(service.getMsgSeparated()).append("，").append(service.getBusCode()).append("，").append(service.getSpUser())
				.append("，").append(service.getIdentifyMode()).append("，").append(service.getRunState()).append(")");
			}
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, String.valueOf(lguserid), opUser, opResult + content.toString(), opType);
		} 
		catch (Exception e)
		{
			//打印和保存异常信息
			EmpExecutionContext.error(e, "上行业务的update方法异常。");
		}
		finally
		{
			//跳转到页面
			String workType = request.getParameter("workType");
			String prId = request.getParameter("prId");
			if(workType!=null && workType.equals("waterLine"))
			{
				//从session中获取加密解密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//业务id密文
				String serIdCipher = "";
				//加密解密对象不能为null
				if(encryptOrDecrypt == null)
				{
					EmpExecutionContext.error("新建上行业务，从session中获取加密对象为空。");
				}else{
					//业务id密文
					serIdCipher = encryptOrDecrypt.encrypt(serId);
				}
				
				//request.setAttribute("serId", serId);
				response.sendRedirect("eng_moService.htm?method=toMo2&serId="+serIdCipher+"&prId="+prId);
				//request.getRequestDispatcher(this.empRoot + "/engine/ser_addMo2.jsp").forward(request, response);
			}
			else 
			{
				request.getRequestDispatcher(this.empRoot + "/engine/eng_MoInfos.jsp").forward(request, response);
			}
		}
	}

	public void toMo2(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String serId=request.getParameter("serId");
		String prId=request.getParameter("prId");
		if(prId!=null&&!"".equals(prId)&&!"null".equals(prId))
		{
			LfProcess process = baseBiz.getById(LfProcess.class, prId);
			request.setAttribute("process", process);
		}
		request.getRequestDispatcher(this.empRoot + "/engine/eng_addMo2.jsp?serId="+serId).forward(request, response);
	}
	
	public void changeState(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try 
		{
			//文业务id密文
			String serIdCipher  = request.getParameter("serId");
			String runState = request.getParameter("runState");
			
			//从session获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务更改状态，session获取当前登录操作员对象为空。");
				response.getWriter().print("false");
				return;
			}
			// 当前登录操作员id
			//Long lguserid = curUser.getUserId();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("上行业务更改状态，解密serId失败。密文：" + serIdCipher);
				response.getWriter().print("false");
				return;
			}
			
			conditionMap.put("serId", serId);
			conditionMap.put("corpCode", lgcorpcode);
			objectMap.put("runState", runState);

			boolean flag = baseBiz.update(LfService.class, objectMap, conditionMap);
			//操作结果
			String opResult = "";
			if(flag)
			{
				opResult = "修改运行状态成功。";
			}
			else
			{
				opResult = "修改运行状态失败。";
			}
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务ID，运行状态](").append(serId).append("，").append(runState).append(")");
			LfSysuser lfSysuser = (LfSysuser)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			//操作员ID
			String userId = "";
			//操作员名称
			String userName = " ";
			if(lfSysuser != null)
			{
				userName = lfSysuser.getUserName();
				userId = lfSysuser.getUserId()==null?null:String.valueOf(lfSysuser.getUserId());
			}
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, userId, userName, opResult + content.toString(), "UPDATE");
			response.getWriter().print(flag);
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "上行业务的changeState方法异常。");
			response.getWriter().print("false");
		}
	}
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//从session获取当前登录操作员对象
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("删除上行业务，session获取当前登录操作员对象为空。");
			response.getWriter().print("false");
			return;
		}
		SuperOpLog spLog = new SuperOpLog();
		//业务名称
		String serName = request.getParameter("serName");
		//日志内容
		String opContent = "删除上行短信业务（业务名称：" + serName + "）";
		//业务id密文
		String serIdCipher = request.getParameter("serId");
		//业务id
		String serId = null;
		try
		{
			//解密，获取明文业务id
			serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("上行业务删除，解密serId失败。密文：" + serIdCipher);
				response.getWriter().print("false");
				return;
			}
			
			int count = serBiz.deleteUpService(serId, curUser.getCorpCode());
			
			//操作结果
			String opResult;
			if (count > 0)
			{
				//异步返回结果
				response.getWriter().print("true");
				spLog.logSuccessString(curUser.getUserName(), opModule, StaticValue.DELETE, opContent, curUser.getCorpCode());
				opResult = "删除上行业务成功。";
				EmpExecutionContext.info("删除上行业务，成功" 
						+ ",serId=" + serId
						+ ",serName=" + serName
						+ ",userName=" + curUser.getUserName()
						+ ",corpCode=" + curUser.getCorpCode()
				);
			}
			else
			{
				//异步返回结果
				response.getWriter().print("false");
				//异常处理
				spLog.logFailureString(curUser.getUserName(), opModule, StaticValue.DELETE, opContent, null, curUser.getCorpCode());
				opResult = "删除上行业务失败。";
				EmpExecutionContext.error("删除上行业务，失败" 
						+ ",serId=" + serId
						+ ",serName=" + serName
						+ ",userName=" + curUser.getUserName()
						+ ",corpCode=" + curUser.getCorpCode()
				);
			}
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务id，业务名称](").append(serId).append("，").append(serName).append(")");
			//操作日志
			EmpExecutionContext.info(moduleName, curUser.getCorpCode(), curUser.getUserId().toString(), curUser.getUserName(), opResult + content.toString(), StaticValue.DELETE);
		} 
		catch (Exception e)
		{
			//异常处理
			spLog.logFailureString(curUser.getUserName(), opModule, StaticValue.DELETE, opContent + opSper, e, curUser.getCorpCode());
			//处理异常信息
			EmpExecutionContext.error(e, "删除上行业务，异常"
					+ ",serId=" + serId
					+ ",serName=" + serName
					+ ",userName=" + curUser.getUserName()
					+ ",corpCode=" + curUser.getCorpCode()
			);
			//异步返回结果
			response.getWriter().print("false");
		}
	}

	public void doEdit(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String workType = request.getParameter("workType");
		try
		{
			//从session获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务信息修改，session获取当前登录操作员对象为空。");
				return;
			}
			LfService ls = new LfService();
			//业务id密文，需要解密才能用
			String serIdCipher = request.getParameter("serId");
			//业务id
			String serId = null;
			// 当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			// 当前登录企业
			//String lgcorpcode = request.getParameter("lgcorpcode");
			if (serIdCipher != null)
			{
				//解密
				serId = decryptionParam(serIdCipher, request);
				//解密失败，返回了null
				if(serId == null)
				{
					EmpExecutionContext.error("上行业务信息修改，解密serId失败。密文：" + serIdCipher);
				}
				ServiceBiz serBiz = new ServiceBiz();
				//根据业务id获取对象
				ls = serBiz.getService(serId, curUser.getCorpCode());
			}

			LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
			conditionMap1.put("corpCode&in", "0," + curUser.getCorpCode());
			//设置启用查询条件
			conditionMap1.put("state", "0");
			//设置查询手动和手动+触发
			conditionMap1.put("busType&in", "0,2");
			
			//获取业务类型
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMap1, null);
			if ((busList == null || busList.size() == 0) && serId != null)
			{
				busList = new ArrayList<LfBusManager>();
				busList.add(baseBiz.getById(LfBusManager.class, ls.getBusCode()));
			}
			
			request.setAttribute("busList", busList);
			
			//查询sp账号
			List<Userdata> spUserList = smsBiz.getSpUserList(curUser);
			//返回查询结果到页面
			request.setAttribute("spUserList", spUserList);
			boolean flag =false ;
			for (int i = 0; i <spUserList.size(); i++)
			{
				if(spUserList.get(i).getUserId().equals(ls.getSpUser()))
				{
					flag = true;
					break;
				}
			}
			if(!flag)
			{
				conditionMap1.clear();
				conditionMap1.put("userId", ls.getSpUser());
				List<Userdata> spUserListTemp =baseBiz.getByCondition(Userdata.class, conditionMap1, null);
				if(spUserListTemp!=null && spUserListTemp.size()>0)
				{
					request.setAttribute("staffName", spUserListTemp.get(0).getStaffName());
				}
			}
			request.setAttribute("prId", request.getParameter("prId"));
			request.setAttribute("ls", ls);
		} 
		catch (Exception e)
		{
			//保存和打印异常信息
			EmpExecutionContext.error(e, "上行业务的doEdit方法异常。");
		}
		finally
		{
			//跳转到页面
			if(workType!=null && workType.equals("waterLine"))
			{
				request.getRequestDispatcher(this.empRoot + "/engine/eng_addMo1.jsp").forward(request, response);
			}
			else 
			{
				request.getRequestDispatcher(this.empRoot + "/engine/eng_MoInfos.jsp").forward(request, response);
			}
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
			EmpExecutionContext.error("上行业务管理列表，传入的参数密文为空。密文："+paramCipher);
			return null;
		}
		//加密解密对象
		ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
		//加密解密对象不能为null
		if(encryptOrDecrypt == null)
		{
			EmpExecutionContext.error("上行业务管理列表，从session中获取加密对象为空。密文："+paramCipher);
			return null;
		}
		
		//解密
		String param = encryptOrDecrypt.decrypt(paramCipher);
		return param;
	}

	public void process(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			//业务id密文
			String serIdCipher = request.getParameter("serId");
			
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("上行业务步骤管理，解密serId失败。密文：" + serIdCipher);
				return;
			}
			
			//步骤集合
			List<LfProcess> proList = proBiz.getProcescBySerId(Long.parseLong(serId));
			/*//加密serId
			Map<String,String> cipherMap = this.encryptionParamForProcess(proList, request);
			if(cipherMap == null)
			{
				cipherMap = new HashMap<String,String>();
			}
			request.setAttribute("cipherMap", cipherMap);*/
			
			request.setAttribute("serId", serIdCipher);
			LfService service  = baseBiz.getById(LfService.class, serId);
			request.setAttribute("serName", service.getSerName());
			request.setAttribute("proList", proList);
			
		} 
		catch (Exception e)
		{
			//保存和打印异常信息
			EmpExecutionContext.error(e, "上行业务的process方法异常。");
		}
		finally
		{
			//跳转到页面
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_moProcess.jsp").forward(
					request, response);
		}
	}
	
	/**
	 * 
	 * @description 加密参数
	 * @param serList 页面显示列表数据对象
	 * @param request 请求对象
	 * @return 加密后的对应关系map，key为明文，value为对应的密文
	 */
	private Map<String,String> encryptionParamForProcess(List<LfProcess> proList, HttpServletRequest request)
	{
		try
		{
			if(proList == null || proList.size() < 1)
			{
				return null;
			}
			
			//从session中获取加密解密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密解密对象不能为null
			if(encryptOrDecrypt == null)
			{
				EmpExecutionContext.error("查询上行业务管理列表，从session中获取加密对象为空。");
				return null;
			}
			
			//加密后的对应关系map，key为明文，value为对应的密文
			Map<String,String> cipherMap = new HashMap<String,String>();
			String serId;
			String keyId;
			//遍历操作员列表，对ID进行加密
			for(LfProcess process : proList)
			{
				serId = String.valueOf(process.getSerId());
				keyId = encryptOrDecrypt.encrypt(serId);
				cipherMap.put(serId, keyId);
			}
			return cipherMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("查询上行业务管理列表，加密参数，异常。");
			return null;
		}
	}

	public void checkName(HttpServletRequest request,
			HttpServletResponse response)
	{
		//业务名称
		String serName = request.getParameter("serName");
		try
		{
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务检查名称，session获取当前登录操作员对象为空。");
				response.getWriter().print("false");
				return;
			}
			// 当前登录操作员id
			//Long lguserid = curUser.getUserId();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			// 当前登录企业
			//String lgcorpcode = request.getParameter("lgcorpcode");
			
			//异步返回结果
			response.getWriter().print(bab.isSerNameExists(serName, "1", lgcorpcode) + "");
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务的checkName方法异常。");
		}
	}

	public void checkOrderCode(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		try
		{
			// 当前登录企业
			//String lgcorpcode = request.getParameter("lgcorpcode");
			//指令代码
			String orderCode = request.getParameter("orderCode");
			//当前数据库中的值。指令代码
			String curCode = request.getParameter("curCode");
			//指令代码类型
			String orderType = request.getParameter("orderType");
			//分隔符
			String msgSeparated = request.getParameter("msgSeparated");
			//识别模式。空和1-使用尾号；2-使用指令
			String identifyMode = request.getParameter("identifyMode");
			//当前数据库中的值。识别模式。空和1-使用尾号；2-使用指令
			String curIdentifyMode = request.getParameter("curIdentifyMode");
			
			EmpExecutionContext.info("上行业务，检查上行指令，页面请求参数。"
					+ "orderCode="+orderCode
					+ "，curCode="+curCode
					+ "，orderType="+orderType
					+ "，msgSeparated="+msgSeparated
					+ "，identifyMode="+identifyMode
					);
			
			if(orderCode == null || orderCode.length() == 0){
				response.getWriter().print(false);
				return;
			}
			//新建的时候，不会有值，给空字符串
			if(curCode == null || curCode.length() < 1)
			{
				curCode = "";
			}
			
			//从session获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务，检查上行指令，session获取当前登录操作员对象为空。");
				//异步返回结果
				response.getWriter().print("false");
				return;
			}
			
			String lgcorpcode = curUser.getCorpCode();
			
			//兼容以前不区分大小写的记录
			boolean afterResult = serBiz.isOrderCodeExists(orderCode.toUpperCase(), lgcorpcode, orderType, identifyMode, msgSeparated,curIdentifyMode, curCode.toUpperCase());
			//区分大小写的记录
			boolean nowResult = serBiz.isOrderCodeExists(orderCode.toLowerCase(), lgcorpcode, orderType, identifyMode, msgSeparated,curIdentifyMode, curCode.toLowerCase());
			
			if(afterResult || nowResult){
				//异步返回结果
				response.getWriter().print("true");
			}else{
				//异步返回结果
				response.getWriter().print("false");
			}
			//String result = serBiz.isOrderCodeExists(orderCode.toLowerCase(), lgcorpcode, orderType, identifyMode, msgSeparated) + "";
			
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务，检查上行指令，处理异常。");
			response.getWriter().print("false");
		}
	}

	public void updatePro(HttpServletRequest request,HttpServletResponse response)
	{
		SuperOpLog spLog = new SuperOpLog();
		String	opUser ="";
		//新增或修改步骤
		try
		{
			LfProcess process = new LfProcess();
			//业务id密文
			String serIdCipher = request.getParameter("serId");
			//描述
			String comments = request.getParameter("comments");
			//步骤id
			String prId = request.getParameter("prId");
			//步骤类型
			Integer prType = Integer.parseInt(request.getParameter("prType"));
			//步骤名称
			String prName = request.getParameter("prName");
			//是否是最终步骤
			Integer finalState = Integer.parseInt(request
					.getParameter("finalState"));
			//指令代码
			String orderCode = request.getParameter("orderCode");

			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务步骤新建/修改，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			/*// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			String lguserid = request.getParameter("lguserid");*/

			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
						//新增上行业务
			opUser = sysuser==null?"":sysuser.getUserName();

			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("上行业务步骤修改，解密serId失败。密文：" + serIdCipher);
				return;
			}

			//设置业务id
			process.setSerId(Long.parseLong(serId));
			//设置指令编码
			process.setProCode(orderCode);
			//设置步骤类型
			process.setPrType(prType);
			//设置是否是最终步骤
			process.setFinalState(finalState);
			//设置描述
			process.setComments(comments);
			//设置步骤名称
			process.setPrName(prName);
			//按业务id获取步骤集合
			List<LfProcess> proList = null;
			Integer prNo = 1;
			if (serId != null && !"".equals(serId))
			{
				LinkedHashMap<String, String> conditionMap2 = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> orderbyMap2 = new LinkedHashMap<String, String>();
				orderbyMap2.put("prId", StaticValue.DESC);
				conditionMap2.put("serId", serId);
				//按业务id获取步骤集合
				proList = baseBiz.getByCondition(
						LfProcess.class, conditionMap2, orderbyMap2);
				if (proList != null && proList.size() > 0)
				{
					prNo = proList.get(0).getPrNo() + 1;
				} else
				{
					prNo = 1;
				}
			}
			process.setPrNo(prNo);
			//操作结果
			String opResult = "";
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务id，步骤id，步骤名称，步骤类型，是否是最终步骤](");
			//操作类型
			String opType = "";
			//新增
			if (prId == null || "".equals(prId))
			{
				opType = StaticValue.ADD;
				//操作内容
				String opContent = "新建步骤（步骤名：" + prName + "）";

				//新增步骤
				Long Id = baseBiz.addObjReturnId(process);

				//判断新增结果
				if (Id>0)
				{
					//处理结果返回页面
					prId=Id.toString();
					request.setAttribute("result", "true");
					//保存成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent,
							lgcorpcode);
					opResult = "新建步骤成功。";
				} else
				{
					//处理结果返回页面
					request.setAttribute("result", "false");
					//保存失败日志
					spLog.logFailureString(opUser, opModule, opType, opContent
							+ opSper, null, lgcorpcode);
					opResult = "新建步骤失败。";
				}
								
				//操作日志信息
				content.append(serId).append("，").append(Id).append("，").append(prName).append("，").append(prType)
				.append("，").append(finalState).append(")");
			}
			//更新
			else
			{
				//操作类型为更新
				opType = StaticValue.UPDATE;
				//操作内容
				String opContent = "修改步骤（步骤名：" + prName + "）";
				//设置步骤id
				process.setPrId(Long.parseLong(prId));
				//设置步骤类型
				process.setPrType(prType);
				//设置是否为最后步骤
				process.setFinalState(finalState);
				//设置评价
				process.setComments(comments);
				process.setPrName(prName);
				//设置指令代码
				process.setProCode(orderCode);
				//更新操作
				boolean result = baseBiz.updateObj(process);
				if (result)
				{
					LfProcess process2 = proBiz.getPorcessByPrId(Long
							.parseLong(prId));
					request.setAttribute("process", process2);
					request.setAttribute("result", "true");
					//成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent,
							lgcorpcode);
					opResult = "修改步骤成功。";
				} else
				{
					//失败日志
					request.setAttribute("result", "false");
					spLog.logFailureString(opUser, opModule, opType, opContent
							+ opSper, null, lgcorpcode);
					opResult = "修改步骤失败。";
				}
								
				//修改前的步骤名称
				String oldPrName = " ";
				//修改前的步骤类型
				String oldprType = "";
				//修改前的是否是最终步骤
				String oldFinalState = "";
				if(proList != null && proList.size() > 0)
				{
					oldPrName = proList.get(0).getPrName();
					oldprType = proList.get(0).getPrType()==null?null:String.valueOf(proList.get(0).getPrType());
					oldFinalState = proList.get(0).getFinalState()==null?null:String.valueOf(proList.get(0).getFinalState());
				}
				//操作日志信息
				content.append(serId).append("，").append(prId).append("，").append(oldPrName).append("，")
				.append(oldprType).append("，").append(oldFinalState)
				.append(")-->(")
				.append(serId).append("，").append(prId).append("，").append(prName).append("，").append(prType)
				.append("，").append(finalState).append(")");
			}
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, lguserid, opUser, opResult + content.toString(), opType);
			request.setAttribute("prId", prId);
			
			//从session中获取加密解密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密解密对象不能为null
			if(encryptOrDecrypt == null)
			{
				EmpExecutionContext.error("新建上行业务，从session中获取加密对象为空。");
				return;
			}
			//业务id密文
			serIdCipher = encryptOrDecrypt.encrypt(serId);
			request.setAttribute("serId", serIdCipher);
			
			//跳转到页面
			String workType = request.getParameter("workType");
			if(workType!=null && workType.equals("waterLine"))
			{
				//request.getRequestDispatcher(this.empRoot + "/engine/ser_addMo3.jsp").forward(request, response);
				if(prType==5)
				{
					//编辑回复短信reply步骤
					//editTemp(request,response);
					response.sendRedirect("eng_moService.htm?method=editTemp&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&workType=waterLine&serId="+serIdCipher+"&prId="+prId);
					return;
				}else 
				{
					//其他步骤
					//editSql(request,response);
					response.sendRedirect("eng_moService.htm?method=editSql&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&workType=waterLine&serId="+serIdCipher+"&prId="+prId);
					return;
				}
			}else {
				request.getRequestDispatcher(this.empRoot + "/engine/eng_addMoProcess.jsp").forward(request, response);
			}
		} catch (Exception e)
		{
			//保存和打印异常信息
			EmpExecutionContext.error(e, "上行业务的updatePro方法异常。");
		}
	}

	public void doProEdit(HttpServletRequest request,HttpServletResponse response)
	{
		//根据步骤id获取步骤对象
		try
		{
			//
			String serId = request.getParameter("serId");
			if (request.getParameter("prId") != null
					&& !"".equals(request.getParameter("prId")))
			{
				Long prId = Long.valueOf(request.getParameter("prId"));
				//查找步骤信息
				LfProcess process = proBiz.getPorcessByPrId(prId);
				request.setAttribute("process", process);
			}

			request.setAttribute("serId", serId);
			//跳转到页面
			String workType = request.getParameter("workType");
			if(workType!=null && workType.equals("waterLine"))
			{
				request.getRequestDispatcher(this.empRoot + "/engine/eng_addMo2.jsp").forward(request, response);
			}else
			{
				request.getRequestDispatcher(this.empRoot + "/engine/eng_addMoProcess.jsp").forward(request, response);
			}
			
		} catch (Exception e)
		{
			//保存和打印异常信息
			EmpExecutionContext.error(e, "上行业务的doProEdit方法异常。");
		}
	}

	public void delPro(HttpServletRequest request, HttpServletResponse response)
	{
		SuperOpLog spLog = new SuperOpLog();
		String opUser ="";
		try
		{
			//日志操作类型：删除
			String opType = StaticValue.DELETE;
			//步骤名
			String prName = request.getParameter("prName");
			//日志操作内容
			String opContent = "删除步骤（步骤名：" + prName + "）";
			//步骤id
			Long prId = Long.valueOf(request.getParameter("prId"));
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务删除步骤，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			/*// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			String lguserid = request.getParameter("lguserid");*/
			//删除操作
			boolean result = proBiz.delAllAboutProcess(prId);
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			opUser = sysuser==null?"":sysuser.getUserName();
			//操作结果
			String opResult = "";
			if (result)
			{
				//成功日志
				response.getWriter().print("true");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				opResult = "删除步骤成功。";
			
			} else
			{
				//失败日志
				response.getWriter().print("false");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
				opResult = "删除步骤失败。";
			}
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[步骤id，步骤名称](").append(prId).append("，").append(prName).append(")");
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, lguserid, opUser, opResult + content.toString(), opType);
		} catch (Exception e)
		{
			//保存和打印异常信息
			EmpExecutionContext.error(e, "上行业务的delPro方法异常。");
		}
	}

	public List<LfDBConnect> getAllDBConnects(String corpCode) throws Exception
	{
		BaseBiz baseBiz = new BaseBiz();
		// 数据库连接对象集合
		List<LfDBConnect> connectsList = null;
		// 条件
		LinkedHashMap<String, String> con = new LinkedHashMap<String, String>();
		try
		{
			// 设置条件
			con.put("corpCode", corpCode);
			// 按条件从数据库查询
			connectsList = baseBiz.findListByCondition(LfDBConnect.class, con,
					null);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务获取所有DB连接信息对象异常。");
			//抛出异常
			throw e;
		}
		// 返回结果
		return connectsList;
	}
	public void editSql(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//判断是否是流水线工作流
			String workType = request.getParameter("workType");
			//步骤id
			String prId = request.getParameter("prId");
			//业务id
			String serId = request.getParameter("serId");
			if (prId == null || prId.equals(""))
			{
				prId = (String) request.getAttribute("prId");
			}
			if (serId.equals("") || serId == null)
			{
				serId = (String) request.getAttribute("serId");
			}
			//通过id获取步骤
			LfProcess process = proBiz.getPorcessByPrId(Long.parseLong(prId));

			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务跳转到Sql新建/修改，session获取当前登录操作员对象为空。");
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

			//获取数据库连接信息
			List<LfDBConnect> dbList = getAllDBConnects(lgcorpcode);

			//查询条件
			LfTemplate templCon = new LfTemplate();
			// 3短信模板；4彩信模板
			templCon.setTmpType(3);
			// 模板状态：0无效；1有效
			templCon.setTmState(1L);
			// 无需审核或审核通过
			templCon.setIsPass(5);
			// 0静态 1动态 2智能抓取模板
			String dsflag = "2";
			List<LfTemplate> tmpList = templBiz.getTemplateByCondition(templCon, 1, lguserid, dsflag, null);
			
			request.setAttribute("tmpList", tmpList);

			request.setAttribute("prId", prId);
			request.setAttribute("serId", serId);
			request.setAttribute("process", process);
			request.setAttribute("dbList", dbList);
			request.setAttribute("lguserid", lguserid.toString());
			request.setAttribute("lgcorpcode", lgcorpcode);
			//跳转到页面
			if(workType!=null && workType.equals("waterLine")){
				request.getRequestDispatcher(this.empRoot + "/engine/eng_addMo3.jsp").forward(request, response);
			}else{
				request.getRequestDispatcher(this.empRoot + "/engine/eng_editMoSql.jsp").forward(request, response);
			}
		} catch (Exception e)
		{
			//保存和打印异常信息
			EmpExecutionContext.error(e, "上行业务的editSql方法异常。");
		}
	}

	public void upSql(HttpServletRequest request, HttpServletResponse response)
	{
		SuperOpLog spLog = new SuperOpLog();
		String opUser = "";
		try
		{
			//判断是否是流水线工作流
			String workType = request.getParameter("workType");
			//判断是否结束创建步骤
			String morePro = request.getParameter("morePro");//1继续0结束
			//步骤id
			String prId = request.getParameter("prId");
			//业务id
			String serId = request.getParameter("serId");
			// 模板Id
			String tempId = request.getParameter("tempSel");
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务Sql修改，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			// 当前登录企业
			/*String lgcorpcode = request.getParameter("lgcorpcode");
			String lguserid = request.getParameter("lguserid");*/

			//操作类型为更新
			String opType = StaticValue.UPDATE;
			//日志内容
			String opContent = "数据库配置";
			//sql语句
			String sql = request.getParameter("sql");
			//数据源id
			Long dbId = Long.valueOf(request.getParameter("dbId"));

			sql = sql.trim();
			if (sql.lastIndexOf(";") == sql.indexOf(";")
					&& sql.lastIndexOf(";") == sql.length() - 1)
			{
				sql = sql.substring(0, sql.length() - 1);
			}
			//获取步骤信息
			LfProcess process = proBiz.getPorcessByPrId(Long.parseLong(prId));
			//修改前的数据库连接id
			String oldDbId = "";
			//修改前的模板ID
			String oldtempID = "";
			//修改前的SQL语名
			String oldSQl = " ";
			if(process != null)
			{
				oldDbId = process.getDbId()==null?null:String.valueOf(process.getDbId());
				oldtempID = process.getTemplateId()==null?null:String.valueOf(process.getTemplateId());
				oldSQl = process.getSql();
			}
			process.setDbId(dbId);
			process.setSql(sql);
			if(tempId == null||"".equals(tempId)){
				tempId="0";
			}
			process.setTemplateId(Long.valueOf(tempId));
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			opUser = sysuser==null?"":sysuser.getUserName();

			//更新步骤
			boolean result = baseBiz.updateObj(process);
			//操作结果
			String opResult = "";
			if (result)
			{
				//成功日志
				request.setAttribute("result", "true");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				opResult = "数据库配置成功。";
			} 
			else
			{
				//失败日志
				request.setAttribute("result", "false");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
				opResult = "数据库配置失败。";
			}
			
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务id，步骤id，数据库连接id，模板Id，SQL语句](")
			.append(serId).append("，").append(prId).append("，").append(oldDbId).append("，").append(oldtempID).append("，").append(oldSQl)
				.append(")-->(")
				.append(serId).append("，").append(prId).append("，").append(process.getDbId()).append("，").append(process.getTemplateId())
				.append("，").append(process.getSql()).append(")");;
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, lguserid, opUser, opResult + content.toString(), opType);
			
			request.setAttribute("prId", prId);
			request.setAttribute("serId", serId);
			
			//如果是流水线
			if (workType!=null && workType.equals("waterLine")) 
			{
				if(morePro!=null && Integer.valueOf(morePro)==0)
				{
					//结束步骤，创建上行业务完成，跳转到管理界面
					request.setAttribute("createResult", "success");
					find(request, response);
				}
				else 
				{
					//继续创建步骤
					//request.setAttribute("serId", serId);
					//request.getRequestDispatcher(this.empRoot + "/engine/ser_addMo2.jsp").forward(request, response);
					response.sendRedirect(this.empRoot + "/engine/eng_addMo2.jsp?serId="+serId);
					return;
				}
			}
			else 
			{
				editSql(request, response);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务的upSql方法异常。");
		}
	}

	public void editCon(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//步骤id
			String prId = request.getParameter("prId");
            //业务id密文
			String serIdCipher = request.getParameter("serId");
			if (prId == null || serIdCipher == null)
			{
				prId = (String) request.getAttribute("prId");
				serIdCipher = (String) request.getAttribute("serId");
			}
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务步骤条件修改，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			
			List<LfProCon> proConList = proBiz.getProCons(Long.parseLong(prId));
			int tableSize = 0;
			if (proConList != null)
			{
				tableSize = proConList.size();
			}
			
			//解密，获取serId文明
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("上行业务信息修改，解密serId失败。密文：" + serIdCipher);
				return;
			}
			
			//获取步骤集合
			List<LfProcess> proList = proBiz.getSelectProcess(Long
					.parseLong(serId), Long.parseLong(prId));
			request.setAttribute("prId", prId);
			request.setAttribute("serId", serIdCipher);
			request.setAttribute("proConList", proConList);
			request.setAttribute("tableSize", tableSize);
			request.setAttribute("proList", proList);
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("lgcorpcode", lgcorpcode);
			//跳转
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_editProCon.jsp").forward(
					request, response);
		} catch (Exception e)
		{
			//保存和打印异常信息
			EmpExecutionContext.error(e, "上行业务的editCon方法异常。");
		}
	}

	public void upProCon(HttpServletRequest request,HttpServletResponse response)
	{
		SuperOpLog spLog = new SuperOpLog();
		BaseBiz baseBiz = new BaseBiz();
		String opUser = "";
		try
		{
			//日志操作类型
			String opType = StaticValue.UPDATE;
			//日志内容
			String opContent = "编辑执行条件";
			//步骤id
			String prId = request.getParameter("prId");
			//业务id
			String serId = request.getParameter("serId");
			String[] UsedPrId = request.getParameterValues("UsedPrId");
			String[] conExpress = request.getParameterValues("conExpress");
			String[] conOperate = request.getParameterValues("conOperate");
			String[] conValue = request.getParameterValues("conValue");

			List<LfProCon> proConList = new ArrayList<LfProCon>();
			//执行条件
			StringBuffer proConInfo = new StringBuffer();
			if(conExpress!=null)
			{
				
				for (int i = 0; i < conExpress.length; i++)
				{
					if (!"".equals(conExpress[i]))
					{
						LfProCon proCon = new LfProCon();
						proCon.setPrId(Long.parseLong(prId));
						if (!"".equals(UsedPrId[i]))
						{
							proCon.setUsedPrId(Long.valueOf(UsedPrId[i]));
						}
						proCon.setConExpress(conExpress[i]);
						proCon.setConOperate(conOperate[i]);
						proCon.setConValue(conValue[i]);
						//拼接执行条件
						proConInfo.append(conExpress[i]).append("|").append(conOperate[i]).append("|").append(conValue[i]).append("&");
						proConList.add(proCon);
					}
				}
			}

			//更新操作
			boolean result = serBiz.updateProCon(proConList, Long
					.parseLong(prId));

			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务步骤条件更新，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			// 当前登录企业
			/*String lgcorpcode = request.getParameter("lgcorpcode");
			String lguserid = request.getParameter("lguserid");*/
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			opUser = sysuser==null?"":sysuser.getUserName();
			//操作结果
			String opResult = "";
			if (result)
			{
				//成功日志
				request.setAttribute("result", "true");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				opResult = "编辑执行条件成功。";
			} else
			{
				//失败日志
				request.setAttribute("result", "false");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
				opResult = "编辑执行条件失败。";
			}
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务id，步骤id，执行条件(表达式|操作符|条件值)](")
			.append(serId).append("，").append(prId).append("，").append(proConInfo.toString()).append(")");
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, lguserid, opUser, opResult + content.toString(), opType);
			request.setAttribute("prId", prId);
			request.setAttribute("serId", serId);
			String workType = request.getParameter("workType");
			String morePro = request.getParameter("morePro");
			if(workType!=null && workType.equals("waterLine"))
			{
				if(morePro!=null && Integer.valueOf(morePro)==0)
				{
					//结束步骤，创建上行业务完成，跳转到管理界面
					request.setAttribute("createResult", "success");
					find(request, response);
					return;
				}else 
				{
					//继续创建步骤
					request.setAttribute("serId", serId);
					request.getRequestDispatcher(this.empRoot + "/engine/eng_addMo2.jsp").forward(request, response);
					return;
				}
			}
			editCon(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务的upProCon方法异常。");
		}
	}
	
	private boolean AddProCon(HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			//步骤id
			String prId = request.getParameter("prId");
			//业务id
			String[] UsedPrId = request.getParameterValues("UsedPrId");
			String[] conExpress = request.getParameterValues("conExpress");
			String[] conOperate = request.getParameterValues("conOperate");
			String[] conValue = request.getParameterValues("conValue");

			List<LfProCon> proConList = new ArrayList<LfProCon>();

			if(conExpress!=null)
			{
				
				for (int i = 0; i < conExpress.length; i++)
				{
					if (!"".equals(conExpress[i]))
					{
						LfProCon proCon = new LfProCon();
						proCon.setPrId(Long.parseLong(prId));
						if (!"".equals(UsedPrId[i]))
						{
							proCon.setUsedPrId(Long.valueOf(UsedPrId[i]));
						}
						proCon.setConExpress(conExpress[i]);
						proCon.setConOperate(conOperate[i]);
						proCon.setConValue(conValue[i]);
						
						proConList.add(proCon);
					}
				}
			}
			//更新操作
			boolean result = serBiz.updateProCon(proConList, Long
					.parseLong(prId));
			return result;

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务的AddProCon方法新增执行条件异常。");
			return false;
		}
	}

	public void editTemp(HttpServletRequest request,HttpServletResponse response)
	{
		try
		{
			//步骤id
			String prId = request.getParameter("prId");
			//业务id密文
			String serIdCipher = request.getParameter("serId");
			if (prId == null || prId.equals(""))
			{
				prId = (String) request.getAttribute("prId");
			}
			if (serIdCipher == null || serIdCipher.equals(""))
			{
				serIdCipher = (String) request.getAttribute("serId");
			}
			LfReply reply = proBiz.getReplyByPrId(Long.parseLong(prId));
			//获取步骤关联模板id
			Long tempId=null;
			if(prId!=null)
			{
				LfProcess process=proBiz.getPorcessByPrId(Long.parseLong(prId));
				tempId = process.getTemplateId();
			}
			if (reply == null)
			{
				reply = new LfReply();
				reply.setMsgLoopId(0l);
			}
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("上行业务短信模板，解密serId失败。密文：" + serIdCipher);
				return;
			}
			request.setAttribute("serId", serIdCipher);
			//根据业务id获取步骤集合
			List<LfProcess> proList = proBiz.getSelectProcess(Long.parseLong(serId));
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务跳转修改短信模板，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			Long lguserid = curUser.getUserId();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			// 当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			
			//查询条件
			LfTemplate templCon = new LfTemplate();
			// 短信模板
			templCon.setTmpType(3);
			// 有效
			templCon.setTmState(1L);
			// 无需审核或审核通过
			templCon.setIsPass(5);
			// 0静态模板；1动态模板
			String dsflag = "0,1";
			List<LfTemplate> temList = templBiz.getTemplateByCondition(templCon, 1, curUser.getUserId(), dsflag, null);
			
			request.setAttribute("prId", prId);
			request.setAttribute("reply", reply);
			request.setAttribute("proList", proList);
			request.setAttribute("temList", temList);
			request.setAttribute("tempId", tempId);
			request.setAttribute("lguserid", lguserid.toString());
			request.setAttribute("lgcorpcode", lgcorpcode);
			//跳转到页面
			String workType = request.getParameter("workType");
			if(workType!=null && workType.equals("waterLine"))
			{
				request.getRequestDispatcher(this.empRoot + "/engine/eng_addMo4.jsp").forward(request, response);
			}else {
				request.getRequestDispatcher(this.empRoot + "/engine/eng_editMoTemp.jsp").forward(request, response);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务的editTemp方法异常。");
		}
	}

	public void upTemp(HttpServletRequest request, HttpServletResponse response)
	{
		SuperOpLog spLog = new SuperOpLog();
		String opUser = "";
		try
		{
			//操作结果
			boolean result = false;
			//步骤id
			String prId = request.getParameter("prId");
			//业务id
			String serId = request.getParameter("serId");
			//tempId
			String tempId = request.getParameter("tempId");
			//操作类型
			String opType = StaticValue.UPDATE;
			//操作内容
			String opContent = "编辑短信回复模板";
			String msgLoopId = request.getParameter("msgLoopId");
			String msgMain = request.getParameter("msgMain");

			LfReply reply = proBiz.getReplyByPrId(Long.parseLong(prId));
			if (reply == null)
			{
				reply = new LfReply();
			}
			//修改前发送内容
			String oldMsgMain = reply.getMsgMain();
			//修改前步骤id
			String oldMsgLoopId = reply.getMsgLoopId()==null?null:String.valueOf(reply.getMsgLoopId());
			
			if (!"".equals(msgLoopId.trim()) && msgLoopId != null)
			{
				reply.setMsgLoopId(Long.valueOf(msgLoopId));
			}
			reply.setMsgMain(msgMain.trim());
			if ("".equals(msgLoopId.trim()) && reply.getReId() != null)
			{
				proBiz.delReplyByPrId(Long.parseLong(prId));
				reply.setReId(null);
				reply.setMsgLoopId(null);
			}
			//修改前的模板ID
			String oldTempId = "";
			//更新步骤关联的模板Id
			if(prId!=null&&!"".equals(prId))
			{
				LfProcess process = proBiz.getPorcessByPrId(Long.parseLong(prId));
				if(tempId==null||"".equals(tempId)){
					tempId="0";
				}
				process.setDbId(null);
				if(process != null)
				{
					oldTempId = process.getTemplateId()==null?null:String.valueOf(process.getTemplateId());
				}
				process.setTemplateId(Long.valueOf(tempId));
				result = baseBiz.updateObj(process);
			}
			String operate = "";
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务id，步骤id，模板id，发送内容](");
			//操作结果
			String opResult = "";
			if(result){
				if (reply.getReId() == null)
				{
					//新增
					reply.setPrId(Long.parseLong(prId));
					result = proBiz.addReply(reply);
					operate = "步骤管理--新增";
					content.append(serId).append("，").append(msgLoopId).append("，").append(tempId).append("，").append(msgMain).append(")");
				} else
				{
					//更新
					result = proBiz.updateReply(reply);
					operate = "步骤管理--修改";
					//修改前的模板ID
					content.append(serId).append("，").append(oldMsgLoopId).append("，").append(oldTempId).append("，").append(oldMsgMain).append(")-->(")
					.append(serId).append("，").append(msgLoopId).append("，").append(tempId).append("，").append(msgMain).append(")");
				}
			}
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务短信模板修改，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			// 当前登录企业
			/*String lgcorpcode = request.getParameter("lgcorpcode");
			String lguserid = request.getParameter("lguserid");*/
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			opUser = sysuser==null?"":sysuser.getUserName();
			if (result)
			{
				//成功日志
				request.setAttribute("result", "true");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				opResult = operate + "短信模块成功。";
				
			} else
			{
				//失败日志
				request.setAttribute("result", "false");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
				opResult = operate + "短信模块失败。";
			}
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, lguserid, opUser, opResult + content.toString(), opType);
			
			request.setAttribute("prId", prId);
			request.setAttribute("serId", serId);
			request.setAttribute("reply", reply);
			request.setAttribute("tempId", tempId);
			//跳转到editTemp方法
			String workType = request.getParameter("workType");
			String morePro=request.getParameter("morePro");
			if(workType!=null && workType.equals("waterLine"))
			{
				//新增执行条件
				if(result){
					this.AddProCon(request, response);
				}
				
				if(morePro!=null && Integer.valueOf(morePro)==0)
				{
					//结束步骤，创建上行业务完成，跳转到管理界面
					request.setAttribute("createResult", "success");
					find(request, response);
					return;
				}else 
				{
					//继续创建步骤
					//request.setAttribute("serId", serId);
					//request.getRequestDispatcher(this.empRoot + "/engine/eng_addMo2.jsp").forward(request, response);
					response.sendRedirect(this.empRoot + "/engine/eng_addMo2.jsp?serId="+serId);
					return;
				}
				
				//upProCon(request,response);
				//response.sendRedirect("eng_moService.htm?method=upProCon&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&workType=waterLine&serId="+serId+"&prId="+prId+"&morePro="+morePro);
			}else {
				editTemp(request, response);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务的upTemp方法异常。");
		}
	}

	/**
	 * 获取通道号 2012-8-6
	 * 
	 * @param request
	 * @param response
	 *            void
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void getGateNumber(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{

		BaseBiz baseBiz = new BaseBiz();
		//发送账号
		String spUser = request.getParameter("spUser");

		//错误编码对象
		ErrorCodeParam errorCodeParam = new ErrorCodeParam();

		//从session获取当前登录操作员对象
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("上行业务，获取通道号，session获取当前登录操作员对象为空。");
			//异步返回结果
			response.getWriter().print("error");
			return;
		}
		// 当前登录企业
		String lgcorpcode = curUser.getCorpCode();
		// 当前登录企业
		//String lgcorpcode = request.getParameter("lgcorpcode");
		
		// 上行模块id
		String subNo = GlobalVariableBiz.getInstance().getValidSubno(
				StaticValue.MOBUSCODE, 0, lgcorpcode, errorCodeParam);

		//尾号分配完了，没有可用的尾号
		if (errorCodeParam.getErrorCode() != null
				&& "EZHB237".equals(errorCodeParam.getErrorCode()))
		{
			response.getWriter().print("noUsedSubNo");
			return;
		} else if (errorCodeParam.getErrorCode() != null
				&& "EZHB238".equals(errorCodeParam.getErrorCode()))
		{
			response.getWriter().print("noSubNo");
			return;
		}

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("userId", spUser);
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		//根据运营商排序
		orderByMap.put("spisuncm", StaticValue.ASC);
		String gateName = null;
		JSONObject jsonObject = null;
		String str = "";
		int strLen = 0;
		int gateTLCount = 0;
		try
		{
			//获取发送账号的通道号记录
			List<GtPortUsed> gtPortUseds = baseBiz.getByCondition(
					GtPortUsed.class, conditionMap, orderByMap);
			GtPortUsed gtPortUsed = null;
			//遍历通道
			for (int i = 0; i < gtPortUseds.size(); i++)
			{
				gtPortUsed = gtPortUseds.get(i);
				//移动
				if (gtPortUsed.getSpisuncm() == 0)
				{
					gateName = "移动：";
				}
				//联通
				else if (gtPortUsed.getSpisuncm() == 1)
				{
					gateName = "联通：";
				}
				//电信
				else if (gtPortUsed.getSpisuncm() == 21)
				{
					gateName = "电信：";
				}
				int cpnoLen = gtPortUsed.getCpno() != null ? gtPortUsed
						.getCpno().length() : 0;
				//获取全同号号长度
				strLen = gtPortUsed.getSpgate().length() + cpnoLen
						+ subNo.length();
				//拼成html代码
				if (strLen > 20)
				{
					//长度大于20，非法
					str = str + "<label style='color:black'>" + gateName
							+ gtPortUsed.getSpgate().trim()
							+ gtPortUsed.getCpno().trim() + "</label>"
							+ "<label style='color:red'>" + subNo.trim()
							+ "</label><br>";
					gateTLCount++;
				} else
				{
					//小于20，合法
					str = str + "<label style='color:black'>" + gateName
							+ gtPortUsed.getSpgate().trim()
							+ gtPortUsed.getCpno().trim() + "</label>"
							+ "<label style='color:blue'>" + subNo.trim()
							+ "</label><br>";
				}
			}
			jsonObject = new JSONObject();
			jsonObject.put("dataStr", str);
			jsonObject.put("gateTLCount", gateTLCount);
			jsonObject.put("subNo", subNo);
			//输出到页面
			response.getWriter().print(jsonObject.toString());
		} catch (Exception e)
		{
			response.getWriter().print("error");
			EmpExecutionContext.error(e, "上行业务的getGateNumber方法异常。");
		}
	}

	public void getSubNo(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		//SuperOpLog spLog = new SuperOpLog();
		//发送账号
		String spUser = request.getParameter("spUser");
		String subNo = "";

		//从session获取当前登录操作员对象
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("上行业务获取尾号，session获取当前登录操作员对象为空。");
			response.getWriter().print("false");
			return;
		}
		
		// 当前登录企业
		String lgcorpcode = curUser.getCorpCode();
		ErrorCodeParam errorCodeParam = new ErrorCodeParam();
		//获取尾号
		subNo = GlobalVariableBiz.getInstance().getValidSubno(
				StaticValue.MOBUSCODE, 0, lgcorpcode, errorCodeParam);

		if (errorCodeParam.getErrorCode() != null
				&& "EZHB237".equals(errorCodeParam.getErrorCode()))
		{
			response.getWriter().print("noUsedSubNo");
			return;
		} else if (errorCodeParam.getErrorCode() != null
				&& "EZHB238".equals(errorCodeParam.getErrorCode()))
		{
			response.getWriter().print("noSubNo");
			return;
		}

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("userId", spUser.replaceAll("'",""));
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		//根据运营商排序
		orderByMap.put("spisuncm", StaticValue.ASC);
		JSONObject jsonObject = null;
		int strLen = 0;
		String yd = "true";
		String lt = "true";
		String dx = "true";
		try
		{
			//根据spuser获取通道号
			List<GtPortUsed> gtPortUseds = baseBiz.getByCondition(
					GtPortUsed.class, conditionMap, orderByMap);
			GtPortUsed gtPortUsed = null;
			jsonObject = new JSONObject();
			for (int i = 0; i < gtPortUseds.size(); i++)
			{
				gtPortUsed = gtPortUseds.get(i);
				int cpnoLen = gtPortUsed.getCpno() != null ? gtPortUsed
						.getCpno().trim().length() : 0;
				//判断长度
				strLen = gtPortUsed.getSpgate().length() + cpnoLen
						+ subNo.length();
				//如果长度大于20则全通道号非法
				if (gtPortUsed.getSpisuncm() == 0 && strLen > 20)
				{
					yd = "false";
				} 
				//如果长度大于20则全通道号非法
				else if (gtPortUsed.getSpisuncm() == 1 && strLen > 20)
				{
					lt = "false";
				} 
				//如果长度大于20则全通道号非法
				else if (gtPortUsed.getSpisuncm() == 21 && strLen > 20)
				{
					dx = "false";
				}

			}
			jsonObject.put("sendFlag", yd + "&" + lt + "&" + dx);
			jsonObject.put("subNo", subNo);
			//处理结果打印到页面
			response.getWriter().print(jsonObject.toString());
		} catch (Exception e)
		{
			//返回错误
			response.getWriter().print("error");
			//打印异常
			EmpExecutionContext.error(e, "上行业务的getSubNo方法异常。");
		}
	}
	
	public void checkBadWord1(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String tmMsg=request.getParameter("tmMsg");
		String corpCode = request.getParameter("corpCode");
		String words=new String();
		try
		{
			KeyWordAtom keyWordAtom = new KeyWordAtom();
			words=keyWordAtom.checkText(tmMsg,corpCode);
		} catch (Exception e)
		{
			words="error";
			EmpExecutionContext.error(e, "上行业务的checkBadWord1方法异常。");
		}finally
		{
			response.getWriter().print("@"+words);
		}
	}
	public void checkMoUrl(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String result="";
		String userID = request.getParameter("userID");
		if(userID!=null && !"".equals(userID))
		{
			result = serBiz.checkSpUser(userID, 1, 2);
		}
		response.getWriter().print(result);
	}
	
	/**
	 * 跳转到上行设置
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toMoConf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{
			//从session获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务跳转到设置，session获取当前登录操作员对象为空。");
				//异步返回结果
				return;
			}
			String lgcorpcode = curUser.getCorpCode();
			// 当前登录企业
			/*String lgcorpcode = request.getParameter("lgcorpcode");
			if(lgcorpcode == null || lgcorpcode.length() == 0){
				EmpExecutionContext.error("上行业务跳转到设置，企业编码为空。");
				return;
			}*/
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpcode", lgcorpcode);
			conditionMap.put("type", "1");
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("id", StaticValue.ASC);
			List<LfAutoreply> replysList = baseBiz.getByCondition(LfAutoreply.class, conditionMap, orderbyMap);
			LfAutoreply reply = new LfAutoreply();
			if(replysList !=null && replysList.size() > 0){
				reply = replysList.get(0);
			}
			request.setAttribute("reply", reply);
			
		}catch(Exception e){
			EmpExecutionContext.error(e, "上行业务跳转到设置异常。");
		}finally{
			request.getRequestDispatcher(this.empRoot  + this.basePath  + "/eng_moConf.jsp")
			.forward(request, response);
		}
		
	}
	
	/**
	 * 新增或修改上行设置
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void updateMoConf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		try
		{
			//从session获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务设置，session获取当前登录操作员对象为空。");
				response.getWriter().print("false");
				return;
			}
			String lguserid = curUser.getUserId().toString();
			String lgcorpcode = curUser.getCorpCode();
			
			String opType = "";
			String opContent = "";
			/*String lguserid = request.getParameter("lguserid");
			String lgcorpcode = request.getParameter("lgcorpcode");*/
			
			LfAutoreply reply = new LfAutoreply();
			
			String repid = request.getParameter("repid");
			//业务id
			String repContent = request.getParameter("repContent");
			//描述
			String state = request.getParameter("state");
			
			if(lguserid == null || lguserid.length() == 0 || lgcorpcode == null || lgcorpcode.length() == 0 
					|| repContent == null || repContent.length() == 0 || state == null || state.length() == 0){
				
				EmpExecutionContext.error("参数错误。" 
						+ "lguserid:"+lguserid
						+ ",lgcorpcode:"+lgcorpcode
						+ "repContent:"+repContent
						+ "state:"+state);
				return;
				
			}
			reply.setReplycontent(repContent);
			reply.setState(Integer.valueOf(state));
			
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			SuperOpLog spLog = new SuperOpLog();
			//操作结果
			String opResult = "";
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[ID，回复内容，状态](");
			if (repid == null || repid.length() == 0)
			{
				opType = StaticValue.ADD;
				opContent = "新增上行业务配置";
				reply.setCorpcode(lgcorpcode);
				
				reply.setType(1);
				
				//新增操作
				Long result = baseBiz.addObjReturnId(reply);
				if (result !=null && result > 0)
				{
					
					request.setAttribute("moConfResult", "true");
					spLog.logSuccessString(sysuser.getUserName(), opModule, opType, opContent,
							lgcorpcode);
					opResult = "设置上行业务回复成功。";
				} else
				{
					//失败日志
					request.setAttribute("moConfResult", "false");
					spLog.logFailureString(sysuser.getUserName(), opModule, opType, opContent
							+ opSper, null, lgcorpcode);
					opResult = "设置上行业务回复失败。";
				}
				//操作日志信息
				content.append(result).append("，").append(repContent).append("，").append(state).append(")");
			}
			//编辑上行业务
			else
			{
				opType = StaticValue.UPDATE;
				reply.setId(Long.valueOf(repid));
				opContent = "编辑上行业务设置";
				//修改前的回复内容
				String oldRepContent = "";
				//修改前的状态
				String oldState = "";
				//查询修改之前数据
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("id", repid);
				conditionMap.put("corpcode", lgcorpcode);
				List<LfAutoreply> proList = baseBiz.getByCondition(LfAutoreply.class, conditionMap, null);
				if(proList != null && proList.size() > 0)
				{
					oldRepContent = proList.get(0).getReplycontent();
					oldState = proList.get(0).getState()==null?null:String.valueOf(proList.get(0).getState());
				}
				
				//编辑操作
				boolean result = baseBiz.updateObj(reply);
				if (result)
				{
					request.setAttribute("moConfResult", "true");
					//成功日志
					spLog.logSuccessString(sysuser.getUserName(), opModule, opType, opContent,
							lgcorpcode);
					opResult = "修改上行业务回复成功。";
				} 
				else
				{
					request.setAttribute("moConfResult", "false");
					spLog.logFailureString(sysuser.getUserName(), opModule, opType, opContent
							+ opSper, null, lgcorpcode);
					opResult = "修改上行业务回复失败。";
				}
				//操作日志信息
				content.append(repid).append("，").append(oldRepContent).append("，").append(oldState).append(")-->(")
					.append(repid).append("，").append(repContent).append("，").append(state).append(")");;
			}
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, lguserid, sysuser.getUserName(), opResult + content.toString(), opType);
			
		} catch (Exception e)
		{
			request.setAttribute("moConfResult", "false");
			//打印和保存异常信息
			EmpExecutionContext.error(e, "上行业务的update方法异常。");
		}finally{
			this.toMoConf(request, response);
		}
	}

	
}
