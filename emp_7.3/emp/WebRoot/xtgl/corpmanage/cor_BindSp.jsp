<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.corpmanage.vo.LfSpCorpBindVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.entity.pasroute.GtPortUsed"%>
<%@page import="com.montnets.emp.entity.corp.LfCorp"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<LfSpCorpBindVo> busList = (List<LfSpCorpBindVo>) request.getAttribute("corpList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("corpSp");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<Userdata> uList = (List<Userdata>)request.getAttribute("udataList");
	LfSpCorpBindVo lbb = (LfSpCorpBindVo)request.getAttribute("lsbv");
	@ SuppressWarnings("unchecked")
	List<LfCorp> lbList = (List<LfCorp>)request.getAttribute("lbList");
	@ SuppressWarnings("unchecked")
	List<GtPortUsed> gpList = (List<GtPortUsed>)request.getAttribute("gpList");
	@ SuppressWarnings("unchecked")
	List<GtPortUsed> dbList = (List<GtPortUsed>)request.getAttribute("dbList");
	int type=3;
	//对象不为空，则进入
	if(lbb!=null){
	    type = lbb.getPlatFormType()==null?3:lbb.getPlatFormType();
	}
	
	String SMSACCOUNT = StaticValue.SMSACCOUNT;
	
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
       	
	<%if(StaticValue.ZH_HK.equals(langName)){%>	
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cor_BindSp.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	</head>
	<body id="cor_BindSp" class="cor_BindSp">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode) %>
			<form name="pageForm" action="<%=path %>/cor_corpSp.htm" method="post" id="pageForm">
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<% if(btnMap.get(menuCode+"-1")!=null) {  %>
					<a id="add"><emp:message key="xtgl_qygl_xj" defVal="新建" fileName="xtgl"></emp:message></a>
					<% } %>
				</div>
				<div id="condition">
					<table>
						<tr>
							<td>
								<span><emp:message key="xtgl_qygl_qymc_mh" defVal="企业名称：" fileName="xtgl"></emp:message></span>
							</td>
							<td>
								<label>
								<%String busName = lbb.getCorpName();%>
									<input type="text" name="cropName" id="cropName" value='<%=busName==null?"":busName %>' class="cropName" maxlength="20"/>
								</label>
							</td>
							<td>
								<span><emp:message key="xtgl_qygl_qybm_mh" defVal="企业编码：" fileName="xtgl"></emp:message></span>
							</td>
							<td>
								<label>
								<%String busCode = lbb.getCorpCode();%>
									<input type="text" name="cropCode" id="cropCode" value='<%=busCode==null?"":busCode %>' class="cropCode" maxlength="20"/>
								</label>
							</td>
							<td class="tdSer">
								<center><a id="search"></a></center>
							</td>
						</tr>
						<tr>
						<td>
								<span><emp:message key="xtgl_qygl_zhlx_mh" defVal="账户类型：" fileName="xtgl"></emp:message></span>
							</td>
							<td>
								<label>
									<select name="atype" id="atype" class="atype" onchange="changeIdt($(this).val())" isInput="false">
									<option value="3"><emp:message key="xtgl_qygl_qb" defVal="全部" fileName="xtgl"></emp:message></option>
									<option value="1" <%=type-1==0?"selected=selected":"" %>><emp:message key="xtgl_qygl_empyyzh" defVal="EMP应用账户" fileName="xtgl"></emp:message></option>
									<%
										int corpType = StaticValue.getCORPTYPE();
										int spytpeflag = StaticValue.getSPTYPEFLAG();
									%>
										<% if(corpType - 0==0 || spytpeflag-0==0){
									%>
									<option value="2" <%=type-2==0?"selected=selected":"" %>><emp:message key="xtgl_qygl_empjrzh" defVal="EMP接入账户" fileName="xtgl"></emp:message></option>
									<% }%>
								
									
									</select>
								</label>
							</td>
						<td>
								<%--<span><%=SMSACCOUNT %>：</span>
								--%><span><emp:message key="xtgl_qygl_spzh_mh" defVal="SP账号：" fileName="xtgl"></emp:message></span>
							</td>
							<td>
								<label>
								<%String accid = lbb.getSpUser(); %>
								<select name="aid3" id="aid3" class="aid3" >
								<option value=""><emp:message key="xtgl_qygl_qb" defVal="全部" fileName="xtgl"></emp:message></option>
									<%
									 if(uList != null && uList.size() > 0){
										 for (Userdata u : uList)
										 {
											 %>
											 <option value="<%=u.getUserId() %>" <%=u.getUserId().toString().equals(accid)?"selected=selected":"" %>><%=u.getUserId() %></option>
											 <%
										 }
									 }
										
									%>
									</select>
									<select name="aid1" id="aid1" class="aid1">
								<option value=""><emp:message key="xtgl_qygl_qb" defVal="全部" fileName="xtgl"></emp:message></option>
									<%
									 if(gpList != null && gpList.size() > 0){
										 for (GtPortUsed u : gpList)
										 {
											 %>
											 <option value="<%=u.getUserId() %>" <%=u.getUserId().toString().equals(accid)?"selected=selected":"" %>><%=u.getUserId() %></option>
											 <%
										 }
									 }
										
									%>
									</select>
									<select name="aid2" id="aid2" class="aid2">
								<option value=""><emp:message key="xtgl_qygl_qb" defVal="全部" fileName="xtgl"></emp:message></option>
									<%
									 if(dbList != null && dbList.size() > 0){
										 for (GtPortUsed u : dbList)
										 {
											 %>
											 <option value="<%=u.getUserId() %>" <%=u.getUserId().toString().equals(accid)?"selected=selected":"" %>><%=u.getUserId() %></option>
											 <%
										 }
									 }
										
									%>
									</select>
								</label>
							</td>
							<td></td>
						</tr>
					</table>
				</div>
				<table id="content">
					<thead>
						<tr >
							<th>
								<emp:message key="xtgl_qygl_qymc" defVal="企业名称" fileName="xtgl"></emp:message>
							</th>
							<th>
								<emp:message key="xtgl_qygl_qybm" defVal="企业编码" fileName="xtgl"></emp:message>
							</th>
							<th>
							   <%--<%=SMSACCOUNT %> --%>
							   <emp:message key="xtgl_qygl_spzh" defVal="SP账号" fileName="xtgl"></emp:message>
							</th>
							<th>
							    <emp:message key="xtgl_qygl_zhlx" defVal="账户类型" fileName="xtgl"></emp:message>
							</th>
							<th><emp:message key="xtgl_qygl_zt" defVal="状态" fileName="xtgl"></emp:message></th>
							<%if(btnMap.get(menuCode+"-3") != null || btnMap.get(menuCode+"-2") != null)  {  %>
							<th <%if(btnMap.get(menuCode+"-3") != null && btnMap.get(menuCode+"-2") != null)  {  out.print(" colspan=2");}%>>
								<emp:message key="xtgl_qygl_cz" defVal="操作" fileName="xtgl"></emp:message>
							</th>
							<%} %>
						</tr>
					</thead>
					<tbody>
						<%
						if(busList != null && busList.size() > 0)
						{
							int t = 3;
							for(LfSpCorpBindVo lb : busList)
							{
								t = lb.getPlatFormType()==null?3:lb.getPlatFormType();
						%>
						<tr>
							<td><label id="ln<%=lb.getCorpCode()%>"><%=lb.getCorpName()%></label></td>
							<td><label id="lc<%=lb.getCorpCode()%>"><%=lb.getCorpCode()%></label></td>
							<td><label id="ls<%=lb.getCorpCode()%>"><%=lb.getSpUser()%></label></td>
							<%--<td><label id="lt<%=lb.getCorpCode()%>"><%=t-1==0?"EMP应用账户":"EMP接入账户"%></label></td>
							--%><td><label id="lt<%=lb.getCorpCode()%>"><%=t-1==0?MessageUtils.extractMessage("xtgl","xtgl_qygl_empyyzh",request):MessageUtils.extractMessage("xtgl","xtgl_qygl_empjrzh",request)%></label></td>
							<td>
							   <%if (lb.getIsValidate()-0==0){ %>
								<emp:message key="xtgl_qygl_sx" defVal="失效" fileName="xtgl"></emp:message>
								<%}else if(lb.getIsValidate()-1==0){ %>
								<emp:message key="xtgl_qygl_jh" defVal="激活" fileName="xtgl"></emp:message>
								<%} %>
							</td> 
							<% if(btnMap.get(menuCode+"-2")!=null)  { %>
							<td>
							   <%if (lb.getIsValidate()-1==0){ %>
								<a name="querySP" href="javascript:del('<%=lb.getCorpCode() %>','<%=lb.getSpUser() %>','0')"><emp:message key="xtgl_qygl_sx" defVal="失效" fileName="xtgl"></emp:message></a>
								<%}else if(lb.getIsValidate()-0==0){ %>
								<a name="querySP" href="javascript:del('<%=lb.getCorpCode() %>','<%=lb.getSpUser() %>','1')"><emp:message key="xtgl_qygl_jh" defVal="激活" fileName="xtgl"></emp:message></a>
								<%} %>
							</td> 
							<% } %>
						</tr>
						<%
						 	}
						 }
						else
						{
						%>
						<tr><td colspan="6"><emp:message key="xtgl_qygl_wjl" defVal="无记录" fileName="xtgl"></emp:message></td></tr>
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
			</div>
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/bindsp.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
			$('#add').click(function(){
				location.href = "<%=path%>/cor_corpSp.htm?method=goAdd&time=<%=System.currentTimeMillis()%>";
			});

			$('#aid1,#aid2,#aid3').isSearchSelect({'width':'183','zindex':0});
            $("#aid<%=type%>").next('.c_selectBox').show().siblings().hide();
		});
          
		</script>
	</body>
</html>
