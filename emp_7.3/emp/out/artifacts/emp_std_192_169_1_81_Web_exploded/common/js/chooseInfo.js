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
							if($(this).val().indexOf("m_")>-1)
							{
								manCount=manCount+1;
                                $("#right").append("<option value=\'"+$(this).val()+"\' et=\'5\'  mobile=\'"+$(this).attr('mobile')+"\' mcount=\'1\'>"+" ["+getJsLocaleMessage("common","common_selfBuild")+"] "+name+"</option>");
                                $("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\' mobile=\'"+$(this).attr('mobile')+"\' mcount=\'1\'>"+" ["+getJsLocaleMessage("common","common_selfBuild")+"] "+name+"</option>");
							}else
							{
								manCount=manCount+1;
                                $("#right").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ "+getJsLocaleMessage("common","common_staff")+"] "+lops[i].text+"</option>");
                                $("#rightSelectTemp").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ "+getJsLocaleMessage("common","common_staff")+"] "+lops[i].text+"</option>");
							}
						} else {
							if(alertNum==1){
                                /*选择记录重复，将自动过滤！*/
                                alert(getJsLocaleMessage("common","common_errorInfo_text_1"));
							}
						}
					});
					$("#manCount").html(manCount);
				} 
			}


			function moveRight() {
				if ($("#right option:selected").size() > 0) {
					$("#right option:selected").each(function() {
						try {
							//如果是1员工成员2是机构3是机构（包含子机构）4群组
							if($(this).attr("et") == 1)
							{
								$("#rightSelectTemp option[value='"+$(this).val()+"']").remove();
								$("#manCount").html(parseInt($("#manCount").html())-1);
							}else if($(this).attr("et") == 3){
								var depidstr = $(window.parent.document).find("#depIdStr").val();
								$(window.parent.document).find("#depIdStr").val(depidstr.replace("e"+$(this).val()+",",""));
								$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
							}else if($(this).attr("et") == 2)
							{
								var depidstr = $(window.parent.document).find("#depIdStr").val();
								$(window.parent.document).find("#depIdStr").val(depidstr.replace(","+$(this).val()+",",","));
								$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
							}else if($(this).attr("et") == 4)
							{
								var depidstr = $(window.parent.document).find("#groupStr").val();
								$(window.parent.document).find("#groupStr").val(depidstr.replace(","+$(this).val()+",",","));
								$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
							}
							$(this).remove();
						} catch (err) {
						}
					});
				} else {
					//alert("没有要移除的记录！");
                    alert(getJsLocaleMessage("common","common_errorInfo_text_2"));
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
                    /*没有要移除的记录！*/
                    alert(getJsLocaleMessage("common","common_errorInfo_text_2"));
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
                                    /*记录已添加!*/
                                    alert(rops[i].text + " "+getJsLocaleMessage("common","common_errorInfo_text_3"));
									return;
								}
							}
						}
					}
					for ( var i = 0; i < lops.length; i=i+1) {
						if(lops[i].value.indexOf("m_")>-1)
						{
                            $("#right").append("<option value=\'"+$(this).val()+"\' et=\'5\'  mobile=\'"+$(this).attr('mobile')+"\' mcount=\'1\'>"+" ["+getJsLocaleMessage("common","common_selfBuild")+"] "+name+"</option>");
                            $("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\' mobile=\'"+$(this).attr('mobile')+"\' mcount=\'1\'>"+" ["+getJsLocaleMessage("common","common_selfBuild")+"] "+name+"</option>");
						}else
						{
                            $("#right").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ "+getJsLocaleMessage("common","common_staff")+"] "+lops[i].text+"</option>");
                            $("#rightSelectTemp").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ "+getJsLocaleMessage("common","common_staff")+"] "+lops[i].text+"</option>");
						}
					}
			}
