	// 关闭div 事件
	function doCloseDiv(){
		$(window.frames['flowFrame'].document).find("#addUserId").val("");
		$(window.frames['flowFrame'].document).find("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
 		$("#infoDiv").dialog("close"); 
	}
	
	// 新增事件
	function doAddPassUser(){
		var addUserId = $(window.frames['flowFrame'].document).find("#addUserId").val();
		if(addUserId == "" || addUserId.length == 0){
			alert(getJsLocaleMessage("user","user_xtgl_czygl_text_24"));
			return;
		}
		
		$("#sureBtn").attr("disabled","disabled");
		var depId = $("#depId").val();
		var path = $("#pathUrl").val();
			$.post("opt_department.htm",{
				method:"addPassUser",
				addUserId:addUserId,
				depId:depId
 			},function(result){
 				if(result == "success"){
 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_25"));
 					$("#sureBtn").attr("disabled",false);
 					$("#infoDiv").dialog("close"); 	
					window.location.href = path+"/opt_department.htm?method=getPassUser&depId="+depId;
 					return;
 				}else if(result == "fail"){
 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
 					$("#sureBtn").attr("disabled",false);
 					return;
 				}else if(result == "repeat"){
 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_27"));
 					$("#sureBtn").attr("disabled",false);
 					$(window.frames['flowFrame'].document).find("#addUserId").val("");
 					$(window.frames['flowFrame'].document).find("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
 					return;
 				}else{
 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
 					$("#sureBtn").attr("disabled",false);
 					return;
 				}
 			});
	}
 	// 删除密码接收人   
 	function deletePassUser(){
 			var items = "";
			$('input[name="checklist"]:checked').each(function(){	
				items += $(this).val()+",";
			});
			if (items != "")
			{
				items = items.toString().substring(0, items.lastIndexOf(','));
			}else{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_28"));
				return
			}
			var depId = $("#depId").val();
			var path = $("#pathUrl").val();

			var str = getJsLocaleMessage("user","user_xtgl_czygl_text_29");
			if(confirm(str)){
				$.post("opt_department.htm",{
					method:"delPassUser",
					items:items,
					depId:depId
	 			},function(result){
	 				if(result == "success"){
	 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_25"));
						window.location.href = path+"/opt_department.htm?method=getPassUser&depId="+depId;
	 					return;
	 				}else if(result == "fail"){
	 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
	 					return;
	 				}else{
	 					alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
	 					return;
	 				}
	 			});

			}
 	}
	
	//  新增密码接收人   
 	function addPassUser(){
		$(window.frames["flowFrame"].document).find("#showdepName").empty();
		var depName = $("#depName").val().replaceAll("<","&lt;").replaceAll(">","&gt;");
		$(window.frames["flowFrame"].document).find("#showdepName").html(getJsLocaleMessage("user","user_xtgl_czygl_text_30")+depName);
 		$("#infoDiv").dialog("open"); 	
 	}
 	
    //选中
    function checkAlls(e)    
 	{  
 			
 		var a = document.getElementsByName("checklist");    
 		var n = a.length;    
 		for (var i=0; i<n; i=i+1) {
 			a[i].checked =e.checked;   
 		}   
 	}