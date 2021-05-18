var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
$(document).ready(function(){
	initUpload();
//	var pubstatus = {'1':'发布成功！','0':'APP平台未登录！','-1':'提交失败！','-2':'APP消息对象为空！'
//					,'-3':'首页发布对象ID为空！','-4':'更新app首页信息失败！','-5':'app首页信息对象为空！'
//					,'-6':'获取并更新app首页信息对象异常！','-8':'主页内容为空！','-99':'发布失败但保存成功！'};
	
	var pubstatus = {'1':getJsLocaleMessage('appmage','appmage_homelist_publicsuc'),'0':getJsLocaleMessage('appmage','appmage_homelist_appnologin'),'-1':getJsLocaleMessage('appmage','appmage_common_submissionfailed'),'-2':getJsLocaleMessage('appmage','appmage_homelist_appobjectisnull')
			,'-3':getJsLocaleMessage('appmage','appmage_homelist_appobjectidisnull'),'-4':getJsLocaleMessage('appmage','appmage_homelist_updateappfalse'),'-5':getJsLocaleMessage('appmage','appmage_homelist_appindexisnull')
			,'-6':getJsLocaleMessage('appmage','appmage_homelist_getappindexerror'),'-8':getJsLocaleMessage('appmage','appmage_homelist_indexisnull'),'-99':getJsLocaleMessage('appmage','appmage_homelist_publicfalsesavesuc')};
	
	getLoginInfo("#loginInfo");
	var sid = $('#sid').val();
	var lgcorpcode = $('#lgcorpcode').val();
	// 1 修改  2 复制
	var type = $('#oppType').val();
	if(sid&&lgcorpcode){
		$.ajax({
			type:"POST",
			url: "app_homeedit.htm",
			data: {method:'getJson',lgcorpcode:lgcorpcode,sid:sid},
			beforeSend: function(){if(type == 2){$('#sid').val('');}},
			complete: function(){},
			success: function(result){
				var data = eval('('+result+')');
				$('#infoname').val(data.infoname);
				$('#infoname').trigger("focus").trigger("blur");
				if(!data.heads){alert(getJsLocaleMessage('appmage','appmage_js_home_getdataerror'));}
				while($('#heads .piclist').size()<data.heads.length){
					$('#addPic').click();
				}
				while($('#newsList li').size()-1>data.lists.length){
					//$('#newsList li').eq(0).find('.delete').click();
					$('#newsList li').eq($('#newsList li').size()-2).remove();
				}
				while($('#newsList li').size()-1<data.lists.length){
					$('#addNewsMod .addNews').click();
				}
				$('#heads .piclist').each(function(index){
					$(this).attr('data-config',JSON.stringify(data.heads[index]));
					$(this).find('.pictit').html(escapelt(data.heads[index].title));
					$(this).find('img.thumb').attr('src',data.heads[index].pic+'?bak='+data.heads[index].thumb).show().siblings('.alt').hide();
					if(index==0){
						$(this).find('.edit').click();
					}
				});
				$('#newsList li').each(function(index){
					if($(this).attr('data-config')){
						$(this).attr('data-config',JSON.stringify(data.lists[index]));
						$(this).find('.artTitle').html(escapelt(data.lists[index].title));
						$(this).find('.artContent').html(escapelt(data.lists[index].content));
						$(this).find('img.thumb').attr('src',data.lists[index].pic+'?bak='+data.lists[index].thumb);
					}
				});
			}
		});
	};
	
	$('.btnGroup .btnClass5').bind('click',function(){
		if(!check()){
			return;
		}
		var $button = $(this);
		var lgcorpcode = $('#lgcorpcode').val();
		var lguserid = $('#lguserid').val();
		var json = {"heads":[],"lists":[]};
		$('#heads .piclist').each(function(){
			if($(this).attr('data-config')){
				var data = eval('('+$(this).attr('data-config')+')');
				json.heads.push(data);
			}
		});
		$('#newsList li').each(function(){
			if($(this).attr('data-config')){
				var data = eval('('+$(this).attr('data-config')+')');
				json.lists.push(data);
			}
		});
		//json字符串 特殊字符处理 已知&会出现解析问题 
		var jsonstr = JSON.stringify(json);
		var infoname = $.trim($('#infoname').val());
		// 0暂存  
		if($button.hasClass('cache')){
			var sid = $('#sid').val();
			$.ajax({
				type:"POST",
				url: "app_homeedit.htm?method=cache&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&time="+new Date().getTime(),
				data: {json:jsonstr,sid:sid,infoname:infoname},
				beforeSend: function(){$button.attr("disabled",true);},
				complete: function(){$button.attr("disabled",false);},
				success: function(result){
					if(result>0){
						// alert('暂存成功！');
						alert(getJsLocaleMessage('appmage','appmage_js_home_zancunsuc'));
						$('#sid').val(result);
						//将是否编辑状态置为false
						isEdited = false;
					}else{
						// alert('暂存失败！');
						alert(getJsLocaleMessage('appmage','appmage_js_home_zancunfalse'));
					}
				}
				});	
		}else if($button.hasClass('save')){//保存
			var sid = $('#sid').val();
			$.ajax({
				type:"POST",
				url: "app_homeedit.htm?method=save&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&time="+new Date().getTime(),
				data: {json:jsonstr,sid:sid,infoname:infoname},
				beforeSend: function(){$button.attr("disabled",true);},
				complete: function(){$button.attr("disabled",false);},
				success: function(result){
					if(result>0){
						//alert('保存成功！');
						alert(getJsLocaleMessage('appmage','appmage_js_home_savesuc'));
						isEdited = false;
						back();
					}else{
						//alert('保存失败！');
						alert(getJsLocaleMessage('appmage','appmage_js_home_savefalse'));
					}
				}
				});	
		}else{//发布
			if(!confirm(getJsLocaleMessage('appmage','appmage_js_home_confiretopublic'))){
				return;
			}
			var sid = $('#sid').val();
			$.ajax({
				type:"POST",
				url: "app_homeedit.htm?method=publishInEdit&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&time="+new Date().getTime(),
				data: {json:jsonstr,sid:sid,infoname:infoname,validity:$('#validity').val()},
				beforeSend: function(){
					//发布之前验证有效期
					if(validate($('#validity'))){
						$button.attr("disabled",true);
						return true;
					}else{
						return false;
					};
				},
				complete: function(){$button.attr("disabled",false);},
				success: function(result){
					if(result == 1){
						// alert('发布成功！');
						alert(getJsLocaleMessage('appmage','appmage_js_homelist_publicsuc'));
					}else{
						// alert(pubstatus[result]||"发布失败！");
						alert(getJsLocaleMessage('appmage','appmage_js_homelist_publicfalse'));
					}
					isEdited = false;
					back();
				}
				});	
		}
	});
	
	
});

