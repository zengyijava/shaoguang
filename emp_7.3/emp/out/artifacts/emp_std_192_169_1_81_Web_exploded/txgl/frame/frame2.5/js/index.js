$(document).ready(function(){
			$(".menuDiv").hover(
				function(){
					var $athi = $(this).find("a");
					var img=$athi.css("background-image");
					$athi.css("background-image",img.replace(".jpg","_hover.jpg"))
				},
				function(){
					var $athi = $(this).find("a");
					var img=$athi.css("background-image");
					$athi.css("background-image",img.replace("_hover.jpg",".jpg"))
				}
			);	
			
			if(aproinfo != null && aproinfo != "" && aproinfo == "over")
			{
	          alert("认证暂可使用"+validday+"天，为了保证系统正常使用，请获取认证！");	             
			}	
			var contents=$("#contents").html();
			window.parent.$("#uChildren").html(contents);
        });
    	function doOpen(priMenus){
        	var $par = $(window.parent.frames["topFrame"].document);
        	$par.find("#onSys").attr("value","2");
       	    location.href = iPath+"/middel.jsp?priMenus="+priMenus;
    	}