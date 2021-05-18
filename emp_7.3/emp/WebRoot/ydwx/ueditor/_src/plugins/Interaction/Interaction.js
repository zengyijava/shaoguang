 
(function (){
    baidu.editor.plugins['interaction'] = function (){
        var editor = this;
        var fakedMap = {};
        var fakedPairs = [];
        var lastFakedId = 0;
 //       function fake(url, width, height,style){
 //           var fakedId = 'edui_faked_video_' + (lastFakedId ++);
 //           var fakedHtml = '<img isfakedvideo id="'+ fakedId +'" width="'+ width +'" height="' + height + '" _url="'+url+'" class="edui-faked-video"' +
 //               ' src="http://hi.baidu.com/fc/editor/images/spacer.gif"' +
 //               ' style="background:url(http://hi.baidu.com/ui/neweditor/lib/fck/images/fck_videologo.gif) no-repeat center center; border:1px solid gray;'+ style +';" />';
 //           fakedMap[fakedId] = '<embed isfakedvideo' +
 //               ' type="application/x-shockwave-flash"' +
 //               ' pluginspage="http://www.macromedia.com/go/getflashplayer"' +
 //               ' src="' + url + '"' +
 //               ' width="' + width + '"' +
 //               ' height="' + height + '"' +
 //               ' wmode="transparent"' +
//                ' play="true"' +
 //               ' loop="false"' +
 //               ' menu="false"' +
 //               ' allowscriptaccess="never"' +
 //               '></embed>';
 //           return fakedHtml;
 //       }
        editor.commands['insertinteraction'] = {
            execCommand: function (cmd, options){
           //     var url = options.url;
           //     var width = options.width || 320;
            //    var height = options.height || 240;
           //     var style = options.style ? options.style : "";
           //     var _voide = fake(url, width, height,style);
                editor.execCommand('inserthtml', options);
            },
             queryCommandState : function(){
                return this.highlight ? -1 :0;
            }
        };
        //获得style里的某个样式对应的值
        function getPars(str,par){
            var reg = new RegExp(par+":\\s*((\\w)*)","ig");
            var arr = reg.exec(str);
            return arr ? arr[1] : "";
        }

        

    };
})();
