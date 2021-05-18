<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.sysuser.LfPrivilege"%>
<%@page import="com.montnets.emp.entity.system.LfThiMenuControl"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.sysuser.LfDep"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@ page import="com.montnets.emp.util.StringUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
@SuppressWarnings("unchecked")
List<LfThiMenuControl> thirdMenuList = (List<LfThiMenuControl>)request.getAttribute("thirdMenuList");
@SuppressWarnings("unchecked")
Map<String,List<LfPrivilege>> menuMap = (Map<String,List<LfPrivilege>>)session.getAttribute("priMap");

LfDep lfdep=(LfDep)request.getAttribute("lfdep");
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String selPriMenus = request.getParameter("priMenus");
String openMenuCode = request.getParameter("openMenuCode");
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String corpcode=lfdep!=null?lfdep.getCorpCode():"100001";
//@ SuppressWarnings("unchecked")
//Map<String,String> infoMap = (Map<String,String>) session.getAttribute("infoMap");
//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
//boolean isCharging = (infoMap.get("feeFlag")!=null&&"true".equals( infoMap.get("feeFlag")))?true:false;//是否启用计费机制
boolean isCharging = SystemGlobals
			.isDepBilling((corpcode == null || corpcode == "") ? "100001"
					: corpcode);//是否启用计费机制
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
//是否显示运营商余额
boolean isShowYe = btnMap.get(ViewParams.YECODE+"-0") == null ? false : true;
//是否显示公告
boolean isShowGg = btnMap.get(ViewParams.GGCODE+"-0") == null ? false : true;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><%@ include file="/common/common.jsp"%>
    <title></title>
   	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=iPath %>/css/left.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/left.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<%if(StaticValue.ZH_HK.equals(langName)){%>
<style>
.mune_show span {
	text-indent: 0px;
	line-height: 13px;
	width: 165px;
    padding-left: 26px;
    font-size: 11px;
	display: block;
	padding-top: 5px;
	height: 31px;
	word-break: normal;
	background: url(<%=skin%>/images/icon_right_sel.gif) 7px 5px no-repeat;
}

.mune_show.open span {
	background: url(<%=skin%>/images/icon_down_sel.gif) 7px 5px no-repeat;
}

#sider .mune_hidden li {
	white-space: normal;
}

#sider .mune_hidden li a {
	line-height: 2px;
    height: 39px;
	word-break: normal;
	width: 100%;
	text-align: left;
	text-indent: 15px;
	/* background-color: #dce8fb; */
}

.act_ico {
	left: 5px;
	top: 12px;
}

