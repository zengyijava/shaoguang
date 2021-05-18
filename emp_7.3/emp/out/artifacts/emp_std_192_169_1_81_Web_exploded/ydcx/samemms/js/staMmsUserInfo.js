   $(document).ready(function(){
	   			hideBtn();
		    	$("#left").click(function(){
				    	$("#treeFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
			    	});
		    	$("#left").dblclick(function(){
		    		moveIn();
			    	$("#treeFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
		    	});
		    	$("#right").dblclick(function(){
		    		moveOut();
		    	});
		    });
     //这里是选择按钮后的操作    员工    客户  自定义  群组
	 function choiceTree(){
		    var treeType = $("#choose_Type").val();
			var ipath = $("#iPathUrl").val();	
			var path = $("#pathUrl").val();	
			//1代表的是完美通知中的群组   2代表的是彩信发送中的群组
			var grouptype = $("#modulegrouptype").val();
			var lguserid= $(window.parent.document).find("#lguserid").val();
			//企业编码
			var corpCode= $(window.parent.document).find("#lgcorpcode").val();
			//树的DIV
			$("#etree").empty();
			//下放DIV
			$("#left").empty();
			//群组DIV
			$("#egroup").empty();
			//当前选择的名称
			$("#choiceName").val("");
			//当前选择的ID
			$("#choiceId").val("");
			//显示当前选择目录
			//$("#addrName").empty();
			//初始化索引指向第一页
			$("#pageIndex").val(1);
			//隐藏按钮
			hideBtn();
			if(treeType == "1"){	//员工
			//	$("#addrName").html("当前通讯录：");
				$("#egroup").hide();
				$("#etree").show();
				$("#etree").html("<iframe id='sonFrame' style='width: 238px; height:237px;'    src='"+ipath+"/smm_sameMmsEmpTree.jsp?lguserid="+lguserid+"' frameborder='0'></iframe>");
			}else if(treeType == "2"){	//客户
				//$("#addrName").html("当前通讯录：");
				$("#egroup").hide();
				$("#etree").show();
				$("#etree").html("<iframe id='sonFrame' style='width: 238px; height:237px;'   src='"+ipath+"/smm_sameMmsCliTree.jsp?lguserid="+lguserid+"' frameborder='0'></iframe>");
			}else if(treeType == "3"){	//群组
			//	$("#addrName").html("当前群组：");
				$("#etree").hide();
				$("#egroup").show();
				$.post(path+"/smm_sameMms.htm",{
					method:"getStaMMSGroup",
					userId:lguserid,
					grouptype:grouptype
		   			},function(GroupList)
		   			{
						$("#egroup").append(GroupList);
					}
				);
		
			}
		}
			
			
	 //选择左边员工的操作
	 function moveIn(){
		 	if($("#left option:selected").size() == 0){
		 		//alert("请选择成员！");
		 		return;
		 	}
		 	var employeeIds = $("#employeeIds").val();
		 	var clientIds = $("#clientIds").val();
		 	var malistIds = $("#malistIds").val();
			var moblieStrs = $("#moblieStrs").val();
		 	
		 	
		 	var kongMoblieStr = "";
			var havExistStr = "";
			var manCount = parseInt($("#manCount").html());
		 	$("#left option:selected").each(function(){
		 		//成员GUID
		 		var guid = $(this).val();
		 		//这里判断成员类型  是4员工5客户6外部人员 
		 		var isdep = $(this).attr("isdep");
		 		//手机号码
		 		var moblie = $(this).attr("moblie");
		 		//成员名称
		 		var name = $(this).text();
		 		if(isdep == "4"){
		 			//处理员工	
		 			if(employeeIds == "" || employeeIds.length == 0){
		 				if(moblie == "" || moblie.length == 0){
		 					kongMoblieStr = kongMoblieStr + name +",";
		 				}else{
		 					manCount=manCount+1;
			 				$("#right").append("<option value=\'"+guid+"\' isdep='4'  et='' moblie='"+moblie+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_80")+name+"</option>");
			 				$("#getChooseMan").append("<li dataval='"+guid+"' isdep='4' et=''  mobile='"+moblie+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_80")+name+"</li>");
			 				employeeIds = employeeIds+guid+",";
			 				moblieStrs = moblieStrs +moblie+",";
		 				}
		 			}else{
		 				if(employeeIds.indexOf(guid+",") >= 0)
						{
		 					havExistStr = havExistStr+name+"，";
						}else{
							manCount=manCount+1;
							employeeIds = employeeIds+guid+",";
							moblieStrs = moblieStrs +moblie+",";
			 				$("#right").append("<option value=\'"+guid+"\' isdep='4'  et='' moblie='"+moblie+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_80")+name + "</option>");
			 				$("#getChooseMan").append("<li dataval='"+guid+"' isdep='4' et=''  mobile='"+moblie+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_80")+name+"</li>");
						}
		 			}
		 		}else if(isdep == "5"){
		 			//处理客户
		 			if(clientIds == "" || clientIds.length == 0){
		 				if(moblie == "" || moblie.length == 0){
		 					kongMoblieStr = kongMoblieStr + name +",";
		 				}else{
		 					manCount=manCount+1;
			 				$("#right").append("<option value=\'"+guid+"\' isdep='5'  et='' moblie='"+moblie+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_81")+name+"</option>");
			 				$("#getChooseMan").append("<li dataval='"+guid+"' isdep='5' et=''  mobile='"+moblie+"'>"+ getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_81")+name+"</li>");
			 				clientIds = clientIds+guid+",";
			 				moblieStrs = moblieStrs +moblie+",";
		 				}
		 			}else{
		 				if(clientIds.indexOf(guid+",") >= 0)
						{
		 					havExistStr = havExistStr+name+"，";
						}else{
							manCount=manCount+1;
							clientIds = clientIds+guid+",";
							moblieStrs = moblieStrs +moblie+",";
			 				$("#right").append("<option value=\'"+guid+"\' isdep='5'  et='' moblie='"+moblie+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_81")+name+"</option>");
			 				$("#getChooseMan").append("<li dataval='"+guid+"' isdep='5' et=''  mobile='"+moblie+"'>"+ getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_81")+name+"</li>");
						}
		 			}
		 		}else if(isdep == "6"){
		 			//处理外部人员
		 			if(malistIds == "" || malistIds.length == 0){
		 				if(moblie == "" || moblie.length == 0){
		 					kongMoblieStr = kongMoblieStr + name +",";
		 				}else{
		 					manCount=manCount+1;
			 				$("#right").append("<option value=\'"+guid+"\' isdep='6'  et='' moblie='"+moblie+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_82")+name+"</option>");
			 				$("#getChooseMan").append("<li dataval='"+guid+"' isdep='6' et=''  mobile='"+moblie+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_82")+name+"</li>");
			 				malistIds = malistIds+guid+",";
			 				moblieStrs = moblieStrs +moblie+",";
		 				}
		 			}else{
		 				if(malistIds.indexOf(guid+",") >= 0)
						{
		 					havExistStr = havExistStr+name+"，";
						}else{
							manCount=manCount+1;
							malistIds = malistIds+guid+",";
							moblieStrs = moblieStrs +moblie+",";
			 				$("#right").append("<option value=\'"+guid+"\' isdep='6'  et='' moblie='"+moblie+"'>"+ getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_82")+name+"</option>");
			 				$("#getChooseMan").append("<li dataval='"+guid+"' isdep='6' et=''  mobile='"+moblie+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_82")+name+"</li>");
						}
		 			}
		 		}
		 	});
		 	var msg = "";
			if(kongMoblieStr != "" && kongMoblieStr.length>0){
				kongMoblieStr = kongMoblieStr.substring(0,kongMoblieStr.length-1);
				//msg = "以下成员手机号码为空："+kongMoblieStr+"；\n";
			}
			if(havExistStr != "" && havExistStr.length>0){
				havExistStr = havExistStr.substring(0,havExistStr.length-1);
				//msg = msg + "以下成员已经存在："+havExistStr+"；";
				msg = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_83");
			}
			if(msg.length>0){
				alert(msg);
			}
			$("#manCount").html(manCount);
			$("#employeeIds").val(employeeIds);
		 	$("#clientIds").val(clientIds);
		 	$("#malistIds").val(malistIds);
		 	$("#moblieStrs").val(moblieStrs);
	 }
	 
	 
	 //移出右边的单个或者多个选择的对象
	 function moveOut(){
		 	if($("#right option:selected").size() == 0){
		 	//	alert("请选择需要删除的成员！");
		 		return;
		 	}
		 	//员工IDS
		 	var employeeIds = $("#employeeIds").val();
		 	//客户IDS
		 	var clientIds = $("#clientIds").val();
		 	//外部人员IDS
		 	var malistIds = $("#malistIds").val();
		 	//员工机构IDS
			var empDepIdsStrs = $("#empDepIdsStrs").val();
			//客户机构IDS
		 	var cliDepIdsStrs = $("#cliDepIdsStrs").val();
		 	//群组IDS
		 	var groupIdsStrs = $("#groupIdsStrs").val();
		 	//用户的 手机号码集合 
		 	var moblieStrs = $("#moblieStrs").val();
		 	$("#right option:selected").each(function(){
		 		//成员GUID
		 		var id = $(this).val();
		 		//这里判断成员类型  是1员工机构  2客户机构 3群组 4员工5客户6外部人员 
		 		var isdep = $(this).attr("isdep");
		 		var moblie = $(this).attr("moblie");
		 		var name = $(this).text();
		 		//1表示机构包含关系    2表示不包含关系
		 		var et =  $(this).attr("et");
		 		if(isdep == "1"){
		 			//处理员工机构 	
		 			//alert("机构ID1："+empDepIdsStrs);
		 			//alert("类型："+et);
		 			if(et == "2")
					{
						empDepIdsStrs = empDepIdsStrs.replace("e"+id+"," , "");
					}else if(et == "1")
					{
						empDepIdsStrs = empDepIdsStrs.replace(id+"," , "");
					}
		 			$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
		 			//alert("机构ID2："+empDepIdsStrs);
		 		}else if(isdep == "2"){
		 			//处理客户机构
		 			if(et == "2")
					{
		 				cliDepIdsStrs = cliDepIdsStrs.replace("e"+id+"," , "");
					}else if(et == "1")
					{
						cliDepIdsStrs = cliDepIdsStrs.replace(id+"," , "");
					}
		 			$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
		 		}else if(isdep == "3"){
		 			//处理外群组
		 			if(groupIdsStrs.indexOf(id+",") >= 0)
					{
		 				groupIdsStrs = groupIdsStrs.replace(id+"," , "");
		 				$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
					}
		 		}else if(isdep == "4"){
		 			//处理员工	
		 			if(employeeIds.indexOf(id+",") >= 0)
					{
		 				employeeIds = employeeIds.replace(id+"," , "");
		 				moblieStrs = moblieStrs.replace(moblie+"," , "");
		 				$("#manCount").html(parseInt($("#manCount").html())-1);
					}
		 		}else if(isdep == "5"){
		 			//处理客户
		 			if(clientIds.indexOf(id+",") >= 0)
					{
		 				clientIds = clientIds.replace(id+"," , "");
		 				moblieStrs = moblieStrs.replace(moblie+"," , "");
		 				$("#manCount").html(parseInt($("#manCount").html())-1);
					}
		 		}else if(isdep == "6"){
		 			//处理外部人员
		 			if(malistIds.indexOf(id+",") >= 0)
					{
		 				malistIds = malistIds.replace(id+"," , "");
		 				moblieStrs = moblieStrs.replace(moblie+"," , "");
		 				$("#manCount").html(parseInt($("#manCount").html())-1);
					}
		 		}
		 		$(this).remove();
		 		if($('#getChooseMan li').size()>0){
					$('#getChooseMan>li').each(function(){
						if($(this).hasClass('cur')){
							$(this).remove();
						}
					})
				}
		 	});
			$("#employeeIds").val(employeeIds);
		 	$("#clientIds").val(clientIds);
		 	$("#malistIds").val(malistIds);
			$("#empDepIdsStrs").val(empDepIdsStrs);
		 	$("#cliDepIdsStrs").val(cliDepIdsStrs);
		 	$("#groupIdsStrs").val(groupIdsStrs);
		 	$("#moblieStrs").val(moblieStrs);
	 }
	 
			
		
			
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 
		// 隐藏按钮/
		function hideBtn(){
				$("#prepage").css("visibility","hidden");
				$("#nextpage").css("visibility","hidden");
				$("#pagecode").empty();
				$("#userTotal").empty();
		}
		
		// 选择员工  /客户 /群组/
		function choiceBtn(){
			// 将选择按钮置灰/
			$("#selectDep").attr("disabled","disabled");	
			// 选择的类型 /
			//var chooseType = $("input:radio[name='dept']:checked").attr("value");
			var chooseType = $("#choose_Type").val();
			// emp路径/
			var path = $("#pathUrl").val();	
			// 企业编码/
			var corpCode= $(window.parent.document).find("#lgcorpcode").val();
			// 群组ID /	
			var groupIds = $("#groupIdsStrs").val();	
			//已选择人数 /
			var manCount = parseInt($("#manCount").html());
			// 群组 /
			if(chooseType == "3"){
					// 没有群组人员的群组名称 /
					var noBodyGroup = "";
					// 已经选择了的群组名称 /
					var havExistGroup = "";
					var groupSize = $("#groupList option:selected").size();
					if(groupSize == 0){
						//alert("请选择群组！");
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_84"));
						$("#selectDep").attr("disabled","");	
						return;
					}
					$("#groupList option:selected").each(function(){
							// 群组VAL /
						 	var groupVal = $(this).val();
						 	// 群组 名称/
						 	var groupName = $(this).text();
						 	// 群组类型  员工还是客户群组  1 是员工群组  2是客户群组  /
						 	var groupType = $(this).attr("gtype");
						 	// 群组   0个人 1共享/
						 	var groupShare = $(this).attr("sharetype");
						 	// 群组 数量/
						 	var groupCount = $(this).attr("gcount");
						 	if(groupCount == "0"){
						 		noBodyGroup = noBodyGroup+groupName+"，";
						 	}else{
						 		var bool = false;
								if(groupIds!=""&&groupIds.length>0){
						 			var groupIdsArr = groupIds.split(",");
						 			for(var i=0;i<groupIdsArr.length;i++){
						 				if(groupVal==groupIdsArr[i]){
						 					bool = true;
						 					break;
						 				}
						 			}
						 		}

						 		if(bool)
								{
									havExistGroup = havExistGroup+groupName+"，";
								}else{
									groupName = groupName +" ["+ groupCount+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+ "]";
									groupIds = groupIds+groupVal+",";
									$("#right").append("<option value=\'"+groupVal+"\' isdep='3'  et='' moblie='' mcount='"+$(this).attr("gcount")+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_85")+groupName+"</option>");
									$("#getChooseMan").append("<li dataval=\'"+groupVal+"\' isdep='3'  et='' moblie='' mcount='"+$(this).attr("gcount")+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_85")+groupName+"</li>");
									manCount +=parseInt($(this).attr("gcount"));
								}
						 	}
						}
					);
					$("#manCount").html(manCount);
					$("#groupIdsStrs").val(groupIds);
					var msg = "";
					if(noBodyGroup != "" && noBodyGroup.length>0){
						noBodyGroup = noBodyGroup.substring(0,noBodyGroup.length-1);
						//msg = "以下群组没有成员："+noBodyGroup+"；\n";
						msg = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_86");
					}
					if(havExistGroup != "" && havExistGroup.length>0){
						havExistGroup = havExistGroup.substring(0,havExistGroup.length-1);
						//msg = msg + "以下群组已经存在："+havExistGroup+"；";
						msg = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_87");
					}
					if(msg.length>0){
						alert(msg);
					}
					$("#selectDep").attr("disabled","");	
					return;
			}
			// 选择的名称 /
			var choiceName = $("#choiceName").val();
			// 选择的ID /
			var choiceId = $("#choiceId").val();
			// 处理员工/客户机构的选择操作/
			if(choiceId == ""){
				if(chooseType == "1"){
					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_88"));
					$("#selectDep").attr("disabled","");	
					return;
				}else if(chooseType == "2"){
					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_88"));
					$("#selectDep").attr("disabled","");	
					return;
				}
			}
			// 员工机构ID /
			var empDepIds = $("#empDepIdsStrs").val();		
			// 客户机构ID /	
			var cliDepIds = $("#cliDepIdsStrs").val();	


			//提示/
			var viewDepNoBody = $("#viewDepNoBody").val();		
			var viewDepIsExist = $("#viewDepIsExist").val();	
			
			// 获取右边选择好的对象 /			
			var rops = $("#right option");
			if(rops.length>0)
			{
				// 员工机构 /	
				if(chooseType == "1"){
					//处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组/
					for(var i = 0;i<rops.length;i=i+1)
					{
						if(choiceId == rops.eq(i).attr("value") && rops.eq(i).attr("isDep") == 1)
						{
							//alert(rops[i].text + " 记录已添加！");
							alert(viewDepIsExist);
							$("#selectDep").attr("disabled","");	
							return;
						}
					} 
				// 客户机构 /
				}else if(chooseType == "2"){
					for(var i = 0;i<rops.length;i=i+1)
					{
						if(choiceId == rops.eq(i).attr("value") && rops.eq(i).attr("isDep") == 2)
						{
							//alert(rops[i].text + " 记录已添加！");
							alert(viewDepIsExist);
							$("#selectDep").attr("disabled","");	
							return;
						}
					}
				} 
			}
			//处理员工/客户机构是否被包含或者包含子机构/
			if(chooseType == "1"){
				if(empDepIds != "" && empDepIds.length>0){
					//处理员工机构/
					$.post(path+"/smm_sameMms.htm", {method:"isEmpDepContained",depId:choiceId,empDepIds:empDepIds}, function(result)
					{
						if("" == result){
		   					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
		   					$("#selectDep").attr("disabled","");	
		   					return;
		   				}else if(result.indexOf("html") > 0){
							alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_89"));
							$("#selectDep").attr("disabled","");	
			    			window.location.href = window.location.href;
			    		    return;
			    		}
						if(result=="depExist")
						{
							alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_90"));
							$("#selectDep").attr("disabled","");	
							return;
					    }
					    var ismut = 0;
					    if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_91"))){
					    	ismut=1;
					    }
					    //处理该机构包含的子机构 操作, 并将选择好的子机构进行删除操作 /
						$.post(path+"/smm_sameMms.htm", 
					    	{
				    			method : "isDepContaineDeps",
				    			depId : choiceId,
				    			empDepIds : empDepIds,
				    			ismut : ismut
			    			}, function(result2){
				    				if(result2 == "nobody"){
				    					//alert("该员工机构下没有员工！");
				    					alert(viewDepNoBody);
				    					$("#selectDep").attr("disabled","");	
				    					return;
				    				}else if(result2 == "errer"){
				    					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
				    					$("#selectDep").attr("disabled","");	
				    					return;
				    				}
			    			 		//机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组      et 1表示不包含    2表示包含/
	    							if(ismut==0){
				    					$("#empDepIdsStrs").val(empDepIds+choiceId+",");
				    					$("#right").append("<option value=\'"+choiceId+"\' isdep='1'  et='1' moblie='' mcount='"+result2+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+ "]</option>");
				    					$("#getChooseMan").append("<li dataval=\'"+choiceId+"\' isdep='1'  et='1' moblie='' mcount='"+result2+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");
				    					$("#selectDep").attr("disabled","");
				    					manCount+=parseInt(result2);
				    	    		}
				    	    		//机构包含子机构但是右边没有其要删除的子机构的操作 /
									else if(result2.indexOf("notContains")==0)
									{
										$("#empDepIdsStrs").val(empDepIds+"e"+choiceId+",")
										$("#right").append("<option value='"+choiceId+"' isdep='1'  et='2' moblie='' mcount='"+result2.substr(12)+"'>"+ getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+"["+result2.substr(12)+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+ "]</option>");
										$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='1'  et='2' moblie='' mcount='"+result2.substr(12)+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+ "["+result2.substr(12)+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93") + "]</li>");
										$("#selectDep").attr("disabled","");
										manCount+=parseInt(result2.substr(12));	
									}else{
											var strArr = result2.split(",");
											$("#empDepIdsStrs").val(empDepIds+"e"+choiceId+",")
											$("#right").append("<option value='"+choiceId+"' isdep='1'  et='2' moblie='' mcount='"+strArr[0]+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+"["+strArr[0]+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
											$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='1'  et='2' moblie='' mcount='"+strArr[0]+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+"["+strArr[0]+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+ "</li>");
											$("#selectDep").attr("disabled","");
											manCount+=parseInt(strArr[0]);	
											for(var i=1;i<strArr.length;i=i+1)
											{
												var containId = $.trim(strArr[i]);
												var $aaa = $("#right").find("option[isdep=1][value='"+containId+"']");
												var $bbb=$("#getChooseMan").find("li[isdep=1][dataval='"+containId+"']");
												// et1表示包含子机构关系,2表示不包含子机构/
												var depidstr = $("#empDepIdsStrs").val();
												if($aaa.attr("et")==2)
												{
													$("#empDepIdsStrs").val(depidstr.replace("e"+$aaa.val()+",",""));
												}else if($aaa.attr("et")==1)
												{
													$("#empDepIdsStrs").val(depidstr.replace($aaa.val()+",",""));
												}
												manCount=manCount-$aaa.attr("mcount");
												$aaa.remove();
												$bbb.remove();
											}
							    	}	
							    	$("#manCount").html(manCount);
					    	});
					});
				}else{
					//直接添加员工机构/
			  		var ismut = 0;
				    if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_91"))){
				    	ismut=1;
				    }
			    	$.post(path+"/smm_sameMms.htm",{
		    			method : "getEmpDepCount",
		    			depId : choiceId,
		    			ismut : ismut
	    			}, function(result2){
	    				if(result2 == "nobody" || result2 == "0"){
	    					//alert("该员工机构下没有员工！");
	    					alert(viewDepNoBody);
	    					$("#selectDep").attr("disabled","");	
	    					return;
	    				}else if(result2 == "errer"){
	    					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
	    					$("#selectDep").attr("disabled","");	
	    					return;
	    				}
    					//机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组   4代表员工 5代表客户 6代表外部人员   et 1表示不包含    2表示包含/
	 					if(ismut==0){
	    					$("#empDepIdsStrs").val(choiceId+",");
	    					//$("#right").append("<option value='"+choiceId+"' isdep='1'  et='1' moblie='' mcount='"+result2+"'>[员工机构] "+choiceName+" ["+result2+"人]</option>");
	    					//$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='1'  et='1' moblie='' mcount='"+result2+"'>[员工机构] "+choiceName+" ["+result2+"人]</li>");

	    					$("#right").append("<option value='"+choiceId+"' isdep='1'  et='1' moblie='' mcount='"+result2+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+" "+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
	    					$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='1'  et='1' moblie='' mcount='"+result2+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+" "+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");
	    					
	    					$("#selectDep").attr("disabled","");	
	    					manCount+=parseInt(result2);
	    	    		}else{
	    	    			var strArr = result2.split(",");
							$("#empDepIdsStrs").val("e"+choiceId+",")
							//$("#right").append("<option value='"+choiceId+"' isdep='1'  et='2' moblie=''  mcount='"+strArr[0]+"'>[员工机构] "+choiceName+"(包含子机构)["+result2+"人]</option>");
							//$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='1'  et='2' moblie=''  mcount='"+strArr[0]+"'>[员工机构] "+choiceName+"(包含子机构)["+result2+"人]</li>");

							$("#right").append("<option value='"+choiceId+"' isdep='1'  et='2' moblie=''  mcount='"+strArr[0]+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
							$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='1'  et='2' moblie=''  mcount='"+strArr[0]+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");
							
							$("#selectDep").attr("disabled","");	
							manCount+=parseInt(strArr[0]);	
						}
						$("#manCount").html(manCount);
	    			});
				}
			}else if(chooseType == "2"){	
				//等下处理
				//处理客户机构/
				if(cliDepIds != "" && cliDepIds.length>0){
					//处理客户机构/
					$.post(path+"/smm_sameMms.htm", {method:"isClientDepContained",depId:choiceId,cliDepIds:cliDepIds}, function(result)
					{
						if("" == result){
		   					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
		   					$("#selectDep").attr("disabled","");	
		   					return;
		   				}else if(result.indexOf("html") > 0){
							alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_89"));
							$("#selectDep").attr("disabled","");	
			    			window.location.href = window.location.href;
			    		    return;
			    		}
						if(result=="depExist")
						{
							alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_90"));
							$("#selectDep").attr("disabled","");	
							return;
					    }
					    var ismut = 0;
					    if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_91"))){
					    	ismut=1;
					    }
					    //处理该机构包含的子机构 操作, 并将选择好的子机构进行删除操作 /
						$.post(path+"/smm_sameMms.htm", 
					    	{
				    			method : "isClientDepContaineDeps",
				    			depId : choiceId,
				    			cliDepIds : cliDepIds,
				    			ismut : ismut
			    			}, function(result2){
				    				if(result2 == "nobody"){
				    					//alert("该员工机构下没有客户！");
				    					alert(viewDepNoBody);
				    					$("#selectDep").attr("disabled","");	
				    					return;
				    				}else if(result2 == "errer"){
				    					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
				    					$("#selectDep").attr("disabled","");	
				    					return;
				    				}
			    			 		//机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组      et 1表示不包含    2表示包含/
	    							if(ismut==0){
				    					$("#cliDepIdsStrs").val(cliDepIds+choiceId+",");
				    					//$("#right").append("<option value=\'"+choiceId+"\' isdep='2'  et='1' moblie='' mcount='"+result2+"'>[客户机构] "+choiceName+" ["+result2+"人]</option>");
				    					//$("#getChooseMan").append("<li dataval=\'"+choiceId+"\' isdep='2'  et='1' moblie='' mcount='"+result2+"'>[客户机构] "+choiceName+" ["+result2+"人]</li>");
				    					
				    					$("#right").append("<option value=\'"+choiceId+"\' isdep='2'  et='1' moblie='' mcount='"+result2+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
				    					$("#getChooseMan").append("<li dataval=\'"+choiceId+"\' isdep='2'  et='1' moblie='' mcount='"+result2+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");
				    					$("#selectDep").attr("disabled","");	
				    					manCount+=parseInt(result2);
				    	    		}
				    	    		//机构包含子机构但是右边没有其要删除的子机构的操作 /
									else if(result2.indexOf("notContains")==0)
									{
										$("#cliDepIdsStrs").val(cliDepIds+"e"+choiceId+",")
										//$("#right").append("<option value='"+choiceId+"' isdep='2'  et='2' moblie='' mcount='"+result2.substr(12)+"'>[客户机构] "+choiceName+"(包含子机构)["+result2.substr(12)+"人]</option>");
										//$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='2'  et='2' moblie='' mcount='"+result2.substr(12)+"'>[客户机构] "+choiceName+"(包含子机构)["+result2.substr(12)+"人]</li>");

										$("#right").append("<option value='"+choiceId+"' isdep='2'  et='2' moblie='' mcount='"+result2.substr(12)+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+result2.substr(12)+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
										$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='2'  et='2' moblie='' mcount='"+result2.substr(12)+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+result2.substr(12)+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");
										$("#selectDep").attr("disabled","");	
										manCount+=parseInt(result2.substr(12));	
									}else{
											var strArr = result2.split(",");
											$("#cliDepIdsStrs").val(cliDepIds+"e"+choiceId+",")
											$("#right").append("<option value='"+choiceId+"' isdep='2'  et='2' moblie='' mcount='"+strArr[0]+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+strArr[0]+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
											$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='2'  et='2' moblie='' mcount='"+strArr[0]+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+strArr[0]+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");
											$("#selectDep").attr("disabled","");	
											manCount+=parseInt(strArr[0]);
											for(var i=1;i<strArr.length;i=i+1)
											{
												var containId = $.trim(strArr[i]);
												var $aaa = $("#right").find("option[isdep=2][value='"+containId+"']");
												var $bbb = $("#getChooseMan").find("li[isdep=2][dataval='"+containId+"']");
												// et1表示包含子机构关系,2表示不包含子机构/
												var depidstr = $("#cliDepIdsStrs").val();
												if($aaa.attr("et")==2)
												{
													$("#cliDepIdsStrs").val(depidstr.replace("e"+$aaa.val()+",",""));
												}else if($aaa.attr("et")==1)
												{
													$("#cliDepIdsStrs").val(depidstr.replace($aaa.val()+",",""));
												}
												manCount=manCount-$aaa.attr("mcount");
												$aaa.remove();
												$bbb.remove();
											}
							    	}	
							    	$("#manCount").html(manCount);
					    	});
					});
				}else{
					//直接添加客户机构/
			  		var ismut = 0;
				    if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_91"))){
				    	ismut=1;
				    }
			    	$.post(path+"/smm_sameMms.htm",{
		    			method : "getClientDepCount",
		    			depId : choiceId,
		    			ismut : ismut
	    			}, function(result2){
	    				if(result2 == "nobody" || result2 == "0"){
	    				//	alert("该客户机构下没有客户！");
	    					alert(viewDepNoBody);
	    					$("#selectDep").attr("disabled","");	
	    					return;
	    				}else if(result2 == "errer"){
	    					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
	    					$("#selectDep").attr("disabled","");	
	    					return;
	    				}
    					//机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组      et 1表示不包含    2表示包含/
	 					if(ismut==0){
	    					$("#cliDepIdsStrs").val(choiceId+",");
	    					$("#right").append("<option value='"+choiceId+"' isdep='2'  et='1' moblie=''  mcount='"+result2+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95") + choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93") + "]</option>");
	    					$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='2'  et='1' moblie=''  mcount='"+result2+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+ "]</li>");
	    					$("#selectDep").attr("disabled","");	
				    		manCount+=parseInt(result2);
	    	    		}else{
	    	    			var strArr = result2.split(",");
							$("#cliDepIdsStrs").val("e"+choiceId+",")
							$("#right").append("<option value='"+choiceId+"' isdep='2'  et='2' moblie=''  mcount='"+strArr[0]+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+ "["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93") + "]</option>");
							$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='2'  et='2' moblie=''  mcount='"+strArr[0]+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+ "["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93") + "]</li>");
							$("#selectDep").attr("disabled","");
							manCount+=parseInt(strArr[0]);	
						}
						$("#manCount").html(manCount);
	    			});
				
				}
	
			}
			
		}
		// 处理按纽上一页  /
		function prePage(){
			var path = $("#pathUrl").val();	
			var pageIndex =  parseInt($("#pageIndex").val());
			$("#nextpage").attr("disabled",false);
			// 选择的类型 /
			//var chooseType = $("input:radio[name='dept']:checked").attr("value");
			var chooseType = $("#choose_Type").val();
			var choiceId = $("#choiceId").val();
			var name = $("#choiceName").val();
			pageIndex = pageIndex - 1;
			$("#pageIndex").val(pageIndex);
			$("#left").empty();
			// 方法名  /
			var methodName ;
			//用户 查询群组的时候 表识 1 员工群组  2客户群组  /
			var type = 1;
			if(chooseType == "1"){
				methodName = "getEmployeeByDepId";
			}else if(chooseType == "2"){
				methodName = "getClientByDepId";
			}else if(chooseType == "3"){
				methodName = "getGroupUserByGroupId";
				type = $("#qztype").val();
			}
			// 处理员工/群组的上一页的操作 /
				//处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组/
				$.post(path+"/smm_sameMms.htm", {method:methodName, depId:choiceId,pageIndex:pageIndex,type:type}, function(result){
								$("#pagecode").empty();
								if(result != "" ){
									// 获取分页信息 /
									var arr = result.substring(0,result.indexOf("#")).split(",");
									//总页数 /
									var pageTotal = parseInt(arr[0]);
									//总记录数 /
									var pageRec = arr[1];
									// 添加记录 /
									$("#left").html(result.substring(result.indexOf("#")+1));
									// 显示当前机构 /
								//	var tempName = name;
								//	if(tempName.length>9){
								//		tempName = tempName.substr(0,9)+"...";
								//	}
								//	$("#addrName").html("当前机构："+tempName);
									// 显示该机构总人数 /
							//		$("#userTotal").html("["+pageRec+"人]");
									// 如果是只有一页记录的话/
									$("#pagecode").html(pageIndex+"/"+pageTotal);
									// 当总页数等于当前页时 ，下一页置灰/
									if(1 == pageIndex){
										$("#prepage").attr("disabled",true);
										return;
									}
								}
				});
		}
		// 处理按纽 下一页 /
		function nextPage(){
			var path = $("#pathUrl").val();	
			var pageIndex =  parseInt($("#pageIndex").val());
			$("#prepage").attr("disabled",false);
			// 选择的类型 /
			//var chooseType = $("input:radio[name='dept']:checked").attr("value");
			var chooseType = $("#choose_Type").val();
			var choiceId = $("#choiceId").val();
			var name = $("#choiceName").val();
			pageIndex = pageIndex + 1;
			$("#pageIndex").val(pageIndex);
			$("#left").empty();
			// 方法名  /
			var methodName ;
			//用户 查询群组的时候 表识 1 员工群组  2客户群组  /
			var type = 1;
			if(chooseType == "1"){
				methodName = "getEmployeeByDepId";
			}else if(chooseType == "2"){
				methodName = "getClientByDepId";
			}else if(chooseType == "3"){
				methodName = "getGroupUserByGroupId";
				type = $("#qztype").val();
			}
				//处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组/
				$.post(path+"/smm_sameMms.htm", {method:methodName, depId:choiceId,pageIndex:pageIndex,type:type}, function(result){
								$("#pagecode").empty();
								if(result != "" ){
									// 获取分页信息 /
									var arr = result.substring(0,result.indexOf("#")).split(",");
									//总页数 /
									var pageTotal = parseInt(arr[0]);
									//总记录数 /
									var pageRec = arr[1];
									// 添加记录 /
									$("#left").html(result.substring(result.indexOf("#")+1));
									// 显示当前机构 /
								//	var tempName = name;
								//	if(tempName.length>9){
								//		tempName = tempName.substr(0,9)+"...";
								//	}
								//	$("#addrName").html("当前机构："+tempName);
									// 显示该机构总人数 /
								//	$("#userTotal").html("["+pageRec+"人]");
									// 如果是只有一页记录的话/
									$("#pagecode").html(pageIndex+"/"+pageTotal);
									// 当总页数等于当前页时 ，下一页置灰/
									if(pageTotal == pageIndex){
										$("#nextpage").attr("disabled",true);
										return;
									}
								}
				});
		}
		
		
		
		function a(){
			var path = $("#pathUrl").val();	
			if ($("#groupList option:selected").size() == 1) {
				$("#groupList option:selected").each(function() {
						$("#left").empty();
						$("#prepage").attr("disabled",false);
						$("#nextpage").attr("disabled",false);
				        var pageIndex = 1; 
				        $("#pageIndex").val(pageIndex);
						// 当前群组ID /
						var groupVal = $(this).val();	
						var groupName = $(this).text();	
						groupName = groupName.substring(0,groupName.lastIndexOf("["));
						// 当前群组类型  1是员工群组  2是客户群组/		
						var grouptype = $(this).attr("gtype");	
						$("#qztype").val(grouptype);
						var gcount = parseInt($(this).attr("gcount"));	
						$("#choiceId").val(groupVal);	
				        $("#choiceName").val(groupName);
				        $("#pagecode").empty();
					    if(gcount == 0){
					    	//	var tempName = groupName;
							//	if(tempName.length>9){
							//		tempName = tempName.substr(0,9)+"...";
							//	}
					    	//	$("#addrName").html("当前群组："+tempName);
							//	$("#userTotal").html("[0人]");
								$("#prepage").css("visibility","hidden");
								$("#nextpage").css("visibility","hidden");
								return;
					    }
					    $("#prepage").css("visibility","visible");
						$("#nextpage").css("visibility","visible");
						
						$.post(path+"/smm_sameMms.htm", {method:"getGroupUserByGroupId",depId:groupVal,pageIndex:pageIndex,type:grouptype}, function(result){
							if(result != "" ){
								// 获取分页信息 /
								var arr = result.substring(0,result.indexOf("#")).split(",");
								//总页数 /
								var pageTotal = parseInt(arr[0]);
								//总记录数 /
								var pageRec = arr[1];
								// 添加记录 /
								$("#left").html(result.substring(result.indexOf("#")+1));
								// 显示当前机构 /
							//	var tempName = groupName;
							//	if(tempName.length>9){
							//		tempName = tempName.substr(0,9)+"...";
							//	}
							//	$("#addrName").html("当前群组："+tempName);
								// 显示该机构总人数 /
							//	$("#userTotal").html("["+pageRec+"人]");
								// 如果是只有一页记录的话/
								$("#pagecode").html(pageIndex+"/"+pageTotal);
								$("#prepage").attr("disabled",true);
								if(pageTotal == 1){
									$("#prepage").css("visibility","hidden");
									$("#nextpage").css("visibility","hidden");
									return;
								}
							}
						});
							
				});
			}
		
		}

		function treeLoseFocus()
		{
			//window.frames['sonFrame'].returnZTree().cancelSelectedNode();
			$("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
			$("#groupList").val("");
		}

		function router()
		{
			if($("#left").val()!=null && $("#left").val()!="")
			{
				moveIn();
			}else{
				choiceBtn();
			}
		}
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
			
			
			
			
			
		
