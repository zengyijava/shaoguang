//删除
function del(sid){
	if(!confirm(getJsLocaleMessage('appmage','appmage_common_confiretodelete'))){
		return;
	}
	if(!sid){
		// alert('请求参数异常！');
		alert(getJsLocaleMessage('appmage','appmage_common_requestparamerror'));
		return;
	}
	var lgcorpcode = $('#lgcorpcode').val();
	$.ajax({
		type:"POST",
		url: "app_homeedit.htm",
		data: {method: "delete",sid:sid,lgcorpcode:lgcorpcode,time:new Date(),isAsync:"yes"},
		success: function(result){
			if(isOutOfLogin(result)){return;}
			if(result == 'true'){
				// alert("删除成功！");
				alert(getJsLocaleMessage('appmage','appmage_common_deletesuccess'));
				submitForm();
			}else{
				// alert("删除失败！");
				alert(getJsLocaleMessage('appmage','appmage_common_deletefalse'));
			}
		},
		error: function(){
			//alert('请求异常！');
			alert(getJsLocaleMessage('appmage','appmage_common_requesterror'));
		}
	});
}

function prev(sid){
	showPreview();
	var lgcorpcode = $('#lgcorpcode').val();
	if(sid&&lgcorpcode){
		$.ajax({
			type:"POST",
			url: "app_homeedit.htm",
			data: {method:'getJson',lgcorpcode:lgcorpcode,sid:sid},
			beforeSend: function(){},
			complete: function(){},
			success: function(result){
				$('#promo-nav').html('');
				var data = eval('('+result+')');
				var heads = data.heads;
				var lists = data.lists;
				$('#sliderBox ul').empty();
				var temp = [];
				for(var i=0;i<heads.length;i++){
					temp.push(getHeadTemplate(heads[i].pic,heads[i].thumb));
				}
				$('#sliderBox ul').html(temp.join(''));
				$('#sliderBox ul>li').each(function(i){
					$(this).find('a').attr('title',heads[i].title);
				});
				
				$('#newsList').empty();
				temp = [];
				for(var i=0;i<lists.length;i++){
					temp.push(getListTemplate(lists[i].pic,lists[i].thumb));
				}
				$('#newsList').html(temp.join(''));
				$('#newsList>li').each(function(i){
					$(this).find('.artTitle').html(escapelt(lists[i].title));
					$(this).find('.artContent').html(escapelt(lists[i].content));
				});
				
				$('#mobilePreview,#previewBg').show();
				new Mslider({
			        id : 'sliderBox',        //对应幻灯容器的ID名
			        auto : true,         //是否开启自动播放，默认不开启
			        nav : 'promo-nav',              //幻灯小导航的ID名
       				navEvent : 'click',             //小导航的每个li的触发事件，默认为 click 还可以为 mouseover
        			current : 'current', 
					isHidden : true,
					isText : true,                  //默认是否开启 文本容器  default = false
					text : 'slider-text' 
			    });
			}
		});
	}
}

//var pubstatus = {'1':'发布成功！','0':'APP平台未登录！','-1':'提交失败！','-2':'APP消息对象为空！'
//	,'-3':'首页发布对象ID为空！','-4':'更新app首页信息失败！','-5':'app首页信息对象为空！'
//	,'-6':'获取并更新app首页信息对象异常！','-8':'主页内容为空！','-99':'发布失败但保存成功！'};
//var cancelstatus ={'1':'取消发布成功！','0':'APP平台未登录！','-1':'提交失败！','-2':'APP消息对象为空！'
//	,'-3':'首页发布对象ID为空！','-5':'app首页信息对象为空！'
//		,'-30':'流水号为空！','-31':'获取和构建消息对象异常！'};

var pubstatus = {'1':getJsLocaleMessage('appmage','appmage_homelist_publicsuc'),'0':getJsLocaleMessage('appmage','appmage_homelist_appnologin'),'-1':getJsLocaleMessage('appmage','appmage_common_submissionfailed'),'-2':getJsLocaleMessage('appmage','appmage_homelist_appobjectisnull')
	,'-3':getJsLocaleMessage('appmage','appmage_homelist_appobjectidisnull'),'-4':getJsLocaleMessage('appmage','appmage_homelist_updateappfalse'),'-5':getJsLocaleMessage('appmage','appmage_homelist_appindexisnull')
	,'-6':getJsLocaleMessage('appmage','appmage_homelist_getappindexerror'),'-8':getJsLocaleMessage('appmage','appmage_homelist_indexisnull'),'-99':getJsLocaleMessage('appmage','appmage_homelist_publicfalsesavesuc')};
