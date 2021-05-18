<%@ page language="java" import="com.montnets.emp.common.constant.SystemGlobals" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.entity.system.LfThiMenuControl" %>
<%@page import="com.montnets.emp.entity.sysuser.LfDep"%>
<%@page import="com.montnets.emp.entity.sysuser.LfPrivilege"%>
<%@page import="com.montnets.emp.util.StringUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
@SuppressWarnings("unchecked")
Map<Integer, String> thirdMenuMap = (Map<Integer, String>) session.getAttribute("thirdMenuMap");
@SuppressWarnings("unchecked")
List<LfThiMenuControl> thirdMenuList = (List<LfThiMenuControl>)request.getAttribute("thirdMenuList");
@SuppressWarnings("unchecked")
Map<String,List<LfPrivilege>> menuMap = (Map<String,List<LfPrivilege>>)session.getAttribute("priMap");

LfDep lfdep=(LfDep)request.getAttribute("lfdep");
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String selPriMenus = request.getParameter("priMenus");
String openMenuCode = request.getParameter("openMenuCode");
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String corpcode=lfdep!=null?lfdep.getCorpCode():"100001";
//@ SuppressWarnings("unchecked")
//Map<String,String> infoMap = (Map<String,String>) session.getAttribute("infoMap");
//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
//boolean isCharging = (infoMap.get("feeFlag")!=null&&"true".equals( infoMap.get("feeFlag")))?true:false;//是否启用计费机制
boolean isCharging = SystemGlobals
			.isDepBilling((corpcode == null || corpcode == "") ? "100001"
					: corpcode);//是否启用计费机制
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
//是否显示运营商余额
boolean isShowYe = btnMap.get(ViewParams.YECODE+"-0") == null ? false : true;
//是否显示公告
boolean isShowGg = btnMap.get(ViewParams.GGCODE+"-0") == null ? false : true;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><%@ include file="/common/common.jsp"%>
    <title></title>
   	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=iPath %>/css/left.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/left.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.left-nav {
			width: 184px;
			height: 100%;
			background: #2c3140;
			position: absolute;
		    z-index: 3;
			box-shadow: 5px 0px 5px #e6edf0;
		}

		.left-nav li {
			padding-left: 48px;
			padding-top: 12px;
			padding-bottom: 12px;
			cursor: pointer;
		}

		.left-nav li span {
			position: absolute;
			top: 14px;
			right: 20px;
			z-index: 2;
			width: 20px;
			background: url("<%=skin %>/images/Arrow_unSelected_icon.png") no-repeat center center;
		}

		.nav-logo {
			height: 60px;
			background: url(<%=skin %>/images/logo.png) no-repeat 20px center;
			background-color: #2c3140;
			padding-left: 55px;
		    font-family: 微软雅黑;
		    color: white;
		    font-size: 18px;
		    line-height: 60px;
		    border-bottom: #181b24 1px solid;
		}

		.nav-index {
			height: 40px;
			line-height: 40px;
			font-size: 14px;
			color: #bdc3d3;
			padding-left: 48px;
			font-family: 微软雅黑;
			cursor: pointer;
		}

		.nav-index:hover{
			background: #00ab72;
			color:#fff;
		}

		.nav-menu li{
			position: relative;
			font-size: 14px;
			color: #bdc3d3;
			align-items: center;
		    list-style: none;
		}

		.nav-menu li:hover{
			/* background-color: #00ab72; */
			color:#fff;
		}

		.nav-menu li:hover span{
			background: url("<%=skin %>/images/Arrow_Selected_icon.png") no-repeat center center;
		}

		.second-nav-menu {
			left: 184px;
			position: absolute;
			background-color: #2c3140;
			opacity: 0.92;
			width:1080px;
			display:none;
			cursor: default;
		}

		.second-nav-menu li:last-child{
			padding-right: 22px;
		}

		.second-nav-menu p,
		.left-nav li span {
			height: 20px;
		}

		.second-nav-menu li {
			font-size: 14px;
			padding: 0;
			float:left;
		}

		.second-nav-menu li .second-nav-title{
			padding-left: 8px;
			width: <%= StaticValue.ZH_HK.equals(empLangName)?164:134%>px;
		    height: 14px;
	        margin-top: 16px;
		    padding-bottom: 14px;
		    font-size: 14px;
		    margin-left: 22px;
	        border-bottom: #434b62 1px solid;
		}

		.second-nav-menu li .second-nav-title:hover{
			background-color: #2c3140;
		}

		.third-nav-menu {
			position: relative;
			padding-bottom: 10px;
		}

		.second-nav-menu .third-nav-menu li{
			padding: 10px 10px 10px 30px;
		}

		.third-nav-menu li{
			cursor: pointer;
			display: block;
			border: none;
			float:none;
		    text-overflow: ellipsis;
		    overflow: hidden;
		    white-space: nowrap;
		    width: 115px;
		}
		.nav-menu li:hover .second-nav-menu p{
			background-color: #2c3140;
			cursor: pointer;
			color: #fff;
		}

		.nav-menu li:hover .third-nav-menu{
			background-color: #2c3140;
		}
		.nav-menu li:hover .third-nav-menu li a:hover{
			color: #00ab72;
			background-color: #2c3140;
		}
		.third-nav-menu li a{
			text-decoration:none;
			color:#bdc3d3;
		}

		.nav-menu li:hover .third-nav-menu li:hover{
			color: #00ab72;
			background-color: #2c3140;
		}

		#mod1{
			background: url(<%=skin %>/images/mod1_unSelected_icon.png) no-repeat 20px center;
		}
		#mod1:hover{
			background: url(<%=skin %>/images/mod1_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod2{
			background: url(<%=skin %>/images/mod2_unSelected_icon.png) no-repeat 20px center;
		}
		#mod2:hover{
			background: url(<%=skin %>/images/mod2_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod4{
			background: url(<%=skin %>/images/mod4_unSelected_icon.png) no-repeat 20px center;
		}
		#mod4:hover{
			background: url(<%=skin %>/images/mod4_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod9{
			background: url(<%=skin %>/images/mod9_unSelected_icon.png) no-repeat 20px center;
		}
		#mod9:hover{
			background: url(<%=skin %>/images/mod9_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod11{
			background: url(<%=skin %>/images/mod11_unSelected_icon.png) no-repeat 20px center;
		}
		#mod11:hover{
			background: url(<%=skin %>/images/mod11_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod12{
			background: url(<%=skin %>/images/mod12_unSelected_icon.png) no-repeat 20px center;
		}
		#mod12:hover{
			background: url(<%=skin %>/images/mod12_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod13{
			background: url(<%=skin %>/images/mod13_unSelected_icon.png) no-repeat 20px center;
		}
		#mod13:hover{
			background: url(<%=skin %>/images/mod13_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod14{
			background: url(<%=skin %>/images/mod14_unSelected_icon.png) no-repeat 20px center;
		}
		#mod14:hover{
			background: url(<%=skin %>/images/mod14_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod16{
			background: url(<%=skin %>/images/mod16_unSelected_icon.png) no-repeat 20px center;
		}
		#mod16:hover{
			background: url(<%=skin %>/images/mod16_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod18{
			background: url(<%=skin %>/images/mod18_unSelected_icon.png) no-repeat 20px center;
		}
		#mod18:hover{
			background: url(<%=skin %>/images/mod18_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod20{
			background: url(<%=skin %>/images/mod20_unSelected_icon.png) no-repeat 20px center;
		}
		#mod20:hover{
			background: url(<%=skin %>/images/mod20_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod21{
			background: url(<%=skin %>/images/mod21_unSelected_icon.png) no-repeat 20px center;
		}
		#mod21:hover{
			background: url(<%=skin %>/images/mod21_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod22{
			background: url(<%=skin %>/images/mod22_unSelected_icon.png) no-repeat 20px center;
		}
		#mod22:hover{
			background: url(<%=skin %>/images/mod22_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
		#mod25{
			background: url(<%=skin %>/images/mod25_unSelected_icon.png) no-repeat 20px center;
		}
		#mod25:hover{
			background: url(<%=skin %>/images/mod25_Selected_icon.png) no-repeat 20px center;
			background-color: #00ab72;
			color:#fff;
		}
        #mod26{
            background: url(<%=skin %>/images/mod26_unSelected_icon.png) no-repeat 20px center;
        }
        #mod26:hover{
            background: url(<%=skin %>/images/mod26_Selected_icon.png) no-repeat 20px center;
            background-color: #00ab72;
            color:#fff;
        }
        #mod28{
            background: url(<%=skin %>/images/mod28_unSelected_icon.png) no-repeat 20px center;
        }
        #mod28:hover{
            background: url(<%=skin %>/images/mod28_Selected_icon.png) no-repeat 20px center;
            background-color: #00ab72;
            color:#fff;
        }
	</style>
	<%if(StaticValue.ZH_HK.equals(langName)){%>
<style>
.mune_show span {
	text-indent: 0px;
	line-height: 13px;
	width: 165px;
    padding-left: 26px;
    font-size: 11px;
	display: block;
	padding-top: 5px;
	height: 31px;
	word-break: normal;
	background: url(<%=skin%>/images/icon_right_sel.gif) 7px 5px no-repeat;
}

.mune_show.open span {
	background: url(<%=skin%>/images/icon_down_sel.gif) 7px 5px no-repeat;
}

#sider .mune_hidden li {
	white-space: normal;
}

#sider .mune_hidden li a {
	line-height: 2px;
    height: 39px;
	word-break: normal;
	width: 100%;
	text-align: left;
	text-indent: 15px;
	/* background-color: #dce8fb; */
}

.act_ico {
	left: 5px;
	top: 12px;
}

#theme span {
	padding-left: 26px;
	padding-top: 10px;
    height: 35px;
}
#balance span {
	padding-left: 26px;
	padding-top: 10px;
    height: 35px;
}
#ggLi span {
	padding-left: 26px;
	padding-top: 10px;
    height: 35px;
}
#upsc span {
	padding-left: 26px;
	padding-top: 10px;
    height: 35px;
}

</style>
	  <%}%>
	  <%if(StaticValue.ZH_HK.equals(empLangName)){%>
	  <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	  <%}%>
	  <!--[if IE 6]>
        <script type="text/javascript" src="<%=path%>/common/js//DD_belatedPNG.js"></script>
        <script language="javascript" type="text/javascript">
        DD_belatedPNG.fix(".act_ico img");
        </script>
        <![endif]-->
	<script type="text/javascript">
		var path = '<%=path%>';
		var getContextPath = '<%=request.getContextPath()%>';
		var selPriMenus = <%=selPriMenus%>;
	</script>
	</head>
  <body onload="reOpen()">
  <input type="hidden" id="langName" value="<%=langName%>" />
  <div class="logininfo" style="display:none"></div>

  <div class="left-nav">
			<%-- <iframe style="height: 100%;" src="<%=path %>/thirdMenu.htm?method=getAllPriList" >浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。</iframe> --%>
			<div class="nav-index" onclick="javascript:doOpen('/thirdMenu.htm?method=toNewIndex', 'Index', '首页');"><emp:message key="common_index" fileName="common" defVal="首页" /></div>



			<ul class="nav-menu">


			<%Iterator<Integer> ite = thirdMenuMap.keySet().iterator();
    	int index = 0;
    	while(ite.hasNext())
    	{
    		index = 0;
    		Integer key = ite.next();
    	%>

    	<li id="mod<%=key %>"><%=thirdMenuMap.get(key) %><span></span>

    	<ul class="second-nav-menu">


    	<%
	if(thirdMenuList != null && thirdMenuList.size()>0)
	{
		int size = thirdMenuList.size();
		//String clorIndex = "";
		String priMenus=String.valueOf(thirdMenuList.get(0).getPriMenu());
		String title = thirdMenuList.get(0).getTitle();

		if(StaticValue.ZH_TW.equals(empLangName)){
			title = thirdMenuList.get(0).getZhTwTitle();
		}
		if(StaticValue.ZH_HK.equals(empLangName)){
			title = thirdMenuList.get(0).getZhHkTitle();
		}

		Integer menuNum = thirdMenuList.get(0).getMenuNum();

		for(int i=1;i<size;i++)
		{
			LfThiMenuControl menu = thirdMenuList.get(i);
			if(menuNum-menu.getMenuNum()==0)
			{
				priMenus+=","+String.valueOf(menu.getPriMenu());
			}else
			{
	   		if(menuMap == null )
   			{
   				continue;
   			}
	   		String[] menus = priMenus.split(",");
			for(int m=0;m<menus.length;m++)
			{

	   			List<LfPrivilege> prList = menuMap.get(menus[m]);
	   			if(prList == null )
	   			{
	   				continue;
	   			}
	   			String modName="";
	   			String showModName = "";
	   			String showMenuName = "";
	 			int prsize=prList.size();
	   			for(int p=0;p<prsize;p++)
	 			{
	   				if(menuNum==key){
	 				LfPrivilege pri=prList.get(p);

					showMenuName = pri.getMenuName();
	 				if(StaticValue.ZH_TW.equals(empLangName)){
	 					showMenuName = StringUtils.isEmpty(pri.getZhTwMenuName())?pri.getMenuName():pri.getZhTwMenuName();
 					}
 					if(StaticValue.ZH_HK.equals(empLangName)){
 						showMenuName = StringUtils.isEmpty(pri.getZhHkMenuName())?pri.getMenuName():pri.getZhHkMenuName();
 					}

	 				if(!modName.equals(pri.getModName()))
	 				{
	 					modName=pri.getModName();
	 					showModName=pri.getModName();
	 					if(StaticValue.ZH_TW.equals(empLangName)){
	 						showModName = pri.getZhTwModName();
	 		   			}
	 		   			if(StaticValue.ZH_HK.equals(empLangName)){
	 						showModName = pri.getZhHkModName();
	 		   			}
	    	%>

		    	<li>
						<p class="second-nav-title"><%=showModName %></p>
							<ul class="third-nav-menu">
								<% 	} %>
									<li onmouseover="mouseOver('<%=pri.getMenuCode() %>')" onmouseout="mouseOut('<%=pri.getMenuCode() %>')" title='<%=showMenuName %>' onclick="javascript:doOpen('<%=pri.getMenuSite() %>','<%=pri.getMenuCode() %>','<%=showMenuName %>')" ><a id="ak<%=pri.getMenuCode() %>"><%=showMenuName %>
						           		</a>
						           		<%if(prList.get(p).getModName().contains("我的快捷场景")){%>
						           			<input type="button" id="closeLi<%=pri.getMenuCode()%>" value="x" style="border: 0;width: 20px;height:20px;position: absolute;left: 130px;cursor: pointer;background-color: #f1f1f9;display:none;" onclick="deleteLi('<%=pri.getMenuCode()%>','<%=showMenuName %>');">
						           		<%}%>
					           		</li>
	           						 <% if(p==prsize-1 || !modName.equals(prList.get(p+1).getModName())) { %>
	    					</ul>
		        </li>

	        <% } }}} %>

<%
				title = menu.getTitle();
				if(StaticValue.ZH_TW.equals(empLangName)){
					title = thirdMenuList.get(0).getZhTwTitle();
				}
				if(StaticValue.ZH_HK.equals(empLangName)){
					title = thirdMenuList.get(0).getZhHkTitle();
				}
				menuNum=menu.getMenuNum();
				priMenus=String.valueOf(menu.getPriMenu());
			}
		}
	}
%>
    	</ul>
				</li>

    	<%} %>


			</ul>
		<input type="hidden" id="skin" value="<%=skin%>" />
		<input type="hidden" id="pathUrl" value="<%=path%>" />
		</div>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script src="<%=path %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
<script type="text/javascript">
            //解决 IE9 不兼容 hover 属性的问题；
            function check() {
                if ($.browser.msie && parseInt($.browser.version) <= 9) {//判断浏览器及其版本,如果是IE9及其以下版本统一这样处理
                    myHover("#mod25");
                    myHover("#mod1");
                    myHover("#mod2");
                    myHover("#mod26");
                    myHover("#mod20");
                    myHover("#mod18");
                    myHover("#mod21");
                    myHover("#mod22");
                    myHover("#mod14");
                    myHover("#mod4");
                    myHover("#mod9");
                    myHover("#mod11");
                    myHover("#mod13");
                    myHover("#mod12");
                    myHover("#mod16");
                }
            }
            function myHover(elementId) {
                var num = elementId.replace(/[^0-9]/ig,"");
                $(elementId).hover(function(){
                    $(elementId).css("background","url(<%=skin %>/images/mod" + num + "_Selected_icon.png) no-repeat 20px center");
                    $(elementId).css("background-color","#00ab72");
                    $(elementId).css("color","#fff");
                },function(){
                    $(elementId).css("background","url(<%=skin %>/images/mod" + num + "_unSelected_icon.png) no-repeat 20px center");
                    $(elementId).css("color","#bdc3d3");
                });
                var obj = $(elementId).find("ul").find("li").find("ul").find("li");
                obj.hover(function(){
                    obj.css("color","#fff");
                },function(){
                    obj.css("color","#bdc3d3");
                });
            }


			var secondMenuList = $('.second-nav-menu');
			for ( var i =0; i<secondMenuList.length;i++) {
				var thirdMenuWidth = <%= StaticValue.ZH_HK.equals(empLangName)?194:164%>;
				secondMenuList[i].style.width = (22+thirdMenuWidth*secondMenuList[i].children.length)+"px";
			}
			$(function(){
			    //检查浏览器是不是IE，同时判断版本
                check();
				var _this1=null,
				_firstMenuWidth = $(".nav-menu").width();
				//页面加载时设置左侧菜单栏宽度，不设置宽度会覆盖部分按钮导致无法点击
				$(window.parent.document).find("#leftIframe").width(_firstMenuWidth);
				//$(window.parent.document).find("#frmTitle").width(_firstMenuWidth);

				$('.nav-menu>li').hover(function(){
					_this1=$(this);

					//当前浏览器高度
                    var browserHeight = $(window).height();
                    //当前元素高度
                    var currentElementHeight = _this1.find('.second-nav-menu').height();
                    //当前元素父元素距离顶部距离
                    var toTopDistance = _this1.offset().top;
                    //具体底部距离
                    var toBottomDistance = browserHeight - currentElementHeight - toTopDistance - 60;

                    /*console.log("当前浏览器高度" + browserHeight + ",当前元素高度" + currentElementHeight
                    + ",当前元素父元素距离顶部距离" + toTopDistance + ",具体底部距离" + toBottomDistance);*/
                    if (toBottomDistance < 80) {
                        // var result = _this1.find('.second-nav-menu').find('.second-nav-title').html();
                        // console.log(result);
                        // if ("网关前端配置" == result) {
                        //     _this1.find('.second-nav-menu').css("top","");
                        //     _this1.find('.second-nav-menu').css("bottom","-250px");
                        // } else {
                        // }
                        _this1.find('.second-nav-menu').css("top","");
                        _this1.find('.second-nav-menu').css("bottom","0px");
                        //当前元素父元素距离顶部距离
                        var distance = _this1.find('.second-nav-menu').offset().top;
                        if (0 == distance) {
                            var divHeight = _this1.find('.second-nav-menu').height();
                            // 偏移量
                            var num = divHeight / 2 - 114;
                            _this1.find('.second-nav-menu').css("bottom", "-" + num);
                        }
                    } else {
                    	_this1.find('.second-nav-menu').css("bottom","");
                        _this1.find('.second-nav-menu').css("top","0px");
                    }

					_this1.find('.second-nav-menu').show();
					$(window.parent.document).find("#leftIframe").width("100%");
					$(window.parent.document).find("#frmTitle").width("100%");
				},function(){
					_this1.find('.second-nav-menu').hide();
                    _this1.find('.second-nav-menu2').hide();
					$(window.parent.document).find("#leftIframe").width(_firstMenuWidth);
					$(window.parent.document).find("#frmTitle").width(_firstMenuWidth);
				});

				$('.third-nav-menu>li>a').hover(function(){
					$(this).css("color","#00ab72");
				},function(){
					$(this).css("color","#bdc3d3");
				});

				$('.third-nav-menu>li').click(function(){
					$('.second-nav-menu').hide();
				});
			});
			function mouseOver(obj){
				$("#closeLi"+obj).css({"display":""});
			}
			function mouseOut(obj){
				$("#closeLi"+obj).css({"display":"none"});
			}
			function deleteLi(tempId,name){
				var skin = $("#skin").val();
				if(confirm(getJsLocaleMessage("common","common_confirm_del_myshortcut")+name+"?")){
                     if (window.event) {
						// 针对 IE 取消冒泡事件
						window.event.cancelBubble = true;
						}else{
						 event.stopPropagation();
						}
					$.ajax({
						type:"POST",
						url :"rms_templateMana.htm?method=deleteShortTemp&tempId="+tempId,
		                success:function (result) {
			                if (result == 'true') {
		                        alert(getJsLocaleMessage("common","common_del_success"));
	                         	if(skin.indexOf("frame4.0") != -1||skin.indexOf("frame2.5") != -1){
	                         		var _secondMenu =$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tempId).parent().parent();
			                        var len = _secondMenu.children('li').length;
			                        if(len<2){
			                        	var thirdMenuWidth = <%= StaticValue.ZH_HK.equals(empLangName)?194:164%>;
			                       	 	var width = _secondMenu.parent().parent().width()-thirdMenuWidth;
			                      	 	_secondMenu.parent().parent().css({"width":width});
			                        	_secondMenu.parent().remove();
			                        }else{
			                        	$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tempId).parent().remove();
			                        }
			                        //将我的场景、公共场景中的状态改为：设置为快捷场景
		                        	var shortScenceBt = $(window.parent.document).find("#cont").contents().find("#deleteshortScene_"+tempId);
		                    		shortScenceBt.val("设置为快捷场景");
		                    		shortScenceBt.attr("class","shortScene");
		                    		//shortScenceBt.removeAttr("onclick");
		                    		//shortScenceBt[0].setAttribute("onclick","toShortScene('"+tempId+"','"+name+"');");
                                    window.parent.document.getElementById('cont').contentWindow.location.reload(true);
                                }else{
			                        $(window.parent.document).find("#m"+tempId).remove();
			                        window.parent.document.getElementById('leftIframe').contentWindow.location.reload(true);
			                    	window.location.reload();
			                    }
		                    } else {
		                        alert(getJsLocaleMessage("common","common_del_failure"));
		                    }
		                }
					});
				}
				return false;
			}
			<%-- function openMenu(url, menucode, menuname) {
				var path = '<%=path%>';
					var value=url+","+menucode+","+menuname;
					$('#menuInfo', window.parent.parent.document).html(value);
					$(this).css("background-color", "#FFFFFF");
					if (url.indexOf("http://") > 0) {
						url = url.substr(1);
					} else if (url.indexOf(path + "/") != 0) {
						url = path + url;
					}
					var conditionUrl = "";
					if (url.indexOf("?") > -1) {
						conditionUrl = "&";
					} else {
						conditionUrl = "?";
					}
					$("#loginparams").find(" input").each(function() {
						conditionUrl = conditionUrl + $(this).attr("id") + "=" + $(this).val() + "&";
					});
					conditionUrl = conditionUrl + "timee=" + new Date();
					window.frames['mainFrame'].frames['cont'].location.href = url + conditionUrl;
					if(url.indexOf("/mon_mainMon.htm")>0)
					{
						$('#mon').dialog("open");
					}
					else
					{
						$('#mon').dialog("close");
						$("#mon_showVoice").val("1");
						$('#load-bg').show();
					}

			} --%>
		</script>

  <%--
  <table id="modIndex" class="div_bd block">
  <tbody><tr>
    <td valign="top">

    <div id="sider" class="sider_round_content div_bd">
    <div id="userDiv">
    	<div style="display:block;width:100%;height:128px;" id="userInfo">

    		<div style="padding-top: 27px;padding-left: 20px;font-size: 12px;">
	    		<div style="float:left;width: 62px;height:62px;">
					<span id="photo"></span>
	    		</div>
    			<div style="padding-left: 0px;float: left;color:#493200;">
    				<div style="width: 100px;padding-top:9px;word-wrap:break-word;">
    					<%
    						LfSysuser sysuser=((LfSysuser) session.getAttribute("loginSysuser"));
    						String userName="";
    						String showName="";
    						if(sysuser!=null)
    						{
    							userName=sysuser.getName();
    							if(userName!=null)
    							{
	    							if(userName.length()>5)
	    							{
	    								showName=userName.substring(0,3)+"...";
	    							}
	    							else
	    							{
	    								showName=userName;
	    							}
    							}
    						}
    					%>
    					<xmp onclick="javascript:doUpdatePass()" style="cursor:pointer;display:inline;font-weight:bold;color:#493200" id="loginName" title="<%=userName %>"><%=showName%></xmp>&nbsp;&nbsp;<emp:message key="common_frame2_left_1" defVal="您好！" fileName="common"></emp:message><span style="display: none;" id="showName"><%=userName %></span>
    				</div>
    				<div style="padding-top: 10px;">
						<emp:message key="common_frame2_left_2" defVal="欢迎登录EMP" fileName="common"></emp:message>
    				</div>
    			</div>
    		</div>
    	</div>
    </div>
    <div id="blue_user_info" class="hidden">
     <div class="left_title_bg">
      <div class="info_inner">
        <div class="show_user_name"><%=userName %></div>
      </div>
      </div>
    </div>
    	<ul>
	   		<li class="mune_show" id="theme" >
	   			<a href="javascript:;" class="act_ico theme hidden">
	   			<img class="flag" src="<%=skin %>/images/left_icon.png" />
	   			</a><span onclick="doOpen('/emp_tz.htm?method=toChangeSkin','','主题换肤')"><emp:message key="common_frame2_left_3" defVal="主题换肤" fileName="common"></emp:message></span>
	   		</li>

	   		<%if(isCharging || isShowYe){ %>
	   		<li class="mune_show" id="balance">
	   			<a href="javascript:;" class="act_ico balance hidden">
	   			<img class="flag" src="<%=skin %>/images/left_icon.png" />
	   			</a><span onclick="doOpen('<%=iPath %>/checkFeeBefor.jsp?method=findSpfee','','余额查询')"><emp:message key="common_balanceQuery" defVal="余额查询" fileName="common"></emp:message></span>
	   		</li>
	   		<%} %>
	   		<%if(StaticValue.inniMenuMap.containsKey("/not_notice.htm") && isShowGg){ %>
		   		<li class="mune_show" id="ggLi">
		   			<a href="javascript:;" class="act_ico ggLi hidden">
		   			<img class="flag" src="<%=skin %>/images/left_icon.png" />
		   			</a><span onclick="doOpen('/not_notice.htm?type=1','','公告')"><emp:message key="common_viewNotice" defVal="公告查看" fileName="common"></emp:message></span>
		   		</li>
	   		<%} %>
   			<li class="mune_show" id="upsc" >
	   			<a href="javascript:;" class="act_ico upsc hidden">
	   			<img class="flag" src="<%=skin %>/images/left_icon.png" />
	   			</a><span onclick="upLoad()"><emp:message key="common_helpManual" defVal="帮助手册" fileName="common"></emp:message></span>
	   		</li>
        </ul>
    </div>
      </td>
  </tr>

