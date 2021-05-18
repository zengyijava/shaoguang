UE.registerUI('params',function(editor,uiName){

    //创建dialog
    var dialog = new UE.ui.Dialog({
        //指定弹出层中页面的路径，这里只能支持页面,因为跟addCustomizeDialog.js相同目录，所以无需加路径
        iframeUrl:'rms/ueditor/params.html',
        //需要指定当前的编辑器实例
        editor:editor,
        //指定dialog的名字
        name:uiName,
        //dialog的标题
        title:"插入参数",

        //指定dialog的外围样式
        cssRules:"width:480px;height:300px;",

        //如果给出了buttons就代表dialog有确定和取消
        buttons:[
            {
                className:'edui-okbutton',
                label:'确定',
                onclick:function () {
                	var paramsDialogs =  document.getElementsByClassName("edui-for-params");
                	var childWindow = paramsDialogs[paramsDialogs.length-1].getElementsByTagName('iframe')[0].contentWindow;
                	var obj=childWindow.document.getElementsByName('params'); 
            		for(var i=0; i<obj.length; i++){ 
            		if(obj[i].checked){
            			var s = obj[i].value;
//            			editor.setContent(s,true);
            			editor.focus();
            			editor.execCommand('inserthtml',s);  
            		} 
            		} 
                    dialog.close(true);
                }
            },
            {
                className:'edui-cancelbutton',
                label:'取消',
                onclick:function () {
                    dialog.close(false);
                }
            }
        ]});

    //参考addCustomizeButton.js
    var btn = new UE.ui.Button({
        name:'cust'+uiName,
        title:'插入参数',
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        label:'插入参数',
        onclick:function () {
            //渲染dialog
            dialog.render();
            dialog.open();
        }
    });

    return btn;
}/*index 指定添加到工具栏上的那个位置，默认时追加到最后,editorId 指定这个UI是那个编辑器实例上的，默认是页面上所有的编辑器都会添加这个按钮*/);