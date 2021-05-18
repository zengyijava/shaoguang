<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>)session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>)session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String)session.getAttribute("stlyeSkin");
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    @SuppressWarnings("unchecked")
    List<DynaBean> userbeans = (List<DynaBean>)request.getAttribute("userbeans");
	List<LfWeiAccount> otWeiAccList=(List<LfWeiAccount>)request.getAttribute("otWeiAccList");
    PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");
    if(null == pageInfo)
    {
        pageInfo = new PageInfo();
    }
    
    String aid= (String)request.getParameter("aid");
    if(null == aid || "".equals(aid))
    {
    	aid="-1";
    }

    String typeid= (String)request.getParameter("typeid");
    if(null == typeid || "".equals(typeid))
    {
        typeid="1";
    }
    
    String weixBasePath = request.getScheme() + "://" + request.getServerName() + path + "/";
    
    //使用集群，文件服务器的地址
    String filePath = GlobalMethods.getWeixFilePath();
    //MP企业微信服务协议
    String approve = (String)session.getAttribute("approve");
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
	<%-- header结束 --%>
	<%-- 内容开始 --%>
	<div id="container" class="container">
	<%-- 当前位置 --%>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
	<div id="rContent" class="rContent">
		<form name="userStatisticsForm" action="" method="post" id="userStatisticsForm">
			<div style="display:none" id="hiddenValueDiv"></div>
			<input type="hidden" name="tp" value="follow"/>
			<div id="condition">
			<table> 
				<tbody> 
					 <tr> 
					<td><emp:message key="wxgl_yhgl_title_1" defVal="公众帐号:" fileName="wxgl"/></td>
					<td>
					<select id="aid" name="aid" class="input_bd" onchange="findUserStatistics('statisticdiv')">
						<%
					    if(null != otWeiAccList && otWeiAccList.size()>0){
                              for(LfWeiAccount acct : otWeiAccList){
                              String aId  = String.valueOf(acct.getAId());
					%>
					<option  value="<%=acct.getAId() %>" <%=(aid.equals(aId))?"selected":"" %>> <%=acct.getName() %></option>
					<%
						}
					}
					%>
					</select>
					</td>

					<td><emp:message key="wxgl_yhgl_title_18" defVal="用户属性：" fileName="wxgl"/></td>
					<td><select id="typeid" name="typeid" class="input_bd" onchange="findUserStatistics('statisticdiv')">
						<option value="1"><emp:message key="wxgl_yhgl_title_19" defVal="性别分布" fileName="wxgl"/></option>	
						<option value="2"><emp:message key="wxgl_yhgl_title_20" defVal="省份分布" fileName="wxgl"/></option>				
<%--						<option value="3">城市分布</option>									--%>
						</select>
					</td>
					
				</tr> 
				</tbody> 
				</table>	
		</div>
		
	
		
               <div id="statisticdiv" align="center" style="padding: 15px 0; " ><emp:message key="wxgl_yhgl_title_21" defVal="无记录" fileName="wxgl"/></div>
        </form>
    </div>
        </div>
	<div id="pageStuff" style="display: none;">
		<input type="hidden" id="pathUrl" value="<%=path%>" />
		<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
		<input type="hidden" id="lguserid" value="<%=lguserid%>" />
		<input type="hidden" id="lgcorpcode" value="<%=lgcorpcode%>" />
	</div>
  	<div class="clear"></div>
   	<div id="updateUserTmpDiv" title="<emp:message key='wxgl_yhgl_title_17' defVal='详细信息' fileName='wxgl'/>" style="padding:5px;display:none">
		<iframe id="updateUserTmpFrame" name="updateUserTmpFrame" style="width:650px;height:540px;border:0;" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	<script src="<%=commonPath%>/wxcommon/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script src="<%=commonPath%>/wxcommon/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/FusionCharts.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/tjgl_userStatistics.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	
	<script type="text/javascript">
<%--	$('#aid').isSearchSelect({'width':'145','isInput':false,'zindex':0},function(){--%>
<%--		findUserStatistics('statisticdiv');--%>
<%--	});--%>
<%--	$('#typeid').isSearchSelect({'width':'145','isInput':false,'zindex':0},function(){--%>
<%--		findUserStatistics('statisticdiv');--%>
<%--	});--%>
    var pathUrl="<%=path%>",
        iPathUrl="<%=iPath%>",
        currentTotalPage="<%=pageInfo.getTotalPage()%>",
        currentPageIndex="<%=pageInfo.getPageIndex()%>",
        currentPageSize="<%=pageInfo.getPageSize()%>",
        currentTotalRec="<%=pageInfo.getTotalRec()%>",
        lguserid=<%=lguserid%>,
        lgcorpcode="<%=lgcorpcode%>";
    $(document).ready(function() {          
        getLoginInfo("#hiddenValueDiv");
        findUserStatistics('statisticdiv');
        $('#search').click(function(){            
            findUserStatistics('statisticdiv');
        });

    });
	</script>
	</body>
</html>