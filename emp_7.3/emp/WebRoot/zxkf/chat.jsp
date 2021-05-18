<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiUserInfo"%>
<%@page import="com.montnets.emp.entity.online.LfOnlGroup"%>
<%@page import="com.montnets.emp.znly.biz.CustomStatusBiz"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	LfSysuser sysuser = (LfSysuser) request.getAttribute("sysuser");
	@SuppressWarnings("unchecked")
	List<LfOnlGroup> groupList = (List<LfOnlGroup>) request.getAttribute("groupList");
	@SuppressWarnings("unchecked")
	List<LfWeiUserInfo> chatUserInfos = (List<LfWeiUserInfo>) request.getAttribute("chatUserInfos");
	LfWeiAccount account = (LfWeiAccount) request.getAttribute("account");
	
	String userJson = (String) request.getAttribute("userJson");
	String groupJson = (String) request.getAttribute("groupJson");
	String userInfoJson = (String) request.getAttribute("userInfoJson");
	String appInfoJson = (String) request.getAttribute("appInfoJson");
	String openId = account == null?"":account.getOpenId();
	Long userCustomeId = sysuser.getUserId();
	Long aId = account == null?0L:account.getAId();
	//Map<String,Integer> customStatusMap = CustomStatusBiz.customStatusMap.get(sysuser.getAId().toString());
	
	//语言方面相关
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	
	
