   $(document).ready(function(){
		    	$("#left").dblclick(function(){
		    		moveLeft();
		    	});
		    	$("#right").dblclick(function(){
		    		moveRight();
		    	});
		    });
		 
		 function chooseType(type){
			 $(window.parent.document).find("#type").val(type);
		 }

		 function moveLeft() {
				if ($("#left option:selected").length > 0) {
					var manCount = parseInt($("#manCount").html());
					var alertNum=0;
					var repate=0;
					$("#left option:selected").each(function() {
						var tempVal = $(this).val();//当前树的id
						var isExist = false;
						//已经绑定的属性
						var ismove=$(this).attr("ismove");
						//如果之前已经绑定，选择删除，再重新选择后，左边要恢复到灰色，不可以选择的状态
						if(ismove=="yes"&&modif){//并且是修改状态，才会变成黑色，否在
							$(this).css("color","#CDCDCD"); 
							$(this).attr("ismove","not");
						}else if(ismove=="not"){
							if(repate==0){
								alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtwgl_text_14"));
							}
							repate=repate+1;
							return;
						} 	
						//是否禁用
						var forbid=$(this).attr("forbid");
						
						$("#right").find("option").each(function(){
							if ($(this).val() == tempVal)
							{
								isExist = true;
								alertNum=alertNum+1;
							}
						});
						var name = $(this).text();
						if (!isExist) {
								manCount=manCount+1;
								$("#right").append("<option value=\'"+$(this).val()+"\' >"+name+"</option>");
								if(forbid=="yes"){
									$("#getChooseMan").append("<li dataval='"+$(this).val()+"'>"+name+"<font color='red'>"+getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_5")+"</font></li>");
								}else{
									$("#getChooseMan").append("<li dataval='"+$(this).val()+"'>"+name+"</li>");
								}
								
								$("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\'>"+name+"</option>");
						} else {
							if(alertNum==1){
								alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_46"));
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
								$("#rightSelectTemp option[value='"+$(this).val()+"']").remove();
								var leftoption= $("#left option[value='"+$(this).val()+"']");
								//处理当已经绑定并且在选择框右边，当用户选择删除，那么左边灰色要变黑色，并且可以点击
								leftoption.css("color","black"); 
								leftoption.attr("ismove","yes");//仅仅作为已经选择，移动到左边的处理
								$("#manCount").html(parseInt($("#manCount").html())-1);
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
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_48"));
				}
				
			};
			
			function moveallRight()
			{
				var rops = $("#right option");
				if(rops.length > 0)
				{
					$("#right").html("");
					$("#getChooseMan").html("");
					$(window.parent.document).find("#depIdStr").val(",");
					$(window.parent.document).find("#groupStr").val(",");
					$("#rightSelectTemp").html("");
				}
				else
				{
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_48"));
				}
			}
