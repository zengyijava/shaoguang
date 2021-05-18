<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.montnets.emp.entity.birthwish.LfBirthdaySetup"%>
<%@page import="com.montnets.emp.common.vo.LfBirthdaySetupVo"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
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
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String titlePath = (String)request.getAttribute("titlePath");
	
	String menuCode = titleMap.get(titlePath);
	menuCode = menuCode==null?"0-0-0":menuCode;
	String updateResult = "";
	if(request.getAttribute("updateResult")!=null)
	{
		updateResult = (String)request.getAttribute("updateResult");
	}
	
	@ SuppressWarnings("unchecked")
	List<LfSpDepBind> smsBindSp = (List<LfSpDepBind>)request.getAttribute("smsBindSp");	
	LfBirthdaySetupVo vo = (LfBirthdaySetupVo)request.getAttribute("vo");
	String reTitle = (String)request.getAttribute("reTitle");
	
	LfSysuser curSysuser = (LfSysuser)request.getAttribute("curSysuser");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/bir_birthdaySendManageEMP.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_YuanGongShengRiZhuFu.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>

	<body id="birthdaySendManageEMP">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<div id="rContent" class="rContent">
			<form name="pageForm" action="bir_birthdaySendEMP.htm?method=find" method="post" id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						
						<a id="add" onclick="window.location.href='bir_birthdaySendEMP.htm?method=toUpdate&setupid=-1&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val()"><emp:message key="dxzs_xtnrqf_button_15" defVal="新建" fileName="dxzs"/></a>
						<%--
						<a id="showAll" onclick="reloadPage()" >显示全部</a>
						 --%>
					</div>
					<div id="hiddenValueDiv"></div>
					<div id="condition">
						<table>
						   
							<tbody>
								<tr>
									<td>
										<emp:message key="dxzs_xtnrqf_title_98" defVal="操作员名称" fileName="dxzs"/>：
									</td>
									<td>	
										<input type="text" size="20"  class="dxzs_input1"  name="username" id="username" value='<%=vo.getUsername()==null?"":vo.getUsername() %>'> 	
									</td>
									<td>
										<emp:message key="dxzs_xtnrqf_title_90" defVal="任务主题" fileName="dxzs"/>：
									</td>
									<td>	
										<input type="text" size="20" name="title" class="dxzs_input1" id="title" value='<%=vo.getTitle()==null?"":vo.getTitle() %>'> 	
									</td>
									<td class="tdSer">
										<center><a id="search"></a></center>
									</td>
									</tr>
									<tr>
									<td>
										<emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>：
									</td>
									<td>	
										<select id="spUser" name="spUser">
										<option value=""><emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/></option>
										<%
										if (smsBindSp != null && smsBindSp.size() > 0) {
											for (LfSpDepBind spUser : smsBindSp) {
										%>
										<option value="<%=spUser.getSpUser()%>" <%=spUser.getSpUser().equals(vo.getSpUser())?"selected":""%>> 
										<%=spUser.getSpUser()%>
										</option>
										
										<%}}%>
										</select>
									</td>
									<td><emp:message key="dxzs_xtnrqf_title_103" defVal="状态" fileName="dxzs"/>：</td>
									  
									<td>	
  		                                 	<label>
											<select name="status" id="status" isInput="false">
												<option value="">
													<emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/>
												</option>
												
												<option value="1" <%=(vo.getIsUse()!=null && vo.getIsUse()==1)?"selected":"" %>>
													<emp:message key="dxzs_xtnrqf_title_96" defVal="启用" fileName="dxzs"/>
												</option>
												<option value="2" <%=(vo.getIsUse()!=null && vo.getIsUse()==2)?"selected":"" %>>
													<emp:message key="dxzs_xtnrqf_title_97" defVal="停用" fileName="dxzs"/>
												</option>
												
											</select>
										</label>		
									</td>	
									<td></td>		
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="dxzs_xtnrqf_title_101" defVal="编号" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_90" defVal="任务主题" fileName="dxzs"/>
								</th>
								<th width="10%">
									<emp:message key="dxzs_xtnrqf_title_98" defVal="操作员名称" fileName="dxzs"/>
								</th>
								<th width="20%">
									<emp:message key="dxzs_xtnrqf_title_18" defVal="机构" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_102" defVal="祝福语" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_103" defVal="状态" fileName="dxzs"/>
								</th>
								<th colspan="3">
									<emp:message key="dxzs_xtnrqf_title_6" defVal="操作" fileName="dxzs"/>
								</th>
								
							</tr>
						</thead>
						<tbody>
						<%
						@SuppressWarnings("unchecked")
						List<LfBirthdaySetupVo> setupVoList = (List<LfBirthdaySetupVo>)request.getAttribute("setupVoList");
						if(setupVoList!=null && setupVoList.size()>0)
						{
							for(LfBirthdaySetupVo set : setupVoList){
								
						%>
						<tr>
							<td>
							<%=set.getId() %>
							</td>
							<td class="textalign">
								<xmp><%=set.getTitle()==null?"":set.getTitle() %></xmp>
							</td>
							<td class="textalign">
							<%=set.getUsername() %>
							</td>
							<td class="textalign">
							<%=set.getDepname()%>
							</td>
							<td>
							<%=set.getSpUser()%>
							</td>
							<td class="textalign" width="20%">
							<%
							String content1 = (set.getAddName()==null?"":set.getAddName())+set.getMsg();
							content1 =(set.getIsSignName()==2?"":set.getSignName())+content1;
							String content=content1.length()>20?content1.substring(0,20)+"...":content1;
							%>
							
								<a onclick="javascript:modifyNew(this)">
<%--								 	 用label显示短信内容<label style="display:none"><xmp><%//content1.replace("#P_1#","XXX")%></xmp></label>--%>
								 	 <textarea class="dxzs_textarea" ><%=content1.replace("#P_1#","XXX")%></textarea>
									 <xmp name="msgXmp" style="display:none"><%=content1.replace("#P_1#","XXX")%></xmp>
								<xmp><%=content.replace("#P_1#","XXX") %></xmp></a>	
							
							</td>
							
							<td>
							<center>
							<%if(set.getIsUse()==1){
							%>
								<select  name="runState<%=set.getId() %>" id="runState<%=set.getId() %>" class="input_bd" onchange="javascript:changestate('<%=set.getId()%>')">
						          <option value="1" selected="selected"><emp:message key="dxzs_xtnrqf_title_104" defVal="已启用" fileName="dxzs"/></option>
						          <option value="2" ><emp:message key="dxzs_xtnrqf_title_97" defVal="停用" fileName="dxzs"/></option>
						        </select>
							<%
							}else{
							%>
								<select  name="runState<%=set.getId() %>" id="runState<%=set.getId() %>" class="input_bd" onchange="javascript:changestate('<%=set.getId() %>')">
						          <option value="2" selected="selected"><emp:message key="dxzs_xtnrqf_title_105" defVal="已停用" fileName="dxzs"/></option>
						          <option value="1" ><emp:message key="dxzs_xtnrqf_title_96" defVal="启用" fileName="dxzs"/></option>									          
						        </select>
							<%	
							}
							%>
							</center>
							</td>
						
							<td>
							<a onclick="deleteSetup1('<%=set.getIdCipher() %>','<%=set.getType() %>')" id="deleteSetup"><emp:message key="dxzs_xtnrqf_button_11" defVal="删除" fileName="dxzs"/></a>
							</td>
							<td>
							<%
							if(curSysuser.getUserId()-0==set.getUserId())
							{
							%>
								<a onclick="window.location.href='bir_birthdaySendEMP.htm?method=toUpdate&userid='+'<%=set.getUserId() %>'+'&setupid='+'<%=set.getId() %>'+'&setupidChpher='+'<%=set.getIdCipher()%>'+'&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val()"><emp:message key="dxzs_xtnrqf_title_107" defVal="修改" fileName="dxzs"/></a>
							<%
							}
							else
							{
							%>
								<a onclick="window.location.href='bir_birthdaySendEMP.htm?method=toUpdate&userid='+'<%=set.getUserId() %>'+'&setupid='+'<%=set.getId() %>'+'&setupidChpher='+'<%=set.getIdCipher()%>'+'&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val()"><emp:message key="dxzs_xtnrqf_title_106" defVal="查看" fileName="dxzs"/></a>
							<%
							}
							%>
							</td>
						</tr>
						<%
							}
							}else{
						%>
						<tr><td align="center" colspan="10"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="10">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="smssendparams" class="hidden"></div>
				</form>
			</div>
			<div id="modify" title="<emp:message key='dxzs_xtnrqf_title_102' defVal='祝福语' fileName='dxzs'/>" >
				<table>
					<thead>
						<tr>
							<td >
<%--								用label显示短信内容<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>--%>
								<span><textarea id="msgcont" rows="15" readonly="readonly"></textarea></span>
								 
							</td>
							 
						</tr>
					   <tr>
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
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
		</div>
    <div class="clear"></div>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widgetdatepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/birManageE.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			$("#content select").empSelect({width:80});
			$('#content select').each(function(){
				$(this).next().hide();
				$(this).before('<div class="selectBefore">'+$(this).find('option:selected').text()+'</div>');
			});
			noquot("#username");
			noquot("#title");
			if("<%=updateResult%>"=="true")
			{
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_153'));
				window.location.href = "<%=path%>/bir_birthdaySendEMP.htm?method=find&isback=1&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
			}
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
			});
			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
				$(this).find('select').next().show().siblings().hide();
				}, function() {
				$(this).removeClass("hoverColor");
				var $select = $(this).find('select');
				$select.next().hide();
				$select.prev().show();
			});
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm1();});
			$('#modify').dialog({
				autoOpen: false,
				width:300,
			    height:300
			});
            $('#spUser').isSearchSelect({'width':'177','isInput':true,'zindex':0});
		});
    
        function reloadPage()
       	{
       		window.location.href = "<%=path%>/bir_birthdaySendEMP.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
       	}
        function submitForm1(){
            var lguserid = $("#lguserid").val();
            var lgcorpcode = $("#lgcorpcode").val();
            $("#pageForm").attr("action",'<%=reTitle%>.htm?method=find&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode);
        	document.forms['pageForm'].elements["pageIndex"].value =1;	// 回到第一页
        	document.forms['pageForm'].submit();
        }

		</script>
	</body>
</html>
