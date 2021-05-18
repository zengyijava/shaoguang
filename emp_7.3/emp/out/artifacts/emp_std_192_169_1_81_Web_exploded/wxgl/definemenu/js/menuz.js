$(document).ready(function(){
  //设置url
  $('#url_setting').click(function(){
  if($(".c_selectedBg").length==0){
	  alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_1"));
	  return false;
  }
  $('#link_transform').show().siblings().hide();
  $('#backgo').show();
	 
  });
  
  //设置图文
  $('#article_setting').click(function(){
	  if($(".c_selectedBg").length==0){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_1"));
		  return false;
	  }
	  if($(".c_selectedBg").attr("tid")=="" || $(".c_selectedBg").attr("tid") == 0)
	  {
		  chooseTemp();
	  }
	//显示图文
	$('#picDiv').show().siblings().hide();
	$('#default_show').hide();
	$('#backgo').show();
	  
  });
  //设置在线客服
  $("#zxkfModel_setting").click(function(){
	  if($(".c_selectedBg").length==0){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_1"));
		  return false;
	  }
	  $('#zxkf_transform').show().siblings().hide();
	  $('#backgo').show();
  });

  //设置lbs采集点
  $("#lbsModel_setting").click(function(){
	  if($(".c_selectedBg").length==0){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_1"));
		  return false;
	  }
	  $('#lbs_transform').show().siblings().hide();
	  $('#backgo').show();
  });

  //设置抽奖活动
  $("#choujiangModel_setting").click(function(){
	  if($(".c_selectedBg").length==0){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_1"));
		  return false;
	  }
	  if($(".c_selectedBg").attr("tid")=="" || $(".c_selectedBg").attr("tid") == 0)
	  {
		  chooseChoujiang();
	  }
	  $('#choujiangDiv').show().siblings().hide();
	  $('#backgo').show();
  });
  
  //设置微站
  $("#wzglModel_setting").click(function(){
	  if($(".c_selectedBg").length==0){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_1"));
		  return false;
	  }
	  if($(".c_selectedBg").attr("tid")=="" || $(".c_selectedBg").attr("tid") == 0)
	  {
		  chooseSite();
	  }
	  $('#wzglDiv').show().siblings().hide();
	  $('#backgo').show();
  });
  
  //选择表单
  $("#formModel_setting").click(function(){
	  if($(".c_selectedBg").length==0){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_1"));
		  return false;
	  }
	  if($(".c_selectedBg").attr("tid")=="" || $(".c_selectedBg").attr("tid") == 0)
	  {
		  chooseForm();
	  }
	  $('#formDiv').show().siblings().hide();
	  $('#backgo').show();
  });
  
//设置菜单关联-连接地址
$("#update_action_url").click(function(){
	  var url=$.trim($("#url").val());
	  var pattern=/http(s)?:\/\/([\w-])+([\w-]*)?/;
	  if(url.length==0){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_2"));
		  return false;
	  }
	  if(!pattern.test(url)){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_3"));
		  return false;
	  }
	  if(url.length>200){
		  alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_4"));
		  return false;
	  }
	$.post("weix_defineMenu.htm?method=setAction",{
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
			}
			returnResult(data);
});
});

//设置菜单关联-在线客服
$("#update_action_zxkfModule").click(function(){
	var checked  = $("#zxkfModule").attr("checked");
	
	if(checked=="checked"){
		$.post("weix_defineMenu.htm?method=setAction",{
			mid:$(".c_selectedBg").attr('mid'),
			mtype:'3',
			isAsync:"yes",
			tid:null
			},function(data,textStatus){
				if(data.indexOf('@')>-1){
					data=data.replace('@','');
					$(".c_selectedBg").attr("mtype","3");
					$(".c_selectedBg").attr("murl",'');
					$(".c_selectedBg").attr("tid",'');
				}
				returnResult(data);
	    });
	}else{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_5"));
	}
});

//设置菜单关联-LBS采集点查询
$("#update_action_lbsModule").click(function(){
	var checked  = $("#lbsModule").attr("checked");
	
	if(checked=="checked"){
		$.post("weix_defineMenu.htm?method=setAction",{
			mid:$(".c_selectedBg").attr('mid'),
			mtype:'4',
			isAsync:"yes",
			tid:null
			},function(data,textStatus){
				if(data.indexOf('@')>-1){
					data=data.replace('@','');
					$(".c_selectedBg").attr("mtype","4");
					$(".c_selectedBg").attr("murl",'');
					$(".c_selectedBg").attr("tid",'');
				}
				returnResult(data);
	    });
	}else{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_5"));
	}
});

//设置菜单关联-图文
$("#update_action_temp").click(function(){
	var tid = $("#tid").val();
	var selTid = $("#selTid").val();
	
	if(tid == "")
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_6"));
		return;
	}
	if(tid == selTid)
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_7"));
		return;
	}
	$("#update_action_Temp").attr("disabled",true);
	$.post("weix_defineMenu.htm?method=setAction",{
		mid:$(".c_selectedBg").attr('mid'),
		url:null,
		mtype:'1',
		isAsync:"yes",
		tid:tid
		},function(data,textStatus){
			$("#update_action_Temp").attr("disabled",false);
			if(data.indexOf('@')>-1){
				data=data.replace('@','');
				$(".c_selectedBg").attr("mtype","1");
				$(".c_selectedBg").attr("tid",tid);
				$("#selTid").val(tid);
				$(".c_selectedBg").attr("murl",'');
			}
			returnResult(data);
	});
});

