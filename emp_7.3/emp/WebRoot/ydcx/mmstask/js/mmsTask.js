
//检测文件是否存在，各网页有涉及到查看短信号码附件的均需调用此文件
function checkFile(url,src) {
	//var isjq = $("#isCluster").val();
	//if(1==isjq)
	//{
	//	checkFile2(url,$("#httpUrl").val());
	//	return;
	//}
	$.post(src+"/common.htm?method=checkFile", {
		url : url
	},
		function(result) {
            if (result.indexOf("true") == 0) {
                src = result.substring(4)||src;
                src = src.replace(/^@/,'');
				window.showModalDialog(src.replace(/\/$/,"") + "/" + url +"?Rnd="+ Math.random());
				//window.open(src + "/" + url,"_blank");
			} else if (result == "false")
				//alert("文件不存在");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_wjbcz"));
			else
				//alert("出现异常,无法跳转");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_cxyc"));
	});
}


//检测文件是否存在，各网页有涉及到查看短信号码附件的均需调用此文件(外网文件服务器)
function checkFileOuter(url,src) {
	$.post(src+"/common.htm?method=checkFileOuter", {
		url : url
	},
		function(result) {
            if (result.indexOf("true") == 0) {
                src = result.substring(4)||src;
                src = src.replace(/^@/,'');
				window.showModalDialog(src.replace(/\/$/,"") + "/" + url +"?Rnd="+ Math.random());
			} else if (result == "false")
				//alert("文件不存在");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_wjbcz"));
			else
				//alert("出现异常,无法跳转");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_cxyc"));
	});
}


//文件下载
function uploadFiles(url,src)
{
	//var isjq = $("#isCluster").val();
	//if(1==isjq)
	//{
	//	uploadFiles2(url,$("#httpUrl").val());
	//	return;
	//}
	$.post(src+"/common.htm?method=checkFile", {
		url : url
	},
		function(result) {
			if (result.indexOf("true") == 0) {
                src = result.substring(4)||src;
                if(src.indexOf('@')==0){//来源文件服务器
                    src = src.replace(/^@/,'');
                    uploadFiles2(url,src);
                }else{//来源emp节点
                    download_href(src.replace(/\/$/,"")+"/doExport.hts?u="+url);
                }
			} else if (result == "false")
				//alert("文件不存在或该文件无访问权限");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_wjbczhgwjwfwqx"));
			else
				//alert("出现异常,无法跳转");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_cxyc"));
	});

}

//文件下载(外网文件服务器)
function downloadFilesOuter(url,src)
{
	$.post(src+"/common.htm?method=checkFileOuter", {
		url : url
	},
		function(result) {
			if (result.indexOf("true") == 0) {
                src = result.substring(4)||src;
                if(src.indexOf('@')==0){//来源文件服务器
                    src = src.replace(/^@/,'');
                    uploadFiles2(url,src);
                }else{//来源emp节点
                    download_href(src.replace(/\/$/,"")+"/doExport.hts?u="+url);
                }
			} else if (result == "false")
				//alert("文件不存在或该文件无访问权限");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_wjbczhgwjwfwqx"));
			else
				//alert("出现异常,无法跳转");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_cxyc"));
	});

}



function checkFile2(url,src) {
	window.showModalDialog(src + "checkFile?url=" + url +"&Rnd="+ Math.random());
}
function uploadFiles2(url,src)
{
	window.open(src + "checkFile?type=downLoad&url=" + url +"&Rnd="+ Math.random());	
}


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~fuction



function modify(t){
	$('#modify').dialog({
		autoOpen: false,
		resizable: false,
		width:250,
	    height:200
	});
	$("#msg").children("xmp").empty();
	$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
	$('#modify').dialog('open');
}

function rtime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#submitSartTime").attr("value");
	if(v.length != 0)
	{
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		var day = 31;
		if (mon != "12")
		{
		    mon = String(parseInt(mon,10)+1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		    switch(mon){
		    case "01":day = 31;break;
		    case "02":day = 28;break;
		    case "03":day = 31;break;
		    case "04":day = 30;break;
		    case "05":day = 31;break;
		    case "06":day = 30;break;
		    case "07":day = 31;break;
		    case "08":day = 31;break;
		    case "09":day = 30;break;
		    case "10":day = 31;break;
		    case "11":day = 30;break;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)+1));
		    mon = "01";
		}
		max = year+"-"+mon+"-"+day+" 23:59:59"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

};

