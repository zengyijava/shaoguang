<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.system.LfTimer" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
	@ SuppressWarnings("unchecked")
	List<LfTimer> timerList=(List<LfTimer>)request.getAttribute("timerList");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    @ SuppressWarnings("unchecked")
    Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
    String menuCode = titleMap.get("timerControl");
    String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
<head>
	<%@include file="/common/common.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=titleMap.get(menuCode) %></title>

		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
</head>
<body id="eng_timerControl" class="eng_timerControl">
        <div id="container">
		<%-- 当前位置 --%>
		<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
		--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<%
			if(btnMap.get(menuCode+"-0")!=null)
			{
		%>
		
    	<div class="rContent" >
    	<form id="pageForm" name="pageForm" action="eng_timerControl.htm" method="post">
    	<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
    	<table id="content">
    	<thead>
            <tr>
                <th >
                   <emp:message key="znyq_ywgl_xxywjk_ywmc" defVal="业务名称" fileName="znyq"></emp:message>
                </th>
                <th >
                    <emp:message key="znyq_ywgl_xxywjk_xczxsj" defVal="下次执行时间" fileName="znyq"></emp:message>
                </th>
                <th >
                    <emp:message key="znyq_ywgl_xxywjk_sczxsj" defVal="上次执行时间" fileName="znyq"></emp:message>
                </th>
                <th >
                    <emp:message key="znyq_ywgl_common_zt" defVal="状态" fileName="znyq"></emp:message>
                </th>
                <%--<th >
                    开始时间
                </th>
                <th >
                    结束时间
                </th>--%>
                <%
						if(btnMap.get(menuCode+"-2")!=null)
						{
					%>
                <th colspan="1">
                	<emp:message key="znyq_ywgl_common_text_14" defVal="操作" fileName="znyq"></emp:message>
                </th>
                <%
						}
                %>
            </tr>
          </thead>
          <tbody>
          <%
            if(timerList!=null&&timerList.size()>0){
          	for(int i=0;i<timerList.size();i++)
          	{
          		LfTimer timer=timerList.get(i);
          %>
	                <tr>
	                    <td class="textalign" >
	                    <xmp class="commonXmp">
	                    	<%=timer.getTimerTaskName() %>
	                    </xmp>
	                    </td>
	                    <td >
	                    	
	                    	<%
	                    		if(timer.getNextTime()==null){
	                    			//out.print("执行完毕");
	                    			out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjk_zxwb",request));
	                    		}else if(timer.getRunState()-0==0){
	                    			out.print("-");
	                    		}else if(timer.getRunState() == 1 && timer.getNextTime().getTime() < System.currentTimeMillis() ){
	                    			//out.print("任务重运行中");
	                    			out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjk_rwcyxz",request));
	                    		}
	                    		else{
	                    			out.print(sdf.format(timer.getNextTime().getTime()));
	                    		}
	                    	%>
	                    </td>
	                    <td >
	                    	<%--<%=timer.getPreTime()==null?"未执行":sdf.format(timer.getPreTime().getTime()) %>--%>
	                    	<%=timer.getPreTime()==null?MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjk_wzx",request):sdf.format(timer.getPreTime().getTime()) %>
	                    </td>
	                    <td class="ztalign" >
	                    	<%
	                    		if(timer.getRunState()-1==0)
	                    		{
	                    			//out.print("运行");
	                    			out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjk_yx",request));
	                    		}else if(timer.getRunState()-0==0)
	                    		{
	                    			//out.print("停止");
	                    			out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjk_tz",request));
	                    		}else if(timer.getRunState()-2==0)
	                    		{
	                    			//out.print("暂停");
	                    			out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjk_zt",request));
	                    		}
	                    	%>
	                    </td>
	                   <%-- <td >
	                    	<s:property value="start_time"/>
	                    </td>
	                    <td >
	                    	<s:property value="end_time"/>
	                    <br></td> 
	                    <td >
	                    <%
	                    		if(timer.getRunState()-1==0)
	                    		{
	                    			%>
	                    	<a href="javascript:doCmd('stop',<%=timer.getTimerTaskId() %>)" >暂停</a>
	                    	<%} else if(timer.getRunState()-2==0 )
	                    		{%>
	                    	<a href="javascript:doCmd('start',<%=timer.getTimerTaskId() %>)" >恢复</a>
	                    	<%} %>
	                    </td> --%>
	                    <td>
	                     <%
								if(btnMap.get(menuCode+"-2")!=null)
								{
							%>
	                        <input type="hidden" name="taskName" id="taskName" value="<%=timer.getTimerTaskName() %>"/>
	                        <%--<a href="javascript:if(confirm('您确定要删除?')==true) doCmd('delete','<%=timer.getTaskExpression() %>');"><emp:message key="znyq_ywgl_common_text_8" defVal="删除" fileName="znyq"></emp:message></a>--%>
							<a href="javascript:if(confirm('<emp:message key="znyq_ywgl_xxywjk_nqdysc_wh" defVal="您确定要删除?" fileName="znyq"></emp:message>')==true) doCmd('delete','<%=timer.getTaskExpression() %>');"><emp:message key="znyq_ywgl_common_text_8" defVal="删除" fileName="znyq"></emp:message></a>
						<%
								}
						%>	                    	
	                    	
	                    </td>
	                </tr>
					<%
	                    		}}else{
	                %>
	                <tr><td colspan="6">
						<emp:message key="znyq_ywgl_common_text_1" defVal="无记录" fileName="znyq"></emp:message>
					</td></tr>
	                <%   			
	                    		}
					%>
					</tbody>
					<tfoot>
					<tr>
					<td colspan="6">
						<div id="pageInfo"></div>
					</td>
				</tr>
				</tfoot>
        </table>
			</form>
			</div>
		<%
			}
			%>  
		</div> <%--end round_content--%>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
	function doCmd(state,taskId){
		var taskName = $("#taskName").val();
		var lguserid=$("#lguserid").val();
		var lgcorpcode=$("#lgcorpcode").val();
		$.post('eng_timerControl.htm',
			{
				method:"delete",
				opType:state,
				taskId:taskId,
				lguserid : lguserid,
				lgcorpcode:lgcorpcode,
				taskName:taskName
			},
			function(result){
				if (result == "true") {
					//alert("删除成功！");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xxywjk_sccg"));
					$("#pageForm").submit();
				}else if (result == "false"){
					//alert("删除失败！");	
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xxywjk_scsb"));	
				}
            });
	}
	$(document).ready(function(){
		getLoginInfo("#hiddenValueDiv");
		$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
			}, function() {
			$(this).removeClass("hoverColor");
		});
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		setInterval("refeshPage()", 10000);
	});
        
	function refeshPage()
	{
		document.pageForm.submit();
	}

</script>
		
</body>
</html>