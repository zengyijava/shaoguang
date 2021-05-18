$(document).ready(function() {
    	$('#setPageField').dialog({
			autoOpen: false,
			height: 400,
			width: 800,
			resizable:false,
			modal:true,
			close:function(){
				$("#setPageField").find("input[type='text']").val("");
				$("#setPageField").find("select").each(function(){
					$(this).find("option").first().attr("selected","selected");
				});
			}
		});

    	$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
		}, function() {
			$(this).removeClass("hoverColor");
		});
    	initPage(total,pageIndex,pageSize,totalRec);

    });
	var isFlag;
  	function doAdd(addFlag)
	{
		isFlag = addFlag;
		$("#fieldid").attr("disabled",true);
	    $("#fieldid").css("background","Silver");
	    $("#isfield").attr("value","1");
   		$("#subfieldvalue").attr("disabled",true);
   		$("#subfieldvalue").css("background","Silver");
   		$("#subfieldname").attr("disabled",true);
   		$("#subfieldname").css("background","Silver");
   		$("#sortvalue").attr("disabled",true);
   		$("#sortvalue").css("background","Silver");
   		$("#fieldtype").attr("disabled",false);
   		$("#fieldtype").css("background","white");
   		
		$('#setPageField').dialog('open');	
	}

    function doEdit(modiFlag,modleid, pageid,fieldid,fieldname,field,fieldtype,subfieldvalue,subfieldname,filedshow,subfield,defaultvalue,sortvalue,isfield)
	{
   		//判断如果不为子项“控件子项值”、“控件子项名称”、“子项排序值”不可输
   		if(isfield == 1)
   		{
    		$("#subfieldvalue").attr("disabled",true);
    		$("#subfieldvalue").css("background","Silver");
    		$("#subfieldname").attr("disabled",true);
    		$("#subfieldname").css("background","Silver");
    		$("#sortvalue").attr("disabled",true);
    		$("#sortvalue").css("background","Silver");
   		}
   		else
   		{
    		$("#subfieldvalue").attr("disabled",false);
    		$("#subfieldvalue").css("background","white");
    		$("#subfieldname").attr("disabled",false);
    		$("#subfieldname").css("background","white");
    		$("#sortvalue").attr("disabled",false);
    		$("#sortvalue").css("background","white");
   		}
		//“控件编号 ”、“控件类型”不能修改
    	$("#fieldid").attr("disabled",true);
    	$("#fieldid").css("background","Silver");
    	$("#fieldtype").attr("disabled",true);
    	$("#fieldtype").css("background","Silver");
    			
		$("#modleid").attr("value",modleid=="null"?"":modleid);
		$("#pageid").attr("value",pageid=="null"?"":pageid); 
		$("#fieldname").attr("value",fieldname=="null"?"":fieldname);
		$("#fieldid").attr("value",fieldid=="null"?"":fieldid);
		$("#field").attr("value",field);  
		$("#fieldtype").attr("value",fieldtype);
		$("#subfieldvalue").attr("value",subfieldvalue=="null"?"":subfieldvalue);
		$("#subfieldname").attr("value",subfieldname=="null"?"":subfieldname);
		$("#filedshow").attr("value",filedshow);
		$("#subfield").val(subfield);
		$("#defaultvalue").attr("value",defaultvalue);
		$("#sortvalue").attr("value",sortvalue);
		$("#isfield").attr("value",isfield);
		
		isFlag = modiFlag;
		$('#setPageField').dialog('open');	
	}

    function doDel(modleid, pageid,fieldid,fieldname,field,fieldtype,subfieldvalue,subfieldname,filedshow,subfield,defaultvalue,sortvalue,isfield)
	{
		/*确定删除该记录？*/
		if(confirm(getJsLocaleMessage("common","common_js_operPageField_1"))){
			$.post("glo_operPageFieldSvt.hts",
			{method:"delPageField",
				modleId:modleid,
				pageId:pageid,
				fieldId:fieldid,
				fieldName:fieldname,
				field:field,
				fieldType:fieldtype,
				subFieldValue:subfieldvalue,
				subFieldName:subfieldname,
				filedShow:filedshow,
				subField:subfield,
				defaultValue:defaultvalue,
				sortValue:sortvalue,
				isField:isfield},
			 function(result){				
				if (result == "success") {
					/*删除成功！*/
					alert(getJsLocaleMessage("common","common_deleteSucceed"));
					$('#setPageField').dialog('close');
					reload();
				}else{
					/*删除失败！*/
					alert(getJsLocaleMessage("common","common_deleteFailed"));
				}		
			});
		}	
	}

    function reload()
    {
	    window.location.href='glo_operPageFieldSvt.hts?method=find';
    }

    function setPageField()
  	{
	  	var modleId=$.trim($("#modleid").attr("value"));
		var pageId=$.trim($("#pageid").attr("value"));
		var fieldId=$.trim($("#fieldid").attr("value"));
		var fieldName=$.trim($("#fieldname").attr("value"));
		var field=$.trim($("#field").attr("value"));
		var fieldType=$.trim($("#fieldtype").attr("value"));
		var subFieldValue=$.trim($("#subfieldvalue").attr("value"));
		var subFieldName=$.trim($("#subfieldname").attr("value"));
		var filedShow=$.trim($("#filedshow").attr("value"));
		var subField=$.trim($("#subfield").attr("value"));
		var defaultValue=$.trim($("#defaultvalue").attr("value"));
		var sortValue=$.trim($("#sortvalue").attr("value"));
		var isField=$.trim($("#isfield").attr("value"));

		if(modleId == null || modleId == ""){
			/*请输入模块编号！*/
			alert(getJsLocaleMessage("common","common_js_operPageField_2"));
			return;
			}
		if(pageId == null || pageId == ""){
			/*请输入界面编号！*/
			alert(getJsLocaleMessage("common","common_js_operPageField_3"));
			return;
			}
		if(fieldName == null || fieldName == ""){
			/*请输入控件名称！*/
			alert(getJsLocaleMessage("common","common_js_operPageField_4"));
			return;
			}
		
		if(isFlag == 0)
		{
			$.post("glo_operPageFieldSvt.hts",
			{method:"addPageField",
				modleId:modleId,
				pageId:pageId,
				fieldId:fieldId,
				fieldName:fieldName,
				field:field,
				fieldType:fieldType,
				subFieldValue:subFieldValue,
				subFieldName:subFieldName,
				filedShow:filedShow,
				subField:subField,
				defaultValue:defaultValue,
				sortValue:sortValue,
				isField:isField
				},
				function(result){			
					if (result == "success") {
						/*增加成功！*/
						alert(getJsLocaleMessage("common","common_addSucceed"));
						reload();
						$('#setPageField').dialog('close');
					}else{
						/*增加失败！*/
						alert(getJsLocaleMessage("common","common_addFailed"));
					}		
				});
			}
		else
		{
			$.post("glo_operPageFieldSvt.hts",
	 			  {method:"modiPageField",
					modleId:modleId,
					pageId:pageId,
					fieldName:fieldName,
					fieldId:fieldId,
					field:field,
					fieldType:fieldType,
					subFieldValue:subFieldValue,
					subFieldName:subFieldName,
					filedShow:filedShow,
					subField:subField,
					defaultValue:defaultValue,
					sortValue:sortValue,
					isField:isField},
	 			  function(result){			
				if (result == "success") {
					/*修改成功！*/
					alert(getJsLocaleMessage("common","common_modifySucceed"));
					reload();
					$('#setPageField').dialog('close');
					//black();
				}else{
					/*修改失败！*/
					alert(getJsLocaleMessage("common","common_modifyFailed"));
				}		
			});
		}
  	}
  
    function cancel()
	{
		if(!$('#setPageField').is(":hidden"))
		{
		    $('#setPageField').dialog('close');	
		}
	}

    function changefieldstate()
    {
    	var isField=$.trim($("#isfield").attr("value"));
    	//判断如果不为子项“控件编号 ”、“控件子项值”、“控件子项名称”、“子项排序值”不可输
   		if(isField == 1)
   		{
    		$("#fieldid").attr("disabled",true);
    		$("#fieldid").css("background","Silver");
    		$("#subfieldvalue").attr("disabled",true);
    		$("#subfieldvalue").css("background","Silver");
    		$("#subfieldname").attr("disabled",true);
    		$("#subfieldname").css("background","Silver");
    		$("#sortvalue").attr("disabled",true);
    		$("#sortvalue").css("background","Silver");
   		}
   		else
   		{
    		$("#fieldid").attr("disabled",false);
    		$("#fieldid").css("background","white");
    		$("#subfieldvalue").attr("disabled",false);
    		$("#subfieldvalue").css("background","white");
    		$("#subfieldname").attr("disabled",false);
    		$("#subfieldname").css("background","white");
    		$("#sortvalue").attr("disabled",false);
    		$("#sortvalue").css("background","white");
   		}
    }