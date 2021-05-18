package com.montnets.emp.engine.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.biz.TemplateBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.engine.bean.ZnyqParamValue;
import com.montnets.emp.engine.biz.FetchSendMsgBiz;
import com.montnets.emp.engine.biz.ProcessConfigBiz;
import com.montnets.emp.entity.datasource.LfDBConnect;
import com.montnets.emp.entity.engine.LfProcess;
import com.montnets.emp.entity.engine.LfReply;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

@SuppressWarnings("serial")
public class eng_mtProcessSvt extends BaseServlet
{
	//日志模块
	private final String opModule = "智能引擎";
	//操作员
	private final String opSper = StaticValue.OPSPER;
	
	
	private final String empRoot = "znyq";
	private final SuperOpLog spLog = new SuperOpLog();

	private final BaseBiz baseBiz = new BaseBiz();
	private final TemplateBiz templBiz = new TemplateBiz();
	
	private final String opUser = "";
	private final String basePath = "/engine";
	
	//上行业务模块名称
	private final String moModuleName = "上行业务管理";
	
	//下行业务模块名称
	private final String mtModuleName = "下行业务管理";
	
	
	private final ProcessConfigBiz proBiz = new ProcessConfigBiz();
	
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		PageInfo pageInfo = new PageInfo();
		try
		{
			//业务id密文
			String serIdCipher = request.getParameter("serId");
			
			//解密，获取业务id明文
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务信息修改，解密serId失败。密文：" + serIdCipher);
				return;
			}
			
