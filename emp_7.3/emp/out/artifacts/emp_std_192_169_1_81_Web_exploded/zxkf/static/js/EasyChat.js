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
				title: '微笑',
				regex: /\/::\)/g,
				image: '1.gif'
			},
			{
				plain: '/::~',
				title: '伤心',
				regex: /\/::~/g,
				image: '2.gif'
			},
			{
				plain: '/::B',
				title: '美女',
				regex: /\/::B/g,
				image: '3.gif'
			},
			{
				plain: '/::|',
				title: '发呆',
				regex: /\/::\|/g,
				image: '4.gif'
			},
			{
				plain: '/:8-)',
				title: '墨镜',
				regex: /\/:8-\)/g,
				image: '5.gif'
			},
			{
				plain: '/::<',
				title: '哭',
				regex: /\/::&lt;/g,
				image: '6.gif'
			},
			{
				plain: '/::$',
				title: '羞',
				regex: /\/::\$/g,
				image: '7.gif'
			},
			{
				plain: '/::X',
				title: '哑',
				regex: /\/::X/g,
				image: '8.gif'
			},
			{
				plain: '/::Z',
				title: '睡',
				regex: /\/::Z/g,
				image: '9.gif'
			},
			{
				plain: "/::'(",
				title: '哭',
				regex: /\/::\'\(/g,
				image: '10.gif'
			},
			{
				plain: '/::-|',
				title: '囧',
				regex: /\/::-\|/g,
				image: '11.gif'
			},
			{
				plain: '/::@',
				title: '怒',
				regex: /\/::@/g,
				image: '12.gif'
			},
			{
				plain: '/::P',
				title: '调皮',
				regex: /\/::P/g,
				image: '13.gif'
			},
			{
				plain: '/::D',
				title: '笑',
				regex: /\/::D/g,
				image: '14.gif'
			},
			{
				plain: '/::O',
				title: '惊讶',
				regex: /\/::O/g,
				image: '15.gif'
			},
			{
				plain: '/::(',
				title: '难过',
				regex: /\/::\(/g,
				image: '16.gif'
			},
			{
				plain: '/::+',
				title: '酷',
				regex: /\/::\+/g,
				image: '17.gif'
			},
			{
				plain:'/:--b',
				title:'汗',
				regex:/\/:--b/g,
				image:'18.gif'
			},
			{
				plain: '/::Q',
				title: '抓狂',
				regex: /\/::Q/g,
				image: '19.gif'
			},
			{
				plain: '/::T',
				title: '吐',
				regex: /\/::T/g,
				image: '20.gif'
			},
			{
				plain: '/:,@P',
				title: '笑',
				regex: /\/:,@P/g,
				image: '21.gif'
			},
			{
				plain: '/:,@-D',
				title: '快乐',
				regex: /\/:,@-D/g,
				image: '22.gif'
			},
			{
				plain: '/::d',
				title: '奇',
				regex: /\/::d/g,
				image: '23.gif'
			},
			{
				plain: '/:,@o',
				title: '傲',
				regex: /\/:,@o/g,
				image: '24.gif'
			},
			{
				plain: '/::g',
				title: '饿',
				regex: /\/::g/g,
				image: '25.gif'
			},
			{
				plain: '/:|-)',
				title: '累',
				regex: /\/:\|-\)/g,
				image: '26.gif'
			},
			{
				plain: '/::!',
				title: '吓',
				regex: /\/::!/g,
				image: '27.gif'
			},
			{
				plain: '/::L',
				title: '汗',
				regex: /\/::L/g,
				image: '28.gif'
			},
			{
				plain: '/::>',
				title: '高兴',
				regex: /\/::&gt;/g,
				image: '29.gif'
			},
			{
				plain: '/::,@',
				title: '闲',
				regex: /\/::,@/g,
				image: '30.gif'
			},
			{
				plain: '/:,@f',
				title: '努力',
				regex: /\/:,@f/g,
				image: '31.gif'
			},
			{
				plain: '/::-S',
				title: '骂',
				regex: /\/::-S/g,
				image: '32.gif'
			},
			{
				plain: '/:?',
				title: '疑问',
				regex: /\/:\?/g,
				image: '33.gif'
			},
			{
				plain: '/:,@x',
				title: '秘密',
				regex: /\/:,@x/g,
				image: '34.gif'
			},
			{
				plain: '/:,@@',
				title: '乱',
				regex: /\/:,@@/g,
				image: '35.gif'
			},
			{
				plain: '/::8',
				title: '疯',
				regex: /\/::8/g,
				image: '36.gif'
			},
			{
				plain: '/:,@!',
				title: '哀',
				regex: /\/:,@!/g,
				image: '37.gif'
			},
			{
				plain: '/:!!!',
				title: '鬼',
				regex: /\/:!!!/g,
				image: '38.gif'
			},
			{
				plain: '/:xx',
				title: '打击',
				regex: /\/:xx/g,
				image: '39.gif'
			},
			{
				plain: '/:bye',
				title: '拜拜',
				regex: /\/:bye/g,
				image: '40.gif'
			},
			{
				plain: '/:wipe',
				title: '汗',
				regex: /\/:wipe/g,
				image: '41.gif'
			},
			{
				plain: '/:dig',
				title: '抠',
				regex: /\/:dig/g,
				image: '42.gif'
			},
			{
				plain: '/:handclap',
				title: '鼓掌',
				regex: /\/:handclap/g,
				image: '43.gif'
			},
			{
				plain: '/:&-(',
				title: '糟糕',
				regex: /\/:\&amp;\-\(/g,
				image: '44.gif'
			},
			{
				plain: '/:B-)',
				title: '恶搞',
				regex: /\/:B-\)/g,
				image: '45.gif'
			},
			{
				plain: '/:<@',
				title: '什么',
				regex: /\/:&lt;@/g,
				image: '46.gif'
			},
			{
				plain: '/:@>',
				title: '什么',
				regex: /\/:@&gt;/g,
				image: '47.gif'
			},
			{
				plain: '/::-O',
				title: '累',
				regex: /\/::-O/g,
				image: '48.gif'
			},
			{
				plain: '/:>-|',
				title: '看',
				regex: /\/:&gt;-\|/g,
				image: '49.gif'
			},
			{
				plain: '/:P-(',
				title: '难过',
				regex: /\/:P-\(/g,
				image: '50.gif'
			},
			{
				plain: "/::'|",
				title: '难过',
				regex: /\/::'\|/g,
				image: '51.gif'
			},
			{
				plain: '/:X-)',
				title: '坏',
				regex: /\/:X-\)/g,
				image: '52.gif'
			},
			{
				plain: '/::*',
				title: '亲',
				regex: /\/::\*/g,
				image: '53.gif'
			},
			{
				plain: '/:@x',
				title: '吓',
				regex: /\/:@x/g,
				image: '54.gif'
			},
			{
				plain: '/:8*',
				title: '可怜',
				regex: /\/:8\*/g,
				image: '55.gif'
			},
			{
				plain: '/:pd',
				title: '刀',
				regex: /\/:pd/g,
				image: '56.gif'
			},
			{
				plain: '/:<W>',
				title: '水果',
				regex: /\/:&lt;W&gt;/g,
				image: '57.gif'
			},
			{
				plain: '/:beer',
				title: '酒',
				regex: /\/:beer/g,
				image: '58.gif'
			},
			{
				plain: '/:basketb',
				title: '篮球',
				regex: /\/:basketb/g,
				image: '59.gif'
			},
			{
				plain: '/:oo',
				title: '乒乓',
				regex: /\/:oo/g,
				image: '60.gif'
			},
			{
				plain: '/:coffee',
				title: '咖啡',
				regex: /\/:coffee/g,
				image: '61.gif'
			},
			{
				plain: '/:eat',
				title: '美食',
				regex: /\/:eat/g,
				image: '62.gif'
			},
			{
				plain: '/:pig',
				title: '动物',
				regex: /\/:pig/g,
				image: '63.gif'
			},
			{
				plain: '/:rose',
				title: '鲜花',
				regex: /\/:rose/g,
				image: '64.gif'
			},
			{
				plain: '/:fade',
				title: '枯',
				regex: /\/:fade/g,
				image: '65.gif'
			},
			{
				plain: '/:showlove',
				title: '唇',
				regex: /\/:showlove/g,
				image: '66.gif'
			},
			{
				plain: '/:heart',
				title: '爱',
				regex: /\/:heart/g,
				image: '67.gif'
			},
			{
				plain: '/:break',
				title: '分手',
				regex: /\/:break/g,
				image: '68.gif'
			},
			{
				plain: '/:cake',
				title: '生日',
				regex: /\/:cake/g,
				image: '69.gif'
			},
			{
				plain: '/:li',
				title: '电',
				regex: /\/:li/g,
				image: '70.gif'
			},
		],
		
		emoticons2: [
			{
				plain: '/no',
				title: '不',
				regex: /\/no/g,
				image: 'f001.png'
			},
			{
				plain: '/ok',
				title: '好',
				regex: /\/ok/g,
				image: 'f002.png'
			},
			{
				plain: '/lb',
				title: '喇叭',
				regex: /\/lb/g,
				image: 'f003.png'
			},
			{
				plain: '/bb',
				title: '拜拜',
				regex: /\/bb/g,
				image: 'f004.png'
			},
			{
				plain: '/bt',
				title: '拜托',
				regex: /\/bt/g,
				image: 'f005.png'
			},
			{
				plain: '/bshang',
				title: '悲伤',
				regex: /\/bshang/g,
				image: 'f006.png'
			},
			{
				plain: '/bshi',
				title: '鄙视',
				regex: /\/bshi/g,
				image: 'f007.png'
			},
			{
				plain: '/bz',
				title: '闭嘴',
				regex: /\/bz/g,
				image: 'f008.png'
			},
			{
				plain: '/ch',
				title: '擦汗',
				regex: /\/ch/g,
				image: 'f009.png'
			},
			{
				plain: "/gd",
				title: '感动',
				regex: /\/gd/g,
				image: 'f010.png'
			},
			{
				plain: '/zy',
				title: '呲牙',
				regex: /\/zy/g,
				image: 'f011.png'
			},
			{
				plain: '/dk',
				title: '大哭',
				regex: /\/dk/g,
				image: 'f012.png'
			},
			{
				plain: '/dx',
				title: '大笑',
				regex: /\/dx/g,
				image: 'f013.png'
			},
			{
				plain: '/yh',
				title: '疑惑',
				regex: /\/yh/g,
				image: 'f014.png'
			},
			{
				plain: '/dg',
				title: '蛋糕',
				regex: /\/dg/g,
				image: 'f015.png'
			},
			{
				plain: '/dy',
				title: '得意',
				regex: /\/dy/g,
				image: 'f016.png'
			},
			{
				plain: '/dj',
				title: '度假',
				regex: /\/dj/g,
				image: 'f017.png'
			},
			{
				plain:'/fd',
				title:'发呆',
				regex:/\/fd/g,
				image:'f018.png'
			},
			{
				plain: '/fn',
				title: '发怒',
				regex: /\/fn/g,
				image: 'f019.png'
			},
			{
				plain: '/fw',
				title: '飞吻',
				regex: /\/fw/g,
				image: 'f020.png'
			},
			{
				plain: '/gz',
				title: '鼓掌',
				regex: /\/gz/g,
				image: 'f021.png'
			},
			{
				plain: '/hx',
				title: '害羞',
				regex: /\/hx/g,
				image: 'f022.png'
			},
			{
				plain: '/hanx',
				title: '憨笑',
				regex: /\/hanx/g,
				image: 'f023.png'
			},
			{
				plain: '/hch',
				title: '喝茶',
				regex: /\/hch/g,
				image: 'f024.png'
			},
			{
				plain: '/huaix',
				title: '坏笑',
				regex: /\/huaix/g,
				image: 'f025.png'
			},
			{
				plain: '/jie',
				title: '饥饿',
				regex: /\/jie/g,
				image: 'f026.png'
			},
			{
				plain: '/jk',
				title: '惊恐',
				regex: /\/jk/g,
				image: 'f027.png'
			},
			{
				plain: '/ksh',
				title: '瞌睡',
				regex: /\/ksh/g,
				image: 'f028.png'
			},
			{
				plain: '/kl',
				title: '可怜',
				regex: /\/kl/g,
				image: 'f029.png'
			},
			{
				plain: '/cd',
				title: '菜刀',
				regex: /\/cd/g,
				image: 'f030.png'
			},
			{
				plain: '/lh',
				title: '流汗',
				regex: /\/lh/g,
				image: 'f031.png'
			},
			{
				plain: '/mbf',
				title: '没办法',
				regex: /\/mbf/g,
				image: 'f032.png'
			},
			{
				plain: '/outu',
				title: '呕吐',
				regex: /\/outu/g,
				image: 'f033.png'
			},
			{
				plain: '/pmy',
				title: '抛媚眼',
				regex: /\/pmy/g,
				image: 'f034.png'
			},
			{
				plain: '/gb',
				title: '干杯',
				regex: /\/gb/g,
				image: 'f035.png'
			},
			{
				plain: '/qiang',
				title: '强',
				regex: /\/qiang/g,
				image: 'f036.png'
			},
			{
				plain: '/dhq',
				title: '打哈欠',
				regex: /\/dhq/g,
				image: 'f037.png'
			},
			{
				plain: '/qw',
				title: '亲吻',
				regex: /\/qw/g,
				image: 'f038.png'
			},
			{
				plain: '/ruo',
				title: '弱',
				regex: /\/ruo/g,
				image: 'f039.png'
			},
			{
				plain: '/se',
				title: '色',
				regex: /\/se/g,
				image: 'f040.png'
			},
			{
				plain: '/tsh',
				title: '吐舌',
				regex: /\/tsh/g,
				image: 'f041.png'
			},
			{
				plain: '/shl',
				title: '胜利',
				regex: /\/shl/g,
				image: 'f042.png'
			},
			{
				plain: '/tyy',
				title: '听音乐',
				regex: /\/tyy/g,
				image: 'f043.png'
			},
			{
				plain: '/tx',
				title: '偷笑',
				regex: /\/tx/g,
				image: 'f044.png'
			},
			{
				plain: '/wx',
				title: '微笑',
				regex: /\/wx/g,
				image: 'f045.png'
			},
			{
				plain: '/wn',
				title: '为难',
				regex: /\/wn/g,
				image: 'f046.png'
			},
			{
				plain: '/wq',
				title: '委屈',
				regex: /\/wq/g,
				image: 'f047.png'
			},
			{
				plain: '/wsh',
				title: '握手',
				regex: /\/wsh/g,
				image: 'f048.png'
			},
			{
				plain: '/wl',
				title: '无聊',
				regex: /\/wl/g,
				image: 'f049.png'
			},
			{
				plain: '/wun',
				title: '无奈',
				regex: /\/wun/g,
				image: 'f050.png'
			},
			{
				plain: "/xh",
				title: '吓唬',
				regex: /\/xh/g,
				image: 'f051.png'
			},
			{
				plain: '/xdl',
				title: '想到了',
				regex: /\/xdl/g,
				image: 'f052.png'
			},
			{
				plain: '/sk',
				title: '思考',
				regex: /\/sk/g,
				image: 'f053.png'
			},
			{
				plain: '/xsh',
				title: '小声',
				regex: /\/xsh/g,
				image: 'f054.png'
			},
			{
				plain: '/yy',
				title: '咬牙',
				regex: /\/yy/g,
				image: 'f055.png'
			},
			{
				plain: '/yw',
				title: '疑问',
				regex: /\/yw/g,
				image: 'f056.png'
			},
			{
				plain: '/ym',
				title: '郁闷',
				regex: /\/ym/g,
				image: 'f057.png'
			},
			{
				plain: '/yun',
				title: '晕',
				regex: /\/yun/g,
				image: 'f058.png'
			},
			{
				plain: '/zhm',
				title: '折磨',
				regex: /\/zhm/g,
				image: 'f059.png'
			},
			{
				plain: '/dm',
				title: '大骂',
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
			// 兼容iphone的表情字符处理
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
				//表情事件
				$('#emoticons-icon').click(function(e) {
					//var type = $("#chosedMenu").val();//当前选中的菜单类型(wx,app,custom)
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
