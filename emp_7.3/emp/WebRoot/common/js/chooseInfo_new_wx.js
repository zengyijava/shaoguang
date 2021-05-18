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
				var gtype = $("#qztype").val(); //判断组类型
				var types = $("#chooseType").val();//判断类型
			 		/*员工*/
				var showtype=getJsLocaleMessage("common","common_staff");
				if(types == "2" && gtype == "2"||gtype == "3"){
                    /*客户*/
					showtype=getJsLocaleMessage("common","common_client");
				}
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
								/*自建*/
								$("#right").append("<option value=\'"+$(this).val()+"\' et=\'5\'  mobile=\'"+$(this).attr('mobile')+"\' mcount=\'1\'>"+" ["+getJsLocaleMessage("common","common_selfBuild")+"] "+name+"</option>");
								$("#getChooseMan").append("<li dataval='"+$(this).val()+"' et='5'  mobile='"+$(this).attr('mobile')+"' mcount=\'1\'>"+" ["+getJsLocaleMessage("common","common_selfBuild")+"] "+name+"</li>");
								$("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\' mobile=\'"+$(this).attr('mobile')+"\' mcount=\'1\'>"+" ["+getJsLocaleMessage("common","common_selfBuild")+"] "+name+"</option>");
							}else
							{
								manCount=manCount+1;
								$("#right").append("<option value=\'"+$(this).val()+"\'  et=\'5\' mobile=\'"+$(this).attr('mobile')+"\' mcount=\'1\'>"+" ["+showtype+"] "+name+"</option>");
								$("#getChooseMan").append("<li dataval='"+$(this).val()+"'  et='5' mobile='"+$(this).attr('mobile')+"' mcount=\'1\'>"+" ["+showtype+"] "+name+"</li>");
								$("#rightSelectTemp").append("<option value=\'"+$(this).val()+"\' mobile=\'"+$(this).attr('mobile')+"\' mcount=\'1\'>"+" ["+showtype+"] "+name+"</option>");
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
				
				if($("#right option:selected").size() == 0){
			 		return;
			 	}
			 	//员工机构IDS
				var empDepIdsStrs = $("#empDepIdsStrs").val();
				//客户机构IDS
			 	var cliDepIdsStrs = $("#cliDepIdsStrs").val();
			 	//群组IDS
			 	var groupIdsStrs = $("#groupIdsStrs").val();
			 	//处理客户群组
			 	var groupClientStr=$("#groupClientStr").val();
			 	//用户的 手机号码集合 
			 	var moblieStrs = $("#moblieStrs").val();
			 	
			 	$("#right option:selected").each(function(){
			 		//成员GUID
			 		var id = $(this).val();
			 		//这里判断成员类型  是1员工机构  2客户机构 3群组 4员工5客户6外部人员 
			 		var isdep = $(this).attr("isdep");
			 		var moblie = $(this).attr("moblie");
			 		var name = $(this).text();
			 		//1表示机构包含关系    2表示不包含关系
			 		var et =  $(this).attr("et");
			 		if(isdep == "1"){
			 			//处理员工机构 	
			 			if(et == "3")
						{
							empDepIdsStrs = empDepIdsStrs.replace("e"+id+"," , "");
						}else if(et == "2")
						{
							empDepIdsStrs = empDepIdsStrs.replace(id+"," , "");
						}
			 			$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
			 		}else if(isdep == "2"){
			 			//处理客户机构
			 			if(et == "2")
						{
			 				cliDepIdsStrs = cliDepIdsStrs.replace("e"+id+"," , "");
						}else if(et == "1")
						{
							cliDepIdsStrs = cliDepIdsStrs.replace(id+"," , "");
						}
			 			$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
			 		}else if(isdep == "3"){
			 			//处理外群组
			 			if(groupIdsStrs.indexOf(id+",") >= 0)
						{
			 				groupIdsStrs = groupIdsStrs.replace(id+"," , "");
			 				$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
						}
			 			
			 			if(groupClientStr.indexOf(id+",") >= 0)
						{
			 				groupClientStr = groupClientStr.replace(id+"," , "");
			 				$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
						}
			 		}else if(et == "5" && null == isdep){
			 			
			 			$("#rightSelectTemp option[value='"+$(this).val()+"']").remove();
						$("#manCount").html(parseInt($("#manCount").html())-1);
						
			 		}
			 		
			 		$(this).remove();
			 		if($('#getChooseMan li').size()>0){
						$('#getChooseMan>li').each(function(){
							if($(this).hasClass('cur')){
								$(this).remove();
							}
						})
					}
			 	});
				$("#empDepIdsStrs").val(empDepIdsStrs);
			 	$("#cliDepIdsStrs").val(cliDepIdsStrs);
			 	$("#groupIdsStrs").val(groupIdsStrs);
			 	$("#groupClientStr").val(groupClientStr);
			 	$("#moblieStrs").val(moblieStrs);
				
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
							/*自建*/
							$("#right").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ "+getJsLocaleMessage("common","common_selfBuild")+"] "+lops[i].text+"</option>");
							$("#getChooseMan").append("<li dataval='"+lops[i].value+"'   mobile='"+lops[i].mobile+"'>"+" [ "+getJsLocaleMessage("common","common_selfBuild")+"] "+lops[i].text+"</li>");
							$("#rightSelectTemp").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ "+getJsLocaleMessage("common","common_selfBuild")+"] "+lops[i].text+"</option>");
						}else
						{
							/*员工*/
							$("#right").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ "+getJsLocaleMessage("common","common_staff")+"] "+lops[i].text+"</option>");
							$("#getChooseMan").append("<li dataval=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ "+getJsLocaleMessage("common","common_staff")+"] "+lops[i].text+"</li>");
							$("#rightSelectTemp").append("<option value=\'"+lops[i].value+"\' mobile=\'"+lops[i].mobile+"\'>"+" [ "+getJsLocaleMessage("common","common_staff")+"] "+lops[i].text+"</option>");
						}
					}
			}