var cancelstatus ={'1':getJsLocaleMessage('appmage','appmage_homelist_cancelpublicsuc'),'0':getJsLocaleMessage('appmage','appmage_homelist_appnologin'),'-1':getJsLocaleMessage('appmage','appmage_common_submissionfailed'),'-2':getJsLocaleMessage('appmage','appmage_homelist_appobjectisnull')
	,'-3':getJsLocaleMessage('appmage','appmage_homelist_appobjectidisnull'),'-5':getJsLocaleMessage('appmage','appmage_homelist_appindexisnull')
		,'-30':getJsLocaleMessage('appmage','appmage_homelist_serialisnull'),'-31':getJsLocaleMessage('appmage','appmage_homelist_createobjecterror')};

//发布
function publish(sid){
	if(!confirm(getJsLocaleMessage('appmage','appmage_js_homelist_publicindex'))){
		return;
	}
	if(!sid){
		// alert('请求参数异常！');
		alert(getJsLocaleMessage('appmage','appmage_common_requestparamerror'));
		return;
	}
	var lgcorpcode = $('#lgcorpcode').val();
	$.ajax({
		type:"POST",
		url: "app_homeedit.htm",
		data: {method: "publish",sid:sid,lgcorpcode:lgcorpcode,time:new Date().getTime(),isAsync:"yes"},
		success: function(result){
			if(isOutOfLogin(result)){return;}
			if(result == 1){
				// alert("发布成功！");
				alert(getJsLocaleMessage('appmage','appmage_js_homelist_publicsuc'));
				submitForm();
			}else{
				//alert(pubstatus[result]||"发布失败！");
				alert(pubstatus[result]||getJsLocaleMessage('appmage','appmage_js_homelist_publicfalse'));
			}
		},
		error: function(){
			// alert('请求异常！');
			alert(getJsLocaleMessage('appmage','appmage_common_requesterror'));
		}
	});
}
//取消发布
function unpublish(sid){
	if(!confirm(getJsLocaleMessage('appmage','appmage_homelist_canceltoindex'))){
		return;
	}
	if(!sid){
		// alert('请求参数异常！');
		alert(getJsLocaleMessage('appmage','appmage_common_requestparamerror'));
		return;
	}
	var lgcorpcode = $('#lgcorpcode').val();
	$.ajax({
		type:"POST",
		url: "app_homeedit.htm",
		data: {method: "unpublish",sid:sid,lgcorpcode:lgcorpcode,time:new Date().getTime(),isAsync:"yes"},
		success: function(result){
			if(isOutOfLogin(result)){return;}
			if(result&&cancelstatus[result]){
				alert(cancelstatus[result]);
				if(result==1){
					submitForm();
				}
			}else{
				// alert("取消发布失败！");
				alert(getJsLocaleMessage('appmage','appmage_homelist_cancelfalse'));
			}
		},
		error: function(){
			// alert('请求异常！');
			alert(getJsLocaleMessage('appmage','appmage_common_requesterror'));
		}
	})
}
//0新建 1修改 2复制 操作
function opp(type,sid){
	var lguserid  = $('#lguserid').val();
	var lgcorpcode = $('#lgcorpcode').val();
	if(type == 0){
		location.href= "app_homeedit.htm?method=toAdd&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&type="+type;
	}else if((type == 1||type == 2)&&sid){
		location.href= "app_homeedit.htm?method=toUpdate&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&type="+type+"&sid="+sid;
	}else{
		// alert('请求参数异常！');
		alert(getJsLocaleMessage('appmage','appmage_common_requestparamerror'));
	}
	
	
}

function isOutOfLogin(result){
	if(result == "outOfLogin"||/\<html\>/i.test(result))
	{
		$("#logoutalert").val(1);
		location.href="common/logoutEmp.html";
		return true;
	}
	return false;
}


