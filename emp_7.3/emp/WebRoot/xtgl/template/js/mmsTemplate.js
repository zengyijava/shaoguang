function delsel(obj)
{
	if(obj == 0)
	{
		if (navigator.appName == "Netscape")
        {
			$("#img").attr("value","");
        }
		else
		{
			document.getElementById("img").select();
			document.selection.clear();
		}
		$("#imgs").show();
		$("#inimgs").hide();
	}
	else if(obj == 1)
	{
		if (navigator.appName == "Netscape")
        {
			$("#img").attr("value","");
        }
		else
		{
			document.getElementById("img").select();
		document.selection.clear();
		}
		$("#imgs").hide();
		if ($("#inimg").val() != "")
		{
			$("#inimgs").show();
		}
	}
	else if(obj == 2)
	{
		if (navigator.appName == "Netscape")
        {
			$("#music").attr("value","");
        }
		else
		{
			document.getElementById("music").select();
		document.selection.clear();
		}
		$("#musics").hide();
		if ($("#inmic").val() != "")
		{
			$("#inmics").show();
		}
	}
	else if(obj == 3)
	{
		if (navigator.appName == "Netscape")
        {
			$("#music").attr("value","");
        }
		else
		{
			document.getElementById("music").select();
		document.selection.clear();
		}
		$("#musics").show();
		$("#inmics").hide();
	}
}
function check(){
	var theme = $.trim($("#theme").val());
    var tmMsg = $.trim($("#tmMsg").val());
    var imgs = $.trim($("#img").val());
    var musics = $.trim($("#music").val());
    var Max_size = parseFloat(50*1024);
    
    if(imgs != ""){
    	imgs = imgs.toString().substring( imgs.toString().lastIndexOf(".")+1,imgs.toString().length);
    	imgs = imgs.toUpperCase();
    }
    
    if (theme == "")
    {
        alert("主题不能为空！");
        return false;
    }else if (tmMsg.length > 100)
    {
        alert("文本内容超过100字！");
        return false;
    }else if (tmMsg == "" && imgs == "" && musics == "")
    {
        alert("无模板内容！");
        return false;
    }else if (imgs !="" && (imgs != 'JPG' ||imgs != 'GIF' ||  imgs != 'PNG' ||  imgs != 'JPEG'))
    {
        alert("不支持的图片格式！");
        return false;
    }else if (imgs != "" && !sizecheck("img"))
    {
    	alert("图片大小超过最大值50KB！");
        return false;
    }else if (musics != "" && !(/^(mid|mp3|wav)$/.test(musics.toString().substring((musics.toString().lastIndexOf("."))+1))))
    {
        alert("不支持的声音格式！");
        return false;
    }
    else if (musics != "" && !sizecheck("music"))
    {
    	alert("声音大小超过最大值50KB！");
        return false;
    }
    var isize = parseFloat(document.getElementById("imgsize").value);
    var msize = parseFloat(document.getElementById("musicsize").value);
    if (isize+msize>Max_size)
    {
    	alert("模板总大小超过最大值50KB！");
        return false;
    }
    return true;
}

function sizecheck(name)
{
       var MAXSIZE = 50 * 1024;
       var obj = document.getElementById(name);
       var image = new Image();
       image.dynsrc= obj.value;         
       var filesize=image.fileSize; 
       var flag = false;
       if ($.browser.msie) {//查看是否是IE
       		if(obj.readyState == "complete") {
       			if (filesize <= MAXSIZE) {
       				flag = true;
       				document.getElementById(name+"size").value = filesize;
       			}
       		}
       	} else {
       		var file = $("input:file[name='"+name+"']")[0];

       		if (file.files[0].fileSize <= MAXSIZE) {
       			flag = true;
       			document.getElementById(name+"size").value = file.files[0].fileSize;
       		}
       	}
       	return flag
}

function doview()
{
	if(check())
	{
		var theme = $.trim($("#theme").val());
		var dsflag = $("#dsflag").attr("value")== "1"?"动态模板":"静态模板";
	    var tmMsg = $.trim($("#tmMsg").val());
	    var imgs = $.trim($("#img").val());
	    var musics = $.trim($("#music").val());
	    var tmState = $("input[name='tmState'][checked]").val()=="1"?"启用":"停用";
		$("#view").css("display","block");
		$('#view').dialog({
			modal:true,
			buttons:[{
				text:'确定',
				handler:function(){
				$('#view').dialog('close');
				}
			},{
				text:'取消',
				handler:function(){
					$('#view').dialog('close');
				}
			}]
		});
		$("#theme2").empty();
		$("#dsflag2").empty();
		$("#tmState2").empty();
		$("#textview").empty();
		$("#theme2").append(theme);
		$("#dsflag2").append(dsflag);
		$("#tmState2").append(tmState);
		$("#textview").append(tmMsg);
		if (imgs != "")
		{
			$("#imgview").empty();
			$("#imgview").append("<img src='"+"file:///"+imgs+"' />")
		}
		$('#view').dialog('open');
	}
}

function del(i){
	if(confirm("您确定要删除该条信息吗？"))
	{
		$.post("tem_mmsTemplate.htm?method=delete",{ids:i,lgcorpcode:$("#lgcorpcode").val()},function(result){
			if(result>0)
			{
				alert("删除成功！");
				location.href = location;
			}else{
				alert("删除失败！");
			}
		});
	}
}

