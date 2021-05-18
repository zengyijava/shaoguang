package com.montnets.emp.zxkf.svt;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.online.LfOnlGroup;
import com.montnets.emp.entity.online.LfOnlRemark;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.ottbase.util.FFmpegKit;
import com.montnets.emp.ottbase.util.StringUtils;
import com.montnets.emp.ottbase.util.TxtFileUtil;
import com.montnets.emp.ottbase.util.WavFileUtil;
import com.montnets.emp.znly.biz.CustomChatBiz;
import com.montnets.emp.znly.biz.CustomStatusBiz;
import com.montnets.emp.znly.biz.CustomTestMode;
import com.montnets.emp.znly.biz.GroupChatBiz;

/**
 * 在线客服servlet
 * 
 * @description
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author linzhihan <zhihanking@163.com>
 * @datetime 2013-11-29 下午03:34:52
 */
@SuppressWarnings("serial")
public class zxkf_customChatSvt extends BaseServlet
{
	private final BaseBiz                     baseBiz       = new BaseBiz();

    // 资源路径
    private static final String PATH          = "/zxkf/";

    private final CustomChatBiz               customChatBiz = new CustomChatBiz();

    /**
     * 进入客服聊天系统
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-5 上午10:07:28
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	String userid = request.getParameter("userid");
        HttpSession session = request.getSession(false);
    	if(session == null || session.getAttribute("loginSysuser") == null)
    	{
    		 response.sendRedirect(request.getContextPath() + "/");
             return;
    	}
        // 当前客服人员
        LfSysuser sysuser = (LfSysuser) session.getAttribute("loginSysuser");//baseBiz.getById(OtSysUser.class, loginUserInfo.getUserId());
        if(sysuser == null || !userid.equals(sysuser.getUserId().toString()))
        {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        String AId = customChatBiz.getAidByUserid(userid);
        // 公众号信息
        if(AId != null)
        {
	        LfWeiAccount account = baseBiz.getById(LfWeiAccount.class, AId);
	        request.setAttribute("account", account);
        }
        
        // 获取同属于一个公众号的客服人员集合json格式数据
        String userJson = customChatBiz.getAccountUserJson(sysuser.getUserId().toString(),AId,0);
        // 获取群组集合
        String groupJson = customChatBiz.getCustomeGroupList(sysuser.getUserId().toString());

        // 获取已接入的客户信息
        String userInfoJson = customChatBiz.getChatUserInfos(userid.toString(), AId);
        //  获取已接入的APP信息
        String appInfoJson = customChatBiz.getAppUserInfos(userid.toString());
        // 初始化客服服务状态
        customChatBiz.initServiceCount(sysuser.getUserId().toString(),AId);
        // 发送状态修改信息
        CustomStatusBiz.changeCustomStatus(sysuser.getUserId().toString(),1);
        
        request.setAttribute("groupJson", groupJson);
        request.setAttribute("sysuser", sysuser);
        request.setAttribute("userJson", userJson);
        request.setAttribute("appInfoJson", appInfoJson);
        
        request.setAttribute("userInfoJson", userInfoJson);
        request.getRequestDispatcher(PATH + "chat.jsp").forward(request, response);
    }

    /**
     * 检测客服身份
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-5 上午10:07:39
     */
    @SuppressWarnings("unchecked")
	public void checkUser(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	String userid = request.getParameter("userid");
    	String tkn = request.getParameter("tkn");
    	String msg = "";
    	String AId = null ;
    	JSONObject resultJson = new JSONObject();
    	try
    	{
	    	LfSysuser user = baseBiz.getById(LfSysuser.class, userid);
	    	
	    	//String AId = customChatBiz.getAidByUserid(userid);
	    	if(user == null || user.getIsCustome() != 1)
	    	{
	    		AId = "0";
	    		msg = "非客服身份或未配置客服权限 ，无法打开客服模块。";
	    	}else
	    	{
	    		AId = "1";
	    	}
	    	
    	}catch(Exception e)
    	{
    		AId = "0";
    		msg = "验证客服身份失败！";
    		EmpExecutionContext.error(e,"验证客服身份失败 ！");
    	}
    	resultJson.put("aid", AId);
    	resultJson.put("msg", msg);
    	resultJson.put("url", "customChat.htm?userid="+userid+"&tkn="+tkn);
    	response.getWriter().print(resultJson.toString());
    }
    /**
     * 检测消息
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-5 上午10:07:39
     */
    @SuppressWarnings("unchecked")
    public void checkMsg(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // 检测次数
        int dex = 1;
        // 客服ID
        String customId = request.getParameter("customId");
        // 已读消息数，用于删除消息内存
        String len = request.getParameter("len");
        // 公众号ID
        String aid = request.getParameter("aid");

        // 群组Id集合
        String groupKeys = request.getParameter("groupKeys");
        // 已读消息的最大ID
        String msgId = request.getParameter("msgId");
        Long newId = 0l;
        if(msgId != null && !"".equals(msgId))
        {
            newId = Long.valueOf(msgId);
        }
        // 获取的消息
        String msg = "";
        // 清除已读消息内存
        CustomChatBiz.removeMess(customId, len, aid);
        // 更新已读最大消息ID
        GroupChatBiz.updateGpMsgId(newId, customId);
        JSONObject resultJson = new JSONObject();
        GroupChatBiz groupChatBiz = new GroupChatBiz();
        while(dex < 61)
        {
        	
            // 读取属于个人的消息
        	resultJson.put("custmsg", CustomChatBiz.getMsg(customId));
            // 查找群组的消息
            //jsonArr = GroupChatBiz.getGroupMsg(groupKeys, customId, System.currentTimeMillis());
        	resultJson.put("groupmsg",groupChatBiz.getGroupMsg(groupKeys, customId));
            // 如果有查找到消息
            if(resultJson != null && resultJson.toString().length()> 30)
            {
                dex = 61;
                msg = resultJson.toString();
            }
            else
            {
                dex++;
                Thread.sleep(1000);
            }
        }
        Thread.sleep(1000);
        response.getWriter().print("chat:" + (msg == null ? "" : msg));
    }

