<%@ page language="java" import="java.util.List" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.group.vo.LfList2groVo"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));


	LfSysuser curSysuser=(LfSysuser)session.getAttribute("loginSysuser");
	String sysName = (String)request.getAttribute("name");
	String sysPhone = (String)request.getAttribute("sysPhone");
	
	String shareType = request.getParameter("shareType"); 
	
	//登录id
	String lguserid = request.getParameter("lguserid");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	boolean plook = (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null);
	String menuCode = "1700-1500";
	PageInfo pageInfo;
	if(request.getAttribute("pageInfo") != null){
		pageInfo = (PageInfo)request.getAttribute("pageInfo");
	}else{
		pageInfo = new PageInfo();
	}
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	CommonVariables cv = new CommonVariables();
	Long groupId = (Long)request.getAttribute("groupId");
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	    <link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
	    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	    <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
			
		<%}%>
		<style type="text/css">
			#grp_groupMembers .group_table1{
				width: 99%;
			}
			#grp_groupMembers #sysName,#sysPhone{
				width: 100px;
			}
			#grp_groupMembers .group_div1{
				overflow-y:auto;height: 270px;width:100%;_width:99%;
			}
			#condition table tr td input, #condition table tr td select{
				width: 100px;
			}
		</style>
	</head>
	<body id="grp_groupMembers">
		<form action="" name="pageForm3" id="pageForm3">
		<div id="condition">
  		<table  style="width: 99%;">
 			<tr >
				<td>
					<emp:message key="group_ydbg_xzqz_text_name" defVal="名称" fileName="group"></emp:message>:<input type="text" name="sysName" id="sysName" value="<%=null==sysName?"":sysName %>" maxlength="11"/>&nbsp;&nbsp;
					<emp:message key="group_ydbg_xzqz_text_number" defVal="号码" fileName="group"></emp:message>:<input type="text" name="sysPhone" id="sysPhone" onkeyup="checkPhoneIput(this);" onpaste="return !clipboardData.getData('text').match(/\D/)" value="<%=null==sysPhone?"":sysPhone %>" maxlength="11"/>
				</td>
	 			<td class="tdSer">
							<center><a id="search" class="search1" onclick="submitForm3()"></a></center>
				</td>
 			</tr>
 		</table>
 		</div>
 		<div class="group_div1" style="overflow-y:auto;height: 278px;width:100%;_width:99%;">
   		<table id="content">
			<thead>
				<tr>
				 
					<th >
						<emp:message key="group_ydbg_xzqz_text_name" defVal="名称" fileName="group"></emp:message>
					</th>
				 
					<th >
						<emp:message key="group_ydbg_xzqz_text_number" defVal="号码" fileName="group"></emp:message>
					</th>
					<th >
						<emp:message key="group_ydbg_xzqz_text_type" defVal="类型" fileName="group"></emp:message>
					</th>
				</tr>
			</thead>
			<tbody>
			<%
				@ SuppressWarnings("unchecked")
				List<LfList2groVo> vos = (List<LfList2groVo>)request.getAttribute("vos");
				for(int g=0;g<vos.size();g++)
				{
					LfList2groVo vo=vos.get(g);
			%>
				<tr >
					<td ><%=vo.getName()%></td>
					<td ><%
					if(plook || (vo.getL2gtype()== 2 && vo.getSharetype() == 0))
					{
						out.print(vo.getMobile());
					}else
					{
						out.print(cv.replacePhoneNumber(vo.getMobile()));
					}
						 %></td>
					<td >
						<%
								if(vo.getL2gtype()==0){
									//out.print("员工");
									out.print(MessageUtils.extractMessage("group","group_ydbg_xzqz_text_employee",request));
								}else{
									if(vo.getSharetype() == 0){
										//out.print("自建");
										out.print(MessageUtils.extractMessage("group","group_ydbg_xzqz_text_selfbuilt",request));
									}else{
										//out.print("共享");
										out.print(MessageUtils.extractMessage("group","group_ydbg_xzqz_text_shared",request));
									}
								
								}
						 %>
					</td>
				</tr>
			 
			 <%} 
			  if(vos.size()==0){%>
					<tr><td colspan="3"  ><emp:message key="group_common_text_norecord" defVal="无记录" fileName="group"></emp:message></td></tr>
			 <%
			 }
			 %>

			</tbody>
		 	<tfoot>
		 		<tr>
				</tr>
			</tfoot>
		</table>
		</div>
		<div style="float: right">
		  <table id="content">
		  <tfoot>
		   <tr  >
				<%if(CstlyeSkin != null && CstlyeSkin.contains("frame4.0")){ %>
					<td colspan="3" >
						<ul class="new-page">
						<li id="p_info">
						    <emp:message key="group_ydkf_ygtxlgl_text_altogether" defVal="共" fileName="group"></emp:message>
							<%=pageInfo.getTotalRec() %>
							<emp:message key="group_ydkf_ygtxlgl_text_article" defVal="条" fileName="group"></emp:message>
						</li>
						<li id="p_back" onclick="javascript:submitForm3(<%=pageInfo.getPageIndex()-1%>)"></li>
						<li><%=pageInfo.getPageIndex()%></li>
						<li id="p_next" onclick="javascript:submitForm3(<%=pageInfo.getPageIndex()+1%>)"></li>
						<li id="p_info">
						<%=pageInfo.getPageSize()%>
						<emp:message key="group_ydkf_ygtxlgl_text_rowPerPage" defVal="条/页" fileName="group"></emp:message>
						</li>
						</ul>
					</td>
				<% }else{ %>
					<td colspan="3" >
						<a id="p_next" href="javascript:submitForm3(<%=pageInfo.getPageIndex()+1%>)"></a>
						<a id="p_back" href="javascript:submitForm3(<%=pageInfo.getPageIndex()-1%>)"></a>
						<span id="p_info">
						<emp:message key="group_ydkf_ygtxlgl_text_altogether" defVal="共" fileName="group"></emp:message>
							<%=pageInfo.getTotalRec() %>
						<emp:message key="group_ydkf_ygtxlgl_text_article" defVal="条" fileName="group"></emp:message>，
						<emp:message key="group_ydkf_ygtxlgl_text_first" defVal="第" fileName="group"></emp:message>
						<%=pageInfo.getPageIndex()%>/<%=pageInfo.getTotalPage()%>
						<emp:message key="group_ydkf_ygtxlgl_text_page" defVal="页" fileName="group"></emp:message>，
						<%=pageInfo.getPageSize()%>
						<emp:message key="group_ydkf_ygtxlgl_text_rowPerPage" defVal="条/页" fileName="group"></emp:message>
						</span>
					</td>
				<% } %>
				</tr>
				</tfoot>
		  </table>
		</div>
