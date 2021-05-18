/******************************************************************************************************************************
	移动财务功能模块使用的脚本 

	@author jinny.ding (www.montnets.com)
	@version 1.0 
******************************************************************************************************************************/



/*****************************************************************************************************************************
	文件导入发送选项卡中
******************************************************************************************************************************/
var bus = "";
function hideSelect()
{
	$("#select").css("display","none");
	$("#busCode").css("display","none");
	$("#spUser").css("display","none");
}
function showSelect()
{
	$("#select").css("display","inline");
	$("#busCode").css("display","inline");
	$("#spUser").css("display","inline");
}

//取得模板内容及参数
function getTempMsg(tmpOb) {
	var msgob = $("form").find("textarea[name='textarea']");
	var text_show=$('#text_show');
	if(tmpOb.val()!=""){
		$.ajax({
			url:"ycw_electronicPayroll.htm",
			data:{method : "getTmMsg",tmId : tmpOb.val()},
			type:"post",
			success:function(result){
				if(result.indexOf("@")==-1){
					window.location.href='ycw_electronicPayroll.htm';
					return;
				}
				result=result.substr(result.indexOf("@")+1);
				if(result=="error")
				{
					alert(getJsLocaleMessage('ydcw','ydcw_alert_1'));
					return;
				}
				var tempArr = result.split("[?]");
				if(!tempArr[1]){
					msgob.val("");
					text_show.html("");
					alert(getJsLocaleMessage('ydcw','ydcw_alert_2'));
					return;
				}
				var tempArr01 = tempArr[1].split("?");
				$("#tempArr").attr("value",tempArr[1]);
				var len = tempArr01.length;//参数个数
				$("#textCell").attr("value",parseFloat(len+1));
				$("#textRow").attr("value",21);//设置默认行数(包括标题),初始化可以手工入录21条记录
				//设置参数个数到隐藏域	
				for(i = 0; i < len; i++){
					$("#paraLen").attr("value",len);
					$("#id"+i).html(getJsLocaleMessage('ydcw','ydcw_alert_3')+tempArr01[i]); //写入表格
				}
                //移动财务&nbsp;显示修改
                // msgob.val(tempArr[0]);//设置内容
                // text_show.html(getChangeVal(tempArr[0]));
                var tmplId=tmpOb.val();
                msgob.text($("#msgXmp"+tmplId).text());//设置内容
                text_show.html("<xmp>"+getChangeVal(tempArr[0])+"</xmp>");

				$("#textarea").attr("readOnly",true);
				$("#paraNumber").html("0");//默认显示录入行数
				var $paraValue = $("#paraValue").val();
				var templateIds = tempArr[1];//得到参数下标
				$("#templateIds").attr("value",templateIds);
				if ($paraValue != "") {
					$("#tableDiv").empty();	//如果没有值则,删除上一次表格
					$("#paraValue").val("");
				}
			},
			error:function(xrq,textStatus,errorThrown){
				alert(getJsLocaleMessage('ydcw','ydcw_alert_4'));
				window.location.href='ycw_electronicPayroll.htm';
			}
		});
	}else {
		msgob.val("");
		text_show.html("");
	}
}
//用参数1，参数2显示 #p_1# #p_2#
function getChangeVal(msg){
	var reg=/#p_(.*?)#/gi;
	var msgre=msg.replace(reg,replacerChange);
	return msgre;
}
//发送验证
function sendChecking(){
	var subType = $("#subType").val();

	if ($('#select').val() == "") {
		alert(getJsLocaleMessage('ydcw','ydcw_alert_5'));
		return false;
	} 
	if(subType == "0"){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_6'));
		return false;
	}
	if (subType == "1" && $('#numFile').val() == "") {
		alert(getJsLocaleMessage('ydcw','ydcw_alert_7'));
		return false;
	}
	if(subType == "2" && ($("#pNumer").val() == 0 || $("#paraValue").val() == "")){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_8'));
		return false;
	}
	if ($('#spUser').val() == null || $('#spUser').val() == "") {
		alert(getJsLocaleMessage('ydcw','ydcw_alert_9'));
		return false;
	} 
	if ($('#textarea').val() == "") {
		alert(getJsLocaleMessage('ydcw','ydcw_alert_10'));
		return false;
	}
	
	$("#tempId").attr("value",$("#select").val());
	// 兼容IE9(ie9不支持document.selection.createRange().text;)
	//$("#fpath1").attr("value",getUploadFilePath());
	var isCheckTime = $("input[name='timerStatus']:checked").val();//是否定时
	isCheckTime = isCheckTime==1?true:false;
	$("#isCheckTime").attr("value",isCheckTime);
	if (isCheckTime == 1) {
		$("#deterTime").attr("value",$("#timerTime").val());//定时内容
		if ($("#deterTime").val() == "") {
			alert(getJsLocaleMessage('ydcw','ydcw_alert_11'));
			return false;
		}else{
			//判断时间是否是未来的时间
			var cbool = this.compareDate($("#deterTime").val());
			if(cbool == false){
				return false;
			} 
		}
	}
	if($("input:radio[name='reply']:checked").attr("value") != 0
			&& $("#subNo").val() == ""){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_12'));
		return ;
	}
	var sendFlag = $("#sendFlag").val().split("&");
	if(sendFlag[0] == "false" && sendFlag[1] == "false" && sendFlag[2] == "false"){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_13'));
		return ;
	}
	if(sendFlag[0] == "false" && sendFlag[1] == "false" ){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_14'));
		return ;
	}
	if(sendFlag[0] == "false" && sendFlag[2] == "false"){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_15'));
		return ;
	}
	if(sendFlag[1] == "false" && sendFlag[2] == "false"){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_16'));
		return ;
	}
	if(sendFlag[0] == "false" ){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_17'));
		return ;
	}
	if(sendFlag[1] == "false"){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_18'));
		return ;
	}
	if(sendFlag[2] == "false"){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_19'));
		return ;
	}
	$("#spAccount").attr("value",$("#spUser").val());//SP账号
	$("#isReply").attr("value",$("input:radio[name='reply']:checked").attr("value"));//是否需要回复
	$("#busCode1").attr("value",$("#busCode").val());
	return true;
}

//删除上传附件
function delfile(){	 
	if(confirm(getJsLocaleMessage('ydcw','ydcw_alert_20'))) { 
	    var fileobj = document.getElementById("numFile");
		fileobj.outerHTML = fileobj.outerHTML; 
	    $("#upTipDiv").html("");
	    $("#upTipDiv").hide();
	}
}

