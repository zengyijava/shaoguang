		var empLangName = $("#empLangName").val();
		var zTree2;
		var zNodes2 =[];
		function showDepTree(){
			var selected=document.getElementsByName("delBook");
			var n=0;		//统计勾选中的个数
			var id="";
			for(i=0;i<selected.length;i=i+1){
				if(selected[i].checked==true){
					id=id+selected[i].value;
					id=id+","
					n=n+1;
				}
			}
			id=id.substring(0,id.lastIndexOf(','));
			if(n<1){alert(getJsLocaleMessage("ydwx","ydwx_wxsc_44"));return;}
			$("#changeDep").dialog({
				autoOpen: false,
				height:480,
				title:getJsLocaleMessage("ydwx","ydwx_wxsc_58"),
				width: 360,
				resizable:false,
				modal: true,
				close:function(){
					 doCancel();
				}
			});
			$("#changeDep").dialog("open");
		}

		function depTreeCancel()
		{
			doCancel();
			$("#changeDep").dialog("close");
		}
		
		function zTreeOnClick2(event, treeId, treeNode) {
			$("#changeDepId").val(treeNode.id);
		}
		//素材提交（特殊的用法：该方法不能去掉）
		function doSubmit(){
		    var selected=document.getElementsByName("delBook");
		    var changeDepId = $("#changeDepId").val();
		    if(changeDepId == null || changeDepId == ""){
		    	alert(getJsLocaleMessage("ydwx","ydwx_wxsc_45"));
		    	return ;
		    }
			var id="";
			for(i=0;i<selected.length;i=i+1){
				if(selected[i].checked==true){
					id=id+selected[i].value;
					id=id+","
				}
			}
			id=id.substring(0,id.lastIndexOf(','));
			$.ajax({
	        url: path+"/wx_attached.htm",
	        data:{
	            method:'changeDep',
	    		bookIds:id,
	    		depId:changeDepId
	        },
	        type: 'post',
	        error: function(){
	           alert(getJsLocaleMessage("ydwx","ydwx_wxsc_46"));		
	        },
	        success: function(result){
				if (result == "true") {
					alert(getJsLocaleMessage("ydwx","ydwx_wxsc_47"));
					submitForm();
				}else {
					alert(getJsLocaleMessage("ydwx","ydwx_wxsc_46"));	
				}
	        }
	    });
	    doCancel();
	    $("#changeDep").dialog("close");
		}
		
		function doCancel(){
			$("#changeDepId").val("");
		}
		//员工转化操作员
		function tochangeSysuser()
		{
			var pathUrl = $("#pathUrl").val();
			var count = $("input[name='delBook']:checked").length;   
			if(count != "1"){alert(getJsLocaleMessage("ydwx","ydwx_wxsc_48"));return;}
			else
			{
				var isopr = $("input[name='delBook']:checked").attr("id");
				if (isopr == "1")
				{
					alert(getJsLocaleMessage("ydwx","ydwx_wxsc_49"));
					return;
				}
				var id = $("input[name='delBook']:checked").val();
			    if (id != null || id != "")
			    {
			    	location.href=pathUrl+"/wx_attached.htm?method=toAdd&eid="+id+"&lgguid="+$("#lgguid").val();
			    }
			}
		}


		//新增素材
	function showAddSucai(){
        var noSelectedFile = "zh_HK"===empLangName?"No file is selected!":"zh_TW"===empLangName?"未選擇任何文件":"未选择任何文件";
		var node = zTree.getSelectedNode(); 
		if(!node){
		 	alert(getJsLocaleMessage("ydwx","ydwx_wxsc_50"));
		}else if(node.id==-1){
			alert(getJsLocaleMessage("ydwx","ydwx_wxsc_51"));
		}else{
			$.post(path+"/wx_attached.htm?method=sortPathById",
					{nodeid:node.id},
					function(r){
							//隐藏标签且清除显示文件名字的input的内容
                        	$(".showFileName").hide();
                        	$(".showFileName").val("");
		            		 if(r!=null&&r!="21")
		                     {
		                     //素材 界面
                                 //给文件上传标签添加title属性覆盖掉原来的默认值
                                 $("#file_1").attr("title",noSelectedFile);
		            			 $("#createSucai").dialog({
		         					autoOpen: false,
		         					height:420,
		         					width: "zh_HK"===empLangName?580:440,
		         					modal: true,
		         					open:function(){
		         			
		         					},
		         					close:function(){
		         						doCancel();
		         					}
		         				});
			         		    $("#createSucai").dialog("open");
			         			$("#AttachmentName").focus();
			         		 if(r=="20"){//显示变成文件素材	
		                      $("#AttachType").val(getJsLocaleMessage("ydwx","ydwx_wxsc_52"));
		                      $("#AttachType1").val(getJsLocaleMessage("ydwx","ydwx_wxsc_52"));
		                     }else if(r=="22"){
		                     $("#AttachType").val(getJsLocaleMessage("ydwx","ydwx_wxsc_53"));
		                      $("#AttachType1").val(getJsLocaleMessage("ydwx","ydwx_wxsc_53"));
		                     }else if(r=="23"){
		                     $("#AttachType").val(getJsLocaleMessage("ydwx","ydwx_wxsc_54"));
		                     $("#AttachType1").val(getJsLocaleMessage("ydwx","ydwx_wxsc_54"));
		                     }
			         		$("#nodeid").val(node.id);
			         		$("#AttachTypeNum").val(r);
			         			
		                    }
		                    
		                     else
		                     {
		                     //视频 界面
                                 //给文件上传标签添加title属性覆盖掉原来的默认值
                                 $("#filemp4").attr("title",noSelectedFile);
		                          $("#createVed").dialog({
		         					autoOpen: false,
		         					height:430,
		         					width: "zh_HK"===empLangName?550:440,
		         					modal: true,
		         					open:function(){
		         			
		         					},
		         					close:function(){
		         						doCancel();
		         					}
		         				});
		         		    $("#createVed").dialog("open");
		         			$("#vedName").focus(); 
		         			$("#VedioType").val(getJsLocaleMessage("ydwx","ydwx_wxsc_55"));
		         			$("#VedNodeid").val(node.id);
		         			$("#VedioType1").val(getJsLocaleMessage("ydwx","ydwx_wxsc_55"));
		                     }
		              
		               });
			
		}
	}