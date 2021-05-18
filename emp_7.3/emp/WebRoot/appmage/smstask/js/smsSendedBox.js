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
		width:250,
	    height:200
	});
	
	//短信任务详细信息弹出框 
	$("#smsdetailinfo").dialog({
		autoOpen: false,
		modal:true,
		title:getJsLocaleMessage('appmage','appmage_js_smsSendedBox_auditdetail'), // '审批详情', 
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
});

function modify(t,state)
{
	if(state=="1")
	{
		// $('#modify').dialog("option","title","信息内容");
		$('#modify').dialog("option","title",getJsLocaleMessage('appmage','appmage_js_smsSendedBox_smscontent'));
	}
	else if(state=="2")
	{
		//$('#modify').dialog("option","title","审批意见");
		$('#modify').dialog("option","title",getJsLocaleMessage('appmage','appmage_js_smsSendedBox_auditdtips'));
	}
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	$('#modify').dialog('open');
}

//撤销任务
function cancelTimer(mtId)
{	  
	if(confirm(getJsLocaleMessage('appmage','appmage_js_smsSendedBox_revoktask')))
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
							// alert("撤销短信任务成功！");		
							alert(getJsLocaleMessage('appmage','appmage_js_smsSendedBox_revoktasksuc'));
							$("#s"+mtId).html("<font color='#FE3E4D'>" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_hasrevoked') + "</font>");
							$("#cz"+mtId).html("-");
							getCt();
							//location.reload();
							//location.href = location.href;
							document.forms['pageForm'].submit();
						}else
						{
							//alert("撤销任务失败！");
							alert(getJsLocaleMessage('appmage','appmage_js_smsSendedBox_revoktaskfalse'));
						}
					}
				   );
				  }
				  else
				  {
				    //alert("该任务不满足撤销条件，不允许撤销！");
					  alert(getJsLocaleMessage('appmage','appmage_js_smsSendedBox_cannotrevok'));
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
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_auditlevel') + "</td><td class='div_bd'>" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_auditer') + "</td><td class='div_bd'>" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_auditdresult') + "</td><td class='div_bd'>" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_auditopinion') + "</td><td class='div_bd'>" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_audittime') + "</td></tr>";
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
			msg = msg +	"<tr><td colspan='5' align='center'  class='div_bd' height='24px'>" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_noauditdata') + "</td></tr>";
		}
		$("#recordTable").html(msg);
		
		$('#recordTableDiv').show();	
		$("#nextrecordmgs").empty();
		if(isshow == "1"){
			var recordmsg = getJsLocaleMessage('appmage','appmage_js_smsSendedBox_noauditprploe') + " ：&nbsp;" + firstshowname;
			if(firstcondition == "1"){
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_allaudit') ;
			}else if(firstcondition == "2"){
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_oneaudit') ;
			}
			if(secondshowname != "" && secondcondition != ""){
				recordmsg = recordmsg + "</br>" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_nextauditdept') + "：&nbsp;" + secondshowname;
				if(secondcondition == "1"){
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_allaudit') ;
				}else if(secondcondition == "2"){
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;" + getJsLocaleMessage('appmage','appmage_js_smsSendedBox_oneaudit') ;
				}
			}
			$("#nextrecordmgs").html(recordmsg);
			$('#nextrecordmgs').show();	
		}
		 $("#smsdetailinfo").dialog("open");
	 });
}	