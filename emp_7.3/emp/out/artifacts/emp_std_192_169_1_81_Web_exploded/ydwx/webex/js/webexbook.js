		//新增素材
		//该脚本不能放到JS里面，导致该脚本不被执行
		function doOk()
		{
			 var zenze=/^[A-Za-z0-9]+$/;
             var pathUrl = $("#pathUrl").val();
             //名称
             var name = $("#depName").val();
             //部门的id
             var scode = $("#sdepcode").val();
             //自定义编码
             var depcodethird=$("#depcodethird").val();
             
              var lgcorpcode=$("#lgcorpcode").val();
              var lguserid=$("#lguserid").val();
              //may 增加了文字长度的校验
             if(name.length>10){
             	alert(getJsLocaleMessage("ydwx","ydwx_wxsc_16"));
             	return;
             }
             
              name = name.replace(/(^\s*)|(\s*$)/g,"");
             //过滤重复部门名
             var node = zTree.getSelectedNode();  
             var pnode = node.parentNode;
             var deleteZtree2Node = zTree.getNodesByParam("pId",node.id ,node);
			 if(deleteZtree2Node!=null){
				for(var i=0;i<deleteZtree2Node.length;i++){
					if(deleteZtree2Node[i].name == name){
							alert(getJsLocaleMessage("ydwx","ydwx_wxsc_17"));
							$("#depName").select();
							return;
					}
				}
			 }
			 if(pnode==null){
			 	 alert(getJsLocaleMessage("ydwx","ydwx_wxsc_18"));
			 	 return ;
			 }
             if (name == "")
             {
                 alert(getJsLocaleMessage("ydwx","ydwx_wxsc_19"));
                 $("#depName").focus();
                 return;
             }
             
             if(name.indexOf("'")!=-1  || outSpecialChar(name)  ){
             	alert(getJsLocaleMessage("ydwx","ydwx_wxsc_20"));
             	return;
             }
             
			if(depcodethird=="")
			{
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_21"));
				$("#depcodethird").focus();
				return;
			}
			if(!zenze.test(depcodethird))
			{
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_22"));
				$("#depcodethird").select();
				return;
			}
			else
             {
				$("#depcancel").attr("disabled",true);
				$("#depsub").attr("disabled",true);
                $.post(pathUrl+"/wx_attached.htm?method=addWeblist",{name:name,currentId:node.id,lguserid:lguserid,lgcorpcode:lgcorpcode},function(r){
                      if(r!=null&&r=="true")
                      {
                    	 alert(getJsLocaleMessage("ydwx","ydwx_wxsc_23"));
                    	 zTree.setting.asyncUrl =  pathUrl+"/wx_attached.htm?method=getEmpSecondDepJson&depId="+node.id;
                    	 zTree.reAsyncChildNodes(zTree.getSelectedNode(), "refresh");
                      	$("#depcancel").attr("disabled",false);
           			 	$("#depsub").attr("disabled",false);
           			 	doNo();
                      }
                      else if(r.indexOf(getJsLocaleMessage("ydwx","ydwx_wxsc_24"))!=-1){
                        $("#depcodethird").select();
                      	$("#depcancel").attr("disabled",false);
           			 	$("#depsub").attr("disabled",false);
                      	alert(getJsLocaleMessage("ydwx","ydwx_wxsc_24"));
                      }
                      else if(r.indexOf("DepAbove,")!=-1)
                      {
                          var rs=r.split(",");
                          $("#depcancel").attr("disabled",false);
             			  $("#depsub").attr("disabled",false);
                          alert(getJsLocaleMessage("ydwx","ydwx_wxsc_25")+rs[1]+getJsLocaleMessage("ydwx","ydwx_wxsc_26"));
                      }
                      else if(r.indexOf("levAbove,")!=-1)
                      {
                          var rs=r.split(",");
                          alert(getJsLocaleMessage("ydwx","ydwx_wxsc_27")+rs[1]+getJsLocaleMessage("ydwx","ydwx_wxsc_28"));
                          $("#depcancel").attr("disabled",false);
             			  $("#depsub").attr("disabled",false);
                      }
                      else if(r.indexOf("childAbove,")!=-1)
                      {
                          var rs=r.split(",");
                          alert(getJsLocaleMessage("ydwx","ydwx_wxsc_29")+rs[1]+getJsLocaleMessage("ydwx","ydwx_wxsc_26"));
                          $("#depcancel").attr("disabled",false);
             			  $("#depsub").attr("disabled",false);
                      }
                      else{
	                  	  alert(getJsLocaleMessage("ydwx","ydwx_wxsc_30"));
	                      $("#depcancel").attr("disabled",false);
	           			  $("#depsub").attr("disabled",false);
	           			  //location.href=location;
	           			var name = $("#depName").val("");
	                   // var scode = $("#sdepcode").val("");
	                    var scode = $("#depcodethird").val("");
	                    $("#addDep").dialog("close");
                      }
                })
              }
		}
		function doNo()
		{
			 var name = $("#depName").val("");
             //var scode = $("#sdepcode").val("");
             var scode = $("#depcodethird").val("");
             $("#addDep").dialog("close");
	    }
		//时间控件的控制
		function rtime(){
		    //var max = "2099-12-31 23:59:59";
		    var max = "2099-12-31";
		    var v = $("#submitSartTime").attr("value");
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
				    case "02":day = 28;break;
				    case "04":day = 30;break;
				    case "06":day = 30;break;
				    case "09":day = 30;break;
				    case "11":day = 30;break;
					default:day = 31;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)+1));
				    mon = "01";
				}
				//max = year+"-"+mon+"-"+day+" 23:59:59"
				max = year+"-"+mon+"-"+day+""
			}
			//WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
		
		};
		//开始时间控件
		function stime(){
		   // var max = "2099-12-31 23:59:59";
		   var max = "2099-12-31";
		    var v = $("#submitEndTime").attr("value");
		  //  var min = "1900-01-01 00:00:00";
		   var min = "1900-01-01";
			if(v.length != 0)
			{
			    max = v;
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
			//	min = year+"-"+mon+"-01 00:00:00"
				min = year+"-"+mon+"-01"
			}
			//WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
		
		};

		//点击取消时，页面上值的处理
		function endSucai(){
			$("#AttachmentName").val(""); 
			$("#AttachType").val("");
			$("#AttachType1").val("");
			$("#nodeid").val("");
			$("#AttachmentDescribe").val("");
			$("#AttachTypeNum").val("");
			$("#file_1").val("");
			$('#createSucai').dialog('close');
		}
		//点击取消时，页面上值的处理
		function endVed(){
			$("#vedName").val(""); 
			$("#VedioType").val("");
			$("#VedioType1").val("");
			$("#VedNodeid").val("");
			$("#veddec").val("");
			$("#filemp4").val("");
			//$("#fileswf").val("");
			//$("#filem3u8").val("");
			$('#createVed').dialog('close');
		}
		
		//onchange事件处理
		function givetype(){
			var file=$("#file_1").val();
            var arr=file.split('\\');
            var fileName=arr[arr.length-1];
			var hz=file.substring(file.lastIndexOf(".")+1);
			$("#AttachType").val(hz);
            $(".showFileName").show();
			$(".showFileName").val(fileName);
            $("#file_1").attr("title",fileName);
		}
		//视频上传显示文件名字
        function showVideoName(){
            var file=$("#filemp4").val();
            var arr=file.split('\\');
            var fileName=arr[arr.length-1];
            $(".showFileName").show();
            $(".showFileName").val(fileName);
            $("#filemp4").attr("title",fileName);
		}
		//
		function toAddEmployee(path){
			var lgcorpcode =  $("#lgcorpcode").val();
			var lguserid =  $("#lguserid").val();
			window.location.href=path+"/wx_attached.htm?method=toAddEmployeePage&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"";
		}
		//删除分类
		function doDel(id,name)
	    {
	    	var oldName =  $("#depOldName").val();
			if(oldName==""||oldName==null)
			{
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_31"));
				return;
			}
			var node = zTree.getSelectedNode().parentNode;  
              if(node==null || node.id==-1){
              	alert(getJsLocaleMessage("ydwx","ydwx_wxsc_32"));
              	return ;
              }
	    	var pathUrl = $("#pathUrl").val();
	    	var lgcorpcode = $("#lgcorpcode").val();
	    	var lguserid = $("#lguserid").val();

			//------换界面后改的地方-------
	    	id= $("#depId").val();
			name=$("#depName2").val();
	    	//---------------------------
	    	if(id==""||id==null)
	    	{
		    
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_31"));
				return;
	    	}
   	
             if(confirm(getJsLocaleMessage("ydwx","ydwx_wxsc_33")))
             {	

            	 $.post(pathUrl+"/wx_attached.htm?method=delDep",{id:id},function(r){
            		 if(r!=null&&r=="1")
                     {
                     	alert(getJsLocaleMessage("ydwx","ydwx_common_shchchg"));
                     	var node = zTree.getSelectedNode().parentNode;  
                     	zTree.setting.asyncUrl =  pathUrl+"/wx_attached.htm?method=getEmpSecondDepJson&depId="+node.id;
                    	zTree.reAsyncChildNodes(node, "refresh");
                    	submitForm();
                        //location.href=location;
                        //---------删除成功清空隐藏域的值--------
                    	$("#depId").val("");
            			$("#depName2").val("");
            			$("#updateCode").attr("value","");
        	            $("#depOldName").attr("value","");
        	            $("#depNewName").attr("value","");
        	            $("#depcodethirdUpdate").html("");
                        //-------------------------------------
                     }
                     else if(r!=null && r=="0")
                     {
                       alert(getJsLocaleMessage("ydwx","ydwx_wxsc_4"));
                     }
                     else
                     {
                         alert(getJsLocaleMessage("ydwx","ydwx_wxsc_5"))
                     }
              
               })
             }
		}
		//取消按钮处理
		function noUpdate(){
              $("#depNewName").val($("#depOldName").val());
              $("#updateDep").dialog("close");
		}
		//修改分类的名称
		function updateDepName()
			{
	             var pathUrl = $("#pathUrl").val();
	             var lgcorpcode = $("#lgcorpcode").val();
	             var code =  $("#updateCode").val();
	             var oldName =  $("#depOldName").val();
	             var newName =  $("#depNewName").val();
	            // newName = newName.replace(/(^\s*)|(\s*$)/g,"");
	             newName = newName.replace(/\s+/g,""); 
	                if (newName == "")
	             {
	                 alert(getJsLocaleMessage("ydwx","ydwx_wxsc_34"));
	                 $("#depNewName").focus();
	                 return;
	             }
	             if(newName.length>10||oldName.length>15){
	             	 alert(getJsLocaleMessage("ydwx","ydwx_wxsc_35"));
	                 $("#depNewName").focus();
	                 return;
	             }
	             if(oldName == newName){
	             	alert(getJsLocaleMessage("ydwx","ydwx_wxsc_36"));
	             	$("#depNewName").select();
	             	return;
	             }
	             if(newName.indexOf("'")!=-1  || outSpecialChar(newName)  ){
	              	alert(getJsLocaleMessage("ydwx","ydwx_wxsc_37"));
	              	return;
	              }
	             var type = "1";
	              var node = zTree.getSelectedNode().parentNode;  
	              if(node==null || node.id==-1){
	              	alert(getJsLocaleMessage("ydwx","ydwx_wxsc_38"));
	              	return ;
	              }
	              if(node != null){
	                  var deleteZtree2Node = zTree.getNodesByParam("pId",node.id ,node);
					  if(deleteZtree2Node!=null){
						for(var i=0;i<deleteZtree2Node.length;i++){
							if(deleteZtree2Node[i].name == newName){
								alert(getJsLocaleMessage("ydwx","ydwx_wxsc_39"));
								$("#depNewName").select();
								return;
							}
						}
					 }
	              }else{
	              	type = "2";
	              }
				$("#updateCancle").attr("disabled",true);
				$("#updateSubmit").attr("disabled",true);
				var currentNode = zTree.getSelectedNode();
			     $.post(pathUrl+"/wx_attached.htm?method=updateDepName",
			    	 {
			    	 	depName:newName,
			    	 	id:currentNode.id
			    	 	},
				     function(resultMsg){
		                  if(resultMsg != null && resultMsg == "1")
		                  {
		                  		alert(getJsLocaleMessage("ydwx","ydwx_common_czchg"));
		                  		//修改后将旧名称标识更新
		                  		$("#depOldName").val(newName);
		                  		var curTreenodes=zTree.getSelectedNode();
		                  		curTreenodes.name = newName;
		                  		zTree.updateNode(curTreenodes, true);
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        //window.location.href=location;
		                  }else if(resultMsg != null && resultMsg == "2"){
		                  		alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        return;
		                  }else if(resultMsg != null && resultMsg == "3"){
		                	  	$("#depNewName").select();
		                  		alert(getJsLocaleMessage("ydwx","ydwx_wxsc_40"));
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        return;
		                  }else if(resultMsg != null && resultMsg == "0"){
		                        noUpdate();
		                  		alert(getJsLocaleMessage("ydwx","ydwx_wxsc_41"));
		                	    $("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        return;
		                  }else if(resultMsg != null && resultMsg == "4"){
		                        noUpdate();
		                  		alert(getJsLocaleMessage("ydwx","ydwx_wxsc_42"));
		                  		$("#updateCancle").attr("disabled",false);
		            			$("#updateSubmit").attr("disabled",false);
		                        noUpdate();
		                        return;
		                  }else{
		                  		alert(getJsLocaleMessage("ydwx","ydwx_wxsc_56"));
		                         window.location.href=location;
		                  }
		              
		             }
	             )
	             
			}
		
		//准备加载
		function loadReady() {
			var h = demoIframe.contents().find("body").height();
			if (h < 600) h = 600;
			demoIframe.height(h);
		}
		//修改的素材分类
		function updateDepFun()
		{
			var oldName =  $("#depOldName").val();
			if(oldName==""||oldName==null)
			{
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_43"));
				return;
			}
			$("#updateDep").dialog("open");
		}
		//弹出增加素材页面
		function addDepFun()
		{
			var oldName =  $("#depOldName").val();
			if(oldName==""||oldName==null)
			{
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_57"));
				return;
			}
			$("#addDep").dialog("open");
		}