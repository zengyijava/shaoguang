<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.wxgl.LfWeiAccount" %>
<%@ page import="com.montnets.emp.entity.wxgl.LfWeiRimg" %>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="com.montnets.emp.ottbase.constant.WXStaticValue"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
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
	String menuCode = titleMap.get(request.getAttribute("rTitle"));
	
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
    List<LfWeiRimg> echorimgs = (List<LfWeiRimg>)request.getAttribute("echorimgs");
    JSONArray array=new JSONArray();
    for(int i=0;i<echorimgs.size();i++){
        JSONObject json=new JSONObject();
    	LfWeiRimg rimg=echorimgs.get(i);
    	json.put("title",rimg.getTitle());
    	json.put("description",rimg.getDescription());
    	json.put("link",rimg.getLink());
    	json.put("summary",rimg.getSummary()==null?"":rimg.getSummary());
    	json.put("picurl",rimg.getPicurl());
    	json.put("source_url",rimg.getSourceUrl());
    	array.add(i,json);
    }
    
	//使用集群，文件服务器的地址
	String filePath = GlobalMethods.getWeixFilePath();
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="wxgl_gzhgl_title_354" defVal="新建图文" fileName="wxgl"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" >
		<link href="<%=commonPath%>/wxcommon/widget/umeditor1.2.2/themes/default/css/umeditor.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" rel="stylesheet">
		<link rel="stylesheet" href="<%=iPath%>/css/weixin.css?V=<%=StaticValue.getJspImpVersion() %>"/>
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
			.edui-body-container ul,.edui-body-container ol{
				padding-left:15px;
			}
		</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
	<div class="w_wrapper">
			<%-- 内容开始 --%>
			<div id="w_main">
				<input type="hidden" id="commonPath" value="<%=commonPath%>"/>
				<input type="hidden" id="path" value="<%=path%>"/>
				<input type="hidden" id="basePath" value="<%=basePath%>"/>
				<input type="hidden" id="filePath" value="<%=filePath%>"/>
				<input type="hidden" id="keywords" value="${keywords }"/>
				<xmp style="display: none;" id="rimgs"><%=array.toString()%></xmp>
				<form  method="post" action="<%=path%>/weix_keywordReply.htm?method=saveOrUpdateImgReply&pageIndex=<%=request.getParameter("pageIndex")%>&pageSize=<%=request.getParameter("pageSize")%>" >
				<div id="getloginUser" style="display:none;"></div>
				<input type="hidden" name="tid" id="accoutid" value="${tid}"/>
			    <div class="w_wapper" id="w_wapper">
	<div id="w_header" class="cf div_bd div_bg">
	  <div class="w_header_left">
	   	<table width="100%">
			  <tbody>
		 			<tr>
		 				<td>
						    <div class="info_list cf">
							  <div class="tit"><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/></div>
							  <div class="inp">
							    <select name="item-acctnum" id="item-acctnum" class="w_input w_select" style="width:195px;">
								  <option value=""><emp:message key="wxgl_gzhgl_title_77" defVal="所有公众帐号" fileName="wxgl"/></option>
								  <%
								      List<LfWeiAccount> list = (List<LfWeiAccount>)request.getAttribute("accoutlist");
								  	                     					if(list!=null && list.size()>0){
								  	                     						for(LfWeiAccount li:list){
								  %>
						                     							<option value="<%=li.getAId()%>" <%=(currid-li.getAId()==0)?"selected":"" %>><%=li.getName()%></option>
						                     					<% 	}%>
						                     					<% }%>
							    </select>
								<%--i>*</i--%>
							  </div>
							</div>
		 				</td>
		 				<td>
		 					<div class="info_list cf">
							  <div class="tit"><emp:message key="wxgl_gzhgl_title_355" defVal="回复名称：" fileName="wxgl"/></div>
							  <div class="inp">
							    <input type="text" name="item-name" id="item-name" maxlength="32" value="${replname }" class="w_input inputSpec reply_name" value=""/>
							  </div>
							</div>
		 				</td>
		 			</tr>
			  </tbody>
		</table>
		<div class="info_list cf">
		  <div class="tit"><emp:message key="wxgl_gzhgl_title_126" defVal="关键字：" fileName="wxgl"/></div>
		  <div class="inp">
		    <input type="text" name="add_keywords" id="keyword" class="w_input inputSpec" value=""/>
			<%--i>*</i--%>
			<input type="button" class="btnClass2" onclick="addKeyword()" value="<emp:message key='wxgl_button_1' defVal='添加' fileName='wxgl'/>">
			<input type="hidden" id="words" name="words" />
			<input type="hidden" id="imgids" name="imgids" />
			<span class="zhu cf">
				  <emp:message key="wxgl_gzhgl_title_356" defVal="多个关键字之间可以用空格隔开" fileName="wxgl"/>&nbsp;&nbsp;<emp:message key="wxgl_gzhgl_title_357" defVal="如：衣服 衬衫 帽子" fileName="wxgl"/>
				</span>
		  </div>
		</div>
	  </div>
	
	  <div class="w_header_right ">
	    <table class="table_defined div_bd" id="infomaTable" >
	    	<tr class="title_bg">
	    		<th class="div_bd"><emp:message key="wxgl_gzhgl_title_72" defVal="关键字" fileName="wxgl"/></th>
				<th class="div_bd"><emp:message key="wxgl_gzhgl_title_358" defVal="完全匹配" fileName="wxgl"/></th>
				<th class="div_bd"><emp:message key="wxgl_gzhgl_title_359" defVal="包含匹配" fileName="wxgl"/></th>
				<th class="div_bd"><emp:message key="wxgl_gzhgl_title_7" defVal="操作" fileName="wxgl"/></th>
	    	</tr>
			<tr class="noKewords" style="display:none">
			  <td colspan="4" class="div_bd"><emp:message key="wxgl_gzhgl_title_360" defVal="暂无关键字" fileName="wxgl"/></td>
			</tr>
	    </table>
	  </div>
	</div>
		<div class="content clearfix">
		  <div class="msg-edit over clearfix">
		     <div class="left msg-preview">
			  
			    <div class="msg-item-wrapper div_bd">
				<div class="msg-meta title_bg">
					  <span class="msg-date" id="spdate"><emp:message key="wxgl_gzhgl_title_361" defVal="预览区" fileName="wxgl"/></span>
					</div>
				  <div id="appmsgItem1" class="appmsgItem" data-rid="1" onmouseover="appmsgItemOver(this)" onmouseout="appmsgItemOut(this)">
				    
					<div class="cover">
					 <p class="default-tip"><emp:message key="wxgl_gzhgl_title_362" defVal="封面图片" fileName="wxgl"/></p>
						 <img style="display:none;" class="i-img default-tip" src="" width="320px">  
					  <h4 class="msg-t"><span class="i-title"><emp:message key="wxgl_gzhgl_title_363" defVal="标题" fileName="wxgl"/></span></h4>
					  <p id="summaryDiv"></p>
					</div>
				  </div>
				</div>
			 </div>
			 <div class=" msg-edit-area" id="msg-edit-area1">
			   <div class="rel msg-editer-wrapper div_bd">
			    <div class="msg-meta title_bg">
					  <span class="msg-date"><emp:message key="wxgl_gzhgl_title_366" defVal="编辑区" fileName="wxgl"/></span>
					</div>
			     <div class="msg-editer">
				 
				   <div class="info_list cf">
		            <div class="tit"><emp:message key="wxgl_gzhgl_title_125" defVal="标题：" fileName="wxgl"/></div>
		            <div class="inp">
		              <input type="text" maxlength="32" onkeyup="showTitle(this);" id="item-title1" name="item-title1" class="w_input inputSpec reply_name" value="">
		            </div>
		           </div>
				   <div class="info_list cf">
		            <div class="tit"><emp:message key="wxgl_gzhgl_title_367" defVal="封面：" fileName="wxgl"/></div>
		            <div class="inp">
		              <input type="file" id="item-img1" onchange="setIputname(this)" name="item-img1" style="height:26px;" class="files" size="1" hidefocus="">
					  <p class="file_tips">
					  <input type="button" class="btnClass2"  value="<emp:message key='wxgl_button_15' defVal='上传' fileName='wxgl'/>" onclick="uploadImage(this)">
					  <input type="hidden" id="item-url1" name="item-url1" />
					  <span><emp:message key="wxgl_gzhgl_title_368" defVal="图片上传格式为jpg、jpeg、gif、bmp、png" fileName="wxgl"/></span></p>
		            </div>
		           </div>
		           <div class="info_list cf">
		            <div class="tit"><emp:message key="wxgl_gzhgl_title_373" defVal="摘要：" fileName="wxgl"/></div>
		            <div class="inp">
		           	 <textarea id="item-summary1"  name="item-summary1"  rows="3"  maxlength="100" class="w_input inputSpec textarea"></textarea>
		           	 <span class="zhu"><emp:message key="wxgl_gzhgl_title_374" defVal="摘要非必填。最多100个字符。" fileName="wxgl"/></span>
		            </div>
		           </div>
				   <div class="info_list cf">
		            <div class="tit"><emp:message key="wxgl_gzhgl_title_369" defVal="正文：" fileName="wxgl"/></div>
		            <div class="inp"  id="inpedit">
	            	<script type="text/plain" id="myEditor1" style="width:350px;height:150px;">
					</script>
		            </div>
					<textarea id="item-content1" style="display:none" name="item-content1"  class="w_input inputSpec textarea"></textarea>
		           </div>
				   <div class="info_list cf">
		            <div class="tit"><emp:message key="wxgl_gzhgl_title_370" defVal="链接：" fileName="wxgl"/></div>
		            <div class="inp">
		              <input type="text" id="item-link1" maxlength="50" value="" name="item-link1" class="w_input inputSpec" value="">
		              <input style="margin-top:5px;" id="item-source1" type="checkbox" name="item-source1" value="1"/>
		              <span class="zhu"><emp:message key="wxgl_gzhgl_title_371" defVal="选中此项后用户点击图文时优先打开链接地址，链接非必填。" fileName="wxgl"/></span>
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
		    <input id="subSend" class="btnClass5 mr23" type="button" value="<emp:message key='wxgl_button_3' defVal='确定' fileName='wxgl'/>" >
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
		    <input id="qingkong" onclick="back();" class="btnClass6" type="button" value="<emp:message key='wxgl_button_8' defVal='返回' fileName='wxgl'/>">
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
   		<script type="text/javascript" src="<%=commonPath %>/wxcommon/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" charset="utf-8" src="<%=commonPath %>/wxcommon/widget/umeditor1.2.2/umeditor.config.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <script type="text/javascript" charset="utf-8" src="<%=commonPath %>/wxcommon/widget/umeditor1.2.2/umeditor.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/umeditor1.2.2/lang/<%=langName %>/<%=langName %>.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/weixeditor.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
   	    <script type="text/javascript" src="<%=iPath %>/js/editImgText.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
   	    <script type="text/javascript" src="<%=iPath %>/js/addSingleImg.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
   	    <script type="text/javascript" src="<%=iPath %>/js/jquery.form.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/blankfilter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/artDialog.js?skin=default"></script>
	   <script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>