//设置菜单管理-微站
$("#update_action_wzgl").click(function(){
	var tid = $("#siteid").val();
	var selTid = $("#siteSelTid").val();
	var appId = $("#aid").find("option:selected").attr("data-id");
	if(tid == "")
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_8"));
		return;
	}
	if(tid == selTid)
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_9"));
		return;
	}
	$("#update_action_wzgl").attr("disabled",true);
	$.post("weix_defineMenu.htm?method=setAction",{
		mid:$(".c_selectedBg").attr('mid'),
		url:null,
		mtype:'5',
		isAsync:"yes",
		tid:tid,
		appid:appId
		},function(data,textStatus){
			$("#update_action_wzgl").attr("disabled",false);
			if(data.indexOf('@')>-1){
				data=data.replace('@','');
				$(".c_selectedBg").attr("mtype","5");
				$(".c_selectedBg").attr("tid",tid);
				$("#siteSelTid").val(tid);
				$(".c_selectedBg").attr("murl",'');
			}
			returnResult(data);
	});
});

//设置菜单管理-抽奖
$("#update_action_chouJiang").click(function(){
	var tid = $("#choujiangid").val();
	var selTid = $("#choujiangSelTid").val();
	if(tid == "")
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_10"));
		return;
	}
	if(tid == selTid)
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_11"));
		return;
	}
	$("#update_action_chouJiang").attr("disabled",true);
	$.post("weix_defineMenu.htm?method=setAction",{
		mid:$(".c_selectedBg").attr('mid'),
		url:null,
		mtype:'6',
		isAsync:"yes",
		tid:tid
		},function(data,textStatus){
			$("#update_action_chouJiang").attr("disabled",false);
			if(data.indexOf('@')>-1){
				data=data.replace('@','');
				$(".c_selectedBg").attr("mtype","6");
				$(".c_selectedBg").attr("tid",tid);
				$("#choujiangSelTid").val(tid);
				$(".c_selectedBg").attr("murl",'');
			}
			returnResult(data);
	});
});

//设置菜单关联-表单
$("#update_action_form").click(function(){
	var tid = $("#formid").val();
	var selTid = $("#formSelTid").val();
	var appId = $("#aid").find("option:selected").attr("data-id");
	if(tid == "")
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_12"));
		return;
	}
	if(tid == selTid)
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_13"));
		return;
	}
	
	$("#update_action_form").attr("disabled",true);
	$.post("weix_defineMenu.htm?method=setAction",{
		mid:$(".c_selectedBg").attr('mid'),
		url:null,
		mtype:'7',
		isAsync:"yes",
		tid:tid,
		appid:appId
		},function(data,textStatus){
			$("#update_action_form").attr("disabled",false);
			if(data.indexOf('@')>-1){
				data=data.replace('@','');
				$(".c_selectedBg").attr("mtype","7");
				$(".c_selectedBg").attr("tid",tid);
				$("#formSelTid").val(tid);
				$(".c_selectedBg").attr("murl",'');
			}
			returnResult(data);
	});
});

});

