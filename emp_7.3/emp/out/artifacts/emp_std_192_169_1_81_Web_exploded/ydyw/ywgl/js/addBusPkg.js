//保存业务包
function save(){
	
	var lguserid=$("#lguserid").val();
	var lgcorpcode=$("#lgcorpcode").val();
	var busPkgCode=$("#busPkgCode").val();
	var busPkgName=$("#busPkgName").val();
	if($.trim(busPkgName)==""){
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_9"));
		return;
	}
	if($.trim(busPkgCode)==""){
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_10"));
		return;
	}
	var pattern=/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;  
	var number=/[^a-zA-Z0-9]/g;
	
	if(pattern.test(busPkgName)){
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_11"));
		return;
	}	
	if(pattern.test(busPkgCode)){
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_12"));
		return;
	}
	if(number.test(busPkgCode)){
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_13"));
		return;
	}
	
	if ($("#getChooseMan li").size() < 1) {
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_14"));
		return;
	}
	var bussids="";
	$("#getChooseMan li").each(function() {
		var dataval = $(this).attr("dataval");
		if (dataval != null && dataval != "") {
			bussids=bussids + dataval + ",";
		}
	});
		$.ajax({
			type:"POST",
			url: "ydyw_busPkgMgr.htm",
			data: {method: "update",doType:'add',busPkgCode:busPkgCode,busPkgName:busPkgName,lguserid:lguserid,lgcorpcode:lgcorpcode,bussids:bussids,isAsync:"yes"},
			success: function(result){
				if(result=='success'){
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_15"));
					location.href="ydyw_busPkgMgr.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				}else if(result=='repeat'){
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_17"));
				}else{
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_16"));
				}
			},
			error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
		})
	
}


//点击选择
function router()
{
	$("#toLeft").attr("disabled", true);
	if($("#left").val()!=null && $("#left").val()!="")
	{
		moveLeft();
	}else{
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_19"));
	}
	$("#toLeft").attr("disabled", false);
}	
	
//点击查询
function searchName(){
    var searStr = $("#busSearStr").val();
    //left
	$.ajax({
		type:"POST",
		url: "ydyw_busPkgMgr.htm",
		data: {method: "busSearch",searStr:searStr,isAsync:"yes"},
		success: function(result){
			var data = eval("("+result+")");
			if(data.length==0){
				alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_18"));
				return;
			}
			var obj=document.getElementById('left'); 
			obj.options.length=0; 				
			for(var i= 0;i<data.length;i++){
				var bus_code = data[i].bus_code;
				var bus_name = data[i].bus_name;
				$("#left").append("<option value=\'"+bus_code+"\' >"+bus_name+"("+bus_code+")</option>");
			}
		},
		error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
	})
}
function back(){
	var lguserid=$("#lguserid").val();
	var lgcorpcode=$("#lgcorpcode").val();
	location.href="ydyw_busPkgMgr.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
}
	
