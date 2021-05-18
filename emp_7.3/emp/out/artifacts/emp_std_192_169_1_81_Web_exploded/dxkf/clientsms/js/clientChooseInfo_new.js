   $(document).ready(function(){
		    	$("#left").click(function(){
				    	$("#treeFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
			    	});
		    	$("#left").dblclick(function(){
		    		moveLeft();
			    	$("#treeFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
		    	});
		    	$("#right").dblclick(function(){
		    		moveRight();
		    	});
		    });
		 
		 function chooseType(type){
			 $(window.parent.document).find("#type").val(type);
		 }

		 function moveLeft() {
				var path = $("#ipath").val();
				if ($("#left option:selected").length > 0) {
					var manCount = parseInt($("#manCount").html());
					var alertNum=0;
					$("#left option:selected").each(function() {
						var tempVal = $(this).val();//当前树的id
						var isExist = false;
						$("#right").find("option").each(function(){
							if ($(this).val() == tempVal)
							{
								isExist = true;
								alertNum=alertNum+1;
							}
						});
						var name = $(this).text();
						if($(this).text().lastIndexOf("]")==($(this).text().length-1))
						{
							name=$(this).text().substring(0,$(this).text().lastIndexOf("["));
						}
						if (!isExist) {
								var types = $("#chooseType").attr("value");
								if(types == 3){
									var path = $("#pathUrl").val();
									var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
									$.post(path+"/kfs_sendClientSMS.htm?method=getCustFieldMemberCount",{
										//method:"getCustFieldMemberCount" ,
										fieldValue:tempVal,
										lgcorpcode:lgcorpcode
							   			},function(count){
											var curCount = count;
											if(curCount == 0){
												alertNum=alertNum+1;
												if(alertNum==1){
													// alert("选择重复记录或没有成员的记录，将自动过滤！");
													alert(getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_guolvcf'));
												}
											}else{
												manCount = parseInt(manCount) + parseInt(curCount);
												//var tempVal = $(this).val();
												var proValueIdStr= $(window.parent.document).find("#proValueIdStr").val();
												proValueIdStr = proValueIdStr+tempVal+",";
												$("#right").append("<option value=\'"+tempVal+"\'  et=\'6\' mobile='' mcount='"+curCount+"'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_sxz') + "] "+name+"["+curCount + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole') + "]</option>");
												$("#getChooseMan").append("<li dataval='"+tempVal+"' et='6'  mobile='' mcount='"+curCount+"'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_sxz') + "] "+name+"["+curCount + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole') + "]</li>");
												$("#rightSelectTemp").append("<option value=\'"+tempVal+"\' mobile='' mcount='"+curCount+"'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_sxz') + "] "+name+"["+curCount + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole') + "]</option>");
												$(window.parent.document).find("#proValueIdStr").val(proValueIdStr);
												$("#manCount").html(manCount);
											}
							   			}
									);
									
								}else{
									if(types == 4){
										manCount=manCount+1;
										$("#right").append("<option value=\'"+$(this).val()+"\'  et=\'7\' mobile=\'"+$(this).attr("mobile")+"\'>"+" [" + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_wechat') + "] "+name+"</option>");
										$("#getChooseMan").append("<li dataval=\'"+$(this).val()+"\'  et=\'7\' mobile=\'"+$(this).attr("mobile")+"\'>"+" [" + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_wechat') + "] "+name+"</li>");
										$("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\'  et=\'7\'  mobile=\'"+$(this).attr("mobile")+"\'>"+" [" + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_wechat') + "] "+name+"</option>");
										$("#manCount").html(manCount);
									}else if(types==5){
										manCount=manCount+1;
										$("#right").append("<option value=\'"+$(this).val()+"\'  et=\'8\' mobile=\'"+$(this).attr("mobile")+"\'>"+" [" + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_signuser') + "] "+name+"</option>");
										$("#getChooseMan").append("<li dataval=\'"+$(this).val()+"\'  et=\'8\' mobile=\'"+$(this).attr("mobile")+"\'>"+" [" + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_signuser') + "] "+name+"</li>");
										$("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\'  et=\'8\'  mobile=\'"+$(this).attr("mobile")+"\'>"+" [" + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_signuser') + "] "+name+"</option>");
										$("#manCount").html(manCount);
									}else{
										manCount=manCount+1;
										$("#right").append("<option value=\'"+$(this).val()+"\'  et=\'1\' mobile=\'"+$(this).attr("mobile")+"\'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_user') + "] "+name+"</option>");
										$("#getChooseMan").append("<li dataval=\'"+$(this).val()+"\'  et=\'1\' mobile=\'"+$(this).attr("mobile")+"\'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_user') + "] "+name+"</li>");
										$("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\'  et=\'1\'  mobile=\'"+$(this).attr("mobile")+"\'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_user') + "] "+name+"</option>");
										$("#manCount").html(manCount);
									}
								}
						} else {
							if(alertNum==1){
								// alert("选择重复记录或没有成员的记录，将自动过滤！");
								alert(getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_guolvcf'));
							}
						}
					});
				} 
			}


			function moveRight() {
				if ($("#right option:selected").size() > 0) {
					$("#right option:selected").each(function() {
						try {
							//如果是1客户成员2是机构3是机构（包含子机构）4群组5客户属性 6属性值
							//单个用户
							if($(this).attr("et") == 1)
							{
								$("#rightSelectTemp option[value='"+$(this).val()+"']").remove();
								$("#manCount").html(parseInt($("#manCount").html())-1);
							//包含机构
							}else if($(this).attr("et") == 3){
								var depidstr = $(window.parent.document).find("#depIdStr").val();
								$(window.parent.document).find("#depIdStr").val(depidstr.replace(",e"+$(this).val()+",",","));
								$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
							//不包含的机构
							}else if($(this).attr("et") == 2)
							{
								var depidstr = $(window.parent.document).find("#depIdStr").val();
								$(window.parent.document).find("#depIdStr").val(depidstr.replace(","+$(this).val()+",",","));
								$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
							//群组 
							}else if($(this).attr("et") == 4)
							{
								var depidstr = $(window.parent.document).find("#groupStr").val();
								$(window.parent.document).find("#groupStr").val(depidstr.replace(","+$(this).val()+",",","));
								$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
							//客户属性
							}else if($(this).attr("et") == 5)
							{
								var depidstr = $(window.parent.document).find("#proIdStr").val();
								$(window.parent.document).find("#proIdStr").val(depidstr.replace(","+$(this).val()+",",","));
								$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
							//客户属性值
							}else if($(this).attr("et") == 6)
							{
								var depidstr = $(window.parent.document).find("#proValueIdStr").val();
								$(window.parent.document).find("#proValueIdStr").val(depidstr.replace(","+$(this).val()+",",","));
								$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
							}else if($(this).attr("et") == 7)
							{
								$("#rightSelectTemp option[value='"+$(this).val()+"']").remove();
								$("#manCount").html(parseInt($("#manCount").html())-1);
							//签约用户
							}else if($(this).attr("et") == 8){
								$("#rightSelectTemp option[value='"+$(this).val()+"']").remove();
								$("#manCount").html(parseInt($("#manCount").html())-1);
							//签约套餐
							}else if($(this).attr("et") == 9){
								var depidstr = $(window.parent.document).find("#ydywGroupStr").val();
								$(window.parent.document).find("#ydywGroupStr").val(depidstr.replace(","+$(this).val()+",",","));
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
				}
			};
			
			function moveallRight()
			{
				var rops = $("#right option");
				if(rops.length > 0)
				{
					$("#right").html("");
					$(window.parent.document).find("#depIdStr").val(",");
					$(window.parent.document).find("#groupStr").val(",");
					$("#rightSelectTemp").html("");
				}
				else
				{
					// alert("没有要移除的记录！");
					alert(getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_nodatatoremove'));
				}
			}
			
			function moveallLeft() {
					var rops = $("#right option");
					var lops = $("#left option");
					var flag = false;
					if (rops.length > 0) {
						var rec = "";
						for ( var i = 0; i < rops.length; i=i+1) {
							var rv = rops[i].value;
							for ( var j = 0; j < lops.length; j=j+1) {
								var lv = lops[j].value;
								if (rv == lv) {
									// alert(rops[i].text + " 记录已添加!");
									alert(rops[i].text + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_jlytj'));
									return;
								}
							}
						}
					}
					for ( var i = 0; i < lops.length; i=i+1) {
						if(lops[i].value.indexOf("m_")>-1)
						{
							$("#right").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_domyself') + "] "+lops[i].text+"</option>");
							$("#getChooseMan").append("<li dataval=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_domyself') + "] "+lops[i].text+"</li>");
							$("#rightSelectTemp").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_domyself') + "] "+lops[i].text+"</option>");
						}else
						{
							$("#right").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_employee') + "] "+lops[i].text+"</option>");
							$("#getChooseMan").append("<li dataval=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_employee') + "] "+lops[i].text+"</li>");
							$("#rightSelectTemp").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ " + getJsLocaleMessage('dxkf','dxkf_dxqf_clientchooseinfo_employee') + "] "+lops[i].text+"</option>");
						}
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
});

function treeLoseFocus()
{
				window.frames['sonFrame'].returnZTree().cancelSelectedNode();
				$("#groupList").val("");
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

