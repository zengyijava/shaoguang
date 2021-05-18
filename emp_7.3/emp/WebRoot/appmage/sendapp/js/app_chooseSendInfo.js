 //isdep表示app账户类别 1表示成员  2群组  3企业
$(document).ready(function() {
			//当前企业
			var lgcorpcode = getParentFrameElements("#lgcorpcode").val();
			//当前APP企业
			var appaccount = getParentFrameElements("#appAccount").val();
			$("#chooseType").val(1);
			$("#left").html("");
			$('#pageIndex1,#totalPage1').val(1);
			$('#showPage1').html('');
			$.post(ipath + "/app_msgsend.htm",{method:"getAppGroupList", lgcorpcode:lgcorpcode,appaccount:appaccount},function(groupList)
	   			{
					$("#showList").html(groupList);
				}
			);
			$("#showList").toggle();
			
			$('#epname').click(function(){
			$('.app_search').hide();});
			
			$('#epname').keyup(function(){
				$('.app_search').hide();
			}).blur(function(){
				if($.trim($(this).val())==''){
					$('.app_search').show();
				}
			});
			$('.app_search').click(function(){
				$(this).hide();
				$('#epname').focus();
			});
	 });
 
	var ipath = base_ipath;
	
	$(function(){
	inputTipText(); //直接调用就OK了
	})
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
		
	//搜索按钮
	 function searchAppCode()
	 {
		var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
		//当前APP企业
		var appaccount = getParentFrameElements("#appAccount").val();
		var chooseType=$("#chooseType").val();
		var epname = $("#epname").val();
		$("#groupList").val("");
		$("#corpList").val("");
		if(epname.length < 1)
		{
			//alert("请输入查询账号!");
			alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_inputselectaccount'));
			return;
		}
		$("#pageIndex1").val(1);
		$.post(ipath + "/app_msgsend.htm",{
			method:"getAllAppMembers",
			epname:epname,
			appaccount:appaccount,
			chooseType:chooseType
   			},function(AppMember){
   				//第二个@出现位置的索引
   				var index = AppMember.indexOf("@",AppMember.indexOf("@")+1);
   				var tempStr = AppMember.substring(0,index);
   				//var tempStr = AppMember.substring(0,AppMember.lastIndexOf("@"));
				var strs =new Array();
				strs =  tempStr.split("@");
				$("#totalPage1").val(strs[1]);
				$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
				var tempStr2 = AppMember.substring(index+1);
				$("#left").html(tempStr2);
			}
		);

	 }
	
	var curI = 1;
	function changeChooseType()
	{
		$("#left").html("");
		$('#pageIndex1,#totalPage1').val(1);
		$('#showPage1').html('');
		var chooseType=$("#chooseType").val();
		//当前APP企业
		var appaccount = getParentFrameElements("#appAccount").val();
		if( curI == chooseType)
		{
			return;
		}else
		{
			curI = chooseType;
		}
		if(chooseType == 1)
		{
			//当前企业
			var lgcorpcode = getParentFrameElements("#lgcorpcode").val();
			$.post(ipath + "/app_msgsend.htm",{method:"getAppGroupList", lgcorpcode:lgcorpcode,appaccount:appaccount},function(groupList)
	   			{
					$("#showList").html(groupList);
				}
			);
			//$("#showUserName").html("当前群组：");
			//$("#selectDep").val("选择群组");
			$("#showUserName").html(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_curgroup'));
			$("#selectDep").val(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_selectgroup'));
		}
		else if(chooseType == 2)
		{
			$.post(ipath + "/app_msgsend.htm",{method:"getAppCorpCodeList"},function(appCorpCodeList)
	   			{
					$("#showList").html(appCorpCodeList);
				}
			);
			//$("#showUserName").html("当前APP企业帐号：");
			//$("#selectDep").val("选择APP企业帐号");
			$("#showUserName").html(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_curappaccount'));
			$("#selectDep").val(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_selectappaccount'));
		}
	}
		
	 //选择
	function router()
	{
		var chooseType=$("#chooseType").val();
		//选择成员
		if($("#left").val()!=null && $("#left").val()!="")
		{
			chooseLeft();
		}
 		else
 		{
			//选择群组
 			if(chooseType == "1")
 			{
 				chooseGroup();
 			}
 			//选择APP企业
 			else
 			{
 				chooseAppCorpCode();
 			}
 		}
	}
	//选择上级frame的元素
	function getParentFrameElements(elements){
		return $(window.parent.document).find(elements);
	}
	
	//选择群组
	function chooseGroup(){
		var manCount = parseInt($("#manCount").html());
		var tempVal="";
		var name ="";
		//group中用于存放最终
		var group= getParentFrameElements("#group").val();
		//当前企业
		var lgcorpcode = getParentFrameElements("#lgcorpcode").val();
		//当前APP企业
		var appaccount = getParentFrameElements("#appAccount").val();
		//已选择的企业
		var appcorpcode= getParentFrameElements("#appcorpcode").val();
		//如果当前企业已选择,无法添加群组
		if(appcorpcode.indexOf(",'"+appaccount+"',") >= 0)
		{
			// alert("该群组所属企业已添加，无法重复选择！");
			alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_groupadded'));
			return;
		}
		
		if($("#groupList option:selected").size()==0)
		{
			//alert("请选择群组！");
			alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_pleaseselectgroup'));
			return;
		}
		$("#groupList option:selected").each(function() 
			{
			 	tempVal = $(this).val();
			 	name = $(this).text();
			 	if(group.indexOf(","+tempVal+",") >= 0)
				{
					//alert("该群组已添加！");
			 		alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_pleaseselectgroup'));
				}else
				{
					if($(this).attr("mcount")=="0")
			    	{
						//alert("该群组下没有成员！");
						alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_groupnouser'));
						return;
				    }
					name = name +"  ["+ $(this).attr("mcount")+ getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_peploe') + "]";
					group = group+tempVal+",";
					$("#right").append("<option value='"+tempVal+"' isdep='2' appcode='' mcount='"+$(this).attr("mcount")+"'>[ " + getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_group') + "] "+name+"</option>");
					$("#getChooseMan").append("<li dataval='"+tempVal+"' isdep='2' appcode='' mcount='"+$(this).attr("mcount")+"'>[ " + getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_group') + "] "+name+"</li>");
					manCount +=parseInt($(this).attr("mcount"));
				}
			}
		);
		$("#manCount").html(manCount);
		getParentFrameElements("#group").val(group);
		return;
	}
	
	//选择APP企业
	function chooseAppCorpCode(){
		var manCount = parseInt($("#manCount").html());
		var tempVal="";
		var name ="";
		var appcorpcode= $(window.parent.document).find("#appcorpcode").val();
		if($("#corpList option:selected").size()==0)
		{
			// alert("请选择APP企业账号！");
			alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_selectappaccount'));
			return;
		}
		$("#corpList option:selected").each(function() 
			{
			 	tempVal = $(this).val();
			 	name = $(this).text();
			 	if(appcorpcode.indexOf(",'"+tempVal+"',") >= 0)
				{
					//alert("该APP企业账号已添加！");
			 		alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_appadded'));
				}else
				{
					if($(this).attr("mcount")=="0")
			    	{
						//alert("该APP企业账号下没有成员！");
						alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_appnouser'));
						return;
				    }
					name = name +"  ["+ $(this).attr("mcount")+ getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_group') + "]";
					appcorpcode = appcorpcode+ "'" +tempVal+"',";
					$("#right").append("<option value='"+tempVal+"' isdep='3' appcode='' mcount='"+$(this).attr("mcount")+"'>[ " + getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_qiye') + "] "+name+"</option>");
					$("#getChooseMan").append("<li dataval='"+tempVal+"' isdep='3' appcode='' mcount='"+$(this).attr("mcount")+"'>[ " + getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_qiye') + "] "+name+"</li>");
					manCount +=parseInt($(this).attr("mcount"));
				}
			}
		);
		$("#manCount").html(manCount);
		$(window.parent.document).find("#appcorpcode").val(appcorpcode);
		return;
	}
	
	//选择成员
 	function chooseLeft() {
		var isRepeat = false;
		if ($("#left option:selected").length > 0) {
			var manCount = parseInt($("#manCount").html());
			$("#left option:selected").each(function() {
				var appcode = $(this).attr("appcode");
				var group =  $(this).attr("group");
				var appcorpcode =  $(this).attr("appcorpcode");
				var isExist = false;
				var info = getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_userselectsuc'); //"用户选择成功！";
				$("#right").find("option").each(function(){
					//1:成员;2:群组;3:企业
					if($(this).attr("isdep") == 1)
					{
						if($(this).attr("appcode")==appcode)
						{
							isExist = true;
							//info = "该用户已添加，无法重复选择！";
							if(!isRepeat)
							{
								isRepeat = true;
								// alert("选择记录重复，将自动过滤！");
								alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_repeatchoose'));
							}
						}
					}
					else if($(this).attr("isdep") == 2)
					{
						if($(this).val()==group)
						{
							isExist = true;
							//info = "该用户所属群组已添加，无法重复选择！";
							if(!isRepeat)
							{
								isRepeat = true;
								//alert("选择记录重复，将自动过滤！");
								alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_repeatchoose'));
							}
						}
					}
					else if($(this).attr("isdep") == 3)
					{
						if($(this).val()==appcorpcode)
						{
							isExist = true;
							//info = "该用户所属企业已添加，无法重复选择！";
							if(!isRepeat)
							{
								isRepeat = true;
								//alert("选择记录重复，将自动过滤！");
								alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_repeatchoose'));
							}
						}
					}
					});
					if (!isExist) 
					{
						var name = $(this).text();
						if($(this).text().lastIndexOf("]")==($(this).text().length-1))
						{
							name=$(this).text().substring(0,$(this).text().lastIndexOf("["));
						}
						manCount=manCount+1;
						$("#right").append("<option value=\'"+$(this).val()+"\'  isdep=\'1\' appcode=\'"+$(this).attr('appcode')+"\'>"+" [" + getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_member') + "] "+name+"</option>");
						$("#getChooseMan").append("<li dataval='"+$(this).val()+"'  isdep='1' appcode='"+$(this).attr('appcode')+"'>"+" [" + getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_member') + "] "+name+"</li>");
						$("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\' appcode=\'"+$(this).attr('appcode')+"\'>"+" [" + getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_member') + "] "+name+"</option>");
					} 
			});
			$("#manCount").html(manCount);
		} 
	}
	 
	//选中APP企业或群组,显示成员列表
	function getAppMembers()
	 {
		//当前企业
		var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
		//当前APP企业
		var appaccount = getParentFrameElements("#appAccount").val();
		var chooseType=$("#chooseType").val();
		var tempVal="";
		var name ="";
		if(chooseType == 1)
		{
			$("#groupList option:selected").each(function() 
				{
				 	tempVal = tempVal+$(this).val();
				 	name = name+$(this).text();
				}
			);
		}
		else
		{
			$("#corpList option:selected").each(function() 
				{
				 	tempVal = tempVal+$(this).val();
				 	name = name+$(this).text();
				}
			);
		}
		$("#pageIndex1").val(1);
		$.post(ipath + "/app_msgsend.htm",{
			method:"getAppMembers" ,
			id:tempVal ,
			lgcorpcode:lgcorpcode,
			appaccount:appaccount,
			chooseType:chooseType
   			},function(AppMember){
   				//第二个@出现位置的索引
   				var index = AppMember.indexOf("@",AppMember.indexOf("@")+1);
   				var tempStr = AppMember.substring(0,index);
   				//var tempStr = AppMember.substring(0,AppMember.lastIndexOf("@"));
				var strs =new Array();
				strs =  tempStr.split("@");
				$("#totalPage1").val(strs[1]);
				$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
				var tempStr2 = AppMember.substring(index+1);
				$("#left").html(tempStr2);
			}
		);
		if(name.length>9)
		{
			name = name.substr(0,9)+"...";
		}else if(name.length > 0)
		{
			name = name.substr(0,name.length-1);
		}
		//$("#showUserName").html("当前群组："+name);
		$("#showUserName").html(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_curgroup')+name);
	 }

	//选中成员,清除群组选中状态 	
	function treeLoseFocus()
	{
		$("#groupList").val("");
	}

	//删除
	function moveRight() {
		if ($("#right option:selected").size() > 0) {
			$("#right option:selected").each(function() {
				try {
					//1:成员;2:群组;3:企业
					if($(this).attr("isdep") == 1)
					{
						$("#rightSelectTemp option[value='"+$(this).val()+"']").remove();
						$("#manCount").html(parseInt($("#manCount").html())-1);
					}else if($(this).attr("isdep") == 2)
					{
						var group = $(window.parent.document).find("#group").val();
						$(window.parent.document).find("#group").val(group.replace(","+$(this).val()+",",","));
						$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
					}
					else if($(this).attr("isdep") == 3)
					{
						var appcorpcode = $(window.parent.document).find("#appcorpcode").val();
						$(window.parent.document).find("#appcorpcode").val(appcorpcode.replace(",'"+$(this).val()+"',",","));
						$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
					}
					$(this).remove();
				} catch (err) {
				}
			});
		} else {
			//alert("没有要移除的记录！");
		}
		if($('#getChooseMan li').size()>0){
			$('#getChooseMan>li').each(function(){
				if($(this).hasClass('cur')){
					$(this).remove();
				}
			})
			
		}else{
			// alert("没有要移除的记录！");
			alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_nodatatoremove'));
		}
	};	
	
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
		//当前APP企业
		var appaccount = getParentFrameElements("#appAccount").val();
		var id = "";
		var chooseType=$("#chooseType").val();
		var epname = $("#epname").val();
		var info = "";
		if(chooseType == 1)
		{
			info = getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_pleaseselectgroup') ; //"请先选择群组！";
			id = $("#groupList").val();
		}
		else
		{
			info= getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_selectqiye') ; //"请先选择企业！";
			id = $("#corpList").val();
		}
		
		var pageIndex1 = $("#pageIndex1").val();
		if(id != null && id.length > 0)
		{
			if(pageIndex1=="1")
			{
				// alert("第一页，没有上一页了！");
				alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_noprepage'));
				return;
			}
			$.post(ipath + "/app_msgsend.htm",{method:"getAppMembers" ,chooseType:chooseType, id:id,appaccount:appaccount,pageIndex1:pageIndex1,opType:"goLast"},function(result)
	   			{
				   	//第二个@出现位置的索引
   					var index = result.indexOf("@",result.indexOf("@")+1);
					$("#left").html(result.substring(index+1));
					$("#pageIndex1").val(parseInt(pageIndex1)-1);
					$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
			});
		}
		else if(epname.length > 0)
		{
			if(pageIndex1=="1")
			{
				// alert("第一页，没有上一页了！");
				alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_noprepage'));
				return;
			}
			$.post(ipath + "/app_msgsend.htm",{method:"getAllAppMembers" ,chooseType:chooseType, epname:epname,appaccount:appaccount,pageIndex1:pageIndex1,opType:"goLast"},function(result)
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
			alert(info);
			return;
		}	
	}
	function goNextPage1()
	{
		//当前APP企业
		var appaccount = getParentFrameElements("#appAccount").val();
		var id = "";
		var chooseType=$("#chooseType").val();
		var epname = $("#epname").val();
		var info = "";
		if(chooseType == 1)
		{
			info = getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_pleaseselectgroup') ; //"请先选择群组！";
			id = $("#groupList").val();
		}
		else
		{
			info= getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_selectqiye') ; //"请先选择企业！";
			id = $("#corpList").val();
		}
		var pageIndex1 = $("#pageIndex1").val();
		var totalPage1 = $("#totalPage1").val();
		if(id != null && id.length > 0)
		{
			if(pageIndex1==totalPage1)
			{
				//alert("已经最后一页了！");
				alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_nonextpage'));
				return;
			}
			$.post(ipath + "/app_msgsend.htm",{method:"getAppMembers" ,chooseType:chooseType, id:id ,appaccount:appaccount,pageIndex1:pageIndex1,opType:"goNext"},function(result)
			{
			   	//第二个@出现位置的索引
   				var index = result.indexOf("@",result.indexOf("@")+1);
				$("#left").html(result.substring(index+1));
				$("#pageIndex1").val(parseInt(pageIndex1)+1);
				$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
			});
		}
		else if(epname.length > 0)
		{
			if(pageIndex1==totalPage1)
			{
				//alert("已经最后一页了！");
				alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_nonextpage'));
				return;
			}
			$.post(ipath + "/app_msgsend.htm",{method:"getAllAppMembers" ,chooseType:chooseType, epname:epname,appaccount:appaccount,pageIndex1:pageIndex1,opType:"goNext"},function(result)
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
			alert(info);
			return;
		}
		
		
		
		
	}
			