//跳到通道账号页面
function toGateAcct()
{	
	setArtDialogState();
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	getErrMon();
	window.parent.mainFrame.openNewTab("2900-1200",pathUrl+"/mon_gateAcctMon.htm?method=find&herfType=1&lgcorpcode="+lgcorpcode);
//	frameRemove();
}

//跳到通道账号页面（具体账号）
function toGateAcctById(gateaccount)
{
	setArtDialogState();
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	getErrMon();
	window.parent.frames['mainFrame'].openNewTab("2900-1200",pathUrl+"/mon_gateAcctMon.htm?method=find&herfType=1&gateaccount="+gateaccount+"&lgcorpcode="+lgcorpcode);
	//frameRemove();
}

//跳到sp账号页面（具体账号）
function toSpAcctMon(spaccountid)
{
	
	setArtDialogState();
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	getErrMon();
	window.parent.frames['mainFrame'].openNewTab("2900-1400",pathUrl+"/mon_spAcctMon.htm?method=find&herfType=1&lgcorpcode="+lgcorpcode+"&spaccountid="+spaccountid);
//	frameRemove();
}

function getErrMon()
{
	var pathUrl = $("#pathUrl").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var lguserid = $("#lguserid").val();
	var time = new Date();
	$.post(pathUrl+"/mon_mainMon.htm", 
	{
		method : "getErrMon",
		lguserid : lguserid,
		lgcorpcode : lgcorpcode,
		isAsync:"yes",
		time : time
	}, 
	function(result) {
		if(result == "outOfLogin")
		{
			location.href=pathUrl+"/common/logoutEmp.html";
			return;
		}
		else if(result!="error")
		{
			var MonMsgHtml = "";
			if(result!="")
			{
				var monMsgJson=eval("("+result+")");
				for(var i=0;i< monMsgJson.length;i++)
				{
					MonMsgHtml = MonMsgHtml + "<dl onclick='toErrMon("+monMsgJson[i].id+")' style='cursor: pointer;'><dt>" + monMsgJson[i].evttime + "</dt><dd>" +monMsgJson[i].monname+"："+  monMsgJson[i].msg + "</dd></dl>";
				}
				window.parent.document.getElementById("warning_info").innerHTML=MonMsgHtml;
				//it.innerHTML=MonMsgHtml;
				$('#mainFrame',window.parent.document).contents().find('#menuFrame>iframe').contents().find('#openTimer').trigger('click');
			}
			//无数据
			else
			{
				MonMsgHtml = getJsLocaleMessage("ptjk","ptjk_jkst_jkst_9");
				window.parent.document.getElementById("warning_info").innerHTML=MonMsgHtml;
			}
			tiemrErrMon = window.setTimeout(getErrMon,30*1000);
		}
	});
}

//跳到主机监控详情页面
function toHostMon(id)
{
	setArtDialogState();
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
//	$('iframe[name="Openx1354"]',window.parent.document).attr('src','');
	$('#monFrame',window.parent.document).attr('src','');
	$('.aui_state_lock',window.parent.document).remove();
	getErrMon();
	window.parent.frames['mainFrame'].openNewTab("2900-1500",pathUrl+"/mon_hostMon.htm?method=find&herfType=1&lgcorpcode="+lgcorpcode+"&hostid="+id);
//	frameRemove();
}
//跳到在线用户页面
function toOnline()
{	
	setArtDialogState();
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	getErrMon();
	window.parent.frames['mainFrame'].openNewTab("2900-1700",pathUrl+"/mon_onlineUserNum.htm?method=find&herfType=1&lgcorpcode="+lgcorpcode);
//	frameRemove();
}
//跳到在线用户页面
function toErrMon(id)
{
	setArtDialogState();
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	getErrMon();
	window.parent.frames['mainFrame'].openNewTab("2900-1000",pathUrl+"/mon_realTimeAlarm.htm?method=find&herfType=1&lgcorpcode="+lgcorpcode+"&id="+id);
//	frameRemove();
}
//跳到wbs程序
function toSpPrcMon(id)
{
	setArtDialogState();
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	getErrMon();
	window.parent.frames['mainFrame'].openNewTab("2900-1600",pathUrl+"/mon_prcMon.htm?method=find&herfType=1&lgcorpcode="+lgcorpcode+"&id="+id);
//	frameRemove();
}
//跳到新建主机页面
function toAddMon()
{
	setArtDialogState();
	var lgcorpcode = $("#lgcorpcode").val();
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	getErrMon();
	window.parent.frames['mainFrame'].openNewTab("3000-1300",pathUrl+"/mon_hostMonCfg.htm?method=find&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid);
//	frameRemove();
}
function frameRemove(){
	//$('.aui_content',window.parent.parent.document).html('');
	//$('.aui_outer:eq(0)',window.parent.parent.document).parent().hide();
	//$('.aui_close',window.parent.document).trigger('click');
	//$('.aui_close',window.parent.document).trigger('click');
	//console.log($('.aui_close',window.parent.document).size());
	$('#mainFrame',window.parent.document).contents().find('#menuFrame>iframe').contents().find('#openTimer').trigger('click');
	//$('iframe[name="Openx1354"]',window.parent.document).attr('src','');
	//$('#monFrame',window.parent.document).attr('src','');
	//$('.aui_state_focus',window.parent.document).hide();
}
//给主界面是指是否是点击关闭按钮标示
function setArtDialogState()
{
	$(window.parent.parent.document).find("#close_mon_State").val("1");
}
// 计算万，亿
function countView(number){
	var viewStr = "0";
	var count=parseInt(number);
	if(count<10000)
	{
		viewStr = count;
	}
	else if(count>=10000&&100000000>count)
	{
		
		viewStr =count/10000;//万位，保留一位小数点
		viewStr=viewStr+"";
		if(viewStr.indexOf(".")>-1){
			//采用四舍五入转换，不采用截取
			var num = new Number(viewStr);
			viewStr=num.toFixed(1);
			viewStr=viewStr+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_17");
		}
		else
		{
			viewStr=viewStr+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_17");
		}

	}else{
		viewStr =count/100000000;
		viewStr=viewStr+"";
		if(viewStr.indexOf(".")>-1){
			//采用四舍五入转换，不采用截取
			var num = new Number(viewStr);
			viewStr=num.toFixed(1);
			viewStr=viewStr+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_18");
		}
		else
		{
			viewStr=viewStr+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_17");
		}
	}
	return viewStr;
}
