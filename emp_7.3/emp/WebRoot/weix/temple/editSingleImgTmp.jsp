<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.weix.LfWcAccount" %>
<%@ page import="com.montnets.emp.entity.weix.LfWcRimg" %>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.weix.common.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path=request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	Long currid = (Long)request.getAttribute("currid");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("replymanger");
	

	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	String result = request.getAttribute("w_routeResult")==null?"-1"
			:(String)request.getAttribute("w_routeResult");
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
 	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    List<LfWcRimg> echorimgs = (List<LfWcRimg>)request.getAttribute("echorimgs");
    JSONArray array=new JSONArray();
    for(int i=0;i<echorimgs.size();i++){
        JSONObject json=new JSONObject();
    	LfWcRimg rimg=echorimgs.get(i);
    	json.put("title",rimg.getTitle());
    	json.put("description",rimg.getDescription());
    	json.put("link",rimg.getLink());
    	json.put("picurl",rimg.getPicurl());
    	array.add(i,json);
    }
    
	//使用集群，文件服务器的地址
	String filePath = GlobalMethods.getWeixFilePath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>新建图文</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css" rel="stylesheet" type="text/css" >
		<link href="<%=skin %>/newjqueryui.css" rel="stylesheet" type="text/css" >
		<link href="<%=skin %>/reviewsend.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=iPath %>/css/weixin.css"/>
		<style type="text/css">
			#batchFileSend {
			    font-size: 12px;
			    margin-top: 16px;
			    text-align: left;
			    width: 960px;
			}
			
			.wjsendclss {
			    height: 41px;
			    line-height: 41px;
			    width: 305px;
			}
			#msg-edit-topleft{
				float:left;
				margin:25px 20px 20px 30px;
			}
			#msg-edit-topright{
				float:left;
				margin:25px 20px 20px 30px;
			}
		</style>
	</head>
	<body >

	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode, "编辑单图文") %>
		
	<div class="w_wrapper">
	   
			<%-- 内容开始 --%>
			<div id="w_main">
				<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
				<input type="hidden" id="path" value="<%=path %>"/>
				<input type="hidden" id="basePath" value="<%=basePath %>"/>
				<input type="hidden" id="filePath" value="<%=filePath %>"/>
				<input type="hidden" id="keywords" value="${keywords }"/>
				<xmp style="display: none;" id="rimgs"><%=array.toString() %></xmp>
				<form  method="post" action="<%=path %>/cwc_replymanger.htm?method=saveOrUpdateImgReply&pageIndex=<%=request.getParameter("pageIndex") %>&pageSize=<%=request.getParameter("pageSize") %>" >
				<div id="getloginUser" style="display:none;"></div>
				<input type="hidden" name="tid" id="accoutid" value="${tid}"/>
			    <div class="w_wapper" id="w_wapper">
	<div id="w_header" class="cf div_bd div_bg">
	  <div class="w_header_left">
	    <div class="info_list cf">
		  <div class="tit"><emp:message key="wexi_qywx_mrhf_text_1" defVal="公众帐号："
											fileName="weix"></emp:message></div>
		  <div class="inp">
		    <select name="item-acctnum" id="item-acctnum" class="w_input w_select" style="width:195px;">
			  <option value=""><emp:message key="wexi_qywx_mrhf_text_12" defVal="所有公众帐号"
											fileName="weix"></emp:message></option>
			  <%List<LfWcAccount> list = (List<LfWcAccount>)request.getAttribute("accoutlist");
	                     					if(list!=null && list.size()>0){
	                     						for(LfWcAccount li:list){%>
	                     							<option value="<%=li.getAId()%>" <%=(currid-li.getAId()==0)?"selected":"" %>><%=li.getName()%></option>
	                     					<% 	}%>
	                     					<% }%>
		    </select>
			<i>*</i>
		  </div>
		</div>
		<div class="info_list cf">
		  <div class="tit"><emp:message key="wexi_qywx_hfgl_text_26" defVal="回复名称："
											fileName="weix"></emp:message></div>
		  <div class="inp">
		    <input type="text" name="item-name" id="item-name" maxlength="32" value="${replname }" class="w_input inputSpec reply_name" value=""/>
			<i>*</i>
		  </div>
		</div>
		<div class="info_list cf">
		  <div class="tit"><emp:message key="wexi_qywx_hfgl_text_8" defVal="关键字："
											fileName="weix"></emp:message></div>
		  <div class="inp">
		    <input type="text" name="add_keywords" id="keyword" class="w_input inputSpec" value=""/>
			<i>*</i>
			<input type="button" class="btnClass2" onclick="addKeyword()" value="<emp:message key="common_btn_13" defVal="添加"
											fileName="common"></emp:message>">
			<input type="hidden" id="words" name="words" />
			<input type="hidden" id="imgids" name="imgids" />
		  </div>
		</div>
		<div class="info-tips cf">
		 <emp:message key="wexi_qywx_hfgl_text_21" defVal="多个关键字之间可以用空格隔开， 如： 衣服  衬衫   帽子"
											fileName="weix"></emp:message>
		</div>
	  
	  </div>
	
	  <div class="w_header_right ">
	    <table class="table_defined div_bd" id="infomaTable" >
	    	<tr class="title_bg">
	    		<th class="div_bd"><emp:message key="wexi_qywx_hfgl_text_13" defVal="关键字"
											fileName="weix"></emp:message></th>
				<th class="div_bd"><emp:message key="wexi_qywx_hfgl_text_22" defVal="完全匹配"
											fileName="weix"></emp:message></th>
				<th class="div_bd"><emp:message key="wexi_qywx_hfgl_text_23" defVal="包含匹配"
											fileName="weix"></emp:message></th>
				<th class="div_bd"><emp:message key="common_text_14" defVal="操作"
											fileName="common"></emp:message></th>
	    	</tr>
			<tr class="noKewords" style="display:none">
			  <td colspan="4" class="div_bd"><emp:message key="wexi_qywx_hfgl_text_28" defVal="暂无关键字"
											fileName="weix"></emp:message></td>
			</tr>
	    </table>
	   
	  </div>
	
	</div>
		<div class="content clearfix">
		  <div class="msg-edit over clearfix">
		     <div class="left msg-preview">
			  
			    <div class="msg-item-wrapper div_bd">
				<div class="msg-meta title_bg">
					  <span class="msg-date" id="spdate"><emp:message key="wexi_qywx_hfgl_text_29" defVal="预览区"
											fileName="weix"></emp:message></span>
				</div>
				  <div id="appmsgItem1" class="appmsgItem" data-rid="1" onmouseover="appmsgItemOver(this)" onmouseout="appmsgItemOut(this)">
				    
					<div class="cover">
					 <p class="default-tip"><emp:message key="wexi_qywx_hfgl_text_30" defVal="封面图片"
											fileName="weix"></emp:message></p>
						 <img style="display:none;" class="i-img default-tip" src="" width="320px">  
					  <h4 class="msg-t"><span class="i-title">+
					  </span></h4>
					  
					</div>
				  </div>
				  
				
				</div>
			 </div>
			 <div class=" msg-edit-area" id="msg-edit-area1">
			   <div class="rel msg-editer-wrapper div_bd">
			    <div class="msg-meta title_bg">
					  <span class="msg-date"><emp:message key="wexi_qywx_hfgl_text_32" defVal="编辑区"
											fileName="weix"></emp:message></span>
					</div>
			     <div class="msg-editer">
				 
				   <div class="info_list cf">
		            <div class="tit"><emp:message key="wexi_qywx_hfgl_text_33" defVal="标题："
											fileName="weix"></emp:message></div>
		            <div class="inp">
		              <input type="text" maxlength="32" onkeyup="showTitle(this);" id="item-title1" name="item-title1" class="w_input inputSpec reply_name" value=""><i>*</i>
		            </div>
		           </div>
				   <div class="info_list cf">
		            <div class="tit"><emp:message key="wexi_qywx_hfgl_text_34" defVal="封面："
											fileName="weix"></emp:message></div>
		            <div class="inp">
		              <input type="text" id="txt" name="txt" class="w_input inputSpec" style="margin-right:5px;">
					  <input type="button" value="<emp:message key="common_btn_11" defVal="浏览"
											fileName="common"></emp:message>" size="30"  class="btnClass2">
		              <input type="file" id="item-img1" onchange="setIputname(this)" name="item-img1" style="height:26px;" class="files" size="1" hidefocus="">
					  <p class="file_tips">
					  <input type="button" class="btnClass2"  value="<emp:message key="common_btn_12" defVal="上传"
											fileName="common"></emp:message>" onclick="uploadImage(this)">
					  <input type="hidden" id="item-url1" name="item-url1" />
					  <span><emp:message key="wexi_qywx_hfgl_text_35" defVal="图片上传格式为.jpg、.jpeg、.gif、.bmp、.png等等"
											fileName="weix"></emp:message></span></p>
		            </div>
		           </div>
				   <div class="info_list cf">
		            <div class="tit"><emp:message key="wexi_qywx_hfgl_text_36" defVal="正文："
											fileName="weix"></emp:message></div>
		            <div class="inp">
					  <textarea id="item-content1" name="item-content1" maxlength="1000"  cols="30" rows="10"  class="w_input inputSpec textarea"></textarea>
		            </div>
		           </div>
				   <div class="info_list cf">
		            <div class="tit"><emp:message key="wexi_qywx_hfgl_text_37" defVal="原文链接："
											fileName="weix"></emp:message></div>
		            <div class="inp">
		              <input type="text" id="item-link1" maxlength="50" value="" name="item-link1" class="w_input inputSpec" value="">
		            </div>
		           </div>
				 
				 </div>  
			     <span class="abs msg-arrow a-out div_bd" style="margin-top: 0px;"></span>
				 <span class="abs msg-arrow a-in div_bd" style="margin-top: 0px;"></span>
			   </div>
			 </div>
		  </div>
		  <p class="msg-btn">
		  	<input id="item-ids" name="itemIds" value="" type="hidden"/>
		    <input id="subSend" class="btnClass5 mr23" type="button" value="<emp:message key="common_btn_7" defVal="确定"
											fileName="common"></emp:message>">" >
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
		    <input id="qingkong" onclick="back();" class="btnClass6" type="button" value="<emp:message key="common_btn_10" defVal="返回"
											fileName="common"></emp:message>">">
		  </p>
		</div>

	</div>	
			  
			 </form>	
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
		
    <div class="clear"></div>
   </div>
      <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/weix_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
   		<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-c.js" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="<%=iPath %>/js/ajaxfileupload.js"></script>
		<script type="text/javascript" src="<%=iPath %>/js/weixeditor.js"></script>
   	    <script type="text/javascript" src="<%=iPath %>/js/editImgText.js"></script>
   	    <script type="text/javascript" src="<%=iPath %>/js/addSingleImg.js"></script>
   	    <script type="text/javascript" src="<%=iPath %>/js/jquery.form.min.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/weix/temple/js/blankfilter.js"></script>		
	</body>
</html>