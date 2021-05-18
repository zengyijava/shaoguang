//修改状态，启用，禁用
function changeStaute(ecid,funname,enStatus){
		if(enStatus=="1"){
			/*您确定要禁用该方法吗？*/
			if(!confirm(getJsLocaleMessage("txgl","txgl_js_funList_1"))){
					return;
				}
		}
		if(enStatus=="0"){
			/*您确定要启用该方法吗？*/
			if(!confirm(getJsLocaleMessage("txgl","txgl_js_funList_2"))){
					return;
				}
		}
			$.post('wg_apiBaseMage.htm?method=changeStaute', {
				ecid:ecid,
				funname:funname,
				enStatus:enStatus
				},function(result)
			{
				window.location.href = window.location.href;

			});
		

}


//删除当前记录
function del(ecid,funname){
	var funtype=$("#funtype").val();
			/*您确定要删除吗？*/
			if(!confirm(getJsLocaleMessage("txgl","txgl_js_funList_3"))){
					return;
				}
			$.post('wg_apiBaseMage.htm?method=del', {
				ecid:ecid,
				funtype:funtype,
				funname:funname
				},function(result)
			{
				window.location.href = window.location.href;

			});
}
//编辑方法
function edit(ecid,funtype,url,funname,cust_intfname,cfunname,reqtype,resptype,status){
	if(status=="1"){
		/*启用状态不能修改！*/
		alert(getJsLocaleMessage("txgl","txgl_js_funList_4"));
		return;
	}
	
	$("#mwInter").val(funname);
	$("#funType").val(funtype);
	$("#basePath").val(url);
	$("#basePath").val(url);
	$("#clientInterface").val(cfunname);
	$("#interfaceName").val(cust_intfname);
	$("#req_type").val(reqtype);
	$("#resp_type").val(resptype);
	$("#updatemethod").css("display","block");
	$('#updatemethod').dialog({
		autoOpen: false,
		height: 310,
		width: 400,
		resizable:false,
		modal:true
	});
	
	$('#updatemethod').dialog('open');
}

function btcancel(){
	$("#updatemethod").dialog("close");
	
}
//修改
function save(){
	var ecid=$("#ecid").val();
	//FUNTYPE 方法作为一个条件，由于主动推送时候为空
	var mwInter=$("#mwInter").val();
	
	var clientInterface =$("#clientInterface").val();
	var interfaceName=$("#interfaceName").val();
	var req_type=$("#req_type").val();
	var resp_type=$("#resp_type").val();
	var funtype=$("#funtype").val();
			$.post('wg_apiBaseMage.htm?method=edit', {
				mwInter:mwInter,
				clientInterface: clientInterface,
				interfaceName:interfaceName,
				req_type:req_type,
				funtype:funtype,
				ecid:ecid,
				resp_type:resp_type
				},function(result)
			{
				if(result=="true"){
                    /*修改成功！*/
                    alert(getJsLocaleMessage("common","common_modifySucceed"));
					$('#updatemethod').dialog('close');	
					window.location.href = window.location.href;
				}else{
					/*修改失败，请联系管理员！*/
					alert(getJsLocaleMessage("txgl","txgl_js_funList_5"));
				}
			});
}
//映射
function mapping(funname,req,resp,funtype,status){
	var commonPath=$("#commonPath").val();
	var ecid=$("#ecid").val();
	$("#mappingDiv").dialog({
		autoOpen: false,
		height:605,
		width: 1050,
		resizable:false,
		modal: true,
		close:function(){
		$("#mappingFrame").attr("src",commonPath+"/common/blank.jsp");
		}
	});

	var pathUrl = $("#pathUrl").val();
	$(".ui-dialog-titlebar-close").show();
	var frameSrc = $("#mappingFrame").attr("src");
	if(frameSrc.indexOf("blank.jsp") > 0)
	{
		var lguserid = $("#lguserid").val();
		frameSrc = pathUrl+"/wg_apiBaseMage.htm?method=mapping&lguserid="+lguserid+"&ecid="+ecid+"&funname="+funname+"&req="+req+"&resp="+resp+"&funtype="+funtype+"&cmdtype=1&status="+status;
		$("#mappingFrame").attr("src",frameSrc);
	}
	
	$("#mappingDiv").dialog("open");
}