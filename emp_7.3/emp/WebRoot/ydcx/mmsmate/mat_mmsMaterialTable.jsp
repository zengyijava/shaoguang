<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.common.vo.LfMaterialVo" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mmsMaterial");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<LfMaterialVo> lfMaterialSortVoList = (List<LfMaterialVo>)request.getAttribute("lfMaterialSortVoList");
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
 		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	</head>
	<body>
			<div id="singledetail" style="padding:5px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"></div>
			</div>
 		<input type="hidden" name="methodType" id="methodType"/>
		<input type="hidden" name="mtalName_old" id="mtalName_old"/>
		<input type="hidden" name="sortId" id="sortId"/>
		<input type="hidden" name="mtalId" id="mtalId"/>
 		<table id="content">
			<thead>
				<tr>
					<th>
						<input type="checkbox" name="checkall" id="checkall">
					</th>
					<th>
						<emp:message key="ydcx_cxyy_cxsc_scid" defVal="素材ID" fileName="ydcx"></emp:message>
					</th>
					<th>
						<emp:message key="ydcx_cxyy_cxsc_scmc" defVal="素材名称" fileName="ydcx"></emp:message>
					</th>
					<th>
						<emp:message key="ydcx_cxyy_cxsc_sclx" defVal="素材类型" fileName="ydcx"></emp:message>
					</th>
					<th>
						<emp:message key="ydcx_cxyy_cxsc_scdx" defVal="素材大小" fileName="ydcx"></emp:message>
					</th>
					<th>
						<emp:message key="ydcx_cxyy_cxsc_tjz" defVal="添加者" fileName="ydcx"></emp:message>
					</th>
					<th>
						<emp:message key="ydcx_cxyy_cxsc_tjxgsj" defVal="添加/修改时间" fileName="ydcx"></emp:message>
					</th>
					<%-- 
					<th>
						备注
					</th>--%>
				<%if(btnMap.get(menuCode+"-3") != null || btnMap.get(menuCode+"-2") != null)  {  %>
					<th <%if(btnMap.get(menuCode+"-3") != null && btnMap.get(menuCode+"-2") != null)  {  out.print(" colspan=2");}%>>
						<emp:message key="ydcx_cxyy_common_text_14" defVal="操作" fileName="ydcx"></emp:message>
					</th>
				<%} %>
				</tr>
			</thead>
			<tbody>
			<%
			 
 			if(lfMaterialSortVoList != null && lfMaterialSortVoList.size()>0)
			{
				String mType = "";
				for(LfMaterialVo lfMaterialVo:lfMaterialSortVoList)
				{
					//LfMaterial lfMaterial=lfMaterialSortList.get(g);
			%>

			<tr>
				<td>
					<input type="checkbox" name="checklist" value="<%=lfMaterialVo.getMtalId() %>&<%=lfMaterialVo.getMtalAddress() %>"/>
 				</td>
				<td><%=lfMaterialVo.getMtalId() %></td>
				<td class="textalign"><%=lfMaterialVo.getMtalName()%></td>
			
				<td>
				<%=lfMaterialVo.getMtalType()%>
				</td>
				<td><%=lfMaterialVo.getMtalSize()%>KB</td>
				<td class="textalign">
				<%if(lfMaterialVo.getUserState() !=null && lfMaterialVo.getUserState() ==2)
				    {
				      //out.print(lfMaterialVo.getUserName()+"<font color='red'>(已注销)</font>");
				      out.print(lfMaterialVo.getUserName()+"<font color='red'>("+MessageUtils.extractMessage("ydcx","ydcx_cxyy_cxsc_yzx",request)+")</font>");
				    }
				    else
				    {
				       out.print(lfMaterialVo.getUserName());
				    }
				  %>
				</td>
				<td><%=df.format(lfMaterialVo.getMtalUptime())%></td>
 				<%if(btnMap.get(menuCode+"-3")!=null){ %>
 				<td><a href="javascript:doEdit('<%=lfMaterialVo.getMtalId() %>','<%=lfMaterialVo.getMtalName() %>','<%=null==lfMaterialVo.getMtalAddress()?" ":lfMaterialVo.getMtalAddress()%>','<%=lfMaterialVo.getSortId() %>','update')"><emp:message key="ydcx_cxyy_common_btn_15" defVal="修改" fileName="ydcx"></emp:message></a></td>
 				<%} %>
 				<%if(btnMap.get(menuCode+"-2")!=null){ %>
 				<td><a href="javascript:del(<%=lfMaterialVo.getMtalId() %>,'<%=lfMaterialVo.getMtalAddress() %>')"><emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message></a></td>
 				<%} %>
			</tr>
			<%
				}
			}else{
					 %>
				<tr>
					<td colspan="9">
						<emp:message key="ydcx_cxyy_common_text_1" defVal="无记录" fileName="ydcx"></emp:message>
					</td>
				</tr>
			 <%
			}
			%>
			
			</tbody>
			<tfoot>
				<tr>
					<td colspan="9">
						<div id="pageInfo"></div>
					</td>
				</tr>
			</tfoot>
		</table>
		 <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		 <script language="javascript" type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		 <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		 <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		 <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		 <script>
			$(document).ready(function(){
				$("input[name='checkall']").each(function(index){
					$(this).click(
						function(){
							$("input[name='checklist']").attr("checked",$(this).attr("checked")); 
						}
					);
				});
				$("#singledetail").dialog({
					autoOpen: false,
					height:200,
					width: 250,
					modal: true
				});
				
				$("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});
				
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				deleteleftline1();
			});
			
				function modify(t)
					{
		      			$("#msg").empty();
						$("#msg").append($(t).children("label").text().replace("<","&lt;").replace(">","&gt;"));
						//$("#singledetail").dialog("option","title", " 彩信素材备注 ");
						$("#singledetail").dialog("option","title", " "+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cxscbz"));
		      			$("#singledetail").dialog("open");
					}	
		</script>
 	</body>
</html>