$(function(){
	var pushmourl = $("#trmorpoption td input#pushmourl"); //需要上行
	var pushrpurl = $("#trmorpoption td input#pushrpurl"); //需要状态报告
	
	var trsxurl = $("#trsxurl"); //上行URL
	var trpturl = $("#trpturl"); //状态报告URL
	var morptmode = $("#morptmode"); //上行、状态报告获取方式
	
	pushmourl.click(function() {
		if(pushrpurl.is(":checked") || pushmourl.is(":checked")) {//两个复选框至少有一个被选中
			
			if(pushmourl.is(":checked") && morptmode.val() == 2) { //选中需要上行，获取方式为2
				$("#trmorptmode").show();
				$("#trpushversion").show();
				$("#trsxurl").show();
			} else if(!pushmourl.is(":checked")) {//未选中需要上行，获取方式为2
				$("#trsxurl").hide();
			} else if(pushmourl.is(":checked") && morptmode.val() == 1) {
				$("#trmorptmode").show();
				$("#trmorpturl").show();
				$("#trsxurl").hide();
			} else if(pushmourl.is(":checked") && morptmode.val() == 0) {
				$("#trmorptmode").show();
			}
		} else {  //都未被选中
			$("#trmorptmode").hide();
			$("#trpushversion").hide();
			$("#trmorpturl").hide();
			$("#trsxurl").hide();
			$("#trpturl").hide();
		}
	})
	pushrpurl.click(function() {
		if(pushrpurl.is(":checked") || pushmourl.is(":checked")) { //两个复选框至少有一个被选中
			if(pushrpurl.is(":checked") && morptmode.val() == 2) {
				$("#trmorptmode").show();
				$("#trpushversion").show();
				$("#trpturl").show();
			} else if(!pushrpurl.is(":checked")) {
				$("#trpturl").hide();
			} else if(pushrpurl.is(":checked") && morptmode.val() == 1) {
				$("#trmorptmode").show();
				$("#trmorpturl").show();
			} else if(pushrpurl.is(":checked") && morptmode.val() == 0) {
				$("#trmorptmode").show();
			}
		} else {  //都未被选中
			$("#trmorptmode").hide();
			$("#trpushversion").hide();
			$("#trmorpturl").hide();
			$("#trsxurl").hide();
			$("#trpturl").hide();
		}
	})
})