$(document).ready(function() {
	getLoginInfo("#hiddenValueDiv");
	noyinhao("#depName");
	noyinhao("#sysName");
	closeTreeFun(["dropMenu"]);
	
	$("#toggleDiv").toggle(function() {
		$("#condition").hide();
		$(this).addClass("collapse");
	}, function() {
		$("#condition").show();
		$(this).removeClass("collapse");
	});

	$("#content tbody tr").hover(function() {
		$(this).addClass("hoverColor");
		}, function() {
		$(this).removeClass("hoverColor");
	});
	$("input[name='checkall2']").each(function(index){
		$(this).click(
			function(){
				$("input[name='sysUserName']").attr("checked",$(this).attr("checked")); 
			}
		);
	});
	CloseTreePlugIN();
});


function noyinhao(obj)
{  
	var isIE = false;
	var isFF = false;
	var isSa = false;
	if ((navigator.userAgent.indexOf("MSIE") > 0)
			&& (parseInt(navigator.appVersion) >= 4))
		isIE = true;
	if (navigator.userAgent.indexOf("Firefox") > 0)
		isFF = true;
	if (navigator.userAgent.indexOf("Safari") > 0)
		isSa = true;
	$(obj).keypress(function(e) {
		var iKeyCode = window.event ? e.keyCode
				: e.which;
		var vv = ((iKeyCode == 39)
				|| (iKeyCode == 34));
		if (vv) {
			if (isIE) {
				event.returnValue = false;
			} else {
				e.preventDefault();
			}
		}
	});
}

var zTree;
var setting;
var pathUrl = $("#pathUrl").val();
function dropSysMenu()
{
	$("#dropMenu").toggle();
	setting.asyncUrl = pathUrl+"/epl_permissions.htm?method=createTree2&lguserid="+$("#lguserid").val();
	zTree=$("#ztree").zTree(setting,zNodes);
}

setting = {
	async : true,
	asyncUrl : pathUrl+"/epl_permissions.htm?method=createTree2", //获取节点数据的URL地址
	isSimpleData : true,
	rootPID : 0,
	treeNodeKey : "id",
	treeNodeParentKey : "pId",
	callback : {
		click: zTreeOnClick,
		beforeAsync: function(treeId, treeNode) {
			zTree.setting.asyncUrl= pathUrl+"/epl_permissions.htm?method=createTree2&depId="+treeNode.id+"&lguserid="+$("#lguserid").val();
		},
		asyncSuccess:function(event, treeId, treeNode, msg){
			if(!treeNode){
			   var rootNode = zTree.getNodeByParam("level", 0);
			   zTree.expandNode(rootNode, true, false);
			}
		}
	}
};
var zNodes =[];
function zTreeOnClick(event, treeId, treeNode)
{
	$("#depname").attr("value",treeNode.name);
	$("#dropMenu").hide();
	$("#depid").val(treeNode.id);
}

function ok()
{
	var sysUserName="";
    $(":checkbox[name='sysUserName']:checked").each(function(){sysUserName=sysUserName+$(this).val()+","});
    if(sysUserName=="")
    {
    	alert(getJsLocaleMessage('employee','employee_alert_115'));
    	return;
    	
    }
    $(window.parent.document).find("#btnok").attr("disabled","disabled");

    sysUserName=sysUserName.substring(0,sysUserName.length-1);
	if(confirm(getJsLocaleMessage('employee','employee_alert_116')))
	{
		var lguserid=$("#lguserid").val();
		var lgcorpcode=$("#lgcorpcode").val();
		$.ajax({
			url:"epl_permissions.htm?method=getInfo",
			type: "POST",
			data:{bookType:"employee",opType:"add",selDepId:$("#selDepId").val(),lguserid:lguserid,lgcorpcode:lgcorpcode,sysUserId:sysUserName},
			success: function(data){
				if(data=="true")
				{
					alert(getJsLocaleMessage('employee','employee_alert_117'));
					cancelDisabled();
				}
				else
				{
					cancelDisabled();
					alert(getJsLocaleMessage('employee','employee_alert_34'));
				}
				window.parent.submitForm();
				window.parent.closeBinddiv();
			}
		});
	}else{
		cancelDisabled();
	}
}

function cancelDisabled()
{
	$(window.parent.document).find("#btnok").removeAttr("disabled");
}

function zTreeOnClickOK3() {
	$("#dropMenu").hide();
}
function cleanSelect_dep3()
{
    zTree.refresh();
    $("#depname").attr("value","");	
    $("#dropMenu").hide();
}