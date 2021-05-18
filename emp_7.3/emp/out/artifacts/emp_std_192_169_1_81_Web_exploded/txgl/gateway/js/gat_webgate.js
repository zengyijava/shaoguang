function submitForm(){
	var allParamItems = $("#allParamItems").val();
	var params = new Array();
	var isSubmit = false;
	params = allParamItems.split(",");
	if(allParamItems==""){
		//alert("该模板未有任何属性，无法提交！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_5"));
	}else{
		var $nullParam;
		for(var i=0;i<params.length && isSubmit== false;i=i+1){
			if(params[i] != null && params[i]!="" && "RESNDRPTERCODE" != params[i] ){
				var $ob = $("#"+params[i]);
				/*if($ob.attr("type")=="checkbox" && $("#"+params[i]+":checked").val()==undefined ){
					isSubmit = true;
				}else*/ 
				//本地数据文件目录和共享数据文件目录允许为空
				if($ob.val()=="" && $ob.attr("type")!="checkbox"&&$ob.attr("name")!="6CLU1DATAFILELPATH"&&$ob.attr("name")!="6CLU1DATAFILERPATH"&&$ob.attr("name")!="FILE07SHAREDIR"){
					isSubmit = true;
					$nullParam = $ob;
				}else if($ob.attr("type")=="text" && $ob.attr("class")=="numInput"){
					var patrn=/^[0-9]*$/;
					var valu = $ob.val();
					if(!patrn.test(valu))
					{
						//alert($ob.attr("name")+"输入的值必须是数字！");
						alert($ob.attr("name")+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_6"));
						$ob.select();
						return;
					}else
					if($ob.next("label").text()!=""){
						$ob.val(valu-0);
						var range = $ob.next("label").text().split("-");
						if(range.length>1){
							if((range[0]!= "" && $ob.val()-range[0]<0)
								|| (range[1]!= "" && $ob.val()-range[1]>0))
							{
								//alert($ob.attr("name")+"的值不在取值范围内，请重新输入！");
								alert($ob.attr("name")+getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_7"));
								$ob.select();
								return;
							}
						}
					}
				}
			}
			//网关运行参数配置不能输入&符号
			if($ob.attr("type")=="text"){
				var sear=new RegExp('&'); 
				if(sear.test($ob.val())){
					//alert("网关运行参数配置输入的值不能包含&符号！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_8"));
					$ob.select();
					return;
				}
			}
		}
	}
	if(isSubmit){
		//alert("存在未填或未选项，无法提交！");
		alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_9"));
		$nullParam .select();
	}else{ 
		var allParamValues = "";
		var specItems = "STOPMTTMRLVLRD,STOPMTLVLRD,STOPMTLVLSD";
		for(var i=0;i<params.length;i=i+1){
			if(params[i] != null && params[i]!="")
			{
				var $ob = $("#"+params[i]);
				if($ob.attr("type")=="checkbox" )
				{
					var boxlength = $("input[name='"+params[i]+"']").length;
					for(var o=0;o<boxlength;o++)
					{
						if($("input:checkbox[name='"+params[i]+"']").eq(o).attr("checked")==true)
						{
							allParamValues+="1";
						}else
						{
							allParamValues+="0";
						}
						if(o<boxlength-1)
						{
							allParamValues+="-";
						}else
						{
							allParamValues+="&";
						}
					}
				}else{
					allParamValues += $ob.val().replaceAll("&","$$")+"&";
				}
			}
		}
		//alert(allParamValues+" "+allParamItems);
		$.post("gat_webgate.htm?method=update",
			{
				allParamItems: allParamItems, allParamValues : allParamValues,
				gwType : $("#gwType").val(),
				keyId : $("#keyId").val(),
				gwNo : $("#gwNo").val()
			},
			function(result){
				if(result == "true"){
					//alert("保存成功！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_10"));
					//document.forms[0].reset();
				}else if(result == "error"){
					//alert("保存失败！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_11"));
				}else if(result == "false"){
					//alert("保存失败！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_11"));
				}
			}
		);
	}
}