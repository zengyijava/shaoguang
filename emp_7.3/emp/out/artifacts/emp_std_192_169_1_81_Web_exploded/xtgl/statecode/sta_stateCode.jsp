<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.statecode.LfStateCode"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%

	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	List<LfStateCode> stateCodeList = (List<LfStateCode>) request.getAttribute("lfStateCodeList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	LfStateCode lfStateCode = (LfStateCode)request.getAttribute("lfStateCode");
	String stateCode = lfStateCode.getStateCode()!=null?lfStateCode.getStateCode():null;
	String mappingCode = lfStateCode.getMappingCode()!=null?lfStateCode.getMappingCode():null;
	String stateDes = lfStateCode.getStateDes()!=null?lfStateCode.getStateDes():null;
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("stateCode");
	//String actionPath = (String)request.getAttribute("actionPath");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="xtgl_cswh_zmtgl_ztm" defVal="状态码" fileName="xtgl"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/sta_stateCode.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%-- <link rel="stylesheet" type="text/css" href="<%=skin %>/sta_stateCode.css?V=<%=StaticValue.JSP_IMP_VERSION %>"/> --%>
		
	</head>
	<body id="sta_stateCode" onload="show()">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<form name="pageForm" action="sta_stateCode.htm" method="post" id="pageForm">
			<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
			<input type="hidden" value="" id="busCodeTemp"></input>
			<input type="hidden" value="" id="menuCodeTemp"></input>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent" >
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<%if(btnMap.get(menuCode+"-1")!=null) { %>  
					<a id="add"><emp:message key="xtgl_spgl_shlcgl_xj" defVal="新建" fileName="xtgl"/></a>
				    <%} %>
					<%if(btnMap.get(menuCode+"-3")!=null) { %>
					<a id="downloadTemplate" >下载模板</a>
					<%} %>
					<%if(btnMap.get(menuCode+"-2")!=null) { %>
					<a id="upload" ><emp:message key="txgl_wghdpz_gjzsz_dr" defVal="导入" fileName="txgl"></emp:message></a>
				    <%} %>
					<%if(btnMap.get(menuCode+"-4")!=null) { %>
					<a id="exportCondition" ><emp:message key="txgl_wghdpz_dxhmd_dc" defVal="导出" fileName="txgl"></emp:message></a>
					<%} %>
				</div>
				<div id="condition">
					<table>
						<tr>
							<td>
								<emp:message key="xtgl_cswh_zmtgl_ztm_mh" defVal="状态码：" fileName="xtgl"/>
							</td>
							<td>
								<label>
									<input type="text" class="stateCode" name="stateCode" id="stateCode" maxlength="24" value="<%=stateCode==null?"":stateCode %>" />
								</label>
							</td>
							<%-- 映射码 --%>
							<td>
								<emp:message key="xtgl_cswh_zmtgl_ysm_mh" defVal="映射码：" fileName="xtgl"/>
							</td>
							<td>
								<label>
									<input type="text" class="stateCode" name="mappingCode" id="mappingCode" maxlength="24" value="<%=mappingCode==null?"":mappingCode %>" />
								</label>
							</td>
							<td>
								<emp:message key="xtgl_cswh_zmtgl_ztmsm_mh" defVal="状态码说明：" fileName="xtgl"/>
							</td>
							<td>
								<label>
									<input type="text" class="stateDes" name="stateDes" id="stateDes" maxlength="64"  value="<%=stateDes==null?"":stateDes %>" />
								</label>
							</td>
							<td class="tdSer">
								<center><a id="search"></a></center>
							</td>
						
					</table>
				</div>
				<table id="content">
					<thead>
						<tr >
							<th class="content_th1">
								<emp:message key="xtgl_cswh_zmtgl_bh" defVal="编号" fileName="xtgl"/>
							</th>
							<th class="content_th2">
								<emp:message key="xtgl_cswh_zmtgl_ztm" defVal="状态码" fileName="xtgl"/>
							</th>
							<th class="content_th7">
								<emp:message key="xtgl_cswh_zmtgl_ysm" defVal="映射码" fileName="xtgl"/>
							</th>
							<th class="content_th3">
								<emp:message key="xtgl_cswh_zmtgl_ztmsm" defVal="状态码说明" fileName="xtgl"/>
							</th>
							<th class="content_th4">
								<emp:message key="xtgl_cswh_twgl_cjsj" defVal="创建时间" fileName="xtgl"/>
							</th>
							<th class="content_th5">
								<emp:message key="xtgl_cswh_whgl_gxsj" defVal="更新时间" fileName="xtgl"/>
							</th>
							<th class="content_th6" colspan="2">
								 <emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
							</th>
						</tr>
					</thead>
					<tbody>
						<%
						if(stateCodeList != null && stateCodeList.size()>0)
						{
							for(int i=0;i<stateCodeList.size();i++)
							{
								LfStateCode lfStaCode = stateCodeList.get(i); 
						%>
						<tr id="tr-<%=lfStaCode.getStateId()%>">
							<td>
							<label id="lbStateId<%=lfStaCode.getStateId() %>"><xmp><%=lfStaCode.getStateId()%></xmp></label>
							</td>
							<td>
							<label id="lbStateCode<%=lfStaCode.getStateId() %>"><xmp><%=StringEscapeUtils.unescapeHtml(lfStaCode.getStateCode())%></xmp></label>
							</td>
							<%-- 输出映射码 --%>
							<td>
							<label id="lbMappingCode<%=lfStaCode.getStateId() %>"><xmp><%=StringEscapeUtils.unescapeHtml(lfStaCode.getMappingCode())%></xmp></label>
							</td>
							<td>
							<label id="lbDes<%=lfStaCode.getStateId() %>"><xmp><%=StringEscapeUtils.unescapeHtml(lfStaCode.getStateDes())%></xmp></label>
							</td>
							<td>
							<label id="lbCreateTime()<%=lfStaCode.getStateId() %>"><%=lfStaCode.getCreateTime()!=null?df.format(lfStaCode.getCreateTime()):"-"%></label>
							</td>
							<td>
							<label id="lbUpdateTime-<%=lfStaCode.getStateId() %>"><%=lfStaCode.getUpdateTime()!=null?df.format(lfStaCode.getUpdateTime()):"-"%></label>
							</td>
							<td>
								<a href="javascript:update(<%=lfStaCode.getStateId()%>)"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a>
							</td>
							<td>
								<a href="javascript:del(<%=lfStaCode.getStateId()%>)"><emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/></a>
							</td>
						</tr>
						<%
						 	}
						 }else{
						%>
						<tr><td align="center" colspan="11"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></td></tr>
						<%} %>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="11">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
			
			<div id="addDiv" class="addDiv">
				<table class="addDiv_table ">
					<tr class="ztm_mh_tr">
						<td  align="right" class="ztm_mh_td">
							<emp:message key="xtgl_cswh_zmtgl_ztm_mh" defVal="状态码：" fileName="xtgl"/>
						</td>
						<td class="stateCodeAdd_td">
							<input type="text"  class="input_bd stateCodeAdd" name="stateCodeAdd" id="stateCodeAdd" maxlength="7"/>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					<%-- 添加映射码 --%>
					<tr class="ztm_mh_tr">
						<td  align="right" class="ztm_mh_td">
							<emp:message key="xtgl_cswh_zmtgl_ysm_mh" defVal="映射码：" fileName="xtgl"/>
						</td>
						<td class="stateCodeAdd_td">
							<input type="text"  class="input_bd stateCodeAdd" name="mappingCodeAdd" id="mappingCodeAdd" maxlength="7"/>
							&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<tr class="ztmms_mh_tr" >
						<td align="right" class="ztmms_mh_td">
							<emp:message key="xtgl_cswh_zmtgl_ztmsm_mh" defVal="状态码说明：" fileName="xtgl"/>
						</td>
						<td class="stateDesAdd_td"><textarea  class="input_bd stateDesAdd"  name="stateDesAdd" id="stateDesAdd" maxlength="64" ></textarea></td>
						
					</tr>
					<tr class="ztmms_mh_tr" >
						<td align="center" class="ztmms_mh_td" colspan="2">
							<span><font color="red">注：状态码说明不超过64位</font></span>
						</td>
					</tr>
					<tr>
					<td colspan="2" class="addsubmit_td">
					<input name="addsubmit" id="addsubmit" class="btnClass5 mr23" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" onclick="javascript: addStateCode();"/>
					<input name="addcancel" id="addcancel" class="btnClass6" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" onclick="javascript:doCancel();"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
				</table>
			</div>
			
			<div id="editDiv" class="editDiv">
				<table class="editDiv_table">
					<tr class="ztm_mh_tr">
						<td   align="right" class="ztm_mh_td">
							<emp:message key="xtgl_cswh_zmtgl_ztm_mh" defVal="状态码：" fileName="xtgl"/>
						</td>
						<td class="stateCodeEdit_td"><label id="stateCodeEdit" class="stateCodeEdit" maxlength="24"></label></td>
					</tr>
					<%-- 编辑映射码 --%>
					<tr class="ztm_mh_tr">
						<td   align="right" class="ztm_mh_td">
							<emp:message key="xtgl_cswh_zmtgl_ysm_mh" defVal="映射码：" fileName="xtgl"/>
						</td>
						<td class="stateCodeEdit_td">
						<%-- <input  class="input_bd mappingCodeEdit" id="mappingCodeEdit" class="mappingCodeEdit" maxlength="20"></input> --%>
						    <label  class="stateCodeEdit" id="mappingCodeEdit" maxlength="24"></label></td>
						</td>
					</tr>
					<tr class="ztmsm_mh_tr">
						<td  align="right" class="ztmsm_mh_td" maxlength="64">
							<emp:message key="xtgl_cswh_zmtgl_ztmsm_mh" defVal="状态码说明：" fileName="xtgl"/>
						</td>
						<td ><textarea  class="input_bd stateDesEdit"  name="stateDesEdit" id="stateDesEdit" maxlength="64"></textarea></td>
					</tr>
					<tr class="ztmms_mh_tr" >
						<td align="center" class="ztmms_mh_td" colspan="2">
							<span><font color="red">注：状态码说明不超过64位</font></span>
						</td>
					</tr>
					<tr><td colspan="2" class="editsubmit_tr_td">
					<input name="editsubmit" id="editsubmit"  class="btnClass5 mr23" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" onclick="javascript:editStateCode()"/>
					<input name="editcancel" id="editcancel"  class="btnClass6" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" onclick="javascript:$('#editDiv').dialog('close')"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td></tr>
				</table>
				
			</div>
			
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div id="singledetail" class="singledetail">
				<div id="msg" class="msg"><xmp></xmp></div>
			</div>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
			<!-- 导入提示信息1 -->
			<div id="importmessage" class="importmessage" style="display:none">
				<div id="importmsg" class="msg">
				    <center>
						
						<div id="u1115" class="ax_default label">
					        <div id="u1115_div" class=""></div>
					        <div id="u1116" class="text">
					          <p><span>导入完成，共导入</span><span style="color:#00AB72;" id="importmessageSucc-3">12</span><span>条，失败</span><span style="color:#FF0000;" id="importmessageFail-3" >0</span><span>条</span></p>
					        </div>
					    </div>
					    <div id="u1117" class="ax_default label">
					        <div id="u1117_div" class=""></div>
					        <input type="hidden" value="" id="importmessageFilePath-3"/>
					        <div id="u1118" class="text">
					          <p><span>下载导入报告，查看失败原因</span></p>
					        </div>
					    </div>
					
					      <!-- Unnamed (矩形) -->
					    <div id="u1119" class="ax_default label">
					        <div id="u1119_div" class=""></div>
					        <!-- Unnamed () -->
					        <div id="u1120" class="text">
					          <p><a id="downloadErrorMessage" ><img id="u1121_img" class="img" src='<%=inheritPath + "/statecode/img/u1074.png"%>'/><span>导入错误报告</span ><span id="yearandmonth">20181212.txt</span></a></p>
					        </div>
					    </div>
				    </center>
	                <div class="messagefoot" >
	   						  <input type="button" id="importwok"  value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" class="btnClass5 mr23" />
					</div>
                </div>
			</div>
			<!-- 导入提示信息2 -->
			<div id="importmessage2" class="importmessage2" style="display:none">
				<div id="importmsg2" class="msg2">
				    <center>
						<div id="u1121" class="ax_default label">
					        <div id="u1121_div" class=""></div>
					        <div id="u1122" class="text">
					          <p><span>导入完成，共导入</span><span style="color:#00AB72;"id="importmessageSucc-2">12</span><span>条，失败</span><span style="color:#FF0000;" id="importmessageFail-2">0</span><span>条</span></p>
					        </div>
					    </div>
				    </center>
	                <div class="messagefoot2" >
	   						  <input type="button" id="importwok2"  value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" class="btnClass5 mr23" />
					</div>
                </div>
			</div>
			<!-- 导入提示信息3 -->
			<div id="importmessage3" class="importmessage3" style="display:none">
				<div id="importmsg3" class="msg3">
				    <center>
						<div id="u1123" class="ax_default label">
					        <div id="u1123_div" class=""></div>
					        <div id="u1124" class="text">
					          <p><span>导入失败，一次最多导入</span><span style="color:#00AB72;"id="importmessageSucc-2">1000</span><span>条数据，已超过导入上限！</span></p>
					        </div>
					    </div>
				    </center>
	                <div class="messagefoot3" >
	   						  <input type="button" id="importwok3"  value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" class="btnClass5 mr23" />
					</div>
                </div>
			</div>
			<!-- 导入提示信息4 -->
			<div id="importmessage4" class="importmessage4" style="display:none">
				<div id="importmsg4" class="msg4">
				    <center>
						<div id="u1125" class="ax_default label">
					        <div id="u1125_div" class=""></div>
					        <div id="u1126" class="text">
					          <p><span>模板为空，请填写内容后继续导入！</span></p>
					        </div>
					    </div>
				    </center>
	                <div class="messagefoot4" >
	   						  <input type="button" id="importwok4"  value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" class="btnClass5 mr23" />
					</div>
                </div>
			</div>
			<!-- 导入提示信息5 -->
			<div id="importmessage5" class="importmessage5" style="display:none">
				<div id="importmsg5" class="msg5">
				    <center>
						<div id="u1127" class="ax_default label">
					        <div id="u1127_div" class=""></div>
					        <div id="u1128" class="text">
					          <p><span>导入文件格式错误，请使用状态码导入模板！</span></p>
					        </div>
					    </div>
				    </center>
	                <div class="messagefoot5" >
	   						  <input type="button" id="importwok5"  value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" class="btnClass5 mr23" />
					</div>
                </div>
			</div>
			<!-- 导入支持 -->
			<div class="fileBindDiv" id="fileBindDiv">
	                <form id="uploadStateCodeForm" action="<%=request.getContextPath() %>/sta_stateCode.htm?method=upload" method="post" enctype="multipart/form-data">
				            <br/>
				            <br/>
				            <br/>
			           		<table class="xzwj_mh_table">
			  					  <!-- 
				           		<thead>
			  					   <tr>
			   				 		 <td  width="70px"><span style="text-align: right;"><emp:message key="xtgl_czygl_gjaqsz_xzwj_mh" defVal="选择文件：" fileName="xtgl"/></span></td>
			   						 <td style="margin-left: -10px;"><a href="javascript:;" class="a-upload"><input type="file" name="uploadName" id="uploadFile"><emp:message key="xtgl_cswh_gxhjmsz_ll" defVal="浏览" fileName="xtgl"/></a><div id="fileDisplay"></div></td>
			 					  	 <td>
			 					  	 	<emp:message key="xtgl_czygl_gjaqsz_xzwj_mh" defVal="选择文件：" fileName="xtgl"/>
			 					  	 </td>
			 					  	 <td class="busNameAdd_td">
			 					  	 	<span class="filepath_span">
											<input class="filepath"  maxlength="16"  type="text" id="filepath" readonly>
											<input class="uploadFile" type="file" id="uploadFile" name="uploadName" onchange="filepath.value=this.value;this.title=this.value"/>			
										</span>
			 					  	 </td>
			 					  	 
			 					  </tr>
			 					  	 -->
			 					  <tr class="ywmc_mh_tr">
										<td align="right" class="ywmc_mh_td"><emp:message
												key="xtgl_czygl_gjaqsz_xzwj_mh" defVal="上传文件" fileName="xtgl" />
										</td>
										<td class="busNameAdd_td">
										 <input type="file" name="file" id="uploadFile" maxlength="32"
											 />
										</td>
								  </tr>
			 					  <%-- <tr class="uploadFile_down_tr"><td colspan="2"></td></tr>
			 					  <tr>
			 					  	<td colspan="2" align="left">
			 					  	<a href="javascript:download_href('<%=path %>/down.htm?filepath=xtgl/statecode/file/temp/statecode_Template_<%=langName %>.xls')" class="zzcxlslx_a"><emp:message key="xtgl_czygl_gjaqsz_zzcxlslx" defVal="只支持xls类型的Excel文件上传，点击下载模板" fileName="xtgl"/></a>
			 					  	</td>
			 					  </tr> --%>
			 					  <tr class="kwComments_up_tr"><td colspan="2"></td></tr>
			  					  <tr>
			   						  <td colspan=2  id="btnd" align="center">
			   						  <input type="hidden" value="" id="kwComments"/>
			   						  <input type="button" id="kwsok"  value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='导入' fileName='xtgl'/>" class="btnClass5 mr23" />
		                        	  <input type="button" id="kwsc"  value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" class="btnClass6" />
		                              <br/>
			 					  </tr>
							</table>
		            </form>
            </div>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<%-- 为了解决ie8空白双击导致浏览器崩溃，增加日期控件的引用，可解决这问题 --%>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/sta_stateCode.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			//解除查询条件输入框的绑定
			//$('#condition input[id="stateCode"]').unbind();
			//$('#condition input[id="mappingCode"]').unbind();
			//$('#condition input[id="stateDes"]').unbind();
			closeTreeFun(["dropMenu"]);
			getLoginInfo("#hiddenValueDiv");
			noquot($("#stateCodeEdit"));
			noquot($("#stateCodeAdd"));
			$("#downloadTemplate").click(function(){
				window.IE_UNLOAD  = true;
				window.location.href="down.htm?filepath=xtgl/statecode/file/temp/statecode_Template_zh_CN.xlsx";
			});
			$("#downloadErrorMessage").click(function(){
				window.IE_UNLOAD  = true;
				var filePathTemp=$("#importmessageFilePath-3").val();
				window.location.href="sta_stateCode.htm?method=downloadErrorMessage&filepath="+filePathTemp;
			});
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
			$("#addDiv").dialog({
				autoOpen: false,
			    width:500,
			    height:300,
			    title:getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_45"),
			    modal:true,
			    resizable:false
			 });
			 $("#editDiv").dialog({
				autoOpen: false,
			    width:500,
			    height:300,
			    title:getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_46"),
			    modal:true,
			    resizable:false
			 });
			 $("#fileBindDiv").dialog({
				    autoOpen: false,
					height: 200,
					width: 450,
					modal: true,
					resizable:false
		    });
			$("#importmessage").dialog({
				    title:"提示",
				    autoOpen: false,
					height: 300,
					width: 450,
					modal: true,
					resizable:false
		    });
			$("#importmessage2").dialog({
				    title:"提示",
				    autoOpen: false,
					height: 300,
					width: 450,
					modal: true,
					resizable:false
		    });
			$("#importmessage3").dialog({
				    title:"提示",
				    autoOpen: false,
					height: 300,
					width: 450,
					modal: true,
					resizable:false
		    });
			$("#importmessage4").dialog({
				    title:"提示",
				    autoOpen: false,
					height: 300,
					width: 450,
					modal: true,
					resizable:false
		    });
			$("#importmessage5").dialog({
				    title:"提示",
				    autoOpen: false,
					height: 300,
					width: 450,
					modal: true,
					resizable:false
		    });
			$('#add').click(function(){
				$("#addDiv").dialog("open");
			});
			$('#upload').click(function(){
				$("#fileBindDiv").dialog("option","title", getJsLocaleMessage("xtgl","xtgl_ywlx_filedaoru"));
				$("#fileBindDiv").dialog("open");
			});
			//取消导入
			$('#kwsc').click(function(){
				$("#fileBindDiv").dialog("close");
			});
			//取消导入的提示信息1
			$('#importwok').click(function(){
				$("#importmessage").dialog("close");
			});
			//取消导入的提示信息2
			$('#importwok2').click(function(){
				$("#importmessage2").dialog("close");
			});
			//取消导入的提示信息3
			$('#importwok3').click(function(){
				$("#importmessage3").dialog("close");
			});
			//取消导入的提示信息4
			$('#importwok4').click(function(){
				$("#importmessage4").dialog("close");
			});
			//取消导入的提示信息5
			$('#importwok5').click(function(){
				$("#importmessage5").dialog("close");
			});
			//点击导入确定
			$('#kwsok').click(function(){
				var uploadFile = $("#uploadFile").attr("value") ;
				$("#kwsok").attr("disabled",true);
				if(uploadFile.length == 0){//非空检查
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_25")) ;
					$("#kwsok").attr("disabled",false);
					return false ;
				}else if(uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xlsx"&&uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xls"){//检查文件格式是否是.txt格式
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_26")) ;
					$("#kwsok").attr("disabled",false);
					return false ;
				}else{
					 $("#uploadStateCodeForm").submit();
				}
			});
			$("#singledetail").dialog({
				autoOpen: false,
				maxWidth: 350,
				maxHeight: 170,
				modal: true
			});
			
			//点击导出状态码数据
			//导出全部数据到excel
			$("#exportCondition").click(
			function()
			{
				//if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_210')))
				if(confirm('确定要导出数据到excel?'))
				 {
			   	   	var stateCode =$("#stateCode").val();
					var mappingCode =$("#mappingCode").val(); 	
					var stateDes=$("#stateDes").val(); 
			   		<%
			   		if(stateCodeList!=null && stateCodeList.size()>0){
			   		  if(pageInfo.getTotalRec()<=500000){
			   		%>	
		   				$.ajax({
								type: "POST",
								url: "<%=path%>/sta_stateCode.htm?method=ReportCurrPageExcel",
								data: {
									    stateCode:stateCode,
									    mappingCode:mappingCode,
										pageIndex:<%=pageInfo.getPageIndex()%>,
										pageSize:<%=pageInfo.getPageSize()%>,
										stateDes:stateDes
									  },
				                beforeSend:function () {
									page_loading();
				                },
				                complete:function () {
							    	page_complete();
				                },
								success: function(result){
				                        if(result=='true'){
				                           download_href("<%=path%>/sta_stateCode.htm?method=downloadFile&exporttype=smstaskrecord_export");
				                        }else{
				                            //alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_211'));
				                            alert('导出失败!');
				                        }
					   			}
							});	
			   		<%	
			   		}else{%>
			   		    //alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_272'));
			   		    alert('数据量超过导出的范围50万，请从数据库中导出！');
			   		<%}
			   		}else{
			   		%>
			   		    //alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_212'));
			   		    alert('无数据可导出！');
			   		<%
			   		}%>
				}				 
				
			});
		});
		function show(){
			<% 
			String result=(String)session.getAttribute("ctrResult");
			if(result != null){
				session.removeAttribute("ctrResult");
				//Upload=1&fail=7&filePath=/D:/javatools/tomcat/apache-tomcat-6.0.35/webapps/emp_std/xtgl/statecode/file/\download\errormessage\StateCode_2018121217241755_1001.zip
				String [] temps=result.split("&");//按&进行分割处理
				if(temps.length==3){
					//返回三个参数：成功数、失败数、文件路径
					String success="";
					String fail="";
					String filePath="";
					for(int x=0;x<temps.length;x++){
						String[] tmp=temps[x].split("=");
						if("upload".equals(tmp[0])){
							success=tmp[1];
						}else if("fail".equals(tmp[0])){
							fail=tmp[1];
						}else if("filePath".equals(tmp[0])){
							filePath=tmp[1];
						}
					}
			  %>
				//成功数
				$("#importmessageSucc-3").html(<%=success%>);
				//失败数
				$("#importmessageFail-3").html(<%=fail%>);
				//文件路径
				$("#importmessageFilePath-3").attr("value","<%=filePath%>");
				//年月
				var myDate2 = new Date();
				
				//获取当前年
				var year2=myDate2.getFullYear();
				//获取当前月
				var month2=myDate2.getMonth()+1;
				//获取当前日
				var date2=myDate2.getDate(); 
				
				var now2=year2+""+month2+""+date2+".txt";
				$("#yearandmonth").text(now2);
				//打开提示会话框
			    $("#importmessage").dialog("open");
			  <% 	
				}else if(temps.length==2){
					//返回两个参数:成功数、失败数（0）
					String success="";
					for(int x=0;x<temps.length;x++){
						String[] tmp=temps[x].split("=");
						if("upload".equals(tmp[0])){
							success=tmp[1];
						}
					}
			  %>
				//成功数
				$("#importmessageSucc-2").html(<%=success%>);
				//失败数
				$("#importmessageFail-2").html("0");
				//打开提示会话框
			    $("#importmessage2").dialog("open");
			  <% 	
				}else if(temps.length==1){
					//返回一个参数：overCount   |   noRecord
					if("overCount".equals(temps[0])){
						//一次最多导入1000条数据。
			  %>
				//打开提示会话框
			    $("#importmessage3").dialog("open");
			  <% 
					}else if("noRecord".equals(temps[0])){
						//模板为空，请填写内容后继续导入！
			  %>
				//打开提示会话框
			    $("#importmessage4").dialog("open");
			  <% 
					}else if("error".equals(temps[0])||"errorFile".equals(temps[0])){
						//导入文件格式错误，请使用状态码导入模板！
			  %>
				//打开提示会话框
			    $("#importmessage5").dialog("open");
			  <% 
					}
				}
			} %>
			
			
		}

		
		</script>
	</body>
</html>
