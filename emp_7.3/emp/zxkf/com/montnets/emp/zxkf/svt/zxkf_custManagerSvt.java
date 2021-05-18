/**
 * @description
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2013-12-6 下午12:03:29
 */
package com.montnets.emp.zxkf.svt;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.online.LfOnlGpmsgid;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiUser2Acc;
import com.montnets.emp.entity.wxsysuser.LfSysUser;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.znly.biz.CustomChatBiz;
import com.montnets.emp.znly.biz.CustomStatusBiz;
import com.montnets.emp.zxkf.biz.CustomerBiz;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2013-12-6 下午12:03:29
 */

public class zxkf_custManagerSvt extends BaseServlet {
	// 资源路径
	private static final String PATH = "/zxkf/customer";
	// 基础逻辑层
	private final BaseBiz baseBiz = new BaseBiz();
	// 客服逻辑层
	private final CustomerBiz customerBiz = new CustomerBiz();
	
	/**
     * 客服列表及查询
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-13 下午07:14:39
    */
	public void find(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter = pageSet(pageInfo, request);
		// conditionMap.put("aId", aId);
		try {
			// 当前企业
			String lgcorpcode = request.getParameter("lgcorpcode");

			// 查询所有公众帐号
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			List<LfWeiAccount> otWeiAcctList = baseBiz.findListByCondition(
					LfWeiAccount.class, conditionMap, null);
			
			
			if (!isFirstEnter) {
				// 当前公众帐号
				String aId = request.getParameter("aId");
				if (aId != null && !"".equals(aId.trim())) {
					conditionMap.put("AId", aId);
				}
				// 客服名称
				String name = request.getParameter("name");
				if (name != null && !"".equals(name)) {
					conditionMap.put("name", name);
				};
			}
			List<DynaBean> beans = customerBiz.findCustomerList(lgcorpcode,
					conditionMap, pageInfo);

			request.setAttribute("custUserList", beans);
			request.setAttribute("otWeiAcctList", otWeiAcctList);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "在线客服-在线客服列表获取失败。");
		} finally {
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(PATH + "/zxkf_custList.jsp").forward(
					request, response);
		}
	}
	
	
	/**
     * 绑定公众号
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-13 下午07:14:39
     */
    public void doBind(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        try{
            String userid = request.getParameter("userid");
            String aid = request.getParameter("aid");
            String opContent = null;
            if(userid == null || aid == null)
            {
            	response.getWriter().print("error");
            	return;
            }
            
            LfWeiUser2Acc user2Acc = new LfWeiUser2Acc();
            user2Acc.setAId(Long.valueOf(aid));
            user2Acc.setUserId(Long.valueOf(userid));
            
            if(baseBiz.addObj(user2Acc))
            {
            	 opContent = "绑定公众账号成功。";
            	response.getWriter().print("success");
            }else
            {
            	 opContent = "绑定公众账号失败。";
            	response.getWriter().print("error");
            }
          //增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				 opContent += "[用户ID,群组ID]("+userid+","+aid+")";
				EmpExecutionContext.info("在线坐席客服", loginSysuser.getCorpCode(),
						loginSysuser.getUserId().toString(), loginSysuser
								.getUserName(), opContent, "OTHER");
			}
        }catch(Exception e){
            EmpExecutionContext.error(e, "在线客服-绑定公众号doBind方法异常！");
            response.getWriter().print("error");
        }
    }
    
    
    /**
     * 解除公众号
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-13 下午07:14:39
     */
    public void doUnBind(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        try{
        	String userid = request.getParameter("userid");
        	String aid = request.getParameter("aid");
            if(userid == null )
            {
            	response.getWriter().print("error");
            	return;
            }
            if(CustomChatBiz.getSerCountMap().get(userid) != null)
            {
            	response.getWriter().print("online");
            	return;
            }
            if(!CustomChatBiz.checkWeixServer(userid))
            {
            	response.getWriter().print("inserver");
            	return;
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            
            conditionMap.put("userId", userid);
            String opContent=null;
            if(baseBiz.deleteByCondition(LfWeiUser2Acc.class, conditionMap) > 0)
            {
            	opContent="解绑公众账号成功。";
            	response.getWriter().print("success");
            	// 移除微信客服队列
            	CustomChatBiz.removeWeiCountMap(aid, userid);
            }else
            {
            	opContent="解绑公众账号失败。";
            	response.getWriter().print("error");
            }
            //增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				 opContent += "[用户ID,群组ID]("+userid+","+aid+")";
				EmpExecutionContext.info("在线坐席客服", loginSysuser.getCorpCode(),
						loginSysuser.getUserId().toString(), loginSysuser
								.getUserName(), opContent, "OTHER");
			}
        }catch(Exception e){
            EmpExecutionContext.error(e, "在线客服-添加客服doEdit方法异常！");
        }
    }
	/**
     * 添加客服
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-13 下午07:14:39
     */
	public void doAdd(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		try{
			String lgcorpcode = request.getParameter("lgcorpcode");
			// 查询所有公众帐号
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			List<LfWeiAccount> otWeiAcctList = baseBiz.findListByCondition(LfWeiAccount.class, conditionMap, null);
			request.setAttribute("otWeiAcctList", otWeiAcctList);
		}catch(Exception e){
			EmpExecutionContext.error(e, "在线客服-添加客服doAdd方法异常！");
		}finally{
			request.getRequestDispatcher(PATH + "/zxkf_addCust.jsp").forward(request, response);
		}
	}
	
	 /**
     * 编辑客服信息
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-13 下午07:14:39
     */
    public void doEdit(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
        try{
            String lgcorpcode = request.getParameter("lgcorpcode");
            String custId = request.getParameter("custId");
            // 查询所有公众帐号
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            List<LfWeiAccount> otWeiAcctList = baseBiz.findListByCondition(LfWeiAccount.class, conditionMap, null);
            
            //查询客服
            LfSysUser otSysUser = baseBiz.getById(LfSysUser.class, custId);
            
            request.setAttribute("otWeiAcctList", otWeiAcctList);
            request.setAttribute("otSysUser", otSysUser);
        }catch(Exception e){
            EmpExecutionContext.error(e, "在线客服-添加客服doEdit方法异常！");
        }finally{
            request.getRequestDispatcher(PATH + "/zxkf_editCust.jsp").forward(request, response);
        }
    }
    
    /**
     * 重置密码
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-13 下午07:14:39
     */
    public void resetPwd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            String lgcorpcode = request.getParameter("lgcorpcode");
            String custId = request.getParameter("custId");
            // 查询所有公众帐号
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            List<LfWeiAccount> otWeiAcctList = baseBiz.findListByCondition(LfWeiAccount.class, conditionMap, null);
            
            //查询客服
            LfSysUser otSysUser = baseBiz.getById(LfSysUser.class, custId);
            
            request.setAttribute("otWeiAcctList", otWeiAcctList);
            request.setAttribute("otSysUser", otSysUser);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "在线客服-加载重置密码页面出错！");
        }
        finally
        {
            request.getRequestDispatcher(PATH + "/zxkf_resetPwd.jsp").forward(request, response);
        }
    }
    
	/**
	 * 添加和编辑客服信息
	 * @param reques
	 * @param response
	 */
	public void update(HttpServletRequest request,HttpServletResponse response){
		String result = "";
		response.setContentType("text/html");
		PrintWriter out = null;
		try{
			//企业编号
			String corpcode = request.getParameter("lgcorpcode");
			//公众帐号
			String aId = request.getParameter("aId");
			//客服名称
            String name = request.getParameter("name");
			//客服帐号
			String userName = request.getParameter("userName");
			//密码
			String pwd = request.getParameter("pwd");
			//所属身份
			String ptype = request.getParameter("ptype");
			//操作类型
			String optype = request.getParameter("optype");
			//帐号状态
			String state = request.getParameter("state");
			//当前客服
            String custId = request.getParameter("custId");
            String optype2="OTHRE";
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corpcode);
            conditionMap.put("userName", userName);
            String opContent="添加和编辑客服信息[企业编号,公众帐号,客服名称,客服帐号,密码,所属身份,帐号状态]";
            List<LfSysUser> userlist = baseBiz.findListByCondition(LfSysUser.class, conditionMap, null);
            if(userlist!=null&&userlist.size()>0)
            {
            	LfSysUser osu=userlist.get(0);
            	opContent+="("+osu.getCorpCode()+","+osu.getAId()+","+osu.getName()+","+osu.getUserName()+","+
            	osu.getPassword()+","+osu.getPermissionType()+","+osu.getUserState()+")";
                result = "exist";
            }else{
                if("add".equals(optype)){
                    //帐号不能重复判断
                    Long resultId = customerBiz.createCustomerReturnId(corpcode, aId, name, userName, ptype, state, pwd);
                    if(resultId != null && resultId > 0)
                    {
                        LfOnlGpmsgid gpmsg = new LfOnlGpmsgid();
                        gpmsg.setGmUser(resultId);
                        gpmsg.setMsgId(0L);
                        baseBiz.addObj(gpmsg);
                        
                        // 将新增的用户存入状态监控内存中
                        CustomStatusBiz.getCustomStatusMap().put(resultId.toString(), 4);
                        opContent+="("+corpcode+","+aId+","+name+","+userName+","+
                        pwd+","+ptype+","+state+")新增成功。";
                        // 新增成功
                        result = "success";
                        optype2="ADD";
                    }
                    else
                    {
                    	   opContent+="("+corpcode+","+aId+","+name+","+userName+","+
                           pwd+","+ptype+","+state+")新增失败。";
                        // 新增失败
                        result = "error";
                    } 
                }else if("edit".equals(optype)){
                    Boolean resultId = customerBiz.updateCustomerReturnId(aId,custId,name, userName, ptype,state);
                    if(resultId)
                    {
                    	 opContent+="-->("+corpcode+","+aId+","+name+","+userName+","+
                         pwd+","+ptype+","+state+")更新成功。";
                        // 更新成功
                        result = "success";
                        optype2="UPDATE";
                    }
                    else
                    {
                   	 opContent+="-->("+corpcode+","+aId+","+name+","+userName+","+
                     pwd+","+ptype+","+state+")更新失败。";
                        // 更新失败
                        result = "error";
                    }
                }else if("pwd".equals(optype)){
                    Boolean resultId = customerBiz.resetCustomerPwdReturnId(custId,pwd);
                    if(resultId)
                    {
                        // 更新成功
                        result = "success";
                    }
                    else
                    {
                        // 更新失败
                        result = "error";
                    }
                }
            }
            //增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info("在线坐席客服", loginSysuser.getCorpCode(),
						loginSysuser.getUserId().toString(), loginSysuser
								.getUserName(), opContent, optype2);
			}
			out = response.getWriter();
			out.print(result);
		}catch(Exception e){
			EmpExecutionContext.error(e, "在线客服-添加或编辑客服update方法异常！");
		}finally{
			if(out!=null){
			out.close();
			}
		}
	}
	
}
