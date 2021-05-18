	function doApp(img,width,height,size)
		{
              if (img != null && img != "")
              {
            	  setImg(img,width,height,size*1024);
            	  addApp(img,"null","",width+";"+height+";"+(size*1024),"");
              }
		}
		function doApp2(addr,size)
		{
              if (addr != null && addr != "")
              {
            	  addApp("null",addr,"","",size*1024);
              }
		}
		var zTree1;
		var setting;
		var pathUrl = $("#pathUrl").val();
		setting = {
			async : true,
			asyncUrl :pathUrl+"/tem_mmsTemplate.htm?method=getMatelTree", //获取节点数据的URL地址
			isSimpleData: true,
			rootPID : 0,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			callback: {
				beforeAsync : function(treeId, treeNode) {
				return false;
			    },
				click: zTreeOnClick,
				asyncSuccess:function(event, treeId, treeNode, msg){
				//zTree1.expandAll(true);
				}
			}
		};

		var zNodes =[];

		$(document).ready(function(){
			getLoginInfo("#getloginUser");
			var lgcorpcode = $("#lgcorpcode").val();
			setting.asyncUrl = setting.asyncUrl+"&lgcorpcode="+lgcorpcode;
			reloadTree();
		});

		function showMenu() {
			var sortSel = $("#sortSel");
			var sortOffset = $("#sortSel").offset();
			$("#dropMenu").toggle();
		}
		function hideMenu() {
			$("#dropMenu").hide();
		}
		
		function zTreeOnClick(event, treeId, treeNode) {
			if (treeNode && !treeNode.isParent) {
				//var sortObj = $("#sortSel");
				//sortObj.attr("value", treeNode.name);
				//hideMenu();
				var id = (treeNode.id).substring(1);
				var type = treeNode.type;
				var address = treeNode.address;
				var width = parseFloat(treeNode.width);
				var height = parseFloat(treeNode.height);
				var size = parseFloat(treeNode.size);
				var name = treeNode.name;
				$("#type").val(type);
				$("#address").val(address);
				$("#size").val(size);
				$("#width").val(width);
				$("#height").val(height);
				if (width-200>=0){
			    	width = width*((190/width));
			    	height = height*(190/height);
			    }
			    if (height-150>=0) {
			    	width = width*(140/width);
			    	height = height*(140/height);
			    }
			    var  padding = parseFloat((140-height)/2);
			    var pathUrl = $("#pathUrl").val();
			    var fileServerUrl = $("#fileServerUrl").val();
			    if(fileServerUrl!=null&&fileServerUrl!=""){
			    	$.post(pathUrl+"/tem_mmsTemplate.htm?method=checkMmsFile", {
					url : address
					},
					function(result) {
						 if(result == "true"){
						 }else if (result == "false"){
						    //alert("素材不存在！");
						    alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_scbcz"));
						    return;
						}else{
							//alert("素材不存在！");
							alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_scbcz"));
							return;
						}
					});
			    }
			    
			    var result ;
			    var skinType = $("#skinType").val();
				if ("AMR" != type && "MID" != type && "MIDI" != type){
					result = "<img src=\'"+address+ "\' style=\'width:"+width+"px;height:"+height+"px;margin-top:"+padding+"px;\'/>";
				}else{
					result = "<img src=\'"+skinType+"/images/hasMusic.png"+"\'/>"+name+"(ID:"+id+")";
				}
				 $("#matList").empty();
                 $("#matList").html("<center>"+result+"</center>");
			}
		}

		function reloadTree() {
			showMenu();
			zTree1 = $("#materialTree").zTree(setting, zNodes);
		}	
		
	
		
		$(document).ready(function() {
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
					//现需支持斜杠跟反斜杠
			
		})
		
	

		//调用模板
		function confirmMatrial(){
			var type = $("#type").val();
			var address = $("#address").val();
			var size = $("#size").val();
			if ("MIDI" != type && "MID" != type && "AMR" != type){
			    var width = $("#width").val();
			    var height = $("#height").val();
				doApp(address,width,height,size);
			}else{
				doApp2(address,size);
			}
			$("#mmsMat").dialog("close");
		}
		
		
		