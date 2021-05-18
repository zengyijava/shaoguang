$(document).ready(function() {
	getLoginInfo("#hiddenValueDiv");
	//获取所有的发送内容
		$("#singledetail").dialog({
			autoOpen: false,
			height:250,
			width: 250,
			modal: true,
			close:function(){
			}
		});
});
//弹出框
function shownamedialog(t){
	$("#msg").empty();
	$("#msg").append($(t).children("label").text().replaceAll("<","&lt;").replaceAll(">","&gt;"));
	$("#singledetail").dialog("option","title", getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_69"));
	$("#singledetail").dialog("open");
}

//返回列表 
function backmgr(){
	var pathUrl = $("#pathUrl").val();
	var lguserid = $("#lguserid").val();
	window.location.href = pathUrl+"/aud_auditpro.htm?method=find&lguserid="+lguserid;
}
//上一步修改界面 
function backUpdateInstallobj(){
	operationBtn(1);
	var pathUrl = $("#pathUrl").val();
	var lguserid = $("#lguserid").val();
	var flowid = $("#flowid").val();
	var pathtype = "2";
	window.location.href = pathUrl+"/aud_auditpro.htm?method=toInstallObj&pathtype=2&lguserid="+lguserid+"&flowid="+flowid;
	operationBtn(2);
}


//处理按钮
function operationBtn(type){
	if(type == 1){
		$("#upbtn").attr("disabled","disabled");
		$("#savebtn").attr("disabled","disabled");
	}else if(type == 2){
		$("#upbtn").attr("disabled","");
		$("#savebtn").attr("disabled","");
	}
}