			//通过id获取业务对象
			LfService service = baseBiz.getById(LfService.class, serId);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("serId", serId);
			//根据业务id获取步骤集合
			List<LfProcess> proList = baseBiz.getByCondition(LfProcess.class,
					conditionMap, null);
			//key为步骤类型，value为对应的步骤
			Map<Integer,LfProcess> proMaps = new HashMap<Integer,LfProcess>();
			if(proList != null && proList.size() > 0)
			{
				for(int i=0;i<proList.size();i++)
				{
					proMaps.put(proList.get(i).getPrType(), proList.get(i));
				}
			}
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("proMaps", proMaps);
			request.setAttribute("service", service);
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务步骤，session获取当前登录操作员对象为空。");
				return;
			}
			String lguserid = curUser.getUserId().toString();
			String lgcorpcode = curUser.getCorpCode();
			String lgusername = curUser.getUserName();
			
			// 当前登录操作员id
			/*String lguserid = request.getParameter("lguserid");
			String lgcorpcode = request.getParameter("lgcorpcode");
			String lgusername = request.getParameter("lgusername");*/
			// 当前登录操作员对象
			LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			request.setAttribute("curSysuser", curSysuser);
			
			String content = "下行步骤管理查询。" 
				+"条件serId="+serId
				+",结果数量："+(proList==null?null:proList.size());
		
			EmpExecutionContext.info(opModule, lgcorpcode, lguserid, lgusername, content, StaticValue.GET);
			
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_mtProcess.jsp").forward(
					request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务步骤的find方法异常。");
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

	public void toAdd(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//业务id
			String serId = request.getParameter("serId");
			//获取业务
			LfService service = baseBiz.getById(LfService.class, serId);
			request.setAttribute("service", service);
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务步骤跳转到新建，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			// 当前登录操作员对象
			//LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			request.setAttribute("curSysuser", curUser);
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_addMtProcess.jsp").forward(
					request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务步骤的toAdd方法异常。");
		}
	}
	
	public void toAddSelect(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String prId = request.getParameter("prId");
			LfProcess process = new LfProcess();
			if(prId != null && !"".equals(prId))
			{
				process = baseBiz.getById(LfProcess.class, prId);
			}
			request.setAttribute("process", process);
			//业务id
			String serId = request.getParameter("serId");
			request.setAttribute("serId", serId);
			
			if(request.getParameter("repPrId") != null && !"".equals(request.getParameter("repPrId")))
			{
				request.setAttribute("repPrId", request.getParameter("repPrId"));
			}
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务步骤跳转到select新建，session获取当前登录操作员对象为空。");
				return;
			}
			Long lguserid = curUser.getUserId();
			//获取业务
			//LfService service = baseBiz.getById(LfService.class, serId);
			//request.setAttribute("service", service);
			// 当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			// 当前登录操作员对象
			//LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			//获取数据连接信息
			List<LfDBConnect> dbList = baseBiz.getByCondition(
					LfDBConnect.class, conditionMap, null);
			
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
			
			request.setAttribute("curSysuser", curUser);
			request.setAttribute("dbList", dbList);
			request.setAttribute("tmpList", tmpList);
			
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_addMt2.jsp").forward(
					request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务步骤的toAddSelect方法异常。");
		}
	}

	public void addSelect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		// 日志操作类型
		String opType = "";
		// 操作内容
		String opContent = "";
		// 描述
		String comments = request.getParameter("comments");

		// 步骤名
		String prName = request.getParameter("prName");
		// 业务id密文
		String serIdCipher = request.getParameter("serId");
		// 步骤类型
		Integer prType = Integer.parseInt(request.getParameter("prType"));
		// 是否为最终步骤
		Integer finalState = Integer.parseInt(request.getParameter("finalState"));

		String sql = request.getParameter("sql");
		// 数据库id
		String dbId = request.getParameter("dbId");
		//模板Id
		String tempId = request.getParameter("tempSel");
		
		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("下行业务步骤select新建，session获取当前登录操作员对象为空。");
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
		//明文业务id
		String serId = null;
		LfProcess process = new LfProcess();
		try
		{
			//解密，获取明文业务id
			serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务新建select步骤，解密serId失败。密文：" + serIdCipher);
				return;
			}
			
			if (request.getParameter("prId") != null
					&& !"".equals(request.getParameter("prId")))
			{
				//prId = request.getParameter("prId");
				// 根据id获取步骤对象
				process = baseBiz.getById(LfProcess.class, request.getParameter("prId"));
			}
			process.setComments(comments);
			process.setPrName(prName);
			process.setDbId(Long.valueOf(dbId));
			sql = sql.trim();
			if (sql.lastIndexOf(";") == sql.indexOf(";")
					&& sql.lastIndexOf(";") == sql.length() - 1)
			{
				// 合法化sql
				sql = sql.substring(0, sql.length() - 1);
			}
			process.setSql(sql);
			if(tempId == null||"".equals(tempId)){
				tempId="0";
			}
			process.setTemplateId(Long.valueOf(tempId));
			//操作结果
			String opResult = "";
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务ID，步骤ID，步骤名称，步骤类型，数据库连接ID，模板ID，SQL语句](");
			
			if (process.getPrId() != null)
			{
				// 修改
				boolean result = baseBiz.updateObj(process);
				
				// 日志操作类型
				opType = StaticValue.UPDATE;
				// 操作内容
				opContent = "修改步骤（步骤名：" + process.getPrName() + "）";
				if(result)
				{
					opResult = "修改select步骤成功。";
				}
				else
				{
					opResult = "修改select步骤失败。";
				}
			} else
			{
				// 新增
				process.setSerId(Long.valueOf(serId));
				process.setPrType(prType);
				process.setFinalState(finalState);
				
				//strResult = "2";
				// 新增步骤
				Long prId = baseBiz.addObjReturnId(process);
				process.setPrId(prId);
				
				// 日志操作类型
				opType = StaticValue.ADD;
				// 操作内容
				opContent = "新增步骤（步骤名：" + process.getPrName() + "）";
				if(prId != null && prId > 0)
				{
					opResult = "新增select步骤成功。";
				}
				else
				{
					opResult = "新增select步骤失败。";
				}
				//操作日志信息
				content.append(process.getSerId()).append("，").append(process.getPrId()).append("，").append(process.getPrName())
				.append("，").append(process.getPrType()).append("，").append(process.getDbId()).append("，")
				.append(process.getTemplateId()).append("，").append(process.getSql()).append(")");
			}
			request.setAttribute("selPro", process);
			// 成功日志，strResult：1为修改成功，2为新增成功
			//request.setAttribute("editProcessResult", strResult);
			spLog.logSuccessString(opUser, opModule, opType, opContent, lgcorpcode);
			//操作日志
			EmpExecutionContext.info(mtModuleName, lgcorpcode, String.valueOf(lguserid), opUser, opResult + content.toString(), opType);
			// 当前登录操作员对象;
			LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			request.setAttribute("curSysuser", curSysuser);
			request.setAttribute("lgcorpcode", lgcorpcode);

		} catch (Exception e)
		{
			//request.setAttribute("editProcessResult", "0");
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);
			EmpExecutionContext.error(e, "下行业务步骤的addSelect方法异常。");
		} finally
		{
			String repPrId = request.getParameter("repPrId")==null?"":request.getParameter("repPrId");
			response.sendRedirect("eng_mtProcess.htm?method=toAddReply&serId="+serIdCipher+"&prId="+process.getPrId()+"&repPrId="+repPrId+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode);
		}
	}
	
	public void toAddReply(HttpServletRequest request,
			HttpServletResponse response)
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
				EmpExecutionContext.error("下行业务跳转到新建reply步骤，解密serId失败。密文：" + serIdCipher);
				return;
			}
			
			LfService service = getService(serId);
			if(service != null)
			{
				String tailcontents = new SmsSendBiz().getTailContents(service.getBusCode(), service.getSpUser(), service.getCorpCode());
				request.setAttribute("tailcontents", tailcontents);
			}
			
			request.setAttribute("serId", serIdCipher);
			//步骤id
			String prId = request.getParameter("repPrId");
			
			LfProcess process = new LfProcess();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			if(prId != null && !"".equals(prId))
			{
				//获取reply步骤对象
				process = baseBiz.getById(LfProcess.class, prId);
				
				conditionMap.clear();
				conditionMap.put("prId", prId);
				//获取reply步骤对象模板
				List<LfReply> replyList = baseBiz.getByCondition(LfReply.class,
						conditionMap, null);
				LfReply reply = new LfReply();
				if (replyList != null && replyList.size() > 0)
				{
					reply = replyList.get(0);
				}
				request.setAttribute("reply", reply);
				
			}
			
			//获取select步骤
			LfProcess selPro = new LfProcess();
			if(request.getParameter("prId") != null && !"".equals(request.getParameter("prId")))
			{
				//新增的时候用这种
				selPro = baseBiz.getById(LfProcess.class, request.getParameter("prId"));
			}
			else
			{
				//修改的时候用这种
				conditionMap.clear();
				conditionMap.put("serId", serId);
				//步骤类型为select步骤
				conditionMap.put("prType", "4");
				List<LfProcess> selProsList = baseBiz.getByCondition(LfProcess.class, conditionMap, null);
				//LfProcess selPro = new LfProcess();
				if(selProsList != null && selProsList.size() > 0)
				{
					selPro = selProsList.get(0);
				}
			}
			request.setAttribute("selPro", selPro);
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务步骤跳转到reply新建，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			Long lguserid = curUser.getUserId();
			// 当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			// 当前登录操作员对象
			//LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			
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
			List<LfTemplate> temList = templBiz.getTemplateByCondition(templCon, 1, lguserid, dsflag, null);

			request.setAttribute("process", process);
			//request.setAttribute("proList", proList);
			request.setAttribute("curSysuser", curUser);
			request.setAttribute("tmpList", temList);
			
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_addMt3.jsp").forward(
					request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务步骤的addSelect方法异常。");
		}
	}
	
	private LfService getService(String serId)
	{
		try
		{
			LfService service = baseBiz.getById(LfService.class, serId);
			return service;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取业务对象，异常。");
			return null;
		}
	}
	
	public void addReply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		// 日志操作类型
		String opType = "";
		// 操作内容
		String opContent = "";
		// 描述
		String comments = request.getParameter("comments");
		// 步骤id
		Long prId = null;
		if (request.getParameter("repPrId") != null
				&& !"".equals(request.getParameter("repPrId")))
		{
			prId = Long.valueOf(request.getParameter("repPrId"));
		}
		// 步骤名
		String prName = request.getParameter("prName");
		// 业务id密文
		String serIdCipher = request.getParameter("serId");
		// 步骤类型
		Integer prType = Integer.parseInt(request.getParameter("prType"));
		// 模板Id
		String tempId = request.getParameter("tempSel");
		// 是否为最终步骤
		Integer finalState = Integer.parseInt(request
				.getParameter("finalState"));
		//select步骤
		String msgLoopId = request.getParameter("msgLoopId");
		//短信内容
		String msgMain = request.getParameter("msgMain");

		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("下行业务步骤reply新建，session获取当前登录操作员对象为空。");
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
		
		//解密，获取明文业务id
		String serId = decryptionParam(serIdCipher, request);
		//解密失败，返回了null
		if(serId == null)
		{
			EmpExecutionContext.error("下行业务修改reply步骤，解密serId失败。密文：" + serIdCipher);
			return;
		}
		
		//修改前的步骤名称
		String oldPrName = "";
		//修改前的步骤类型
		String oldPrType = "";
		//修改前的数据源ID
		String oldMsgLoopId = "";
		//修改前的模块ID
		String oldTempId = "";
		//修改前的发送内容
		String oldMsgMain = "";
		
		//获取连接
		IEmpTransactionDAO empTransDao=new DataAccessDriver().getEmpTransDAO();
		Connection conn = empTransDao.getConnection();
		try
		{
			//打开事务
			empTransDao.beginTransaction(conn);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LfProcess process = new LfProcess();
			if(tempId == null||"".equals(tempId)){
				tempId="0";
			}
			LfReply reply = new LfReply();
			//操作结果
			String opResult = "";
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务ID，步骤ID，步骤名称，步骤类型，数据源ID，模块ID，发送内容](");
			if (prId != null)
			{
				// 修改
				// 根据id获取步骤对象
				process = baseBiz.getById(LfProcess.class, prId);
				oldPrName = process.getPrName();
				oldPrType = process.getPrType()==null?null:String.valueOf(process.getPrType());
				oldTempId = process.getTemplateId()==null?null:String.valueOf(process.getTemplateId());
				process.setComments(comments);
				process.setPrName(prName);
				process.setTemplateId(Long.valueOf(tempId));
				process.setDbId(null);
				//执行修改
				empTransDao.update(conn, process);
				
				conditionMap.clear();
				conditionMap.put("prId", prId.toString());
				List<LfReply> replyList = baseBiz.getByCondition(LfReply.class,
						conditionMap, null);
				if (replyList != null && replyList.size() > 0)
				{
					reply = replyList.get(0);
					oldMsgMain = reply.getMsgMain();
					oldMsgLoopId= reply.getMsgLoopId()==null?null:String.valueOf(reply.getMsgLoopId());
				}
				
				// 日志操作类型
				opType = StaticValue.UPDATE;
				// 操作内容
				opContent = "修改步骤（步骤名：" + process.getPrName() + "）";
				opResult = "修改reply步骤";
				
			} else
			{
				// 新增
				process.setComments(comments);
				process.setPrName(prName);
				process.setSerId(Long.valueOf(serId));
				process.setPrType(prType);
				process.setTemplateId(Long.valueOf(tempId));
				process.setFinalState(finalState);
				
				// 执行新增步骤
				prId = empTransDao.saveObjectReturnID(conn, process);
				process.setPrId(prId);
				
				// 日志操作类型
				opType = StaticValue.ADD;
				// 操作内容
				opContent = "新增步骤（步骤名：" + process.getPrName() + "）";
				opResult = "新增reply步骤";
				
			}
			request.setAttribute("repPrId", prId);
			
			if (msgLoopId != null && !"".equals(msgLoopId.trim()))
			{
				reply.setMsgLoopId(Long.valueOf(msgLoopId));
			}
			if (msgMain != null && !"".equals(msgMain.trim()))
			{
				reply.setMsgMain(msgMain.trim());
			}
			
			if (reply.getReId() == null)
			{
				reply.setPrId(prId);
				//新增reply操作
				Long reId = empTransDao.saveObjectReturnID(conn, reply);
				reply.setReId(reId);
				//操作日志信息
				content.append(process.getSerId()).append("，").append(process.getPrId()).append("，").append(process.getPrName()).append("，")
				.append(process.getPrType()).append("，").append(reply.getMsgLoopId()).append("，").append(process.getTemplateId()).append("，")
				.append(reply.getMsgMain()).append(")");
				if(reId != null)
				{
					opResult += "成功。";
				}
				else
				{
					opResult += "失败。";
				}
			} else
			{
				//更新reply操作
				boolean result = empTransDao.update(conn, reply);
				if(result)
				{
					opResult += "成功。";
				}
				else
				{
					opResult += "失败。";
				}
				//操作日志信息
				content.append(process.getSerId()).append("，").append(process.getPrId()).append("，").append(oldPrName).append("，")
				.append(oldPrType).append("，").append(oldMsgLoopId).append("，").append(oldTempId).append("，")
				.append(oldMsgMain).append(")-->(");
				content.append(process.getSerId()).append("，").append(process.getPrId()).append("，").append(process.getPrName()).append("，")
				.append(process.getPrType()).append("，").append(reply.getMsgLoopId()).append("，").append(process.getTemplateId()).append("，")
				.append(reply.getMsgMain()).append(")");
			}
			
			//提交事务
			empTransDao.commitTransaction(conn);
			//result = true;
		
			// 成功日志，strResult：1为修改成功，2为新增成功
			//request.setAttribute("editProcessResult", strResult);
			spLog.logSuccessString(opUser, opModule, opType, opContent,
					lgcorpcode);
			
			// 当前登录操作员对象
			LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			request.setAttribute("curSysuser", curSysuser);
			request.setAttribute("lgcorpcode", lgcorpcode);
			//登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			//操作员名称
			String userName = " ";
			if(lfSysuser != null)
			{
				userName = lfSysuser.getUserName();
			}
			//操作日志
			EmpExecutionContext.info(mtModuleName, lgcorpcode, String.valueOf(lguserid), userName, opResult + content.toString(), opType);
		} catch (Exception e)
		{
			//异常回滚
			empTransDao.rollBackTransaction(conn);
			
			//request.setAttribute("editProcessResult", "0");
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);
			
			EmpExecutionContext.error(e, "下行业务步骤的addReply方法异常。");
		} 
		finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
			//&serId="+serId+"&prId="+process.getPrId()+"&repPrId="+repPrId+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode);
			response.sendRedirect("eng_mtService.htm?method=toAddTime&serId="+serIdCipher+"&repPrId="+prId+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode);
		}
	}
	
	
	public void toEdit(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//业务id密文
			String serIdCipher = request.getParameter("serId");
			//步骤id
			String prId = request.getParameter("prId");
			
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务步骤修改，解密serId失败。密文：" + serIdCipher);
				return;
			}
			
			LfProcess process = new LfProcess();
			
			//没步骤id，则为新增步骤
			if(prId!=null)
			{
				//获取步骤
				process = baseBiz.getById(LfProcess.class, prId);
				
			}
			else
			{
				//步骤类型
				String prType = request.getParameter("prType");
				//设置步骤类型，便于页面判断新增步骤类型
				process.setPrType(Integer.valueOf(prType));
			}
			request.setAttribute("process", process);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			if(process.getPrType() == 5)
			{
				//进入修改reply则需要加载对应的select步骤
				conditionMap.clear();
				conditionMap.put("serId", serId);
				conditionMap.put("prType", "4");
				//获取步骤列表
				List<LfProcess> proList = baseBiz.getByCondition(LfProcess.class,
						conditionMap, null);
				if(proList != null && proList.size()>0)
				{
					LfProcess selPro = proList.get(0);
					request.setAttribute("selPro", selPro);
				}
				
				if(prId != null)
				{
					//获取reply步骤对象
					conditionMap.clear();
					conditionMap.put("prId", prId);
					List<LfReply> replyList = baseBiz.getByCondition(LfReply.class,
							conditionMap, null);
					LfReply reply = null;
					if (replyList != null && replyList.size() > 0)
					{
						reply = replyList.get(0);
						request.setAttribute("reply", reply);
					}
				}
			}
			
			//获取服务
			LfService service = baseBiz.getById(LfService.class, serId);
			request.setAttribute("service", service);
			
			if(service != null)
			{
				String tailcontents = new SmsSendBiz().getTailContents(service.getBusCode(), service.getSpUser(), service.getCorpCode());
				request.setAttribute("tailcontents", tailcontents);
			}
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务步骤修改，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			Long lguserid = curUser.getUserId();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			// 当前登录操作员id
			/*Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");*/
			// 当前登录操作员对象
			//LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			
			conditionMap.clear();
			conditionMap.put("corpCode", lgcorpcode);
			//获取数据连接信息
			List<LfDBConnect> dbList = baseBiz.getByCondition(
					LfDBConnect.class, conditionMap, null);
			
			conditionMap.clear();
			conditionMap.put("tmState", "1");
			conditionMap.put("tmpType", "3");
			conditionMap.put("isPass&in", "0,1");
			if(process.getPrType() == 5)
			{
				//reply步骤加载模板类型
				conditionMap.put("dsflag&in", "0,1");
			}
			else if(process.getPrType() == 4)
			{
				//select步骤加载模板类型
				conditionMap.put("dsflag", "2");
			}
			else
			{
				conditionMap.put("dsflag&in", "0,1,2");
			}
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("tmid", StaticValue.ASC);
			//获取模板列表
			List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class,
					lguserid, conditionMap, orderbyMap);
			
			request.setAttribute("dbList", dbList);
			request.setAttribute("tmpList", tmpList);
			request.setAttribute("curSysuser", curUser);
			request.setAttribute("lgcorpcode", lgcorpcode);
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_editMtProcess.jsp").forward(
					request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务步骤的toEdit方法异常。");
		}
	}

	public void toSqlConfig(HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//获取服务id
			String serId = request.getParameter("serId");
			//步骤id
			String proId = request.getParameter("prId");
			LfProcess process = baseBiz.getById(LfProcess.class, proId);
			LfService service = baseBiz.getById(LfService.class, serId);

			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务跳转到Sql配置，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			Long lguserid = curUser.getUserId();
			// 当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			// 当前登录操作员对象
			//LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");

			conditionMap.put("corpCode", lgcorpcode);
			//获取数据连接信息
			List<LfDBConnect> dbList = baseBiz.getByCondition(
					LfDBConnect.class, conditionMap, null);
			request.setAttribute("service", service);
			request.setAttribute("process", process);
			request.setAttribute("dbList", dbList);
			//当前操作员
			request.setAttribute("curSysuser", curUser);

			conditionMap.clear();
			conditionMap.put("tmpType", "3");
			conditionMap.put("tmState", "1");
			conditionMap.put("isPass&in", "0,1");
			conditionMap.put("dsflag", "2");

			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("tmid", StaticValue.ASC);
			//获取模板列表
			List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class,
					lguserid, conditionMap, orderbyMap);
			request.setAttribute("tmpList", tmpList);
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_mtSqlConfig.jsp").forward(
					request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务步骤的toSqlConfig方法异常。");
		}
	}

	public void toSmsTemp(HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			//业务id
			String serId = request.getParameter("serId");
			//步骤id
			String prId = request.getParameter("prId");
			//获取步骤对象
			LfProcess process = baseBiz.getById(LfProcess.class, prId);
			//获取业务对象
			LfService service = baseBiz.getById(LfService.class, serId);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("serId", serId);
			conditionMap.put("prType", "4");
			//获取步骤列表
			List<LfProcess> proList = baseBiz.getByCondition(LfProcess.class,
					conditionMap, null);

			LinkedHashMap<String, String> conditionMap2 = new LinkedHashMap<String, String>();
			conditionMap2.put("tmState", "1");
			conditionMap2.put("tmpType", "3");
			conditionMap2.put("isPass&in", "0,1");
			conditionMap2.put("dsflag&in", "0,1");

			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务跳转到短信模板，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			Long lguserid = curUser.getUserId();
			// 当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			// 当前登录操作员对象
			//LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);

			List<LfTemplate> temList = baseBiz.getByCondition(LfTemplate.class,
					lguserid, conditionMap2, null);

			conditionMap.clear();
			conditionMap.put("prId", prId);
			List<LfReply> replyList = baseBiz.getByCondition(LfReply.class,
					conditionMap, null);
			LfReply reply = null;
			if (replyList != null && replyList.size() != 0)
			{
				reply = replyList.get(0);
			}

			request.setAttribute("service", service);
			request.setAttribute("process", process);
			request.setAttribute("proList", proList);
			request.setAttribute("curSysuser", curUser);
			request.setAttribute("temList", temList);
			request.setAttribute("reply", reply);
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_mtSmsTemplate.jsp").forward(
					request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务步骤的toSmsTemp方法异常。");
		}
	}

	public void add(HttpServletRequest request, HttpServletResponse response)
	{
		LfProcess process = new LfProcess();
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		String opType = StaticValue.ADD;
		String opContent = "新建步骤（步骤名：" + process.getPrName() + "）";
		try
		{
			//描述
			String comments = request.getParameter("comments");
			//步骤名称
			String prName = request.getParameter("prName");
			//业务id
			String serId = request.getParameter("serId");
			//步骤类型
			Integer prType = Integer.parseInt(request.getParameter("prType"));
			Integer finalState = Integer.parseInt(request
					.getParameter("finalState"));
			process.setPrType(prType);
			process.setFinalState(finalState);
			process.setComments(comments);
			process.setPrName(prName);
			process.setSerId(Long.valueOf(serId));

			
			//新增步骤
			boolean result = baseBiz.addObj(process);

			if (result)
			{
				//成功日志
				request.setAttribute("addProcessResult", "1");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
			} else
			{
				//失败日志
				request.setAttribute("addProcessResult", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
			}
		} catch (Exception e)
		{
			request.setAttribute("addProcessResult", "0");
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);
			EmpExecutionContext.error(e, "下行业务步骤的add方法异常。");
		} finally
		{
			//通过toadd跳转
			this.toAdd(request, response);
		}
	}

	/**
	 * 新增方法
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String opType = "";
		String opContent = "";
		//描述
		String comments = request.getParameter("comments");
		//步骤id
		Long prId = null;
		if(request.getParameter("prId") != null && !"".equals(request.getParameter("prId")))
		{
			prId = Long.valueOf(request.getParameter("prId"));
		}
		//步骤名
		String prName = request.getParameter("prName");
		//业务id密文
		String serIdCipher = request.getParameter("serId");
		//模板id
		String tempId = request.getParameter("tempSel");
		//步骤类型
		Integer prType = Integer.parseInt(request.getParameter("prType"));
		//是否为最终步骤
		Integer finalState = Integer.parseInt(request.getParameter("finalState"));
		
		String sql = request.getParameter("sql");
		//数据库id
		String dbId = request.getParameter("dbId");

		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("下行业务步骤修改，session获取当前登录操作员对象为空。");
			request.setAttribute("editProcessResult", "0");
			return;
		}
		// 当前登录操作员id
		Long lguserid = curUser.getUserId();
		// 当前登录企业
		String lgcorpcode = curUser.getCorpCode();
		// 当前登录操作员id
		//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
		// 当前登录企业
		//String lgcorpcode = request.getParameter("lgcorpcode");

		//解密，获取明文业务id
		String serId = decryptionParam(serIdCipher, request);
		//解密失败，返回了null
		if(serId == null)
		{
			EmpExecutionContext.error("下行业务修改select步骤，解密serId失败。密文：" + serIdCipher);
			request.setAttribute("editProcessResult", "0");
			return;
		}
		
		try
		{

			sql = sql.trim();
			if (sql.lastIndexOf(";") == sql.indexOf(";")
					&& sql.lastIndexOf(";") == sql.length() - 1)
			{
				//合法化sql
				sql = sql.substring(0, sql.length() - 1);
			}
			
			LfProcess process = new LfProcess();
			//修改前的步骤名称
			String oldPrName = "";
			//修改前的步骤类型
			String oldPrType = "";
			//修改前的数据库连接ID
			String oldDbid = "";
			//修改前的模块ID
			String oldTempId = "";
			//修改前的SQL语句
			String oldSQL = "";
			if(prId != null)
			{
				//修改
				//根据id获取步骤对象
				process = baseBiz.getById(LfProcess.class, prId);
				oldPrName = process.getPrName();
				oldPrType = process.getPrType()==null?null:String.valueOf(process.getPrType());
				oldDbid = process.getDbId()==null?null:String.valueOf(process.getDbId());
				oldTempId = process.getTemplateId()==null?null:String.valueOf(process.getTemplateId());
				oldSQL = process.getSql();
			}
			else
			{
				//新增
				process.setSerId(Long.valueOf(serId));
				process.setPrType(prType);
				process.setFinalState(finalState);
			}
			if(tempId==null||"".equals(tempId)){
				tempId="0";	
			}
			process.setTemplateId(Long.valueOf(tempId));
			process.setComments(comments);
			process.setPrName(prName);
			process.setDbId(Long.valueOf(dbId));
			process.setSql(sql);
			//操作结果
			String opResult = "";
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务ID，步骤ID，步骤名称，步骤类型，数据库连接ID，模块ID，SQL语句](");
			if(prId == null)
			{
				//日志操作类型
				opType = StaticValue.ADD;
				//操作内容
				opContent = "新增步骤（步骤名：" + process.getPrName() + "）";
				opResult = "新增select步骤";
			}
			else
			{
				//日志操作类型
				opType = StaticValue.UPDATE;
				//操作内容
				opContent = "修改步骤（步骤名：" + process.getPrName() + "）";
				opResult = "修改select步骤";
			}
			
			boolean result = false;
			String strResult=null;
			if(prId != null)
			{
				strResult="1";
				//修改
				result = baseBiz.updateObj(process);
				//操作日志信息
				content.append(process.getSerId()).append("，").append(process.getPrId()).append("，").append(oldPrName).append("，")
				.append(oldPrType).append("，").append(oldDbid).append("，").append(oldTempId).append("，")
				.append(oldSQL).append(")-->(")
				.append(process.getSerId()).append("，").append(process.getPrId()).append("，").append(process.getPrName()).append("，")
				.append(process.getPrType()).append("，").append(process.getDbId()).append("，").append(process.getTemplateId()).append("，")
				.append(process.getSql()).append(")");
			}
			else
			{
				strResult="2";
				//新增步骤
				prId = baseBiz.addObjReturnId(process);
				process.setPrId(prId);
				result = true;
				//操作日志信息
				content.append(process.getSerId()).append("，").append(process.getPrId()).append("，").append(process.getPrName()).append("，")
				.append(process.getPrType()).append("，").append(process.getDbId()).append("，").append(process.getTemplateId()).append("，")
				.append(process.getSql()).append(")");
			}

			if (result)
			{
				//成功日志，strResult：1为修改成功，2为新增成功
				request.setAttribute("editProcessResult", strResult);
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				opResult += "成功。";
				
			} else
			{
				//失败日志
				request.setAttribute("editProcessResult", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
				opResult += "失败。";
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			//获取数据连接信息
			List<LfDBConnect> dbList = baseBiz.getByCondition(
					LfDBConnect.class, conditionMap, null);
			
			conditionMap.clear();
			conditionMap.put("tmpType", "3");
			conditionMap.put("tmState", "1");
			conditionMap.put("isPass&in", "0,1");
			conditionMap.put("dsflag", "2");
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("tmid", StaticValue.ASC);
			//获取模板列表
			List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class,
					lguserid, conditionMap, orderbyMap);
			
			request.setAttribute("dbList", dbList);
			request.setAttribute("tmpList", tmpList);
			
			//根据id获取业务对象
			LfService service = baseBiz.getById(LfService.class, serId);
			request.setAttribute("service", service);
			request.setAttribute("process", process);
			
			// 当前登录操作员对象
			LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			request.setAttribute("curSysuser", curSysuser);
			request.setAttribute("lgcorpcode", lgcorpcode);
			//登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			//操作员名称
			String userName = " ";
			if(lfSysuser != null)
			{
				userName = lfSysuser.getUserName();
			}
			//操作日志
			EmpExecutionContext.info(mtModuleName, lgcorpcode, String.valueOf(lguserid), userName, opResult + content.toString(), opType);
		} catch (Exception e)
		{
			request.setAttribute("editProcessResult", "0");
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);
			EmpExecutionContext.error(e, "下行业务步骤的edit方法异常。");
		} finally
		{
			request.getRequestDispatcher(this.empRoot + "/engine/eng_editMtProcess.jsp").forward(request, response);
		}
	}
	
	public void editReply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		String opType = "";
		String opContent = "";
		// 描述
		String comments = request.getParameter("comments");
		// 步骤id
		Long prId = null;
		if (request.getParameter("prId") != null
				&& !"".equals(request.getParameter("prId")))
		{
			prId = Long.valueOf(request.getParameter("prId"));
		}
		// 步骤名
		String prName = request.getParameter("prName");
		// 业务id密文
		String serIdCipher = request.getParameter("serId");
		//模板id
		String tempId = request.getParameter("tempSel");
		// 步骤类型
		Integer prType = Integer.parseInt(request.getParameter("prType"));
		// 是否为最终步骤
		Integer finalState = Integer.parseInt(request
				.getParameter("finalState"));
		//select步骤
		String msgLoopId = request.getParameter("msgLoopId");
		//短信内容
		String msgMain = request.getParameter("msgMain");

		// 当前登录操作员id
		Long lguserid = null;
		// 当前登录企业
		String lgcorpcode = null;
		/*// 当前登录操作员id
		Long lguserid = Long.valueOf(request.getParameter("lguserid"));
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");*/
		
		//获取连接
		IEmpTransactionDAO empTransDao=new DataAccessDriver().getEmpTransDAO();
		Connection conn = empTransDao.getConnection();
		try
		{
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务修改reply配置，session获取当前登录操作员对象为空。");
				request.setAttribute("editProcessResult", "0");
				return;
			}
			// 当前登录操作员id
			lguserid = curUser.getUserId();
			// 当前登录企业
			lgcorpcode = curUser.getCorpCode();
			
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("下行业务修改回复步骤，解密serId失败。密文：" + serIdCipher);
				request.setAttribute("editProcessResult", "0");
				return;
			}
			
			//获取服务
			LfService service = baseBiz.getById(LfService.class, serId);
			request.setAttribute("service", service);
			
			if(service != null)
			{
				String tailcontents = new SmsSendBiz().getTailContents(service.getBusCode(), service.getSpUser(), service.getCorpCode());
				request.setAttribute("tailcontents", tailcontents);
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LfProcess process = new LfProcess();
			LfReply reply = new LfReply();
			//修改前的步骤名称
			String oldPrName = "";
			//修改前的步骤类型
			String oldPrType = "";
			//修改前的数据源ID
			String oldMsgLoopId = "";
			//修改前的模块ID
			String oldTempId = "";
			//修改前的发送内容
			String oldMsgMain = "";
			if (prId != null)
			{
				// 修改
				// 根据id获取步骤对象
				process = baseBiz.getById(LfProcess.class, prId);
				process.setDbId(null);
				oldPrName = process.getPrName();
				oldPrType = process.getPrType()==null?null:String.valueOf(process.getPrType());
				oldTempId = process.getTemplateId()==null?null:String.valueOf(process.getTemplateId());
				conditionMap.clear();
				conditionMap.put("prId", prId.toString());
				List<LfReply> replyList = baseBiz.getByCondition(LfReply.class,
						conditionMap, null);
				if (replyList != null && replyList.size() > 0)
				{
					reply = replyList.get(0);

					oldMsgMain = reply.getMsgMain();
					oldMsgLoopId= reply.getMsgLoopId()==null?null:String.valueOf(reply.getMsgLoopId());
				}
				
			} else
			{
				// 新增
				process.setSerId(Long.valueOf(serId));
				process.setPrType(prType);
				process.setFinalState(finalState);
				
			}
			if(tempId==null||"".equals(tempId)){
				tempId="0";	
			}
			process.setTemplateId(Long.valueOf(tempId));
			process.setComments(comments);
			process.setPrName(prName);
			
			if (msgLoopId != null && !"".equals(msgLoopId.trim()))
			{
				reply.setMsgLoopId(Long.valueOf(msgLoopId));
			}
			if (msgMain != null && !"".equals(msgMain.trim()))
			{
				reply.setMsgMain(msgMain.trim());
			}
			//操作结果
			String opResult = "";
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务ID，步骤ID，步骤名称，步骤类型，数据源ID，模块ID，发送内容](");
			// 为空则新增，定义日志放这里，因为下面prId就有有值
			if (prId == null)
			{
				// 日志操作类型
				opType = StaticValue.ADD;
				// 操作内容
				opContent = "新增步骤（步骤名：" + process.getPrName() + "）";
				opResult = "新增reply步骤";
			} else
			{
				// 日志操作类型
				opType = StaticValue.UPDATE;
				// 操作内容
				opContent = "修改步骤（步骤名：" + process.getPrName() + "）";
				opResult = "修改reply步骤";
			}

			//打开事务
			empTransDao.beginTransaction(conn);
			//boolean result = false;
			//返回页面，用于判断是新增or修改
			String strResult = null;
			if (prId != null)
			{
				strResult = "1";
				// 修改
				empTransDao.update(conn, process);
			} else
			{
				strResult = "2";
				// 新增步骤
				prId = empTransDao.saveObjectReturnID(conn, process);
				process.setPrId(prId);
				//result = true;
			}
			Long reId = null;
			if (reply.getReId() == null)
			{
				reply.setPrId(prId);
				//新增reply操作
				reId = empTransDao.saveObjectReturnID(conn, reply);
				reply.setReId(reId);
				//操作日志信息
				content.append(process.getSerId()).append("，").append(process.getPrId()).append("，").append(process.getPrName()).append("，")
				.append(process.getPrType()).append("，").append(reply.getMsgLoopId()).append("，").append(process.getTemplateId()).append("，")
				.append(reply.getMsgMain()).append(")");
				if(reId != null)
				{
					opResult += "成功。";
				}
				else
				{
					opResult += "失败。";
				}
			} else
			{
				//更新reply操作
				boolean result = empTransDao.update(conn, reply);
				if(result)
				{
					opResult += "成功。";
				}
				else
				{
					opResult += "失败。";
				}
				//操作日志信息
				content.append(process.getSerId()).append("，").append(process.getPrId()).append("，").append(oldPrName).append("，")
				.append(oldPrType).append("，").append(oldMsgLoopId).append("，").append(oldTempId).append("，")
				.append(oldMsgMain).append(")-->(");
				content.append(process.getSerId()).append("，").append(process.getPrId()).append("，").append(process.getPrName()).append("，")
				.append(process.getPrType()).append("，").append(reply.getMsgLoopId()).append("，").append(process.getTemplateId()).append("，")
				.append(reply.getMsgMain()).append(")");
			}
			
			//提交事务
			empTransDao.commitTransaction(conn);
			//result = true;
		
			// 成功日志，strResult：1为修改成功，2为新增成功
			request.setAttribute("editProcessResult", strResult);
			spLog.logSuccessString(opUser, opModule, opType, opContent,
					lgcorpcode);

			conditionMap.clear();
			conditionMap.put("tmpType", "3");
			conditionMap.put("tmState", "1");
			conditionMap.put("isPass&in", "0,1");
			conditionMap.put("dsflag&in", "0,1");
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("tmid", StaticValue.ASC);
			// 获取模板列表
			List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class,
					lguserid, conditionMap, orderbyMap);

			request.setAttribute("tmpList", tmpList);

			// 根据id获取业务对象
			/*LfService service = baseBiz.getById(LfService.class, serId);
			request.setAttribute("service", service);*/
			request.setAttribute("process", process);
			request.setAttribute("reply", reply);
			LfProcess selPro = baseBiz.getById(LfProcess.class, msgLoopId);
			request.setAttribute("selPro", selPro);

			// 当前登录操作员对象
			LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			request.setAttribute("curSysuser", curSysuser);
			request.setAttribute("lgcorpcode", lgcorpcode);
			//登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			//操作员名称
			String userName = " ";
			if(lfSysuser != null)
			{
				userName = lfSysuser.getUserName();
			}
			//操作日志
			EmpExecutionContext.info(mtModuleName, lgcorpcode, String.valueOf(lguserid), userName, opResult + content.toString(), opType);
		} catch (Exception e)
		{
			//异常回滚
			empTransDao.rollBackTransaction(conn);
			request.setAttribute("editProcessResult", "0");
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);
			EmpExecutionContext.error(e, "下行业务步骤的editReply方法异常。");
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_editMtProcess.jsp").forward(
					request, response);
		}
	}
	
	public void sqlConfig(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		//日志类型：更新
		String opType = StaticValue.UPDATE;
//		日志内容
		String opContent = "步骤管理-数据库配置";

		//步骤id
		String prId = request.getParameter("prId");
		//sql语句
		String sql = request.getParameter("sql");
		//数据库id
		String dbId = request.getParameter("dbId");
		
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");

		try
		{
			sql = sql.trim();
			if (sql.lastIndexOf(";") == sql.indexOf(";")
					&& sql.lastIndexOf(";") == sql.length() - 1)
			{
				//合法化sql
				sql = sql.substring(0, sql.length() - 1);
			}
			//通过id获取步骤
			LfProcess process = baseBiz.getById(LfProcess.class, prId);
			process.setDbId(Long.valueOf(dbId));
			process.setSql(sql);
			//更新步骤信息
			boolean result = baseBiz.updateObj(process);
			if (result)
			{
				//成功日志
				request.setAttribute("sqlConfigResult", "1");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
			} else
			{
				//失败日志
				request.setAttribute("sqlConfigResult", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
			}
		} catch (Exception e)
		{
			request.setAttribute("sqlConfigResult", "0");
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);
			EmpExecutionContext.error(e, "下行业务步骤的sqlConfig方法异常。");
		} finally
		{
			//跳转
			this.toSqlConfig(request, response);
		}
	}

	public void editReply2(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		String opType = StaticValue.UPDATE;
		String opContent = "编辑短信回复模板";

		//步骤id
		String prId = request.getParameter("prId");
		String msgLoopId = request.getParameter("msgLoopId");
		String msgMain = request.getParameter("msgMain");
		
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");

		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("prId", prId);
			List<LfReply> replyList = baseBiz.getByCondition(LfReply.class,
					conditionMap, null);
			LfReply reply;
			if (replyList == null || replyList.size() == 0)
			{
				reply = new LfReply();
			} else
			{
				reply = replyList.get(0);
			}
			if (!"".equals(msgLoopId.trim()) && msgLoopId != null)
			{
				reply.setMsgLoopId(Long.valueOf(msgLoopId));
			}
			reply.setMsgMain(msgMain.trim());
			if ("".equals(msgLoopId.trim()) && reply.getReId() != null)
			{
				//删除操作
				baseBiz.deleteByIds(LfReply.class, reply.getReId().toString());
				reply.setReId(null);
				reply.setMsgLoopId(null);
			}
			boolean result;
			if (reply.getReId() == null)
			{
				reply.setPrId(Long.valueOf(prId));
				//新增操作
				result = baseBiz.addObj(reply);
			} else
			{
				//更新操作
				result = baseBiz.updateObj(reply);
			}
			if (result)
			{
				//保存成功日志
				request.setAttribute("editReplyResult", "4");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
			} else
			{
				//保存失败日志
				request.setAttribute("editReplyResult", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
			}
		} catch (Exception e)
		{
			//保存异常日志
			request.setAttribute("editReplyResult", "0");
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);
			EmpExecutionContext.error(e, "下行业务步骤的editReply2方法异常。");
		} finally
		{
			//跳转
			this.toSmsTemp(request, response);
		}
	}

	public void previewSms(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		//预览信息
		FetchSendMsgBiz fsmBiz = new FetchSendMsgBiz();
		// 当前登录企业
		//String lgcorpcode = request.getParameter("lgcorpcode");

		try
		{
			//步骤id
			Long prId = Long.valueOf(request.getParameter("prId"));
			List<Map<String, String>> resultList = fsmBiz
					.findProcessResult(prId);
			if (resultList != null && resultList.size() > 0)
			{
				Map<String, String> resultMap = resultList.get(0);
				//异步返回内容
				response.getWriter().print(resultMap.get("CONTENT") == null ? "" : resultMap
						.get("CONTENT"));
			} else
			{
				//内容为空
				response.getWriter().print("contentIsNull");
			}
		} catch (Exception e)
		{
			//异常处理
			response.getWriter().print("errorSms");
			//失败日志
			/*spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);*/
			EmpExecutionContext.error(e, "下行业务步骤的previewSms方法异常。");
		}
	}

	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//日志操作类型为删除
		String opType = StaticValue.DELETE;
		//步骤名称
		String prName = request.getParameter("prName");
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		//操作内容
		String opContent = "删除步骤（步骤名：" + prName + "）";
		try
		{
			//步骤id
			Long prId = Long.valueOf(request.getParameter("prId"));
			ProcessConfigBiz proBiz = new ProcessConfigBiz();
			//删除步骤
			boolean result = proBiz.delAllAboutProcess(prId);
			if (result)
			{
				//成功日志
				request.setAttribute("result", "5");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
			} else
			{
				//失败日志
				request.setAttribute("result", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
			}
		} catch (Exception e)
		{
//			异常处理，打印到页面
			response.getWriter().print(ERROR);
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);
			EmpExecutionContext.error(e, "下行业务步骤的delete方法异常。");
		} finally
		{
			this.find(request, response);
		}
	}
	
	/**
	 * 获取短信模板内容
	 * @param request
	 * @param response
	 */
	public void getTmMsg(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		//模板id
		String tmId=request.getParameter("tmId");
		try
		{
			writer = response.getWriter();
			if("".equals(tmId))
			{
				writer.print("");
			}else
			{
				LfTemplate template=baseBiz.getById(LfTemplate.class, tmId);
				//异步返回模板内容
				writer.print(template.getTmMsg());
			}
		}catch (Exception e)
		{
			if(writer!=null){
			writer.print("");
			}
			EmpExecutionContext.error(e, "下行业务步骤的getTmMsg方法异常。");
		}finally
		{
			if(writer!=null){
			writer.flush();
			writer.close();
			}
		}
	}

	/**
	 * 跳转到新增数据源页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toAddDB(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		//跳转到新增数据源页面
		request.getRequestDispatcher(this.empRoot + this.basePath + "/eng_addDBConf.jsp").forward(request, response);
	}
	
	/**
	 * 测试连接
	 * @param request
	 * @param response
	 */
	public void testConnection(HttpServletRequest request,
			HttpServletResponse response)
	{
		//测试连接是否正常
		LfDBConnect dbConn = new LfDBConnect();
		//数据库连接类型
		String dbConnType = request.getParameter("dbConnType");
		//数据库名称
		String dbName = request.getParameter("dbName");
		//用户名
		String dbUser = request.getParameter("dbUser");
		//密码
		String dbPwd = request.getParameter("dbPwd");
		//端口号
		String port = request.getParameter("port");
		
		//数据库类型
		String dbType = request.getParameter("dbType");
		//ip地址
		String dbconIp = request.getParameter("dbconIp");

		dbConn.setDbUser(dbUser);
		dbConn.setDbPwd(dbPwd);
		dbConn.setDbName(dbName);
		dbConn.setPort(port);
		dbConn.setDbType(dbType);
		dbConn.setDbconIP(dbconIp);
		if (dbType.equals("Oracle"))
		{
			dbConn.setDbconnType(Integer.parseInt(dbConnType));
		}
		try
		{
			ProcessConfigBiz proBiz = new ProcessConfigBiz();
			response.getWriter().print(proBiz.testConnection(dbConn));
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"测试连接异常！");
		}
	}
	
	/**
	 * 验证连接名称是否重复
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void checkDbName(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		//数据库名称
		String conName = request.getParameter("conName").trim();
		//当前登录企业
		//String lgcorpcode = request.getParameter("lgcorpcode");
		
		try
		{
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务检查DB配置，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			//Long lguserid = curUser.getUserId();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			
			//存在表示符
			boolean exists = false;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("dbconName", conName);
			conditionMap.put("corpCode", lgcorpcode);
			List<LfDBConnect> dbList = baseBiz.getByCondition(
					LfDBConnect.class, conditionMap, null);
			//验证名称重复
			if (dbList != null && dbList.size() > 0)
			{
				exists = true;
			}
			//异步返回结果
			response.getWriter().print(exists);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"验证连接名称是否重复异常！");
		}
	}
	
	/**
	 * 新增数据源配置
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addDB(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		//数据源名称
		String dbconName = request.getParameter("dbconName");
		//描述
		String comments = request.getParameter("comments");
//		端口号
		String port = request.getParameter("port");
//		数据库类型
		String dbType = request.getParameter("dbType");
//		数据库id地址
		String dbconIP = request.getParameter("dbconIp");
//		连接类型
		String dbConnType = request.getParameter("dbConnType");
		//用户名
		String dbName = request.getParameter("dbName");
		String dbUser = request.getParameter("dbUser");
		//密码
		String dbPwd = request.getParameter("dbPwd");
		
		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("下行业务新建DB配置，session获取当前登录操作员对象为空。");
			return;
		}
		// 当前登录操作员id
		//Long lguserid = curUser.getUserId();
		// 当前登录企业
		String lgcorpcode = curUser.getCorpCode();
		//当前登录企业
		//String lgcorpcode = request.getParameter("lgcorpcode");
		String corpCode = curUser.getCorpCode();

		LfDBConnect dbConn = new LfDBConnect();
		HttpSession session = request.getSession(ZnyqParamValue.GET_SESSION_FALSE);
		try
		{
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			String opUser="";
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, Long.parseLong(lguserid));
			opUser = sysuser==null?"":sysuser.getUserName();
			dbConn.setComments(comments);
			dbConn.setDbconIP(dbconIP);
			dbConn.setDbconName(dbconName);
			dbConn.setDbUser(dbUser);
			dbConn.setDbPwd(dbPwd);
			dbConn.setDbName(dbName);
			dbConn.setPort(port);
			dbConn.setDbType(dbType);
			dbConn.setCorpCode(corpCode);
			String opType = StaticValue.ADD;
			//保存新增日志
			String opContent = "新建数据库连接（数据源名称：" + dbConn.getDbconName() + "）";

			if (dbType.equals("Oracle"))
			{
				//设置数据库类型
				dbConn.setDbconnType(Integer.parseInt(dbConnType));
			}
			boolean result = false;
			ProcessConfigBiz proBiz = new ProcessConfigBiz();
			result = proBiz.addDBConnect(dbConn);
			//操作结果
			String opResult = "";
			if (result)
			{
				//保存成功
				session.setAttribute("result1", "1");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				opResult = "新建数据源成功。";

			} else
			{
				session.setAttribute("result1", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
				opResult = "新建数据源失败 。";
			}
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[数据源名称，端口，数据库地址，数据库类型，连接类型，服务/实例名，用户名](")
			.append(dbconName).append("，").append(port).append("，").append(dbconIP).append("，").append(dbType).append("，")
			.append(dbConnType).append("，").append(dbName).append("，").append(dbUser).append(")");
			//操作日志
			EmpExecutionContext.info(moModuleName, corpCode, lguserid, opUser, opResult + content, opType);
			
			request.getRequestDispatcher(this.empRoot + this.basePath + "/eng_addDBConf.jsp")
			.forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"智能引擎新增数据源配置异常！");
		}
	}
	
	/**
	 * 获取数据库集合
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getdb(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务获取DB配置，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			//Long lguserid = curUser.getUserId();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			
			//当前登录企业
			//String lgcorpcode = request.getParameter("lgcorpcode");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			List<LfDBConnect> dbConnsList = baseBiz.getByCondition(LfDBConnect.class, conditionMap, null);
			if(dbConnsList == null || dbConnsList.size() == 0){
				//异步返回结果
				response.getWriter().print("nodata");
			}else{
				//异步返回结果
				response.getWriter().print(dbConnsList.toString());
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"智能引擎获取数据库连接集合异常。");
			//异步返回结果
			response.getWriter().print("errdb");
		}
	}
	
	/**
	 * 短信模板新建页面跳转方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toAddSmsTmp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher(this.empRoot  + this.basePath  + "/eng_addSmsTmpl.jsp")
		.forward(request, response);
		
	}
	
	/**
	 * 短信模板新建或修改
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addSmsTmpl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
		try
		{
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务新建短信模板，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			//Long lguserid = curUser.getUserId();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			
			LfTemplate temp = new LfTemplate();
			//当前登录企业
			//String lgcorpcode = request.getParameter("lgcorpcode");
			
			//模板名称
			String tmName = request.getParameter("tmName");
			//模板编号
			String tmCode = request.getParameter("tmCode");
			//模板内容
			String tmMsg = request.getParameter("tmMsg").trim();
			//模板状态
			Long tmState = Long.valueOf(request.getParameter("tmState"));
			Long dsflag = Long.valueOf(request.getParameter("dsflag"));
			
			//当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			//漏洞修复 session里获取操作员信息
			Long lguserid = SysuserUtil.longLguserid(request);


			//特殊符号发送回乱码，特殊处理下
			if(tmMsg!=null && tmMsg.length()>0)
			{
				tmMsg = tmMsg.replaceAll("•", "·").replace("¥", "￥");
			}
			
			if(tmMsg.length() > 990)
			{
				tmMsg = tmMsg.substring(0,990);
			}
			temp.setTmpType(3);
			temp.setTmName(tmName);
			temp.setTmCode(tmCode==null?"":tmCode.trim());
			temp.setTmState(tmState);
			temp.setDsflag(dsflag);
			temp.setCorpCode(lgcorpcode);
			
			if(dsflag==3)
			{
				temp.setBizCode(request.getParameter("cwcode"));
			}
			//新增模板
		
			//判断从哪发出的请求，为1是发送模块,其余说明是从短信模板管理
			String fromState=request.getParameter("fromState");
			request.setAttribute("fromState", fromState);
			String opType = StaticValue.ADD;
			String opContent = "新建短信模板（模板名称："+temp.getTmName()+"）";
			
			ProcessConfigBiz proBiz = new ProcessConfigBiz();
			temp.setTmMsg(proBiz.changeParam(tmMsg));
			temp.setUserId(lguserid);
			temp.setIsPass(-1);
			long addTemp = proBiz.addTemplate(temp);
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			String userName = sysuser==null?"":sysuser.getUserName();
			//操作结果
			String opResult = "";
			if(addTemp > 0)
			{
				//新增成功
				request.setAttribute("tmresult", "true");
				//添加操作成功日志
				spLog.logSuccessString(userName, opModule, opType, opContent,lgcorpcode);
				opResult = "新建短信模板成功。";
			}else if(addTemp == -1)
			{
				request.setAttribute("tmresult", "noFlower");
				//添加操作失败日志
				spLog.logFailureString(userName, opModule, opType, opContent+opSper,null,lgcorpcode);
				opResult = "新建短信模板失败。";
			}else
			{
				//新增失败
				request.setAttribute("tmresult", "false");
				//添加操作失败日志
				spLog.logFailureString(userName, opModule, opType, opContent+opSper,null,lgcorpcode);
				opResult = "新建短信模板失败。";
			}
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[模板名称，模板类型，模板内容，模板状态](")
			.append(tmName).append("，").append(dsflag).append("，").append(tmMsg).append("，").append(tmState).append(")");
			//操作日志
			EmpExecutionContext.info(moModuleName, lgcorpcode, String.valueOf(lguserid), userName, opResult + content, opType);
		}catch(EMPException ex)
		{
			ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
			String desc = info.getErrorInfo(ex.getMessage());
			request.setAttribute("tmresult", desc);
			EmpExecutionContext.error(ex,"短信模板新建异常！");
		}catch(Exception e)
		{
			request.setAttribute("tmresult", "error");
			EmpExecutionContext.error(e,"短信模板新建异常！");
		}finally
		{
			request.getRequestDispatcher(this.empRoot + this.basePath +"/eng_addSmsTmpl.jsp")
				.forward(request, response);
		}
		
	}
	
	/**
	 *  检查编码是否重复
	 * @param request
	 * @param response
	 */
	public void checkRepeat(HttpServletRequest request, HttpServletResponse response)throws IOException{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		String tmCode=request.getParameter("tmCode");
		String corpCode=request.getParameter("corpCode");
		tmCode=tmCode==null?"":tmCode.trim();
		conditionMap.put("tmCode", tmCode); 
		conditionMap.put("corpCode", corpCode);
		//添加必要类型
		conditionMap.put("tmpType", "3");
		//状态为启用
		try {
			List<LfTemplate> tempList = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
			if(tempList!=null&&tempList.size()>0){
				response.getWriter().print("repeat");
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"检查编码是否重复异常！");
			//异步返回结果
			response.getWriter().print("");
		}
		
	}
	
	/**
	 * 获取模板
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getSmsTmpl(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务获取短信模板，session获取当前登录操作员对象为空。");
				response.getWriter().print("errtmpl");
				return;
			}
			// 当前登录操作员id
			Long lguserid = curUser.getUserId();
			// 当前登录企业
			//String lgcorpcode = curUser.getCorpCode();
			//当前登录企业
			/*String lguserid = request.getParameter("lguserid");
			if(lguserid == null || lguserid.length() == 0){
				response.getWriter().print("errtmpl");
				return;
			}*/
			
			//查询条件
			LfTemplate templCon = new LfTemplate();
			// 3短信模板；4彩信模板
			templCon.setTmpType(3);
			// 模板状态：0无效；1有效
			templCon.setTmState(1L);
			// 无需审核或审核通过
			templCon.setIsPass(5);
			// 0静态 1动态 2智能抓取模板
			String dsflag = request.getParameter("dsflag");
			if(dsflag == null || dsflag.length() == 0){
				// 0静态1动态2智能抓取模板
				dsflag = "0,1,2";
			}
			List<LfTemplate> tmpList = templBiz.getTemplateByCondition(templCon, 1, lguserid, dsflag, null);
			
			if(tmpList == null || tmpList.size() == 0){
				//异步返回结果
				response.getWriter().print("nodata");
			}else{
				//异步返回结果
				response.getWriter().print(tmpList.toString());
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"智能引擎获取模板集合异常。");
			//异步返回结果
			response.getWriter().print("errtmpl");
		}
	}
	
	/**
	 *   查询网讯模板
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void toWxTmpl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try {
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务跳转到网讯模板，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			
			/*String lgcorpcode = request.getParameter("lgcorpcode");
			String lguserid = request.getParameter("lguserid");*/
			
			if(lgcorpcode == null || lgcorpcode.length() == 0 || lguserid == null || lguserid.length() == 0){
				
				EmpExecutionContext.error("智能引擎获取网讯模板，参数错误。" 
						+ "lgcorpcode:" + lgcorpcode
						+ "lguserid:" + lguserid);
				return;
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			String name = request.getParameter("name");
			if(name != null && name.length() > 0){
				conditionMap.put("name", name);
			}
			conditionMap.put("corpCode", lgcorpcode);
			conditionMap.put("creatId", lguserid);
			conditionMap.put("status", "2");
			//使用当前时间
			conditionMap.put("timeOut", String.valueOf(System.currentTimeMillis()));
			//使用静态的模板
			conditionMap.put("tempType", "1");
			
			//网讯是否运营商商审核 0表示运营商不审核，1表示运营商审核
			if(StaticValue.getIsWxOperatorReview() ==1){
				//运营商审核通过
				conditionMap.put("operAppStatus", "1");
			}
		
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo, request);
			pageInfo.setPageSize(10);
			List<DynaBean> wxTmplsList = new ProcessConfigBiz().getNetTemplate(conditionMap, pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("baseInfos", wxTmplsList);
			request.setAttribute("conditionMap", conditionMap);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取网讯模板信息异常!");
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("error", e);
		}finally{
			request.getRequestDispatcher(this.empRoot + this.basePath + "/eng_wxTmpl.jsp")
			.forward(request, response);
		}
	}


	/**
	 * 获取通道信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getSmsContentMaxLen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String info = "infos:";
		try {
			//业务id密文
			String serIdCipher = request.getParameter("serId"); 
			
			//解密，获取明文业务id
			String serId = decryptionParam(serIdCipher, request);
			//解密失败，返回了null
			if(serId == null)
			{
				EmpExecutionContext.error("上行业务删除，解密serId失败。密文：" + serIdCipher);
				response.getWriter().print(info);
				return;
			}
			
			info = proBiz.getSmsContentMaxLen(serId);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取发送账户绑定的通道信息异常！");
		}finally{
			response.getWriter().print(info);
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
	
}
