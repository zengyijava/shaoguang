   $(document).ready(function(){
	   			//业务的
		    	$("#left").dblclick(function(){
		    		moveLeft();
		    	});
		    	$("#right").dblclick(function(){
		    		moveRight();
		    	});
		    	//sp账号的
		    	$("#leftSp").dblclick(function(){
		    		moveLeftSp();
		    	});
		    	$("#rightSp").dblclick(function(){
		    		moveRightSp();
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
								alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_69"));
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
									$("#getChooseMan").append("<li dataval='"+$(this).val()+"'>"+name+"<font color='red'>"+getJsLocaleMessage("xtgl","xtgl_cswh_twgl_70")+"</font></li>");
								}else{
									$("#getChooseMan").append("<li dataval='"+$(this).val()+"'>"+name+"</li>");
								}
								
								$("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\'>"+name+"</option>");
						} else {
							if(alertNum==1){
								alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_71"));
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
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_72"));
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
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_72"));
				}
			}
			
			 function moveLeftSp() {
					if ($("#leftSp option:selected").length > 0) {
						var manCountSp = parseInt($("#manCountSp").html());
						var alertNumSp=0;
						var repateSp=0;
						$("#leftSp option:selected").each(function() {
							var tempValSp = $(this).val();//当前树的id
							var isExistSp = false;
							//已经绑定的属性
							var ismoveSp=$(this).attr("ismove");
							//如果之前已经绑定，选择删除，再重新选择后，左边要恢复到灰色，不可以选择的状态
							if(ismoveSp=="yes"&&modif){//并且是修改状态，才会变成黑色，否在
								$(this).css("color","#CDCDCD"); 
								$(this).attr("ismove","not");
							}else if(ismoveSp=="not"){
								if(repateSp==0){
									alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_73"));
								}
								repateSp=repateSp+1;
								return;
							} 	
							//是否禁用
							//var forbidSp=$(this).attr("forbid");
							
							$("#rightSp").find("option").each(function(){
								if ($(this).val() == tempValSp)
								{
									isExistSp = true;
									alertNumSp=alertNumSp+1;
								}
							});
							var nameSp = $(this).text();
							if (!isExistSp) {
									manCountSp=manCountSp+1;
									$("#rightSp").append("<option value=\'"+$(this).val()+"\' >"+nameSp+"</option>");
									/*
									if(forbidSp=="yes"){
										//$("#getChooseManSp").append("<li dataval='"+$(this).val()+"'>"+nameSp+"<font color='red'>已禁用</font></li>");
									}else{
										$("#getChooseManSp").append("<li dataval='"+$(this).val()+"'>"+nameSp+"</li>");
									}
									*/
									$("#getChooseManSp").append("<li dataval='"+$(this).val()+"'>"+nameSp+"</li>");
									$("#rightSelectTempSp").append("<option value=\'"+$(this).val()+"\'>"+nameSp+"</option>");
							} else {
								if(alertNumSp==1){
									alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_71"));
								}
							}
						});
						$("#manCountSp").html(manCountSp);
					} 
				}


				function moveRightSp() {
					
					if ($("#rightSp option:selected").size() > 0) {
						
						$("#rightSp option:selected").each(function() {
							try {
									$("#rightSelectTempSp option[value='"+$(this).val()+"']").remove();
									var leftoptionSp= $("#leftSp option[value='"+$(this).val()+"']");
									//处理当已经绑定并且在选择框右边，当用户选择删除，那么左边灰色要变黑色，并且可以点击
									leftoptionSp.css("color","black"); 
									leftoptionSp.attr("ismove","yes");//仅仅作为已经选择，移动到左边的处理
									$("#manCountSp").html(parseInt($("#manCountSp").html())-1);
								$(this).remove();
							} catch (err) {
							}
						});
						
						
					} else {
						//alert("没有要移除的记录！");
					}
					if($('#getChooseManSp li').size()>0){
						$('#getChooseManSp>li').each(function(){
							if($(this).hasClass('cur')){
								$(this).remove();
							}
						})
						
					}else{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_72"));
					}
					
				};
			
			
			
			function moveallRightSp()
			{
				var rops = $("#rightSp option");
				if(rops.length > 0)
				{
					$("#rightSp").html("");
					$("#getChooseManSp").html("");
					$(window.parent.document).find("#depIdStr").val(",");
					$(window.parent.document).find("#groupStr").val(",");
					$("#rightSelectTempSp").html("");
				}
				else
				{
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_twgl_72"));
				}
			}
			
			
			//由于添加SP帐号后，在新建和修改时，SP帐号选择后无法从select框的右边移到左边，所以这里要处理下
			//-------------对getChooseManSp进行处理--------------------
			//ul模拟select
			$('#getChooseManSp').live('click',function(e){
			   if($(this).find('li').size()>0){
			     var target = e.target,$target = $(target);
			     if (target.nodeName === 'LI') {
				    if($target.hasClass('cur')){
				      $target.removeClass('cur');
				      $('#rightSp').find('option').each(function(){
				        if($(this).val()==$target.attr('dataval')){
				          $(this).attr('selected',false);
				         
				        }
				      })
				    }else{
				      $target.addClass('cur');
				      $('#rightSp').find('option').each(function(){
				        if($(this).val()==$target.attr('dataval')){
				          $(this).attr('selected','selected');
				        }
				      })
				    }
				  }
			     
			   }
			})