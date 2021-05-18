$(document).ready(function(){
	$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
		
	$("#fileBindDiv").dialog({
		autoOpen: false,
		resizable:false,
		height: 200,
		width: 450,
		modal: true,
		close:function(){
			showSelect();
		},
		open:function(){
			hideSelect();
		}
	});
	$("#cancelipmacDiv").dialog({
		autoOpen: false,
		resizable:false,
		height: 225,
		width: 220,
		title:getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_17"),
		modal: true,
		close:function(){
			showSelect();
			$('input[name="cancelType"]').each(function(){    
		   		 $(this).attr("checked", false);
		 	});   
		},
		open:function(){
			hideSelect();
		}
	});
    //var X = $("#ipBindDiv").offset().left+250;
   // var Y = $("#ipBindDiv").offset().top+150;
	$("#ipBindDiv").dialog({
		autoOpen: false,
		resizable:false,
		//position:[X,Y],
		height: 450,
		width: 500,
		modal: true,
		close:function(){
			doCancel();
			showSelect();
			//$(".ui-dialog-buttonpane button").attr("disabled",false);
		},
		open:function(){
			hideSelect();
		}
	});

	$('#search').click(function(){submitForm();});
});
function fileBind(){
		$("#fileBindDiv").dialog("option","title", getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_18"));
		$("#fileBindDiv").dialog("open");
}
function doCancel(){
	 for(var i=1;i<11;i++){
	    $("#ip"+i).val("");
	    $("#mac"+i).val("");
    	if(i>=3 && $("#ip"+i+"tr").css("display")=="inline"){
    		$("#ip"+i+"tr").css("display","none");
    	}
    	if(i>=3 && $("#mac"+i+"tr").css("display")=="inline"){
    		$("#mac"+i+"tr").css("display","none");
    	}
    }
}

function hideSelect(){
	$("#isBindMac").css("visibility","hidden");
	 $("#isBindIp").css("visibility","hidden");
	 $("#isBindPwd").css("visibility","hidden");
}
function showSelect(){
	 $("#isBindMac").css("visibility","visible");
	 $("#isBindIp").css("visibility","visible");
	 $("#isBindPwd").css("visibility","visible");
}

function manualAdd(){
	var macs = "";
	var regMac = /[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}/;
	for(var i = 1;i<11;i++){
    	if($("#mac"+i).val() != ""){
    	    if(!regMac.test($("#mac"+i).val())){
    	    	alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_19"));
    			$("#mac"+i).focus();
    			return ;
    	    }
    	    if(macs.indexOf($("#mac"+i).val()) != -1){
    	    	alert($("#mac"+i).val()+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_20"));
    	    	$("#mac"+i).focus();
    			return ;
    	    }
    		macs = macs+ $("#mac"+i).val()+",";
    	}
	}
    macs = macs.substring(0,macs.lastIndexOf(","));
    $("#macaddrs").val(macs);
	var ips = "";
    var reg = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
    for(var i = 1;i<11;i++){
    	if($("#ip"+i).val() != ""){
    		if(!reg.test($("#ip"+i).val())){
    			alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_21"));
    			$("#ip"+i).focus();
    			return ;
    		}
    		if(ips.indexOf($("#ip"+i).val()) != -1){
    	    	alert($("#ip"+i).val()+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_20"));
    	    	$("#ip"+i).focus();
    			return ;
    	    }
    		ips = ips+$("#ip"+i).val()+",";
    	}
    }
    if(ips == "" && macs == "" && $("#opType").val()=="add"){
    	alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_22"));
    	return;
    }
    ips = ips.substring(0,ips.lastIndexOf(","));
    $("#ipaddrs").val(ips);
    $(".ui-dialog-buttonpane button").attr("disabled",true);
	document.getElementById("macipAdd").submit();
}
function submitForm(){
	//添加查询滚动功能
	var search = document.getElementById('search');
	if(search)
	{
		search.isClick = true;
	}
	window.parent.loading();
	
	var time = new Date().valueOf();
	$('#tableInfo').load($('#servletUrl').val(),
		{
			method:'getTable',
			time:time,
			lgcorpcode:$("#lgcorpcode").val(),
			lgguid:$("#lgguid").val(),
			type:$("#type").val(),
			depId:$('#depTreeId').val(),
			userName:$("#userName").val(),
			name:$("#name").val(),
			ipaddr:$("#ipaddr").val(),
			macaddr:$("#macaddr").val(),
			pageIndex:$('#txtPage').val(),
			pageSize:$('#pageSize').val(),
			totalPage:$("#totalPage").val(),
			totalRec:$("#totalRec").val(),
			isBindMac:$("#isBindMac").val(),
			isBindIp:$("#isBindIp").val(),
			isBindPwd:$("#isBindPwd").val()
		}
	);
}
function toBindIpMac(){
	var selected=document.getElementsByName("macip");
	var n=0;		//统计勾选中的个数
	var id="";
	var lmiid;
	for(i=0;i<selected.length;i=i+1){
		if(selected[i].checked==true){
			id=id+selected[i].value;
			id=id+","
			n=n+1;
		}
	}
	id=id.substring(0,id.lastIndexOf(','));
	if(n<1){
		alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_23"));
		return;
	}else{
    	$("#ids").val(id);
    	$("#ipBindDiv").dialog("option","title", getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_24"));
    	 $("#pwd").css("display","none");
    	 $("#opType").val("add");
    	$("#ipBindDiv").dialog("open");
	}
}

//检查上传的文件是否符合条件
function checkUpload(){
	var uploadFile = $("#uploadFile").attr("value") ;
	$("#kwsok").attr("disabled",true);
	if(uploadFile.length == 0){//非空检查
		alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_25")) ;
		$("#kwsok").attr("disabled",false);
		return false ;
	}else if(uploadFile.substring(uploadFile.lastIndexOf('.')+1)!="xls"){//检查文件格式是否是.txt格式
		alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_26")) ;
		$("#kwsok").attr("disabled",false);
		return false ;
	}else{
		$('#import').attr("disabled","disabled");
		$("#uploadForm").submit();
	}
}
function back(){
	$("#fileBindDiv").dialog("close");
}

function addAddr(type){
    var msg = type=="ip"?"IP":"MAC";
    if($("#"+type+"10tr").css("display") == "inline"){
    	alert(msg+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_27"));
    }
    for(var i=3;i<11;i++){
    	if(!$("#"+type+i+"tr").is(":hidden")){
    		continue;
    	}else{
    		$("#"+type+i+"tr").show();
    		break;
    	}
    }
}
//启用动态口令
function batchEnablePwd(){
	var selected=document.getElementsByName("macip");
	var n=0;		//统计勾选中的个数
	var id="";
	for(i=0;i<selected.length;i=i+1){
		if(selected[i].checked==true){
			id=id+selected[i].value;
			id=id+","
			n=n+1;
			if(selected[i].id != "0"){
				alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_28"));
				return ;
			}
		}
	}
    //对选中的id过滤 若包含100000企业sysadmin或其他企业admin则提示
    var $checked = $("input[name='macip']:checked");
    var lgcorpcode = GlobalVars.lgcorpcode;
    var isExistAdmin = false;
    $checked.each(function(){
        var username = $.trim($(this).parents("tr").children("td:eq(2)").text());
        if((lgcorpcode == 100000 && username == 'sysadmin') || (lgcorpcode != 100000 && username == 'admin')){
            //$(this).attr('checked',false);
            alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_29")+username+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_30"));
            isExistAdmin = true;
            return false;
        }
    });
    if(isExistAdmin){
        return;
    }
	id=id.substring(0,id.lastIndexOf(','));
	if(n<1){
		alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_31"));
		return;
	}else{
	    if(confirm(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_32"))){
	    	$.post("ctr_securectrl.htm",{method:"enablePwd",guid:id,lgcorpcode:$("#lgcorpcode").val()},
			function(result){

				if(result=="noSpUser")
				{
				    alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_33"));
				}
				else if(result.indexOf("&") > 0)
				{
					var sucResult = result.substring(0,result.indexOf("&"));
					if(sucResult == "true")
					{
						var nophonename = result.substring(result.indexOf("&")+1,result.length);
						
						if(nophonename.length > 0)
						{
							alert(nophonename+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_34"));
						}else{
							alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_12"));
						}
						
						location.href="ctr_securectrl.htm?method=find&lgguid="+$("#lgguid").val()+"&lgcorpcode="+$("#lgcorpcode").val()+"&isOperateRetrun=true";
					}
					if(sucResult == "false")
					{
						var nophonename = result.substring(result.indexOf("&")+1,result.length);
						
						if(nophonename.length > 0)
						{
							alert(nophonename+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_35"));
						}else{
							alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_13"));
						}
						
					}
					
				}
				else if(result == "error")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_13"));
				}
			});
	    }
	}
}
function toCancelBind(){
	var selected=document.getElementsByName("macip");
	var n=0;		//统计勾选中的个数
	for(i=0;i<selected.length;i=i+1){
		if(selected[i].checked==true){
			n=n+1;
		}
	}
	if(n<1){
		alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_31"));
		return;
	}else{
	$("#checkedCount").val(n);
		$("#cancelipmacDiv").dialog("open");
	}
	
}

function cancelIpMac(){
	var cancelValue ="";    
 	$('input[name="cancelType"]:checked').each(function(){    
   		cancelValue += $(this).val()+",";
 	});    
 	var cancelType;
 	var str ;
    if(cancelValue.length==0){
  	  alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_36"));
  	  return ;
    } 
    cancelValue= cancelValue.substring(0,cancelValue.lastIndexOf(","));
    var selected=document.getElementsByName("macip");
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
    if(cancelValue.indexOf("1") !=-1 && cancelValue.indexOf("2") !=-1 && cancelValue.indexOf("3") !=-1){
    	str=getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_37");
    }else if(cancelValue.indexOf("1") !=-1 && cancelValue.indexOf("2") !=-1){
    	str = getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_38");
    }else if(cancelValue.indexOf("1") !=-1 && cancelValue.indexOf("3") !=-1){
    	str = getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_39");
    }else if(cancelValue.indexOf("2") !=-1 && cancelValue.indexOf("3") !=-1){
    	str = getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_40");
    }else{
   	 	if(cancelValue.indexOf("1") !=-1){
    		str = getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_41");
    	}else if(cancelValue.indexOf("2") !=-1){
    		str = getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_42");
    	}else if(cancelValue.indexOf("3") !=-1){
    		str = getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_43");
    	}
    }
    var c = n == 1?"":getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_44");
	if(confirm(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_45")+c+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_46")+str+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_47"))){
		ipmacBtCancel();
	    var guids = $("#guids").val();
		$.post("ctr_securectrl.htm",{method:"cancelBind",guid:id,cancelType:cancelValue},
			function(result){
				if(result=="true"){
					alert(c+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_46")+str+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_48"));
					location.href="ctr_securectrl.htm?method=find&lgguid="+$("#lgguid").val()+"&lgcorpcode="+$("#lgcorpcode").val()+"&isOperateRetrun=true";
				}else if(result == "error" || result == "false"){
					alert(c+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_46")+str+getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_49"));
				}else if(result == "norecord"){
					alert(getJsLocaleMessage("xtgl","xtgl_czygl_gjaqsz_50"));
				}
			});
	}
}
function ipmacBtCancel(){
	$("#cancelipmacDiv").dialog("close");
}