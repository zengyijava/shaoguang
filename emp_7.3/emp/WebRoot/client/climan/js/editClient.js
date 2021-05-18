function inputTipText(){
	//所有样式名中含有graytext的input
	$("input[class*=graytext]").each(function(){
		var oldVal=$(this).val(); //默认的提示性文本
		$(this)
		.css({'color':'#000'}) //灰色
		.focus(function(){
		if($(this).val()!=oldVal)
			{$(this).css({'color':'#000'})}
		else
			{$(this).css({'color':'#000'})}
		})
		.blur(function(){
		if($(this).val()=="")
			{$(this).val(oldVal).css({'color':'#000'})}
		})
		.keydown(function(){
			$(this).css({'color':'#000'})
		})
	})
}
function show(result){
	if( true==result)
	{
		//alert("操作成功！");
		alert(getJsLocaleMessage('client','client_common_optsuc'));
		window.location.href = "cli_addrBook.htm?method=find&lguserid="
			+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val()+"&skip=ture";
	}else if(false==result)
	{
			//alert("操作失败！");
		alert(getJsLocaleMessage('client','client_common_optfalse'));
			return;
	}
}
var zTree1;
var setting;
var formName = "addForm";
var lfclientid = $("#lfclientid").val();
var userid = $("#userid").val();
setting = {
		checkable : true,
	    checkStyle : "checkbox",
    checkType : { "Y": "s", "N": "s" },
	async : true,
	asyncUrl :"cli_addrbookDepTree.htm?method=getClientSecondDepJsonedit&cleid="
		+lfclientid+"&lguserid="+userid, //获取节点数据的URL地址
	isSimpleData: true,
	rootPID : -1,
	treeNodeKey: "id",
	treeNodeParentKey: "pId",
	callback: {
		change: zTreeOnClick,
		asyncSuccess:function(event, treeId, treeNode, msg){
			if(!treeNode)
			{
				var rootNode = zTree1.getNodeByParam("level",0);
				zTree1.expandNode(rootNode,true,false);
			}
		},
		beforeAsync:function(treeId,treeNode){
			zTree1.setting.asyncUrl="cli_addrbookDepTree.htm?method=getClientSecondDepJsonedit&cleid="
				+lfclientid+"&lguserid="+userid+"&depId="+treeNode.id;
			}
		}
};

var zNodes =[];

function showMenu() {
	var isIE = (document.all) ? true : false;
	 var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
if(isIE6){
 	$("#sex ").show();
 }
var sortSel = $("#depNam");
var sortOffset = $("#depNam").offset();
$("#dropMenu").toggle();
if(isIE6){
	if(document.getElementById('dropMenu').style.display=='none'){
	 	$("select").show();
		 }
	}
}
function hideMenu() {
	$("#dropMenu").hide();
}

