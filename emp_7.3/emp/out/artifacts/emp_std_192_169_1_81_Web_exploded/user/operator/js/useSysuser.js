function openRoleChoose(){
		//	$("select[name='duty']").hide();
			$("select[name='sex']").hide();
			$("select[name='userState']").hide();
			$("select[name='permissionType']").hide();
		//	$("select[name='postId']").hide();
			$('#roDiv').dialog('open');
		}
		//这里是点击CHECKBOX 是否需要固定尾号
		//isHave  是判断它是否分配尾号   1 是有   2是无
		//oldSubno  分配的尾号
		function updateHaveSubno(isHave,oldSubno){
			 var isneedSubno = "2";		
			 var guid = GlobalVars.lgguid;
		  	//如果点击了选中 
			if(document.getElementsByName('isNeedSubno')[0].checked==true){	
				isneedSubno = "1";
			}

			//这里是判断下  在该操作员有绑定固定尾号情况下，选择是否需要回收还是修改尾号
		 	if(isHave == "1"){
		 		if(isneedSubno == "1"){
		 			$("#updateSubno").val(oldSubno);
		 			$("#updateSubno").css("visibility","visible");
		 		}else if(isneedSubno == "2"){
		 			$("#updateSubno").val("");
		 			$("#updateSubno").css("visibility","hidden");
		 		}
		 		$("#haveSubno").val(isneedSubno);
		 	}else if(isHave == "2"){
		 		//这里是判断下  在该操作员没有绑定固定尾号情况下，选择是否需要绑定尾号
		 		if(isneedSubno == "1"){
		 			$.post("opt_sysuser.htm?method=getSingleSubno",{guid:guid,lgcorpcode:GlobalVars.lgcorpcode},function(msg){
						if("" == msg){
		 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_132"));
		 					document.getElementsByName('isNeedSubno')[0].checked = false;
		 					return;
		 				}else if("notsubno" == msg){
		 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_132"));
		 					document.getElementsByName('isNeedSubno')[0].checked = false;
		 					return;
		 				}else if("enough" == msg){
		 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_133"));
		 					document.getElementsByName('isNeedSubno')[0].checked = false;
		 					return;
		 				}else{
		 					$("#addSubno").val(msg);
							$("#subno2").val(msg);
							$("#addSubno").show();
		 				}
					});
		 		}else if(isneedSubno == "2"){
		 			$.post("opt_sysuser.htm?method=delSingleSubno",{guid:guid,lgcorpcode:GlobalVars.lgcorpcode},function(msg){
						if(msg == "1"){
							$("#addSubno").val("");
							$("#addSubno").hide();
						}else{
							$("#zhu").html(getJsLocaleMessage("user","user_xtgl_czygl_text_134"));
						}
					});
		 		}
		 		$("#haveSubno").val(isneedSubno);
		 	}
}
		
		
		function showRoleName(){
		}
		function hideRoleName(){
			$("#allRoleName").css("display","none");
		}	

		
		
		function changePermType(){
		    var permissionType = $("#permissionType").attr("value");
		    if(permissionType == 1){
		        $("#selectDepBtn").hide();
		        hideMenu2();
		    }else if(permissionType == 2){
		        $("#selectDepBtn").show();
		        showMenu2();
		    }
		}
		
		
		function dorole()
		{
			$("select[name='sex']").show();
			$("select[name='userState']").show();
			$("select[name='permissionType']").show();
		//	$("select[name='postId']").show();
			$('#roDiv').dialog('close'),checkData(1);
			fillRoleName();
		}
		
		function shFlow()
		{
			var departId = $("#depId").attr("value");
			if ($("#depNam").attr("value") == "")
			{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_135"));
				return;
			}
			$("#flowDiv").dialog("open");
		}
		
		
		
		var zTree3;
		var zTree2;
		var setting3;
		var setting2;
		var pathUrl = $("#pathUrl").val();
		setting3 = {
				async : true,
				asyncUrl :pathUrl+"/opt_department.htm?method=createTree2", //获取节点数据的URL地址
				isSimpleData: true,
				rootPID : -1,
				treeNodeKey: "id",
				treeNodeParentKey: "pId",
				asyncParam: ["depId"],
				callback: {
					click: zTreeOnClick3,
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree3.getNodeByParam("level", 0);
						   zTree3.expandNode(rootNode, true, false);
						}
					}
				}
		};
		setting2 = {
				async : true,
				asyncUrl :pathUrl+"/opt_department.htm?method=createTree2", //获取节点数据的URL地址
				isSimpleData: true,
				rootPID : -1,
				treeNodeKey: "id",
				treeNodeParentKey: "pId",
				asyncParam: ["depId"],
				callback: {
					click: zTreeOnClick2,
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree2.getNodeByParam("level", 0);
						   zTree2.expandNode(rootNode, true, false);
						}
					}
				}
		};
		var zNodes3 =[];
		var zNodes2 =[];

		function showMenu() {
			hideMenu2();
			var sortSel = $("#depNam");
			var sortOffset = $("#depNam").offset();
			var disstate = $("#dropMenu").css("display");
			if(disstate == 'block'){
			    $("#permissionType").css("visibility","visible");
				$("#selectDepBtn").css("visibility","visible");
			//	$("#postId").css("visibility","hidden");
				$("#ssdep").css("visibility","visible");
				$("#dropMenu").toggle();
			}else if(disstate == 'none'){
			   $("#permissionType").css("visibility","hidden");
				$("#selectDepBtn").css("visibility","hidden");
				//$("#postId").css("visibility","hidden");
				$("#ssdep").css("visibility","hidden");
				$("#dropMenu").toggle();
			}
		}
		function showMenu2() {
			hideMenu();
			var permissionType = $("#permissionType");
			var sortOffset = permissionType.offset();
		//	$("#postId").css("visibility","hidden");
			$("#selectDepBtn").css("visibility","hidden");
			$("#dropMenu2").css({'top':sortOffset.top+permissionType[0].offsetHeight,'left':sortOffset.left}); 
			$("#dropMenu2").toggle();
		}
		function hideMenu() {
			$("#dropMenu").hide();
		}
		function hideMenu2() {
			$("#dropMenu2").hide();
		}
		
		function zTreeOnClick3(event, treeId, treeNode) {
		    $("#permissionType").css("visibility","visible");
		    $("#selectDepBtn").css("visibility","visible");
		//	$("#postId").css("visibility","visible");
			$("#ssdep").css("visibility","visible");
			if (treeNode) {
				$("#depNam").attr("value", treeNode.name);
				$("#depId").attr("value",treeNode.id);
				//$("#domdepName").attr("value", treeNode.name);
				//$("#domString").attr("value",treeNode.id);
				var domString = $("#domString").val();
				 if(domString !=  treeNode.id){
			  	 	var depNode = zTree2.getNodesByParam("id", treeNode.id)[0];
			  	 	var domNodes = zTree2.getNodesByParam("id", domString,depNode);
			  	 	if(domNodes.length != 1){
			  	 		//alert("管辖范围必须在所属机构范围内,请重新选择");
			  	 		  $("#selectDepBtn").css("visibility","hidden");
				    	$("#permissionType").empty();
				    	$("#permissionType").append("<option value=\'1\'>" + getJsLocaleMessage("user","user_xtgl_czygl_text_139") + "</option><option id='depOption' value='2'>" + getJsLocaleMessage("user","user_xtgl_czygl_text_7") + "</option>");
			  	 	}else{
			  	 		hideMenu2();
			  	 	}
			   }else{
			   			hideMenu2();
			   }
				hideMenu();
			}
		}

		function zTreeOnClick2(event, treeId, treeNode) {
			if (treeNode) {
				var sortObj = $("#domdepName");
				sortObj.attr("value", treeNode.name);
				$("#depOption").attr("text",getJsLocaleMessage("user","user_xtgl_czygl_text_9")+treeNode.name);
				$("#domString").attr("value",treeNode.id);
				   var depId = $("#depId").attr("value");
			   
			   if(depId !=  treeNode.id){
			  	 	var depNode = zTree2.getNodesByParam("id", depId)[0];
			  	 	var domNodes = zTree2.getNodesByParam("id", treeNode.id,depNode);
			  	 	if(domNodes.length != 1){
			  	 		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_136"));
				    	//$("#permissionType").empty();
				    	//$("#permissionType").append("<option value=\'1\'>个人</option><option id='depOption' value='2'>机构</option>");
			  	 	    return ;
			  	 	}else{
			  	 		hideMenu2();
			  	 	}
			   }else{
			   			hideMenu2();
			   }
			}
		//    $("#postId").css("visibility","visible");
	        $("#selectDepBtn").css("visibility","visible");
		}

		function reloadTree() {
			hideMenu();
			hideMenu2();
			var lgcorpcode =GlobalVars.lgcorpcode;
			var lguserid = GlobalVars.lguserid;
			    // var lgguid =$("#lgguid").val();
			var pathUrl = $("#pathUrl").val();
			setting2.asyncUrl = pathUrl+"/opt_department.htm?method=createTree2&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
			setting3.asyncUrl = pathUrl+"/opt_department.htm?method=createTree2&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
			zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
			zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);
		}	
		
		
		
		$(document).ready(function(){	
		    getLoginInfo("#loginUser");	    
			reloadTree();
			setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
			zTree = $("#tdiv").zTree(setting, zNodes);
			zTree.expandAll(true);
			demoIframe = $("#testIframe");
			$('#flowDiv').dialog({
				autoOpen: false,
				height: 523,
				title:getJsLocaleMessage("user","user_xtgl_czygl_text_137"),
				width: 668,
				close:function(){
				//		$("select[name='duty']").show();
						$("select[name='sex']").show();
						$("select[name='userState']").show();
						$("select[name='permissionType']").show();
				//		$("select[name='postId']").show();
				},
				open:function(){
					//	$("select[name='duty']").hide();
						$("select[name='sex']").hide();
						$("select[name='userState']").hide();
						$("select[name='permissionType']").hide();
					//	$("select[name='postId']").hide();
				},
				modal:true
			});
			$('#roDiv').dialog({
				autoOpen: false,
				height: 300,
				title:getJsLocaleMessage("user","user_xtgl_czygl_text_138"),
				width: 320,
				close:function(){
				//		$("select[name='duty']").show();
						$("select[name='sex']").show();
						$("select[name='userState']").show();
						$("select[name='permissionType']").show();
				//		$("select[name='postId']").show();
						fillRoleName();
				},
				modal:true
			});
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
			$('#name').keypress(function(e) {
				var iKeyCode = window.event ? e.keyCode
						: e.which;
				if (iKeyCode == 60 || iKeyCode == 62) {
					if (isIE) {
						event.returnValue = false;
					} else {
						e.preventDefault();
					}
				}
			});
			$('#name').blur(function(e) {
				$(this).val($(this).val().replaceAll("<","").replaceAll(">",""));
			});
			checkDep();
			closeTreeFunSel(["dropMenu2","dropMenu"]);
			closeTreeFunOnSpc(["dropMenu_employ","dropMenu_client","dropMenu_user"]);
		});
		
		
		