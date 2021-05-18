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
	          alert(getJsLocaleMessage("common","common_frame2_index_1")+validday+getJsLocaleMessage("common","common_frame2_index_2"));
			}	
			var contents=$("#contents").html();
			window.parent.$("#uChildren").html(contents);
        });
    	function doOpen(priMenus){
        	var $par = $(window.parent.frames["topFrame"].document);
        	$par.find("#onSys").attr("value","2");
       	    location.href = iPath+"/middel.jsp?priMenus="+priMenus;
    	}