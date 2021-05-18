<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.entity.system.LfSpeUICfg"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path=request.getContextPath();
    String langName = (String)session.getAttribute("emp_lang");
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath=iPath.substring(0,iPath.lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	//内页公司名
	String companyName="";
	//内页公司logo
	String companyLogo="";
	//登录页背景图
	String bgImg="";
	//登录页logo
	String loginLogo="";
	//个性化显示内容，默认显示 "关于平台"、"收藏此页"、"版权"、"反馈"、"帮助手册"
	String dispContent="11111000";
	//显示类别  1 默认 0个性化
	Integer displayType=1;
	LfSpeUICfg cfg = request.getAttribute("cfg")==null?null:(LfSpeUICfg) request.getAttribute("cfg");
	if(cfg!=null){
		companyName=cfg.getCompanyName();
		companyLogo=cfg.getCompanyLogo();
		if(companyLogo==null||"".equals(companyLogo.trim())){companyLogo="";}
		bgImg=cfg.getBgImg();
		if(bgImg==null||"".equals(bgImg.trim())){bgImg="";}
		loginLogo=cfg.getLoginLogo();
		if(loginLogo==null||"".equals(loginLogo.trim())){loginLogo="";}
				
		if(cfg.getDispContent()!=null){
			dispContent=cfg.getDispContent();
		};
		displayType=cfg.getDisplayType();
	}
	int corpType = StaticValue.getCORPTYPE();
	//
	//使用集群，文件服务器的地址
	//String filePath = GlobalMethods.getWeixFilePath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
        <title><%=titleMap.get(menuCode) %></title>
        <%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/par_indexConfig.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/imageRotate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
  	<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style>
			.info_list .tit {
				width:115px;
			}
		
			.info_list .inp {
				margin-left:115px;
			}
			.cg_mod1 {
				width:420px;
			}
		</style>
		<%}%>
		
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/par_indexConfig2.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/par_indexConfig2.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
		
  </head>
  
  <body id="par_indexConfig">
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
			<input type="hidden" id="path" value="<%=path %>"/>
			<input type="hidden" id="filePath" value=""/>
			<div id="getloginUser" class="getloginUser"></div>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%if(StaticValue.getCORPTYPE() == 0) {//单企业%>
			<form action="<%=path %>/par_indexCfg.htm?method=updateCfg" method="post" >
			  <div class="loginPage_cg cf">
			    <h2 class="title_bg"><emp:message key="xtgl_cswh_gxhjmsz_dljmsz" defVal="登录界面设置" fileName="xtgl"/></h2>
			    <div class="inner">
			    <div class="cg_mod1">
			    	<div class="info_list cf">
			    		<div class="tit"><emp:message key="xtgl_cswh_gxhjmsz_logotp_mh" defVal="LOGO图片：" fileName="xtgl"/></div>
			    		<div class="inp">
			    			<input type="text" id="txt" name="txt" class="w_input inputSpec txt"  readonly="readonly" >
			    			<input type="button" value="<emp:message key='xtgl_cswh_gxhjmsz_ll' defVal='浏览' fileName='xtgl'/>" size="30" class="btnClass2">
			    			<input type="file" id="logo_pic" name="logo_pic"  class="files logo_pic"  >
			    			<input type="hidden" name="login_logo" value=""/>
			    			<p class="file_tips">
							  <span><emp:message key="xtgl_cswh_gxhjmsz_tpgs_1" defVal="支持JPG、JPEG、PNG等格式，图片大小不超过280K，图片尺寸：240*40px" fileName="xtgl"/></span></p>
			    		</div>
			    	</div>
			    	<div class="info_list cf">
			    		<div class="tit"><emp:message key="xtgl_cswh_gxhjmsz_dbjtp_mh" defVal="大背景图片：" fileName="xtgl"/></div>
			    		<div class="inp">
			    			<input type="text" id="txt" name="txt" class="w_input inputSpec txt"  >
			    			<input type="button" value="<emp:message key='xtgl_cswh_gxhjmsz_ll' defVal='浏览' fileName='xtgl'/>" size="30" class="btnClass2">
			    			<input type="file" id="back_pic" name="back_pic"  class="files back_pic">
			    			<input type="hidden" name="back" value=""/>
			    			<p class="file_tips">
							  <span><emp:message key="xtgl_cswh_gxhjmsz_tpgs_2" defVal="支持JPG、JPEG、PNG等格式，图片大小不超过2M，图片尺寸：1024*580px" fileName="xtgl"/></span></p>
			    		</div>
			    	</div>
			    	<div class="info_list cf" id="cx">
			    		<div class="tit"><emp:message key="xtgl_cswh_gxhjmsz_xsnrsz_mh" defVal="显示内容设置：" fileName="xtgl"/></div>
			    		<div class="inp">
			    			<ul class="cont_cg">
			    			  <li><input type="checkbox" name="gypt" id="aboutEmp"><label for="aboutEmp"><emp:message key="xtgl_cswh_gxhjmsz_gypt" defVal="关于平台" fileName="xtgl"/></label></li>
			    			  <li><input type="checkbox" name="sccy" id="collect"><label for="collect"><emp:message key="xtgl_cswh_gxhjmsz_sccy" defVal="收藏此页" fileName="xtgl"/></label></li>
			    			  <li><input type="checkbox" name="cright" id="copyRight"><label for="copyRight">Copyright© <%= Calendar.getInstance().get(Calendar.YEAR)%><emp:message key="xtgl_cswh_gxhjmsz_mwkj" defVal="梦网科技" fileName="xtgl"/> All rights reserved </label></li>
			    			  <li><input type="checkbox" name="fk" id="feedbackEmp"><label for="feedbackEmp"><emp:message key="xtgl_cswh_gxhjmsz_fk" defVal="反馈" fileName="xtgl"/></label></li>
			    			  <li><input type="checkbox" name="bzsc" id="helpManual"><label for="helpManual"><emp:message key="xtgl_cswh_gxhjmsz_bzsc" defVal="帮助手册" fileName="xtgl"/></label></li>
			    			</ul>
			    		</div>
			    	</div>
			    </div>
			    <div class="cg_mod2">
			    	<div class="btnDiv">
			    		<input type="button" class="btnClass2 default" name="0" value="<emp:message key='xtgl_cswh_gxhjmsz_hfmr' defVal='恢复默认' fileName='xtgl'/>">
			    	    <input type="button" class="btnClass2 submit" name="0" value="<emp:message key='xtgl_spgl_shlcgl_bc' defVal='保存' fileName='xtgl'/>">
			    	</div>
			    	<div class="preview">
			    		<p><emp:message key="xtgl_spgl_xxsp_yl" defVal="预览" fileName="xtgl"/></p>
			    		<div class="showImg">
			    			<div id="login_pre" class="imageRotation">	
								<div class="imageBox">
									<div class="img"><span><emp:message key="xtgl_cswh_gxhjmsz_zwyl" defVal="暂无预览" fileName="xtgl"/></span><img src="" /></div>
									<div class="img"><span><emp:message key="xtgl_cswh_gxhjmsz_zwyl" defVal="暂无预览" fileName="xtgl"/></span><img src="" /></div>
								</div>
							    <div class="titleBox">
							    	<p class="active"><span><emp:message key="xtgl_cswh_gxhjmsz_logotp" defVal="LOGO图片" fileName="xtgl"/></span></p>
							        <p><emp:message key="xtgl_cswh_gxhjmsz_dbjtp" defVal="大背景图片" fileName="xtgl"/></p>
							    </div>
								<div class="icoBox">
									<span class="active" rel="1">1</span>
									<span rel="2">2</span>
								</div>
							</div>
			    		</div>
			    	
			    	</div>
			    </div>
			    </div>
			  </div>
			 </form> 
			<%} %>  
			 <form action="<%=path %>/par_indexCfg.htm?method=updateCfg" method="post">
			  <div class="loginPage_cg cf cg2">
			    <h2 class="title_bg"><emp:message key="xtgl_cswh_gxhjmsz_nyjmsz" defVal="内页界面设置" fileName="xtgl"/></h2>
			    <div class="inner">
			    <div class="cg_mod1 logo_top">
			    	<div class="info_list cf">
			    		<div class="tit"><emp:message key="xtgl_cswh_gxhjmsz_logotp_mh" defVal="LOGO图片：" fileName="xtgl"/></div>
			    		<div class="inp">
			    			<input type="text" id="txt" name="txt" class="w_input inputSpec txt"  >
			    			<input type="button" value="<emp:message key='xtgl_cswh_gxhjmsz_ll' defVal='浏览' fileName='xtgl'/>" size="30" class="btnClass2">
			    			<input type="file" id="logo_img" name="logo_img"  class="files logo_img">
			    			<input type="hidden" name="company_logo" value=""/>
			    			<p class="file_tips">
							  <span><emp:message key="xtgl_cswh_gxhjmsz_qscpngtp" defVal="请上传PNG图片，大小不超过280K，尺寸：130*50px" fileName="xtgl"/></span></p>
			    		</div>
			    	</div>
			    	
			    </div>
			    <div class="cg_mod2 yl_top">
			    	<div class="btnDiv">
			    		<input type="button" class="btnClass2 default" name="1" value="<emp:message key='xtgl_cswh_gxhjmsz_hfmr' defVal='恢复默认' fileName='xtgl'/>">
			    	    <input type="button" class="btnClass2 submit bC2marginleft" name="1" value="<emp:message key='xtgl_spgl_shlcgl_bc' defVal='保存' fileName='xtgl'/>">
			    	</div>
			    	<div class="preview">
			    		<p><emp:message key="xtgl_spgl_xxsp_yl" defVal="预览" fileName="xtgl"/></p>
			    		<div class="showImg">
			    			<div id="main_pre">
			    				<div class="img"><span><emp:message key="xtgl_cswh_gxhjmsz_zwyl" defVal="暂无预览" fileName="xtgl"/></span><img src="" /></div>
			    			</div>
			    		</div>
			    	
			    	</div>
			    </div>
			    </div>
			  </div>
			</form>
			
		  	 </div>
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
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=iPath %>/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/jquery.form.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/imageRotate.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/indexConfig.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		 var corpType="<%=corpType%>";
		$(document).ready(function(){
			var companyLogo='<%=companyLogo%>';
			if(companyLogo!=''){
				$("#main_pre").find("img:eq(0)").attr('src',companyLogo).show().siblings().hide();
			}else{
				$("#main_pre").find("img:eq(0)").hide().siblings().show();
				}
			});
		</script>
		<script type="text/javascript">
		<%if(StaticValue.getCORPTYPE() == 0) {//单企业初始化登录页配置%>
		$(document).ready(function(){
			var loginLogo='<%=loginLogo%>';
			var bgImg='<%=bgImg%>';
			var dispContent='<%=dispContent%>';
			$("input[type='checkbox']").each(function(index){
				if(dispContent.charAt(index)=='1'){
						$(this).attr('checked','checked');
					}
			});
			if(loginLogo!=''){
				$("#login_pre").find("img:eq(0)").attr('src',loginLogo).show().siblings().hide();
			}else{
				$("#login_pre").find("img:eq(0)").hide().siblings().show();
				}
			if(bgImg!=''){
				$("#login_pre").find("img:eq(1)").attr('src',bgImg).show().siblings().hide();
			}else{
				$("#login_pre").find("img:eq(1)").hide().siblings().show();
			}
			});
		<%} else{%>
			$('.loginPage_cg').removeClass('cg2');
			$('.loginPage_cg>.inner').addClass('inner-margin');
		<%}%>
		</script>
  </body>
</html>
