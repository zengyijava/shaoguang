<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.shorturl.surlmanage.entity.LfDomain"%>
<%
	//清除页面缓存
	response.setHeader("Pragma","No-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", 0); 
	
	String langName = (String)session.getAttribute("emp_lang");
	
	
	String path=request.getContextPath();
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo.setPageSize(10);
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("bind");
	
	@SuppressWarnings("unchecked")
	List<Userdata> userList = (List<Userdata>)request.getAttribute("userList");
	
	@SuppressWarnings("unchecked")
	List<LfDomain> domains = (List<LfDomain>) request.getAttribute("urlList");
	
	String result = request.getAttribute("w_userdataResult")==null?"0"
			:(String)request.getAttribute("w_userdataResult");
	request.removeAttribute("w_userdataResult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@SuppressWarnings("unchecked")

	String txglFrame = skin.replace(commonPath, inheritPath);
	
	@SuppressWarnings("unchecked")
	List<LfDomain> urlListadd = (List<LfDomain>) request.getAttribute("urlListadd");
	
	@SuppressWarnings("unchecked")
	List<String> corpCodeList = (List<String>) request.getAttribute("corpCodeList");
	@SuppressWarnings("unchecked")
	List<String> corpNameList = (List<String>) request.getAttribute("corpNameList");
	String addcorpCode = (String) request.getAttribute("addcorpCode");

 	String findResult= (String)request.getAttribute("findresult");
    CommonVariables  CV = new CommonVariables();
  
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>新建短域名</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin%>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body>
		<div id="container" class="container">
            <input type="hidden" id="pathUrl" value="<%=path %>"/>
			<input type="hidden" id="corpNameList" value="<%=corpNameList.toString().substring(1,corpNameList.toString().length() - 1)%>"/>
			<input type="hidden" id="corpCodeList" value="<%=corpCodeList.toString().substring(1,corpCodeList.toString().length() - 1)%>"/>
			<%-- 当前位置 --%>

			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<% if(btnMap.get(menuCode+"-1")!=null) {  %>
			<div class="titletop">
					<table class="titletop_table" style="width:388px;">
						<tr>
							<td class="titletop_td">
								新建短域绑定
							</td>
						</tr>
					</table>
			</div>

            <form action="<%=path %>/surl_bind.htm" name="form4" onsubmit="return check()" method="post" id="form4">
            		<div id="loginUser" class="hidden"></div>
            		<input type="hidden" value="<%=path %>" id="path1"/>
            		<div class="buttons" style="background-color: #ccdef8;height: 25px;padding-top: 10px; ">
						新建域名绑定						
					</div>
            		<div id="condition">
						<table>
							<tbody>
								<tr>		
									<td <%=skin.contains("frame4.0") ? "" : "width='7%'"%>>
										企业编号：
									</td>
									<td>
									<%--
										<input maxlength="6" type="text" id="addcorpCode" style="width: 260px;"  name="addcorpCode" value="" onkeyup="getAddcorpName()"  class="input_bd" />
									--%>
										<label>
										<select id="addcorpCode" name="addcorpCode" style="width: 260px;">
										<%
										if(corpCodeList.size()>0){
											for(String corpCode:corpCodeList){
											%>
											<option value="<%=corpCode%>" <%=corpCode.equals(addcorpCode) ? "selected":""%>><%=corpCode%></option>
											<%
											}
										}
										 %>
											
										</select>
										</label>
									</td>
								</tr>
								<tr>									
									<td>
										企业名称：
									</td>
									<td>
										<input maxlength="256" type="text" id="addcorpName" name="addcorpName" <%=skin.contains("frame4.0")?"style=\"width: 198px !important;\"":"style=\"width: 260px !important;\""%>  value="<%=addcorpCode%>"  class="input_bd"  disabled />
									</td>	
								</tr>
							</tbody>
						</table>
					</div>
					<div>
						<b>域名绑定：</b>
					</div>
   					<table id="content" class="domainclass" >
						<thead>
							<tr>
							  <th width="5%"><input type="checkbox"  id="all" value="0"/></th>
							  <th>域名</th>
							  <th>应用类型</th>
							  <th>扩展位数</th>	  
							</tr>
						</thead>
						<tbody>
								<%if(urlListadd != null && urlListadd.size()>0){
									for(LfDomain lfdomain : urlListadd){
								
								%>
								<tr>
									
									<td>
										<label><input type="checkbox" name="domainbind" domain="<%=lfdomain.getDomain() %>" value="<%=lfdomain.getId() %>" /></label>
									</td>
									<td>
										<%
											String urlName = lfdomain.getDomain();
											if(lfdomain.getDomain().length() > 50){
												urlName = urlName.substring(0,50) + "...";
											}%>
										<label title="<%=urlName%>">

											<%=urlName%>
										</label>
									</td>
									<td>
										<label><center>
											<%
											if(lfdomain.getDtype()==0){
												%>
													<label>公用</label>
												<%
											}else{
												%>
													<label>企业独享</label>
												<%
											}
											%>
										</center></label>
									</td>
									<td>
										<label><%=lfdomain.getLenExten()%></label>位
									</td>
																	
								</tr>
								<%
								}
							}else {
								%>
								<tr><td colspan="10" align="center">无记录</td></tr>
								<%	
							}
								%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="4">
									<div id="pageInfo"></div>
								</td>
							</tr>
							<tr>
								<br/>
									 <table style="width: 100%;">
									 	<tr>
									 		<td align="right" >
					                         	<input type="button" id="btnSsu" value="确定" class="btnClass5 mr23"/>
					                         </td>
					                         <td  align="left">
					                         	<input type="button" id="btnSca"  value="取消" class="btnClass6" />
					                         </td>
									 	</tr>
									 </table>
							</tr>
						</tfoot>
                		</table>
                		<div id="corpCode" class="hidden"></div>
                	</form>
                </div>
                		
			<%} %>
			</div>
			<%-- 内容结束 --%>
			<div class="clear"></div>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		
    <div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/shorturl/urldomainBind/js/domainBind.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/shorturl/urldomainBind/js/url_addDomianBind.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/shorturl/urldomainBind/js/pageInfo.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
                showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[5,10,15]);
			});
		</script>
	</body>
</html>
