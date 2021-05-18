	$(document).ready(function(){
		$("#addNotice").dialog({
	    	autoOpen: false,
	    	height:355,
	    	width: 400,
	    	modal: true,
	    	close:function(){
				$("#title").val("");
				$("#cont").val("");
				$("#nid").val("");
	 		}
	    });
	    $("#Notices").dialog({
	    	autoOpen: false,
	    	height:350,
	    	width: 400,
	    	modal: true
	    });
	});
     function showDiv()
     {
           $("#tableDiv").toggle();
     }
     function doAdd()
     {
           $("#addNotice").dialog("open");
     }
     function doEdit(id)
     {
     	$.post("not_notice.htm?method=showDetail",{id:id,isAsync:"yes"},function(data){
     		var path=$("#contextPath").val();
     		if(result == "outOfLogin")
			{
				$("#logoutalert").val(1);
				location.href=contextPath+"/common/logoutEmp.html";
				return;
			}
    		if(data == "false"){
 		     	alert(getJsLocaleMessage("xtgl","xtgl_gg_gglb_1"));
 		     	return ;
 		     }
			var result=eval("("+data+")");//转换为json对象   
  			$("#nid").attr("value",id);
	    	$("#title").attr("value",result.title);
	        $("#cont").val(result.content);
	        $("#addNotice").dialog("open");
    	});
     }
     function doCancel()
     {
    	 $("#addNotice").dialog("close");
     }
     function doOk()
     {
         var id = $("#nid").attr("value");
         var tt = $("#title").attr("value");
         var cont = $("#cont").val();
         //cont = $.trim(cont);
         var lguserid = $("#lguserid").val();
         var lgcorpcode =$("#lgcorpcode").val();
         if (tt==null || $.trim(tt) == "")
         {
             alert(getJsLocaleMessage("xtgl","xtgl_gg_gglb_2"));
             return;
         }
         else if (cont==null || $.trim(cont) == "")
         {
             alert(getJsLocaleMessage("xtgl","xtgl_gg_gglb_3"));
             return;
         }
         else if(cont.length>650){
             alert(getJsLocaleMessage("xtgl","xtgl_gg_gglb_4"));
             return; 
          }
         $.post("not_notice.htm?method=update",{id:id,tt:tt,cont:cont,lguserid:lguserid,lgcorpcode:lgcorpcode,isAsync:"yes"},function(result){
        	var path=$("#contextPath").val();
      		if(result == "outOfLogin")
 			{
 				$("#logoutalert").val(1);
 				location.href=contextPath+"/common/logoutEmp.html";
 				return;
 			}
        	 if (result != null && result == "true")
             {
                  alert(getJsLocaleMessage("xtgl","xtgl_gg_gglb_5"));
                  $("#addNotice").dialog("close");
                  //location.href = location;
                  submitForm();
             }
             else
             {
            	 alert(getJsLocaleMessage("xtgl","xtgl_gg_gglb_6"));
             }
         });
     }
     function doDel(id){
    		if(confirm(getJsLocaleMessage("xtgl","xtgl_gg_gglb_7"))==true)
    		{
    			$.post("not_notice.htm?method=delete",{ids:id,isAsync:"yes"},function(result){
    				var path=$("#contextPath").val();
    	     		if(result == "outOfLogin")
    				{
    					$("#logoutalert").val(1);
    					location.href=contextPath+"/common/logoutEmp.html";
    					return;
    				}
    				if(result>0){
    					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_5"));
    					//window.location = location;
    					back();

    				}else alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_6"));
    			});
    		}
    	}
 	function showDetail(id)
 	{
 		$.post("not_notice.htm?method=showDetail",{id:id,isAsync:"yes"},function(data){
 			var path=$("#contextPath").val();
     		if(result == "outOfLogin")
			{
				$("#logoutalert").val(1);
				location.href=contextPath+"/common/logoutEmp.html";
				return;
			}
			if(data == "false"){
 		     	alert(getJsLocaleMessage("xtgl","xtgl_gg_gglb_1"));
 		     	return ;
 		     }
				var result=eval("("+data+")");//转换为json对象   
  				$("#user").attr("value",result.name);
	 		$("#ttime").attr("value",result.publishTime);
	   	    $("#ttitle").attr("value",result.title);
	        $("#tcont").val(result.content);
	        $("#Notices").dialog("open");
    	});
 	}
 	
	function back()
	{
			var type=$("#type").val();
	        var url = 'not_notice.htm?method=find&type='+type;
			var conditionUrl = "";
			if(url.indexOf("?")>-1)
			{
				conditionUrl="&";
			}else
			{
				conditionUrl="?";
			}
			$("#loginparams").find(" input").each(function(){
				conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
			});
			location.href=url+conditionUrl;
	}