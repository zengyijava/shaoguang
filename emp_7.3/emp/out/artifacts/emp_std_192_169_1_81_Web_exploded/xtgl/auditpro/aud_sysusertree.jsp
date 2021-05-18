<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%
    String path = request.getContextPath();
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	//登录id
	String lguserid = request.getParameter("lguserid");
	//判断是哪个审核DIV发出的请求
	String divcount = request.getParameter("divcount");
	//判断是不是回值
	String pathtype  = request.getParameter("pathtype");
	//审核流程的ID
	String flowid = request.getParameter("flowid");
	//第几个审核等级
	String auditlevel = request.getParameter("auditlevel");
	
	//该DIV审批所选择的ID串 
	String labelidstr = request.getParameter("labelidstr");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>

<title></title>
	<%@include file="/common/common.jsp" %>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
<%if(StaticValue.ZH_HK.equals(langName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<%}%>

<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/aud_sysusertree.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<link rel="stylesheet" type="text/css" href="<%=skin %>/aud_sysusertree.css?V=<%=StaticValue.getJspImpVersion() %>"/>

<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  <script type="text/javascript">
	var zTree;
	var setting;
	var isHand = "true";
	var treeNode2;
	var lguserid=<%=lguserid%>;
	setting = {
			async: true,
			checkable : true,
		    checkStyle : "checkbox",
		    checkType : { "Y": "s", "N": "s" },	
			isSimpleData: true,
			rootPID : 0,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			checkRadioType: "level",
			asyncUrl: "<%=request.getContextPath()%>/aud_auditpro.htm?method=createsysdeptree&lguserid="+lguserid+"&pathtype=<%=pathtype%>&auditlevel=<%=auditlevel%>&flowid=<%=flowid%>&labelidstr=<%=labelidstr%>&time="+encodeURI(new Date().getTime()),
			callback:{
                beforeChange:function(treeId,treeNode){
                    if(treeNode.isParent){
                        return false;
                    }
                    // //操作员不是审核员不允许添加(修改：操作员审核无论操作员存在审核权限时都可以进行添加)
                    // if(!treeNode.checked && treeNode.hasReviewer != undefined && treeNode.hasReviewer != 1) {
                    //     alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_72"));
                    //     return false;
                    // }
                },
				change: zTreeOnClick,
				asyncSuccess:function(event, treeId, treeNode, msg)
				{
					if(!treeNode)
					{
						var rootNode = zTree.getNodeByParam("level",0);
						zTree.expandNode(rootNode,true,false);
					}
			    },
				beforeAsync: function(treeId, treeNode) {
					zTree.setting.asyncUrl="<%=request.getContextPath()%>/aud_auditpro.htm?method=createsysdeptree&depId="+treeNode.id+
						"&lguserid="+lguserid+"&pathtype=<%=pathtype%>&auditlevel=<%=auditlevel%>&flowid=<%=flowid%>&labelidstr=<%=labelidstr%>&time="+new Date().getTime();
				}
			}
		};

	//处理 选择的操作员
	function zTreeOnClick(event, treeId, treeNode) {
		if(treeNode.isParent){
			return false;
		}
		//var zTreeuserNodes=zTree.getChangeCheckedNodes();	
		var zTreeuserNodes=zTree.getCheckedNodes(true);	
		var nameid= "";        				
		for(var i=0; i<zTreeuserNodes.length; i++){					
			nameid = nameid + zTreeuserNodes[i].name + "_" + zTreeuserNodes[i].id + "#";
		}	
		var divcount = $("#divcount").val();
        $(window.parent.document).find("#addsysuserstr"+divcount).val("");	
        $(window.parent.document).find("#addsysuserstr"+divcount).val(nameid);
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

	function returnZTree()
	{
		return zTree;
	}

  </script>
  </head>
  <body id="aud_sysusertree" class="aud_sysusertree_body">
  		<input type="hidden" name="divcount" id="divcount" value="<%=divcount %>">
  		<label id="test"></label>
	    <ul id="tree" class="tree tree2" ></ul>
  </body>
</html>
