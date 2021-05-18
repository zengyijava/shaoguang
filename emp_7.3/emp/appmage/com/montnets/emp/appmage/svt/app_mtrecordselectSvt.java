package com.montnets.emp.appmage.svt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.montnets.emp.appmage.biz.app_morequeryBiz;
import com.montnets.emp.appmage.biz.app_mtrecordselectBiz;
import com.montnets.emp.appmage.util.FFmpegKit;
import com.montnets.emp.appwg.biz.WgMwFileBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.appmage.LfAppMtmsg;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;

public class app_mtrecordselectSvt extends BaseServlet 
{
	
	// 资源路径
    private static final String          PATH       = "/appmage/record";
    static BaseBiz baseBiz = new BaseBiz();
    /**
     * APP发送记录查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	//查询开始时间
    	long stratTime = System.currentTimeMillis();
//    	String userName = request.getParameter("userName");
		//操作员字符串 逗号隔开
		String userid = request.getParameter("userid");
		String userName=request.getParameter("userName");
		String msg_type = request.getParameter("msg_type");
		String tousername = request.getParameter("tousername");
		String sendstate = request.getParameter("sendstate");
		String createtime = request.getParameter("createtime");
		String endtime = request.getParameter("endtime");
		String rptstate = request.getParameter("rptstate");
		//String lguserid=request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		String title=request.getParameter("title");
		List<DynaBean>  mtRecordList=null;
		boolean isFirstEnter;
		try {
			HttpSession session= request.getSession(false);
			LfCorp corp=(LfCorp)session.getAttribute("loginCorp");
			String corpCode = corp.getCorpCode();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> condiMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("lguserid", lguserid);
			PageInfo pageInfo = new PageInfo();
			
			isFirstEnter = pageSet(pageInfo, request);
			if (!isFirstEnter) {
				if(userid!=null&&userid.length()>0){
					String struserid=userid.substring(0,userid.lastIndexOf(","));
					conditionMap.put("userid", struserid);
				}
				
				conditionMap.put("userName", userName);
				conditionMap.put("msg_type", msg_type);
				conditionMap.put("tousername", tousername);
				conditionMap.put("sendstate", sendstate);
				conditionMap.put("createtime", createtime);
				conditionMap.put("endtime", endtime);
				conditionMap.put("rptstate", rptstate);
				conditionMap.put("title", title);

				
				
				//获取当前登录用户的数据访问权限
				LfSysuser cursys=baseBiz.getById(LfSysuser.class, lguserid!=null?Long.parseLong(lguserid):0l);
				if(cursys==null){
					cursys=new LfSysuser();
					cursys.setUserId(0l);
					cursys.setPermissionType(1);
				}
			condiMap.put("permissionType", cursys.getPermissionType()+"");
			condiMap.put("corpCode", corpCode);
			orderMap.put("id", "desc");
			app_mtrecordselectBiz biz=new app_mtrecordselectBiz();
			mtRecordList =biz.query(corpCode, conditionMap, pageInfo);
			}
			request.setAttribute("mtRecordList", mtRecordList);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("isFirstEnter", isFirstEnter);
			
			//当前登录用户的登录名
			Object sysuserObj = request.getSession(false).getAttribute("loginSysuser");
			String lguserName = null;
			if(sysuserObj != null)
			{
				LfSysuser sysuser = (LfSysuser) sysuserObj;
				lguserName = (sysuser != null && sysuser.getUserName()!= null 
						&& !"".equals(sysuser.getUserName())) ? sysuser.getUserName() : null;
			}
			
			//查询出的数据的总数量
			int totalCount = pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间：" + sdf.format(stratTime) + " 耗时："+
			(System.currentTimeMillis()-stratTime) + "ms  数量：" + totalCount;
			
			EmpExecutionContext.info("APP发送记录查询", corpCode, lguserid, lguserName, opContent, "GET");
			
			request.getRequestDispatcher(PATH +"/app_mtrecordselect.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "业务数据查询异常!");
			try {		
				request.getRequestDispatcher(PATH +"/app_mtrecordselect.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1, "业务数据跳转异常!");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1, "业务数据IO加载异常!");
			}
		}
    }
    /**
     *  视频文字的预览
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws JSONException
     */
	public void prev(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, JSONException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String id = request.getParameter("id");
		LfAppMtmsg appmttask = null;
		if(StringUtils.isNotBlank(id)&&StringUtils.isNumeric(id)){
			try
			{
				LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
				conditionMap.put("id", id);
				List<LfAppMtmsg> mttasklist=baseBiz.getByCondition(LfAppMtmsg.class, conditionMap, null);
				if(mttasklist!=null&&mttasklist.size()>0){
					appmttask = mttasklist.get(0);
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "查询对应任务异常！");
			}
		}
		JSONObject json = new JSONObject();
		try
		{
			if(appmttask != null){
				json.put("errcode", 0);
				json.put("msgtype",appmttask.getMsgtype());
			if(appmttask.getMsgtype()!=0&&appmttask.getMsgtype()!=4){
				json.put("msgtext",appmttask.getContent());
				json.put("title",appmttask.getTitle());
			}else {
				//对文本做特殊处理 里面可能存在表情字符
				if(appmttask.getMsgtype() == 0){
					//对文本进行处理
					String text = appmttask.getContent()==null?"":appmttask.getContent();
					text = StringEscapeUtils.escapeHtml(text);
					//加空格后，出现页面显示错误的情况
					if(text!=null&&text.length()>0){
						text = text.replaceAll("\\n", "<br/>");
						text=text.replaceAll(" ", "&nbsp;");
					}
					String basePath = this.getServletContext().getRealPath("/");
					Map<String,String> emos = app_morequeryBiz.getEmoMap(basePath);
					Iterator<String> its = emos.keySet().iterator();
					while(its.hasNext()){
						String key = its.next();
						text = text.replaceAll(key, "<img class=\"emo\" src=\"appmage/morequery/emo/"+emos.get(key)+"\" />");
					}

					json.put("msgtext",text);
				}else {
					json.put("msgtext",appmttask.getContent()==null?"":appmttask.getContent());
				}
				
				json.put("title",appmttask.getTitle()==null?"":appmttask.getTitle());
			}
				if((appmttask.getContent()!=null&&!"".equals(appmttask.getContent()))&&appmttask.getMsgtype()!=0&&appmttask.getMsgtype()!=4){
					WgMwFileBiz fileBiz = new WgMwFileBiz();
					//请求地址相对路径
						String path="file"+File.separator+appmttask.getContent();
					
					//请求文件真实路径
					String realPath = request.getRealPath(path);
					//String extName = realPath.substring(realPath.lastIndexOf(".")).toLowerCase();
					File f = new File(realPath);
					if(!f.exists()){
							//String allImgExt = ".jpg|.jpeg|.gif|.bmp|.png|";
							//if(allImgExt.indexOf(extName)!=-1){
							fileBiz.downloadFromMwFileSer(realPath, appmttask.getContent());
							File f2 = new File(realPath);
							if(!f2.exists()){
								json.put("msgurl","");
							}else{
								if(appmttask.getMsgtype()==2){
									String flvpatch=FFmpegKit.convertPath(path);
									String flvrealPath = request.getRealPath(flvpatch);
									File flvf = new File(flvrealPath);
									if(!flvf.exists()){
										FFmpegKit.converVideo(realPath);
									}
									path=flvpatch;
								}
								//如果是音频，需要先转换成MP3格式，然后转换一下路径
								if(appmttask.getMsgtype()==3){
									FFmpegKit.converAudio(realPath);
									if(path.indexOf("/")>-1){
									String msgurl=path.replace("/", "\\");
									msgurl=msgurl.substring(0, msgurl.lastIndexOf("."));
									msgurl=msgurl+".mp3";
									json.put("msgurl",msgurl);
									}
								}else{
									json.put("msgurl",path);
								}
							}
							//}else if(){
							//	fileBiz.do
							//}else{
							//	json.put("msgurl","");
							//}
					}else{
						if(appmttask.getMsgtype()==2){
							String flvpatch=FFmpegKit.convertPath(path);
							String flvrealPath = request.getRealPath(flvpatch);
							File flvf = new File(flvrealPath);
							if(!flvf.exists()){
								FFmpegKit.converVideo(realPath);
							}
							path=flvpatch;
						}
						//如果是音频，需要先转换成MP3格式，然后转换一下路径
						if(appmttask.getMsgtype()==3){
							FFmpegKit.converAudio(realPath);
							if(path.indexOf("/")>-1){
							String msgurl=path.replace("/", "\\");
							msgurl=msgurl.substring(0, msgurl.lastIndexOf("."));
							msgurl=msgurl+".mp3";
							json.put("msgurl",msgurl);
							}
						}else{
							json.put("msgurl",path);
						}
					}

				}else{
					json.put("msgurl","");
				}
				
				
			}else{
				json.put("errcode", -1);
				json.put("errmsg", "未找到相应记录！");
			}
		}
		catch (Exception e)
		{
			json.put("errcode", -1);
			json.put("errmsg", "处理数据出现错误！");
		}finally{
			out.print(json.toString());
		}
		
	}
    
