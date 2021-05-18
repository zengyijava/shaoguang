$(document).ready(function() {
	getLoginInfo("#loginUser");
	$("#addTypeDiv").dialog({
		autoOpen: false,
		height:280,
		width: 450,
		resizable:false,
		modal: true,
		close:function(){
			$("#dataTypeId").empty();
			var path = $("#path").val();
			var lgcorpcode = $("#lgcorpcode").val();
			$.post(path + "/wx_ueditor.htm?method=getActiviTables",{lgcorpcode:lgcorpcode},function(data){
                    var list=eval("("+data+")");
                    var tname=$("#dataTypeId");
                    for(var i=0;i<list.length;i++){
                          var options=$("<option>").val(list[i].id).html(list[i].name).appendTo(tname);
                    }
                    
           });
			
		}
	});
	
	$("#quesContent").keyup(function() {
		var content = $(this).val();
		var huiche = content.length - content.replaceAll("\n", "").length;
		if (content.length + huiche > 512) {
			$(this).val(content.substring(0, 512 - huiche));
		}
		len($(this));
	});
});

function manageType()
{
	var pathUrl = $("#pathUrl").val();
	var frameSrc = $("#tempFrame").attr("src");
	$("#tempFrame").css({'width':'430px','height':'235px'});
	if(frameSrc.indexOf("blank.jsp") > 0)
	{
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		frameSrc = pathUrl+"/wx_trustdata.htm?method=getDataTypes&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
		$("#tempFrame").attr("src",frameSrc);
	}
	$("#addTypeDiv").dialog("open");
} 
function addCols(){
	var quesType = $("#quesType").val();
	var cols = $("#initCols");
	var colNum = $("#colNum").val();
	var judgementNum=$("#judgementNum").val();
	if(judgementNum=='10'){
		alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_1"));
		return;
	}
	if(quesType == 2){
		cols.append('<tr><td><input type="radio" disabled name="check">'+
				'<input type="text" style="width:160px;margin-left: 10px;" maxlength="20" class="input_bd" id="displayName_'+colNum+'" value="" name="displayName_'+
				colNum+'" ><font style="color: red;">&nbsp;&nbsp;*</font><a style="margin-left:10px;cursor:hand;" onclick="deleteRow(this)">'+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+'</a></td></tr>');
	}else if(quesType == 3){
	cols.append('<tr><td><input type="checkbox" disabled name="check">'+
				'<input type="text" style="width:160px;margin-left: 10px;" maxlength="20" class="input_bd" id="displayName_'+colNum+'" value="" name="displayName_'+colNum
				+'"><font style="color: red;">&nbsp;&nbsp;*</font><a style="margin-left:10px;cursor:hand;" onclick="deleteRow(this)">'+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+'</a></td></tr>');
	}else if(quesType ==4){
		cols.append('<tr><td>'+
				'<input type="text" style="width:180px;" maxlength="20" class="input_bd" id="displayName_'+colNum+'" value="" name="displayName_'+colNum
				+'"><font style="color: red;">&nbsp;&nbsp;*</font><a style="margin-left:10px;cursor:hand;" onclick="deleteRow(this)">'+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+'</a></td></tr>');
	}
	$("#colNum").val(parseInt(colNum)+1);
	$("#judgementNum").val(parseInt(judgementNum)+1);
}

function changeQuesType(){
	var quesType = $("#quesType").val();
	$('#initCols_show').show();
	if(quesType == 1){
		$("#addCol").css("display","none");
	}else{
		$("#addCol").css("display","");
	}
	var cols = $("#initCols");
	cols.html("");
	if(quesType == 2){
		cols.append('<tr><td><input type="radio" disabled name="check">'+
				'<input type="text" style="width:160px;margin-left: 10px;" maxlength="20" class="input_bd" name="displayName_0" id="displayName_0" value="">'+
				'<font style="color: red;">&nbsp;&nbsp;*</font><a style="margin-left:10px;cursor:hand;" onclick="deleteRow(this)">'+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+'</a></td></tr><tr><td><input type="radio" disabled name="check">'+
				'<input type="text" style="width:160px;margin-left: 10px;" maxlength="20" class="input_bd" name="displayName_1" id="displayName_1" value="">'+
							'<font style="color: red;">&nbsp;&nbsp;*</font><a style="margin-left:10px;cursor:hand;" onclick="deleteRow(this)">'+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+'</a></td></tr>');
	}else if(quesType == 3){
	cols.append('<tr><td><input type="checkbox" disabled name="check">'+
				'<input type="text" style="width:160px;margin-left: 10px;" maxlength="20" class="input_bd" name="displayName_0" id="displayName_0" value="">'+
				'<font style="color: red;">&nbsp;&nbsp;*</font><a style="margin-left:10px;cursor:hand;" onclick="deleteRow(this)">'+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+'</a></td></tr><tr><td><input type="checkbox" disabled name="check">'+
				'<input type="text" style="width:160px;margin-left: 10px;" maxlength="20" class="input_bd" name="displayName_1" id="displayName_1" value="">'+
							'<font style="color: red;">&nbsp;&nbsp;*</font><a style="margin-left:10px;cursor:hand;" onclick="deleteRow(this)">'+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+'</a></td></tr>');
	}else if(quesType ==4){
		cols.append('<tr><td>'+
				'<input type="text" style="width:180px;" class="input_bd" maxlength="20" name="displayName_0" id="displayName_0" value="">'+
				'<font style="color: red;">&nbsp;&nbsp;*</font><a style="margin-left:10px;cursor:hand;" onclick="deleteRow(this)">'+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+'</a></td></tr><tr><td>'+
				'<input type="text" style="width:180px;" class="input_bd" maxlength="20" name="displayName_1" id="displayName_1" value="">'+
							'<font style="color: red;">&nbsp;&nbsp;*</font><a style="margin-left:10px;cursor:hand;" onclick="deleteRow(this)">'+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+'</a></td></tr>');
	}
	$("#judgementNum").val(parseInt(2));
}

