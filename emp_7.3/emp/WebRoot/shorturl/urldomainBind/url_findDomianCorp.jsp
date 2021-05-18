<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.shorturl.surlmanage.vo.LfDomainCorpVo"%>
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
	
	String result = request.getAttribute("w_userdataResult")==null?"0"
			:(String)request.getAttribute("w_userdataResult");
	request.removeAttribute("w_userdataResult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@SuppressWarnings("unchecked")

	String txglFrame = skin.replace(commonPath, inheritPath);
	
	String findResult= (String)request.getAttribute("findresult");
    CommonVariables  CV = new CommonVariables();
    
	@SuppressWarnings("unchecked")
	List<LfDomain> domains = (List<LfDomain>) request.getAttribute("domains");
	String ids = "";
	if(domains!=null&&domains.size()>0){
		for (int i = 0; i < domains.size(); i++) {
			ids = ids + String.valueOf(domains.get(i).getId())+",";
		}
		ids = ids.substring(0,ids.length()-1);
	}

	@SuppressWarnings("unchecked")
	LfDomainCorpVo lfDomainCorpVo = (LfDomainCorpVo)request.getAttribute("lfDomainCorpVo");
	
	@SuppressWarnings("unchecked")
	List<LfDomain> urlListadd = (List<LfDomain>) request.getAttribute("urlListadd");
	
	String corpid= (String)request.getAttribute("corpid");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>查看域名绑定</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
		<link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	</head>
	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>

			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<% if(btnMap.get(menuCode+"-1")!=null) {  %>
			<div class="titletop">
					<table class="titletop_table" style="width:388px;">
						<tr>
							<td class="titletop_td">
								查看域名绑定
							</td>
						</tr>
					</table>
			</div>

            <form action="<%=path %>/surl_bind.htm" name="form6"  method="post" id="form6">
            		<div id="loginUser" class="hidden"></div>
            		<input type="hidden" value="<%=path %>" id="path1"/>
            		<input type="hidden" name="corpid" id="corpid" value="<%=corpid%>"/>
            		<div class="buttons" style="background-color: #ccdef8;height: 25px;padding-top: 10px; ">
						查看域名绑定						
					</div>
            		<div id="condition">
						<table>
							<tbody>
								<tr>		
									<td <%=skin.contains("frame4.0") ? "" : "width='7%'"%>>
										企业编号：
									</td>
									<td>
										<input maxlength="6" type="text" id="addcorpCode" style="width: 260px;"  name="addcorpCode" value="<%=lfDomainCorpVo.getCorpCode() %>" disabled="disabled"  class="input_bd" />
									</td>
								</tr>
								<tr>									
									<td>
										企业名称：
									</td>
									<td>
										<input maxlength="256" type="text" id="addcorpName" name="addcorpName" style="width: 260px;"  value="<%=lfDomainCorpVo.getCorpName() %>"  class="input_bd"  disabled="disabled" />
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
							  <th width="5%"><input type="checkbox" onclick="return false;" id="all" value="0"/></th>
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
										<label><input type="checkbox" checked="checked" name="domainbind" onclick="return false;" value="<%=lfdomain.getId() %>" /></label>
									</td>
									
									<td>
										<label><%=lfdomain.getDomain() %></label>
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
								    <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
									<div id="pageInfo"></div>
								</td>
								<br/>
									 <table style="width: 100%;">
									 	<tr>
					                         <td  align="center">
					                         	<input type="button" id="btnSca"  value="返回" class="btnClass6" />
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame2.5/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/shorturl/urldomainBind/js/domainBind.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/shorturl/urldomainBind/js/pageInfoFind.js"></script>
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
            showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[10]);
			$('#btnSca').click(function()
				{
					window.location.href="<%=path %>/surl_bind.htm?method=find";
				}
			);
		
			//复选框全选操作

    		var ids ='<%=ids %>'; 
    		var checkvalue = "";
    		var id=ids.split(",");
    		$("input[name='domainbind']").each(function(){
    			checkvalue = $(this).val(); 
    			for( var i = 0; i < id.length; i++){
	    			if(id[i]==checkvalue){ 
			            this.checked = true; 
			            break;   
		             }else{
		              	this.checked = false;     
		             }   
    			}
//               if(ids.indexOf(checkvalue)>-1){ 
//                   this.checked = true;    
//                 }else{
//                 	this.checked = false;     
//                 }   
           }); 		 
			
	})
	</script>
	</body>
</html>
