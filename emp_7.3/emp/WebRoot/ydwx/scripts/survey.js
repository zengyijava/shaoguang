var dialog;
var editor;
var args;
(function (){
    var iframe = window.frameElement;
    var parentWindow = window.parent;
    //alert(iframe.id);
    //var dialogId = iframe.id.replace(/_iframe$/, '');
    //dialog = parentWindow.$EDITORUI[dialogId];
    //editor = dialog.editor;
    editor = parentWindow.editor;
})();