function delAll(){
	var items = "";
	$('input[name="checklist"]:checked').each(function(){	
		items += $(this).val()+",";
	});
	if (items != "")
	{
		items = items.toString().substring(0, items.lastIndexOf(','));
	}
	if(items==null || items==""){
		alert("请选择您要删除的模板！");
	}else{
	if(confirm("您确定要删除选择的模板?")==true){
				$.post("tem_mmsTemplate.htm?method=delete",{ids:items},function(result){
					if(result>0)
					{
						alert("删除成功,共删除"+result+"条信息！");
						window.location.href=location;
					}else{
						alert("删除失败！");
					}
				});
				
	}
	}
}
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



function checkAlls(e,str)    
{  
		var a = document.getElementsByName(str);    
		var n = a.length;    
		for (var i=0; i<n; i=i+1)    
			a[i].checked =e.checked;    
}
//点击“查看”的方法    
function doPreview(msg,dsFlag)
{
	inits();
	$.post("tem_mmsTemplate.htm?method=getTmMsg",{tmUrl:msg},function(result){
		if (result != null && result != "null" && result != "")
		{
			arr = result.split(">");
			if(arr[0] != null && arr[0] != "")
			{
				var da = $.parseJSON(arr[0]);
				ttime = (da.totaltime/1000);
			}
			index = 1;
			parmCount =null;
			$("#screen").empty();
			$("#pointer").empty();
			$("#nextpage").empty();
			$("#currentpage").empty();
			$("#inputParamCnt1").empty();
			/*
        	 $("#screen").html("<center style='padding-top:120px'><img src='<%=inheritPath%>/images/play.png' style='cursor:pointer;width:48;height:48' title='播放' onclick='javascript:play();'/></center>");
			 
			$("select[name='userId']").hide();
			$("select[name='rState']").hide();
			$("select[name='state']").hide();
			$("select[name='dsFlag']").hide();
			$("select[name='auditStatus']").hide();
			$("select[name='submitstatus']").hide();*/
			$("#tempView").dialog("open");
			play(dsFlag);
		}
		else
		{
             alert("内容文件不存在，无法预览！");
		}
	});
}

function chState(id,t)
{
    if(confirm("确定修改状态吗？"))
    {
        $.post("tem_mmsTemplate.htm?method=changeState",{id:id,t:t},function(result){
            if(result != null && result == "true")
            {
              alert("修改成功！");
              location.href = location;
            }
            else
            {
            	 alert("修改失败！");
            }
        });
    }
}
function doExport(u)
{
	$.post("tem_mmsTemplate.htm?method=checkMmsFile", {
		url : u
	},function(result) {
			if (result == "true") {
				location.href="tem_mmsTemplate.htm?method=exportTms&u="+u;
			} else if (result == "false")
				alert("文件不存在");
			else
				alert("出现异常,无法跳转");
	});
}
function doCopy(tmId,tmUrl,opType){
	$.post("tem_mmsTemplate.htm?method=checkMmsFile", {
		url : tmUrl
	},
		function(result) {
			if (result == "true") {
				var lgcorpcode= $("#lgcorpcode").val();
				location.href="tem_mmsTemplate.htm?method=doCopy&tmId="+tmId+"&opType="+opType+"&lgcorpcode="+lgcorpcode;
			} else if (result == "false"){
			    if(opType == "copy"){
			    	alert("文件不存在，无法复制！");
			    }else{
			    	alert("文件不存在，无法编辑！");
			    }
			}else{
				alert("出现异常，不能进行操作");
			}
	});
}
function doUp(){
	    var tms = $.trim($("#chooseTms").val());
	if (tms == ""){
		alert("请选择要上传的彩信文件！");
		return ;
	}else {
		tms = tms.substring(tms.lastIndexOf(".")+1,tms.length);
    	if(tms != "tms" && tms != "TMS"){
    		alert("不支持的彩信格式！");
	        return ;
    	}
    }

   var lguserid= $("#lguserid").val();
   var lgcorpcode= $("#lgcorpcode").val();
   var data = {lguserid:lguserid,lgcorpcode:lgcorpcode};
	   $.ajaxFileUpload ({ 
	    url:'tem_mmsTemplate.htm?method=uploadTms&lgcorpcode='+lgcorpcode, //处理上传文件的服务端 
	    secureuri:false, //是否启用安全提交，默认为false
	    data:data,
	    fileElementId:'chooseTms', //与页面处理代码中file相对应的ID值
	    dataType: 'text', //返回数据类型:text，xml，json，html,scritp,jsonp五种
	    success: function (result) { 
	        if(result == "typeNotMatch"){
	        	alert("导入模板中图片或音频格式不支持！");
	        }else if(result == "true"){
	        	alert("导入模板成功！");
	        	location.href = "tem_mmsTemplate.htm?method=find&lgguid="+$("#lgguid").val();
	        }else{
	        	alert("导入模板失败！");
	        }
	    }
	});
}
function detail(t,i){
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	if(i==1){
		$('#detail').dialog('option','title','模板ID');
	}else if(i==2){
		$('#detail').dialog('option','title','模板名称');
	}else if(i==3){
		$('#detail').dialog('option','title','所属机构');
	}
	$('#detail').dialog('open');
}

function doAdd()
{
   window.location.href='tem_mmsTemplate.htm?method=doAdd&lgcorpcode='+$("#lgcorpcode").val();
}


	//模板审批详情
function opentmpAudmsg(tmpid){
	 var pathUrl = $("#pathUrl").val();
	 var lguserid = $("#lguserid").val();
	 $.post(pathUrl+"/tem_mmsTemplate.htm?method=getMmsTplDetail",{tmpid:tmpid,lguserid:lguserid},function(jsonObject){
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
		$("#mmsdetailinfo").dialog("open");
	 });
   
}
