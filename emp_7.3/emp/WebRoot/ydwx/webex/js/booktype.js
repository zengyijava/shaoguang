	/**
	* 该文件通过查询尚未被引用（由于之前人员所加，待真正确认后删除）
	* @base 网讯素材
	* @author Administrator
	* @date 2013-10-10
	* ***/
		//删除一个或多个记录
		function delType()
	    {
			var selected=document.getElementsByName("delTypeBook");
			var corpCode =document.getElementById("lgcorpcode").value;
			var n=0;		//统计勾选中的个数
			var id="";
			for(i=0;i<selected.length;i=i+1){
				if(selected[i].checked==true){
					id=id+selected[i].value;
					id=id+",";
					n=n+1;
				}
			}
			id=id.substring(0,id.lastIndexOf(','));
            /*请选择一个或多个记录进行操作！*/
            if(n<1){alert(getJsLocaleMessage("common","common_js_addrBook_1"));return;}
			/*确认删除该记录？*/
			if(confirm(getJsLocaleMessage("common","common_js_addrBook_2"))){
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
                                /*删除失败！*/
                                alert(getJsLocaleMessage("common","common_deleteFailed"));
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
				 /*职位名不能为空！*/
		 	 	alert(getJsLocaleMessage("employee","employee_alert_70"));
		 	 	$("#addN").focus();
		 	 	return;
		 	 }
		 	 if(type.length>32){
		 	 	 /*职位名太长！*/
		 		 alert(getJsLocaleMessage("employee","employee_alert_71"));
		 		 $("#addN").focus();
		 		 return;
		 	 }
		 	 if(type.indexOf("'")!=-1 || type.indexOf("\"")!=-1){//
                 /*职位名不能含有非法字符！*/
                 alert(getJsLocaleMessage("employee","employee_alert_72"));
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
							 window.location.href="epl_position.htm";
						}else if(data == "mid"){
                            /*职位名重复！*/
                            alert(getJsLocaleMessage("employee","employee_alert_75"));
						    $("#addN").focus();
						}else if(data == "fail"){
                            /*操作失败！*/
                            alert(getJsLocaleMessage("common","common_operateFailed"));
						    $("#addN").focus();
						}else{
                            /*操作失败！*/
                            alert(getJsLocaleMessage("common","common_operateFailed"));
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
                     /*职位名不能为空！*/
                     alert(getJsLocaleMessage("employee","employee_alert_70"));
			 	 	$("#updN").focus();
			 	 	return;
			 	 }
			 	  if(type.indexOf("'")!=-1 || type.indexOf("\"")!=-1){//
					/*职位名不能含有非法字符！*/
			 	 	alert(getJsLocaleMessage("employee","employee_alert_72"));
			 	 	$("#updN").focus();
			 	 	return;
			 	 }
			 	 if(type.length>32){
                     /*职位名太长！*/
                     alert(getJsLocaleMessage("employee","employee_alert_71"));
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
								 window.location.href="epl_position.htm";
							}
							else if(data == "mid"){
								/*职位名重复！*/
								alert(getJsLocaleMessage("employee","employee_alert_75"));
							    $("#updN").focus();
							}else if(data == "fail"){
                                /*操作失败！*/
                                alert(getJsLocaleMessage("common","common_operateFailed"));
							    $("#updN").focus();
							}else{
                                /*操作失败！*/
                                alert(getJsLocaleMessage("common","common_operateFailed"));
							    $("#updN").focus();
							}
							
						}
					});	
		 }
		 