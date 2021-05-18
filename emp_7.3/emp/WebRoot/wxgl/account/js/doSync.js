$(function(){
	$(".doSyncBut").click(function(){
		 var pathUrl = $("#pathUrl").val();
		 var aId = $(this).attr("data-id");
		 var $td = $(this).parent();
		 $td.addClass("loading");
		 $(this).siblings().andSelf().hide()
		 if($td.find(".loadingTip").size()==0){
			 $td.append("<span style='margin-left:40px;' class='loadingTip'>"+getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_29")+"</span>");
		 }
		 var syncUrl = pathUrl + "/weix_acctManager.htm?method=doSyncProcess&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val() + "&aId="+aId+"&time="+new Date().getTime();
		 $.ajax({
			  url: syncUrl,
			  method: "get",
			  success: function(data){
			 	 if(data=="success"){
			 		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_26"));
			 		$td.removeClass("loading");
			 		$td.find("span.loadingTip").remove();
			 		$td.children().show();
			 	 }else if(data == "fail"){
			 		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_27"));
			 		$td.removeClass("loading");
			 		$td.find("span.loadingTip").remove();
			 		$td.children().show();
			 	 }else if(data == "errorToken"){
			 		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_28")); 
			 		$td.removeClass("loading");
			 		$td.find("span.loadingTip").remove();
			 		$td.children().show();
			 	 }else{
			 		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_30"));
			 		$td.removeClass("loading");
			 		$td.find("span.loadingTip").remove();
			 		$td.children().show();
			 	 }
		 	  },
			  dataType: "text"
		 });
	});
});