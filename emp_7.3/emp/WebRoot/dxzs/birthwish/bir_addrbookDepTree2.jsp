<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String treemethod = request.getParameter("treemethod");
	String url = request.getParameter("url");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
  <%@include file="/common/common.jsp" %>		
<title></title>
<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>

<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  <script type="text/javascript">
	var zTree;
    var zTree1;	
	var setting;
	var isHand = "true";
	var treeNode2;
	setting = {
			async: true,
			fontCss: setFont,
			isSimpleData: true,
			rootPID : 0,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			checkable: false,
			checkRadioType: "level",
			asyncUrl: "<%=path%>/bir_birthdaySendEMP.htm?method=<%=treemethod%>&lguserid="+$(window.parent.parent.document).find("#taskOwnerId").val()+"&lgcorpcode="+$(window.parent.parent.document).find("#lgcorpcode").val(),  //?????????????????????URL??????
			asyncParam: ["name", "id"],  //?????????????????????????????????????????????????????????id???name
			asyncParamOther: ["test","true"], //???????????? ( key, value ???????????????)
			callback:{
				click: zTreeOnClick,
				asyncSuccess:function(event, treeId, treeNode, msg)
				{
					if(!treeNode)
					{
						var rootNode = zTree.getNodeByParam("level",0);
						zTree.expandNode(rootNode,true,false);
					}
			    },
				beforeAsync: function(treeId, treeNode) {
			    	zTree.setting.asyncUrl="<%=path%>/bir_birthdaySendEMP.htm?method=<%=treemethod%>&depId="+treeNode.id+"&lguserid="+$(window.parent.parent.document).find("#taskOwnerId").val()+"&lgcorpcode="+$(window.parent.parent.document).find("#lgcorpcode").val();
				}
			}
		};

	var colorR = 16;
	var colorG = 80;
	var colorB = 120;
	function setFont(treeId, treeNode) {
		if (treeNode && treeNode.isBind==true) {
			var css = {color: "#808080"};
			return css;
		} else {
			return {};
		}
	}
	//????????????
	function zTreeOnClick(event, treeId, treeNode) {
		$(window.parent.document).find("#pageIndex1").val("1");
		$(window.parent.document).find("#left").empty();
		depId=treeNode.id;
        $(window.parent.document).find("#depId").val(depId);	
        $(window.parent.document).find("#depName").val(treeNode.name); 
        $(window.parent.document).find("#addrName").html(treeNode.name.length>10?treeNode.name.substr(0,10)+"...":treeNode.name);
        var addTypes = $(window.parent.document).find("#addType").val();
        var lgcorpcode = $(window.parent.parent.document).find("#lgcorpcode").val();
        var lguserid = $(window.parent.parent.document).find("#lguserid").val();
		$.post("<%=path %>/<%=url%>", {method:"getDepAndEmpTree1", lguserid:lguserid,lgcorpcode:lgcorpcode,depId:depId,addTypes:addTypes}, function(result){
			//?????????@?????????????????????
		   	var index = result.indexOf("@",result.indexOf("@")+1);
			var tempStr = result.substring(0,index);
			var strs =new Array();
			strs =  tempStr.split("@");
			$(window.parent.document).find("#totalRec1").html("("+strs[0]+"???)");
			//var pageCount = Math.ceil(parseInt(strs[0])/50);
			$(window.parent.document).find("#totalPage1").val(strs[1]);
			$(window.parent.document).find("#showPage1").html($(window.parent.document).find("#pageIndex1").val()+"/"+$(window.parent.document).find("#totalPage1").val());
			var tempStr2 = result.substring(index+1);
			$(window.parent.document).find("#left").html(tempStr2);
		});
	}
	
	
	function zTreeBeforeAsync(treeId, treeNode) {
	    if (treeNode.id == 1) return false;
	    return true;
	}
	
	var zNodes =[];

    $(document).ready(function(){
        setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
        zTree = $("#tree").zTree(setting, zNodes);
    });

	function returnZTree() {
		return zTree;
	}

  </script>


  </head>
  <body style="margin:0;padding:0;">
  		<label id="test"></label>
	    <ul id="tree" class="tree" style="width:230px;"></ul>
  </body>
</html>
