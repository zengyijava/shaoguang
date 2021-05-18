function cut(){
	var content = $.trim($('#tmMsg').val());
	var huiche = content.length - content.replaceAll("\n", "").length;
	if (content.length + huiche > 990) {
		$("#tmMsg").val(content.substring(0, 990 - huiche));
	}
	//len(ob);
}



var tm;
/*短信模板添加修改js*/
$(function() {
	var pathUrl = $("#pathUrl").val();
	var index=1;
	var url="";
	//$('#dsflag').trigger("change");
	$('#subBut').click(function() {
		$('#subBut').attr("disabled",true);
		var tmName = $.trim($('#tmName').val());
		var tmCode = $.trim($('#tmCode').val());
		var hiddenCode=$.trim($('#hiddenCode').val());
		var tmMsg = getContentVal($.trim($('#tmMsg').val()));
		var pattern=/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;  
		var selectnum=$("#trSmsTmpType  option:selected").val();//取得选中是下拉框的值 may add
		//判断字母或数字
		var numOrLetters = /^[0-9a-zA-Z]+$/;
		if (tmName == "") {
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_5"));
			$('#subBut').attr("disabled",false);
			return;
		}
		var dsflag = $("#dsflag").val();
		if (dsflag!='4'&&tmCode == "") {
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_6"));
			$('#subBut').attr("disabled",false);
			return;
		}
		// else if(escape(tmCode).indexOf("%u")!=-1){
		// 	alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_7"));
		// 	$('#subBut').attr("disabled",false);
		// 	return;
		// }else if(pattern.test(tmCode)){
		// 	alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_8"));
		// 	$('#subBut').attr("disabled",false);
		// 	return;
		// }
		else if(tmCode.indexOf(' ')>-1){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_9"));
			$('#subBut').attr("disabled",false);
			return;   
		}else if(!numOrLetters.test(tmCode)){
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_69"));
			$('#subBut').attr("disabled",false);
			return;
		}else if (dsflag=='4'&&tmCode != "") {
			var dsf = /^[\d]+$/;
			if(!tmCode.match(dsf)){
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_10"));
				$('#subBut').attr("disabled",false);
				return;
			}
		//	if(tmCode>=1000000000){
		//		alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_11"));
		//		$('#subBut').attr("disabled",false);
		//		return;
		//	}
		}
		
		if (tmMsg == "") {
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_12"));
			$('#subBut').attr("disabled",false);
			return;
		}
		if (tmName.length > 20) {
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_13"));
			$('#subBut').attr("disabled",false);
			return;
		}
		//通用动态模板和智能抓取模板输入格式判断
		if(dsflag == 1 || dsflag == 3)
		{
			
			if(!/#[Pp]_[1-9][0-9]*#+/g.test(tmMsg)){
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_14"));
				$('#subBut').attr("disabled",false);
				return;
			}
		}
		if (dsflag == "3" && tmMsg.length > 320) {
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_15"));
			$("#tmMsg").val($("#tmMsg").val().substring(0,320));
			$('#subBut').attr("disabled",false);
			return;
		}
		
		if (tmMsg.length > 990) {
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_16"));
			$("#tmMsg").val($("#tmMsg").val().substring(0,990));
			$('#subBut').attr("disabled",false);
			return;
		}
		
		var checkName="false";
		var checkURL="";
		if($('#OpType').val()=="add") {
			checkName="true";
			url = pathUrl+"/common.htm?method=checkBadWord1";
			checkURL= pathUrl+"/tem_smsTemplate.htm?method=checkRepeat";
		} else {
			url = "common.htm?method=checkBadWord1";
			checkURL= "tem_smsTemplate.htm?method=checkRepeat";
		}
		var is="false";
		//检查重复
		$.post(checkURL, {
			tmCode:tmCode,
			hiddenCode:hiddenCode,
			corpCode : $("#lgcorpcode").val()
		}, function(message) {
			if (message == "repeat") {
				is="true";
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_17"));
				$('#subBut').attr("disabled",false);
			}else{

				$.post(url, {
				tmName:tmName,
				checkName:checkName,
				tmMsg : tmMsg,
				corpCode : $("#lgcorpcode").val()
			}, function(message) {
				if(message.indexOf("@")==-1){
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_18"));
					$('#subBut').attr("disabled",false);
					return;
				}
				message=message.substr(message.indexOf("@")+1);
				if (message != "") {
					if (message == "error") {
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_19"));
						$('#subBut').attr("disabled",false);
					} else if (message == "errorName") {
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_20"));
						$('#subBut').attr("disabled",false);
					} else
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_21")+"\n     "+message+"\n"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_22"));
						$('#subBut').attr("disabled",false);
					}
				} else {
					$("#tmform").attr("action",$("#tmform").attr("action"));
					document.forms["tmform"].submit();
				}
			});
			
			}
		});
	});
	$("#tmMsg").keydown(function(){
		if($.trim($('#tmMsg').val()).length>=990)
		{
			$(this).val($(this).val().substring(0,990));
		}
	});
	$("#tmMsg").keyup(function(){
		if($.trim($('#tmMsg').val()).length>=990)
		{
			$(this).val($(this).val().substring(0,990));
		}
	});
	$("#checkall").click(function(){ 
					$("input[name='checklist']").attr("checked",$(this).attr("checked")); 
				}); 
	
});

