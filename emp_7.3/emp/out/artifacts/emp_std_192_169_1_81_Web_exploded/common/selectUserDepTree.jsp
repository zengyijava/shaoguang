<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%
    String path = request.getContextPath();
	//登录id
	String lguserid = request.getParameter("lguserid");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>

<title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <link rel="stylesheet" href="<%=path %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
<link rel="stylesheet" href="<%=path %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link href="<%=path %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
<%if(StaticValue.ZH_HK.equals(langName)){%>
	<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
<%}%>
<script type="text/javascript" src="<%=path %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=path %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=path %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  <script type="text/javascript">
	var zTree;
    var zTree1;
	var setting;
	var isHand = "true";
	var treeNode2;
	var lguserid=<%=lguserid%>;
	setting = {
			async: true,
			isSimpleData: true,
			rootPID : 0,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			checkable: false,
			checkRadioType: "level",
			asyncUrl: "<%=path%>/selectUserInfo.htm?method=getSecondDepJson",
			 //获取节点数据的URL地址
			//asyncParam: ["name", "id"],  //获取节点数据时，必须的数据名称，例如：id、name
			//asyncParamOther: ["test","true"], //其它参数 ( key, value 键值对格式)
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
					zTree.setting.asyncUrl="<%=path%>/selectUserInfo.htm?method=getSecondDepJson&depId="+treeNode.id+
						"&lguserid="+lguserid;
				}
			}
		};

    //点击事件
    function zTreeOnClick(event, treeId, treeNode) {
        var depId = treeNode.id;
        var name = treeNode.name;
        $(window.parent.document).find("#left").empty();
        $(window.parent.document).find("#depId").val(depId);
        $(window.parent.document).find("#choiceId").val(depId);
        $(window.parent.document).find("#choiceName").val(name);
        $.post("<%=path%>/selectUserInfo.htm",
            {
                method:"getDepMemberByDepId",
                depId:depId
            }, function(result){
                var resultHtml ="";
                if(result=='null'){
                    $(window.parent.document).find("#left").html(resultHtml);
                    return;
                }
                var skin = $("#skin").val();
                var obj = eval('(' + result + ')');
                if(skin.indexOf("frame4.0") != -1) {
                    for(var i=0;i<obj.length;i++){
                        resultHtml = resultHtml+"<div class=\"memberList\" onclick=\"move2RightByClick(this)\" ondblclick=\"move2Right(this)\" datavalue=\""+obj[i].userId+"\" isdep=\"11\" et=\"\" mobile=\""+obj[i].mobile+"\">"+obj[i].name+"</div>"
                    }
                }else{
                    for(var i=0;i<obj.length;i++){
                        resultHtml = resultHtml+"<option value='"+obj[i].userId+"' mobile='"+obj[i].mobile+"'>"+obj[i].name+"</option>"
                    }
                }
                $(window.parent.document).find("#left").html(resultHtml);
            });
    }



    function zTreeBeforeAsync(treeId, treeNode) {
	    if (treeNode.id === 1) return false;
	    return true;
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


  </head>
  <body style="margin:0;padding:0;">
  <input id="skin" type="hidden" name="skin" value="<%=skin %>" />
  		<label id="test"></label>
	    <ul id="tree" class="tree" style="width:230px;"></ul>
  </body>
</html>