//验证有效期
function validate($obj){
	if(!$obj.val()){
		// alert("请选择有效期！");
		alert(getJsLocaleMessage('appmage','appmage_js_home_selecttime'));
		return false;
	}
	var curDate = new Date();
	var validate = Date.parse($obj.val().replace(/-/g,'/').replace(/：/g,":"));
	if(validate>curDate){
		return true;
	}else{
		// alert("有效期已经过期，请重新选择！");
		alert(getJsLocaleMessage('appmage','appmage_js_home_errortime'));
		return false;
	}
	
}

//重置上传文件控件
function resetfileinput(obj){
	 $(obj).wrap("<form></form>");
     var $form = $(obj).parent();
     $form[0].reset();
     $(obj).unwrap();
}

function upload(obj){
	var imgtem=$(obj).val();
   	if(imgtem.length<1){
    	return ;	
    }else{
    	var c = ".jpg|.jpeg|.bmp|.png|";
        var b = imgtem.substring(imgtem.lastIndexOf("."),imgtem.length).toLowerCase();
        if(c.indexOf(b)<0){
            // alert("请上传jpg、jpeg、bmp、png类型的图片！");
        	alert(getJsLocaleMessage('appmage','appmage_js_home_uploadformat'));
            resetfileinput(obj);
            return;
        }
	}
   	doUploadImg('uploadFile');
}

