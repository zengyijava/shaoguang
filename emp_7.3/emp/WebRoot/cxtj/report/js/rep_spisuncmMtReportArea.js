
	//国家/地区代码验证
	function numberControl(va)
	{
		var pat=/^\d*$/;
		if(!pat.test(va.val()))
		{
			va.val(va.val().replace(/[^\d]/g,''));
		}
	}


	//返回
	function showback(){
		window.location.href = 'rep_spisuncmMtReport.htm?method=find&isback=1';
	}
	
	
	//显示列表状态下，通道名称详细信息
	function modify(t)
	{
		$('#modify').dialog({
			autoOpen: false,
			resizable: false,
			width:250,
		    height:200
		});
		$("#msgss").children("xmp").empty();
		$("#msgss").children("xmp").text($(t).children("label").children("xmp").text());
		$('#modify').dialog('open');
	}	