function del(path, i){
	if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_23")))
	{
		var lgcorpcode=$("#lgcorpcode").val();
		var lguserid=$("#lguserid").val();
		
		$.get("tem_smsTemplate.htm?method=delete",{ids:i,lgcorpcode:lgcorpcode},function(result){
			if(result>0)
			{
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_5"));
				//alert(path+"/tem_smsTemplate.htm?lguserid="+lguserid);
				location.href = path+"/tem_smsTemplate.htm?lguserid="+lguserid;
			}else{
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_24"));
				location.href = path+"/tem_smsTemplate.htm?lguserid="+lguserid;
			}
		});
	}
}
function showButton(boo)
{
	if(boo==0)
	{
		//var tmM = $.trim(tm.getContent());
		$('#showEditor').removeClass("hidden");
		//$("#tmMsg").val(tmM);
		$("#showEditor2").addClass("hidden");
		//$("#tmMsg2").val("");
	}else
	{
		var tm1 = $.trim($("#tmMsg").val());
		//alert(tm1)
		$('#showEditor').addClass("hidden");
		$('#showEditor2').removeClass("hidden");
		tm.clearContent();
		tm.setContent(tm1);
		//$("#tmMsg2").val(tm1);
	}
}

function delAll(path){
	var items = "";
	$('input[name="checklist"]:checked').each(function(){	
		items += $(this).val()+",";
	});
	if (items != "")
	{
		items = items.toString().substring(0, items.lastIndexOf(','));
	}
	if(items==null || items.length == 0){
		alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_25"));
		return;
	}else{
	if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_26"))==true){
		var lgcorpcode = $("#lgcorpcode").val();
				$.get("tem_smsTemplate.htm?method=delete",{ids:items,lgcorpcode:lgcorpcode},function(result){
					if(result>0)
					{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_27")+result+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_28"));
						
						var url = path+'/tem_smsTemplate.htm';
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
						location.href=url+conditionUrl;	
						//window.location.href=location;
					}else{
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_24"));
						location.href=url+conditionUrl;	
					}
				});
				
	}
	}
}

