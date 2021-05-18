<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%
    String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String lguserid = request.getParameter("lguserid");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  		
<title></title>
  <link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=skin %>/newjqueryui.css" type="text/css" >
<%if(StaticValue.ZH_HK.equals(langName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
<%}%>
<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  <script type="text/javascript">
	var zTree;
    var zTree1;	
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
			asyncUrl: "<%=path%>/grp_cliGroupManage.htm?method=getClientSecondDepJson&lguserid=<%=lguserid%>",  //获取节点数据的URL地址
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
					if(!treeNode.id){
						return false;
					}
					var url = "<%=path%>/grp_cliGroupManage.htm?method=getClientSecondDepJson&depId="+treeNode.id+"&lguserid=<%=lguserid%>";
					EmpExecutionContext.log(url);
			    	zTree.setting.asyncUrl=url;
				}
			}
		};

	//点击事件
	function zTreeOnClick(event, treeId, treeNode) {
		$(window.parent.document).find("#epname").val("");
		var ipathh = "<%=path %>";
		$(window.parent.document).find("#left").empty();
		depId=treeNode.id;
        $(window.parent.document).find("#depId").val(depId);	
        //$(window.parent.document).find("#depName").val(treeNode.name);
        var name = treeNode.name;
        var epname = $(window.parent.document).find("#epname").val();
		var epno = $(window.parent.document).find("#epno").val(); 
        var addTypes = $(window.parent.document).find("#addType").val();
        var lgcorpcode = $(window.parent.parent.document).find("#lgcorpcode").val();
        var pageIndex = $(window.parent.document).find("#pageIndexAdd").val();
        
		$(window.parent.document).find("#left").empty();
		$(window.parent.document).find("#pagecode").empty();
		$(window.parent.document).find("#prepage").css("visibility","hidden");
		$(window.parent.document).find("#nextpage").css("visibility","hidden");
		$(window.parent.document).find("#prepage").attr("disabled",true);
		$(window.parent.document).find("#nextpage").attr("disabled",false);
		var pageIndex = 1; 
		$(window.parent.document).find("#pageIndexAdd").val(pageIndex);
        
		$.post(ipathh+"/grp_cliGroupManage.htm", 
			{
				method:"getDepAndEmpTree1", 
				lgcorpcode:lgcorpcode,
				//epname : epname,
				epno : epno,
				depId:depId,
				pageIndex:pageIndex,
				addTypes:addTypes
			}, function(result){
					$(window.parent.document).find("#pagecode").empty();
					if(result != "" ){
						$(window.parent.document).find("#prepage").css("visibility","visible");
						$(window.parent.document).find("#nextpage").css("visibility","visible");
						<%-- 获取分页信息 --%>
						var arr = result.substring(0,result.indexOf("#")).split(",");
						<%--总页数 --%>
						var pageTotal = parseInt(arr[0]);
						<%--总记录数 --%>
						var pageRec = arr[1];
						<%-- 添加记录 --%>
						$(window.parent.document).find("#left").html(result.substring(result.indexOf("#")+1));
						<%-- 如果是只有一页记录的话--%>
						$(window.parent.document).find("#pagecode").html(pageIndex+"/"+pageTotal);
						<%-- 当总页数等于当前页时 ，下一页置灰--%>
						if(pageTotal == 1){
						    $(window.parent.document).find("#prepage").css("visibility","hidden");
							$(window.parent.document).find("#nextpage").css("visibility","hidden");
							return;
						}
					}
		});
	}
	
	
	function zTreeBeforeAsync(treeId, treeNode) {
	    if (treeNode.id == 1) return false;
	    return true;
	}
	
	var zNodes =[];

	$(document).ready(function(){
		zTree = $("#tree").zTree(setting, zNodes);	
		setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
	});

	function returnZTree()
	{
		return zTree;
	}

  </script>


  </head>
  <body style="margin:0;padding:0;">
  		<label id="test"></label>
	    <ul id="tree" class="tree" style="width:230px;"></ul>
  </body>
</html>