function showPreview(){
	var bg=$('#previewBg');
	if(bg.is(':visible')){
		var clientHeight=$(window).height();
		$('#previewBg').css({'height':clientHeight});
	}
}

function getHeadTemplate(pic,thumb){
	var time = new Date().getTime();
	var temp = [];
	temp.push('<li>');
	temp.push('<a href="javascript:void(0)" title=""><img src="'+pic+'?bak='+thumb+'&time='+time+'" alt="" class="thumb"></a>');
	temp.push('</li>');
	return temp.join('');
}

function getListTemplate(pic,thumb){
	var time = new Date().getTime();
	var temp = [];
	temp.push('<li class="modList">');
	temp.push('<p class="artTitle"></p>');
	temp.push('<p class="artContent"></p>');
	temp.push('<img width="50" src="'+pic+'?bak='+thumb+'&time='+time+'" class="thumb"></li>');
	return temp.join('');
}
//转换双引号
function escapequot(str){
	return (str||'').replace(/\"/g,'#quot;').replace(/&/g, '#amp;');
}
function unescapequot(str){
	return (str||'').replace(/#quot;/g,'"').replace(/#amp;/g, '&');
}
function getqtipcfg(val){
	return {
		   content: val,
		   show: 'mouseover',
		   hide: 'mouseout',
		   position: {  
                corner: {  
                    target: 'topMiddle',  
                    tooltip: 'bottomMiddle'  
                } 
		   },
           style: {
            border: {
               width: 5,
               radius: 10
            },
            padding: 10, 
            textAlign: 'center',
            tip: true
         }
		};
}

$(document).ready(function(){
	$(".qtip").each(function(){
		$(this).qtip(getqtipcfg(unescapequot($(this).attr('old'))));
	});
	$('td.edit_td').live('dblclick',function(){
		$('.edit_td').each(function(){
			$(this).html(escapelt(subStr(unescapequot($(this).attr('old')),18)));
		})
		var temp = [];
		temp.push('<input type="text" class="tdInput" value="" maxlength="25"/>');
		temp.push('&nbsp;&nbsp;<a class="sure">' + getJsLocaleMessage('appmage','appmage_common_ok') + '</a>&nbsp;<a class="cancel">' + getJsLocaleMessage('appmage','appmage_common_cancel') + '</a>');
		$(this).html(temp.join(''));
		$(this).find('.tdInput').val(unescapequot($(this).attr('old')));
	});
	
	$(".int").live('keyup blur',function(){
		var value=$(this).val();
		if(/[^\d]/.test(value)){
			value=value.replace(/[^\d]/g,'');
			$(this).val(value);
		}
	});
	$('.tdInput').live('blur',function(){
		var value = $(this).val();
		$(this).val($.trim(value));
	})
	
	$('.sure').live('click',function(){
		var $td = $(this).parents('.qtip');
		$td.qtip("destroy");
		var $button = $(this);
		var sid = $button.parents("tr").children("td:eq(0)").html();
		var lgcorpcode = $('#lgcorpcode').val();
		var name = $button.siblings(':text').val();
		if(name.length==0){
			// alert('首页名称不能为空！');
			alert(getJsLocaleMessage('appmage','appmage_homelist_indexnameisnull'));
			return;
		}
		$.ajax({
			type:"POST",
			url: "app_homeedit.htm",
			async : false,
			data: {method:"updateInfo",lgcorpcode:lgcorpcode,sid:sid,name:name},
			beforeSend: function(){$button.attr("disabled",true);},
			complete: function(){$button.attr("disabled",false);},
			success: function(result){
				if(result=='true'){
					$button.parents('td').attr('old',escapequot(name)).html(escapelt(subStr(name,18)));
				}else{
					// alert('修改首页名称失败！');
					alert(getJsLocaleMessage('appmage','appmage_homelist_updateindexnamefalse'));
				}
			}
			});
		$td.qtip(getqtipcfg(unescapequot($td.attr('old'))));
		
	});
	
	$('.cancel').live('click',function(){
		var $td = $(this).parents('td');
		$td.html(escapelt(subStr(unescapequot($td.attr('old')),18)));
	});
	
})

function subStr(str,maxlen){
		if(!str) return "";
		var len = str.length;
		if(len<=maxlen){
			return str;
		}else{
			return str.substr(0,15)+"...";
		}
	}
