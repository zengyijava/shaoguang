  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`
	$(document).ready(function() {
		$("#content select").empSelect({width:80});
		//初始化彩信预览DIV
		var h = 500;
		if (navigator.appName == "Netscape")
	    {
	    	h = 500;
	    }
		$("#tempView").dialog({
			autoOpen: false,
			height:h,
			width: 290,
			resizable:false,
			close:function(){
			    cplaytime = 0;
				nplaytime = -1;
				$("#screen").empty();
				clearInterval(ttimer); 
			}
		});
		$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
		$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
			}, function() {
			$(this).removeClass("hoverColor");
		});
	});
		  
   function modify(t,i)
	{
		$('#modify').dialog({
			autoOpen: false,
			resizable: false,
			width:250,
		    height:200
		});
		$("#msg").children("xmp").empty();
		$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
		if(i==1)
		{
			$('#modify').dialog('option','title',getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_76"));
		}
		else
		{
			$('#modify').dialog('option','title',getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_77"));
		}
		$('#modify').dialog('open');
	}

	// 彩信预览/
	function doPreview111(msg,dsFlag)
	{
		$(".ui-dialog-titlebar-close").show();
		inits();
		var pathUrl = $("#pathUrl").val();
		$.post(pathUrl+"/smm_sameMms.htm?method=getTmMsg",{tmUrl:msg},function(result){
			if (result != null && result != "null" && result != "")
			{
				arr = result.split(">");
				if(arr[0] != null && arr[0] != "")
				{
					var da = $.parseJSON(arr[0]);
					ttime = (da.totaltime/1000);
				}
				index = 1;
				$("#screen").empty();
				$("#pointer").empty();
				$("#nextpage").empty();
				$("#currentpage").empty();
				parmCount =null;
				$("#inputParamCnt1").empty();
				$("#tempView").dialog("open");
				play(dsFlag);
			}else{
	             alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_78"));
			}
		});
	}


	//查看彩信内容
	function doPreview(tplPath)
	{
		$(".ui-dialog-titlebar-close").show();
		var bmtype; 
		var msg = "";
		var tmpltype = $("#tmpltype").val();
		if(tmpltype == "0"){
			bmtype = 11;
		}else if(tmpltype == "1"){
			bmtype = 12;
		}
		inits();
		var pathUrl = $("#pathUrl").val();
		$.post(pathUrl+"/mmt_mmsTask.htm?method=getTmMsgByBmtype",{tmUrl:msg,bmtype:bmtype,tplPath:tplPath},function(result){
			if (result != null && result != "null" && result != "")
			{
				arr = result.split(">");
				if(arr[0] != null && arr[0] != "")
				{
					var da = $.parseJSON(arr[0]);
					ttime = (da.totaltime/1000);
				}
				index = 1;
				$("#screen").empty();
				$("#pointer").empty();
				$("#nextpage").empty();
				$("#currentpage").empty();
				parmCount =null;
				$("#inputParamCnt1").empty();
				$("#tempView").dialog("open");
				var dsFlag = bmtype==12?1:0;
				play(dsFlag);
			}
			else
			{
	             alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_78"));
			}
		});
	}
	

	//彩信模板确认/
	function tempSure()
	{
		var tem = $("input[type='radio']:checked").val();
		var $ro = $("input[type='radio']:checked");
		var commonPath = $("#commonPath").val();
		var ipathUrl = $("#ipathUrl").val();
		var skinType = $("#skinType").val();
		if(tem == undefined || tem == "" || tem == null)
		{
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_79"));
			return;
		}else
		{
			var message = $ro.next("xmp").text();
			var tmname  = $ro.next().next("#templatename").val();
			//选择的模板ID  模板URL/
			$(window.parent.document).find("#mmstemplateid").val(tem);
			$(window.parent.document).find("#mmstemplateurl").val(message);
			$(window.parent.document).find("#mmsFileName").val("");
			//选择的彩信模板模板1/
			$(window.parent.document).find("#teplortms").val("1");
        var ss = "<div style='float:left;' class='div_bg'>"
		+"<label><img border='0' src='"+ipathUrl+"/img/fileimg.png'></img>&nbsp;"+tmname+"</label>"
		+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font onclick='doPreview(\""+message+"\",0)' style='color:#095AD1;cursor:pointer;'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_13") + "</font>&nbsp;&nbsp;"
		+"<font onclick='tempNo()' style='color:#095AD1;cursor:pointer;'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_14")+ "</font></div>";
			$(window.parent.document).find("#templatepre").empty();
			$(window.parent.document).find("#templatepre").html(ss);
			$(window.parent.document).find("#templatepre").show();


		}
		//调用父窗口的函数/
		parent.closeDialog();
	}

	//关闭窗口
	function tempCancel(){
		//调用父窗口的函数/
		parent.closeDialog();
	}
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   