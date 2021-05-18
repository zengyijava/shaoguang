<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.entity.passage.XtGateQueue"%>
<%@page import="com.montnets.emp.entity.sms.MtTaskC"%>
<%@page import="com.montnets.emp.query.vo.SysMoMtSpgateVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	boolean isFirstEnter = (Boolean)request.getAttribute("isFirstEnter");
	
	@ SuppressWarnings("unchecked")
	List<String> userList = (List<String>)request.getAttribute("sendUserList");
	
	String spgate = request.getParameter("spgate");
	String spUser = request.getParameter("spUser");
	String phone = request.getParameter("phone");
	String startTime = request.getParameter("sendtime");
	String endTime = request.getParameter("recvtime");
	String msg = request.getParameter("msg");
	String staskid = request.getParameter("taskid");
	
	@ SuppressWarnings("unchecked")
	List<SysMoMtSpgateVo> spList = (List<SysMoMtSpgateVo>) request.getAttribute("spList");
	
	@ SuppressWarnings("unchecked")
	List<MtTaskC> taskCList = (List<MtTaskC>) request.getAttribute("taskCList");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("remainMtRecord");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	MessageUtils messageUtils = new MessageUtils();
	//发送账号 
    String fszh = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_fszh", request);
    if(fszh!=null&&fszh.length()>1){
    	fszh = fszh.substring(0,fszh.length()-1);
    }
	//通道号码
	String tdhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_tdhm", request);
    if(tdhm!=null&&tdhm.length()>1){
    	tdhm = tdhm.substring(0,tdhm.length()-1);
    }
	//手机号码 
	String sjhm = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_sjhm", request);
    if(sjhm!=null&&sjhm.length()>1){
    	sjhm = sjhm.substring(0,sjhm.length()-1);
    }
	//任务批次
	String rwpc = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_rwpc", request);
    if(rwpc!=null&&rwpc.length()>1){
    	rwpc = rwpc.substring(0,rwpc.length()-1);
    }
	//滞留时间
	String zlsj = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_zlsj", request);

	//滞留原因
	String zlyy = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_zlyy", request);

	//短信内容 
	String dxnr = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_dxnr", request);
    if(dxnr!=null&&dxnr.length()>1){
    	dxnr = dxnr.substring(0,dxnr.length()-1);
    }
	//操作
	String cz = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_cz", request);
    //欠费滞留
    String qfzl = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_qfzl", request);
    //其他原因
    String qtyy = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_qtyy", request);
    //重新发送当前查询的所有记录
    String cxfsdqcxsyjl = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_cxfsdqcxsyjl", request);
    //删除当前查询的所有记录
     String scdqcxsyjl = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_scdqcxsyjl", request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	    <link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/cxtj/common/css/inbox.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui//jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 	    <script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 	    <script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			 getLoginInfo("#loginpa");
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
				$('#searchIcon').attr('src', '<%=inheritPath %>/images/toggle_expand.png');
				//$('#searchIcon').attr('title', '收缩查询条件');
				$('#searchIcon').attr('title', getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_1"));
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
				$('#searchIcon').attr('src', '<%=inheritPath %>/images/toggle_collapse.png');
				//$('#searchIcon').attr('title', '展开查询条件');
				$('#searchIcon').attr('title', getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_2"));
			});
			$('#content tbody tr').hover(function() {
				$(this).css('background-color', '#c1ebff');
			}, function() {
				$(this).css('background-color', '#FFFFFF');
			});			
			
			$("#msg").live('keyup blur',function(){
					var value=$(this).val();
					if(value!=filterString(value)){
						$(this).val(filterString(value));
					}
			});
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
            $('#spUser,#spgate').isSearchSelect({'width':'179','isInput':true,'zindex':0});
			$('#search').click(function(){submitForm();});
		});
		function modify(t)
		{
			$('#modify').dialog({
				autoOpen: false,
				width:250,
			    height:200
			});
			$("#msgss").empty();
			$("#msgss").text($(t).children("label").children("xmp").text());
			$('#modify').dialog('open');
		}

		function numberControl(va)
		{
			var pat=/^\d*$/;
			if(!pat.test(va.val()))
			{
				va.val(va.val().replace(/[^\d]/g,''));
			}
		}
		//checkbox全选
		function checkAlls(e,str)    
		{  
			var a = document.getElementsByName(str);    
			var n = a.length;    
			for (var i=0; i<n; i=i+1)    
				a[i].checked =e.checked;    
		}
		//发送起止时间控制
		function rtime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#sendtime").attr("value");
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
				    case "01":day = 31;break;
				    case "02":day = 28;break;
				    case "03":day = 31;break;
				    case "04":day = 30;break;
				    case "05":day = 31;break;
				    case "06":day = 30;break;
				    case "07":day = 31;break;
				    case "08":day = 31;break;
				    case "09":day = 30;break;
				    case "10":day = 31;break;
				    case "11":day = 30;break;
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
		    var v = $("#recvtime").attr("value");
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
		function doSel(doType)
		{
			doSel(doType,null);
		}
		function doSel(doType,id)
		{
			var selected=document.getElementsByName("mcid");
			var count='<%=pageInfo.getTotalRec()%>';
			var n=0;		//统计勾选中的个数
			if(id == "" )
			{
				for(i=0;i<selected.length;i=i+1){
					if(selected[i].checked==true){
						id=id+selected[i].value;
						id=id+","
						n=n+1;
					}
				}
				//if(n<1){alert("请选择一条或多条记录进行操作！");return;}
				if(n<1){alert(getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_3"));return;}
				id=id.substring(0,id.lastIndexOf(','));
			}
			var alertStr = "";
			var met = "";
			var recordCount = <%=pageInfo.getTotalRec()%>;
			if(doType ==1 )
			{
				//alertStr = "您确定要重新发送当前选中的记录？";
				alertStr = getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_4");
				met = "send";
			}else if(doType == 2)
			{
				//alertStr = "您确定要删除当前选中的记录？";
				alertStr = getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_5");
				met = "del";
			}else if (doType == 3)
			{
				alertStr = getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_6");
				
				if(recordCount - 0 <= 0)
				{
					//alert("当前查询记录为空！");
					alert(getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_7"));
					return;
				}
				met = "send";
			}else if(doType == 4)
			{
				//alertStr = "您确定要删除当前查询的所有记录？"
				alertStr = getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_8");
				if(recordCount - 0 <= 0)
				{
					//alert("当前查询记录为空！");
					alert(getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_7"));
					return;
				}
				met = "del";
			}
			if(confirm(alertStr))
			{
				$.post("rep_remainMtRecord.htm",{method : "update",optype: met,ids: id,lguserid:$("#lguserid").val(),count:count,isAsync : "yes"}
				,function(result){
					if(result == "outOfLogin")
					{
						location.href="<%=path%>/common/logoutEmp.html";
						return;
					}
					var noFeeSpUser = "";
					if(result == "nocount"){
						//无符合发送条件的记录
						//alert("无符合重发条件的记录，请检查滞留记录是否已超过24小时，且运营商余额是否充足。");
						alert(getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_9"));
						//submitForm();
						return;
					}
					
					if(result.indexOf("&") > 0){
						//有&，表明带了余额不足而不发送的账号
						noFeeSpUser = result.subString(result.indexOf("&"));
						result = result.subString(0, result.indexOf("&"));
					}
					
					if(result == "true" && noFeeSpUser.length > 0)
					{
						//alert("操作成功！但发送账号"+noFeeSpUser+"的记录因运营商余额不足而未发送。");
						alert(getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_10")+noFeeSpUser+getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_11"));
						submitForm();
					}
					else if(result == "true")
					{
						//alert("操作成功！");
						alert(getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_12"));
						submitForm();
						
					}else
					{
						//alert("该条数据已删除或已重发失败！");
						alert(getJsLocaleMessage("cxtj","cxtj_sjcx_dxzljl_text_13"));
						submitForm();
					}
				});
			}<%--else if(doType==2 && confirm("确认删除选中的记录吗？"))
			{
				$.post("rep_remainMtRecord.htm",{method : "update",optype: "del",ids : id,isAsync : "yes"}
				,function(result){
					if(result == "outOfLogin")
					{
						location.href="<%=path%>/common/logoutEmp.html";
						return;
					}
					if(result == "true")
					{
						alert("操作成功！");
					}else
					{
						alert("操作失败！");
					}
				});
			}--%>
		}
		</script>
	</head>
	<body class="rep_remainMtRecord">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="rep_remainMtRecord.htm" method="post" id="pageForm">
				<div style="display:none" id="loginpa"></div>
						<div class="buttons">
							<div id="toggleDiv" >
							</div>
							<a href="javascript:doSel(1,'')" id="addcli" ><emp:message key="cxtj_sjcx_dxzljl_cfxz" defVal="重发选中" fileName="cxtj"></emp:message></a>
							<a href="javascript:doSel(2,'')" id="delcli" ><emp:message key="cxtj_sjcx_dxzljl_scxz" defVal="删除选中" fileName="cxtj"></emp:message></a>
							<a href="javascript:doSel(3)" id="addcli" title=<%=cxfsdqcxsyjl %>><emp:message key="cxtj_sjcx_dxzljl_cfsy" defVal="重发所有" fileName="cxtj"></emp:message></a>
							<a href="javascript:doSel(4)" id="delcli" title=<%=scdqcxsyjl %>><emp:message key="cxtj_sjcx_dxzljl_scsy" defVal="删除所有" fileName="cxtj"></emp:message></a>
						</div>
						
						<div id="condition" >
						 <table>
								<tbody>
								<tr>
								<td>
										<emp:message key="cxtj_sjcx_dxzljl_rwpc" defVal="任务批次：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text" value='<%=staskid==null?"":staskid %>' id="taskid" name="taskid" onkeyup="javascript:numberControl($(this))" maxlength="16"/>
									</td>
									<td>
										<emp:message key="cxtj_sjcx_dxzljl_fszh" defVal="发送账号：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<label>
											<select id="spUser" name="spUser">
											<option value="">
													<emp:message key="cxtj_sjcx_dxzljl_qb" defVal="全部" fileName="cxtj"></emp:message>
												</option>
											<%
											if (userList != null && userList.size() > 0)
											{
												for(String userdata : userList)
												{
											%>
													<option value="<%=userdata %>"
														<%=userdata.equals(spUser)?"selected":"" %>>
														<%=userdata %>
													</option>
													<%}
											}													
											%>
											</select>
										</label>
									</td>
									<td>
										<emp:message key="cxtj_sjcx_dxzljl_tdhm" defVal="通道号码：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<label>
											<select name="spgate" id="spgate">
												<option value="">
													<emp:message key="cxtj_sjcx_dxzljl_qb" defVal="全部" fileName="cxtj"></emp:message>
												</option>
													<%
														if (spList != null && spList.size() > 0)
														{
															for(int i=0;i<spList.size();i++)
															{
													%>
												<option value="<%=spList.get(i).getSpgate()%>"
													<%=spList.get(i).getSpgate().equals(spgate)?"selected":""%>>
													<%=spList.get(i).getSpgate() %>
												</option>
											<%		}
												}
											%>
											</select>
										</label>
									</td>
									
									<td class="tdSer">
										<center><a id="search"></a></center>
									</td>
								</tr>
								<tr>
								<td>
										<emp:message key="cxtj_sjcx_dxzljl_sjhm" defVal="手机号码：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text" value='<%=phone==null?"":phone %>' id="phone" name="phone" maxlength="21" onkeyup="javascript:phoneInputCtrl($(this))"/>
									</td>
									
									<td><emp:message key="cxtj_sjcx_dxzljl_dxnr" defVal="短信内容：" fileName="cxtj"></emp:message></td>
									<td>
										<input type="text" value='<%=msg==null?"":msg %>' id="msg" name="msg" />
									</td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td>
										<emp:message key="cxtj_sjcx_dxzljl_zlsjd" defVal="滞留时间段：" fileName="cxtj"></emp:message>
									</td>
									<td class="tableTime">

										<input type="text" class="Wdate" readonly="readonly" onclick="stime()"
											 value="<%=startTime==null?"":startTime %>" id="sendtime" name="sendtime">
									</td>
									<td>
										<emp:message key="cxtj_sjcx_dxzljl_z" defVal="至：" fileName="cxtj"></emp:message>
									</td>
									<td>
										<input type="text" class="Wdate" readonly="readonly" onclick="rtime()"
											 value="<%=endTime==null?"":endTime %>" id="recvtime" name="recvtime">
									</td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
							</table>
						</div>
						<table id="content">
							<thead>
							    <tr>
							    	 <th>
										<input type="checkbox" name="dels" value=""
											onclick="checkAlls(this,'mcid')" />
									</th>
						            <th><%=fszh %> </th>
						            <th> <%=tdhm %> </th>
						            <%-- <th>运营商</th> --%>
						            <th><%=sjhm %>  </th>
						            <th><%=rwpc %> </th>
						            <th><%=zlsj %> </th>
						            <th><%=zlyy %> </th>
						            <th><%=dxnr%> </th>
						            <th colspan="2"><%=cz %> </th>
						        </tr>
							</thead>
							<tbody>
							<%if(isFirstEnter){ %>
							 	<tr><td colspan="11" align="center"><emp:message key="cxtj_sjcx_dxzljl_qdjcxhqsj" defVal="请点击查询获取数据" fileName="cxtj"></emp:message></td></tr>
							<%} else 
							{
								if(taskCList!=null && taskCList.size()>0){
									Calendar curTime = Calendar.getInstance();
									//距离当前时间24小时前的时间点
									curTime.add(Calendar.HOUR, -StaticValue.MTTASKC_OVERDUE);
									
									for(int i=0;i<taskCList.size();i++)
									{
										MtTaskC mtTask=taskCList.get(i);
							%>
								<tr>
									<td>
										<input type="checkbox" name="mcid" value="<%=mtTask.getId() %>" />
									</td>
									<td>
										<%=mtTask.getUserId() %>
									</td>
									<td>
										<%=mtTask.getSpgate() %><%=mtTask.getCpno() %>
									</td>
									<%-- <td>
										<%
										if(mtTask.getUnicom()-0==0){%>
										<emp:message key="cxtj_sjcx_dxzljl_yd" defVal="移动" fileName="cxtj"></emp:message>
										<%}else if(mtTask.getUnicom()-1==0){%>
										<emp:message key="cxtj_sjcx_dxzljl_lt" defVal="联通" fileName="cxtj"></emp:message>
										<%}else if(mtTask.getUnicom()-21==0){ %>
										<emp:message key="cxtj_sjcx_dxzljl_dx" defVal="电信" fileName="cxtj"></emp:message>
										<%} %>
									</td>--%>
									<td>
										<%=mtTask.getShouji() %>
									</td>
									<td>
									<%
									Long taskid=mtTask.getTaskId()!=null?mtTask.getTaskId():0l;
									if(taskid-0!=0){
										out.print(taskid+"");
									}else{
										out.print("--");
									}
									 %>
									</td>
									<td>
										<%=df.format(mtTask.getSendTime()) %>
									</td>
									<td><%=mtTask.getSendStatus()-208==0?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_qfzl", request):MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_qtyy", request)%></td>
									<td align="left">
										<a onclick="modify(this);">
								  <label style="display:none"><xmp><%=mtTask.getMessage()%></xmp></label>
											<%=mtTask.getMessage().length()>5?mtTask.getMessage().substring(0,5)+"...":mtTask.getMessage() %>
								  </a> 
									</td>
									<td>
									<%
										if(mtTask.getSendTime().getTime() > curTime.getTimeInMillis() ){
										//24小时内的才显示重发
									%>
										<a href="javascript:doSel(1,<%=mtTask.getId() %>)"><emp:message key="cxtj_sjcx_dxzljl_cf" defVal="重发" fileName="cxtj"></emp:message></a>
									<%} else {%>
									--
									<%} %>
									</td>
									<td><a href="javascript:doSel(2,<%=mtTask.getId() %>)"><emp:message key="cxtj_sjcx_dxzljl_sc" defVal="删除" fileName="cxtj"></emp:message></a></td>
								</tr>
							<%}
								}else{
							%>
							 <tr><td colspan="11" align="center"><emp:message key="cxtj_sjcx_dxzljl_wjl" defVal="无记录" fileName="cxtj"></emp:message></td></tr>
							 <%}
							} %>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="11">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>
				 </form>
			</div>
			<%}%>
			<%-- 内容结束 --%>
			       
					  
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			</div>
		 
			<%-- foot结束 --%>
			<div id="modify" title="<%=dxnr%>">
				<table>
					<thead>
						<tr class="modify_tr1">
							<td>
								<label id="msgss"></label>
							</td>
						</tr>
					   <tr class="modify_tr2">
							<td></td>
						</tr>
					</thead>
				</table>
			</div>
						 
    <div class="clear"></div>
	</body>
</html>