//菜单操作-点击
function menuClick(obj){
	$('.area_content').show();
	var pid=$(obj).attr("pid");
	if(pid!=0||(pid==0&&$(obj).siblings(".sub_nav_list").children("#level0_2_1").length==0)){
		var mtype=$(obj).attr("mtype");
		var pathUrl = $("#pathUrl").val();
		var url,tid;
		//选择图文
		if(mtype==1){
			reset_hidden_teansfrom("1");
			$('#backgo').css({'display':'block'});
			$('#picDiv').show().siblings().hide();
			var tid = $('.c_selectedBg').attr('tid');
			$('#tid').val(tid);
			if(tid != "")
			{
				$.post(pathUrl + "/weix_defineMenu.htm",{method:"getTempName",tid:tid},function(result){
					if(result!=null&&result!=""){
						var content = "\""+ result +"\"" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_13")+"<a style='color:blue;cursor:pointer' onclick='javascript:toPreview()' title='" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_14") + "'>" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_15")+ "</a>"
						
						var html = getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_50");
						html = html +content;
						$("#selDiv").html(html);
						$("#selTid").val(tid);
					}
				});
				
			}
			return false;
		}
		
		//选择连接
		if(mtype==2){
			reset_hidden_teansfrom("2");
			$("#url").val($(obj).attr("murl"));
			$('#link_transform').show().siblings().hide();
			$('#backgo').css({'display':'block'});
			return false;
		}
		
		//在线客服
		if(mtype==3){
			reset_hidden_teansfrom("3");
			$('#zxkfModule').attr("checked",true);
			$('#zxkf_transform').show().siblings().hide();
			$('#backgo').css({'display':'block'});
			return false;
		}
		
		//LBS采集点查询
		if(mtype==4){
			reset_hidden_teansfrom("4");
			$('#lbsModule').attr("checked",true);
			$('#lbs_transform').show().siblings().hide();
			$('#backgo').css({'display':'block'});
			return false;
		}
		
		//微站
		if(mtype==5){
			reset_hidden_teansfrom("5");
			$('#backgo').css({'display':'block'});
			$('#wzglDiv').show().siblings().hide();
			var tid = $('.c_selectedBg').attr('tid');
			$('#siteid').val(tid);
			if(tid != "")
			{
				$.post(pathUrl + "/wzgl_siteManager.htm",{method:"getSiteInfo",siteid:tid},function(result){
					var siteObj = $.parseJSON(result);
					if(siteObj.sid!=undefined&&siteObj.name!=undefined&&siteObj.url!=undefined){
						var tid = siteObj.sid;
						var url = siteObj.url;
			        	var name = siteObj.name;
			        	
			        	//将当前选择的微站id赋值给隐藏域
						$("#siteid").val(tid);
						var message = $.trim(name).substr(0,10);
						var content = '\"'+ message +'\"' + getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_14")+
						'<a style="color:blue;cursor:pointer" onclick="javascript:doPreview(\''+url+'\')" title=\'' + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_14") + "'>" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_15")+ "</a>";
						var html = getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_16");
						
						html = html +content;
						
						console.log(html);
						$("#siteSelDiv").html(html);
						$('#siteSelTid').val(tid);
					}
				});
			}
			return false;
		}
		  
		//抽奖活动
		if(mtype==6){
			reset_hidden_teansfrom("6");
			$('#backgo').css({'display':'block'});
			$('#choujiangDiv').show().siblings().hide();
			var tid = $('.c_selectedBg').attr('tid');
			$('#choujiangid').val(tid);
			if(tid != "")
			{
				$.post(pathUrl + "/yxgl_lotteryMgr.htm",{method:"getSweepInfo",sweepId:tid},function(result){
					var sweepObj = $.parseJSON(result);
					if(sweepObj.sweepid!=undefined&&sweepObj.name!=undefined){
						var tid = sweepObj.sweepid;
			        	var name = sweepObj.name;
			        	
						var message = $.trim(name).substr(0,10);
						var content = getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_16") + message +'...\"' +getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_17");
						$("#choujiangSelDiv").html(content);
						$("#choujiangSelTid").val(tid);
					}
				});
			}
			return false;
		}
		
		//表单
		if(mtype==7){
			reset_hidden_teansfrom("7");
			$('#backgo').css({'display':'block'});
			$('#formDiv').show().siblings().hide();
			var tid = $('.c_selectedBg').attr('tid');
			$('#formid').val(tid);
			if(tid != "")
			{
				$.post(pathUrl + "/wzgl_formManager.htm",{method:"getFormInfo",formid:tid},function(result){
					var formObj = $.parseJSON(result);
					if(formObj.formid!=undefined&&formObj.name!=undefined&&formObj.url!=undefined){
						var formid = formObj.formid;
						var url = formObj.url;
			        	var name = formObj.name;
			        	
						var message = $.trim(name).substr(0,10);
						var content = '\"'+ message +'...\"' + getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_14")+'<a style="color:blue;cursor:pointer" onclick="javascript:doPreview(\''+url+'\')" title="' + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_14") + "'>" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_15")+ "</a>";
						var html = getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_16");
						html = html +content;
						$("#formSelDiv").html(html);
						$('#formSelTid').val(tid);
					}
				});
			}
			return false;
		}
		
		//未选择
		if(mtype==0){
			reset_hidden_teansfrom("0");
		    $('#default_show').show().siblings().hide();
		    $("#backgo").hide();
		    return false;
		}
	}else{
		$(".nav_tip_box").show().siblings().hide();
		$("#backgo").hide();
	}
}