    /**
     * 客服消息推送
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-5 上午10:07:20
     */
    public void sendMsg(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String customId = request.getParameter("customId");
        String toUser = request.getParameter("toUser");
        String msg = request.getParameter("msg");
        String msgType = request.getParameter("msgType");
        String pushType = request.getParameter("pushType");
        String openId = request.getParameter("openId");
        String name = request.getParameter("name");
        String aId = request.getParameter("aId");
        String ecode = request.getParameter("ecode");
        msg=msg.replace(" ", " ");// 重要注意事项，前面的那个空格不是真的空格，在数据库会被识别为问号。所以这一句看上去有问题，其实必不可少啊
        // 是否发给客服
        if("3".equals(pushType))
        {
            String servernum = customId + "to" + toUser;
            response.getWriter().print("chat:" + CustomChatBiz.setMsgJsonBefore(toUser, servernum, customId, msgType, msg, pushType, name, Long.valueOf(aId),0));
        }
        // 是否发给群组
        else if("4".equals(pushType))
        {
            response.getWriter().print("chat:" + GroupChatBiz.setGroupMsgJson(toUser, customId, msgType, msg, name));
        }
        // 是否发给微信
        else if("2".equals(pushType))
        {
            // 走下发手机流程
            response.getWriter().print("chat:" + CustomChatBiz.doSendBefore(toUser, msg, msgType, openId, customId));
        }
        // 是否发给手机APP
        else if("7".equals(pushType))
        {
            // 走下发手机流程
            response.getWriter().print("chat:" + CustomChatBiz.doSendApp(toUser, msg, msgType, customId, ecode));
        }
        else
        {
        	EmpExecutionContext.error("无效的下发类型pushtype="+pushType+",customId="+customId+",toUser="+toUser);
            response.getWriter().print("chat:error");
        }
    }