//保存 发布时校验信息的完整性
function check(){
	var result = true;
	$('#heads .piclist').each(function(index){
		if(result&&$(this).attr('data-config')){
			var data = eval('('+$(this).attr('data-config')+')');
			if(!checkConfig(index,data)){
				result = false;
			}
		}
	});
	$('#newsList li').each(function(index){
		if(result&&$(this).attr('data-config')){
			var data = eval('('+$(this).attr('data-config')+')');
			if(!checkConfig(index,data,1)){
				result = false;
			}
		}
	});
	return result;
}

function checkConfig(index,data,type){
	var msg = '';
	if(!data){
		return false;
	}
	
	if(!data.title){
		msg = msg+ getJsLocaleMessage('appmage','appmage_js_home_titleisnotnull') + "\n";
	}
	if(!data.thumb){
		msg = msg+ getJsLocaleMessage('appmage','appmage_js_home_imageisnotnull') + "\n";
	}
	var link=data.url;
	if(link!=''&&link.indexOf("http://")<0){
		msg = msg+ getJsLocaleMessage('appmage','appmage_js_home_urlerror') + "\n";
	}	
	if(!type){type == 0};
	//列表
	if(type == 1){
		if(!data.content&&!data.url){
			msg = msg+ getJsLocaleMessage('appmage','appmage_js_home_contentisnotnull') + "\n";
		}
		if(msg.length>0){
			goTip(index,data);
			alert(getJsLocaleMessage('appmage','appmage_js_home_di') + (index+1)+ getJsLocaleMessage('appmage','appmage_js_home_contentisnotall') + "\n"+msg);
			return false;
		}
	}else{
		if(msg.length>0){
			goTip(index,data);
			alert(getJsLocaleMessage('appmage','appmage_js_home_di') + (index+1)+ getJsLocaleMessage('appmage','appmage_js_home_imageisnotall') + "\n"+msg);
			return false;
		}
	}
	return true;
}

function goTip(index,data){
	if(data.role=='listArticle'){
		$('#newsList li').eq(index).find('.edit').click();
	}else{
		$('#heads .piclist').find('.edit').click();
		$('#largePic ul li').eq(index).click();
	}
}

function updateConfig(imgurl){
	//imgurl ”?bak=“
	var urls = imgurl.split('?bak=');
	var url = urls[0],bakurl = urls[1]; //url 本地路径       bakurl 文件服务器路径
	var editArea=$('.pageEditArea').attr('data-editArea');
	var $obj;
	if(editArea){
		editArea=eval('('+editArea+')');
		var role=editArea.role,pos=editArea.pos;
		if(role=='largeArticle'){
			$obj = $('.piclist').eq(pos);
		}else{
			$obj = $('#newsList li').eq(pos);
		}
		var config = eval('('+$obj.attr('data-config')+')');
		config.thumb = bakurl;
		config.pic = url;
		$obj.attr('data-config',JSON.stringify(config));
		$obj.find('img.thumb').attr('src',imgurl).show().siblings('.alt').hide();
	}
	
}

function back() {
	//暂存之后进行过编辑
	if(isEdited&&!confirm(getJsLocaleMessage('appmage','appmage_js_home_confireeditcontent'))){
		return;
	}
	var lgcorpcode = $('#lgcorpcode').val();
	var lguserid = $('#lguserid').val();
	url = "app_homeedit.htm?lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&time="+new Date().getTime();
	window.location.href = url ;
}
function beforeUpload(){
	$('.uploadify-queue').hide();
	$('#largePic').css('visibility','hidden');
	$('#addNewsMod').css('visibility','hidden');
	$('#uploadFile').attr('disabled',true);
	$('.statusInner').hide();
   	var offset = $('#thumbView').offset();
   	var width = $('#thumbView').width();
   	var height = $('#thumbView').height();
   	$('#thumbView img').hide();
   	$('#uptip').width(width).height(10)
   	.css('top',parseInt(offset.top+height/2-5)+'px').css('left',offset.left+'px')
   	.show();
}

