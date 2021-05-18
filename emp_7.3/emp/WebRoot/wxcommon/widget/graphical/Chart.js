Jon.Chart=function(config){ 
      config =config ||{};
     if (Jon.isString(config)) {
        config={id:config.id||config} ;
     } 
    Jon.apply(this,config);
    this.init();
}
 Jon.apply(Jon.Chart.prototype,{
      //标题 
       caption:'',
       //背景颜色
       hovercapbg:'FFECAA',
       //边界颜色
       hovercapborder:'F47E00',
       formatNumberScale:'0',
       //精度
       decimalPrecision:'2',
       showvalues:'1',
       numdivlines:'4',
       numVdivlines:'0',
       yaxisminvalue:'10',
       yaxismaxvalue:'10',
       rotateNames:'0',
       //副标题
       subcaption:'',
       //X坐标文本
       xAxisName:'',
       //Y坐标文本
       yAxisName:'',
       //字体大小 
       baseFontSize:'11',
       //
       outCnvBaseFontSize:'13',
       //背景颜色
       bgColor:'ffffff',
       canvasBorderColor:'ccddff',
       divLineColor:'e4e4e4', 
       vDivLineThickness:'1',
       numberSuffix:'',
       //字体颜色
       baseFontColor:'444444',
       outCnvBaseFontColor:'777777',
      // hoverCapBorderColor:'aaaaaa',
      // hoverCapBgColor:'ffffff',
       graph:[], 
       isTransparent:true,
       base:{
                id:"MyCart",
                url:"../FusionCharts/Charts3.1/MSLine.swf",
                width:0,
                height:0,
                renderTo:''
       },
       chartObject:{},
       getChartObject:function () {
          return this.chartObject;
       },
       getBase:function () {
           return this.base;
       },
       setBase:function (b) {
            this.base = b;
       },
       init:function () {
             this.graph   = new Array();
             var me =this,
                    val,
                    head='<graph ';  
             for(var n in me){
                 val = me[n]; 
                 //只需要字符类型 
                 if (Jon.isString(val)) { 
                      head += (n +"='"+val+"' ");
                 } 
             }
            head +=" >" ; 
           this.graph[this.graph.length]=head;  
       },
       loadCategories:function (cates) {
               var s ="<categories >";
                for(var i=0,size=cates.length;i<size;i++){
                      s += String.format("<category name='{0}' />",cates[i]);
                } 
                s +="</categories>"; 
                this.graph[this.graph.length]=s;       
       },
       /*
        *	config:配置参数{seriesName:'name',color:''}
       *    items:数据项 
        */
       add:function (config,items) {
               var s ="<dataset ",
                      end ='</graph>',
                      val;
               if (Jon.isObject(config)) {
                    for(var n in config){
                       val = config[n];
                       //支持回调
                       if (Jon.isFunction(val)) {
                            val =  val();
                         } 
                         s += (n +"='"+val+"' ");
                    }
               } 
               s += " >" ;
               for(var i=0,size=items.length;i<size;i++){
                     s += String.format("<set value='{0}' />",items[i]);      
              } 
              s  +=  "</dataset>";  
               //去掉尾标签
               if (this.graph[this.graph.length-1] ==end) { 
                         this.graph[this.graph.length-1]='';                       
                }  
              this.graph[this.graph.length]=s; 
              //加尾标签
              this.graph[this.graph.length]=end; 
       },
       show:function () { 
           var xml =this.graph.join(''),
                  base=this.base;    
              this.chartObject = new FusionCharts(base.url,base.id,base.width,base.height);
              this.chartObject.setTransparent(this.isTransparent);
              this.chartObject.setDataXML(xml);
              this.chartObject.render(base.renderTo);
       } 
 
 });
 
