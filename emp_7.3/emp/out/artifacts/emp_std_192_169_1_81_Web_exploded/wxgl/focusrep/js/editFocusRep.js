function initRadioDiv2()
{
	var message = $("#tempName").val();
	$("#radioDiv2").empty();
	var content = "\""+ message +"\"" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_13")+"<a onclick='javascript:toPreview("+$("#tid").val()+")' style='cursor:pointer' title='" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_14") + "'>" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_15")+ "</a>"
	var html = "<td style=\"vertical-align: top;text-align: right;\">" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_26") + "</td>";
	html = html + "<td colspan=\"3\">"+content+"</td>"
	$("#radioDiv2").html(html);
}

//保存表单提交数据
function save()
{
	var AId=$("#AId").val();
	var tid=$("#tid").val();
	var title=$.trim($("#title").val());
	
	if(AId==null|| AId=="")
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_1"));
		return;
	}
	if(title==null|| title=="")
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_2"));
		return;
	}
	if(title.length>32)
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_3"));
		return;
	}
	
	var msgText=$("#msgText").val();
	if(msgText==undefined||msgText==null){
		msgText = "";
	}
	if($("#radio1:checked").size()==1){
		tid = "";
		if(msgText.length<=0){
			alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_4"));
			return;
		}
	}

	if($("#radio2:checked").size()==1){
		msgText = "";
		if(tid.length<=0){
			alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_5"));
			return;
		}
	}
	
	if(tid.length<=0 && msgText.length<=0)
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_6"));
		return;
	}
	else if(msgText.length>500)
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_7"));
		return;
	}
	
	$("input[type='button']").attr("disabled","disabled");
	$.post("weix_focusReply.htm",
			{method:"update",lgcorpcode:$("#lgcorpcode").val(),msgText:msgText,AId: AId,title:title,tid:tid,OpType:"edit",userid:$("#lguserid").val(),isAsync:"yes"},
	    	function(result){
    	    	if(result == "outOfLogin")
    	    	{
    	    		$("#logoutalert").val(1);
    	    		location.href= $("#pathUrl").val() + "/quit";
    	    		return;
    	    	}
    	    	if(result=="success")
	    		{
		    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_8"));
		    		var url =$("#pathUrl").val() + "/weix_focusReply.htm?method=find&lguserid="
			    	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
		    		doGo(url);
	    		}
    	    	else if(result=="noTemplate")
	    		{
	    			alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_9"));
	    		}
	    		else if(result=="fail")
	    		{
	    			alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_10"));
	    		}
	    		else
	    		{
		    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_11"))
	    		}
    	    	$("input[type='button']").attr("disabled","disabled");
    	    	$("#tid").val("");
    	    	doGo($("#pathUrl").val() + "/weix_focusReply.htm?method=find&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val());
	});
}

//返回上一级
function doreturn()
{
	var url =$("#pathUrl").val() + "/weix_focusReply.htm?method=find&lguserid="
	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
	doGo(url);
}

//点击选择模板
function chooseTemp()
{
	var tpath = $("#cpath").val();
	var frameSrc = $("#tempFrame").attr("src");
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var frameSrc = tpath+"/weix_defaultReply.htm?method=getTemplates&dsflag=1&lguserid="
		+lguserid+"&lgcorpcode="+lgcorpcode;
	var aboutConfig={
			content:getIframe(frameSrc,780,400,'choiceFrame'),	
	        title: getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_12"),
	        lock: true,
	        opacity: 0.5,
	        width:750,
	        height:400,
	        ok: function(){
//					var iframe = this.iframe.contentWindow;
					var iframe = $("#choiceFrame")[0].contentWindow;
					if(!iframe.document.body){
						alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_13"));
			        	return false;
			        };
			        var form = iframe.document.getElementById('pageForm');
					var checked = $(form).find("input[name='checklist']:checked");
					if(checked&&checked.size()>0){
							//将模板id赋值给隐藏域
							var tid = checked.val();
							var message = checked.next("xmp").text();
							
							$("#tid").val(tid);
							$("#radioDiv2").empty();
							var content = "\""+ message +"\"" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_13")+"<a onclick='javascript:toPreview("+tid+")' style='cursor:pointer' title='" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_14") + "'>" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_15")+ "</a>"
							var html = "<td style=\"vertical-align: top;text-align: right;\">" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_26") + "</td>";

							html = html + "<td colspan=\"3\">"+content+"</td>"
							$("#radioDiv2").html(html);
					}else{
						alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_12"));
						return false;
					}
				},
				cancel: true
	    };
	dlog = art.dialog(aboutConfig);
	setTimeout(function(){$(".aui_content").css("padding","5");},200);
}
function getIframe(src,width,height,frameid)
{
	return '<iframe id="'+frameid+'" src = "'+src+'" frameborder="0" allowtransparency="true" style="width: '
		+width+'px; height: '+height+'px; border: 0px none;"></iframe>';
}

//浏览效果
function toPreview(tempId){
	url = $("#pathUrl").val() + "/weix_keywordReply.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
	/*$("#msgPreviewFrame2").attr("src",url);
	dlog = art.dialog.through({
		title: "预览",
	    content: document.getElementById('divBox2'),
	    id: 'divBox2',
	    lock: true
	});*/
	showbox({src:url});
}

//弹出框的预览效果
function preview(tempId){
	url = $("#pathUrl").val() + "/weix_keywordReply.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
	/*$("#msgPreviewFrame2").attr("src",url);
	dlog = art.dialog.through({
		title: "预览",
	    content: document.getElementById('divBox2'),
	    id: 'divBox2',
	    lock: true
	});*/
	showbox({src:url});
}

//回复类型选择
function selectTemp(value){
	var tp = parseInt(value);
	if(tp==1){
		$("#radioDiv1").hide();
		$("#radioDiv2").show();
		$("#chooseTemp").show().css("display","inline");
	}else{
		$("#radioDiv1").show();
		$("#radioDiv2").hide();
		$("#chooseTemp").hide();
		if($("#msgText").val().length<=0)
		{
			$("#text_title").show();
		}
	}
}

//去掉匹配的字符
function secapp(obj,reg){
	var val=$(obj).val();
	if(reg.test(val)){
		$(obj).val($(obj).val().replace(reg,''));
	}
}

$(document).ready(function() {
	getLoginInfo("#hiddenValueDiv");

	//选择手工编辑/引用模板
	if($("#radioType").val()=="2"){
		initRadioDiv2();
		$("#radio2").click();
		$("#msgText").val("");
	}else{
		$("#radio1").click();
		$("#radioDiv2").empty();
	}

	//限制输入的字符个数
	$("#msgText").manhuaInputLetter({				       
		len : 500,//限制输入的字符个数				       
		showId : "sid",//显示剩余字符文本标签的ID
		showNum: $("#tempTextSize").val()
	});
	
	
	$("#title").bind('keyup blur',function(){
		secapp(this,/[<'">]/g);
	})
});