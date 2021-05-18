/**
 * Program  : wx_netAttachedServlet.java
 * Author   : chensj
 * Create   : 2013-6-7 下午02:55:59
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.netnews.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.base.DateFormatter;
import com.montnets.emp.netnews.base.GlobalMethods;
import com.montnets.emp.netnews.dao.SUCATableDao;
import com.montnets.emp.netnews.entity.LfWXSORT;
import com.montnets.emp.netnews.entity.LfWXUploadFile;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;



/**
 * 
 * @author   chensj <510061684@qq.com>
 * @version  1.0.0
 * @2013-6-7 下午02:55:59
 * 网讯素材查询
 */
public class wx_netAttachedServlet extends BaseServlet {
	
	//日志logger
	private static final Logger logger = Logger.getLogger("wx_netAttachedServlet");
	
	private static final String PATH = "/ydwx/webex";
	
	private final SUCATableDao sucaDao = new SUCATableDao();
	
	private final BaseBiz baseBiz = new BaseBiz();
	
	/**
	 * 
	 * @author chensj
	 * @create 2013-6-7 下午03:24:15
	 * @since 
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		
		try {
			if(request.getSession(false).getAttribute("msgshow")!=null){
				String s=(String)request.getSession(false).getAttribute("msgshow");
				request.setAttribute("msg",s);
				request.getSession(false).removeAttribute("msgshow");
			}
			request.getRequestDispatcher(PATH+ "/webexbook.jsp").forward(request,
					response);
		} catch (ServletException e) {
			EmpExecutionContext.error(e, "查询网讯素材出错！");
		} catch (IOException e) {
			EmpExecutionContext.error(e, "查询网讯素材出错！");
		}
	}
	/**
	 * 加载素材列表json项
	 * @author chensj
	 * @create 2013-6-8 上午09:45:12
	 * @since 
	 * @param request
	 * @param response
	 */
	public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{	
			String depId = request.getParameter("depId");
			HttpSession session= request.getSession(false);
			LfCorp corp=(LfCorp)session.getAttribute("loginCorp");
			String corpCode = corp.getCorpCode();
			String userid = "";
			if(GlobalMethods.isInvalidString(depId)){
				depId = "-1";
			}
			LfWXSORT dep = null;
			if("-1".equals(depId)){
				dep = new LfWXSORT();
				dep = new LfWXSORT();
				dep.setNAME(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_34",request));
				dep.setID(-1L);
				dep.setParentId(0L);
			}
			
			StringBuffer tree = new StringBuffer("");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			conditionMap.put("parentId", depId);
			conditionMap.put("TYPE", "1");
			conditionMap.put("corpCode&in", "0,"+corpCode);
			List<LfWXSORT> empDepList =baseBiz.getByCondition(LfWXSORT.class, conditionMap, orderbyMap);
			
			if(empDepList != null && empDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < empDepList.size(); i++) {
					if(dep!=null && dep.getID()==-1L){
						tree.append("{");
						tree.append("id:'").append(dep.getID()+"'");
						tree.append(",name:'").append(dep.getNAME()).append("'");
						tree.append(",pId:'").append(dep.getParentId()+"'");
						tree.append(",isParent:").append(true);
						tree.append("},");
				   }
					dep = empDepList.get(i);
					String depName = dep.getNAME();
					if("文件".equals(depName)){
						depName = MessageUtils.extractMessage("ydwx","ydwx_common_file",request);
					}else if("视频".equals(depName)){
						depName = MessageUtils.extractMessage("ydwx","ydwx_common_video",request);
					}else if("图片".equals(depName)){
						depName = MessageUtils.extractMessage("ydwx","ydwx_common_picture",request);
					}else if("其他".equals(depName)){
						depName = MessageUtils.extractMessage("ydwx","ydwx_common_other",request);
					}
					tree.append("{");
					tree.append("id:'").append(dep.getID()+"'");
					tree.append(",name:'").append(depName).append("'");
					tree.append(",pId:'").append(dep.getParentId()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != empDepList.size()-1){
						tree.append(",");
					}
			   }
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "加载素材内容失败!");
			
		}
	}
	/**
	 * 显示素材table项
	 * @author chensj
	 * @create 2013-6-14 上午08:45:41
	 * @since 
	 * @param request
	 * @param response
	 */
	public void getTable(HttpServletRequest request, HttpServletResponse response)
	{	
		String id = request.getParameter("depId");
		
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		
		Long serid;
		if(GlobalMethods.isInvalidString(id)||"-1".equals(id)){
			serid = -1L;
		}else{
			serid = GlobalMethods.swicthLong(id);
		}
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter;
		try {
			isFirstEnter = pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			if(!isFirstEnter)
			{
				String serahname=request.getParameter("serahname");
				String startdate=request.getParameter("submitSartTime");
				String enddate=request.getParameter("submitEndTime");

				HttpSession session= request.getSession(false);
				LfCorp corp=(LfCorp)session.getAttribute("loginCorp");
				String lgcorpcode=corp.getCorpCode();
				if(serahname!=null && !"".equals(serahname.trim()))
				{
					conditionMap.put("serahname", serahname.trim());
					request.setAttribute("serahname",serahname);
				}
				if(startdate!=null && !"".equals(startdate))
				{
					request.setAttribute("startdate",startdate);
					conditionMap.put("startdate", startdate);
				}
				if(enddate!=null && !"".equals(enddate))
				{
					request.setAttribute("enddate",enddate);
					conditionMap.put("enddate", enddate);
				}
				//增加公司
				if(lgcorpcode!=null && !"".equals(lgcorpcode))
				{
					conditionMap.put("lgcorpcode", lgcorpcode);
				}
				
			}
			List<DynaBean> beans = sucaDao.getLfWXSORTs(conditionMap, pageInfo, serid);
			request.setAttribute("pagebaseinfo",beans);
			request.setAttribute("pageInfo",pageInfo);
			
			//增加查询日志
			long end_time=System.currentTimeMillis();
			String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+pageInfo.getTotalRec();
			Object sysObj=request.getSession(false).getAttribute("loginSysuser");
			if(sysObj!=null){
				LfSysuser lfSysuser=(LfSysuser)sysObj;
				EmpExecutionContext.info("网讯素材", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), opContent, "GET");
			}
			
			request.getRequestDispatcher(PATH + "/webexTable.jsp").forward(request, response);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "加载素材项失败!");
		}
	}
	
	/**
	 * 添加素材列表
	 * @author chensj
	 * @create 2013-6-13 下午03:48:19
	 */
	public void addWeblist(HttpServletRequest request, HttpServletResponse response){
		String name = request.getParameter("name");
		String currentId = request.getParameter("currentId");
		//String sysuser = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String sysuser = SysuserUtil.strLguserid(request);
		String corpcode = request.getParameter("lgcorpcode");
		LfWXSORT lf ;
		String sort_key_;
		Long currid = GlobalMethods.swicthLong(currentId);
		try {
			lf = baseBiz.getById(LfWXSORT.class, currid);
			sort_key_ = lf.getSort_path();
			lf = new LfWXSORT();
			lf.setNAME(name);
			lf.setCREATID(GlobalMethods.swicthLong(sysuser));
			lf.setParentId(currid);
			lf.setCorpCode(corpcode==null?"":corpcode);
			lf.setCREATDATE(DateFormatter.sqlDate());
			currid = baseBiz.addObjProReturnId(lf);
			lf = new LfWXSORT();
			lf.setID(currid);
			lf.setNAME(name);
			lf.setTYPE(1);// 网讯素材
			lf.setSort_path(sort_key_+currid+"/");
			boolean res=baseBiz.updateObj(lf);
			String log="失败";
			if(res){
				log="成功";
			}
			Object sysObj=request.getSession(false).getAttribute("loginSysuser");
			if(sysObj!=null){
				LfSysuser lfSysuser=(LfSysuser)sysObj;
				EmpExecutionContext.info("网讯素材", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "添加素材列表"+log+"[素材名称]（"+name+"）", "ADD");
				//EmpExecutionContext.info("模块名称：网讯素材，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，添加素材列表（素材名称："+name+"）操作成功");
			}
			response.getWriter().print("true");
		} catch (Exception e) {
			EmpExecutionContext.error(e, "添加素材列表异常！");
		}
		
	}
	/**
	 * 删除素材列表项
	 * @author chensj
	 * @create 2013-6-14 上午08:45:08
	 * @since
	 */
	public void delDep(HttpServletRequest request, HttpServletResponse response){
		try {
			String id = request.getParameter("id");
			LfWXSORT sw;
			sw = baseBiz.getById(LfWXSORT.class, GlobalMethods.swicthLong(id));
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			Integer aInteger=0;
			String sort_path=sw.getSort_path();
			if(sw.getSort_path()!=null){
				sucaDao.deleteFileBySort(sw.getSort_path());
				conditionMap.put("sort_path&like", sw.getSort_path()+"%"); //
				aInteger=baseBiz.deleteByCondition(LfWXSORT.class, conditionMap);
			}
//			Integer aInteger = baseBiz.deleteByCondition(LfWXSORT.class, conditionMap);
			String log="失败";
			if(aInteger>=1){
				log="成功";
				response.getWriter().print("1");
			}
			Object sysObj=request.getSession(false).getAttribute("loginSysuser");
			if(sysObj!=null){
				LfSysuser lfSysuser=(LfSysuser)sysObj;
				EmpExecutionContext.info("网讯素材", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "删除素材列表"+log+"[素材路径]("+sort_path+"以下的路径）", "DELETE");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除素材列表项异常！");
		}
	}
	/**
	 * 修改类型
	 * @author chensj
	 * @create 2013-6-21 下午05:31:29
	 */
	public void updateDepName(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		String newName = request.getParameter("depName");
		try {
//			LfWXSORT sW = new LfWXSORT();
			//
			LfWXSORT sW = baseBiz.getById(LfWXSORT.class, id);
			String beforeValue=sW.getNAME();
			sW.setID(GlobalMethods.swicthLong(id));
			sW.setNAME(newName);
			boolean b = baseBiz.updateObj(sW);
			if(b){
				Object sysObj=request.getSession(false).getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					EmpExecutionContext.info("网讯素材", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "修改类型成功。[类型ID，类型名称]("+id+"，"+beforeValue+"）->("+id+"，"+newName+"）", "UPDATE");
					//EmpExecutionContext.info("模块名称：网讯素材，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，修改列表（修改后的名称："+newName+"）成功");
				}
				response.getWriter().print("1");
			}else{
				Object sysObj=request.getSession(false).getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					EmpExecutionContext.info("网讯素材", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "修改类型失败。[类型ID，类型名称]("+id+"，"+beforeValue+"）->("+id+"，"+newName+"）", "UPDATE");
					//EmpExecutionContext.error("模块名称：网讯素材，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，修改列表（修改后的名称："+newName+"）失败");
				}
				response.getWriter().print("2");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "修改列表异常！");
		}
	}
	/**
	 * 删除素材
	 * @author chensj
	 * @create 2013-6-17 下午02:26:07
	 */
	public void doDelsucai(HttpServletRequest request, HttpServletResponse response){
		String delid = request.getParameter("bookIds");
		try {
			String name="";
			if(delid!=null){
				String[] ids=delid.split(",");
				for(int i=0;i<ids.length;i++){
					if("".equals(ids[i])){
						continue;
					}
					LfWXUploadFile file = baseBiz.getById(LfWXUploadFile.class, ids[i]);
					if(file!=null){
						name=file.getFILENAME()+","+name;
					}
				}
			}
			Integer aInteger = baseBiz.deleteByIds(LfWXUploadFile.class, delid);
			String log="失败";
			if(aInteger>=1){
				log="成功";
				response.getWriter().print("1");
			}
			
			Object sysObj=request.getSession(false).getAttribute("loginSysuser");
			if(sysObj!=null){
				LfSysuser lfSysuser=(LfSysuser)sysObj;
				EmpExecutionContext.info("网讯素材", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "删除素材"+log+"。[素材名称]("+name+"）", "DELETE");
			}
		} catch (Exception e) {
			logger.info("删除素材出错了");
			EmpExecutionContext.error(e, "删除素材异常！");
		}
		
	}
	/**
	 * 修改素材
	 * @author chensj
	 * @create 2013-6-17 下午02:26:32
	 */
	public void doEdisucai(HttpServletRequest request, HttpServletResponse response){
		String delid = request.getParameter("bookId");
		String filename = request.getParameter("filename");
		String filedes = request.getParameter("filedes");
		// 以前的代码写的有问题，更新的对象不是先查询进行修改，而是直接new（新建）一个，进行更新
		boolean b;
		try{
			LfWXUploadFile file = baseBiz.getById(LfWXUploadFile.class, delid);
			String beforeValue=file.getFILENAME(); 
			file.setID(GlobalMethods.swicthLong(delid));
			file.setFILENAME(filename);
			file.setFILEDESC(filedes);
			b = baseBiz.updateObj(file);
			if(b){
				Object sysObj=request.getSession(false).getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					//EmpExecutionContext.info("模块名称：网讯素材，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，修改素材（新的文件名称："+filename+"）成功");
					EmpExecutionContext.info("网讯素材", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "修改素材成功。[类型ID，类型名称]("+delid+"，"+beforeValue+"）->("+delid+"，"+filename+"）", "UPDATE");
				}
				response.getWriter().print("1");
			}else{
				Object sysObj=request.getSession(false).getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					//EmpExecutionContext.info("模块名称：网讯素材，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，修改素材（新的文件名称："+filename+"）成功");
					EmpExecutionContext.info("网讯素材", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "修改素材失败。[类型ID，类型名称]("+delid+"，"+beforeValue+"）->("+delid+"，"+filename+"）", "UPDATE");
				}
			}
		}catch(Exception e){
			logger.info("更新数据出错了");
			EmpExecutionContext.error(e, "更新数据异常！");
		}
	}
	
	/****
	 * 通过ID查询分类路径信息
	 * @param request
	 * @param response
	 */
	public void sortPathById(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("nodeid");
		LfWXSORT lfWXSORT ;
		try {
			lfWXSORT = baseBiz.getById(LfWXSORT.class, id);
			if(lfWXSORT!=null){
				response.getWriter().print(lfWXSORT.getSort_path().substring(3,5));
			}
		} catch (Exception e) {
			logger.info("查询出错了");
			EmpExecutionContext.error(e, "查询lfWXSORT异常！");
		}
	}
}