//创建手工录入数据DIV层
function modify(){
	//隐藏上传文件文件列表
	var fileobj = document.getElementById("numFile");
	fileobj.outerHTML = fileobj.outerHTML; 
    $("#upTipDiv").html("");
    $("#upTipDiv").hide();
	var msgob = $("form[id='ydcwForm']").find("select[name='select']");
	if (msgob.val() == ""){//不选择模板,但内容不为空则要清空
		$("#textarea1").val("");
	}
			
	//Is selected template 
	var $textarea = $("#textarea").val();
	if ($textarea == ""){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_5'));
		return;
	}
	hideSelect();			
	//Show dialog DIV
	$('#modify').dialog({
		autoOpen:false,
		closeOnEscape:true,//点击键盘ESC键关闭dialog，默认为true
		resizable:false,//不能调整大小
		modal:true,
		bgiframe:true,//用于修复在IE6浏览器中无法显示于其它控件（select,flash）之上的问题。
		width:450,
	    height:500,
	    close: function(event, ui){
	    	//save();
	    	showSelect();
	    },open:function(){
	    	$(".ui-dialog-titlebar").show();
	    	$(".ui-dialog-titlebar-close").show();
	    }
	/*,
	    buttons:{
			'保存(Ctrl+S)':function(){
	    	save();
	    	},
			'重置(Ctrl+Del)':function(){
	    		cleardb();
	    	}
	    
	    }*/
	    
	});
	//$(".ui-widget-header").css("background","#e9f1fa");			
	//Delete history table 
	$("#tabletId").html("");
	$("#modify").dialog('open'); 
				
	//Initialize tables
	initTable();
				
	//Processing key combinations
	//$.hotkeys.add('Ctrl+S', function () {
	 	//save();
	//});
	//$.hotkeys.add('Ctrl+del',function(){
		//cleardb();
	//});
} 
	
/*****************************************************************************************************************************
	选项卡加载
******************************************************************************************************************************/
$(document).ajaxStop($.unblockUI); 
		 
$(document).ready(function(){
	$('#preview').dialog({
		autoOpen: false,
		bgiframe:true,
		width:520,
	    height:520,
	    modal:true,
	    open:function(){
			$(".ui-dialog-titlebar").show();
	    	$(".ui-dialog-titlebar-close").hide();
	    }, 
	    close:function(){
	    	$("#subSend").attr("disabled",false); 
	    }
	}
	);
	
	var isIE = (document.all) ? true : false;
    var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
	var isIE7 = isIE && (navigator.appVersion.match(/MSIE 7.0/i)=="MSIE 7.0" ? true : false);
	var isIE8 = isIE && (navigator.appVersion.match(/MSIE 8.0/i)=="MSIE 8.0" ? true : false);
	var isIE9 = isIE && (navigator.appVersion.match(/MSIE 9.0/i)=="MSIE 9.0" ? true : false);
	var userAgent = navigator.userAgent.toLowerCase(); 
	var ismozi360 = userAgent.indexOf('mozilla/4.0')>-1?true:false;
	var isie360 = userAgent.indexOf('360se')>-1?true:false;
	var isSG =(userAgent.indexOf('se 2.x') != -1);
	var tophei = $('.top:visible').height();
	var topMargin = $('.top:visible').css('margin-top'); 
	if(tophei==null)
	{
		tophei = 0;
		/*if(isSG){
			$("#filesdiv").css("top","165px");
		}else if(isIE && isIE6){
			$("#filesdiv").css("top","180px");
		}else if(isIE && isIE7){
			$("#filesdiv").css("top","165px");
		}else if(isIE && (isIE8 || isIE9)){
			$("#filesdiv").css("top","170px");
		}else{
			$("#filesdiv").css("top","175px");
		}*/
	}else{
		/*if(isSG){
			$("#filesdiv").css("top","185px");
		}else if(isIE && isIE6){
			$("#filesdiv").css("top","210px");
		}else if(isIE && isIE7){
			$("#filesdiv").css("top","195px");
		}else if(isIE && (isIE8 || isIE9)){
			$("#filesdiv").css("top","200px");
		}else{
			$("#filesdiv").css("top","200px");
		}*/

	}
	if(topMargin != null)
	{
		tophei = tophei + 5;
		/*if(isSG){
			$("#filesdiv").css("top","195px");
		}else if(isIE && isIE6){
			$("#filesdiv").css("top","210px");
		}else if(isIE && isIE7){
			$("#filesdiv").css("top","195px");
		}else if(isIE && (isIE8 || isIE9)){
			$("#filesdiv").css("top","200px");
		}else{
			$("#filesdiv").css("top","205px");
		}*/
	}
	
	
	
	$("#filesdiv input").hover(function(e){
		$("#upfile").css("text-decoration","underline");
	},function(e){
		$("#upfile").css("text-decoration","none");
	});
	$("#input").hover(function(e){
		$(this).css("text-decoration","underline");
	},function(e){
		$(this).css("text-decoration","none");
	});
	//导入文件发送等待
	$('#subButt').click(function(){ 
		if(sendChecking()){
		 	//var path =$("#pathId").val();  
		    //$.blockUI({message:'<table width="100%"><tr><td align="right" width="35%"><img src="'+path+'/images/webim/loading.gif" /></td><td align="left"><h1><font size="2px">&nbsp;请稍后,正在提交预览...</font></h1></td></tr></table>' });
			var input_method = "filePreview";
			var manual_method = "manualPreview";
			if($("#buscode").val() == "MF0001"){//电子工资单
				bus = "ycw_electronicPayroll.htm";
			}
			if($("#buscode").val() == "MF0002"){//报销提醒
				bus = "ycw_reimbursementRemind.htm";
			}
			if($("#buscode").val() == "MF0003"){//回款通知
				bus = "ycw_backFundsNotice.htm";
			}
			var subType = $("#subType").val();
		    if(subType == "1"){
				$("form[name='ydcwForm']").attr("encoding","multipart/form-data");
				$("form[name='ydcwForm']").attr("action",bus+"?method=filePreview");
		    }else{
					$("form[name='ydcwForm']").attr("encoding","application/x-www-form-urlencoded");
		    	$("form[name='ydcwForm']").attr("action",bus+"?method=manualPreview");
		    }
		    $("#probar").dialog({
				modal:true,
				title:getJsLocaleMessage('ydcw','ydcw_alert_21'), 
				height:100,
				resizable :false,
				closeOnEscape: false,
				width:200,
				open: function(){
			  		errorNum=0;
					$(".ui-dialog-titlebar").hide();
					dd = window.setTimeout("fresh()",3000);
		  		}
		    });
			//防止内存溢出，初始化为0。预览成功为1，为0则 提示系统繁忙
			$("#error").val("0");
			$("#ydcwForm").submit();
		    //return true;
	    }else{
	       //	return false;
	    }
	}); 
    
});
	
