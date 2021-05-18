		var zTree1;
		var setting;
		var pathUrl = $("#pathUrl").val();
		setting = {
				async : true,
				asyncUrl :pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson", //获取节点数据的URL地址
				isSimpleData: true,
				rootPID : -1,
				treeNodeKey: "id",
				treeNodeParentKey: "pId",
				callback: {
					//beforeExpand: function(){},
					//beforeCollapse: function(){},
					click: zTreeOnClick,
					asyncSuccess:function(event, treeId, treeNode, msg){
						//zTree1.expandAll(true);
						if(!treeNode){	//判断是 顶级机构就展开,其余的收缩+
						   var rootNode = zTree1.getNodeByParam("level", 0);
						   zTree1.expandNode(rootNode, true, false);
						}
					},
					beforeAsync:function(treeId,treeNode){
						zTree1.setting.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
						//return false;
					}
				}
		};

		var zNodes =[];

		function showMenu() {
			$("#dropMenu").focus();
			var sortSel = $("#depNam");
			var sortOffset = $("#depNam").offset();
			$("#dropMenu").toggle();
			
			var disstate = $("#dropMenu").css("display");
			if(disstate == 'block'){			
			   $("#job").css("visibility","hidden");
			}else if(disstate == 'none'){
			   $("#job").css("visibility","visible");
			}
		}
		function hideMenu() {
		    $("#job").css("visibility","visible");
			$("#dropMenu").hide();
		}
		
		function zTreeOnClick(event, treeId, treeNode) {
			if (treeNode.isParent) {
				setting.asyncUrl =  pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
			}
			if (treeNode) {
				var sortObj = $("#depNam");
				sortObj.attr("value", treeNode.name);
				$("#depId").attr("value",treeNode.id);
				hideMenu();
			}
		}

		function reloadTree() {
			hideMenu();
			zTree1 = $("#dropdownMenu").zTree(setting, zNodes);
		}	

		//关闭树的下拉框方法（点击别处时关闭）这个是要特殊处理的方法
		function closeTreeFunOnSpc(ids)
		{
			$("#employPerType").click(function(e){
				e.stopPropagation();
			});

			$("#clientPerType").click(function(e){
				e.stopPropagation();
			});
			
			$("#userPerType").click(function(e){
				e.stopPropagation();
			});
			
			$("#selectDepBtn_employ").click(function(e){
				e.stopPropagation();
			});
			
			$("#selectDepBtn_user").click(function(e){
				e.stopPropagation();
			});

			$("#treeInput").click(function(e){
				e.stopPropagation();
			});
			
			$("#selectDepBtn_client").click(function(e){
				e.stopPropagation();
			});

			for(var i=0;i<ids.length;i++)
			{
				$("#"+ids[i]).click(function(e){
					e.stopPropagation();
				});
			}

			$('html,body').click(function(e){
				var $obj=$(e.target);
		        if($obj.attr("class").indexOf("treeInput")==-1){
		        	for(var i=0;i<ids.length;i++)
		        	{
		        		$("#"+ids[i]).css("display","none");
		        		$("select").css("visibility","");
		        	}
		      }
		   });
		}
	
		
		function back(){
			location.href=pathUrl+"/epl_employeeBook.htm?method=find&isDes=1&lgguid="+$("#lgguid").val()+
						"&lgusername="+$("#lgusername").val();
		}
		
		//处理获得焦点事件
		function getFocus(){
			$("#tempMobile").val("");
		}
		// 处理失去焦点事件
		function getBlur(){
			var phone_temp = $("#mobile").val();
			var ishidephone = $("#ishidephone").val();
			var mobile= $("#tempMobile").attr("value");
			
			if((mobile == "" || mobile.length == 0) && phone_temp.length>0){
				 $("#tempMobile").val(ishidephone);
			}
		}
		//这里是点击CHECKBOX 是否需要固定尾号
			//isHave  是判断它是否分配尾号   1 是有   2是无
			//oldSubno  分配的尾号
			function updateHaveSubno(isHave,oldSubno){
				 var isneedSubno = "2";		
				 var guid = $("#guId").val();
			  	//如果点击了选中 
				if(document.getElementsByName('isNeedSubno')[0].checked==true){	
					isneedSubno = "1";
				}
				//这里是判断下  在该操作员有绑定固定尾号情况下，选择是否需要回收还是修改尾号
			 	if(isHave == "1"){
			 		if(isneedSubno == "1"){
			 			$("#addSubno").val(oldSubno);
			 			$("#addSubno").css("visibility","visible");
			 		}else if(isneedSubno == "2"){
			 			$("#addSubno").val("");
			 			$("#addSubno").css("visibility","hidden");
			 		}
			 		$("#haveSubno").val(isneedSubno);
			 	}else{					// if(isHave == "2" || isHave == "0" )
			 		//这里是判断下  在该操作员没有绑定固定尾号情况下，选择是否需要绑定尾号
			 		if(isneedSubno == "1"){
			 			$("#addSubno").show();
			 			$.post("opt_sysuser.htm?method=getSingleSubno",{guid:guid,lgcorpcode:$("#lgcorpcode").val()},function(msg){
							if("" == msg){
			 					alert(getJsLocaleMessage('employee','employee_alert_84'));
			 					document.getElementsByName('isNeedSubno')[0].checked = false;
			 					return;
			 				}else if("notsubno" == msg){
			 					alert(getJsLocaleMessage('employee','employee_alert_84'));
			 					document.getElementsByName('isNeedSubno')[0].checked = false;
			 					return;
			 				}else if("enough" == msg){
			 					alert(getJsLocaleMessage('employee','employee_alert_85'));
			 					document.getElementsByName('isNeedSubno')[0].checked = false;
			 					return;
			 				}else{
			 					$("#addSubno").val(msg);
								$("#subno2").val(msg);
								$("#addSubno").show();
			 				}
						});
			 		}else if(isneedSubno == "2"){
			 			$("#addSubno").show();
			 			$.post("opt_sysuser.htm?method=delSingleSubno",{guid:guid,lgcorpcode:$("#lgcorpcode").val()},function(msg){
							if(msg == "1"){
								$("#addSubno").val("");
								$("#addSubno").hide();
							}else{
								$("#zhu").html(getJsLocaleMessage('employee','employee_alert_86'));
							}
						});
			 		}
			 		$("#haveSubno").val(isneedSubno);
			 	}
			}
			$(document).ready(function(){
				$('#depNam,#userDepName').wrapSel();
			})
