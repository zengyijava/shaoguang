var setting3;
var zNodes3 = [];
var setting2;
var zNodes2 = [];
$(document).ready(function() {
	 getLoginInfo("#getloginUser");
	 var lgcorpcode=GlobalVars.lgcorpcode;
	 var lguserid=GlobalVars.lguserid;
	 setting2 = {
			    checkable : true,
			    checkStyle : "checkbox",
			    checkType : { "Y": "s", "N": "s" },
			    async : true,
		        asyncUrl : "mmt_mmsTask.htm?method=createUserTree2&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode, //获取节点数据的URL地址
				isSimpleData: true,
				rootPID : 0,
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
	        //选中的人员显示文本框
	function zTreeOnClick2(event, treeId, treeNode) {
		if (treeNode) {				
			var zTreeNodes2=zTree2.getChangeCheckedNodes();
			
			var pops="";
			var userids = "";				
			for(var i=0; i<zTreeNodes2.length; i++){
			   
				pops+=zTreeNodes2[i].name+";";	
				userids+=zTreeNodes2[i].id.replace("u","")+","; 				
			}					
			$("#userName").attr("value", pops);
			$("#userid").attr("value",userids);
			if(zTreeNodes2.length==0){
			 $("#userid").attr("value","");
			 $("#userName").attr("value", "");
			}
		}
	}
	        setting3 = {									
			async : true,				
			asyncUrl : "mmt_mmsTask.htm?method=createDeptTree&lguserid="+lguserid, //获取节点数据的URL地址
			
			//checkable : true,
		    //checkStyle : "radio",
		    //checkType : { "Y": "s", "N": "s" },
		    isSimpleData : true,
			rootPID : 0,
			treeNodeKey : "id",
			treeNodeParentKey : "pId",
			asyncParam: ["depId"],	
			
			callback: {
					
				click: zTreeOnClick3,					
				asyncSuccess:function(event, treeId, treeNode, msg){
					if(!treeNode){
					   var rootNode = zTree3.getNodeByParam("level", 0);
					   zTree3.expandNode(rootNode, true, false);
					}
				//zTree3.expandAll(false);
				}
			}
		};
		
	        reloadTree();
});
//选中的机构显示文本框
function zTreeOnClick3(event, treeId, treeNode) {
	if (treeNode) {				
		//var zTreeNodes3=zTree3.getChangeCheckedNodes();
		var pops="";
		var depts ="";
		//for(var i=0; i<zTreeNodes3.length; i++){					
			//pops+=zTreeNodes3[i].name+";";	
			//depts+=zTreeNodes3[i].depId+",";			
		//}
		$("#depNam").attr("value", treeNode.name);	
		$("#deptid").attr("value",treeNode.id);
		//if(zTreeNodes3.length==0){
		
		//$("#depNam").attr("value", "");	
		//$("#deptid").attr("value","");				
		//}
	}
}	
//隐藏人员树形控件
function showMenu() {
	//hideMenu2();
	$("#dropMenu2").hide();
	var sortSel = $("#depNam");
	var sortOffset = $("#depNam").offset();
	$("#dropMenu").toggle();
}
//选中的机构显示文本框
function zTreeOnClickOK3() {
	$("#dropMenu").hide();
}
function cleanSelect_dep3()
{
	var checkNodes = zTree3.getCheckedNodes();
    for(var i=0;i<checkNodes.length;i++){
     checkNodes[i].checked=false;
    }
    zTree3.refresh();
    //$("#depNam").attr("value", "请选择");
    $("#depNam").attr("value", getJsLocaleMessage("ydcx","ydcx_cxyy_dsxxck_qxz"));	
	$("#deptid").attr("value","");	
}
// 加载人员/机构树形控件
function reloadTree() {
	$("#dropMenu").hide();
	$("#dropMenu2").hide();
	setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
	zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
	zTree3.expandAll(true);
	setting2.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
	zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);		
}
//选中的人员显示文本框
function zTreeOnClickOK2() {
	
	    $("#dropMenu2").hide();
		var zTreeNodes2=zTree2.getChangeCheckedNodes();
		
		var pops="";
		var userids ="";				
		for(var i=0; i<zTreeNodes2.length; i++){
		
			pops+=zTreeNodes2[i].name+";";	
			userids+=zTreeNodes2[i].id.replace("u","")+","; 			
		}					
		$("#userName").attr("value", pops);
		$("#userid").attr("value",userids);
		if(zTreeNodes2.length==0){
		 $("#userid").attr("value","");
		 //$("#userName").attr("value", "请选择");
		 $("#userName").attr("value", getJsLocaleMessage("ydcx","ydcx_cxyy_dsxxck_qxz"));
		}
 }
 function cleanSelect_dep()
{
	var checkNodes = zTree2.getCheckedNodes();
    for(var i=0;i<checkNodes.length;i++){
     checkNodes[i].checked=false;
    }
    zTree2.refresh();
    $("#userid").attr("value","");
    //$("#userName").attr("value", "请选择");
    $("#userName").attr("value", getJsLocaleMessage("ydcx","ydcx_cxyy_dsxxck_qxz"));
} 
//隐藏机构树形控件
function showMenu2() {
	$("#dropMenu").hide();
	var sortSel = $("#userName");
	var sortOffset = $("#userName").offset();
	$("#dropMenu2").toggle();
}
//提交网关状态：//审批状态：选择框
$(function(){
	  $('#state,#sstate').isSearchSelect({'width':'180','zindex':1,'isInput':false});
 });
