		var zTree;
		var ipathh = $("#pathUrl").val();
		function aaa(){
			var depId;
			var depName;
			zTree = window.frames['sonFrame'].returnZTree();
			if(zTree.getSelectedNode()==null)
			{
				//alert("请选择机构！");
				alert(getJsLocaleMessage('group','group_js_sysChooseInfo_recordadded'));
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
						//alert(rops[i].text + " 记录已添加！");
						alert(rops[i].text + getJsLocaleMessage('group','group_js_sysChooseInfo_recordadded'));
						return;
					}
				}
			}
			var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
			$.post(ipathh+"/grp_cliGroupManage.htm", {method:"checkDepIsExist", lgcorpcode:lgcorpcode,depId:depId,depIdsExist:depIdsExist,depName:depName}, function(result)
			{
				if(result=="depExist")
				{
					//alert("该机构已经包含在添加的机构内！");
					alert(getJsLocaleMessage('group','group_js_sysChooseInfo_orgadded'));
					return;
			    }
			    var ismut = 0;
			    if(confirm(getJsLocaleMessage('group','group_js_sysChooseInfo_isselectchild'))){
			    	ismut=1;
			    }
			    //检查要添加的机构是不是包含已经添加的机构，如果是则删除已经添加的子机构，如果不是则生成"[机构]...机构 (包含子机构)"
		    	$.post(ipathh+"/grp_cliGroupManage.htm", 
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
	    					if(result2==0){
	    						//alert("[ "+depName+" ] 机构里没有操作员！");
	    						alert("[ "+depName+" ] " + getJsLocaleMessage('group','group_js_sysChooseInfo_orgnoopt'));
	    						return ;
	    					}
	    					$(window.parent.document).find("#depIdStr").val(depIdsExist+depId+",");
	    					$("#right").append("<option value=\'"+depId+"\' isdep='1'  et='2' mobile=''>[ " + getJsLocaleMessage('group','group_common_org') + "] "+depName+" ("+result2+ getJsLocaleMessage('group','group_common_peploe') + ")</option>");
	    					$("#getChooseMan").append("<li dataval=\'"+depId+"\' isdep='1'  et='2' mobile=''>[ " + getJsLocaleMessage('group','group_common_org') + "] "+depName+" ("+result2+ getJsLocaleMessage('group','group_common_peploe') + ")</li>");
	    					return ;
	    	    		}
						if(result2.indexOf("notContains")==0)
						{
							if(result2.substr(12)==0){
	    						alert("[ "+depName+" ] 机构里没有操作员！");
	    						return ;
	    					}
							$(window.parent.document).find("#depIdStr").val(depIdsExist+"e"+depId+",")
							$("#right").append("<option value='"+depId+"' isdep='1'  et='3' mobile=''>[ " + getJsLocaleMessage('group','group_common_org') + "] "+depName+"(" + getJsLocaleMessage('group','group_common_includesubunits') + ")("+result2.substr(12)+ getJsLocaleMessage('group','group_common_peploe') + ")</option>");
						    $("#getChooseMan").append("<li dataval='"+depId+"' isdep='1'  et='3' mobile=''>[ " + getJsLocaleMessage('group','group_common_org') + "] "+depName+"(" + getJsLocaleMessage('group','group_common_includesubunits') + ")("+result2.substr(12)+ getJsLocaleMessage('group','group_common_peploe') + ")</li>");
						}else{
							var strArr = result2.split(",");
							if(strArr[0]==0){
	    						alert("[ "+depName+" ] 机构里没有操作员！");
	    						return ;
	    					}
							$(window.parent.document).find("#depIdStr").val(depIdsExist+"e"+depId+",")
							$("#right").append("<option value='"+depId+"' isdep='1'  et='3' mobile=''>[ " + getJsLocaleMessage('group','group_common_org') + "] "+depName+"(" + getJsLocaleMessage('group','group_common_includesubunits') + ")("+strArr[0]+ getJsLocaleMessage('group','group_common_peploe') + ")</option>");
$("#getChooseMan").append("<li dataval='"+depId+"' isdep='1'  et='3' mobile=''>[ " + getJsLocaleMessage('group','group_common_org') + "] "+depName+"(" + getJsLocaleMessage('group','group_common_includesubunits') + ")("+strArr[0]+ getJsLocaleMessage('group','group_common_peploe') + ")</li>");
							for(var i=1;i<strArr.length;i=i+1)
							{
								var $aaa = $("#right").find("option[isdep=1][value='"+strArr[i]+"']");
								if($aaa.attr("et")==3)
								{
									var depidstr = $(window.parent.document).find("#depIdStr").val();
									$(window.parent.document).find("#depIdStr").val(depidstr.replace("e"+$aaa.val()+",",""));
								}else if($aaa.attr("et")==2)
								{
									var depidstr = $(window.parent.document).find("#depIdStr").val();
									$(window.parent.document).find("#depIdStr").val(depidstr.replace(","+$aaa.val()+",",","));
								}
								$aaa.remove();
							}
						}
				    }
				 );
			});
		}


		function checkText(ep)
		{
			if(ep.val()=="")
			{
				ep.next("label").css("display","inline");
			}else
			{
				ep.next("label").css("display","none");
			}

		}
		
		 //搜索按钮
		 function zhijieSearch()
		 {
			 var dqcheck;
				$("#left").empty();
				var depId = "";	
		        var epname = $("#epname").val();
		        if(epname == null || epname == ""){
					return;
			    }
		        var addTypes = $("#addType").val();
		        var chooseType = $("#chooseType").val();
		        var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
				$.post(ipathh+"/grp_cliGroupManage.htm", 
					{
						method:"getDepAndUserTree", 
						lgcorpcode:lgcorpcode,
						epname : epname,
						depId:depId,
						chooseType:chooseType
					}, function(result){
						var resultHtml ="";
						if(result=='null'){
							$(window.parent.document).find("#left").html(resultHtml);
							return;
						}
						var skin = $("#skin").val();
						var obj = eval('(' + result + ')');
						if(skin.indexOf("frame4.0") != -1) {
							for(var i=0;i<obj.length;i++){
								resultHtml = resultHtml+"<div class=\"memberList\" onclick=\"move2RightByClick(this)\" ondblclick=\"move2Right(this)\" datavalue=\""+obj[i].userId+"\" isdep=\"11\" et=\"\" mobile=\""+obj[i].mobile+"\">"+obj[i].name+"</div>"
							}
						}else{
							for(var i=0;i<obj.length;i++){
								resultHtml = resultHtml+"<option value='"+obj[i].userId+"' mobile='"+obj[i].mobile+"'>"+obj[i].name+"</option>"
							}
						}
						$("#left").html(resultHtml);
				});
		 }
		 function loadFn()
		 {
			 var $pa = $(window.parent.parent.document);
			var pahtm = $pa.find("#getloginUser").html();
			$("#loginUserInfo").html(pahtm);
		 }

		 function changeshow(){
			var chooseType = $("#chooseType").val();
			//$("#idPlaceholder").empty();
			//清空input标签内的值
			$("#epname").attr("value","");
			if(chooseType == "0"){
				//$("#idPlaceholder").html("请输入姓名");
				//$("#idPlaceholder").html(getJsLocaleMessage('group','group_js_sysChooseInfo_entername'));
				//改变placeholder的值
				$("#epname").attr("placeholder",getJsLocaleMessage('group','group_js_sysChooseInfo_entername'));
			}else if(chooseType == "1"){
				//$("#idPlaceholder").html("请输入手机号码");
				//改变placeholder的值
				$("#epname").attr("placeholder",getJsLocaleMessage('group','group_js_sysChooseInfo_enterphone'));
			}
		}


		function router()
		{
			if($("#left").val()!=null && $("#left").val()!="")
			{
				moveLeft();
			}else{
				aaa();
			}
		}
		