//点击机构进行过滤
function zTreeOnClick(event, treeId, treeNode) {
	if (treeNode) {
		//选中的机构
	var zTreeusedNodes = zTree1.getCheckedNodes(true);
	//未选中的机构
	var zTreeunUsedNodes = zTree1.getCheckedNodes(false);
	//页面上存在的机构
	var depidobj = $("#depId").val();
	//默认是未选择 1   有选择记录则是 2
	var isFlag = "1";
	//新增加的结点
	var newaddNodes = "";
	//新增加的结点名称
	var newaddNames = "";
	//要移除的结点
	var pushNodes = "";
	var addNodes = "";
	// #^@;depid;depname#^@ 格式
	var isExistDep = $("#isExistDep").val();
	if(depidobj != null && depidobj.length>0){
		isFlag = "2";
		//根据页面上获取的隐藏域的值进行   新增结点的判断
		var allobjNodes = ","+depidobj;
		if(zTreeusedNodes != null && zTreeusedNodes.length>0){
			for(var i=0; i<zTreeusedNodes.length; i++){
				var selectid = zTreeusedNodes[i].id;
				selectid = ","+ selectid + ",";
				//说明该结点存在   ,不处理
				if(allobjNodes.indexOf(selectid) >= 0){
				}else{
					//新增的结点ID
					newaddNodes = newaddNodes + zTreeusedNodes[i].id + ",";
					//新增的结点名称
					newaddNames = newaddNames + zTreeusedNodes[i].name + "#";
					isExistDep = isExistDep + ";" + zTreeusedNodes[i].id + ";" + zTreeusedNodes[i].name + "#^@";
				}
			}
		}
		addNodes = ","+depidobj;
		//未选中的结点
		var unselectNodes = "";
		//获取弹出框中未被选中的结点，不包括未展开过的结点
		if(zTreeunUsedNodes != null && zTreeunUsedNodes.length>0){
			for(var j=0; j<zTreeunUsedNodes.length;j++){
				unselectNodes = unselectNodes + zTreeunUsedNodes[j].id  + ",";
			}
		}
		//这里处理     那些页面已经存在但在弹出框中被取消选中的结点
		if(unselectNodes.length >0){
			unselectNodes = "," + unselectNodes;
			var objExistNodes = depidobj.split(",");
			if(objExistNodes != null && objExistNodes.length>0){
				for(var m=0; m<objExistNodes.length;m++){
					var existSelectNode = objExistNodes[m];
					if(existSelectNode != null && existSelectNode.length>0){
						existSelectNode = ","+ objExistNodes[m] + ","
						if(unselectNodes.indexOf(existSelectNode) >= 0){
							pushNodes = pushNodes + objExistNodes[m] + ",";
							if(addNodes.indexOf(existSelectNode) >= 0){
								addNodes = addNodes.replace(existSelectNode,",");
							}
						}
					}
				}
			}
		}
		depidobj = "";
		//这里的obj是新增的结点 + 除去被取消后的结点的结点
		depidobj = newaddNodes + addNodes.substring(1,addNodes.length);
	}else{
		if(zTreeusedNodes != null && zTreeusedNodes.length>0){
			for(var n=0; n<zTreeusedNodes.length; n++){
				depidobj = depidobj + zTreeusedNodes[n].id + ",";
			}
		}
	}
	if(depidobj != null && depidobj.length>0){
		if(isFlag == "2"){
			if(pushNodes != null && pushNodes.length > 0){
				var pusharr = pushNodes.split(",");
				for(var t=0;t<pusharr.length;t++){
					var pushid = pusharr[t];
					if(pushid != null && pushid.length > 0){
					    var arr = isExistDep.split("#^@");
						if(arr != null && arr.length>0){
							for(var m=0;m<arr.length;m++){
								var str = arr[m];
								if(str.indexOf(";"+pushid+";") >= 0){
									isExistDep = isExistDep.replace("#^@"+str,"");
								}
							}
						}
					}
				}
			}
			var deparr = isExistDep.split("#^@");
			var name = "";
			if(deparr != null && deparr.length>0){
				for(var a=0;a<deparr.length;a++){
					var depnamestr = deparr[a];
					if(depnamestr != "" && depnamestr.length>0){
						depnamestr = depnamestr.substring(1,depnamestr.length);
						var n = depnamestr.substring(depnamestr.indexOf(";")+1,depnamestr.length);
						name = name + n + ";";
					}
				}
			}
			$("#depNam").val(name);
			$("#depId").val(depidobj);
			$("#isExistDep").val(isExistDep);
		}else if(isFlag == "1"){
			var name = "";
			var id = "";
			var existDep = "";
			for(var i=0; i<zTreeusedNodes.length; i++){		
				name = name + zTreeusedNodes[i].name + ";";
				id = id + zTreeusedNodes[i].id+",";
				existDep = existDep + ";" + zTreeusedNodes[i].id + ";" + zTreeusedNodes[i].name + "#^@";
    		}	
			$("#depNam").val(name);
			$("#depId").val(id);
			$("#isExistDep").val(existDep);
		}
	}else{
		$("#depNam").attr("value","");
		$("#depId").attr("value","");
		$("#isExistDep").val("");
		}
	}
}

//手机号输入框输入过滤
function checkPhoneIput(str)
{
	if(str.value!=str.value.replace(/\D/g,''))
{
	str.value=str.value.replace(/\D/g,'');
	}
}

function reloadTree() {
	hideMenu();
	zTree1 = $("#dropdownMenu").zTree(setting, zNodes);
}	
$(document).ready(function(){
	inputTipText();
	closeTreeFun(["dropMenu"]);
	getLoginInfo("#hiddenValueDiv");
	reloadTree();
});

function back(){
	window.location.href="cli_addrBook.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val()+"&skip=ture";
}

function zTreeOnClickOK() {
	$("form[name='"+formName+"']").find("#dropMenu").hide();
}
//清空选择
function cleanSelect() {
	var checkNodes = zTree1.getCheckedNodes();
	for ( var i = 0; i < checkNodes.length; i++) {
		checkNodes[i].checked = false;
	}
	$("#depNam").attr("value",getJsLocaleMessage('client','client_js_addClient_clickselectorg'));
	$("#depId").attr("value","");
	zTree1.refresh();
}