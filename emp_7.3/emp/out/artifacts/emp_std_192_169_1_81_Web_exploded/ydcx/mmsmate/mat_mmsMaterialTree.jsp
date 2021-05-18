<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String inheritPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	String lgcorpcode = request.getParameter("lgcorpcode");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<HTML>
	<HEAD>
		<%@include file="/common/common.jsp" %>
		<TITLE></TITLE>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeMaterial.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		
		<style>
		.tree li button.ico_open{ background:url(<%=commonPath %>/common/widget/zTreeStyle/img/sorts.gif) no-repeat;}
        .tree li button.ico_close{ background:url(<%=commonPath %>/common/widget/zTreeStyle/img/sorts.gif) no-repeat;}
		</style>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<SCRIPT LANGUAGE="JavaScript">
		
			var zTree;
			var setting;
			var rMenu = document.getElementById("rMenu");
				setting = {
					async : true,
					asyncUrl :"<%=request.getContextPath()%>/mat_mmsMaterial.htm?method=createTree&lgcorpcode=<%=lgcorpcode%>", //获取节点数据的URL地址
					isSimpleData : true,
					keepParent: true,
 					keepLeaf: true,
					dragCopy: true,
					dragMove: true,
				//	addDiyDom:addDom,
					isSimpleData : true,
					rootPID : 0,
					treeNodeKey : "id",
					treeNodeParentKey : "pId",
					callback: {
						
						beforeAsync: zTreeBeforeAsync,
						asyncSuccess:zTreeOnAsyncSuccess,
 						//confirmRename: zTreeConfirmRename,
 						beforeRemove: zTreeBeforeDel,
 						click:getMaterialSortInfo,
 						beforeExpand:function(nodeId, treeNode){
							if(!treeNode.nodes){
								return false;
							}
						}
 						
 						
 						
					}
				};
				function addDom(treeId, treeNode) {
					var add = "";
					var del = "";
					var edit = "";
			        //add= "<button type='button' class='diyBtn1' id='add_" +treeNode.id+ "' title='新增' onfocus='this.blur();'></button>&nbsp;";
			        add= "<button type='button' class='diyBtn1' id='add_" +treeNode.id+ "' title='"+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_xz")+"' onfocus='this.blur();'></button>&nbsp;";
                    if(treeNode.pId != 0)
                    {
			       		 //del="<button type='button' class='diyBtn2' id='del_" +treeNode.id+ "' title='删除' onfocus='this.blur();'></button>";
			       		 del="<button type='button' class='diyBtn2' id='del_" +treeNode.id+ "' title='"+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sc")+"' onfocus='this.blur();'></button>";
                    }
                    //edit="<button type='button' class='diyBtn3' id='edit_" +treeNode.id+ "' title='编辑' onfocus='this.blur();'></button>&nbsp;";
                    edit="<button type='button' class='diyBtn3' id='edit_" +treeNode.id+ "' title='"+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_bj")+"' onfocus='this.blur();'></button>&nbsp;";
					var editStr = "<span id='hrefDiv'>&nbsp;&nbsp;&nbsp;"+add+edit+del+"</span>";
					$("#"+treeNode.tId+"_a").append(editStr);
					var btn = $("#add_"+treeNode.id);
					if (btn) btn.bind("click", function(){zT(treeId, treeNode);doEditSort('add');});
					var btn2 = $("#del_"+treeNode.id);
					if (btn2) btn2.bind("click", function(){zT(treeId, treeNode);menuDelete();});
					var btn3 = $("#edit_"+treeNode.id);
					if (btn3) btn3.bind("click", function(){zT(treeId, treeNode);doEditSort('update');});
				}
			$(document).ready(function(){
				refreshTree();
 				$("body").bind("mousedown", 
				function(event){
					if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
						document.getElementById("rMenu").style.visibility = "hidden";
					}
				});
			});
			 
		  	function getMaterialSortInfo(event,treeId,treeNode)
			{
				hiddenMenu();
			     $("#addrTemp2").val("");
 				 $("#childCode").val(treeNode.id);
				 $("#parentCodetemp").val(treeNode.pId);
				 zT(treeId, treeNode);
				 submitForm();
			}
			function zTreeBeforeAsync(treeId, treeNode) {
			 	 return false;
			}
			function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
 				if(!treeNode){
				   var rootNode = zTree.getNodeByParam("level", 0);
				   zTree.expandNode(rootNode, true, false);
				}
			}
		   
			 
			function zTreeBeforeDel(treeId, treeNode) {
				//if(confirm('是否删除"'+treeNode.name+'"分类？'))
				if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sfsc")+'"'+treeNode.name+'"'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_fl")))
				{
				   $.post("mat_mmsMaterial.htm",
			       {
			       method:"delMaterialSort",
			       childCode:treeNode.id
			       },
			       function(result){
			         if(result =='1')
			         {
			            //alert('删除"'+treeNode.name+'"成功！');
			            alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sc")+'"'+treeNode.name+'"'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cg"));
			         	return;
 			         }else if(result =='-1'){
 			         	//alert('"'+treeNode.name+'"分类存在素材！');
 			         	alert('"'+treeNode.name+'"'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_flczsc"));
 			         	return;
 			         }else
			         {
			         	 //alert('删除"'+treeNode.name+'"失败！');
			         	 alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sc")+'"'+treeNode.name+'"'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_sb"));
			         	 return;
			         }
			       });
			       refreshTree();
			       
				}else
				{
					return false;
				}
			}
			
			function zTreeConfirmRename(treeId, treeNode, newName) {
			    //if(confirm('"'+ treeNode.name +'" 确认更名为"'+newName+'"？'))
			    if(confirm('"'+ treeNode.name +'" '+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_qrgmw")+'"'+newName+'"'+getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_wh")))
			    {
			       $.post("mat_mmsMaterial.htm",
			       {
			       method:"updateSortName",
			       sortName:newName,
			       childCode:treeNode.id
			       },
			       function(result){
			         if(result=='1')
			         {
			         	 //alert("重命名成功！");
			         	 alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cmmcg"));
			         	 refreshTree();
 			         }else
			         {
			         	//alert("重命名失败！");
			         	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_cxsc_cmmsb"));
			         
			         }
			       
			       });
			      	  
			      
			      	  return true;
			    }else
			    {
			    	return true;
			    }
				$("#sortName2").val("");
		   	    $("#childCode2").val(""); 
		   	    $("#parentCode2").val("");
		   	    $("#sortNametemp").val("");
			}
 		
		 
		
			function refreshTree() {
			    $("#sortName2").val("");
		   	    $("#childCode2").val(""); 
		   	    $("#parentCode2").val("");
		   	    $("#sortNametemp").val("");
		   	    $("#addOrRename").val("");
				zTree = $("#tree").zTree(setting, null);
				zTree.expandAll(true);
				zTree.setEditable(false);
				
 		    	zTree.refresh();
			}

			function showMenu(type, x, y) {
			   x = x;
			   y = y;
				$("#rMenu ul").show();
				if (type=="root") {
				}
				$("#rMenu").css({"top":y+"px", "left":x+"px", "visibility":"visible"});
		   }
		   function hiddenMenu()
		   {
		   	  if (rMenu) rMenu.style.visibility = "hidden";
		   }
 			function zT(treeId, treeNode) {
 			
					$("#childCode").val(treeNode.id);
					$("#sortNametemp").val(treeNode.name);
					$("#sortName").val(treeNode.name);
					$("#parentCode").val(treeNode.pId);
					$("#childCode2").val(treeNode.id);
					$("#addrTemp2").val("");
					$("#parentCode2").val(treeNode.pId);
				}
		
	    </SCRIPT>
	</HEAD>
	<BODY>
		<%-- <div id="tree" class="tree" style="width: inherit;"></div>--%>
		<ul id="tree" class="tree" style="width:auto;width: 180px;"></ul>
	 	<div id="rMenu" style="position:absolute; visibility:hidden;">
		<input type="hidden" name="childCode" id="childCode" />
		<input type="hidden" name="sortName" id="sortName"/>
		<input type="hidden" name="parentCode" id="parentCode"/>
				 <ul>
				  <Li   onclick="doEditSort('add')"><emp:message key="ydcx_cxyy_common_btn_4" defVal="新建" fileName="ydcx"></emp:message></Li>
				  <Li   onclick="doEditSort('update')"><emp:message key="ydcx_cxyy_common_text_7" defVal="编辑" fileName="ydcx"></emp:message></Li>
				  
				  <Li   onclick="menuDelete()"   ><emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message> </Li>	  
				 </ul>
 		</div>
	</BODY>
</HTML>