function changeMb(i)
{
	if(i==3)
	{
		var isIE=!!window.ActiveXObject; 
		var isIE6=isIE&&!window.XMLHttpRequest; 
		var isIE8=isIE&&!!document.documentMode;
		var isIE7=isIE&&!isIE6&&!isIE8; 		
		
		if (navigator.userAgent.indexOf("MSIE 6.0")>0 || navigator.userAgent.indexOf("MSIE 7.0")>0) {
			$('#editSmstmpTable').css("margin-top","-30px");
			$('#fontFmTip').css("margin-top","30px");
		}	
		
		var is360 = false;  
		var isMIE = false;  
	    if (window.navigator.appName.indexOf("Microsoft") != -1){  
	    	isMIE= true;
		} 
	    
		if(isMIE&&(window.navigator.userProfile+'')=='null'){  
		    is360 = true;
		    $('#editSmstmpTable').css("margin-top","-30px");
			$('#fontFmTip').css("margin-top","30px");
		}  
		
		$("#trSmsTmpType").css("height","100px");
		$("#subModule").css("display","inline");
		$('#tmcodetr').show();
		$('#mbcodems').html("&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_29"));
		$('#mbcodexh').show();
	}else if(i==4){
		var isIE=!!window.ActiveXObject; 
		var isIE6=isIE&&!window.XMLHttpRequest; 
		var isIE8=isIE&&!!document.documentMode;
		var isIE7=isIE&&!isIE6&&!isIE8; 		
		
		if (navigator.userAgent.indexOf("MSIE 6.0")>0 || navigator.userAgent.indexOf("MSIE 7.0")>0) {
			$('#editSmstmpTable').css("margin-top","-30px");
			$('#fontFmTip').css("margin-top","30px");
		}	
		
		var is360 = false;  
		var isMIE = false;  
	    if (window.navigator.appName.indexOf("Microsoft") != -1){  
	    	isMIE= true;
		} 
	    
		if(isMIE&&(window.navigator.userProfile+'')=='null'){  
		    is360 = true;
		    $('#editSmstmpTable').css("margin-top","-30px");
			$('#fontFmTip').css("margin-top","30px");
		}
		if($('#cztype').val()==1){
			$('#tmcodetr').hide();
		}else{
			$('#tmcodetr').show();
		}
		$('#mbcodems').html(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_30"));
		$('#mbcodexh').hide();
		$("#subModule").css("display","none");
		$("#trSmsTmpType").css("height","auto");
		
	}else
	{
		$("#subModule").css("display","none");
		$("#trSmsTmpType").css("height","auto");
		$('#tmcodetr').show();
		$('#mbcodems').html("&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_29"));
		$('#mbcodexh').show();
	}
	if(i==0 || i==2)
	{
		
		$("#fontFmTip,.para_cg,.tit_panel,.showParams").hide();
		var isIE=!!window.ActiveXObject; 
		var isIE6=isIE&&!window.XMLHttpRequest; 
		var isIE8=isIE&&!!document.documentMode;
		var isIE7=isIE&&!isIE6&&!isIE8; 		
		
		if (navigator.userAgent.indexOf("MSIE 6.0")>0 || navigator.userAgent.indexOf("MSIE 7.0")>0) {
			$('#editSmstmpTable').css("margin-top","0px");
			$('#fontFmTip').css("margin-top","0px");			
		}	
		
		var is360 = false;  
		var isMIE = false;  
	    if (window.navigator.appName.indexOf("Microsoft") != -1){  
	    	isMIE= true;
		} 
	    
		if(isMIE&&(window.navigator.userProfile+'')=='null'){  
		    is360 = true;
		    $('#editSmstmpTable').css("margin-top","0px");
			$('#fontFmTip').css("margin-top","0px");
		}  
	} 
	else
	{
		$('.tit_panel,.showParams').show();
		$(".para_cg").show();
		$("#fontFmTip").show();
	}
	
}
//按回车登录
function keydown(e) //支持ie 火狐 键盘按下事件      
{        
	var currKey=0,e=e||event;
       if(e.keyCode==13) return false;      
}

