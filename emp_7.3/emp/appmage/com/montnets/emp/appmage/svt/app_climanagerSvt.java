package com.montnets.emp.appmage.svt;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.appmage.biz.app_climanagerBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
/**
 * @description 用户管理模块
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-11-29 下午03:37:13
 */

public class app_climanagerSvt extends BaseServlet
{

    /**
     * @description serialVersionUID
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-29 下午03:56:20
     */
    private static final long   serialVersionUID = 6834167819550902714L;

    /**
     * 资源路径
     */
    private static final String PATH= "/appmage/contact";

    /**
     * 声明一个公用的baseBiz对象
     */
    private static final BaseBiz    baseBiz          = new BaseBiz();

    /**
     * 用戶管理页面
     *   获取APP客户管理列表信息（根据用户的查询条件）
     * @description 默认查询方法
     * @param request
     *        HttpServletRequest
     * @param response
     *        HttpServletResponse
     * @throws ServletException
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-2 上午11:09:07
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	//查询开始时间
    	long stratTime = System.currentTimeMillis();
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        pageSet(pageInfo, request);
        String showAll = request.getParameter("showAll"); //如果全选，这个有值
        String lgcorpcode = request.getParameter("lgcorpcode");
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);


        String exportExcel = request.getParameter("exportExcel"); //判断导出提示
        try
        {
            // 当前登录企业


            // 存放查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            // 获取查询条件
            if(showAll==null||"".equals(showAll))
            {
                // 用户昵称
                String nickname = request.getParameter("nickname");
                if(null != nickname && !"".equals(nickname.trim()))
                {
                    conditionMap.put("nickname", nickname.trim());
                }
                
                // 性别
                String sex = request.getParameter("sex");
                if(null != sex && !"".equals(sex.trim()))
                {
                    conditionMap.put("sex", sex.trim());
                }
                
                // 账户
                String app_code = request.getParameter("app_code");
                if(null != app_code && !"".equals(app_code.trim()))
                {
                    conditionMap.put("app_code", app_code.trim());
                }
                
                
                String phone = request.getParameter("phone");
                if(null != phone && !"".equals(phone.trim()))
                {
                    conditionMap.put("phone", phone.trim());
                }
                
                String age = request.getParameter("age");
                if(null != age && !"".equals(age.trim()))
                {
                    conditionMap.put("age", age.trim());
                }
                
                String createtime = request.getParameter("createtime");
                if(null != createtime && !"".equals(createtime.trim()))
                {
                    conditionMap.put("createtime", createtime.trim());
                }
                
                String endtime = request.getParameter("endtime");
                if(null != endtime && !"".equals(endtime.trim()))
                {
                    conditionMap.put("endtime", endtime.trim());
                }
            }else{
            	//第一次进入就选中
            	request.setAttribute("selectAll", "selectAll");
            }
            
            app_climanagerBiz biz=new app_climanagerBiz();
            List<DynaBean>  beans = biz.query(lgcorpcode, conditionMap, pageInfo);
	     	List<DynaBean>  listGroup=biz.querygroup();
	     	//显示左边群组信息
	      	request.setAttribute("appArrayList", listGroup);
	      	//根据条件查询客户列表
            request.setAttribute("clientList", beans);
            request.setAttribute("conditionMap", conditionMap);
            request.setAttribute("lgcorpcode", lgcorpcode);
            request.setAttribute("lguserid", lguserid);
            if(exportExcel!=null&&!"".equals(exportExcel)){
            	request.setAttribute("exportExcel", exportExcel);
            }
            //当前登录用户登录名
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
			
			EmpExecutionContext.info("APP客户管理", lgcorpcode, lguserid, lguserName, opContent, "GET");
            
        }
        catch (Exception e)
        {
        	EmpExecutionContext.error(e, "APP客户管理页面加载出错！");
        }
        finally
        {
            request.setAttribute("pageInfo", pageInfo);
            //为了处理保持之前状态
			String skip=request.getParameter("skip");
			String url="";
			if("1".equals(skip)){
				url="?skip=1";
			}
            request.getRequestDispatcher(PATH + "/app_climanager.jsp"+url).forward(request, response);
        }
    }
    /**
     * APP用户管理列表信息查询（点击查询按钮）
     * @Title: findList
     * @Description: TODO
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @return void
     */
    public void findList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	SimpleDateFormat sdf =new SimpleDateFormat("HH:mm:ss");
    	//查询开始时间
    	long stratTime = System.currentTimeMillis();
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        String page = request.getParameter("page");
        String size = request.getParameter("size");
    	pageInfo.setPageSize(10);
        if(page!=null&&!"".equals(page)){
        	pageInfo.setPageIndex(Integer.parseInt(page));
        }
        if(size!=null&&!"".equals(size)){
        	pageInfo.setPageSize(Integer.parseInt(size));
        }
        
