<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.entity.group.LfUdgroup"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
String path=request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = "1700-1500";
@ SuppressWarnings("unchecked")
LinkedHashMap<String,String> groupMap = (LinkedHashMap<String,String>)session.getAttribute("groupMap");
String groupType  = (String)request.getAttribute("groupType");
//共享状态
String shareStatus  = (String)request.getAttribute("shareStatus");
String groupName = (String)request.getAttribute("groupName");
String names="1700";

//登录id
String lguserid = request.getParameter("lguserid");
String lgcorpcode = request.getParameter("lgcorpcode");

String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");
@ SuppressWarnings("unchecked")
HashMap<String,String> encryptmap =(HashMap<String,String>)request.getAttribute("encryptmap");
@ SuppressWarnings("unchecked")
HashMap<String,String> groupEncryptmap =(HashMap<String,String>)request.getAttribute("groupEncryptmap");
@ SuppressWarnings("unchecked")
List<LfUdgroup> groupList = (List<LfUdgroup>)request.getAttribute("groupList");

String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
	    <link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
	    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	    <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
        <style>
			#grp_groupManage #load-bg{
	             display: block;
	             position: absolute;
	             top: 0%;
	             left: 0%;
	             width: 100%;
	             height: 100%;
	             z-index:99999;
	             -moz-opacity: 0.4;
	             opacity:.40;
	             filter: alpha(opacity=40);
	             background:#eee url('<%=commonPath %>/common/img/loading-bg.gif') no-repeat center
	         }            
        </style>
        <link rel="stylesheet" type="text/css" href="<%=iPath %>/css/grp_groupManage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dxzs_YGTongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="grp_groupManage">
		<%
			if(skin.contains("frame4.0")){
		%>
			<input id='hasBeenBind' value='1' type='hidden'/>
		<%
			}
		%>
		<div id="container" class="container">
			<%=ViewParams.getPosition(empLangName, menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<input type="hidden" id="udgIdtemp" name="udgIdtemp"/>
				<input type="hidden" id="udgNametemp" name="udgNametemp"/>
				<input type="hidden" id="zjStr" name="zjStr" value=","/>
				<input type="hidden" id="qzStr" name="qzStr" value=","/>
				<input type="hidden" id="gxStr" name="gxStr" value=","/>
				<input type="hidden" id="ygStr" name="ygStr" value=","/>
				<input type="hidden" id="udgName_old" name="udgName_old"/>
				
			 <input type="hidden" name="pathUrl" id="pathUrl" value="<%=path %>"/>
				<form name="pageForm" action="<%=path %>/grp_groupManage.htm?method=find" method="post">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
 						<input type="hidden" id="servletUrl" value="a_groupManage.htm"/>
 						<input type="hidden" id="udgTypeId" name="udgTypeId" value=""/>
						<% if(btnMap.get(menuCode+"-1")!=null) {  %>
							<a id="addgr" onclick="addGroup()" ><emp:message key="group_ydbg_xzqz_text_addgroup" defVal="新增群组" fileName="group"/></a>
						<% } %>
						<% if(btnMap.get(menuCode+"-2")!=null) {  %>
							<a id="delgr" onclick="deleteGroup()"><emp:message key="group_ydbg_xzqz_text_deletegroup" defVal="删除群组" fileName="group"/></a>
						<% } %>
						<%--<a id="showAll" onclick="showAllGroup()"></a>--%>
						
					</div>
					<div id="perSearchTemp" onclick="getSearch()"></div>
					
					<div id="getloginUser">
					</div>
					
 	                <input type="hidden" id="depCode" name="depCode"/><%-- 员工和客户通讯录  机构编码 --%>
	                <input type="hidden" id="depId" name="depId"/><%-- 自定义通讯录  机构ID --%>
	                <input type="hidden" id="depId" name="depName"/><%-- 自定义通讯录  机构名称 --%>
	                <input type="hidden" name="addrType" id="addrType"/>
					<input type="hidden" id="servletUrl" value="<%=path %>/a_groupManage.htm?method=getTable"/>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td><emp:message key="group_ydbg_xzqz_text_groupname" defVal="群组名称" fileName="group"/>：</td>
									<td><input id="groupName" name="groupName" value="<%=null==groupName?"":groupName %>" type="text"/></td>
									<td><emp:message key="group_ydbg_xzqz_text_type" defVal="类型" fileName="group"/>：</td>
									<td>
										<select id="groupType" name="groupType">
											<option value=""><emp:message key="group_common_text_all" defVal="全部" fileName="group"/></option>
											<option value="0" <%=groupType !=null && groupType.equals("0")?"selected":""%>><emp:message key="group_ydbg_xzqz_text_personalgroup" defVal="个人群组" fileName="group"/></option>
											<option value="1" <%=groupType!=null && groupType.equals("1")?"selected":""%>><emp:message key="group_ydbg_xzqz_text_sharegroup" defVal="共享群组" fileName="group"/></option>
										</select>
									</td>
									<!-- 共享状态 -->
									<td><emp:message key="group_ydbg_xzqz_text_sharestatus" defVal="共享状态" fileName="group"></emp:message>：</td>
									<td>
										<select id="shareStatus" name="shareStatus">
											<option value=""><emp:message key="group_common_text_all" defVal="全部" fileName="group"></emp:message></option>
											<option value="0" <%=shareStatus !=null && shareStatus.equals("0")?"selected":""%>><emp:message key="group_ydbg_xzqz_text_notshared" defVal="未共享" fileName="group"></emp:message></option>
											<option value="1" <%=shareStatus!=null && shareStatus.equals("1")?"selected":""%>><emp:message key="group_ydbg_xzqz_text_hasshared" defVal="已共享" fileName="group"></emp:message></option>
										</select>
									</td>
									<td class="tdSer">
										<center><a id="search"></a></center>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="group_div1"></div>	
					<%-- 表格div 以前是a_groupmanage.jsp--%>
					<div id="bookInfo">
						<input type="hidden" name="path" id="path" value="<%=path %>"/>
				 		<input type="hidden" name="inheritPath" id="inheritPath" value="<%=inheritPath %>"/>
				 		<table id="content">
							<thead>
								<tr>
									<th>
										<input type="checkbox" name="checkall" id="checkall"  onclick='checkAlls(this)'>
									</th>
									<th> 
										<emp:message key="group_ydbg_xzqz_text_groupname" defVal="群组名称" fileName="group"/>
									</th>
									<th> 
										<emp:message key="group_ydbg_xzqz_text_type" defVal="类型" fileName="group"/>
									</th>
									<!-- 共享状态 -->
									<th> 
										<emp:message key="group_ydbg_xzqz_text_sharestatus" defVal="共享状态" fileName="group"></emp:message>
									</th>
									<!-- 创建时间 -->
									<th> 
										<emp:message key="group_ydbg_xzqz_text_createtime" defVal="创建时间" fileName="group"></emp:message>
									</th>
									<th colspan="3">
										<emp:message key="group_common_text_opt" defVal="操作" fileName="group"/>
									</th>
								</tr>
							</thead>
							<tbody>
								<%
									if(null != groupList && 0 != groupList.size())
									{
										String l2gTypeName = "";
										String shareStatusName = "";
										String udgName = "";
										String udgId1 = null;
										for(LfUdgroup groupInfoVo:groupList)
										{
										
											switch(groupInfoVo.getSharetype())
											{
												case 0:
													l2gTypeName = MessageUtils.extractMessage("group","group_ydbg_xzqz_text_personalgroup",request);  //"个人群组";
													break;
												case 1:
													l2gTypeName = MessageUtils.extractMessage("group","group_ydbg_xzqz_text_sharegroup",request);  //"共享群组";
													break;
												    default:break;
												
											}
											switch(groupInfoVo.getShareStatus())
											{
												case 0:
													shareStatusName = MessageUtils.extractMessage("group","group_ydbg_xzqz_text_notshared",request);  //"未共享";
													break;
												case 1:
													shareStatusName = MessageUtils.extractMessage("group","group_ydbg_xzqz_text_hasshared",request);  //"已共享";
													break;
												case 3:
													if(groupInfoVo.getSharetype()==0){
														shareStatusName = MessageUtils.extractMessage("group","group_ydbg_xzqz_text_hasshared",request);  //"已共享";
													}else{
														shareStatusName="-";
													}
													break;
												    default:break;
												
											}
											String UdgId="";
											if(encryptmap!=null){
											 UdgId=encryptmap.get(groupInfoVo.getUdgId()+"");
											}
											String groupid="";
											if(groupEncryptmap!=null){
											groupid=groupEncryptmap.get(groupInfoVo.getGroupid()+"");
											}
								 %>
								 
							<tr>
								<td>
									<input type="checkbox" name="checklist" value="<%=UdgId %>"/>
				 				</td>
								<td><%=null == groupInfoVo.getUdgName()?" ": groupInfoVo.getUdgName().replace("<","&lt;").replace(">","&gt;") %></td>
								<td  class="ztalign"><%=l2gTypeName%></td>
								<td  class="ztalign"><%=shareStatusName%></td>
								<td  class="ztalign"><%=groupInfoVo.getCreateTime()!=null?df.format(groupInfoVo.getCreateTime()):"-"%></td>
								
								 <td><%if(groupInfoVo.getSharetype()==0 && btnMap.get(menuCode+"-3")!=null){ %><a href="javascript:editGourpInfo('<%=groupid %>','<%=groupInfoVo.getUdgName() %>')"><emp:message key="group_common_opt_modify" defVal="修改" fileName="group"/></a><%}else{out.print("-");} %></td>
								<td>
								<%if(groupInfoVo.getSharetype() != 1){ %>
								<a onclick='javascript:showShare("<%=groupid %>","<%=groupInfoVo.getUdgName() %>");'><emp:message key="group_ydbg_xzqz_text_shared" defVal="共享" fileName="group"/></a>
								<%}else{ %>
								-
								<%} %>
								</td>
								 <td><a onclick="showGroupDetails('<%=groupInfoVo.getUdgName() %>','<%=UdgId %>','<%=groupInfoVo.getSharetype() %>')"><emp:message key="group_ydbg_xzqz_text_detail" defVal="详情" fileName="group"/></a></td>
							</tr>
							
							<%
								}
							}
								else if(groupList==null || groupList.size()==0){%>
									<tr><td colspan="8"><emp:message key="group_common_text_norecord" defVal="无记录" fileName="group"/></td></tr>
							 <%
							 }
							 %>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="8">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
				</form>
			</div>
			
 					
			</div>
			<%-- 内容结束 --%>
			
			<div id="infoDiv" title="<emp:message key="group_ydbg_xzqz_text_addgroup" defVal="新增群组" fileName="group"/>"  class="group_display_none">
				<iframe id="flowFrame" name="flowFrame" src="" 
					 marginwidth="0" marginheight="-20px" scrolling="no" frameborder="0"></iframe>
					 <table class="group_table1"><tr><td class="group_td1" align="center">
				<input type="button"  value="<emp:message key="group_common_opt_confire" defVal="确定" fileName="group"/>" class="btnClass5 mr23" onclick="groupAdd()" />
				<input type="button"  value="<emp:message key="group_common_opt_shutdown" defVal="关闭" fileName="group"/>" class="btnClass6" onclick="doNo1()"    />
				<br/>
				</td></tr></table> 
				
			</div>
			<form id="groupShareForm" name="groupShareForm"
							action="grp_groupManage.htm?method=groupShare" method="post">
<%--				<div id="getloginUser" style="padding:5px;display:none;">--%>
<%--					</div>--%>
				<input type="hidden" name="depIdStr" id="depIdStr" value=","/>
				<input type="hidden" name="userIdStr" id="userIdStr" value=","/>
				<input type="hidden" name="depIdStrTemp" id="depIdStrTemp" value=","/><%-- 临时保存depid字符串，在选择员工取消时用到 --%>
				<input type="hidden" id="groupId" name="groupId" value=""/>
				<input type="hidden" id="_groupName" name="_groupName" value=""/>
			</form>
			<div id="shareDiv" title="<emp:message key="group_ydbg_ygtxlgl_text_groupshare" defVal="群组共享" fileName="group"/>"  >
<%--				<input id="flowNames" type="hidden" name="flowNames" value="" />--%>
				<%if(skin.contains("frame4.0")){%>
					<iframe id="shareflowFrame" name="shareflowFrame" src="" marginwidth="0" marginheight="-20px" scrolling="no" frameborder="no"></iframe>
				<% }else{ %>
					<iframe id="shareflowFrame" name="shareflowFrame" src="<%=iPath %>/grp_chooseShareInfo.jsp?lguserid=<%=lguserid%>" marginwidth="0" marginheight="-20px" scrolling="no" frameborder="no"></iframe>
				<%} %>
				<table><tr><td class="group_td1" align="center">
				<input id="skin" type="hidden" name="skin" value="<%=skin %>" />
				<input id="lguserid" type="hidden" name="lguserid" value="<%=lguserid %>" />
				<input id="lgcorpcode" type="hidden" name="lgcorpcode" value="<%=lgcorpcode %>" />
				<input id="commonPath" type="hidden" name="commonPath" value="<%=commonPath %>" />
				<input id="iPath" type="hidden" name="iPath" value="<%=iPath %>" />
				<input id="curId" type="hidden" name="curId" value="" />
				<div id="choiceNum" style="display: none"></div>
				<input id="rightSelectedUserOption" type="hidden" name="rightSelectedUserOption" value="" />
				<input type="button"  value="<emp:message key="group_common_opt_confire" defVal="确定" fileName="group"/>" id="shareSubmit" class="btnClass5" onclick="doOk()" />
				&nbsp;<input type="button"  value="<emp:message key="group_common_opt_cancel" defVal="取消" fileName="group"/>" id="shareCancel" class="btnClass6" onclick="doNo()" />
				<br/>
				</td></tr></table> 
			</div>
			<div id="editInfoDiv" title="<emp:message key="group_ydbg_xzqz_text_modifygroup" defVal="修改群组" fileName="group"/>">
				<iframe id="editFrame" name="editFrame" src=""
					 marginwidth="0" marginheight="-20px" scrolling="no" frameborder="0"></iframe>
					  <table><tr><td class="group_td1" align="center">
					  <input type="button"  value="<emp:message key="group_common_opt_confire" defVal="确定" fileName="group"/>" class="btnClass5 mr23" onclick="groupEdit()"  />
				<input type="button"  value="<emp:message key="group_common_opt_cancel" defVal="取消" fileName="group"/>" class="btnClass6" onclick="doNo2()"   />
				<br/>
				</td></tr></table>
               <%--  <div id="load-bg" class="group_display_none"></div> --%>
			</div>
			
					<div id="com_add_Dom2" title="<emp:message key="group_ydbg_xzqz_text_groupdetail" defVal="群组详情" fileName="group"/>">
						<div id=groupDetail>
						</div>
						<div class="group_div2"></div>
						<center>
							<div style="margin: 0 auto;width: 100px;height: 44px">
								<input type="button" class="btnClass5" value="<emp:message key="group_common_opt_shutdown" defVal="关闭" fileName="group"/>" onclick="closeDom2dialog()"/>
							</div>
						</center>
					</div>
					
			
			<%-- foot开始 --%>
		<div class="bottom">
			<div id="bottom_right">
				<div id="bottom_left"></div>
			</div>
		</div>
		<%-- foot结束 --%>
     <div class="clear"></div>
        <script type="text/javascript">
            function setLoginInfo(id){
                var pdoc = window.parent.parent.document;  //框架顶级页面document
                var pahtm = pdoc.getElementById('loginparams');
                var loginDiv = document.getElementById(id);
                loginDiv.innerHTML = pahtm.innerHTML;
            }
            setLoginInfo("getloginUser");
        </script>
     <script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/group_<%=empLangName%>.js"></script>
 		<script language="javascript" type="text/javascript" src="<%=iPath %>/js/groupManageEqa.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath%>/frame/frame4.0/js/dialogui.js"></script>
 		<script>
 		$(document).ready(function() {
 			noyinhao("#name");
 			var time = new Date();
 			var lguserid = $("#lguserid").val();
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
 			bindPClik();
 			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
 			$('#search').click(function(){submitForm();});
 			
 			limit();
 			
 			$('#com_add_Dom2').dialog({
 				autoOpen: false,
 				height: 500,
 				width:430,
 				modal:true,
 				/*buttons:{
 					"关闭":function(){
 						$('#com_add_Dom2').dialog('close');
 						submitForm();
 					}
 				},*/
 				open:function(){
 					$("select[name='groupType']").css("visibility","hidden");
 				},
 				close:function(){
 					$("select[name='groupType']").css("visibility","visible");
 				}
 			});
 			resizeDialog($("#com_add_Dom2"),"ydbgDialogJson","ygtxlgl_grqz_test1");
 			$("#infoDiv").dialog({
 				autoOpen: false,
 				/* height: 590, */
 				width: 626,
 				resizable:false,
 				modal: true,
 				open:function(){
 					//$(".ui-dialog-titlebar-close").hide();
 					$("select[name='groupType']").css("visibility","hidden");
 				},
 				close:function(){
 					$("select[name='groupType']").css("visibility","visible");
 				}
 			});
 			resizeDialog($("#infoDiv"),"ydbgDialogJson","ygtxlgl_grqz_test2");
 			$("#editInfoDiv").dialog({
 				autoOpen: false,
 				height: 600,
 				width: 630,
 				resizable:false,
 				modal: true,
 				open:function(){
 					//$(".ui-dialog-titlebar-close").hide();
 					$("select[name='groupType']").css("visibility","hidden");
                    //$('#load-bg').show();
 				},
 				close:function(){
 					$("select[name='groupType']").css("visibility","visible");
                    window.frames["editFrame"].document.write('');
 				}
 			});
 			resizeDialog($("#editInfoDiv"),"ydbgDialogJson","ygtxlgl_grqz_test2");
 			$("#shareDiv").dialog({
 				autoOpen: false,
 				height:550,
 				width: 555,
 				resizable:false,
 				modal: true,
 				open:function(){
 				//	$(".ui-dialog-titlebar-close").hide();
 					$("select[name='groupType']").css("visibility","hidden");
 				},
 				close:function(){
 					$("select[name='groupType']").css("visibility","visible");
 					$(window.frames['shareflowFrame'].document).find("#idPlaceholder").css("display","");
 				}
 			});
 			resizeDialog($("#shareDiv"),"ydkfDialogJson","kfdx_kfqzqf_test2");
 		});
 		</script>
	</body>
</html>