function showOfAdd(result,fromState)
{
	if(result=="true")
	{
		$("#fontFmTip,.para_cg,.all_param").hide();
		alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_31"));
		
		//从发送页面跳过来的
		if("1"==fromState){
			window.parent.closeAddSmsTmpdiv();
		} else if("3"==fromState) {
			//window.parent.closePage();
			
			window.parent.closeSmsTmp();			
		} else{
			window.parent.location.href="tem_smsTemplate.htm?method=find&lguserid="+
			 	$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
		}

	}
	else if(result=="pstrue")
	{
		$("#fontFmTip,.para_cg,.all_param").hide();
		alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_32"));
		
		//从发送页面跳过来的
		if("1"==fromState){
			window.parent.closeAddSmsTmpdiv();
		} else if("3"==fromState) {
			//window.parent.closePage();
			
			window.parent.closeSmsTmp();			
		} else{
			window.parent.location.href="tem_smsTemplate.htm?method=find&lguserid="+
			 	$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
		}

	}
	else if(result=="error")
	{

		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
		window.parent.closeAddSmsTmpdiv()

	}else if( "noFlower"==result)
	{

		alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_33"));
		window.parent.closeAddSmsTmpdiv();
	}
	else if(result != "null" && result != null)
	{
		if(result.indexOf("empex") == 0){
			result = result.substring(5);
		}

		alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_34")+result);
		window.parent.closeAddSmsTmpdiv()
	}
	


}
function showOfEdit(result)
{
	if( result=="true")
	{
		alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_35"));
	}
	else if( result=="error")
	{
		alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
	}
	if (result != "null")
	{
	    window.parent.location.href="tem_smsTemplate.htm?method=find&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
	}
	
}
function switchState(obj) {
	if(obj.value == 1) {
		$("#bizCode").attr("disabled", false);
	} else {
		$("#bizCode").attr("disabled", true);
	}
}

