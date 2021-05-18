<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo =(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	boolean addcode = false;
	if(btnMap.get(menuCode+"-1")!=null)
	{
		addcode = true;
	}
	boolean delcode = false;
	if(btnMap.get(menuCode+"-2")!=null)
	{
		delcode = true;
	}
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute("emp_lang");
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath %>/common/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" href="<%=inheritPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=iPath %>/css/templateManger.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
        <script type="text/javascript" src="<%=inheritPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=inheritPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/addrbook.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
</head>
<body id="ydwx_templateManger">
		<div id="container" class="container">
				<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<%-- header结束 --%>
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent"  >
			<div id="loginUser" class="hidden"></div>
				<div class="left_dep div_bd" align="left">
					<h3 class="div_bd title_bg">
						<emp:message key="ydwx_wxgl_wxmb_leixing" defVal="模板类型" fileName="ydwx"></emp:message>
					</h3>
					<div id="depOperate" class="depOperate">
						<%if (delcode){%>
							<span id="delDepNew" class="depOperateButton3" onclick="del()"></span>
						<%} %>
						<span id="updateDepNew" class="depOperateButton2" onclick="update()"></span>
						<%if (addcode){%>
							<span id="addDepNew" class="depOperateButton1" onclick="add()"></span>
						<%} %>
					</div>
					<div class="list ydwx_list">
						<div id="depTree"><jsp:include page="templateTree.jsp"></jsp:include>  </div>	
					</div>
				</div>
		<div class="right_info">
			<form name="pageForm" action="" method="post">
					<input type="hidden" id="servletUrl" value="wx_template.htm"/>
				<div id="loginUser" class="hidden"></div>
				<input type="hidden" name="sortId" id="sortId">
				<div class="buttons">
					<div id="toggleDiv"> </div>
				</div>
				<div id="condition">
					<table width="100%">
					<tr>
						<td>
							<span><emp:message key="ydwx_wxgl_wxmb_bianhaos" defVal="模板编号：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input type="text" name="mbid" id="mbid" class="ydwx_mbid"  onkeyup="if(this.value!=this.value.replace(/\D/g,'')) this.value=this.value.replace(/\D/g,'')" value="" />
						</td>
						<td>
							<span><emp:message key="ydwx_wxgl_wxmb_mingchens" defVal="模板名称：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input type="text" name="mbname" id="mbname"" class="ydwx_mbname" value="">
						</td>
						<td class="tdSer">
							<center><a id="search"></a></center>
						</td>
					</tr>
					</table>
		 		</div>
		 <div id="tableInfo"></div>
		 </form>
	  </div>
	
	  <div id="addDiv" style="display: none; ">
			<br>
			<input type="hidden" name="optType" id="optType">
			<input type="hidden" id="tempName" class="ydwx_tempName">
			<div class="mb10 ydwx_addDiv_sub">
				<div class="mb10"><b><emp:message key="ydwx_wxgl_wxmb_shurumingchen" defVal="请输入名称：" fileName="ydwx"></emp:message></b></div>
				<div><input type="text" id="name"  class="ydwx_name"></div>

			</div>
			<div align="center" class="ydwx_btns">
				<input class="btnClass5 mr23" type="button" onclick="addOk();" value="<emp:message key='ydwx_common_btn_queren' defVal='确认' fileName='ydwx'></emp:message>">
				<input type="button" class="btnClass6" onclick="cancel();" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>"><br/>
			</div>
		</div>
	</div>
	<%} %>
	</div>
	<%-- 内容结束 --%>
