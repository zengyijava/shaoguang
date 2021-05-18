define(['jquery','require','util','artDialog','fileupload'],function($,req,util){
		var im={},edit_msg=[],
		stausClass='status dropdown';
		$(document).delegate('#file_upload','change',function(){
			var url = $('#file_upload').val();
			if($(".hd-tab-list li").size()==0||url.length==0){resetfileinput();return;};
			var fileType = url.substring(url.lastIndexOf('.')).toLowerCase();
			if('.jpeg|.jpg|.gif|.png'.indexOf(fileType)==-1){
				alert('不支持的图片类型！');
				resetfileinput();
				return;
			}
			var modtype = "";
			var data_id = $(".hd-tab-list li.current").attr("data-id");
			if(typeof(data_id)!= 'undefined' && data_id.indexOf('kh')!=-1){//APP
				modtype = "app";
			}
		 	 $.ajaxFileUpload({
		            url: path+'/customChat.hts?method=sendPic&fileName=file_upload&fileType='+fileType+'&modtype='+modtype+'&data_id='+data_id, //需要链接到服务器地址
		            secureuri: false,
		            dataType: "json",
		            fileElementId: "file_upload",
		            success: function (data, textStatus) {
		 		 		resetfileinput();
						if(data.result.indexOf('success')>-1){
							var u=new util.util(),
			            		param=u.getCurUserInfo(data.filepath,'image');
							//u.sendMsg(param['options'],param['data-id']);
							var datas = data.result.split(':');
							if(datas&&datas.length>1&&datas[1]){
								u.sendMsg(param['options'],datas[1]);	
							}
						}else if(data.result=='wrong'){
							alert('错误的文件格式！');//目前这个提示不会出现。
							return false;
						}
						else if(data.result=='oversize'){
							alert('图片超出大小！大小不超过512K。');
							return false;
						}
		        	},
		            error: function (data, status, e) {           //相当于java中catch语句块的用法
		        		alert('上传出错！');
		        		resetfileinput();
		        	}
		        });
		})
		
		$('body').click(function(){/*事件冒泡隐藏下拉*/
			$('.dropdown-menu,.send-set').hide();
			$('.btnToggle').removeClass('ico-down');
		});
		$(document).delegate('.dropdown-menu','click',function(e){
			e.stopPropagation();
			$(this).show();
		})
		//关闭所有
		$(document).delegate('a.close-all','click',function(e){
			e.preventDefault();
			e.stopPropagation();
			$('.hd-tab-list,#tab-container,#slide-others-group').html('');
			$('.im-send-area>div:gt(0)').remove();
			$('.im-send-area>div:eq(0)').attr('data-id','');
			$('.im-right-sidebar>div').remove();
			$('.im-right-sidebar').hide();

		})
		//关闭已读
		$(document).delegate('a.close-have-read','click',function(e){
			e.preventDefault();
			e.stopPropagation();
			$('li.have-msg').each(function(){
				var unRead=$(this).attr('data-id');
				$('.im-edit-msg[data-id='+unRead+'],.chat-tab[data-id='+unRead+'],.hd-tab-list li[data-id='+unRead+']').attr('unRead',1);
				$('.im-right-sidebar>div.group-list-'+unRead).attr('unRead',1);
			})
			$('.im-edit-msg[unRead!=1]').remove();
			$('.chat-tab[unRead!=1]').remove();
			$('.hd-tab-list li[unRead!=1]').remove();
			$('.im-right-sidebar>div[unRead!=1]').remove();
			$('.im-right-sidebar').hide();
			if($('.hd-tab-list li.current').size()==0){
				$('.hd-tab-list li').eq(0).trigger('click');
			}
			var u=new util.util();
				u.slideList();
			

		})

		
		//发送设置
		$('.im-btn-send-set').click(function(e){
			e.preventDefault();
			e.stopPropagation();
			$('.send-set').show();
		})

		$('.send-set').delegate('a','click',function(e){
		    e.preventDefault();
		    e.stopPropagation();
		    $('.send-set a').removeClass('current');
		    $(this).addClass('current');
		    $('.send-set').hide();
		})

		//点击左侧添加对话,此处作用比较微妙。。。略难以理解
		var TimeFn = null;
		$('.cus-group,.im-right-sidebar').delegate('a.tab-fav','click',function(e){
				e.preventDefault();
				e.stopPropagation();
				clearTimeout(TimeFn);
			    //执行延时
			    TimeFn = setTimeout(function(){
			        //do function在此处写单击事件要执行的代码
			    },300);
		})
		//点击左侧添加对话
		$('.cus-group,.im-right-sidebar').delegate('.tab-group-list>li','dblclick',function(e){
				e.preventDefault();
				e.stopPropagation();
				//clearTimeout(TimeFn);
				var data_id=$(this).attr('class'),
					uid=$(this).attr('uid'),
					pushtype=$(this).attr('pushtype'),
					state=$(this).attr('state'),
					servernum=$(this).attr('servernum'),
					msgid='',
					flag=true;
				
				if(pushtype !=4 && uid == userCustomeId)
				{
					// 不为群组时 如果打开的是自己的对话框，则不作处理
					return;
				}
				//msgid=typeof $(this).attr('msgid')=='undefined' ? '' : $(this).attr('msgid'),

				//检测是否存在该对象
				if($('.hd-tab-list li[data-id='+data_id+']').size()>0){
                    flag=false;
                }
				if($('.chat-tab[data-id='+data_id+']').size()>0){
				    msgid = $('.chat-tab[data-id='+data_id+']').find('.im-item').eq(0).attr('msgid');
				    msgid = typeof msgid =='undefined' ? '' : msgid;
				}
				//检测点击的是否为微信对象
				if(data_id.indexOf('cus')!=-1 || data_id.indexOf('kh-')!=-1){
					$('.im-t4').show();
				}else{
					$('.im-t4').hide();
				}
				
				if(flag===true){//不存在人物对话
				    
					// 设置为空，使得重新设置该属性值
				    //msgid = '';
				    
					var u=new util.util();	
					//添加至头部
					var newPerson=u.addToTop(this,data_id,uid,pushtype,state,servernum,msgid);
					//群组右边显示成员列表
					u.showGroup(data_id,uid);	
					//添加至悬浮位置		
					$('.hd-tab-list').prepend(newPerson).find('li:first').addClass('current').siblings().removeClass('current');
					$(".hd-tab-list > li[data-id='"+data_id+"']").find(".hd-tab-fav").attr("title",$(this).find('.cus-title').text());
					//添加相对应的聊天主体窗
					u.load_MsgBox(data_id);
					//添加至悬浮位置显示不下的放置在隐藏列表中
					u.slideList();
					//聊天输入框赋data-id
					u.load_EditMsg(data_id);
					//加载历史消息
					loadMoreMsg();
					//双击新消息图标去除
					u.removeMsgTips(this,data_id);
					$('#limit-count').html(650);
				}else{
					//聊天窗口已存在人物对话，则切换至选中状态
					var u=new util.util();
					u.choosePerson(this,data_id);
					u.showGroup(data_id,uid);

				}
			});

			//点击聊天窗口位置tab
			$('.contentcolumn').delegate('.hd-tab-list>li','click',function(){
				var u=new util.util();
				u.choosePerson(this);
			});


			//聊天tab选项卡鼠标经过状态
			$('.hd-tab-list').delegate('li',{
				mouseenter:function(){
						$(this).addClass('hover');
				},
				mouseleave:function(){
					
						$(this).removeClass('hover');
				}
			})
			//聊天tab选项卡关闭
			$('.hd-tab-list,#slide-others-group').delegate('.tab-close','click',function(e){
				e.stopPropagation();
				var tabli=$(this).parent().parent(),
					data_id=tabli.attr('data-id'),
					index=$('.hd-tab-list li[data-id='+data_id+']').index(),
					len=$('.hd-tab-list >li').size()-1;
					tabDiv=$('.chat-tab[data-id='+data_id+']'),
					current_edit_msg=$('.im-edit-msg[data-id='+data_id+']');
					var u=new util.util();
				if(index==len){//当前关闭的是最后一个
					if(tabli.hasClass('current')){
						$('.hd-tab-list li:eq(0)').addClass('current');
						$('.chat-tab:eq(0)').show();
						$('.im-edit-msg:eq(0)').show();
						var new_data_id=$('.hd-tab-list li:eq(0)').attr('data-id'),
							new_uid=$('.hd-tab-list li:eq(0)').attr('uid');
						u.showGroup(new_data_id,new_uid);	
					}
				}else{
					if(tabli.hasClass('current')){
						$('.hd-tab-list li').eq(index+1).addClass('current');
						$('.chat-tab').eq(index+1).show();
						$('.im-edit-msg').eq(index+1).show();
						var new_data_id=$('.hd-tab-list li').eq(index+1).attr('data-id'),
							new_uid=$('.hd-tab-list li').eq(index+1).attr('uid');
						u.showGroup(new_data_id,new_uid);
					}
				}

				

				$('.hd-tab-list li').eq(index).remove();
				$('.im-right-sidebar>div.'+data_id).remove();
				if($('.im-right-sidebar>div').size()==0){
					var u=new util.util();
					u.initChatContent();
				}
				tabli.remove();
				tabDiv.remove();
				current_edit_msg.remove();
				u.limit($('.im-edit-msg:visible'));

			})
			
			//左菜单客户列表鼠标经过显示备注修改
			$('#cus-group-custom .tab-group-list').delegate('li',{
				mouseenter:function(){
					if($(this).find('.tab-surmark').is(':hidden'))
					{
						$(this).find(".tab-remark").show();
					}
				},
				mouseleave:function(){
					$(this).find(".tab-remark").hide();					
				}
			})
			
			//点击备注
			$('#cus-group-custom').delegate('.tab-group-list .tab-remark','click',function(e){
				e.preventDefault();
				e.stopPropagation();
				var cusInput = $(this).parent().find(".cus-input");
				var $parent = $(this).parent();
				var name = $parent.find(".tab-main > span").text();
				var markName = name.substring(0,name.lastIndexOf("("));
				name = name.substring(name.lastIndexOf("("));
				$parent.find(".cus-input").val(markName);
				$parent.find(".cus-input").show();
				$parent.find(".cus-title").hide();
				$(this).prev(".tab-surmark").show();
				$(this).hide();
			});			

			//点击备注
			$('#cus-group-custom').delegate('.tab-group-list .tab-surmark','click',function(e){
				e.preventDefault();
				e.stopPropagation();
				var cusInput = $(this).parent().find(".cus-input");
				var $parent = $(this).parent();
				var name = $parent.find(".tab-main > span").text();
				var markName = name.substring(0,name.lastIndexOf("("));
				name = name.substring(name.lastIndexOf("("));
				var rname = $parent.find(".cus-input").val();
				cusInput.hide();
				if(rname != markName)
				{
					var uid = $parent.parent().attr("uid");
					$.post('customChat.htm',
						{
							method:'addRemark',
							userId:userCustomeId ,
							markId:uid,
							markName:rname,
							isAsync:'yes'
						},function(result){
							if(result == "outOfLogin")
				    		{
				    			doOut();
				    			return;
				    		}
							if("success"==result)
							{
								// 修改显示名称
								$parent.find(".tab-main > span").text(rname+name);
								$parent.attr("title",rname+name);
								// 设置聊天窗口选项卡的名称
								$(".hd-tab-list > li[data-id='kf-list-"+uid+"']").find(".hd-tab-fav").attr("title",rname+name);
								$(".hd-tab-list > li[data-id='kf-list-"+uid+"']").find(".tab-name").text(rname+name);
								// 设置打开的群组成员列表对应的名称
								$('.im-right-sidebar .tab-group-list li[uid='+uid+']').find(".tab-main > span").text(rname+name);
								$('.im-right-sidebar .tab-group-list li[uid='+uid+'] .tab-fav').attr("title",rname+name);
							}else
							{
								$parent.find(".tab-main > span").text(markName+name);
							}
							$parent.find(".cus-title").show();
						}
					)
				}else
				{
					$parent.find(".cus-title").show();
				}
				$(this).hide();
			});	
			//字符输入限制
			//存在多个窗口时 每个窗口都会触发事件 加上：visible只对当前可见窗口有效 
			$(document).delegate('.im-edit-msg:visible','keyup blur',function(e){
				var kc = window.event ? e.keyCode: e.which;
				if(kc>35 && kc < 41)
				{
					return true;
				}
				var len=650,
				show='#limit-count',
				content =$(this).html();
				var afterContent = EasyChat.Util.Parser.imgtoEmotion(content);
				var lastContent = removeHTMLTag2(afterContent);
				lastContent=escapeString2(lastContent);
				$("#htmlToText").html(lastContent);
				lastContent = $("#htmlToText").text();
				var length = lastContent.length;
				var result = len - length;
				
				var type = "";
				var data_id = $(".hd-tab-list li.current").attr("data-id");
				if(typeof(data_id)!= 'undefined' && data_id.indexOf('kh')!=-1){//APP
					type = "app";
					
					//APP表情发送不能超过20个
					var alen = $(this).find("img").length;
					if(alen > 20){
						$("#context-menu2").hide();
						//alert("表情发送不能超过20个！");
						var dia=art.dialog({
							title: '提示',
						    content: '表情发送不能超过20个！',
						    lock:true,
						    ok:function(){
						}})
						//$(this).find("img:last").remove();
						$(this).find("img:gt(19)").remove()
						return false;
					}
				}else{
					type = "";
				}
				afterContent = EasyChat.Util.Parser.all(lastContent,type);
				if (result >= 0){
					 if(afterContent.length < content.length)
					{
						$(this).html(afterContent);
					}
					$(show).html(result);
					$(this).attr('left',result);//显示剩余可输入字数 同时保存至对应的窗口以便切换回来展示
					var signstr = afterContent.substring(afterContent.length-3,afterContent.length);
					if(signstr == " />"){
						focusEnd(this);
					}
					//$('.im-edit-msg[data-id='+data_id+']').focus();
				}else{
					if(length>650){
						var oldContent = lastContent;//保留截取前的值
						//处理最后可能的表情截断
						lastContent = lastContent.substring(0,650);
						var index = lastContent.lastIndexOf("/");
						var leftcount = 0;
						if(index>640){
							var allstr = oldContent.substring(index);
							var all = EasyChat.Util.Parser.all(allstr,type);
							if(!/^\//.test(all)){
								var emostr = oldContent.substring(index,650);
								var emo = EasyChat.Util.Parser.all(emostr,type);
								if(/^\//.test(emo)){//表情被截断
									lastContent = lastContent.substring(0,index);
									leftcount = 650-index;
								}
							}
						}
						afterContent = EasyChat.Util.Parser.all(lastContent,type);
						$(this).html("");
						$(this).html(afterContent);
						$(show).html(leftcount);
						$(this).attr('left',leftcount);//显示剩余可输入字数 同时保存至对应的窗口以便切换回来展示
						focusEnd(this);
					}
					return false;
				}
			})

			$('.tab_menu').delegate('li','click',function(){
				var _index=$(this).index();
				var type = $("#chosedMenu").val();//当前选中的菜单类型(wx,app,custom)
				$(this).addClass('current').siblings().removeClass('current');
				$('#cus-group-'+type+' #chat-tab-box .tabContainer').eq(_index).removeClass('hide').siblings().addClass('hide');
				var u=new util.util();	
				u.initScrollOfUserList();
			})

			$(window).resize(function(){
				im.init();
			})
			im.lastVoice = {};
			//语音播放
			$(document).delegate('.voice','click',function(){
				var voice_path=$(this).attr('data-path'),
					voice_second=parseInt($(this).attr('data-second')),
					_this=$(this);
				//window.open(path+voice_path);
				//$('#voice-div').empty();
				//var voiceHtml = '<embed src="'+voice_path+'" type="audio/x-pn-realaudio-plugin" hidden=true autostart="true" loop="false" id="MediaPlayer" name="MediaPlayer"/>';
				//$('#voice-div').append(voiceHtml);
				$.post(path+'/customChat.hts?method=playVoice',{voiceFile: voice_path},function(result){
					if(result == 'noFile')
					{
						alert('音频文件不存在，无法播放！');
						return;
					}
					else if(result != 'voiceSuccess')
					{
						alert('读取音频文件失败！');
						return;
					} 
					var browserStr = userAgent();
					if(browserStr.indexOf('chrome')== -1 ){
						$('#browerVer').val(1);
					}else
					{
						$('#browerVer').val(0);
					}
					$('#voiceFile').val(voice_path);
					play(path+'/'+voice_path);
					if(im.lastVoice.element){
						clearTimeout(im.lastVoice.timer);
						im.lastVoice.element.attr('src',iPath+'/static/images/voice_static.png');
					}
					_this.attr('src',iPath+'/static/images/voice_run.gif');
					im.lastVoice.element = _this;
					im.lastVoice.timer = setTimeout(function(){
					    _this.attr('src',iPath+'/static/images/voice_static.png');
					},(voice_second+1)*1000);
				});
				
			});


		im.init=function(){/*设置各个容器高度*/
			var im_header=$('.im-header'),
				contentcolumn=$('.contentcolumn'),
				im_sidebar=$('.im-sidebar'),
				im_container=$('.im_container'),
				im_chat_list=$('.im-chat-list'),
				im_right_sidebar=$('.im-right-sidebar'),
				im_right_hd=$('.contentcolumn > .hd').height(),
				im_toolbar=$('.im-toolbar').height(),
				im_send_area=$('.im-send-area').outerHeight(),
				im_edit_btn_area=$('.im-edit-btn-area').height(),
			    clientH=$(window).height(),
			    dh=clientH-im_header.height()+'px',
			    slide_others=$('#slide-others'),
			    slide_others_list=$('#slide-other-list'),
			    chatH=clientH-im_right_hd-im_toolbar-im_send_area-im_edit_btn_area-50;
			    chatpx=chatH+'px';	
			im_container.height(dh);
			im_sidebar.height(dh);
			contentcolumn.height(dh);
			im_chat_list.height(chatpx);
			im_right_sidebar.height(chatpx);
			slide_others.height(chatH-25+'px');
			slide_others_list.height(chatH-25-55+'px');
			//slide_others.find('#slide-others-group').height(chatH-25-55+'px');
			$('.tab_box').height(parseInt(dh)-33-25+'px');
			/*自定义滚动条*/
			
			$("#cus-group-wx #chat-tab-box,#cus-group-app #chat-tab-box,#cus-group-custom #chat-tab-box").nanoScroller({ preventPageScrolling: true });
			setTimeout(function(){
			    $("#chat-tab-box").nanoScroller();
			}, 300);

			var u=new util.util();
			u.slideList();
		};

		im.dropdown=function(dropdown,menu){
			$(document).delegate(dropdown,'click',function(e){/*点击显示下拉列表*/
				e.preventDefault();
				e.stopPropagation();
				var oMenu=$(dropdown).siblings(menu);
				oMenu.toggle();
				oMenu.delegate('a','click',function(e){/*点击切换在线状态*/
				    
					e.preventDefault();
					e.stopPropagation();
					var oClass=$(this).prop('class'),//只限单个类名
						sText=$(this).html();
					$(dropdown).prop('class',stausClass).addClass(oClass).html(sText);
					$(menu).hide();	

				});
				
			})
			
		};
		 $(document).delegate('.leaveGroup','click',function(e){
             e.stopPropagation();
             var groupid = $(this).attr('uid');
             req(['artDialog','iframeTools'],function(){
                 var dlog=art.dialog({
                     title: '退出群组确认',
                     lock: true,
                     content: '是否退出当前该群组？',
                     background: "#000",
                     opacity: 0.87,
                     ok:function(){
                	 	 $('button.aui_state_highlight').attr('disabled',true);
                         $.post('customChat.htm?method=leaveGroup',{groupid:groupid,customeid:userCustomeId,name:kfname,isAsync:'yes'},
                                 function(result){
                        	 if(result == "outOfLogin")
 				    		{
 				    			doOut();
 				    			return;
 				    		}
                             if(result.indexOf('group:')==0 && result != 'group:')
                             {
                                result = result.substring(6);
                                if('success' == result)
                                {
                                    $('.mod-group').find('li[uid='+groupid+']').remove();
                                    var $hdtab = $('.hd-tab-list').find('li[uid='+groupid+'][pushtype=4]');
                                    $hdtab.find('> div > .tab-close').trigger('click');
                                    art.dialog.tips('退出群组成功！');
                                    setGroupKey();
                                }else
                                {
                                    art.dialog.tips('退出群组失败！');
                                    return false;
                                }
                             }else
                             {
                                 art.dialog.tips('退出群组失败！');
                                 return false;
                             }
                             $('button.aui_state_highlight').attr('disabled',false);
                             dlog.close();
                         });
                         return false;
                     },
                     cancel: true
                 });
                 dlog.show();
                 
                 var u=new util.util();
                 u.initScrollOfUserList();
             })
        })
			$(document).delegate('.edit-pwd','click',function(e){
				e.preventDefault();
				e.stopPropagation();
				req(['artDialog','iframeTools'],function(){
				    var passdilog = art.dialog({
						title:'修改密码',
					    lock: true,
					    background: '#000', // 背景色
					    opacity: 0.87,	// 透明度
					    content: document.getElementById('form-edit-password'),
					    ok:function(){
			            $('#zhu').text('');
			            var oldPass = $.trim($('#oldPass').val());
			            var newPass1 = $.trim($('#newPass1').val());
			            var newPass2 = $.trim($('#newPass2').val());
			            if(oldPass=='' )
			            {
			                $('#zhu').text('*请输入原密码！');
			                $('#oldPass').select();
			                return false;
			            }
			            if(newPass1 =='' || newPass1.length < 4 || newPass1.length > 20)
			            {
			                $('#zhu').text('*请输入新密码，密码长度为4-20！');
			                $('#newPass1').select();
			                return false;
			            }
			            if( newPass2 == '' )
			            {
			                $('#zhu').text('*请输入确认新密码！');
			                $('#newPass2').select();
			                return false;
			            }
			            if( newPass2 != newPass1 )
			            {
			                $('#zhu').text('*新密码输入不一致！');
			                $('#newPass1').select();
			                return false;
			            }
			            $.post('frame.htm?method=updatePass',
			            {
			                newPass: newPass1,
			                oldPass: oldPass,
			                isAsync:'yes'
			            },function(result){
			                if('outOfLogin' == result)
			                {
			                    showLogin(0);
			                    return false;
			                }
			                if('errorpass' == result)
			                {
			                    $('#zhu').text('*原密码输入不正确！');
			                    return false;
			                }
			                if('true' == result)
			                {
			                    $('#zhu').text('');
			                    passdilog.close();
			                    art.dialog.tips('修改密码成功！');
			                }else
			                {
			                    $('#zhu').text('*修改密码失败！');
			                    return false;
			                }
			            });
			            return false;
			        },
					    cancel: true
					});
				})
			
			})
		
		//创建群组
		$(document).delegate('a#add_group','click',function(e){
			e.preventDefault();
			e.stopPropagation();
			var dlog;
			req(['artDialog','iframeTools'],function(){
				var aboutConfig={
			        title: '新建群组',
			        lock: true,
			        background: "#000",
			        opacity: 0.87,
			        width:555,
			        height:405,
			        ok:function(){
			        	var group_name = art.dialog.data('group_name'),
			        		group_list = art.dialog.data('group_list');
			        	if(group_name == undefined || group_name.length == 0)
			        	{
			        		art.dialog.tips('请输入群组名称！');
			        		return false;
			        	}
			        	if(group_list == undefined || group_list.length == 0)
			        	{
			        		art.dialog.tips('请给该群组添加客服人员！');
			        	    return false;
			        	}
			        	if($.trim(group_name) == "")
			        	{
			        		art.dialog.tips('群组名称不能全是空格！');
			        		return false;
			        	}
			        	if(group_list == undefined )
                        {
			        	    group_list == userCustomeId;
                        }else
                        {
                            group_list += ','+userCustomeId;
                        }
			        	$.post('customChat.htm?method=addGroup',{gpName:group_name,customeId:userCustomeId,userId:group_list,aId : aId,isAsync : 'yes'},
			        	        function(result){
			        		if(result == "outOfLogin")
				    		{
				    			doOut();
				    			return;
				    		}
			        	    if(result.indexOf('group:')==0 && result != 'group:')
			        	    {
			        	        result = result.substring(6);
                                //console.log(result);
                                result=eval('('+result+')');
                                dlog.close();
                                art.dialog.tips('群组创建成功！');
                                var gourpHtml = '<li class="group-list-'+result.groupid+'" uid="'+result.groupid
                                    +'" pushtype="4" count="'+result.count
                                    +'"><a href="" class="tab-fav cc"><div class="tab-left"><img src="'
                                    +iPath+'/static/images/icon_group.png" alt=""></div><div class="tab-right">'
                                    +'<span class="count"></span></div><div class="tab-main"><span class="cus-title">'
                                    +escapeString(result.name)+'</span></div></a></li>';
                                $('.mod-group').prepend(gourpHtml);
                                setGroupKey();
                                
                                var u=new util.util();
                                u.initScrollOfUserList();
                                
			        	    }else if(result == 'samename')
			        	    {
			        	    	art.dialog.tips('群组名称重复，不予创建！');
			        	        return false;
			        	    }else
			        	    {
			        	    	art.dialog.tips('新增群组失败！');
			        	        return false;
			        	    }
			        	});
			        	return false;
			        },
				    cancel: true
			    };
				//art.dialog.open(iPath + "/group.html", aboutConfig);
				dlog=art.dialog.open(path + '/customChat.htm?method=showAddGroup&aId='+
		   		      aId + '&customeId='+userCustomeId, aboutConfig);
			})
			
		})
		
		//转接客服
		$('#tranService').click(function(){
			req(['artDialog','iframeTools'],function(){
				var pushType = $('.hd-tab-list li.current').attr('pushtype');
				var titleText = '7'==pushType?'转接APP客服':'转接微信客服';
				var aboutConfig={
			        title: titleText,
			        lock: true,
			        background: "#000",
			        opacity: 0.87,
				    cancel: true,
				    width:550,
				    height:300,
				    ok:function(){
				    	var service = art.dialog.data('service'),
		        		dest = art.dialog.data('dest'),
		        		serviceName=art.dialog.data('serviceName');
		        		if(typeof service=='undefined' || service==''){
		        			art.dialog.tips('请选择转接客服人员');
		        			return false;
		        		}else if(typeof dest=='undefined' || dest==''){
		        			art.dialog.tips('请输入转接客服说明');
		        			return false;
		        		}else{
		        			/*serverNum：服务号
							customeId：发起转接的客服Id
							toCustome：转接目标
							aId：公众号Id
							msg：转接说明
							name：发起转接的客服姓名
							openId：// 客户微信号*/
							var options={
								serverNum:$('.hd-tab-list li.current').attr('serverNum'),
	        					customeId:userCustomeId,
	        					toCustome:service,
	        					aId:aId,
	        					msg:dest,
	        					name:kfname,
	        					isAsync:'yes',
	        					openId:$('.hd-tab-list li.current').attr('uid')
							};
		        			$.ajax({
		        				method:'POST',
		        				url:'customChat.htm?method=converCust&time='+new Date().getTime(),
		        				data:options,
		        				success:function(result){
			        				if(result == "outOfLogin")
			    		    		{
			    		    			doOut();
			    		    			return;
			    		    		}
									var cancelService='{serverNum:\''+options.serverNum+'\',customeId:'+options.customeId+',toCustome:'+options.toCustome+'}';
		        					var msg="";
		        					if(result=='turning'){
		        						msg='已向客服['+serviceName+']发送申请,请勿重复提交!<a href="javascript:void(0)" cancelService="'+cancelService+'" class="cancelService">点击此处取消转接</a>';
		        					}else if(result=='customout'){
		        						msg='该客户已经退出本次服务！';
		        					}else if(result=='error'){
		        						msg='转接失败！';
		        					}else{
		        						msg=result+' 已向客服['+serviceName+']发送申请,正在转接中...<a href="javascript:void(0)" cancelService="'+cancelService+'" class="cancelService">点击此处取消转接</a>';
		        					}
		        					//显示到对应的窗口中
		        					var data_id=$('.hd-tab-list li.current').attr('data-id');

		        					var panel=$('#end-panel').find('div.chatMsg').clone();

									panel.addClass('im-msg-success').find('.im-icon').addClass('im-icon-success');

									panel.find('p').html(msg);

									$('.chat-tab[data-id='+data_id+']').append(panel);

									var u=new util.util();
									u.initScroll();
		        				}
		        			})
		        			
		        		}
		        		return true;
				    }
			    };
				art.dialog.open(path + '/customChat.htm?method=showTrans&aId='+
                    aId + '&customeId='+userCustomeId+'&pushType='+pushType, aboutConfig);
         	})
		})
		/*参数：
		serverNum：服务号
		fromCust：发起转接的客服Id
		toCustome：转接目标
		aId：公众号Id
		openId：// 客户微信号
		code:如果是1则接受0为拒绝*/
		$(document).delegate('a.acceptinfo','click',function(){
			var options={
				serverNum:$(this).parent().attr('servernum'),
				fromCust:$(this).parent().attr('fromuser'),
				toCustome:userCustomeId,
				aId:aId,
				code:$(this).attr('code'),
				openId:$(this).parent().attr('openid'),
				isAsync : 'yes'
			};
			var _this=$(this);
			$.ajax({
				method:'POST',
				url:'customChat.htm?method=agreeConver&time='+new Date().getTime(),
				data:options,
				success:function(result){
					//console.log(result);
					if(result == "outOfLogin")
		    		{
		    			doOut();
		    			return;
		    		}
					var msg;
					if(result=='conver:noconver'){
		        		msg='该转接请求已被取消！';
		        	}else if(result=="conver:cancel"){
		        		msg="该转接请求已被拒绝！";
		        	}else if (result=='conver:customout')
		        	{
		        		msg="客户已经退出了本次客服！";
		        	}else if(result.indexOf("conver:") == 0 && result != "conver:"){
		        		var showid = 'converhis_'+options.serverNum+new Date().getTime();
		        		$('body').append('<div class="hide" id = "'+showid+'"></div>');
						var historyJson=result.substr(7),
						msg='转接成功，<a href="javascript:void(0)" showid="'+showid+'" class="showHistory" servernum ="'+options.serverNum+'" >点击此处查看转接前历史消息</a>';
						$('#'+showid).text(historyJson);
					}
					var panel=$('#end-panel').find('div.chatMsg').clone();

					var data_id=$('.hd-tab-list li.current').attr('data-id');

					panel.addClass('im-msg-success').find('.im-icon').addClass('im-icon-success');

					panel.find('p').html(msg);

					$('.chat-tab[data-id='+data_id+']').append(panel);
					//显示到对应的窗口中
					var reg=/(.*?)(\<a.*?>(.*?)<\/a>)(.*?)/gi,arr=[];
					_this.parent().html().replace(reg,function(match, p1, p2, p3,p4, offset, string){
						arr.push(p1,p3);
					});
					_this.parent().html(arr.join(' '));
					
				}
			})
		})
		/*
		取消转接
		serverNum：服务号
		customeId：发起转接的客服Id
		toCustome：转接目标
		aId：公众号Id
		name：发起转接的客服姓名
		iscancel：传数字1（此参数如果不传或传其他的值则为发起转接请求）
		*/
		$(document).delegate('a.cancelService','click',function(){
			var cancelService=eval('('+$(this).attr('cancelService')+')');
			var _this=$(this);
			$.ajax({
				method:'POST',
				url:'customChat.htm?method=converCust&time='+new Date().getTime(),
				data:{
					serverNum:cancelService.serverNum,
					customeId:cancelService.customeId,
					toCustome:cancelService.toCustome,
					aId:aId,
					name:kfname,
					iscancel:1,
					isAsync:'yes'
				},
				success:function(result){
					if(result == "outOfLogin")
		    		{
		    			doOut();
		    			return;
		    		}
					//console.log(result);

					var panel=$('#end-panel').find('div.chatMsg').clone();

					var data_id=$('.hd-tab-list li.current').attr('data-id');

					panel.addClass('im-msg-success').find('.im-icon').addClass('im-icon-success');

					if('noconver' == result)
					{
					    panel.find('p').html('转接已经结束，无需取消！');
					}else
					{
					    panel.find('p').html('取消转接成功！');
					}

					$('.chat-tab[data-id='+data_id+']').append(panel);

				
					//显示到对应的窗口中
					var reg=/(.*?)(\<a.*?>(.*?)<\/a>)(.*?)/gi,arr=[];
					_this.parent().html().replace(reg,function(match, p1, p2, p3,p4, offset, string){
						arr.push(p1,p3);
					});
					_this.parent().html(arr.join(' '));
					
				}
			})
		})

		$(document).delegate('.showHistory','click',function(){
			var serverNum = $(this).attr('showid');
			var data_msg=eval('('+$('#'+serverNum).text()+')');
			var historyMsg='<div class="historyMsg">';
			var u=new util.util();
			for(var attr in data_msg){
				if(data_msg[attr].pushtype==1){
					var sayClass="cusSay";
				}else{
					var sayClass="kfSay";
				}
				var message="";
				message=u.getMessage2(data_msg[attr].msgtype,path,data_msg[attr].message,data_msg[attr].pushtype);

				historyMsg+='<div class="msgList '+sayClass+'">'
						+'<div class="title">'+data_msg[attr].name+'<span class="time">'+data_msg[attr].time+'</span></div>'
						+'<div class="inner">'+message+'</div>'
					+'</div>';
			}
			historyMsg+='</div>';
			req(['artDialog'],function(){
				art.dialog({
					title:'客服转接历史聊天记录',
				    content: historyMsg,
			        lock: false,
			        background: "#000",
			        opacity: 0.87,
			        width:'20em'
				});
			});
		});

		$(document).delegate('.msgDetail','click',function(){
			var servernum=$(this).attr('showid');
			req(['artDialog'],function(){
				art.dialog({
					title:'客服转接消息',
				    content: escapeString($('#'+servernum).text()),
			        lock: false,
			        background: "#000",
			        opacity: 0.87,
			        width:'20em'
				});
			});
		});

		/*
		customChat.htm?method=readHisMsg
		参数：
		customeId：当前的客服id
		objId: 聊天对象为客户时，传服务号，聊天对象为客服或群组则传
		pushType：消息推送类型
		msgId: 消息id，取聊天窗口第一条消息的msgid，没有则传空字符串
		count:查询数量
		*/
		function loadMoreMsg(options){
			var target=$('.hd-tab-list li.current')
			var data_id=target.attr('data-id'),
				pushtype=target.attr('pushtype'),
				msgid=typeof target.attr('msgid')=='undefined' ? '' : target.attr('msgid'),
				objId;
				//console.log(target.attr('msgid'));	
			if(data_id.indexOf('cus-')!=-1 ){
				objId=target.attr('servernum');
			}else{
				objId=target.attr('uid');
			}
				var defaults={
					customeId:userCustomeId,
					objId:objId,
					pushType:pushtype,
					msgId:msgid,
					count:3,
					isAsync : 'yes'
				};
			$.extend(true,defaults,options);
			//console.log(defaults);	
			$.ajax({
				method:'POST',
				url:'customChat.htm?method=readHisMsg&time='+new Date().getTime(),
				data:defaults,
				cache:false,
				success:function(result){
					//console.log(result);
					if(result == "outOfLogin")
		    		{
		    			doOut();
		    			return;
		    		}
					if(result.indexOf('msghis:')!=-1 && result.substring(7)!=''){
						result=eval('('+result.substring(7)+')');
						var u=new util.util();
						var chatTab=$('.chat-tab[data-id='+data_id+']'),panelHeight=0,msgid="",item_panel="";
						
						if(chatTab.find('.historybg').size()<=0){
							chatTab.prepend('<div class="historyContent"><div class="historybg"></div></div>');
						}
						
						for(var attr in result){
							var name=result[attr].name,
								msgtype=result[attr].msgtype,
								time=result[attr].time,
								message=result[attr].message,
								msgid=result[attr].msgid,
								fromuser =result[attr].fromuser;
							message=u.getMessage(msgtype,path,message);
							if(msgtype=='zjkf'){
								message=eval('('+message+')');
								message=message.msg;
							}
							var boxpic = "kh_wei.png";
							if(result[attr].fromuser==userCustomeId){
								item_panel=$('#item-me-panel');
								name="我";
								if(aId != 0)
								{
									boxpic = "user1_wei.png";
								}else
								{
									boxpic = "user1.png";
								}
							}else{
								if(pushtype == "3" || pushtype == "4")
								{
									var iswei = $('.kf-list-'+fromuser).attr('iswei');
									if(iswei == 1)
									{
										boxpic = 'user1_wei.png';
									}else
									{
										boxpic = 'user1.png';
									}
								}else if(pushtype == "7")
								{
									boxpic = 'kh_app.png';
								}
								item_panel=$('#item-panel');
							}
							
							var panel=item_panel.find('div.im-item').clone();
							panel.find('.im-txt-bold').html(name);
							panel.find('.im-send-time').html(time);
							panel.find('.im-message-content').html(message);
							panel.find('.userPic').attr('src',iPath+'/static/images/'+boxpic);
							chatTab.prepend(panel);
							panelHeight+=panel.height();
							

						}
						$('.historyLoadding',chatTab).parent().remove();
						chatTab.prepend('<div class="historyContent"><a href="javascript:void(0)" class="historyLoadding" msgid="'+msgid+'">查看更多消息</a></div>');
						$("#chat-nano").nanoScroller({ preventPageScrolling: true });
						setTimeout(function(){
						    $("#chat-nano").nanoScroller({scrollTop:panelHeight});
						}, 500);
					}
				}
			});
		}

		/**
		 * 重新设置群组id集合
		 */
		function setGroupKey()
		{
		    var newGroupKey = "";
		    $('.mod-group li').each(function(){
		        newGroupKey += $(this).attr('uid')+',';
		    })
		    if(newGroupKey.length > 0 )
		    {
		        newGroupKey = newGroupKey.substring(0,newGroupKey.length);
		    }
		    groupKeys = newGroupKey;
		}

		$(document).delegate('a.historyLoadding','click',function(){
			loadMoreMsg({
				msgId:$(this).attr('msgid'),
				count:5
			});
			
		})
		
		im.preventDefault=function(){
			$('.hd-tab-list').delegate('.hd-tab-fav','click',function(e){
				e.preventDefault();
			});
		};
		
		//光标停在最后
		function focusEnd(obj){
			var sel = window.getSelection();
			var range = document.createRange();
			range.selectNodeContents(obj);
			range.collapse(false);
			sel.removeAllRanges();
			sel.addRange(range);
		}
		//重置上传文件控件
		function resetfileinput(){
			 $('#file_upload').wrap("<form></form>");
		     var $form = $('#file_upload').parent();
		     $form[0].reset();
		     $('#file_upload').unwrap();
		     $('#file_upload').val('');
		}
		
		function play(url){
			document.getElementById('bubbleContent').innerHTML = '';
			var videoPath=path+"/common/widget/video/";
			var mp3Array=[],temp;
			var data = videoPath+'/mp3/dewplayer-mini.swf?mp3='+url+'&autostart=1';
			mp3Array.push('<object style="width:20px" type="application/x-shockwave-flash" data="'+data+'" width="160" height="20" id="dewplayer-mini">');
			mp3Array.push('<param name="wmode" value="transparent" />');
			mp3Array.push('<param name="movie" value="'+data+'" />');
			mp3Array.push('</object>');
			temp=mp3Array.join('');
			document.getElementById('bubbleContent').innerHTML = temp;
		}
		return {
			im:im
		};
});