function preSend(errorString,result){
	$("#error").val("1");	
	window.clearInterval(dd);
	//关闭旋转界面
	$("#probar").dialog("close");
	if(errorString != ""){
		alert(errorString);
		return ;
	}
	if(result.indexOf("empex")===0){
		alert(getJsLocaleMessage("ydcw","ydcw_alert_52")+result.substring(5));
		$("input#subButt").attr("disabled","disabled");
		return;
	}
	var cal = result.split("cw3Pre");
	if (cal[0] == "" && cal[1] == ""){
		return ;
	} 
	if (cal[2] == ""){
		$("#subSend").attr("disabled",true); 
	}
	hideSelect();
	//绘制预览层
	$("#newtable tr").remove();
	var count = cal[0].split("cw0Pre");
	var msg = cal[1].split("cw2Pre");
	if(checkMax(count[1])){
	 	$("#counts").text(count[0]);
		$("#effs").text(count[1]);
		$("#yct").text(count[2]);
		$("#blacks").text(count[3]);
		$("#sames").text(count[4]);
		$("#keyW").text(count[5]);
		$("#legers").text(count[6]);
		//无效号码路径
		$("#badurl").attr("value",count[7]);
		//无效号码
		$("#alleff").text(count[0]-count[1]);
		//如果无效号码为零则不显示详情下载的链接
		if(count[0]-count[1] == 0)
		{
		    $("#preinfonum").attr("style","display:none");
	    }else{
	    	$("#preinfonum").attr("style","display:block");
	    }
		//余额
		$("#ct").text(count[8]);
		//alert(count[8]);
		var isCharg = count[9];
		var spFeeResult = count[10];
		
		//sp账号计费类型
		var spChargetype = count[11];
		//sp账号余额
		$("#spf").text(count[12]);
		
		if(spFeeResult.indexOf("lessgwfee") == 0)
		{
			altstr = "<font color='red'>"+getJsLocaleMessage('ydcw','ydcw_alert_22')+spFeeResult.split("=")[1]+"</font>";
			$("#messages1").empty();
 			$("#messages1").html(altstr);
 		     $("#messages1").show(); 
 		     $("#subSend").attr("disabled","disabled");
 		   // return;
		}else if(spFeeResult=="feefail" || spFeeResult=="nogwfee")
		{
			altstr = "<font color='red'>"+getJsLocaleMessage('ydcw','ydcw_alert_23')+"</font>";
			$("#messages1").empty();
 			$("#messages1").html(altstr);
 		     $("#messages1").show(); 
 		     $("#subSend").attr("disabled","disabled");
 		    // return;
		}else{
			var ct = $.trim($("#ct").text());
	 		 var yct =$.trim($("#yct").text());
	 		 if(isCharg == "true")
	 		 {
		 		 if(eval(ct)<eval(yct))
		 		 {
		 		     $("#messages1").empty();
		 			 $("#messages1").html("<font color='red'>"+getJsLocaleMessage('ydcw','ydcw_alert_24')+"</font>");
		 		     $("#messages1").show(); 
		 		     $("#subSend").attr("disabled","disabled");
		 		 }				 		 
		 		 else
		 		 {
		 		    $("#messages1").hide(); 
		 		 }
		 		 $("#showyct").show();
	 		 }else{
	 		     $("#showyct").hide();
	 		     $("#messages1").hide(); 
	 		 }
	 		 
	 		/********** sp账号余额检查****************/
				//提示信息
				var tips = "";
				//预付费
				if(spChargetype=="1"){
					var spf =$.trim($("#spf").text());
					//余额大于0
					if(eval(spf)<eval(yct)){
						tips = getJsLocaleMessage('ydcw','ydcw_alert_25');
						$("#subSend").attr("disabled","disabled");
					}
					$("#showspf").show();
				}else if(spChargetype=="2")
				{
					//后付费账号，不作处理
					$("#showspf").hide();
				}else{
					tips = getJsLocaleMessage('ydcw','ydcw_alert_26');
					 $("#subSend").attr("disabled","disabled");
				}
				if(tips!=""){
					$("#messages1").html(tips);
					$("#messages1").show(); 
				}
		}
	}
	
	//$("#newtable").append("<tr align='center'><td style='border: 1px  solid; height: 25px;'>编号</td><td style='border: 1px  solid; height: 25px;'>手机号码</td><td style='border: 1px  solid; height: 25px;'>短信内容</td></tr>");
	if(msg != ""){ 
		for ( var x = 0; x < msg.length; x++) {
			var msgArr=msg[x].split("cw1Pre");
			$("#newtable").append("<tr align ='center'><td style='width:40px;'>"+(x+1)+"</td><td style='width:110px;'>"+msgArr[0]+"</td><td style='width:320px;'><div style='background: transparent; width:320px;word-break:break-all;text-align: left;'><xmp>"+msgArr[1].replaceAll(">","&gt;").replaceAll("<","&lt;")+"</xmp></div></td></tr>");
		}
	} else {
		$("#newtable").append("<tr align ='center'><td style='height: 25px;' colspan='3'>"+getJsLocaleMessage('ydcw','ydcw_alert_27')+"</td></tr>");
	}
	$('#preview').dialog('open');
	$("#subSend").hide();
	$("#subSend").show();
	deleteleftline1();
	$('#preview ul li').css('font-size','13px');
	$('#preview ul li').css('margin','5px 5px 5px');
	showSelect();

}	
//详情下载
function uploadbadFiles(){
	var path = $("#path").val();
    var badurl = $("#badurl").val();
    badurl = badurl.replace(".txt","_bad.txt");
   	$.post("ycw_electronicPayroll.htm?method=goToFile", {
		url : badurl
	},
		function(result) {
			if (result == "true") {
				download_href(path+"/doExport.hts?u="+badurl);				
			} else if (result == "false")
				alert(getJsLocaleMessage('ydcw','ydcw_alert_28'));
			else
				alert(getJsLocaleMessage('ydcw','ydcw_alert_29'));
	});
}
function btsend(){
	$("#subSend").attr("disabled",true);
	$("#cancelSend").attr("disabled",true);
	showSelect();
	var bus = "";	
	var input_method = "filePreview";
	var manual_method = "manualPreview";
	if($("#buscode").val() == "MF0001"){//工资单
		bus = "ycw_electronicPayroll.htm";
	}
	if($("#buscode").val() == "MF0002"){//报销
		bus = "ycw_reimbursementRemind.htm";
	}
	if($("#buscode").val() == "MF0003"){//回款
		bus = "ycw_backFundsNotice.htm";
	}
    if($("form[name='ydcwForm']").attr("encoding")){
		$("form[name='ydcwForm']").attr("encoding","application/x-www-form-urlencoded");
	}else{
		$("form[name='ydcwForm']").attr("enctype","application/x-www-form-urlencoded");
	}
    $("form[name='ydcwForm']").attr("target","_self");
	//$("#form3").attr('action', bus+"?method="+$("#sendtype").val());
    $("form[name='ydcwForm']").attr("action",bus+"?method=send");
	$("form[name='ydcwForm']").submit();
}

