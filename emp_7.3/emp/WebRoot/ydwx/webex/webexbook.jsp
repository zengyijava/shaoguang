<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%--@ page import="com.montnets.emp.employee.vo.LfEmployeeTypeVo"--%>
<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	@ SuppressWarnings("unchecked")
	//List<LfEmployeeTypeVo> optionList = (List<LfEmployeeTypeVo>)request.getAttribute("optionList");
	
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
	String username = request.getParameter("lgusername");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default": (String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute("emp_lang");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/addrBook.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css">
		<link href="<%=commonPath %>/ydwx/webex/css/uploadFile.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<style type="text/css">
			input#vdiosubmit,input#filesubmit{
				text-align: center;
				text-indent: 0px;
			}
			#subform1 > table > tbody > tr > td{
				width: 100px;
			}
		</style>
		<%}%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style type="text/css">
			.buttons a#addeployee{width: 60px;}
		</style>
		<%}%>
		<link href="<%=skin %>/newjqueryui.css" rel="stylesheet" type="text/css" >
		<link href="<%=iPath %>/css/webexbook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>"/>
        <script>
        var iPath="<%=iPath %>";
        var addcode="<%=addcode%>";
        var delcode="<%=delcode%>";
        var path="<%=path %>";
        </script>
	</head>
	<body onload="submitForm()" id="ydwx_webexbook">
	<input type="hidden" id="empLangName" value="<%=empLangName%>"/>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<%
	if(CstlyeSkin.contains("frame4.0")){
	%>
		<input id='hasBeenBind' value='1' type='hidden'/>
	<%
		}
	 %>
		
			<%-- header开始 --%>
			<%=ViewParams.getPosition(empLangName,menuCode) %>
			<%-- header结束 --%>
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
			 <div id="frame_main">
				<div class="left_dep div_bd" align="left" >
					<input type="hidden" id="id" />
					<h3 class="div_bd title_bg">
		         	<emp:message key="ydwx_wxgl_wxsc_liexing" defVal="素材类型 " fileName="ydwx"></emp:message>
					</h3>
					<div id="depOperate" class="depOperate">
						<span id="delDepNew" class="depOperateButton3" onclick="doDel()" title="<emp:message key="common_delete" defVal="删除" fileName="common"></emp:message>"></span>
					    <span id="updateDepNew" class="depOperateButton2" onclick="updateDepFun()" title="<emp:message key="ydwx_common_btn_chongmingming" defVal="重命名" fileName="ydwx"></emp:message>"></span>
						<span id="addDepNew" class="depOperateButton1" onclick="addDepFun()" title="<emp:message key="common_newlyIncrease" defVal="新增" fileName="common"></emp:message>"></span>
					</div>
					<div id="etree" class="list ydwx_etree">
					</div>
				</div>
				
				<div class="right_info">
				<form name="pageForm" action="" method="post">
				<div id="getloginUser" style="display:none;"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
					  	<a id="addeployee" onclick="javascript:showAddSucai()">
					  	<emp:message key="ydwx_wxgl_wxsc_tianjia" defVal="添加素材" fileName="ydwx"></emp:message></a>
						<%-- <a id="changedep" onclick="javascript:showAddVod()">上传视频</a> --%>
						<a id="delepl" onclick="javascript:delBk()">
						<emp:message key="ydwx_wxgl_wxsc_shanchu" defVal="删除素材" fileName="ydwx"></emp:message></a>
					</div>
					<input type="hidden" id="servletUrl" value="<%=path %>/wx_attached.htm?method=getTable"/>
					<input type="hidden" id="delUrl" value="<%=path %>/wx_attached.htm?method=doDelsucai"/>
					<input type="hidden" id="depId" value="<%=request.getAttribute("connDepId")==null?"":request.getAttribute("connDepId") %>" />
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td><emp:message key="ydwx_wxgl_wxsc_mingchens" defVal="素材名称：" fileName="ydwx"></emp:message></td>
									<td><input id="serahname" class="ydwx_serahname" type="text" maxlength="32"/></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									
									<td><emp:message key="ydwx_common_time_chuangjianshijians" defVal="创建时间：" fileName="ydwx"></emp:message></td>
									<td>
										<input type="text" value='' id="submitSartTime" name="submitSartTime"
										class="Wdate ydwx_submitSartTime" readonly="readonly" onclick="stime()">
									</td>
									<td><emp:message key="ydwx_common_time_zhi" defVal="至：" fileName="ydwx"></emp:message></td>
									<td>
										<input type="text" value='' id="submitEndTime" name="submitEndTime" 
										class="Wdate ydwx_submitEndTime" readonly="readonly" onclick="rtime()">
									</td>
									
									
									<td class="tdSer">
										<a id="search"></a>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
		
					<div style="clear:right"></div>	
					<div id="bookInfo">

					</div>
					
				</form>
				</div>
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
    <div id="changeDep" class="ydwx_changeDep">
			 <input type="hidden" id="changeDepId" >
			<div id="depDiv" style="">
				<ul id="dropdownMenu" class="tree"></ul>
			</div>
			<div class="ydwx_changeDep_sub2"></div>
			<center>
				<div>
					<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>" onclick="doSubmit()" class="btnClass5 mr23"/>
					<input type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" onclick="depTreeCancel()" class="btnClass6"/>
				</div>
			</center>
			  
	</div>
	
	<%-- sucai --%>
	<div id="createSucai" title="<emp:message key='ydwx_wxgl_wxsc_xianjian' defVal='新建素材' fileName='ydwx'></emp:message>" class="hidden ydwx_createSucai">
		<form id ="subform1" action="<%=path %>/FileUpload.htm"  onsubmit="return startProgress();" method="post" enctype="multipart/form-data">
						
				<table width="100%" border="0" cellpadding="0" cellspacing="0" >
					<tr><td>&nbsp;</td></tr>
				 <tr>
   				  	<td nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;
   				  	<emp:message key="ydwx_wxgl_wxsc_mingchens" defVal="素材名称：" fileName="ydwx"></emp:message>
					</td>
					<td>
						<input id="AttachmentName" name="AttachmentName" type="text" class="ydwx_AttachmentName" /> <font color="red">*</font>
						（<emp:message key="ydwx_wxgl_wxsc_bitianxiang" defVal="必填项" fileName="ydwx"></emp:message>）
					</td>
  				 </tr>
  				 <tr><td>&nbsp;</td></tr>
  				 <tr>
   				  	<td nowrap="nowrap" style="display:none">&nbsp;&nbsp;&nbsp;&nbsp;
						<emp:message key="ydwx_wxgl_wxsc_banben" defVal="素材版本：" fileName="ydwx"></emp:message>
					</td>
					<td nowrap="nowrap" style="display:none">
						<input id="AttachmentName2" name="AttachmentName2" type="text" class="ydwx_AttachmentName2"/>
					</td>
  				 </tr>
 			   <tr>
   				  <td valign="top" >&nbsp;&nbsp;&nbsp;&nbsp;
   				  		<emp:message key="ydwx_wxgl_wxsc_miaoshus" defVal="素材描述：" fileName="ydwx"></emp:message>
				  </td>
				  <td>
					  <textarea name="AttachmentDescribe" id="AttachmentDescribe" cols="32" rows="5" class="ydwx_AttachmentDescribe"></textarea>
				  </td>
 			   </tr>
 			   
				<tr><td>&nbsp;</td></tr>
				 <tr>
   				  	<td nowrap="nowrap" >
  						 <div id="sstype">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="ydwx_wxgl_wxsc_liexings" defVal="素材类型：" fileName="ydwx"></emp:message>
				  	</td>
					<td>
						<input id="AttachType" name="AttachType"  class="ydwx_AttachType" disabled="true" />
						<input  type="hidden" id="AttachTypeNum" />
						<input  type="hidden" id="AttachType1" name="AttachType1" type="text"/>
					</td>
					<td nowrap="nowrap" style="display:none">
						<input id="nodeid" name="nodeid" type="text"/>
						<input id="filetype" name="filetype" type="text"/>
						<input id="scfication" name="scfication" type="text">
					</td>
 			   </tr>
				<tr><td>&nbsp;</td></tr>
				
				<tr>
					<td nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;
  						<emp:message key="ydwx_wxgl_wxsc_tianjias" defVal="添加素材：" fileName="ydwx"></emp:message>
				  	</td>
					<td>
						<a href="javascript:" class="a-upload">
							<input type="file" name="file1" onchange="givetype();" class="file" id="file_1" size="22"/>
							<emp:message key="ydwx_wxfs_dtwxfs_schwj" defVal="上传文件" fileName="ydwx"></emp:message>
						</a>
						<input class="showFileName" value="" disabled="disabled" style="display: none">
					</td>
				</tr>
				<tr>
					<td height="20px"></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;<font color="red" size="+1">*</font><%="zh_HK".equals(empLangName)?"Note:":"注："%><br/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="ydwx_wxgl_wxsc_xinxi1" defVal="1.其他(建议大小控制在3M以内)" fileName="ydwx"></emp:message><br/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="ydwx_wxgl_wxsc_xinxi2" defVal="2.视频(建议大小控制在3M以内，支持mp4,swf格式)" fileName="ydwx"></emp:message><br/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="ydwx_wxgl_wxsc_xinxi3" defVal="3.文件(建议大小控制在2M以内，支持txt,doc,xls格式)" fileName="ydwx"></emp:message><br/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="ydwx_wxgl_wxsc_xinxi4" defVal="4.图片(建议大小控制在1M以内，支持jpg,bmp,gif,img,png,jpeg格式)" fileName="ydwx"></emp:message>
					</td>
				</tr>
				<tr>
					<td colspan="2">
							<div id="cover"></div>
							<div id="progressBar" class="ydwx_progressBar">
								<div id="theMeter">
									<div id="progressBarText"></div>
									<div id="progressBarBox">
										<div id="progressBarBoxContent"></div>
									</div>
								</div>
						   </div>
					</td>
				</tr>
				<tr><td height="20px"></td></tr>
							<tr><td colspan="2" align="center">
								<input type="submit" id="filesubmit"  class="btnClass5 mr23" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>"/>
								<input type="button" onclick="endSucai()" class="btnClass6" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>"/>
								<br/>
							</td></tr>
			</table>
		</form>
						
	</div>
	<%-- ved --%>
	<div id="createVed" title="<emp:message key='ydwx_wxgl_wxsc_xianjian' defVal='新建素材' fileName='ydwx'></emp:message>" class="hidden ydwx_createVed">
						
			<form id="subform2" action="<%=path %>/VedFileUpload.htm"  onsubmit="return VedstartProgress();" method="post" enctype="multipart/form-data">
						
				<table width="100%" border="0" cellpadding="0" cellspacing="0" >
				<tr>
					<td>&nbsp;</td>
				</tr>
				  <tr>
				 	<td nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;
   				  		<emp:message key="ydwx_wxgl_wxsc_mingchens" defVal="素材名称：" fileName="ydwx"></emp:message>
				 	</td>
					<td>
						<input id="vedName" name="vedName" type="text" class="ydwx_vedName"/> <font color="red">*</font>
						（<emp:message key="ydwx_wxgl_wxsc_bitianxiang" defVal="必填项" fileName="ydwx"></emp:message>）
					</td>
  				 </tr>
  				 <tr>
					 <td>&nbsp;</td>
				 </tr>
  				 <tr>
   				  	<td nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;
   				  		<emp:message key="ydwx_wxgl_wxsc_miaoshus" defVal="素材描述：" fileName="ydwx"></emp:message>
				 	</td>
					<td>
						<textarea id="veddec" name="veddec"  cols="32" rows="5" class="ydwx_veddec"></textarea>
					</td>
  				 </tr>
  				 <tr><td>&nbsp;</td></tr>
  				 <tr>
   				  <td nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;
  						  <emp:message key="ydwx_wxgl_wxsc_liexings" defVal="素材类型：" fileName="ydwx"></emp:message>
				  </td>
				  <td>
					  <input id="VedioType" name="VedioType" class="ydwx_VedioType" disabled="true" />
					  <input  type="hidden" id="VedioType1" name="VedioType1" type="text"/>
				  </td>
				  <td nowrap="nowrap" style="display:none">
  				  		<input id="VedNodeid" name="VedNodeid" type="text"/>
  				  		<input id="Vedfication" name="Vedfication" type="text">
  				  </td>
 			   </tr>
 			   <tr><td>&nbsp;</td></tr>
				<tr>
					<td nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;
					<select class="ydwx_videoType_select" id="videoType">
						<option value ="1">MP4</option>
						<option value ="2">SWF</option>
					</select>：
				  </td>
				  <td>
					  <a href="javascript:" class="a-upload">
						  <input type="file" name="file1" class="file" id="filemp4" size="22" onchange="showVideoName();"/>
						  <emp:message key="ydwx_wxfs_dtwxfs_schwj" defVal="上传文件" fileName="ydwx"></emp:message>
					  </a>
					  <input class="showFileName" value="" disabled="disabled" style="display: none">
				  </td>
				</tr>	
				<tr><td>&nbsp;</td></tr>
					<tr>
					<%--  <td nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  						  背景图：<input type="file" name="file1" class="file" id="filem3u8" size="22" />
				  </td>--%>
				</tr>
				
				<tr><td height="20px"></td></tr>
				<tr>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;<font color="red" size="+1">*</font><emp:message key="ydwx_wxgl_wxsc_xinxi5" defVal="注：*为必填项" fileName="ydwx"></emp:message><br/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="ydwx_wxgl_wxsc_xinxi6" defVal="1. MP4(必选项，MP4文件，建议大小控制3M以内)" fileName="ydwx"></emp:message><br/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="ydwx_wxgl_wxsc_xinxi7" defVal="2.SWF(必选项，SWF文件，建议大小控制3M以内)" fileName="ydwx"></emp:message><br/>
							<%--	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.背景图(支持bmp,png格式，建议大小控制在1M以内)--%>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<div id="vedcover"></div>
							<div id="VedprogressBar" class="ydwx_VedprogressBar">

								<div id="VedtheMeter">
									<div id="VedprogressBarText"></div>
				
									<div id="VedprogressBarBox">
										<div id="VedprogressBarBoxContent"></div>
									</div>
								</div>
						   </div>
					</td>
				</tr>
				<tr><td height="20px"></td></tr>
							<tr><td colspan="2" align="center">
								<input type="submit" id="vdiosubmit"  class="btnClass5 mr23" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>"/>
								<input type="button" onclick="endVed()" class="btnClass6" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>"/>
								<br/>
							</td></tr>
			</table>
		</form>			
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/addrbook.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/upload.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/webexbook.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/webexbook_sel.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
				<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#getloginUser");
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
			if("${msg}".length>0){
				alert("${msg}");
				}
			$("#search").click(function(){ submitForm(); });
			$("#etree").load("<%=iPath %>/webexlist.jsp?treemethod=getEmpSecondDepJson&ac="+<%=addcode%>+"&dc="+<%=delcode%>);
			//reloadTree();
			setLeftHeight();
			
		});
		</script>
	</body>
	
</html>
