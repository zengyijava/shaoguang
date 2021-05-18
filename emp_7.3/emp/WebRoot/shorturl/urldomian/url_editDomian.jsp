<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar"%>
<%@page import="java.util.LinkedHashMap"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.shorturl.surlmanage.entity.LfDomain"%>
<%
	//清除页面缓存
	response.setHeader("Pragma","No-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", 0); 
	
	String path=request.getContextPath();
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	@SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("manage");
	
	@SuppressWarnings("unchecked")
	List<Userdata> userList = (List<Userdata>)request.getAttribute("userList");
	
	String result = request.getAttribute("w_userdataResult")==null?"0"
			:(String)request.getAttribute("w_userdataResult");
	request.removeAttribute("w_userdataResult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@SuppressWarnings("unchecked")

	String txglFrame = skin.replace(commonPath, inheritPath);
	
	@SuppressWarnings("unchecked")
	LfDomain lfDomain = (LfDomain) request.getAttribute("lfDomain");
	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>修改短域名</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body>
		<div id="container" class="container" style="width:70%;">
			<%-- 当前位置 --%>

			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<% if(btnMap.get(menuCode+"-1")!=null) {  %>
			<div class="titletop">
					<table class="titletop_table" style="width:388px;">
						<tr>
							<td class="titletop_td">
								修改短域名
							</td>
						</tr>
					</table>
			</div>

            <form action="<%=path %>/surl_manage.htm" name="form2" onsubmit="return update()" method="post" id="form2">
            		<div id="id" class="hidden" name="id" value="<%=lfDomain.getId()%>"></div>
            		<div id="loginUser" class="hidden"></div>
            		<input type="hidden" value="<%=path %>" id="path1"/>
            		<div class="buttons" style="background-color: #ccdef8;height: 25px;padding-top: 10px; ">
						&nbsp;修改短域名					
					</div>
            		<div id="condition">
						<table>
							<tbody>
								<tr>		
									<td>
										短域名：
									</td>
									<td>
										<input maxlength="256" type="text" id="adddomain" style="width: 260px;"  name="adddomain" value="<%=lfDomain.getDomain()%>"   class="input_bd" /></label>
                            				<font style="color: gray;">&nbsp;请添加企业自有域名或梦网指定的域名。</font>
                            
									</td>
								</tr>
								<tr>									
									<td>
										全局扩展位数：
									</td>
									<td>
										<input maxlength="256" type="text" id="addlenExten" name="addlenExten" style="width: 260px;"  value="<%=lfDomain.getLenExten()%>"  class="input_bd" /></label>
                            				&nbsp;位<font style="color: gray;">&nbsp;&nbsp;&nbsp;占位合计N位</font>
                            
									</td>	
								</tr>
								<tr>									
									<td>
										访问时效上限：
									</td>
									<td>
                            			<input maxlength="3" type="text" id="addvalidDays" name="addvalidDays" style="width: 260px;"  value="<%=lfDomain.getValidDays()%>"  class="input_bd" /></label>
                            				&nbsp;天
									</td>	
								</tr>
								<tr>									
									<td>
										应用类型：
									</td>
									<td>
										<label>
				                            <%
			                            		int dtype1 = lfDomain.getDtype();
			                            	 %>
				                            <select name="adddtype" id="adddtype"  class="input_bd" style="<%=skin.contains("frame4.0")?"width:222px;":"width: 265px;"%>" >
			                            			<option value="0" <%="0".equals( lfDomain.getDtype().toString())?"selected":"" %> >共用</option>	
			                            			<option value="1" <%="1".equals( lfDomain.getDtype().toString())?"selected":"" %> >企业独享</option>
				                            </select>
			                            </label>
									</td>	
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content" class="domainclass" >
					<tfoot>
							<tr>
								<br/>
									 <table style="width: 100%;">
									 	<tr>
									 		<td align="right" >
					                         	<input type="submit" id="btnSsu" value="确定" class="btnClass5 mr23"/>
					                         </td>
					                         <td  align="left">
					                         	<input type="button" id="btnSca"  value="取消" class="btnClass6" />
					                         </td>
									 	</tr>
									 </table>
							</tr>
						</tfoot>
						</table>
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame2.5/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/shorturl/urldomian/js/domain.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

	<script>
				
		function checkUserType(loginid){
			if(loginid=="1" || loginid=="2")
			{
				$("#lbUserType").val("WBS00A");
			}else
			{
				$("#lbUserType").val("DBS00A");
			}
			if(loginid=="2" || loginid=="3"){
				$("#spuserdes").show();
			}else{
				$("#spuserdes").hide();
			}
		}
		
		$(document).ready(function(){
		
		getLoginInfo("#corpCode");
		getLoginInfo("#loginUser");
		checkUserType($("#select").val());
		$("#trmorpturl").hide();
		show(<%=result %>);
		var corpType='<%=StaticValue.getCORPTYPE()%>';
	
		
		function show(i)
		{
			if(i == 1)
			{
				alert("新建账户成功！");
				location.href="<%=path%>/pag_userData.htm?lgcorpcode=<%=request.getParameter("lgcorpcode") %>";
			}else if(i == 2)
			{
				alert("修改账户成功！");
				location.href="<%=path%>/pag_userData.htm?lgcorpcode=<%=request.getParameter("lgcorpcode") %>";
			}else if(i != 0)
			{
				alert("操作失败！");
			}	
		}
		
		$('#btnSca').click(function()
			{
				window.location.href="<%=path %>/surl_manage.htm?method=find";
			}
		);
		
		function showExplain(){
  		var ver = $('#showexplain');
  		if(ver.is(':hidden')){
  			ver.show(600);
  		}else{
  			ver.hide(600);
  		}  	
		}
	})
	</script>
	</body>
</html>
