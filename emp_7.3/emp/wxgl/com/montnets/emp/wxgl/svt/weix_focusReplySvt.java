package com.montnets.emp.wxgl.svt;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiRevent;
import com.montnets.emp.entity.wxgl.LfWeiTemplate;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.ottbase.util.GlobalMethods;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.biz.FocusrepBiz;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("serial")
public class weix_focusReplySvt extends BaseServlet
{
	// 关注时回复逻辑层
	private final FocusrepBiz replyBiz		= new FocusrepBiz();
	// 资源路径
	private static final String		PATH			= "/wxgl/focusrep";
	// 基础逻辑层
	private final BaseBiz	baseBiz	= new BaseBiz();

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
		
		//添加与日志相关
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
			List<DynaBean> replyBeans = replyBiz.findFocusReply(conditionMap, pageInfo);
			
			request.setAttribute("replyBeans", replyBeans);
			request.setAttribute("otWeiAccList", otWeiAccList);
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "关注回复-列表页面加载失败!");
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
				
				EmpExecutionContext.info("关注回复", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "GET");
			}
			
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(PATH + "/weix_focusRepList.jsp").forward(request, response);
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
		List<LfWeiAccount> otWeiAccList = new ArrayList<LfWeiAccount>();
		try
		{
			// 查询所有的公众帐号
			otWeiAccList = replyBiz.findBindAccountByCorpCode(lgcorpcode, "0");
			
			request.setAttribute("otWeiAccList", otWeiAccList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "关注回复-新增页面加载失败!");
		}finally{
			request.getRequestDispatcher(PATH + "/weix_addFocusRep.jsp").forward(request, response);
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
		//关注时回复ID
		String evt_id = request.getParameter("evt_id");
		// 查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", lgcorpcode);
		try
		{	
			// 查询所有的公众帐号
			otWeiAccList = replyBiz.findBindAccountByCorpCode(lgcorpcode, "0");
			
			// 根据"关注时回复"的id查询对应记录
			conditionMap.put("evt_id", evt_id);
			List<DynaBean> replyBeans = replyBiz.findFocusReply(conditionMap, null);
			
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

			request.setAttribute("otWeiAccList", otWeiAccList);
			request.setAttribute("replyBean", replyBean);
			request.setAttribute("tempName", tempName);
			request.setAttribute("evt_id", evt_id);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "关注回复-编辑页面加载失败!");
		}finally{
			request.getRequestDispatcher(PATH + "/weix_editFocusRep.jsp").forward(request, response);
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
			
			LfWeiRevent otWeiRevent = new LfWeiRevent();
			// 没选模板，模板id设为0
			otWeiRevent.setTId(0L);
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
					msgXml = wcTemplate.getMsgXml();
				}
				otWeiRevent.setTId(Long.parseLong(tid));
			}
			else
			{
				// 将msgtext转换成xml格式的字符串
				msgXml = WeixBiz.createInitRtextMessage(GlobalMethods.handleSpecialTag(msgText));

			}

			otWeiRevent.setMsgText(msgText);
			otWeiRevent.setMsgXml(msgXml);
			Timestamp currTime = new Timestamp(new Date().getTime());
			otWeiRevent.setCorpCode(lgcorpcode);
			otWeiRevent.setAId(Long.parseLong(AId));
			otWeiRevent.setModifytime(currTime);
			otWeiRevent.setTitle(title);
			otWeiRevent.setEvtType(1);

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);

			conditionMap.put("AId", AId);
			conditionMap.put("evtType", "1");
			List<LfWeiRevent> reList = baseBiz.getByCondition(LfWeiRevent.class, conditionMap, null);
			boolean issuccess = false;
			// 如果有记录表示修改，否则就是新增
			if(reList != null && reList.size() > 0)
			{
				otWeiRevent.setEvtId(reList.get(0).getEvtId());
				issuccess = baseBiz.updateObj(otWeiRevent);
			}
			else
			{
				otWeiRevent.setCreatetime(currTime);
				issuccess = baseBiz.addObj(otWeiRevent);
			}

			if(issuccess)
			{
				//保存成功
				result = "success";
			}
			else
			{
				//保存失败
				result = "fail";
			}

		}
		catch (Exception ex)
		{
			result = "error";
			EmpExecutionContext.error(ex, "关注回复-保存关注时回复出错!");
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
				long count = new BaseBiz().deleteByIds(LfWeiRevent.class, recordIds);
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
			EmpExecutionContext.error(e,"删除关注时回复失败！");
		}
		finally
		{
			response.getWriter().print(result);
		}
	}

}
