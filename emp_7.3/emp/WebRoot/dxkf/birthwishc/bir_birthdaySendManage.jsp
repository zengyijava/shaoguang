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
	String updateResultClient="";
	if(request.getAttribute("updateResultClient")!=null)
	{
		updateResultClient = (String)request.getAttribute("updateResultClient");
	}
	LfBirthdaySetupVo vo = (LfBirthdaySetupVo)request.getAttribute("vo");
	
	LfSysuser curSysuser = (LfSysuser)request.getAttribute("curSysuser");
	String lguserid = String.valueOf(curSysuser.getUserId());
	String lgcorpcode = curSysuser.getCorpCode();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@ SuppressWarnings("unchecked")
	List<LfSpDepBind> smsBindSp = (List<LfSpDepBind>)request.getAttribute("smsBindSp");	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/bir_birthdaySendManage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_KeHuShengRiZhuFu.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>

	<body id="birthdaySendManage">
	<%
		if(CstlyeSkin.contains("frame4.0")){
	%>
		<input id='hasBeenBind' value='1' type='hidden'/>
	<%
		}
	%>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="bir_birthdaySendClient.htm?method=find" method="post" id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">
					
					
						</div>
						<a id="add" onclick="window.location.href='bir_birthdaySendClient.htm?method=toUpdate&setupid=-1&lguserid='+'<%=lguserid%>'+'&lgcorpcode='+'<%=lgcorpcode%>'"><emp:message key="dxkf_ydkf_srzf_text_new" defVal="新建" fileName="dxkf"></emp:message></a>
					</div>
					<div class="dxkf_display_none" id="hiddenValueDiv"></div>
					<div id="condition">
						<table>
						   
							<tbody>
								<tr>
									<td class="dxkf_td2">
										<emp:message key="dxkf_ydkf_srzf_text_czyname" defVal="操作员名称" fileName="dxkf"></emp:message>：
									</td>
									<td>	
										<input type="text" size="20" name="username" id="username" value='<%=vo.getUsername()==null?"":vo.getUsername() %>'> 	
									</td>
									<td>
										<emp:message key="dxkf_ydkf_srzf_text_taskname" defVal="任务主题" fileName="dxkf"></emp:message>：
									</td>
									<td>	
										<input type="text" size="20" name="title"  id="title" value='<%=vo.getTitle()==null?"":vo.getTitle() %>'> 	
									</td>
									<td class="tdSer">
										<center><a id="search"></a></center>
									</td>
									</tr>
									<tr>
									<td>
										<emp:message key="dxkf_ydkf_srzf_text_spaccount" defVal="SP账号" fileName="dxkf"></emp:message>：
									</td>
									<td>	
										<select id="spUser" name="spUser">
										<option value=""><emp:message key="dxkf_common_text_all" defVal="全部" fileName="dxkf"></emp:message></option>
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
									<td><emp:message key="dxkf_ydkf_srzf_text_status" defVal="状态" fileName="dxkf"></emp:message>：</td>
									  
									<td>	
  		                                 	<label>
											<select name="status"  class="dxkf_status">
												<option value="">
													<emp:message key="dxkf_common_text_all" defVal="全部" fileName="dxkf"></emp:message>
												</option>
												
												<option value="1" <%=(vo.getIsUse()!=null && vo.getIsUse()==1)?"selected":"" %>>
													<emp:message key="dxkf_common_opt_qiyong" defVal="启用" fileName="dxkf"></emp:message>
												</option>
												<option value="2" <%=(vo.getIsUse()!=null && vo.getIsUse()==2)?"selected":"" %>>
													<emp:message key="dxkf_common_opt_tingyong" defVal="停用" fileName="dxkf"></emp:message>
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
									<emp:message key="dxkf_ydkf_srzf_text_numbering" defVal="编号" fileName="dxkf"></emp:message>
								</th>
								<th>
									<emp:message key="dxkf_ydkf_srzf_text_taskname" defVal="任务主题" fileName="dxkf"></emp:message>
								</th>
								<th width="10%">
									<emp:message key="dxkf_ydkf_srzf_text_czyname" defVal="操作员名称" fileName="dxkf"></emp:message>
								</th>
								<th width="20%">
									<emp:message key="dxkf_ydkf_srzf_text_org" defVal="机构" fileName="dxkf"></emp:message>
								</th>
								<th>
									<emp:message key="dxkf_ydkf_srzf_text_spaccount" defVal="SP账号" fileName="dxkf"></emp:message>
								</th>
								<th>
									<emp:message key="dxkf_ydkf_srzf_text_wish" defVal="祝福语" fileName="dxkf"></emp:message>
								</th>
								<th>
									<emp:message key="dxkf_ydkf_srzf_text_status" defVal="状态" fileName="dxkf"></emp:message>
								</th>
								<th colspan="3">
									<emp:message key="dxkf_ydkf_srzf_text_option" defVal="操作" fileName="dxkf"></emp:message>
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
							<td class="text-center">
								<xmp><%=set.getTitle()==null?"":set.getTitle() %></xmp>
							</td>
							<td class="text-center">
							<%=set.getUsername() %>
							</td>
							<td class="text-center">
							<%=set.getDepname()%>
							</td>
							<td>
							<%=set.getSpUser()%>
							</td>
							<td class="text-center" width="20%">
							<%
							String content1 = (set.getAddName()==null?"":set.getAddName())+set.getMsg();
							content1 =(set.getIsSignName()==2?"":set.getSignName())+content1;
							String content=content1.length()>20?content1.substring(0,20)+"...":content1;
							%>
							
								<a onclick="javascript:modifyNew(this)">
