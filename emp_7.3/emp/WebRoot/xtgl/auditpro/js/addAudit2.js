
$(document).ready(function() {
	getLoginInfo("#hiddenValueDiv");
});
// 下一步设置 对象或者保存对象
function nextsaveobj(operation){
	operationBtn(1);
	var optionSize = $(window.frames['flowFrame'].document).find("#right option").size();
	// if(optionSize == 0){
	// alert("请设置审核对象！");
	// operationBtn(2);
	// return;
	// }
	// 设置的机构IDS
	var depidstr = "";
    //机构包含状态
    var depcontainstr = "";
	// 设置的操作员IDS
	var useridstr = "";
	$(window.frames['flowFrame'].document).find("#right option").each(function(){
		var id = $(this).val();
		// 1是机构 2是操作员
		var type = $(this).attr("isdeporuser");
		 if(type == "1"){
			useridstr = useridstr + id + ",";
		}else if(type == "2"){
			depidstr = depidstr + id + ",";
            depcontainstr = depcontainstr + $(this).attr("iscontain")+',';
		}
	});
	var flowid = $("#flowid").val();
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	var pathtype = $("#pathtype").val();
    var data = {method:"installaudobj",depidstr:depidstr,depcontainstr:depcontainstr,useridstr:useridstr,lguserid:lguserid,flowid:flowid,pathtype:pathtype};
	EmpExecutionContext.log(pathUrl+"/aud_auditpro.htm",data);
	$.post(pathUrl+"/aud_auditpro.htm", data, function(returnmsg){
		if(returnmsg.indexOf("html") > 0){
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_1"));
			operationBtn(2);
		    return;
    	}else if(returnmsg == "success"){
			if(operation == 1){// 下一步
				// 审核流程ID
				operationBtn(2);
				window.location.href = pathUrl+"/aud_auditpro.htm?method=tofinishAudit&lguserid="+lguserid+"&flowid="+flowid;
			}else if(operation == 2){// 保存
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_16"));
				operationBtn(2);
				window.location.href = pathUrl+"/aud_auditpro.htm?method=find&lguserid="+lguserid+"&isOperateReturn=true";
				return;
			}
		}else if(returnmsg == "fail"){
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_17"));
			operationBtn(2);
			return;
		}else{
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_7"));
			operationBtn(2);
			return;
		}
	});
}

//上一步 
function backUpdateAudit(){
	var pathUrl = $("#pathUrl").val();
	var lguserid = $("#lguserid").val();
	var flowid = $("#flowid").val();
	var pathtype = "2";
	window.location.href = pathUrl+"/aud_auditpro.htm?method=toAddAuditPro&pathtype=2&lguserid="+lguserid+"&flowid="+flowid;
}

//处理按钮
function operationBtn(type){
	if(type == 1){
		$("#upbtn").attr("disabled","disabled");
		$("#nextbtn").attr("disabled","disabled");
		$("#savebtn").attr("disabled","disabled");
		$("#cancelbtn").attr("disabled","disabled");
	}else if(type == 2){
		$("#upbtn").attr("disabled","");
		$("#nextbtn").attr("disabled","");
		$("#savebtn").attr("disabled","");
		$("#cancelbtn").attr("disabled","");
	}
}

//点取消
function cancelAudit(){
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	window.location.href = pathUrl+"/aud_auditpro.htm?method=find&lguserid="+lguserid+"&isOperateReturn=true";
}