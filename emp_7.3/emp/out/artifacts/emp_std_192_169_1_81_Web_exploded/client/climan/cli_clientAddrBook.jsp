<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.client.vo.LfClientVo"%>
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
	
	@SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String bookType=(String)request.getAttribute("bookType");
	boolean ismodule=(Boolean)request.getAttribute("ismodule");
	String menuCode = titleMap.get("addrBook");
	boolean addcode = false;
	if(btnMap.get(menuCode+"-1")!=null)
	{
		addcode = true;
	}
	boolean delcode = false;
	if(btnMap.get(menuCode+"-2")!=null)
	{
		delcode = true;
	}
	LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	LinkedHashMap<String, String> corpConf = SystemGlobals.getSysParamLfcorpConf(lfSysuser.getCorpCode());
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = String.valueOf(request.getAttribute("lguserid"));
	
	LfClientVo bookInfo =(LfClientVo)session.getAttribute("clientInfo");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
		
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="client_khtxlgl_kftxl_text_addressbook" defVal="客户通讯录" fileName="client"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link href="<%=iPath %>/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<style type="text/css">
			#cli_clientAddrBook #etree{
				height:250px;WIDTH: 200px;overflow:auto;
			}
			#cli_clientAddrBook #appstatue{
				width:210px;
			}
			#cli_clientAddrBook .client_div1{
				clear:right;
			}
			.client_display_none{
				display: none;
			}
		</style>
	</head>
	<body onload="getClientTable('<%=path %>')" id="cli_clientAddrBook">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=ViewParams.getPosition(empLangName, menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">

				<div class="left_dep div_bd" align="left" >
						<input type="hidden" id="id" />
					<h3 class="div_bd title_bg">
						<emp:message key="client_khtxlgl_kftxl_text_clientorg" defVal="客户机构" fileName="client"></emp:message>
					</h3>
					<div id="depOperate" class="depOperate">
						<%if (delcode){%>
							<span id="delDepNew" class="depOperateButton3" onclick="doDepDel()" title="<emp:message key="common_delete" defVal="删除" fileName="common"/>"></span>
						<%} %>
						<span id="updateDepNew" class="depOperateButton2" onclick="updateDepFun()" title="<emp:message key="common_edit" defVal="编辑" fileName="common"/>"></span>
						<%if (addcode){%>
							<span id="addDepNew" class="depOperateButton1" onclick="addDepFun()" title="<emp:message key="common_add" defVal="增加" fileName="common"/>"></span>
						<%} %>
					</div>
					<div id="etree" class="list">
					</div>
				</div>
			<div class="right_info">
			<form name="pageForm" action="" method="post">
			<div class="client_display_none" id="hiddenValueDiv"></div>
				<div class="buttons">
					<div id="toggleDiv"></div>
					<% if(btnMap.get(menuCode+"-1")!=null) {  %>
					<a id="addcli" onclick="javascript:addClient();"><emp:message key="client_khtxlgl_kftxl_opt_newclient" defVal="新建客户" fileName="client"></emp:message></a>
					<%} %>
					<% if(btnMap.get(menuCode+"-2")!=null) {  %>
					<a id="delcli" onclick="javascript:delBk()"><emp:message key="client_khtxlgl_kftxl_opt_deleteclient" defVal="删除客户" fileName="client"></emp:message></a>
					<%} %>
					<% if(btnMap.get(menuCode+"-9")!=null) {  %>
					<%-- <a id="syncCus" onclick="sync(2)"></a> --%>
					<% } %>
					<% if(btnMap.get(menuCode+"-8")!=null) {  %>
					<%-- <a id="syncDep" onclick="sync(3)"></a> --%>
					<% } %>
					<%--
					<a id="showAll" onclick="showAll()"></a>
					 --%>
					<%
						if(btnMap.get(menuCode+"-5")!=null)
						{
					%>
					<a id="exportCondition" onclick="importExcel()"><emp:message key="client_common_opt_export" defVal="导出" fileName="client"></emp:message></a>
					<%		
						}
					%>
				</div>
				<input type="hidden" id="servletUrl" value="<%=path %>/cli_addrBook.htm?method=getTable"/>
				<input type="hidden" id="depId" value="" />
				<input type="hidden" id="dName" value=""/>
				<input type="hidden" id="delUrl" value="<%=path %>/cli_addrBook.htm?method=deletecd"/>
				<div id="condition">
					<table>
						<tbody>
							<tr>
								<td><emp:message key="client_khtxlgl_kftxl_text_name" defVal="姓名" fileName="client"></emp:message>：</td>
								<td><input id="name" value="<%=bookInfo.getName()==null?"":bookInfo.getName() %>"/></td>
								<td><emp:message key="client_khtxlgl_kftxl_text_phone" defVal="手机" fileName="client"></emp:message>：</td>
								<td><input id="phone" onkeyup="phoneInputCtrl($(this))" maxlength="21" value="<%=bookInfo.getMobile()==null?"":bookInfo.getMobile() %>"/></td>
								<td><emp:message key="client_khtxlgl_kftxl_text_signuser" defVal="签约用户" fileName="client"></emp:message>：</td>
								<td>
									<select id="iscontract" name="iscontract">
										<option value=""><emp:message key="client_common_text_all" defVal="全部" fileName="client"></emp:message></option>
										<option value="1" <%=bookInfo.getIsContract()!=null&& 1==bookInfo.getIsContract()? "selected" : "" %>><emp:message key="client_common_text_yes" defVal="是" fileName="client"></emp:message></option>
										<option value="0" <%=bookInfo.getIsContract()!=null&& 0==bookInfo.getIsContract()? "selected" : "" %>><emp:message key="client_common_text_no" defVal="否" fileName="client"></emp:message></option>
									</select>
								</td>
								<td class="tdSer">
								    <center><a id="search"></a></center>
								</td>
							</tr>
							<% if("client".equals(bookType)&&ismodule==true){ %>
								<tr>
								<td><emp:message key="client_khtxlgl_kftxl_text_appaccount" defVal="APP用户账户" fileName="client"></emp:message>：</td>
								<td><input id="appacount" name="appacount" /></td>								
								<td><emp:message key="client_khtxlgl_kftxl_text_appstatus" defVal="APP状态" fileName="client"></emp:message>：</td>
								<td> 
								<select name="appstatue" id="appstatue">
								<option value=""><emp:message key="client_common_text_all" defVal="全部" fileName="client"></emp:message></option>
								<option value="0" <%if(("1").equals("")) {%>selected="selected" <%}%> ><emp:message key="client_khtxlgl_kftxl_text_registered" defVal="已注册" fileName="client"></emp:message></option>
								<option value="1" <%if(("2").equals("")) {%>selected="selected" <%}%> ><emp:message key="client_khtxlgl_kftxl_text_unregistered" defVal="未注册" fileName="client"></emp:message></option>
								</select>
								</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						<% }%>
						</tbody>
					</table>
				</div>

				<div class="client_div1"></div>	
				<div id="bookInfo">

				</div>
			</form>
			</div>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/client_<%=empLangName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/client/climan/js/addrbook.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
            $("#toggleDiv").toggle(function() {
                $("#condition").hide();
                $(this).removeClass("collapse");
            }, function() {
                $("#condition").show();
                $(this).addClass("collapse");
            });
			$("#addDepNew").hover(function() {
				$(this).removeClass("depOperateButton1");
				$(this).addClass("depOperateButton1On");
			}, function() {
				$(this).addClass("depOperateButton1");
				$(this).removeClass("depOperateButton1On");
			});

			$("#updateDepNew").hover(function() {
				$(this).removeClass("depOperateButton2");
				$(this).addClass("depOperateButton2On");
			}, function() {
				$(this).addClass("depOperateButton2");
				$(this).removeClass("depOperateButton2On");
			});

			$("#delDepNew").hover(function() {
				$(this).removeClass("depOperateButton3");
				$(this).addClass("depOperateButton3On");
			}, function() {
				$(this).addClass("depOperateButton3");
				$(this).removeClass("depOperateButton3On");
			});

			$("#etree").load("<%=iPath %>/a_addrbookDepTree.jsp?treemethod=getClientSecondDepJson&lguserid=<%=lguserid%>&ac="+<%=addcode%>+"&dc="+<%=delcode%>);
			$('#search').click(function(){submitForm();});	
		});
		
		function doOk()
		{
             var pathUrl = $("#pathUrl").val();
             var name = $("#depName").val();
             var superiorId = $("#superiorId").val();//父id
             var depcodethird = $("#depcodethird").val();//自定义机构编码
             name = name.replace(/(^\s*)|(\s*$)|\\/g,"");
             
             //过滤重复部门名
             var node = zTree.getSelectedNode();  
             var deleteZtree2Node = zTree.getNodesByParam("pId",node.id  ,node);
			 if(deleteZtree2Node!=null){
				for(var i=0;i<deleteZtree2Node.length;i++){
					if(deleteZtree2Node[i].name == name){
							//alert("名称重复");
							alert(getJsLocaleMessage('client','client_page_clientAddrBook_samename'));
							return;
					}
				}
			}

             if (name == "")
             {
                 //alert("请输入名称！");
                 alert(getJsLocaleMessage('client','client_page_clientAddrBook_entername'));
                 return;
             }
             if(name.indexOf("'")!=-1){
               	//alert("名称中不能包含单引号！");
               	 alert(getJsLocaleMessage('client','client_page_clientAddrBook_errorname'));
               	return;
               }
             if(depcodethird=="")
             {
				//alert("请输入机构编码！");
				alert(getJsLocaleMessage('client','client_page_clientAddrBook_enterorgcode'));
				return;
             }
             if(depcodethird.indexOf("'")!=-1)
             {
				//alert("请合理输入机构编码！");
				alert(getJsLocaleMessage('client','client_page_clientAddrBook_orgcodeerror'));
				return;
             }
			 var zenze=/^[A-Za-z0-9]+$/;
			 if(!zenze.test(depcodethird))
			 {
			 	 //alert("输入非法，编码只能是字母和数字组成！");
			 	 alert(getJsLocaleMessage('client','client_page_clientAddrBook_entererror'));
			 	 return;
			 } 
             else
             {
                 var lguserid = $("#lguserid").val();
                 var lgcorpcode=$("#lgcorpcode").val();
                 $("#depcofim").attr("disabled",true);
                $.post(pathUrl+"/cli_addrBook.htm?method=addDep",{lguserid:lguserid,lgcorpcode:lgcorpcode,name:name,superiorId:superiorId,depcodethird:depcodethird},function(r){
					 if(r == "error"){
     				 	//alert("获取机构树异常！");
     				 	alert(getJsLocaleMessage('client','client_page_clientAddrBook_getorgexp'));
     				 }else if(r == "parentnotexit"){
     				 	//alert("该机构不存在，可能已被删除！");
     				 	alert(getJsLocaleMessage('client','client_page_clientAddrBook_orgisnull'));
    					window.location.reload();
     				 }else if(r == "maxLevel"){
     				 	// alert("机构级数最大限制为"+<%=corpConf.get(StaticValue.DEP_MAXLEVEL) %> +"级！");
     				 	alert(getJsLocaleMessage('client','client_page_clientAddrBook_orgmax')+<%=corpConf.get(StaticValue.DEP_MAXLEVEL) %> +getJsLocaleMessage('client','client_page_clientAddrBook_ji'));
     				 }else if(r == "maxChild"){
                        //alert("子机构最大限制为"+<%=corpConf.get(StaticValue.DEP_MAXCHILD) %>+"个！");
                        alert(getJsLocaleMessage('client','client_page_clientAddrBook_childorgmax')+<%=corpConf.get(StaticValue.DEP_MAXCHILD) %>+getJsLocaleMessage('client','client_page_clientAddrBook_ge'));
                     }else if(r == "maxDep"){
                     	//alert("机构总数最大限制为"+<%=corpConf.get(StaticValue.DEP_MAXDEP) %>+"个！ ");
                     	alert(getJsLocaleMessage('client','client_page_clientAddrBook_orgmaxnum')+<%=corpConf.get(StaticValue.DEP_MAXDEP) %>+getJsLocaleMessage('client','client_page_clientAddrBook_ge'));
                     }else if(r=="codethirdRepeat"){
						//alert("机构编码重复！");
						alert(getJsLocaleMessage('client','client_page_clientAddrBook_reorgcode'));
					 }else if(r=="existdepname"){
						//alert("机构名称重复！");
						alert(getJsLocaleMessage('client','client_page_clientAddrBook_reorgname'));
					 }else if(r=="true"){
						//alert("添加机构成功！");
						alert(getJsLocaleMessage('client','client_page_clientAddrBook_addorgsuc'));
						zTree.setting.asyncUrl =  pathUrl+"/cli_addrbookDepTree.htm?method=getClientSecondDepJson&depId="+node.id;
                    	 zTree.reAsyncChildNodes(zTree.getSelectedNode(), "refresh");
                    	 doNo();
                     }else{
						//alert("新增机构失败！");
						alert(getJsLocaleMessage('client','client_page_clientAddrBook_addorgfalse'));
                     }
                     //doNo();
					 $("#depcofim").attr("disabled",false);
                })
              }
		}
		function doNo()
		{
			 $("#depName").val("");
            // $("#superiorId").val("");//父机构id清空
             $("#depcodethird").val("");//机构编码清空
             $("#addDep").dialog("close");
	    }
		function doDepDel()
	    {
			var oldName =  $("#depOldName").val();
			if(oldName==""||oldName==null)
			{
				//alert("请选择要删除的机构！");
				alert(getJsLocaleMessage('client','client_page_clientAddrBook_selectorgdelete'));
				return;
			}
	    	var id= $("#depId").val();
	    	var pathUrl = $("#pathUrl").val();
             if(confirm(getJsLocaleMessage('client','client_page_clientAddrBook_confiredeleteorg')))
             {
                 var lgcorpcode=$("#lgcorpcode").val();
                 var lguserid=$("#lguserid").val();
            	 $.post(pathUrl+"/cli_addrBook.htm?method=delDep",{lgcorpcode:lgcorpcode,lguserid:lguserid,id:id},function(r){
                     if(r!=null&&r=="1")
                     {
                     	//alert("删除成功！");
                     	alert(getJsLocaleMessage('client','client_common_deletesuc'));
                     	$("#superiorId").val("");
                		$("#updateDid").val("");
                		$("#depOldName").val("");
                		$("#depNewName").val("");
                		$("#depcodethird2").html("");
                		$("#depPid").val("");
                     	//zTree.setting.asyncUrl =  pathUrl+"/a_addrbookDepTree.htm?method=getClientSecondDepJson&depId="+zTree.getSelectedNode().parentNode.id;
                   	 	//zTree.reAsyncChildNodes(zTree.getSelectedNode().parentNode, "refresh");
                   	 	//submitForm();
                   	 	window.location.href = pathUrl + "/cli_addrBook.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
                     }else if(r!=null && r=="0"){
                       //alert("该机构下有子机构不能删除！");
                       alert(getJsLocaleMessage('client','client_page_clientAddrBook_childorgnotdel'));
                     }else if(r!=null && r=="2"){
                      	//alert("总机构不能删除！");
                      	alert(getJsLocaleMessage('client','client_page_clientAddrBook_rootorgnotdel'));
                     }else if(r!=null && r=="3"){
                      	//alert("顶级机构不能删除！");
                      	alert(getJsLocaleMessage('client','client_page_clientAddrBook_parentorgnotdel'));
                     }else if(r!=null && r=="4"){
                      	//alert("'APP未挂接用户'机构不允许删除！");
                      	alert(getJsLocaleMessage('client','client_page_clientAddrBook_apporgnotdes'));
                     }else if(r!=null && r=="5"){
                      	//alert("该机构不存在，可能已被删除！");
                      	alert(getJsLocaleMessage('client','client_page_clientAddrBook_orgisnull'));
                        //多个用户删除同一个机构，删除失败，要刷新
                    	$("#superiorId").val("");
                		$("#updateDid").val("");
                		$("#depOldName").val("");
                		$("#depNewName").val("");
                		$("#depcodethird2").html("");
                		$("#depPid").val("");
                       window.location.href = pathUrl + "/cli_addrBook.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
                     } else if (r!=null && r=="haveBirthdayMembers"){
				 		alert("该机构在客户生日祝福中被选中，不能删除！")
			 }else{
                       //alert("删除失败！");
                       alert(getJsLocaleMessage('client','client_common_deletefalse'));
                     }
               })
             }
		}
		
			function addClient(){
				var lguserid = $("#lguserid").val();
				var lgcorpcode = $("#lgcorpcode").val();
				window.location.href='<%=path%>/cli_addrBook.htm?method=getClientType&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode;
			}
		
		function addDepFun()
	{
		var oldName =  $("#depOldName").val();
		if(oldName==""||oldName==null)
		{
		    /*请选择机构！*/
			alert(getJsLocaleMessage('client','client_page_addrbookDepTree_selectorg'));
			return;
		}
		$("#addDep").dialog("open");
	}
	
		</script>
	</body>
</html>