<%-- 加载JS --%>
<script type="text/javascript">
	$(document).ready(function(){
		getLoginInfo("#loginUser");
		$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
		
		
		$('#search').click(function(){submitForm();});
		$('#tableInfo').load('wx_template.htm?tttt=<%=System.currentTimeMillis()%>',
			{method:'getTable',lguserid:$("#lguserid").val(),lgcorpcode:$("#lgcorpcode").val()});
		$("#addDiv").dialog({
			autoOpen: false,
			height:160,
			width: 240,
			modal: true,
			close:function(){
			}
		});
		setLeftHeight();
	});
	

	function add(){
		$("#optType").val("0");
		$("#addDiv").dialog("open");
	}
	
	function update(){
		$("#optType").val("1");
		 var node=zTree.getSelectedNode(); 
		 if(!node){
		 	alert(getJsLocaleMessage("ydwx","ydwx_wxmb_2"));
		 	return ;
		 }
            if(node.id==0){
                  alert(getJsLocaleMessage("ydwx","ydwx_wxmb_3"));
                  return;
           } else{
	         $("#name").val(node.name);
	         $("#tempName").val(node.name);
	         $("#sortId").val(node.id);
	         
	    }
		$("#addDiv").dialog("open");
	}
	
	function cancel(){
		$("#name").val("");
		$("#addDiv").dialog("close");
	}
	
	function addOk(){
	var namevalue=$.trim($("#name").attr("value"));
		if($("#name").val().length>10){
			alert(getJsLocaleMessage("ydwx","ydwx_wxmb_4"));
			return;
		}
      if(namevalue==""){
          alert(getJsLocaleMessage("ydwx","ydwx_wxmb_5"));
          return;
      }else{
          var lgcorpcode = $("#lgcorpcode").val();
          var lguserid = $("#lguserid").val();
          var type = $("#optType").val();
          var sortId = $("#sortId").val();
          if(type == 1){
          	var tempName = $("#tempName").val();
          	if(tempName == $("#name").val()){
          		alert(getJsLocaleMessage("ydwx","ydwx_wxmb_6"));
          		return;
          	}
          }
            if($("#name").val().indexOf("'")!=-1  || outSpecialChar($("#name").val())  ){
             	alert(getJsLocaleMessage("ydwx","ydwx_wxmb_13"));
             	return;
             }
           $.post("<%=path%>/wx_template.htm?method=updMB",{name:$("#name").val(),id:sortId,type:type,lgcorpcode:lgcorpcode,lguserid:lguserid},function(data){
			    
			    if(data==1){
			       alert(getJsLocaleMessage("ydwx","ydwx_common_czchg"));
			      	location.reload();
			    }else if(data==0){
			       alert(getJsLocaleMessage("ydwx","ydwx_wxmb_7"));
			        return;
			    } else if(data==2){
			        alert(getJsLocaleMessage("ydwx","ydwx_wxmb_8"));
			        return;
			    }
			}); 
      }
	}
	
	function del(){
		 var node=zTree.getSelectedNode();
		 if(!node){
		 	alert(getJsLocaleMessage("ydwx","ydwx_wxmb_2"));
		 	return ;
		 }
	     if(node.id==0){
	         alert(getJsLocaleMessage("ydwx","ydwx_wxmb_9"));
	         return;
	     }else if(node.id==10){
	         alert(getJsLocaleMessage("ydwx","ydwx_wxmb_10"));
	         return ;
	     }else{
	     	var lgcorpcode = $("#lgcorpcode").val();
	        $.post("<%=path%>/wx_template.htm?method=getSortCount",{lgcorpcode:lgcorpcode,sortId:node.id},function(data){
				if(data>0){
				    alert(getJsLocaleMessage("ydwx","ydwx_wxmb_11"));
				    return;
				}else{
				     if(window.confirm(getJsLocaleMessage("ydwx","ydwx_wxmb_12"))){
			              $.post("<%=path%>/wx_template.htm?method=updMB",{name:"",id:node.id,type:2},function(data){
						    if(data==1){
						       alert(getJsLocaleMessage("ydwx","ydwx_common_czchg"));
						       location.href = location;
						    }else if(data==0){
						       alert(getJsLocaleMessage("ydwx","ydwx_wxmb_7"));
						    }
						});
			           } 
				   }   
				           
		    });
	       }
	}
</script>	
	
</body>
</html>			
