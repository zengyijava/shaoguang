define(['jquery','require','util'],function($,req,util){

	var btn_send='#sendMsg';
	$(document).delegate(btn_send,'click',function(e){
		e.preventDefault();
		var u=new util.util();
		var data_id=u.getDataId();
		
		if(!data_id || isOkToSend == 0)
		{
			return;
		}
		isOkToSend = 0;
		var content=$('.im-edit-msg[data-id='+data_id+']'),
			content_val=content.html(),
			param=u.getCurUserInfo(content_val);
		u.sendMsg(param['options'],param['data-id']);

		/*var cur_hd_tab=$('.hd-tab-list li.current'),
			data_id=cur_hd_tab.attr('data-id'),
			content=$('.im-edit-msg[data-id='+data_id+']'),
			content_val=content.html(),
			index;*/
		//通过顶部的data-id寻找左侧用户的更多信息
		/*var mod=$('.tab_box li[id='+data_id+']');
		
			options={
				'toUser':mod.attr('uid'),
				'msg':content_val,
				'msgType':'text',
				'customId':userCustomeId,//global
				'name':kfname,
				'aId':aId,
				'pushType':mod.attr('pushtype'),
				'openId':openId//global
			};
		if($.trim(content_val)){
			var u=new util.util();
			u.sendMsg(options,data_id);*/

			/*chat_content.each(function(){
				if($(this).attr('data-id')==data_id){
					index=$(this).index();
				}
			})
			if(typeof index!='undefined'){
				templete.find('.im-message-content').html(content_val);
				chat_content.eq(index).append(templete);
				content.html('');
				templete=null;
			}

		}*/

	})	

	//判断enter或ctrl+enter键发送消息
	$(document).delegate('.im-edit-msg','keydown',function(e){
		//获取用户选择的发送方式
		var method=$('.send-set a.current').attr('data-method');
		if(method=='Ctrl+Enter'){
			if(e.ctrlKey && 13==e.keyCode){
			   $(btn_send).trigger('click');
			}
		}

		if(method=='Enter'){
			if(13==e.keyCode && !e.ctrlKey){
				$(btn_send).trigger('click');
			}
		}
		
	})


});