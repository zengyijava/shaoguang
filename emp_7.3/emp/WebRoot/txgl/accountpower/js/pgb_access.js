function changeBindType(i)
{
	if(i==0)
	{
		$("#depDiv").show();
		$("#userDiv").hide();
	}else
	{
		$("#userDiv").show();
		$("#depDiv").hide();
	}
}

//获取人员代码
setting2 = {    			    
		async : true,
		asyncUrl :"pgb_spBind.htm?method=createUserTree", //获取节点数据的URL地址
		
		checkable : true,
	    checkStyle : "checkbox",
	    checkType : { "Y": "s", "N": "s" },	
	    
		isSimpleData: true,
		rootPID : -1,
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		asyncParam: ["depId"],
		callback: {				  
			//beforeAsync: function(treeId, treeNode) {return false;},
			change: zTreeOnClick2,
			asyncSuccess:function(event, treeId, treeNode, msg){
				if(!treeNode){
				   var rootNode = zTree2.getNodeByParam("level", 0);
				   zTree2.expandNode(rootNode, true, false);
				}
			}
		}
};


//隐藏人员树形控件
function showMenu() {
	hideMenu2();
	var sortSel = $("#depNam");
	var sortOffset = $("#depNam").offset();
	$("#depDiv").toggle();
}

//隐藏机构树形控件
function showMenu2() {
	hideMenu();
	var sortSel = $("#userName");
	var sortOffset = $("#userName").offset();
	$("#userDiv").toggle();
}

//隐藏机构树形控件
function hideMenu() {
	$("#depDiv").hide();
}


//隐藏人员树形控件
function hideMenu2() {
	$("#userDiv").hide();
}


//选中的人员显示文本框
function zTreeOnClick2(event, treeId, treeNode) {
	if (treeNode) {				
		var zTreeNodes2=zTree2.getChangeCheckedNodes();
		
		var pops="";
		var params=[]; //获取人员字符串     				
		for(var i=0; i<zTreeNodes2.length; i++){				
			pops+=zTreeNodes2[i].name+";";
			params[i]=zTreeNodes2[i].id;	//人员编号     								
		}					
		$("#userName").attr("value", pops); //设置人员属性
		$("#userString").attr("value", params);	//设置人员代码
		
		//if(zTreeNodes2.length==0){
		
		// $("#userName").attr("value", "");
		// cleanSelect_user();
		//}
	}
	
}

//选中的人员显示文本框
function zTreeOnClickOK2() {
	
	    hideMenu2();
		var zTreeNodes2=zTree2.getChangeCheckedNodes();
		
		var pops="";				
		for(var i=0; i<zTreeNodes2.length; i++){
		
			pops+=zTreeNodes2[i].name+";";					
		}					
		$("#userName").attr("value", pops);
		
		//if(zTreeNodes2.length==0){
		
		 //$("#userName").attr("value", "");
		 //cleanSelect_user();
		//}
		//$("#depNam").attr("value", "");			
		//cleanSelect_dep();
 }

		// 加载人员/机构树形控件
function reloadTree() {
	//hideMenu();
	//hideMenu2();
	zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);		
}

function black()
{
	var url = 'pgb_accessPri.htm';
	var conditionUrl = "";
	if(url.indexOf("?")>-1)
	{
		conditionUrl="&";
	}else
	{
		conditionUrl="?";
	}
	$("#corpCode").find(" input").each(function(){
		conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
	});
	window.location=url+conditionUrl;
}