function stime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#submitEndTime").attr("value");
    var min = "1900-01-01 00:00:00";
	if(v.length != 0)
	{
	    max = v;
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		if (mon != "01")
		{
		    mon = String(parseInt(mon,10)-1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)-1));
		    mon = "12";
		}
		min = year+"-"+mon+"-01 00:00:00"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

};
function doCancel(id)
{
	var pathUrl = $("#pathUrl").val();
	//if (confirm("确定要撤销该彩信的发送吗？"))
	if (confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qdycxgcxdfsm")))
	{
		$.post(pathUrl+"/mmt_mmsTimerTask.htm?method=doCancel",{mtId:id},function(result){
	          if (result != null && result != "")
	          {
	               getCt();
	              alert(result);
		           window.location.href = pathUrl+"/mmt_mmsTask.htm?method=find&lgguid="+$("#lgguid").val()+"&lguserid="+$("#lguserid").val();
	          }
	    });
	}
}
//查看彩信内容
function doPreview(msg,bmtype,tplPath)
{
	inits();
	var pathUrl = $("#pathUrl").val();
	$.post(pathUrl+"/mmt_mmsTask.htm?method=getTmMsgByBmtype",{tmUrl:msg,bmtype:bmtype,tplPath:tplPath},function(result){
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
        	//$("#screen").html("<center style='padding-top:120px'><img src='<%=inheritPath%>/images/play.png' style='cursor:pointer;width:48;height:48' title='播放' onclick='javascript:play();'/></center>");
			$("#tempView").dialog("open");
			var dsFlag = bmtype==12?1:0;
			play(dsFlag);
		}
		else
		{
             //alert("内容文件不存在，无法预览！");
             alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_nrwjbcz"));
		}
	});
}

function reSend(mtId){
   //if(confirm("确定要重新提交吗？")){
   if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qdycxtjm"))){
	   var pathUrl = $("#pathUrl").val();
	   $.post(pathUrl+"/smm_sameMms.htm?method=reSend",{mtId:mtId},function(result){
	          if (result != null && result != "")
	          {
	               getCt();
	               alert(result);
		           window.location.href = pathUrl+"/mmt_mmsTask.htm?method=find&lgguid="+$("#lgguid").val()+"&lguserid="+$("#lguserid").val();
	          }else{
	              //alert("系统繁忙，请稍后再试！");
	              alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_xtfm"));
	          }
		});
	}
}




//点详细，弹出框
function openMmsDetail(mtId,userId){
	 var pathUrl = $("#pathUrl").val();
	 $('#recordTableDiv').hide();
	 $.post(pathUrl+"/mmt_mmsTask.htm?method=getMmsDetail",{mtId:mtId,userId:userId},function(jsonObject){
		 var json = eval("("+jsonObject+")");
			 //$("#mms_taskname").text(json.mmsname);
			 //$("#mms_title").text(json.mmstitle);
			 // $("#mms_time").text(json.mmstime);
			 //$("#mms_count").text(json.mmscount);
			 //$("#mms_effcount").text(json.mmseffcount);
			 //提交状态
		 var substate = json.mmsubstate;
		 //发送状态
		 var sendstate = json.mmsendstate;
		//是否有 审批记录
		var haveRecord = json.haveRecord;

		var firstshowname = json.firstshowname;
		var firstcondition = json.firstcondition;
		var secondshowname = json.secondshowname;
		var secondcondition = json.secondcondition;
		var isshow = json.isshow;
		
		$("#recordTable").empty();
		if(haveRecord == "1"){
			//审批记录
			var recordList = json.members;
			//var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>审核级别</td><td class='div_bd'>审核人</td><td class='div_bd'>审核结果</td><td class='div_bd'>审核意见</td><td class='div_bd'>审核时间</td></tr>";
			var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shjb")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shr")+"</td><td class='div_bd'>"
			+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shjg")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shyj")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shsj")+"</td></tr>";
			 for(var i= 0;i<recordList.length;i++){
					var mms_Rlevel = recordList[i].mmsRlevel;
					var mms_Reviname = recordList[i].mmsReviname;
					var mms_Exstate = recordList[i].mmsexstate;
					var mms_Comments = recordList[i].mmsComments;
					var mms_rtime = recordList[i].mmsrtime;
					msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+mms_Rlevel+"</td> "
	        		+"  <td align='center'  width='15%'  class='div_bd'>"+mms_Reviname+"</td>"          
			        +"  <td align='center'  width='15%'  class='div_bd'>"+mms_Exstate+"</td> "
			        +"  <td align='left'  width='35%'  style='word-break: break-all;'  class='div_bd'>";
			        var view_Comments = mms_Comments.length>15?(mms_Comments.substr(0,15)+"..."):mms_Comments;
			        msg=msg+"<a onclick='javascript:modify(this)' style='cursor: pointer;'><label style='display:none'><xmp>"+mms_Comments+"</xmp></label>"
					+"<xmp>"+view_Comments+"</xmp></a>"
			       +"</td>  <td align='center'  width='25%'  class='div_bd'>"+mms_rtime+"</td> </tr>" ;
			}
			$("#recordTable").html(msg);
			
			$('#recordTableDiv').show();	
		}else{
			
		}
		$("#nextrecordmgs").empty();
		if(isshow == "1"){
			//var recordmsg = " 本级未审批人员：&nbsp;" + firstshowname;
			var recordmsg = " "+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_bjwspry_mh")+"&nbsp;" + firstshowname;
			if(firstcondition == "1"){
				//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qbtgsx");
			}else if(firstcondition == "2"){
				//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qzyrspsx");
			}
			if(secondshowname != "" && secondcondition != ""){
				//recordmsg = recordmsg + "</br>下一级审批人员/机构：&nbsp;" + secondshowname;
				recordmsg = recordmsg + "</br>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_xyjspryjg_mh")+"&nbsp;" + secondshowname;
				if(secondcondition == "1"){
					//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qbtgsx");
				}else if(secondcondition == "2"){
					//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qzyrspsx");
				}
			}
			$("#nextrecordmgs").html(recordmsg);
			$('#nextrecordmgs').show();	
		}
		 $("#mmsdetailinfo").dialog("open");
	 });
}

