	/**
	* 该文件为网讯素材上传的方法，其中包括一些校验
	* @base 网讯素材
	* @author Administrator
	* @date 2013-10-10
	* ***/

		//网讯素材校验
		function startProgress()
		{
			var AttachName=$("#AttachmentName").val().replace(/(^\s*)|(\s*$)/g,'');
			var AttachmentDescribe=$("#AttachmentDescribe").val().replace(/(^\s*)|(\s*$)/g,'');
		
				if(AttachName=="")
				{
					alert(getJsLocaleMessage("ydwx","ydwx_wxsc_59"));
					$("#AttachmentName").focus();
					return false;
				}
				if($("#AttachType").val()=="")
				{
					alert(getJsLocaleMessage("ydwx","ydwx_wxsc_60"));
					$("#AttachType").focus();
					return false;
				}
				var filepath =$("#file_1").val();
				if(filepath == ''){
					alert(getJsLocaleMessage("ydwx","ydwx_wxsc_61"));
					return false;
				}
				
		   	    var pattern = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）—|{}【】‘；：”“'。，、？]"; 
			    for(var i = 0 ; i< pattern.length; i++){
			  		if(AttachName.replace(/(\s*$)/g, "").indexOf(pattern.charAt(i))>-1){
						alert(getJsLocaleMessage("ydwx","ydwx_wxsc_8"));
				    	$("#AttachmentName").select();
			    	  	return false;
					}			
			    }	
		  		if(yinhao(getJsLocaleMessage("ydwx","ydwx_wxsc_9"),AttachName)){
		  			return false;
			  	}
				if(AttachName.length>25)
			      {
			      		alert(getJsLocaleMessage("ydwx","ydwx_wxsc_6")); 
			      		$("#AttachmentName").select();
			      		return false;
			      }
			  	for(var i = 0 ; i< pattern.length; i++){
			  		if(AttachmentDescribe.replace(/(\s*$)/g, "").indexOf(pattern.charAt(i))>-1){
						alert(getJsLocaleMessage("ydwx","ydwx_wxsc_10"));
				    	$("#AttachmentDescribe").select();
			    	  	return false;
					}			
			    }
		  		if(yinhao(getJsLocaleMessage("ydwx","ydwx_wxsc_11"),AttachmentDescribe)){
		  			return false;
			  	}
				if(AttachmentDescribe.length>50)
				 {
					alert(getJsLocaleMessage("ydwx","ydwx_wxsc_7"));
				    $("#AttachmentDescribe").select();
				 	return false;
				 }   
				var video="mp4,MP4,webm,WEBM,swf,SWF,m3u8,M3U8,flv,FLV";//视频类型  0
				var msg="jpg,JPG,bmp,BMP,gif,GIF,img,IMG,png,PNG,jpeg,JPEG";//图片类型 1
				var file="txt,TXT,doc,DOC,xls,XLS";//文件类型 2  
		
				var typeId = $("#AttachTypeNum").val();
				var fileField=$("#file_1").val();
				var str = fileField.split("\.");
				var ss =str[str.length-1];
				 if(typeId==20){
					 if(file.indexOf(ss)!=-1){
						$("#filesubmit").attr("disabled","disabled");
						 return true;
					 }else{
						 alert(getJsLocaleMessage("ydwx","ydwx_wxsc_62"));
						 return false;
					 }
				 }else if(typeId==22){
					 if(msg.indexOf(ss)!=-1){
						$("#filesubmit").attr("disabled","disabled");
						 return true;
					 }else{
						 alert(getJsLocaleMessage("ydwx","ydwx_wxsc_62"));
						 return false;
					 }
				 }else if(typeId==23){
					 if((file.indexOf(ss)!=-1) || (video.indexOf(ss)!=-1) || (msg.indexOf(ss)!=-1)){
						 alert(getJsLocaleMessage("ydwx","ydwx_wxsc_62"));
						 return false;
					 }else{
					$("#filesubmit").attr("disabled","disabled");
						 return true;
					 }
				 }else{
					 alert(getJsLocaleMessage("ydwx","ydwx_wxsc_63"));
					 return false;
				 }
		
		 
		}
		function VedstartProgress()
		{
			if($("#vedName").val().replace(/(^\s*)|(\s*$)/g,'')=="")
			{
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_59"));
				$("#vedName").focus();
				return false;
			}
		    var pattern = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）—|{}【】‘；：”“'。，、？]"; 
			for(var i = 0 ; i< pattern.length; i++){
		  		if($("#vedName").val().replace(/(^\s*)|(\s*$)/g,'').indexOf(pattern.charAt(i))>-1){
					alert(getJsLocaleMessage("ydwx","ydwx_wxsc_64"));
			    	$("#vedName").select();
		    	  	return false;
				}			
			}	
			if($("#vedName").val().replace(/(^\s*)|(\s*$)/g,'').length>25)
		    {
		      	alert(getJsLocaleMessage("ydwx","ydwx_wxsc_65")); 
		      	$("#vedName").select();
		      	return false;
		    }
		    for(var i = 0 ; i< pattern.length; i++){
		  		if($("#veddec").val().replace(/(^\s*)|(\s*$)/g,'').indexOf(pattern.charAt(i))>-1){
					alert(getJsLocaleMessage("ydwx","ydwx_wxsc_66"));
			    	$("#veddec").select();
		    	  	return false;
				}			
			}	
		
		    if($("#veddec").val().replace(/(^\s*)|(\s*$)/g,'').length>50)
		    {
		   		alert(getJsLocaleMessage("ydwx","ydwx_wxsc_67")); 
		   		$("#veddec").select();
		   		return false;
		    }
			if($("#filemp4").val().replace(/(^\s*)|(\s*$)/g,'')=="")
			{
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_68"));
				$("#filemp4").focus();
				return false;
			}
			//if($("#fileswf").val().replace(/(^\s*)|(\s*$)/g,'')=="")
			//{
			//	alert("SWF文件不能为空，请选择");
			//	$("#fileswf").focus();
			//	return false;
			//}
			
			var filemp4=$("#filemp4").val().replace(/(^\s*)|(\s*$)/g,'');
			var videoType=$("#videoType").val();
			var str = filemp4.split("\.");
			var s1 =str[str.length-1];
			var mp4="mp4,MP4";
			if(videoType=='1'&&mp4.indexOf(s1)<0) 
			{
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_69"));
				return false;
			}
			
			var swf="swf,SWF";
			if(videoType=='2'&&swf.indexOf(s1)<0)
			{
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_70"));
				return false;
			}
			$("#vdiosubmit").attr("disabled","disabled");
			
		    return true;
		
		}
		
				