/**选择图文--start--**/
//图文-弹出框
function chooseTemp()
{
	
	var pathUrl = $("#pathUrl").val();
	var src=pathUrl + "/weix_defaultReply.htm?method=getTemplates&" +"dsflag=1&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
	var aboutConfig = {
		title: getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_18"),
		content:getIframe(src,780,400,'choiceFrame'),
	    id: 'tempDiv',
	    opacity: 0.5,
	    padding:0,
	    lock: true,
	    ok: function(){
		
		var iframe = $("#choiceFrame")[0].contentWindow;
		if(!iframe.document.body){
			alert(getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_23"));
        	return false;
        };
        var form = iframe.document.getElementById('pageForm');
		var checked = $(form).find("input[name='checklist']:checked");
			if(checked.val() == undefined)
			{
				alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_12"));
				return false;
			}else
			{
				//将模板id赋值给隐藏域
				$("#tid").val(checked.val());
				var message = checked.next("xmp").text();
				var content = "\""+ message +"\"" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_13")	+"<a style='color:blue;cursor:pointer' onclick='javascript:toPreview()' title='" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_14") + "'>" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_15")+ "</a>"
				var html = getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_50");
				html = html +content;
				$("#selDiv").html(html);
				$("#selMsgText").val(message);
			}
		},
		cancel: true
	};
	dlog = art.dialog(aboutConfig);
	setTimeout(function(){$(".aui_content").css("padding","0");},200);
}
function getIframe(src,width,height,frameid)
{
	return '<iframe id="'+frameid+'" src = "'+src+'" frameborder="0" allowtransparency="true" style="width: '
		+width+'px; height: '+height+'px; border: 0px none;"></iframe>';
}
//图文-取消选择模板
function cancelTemp(){
	 if(($("#tid").val() == "" || $("#tid").val()==0))
	  {
		 $('#default_show').show().siblings().hide();
		 $('#backgo').hide();
	  }
	 closeDialog();
}

//图文-浏览效果
function toPreview(){
	var pathUrl = $("#pathUrl").val();
	var tempId = $("#tid").val();
	var url = pathUrl + "/weix_keywordReply.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
	/*$("#msgPreviewFrame2").attr("src",url);
	
	dlog = art.dialog.through({
		title: "选择模板",
	    content: document.getElementById('divBox2'),
	    id: 'divBox2',
	    padding:0,
	    lock: true
	});*/
	showbox({src:url});
}
/**关联图文--end--**/

