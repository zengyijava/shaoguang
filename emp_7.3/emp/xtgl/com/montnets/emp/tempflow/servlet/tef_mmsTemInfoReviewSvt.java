package com.montnets.emp.tempflow.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.tempflow.biz.TempReviewBiz;
import com.montnets.emp.tempflow.vo.LfFlowRecordTemplateVo;
import com.montnets.emp.template.atom.CreateTmsFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description 
 */
@SuppressWarnings("serial")
public class tef_mmsTemInfoReviewSvt extends BaseServlet {

	private final SysuserBiz SysuserBiz = new SysuserBiz();
	private final TempReviewBiz tempReviewBiz=new TempReviewBiz();
	private final CreateTmsFile mpb = new CreateTmsFile();
	private static final String PATH ="/xtgl/tempflow";
	public static final String opModule= "模板审批";
	
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LfFlowRecordTemplateVo mt = new LfFlowRecordTemplateVo();
		PageInfo pageInfo=new PageInfo();
		try {
			//是否第一次打开
			boolean isFirstEnter = false;
			isFirstEnter=pageSet(pageInfo, request);
			String theme = request.getParameter("theme");
			String userId = request.getParameter("userId");
			//String lguserid=request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//String beginTime = request.getParameter("beginTime");
			//String endTime = request.getParameter("endTime");
			request.setAttribute("pageInfo", pageInfo);
			if (!isFirstEnter)
			{
				if (theme != null)
				{
					mt.setTmName(theme);
				}
				if (userId != null && !"".equals(userId))
				{
					mt.setUserName(userId);
				}
			}
			List<LfSysuser> sysList = null;
			sysList = SysuserBiz.getAllSysusers(Long.parseLong(lguserid));
			
			request.getSession(false).setAttribute("sysList",sysList);
			mt.setRState(-1);
			mt.setReviewType(4);
			List<LfFlowRecordTemplateVo> mmsList = tempReviewBiz.getFlowRecordTemplateVos(Long.parseLong(lguserid), mt, pageInfo);
			request.setAttribute("mmsVo", mt);
			request.setAttribute("mmsList", mmsList);
			request.getRequestDispatcher(PATH+"/t_mmsExamine.jsp")
			.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"跳转彩信模板审批出现异常！");
			request.getRequestDispatcher(PATH+"/t_mmsExamine.jsp")
			.forward(request, response);
		}
	}

	/**
	 * 获取彩信模板信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getTmMsg(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		TxtFileUtil txtfileutil=new TxtFileUtil();
		String tmUrl=request.getParameter("tmUrl");
		String isokDownload = "";
		try{
			//判断是否使用集群   以及如果不存在该文件
			if(StaticValue.getISCLUSTER() == 1 && !txtfileutil.checkFile(tmUrl))
			{
				CommonBiz commBiz = new CommonBiz();
				//下载到本地
				if(!"success".equals(commBiz.downloadFileFromFileCenter(tmUrl))){
					isokDownload = "notmsfile";
				}
			}
			
			if("".equals(isokDownload)){
				if (tmUrl == null || "".equals(tmUrl)) {
					response.getWriter().print("");
				} else {
					String mms = mpb.getTmsFileInfo(tmUrl);
					if(mms != null)
					{
						mms = mms.replace("\r\n","&lt;BR/&gt;");   
						mms = mms.replace("\n","&lt;BR/&gt;"); 
					}
					response.getWriter().print(mms);
				}
			}else{
				response.getWriter().print("");
			}

		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取彩信模板信息出现异常！");
		}
	}
	/**
	 * 彩信审批的方法
	 * @param request
	 * @param response
	 */
	public void review(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String tmId = request.getParameter("tmId");
			String cont = request.getParameter("cont");
			String rState = request.getParameter("rState");
			if (tmId != null && !"".equals(tmId))
			{
				boolean result = tempReviewBiz.reviewTemplate(Long.parseLong(tmId), Integer.parseInt(rState), cont);
				if (result)
				{
					opSucLog(request, opModule, "彩信模板（ID:"+tmId+"，审批状态："+rState+"）审批成功。","OTHER");
					response.getWriter().print("true");
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"彩信审批出现异常！");
		}
	}
	/**
	 * 删除彩信文件
	 * @param request
	 * @param response
	 */
	public void delSource(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String sourceUrl = request.getParameter("sourceUrl");
			if (sourceUrl != null && !"".equals(sourceUrl) && sourceUrl.indexOf("mmsTemplate") < 0)
			{
				File f = new File(this.getServletContext().getRealPath("/") + sourceUrl);
				 if (!f.isDirectory()) 
				 {
					 boolean r = f.delete();
					 if (r)
					 {
						 response.getWriter().print("true");
					 }
				 }
			}
			else if (sourceUrl != null && !"".equals(sourceUrl) && sourceUrl.indexOf("mmsTemplate") >= 0)
			{
				response.getWriter().print("true");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"删除彩信文件出现异常！");
		}
	}
	
	/**
	 *   根据彩信模板类型其彩信tms文件路径
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getTmMsgByBmtype(HttpServletRequest request, HttpServletResponse response)throws Exception
	{
	
		try
		{
			String tmIdorUrl=request.getParameter("tmUrl");
			String bmtype=request.getParameter("bmtype");
			String tplPath=request.getParameter("tplPath");
			String tmUrl = "";
			if(!"10".equals(bmtype)){
				/*LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("sptemplid", tmIdorUrl);
				List<LfTemplate> templateList = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
				if(templateList != null && templateList.size()>0){
					tmUrl = templateList.get(0).getTmMsg();
				}else{
					response.getWriter().print("");
					return;
				}*/
				tmUrl = tplPath;
			}else{
				tmUrl = tmIdorUrl;
			}
			if(tmUrl == null || "".equals(tmUrl))
			{
				response.getWriter().print("");
				return;
			}else{
				String mms = mpb.getTmsFileInfo(tmUrl);
				if(mms != null)
				{
					mms = mms.replace("\r\n","&lt;BR/&gt;");   
					mms = mms.replace("\n","&lt;BR/&gt;"); 
				}
				response.getWriter().print(mms);
				return;
			}
		}catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取彩信模板信息出现异常！");
		}
	}
	
	//获取彩信模板的信息
	public void getTmMsgXtgl(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		TxtFileUtil txtfileutil=new TxtFileUtil();
		String tmUrl=request.getParameter("tmUrl");
		String isokDownload = "";
		try{
			//判断是否使用集群   以及如果不存在该文件
			if(StaticValue.getISCLUSTER() == 1 && !txtfileutil.checkFile(tmUrl))
			{
				CommonBiz commBiz = new CommonBiz();
				//下载到本地
				if(!"success".equals(commBiz.downloadFileFromFileCenter(tmUrl))){
					isokDownload = "notmsfile";
				}
			}
			
			String paramContent = request.getParameter("paramContent");
			if("".equals(isokDownload)){
				if (tmUrl == null || "".equals(tmUrl)) {
					response.getWriter().print("");
				} else {
					String mms = "";
					if (paramContent != null && !"".equals(paramContent)
							&& !"null".equals(paramContent)) {
						paramContent = paramContent.replaceAll(",", "，");
						mms = mpb.getDynTmsFileInfo(tmUrl, paramContent);
					} else {
						mms = mpb.getTmsFileInfo(tmUrl);
					}
					if (mms != null) {
						mms = mms.replace("\r\n", "&lt;BR/&gt;");
						mms = mms.replace("\n", "&lt;BR/&gt;").replaceAll("#[pP]_([1-9][0-9]*)#","{#参数$1#}");
					}
					response.getWriter().print(mms);
				}
			}else{
				response.getWriter().print("");
			}

		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"取彩信模板的信息出现异常！");
		}
	}
	
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志异常,session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
	
	
	
	
	
	
	
	
	
}
