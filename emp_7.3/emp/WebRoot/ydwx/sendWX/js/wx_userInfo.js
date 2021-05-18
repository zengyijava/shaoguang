//<%-- 选择员工  /客户 /群组--%>
		function aaa(){
			var types = $("#chooseType").attr("value");
			var manCount = parseInt($("#manCount").html());
			if(types == 2)
			{
				var tempVal="";
				var name ="";
				var groupStr= $("#groupIdsStrs").val();
				var groupclientStr= $("#groupClientStr").val();
				if($("#groupList option:selected").size()==0)
				{
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_118"));
					return;
				}
				$("#groupList option:selected").each(function() 
					{
					 	tempVal = $(this).val();
					 	name = $(this).text();
					 	
					 	var groupSplit = groupStr.split(",");
					 	for (j =0; j < groupSplit.length; j++ )     
					    {    
					       if(tempVal == groupSplit[j]){
					           alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_119"));
					           return;
					       }    
					    } 
					 	var groupclient = groupclientStr.split(",");
					 	for (j =0; j < groupclient.length; j++ )     
					    {    
					       if(tempVal == groupclient[j]){
					           alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_119"));
					           return;
					       }    
					    } 
					 	
						if($(this).attr("mcount")=="0")
				    	{
							alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_120"));
							return;
					    }
						var gtype=$(this).attr("gtype");
						name = name +"  ["+ $(this).attr("mcount")+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]";
					    if(gtype=="2"){
					    	$("#groupClientStr").val(groupclientStr+tempVal+",");
					    }else{
					    	$("#groupIdsStrs").val(groupStr+tempVal+",");
					    }
						$("#right").append("<option value='"+tempVal+"' et='3' isdep='3' mobile='' mcount='"+$(this).attr("mcount")+"'>[ "+getJsLocaleMessage("ydwx","ydwx_jtwxfs_121")+"] "+name+"</option>");
						$("#getChooseMan").append("<li dataval='"+tempVal+"' et='3' isdep='3' mobile='' mcount='"+$(this).attr("mcount")+"'>[ "+getJsLocaleMessage("ydwx","ydwx_jtwxfs_121")+"] "+name+"</li>");
						manCount +=parseInt($(this).attr("mcount"));
					}
				);
				$("#manCount").html(manCount);
				return;
			}
			var depId;
			var depName;
			zTree = window.frames['sonFrame'].returnZTree();
			if(zTree.getSelectedNode()==null)
			{
				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_122"));
				return;
			}
			depId = zTree.getSelectedNode().id;
			depName = zTree.getSelectedNode().name;
			var rops = $("#right option");
			//<%-- 员工机构ID --%>
			var depIdsExist = $("#empDepIdsStrs").val();	
			//<%-- 客户机构ID --%>	
			var cliDepIds = $("#cliDepIdsStrs").val();	
			//<%-- 选择的名称 --%>
			var choiceName = $("#choiceName").val();
			var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
			if(types=="1"){
			  if(rops.length>0)
			  {
					for(var i = 0;i<rops.length;i=i+1)
					{
						if(depId==rops.eq(i).attr("value") && rops.eq(i).attr("isdep") == 1)
						{
							alert(rops[i].text + getJsLocaleMessage("ydwx","ydwx_jtwxfs_123"));
							return;
						}
					}
			   }
			
			  $.post(ipathh+"/wx_send.htm", {method:"getDep", lgcorpcode:lgcorpcode,depId:depId,depIdsExist:depIdsExist,depName:depName}, function(result)
				{
				if(result=="depExist")
				{
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_124"));
					return;
			    }
			    var ismut = 0;
			    if(confirm(getJsLocaleMessage("ydwx","ydwx_jtwxfs_125"))){
			    	ismut=1;
			    }
			    //检查要添加的机构是不是包含已经添加的机构，如果是则删除已经添加的子机构，如果不是则生成"[机构]...机构 (包含子机构)"
		    	$.post(ipathh+"/wx_send.htm", 
			    	{
		    			method : "isDepsContainedByDepB",
		    			depId : depId,
		    			lgcorpcode:lgcorpcode,
		    			depIdsExist : depIdsExist,
		    			depName : depName,
		    			ismut : ismut
	    			}, function(result2)
			    	{
				    	
				    	if(ismut==0)
	    				{
				    		if(result2=="0")
					    	{
								alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_126"));
								return;
						    }
						    $("#empDepIdsStrs").val(depIdsExist+depId+",");
	    					$("#right").append("<option value=\'"+depId+"\' isdep='1'  et='2' mobile='' mcount='"+result2+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_127")+"] "+depName+" ["+result2+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</option>");
	    					$("#getChooseMan").append("<li dataval=\'"+depId+"\' isdep='1'  et='2' mobile='' mcount='"+result2+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_127")+"] "+depName+" ["+result2+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</li>");
	    					manCount+=parseInt(result2);
	    					//return ;
	    	    		}else if(result2.indexOf("notContains")==0)
						{
							if(result2.substr(12)=="0")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_126"));
								return;
							}
							$("#empDepIdsStrs").val(depIdsExist+"e"+depId+",");
							$("#right").append("<option value='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+result2.substr(12)+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_127")+"] "+depName+"("+getJsLocaleMessage("ydwx","ydwx_jtwxfs_128")+")["+result2.substr(12)+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</option>");
							$("#getChooseMan").append("<li dataval='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+result2.substr(12)+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_127")+"] "+depName+"("+getJsLocaleMessage("ydwx","ydwx_jtwxfs_128")+")["+result2.substr(12)+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</li>");
							manCount+=parseInt(result2.substr(12));
						}else{
							var strArr = result2.split(",");

							if(strArr[0]=="0")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_126"));
								return;
							}
							$("#empDepIdsStrs").val(depIdsExist+"e"+depId+",");
							$("#right").append("<option value='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+strArr[0]+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_127")+"] "+depName+"("+getJsLocaleMessage("ydwx","ydwx_jtwxfs_128")+")["+strArr[0]+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</option>");
							$("#getChooseMan").append("<li dataval='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+strArr[0]+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_127")+"] "+depName+"("+getJsLocaleMessage("ydwx","ydwx_jtwxfs_128")+")["+strArr[0]+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</li>");
							manCount+=parseInt(strArr[0]);
							for(var i=1;i<strArr.length;i=i+1)
							{
								var $aaa = $("#right").find("option[isdep='1'][value="+strArr[i]+"]");
								var $bbb = $("#getChooseMan").find("li[isdep='1'][dataval="+strArr[i]+"]");
								if($aaa.attr("et")==3)
								{
									var depidstr = $(window.parent.document).find("#depIdStr").val();
									$(window.parent.document).find("#depIdStr").val(depidstr.replace("e"+$aaa.val()+",",""));
								}else if($aaa.attr("et")==2)
								{
									var depidstr = $(window.parent.document).find("#depIdStr").val();
									$(window.parent.document).find("#depIdStr").val(depidstr.replace(","+$aaa.val()+",",","));
								}
								manCount=manCount-$aaa.attr("mcount");
								$aaa.remove();
								$bbb.remove();
							}
						}
						$("#manCount").html(manCount);
				    }
				 );
			});
			}else  if(types == "3"){
					//<%--处理客户机构--%>
					if(cliDepIds != "" && cliDepIds.length>0){
						//<%--处理客户机构--%>
						$.post(ipathh+"/wx_send.htm", {method:"isClientDepContained",depId:depId,cliDepIds:cliDepIds}, function(result)
						{
							if("" == result){
								alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));
			   					$("#selectDep").attr("disabled","");	
			   					return;
			   				}else if(result.indexOf("html") > 0){
								alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_129"));
								$("#selectDep").attr("disabled","");	
				    			window.location.href = window.location.href;
				    		    return;
				    		}
							if(result=="depExist")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_124"));
								$("#selectDep").attr("disabled","");	
								return;
						    }
						    var ismut = 0;
						    if(confirm(getJsLocaleMessage("ydwx","ydwx_jtwxfs_125"))){
						    	ismut=1;
						    }
						   // <%--处理该机构包含的子机构 操作, 并将选择好的子机构进行删除操作 --%>
							$.post(ipathh+"/wx_send.htm", 
						    	{
					    			method : "isClientDepContaineDeps",
					    			depId : depId,
					    			cliDepIds : cliDepIds,
					    			ismut : ismut
				    			}, function(result2){
					    				if(result2 == "nobody"){
					    					//alert("该员工机构下没有客户！");
					    					$("#selectDep").attr("disabled","");	
					    					return;
					    				}else if(result2 == "errer"){
					    					alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));
					    					$("#selectDep").attr("disabled","");	
					    					return;
					    				}
				    			 		//<%--机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组      et 1表示不包含    2表示包含--%>
		    							if(ismut==0){
					    					$("#cliDepIdsStrs").val(cliDepIds+depId+",");
					    					$("#right").append("<option value=\'"+depId+"\' isdep='2'  et='1' moblie='' mcount='"+result2+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_130")+"] "+choiceName+" ["+result2+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</option>");
					    					$("#getChooseMan").append("<li dataval=\'"+depId+"\' isdep='2'  et='1' moblie='' mcount='"+result2+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_130")+"] "+choiceName+" ["+result2+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</li>");
					    					$("#selectDep").attr("disabled","");	
					    					manCount+=parseInt(result2);
					    	    		}
					    	    		//<%--机构包含子机构但是右边没有其要删除的子机构的操作 --%>
										else if(result2.indexOf("notContains")==0)
										{
											if(result2.substr(12)=="0")
											{
												alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_126"));
												return;
											}
											$("#cliDepIdsStrs").val(cliDepIds+"e"+depId+",")
											$("#right").append("<option value='"+depId+"' isdep='2'  et='2' moblie='' mcount='"+result2.substr(12)+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_130")+"] "+choiceName+"("+getJsLocaleMessage("ydwx","ydwx_jtwxfs_128")+")["+result2.substr(12)+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</option>");
											$("#getChooseMan").append("<li dataval='"+depId+"' isdep='2'  et='2' moblie='' mcount='"+result2.substr(12)+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_130")+"] "+choiceName+"("+getJsLocaleMessage("ydwx","ydwx_jtwxfs_128")+")["+result2.substr(12)+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</li>");
											$("#selectDep").attr("disabled","");	
											manCount+=parseInt(result2.substr(12));	
										}else{
												var strArr = result2.split(",");
												if(strArr[0]=="0")
												{
													alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_126"));
													return;
												}
												$("#cliDepIdsStrs").val(cliDepIds+"e"+depId+",")
												$("#right").append("<option value='"+depId+"' isdep='2'  et='2' moblie='' mcount='"+strArr[0]+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_130")+"] "+choiceName+"("+getJsLocaleMessage("ydwx","ydwx_jtwxfs_128")+")["+strArr[0]+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</option>");
												$("#getChooseMan").append("<li dataval='"+depId+"' isdep='2'  et='2' moblie='' mcount='"+strArr[0]+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_130")+"] "+choiceName+"("+getJsLocaleMessage("ydwx","ydwx_jtwxfs_128")+")["+strArr[0]+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</li>");
												$("#selectDep").attr("disabled","");	
												manCount+=parseInt(strArr[0]);
												for(var i=1;i<strArr.length;i=i+1)
												{
													var containId = $.trim(strArr[i]);
													var $aaa = $("#right").find("option[isdep=2][value='"+containId+"']");
													var $bbb = $("#getChooseMan").find("li[isdep=2][dataval='"+containId+"']");
													//<%-- et1表示包含子机构关系,2表示getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"含子机构--%>
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
						//<%--直接添加客户机构--%>
				  		var ismut = 0;
					    if(confirm(getJsLocaleMessage("ydwx","ydwx_jtwxfs_125"))){
					    	ismut=1;
					    }
				    	$.post(ipathh+"/wx_send.htm",{
			    			method : "getClientDepCount",
			    			depId : depId,
			    			ismut : ismut
		    			}, function(result2){
		    				if(result2 == "nobody" || result2 == "0"){
		    				//	alert("该客户机构下没有客户！");
		    					$("#selectDep").attr("disabled","");	
		    					return;
		    				}else if(result2 == "errer"){
		    					alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));
		    					$("#selectDep").attr("disabled","");	
		    					return;
		    				}
	    					//<%--机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组      et 1表示不包含    2表示包含--%>
		 					if(ismut==0){
		    					$("#cliDepIdsStrs").val(depId+",");
		    					$("#right").append("<option value='"+depId+"' isdep='2'  et='1' moblie=''  mcount='"+result2+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_130")+"] "+choiceName+" ["+result2+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</option>");
		    					$("#getChooseMan").append("<li dataval='"+depId+"' isdep='2'  et='1' moblie=''  mcount='"+result2+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_130")+"] "+choiceName+" ["+result2+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</li>");
		    					$("#selectDep").attr("disabled","");	
					    		manCount+=parseInt(result2);
		    	    		}else{
		    	    			var strArr = result2.split(",");
								$("#cliDepIdsStrs").val("e"+depId+",")
								$("#right").append("<option value='"+depId+"' isdep='2'  et='2' moblie=''  mcount='"+strArr[0]+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_130")+"] "+choiceName+"("+getJsLocaleMessage("ydwx","ydwx_jtwxfs_128")+")["+result2+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</option>");
								$("#getChooseMan").append("<li dataval='"+depId+"' isdep='2'  et='2' moblie=''  mcount='"+strArr[0]+"'>["+getJsLocaleMessage("ydwx","ydwx_jtwxfs_130")+"] "+choiceName+"("+getJsLocaleMessage("ydwx","ydwx_jtwxfs_128")+")["+result2+getJsLocaleMessage("ydwx","ydwx_jtwxfs_60")+"]</li>");
								$("#selectDep").attr("disabled","");
								manCount+=parseInt(strArr[0]);	
							}
							$("#manCount").html(manCount);
		    			});
					
					}
			}
		}

	
		var curI = 1;
		// 选择人员，群组等的相关显示
		function changeChooseType()
		{
			$('#pageIndex,#totalPage').val(1);
			var i=$("#chooseType").val();
			if(i == 1)
			{	$("#left").empty();	
				$("#pagecode").empty();
				$("#egroup").hide();
				$("#etree").show();	
				$("#prepage").css("visibility","hidden");
				$("#nextpage").css("visibility","hidden");
				$("#etree").html("<iframe id='sonFrame' name='sonFrame' style='width: 240px; height:240px;'   src="+iPath+"/wx_tree.jsp?userid2="+userid2+" frameborder='0'></iframe>");
			}else if(i == 2)
			{	
			    $("#left").empty();	
				$("#pagecode").empty();
				$("#etree").hide();
				$("#egroup").show();
				$("#prepage").css("visibility","hidden");
				$("#nextpage").css("visibility","hidden");
				$.post(ipathh+"/wx_send.htm",{
						method:"getGroupList" ,
						lguserid:$(window.parent.document).find("#lguserid").val()
			   			},function(GroupList)
			   			{
							$("#egroup").html(GroupList);
						}
					);
			}else if(i == 3){	
				$("#left").empty();	
				$("#egroup").hide();
				$("#etree").show();	
				$("#pagecode").empty();
				$("#prepage").css("visibility","hidden");
				$("#nextpage").css("visibility","hidden");
				var userid=$(window.parent.document).find("#lguserid").val();
				$("#etree").html("<iframe id='sonFrame' name='sonFrame' style='width: 240px; height:240px;'   src="+iPath+"/wx_CliTree.jsp?userid="+userid+" frameborder='0'></iframe>");
			}

		}
		// 选择当前群组
		function grouponChange(){
			var name = $("#epname").val();
            var $selectedGroup = $("#groupList option:selected");
            var tempVal = $selectedGroup.val();
            var groupType = $selectedGroup.attr("gtype");
			$.post(ipathh+"/wx_send.htm",{
				method:"getGroupMemberByNameAndId" ,
				udgId:tempVal ,
				name : name,
                groupType : groupType
			},
			function(data) {
                var type = "";
                var array = eval("(" + data + ")");
                $("#left").empty();
                $.each(array, function (index, value) {
                    //默认员工
                    var l2g_type = value.l2g_type;
                    if (l2g_type === 0) {
                        //员工
                        type = "4";
                    } else if (l2g_type === 1) {
                        //客户
                        type = "5";
                    } else if (l2g_type === 2) {
                        //自定义
                        type = "6";
                    }
                    if (value.name) {
                        $("#left").append("<option value='" + value.guid + "' et='' isdep='" + type + "' mobile='" + value.mobile + "'>" + value.name + "</option>");
                    }
                });
            });
		}


		 //搜索按钮
		 function zhijieSearch()
		 {
			 var types = $("#chooseType").val();
			 var dqcheck;
			 if(types == 1)
			 {
				$("#left").empty();
				//var depId = $("#depId").val();
				var depId = "";
		        var epname = $("#epname").val();
		        if(epname == null || $.trim(epname).length==0)
		        {
		        	return;
		       	}
		        var addTypes = $("#addType").val();
		        var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
				$.post(ipathh+"/wx_send.htm", 
					{
						method:"getDepAndEmpTree1", 
						epname : epname,
						lgcorpcode:lgcorpcode,
						depId :depId,
						addTypes:addTypes
					}, function(result){
					$("#left").html(result);
				});
			 }
			 else if(types == 2)
			 {
				 grouponChange();
			 }if(types == 3)
			 {
				$("#left").empty();
				//var depId = $("#depId").val();
				var depId = "";
		        var epname = $("#epname").val();
		        if(epname == null || $.trim(epname).length==0)
		        {
		        	return;
		       	}
		        var addTypes = $("#addType").val();
		        var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
				$.post(ipathh+"/wx_send.htm", 
					{
						method:"getClientByDepId", 
						epname : epname,
						lgcorpcode:lgcorpcode,
						depId :depId
					}, function(result){
					$("#left").html(result);
				});
			 }
			 	$("#prepage").css("visibility","hidden");
				$("#nextpage").css("visibility","hidden");
				$("#pagecode").empty();
		 }
		 
		 //文本样式的处理
		 function inputTipText(){
				//所有样式名中含有graytext的input
				$("input[class*=graytext]").each(function(){
					var oldVal=$(this).val(); //默认的提示性文本
					$(this)
					.css({'color':'#666'}) //灰色
					.focus(function(){
					if($(this).val()!=oldVal)
						{$(this).css({'color':'#000'})}
					else
						{$(this).val('').css({'color':'#666'})}
					})
					.blur(function(){
					if($(this).val()=="")
						{$(this).val(oldVal).css({'color':'#666'})}
					})
					.keydown(function(){
						$(this).css({'color':'#000'})
					})
				})
			}
		 
			$(function(){
				inputTipText(); //直接调用就OK了
			})
			// 树状结构失去焦点
			function treeLoseFocus()
			{
				window.frames['sonFrame'].returnZTree().cancelSelectedNode();
				$("#groupList").val("");
			}
			// 选择的左边人员移动到右边显示
			function router()
			{
				if($("#left").val()!=null && $("#left").val()!="")
				{
					moveLeft();
				}else{
					aaa();
				}
			}
			//实践中样式的改变
			function fixWidth()
			{
			    var len = $("#right option").length ;
			    if( len != 0 ){
			        $('#right').css("width","525");
			        $("#rightDiv").css("overflow-x","scroll");    
			    }else{
			    	$('#right').css("width","204");
				}
			}
			function fixWidth2()
			{
			        $("#rightDiv").css("overflow-x","hidden");    
			}
			
			//此处是点击触发事件
			function a(){
				var path = $("#pathUrl").val();	
				if ($("#groupList option:selected").size() == 1) {
					$("#groupList option:selected").each(function() {
							$("#left").empty();
							//$("#prepage").attr("disabled",false);
							//$("#nextpage").attr("disabled",false);
					        var pageIndex = 1; 
					        $("#pageIndex").val(pageIndex);
							//<%-- 当前群组ID --%>
							var groupVal = $(this).val();	
							var groupName = $(this).text();	
							groupName = groupName.substring(0,groupName.lastIndexOf("["));
							//<%-- 当前群组类型  1是员工群组  2是客户群组--%>		
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

							
							$.post(path+"/wx_send.htm", {method:"getGroupUserByGroupId",depId:groupVal,pageIndex:pageIndex,type:grouptype}, function(result){
								if(result != "" ){
									//<%-- 获取分页信息 --%>
									var arr = result.substring(0,result.indexOf("#")).split(",");
									//<%--总页数 --%>
									var pageTotal = parseInt(arr[0]);
									//<%--总记录数 --%>
									var pageRec = arr[1];
									//<%-- 添加记录 --%>
									$("#left").html(result.substring(result.indexOf("#")+1));
									//<%-- 显示当前机构 --%>
								//	var tempName = groupName;
								//	if(tempName.length>9){
								//		tempName = tempName.substr(0,9)+"...";
								//	}
								//	$("#addrName").html("当前群组："+tempName);
									//<%-- 显示该机构总人数 --%>
								//	$("#userTotal").html("["+pageRec+"人]");
									//<%-- 如果是只有一页记录的话--%>
									$("#totalPage").val(pageTotal);
									$("#pagecode").html(pageIndex+"/"+pageTotal);
									//$("#prepage").attr("disabled",true);
							 		$("#prepage").css("visibility","visible");
									$("#nextpage").css("visibility","visible");
									if(pageTotal == 1){
										//$("#prepage").css("visibility","hidden");
										//$("#nextpage").css("visibility","hidden");
										return;
									}
								}else{
									$("#prepage").css("visibility","hidden");
									$("#nextpage").css("visibility","hidden");
								}
							});
								
					});
				}
			
			}
			
			
			
			
			//<%-- 处理按纽上一页  --%>
			function prePage(){
				var path = $("#pathUrl").val();	
				var pageIndex =  parseInt($("#pageIndex").val());
				//$("#nextpage").attr("disabled",false);
				//<%-- 选择的类型 --%>
				//var chooseType = $("input:radio[name='dept']:checked").attr("value");
				var chooseType = $("#chooseType").val();
				var choiceId = $("#choiceId").val();
				var name = $("#choiceName").val();
				pageIndex = pageIndex - 1;
				if(pageIndex<1){
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_132"));
					return;
				}
				$("#pageIndex").val(pageIndex);
				$("#left").empty();
				//<%-- 方法名  --%>
				var methodName ;
				//<%--用户 查询群组的时候 表识 1 员工群组  2客户群组  --%>
				var type = 1;
				
				if(chooseType == "3"){
					methodName = "getClientByDepId";
				}else if(chooseType == "2"){
					methodName = "getGroupUserByGroupId";
					type = $("#qztype").val();
				}else if(chooseType=="1"){
					methodName="getDepAndEmpTree1";
					choiceId = $("#depId").val();
				}
				//<%-- 处理员工/群组的上一页的操作 --%>
				//	<%--处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组--%>
					$.post(path+"/wx_send.htm", {method:methodName, depId:choiceId,pageIndex:pageIndex,type:type}, function(result){
									$("#pagecode").empty();
									if(result != "" ){
										if(chooseType!="1"){
											//<%-- 获取分页信息 --%>
											var arr = result.substring(0,result.indexOf("#")).split(",");
											//<%--总页数 --%>
											var pageTotal = parseInt(arr[0]);
											//<%--总记录数 --%>
											var pageRec = arr[1];
											//<%-- 添加记录 --%>
											$("#left").html(result.substring(result.indexOf("#")+1));
											//<%-- 显示当前机构 --%>
										//	var tempName = name;
										//	if(tempName.length>9){
										//		tempName = tempName.substr(0,9)+"...";
										//	}
										//	$("#addrName").html("当前机构："+tempName);
											//<%-- 显示该机构总人数 --%>
									//		$("#userTotal").html("["+pageRec+"人]");
											//<%-- 如果是只有一页记录的话--%>
											$("#pagecode").html(pageIndex+"/"+pageTotal);
											//<%-- 当总页数等于当前页时 ，下一页置灰--%>
											if(1 == pageIndex){
												//$("#prepage").attr("disabled",true);
												return;
											}
										}else{
											var tempStr = result.substring(0,result.lastIndexOf("@"));
											var strs =new Array();
											strs =  tempStr.split("@");
											$("#totalPage").val(strs[1]);
											$("#pagecode").html($("#pageIndex").val()+"/"+strs[1]);
											var tempStr2 = result.substring(result.lastIndexOf("@")+1);
											$("#left").html(tempStr2);
											//<%-- 当总页数等于当前页时 ，下一页置灰--%>
											if(1 == $("#pageIndex").val()){
												//$("#nextpage").attr("disabled",true);
												return;
											}	
										}
										
										
									}
					});
			}
			//<%-- 处理按纽 下一页 --%>
			function nextPage(){
				var path = $("#pathUrl").val();	
				var pageIndex =  parseInt($("#pageIndex").val());
				//$("#prepage").attr("disabled",false);
				
				//<%-- 选择的类型 --%>
				//var chooseType = $("input:radio[name='dept']:checked").attr("value");
				var chooseType = $("#chooseType").val();
				var choiceId = $("#choiceId").val();
				var totalPage= $("#totalPage").val();
				var name = $("#choiceName").val();
				pageIndex = pageIndex + 1;
				if(pageIndex>totalPage){
					alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_133"));
					return;
				}
				$("#pageIndex").val(pageIndex);
				$("#left").empty();
				//<%-- 方法名  --%>
				var methodName ;
				//<%--用户 查询群组的时候 表识 1 员工群组  2客户群组  --%>
				var type = 1;
				//if(chooseType == "1"){
				//	methodName = "getEmployeeByDepId";
				//}else if(chooseType == "3"){
					methodName = "getClientByDepId";
				//}else 
				if(chooseType == "2"){
					methodName = "getGroupUserByGroupId";
					type = $("#qztype").val();
				}else if(chooseType=="1"){
					methodName="getDepAndEmpTree1";
					choiceId = $("#depId").val();
				}
					//<%--处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组--%>
					$.post(path+"/wx_send.htm", {method:methodName, depId:choiceId,pageIndex:pageIndex,type:type}, function(result){
									$("#pagecode").empty();
									if(result != "" ){
										if(chooseType!="1"){
											//<%-- 获取分页信息 --%>
											var arr = result.substring(0,result.indexOf("#")).split(",");
											//<%--总页数 --%>
											var pageTotal = parseInt(arr[0]);
											//<%--总记录数 --%>
											var pageRec = arr[1];
											//<%-- 添加记录 --%>
											$("#left").html(result.substring(result.indexOf("#")+1));
											//<%-- 显示当前机构 --%>
										//	var tempName = name;
										//	if(tempName.length>9){
										//		tempName = tempName.substr(0,9)+"...";
										//	}
										//	$("#addrName").html("当前机构："+tempName);
											//<%-- 显示该机构总人数 --%>
										//	$("#userTotal").html("["+pageRec+"人]");
											//<%-- 如果是只有一页记录的话--%>
											$("#pagecode").html(pageIndex+"/"+pageTotal);
											//<%-- 当总页数等于当前页时 ，下一页置灰--%>
											if(pageTotal == pageIndex){
												//$("#nextpage").attr("disabled",true);
												return;
											}
										}else{
											var tempStr = result.substring(0,result.lastIndexOf("@"));
											var strs =new Array();
											strs =  tempStr.split("@");
											$("#totalPage").val(strs[1]);
											$("#pagecode").html($("#pageIndex").val()+"/"+strs[1]);
											var tempStr2 = result.substring(result.lastIndexOf("@")+1);
											$("#left").html(tempStr2);
											//<%-- 当总页数等于当前页时 ，下一页置灰--%>
											if(strs[1] == $("#pageIndex").val()){
												//$("#nextpage").attr("disabled",true);
												return;
											}
										}
									}
					});
			}
			
			$(document).ready(function(){
				
				var valu=$(window.parent.document).find("#userid").val();
				$("#prepage").css("visibility","hidden");
				$("#nextpage").css("visibility","hidden");
				$("#lguser").val(valu);
				});
			$('#getChooseMan').live('click',function(e){
				   if($(this).find('li').size()>0){
				     var target = e.target,$target = $(target);
				     if (target.nodeName === 'LI') {
					    if($target.hasClass('cur')){
					      $target.removeClass('cur');
					      $('#right').find('option').each(function(){
					        if($(this).val()==$target.attr('dataval')){
					          $(this).attr('selected',false);
					         
					        }
					      })
					    }else{
					      $target.addClass('cur');
					      $('#right').find('option').each(function(){
					        if($(this).val()==$target.attr('dataval') 
					        	&& $(this).attr('isdep')==$target.attr('isdep')){
					          $(this).attr('selected','selected');
					        }
					      })
					    }
					  }
				     
				   }
				})