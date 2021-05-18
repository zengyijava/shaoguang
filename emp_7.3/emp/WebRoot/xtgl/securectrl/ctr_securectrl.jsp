<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.securectrl.vo.LfMacIpVo"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	//当前登录操作员
	LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("securectrl");
	menuCode = menuCode==null?"0-0-0":menuCode;
	//ip-mac绑定集合
	@ SuppressWarnings("unchecked")
	List<LfMacIpVo> macipVoList=(List<LfMacIpVo>)request.getAttribute("macipVoList");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	@ SuppressWarnings("unchecked")	
	Map<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	if(conditionMap==null)
	{
		conditionMap = new LinkedHashMap<String, String>();
	}
%>
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"	src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/ctr_securectrl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
				
	</head>

	<body id="ctr_securectrl" onload="show()">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<div id="container" class="container">
			<div id="loginUser" class="hidden"></div>
			
			<%-- 内容开始 --%>
				<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
				<div class="left_dep div_bd" align="left" >
					<input type="hidden" id="id" />
					<h3 class="div_bd title_bg">
						<emp:message key="xtgl_czygl_gjaqsz_cyzjg" defVal="操作员机构" fileName="xtgl"/>
					</h3>
					<div class="list">
						<div id="depTree"><jsp:include page="ctr_depMacIpTree.jsp"></jsp:include>  </div>	
					</div>
				</div>
				
				
				<div class="right_info toggleDiv_up_div"  >
				<form name="pageForm" action="" method="post"  autocomplete="off">
					<div class="buttons">
						<input type="hidden" id="depTreeId" value=""/>
						<input type="hidden" id="servletUrl" value="ctr_securectrl.htm"/>
								<div id="toggleDiv" >
