<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page
        import="com.montnets.emp.entity.monitor.LfMonDbstate" %>
<%@ page
        import="java.util.HashMap" %>
<%@ page
        import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,
			iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo  = (PageInfo) request.getAttribute("pageInfo");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@SuppressWarnings("unchecked")
	List<LfMonDbstate> pageList = (List<LfMonDbstate>)request.getAttribute("pageList");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
        <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
        <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
        <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
        <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
			<table id="content" class="content_table">
				<thead>
					<tr>
						<th><emp:message key="ptjk_jkxq_sjk_cxm" defVal="程序名" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_sjk_ssjd" defVal="所属节点" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_common_cxlx" defVal="程序类型" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_common_gjjb" defVal="告警级别" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_sjk_9" defVal="数据库连接状态" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_sjk_10" defVal="增加操作状态" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_sjk_11" defVal="修改操作状态" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_sjk_12" defVal="查询操作状态" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_sjk_13" defVal="删除操作状态" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_common_gxsj" defVal="更新时间" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_common_jkzt" defVal="监控状态" fileName="ptjk"/></th>
					</tr>
				</thead>
				<tbody>
					<%
					if (pageList != null && pageList.size() > 0)
					{
                        Map<Integer,String> evtMap = new HashMap<Integer, String>();
                        Map<Integer,String> evtStyleMap = new HashMap<Integer, String>();
                        evtMap.put(0,MessageUtils.extractMessage("ptjk","ptjk_common_zc",request));
                        evtMap.put(1,MessageUtils.extractMessage("ptjk","ptjk_common_gj",request));
                        evtMap.put(2,MessageUtils.extractMessage("ptjk","ptjk_common_yz",request));
                        evtStyleMap.put(0,"natural");
                        evtStyleMap.put(1,"warn");
                        evtStyleMap.put(2,"bad");
                        Map<Integer,String> proceTypeMap = new HashMap<Integer, String>();
                        proceTypeMap.put(5000,MessageUtils.extractMessage("ptjk","ptjk_common_webcx",request));
                        proceTypeMap.put(5200,MessageUtils.extractMessage("ptjk","ptjk_common_empwg",request)+"(EMP_GW)");
                        proceTypeMap.put(5300,MessageUtils.extractMessage("ptjk","ptjk_common_yysjk",request)+"(SPGATE)");
                        proceTypeMap.put(5800,MessageUtils.extractMessage("ptjk","ptjk_common_wjfwq",request));
                        Map<Integer,String> oprMap = new HashMap<Integer, String>();
                        oprMap.put(0,MessageUtils.extractMessage("ptjk","ptjk_common_zc",request));
                        oprMap.put(1,MessageUtils.extractMessage("ptjk","ptjk_common_sb",request));
                        Map<Integer,String> dbConMap = new HashMap<Integer, String>();
                        dbConMap.put(0,MessageUtils.extractMessage("ptjk","ptjk_common_zc",request));
                        dbConMap.put(1,MessageUtils.extractMessage("ptjk","ptjk_common_dk",request));
						for (LfMonDbstate dbstate : pageList) {
                            String updateTime = dbstate.getUpdatetime().toString();
                            if(updateTime.length() > 19)
                            {
                                updateTime = updateTime.substring(0,19);
                            }
					%>
				        <tr>
                            <td><%=dbstate.getProcename()%></td>
                            <td><%=dbstate.getProcenode()%></td>
                            <td><%=StringUtils.defaultIfEmpty(proceTypeMap.get(dbstate.getProcetype()),"-")%></td>
                            <td class="<%=evtStyleMap.get(dbstate.getEvttype())%>"><%=evtMap.get(dbstate.getEvttype())%></td>
                            <td><%=dbConMap.get(dbstate.getDbconnectstate())%></td>
                            <td><%=oprMap.get(dbstate.getAddopr())%></td>
                            <td><%=oprMap.get(dbstate.getModiopr())%></td>
                            <td><%=oprMap.get(dbstate.getDispopr())%></td>
                            <td><%=oprMap.get(dbstate.getDelopr())%></td>
                            <td><%=updateTime%></td>
                            <td>
                                <center>
                                    <div class="setControl"></div>
                                    <select class="monstatus" procetype="<%=dbstate.getProcetype()%>" procenode="<%=dbstate.getProcenode()%>">
                                        <option value="0" <%if(dbstate.getMonstatus()-0 == 0){out.print("selected");}%>><emp:message key="ptjk_common_wjk" defVal="未监控" fileName="ptjk"/></option>
                                        <option value="1" <%if(dbstate.getMonstatus()-1 == 0){out.print("selected");}%>><emp:message key="ptjk_common_jk" defVal="监控" fileName="ptjk"/></option>
                                    </select>
                                </center>
                            </td>
                        </tr>
					<%
						}
					}else{
					%>
					<tr><td colspan="11" align="center"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr><%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="11">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
			</table>
			<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
	<%--<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.JSP_IMP_VERSION%>"></script>--%>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		<script type="text/javascript">
		$(document).ready(function() {
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
			});
		    $("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});
			var findresult="<%=(String)request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
		       alert(getJsLocaleMessage("ptjk","ptjk_jkxq_sjk_6"));
		       return;			       
		    }
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);

			//解除绑定事件，使表格选中不变色
			$('#content table tbody tr,#content tbody tr').unbind("click");

            //修改状态
            $('select.monstatus').change(function(){
                var $select = $(this);
                var procetype = $select.attr('procetype');
                var procenode = $select.attr('procenode');
                var monstatus = $select.val();
                $.post("mon_dataBaseMon.htm?method=changeSate",{procetype:procetype,procenode:procenode,monstatus:monstatus,misAsync:"yes"},function(data){
                    if(data == "outOfLogin")
                    {
                        location.href="common/logoutEmp.html";
                        return;
                    }
                    if (data == "true") {
                        alert(getJsLocaleMessage("ptjk","ptjk_jkxq_sjk_7"));
                        submitForm();
                    }else{
                        alert(getJsLocaleMessage("ptjk","ptjk_jkxq_sjk_8"));
                    }
                });
            })

			});



        $("#content select").isSearchSelectNew({'width':<%= "zh_HK".equals(empLangName)?100:60 %>,'isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(data){
            $(data.box.self).change();
        },function(data){
            var self=$(data.box.self);
            self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'<%= "zh_HK".equals(empLangName)?100:60 %>px'});
        });
		</script>
	</body>
</html>
