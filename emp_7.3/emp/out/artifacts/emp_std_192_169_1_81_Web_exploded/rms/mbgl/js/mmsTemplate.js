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
        //alert("主题不能为空！");
        alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_ztbnwk"));
        return false;
    }else if (tmMsg.length > 100)
    {
        //alert("文本内容超过100字！");
        alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_wbnrcg100z"));
        return false;
    }else if (tmMsg == "" && imgs == "" && musics == "")
    {
        //alert("无模板内容！");
        alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_wmbnr"));
        return false;
    }else if (imgs !="" && (imgs != 'JPG' ||　imgs != 'GIF' ||  imgs != 'PNG' ||  imgs != 'JPEG'))
    {
        //alert("不支持的图片格式！");
        alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_bzcdtpgs"));
        return false;
    }else if (imgs != "" && !sizecheck("img"))
    {
    	//alert("图片大小超过最大值50KB！");
    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_tpdxcgzdz50kb"));
        return false;
    }else if (musics != "" && !(/^(mid|mp3|wav)$/.test(musics.toString().substring((musics.toString().lastIndexOf("."))+1))))
    {
        //alert("不支持的声音格式！");
        alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_bzcdsygs"));
        return false;
    }
    else if (musics != "" && !sizecheck("music"))
    {
    	//alert("声音大小超过最大值50KB！");
    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_sydxcgzdz50kb"));
        return false;
    }
    var isize = parseFloat(document.getElementById("imgsize").value);
    var msize = parseFloat(document.getElementById("musicsize").value);
    if (isize+msize>Max_size)
    {
    	//alert("模板总大小超过最大值50KB！");
    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_mbzdxcgzdz50kb"));
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
		//var dsflag = $("#dsflag").attr("value")== "1"?"动态模板":"静态模板";
		var dsflag = $("#dsflag").attr("value")== "1"?getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_dtmb"):getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_jtmb");
	    var tmMsg = $.trim($("#tmMsg").val());
	    var imgs = $.trim($("#img").val());
	    var musics = $.trim($("#music").val());
	    //var tmState = $("input[name='tmState'][checked]").val()=="1"?"启用":"停用";
	    var tmState = $("input[name='tmState'][checked]").val()=="1"?getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qy"):getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_ty");
		$("#view").css("display","block");
		$('#view').dialog({
			modal:true,
			buttons:[{
				//text:'确定',
				text:getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qd"),
				handler:function(){
				$('#view').dialog('close');
				}
			},{
				//text:'取消',
				text:getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qx"),
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

function del(path,i){
	//if(confirm("您确定要删除该条信息吗？"))
	if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_nqdyscgtxxm")))
	{
		$.post("mbgl_mytemplate.htm?method=delete",{ids:i,lgcorpcode:$("#lgcorpcode").val()},function(result){
			if(result>0)
			{
				//alert("删除成功！");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_sccg"));
				
				var url = path+'/mbgl_mytemplate.htm';
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
				//location.href = location;
			}else{
				//alert("删除失败！");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_scsb_1"));
			}
		});
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
	if(items==null || items==""){
		//alert("请选择您要删除的模板！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qxznyscdmb"));
	}else{
	//if(confirm("您确定要删除选择的模板?")==true){
	if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_nqdyscxzdmb"))==true){
				$.post("mbgl_mytemplate.htm?method=delete",{ids:items},function(result){
					if(result>0)
					{
						//alert("删除成功,共删除"+result+"条信息！");
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_sccggsc")+result+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_txx"));
						
						var url = path+'/mbgl_mytemplate.htm';
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
						
					}else{
						//alert("删除失败！");
						alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_scsb_1"));
					}
				});
				
	}
	}
}


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


		$(function(){
			  $('#state,#submitstatus,#rState,#dsFlag,#auditStatus').isSearchSelect({'width':'180','isInput':false});
			  
				
				//新增共享模板DIV
				$("#shareTmpDiv").dialog({
					autoOpen: false,
					height:480,
					width: 520,
					resizable:false,
					modal: true
				});

		 });
	    $(document).ready(function() {
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
		    $("#modelTms").dialog({				//添加声音的dialog
				autoOpen: false,
				title:getJsLocaleMessage("rms","rms_myscene_importtpl"),//"导入富信文件",
				//title:getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_drcxwj"),
				height:200,
				width:365
			});
			$('#detail').dialog({
				autoOpen: false,
				width:250,
			    height:200
			});


			//彩信任务详细信息弹出框 
			$("#mmsdetailinfo").dialog({
				autoOpen: false,
				modal:true,
				//title:'详细信息', 
				title:getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_xxxx"), 
				width:680,
				height: 'auto',
				minHeight:180,
				maxHeight:650,
				closeOnEscape: false,
				resizable:false,
				open:function(){
				},
				close:function(){
				}
			});	
			$("#reviewflowinfo").dialog({
				autoOpen: false,
				modal:true,
				//title:'待审批', 
				title:getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_dsp"), 
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


function chState(id,t)
{
    //if(confirm("确定修改状态吗？"))
    if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qdxgztm")))
    {
    	 var pathUrl = $("#pathUrl").val();
        $.post(pathUrl+"/mbgl_mytemplate.htm?method=changeState",{id:id,t:t},function(result){
            if(result != null && result == "true")
            {
              //alert("修改成功！");
              alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_xgcg"));
              $("#pageForm").find("#search").click();
            }
            else
            {
            	 //alert("修改失败！");
            	 alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_xgsb"));
            }
        });
    }
}
function doExport(u)
{
	 var pathUrl = $("#pathUrl").val();
	 $.post(pathUrl+"/mbgl_mytemplate.htm?method=checkMmsFile", {
		url : u
	},function(result) {
			if (result == "true") {
				download_href(pathUrl+"/mbgl_mytemplate.htm?method=exportRms&u="+u);
			} else if (result == "false")
				//alert("文件不存在");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_wjbcz"));
			else
				//alert("出现异常,无法跳转");
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_cxycwftz"));
	});
}
//编辑
//function doCopy(tmId,tmUrl,opType){
//	 var pathUrl = $("#pathUrl").val();
//	 $.post(pathUrl+"/mbgl_mytemplate.htm?method=checkMmsFile", {
//		url : tmUrl
//	 },
//		function(result) {
//			if (result == "true") {
//				var lgcorpcode= $("#lgcorpcode").val();
//				location.href=pathUrl+"/mbgl_mytemplate.htm?method=doCopy&tmId="+tmId+"&opType="+opType+"&lgcorpcode="+lgcorpcode;
//			} else if (result == "false"){
//			    if(opType == "copy"){
//			    	//alert("文件不存在，无法复制！");
//			    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_wjbczwffz"));
//			    }else{
//			    	//alert("文件不存在，无法编辑！");
//			    	alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_wjbczwfbj"));
//			    }
//			}else{
//				//alert("出现异常，不能进行操作");
//				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_cxycbnjxcz"));
//			}
//	});
//}
///------ 导入富信模板

function detail(t,i){
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	if(i==1){
		//$('#detail').dialog('option','title','模板ID');
		$('#detail').dialog('option','title',getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_mbid"));
	}else if(i==2){
		//$('#detail').dialog('option','title','模板名称');
		$('#detail').dialog('option','title',getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_mbmc"));
	}else if(i==3){
		//$('#detail').dialog('option','title','所属机构');
		$('#detail').dialog('option','title',getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_ssjg"));
	}
	$('#detail').dialog('open');
}

function doAdd()
{
 //1静态彩信  2动态彩信  3新增
	 var pathUrl = $("#pathUrl").val();
   window.location.href=pathUrl+'/mbgl_mytemplate.htm?method=doAdd&type=3&lgcorpcode='+$("#lgcorpcode").val();
}


//模板审批详情
function opentmpAudmsg(tmpid){
	 var pathUrl = $("#pathUrl").val();
	 var lguserid = $("#lguserid").val();
	 $.post(pathUrl+"/mbgl_mytemplate.htm?method=getMmsTplDetail",{tmpid:tmpid,lguserid:lguserid},function(jsonObject){
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
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shjb")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shr")+"</td><td class='div_bd'>"
		+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shjg")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shyj")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shsj")+"</td></tr>";
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
			//msg = msg + "<tr class='div_bd' height='24px'> <td  align='center' class='div_bd' colspan='5'>无记录</td></tr>"
			msg = msg + "<tr class='div_bd' height='24px'> <td  align='center' class='div_bd' colspan='5'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_wjl")+"</td></tr>"
		}
		$("#recordTable").html(msg);
		$('#recordTableDiv').show();	
		$("#nextrecordmgs").empty();
		if(isshow == "1"){
			//var recordmsg = " 本级未审批人员：&nbsp;" + firstshowname;
			var recordmsg = " "+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_bjwspry")+"&nbsp;" + firstshowname;
			if(firstcondition == "1"){
				//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qbtgsx");
			}else if(firstcondition == "2"){
				//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
				recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qzyrspsx");
			}
			if(secondshowname != "" && secondcondition != ""){
				//recordmsg = recordmsg + "</br>下一级审批人员/机构：&nbsp;" + secondshowname;
				recordmsg = recordmsg + "</br>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_xyjspryjg")+"&nbsp;" + secondshowname;
				if(secondcondition == "1"){
					//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qbtgsx");
				}else if(secondcondition == "2"){
					//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
					recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qzyrspsx");
				}
			}
			$("#nextrecordmgs").html(recordmsg);
			$('#nextrecordmgs').show();	
		}
		$("#mmsdetailinfo").dialog("open");
	 });
   
}
//共享模板操作
function showShareTmp(tmId, tmName,userId) {
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var iPath = $("#iPath").val();
	tmName=tmName||'-';
//	tmName=encodeURI(tmName);
//	tmName=encodeURI(tmName);
	var url = iPath+"/tem_shareTemplate.jsp?lguserid="+ lguserid + "&lgcorpcode=" + lgcorpcode+"&tempId="+tmId+"&userId="+userId;
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
	$.post(pathUrl + "/mbgl_mytemplate.htm", {
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
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_wlcsqcxdl"));
		} else if (returnmsg == "success") {
			//alert("设置模板共享成功！");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_szmbgxcg"));
			$("#shareTmpDiv").dialog("close");
		} else if (returnmsg == "fail") {
			//alert("设置模板共享失败！");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_szmbgxsb"));
		} else {
			//alert("操作失败！");
			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_czsb"));
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
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shjb")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shr")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shzt")+"</td><td class='div_bd'>"
		+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shjg")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shyj")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_shsj")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_zjcbsj")+"</td><td class='div_bd'>"
		+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_cz")+"</td></tr>";
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
					        msg=msg+"<td align='center'  width='10%'  class='div_bd'><a onclick='javascript:remind("+frid+")' style='cursor: pointer;color:blue;'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_cb")+"</a></td>" +"</tr>" ;
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
			msg = msg +	"<tr><td colspan='8' align='center'  class='div_bd' height='24px'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_wjl")+"</td></tr>";
		}
		$("#reviewTable").html(msg);
		
		$('#reviewTableDiv').show();	
		$("#nextreviewmgs").empty();
		if(isshow == "1"){
			var recordmsg = "";
				if(onelevel=="1"){
					//recordmsg=recordmsg+"1级"
					recordmsg=recordmsg+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_1j")
					if(onecondition == "1"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qbtgsx");
					}else if(onecondition == "2"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qzyrspsx");
					}
				}
				if(twolevel=="2"){
					//recordmsg=recordmsg+"</br>2级"
					recordmsg=recordmsg+"</br>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_2j")
					if(twocondition == "1"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qbtgsx");
					}else if(twocondition == "2"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qzyrspsx");
					}
				}
				if(threelevel=="3"){
					//recordmsg=recordmsg+"</br>3级"
					recordmsg=recordmsg+"</br>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_3j")
					if(threecondition == "1"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qbtgsx");
					}else if(threecondition == "2"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qzyrspsx");
					}
				}
				if(fourlevel=="4"){
					//recordmsg=recordmsg+"</br>4级"
					recordmsg=recordmsg+"</br>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_4j")
					if(fourcondition == "1"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;";+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qbtgsx")
					}else if(fourcondition == "2"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qzyrspsx");
					}
				}
				if(fivelevel=="5"){
					//recordmsg=recordmsg+"</br>5级"
					recordmsg=recordmsg+"</br>"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_5j")
					if(fivecondition == "1"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;全部通过生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;";+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qbtgsx")
					}else if(fivecondition == "2"){
						//recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;其中一人审批生效";
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qzyrspsx");
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
					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_cbxxyfs"));
				  }
				  else if(result=="getTaskFail")
				  {
				    //alert("获取发送任务信息失败！");
				    alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_hqfsrwxxsb"));
				  }
				  else if(result=="getDcTempFail")
				  {
					  //alert("获取模板信息失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_hqmbxxsb"));
				  }
				  else if(result=="getWxTempFail")
				  {
					  //alert("获取网讯模板信息失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_hqwxmbxxsb"));
				  }
				  else if(result=="noPhone")
				  {
					  //alert("审批人员没有设置手机号码！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_sprymyszsjhm"));
				  }
				  else if(result=="noContent")
				  {
					  //alert("生成催办短信内容失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_sccbdxnrsb"));
				  }
				  else if(result=="noAdmin")
				  {
					  //alert("获取admin失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_hqadminsb"));
				  }
				  else if(result=="noAgree")
				  {
					  //alert("获取同意指令失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_hqtyzlsb"));
				  }
				  else if(result=="noDisAgree")
				  {
					  //alert("获取不同意指令失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_hqbtyzlsb"));
				  }
				  else if(result=="noSP")
				  {
					  //alert("没有可用的SP账号！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_mykydspzh"));
				  }
				  else if(result=="noSubNo")
				  {
					  //alert("没有可用的尾号！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_mykydwh"));
				  }
				  else if(result=="noSpNumber")
				  {
					  //alert("没有可用的通道号！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_mykydtdh"));
				  }
				  else if(result=="validPhone")
				  {
					  //alert("审批人员手机号码格式非法！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_sprysjhmgsff"));
				  }
				  else if(result=="wgkoufeiFail")
				  {
					  //alert("运营商扣费失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_yyskfsb"));
				  }else{
					  //alert("发送催办短信失败！");
					  alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_fscbdxsb"));
				  }
				}
			);
}