<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.pasgroup.entity.Userdata" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfSpFee"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%//清除页面缓存
	response.setHeader("Pragma","No-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", 0); 
	
	String path=request.getContextPath();
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	@SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = "1900-1480";
	
	@SuppressWarnings("unchecked")
	List<Userdata> userList = (List<Userdata>)request.getAttribute("userList");
	
	String result = request.getAttribute("w_userdataResult")==null?"0"
			:(String)request.getAttribute("w_userdataResult");
	request.removeAttribute("w_userdataResult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String txglFrame = skin.replace(commonPath, inheritPath);
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	//确定
    String qd = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_qd", request);
	//取消
	String qx = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_qx", request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_dlzhgl_dcdlzhxg" defVal="短彩代理账号修改" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/pag_editProxyMage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/pag_editProxyMage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>

	<body id="pag_editProxyMage">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dlzhgl_xgddlzh", request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<div class="titletop">
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
								<emp:message key="txgl_wgqdpz_dlzhgl_xgdlzh" defVal="修改代理账号" fileName="txgl"></emp:message>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_fhsyj" defVal="返回上一级" fileName="txgl"></emp:message></font>
							</td>
						</tr>
					</table>
				</div>
			<div id="detail_Info" class="<%="zh_HK".equals(empLangName)?"detail_Info1":"detail_Info2"%>" >
            <form action="<%=path %>/pag_proxyMage.htm" name="form1" onsubmit="return checkform()" method="post">
            	 <div id="corpCode" class="hidden"></div>
            	 <%--<input type="hidden" name="feeflag" id="selectFlag" value="2">
            	 <input type="hidden" value="0" name="riselevel"/>
            --%><table class="dlhhh_table">
            		<thead>
                           <%
                           		String userid=request.getParameter("userid");
                           		Userdata user=(Userdata)request.getAttribute("userdata");
                           		String keyId = request.getParameter("keyId");
                           	%>  			 	
               	  		 <tr>
                         	<td class="dlhhh_td"><span><emp:message key="txgl_wgqdpz_dcspzh_dlhhh" defVal="代理账号：" fileName="txgl"></emp:message></span></td>
                            <td>
                            <label><%=user.getUserId() %></label><input  value="<%=user.getUserId() %>" type="hidden" id="userid2" name="userid" maxlength="6"  onkeyup= "spCard(this)"  class="input_bd dlhhh_table_input"  />
                            <input type="hidden" name="hidOpType" value="edit"/>
                            <input type="hidden" name="method" value="update"/>
                            <input type="hidden" name="uid" value="<%=user.getUid() %>"/>
                            <input type="hidden" id="ips" name ="ips" value=""/>
                            <input type="hidden" id="keyId" name ="keyId" value="<%=keyId %>"/>
                            </td>
                          </tr>
                         <tr>
                         	<td><span><emp:message key="txgl_wgqdpz_dcspzh_zhmc" defVal="账户名称：" fileName="txgl"></emp:message></span></td>
                            <td><label><input maxlength="20" type="text" id="xiugai"  name="staffname"  class="input_bd dlhhh_table_input"  value="<%=user.getStaffName().replace("&","&amp;").replace("\"","&quot;") %>"  onkeyup= "if(value!=value.replace(/[\n\s*\r]/g,''))value=value.replace(/[\n\s*\r]/g,'')" /></label>
                            	<font class="font_red">&nbsp;*</font><font class="font_color">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_pysb" defVal="自定义，便于识别" fileName="txgl"></emp:message></font>
                            </td>
                         </tr>
                          <tr>
                            <td ><span><emp:message key="txgl_wgqdpz_dcspzh_zhmm" defVal="账户密码：" fileName="txgl"></emp:message></span></td>
                            <td>
                            <label><input maxlength="18" type="password" id="xiugai" name="userpassword"  class="input_bd dlhhh_table_input"  value="<%=user.getUserPassword().replace("&","&amp;").replace("\"","&quot;") %>" /></label>
                            	<font class="font_red">&nbsp;*</font><font class="font_color">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dlzhgl_zfqfdxx" defVal="6－18个字符，区分大小写" fileName="txgl"></emp:message></font>
                            </td>
                         </tr>
                                                 
                           <tr align="left">
                         	<td><span><emp:message key="txgl_wgqdpz_dcspzh_zhzt" defVal="账户状态：" fileName="txgl"></emp:message></span></td>
                            <td><label>
                            <select name="status" id="select"  class="input_bd select"  >
                      			<option value="0" <%="0".equals(user.getStatus().toString())?"selected":"" %>><emp:message key="txgl_wgqdpz_dcspzh_yjh" defVal="已激活" fileName="txgl"></emp:message></option>
                	  			<option value="1" <%="1".equals(user.getStatus().toString())?"selected":"" %> ><emp:message key="txgl_wgqdpz_dcspzh_ysx" defVal="已失效" fileName="txgl"></emp:message></option>
                            </select></label></td>
                         </tr>

                           <tr align="left"  id="trip">
	                         <td valign="top"><span><emp:message key="txgl_mwgateway_text_4" defVal="绑定IP：" fileName="txgl"></emp:message></span></td>
	                         <td valign="top" id="td-ips">
		                         <div class="clearfix">
			                         <ul>
			                         <%if(user.getLoginIp()!=null&&!"".equals(user.getLoginIp().trim())) {
			                        		String[] ips = user.getLoginIp().split(",");
				                         	if(ips.length>0){
				                         		String[] iparr = null;
				                         		for(String ip:ips){
				                         			iparr = ip.split("\\.");
				                         			out.print("<li class=\"input_bd\">");
				                         			for(int i=0;i<iparr.length;i++){
				                         				if(i>0){
				                         					out.print(".");
				                         				}
				                         				out.print("<input type=\"text\" value=\""+iparr[i]+"\" maxlength=\"3\">");
				                         			}
				                         			out.println("</li>");
				                         		}
				                         	}else{
				                         		 %>
													<li class="input_bd">
														<input type="text" maxlength="3">.<input type="text" maxlength="3">.
														<input type="text" maxlength="3">.<input type="text" maxlength="3">
													</li>
												<%	
				                         	}
			                         }else{ %>
										<li class="input_bd">
											<input type="text" maxlength="3">.<input type="text" maxlength="3">.
											<input type="text" maxlength="3">.<input type="text" maxlength="3">
										</li>
									<%} %>
									</ul><a id="add-ip" class="ip-btn"><emp:message key="txgl_wgqdpz_dlzhgl_jxtj" defVal="继续添加" fileName="txgl"></emp:message></a> <a id="remove-ip" class="ip-btn"><emp:message key="txgl_wgqdpz_qyhdgl_sc" defVal="删除" fileName="txgl"></emp:message></a>
		                         </div>
		                         <span class="dzdyd_span"><emp:message key="txgl_wgqdpz_dlzhgl_zspzipdzdyd" defVal="在所配置IP地址对应的服务器上使用代理账号发送信息，为空则不受限制" fileName="txgl"></emp:message></span>
	                         </td>
                         </tr>
                         <tr>
                         	<td colspan="2"  id="btn">
                         	<div  align="right" class="mt10 btnSsu_div">
	                         	<input  type="submit" id="btnSsu" value="<%=qd %>" class="btnClass5 mr23"/>
	                         	<input type="button" onclick="javascript:back()" value="<%=qx %>" class="btnClass6"/>
                         	</div>
                         	</td>
                         </tr> 
                        
                  </thead>    
                </table>
                <div id="corpCode" class="hidden"></div>
                				</form>
                			</div>

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
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=iPath%>/js/userdata.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script language="javascript" src="<%=iPath%>/js/ip.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	
	<script>
		$(document).ready(function(){
			getLoginInfo("#corpCode");
			getLoginInfo("#loginUser");
			show(<%=result %>);
		} );
		function show(i)
		{
			if(i == 1)
			{
				//alert("新建账户成功！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dlzhgl_text_1"));
				location.href="<%=path%>/pag_proxyMage.htm?lgcorpcode=<%=request.getParameter("lgcorpcode") %>";
			}else if(i == 2)
			{
				//alert("修改账户成功！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dlzhgl_text_2"));
				location.href="<%=path%>/pag_proxyMage.htm?lgcorpcode=<%=request.getParameter("lgcorpcode") %>";
			}else if(i != 0)
			{
				//alert("操作失败！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dlzhgl_text_3"));
			}	
		}
		
		function back(){
			var codecorp  = $("#lgcorpcode").val();
			window.location.href='<%=path %>/pag_proxyMage.htm?method=find&lgcorpcode='+codecorp;
		}
	</script>
	</body>
</html>
