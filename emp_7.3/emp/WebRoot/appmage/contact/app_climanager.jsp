<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.entity.appmage.LfAppOlGroup"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	LinkedHashMap<String,String> conditionMap = (LinkedHashMap)request.getAttribute("conditionMap");
	Object appArrayList=request.getAttribute("appArrayList");
	
	Object selectAll=request.getAttribute("selectAll");
	String menuCode = titleMap.get(rTitle);
	@ SuppressWarnings("unchecked")
	List<DynaBean> trustDatas = (List<DynaBean>)request.getAttribute("clientList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
	String showSex="";
	if(conditionMap.get("sex")!=null&&!"".equals(conditionMap.get("sex"))){
		showSex=conditionMap.get("sex")+"";
	}
	//处理是否被选中的(防止刷新后，节点上的值没有了)
	String groupid="";
	if(conditionMap.get("groupid")!=null&&!"".equals(conditionMap.get("groupid"))){
		groupid=conditionMap.get("groupid")+"";
	}
	Object export=request.getAttribute("exportExcel");
	String exportExcel="";
	if(export!=null&&!"".equals(export)&&"exportExcel".equals(export)){
		exportExcel="exportExcel";
	}
	
		
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="appmage_jcsj_yhgl_text_usergroup" defVal="用户群组" fileName="appmage"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" >
		<link href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" >
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<style>
			.avatar{
				width:40px;
				vertical-align: middle;
				padding: 3px 0;
			}
			#selectAll{
				vertical-align: -2px;
			}
			.showControl{
				padding: 3px 0 0 15px;
				float: left;
			}
		</style>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link href="<%=iPath %>/css/contact_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			<%-- header结束 --%>
		<div id="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
			 <div id="frame_main">
				<div class="left_dep div_bd" align="left" >
					<input type="hidden" id="id" />
					<h3 class="div_bd title_bg">
		         	<emp:message key="appmage_jcsj_yhgl_text_usergroup" defVal="用户群组" fileName="appmage"></emp:message>
					</h3>
						<span class="showControl">
						<input type="checkbox" name="selectAll" onclick="showAll()" id="selectAll" <%if(selectAll!=null&&!"".equals(selectAll)) {%> checked<%}%> >
						<emp:message key="appmage_jcsj_yhgl_text_allshow" defVal="全显" fileName="appmage"></emp:message>
						</span>
						<div id="depOperate" class="depOperate">
						<span id="delDepNew" class="depOperateButton3" onclick="doDel()"></span>
					    <span id="updateDepNew" class="depOperateButton2" onclick="updateDepFun()"></span>
						<span id="addDepNew" class="depOperateButton1" onclick="addDepFun()"></span>

					</div>
					<div id="etree"  style="clear:both;">
					</div>
				</div>
				
				<div class="right_info">
				<form name="pageForm" id="pageForm" action="<%=path%>/app_climanager.htm?method=find" method="post">
				<input type="hidden" name="depId" id="depId" value='1'/>
				<input type="hidden" name="lgcorpcode" id="lgcorpcode" value="<%=request.getAttribute("lgcorpcode") %>"/>
				<div id="getloginUser" style="display:none;"></div>
				
					<div class="buttons mb10">
						<div>
						<a id="exportCondition"><emp:message key="appmage_common_opt_daochu" defVal="导出" fileName="appmage"></emp:message></a>
						</div>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td><emp:message key="appmage_jcsj_yhgl_text_useraccount" defVal="用户账户" fileName="appmage"></emp:message>：</td>
									<td><input id="app_code" name="app_code" style="width:160px" type="text" maxlength="32" value="<%=conditionMap.get("app_code") == null?"":conditionMap.get("app_code") %>"/></td>
									<td><emp:message key="appmage_jcsj_yhgl_text_nichen" defVal="昵称" fileName="appmage"></emp:message>：</td>
									<td>
										<input id="nickname" name="nickname" style="width:160px" type="text" maxlength="32" value="<%=conditionMap.get("nickname") == null?"":conditionMap.get("nickname") %>"/>
									</td>

									<td><emp:message key="appmage_jcsj_yhgl_text_phone" defVal="手机号码" fileName="appmage"></emp:message>：</td>
									<td>
										<input id="phone"  name="phone"  style="width:160px" onkeyup="numberControl($(this))" onblur="numberControl($(this))"  type="text" maxlength="32" value="<%=conditionMap.get("phone") == null?"":conditionMap.get("phone") %>"/>
									</td>
									<td class="tdSer">
										<a id="search"></a>
									</td>
									<td></td>
								</tr>
								<tr>
									
									<td><emp:message key="appmage_jcsj_yhgl_text_sex" defVal="性别" fileName="appmage"></emp:message>：</td>
									<td>
										<select name="sex" id="sex" style="width:165px">
										<option value=""><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
										<option value="1" <%if(("1").equals(showSex)) {%>selected="selected" <%}%> ><emp:message key="appmage_jcsj_yhgl_text_man" defVal="男" fileName="appmage"></emp:message></option>
										<option value="2" <%if(("2").equals(showSex)) {%>selected="selected" <%}%> ><emp:message key="appmage_jcsj_yhgl_text_women" defVal="女" fileName="appmage"></emp:message></option>
										<option value="0" <%if(("0").equals(showSex)) {%>selected="selected" <%}%>><emp:message key="appmage_xxfb_appsyfb_text_unknown" defVal="未知" fileName="appmage"></emp:message></option>
										</select>
									</td>
									<td><emp:message key="appmage_jcsj_yhgl_text_age" defVal="年龄" fileName="appmage"></emp:message>：</td>
									<td>
										<input id="age"  name="age"  style="width:160px" onkeyup="numberControl($(this))" onblur="numberControl($(this))"  type="text" maxlength="10" value="<%=conditionMap.get("age") == null?"":conditionMap.get("age") %>"/>
									</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>	
								</tr>
								<tr>
								<td><emp:message key="appmage_jcsj_yhgl_text_regtime" defVal="注册时间" fileName="appmage"></emp:message>：</td>
								 <td>
									<input type="text" name="createtime" id="createtime"
										style="cursor: pointer; width: 162px;background-color: white;" 
										class="Wdate" readonly="readonly" onclick="stime()"
										value="<%=conditionMap.get("createtime")== null?"":conditionMap.get("createtime") %>" />
										
								 </td>	
								 <td><emp:message key="appmage_common_text_zhi" defVal="至：" fileName="appmage"></emp:message></td>
								 <td><input type="text" name="endtime" id="endtime"
										style="cursor: pointer; width: 162px;background-color: white;" 
										class="Wdate" readonly="readonly" onclick="rtime()"
										value="<%=conditionMap.get("endtime")== null?"":conditionMap.get("endtime") %>"/></td>		    
								 	<td></td>
									<td></td>	
									<td></td>
									<td></td>	
								</tr>
							</tbody>
						</table>
					</div>
					<p>
				<input type="button" value="<emp:message key="appmage_jcsj_yhgl_text_addgroup" defVal="添加到群组" fileName="appmage"></emp:message>" class="btnClass3 appmage_appuser_member" onclick="javascript:showUserMenu();"/>
				<input type="button" value="<emp:message key="appmage_jcsj_yhgl_text_exitgroup" defVal="退出群组" fileName="appmage"></emp:message>" class="btnClass3 appmage_appuser_member" 	onclick="javascript:allOut();"/>
				</p>
				<div id="dropMenu_user" style="position: absolute;display:none; left:321px;top:168px; width:366px;background-color:white;border:1px solid;z-index:999;">
					<div style="margin-top: 3px;margin-right:10px;text-align:right">
						<input type="button" value="<emp:message key="appmage_common_opt_queding" defVal="确定" fileName="appmage"></emp:message>" class="btnClass1" id="confim" onclick="javascript:changeGroup();" style=""/>&nbsp;&nbsp;
						<input type="button" value="<emp:message key="appmage_common_opt_qingkong" defVal="清空" fileName="appmage"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_employ();" style=""/>
						<input type="button" value="<emp:message key="appmage_jcsj_yhgl_text_shutdown" defVal="关闭" fileName="appmage"></emp:message>" class="btnClass1" onclick="javascript:closediv();" style=""/>
					</div>
				<ul id="dropdownMenu_user" class="tree" style="margin-left:0px;height:450px; width:356px; background-color:white;overflow-y:auto;overflow-x:auto;z-index:999;">
				</ul>
				</div>
					<div style="clear:right"></div>	
					<div id="bookInfo">

					</div>
				</form>
				</div>
				</div>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    
	
    <div class="clear"></div>
    <div id="changeDep" style="padding:5px;display:none;">
			 <input type="hidden" id="changeDepId" >
			<div id="depDiv" style="">
				<ul id="dropdownMenu" class="tree"></ul>
			</div>
			<div style="height: 20px;"></div>
			<center>
				<div>
					<input type="button" value="<emp:message key="appmage_common_opt_queding" defVal="确定" fileName="appmage"></emp:message>" onclick="doSubmit()" class="btnClass5 mr23"/>
					<input type="button" value="<emp:message key="appmage_common_opt_quxiao" defVal="取消" fileName="appmage"></emp:message>" onclick="depTreeCancel()" class="btnClass6"/>
				</div>
			</center>
			  
	</div>

