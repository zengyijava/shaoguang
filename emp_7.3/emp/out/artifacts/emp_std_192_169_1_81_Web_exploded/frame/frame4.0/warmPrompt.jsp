<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.notice.LfNotice"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
LfNotice lfNotice=(LfNotice)request.getAttribute("lfNotice");
String[] resultStr = (String[])request.getAttribute("resultStr");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
//是否显示公告
boolean isShowGg = btnMap.get(ViewParams.GGCODE+"-0") == null ? false : true;
String [] menu_num=StaticValue.getMenu_num().toString().split(",");
LinkedHashMap<String,String> menMap=new LinkedHashMap<String,String>();
if(menu_num!=null )
{
	for(int i=0;i<menu_num.length;i++)
	{
		menMap.put(menu_num[i],"");
	}
}
//没有彩信模块不显示彩信信息
boolean isHaveMms=true;
if(!menMap.containsKey("4"))
{
	isHaveMms=false;
}
//没有网讯模块不显示网讯信息
boolean isHaveWx=true;
if(!menMap.containsKey("14"))
{
	isHaveWx=false;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
	  <%@ include file="/common/common.jsp"%>
    <title><emp:message key="common_index" defVal="首页" fileName="common"></emp:message></title>
    
	<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/index.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
  	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
  	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<style type="text/css">
		#lyout > div:nth-child(2){
			margin-top: 25px;
		}
	</style>
  	<%}%>
    
    <style type="text/css">
     
        #more a{
        	margin-right: 45px;
        }
   
        #more a:hover
        {
        	text-decoration: underline;
        }
         #lyout{
	    	width:596px;
	    	margin-left: 5px;
	    	padding:15px;
	    	margin-bottom:15px;
	    	display: block;
		    <%="zh_HK".equals(empLangName)?"height:80px":""%>
	    }
	    #lyout div
	    {
	    	height:24px;
	    	line-height:24px;
	    }
	     #lyout div a{
	     	font-size : 18px;
	     	font-weight: 800;
	     	cursor: pointer;
	     	margin: 0 2px;
	     }
	     #lyout div span{
	     	font-size : 14px;
	     	/*font-weight: 800;
	     	cursor: pointer;*/
	     	margin: 0 2px;
	     }
	    .tabyys {
	    	display:block;
	    	width:<%="zh_HK".equals(empLangName)?140:94%>px;
	    	height:20px;
	    	line-height:20px;
	    	margin-left:20px;
	    	margin-bottom:-10px;
	    	position: relative;
	    	background-color: #FFF;
	    	font-weight: 800;
	    	text-align: center;
	    }
    </style>
  </head>
  
  <body>
	<div id="container" class="container" >
		<%-- 当前位置 --%>
		<div class="top">
			<div id="top_right">
				<div id="top_left"></div>
				<div id="top_main" style="">
					<%if(isShowGg && StaticValue.getInniMenuMap().get("/not_notice.htm")!=null){ %>
					<div id="gonggao" style="float: left;width: 500px;padding-left: 13px;">
						<%if(lfNotice!=null && lfNotice.getTitle() != null){ %>
							<a href="javascript:showDetail(<%=lfNotice.getNoticeID() %>);"><emp:message key="common_frame2_warmPrompt_1" defVal="最新公告：" fileName="common"></emp:message><%=lfNotice.getTitle().replace("<","&lt;").replace(">","&gt;") %></a>
						<%}else{out.print(MessageUtils.extractMessage("common","common_frame2_warmPrompt_2",request));} %>
					</div>
					<div id="more" class="" style="cursor: pointer;" align="right" ><a onclick="toNotic()"><emp:message key="common_more" defVal="更多" fileName="common"></emp:message>&nbsp;>></a></div>
					<%} %>
				</div>
			</div>
		</div>
		
		<%-- 内容开始 --%>
		<%int index = 0; %>
		<div id="rContent" class="rContent">
			<div id="loginparam" ></div>
			
		<%if(!"0".equals(resultStr[0]) ||  !"0".equals(resultStr[3])){ %>
			<div class="tabyys" style="width:94px;"><emp:message key="common_frame2_warmPrompt_4" defVal="待处理事项" fileName="common"></emp:message></div>
		    <div id="lyout" class="div_bd" >
		    <%if(!"0".equals(resultStr[0]))
		    {%>
		  	  <div ><emp:message key="common_frame2_warmPrompt_5" defVal="您当前有" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.MSRECODE %>')"><%=resultStr[index++] %></a><emp:message key="common_frame2_warmPrompt_6" defVal="个信息发送任务未审批，其中短信发送" fileName="common"></emp:message><span><%=resultStr[index++]
		  	  %></span><emp:message key="common_single" defVal="" fileName="common"></emp:message><%if(isHaveMms){ %><emp:message key="common_frame2_warmPrompt_7" defVal="，彩信发送" fileName="common"></emp:message><span><%=resultStr[index++] %></span><emp:message key="common_single" defVal="" fileName="common"></emp:message><%} %><emp:message key="common_semicolon" defVal="；" fileName="common"></emp:message></div>
		  	 <%}else{
		  		 index = 3;
		  	 }
		    if(!"0".equals(resultStr[3])){ %>
		  	  <div ><emp:message key="common_frame2_warmPrompt_5" defVal="您当前有" fileName="common"></emp:message><span><%=resultStr[index++] %></span><emp:message key="common_frame2_warmPrompt_8" defVal="个模板未审批，其中短信模板" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.TEMPRECODE %>')"><%=resultStr[index++]
		  	  %></a><emp:message key="common_single" defVal="" fileName="common"></emp:message><%if(isHaveMms){ %><emp:message key="common_frame2_warmPrompt_9" defVal="，彩信模板" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.TEMPRECODE %>')"><%=resultStr[index++] %></a><emp:message key="common_single" defVal="" fileName="common"></emp:message><%}if(isHaveWx){ %><emp:message key="common_frame2_warmPrompt_10" defVal="，网讯模板" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.WXRECODE %>')"><%=resultStr[index++] %></a><emp:message key="common_single" defVal="" fileName="common"></emp:message><%} %><emp:message key="common_semicolon" defVal="；" fileName="common"></emp:message></div>
		  	 <%} else{index=7;}%>
		    </div>
		  <%}else
		  {
			  index = 7;
		  }%>
		  <%if(!"0".equals(resultStr[7]) ||  !"0".equals(resultStr[11])){ %>
			<div class="tabyys" style="width:94px;"><emp:message key="common_frame2_warmPrompt_11" defVal="任务审批进度" fileName="common"></emp:message></div>
		    <div id="lyout" class="div_bd" >
		      <%if(!"0".equals(resultStr[7]))
		    {%>
		  	  <div ><emp:message key="common_frame2_warmPrompt_12" defVal="您当前还有" fileName="common"></emp:message><span><%=resultStr[index++]
		  	  %></span><emp:message key="common_frame2_warmPrompt_33" defVal="个信息发送任务待审批，其中短信发送" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.SMSTASKCODE %>')"><%=resultStr[index++]
		  	  %></a><emp:message key="common_single" defVal="" fileName="common"></emp:message><%if(isHaveMms){ %><emp:message key="common_frame2_warmPrompt_7" defVal="，彩信发送" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.MMSTASKCODE %>')"><%=resultStr[index++] %></a><emp:message key="common_single" defVal="" fileName="common"></emp:message><%} if(isHaveWx){ %><emp:message key="common_frame2_warmPrompt_13" defVal="，网讯发送" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.WXTASKCODE %>')"><%=resultStr[index++] %></a><emp:message key="common_single" defVal="" fileName="common"></emp:message><%} %><emp:message key="common_semicolon" defVal="；" fileName="common"></emp:message></div>
		  	 <%} else{index=11;} if(!"0".equals(resultStr[11])){%>
		  	  <div ><emp:message key="common_frame2_warmPrompt_5" defVal="您当前有" fileName="common"></emp:message><span><%=resultStr[index++] %></span><emp:message key="common_frame2_warmPrompt_34" defVal="个模板待审批，其中短信模板" fileName="common"></emp:message><a  href="javascript:openNewTab('<%=ViewParams.SMSTEMPCODE %>')"><%=resultStr[index++]
		  	  %></a><emp:message key="common_single" defVal="" fileName="common"></emp:message><%if(isHaveMms){ %><emp:message key="common_frame2_warmPrompt_9" defVal="，彩信模板" fileName="common"></emp:message><a  href="javascript:openNewTab('<%=ViewParams.MMSTEMPCODE %>')"><%=resultStr[index++] %></a><emp:message key="common_single" defVal="" fileName="common"></emp:message><%} if(isHaveWx){ %><emp:message key="common_frame2_warmPrompt_10" defVal="，网讯模板" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.WXTEMPCODE %>')"><%=resultStr[index++] %></a><emp:message key="common_single" defVal="" fileName="common"></emp:message><%} %><emp:message key="common_semicolon" defVal="；" fileName="common"></emp:message></div>
		     <%}%>
		    </div>
	     <%} %>
	     <%int smsTimer[] = (int[])request.getAttribute("smsTimer");
	     index = 0;%>
			<div class="tabyys"><emp:message key="common_frame2_warmPrompt_19" defVal="短信定时发送" fileName="common"></emp:message></div>
		    <div id="lyout" class="div_bd" >
		  	  <div ><emp:message key="common_frame2_warmPrompt_14" defVal="您当天有" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.SMSTASKCODE %>')"><%=smsTimer[index++] %></a><emp:message key="common_frame2_warmPrompt_20" defVal="个短信定时发送任务，其中" fileName="common"></emp:message><span><%=smsTimer[index++] %></span><emp:message key="common_frame2_warmPrompt_15" defVal="个已执行成功，" fileName="common"></emp:message><span><%=smsTimer[index++]
		  	  %></span><emp:message key="common_frame2_warmPrompt_16" defVal="个已执行失败，" fileName="common"></emp:message><span><%=smsTimer[index++] %></span><emp:message key="common_frame2_warmPrompt_18" defVal="个未执行。" fileName="common"></emp:message></div>
		  	  <div><emp:message key="common_frame2_warmPrompt_17" defVal="您当前共有" fileName="common"></emp:message><span><%=smsTimer[index++] %></span><emp:message key="common_frame2_warmPrompt_21" defVal="个短信定时发送任务未执行。" fileName="common"></emp:message></div>
		    </div>
	       <%int mmsTimer[] = (int[])request.getAttribute("mmsTimer");
	     index = 0;
	     if(isHaveMms)
	     {
	     %>
			<div class="tabyys"><emp:message key="common_frame2_warmPrompt_22" defVal="彩信定时发送" fileName="common"></emp:message></div>
		    <div id="lyout" class="div_bd" >
		  	  <div ><emp:message key="common_frame2_warmPrompt_14" defVal="您当天有" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.MMSTASKCODE %>')"><%=mmsTimer[index++] %></a><emp:message key="common_frame2_warmPrompt_23" defVal="个彩信定时发送任务，其中" fileName="common"></emp:message><span><%=mmsTimer[index++] %></span><emp:message key="common_frame2_warmPrompt_15" defVal="个已执行成功，" fileName="common"></emp:message><span><%=mmsTimer[index++]
		  	  %></span><emp:message key="common_frame2_warmPrompt_16" defVal="个已执行失败，" fileName="common"></emp:message><span><%=mmsTimer[index++] %></span><emp:message key="common_frame2_warmPrompt_18" defVal="个未执行。" fileName="common"></emp:message></div>
		  	  <div><emp:message key="common_frame2_warmPrompt_17" defVal="您当前共有" fileName="common"></emp:message><span><%=mmsTimer[index++] %></span><emp:message key="common_frame2_warmPrompt_24" defVal="个彩信定时发送任务未执行。" fileName="common"></emp:message></div>
		    </div>
		 <%} %>
	       <%int wxTimer[] = (int[])request.getAttribute("wxTimer");
	       index = 0;
	       if(isHaveWx)
	       {
	       %>
			<div class="tabyys"><emp:message key="common_frame2_warmPrompt_25" defVal="网讯定时发送" fileName="common"></emp:message></div>
		    <div id="lyout" class="div_bd" >
		  	  <div ><emp:message key="common_frame2_warmPrompt_14" defVal="您当天有" fileName="common"></emp:message><a href="javascript:openNewTab('<%=ViewParams.WXTASKCODE %>')"><%=wxTimer[index++] %></a><emp:message key="common_frame2_warmPrompt_26" defVal="个网讯定时发送任务，其中" fileName="common"></emp:message><span><%=wxTimer[index++] %></span><emp:message key="common_frame2_warmPrompt_15" defVal="个已执行成功，" fileName="common"></emp:message><span><%=wxTimer[index++]
		  	  %></span><emp:message key="common_frame2_warmPrompt_16" defVal="个已执行失败，" fileName="common"></emp:message><span><%=wxTimer[index++] %></span><emp:message key="common_frame2_warmPrompt_18" defVal="个未执行。" fileName="common"></emp:message></div>
		  	  <div><emp:message key="common_frame2_warmPrompt_17" defVal="您当前共有" fileName="common"></emp:message><span><%=wxTimer[index++] %></span><emp:message key="common_frame2_warmPrompt_27" defVal="个网讯定时发送任务未执行。" fileName="common"></emp:message></div>
		    </div>
		   <%} %>
	     
		</div>
	</div>
	<div id="Notices" title="<emp:message key="common_frame2_warmPrompt_28" defVal="公告内容" fileName="common"></emp:message>" style="padding:5px;display:none;font-size: 12px;">
      <table>
        <tr><td><emp:message key="common_frame2_warmPrompt_29" defVal="发  布  人：" fileName="common"></emp:message></td><td><input type="text" id="user" value="" style="width:300px" readonly="readonly" onfocus="this.blur()"  class="input_bd"/></td></tr>
		<tr><td height="5px;"></td></tr>
		<tr><td><emp:message key="common_frame2_warmPrompt_30" defVal="发布时间：" fileName="common"></emp:message></td><td><input type="text" id="ttime" value="" style="width:300px" readonly="readonly" onfocus="this.blur()"  class="input_bd"/></td></tr>
		<tr><td height="5px;"></td></tr>
		<tr><td><emp:message key="common_frame2_warmPrompt_31" defVal="标　　题：" fileName="common"></emp:message></td><td><input type="text" id="ttitle" value="" style="width:300px" readonly="readonly" onfocus="this.blur()"  class="input_bd"/></td></tr>
		<tr><td height="5px;"></td></tr>
		<tr><td valign="top"><emp:message key="common_frame2_warmPrompt_32" defVal="内　　容：" fileName="common"></emp:message></td><td><textarea id="tcont" readonly="readonly" style="width:300px;height:200px;overflow-y:auto;word-break: break-all;" class="input_bd"></textarea></td></tr>
		<tr><td height="5px;"></td></tr>
		</table>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
    <script type="text/javascript">
    var path="<%=path%>";
    var iPath="<%=commonPath%>"+"/xtgl/notice";
    </script>
    <script type="text/javascript" src="<%=iPath %>/js/warmPrompt.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
