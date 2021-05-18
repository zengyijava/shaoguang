<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="com.montnets.emp.entity.pasroute.LfSpDepBind" %>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@page import="com.montnets.emp.entity.datasource.LfDBConnect"%>
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
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mtService");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	String serId = (String)request.getAttribute("serId");

	LfProcess lp = (LfProcess)request.getAttribute("process");
	if (lp == null)
	{
		lp = new LfProcess();
	}
	
	List<LfDBConnect> dbList = null;
	if(request.getAttribute("dbList") != null)
	{
		
		dbList=(List<LfDBConnect>)request.getAttribute("dbList");
	}
	else
	{
		dbList = new ArrayList<LfDBConnect>();
	}

	List<LfTemplate> tmpList = null;
	if(request.getAttribute("tmpList") != null)
	{
		tmpList=(List<LfTemplate>)request.getAttribute("tmpList");
	}
	else
	{
		tmpList = new ArrayList<LfTemplate>();
	}
	
	String repPrId = request.getAttribute("repPrId")==null?"":(String)request.getAttribute("repPrId");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
				
		
		
		<style type="text/css">
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			table#addseltable {
			    width: 690px;
			}
		<%}%>	
		
		</style>
		
	</head>
	<body id="eng_addMt2" class="eng_addMt2">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode,"步骤管理") %>--%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_bzgl",request)) %>
			
			
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="eng_mtProcess.htm" method="post" id="proform">
			
				<input type="hidden" id="prId" name="prId" value='<%=lp.getPrId()==null?"":lp.getPrId() %>' />
				<input type="hidden" id="serId" name="serId" value='<%=serId%>' />
				<input type="hidden" id="repPrId" name="repPrId" value="<%=repPrId %>"/>
				<input type="hidden" id="finalState" name="finalState" value="0"/><%-- select步骤默认设为不是最终步骤 --%>
				
				<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
				<div class="linediv">
					<table class="linebgimg2">
						<tr>
						<td class="linebgth1"><b>1.</b><emp:message key="znyq_ywgl_xhywgl_xjyw" defVal="新建业务" fileName="znyq"></emp:message></td>
						<td class="linebgth2"><b>2.</b><emp:message key="znyq_ywgl_xhywgl_xjsleletbz" defVal="新建select步骤" fileName="znyq"></emp:message></td>
						<td class="linebgth3"><b>3.</b><emp:message key="znyq_ywgl_xhywgl_xjreplybz" defVal="新建reply步骤" fileName="znyq"></emp:message></td>
						<td class="linebgth4"><b>4.</b><emp:message key="znyq_ywgl_xhywgl_szfssj" defVal="设置发送时间" fileName="znyq"></emp:message></td>
						<td class="linebgth5"><b>5.</b><emp:message key="znyq_ywgl_xhywgl_wc" defVal="完成" fileName="znyq"></emp:message></td>
						</tr>
					</table>
				</div>
				<div class="itemMtDiv" id="item1">
				<span id="spanPrName"  class="righttitle"><emp:message key="znyq_ywgl_xhywgl_bzmc_mh" defVal="步骤名称：" fileName="znyq"></emp:message></span>
				<table>
				<tr>
				<td class="input_bd">
				<input type="hidden" id="hprName" name="hprName" value='<%=lp.getPrName()==null?"":lp.getPrName() %>' />
				<input type="text" name="prName" id="prName" value='<%=lp.getPrName()==null?"":lp.getPrName() %>' maxlength="20"/>
				</td><td class="zeroBorder"><font class="tipColor">&nbsp;*</font></td></tr></table>
				</div>
				
				<div class="itemMtDiv" id="item2">
				<span class="righttitle"><emp:message key="znyq_ywgl_xhywgl_bzms_mh" defVal="步骤描述：" fileName="znyq"></emp:message></span>
				<table><tr><td class="input_bd">
				<input type="text" name="comments" id="comments" value='<%=lp.getComments()==null?"":lp.getComments() %>' maxlength="32"/>
				</td></tr></table>
				</div>
				
				<div class="itemMtDiv">
				<span id="spanPrType" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_bzlx_mh" defVal="步骤类型：" fileName="znyq"></emp:message></span>
				<table class="ownerTable"><tr><td class="zeroBorder">
				<input type="hidden" name="prType" value="4" />
				&nbsp;&nbsp;Select
				</td></tr></table>
				</div>
				
				<div class="addselDiv">
				<table id="addseltable" class="div_bg">
					<thead>
					<tr>
					
						<%if(StaticValue.ZH_HK.equals(langName)){%>
								<td class="ZH_HKtd">
						<%}else{%>
								<td class="defaultTd">
						<%}%>
							<span id="spanDBL" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_sjklj_mh" defVal="数据库连接：" fileName="znyq"></emp:message></span>
							<input type="hidden" name="hidOpType" value="editSql"/>
						</td>
						<td class="firstTd">
						<div class="input_bd">
							<label>
							<select name="dbId" id="dbId" class="zeroBorder">
							<option class="slvColor"value=""><emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message></option>
							<%
								for(int i=0;i<dbList.size();i++)
								{
									LfDBConnect dbConn=dbList.get(i);
							%>
								<option value="<%=dbConn.getDbId() %>"
									<%
										if(lp.getDbId()!=null && lp.getDbId()-dbConn.getDbId()==0)
										{
											out.print("selected=\"selected\"");
										}
									%>>
									<%=dbConn==null?"":(dbConn.getDbconName()==null?"":dbConn.getDbconName().replace("<","&lt;").replace(">","&gt;")) %>
								</option>
							<%
								}
							%>
							</select>
							</label>
						</div>
							<font class="tipColor">&nbsp;*</font>
						</td>
						<% if(btnMap.get(titleMap.get("datasourceConf")+"-1")!=null){%>
						<td><a class="toAddDB" onclick="toAddDB()"><emp:message key="znyq_ywgl_xhywgl_xz" defVal="新增" fileName="znyq"></emp:message></a></td>
						<%} %>
					</tr>
					<tr>
						<td><span id="spanSqlTemp" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_znzqmb" defVal="智能抓取模板：" fileName="znyq"></emp:message></span></td>
						<td class="leftPad">
						<div class="input_bd">
						<label><select name="tempSel" id="tempSel" onchange="getSql(this.value)" class="zeroBorder">
							<option value="" class="slvColor"><emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message></option>
						<%
							if(tmpList != null && tmpList.size()>0)
							{
								for(LfTemplate temp : tmpList)
								{
						%>
								<option value="<%=temp.getTmid() %>" <% if(temp.getTmid().equals(lp.getTemplateId())){ %>selected="selected"<%} %>><%=temp.getTmName().replace("<","&lt;").replace(">","&gt;") %>(ID:<%=temp.getTmid() %>)</option>
						<%
								}
							}
						 %>
						</select></label><%-- &nbsp;<a href="javascript:location.href=location.href">还原</a> --%>
						</div>
						</td>
						<%if(btnMap.get(titleMap.get("smsTemplate")+"-1")!=null) { %>
						<td><a class="showAddSMS" onclick="showAddSmsTmp(2)"><emp:message key="znyq_ywgl_xhywgl_xz" defVal="新增" fileName="znyq"></emp:message></a></td>
						<%} %>
					</tr>
					<tr>
						<td class="spanSqlTd">
							<span id="spanSql" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_sqlyj_mh" defVal="SQL语句：" fileName="znyq"></emp:message></span>
						</td>
						<td class="spanSqlNextTd">
						<div class="input_bd" >
							<textarea class="textarea_limit" name="sql" id="sql" ><%
								if(lp.getSql()!=null)
								{
									out.print(lp.getSql());
								}
							%></textarea>
						</div>
							<font class="tipColor">&nbsp;*</font>
						</td>
					</tr>
					</thead>
				</table>
				</div>
				
				<div class="nextStepDiv">
				<input type="button" id="btnNextstup"  value="<emp:message key="znyq_ywgl_common_btn_17" defVal="下一步" fileName="znyq"></emp:message>" class="btnClass5 mr23 indent_none" onclick="checkSubBefore()" />
				<input type="button"  value="<emp:message key="znyq_ywgl_common_btn_23" defVal="上一步" fileName="znyq"></emp:message>" class="btnClass6 indent_none" onclick="goLast()" />
				<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
				<br/>
				</div>
			</form>
			
			</div>
			<div id="addFrame" title="<emp:message key="znyq_ywgl_xhywgl_xjsjy" defVal="新建数据源" fileName="znyq"></emp:message>">
			<center>
				<iframe id="addDataSource" name="addDataSource" marginwidth="0" scrolling="no" frameborder="no"></iframe>
			</center>
			</div>
			<div id="addSmsTmpDiv" title="<emp:message key="znyq_ywgl_xhywgl_mb" defVal="模板" fileName="znyq"></emp:message>">
			<iframe id="addSmsTmpFrame" name="addSmsTmpFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
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
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/textarea_limit.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			//show();
		});
		function goLast()
		{
			var lguserid =$('#lguserid').val();
			var lgcorpcode=$('#lgcorpcode').val();
			location.href="<%=path%>/eng_mtService.htm?method=toAdd&serId=<%=serId %>&prId=<%=lp.getPrId()==null?"":lp.getPrId() %>&repPrId=<%=repPrId %>";
		}

	    //获取sql模板内容
		function getSql(sqlId) {
			var $sqlContent = $("form[name='proform']").find("textarea[name='sql']");
			if(sqlId != "")
			{
				$.post("tem_smsTemplate.htm",{method:"getTmMsg",tmId:sqlId},function(msg)
					{
						$("#sql").val(msg);
					}
				);
			}else
			{
				$("#sql").val("");
			}
		}
		
	    //表单验证
		function checkSubBefore()
	    {  
			var prName = $("#prName").val();         //步骤名称
			//var prType = $("#prType").val();
			prName = prName.replace(/\s+/g,"");         // 清除所有空格
			if(prName == "") {      //检查步骤名称
				//alert("步骤名称不能为空，请输入步骤名称");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcbnwk"));
				return false;
			}
			if(!prName.match( /^[\u4E00-\u9FA5a-zA-Z0-9_]+$/))
			{
				//alert("步骤名称只能由汉字、英文字母、数字、下划线组成");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcznyhz"));
				return false;
			}
			var dbId = $("#dbId").val();
			if (dbId == "" || dbId == null) 
			{
				//alert("数据库连接不能为空");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjkljbnwk"));
				return false;
			}
			var sql = $("#sql").val();
			if (sql == "" || sql == null) 
			{
				//alert("SQL语句不能为空");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sqlyjbnwk"));
				return false;
			}

	   	    $("#btnNextstup").attr("disabled","disabled");
	
			$('#proform').attr("action",$('#proform').attr('action')+'?method=addSelect&serId=<%=serId %>'+'&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val());
					
			$("#proform").submit();
			        	  
		}
	    
	     //获取新模板
		function getTmplInfo() {
			
			var lguserid=$("#lguserid").val();
			$.post("eng_mtProcess.htm?method=getSmsTmpl&dsflag=2&lguserid="+lguserid,function(msg)
					{
						if(msg== "" || msg=="errtmpl"){
							//alert("获取模板信息失败。");
							alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_hqmbxxsb"));
							return;
						}
						if(msg=="nodata"){
							return;
						}
						msg = eval("("+msg+")");
						//var selStr = '<option value="" style="color:#666666">请选择</option>';
						var selStr = '<option value="" class="slvColor">'+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qxz")+'</option>';
						$.each(msg, function(idx,item){
							selStr += '<option value="'+item.tmid+'" >'+item.tmname+'</option>';
						});
						$("#tempSel").find("option").remove();
						$("#tempSel").append(selStr);
					}
			);
		}
    
	</script>
		
	</body>
</html>