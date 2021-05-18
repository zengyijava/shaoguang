UE.registerUI('cmusic',function(editor,uiName){

    var dialog = new UE.ui.Dialog({
        iframeUrl:'rms/ueditor/dialogs/cmusic/index.html',
        editor:editor,
        name:uiName,
        title:"插入音频",

        cssRules:"width:640px;height:400px;",

        buttons:[
            {
                className:'edui-okbutton',
                label:'确定',
                onclick:function () {
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

    var btn = new UE.ui.Button({
        name:'cust'+uiName,
        title:'插入音频',
        label:'插入音频',
        onclick:function () {
            dialog.render();
            dialog.open();
        }
    });

    return btn;
},1);