</tbody></table>
   <div id="blue_user_info" class="hidden">
     <div class="left_title_bg">
      <div class="info_inner">
        <div class="show_user_name"><%=userName %></div>
      </div>
      </div>
    </div>
  <%
	if(thirdMenuList != null && thirdMenuList.size()>0)
	{
		int size = thirdMenuList.size();
		//String clorIndex = "";
		String priMenus=String.valueOf(thirdMenuList.get(0).getPriMenu());
		String title = thirdMenuList.get(0).getTitle();

		if(StaticValue.ZH_TW.equals(langName)){
			title = thirdMenuList.get(0).getZhTwTitle();
		}
		if(StaticValue.ZH_HK.equals(langName)){
			title = thirdMenuList.get(0).getZhHkTitle();
		}

		Integer menuNum = thirdMenuList.get(0).getMenuNum();

		for(int i=1;i<size;i++)
		{
			LfThiMenuControl menu = thirdMenuList.get(i);
			if(menuNum-menu.getMenuNum()==0)
			{
				priMenus+=","+String.valueOf(menu.getPriMenu());
			}else
			{
	%>

   <table id="mod<%=menuNum %>" class="div_bd" menuNum="<%=menuNum %>">
  <tr>
    <td valign="top" >

    <div id="sider" class="sider_round_content div_bd">
    	<ul>
	   		<%
	   		if(menuMap == null )
   			{
   				continue;
   			}
	   		String[] menus = priMenus.split(",");
			for(int m=0;m<menus.length;m++)
			{
	   			List<LfPrivilege> prList = menuMap.get(menus[m]);
	   			if(prList == null )
	   			{
	   				continue;
	   			}
	   			String modName="";
	   			String showModName = "";
	   			String showMenuName = "";

	 			int prsize=prList.size();
	   			for(int p=0;p<prsize;p++)
	 			{
	 				LfPrivilege pri=prList.get(p);

					showMenuName = pri.getMenuName();
	 				if(StaticValue.ZH_TW.equals(langName)){
	 					showMenuName = pri.getZhTwMenuName();
 					}
 					if(StaticValue.ZH_HK.equals(langName)){
 						showMenuName = pri.getZhHkMenuName();
 					}

	 				if(!modName.equals(pri.getModName()))
	 				{
	 					modName=pri.getModName();
	 					showModName=pri.getModName();
	 					if(StaticValue.ZH_TW.equals(langName)){
	 						showModName = pri.getZhTwModName();
	 		   			}
	 		   			if(StaticValue.ZH_HK.equals(langName)){
	 						showModName = pri.getZhHkModName();
	 		   			}
	    	%>
    		<li class="mune_show"><span><%=showModName %></span>
    			<ul class="mune_hidden div_bd">
	    	<% 	} %>
	           		<li><a id="ak<%=pri.getMenuCode() %>" onclick="javascript:doOpen('<%=pri.getMenuSite() %>','<%=pri.getMenuCode() %>',
	           		'<%=showMenuName %>')"><%=showMenuName %></a></li>
            <% if(p==prsize-1 || !modName.equals(prList.get(p+1).getModName())) { %>
    			</ul>
	        </li>
	        <% } }} %>
        </ul>
    </div>
      </td>
  </tr>
</table>
<%
				title = menu.getTitle();
				if(StaticValue.ZH_TW.equals(langName)){
					title = thirdMenuList.get(0).getZhTwTitle();
				}
				if(StaticValue.ZH_HK.equals(langName)){
					title = thirdMenuList.get(0).getZhHkTitle();
				}
				menuNum=menu.getMenuNum();
				priMenus=String.valueOf(menu.getPriMenu());
			}
		}
	}
%> --%>

	<div id="tmdiv"></div>
	<script src="<%=path %>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
   	<script src="<%=path %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
   	<script type="text/javascript" src="<%=iPath %>/js/left.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  </body>
</html>

