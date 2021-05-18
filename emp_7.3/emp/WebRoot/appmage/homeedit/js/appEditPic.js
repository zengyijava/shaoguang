/*var config={
	'title' : 'hello,这是个小标题',
	'content' : '这是小图文的正文内容',
	'thumb' : 'img/demo.jpg',
	'url' : '',
	'order':0,
	'role':'listArticle'
}*/
//
//是否进行过编辑
var isEdited = false;
$(function(){

	// var navTextArr=['大图一','大图二','大图三','大图四','大图五'];
	// var newstxt=['大标题：','大图片：','小标题：','小图片：'];
	
	var navTextArr=[getJsLocaleMessage('appmage','appmage_js_appEditPic_bigimage1'),getJsLocaleMessage('appmage','appmage_js_appEditPic_bigimage2'),getJsLocaleMessage('appmage','appmage_js_appEditPic_bigimage3'),getJsLocaleMessage('appmage','appmage_js_appEditPic_bigimage4'),getJsLocaleMessage('appmage','appmage_js_appEditPic_bigimage5')];
	var newstxt=[getJsLocaleMessage('appmage','appmage_js_appEditPic_bigtitle'),getJsLocaleMessage('appmage','appmage_js_appEditPic_bigimage'),getJsLocaleMessage('appmage','appmage_js_appEditPic_smalltitle'),getJsLocaleMessage('appmage','appmage_js_appEditPic_smallimage')];
	
	//页面初始化时，显示首个轮播图编辑
	if($('#newsId').val()==''){
		dataConfig=getConfig($('.piclist').eq(0));
		$('#newsId').val(dataConfig['newsId']);
	}

	/**字符限制**/
	$('#sendTitle').manhuaInputLetter({'showId':'countTit','len':25});
	$('#sendContent').manhuaInputLetter({'showId':'countCont','len':1000});

	/**大图点击切换**/
	$('#largePic').delegate('li','click',function(){
		var _this=$(this),_index=_this.index();
		$('#largePic li,.piclist').removeClass('cur');
		_this.addClass('cur');
		$('.piclist').eq(_index).addClass('cur').find('.edit').trigger('click');
	});

	

	/**大图添加**/
	$('#addPic').click(function(){
		var largePicCount=$('#largePic li').size();
		if(largePicCount<5){
			$('#largePic ul').append('<li>'+navTextArr[largePicCount]+'</li>');
			//效果区域添加新大图
			var order=largePicCount;
			$('#sliderBox').append(getlargePicTemplate(navTextArr[largePicCount],order));
		}else{
			// alert('最多只能添加5张大图!');
			alert(getJsLocaleMessage('appmage','appmage_js_appEditPic_maxaddimage'));
			return false;
		}
	})


	/**效果预览区鼠标滑过的状态**/
	$(document).delegate('.piclist,#newsList li','mouseover',function(){
		var _this=$(this);
		var h_box=_this.outerHeight(),
			w_box=_this.outerWidth();
		$('.hoverStatus').hide();
		h_box-=1;
		_this.find('.hoverStatus').css({
			'height':h_box,
			'width':w_box-2
		}).show();
	});

	$(document).delegate('.hoverStatus','mouseout',function(){
		$(this).hide();
	});

	/**效果区编辑**/
	$('#effectIndex').delegate('.edit','click',function(){
		var _this=$(this),dbParent=_this.parent().parent().parent(),
		dataConfig=getConfig(dbParent),
		_index=dbParent.index();
		//设置三角形箭头的位置
		setArrowPos(dbParent);
		domCallBack(dataConfig,_index);
		$('#newsList li').removeClass('act');
		if(dataConfig.role=='listArticle'){
			dbParent.addClass('act');
			$('#largePic').hide();
			$('#sendContent').parents('.listArea').show();
			//修改标题
			toLowerTitle();
		}else{
			$('#largePic').show();
			$('.piclist').removeClass('cur');
			$('#sendContent').parents('.listArea').hide();
			dbParent.addClass('cur');
			//修改标题
			toUpTitle();
		}
	})
	
	$('#sliderBox .edit').trigger('click');

	

	/**效果区删除**/
	$('#effectIndex').delegate('.delete','click',function(){
		var _this=$(this),dbParent=_this.parent().parent().parent(),
		dataConfig=getConfig(dbParent);
		if(dataConfig['role']=='listArticle'){
			//获取总大图数量,以及当前序号
			var _index=dbParent.index(),
				modList=$('.modList'),
				size=modList.size()-1;
			if(size>0&&!confirm(getJsLocaleMessage('appmage','appmage_js_appEditPic_deleterow'))){
				return;
			}
			if(_index<size){
				dbParent.remove();
				modList.eq(_index).remove();
				$('.modList').eq(_index).find('.edit').trigger('click');
				listSort();
			}else if(_index==size && size!=0){
				dbParent.remove();
				modList.eq(_index).remove();
				$('.modList').eq(0).find('.edit').trigger('click');
				listSort();
			}else if(size==0){
				//$('#largePic').show();
				//$('.piclist:eq(0)').find('.edit').trigger('click');
				// alert("请至少保留一行，删除失败！");
				alert(getJsLocaleMessage('appmage','appmage_js_appEditPic_deleterowfalse'));
			}

		}else{
			//获取总大图数量,以及当前序号
			var _index=dbParent.index(),
				piclist=$('.piclist'),
				largePic_li=$('#largePic li'),
				size=piclist.size()-1;
			if(size>0&&!confirm(getJsLocaleMessage('appmage','appmage_js_appEditPic_deleteimage'))){
				return;
			}
			//表示后面还有其他轮播图	
			if(_index<size){
				dbParent.remove();
				largePic_li.eq(_index).remove();
				resetNavText();
				$('#largePic li').eq(_index).trigger('click');
				listSort('.picList');
			//表示为末位轮播图
			}else if(_index==size && size!=0){
				dbParent.remove();
				largePic_li.eq(_index).remove();
				resetNavText();
				$('#largePic li').eq(0).trigger('click');
				listSort('.picList');
			}else if(size==0){
				// alert('请至少保留一张图，删除失败！');
				alert(getJsLocaleMessage('appmage','appmage_js_appEditPic_deleteimagefalse'));
			}
		}

	});
	

	/**大图选项卡删除后重新命名**/
	function resetNavText(){
		$('#largePic li').each(function(i){
			$(this).text(navTextArr[i]);
		})
		//删除大图时 增加对封面大图序号的更新
		$('#sliderBox span.alt').each(function(i){
			$(this).text(getJsLocaleMessage('appmage','appmage_js_appEditPic_fengmian')+navTextArr[i]);
		})
	}

	/**添加新图文(列表)**/
	$('.addNews').click(function(){
		if($('#newsList li').length<=8){
			$('#largePic').hide();
			formToEmpty();
			var order=$('.modList').length;
			var template=getNewsTemplate(order);
			$(this).parent().before(template);
			$(this).parent().prev().find('.edit').trigger('click');
		}else{
			// alert('最多只能存在8条图文信息！');
			alert(getJsLocaleMessage('appmage','appmage_js_appEditPic_maximageandword'));
			return false;
		}
	});

	/**内容编辑区域事件**/
	$('#sendTitle,#linkUrl,#sendContent').live('keyup blur',function(e){
		var value = $(this).val();
//		//过滤非法字符
//		if(charReg.test(value)){
//			value=value.replace(charReg,'');
//			$(this).val(value);
//		}
		//失焦事件 过滤两端的空格
		if(e.which == 0 || e.which == '0'){
			$(this).val($.trim($(this).val()));
		}
		var newsId=$('#newsId').val(),_this=$(this),
		editArea=$('.pageEditArea').attr('data-editArea');
		if(editArea){
			editArea=eval('('+editArea+')');
			var role=editArea.role,pos=editArea.pos,newsView={};
			if(pos>=0){
				var urls = $('#thumbView img').attr('src').split('?bak=');
				newsView['title']=$('#sendTitle').val();
				newsView['content']=$('#sendContent').val();
				newsView['url']=$('#linkUrl').val();
				newsView['newsId']=newsId;
				newsView['thumb']=urls[1];
				newsView['pic']=urls[0];
				newsView['role']=role;
				newsView['order']=pos;
				var	params={
						'formatJson':JSON.stringify(newsView),
						'pos':pos,
						'target':'sendTitle',
						'editContent':'sendContent',
						'config':'data-config',
						'currentId':e.currentTarget.id,
						'self':_this
					};
				if(role=='largeArticle'){
					params['dom']='.piclist';
					params['title']='.pictit';
				}else{
					params['dom']='.modList';
					params['title']='.artTitle';
					params['content']='.artContent';
				}
				setPreview(params);
				isEdited = true;
			}
		}
	});

	/**实时预览**/
	function setPreview(params){
		var el=$(params['dom']).eq(params['pos']);
		if(params['currentId']==params['target']){
			el.find(params['title']).html(escapelt(params['self'].val()));
		}
		if(params['currentId']==params['editContent']){
			el.find(params['content']).html(escapelt(params['self'].val()));
		}
		el.attr(params['config'],params['formatJson']);
	}

	/**图文上下移动**/
	$('#effectIndex').delegate('.moveDown','click',function(){
		var _this=$(this);
		 moveDown(_this);
	});

	$('#effectIndex').delegate('.moveUp','click',function(){
		var _this=$(this);
		moveUp(_this);
	});

	/**列表排序方法**/
	function listSort(obj){
		obj=obj ? obj : '.modList';
		$(obj).each(function(i){
			var dataConfig=getConfig($(this));
			dataConfig['order']=i;
			$(this).attr('data-config',JSON.stringify(dataConfig));
		})
	}

	/**获取图文排序值**/
	function getOrder(obj){
		return getConfig(obj).order;
	}

	/**图文上移方法**/
	function moveUp(obj){
		_thisLi=obj.parent().parent().parent(),
		_thisUl=_thisLi.parent(),
		_index=_thisUl.find('li').index(_thisLi);
		if(_index>0){
		    _thisUl.find('li').eq(_index-1).before(_thisLi);
		    listSort();	
		}
	}

	/**图文下移方法**/
	function moveDown(obj){
		_thisLi=obj.parent().parent().parent(),
		_thisUl=_thisLi.parent(),
		_index=_thisUl.find('li').index(_thisLi);
		if(_index<_thisUl.find('li').length-2){
		    _thisUl.find('li').eq(_index+1).after(_thisLi);	
		    listSort();
		}
	}


	function findArticlePos(newsId){
		$('.modList').each(function(index){
			var dataConfig=$(this).data('config');
			if(dataConfig['newsId']==newsId){
				return index;
			}
		})
	}

	/**获取LI中的data-congfig**/
	function getConfig(obj){

		return eval('('+obj.attr('data-config')+')');
	}

	/**表单内容清空**/
	function formToEmpty(dataConfig){
		$('#newsId').val('');
		$('#sendTitle').val('');
		$('#sendContent').val('');
		$('#linkUrl').val('');
		$('#thumbView').html('');
	}

	/**新图文列表模板**/
	function getNewsTemplate(order){
		var template=[],guid=Math.guid();
		template.push('<li data-config="{newsId:\''+guid+'\',order:\''+order+'\',role:\'listArticle\',title:\'\',content:\'\',url:\'\',thumb:\'\',pic:\'\'}" class="modList">');
		template.push('<p class="artTitle">' + getJsLocaleMessage('appmage','appmage_js_appEditPic_title') + '</p>');
		template.push('<p class="artContent">' + getJsLocaleMessage('appmage','appmage_js_appEditPic_content') + '</p>');
		template.push('<img width="50" src="'+iPath+getJsLocaleMessage('appmage','appmage_js_url_1') + '" class="thumb">');
		template.push('<div class="hoverStatus">');
		template.push('<div class="statusInner">');
		template.push('<b class="edit" title="' + getJsLocaleMessage('appmage','appmage_js_appEditPic_edit') + '"></b>');
		template.push('<b class="delete" title="' + getJsLocaleMessage('appmage','appmage_js_appEditPic_delete') + '"></b>');
		template.push('<b class="moveDown" title="' + getJsLocaleMessage('appmage','appmage_js_appEditPic_down') + '"></b>');	
		template.push('<b class="moveUp" title="' + getJsLocaleMessage('appmage','appmage_js_appEditPic_up') + '"></b>');	
		template.push('</div>');	
		template.push('</div>');	
		template.push('</li>');	
		return template.join('');
	}

	/**新增大图图文模板**/
	function getlargePicTemplate(txt,order){
		var template=[],guid=Math.guid();
		template.push('<div class="piclist" data-config="{newsId:\''+guid+'\',order:\''+order+'\',role:\'largeArticle\',title:\'\',content:\'\',url:\'\',thumb:\'\',pic:\'\'}">');
		template.push('<span class="alt">' + getJsLocaleMessage('appmage','appmage_js_appEditPic_fengmian') + txt + '</span>');
		template.push('<img src="" alt="" class="thumb" style="display:none;">');
		template.push('<div class="pictit">' + getJsLocaleMessage('appmage','appmage_js_appEditPic_title') + '</div>');
		template.push('<div class="hoverStatus">');
		template.push('<div class="statusInner">');
		template.push('<b class="edit" title="' + getJsLocaleMessage('appmage','appmage_js_appEditPic_edit') + '"></b>');
		template.push('<b class="delete" title="' + getJsLocaleMessage('appmage','appmage_js_appEditPic_delete') + '"></b>');
		template.push('</div>');
		template.push('</div>');
		template.push('</div>');
		return template.join('');
	}

	/**获取key**/
	Math.guid = function(){
	  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
	    return v.toString(16);
	  }).toUpperCase();      
	};

	//文字改变，如小标题改为大标题
	function toUpTitle(){
		$('#newsTitle').html(newstxt[0]);
		$('#newsPic').html(newstxt[1]);
	}
	//文字改变，如大标题改为小标题
	function toLowerTitle(){
		$('#newsTitle').html(newstxt[2]);
		$('#newsPic').html(newstxt[3]);
	}

	//字数限制提示清0
	function emptyToLimit(){
		$('#countTit').text(0);
	}

	//编辑操作赋值
	function domCallBack(dataConfig,_index){
		$('.pageEditArea').attr('data-editArea',"{'pos':\'"+_index+"\','role':\'"+dataConfig['role']+"\'}");
		$('#newsId').val(dataConfig['newsId']);
		$('#sendTitle').val(dataConfig['title']);
		$('#sendContent').val(dataConfig['content']);
		$('#linkUrl').val(dataConfig['url']);
		if(dataConfig['pic'].length>0){
			$('#thumbView').html('<img src="'+dataConfig['pic']+'?bak='+dataConfig['thumb']+'" alt="" />');
		}else{
			$('#thumbView').html('<img src="" alt="" style="display:none;"/>');
		}
		//获取标题和正文的字符个数回填
		var contentLen=dataConfig['content'].length;
		var texttitle=dataConfig['title']+"";
		titlelen=$.trim(texttitle).length;
		$('#countTit').text(titlelen);
		$('#countCont').text(contentLen);
	}
	
	//设置当前选中时三角图标的位置
	function setArrowPos(obj){
		var offset=obj.offset(),offsetTop=offset.top,objHeight=obj.outerHeight(),
		actTop=offsetTop+parseInt(objHeight/2)-5,
		pageEditAreaLeft=$('.pageEditArea').offset().left;
		$('.appArrow').show().each(function(){
			var arrowLeft=$(this).attr('data-left');
			$(this).css({
				'top':actTop+'px',
				'left':pageEditAreaLeft+parseInt(arrowLeft)+'px'
			});
		});
		
	}
})
								
								
							