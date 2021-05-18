package com.montnets.emp.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import com.montnets.emp.common.atom.AddrBookAtom;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.HttpUtil;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.biz.SpUserBiz;
import com.montnets.emp.common.biz.TemplateBiz;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sms.LfBigFile;
import com.montnets.emp.entity.sms.LfDrafts;
import com.montnets.emp.entity.sms.LfSubDrafts;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.query.biz.SysMtRecordBiz;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.TxtFileUtil;

@SuppressWarnings("serial")
public class CommonSvt extends BaseServlet
{

	private final WgMsgConfigBiz	msgConfigBiz	= new WgMsgConfigBiz();

	private final PhoneUtil		phoneUtil		= new PhoneUtil();

	private final BaseBiz			baseBiz			= new BaseBiz();
	
	private final CommonBiz commonBiz = new CommonBiz();

	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		EmpExecutionContext.error("进入CommonSvt的find方法:"+request.getRequestURL()+"?"+request.getQueryString()+";"+request.getParameter("method"));
	}
	
	/**
	 * 检测文件是否存在
	 * 
	 * @param request
	 * @param response
	 */
	public void checkFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        boolean result = false;
		String url = request.getParameter("url");
		TxtFileUtil tfu = new TxtFileUtil();
        String httpUrl = StaticValue.OUTERURL;
        //文件是否上传文件服务器
        String upload = request.getParameter("upload");
        //有此参数标示 认为不上传文件服务器
        boolean isUpload = (upload == null);
		try
		{
			
            //非集群判断本地文件存在
            if(StaticValue.getISCLUSTER() == 0 || !isUpload)
            {
                //本地是否存在该文件
                result = tfu.checkFile(url);
            }//帮助手册从本地下载
            else if("common/file/help.zip".equals(url)){
				result = tfu.checkFile(url);
			}//集群 优先从文件服务器获取
            else{
                String[] httpUrls = CommonBiz.getALiveFileServer();
                if(httpUrls != null)
                {
                    httpUrl = httpUrls[0];
                    if(HttpUtil.checkState(httpUrl+url) < 400)
                    {
                        result = true;
                        //url加上标示 表示 来源文件服务器节点
                        httpUrl = "@"+httpUrl;
                        return;
                    }
                }

                httpUrl = commonBiz.checkFile(url);
                if(httpUrl != null)
                {
                    result = true;
                }
            }
		}
		catch (Exception e)
		{
            httpUrl = "";
			// 异常处理
			EmpExecutionContext.error(e, "检测文件是否存在异常。");
		}
        finally
        {
            if(!result || !isUpload)
            {
                httpUrl = "";
            }
            out.print(result+httpUrl);
        }
    }

	
	/**
	 * 检测文件是否存在(外网地址)
	 * 
	 * @param request
	 * @param response
	 */
	public void checkFileOuter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        boolean result = false;
		String url = request.getParameter("url");
		TxtFileUtil tfu = new TxtFileUtil();
        String httpUrl = StaticValue.OUTERURL;
        //文件是否上传文件服务器
        String upload = request.getParameter("upload");
        //有此参数标示 认为不上传文件服务器
        boolean isUpload = (upload == null);
		try
		{
            //非集群判断本地文件存在
            if(StaticValue.getISCLUSTER() == 0 || !isUpload)
            {
                //本地是否存在该文件
                result = tfu.checkFile(url);
            }//集群，从文件服务器获取
            else
            {
                httpUrl = CommonBiz.getALiveFileServerOut(url);
                if(httpUrl != null)
                {
                    result = true;
                    //url加上标示 表示 来源文件服务器节点
                    httpUrl = "@"+httpUrl;
                    return;
                }
            }
		}
		catch (Exception e)
		{
            httpUrl = "";
			// 异常处理
			EmpExecutionContext.error(e, "检测文件是否存在异常。");
		}
        finally
        {
            if(!result || !isUpload)
            {
                httpUrl = "";
            }
            EmpExecutionContext.error("文件服务器地址："+result+httpUrl);
            out.print(result+httpUrl);
        }
    }

	
	// 获取当前系统时间（断库断网断服务器版）
	public void getServerTime1(HttpServletRequest request, HttpServletResponse response)
	{
		String serverTime = null;
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = new Date();
			serverTime = format.format(date);
		}
		catch (Exception e)
		{
			serverTime = "error";
			EmpExecutionContext.error(e, "获取当前系统时间异常。");
		}
		finally
		{
			if(writer != null){
				writer.print("@" + serverTime);
			}
		}
	}

	/**
	 * 检查关键字
	 * 
	 * @param request
	 * @param response
	 */
	public void checkBadWord1(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		String tmMsg = request.getParameter("tmMsg");
		String corpCode = request.getParameter("corpCode");
		String words = new String();
		try
		{
			writer = response.getWriter();
			KeyWordAtom keyWordAtom = new KeyWordAtom();
			words = keyWordAtom.checkText(tmMsg, corpCode);
			//内容为空，记录请求日志
			if("error".equals(words))
			{
				EmpExecutionContext.logRequestUrl(request, "后台请求");
			}
		}
		catch (Exception e)
		{
			words = "error";
			EmpExecutionContext.error(e, "检查关键字异常。tmMsg:"+tmMsg+",corpCode:"+corpCode);
		}
		finally
		{
			if(writer != null)
			{
				writer.print("@" + words);
				writer.flush();
				writer.close();
			}
		}
	}

	// 获取当前系统时间
	public void getServerTime(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String serverTime = format.format(date);
		PrintWriter writer = response.getWriter();
		writer.print(serverTime);
	}

	/**
	 * 过滤号段
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void filterPh(HttpServletRequest request,

	HttpServletResponse response) throws Exception
	{
		String[] haoduan = msgConfigBiz.getHaoduan();
		String tmp = request.getParameter("tmp");
		PrintWriter out = response.getWriter();
		//合法性校验,返回-1为非法号码
		if(phoneUtil.getPhoneType(tmp, haoduan) + 1 == 0)
		{
			out.print("false");
		}
		else
		{
			out.print("true");
		}
	}

	public void getTempMsg(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String result = null;
		// 模板id
		String tmId = request.getParameter("tmId");
		try
		{
			if("".equals(tmId))
			{
				result = "";
			}
			else
			{
				// 根据id查询模板记录
				LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
				result = template.getTmMsg();
			}
		}
		catch (Exception e)
		{
			result = "error";
			EmpExecutionContext.error(e, "获取模板信息异常。");
		}
		finally
		{
			// 异步返回操作结果
			response.getWriter().print("@" + result);
		}
	}

	public void getLfTemplateBySms(HttpServletRequest request, HttpServletResponse response)
	{

		// 查询条件vo
		PageInfo pageInfo = new PageInfo();
		//企业编码
		String lgcorpcode = "";
		//用户ID
		String strlguserid = "";
		Long lguserid = 0L;
		try
		{
			pageSet(pageInfo, request);
			// 模板名称
			String tmName = request.getParameter("tmName");
			// 模板内容
			String tmMsg = request.getParameter("tmMsg");
			//企业编码
			lgcorpcode = request.getParameter("lgcorpcode");
			// 当前登录用户id
			//strlguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			strlguserid = SysuserUtil.strLguserid(request);

			//获取参数异常，从session中获取
			if(lgcorpcode == null || "undefined".equals(lgcorpcode) || strlguserid == null || "undefined".equals(strlguserid))
			{
				EmpExecutionContext.error("获取模板请求参数异常！lgcorpcode=" + lgcorpcode + "，lguserid=" + strlguserid);
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				lgcorpcode = lfSysuser.getCorpCode();
				lguserid = lfSysuser.getUserId();
			}
			else
			{
				lguserid = Long.valueOf(strlguserid);
			}
			if(tmName != null)
			{
				tmName = tmName.trim();
			}
			if(tmMsg != null)
			{
				tmMsg = tmMsg.trim();
			}
			tmName = (tmName == null ? "" : tmName);
			tmMsg = (tmMsg == null ? "" : tmMsg);
			LfTemplate template=new LfTemplate();
			template.setTmName(tmName);
			template.setTmMsg(tmMsg);
			template.setCorpCode(lgcorpcode);
			template.setTmpType(3);
			template.setTmState(1L);
			template.setIsPass(5);//ispass为5 等效于 isPass in (0,1)
			String dsflag=request.getParameter("dsflag");
			int templType=1;//1为短信 2为彩信
			// 条件查询结果集
			List<LfTemplate> temList = new TemplateBiz().getTemplateByCondition(template, templType, lguserid,dsflag, pageInfo);
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("temList", temList);
			request.getRequestDispatcher("common/ssm_smsTemplate.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("获取模板请求URL:" +request.getRequestURI() + "；lgcorpcode:" + lgcorpcode + "，strlguserid：" + strlguserid);
			EmpExecutionContext.error(e, "svt方法getLfTemplateBySms异常。");
		}
	}
	public void getLfBigFileBySms(HttpServletRequest request, HttpServletResponse response)
    {
        // 查询条件vo
        PageInfo pageInfo = new PageInfo();
        //企业编码
        String lgcorpcode = "";
        //用户ID
        String strlguserid = "";
        Long lguserid = 0L;
        try
        {
            pageSet(pageInfo, request);
           
            String id = request.getParameter("id");
            String starttime = request.getParameter("sendtime");           
            String endtime = request.getParameter("recvtime");           
            //企业编码
            lgcorpcode = request.getParameter("lgcorpcode");
            // 当前登录用户id
            strlguserid = SysuserUtil.strLguserid(request);
            LfSysuser lfSysuser =null;
            //获取参数异常，从session中获取
            if(lgcorpcode == null || "undefined".equals(lgcorpcode) || strlguserid == null || "undefined".equals(strlguserid))
            {
                EmpExecutionContext.error("获取大文件请求参数异常！lgcorpcode=" + lgcorpcode + "，lguserid=" + strlguserid);
            }
            else
            {
                lguserid = Long.valueOf(strlguserid);
            }
            lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            lgcorpcode = lfSysuser.getCorpCode();
            lguserid = lfSysuser.getUserId();
            LinkedHashMap<String,String> conditionMap =new LinkedHashMap<String, String>();
            if(id != null)
            {
                id = id.trim();
                conditionMap.put("id", id);
            }
            if(starttime != null)
            {
                starttime = starttime.trim();                
                conditionMap.put("createTime&>=", starttime);
            }
            
            if(endtime != null)
            {
                endtime = endtime.trim();
                conditionMap.put("createTime&<=", endtime);
            }
              
            //个人权限
            if (lfSysuser.getPermissionType() == 1) {
                conditionMap.put("userId", lfSysuser.getUserId().toString());
            }else {
                //设置有权限看的操作员编码。不需要考虑权限则是空字符串，目前只有admin和管辖范围是顶级机构不需要考虑权限
                String usercode =  new SysMtRecordBiz().getPermissionUserId(lfSysuser);
                
                //1000个人以上，或者查询异常
                if(usercode == null)
                {
                    conditionMap.put("userId", lfSysuser.getUserId().toString());
                }
                else if("".equals(usercode))
                {
                    //admin和管辖范围是顶级机构不需要考虑权限
                }
                else
                {
                    //conditionMap.put("userId&in", usercode);
                    conditionMap.put("userId&in", usercode.replace("'", ""));

                }
                
            }
            
            conditionMap.put("handleStatus", "2");
            conditionMap.put("effNum&>", "0");
            conditionMap.put("fileStatus", "1");
            
            conditionMap.put("corpCode", lgcorpcode);
            LinkedHashMap<String, String> orderbyMap= new LinkedHashMap<String, String>();    
            orderbyMap.put("id", "DESC");
            
            // 条件查询结果集
            List<LfBigFile> bigfileList = new BaseBiz().getByCondition(LfBigFile.class, null, conditionMap, orderbyMap, pageInfo);
            
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("bigfileList", bigfileList);
            request.getRequestDispatcher("common/ssm_bigfile.jsp").forward(request, response);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "svt方法getLfBigFileBySms异常。"+"获取模板请求URL:" +request.getRequestURI() + "；lgcorpcode:" + lgcorpcode + "，strlguserid：" + strlguserid);
        }
    }
	public void getSubNo(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String corpCode = request.getParameter("lgcorpcode");
		String guid = request.getParameter("lgguid");
		String spUser = request.getParameter("spUser");
		String isReply = request.getParameter("isReply");
		String circleSubNo = request.getParameter("circleSubNo");
		String taskId = request.getParameter("taskId");
		String subNo = "";
		LfSubnoAllotDetail subnoAllotDetail = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			if("0".equals(isReply))
			{
				// 0表示不需要回复
				subNo = "";
			}
			else
				if("1".equals(isReply))
				{
					// 1表示 本次任务，需要回复
					// 获取循环尾号，避免产生大量没有使用的尾号，只获取一次
					if(circleSubNo != null && !"".equals(circleSubNo))
					{
						subNo = circleSubNo;
					}
					else
					{
						SMParams smParams = new SMParams();
						// 编码（0模块编码1业务编码2产品编码3机构id4操作员guid,5任务id）
						smParams.setCodes(taskId.toString());
						// 编码类别
						smParams.setCodeType(5);
						smParams.setCorpCode(corpCode);
						// (分配类型0固定1自动有效期7天，null表是不设有效期)
						smParams.setAllotType(1);
						// 尾号是否确定插入表
						smParams.setSubnoVali(false);
						smParams.setTaskId(Long.parseLong(taskId));
						smParams.setLoginId(guid);
						ErrorCodeParam errorCodeParam = new ErrorCodeParam();
						subnoAllotDetail = GlobalVariableBiz.getInstance().getSubnoDetail(smParams, errorCodeParam);
						if(errorCodeParam.getErrorCode() != null && "EZHB237".equals(errorCodeParam.getErrorCode()))
						{
							// 没有可用的尾号（尾号已经用完）
							response.getWriter().print("noUsedSubNo");
							return;
						}
						else
							if(errorCodeParam.getErrorCode() != null && "EZHB238".equals(errorCodeParam.getErrorCode()))
							{
								// 获取尾号失败
								response.getWriter().print("noSubNo");
								return;
							}
						subNo = subnoAllotDetail != null ? subnoAllotDetail.getUsedExtendSubno() : null;
						if(subNo == null || "".equals(subNo))
						{
							// 获取尾号失败
							response.getWriter().print("noSubNo");
							return;
						}
					}
				}
				else
					if("2".equals(isReply))
					{
						// 2表示我的尾号（操作员固定尾号）
						// 获取操作员固定尾号
						conditionMap.put("loginId", guid);
						conditionMap.put("corpCode", corpCode);
						conditionMap.put("menuCode&is null", "isnull");
						// conditionMap.put("taskId&is null", "isnull");
						conditionMap.put("busCode&is null", "isnull");
						List<LfSubnoAllot> allots = baseBiz.getByCondition(LfSubnoAllot.class, conditionMap, null);
						LfSubnoAllot allot = allots != null && allots.size() > 0 ? allots.get(0) : null;
						if(allot == null || allot.getUsedExtendSubno() == null || "".equals(allot.getUsedExtendSubno()))
						{
							// 获取尾号失败
							response.getWriter().print("noSubNo");
							return;
						}
						subNo = allot.getUsedExtendSubno();
					}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "svt获取尾号异常。");
		}
		conditionMap.clear();
		conditionMap.put("userId", spUser);
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		orderByMap.put("spisuncm", StaticValue.ASC);
		JSONObject jsonObject = null;
		int strLen = 0;
		String yd = "true";
		String lt = "true";
		String dx = "true";
		try
		{
			List<GtPortUsed> gtPortUseds = baseBiz.getByCondition(GtPortUsed.class, conditionMap, orderByMap);
			GtPortUsed gtPortUsed = null;
			jsonObject = new JSONObject();
			for (int i = 0; i < gtPortUseds.size(); i++)
			{
				gtPortUsed = gtPortUseds.get(i);
				// 拓展尾号长度
				int cpnoLen = gtPortUsed.getCpno() != null ? gtPortUsed.getCpno().trim().length() : 0;
				// 通道号+拓展尾号+尾号 的长度
				strLen = gtPortUsed.getSpgate().length() + cpnoLen + subNo.length();
				// 判断各运营商的通道号+拓展尾号+尾号是否大于20，如果大于20
				// 则前台需提示XX运营商通道号+尾号长度大于20，不允许发送
				if(gtPortUsed.getSpisuncm() == 0 && strLen > 20)
				{
					yd = "false";
				}
				else
					if(gtPortUsed.getSpisuncm() == 1 && strLen > 20)
					{
						lt = "false";
					}
					else
						if(gtPortUsed.getSpisuncm() == 21 && strLen > 20)
						{
							dx = "false";
						}

			}
			// 是否可以进行发送的标志
			jsonObject.put("sendFlag", yd + "&" + lt + "&" + dx);
			// 尾号
			jsonObject.put("subNo", subNo);
			response.getWriter().print(jsonObject.toString());
		}
		catch (Exception e)
		{
			response.getWriter().print("error");
			EmpExecutionContext.error(e, "svt获取尾号异常。");
		}
	}

	/**
	 * 检测文件是否存在
	 * 
	 * @param request
	 * @param response
	 */
	public void goToFile(HttpServletRequest request, HttpServletResponse response)
	{
		String url = request.getParameter("url");
		TxtFileUtil tfu = new TxtFileUtil();
		try
		{
			response.getWriter().print(tfu.checkFile(url));
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "svt检测文件是否存在异常。");
		}
	}

	/**
	 * 设置发送时需要用到的计费信息及usercode参数
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void setSendInfoMap(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		HttpSession session = request.getSession(false);
		// 获取当前登录的对象
		LfSysuser sysuser = (LfSysuser) session.getAttribute("loginSysuser");
		Map<String, String> infoMap = new HashMap<String, String>();
		// 用户编码
		infoMap.put("userCode", sysuser.getUserCode());
		// 机构是否计费
		infoMap.put("feeFlag", String.valueOf(SystemGlobals.isDepBilling(sysuser.getCorpCode())));
		// 存放session
		session.setAttribute("infoMap", infoMap);
	}

	/**
	 * 点击选择机构按钮的时候如果包含子机构则获取子机构集合
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getDep(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String depId = request.getParameter("depId");
		String depIdsExist = request.getParameter("depIdsExist");
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		try
		{

			String[] depIds = depIdsExist.split(",");
			StringBuffer depIdsTemp = new StringBuffer();
			for (int i = 0; i < depIds.length; i++)
			{
				if(depIds[i].indexOf("e") > -1)
				{
					depIdsTemp.append(depIds[i].substring(1) + ",");
				}
				else
					if(depIds[i].equals(depId))
					{
						response.getWriter().print("depExist");
						return;
					}
			}
			// 判断新添加的机构是不是已经添加的机构的子机构
			boolean result = isDepAcontainsDepB(depIdsTemp.toString(), depId, lgcorpcode);
			if(result)
			{
				response.getWriter().print("depExist");
				return;
			}
			else
			{
				response.getWriter().print("notExist");
				return;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "svt点击选择机构按钮的时候如果包含子机构则获取子机构集合异常。depId:"+depId
					+",corpcode:"+lgcorpcode+",depIdsExist:"+depIdsExist);
			EmpExecutionContext.logRequestUrl(request, "后台请求");
		}
	}

	// 判断一个机构是否被包含在其它机构
	private boolean isDepAcontainsDepB(String depIdAs, String depIdB, String corpCode)
	{
		boolean result = false;
		List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();
		LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
		String[] depIdAsTemp = depIdAs.split(",");
		try
		{
			for (int a = 0; a < depIdAsTemp.length; a++)
			{
				if(depIdAsTemp[a] != null && !"".equals(depIdAsTemp[a]))
				{
					/*
					 * lfEmployeeDepList = new
					 * GenericLfEmployeeVoDAO().findEmployeeDepsByDepId
					 * (corpCode,depIdAsTemp[a]);
					 */
					lfEmployeeDepList = new DepBiz().findEmpDepsByDepId(corpCode, depIdAsTemp[a]);
					for (int i = 0; i < lfEmployeeDepList.size(); i++)
					{
						depIdSet.add(lfEmployeeDepList.get(i).getDepId());
					}
				}
			}
			result = depIdSet.contains(Long.valueOf(depIdB));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "svt判断一个机构是否被包含在其它机构异常。");
		}
		return result;
	}

	/**
	 * 判断选择的机构是否把其它已经选择的机构包含了
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void isDepsContainedByDepB(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//机构ID
		String depId = "";
		try
		{
			String ismut = request.getParameter("ismut");
			depId = request.getParameter("depId");
			if("0".equals(ismut))
			{
				String countttt = new AddrBookAtom().getEmployeeCountByDepId(depId);
				response.getWriter().print(countttt);
				return;
			}
			List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();
			LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
			String depIdsExist = request.getParameter("depIdsExist");
			String[] depIds = depIdsExist.split(",");
			List<String> list = Arrays.asList(depIds);
			// 将已经存在的机构id放在一个list里面(如果前缀有e就去掉e放在depIdExistList里面)
			List<Long> depIdExistList = new ArrayList<Long>();
			for (int j = 0; j < depIds.length; j++)
			{
				if(depIds[j] != null && !"".equals(depIds[j]))
				{
					if(depIds[j].indexOf("e") > -1)
					{
						if(!"".equals(depIds[j].substring(1)))
						{
							depIdExistList.add(Long.valueOf(depIds[j].substring(1)));
						}
					}
					else
					{
						depIdExistList.add(Long.valueOf(depIds[j]));
					}

				}
			}
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");

			// 查找出要添加的机构的所有子机构，放在一个set里面
			/*
			 * lfEmployeeDepList = new
			 * GenericLfEmployeeVoDAO().findEmployeeDepsByDepId(lgcorpcode,depId);
			 */
			lfEmployeeDepList = new DepBiz().findEmpDepsByDepId(lgcorpcode, depId);
			List<Long> depIdListTemp = new ArrayList<Long>();
			for (int i = 0; i < lfEmployeeDepList.size(); i++)
			{
				depIdSet.add(lfEmployeeDepList.get(i).getDepId());
			}
			// 遍历这个set，看看已经存在的机构是否包含在这个机构的子机构里面，如果包含的话，就重新生成一个option列表的字符串给select控件
			for (int a = 0; a < depIdExistList.size(); a++)
			{
				if(depIdSet.contains(depIdExistList.get(a)))
				{
					depIdListTemp.add(depIdExistList.get(a));
				}
			}
			// 如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
			String depids = depIdSet.toString();
			depids = depids.substring(1, depids.length() - 1);
			// 计算机构人数
			String countttt = new AddrBookAtom().getEmployeeCountByDepId(depids);
			if(depIdListTemp.size() > 0)
			{
				String tempDeps = depIdListTemp.toString();
				tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
				response.getWriter().print(countttt + "," + tempDeps);
				return;
			}
			// 如果没有包含关系
			else
			{
				response.getWriter().print("notContains" + "&" + countttt);
				return;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "判断所选机构在已选机构中是否存在出现异常！depId:" + depId);
		}
	}

	// 改成OA样式树,点击机构只列出该机构下的员工，不管该机构的子机构的员工
	public void getDepAndEmpTree1(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			String epname = request.getParameter("epname");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String depId = request.getParameter("depId");
			StringBuffer sb = new StringBuffer();
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");

			// 有机构则带上机构id
			if(depId != null && !"".equals(depId.trim()))
			{
				conditionMap.put("depId", depId);
			}

			conditionMap.put("corpCode", lgcorpcode);
			if(epname != null && !"".equals(epname.trim()))
			{
				conditionMap.put("name&like", epname);
			}
			// 查询员工列表
			List<LfEmployee> lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);

			// 生成html
			if(lfEmployeeList != null && lfEmployeeList.size() > 0)
			{
				// 获取员工的机构id并存入集合，key为depId
				Map<Long, String> depIdMap = new HashMap<Long, String>();
				for (LfEmployee user : lfEmployeeList)
				{
					depIdMap.put(user.getDepId(), "");
				}

				StringBuffer bufDepId = new StringBuffer();
				// 循环遍历所有机构id，格式为id1,id2,id3
				Set<Long> keys = depIdMap.keySet();
				for (Iterator<Long> it = keys.iterator(); it.hasNext();)
				{
					bufDepId.append(it.next()).append(",");
				}

				String strDepId = null;
				if(bufDepId != null && bufDepId.length() != 0)
				{
					// 截掉多余的,号
					strDepId = bufDepId.substring(0, bufDepId.length() - 1);
				}

				// 查询出机构对象
				conditionMap.clear();
				conditionMap.put("depId&in", strDepId);
				List<LfEmployeeDep> empDepsList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);

				for (LfEmployee user : lfEmployeeList)
				{
					sb.append("<option value='").append("e_" + user.getEmployeeId()).append("' mobile='").append(user.getMobile()).append("'>").append(user.getName().trim());

					for (LfEmployeeDep empDep : empDepsList)
					{
						if(user.getDepId().equals(empDep.getDepId()))
						{
							// 带上机构名称
							sb.append(" [").append(empDep.getDepName()).append("]");
						}
					}

					sb.append("</option>");
				}
			}
			// 异步返回处理
			response.getWriter().print(sb.toString());

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "svt改成OA样式树,点击机构只列出该机构下的员工，不管该机构的子机构的员工异常。");
		}
	}
	
	/**
	 * 优化getDepAndEmpTree1方法，并使用分页查询
	 * @description    
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-5-22 下午01:05:45
	 */
	public void getDepAndEmpTree(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PageInfo pageinfo = new PageInfo();
		// 页码
		String pageIndex1 = request.getParameter("pageIndex1");
		// 区分上一页还是下一页
		String opType = request.getParameter("opType");
		if(opType != null && opType.equals("goNext"))
		{
			pageinfo.setPageIndex(Integer.parseInt(pageIndex1) + 1);
		}
		else
		{
			if(opType != null && opType.equals("goLast"))
			{
				pageinfo.setPageIndex(Integer.parseInt(pageIndex1) - 1);
			}
			else
			{
				pageinfo.setPageIndex(1);
			}
		}
		// 每页显示50条记录
		pageinfo.setPageSize(50);
		String epname = "";
		//机构
		String depId = "";
		// 当前登录企业
		String lgcorpcode = "";
		try
		{
			epname = request.getParameter("epname");
			// 机构
			depId = request.getParameter("depId");
			// 当前登录对象
			LfSysuser lfSysuser = getLoginUser(request);
			// 当前登录操作员企业，从会话中获取
			//lgcorpcode = request.getParameter("lgcorpcode");
			lgcorpcode = lfSysuser.getCorpCode();
			//查询机构下员工信息
			List<DynaBean> lfDepLoyeeList = commonBiz.getDepLoyee(epname, depId, lgcorpcode, pageinfo);
			// 生成html
			StringBuffer sb = new StringBuffer();
			if(lfDepLoyeeList != null && lfDepLoyeeList.size() > 0)
			{
				for (DynaBean bean : lfDepLoyeeList)
				{
					sb.append("<option value='").append("e_" + bean.get("employeeid").toString())
					.append("' mobile='").append(bean.get("mobile").toString())
					.append("'>").append(bean.get("name").toString())
					.append(" [").append(bean.get("dep_name").toString()).append("]")
					.append("</option>");
				}
			}
			String pageStr = pageinfo.getTotalRec() + "@" + pageinfo.getTotalPage() + "@" + sb.toString();

			// 异步返回处理
			response.getWriter().print(pageStr);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询机构员工异常！depId:" + depId+"，epname："+epname+"，lgcorpcode：" +lgcorpcode);
		}
	}

	/**
	 * 前端页面日志记录
	 * @param request
	 * @param response
	 */
	public void log(HttpServletRequest request, HttpServletResponse response){
		String info = request.getParameter("info");
		String extinf = request.getParameter("extinf");
		EmpExecutionContext.info((StringUtils.isBlank(extinf)?"requestUrl":extinf)+" "+info);
	}
	
	/**
	 * 前端页面日志信息
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-10-8 下午02:17:08
	 */
	public void frontLog(HttpServletRequest request, HttpServletResponse response)
	{
		String info = request.getParameter("info");
		if(info == null || "null".equals(info))
		{
			info = "刷新或关闭浏览器";
		}
		EmpExecutionContext.info("<前端日志>："+info);
	}

    /**
     * 设置发送界面 贴尾内容
     * @param request
     * @param response
     * @throws IOException
     */
    public void setTailInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String busCode = request.getParameter("busCode");
        String spUser = request.getParameter("spUser");
        String lgcorpcode = request.getParameter("lgcorpcode");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        String tailcontents = new SmsSendBiz().getTailContents(busCode,spUser,lgcorpcode);
        if(tailcontents != null){
            jsonObject.put("status","1");
            jsonObject.put("contents",tailcontents);
        }else{
            jsonObject.put("status","0");
        }
        out.print(jsonObject.toString());
    }

    /**
     * 获取草稿箱列表
     * @param request
     * @param response
     */
    public void getDrafts(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        PageInfo pageInfo = new PageInfo();
        List<DynaBean> list = new ArrayList<DynaBean>();
        //企业编码
        String lgcorpcode = null;
        //用户id
        String lguserid = null;
        //是否来自短链
        String fromShortUrl = null;
        try
        {
            pageSet(pageInfo, request);
            // 发送主题
            String taskName = request.getParameter("taskname");
            // 内容
            String msg = request.getParameter("msg");
            // 开始时间
            String starttime = request.getParameter("starttime");
            // 结束时间
            String endtime = request.getParameter("endtime");
            //草稿类型
            String draftstype = request.getParameter("draftstype");

            // 是否来自短链的草稿箱
            fromShortUrl = request.getParameter("shorturl");

            LfSysuser sysuser = getLoginUser(request);

            lgcorpcode = sysuser.getCorpCode();

            lguserid = sysuser.getUserId()+"";

            LinkedHashMap<String,String> condMap = new LinkedHashMap<String, String>();
            //企业编码
            condMap.put("corpcode",lgcorpcode);
            //用户ID
            condMap.put("userid",lguserid);
            //草稿箱类型
            condMap.put("draftstype",draftstype);
            //任务主题
            if(StringUtils.isNotBlank(taskName)){
                condMap.put("title",taskName.trim());
            }
            //发送内容
            if(StringUtils.isNotBlank(msg)){
                condMap.put("msg",msg.trim());
            }
            //开始时间
            if(StringUtils.isNotBlank(starttime)){
                condMap.put("starttime",starttime);
            }
            //结束时间
            if(StringUtils.isNotBlank(endtime)){
                condMap.put("endtime",endtime);
            }

            if(StringUtils.isEmpty(fromShortUrl) || "null".equals(fromShortUrl)) {
                list = commonBiz.getUserDrafts(condMap,pageInfo);
            } else {
                list = commonBiz.getUserDraftsFromShortUrl(condMap,pageInfo);
            }

        }
        catch (Exception e)
        {
            EmpExecutionContext.error("获取草稿箱请求URL:" +request.getRequestURI() + "；lgcorpcode:" + lgcorpcode + "，strlguserid：" + lguserid);
        }
        finally
        {
            request.setAttribute("fromShortUrl", fromShortUrl);
            request.setAttribute("list", list);
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher("common/draftlist.jsp").forward(request, response);
        }
    }
    
    /**
     * 删除草稿的方法
     * @description    
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException       			 
     * @author tanglili <jack860127@126.com>
     * @datetime 2016-1-20 下午04:16:37
     */
    public void deleteDrafts(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        //草稿箱ID
    	 String result="false";
 		try
 		{
 			//流程ID
 			String id=request.getParameter("sid");

 			String domainId = request.getParameter("domainId");
 			String netUrlId = request.getParameter("netUrlId");

            conn = baseBiz.getConnection();
            baseBiz.beginTransaction(conn);
 			//删除草稿箱
 			Integer count=baseBiz.deleteByIds(conn, LfDrafts.class, id);
 			//删除结果
 			if(count!=null&&count>0){
                int flag = 1;
 			    if(StringUtils.isNotEmpty(domainId) && StringUtils.isNotEmpty(netUrlId)) {
 			        //删除短链子草稿箱
                    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                    conditionMap.put("draftId", id);
                    conditionMap.put("domainId", domainId);
                    conditionMap.put("netUrlId", netUrlId);
                    flag = baseBiz.deleteByCondition(conn, LfSubDrafts.class, conditionMap);
                }

 				result = flag > 0 ? "true" : "false";

 			    if("true".equals(result)) {
 			        baseBiz.commitTransaction(conn);
                } else {
                    baseBiz.rollBackTransaction(conn);
                }

 			}
 		}
 		catch (Exception e)
 		{
 			result="false";
            baseBiz.rollBackTransaction(conn);
 			EmpExecutionContext.error(e,"催办短信发送异常！");
 		}finally{
 			response.getWriter().print(result);
 		}
    }

    public void createtaskid(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String result = "";
        // 模板id
        String num = request.getParameter("num");
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Integer.parseInt(num); i++) {
                Long taskId = new CommonBiz().getAvailableTaskId();
                if (taskId != null)
                    sb.append(taskId).append(",");
            }
            result = sb.toString();
        }
        catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "获取模板信息异常。");
        }
        finally {
            // 异步返回操作结果
            response.getWriter().print(result);
        }
    }
    
    /**
     * 根据sp账号获取sp账号起止发送时间
     * @param userId
     * @return
     */
    public void getSpUserTimePeriod(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	String result = "";
    	String userId = request.getParameter("spUser");
    	List<DynaBean> userdatas = new SpUserBiz().getUserData(userId);
    	if(userdatas != null && !userdatas.isEmpty()){
    		result = (String) userdatas.get(0).get("sendtmspan");
    	}
    	response.getWriter().print(result);
    }

	public static String unescape(String content){
		if (content.isEmpty()){
			return content;
		}
		if(content.contains("&amp")){
			content = content.replaceAll("&amp","&amp;amp");
		}
		String [] uncate = {"&quot;","&lt;","&gt;","&nbsp;","&ensp;","&emsp;","&copy;","&reg;","&trade;","&times;","&divide;","&yen;","&ordf;","&macr;","&acute;","&sup1;","&frac34;","&Atilde;"
				,"&Egrave;","&Iacute;","&Ograve;","&Uuml;","&aacute;","&aelig;","&euml;","&eth;","&otilde;","&uacute;","&yuml;","&iexcl;","&brvbar;","&laquo;","&deg;","&micro;","&ordm;"
				,"&iquest;","&Auml;","&Eacute;","&Icirc;","&Oacute;","&Oslash;","&Yacute;","&acirc;","&ccedil;","&igrave;","&ntilde;","&ouml;","&ucirc;","&cent;","&sect;","&not;","&plusmn;"
				,"&para;","&raquo;","&Agrave;","&Aring;","&Ecirc;","&Iuml;","&Ocirc;","&Ugrave;","&THORN;","&atilde;","&egrave;","&iacute;","&ograve;","&divide;","&uuml;","&pound;","&uml;"
				,"&shy;","&sup2;","&middot;","&frac14;","&Aacute;","&AElig;","&Euml;","&ETH;","&Otilde;","&Uacute;","&szlig;","&auml;","&eacute;","&icirc;","&oacute;","&oslash;","&yacute;"
				,"&curren;","&sup3;","&cedil;","&frac12;","&Acirc;","&Ccedil;","&Igrave;","&Ntilde;","&Ouml;","&Ucirc;","&agrave;","&aring;","&ecirc;","&iuml;","&ocirc;","&ugrave;","&thorn;"
				,"&fnof;","&Epsilon;","&Kappa;","&Omicron;","&Upsilon;","&alpha;","&zeta;","&lambda;","&pi;","&upsilon;","&thetasym;","&prime;","&image;","&uarr;","&lArr;","&forall;","&isin;"
				,"&minus;","&ang;","&int;","&ne;","&sup;","&otimes;","&lfloor;","&spades;","&Alpha;","&Zeta;","&Lambda;","&Pi;","&Phi;","&beta;","&eta;","&mu;","&rho;","&phi;","&upsih;","&Prime;"
				,"&real;","&rarr;","&uArr;","&part;","&notin;","&lowast;","&and;","&there4;","&equiv;","&nsub;","&perp;","&rfloor;","&clubs;","&Beta;","&Eta;","&Mu;","&Rho;","&Chi;","&gamma;"
				,"&theta;","&nu;","&sigmaf;","&chi;","&piv;","&oline;","&trade;","&darr;","&rArr;","&exist;","&ni;","&radic;","&or;","&sim;","&le;","&sube;","&sdot;","&lang;","&hearts;","&Gamma;"
				,"&Theta;","&Nu;","&Sigma;","&Psi;","&delta;","&iota;","&xi;","&sigma;","&psi;","&bull;","&frasl;","&alefsym;","&harr;","&dArr;","&empty;","&prod;","&prop;","&cap;","&cong;","&ge;"
				,"&supe;","&lceil;","&rang;","&diams;","&Delta;","&Iota;","&Xi;","&Tau;","&Omega;","&epsilon;","&kappa;","&omicron;","&tau;","&omega;","&hellip;","&weierp;","&larr;","&crarr;","&hArr;"
				,"&nabla;","&sum;","&infin;","&cup;","&asymp;","&sub;","&oplus;","&rceil;","&loz;","&oelig;","&tilde;","&zwj;","&lsquo;","&bdquo;","&rsaquo;","&Scaron;","&lrm;","&rsquo;","&dagger;","&euro;"
				,"&scaron;","&emsp;","&rlm;","&sbquo;","&Dagger;","&Yuml;","&thinsp;","&ndash;","&ldquo;","&permil;","&OElig;","&circ;","&zwnj;","&mdash;","&rdquo;","&lsaquo;","&apos;"};

		List<String> unescapeList = Arrays.asList(uncate);
		for(String s : unescapeList){
			s = s.substring(0,s.indexOf(";"));
			if(content.contains(s)){
				String rec = s.substring(s.indexOf("&")+1);
				content = content.replaceAll(s,"&amp;"+rec);
			}
		}

		return content;
	}
}