function reg_exp(s) { 
	var pattern = "[`~!@#$^&*()={}:;',\.<>/?~！@#￥……&*（）——|【】‘；：”“'。，、？～]";
	for (var i = 0; i < pattern.length; i++) {
		if(s.indexOf(pattern.charAt(i)) > -1){
			return false;
		}
	} 
	return true; 
} 

function saveTrustData(refpage) {
	var code = $("#code").val();
	if(code == ""){
		alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_2"));
		$("#code").focus();
		return;
	}
	var trustName = $.trim($("#name").val());
	if (trustName=="") {
		alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_3"));
		$("#name").focus();
		return;
	}else if(!reg_exp(trustName)){
		alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_4"));
		$("#trustName").focus();
		return;
	}
	if (trustName.length > 20) {
		alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_5"));
		$("#trustName").focus();
		return;
	}
	var dataTypeId = $("#dataTypeId").val();
	if(dataTypeId == ""){
		alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_6"));
		return;
	}
	var quesContent = $("#quesContent").val();
	if(quesContent == ""){
		alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_7"));
		$("#quesContent").focus();
		return;
	}
	var disName = $("#displayName_0").val();
	if($("#quesType").val()!=1 ){
		var isNull = false;
		$("#initCols :text").each(function(){  
			if($(this).val() == ""){
				isNull = true;
			}
		}); 
		if(isNull){
			alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_8"));
			return ;
		}
	}
	var lgcorpcode = $("#lgcorpcode").val();
	var oldCode = $("#oldCode").val();
	var optype = $("#optype").val();
	if(optype == "copy"){
		$("#trustForm").attr('action', "wx_trustdata.htm?method=add");
	}
	if(optype == "add" || optype == "copy" || (optype == "edit" && code != oldCode)){
		$.post("wx_trustdata.htm?method=validateCode",{code:code,lgcorpcode:lgcorpcode},function(result){
			if(result == "true" && refpage != "addhdx"){
				submitForm();
				$("#save").attr("disabled","disabled");
			}else if(result == "true" && refpage == "addhdx"){
				submitFormAddhdx();
				$("#save").attr("disabled","disabled");
				
			}else if(result == "exist"){
				alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_9"));
				$("#code").focus();
			}else{
				alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_10"));
			}
		});
	}else{
		submitForm();
		$("#save").attr("disabled","disabled");
	}
}


function deleteRow(button) {

	if($("#initCols").find("tr").length == 1){
		alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_11"));
	}else{
		var judgementNum=$("#judgementNum").val();
		$("#judgementNum").val(parseInt(judgementNum)-1);
		$(button).parent().parent().remove();
	}
}

function doreturn(){
	var path = $("#pathUrl").val();
	window.location.href = path+"/wx_trustdata.htm";
}


function submitForm(){
	$("#trustForm").submit();
}

function submitFormAddhdx(){
	
	var code = $("#code").val();
	var colNum = $("#colNum").val();
	var refpage = $("#refpage").val();
	var name = $.trim($("#name").val());
	var dataTypeId = $("#dataTypeId").val();
	var quesContent = $("#quesContent").val();
	var quesType = $("#quesType").val();
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var replySetType = "1";//默认多次回复有效
	
	$.post("wx_trustdata.htm?method=add",{code:code,
		refpage:refpage,
		colNum:colNum,
		name:name,
		dataTypeId:dataTypeId,
		quesContent:quesContent,
		displayName_0:$("#displayName_0").val(),
		displayName_1:$("#displayName_1").val(),
		displayName_2:$("#displayName_2").val(),
		displayName_3:$("#displayName_3").val(),
		displayName_4:$("#displayName_4").val(),
		displayName_5:$("#displayName_5").val(),
		displayName_6:$("#displayName_6").val(),
		displayName_7:$("#displayName_7").val(),
		displayName_8:$("#displayName_8").val(),
		displayName_9:$("#displayName_9").val(),
		quesType:quesType,
		lguserid:lguserid,
		lgcorpcode:lgcorpcode,
		replySetType:replySetType
		},function(result){
		if(result == "true" ){
			alert(getJsLocaleMessage("ydwx","ydwx_common_czchg"));
			window.parent.refreshInterType();
			window.parent.closeDialog();
		}else{
			alert(getJsLocaleMessage("ydwx","ydwx_common_czych"));
			$("#save").attr("disabled","");
		}
	});
}

// 失去焦点时判断
function eblur(ob) {
	if (ob.is("#quesContent")) {
		ob.val($.trim(ob.val()));
		var content = $("#quesContent").val();
		var huiche = content.length - content.replaceAll("\n", "").length;
		if (content.length + huiche > 512) {
			$("#quesContent").val(content.substring(0, 512 - huiche));
		}
		len(ob);
	}
}

// 统计短信内容字数
function len(ob) {
	var content = $.trim(ob.val());
	var huiche = content.length - content.replaceAll("\n", "").length;
	if(ob.is("#quesContent")){
		if(content.length>512)
		{
			$("#quesContent").val(content.substring(0,512));
		}
	}
	$('#quesstrlen').html(($("#quesContent").val().length + huiche));
}