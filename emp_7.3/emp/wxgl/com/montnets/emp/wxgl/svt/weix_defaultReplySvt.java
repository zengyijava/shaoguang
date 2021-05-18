package com.montnets.emp.wxgl.svt;

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

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiRtext;
import com.montnets.emp.entity.wxgl.LfWeiTemplate;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.ottbase.util.GlobalMethods;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.biz.DefaultrepBiz;


@SuppressWarnings("serial")
public class weix_defaultReplySvt extends BaseServlet
{
	// 默认回复逻辑层
	private final DefaultrepBiz			replyBiz		= new DefaultrepBiz();
	// 资源路径
	private static final String		PATH			= "/wxgl/defaultrep";
	// 基础逻辑层
	private final BaseBiz	baseBiz	= new BaseBiz();

	/**
	 * 默认回复管理页面
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 分页信息
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter = pageSet(pageInfo, request);
		
		//添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
		
		try
		{
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			
			// 查询所有的公众帐号
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			List<LfWeiAccount> otWeiAccList = new ArrayList<LfWeiAccount>();
			otWeiAccList = new BaseBiz().getByCondition(LfWeiAccount.class, conditionMap, null);
			
			if(!isFirstEnter)
			{
				// 获取查询条件
				String a_id = request.getParameter("a_id");
				String title = request.getParameter("title");
				// 帐号名称
				conditionMap.put("a_id", a_id);
				// 标题
				conditionMap.put("title", title);
			}
			List<DynaBean> replyBeans = replyBiz.findDefaltReply(conditionMap, pageInfo);
			
			request.setAttribute("otWeiAccList", otWeiAccList);
			request.setAttribute("replyBeans", replyBeans);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "默认回复-加载默认回复列表失败!");
		}finally{
			
			//添加与日志相关 
			long endTimeByLog = System.currentTimeMillis();  //查询结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
			
			//增加操作日志 
			Object loginSysuserObj = null;
			try {
				loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e) {
				EmpExecutionContext.error(e, "session为null");
			}
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
				
				EmpExecutionContext.info("默认回复", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "GET");
			}
			
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(PATH + "/weix_defaultRepList.jsp").forward(request, response);
		}
	}

	/**
	 * 默认回复新增页面
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void doAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		// 存放公众帐号
		List<LfWeiAccount> otWeiAccList = new ArrayList<LfWeiAccount>();
		// 当前企业的所有公众帐号
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", lgcorpcode);
		try
		{
			otWeiAccList = new BaseBiz().getByCondition(LfWeiAccount.class, conditionMap, null);
			
			request.setAttribute("otWeiAccList", otWeiAccList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "默认回复-新增默认回复页面加载失败！");
		}finally{
			request.getRequestDispatcher(PATH + "/weix_addDefault.jsp").forward(request, response);
		}
	}

	/**
	 * 编辑默认回复
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void doEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		// 公众帐号列表对象
		List<LfWeiAccount> otWeiAccList = new ArrayList<LfWeiAccount>();
		// 公众帐号下列表框选择
		LinkedHashMap<String, String>	acctOptionsMap	= new LinkedHashMap<String, String>();
		// 默认回复ID
		String tet_id = request.getParameter("tet_id");
		// 查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", lgcorpcode);
		try
		{
			// 查询所有的公众帐号
			otWeiAccList = new BaseBiz().getByCondition(LfWeiAccount.class, conditionMap, null);
			//公众帐号下列表框选择
			acctOptionsMap = this.getAcctOptionsMap(otWeiAccList);
			
			// 根据"默认回复"的id查询对应记录
			conditionMap.put("tet_id", tet_id);
			List<DynaBean> replyBeans = replyBiz.findDefaltReply(conditionMap, null);
			DynaBean replyBean = null;
			if(replyBeans != null && replyBeans.size() > 0)
			{
				replyBean = replyBeans.get(0);
			}

			// 查询当前默认回复模板的Title
			LfWeiTemplate temp = new LfWeiTemplate();
			String tempName = "";
			if(replyBean != null)
			{
				String t_id = String.valueOf(replyBean.get("t_id"));
				if(t_id != null && !"".equals(t_id) && !"0".equals(t_id))
				{
					temp = baseBiz.getById(LfWeiTemplate.class, t_id);
					tempName = temp.getName();
				}
			}

			request.setAttribute("replyBean", replyBean);
			request.setAttribute("acctOptionsMap", acctOptionsMap);
			request.setAttribute("tempName", tempName);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "默认回复-编辑页面加载失败！");
		}finally{
			request.getRequestDispatcher(PATH + "/weix_editDefault.jsp").forward(request, response);
		}
	}

	/**
	 * 添加和编辑微信公众帐号(OpType == "add" 新增微信公众帐号 || OpType == "edit" 编辑微信公众帐号)
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		String result = "";

		try
		{
			// 操作类型是新增还是修改
			String OpType = request.getParameter("OpType");
			String aid = request.getParameter("AId");
			// 模板id
			String tid = request.getParameter("tid");
			String title = request.getParameter("title");
			String msgText = request.getParameter("msgText");
			String msgXml = "";

			LfWeiRtext rtext = new LfWeiRtext();
			rtext.setAId(Long.parseLong(aid));// 公众帐号的ID
			rtext.setCorpCode(lgcorpcode);
			// 没选模板，模板id设为0
			rtext.setTId(0L);
			// 模板id为空或为0就是文本内容内容回复
			if(tid != null && !"".equals(tid) && !"0".equals(tid))
			{
				// 模板类型的回复直接去模板表取msgxml
				LfWeiTemplate wcTemplate = baseBiz.getById(LfWeiTemplate.class, tid);
				if(wcTemplate == null)
				{
					result = "noTemplate";
					return;
				}
				else
				{
					// 和模板类型保持一致
					msgXml = wcTemplate.getMsgXml();
					rtext.setTetType(wcTemplate.getMsgType());
				}
				rtext.setTId(Long.parseLong(tid));
			}
			else
			{
				// 将msgtext转换成xml格式的字符串
				msgXml = WeixBiz.createInitRtextMessage(GlobalMethods.handleSpecialTag(msgText));
				rtext.setTetType(0);
			}
			rtext.setMsgText(msgText);
			rtext.setMsgXML(msgXml);
			rtext.setTitle(title);
			// 设置日期格式
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// new Date()为获取当前系统时间
			rtext.setCreatetime(Timestamp.valueOf(df.format(new Date())));
			// 新增模板
			if("add".equals(OpType))
			{
				Long addTemp = baseBiz.addObjReturnId(rtext);
				if(addTemp != null && addTemp > 0)
				{
					//创建成功
					result = "success";
				}
				else
				{
					//创建失败
					result = "fail";
				}
			}
			// 修改模板
			else if("edit".equals(OpType))
			{
				rtext.setModifytime(Timestamp.valueOf(df.format(new Date())));
				String tet_id = request.getParameter("tet_id");
				rtext.setTetId(Long.parseLong(tet_id));
				boolean upTemp = baseBiz.updateObj(rtext);
			
				if(upTemp)
				{
					// 修改成功
					result = "success";
				}
				else
				{
					// 修改失败
					result = "fail";
				}
			}
		}
		catch (Exception ex)
		{
			result = "error";
			EmpExecutionContext.error(ex, "默认回复-保存默认回复出错！");
		}
		finally
		{
			response.getWriter().print(result);
		}
	}

	/**
	 * 删除默认回复
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String recordIds = request.getParameter("recordIds");
		String result = "";
		try
		{
			if(recordIds != null && !"".equals(recordIds))
			{
				//通过ids删除关注时回复,返回删除的记录条数
				long count = new BaseBiz().deleteByIds(LfWeiRtext.class, recordIds);
				if(count == 0)
				{
					result = "fail";
				}
				else
				{
					result = "success" + count;
				}
			}
			else
			{
				result = "fail";
			}
		}
		catch (Exception e)
		{
			result = "error";
			EmpExecutionContext.error(e,"删除默认回复失败！");
		}
		finally
		{
			response.getWriter().print(result);
		}
	}

	/**
	 * 点击选择模板跳到模板页面
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void getTemplates(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{

		// 查询条件vo
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		// 分页信息
		PageInfo pageInfo = new PageInfo();
		pageSet(pageInfo, request);
		pageInfo.setPageSize(10);
		
		DefaultrepBiz replyBiz = new DefaultrepBiz();
		String lgcorpcode = request.getParameter("lgcorpcode");
		try
		{
			String tempType = request.getParameter("tempType");
			String startdate = request.getParameter("startdate");
			String enddate = request.getParameter("enddate");
			String onlyImgText = request.getParameter("onlyImgText");
			String nogif = request.getParameter("nogif");
			conditionMap.put("corpCode", lgcorpcode);
			if(tempType != null && !"".equals(tempType.trim()))
			{
				conditionMap.put("tempType", tempType.trim());
				request.setAttribute("tempType", tempType);
			}
			if(startdate != null && !"".equals(startdate))
			{
				request.setAttribute("startdate", startdate);
				conditionMap.put("startdate", startdate);
			}
			if(enddate != null && !"".equals(enddate))
			{
				request.setAttribute("enddate", enddate);
				conditionMap.put("enddate", enddate);
			}
			if(onlyImgText != null && !"".equals(onlyImgText))
			{
				request.setAttribute("onlyImgText", onlyImgText);
				conditionMap.put("onlyImgText", onlyImgText);
			}
			if(nogif != null && !"".equals(nogif)){
				request.setAttribute("nogif", nogif);
				conditionMap.put("nogif", nogif);
			}
			// 条件查询结果集
			List<DynaBean> templelist = replyBiz.getBaseTempInfos(conditionMap, pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("templelist", templelist);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"模板选择页查询模板失败！");
		}finally{
			request.getRequestDispatcher(PATH + "/weix_deFTemp.jsp").forward(request, response);
		}
	}

	/**
	 * 微信公众帐号的类型
	 * 
	 * @return
	 */
	private LinkedHashMap<String, String> getAcctOptionsMap(List<LfWeiAccount> acctList)
	{
		LinkedHashMap<String, String>	acctOptionsMap	= new LinkedHashMap<String, String>();
		for (LfWeiAccount acct : acctList)
		{
			acctOptionsMap.put(String.valueOf(acct.getAId()), acct.getName());
		}
		return acctOptionsMap;
	}
}
