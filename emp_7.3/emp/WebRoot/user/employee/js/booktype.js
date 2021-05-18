   function delType()
	    {
			var selected=document.getElementsByName("delTypeBook");
			var corpCode =document.getElementById("lgcorpcode").value;
			var n=0;		//统计勾选中的个数
			var id="";
			for(var i=0;i<selected.length;i=i+1){
				if(selected[i].checked==true){
					id=id+selected[i].value;
					id=id+",";
					n=n+1;
				}
			}
			id=id.substring(0,id.lastIndexOf(','));
			if(n<1){alert(getJsLocaleMessage('employee','employee_alert_6'));return;}
			
			if(confirm(getJsLocaleMessage('employee','employee_alert_7'))){
				   $.ajax({
						url:"epl_position.htm",
						type:"post",
						data:{
							method:"delete",
							bookIds:id,
							corpCode:corpCode
						},
						success:function(data){
							if(data == "suceess"){
								window.location.href="epl_position.htm";
							}else{
								alert(getJsLocaleMessage('employee','employee_alert_69'));
								return;
							}
						}
					});	
			}
		}
		
			function addType(){	
				//document.getElementById("addPres").style.display = "block";
				$("#addPres").css("display","block");
				$('#addPres').dialog({
					autoOpen: false,
					height: 150,
					width: 300,
					modal:true
				});
				resizeDialog($("#addPres"),"ydbgDialogJson","ygtxlgl_zhwgl_test1");
			 	$('#addPres').dialog('open');
		 }
		 
		 function addcloseDiv(){
		 	//document.getElementById("addPres").style.display = "none";
		 	$('#addPres').dialog('close');
		 }
		 
		 function addNodeClick(){
		 	var type = document.getElementById('addN').value;
		 	 var lguserid =  document.getElementById('lguserid').value;
	 		 type = type.replace(/(^\s*)|(\s*$)/g, "");
		 	 if(type==""){//去除两端空格
		 	 	alert(getJsLocaleMessage('employee','employee_alert_70'));
		 	 	$("#addN").focus();
		 	 	return;
		 	 }
		 	 if(type.length>32){
		 		 alert(getJsLocaleMessage('employee','employee_alert_71'));
		 		 $("#addN").focus();
		 		 return;
		 	 }
		 	 if(type.indexOf("'")!=-1 || type.indexOf("\"")!=-1){//
			 	 	alert(getJsLocaleMessage('employee','employee_alert_72'));
			 	 	$("#addN").focus();
			 	 	return;
			 	 }
		 	$.ajax({
					url:"epl_position.htm",
					type:"post",
					data:{
						method:"addType",
						zhiweiType : type,
						lguserid:lguserid
					},
					success:function(data){
						if(data=="ok"){
							alert(getJsLocaleMessage('employee','employee_alert_74'));
							 window.location.href="epl_position.htm";
						}else if(data == "mid"){
							alert(getJsLocaleMessage('employee','employee_alert_75'));
						    $("#addN").focus();
						}else if(data == "fail"){
							alert(getJsLocaleMessage('employee','employee_alert_76'));
						    $("#addN").focus();
						}else{
							alert(getJsLocaleMessage('employee','employee_alert_76'));
						    $("#addN").focus();
						}
					}
				});	
		 }
		 
		 function updType(id,value){
				//document.getElementById("updPres").style.display = "block";
				// document.getElementById('updN').value = value;
				//  document.getElementById('saveId').value = id;
			 		$("#updateposition").val("");
					$("#updPres").css("display","block");
					document.getElementById('updN').value = value;
					document.getElementById('saveId').value = id;
					$("#updateposition").val(value);
					$('#updPres').dialog({
						autoOpen: false,
						height: 140,
						width: 300,
						modal:true
					});
				resizeDialog($("#updPres"),"ydbgDialogJson","ygtxlgl_zhwgl_test2");
			 	$('#updPres').dialog('open');
				
		 }
		 
		  function updcloseDiv(){
		 	$('#updPres').dialog('close');
		 }
		 
		 	 function updNodeClick(){
			 	 var type = document.getElementById('updN').value;
			 	 var id =  document.getElementById('saveId').value;
			 	 var lguserid =  document.getElementById('lguserid').value;
			 	 type = type.replace(/(^\s*)|(\s*$)/g, "");
			 	 if(type==""){//去除两端空格
			 	 	alert(getJsLocaleMessage('employee','employee_alert_70'));
			 	 	$("#updN").focus();
			 	 	return;
			 	 }
			 	  if(type.indexOf("'")!=-1 || type.indexOf("\"")!=-1){//
			 	 	alert(getJsLocaleMessage('employee','employee_alert_72'));
			 	 	$("#updN").focus();
			 	 	return;
			 	 }
			 	 if(type.length>32){
		 		    alert(getJsLocaleMessage('employee','employee_alert_71'));
		 		   $("#updN").focus();
		 		    return;
			 	 }
			 	var potion = $("#updateposition").val();
			 	if(potion == type){
			 		$('#updPres').dialog('close');
			 		$("#updateposition").val("");
		 		   return;
			 	}
			 	$.ajax({
						url:"epl_position.htm",
						type:"post",
						data:{
							method:"update",
							zhiweiType : type,
							lguserid:lguserid,
							id:id
						},
						success:function(data){
							if(data=="ok"){
								alert(getJsLocaleMessage('employee','employee_alert_74'));
								 window.location.href="epl_position.htm";
							}
							else if(data == "mid"){
								alert(getJsLocaleMessage('employee','employee_alert_75'));
							    $("#updN").focus();
							}else if(data == "fail"){
								alert(getJsLocaleMessage('employee','employee_alert_76'));
							    $("#updN").focus();
							}else{
								alert(getJsLocaleMessage('employee','employee_alert_76'));
							    $("#updN").focus();
							}
							
						}
					});	
		 }
		 