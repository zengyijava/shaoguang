<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Iterator" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

@SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("acctmanager");
menuCode = menuCode==null?"0-0-0":menuCode;
LinkedHashMap<String,String> tpList = (LinkedHashMap<String,String>)request.getAttribute("tpList");
String showType = request.getParameter("showType");
%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table_detail.css" rel="stylesheet"type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css" />
		<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/floating.css" rel="stylesheet" type="text/css" />	
		<link href="<%=skin %>/floating.css" rel="stylesheet" type="text/css" />	
		<style type='text/css'>
			table#editAcctTable tr {
				height: 42px;
			}
			table#editAcctTable td input.bd_none {
				width: 250px;
				
				border: 0;
			}
			table#editAcctTable td select {
				width: 246px;
				margin:4px 3px 4px 2px;
				border: 0; 
			}
			table .help-line{
				margin-top:5px;
			}
			.headimg100 {
			    border: 1px solid #CCCCCC;
			    border-radius: 500px 500px 500px 500px;
			    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
			    height: 80px;
			    width: 80px;
			    max-width: 100%;
    			vertical-align: middle;
			}
			.choose_file{
				position: relative;
			}
			.files{
				position: absolute;
				left: 0;
				top: 0;
				heigth: 28px;
				cursor: pointer;
				filter: Alpha(opacity=0);
				-moz-opacity: 0;
				opacity: 0;
				width: 330px;
			}
			.w_input{
				width:250px;
				height: 22px;
				line-height: 22px;
				outline: 0;
			}
		</style>
	</head>

	<body onload="show()">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- header开始 --%>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<center>
					<div >
						<form action="<%=path %>/cwc_acctmanager.htm?method=update" method="post" name="acctform" id="acctform" enctype="multipart/form-data">
						<div style="display:none" id="hiddenValueDiv"></div>
						<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
						<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
						
							<table id="editAcctTable" style="width: 610px;text-align: left;">
							<tbody>
								<tr style="display:none;">
									<td colspan="2">
										<input type="hidden" id="OpType" name="OpType" value="add" />
									</td>
								</tr>
                                <tr>
                                    <td>
                                        <span><emp:message key="weix_qywx_gzzhgl_text_11" defVal="公众帐号类型：" 
                                        	fileName="weix"></emp:message></span>
                                    </td>
                                    <td>
                                        <div style="float: left;" >
                                            <label>
                                                <select name="acctType" id="acctType">
										      <% 
										  		if(tpList!=null && tpList.size()>0) {          
											     for (Iterator it =  tpList.keySet().iterator();it.hasNext();){
											     	String key = (String)it.next();
											     	String value =  tpList.get(key); 
											    
										     %> 
										    	<option value="<%=key%>">
                                                        <%= value %>
                                                    </option>
										      <%}%>
										     <%}%>
                                                </select>
                                            </label>
                                        </div>
                                     <div class="help-line">
										<font color="red">&nbsp;&nbsp;&nbsp;*</font><span style="color:gray">
										<emp:message key="weix_qywx_gzzhgl_text_12" defVal="选择类别利于推广" 
                                        	fileName="weix"></emp:message></span>
									 </div>
                                    </td>
                                </tr>
								<tr>
									<td style="width: 90px;">
										<span><emp:message key="weix_qywx_gzzhgl_text_13" defVal="帐号名称：" 
                                        	fileName="weix"></emp:message></span>
									</td>
									<td>
									<div style="float: left;" class="input_bd">
										<label>
											<input name="acctName" id="acctName" class="bd_none"
												type="text"  maxlength="20" />
										</label><font color="red" style="display:none;">
										<emp:message key="weix_qywx_gzzhgl_text_14" defVal="长度不能大于20" 
                                        	fileName="weix"></emp:message>
										</font>
									</div>
									<div class="help-line">
										<font color="red">&nbsp;&nbsp;&nbsp;*</font><span style="color:gray"><emp:message key="weix_qywx_gzzhgl_text_15" defVal="请保持和公众平台名称一致" 
                                        	fileName="weix"></emp:message></span>
									</div>
									</td>
								</tr>
								<tr>
                                    <td style="width: 90px;">
                                        <span><emp:message key="weix_qywx_gzzhgl_text_16" defVal="微信帐号：" 
                                        	fileName="weix"></emp:message></span>
                                    </td>
                                    <td>
                                        <div style="float: left;" class="input_bd">
                                            <label>
                                                <input name="acctCode" id="acctCode" class="bd_none"
                                                       type="text"  maxlength="20" onkeypress="if(event.keyCode==13) return false;" />
                                            </label><font color="red" style="display:none;"><emp:message key="weix_qywx_gzzhgl_text_14" defVal="长度不能大于20" 
                                        	fileName="weix"></emp:message></font>
                                        </div>
                                     <div class="help-line">
                                         <font color="red">&nbsp;&nbsp;&nbsp;*</font><span style="color:gray"><emp:message key="weix_qywx_gzzhgl_text_17" defVal="公众平台微信号" 
                                        	fileName="weix"></emp:message></span>
									 </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 90px;">
                                        <span><emp:message key="weix_qywx_gzzhgl_text_18" defVal="原始帐号：" 
                                        	fileName="weix"></emp:message></span>
                                    </td>
                                    <td>
                                        <div style="float: left;" class="input_bd">
                                            <label>
                                                <input name="acctOpenId" maxlength="15" id="acctOpenId" class="bd_none"
                                                       type="text" onkeypress="if(event.keyCode==13) return false;" />
                                            </label><font color="red" style="display:none;"><emp:message key="weix_qywx_gzzhgl_text_14" defVal="长度不能大于20" 
                                        	fileName="weix"></emp:message></font>
                                        </div>
                                         <div class="help-line">
										<font color="red">&nbsp;&nbsp;&nbsp;*</font>
										<span style="color:gray"><emp:message key="weix_qywx_gzzhgl_text_19" defVal="格式：gh_0f231232867d" 
                                        	fileName="weix"></emp:message></span>
									 </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 90px;">
                                        <span><emp:message key="weix_qywx_gzzhgl_text_20" defVal="上传头像：" 
                                        	fileName="weix"></emp:message></span>
                                    </td>
                                    <td>
                                    <div class="choose_file">
						              <input type="text" id="txt" name="txt" class="w_input" style="margin-right:5px;">
									  <input type="button" value="<emp:message key="common_btn_11" defVal="浏览"
											fileName="common"/>" size="30" class="btnClass2">
						              <input type="file"  onchange="setIputname(this)" id="acctImg" name="acctImg" style="height:26px;" class="files" size="1" hidefocus="">
									  <font color="red" style="display:none;"><emp:message key="weix_qywx_gzzhgl_text_14" defVal="长度不能大于20" 
                                        	fileName="weix"></emp:message></font>
						    		</div>
                                        
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 90px;">
                                        <span><emp:message key="weix_qywx_gzzhgl_text_21" defVal="介绍文字：" 
                                        	fileName="weix"></emp:message></span>
                                    </td>
									<td style="height:126px;">
									<div style="float:left;width:522px;overflow:hidden;position: relative;">
										<label id="showEditor">
											<textarea id="acctInfo" style="width:518px;height:85px;" class="input_bd" name="acctInfo" id="acctInfo" onkeypress="if(event.keyCode==13) return false;"></textarea>
											<b style="bottom:-15px;left:0;color:#656565;display: block;"><span id="sid"></span>/215</b>
										</label><br>
										<font color="#7f7f7f" style="display: none;" id="fontFmTip"><emp:message key="weix_qywx_gzzhgl_text_22" defVal="参数格式为：“#P_1#”(如：我和#P_1#去#P_2#)" 
                                        	fileName="weix"></emp:message>)</font>
									</div>
									</td>
                                </tr>
                                <tr>
                                 <td></td>
									<td style="text-align:right;" id="btn">

										<input type="button" class="btnClass5 mr23" value="<emp:message key="common_btn_7" defVal="确定"
											fileName="common"></emp:message>" id="subBut" name="" >
										<input type="button" class="btnClass6" value="<emp:message key="common_btn_16" defVal="取消"
											fileName="common"></emp:message>" onclick="javascript:window.parent.closeaddAcctTmpDiv()" name="">
									  
									</td>
								</tr>
								</tbody>
							</table>
						</form>
					</div>
				</center>
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
   	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js"></script>
	<script src="<%=commonPath%>/common/js/floating.js" type="text/javascript"></script>
	<script src="<%=commonPath%>/common/js/tipcontent.js"></script>
	
		<%-- 国际化js加载 --%>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/weix_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	
	<script type="text/javascript" src="<%=iPath%>/js/account.js"></script> 
		
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.InputLetter.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js"></script>
 	<script type="text/javascript">
 	//判读是否新建成功
 	function show()
	{
		<% 
		String result=(String)request.getAttribute("tmresult");
		if(result!=null && "true".equals(result))
		{
		%>
			alert(getJsLocaleMessage("weix_qywx_gzzhgl_text_16","weix"));
		<%
		}
		else if(result!=null && "error".equals(result))
		{
		%>
			alert(getJsLocaleMessage("weix_qywx_gzzhgl_text_17","weix"));
		<%
		}
		request.removeAttribute("tmresult");
		if (result != null)
		{
		%>
		    window.parent.location.href="<%=path %>/cwc_acctmanager.htm?method=find&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
		<%
		}
		%>	
	}
	function setIputname(obj){
	    $(obj).prev().prev().val(obj.value);
	 }
	$(function(){
	  getLoginInfo("#hiddenValueDiv");
	  $('#acctType').isSearchSelect({'width':'253','isInput':false,'zindex':0});
	  $("#acctInfo").manhuaInputLetter({					       
			len : 215,//限制输入的字符个数				       
			showId : "sid"//显示剩余字符文本标签的ID
	  });
   	})
	</script>
	</body>
</html>