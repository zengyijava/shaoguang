<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String userid2=request.getParameter("userid2");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css" type="text/css"/>
  		
<title></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
<link rel="stylesheet" href="<%=skin %>/newjqueryui.css" type="text/css" >
<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
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
			asyncUrl: "<%=request.getContextPath()%>/wx_send.htm?method=getEmpSecondDepJson&userid=<%=userid2%>",  //获取节点数据的URL地址
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
			    	zTree.setting.asyncUrl="<%=request.getContextPath()%>/wx_send.htm?method=getEmpSecondDepJson&userid=<%=userid2%>&depId="+treeNode.id;
				}
			}
		};

	//点击事件
	function zTreeOnClick(event, treeId, treeNode) {
		var ipathh = "<%=path %>";
		$(window.parent.document).find("#left").empty();
		depId=treeNode.id;
        $(window.parent.document).find("#depId").val(depId);	
        $(window.parent.document).find("#qztype").val("1");
        //$(window.parent.document).find("#depName").val(treeNode.name);
        var name = treeNode.name;
        //var epname = $(window.parent.document).find("#epname").val();
        var epname = "";
		var epno = $(window.parent.document).find("#epno").val(); 
        var addTypes = $(window.parent.document).find("#addType").val();
        var lgcorpcode = $(window.parent.parent.document).find("#lgcorpcode").val();
		$.post(ipathh+"/wx_send.htm", 
			{
				method:"getDepAndEmpTree1", 
				lgcorpcode:lgcorpcode,
				epname : epname,
				epno : epno,
				depId:depId,
				addTypes:addTypes
			}, function(result){
				if(result!=null&&result!=""){
					var tempStr = result.substring(0,result.lastIndexOf("@"));
					var strs =new Array();
					strs =  tempStr.split("@");
					//上一页按钮可见
					$(window.parent.document).find("#prepage").css("visibility","visible");
					//下一页按钮可见
					$(window.parent.document).find("#nextpage").css("visibility","visible");
					$(window.parent.document).find("#pageIndex").val("1");
					$(window.parent.document).find("#totalPage").val(strs[1]);
					$(window.parent.document).find("#pagecode").html($(window.parent.document).find("#pageIndex").val()+"/"+$(window.parent.document).find("#totalPage").val());
					var tempStr2 = result.substring(result.lastIndexOf("@")+1);
					$(window.parent.document).find("#left").html(tempStr2);
					if(tempStr2==""){
						$(window.parent.document).find("#prepage").css("visibility","hidden");
						$(window.parent.document).find("#nextpage").css("visibility","hidden");
						$(window.parent.document).find("#pagecode").html("");
					}
					
					//$(window.parent.document).find("#left").html(result);
					if(name.length>9)
					{
						name = name.substr(0,9)+"...";
					}
				}else{
					$(window.parent.document).find("#prepage").css("visibility","hidden");
					$(window.parent.document).find("#nextpage").css("visibility","hidden");
				}
				/*当前通讯录：*/
			$(window.parent.document).find("#showUserName").html(getJsLocaleMessage("ydwx","ydwx_jtwxfs_63")+name);
		});
	}
	
	//树状节点的判断
	function zTreeBeforeAsync(treeId, treeNode) {
	    if (treeNode.id == 1) return false;
	    return true;
	}
	
	var zNodes =[];
	// 页面初始化
	$(document).ready(function(){
		setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		zTree = $("#tree").zTree(setting, zNodes);	
	});
	//树状结构的节点赋值
	function a(){
		zTree = $("#tree").zTree(setting, zNodes);	
	}
	//返回节点树
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
