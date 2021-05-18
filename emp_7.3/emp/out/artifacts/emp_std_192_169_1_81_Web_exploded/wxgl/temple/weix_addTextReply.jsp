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

    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));

    String lgcorpcode = (String) request.getAttribute("lgcorpcode");

    String pageSize = request.getParameter("pageSize");

    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="wxgl_gzhgl_title_375" defVal="新建文本" fileName="wxgl"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<style type="text/css">
			#moreSelect{
				width: 610px;
				padding-top:20px;
			}
		</style>
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
			<input type="hidden" id="pageSize" value="<%=pageSize%>"/>
				<div class="titletop">
					<table class="titletop_table" style="width:510px;">
						<tr>
							<td class="titletop_td">
								<emp:message key="wxgl_gzhgl_title_379" defVal="新增文本内容" fileName="wxgl"/>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="wxgl_gzhgl_title_378" defVal="返回上一级" fileName="wxgl"/></font>
							</td>
						</tr>
					</table>
				</div>
            	<div id="detail_Info" style="padding-left: 40px;width: 610px;"> 
                <form name="updateInfo" method="post" action="">
                <div id="getloginUser" style="display:none;"></div>
                	<input type="hidden" value="add" name="hidOpType" id="hidOpType" />
                	<input type="hidden" value="update" name="method" id="update" />                
                	<div id="moreSelect">
                	<table  width="104%" height="100%" style="font-size: 12px;line-height: 40px !important; line-height: 30px;">         	
       			 	<thead>
               	  		 <tr>
               	  		 <td width="100"><span><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/></span></td>
               	  		  <td><label>
	                     		<select name="userid" id="accuntnum" class="input_bd" style="width:265px;" >
	                     				<option value=""><emp:message key="wxgl_gzhgl_title_77" defVal="所有公众帐号" fileName="wxgl"/></option>
	                     				<%
	                     				    List<LfWeiAccount> list = (List<LfWeiAccount>) request.getAttribute("accoutlist");
	                     				    if(list != null && list.size() > 0)
	                     				    {
	                     				        for (LfWeiAccount li : list)
	                     				        {
	                     				%>
	                     							<option value="<%=li.getAId()%>"><%=li.getName()%></option>
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
									<input type="text"  name="replyname" id="replyname" maxlength="32" class="input_bd reply_name" style="width:260px;" />
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
                    			<div id="getObject" class="div_bd" style="width:408px;height:auto;display: none">
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
	                    	<textarea rows="8" cols="50" id="replycont"  style="width:408px;padding:3px;height:136px;color:#666;"><emp:message key="wxgl_gzhgl_title_377" defVal="请在这里输入文本信息：(最多可以输入500个字符!)" fileName="wxgl"/></textarea>
	                    	</div>
	                    	<div style="height:20px;line-height:20px;color:#656565;"><span id="sid"></span>/500</div>
	                    	</td>
                    	</tr>
                         <tr align="right">
                            <td>&nbsp;</td>
                         	<td  id="btn" align="right" >
                         		<input type="button" onclick="save(this)"  value="<emp:message key='wxgl_button_6' defVal='保存' fileName='wxgl'/>" class="btnClass5 mr23"/>
                         		<input type="button" onclick="javascript:back()"  value="<emp:message key='wxgl_button_8' defVal='返回' fileName='wxgl'/>" class="btnClass6" style="margin-right:120px"/>
                         	</td>
                         </tr>
                  </thead>
                </table>
                </div>
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
	<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.InputLetter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/addText.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/blankfilter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>
</html>