%>
<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<%@include file="/common/common.jsp" %>
	<title><emp:message key="zxkf_chat_title_1" defVal="在线聊天窗口" fileName="zxkf"/> </title>
	<link rel="icon" href="<%=path%>/common/img/favicon.ico" mce_href="<%=path%>/common/img/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="<%=path%>/common/img/favicon.ico" type="image/x-icon" />
    <link rel="bookmark" href="<%=path%>/common/img/favicon.ico" type="image/x-icon"/>
	<link rel="stylesheet" href="<%=iPath%>/static/css/base.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath%>/static/css/im.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath%>/static/css/face.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath%>/static/css/animate.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath%>/static/css/nanoscroller.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=path %>/common/widget/artDialog/skins/default.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath%>/static/js/lib/fancyBox/jquery.fancybox.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=iPath%>/static/css/newIm.css?V=<%=StaticValue.getJspImpVersion() %>">
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
</head>
<body>

	<h2 class="ywx animate1"> <emp:message key="zxkf_chat_title_2" defVal="企业移动即时沟通专家 | 梦网.com" fileName="zxkf"/></h2>
	<h2 class="ywx animate2"><emp:message key="zxkf_chat_title_2" defVal="企业移动即时沟通专家 | 梦网.com" fileName="zxkf"/></h2>
	<div class="copyinfo">&nbsp;&nbsp;<emp:message key="zxkf_chat_title_2" defVal="企业移动即时沟通专家 | 梦网.com" fileName="zxkf"/></div>
	<div class="tip-browser"><span><emp:message key="zxkf_chat_title_3" defVal="建议使用google浏览器" fileName="zxkf"/></span><span class="tip-browser-x"></span></div>

	<div class="im-header">
		<div class="sign_info">
			<ul>
				<li><emp:message key="zxkf_chat_title_4" defVal="欢迎您" fileName="zxkf"/>[<%=sysuser.getName() %>]</li>
				<%-- <li><a href="#" class="status online dropdown">
					<i></i>在线</a>
					<ul class="dropdown-menu">
						<li><a href="#" class="online"><i></i>我在线上</a></li>
						<li><a href="#" class="leave"><i></i>离开</a></li>
						<li><a href="#" class="busy"><i></i>忙碌</a></li>
						<li><a href="#" class="offline"><i></i>离线</a></li>
					</ul>
				</li> 
				<li><a href="" class="edit-pwd">修改密码</a></li>--%>
				<li><a href="javascript:logout()"><emp:message key="zxkf_chat_title_3" defVal="退出" fileName="zxkf"/></a></li>
			</ul>
		</div>
		<h1>&nbsp;&nbsp;<emp:message key="zxkf_chat_title_3" defVal="建议使用google浏览器" fileName="zxkf"/> </h1>
	</div>
	<div class="ywx-main">
	<div class="im-left">
		<div class="im-left-containter">
			<input type="hidden" id="chosedMenu" value="wx"/><%-- 左菜单 :微信wx ,APP app, 客服custom  --%>
			<div class="im-block-chose" id="im-block"></div>
			<div class="im-menu">
				<div class="im-tmenu-chose" id="im-tmenu">
					<div class="im-menu-icondiv pointer">
						<div class="im-menu-icon">
							<span class="count"></span>
							<span class="wxIcon" style="background-position: 0px 0px;"></span>
						</div>
					</div>
				</div>
				<div class="im-mmenu-default" id="im-mmenu">
					<span class="im-bg1" id="im-mm-bg"></span>
					<div class="im-menu-icondiv pointer">
						<div class="im-menu-icon">
							<span class="count"></span>
							<span class="appIcon"></span>
						</div>
					</div>
				</div>
				<div class="im-bmenu-default" id="im-bmenu">
					<span class="im-bg3" id="im-bm-bg"></span>
					<div class="im-menu-icondiv pointer">
						<div class="im-menu-icon">
							<span class="count"></span>
							<span class="cusIcon"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="im_container">
		<div class="im_contentwrapper">
			<div class="contentcolumn">
				<div class="hd">
					<div class="show-others">
						<a href="" class="btnToggle"></a>
						<div class="dropdown-menu" id="slide-others">
							<ul class="im-mark">
								<li><a href="" class="close-all"><emp:message key="zxkf_chat_title_5" defVal="关闭所有" fileName="zxkf"/></a></li>
								<li><a href="" class="close-have-read"><emp:message key="zxkf_chat_title_6" defVal="关闭已读" fileName="zxkf"/></a></li>
								<li class="divider"></li>
							</ul>
							<div class="nano" id="slide-other-list">
							<div class="content">
								<ul class="tab-group-list" id="slide-others-group">
								
								</ul>
							</div>
							</div>
						</div>
					</div>
					<ul class="hd-tab-list">
						
					</ul>
				</div>
				<div class="bd chatContainer">
					<div class="chatWrapper">
						<div class="chatContent">
							<div class="im-chat-list nano" id="chat-nano">
								<div class="content" id="tab-container">
									
								</div>
							</div>
						</div>
					</div>
					<div class="im-right-sidebar" style="display:none;">
					</div>
					<div class="im-toolbar cc">
						<span class="im-t5 uploadify" style="width:18px;height:18px;overflow:hidden;">
							<input id="file_upload" name="file_upload" type="file" style="width:18px;opacity:0;">
						</span>
						<span class="im-t6" id="emoticons-icon"></span>
						<span class="im-t4" id="tranService" style="display:none;"></span>
					</div>
					<div class="im-send-area">
						<div class="im-edit-msg" contenteditable="true" hidefocus="true" ></div>
					</div>
					<div class="im-edit-btn-area">
						<div class="im-btn-send-area" title="<emp:message key='zxkf_chat_title_7' defVal='按Enter键发送' fileName='zxkf'/>">
							<a href="" class="im-btn im-btn-send" id="sendMsg">
								<span class="im-txt"><emp:message key="zxkf_chat_title_8" defVal="发送" fileName="zxkf"/></span>
							</a>
							<%-- <a href="" class="im-btn im-btn-send-set" title="发送设置">
							</a> --%>
						</div>
						<a href="" class="im-btn im-btn-close" style="display:none">
							<span class="im-txt"><emp:message key="zxkf_chat_title_9" defVal="关闭" fileName="zxkf"/></span>
						</a>

						<span id="im-edit-tip"><emp:message key="zxkf_chat_title_10" defVal="您还可以输入" fileName="zxkf"/><span id="limit-count">650</span><emp:message key="zxkf_chat_title_11" defVal="个字" fileName="zxkf"/></span>
						<div class="send-set">
							<ul>
								<li><a href="" class="current" data-method="Enter">
									<i></i><emp:message key="zxkf_chat_title_12" defVal="按Enter键发送消息" fileName="zxkf"/></a>
								</li>
								<li><a href="" data-method="Ctrl+Enter">
									<i></i><emp:message key="zxkf_chat_title_13" defVal="按Ctrl+Enter键发送消息" fileName="zxkf"/></a>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="im-sidebar">
			<%--
			<div class="im-search hide">
				<input type="text" name="" class="i-inp" placeholder="请输入内容">
				<div class="seq-img"></div>
			</div>
			 --%>
			<%-- 微信 开始 --%>
			<div class="cus-group mt10"  id="cus-group-wx">
				<div class="hd im-side-tab">
					<ul class="tab_menu">
						<li class="cus_menu"><emp:message key="zxkf_chat_title_14" defVal="微信客户" fileName="zxkf"/><i class="im-badge">0</i></li>
					</ul>
				</div>
				<div class="bd tab_box nano" id="chat-tab-box">
					<div class="content">
						<div class="tabContainer">
							<span class="mod-cus-title"><emp:message key="zxkf_chat_title_15" defVal="当前接入" fileName="zxkf"/></span>
							<ul class="tab-group-list mod-cus">
								
							</ul>
							<span class="mod-cus-title"><emp:message key="zxkf_chat_title_16" defVal="今日接入" fileName="zxkf"/></span>
							<ul class="tab-group-list mod-cus-today">
								
							</ul>
							<span class="mod-cus-title"><emp:message key="zxkf_chat_title_17" defVal="昨日接入" fileName="zxkf"/></span>
							<ul class="tab-group-list mod-cus-yest">
								
							</ul>
							<span class="mod-cus-title"><emp:message key="zxkf_chat_title_18" defVal="更早接入" fileName="zxkf"/></span>
							<ul class="tab-group-list mod-cus-ago">
								
							</ul>
						</div>
					</div>
				</div>
			</div>
			<%-- 微信 结束 --%>
			
			<%-- APP 开始 --%>
			<div class="cus-group mt10 displayn" id="cus-group-app">
				<div class="hd im-side-tab">
					<ul class="tab_menu">
						<li class="current app_menu"><emp:message key="zxkf_chat_title_19" defVal="APP客户" fileName="zxkf"/><i class="im-badge">0</i></li>
					</ul>
				</div>
				<div class="bd tab_box nano" id="chat-tab-box">
					<div class="content">
						<div class="tabContainer">
							<span class="mod-cus-title"><emp:message key="zxkf_chat_title_15" defVal="当前接入" fileName="zxkf"/></span>
							<ul class="tab-group-list mod-kh">
								
							</ul>
							<span class="mod-cus-title"><emp:message key="zxkf_chat_title_16" defVal="今日接入" fileName="zxkf"/></span>
							<ul class="tab-group-list mod-kh-today">
								
							</ul>
							<span class="mod-cus-title"><emp:message key="zxkf_chat_title_17" defVal="昨日接入" fileName="zxkf"/></span>
							<ul class="tab-group-list mod-kh-yest">
								
							</ul>
							<span class="mod-cus-title"><emp:message key="zxkf_chat_title_18" defVal="更早接入" fileName="zxkf"/></span>
							<ul class="tab-group-list mod-kh-ago">
								
							</ul>
							<span class="mod-cus-title"><emp:message key="zxkf_chat_title_20" defVal="从未接入" fileName="zxkf"/></span>
							<ul class="tab-group-list mod-kh-never">
								
							</ul>
						</div>
					</div>
				</div>
			</div>			
			<%-- APP 结束 --%>
			
			<%-- 客服 开始 --%>
			<div class="cus-group mt10 displayn"  id="cus-group-custom">
				<div class="hd im-side-tab">
					<ul class="tab_menu">
						<li class="current kf_menu"><emp:message key="zxkf_chat_title_21" defVal="客服" fileName="zxkf"/><i class="im-badge">0</i></li>
						<li class="group_menu"><emp:message key="zxkf_chat_title_22" defVal="群组" fileName="zxkf"/><i class="im-badge">0</i></li>
					</ul>
				</div>
				<div class="bd tab_box nano" id="chat-tab-box">
					<div class="content">
						<div class="tabContainer">
							<ul class="tab-group-list mod-kf">

							</ul>
						</div>
						<div class="tabContainer hide">
							<div class="tab-group-title">
								<i class="group-plus"></i>
								<a href="" id="add_group"><emp:message key="zxkf_chat_title_23" defVal="创建群组" fileName="zxkf"/></a>
							</div>
							<ul class="tab-group-list mod-group">
							</ul>
						</div>
					</div>
				</div>
			</div>			
			<%-- 客服 结束 --%>
			
		</div>
	</div>
	
	<div class="clear"></div>
	</div>
	<%--hide-element--%>
	<%--I-Say--%>
	<div id="item-me-panel" class="hide">
		<div class="im-item item-me">
			<div class="im-message cc">
				<div class="im-user-area">
					<a href="javascript:void(0)" class="im-user-pic">
						<img src="<%=iPath%>/static/images/user1.png" alt="" class="userPic" width="46" heigh="46">
						<img src="<%=iPath%>/static/images/tx.png" alt="" class="userCap">
					</a>
				</div>
				<div class="im-message-detail">
					<table class="im-message-table" border="0" cellpadding="0" cellspacing="0"> 
						<tbody>   
							<tr>      
								<td class="lt"></td>     
								<td class="tt"></td>     
								<td class="rt"></td> 
							</tr>
							<tr> 
								<td class="lm"></td>
								<td class="mm">
									<div class="im-message-title">
										<span class="im-send-time">14:28:12</span>
										<span class="im-txt-bold mename"></span>
									</div> 
									<div class="im-message-content"> 	
										<p ></p> 
									</div> 
								</td> 
								<td class="rm"><span></span></td>
							</tr>
							<tr>        
								<td class="lb"></td>           
								<td class="bm"></td>            
								<td class="rb"></td>         
							</tr>      
						</tbody>   
					</table>
				</div>
			</div>
		</div>
	</div>
	<%--end--%>
	<%--cus say--%>
	<div id="item-panel" class="hide">
	<div class="im-item">
		<div class="im-message cc">
			<div class="im-user-area">
				<a href="javascript:void(0)" class="im-user-pic">
					<img src="<%=iPath%>/static/images/icon_customer_80.png" alt="" class="userPic" width="46" heigh="46">
					<img src="<%=iPath%>/static/images/tx.png" alt="" class="userCap">
				</a>
			</div>
		
		<div class="im-message-detail">
			<table class="im-message-table" border="0" cellpadding="0" cellspacing="0"> 
				<tbody>   
					<tr>      
						<td class="lt"></td>     
						<td class="tt"></td>     
						<td class="rt"></td> 
					</tr>
					<tr> 
						<td class="lm">
							<span></span>
						</td>
						<td class="mm">
							<div class="im-message-title">
								<p class="im-message-owner">	
									<span class="im-txt-bold"></span>
								</p>	
								<span class="im-send-time"></span>
							</div> 
							<div class="im-message-content"> 	
								<p ></p> 
							</div> 
						</td> 
						<td class="rm"><span></span></td>
					</tr>
					<tr>        
						<td class="lb"></td>           
						<td class="bm"></td>            
						<td class="rb"></td>         
					</tr>      
				</tbody>   
			</table>
		</div>
		</div>
	</div>
	</div>
	<div id="end-panel" class="hide">
		<div class="chatMsg">
			<div class="im-icon"></div>
				<p></p>
			</div>
		</div>
	<div id="context-menu">
		<span class="Corner"></span>
			<ul></ul>
	</div>
	<div id="context-menu2">
		<span class="Corner"></span>
			<ul></ul>
	</div>
	<div class="hide" id="voice-div">
	<form action="<%=path %>/wxcommon/voicePlay.jsp" name="voiceForm" target="hidenFrame">
		<input type="hidden" name="browerVer" id="browerVer" value=""/>
		<input type="hidden" name="voiceFile" id="voiceFile" value=""/>
	</form>
	<iframe id="hidenFrame" name="hidenFrame"></iframe>
	</div>
	<div id="htmlToText" class="hide"></div>
	<div id="bubbleContent"></div>
	<%--end--%>
	<script>
		var userJson = <%=userJson%>;//客服列表json {name:名称,customeId:客服Id,state:客服状态}
		var groupJson = <%=groupJson%>;//群组列表json{name:名称,groupid:群组id,count:人员数量}
		var userInfoJson = <%=userInfoJson%>;//客户列表json{name:名称,openid:客户微信号id,servernum:服务号}
		var appUserJson = <%=appInfoJson%>;//app用户列表json{name:名称,appcode:用户标识id,servernum:服务号}
		var openId = "<%=openId%>";// 用户所属公众号微信ID
		var aId = <%=aId%>;//用户所属公众号id
		var userCustomeId = <%=userCustomeId%>;//当前登录的客服人员Id
		var iPath="<%=iPath%>";
		var path="<%=path%>";
		var groupKeys = "";
		var kfname='<%=sysuser.getName() %>';
		var corpCode='<%=sysuser.getCorpCode() %>';
		var isOkToSend = 1;
	</script>
	<script type="text/javascript" src="<%=iPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=iPath%>/common/i18n/<%=langName%>/zxkf_<%=langName%>.js"></script>
	<script src="<%=iPath%>/static/js/im.js?V=<%=StaticValue.getJspImpVersion() %>" data-main="<%=iPath%>/static/js/main"></script>
	<script src="<%=iPath%>/static/js/require.js?V=<%=StaticValue.getJspImpVersion() %>" data-main="<%=iPath%>/static/js/main"></script>
</body>
</html>