    /**
     * 修改客服状态
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-5 上午10:07:46
     */
    public void changeState(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String customId = request.getParameter("customId");
        int status = Integer.parseInt(request.getParameter("status"));
        String aId = request.getParameter("aId");

        CustomStatusBiz.changeCustomStatus(customId, status);
    }

    /**
     * 客服状态检测
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-5 上午10:07:46
     */
    public void checkState(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // String corpcode = request.getParameter("corpcode");
        String aid = request.getParameter("aId");

        String customid = request.getParameter("customId");
        int dex = 1;
        CustomStatusBiz.setUserOnlineTiem(customid, aid);
        // 更新客服的服务数量
        customChatBiz.initServiceCount(customid, aid);
        JSONObject statusJSONObject = new JSONObject();
        while(dex < 60)
        {
            statusJSONObject.putAll(CustomStatusBiz.getChangeStatusMap());
            statusJSONObject.remove(customid);
            if(null == statusJSONObject || statusJSONObject.size() <= 0)
            {
                dex++;
                Thread.sleep(1000);
            }
            else
            {
                dex = 61;
            }
        }
        response.getWriter().print("status:" + statusJSONObject.toString());
        

    }

    /**
     * 单图发送
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-5 上午10:10:22
     */
    public void sendPic(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String fileName = request.getParameter("fileName");
        String fileType = request.getParameter("fileType");
        String modType = request.getParameter("modtype");
        String dataId = request.getParameter("data_id");
        String msgtype = "image";

        WeixBiz weixBiz = new WeixBiz();
        String[] path = weixBiz.getResourceUrl(msgtype,fileType,modType);//weixBiz.getWeixResourceUrl(msgtype);//去掉老方法，新增对APP支持原图发送
        // 上传文件到web服务器
        String result = weixBiz.uploadToServer(path, msgtype, fileName, request);

        JSONObject resultObj = new JSONObject();
        resultObj.put("filepath", path[1]);
        if("success".equals(result)){
        	result = result+":"+dataId;
        }
        resultObj.put("result", result);
        response.getWriter().print(resultObj.toString());
        // request.getRequestDispatcher("/online_uploadResult.jsp").forward(request,
        // response);
    }

    /**
     * 转接客服
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-5 上午10:10:22
     */
    public void converCust(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // 服务号
        String serverNum = request.getParameter("serverNum");
        // 发起转接的客服Id
        String customeId = request.getParameter("customeId");
        // 转接目标
        String toCustome = request.getParameter("toCustome");
        // 公众号
        String aId = request.getParameter("aId");

        // 发起转接的客服姓名
        String name = request.getParameter("name");

        // 判断是否取消转接请求
        String iscancel = request.getParameter("iscancel");
        if("1".equals(iscancel))
        {
            String cancelResult = customChatBiz.cancelConver(serverNum, customeId, toCustome, Long.valueOf(aId), name);
            response.getWriter().print(cancelResult);
            return;
        }
        // 转接说明
        String msg = request.getParameter("msg");
        // 客户微信号
        String openId = request.getParameter("openId");
        JSONObject json = new JSONObject();
        json.put("openid", openId);
        json.put("servernum", serverNum);
        json.put("msg", msg);

        String result = customChatBiz.turnCustome(serverNum, customeId, toCustome, json.toString(), Long.valueOf(aId), name);
        response.getWriter().print(result);
    }

    /**
     * 处理转接信息
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-15 下午04:44:23
     */
    public void agreeConver(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // 服务号
        String serverNum = request.getParameter("serverNum");
        // 发起转接的客服Id
        String fromCust = request.getParameter("fromCust");
        // 转接目标
        String customeId = request.getParameter("toCustome");
        // 公众号ID
        String aId = request.getParameter("aId");
        // 客户微信号
        String openId = request.getParameter("openId");

        String code = request.getParameter("code");

        String result = customChatBiz.agreeConver(serverNum, fromCust, customeId, Long.valueOf(aId), openId, code);
        response.getWriter().print("conver:" + result);
    }

