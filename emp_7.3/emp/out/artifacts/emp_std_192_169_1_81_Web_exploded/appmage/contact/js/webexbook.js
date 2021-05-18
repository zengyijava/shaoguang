	

//该脚本不能放到JS里面，导致该脚本不被执行
		function doOk()
		{
             var pathUrl = $("#pathUrl").val();
             //名称
             var name = $("#depName").val();
              var lgcorpcode=$("#lgcorpcode").val();
              //may 增加了文字长度的校验
             if(name.length>15){
             	//alert("输入文字太长,文字长度不能超过15个！");
            	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_wordtoolong'));
             	return;
             }
              name = name.replace(/(^\s*)|(\s*$)/g,"");
             if (name == "")
             {
                // alert("请输入群组名称！");
            	 alert(getJsLocaleMessage('appmage','appmage_js_webexbook_entergroupname'));
                 $("#depName").focus();
                 return;
             }

             if (name == getJsLocaleMessage('appmage','appmage_js_webexbook_nogroupuser'))
             {
                 // alert("群组名称重复！");
                 alert(getJsLocaleMessage('appmage','appmage_js_webexbook_samegroupname'));
                 $("#depName").focus();
                 return;
             }
             
             if(!reg_exp(name)){
             	//alert("请输入合法群组名称！");
            	 alert(getJsLocaleMessage('appmage','appmage_js_webexbook_enterokname'));
             	return;
             }
 			$.ajax({
 		        url: pathUrl+'/app_cligroupmanager.htm',
 		        data:{
 		            method:'addWeblist',
 		           name:name,
 		          lguserid:lguserid,
 		          lgcorpcode:lgcorpcode
 		        },
 		        type: 'post',
 		        error: function(){
 		           //alert("新增群组失败！");
 		        	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_addgroupfalse'));
 		        },
 		        success: function(r){
                    if(r!=null&&r=="true")
                    {
                  	 //alert("新增群组成功！");
                  	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_addgroupsuc'));
                  	window.location.href = window.location.href;
                  	doNo();
                    }else if(r!=null&&r=="repeat"){
	                  //alert("群组名称重复！");
                    	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_samegroupname'));
	                 }else{
                  	  //alert("新增群组失败！");
	                	 alert(getJsLocaleMessage('appmage','appmage_js_webexbook_addgroupfalse'));
                    $("#addDep").dialog("close");
                    }
 		        }
 		    });
            
		}
		function doNo()
		{
			 var name = $("#depName").val("");
             $("#addDep").dialog("close");
	    }

		//取消按钮处理
		function noUpdate(){
			$("#depNewName").val("");
              $("#updateDep").dialog("close");
		}
		
		function reg_exp(s) { 
			var pattern = "`~!#$^&*={};',<>?~！#￥……&*\\——|‘；：:”“'\"%～。，、？./";
			for (var i = 0; i < pattern.length; i++) {
				if(s.indexOf(pattern.charAt(i)) > -1){
					return false;
				}
			} 
			return true; 
		} 

		//修改群组的名称
		function updateDepName()
			{
	             var lgcorpcode = $("#lgcorpcode").val();
	             var newName =  $("#depNewName").val();
	             var groupid=  $("#groupid").val();
	             newName = newName.replace(/\s+/g,""); 
	                if (newName == "")
	             {
	                 //alert("请输入群组名称！");
	                 alert(getJsLocaleMessage('appmage','appmage_js_webexbook_entergroupname'));
	                 $("#depNewName").focus();
	                 return;
	             }
	             if(newName.length>15){
	             	 //alert("群组名称不能太长,文字长度不能超过15个！");
	            	 alert(getJsLocaleMessage('appmage','appmage_js_webexbook_wordtoolong'));
	                 $("#depNewName").focus();
	                 return;
	             }
	             if(!reg_exp(newName)){
	              	//alert("请输入合法群组名称！");
	            	 alert(getJsLocaleMessage('appmage','appmage_js_webexbook_enterokname'));
	              	return;
	              }
	             if (newName == getJsLocaleMessage('appmage','appmage_js_webexbook_nogroupuser'))
	             {
	                 //alert("群组名称重复！");
	            	 alert(getJsLocaleMessage('appmage','appmage_js_webexbook_enterokname'));
	                 $("#depName").focus();
	                 return;
	             }
				$("#updateCancle").attr("disabled",true);
				$("#updateSubmit").attr("disabled",true);
				
	 			$.ajax({
	 		        url: pathUrl+'/app_cligroupmanager.htm',
	 		        data:{
	 		            method:'updateDepName',
			    	 	depName:newName,
			    	 	groupid:groupid
	 		        },
	 		        type: 'post',
	 		        error: function(){
	 		           //alert("修改群组失败！");
	 		        	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_updategroupfalse'));
	 		        },
	 		        success: function(resultMsg){

		                  if(resultMsg != null && resultMsg == "true")
		                  {
		                  		//alert("操作成功！");
		                	  	alert(getJsLocaleMessage('appmage','appmage_page_mtrecordselect_optsuc'));
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        window.location.href = window.location.href;
		                  }else if(resultMsg != null && resultMsg == "false"){
		                  		//alert("操作失败！");
		                	  	alert(getJsLocaleMessage('appmage','appmage_page_mtrecordselect_optfalse'));
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        return;
		                  }else if(resultMsg != null && resultMsg == "reapt"){
		                  		//alert("群组名称重复！");
		                	  	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_samegroupname'));
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        return;
		                  }
	 		        }
	 		    });
			}
		//删除群组
		function doDel(){
            var groupid=  $("#groupid").val();
            var pathUrl = $("#pathUrl").val();
			if(groupid=='-1'){
				//alert("该群组不允许删除！");
				alert(getJsLocaleMessage('appmage','appmage_js_webexbook_cannotdeltegroup'));
				return;
			}
			if(groupid==""||groupid==null)
			{
				//alert("请选择要删除的用户群组！");
				alert(getJsLocaleMessage('appmage','appmage_js_webexbook_selectdeletegroup'));
				return;
			}

			if(!confirm(getJsLocaleMessage('appmage','appmage_js_webexbook_confiretodeltegroup'))){
				return;
			}
			
 			$.ajax({
 		        url: pathUrl+'/app_cligroupmanager.htm',
 		        data:{
 		            method:'delDep',
		    	 	groupid:groupid
 		        },
 		        type: 'post',
 		        error: function(){
 		           //alert("删除群组失败！");
 		        	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_deletegroupfalse'));
 		        },
 		        success: function(r){
                    if(r!=null&&r=="true")
                    {
                  	// alert("删除群组成功！");
                    alert(getJsLocaleMessage('appmage','appmage_js_webexbook_deletegroupsuc'));
                  	window.location.href = window.location.href;
                  	doNo();
                    }else{
	                  	//  alert("删除群组失败！");
                    	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_deletegroupfalse'));
	                    $("#addDep").dialog("close");
                    }
 		        }
 		    });
			
		}

		//修改群组
		function updateDepFun()
		{
			var groupid =  $("#groupid").val();
			if(groupid==""||groupid==null)
			{
				//alert("请选择要修改的用户群组！");
				alert(getJsLocaleMessage('appmage','appmage_js_webexbook_selectgroupupdate'));
				return;
			}
			if(groupid=='-1'){
				//alert("该群组不允许修改！");
				alert(getJsLocaleMessage('appmage','appmage_js_webexbook_cannotupdategroup'));
				return;
			}
			$("#depNewName").val($("#depOldName").val());
			$("#updateDep").dialog("open");
		}
		//弹出增加群组
		function addDepFun()
		{
		
			$("#addDep").dialog("open");
		}
		
		//赋值处理
		function setValue(id,name)
		{
			//选中元素样式
			$(this).addClass("roleNameColor");
			//查询当前群组的人员
			$("#groupid").val(id);
			$("#depOldName").val(name);
			var lgcorpcode = $("#lgcorpcode").val();
			var pathUrl = $("#pathUrl").val();
			//去掉勾选
			$("#selectAll").attr("checked",false);
			$('#bookInfo').load(pathUrl+'/app_climanager.htm?method=findByGroup&lgcorpcode='+lgcorpcode+'&groupid='+id+'&time='+new Date().getTime());
		}
		
		// 判断是否选中
		   function checkCh(ob,ids){
			var c=ob.checked;
			var strs= new Array(); //定义一数组
			strs=ids.split(";");
			
			var hiddenValue=$("#checkBoxsel").val();
			var groupValue=$("#checkGroup").val();
			  if(c){
				  $("#checkBoxsel").val(strs[0]+","+hiddenValue);
				  $("#checkGroup").val(strs[1]+","+groupValue);
			  }else{
				  hiddenValue=hiddenValue.replace(strs[0]+",","");
				  $("#checkBoxsel").val(hiddenValue);
				  groupValue=groupValue.replace(strs[1]+",","");
				  $("#checkGroup").val(groupValue);
			  }
			}
		   
		// 选中所有
		   function checkAlls(e)    
			{  
			 var check="";
			 var group="";
			var a = document.getElementsByName("checklist");    
			var n = a.length;
			for (var i=0; i<n; i=i+1) {
				a[i].checked =e.checked; 
				if(e.checked==true){
				var strs= new Array(); //定义一数组
				strs=a[i].value.split(";");
				check=strs[0]+","+check;
				group=strs[1]+","+group;
				}
			} 
			
			$("#checkBoxsel").val(check);
			$("#checkGroup").val(group);
		}
		   
		   //变更群组
		   function changeGroup(){
	            var pathUrl = $("#pathUrl").val();
	            var lgcorpcode = $("#lgcorpcode").val();
	            var value=$("#checkBoxsel").val();
	            var groupValue=$("#checkGroup").val();
	            
			   if(groupValue==undefined){
				   //alert("没有客户可以添加到群组！");
				   alert(getJsLocaleMessage('appmage','appmage_js_webexbook_noclient'));
				   return;
				   
			   }
	            if(value==""){
	            	//alert("请选择客户！");
	            	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_selectclient'));
	            	return;
	            }
	            if(selected==""){
	            	//alert("请选择群组！");
	            	alert(getJsLocaleMessage('appmage','appmage_js_chooseSendInfo_pleaseselectgroup'));
	            	return;
	            }
	            $("#confim").attr("disabled",true);
	            
	 			$.ajax({
	 		        url: pathUrl+'/app_cligroupmanager.htm',
	 		        data:{
	 		            method:'changeGroup',
	 		           selected:selected,
	 		          value:value,
	 		         lgcorpcode:lgcorpcode
	 		        },
	 		        type: 'post',
	 		        error: function(){
	 		          // alert("变更群组失败！");
	 		        	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_updategroupfalse'));
	 		          $("#confim").attr("disabled",false);
	 		        },
	 		        success: function(r){
	                    if(r!=null&&r=="true")
	                    {
	                  	 //alert("变更群组成功！");
	                    	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_updategroupsuc'));
	                  	 window.location.href = window.location.href;
	                    }else{
		                  	 // alert("变更群组失败！");
	                    	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_updategroupfalse'));
		                  	$("#confim").attr("disabled",false);
	                    }
	 		        }
	 		    });
	 			
		   }
		   
		   //全显
		   function showAll(){
			   if($("#selectAll").attr("checked")){
	            var pathUrl = $("#pathUrl").val();
	            window.location.href = pathUrl+'/app_climanager.htm?method=find&showAll=showAll&lgcorpcode='+ lgcorpcode;
			   }
		   }
	   
		   //退出群组
		   function delLink(guid,gid){
	 		   var lgcorpcode =$("#lgcorpcode").val();
			   var lguserid =$("#lguserid").val();
				if(!confirm(getJsLocaleMessage('appmage','appmage_js_webexbook_isexitgroup'))){
					return;
				}
				
	 			$.ajax({
	 		        url: pathUrl+'/app_cligroupmanager.htm',
	 		        data:{
	 		            method:'delLink',
	 		           guid:guid,
	 		          gid:gid
	 		        },
	 		        type: 'post',
	 		        error: function(){
	 		           //alert("退出群组失败！");
	 		        	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_exitgroupfalse'));
	 		        },
	 		        success: function(r){
	                    if(r!=null&&r=="true")
	                    {
	                  	// alert("退出群组成功！");
	                    	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_exitgroupsuc'));
	                  	 window.location.href = pathUrl+'/app_climanager.htm?method=find&lgcorpcode='+lgcorpcode+'&lguserid='+lguserid+'&skip=1';
	                    }else{
		                  	  //alert("退出群组失败！");
	                    	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_exitgroupfalse'));
	                    }
	 		        }
	 		    });
			   
		   }
		   
		   function cleanSelect_employ() {
				var checkNodes = zTree.getCheckedNodes();
			    for(var i=0;i<checkNodes.length;i++){
			     checkNodes[i].checked=false;
			    }
			    zTree.refresh();
			    selected="";
			}
		   //清除选择，退出
		   function closediv(){
			   cleanSelect_employ();
				$("#dropMenu_user").hide();
		   }
		   
		   //退出所有群组
		   function allOut(){
				var hiddenValue=$("#checkBoxsel").val();
				var groupValue=$("#checkGroup").val();
		 		var lgcorpcode =$("#lgcorpcode").val();
				var lguserid =$("#lguserid").val();
			   if(hiddenValue==""){
				   //alert("请选择需要退出群组的客户！");
				   alert(getJsLocaleMessage('appmage','appmage_js_webexbook_selectcilent'));
				   return;
			   }
			   if(groupValue==undefined){
				  // alert("没有客户可以退出群组！");
				   alert(getJsLocaleMessage('appmage','appmage_js_webexbook_nocletnexit'));
				   return;
				   
			   }
			   if((groupValue+"").indexOf("null")>-1||groupValue==","||(groupValue+"").indexOf(",,")>-1){
				   //alert("选择中包括未分组用户！");
				   alert(getJsLocaleMessage('appmage','appmage_js_webexbook_selecterror'));
				   return;
			   }
			   if(!confirm(getJsLocaleMessage('appmage','appmage_js_webexbook_someexitgroup'))){
					return;
				}

				
	 			$.ajax({
	 		        url: pathUrl+'/app_cligroupmanager.htm',
	 		        data:{
	 		            method:'delAllLink',
	 		           hiddenValue:hiddenValue,
	 		          groupValue:groupValue
	 		        },
	 		        type: 'post',
	 		        error: function(){
	 		           //alert("退出群组失败！");
	 		        	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_exitgroupfalse'));
	 		        },
	 		        success: function(r){
	                    if(r!=null&&r=="true")
	                    {
	                  	// alert("退出群组成功！");
	                    	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_exitgroupsuc'));
	                  	 window.location.href = pathUrl+'/app_climanager.htm?method=find&lgcorpcode='+lgcorpcode+'&lguserid='+lguserid+'&skip=1';
	                    }else{
		                  //	  alert("退出群组失败！");
	                    	alert(getJsLocaleMessage('appmage','appmage_js_webexbook_exitgroupfalse'));
	                    }
	 		        }
	 		    });
			   
		   }
		   
		   //结束时间
			function rtime(){
				
			    var max = "2099-12-31 23:59:59";
			    var v = $("#createtime").attr("value");
			    var min = "1900-01-01 00:00:00";
				if(v.length != 0)
				{
				    var year = v.substring(0,4);
					var mon = v.substring(5,7);
					var day = 31;
					if (mon != "12")
					{
					    mon = String(parseInt(mon,10)+1);
					    if (mon.length == 1)
					    {
					        mon = "0"+mon;
					    }
					    switch(mon){
					    case "01":day = 31;break;
					    case "02":day = 28;break;
					    case "03":day = 31;break;
					    case "04":day = 30;break;
					    case "05":day = 31;break;
					    case "06":day = 30;break;
					    case "07":day = 31;break;
					    case "08":day = 31;break;
					    case "09":day = 30;break;
					    case "10":day = 31;break;
					    case "11":day = 30;break;
					    }
					}
					else
					{
					    year = String((parseInt(year,10)+1));
					    mon = "01";
					}
				}
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
			
			};
			
		//开始时间	
		function stime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#endtime").attr("value");
		    var min = "1900-01-01 00:00:00";
			if(v.length != 0)
			{
//			    max = v;
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				if (mon != "01")
				{
				    mon = String(parseInt(mon,10)-1);
				    if (mon.length == 1)
				    {
				        mon = "0"+mon;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)-1));
				    mon = "12";
				}
//				min = year+"-"+mon+"-01 00:00:00"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

		};

$(document).ready(function(){
	//导出全部数据到excel
	$("#exportCondition").click(function(){
		exportExcel();
	});
})
