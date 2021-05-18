$(document).ready(function(){
	 $("#fileUploadDiv").dialog({
			autoOpen: false,
		    width:450,
		    height:150,
		    title:getJsLocaleMessage("xtgl","xtgl_ywlx_filedaoru"),
		    modal:true,
		    resizable:false
		 });

	 
	$('#upload').click(function(){
		$("#fileUploadDiv").dialog("open");
	});

	

});
function back(){
	$("#fileUploadDiv").dialog("close");
}
function resultBack(){
	$("#resultUploadDiv").dialog("close");
}
function bindBusSP()
    {
        var SPUserID = $("#sp").val();
        if(SPUserID==null || SPUserID=="")
        {
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_61"));
			return;
        }
        var description = $("#busDescriptionBind").val();
		var busCode = $("#busCodeTemp").val();
    	if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_62")))
		{
			$.post("bit_busType.htm",{method:"bindSP",SPUserID:SPUserID,busCode:busCode,description:description},function(result){
				if(result=="true")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_44"));
					
					location.href=location.href;
				}else 
				{
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_whgl_44"));
					return;
				}
			});
		}
    }
	function del(busId)
	{
		if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_63")))
		{
			$.post("bit_busType.htm",{method:"delete",busId : busId},function(result){
				if(result=="true")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_5"));
					location.href=location.href;
				}else 
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
					return;
				}
			});
		}
	}
	function showMenu() {
		//hideMenu2();
		var sortSel = $("#depNam");
		var sortOffset = $("#depNam").offset();
		$("#dropMenu").toggle();
	}
    function doCancel()
    {
         $("#bindDivBusName").val("");
         $("#busDescriptionAdd").val("");
         $("#addDiv").dialog("close");
    }
	
	var BUSNAME = "";
	var BUSCODE = "";
	var BUSTYPE = "";//RiseLevel
	var RISELEVEL = "";
	var STATE = "";
	var BUSDESC = "";
	var BUSID = 0;
	function update(busId)
	{
		BUSCODE = $.trim($('#lbCode'+busId).text());
		BUSNAME = $.trim($('#lbName'+busId).text());
		BUSTYPE = $.trim($('#lbType'+busId).val());
		RISELEVEL = $.trim($('#lbRiseLevel'+busId).val());
		STATE = $.trim($('#lbState'+busId).val());
		BUSDESC = $.trim($('#lbDesc'+busId).text());
		$('#busCodeEdit').text(BUSCODE);
		$('#busNameEdit').val(BUSNAME);
		$("#busTypeEdit").val(BUSTYPE);
		$("#riseLevelEdit").val(RISELEVEL);
		$("input:radio[name='stateEdit'][value="+STATE+"]").attr("checked",true);
		$('#busDescriptionEdit').val(BUSDESC);
		BUSID = busId;
		$("#editDiv").dialog("open");
	}
	function bind(busId,busName)
	{
		
		var busCode =$.trim($('#lbCode'+busId).text());
		$('#bindDivBusName').text(busName);
		$('#busCodeTemp').val(busCode);
		//$('#menuCodeTemp').val();
		$("#bindDiv").dialog("open");
	}
	
	function editBusType()
	{
		var busName = $.trim($('#busNameEdit').val());
		var busDescription = $.trim($('#busDescriptionEdit').val());
		var busCode = $.trim($('#busCodeEdit').text());
		var busType = $('#busTypeEdit').val();
		var riseLevel = $('#riseLevelEdit').val();
		var state = $("input[name='stateEdit']:checked").val();
		$('#editcancel').attr("disabled",true);
		$('#editsubmit').attr("disabled",true);
		if($('#busDescriptionEdit').val().length>200){
		  alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_64"));
		  $('#busDescriptionEdit').select();
		  $('#editcancel').attr("disabled",false);
		  $('#editsubmit').attr("disabled",false);
		  return ;
		}
		if(busName == ""){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_65"));
			$('#busNameEdit').val("");
			$('#busNameEdit').select();
			$('#editcancel').attr("disabled",false);
			$('#editsubmit').attr("disabled",false);
		}
		//else if(busCode == ""){
		//	alert("业务编码不能为空！");
		//	$('#busCodeEdit').select();
		//	$('#editcancel').attr("disabled",false);
		//    $('#editsubmit').attr("disabled",false);
		//}
		else if(busType == ""){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_67"));
			$('#busTypeEdit').select();
			$('#editcancel').attr("disabled",false);
		    $('#editsubmit').attr("disabled",false);
		}
		else if(BUSNAME != busName || BUSCODE != busCode || BUSDESC != busDescription || BUSTYPE !=busType || RISELEVEL != riseLevel || STATE != state )
		{

			var lguserid = $("#lguserid").val();
			var lgcorpcode = $('#lgcorpcode').val();
			$.post("bit_busType.htm",
				{
					method:"update",
					lgcorpcode:lgcorpcode,
					lguserid:lguserid,
					busName : busName,
					busDescription : busDescription,
					busCode : busCode,
					busType : busType,
					riseLevel : riseLevel,
					state : state,
					busId : BUSID
				},function(result){
					if(result == "codeExists")
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_68"));
						$('#busCodeEdit').select();
						$('#editcancel').attr("disabled",false);
					    $('#editsubmit').attr("disabled",false);
					}else if(result == "nameExists")
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_69"));
						$('#busNameEdit').select();
						$('#editcancel').attr("disabled",false);
					    $('#editsubmit').attr("disabled",false);
					}else if(result == "true")
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_35"));
						$("#editDiv").dialog("close");
						
						var url = 'bit_busType.htm';
						var conditionUrl = "";
						if(url.indexOf("?")>-1)
						{
							conditionUrl="&";
						}else
						{
							conditionUrl="?";
						}
						$("#hiddenValueDiv").find(" input").each(function(){
							conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
						});
						var pageIndex=$('#txtPage').val();
						var pageSize=$('#pageSize').val();
						var busName = $('#busName').val();
						var busCode = $('#busCode').val();
					//	conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize+"&busName="+busName+"&busCode="+busCode+"&isContainsSun=1";
						conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize+"&isContainsSun=1";
						// location.href=url+conditionUrl;
                        window.location.reload(true);
					}else {
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_36"));
						$('#editcancel').attr("disabled",false);
					    $('#editsubmit').attr("disabled",false);
					}
				}
			);
		}else
		{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_50"));
			$('#editcancel').attr("disabled",false);
		    $('#editsubmit').attr("disabled",false);
		}
	}

	function addBusType()
	{
		var busName = $.trim($('#busNameAdd').val());
		var busDescription = $.trim($('#busDescriptionAdd').val());
		var busCode = $.trim($('#busCodeAdd').val());
		var busType = $('#busTypeAdd').val();
		var riseLevel = $('#riseLevelAdd').val();
		var pattern = /^[A-z0-9]+$/;
		var number=/^[0-9]*$/;
		var char =/[A-Za-z]/;
		var sign = /[@#_\-]/;
		$('#addcancel').attr("disabled",true);
		$('#addsubmit').attr("disabled",true);
		if(busName == ""){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_65"));
			$('#busNameAdd').val("");
			$('#busNameAdd').select();
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		if(busCode == ""){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_66"));
			$('#busCodeAdd').val("");
			$('#busCodeAdd').select();
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		if(busType == ""){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_67"));
			$('#busTypeAdd').select();
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		if($('#busDescriptionAdd').val().length>200)
		{
		    alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_64"));
		    $('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
		    return;
		}
		busCode=busCode.replace(/\\/g,getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_71"));
		if(!number.test(busCode)&&!char.test(busCode)&&!sign.test(busCode)) {
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_72"));
			$('#addcancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		else
		{
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $('#lgcorpcode').val();
			$.post("bit_busType.htm",
				{
					method:"add",
					lgcorpcode:lgcorpcode,
					lguserid:lguserid,
					busName : busName,
					busType : busType,
					riseLevel : riseLevel,
					busDescription : busDescription,
					busCode : busCode
				},function(result){
					if(result == "codeExists")
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_68"));
						$('#busCodeAdd').select();
						$('#addcancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}else if(result == "nameExists")
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_69"));
						$('#busNameAdd').select();
						$('#addcancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}else if(result == "true")
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_54"));
						doCancel();
						var url = 'bit_busType.htm';
						var conditionUrl = "";
						if(url.indexOf("?")>-1)
						{
							conditionUrl="&";
						}else
						{
							conditionUrl="?";
						}
						$("#hiddenValueDiv").find(" input").each(function(){
							conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
						});
						var pageIndex=$('#txtPage').val();
						var pageSize=$('#pageSize').val();
						var busName = $('#busName').val();
						var busCode = $('#busCode').val();
//						conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize+"&busName="+busName+"&busCode="+busCode+"&isContainsSun=1";
						conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize+"&isContainsSun=1";
						location.href=url+conditionUrl;
					}else {
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_55"));
						$('#addcancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}
				}
			);
		}
	}
	function regs(va) {
		var pattern = /^[A-z0-9]+$/;
		if(pattern.test(va.val())) {
			return;
		} else {
			
		}
	}
	function rtime()
	{
	    var max = "2099-12-31 23:59:59";
	    var v = $("#sendtime").attr("value");
	    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
    }
	function stime(){
	    var max = "2099-12-31 23:59:59";
	    var v = $("#recvtime").attr("value");
	    var min = "1900-01-01 00:00:00";
	    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:v});
	}
	
	function modify(t)
	{
  			$("#msg").children("xmp").empty();
			$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
			$("#singledetail").dialog("option","title", getJsLocaleMessage("xtgl","xtgl_cswh_ztmgl_56"));
  			$("#singledetail").dialog("open");
	}
	function fileBind(){
		$("#fileBindDiv").dialog("option","title", getJsLocaleMessage("xtgl","xtgl_ywlx_filedaoru"));
		$("#fileBindDiv").dialog("open");
}
	//检查上传的文件是否符合条件
	function checkUpload(){
		var uploadFile = $("#uploadFile").attr("value") ;
		$("#kwsok").attr("disabled",true);
		if(uploadFile.length == 0){//非空检查
			alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_25")) ;
			$("#kwsok").attr("disabled",false);
			return false ;
		}else if(uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xls" && uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xlsx" ){//检查文件格式是否是.txt格式
			alert(getJsLocaleMessage("xtgl","xtgl_ywlx_geshicuowu")) ;
			$("#kwsok").attr("disabled",false);
			return false ;
		}else{
			$('#import').attr("disabled","disabled");
			$("#uploadForm").submit();
		
		}
	}	