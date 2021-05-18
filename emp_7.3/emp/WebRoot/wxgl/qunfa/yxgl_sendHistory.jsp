<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.ottbase.util.StringUtils"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiSendlog"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    @SuppressWarnings("unchecked")
    List<LfWeiAccount> otWeiAcctList = (List<LfWeiAccount>)request.getAttribute("otWeiAcctList");
    //公众帐号过滤
    String aid = request.getParameter("aId");
    PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");
    @SuppressWarnings("unchecked")
    List<LfWeiSendlog> logList = (List<LfWeiSendlog>) request.getAttribute("logList");
    Map<Long,String> accountNameMap = new HashMap<Long,String>();
    String wxskin = skin.indexOf("/frame") == -1 ? "default" : skin.substring(skin.indexOf("/frame")+1, skin.length());
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    String tw = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_350", request);
    String wb = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_74", request);
    String fzqf = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_348", request);
    String dqfs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_349", request);
    String fscg = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_351", request);
    String fssb = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_352", request);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<div id="container" class="container">
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="yxgl_sendHistory.htm?method=find" method="post" id="pageForm">
					<div style="display: none" id="hiddenValueDiv"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
					</div>
					<div id="condition">
					<table>
							<tbody>
								<tr>
									
									<td><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/>
									</td>
									<td>
										<label>
										<select id="aId" name="aId" class="input_bd" >
											<option value=""><emp:message key="wxgl_gzhgl_title_135" defVal="请选择公众帐号" fileName="wxgl"/></option>
											<%
												if (otWeiAcctList != null && otWeiAcctList.size() > 0) {
													for (LfWeiAccount acct : otWeiAcctList) {
														Long aId = acct.getAId();
														accountNameMap.put(aId,acct.getName());
											%>
											<option value="<%=acct.getAId()%>"
												<%=(aId.toString().equals(aid)) ? "selected" : ""%>>
												<%=acct.getName()%></option>
											<%
												}
												}
											%>
										</select>
										</label>
									</td>
									<td class="tdSer">
										<a id="search"></a>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="container" class="container">
					<div class="hd">
		            </div>
		            <div class="bd">
                    <table id="content">
                        <thead>
                        <tr>
                          <th>
                            	<emp:message key="wxgl_gzhgl_title_66" defVal="公众帐号" fileName="wxgl"/>
                            </th>
                            <th>
                            	<emp:message key="wxgl_gzhgl_title_344" defVal="发送类型" fileName="wxgl"/><%-- 0：分组群发，1：按地区发送 --%>
                            </th>
                            <th>
                              	<emp:message key="wxgl_gzhgl_title_345" defVal="消息类型" fileName="wxgl"/><%-- 文本：text,图文：mpnews --%>
                            </th>
                            <th>
                              	<emp:message key="wxgl_gzhgl_title_346" defVal="消息内容" fileName="wxgl"/>
                            </th>
                            <th>
                              	<emp:message key="wxgl_gzhgl_title_6" defVal="创建时间" fileName="wxgl"/>
                            </th>
                            <th>
                            	<emp:message key="wxgl_gzhgl_title_347" defVal="发送状态" fileName="wxgl"/>
                            </th>
                            <th>
                            	<emp:message key="wxgl_gzhgl_title_7" defVal="操作" fileName="wxgl"/>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <%if(logList == null||logList.size()==0) {%>
                        <tr><td colspan="7"><emp:message key="wxgl_gzhgl_title_11" defVal="无记录" fileName="wxgl"/></td></tr>
                        <%} else{
                        for(LfWeiSendlog log : logList)
                        {
                        %>
                        <tr>
                        	<td>
                        		<%=accountNameMap.get(log.getAId()) %>
                        	</td>
                        	<td>
                        		<%
                       			String tp = log.getTp();
                       			out.print("0".equals(tp)?fzqf:dqfs);
                        		%>
                        	</td>
                        	<td>
                        		<%
                       			String msgType = log.getMsgType();
                    			out.print("text".equals(msgType)?wb:tw);
                        		%>
                        	</td>
                        	<td>
                        		<%
                        		String sendContent = log.getSendContent();
                        		if("mpnews".equals(msgType))
                        		{
                        		    out.print("<a onclick='preview("+log.getTId().toString()+")'>");
                        		}
                       		    %>
                       		    <xmp><%
                       		    if(sendContent != null && sendContent.length() > 9)
                       		    {
                       		        out.print(sendContent.substring(0,6)+"...");
                       		    }else
                       		    {
                       		        out.print(sendContent==null?"":sendContent);
                       		    }
                       		    %></xmp>
                       		    <div class="titleTip" style="display:none"><xmp><%=sendContent %></xmp></div>
                       		    <% 
                        		if("mpnews".equals(msgType))
                        		{
                        		    out.print("</a>");
                        		}
                        		%>
                        		
                        	</td>
                        	<td>
                        		<%=StringUtils.timeFormat(log.getCreatetime()) %>
                        	</td>
                        	<td>
                        		<%=log.getStatus()==0?"<font color=red>"+fssb+"</font>":fscg %>
                        	</td>
                        	<td>
                        		<a href="#1" onclick="reSendAll('<%=log.getSendId()%>','<%=log.getMsgType()%>');"><emp:message key="wxgl_gzhgl_title_353" defVal="重发" fileName="wxgl"/></a>
                        	</td>
                        </tr> 
                        <%} }%>
                        </tbody>
                        <tfoot>
                        	<tr>
                            <td colspan="7">
                                <div id="pageInfo">
                                </div>
                            </td>
                        </tr>
                        </tfoot>
                     </table>
                     </div>
				</div>
			</form>	
			<input type="hidden" id="pathUrl" value="<%=path%>" />
		</div>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.InputLetter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
			$(document).ready(function() {
				getLoginInfo("#hiddenValueDiv");
				$("#aId").isSearchSelect({'width':'205','isInput':false,'zindex':0});
				currentTotalPage="<%=pageInfo.getTotalPage()%>";
				currentPageIndex="<%=pageInfo.getPageIndex()%>";
				currentPageSize="<%=pageInfo.getPageSize()%>";
				currentTotalRec="<%=pageInfo.getTotalRec()%>";
				initPage(currentTotalPage,currentPageIndex,currentPageSize,currentTotalRec);
				$('#search').click(function(){submitForm();});
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
			});
			//回复预览
			function preview(tempId){
				var url = "weix_keywordReply.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
				showbox({src:url});
			}
			
			//重新选择发送范围
			function reSendAll(sendlogId,tp)
			{
				var pathUrl = $("#pathUrl").val();
				var lgcorpcode = $("#lgcorpcode").val();
				var height = "mpnews" == tp ? 400 : 450;
	
				var frameSrc = pathUrl+"/yxgl_fzqfManager.htm?method=reSendAll"+"&lgcorpcode="+lgcorpcode + "&sendlogId="+sendlogId;
				var aboutConfig={
					content:getIframe(frameSrc,750,height,"reSendAll"),
			        title: getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_61"),
			        lock: true,
			        opacity: 0.5,
			        ok: function(){
							var iframe = $("#reSendAll")[0].contentWindow;
							if(!iframe.document.body){
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_62"));
					        	return false;
					        };
					        var form = iframe.document.getElementById("pageForm");
					        
					        var aid = $("#aid",form).val();
							var gid = $("#groupid",form).val();
							var pathUrl = $("#pathUrl",form).val();
							var tp = $("#tp",form).val();
							var sendlogId = $("#sendlogId",form).val();
							
							if("" == aid||null==aid){
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_50"));
								return false;
							}
							
							//群组默认为"000"
							if("" == gid||null==gid){
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_51"));
								return false;
							}
							
							var data = {method: "doRepeatSendAll",gid:gid,tp:tp,sendlogId:sendlogId,aid:aid,isAsync:"yes"};
							
							//发送对象为地区
							if("1" == tp){
								data.country = $("#areaid",form).val();
								data.province = $("#province",form).val();
								data.city = $("#city",form).val();
							}

							$.ajax({
								url: pathUrl + "/yxgl_fzqfManager.htm",
								dataType: "text",
								type: "POST",
								data: data,
							 	success: function(data){
									if(data == "outOfLogin"){
							 			window.location.href = pathUrl + "/common/logoutEmp.html";
							 		}
							 		if("parameterError"== data){
							 			alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_54"));
							 		}else if("nomember" == data){
							 			alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_55"));
							 		}else if("msgTypeError" == data){
							 			alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_56"));
							 		}else if(data == undefined || "error" == data ||"" == data){
							 			alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_57"));
							 		}else if("uploadImgError" == data){
							 			alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_63"));
							 		}else if(data == undefined || "error" == data ||"" == data){
							 			alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_64"));
							 		}else{
							 			//alert(data);
							 			//var dataJson = $.parseJSON(data);
							 			//if(dataJson.errcode != undefined && (dataJson.errcode == "000"||dataJson.errcode=="0")){
							 			if(data=="success"){
							 				alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_65"));
							 				//art.dialog({
							 				 //   time: 10,
							 				//    content: "消息已成功提交到微信服务器，等待审核发送！"
							 				//});
							 			}
							 		}
							 		submitForm();
							 		return false;
							 	}
							});
						},
						cancel: true
			    };
				dlog = art.dialog(aboutConfig);
				setTimeout(function(){$(".aui_content").css("padding","5");},200);
			}
			
			function getIframe(src,width,height,frameid)
			{
				return '<iframe id="'+frameid+'" src = "'+src+'" frameborder="0" allowtransparency="true" style="width: '
					+width+'px; height: '+height+'px; border: 0px none;"></iframe>';
			}
	    </script>
	     <link href="<%=commonPath %>/wxcommon/css/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    	 <link href="<%=commonPath %>/wxcommon/<%=wxskin %>/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    	  <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link href="<%=path %>/wxcommon/css/artDialogCss_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%}%>
	</body>
</html>