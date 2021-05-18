package com.montnets.emp.weix.servlet;

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

import com.montnets.emp.weix.biz.DefaultrepBiz;
import com.montnets.emp.weix.biz.WeixBiz;
import com.montnets.emp.weix.common.util.GlobalMethods;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.entity.weix.LfWcRevent;
import com.montnets.emp.entity.weix.LfWcRtext;
import com.montnets.emp.entity.weix.LfWcTemplate;
import com.montnets.emp.util.PageInfo;

@SuppressWarnings("serial")
public class DefaultrepSvt extends BaseServlet
{
	//操作模块
	private final String opModule= "默认回复";

	//操作用户
	private final String opSper = StaticValue.OPSPER;
	// 默认回复逻辑层
	private final DefaultrepBiz	replyBiz	= new DefaultrepBiz();
	// 资源路径
	private static final String		PATH			= "/weix/defaultrep";
	// 基础逻辑层
	private final BaseBiz							baseBiz			= new BaseBiz();

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
		
		try
		{
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			
			// 查询所有的公众帐号
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			List<LfWcAccount> lfWcAccList = new ArrayList<LfWcAccount>();
			lfWcAccList = new BaseBiz().getByCondition(LfWcAccount.class, conditionMap, null);
			
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
			
			request.setAttribute("lfWcAccList", lfWcAccList);
			request.setAttribute("replyBeans", replyBeans);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, opModule + "-加载默认回复列表失败!");
		}finally{
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(PATH + "/cwc_defaultRepList.jsp").forward(request, response);
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
		List<LfWcAccount> lfWcAccList = new ArrayList<LfWcAccount>();
		// 当前企业的所有公众帐号
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", lgcorpcode);
		try
		{
			lfWcAccList = new BaseBiz().getByCondition(LfWcAccount.class, conditionMap, null);
			
			request.setAttribute("lfWcAccList", lfWcAccList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, opModule + "-新增默认回复页面加载失败！");
		}finally{
			request.getRequestDispatcher(PATH + "/cwc_addDefault.jsp").forward(request, response);
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
		List<LfWcAccount> lfWcAccList = new ArrayList<LfWcAccount>();
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
			lfWcAccList = new BaseBiz().getByCondition(LfWcAccount.class, conditionMap, null);
			//公众帐号下列表框选择
			acctOptionsMap = this.getAcctOptionsMap(lfWcAccList);
			
			// 根据"默认回复"的id查询对应记录
			conditionMap.put("tet_id", tet_id);
			List<DynaBean> replyBeans = replyBiz.findDefaltReply(conditionMap, null);
			DynaBean replyBean = null;
			if(replyBeans != null && replyBeans.size() > 0)
			{
				replyBean = replyBeans.get(0);
			}

			// 查询当前默认回复模板的Title
			LfWcTemplate temp = new LfWcTemplate();
			String tempName = "";
//findbugs:	if(replyBean != null && !"".equals(replyBean))
			if(replyBean != null)
			{
				String t_id = String.valueOf(replyBean.get("t_id"));
				if(t_id != null && !"".equals(t_id) && !"0".equals(t_id))
				{
					temp = baseBiz.getById(LfWcTemplate.class, t_id);
					tempName = temp.getName();
				}
			}

			request.setAttribute("replyBean", replyBean);
			request.setAttribute("acctOptionsMap", acctOptionsMap);
			request.setAttribute("tempName", tempName);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, opModule + "-编辑页面加载失败！");
		}finally{
			request.getRequestDispatcher(PATH + "/cwc_editDefault.jsp").forward(request, response);
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
//		WeixBiz weixBiz = new WeixBiz();  //备份：findbugs报错

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

			LfWcRtext rtext = new LfWcRtext();
			rtext.setAId(Long.parseLong(aid));// 公众帐号的ID
			rtext.setCorpCode(lgcorpcode);
			// 没选模板，模板id设为0
			rtext.setTId(0L);
			// 模板id为空或为0就是文本内容内容回复
			if(tid != null && !"".equals(tid) && !"0".equals(tid))
			{
				// 模板类型的回复直接去模板表取msgxml
				LfWcTemplate wcTemplate = baseBiz.getById(LfWcTemplate.class, tid);
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
				msgXml = WeixBiz.createInitRtextMessage(GlobalMethods.handleSpecialTag(msgText));  //findbugs修改后
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
					//增加操作日志
					Object loginSysuserObj=request.getSession().getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						
						EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), 
								loginSysuser.getUserId().toString(), loginSysuser.getUserName(), 
								"创建默认回复（回复标题:"+title+"）成功", StaticValue.ADD);
						/*EmpExecutionContext.info("模块名称：默认回复，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"创建默认回复（回复标题:"+title+"）成功。");*/
					}
				}
				else
				{
					//创建失败
					result = "fail";
					//增加操作日志
					Object loginSysuserObj=request.getSession().getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.error("模块名称：" + opModule + "，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"创建微默认回复（回复标题:"+title+"）失败。");
					}
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
					
					//增加操作日志
					Object loginSysuserObj=request.getSession().getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), 
								loginSysuser.getUserId().toString(), loginSysuser.getUserName(), 
								"修改默认回复（回复标题:"+title+"）成功", StaticValue.UPDATE);
						/*EmpExecutionContext.info("模块名称：默认回复，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"修改默认回复（回复标题:"+title+"）成功。");*/
					}
				}
				else
				{
					// 修改失败
					result = "fail";
					//增加操作日志
					Object loginSysuserObj=request.getSession().getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.error("模块名称：" + opModule + "，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"修改微默认回复（回复标题:"+title+"）失败。");
					}
				}
			}
		}
		catch (Exception ex)
		{
			result = "error";
			EmpExecutionContext.error(ex, opModule + "-保存默认回复出错！");
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
				long count = new BaseBiz().deleteByIds(LfWcRtext.class, recordIds);
				if(count == 0)
				{
					//增加操作日志
					Object loginSysuserObj=request.getSession().getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;						
						EmpExecutionContext.error("模块名称：" + opModule + "，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"删除默认回复（回复IDS:"+recordIds+"）失败。");
					}
					result = "fail";
				}
				else
				{
					//增加操作日志
					Object loginSysuserObj=request.getSession().getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						
						EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), 
								loginSysuser.getUserId().toString(), loginSysuser.getUserName(), 
								"删除默认回复（回复IDS:"+recordIds+"）成功", StaticValue.DELETE);
						/*EmpExecutionContext.info("模块名称：默认回复，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"删除默认回复（回复IDS:"+recordIds+"）成功。");*/
					}
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
	public void getLfTemplateByWeix(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
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
			// 条件查询结果集
			List<DynaBean> templelist = replyBiz.getBaseTempInfos(conditionMap, pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("templelist", templelist);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"模板选择页查询模板失败！");
		}finally{
			request.getRequestDispatcher(PATH + "/cwc_deFTemp.jsp").forward(request, response);
		}
	}

	/**
	 * 微信公众帐号的类型
	 * 
	 * @return
	 */
	private LinkedHashMap<String, String> getAcctOptionsMap(List<LfWcAccount> acctList)
	{
		LinkedHashMap<String, String>	acctOptionsMap	= new LinkedHashMap<String, String>();
		acctOptionsMap.put("0", "所有公众帐号");
		for (LfWcAccount acct : acctList)
		{
			acctOptionsMap.put(String.valueOf(acct.getAId()), acct.getName());
		}
		return acctOptionsMap;
	}
}
