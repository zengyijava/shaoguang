///import core
///import commands\paragraph.js
///commands 首行缩进
/**
 * 首行缩进
 * @function
 * @name baidu.editor.execCommand
 * @param   {String}   cmdName     outdent取消缩进，indent缩进
 */
(function (){
    var domUtils = baidu.editor.dom.domUtils;
    baidu.editor.commands['outdent'] = baidu.editor.commands['indent'] = {
        execCommand : function(cmd) {
             var me = this,
                 value = cmd.toLowerCase() == 'outdent' ? '0em' : (me.options.indentValue || '2em');
             me.execCommand('Paragraph','p',{'textIndent':value});
        },
        queryCommandState : function(cmd) {
            if(this.highlight){
                       return -1;
                   }
            var start = this.selection.getStart(),
                pN = domUtils.findParentByTagName(start,['p', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6'],true),
                indent = pN && pN.style.textIndent ? parseInt(pN.style.textIndent) : '';
            return indent ?  (cmd == 'indent' ?
                (indent > 0 ? 1 : 0)
                : (indent==0 ? 1 : 0))
                : cmd == 'outdent' ? 1 : 0
        }

    };
})();