function showAddSmsTmp(showType)
{
	$("#addSmsTmpDiv").css("display","block");
	$("#addSmsTmpFrame").css("display","block");

	$("#addSmsTmpDiv").dialog({
		autoOpen: false,
		height:425,
		width: 700,
		resizable:false,
		modal: true,
		open:function(){
			
		},
		close:function(){
			$("#addSmsTmpDiv").css("display","none");
			$("#addSmsTmpFrame").css("display","none");
		}
	});
	//新增
    $("#addSmsTmpFrame").attr("src","tem_smsTemplate.htm?method=doAdd&lguserid="
    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&showType="+showType+"&time="+new Date().getTime());

	$("#addSmsTmpDiv").dialog("open");
}

function closeAddSmsTmpdiv()
{
	$("#addSmsTmpDiv").dialog("close");
	$("#addSmsTmpFrame").attr("src","");
	
}

function showEditSmsTmp(tmId)
{
	$("#editSmsTmpDiv").css("display","block");
	$("#editSmsTmpFrame").css("display","block");
	//修改
	
	$("#editSmsTmpDiv").dialog({
		autoOpen: false,
		height:400,
		width: 700,
		resizable:false,
		modal: true,
		open:function(){
			
		},
		close:function(){
			$("#editSmsTmpDiv").css("display","none");
			$("#editSmsTmpFrame").css("display","none");
		}
	});
    $("#editSmsTmpFrame").attr("src","tem_smsTemplate.htm?method=doEdit&tmId="+tmId+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val());
          
	$("#editSmsTmpDiv").dialog("open");
}

function closeEditSmsTmpdiv()
{
	$("#editSmsTmpDiv").dialog("close");
	$("#editSmsTmpFrame").attr("src","");
	
}

function checkAlls(e,str)    
	{  
		var a = document.getElementsByName(str);    
		var n = a.length;    
		for (var i=0; i<n; i=i+1)    
			a[i].checked =e.checked;    
	}
function modify(t,state)
{
$('#modify').dialog({
	autoOpen: false,
	resizable: false,
	width:250,
    height:200
});
if(state=="1")
{
	$('#modify').dialog("option","title",getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_37"));
}
else if(state=="2")
{
	$('#modify').dialog("option","title",getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_38"));
}
$("#msg").children("xmp").empty();
$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
$('#modify').dialog('open');
}

function changestate(i)
{
	var ks=$.trim($("#tempState"+i).attr("value"));
	var lgcorpcode =$("#lgcorpcode").val();
	$.post("tem_smsTemplate.htm?method=changeState",{tempId:i,tempState:ks,lgcorpcode:lgcorpcode},function(result){
		
        if (result == "true") {
			$("#tempState"+i).empty();
			if(ks == 1)
			{
			    $("#tempState"+i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_36")+"</option>");
			    $("#tempState"+i).append("<option value='0' >"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_37")+"</option>");
			}
			else
			{
				$("#tempState"+i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_38")+"</option>");
				$("#tempState"+i).append("<option value='1' >"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_39")+"</option>");
			}
			var $select = $("#tempState"+i);
			changeEmpSelect($select,80);
			$select.next().hide();
			$select.prev().html($select.find('option:selected').text());
			$select.prev().show();
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_35"));
		}else{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_36"));
		}		
	});
}

//模板审批详情
function opentmpAudmsg(tmpid){
	 var pathUrl = $("#pathUrl").val();
	 var lguserid = $("#lguserid").val();
	 $.post(pathUrl+"/tem_smsTemplate.htm?method=getSmsTplDetail",{tmpid:tmpid,lguserid:lguserid},function(jsonObject){
		 var json = eval("("+jsonObject+")");
		//是否有 审批记录
		var haveRecord = json.haveRecord;
		var firstshowname = json.firstshowname;
		var firstcondition = json.firstcondition;
		var secondshowname = json.secondshowname;
		var secondcondition = json.secondcondition;
		var isshow = json.isshow;
		$("#recordTable").empty();
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_18")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_39")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_40")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_41")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_42")+"</td></tr>";
		if(haveRecord == "1"){
			//审批记录
			var recordList = json.members;
			 for(var i= 0;i<recordList.length;i++){
					var sms_Rlevel = recordList[i].smsRlevel;
					var sms_Reviname = recordList[i].smsReviname;
					var sms_Exstate = recordList[i].smsexstate;
					var sms_Comments = recordList[i].smsComments;
					var sms_rtime = recordList[i].smsrtime;
					msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+sms_Rlevel+"</td> "
	        		+"  <td align='center'  width='15%'  class='div_bd'>"+sms_Reviname+"</td>"          
			        +"  <td align='center'  width='15%'  class='div_bd'><div>"+sms_Exstate+"</div></td> "
			        +"  <td align='left'  width='35%'  style='word-break: break-all;'  class='div_bd'>";
			        
			        var view_sms_Comments=sms_Comments.length>17?(sms_Comments.substr(0,17)+"..."):sms_Comments;
			        msg=msg+"<a onclick='javascript:modify(this,2)' style='cursor: pointer;'><label style='display:none'><xmp>"+sms_Comments+"</xmp></label>"
					+"<xmp>"+view_sms_Comments+"</xmp></a>"
			        +"</td><td align='center'  width='25%'  class='div_bd'>"+sms_rtime+"</td> </tr>" ;
			}
		}else{
			msg = msg + "<tr class='div_bd' height='24px'> <td  align='center' class='div_bd' colspan='5'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_43")+"</td></tr>"
		}
		$("#recordTable").html(msg);
		$('#recordTableDiv').show();	
		$("#nextrecordmgs").empty();
		if(isshow == "1"){
			var recordmsg = getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_43")+"&nbsp;" + firstshowname;
			if(firstcondition == "1"){
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_48");
			}else if(firstcondition == "2"){
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_49");
			}
			if(secondshowname != "" && secondcondition != ""){
				recordmsg = recordmsg + getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_45")+"&nbsp;" + secondshowname;
				if(secondcondition == "1"){
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_48");
				}else if(secondcondition == "2"){
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_49");
				}
			}
			$("#nextrecordmgs").html(recordmsg);
			$('#nextrecordmgs').show();	
		}
		$("#smsdetailinfo").dialog("open");
	 });
}

//共享模板操作
function showShareTmp(tmId,tmCode, tmName,userId) {
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var iPath = $("#iPath").val();
	tmName=tmName||'-';
//	tmName=encodeURI(tmName);
//	tmName=encodeURI(tmName);
	var url = iPath+"/tem_shareTemplate.jsp?lguserid="+ lguserid + "&lgcorpcode=" + lgcorpcode+"&tempId="+tmId+"&userId="+userId+"&tmCode="+tmCode;
	$("#flowFrame").attr("src", url);
	$("#flowFrame").attr("attrid", tmId);
	$("#flowFrame").attr("tmpname", tmName);
	$("#shareTmpDiv").dialog("open");
}

//修改模板共享
function updateShareTemp() {
	var tempId = $("#flowFrame").attr("attrid");
	var optionSize = $(window.frames['flowFrame'].document).find(
			"#right option").size();
	// 设置的机构IDS
	var depidstr = "";
	// 设置的操作员IDS
	var useridstr = "";
	$(window.frames['flowFrame'].document).find("#right option").each(
			function() {
				var id = $(this).val();
				// 1是机构 2是操作员
				var type = $(this).attr("isdeporuser");
				if (type == "2") {
					useridstr = useridstr + id + ",";
				} else if (type == "1") {
					depidstr = depidstr + id + ",";
				}
			});
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	//短信模板
	var infoType = $("#templType").val();
	$("#updateShareTemp").attr("disabled", true);
	$.post(pathUrl + "/tem_smsTemplate.htm", {
		method : "updateShareTemp",
		depidstr : depidstr,
		useridstr : useridstr,
		lguserid : lguserid,
		tempid : tempId,
		infotype : infoType
	}, function(returnmsg) {
		$("#updateShareTemp").attr("disabled", false);
		if (returnmsg.indexOf("html") > 0) {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_1"));
		} else if (returnmsg == "success") {
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_46"));
			$("#shareTmpDiv").dialog("close");
		} else if (returnmsg == "fail") {
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_47"));
		} else {
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_3"));
		}
	});
}
// 关闭模板共享窗口
function closeShare() {
	$("#flowFrame").attr("src", "");
	$("#flowFrame").attr("attrid", "");
	$(window.frames['flowFrame'].document).find("#right").empty();
	$("#shareTmpDiv").dialog("close");
}

//对表单字符进行过滤
$(document).ready(function(){
	$(".filter").bind('blur keyup',function(){
		var value=$.trim($(this).val());
		var reg=/['<>"\n]/g;
		if(reg.test(value)){
			value=value.replace(reg, "");
			$(this).val(value);
		}
		
	})
});

function getContentVal(msg){
	var reg=/\{#参数(.*?)#\}/g;
	if(getJsLocaleMessage("xtgl","xtgl_yy")=="zh_CN"){
		reg=/\{#参数(.*?)#\}/g;
	}else if(getJsLocaleMessage("xtgl","xtgl_yy")=="zh_TW"){
		reg=/\{#參數(.*?)#\}/g;
	}else if(getJsLocaleMessage("xtgl","xtgl_yy")=="zh_HK"){
		reg=/\{#[Pp]aram(.*?)#\}/g;
	}
	msg=msg.replace(reg,replacer);
	return msg;
}

//点详细，弹出框
function openReviewFlow(mtId,userId,reviewType){
	$('#reviewTableDiv').hide();
	$.post("reviewflow.htm?method=getReviewFlow",{mtId:mtId,userId:userId,reviewType:reviewType},function(jsonObject){
		 var json = eval("("+jsonObject+")");
		//是否有 审批记录
		var haveRecord = json.haveRecord;

		var onelevel = json.onelevel;
		var onecondition = json.onecondition;
		var twolevel = json.twolevel;
		var twocondition = json.twocondition;
		var threelevel = json.threelevel;
		var threecondition = json.threecondition;
		var fourlevel = json.fourlevel;
		var fourcondition = json.fourcondition;
		var fivelevel = json.fivelevel;
		var fivecondition = json.fivecondition;
		var isshow = json.isshow;
		$("#reviewTable").empty();
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_18")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_39")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_49")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_40")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_41")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_42")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_50")+"</td><td class='div_bd'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_51")+"</td></tr>";
		if(haveRecord == "1"){
			//审批记录
			var recordList = json.members;
			 for(var i= 0;i<recordList.length;i++){
					var Rlevel = recordList[i].Rlevel;
					var Reviname = recordList[i].Reviname;
					var Exstate = recordList[i].exstate;
					var Exresult= recordList[i].exresult;
					var Comments = recordList[i].Comments;
					var rtime = recordList[i].rtime;
					var remindtime=recordList[i].remindtime;
					var allowremind=recordList[i].allowremind;
					var frid=recordList[i].flowid;
					var existreviewer=recordList[i].existreviewer;
					if(existreviewer=="1"){
						msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+Rlevel+"</td> "
		        		+"  <td align='center'  width='10%'  class='div_bd'>"+Reviname+"</td>"          
				        +"  <td align='center'  width='10%'  class='div_bd'>"+Exstate+"</td> "
				         +"  <td align='center'  width='10%'  class='div_bd'>"+Exresult+"</td> "
				        +"  <td align='left'  width='20%'  style='word-break: break-all;'  class='div_bd'>";
	
				        var view_Comments=Comments.length>17?(Comments.substr(0,17)+"..."):Comments;
				        msg=msg+"<a onclick='javascript:modify(this,2)' style='cursor: pointer;'><label style='display:none'><xmp>"+Comments+"</xmp></label>"
						+"<xmp>"+view_Comments+"</xmp></a>"
				        +"</td> <td align='center'  width='15%'  class='div_bd'>"+rtime+"</td>" 
				        +"</td> <td align='center'  width='15%'  class='div_bd'>"+remindtime+"</td>" ;
				        if(allowremind=="1"){
				        	msg=msg+"<td align='center'  width='10%'  class='div_bd'><a onclick='javascript:remind("+frid+")' style='cursor: pointer;color:blue;'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_52")+"</a></td>" +"</tr>" ;
				        }else{
				        	msg=msg+"<td align='center'  width='10%'  class='div_bd'>"+allowremind+"</td>" +"</tr>" ;
				        }
			        }else{
			        	msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+Rlevel+"</td> "
		        		+"  <td colspan='6' align='center'  width='80%'  class='div_bd' style='color:red;'>"+Exstate+"</td>";          
				        msg=msg+"<td align='center'  width='10%'  class='div_bd'>-</td>" +"</tr>" ;
				        
			        }
			        
			}
		}else{
			msg = msg +	"<tr><td colspan='8' align='center'  class='div_bd' height='24px'>"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_43")+"</td></tr>";
		}
		$("#reviewTable").html(msg);
		
		$('#reviewTableDiv').show();	
		$("#nextreviewmgs").empty();
		if(isshow == "1"){
			var recordmsg = "";
				if(onelevel=="1"){
					recordmsg=recordmsg+"1"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_53")
					if(onecondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_48");
					}else if(onecondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_49");
					}
				}
				if(twolevel=="2"){
					recordmsg=recordmsg+"</br>2"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_53")
					if(twocondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_48");
					}else if(twocondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_49");
					}
				}
				if(threelevel=="3"){
					recordmsg=recordmsg+"</br>3"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_53")
					if(threecondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_48");
					}else if(threecondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_49");
					}
				}
				if(fourlevel=="4"){
					recordmsg=recordmsg+"</br>4"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_53")
					if(fourcondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_48");
					}else if(fourcondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_49");
					}
				}
				if(fivelevel=="5"){
					recordmsg=recordmsg+"</br>5"+getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_53")
					if(fivecondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_48");
					}else if(fivecondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_49");
					}
				}
			$("#nextreviewmgs").html(recordmsg);
			$('#nextreviewmgs').show();	
		}
		 $("#reviewflowinfo").dialog("open");
	 });
}

function remind(frid){
		$.post("reviewflow.htm?method=cuibanFlow&frid="+frid,
				{},
				function(result)
				{
				  if(result=="success")
				  {
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_54"));
				  }
				  else if(result=="getTaskFail")
				  {
				    alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_55"));
				  }
				  else if(result=="getDcTempFail")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_56"));
				  }
				  else if(result=="getWxTempFail")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_57"));
				  }
				  else if(result=="noPhone")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_58"));
				  }
				  else if(result=="noContent")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_59"));
				  }
				  else if(result=="noAdmin")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_60"));
				  }
				  else if(result=="noAgree")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_61"));
				  }
				  else if(result=="noDisAgree")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_62"));
				  }
				  else if(result=="noSP")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_63"));
				  }
				  else if(result=="noSubNo")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_64"));
				  }
				  else if(result=="noSpNumber")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_65"));
				  }
				  else if(result=="validPhone")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_66"));
				  }
				  else if(result=="wgkoufeiFail")
				  {
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_67"));
				  }else{
					  alert(getJsLocaleMessage("xtgl","xtgl_cswh_dxmbgl_68"));
				  }
				}
			);
}