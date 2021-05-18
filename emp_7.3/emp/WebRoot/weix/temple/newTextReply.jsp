<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.weix.LfWcAccount" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("replymanger");
	
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	
	String pageSize = request.getParameter("pageSize");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>新建文本</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
			#moreSelect{
				width: 610px;
				padding-top:20px;
			}
		</style>
	</head>
	<body >
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode, "新增文本内容") %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
			<input type="hidden" id="path" value="<%=path %>"/>
			<input type="hidden" id="pageSize" value="<%=pageSize %>"/>
				<div class="titletop">
					<table class="titletop_table" style="width:510px;">
						<tr>
							<td class="titletop_td">
								<emp:message key="wexi_qywx_hfgl_text_25" defVal="新增文本内容"
											fileName="weix"></emp:message>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">
								&larr;&nbsp;<emp:message key="wexi_qywx_hfgl_text_20" defVal="返回上一级"
											fileName="weix"></emp:message></font>
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
                	<table  width="100%" height="100%" style="font-size: 12px;line-height: 40px !important; line-height: 30px;">         	
       			 	<thead>
               	  		 <tr>
               	  		 <td width="80"><span><emp:message key="wexi_qywx_mrhf_text_1" defVal="公众帐号："
											fileName="weix"></emp:message></span></td>
               	  		  <td><label>
	                     		<select name="userid" id="accuntnum" class="input_bd" style="width:265px;" >
	                     				<option value=""><emp:message key="wexi_qywx_mrhf_text_12" defVal="所有公众帐号"
											fileName="weix"></emp:message></option>
	                     				<%List<LfWcAccount> list = (List<LfWcAccount>)request.getAttribute("accoutlist");
	                     					if(list!=null && list.size()>0){
	                     						for(LfWcAccount li:list){%>
	                     							<option value="<%=li.getAId()%>"><%=li.getName()%></option>
	                     					<% 	}%>
	                     					<% }%>
	                     		</select></label>
	                     	</td>  
                     	</tr>
                         <tr >
		                    	<td>
									<span><emp:message key="wexi_qywx_hfgl_text_26" defVal="回复名称："
											fileName="weix"></emp:message></span>
								</td>
								<td>
									<input type="text"  name="replyname" id="replyname" maxlength="32" class="input_bd reply_name" style="width:260px;" />
									<font color="red">*</font>
								</td>
	                    	</tr>                                      
						                                    
						<tr>
							<td>
								<span><emp:message key="wexi_qywx_hfgl_text_8" defVal="关键字："
											fileName="weix"></emp:message></span>
							</td>
							<td>
								<input type="text"  name="signstr" id="keyword"    class="input_bd" style="width:260px;" />
								<font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" class="btnClass2" onclick="addKeyword()"  value="<emp:message key="common_btn_13" defVal="添加"
											fileName="common"></emp:message>" />
							</td>
						</tr>
						<tr>
							<td>
								<span></span>
							</td>
							<td style="line-height:15px!important;padding-bottom:10px;">
								<span><font color="#808080"><emp:message key="wexi_qywx_hfgl_text_21" defVal="多个关键字之间可以用空格隔开， 如： 衣服  衬衫   帽子"
											fileName="weix"></emp:message></font>	
								</span>
							</td>
						</tr>
                    	<tr>
                    		<td></td>
                    		<td>
                    			<div id="getObject" class="div_bd" style="width:408px;height:auto;display: none">
                    			<table id="infomaTable" width="100%" style="border-left: none;">
										<tr  class="title_bg">
											<td  class="div_bd"  style="border-left: none;height: 25px;" align="center"><emp:message key="wexi_qywx_hfgl_text_13" defVal="关键字"
											fileName="weix"></emp:message></td>
											<td   class="div_bd" align="center" style="height: 25px;"><emp:message key="wexi_qywx_hfgl_text_22" defVal="完全匹配"
											fileName="weix"></emp:message></td>
											<td   class="div_bd" style="height: 25px;" align="center"><emp:message key="wexi_qywx_hfgl_text_23" defVal="包含匹配"
											fileName="weix"></emp:message></td>
											<td   class="div_bd" style="border-right:0;height: 25px;" align="center"><emp:message key="common_text_14" defVal="操作"
											fileName="common"></emp:message></td>
										</tr>
								</table>
								<table  width="100%" style="border-left: none;">
										<tr align="center">
										<td colspan="4">
											<span><FONT COLOR="#808080"><emp:message key="wexi_qywx_hfgl_text_24" defVal="完全匹配，和关键字一样才会触发! 包含匹配，只要包含本关键字就触发!"
											fileName="weix"></emp:message></FONT>
													
											</span>
										</td>
										</tr>
								</table>
								</div>
                    		</td>
                    	</tr>
                    	
						
                    	<tr >
	                    	<td><span><emp:message key="wexi_qywx_hfgl_text_11" defVal="回复内容："
											fileName="weix"></emp:message></span></td>
                            <td>
	                    	<div style="width:530px;height:144px;">
	                    	<textarea rows="8" cols="50" id="replycont"  style="width:408px;padding:3px;height:136px;color:#666;"><emp:message key="wexi_qywx_mrhf_text_20" defVal="请在这里输入文本信息：(最多可以输入500个字符!)"
											fileName="weix"></emp:message></textarea>

	                    	
	                    	</div>
	                    	<div style="height:20px;line-height:20px;color:#656565;"><span id="sid"></span>/500</div>
	                    	</td>
                    	</tr>
                         <tr align="right">
                            <td>&nbsp;</td>
                         	<td  id="btn" align="right" >
                         		<input type="button" onclick="save(this)"  value="<emp:message key="common_btn_8" defVal="保存"
											fileName="common"></emp:message>" class="btnClass5 mr23"/>
                         		<input type="button" onclick="javascript:back()"  value="<emp:message key="common_btn_10" defVal="返回"
											fileName="common"></emp:message>" class="btnClass6" style="margin-right:120px"/>
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
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/weix_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
   	<script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/weix/temple/js/blankfilter.js"></script>
	<script type="text/javascript" src="<%=iPath %>/js/jquery.InputLetter.js"></script>    
	<script type="text/javascript" src="<%=iPath %>/js/addText.js"></script>    

</body>
</html>