    /**
	 * APP发送记录查询excel导出
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//导出开始时间
		long stratTime = System.currentTimeMillis();
		String corpcode = request.getParameter("lgcorpcode");
		String userid=request.getParameter("userid");
		String msg_type=request.getParameter("msg_type");
		String tousername=request.getParameter("tousername");
		tousername = new String(tousername.getBytes("iso8859-1"),"UTF-8");
		String sendstate=request.getParameter("sendstate");
		String createtime=request.getParameter("createtime");
		String endtime = request.getParameter("endtime");
		String title=request.getParameter("title");
		title = new String(title.getBytes("iso8859-1"),"UTF-8");
		String rptstate = request.getParameter("rptstate");
		// 用户id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		// 分页对象
		PageInfo pageInfo = new PageInfo();
		try
		{
			if(userid!=null&&userid.length()>0){
				String struserid=userid.substring(0,userid.lastIndexOf(","));
				conditionMap.put("userid", struserid);
			}
				conditionMap.put("corpCode", corpcode);
				conditionMap.put("lguserid", lguserid);
				conditionMap.put("msg_type", msg_type);
				conditionMap.put("tousername", tousername);
				//创建人
				conditionMap.put("sendstate", sendstate);
				conditionMap.put("createtime", createtime);
				conditionMap.put("endtime", endtime);
				conditionMap.put("title", title);
				conditionMap.put("rptstate", rptstate);
				String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);	
				Map<String, String> resultMap = new app_mtrecordselectBiz().createMtHistoryExcel(corpcode, conditionMap, pageInfo, langName);

				if(resultMap != null && resultMap.size() > 0)
				{
					request.getSession(false).setAttribute("app_mtrecordselectMap", resultMap);
					// 文件名称
					//String fileName = (String) resultMap.get("FILE_NAME");
					// 路径
					//String filePath = (String) resultMap.get("FILE_PATH");
					//导出数量
					String mtTaskList =(String)resultMap.get("mtTaskList");
					 // 增加操作日志
					Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
					if (loginSysuserObj != null) {
						LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
						String opContent = "导出成功,开始时间："+sdf.format(stratTime)+" 耗时："+
						(System.currentTimeMillis()-stratTime)+"ms" +"数量：" + mtTaskList;
						EmpExecutionContext.info("APP发送记录查询(导出)", loginSysuser.getCorpCode(),
								loginSysuser.getUserId().toString(), loginSysuser
										.getUserName(), opContent, "OTHER");
					}

					//DownloadFile dfs = new DownloadFile();
					// 导出
					//dfs.downFile(request, response, filePath, fileName);
					PrintWriter out = response.getWriter();
					out.println("true");
				}
				else
				{
					response.sendRedirect(request.getContextPath() + "/app_mtrecordselect.htm?lguserid=" + lguserid + "&lgcorpcode=" + corpcode);
				}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "APP发送记录查询excel导出异常！");
		}
	}
	/**
	 * app发送记录查询导出
	 * @Title: downloadFile
	 * @Description: TODO
	 * @param  
	 * @return void    返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try {
			Object resultMapObj = request.getSession(false).getAttribute("app_mtrecordselectMap");
			if(resultMapObj != null)
			{
				Map<String, String> resultMap = (Map<String, String>) resultMapObj;
				//文件名
				String fileName=(String)resultMap.get("FILE_NAME");
				//文件路径
			    String filePath=(String)resultMap.get("FILE_PATH");
				
			    DownloadFile dfs=new DownloadFile();
			    dfs.downFile(request, response, filePath, fileName);
			    request.getSession(false).removeAttribute("app_mtrecordselectMap");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EmpExecutionContext.error(e,"app发送记录查询导出异常!");
		}

	}
	
	
}
