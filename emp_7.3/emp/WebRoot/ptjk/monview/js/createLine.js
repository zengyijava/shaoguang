/*写此原生js原因：在框架内刷新，ie版本下虚线时有时无*/
function CreateLine(option){
  this.defaults=option;
  this.monitor=this.getDom(option.monitorBox);
  this.initialize();
}

CreateLine.prototype={
    getId:function(id){
      return document.getElementById(id);
    },
    getByClassName:function(className,parent){
      var elem = [],
        node = parent != undefined&&parent.nodeType==1?parent.getElementsByTagName('*'):document.getElementsByTagName('*'),
        p = new RegExp("(^|\\s)"+className+"(\\s|$)");
      for(var n=0,i=node.length;n<i;n++){
        if(p.test(node[n].className)){
          elem.push(node[n]);
        }
      }
      return elem;
    },
    getDom:function(s){
      var nodeName = s.split(' '),
        p = this.getId(nodeName[0].slice(1)),
        c = this.getByClassName(nodeName[1].slice(1),p);
      if(!p || c.length==0) return null;
      return c;
    },
    hasClass:function(element,className){
      return element.className.match(new RegExp('(\\s|^)'+className+'(\\s|$)')); 
    },
    initialize:function(){
        var myCanvas = this.getId("myCanvas");
        myCanvas.width = 43;
        myCanvas.height = 380;
        for(var i=0;i<this.monitor.length;i++){
          var _height=parseInt(this.monitor[i].offsetHeight),
              mon_height=(_height+2)*(i+1)-_height/2,
              bgColor;
          if(this.hasClass(this.monitor[i],'noData')){
              continue;
          }
          if(this.hasClass(this.getId('gateway'),"malfunction")){
            bgColor=this.defaults.malfunctionBgColor;
          }else if(this.hasClass(this.monitor[i],'abnormal')){
            bgColor=this.defaults.abnormalBgColor;
          }else if(this.hasClass(this.monitor[i],'malfunction')){
            bgColor=this.defaults.malfunctionBgColor;
          }else{
            bgColor=this.defaults.normalBgColor;
          }  
          var options={
              lineToX:43,
              lineToY:mon_height,
              strokeStyle:bgColor
            };
          this.createdashed(myCanvas,options); 
        }
    },
    createdashed:function(myCanvas,options){
        var context = myCanvas.getContext("2d");  
        context.strokeStyle =options.strokeStyle;
        context.lineWidth = 1; 
        context.dashedLineTo(0, 190, options.lineToX, options.lineToY, 3);
        context.stroke();
    }
};
window.onload=function(){
  new CreateLine({
        monitorBox:'#interface-data .monitor',
        malfunctionBgColor:'#f07171',
        abnormalBgColor:'#ffbc07',
        normalBgColor:'#67bb0b'
    });
}
