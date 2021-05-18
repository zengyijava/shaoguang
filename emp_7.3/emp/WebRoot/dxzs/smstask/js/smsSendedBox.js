$(document).ready(function() {
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
    $('#search').click(function(){submitForm();});	
	$('#modify').dialog({
		autoOpen: false,
		width:300,
	    height:300
	});
	
	//短信任务详细信息弹出框 
	$("#smsdetailinfo").dialog({
		autoOpen: false,
		modal:true,
		title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_215'), 
		width:680,
		height: 'auto',
		minHeight:170,
		maxHeight:650,
		closeOnEscape: false,
		resizable:false,
		open:function(){
		},
		close:function(){
		}
	});	
	//短信任务详细信息弹出框 
	$("#reviewflowinfo").dialog({
		autoOpen: false,
		modal:true,
		title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_216'), 
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

function modify(t,state)
{
	if(state=="1")
	{
		$('#modify').dialog("option","title",getJsLocaleMessage('dxzs','dxzs_ssend_alert_217'));
	}
	else if(state=="2")
	{
		$('#modify').dialog("option","title",getJsLocaleMessage('dxzs','dxzs_ssend_alert_218'));
	}
	$("#msgcont").empty();
	//用label显示短信内容
	$("#msgcont").text($(t).children("label").children("xmp").text());
	//修改成用textarea显示短信内容
	//$("#msgcont").val($(t).children("textarea").val());
	$('#modify').dialog('open');
}

//撤销任务
function cancelTimer(mtId)
{	  
	if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_219')))
	{
		var htmName=$("#htmName").val();
	    var lgcorpcode =$("#lgcorpcode").val();
	    var lguserid =$("#lguserid").val();
		$.post(htmName,
				{
					method : "checkCancel",
					mtId:mtId,
					lgcorpcode:lgcorpcode,
					lguserid:lguserid
				},
				function(result)
				{
				  if(result=="true")
				  {
					$.post(htmName,
					{
						method : "changeState",
						mtId:mtId,
						subState : "3",
						lgcorpcode:lgcorpcode,
					    lguserid:lguserid
					},
					function(result)
					{
					  
						if(result=="cancelSuccess")
						{
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_220'));										
							$("#s"+mtId).html("<font color='#FE3E4D'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_221')+"</font>");
							$("#cz"+mtId).html("-");
							getCt();
							//location.reload();
							//location.href = location.href;
							document.forms['pageForm'].submit();
						}else
						{
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_222'));
						}
					}
				   );
				  }
				  else
				  {
				    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_223'));
				  }	
				}
			);
	}
}

//点详细，弹出框
function openSmsDetail(mtId,userId){
	var htmName=$("#htmName").val();
	var pathUrl = $("#pathUrl").val();
	$('#recordTableDiv').hide();
	$.post(htmName+"?method=getSmsDetail",{mtId:mtId,userId:userId},function(jsonObject){
		 var json = eval("("+jsonObject+")");
		//是否有 审批记录
		var haveRecord = json.haveRecord;

		var firstshowname = json.firstshowname;
		var firstcondition = json.firstcondition;
		var secondshowname = json.secondshowname;
		var secondcondition = json.secondcondition;
		var isshow = json.isshow;
		$("#recordTable").empty();
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_224')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_225')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_226')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_227')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_228')+"</td></tr>";
		if(haveRecord == "1"){
			//审批记录
			var recordList = json.members;
			 for(var i= 0;i<recordList.length;i++){
					var sms_Rlevel = recordList[i].mmsRlevel;
					var sms_Reviname = recordList[i].mmsReviname;
					var sms_Exstate = recordList[i].mmsexstate;
					var sms_Comments = recordList[i].mmsComments;
					var sms_rtime = recordList[i].mmsrtime;
					msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+sms_Rlevel+"</td> "
	        		+"  <td align='center'  width='15%'  class='div_bd'>"+sms_Reviname+"</td>"          
			        +"  <td align='center'  width='15%'  class='div_bd'>"+sms_Exstate+"</td> "
			        +"  <td align='left'  width='35%'  style='word-break: break-all;'  class='div_bd'>";

			        var view_sms_Comments=sms_Comments.length>17?(sms_Comments.substr(0,17)+"..."):sms_Comments;
			        msg=msg+"<a onclick='javascript:modify(this,2)' style='cursor: pointer;'><label style='display:none'><xmp>"+sms_Comments+"</xmp></label>"
					+"<xmp>"+view_sms_Comments+"</xmp></a>"
			        +"</td> <td align='center'  width='25%'  class='div_bd'>"+sms_rtime+"</td>  </tr>" ;
			}
		}else{
			msg = msg +	"<tr><td colspan='5' align='center'  class='div_bd' height='24px'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_234')+"</td></tr>";
		}
		$("#recordTable").html(msg);
		
		$('#recordTableDiv').show();	
		$("#nextrecordmgs").empty();
		if(isshow == "1"){
			var recordmsg = getJsLocaleMessage('dxzs','dxzs_ssend_alert_229')+"：&nbsp;" + firstshowname;
			if(firstcondition == "1"){
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_230');
			}else if(firstcondition == "2"){
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_231');
			}
			if(secondshowname != "" && secondcondition != ""){
				recordmsg = recordmsg + "</br>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_232')+"：&nbsp;" + secondshowname;
				if(secondcondition == "1"){
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_230');
				}else if(secondcondition == "2"){
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_231');
				}
			}
			$("#nextrecordmgs").html(recordmsg);
			$('#nextrecordmgs').show();	
		}
		 $("#smsdetailinfo").dialog("open");
	 });
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
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_224')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_225')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_250')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_226')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_227')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_228')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_251')+"</td><td class='div_bd'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_252')+"</td></tr>";
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
				        	msg=msg+"<td align='center'  width='10%'  class='div_bd'><a onclick='javascript:remind("+frid+")' style='cursor: pointer;color:blue;'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_233')+"</a></td>" +"</tr>" ;
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
			msg = msg +	"<tr><td colspan='8' align='center'  class='div_bd' height='24px'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_234')+"</td></tr>";
		}
		$("#reviewTable").html(msg);
		
		$('#reviewTableDiv').show();	
		$("#nextreviewmgs").empty();
		if(isshow == "1"){
			var recordmsg = "";
				if(onelevel=="1"){
					recordmsg=recordmsg+"1"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_253')
					if(onecondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_230');
					}else if(onecondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_231');
					}
				}
				if(twolevel=="2"){
					recordmsg=recordmsg+"</br>2"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_253')
					if(twocondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_230');
					}else if(twocondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_231');
					}
				}
				if(threelevel=="3"){
					recordmsg=recordmsg+"</br>3"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_253')
					if(threecondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_230');
					}else if(threecondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_231');
					}
				}
				if(fourlevel=="4"){
					recordmsg=recordmsg+"</br>4"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_253')
					if(fourcondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_230');
					}else if(fourcondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_231');
					}
				}
				if(fivelevel=="5"){
					recordmsg=recordmsg+"</br>5"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_253')
					if(fivecondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_230');
					}else if(fivecondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_231');
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
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_235'));
				  }
				  else if(result=="getTaskFail")
				  {
				    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_236'));
				  }
				  else if(result=="getDcTempFail")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_237'));
				  }
				  else if(result=="getWxTempFail")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_238'));
				  }
				  else if(result=="noPhone")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_239'));
				  }
				  else if(result=="noContent")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_240'));
				  }
				  else if(result=="noAdmin")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_241'));
				  }
				  else if(result=="noAgree")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_242'));
				  }
				  else if(result=="noDisAgree")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_243'));
				  }
				  else if(result=="noSP")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_244'));
				  }
				  else if(result=="noSubNo")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_245'));
				  }
				  else if(result=="noSpNumber")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_246'));
				  }
				  else if(result=="validPhone")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_247'));
				  }
				  else if(result=="wgkoufeiFail")
				  {
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_248'));
				  }else{
					  alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_249'));
				  }
				}
			);
}