//对Date的扩展，将 Date 转化为指定格式的String 
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function(fmt) 
{ //author: meizz 
var o = { 
 "M+" : this.getMonth()+1,                 //月份 
 "d+" : this.getDate(),                    //日 
 "h+" : this.getHours(),                   //小时 
 "m+" : this.getMinutes(),                 //分 
 "s+" : this.getSeconds(),                 //秒 
 "q+" : Math.floor((this.getMonth()+3)/3), //季度 
 "S"  : this.getMilliseconds()             //毫秒 
}; 
if(/(y+)/.test(fmt)) 
 fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
for(var k in o) 
 if(new RegExp("("+ k +")").test(fmt)) 
fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
return fmt; 
}

//判断是否谷歌浏览器 谷歌浏览器与其他浏览器采用不同的上传方式
function initUpload(){
	// if(!isChrome){
	// 	$('#uploadFile').uploadify({
	// 	swf: iPath+'/js/uploadify/uploadify.swf',
     //      //swf的相对路径，必写项
     //    uploader: 'app_homeedit.htm?method=uploadImg',
     //    removeCompleted: false,
     //    preventCaching:true,
     //    width: 65,
     //    buttonText:'',
     //    queueSizeLimit:1,
     //    multi: false,
     //    formData:{'appcode':$('#appCode').val()},
     //    onUploadStart: function(file){
	//    		beforeUpload();
	//    		$('#uploadFile').uploadify('disable',true);
	//     },
	//      onUploadComplete: function(file){
	//     	handleComplete();
	//     	$('#uploadFile').uploadify('disable',false);
	//     },
	//     onUploadError: function(file,errorCode,erorMsg,errorString){
	//     	// alert("上传图片失败！");
	//     	alert(getJsLocaleMessage('appmage','appmage_js_home_uploadimagefalse'));
	//     },
	//     onUploadSuccess: function(file,data,respone){
	//  	   if(true==respone){
	//  		  handleSuccess(data);
	//  	   }
	//     }
	//     });
	// }else{
		$('#uploadFile').fileupload({
	        url: 'app_homeedit.htm?method=uploadImg',
	        dataType: 'json',
	        formData: {'appcode':$('#appCode').val()},
	        add: function (e, data) {
	        	beforeUpload();
	        	 var jqXHR = data.submit()
	             .complete(function (result) {
	            	 handleSuccess(result.responseText);
	            	 handleComplete();
	            });
	        }
	    });
	// }
}	
function handleSuccess(url){
	if(!url){
		// alert("上传图片失败！");
		alert(getJsLocaleMessage('appmage','appmage_js_home_uploadimagefalse'));
		return;
		}
   if(url.indexOf("overSize")!=-1){
       // alert("文件大小超过限制5M！");
	   alert(getJsLocaleMessage('appmage','appmage_js_home_filetoobig'));
   }else if(url.indexOf("uploadError")!=-1){
 	  // alert("文件上传过程出错！");
	   alert(getJsLocaleMessage('appmage','appmage_js_home_uploaderror'));
   }else if(url.indexOf("parseError")!=-1){
 	  // alert("解析上传文件异常！");
	   alert(getJsLocaleMessage('appmage','appmage_js_home_parefileerror'));
   }else if(url.indexOf("fileError")!=-1){
 	  // alert("上传文件格式不支持！");
	   alert(getJsLocaleMessage('appmage','appmage_js_home_fileformaterror'));
   }else if(url.length>0){
 	  $('#thumbView img').attr('src',url).show();
 	  updateConfig(url);
 	  //标注为已编辑过
		  isEdited = true;
	}else{
		// alert("上传图片失败！");
		alert(getJsLocaleMessage('appmage','appmage_js_home_uploadimagefalse'));
	}
}

function handleComplete(){
    $('#uptip').hide();
  // $('.overlay').remove();
	$('#largePic').css('visibility','visible');
	$('#addNewsMod').css('visibility','visible');
	$('.statusInner').show();
	$('#uploadFile').attr('disabled',false);
	if($('#thumbView img').attr('src').length>0){
		$('#thumbView img').show();
	}
}

function doUploadImg(id){
	if(!isChrome){
		$('#'+id).uploadify('upload');
	}else{
		//$('#fileupload').fileupload();
	}
}
