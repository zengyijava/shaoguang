<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
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
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("mtService");

String serId = request.getParameter("serId");
LfService service=(LfService)request.getAttribute("service");

LfSysuser curSysuser=(LfSysuser)request.getAttribute("curSysuser");
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
	 	<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><emp:message key="znyq_ywgl_xhywgl_xjclbzxx" defVal="新建处理步骤信息" fileName="znyq"></emp:message></title>
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
			function show(){
				<% Object result=request.getAttribute("addProcessResult");
						if(result!=null && result.toString().equals("1")){%>
						//alert("新增步骤成功！");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xzbzcg"));
					<%}else if(result!=null && result.toString().equals("0")){%>
						//alert("操作失败！");	
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));	
					<%}request.removeAttribute("addProcessResult");
					 %>
				}
               $(document).ready(function(){
            		getLoginInfo("#hiddenValueDiv");
            	   noquot("#prName");
            	   noquot("#comments");
            	    $("#select").change(setSelect);    //选择步骤类型列表事件
            	    $("#prName").focus(removeprNameMark);    //鼠标进入输入框
            	    $("#prName").blur(addprNameMark);
            	    $("#addBasicInfoBtn").click(checkSubBefore);
            	    $('#u_o_c_explain').find('> p').next().hide();
          			$('#u_o_c_explain').click(function(){$(this).find('> p').next().toggle();});
          			show();
                });
               function setSelect(){
            	   var prType = $("#select option:selected").val(); //获取选中的值
            	   initShow();          //  初始化显示单选按钮
            	  if(prType == 4){      //判断如果选择prType的值为4,就隐藏ID为yes的radio表单和他的值
            		     $("#select").parent().find("span").remove();
            	    	 $("#no").attr("checked","checked");   //设置选中radio
            	    	 $("#yes").hide();
            	    	 $("#y").hide();
                	}
            	 else if(prType == 5){   //判断如果选择prType的值为5,就隐藏ID为no的radio表单和他的值
            		    $("#select").parent().find("span").remove();
            	    	$("#yes").attr("checked","checked");   //设置选中radio
           	    	    $("#no").hide();
           	    	    $("#n").hide();
                	 }
               }
               //提交表单之前检查步骤类型选择
               function checkSubBefore(){  
            	   var prType = $("#select option:selected").val(); //获取选中的值
            	   var prName = $("#prName").val();         //步骤名称
            	   prName = prName.replace(/\s+/g,"");         // 清除所有空格
            	   if(prName == "") {      //检查步骤名称
            		   $("#prName").parent().find("span").remove();
  					   //var msg = $("<span>").css("color", "red").text("步骤名称不能为空,请输入步骤名称");
  					   var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcbnwk"));
  						$("#prName").parent().append(msg);	
                   }
            	   var c = true;
            	   if(!prName.match( /^[\u4E00-\u9FA5a-zA-Z0-9_]+$/))
            	   {
            		   $("#prName").parent().find("span").remove();
  					   //var msg = $("<span>").css("color", "red").text("步骤名称只能由汉字、英文字母、数字、下划线组成");
  					   var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcznyhz"));
  						$("#prName").parent().append(msg);
  						c=false;
            	   }
            	   if(prType == "") {
                	     // alert("请选择步骤类型");
                	      $("#select").parent().find("span").remove();
     					   //var msg = $("<span>").css("color", "red").text("请选择步骤类型");
     					   var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qxzbzlx"));
     						$("#select").parent().append(msg);	
     				}
            	   if(prName != "" && prType != "" && c) {
                	        //if(confirm("确定提交吗?")) {
                	        if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))) {
                	        	$("#addBasicInfoBtn").attr("disabled","disabled");
                    	        $("#previous").attr("disabled","disabled");
                    	        //var msg = $("<span>").css("color", "green").text("正在提交中,请稍候.........");
                    	        var msg = $("<span>").css("color", "green").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_zztjz"));
          						$("#previous").parent().append(msg);
          						$("#proform").attr("action",$('#proform').attr('action')+'&lgcorpcode='+$('#lgcorpcode').val());
                    	        $("#proform").submit();
                    	     }
					}
            	         	  
                 }
               //  初始化显示单选按钮
               function initShow(){  
            	   $("#yes").show();
      	    	   $("#y").show();
      	    	   $("#no").show();
           	       $("#n").show();
               }

               //删除步骤名称提示信息
               function removeprNameMark(){  
            	   $("#prName").parent().find("span").remove();
                  }
               
               //添加步骤名称提示信息
               
               function addprNameMark() {
                   var prName = $("#prName").val();
                   prName = prName.replace(/\s+/g,"");         // 清除所有空格
                   if(prName == "") {
            	   $("#prName").parent().find("span").remove();
					   //var msg = $("<span>").css("color", "red").text("步骤名称不能为空,请输入步骤名称");
					   var msg = $("<span>").css("color", "red").text(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcbnwk"));
						$("#prName").parent().append(msg);	
                   }
               }
         </script>
	</head>
	<body id="eng_addMtProcess" class="eng_addMtProcess">
		<div id="container">
			<div class="top">
				<div id="top_right">
					<div id="top_left"></div>
					<b><emp:message key="znyq_ywgl_xhywgl_dqwz_mh" defVal="当前位置：" fileName="znyq"></emp:message></b>[<%=titleMap.get(menuCode.substring(0,menuCode.indexOf("-"))) %>]-[
					<%=titleMap.get(menuCode) %><emp:message key="znyq_ywgl_xhywgl_bzglxjclbzxx" defVal="]-[步骤管理]-[新建处理步骤信息]" fileName="znyq"></emp:message>
				</div>
			</div>
			<div class="rContent">
				<center><div id="u_o_c_explain">
						<p>
							<emp:message key="znyq_ywgl_xhywgl_smxjclbzxx" defVal="说明：新建处理步骤信息" fileName="znyq"></emp:message>
						</p>
						<ul>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_bzmcwsjkclbzdmc" defVal="步骤名称：为数据库处理步骤的名称" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_bzlx" defVal="步骤类型" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_selectff" defVal="Select 方法----表示本步骤处理的是一个数据库Select语句" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_replyff" defVal="Reply方法----表示本步骤是用来设置回复短信的格式" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_rgszbbzwzhclbz" defVal="如果设置本步骤为最后处理步骤，则当系统执行完本处理步骤后，将不执行后面定义的处理步骤" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_djqdanbcclbzjbxx" defVal="点击【确定】按钮保存处理步骤基本信息" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_djfhanfqbcbfhbzgllb" defVal="点击【返回】按钮放弃保存并返回步骤管理列表" fileName="znyq"></emp:message>
							</li>
						</ul>
					</div>
				<div id="detail_Info">
						<form method="post" action="<%=path %>/eng_mtProcess.htm?method=add" id="proform">
						<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
							<table>
							<thead>
								<tr>
									<td >
										<span><emp:message key="znyq_ywgl_xhywgl_bzmc_mh" defVal="步骤名称：" fileName="znyq"></emp:message></span>
									</td>
									<td class="stepNameTd">
										<input type="text" name="prName" id="prName" /><font class="tipColor">&nbsp;*</font>
										<input type="hidden" name="serId" value="<%=serId %>"/>
										<input type="hidden" name="hidOpType" value="add"/>
										<input type="hidden" name="prNo" value="0"/>
									</td>
								</tr>
								<tr>
									<td  class="stepDesTd">
										<span><emp:message key="znyq_ywgl_xhywgl_bzms_mh" defVal="步骤描述：" fileName="znyq"></emp:message></span>
									</td>
									<td>
										<input type="text" name="comments" id="comments" />
									</td>
								</tr>
								<tr>
									<td >
										<span><emp:message key="znyq_ywgl_xhywgl_bzlx_mh" defVal="步骤类型：" fileName="znyq"></emp:message></span>
									</td>
									<td>
										<select name="prType" id="select">
											<option value="">
												<emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message>
											</option>
											<option value="4">
												Select
											</option>
											<option value="5">
												Reply
											</option>
										</select><font class="tipColor">&nbsp;*</font>
									</td>
								</tr>
								<tr>
									<td >
										<span><emp:message key="znyq_ywgl_xhywgl_bzsfzhclbz_mh" defVal="步骤是否最后处理步骤：" fileName="znyq"></emp:message></span>
									</td>
									<td>
										<input name="finalState" type="radio" value="1" id="yes" />
										<em id="y"><emp:message key="znyq_ywgl_xhywgl_s" defVal="是" fileName="znyq"></emp:message></em>
										<input name="finalState" type="radio" value="0" id="no"
											checked="checked" />
										<em id="n"><emp:message key="znyq_ywgl_xhywgl_f" defVal="否" fileName="znyq"></emp:message></em>
									</td>
								</tr>
								<tr>
									<td colspan="2" id="btn">
									<%
										if(service.getUserId()-curSysuser.getUserId()==0 || service.getOwnerId()-curSysuser.getUserId()==0)
										{
									%>
										<input id="addBasicInfoBtn" type="button" value="<emp:message key="znyq_ywgl_xhywgl_q_d" defVal="确 定" fileName="znyq"></emp:message>" class="btnClass1"/>
									<%
										}
									%>
										<input type="button" name="previous" id="previous" value="<emp:message key="znyq_ywgl_common_btn_10" defVal="返回" fileName="znyq"></emp:message>"
											onclick="javascript:location.href='eng_mtProcess.htm?serId=<%=serId %>&lguserid='+$('#lguserid').val()" class="btnClass1"/>
									</td>
								</tr>
								</thead>
							</table>
						</form>
					</div></center>
			</div>
			<%-- 这是每个界面相应的DIV --%>
			<div class="clear"></div>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
	</body>
</html>