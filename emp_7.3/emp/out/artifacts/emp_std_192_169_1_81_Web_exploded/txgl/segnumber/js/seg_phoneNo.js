	//取消
	function btcancel()
	{
		$('#editNo').dialog('close');	
	}
	
	function edit(id,name,num, keyId){
		$('#editNo').dialog({
			autoOpen: false,
			height: 235,
			width: 500,
			modal:true
		});
		$("#spisuncm").attr("value",name);
		$("#spid").attr("value",id);
		$("#keyId").attr("value",keyId);
		$("#phoneNo").val(num);
		$('#editNo').dialog('open');
	}
	
	function editNo() { 
		$("#bt1").attr("disabled", true);
		$("#bt2").attr("disabled", true);
		var pathUrl = $("#pathUrl").attr("value");
	    var phoneNo = $.trim($("#phoneNo").val());
	    //移动
	    var phoneNoYd = $.trim($("#1").val());
	    //联通
	    var phoneNoLt = $.trim($("#0").val());
	    //电信
	    var phoneNoDx = $.trim($("#21").val());
	    var spid = $("#spid").attr("value");
	    var keyId = $("#keyId").attr("value");
		if(phoneNo.length==0){
			//alert("号段值不能为空！");
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_2"));
			$("#bt1").attr("disabled", false);
			$("#bt2").attr("disabled", false);
			return;
		}
		else
		{
			var l = phoneNo.length;
			var phoneNoStr;
			if(","==phoneNo.charAt(l-1))
			{
				phoneNo = phoneNo.substring(0,(l-1));
			}
			
	        var a = phoneNo.split(",");
	        if(spid=="0")
	        {
	        	phoneNoStr=phoneNoYd+","+phoneNoDx+","+phoneNo;
	        }
	        else if(spid=="21")
	        {
	        	phoneNoStr=phoneNoYd+","+phoneNoLt+","+phoneNo;
	        }
	        else
	        {
	        	phoneNoStr=phoneNoDx+","+phoneNoLt+","+phoneNo;
	        }
	        if(a != null && a.length > 1)
		        {
		            for(var i=0;i<a.length;i++)
		            {
		            	if(a[i].length!=3&&a[i].length!=4)
		            	{
		            		//alert("输入的是错误号段,请修改！");
		            		alert(getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_3"));
		            		$("#bt1").attr("disabled", false);
							$("#bt2").attr("disabled", false);
		            		return;
		            	}
				        if(a[i].length==4)
				        {
				        	var ph=a[i];
				        	//if(ph.indexOf("170")!=0)
				        	//{
				        		var hd=ph.substr(0,3);
				        		if((phoneNoStr.split(hd+"")).length>2)
				        		{
				        			if(phoneNoStr.indexOf(","+hd+",")>0||phoneNoStr.indexOf(hd+",")==0)
				        			{
					        			//alert(hd+"号段已存在，不允许添加"+a[i]+"号段，请修改！");
				        				alert(hd+getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_4")+a[i]+getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_5"));
		        						$("#bt1").attr("disabled", false);
										$("#bt2").attr("disabled", false);
					        			return;
				        			}
				        		}
				        	//}
				        }
		            	
		            }  
		            for(var i=0;i<a.length;i++)
		            {
			            if((phoneNoStr.split(a[i]+"")).length>2)
			            {
			            	
			            	//alert(a[i]+"号段重复输入，请修改！");
			            	alert(a[i]+getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_6"));
        					$("#bt1").attr("disabled", false);
							$("#bt2").attr("disabled", false);
			            	return;
			            }
		            }
		        }
		}
		
		//if(confirm("确定修改吗？")){ 
		if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_7"))){
			$.ajax({
				dataType : "text",
				type: "POST",
				url: pathUrl+"/seg_phoneNo.htm?method=update",
				data: "phoneNo="+phoneNo+"&spid="+spid+"&keyId="+keyId,
				success: function(result){
					if(result == "true"){
				 		//alert("修改成功！");
						alert(getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_8"));
				 		$('#editNo').dialog('close');
				 		window.location = location;
					}else{
						//alert("修改失败！");
						alert(getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_9"));
					}
	   			}
			});
		}
		$("#bt1").attr("disabled", false);
		$("#bt2").attr("disabled", false);
	}  