/**选择抽奖活动 --start--**/
//选择抽奖
function chooseChoujiang(){
	var lgcorpcode = $("#lgcorpcode").val();
	var url = $("#pathUrl").val() + "/yxgl_lotteryMgr.htm?isArtDialog=true&lgcorpcode="+lgcorpcode;
	art.dialog.open(url,{
		title: getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_19"),
		width: "720px",
		height: "450px",
		lock: true,
		background: "#000",
	    opacity: 0.5,
	    ok: function(){
			var iframe = this.iframe.contentWindow;
			if(!iframe.document.body){
				alert(getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_23"))
	        	return false;
	        };
	        var form = iframe.document.getElementById('pageForm');
	        var checked = $(form).find("input[name='checklist']:checked");
	        if(checked&&checked.size()>0){
	        	var tid = checked.val();
	        	var name = checked.parent().siblings(":eq(0)").text();
	        	
				//将当前选择的抽奖活动id赋值给隐藏域
				$("#choujiangid").val(tid);
				var message = $.trim(name).substr(0,10);
				var content = getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_16") + '"' + message +'\"' + getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_17");
				$("#choujiangSelDiv").html(content);
				return true;
	        }else{
	        	alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_5"));
	        	return false;
	        }
			
		},
		cancel: true
	});
}
/**选择抽奖活动 --end--**/