function btcancel(){
	showSelect();
    $("#preview").dialog("close");
}

function checkError()
{
	if($("#error").val()=="0")
	{
		//window.clearInterval(dd);
		$("#probar").dialog("close");
		alert(getJsLocaleMessage('ydcw','ydcw_alert_30'));
		$("#subSend").attr("disabled",false);
        $("#qingkong").attr("disabled",false);
	}
}
		
function checkMax(permitMax){
	var num=parseInt(permitMax);
	if(num>500000){
	    parent.$("#counts").text();
		parent.$("#effs").text();
		parent.$("#blacks").text();
		parent.$("#legers").text();
		parent.$("#sames").text();
		parent.$("#keyW").text();						  
		alert(getJsLocaleMessage('ydcw','ydcw_alert_31'));
		parent.$("#subSend").attr("disabled",false);
        parent.$("#qingkong").attr("disabled",false);
		parent.$("#preview").dialog("close");	
		return false;
	}
	return true;
}
		
function checkMatch(match){	
	if(match=="false"){
		parent.$("#subSend").attr("disabled",false);
        parent.$("#qingkong").attr("disabled",false);
		alert(getJsLocaleMessage('ydcw','ydcw_alert_32'));
		parent.$("#detail_Info").dialog("close");	
		return false;
	}
	return true;
} 
		
/*****************************************************************************************************************************
	创建手工录入DIV显示层表格
******************************************************************************************************************************/


//获取文本输入框中的光标前面的内容
function selectInputContent(inputId){
	var obj=document.getElementById(inputId);     
  	var rngSel = document.selection.createRange();//建立选择域
  	var rngTxt = obj.createTextRange();//建立文本域
    var flag = rngSel.getBookmark();//用选择域建立书签
	rngTxt.collapse();//瓦解文本域到开始位,以便使标志位移动
	rngTxt.moveToBookmark(flag);//使文本域移动到书签位
	rngTxt.moveStart("character",-obj.value.length);
  	//o.select();//获取选择的文本内容     
    str = rngTxt.text.replace(/\r\n/g,'');//替换回车换行符
    return(str.length);//返回文本域文本长度
}
		
//回显上一次有效参数记录行
function backDisplayData($paraValue,textRow,textCell){
	var pArr = $paraValue.split("&");//获取参数数组
	var len = 0;
	textRow = textRow - 1;//减去'标题'
	for(var i=textRow-1;i>=0;i--){
		for(var j=textCell-1;j>=0;j--){ 
			if(pArr.length > len){
				$("#para"+i+"_"+j).attr("value",pArr[len]);
				var paramval = getJsLocaleMessage('ydcw','ydcw_alert_33');
				if($("#para"+i+"_"+j).val()!=paramval){
					$("#para"+i+"_"+j).css("color","#000");
				}
				
				len++;
			}
		}
  		}
}
		
//将行列的参数值组合成字符串   textRow:行,textCell:列
function combParaValueString(textRow,textCell){ 
	var paras02 = ""; //一行字符串
	var paras03 = ""; //组合字符串
	var row = textRow-1;//减去标题行,eg.10,9,8..
	var cell = textCell-1;//1,0
	var bool02 = 0;
	var isBreak = false;
	var haveDate = 0;//实际输入行
	var rightDate = 0;//记忆输入行
	var isEditRows = 0;//没有编辑的行
	
	
	var r = row-1;
	for(var i = r ;i >= 0;i--){
		//alert("第"+(i+1)+"行")
	 	paras02 = "";//每行开始初始化为""
	 	haveDate++;//实际输入行
	 	var isEditCell = 0;
	 	var isNull = 0;
	 	for(var j = cell;j >= 0;j--){ 
	 		//alert("第"+j+"列");
	 	 	var id = "para"+i+"_"+j;
	 	 	var paras = $("#"+id).val();
	 	 	var phoneNum = getJsLocaleMessage('ydcw','ydcw_alert_34');
	 	 	var paramval = getJsLocaleMessage('ydcw','ydcw_alert_33');
	 	 	//检查是否有输入
	 	 	if(j == cell){
	 	 		if(paras == phoneNum || paras == ""){
	 	 			//alert("mobile is null");
	 	 			isNull++;
	 	 		}
	 	 	} else {
	 	 		if(paras == paramval || paras == ""){
	 	 			//alert("para. is null");
	 	 			isNull++;
	 	 		}
	 	 	}
	 	 	
	 	 	if(isNull == textCell){
	 	 		//alert("第"+(i+1)+"行，正行为空.");
	 	 		haveDate--;//空行不计算实际输入
	 	 		isEditCell = isNull;
	 	 		continue;
	 	 	}
	 	 	
	 	 	//para. is null and display style
	 	 	if(j == 0){
	 	 		if(isNull != textCell){
	 	 			var $p_cell_obj = $("#para"+i+"_"+cell);
		 	 		if(!isMobel($p_cell_obj.val())){
		 	 			$p_cell_obj.css("background","#fcc").focus();
		 	 			if($p_cell_obj.val()==phoneNum){
		 	 				$p_cell_obj.val("");
		 	 			}
		 	 			isBreak = true;
			 	 		break;
		 	 		} 
		 	 		
		 	 		for(var p = (cell-1); p >= 0; p--){
		 	 			var $p_cells = $("#para"+i+"_"+p);
		 	 			//checkParas($p_cells.val() 
		 	 			if(checkParas($.trim($p_cells.val())) || $.trim($p_cells.val()) == "" || $p_cells.css("color") == '#e0e0e0' ){
		 	 				//alert("参数值输入不正确，请重新输入！");
		 	 				$p_cells.css("background","#fcc").focus();
		 	 				/*if($p_cells.val()=="参数值"){
		 	 					$p_cells.val("");
		 	 				}*/
		 	 				isBreak = true;
			 	 			break;
		 	 			} 
		 	 		}
		 	 		if(isBreak){
		 	 			break;
		 	 		}
	 	 		}
	 	 	}
	 	 	
	 	 	//Memory data
	 	 	if (j == cell) { 			 	 		 
 	 			paras02 += paras + "&";
	 	 	} else {  
	 	 		if (j == 0){
	 	 			paras02 += paras;
	 	 		} else {
	 	 			paras02 += paras + "&" ;
	 	 		}
	 	 	}
	 	}//end for cell
	 	 
	 	// alert("有 "+isEditCell+ "列为空");
	 	//检查列的编辑
 	 	if(isEditCell == textCell){
 	 		isEditRows++;
 	 		continue;
 	 	} 
 	 	
 	 	//跳出循环
 	 	if(isBreak){
	 		break;
	 	} 
	 	
		//正常输入的值将组合成字符串
		if (bool02 == 0) {
			paras03 = paras02;
			bool02++;
		} else {
			paras03 += "&" + paras02;
		}
		
		//记录实际上成功保存记忆的数据
		rightDate++;
		
	}// end for row
	
	//检查行的编辑
 	if(isEditRows == row){
 		if((typeof $("#modify").dialog("isOpen")=='object') || $('#modify').dialog('isOpen')){
 	 		if(confirm(getJsLocaleMessage('ydcw','ydcw_alert_35'))){
 	 			$("#modify").dialog("close");
 	 		}
 		}
 		return false;
 	}
	 
	//alert("实际输入了"+haveDate+"行！保存了 "+rightDate+" 行！");
	//alert("记录："+paras03);
	if(haveDate != rightDate){
		return false;
	}
	return paras03;
}
		
