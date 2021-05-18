define(['jquery','require','EasyChat'],function($,req){
	var util=function(){
		this.kf_msg_count=0;
		this.cus_msg_count=0;
	};
	util.prototype.countSetting=function(num1,num2){
		this.kf_msg_count=num1;
		this.cus_msg_count=num2;
	}
	util.prototype.msgSetting=function(num1,num2){
		var kf_menu=$('.kf_menu'),
			cus_menu=$('.cus_menu'),
			kf_badge=$('.im_badge',kf_menu),
			cus_badge=$('.cus_badge',cus_menu);
		kf_badge.html(this.numCalculate(num1));	
		cus_badge.html(this.numCalculate(num2));
	}
	util.prototype.numCalculate=function(num){
		var str="";
		if( parseInt(num)>99){
			str="99+";
			return str;
		}else{
			return num;
		}
		
	}
	util.prototype.hidden_li_count=function(){
		var hd=$('.hd-tab-list'),
			li=hd.find('li:eq(0)'),
			hd_w=hd.outerWidth(),
			li_w=li.outerWidth(),
			count;
		if(typeof hd_w=='undefined' || typeof li_w=='undefined'){
			count='undefined';
		}else{
		   var  len=Math.floor(hd_w/li_w),//可存放个数
				all_len=hd.children('li').size(),
				count=all_len-len;
		}	
		return {'count':count,'len':len};
	}

	util.prototype.slideList=function(){
		var obj=this.hidden_li_count(),
			slide_others_group=$('#slide-others-group'),
			hd=$('.hd-tab-list');

		if(typeof obj.count=='number'){
			if(obj.count>0){
				var clone=hd.children('li:gt('+(obj.len-1)+')').clone();
				slide_others_group.empty().append(clone);
			}else{
				slide_others_group.empty();
			}
		}
	}

	util.prototype.limit=function(edit_msg){
			var len=650,
				showid='#limit-count';
			//切换聊天窗口时 读取剩余字符数以及光标移至文字末尾
			$(showid).html(edit_msg.attr('left')||len);
			focusEnd(edit_msg[0]);
	};
	util.prototype.msgAlert=function(){
		alert(userCustomeId);
	}

	util.prototype.choosePerson=function(element,data_id,toprev){
		//var type = $("#chosedMenu").val();//当前选中的菜单类型(wx,app,custom)
		var obj=this.hidden_li_count();
		var data_id=typeof data_id=='undefined' ? $(element).attr('data-id') : data_id;
		var current_edit_msg=$('.im-edit-msg[data-id='+data_id+']'),
			current_hd_tab=$('.hd-tab-list li[data-id='+data_id+']'),
			roster_li=$('.cus-group li[class='+data_id+']'),
			uid=$(element).attr('uid'),
			_index=current_hd_tab.index(),
			current_tab=$('#tab-container .chat-tab[data-id='+data_id+']');
			//切换窗口时 将未读置为已读
			current_edit_msg.removeAttr('unRead');
			current_tab.removeAttr('unRead');
			current_hd_tab.removeAttr('unRead');
		if(data_id.indexOf('group')>-1){//检测是否为群组，群组右侧切换
			$('.im-right-sidebar').find('div.'+data_id).removeAttr('unRead').show().siblings().hide();
		}
		
		if(data_id.indexOf('cus')>-1){//检测当前聊天窗口是属哪种菜单，菜单右侧切换
			$('#im-tmenu .im-menu-icondiv').click();
		}else if(data_id.indexOf('kh')>-1){
			$('#im-mmenu .im-menu-icondiv').click();
		}else if(data_id.indexOf('kf')>-1 || data_id.indexOf('group')>-1){
			$('#im-bmenu .im-menu-icondiv').click();
		}

		current_hd_tab.addClass('current').removeClass('have-msg').siblings().removeClass('current');
		current_tab.show().siblings().hide();
		current_edit_msg.show().siblings().hide();
		//字符输入限制
		this.limit(current_edit_msg);
		if(_index>obj.len-1){//当前的person处于隐藏状态
			current_hd_tab.remove();
			$('.hd-tab-list').prepend(current_hd_tab);
			this.slideList();
		}
		this.showGroup(data_id,uid);
		//获取焦点
		this.addFocus(data_id);
		
		this.initScroll();
		//双击新消息图标去除
		var oCount=roster_li.find('.count'),
			oKfCount=$('.kf_menu .im-badge'),
			oCusCount=$('.cus_menu .im-badge'),
			oGroupCount=$('.group_menu .im-badge'),
			oAppCount=$('.app_menu .im-badge'),
			oLMxCount=$('#im-tmenu .count'),
			oLAppCount=$('#im-mmenu .count'),
			oLKfCount=$('#im-bmenu .count'),
			_cusCount=oCusCount.text()-0,
			_kfCount=oKfCount.text()-0,
			_gpCount=oGroupCount.text()-0,
			_appCount=oAppCount.text()-0,
			_count=oCount.text();
		//去除左侧消息提醒
		roster_li.find('span.count').html('');	
		//双击更新总消息
		if(data_id.indexOf('kf')!=-1){
			var diff=_kfCount-_count;
			if(diff<=0){
				oKfCount.hide().html(0);
				oLKfCount.html(_gpCount);
			}else{
				oKfCount.html(diff);
				oLKfCount.html(diff+_gpCount);
			}
		}
		else if(data_id.indexOf('cus')!=-1){
			var diff=_cusCount-_count;
			if(diff<=0){
				oCusCount.hide().html(0);
				oLMxCount.hide().html(0);
			}else{
				oCusCount.html(diff);
				oLMxCount.html(diff);
			}
		}
	    else if(data_id.indexOf('group')!=-1){
	        var diff=_gpCount-_count;
            if(diff<=0){
                oGroupCount.hide().html(0);
                oLKfCount.html(_kfCount);
            }else{
                oGroupCount.html(diff);
                oLKfCount.html(diff+_kfCount);
            }
        }
	    else if(data_id.indexOf('kh')!=-1){
	        var diff=_appCount-_count;
            if(diff<=0){
            	oAppCount.hide().html(0);
            	oLAppCount.hide().html(0);
            }else{
            	oAppCount.html(diff);
            	oLAppCount.html(diff);
            }
        }
		if(data_id.indexOf('cus')!=-1  || data_id.indexOf('kh-')!=-1 ){
			$('.im-t4').show();
		}else{
			$('.im-t4').hide();
		}
	}

	util.prototype.addUserJson=function(userJson,iPath){
		if(userJson){
			var str='';
			for(var attr in userJson){
				var customeId=userJson[attr].customeId,
					state=userJson[attr].state,
					name=userJson[attr].name,
					className=this.getState(state);
				var iswei = userJson[attr].iswei;
        		var pic = this.getPic(state, iswei);
				var html ='<li class="kf-list-'+customeId+'" iswei="'+iswei+'" uid="'+customeId+'" state="'+state+'" pushtype="3">'+
						'<a href="" class="tab-fav cc">'+
							'<div class="tab-left '+className+'"><img src="'+iPath+'/static/images/'+pic+'" alt=""><i></i></div>'+
							'<div class="tab-surmark"></div>'+
							'<div class="tab-remark"></div>'+
							'<div class="tab-right">'+
								'<span class="count"></span>'+
							'</div>'+
							'<div class="tab-main">'+
								'<span class="cus-title">'+name+'</span>'+
								'<input type="text" class="cus-input" value="'+name+'" maxlength = "20"/>'+
							'</div>'+
						'</a>'+
					'</li>';
				if(state == 1)
				{
					str = html + str;
				}else
				{
					str += html;
				}
			}
			$('#cus-group-custom .mod-kf').empty().append(str);
			setTimeout(function(){
				$('a.tab-fav').each(function(){
					$(this).attr('title',$(this).find('.tab-main span').text());
				});
			},200);
		}
	};
	
	util.prototype.addUserOfAppJson=function(appUserJson,iPath){
		if(appUserJson){
			var mod_kh=$('.mod-kh');
		    var mod_kh_today=$('.mod-kh-today');
		    var mod_kh_yest=$('.mod-kh-yest');
		    var mod_kh_ago=$('.mod-kh-ago');
			var mod_kh_never=$('.mod-kh-never');
			var str='';
			for(var uk in appUserJson){//页面初始化加载客户列表
			    var data=appUserJson[uk]
				for(var attr in data){
					var appcode=data[attr].appcode,
						servernum=data[attr].servernum,
						name=data[attr].name,
						ecode=data[attr].ecode;
					str+='<li class="kh-list-'+appcode+'" uid="'+appcode+'" ecode="'+ecode+'" pushtype="7" servernum = "'+servernum+'">'+
							'<a href="" class="tab-fav cc">'+
								'<div class="tab-left"><img src="'+iPath+'/static/images/app.png" alt=""><i></i></div>'+
								'<div class="tab-right">'+
									'<span class="count"></span>'+
								'</div>'+
								'<div class="tab-main">'+
									'<span class="cus-title">'+name+'('+appcode+')</span>'+
								'</div>'+
							'</a>'+
						'</li>';
				}
			    if(uk == 'curs')
				{
				    mod_kh.append(str);
				}
				else if(uk == 'today')
				{
				    mod_kh_today.append(str);
				}
				else if(uk == 'yest')
				{
				    mod_kh_yest.append(str);
				}
				else if(uk == 'ago')
				{
				    mod_kh_ago.append(str);
				}
				else if(uk == 'never')
				{
				    mod_kh_never.append(str);
				}
				str='';
			}
		}
	};	

	util.prototype.addFocus=function(data_id){
		$('.im-edit-msg[data-id='+data_id+']').focus();
	}
	util.prototype.addGroupJson=function(groupJson,iPath){
		if(groupJson){
			var str='';
			for(var attr in groupJson){
				var groupid=groupJson[attr].groupid,
					count=groupJson[attr].count,
					name=groupJson[attr].name;
				str+='<li class="group-list-'+groupid+'" uid='+groupid+' pushtype="4" count="'+count+'">'+
						'<a href="" class="tab-fav cc">'+
							'<div class="tab-left"><img src="'+iPath+'/static/images/icon_group.png" alt=""></div>'+
							'<div class="tab-right">'+
								'<span class="count"></span>'+
							'</div>'+
							'<div class="tab-main">'+
								'<span class="cus-title">'+escapeString(name)+'</span>'+
							'</div>'+
						'</a>'+
					'</li>';
				groupKeys += groupid + ',';
			}
			$('#cus-group-custom .mod-group').empty().append(str);
			if(groupKeys.length > 0)
			{
			    groupKeys = groupKeys.substring(0,groupKeys.length - 1);
			}
		}
	}
	util.prototype.showGroup=function(data_id,uid){
		var _this=this,groupName=$('.mod-group .'+data_id).find('.cus-title').text();
		
			if(data_id.indexOf('group')>-1){
				if($('.im-right-sidebar').find('div.'+data_id).size()==0){
					$.ajax({
						method:'GET',
						url:'customChat.htm?method=getGroupMember&t='+(new Date()).valueOf(),
						data:{
							groupKey:uid,
							aid:aId,
							isAsync:'yes',
							userCustomeId:userCustomeId
						},
						success:function(result){
							if(result == "outOfLogin")
				    		{
				    			doOut();
				    			return;
				    		}
							if(result.indexOf("chat:")==0 && result != "chat:"){
				            	result = result.substring(5);
				            	result=eval('('+result+')');
				            	var str="",count=0,total=result.length;

				            	str='<div class="'+data_id+'"><div class="bd grlist nano"><div class="content"><ul class="tab-group-list">';
				            	for(var key in result){
				            		var pic = _this.getPic(result[key]["state"], result[key]["iswei"]);
				            		str+='<li class="kf-list-'+result[key]["customeid"]+'-'+uid+'" uid="'+result[key]["customeid"]+'" pushtype="3" state="'+result[key]["state"]+'">'+
										'<a href="" class="tab-fav cc">'+
											'<div class="tab-left"><img src="'+iPath+'/static/images/'+pic	+'" alt=""><i></i></div>'+
											'<div class="tab-main">'+
												'<span class="cus-title">'+result[key]["name"]+'</span>'+
											'</div>'+
										'</a>'+
									'</li>';
				            		
									if(result[key]["state"]==1){
										count=count+1;
									}
				            	}
				            	str+='</ul></div></div>';

				            	_this.setChatContent();
				            	
				            	$(str).prependTo($('.im-right-sidebar'));
				            	 var leaveGroupStr = '<a uid="'+uid+'" class="leaveGroup">[退出]</a>';
                                 $('<div class="tab-group-title">'+groupName+'<span>['+count+'/'+total+']'+leaveGroupStr+'</span></div>')
                                     .prependTo($('.im-right-sidebar div.'+data_id));
				            	$('.im-right-sidebar').find('div.'+data_id).show().siblings().hide();
				       		}
							setTimeout(function(){
								$('div.'+data_id+' .tab-fav').each(function(){
									$(this).attr('title',$(this).find('.tab-main span').text());
								});
							},200);
							
							$(".grlist").height($('.im-right-sidebar').height()-36);
							$(".grlist").nanoScroller({ preventPageScrolling: true });
							setTimeout(function(){$(".grlist").nanoScroller();}, 300);
							$(window).resize(function(){
								$(".grlist").height($('.im-right-sidebar').height()-36);
								$(".grlist").nanoScroller({ preventPageScrolling: true });
								setTimeout(function(){$(".grlist").nanoScroller();}, 300);
							})
						}
					});
				}else{
					$('.im-right-sidebar').show();
					$('.im-right-sidebar').find('div.'+data_id).show().siblings().hide();
					this.setChatContent();
					
				}
			}else{
				$('.im-right-sidebar').hide();
				$('.chatWrapper,.chatContent').css({
    					'margin-left':'0'
    				})
			}
		
	}
	util.prototype.addToTop=function(element,data_id,uid,pushtype,state,servernum,msgid){
		var personIco=$(element).find('img').attr('src'),
		cusTitle=$(element).find('.cus-title').text(),
		newPerson='<li data-id="'+data_id+'" uid="'+uid+'" msgid="'+msgid+'" pushtype="'+pushtype+'" state="'+state+'" servernum="'+servernum+'">'+
				'<div class="hd-tab-fav cc">'+
						'<img src="'+personIco+'" class="avatar">'+
						'<span class="tab-name">'+cusTitle+'</span>'+
						'<i class="tab-close">close</i>'+
				'</div>'+
			'</li>';
		return newPerson;	
	}
	util.prototype.setChatContent=function(){
		$('.chatWrapper').css({
    		'margin-left':'-168px'
    	})
    	$('.chatContent').css({
    		'margin-left':'177px'
    	})
    	$('.im-right-sidebar').show();
	}
	util.prototype.initChatContent=function(){
		$('.chatWrapper,.chatContent').css({
    		'margin-left':'0'
    	})
    	$('.im-right-sidebar').hide();
	}

	util.prototype.getPic=function(state,iswei){
		
		var pic = "kf.png";
		if(iswei == 1)
		{
			pic = "kf_wei.png";
			if( state == 4)
			{
    			pic = "kf_wei_lx.png";
    			className = "";
			}
		}else
		{
			if(state == 4)
			{
				pic="kf_lx.png";
			}
		}
		return pic;
		
	}
	util.prototype.getState=function(state){

		var className="";
		switch(state){
			case 1 :
			className="online";
			break;
			case 2 :
			className="busy";
			break;
			case 3 :
			className="leave";
			break;
			case 4 :
			className="offline";
			break;
			default :
			className="online";
			break;
		}
		return className;

	}

	util.prototype.userInfoJson=function(userInfoJson,dir){
		if(userInfoJson){
		    var mod_cus=$('.mod-cus');
		    var mod_cus_today=$('.mod-cus-today');
		    var mod_cus_yest=$('.mod-cus-yest');
			var mod_cus_ago=$('.mod-cus-ago');
			var tab_list=$('.hd-tab-list');
			var str='';
			var _count=0;
			for(var uk in userInfoJson){//页面初始化加载客户列表
			    var data=userInfoJson[uk];
				for(var key in data){
					var openid=data[key].openid,
						servernum=data[key].servernum,
						name=data[key].name;
					if(mod_cus.find('li[uid='+openid+']').size()==0){
						str+='<li class="cus-list-'+openid+'" uid='+openid+' servernum='+servernum+' pushtype="2">'+
							'<a href="" class="tab-fav cc">'+
								'<div class="tab-left"><img src="'+iPath+'/static/images/iman.png" alt=""><i></i></div>'+
								'<div class="tab-right">'+
									'<span class="count"></span>'+
								'</div>'+
								'<div class="tab-main">'+
									'<span class="cus-title">'+name+'</span>'+
								'</div>'+
									
							'</a>'+
						'</li>';
					}	
				}
				if(uk == 'curs')
				{
				    mod_cus.append(str);
				}
				else if(uk == 'today')
				{
				    mod_cus_today.append(str);
				}
				else if(uk == 'yest')
				{
				    mod_cus_yest.append(str);
				}
				else if(uk == 'ago')
				{
				    mod_cus_ago.append(str);
				}
				str='';
			}

			
		}
	}
	//获取上传信息
	util.prototype.load_MsgJson=function(data,dir){
		//console.log(data);
		if(data instanceof Object){//客户微信自动上载信息
				var fromuser=data.fromuser,
					name=data.name,
					time=data.time,
					message=data.message,
					msgtype=data.msgtype,
					msgid=data.msgid,
					servernum=data.servernum,
					pushtype=data.pushtype,
					dotype=data.dotype,
					str="",
					data_id="",
					tabContainer="",
				    _count=0,
				    _wiseCount=0;
				switch(pushtype){
					case '1':
					data_id='cus-list-'+fromuser,
					tabContainer=$('.mod-cus');
					break;
					case '2':
					data_id='cus-list-'+fromuser;
					tabContainer=$('.mod-cus');
					break;
					case '3':
					data_id='kf-list-'+fromuser;
					tabContainer=$('.mod-kf');
					break;
					case '4':
					    fromuser = servernum.substring(5);
	                    data_id='group-list-'+ fromuser;
	                    tabContainer=$('.mod-group');
	                    break;
					case '6':
						data_id='kh-list-'+fromuser;
						tabContainer=$('.mod-kh');
						this.updateAppName(data_id,name);
						break;
					default:
					data_id='cus-list-'+fromuser,
					tabContainer=$('.mod-cus');
					break;

				}
				if((pushtype == '1' && tabContainer.find('li[uid='+fromuser+']').size()==0 && dotype == 1)
						|| (pushtype == '6' && tabContainer.find('li[uid='+fromuser+']').size()==0 && dotype == 1)){
				    if(tabContainer.parent().find('li[uid='+fromuser+']').length > 0)
				    {
				        tabContainer.parent().find('li[uid='+fromuser+']').remove();
				    }
				    var pic = 'iman.png';
				    var showname = escapeString(name);
				    if(pushtype == '6')
				    {
				    	pic = 'app.png';
				    	showname = name+'('+fromuser+')';
				    }
				    var str = '';
					str+='<li class="'+data_id+'" uid='+fromuser+' servernum='+servernum+'  pushtype="'+(parseInt(pushtype)+1)+'">'+
						'<a href="" class="tab-fav cc" title="'+showname+'">'+
							'<div class="tab-left"><img src="'+iPath+'/static/images/'+pic+'" alt=""><i></i></div>'+
							'<div class="tab-right">'+
									'<span class="count"></span>'+
								'</div>'+
							'<div class="tab-main">'+
								'<span class="cus-title">'+showname+'</span>'+
							'</div>'+
						'</a>'+
					'</li>';

					tabContainer.append(str);
				}else if(pushtype == '1' ||  pushtype == '6')
				{
					$('.hd-tab-list li[data-id="'+data_id+'"]').attr('servernum',servernum);
					tabContainer.find('li[uid='+fromuser+']').attr('servernum',servernum);
				}
				
				this.checkDotype(pushtype,dotype,fromuser,tabContainer);
				
				//新消息msgid
				tabContainer.find('li[uid='+fromuser+']').attr('msgid',msgid);
				$('.hd-tab-list li[uid='+fromuser+']').attr('msgid',msgid);
				$('#slide-others-group li[uid='+fromuser+']').attr('msgid',msgid);

				//新消息个数
				var oCount;
				if(pushtype == '1' || pushtype == '6')
				{
					oCount=tabContainer.parent().find('li[uid='+fromuser+']').find('.count');
				}else
				{
					oCount=tabContainer.find('li[uid='+fromuser+']').find('.count');
				}
				// 如果找不到对应的标签栏，则不计算数字
				if(oCount.length > 0)
				{
					if($.inArray(pushtype,['1','3','4','6'])!=-1){
						if(pushtype==3){
							//客服消息总个数
							var oWiseCount=$('.kf_menu .im-badge');
						}else if(pushtype==1){
							//微信消息总个数
							var oWiseCount=$('.cus_menu .im-badge');
						}else if(pushtype==4){
							//微信消息总个数
							var oWiseCount=$('.group_menu .im-badge');
						}else if(pushtype==6){
							//APP消息总个数
							var oWiseCount=$('.app_menu .im-badge');
						}
						var _wiseCount=oWiseCount.html()-0;//parseInt(oWiseCount.html());
						_wiseCount++;
						_wiseCount=this.numCalculate(_wiseCount);
					}
					count=oCount.html();
	
					_count=(typeof count=='undefined') ? _count : count;
	
					_count++;
					//如果A客户处于当前聊天状态，则不显示消息数量
					var checkCurrent=this.checkCurrent(data_id);
					if(!checkCurrent){
						oCount.html(_count);
						if(typeof oWiseCount!='undefined'){
							oWiseCount.html(_wiseCount);
							
							//左边大菜单统计未读消息
							if(pushtype==1){//微信
								$("#im-tmenu .count").html("");
								$("#im-tmenu .count").show();
								$("#im-tmenu .count").html(_wiseCount);							
							}else if(pushtype==6){//APP
								$("#im-mmenu .count").html("");
								$("#im-mmenu .count").show();
								$("#im-mmenu .count").html(_wiseCount);
							}else if(pushtype==3 || pushtype==4){//客服
								var otherCount;
								if(pushtype==3){
									otherCount = $('.group_menu .im-badge').html();
								}else if(pushtype==4){
									otherCount = $('.kf_menu .im-badge').html();
								}
								$("#im-bmenu .count").html("");
								$("#im-bmenu .count").show();
								$("#im-bmenu .count").html(_wiseCount+(otherCount-0));
							}
						}
						
						$('.hd-tab-list li[data-id='+data_id+'],#slide-others-group li[data-id='+data_id+']').addClass('have-msg');
	
					}
					if(typeof oWiseCount!='undefined'){
						if(oWiseCount.html()-0){
							oWiseCount.show();
						}else{
							oWiseCount.hide();
						}	
					}
				}
				//构建聊天主体容器
				this.load_MsgBox(data_id,pushtype);

				//信息格式: `text`  `image`  `voice[amr]`

				message=this.getMessage2(msgtype,dir,message,pushtype);

				//构建聊天输入框
				this.load_EditMsg(data_id,1);
				var boxpic = "kh_wei.png";
				var panel;
				if(msgtype=='text' || msgtype=='image' || msgtype=='voice'){

					var item_panel=$('#item-panel');

						panel=item_panel.find('div.im-item').clone();
					
					panel.attr('msgid',msgid);
					
					panel.find('.im-txt-bold').html(name);

					panel.find('.im-send-time').html(time);

					panel.find('.im-message-content').html(message);
					if(pushtype == "3" || pushtype == "4")
					{
						var iswei = $('.kf-list-'+data.fromuser).attr('iswei');
						if(iswei == 1)
						{
							boxpic = 'user1_wei.png';
						}else
						{
							boxpic = 'user1.png';
						}
					}else if(pushtype == "6" )
					{
						boxpic = 'kh_app.png';
					}
					panel.find('.userPic').attr('src',iPath+'/static/images/'+boxpic);

				}else if(msgtype=='tips' || msgtype=='zjkf' || msgtype=='tcqz' || msgtype == 'newqz'){

					panel=$('#end-panel').find('div.chatMsg').clone();

					panel.addClass('im-msg-success').find('.im-icon').addClass('im-icon-success');

					if(msgtype=='zjkf'){
						message=eval('('+message+')');
						var showid = 'converque_'+message.servernum+new Date().getTime();
						$('body').append('<div class="hide" id = "'+showid+'"></div>');
						$('#'+showid).text(message.msg);
						
						// 先清除上一次转接的点击
						var $acceptinfo = $('.chat-tab[data-id='+data_id+']').find('.acceptinfo[servernum="'+message.servernum+'"]');
						if($acceptinfo.length > 0)
						{
							var reg=/(.*?)(\<a.*?>(.*?)<\/a>)(.*?)/gi,arr=[];
							$acceptinfo.parent().html().replace(reg,function(match, p1, p2, p3,p4, offset, string){
								arr.push(p1,p3);
							});
							$acceptinfo.parent().html(arr.join(' '));
						}
						
						message='<span servernum="'+message.servernum+'"  fromuser="'+fromuser+'" openid="'+message.openid+'">'
							+name+'于'+time+'向您发起客户转接申请<span servernum="'
							+message.servernum+'" showid="'+showid+'" class="msgDetail" '
							+'>[查看消息]</span>,<a servernum="'+message.servernum+'" href="javascript:void(0)" code="1" class="acceptinfo" >&nbsp;接受&nbsp;</a>或'+
							'<a href="javascript:void(0)" code="0" class="acceptinfo" >&nbsp;拒绝&nbsp;</a></span>';
						panel.find('p').html(message);
					}else if(msgtype == 'newqz')
					{
						message=eval('('+message+')');
						if($('.mod-group li[uid='+message.groupid+']').length == 0)
						{
							var gourpHtml = '<li class="group-list-'+message.groupid+'" uid="'+message.groupid
		                        +'" pushtype="4" count="'+message.count
		                        +'"><a href="" class="tab-fav cc"><div class="tab-left"><img src="'
		                        +iPath+'/static/images/icon_group.png" alt=""></div><div class="tab-right">'
		                        +'<span class="count"></span></div><div class="tab-main"><span class="cus-title">'
		                        +name+'</span></div></a></li>';
							$('.mod-group').prepend(gourpHtml);
						}
						panel.find('p').html(message.msg+' '+time);
						groupKeys +=","+message.groupid;
					}else{
						panel.find('p').html(message+' '+time);
					}
					
					if(msgtype == 'tcqz')
					{
					    $('.kf-list-'+data.fromuser+'-'+fromuser).remove();
					    //对该群组人数修改
					    var $span = $('.im-right-sidebar>div.'+data_id+'>.tab-group-title>span');
				    	if($span.size()>0){
					    	var html = $span.html();
					    	html = html.replace(/\[(\d+)\/(\d+)]/,function(match, p1, p2, offset, string){
								return '['+(p1-1)+'/'+(p2-1)+']';
							});
					    	$span.html(html);
				    	}

					}
				}

				$('.chat-tab[data-id='+data_id+']').append(panel);

				//更新servernum
				if(pushtype == '1'){
					$('li[uid='+fromuser+']').attr('servernum',servernum);
				}
				this.initScrollOfUserList();
				
				this.showFlashingTitle();
			}
	}
	//根据msgtype判断获取message
	util.prototype.getMessage=function(msgtype,dir,message){
		if(msgtype=='image'){
			message='<a class="fancybox" href="'+dir+'/'+message+'"><img src="'+dir+'/'+message+'"></a>';
		}

		if(msgtype=='text'){
			//message=EasyChat.Util.Parser.all(message);
			message = message.replace(/ /gi,'&nbsp;');
			var data_id = $(".hd-tab-list li.current").attr("data-id");
			if(typeof(data_id)!= 'undefined' && data_id.indexOf('kh')!=-1){//APP
				message=EasyChat.Util.Parser.all(message,"app");
			}else{
				message=EasyChat.Util.Parser.all(message,"");
			}
 		   	
		}

		if(msgtype=='voice'){

			message=eval('('+message+')');

			message='<img src="'+iPath+'/static/images/voice_static.png" class="voice" data-path="'+message.path+'" data-second="'+message.second+'">'+message.second+'″ '+message.msg;
		}
		return message;
	}
	//根据msgtype判断获取message
	util.prototype.getMessage2=function(msgtype,dir,message,pushtype){
		var type = pushtype=="7"||pushtype=="6"?"app":"";//当前选中的菜单类型(wx,app,custom)
		if(msgtype=='image'){
			message='<a class="fancybox" href="'+dir+'/'+message+'"><img src="'+dir+'/'+message+'"></a>';
		}

		if(msgtype=='text'){
			//message=EasyChat.Util.Parser.all(message);
			message = message.replace(/ /gi,'&nbsp;');
 		   	message=EasyChat.Util.Parser.all(message,type);	
		}

		if(msgtype=='voice'){

			message=eval('('+message+')');

			message='<img src="'+iPath+'/static/images/voice_static.png" class="voice" data-path="'+message.path+'" data-second="'+message.second+'">'+message.second+'″ '+message.msg;
		}
		return message;
	}


	//构建聊天输入框
	util.prototype.load_EditMsg=function(data_id,pushtype){
		var editMsgMain=$('.im-send-area'),
			editmsgBox=$('.im-edit-msg'),
			current_edit=$('.im-edit-msg[data-id='+data_id+']');
			initBox='<div class="im-edit-msg" contenteditable="true" hidefocus="true" data-id="'+data_id+'"></div>';
		if(editmsgBox.size()==1 && typeof editmsgBox.attr('data-id')=='undefined'){
			editmsgBox.attr('data-id',data_id);
		}else{
			if(!$('.im-edit-msg[data-id='+data_id+']').size()){
				if(pushtype==1){
					$(initBox).hide().prependTo(editMsgMain);
				}else{
					$(initBox).prependTo(editMsgMain).show().siblings().hide();
				}
			}else{
				if(pushtype==1){
				}else{
					current_edit.show().siblings().hide();
					$('#limit-count').html(650);
				}
			}	
		}
		//获取焦点
		this.addFocus(data_id);
		this.initScroll();
	}

	//检测顶部头像是否处于聊天状态
	util.prototype.checkCurrent=function(data_id){
		var tab_list=$('.hd-tab-list');
		return tab_list.find('li[data-id='+data_id+']').hasClass('current');
	}

	//构建聊天主体窗
	util.prototype.load_MsgBox=function(data_id,pushtype){
		var container='<div class="chat-tab" data-id="'+data_id+'"></div>',
			parentBox=$('#tab-container'),
			current_tab=$('.chat-tab[data-id='+data_id+']');
		if(!current_tab.size()){//不存在则构建
			if(pushtype){
				$(container).hide().prependTo(parentBox);
			}else{
				$(container).prependTo(parentBox).show().siblings().hide();
			}
		}else{//存在则切换
			if(pushtype){
			}else{
				current_tab.show().siblings().hide();
			}
			
		}
	}

	//过去当前用户的data_id
	util.prototype.getDataId=function(){
		var cur_hd_tab=$('.hd-tab-list li.current'),
			data_id=cur_hd_tab.attr('data-id');
		return data_id;	
	}

	//通过顶部的data-id寻找用户的更多信息
	util.prototype.getCurUserInfo=function(msg,msgType){
		var data_id=this.getDataId();
	    var mod=$('.hd-tab-list li[data-id='+data_id+']'),
		    msgType=!msgType ? 'text' : msgType,
		    pushType=mod.attr('pushtype');
		
		    msg=EasyChat.Util.Parser.imgtoEmotion(msg);	
		    msg = removeHTMLTag2(msg);
		    msg = escapeString2(msg);
		$('#htmlToText').html(msg);
		msg = $('#htmlToText').text();
		var options={
				'toUser':mod.attr('uid'),
				'msg':msg,
				'msgType':msgType,
				'customId':userCustomeId,//global
				'name':kfname,
				'aId':aId,
				'pushType':pushType,
				'corpCode':corpCode,
				'openId':openId//global
			};	
		return {'options':options,'data-id':data_id};	
	}

	/**
	 * 加载未读消息
	 */
	util.prototype.loadUnReadMsg=function(dir){
	    var _this=this;
	    $.ajax({
            method:'POST',
            url:'customChat.htm?method=loadUnReadMsg&time='+new Date().getTime(),
            data:{
                customeId : userCustomeId,
                groupKeys : groupKeys,
                isAsync:'yes'
            },
            cache:false,
            success:function(result){
                var msgId = 0;
                if(typeof result=='string'){
                	if(result == "outOfLogin")
    	    		{
    	    			doOut();
    	    			return;
    	    		}
                    if ( result != "chat:" && result.indexOf("chat:") == 0 )
                    {
                        result = result.substr(5);
                        var array=eval("("+result+")");
                        for(var i=0;i<array.length;i=i+1)
                        {
                            var jso = array[i];
                            if(jso.pushtype==4)
                            {
                                msgId = jso.msgid;
                            }
                            //显示对应的消息内内容到窗口中
                             _this.load_MsgJson(jso,dir);
                        }
                       
                        _this.checkMess(0,dir,msgId);
                    }
                }
            }
	    })
	}
	/**
	* 轮询检测消息
	* @return
	*/
	util.prototype.checkMess=function(len,dir,msgId){
		var _this=this;
		$.ajax({
			method:'POST',
			url:"customChat.htm?method=checkMsg&time="+new Date().getTime(),
			data:{
				customId : userCustomeId,
		        len : len,
		        groupKeys : groupKeys,
		        aid : aId,
		        msgId : msgId,
		        isAsync:'yes'
		    },
		    cache:false,
		    success:function(result){
		    	var size = 0;
		    	if(typeof result=='string'){
		    		if(result == "outOfLogin")
		    		{
		    			doOut();
		    			return;
		    		}
		    		if ( result.indexOf("chat:") == 0 && result != "chat:")
			        {
			            var resultJ=eval("("+result.substr(5)+")");
			            for(var mkey in resultJ)
			            {
			            	var array = resultJ[mkey];
				            for(var i=0;i<array.length;i=i+1)
				            {
				                var jso = array[i];
				                /*if(jso.pushtype==4)
				                {
				                    msgId = jso.msgid;
				                }*/
				                //if(jso.pushtype==3 || jso.pushtype==1 || jso.pushtype==2 || jso.pushtype==6){
				                if(mkey == "custmsg"){
	
				                    size = size+1;
				                }
				                //显示对应的消息内内容到窗口中
				                 _this.load_MsgJson(jso,dir);
				            }
			            }
			           
			        }
			         _this.checkMess(size,dir,'');
		    	}
		        
		    }
			
		})
		
	}
	util.prototype.removeMsgTips=function(obj,data_id){
		var oCount=$(obj).find('.count'),
			oKfBadge=$('.kf_menu .im-badge'),
			oCusBadge=$('#cus-group-wx .im-badge'),
			oGroupBadge=$('.group_menu .im-badge'),
			oAppBadge=$('#cus-group-app .im-badge'),
			aWxMsgCountBadge=$("#im-tmenu .count"),//左菜单未读消息统计－微信
			aAppMsgCountBadge=$("#im-mmenu .count"),//左菜单未读消息统计－APP
			aKfMsgCountBadge=$("#im-bmenu .count"),//左菜单未读消息统计－客服
			options=null;
			$(obj).find('.count').empty();
			//双击更新总消息
			if(data_id.indexOf('kf')!=-1){
				options={
					target:$('.mod-kf .count'),
					oBadge:oKfBadge,
					oMcBadge:aKfMsgCountBadge
				}
			}else if(data_id.indexOf('cus')!=-1){
				options={
					target:$('.mod-cus .count'),
					oBadge:oCusBadge,
					oMcBadge:aWxMsgCountBadge
				}
			}else if(data_id.indexOf('group')!=-1){
				options={
					target:$('.mod-group .count'),
					oBadge:oGroupBadge,
					oMcBadge:aKfMsgCountBadge
				}
			}else if(data_id.indexOf('kh')!=-1){
				options={
					target:$('.mod-kh .count'),
					oBadge:oAppBadge,
					oMcBadge:aAppMsgCountBadge
				}				
			}
			this.updateMsg(options);
			
	}
	util.prototype.updateMsg=function(options){
		var oWiseCount=0,_wiseCount;
		options['target'].each(function(i){
			_wiseCount=$(this).text().length==0 ? 0 :  $(this).text();
			oWiseCount+=(_wiseCount-0);
		})
		oWiseCount=this.numCalculate(oWiseCount);
		if(typeof oWiseCount=='undefined' || parseInt(oWiseCount-0)<=0){
			options['oBadge'].hide().html(0);
			options['oMcBadge'].hide().html(0);
		}else{
			options['oBadge'].html(oWiseCount);
			options['oMcBadge'].html(oWiseCount);
		}
	}
	/*
		toUser : 发给谁（客户，客服，群组）的Id,
		msg :消息内容,
		servernum: 服务号,
		msgType: 消息类型（text:文本，image图片，目前不知发送语音，但支持接收语音）,
		customId: 发送者的客服Id,
		pushType: 推送类型（2：客服to客户，3客服to客服，4群组）,
		name: 发送者名称,
		openId: 发送者所在微信号的openId
	*/
	util.prototype.sendMsg=function(options,data_id){
		var showmsg =options.msg;
	
		if($.trim(options.msg)==''){
			$('.im-edit-msg[data-id='+data_id+']').html('');
			alert('发送内容不能为空！或因发送的内容不存在文本消息！');
			isOkToSend = 1;
			return false;
		}
		var type = "";//$("#chosedMenu").val();//当前选中的菜单类型(wx,app,custom)
		//var data_id = $(".hd-tab-list li.current").attr("data-id");接收传递过来的data_id
		if(typeof(data_id)!= 'undefined' && data_id.indexOf('kh')!=-1){//APP
			type="app";
		}else{
			type="";
		}
		var _this=this;
		$.ajax({
			method:'POST',
			url:"customChat.htm?method=sendMsg&isAsync=yes&time="+new Date().getTime(),
			data:options,
		    cache:false,
		    success:function(result){
				if(result == "outOfLogin")
	    		{
	    			doOut();
	    			return;
	    		}
				
				isOkToSend = 1;
				if(result.indexOf("chat:")==0 && result != "chat:"){
		            result = result.substring(5);
		            if(result == "error" || result.length<6)//可能包含微信返回的错误码
					{
						result = "<span style='color:red'>发送失败</span>";
					}
		            //result为发送时间
		            //显示到对应的窗口中
					var chat_content=$('#tab-container .chat-tab'),
						templete=$('#item-me-panel>div').clone(),
						msg="",
						content=$('.im-edit-msg[data-id='+data_id+']');
					if(options.msgType=='image'){
						msg='<a class="fancybox" href="'+path+'/'+options.msg+'"><img src="'+path+'/'+options.msg+'">';
					}else if(options.msgType=='text'){
						msg = EasyChat.Util.Parser.all(showmsg,type);
					}
					templete.find('.im-message-content').append(msg);
					templete.find('.im-send-time').html(result);
					templete.find('.im-txt-bold').html("我");
					$('.chat-tab[data-id='+data_id+']').append(templete);
					if(options.msgType!='image')
					{
						content.html('');
					}
					$('#limit-count').html(650);
					templete=null;
					_this.initScroll();
					isOkToSend = 1;
		        }
		    }
		})
	    
	}

	/**
	* 轮询检测人员状态
	* @return
	*/
	util.prototype.checkStatus=function(){
		var _this=this;
		$.ajax({
			method:'POST',
			url:'customChat.htm?method=checkState&time='+new Date().getTime(),
			data:{
    		    customId : userCustomeId,
    	        aId : aId,
    	        isAsync:'yes'
		    },
		    cache:false,
		    success:function(result){
		    	//console.log(result);
		    	if(typeof result=='string'){
		    		if(result == "outOfLogin")
		    		{
		    			doOut();
		    			return;
		    		}
		    	    if (result.indexOf("status:") == 0 && result != "status:")
		            {
		                //这里面存放了所有客服人员的状态
		                var statusjson=eval("("+result.substr(7)+")");
		                
		                for(var key in statusjson)
		                {
		                    var status = statusjson[key].status;
		                    var className=_this.getState(status);
		                    var oldstatus = $('.mod-kf li[uid='+key+']').attr('state');
		                    // 当前状态不相等时
		                    if(oldstatus != status)
		                    {
		                    	$('.mod-kf li[uid='+key+']').attr('state',status);
		                    	var iswei = $('.mod-kf li[uid='+key+']').attr('iswei');
		                    	var pic = _this.getPic(status, iswei);
			                    $('.mod-kf li[uid='+key+']').find('.tab-left img').attr('src',iPath+'/static/images/'+pic);
			                    $('.im-right-sidebar li[uid='+key+']').find('.tab-left img').attr('src',iPath+'/static/images/'+pic);
		                    	$('li[data-id="kf-list-'+key+'"] img').attr('src',iPath+'/static/images/'+pic);
			                    if(status==1){
			                    	$('.mod-kf li[uid='+key+']').prependTo($('.mod-kf'));
			                    	$('.im-right-sidebar li[uid='+key+']').each(function(){
			                    		$(this).prependTo($(this).parent());
			                    	});
			                    }else
			                    {
			                    	$('.mod-kf li[uid='+key+']').appendTo($('.mod-kf'));
			                    	$('.im-right-sidebar li[uid='+key+']').each(function(){
			                    		$(this).appendTo($(this).parent());
			                    	});
			                    }
			                    // 赋值新状态
			                    $('.mod-kf li[uid='+key+']').attr('state',status);
		                    }
		                }
		            }
		    	    setTimeout(function(){
		    	    	_this.checkStatus();
		    	    },10000);
		    	}
		        
		    }
			
		})
	}
	
	util.prototype.initScroll=function(){
		$("#chat-nano").nanoScroller({ preventPageScrolling: true });
		setTimeout(function(){
		    $("#chat-nano").nanoScroller({ scroll: "bottom" });
		}, 300);
	}
	util.prototype.initScrollOfUserList=function(){
		$("#cus-group-wx #chat-tab-box,#cus-group-app #chat-tab-box,#cus-group-custom #chat-tab-box").nanoScroller({ preventPageScrolling: true });
		setTimeout(function(){
		    $("#chat-tab-box").nanoScroller();
		}, 300);
	}

	var time=0;  
    var timer=null;
	/**
	 * 闪动title方法
	 * @return
	 */
    util.prototype.showFlashingTitle=function(){
    	var _this=this;
    	var title = document.title.replace("【　　　】", "").replace("【新消息】", "");  
    	// 定时器，设置消息切换频率闪烁效果就此产生  
    	timer = setTimeout(function () { 
    		if(_this.isMinStatus() || !window.focus){
    			time++;  
    			if (time % 2 == 0) {  
    				document.title = "【新消息】" + title;
    			}  
    			else {  
    				document.title = "【　　　】" + title;  
    			};
    			_this.showFlashingTitle();
    		}else{
    			_this.clearFlashingTitle();
    		}
    		
    	}, 600);  
    	//return [timer, title];  		
    }
	util.prototype.updateAppName=function(data_id,name){
		if(name=="")
		{
			return;
		}
		var $namespan = $('.mod-kh li.'+data_id).find('.cus-title');
		var nametext = $namespan.text();
		nametext = name+nametext.substring(nametext.lastIndexOf('('));
		//改标签的显示名称
		$(".hd-tab-list > li[data-id='"+data_id+"']").find(".hd-tab-fav").find(".tab-name").text(nametext);
		$(".hd-tab-list > li[data-id='"+data_id+"']").find(".hd-tab-fav").attr("title",nametext);
		$namespan.text(nametext);
	}
	
	/**
	 * 处理左边菜单的上移或下移操作 
	 */
	util.prototype.checkDotype=function(pushtype,dotype,fromuser,tabContainer){
		//如果是离线客服人员发来的消息则不置顶
		if (pushtype == '3' )
		{
			if( tabContainer.find('li[uid='+fromuser+']').attr('state')==1)
			{
				tabContainer.find('li[uid='+fromuser+']').prependTo(tabContainer);
			}
		}else
		{
			if(dotype == 1)
			{
				tabContainer.find('li[uid='+fromuser+']').prependTo(tabContainer);
			}else
			{
				if(dotype == 2 )
				{
					if( tabContainer.find('li[uid='+fromuser+']').length > 0)
					{
						if(pushtype == '1')
						{
							tabContainer.find('li[uid='+fromuser+']').prependTo($('.mod-cus-today'));
						}else if (pushtype == '6')
						{
							tabContainer.find('li[uid='+fromuser+']').prependTo($('.mod-kh-today'));
						}
					}
				}
			}
		} 		
	}
	/**
	 * 取消新消息提示  
	 */
	util.prototype.clearFlashingTitle=function(){
        clearTimeout(timer);  
        document.title = '在线聊天窗口'; 		
	}
	/**
	 * 判断窗口是否最小化
	 * 在Opera中还不能显示
	 */
	var isMin = false;
	util.prototype.isMinStatus=function(){
		//除了Internet Explorer浏览器，其他主流浏览器均支持Window outerHeight 和outerWidth 属性
		if(window.outerWidth != undefined && window.outerHeight != undefined){
			isMin = window.outerWidth <= 160 && window.outerHeight <= 27;
		}else{
		    isMin = window.outerWidth <= 160 && window.outerHeight <= 27;
		}
		//除了Internet Explorer浏览器，其他主流浏览器均支持Window screenY 和screenX 属性
		if(window.screenY != undefined && window.screenX != undefined ){
			isMin = window.screenY < -30000 && window.screenX < -30000;//FF Chrome       
		}else{
			isMin = window.screenTop < -30000 && window.screenLeft < -30000;//IE
		}
		return isMin;		
	}
	//光标停在最后
	function focusEnd(obj){
		if(obj){
			var sel = window.getSelection();
			var range = document.createRange();
			range.selectNodeContents(obj);
			range.collapse(false);
			sel.removeAllRanges();
			sel.addRange(range);
		}
	}
	return {
		util:util
	}
});

