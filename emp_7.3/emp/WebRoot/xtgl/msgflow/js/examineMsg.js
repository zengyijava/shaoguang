$(document).ready(function(){
	getLoginInfo("#hiddenValueDiv");
	var h = 510;
	if (navigator.appName == "Netscape")
	{
		h = 510;
	}
	$("#tempView").dialog({
		autoOpen: false,
		height:h,
		width: 290,
		modal: true,
		resizable:false,
		close:function(){
		    cplaytime = 0;
			nplaytime = -1;
			clearInterval(ttimer); 
		}
	});
});

function compartDate() { 
	var endDate=$("#timerTime").val();
	if(endDate==undefined)
	{
		return false
	}
	else{
		return true;
	}
}  

function buttonDisabled(){
	$('#butt').attr("disabled","disabled");
	$('#butt1').attr("disabled","disabled");
}

function buttongEffect(){
	$('#butt').attr("disabled","");
	$('#butt1').attr("disabled","");
}


// 同意 / 拒绝 流程ID 同意1 拒绝2
function submitForm2(i,j){
	var msgname =  $("#msgname").val();
	var pathUrl = $("#pathUrl").val();
	buttonDisabled();
	// 获取审核意见
	var cont = $("textarea[name='content']").val();
	var date = new Date();
	// 是否定时 1定 0不是定
	var timerStatus = $("#timerStatus").val();
	// 定时时间
	var timerTime = $("#timerTime").val();
	var isLastCheck = $("#isLastCheck").val();
	var count = parseInt($("#count").val());
	if(j==1 && count == 0){
		alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_71"));
		buttongEffect();
	}else if(cont.length>300){
		alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_72"));
		buttongEffect();
		return;
	}else if(timerStatus==1 && timerTime==""){
		alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_73"));
		buttongEffect();
		return;
	}else if(j==1&&compartDate()){
		$.ajax({
			url:"msg_smsInfoReview.htm",
			data:{method : "getServerTime"},
			type:"post",
			success:function(msg){
			if(msg.indexOf("@")==-1){
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_74"));
			}
			msg=msg.substr(msg.indexOf("@")+1);
			if(msg=="error"){
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_75"));
			}else {
				var date1 = new Date(Date.parse(timerTime.replaceAll("-","/")));
				var date2 = new Date(Date.parse(msg.replaceAll("-","/")));
				// 这里是判断时间是否超时
				if (date1 < date2) {
		    		// 这个是最后审批人
				    if(isLastCheck == "yes"){
				    	if(confirm(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_76"))){
				    		timerStatus = "0";// 立即发送：发送时间改为立即发送
							sub(i,j,cont,timerStatus,timerTime);
						}else{
		    				buttongEffect();
		    			}
				    }else{
				    	alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_77"));
				    	buttongEffect();
				    }
				}else{
					if(j==1 && confirm(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_78")+msgname+getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_79")))
					{
						sub(i,j,cont,timerStatus,timerTime);
					} else {
						buttongEffect();
					}
				}
			}
				
		},
		error:function(xrq,textStatus,errorThrown){
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_74"));
			}
		});
	}else if((j==1 && confirm(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_78")+msgname+getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_79"))) || (j==2 && confirm(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_80")+msgname+getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_79")))){
			sub(i,j,cont,timerStatus,timerTime);
		}else{
			buttongEffect();
		}
}


// 提交后台处理
function sub(i,j,cont,timerStatus,timerTime)
{
	var pathUrl = $("#pathUrl").val();
	var lguserid = $("#lguserid").val();
	var mtId = $("#mtId").val();
	var isLastCheck = $("#isLastCheck").val();
	var frId = $("#frId").val();
	var msgtype = $("#msgtype").val();
	//加日志
	EmpExecutionContext.log(pathUrl+"/msg_smsInfoReview.htm?method=reviewMsg",{
		flowid:i,
		RState:j,
		comments:cont,
		timerStatus: timerStatus,
		timerTime : timerTime,
		isLastCheck:isLastCheck,
		mtId:mtId,
		lguserid:lguserid,
		frId:frId,
		msgtype:msgtype
	});
	$.post(pathUrl+"/msg_smsInfoReview.htm?" +
			"method=reviewMsg&flowid="+i+"&RState="+j+
			"&timerStatus="+timerStatus+"&timerTime="+timerTime+
			"&isLastCheck="+isLastCheck+"&mtId="+mtId+
			"&lguserid="+lguserid+"&frId="+frId+"&msgtype="+msgtype,{
		comments:cont
	},function(result){
			if(result != null && result.indexOf("success")!=-1){
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_81"));
				updateToList();
			}else if(result=="000"){
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_82"));
				updateToList();
			}else if (result=="timerSuccess")
			{
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_83"));
				updateToList();
			}else{
				 buttongEffect();
	    		if(result=="fail")
	    		{
	        		alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_84"));
	    		}else if(result == "overtime"){
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_85"));
				}else if(result=="nospnumber")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_86")+'<%=StaticValue.SMSACCOUNT %>'+getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_87"));
					updateToList();
				}
				else if(result=="feefail")
	    		{
	    		    alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_88"));
	    		}else if(result=="timerFail")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_89"));
					updateToList();
				}else if(result=="addChildFail")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_90"));
					updateToList();
				}else if(result=="noflow")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_91"));
					updateToList();
				}else if(result=="finish")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_92"));
					updateToList();
				}else if(result=="unfinish")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_93"));
					updateToList();
				}else if(result=="isrevoke")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_94"));
					updateToList();
				}else if(result=="freeze")
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_95"));
					updateToList();
				}else
	    		{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_96")+result);         
								updateToList();				
			    }
		    }
	});
}

// 显示隐藏信息
function changflod(num){
	if($("#img"+num).hasClass("unfold")){
		$("#msgdiv"+num).show();
		$("#img"+num).removeClass("unfold");
		$("#img"+num).addClass("fold");
	}else if($("#img"+num).hasClass("fold")){
		$("#msgdiv"+num).hide();
		$("#img"+num).removeClass("fold");
		$("#img"+num).addClass("unfold");
	}
}

// 刷新到列表 页面
function updateToList(){
	var lguserid = $("#lguserid").val();
	var pathUrl = $("#pathUrl").val();
	window.location.href = pathUrl+"/msg_smsInfoReview.htm?method=find&lguserid="+lguserid+"&isOperateReturn=true";
}


function doPreview(msg,bmtype,tplPath)
{
	inits();
	// 兼容静态动态以及普通 模板发送中的 彩信预览发送
	if(bmtype == "11" || bmtype == "12"){
		msg = tplPath;
	}
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl+"/tef_mmsTemInfoReview.htm?method=getTmMsgXtgl",{tmUrl:msg},function(result){
		if (result != null && result != "null" && result != "")
		{
			arr = result.split(">");
		if(arr[0] != null && arr[0] != "")
		{
			var da = $.parseJSON(arr[0]);
			ttime = (da.totaltime/1000);
		}
		index = 1;
		$("#screen").empty();
		$("#pointer").empty();
		$("#nextpage").empty();
		$("#currentpage").empty();
		parmCount =null;
		$("#inputParamCnt1").empty();
		$("#tempView").dialog("open");
			var dsFlag = bmtype==12?1:0;
			play(dsFlag);
		}
		else
		{
			alert(getJsLocaleMessage("xtgl","xtgl_spgl_xxsp_97"));
		}
	});
}