<%--									<img id="searchIcon" style="cursor: pointer;"  src="/images/toggle_collapse.png" title="展开查询条件"/>--%>
								</div>
									<%if(btnMap.get(menuCode+"-1")!=null){ %>
									<a id="upload" href="javascript:fileBind();"><emp:message key="xtgl_czygl_gjaqsz_dr" defVal="导入" fileName="xtgl"/></a>
									<a id="bindIpMac" href="javascript:toBindIpMac();"><emp:message key="xtgl_czygl_gjaqsz_bd" defVal="绑定" fileName="xtgl"/>IP/MAC</a>
									<%} %>
									<%if(btnMap.get(menuCode+"-1")!=null){ %>
									<a id="enablePwd" href="javascript:batchEnablePwd();"><emp:message key="xtgl_czygl_gjaqsz_qydtkl" defVal="启用动态口令" fileName="xtgl"/></a>
									<%} %>
									<%if(btnMap.get(menuCode+"-3")!=null){ %>
									<a id="cancelBind" href="javascript:toCancelBind();"><emp:message key="xtgl_czygl_gjaqsz_jcbd" defVal="解除绑定" fileName="xtgl"/></a>
									<%} %>
							</div>
					<div id="condition">
								<table>
								<tbody>
								<tr>
									<td><emp:message key="xtgl_cswh_whgl_dlzh" defVal="登录账号" fileName="xtgl"/>：</td>
										<td>
											<input type="text" name="userName" id="userName" value="<%=conditionMap.get("userName")==null?"":conditionMap.get("userName") %>" maxlength="20"/>
										</td>
										<td><emp:message key="xtgl_cswh_whgl_czymc_mh" defVal="操作员名称：" fileName="xtgl"/></td>
										<td>
											<input type="text" name="name" id="name" value="<%=conditionMap.get("name")==null?"":conditionMap.get("name") %>" maxlength="20"/>
										</td>
										<td class="tdSer">
											<center><a id="search"></a></center>
										</td>
										</tr>
								<tr>
											<td><emp:message key="xtgl_czygl_gjaqsz_ipdz_mh" defVal="IP地址：" fileName="xtgl"/></td>
											<td>
												<input type="text" name="ipaddr" id="ipaddr" value="<%=conditionMap.get("ipaddr")==null?"":conditionMap.get("ipaddr") %>" maxlength="20"/>
											</td>
										    <td><emp:message key="xtgl_czygl_gjaqsz_macdz_mh" defVal="MAC地址：" fileName="xtgl"/></td>
											<td>
												<input type="text" name="macaddr" id="macaddr" value="<%=conditionMap.get("macaddr")==null?"":conditionMap.get("macaddr") %>" maxlength="20"/>
											</td>
											<td></td>
										    	
									</tr>
									<tr>
									<td><emp:message key="xtgl_czygl_gjaqsz_ipdzzt_mh" defVal="IP地址状态：" fileName="xtgl"/></td>
									<td>
									<%String isBindIp = conditionMap.get("isBindIp"); %>
										<select name="isBindIp" class="isBindIp" id="isBindIp">
										<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
										<option value="1" <%if("1".equals(isBindIp)) {%>selected="selected"<%}%>><emp:message key="xtgl_czygl_gjaqsz_ybd" defVal="已绑定" fileName="xtgl"/></option>
										<option value="0" <%if("0".equals(isBindIp)) {%>selected="selected"<%}%>><emp:message key="xtgl_czygl_gjaqsz_wbd" defVal="未绑定" fileName="xtgl"/></option>
										</select>
									</td>
									<td><emp:message key="xtgl_czygl_gjaqsz_macdzzt_mh" defVal="MAC地址状态：" fileName="xtgl"/></td>
									<td>
									<%String isBindMac = conditionMap.get("isBindMac"); %>
										<select  name="isBindMac" class="isBindMac" id="isBindMac">
										<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
										<option value="1" <%if("1".equals(isBindMac)) {%>selected="selected"<%}%>><emp:message key="xtgl_czygl_gjaqsz_ybd" defVal="已绑定" fileName="xtgl"/></option>
										<option value="0" <%if("0".equals(isBindMac)) {%>selected="selected"<%}%>><emp:message key="xtgl_czygl_gjaqsz_wbd" defVal="未绑定" fileName="xtgl"/></option>
										</select>
									</td>
									<td></td>
								</tr>
								<tr>
								<td><emp:message key="xtgl_czygl_gjaqsz_dtklzt_mh" defVal="动态口令状态：" fileName="xtgl"/></td>
										   <td>
										   <%String isBindPwd = conditionMap.get("isBindPwd"); %>
												<select name="isBindPwd"  class="isBindPwd" id="isBindPwd">
												<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
												<option value="1" <%if("1".equals(isBindPwd)) {%>selected="selected"<%}%>><emp:message key="xtgl_czygl_gjaqsz_qy" defVal="启用" fileName="xtgl"/></option>
												<option value="0" <%if("0".equals(isBindPwd)) {%>selected="selected"<%}%>><emp:message key="xtgl_czygl_gjaqsz_wqy" defVal="未启用" fileName="xtgl"/></option>
												</select>
											</td>
									<td colspan="3"></td>
								</tr>
								</tbody>
								</table>
							</div>
					<div class="tableInfo_up_div"></div>			
					<div id="tableInfo">
					</div>
					</form>
				</div>
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
		</div>
		<div class="clear"></div>
			 <div id="ipBindDiv" class="ipBindDiv">
	    	<form action="<%=request.getContextPath() %>/ctr_securectrl.htm?method=update" method="post" id="macipAdd" name="macipAdd" >
						<div id="loginUser2" class="hidden"></div>
						<input type="hidden" id="inheritPath" value="<%=inheritPath %>"/>
						<input type="hidden" id="ids" name="ids" value=""/>
						<input type="hidden" id="opType" name="opType" value="add"/>
						<input type="hidden" id="ipaddrs" name="ipaddrs"/>
						<input type="hidden" id="guid" name="guid"/>
						<input type="hidden" id="macaddrs" name="macaddrs"/>
				    <%-- 		<label style="color:red">
						规则：IP或MAC地址不为空时，默认启用不为空的登录验证方式，两者均不为空时，同时启用。<br>
						注：IP地址与MAC地址不存在一对一组合绑定关系。
						</label>
						<br/>
						 --%>
					<center>
					<div class="pwd_up_div">
						<div class="pwd" id="pwd">
							<label class="dtkl_mh"><emp:message key="xtgl_czygl_gjaqsz_dtkl_mh" defVal="动态口令：" fileName="xtgl"/></label>
							<input type="radio" name="dtpwd" value="1"  /><label><emp:message key="xtgl_czygl_gjaqsz_qy" defVal="启用" fileName="xtgl"/></label>
							<input type="radio" name="dtpwd" value="0" /><label><emp:message key="xtgl_czygl_gjaqsz_wqy" defVal="不启用" fileName="xtgl"/></label>
						</div>
						<div class="ipdz_div" align="center">
							<label class="<%=StaticValue.ZH_HK.equals(langName)?"ipdz_label_1":"ipdz_label_2"%>" ><emp:message key="xtgl_czygl_gjaqsz_ipdz" defVal="IP地址" fileName="xtgl"/></label>
							<input type="button" class="btnClass2" value="<emp:message key='xtgl_czygl_gjaqsz_tjl' defVal='添加栏' fileName='xtgl'/>" onclick="javascript:addAddr('ip');"/> 
							<label class="<%=StaticValue.ZH_HK.equals(langName)?"macdz_label_1":"macdz_label_2"%>"  ><emp:message key="xtgl_czygl_gjaqsz_macdz" defVal="MAC地址" fileName="xtgl"/></label>
							<input type="button" class="btnClass2"  value="<emp:message key='xtgl_czygl_gjaqsz_tjl' defVal='添加栏' fileName='xtgl'/>" onclick="javascript:addAddr('mac');"/> 
						</div>
						<div class="ipmacTable_div">
						<table class="ipmacTable" id="ipmacTable">
							<tr id="ip1tr"><td class="ip1tr_td">1.</td>
								<td>
								<label><input type="text" name="ip1" id="ip1" class="input_bd" value="" maxlength="15" /></label>
							</td></tr>
							<tr id="ip2tr"><td>2.</td>
								<td>
								<label><input type="text" name="ip2" id="ip2"  class="input_bd" value="" maxlength="15"/></label>
							</td></tr>
							<tr class="trVisible" id="ip3tr"><td>3.</td>
								<td>
								<label><input type="text" name="ip3" id="ip3"  class="input_bd" value="" maxlength="15"/></label>
							</td></tr>
							<tr class="trVisible" id="ip4tr"><td>4.</td>
								<td>
								<label><input type="text" name="ip4" id="ip4"  class="input_bd" value="" maxlength="15"/></label>
							</td></tr>
							<tr class="trVisible" id="ip5tr"><td>5.</td>
								<td>
								<label><input type="text" name="ip5" id="ip5"  class="input_bd" value="" maxlength="15"/></label>
							</td></tr>
							<tr class="trVisible" id="ip6tr"><td>6.</td>
								<td>
								<label><input type="text" name="ip6" id="ip6"  class="input_bd" value="" maxlength="15"/></label>
							</td></tr>
							<tr class="trVisible" id="ip7tr"><td>7.</td>
								<td>
								<label><input type="text" name="ip7" id="ip7"  class="input_bd" value="" maxlength="15"/></label>
							</td></tr>
							<tr class="trVisible" id="ip8tr"><td>8.</td>
								<td>
								<label><input type="text" name="ip8" id="ip8"  class="input_bd" value="" maxlength="15"/></label>
							</td></tr>
							<tr class="trVisible" id="ip9tr"><td>9.</td>
								<td>
								<label><input type="text" name="ip9" id="ip9"  class="input_bd" value="" maxlength="15"/></label>
							</td></tr>
							<tr class="trVisible" id="ip10tr"><td>10.</td>
								<td>
								<label><input type="text" name="ip10" id="ip10"  class="input_bd" value="" maxlength="15"/></label>
							</td></tr>
						
						</table>
						</div>
						<div class="ipmacTable_div">
						<table align="right" id="ipmacTable" class="ipmacTable">
						<tr id="mac1tr"><td align="right" >1.</td>
							<td >
							<label><input type="text" name="mac1" id="mac1"  class="input_bd" value="" maxlength="17" /></label>
						</td></tr>
						<tr id="mac2tr"><td align="right">2.</td>
							<td>
							<label><input type="text" name="mac2" id="mac2"  class="input_bd" value="" maxlength="17"/></label>
						</td></tr>
						<tr class="trVisible" id="mac3tr"><td align="right">3.</td>
							<td>
							<label><input type="text" name="mac3" id="mac3"  class="input_bd"  value="" maxlength="17"/></label>
							</td>
						</tr>
						<tr class="trVisible" id="mac4tr"><td align="right">4.</td>
							<td>
							<label><input type="text" name="mac4" id="mac4"  class="input_bd" value="" maxlength="17"/></label>
						</td></tr>
						<tr class="trVisible" id="mac5tr"><td align="right">5.</td>
							<td>
							<label><input type="text" name="mac5" id="mac5"  class="input_bd" value="" maxlength="17"/></label>
						</td></tr>
						<tr class="trVisible" id="mac6tr"><td align="right">6.</td>
							<td>
							<label><input type="text" name="mac6" id="mac6"  class="input_bd" value="" maxlength="17"/></label>
						</td></tr>
						<tr class="trVisible" id="mac7tr"><td align="right">7.</td>
							<td>
							<label><input type="text" name="mac7" id="mac7"  class="input_bd" value="" maxlength="17"/></label>
						</td></tr>
						<tr class="trVisible" id="mac8tr"><td align="right">8.</td>
							<td>
							<label><input type="text" name="mac8" id="mac8"  class="input_bd" value="" maxlength="17"/></label>
						</td></tr>
						<tr class="trVisible" id="mac9tr">
							<td align="right">9.</td>
							<td>
							<input type="text" name="mac9" id="mac9"  class="input_bd" value="" maxlength="17"/>
						</td></tr>
						<tr class="trVisible" id="mac10tr"><td align="right">10.</td>
							<td >
							<input type="text" name="mac10" id="mac10"  class="input_bd" value="" maxlength="17"/>
						</td></tr>
						</table>
						</div>
						</div>
					</center>
					<hr class="div_bd">
					<center>
			   			<div class="qd_div">
			   		 	 	<input type="button" onclick="javascript:manualAdd();" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" class="btnClass5 mr23" />
	                        <input type="button" onclick="javascript:$('#ipBindDiv').dialog('close')" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" class="btnClass6" />
	                        <br/>
			   			</div>
			   		</center>
				</form>
	   	 </div>
	   	 <div class="fileBindDiv" id="fileBindDiv">
	                <form id="uploadForm" action="ctr_securectrl.htm?method=upload" method="post" enctype="multipart/form-data">
		           <div id="loginUser3" class="hidden"></div>
		            <center>
		            <br/>
		            <br/>
		            <br/>
		           		<table class="xzwj_mh_table">
		           		<thead>
	  					  <tr><%--
	   				 		 <td  width="70px"><span style="text-align: right;"><emp:message key="xtgl_czygl_gjaqsz_xzwj_mh" defVal="选择文件：" fileName="xtgl"/></span></td>
	   						 <td style="margin-left: -10px;"><a href="javascript:;" class="a-upload"><input type="file" name="uploadName" id="uploadFile"><emp:message key="xtgl_cswh_gxhjmsz_ll" defVal="浏览" fileName="xtgl"/></a><div id="fileDisplay"></div></td>
	 					  	 --%>
	 					  	 <td>
	 					  	 	<emp:message key="xtgl_czygl_gjaqsz_xzwj_mh" defVal="选择文件：" fileName="xtgl"/>
	 					  	 </td>
	 					  	 <td>
	 					  	 	<span class="filepath_span">
									<input class="filepath"  maxlength="16"  type="text" id="filepath" readonly>
									<input class="uploadFile" type="file" id="uploadFile" name="uploadName" onchange="filepath.value=this.value;this.title=this.value"/>			
								</span>
	 					  	 </td>
	 					  </tr>
	 					  <tr class="uploadFile_down_tr"><td colspan="2"></td></tr>
	 					  <tr>
	 					 
	 					  	<td colspan="2" align="left">
	 					  	<a href="javascript:download_href('<%=path %>/down.htm?filepath=xtgl/securectrl/file/temp/ipMacTem_<%=langName %>.xls')" class="zzcxlslx_a"><emp:message key="xtgl_czygl_gjaqsz_zzcxlslx" defVal="只支持xls类型的Excel文件上传，点击下载模板" fileName="xtgl"/></a>
	 					  	</td>
	 					  </tr>
	 					   <tr class="kwComments_up_tr"><td colspan="2"></td></tr>
	  					  <tr>
	   						 <td colspan=2  id="btn" align="center">
	   						  <input type="hidden" value="" id="kwComments"/>
	   						 <input type="button" id="kwsok" onclick="javascript:checkUpload()" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='导入' fileName='xtgl'/>" class="btnClass5 mr23" />
                        <input type="button" id="kwsc" onclick="javascript:back()" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" class="btnClass6" />
                        <br/>
	 					  </tr>
	 					  </thead>
						</table>
		            	</center>
		            </form>
            </div>
            <div class="cancelipmacDiv" id="cancelipmacDiv">
			   		<label> <input type="checkbox" name="cancelType" value="1"/>&nbsp;<emp:message key="xtgl_czygl_gjaqsz_jcipdzbd" defVal="解除IP地址绑定" fileName="xtgl"/></label>
					<label><input type="checkbox" name="cancelType" value="2"/>&nbsp;<emp:message key="xtgl_czygl_gjaqsz_jcmacdzbd" defVal="解除MAC地址绑定" fileName="xtgl"/></label>
			   		<label><input type="checkbox" name="cancelType" value="3"/>&nbsp;<emp:message key="xtgl_czygl_gjaqsz_jcdtkl" defVal="解除动态口令" fileName="xtgl"/></label>
			   		
			   			<div class="qveding_div">
			   		 	 	<input type="button" onclick="javascript:cancelIpMac()" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" class="btnClass5 left mr10"/>
	                        <input type="button" onclick="javascript:ipmacBtCancel()" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" class="btnClass6 left" />
			   			</div>
			   		
            </div>
            <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
	        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
			<script type="text/javascript" src="<%=iPath %>/js/macip.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
			<script type="text/javascript" src="<%=iPath %>/js/ctr_securectrl.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
			<script type="text/javascript">
			$(document).ready(function(){
				setLeftHeight();
				getLoginInfo("#loginUser");
				getLoginInfo("#loginUser2");
				getLoginInfo("#loginUser3");
				$('#tableInfo').load('ctr_securectrl.htm?tttt=<%=System.currentTimeMillis()%>',{
				method:'getTable',lgguid:$("#lgguid").val(),lgcorpcode:$("#lgcorpcode").val(),isOperateRetrun:<%=request.getAttribute("isOperateRetrun")%>});
				
				$(".a-upload").bind("change","input[type='file']",function(){
					
					//var filePath=$(this).val();
					var filePath=$("#uploadFile").val();
					$("#fileDisplay").html(filePath);
				});
			});
			function show(){
				<% 
				String result=(String)session.getAttribute("ctrResult");
				if(result != null){
					session.removeAttribute("ctrResult");
				}
				
				if (result!=null && result.equals("true")){%>
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_38"));
				<%}else if (result!=null && result.startsWith("upload")){%>
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_1")+"<%=result.substring(6)%>"+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_2"));
				<%}else if (result!=null && result.equals("noRecord")){%>
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_3"));
				<%}else if(result!=null && (result.equals("errorFile"))){%>
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_4"));
				<%}else if(result!=null && (result.equals("false") || result.equals("error"))){%>
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
				<%}else if(result != null && result.equals("noSpUser")){%>
				    alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_5"));
				<%}else if(result != null && result.equals("noPhone")){%>
				    alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_6"));
				<%}%>
			}
			</script>
	</body>
</html>