		var zTree;
		var ipath = base_ipath;
		function aaa(){
			var types = $("#chooseType").attr("value");
			var manCount = parseInt($("#manCount").html());
			if(types == 2)
			{
				var tempVal="";
				var name ="";
				var groupStr= $(window.parent.document).find("#groupStr").val();
				if($("#groupList option:selected").size()==0)
				{
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_48"));
					return;
				}
				$("#groupList option:selected").each(function() 
					{
					 	tempVal = $(this).val();
					 	name = $(this).text();
					 	if(groupStr.indexOf(","+tempVal+",") >= 0)
						{
							//alert(name + "群组已添加！");
							alert(getJsLocaleMessage("qyll","qyll_lldg_alter_49"));
						}else
						{
							if($(this).attr("mcount")=="0")
					    	{
								alert(getJsLocaleMessage("qyll","qyll_lldg_alter_50"));
								return;
						    }
						    
							name = name +"  ["+ $(this).attr("mcount")+"人]";
							groupStr = groupStr+tempVal+",";
							$("#right").append("<option value='"+tempVal+"' et='4' mobile='' mcount='"+$(this).attr("mcount")+"'>[ "+getJsLocaleMessage("qyll","qyll_lldg_alter_51")+"] "+name+"</option>");
							$("#getChooseMan").append("<li dataval='"+tempVal+"' et='4' mobile='' mcount='"+$(this).attr("mcount")+"'>[ "+getJsLocaleMessage("qyll","qyll_lldg_alter_51")+"] "+name+"</li>");
							manCount +=parseInt($(this).attr("mcount"));
						}
					}
				);
				$("#manCount").html(manCount);
				$(window.parent.document).find("#groupStr").val(groupStr);
				return;
			}
			var depId;
			var depName;
			zTree = window.frames['sonFrame'].returnZTree();
			if(zTree.getSelectedNode()==null)
			{
				alert(getJsLocaleMessage("qyll","qyll_lldg_alter_52"));
				return;
			}
			depId = zTree.getSelectedNode().id;
			depName = zTree.getSelectedNode().name;
			var rops = $("#right option");
			var depIdsExist= $(window.parent.document).find("#depIdStr").val();
			if(rops.length>0)
			{
				for(var i = 0;i<rops.length;i=i+1)
				{
					if(depId==rops.eq(i).attr("value") && rops.eq(i).attr("isdep") == 1)
					{
						alert(rops[i].text + getJsLocaleMessage("qyll","qyll_lldg_alter_53"));
						return;
					}
				}
			}
			var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
			EmpExecutionContext.log(ipath+"/common.htm?method=getDep",{lgcorpcode:lgcorpcode,depId:depId,depIdsExist:depIdsExist,depName:depName}); 
			$.ajax({
			async : false,
			url: ipath+"/common.htm?method=getDep", 
			type : "GET",
			data: {lgcorpcode:lgcorpcode,depId:depId,depIdsExist:depIdsExist,depName:depName},
			success: function(result)
			//$.post(ipath+"/common.htm?method=getDep", {lgcorpcode:lgcorpcode,depId:depId,depIdsExist:depIdsExist,depName:depName}, function(result)
			{
				if(result=="depExist")
				{
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_54"));
					return;
			    }
			    var ismut = 0;
			    if(confirm(getJsLocaleMessage("qyll","qyll_lldg_alter_55"))){
			    	ismut=1;
			    }
			    //检查要添加的机构是不是包含已经添加的机构，如果是则删除已经添加的子机构，如果不是则生成"[机构]...机构 (包含子机构)"
		    	$.ajax({
				async : false,
				url: ipath+"/common.htm?method=isDepsContainedByDepB" ,
				type : "POST",
				data: {depId : depId,
		    			lgcorpcode:lgcorpcode,
		    			depIdsExist : depIdsExist,
		    			depName : depName,
		    			ismut : ismut},
				success: function(result2)
			    	{
			    	
				    	if(ismut==0)
	    				{
				    		if(result2=="0")
					    	{
								alert(getJsLocaleMessage("qyll","qyll_lldg_alter_56"));
								return;
						    }
						    
	    					$(window.parent.document).find("#depIdStr").val(depIdsExist+depId+",");
	    					$("#right").append("<option value=\'"+depId+"\' isdep='1'  et='2' mobile='' mcount='"+result2+"'>[ "+getJsLocaleMessage("qyll","qyll_lldg_alter_57")+"] "+depName+" ["+result2+getJsLocaleMessage("qyll","qyll_lldg_alter_58")+"]</option>");
	    					$("#getChooseMan").append("<li dataval=\'"+depId+"\' isdep='1'  et='2' mobile='' mcount='"+result2+"'>[ "+getJsLocaleMessage("qyll","qyll_lldg_alter_57")+"] "+depName+" ["+result2+getJsLocaleMessage("qyll","qyll_lldg_alter_58")+"]</li>");
	    					manCount+=parseInt(result2);
	    					//return ;
	    	    		}else if(result2.indexOf("notContains")==0)
						{
							if(result2.substr(12)=="0")
							{
								alert(getJsLocaleMessage("qyll","qyll_lldg_alter_56"));
								return;
							}
							$(window.parent.document).find("#depIdStr").val(depIdsExist+"e"+depId+",");
							$("#right").append("<option value='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+result2.substr(12)+"'>[ "+getJsLocaleMessage("qyll","qyll_lldg_alter_57")+"] "+depName+"("+getJsLocaleMessage("qyll","qyll_lldg_alter_59")+")["+result2.substr(12)+getJsLocaleMessage("qyll","qyll_lldg_alter_58")+"]</option>");
							$("#getChooseMan").append("<li dataval='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+result2.substr(12)+"'>[ "+getJsLocaleMessage("qyll","qyll_lldg_alter_57")+"] "+depName+"("+getJsLocaleMessage("qyll","qyll_lldg_alter_59")+")["+result2.substr(12)+getJsLocaleMessage("qyll","qyll_lldg_alter_58")+"]</li>");
							manCount+=parseInt(result2.substr(12));
						}else{
							var strArr = result2.split(",");

							if(strArr[0]=="0")
							{
								alert(getJsLocaleMessage("qyll","qyll_lldg_alter_56"));
								return;
							}
							
							$(window.parent.document).find("#depIdStr").val(depIdsExist+"e"+depId+",");
							$("#right").append("<option value='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+strArr[0]+"'>[ "+getJsLocaleMessage("qyll","qyll_lldg_alter_57")+"] "+depName+"("+getJsLocaleMessage("qyll","qyll_lldg_alter_59")+")["+strArr[0]+getJsLocaleMessage("qyll","qyll_lldg_alter_58")+"]</option>");
							$("#getChooseMan").append("<li dataval='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+strArr[0]+"'>[ "+getJsLocaleMessage("qyll","qyll_lldg_alter_57")+"] "+depName+"("+getJsLocaleMessage("qyll","qyll_lldg_alter_59")+")["+strArr[0]+getJsLocaleMessage("qyll","qyll_lldg_alter_58")+"]</li>");
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
				 });
		    	}
			});
		}

		var curI = 1;
		function changeChooseType()
		{
			$("#left").html("");
			$('#pageIndex1,#totalPage1').val(1);
			$('#showPage1').html('');
			var i=$("#chooseType").val();
			if( curI == i)
			{
				return;
			}else
			{
				curI = i;
			}
			if(i == 1)
			{
				//$("#etree").html("<iframe id='sonFrame' style='width: 240px; height: 175px;' frameborder='1' src='<%=iPath %>/ssm_addrbookDepTree1.jsp'></iframe>");
				$("#showUserName").html(getJsLocaleMessage("qyll","qyll_lldg_alter_60"));
				$("#selectDep").val(getJsLocaleMessage("qyll","qyll_lldg_alter_61"));
				
			}else if(i == 2)
			{
				if($("#egroup").html() == "")
				{
					//$.post(ipath + "/ssm_comm.htm",{
					//	method:"getGroupList" ,
					$.post(ipath + "/ll_comm.htm?method=getGroupList",{
						lguserid:$(window.parent.document).find("#lguserid").val()
			   			},function(GroupList)
			   			{
							$("#egroup").html(GroupList);
						}
					);
				}else
				{
					grouponChange();
				}
				$("#showUserName").html(getJsLocaleMessage("qyll","qyll_lldg_alter_62"));
				$("#selectDep").val(getJsLocaleMessage("qyll","qyll_lldg_alter_63"));
			}
			$("#etree").toggle();
			$("#egroup").toggle();
		}

		function grouponChange()
		 {
				var tempVal="";
				var name ="";
				var epname = $("#epname").val();
				$("#groupList option:selected").each(function() 
					{
					 	tempVal = tempVal+$(this).val()+",";
					 	name = name+$(this).text()+",";
					}
				);
				$("#pageIndex1").val(1);
				//$.post(ipath + "/ssm_comm.htm",{
				//	method:"getGroupMember" ,
				$.post(ipath + "/ll_comm.htm?method=getGroupMember",{
					udgId:tempVal ,
					epname : epname,
					lguserid:$(window.parent.document).find("#lguserid").val()
		   			},function(groupMember){
		   				//第二个@出现位置的索引
		   				var index = groupMember.indexOf("@",groupMember.indexOf("@")+1);
		   				var tempStr = groupMember.substring(0,index);
		   				//var tempStr = groupMember.substring(0,groupMember.lastIndexOf("@"));
						var strs =new Array();
						strs =  tempStr.split("@");
						$("#totalPage1").val(strs[1]);
						$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
						var tempStr2 = groupMember.substring(index+1);
						$("#left").html(tempStr2);
						//$("#left").html(groupMember);
					}
				);
				if(name.length>9)
				{
					name = name.substr(0,9)+"...";
				}else if(name.length > 0)
				{
					name = name.substr(0,name.length-1);
				}
				$("#showUserName").html(getJsLocaleMessage("qyll","qyll_lldg_alter_62")+"："+name);
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
		        var epname = $("#epname").val();
		        if(epname == null || $.trim(epname).length==0)
		        {
		        	return;
		       	}
		        var addTypes = $("#addType").val();
		        var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
		        $(window.parent.document).find("#pageIndex1").val(1);
				$.post(ipath+"/common.htm?method=getDepAndEmpTree", 
					{
					epname : epname,
					lgcorpcode:lgcorpcode,
					depId:"",
					addTypes:addTypes
					}, function(result){
						//第二个@出现位置的索引
		   				var index = result.indexOf("@",result.indexOf("@")+1);
		   				var tempStr = result.substring(0,index);
						//var tempStr = result.substring(0,result.lastIndexOf("@"));
						var strs =new Array();
						strs =  tempStr.split("@");
						$("#totalPage1").val(strs[1]);
						$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
						var tempStr2 = result.substring(index+1);
						$("#left").html(tempStr2);
				});
			 }
			 else if(types == 2)
			 {
				 grouponChange();
			 }
		 }
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
			function treeLoseFocus()
			{
				window.frames['sonFrame'].returnZTree().cancelSelectedNode();
				$("#groupList").val("");
			}
			function router()
			{
				$("#toLeft").attr("disabled", true);
				if($("#left").val()!=null && $("#left").val()!="")
				{
					moveLeft();
				}else{
					aaa();
				}
				$("#toLeft").attr("disabled", false);
			}
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
			
			function goLastPage1()
			{
				var types = $("#chooseType").attr("value");
				var depId=$("#depId").val();
				var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
				var lguserid = $(window.parent.document).find("#lguserid").val();
				var epname = $("#epname").val();
				var tempVal= $("#groupList").val()+",";
				if(types == 1 && depId=="")
				{
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_64"));
					return;
				}
				else if(types == 2 && tempVal.length < 2)
				{
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_65"));
					return;
				}
				var pageIndex1 = $("#pageIndex1").val();
				if(pageIndex1=="1")
				{
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_66"));
					return;
				}
				//员工通讯录
				if(types == 1)
				{
					$.post(base_ipath + "/common.htm", {method:"getDepAndEmpTree",lgcorpcode:lgcorpcode,pageIndex1:pageIndex1,depId:depId,opType:"goLast"}, function(result)
					{
					   	//第二个@出现位置的索引
	   					var index = result.indexOf("@",result.indexOf("@")+1);
						$("#left").html(result.substring(index+1));
						$("#pageIndex1").val(parseInt(pageIndex1)-1);
						$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
					});
				}
				else
				{
					$.post(ipath + "/ll_comm.htm?method=getGroupMember",{udgId:tempVal,epname:epname,lguserid:lguserid,pageIndex1:pageIndex1,opType:"goLast"},function(result)
		   			{
					   	//第二个@出现位置的索引
	   					var index = result.indexOf("@",result.indexOf("@")+1);
						$("#left").html(result.substring(index+1));
						$("#pageIndex1").val(parseInt(pageIndex1)-1);
						$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
					});
				}
			}
			function goNextPage1()
			{
				var types = $("#chooseType").attr("value");
				var depId=$("#depId").val();
				var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
				var lguserid = $(window.parent.document).find("#lguserid").val();
				var epname = $("#epname").val();
				var tempVal= $("#groupList").val()+",";
				if(types == 1 && depId=="")
				{
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_64"));
					return;
				}
				else if(types == 2 && tempVal.length < 2)
				{
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_65"));
					return;
				}
				var pageIndex1 = $("#pageIndex1").val();
				var totalPage1 = $("#totalPage1").val();
				if(pageIndex1==totalPage1)
				{
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_69"));
					return;
				}
				if(types == 1)
				{
					$.post(base_ipath + "/common.htm", {method:"getDepAndEmpTree",lgcorpcode:lgcorpcode,pageIndex1:pageIndex1,depId:depId,opType:"goNext"}, function(result)
					{
					   	//第二个@出现位置的索引
	   					var index = result.indexOf("@",result.indexOf("@")+1);
						$("#left").html(result.substring(index+1));
						$("#pageIndex1").val(parseInt(pageIndex1)+1);
						$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
					});
				}
				else
				{
					$.post(ipath + "/ll_comm.htm?method=getGroupMember",{udgId:tempVal,epname:epname,lguserid:lguserid,pageIndex1:pageIndex1,opType:"goNext"},function(result)
					{
					   	//第二个@出现位置的索引
	   					var index = result.indexOf("@",result.indexOf("@")+1);
						$("#left").html(result.substring(index+1));
						$("#pageIndex1").val(parseInt(pageIndex1)+1);
						$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
					});
				}
			}
			