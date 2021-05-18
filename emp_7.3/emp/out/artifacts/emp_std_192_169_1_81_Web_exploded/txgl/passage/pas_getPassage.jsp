<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.servmodule.txgl.entity.XtGateQueue"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	XtGateQueue gate = (XtGateQueue) request.getAttribute("gate");
	int privilege = gate.getGateprivilege();
	boolean isSupportEn = (privilege&2)==2;
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String txglFrame = skin.replace(commonPath, inheritPath);
    
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
    //签名
    String qm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_qm", request);
    if(qm!=null&&qm.length()>1){
    	qm = qm.substring(0,qm.length()-1);
    }
   //返回 
    String fh = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_fh", request);
    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/common/common.jsp" %>
<title><emp:message key="txgl_wghdpz_tdgl_tdglxx" defVal="通道详细信息" fileName="txgl"></emp:message></title>

<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
<link href="<%=commonPath%>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />

<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/pas_getPassage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/pas_getPassage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
</head>
<body id="pas_getPassage">
    <div id="container">
    	<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,"2000-1300",MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_tdglxx", request)) %>
    	<div class="rContent">
    	<div class="titletop tdglxx_div"  >
            <emp:message key="txgl_wghdpz_tdgl_tdglxx" defVal="通道详细信息" fileName="txgl"></emp:message>
        </div>
			<div id="corpCode" class="hidden"></div>
			
    		<div class="tdlx_div">
             	<table class="tdlx_table">
   			 	<thead>
           	  		
                     <tr>
                     <td><span><emp:message key="txgl_wghdpz_tdgl_tdlx" defVal="通道类型：" fileName="txgl"></emp:message>：</span></td>
                        <td><%
                       	Integer gatetype = gate.getGateType();
                        if(gatetype==1){out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dux", request));}
                        else if(gatetype==2){out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_cx", request));}
                        %></td>
                       <td><span><emp:message key="txgl_wghdpz_tdgl_yys" defVal="运营商：" fileName="txgl"></emp:message></span></td>
                        <td><%
                       	Integer spisuncm = gate.getSpisuncm();
                        if(spisuncm==0){out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_yd", request));}
                        else if(spisuncm==1){out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_lt", request));}
                        else if(spisuncm==21){out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dx", request));}
                        else if(spisuncm==5){out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_gw", request));}
                        %></td>
                     </tr>
                      <tr>
                        <td><span><emp:message key="txgl_wghdpz_tdgl_tdmc" defVal="通道名称：" fileName="txgl"></emp:message></span></td>
                     	<td><span class="maxWidth" title="<%=gate.getGateName()%>"><%=gate.getGateName() %></span></td>
                        <td><span><emp:message key="txgl_wghdpz_tdgl_tdhm" defVal="通道号码：" fileName="txgl"></emp:message></span></td>
                        <td><%=gate.getSpgate() %></td>
                     </tr>
                     
                      <tr>
                     <td><span><emp:message key="txgl_wghdpz_tdgl_zdkzws" defVal="最大扩展位数：" fileName="txgl"></emp:message></span></td>
                     	<td><%=gate.getSublen() %><emp:message key="txgl_wghdpz_tdgl_w" defVal="位" fileName="txgl"></emp:message></td>
                     	<td>
                            <span>
                            <%if(gatetype==1){
                                out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dux", request));
                            } else if(gatetype==2){
                                out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_cx", request));
                            }
                            %>
                            <%=qm%>
                            <%if(gatetype==1&&spisuncm==5&&isSupportEn){
                                out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_zwyw", request));}
                            %>：
                            </span>
                        </td>
                        <td><span><xmp><%if(gatetype==1&&spisuncm==5&&isSupportEn){out.print(StringUtils.defaultIfEmpty(gate.getSignstr().trim(),"-")+"/"+StringUtils.defaultIfEmpty(gate.getEnsignstr().trim(),"-"));}else{out.print(StringUtils.defaultIfEmpty(gate.getSignstr().trim(),"-"));} %></xmp></span></td>
                                  
                     </tr>
                     <%--
                     <tr>
                      <td><span>通道优先级：</span></td>
                        <td><%=gate.getRiseLevel() %>
                        </td>
                         <td><span>状态：</span></td>
                        <td><%=gate.getStatus()-0==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_jh", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_sx", request) %></td>
                     </tr>
                     --%><%
			             //是否由运营商加签名
						String signdroptype=gate.getSignDropType()!=null?gate.getSignDropType().toString():"";
						String signMode="";
						if("0".equals(signdroptype)){
							//短信签名长度模式 固定长度 自动计算
							signMode="1";
						}else{
							//短信签名长度模式 固定长度 自动计算
							signMode="0";
						}
						if("0".equals(signdroptype)||"1".equals(signdroptype)){
							//是否由运营商加签名
							signdroptype="1";
						}else{
							//是否由运营商加签名
							signdroptype="0";
						}
                     
                     
                     if(gatetype==1){ 
                     
                     %>
                     <tr>
                     <td><span><emp:message key="txgl_wghdpz_tdgl_qmcdms" defVal="签名长度模式：" fileName="txgl"></emp:message></span></td>
                        <td>
                        <%="1".equals(signMode)?MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_gdcd", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_zdjs", request) %>
                        </td>           
                      <td><span><emp:message key="txgl_wghdpz_tdgl_qmwz" defVal="签名位置：" fileName="txgl"></emp:message>：</span></td>
                        <td>
                        <%=(privilege&4)==4?MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_qz", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_hz", request) %>
                        </td>
                     </tr>
                      <tr>
                      <td><span><emp:message key="txgl_wghdpz_tdgl_yyysjqm" defVal="由运营商加签名：" fileName="txgl"></emp:message></span></td>
                        <td>
                        <%="1".equals(signdroptype)?MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_s", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_f", request) %>
                        </td>
                         <td><span><emp:message key="txgl_wghdpz_tdgl_zccdx" defVal="支持长短信：" fileName="txgl"></emp:message></span></td>
                        <td><%=gate.getLongSms()-1==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_s", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_f", request) %>
                        </td>
                        
                     </tr> 
                       <tr>
                        <td><span><emp:message key="txgl_wghdpz_tdgl_acdxcf" defVal="按长短信拆分：" fileName="txgl"></emp:message></span></td>
                        <td><%=gate.getSplitRule()-1==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_s", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_f", request) %>
                        </td>
                         <td><span><emp:message key="txgl_wghdpz_tdgl_zczttj" defVal="支持整条提交：" fileName="txgl"></emp:message></span></td>
                        <td>
                        <%=gate.getEndSplit()-1==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_s", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_f", request) %>
                        </td><%--
                      <td><span>短信签名长度：</span></td>
                     	<td><%=gate.getSignlen()==0?gate.getSignstr().trim().length():gate.getSignlen() %>
                     	</td> 
                     
                        
                     --%></tr>
                       <%--<tr>
                     
                        <td><span>逐条签名：</span></td>
                        <td><%=gate.getEachSign()-1==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_s", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_f", request) %>
                        </td>
                        
                     </tr>
                     --%><tr>
                        <%--<td><span>短信最大字数：</span></td>
                        <td>
                        	<%=gate.getMaxWords() %>
                        </td>
                     	--%>
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_dtdxzs" defVal="单条短信字数" fileName="txgl"></emp:message><%if(spisuncm==5&&isSupportEn){out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_zwyw", request));} %>：</span></td>
                        <td><%if(spisuncm==5&&isSupportEn){out.print(gate.getSingleLen()+"/"+gate.getEnsinglelen());}else{out.print(gate.getSingleLen());} %></td>
                        <td><span><emp:message key="txgl_wghdpz_tdgl_fl" defVal="费率：" fileName="txgl"></emp:message></span></td>
                        <td><%=gate.getFee() %><emp:message key="txgl_wghdpz_tdgl_yt" defVal="元/条" fileName="txgl"></emp:message></td>
                     </tr>
                      <tr>
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_hcfdzdzs" defVal="后拆分最大字数" fileName="txgl"></emp:message><%if(spisuncm==5&&isSupportEn){out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_zwyw", request));} %></span></td>
                        <td><%if(spisuncm==5&&isSupportEn){out.print(gate.getEsplitmaxwd()+"/"+gate.getEsplitenmaxwd());}else{out.print(gate.getEsplitmaxwd());} %></td>
                     </tr>
                     
                     <% }else{ %>
                    
                     <tr>
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_fl" defVal="费率：" fileName="txgl"></emp:message></span></td>
                        <td><%=gate.getFee() %><emp:message key="txgl_wghdpz_tdgl_yt" defVal="元/条" fileName="txgl"></emp:message></td>
                        <td></td>
                        <td></td>
                        <%--
                       <td><span>通道长度：</span></td><td ><%=gate.getSpgate().length() %></td>
                     --%></tr>
                     <%} %>
                     <tr align="center" >
                     	<td colspan="4" id="btn" class="btn">
                     		<input type="button" onclick="javascript:history.go(-1)" value="<%=fh %>" class="btnClass6"/>
                     	</td>
                     </tr>
              </thead>
            </table>
          </div>
    	</div>
    	<div class="bottom"><div id="bottom_right"><div id="bottom_left"></div></div></div>
	</div><%--end round_content--%>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		getLoginInfo("#corpCode");
	});
		function back()
		{
			window.location.href='pas_passage.htm?isback=1&lguserid='+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
		}
	</script>
</body>
</html>
