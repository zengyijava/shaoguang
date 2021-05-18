<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.beanutils.DynaBean" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path=request.getContextPath();
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
	//按钮权限
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	
	@ SuppressWarnings("unchecked")
	List<DynaBean> surveyList = (List<DynaBean>)request.getAttribute("surveyList");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	
	//修改时修改操作员后，返回最后一次查看的页面
	session.setAttribute("lastPageInfo",pageInfo);
	session.setAttribute("lastConMap",conditionMap);
	String findResult= (String)request.getAttribute("findresult");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode)%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=inheritPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script language="javascript" src="<%=iPath%>/js/sysuser.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		
		function show(){
			<% 
				String result=(String)request.getAttribute("sresult");
				if(result!=null && result.equals("succ")){
			%>
					/*操作成功！*/
					alert(getJsLocaleMessage("ydwx","common_operateSucceed"));
			<%
				}else if(result!=null && !result.equals("succ")){%>
					/*操作失败！*/
					alert(getJsLocaleMessage("common","common_operateFailed"));
			<%
				}
				request.removeAttribute("sresult");
			 	if (result != null) {
			%>
					location.href="<%=path%>/wx_survey.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
			<%	}%>
		}
		
		var zTree1;
		var setting;
		var zNodes =[];

		function showMenu() {
			//$("select[name='subno']").css("visibility","hidden");
			var sortSel = $("#sortSel");
			var sortOffset = $("#sortSel").offset();
			$("#dropMenu").toggle();
			//$("select[name='subno']").css("visibility","visible");
		}
		function hideMenu() {
			$("#dropMenu").hide();
		//	$("select[name='subno']").css("visibility","visible");
		}
		
		function zTreeOnClick(event, treeId, treeNode) {
			if (treeNode) {
				var sortObj = $("#sortSel");
				sortObj.attr("value", treeNode.name);
				$("#depName").attr("value",treeNode.id);
				hideMenu();
			}
		}

		function reloadTree() {
			hideMenu();
			zTree1 = $("#dropdownMenu").zTree(setting, zNodes);
		}	
		function cAll()
		{
			var sortObj = $("#sortSel");
            /*全部*/
			sortObj.attr("value", getJsLocaleMessage("common","common_whole"));
			$("#depName").attr("value","");
			hideMenu();
		}
		$(document).ready(function(){
		getLoginInfo("#loginUser");
		setting = {
				async : true,
				asyncUrl :"<%=path%>/opt_department.htm?method=createTree2&lguserid="+$("#lguserid").val(), //获取节点数据的URL地址
				isSimpleData: true,
				rootPID : -1,
				treeNodeKey: "id",
				treeNodeParentKey: "pId",
				asyncParam: ["depId"],
				callback: {
					click: zTreeOnClick,
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree1.getNodeByParam("level", 0);
						   zTree1.expandNode(rootNode, true, false);
						}
					}
				}
		};
		var findresult="<%=findResult%>";
	    if(findresult=="-1")
	    {
	        /*加载页面失败,请检查网络是否正常!*/
	       alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_54"));
	       return;			       
	    }
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
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
		
		
			$('#search').click(function(){submitForm();});
			reloadTree();
			$("#call").hover(function(){$("#call").css("background-color","#c1ebff");},
					function(){$("#call").css("background-color","");});
					
				
			$("#content select").empSelect({
					width:80
			});
			
			$("#divBox").dialog({
				autoOpen: false,
				height:500,
				width: 300,
				modal: true,
				close:function(){
				}
			});
			
			show();

            //页码改变时  div层内容变化的方法
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

		function toAdd(){
			location.href="wx_survey.htm?method=toAdd&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
		}
		function toEdit(netid){
			location.href="wx_survey.htm?method=findBYid&netid="+netid+"&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
		}

		function rtime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#creatdateBegin").attr("value");
			if(v.length != 0)
			{
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				var day = 31;
				if (mon != "12")
				{
				    mon = String(parseInt(mon,10)+1);
				    if (mon.length == 1)
				    {
				        mon = "0"+mon;
				    }
				    switch(mon){
                        case "02":day = 28;break;
                        case "04":day = 30;break;
                        case "06":day = 30;break;
                        case "09":day = 30;break;
                        case "11":day = 30;break;
                        default:day = 31;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)+1));
				    mon = "01";
				}
				max = year+"-"+mon+"-"+day+" 23:59:59"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
		
		};

		function stime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#creatdateEnd").attr("value");
		    var min = "1900-01-01 00:00:00";
			if(v.length != 0)
			{
			    max = v;
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				if (mon != "01")
				{
				    mon = String(parseInt(mon,10)-1);
				    if (mon.length == 1)
				    {
				        mon = "0"+mon;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)-1));
				    mon = "12";
				}
				min = year+"-"+mon+"-01 00:00:00"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
		
		};

		function delSurvey(netId)
		{
            /*确定删除该记录？*/
            if(confirm(getJsLocaleMessage("common","common_js_operPageField_1"))){
			var lgcorpcode = $("#lgcorpcode").val();
			$.post("<%=path%>/wx_survey.htm",{method:"del",netId:netId,lgcorpcode:lgcorpcode},
				function(result)
				{
					
					if(result=="succ")
					{
                        /*删除成功！*/
                        alert(getJsLocaleMessage("common","common_deleteSucceed"));
						submitForm();
						return;
					}
					else
					{
                        /*删除失败！*/
                        alert(getJsLocaleMessage("common","common_deleteFailed"));
						return;
					}
				});
			}
		}
		
		//查看
		function Look(netId){	
		    //$("#netid").val(netId);
		    $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
		       data=eval("("+data+")");
		       listpage=data;
		       //$(".ym").children().remove();
		       for(var i=0;i<listpage.length;i++)	{   
		           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
		      }
                // 此处为显示错误页面，避免进入登录页面
                if(listpage[0].content=="notexists"){
                    $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
                }else{
                    $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
                }
		      $("#divBox").dialog("open");
	       });
	    }
		
		</script>
		<style type="text/css">
		#rolesDialog label {
			width:100%;
		}
		#rolesDialog xmp
		{
			display: inline;
		}
		</style>
	</head>
	<body>
	<input type="hidden" id="pathUrl" value="<%=path%>" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<div class="top">
				<div id="top_right">
					<div id="top_left"></div>
					<div id="top_main">
					<strong><emp:message key="ydwx_add_jsp_1" defVal="当前位置：" fileName="ydwx"></emp:message></strong>[<%=titleMap.get(menuCode.substring(0,menuCode.indexOf("-")))%>]-[<%=titleMap.get(menuCode)%>]</div>
				</div>
			</div>
			<div id="roleName" style="padding:5px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"></div>
			</div>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%
				if(btnMap.get(menuCode+"-0")!=null)                       		
							{
			%>
					<form name="pageForm" action="<%=path%>/wx_survey.htm" method="post" id="pageForm">
						<div id="loginUser" class="hidden"></div>
						<input type="hidden" id="inheritPath" value="<%=inheritPath%>"/>
							<div class="buttons">
								<div id="toggleDiv"></div>
								<%
									if(btnMap.get(menuCode+"-1")!=null){
								%>
									<a id="add" onclick="javascript:toAdd()"><emp:message key="common_establish" fileName="common" defVal="新建"></emp:message></a>
								<%
									}
								%>
								
							</div>
							<div id="condition">
								<table>
									<tr>
										<td><emp:message key="ydwx_survey_1" defVal="问卷名称：" fileName="ydwx"></emp:message></td>
										<td>
											<input type="text" name="name" id="name" style="width:180px" value="<%=conditionMap.get("name")==null?"":conditionMap.get("name") %>" />
										</td>
										<td><emp:message key="ydwx_wxgl_hdxgl_cjr" defVal="创建人：" fileName="ydwx"></emp:message></td>
										<td>
											<input id="creatid"  name="creatid" value="<%=conditionMap.get("creatid")==null?"":conditionMap.get("creatid") %>" type="text" style="width:180px"/>
										</td>
										<td class="tdSer">
											<center><a id="search"></a></center>
										</td>
									</tr>
										
									<tr>
										<td><emp:message key="ydwx_common_time_chuangjianshijians" defVal="创建时间：" fileName="ydwx"></emp:message></td>
										<td>
											<input type="text" id="creatdateBegin" name="creatdateBegin" 
											value="<%=conditionMap.get("creatdateBegin")==null?"":conditionMap.get("creatdateBegin") %>"
											style="cursor: pointer; width: 180px;background-color: white;" 
											class="Wdate" readonly="readonly" onclick="stime()">
										</td>
										<td><emp:message key="common_to" defVal="至：" fileName="common"></emp:message></td>
										<td>
											<input type="text" id="creatdateEnd" name="creatdateEnd" 
											value="<%=conditionMap.get("creatdateEnd")==null?"":conditionMap.get("creatdateEnd") %>"
											style="cursor: pointer; width: 180px;background-color: white;" 
											class="Wdate" readonly="readonly" onclick="rtime()">
										</td>
										<td>
										</td>
									</tr>
										
								</table>
							</div>
							<table id="content">
								<thead>
									<tr>
										<th>
											<emp:message key="ydwx_survey_3" defVal="问卷ID" fileName="ydwx"></emp:message>
										</th>
										<th>
											<emp:message key="ydwx_survey_2" defVal="问卷名称" fileName="ydwx"></emp:message>
										</th>
										<th>
											<emp:message key="ydyw_ywgl_ywbgl_text_30" fileName="ydyw" defVal="状态"></emp:message>
										</th>
										<th>
											<emp:message key="ydwx_wxgl_cjr" defVal="创建人" fileName="ydwx"></emp:message>
										</th>
										<th>
											<emp:message key="ydwx_wxgl_hdxgl_ssjg" defVal="所属机构" fileName="ydwx"></emp:message>
										</th>
										<th style="width:125px;">
											<emp:message key="ydwx_common_time_chuangjianshijians" defVal="创建时间：" fileName="ydwx"></emp:message>
										</th>
										<th style="width:125px;">
											<emp:message key="ydwx_common_time_gengxinshijian" defVal="更新时间" fileName="ydwx"></emp:message>
										</th>
										
										<th colspan=2>
											<emp:message key="common_operation" defVal="操作" fileName="common"></emp:message>
										</th>
									</tr>
								</thead>
								<tbody>
									<%
										if(surveyList != null && surveyList.size()>0){
											DynaBean bean = null;	
											
											for(int v=0; v < surveyList.size(); v++)
											{
												bean = surveyList.get(v);
									%>
									<tr>
										<td>
											<%=bean.get("id") %>
										</td>
										<td class="textalign">
											<a onclick="Look(<%=bean.get("id") %>)" title="<%=bean.get("name") %>">
												<label style="display:none"><xmp><%=bean.get("name") %></xmp></label>
												<xmp><%=bean.get("name").toString().length()>10?bean.get("name").toString().substring(0,10)+"...":bean.get("name") %></xmp>
											</a>
										</td>
										<td class="textalign">
											<%
												String statusInfo = "-";
												if(bean.get("status").equals(1)){
												    /*定稿*/
													statusInfo = MessageUtils.extractMessage("ydwx","ydwx_survey_4",request);
												}else if(bean.get("status").equals(0)){
												    /*草稿*/
													statusInfo = MessageUtils.extractMessage("ydwx","ydwx_wxgl_EMPspzt_options2",request);
												}else if(bean.get("status").equals(2)){
												    /*审批通过*/
													statusInfo = MessageUtils.extractMessage("ydwx","ydwx_wxgl_EMPspzt_options4",request);
												}else if(bean.get("status").equals(3)){
												    /*审批未通过*/
													statusInfo = MessageUtils.extractMessage("ydwx","ydwx_wxgl_EMPspzt_options5",request);
												}
											%>
											<label><%=statusInfo %></label>
										</td>
										<td>
											<%=bean.get("uname") %>
										</td>
										<td>
											<%=bean.get("dep_name") %>
										</td>
										<td class="textalign">
											<%=bean.get("creatdate")==null?"":df.format(bean.get("creatdate")) %>
										</td>
										<td class="textalign">
											<%=bean.get("modifydate")==null?"":df.format(bean.get("modifydate")) %>
										</td>
										
										<%
											if(btnMap.get(menuCode+"-3")!=null)
											{
										%>
										<td>
											<a onclick="javascript:toEdit(<%=bean.get("id") %>);" <%=!bean.get("status").equals(0)?"disabled":"" %> ><emp:message key="ydwx_common_btn_xiugai" defVal="修改" fileName="ydwx"></emp:message></a>
										<%
											}else
											{
												out.print("-");
											}
										%>
										</td>
										
										<%
											if(btnMap.get(menuCode+"-2")!=null)
											{
										%>
										<td>
											<a onclick="javascript:delSurvey(<%=bean.get("netid") %>);"><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a>
										<%
											}else
											{
												out.print("-");
											}
										%>
										</td>
										
										</tr>
									<%
										}
									}else{
									%>
										<tr>
											<td colspan="16" align="center">
												<emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message>
											</td>
										</tr>
									<%
										}
									%>
								</tbody>
								<tfoot>
							<tr>
							<td colspan="16">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
			</form>
			<%
				}
			%>
						
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

	<div id="divBox" class="hideDlg" style="display:none" title="<emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message>">
		<div style="width:240px;height:460px;margin-left:30px; margin-top:-3px; background:url(<%=inheritPath %>/common/img/iphone5.jpg);">
			<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="nm_preview_common" src=""></iframe>	
		</div>
	</div>    

	</body>
</html>
