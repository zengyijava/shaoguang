	var zTree1;
	var zTree2;
	var setting2;
	var zNodes = [];
	var formName = "addForm";			//点的卡片是新建员工还是上传员工
	var pathUrl = $("#pathUrl").val();
	setting2 = {
			async : true,
			//asyncUrl :"path%>/a_addrbookDepTree.htm?method=getEntDepTreeJson", //获取节点数据的URL地址
			asyncUrl:"",  //获取节点数据的URL地址
			isSimpleData: true,
			rootPID : -1,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			callback: {
				//beforeExpand: function(){},
				//beforeCollapse: function(){},
				click: zTreeOnClick,
				asyncSuccess:function(event, treeId, treeNode, msg){
					if(!treeNode){	//判断是 顶级机构就展开,其余的收缩+				
						if(formName == "addForm"){
							 var rootNode2 = zTree2.getNodeByParam("level", 0);
				             zTree2.expandNode(rootNode2, true, false);
						}else if(formName == "uploadForm"){
							 var rootNode1 = zTree1.getNodeByParam("level", 0);
				             zTree1.expandNode(rootNode1, true, false);
						}				  
			      }
						
				},
				beforeAsync:function(treeId,treeNode){				
					if(formName == "addForm"){
						zTree2.setting.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
					}else if(formName == "uploadForm"){
						zTree1.setting.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
					}
					//return false;
				}	
			}
	};
	var zNodes2 =[];
	
	function showMenu(fname) {
		$("form[name='"+fname+"']").find("#dropMenu").focus();
		var sortSel = $("form[name='"+fname+"']").find("#depNam");
		var sortOffset = $("form[name='"+fname+"']").find("#depNam").offset();
		$("form[name='"+fname+"']").find("#dropMenu").toggle();
		setFormName(fname);
		if(formName == "addForm"){
			setting2.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson";
			zTree2 = $("form[name='addForm']").find("#dropdownMenu").zTree(setting2, zNodes);
			$("form[name='uploadForm']").find("#dropMenu").hide();
		}else if(formName == "uploadForm"){
			setting2.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson";
			zTree1 = $("form[name='uploadForm']").find("#dropdownMenu").zTree(setting2, zNodes);
			$("form[name='addForm']").find("#dropMenu").hide();
		}
		var disstate = $("#dropMenu").css("display");
		if(disstate == 'block'){			
		   $("#job").css("visibility","hidden");
		   var menuIds = ["dropMenu_user","dropMenu_employ","dropMenu_client","dropMenu_udep"];
		  hideMenuById(menuIds);
		}else if(disstate == 'none'){
		    $("#job").css("visibility","visible");
		    $("#userPerType").css("visibility","visible");
			$("#employPerType").css("visibility","visible");
			$("#clientPerType").css("visibility","visible");
		}
		
		
	}

	function hideMenu() {
	    $("#job").css("visibility","visible");
		$("form[name='"+formName+"']").find("#dropMenu").hide();
	}
	
	function zTreeOnClick(event, treeId, treeNode) {
	    //$("#sex").css("visibility","visible");
		if (treeNode.isParent) {
			if(formName == "addForm"){
				zTree2.setting.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
			}else if(formName == "uploadForm"){
				zTree1.setting.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
			}
		}
		if (treeNode) {
			var sortObj = $("form[name='"+formName+"']").find("#depNam");
			sortObj.attr("value", treeNode.name);
			$("form[name='"+formName+"']").find("#depId").attr("value",treeNode.id);
			hideMenu();
		}
	}
	function setFormName(name){
		formName = name;
		//reloadTree();
	}
	function reloadTree() {
		hideMenu();
		setting2.asyncUrl=pathUrl+"/epl_employeeBook.htm?method=getEmpSecondDepJson";
		zTree2 = $("form[name='addForm']").find("#dropdownMenu").zTree(setting2, zNodes2);
	}	

	//关闭树的下拉框方法（点击别处时关闭）这个是要特殊处理的方法
	function closeTreeFunOnSpc(ids)
	{
		$("#employPerType").click(function(e){
			e.stopPropagation();
		});

		$("#clientPerType").click(function(e){
			e.stopPropagation();
		});
		
		$("#userPerType").click(function(e){
			e.stopPropagation();
		});
		
		$("#selectDepBtn_employ").click(function(e){
			e.stopPropagation();
		});
		
		$("#selectDepBtn_user").click(function(e){
			e.stopPropagation();
		});

		$("#treeInput").click(function(e){
			e.stopPropagation();
		});
		
		$("#selectDepBtn_client").click(function(e){
			e.stopPropagation();
		});
		for(var i=0;i<ids.length;i++)
		{
			$("#"+ids[i]).click(function(e){
				e.stopPropagation();
			});
		}

		$('html,body').click(function(e){
			var $obj=$(e.target);
	        if($obj.attr("class").indexOf("treeInput")==-1){
	        	for(var i=0;i<ids.length;i++)
	        	{
	        		$("#"+ids[i]).css("display","none");
	        		$("select").css("visibility","");
	        	}
	      }
	   });
	}
	$(document).ready(function(){
		
		show();
		setting2.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		
		reloadTree();
		closeTreeFunOnSpc(["dropMenu_employ","dropMenu_client","dropMenu_udep","dropMenu","dropMenu_user"]);
		
		
		$("#s_ud_s_list li").each(function(index){
			$(this).click(function(){
				$("div.block").removeClass("block");
				$("#tab_menu > div").addClass("hidden");
				$("li.s_ud_tabin").removeClass("s_ud_tabin");
				$("#tab_menu > div").eq(index).addClass("block");
				$("#tab_menu > div").eq(index).removeClass("hidden");
				$(this).addClass("s_ud_tabin");
			});
		});
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
			$('#cName').keypress(function(e) {
				var iKeyCode = window.event ? e.keyCode
						: e.which;
				if (iKeyCode == 60 || iKeyCode == 62) {
					if (isIE) {
						event.returnValue = false;
					} else {
						e.preventDefault();
					}
				}
			});
			$('#cName').blur(function(e) {
				$(this).val($(this).val().replaceAll("<","").replaceAll(">",""));
			});
			
			$(".closeTree").click(function(){
				$(this).parent().parent().hide();
			})
			//closeTreeFunSelBook();
	});
	
	
	function doUpload(){
		var uploadFile = $("#uploadFile").attr("value") ;
		var depId = $("form[name='"+formName+"']").find("#depId").attr("value");
		if (depId=="")
		{
			alert(getJsLocaleMessage('employee','employee_alert_2'));
			$("form[name='"+formName+"']").find("#depId").focus();
		}else
		if(uploadFile.length == 0){//非空检查
			//alert("没有选择导入文件，请导入TXT文本或Excel文件！") ;
			alert(getJsLocaleMessage('employee','employee_alert_1')) ;
			return false ;
		}else if(//uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="txt" &&
			 uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xls"){//检查文件格式是否是.txt格式
			//alert("不支持此种文件类型，请导入后缀名为.txt或.xls的文件！") ;
			alert(getJsLocaleMessage('employee','employee_alert_3')) ;
			return false ;
		}else{		
				 $("#button1").parent().prepend("<font style='margin-right:25px;'>"+getJsLocaleMessage('employee','employee_alert_4')+"</font>");
                 $("#button1").attr("disabled","disabled");
                 $("#uploadForm").attr("action",$("#uploadForm").attr("action")+"&lgguid="+$("#lgguid").val()+"&lgcorpcode="+$("#lgcorpcode").val()+"&lguserid="+$("#lguserid").val()+"&checkFlag="+$("input[name='checkFlag']:checked").val());
			  $("#uploadForm").submit();			  
		}
	}
	
	function UploadEmployee(result)
	{		
   		   if(result == "phoneExist")
		   {
			  var message=getJsLocaleMessage('employee','employee_alert_5');
			   
			   if (confirm(message))
			   {
	    		   //表单重新提交
	    		   $("form[name='uploadForm']").attr("target","_self");
	    		   document.forms['uploadForm'].action = pathUrl+"/epl_employeeBook.htm?method=uploadBook";
	    		   $("#uploadForm").submit();
			   }
			   else
			   {
			     $("#button1").attr("disabled","");
			   }
		   }
		   else
		   {
		      //表单重新提交
		      $("form[name='uploadForm']").attr("target","_self");
		      document.forms['uploadForm'].action = pathUrl+"/epl_employeeBook.htm?method=uploadBook"+"&lgcorpcode="+$("#lgcorpcode").val()+"&lguserid="+$("#lguserid").val()+"&checkFlag="+$("input[name='checkFlag']:checked").val();
	    	  $("#uploadForm").submit();
		   }
		   
		   
	}		
	function back(){
		location.href=pathUrl+"/epl_employeeBook.htm?method=find&isDes=1&lgguid="+$("#lgguid").val()+
					"&lgusername="+$("#lgusername").val();
	}

	function resetAddForm(){
		location.href = pathUrl+"/epl_employeeBook.htm?method=toAddEmployeePage&lguserid="+$("#lguserid").val()+
					"&lgcorpcode="+$("#lgcorpcode").val();
	}
	
	$(document).ready(function(){
		$('#depNam,#userDepName,.depNam-inp').wrapSel({width:225});
	})