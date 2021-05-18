<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	LfSysuser sysuser=(LfSysuser)session.getAttribute("loginSysuser");
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<link rel="stylesheet" href="<%=empBasePath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_sysuserTree.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
		<script type="text/javascript" src="<%=empBasePath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		var isDep = "3";
		var isExist = false;
		var zTree;
		var setting;
		var demoIframe;
		var zNodes =[];
		getLoginInfo("#loginUser");
		setting = {
			async : true,
			asyncUrl : "<%=path%>/opt_sysuser.htm?method=createTree1&lguserid="+$("#lguserid").val(), //获取节点数据的URL地址
			isSimpleData : true,
			rootPID : 0,
			treeNodeKey : "id",
			treeNodeParentKey : "pId",
			asyncParam: ["id"],
			callback : {
				asyncSuccess:function(event, treeId, treeNode, msg){
					if(!treeNode){
					   var rootNode = zTree.getNodeByParam("level", 0);
					   zTree.expandNode(rootNode, true, false);
					}
				},
				dblclick: getSysuser
			}
		};
		function getSysuser(event,treeId,treeNode)
		{
			if(treeNode!=null&&treeNode.isParent){
				return;
			}
			var nodeId = (treeNode.id+"").toString().substring(1) ;
			var nodeName = treeNode.name;
			var userId = $("#userId").attr("value");
			$('.selected').removeClass('selected');
			$(this).addClass('selected');
				$("#right").find("option").each(function(){
					if ($(this).val() == nodeId)
					{
						isExist = true;
					}
				});
				if (nodeId == userId)
				{
					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_151"));
				}
				else if (!isExist)
                 {
              	   try
              	   {
              		   $("#right").append("<option value=\'"+nodeId+"\'>"+nodeName.replace("<","&lt;").replace(">","&gt;")+"</option>");
              	   }
                   catch(err)
                   {}
                 }
                 else
                 {
                     alert(nodeName+getJsLocaleMessage("user","user_xtgl_czygl_text_152"));
                 }
			isExist = false;
			$('#selectNodeId').val(nodeId);
			$('#selectNodeName').val(nodeName);
		}
	</script>
	</head>
	<body id="opt_sysuserTree" class="opt_sysuserTree_body">
		<input id="selectNodeId" type="hidden" value=""/>
		<input id="selectNodeName" type="hidden" value=""/>
		<input id="selId" type="hidden" value="<%=sysuser.getUserId() %>"/>
		<input id="selName" type="hidden" value="<%=sysuser.getName() %>"/>
		<div>
		<table border="0">
		<tr><td colspan='4' align="left"><font class="zhu"><emp:message key="user_xtgl_czygl_text_108" 
										defVal="* 从左边的操作员列中双击添加为右边的审批人，并且审批人审批顺序为自上而下，可以自由设定顺序。" fileName="user" /></font></td></tr>
		<tr>
			<td class="czy_td">
				<center>
					<emp:message key="user_xtgl_czygl_text_109" 
										defVal="操作员" fileName="user" />
				</center>
			</td>
			<td class="czy_down_td">
			</td>
			<td class="spr_td">
				<center>
					<emp:message key="user_xtgl_czygl_text_110" 
										defVal="审批人" fileName="user" />
				</center>
			</td>
			<td></td>
		</tr>
		<tr>
			<td>
				<div class="oprTree div_bd">
				<ul id="tdiv" class="tree tdiv"  >
				</ul>
				</div>
			</td>
			<td align="right" valign="top">
			</td>
			<td align="left">
				<select multiple="multiple" name="right" id="right" size="15"
					 onchange="showFlow()" class="div_bd right">
				</select>
				<label><emp:message key="user_xtgl_czygl_text_111" 
										defVal="当前审批人" fileName="user" /></label><emp:message key="user_xtgl_czygl_text_112" 
										defVal="审批流任务：" fileName="user" />
				<textarea id="showFlow"  readonly="readonly" class="div_bd showFlow"></textarea>
			</td>
			<td><input type="button" id="moveUp" value="<emp:message key="user_xtgl_czygl_text_113" 
										defVal="上移↑" fileName="user" />"  class="btnClass1"/>
				<br/>
				<input type="button" id="moveDown" value="<emp:message key="user_xtgl_czygl_text_114" 
										defVal="下移↓" fileName="user" />"  class="btnClass1"/>
				<br/>
				<input type="button" id="moveRight" value="<emp:message key="user_xtgl_czygl_text_115" 
										defVal="移除" fileName="user" />"  class="btnClass1"/>
				<br />
				</td>
		</tr>
		<tr>
		<td colspan="4">
			<center>
			<div class="sureFlow_div">
			<hr/><br/>
			    <input type="button" id="sureFlow" value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />" onclick="javascript:flowOk();" class="btnClass5 mr23"/>
				<input type="button" id="resetFlow" value="<emp:message key="common_btn_16" 
										defVal="取消" fileName="common" />" onclick="javascript:$('#flowDiv').dialog('close');" class="btnClass6" />
			</div>
			</center>
		</td>
		</tr>
		</table>
		</div>
	</body>
</html>