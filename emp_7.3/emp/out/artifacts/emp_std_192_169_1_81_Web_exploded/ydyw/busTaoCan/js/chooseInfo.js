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
					$("#left option:selected").each(function() {
						var tempVal = $(this).val();//当前树的id
						var isExist = false;
						
						var taocan_name=$(this).attr("taocan_name");
						var taocan_code=$(this).attr("taocan_code");
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
								$("#right").append("<option value=\'"+$(this).val()+"\' taocan_name=\'"+taocan_name+"\' taocan_code=\'"+taocan_code+"\' >"+name+"</option>");
								$("#getChooseMan").append("<li dataval='"+$(this).val()+"' taocan_name=\'"+taocan_name+"\' taocan_code=\'"+taocan_code+"\' >"+name+"</li>");
								$("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\' taocan_name=\'"+taocan_name+"\' taocan_code=\'"+taocan_code+"\' >"+name+"</option>");
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
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_20"));
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
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_20"));
				}
			}
			
			//点击选择
			function router()
			{
				if($("#left").val()!=null && $("#left").val()!="")
				{
					moveLeft();
				}else{
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_19"));
					return;
				}
			}	