//保存数据
function save(){
	$("#handInput").css("display","");
	var $textRow = $("#textRow").val();
    var $textCell = $("#textCell").val(); 
	//组合参数值为字符串
	var paras01 = combParaValueString($textRow,$textCell);
	if(paras01 == false){
		return false;
	}
	var haveValues = paras01.split("&");
	var rows = haveValues.length/$textCell;//计算实际录入数据行
	$("#paraValue").attr("value",paras01);
	//显示录入几条数据
	$("#paraNumber").html(rows);
	$("#pNumer").attr("value",rows);
	
	if((typeof $("#modify").dialog("isOpen")=='object') || $('#modify').dialog('isOpen')){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_36'));
 	 	$("#modify").dialog("close");
 	}
}

//添加新行
function addRow(){
	var divt = document.getElementById("tableDiv"); 
    var tablet = document.createElement("table"); 
    //tablet.setAttribute("id",id);    
    tablet.setAttribute("className","TableLine");//设定样式    
    tablet.setAttribute("width",'98%');    
    tablet.setAttribute("cellpadding",'3');    
    tablet.setAttribute("cellspacing",'0');    
    
    
 	var textCell = document.getElementById("textCell").value;
 	var num = 0; 
 	var rowid = document.getElementById("rowid").value;
 	tablet.border=1;   
 	
 	for(var i=0;i<rowid;i++){  
		var row = tablet.insertRow(num); 
	 	row.style.setAttribute("backgroundColor","#e0e0e0");    
	 	for(var j=0;j<textCell;j++){  
	 		var cell = row.insertCell(num);
	 		cell.innerHTML="<input type='text' />";
		    cell.width=150; 
		    cell.height = 20;
		    cell.setAttribute("className","border:1px solid #9BC2E0;");    
       			cell.setAttribute("align","center");
	 	}//end for
	 	
 	}//end for
 	
    num = num + 1;
    document.getElementById("num").value = num;
    
    divt.appendChild(tablet); 
} 
 
 //手工录入层清空数据
 function cleardb(){ 
 	var $textRow = $("#textRow").val();
  	var $textCell = $("#textCell").val();
  	var row = $textRow-1;
  	var $obj = $("#para"+(row-1)+"_"+($textCell-1));
  	
  	var phoneNum = getJsLocaleMessage('ydcw','ydcw_alert_34');
	var paramval = getJsLocaleMessage('ydcw','ydcw_alert_33');
	if(confirm(getJsLocaleMessage('ydcw','ydcw_alert_37'))){
	 	for(var i=0;i < row;i++){  
		 	for(var j=0;j < $textCell;j++){ 
		 		var id = "para"+i+"_"+j;
		 		var $p = $("#"+id);
		 		if($p.val() != "" && $p.val()!=phoneNum && $p.val()!=paramval){
		 			$p.attr("value","");
		 			if(j==($textCell-1)){
						$p.css("color","#E0E0E0").css("background","#ffffff").val(phoneNum);			 				
		 			} else {
		 				$p.css("color","#E0E0E0").val(paramval);
		 			}
		 		}
		    }
		}
		//显示为0 , 数据清空
		$("#paraValue").attr("value","");
		$obj.focus();
		$("#paraNumber").html("0");
	}else{
		$obj.focus();
		return false;	
	}
	
 }

 function getGateNumber(){
	var circleSubNo = $("#circleSubNo").val();
	var isReply = $("input:radio[name='reply']:checked").attr("value");
    var spUser = $("#spUser").val();
    var taskId = $("#taskId").val();
    if(spUser == null || spUser == ""){
    	return ;
    }
    var lguserid = $("#lguserid").val();
    $("#subButt").attr("disabled",true); 
	$.ajax({
		async:false,
		type:"post",
		url:"ycw_electronicPayroll.htm", 
		data:"method=getSubNo&spUser="+spUser+"&isReply="+isReply+"&circleSubNo="+circleSubNo+"&taskId="+taskId+"&lguserid="+lguserid,
		success: function(result) {
			if(result == "noSubNo"){
				alert(getJsLocaleMessage('ydcw','ydcw_alert_38'));
				$("#subno").html("");
				$("#subNo").val("");
				$("#subButt").attr("disabled",false);
				return ;
			}else if(result == "error"){
				alert(getJsLocaleMessage('ydcw','ydcw_alert_39'));
				$("#subno").html("");
				$("#subNo").val("");
				$("#subButt").attr("disabled",false);
				return ;
			}else if(result == "noUsedSubNo"){
				alert(getJsLocaleMessage('ydcw','ydcw_alert_40'));
				$("#subno").html("");
				$("#subNo").val("");
				$("#subButt").attr("disabled",false);
				return ;
			}
			if(isReply != 0){
		    	$("#curSubNo").css("display","inline");
		    }else{
		    	$("#curSubNo").css("display","none");
		    }
			var data = eval("("+result+")");
			$("#subNo").val(data.subNo);
			$("#subno").html(data.subNo);
			var sendFlag = (data.sendFlag).split("&");
			if(sendFlag[0] == "false" || sendFlag[1] == "false" || sendFlag[2] == "false"){
				$("#subno").css("color","red");
			}else{
				$("#subno").css("color","blue");
			}
			$("#sendFlag").val(data.sendFlag);
			$("#subNo").val(data.subNo);
			if(isReply == 1){
				$("#circleSubNo").val(data.subNo);
			}
			$("#subButt").attr("disabled",false);
		}
	});
				
}
//参数过滤特殊字符
function checkParas(str){
	pattern = /[\&]/;
	//pattern = /[\&]/;//除&之外其他所有的都包含
	// /[^\u4e00-\u9fa5\d\%$￥&a-zA-Z]/;//特殊字符中只能包含%$￥
	// /^[a-z\d\u4E00-\u9FA5]+$/i; //不包含特殊字符
	return pattern.test(str);
}
	
