baidu.editor.commands['insertactividata'] = {
 queryCommandState : function(){
             
            
            var a=this.highlight ? -1 :0;
              alert(a);
            return a;
    },
      
    execCommand : function(cmd,options) {
       editor.execCommand('inserthtml', options);
     
    }
    
};