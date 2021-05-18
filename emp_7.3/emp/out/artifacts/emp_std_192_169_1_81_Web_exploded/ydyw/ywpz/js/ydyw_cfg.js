//新建业务类型
function addBus() {
 	$("#addDiv").dialog("open");
}

function doCancel() {
  	$("#addDiv").dialog("close");
}


function closeAddSmsTmpdiv() {
  	$("#addSmsTmpDiv").dialog("close");
  	$("#addSmsTmpFrame").attr("src","");
}

function changeMb(i) {
	if(i==3) {
		$("#trSmsTmpType").css("height","100px");
		$("#subModule").css("display","inline");
	} else {
		$("#subModule").css("display","none");
		$("#trSmsTmpType").css("height","auto");
	}
	
	if(i==0 || i==2) {
		
		$("#fontFmTip,.para_cg,.tit_panel,.showParams").hide();
	} else {
		$('.tit_panel,.showParams').show();
		$(".para_cg").show();
		$("#fontFmTip").show();
	}
	
}

function addBusType() {
	var busName = $('#busNameAdd').val();
	var busDescription = $('#busDescriptionAdd').val();
	var busCode = $('#busCodeAdd').val();
	var busType = $('#busTypeAdd').val();
	var riseLevel = $('#riseLevelAdd').val();
	var pattern = /^[A-z0-9]+$/;
	if(busName == ""){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_36"));$('#busNameAdd').select();return;}
	if(busCode == ""){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_37"));$('#busCodeAdd').select();return;}
	if(busType == ""){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_38"));$('#busTypeAdd').select();return;}
	
	if($('#busDescriptionAdd').val().length>200) {
	    alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_39"));
	    return;
	}
		
	busCode=busCode.replace(/\\/g,getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_50"));
		
	if(!pattern.test(busCode)) {
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_19"));
		return;
	} else {
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $('#lgcorpcode').val();
		$.post("ydyw_busTempConf.htm",
			{
				method:"addBus",
				lgcorpcode:lgcorpcode,
				lguserid:lguserid,
				busName : busName,
				busType : busType,
				riseLevel : riseLevel,
				busDescription : busDescription,
				busCode : busCode
			},function(result){
				if(result == "codeExists") {
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_40"));
					$('#busCodeAdd').select();
					return;
				} else if(result == "nameExists") {
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_41"));
					$('#busNameAdd').select();
					return;
				} else if(result == "true") {
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_42"));
					doCancel();						
				} else {
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_43"));
					return;
				}
				
				window.location.reload();
			}
		);
	}
}

function exist(id){
    var s=document.getElementById(id);
    
    if(s) {
    	return true
    } else {
    	return false
    }
}

//点击选择
function router() {
	var manCount = parseInt($("#manCount").html());
	var alertNum = 0;
	var checkCount = 0;	
	
	$('[name=cks]:checkbox').each(function() {	
		
		if(this.checked) {	
			checkCount = checkCount + 1;
			var valId = "hid_" + $(this).val();
			var name = $("#" + valId).val();
			var lid = "tr_" + $(this).val();			
			
			//隐藏左边已选项
			//$("#" + lid).css("display", "none");
			
			var rid = "rtr_" + $(this).val();
			if (exist(rid)) {
				//如果已经存在此ID，将隐藏的行显示出来				
				//$("#" + rid).css("display", "block");
				alertNum = alertNum + 1;							
			} else {
				//模板名称（显示全部字数）
				var titleid = "hid_" + $(this).val();
				var title = $("#" + titleid).val();
				//超链接内容
				var msgid = "msg_" + $(this).val();
				var msg = $("#" + msgid).val();
				msg = msg.replaceAll("#[pP]_([1-9][0-9]*)#","{#"+getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_44")+"$1#}");
				
				if (msg.length > 5) {
					msg = msg.substring(0, 5) + "...";
				}
				
				if (name.length > 5) {
					name = name.substring(0, 5) + "...";
				}
				
				//此ID不存在，则添加一行
				$("#rightTb").append("<tr id='rtr_" + $(this).val() + "'><td><input type='checkbox' name='rcks' " 
						+ "id='rcks_" + $(this).val() + "' value='"+$(this).val()+"'/>" 
						+ "<a href='javascript:void(0);' title='" + title + "' style='color: black;'>&nbsp;" + name + "</a>" 
						+ "</td><td>&nbsp;</td><td><a href='javascript:showMsg(" + $(this).val() + ");' align='right' title='"+getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_45")+"'><xmp align='left'>" + msg + "</xmp></a></td></tr>");
				
				$("#getChooseMan").append("<li id='rli_" + $(this).val() + "' dataval='" + $(this).val() + "'>" + name + "</li>");
				
				manCount = manCount + 1;
			}
			
			if (alertNum == 1) {
				alertNum = alertNum + 1;
				alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_46"));
			}	
		}	
	});
	
	if (checkCount == 0) {
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_47"));
		return;
	}
	$("#manCount").html(manCount);
}	

function moveRight() {
	var manCount = parseInt($("#manCount").html());
	if ($('#rallCk').is(':checked')) {		
		$('#rallCk').attr("checked", false);  
	}
	
	if (manCount == 0) {
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_48"));
		return;
	}
	
	var checkCount = 0;		
	$('[name=rcks]:checkbox').each(function() {
		
		if(this.checked) {
			checkCount = checkCount + 1;
			var lid = "tr_" + $(this).val();
			//如果已经存在此ID，将隐藏的行显示出来				
			//$("#" + lid).css("display", "block");
			
			var valId = "hid_" + $(this).val();		
			
			var rid = "rtr_" + $(this).val();
			//移除右侧已选项
			$("#" + rid).remove();
			
			var rli = "rli_" + $(this).val();
			$("#" + rli).remove();
			
			if (manCount > 0) {
				manCount = manCount - 1;
			}
		}		
	});
	
	if (checkCount == 0) {
		alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_49"));
		return;
	}
	$("#manCount").html(manCount);
};