#theme span {
	padding-left: 26px;
	padding-top: 10px;
    height: 35px;
}
#balance span {
	padding-left: 26px;
	padding-top: 10px;
    height: 35px;
}
#ggLi span {
	padding-left: 26px;
	padding-top: 10px;
    height: 35px;
}
#upsc span {
	padding-left: 26px;
	padding-top: 10px;
    height: 35px;
}
</style>
	  <%}%>
	  <%if(StaticValue.ZH_HK.equals(empLangName)){%>
	  <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	  <%}%>
	  <%--[if IE 6]>
        <script type="text/javascript" src="<%=path%>/common/js//DD_belatedPNG.js"></script>
        <script language="javascript" type="text/javascript">
        DD_belatedPNG.fix(".act_ico img");
        </script>
        <![endif]--%>
	<script type="text/javascript">
		var path = '<%=path%>';
		var getContextPath = '<%=request.getContextPath()%>';
		var selPriMenus = <%=selPriMenus%>;
	</script>
	</head>
  <body onload="reOpen()">
  <input type="hidden" id="langName" value="<%=langName%>" />
  <div class="logininfo" style="display:none"></div>
  <table id="modIndex" class="div_bd block">
  <tbody><tr>
    <td valign="top">
    <input type="hidden" id="skin" value="<%=skin%>" />
    <input type="hidden" id="path" value="<%=path%>" />
    <input type="hidden" id="selPriMenus" value="<%=selPriMenus%>" />
    <input type="hidden" id="openMenuCode" value="<%=openMenuCode%>" />
	<input type="hidden" id="pathUrl" value="<%=path%>" />
    <div id="sider" class="sider_round_content div_bd">
    <div id="userDiv">
    	<div style="display:block;width:100%;height:128px;" id="userInfo">

    		<div style="padding-top: 27px;padding-left: 20px;font-size: 12px;">
	    		<div style="float:left;width: 62px;height:62px;">
					<span id="photo"></span>
	    		</div>
    			<div style="padding-left: 0px;float: left;color:#493200;">
    				<div style="width: 100px;padding-top:9px;word-wrap:break-word;">
    					<%
    						LfSysuser sysuser=((LfSysuser) session.getAttribute("loginSysuser"));
    						String userName="";
    						String showName="";
    						if(sysuser!=null)
    						{
    							userName=sysuser.getName();
    							if(userName!=null)
    							{
	    							if(userName.length()>5)
	    							{
	    								showName=userName.substring(0,3)+"...";
	    							}
	    							else
	    							{
	    								showName=userName;
	    							}
    							}
    						}
    					%>
    					<xmp onclick="javascript:doUpdatePass()" style="cursor:pointer;display:inline;font-weight:bold;color:#493200" id="loginName" title="<%=userName %>"><%=showName%></xmp>&nbsp;&nbsp;<emp:message key="common_frame2_left_1" defVal="您好！" fileName="common"></emp:message><span style="display: none;" id="showName"><%=userName %></span>
    				</div>
    				<div style="padding-top: 10px;">
						<emp:message key="common_frame2_left_2" defVal="欢迎登录EMP" fileName="common"></emp:message>
    				</div>
    			</div>
    		</div>
    	</div>
    </div>
    <div id="blue_user_info" class="hidden">
     <div class="left_title_bg">
      <div class="info_inner">
        <div class="show_user_name"><%=userName %></div>
      </div>
      </div>
    </div>
    	<ul>
	   		<li class="mune_show" id="theme" >
	   			<a href="javascript:;" class="act_ico theme hidden">
	   			<img class="flag" src="<%=skin %>/images/left_icon.png" />
	   			</a><span onclick="doOpen('/emp_tz.htm?method=toChangeSkin','','主题换肤')"><emp:message key="common_frame2_left_3" defVal="主题换肤" fileName="common"></emp:message></span>
	   		</li>

	   		<%if(isCharging || isShowYe){ %>
	   		<li class="mune_show" id="balance">
	   			<a href="javascript:;" class="act_ico balance hidden">
	   			<img class="flag" src="<%=skin %>/images/left_icon.png" />
	   			</a><span onclick="doOpen('<%=iPath %>/checkFeeBefor.jsp?method=findSpfee','','余额查询')"><emp:message key="common_balanceQuery" defVal="余额查询" fileName="common"></emp:message></span>
	   		</li>
	   		<%} %>
	   		<%if(StaticValue.getInniMenuMap().containsKey("/not_notice.htm") && isShowGg){ %>
		   		<li class="mune_show" id="ggLi">
		   			<a href="javascript:;" class="act_ico ggLi hidden">
		   			<img class="flag" src="<%=skin %>/images/left_icon.png" />
		   			</a><span onclick="doOpen('/not_notice.htm?type=1','','公告')"><emp:message key="common_viewNotice" defVal="公告查看" fileName="common"></emp:message></span>
		   		</li>
	   		<%} %>
   			<li class="mune_show" id="upsc" >
	   			<a href="javascript:;" class="act_ico upsc hidden">
	   			<img class="flag" src="<%=skin %>/images/left_icon.png" />
	   			</a><span onclick="upLoad()"><emp:message key="common_helpManual" defVal="帮助手册" fileName="common"></emp:message></span>
	   		</li>
        </ul>
    </div>
      </td>
  </tr>

