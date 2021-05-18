	var zTree;				
	var setting;	
	var zNodes = [];
	var htmName;

		function modify(t)
		{
			$('#modify').dialog({
			autoOpen: false,
			width:250,
		    height:200
		});
		$("#msg").empty();
		$("#msg").text($(t).children("label").children("xmp").text());
		$('#modify').dialog('open');
		}
		
		//隐藏人员树形控件
		function showMenu() {
			hideMenu();
			$("#dropMenu").toggle();
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
				 // $("#userName").attr("value", "请选择");
				 $("#userName").attr("value", getJsLocaleMessage('appmage','appmage_js_mttaskselect_pleaseselect'));
				}
		 }
		
		//隐藏人员树形控件
		function hideMenu() {
			$("#dropMenu").hide();
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