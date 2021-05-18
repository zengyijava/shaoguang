<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.servmodule.txgl.entity.Userdata" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfSpFee"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

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
	String menuCode = "1900-1300";
	
	@SuppressWarnings("unchecked")
	List<Userdata> userList = (List<Userdata>)request.getAttribute("userList");
	
	String result = request.getAttribute("w_userdataResult")==null?"0"
			:(String)request.getAttribute("w_userdataResult");
	request.removeAttribute("w_userdataResult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	LfSpFee lfSpFee = (LfSpFee)request.getAttribute("lfSpFee");
	String txglFrame = skin.replace(commonPath, inheritPath);
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	//确定
	String qd = MessageUtils.extractMessage("common", "common_confirm", request);
	//返回
	String fh = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_fh", request);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_dcspzh_dcspzhxg" defVal="短彩SP账户修改" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/pag_editUserData.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/pag_editUserData.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>

	<body id="pag_editUserData">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_xgdcspzh", request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<div class="titletop">
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
								<emp:message key="txgl_wgqdpz_dcspzh_xgdcspzh" defVal="修改短彩SP账号" fileName="txgl"></emp:message>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_fhsyj" defVal="返回上一级" fileName="txgl"></emp:message></font>
							</td>
						</tr>
					</table>
				</div>
			<div id="detail_Info" class="detail_Info">
            <form action="<%=path %>/pag_userData.htm" name="form1" onsubmit="return checkform()" method="post">
            	 <div id="corpCode" class="hidden"></div>
            	 <%--<input type="hidden" name="feeflag" id="selectFlag" value="2">
            	 <input type="hidden" value="0" name="riselevel"/>
            --%><table class="corpCode_table">
            		<thead>
                           <%
                           		String userid=request.getParameter("userid");
                           		Userdata user=(Userdata)request.getAttribute("userdata");
                           		String keyId = request.getParameter("keyId");
                           	%>  
                        <tr>
                         	<td><span ><emp:message key="txgl_mwgateway_text_2" defVal="信息类型：" fileName="txgl"></emp:message></span></td>
                            <td>
								<input class="accability" type="checkbox" <%="1".equals(user.getAccouttype().toString()) && (0==user.getAccability() || 1 == (int)(0x00000001&user.getAccability()))?"checked":"" %> <%="2".equals(user.getAccouttype().toString())?"disabled":"" %> name="dxval" value="1" ><label>短信SP账户</label>&nbsp;&nbsp;
								<input class="accability" type="checkbox" <%="2".equals(user.getAccouttype().toString()) && (2 == (int)(0x00000002&user.getAccability()))?"checked":"" %> disabled="disabled" name="cxval" value="2" ><label>彩信SP账户</label>&nbsp;&nbsp;
								<input class="accability" type="checkbox" <%="1".equals(user.getAccouttype().toString()) && (4 == (int)(0x00000004&user.getAccability()))?"checked":"" %> <%="2".equals(user.getAccouttype().toString())?"disabled":"" %> name="fxval" value="4" > <label>富信SP账户</label>&nbsp;&nbsp;

							<%--								<label ><select name="bbddd" id="bbddd"  disabled="disabled"  class="input_bd bbddd"  >
                           		<option value="1" <%="1".equals(user.getAccouttype().toString())?"selected":"" %> ><emp:message key="txgl_wgqdpz_dcspzh_dxspzh" defVal="短信SP账户" fileName="txgl"></emp:message></option>
                           		<option value="2" <%="2".equals(user.getAccouttype().toString())?"selected":"" %>><emp:message key="txgl_wgqdpz_dcspzh_cxspzh" defVal="彩信SP账户" fileName="txgl"></emp:message></option>
                            </select></label>--%>
                            <input type="hidden" name="accouttype" id="accouttype" value="<%=user.getAccouttype() %>" />
							<input type="hidden" name="accability" id="accability" value="<%=user.getAccability() %>" />
                            <input type="hidden" value="0" name="usertype" id="usertype"/>
                              <label><input class="hidden" type=text id="lbUserType" name="loginid" value="<%=user.getLoginId() %>" readonly="readonly"/></label>
                            </td>
                           </tr>   			 	
               	  		 <tr>
                         	<td class="UserId_up_td"><span><%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_3",request) %></span></td>
                            <td>
                            <label><%=user.getUserId() %></label><input  value="<%=user.getUserId() %>" type="hidden" id="userid2" name="userid" maxlength="6"  onkeyup= "spCard(this)"  class="input_bd userid2"   />
                            <input type="hidden" name="hidOpType" value="edit"/>
                            <input type="hidden" name="method" value="update"/>
                            <input type="hidden" name="uid" value="<%=user.getUid() %>"/>
                            <input type="hidden" id="ips" name ="ips" value=""/>
                            <input type="hidden" id="keyId" name ="keyId" value="<%=keyId %>"/>
                            </td>
                          </tr>
                         <tr>
                         	<td><span><emp:message key="txgl_wgqdpz_dcspzh_zhmc" defVal="账户名称：" fileName="txgl"></emp:message></span></td>
                            <td><label><input maxlength="32" type="text" id="xiugai"  name="staffname"  class="input_bd xiugai"  value="<%=user.getStaffName().replace("&","&amp;").replace("\"","&quot;") %>"  onkeyup= "if(value!=value.replace(/[\n\s*\r]/g,''))value=value.replace(/[\n\s*\r]/g,'')" /></label>
                            	<font class="font_red">&nbsp;*</font><font class="font_color">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_pysb" defVal="自定义，便于识别" fileName="txgl"></emp:message></font>
                            </td>
                         </tr>
                          <tr>
                            <td ><span><emp:message key="txgl_wgqdpz_dcspzh_zhmm" defVal="账户密码：" fileName="txgl"></emp:message></span></td>
                            <td>
                            <label><input maxlength="18" type="password" id="xiugai" name="userpassword"  class="input_bd xiugai" value="<%=user.getUserPassword().replace("&","&amp;").replace("\"","&quot;") %>" /></label>
                            	<font class="font_red">&nbsp;*</font><font class="font_color">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_zfqfdxx" defVal="6－18个字符，区分大小写" fileName="txgl"></emp:message></font>
                            </td>
                         </tr>
                          <tr>
                            <td><span ><emp:message key="txgl_wgqdpz_dcspzh_zhlx" defVal="账户类型：" fileName="txgl"></emp:message></span></td>
                            <td><label ><select name="ujjjj" id="select" onchange="checkUserType(this.value)" disabled="disabled"  class="input_bd select_width"  >
                           		<option value="1" <%=1==user.getSptype()?"selected":"" %> ><emp:message key="txgl_wgqdpz_dcspzh_empyyzh" defVal="EMP应用账户" fileName="txgl"></emp:message></option>
                           		<% 
                           		int corptype= StaticValue.getCORPTYPE();
                           		int spytpeflag = StaticValue.getSPTYPEFLAG();
                           		if(corptype-0==0 || spytpeflag-0==0){ 
                           		 %>
                           		<option value="2" <%=2==user.getSptype()?"selected":"" %>><emp:message key="txgl_wgqdpz_dcspzh_empjrzh" defVal="EMP接入账户" fileName="txgl"></emp:message></option>
                           		<option value="3" <%=3==user.getSptype()?"selected":"" %>><emp:message key="txgl_wgqdpz_dcspzh_zlzh" defVal="直连账户" fileName="txgl"></emp:message></option>
                           		<%} %>
                            </select>
                            <%-- 存放账户类型，因为修改时账户类型不允许修改disabled属性的表单后台接收不到，因此用个隐藏域来存放 --%>
                           	<input type="hidden" value="<%=user.getSptype() %>" name="uuuutype" id="uuuutype"/>
                            </label>
                            </td> 
                         </tr>
                         

                            
                             <tr align="left">
                         	<td><span><emp:message key="txgl_wgqdpz_dcspzh_fsjb" defVal="发送级别：" fileName="txgl"></emp:message></span></td>
                            <td><label>
                           <select id="riselevel" class="input_bd select_width" name="riselevel"  >
							<option value="4" <%="4".equals(user.getRiseLevel().toString())?"selected":"" %>>1(<emp:message key="txgl_wgqdpz_dcspzh_yxjzg" defVal="优先级最高" fileName="txgl"></emp:message>)</option>
							<option value="3" <%="3".equals(user.getRiseLevel().toString())?"selected":"" %>>2</option>
							<option value="2" <%="2".equals(user.getRiseLevel().toString())?"selected":"" %>>3</option>
							<option value="1" <%="1".equals(user.getRiseLevel().toString())?"selected":"" %>>4</option>
							<option value="0" <%="0".equals(user.getRiseLevel().toString())?"selected":"" %>>5</option>
							<option value="-1" <%="-1".equals(user.getRiseLevel().toString())?"selected":"" %>>6</option>
							<option value="-2" <%="-2".equals(user.getRiseLevel().toString())?"selected":"" %>>7</option>
							<option value="-3" <%="-3".equals(user.getRiseLevel().toString())?"selected":"" %>>8</option>
							<option value="-4" <%="-4".equals(user.getRiseLevel().toString())?"selected":"" %>>9(<emp:message key="txgl_wgqdpz_dcspzh_yxjzd" defVal="优先级最低" fileName="txgl"></emp:message>)</option>
							</select></label></td>
                        </tr>
                        <%if(StaticValue.getCORPTYPE() ==1 && lfSpFee!=null){ %>
                        <tr>
                        	<td><span><emp:message key="txgl_wgqdpz_dcspzh_yysjflx" defVal="运营商计费类型：" fileName="txgl"></emp:message></span></td>
                            <td><label>
                        	<input type="hidden" name="sfId" id="sfId" value="<%=lfSpFee.getSfId() %>"/>
                            <select name="feeflag_spfee" id="feeflag_spfee"  class="input_bd select_width">
                	 			<option value="1" ><emp:message key="txgl_wgqdpz_dcspzh_yff" defVal="预付费" fileName="txgl"></emp:message></option>
                	  			<option value="2" <%=lfSpFee.getSpFeeFlag()==2?"selected":"" %>><emp:message key="txgl_wgqdpz_dcspzh_hff" defVal="后付费" fileName="txgl"></emp:message></option>
                            </select>
                            </label>
                            </td>
                        </tr>
                         <%} %>
                           <tr>
                         	<td><span><emp:message key="txgl_wgqdpz_dcspzh_jflx" defVal="计费类型：" fileName="txgl"></emp:message></span></td>
                            <td><label>
                            <select name="feeflag" id="selectFlag"  class="input_bd select_width">
                	 			<option value="1" ><emp:message key="txgl_wgqdpz_dcspzh_yff" defVal="预付费" fileName="txgl"></emp:message></option>
                	  			<option value="2" <%=user.getFeeFlag()==2?"selected":"" %>><emp:message key="txgl_wgqdpz_dcspzh_hff" defVal="后付费" fileName="txgl"></emp:message></option>
                            </select>
                           <span class="font_color">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_yffxydspjx" defVal="预付费需对SP账号进行充值才能进行发送" fileName="txgl"></emp:message></span>
                            </label>
                            </td>
                         </tr> 
                        <%if(user.getAccouttype()==1){ %>
                         <tr>
                         	<td><span><emp:message key="txgl_wygl_wytdgl_5" defVal="连接方式：" fileName="txgl"></emp:message></span></td>
                            <td><label>
	                            <select name="connmethod"  class="input_bd select_width">
	                            <%
	                            int connMethod = user.getUserprivilege()&0x00008000;
	                            if(connMethod > 0) {%>
	                	 			<option value="1" selected="selected"><emp:message key="txgl_wygl_wytdgl_6" defVal="短连接" fileName="txgl"></emp:message></option>
	                	  			<option value="2" ><emp:message key="txgl_wygl_wytdgl_7" defVal="长连接" fileName="txgl"></emp:message></option>
	                            <%
	                            } else {
	                            %>
	                            	<option value="1" ><emp:message key="txgl_wygl_wytdgl_6" defVal="短连接" fileName="txgl"></emp:message></option>
	                	  			<option value="2" selected="selected" ><emp:message key="txgl_wygl_wytdgl_7" defVal="长连接" fileName="txgl"></emp:message></option>
	                            <%
	                            }
	                            %>
	                            </select>
                            </label>
                            </td>
                         </tr>
                          <tr valign="top" align="left" id="trmorpoption">
                        	 <td></td>
                         	 <td>	
                         		<% 
                         		 int needMO = user.getUserprivilege()&0x00000200;	
                         		if(needMO > 0) {%>
	                	 			<input class="trmorpoption_ver" type="checkbox"  name="needmo" value="true" id="pushmourl"/>
	                            <%
	                            } else {
	                            %>
	                            	<input class="trmorpoption_ver" type="checkbox" checked="checked"  name="needmo" value="true" id="pushmourl"/>
	                            <%
	                            }
	                            %>
                         	 	<label><emp:message key="txgl_wygl_wytdgl_9" defVal="需要推送上行" fileName="txgl"></emp:message></label> 
                         	 	<% 
                         		 int needRPT = user.getUserprivilege()&0x00000010;	
                         		if(needRPT > 0) {%>
	                	 			<input class="trmorpoption_ver" type="checkbox"  name="needrpt" value="true" id="pushrpurl"/>
	                            <%
	                            } else {
	                            %>
	                            	<input class="trmorpoption_ver" type="checkbox" checked="checked"  name="needrpt" value="true" id="pushrpurl"/>
	                            <%
	                            }
	                            %>	
                         	 	<label><emp:message key="txgl_wygl_wytdgl_10" defVal="需要推送状态报告" fileName="txgl"></emp:message></label>
                         	 </td>
                         </tr>
                             <%} %>
                         <%--  <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                         	<td><span>用户状态：</span></td>
                            <td><label>
                            <select name="status" id="select" >
                            <% if(user.getStatus()==0){ %>
                      			<option value="0" selected="selected">已激活</option><%}else{ %><option value="0" >已激活</option><%} %>
                	  		<% if(user.getStatus()==1 || user.getStatus()==2){ %>
                	  			<option value="1" selected="selected" >已失效</option><%}else{ %><option value="1" >已失效</option><%} %>
                            </select></label></td> 
                         </tr>--%>
                        <%-- <tr>
                         	<td><span>扣费类型：</span></td>
                            <td><label>
                            <%
                            if(StaticValue.CORPTYPE==0){
                            %>
                            <select name="feeflag" id="selectFlag" >
                	 			<option value="1" >预付费</option>
                	  			<%if(user.getUserType()-0==0){ %>
                	  			<option value="2" <%=user.getFeeFlag()-2==0?"selected":"" %>>后付费</option>
                	  			<%} %>
                            </select>
                            <% }else{
                            %> 
                          		  后付费
                            <input type="hidden" name="feeflag" id="selectFlag" value="2">
                            <%} %>
                            
                            </label></td>
                             <td></td>
                             <td>
                              <input type="hidden" value="0" name="riselevel"/>
                              </td>                                                             
                         </tr>
                         --%> 
                         <%--<tr>
                         	<td><span>联系人：</span></td>
                            <td>
                            	<label>
                            		<input maxlength="12" type="text" id="xiugai" name="lxr" value="<%=user.get() %>"  onkeypress="checkstr()" />
                            	</label>
                           	</td>
                            <td><span>联系电话：</span></td>
                            <td><label><input maxlength="18" type="text" id="xiugai" name="lxrph"  value="" onkeypress="checknum()"/></label></td>
                         </tr> --%>
                         <%if(user.getAccouttype()==1){ %>
                          <tr id="trmorptmode">
                         <td class="szbghqfs_td"><span><emp:message key="txgl_wgqdpz_dcspzh_szbghqfs" defVal="上行、状态报告获取方式：" fileName="txgl"></emp:message></span></td>
                           <td>
                           <label>
	                             <select name="morptmode" id="morptmode"  class="input_bd select_width">
	                	  			<option value="2" <%=2==user.getTransmotype()?"selected":"" %>><emp:message key="txgl_wgqdpz_dcspzh_hxywgzdts" defVal="HTTP/HTTPS协议网关主动推送" fileName="txgl"></emp:message></option>
	                	  			<option value="1" <%=1==user.getTransmotype()?"selected":"" %>><emp:message key="txgl_wgqdpz_dcspzh_sxywgzdts" defVal="SGIP协议网关主动推送" fileName="txgl"></emp:message></option>
	                	 			<option value="0" <%=0==user.getTransmotype()?"selected":"" %>><emp:message key="txgl_wgqdpz_dcspzh_wgyhzdhq" defVal="网关直连转发、用户主动获取" fileName="txgl"></emp:message></option>
	                            </select>
	                            <a id="explain" class="explain" href="javascript:showExplain();"><emp:message key="txgl_wgqdpz_dcspzh_sm" defVal="说明" fileName="txgl"></emp:message></a>
                            </label>
                            </td>
                         </tr>
                         <tr id="trpushversion">
                         <td><span><emp:message key="txgl_wgqdpz_dcspzh_tsjkbb" defVal="推送接口版本：" fileName="txgl"></emp:message></span></td>
                           <td>
                           <label>
	                             <select name="pushversion" id="pushversion"  class="input_bd select_width">
	                	  			<option value="16" <%=16==user.getPushversion()?"selected":"" %>>API V4.x<emp:message key="txgl_wgqdpz_dcspzh_ts" defVal="推送" fileName="txgl"></emp:message></option>
	                	  			<option value="48" <%=48==user.getPushversion()?"selected":"" %>>API V5.x<emp:message key="txgl_wgqdpz_dcspzh_plts" defVal="批量推送" fileName="txgl"></emp:message></option>
	                	  			<option value="49" <%=49==user.getPushversion()?"selected":"" %>>API V5.x<emp:message key="txgl_wgqdpz_dcspzh_dtts" defVal="单条推送" fileName="txgl"></emp:message></option>
	                            </select>
                            </label>
                            </td>
                         </tr>
	                     <tr valign="top" align="left" id="trmorpturl">
	                     	<td><span><emp:message key="txgl_wgqdpz_dcspzh_szbgurl" defVal="推送URL：" fileName="txgl"></emp:message></span></td>
	                         <td valign="top"><input  class="input_bd spbindurl" maxlength="128" type="text" id="spbindurl" name="spbindurl"  value="<%=user.getSpbindurl()==null?"":user.getSpbindurl() %>" />
	                         <font class="font_red">&nbsp;*</font><span class="jssxztbgdy_span"><emp:message key="txgl_wgqdpz_dcspzh_jssxztbgdy" defVal="接收上行、状态报告的业务系统地址，格式如http://192.169.1.155:8080" fileName="txgl"></emp:message></span>
	                         </td>
                         </tr>
                          <tr id="trsxurl">
                          	<td><span><emp:message key="txgl_wgqdpz_dcspzh_sxurl" defVal="上行URL：" fileName="txgl"></emp:message></span></td>
                          	<td>
                          		<input maxlength="128" type="text" id="sx" name="moUrl" value="<%=user.getMoUrl()==null?"":user.getMoUrl() %>"  class="input_bd sx"  />
                          		<font class="font_red">&nbsp;*</font><span class="jssxd_span"><emp:message key="txgl_wgqdpz_dcspzh_jssxd" defVal="接收上行的业务系统地址，默认为EMP上行地址，格式如http://192.169.1.155:8080/emp/moreceive.hts" fileName="txgl"></emp:message></span>
                          	</td>
                          </tr>
                          <tr id="trpturl"><td><span><emp:message key="txgl_wgqdpz_dcspzh_ztbgurl" defVal="状态报告URL：" fileName="txgl"></emp:message></span></td>
                          <td>
                          <input maxlength="128" type="text" id="rptUrl" name="rptUrl"  value="<%=user.getRptUrl()==null?"":user.getRptUrl() %>"  class="input_bd rptUrl"  />
                          <font class="font_red">&nbsp;*</font><span class="jsxxztbg_span"><emp:message key="txgl_wgqdpz_dcspzh_jsxxztbg" defVal="接收下行状态报告的业务系统地址，默认为EMP平台地址，格式如 http://192.169.1.155:8080/emp/rptreceive.hts" fileName="txgl"></emp:message></span>
                          </td></tr>
                          <%
                          		String sendtmspan = user.getSendtmspan();
                          		String startTime = "";
                          		String endTime = "";
                          		if(sendtmspan != null && !"".equals(sendtmspan) && !" ".equals(sendtmspan) && sendtmspan.indexOf("-") > -1)
                          		{
                          			startTime = sendtmspan.split("-")[0];
                          			endTime = sendtmspan.split("-")[1];
                          		}
                           %>
                          <tr align="left" id="trtime">
                         <td ><span><emp:message key="txgl_wgqdpz_dcspzh_qzfssj" defVal="起止发送时间：" fileName="txgl"></emp:message></span></td>
                         <td>
                       		<input type="text"  
								class="Wdate div_bd starttime" readonly="readonly" onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowToday:false,lang:'<%=empLangName%>'})"
								id="starttime" name="starttime" value="<%=startTime %>">
							<span><emp:message key="txgl_wgqdpz_dcspzh_z" defVal="至" fileName="txgl"></emp:message></span>
							<input type="text"  
								class="Wdate div_bd endtime" readonly="readonly" onclick="WdatePicker({dateFmt:'HH:mm:ss',isShowToday:false,lang:'<%=empLangName%>'})"

								id="endtime" name="endtime" value="<%=endTime %>">
	                        <span class="xxrxfsdsjd_span"><emp:message key="txgl_wgqdpz_dcspzh_xxrxfsdsjd" defVal="信息允许发送的时间段，为空则默认为00:00:00-23:59:59，表示不受限制" fileName="txgl"></emp:message></span>
                         </td>
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
									</ul><a id="add-ip" class="ip-btn"><emp:message key="txgl_wgqdpz_dcspzh_jxtj" defVal="继续添加" fileName="txgl"></emp:message></a> <a id="remove-ip" class="ip-btn"><emp:message key="txgl_wgqdpz_dcspzh_sc" defVal="删除" fileName="txgl"></emp:message></a>
		                         </div>
		                         <span class="zspzipdz_span"><emp:message key="txgl_wgqdpz_dcspzh_zspzipdz" defVal="在所配置IP地址对应的服务器上使用SP账号发送信息，为空则不受限制" fileName="txgl"></emp:message></span>
	                         </td>
                         </tr>
                          <% } %>
                         <tr>
                         	<td colspan="2"  id="btn">
                         	<div  class="mt10 btnSsu_div">
	                         	<input  type="submit" id="btnSsu" value="<%=qd %>" class="btnClass5 mr23"/>
	                         	<input type="button" onclick="javascript:history.go(-1)" value="<%=fh %>" class="btnClass6"/>
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
		<div id="showexplain">
			<table>
					<td class="sxztbg_td" align="left">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_sxztbg" defVal="上行、状态报告获取方式说明：" fileName="txgl"></emp:message></td>
			</table>
  					<ul>
  						<li class="showexplain_ul">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_hjkwgzdts" defVal="1、HTTP/HTTPS接口网关主动推送：网关通过HTTP/HTTPS协议推送到指定URL。" fileName="txgl"></emp:message></li>
  						<li class="showexplain_ul">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_sjkwgzdts" defVal="2、SGIP协议网关主动推送：网关通过SGIP协议推送到指定URL（限SGIP协议登录账户使用）。" fileName="txgl"></emp:message></li>
  						<li class="showexplain_ul">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_wgzlzf" defVal="3、网关直连转发、用户主动获取：网关通过TCP连接方式，以字节流socket通信方式主动推送。" fileName="txgl"></emp:message></li>
 						<li class="showexplain_ul">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_atgcmpp" defVal="a.用户通过CMPP、SMGP协议socket连接的方式登录到网关后，网关以登录的链接转发上行和状态报告。" fileName="txgl"></emp:message></li>
 						<li class="showexplain_ul">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_atweb" defVal="b.用户通过Webservice获取上行状态报告的接口主动向网关请求获取上行和状态报告。" fileName="txgl"></emp:message></li>
  						<li class="showexplain_ul">&nbsp;&nbsp;&nbsp;<emp:message key="txgl_wygl_wytdgl_4" defVal="4、无需推送和获取：状态报告及上行不需要推送至业务系统，同时也不需要主动向EMP网关获取。" fileName="txgl"></emp:message></li>
  					</ul>
		</div>
    <div class="clear"></div>
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=iPath%>/js/userdata.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script language="javascript" src="<%=iPath%>/js/ip.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script language="javascript" src="<%=iPath%>/js/morpoption.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	
	<script>
		function checkUserType(loginid){
			if(loginid=="1" || loginid=="2")
			{
				$("#lbUserType").val("WBS00A");
			}else
			{
				$("#lbUserType").val("DBS00A");
			}
			//if(loginid=="2"){
				//$("#trip").show();
			//}else{
				//$("#trip").hide();
				//removeIps();
			//}
		}
		$(document).ready(function(){
			getLoginInfo("#corpCode");
			getLoginInfo("#loginUser");
			checkUserType($("#select").val());
			ControlsCtrl($("#morptmode").val());
			show(<%=result %>);
			
			$("#morptmode").change(function(){
				var morptnode = $("#morptmode").val();
				ControlsCtrl(morptnode);
			});

			var $pushmourl = $("#pushmourl");
			var $pushrpurl = $("#pushrpurl");
			var morptnode = $("#morptmode").val();
			if($pushmourl.is(":checked") || $pushrpurl.is(":checked")) {
				$("#trmorptmode").show();
				ControlsCtrl(morptnode)
			} else {
				$("#trmorptmode").hide();
				$("#trpushversion").hide();
				$("#trmorpturl").hide();
				$("#trsxurl").hide();
				$("#trpturl").hide();
			}
		});
		function show(i)
		{
			if(i == 1)
			{
				//alert("新建账户成功！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_28"));
				location.href="<%=path%>/pag_userData.htm?lgcorpcode=<%=request.getParameter("lgcorpcode") %>";
			}else if(i == 2)
			{
				//alert("修改账户成功！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_29"));
				location.href="<%=path%>/pag_userData.htm?lgcorpcode=<%=request.getParameter("lgcorpcode") %>";
			}else if(i != 0)
			{
				//alert("操作失败！");
				alert(getJsLocaleMessage("txgl","txgl_wgqdpz_dcspzh_text_30"));
			}	
		}
		
		function back(){
			var codecorp  = $("#lgcorpcode").val();
			window.location.href='<%=path %>/pag_userData.htm?method=find&lgcorpcode='+codecorp;
		}
		

		function ControlsCtrl(morptnode)
		{
			if(morptnode == 0)
			{
				$("#trmorpoption").show();
				$("#trmorpturl").hide();
				$("#trsxurl").hide();
				$("#trpturl").hide();
				$("#trpushversion").hide();
			}
			else if(morptnode == 1)
			{
				$("#trmorpturl").show();
				$("#trmorpoption").show();
				$("#trsxurl").hide();
				$("#trpturl").hide();
				$("#trpushversion").hide();
			}
			else if(morptnode == 2)
			{
				$("#trmorpturl").hide();
				$("#trmorpoption").show();
				if($("#pushmourl").is(":checked")) {
					$("#trsxurl").show();
				} else {
					$("#trsxurl").hide();
				}
				if($("#pushrpurl").is(":checked")){
					$("#trpturl").show();
				} else {
					$("#trpturl").hide();
				}
				$("#trpushversion").show();
			} else if(morptnode == -1){
				$("#trmorpturl").hide();
				$("#trsxurl").hide();
				$("#trpturl").hide();
				$("#trpushversion").hide();
				$("#trmorpoption").hide();
			}
		}
		function showExplain(){
			var ver = $('#showexplain');
			if(ver.is(':hidden'))
			{
				ver.show(600);
			}
			else
			{
				ver.hide(600);
			}
		} 
	</script>
	</body>
</html>
