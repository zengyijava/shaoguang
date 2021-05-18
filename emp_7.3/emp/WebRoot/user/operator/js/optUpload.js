$(document).ready(function(){
	 $("#fileUploadDiv").dialog({
			autoOpen: false,
		    width:450,
		    height:150,
		    title:getJsLocaleMessage("user","user_xtgl_czygl_text_160"),
		    modal:true,
		    resizable:false
		 });
	 
	$('#upload').click(function(){
		$("#fileUploadDiv").dialog("open");
	});
	
	 $("#erroDataDiv").dialog({
			autoOpen: false,
		    width:400,
		    height:300,
		    title:getJsLocaleMessage("user","user_xtgl_czygl_text_159"),
		    modal:true,
		    resizable:false
		 });
	 
	$('#erroData').click(function(){
		$("#erroDataDiv").dialog("open");
	});
	
	
});
function back(){
	$("#fileUploadDiv").dialog("close");
}
//检查上传的文件是否符合条件
function checkUpload(){
	var uploadFile = $("#uploadFile").attr("value") ;
	$("#kwsok").attr("disabled",true);
	if(uploadFile.length == 0){//非空检查
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_164")) ;
		$("#kwsok").attr("disabled",false);
		return false ;
	}else if(uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xls" && uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xlsx"){//检查文件格式
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_163")) ;
		$("#kwsok").attr("disabled",false);
		return false ;
	}else{
		$('#import').attr("disabled","disabled");
		$("#uploadForm").submit();
	
	}
}

function goback(path){
	window.location.href=path+"/opt_department.htm";
	
}

function importExcel(){
	//导出全部数据到excel
var name=$("#upDepName").val();
var id=$("#depId").val();
if(confirm(getJsLocaleMessage("user","user_xtgl_czygl_text_69"))){
		      var lgcorpcode =GlobalVars.lgcorpcode;
			  var lguserid =GlobalVars.lguserid;
		      var lgguid =GlobalVars.lgguid;
		     /* if(sysvoFlag == "haverecord"){*/
					$.ajax({
						type: "POST",
						url: "opt_department.htm?method=exportDep",
						data: {
							"name":name,
							"id":id,
						lgcorpcode:lgcorpcode
						},
						beforeSend: function(){
							page_loading();
						},
		                complete:function () {
							page_complete();
		                },
						success: function(result){
							if(result=='true'){	
								download_href("opt_department.htm?method=downloadFile&down_session=exportToExcel");
							}else{
		                        alert(getJsLocaleMessage("user","user_xtgl_czygl_text_70"));
		                    }
			   			}
					});
		   		
		  }
	 }

function upload(path){
	window.location.href=path+"/opt_department.htm?method=goUplode";
}


