<%@ page language="java" import="java.util.List" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.pasroute.GtPortUsed" %>
<%@ page import="com.montnets.emp.servmodule.txgl.entity.XtGateQueue" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.GwUserproperty"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path=request.getContextPath();

	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	Integer corpType = StaticValue.getCORPTYPE();
	GwUserproperty gwUserproperty=null;
	if(request.getAttribute("userProperty")!=null){
		List<GwUserproperty> list=(List<GwUserproperty>)request.getAttribute("userProperty");
		if(list!=null&&list.size()>0){
			gwUserproperty=list.get(0);
		}
	}
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title>修改SP账号参数设置</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/frame/frame3.0/skin/default/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/frame/frame3.0/skin/default/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=commonPath %>/frame/frame3.0/skin/default/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >

        <%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mwp_editData.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/mwp_editData.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="mwp_editData">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent rContent2"  >
            <div id="detail_Info" class="detail_Info"> 
            
                <form name="updateInfo" method="post" action="<%=commonPath %>/mwp_userData.htm?method=save"> 
                <input type="hidden" id="userid" name="userid" value="<%=request.getParameter("userid") %>" />  
                  
                 <input type="hidden" id="pathUrl" name="pathUrl" value="<%=path %>" />
                <input type="hidden" id="userpropertyID" name="userpropertyID" value="<%=gwUserproperty!=null&&gwUserproperty.getId()!=null?gwUserproperty.getId():"" %>" />   
                	<table class="mmjm_table">         	
       			 	<thead>
               	  		 <tr>
               	  		 <td class="<%="zh_HK".equals(empLangName)?"mmjmfs_td1":"mmjmfs_td2"%>"  ><span><emp:message key='txgl_mwpasgroup_text_1' defVal='密码加密方式：' fileName='mwadmin'/></span></td>
               	  		  <td>
               	  		    <label>
               	  		    <select name="pwdencode" id="pwdencode" class="input_bd pwdencode"  onchange="changepwd();">
               	  		    <option value="1" <%=gwUserproperty!=null&&gwUserproperty.getPwdencode()==1?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_2' defVal='账号+固定字符串+密码时间戳' fileName='mwadmin'/></option>
	                        <option value="0" <%=gwUserproperty!=null&&gwUserproperty.getPwdencode()==0?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_3' defVal='明文' fileName='mwadmin'/></option>
	                            </select>
                     		</label>
                     		</td> 
                     		</tr >
                     		<tr id="pwdEncodetr" class="pwdEncodetr">  
                             <td><span><emp:message key='txgl_mwpasgroup_text_4' defVal='加密固定串：' fileName='mwadmin'/></span></td>
                            <td>
                            <label>
	                            <input  class="input_bd pwdEncodeStr" maxlength="10" type="text" value="<%=gwUserproperty!=null&&gwUserproperty.getPwdencodestr()!=null?gwUserproperty.getPwdencodestr():"00000000" %>" id="pwdEncodeStr" name="pwdEncodeStr"/>
                            </label>
                            </td>
                         </tr>
                         
                          <tr>  
                             <td><span><emp:message key='txgl_mwpasgroup_text_5' defVal='下行内容编码方式：' fileName='mwadmin'/></span></td>
                            <td>
                            <label>
	                            <select name="msgCode" id="msgCode" class="input_bd msgCode"  >
	                            	<option value="1" <%=gwUserproperty!=null&&gwUserproperty.getMsgcode()==1?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_3' defVal='明文' fileName='mwadmin'/></option>
	                            	<option value="2" <%=gwUserproperty!=null&&gwUserproperty.getMsgcode()==2?"selected":"" %>>URLENCODE</option>
	                            	<option value="3" <%=gwUserproperty!=null&&gwUserproperty.getMsgcode()==3?"selected":"" %>>BASE64</option>
	                            	<option value="4" <%=gwUserproperty!=null&&gwUserproperty.getMsgcode()==4?"selected":"" %>>UTF-8</option>
	                            </select>
                            </label>
                            </td>
                         </tr>


                          <tr>  
                             <td><span><emp:message key='txgl_mwpasgroup_text_6' defVal='下行内容加密方式：' fileName='mwadmin'/></span></td>
                            <td>
                            <label>
	                            <select name="msgEnCode" id="msgEnCode" class="input_bd msgEnCode"  >
	                            	<option value="1" <%=gwUserproperty!=null&&gwUserproperty.getMsgencode()==1?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_3' defVal='明文' fileName='mwadmin'/></option>
	                            	<option value="2" <%=gwUserproperty!=null&&gwUserproperty.getMsgencode()==2?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_7' defVal='预留(内容需要先加密后编码)' fileName='mwadmin'/></option>
	                            </select>
                            </label>
                            </td>
                         </tr>

                     		<tr >  
                             <td><span><emp:message key='txgl_mwpasgroup_text_8' defVal='推送密码加密方式：' fileName='mwadmin'/></span></td>
                            <td>
                            <label>
	                           <select name="pushPwdEncode" id="pushPwdEncode" class="input_bd pushPwdEncode"  onchange="changepush();">
	                           		<option value="1" <%=gwUserproperty!=null&&gwUserproperty.getPushpwdencode()==1?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_2' defVal='账号+固定字符串+密码时间戳' fileName='mwadmin'/></option>
	                            	<option value="0" <%=gwUserproperty!=null&&gwUserproperty.getPushpwdencode()==0?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_3' defVal='明文' fileName='mwadmin'/></option>
	                            	
	                            </select>
                            </label>
                            </td>
                         </tr>
                         
                     		<tr id="pushtr" class="pushtr">  
                             <td><span><emp:message key='txgl_mwpasgroup_text_9' defVal='推送加密固定串：' fileName='mwadmin'/></span></td>
                            <td>
                            <label>
	                           <input  class="input_bd pushPwdEncodeStr" type="text"  maxlength="10"
	                           value="<%=gwUserproperty!=null&&gwUserproperty.getPushpwdencodestr()!=null?gwUserproperty.getPushpwdencodestr():"00000000" %>" 
	                           id="pushPwdEncodeStr" name="pushPwdEncodeStr"/>
                            </label>
                            </td>
                         </tr>


                          <tr>  
                             <td><span><emp:message key='txgl_mwpasgroup_text_10' defVal='推送内容编码方式：' fileName='mwadmin'/></span></td>
                            <td>
                            <label>
	                            <select name="pushMsgCode" id="pushMsgCode" class="input_bd pushMsgCode"  >
	                            	<option value="2" <%=gwUserproperty!=null&&gwUserproperty.getPushmsgcode()==2?"selected":"" %>>URLENCODE</option>
	                            	<option value="1" <%=gwUserproperty!=null&&gwUserproperty.getPushmsgcode()==1?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_3' defVal='明文' fileName='mwadmin'/></option>
	                            	<option value="3" <%=gwUserproperty!=null&&gwUserproperty.getPushmsgcode()==3?"selected":"" %>>BASE64</option>
	                            	<option value="4" <%=gwUserproperty!=null&&gwUserproperty.getPushmsgcode()==4?"selected":"" %>>UTF-8</option>
	                            </select>
                            </label>
                            </td>
                         </tr>
                         
                           <tr>  
                             <td><span><emp:message key='txgl_mwpasgroup_text_11' defVal='推送内容加密方式：' fileName='mwadmin'/></span></td>
                            <td>
                            <label>
	                            <select name="pushMsgEnCode" id="pushMsgEnCode" class="input_bd pushMsgEnCode"  >
	                            <option value="1" <%=gwUserproperty!=null&&gwUserproperty.getPushmsgencode()==1?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_3' defVal='明文' fileName='mwadmin'/></option>
	                            <option value="2" <%=gwUserproperty!=null&&gwUserproperty.getPushmsgencode()==2?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_7' defVal='预留(内容需要先加密后编码)' fileName='mwadmin'/></option>
	                            </select>
                            </label>
                            </td>
                         </tr>

                            <tr>  
                             <td><span><emp:message key='txgl_mwpasgroup_text_12' defVal='推送上行格式：' fileName='mwadmin'/></span></td>
                            <td>
                            <label>
	                            <select name="pushMoFmt" id="pushMoFmt" class="input_bd pushMoFmt"  >
	                            	<option value="2"<%=gwUserproperty!=null&&gwUserproperty.getPushmofmt()==2?"selected":"" %>>JSON</option>
	                            	<option value="0" <%=gwUserproperty!=null&&gwUserproperty.getPushmofmt()==0?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_13' defVal='未知' fileName='mwadmin'/></option>
	                            	<option value="1" <%=gwUserproperty!=null&&gwUserproperty.getPushmofmt()==1?"selected":"" %>>XML</option>
	                            	<option value="4" <%=gwUserproperty!=null&&gwUserproperty.getPushmofmt()==4?"selected":"" %>>URLENCODE</option>
	                            </select>
                            </label>
                            </td>
                         </tr>
                         
                         <tr>  
                             <td><span><emp:message key='txgl_mwpasgroup_text_14' defVal='推送状态报告格式：' fileName='mwadmin'/></span></td>
                            <td>
                            <label>
	                            <select name="pushRptFmt" id="pushRptFmt" class="input_bd pushRptFmt"  >
	                            	<option value="2" <%=gwUserproperty!=null&&gwUserproperty.getPushrptfmt()==2?"selected":"" %>>JSON</option>
	                            	<option value="0" <%=gwUserproperty!=null&&gwUserproperty.getPushrptfmt()==0?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_13' defVal='未知' fileName='mwadmin'/></option>
	                            	<option value="1" <%=gwUserproperty!=null&&gwUserproperty.getPushrptfmt()==1?"selected":"" %>>XML</option>
	                            	<option value="4" <%=gwUserproperty!=null&&gwUserproperty.getPushrptfmt()==4?"selected":"" %>>URLENCODE</option>
	                            </select>
                            </label>
                            </td>
                         </tr>
                         <tr>  
                         <td><span><emp:message key='txgl_mwpasgroup_text_15' defVal='重推次数：' fileName='mwadmin'/></span></td>
                            <td>
                            <label>
	                            <select name="pushMoTime" id="pushMoTime" class="pushMoTime" onchange="changetimes();">
	                            	<option value="1" <%=gwUserproperty!=null&&gwUserproperty.getPushfailcnt()!=0?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_16' defVal='固定次数' fileName='mwadmin'/></option>
	                            	<option value="2" <%=gwUserproperty!=null&&gwUserproperty.getPushfailcnt()==0?"selected":"" %>><emp:message key='txgl_mwpasgroup_text_17' defVal='不限次数' fileName='mwadmin'/></option>
	                            </select>

                            </label>
                          <input  class="input_bd pushFailcnt" maxlength="1" type="text" onkeyup="value=value.replace(/[^\d]/g,'')" value="<%=(gwUserproperty!=null&&gwUserproperty.getPushfailcnt()!=null)?gwUserproperty.getPushfailcnt():"3" %>" id="pushFailcnt" name="pushFailcnt"/>
                            <font class="numbers" id="numbers" ><emp:message key='txgl_mwpasgroup_text_18' defVal='0表示不限次数' fileName='mwadmin'/></font>
                            </td>
                         </tr>
                         
                         
                         <tr>
                            <td><span><emp:message key='txgl_mwpasgroup_text_19' defVal='推送窗口大小' fileName='mwadmin'/></span></td>
                            <td>
							<input  class="input_bd pushSlideWnd" type="text" maxlength="2" onkeyup="value=value.replace(/[^\d]/g,'')" value="<%=gwUserproperty!=null&&gwUserproperty.getPushslidewnd()!=null?gwUserproperty.getPushslidewnd():"5" %>" id="pushSlideWnd" name="pushSlideWnd"/>
                            </td>
                            </tr>
                            
                            
                          <tr>
                            <td><span><emp:message key='txgl_mwpasgroup_text_20' defVal='推送MO最大条数' fileName='mwadmin'/></span></td>
                            <td>
							<input  class="input_bd pushMomaxCnt" maxlength="9" type="text" onkeyup="value=value.replace(/[^\d]/g,'')" value="<%=gwUserproperty!=null&&gwUserproperty.getPushmomaxcnt()!=null?gwUserproperty.getPushmomaxcnt():"100" %>" id="pushMomaxCnt" name="pushMomaxCnt"/>
                            </td>
                            </tr>
                              <tr>
                            <td><span><emp:message key='txgl_mwpasgroup_text_21' defVal='推送RPT最大条数' fileName='mwadmin'/></span></td>
                            <td>
							<input  class="input_bd pushrptmaxcnt" maxlength="9" type="text" onkeyup="value=value.replace(/[^\d]/g,'')" value="<%=gwUserproperty!=null&&gwUserproperty.getPushrptmaxcnt()!=null?gwUserproperty.getPushrptmaxcnt():"100" %>" id="pushrptmaxcnt" name="pushrptmaxcnt"/>
                            </td>
                            </tr>
                            
                            <tr>
                            <td><span><emp:message key='txgl_mwpasgroup_text_22' defVal='获得MO最大条数' fileName='mwadmin'/></span></td>
                            <td>
							<input  class="input_bd getmomaxcnt" maxlength="9" type="text" onkeyup="value=value.replace(/[^\d]/g,'')" value="<%=gwUserproperty!=null&&gwUserproperty.getGetmomaxcnt()!=null?gwUserproperty.getGetmomaxcnt():"100" %>" id="getmomaxcnt" name="getmomaxcnt"/>
                            </td>
                            </tr>
                            
                                                        <tr>
                            <td><span><emp:message key='txgl_mwpasgroup_text_23' defVal='获得RPT最大条数' fileName='mwadmin'/></span></td>
                            <td>
							<input  class="input_bd getrptmaxcnt" maxlength="9" type="text" onkeyup="value=value.replace(/[^\d]/g,'')" value="<%=gwUserproperty!=null&&gwUserproperty.getGetrptmaxcnt()!=null?gwUserproperty.getGetrptmaxcnt():"100" %>" id="getrptmaxcnt" name="getrptmaxcnt"/>
                            </td>
                            </tr>
                            
                          <tr align="left">
                         	<td id="btn" colspan="2">
                         	  <div  align="right" class="mt10 qd_div">
                         		<input type="button" onclick="save()" id ="arOk" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" class="btnClass5 mr23"/>
                         		<input type="button"  id="arNo" value="<emp:message key='txgl_mwpasgroup_text_25' defVal='返回' fileName='mwadmin'/>" class="btnClass6" onclick="javascript:back()"/>
                         		</div>
                         	</td>
                         </tr>
                  </thead>
                </table>
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
			<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
			<script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/mwp_userdata.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript">
		$(document).ready(function() {
			changepwd();
			changepush();
			var pushMoTime=$("#pushMoTime").val();
			if(pushMoTime=="1"){ 
				$("#pushFailcnt").attr("disabled","");
				$("#numbers").css('display','none');
			}else{
				$("#pushFailcnt").val("0");
				$("#pushFailcnt").attr("disabled","disabled"); 
				$("#numbers").css('display',''); 
			}
		});
		</script>
	</body>
</html>
