<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.wxgl.LfWeiAccount" %>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
    String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get(request.getAttribute("rTitle"));
	
	String result = request.getAttribute("w_routeResult")==null?"-1"
			:(String)request.getAttribute("w_routeResult");
	request.removeAttribute("w_routeResult");
 	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
 	String pageSize = request.getParameter("pageSize");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
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
		<link rel="stylesheet" href="<%=iPath%>/css/weixin.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=commonPath%>/wxcommon/widget/umeditor1.2.2/themes/default/css/umeditor.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" rel="stylesheet">
		
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
			<link rel="stylesheet" href="<%=iPath%>/css/temple_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body >
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
	<div class="w_wrapper">
	<div id="w_main">
	<input type="hidden" id="commonPath" value="<%=commonPath%>"/>
	<input type="hidden" id="path" value="<%=path%>"/>
	<input type="hidden" id="isMuitil" value="true"/>
	<input type="hidden" id="filePath" value="<%=filePath%>"/>
	<form  method="post" action="<%=path%>/weix_keywordReply.htm?method=saveOrUpdateImgReply&pageSize=<%=pageSize%>" >
		<div id="getloginUser" style="display:none;"></div>
		<div id="container" class="container">
			
			<%-- 内容开始 --%>
		<div class="w_wapper" id="w_wapper">
			<%-- 编辑器上面部分 -开始--%>
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
									      List<LfWeiAccount> list = (List<LfWeiAccount> )request.getAttribute("accoutlist");
									  		    					if(list!=null && list.size()>0){
									  		    						for(LfWeiAccount li:list){
									  %>
						    							<option value="<%=li.getAId()%>"><%=li.getName()%></option>
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
								    <input type="text" name="item-name" id="item-name" maxlength="32" class="w_input inputSpec reply_name"  value=""/>
									<%--i>*</i--%>
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
					<span class="cf zhu">  <emp:message key="wxgl_gzhgl_title_356" defVal="多个关键字之间可以用空格隔开" fileName="wxgl"/>&nbsp;&nbsp;<emp:message key="wxgl_gzhgl_title_357" defVal="如：衣服 衬衫 帽子" fileName="wxgl"/></span>
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
					<tr class="noKewords">
					  <td colspan="4" class="div_bd"><emp:message key="wxgl_gzhgl_title_360" defVal="暂无关键字" fileName="wxgl"/></td>
					</tr>
			    </table>
			  </div>
		</div>
		<%-- 编辑器上面部分-结束 --%>
		
		<div class="content clearfix">
		  <div class="msg-edit over clearfix">
		  	 <%-- 左边区块开始 --%>
		     <div class="left msg-preview" id="msg-preview">
			    <div class="msg-item-wrapper div_bd">
				<div class="msg-meta title_bg">
					  <span class="msg-date" id="spdate"><emp:message key="wxgl_gzhgl_title_361" defVal="预览区" fileName="wxgl"/></span>
					</div>
				  <div id="appmsgItem1" class="appmsgItem" data-rid="1" onmouseover="appmsgItemOver(this)" onmouseout="appmsgItemOut(this)">
				    
					<div class="cover">
					 <p class="default-tip"><emp:message key="wxgl_gzhgl_title_362" defVal="封面图片" fileName="wxgl"/></p>
						 <img style="display:none;" class="i-img default-tip" src="" width="320px">  
					  <h4 class="msg-t"><span class="i-title"><emp:message key="wxgl_gzhgl_title_363" defVal="标题" fileName="wxgl"/></span></h4>
					  <div class="sub-msg-opa tc " style="line-height:160px;height:160px;">
						 <a href="javascript:;" class="tc first" onclick="iconeditClick(this)"><emp:message key="wxgl_button_12" defVal="编辑" fileName="wxgl"/></a>
					  </div>
					</div>
				  </div>
				  <div id="appmsgItem2" class="appmsgItem sub-msg-item rel" data-rid="2"  onmouseover="appmsgItemOver(this)" onmouseout="appmsgItemOut(this)">
				    <span class="thumb">
					  <span class="default-tip"><emp:message key="wxgl_gzhgl_title_364" defVal="缩略图" fileName="wxgl"/></span>
					  <img src="" class="i-img default-tip" style="display:none;"/>
					</span>
				    <h4 class="msg-t"><span class="i-title"><emp:message key="wxgl_gzhgl_title_363" defVal="标题" fileName="wxgl"/></span></h4>
					<div class="sub-msg-opa tc ">
						 <a href="javascript:;" class="tc first" onclick="iconeditClick(this)"><emp:message key="wxgl_button_12" defVal="编辑" fileName="wxgl"/></a>
						 <a href="javascript:;" class="tc " onclick="appmsgItemRemove(this)"><emp:message key="wxgl_button_10" defVal="删除" fileName="wxgl"/></a>
					  </div>
				  </div>
				  <div class="sub-add">
				    <a href="javascript:;" class="sub-btn-add block tc" >
					  <emp:message key="wxgl_gzhgl_title_365" defVal="增加一条" fileName="wxgl"/>
					</a>
				  </div>
				
				</div>
			 </div>
			 <%-- 左边区块结束 --%>
			 <%-- 右边编辑区块开始 --%>

		<div class="msg-edit-area div_bd" id="msg-edit-area1">
		   <div class="rel msg-editer-wrapper">
		    <div class="msg-meta title_bg">
				  <span class="msg-date"><emp:message key="wxgl_gzhgl_title_366" defVal="编辑区" fileName="wxgl"/></span>
				</div>
		     <div class="msg-editer">
			 
			   <div class="info_list cf">
	            <div class="tit"><emp:message key="wxgl_gzhgl_title_125" defVal="标题：" fileName="wxgl"/></div>
	            <div class="inp">
	              <input type="text" maxlength="32" onkeyup="showTitle(this);" id="item-title1" name="item-title1" class="w_input inputSpec reply_name" value="" >
	            </div>
	           </div>
			   <div class="info_list cf">
	            <div class="tit"><emp:message key="wxgl_gzhgl_title_367" defVal="封面：" fileName="wxgl"/></div>
	            <div class="inp">
		              <input type="file" id="item-img1" onchange="setIputname(this)" name="item-img1" style="height:26px;" class="files" size="1" hidefocus="">
				  <p class="file_tips">
				  <input type="button" class="btnClass2"  onclick="uploadImage(this)" value="<emp:message key='wxgl_button_15' defVal='上传' fileName='wxgl'/>">
				  <input type="hidden" id="item-url1" name="item-url1" />
				  <span><emp:message key="wxgl_gzhgl_title_368" defVal="图片上传格式为jpg、jpeg、gif、bmp、png" fileName="wxgl"/></span></p>
	            </div>
	           </div>
			   <div class="info_list cf">
	            <div class="tit"><emp:message key="wxgl_gzhgl_title_369" defVal="正文：" fileName="wxgl"/></div>
	            <div class="inp">
	            	<script type="text/plain" id="myEditor1" style="width:350px;height:150px;">
					</script>
	            </div>
				  <textarea id="item-content1" style="display:none" name="item-content1"  class="w_input inputSpec textarea"></textarea>
	           </div>
			   <div class="info_list cf">
	            <div class="tit"><emp:message key="wxgl_gzhgl_title_370" defVal="链接：" fileName="wxgl"/></div>
	            <div class="inp">
	              <input id="item-link1" name="item-link1" type="text"  maxlength="50" class="w_input inputSpec" value="">
	              <input style="margin-top:5px;" name="item-source1" type="checkbox" name="item-source1" value="1"/>
		              <span class="zhu"><emp:message key="wxgl_gzhgl_title_371" defVal="选中此项后用户点击图文时优先打开链接地址，链接非必填。" fileName="wxgl"/></span>
	            </div>
	           </div>
			 
			 </div>  
		     <span class="abs msg-arrow a-out div_bd" style="margin-top: 0px;"></span>
			 <span class="abs msg-arrow a-in div_bd" style="margin-top: 0px;"></span>
		   </div>
		 </div>
			 <%-- 右边编辑区块结束 --%>
		  </div>
		  <p class="msg-btn">
		 	 <input id="item-ids" name="itemIds" value="" type="hidden"/>
		    <input id="subSend" class="btnClass5 mr23" type="button" value="<emp:message key='wxgl_button_17' defVal='提交' fileName='wxgl'/>" >
		    <%-- 参数传递 --%>
		    <input id="qingkong" onclick="javascript:back()" class="btnClass6" type="button" value="<emp:message key='wxgl_button_8' defVal='返回' fileName='wxgl'/>">
		  </p>
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
	<%-- 多图文弹出框 --%>
		<div id="msg-edit-area-hidden" title="<emp:message key='wxgl_gzhgl_title_372' defVal='添加图文' fileName='wxgl'/>"  style="display: none">
		   		<div class="msg-edit-area div_bd" id="msg-edit-area">
		   <div class="rel msg-editer-wrapper">
		    <div class="msg-meta title_bg">
				  <span class="msg-date"><emp:message key="wxgl_gzhgl_title_366" defVal="编辑区" fileName="wxgl"/></span>
				</div>
		     <div class="msg-editer">
			 
			   <div class="info_list cf">
	            <div class="tit"><emp:message key="wxgl_gzhgl_title_125" defVal="标题：" fileName="wxgl"/></div>
	            <div class="inp">
	              <input type="text" onkeyup="showTitle(this);"  maxlength="32" id="item-title" name="item-title" class="w_input inputSpec reply_name" value="" >
	            </div>
	           </div>
			   <div class="info_list cf">
	            <div class="tit"><emp:message key="wxgl_gzhgl_title_367" defVal="封面：" fileName="wxgl"/></div>
	            <div class="inp">
	              <input type="file" id="item-img" onchange="setIputname(this)" name="item-img" style="height:26px;" class="files" size="1" hidefocus="">
				  <p class="file_tips">
				  <input type="button" class="btnClass2"  value="<emp:message key='wxgl_button_15' defVal='上传' fileName='wxgl'/>">
				  <input type="hidden" id="item-url" name="item-url" />
				  <span><emp:message key="wxgl_gzhgl_title_368" defVal="图片上传格式为jpg、jpeg、gif、bmp、png" fileName="wxgl"/></span></p>
	            </div>
	           </div>
			   <div class="info_list cf">
	            <div class="tit"><emp:message key="wxgl_gzhgl_title_369" defVal="正文：" fileName="wxgl"/></div>
	            <div class="inp" id="eidtorDiv">
	            </div>
			  	<textarea id="item-content" name="item-content" style="display:none"  class="w_input inputSpec textarea"></textarea>
	           </div>
			   <div class="info_list cf">
	            <div class="tit"><emp:message key="wxgl_gzhgl_title_370" defVal="链接：" fileName="wxgl"/></div>
	            <div class="inp">
	              <input id="item-link" name="item-link" type="text" maxlength="50"  class="w_input inputSpec" value="">
	              <input style="margin-top:5px;" id="item-source" type="checkbox" name="item-source" value="1"/>
		          <span class="zhu"><emp:message key="wxgl_gzhgl_title_371" defVal="选中此项后用户点击图文时优先打开链接地址，链接非必填。" fileName="wxgl"/></span>
	            </div>
	           </div>
			 
			 </div>  
		     <span class="abs msg-arrow a-out div_bd" style="margin-top: 0px;"></span>
			 <span class="abs msg-arrow a-in div_bd" style="margin-top: 0px;"></span>
		   </div>
		 </div>

		</div>
	</div>
    <div class="clear"></div>

    </form>	
    </div></div>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
    	<script type="text/javascript" src="<%=commonPath %>/wxcommon/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" charset="utf-8" src="<%=commonPath %>/wxcommon/widget/umeditor1.2.2/umeditor.config.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <script type="text/javascript" charset="utf-8" src="<%=commonPath %>/wxcommon/widget/umeditor1.2.2/umeditor.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/umeditor1.2.2/lang/<%=langName %>/<%=langName %>.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/addMutilImg.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/addImgText.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/jquery.form.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/weixeditor.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/blankfilter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/artDialog.js?skin=default"></script>
	   <script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>