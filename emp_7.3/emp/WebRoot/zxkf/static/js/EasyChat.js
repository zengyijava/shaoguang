var EasyChat=(function(self){
	
	self.about = {
		name: 'EasyChat',
		version: '1.0'
	};
	
	self.init = function(options) {
		if (!options.viewClass) {
			options.viewClass = self.View;
		}
		self.type=options.type || '';
		options.viewClass.init($('#EasyChat'), options.view);
	};
	return self;
}(EasyChat || {}));

EasyChat.Util=(function(self){
	self.setCookie = function(name, value, lifetime_days) {
		var exp = new Date();
		exp.setDate(new Date().getDate() + lifetime_days);
		document.cookie = name + '=' + value + ';expires=' + exp.toUTCString() + ';path=/';
	};
	
	self.cookieExists = function(name) {
		return document.cookie.indexOf(name) > -1;
	};
	
	self.getCookie = function(name) {
		if(document.cookie)	{
			var regex = new RegExp(escape(name) + '=([^;]*)', 'gm'),
				matches = regex.exec(document.cookie);
			if(matches) {
				return matches[1];
			}
		}
	};
	
	self.deleteCookie = function(name) {
		document.cookie = name + '=;expires=Thu, 01-Jan-70 00:00:01 GMT;path=/';
	};

	
	self.getPosLeftAccordingToWindowBounds = function(elem, pos) {
		var windowWidth = $(document).width(),
			elemWidth   = elem.outerWidth(),
			marginDiff = elemWidth - elem.outerWidth(true),
			backgroundPositionAlignment = 'left';

		if (pos + elemWidth >= windowWidth) {
			pos -= elemWidth - marginDiff;
			backgroundPositionAlignment = 'right';
		}else{
			pos -=45;
		}

		return { px: pos, backgroundPositionAlignment: backgroundPositionAlignment };
	};
	
	self.getPosTopAccordingToWindowBounds = function(elem, pos) {
		var windowHeight = $(document).height(),
			elemHeight   = elem.outerHeight(),
			marginDiff = elemHeight - elem.outerHeight(true),
			backgroundPositionAlignment = 'top';

		if (pos + elemHeight >= windowHeight) {
			pos -= elemHeight - marginDiff;
			backgroundPositionAlignment = 'bottom';
		}else{
			pos-=elemHeight- marginDiff+30;
		}

		return { px: pos, backgroundPositionAlignment: backgroundPositionAlignment };
	};
	
	self.Parser={
		
		_emoticonPath: '',

		
		setEmoticonPath: function(path) {
			this._emoticonPath = path;
		},
		
		emoticons1: [
			{
				plain: '/::)',
				title: '??????',
				regex: /\/::\)/g,
				image: '1.gif'
			},
			{
				plain: '/::~',
				title: '??????',
				regex: /\/::~/g,
				image: '2.gif'
			},
			{
				plain: '/::B',
				title: '??????',
				regex: /\/::B/g,
				image: '3.gif'
			},
			{
				plain: '/::|',
				title: '??????',
				regex: /\/::\|/g,
				image: '4.gif'
			},
			{
				plain: '/:8-)',
				title: '??????',
				regex: /\/:8-\)/g,
				image: '5.gif'
			},
			{
				plain: '/::<',
				title: '???',
				regex: /\/::&lt;/g,
				image: '6.gif'
			},
			{
				plain: '/::$',
				title: '???',
				regex: /\/::\$/g,
				image: '7.gif'
			},
			{
				plain: '/::X',
				title: '???',
				regex: /\/::X/g,
				image: '8.gif'
			},
			{
				plain: '/::Z',
				title: '???',
				regex: /\/::Z/g,
				image: '9.gif'
			},
			{
				plain: "/::'(",
				title: '???',
				regex: /\/::\'\(/g,
				image: '10.gif'
			},
			{
				plain: '/::-|',
				title: '???',
				regex: /\/::-\|/g,
				image: '11.gif'
			},
			{
				plain: '/::@',
				title: '???',
				regex: /\/::@/g,
				image: '12.gif'
			},
			{
				plain: '/::P',
				title: '??????',
				regex: /\/::P/g,
				image: '13.gif'
			},
			{
				plain: '/::D',
				title: '???',
				regex: /\/::D/g,
				image: '14.gif'
			},
			{
				plain: '/::O',
				title: '??????',
				regex: /\/::O/g,
				image: '15.gif'
			},
			{
				plain: '/::(',
				title: '??????',
				regex: /\/::\(/g,
				image: '16.gif'
			},
			{
				plain: '/::+',
				title: '???',
				regex: /\/::\+/g,
				image: '17.gif'
			},
			{
				plain:'/:--b',
				title:'???',
				regex:/\/:--b/g,
				image:'18.gif'
			},
			{
				plain: '/::Q',
				title: '??????',
				regex: /\/::Q/g,
				image: '19.gif'
			},
			{
				plain: '/::T',
				title: '???',
				regex: /\/::T/g,
				image: '20.gif'
			},
			{
				plain: '/:,@P',
				title: '???',
				regex: /\/:,@P/g,
				image: '21.gif'
			},
			{
				plain: '/:,@-D',
				title: '??????',
				regex: /\/:,@-D/g,
				image: '22.gif'
			},
			{
				plain: '/::d',
				title: '???',
				regex: /\/::d/g,
				image: '23.gif'
			},
			{
				plain: '/:,@o',
				title: '???',
				regex: /\/:,@o/g,
				image: '24.gif'
			},
			{
				plain: '/::g',
				title: '???',
				regex: /\/::g/g,
				image: '25.gif'
			},
			{
				plain: '/:|-)',
				title: '???',
				regex: /\/:\|-\)/g,
				image: '26.gif'
			},
			{
				plain: '/::!',
				title: '???',
				regex: /\/::!/g,
				image: '27.gif'
			},
			{
				plain: '/::L',
				title: '???',
				regex: /\/::L/g,
				image: '28.gif'
			},
			{
				plain: '/::>',
				title: '??????',
				regex: /\/::&gt;/g,
				image: '29.gif'
			},
			{
				plain: '/::,@',
				title: '???',
				regex: /\/::,@/g,
				image: '30.gif'
			},
			{
				plain: '/:,@f',
				title: '??????',
				regex: /\/:,@f/g,
				image: '31.gif'
			},
			{
				plain: '/::-S',
				title: '???',
				regex: /\/::-S/g,
				image: '32.gif'
			},
			{
				plain: '/:?',
				title: '??????',
				regex: /\/:\?/g,
				image: '33.gif'
			},
			{
				plain: '/:,@x',
				title: '??????',
				regex: /\/:,@x/g,
				image: '34.gif'
			},
			{
				plain: '/:,@@',
				title: '???',
				regex: /\/:,@@/g,
				image: '35.gif'
			},
			{
				plain: '/::8',
				title: '???',
				regex: /\/::8/g,
				image: '36.gif'
			},
			{
				plain: '/:,@!',
				title: '???',
				regex: /\/:,@!/g,
				image: '37.gif'
			},
			{
				plain: '/:!!!',
				title: '???',
				regex: /\/:!!!/g,
				image: '38.gif'
			},
			{
				plain: '/:xx',
				title: '??????',
				regex: /\/:xx/g,
				image: '39.gif'
			},
			{
				plain: '/:bye',
				title: '??????',
				regex: /\/:bye/g,
				image: '40.gif'
			},
			{
				plain: '/:wipe',
				title: '???',
				regex: /\/:wipe/g,
				image: '41.gif'
			},
			{
				plain: '/:dig',
				title: '???',
				regex: /\/:dig/g,
				image: '42.gif'
			},
			{
				plain: '/:handclap',
				title: '??????',
				regex: /\/:handclap/g,
				image: '43.gif'
			},
			{
				plain: '/:&-(',
				title: '??????',
				regex: /\/:\&amp;\-\(/g,
				image: '44.gif'
			},
			{
				plain: '/:B-)',
				title: '??????',
				regex: /\/:B-\)/g,
				image: '45.gif'
			},
			{
				plain: '/:<@',
				title: '??????',
				regex: /\/:&lt;@/g,
				image: '46.gif'
			},
			{
				plain: '/:@>',
				title: '??????',
				regex: /\/:@&gt;/g,
				image: '47.gif'
			},
			{
				plain: '/::-O',
				title: '???',
				regex: /\/::-O/g,
				image: '48.gif'
			},
			{
				plain: '/:>-|',
				title: '???',
				regex: /\/:&gt;-\|/g,
				image: '49.gif'
			},
			{
				plain: '/:P-(',
				title: '??????',
				regex: /\/:P-\(/g,
				image: '50.gif'
			},
			{
				plain: "/::'|",
				title: '??????',
				regex: /\/::'\|/g,
				image: '51.gif'
			},
			{
				plain: '/:X-)',
				title: '???',
				regex: /\/:X-\)/g,
				image: '52.gif'
			},
			{
				plain: '/::*',
				title: '???',
				regex: /\/::\*/g,
				image: '53.gif'
			},
			{
				plain: '/:@x',
				title: '???',
				regex: /\/:@x/g,
				image: '54.gif'
			},
			{
				plain: '/:8*',
				title: '??????',
				regex: /\/:8\*/g,
				image: '55.gif'
			},
			{
				plain: '/:pd',
				title: '???',
				regex: /\/:pd/g,
				image: '56.gif'
			},
			{
				plain: '/:<W>',
				title: '??????',
				regex: /\/:&lt;W&gt;/g,
				image: '57.gif'
			},
			{
				plain: '/:beer',
				title: '???',
				regex: /\/:beer/g,
				image: '58.gif'
			},
			{
				plain: '/:basketb',
				title: '??????',
				regex: /\/:basketb/g,
				image: '59.gif'
			},
			{
				plain: '/:oo',
				title: '??????',
				regex: /\/:oo/g,
				image: '60.gif'
			},
			{
				plain: '/:coffee',
				title: '??????',
				regex: /\/:coffee/g,
				image: '61.gif'
			},
			{
				plain: '/:eat',
				title: '??????',
				regex: /\/:eat/g,
				image: '62.gif'
			},
			{
				plain: '/:pig',
				title: '??????',
				regex: /\/:pig/g,
				image: '63.gif'
			},
			{
				plain: '/:rose',
				title: '??????',
				regex: /\/:rose/g,
				image: '64.gif'
			},
			{
				plain: '/:fade',
				title: '???',
				regex: /\/:fade/g,
				image: '65.gif'
			},
			{
				plain: '/:showlove',
				title: '???',
				regex: /\/:showlove/g,
				image: '66.gif'
			},
			{
				plain: '/:heart',
				title: '???',
				regex: /\/:heart/g,
				image: '67.gif'
			},
			{
				plain: '/:break',
				title: '??????',
				regex: /\/:break/g,
				image: '68.gif'
			},
			{
				plain: '/:cake',
				title: '??????',
				regex: /\/:cake/g,
				image: '69.gif'
			},
			{
				plain: '/:li',
				title: '???',
				regex: /\/:li/g,
				image: '70.gif'
			},
		],
		
		emoticons2: [
			{
				plain: '/no',
				title: '???',
				regex: /\/no/g,
				image: 'f001.png'
			},
			{
				plain: '/ok',
				title: '???',
				regex: /\/ok/g,
				image: 'f002.png'
			},
			{
				plain: '/lb',
				title: '??????',
				regex: /\/lb/g,
				image: 'f003.png'
			},
			{
				plain: '/bb',
				title: '??????',
				regex: /\/bb/g,
				image: 'f004.png'
			},
			{
				plain: '/bt',
				title: '??????',
				regex: /\/bt/g,
				image: 'f005.png'
			},
			{
				plain: '/bshang',
				title: '??????',
				regex: /\/bshang/g,
				image: 'f006.png'
			},
			{
				plain: '/bshi',
				title: '??????',
				regex: /\/bshi/g,
				image: 'f007.png'
			},
			{
				plain: '/bz',
				title: '??????',
				regex: /\/bz/g,
				image: 'f008.png'
			},
			{
				plain: '/ch',
				title: '??????',
				regex: /\/ch/g,
				image: 'f009.png'
			},
			{
				plain: "/gd",
				title: '??????',
				regex: /\/gd/g,
				image: 'f010.png'
			},
			{
				plain: '/zy',
				title: '??????',
				regex: /\/zy/g,
				image: 'f011.png'
			},
			{
				plain: '/dk',
				title: '??????',
				regex: /\/dk/g,
				image: 'f012.png'
			},
			{
				plain: '/dx',
				title: '??????',
				regex: /\/dx/g,
				image: 'f013.png'
			},
			{
				plain: '/yh',
				title: '??????',
				regex: /\/yh/g,
				image: 'f014.png'
			},
			{
				plain: '/dg',
				title: '??????',
				regex: /\/dg/g,
				image: 'f015.png'
			},
			{
				plain: '/dy',
				title: '??????',
				regex: /\/dy/g,
				image: 'f016.png'
			},
			{
				plain: '/dj',
				title: '??????',
				regex: /\/dj/g,
				image: 'f017.png'
			},
			{
				plain:'/fd',
				title:'??????',
				regex:/\/fd/g,
				image:'f018.png'
			},
			{
				plain: '/fn',
				title: '??????',
				regex: /\/fn/g,
				image: 'f019.png'
			},
			{
				plain: '/fw',
				title: '??????',
				regex: /\/fw/g,
				image: 'f020.png'
			},
			{
				plain: '/gz',
				title: '??????',
				regex: /\/gz/g,
				image: 'f021.png'
			},
			{
				plain: '/hx',
				title: '??????',
				regex: /\/hx/g,
				image: 'f022.png'
			},
			{
				plain: '/hanx',
				title: '??????',
				regex: /\/hanx/g,
				image: 'f023.png'
			},
			{
				plain: '/hch',
				title: '??????',
				regex: /\/hch/g,
				image: 'f024.png'
			},
			{
				plain: '/huaix',
				title: '??????',
				regex: /\/huaix/g,
				image: 'f025.png'
			},
			{
				plain: '/jie',
				title: '??????',
				regex: /\/jie/g,
				image: 'f026.png'
			},
			{
				plain: '/jk',
				title: '??????',
				regex: /\/jk/g,
				image: 'f027.png'
			},
			{
				plain: '/ksh',
				title: '??????',
				regex: /\/ksh/g,
				image: 'f028.png'
			},
			{
				plain: '/kl',
				title: '??????',
				regex: /\/kl/g,
				image: 'f029.png'
			},
			{
				plain: '/cd',
				title: '??????',
				regex: /\/cd/g,
				image: 'f030.png'
			},
			{
				plain: '/lh',
				title: '??????',
				regex: /\/lh/g,
				image: 'f031.png'
			},
			{
				plain: '/mbf',
				title: '?????????',
				regex: /\/mbf/g,
				image: 'f032.png'
			},
			{
				plain: '/outu',
				title: '??????',
				regex: /\/outu/g,
				image: 'f033.png'
			},
			{
				plain: '/pmy',
				title: '?????????',
				regex: /\/pmy/g,
				image: 'f034.png'
			},
			{
				plain: '/gb',
				title: '??????',
				regex: /\/gb/g,
				image: 'f035.png'
			},
			{
				plain: '/qiang',
				title: '???',
				regex: /\/qiang/g,
				image: 'f036.png'
			},
			{
				plain: '/dhq',
				title: '?????????',
				regex: /\/dhq/g,
				image: 'f037.png'
			},
			{
				plain: '/qw',
				title: '??????',
				regex: /\/qw/g,
				image: 'f038.png'
			},
			{
				plain: '/ruo',
				title: '???',
				regex: /\/ruo/g,
				image: 'f039.png'
			},
			{
				plain: '/se',
				title: '???',
				regex: /\/se/g,
				image: 'f040.png'
			},
			{
				plain: '/tsh',
				title: '??????',
				regex: /\/tsh/g,
				image: 'f041.png'
			},
			{
				plain: '/shl',
				title: '??????',
				regex: /\/shl/g,
				image: 'f042.png'
			},
			{
				plain: '/tyy',
				title: '?????????',
				regex: /\/tyy/g,
				image: 'f043.png'
			},
			{
				plain: '/tx',
				title: '??????',
				regex: /\/tx/g,
				image: 'f044.png'
			},
			{
				plain: '/wx',
				title: '??????',
				regex: /\/wx/g,
				image: 'f045.png'
			},
			{
				plain: '/wn',
				title: '??????',
				regex: /\/wn/g,
				image: 'f046.png'
			},
			{
				plain: '/wq',
				title: '??????',
				regex: /\/wq/g,
				image: 'f047.png'
			},
			{
				plain: '/wsh',
				title: '??????',
				regex: /\/wsh/g,
				image: 'f048.png'
			},
			{
				plain: '/wl',
				title: '??????',
				regex: /\/wl/g,
				image: 'f049.png'
			},
			{
				plain: '/wun',
				title: '??????',
				regex: /\/wun/g,
				image: 'f050.png'
			},
			{
				plain: "/xh",
				title: '??????',
				regex: /\/xh/g,
				image: 'f051.png'
			},
			{
				plain: '/xdl',
				title: '?????????',
				regex: /\/xdl/g,
				image: 'f052.png'
			},
			{
				plain: '/sk',
				title: '??????',
				regex: /\/sk/g,
				image: 'f053.png'
			},
			{
				plain: '/xsh',
				title: '??????',
				regex: /\/xsh/g,
				image: 'f054.png'
			},
			{
				plain: '/yy',
				title: '??????',
				regex: /\/yy/g,
				image: 'f055.png'
			},
			{
				plain: '/yw',
				title: '??????',
				regex: /\/yw/g,
				image: 'f056.png'
			},
			{
				plain: '/ym',
				title: '??????',
				regex: /\/ym/g,
				image: 'f057.png'
			},
			{
				plain: '/yun',
				title: '???',
				regex: /\/yun/g,
				image: 'f058.png'
			},
			{
				plain: '/zhm',
				title: '??????',
				regex: /\/zhm/g,
				image: 'f059.png'
			},
			{
				plain: '/dm',
				title: '??????',
				regex: /\/dm/g,
				image: 'f060.png'
			},
		],
		
		emotify: function(text,type) {
			var i,emo;
			if(type=='app' || self.type=='app'){
				emo=this.emoticons2;
			}else{
				emo=this.emoticons1;
			}
			for(i = emo.length-1; i >= 0; i--) {
				text = text.replace(emo[i].regex, '<img class="emoticon" title="'+emo[i].title+'" alt="'+emo[i].plain+'" src="' + this._emoticonPath +emo[i].image + '" />');
			}
			return text;
		},

		imgtoEmotion:function(text){
			var reg=/(.*?)(\<img.*?alt=\"(.*?)\".*?>)(.*?)/gi;
			return text.replace(reg,function(match, p1, p2, p3, offset, string){
				p3=p3.replace(/&lt;/,'<').replace(/&gt;/,'>');
				return [p1, p3].join('');
			})
		},

		
		linkify: function(text) {
			text = text.replace(/(^|[^\/])(www\.[^\.]+\.[\S]+(\b|$))/gi, '$1http://$2');
			return text.replace(/(\b(https?|ftp|file):\/\/[\-A-Z0-9+&@#\/%?=~_|!:,.;]*[\-A-Z0-9+&@#\/%=~_|])/ig, '<a href="$1" target="_blank">$1</a>');
		},
		
		escape: function(text) {
			// ??????iphone?????????????????????
			while(text.indexOf("/::&lt;") > -1)
			{
				text=text.replace("/::&lt;","/::<");
			}
			while(text.indexOf("/:&lt;@") > -1)
			{
				text=text.replace("/:&lt;@","/:<@");
			}
			while(text.indexOf("/:&lt;W>") > -1)
			{
				text=text.replace("/:&lt;W>","/:<W>");
			}
			while(text.indexOf("/:&amp;-(") > -1)
			{
				text=text.replace("/:&amp;-(","/:&-(");
			}
			return $('<div/>').text(text).html().replace(/&amp;amp;/gi,'&').replace(/&amp;nbsp;/gi,'&nbsp;').replace(/\/huanhang\//gi,'<br/>');
		},
		
		nl2br: function(text) {
			return text.replace(/\r\n|\r|\n/g, '<br />');
		},

		
		all: function(text,type) {
			if(text) {
				text = this.escape(text);
				//text = this.linkify(text);
				text = this.emotify(text,type);
				//text = this.nl2br(text);
			}
			return text;
		}

	}; 
	return self;
}(EasyChat.Util || {}));

EasyChat.View=(function(self){
	_options = {
		language: 'en',
		resources: 'res/',
		messages: { limit: 2000, remove: 500 },
		crop: {
			message: { nickname: 15, body: 1000 },
			roster: { nickname: 15 }
		}
	},
	
	_initToolbar = function() {
		self.Pane.Chat.Toolbar.init();
	},
	
	self.init = function(container, options) {
		$.extend(true, _options, options);
		EasyChat.Util.Parser.setEmoticonPath(this.getOptions().resources + '/images/emoticons/');
		_initToolbar();
	};

	
	self.getOptions = function() {
		return _options;
	};	
	return self;
}(EasyChat.view || {}));

EasyChat.View.Pane=(function(self){
	self.Chat={
		
		Toolbar: {
			_supportsNativeAudio: false,

			init: function() {
				//????????????
				$('#emoticons-icon').click(function(e) {
					//var type = $("#chosedMenu").val();//???????????????????????????(wx,app,custom)
					var data_id = $(".hd-tab-list li.current").attr("data-id");
					if(data_id.indexOf('kh')!=-1){//APP
						self.Chat.Context.showEmoticonsMenu2(e.currentTarget);
					}else{
						self.Chat.Context.showEmoticonsMenu(e.currentTarget);
					}
					
					e.stopPropagation();
				});
				
				$('#context-menu').mouseleave(function() {
						$(this).fadeOut('fast');
				});
				$(document).click(function() {
						$('#context-menu').fadeOut('fast');
				});
				$('#context-menu2').mouseleave(function() {
					$(this).fadeOut('fast');
				});
				$(document).click(function() {
						$('#context-menu2').fadeOut('fast');
				});				
			}
		},
		
		Context: {
			
			showEmoticonsMenu: function(elem) {
				elem = $(elem);
				emo=EasyChat.Util.Parser.emoticons1;
				var pos = elem.offset(),
					menu = $('#context-menu'),
					content = $('ul', menu),
					emoticons = '';
				for(var i=0;i<emo.length-1;i++) {
					emoticons = '<img src="' + EasyChat.Util.Parser._emoticonPath + emo[i]['image'] + '" alt="' + emo[i]['plain'] + '" title="' + emo[i]['title'] + '" />' + emoticons;
				}
				content.html('<li class="emoticons">' + emoticons + '</li>');
				content.find('img').click(function(e) {
					e.stopPropagation();
					var getSendArea=EasyChat.View.Pane.Room.getSendArea();
						//value = getSendArea.html(),
						//emoticon = $(this).attr('alt') + ' ';
						getSendArea.append($(this).clone());
						//getSendArea.html(value ? value + ' ' + emoticon : emoticon);
						
						$(".im-edit-msg").keyup();
				});	
				var posLeft = EasyChat.Util.getPosLeftAccordingToWindowBounds(menu, pos.left),
					posTop  = EasyChat.Util.getPosTopAccordingToWindowBounds(menu, pos.top);

				menu
					.css({'left': posLeft.px, 'top': posTop.px})
					.removeClass('left-top left-bottom right-top right-bottom')
					.addClass(posLeft.backgroundPositionAlignment + '-' + posTop.backgroundPositionAlignment)
					.fadeIn('fast');

				return true;
			},
			showEmoticonsMenu2: function(elem) {
				elem = $(elem);
				emo=EasyChat.Util.Parser.emoticons2;
				var pos = elem.offset(),
					menu = $('#context-menu2'),
					content = $('ul', menu),
					emoticons = '';
				for(var i=0;i<emo.length-1;i++) {
					emoticons = '<img src="' + EasyChat.Util.Parser._emoticonPath + emo[i]['image'] + '" alt="' + emo[i]['plain'] + '" title="' + emo[i]['title'] + '" />' + emoticons;
				}
				content.html('<li class="emoticons">' + emoticons + '</li>');
				content.find('img').click(function(e) {
					e.stopPropagation();
					var getSendArea=EasyChat.View.Pane.Room.getSendArea();
						//value = getSendArea.html(),
						//emoticon = $(this).attr('alt') + ' ';
						getSendArea.append($(this).clone());
						//getSendArea.html(value ? value + ' ' + emoticon : emoticon);
						
						$(".im-edit-msg").keyup();
				});	
				var posLeft = EasyChat.Util.getPosLeftAccordingToWindowBounds(menu, pos.left),
					posTop  = EasyChat.Util.getPosTopAccordingToWindowBounds(menu, pos.top);

				menu
					.css({'left': posLeft.px, 'top': posTop.px})
					.removeClass('left-top left-bottom right-top right-bottom')
					.addClass(posLeft.backgroundPositionAlignment + '-' + posTop.backgroundPositionAlignment)
					.fadeIn('fast');

				return true;
			}
		}
	};
	
	self.Room = {
		
		getSendArea:function(){
			var data_id=self.Room.getDataId();
			return $('.im-edit-msg[data-id='+data_id+']');
		},
		getDataId:function(){
			var cur_hd_tab=$('.hd-tab-list li.current'),
			data_id=cur_hd_tab.attr('data-id');
			return data_id;	
		} 
	}; 
	return self;
}(EasyChat.View.Pane || {}));
