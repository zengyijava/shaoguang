$(document).ready(function(){
$("#sub_update_app").click(function(){
		var aid = $.trim($("#aid").val());
		var appid=$.trim($("#appid").val());
		var secret=$.trim($("#secret").val());
		//公众帐号
		if(aid==null||aid==""){
			alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_1"));
			return false;
		}
		if(appid.length==0){
			alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_20"));
			return false;
		}
		if(!/^[a-zA-Z0-9]+$/.test(appid)){
			alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_26"));
			return false;
		}
		if(appid.length>64){
			alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_27"));
			return false;
		}
		if(secret.length==0){
			alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_22"));
			return false;
		}
		if(!/^[a-zA-Z0-9]+$/.test(secret)){
			alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_26"));
			return false;
		}
		if(secret.length>64){
			alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_27"));
			return false;
		}
		$.post("dfm_definemenu.htm?method=saveAppId",{
			aid:$("#aid").val(),
			appid:appid,
			secret:secret,
			isAsync:"yes"
			},function(data,textStatus){
				returnResult(data);
	});
})
$("#update_action").click(function(){
		var url=$.trim($("#url").val());
	  var pattern=/http(s)?:\/\/([\w-]+\.)+[\w-]+([\w-]*)?/;
	  if(url.length==0){
		  alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_28"));
		  return false;
	  }
	  if(!pattern.test(url)){
		  alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_29"));
		  return false;
	  }
	  if(url.length>200){
		  alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_30"));
		  return false;
	  }
	$.post("dfm_definemenu.htm?method=setAction",{
		mid:$(".c_selectedBg").attr('mid'),
		url:url,
		mtype:'2',
		isAsync:"yes",
		tid:null
		},function(data,textStatus){
			if(data.indexOf('@')>-1){
				data=data.replace('@','');
				$(".c_selectedBg").attr("mtype","2");
				$(".c_selectedBg").attr("murl",url);
				$(".c_selectedBg").attr("tid",'');
				selTid = '';
			}
			returnResult(data);
});
});
})

function returnResult(data){
	if(data=="outOfLogin"){
		alert(getJsLocaleMessage("weix","common_text_10"));
	}else{
		if(data==""||data==null){
		alert(getJsLocaleMessage("weix","common_text_11"));	
		}else{
		alert(data);
			}
		}
	}
//上移
function uporder(obj){
  	var cur=$(obj).parents(".nav_item");
  	var up=cur.parent().prev().children(".nav_item");
	$.post("dfm_definemenu.htm?method=updateOrder",{
		mid1:cur.attr("mid"),
		mid2:up.attr("mid"),
		order:'1',
		isAsync:"yes"
		},function(data,textStatus){
			if(data.indexOf('@')>-1){
				  var onthis=$(obj).closest('div.nav_item_wrapper');
				  var getup=onthis.prev();
				  $(getup).before(onthis);
			}
			if(data.indexOf('#')>-1)
			{
			alert(data.replace('#',''));
			}
			if(data=="outOfLogin"){
				alert(getJsLocaleMessage("weix","common_text_10"));
			}
			});
}
//下移
function downorder(obj){
	var cur=$(obj).parents(".nav_item");
	var down=cur.parent().next().children(".nav_item");
	$.post("dfm_definemenu.htm?method=updateOrder",{
		mid1:cur.attr("mid"),
		mid2:down.attr("mid"),
		order:'0',
		isAsync:"yes"
	},function(data,textStatus){
		if(data.indexOf('@')>-1){
			  var onthis=$(obj).closest('div.nav_item_wrapper');
			  var getup=onthis.next();
			  $(getup).after(onthis);
		}
		if(data.indexOf('#')>-1)
		{
		alert(data.replace('#',''));
		}
		if(data=="outOfLogin"){
			alert(getJsLocaleMessage("weix","common_text_10"));
		}
	});
}
var selTid= "";
function menuClick(obj){
	$('.area_content').show();
	var pid=$(obj).attr("pid");
	if(pid!=0||(pid==0&&$(obj).siblings(".sub_nav_list").children("#level0_2_1").length==0)){
	var type=$(obj).attr("mtype");
	var url,tid;
	if(type==2){
		$('#tid').val('');
		$("#selDiv").html('');
		selTid='';
		$("#url").val($(obj).attr("murl"));
		  $('#link_transform').show().siblings().hide();
		  $('#backgo').css({'display':'block'});
	}
	if(type==1){
		$("#url").val('');
		$('#backgo').css({'display':'block'});
		$('#picDiv').show().siblings().hide();
		var tid = $('.c_selectedBg').attr('tid');
		$('#tid').val(tid);
		if(tid != "")
		{
			$.post("dfm_definemenu.htm",{method:"getTempName",tid:tid},function(result){
				var content = "\""+ result +"\"" + getJsLocaleMessage("weix","weix_qywx_zdycd_text_18");
				var html = getJsLocaleMessage("weix","weix_qywx_zdycd_text_19");
				html = html +content;
				$("#selDiv").html(html);
			});
			selTid = tid;
		}
	}
	if(type==0){
		$('#tid').val('');
		$("#selDiv").html('');
		$("#url").val('');
		selTid='';
		  $('#default_show').show().siblings().hide();
		  $("#backgo").hide();		
	}
	}else{
		$(".nav_tip_box").show().siblings().hide();
		$("#backgo").hide();
	}
	
}

function savePic()
{
	var tid = $("#tid").val();
	if(tid == "")
	{
		alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_31"));
		return;
	}
	if(tid == selTid)
	{
		alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_32"));
		return;
	}
	$("#savePicBtn").attr("disabled","disabled");
	$.post("dfm_definemenu.htm?method=setAction",{
		mid:$(".c_selectedBg").attr('mid'),
		url:null,
		mtype:'1',
		isAsync:"yes",
		tid:tid
		},function(data,textStatus){
			$("#savePicBtn").attr("disabled",false);
			if(data.indexOf('@')>-1){
				data=data.replace('@','');
				$(".c_selectedBg").attr("mtype","1");
				$(".c_selectedBg").attr("tid",tid);
				selTid = tid;
				$(".c_selectedBg").attr("murl",'');
			}
			returnResult(data);
	});
}