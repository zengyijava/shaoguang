<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	inheritPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	List<Userdata> userdatas = (List<Userdata>)request.getAttribute("userdatas");
	if(userdatas==null){
		//out.println("没有批量充值的SP账号");
		out.println(MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_myplczdspzh", request));
		return;
	}

    //确定
	String qd = MessageUtils.extractMessage("common", "common_confirm", request);
    //取消
	String qx = MessageUtils.extractMessage("common", "common_cancel", request);
	//sp账号
	String spzzh = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_spzzh", request);
    if(spzzh!=null&&spzzh.length()>1){
    	spzzh = spzzh.substring(0,spzzh.length()-1);
    }
	//账号名称
	String zhmc = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_zhmc", request);
    if(zhmc!=null&&zhmc.length()>1){
    	zhmc = zhmc.substring(0,zhmc.length()-1);
    }
    
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_spzhczhs_gjfzsz" defVal="告警阀值设置" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
	
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style type="text/css">
			#toptable{
				width: 484px;line-height: 30px;margin-left: 20px;margin-top: 20px;
			}
		</style>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/spb_spBalanceAll.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/spb_spBalanceAll.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="spb_spBalanceAll">
					<div >
						<input type="hidden" id="useridstrs" name="useridstrs" value="<%=request.getParameter("useridstrs") %>"/>
						<div id="addBalanceAll">
					
						<table id="toptable">
						<tr>
						<td>
							<emp:message key="txgl_wgqdpz_spzhczhs_lx" defVal="类型：" fileName="txgl"></emp:message>
						</td>
						<td>
							 <select id="bltype" name="bltype" class="input_bd bltype"  >
					           	<option value=""><emp:message key="txgl_wgqdpz_spzhczhs_qxz" defVal="请选择" fileName="txgl"></emp:message></option>
					           	<option value="1"><emp:message key="txgl_wgqdpz_spzhczhs_cz" defVal="充值" fileName="txgl"></emp:message></option>
					           	<option value="2"><emp:message key="txgl_wgqdpz_spzhczhs_hs" defVal="回收" fileName="txgl"></emp:message></option>
			          		</select>
			          		</td>
			          		<td>
			          		<emp:message key="txgl_wgqdpz_spzhczhs_tss" defVal="条数：" fileName="txgl"></emp:message>
			          		</td>
			          		<td>
				           		<input type="text" id="addCount" name="addCount" class="input_bd" maxlength="9" onkeyup="if(value!=value.replace(/[^\w\/]/g,''))value=value.replace(/[^\w\/]/g,'')"/>&nbsp;<emp:message key="txgl_wgqdpz_spzhczhs_t" defVal="条" fileName="txgl"></emp:message>
				           	</td>
				           </tr>
				         </table>
						<table id="spusertable">
			        	<tr>
			        	<th class="spzzh_th">
			        	<%=spzzh %>
			        	</th>
			        	<th>
			        	<%=zhmc %>
			        	</th>
			        	</tr>
			        		<%
			        		if(userdatas != null && userdatas.size() > 0)
			        		{
			        			for(Userdata userdata:userdatas)
			        			{
			        		%>
				        		<tr align="center">
				        			<td calss="textalign">
				        			<%=userdata.getUserId() %>
				        			</td>
				        			<td calss="textalign">
				        			<%=userdata.getStaffName() %>
				        			</td>
				        		</tr>
			        		<%
			        			}
			        		}
			        		%>
			        	</table>
						
						<div  align="center" class="qd_div">
							<input type="button" class="btnClass5 mr23" value="<%=qd %>" onclick="javascript:plchongZhi(arguments[0]);" <%=userdatas != null && userdatas.size() > 0?"":"disabled"%>>
				           	<input type="button" class="btnClass6" value="<%=qx %>" onclick="javascript:doCancelEdit('#addAllSpuserDiv');">
							<br/>
						</div>
					</div>
					</div>
					</div>
					</div>
    <div class="clear"></div>
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/spb_spBalanceAll.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	</body>
</html>

