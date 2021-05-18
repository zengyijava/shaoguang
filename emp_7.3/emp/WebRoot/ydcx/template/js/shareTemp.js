$(document).ready(function() {
	$("#left").click(function() {
		treeLoseFocus();
	});
	$("#left").dblclick(function() {
		moveIn();
		treeLoseFocus();
	});
	$("#right").dblclick(function() {
		moveOut();
	});
});

function moveIn() {
	var zTree = window.frames['sonFrame'].returnZTree();
	if(zTree.getSelectedNode()==null&&$("#left option:selected").size() == 0)
	{
		//alert("请选择机构或操作员！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qxzjghczy"));
		return;
	}

	if($("#left option:selected").size()>0){
		addUser();
	}else{
		if(zTree.getSelectedNode()!=null){
			window.frames['sonFrame'].zTreeDbClick(null,null,zTree.getSelectedNode()); 
		}
	}
}

// 选择操作员移到右边
function addUser() {
	var tsmsgFlag = "1";
	//当前登录操作员Id
	var lguserid=$("#lguserid").val();
	//模板创建者id
	var userid=$("#userid").val();
	$("#left option:selected").each(function() {
		//左边选中操作员遍历
		var lfuserid = parseInt($(this).val());
		if(lfuserid==lguserid||lfuserid==userid){
			//alert("不能共享给自己或模板创建人！");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_bngxgzjhmbcjr"));
			return;
		}
		// 这里判断类型 1 是机构 2是操作员
			var isdeporuser = $(this).attr("isdeporuser");
			// 成员名称
			var name = $(this).text();
			// 循环判断是否存在操作员获取机构
			var isflag = "1";
			var iscontinue = "1";
			$("#right option").each(function() {
				if (iscontinue == "2") {
					return;
				}
				var rightobjtype = $(this).attr("isdeporuser");
				var rightobjvalue = parseInt($(this).val());
				if (rightobjtype == "2" && (rightobjvalue == lfuserid)) {
					isflag = "2";
					iscontinue = "2";
					tsmsgFlag = "2";
				}
			});
			if (isflag == "1") {
				//$("#right").append("<option value=\'" + lfuserid+ "\' isdeporuser='2'>[操作员]" + name+ "</option>");
				$("#right").append("<option value=\'" + lfuserid+ "\' isdeporuser='2'>["+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_czy")+"]" + name+ "</option>");
			}
		});
	if (tsmsgFlag == "2") {
		//alert("选择记录重复，将自动过滤！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_xzjlcf"));
	}
}

// 移出右边的单个或者多个选择的对象
function moveOut() {
	if ($("#right option:selected").size() == 0) {
		return;
	}
	$("#right option:selected").each(function() {
			$(this).remove();
		});
}

// 选择机构移到右边
function addSeldep(treeNode,has) {
	var ids=new Array();
	if(has){
		ids=getChildren(ids,treeNode);
		for(var i=0;i<ids.length;i++){
			var id=ids[i].split("##");
			addDep(id[0],id[1]);
		}
	}else{
		addDep(treeNode.id, treeNode.name);
	}
}

function addDep(depid,depname) {
	// 是否填加
	var isflag = "1";
	// 是否有相同记录走下去
	var iscontinue = "1";
	// 是否提示是否重复记录
	var tsmsgFlag = "1";
	$("#right option").each(function() {
		if (iscontinue == "2") {
			return;
		}
		var rightobjtype = $(this).attr("isdeporuser");
		var rightobjvalue = parseInt($(this).val());
		if (rightobjtype == "1" && (rightobjvalue == parseInt(depid))) {
			isflag = "2";
			iscontinue = "2";
			tsmsgFlag = "2";
		}
	});
	if (isflag == "1") {
		//$("#right").append("<option value=\'" + depid + "\' isdeporuser='1'>[机构]"+ depname + "</option>");
		$("#right").append("<option value=\'" + depid + "\' isdeporuser='1'>["+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_jg")+"]"+ depname + "</option>");
	}
/**	if (tsmsgFlag == "2") {
		alert("选择记录重复，将自动过滤！");
	}*/
}

function treeLoseFocus()
{
	$("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
	$("#auditdepid").val("");
	$("#auditdepname").val("");
}
function router()
{
 moveIn();
}
//查询
function searchbyname(){
	//企业 编码
	var lgcorpcode = $("#lgcorpcode").val();
	//查询的名称
	var searchname = $("#searchname").val();
	treeLoseFocus();
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl+"/tem_mmsTemplate.htm", {method:"getSysuserByDepId",searchname:searchname,lgcorpcode:lgcorpcode}, function(returnmsg){
		if(returnmsg != "" && returnmsg.indexOf("suceess#") != -1){
			var arr = returnmsg.split("#");
			$("#left").empty();
			$("#left").html(arr[1]);
		}
	});
}
//ids是一个数组 返回结果数组     treeNode是选中的节点
function getChildren(ids,treeNode){
	ids.push(treeNode.id+"##"+treeNode.name);
			for(var obj in treeNode.nodes){
				getChildren(ids,treeNode.nodes[obj]);
			}
	 return ids;
}
