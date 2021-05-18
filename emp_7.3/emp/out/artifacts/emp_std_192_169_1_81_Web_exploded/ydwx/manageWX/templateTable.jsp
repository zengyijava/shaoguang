<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.util.LinkedHashMap"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
		String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<DynaBean> beans = (List<DynaBean>)request.getAttribute("beans");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String,String> conMap = (LinkedHashMap<String,String>)request.getAttribute("conMap");
	String skin = session.getAttribute("stlyeSkin")==null?"default": (String)session.getAttribute("stlyeSkin");
%>
<html>
	<head><%@include file="/common/common.jsp" %>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/templateManger.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body class="templateTable">

					<table id="content">
					
					<thead>
					<tr>

						<th class="ydwx_no">
							<emp:message key="ydwx_wxgl_wxmb_bianhao" defVal="模板编号" fileName="ydwx"></emp:message>
						</th>
						<th  class="ydwx_name_th" style="max-width: 25%">
							<emp:message key="ydwx_wxgl_wxmb_mingchen" defVal="模板名称" fileName="ydwx"></emp:message>
						</th>
						<th class="ydwx_content">
							<emp:message key="ydwx_wxgl_wxmb_neirong" defVal="内容" fileName="ydwx"></emp:message>
						</th>
						<th class="ydwx_validtm">
							<emp:message key="ydwx_common_time_youxiaoshijian" defVal="有效时间" fileName="ydwx"></emp:message>
						</th>
						<th class="ydwx_type">
							<emp:message key="ydwx_wxgl_wxmbs_leibie" defVal="类别" fileName="ydwx"></emp:message>
						</th>
						<th align="center" colspan="2" class="ydwx_operate">
							<emp:message key="ydwx_common_caozuo" defVal="操作" fileName="ydwx"></emp:message>
						</th>
					</tr>
				</thead>						<tbody>
						<%
							if(beans != null && beans.size()>0){
								for(DynaBean bean : beans){
						%>
									
							<tr>
								<td class="textalign">
									<%=bean.get("id") %>
								</td>
								<td class="textalign">
								<%=bean.get("name") %>
								</td>
								<td>
								<a href="javascript:Looks('<%=bean.get("id") %>');"><emp:message key="ydwx_common_yulan" defVal="预览" fileName="ydwx"></emp:message></a>
								</td>
								<% String time="";
								if(bean.get("timeout")!=null){
								String tempStr=bean.get("timeout").toString();
								if(!"".equals(tempStr)&&tempStr.length()>=2){
								time=tempStr.substring(0,tempStr.length()-2);
								}
								}
								%>
								<td>
								<%=time %>
								</td>
								<td>
								<%=bean.get("typename") %>
								</td>
								<td><a href="javascript:delNetTemplate('<%=bean.get("id") %>');"><emp:message key='common_delete' defVal='删除' fileName='common'></emp:message></a></td>
								<td><a href="javascript:setNetTemplate('<%=bean.get("id") %>',2,'<%=path%>');"><emp:message key='ydwx_wxgl_qxmb' defVal='取消模板' fileName='ydwx'></emp:message></a></td>
							</tr>
						<%			
								}
							}else{%>
							<tr><td colspan="7"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
							<%}
						%>
						</tbody>
						
						<tfoot>
							<tr>
								<td colspan="7">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
		<div id="divBox" style="display:none" title="<emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message>">
			<div  align="center" class="ydwx_divBox_sub">
				<emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
				<select class="ym ydwx_ym" ></select>
			</div>
			<div class="ydwx_preview_common_dv">
				<iframe class="ydwx_preview_common" id="nm_preview_common" src=""></iframe>
			</div>
		</div>
	
<script type="text/javascript">
		$(document).ready(function(){
		
			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			
			deleteleftline();
			
			$("#divBox").dialog({
				autoOpen: false,
				height:510,
				width: 300,
				modal: true,
				close:function(){
				}
			});
		
			$(".ym").change(function(){
	           var id=$(this).val();
	           for(var i=0;i<listpage.length;i++){
	                 if(id==listpage[i].id){
                         // 此处为显示错误页面，避免进入登录页面
                         if(listpage[i].content=="notexists"){
                             $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
                         }else{
                             $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
                         }
	                 }
	           }
	       	});
			
		});
		
		function delNetTemplate(id){
			if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qdshch"))){
				$.post("<%=path%>/wx_template.htm?method=updMB",{id:id,type:3,name:""},function(data){
				 if(data==1){
				    alert(getJsLocaleMessage("ydwx","ydwx_common_czchg"));
	                location.href = location;
				 }else if(data==0){
                    alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));  				   
				 }
				});
			}
    	} 
    	
    	function setNetTemplate(id,act,path){
			$("#netTempId").val(id);
			if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qdqx"))){
				$.post("wx_template.htm?method=cancelType",{netid:id},function(data){
				 if(data=="true"){
				    alert(getJsLocaleMessage("ydwx","ydwx_common_qxmbchg"));
				    location.href = location;
				 }else if(data=="false"){
                    alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));  				   
				 }
				});
			}
		}  
			
		function Looks(netId){	
		
		    $("#netid").val(netId);
		    $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
		    data=eval("("+data+")");
		    listpage=data;
		    $(".ym").children().remove();
		    for(var i=0;i<listpage.length;i++) {
		        $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
		   }
			// 此处为显示错误页面，避免进入登录页面
			if(listpage[0].content=="notexists"){
				$("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
			}else{
				$("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
			}
		      //showDlgDivs();
		      $("#divBox").dialog("open");
	     });
	   }           
			
	</script>				
	</body>
</html>