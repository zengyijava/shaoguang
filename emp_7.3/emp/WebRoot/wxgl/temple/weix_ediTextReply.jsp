<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.wxgl.LfWeiAccount" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

    Long currid = (Long) request.getAttribute("currid");
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));

    String result = request.getAttribute("w_routeResult") == null ? "-1" : (String) request.getAttribute("w_routeResult");
    request.removeAttribute("w_routeResult");
    String pageSize = request.getParameter("pageSize");
    String pageIndex = request.getParameter("pageIndex");
    //模板类型
    String tempType = request.getParameter("tempType");
    //起始时间
    String startdate = request.getParameter("startdate");
    //结束时间
    String enddate = request.getParameter("enddate");
    //公众账号ID
    String accoutid = request.getParameter("accoutid");
    //关键字
    String serkey = request.getParameter("serkey");
    //回复内容
    String serReply = request.getParameter("serReply");
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<%!public String turn(Object obj)
    {
        //下面的代码将字符串以正确方式显示（包括回车，换行，空格）

        if(obj == null)
            return "";
        String str = obj.toString();
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        return str;
    }%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="wxgl_gzhgl_title_380" defVal="修改文本" fileName="wxgl"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" >
		<link href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body >
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<div id="container" class="container">
			<%-- header开始 --%>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" id="commonPath" value="<%=commonPath%>"/>
			<input type="hidden" id="path" value="<%=path%>"/>
			<input type="hidden" id="msgleng" value="${msgleng }"/>
			<input type="hidden" id="keywords" value="${keywords }"/>
				<div class="titletop">
					<table class="titletop_table" style="width:510px;">
						<tr>
							<td class="titletop_td">
								<emp:message key="wxgl_gzhgl_title_381" defVal="修改文本内容" fileName="wxgl"/>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="wxgl_gzhgl_title_378" defVal="返回上一级" fileName="wxgl"/></font>
							</td>
						</tr>
					</table>
				</div>
            	<div id="detail_Info" style="padding-left: 40px;width: 610px;"> 
                <form name="updateInfo" method="post" action="<%=path%>/weix_keywordReply.htm?method=find">
                <div id="getloginUser" style="display:none;"></div>
                 	  
                	<input type="hidden" id="accoutid" value="${tid}"/>             
                	<table  width="104%" height="100%" style="font-size: 12px;line-height: 40px !important; line-height: 30px;">         	
       			 	<thead>
               	  		 <tr>
               	  		 <td width="100"><span><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/></span></td>
               	  		   <td><label>
	                     		<select name="accuntnum" id="accuntnum" class="input_bd" style="width:265px;" >
	                     				<option value=""><emp:message key="wxgl_gzhgl_title_77" defVal="所有公众帐号" fileName="wxgl"/></option>
	                     				<%
	                     				    List<LfWeiAccount> list = (List<LfWeiAccount>) request.getAttribute("accoutlist");
	                     				    if(list != null && list.size() > 0)
	                     				    {
	                     				        for (LfWeiAccount li : list)
	                     				        {
	                     				%>
	                     							<option value="<%=li.getAId()%>" <%=(currid - li.getAId() == 0) ? "selected" : ""%>><%=li.getName()%></option>
	                     					<%
	                     					    }
	                     					%> 
	                     					<%
 	                     					    }
 	                     					%>
	                     				
	                  
	                     		</select></label>
	                     	</td>  
                     	
                     	</tr>
                         <tr >
		                    	<td>
									<span><emp:message key="wxgl_gzhgl_title_355" defVal="回复名称：" fileName="wxgl"/></span>
								</td>
								<td>
									<input type="text"  name="replyname" id="replyname" maxlength="32" value="${replname}" class="input_bd reply_name" style="width:260px;" />
									<font color="red">*</font>
								</td>
	                    	</tr>                                        
						<tr>
							<td>
								<span><emp:message key="wxgl_gzhgl_title_126" defVal="关键字：" fileName="wxgl"/></span>
							</td>
							<td>
								<input type="text"  name="signstr" id="keyword"    class="input_bd" style="width:260px;" />
								<font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" class="btnClass2" onclick="addKeyword()"  value="<emp:message key='wxgl_button_1' defVal='添加' fileName='wxgl'/>" />
							</td>
						</tr>
						<tr>
							<td>
								<span></span>
							</td>
							<td>
								<span><font color="#808080"><emp:message key="wxgl_gzhgl_title_356" defVal="多个关键字之间可以用空格隔开" fileName="wxgl"/>&nbsp;&nbsp;<emp:message key="wxgl_gzhgl_title_357" defVal="如：衣服 衬衫 帽子" fileName="wxgl"/></font>
										
								</span>
							</td>
						</tr>
                    	<tr>
                    		<td></td>
                    		<td>
                    			<div id="getObject" class="div_bd" style="width:408px;height:auto;">
                    			<table id="infomaTable" width="100%" style="border-left: none;">
												<tr  class="title_bg">
													<td  class="div_bd"  style="border-left: none;height: 25px;" align="center"><emp:message key="wxgl_gzhgl_title_72" defVal="关键字" fileName="wxgl"/></td>
													<td   class="div_bd" align="center" style="height: 25px;"><emp:message key="wxgl_gzhgl_title_358" defVal="完全匹配" fileName="wxgl"/></td>
													<td   class="div_bd" style="height: 25px;" align="center"><emp:message key="wxgl_gzhgl_title_359" defVal="包含匹配" fileName="wxgl"/></td>
													<td   class="div_bd" style="border-right:0;height: 25px;" align="center"><emp:message key="wxgl_gzhgl_title_7" defVal="操作" fileName="wxgl"/></td>
												</tr>
								</table>
								<table  width="100%" style="border-left: none;">
												
												<tr align="left">
												<td colspan="4">
													<span><FONT COLOR="#808080"><emp:message key="wxgl_gzhgl_title_376" defVal="完全匹配，和关键字一样才会触发! 包含匹配，只要包含本关键字就触发!" fileName="wxgl"/></FONT>
															
													</span>
												</td>
												</tr>
								</table>
								</div>
                    		</td>
                    	</tr>
                    	
						
                    	<tr >
	                    	<td><span><emp:message key="wxgl_gzhgl_title_62" defVal="回复内容：" fileName="wxgl"/></span></td>
	                    	<td>
	                    	<div style="width:530px;height:144px;">
	                    	<textarea rows="8" cols="50" id="replycont" style="width:408px;height:136px;color:#666;padding:3px;"><%=turn(request.getAttribute("msgtext"))%></textarea>
	                    	
	                    	</div>
	                    	<div style="height:20px;line-height:20px;color:#656565;"><span id="sid"></span>/500</div>
	                    	</td>
                    	</tr>
                         <tr>
                         	<td></td>
                         	<td  id="btn" align="right" style="padding-right:120px;">
                         		<input type="button" onclick="save(this)"  value="<emp:message key='wxgl_button_3' defVal='确定' fileName='wxgl'/>" class="btnClass5 mr23"/>
		    <%-- 参数传递 --%>
		     <input name="pageSize" type="hidden" value='<%=pageSize%>' >
		     <input name="pageIndex" type="hidden" value='<%=pageIndex%>' >
		     <input name="tempType" type="hidden" value='<%=tempType%>' >
		     <input name="startdate" type="hidden" value='<%=startdate%>' >
		     <input name="enddate" type="hidden" value='<%=enddate%>' >
		     <input name="serkey" type="hidden" value='<%=serkey%>' >
		     <input name="serReply" type="hidden" value='<%=serReply%>' >
		     <input name="accoutid" type="hidden" value='<%=accoutid%>' >
		    <%-- 参数传递 --%>
                         		<input type="button" onclick="back()"  value="<emp:message key='wxgl_button_8' defVal='返回' fileName='wxgl'/>" class="btnClass6"/>
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
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
   	<script src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.InputLetter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=iPath%>/js/editText.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/blankfilter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