        pageSet(pageInfo, request);
        String lgcorpcode = request.getParameter("lgcorpcode");
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        try
        {
            // 存放查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            // 获取查询条件

                // 用户昵称
                String nickname = request.getParameter("nickname");
                if(null != nickname && !"".equals(nickname.trim()))
                {
                    conditionMap.put("nickname", nickname.trim());
                }
                
                // 性别
                String sex = request.getParameter("sex");
                if(null != sex && !"".equals(sex.trim()))
                {
                    conditionMap.put("sex", sex.trim());
                }
                
                // 账户
                String app_code = request.getParameter("app_code");
                if(null != app_code && !"".equals(app_code.trim()))
                {
                    conditionMap.put("app_code", app_code.trim());
                }
                
                
                String phone = request.getParameter("phone");
                if(null != phone && !"".equals(phone.trim()))
                {
                    conditionMap.put("phone", phone.trim());
                }
                
                String age = request.getParameter("age");
                if(null != age && !"".equals(age.trim()))
                {
                    conditionMap.put("age", age.trim());
                }
                
                String createtime = request.getParameter("createtime");
                if(null != createtime && !"".equals(createtime.trim()))
                {
                    conditionMap.put("createtime", createtime.trim());
                }
                
                String endtime = request.getParameter("endtime");
                if(null != endtime && !"".equals(endtime.trim()))
                {
                    conditionMap.put("endtime", endtime.trim());
                }
                
                String groupid = request.getParameter("groupid");
                if(null != groupid && !"".equals(groupid.trim()))
                {
                    conditionMap.put("groupid", groupid.trim());
                }
                
            // 退群后，为了保持退群之前的状态
			String skip=request.getParameter("skip");
			if("1".equals(skip)){
				conditionMap=(LinkedHashMap)request.getSession(false).getAttribute("appCliManagerMap");
				pageInfo=(PageInfo)request.getSession(false).getAttribute("appCliManagerPageInfo");
			}     
            app_climanagerBiz biz=new app_climanagerBiz();
            List<DynaBean>  beans = biz.query(lgcorpcode, conditionMap, pageInfo);
//	     	List<DynaBean>  listGroup=biz.querygroup();
//	     	//显示左边群组信息
//	      	request.setAttribute("appArrayList", listGroup);
	      	//根据条件查询客户列表
            request.setAttribute("clientList", beans);
            request.setAttribute("conditionMap", conditionMap);
            request.setAttribute("lgcorpcode", lgcorpcode);
            request.setAttribute("lguserid", lguserid);
			request.getSession(false).setAttribute("appCliManagerMap", conditionMap);
			request.getSession(false).setAttribute("appCliManagerPageInfo", pageInfo);
			
            //当前登录用户登录名
            String lguserName = null;
			Object sysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(sysuserObj != null)
			{
				LfSysuser sysuser = (LfSysuser)sysuserObj;
				lguserName = (sysuser.getUserName()!= null && !"".equals(sysuser.getUserName())) ? sysuser.getUserName() : null;
			}					
			//查询出的数据的总数量
			int totalCount = pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间：" + sdf.format(stratTime) + " 耗时："+
			(System.currentTimeMillis()-stratTime) + "ms  数量：" + totalCount;
			
			EmpExecutionContext.info("APP客户管理查询", lgcorpcode, lguserid, lguserName, opContent, "GET");	
			
        }
        catch (Exception e)
        {
        	EmpExecutionContext.error(e, "APP客户管理列表信息查询异常！");
        }
        finally
        {
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(PATH + "/app_webtable.jsp").forward(request, response);
        }
    
    	
    }
    
    /**
	 * 客户列表excel导出
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
		String nickname = request.getParameter("nickname");
		if(nickname!=null){
			nickname = new String(nickname.getBytes("iso8859-1"), "UTF-8");
		}
		String sex=request.getParameter("sex");
		String app_code=request.getParameter("app_code");
		String phone=request.getParameter("phone");
		String age=request.getParameter("age");
		String createtime=request.getParameter("createtime");
		String endtime = request.getParameter("endtime");
		// 用户id
		//String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        String corpcode=request.getParameter("lgcorpcode");
		String groupid = request.getParameter("groupid");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		// 分页对象
		PageInfo pageInfo = new PageInfo();
		try
		{
				conditionMap.put("nickname", nickname);
				conditionMap.put("sex", sex);
				conditionMap.put("app_code", app_code);
				conditionMap.put("phone", phone);
				conditionMap.put("age", age);
				conditionMap.put("createtime", createtime);
				conditionMap.put("endtime", endtime);
                if(null != groupid && !"".equals(groupid.trim()))
                {
                    conditionMap.put("groupid", groupid.trim());
                }
                String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);
				Map<String, String> resultMap = new app_climanagerBiz().createMtHistoryExcel(corpcode, conditionMap, pageInfo,langName);

				if(resultMap != null && resultMap.size() > 0)
				{
					request.getSession(false).setAttribute("app_climanagerMap", resultMap);
					// 文件名称
					//String fileName = (String) resultMap.get("FILE_NAME");
					// 路径
					//String filePath = (String) resultMap.get("FILE_PATH");
					//数量
					String  mtTaskNumber=(String)resultMap.get("mtTaskList");
					
					// 增加操作日志
					Object loginSysuserObj = request.getSession(false).getAttribute(
							"loginSysuser");
					if (loginSysuserObj != null) {
						LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
						String opContent = "导出成功, 开始时间："+sdf.format(stratTime)+
						" 耗时：" + (System.currentTimeMillis() - stratTime) + "ms 数量：" + mtTaskNumber;
						EmpExecutionContext.info("APP用户管理(导出)", loginSysuser.getCorpCode(),
								loginSysuser.getUserId().toString(), loginSysuser
										.getUserName(), opContent, "OTHER");
					}
					//DownloadFile dfs = new DownloadFile();
					// 导出
		            request.setAttribute("conditionMap", conditionMap);
		    		PrintWriter out = response.getWriter();
		    		out.println("true");
					//dfs.downFile(request, response, filePath, fileName);
				}
				else
				{
					response.sendRedirect(request.getContextPath() + "/app_climanager.htm?method=find&exportExcel=exportExcel&lguserid=" + lguserid + "&lgcorpcode=" + corpcode);
				}


		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "客户列表excel导出异常");
		}
	}
	
	
	/**
	 * 查询当前群组的相关信息
	 */
    public void findByGroup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	 PageInfo pageInfo = new PageInfo();
         String page = request.getParameter("page");
         String size = request.getParameter("size");
     	 pageInfo.setPageSize(10);
         if(page!=null&&!"".equals(page)){
         	pageInfo.setPageIndex(Integer.parseInt(page));
         }
         if(size!=null&&!"".equals(size)){
         	pageInfo.setPageSize(Integer.parseInt(size));
         }
         
         pageSet(pageInfo, request);
         String lgcorpcode = request.getParameter("lgcorpcode");
         String groupid=request.getParameter("groupid");
         

        app_climanagerBiz biz =new app_climanagerBiz();
     	List<DynaBean>  listGroup=biz.querygroup();
   		request.setAttribute("appArrayList", listGroup);
	 	LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
	 	 // 用户昵称
        String nickname = request.getParameter("nickname");
        if(null != nickname && !"".equals(nickname.trim()))
        {
            conditionMap.put("nickname", nickname.trim());
        }
        
        // 性别
        String sex = request.getParameter("sex");
        if(null != sex && !"".equals(sex.trim()))
        {
            conditionMap.put("sex", sex.trim());
        }
        
        // 账户
        String app_code = request.getParameter("app_code");
        if(null != app_code && !"".equals(app_code.trim()))
        {
            conditionMap.put("app_code", app_code.trim());
        }
        
        
        String phone = request.getParameter("phone");
        if(null != phone && !"".equals(phone.trim()))
        {
            conditionMap.put("phone", phone.trim());
        }
        
        String age = request.getParameter("age");
        if(null != age && !"".equals(age.trim()))
        {
            conditionMap.put("age", age.trim());
        }
        
        String createtime = request.getParameter("createtime");
        if(null != createtime && !"".equals(createtime.trim()))
        {
            conditionMap.put("createtime", createtime.trim());
        }
        
        String endtime = request.getParameter("endtime");
        if(null != endtime && !"".equals(endtime.trim()))
        {
            conditionMap.put("endtime", endtime.trim());
        }
	    conditionMap.put("groupid", groupid);
	    List<DynaBean>  beans = biz.findByGroup(lgcorpcode, conditionMap, pageInfo);
	    request.setAttribute("lgcorpcode", lgcorpcode);
	    request.setAttribute("clientList", beans);
         request.setAttribute("pageInfo", pageInfo);
         
         request.setAttribute("conditionMap", conditionMap);
         //增加一个值，表示是否被选择
        request.getRequestDispatcher(PATH + "/app_webtable.jsp").forward(request, response);
    }
	//获取机构�?
	public void createTree2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{

			//获取机构树字符串
			String depStr = request.getParameter("depId");
			String departmentTree = getDepartmentJosnData2(depStr);
			
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取操作员机构树出现异常！");
		}
	}

	/**
	 * 树
	 * @return
	 */
	protected String getDepartmentJosnData2(String depStr){
		StringBuffer tree = null;
		app_climanagerBiz biz =new app_climanagerBiz();

			List<DynaBean>  listGroup=biz.querygroup();

		if(listGroup.size()==0||depStr!=null)
		{
			tree = new StringBuffer("[]");
		}else
		{
			
			try {
				tree = new StringBuffer("[");
				for (int i = 0; i < listGroup.size(); i++) {
					DynaBean bean=listGroup.get(i);
					tree.append("{");
					tree.append("id:").append(bean.get("g_id")+"");
					tree.append(",name:'").append(bean.get("name")).append("'");;
					tree.append(",pId:").append("-1");
					tree.append(",depId:").append("'0'");
					tree.append(",dlevel:").append("'-1'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != listGroup.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
		
			} catch (Exception e) {
				EmpExecutionContext.error(e,"操作员机构获取机构出现异常！");
				tree = new StringBuffer("[]");
			}
		}
		return tree.toString();
	}
	
	/**
	 * APP用户管理查询导出
	 * @Title: downloadFile
	 * @Description: TODO
	 * @param  
	 * @return void    返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try {
			Object resultMapObj = request.getSession(false).getAttribute("app_climanagerMap");
			if(resultMapObj != null)
			{
				Map<String, String> resultMap = (Map<String, String>) resultMapObj;
				//文件名
				String fileName=(String)resultMap.get("FILE_NAME");
				//文件路径
			    String filePath=(String)resultMap.get("FILE_PATH");
				
			    DownloadFile dfs=new DownloadFile();
			    dfs.downFile(request, response, filePath, fileName);
			    request.getSession(false).removeAttribute("app_climanagerMap");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EmpExecutionContext.error(e,"APP用户管理查询导出异常!");
		}

	}
	
	
}