</form>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/group_<%=empLangName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script>
		$(document).ready(function() {
			//修复分页样式问题
			$("#p_goto>font").attr("style","padding-left:5px;width:100%;color:#000;line-height:24px;");
			if('-1' == '<%=groupId%>'){
				//alert('未获取到群组信息,该群组可能已删除！');
				alert(getJsLocaleMessage('group','group_page_groupMembers_groupisnull'));
				window.location.reload();
			}
			noyinhao("#sysName");

			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});

		 
			$("input[name='checkall2']").each(function(index){
				$(this).click(
					function(){
						$("input[name='sysUserName']").attr("checked",$(this).attr("checked")); 
					}
				);
			});
			
			var value=getJsLocaleMessage("common","common_query");
			$(".search1").html('<font style="text-align: center;margin-left: 21px;color:#000;line-height:24px;">'+value+'</font>');
			
		});
		
		function noyinhao(obj)
		{  
			var isIE = false;
			var isFF = false;
			var isSa = false;
			if ((navigator.userAgent.indexOf("MSIE") > 0)
					&& (parseInt(navigator.appVersion) >= 4))
				isIE = true;
			if (navigator.userAgent.indexOf("Firefox") > 0)
				isFF = true;
			if (navigator.userAgent.indexOf("Safari") > 0)
				isSa = true;
			$(obj).keypress(function(e) {
				var iKeyCode = window.event ? e.keyCode
						: e.which;
				var vv = ((iKeyCode == 39)
						|| (iKeyCode == 34));
				if (vv) {
					if (isIE) {
						event.returnValue = false;
					} else {
						e.preventDefault();
					}
				}
			});
		}
		function submitForm3(pageIndex)
		{
			var pageTotal = <%=pageInfo.getTotalPage()%>;
			var pageIndex = pageIndex==undefined?1:pageIndex;
			if(pageIndex<=0||pageIndex>=pageTotal+1){
				return;
			}
			var sysName = $("#sysName").val();
			var sysPhone = $("#sysPhone").val();
			var udgid = $("#udgIdtemp").val();
			var path = $("#path").val();
			var lgcorpcode=$("#lgcorpcode").val();
			var lguserid=$("#lguserid").val();
			var time=new Date();
			$("#groupDetail").load(path+'/grp_groupManage.htm?method=getTable',{lguserid:lguserid,lgcorpcode:lgcorpcode,sysName:sysName,sysPhone:sysPhone,udgid:udgid,pageIndex:pageIndex,time:time});
		}

		//手机号输入框输入过滤
		function checkPhoneIput(str)
		{
			if(str.value!=str.value.replace(/\D/g,''))
			{
				str.value=str.value.replace(/\D/g,'');
			}
		}
		</script>
	</body>
</html>