/**选择微站 --start--**/
//选择微站
function chooseSite(){
	var lgcorpcode = $("#lgcorpcode").val();
	var src = $("#pathUrl").val() + "/wzgl_siteManager.htm?isArtDialog=true&lgcorpcode="+lgcorpcode;
	
	var aboutConfig ={
		title: getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_51"),
		content:getIframe(src,750,400,'wxchoiceFrame'),
		lock: true,
	    opacity: 0.5,
	    ok: function(){
			var iframe = $("#wxchoiceFrame")[0].contentWindow;
			if(!iframe.document.body){
				alert(getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_23"));
	        	return false;
	        };
	        var form = iframe.document.getElementById('pageForm');
			var checked = $(form).find("input[name='checklist']:checked");
			if(checked&&checked.size()>0){
				var tid = checked.val();
				var url = checked.next().val();
	        	var name = checked.parent().siblings(":eq(0)").text();
	        	
	        	//将当前选择的微站id赋值给隐藏域
				$("#siteid").val(tid);
				var message = $.trim(name).substr(0,10);
				var content = '\"'+ message +'\"' + getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_14")+
				'<a style="color:blue;cursor:pointer" onclick="javascript:doPreview(\''+url+'\')" title=\'' + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_14") + "'>" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_15")+ "</a>";
				var html = getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_16");
				html = html +content;
				$("#siteSelDiv").html(html);
				return true;
			}else{
				alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_5"));
				return false;
			}
		},
		cancel: true
	};
	dlog = art.dialog(aboutConfig);
	setTimeout(function(){$(".aui_content").css("padding","5");},200);
	
}

//选择表单
function chooseForm(){
	var lgcorpcode = $("#lgcorpcode").val();
	var url = $("#pathUrl").val() + "/wzgl_formManager.htm?isArtDialog=true&lgcorpcode="+lgcorpcode;
	art.dialog.open(url,{
		title: getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_16"),
		width: "720px",
		height: "450px",
		lock: true,
		background: "#000",
	    opacity: 0.5,
	    ok: function(){
			var iframe = this.iframe.contentWindow;
			if(!iframe.document.body){
				alert(getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_23"))
	        	return false;
	        };
	        var form = iframe.document.getElementById('pageForm');
			var checked = $(form).find("input[name='checklist']:checked");
			if(checked&&checked.size()>0){
				var tid = checked.val();
				var url = checked.next().val();
				var name = checked.parent().siblings(":eq(1)").find("a").text();
	        	//将当前选择的表单id赋值给隐藏域
				$("#formid").val(tid);
				var message = $.trim(name).substr(0,10);
				var content = '\"'+ message +'\"' + getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_52")+
				'<a style="color:blue;cursor:pointer" onclick="javascript:doPreview(\''+url+'\')" title="' + 
				getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_14") + "'>" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_15")+ "</a>";
				var html = getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_16");
				html = html +content;
				$("#formSelDiv").html(html);
				return true;
			}else{
				alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_5"));
				return false;
			}
		},
		cancel: true
	});
}

//微站预览
function doPreview(urlToken,from){
	url = $("#pathUrl").val() + "/wzgl_sitePreview.hts?from=pc&urlToken="+urlToken;
	/*$("#msgPreviewFrame").attr("src",url);
	 dlog = art.dialog.through({
			title: "预览",
		    content: document.getElementById('divPreBox'),
		    id: 'divPreBox',
		    lock: true
	  });*/
	showbox({src:url,mode:1});
}

//表单预览
function toPreviewForm(filePath){
	var url = $("#pathUrl").val() +"/" + filePath;
	/*$("#formPreviewFrame3").attr("src",url);
	 dlog = art.dialog.through({
			title: "预览",
		    content: document.getElementById('divPreBox3'),
		    id: 'divPreBox3',
		    lock: true
	  });*/
	showbox({src:url});
}

/**选择微站 --end--**/

//重置隐藏域中的值
function reset_hidden_teansfrom(mtype){
	if(mtype=="1"){
	   //图文
		reset_link_transform();
		reset_zxkf_transform();
		reset_lbs_transform();
		reset_wzgl_transform();
		reset_chouJiang_transform();
		reset_form_transform();
	}else if(mtype=="2"){
	   //URL状态
		reset_temp_transform();
		reset_zxkf_transform();
		reset_lbs_transform();
		reset_wzgl_transform();
		reset_chouJiang_transform();
		reset_form_transform();
	}else if(mtype=="3"){
	   //在线客服
		reset_temp_transform();
		reset_link_transform();
		reset_lbs_transform();
		reset_wzgl_transform();
		reset_chouJiang_transform();
		reset_form_transform();
	}else if(mtype=="4"){
	   //LBS网点查询
		reset_temp_transform();
		reset_link_transform();
		reset_zxkf_transform();
		reset_wzgl_transform();
		reset_chouJiang_transform();
		reset_form_transform();
	}else if(mtype=="5"){
	   //微站
		reset_temp_transform();
		reset_link_transform();
		reset_zxkf_transform();
		reset_lbs_transform();
		reset_chouJiang_transform();
		reset_form_transform();
	}else if(mtype=="6"){
	   //抽奖
		reset_temp_transform();
		reset_link_transform();
		reset_zxkf_transform();
		reset_lbs_transform();
		reset_wzgl_transform();
		reset_form_transform();
	}else if(mtype=="7"){
	   //表单
		reset_temp_transform();
		reset_link_transform();
		reset_zxkf_transform();
		reset_lbs_transform();
		reset_wzgl_transform();
		reset_chouJiang_transform();
	}else{
		reset_temp_transform();
		reset_link_transform();
		reset_zxkf_transform();
		reset_lbs_transform();
		reset_wzgl_transform();
		reset_chouJiang_transform();
		reset_form_transform();
	}
}
//重置设置图文状态-1
function reset_temp_transform(){
	$("#tid").val("");
	$("#selTid").val("");
	$("#selDiv").html("");
}

//重置设置URL状态-2
function reset_link_transform(){
	$("#url").val("");
}

//重置在线客服状态-3
function reset_zxkf_transform(){
	$("#zxkfModule").attr("checked",false);
}

//重置设置LBS网点查询状态-4
function reset_lbs_transform(){
	$("#lbsModule").attr("checked",false);
}

//重置微站状态-5
function reset_wzgl_transform(){
	$("#siteid").val("");
	$("#siteSelTid").val("");
	$("#siteSelDiv").html("");
}

//重置设置抽奖活动状态-6
function reset_chouJiang_transform(){
	$("#choujiangid").val("");
	$("#choujiangSelTid").val("");
	$("#choujiangSelDiv").html("");
}

//重置设置表单状态-7
function reset_form_transform(){
	$("#formid").val("");
	$("#formSelTid").val("");
	$("#formSelDiv").html("");
}

//操作异常处理
function returnResult(data){
	//console.log("data:" + data);
	if(data=="outOfLogin"){
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_20"));
	}else{
		if(data=="null！"||data==null){
			alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_21"));	
		}else{
			alert(data);
		}
	}
}