<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.sysuser.LfPrivilege"%>
<%@page import="com.montnets.emp.entity.system.LfThiMenuControl"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String selPriMenus = request.getParameter("priMenus");
String openMenuCode = request.getParameter("openMenuCode");
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><%@ include file="/common/common.jsp"%>
    <title></title>
   	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=iPath %>/css/left.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/left.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
  	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
  	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
  	<%}%>
	<%if(StaticValue.ZH_HK.equals(langName)){%>
<style>
	.mune_show.open {
		background: url(<%=skin%>/images/menu_2_down.gif) 2px 9px no-repeat;
	}
	.mune_show {
		text-indent: 20px;
		background: url(<%=skin%>/images/menu_2_left.gif) 2px 9px no-repeat;
	}
	#sider .mune_hidden li a{
		text-align: left;
		text-indent: 20px;
	}
/*.mune_show {
	line-height: 22px;
	text-indent: 0px;
    padding-left: 16px;
	background: url(<%=skin%>/images/menu_2_left.gif) 1px 12px no-repeat;
}

.mune_show.open {
	background: url(<%=skin%>/images/menu_2_down.gif) 1px 14px no-repeat;
}

.mune_show span {
	font-size: 14px;
    width: 165px;
	padding-top: 6px;
	display: block;
	word-break: break-all;
}

#sider .mune_hidden li {
	white-space: normal;
	padding-left: 6px;
}

#sider .mune_hidden li a {
    padding-top: 1px;
    padding-bottom: 1px;
	text-indent: 1px;
	line-height: 16px;
    width: 82%;
	!* background-color: #dce8fb; *!
}*/
</style><%}%>
</head>
  <body>
  <div class="logininfo" style="display:none"></div>
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
   <table id="mod<%=menuNum %>" class="div_bd">
  <tr>
    <td valign="top" >
     <span class="menu_top"><a onclick="javascript:void(0)"><%=title %></a></span>
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
	 					showModName = pri.getModName();
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
	           			<input type="button" id="closeLi<%=pri.getMenuCode()%>" value="x" style="border: 0;width: 20px;height:30px;position: absolute;left: 150px;cursor: pointer;background-color: #f1f1f9;display:none;" onclick="deleteLi('<%=pri.getMenuCode()%>','<%=showMenuName %>');">
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
					title = menu.getZhTwTitle();
				}
				if(StaticValue.ZH_HK.equals(langName)){
					title = menu.getZhHkTitle();
				}
				menuNum=menu.getMenuNum();
				priMenus=String.valueOf(menu.getPriMenu());
			}
		}
	}
%>
  <input type="hidden" id="skin" value="<%=skin%>" />
  <input type="hidden" id="pathUrl" value="<%=path%>" />
	<div id="tmdiv"></div>
  <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
  <script src="<%=path %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
  <script>
	var empRoot="<%=session.getAttribute("empRoot")%>";
	var path="<%=path%>";
	var selPriMenus=<%=selPriMenus%>;
	var openMenuCode="<%=openMenuCode%>";
	function mouseOver(obj){
		$("#closeLi"+obj).css({"display":""});
		$(this).removeAttr("onclick");
	}
	function mouseOut(obj){
		$("#closeLi"+obj).css({"display":"none"});
	}
	function deleteLi(tempId,name){
		if(confirm(getJsLocaleMessage("common","common_confirm_del_myshortcut")+name+"?")){
			$.ajax({
				type:"POST",
				url :"rms_templateMana.htm?method=deleteShortTemp&tempId="+tempId,
                success:function (result) {
	                if (result == 'true') {
                        alert(getJsLocaleMessage("common","common_del_success"));
                        window.location.reload();
                        $(window.parent.document).find("#m"+tempId).remove();
                        //window.parent.document.getElementById('leftIframe').contentWindow.location.reload(true);
                        window.parent.document.getElementById('cont5100-1300').contentWindow.location.reload(true);
                    } else {
                        alert(getJsLocaleMessage("common","common_del_failure"));
                    }
                }
			});
		}
	}
	</script>
  <%--<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>--%>
  <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

  <script type="text/javascript" src="<%=iPath %>/js/left.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	
  </body>
</html>

