//跳到公共页面
        function toNotic()
        {
            var lgcorpcode=$("#lgcorpcode").val();
            var lguserid=$("#lgcorpcode").val();
            $(window.parent.document).find("#leftIframe")[0].contentWindow.showgg();
            //type=1表示充首页跳到公告，用来公告显示当前位置
            window.location.href='not_notice.htm?lguserid='+lguserid+"&lgcorpcode="+lgcorpcode+"&lgusername="+$("#lgusername").val()+"&type=1";
        }
        $(document).ready(
            function(){
    			getLoginInfo("#loginparam");
    			 $("#Notices").dialog({
    		 	    	autoOpen: false,
    		 	    	height:350,
    		 	    	width: 400,
    		 	    	modal: true
    		 	    });
    	});
    	var hasShow = 0;
     	function showDetail(id)
     	{
         	if(hasShow==0)
         	{
	     		$.post(path+"/not_notice.htm?method=showDetail",{id:id},function(data){
	    			if(data == "false"){
	     		     	alert(getJsLocaleMessage("common","common_frame2_warmPrompt_35"));
	     		     	return ;
	     		     }
	    				var result=eval("("+data+")");//转换为json对象   
	      				$("#user").attr("value",result.name);
	    	 		$("#ttime").attr("value",result.publishTime);
	    	   	    $("#ttitle").attr("value",result.title);
	    	        $("#tcont").val(result.content);
	    	        $("#Notices").dialog("open");
	        	});
         	}else
         	{
         		$("#Notices").dialog("open");
         	}
     	}
     	function openNewTab(menuCode)
     	{
         	parent.openNewTab(menuCode,"");
     	}