    /**
     * 创建群组
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-5 上午10:10:59
     */
    public void addGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // 群组的名称
        String gpName = request.getParameter("gpName");
        // 创建者
        String userId = request.getParameter("userId");
        String customeId = request.getParameter("customeId");
        String aId = request.getParameter("aId");
        
        String result = "error";
        JSONObject groupJSONObject = new JSONObject();
        GroupChatBiz gpBiz = new GroupChatBiz();
        try
        {
        	// 获取检验名称是否重复的结果
        	int checkNameResult = gpBiz.checkGroupName(customeId, gpName);
        	if(checkNameResult > 0)
        	{
	            LfOnlGroup group = gpBiz.createGroup(gpName, userId,aId,customeId);
	            if(group != null && group.getGpId() != null)
	            {
	                groupJSONObject.put("groupid", group.getGpId());
	                groupJSONObject.put("name", group.getGpName());
	                groupJSONObject.put("count", group.getMemCount());
	                result = "group:" + groupJSONObject.toString();
	            }
	            
        	}
        	else if(checkNameResult == 0)
        	{
        		result = "samename";
        	}
        	//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent = "创建群组名称成功。[群组ID,群组名]("+aId+","+gpName+")";
				EmpExecutionContext.info("在线坐席客服", loginSysuser.getCorpCode(),
						loginSysuser.getUserId().toString(), loginSysuser
								.getUserName(), opContent, "ADD");
			}
        	
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "addGroup-创建群组失败！");
        }
        finally
        {
            response.getWriter().print(result);
        }
    }

    /**
     * 删除群组
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-5 上午10:10:59
     */
    public void delGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // 群组的Id
        String gpId = request.getParameter("gpId");

        String result = "";
