<%@ page language="java" import="java.util.List" pageEncoding="utf-8"%>
<%@ page import="org.apache.commons.beanutils.DynaBean" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@page import="java.util.Date"%>
<%@ page import="java.util.HashMap" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	LfSysuser curSysuser=(LfSysuser)session.getAttribute("loginSysuser");
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	String bookType=(String)request.getAttribute("bookType");
	@SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("addrBook");
	boolean ismodule=(Boolean)request.getAttribute("ismodule");
	List<DynaBean> clientList = new ArrayList<DynaBean>();
	//List<LfEmployeeVo> employeeList= new ArrayList<LfEmployeeVo>();
	if("client".equals(bookType))
	{
		clientList=(List<DynaBean>)session.getAttribute("bookInfoList");
	}else if("employee".equals(bookType)){
		//employeeList=(List<LfEmployeeVo>)session.getAttribute("bookInfoList");
		menuCode = titleMap.get("employeeAddrBook");
	}
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	CommonVariables commonVariables = new CommonVariables();
	HashMap<String,String> encryptmap=   (HashMap)request.getAttribute("encryptmap");
	String unknowDepClient = (String)request.getAttribute("unknowDepClient");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
		
%>
		<table id="content">
			<thead>
				<tr>
				 <th>
						<input type="checkbox" name="dels" value=""
							onclick="checkAlls(this,'delBook')" />
					</th>
					<%if("client".equals(bookType)) {%><th><emp:message key="client_khtxlgl_kftxl_text_clientnumber" defVal="客户号" fileName="client"></emp:message></th><%} %>
					<% if("employee".equals(bookType)) {%><th><emp:message key="client_khtxlgl_kftxl_text_jobnumber" defVal="工号" fileName="client"></emp:message></th><%} %>
					<th>
						<emp:message key="client_khtxlgl_kftxl_text_name" defVal="姓名" fileName="client"></emp:message>
					</th>
					<th>
						<emp:message key="client_khtxlgl_kftxl_text_phone" defVal="手机" fileName="client"></emp:message>
					</th>
					<% if("client".equals(bookType)&&ismodule==true){ %>
					<th>
						<emp:message key="client_khtxlgl_kftxl_text_appaccount" defVal="APP用户账户" fileName="client"></emp:message>
					</th>
					<% }%>
					<th>
						<%if("custorm".equals(bookType)){out.print(MessageUtils.extractMessage("client","client_khtxlgl_kftxl_text_affiliatedoperator",request));}else{out.print(MessageUtils.extractMessage("client","client_khtxlgl_kftxl_text_affiliation",request));} %>
					</th>
					<th><emp:message key="client_khtxlgl_kftxl_text_signuser" defVal="签约用户" fileName="client"></emp:message></th>
					
					
					<%--<%if(!"custorm".equals(bookType)) {%><th>操作</th><%} %>  --%>
					<%if("employee".equals(bookType)&&btnMap.get(menuCode+"-3")!=null){%>
					<th><emp:message key="client_common_text_opt" defVal="操作" fileName="client"></emp:message></th>
					<%} %>
					<%if("custorm".equals(bookType)&&btnMap.get(menuCode+"-3")!=null){%>
					<th><emp:message key="client_common_text_opt" defVal="操作" fileName="client"></emp:message></th>
					<%} %>
					<%if("client".equals(bookType)&&btnMap.get(menuCode+"-3")!=null){%>
					<th><emp:message key="client_common_text_opt" defVal="操作" fileName="client"></emp:message></th>
					<%} %>
				</tr>
			</thead>
			<tbody>
			<%
			if(clientList!=null && clientList.size()>0)
			{

				for(int g=0;g<clientList.size();g++)
				{
					DynaBean bookInfo=clientList.get(g);
			%>
				<tr>
					<%
						String client_Id = bookInfo.get("client_id").toString();
						String clientId=encryptmap.get(client_Id);
						if(unknowDepClient.contains(","+clientId+","))
						{
					%>		
							 <td><input type="checkbox" name="delBook" value="<%=clientId %>" disabled="disabled"/></td>
					<%
						}
						else
						{
					%>	
							<td><input type="checkbox" name="delBook" value="<%=clientId %>"/></td>
					<%
						}
					 %>
				    <%--
				    <td><xmp><%=bookInfo.getClientCode() %></xmp></td>
				     --%>
				     <td>	<%
				     			if(bookInfo.get("client_code") != null && !"".equals(bookInfo.get("client_code") )){
				     				out.print(bookInfo.get("client_code").toString() .replace("<","&lt;").replace(">","&gt;"));
				     			}else{
				     				out.print("-");
				     			}
				     		%></td>
					<td class="textalign" ><xmp><%=bookInfo.get("name")==null?"-":bookInfo.get("name") %></xmp></td>
					<%
					String phoneStr=bookInfo.get("mobile")!=null?bookInfo.get("mobile").toString():"-";
					if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) {
					%>
					<td><%=phoneStr==null?"-":phoneStr %></td>
					<%}else{ %>
					<td><%=phoneStr==null?"-":commonVariables.replacePhoneNumber(phoneStr) %></td>
					<%} %>
					<% if("client".equals(bookType)&&ismodule==true){
						
						%>
					<td >
					<%
				     			if(bookInfo.get("app_code") != null && !"".equals(bookInfo.get("app_code") )){
				     				String appcode=bookInfo.get("app_code").toString();
				     				%>
				     				<a href="javascript:prev('<%=appcode%>');"><%=appcode%></a>
				     				<% 
				     			}else{
				     				out.print(MessageUtils.extractMessage("client","client_khtxlgl_kftxl_text_unregistered",request));
				     			}
				     		%>
					
					</td>
					<% }%>
					<td class="textalign" ><xmp><%=bookInfo.get("dep_name")!=null?bookInfo.get("dep_name"):"-" %></xmp></td>
					<td>
						<%
							if(null!=bookInfo.get("iscontract") && "0".equals(bookInfo.get("iscontract").toString())){
						%>
								<a href="javascript:openContractDetail('<%=bookInfo.get("guid").toString() %>')"><emp:message key="client_common_text_no" defVal="否" fileName="client"></emp:message></a>
						<%		
							}else if(null!=bookInfo.get("iscontract") && "1".equals(bookInfo.get("iscontract").toString())){
						%>
								<a href="javascript:openContractDetail('<%=bookInfo.get("guid").toString() %>')"><emp:message key="client_common_text_yes" defVal="是" fileName="client"></emp:message></a>
						<%		
							}
						%>
					</td>
					<%if(btnMap.get(menuCode+"-3")!=null) {
					String updateid="";
					if(bookInfo.get("client_id")!=null){
						updateid=encryptmap.get(bookInfo.get("client_id").toString());
					}
					%>
					<td><a onclick="window.location.href='cli_addrBook.htm?method=doEditcd&bookId=<%=updateid %>&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val()"><emp:message key="client_common_opt_modify" defVal="修改" fileName="client"></emp:message></a></td>
					<%} %>
				</tr>
			<%
				}
			}else{
				if("client".equals(bookType)&&ismodule==true){
			%>
			
					<tr><td colspan="8"><emp:message key="client_common_text_norecord" defVal="无记录" fileName="client"></emp:message></td></tr>
			 <%
				}else {%>
					 <tr><td colspan="7"><emp:message key="client_common_text_norecord" defVal="无记录" fileName="client"></emp:message></td></tr>
				 <%
				 }
				}
			%>

			</tbody>
			<tfoot>
				<tr>
					<td colspan="9">
						<div id="pageInfo"></div>
					</td>
				</tr>
			</tfoot>
		</table>
	    <%-- 查看签约详情 --%>
		<div id="contractDetail" title="<emp:message key="client_khtxlgl_kftxl_text_singdetail" defVal="签约详情" fileName="client"></emp:message>" style="padding:0 0 0 10px;display:none">
			<iframe id="contractDetailFrame" name="contractDetailFrame" src="" style="width:690px;height:400px;border: 0;" marginwidth="0"  frameborder="no"></iframe>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/client_<%=empLangName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script>
		<script>
		$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
		}, function() {
			$(this).removeClass("hoverColor");
		});
		$("#contractDetail").dialog({
			autoOpen: false,
			height: 475,
			width: 700,
			resizable:false,
			modal: true,
			open:function(){
			},
			close:function(){
				$("#contractDetailFrame").attr("src","");
			}
		});
		function openContractDetail(guid){
			$("#contractDetail").css("display","block");
		    var lguserid = $("#lguserid").val();
		    var lgcorpcode = $("#lgcorpcode").val();
		    var time = new Date();
		 	$("#contractDetailFrame").attr("src",'cli_addrBook.htm?method=getContractDetail&pageSize=10&guid='+guid+'&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode,{time:time});
		 	$('#contractDetail').dialog('open');
		}
		function closeContractDetal()
		{
			$("#contractDetail").dialog("close");
			$("#contractDetailFrame").attr("src","");
		}			
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		
		//导出全部数据到excel
		//$("#exportCondition").click(
		function importExcel()
		 {
			  if(confirm(getJsLocaleMessage('client','client_common_exportexcel')))
			   {
			   		var depId = $("#depId").val();
			   		
			   		var name = $("#name").val();
			   		
			   		var phone = $("#phone").val();
			   		var appacount=$("#appacount").val();
			   		var appstatue=$("#appstatue").val();
			   		<%
			   		if("client".equals(bookType)){
					   		if(clientList !=null && clientList.size() != 0){
					   			if(pageInfo.getTotalRec()<=500000)
					   			{
					   		%>			
					   			  $.ajax({
									type: "POST",
									url: "<%=path%>/cli_addrBook.htm?method=exportClientExcel",
									data: {
											lguserid:$("#lguserid").val(),
										   	lgcorpcode:$("#lgcorpcode").val(),
										   	appacount:appacount,
										   	appstatue:appstatue
										  },
					                beforeSend:function () {
					                    page_loading();
					                },
					                complete:function () {
					               	  	page_complete()
					                },
					                success:function (result) {
					                    if (result == 'true') {
					                    	download_href("<%=path%>/cli_addrBook.htm?method=downloadFile");
					                    } else {
					                        //alert('导出失败！');
					                        alert(getJsLocaleMessage('client','client_common_exportfalse'));
					                    }
	               					}
								  });
					   		      //location.href="<%=path %>/cli_addrBook.htm?method=exportClientExcel&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val()+"&appacount="+appacount+"&appstatue="+appstatue;
		
					   		<%	
					   			}
					   			else
					   			{
					   		%>
					   				//alert("数据量超过导出的范围50万，请从数据库中导出！");
					   				 alert(getJsLocaleMessage('client','client_common_datatoobig'));
					   		<%
					   			}
					   		}else{
					   		%>
					   		   // alert("无数据可导出！");
					   		    alert(getJsLocaleMessage('client','client_common_nodatatoexport'));
					   		<%
					   		}
			   		}%>
			   		
			   }
		  }//);
		
		</script>
