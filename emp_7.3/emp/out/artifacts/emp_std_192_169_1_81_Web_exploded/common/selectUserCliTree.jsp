<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
    String path = request.getContextPath();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  	<%@include file="/common/common.jsp" %>
  	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>选择人员客户机构树</title>
	<link href="<%=path%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" href="<%=path%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	<link rel="stylesheet" href="<%=path%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />

  </head>
  <body class="rms_sameMmsCliTree" style="margin:0;padding:0;">
  		<label id="test"></label>
	    <ul id="tree" class="tree" style="width:200px;"></ul>
	    <script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=path%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=path%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
	<script type="text/javascript">
	var zTree;
	var setting;
	var isHand = "true";
	var treeNode2;
	setting = {
			async: true,
			isSimpleData: true,
			rootPID : 0,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			checkable: false,
			checkRadioType: "level",
			asyncUrl: "<%=path%>/selectUserInfo.htm?method=getClientSecondDepJson",  //获取节点数据的URL地址
			asyncParam: ["name", "id"],  //获取节点数据时，必须的数据名称，例如：id、name
			asyncParamOther: ["test","true"], //其它参数 ( key, value 键值对格式)
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
			    	zTree.setting.asyncUrl="<%=path%>/selectUserInfo.htm?method=getClientSecondDepJson&depId="+treeNode.id;
				}
			}
		};

	//点击事件 		点击上 框的树   显示其机构下的人员
	function zTreeOnClick(event, treeId, treeNode) {
		$(window.parent.document).find("#left").empty();
		$(window.parent.document).find("#prepage").attr("disabled",false);
		$(window.parent.document).find("#nextpage").attr("disabled",false);
		var depId=treeNode.id;
		var depName=treeNode.name;
      	$(window.parent.document).find("#choiceId").val(depId);	
        $(window.parent.document).find("#choiceName").val(treeNode.name);
        //每点机构树的时候，查询的是第一个索引页面的数据 
        var pageIndex = 1; 
        $(window.parent.document).find("#pageIndex").val(pageIndex);
		$.post("<%=path%>/selectUserInfo.htm", {method:"getClientByDepId", depId:depId,pageIndex:pageIndex}, function(result){
			$(window.parent.document).find("#pagecode").empty();
			if(result !== "" ){
				//上一页按钮可见
				$(window.parent.document).find("#prepage").css("visibility","visible");
				//下一页按钮可见
				$(window.parent.document).find("#nextpage").css("visibility","visible");
				//获取分页信息
				var arr = result.substring(0,result.indexOf("#")).split(",");
                //总页数
                var pageTotal = arr[0] === "" || arr[0] === undefined || arr[0] === "null" ? 0 : parseInt(arr[0]);
				//总记录数
                var pageRec = arr[1] === "" || arr[1] === undefined || arr[1] === "null" ? 0 : parseInt(arr[1]);
				//添加记录
                var memberList = eval("("+result.substring(result.indexOf("#")+1)+")");
                for (var i=0;i < memberList.length;i++) {
                    $(window.parent.document).find("#left").append("<div class='memberList' onclick='move2RightByClick(this)' " +
                        "ondblclick='move2Right(this)' dataValue='"+ memberList[i].udgId +"' isdep='"+ memberList[i].isDep +"'" +
                        " et='' mobile='"+ memberList[i].mobile +"'>"+ $.trim(memberList[i].userName) +"</div>");
                }
                //如果总记录数为0则不显示
				if(pageRec !== 0){
                    $(window.parent.document).find("#pagecode").html(pageIndex+"/"+pageTotal);
                    $(window.parent.document).find("#prepage").attr("disabled",true);
                }
                //如果是只有一页记录或者总条数为0的话
				if(pageTotal === 1 || pageRec === 0){
					$(window.parent.document).find("#prepage").css("visibility","hidden");
					$(window.parent.document).find("#nextpage").css("visibility","hidden");
				}
			}else{
				$(window.parent.document).find("#prepage").css("visibility","hidden");
				$(window.parent.document).find("#nextpage").css("visibility","hidden");
			}
		});
		
	}
	
	var zNodes =[];
	$(document).ready(function(){
		setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		zTree = $("#tree").zTree(setting, zNodes);	
	});

	function returnZTree()
	{
		return zTree;
	}

  </script>
  </body>
</html>