function detail(t,i)
{
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	if(i==1)
	{
		//$('#detail').dialog('option','title','隶属机构');
		$('#detail').dialog('option','title',getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_dsjg"));
	}else if(i==2)
	{
		//$('#detail').dialog('option','title','主题');
		$('#detail').dialog('option','title',getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_zt"));
	}
	$('#detail').dialog('open');
}


$(document).ready(function() {
    getCt();
    getLoginInfo("#getloginUser");
	$("#toggleDiv").toggle(function() {
		$("#condition").hide();
		$(this).addClass("collapse");
	}, function() {
		$("#condition").show();
		$(this).removeClass("collapse");
	});
	$("#content tbody tr").hover(function() {
		$(this).addClass("hoverColor");
	}, function() {
		$(this).removeClass("hoverColor");
	});
	//彩信任务详细信息弹出框 
	$("#mmsdetailinfo").dialog({
		autoOpen: false,
		modal:true,
		//title:'详细信息', 
		title:getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_xxxx"), 
	//height:410,
		width:680,
		height: 'auto',
		minHeight:170,
		maxHeight:650,
		closeOnEscape: false,
		resizable:false,
		open:function(){
		    $("#state").css("visibility","hidden");
		    $("#sstate").css("visibility","hidden");
		},
		close:function(){
			$("#state").css("visibility","visible");
			$("#sstate").css("visibility","visible");
		}
	});	

	$('#detail').dialog({
			autoOpen: false,
			width:250,
		    height:200
		});
	
	var h = 520;
	if (navigator.appName == "Netscape")
    {
    	h = 520;
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
			$("#screen").empty();
			clearInterval(ttimer); 
			$("#state").css("visibility","visible");
			$("#sstate").css("visibility","visible");
		},
		open:function(){
		    $("#state").css("visibility","hidden");
		    $("#sstate").css("visibility","hidden");
		}
	});
	$("#reviewflowinfo").dialog({
		autoOpen: false,
		modal:true,
		//title:'待审批', 
		title:getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_dsp"), 
		width:880,
		height: 'auto',
		minHeight:170,
		maxHeight:750,
		closeOnEscape: false,
		resizable:false,
		open:function(){
		},
		close:function(){
		}
	});	
});



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
		//var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>审核级别</td><td class='div_bd'>审核人</td><td class='div_bd'>审核状态</td><td class='div_bd'>审核结果</td><td class='div_bd'>审核意见</td><td class='div_bd'>审核时间</td><td class='div_bd'>最近催办时间</td><td class='div_bd'>操作</td></tr>";
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shjb")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shr")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shzt")+"</td><td class='div_bd'>"
		+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shjg")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shyj")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_shsj")+"</td><td class='div_bd'>"
		+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_zjcbsj")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_cz")+"</td></tr>";
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
				        	//msg=msg+"<td align='center'  width='10%'  class='div_bd'><a onclick='javascript:remind("+frid+")' style='cursor: pointer;color:blue;'>催办</a></td>" +"</tr>" ;
						    msg=msg+"<td align='center'  width='10%'  class='div_bd'><a onclick='javascript:remind("+frid+")' style='cursor: pointer;color:blue;'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_cb")+"</a></td>" +"</tr>" ;
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
			//msg = msg +	"<tr><td colspan='8' align='center'  class='div_bd' height='24px'>无记录</td></tr>";
			msg = msg +	"<tr><td colspan='8' align='center'  class='div_bd' height='24px'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_wjl")+"</td></tr>";
		}
		$("#reviewTable").html(msg);
		
		$('#reviewTableDiv').show();	
		$("#nextreviewmgs").empty();
		if(isshow == "1"){
			var recordmsg = "";
				if(onelevel=="1"){
					//recordmsg=recordmsg+"1级"
					recordmsg=recordmsg+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_1j")
					if(onecondition == "1"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qbtgsx");
					}else if(onecondition == "2"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qzyrspsx");
					}
				}
				if(twolevel=="2"){
					//recordmsg=recordmsg+"</br>2级"
					recordmsg=recordmsg+"</br>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_2j")
					if(twocondition == "1"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qbtgsx");
					}else if(twocondition == "2"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qzyrspsx");
					}
				}
				if(threelevel=="3"){
					//recordmsg=recordmsg+"</br>3级"
					recordmsg=recordmsg+"</br>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_3j")
					if(threecondition == "1"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qbtgsx");
					}else if(threecondition == "2"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qzyrspsx");
					}
				}
				if(fourlevel=="4"){
					//recordmsg=recordmsg+"</br>4级"
					recordmsg=recordmsg+"</br>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_4j")
					if(fourcondition == "1"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qbtgsx");
					}else if(fourcondition == "2"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qzyrspsx");
					}
				}
				if(fivelevel=="5"){
					//recordmsg=recordmsg+"</br>5级"
					recordmsg=recordmsg+"</br>"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_5j")
					if(fivecondition == "1"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qbtgsx");
					}else if(fivecondition == "2"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_qzyrspsx");
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
					//alert("催办信息已发送！");
					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_cbxxyfs"));
				  }
				  else if(result=="getTaskFail")
				  {
				    //alert("获取发送任务信息失败！");
				    alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_hqfsrwxxsb"));
				  }
				  else if(result=="getDcTempFail")
				  {
					  //alert("获取模板信息失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_hqmbxxsb"));
				  }
				  else if(result=="getWxTempFail")
				  {
					  //alert("获取网讯模板信息失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_hqwgmbxxsb"));
				  }
				  else if(result=="noPhone")
				  {
					  //alert("审批人员没有设置手机号码！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_sprymyszsjhm"));
				  }
				  else if(result=="noContent")
				  {
					  //alert("生成催办短信内容失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_sccbdxnrsb"));
				  }
				  else if(result=="noAdmin")
				  {
					  //alert("获取admin失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_hqadminsb"));
				  }
				  else if(result=="noAgree")
				  {
					  //alert("获取同意指令失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_hqtyzlsb"));
				  }
				  else if(result=="noDisAgree")
				  {
					  //alert("获取不同意指令失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_hqbtyzlsb"));
				  }
				  else if(result=="noSP")
				  {
					  //alert("没有可用的SP账号！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_mykydspzh"));
				  }
				  else if(result=="noSubNo")
				  {
					  //alert("没有可用的尾号！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_mykydwh"));
				  }
				  else if(result=="noSpNumber")
				  {
					  //alert("没有可用的通道号！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_mykydtdh"));
				  }
				  else if(result=="validPhone")
				  {
					  //alert("审批人员手机号码格式非法！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_sprysjhmgsff"));
				  }
				  else if(result=="wgkoufeiFail")
				  {
					  //alert("运营商扣费失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_yyskfsb"));
				  }else{
					  //alert("发送催办短信失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_fsxxck_fscbdxsb"));
				  }
				}
			);
}