</tbody></table>
  <div id="blue_user_info" class="hidden">
     <div class="left_title_bg">
      <div class="info_inner">
        <div class="show_user_name"><%=userName %></div>
      </div>
      </div>
    </div>
  <%
	if(thirdMenuList != null && thirdMenuList.size()>0)
	{
		int size = thirdMenuList.size();
		//String clorIndex = "";
		String priMenus=String.valueOf(thirdMenuList.get(0).getPriMenu());
		String title = thirdMenuList.get(0).getTitle();

		if(StaticValue.ZH_TW.equals(langName)){
			title = thirdMenuList.get(0).getZhTwTitle();
		}
		if(StaticValue.ZH_HK.equals(langName)){
			title = thirdMenuList.get(0).getZhHkTitle();
		}

		Integer menuNum = thirdMenuList.get(0).getMenuNum();

		for(int i=1;i<size;i++)
		{
			LfThiMenuControl menu = thirdMenuList.get(i);
			if(menuNum-menu.getMenuNum()==0)
			{
				priMenus+=","+String.valueOf(menu.getPriMenu());
			}else
			{
	%>

   <table id="mod<%=menuNum %>" class="div_bd" menuNum="<%=menuNum %>">
  <tr>
    <td valign="top" >

    <div id="sider" class="sider_round_content div_bd">
    	<ul>
	   		<%
	   		if(menuMap == null )
   			{
   				continue;
   			}
	   		String[] menus = priMenus.split(",");
			for(int m=0;m<menus.length;m++)
			{
	   			List<LfPrivilege> prList = menuMap.get(menus[m]);
	   			if(prList == null )
	   			{
	   				continue;
	   			}
	   			String modName="";
	   			String showModName = "";
	   			String showMenuName = "";

	 			int prsize=prList.size();
	   			for(int p=0;p<prsize;p++)
	 			{
	 				LfPrivilege pri=prList.get(p);

					showMenuName = pri.getMenuName();
                    if(StaticValue.ZH_TW.equals(empLangName)){
                        showMenuName = StringUtils.isEmpty(pri.getZhTwMenuName())?pri.getMenuName():pri.getZhTwMenuName();
                    }
                    if(StaticValue.ZH_HK.equals(empLangName)){
                        showMenuName = StringUtils.isEmpty(pri.getZhHkMenuName())?pri.getMenuName():pri.getZhHkMenuName();
                    }

	 				if(!modName.equals(pri.getModName()))
	 				{
	 					modName=pri.getModName();
	 					showModName=pri.getModName();
	 					if(StaticValue.ZH_TW.equals(langName)){
	 						showModName = pri.getZhTwModName();
	 		   			}
	 		   			if(StaticValue.ZH_HK.equals(langName)){
	 						showModName = pri.getZhHkModName();
	 		   			}
	    	%>
    		<li class="mune_show"><span><%=showModName %></span>
    			<ul class="mune_hidden div_bd">
	    	<% 	} %>
	           		<li><a id="ak<%=pri.getMenuCode() %>" onclick="javascript:doOpen('<%=pri.getMenuSite() %>','<%=pri.getMenuCode() %>',
	           		'<%=showMenuName %>')" onmouseover="mouseOver('<%=pri.getMenuCode() %>')" onmouseout="mouseOut('<%=pri.getMenuCode() %>')" >
                        <%
                            if (prList.get(p).getModName().contains("我的快捷场景")){
                                String tmpMenuName = showMenuName;
                                if (tmpMenuName.length() > 6) {
                                    tmpMenuName = showMenuName.substring(0,6) + "...";
                                }
                        %>
                        <%=tmpMenuName %>
                        <%
                        } else {
                        %>
                        <%=showMenuName %>
                        <%
                            }
                        %>
						<%if(prList.get(p).getModName().contains("我的快捷场景")){%>
	           			<input type="button" id="closeLi<%=pri.getMenuCode()%>" class="delShortCut" value="x" style="border: 0;width: 20px;height:30px;position: absolute;left: 170px;cursor: pointer;background-color: #f1f1f9;display:none;" onclick="deleteLi('<%=pri.getMenuCode()%>','<%=showMenuName %>');">
	           		<%}%></a></li>
            <% if(p==prsize-1 || !modName.equals(prList.get(p+1).getModName())) { %>
    			</ul>
	        </li>
	        <% } }} %>
        </ul>
    </div>
      </td>
  </tr>
</table>
<%
				title = menu.getTitle();
				if(StaticValue.ZH_TW.equals(langName)){
					title = thirdMenuList.get(0).getZhTwTitle();
				}
				if(StaticValue.ZH_HK.equals(langName)){
					title = thirdMenuList.get(0).getZhHkTitle();
				}
				menuNum=menu.getMenuNum();
				priMenus=String.valueOf(menu.getPriMenu());
			}
		}
	}
%>

	<div id="tmdiv"></div>
	<script src="<%=path %>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
   	<script src="<%=path %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
  	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
  <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  <script type="text/javascript" src="<%=iPath %>/js/left.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  	<script type="text/javascript">
  	function mouseOver(obj){
		$("#closeLi"+obj).css({"display":""});
		$(this).removeAttr("onclick");
	}
	function mouseOut(obj){
		$("#closeLi"+obj).css({"display":"none"});
	}
	function deleteLi(tempId,name){
		var skin = $("#skin").val();
		if(confirm(getJsLocaleMessage("common","common_confirm_del_myshortcut")+name+"?")){
            //event.stopPropagation();
			//兼容ie10及以下浏览器
            if(event && event.stopPropagation)
            {
                event.stopPropagation();  //  w3c 标准
            } else {
                event.cancelBubble = true;  // ie  9 10 
            };
			$.ajax({
				type:"POST",
				url :"rms_templateMana.htm?method=deleteShortTemp&tempId="+tempId,
               		success:function (result) {
	                	if (result == 'true') {
                            alert(getJsLocaleMessage("common","common_del_success"));
                       		var _secondMenu =$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tempId).parent().parent();
                        	var len = _secondMenu.children('li').length;
	                        if(len<2){
                        		_secondMenu.parent().remove();
	                        }else{
	                        	$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tempId).parent().remove();
	                        }
                            //document.getElementById('cont').contentWindow.location.reload(true);
                            window.parent.document.getElementById('cont').contentWindow.location.reload(true);
                        } else {
                            alert(getJsLocaleMessage("common","common_del_failure"));
	                   }
               		}
			});
		}
		return false;
	}
  	</script>
  </body>
</html>