//      JSONObject groupJSONObject = new JSONObject();  //findbugs
        try
        {
        	String opContent = null;
            boolean success = new GroupChatBiz().delGroup(gpId);
            if(success)
            {
            	opContent = "删除群组名称成功";
                result = "success";
            }
            else
            {
            	 opContent = "删除群组名称失败";
                result = "error";
            }
        	//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				 opContent += "[群组ID]("+gpId+")";
				EmpExecutionContext.info("在线坐席客服", loginSysuser.getCorpCode(),
						loginSysuser.getUserId().toString(), loginSysuser
								.getUserName(), opContent, "DELETE");
			}
        	
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "delGroup-删除群组失败！");
        }
        finally
        {
            response.getWriter().print(result);
        }
    }

    /**
     * 加入群组
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2013-12-20 上午10:22:41
     */
    public void joinGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // 预添加的成员Ids
        String gpmIds = request.getParameter("gpmIds");
        // 群组Id
        String gpId = request.getParameter("gpId");

        String result = "";
        String opContent = null;
        try
        {
            if(StringUtils.isNotEmpty(gpmIds))
            {
                boolean success = new GroupChatBiz().joinGroup(gpId, gpmIds);
                if(success)
                {
                    opContent = "加入群组名称成功。";
                    result = "success";
                }
                else
                {
                	opContent = "加入群组名称失败";
                    result = "error";
                }
            }
            else
            {
                result = "noIds";
            }
          //增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				 opContent += "[群组ID,用户id]("+gpId+","+gpmIds+")";
				EmpExecutionContext.info("在线坐席客服", loginSysuser.getCorpCode(),
						loginSysuser.getUserId().toString(), loginSysuser
								.getUserName(), opContent, "ADD");
			}
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "delGroup-删除群组失败！");
        }
        finally
        {
            response.getWriter().print(result);
        }
    }

    /**
     * 退群请求
     * 
     * @description
     * @param request
     * @param response
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2013-12-20 下午02:46:36
     */
    public void leaveGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // 群组Id
        String gpId = request.getParameter("groupid");
        String customeId = request.getParameter("customeid");
        String name = request.getParameter("name");

        String result = "";
        String opContent = null;
        try
        {
            if(StringUtils.isNotEmpty(gpId))
            {
                int deleteCount = new GroupChatBiz().leaveGroup(gpId, customeId, name);
                if(deleteCount > 0)
                {
                	 opContent = "退出群组名称成功";
                    result = "success";
                }
                else
                {
                	opContent = "退出群组名称失败";
                    result = "error";
                }
            }
            else
            {
            	opContent = "退出群组名称失败";
                result = "noIds";
            }
          //增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				 opContent += "[群组ID,用户id，用户名称]("+gpId+","+customeId+","+name+")";
				EmpExecutionContext.info("在线坐席客服", loginSysuser.getCorpCode(),
						loginSysuser.getUserId().toString(), loginSysuser
								.getUserName(), opContent, "DELETE");
			}
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "delGroup-删除群组失败！");
        }
        finally
        {
            response.getWriter().print("group:" + result);
        }

    }

    /**
     * 加载未读消息
     * 
     * @description
     * @param request
     * @param response
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-15 下午04:43:48
     */
    public void loadUnReadMsg(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // 群组Id集合
        String groupKeys = request.getParameter("groupKeys");
        // 客服ID
        String customeId = request.getParameter("customeId");

        CustomChatBiz cusChatBiz = new CustomChatBiz();
        GroupChatBiz groupChatBiz = new GroupChatBiz();

        JSONArray resultArray = groupChatBiz.getGroupMaxInfo(customeId, groupKeys);
        // 将未读的客服消息读入内存
        cusChatBiz.getUnReadMsg(customeId);
        if(resultArray != null)
        {
            response.getWriter().print("chat:" + resultArray.toString());
        }
        else
        {
            response.getWriter().print("chat:");
        }
    }

    /**
     * 获取群组成员
     * @description    
     * @param request
     * @param response
     * @throws IOException       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-17 上午11:18:19
     */
    public void getGroupMember(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
    	String groupKey = request.getParameter("groupKey");
    	String aid = request.getParameter("aid");
        String customId = request.getParameter("userCustomeId");
        GroupChatBiz groupChatBiz = new GroupChatBiz();
        JSONArray resultArray = groupChatBiz.getGroupMember(groupKey,aid,customId);

        if(resultArray != null)
        {
            response.getWriter().print("chat:" + resultArray.toString());
        }
        else
        {
            response.getWriter().print("chat:");
        }
    }

    /**
     * 跳转到群组添加页面
     * 
     * @description
     * @param request
     * @param response
     * @throws IOException
     * @author linzhihan <zhihanking@163.com>
     * @throws ServletException
     * @datetime 2014-1-8 上午11:04:59
     */
    public void showAddGroup(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String aId = request.getParameter("aId");
        String customeId = request.getParameter("customeId");
        String userJson = "";
        try
        {
            userJson = customChatBiz.getAccountUserJson(customeId, aId,0);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取客服人员列表信息失败！");
        }
        request.setAttribute("userJson", userJson);
        request.getRequestDispatcher(PATH + "group.jsp").forward(request, response);
    }

    /**
     * 跳转到客服转接页面
     * 
     * @description
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-14 下午05:15:51
     */
    public void showTrans(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String aId = request.getParameter("aId");
        String customeId = request.getParameter("customeId");
        String pushType = request.getParameter("pushType");
        if("7".equals(pushType))
        {
        	aId = "0";
        }
        String userJson = "";
        try
        {
            userJson = customChatBiz.getAccountUserJson(customeId, aId,1);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取客服人员列表信息失败！");
        }
        request.setAttribute("userJson", userJson);
        request.getRequestDispatcher(PATH + "tranService.jsp").forward(request, response);
    }

    /**
     * 打开聊天窗口时加载历史消息记录
     * 
     * @description
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-15 下午02:17:06
     */
    public void readHisMsg(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        // 当前客服Id
        String customeId = request.getParameter("customeId");
        // 聊天对象标识
        String objId = request.getParameter("objId");
        // 推送类型
        String pushType = request.getParameter("pushType");
        // 消息ID
        String msgId = request.getParameter("msgId");
        // 查询条数
        String count = request.getParameter("count");

        String serverNum = "";
        String anServerNum = null;
        if("2".equals(pushType))
        {
            serverNum = objId;
        }
        else if("3".equals(pushType))
        {
            serverNum = customeId + "to" + objId;
            anServerNum = objId + "to" + customeId;
        }
        else if("4".equals(pushType))
        {
            serverNum = "group" + objId;
        }
        else if("7".equals(pushType))
        {
            serverNum = objId;
            anServerNum = customeId;
        }

        String result = customChatBiz.loadMsgHis(count, serverNum, anServerNum, msgId,pushType);
        response.getWriter().print("msghis:" + result);
    }

    /**
     * 退出在线客服窗口
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	String userid = request.getParameter("userId");
    	String aId = request.getParameter("aId");
    	if(userid == null )
    	{
    		return;
    	}
		CustomStatusBiz.changeCustomStatus(userid, 4);
		CustomChatBiz.removeServerCount(userid, aId);
    }
   
    /***
     * 播放音频
    * @Description: TODO
    * @param @param request
    * @param @param response
    * @param @throws IOException
    * @param @throws ServletException
    * @return void
     */
    public void playVoice(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	String webRoot = this.getServletContext().getRealPath("/");
    	EmpExecutionContext.info("webroot:"+webRoot);
    	String voiceFile = request.getParameter("voiceFile");
    	File file = new File(webRoot+File.separator+ voiceFile) ;
    	if(!file.exists())
    	{
    		String palySrc = file.getAbsolutePath();
            String amrPath = palySrc.replaceAll("\\.[a-zA-Z0-9]+$",".amr");
            File source = new File(amrPath);
            if(!source.exists())
            {
            	String cafPath = palySrc.replaceAll("\\.[a-zA-Z0-9]+$",".caf");
            	source = new File(cafPath);
            }
            if(!source.exists()){
            	response.getWriter().print("noFile");
            	return;
            }
            FFmpegKit.converAudio(source.getAbsolutePath());
    	}
    	response.getWriter().print("voiceSuccess");
    }
    
    /**
     * 为客服人员添加备注
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void addRemark(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	String userId = request.getParameter("userId");
    	String markId = request.getParameter("markId");
    	String markName = request.getParameter("markName");
    	if(userId == null || markId == null || markName == null)
    	{
    		response.getWriter().print("error");
    	}
    	LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
    	conditionMap.put("userId", userId);
    	conditionMap.put("markId", markId);
    	LfOnlRemark reamrk;
    	
    	try {
    		List<LfOnlRemark> remarkList = baseBiz.getByCondition(LfOnlRemark.class, conditionMap, null);
        	if(remarkList != null && remarkList.size() > 0)
        	{
        		LinkedHashMap<String,String> objectMap = new LinkedHashMap<String, String>();
        		objectMap.put("markName", markName);
        		if(baseBiz.update(LfOnlRemark.class, objectMap, conditionMap))
    			{
    				response.getWriter().print("success");
    			}
	        	return;	
        	}
        	reamrk = new LfOnlRemark(Long.valueOf(userId), Long.valueOf(markId), markName);
			if(baseBiz.addObj(reamrk))
			{
				response.getWriter().print("success");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"设置客服人员备注失败！");
		}
    }
    
    /***
     * 跳转到测试模式页面
    * @Description: TODO
    * @param @param request
    * @param @param response
    * @param @throws IOException
    * @param @throws ServletException
    * @return void
     */
    public void testMode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	 // 获取同属于一个公众号的客服人员集合json格式数据
        try {
			String userJson = customChatBiz.getAccountUserJson("0","0",0);
			request.setAttribute("userJson", userJson);
			
			//  获取已接入的APP信息
	        String appInfoJson = customChatBiz.getAppUserInfos("2");
	        request.setAttribute("appInfoJson", appInfoJson);
		} catch (Exception e) {
		}
    	request.getRequestDispatcher(PATH + "testMode.jsp").forward(request, response);
    }
    
    /***
     *  调整调试模式
    * @Description: TODO
    * @param @param request
    * @param @param response
    * @param @throws IOException
    * @param @throws ServletException
    * @return void
     */
    public void changeMode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	String ist = request.getParameter("ist");
    	if("0".equals(ist))
    	{
    		CustomTestMode.setTestMode(false);
    	}else
    	{
    		CustomTestMode.setTestMode(true);
    	}
    }
    
    /**
     * 封装客服推送消息
    * @Description: TODO
    * @param @param request
    * @param @param response
    * @param @throws IOException
    * @param @throws ServletException
    * @return void
     */
    public void sendwx(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	String fromUser = request.getParameter("fromUser");
    	String toCusUser = request.getParameter("toCusUser");
    	String serverNum = request.getParameter("serverNum");
    	String msg = request.getParameter("msg");
    	String name = request.getParameter("name");
    	String pushType = request.getParameter("pushType");
    	String AId = request.getParameter("AId");
    	
    	String result = CustomTestMode.setMsgJson(toCusUser, serverNum, fromUser, "text", msg, pushType, name, Long.valueOf(AId));
    	response.getWriter().print(result);
    }
    
    /***
     * 发送APP消息
    * @Description: TODO
    * @param @param request
    * @param @param response
    * @param @throws IOException
    * @param @throws ServletException
    * @return void
     */
    public void sendApp(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	String fromUser = request.getParameter("fromUser");
    	String msg = request.getParameter("msg");
    	String fromName = request.getParameter("fromName");
    	JSONObject json = new JSONObject();
    	JSONObject styleJson = new JSONObject();
    	JSONObject testJson = new JSONObject();
    	json.put("ECODE", "10001");
    	json.put("MSGTYPE", "0");
    	json.put("FROM", fromUser);
    	json.put("FROMNAME", fromName);
    	
    	testJson.put("content", msg);
    	styleJson.put("PMessageStyle1", testJson);
    	
    	json.put("pMessageStyles", styleJson);
    	msg = json.toString();
    	json = StringUtils.parsJsonObj(msg);
    	CustomChatBiz.getAppMsg(json);
    	Timestamp sendTime = new Timestamp(System.currentTimeMillis());
    	response.getWriter().print(StringUtils.timeFormat(sendTime));
    }
    
    /***
     * 检测客服推送消息 
    * @Description: TODO
    * @param @param request
    * @param @param response
    * @param @throws Exception
    * @return void
     */
    public void testMess(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	int i = Integer.parseInt(request.getParameter("len"));
        // 检测次数
        int dex = 1;
        String resultStr="";
        if(i > 0)
        {
        	CustomTestMode.remove(i);
        }
        while(dex < 61)
        {
        	resultStr = CustomTestMode.getmsg();
        	if(resultStr == null || resultStr.length() < 3)
        	{
        		Thread.sleep(1000);
        	}else
        	{
        		break;
        	}
        }
        Thread.sleep(1000);
        response.getWriter().print((resultStr == null ? "" : resultStr));
    }
    
    /***
     * 跳转到信息页面
    * @Description: TODO
    * @param @param request
    * @param @param response
    * @param @throws IOException
    * @param @throws ServletException
    * @return void
     */
    public void getInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	request.getRequestDispatcher(PATH + "info.jsp").forward(request, response);
    }
    
    /***
     * 音频文件处理
    * @Description: TODO
    * @param @param request  请求
    * @param @param response 回应
    * @param @throws IOException
    * @param @throws ServletException
    * @return void
     */
    public void wav(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
    	String webRoot = new TxtFileUtil().getWebRoot();
    	String voiceFile = request.getParameter("file");
    	String type = request.getParameter("type");
    	if(type == null)
    	{
    		type ="2";
    	}
    	String amr = ".amr";
    	File file = new File(webRoot + voiceFile.replace(amr, "_"+type+".au")) ;
		StringBuffer ssb = new StringBuffer(voiceFile);
		
        String resultFile = ssb.replace(voiceFile.lastIndexOf("."),voiceFile.length(),amr).toString();
        File source = new File(webRoot + resultFile);
    	WavFileUtil.converToWav3(file,source);
    	response.getWriter().print("voiceSuccess:type="+type);
    }
}
