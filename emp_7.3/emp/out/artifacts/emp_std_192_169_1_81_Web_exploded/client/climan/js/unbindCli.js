function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
		a[i].checked =e.checked;    
}

var zTree;
var setting;
function dropSysMenu()
{
	$("#dropMenu").toggle();
	setting.asyncUrl="cli_permissions.htm?method=createTree2&lguserid="+$("#lguserid").val();
	zTree=$("#ztree").zTree(setting,zNodes);
}

setting = {
	async : true,
	asyncUrl : "cli_permissions.htm?method=createTree2", //获取节点数据的URL地址
	isSimpleData : true,
	rootPID : 0,
	treeNodeKey : "id",
	treeNodeParentKey : "pId",
	callback : {
		click: zTreeOnClick,
		beforeAsync: function(treeId, treeNode) {
			zTree.setting.asyncUrl="cli_permissions.htm?method=createTree2&depId="+treeNode.id+"&lguserid="+$("#lguserid").val();
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
	$("#depName").attr("value",treeNode.name);
	$("#dropMenu").hide();
	$("#depid").val(treeNode.id);
}
//清空
function clearTreeValue()
{
	$("#depName").attr("value","");
	$("#dropMenu").hide();
	$("#depid").val("");
}

//点击确定按钮
function sure()
{
	$("#dropMenu").hide();
}

function ok()
{
	var sysUserName="";
    $(":checkbox[name='sysUserName']:checked").each(function(){sysUserName=sysUserName+$(this).val()+","});
    if(sysUserName=="")
    {
    	//alert("未选择操作员！");
    	alert(getJsLocaleMessage('client','client_js_unbindCli_noselectopt'));
    	return;
    	
    }
    $(window.parent.document).find("#btnok").attr("disabled","disabled");

    sysUserName=sysUserName.substring(0,sysUserName.length-1);
	if(confirm(getJsLocaleMessage('client','client_js_unbindCli_addoptpem')))
	{
		var lguserid=$("#lguserid").val();
		var lgcorpcode=$("#lgcorpcode").val();
		$.ajax({
			url:"cli_permissions.htm?method=getInfo",
			type: "POST",
			data:{bookType:"client",opType:"add",selDepId:$("#selDepId").val(),lguserid:lguserid,lgcorpcode:lgcorpcode,sysUserId:sysUserName},
			success: function(data){
				if(data=="true")
				{
						//$("#sysDiv").css("display","none");
					//alert("添加成功！");
					alert(getJsLocaleMessage('client','client_common_addsuc'));
					cancelDisabled();
					//$('#com_add_Dom2').dialog('close');
					//submitForm();
				}
				else
				{
					cancelDisabled();
					//alert("添加失败！");
					alert(getJsLocaleMessage('client','client_common_addfalse'));
				}
				window.parent.submitForm();
				window.parent.closeBinddiv();
				//window.parent.location.href="mcsa_mcsPermissions.htm?method=find&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
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