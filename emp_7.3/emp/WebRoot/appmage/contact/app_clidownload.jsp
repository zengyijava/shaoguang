<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.app.LfAppClidowload"%>
<%@page import="com.montnets.emp.entity.appmage.LfAppAccount"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
    String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath=iPath.substring(0,iPath.lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	//String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get("clidownload");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	LfAppClidowload acl = request.getAttribute("acl")==null?null:(LfAppClidowload) request.getAttribute("acl");
	LfAppAccount appac = request.getAttribute("appac")==null?null:(LfAppAccount) request.getAttribute("appac");
	//clid
	String clid="";
	//所属公司名称
	String companyName= MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_tiyanban",request) ;  //"深圳市梦网科技企客通体验版";
	//String companyName="未申请";
	//所属公司企业编码
	String companyCode="MWKJ00007";
	//关联id
	String aid="";
	//IOS发布日期
	String iospushdate="2014" + MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_year",request) + "9" + MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_month",request) + "10" + MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_day",request) ;
	//安卓发布日期
	String adpushdate="2014" + MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_year",request) + "9" + MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_month",request) + "9" + MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_day",request);
	//ios文件版本号
	String iosfileversion="V2.0.0.18";
	//安卓文件版本号
	 String adfileversion="V2.0.0.18";
	 //IOS文件版本支持
	 String iosfiledescr= MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_iosversion",request) ;  //"IOS7及以上版本";
	 //安卓文件版本支持
	 String adfiledescr= MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_andversion",request) ;  //"Android2.2及以上版本";
	//ios文件路径
	 String iosfileurl=iPath+"/image/iosewm.png";
	//安卓文件路径
	 String adfileurl=iPath+"/image/azewm.png";
	//IOS文件大小
	 String iosfilesize="12.6";
	//安卓文件大小
 	 String azfilesize="3.43";
	
	if(appac!=null){
		//设置企业编码
		if(appac.getCode()!=null&&!"".equals(appac.getCode())){
			companyCode=appac.getCode();
		}
		//设置企业名称
		if(appac.getName()!=null&&!"".equals(appac.getName())){
			companyName=appac.getName();
		}
		if(appac.getAid()!=null&&appac.getAid()!=0){
			aid=appac.getAid().toString();
		}
	}
	SimpleDateFormat df = null;
	if(StaticValue.ZH_HK.equals((String) request.getSession().getAttribute(StaticValue.LANG_KEY))){
		df =  new SimpleDateFormat( "yyyy/MM/dd/" );
	} else {
		df =  new SimpleDateFormat("yyyy" + MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_year",request) + "MM" + MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_month",request) + "dd" + MessageUtils.extractMessage("appmage","appmage_jcsj_khdxz_text_day",request));
	}
	if(acl!=null){
		if(acl.getClid()!=null&&acl.getClid()!=0){
			clid=acl.getClid().toString();
		}
	
		if(acl.getIospushdate()!=null&&!"".equals(acl.getIospushdate())){
			//IOS发布日期
	 		iospushdate=df.format(acl.getIospushdate());
		}
		
		if(acl.getAdpushdate()!=null&&!"".equals(acl.getAdpushdate())){
	 		//安卓发布日期
	 		adpushdate=df.format(acl.getAdpushdate());
		}
		
		if(acl.getIosfileversion()!=null&&!"".equals(acl.getIosfileversion())){
	 		//ios文件版本号
			iosfileversion=acl.getIosfileversion();
		}
		
		if(acl.getAdfileversion()!=null&&!"".equals(acl.getAdfileversion())){
	 		//安卓文件版本号
			adfileversion=acl.getAdfileversion();
		}
		
		//if(acl.getIosfiledescr()!=null&&!"".equals(acl.getIosfiledescr())){
			 //IOS文件版本支持
	  		//iosfiledescr=acl.getIosfiledescr();
		//}
		
		//if(acl.getAdfiledescr()!=null&&!"".equals(acl.getAdfiledescr())){
			 //安卓文件版本支持
	  //		adfiledescr=acl.getAdfiledescr();
		//}
	 
	 	if(acl.getIosfileurl()!=null&&!"".equals(acl.getIosfileurl())){
			//ios文件路径
	  		iosfileurl=acl.getIosfileurl();
		}
		
		if(acl.getAdfileurl()!=null&&!"".equals(acl.getAdfileurl())){
			//安卓文件路径
	  		adfileurl=acl.getAdfileurl();
		}
		if(acl.getIosfilesize()!=null&&!"".equals(acl.getIosfilesize())){
			//IOS文件大小
	  		iosfilesize=acl.getIosfilesize();
		}
		
		if(acl.getAzfilesize()!=null&&!"".equals(acl.getAzfilesize())){
			//安卓文件大小
  			azfilesize=acl.getAzfilesize();
		}
	}
	//
	//使用集群，文件服务器的地址
	//String filePath = GlobalMethods.getWeixFilePath();
	
		
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
        <title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/app_clidownload.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<style type="text/css">
	
	.td-tips{
		line-height: 25px;
		margin:10px 10px 10px 10px;
		padding: 2px 5px;
		border-radius: 5px;
		text-align: left;
		font-size: 15px;
		text-indent: 2em;
	}
	
	</style>
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
  </head>
  
  <body>
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<div id="condition" style="margin-left: 10px;">
				<table>
				<tr>
				<td>
					<p class="td-tips " >
						<emp:message key="appmage_jcsj_khdxz_text_tips" 
						defVal="欢迎使用梦网科技企业APP，扫一扫马上下载APP体验版客户端，用户登录账号及密码请自行注册。如果您需要开通专属自己企业的APP，请联系客户经理申请企业编码，我们将竭诚为您服务，感谢您的使用！" fileName="appmage"></emp:message>
					</p>
				</td>
				</tr>
				</table>
			</div>
			<form action="<%=path %>/app_clidownload.htm?method=updateEwm&type=0" method="post" >
				<input type="hidden" name="aid" value="<%=aid %>" /> 
				<input type="hidden" name="clid" value="<%=clid %>" /> 
				<input type="hidden" id="lgusername" name="lgusername" value="<%=request.getParameter("lgusername") %>"/>
				<input type="hidden" name="lgcorpcode" id="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>">
			  <div class="loginPage_ios cf div_bd">
			   <div class="title_c div_bd title_bg">
			   	<img  class="dimg" src="<%=iPath %>/image/IOS.png" />
			 	 <span class="spant"><emp:message key="appmage_jcsj_khdxz_text_iosdownload" defVal="企客通IOS端APP下载" fileName="appmage"></emp:message></span>
			   </div>
			  <div id="iosdiv" class="tdiv">
			  <table>
			  <tr>
			  <td style="width: 70px;"><emp:message key="appmage_jcsj_khdxz_text_ssqy" defVal="所属企业" fileName="appmage"></emp:message>：</td>
			  <td><%=companyName %></td>
			  </tr>
			  <tr>
			  <td><emp:message key="appmage_jcsj_yhgl_text_buscode" defVal="企业编码" fileName="appmage"></emp:message>：</td>
			  <td><%=companyCode %></td>
			  </tr>
			  <tr>
			  <td><emp:message key="appmage_jcsj_khdxz_text_publictime" defVal="发布日期" fileName="appmage"></emp:message>：</td>
			  <td><%=iospushdate %></td>
			  </tr>
			  <tr>
			  <td><emp:message key="appmage_jcsj_khdxz_text_filesize" defVal="文件大小" fileName="appmage"></emp:message>：</td>
			  <td><%=iosfilesize %> MB</td>
			  </tr>
			  <tr>
			  <td><emp:message key="appmage_jcsj_khdxz_text_fileversion" defVal="文件版本" fileName="appmage"></emp:message>：</td>
			  <td><%=iosfileversion %></td>
			  </tr>
			   <tr>
			  <td><emp:message key="appmage_jcsj_khdxz_text_suport" defVal="支持" fileName="appmage"></emp:message>：</td>
			  <td><%=iosfiledescr %></td>
			  </tr>
			  </table>
			  <table>
			  <%if(btnMap.get(menuCode+"-1")!=null) { %>
				  <tr class="btntr">
				  	<td>
				    	    <input type="button" class="btnClass5" onclick="toupdate(0);" name="1" value="<emp:message key="appmage_jcsj_yhgl_text_modify" defVal="修改" fileName="appmage"></emp:message>">
				  	</td>
				  </tr>
			  <%} %>
			  </table> 

			   </div>
			   	<div class="preview">
			    		<div class="showImg">
			    			<div id="iosmain_pre">
			    				<div class="img"><img src="<%=iosfileurl!=null?iosfileurl:"" %>" /></div>
			    			</div>
			    		</div>
			    		<div id="iosscdiv" style="position: relative;margin-top: 13px;margin-left: 13px;">
			    			<span style="font-weight: bold;"><emp:message key="appmage_jcsj_khdxz_text_dwonload" defVal="扫一扫，马上下载" fileName="appmage"></emp:message></span>
			    		</div>
			    </div>
			    
			  </div>
			 </form> 
			 <form action="<%=path %>/app_clidownload.htm?method=updateEwm&type=1" method="post">
			 	<input type="hidden" name="aid" value="<%=aid %>" /> 
			 	<input type="hidden" name="clid" value="<%=clid %>" /> 
			 	<input type="hidden" id="lgusername" name="lgusername" value="<%=request.getParameter("lgusername") %>"/>
				<input type="hidden" name="lgcorpcode" id="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"> 
			  <div class="loginPage_ad cf div_bd">
			   <div class="title_c div_bd title_bg">
			   	<img class="dimg" src="<%=iPath %>/image/And.png">
			   	<span class="spant"><emp:message key="appmage_jcsj_khdxz_text_anddownload" defVal="企客通Android端APP下载" fileName="appmage"></emp:message></span>
			   	</div>
			   <div id="addiv" class="tdiv">
			  <table>
			  <tr>
			  <td style="width: 70px;"><emp:message key="appmage_jcsj_khdxz_text_ssqy" defVal="所属企业" fileName="appmage"></emp:message>：</td>
			  <td><%=companyName %></td>
			  </tr>
			  <tr>
			  <td><emp:message key="appmage_jcsj_yhgl_text_buscode" defVal="企业编码" fileName="appmage"></emp:message>：</td>
			  <td><%=companyCode %></td>
			  </tr>
			  <tr>
			  <td><emp:message key="appmage_jcsj_khdxz_text_publictime" defVal="发布日期" fileName="appmage"></emp:message>：</td>
			  <td><%=adpushdate %></td>
			  </tr>
			  <tr>
			  <td><emp:message key="appmage_jcsj_khdxz_text_filesize" defVal="文件大小" fileName="appmage"></emp:message>：</td>
			  <td><%=azfilesize %> MB</td>
			  </tr>
			  <tr>
			  <td><emp:message key="appmage_jcsj_khdxz_text_fileversion" defVal="文件版本" fileName="appmage"></emp:message>：</td>
			  <td><%=adfileversion %></td>
			  </tr>
			   <tr>
			  <td><emp:message key="appmage_jcsj_khdxz_text_suport" defVal="支持" fileName="appmage"></emp:message>：</td>
			  <td><%=adfiledescr %></td>
			  </tr>
			    </table>
			    <table>
			   <%if(btnMap.get(menuCode+"-1")!=null) { %>
			  <tr class="btntr">
			  	<td>
			    	    <input type="button" class="btnClass5" onclick="toupdate(1);" name="1" value="<emp:message key="appmage_jcsj_yhgl_text_modify" defVal="修改" fileName="appmage"></emp:message>">
			  	</td>
			  </tr>
			  <%} %>
			  </table> 

			   </div>
			   	<div class="preview">
			    		<div class="showImg">
			    			<div id="admain_pre">
			    				<div class="img"><img src="<%=adfileurl!=null?adfileurl:"" %>" /></div>
			    			</div>
			    		</div>
			    		<div id="adscdiv" style="position: relative;margin-top: 13px;margin-left: 13px;">
			    			<span style="font-weight: bold;"><emp:message key="appmage_jcsj_khdxz_text_dwonload" defVal="扫一扫，马上下载" fileName="appmage"></emp:message></span>
			    		</div>
			    </div>


			    <%--<div class="cg_mod1">
			    	<div class="info_list cf">
			    		<div class="tit">LOGO图片：</div>
			    		<div class="inp">
			    			<input type="text" id="txt" name="txt" class="w_input inputSpec" style="margin-right:5px;">
			    			<input type="button" value="浏览" size="30" class="btnClass2">
			    			<input type="file" id="logo_img" name="logo_img" style="height:26px;" class="files">
			    			<input type="hidden" name="company_logo" value=""/>
			    			<p class="file_tips">
							  <span>支持JPG、JPEG、PNG等格式，图片大小不超过280K，图片尺寸：290*60px</span></p>
			    		</div>
			    	</div>
			    	
			    </div>
			   
			    --%>
			    </div>
			</form>
			
		  	 </div>
			</div>	
			
			 <div id="iostemplatediv"  style="display: none;">
				  <table>
					  <tr>
					  <td style="width: 70px;"><emp:message key="appmage_jcsj_khdxz_text_ssqy" defVal="所属企业" fileName="appmage"></emp:message>：</td>
					  <td><%=companyName %></td>
					  </tr>
					  <tr>
					  <td><emp:message key="appmage_jcsj_yhgl_text_buscode" defVal="企业编号：" fileName="appmage"></emp:message>：</td>
					  <td><%=companyCode %></td>
					  </tr>
					  <tr>
					  <td><emp:message key="appmage_jcsj_khdxz_text_publictime" defVal="发布日期" fileName="appmage"></emp:message>：</td>
					  <td>
					  <input type="text" class="Wdate" readonly="readonly"  onclick="time()" value="<%=iospushdate %>"
											id="iospushdate" name="pushdate">
					  </td>
					  </tr>
					  <tr>
					  <td><emp:message key="appmage_jcsj_khdxz_text_filesize" defVal="文件大小" fileName="appmage"></emp:message>：</td>
					  <td><input id="iosfilesize" name="filesize" value="<%=iosfilesize %>"  maxlength="10"   /> MB</td>
					  </tr>
					  <tr>
					  <td><emp:message key="appmage_jcsj_khdxz_text_fileversion" defVal="文件版本" fileName="appmage"></emp:message>：</td>
					  <td><input id="iosfileversion" name="fileversion" value="<%=iosfileversion %>"  maxlength="20" /></td>
					  </tr>
					   <tr>
					  <td><emp:message key="appmage_jcsj_khdxz_text_suport" defVal="支持" fileName="appmage"></emp:message>：</td>
					  <td><input id="iosfiledescr" name="filedescr" value="<%=iosfiledescr %>"  maxlength="30" /></td>
					  </tr>
					  </table>
					  <table>
					  <tr class="btntr">
					  <td> <input type="submit" class="btnClass5" name="1" value="<emp:message key="appmage_common_opt_baocun" defVal="保存" fileName="appmage"></emp:message>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				    	    	<input type="button" class="btnClass6" onclick="cupdate()" name="2" value="<emp:message key="appmage_common_opt_quxiao" defVal="取消" fileName="appmage"></emp:message>"><br/></td>
					  </tr>
			  	</table> 
			   </div>
			   
			   
			   
			   <div id="adtemplatediv" style="display: none;" >
				  <table>
				  <tr>
				  <td style="width: 70px;"><emp:message key="appmage_jcsj_khdxz_text_ssqy" defVal="所属企业" fileName="appmage"></emp:message>：</td>
				  <td><%=companyName %></td>
				  </tr>
				  <tr>
				  <td><emp:message key="appmage_jcsj_yhgl_text_buscode" defVal="企业编码" fileName="appmage"></emp:message>：</td>
				  <td><%=companyCode %></td>
				  </tr>
				  <tr>
				  <td><emp:message key="appmage_jcsj_khdxz_text_publictime" defVal="发布日期" fileName="appmage"></emp:message>：</td>
				  <td><input type="text" class="Wdate" readonly="readonly" onclick="time()" value="<%=adpushdate %>"
											id="adpushdate" name="pushdate">
				  </td>
				  </tr>
				  <tr>
				  <td><emp:message key="appmage_jcsj_khdxz_text_filesize" defVal="文件大小" fileName="appmage"></emp:message>：</td>
				  <td><input id="azfilesize" name="filesize" value="<%=azfilesize %>"  maxlength="10" /> MB</td>
				  </tr>
				  <tr>
				  <td><emp:message key="appmage_jcsj_khdxz_text_fileversion" defVal="文件版本" fileName="appmage"></emp:message>：</td>
				  <td><input id="adfileversion" name="fileversion" value="<%=adfileversion %>" maxlength="20" /></td>
				  </tr>
				   <tr>
				  <td><emp:message key="appmage_jcsj_khdxz_text_suport" defVal="支持" fileName="appmage"></emp:message>：</td>
				  <td><input id="adfiledescr" name="filedescr" value="<%=adfiledescr %>"  maxlength="30" /></td>
				  </tr>
				  </table>
				  <table>
				   <tr class="btntr">
				   <td>
				   			<input type="submit" class="btnClass5" name="3" value="<emp:message key="appmage_common_opt_baocun" defVal="保存" fileName="appmage"></emp:message>">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				    	    <input type="button" class="btnClass6" onclick="cupdate()" name="4" value="<emp:message key="appmage_common_opt_quxiao" defVal="取消" fileName="appmage"></emp:message>"><br/>
				   </td>
				   </tr>
				  </table> 
			   </div>
			   
			   
			<div id="iostemscdiv" style="display: none;">
			    			<input type="hidden" id="txt" name="txt" class="w_input inputSpec" style="margin-right:5px;">
			    			<input type="button" value="<emp:message key="appmage_jcsj_khdxz_text_uploaderweima" defVal="上传二维码" fileName="appmage"></emp:message>" size="30" class="btnClass4">
			    			<input type="file" id="logo_img"  name="logo_img" style="height:26px;"  class="files">
			    			<input type="hidden" name="company_logo" value="<%=iosfileurl %>"/>
			</div>
			
			<div id="adtemscdiv" style="display: none;">
			    			<input type="hidden" id="txt" name="txt" class="w_input inputSpec" style="margin-right:5px;">
			    			<input type="button" value="<emp:message key="appmage_jcsj_khdxz_text_uploaderweima" defVal="上传二维码" fileName="appmage"></emp:message>" size="30" class="btnClass4">
			    			<input type="file" id="logo_img1"  name="logo_img1" class="files">
			    			<input type="hidden" name="company_logo" value="<%=adfileurl %>"/>
			</div>
			
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
    <div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=iPath %>/js/app_clidownload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			var result='<%=request.getParameter("result")%>';
			if(result=="true"){
				//alert("修改成功");
				alert(getJsLocaleMessage('appmage','appmage_page_clidownload_midifysuc'));
				location="<%=path %>/app_clidownload.htm?lgcorpcode=<%=request.getParameter("lgcorpcode") %>&lgusername=<%=request.getParameter("lgusername") %>";
			}else if(result=="false"){
				//alert("修改失败");
				alert(getJsLocaleMessage('appmage','appmage_page_clidownload_midifyfalse'));
				location="<%=path %>/app_clidownload.htm?lgcorpcode=<%=request.getParameter("lgcorpcode") %>&lgusername=<%=request.getParameter("lgusername") %>";
			}	
			var iosfileurl='<%=iosfileurl%>';
			var adfileurl='<%=adfileurl%>';
			if(iosfileurl!=''){
				$("#iosmain_pre").find("img:eq(0)").attr('src',iosfileurl).show().siblings().hide();
			}else{
				$("#iosmain_pre").find("img:eq(0)").hide().siblings().show();
			}
			
			if(adfileurl!=''){
				$("#admain_pre").find("img:eq(0)").attr('src',adfileurl).show().siblings().hide();
			}else{
				$("#admain_pre").find("img:eq(0)").hide().siblings().show();
			}
			
			$('.files').live('change',function(){
				var id = $(this).attr('name');
				upload(id);
			})
			
		});
		
		
		function toupdate(type){
			if(type==0){
				var clone=$('#iostemplatediv').children().clone();
				var clone2=$('#iostemscdiv').children().clone();
				$('#iosdiv').html(clone);
				$('#iosscdiv').html(clone2);
			}else if(type==1){
				var clone=$('#adtemplatediv').children().clone();
				var clone2=$('#adtemscdiv').children().clone();
				$('#addiv').html(clone);
				$('#adscdiv').html(clone2);
			}
		}
		
		function cupdate(){
			location=location;
		}
		
		//开始时间
		function time(){
		    var max = "2099-12-31";
		    var min = "1900-01-01";
			WdatePicker({dateFmt:'yyyy' + getJsLocaleMessage('appmage','appmage_page_clidownload_year') + 'MM' + getJsLocaleMessage('appmage','appmage_page_clidownload_month') + 'dd' + getJsLocaleMessage('appmage','appmage_page_clidownload_day'),minDate:min,maxDate:max});
		}
		</script>
  </body>
</html>
