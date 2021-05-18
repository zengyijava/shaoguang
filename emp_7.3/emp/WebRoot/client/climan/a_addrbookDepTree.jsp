<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
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
	String treemethod = request.getParameter("treemethod");
	String getType = request.getParameter("getType");
	boolean addcode = new Boolean(request.getParameter("ac"));
	boolean delcode = new Boolean(request.getParameter("dc"));
	String lguserid = request.getParameter("lguserid");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="/common/common.jsp" %>
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<%if(StaticValue.ZH_HK.equals(langName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
	<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/cli_clientAddrBook.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_TongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
  </head>
  <body id="a_addrbookDepTree">
  <div id="addDep">
  <input id="skin" type="hidden" name="skin" value="<%=skin %>" />
  <input type="hidden" id="superiorId" value="" />
			  <table class="client_table1">
			  <tr><td class="client_td1"></td></tr>
			  <tr><td align="right"><emp:message key="client_khtxlgl_kftxl_text_orgname" defVal="机构名称" fileName="client"></emp:message>：</td><td><input type="text" id="depName" maxlength="20" value="" class="w260"/></td></tr>
			  <tr><td class="client_td1"></td></tr>
			  <tr><td><emp:message key="client_khtxlgl_kftxl_text_orgcode" defVal="机构编码" fileName="client"></emp:message>：</td><td><input type="text" id="depcodethird" maxlength="32" value="" class="w260"/></td></tr>
			  <tr><td class="client_td1"></td></tr>
			  <tr><td colspan="2" align="center" class="client_center">&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>"  class="btnClass5" id="depcofim"  onclick="javascript:doOk();"/>
			  <input type="button" value="<emp:message key="client_common_opt_cancel" defVal="取消" fileName="client"></emp:message>"  class="btnClass6"  onclick="javascript:doNo();"/></td></tr>
			  </table>
  </div>
  
    <div id="tmpdiv1">
	<table id="infos" >
				  <tr>
				  		<td class="infostd div_bd"><emp:message key="client_khtxlgl_kftxl_text_nikename" defVal="昵称" fileName="client"></emp:message></td>
						<td class="infostd div_bd"><span id="uname"></span></td>
						<td class="infostd div_bd"><emp:message key="client_khtxlgl_kftxl_text_useraccount" defVal="用户账户" fileName="client"></emp:message></td>
						<td class="infostd div_bd"><span id="app_code"></span></td>

						</tr>
						<tr>
						<td class="infostd div_bd"><emp:message key="client_khtxlgl_kftxl_text_phonenum" defVal="手机号码" fileName="client"></emp:message></td>
						<td class="infostd div_bd"><span id="phonenum"></span></td>
						<td class="infostd div_bd"><emp:message key="client_khtxlgl_kftxl_text_sex" defVal="性别" fileName="client"></emp:message></td>
						<td class="infostd div_bd"><span id="sex"></span></td>
						</tr>
					<tr>
						<td class="infostd div_bd"><emp:message key="client_khtxlgl_kftxl_text_age" defVal="年龄" fileName="client"></emp:message></td>
						<td class="infostd div_bd"><span id="age"></span></td>
						<td class="infostd div_bd"><emp:message key="client_khtxlgl_kftxl_text_regtime" defVal="注册时间" fileName="client"></emp:message></td>
						<td class="infostd div_bd"><span id="verifytime"></span></td>
				  </tr>
			
			


		</table>
  </div>

  <div id="updateDep">
 			 	
				  <table  class="client_table1">
				  <tr><td class="client_td1"></td></tr>
				  <tr>	<td><emp:message key="client_khtxlgl_kftxl_text_orgname" defVal="机构名称" fileName="client"></emp:message>：</td>
				  		<td>
				  			<input type="hidden" id="updateDid" value="" />
				  			<input type="hidden" id="depPid" value="" />
				  			<input type="hidden" id="depOldName" value="" />
					  		<input type="text" id="depNewName" maxlength="20" value="" class="w260"/>
				  		</td>
				  </tr>
				  <tr><td class="client_td1"></td></tr>
				  <tr class="client_tr1"><td><emp:message key="client_khtxlgl_kftxl_text_orgcode" defVal="机构编码" fileName="client"></emp:message>：</td>
				  <td>
				  <label id="depcodethird2"></label>
				  </td>
				  </tr>
				  <tr><td class="client_td1"></td></tr>
				  <tr><td colspan="2" align="center"><input type="button" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>"  class="btnClass5 mr23"  id="depupdate" onclick="javascript:updateDepName();"/>
				  <input type="button" value="<emp:message key="client_common_opt_cancel" defVal="取消" fileName="client"></emp:message>"  class="btnClass6"  onclick="javascript:noUpdate();"/></td></tr>
				  </table>
  </div>
	 <ul id="tree" class="tree"></ul>
	 <script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/client_<%=empLangName%>.js"></script>
	<script type="text/javascript" src="<%=iPath %>/js/addrbook.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/frame/frame4.0/js/dialogui.js"></script>
  <script type="text/javascript">
	var zTree;
	var demoIframe;
	
	var setting;
	var isHand = "true";

	setting = {
		async: true,
		isSimpleData: true,
		asyncUrl: "<%=request.getContextPath()%>/cli_addrbookDepTree.htm?method=<%=treemethod%>&lguserid=<%=lguserid%>",  //获取节点数据的URL地址
		rootPID : -1,
		treeNodeKey: "id",
		//addDiyDom : addDom,
		treeNodeParentKey: "pId",
		asyncParam: ["depId"],
		callback:{
			beforeAsync: function(treeId, treeNode) {
				zTree.setting.asyncUrl="<%=request.getContextPath()%>/cli_addrbookDepTree.htm?method=<%=treemethod%>&lguserid=<%=lguserid%>&depId="+treeNode.id;
			},
			asyncSuccess:function(event, treeId, treeNode, msg){
				if(!treeNode)
				{
					var rootNode = zTree.getNodeByParam("level",0);
					zTree.expandNode(rootNode,true,false);
				}
			},
			click: zTreeOnClick
			
			//asyncSuccess: zTreeOnAsyncSuccess,
			//asyncError: zTreeOnAsyncError
		}
	};

	function addDom(treeId, treeNode) {
		if (isHand == "true")
		{
			var add = "";
			var del = "";
			var edit = "";
			if (<%=addcode%>)
			{
	            add= "<button type='button' class='diyBtn1' id='add_" +treeNode.id+ "' title='" + getJsLocaleMessage('client','client_page_addrbookDepTree_add') + "' onfocus='this.blur();'></button>&nbsp;";
			}
			if (<%=delcode%>&&treeNode.pId!=-1)
			{
	            del="<button type='button' class='diyBtn2' id='del_" +treeNode.id+ "' title='" + getJsLocaleMessage('client','client_common_delete') + "' onfocus='this.blur();'></button>";
	            
			}
			if(<%=delcode%>)
			{
			edit="<button type='button' class='diyBtn3' id='edit_" +treeNode.id+ "' title='" + getJsLocaleMessage('client','client_page_addrbookDepTree_edit') + "' onfocus='this.blur();'></button>&nbsp;";
				}
			var editStr = "<span id='hrefDiv'>&nbsp;&nbsp;&nbsp;"+add+edit+del+"</span>";
			$("#"+treeNode.tId+"_a").append(editStr);
			var btn = $("#add_"+treeNode.id);
			if (btn) btn.bind("click", function(){$("#superiorId").val(treeNode.id);$("#addDep").dialog("open");});
			var btn2 = $("#del_"+treeNode.id);
			if (btn2) btn2.bind("click", function(){doDel(treeNode.id);});
			var btn3 = $("#edit_"+treeNode.id);
			if (btn3) btn3.bind("click", function(){ 
					$("#updateDid").val(treeNode.id);
					$("#depOldName").val(treeNode.name);
					$("#depNewName").val(treeNode.name);
					$("#depcodethird2").html(treeNode.depcodethird=='null'?"":treeNode.depcodethird);
					$("#depPid").val(treeNode.pId);
					$("#updateDep").dialog("open");
			});
		} 
	}

	function addDepFun()
	{
		var oldName =  $("#depOldName").val();
		if(oldName==""||oldName==null)
		{
			//alert("请选择机构！");
			alert(getJsLocaleMessage('client','client_page_addrbookDepTree_selectorg'));
			return;
		}
		$("#addDep").dialog("open");
	}
	function prev(code){
		$.post("cli_addrBook.htm",{
			method:"getMWClient",
			appcode:code 
			
   			},function(result){
   				if(result.indexOf('@@') != -1)
   				{
					var data = result.split("@@");
					$('#app_code').html(data[0]);
					$('#uname').html(data[1]);
					$('#phonenum').html(data[2]);
					$('#sex').html(data[3]);
					$('#age').html(data[4]);
					$('#verifytime').html(data[5]);
					$("#tmpdiv1").dialog("open");
   				}	
			}
		);

	}
	function updateDepFun()
	{
		var oldName =  $("#depOldName").val();
		if(oldName==""||oldName==null)
		{
			//alert("请选择要修改的机构！");
			alert(getJsLocaleMessage('client','client_page_addrbookDepTree_selectorgmodify'));
			return;
		}
		$("#updateDep").dialog("open");
	}
	
	function zTreeOnClick(event, treeId, treeNode) {
		//首先判断 机构是否被删除
		$.post("<%=request.getContextPath()%>/cli_permissions.htm?method=checkdepDel", {
			selDepId:treeNode.id
		}, function(result) {
			if(result != ""){
				//alert("该机构不存在，可能已被删除，不能进行绑定操作！");
				alert(getJsLocaleMessage('client','client_js_permissions_notbindnullorg'));
				window.location.reload();
			}else{
				//-----add------
				$("#superiorId").val(treeNode.id);
				//-----update------
				$("#updateDid").val(treeNode.id);
				$("#depOldName").val(treeNode.name);
				$("#depNewName").val(treeNode.name);
				$("#depcodethird2").html(treeNode.depcodethird=='null'?"":treeNode.depcodethird);
				$("#depPid").val(treeNode.pId);
				//---------delete----------
				 <%if("privi".equals(getType)){//通讯录权限分配时%>
					 $(window.parent.document).find("#depName").text(treeNode.name);
					 $(window.parent.document).find("#depName2").val(treeNode.name);
		 		 	 $(window.parent.document).find("#depId").val(treeNode.id);
		 			 $(window.parent.document).find("#dName").val(treeNode.name);
				 window.parent.submitForm();
				 <%}else if("cust".equals(getType)){%>
					 $("#depId").val(treeNode.id);
					 submitForm();
				 <%}else{%>
				 $("#depId").val(treeNode.id);
				 $("#depName2").val(treeNode.name);
				 submitForm();
				 <%}%>

				}
		});

		 
	}
	
	var zNodes =[];

	$(document).ready(function(){
		setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		$.post("<%=path%>/cli_addrbookDepTree.htm?method=isHandAdd&m=<%=treemethod%>",{},function(result){
             if(result != null && result == "false")
             {
            	isHand = "false";
             }
             zTree = $("#tree").zTree(setting, zNodes);
		});
		
		//var nodes = zTree.getNodes();
	//	zTree.selectNode(nodes[2].nodes[0].nodes[0]);

		demoIframe = $("#testIframe");
		$("#addDep").dialog({
			autoOpen: false,
			height:150,
			title:getJsLocaleMessage('client','client_page_addrbookDepTree_addorg'),//"新增机构",
			width:440,
			modal: true
		});
		resizeDialog($("#addDep"),"ydkfDialogJson","kfdx_kftxlgl_test1");

		$("#tmpdiv1").dialog({
			autoOpen: false,
			height:150,
			title:getJsLocaleMessage('client','client_page_addrbookDepTree_appuserinfo'),//"APP用户信息",
			width:480,
			modal: true
		});
		
		$("#updateDep").dialog({
			autoOpen: false,
			height:150,
			title:getJsLocaleMessage('client','client_page_addrbookDepTree_modifyorgname'),//"修改机构名称",
			width: 440,
			modal: true
		});
		resizeDialog($("#updateDep"),"ydkfDialogJson","kfdx_kftxlgl_test1");
		
			var isIE = false;
			var isFF = false;
			var isSa = false;
			if ((navigator.userAgent.indexOf("MSIE") > 0)
					&& (parseInt(navigator.appVersion) >= 4))
				isIE = true;
			if (navigator.userAgent.indexOf("Firefox") > 0)
				isFF = true;
			if (navigator.userAgent.indexOf("Safari") > 0)
				isSa = true;
			$('#depName,#depNewName').keypress(function(e) {
				var iKeyCode = window.event ? e.keyCode
						: e.which;
				if (iKeyCode == 60 || iKeyCode == 62) {
					if (isIE) {
						event.returnValue = false;
					} else {
						e.preventDefault();
					}
				}
			}
			);
			$('#depName,#depNewName').blur(function(e) {
				$(this).val($(this).val().replaceAll("<","").replaceAll(">",""));
			}
			);
				
		
		
	});
	function loadReady() {
		var h = demoIframe.contents().find("body").height();
		if (h < 600) h = 600;
		demoIframe.height(h);
	}

	function updateDepName()
	{
         var pathUrl = $("#pathUrl").val();
         var depId =  $("#updateDid").val();
         var oldName =  $("#depOldName").val();
         if(depId == -10)
         {
             //alert("'"+oldName+"'机构名称不允许修改！");
             alert("'"+oldName+"'"+getJsLocaleMessage('client','client_page_addrbookDepTree_cannotmodifyorgname'));
             return;        
         }
         var newName =  $("#depNewName").val();
           newName = newName.replace(/\s+/g,"");
         if (newName == "")
         {
             //alert("请输入机构名称！");
             alert(getJsLocaleMessage('client','client_page_addrbookDepTree_enterorgname'));
             return;
         }
         //过滤\导致客户机构组织数无法显示
         newName = newName.replace(/(^\s*)|(\s*$)|\\/g,"");
         if(newName.indexOf("'")!=-1){
          	//alert("名称中不能包含单引号！");
          	alert(getJsLocaleMessage('client','client_page_clientAddrBook_errorname'));
          	return;
          }
         if(oldName == newName){
         	//alert("机构名称没有做修改！");
         	alert(getJsLocaleMessage('client','client_page_addrbookDepTree_nomofifyorgname'));
         	noUpdate();
         	return;
         }
         var type = "1";
          var node = zTree.getSelectedNode().parentNode; 
          var node2 = zTree.getSelectedNode(); 
          if(node != null){
              var deleteZtree2Node = zTree.getNodesByParam("pId",node.id ,node);
			  if(deleteZtree2Node!=null){
				for(var i=0;i<deleteZtree2Node.length;i++){
					if(deleteZtree2Node[i].name == newName){
						//alert("机构名称重复，请检查后再修改！");
						alert(getJsLocaleMessage('client','client_page_addrbookDepTree_reorgname'));
						return;
					}
				}
			 }
          }else{
          	type = "2";
          }

          $("#depupdate").attr("disabled",true);
	     $.post(pathUrl+"/cli_addrbookDepTree.htm?method=updateDepName",
	    	 {
	    	 	depName:newName,
	    	 	depId:depId,
	    	 	type:type
	    	 	},
		     function(resultMsg){
                  if(resultMsg != null && resultMsg == "1")
                  {
                  		//alert("操作成功！");
                  		alert(getJsLocaleMessage('client','client_common_optsuc'));
                  		//修改后将旧名称标识更新
                  		$("#depOldName").val(newName);
                  		var curTreenodes=zTree.getSelectedNode();
                  		curTreenodes.name = newName;
                  		zTree.updateNode(curTreenodes, true);
                  		submitForm();
                        noUpdate();
                  }else if(resultMsg != null && resultMsg == "2"){
                  		//alert("操作失败！");
                  		alert(getJsLocaleMessage('client','client_common_optfalse'));
                  		$("#depupdate").attr("disabled",false);
                        return;
                  }else if(resultMsg != null && resultMsg == "3"){
                  		//alert("不好意思，您所修改的机构名称在同机构下有相同机构名称，请重新输入！");
                  		alert(getJsLocaleMessage('client','client_page_addrbookDepTree_unallowsamename'));
                  		$("#depupdate").attr("disabled",false);
                        return;
                  }else if(resultMsg != null && resultMsg == "0"){
                  		//alert("请检查网络/数据库连接是否正常！");
                  		alert(getJsLocaleMessage('client','client_common_checknet'));
                  		$("#depupdate").attr("disabled",false);
                        return;
                  }else if(resultMsg != null && resultMsg == "4"){
                  		//alert("该机构不存在，可能已被删除！");
                  		alert(getJsLocaleMessage('client','client_page_clientAddrBook_orgisnull'));
                  		window.location.reload();
                  }else{
                  		//alert("网络超时，请重新登录！");
                  		alert(getJsLocaleMessage('client','client_common_nettimeout'));
                         window.location.href=location;
                  }
                  $("#depupdate").attr("disabled",false);
              
             }
         )
	}
	function noUpdate(){
		  //$("#updateDid").attr("value","");
          //$("#depOldName").attr("value","");
          $("#depNewName").val($("#depOldName").val());
          $("#updateDep").dialog("close");
	}

  </script>
  </body>
</html>
