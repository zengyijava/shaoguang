function cut(){
	var content = $.trim($('#tmMsg').val());
	var huiche = content.length - content.replaceAll("\n", "").length;
	if (content.length + huiche > 990) {
		$("#tmMsg").val(content.substring(0, 990 - huiche));
	}
	//len(ob);
}
function getContentVal(obj){
	var msg = $(obj).val();
		//reg=/\{#参数(.*?)#\}/g;
		var cs=getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_cs");
		if(cs == '参数'){
			reg=/\{#参数(.*?)#\}/g;
		}else if(cs == '參數'){
			reg=/\{#參數(.*?)#\}/g;
		}else{
			reg=/\{#Param(.*?)#\}/g;
		}
	msg=msg.replace(reg,replacer);
	return msg;
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
		var tmMsg = $.trim(getContentValX('#tmMsg'));
		var pattern=/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;  
		var selectnum=$("#trSmsTmpType  option:selected").val();//取得选中是下拉框的值 may add 
		if (tmName == "") {
			//alert("模板名称不能为空！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbmcbnwk"));
			$('#subBut').attr("disabled",false);
			return;
		}
		if (tmCode == "") {
			//alert("模板编号不能为空！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbbhbnwk"));
			$('#subBut').attr("disabled",false);
			return;
		}else if(escape(tmCode).indexOf("%u")!=-1){
			//alert("模板编号不能包含汉字！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbbhbnbhhz"));
			$('#subBut').attr("disabled",false);
			return;   
		}else if(pattern.test(tmCode)){
			//alert("模板编号不能包含特殊字符！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbbhbnbhtszf"));
			$('#subBut').attr("disabled",false);
			return;   
		}else if(tmCode.indexOf(' ')>-1){
			//alert("模板编号中间不能包含空格！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbbhzjbnbhkg"));
			$('#subBut').attr("disabled",false);
			return;   
		}
		
		if (tmMsg == "") {
			//alert("模板内容不能为空！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbnrbnwk"));
			$('#subBut').attr("disabled",false);
			return;
		}
		if (tmName.length > 20) {
			//alert("模板名称长度过长！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbmccdgc"));
			$('#subBut').attr("disabled",false);
			return;
		}
		var dsflag = $("#dsflag").val();
		//通用动态模板和智能抓取模板输入格式判断
		if(dsflag == 1 || dsflag == 3)
		{
			
			if(!/#[Pp]_[1-9][0-9]*#+/g.test(tmMsg)){
				//alert("请按参数格式输入模板内容！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qacsgssrmbnr"));
				$('#subBut').attr("disabled",false);
				return;
			}
		}
		if (dsflag == "3" && tmMsg.length > 320) {
			//alert("移动财务模板内容长度过长,不能大于320个字符，超过部分将被截取！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ydcwmbnrcdgc"));
			$("#tmMsg").val($("#tmMsg").val().substring(0,320));
			$('#subBut').attr("disabled",false);
			return;
		}
		
		if (tmMsg.length > 990) {
			//alert("模板内容长度过长,不能大于990个字符，超过部分将被截取！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbnrcdgc"));
			$("#tmMsg").val($("#tmMsg").val().substring(0,990));
			$('#subBut').attr("disabled",false);
			return;
		}

		var checkName="false";
		var checkURL="";
		if($('#OpType').val()=="add")
		{
			checkName="true";
			url = pathUrl+"/common.htm?method=checkBadWord1";
			checkURL= pathUrl+"/eng_mtProcess.htm?method=checkRepeat";
		}
		else
		{
			url = "common.htm?method=checkBadWord1";
			checkURL= "eng_mtProcess.htm?method=checkRepeat";
		}
		if(dsflag == "2")
		{
			var is="false";
			//检查重复
			$.post(checkURL, {
				tmCode:tmCode,
				corpCode : $("#lgcorpcode").val()
			}, function(message) {
				if (message == "repeat") {
					is="true";
					//alert("模板编号不能重复！");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbbhbncf"));
					$('#subBut').attr("disabled",false);
				}else{
					$("#tmform").attr("action",$("#tmform").attr("action")+"&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val());
					document.forms["tmform"].submit();
				}
			});
		}
		else
		{
			var is="false";
			//检查重复
			$.post(checkURL, {
				tmCode:tmCode,
				corpCode : $("#lgcorpcode").val()
			}, function(message) {
				if (message == "repeat") {
					is="true";
					//alert("模板编号不能重复！");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbbhbncf"));
					$('#subBut').attr("disabled",false);
				}else{
						$.post(url, {
							tmName:tmName,
							checkName:checkName,
							tmMsg : tmMsg,
							corpCode : $("#lgcorpcode").val()
						}, function(message) {
							if(message.indexOf("@")==-1){
								//alert("系统繁忙，请刷新");
								alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xtfm"));
								$('#subBut').attr("disabled",false);
								return;
							}
							message=message.substr(message.indexOf("@")+1);
							if (message != "") {
								if (message == "error") {
									//alert("过滤关键字失败！");
									alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_glgjzsb"));
									$('#subBut').attr("disabled",false);
								} else if (message == "errorName") {
									//alert("短信模板名重复，请重新输入！");
									alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dxmbmcf"));
									$('#subBut').attr("disabled",false);
								} else
								{
									//alert("短信模板内容包含如下违禁词组：\n     "+message+"\n请检查后重新输入。");
									alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dxmbnrbhrxwjcz")+"\n     "+message+"\n"+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qjchcxsr"));
									$('#subBut').attr("disabled",false);
								}
							} else {
								var repedMsg = getContentValX($("#tmMsg"));
								$("#tmMsg").val(repedMsg);
								$("#tmform").attr("action",$("#tmform").attr("action")+"&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val());
								document.forms["tmform"].submit();
							}
						});
				}
			});
		}
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
	
	//新增共享模板DIV
	/*$("#shareTmpDiv").dialog({
		autoOpen: false,
		height:480,
		width: 520,
		resizable:false,
		modal: true
	});*/
});

function del(path, i){
	//if(confirm("您确定要删除该条信息吗？"))
	if(confirm(+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_nqdyscgxxm")))
	{
		var lgcorpcode=$("#lgcorpcode").val();
		var lguserid=$("#lguserid").val();
		
		$.post("tem_smsTemplate.htm?method=delete",{ids:i,lgcorpcode:lgcorpcode},function(result){
			if(result>0)
			{
				//alert("删除成功！");
				alert(+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sccg"));
				//alert(path+"/tem_smsTemplate.htm?lguserid="+lguserid);
				location.href = path+"/tem_smsTemplate.htm?lguserid="+lguserid;
			}else{
				//alert("删除失败！");
				alert(+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_scsb"));
			}
		});
	}
}



function changeMb(i)
{
	if(i==3)
	{
		$("#trSmsTmpType").css("height","100px");
		$("#subModule").css("display","inline");
	}else
	{
		$("#subModule").css("display","none");
		$("#trSmsTmpType").css("height","auto");
	}
	if(i==0 || i==2)
	{
		$("#fontFmTip,.para_cg,.showParams,.tit_panel").hide();
	}
	else
	{
		$("#fontFmTip,.tit_panel,.showParams").show();
		$(".para_cg").css("display","block");
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
		//alert("新建成功！");
		alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xjcg"));
		window.parent.getTmplInfo();
		window.parent.closeAddSmsTmpdiv();

	}
	else if(result=="error")
	{

		//alert("操作失败！");
		alert(+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));
		window.parent.closeAddSmsTmpdiv()

	}else if( "noFlower"==result)
	{

		//alert("操作失败，未找到下一级审批人员，请联系管理员！");
		alert(+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsbwzdxyjspry"));
		window.parent.closeAddSmsTmpdiv();
	}
	else if(result != "null" && result != null)
	{
		if(result.indexOf("empex") == 0){
			result = result.substring(5);
		}

		//alert("短信模板创建失败："+result);
		alert(+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dxmbcjsb")+result);
		window.parent.closeAddSmsTmpdiv()
	}
	


}
function showOfEdit(result)
{
	if( result=="true")
	{
		//alert("修改成功！");
		alert(+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xgcg"));
	}
	else if( result=="error")
	{
		//alert("操作失败！");
		alert(+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));
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





function showEditSmsTmp(tmId)
{
	$("#editSmsTmpDiv").css("display","block");
	$("#editSmsTmpFrame").css("display","block");
	//修改
	
	$("#editSmsTmpDiv").dialog({
		autoOpen: false,
		height:410,
		width: 620,
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
	//$('#modify').dialog("option","title","模板内容");
	$('#modify').dialog("option","title",+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_mbnr"));
}
else if(state=="2")
{
	//$('#modify').dialog("option","title","审批意见");
	$('#modify').dialog("option","title",+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_spyj"));
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
			//alert("修改成功！");
			alert(+message+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xgcg"));
			$("#tempState"+i).empty();
			if(ks == 1)
			{
			    //$("#tempState"+i).append("<option value='1' selected='selected'>已启用</option>");
			    //$("#tempState"+i).append("<option value='0' >停用</option>");
				$("#tempState"+i).append("<option value='1' selected='selected'>"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yqy")+"</option>");
			    $("#tempState"+i).append("<option value='0' >"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_ty")+"</option>");
			}
			else
			{
				//$("#tempState"+i).append("<option value='0' selected='selected'>已停用</option>");
				//$("#tempState"+i).append("<option value='1' >启用</option>");
				$("#tempState"+i).append("<option value='0' selected='selected'>"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yty")+"</option>");
				$("#tempState"+i).append("<option value='1' >"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qy")+"</option>");
			}
			changeEmpSelect($("#tempState"+i),80);
		}else{
			//alert("修改失败！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xgsb"));
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
		//var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>审核级别</td><td class='div_bd'>审核人</td><td class='div_bd'>审核结果</td><td class='div_bd'>审核意见</td><td class='div_bd'>审核时间</td></tr>";
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_spjb")
		+"</td><td class='div_bd'>"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_shr")
		+"</td><td class='div_bd'>"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_shjg")
		+"</td><td class='div_bd'>"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_shyj")
		+"</td><td class='div_bd'>"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_shsj")+"</td></tr>";
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
			//msg = msg + "<tr class='div_bd' height='24px'> <td  align='center' class='div_bd' colspan='5'>无记录</td></tr>"
			msg = msg + "<tr class='div_bd' height='24px'> <td  align='center' class='div_bd' colspan='5'>"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_wjl")+"</td></tr>"
		}
		$("#recordTable").html(msg);
		$('#recordTableDiv').show();	
		$("#nextrecordmgs").empty();
		if(isshow == "1"){
			//var recordmsg = " 本级未审批人员：&nbsp;" + firstshowname;
			var recordmsg = getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bjwspry_mh")+"&nbsp;" + firstshowname;
			if(firstcondition == "1"){
				//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qbtgsx");
			}else if(firstcondition == "2"){
				//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qzyrspsx");
			}
			if(secondshowname != "" && secondcondition != ""){
				//recordmsg = recordmsg + "</br>下一级审批人员/机构：&nbsp;" + secondshowname;
				recordmsg = recordmsg + "</br>"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xyjspryjg")+"&nbsp;" + secondshowname;
				if(secondcondition == "1"){
					//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qbtgsx");
				}else if(secondcondition == "2"){
					//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qzyrspsx");
				}
			}
			$("#nextrecordmgs").html(recordmsg);
			$('#nextrecordmgs').show();	
		}
		$("#smsdetailinfo").dialog("open");
	 });
}

//共享模板操作
function showShareTmp(tmId, tmName) {
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var iPath = $("#iPath").val();
	tmName=tmName||'-';
	var url = iPath+"/tem_shareTemplate.jsp?lguserid="+ lguserid + "&lgcorpcode=" + lgcorpcode+"&tempId="+tmId+"&tempName="+tmName;
	$("#flowFrame").attr("src", url);
	$("#flowFrame").attr("attrid", tmId);
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
			//alert("网络超时，请重新登录！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_glcs"));
		} else if (returnmsg == "success") {
			//alert("设置模板共享成功！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_szmbgxcg"));
			$("#shareTmpDiv").dialog("close");
		} else if (returnmsg == "fail") {
			//alert("设置模板共享失败！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_szmbgxsb"));
		} else {
			//alert("操作失败！");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));
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