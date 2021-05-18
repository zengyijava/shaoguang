
		function showback()
		{
		   var lgcorpcode =$("#lgcorpcode").val();
		   var lguserid =$("#lguserid").val();
		   location.href="wx_wmsTaskRecord.htm?method=findallLfMttask&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&skip=true";
		}