<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script>

<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=iPath %>/js/webexbook.js"></script>
<script type="text/javascript">
var setting_user;
var zNodes_user = [];
 var zTree_user;
 var pathUrl ;
 var zTree;
 var pathUrl = $("#pathUrl").val();
 //处理变换群组
 var selected="";
	var groupid ='<%=groupid%>';
var exportExcel='<%=exportExcel%>';
 var lgcorpcode ='<%=request.getAttribute("lgcorpcode")%>';
	if(exportExcel!=""){
		//alert("无数据可导出！");
		alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_nodatatoexport'));
	}
$(document).ready(function() {
	
	$(window).resize(function(){
		setLeftDep_layout();
	})
		setting_user = {
				async : true,
				asyncUrl : pathUrl
						+ "/app_climanager.htm?method=createTree2", // 获取节点数据的URL地址
				isSimpleData : true,
				rootPID : -1,
				treeNodeKey : "id",
				checkable: true,
				treeNodeParentKey : "pId",
				asyncParam : [ "depId" ],
				callback : {
					change: zTreeOnClick,
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree.getNodeByParam("level", 0);
						   zTree.expandNode(rootNode, true, false);
						}
					}
				}
			};



	zTree = $("#dropdownMenu_user").zTree(setting_user, zNodes_user);

	getLoginInfo("#getloginUser");
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
	
	pathUrl = $("#pathUrl").val();
	$("#addDepNew").hover(function() {
		$(this).removeClass("depOperateButton1");
		$(this).addClass("depOperateButton1On");
	}, function() {
		$(this).addClass("depOperateButton1");
		$(this).removeClass("depOperateButton1On");
	});

	$("#updateDepNew").hover(function() {
		$(this).removeClass("depOperateButton2");
		$(this).addClass("depOperateButton2On");
	}, function() {
		$(this).addClass("depOperateButton2");
		$(this).removeClass("depOperateButton2On");
	});

	$("#delDepNew").hover(function() {
		$(this).removeClass("depOperateButton3");
		$(this).addClass("depOperateButton3On");
	}, function() {
		$(this).addClass("depOperateButton3");
		$(this).removeClass("depOperateButton3On");
	});


	

	var lguserid='<%=request.getAttribute("lguserid")%>';
	//为了处理保持之前状态
	var skip='<%=request.getParameter("skip")%>';
	$("#etree").load(pathUrl+"/app_cligroupmanager.htm?lguserid="+lguserid+"&groupid="+groupid+"&lgcorpcode="+lgcorpcode+"&time="+new Date().getTime(),function(){
		setLeftDep_layout();
	});
	$('#bookInfo').load(pathUrl+"/app_climanager.htm?method=findList&lguserid="+lguserid+"&skip="+skip+"&groupid="+groupid+"&lgcorpcode="+lgcorpcode+"&time="+new Date().getTime());
		
	});

	//查询功能	
	$("#search").click(function(){
		var search = document.getElementById('search');
		if(search)
		{
			search.isClick = true;
		}
		window.parent.loading();
		groupid=$("#groupid").val();
		var nickname=$("#nickname").val();
		var sex=$("#sex").val();
		var app_code=$("#app_code").val();
		var phone=$("#phone").val();
		var age=$("#age").val();
		var createtime=$("#createtime").val();
		var endtime=$("#endtime").val();
		$('#bookInfo').load(pathUrl+'/app_climanager.htm?method=findList&time='+new Date().getTime(),{lguserid:lguserid,
			groupid:groupid,
			nickname:nickname,
			sex:sex,
			app_code:app_code,
			phone:phone,
			age:age,
			createtime:createtime,
			endtime:endtime,
			lgcorpcode:lgcorpcode
			},function(){
			window.parent.complete();
		});
		
	//$('#bookInfo').load(pathUrl+"/app_climanager.htm?method=findList&lguserid="+lguserid+"&groupid="+groupid+"&lgcorpcode="+lgcorpcode+"&nickname="+nickname+"&sex="+sex+"&app_code="+app_code+"&phone="+phone+"&age="+age+"&createtime="+createtime+"&endtime="+endtime);
	 });	

		
	  function showUserMenu() {
		$("#dropMenu_user").toggle();
	}	
		
  	//设置位置定位
	  function setLeftDep_layout(){
		  var left_dep=$('.left_dep'),
		  offset=left_dep.offset(),
		  win_h=$(window).height();
		  left_dep.css({'height':win_h-12-parseInt(offset.top)+'px'});
		  $('#etree').css({'height':win_h-12-parseInt(offset.top)-50+'px'});
		  $('#list').css({'height':win_h-12-parseInt(offset.top)-70+'px'});
	  }
	//选中的人员显示文本框
	function zTreeOnClick(event, treeId, treeNode) {
		if (treeNode) {				
			var zTreeNodes=zTree.getChangeCheckedNodes();
			var pops="";
			var userids = "";				
			for(var i=0; i<zTreeNodes.length; i++){
				pops+=zTreeNodes[i].id+";";				
			}					
			 selected=pops;  
		}
	}
</script>
	</body>
	
</html>