//手机号码验证
function isMobel(value){
	//if(/^13\d{9}$/g.test(value)||(/^15[0-35-9]\d{8}$/g.test(value))|| (/^18[05-9]\d{8}$/g.test(value))){
	reg = /^0?1\d{10}$/;
	return reg.test(value);
}

//显示提示信息
function showMsgElec(){
	getLoginInfo("#loginUser");
	getGateNumber();
	var result = $("#repMsg").val();
	var sendForReturnStr = $("#sendForReturnStr").val();
	if(result != ""){
		getCt();//更新短信余额
		if(result == "000"){
			$("#message").dialog({width:300,height:180});
			$("#message").dialog("open");
		}
		else{
			if(result.indexOf("empex") == 0){
				result = result.substr(5);
			}else if(result.indexOf("MW") == 0){
				result = getJsLocaleMessage('ydcw','ydcw_alert_41')+result;
			}
	
			alert(result);
			$("#repMsg").attr("value","");
		}
		//window.location.href="ycw_electronicPayroll.htm"
	}
}
//显示提示信息
function showMsgBack(){
	getLoginInfo("#loginUser");
	getGateNumber();
	var result = $("#repMsg").val();
	if(result != ""){
		getCt();//更新短信余额
		if(result == "000"){
			$("#message").dialog({width:300,height:180});
			$("#message").dialog("open");
		}
		else{
			if(result.indexOf("empex") == 0){
				result = result.substr(5);
			}else if(result.indexOf("MW") == 0){
				result = getJsLocaleMessage('ydcw','ydcw_alert_42')+result;
			}
			alert(result);
			$("#repMsg").attr("value","");
			//window.location.href="ycw_backFundsNotice.htm"
		}
	}
}
//显示提示信息
function showMsgReim(){
	getLoginInfo("#loginUser");
	getGateNumber();
	var result = $("#repMsg").val();
	if(result != ""){
		getCt();//更新短信余额
		if(result == "000"){
			$("#message").dialog({width:300,height:180});
			$("#message").dialog("open");
		}
		else{
			if(result.indexOf("empex") == 0){
				result = result.substr(5);
			}else if(result.indexOf("MW") == 0){
				result = getJsLocaleMessage('ydcw','ydcw_alert_41')+result;
			}
			alert(result);
			$("#repMsg").attr("value","");
			//window.location.href="ycw_reimbursementRemind.htm"
		}
	}
}
//判断时间是否合法
function compareDate(deterTime){
	var myDate = new Date();
									
	var year = myDate.getFullYear();        //完整年份
	var month = myDate.getMonth();          //月份					 
	var date = myDate.getDate()	            //日期	
	var time = myDate.toLocaleTimeString(); //时间
	time = time.substring(0, time.lastIndexOf(":", time.length));
	var todayDate = year + '-' + (month+1) + '-' + date + ' ' + time ;  
	if(Date.parse(deterTime.replace("-","/"))<Date.parse(todayDate.replace("-","/"))){
		alert(getJsLocaleMessage('ydcw','ydcw_alert_42'))
		return false;
	}else{
		return true;
	}
}
function initTable(){
    var tablet = document.getElementById("tabletId"); 
   // var tbody = document.createElement("tbody");
    //表格行
    var textRow = $("#textRow").val();
    //表格列
    var textCell = $("#textCell").val(); 
    var width = 160;
    var cellWidth = 130;
    if(textCell == 2){
    	width = 400;
    	cellWidth = 180;
    }else{
	    for(var i=3;i<=21;i++){
	    	if(textCell == i){
	    		width = width+130*(i-1);
	    	}
	    }
    }
    tablet.setAttribute("width",width+"px");
    
    var tempArr = $("#tempArr").val(); 
   
    var tempArr01 = tempArr.split("?");
    //tablet.border=1;        
    var num = 0; 
    var k = textCell - 2;//调整参数序号显示先后顺序
       
    //如果有内容,则将内容回显到表格,不再创建表格
	var $paraValue = $('#paraValue').val();//或者attr('value') 
	//创建表格
    for(var i=0;i<textRow;i++){  
        var row=tablet.insertRow(num); 
        for(var j=0;j<=textCell;j++){ //设置序号列
            var cell=row.insertCell(num);
            row.style.backgroundColor="#f1f1f3";
            if(i == (textRow-1)){
            	//第一行
            	cell.style.fontWeight="Bold";  
            	//cell.setAttribute("height","8px");
            	//cell.style.setAttribute("backgroundColor","#e0ebfe");
            	if($.browser.msie && parseInt($.browser.version)<=7){
            		cell.setAttribute("className","div_bg");
            	}else{
            		cell.setAttribute("class","div_bg");
            	}
            }
            
            if(j==textCell){
            	//第一列
            	cell.setAttribute("height","20px");
            	cell.setAttribute("width","30px");
            }
            
            if(i == (textRow-1) && j==textCell){
            	//第一行第一列
            	cell.innerHTML=getJsLocaleMessage('ydcw','ydcw_alert_43'); 
            }
            
            if(i == (textRow-1) && j==(textCell -1)){
            	//第一行第二列
           		cell.innerHTML=getJsLocaleMessage('ydcw','ydcw_alert_44'); 
            }
            
            if(i!=(textRow-1)){
            	if(j==textCell){
            		cell.innerHTML=textRow-(i+1);//显示序号列
            		cell.setAttribute("width","30px");
            	} /*else {
					cell.innerHTML="<input id='para"+i+"_"+j+"' name='para"+i+"_"+j+"' type='text'/>";	                	
            	}*/
            	else if(j == (textCell -1)){
            		cell.setAttribute("width",cellWidth);
            		cell.innerHTML="<input id='para"+i+"_"+j+"' name='para"+i+"_"+j+"' " +
            				"type='text' maxlength='11' " +
            				"onkeyup='checkPhoneIput(this)'" +
            				"style='border:1px solid #ccd5dc;width:"+parseInt(cellWidth-20)+"px;height:18px;margin-bottom: 7px;' />";	
    			} else {
    				cell.setAttribute("width",cellWidth);
    				cell.innerHTML="<input id='para"+i+"_"+j+"' name='para"+i+"_"+j+"' type='text'" +
    						"style='border:1px solid #ccd5dc;width:"+parseInt(cellWidth-20)+"px;height:18px;margin-bottom: 7px;' />";	
    			}
            }
            
            if(i == (textRow-1) && j!=(textCell-1) && j!=textCell){
            	cell.innerHTML=getJsLocaleMessage('ydcw','ydcw_alert_3')+tempArr01[k];
            	k--;
            }
            
       } //end for cell
    } //end for row
    $("#num").attr("value",(num+1));
        
    //add style in cell
    for(var i=textRow-2;i>=0;i--){
		for(var j=textCell-1;j>=0;j--){ 
			var $inputRow = $("#para"+i+"_"+j);
			if(j == (textCell -1)){
               	$inputRow.css("color","#E0E0E0").attr("value",getJsLocaleMessage('ydcw','ydcw_alert_34'));
			} else {
				$inputRow.css("color","#E0E0E0").attr("value",getJsLocaleMessage('ydcw','ydcw_alert_33'));
			}
			if((i==(textRow-2)) && (j==(textCell-1))){
				$inputRow.focus();
			}
		}
   	}
   		
  	//When clicked on display style
  	$("input:text'").click(function(){ 
  		if($(this).attr("id").substring(0,4) != "para"){
  			return;
  		}
  		if ($(this).val() == getJsLocaleMessage('ydcw','ydcw_alert_34')){
			$(this).css("color","").val("");
			$(this).css("borderColor","#62728b"); 
		}
		
  		if ($(this).css("color") == "#e0e0e0"){
			$(this).css("color","").val("");
			$(this).css("borderColor","#62728b"); 
		} 
  		
	}).blur(function(){
		var $inputId = $(this).attr("id"); 
		var inputArr = $inputId.split("_");
		$(this).css("borderColor","#ccd5dc"); 
		if ($inputId.substring(0,4) != "para"){
			return;
		}
		if ($(this).val() == ""){
			$(this).css("background","#ffffff");
			if (inputArr[1] == (textCell-1)){
				$(this).css("color","#E0E0E0").val(getJsLocaleMessage('ydcw','ydcw_alert_34'));
			} else {
				$(this).css("color","#E0E0E0").val(getJsLocaleMessage('ydcw','ydcw_alert_33'));
			}
		} else {
			if (inputArr[1] == (textCell-1)){
				if ($(this).val() != getJsLocaleMessage('ydcw','ydcw_alert_34') && !isMobel($(this).val())){//验证手机号码
					$(this).css("background","#fce1d8");//css("border","1px solid #888")
					//$(this).addClass("redStyle");
					return;
				} 
			} else {  
				if($(this).val() != getJsLocaleMessage('ydcw','ydcw_alert_33') && checkParas($(this).val())){
					$(this).css("background","#fce1d8");
					//$(this).addClass("redStyle");
					return;
				} 
			}
			//$(this).addClass("whiteStyle");
			$(this).css("background","#FFFFFF");
			
			/*var paraId = $(this).attr("id");
			alert(paraId.indexOf("_"));
			var paraRow = paraId.substring(4,paraId.indexOf("_"));
			alert(paraRow);*/
			/*if($(this).find('input[class="redStyle"]')){
				//alert("ddd"+$(this).find('input[class="redStyle"]'));
				$(this).removeClass('redStyle');
			} 
			if($(this).val() != "请输入手机号码" && $(this).val() != "请输入参数值"){
				$(this).addClass("transStyle");//css("border","0px").css("background","#d9f0ff");
			}*/
		}
	}).bind('keydown',function(e){
		var key = e.which;
		//alert("按键值："+key);
		
		var $inputId = $(this).attr("id"); 
		var inputArr = $inputId.split("_");
		
		if ($inputId.substring(0,4) != "para"){
			return;
		}
		
		var arr = new Array();
		arr[0] = inputArr[0];
		arr[1] = inputArr[1];
		var p_rows = 0;
		p_rows = arr[0].substring(4);
		
        if (key == 13 || key == 9) {
           	e.preventDefault();
			if(arr[1] == 0){
				//last cell and brack first row
				if(p_rows == 0){
					$("#para"+(textRow-2)+"_"+(textCell-1)).focus();
				}
				p_rows = Number(p_rows)-1;
				var $newId = "para"+p_rows + "_"+(textCell-1);
				$("#"+$newId).focus();
			} else {
				var $newId = inputArr[0]+"_"+(inputArr[1]-1);
				$("#"+$newId).focus();
			}  
        }
        
        if (key == 37){//向左
        	if(selectInputContent($inputId) == 0){
        		//如果文本有字符不能直接跳到下个文本框的焦点处
        		$("#"+arr[0]+"_"+(Number(arr[1])+1)).focus();
        	}
        }
        if (key == 38){//向上
        	$("#para"+(Number(p_rows)+1)+"_"+arr[1]).focus();
        }
      	if (key == 39){//向右
      		if(($("#"+$inputId).val().length-selectInputContent($inputId)) == 0){
        		$("#"+arr[0]+"_"+(Number(arr[1])-1)).focus();
        	}
        	if (p_rows == 0 && arr[1] == 0){
        		$("#para"+(textRow-2)+"_"+(textCell-1)).focus();
        	}
        }
        if (key == 40){//向下
        	$("#para"+(Number(p_rows)-1)+"_"+arr[1]).focus();
        	if (p_rows == 0 && arr[1] == 0){
        		$("#para"+(textRow-2)+"_"+(textCell-1)).focus();
        	}
        }
            
	}).focus(function(){
		if($(this).attr("id").substring(0,4) != "para"){
  			return;
  		}
		if ($(this).val() == getJsLocaleMessage('ydcw','ydcw_alert_34')){
			$(this).css("color","").val("");
			$(this).css("borderColor","#62728b");
		}
		
		if ($(this).css("color") == "#e0e0e0" || $(this).val() == getJsLocaleMessage('ydcw','ydcw_alert_33')){
			$(this).css("color","").val("");
			$(this).css("borderColor","#62728b");
		} 
		//如果第一个文本框(id值为最后一个)得到焦点,则滚动条移至最顶端和最左端
		if ($(this).attr("id") == ("para"+(textRow-2)+"_"+(textCell-1))){
			$("#modify").attr('scrollLeft','0');
			$("#modify").attr('scrollTop','0');
			//$("#modify").scroll(function(){
		    //}); 
		}
	});
   		  
	//如果存在值,则直接复制
	if($paraValue != ""){
		backDisplayData($paraValue,textRow,textCell);
	} else {
		$("#para"+(textRow-2)+"_"+(textCell-1)).focus();
	}
		
	//重设宽度,解决IE中不出现滚动条的状况.
	$('#modify').css('width',450);
} 

