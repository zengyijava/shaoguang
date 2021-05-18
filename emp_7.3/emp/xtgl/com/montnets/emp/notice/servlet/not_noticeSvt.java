package com.montnets.emp.notice.servlet;



import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.notice.LfNotice;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.notice.dao.LfNoticeVoDAO;
import com.montnets.emp.notice.vo.LfNoticeVo;
import com.montnets.emp.util.PageInfo;
/**
 * 公告管理
 * @author LINZHIHAN 
 *
 */
@SuppressWarnings("serial")
public class not_noticeSvt extends BaseServlet {

	private final String empRoot = "xtgl";
	private final String basePath = "/notice";
	private final BaseBiz baseBiz=new BaseBiz();
    /**
     * 查询方法
     * @param request
     * @param response
     */
	public void findAll(HttpServletRequest request, HttpServletResponse response)
	{
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		try {
			PageInfo pageInfo=new PageInfo();
			pageSet(pageInfo, request);
			//企业编码
			String lgcorpcode = request.getParameter("lgcorpcode");
			//调用查询方法
			List<LfNoticeVo> lnList = new LfNoticeVoDAO().findLatestLfNoticeVos(5,lgcorpcode);
			//结果返回前台
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lnList", lnList);
			
			//增加查询日志
			int records=0;
			if(lnList!=null){
				records=lnList.size();
			}
			long end_time=System.currentTimeMillis();
			String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+records;
			opSucLog(request, "公告列表", opContent, "GET");
			request.getRequestDispatcher(this.empRoot + this.basePath + "/not_notice.jsp")
			.forward(request, response);
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e, "公告svt的findAll方法异常。");
		}
	}
	
	/**
	 * 点击更多的查询方法
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		try {
			//是否第一次打开
			PageInfo pageInfo=new PageInfo();
			pageSet(pageInfo, request);
			//企业编码
			String lgcorpcode = request.getParameter("lgcorpcode");
            //调用查询方法
			List<LfNoticeVo> lnList = new LfNoticeVoDAO().findLfNoticeVos(pageInfo,lgcorpcode);
			//1表示从首页公告查看，其余表示公告显示公告
			String type=request.getParameter("type");
			//结果返回
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("allList", lnList);
			request.setAttribute("lgusername", request.getParameter("lgusername"));
			request.setAttribute("type", type);
			
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec()+"条";
				opSucLog(request, "公告列表", opContent, "GET");
			}
			request.getRequestDispatcher(this.empRoot + this.basePath + "/not_notice.jsp")
			.forward(request, response);
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e, "公告svt的find方法异常。");
		}
	}
	
	/**
	 * 添加及修改公告
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try {
			String id = request.getParameter("id");
			//标题
			String title = request.getParameter("tt");
			//内容
			String content = request.getParameter("cont"); 
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			String corpCode = request.getParameter("lgcorpcode");
			LfNotice ln = null;
			String preTitle="";
			if (id != null && !"".equals(id))
			{
				ln = baseBiz.getById(LfNotice.class, id);
				preTitle=ln.getTitle();
			}
			else
			{
				ln = new LfNotice(); 
			}
			if (title != null && !"".equals(title))
			{
				ln.setTitle(title);
			}
			if (content != null && !"".equals(content))
			{
				content = content.replaceAll("\n", "\\\\n");
				ln.setContext(content);
			}
			ln.setUserID(Long.parseLong(lguserid));
			//发布时间
			ln.setPublishTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
			//调用添加或修改方法
			boolean result = false;
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			String lgCorpCode = null;
			String lgUserId = null;
			String lgUserName = null;
			if(loginSysuserObj!=null){
				LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
				lgCorpCode = lfSysuser.getCorpCode();
				lgUserId = String.valueOf(lfSysuser.getUserId());
				lgUserName = lfSysuser.getUserName();
			}
			if (id != null && !"".equals(id))
			{
				result = baseBiz.updateObj(ln);
				if(result){
					//增加操作日志
					EmpExecutionContext.info("公告列表", lgCorpCode, lgUserId, lgUserName, "修改公告成功。[标题]（"+preTitle+"）->（"+title+"）", "UPDATE");
				}else{
					//增加操作日志
					EmpExecutionContext.info("公告列表", lgCorpCode, lgUserId, lgUserName, "修改公告失败。[标题]（"+preTitle+"）->（"+title+"）", "UPDATE");
				}
			}
			else
			{
				ln.setCorpcode(corpCode);
				result = baseBiz.addObj(ln);
				if(result){
					//增加操作日志
					EmpExecutionContext.info("公告列表", lgCorpCode, lgUserId, lgUserName, "新增公告成功。[标题]（"+title+"）", "ADD");
				}else{
					//增加操作日志
					EmpExecutionContext.info("公告列表", lgCorpCode, lgUserId, lgUserName, "新增公告失败。[标题]（"+title+"）", "ADD");
				}
			}
			//结果返回
			if (result)
			{
				response.getWriter().print("true");
			}
			else
			{
				response.getWriter().print("false");
			}
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e, "公告svt的修改方法异常。");
			response.getWriter().print("false");
		}
	}
	
	/**
	 * 删除公告方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			//获取ids
			String ids = request.getParameter("ids").toString();
			//查询一遍
			String title="";
			if(ids!=null){
				String[] id=ids.split(",");
				for(int i=0;i<id.length;i++){
					if("".equals(id[i])){
						continue;
					}
					LfNotice info = baseBiz.getById(LfNotice.class, id[i]);
					if(info!=null){
						title=info.getTitle()+","+title;
					}
				}
			}
			//调用删除方法
			int r = baseBiz.deleteByIds(LfNotice.class, ids);
			if(r>0){
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("公告列表", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "删除公告成功。[标题]("+title+"）", "DELETE");
				}
			}else{
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("公告列表", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "删除公告失败。[标题]("+title+"）", "DELETE");
				}
			}
			response.getWriter().print(r);
		} catch (Exception e)
		{
			//异常处理
			response.getWriter().print(0);
			EmpExecutionContext.error(e, "公告svt的删除方法异常。");
		}
	}
	/**
	 * 查看公告明细
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void showDetail(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//获取id
		String id = request.getParameter("id");
		LfNoticeVo ln = null;
		if (id != null && !"".equals(id))
		{
			SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//加载过滤条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("noticeID", id);
			try {
				//根据id查询对象
				ln =  new LfNoticeVoDAO().getNoticeVo(Long.valueOf(id));
				if(ln != null){
					String context="";
					if(ln.getContext()!=null)
					{
						context=ln.getContext().replaceAll("\'", "\\\\'").replaceAll("\"", "\\\\\"");
					}
					String str = "{'name':'"+ln.getName()+"','publishTime':'"+df.format(ln.getPublishTime())+"','title':'"
					+ln.getTitle().replaceAll("\'", "\\\\'").replaceAll("\"", "\\\\\"")+"','content':'"+context+"'}";
					response.getWriter().print(str);
				}else{
					response.getWriter().print("false");
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"查询公告信息异常");
				//异常处理
				response.getWriter().print("false");
				
			}
		}else{
			response.getWriter().print("false");
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
			EmpExecutionContext.error(e, "记录操作日志异常");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
}
