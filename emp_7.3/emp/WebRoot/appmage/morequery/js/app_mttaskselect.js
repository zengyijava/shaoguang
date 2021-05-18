	var zTree;				
	var setting;	
	var zNodes = [];
	var htmName;

		$(document).ready(function() {
	            getLoginInfo("#smssendparams");//加载头文件内容 
			     var lgcorpcode =$("#lgcorpcode").val();
				 var lguserid =$("#lguserid").val();
				//请求名称
				htmName="app_mttaskselect.htm";
				//操作员树
			    setting.asyncUrl = htmName+"?method=createUserTree&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
				zTree = $("#dropdownMenu").zTree(setting, zNodes);
				zTree.expandAll(true);
				
		});


function modify(t)
	{
		$('#modify').dialog({
			autoOpen: false,
			width:250,
			   height:200
		});
		$("#msgcont").children("xmp").empty();
		$("#msgcont").children("xmp").text($(t).children("label").children("xmp").html());
		$('#modify').dialog('open');
	}
	function rtime()
	{
	    var max = "2099-12-31 23:59:59";
	    var v = $("#sendtime").attr("value");
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
    }

	function stime(){
	    var max = "2099-12-31 23:59:59";
	    var v = $("#recvtime").attr("value");
	    var min = "1900-01-01 00:00:00";

		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
	}
	
	
	 setting = {
			    checkable : true,
			    checkStyle : "checkbox",
			    checkType : { "Y": "s", "N": "s" },
			    async : true,
		        asyncUrl : htmName+"?method=createUserTree", //获取节点数据的URL地址
				isSimpleData: true,
				rootPID : 0,
				treeNodeKey: "id",
				treeNodeParentKey: "pId",
				asyncParam: ["depId"],
				callback: {				  
					change: zTreeOnClick,
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree.getNodeByParam("level", 0);
						   zTree.expandNode(rootNode, true, false);
						}
					}
				}
			 
	 };

	//隐藏人员树形控件
	function showMenu() {
		hideMenu();
		$("#dropMenu").toggle();
	}


	//隐藏人员树形控件
	function hideMenu() {
		$("#dropMenu").hide();
	}

	//选中的人员显示文本框
	function zTreeOnClick(event, treeId, treeNode) {
		if (treeNode) {				
			var zTreeNodes=zTree.getChangeCheckedNodes();
			var pops="";
			var userids = "";				
			for(var i=0; i<zTreeNodes.length; i++){
				pops+=zTreeNodes[i].name+";";	
				userids+=zTreeNodes[i].id.replace("u","")+","; 				
			}					
			$("#userName").attr("value", pops);
			$("#userid").attr("value",userids);
			if(zTreeNodes.length==0){
			 $("#userid").attr("value","");
			 $("#userName").attr("value", "");
			}
		}
	}



	//选中的人员点击确定显示文本框
	function zTreeOnClickOK() {
		
		    hideMenu();
			var zTreeNodes=zTree.getChangeCheckedNodes();
			var pops="";
			var userids ="";				
			for(var i=0; i<zTreeNodes.length; i++){
			
				pops+=zTreeNodes[i].name+";";	
				userids+=zTreeNodes[i].id.replace("u","")+","; 			
			}					
			$("#userName").attr("value", pops);
			$("#userid").attr("value",userids);
			if(zTreeNodes.length==0){
			 $("#userid").attr("value","");
			 //$("#userName").attr("value", "请选择");
			 $("#userName").attr("value", getJsLocaleMessage('appmage','appmage_js_mttaskselect_pleaseselect'));
			}
			
	 }

	function cleanSelect_dep()
	{
		var checkNodes = zTree.getCheckedNodes();
	    for(var i=0;i<checkNodes.length;i++){
	     checkNodes[i].checked=false;
	    }
	    zTree.refresh();
	    $("#userid").attr("value","");
	    //$("#userName").attr("value", "请选择");
	    $("#userName").attr("value", getJsLocaleMessage('appmage','appmage_js_mttaskselect_pleaseselect'));
	} 
	



