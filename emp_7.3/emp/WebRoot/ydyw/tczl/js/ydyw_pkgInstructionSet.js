	function bindBusSP()
    {
		var exitAllCmdAdd = $.trim($('#exitAllCmdEdit').val());
		var spidAll = $("#spidAllEdit").val();
		//确定之前还原必填项输入框颜色
		$('#exitAllCmdEdit').css("border-color","#BEC3D1");
		$('#spidAllEdit').css("border-color","#BEC3D1");
		//禁用确定 取消
		$('#allCancel').attr("disabled",true);
		$('#allCancelsubmit').attr("disabled",true);
		if(spidAll == "")
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_12"));
			$('#spidAllEdit').select();
			$('#spidAllEdit').css("border-color","red");
			$('#allCancel').attr("disabled",false);
			$('#allCancelsubmit').attr("disabled",false);
			return;
		}
		if(exitAllCmdAdd == "")
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_13"));
			$('#exitAllCmdEdit').select();
			$('#exitAllCmdEdit').css("border-color","red");
			$('#allCancel').attr("disabled",false);
			$('#allCancelsubmit').attr("disabled",false);
			return;
		}
		
		if (exitAllCmdAdd.indexOf(banStr) >= 0) {
            alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_14")+banStr+getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_15"));
            $('#exitAllCmdEdit').select();
            $('#exitAllCmdEdit').css("border-color","red");
            $('#allCancel').attr("disabled",false);
			$('#allCancelsubmit').attr("disabled",false);
            return;
        } 
		if (myReg.test(exitAllCmdAdd)) {
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_16"));
			$('#exitAllCmdEdit').select();
			$('#exitAllCmdEdit').css("border-color","red");
			$('#allCancel').attr("disabled",false);
			$('#allCancelsubmit').attr("disabled",false);
			return;
		} 
		if(exitAllCmdAdd.length>8)
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_17"));
			$('#exitAllCmdEdit').select();
			$('#exitAllCmdEdit').css("border-color","red");
			$('#allCancel').attr("disabled",false);
			$('#allCancelsubmit').attr("disabled",false);
			return;
		}else{
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $('#lgcorpcode').val();
			
			$.post("ydyw_pkgInstructionSet.htm",{method:"exitAll",lguserid:lguserid,lgcorpcode:lgcorpcode,exitAllCmdAdd:exitAllCmdAdd,spidAll:spidAll},function(result){
				if(result=="isExits")
				{
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_18"));
					$('#allCancel').attr("disabled",false);
					$('#allCancelsubmit').attr("disabled",false);
					return;
				}	
				else if(result=="true")
				{
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_19"));
					// 修复：先点‘查询’，再点‘全局设置’-‘确定’，‘设置成功’的弹框关闭后，页面显示数据被清空（有存在数据）。
					// location.href=location.href;
                    submitForm();
				}else 
				{
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_20"));
					$('#allCancel').attr("disabled",false);
					$('#allCancelsubmit').attr("disabled",false);
					return;
				}
			});
		}
    }
	function del(busId,aId)
	{
		if(confirm(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_21")))
		{
			$.post("ydyw_pkgInstructionSet.htm",{method:"delete",busId : busId,aId : aId},function(result){
				if(result=="true")
				{
					/*删除成功！*/
                    alert(getJsLocaleMessage("ydyw","ydyw_deleteSucceed"));
					var url = 'ydyw_pkgInstructionSet.htm';
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
					//conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize+"&busName="+busName+"&busCode="+busCode;
					conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize;
					location.href=url+conditionUrl;	
				}else 
				{
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_22"));
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
         $("#taocanNameAdd").val("");
         $("#taocanTypeAdd").text("");
         $("#spidAdd").val("");
         $("#orderCmdAdd").val("");
         $("#exitCmdAdd").val("");
         $("#addDiv").dialog("close");
    }
	
    function doAllCancel()
    {
         $("#bindDiv").dialog("close");
    }
    
	var BUSNAME = "";
	var TAOCANCODE = "";
	var TAOCANTYPE = "";
	var SPID = "";
	var STRUCTTYPE ="";
	var STRUCTCODE = ""
	var STATE = "";
	var BUSDESC = "";
	var CMDID = 0;
	var AID = 0;
	var myReg = /^[\u4e00-\u9fa5]+$/;
	var banStr =$('#splitStr').val(); 
	function update(busId,aId)
	{
		TAOCANCODE = $('#tcCode'+busId).val();
		TAOCANTYPE = $('#tcType'+busId).val();
		SPID = $('#spid'+busId).val();
		STRUCTTYPE = $('#structType'+busId).val();
		STRUCTCODE = $('#structCode'+busId).val();
		
		var taocantypetemp ="";
		if(TAOCANTYPE=="1"){
			taocantypetemp = getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_5");
		}else if(TAOCANTYPE=="2")
		{
			taocantypetemp = getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_by");
		}else if(TAOCANTYPE=="3")
		{
			taocantypetemp = getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_bj");
		}else if(TAOCANTYPE=="4")
		{
			taocantypetemp = getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_bn");
		}
		var structtypetemp ="";
		if(STRUCTTYPE=="0"){
			structtypetemp = getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_3");
		}else if(STRUCTTYPE=="1")
		{
			structtypetemp = getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_4");
		}
		$('#taocanNameEdit').text(TAOCANCODE);
		$('#taocanTypeEdit').text(taocantypetemp);
		$('#structTypeEdit').text(structtypetemp);
		$('#hidstructTypeEdit').val(STRUCTTYPE);
		$('#structCodeEdit').val(STRUCTCODE);
		$("#spidEdit").val(SPID);
		//$("#busTypeEdit").val(BUSTYPE);
		//$("#riseLevelEdit").val(RISELEVEL);
		//$("input:radio[name='stateEdit'][value="+STATE+"]").attr("checked",true);
		//$('#busDescriptionEdit').val(BUSDESC);
		CMDID = busId;
		AID = aId;
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
		var spid = $('#spidEdit').val();
		//指令类型
		var structType = $('#hidstructTypeEdit').val();
		var structTypeStr ="";
		var structCode = $.trim($('#structCodeEdit').val());
		//确定之前还原必填项输入框颜色
		$('#spidEdit').css("border-color","#BEC3D1");
		//禁用确定 取消
		$('#editcancel').attr("disabled",true);
		$('#editsubmit').attr("disabled",true);
		if(structType == "0")
		{
			structTypeStr = getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_43");
		}else if(structType == "1"){
			structTypeStr = getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_44");
		}
		if(spid == "")
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_12"));
			$('#spidEdit').select();
			$('#spidEdit').css("border-color","red");
			$('#editcancel').attr("disabled",false);
			$('#editsubmit').attr("disabled",false);
			return;
		}
		if(structCode == "")
		{
			alert(structTypeStr+getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_23"));
			$('#structCodeEdit').select();
			$('#editcancel').attr("disabled",false);
			$('#editsubmit').attr("disabled",false);
			return;
		}
		if(structCode.length>8)
		{
			alert(structTypeStr+getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_24"));
			$('#structCodeEdit').select();
			$('#editcancel').attr("disabled",false);
			$('#editsubmit').attr("disabled",false);
			return;
		}
		if (myReg.test(structCode)) {
			alert(structTypeStr+getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_25"));
			$('#structCodeEdit').select();
			$('#editcancel').attr("disabled",false);
			$('#editsubmit').attr("disabled",false);
			return;
		}
		var pattern=/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;  
		if(pattern.test(structCode)){
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_26"));
			$('#structCodeEdit').select();
			$('#editcancel').attr("disabled",false);
			$('#editsubmit').attr("disabled",false);			
			return;
		}			
		if (structCode.indexOf(banStr) >= 0) {
            alert(structTypeStr+getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_27")+banStr+"！");
            $('#structCodeEdit').select();
            $('#editcancel').attr("disabled",false);
			$('#editsubmit').attr("disabled",false);
            return;
        }
		else if(SPID != spid || STRUCTCODE != structCode )
		{
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $('#lgcorpcode').val();
			$.post("ydyw_pkgInstructionSet.htm",
				{
					method:"update",
					lgcorpcode:lgcorpcode,
					lguserid:lguserid,
					structCode : structCode,
					spid : spid,
					aId : AID,
					cmdId : CMDID
				},function(result){
					if(result == "structcodeExists")
					{
						alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_18"));
						$('#structCodeEdit').select();
						$('#editcancel').attr("disabled",false);
						$('#editsubmit').attr("disabled",false);
					}else if(result == "true")
					{
						alert(getJsLocaleMessage("ydyw","ydyw_ywgl_text_40"));
						$("#editDiv").dialog("close");
						
						var url = 'ydyw_pkgInstructionSet.htm';
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
						//var busName = $('#busName').val();
						//var busCode = $('#busCode').val();
						conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize;
						location.href=url+conditionUrl;							
						
					}else {
						alert(getJsLocaleMessage("ydyw","ydyw_ywgl_text_41"));
						$('#editcancel').attr("disabled",false);
						$('#editsubmit').attr("disabled",false);
					}
				}
			);
		}else
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_28"));
			$('#editcancel').attr("disabled",false);
			$('#editsubmit').attr("disabled",false);
		}
	}

	function addBusType()
	{
		var taocanCode = $('#taocanNameAdd').val();
		var taocantype = $("#taocanNameAdd option:selected").attr("tem");
		var taocanname = $("#taocanNameAdd option:selected").attr("tcname");
		var taocanmoney = $("#taocanNameAdd option:selected").attr("tcmoney");
		var spid = $('#spidAdd').val();
		var orderCmd = $.trim($('#orderCmdAdd').val());
		var exitCmd = $.trim($('#exitCmdAdd').val());
		//禁用确定 取消
		$('#addCancel').attr("disabled",true);
		$('#addsubmit').attr("disabled",true);
		//确定之前还原必填项输入框颜色
		$('#taocanNameAdd').css("border-color","#BEC3D1");
		$('#spidAdd').css("border-color","#BEC3D1");
		if(taocanCode == "")
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_29"));
			$('#taocanNameAdd').css("border-color","red");
			$('#taocanNameAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		if(spid == "")
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_30"));
			$('#spidAdd').css("border-color","red");
			$('#spidAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		if(orderCmd == "" && exitCmd == "")
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_31"));
			$('#orderCmdAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		if(orderCmd.toLowerCase() == exitCmd.toLowerCase())
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_32"));
			$('#orderCmdAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		if(orderCmd.length>8)
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_33"));
			$('#orderCmdAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		if(exitCmd.length>8)
		{
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_34"));
			$('#exitCmdAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		//不能包含特殊符号
		
		if (orderCmd.indexOf(banStr) >= 0) {
            alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_35")+banStr+"！");
            $('#orderCmdAdd').select();
            $('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
            return;
        } 
		if (exitCmd.indexOf(banStr) >= 0) {
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_36")+banStr+"！");
			$('#exitCmdAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		var pattern=/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;  
		if(pattern.test(orderCmd)){
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_37"));
			$('#orderCmdAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);			
			return;
		}
		if(pattern.test(exitCmd)){
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_38"));
			$('#exitCmdAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);			
			return;
		}			
		if (myReg.test(orderCmd)) {
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_39"));
			$('#orderCmdAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		} 
		if (myReg.test(exitCmd)) {
			alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_40"));
			$('#exitCmdAdd').select();
			$('#addCancel').attr("disabled",false);
			$('#addsubmit').attr("disabled",false);
			return;
		}
		else
		{
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $('#lgcorpcode').val();
			$.post("ydyw_pkgInstructionSet.htm",
				{
					method:"add",
					lgcorpcode:lgcorpcode,
					lguserid:lguserid,
					taocanCode : taocanCode,
					taocantype : taocantype,
					taocanname : taocanname,
					taocanmoney : taocanmoney,
					spid : spid,
					orderCmd : orderCmd,
					exitCmd : exitCmd
				},function(result){
					if(result == "orderisExits")
					{
						alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_41"));
						$('#orderCmdAdd').select();
						$('#addCancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}else if(result == "exitisExits")
					{
						alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_42"));
						$('#exitCmdAdd').select();
						$('#addCancel').attr("disabled",false);
						$('#addsubmit').attr("disabled",false);
						return;
					}else if(result == "true")
					{
						alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_42"));
						doCancel();
						var url = 'ydyw_pkgInstructionSet.htm';
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
						//conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize+"&busName="+busName+"&busCode="+busCode;
						conditionUrl = conditionUrl+"pageIndex="+pageIndex+"&pageSize="+pageSize;
						location.href=url+conditionUrl;	
					}else {
						alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_43"));
						$('#addCancel').attr("disabled",false);
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
			$("#singledetail").dialog("option","title",getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_8_p"));
  			$("#singledetail").dialog("open");
	}
    function taocanChange()
    {
    	var jf = $("#taocanNameAdd option:selected").attr("tem");
    	var s ="";
    	if(jf =="1"){
    		s= getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_5");
    	}else if(jf =="2"){
    		s= getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_by");
    	}else if(jf == "3"){
    		s= getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_bj");
    	}else if(jf == "4"){
    		s= getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_bn");
    	}
    	$("#taocanTypeAdd").text(s);
    }
    function limitzw(value){
    	var val=value.val();
    
    	var sArr2 = val.split("");
    	var s1= "";
		for(y in sArr2){
			if(!myReg.test(sArr2[y])){
				s1+=sArr2[y];
			}
		}
		value.val(s1);
    }