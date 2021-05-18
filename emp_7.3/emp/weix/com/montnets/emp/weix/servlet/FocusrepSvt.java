package com.montnets.emp.weix.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.weix.biz.FocusrepBiz;
import com.montnets.emp.weix.biz.WeixBiz;
import com.montnets.emp.weix.common.util.GlobalMethods;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.entity.weix.LfWcRevent;
import com.montnets.emp.entity.weix.LfWcTemplate;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

@SuppressWarnings("serial")
public class FocusrepSvt extends BaseServlet
{
	// 关注时回复逻辑层
	private final FocusrepBiz				replyBiz		= new FocusrepBiz();
	// 资源路径
	private static final String		PATH			= "/weix/focusrep";
	// 基础逻辑层
	private final BaseBiz		baseBiz			= new BaseBiz();

	private final String opModule = "关注时的回复";
	
	/**
	 * 关注时的回复管理页面
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
			List<DynaBean> replyBeans = replyBiz.findFocusReply(conditionMap, pageInfo);
			
			request.setAttribute("replyBeans", replyBeans);
			request.setAttribute("lfWcAccList", lfWcAccList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, opModule + "-列表页面加载失败!");
		}finally{
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(PATH + "/cwc_focusRepList.jsp").forward(request, response);
		}
	}

	/**
	 * 关注时的回复新增页面
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
		try
		{
			// 查询所有的公众帐号
			lfWcAccList = replyBiz.findBindAccountByCorpCode(lgcorpcode, "0");
			
			request.setAttribute("lfWcAccList", lfWcAccList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, opModule + "-新增页面加载失败!");
		}finally{
			request.getRequestDispatcher(PATH + "/cwc_addFocusRep.jsp").forward(request, response);
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
		//关注时回复ID
		String evt_id = request.getParameter("evt_id");
		// 查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", lgcorpcode);
		try
		{	
			// 查询所有的公众帐号
			lfWcAccList = replyBiz.findBindAccountByCorpCode(lgcorpcode, "1");
			
			// 根据"关注时回复"的id查询对应记录
			conditionMap.put("evt_id", evt_id);
			List<DynaBean> replyBeans = replyBiz.findFocusReply(conditionMap, null);
			
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

			request.setAttribute("lfWcAccList", lfWcAccList);
			request.setAttribute("replyBean", replyBean);
			request.setAttribute("tempName", tempName);
			request.setAttribute("evt_id", evt_id);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, opModule + "-编辑页面加载失败!");
		}finally{
			request.getRequestDispatcher(PATH + "/cwc_editFocusRep.jsp").forward(request, response);
		}
	}

	/**
	 * 关注时回复（保存）
	 * 一个公众帐号对应一个“关注时回复”，下面逻辑通过在数据库查找是否有记录，然后判读“更新”和“创建”对象。
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		String result = "";
//		WeixBiz weixBiz = new WeixBiz();  //备份：findbugs报错
		try
		{
			// 公众帐号ID编号
			String AId = request.getParameter("AId");
			// 消息的类型（0：无关键字默认回复；1：关注事件；2：点阅；3：取消点阅；4：CLICK(自定义菜单点击事件)；）
			// String evtType = request.getParameter("evtType");
			// 内容摘要，界面显示用
			String tid = request.getParameter("tid");
			String title = request.getParameter("title");
			String msgText = request.getParameter("msgText");
			String msgXml = "";
			
			LfWcRevent lfWcRevent = new LfWcRevent();
			// 没选模板，模板id设为0
			lfWcRevent.setTId(0L);
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
					msgXml = wcTemplate.getMsgXml();
				}
				lfWcRevent.setTId(Long.parseLong(tid));
			}
			else
			{
				// 将msgtext转换成xml格式的字符串
				msgXml = WeixBiz.createInitRtextMessage(GlobalMethods.handleSpecialTag(msgText));  //findbugs修改后

			}

			lfWcRevent.setMsgText(msgText);
			lfWcRevent.setMsgXml(msgXml);
			Timestamp currTime = new Timestamp(new Date().getTime());
			lfWcRevent.setCorpCode(lgcorpcode);
			lfWcRevent.setAId(Long.parseLong(AId));
			lfWcRevent.setModifytime(currTime);
			lfWcRevent.setTitle(title);
			lfWcRevent.setEvtType(1);

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);

			conditionMap.put("AId", AId);
			conditionMap.put("evtType", "1");
			List<LfWcRevent> reList = baseBiz.getByCondition(LfWcRevent.class, conditionMap, null);
			boolean issuccess = false;
			// 如果有记录表示修改，否则就是新增
			if(reList != null && reList.size() > 0)
			{
				lfWcRevent.setEvtId(reList.get(0).getEvtId());
				issuccess = baseBiz.updateObj(lfWcRevent);
			}
			else
			{
				lfWcRevent.setCreatetime(currTime);
				issuccess = baseBiz.addObj(lfWcRevent);
			}

			if(issuccess)
			{
				//保存成功
				Object sysObj=request.getSession().getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					
					EmpExecutionContext.info(opModule, lfSysuser.getCorpCode(), 
							lfSysuser.getUserId().toString(), lfSysuser.getUserName(), 
							"关注时的回复（保存）成功", StaticValue.ADD);
					//EmpExecutionContext.info("模块名称：" + opModule + "，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，关注时的回复（保存）成功");
				}
				result = "success";
			}
			else
			{
				//保存失败
				Object sysObj=request.getSession().getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					EmpExecutionContext.error("模块名称：" + opModule + "，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，关注时回复（保存）失败");
				}
				result = "fail";
			}

		}
		catch (Exception ex)
		{
			result = "error";
			EmpExecutionContext.error(ex, opModule + "关注回复-保存关注时回复出错!");
		}
		finally
		{
			response.getWriter().print(result);
		}
	}

	/**
	 * 删除关注时的回复
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String recordIds = request.getParameter("recordIds");

		String result = "";
		try
		{
			if(recordIds != null && !"".equals(recordIds))
			{
				//通过ids删除关注时回复,返回删除的记录条数
				long count = new BaseBiz().deleteByIds(LfWcRevent.class, recordIds);
				if(count == 0)
				{
					Object sysObj=request.getSession().getAttribute("loginSysuser");
					if(sysObj!=null){
						LfSysuser lfSysuser=(LfSysuser)sysObj;
						EmpExecutionContext.error("模块名称：" + opModule + "，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，删除关注时的回复（ID:"+recordIds+"）失败");
					}
					result = "fail";
				}
				else
				{
					Object sysObj=request.getSession().getAttribute("loginSysuser");
					if(sysObj!=null){
						LfSysuser lfSysuser=(LfSysuser)sysObj;
						
						EmpExecutionContext.info(opModule, lfSysuser.getCorpCode(), 
								lfSysuser.getUserId().toString(), lfSysuser.getUserName(), 
								"删除关注时的回复（ID:"+recordIds+"）成功", StaticValue.DELETE);
						//EmpExecutionContext.info("模块名称：" + opModule + "，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，删除关注时的回复（ID:"+recordIds+"）成功");
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
			EmpExecutionContext.error(e,"删除" + opModule + "失败！");
		}
		finally
		{
			response.getWriter().print(result);
		}
	}

}