function deleteFile(){
	var fileobj = document.getElementById("numFile");
	fileobj.outerHTML = fileobj.outerHTML; 
	$("#addFile").css("display","none");
	$("#subType").val("0");
}
function handInput(){
	//$("#handInput").css("display","");
	modify($(this));
	$("#addFile").css("display","none");
	$("#subType").val("2");
}
function deleteInput(){
	if(!$("#handInput").is(":hidden") &&  confirm(getJsLocaleMessage('ydcw','ydcw_alert_45'))) { 
		$("#paraValue").val("");
		$("#paraNumber").html("0");
		$("#handInput").css("display","none");
		$("#subType").val("0");
	}
}
//验证上传文件格式
function checkFile() {
	var fileName = $("#numFile").val();
	if (fileName != "") {
		var index = fileName.lastIndexOf(".");
		var fileType = fileName.substring(index + 1).toLowerCase();
		if (fileType != "txt" && fileType !="xls" && fileType !="xlsx") {   
			alert(getJsLocaleMessage('ydcw','ydcw_alert_46'));
			return false;
		} else {
			return true;
		}		
	} else {
		alert(getJsLocaleMessage('ydcw','ydcw_alert_47'));
		return false;
	}
}
//浏览按纽事件
function upFile(){
	$("#paraValue").val("");
	$("#paraNumber").html("0");
	$("#handInput").css("display","none");
	$("#subType").val("0");
	if(checkFile("numFile")){  
		//$("#addFile").css("display","");
	$("#handInput").css("display","none");
	$("#subType").val("1");
	var pathValue = $("#numFile").val();
	var index = pathValue.lastIndexOf("\\");
	var name = pathValue.substring(index + 1);
	/*if(name.length >12){
	   name = name.substring(0,12)+"....";
	}*/
	var imgPath = $("#imgPath").val();
	var ipathUrl = $("#ipathUrl").val();
	var fileType=getFileType(name),icon;
 	 if(fileType=='txt'){
 	 	icon='txt.gif';
 	 }else if(fileType=='xls' || fileType=='xlsx' || fileType=='et'){
 	 	icon='xls.gif';
 	 }else{
 	 	icon='fileimg.png';
 	 }
 	 //截取固定长度
 	 var text = widthText(name,410);
 	 if(text != name){
 		 text = '...'+text;
 	 }
     $("#upTipDiv").html("<div id='tr' style='margin-bottom:0px;height:35px;line-height:35px;'><a style='background : url("+ipathUrl+"/img/"+icon+") no-repeat left center;margin-left:5px;padding: 2px 0;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;<label>"+text+"</label>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:delfile()' style='text-decoration: none;color: #2970c0;'>"+getJsLocaleMessage('ydcw','ydcw_alert_51')+"</a></div>");  		     
     $("#upTipDiv").show();   
     $("#subType").val("1");     			 
	}
}
function widthText(str,width){
	var str1 = escapelt(str);
	var label = $('<label>'+str1+'</label>');
	$('body').append(label);
	var w = label[0].offsetWidth;
	label.remove();
	if(w>width){
		str = str.substr(1);
		return widthText(str,width);
	}else{
		return str;
	}
}
//显示日历
function showWdate(id){
	WdatePicker({el:id,skin:'simple',minDate:'%y-%M-%d %H:%m',dateFmt:'yyyy-MM-dd HH:mm',readOnly:true});
}

