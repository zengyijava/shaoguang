<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo  = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<DynaBean> infos = (List<DynaBean>)request.getAttribute("baseInfos");
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	String lgcorpcode = request.getParameter("lgcorpcode");
	//区分是静态还是动态
	String tmpltype = request.getParameter("tmpltype");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%--   template.js 不存在
		<script type="text/javascript" src="<%=iPath%>/js/template.js" ></script>
		--%>
		
	</head>
	<body id="eng_wxTmpl" class="eng_wxTmpl">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form name="pageForm" action="eng_mtProcess.htm?method=toWxTmpl&lguserid=<%=lguserid %>&lgcorpcode=<%=lgcorpcode %>" method="post"
					id="pageForm">
					<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
					<input type="hidden" id="ipathUrl" value="<%=iPath %>"/>
					<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
					<input type="hidden" id="skinType" value="<%=skin %>"/>
					<input id="pathUrl" value="<%=path%>" type="hidden" />
					<input type="hidden" id="tmpltype" name="tmpltype" value="<%=tmpltype %>"/>
					
			<div id="rContent" class="rContent">
					<div id="condition">
						<table>
							<tr>
								<td>
									<span><emp:message key="znyq_ywgl_xhywgl_wxmc_mh" defVal="网讯名称：" fileName="znyq"></emp:message></span>
								</td>
								<td>
									<input type="text" name="name" 
										id="name" value="<%=null!=conditionMap.get("name")?conditionMap.get("name"):"" %>"  maxlength="16"/>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
					  <tr>
							<th>
								<emp:message key="znyq_ywgl_common_text_10" defVal="选择" fileName="znyq"></emp:message>
							</th>
							<th>
								<emp:message key="znyq_ywgl_xhywgl_wxbh" defVal="网讯编号" fileName="znyq"></emp:message>
							</th>
							<th>
								<emp:message key="znyq_ywgl_xhywgl_wxmc" defVal="网讯名称" fileName="znyq"></emp:message>
							</th>
							<th>
								<emp:message key="znyq_ywgl_xhywgl_wxnr" defVal="网讯内容" fileName="znyq"></emp:message>
							</th>
							<th>
								<emp:message key="znyq_ywgl_xhywgl_cjr" defVal="创建人" fileName="znyq"></emp:message>
							</th>
							<th>
							    <emp:message key="znyq_ywgl_xhywgl_cjsj" defVal="创建时间" fileName="znyq"></emp:message>
							</th>
										
									</tr>
								</thead>
								<tbody>
								<%
								if (infos != null && infos.size() > 0)
								{
									for (DynaBean info : infos)
									{
								%>
									<tr>
										<td>
											<input type="radio" name="checklist" id="checklist" value="<%=info.get("id") %>" 
											netid="<%=info.get("netid") %>" netName="<%=info.get("name") %>"/>
										</td>
										<td>
											<%=info.get("netid") %>
										</td>
										<td class="textalign" >
											<%=info.get("name") %>
										</td>
										<td>
											<a onclick="Look(<%=info.get("netid") %>)">
												<emp:message key="znyq_ywgl_common_text_6" defVal="预览" fileName="znyq"></emp:message>
											</a> 		
										</td>
										<td>
										<%=info.get("username")!=null?info.get("username"):"" %>
										</td>
										<td>
											<%=info.get("creatdate")!=null?df.format(info.get("creatdate")):"" %>
										</td>
									</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="6"><emp:message key="znyq_ywgl_common_text_1" defVal="无记录" fileName="znyq"></emp:message></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="6">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					
					<div class="firstDiv">
						<input class="btnClass5 mr23" onclick="tempSure()" value="<emp:message key="znyq_ywgl_common_text_10" defVal="选择" fileName="znyq"></emp:message>" type="button"/>
						<input class="btnClass6" onclick="tempCancel()" value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" type="button"/>
						<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
						<br/>
					</div>
					
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			</form>
			<%-- foot结束 --%>
		</div>
		<div id="divBox" class="hideDlg" title="<emp:message key="znyq_ywgl_xhywgl_mbyl" defVal="模板预览" fileName="znyq"></emp:message>">
			<div  class="secondDiv" align="center">
          	<emp:message key="znyq_ywgl_xhywgl_ymdh_mh" defVal="页面导航：" fileName="znyq"></emp:message>
          	 <select class="ym"></select>
        	</div>
			<div id="nm_preview_common1Div" style="background:url(<%=commonPath %>/common/img/iphone5.jpg);">
				<iframe id="nm_preview_common1" src=""></iframe>	
			</div>
		</div>
		
		
		<div class="clear"></div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		//页面加载，初始化相关数据
		$(document).ready(function() {
			$("#content select").empSelect({width:80});
			$("#tempView").dialog({
				autoOpen: false,
				height:510,
				width: 290,
				resizable:false,
				close:function(){
				    cplaytime = 0;
					nplaytime = -1;
					$("#screen").empty();
					clearInterval(ttimer); 
				}
			});
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
		                     $("#nm_preview_common1").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
		               }
		            }
				});
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
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
		});

		<%--模板确认--%>
		function tempSure()
		{
			var tem = $("input[type='radio']:checked").val();
			var $ro = $("input[type='radio']:checked");
			var commonPath = $("#commonPath").val();
			var skinType = $("#skinType").val();
			if(tem == undefined || tem == "" || tem == null)
			{
				//alert("未选择网讯模板！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_wxzwxmb"));
				return;
			}else
			{
				var netid = $ro.attr("netid");
				var tmname  =$ro.attr("netName");
				parent.setWxInfo(netid);
			}
			
			<%--调用父窗口的函数--%>
			parent.closeDialog();
		}

		//关闭窗口
		function tempCancel(){
			<%--调用父窗口的函数--%>
			parent.closeDialog();
		}
		// 点击查看网讯
		function Look(netId){	
		    //$("#netid").val(netId);
		    $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
		       data=eval("("+data+")");
		       listpage=data;
		       $(".ym").children().remove();
		       for(var i=0;i<listpage.length;i++)	{   
		           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
		           		           // 此处为显示错误页面，避免进入登录页面
		           if(listpage[i].content=="notexists"){
		           	$("#nm_preview_common1").attr("src","ydwx/wap/404.jsp");
		           }else{
			        $("#nm_preview_common1").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
		           }
		      }
		      $("#divBox").dialog("open");
	       });
	    }
		</script>
	</body>
</html>
