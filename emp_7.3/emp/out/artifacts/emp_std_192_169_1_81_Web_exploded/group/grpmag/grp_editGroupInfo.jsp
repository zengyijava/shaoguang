<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.employee.LfEmployee"%>
<%@page import="com.montnets.emp.entity.group.LfMalist"%>
<%@page import="com.montnets.emp.entity.group.LfUdgroup"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	List<LfEmployee> lfEmployeeList = (List<LfEmployee>)request.getAttribute("empList");
	@ SuppressWarnings("unchecked")
	List<LfMalist> malistList1 = (List<LfMalist>)request.getAttribute("malList1");
	@ SuppressWarnings("unchecked")
	List<LfMalist> malistList2 = (List<LfMalist>)request.getAttribute("malList2");
	
	LfUdgroup udg = (LfUdgroup)request.getAttribute("udg");
	if(udg == null)
	{
		udg = new LfUdgroup();
	}
	
	String udgId = (String)request.getAttribute("udgId");
	
	String lguserid = (String)request.getParameter("lguserid");
	
	//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	int plook = (btnMap.get(StaticValue.PHONE_LOOK_CODE) == null)?0:1;
	
	String menuCode = "1700-1500";
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String isDel = (String) request.getAttribute("isDel");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="group_ydbg_xzqz_text_selectsendobj" defVal="选择发送对象" fileName="group"/></title>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/group.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/group.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<style>
			#grp_editGroupInfo  #right p{
				height:26px;
			}
            #grp_editGroupInfo  div#gpName_div{
            	text-align: left;
            }
            #grp_editGroupInfo  input#addGpName{
            	width: 400px;
            }
            #grp_editGroupInfo  #right{
            	overflow-y: auto;
            }
            #grp_editGroupInfo  .md{
            	width: 40%;
            }
            #grp_editGroupInfo  .ld{
            	width: 30%;
            }
            #grp_editGroupInfo  .rd{
            	width: 29%;
            }
			#grp_editGroupInfo  .formatInfo{
				width: 39%;
			}
			#grp_editGroupInfo label#enterName{
				display: inline;
				color: #CCC;
				position: absolute;
				left: 125px;
				height: 43px;
				line-height: 43px;
				cursor: text;
				top: 36px;
			}
			#grp_editGroupInfo .group_span1 {
				margin-left: 10px;
			}
			#grp_editGroupInfo #gpName_label {
				margin-left: 70px;display: none;
			}
			#grp_editGroupInfo .group_table1 {
				margin:0 auto;border:0;
			}
			#grp_editGroupInfo .group_td1 {
				font-size:12px; line-height:30px;height:30px;text-align: left;
			}
			#grp_editGroupInfo #chooseType {
				float:left;margin-left:5px;margin-top:2px;margin-right: 2px;
			}
			#grp_editGroupInfo #epname {
				float:left;width:120px;height:21px;
			}
			#grp_editGroupInfo #btnSearch {
				margin-left: -1px;
			}
			#grp_editGroupInfo #etree {
				height: 224px;width:257px;display:block;
			}
			#grp_editGroupInfo #sonFrame {
				width: 257px; height: 224px;
			}
			#grp_editGroupInfo #egroup {
				display:none;height: 224px;width:257px;overflow:hidden;
			}
			#grp_editGroupInfo .group_div1 {
				height:10px;
			}
			#grp_editGroupInfo .group_div2 {
				float: left;text-align:center;width:99%;
			}
			#grp_editGroupInfo #left {
				width: 263px; font-size: 12px;padding:2px;vertical-align:middle;margin:-4px -6px;
			}
			#grp_editGroupInfo .group_div3 {
				padding-top: 5px;
			}
			#grp_editGroupInfo #prepage,#nextpage {
				visibility:hidden;
			}
			#grp_editGroupInfo #pagecode {
				color: blue;font-size: 12px;
			}
			#grp_editGroupInfo .group_div4 {
				height:15px;
			}
			#grp_editGroupInfo #adName {
				height:20px;line-height:20px;width:60px;border-right: 0px;
			}
			#grp_editGroupInfo #adPhone {
				height:20px;line-height:20px;width:165px;
			}
			#grp_editGroupInfo .div_bd.group_div5 {
				height: 25px;line-height: 25px;width:286px;display:block;border-bottom: none;border-top:none;
			}
			#grp_editGroupInfo .ld.div_bd,.md.div_bd,.rd.div_bd {
				height: 25px;line-height: 25px;
			}
			#grp_editGroupInfo .div_bd.group_div6 {
				height: 25px;line-height: 25px;width:286px;display:block;border-bottom: none;
			}
			
			.group_display_none{
				display:none;
			}
			 input, textarea { font-family: Helvetica, Arial; color: #000;}
            .placeholder {color: #aaa;}
            
            #efile .remindMsg{
				margin-top:20px;
			}
			#efile .remindMsg p{
			    font-size: 12px;
			    margin-left: 15px;
			    color: #999999;
			    float:left;
			    text-align: left;
			}
			            
		</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<style>
				#grp_editGroupInfo .epname_la {
					width: 120px;
					left: 110px;
				}
				#grp_editGroupInfo .placeholder.adName_la {
					left: 345px;
				}
				#grp_editGroupInfo .placeholder.adPhone_la {
					left: 380px;
					width: 150px;
				}
			
			</style>
		<%}%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/group_<%=empLangName%>.js"></script>
		<script>
			if('1' == '<%=isDel%>'){
				//alert('查询群组失败,该群组可能已删除!');
				alert(getJsLocaleMessage('group','group_js_editGroupInfo_selectfalse'));
				window.parent.location.reload();
			}
		</script>
	</head>
	<body onload="setcount()" id="grp_editGroupInfo">
	<div id="gpName_div" class="div_bg">
		<span class="group_span1" ><emp:message key="group_ydbg_xzqz_text_groupname" defVal="群组名称" fileName="group"/>：</span>
		<input type="text" name="addGpName" id="addGpName" value="<%=udg.getUdgName()==null?"":udg.getUdgName() %>" maxlength="18"  onkeyup = "checkText($(this))"/>
		<label id="gpName_label"  for="addGpName">
			<emp:message key="group_ydbg_xzqz_text_entergroupname" defVal="请输入群组名称" fileName="group"/>
		</label>
	</div>
			<input type="hidden" id="curId" name="curId" value="<%=udgId %>"/>
			<input type="hidden" id="curName" name="curName" value="<%=udg.getUdgName()==null?"":udg.getUdgName() %>"/>
			<input type="hidden" id="ipath" value="<%=path%>"/>	
			<input type="hidden" id="plook" name="plook" value="<%=plook %>"/>
			<input type="hidden" id="depId" name="depId" value=""/>
			<input type="hidden" id="depName" name="depName" value=""/>
			<input type="hidden" id="epno" name="epno" value=""/>
			<input type="hidden" id="zjcur" name="zjcur" value=""/>
			<input type="hidden" id="ygcur" name="ygcur" value=""/>
			<input type="hidden" id="qzStr" name="qzStr" value=","/>
			<input type="hidden" id="zjStr" name="zjStr" value=","/>
			<input type="hidden" id="gxStr" name="gxStr" value=","/>
			<input type="hidden" id="ygStr" name="ygStr" value=","/>
			<input type="hidden" id="addType" name="addType" value="3"/>
			<%-- 默认是新增分页信息 中第一页 --%>
			<input type="hidden" id="pageIndexAdd" value="1"/>		
			<table cellspacing="0" cellpadding="0" class="group_table1">
				<tr>
					<td class="group_td1">
						<center><select id="chooseType" onchange="changeChooseType(this.value)">
							<option value="1"><emp:message key="group_ydbg_xzqz_text_memberaddbook" defVal="员工通讯录" fileName="group"/></option>
							<option value="2"><emp:message key="group_ydbg_xzqz_text_personalgroup" defVal="个人群组" fileName="group"/></option>
							<% if(btnMap.get(menuCode+"-4")!=null) {  %><option value="3"><emp:message key="group_ydbg_xzqz_text_importfile" defVal="文件导入" fileName="group"/></option><%} %>
						</select>
						<input type=text" name="epname" id="epname" class="input_bd" value="" maxlength="20" onkeyup = "checkText($(this))"/>
						<label id="enterName" for="epname"><emp:message key="group_ydbg_xzqz_text_entername" defVal="请输入姓名" fileName="group"/></label>
						<a  id="btnSearch" href="javascript:dosearch()"></a>
						</center>
					</td>
					<td></td>
					<td>&nbsp;&nbsp;<emp:message key="group_ydbg_xzqz_text_added" defVal="已添加" fileName="group"/>&nbsp;<b id="numcount">0</b>&nbsp;<emp:message key="group_ydbg_xzqz_text_peploe" defVal="人" fileName="group"/></td>
				</tr>
				<tr>
					<td width="267" height="410px;" valign="top">
						<center>
							<div id="etree" class="div_bd">
								<iframe id="sonFrame" frameborder="0" src="<%=iPath %>/grp_addrbookDepTree1.jsp?lguserid=<%=lguserid %>"></iframe>
							</div>
							<div id="egroup" class="div_bd">
							</div>
							<div id="efile" class="div_bd">
								<div class="group_div1"></div>
								<input type="file" name="drfile" id="drfile"  onchange="doupload()"/>
								<div  class="btnClass3" id="fileBtn" ><emp:message key="group_ydbg_xzqz_text_uploadfile" defVal="上传文件" fileName="group"/></div>
								<!-- 
								<span id="sileSpan" >
									<emp:message key="group_ydbg_xzqz_text_fileformat" defVal="只支持上传.txt和.xls文件" fileName="group"/>
								</span> 
								-->
								<div class='remindMsg'>
									<p><emp:message key="group_ydbg_xzqz_text_fileformat1" defVal="1.批量添加只支持上传.txt/.xls/.xlsx文件。" fileName="group"/></p>
									<p><emp:message key="group_ydbg_xzqz_text_fileformat2" defVal="2.单个文件上传有效数据不能超过2000条。" fileName="group"/></p>
									<p><emp:message key="group_ydbg_xzqz_text_fileformat3" defVal="3.最多上传五个文件。" fileName="group"/></p>
									<p><emp:message key="group_ydbg_xzqz_text_fileformat4" defVal="4.错误格式数据(名字包含特殊符号等)将会被自动过滤。" fileName="group"/></p>
									<p><emp:message key="group_ydbg_xzqz_text_fileformat5" defVal="5.具体填写见格式图例(左为.txt文件，右为.xls/.xlsx文件)。" fileName="group"/></p>
								</div>
								<div id="upts"></div>
							<div class="formatText"><emp:message key="group_ydbg_xzqz_text_formatdes" defVal="格式说明" fileName="group"/></div>
								<div  class="formatInfo">
								<%--注：批量添加只支持上传.txt和.xls文件，.txt文档内容的格式为：姓名 号码<br/>
		 					   	姓名和号码以空格隔开，每个通讯录信息为一行。
		 					   	.xls文档内第一个单元格为姓名，第二个单元格为号码，每行为一条记录。
		 					   	错误格式数据将会被过滤 --%>
		 					   	<div class="pic_div">
		 					   		<div>.txt&nbsp;<emp:message key="group_ydbg_xzqz_text_document" defVal="文档" fileName="group"/></div>
		 					   		<img src="<%=commonPath%>/common/img/gp_upload_intro1_<%=empLangName%>.png"/>
		 					   	</div>
		 					   	<div class="pic_div">
		 					   		<div>.xls&nbsp;<emp:message key="group_ydbg_xzqz_text_document" defVal="文档" fileName="group"/></div>
		 					   		<img src="<%=commonPath%>/common/img/gp_upload_intro2_<%=empLangName%>.png"/>
		 					   	</div>
		 					   	<div class="group_div2"><emp:message key="group_ydbg_xzqz_text_filemaxsize" defVal="单个文件上传有效数据不能超过2000条" fileName="group"/></div>
		 					   	</div>
							</div>
							<div id="div_sel"  class="div_bd div_bg">
								&nbsp;&nbsp;<emp:message key="group_ydbg_ygtxlgl_text_memberlist" defVal="成员列表" fileName="group"/>：
							</div>
							<span class="group_display_none" id="showUserName"></span>
							<div class="div_bd" id="left_div">
								<select  multiple name="left" id="left" ondblclick="" size="9">
								</select>
							</div>
							<div class="group_div3">
								<input class="btnClass1" type="button" id="prepage" value="<emp:message key="group_common_text_prepage" defVal="上一页" fileName="group"/>"  onclick="javascript:prePage();">
								<input class="btnClass1" type="button" id="nextpage" value="<emp:message key="group_common_text_nextpage" defVal="下一页" fileName="group"/>"  onclick="javascript:nextPage();">
								&nbsp;&nbsp;&nbsp;&nbsp;
								<label id="pagecode"></label>
							</div>
						</center>
					</td>
					<td width="60" align="center"  valign="middle">
						<%--<input class="btnClass1" type="button" id="toLefts" value="&gt;&gt;" onclick="javascript:moveallLeft();">
						<div style="height:15px;"></div> --%>
						<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="group_common_opt_add" defVal="添加" fileName="group"/>" onclick="javascript:moveLeft();">
						<div class="group_div4"></div>
						<%-- <input class="btnClass1" type="button" id="toRight" value="&lt;" onclick="javascript:moveRight();"> --%>
						<input class="btnClass1" type="button" id="toRights" value="<emp:message key="group_common_opt_delete" defVal="删除" fileName="group"/>" onclick="javascript:moveRight()">
					</td>
					<td width="226" valign="top" >
					<center>
					<%
					int width = 378;
					if(btnMap.get(menuCode+"-5")!=null) {
						width = 350;
						%>
						<div  class="div_bd div_bg" id="right_search">
							<input type=text" class="input_bd" name="adName" id="adName" value='<emp:message key="group_ydbg_ygtxlgl_text_name" defVal="姓名" fileName="group"/>' maxlength="20" onkeyup = "checkText($(this))"/>

							<input type=text" class="input_bd" name="adPhone" id="adPhone" value='<emp:message key="group_ydbg_xzqz_text_phonenumber" defVal="手机号" fileName="group"/>' maxlength="21"  onkeyup = "checkText($(this));phoneInputCtrl($(this))"/>

								<a id="btnAdd" href="javascript:doAdd()"></a>
						</div>
						<div  class="div_bd group_div5">
							<span class="ld div_bd"><emp:message key="group_ydbg_xzqz_text_nikename" defVal="名称" fileName="group"/></span>
							<span class="md div_bd"><emp:message key="group_ydbg_xzqz_text_number" defVal="号码" fileName="group"/></span>
							<span class="rd div_bd"><emp:message key="group_ydbg_xzqz_text_src" defVal="来源" fileName="group"/></span>
						</div>
						<%}else{%>
						<div  class="div_bd group_div6">
							<span class="ld div_bd"><emp:message key="group_ydbg_xzqz_text_nikename" defVal="名称" fileName="group"/></span>
							<span class="md div_bd"><emp:message key="group_ydbg_xzqz_text_number" defVal="号码" fileName="group"/></span>
							<span class="rd div_bd"><emp:message key="group_ydbg_xzqz_text_src" defVal="来源" fileName="group"/></span>
						</div>
						<%} %>

							<div id="right" class="div_bd" style="	height: <%=width+4 %>px !important;height: <%=width %>px;border-top:none">
						<%
						CommonVariables cv = new CommonVariables();
						int count = 0;
						String phone;
						if(lfEmployeeList != null && lfEmployeeList.size() > 0)
						{
							for(LfEmployee employee : lfEmployeeList)
							{
								if(plook == 0)
								{
									phone = cv.replacePhoneNumber(employee.getMobile());
								}else
								{
									phone = employee.getMobile();
								}
								count++;
								out.print("<p title="+employee.getName()+" onclick='cliselp($(this))'  ondblclick='moveme($(this))' iscur='1' etype='1' pva='"+ employee.getGuId()+"'>"
										+"<span class='ld  div_bd' >"+ employee.getName()+"</span>"
										+"<span class='md  div_bd' >"+ phone +"</span>"
										+"<span class='rd  div_bd' >" + MessageUtils.extractMessage("group","group_ydbg_xzqz_text_employee",request) + "</span></p>");
							}
						}
						if(malistList1 != null && malistList1.size() > 0)
						{
							for(LfMalist malist : malistList1)
							{
								phone = malist.getMobile();
								/*
								if(plook == 0)
								{
									phone = cv.replacePhoneNumber(malist.getMobile());
								}else
								{
									phone = malist.getMobile();
								}*/
								count++;
								out.print("<p title="+malist.getName()+" onclick='cliselp($(this))' ondblclick='moveme($(this))' iscur='1' etype='2' pva='"
										+ malist.getGuId()+"' pva2='"+malist.getName()+"|"+malist.getMobile()+"'>"
										+"<span class='ld  div_bd' >"+ malist.getName()+"</span>"
										+"<span class='md  div_bd' >"+ malist.getMobile()+"</span>"
										+"<span class='rd  div_bd' >" + MessageUtils.extractMessage("group","group_ydbg_xzqz_text_selfbuilt",request) + "</span></p>");
							}
						}
						if(malistList2 != null && malistList2.size() > 0)
						{
							for(LfMalist malist : malistList2)
							{
								if(plook == 0)
								{
									phone = cv.replacePhoneNumber(malist.getMobile());
								}else
								{
									phone = malist.getMobile();
								}
								count++;
								out.print("<p title="+malist.getName()+" ondblclick='moveme($(this))' iscur='1' etype='4' pva='"+ malist.getGuId()+"'>"
										+"<span class='ld  div_bd' >"+ malist.getName()+"</span>"
										+"<span class='md  div_bd' >"+ phone+"</span>"
										+"<span class='rd  div_bd' >" + MessageUtils.extractMessage("group","group_ydbg_xzqz_text_shared",request) + "</span></p>");
							}
						}
						%>
						</div>
						<input type="hidden" value="<%=count %>" id="count"/>
						</center>
					</td>
					</tr>
		</table>
		<script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=path%>/common/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/editGroupInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
		 $(function() {
                $('input, textarea').placeholder();
            });
			$(":text").each(function(){
				$(this).attr("holder",$(this).val()).css("color","gray");
			}).focus(function(){
				var holder = $(this).attr("holder");
				var value = $.trim($(this).val());
				if(value == holder)
					$(this).val("");
			}).blur(function(){
				var holder = $(this).attr("holder");
				var value = $.trim($(this).val());
				if(value == "")
					$(this).val(holder);
			});
		</script>
	</body>
</html>