//获取文件路径只兼容IE
function getUploadFilePath(){
	var isIE = (document.all) ? true : false;
    var isIE7 = isIE && (navigator.userAgent.indexOf('MSIE 7.0') != -1);
    var isIE8 = isIE && (navigator.userAgent.indexOf('MSIE 8.0') != -1);
    var file=document.getElementById("numFile");
    var path = "";
    if(isIE7 || isIE8){
        file.select();
        path = document.selection.createRange().text;
        document.selection.empty();
    }
    //alert("文件完整路径是：" + path);
    return path;
}
//定义replaceAll方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
//防止数据库网络断开
function fresh()
{
	$.ajax({
		url:"LoadingServlet.htm",
		type:"post",
		dataType:"script",
		success:function(result){
			if( result !="true")
			{
				if(errorNum==0)
				{
					errorNum==1;
					alert(getJsLocaleMessage('ydcw','ydcw_alert_4'));
					window.clearInterval(dd);
					window.location.href=bus+'?method=find&lgguid='+$('#lgguid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
				}
			}
		},
		error:function(xrq,textStatus,errorThrown){
			if(errorNum==0)
			{
				errorNum=1;
				alert(getJsLocaleMessage('ydcw','ydcw_alert_4'));
			}
			window.clearInterval(dd);
			window.location.href=bus+'?method=find&lgguid='+$('#lgguid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
		}
	});
}

//手机号输入框输入过滤
function checkPhoneIput(str)
{
	if(str.value!=str.value.replace(/\D/g,''))
	{
		str.value=str.value.replace(/\D/g,'');
	}
}

//手机号输入框输入过滤（剪贴过滤）
function checkPhoneOnPase()
{
	return !clipboardData.getData('text').match(/\D/);
}

//发送成功跳转群发任务查看界面
 function sendRecord(menuCode, taskid, lguserid, lgcorpcode)
 {
	closemessage();
	window.parent.openNewTab(menuCode,base_path+"/ycw_sendTask.htm?method=find&taskid="+taskid+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&pageIndex="+1+"&pageSize="+15);
 }
 function closemessage()
 {
	 $("#message").dialog("close");
 }
 
 function setDefault()
{
	if(confirm(getJsLocaleMessage('ydcw','ydcw_alert_48'))) {
		var lguserid = $('#lguserid').val();
		var lgcorpcode = $('#lgcorpcode').val();
		var busCode = $("#busCode").val();
		var spUser = $("#spUser").val();
		var isReply = $("input:radio[name='reply']:checked").attr("value");
		$.post("ycw_electronicPayroll.htm?method=setDefault", {
			lguserid: lguserid,
			lgcorpcode: lgcorpcode,
			busCode: busCode,
			spUser: spUser,
			isReply: isReply,
			flag: "4"			
			}, function(result) {
			if (result == "seccuss") {
				alert(getJsLocaleMessage('ydcw','ydcw_alert_49'));
				return;
			} 
			else if(result == "fail"){
				alert(getJsLocaleMessage('ydcw','ydcw_alert_50'));
				return;
			}
		});
	}
}