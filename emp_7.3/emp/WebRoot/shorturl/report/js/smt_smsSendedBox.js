	var zTree3;
	var zTree2;				
	var setting3;
	var setting2;	
	var zNodes2 = [];
	var zNodes3 = [];
	var htmName;

$(document).ready(function() {
	            getLoginInfo("#smssendparams");//加载头文件内容 
				var windowheight = $(document).height();
				$("#content tbody tr #msgcont a").hover(function(e){
					var tooltip = "";
					tooltip = "<div id='tooltip' style='background:#FFFFE0;border:1px solid #000;'><table><tr><td word-break: break-all;><xmp style='word-break: break-all;white-space:normal;'>"
						+$(this).children("label").children("xmp").html()+"</xmp></td></tr></div>";
					setTimeout(function(){
							$('#tooltip').remove();
							$('body').append(tooltip);
  							var divwidth =document.getElementById("tooltip").offsetWidth+20;
							$('#tooltip')
								.css({
									"opacity":"1",
									"top":(e.pageY-20)+"px",
									"left":(e.pageX-divwidth)+"px"
								});	
							//-----------调整弹出框位置
							var divtop = $("#tooltip").offset().top;
							var divleft = $("#tooltip").offset().left;
							var divheight = $("#tooltip").height();
							if((divtop+divheight)>windowheight){
								$('#tooltip')
								.css({
									"top":(windowheight-divheight-20)+"px",
									"left":divleft+"px"
								});	
								}
							//----------
						},250);
					},function(){
						setTimeout(function(){
							if (null != document.getElementById("tooltip"))
							{
								
								document.body.removeChild(document.getElementById("tooltip"));
							}
						},250);
					}
				);
				
			     //var lgcorpcode =$("#lgcorpcode").val();
				 //var lguserid =$("#lguserid").val();
				var lgcorpcode=GlobalVars.lgcorpcode;
				var lguserid=GlobalVars.lguserid;
			    // var lgguid =$("#lgguid").val();
				//请求名称
				htmName=$("#htmName").val();
			    setting2.asyncUrl = htmName+"?method=createUserTree2&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
			    //机构树
			    setting3.asyncUrl = htmName+"?method=createDeptTree&lguserid="+lguserid;
				setting2.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
				zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);
				zTree2.expandAll(true);
				reloadTree(zNodes3);
				
			});

	function rtime() {
	    var max = "2099-12-31 23:59:59";
	    var v = $("#sendtime").attr("value");
		//if(v.length != 0)
		//{
		//    var year = v.substring(0,4);
		//	var mon = v.substring(5,7);
		//	var day = 31;
		//	if (mon != "12")
		//	{
		//	    mon = String(parseInt(mon,10)+1);
		//	    if (mon.length == 1)
		//	    {
		//	        mon = "0"+mon;
		//	    }
		//	    switch(mon){
		//	    case "01":day = 31;break;
		//	    case "02":day = 28;break;
		//	    case "03":day = 31;break;
		//	    case "04":day = 30;break;
		//	    case "05":day = 31;break;
		//	    case "06":day = 30;break;
		//	    case "07":day = 31;break;
		//	    case "08":day = 31;break;
		//	    case "09":day = 30;break;
		//	    case "10":day = 31;break;
		//	    case "11":day = 30;break;
		//	    }
		//	}
		//	else
		//	{
		//	    year = String((parseInt(year,10)+1));
		//	    mon = "01";
		//	}
		//	max = year+"-"+mon+"-"+day+" 23:59:59"
	    //}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',enableInputMask:false});
    }

	function stime(){
	    var max = "2099-12-31 23:59:59";
	    var v = $("#recvtime").attr("value");
	    var min = "1900-01-01 00:00:00";
		//if(v.length != 0)
		//{
		//    max = v;
		//    var year = v.substring(0,4);
		//	var mon = v.substring(5,7);
		//	if (mon != "01")
		//	{
		//	    mon = String(parseInt(mon,10)-1);
		//	    if (mon.length == 1)
		//	    {
		//	        mon = "0"+mon;
		//	    }
		//	}
		//	else
		//	{
		//	    year = String((parseInt(year,10)-1));
		//	    mon = "12";
		//	}
		//	min = year+"-"+mon+"-01 00:00:00"
		//}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',enableInputMask:false});
	}
	
	//获取机构代码
	setting3 = {									
			async : true,				
			asyncUrl : htmName+"?method=createDeptTree", //获取节点数据的URL地址
			
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
	
	//获取人员代码
	/*setting2 = {				
			checkable : true,
		    checkStyle : "checkbox",
		    checkType : { "Y": "s", "N": "s" },
		    
			isSimpleData: true,
			rootPID : -1,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			callback: {				  
				beforeAsync: function(treeId, treeNode) {return false;},
				change: zTreeOnClick2,
				asyncSuccess:function(event, treeId, treeNode, msg){
				zTree2.expandAll(false);
				}
			}
	};*/
	
	 setting2 = {
			    checkable : true,
			    checkStyle : "checkbox",
			    checkType : { "Y": "s", "N": "s" },
			    async : true,
		        asyncUrl : htmName+"?method=createUserTree2", //获取节点数据的URL地址
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

	//隐藏人员树形控件
	function showMenu() {
		hideMenu2();
		var sortSel = $("#depNam");
		var sortOffset = $("#depNam").offset();
		$("#dropMenu").toggle();
	}

	//失去焦点隐藏人员树形控件
	function hideMenuOnblur()
	{
		hideMenu2();
		var sortSel = $("#depNam");
		var sortOffset = $("#depNam").offset();
		$("#dropMenu").hide();
	}
	
	//隐藏机构树形控件
	function showMenu2() {
		hideMenu();
		var sortSel = $("#userName");
		var sortOffset = $("#userName").offset();
		$("#dropMenu2").toggle();
	}

	//隐藏人员树形控件
	function hideMenu() {
		$("#dropMenu").hide();
	}


	//隐藏人员树形控件
	function hideMenu2() {
		$("#dropMenu2").hide();
	}

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


	//选中的机构显示文本框
	function zTreeOnClickOK3() {
		hideMenu();
		
		/*var zTreeNodes3=zTree3.getChangeCheckedNodes();
		
		var pops="";
		var deptids = "";
		for(var i=0; i<zTreeNodes3.length; i++){					
			pops+=zTreeNodes3[i].name+";";
			deptids+=zTreeNodes3[i].depId+",";					
		}
		$("#depNam").attr("value", pops);	
		$("#deptid").attr("value",deptids);
		if(zTreeNodes3.length==0){
			$("#depNam").attr("value", "请选择");	
			$("#deptid").attr("value","");				
		}*/
		//treeNode=zTree3.getSelectedNode();  
		//$("#depNam").attr("value", treeNode.name);	
		//$("#deptid").attr("value",treeNode.id);
	}


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

	//选中的人员显示文本框
	function zTreeOnClickOK2() {
		
		    hideMenu2();
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
			 $("#userName").attr("value", getJsLocaleMessage('dxzs','dxzs_ssend_alert_159'));
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
	    $("#userName").attr("value", getJsLocaleMessage('dxzs','dxzs_ssend_alert_159'));
	} 
	
	function cleanSelect_dep3()
	{
		var checkNodes = zTree3.getCheckedNodes();
	    for(var i=0;i<checkNodes.length;i++){
	     checkNodes[i].checked=false;
	    }
	    zTree3.refresh();
	    $("#depNam").attr("value", getJsLocaleMessage('dxzs','dxzs_ssend_alert_159'));	
		$("#deptid").attr("value","");	
	}

		// 加载人员/机构树形控件
	function reloadTree(zNodes3) {
		hideMenu();
		hideMenu2();
		setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
		zTree3.expandAll(true);
		//zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);		
	}

	function zTreeBeforeAsync(treeId, treeNode) {
		if (treeNode.id == 1)
			return false;
		return true;
	}


