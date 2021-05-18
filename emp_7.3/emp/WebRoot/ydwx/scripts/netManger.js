  
  
    var loadNetmanger=function(){
    
    $("#lookup").click(function(){
		var wxid=$("#wxid").val();
	    var wxname=$("#wxname").val();
	    var zhuangtai=$("#rState").val();
	    var creatname=$("#creatname").val();
	    var startdate=$("#startdate").val();
	    var enddate=$("#enddate").val();
	    var corp=$("#corp").val();
		
	   wxid=document.managerForm.wxid.value;  
	   if (wxid.indexOf(" ")>-1) 
	   {  
			wxid=wxid.replace(/(^\s*)|(\s*$)/g,"");
       } 
	   var pattern = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）—|{}【】‘；：”“'。，、？]"; 
	   for(var i = 0 ; i< pattern.length; i++){
	  		if(wxname.replace(/(\s*$)/g, "").indexOf(pattern.charAt(i))>-1){
				alert(getJsLocaleMessage("ydwx","ydwx_wxhftj_1"));
		    	$("#wxname").select();
	    	  	return;
			}			
	   }
		flg=0;  
		str="";  
		spc=""  
		arw="";  
		for (var i=0;i<wxid.length;i++)
		{  
			cmp="0123456789"  
			tst=wxid.substring(i,i+1)  
			if (cmp.indexOf(tst)<0)
			{  
				flg++;  
				str+=" "+tst;  
				spc+=tst;  
				arw+="^";  
			} else{arw+="_";}  
		}  
		if (flg!=0)
		{    
			alert(getJsLocaleMessage("ydwx","ydwx_wxhftj_2"));  
			$("#wxid").select();
			return;  
		}  
		
		
		wxname=document.managerForm.wxname.value; 
		if (wxname.indexOf(" ")>-1) 
			{  
			wxname=wxname.replace(/(^\s*)|(\s*$)/g,"");

			} 
		
		if(startdate>enddate && enddate.length>0)
		{
		
			alert(getJsLocaleMessage("ydwx","ydwx_wxhftj_3"));
			$("#startdate").select();
			return;
		}
		
		else
		{
		 
			wxname=encodeURI(wxname); 
			wxname=encodeURI(wxname); 
			 
			creatname=encodeURI(creatname); 
		    creatname=encodeURI(creatname);
			window.location.href='wx_manger.htm?method=lookup&&wxid='+wxid+'&&wxname='+wxname+'&&zhuangtai='+zhuangtai+'&&creatname='+creatname+'&&startdate='+startdate+'&&corp='+corp+'&&enddate='+enddate;
		}
		});
    }
    	//网讯管理复制
	function copy(coid)
	{
		if(confirm(getJsLocaleMessage("ydwx","ydwx_common_nqryfzh")))
		{
			var lguserid = $("#lguserid").val();
			$.post("wx_manger.htm",{method:"Copy",coid:coid,lguserid:lguserid},function(data)
			{
				if(data=="true")
				{
					alert(getJsLocaleMessage("ydwx","ydwx_common_fzhchg"));
					submitForm();
				}else
				{
					alert(getJsLocaleMessage("ydwx","ydwx_common_fzhshb"));
				}
			});
		}
	}
     String.prototype.trim = function () {
     return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
    }

   
