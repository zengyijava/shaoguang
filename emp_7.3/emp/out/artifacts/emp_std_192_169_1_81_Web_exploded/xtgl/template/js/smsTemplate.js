function showAddSmsTmp(showType)
{
	$("#addSmsTmpDiv").css("display","block");
	$("#addSmsTmpFrame").css("display","block");

	$("#addSmsTmpDiv").dialog({
		autoOpen: false,
		height:410,
		width: 620,
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
	$('#modify').dialog("option","title","模板内容");
}
else if(state=="2")
{
	$('#modify').dialog("option","title","审批意见");
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
			alert("修改成功！");
			$("#tempState"+i).empty();
			if(ks == 1)
			{
			    $("#tempState"+i).append("<option value='1' selected='selected'>已启用</option>");
			    $("#tempState"+i).append("<option value='0' >停用</option>");
			}
			else
			{
				$("#tempState"+i).append("<option value='0' selected='selected'>已停用</option>");
				$("#tempState"+i).append("<option value='1' >启用</option>");
			}
			changeEmpSelect($("#tempState"+i),80);
		}else{
			alert("修改失败！");
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
				var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>审核级别</td><td class='div_bd'>审核人</td><td class='div_bd'>审核结果</td><td class='div_bd'>审核意见</td><td class='div_bd'>审核时间</td></tr>";
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
					msg = msg + "<tr class='div_bd' height='24px'> <td  align='center' class='div_bd' colspan='5'>无记录</td></tr>"
				}
				$("#recordTable").html(msg);
				$('#recordTableDiv').show();	
				$("#nextrecordmgs").empty();
				if(isshow == "1"){
					var recordmsg = " 本级未审批人员：&nbsp;" + firstshowname;
					if(firstcondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
					}else if(firstcondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
					}
					if(secondshowname != "" && secondcondition != ""){
						recordmsg = recordmsg + "</br>下一级审批人员/机构：&nbsp;" + secondshowname;
						if(secondcondition == "1"){
							recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						}else if(secondcondition == "2"){
							recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						}
					}
					$("#nextrecordmgs").html(recordmsg);
					$('#nextrecordmgs').show();	
				}
				$("#smsdetailinfo").dialog("open");
			 });
		   
	   }