<%--								 <label class="dxkf_display_none"><xmp><%//content1.replace("#P_1#","XXX")%></xmp></label>--%>
									 <textarea class="dxkf_display_none"><%=content1.replace("#P_1#","XXX")%></textarea>
									 <xmp name="msgXmp" style="display:none"><%=content1.replace("#P_1#","XXX")%></xmp>
								<xmp><%=content.replace("#P_1#","XXX") %></xmp></a>	
							
							</td>
							
							<td>
							<center>
							<%if(set.getIsUse()==1){
							%>
								<select  name="runState<%=set.getId() %>" id="runState<%=set.getId() %>" class="input_bd" onchange="javascript:changestate('<%=set.getId()%>')">
						          <option value="1" selected="selected"><emp:message key="dxkf_common_opt_yiqiyong" defVal="已启用" fileName="dxkf"></emp:message></option>
						          <option value="2" ><emp:message key="dxkf_common_opt_tingyong" defVal="停用" fileName="dxkf"></emp:message></option>
						        </select>
							<%
							}else{
							%>
								<select  name="runState<%=set.getId() %>" id="runState<%=set.getId() %>" class="input_bd" onchange="javascript:changestate('<%=set.getId() %>')">
						          <option value="2" selected="selected"><emp:message key="dxkf_common_opt_yitingyong" defVal="已停用" fileName="dxkf"></emp:message></option>
						          <option value="1" ><emp:message key="dxkf_common_opt_qiyong" defVal="启用" fileName="dxkf"></emp:message></option>									          
						        </select>
							<%	
							}
							%>
							</center>
							</td>
						
							<td>
							<a onclick="deleteSetup1('<%=set.getIdCipher() %>','<%=set.getType() %>')" id="deleteSetup"><emp:message key="dxkf_common_opt_shanchu" defVal="删除" fileName="dxkf"></emp:message></a>
							</td>
							<td>
							<%
							if(curSysuser.getUserId()-0==set.getUserId())
							{
							%>
								<a onclick="window.location.href='bir_birthdaySendClient.htm?method=toUpdate&userid='+'<%=set.getUserId() %>'+'&setupid='+'<%=set.getId() %>'+'&setupidChpher='+'<%=set.getIdCipher()%>'+'&lguserid='+'<%=lguserid%>'+'&lgcorpcode='+'<%=lgcorpcode%>'"><emp:message key="dxkf_common_opt_xiugai" defVal="修改" fileName="dxkf"></emp:message></a>
							<%
							}
							else
							{
							%>
								<a onclick="window.location.href='bir_birthdaySendClient.htm?method=toUpdate&userid='+'<%=set.getUserId() %>'+'&setupid='+'<%=set.getId() %>'+'&setupidChpher='+'<%=set.getIdCipher()%>'+'&lguserid='+'<%=lguserid%>'+'&lgcorpcode='+'<%=lgcorpcode%>'"><emp:message key="dxkf_common_opt_chakan" defVal="查看" fileName="dxkf"></emp:message></a>
							<%
							}
							%>
							</td>
						</tr>
						<%
							}
							}else{
						%>
						<tr><td align="center" colspan="10"><emp:message key="dxkf_common_text_nodata" defVal="无记录" fileName="dxkf"></emp:message></td></tr>
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
			<div id="modify" title="<emp:message key="dxkf_ydkf_srzf_text_wish" defVal="祝福语" fileName="dxkf"></emp:message>">
				<table>
					<thead>
						<tr class="dxkf_tr1">
							<td class="dxkf_word_break">
<%--								<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>--%>
								    <span><textarea id="msgcont" rows="15" readonly="readonly"></textarea></span>
							</td>
							 
						</tr>
					   <tr class="dxkf_tr2">
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
    
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/dxkf_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/birManage.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <%-- <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script> --%>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
			});
			$("#content select").empSelect({width:80});
			$('#content select').each(function(){
				$(this).next().hide();
				$(this).before('<div class="selectBefore">'+$(this).find('option:selected').text()+'</div>');
			});
			noquot("#username");
			noquot("#title");
			if("<%=updateResultClient%>"=="true")
			{
				//alert("设置成功");
				alert(getJsLocaleMessage('dxkf','dxkf_srzf_page4_setsuccess'));
				reloadPage();
			}
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

           // $('#spUser').isSearchSelect({'width':'177','isInput':true,'zindex':0});
		});
    
        function reloadPage()
       	{
           	window.location.href = "<%=path%>/bir_birthdaySendClient.htm?method=find&isback=1&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
       	}
       
		</script>
	</body>
</html>
