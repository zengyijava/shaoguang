/*
 *	JON
 *V 1.2
 *1.增加了formatNumber函数
 *2.在数据加载错误时填表充NO DATA
 *V 1.3
 *1.支持包含合计记录的排序
 *2.支持扩展中排序
* V 1.4
* 1.验证数据的合法性 （数据为空对象）
* 2.行支持双击自定义事件 
 */
Jon={
    version:1.3
}
/*
 *	
 */
Jon.apply = function(o, c, defaults){ 
    if(defaults){
        Jon.apply(o, defaults);
    }
    if(o && c && typeof c == 'object'){
        for(var p in c){
            o[p] = c[p];
        }
    }
    return o;
};
Jon.AUTOID=10000;
(function () {
          var  toString = Object.prototype.toString,
                    isIterable = function(v){ 
                        if(Jon.isArray(v) || v.callee){
                            return true;
                        } 
                        if(/NodeList|HTMLCollection/.test(toString.call(v))){
                            return true;
                        }
                        //NodeList has an item and length property
                        //IXMLDOMNodeList has nextNode method, needs to be checked first.
                        return ((v.nextNode || v.item) && Jon.isNumber(v.length));
                    },
                   ua = navigator.userAgent.toLowerCase(),
                    check = function(r){
                        return r.test(ua);
                    },
                    DOC = document,
                    isOpera = check(/opera/),
                    isIE = !isOpera && check(/msie/),
                    isIE7 = isIE && check(/msie 7/),
                    isIE8 = isIE && check(/msie 8/),
                    isIE6 = isIE && !isIE7 && !isIE8;
          Jon.apply(Jon,{
              extend : function(){ 
                    var io = function(o){
                        for(var m in o){
                            this[m] = o[m];
                        }
                    };
                    var oc = Object.prototype.constructor;

                    return function(sb, sp, overrides){
                        if(Jon.isObject(sp)){
                            overrides = sp;
                            sp = sb;
                            sb = overrides.constructor != oc ? overrides.constructor : function(){sp.apply(this, arguments);};
                        }
                        var F = function(){},
                            sbp,
                            spp = sp.prototype;

                        F.prototype = spp;
                        sbp = sb.prototype = new F();
                        sbp.constructor=sb;
                        sb.superclass=spp;
                        if(spp.constructor == oc){
                            spp.constructor=sp;
                        }
                        sb.override = function(o){
                            Jon.override(sb, o);
                        };
                        sbp.superclass = sbp.supr = (function(){
                            return spp;
                        });
                        sbp.override = io;
                        Jon.override(sb, overrides);
                        sb.extend = function(o){Jon.extend(sb, o);};
                        return sb;
                    };
                }(),
                 override : function(origclass, overrides){
                    if(overrides){
                        var p = origclass.prototype;
                        Jon.apply(p, overrides);
                        if(Jon.isIE && overrides.toString != origclass.toString){
                            p.toString = overrides.toString;
                        }
                    }
                },
               isString: function(v){
                    return typeof v === 'string';
                },
                 isObject : function(v){
                    return v && typeof v == "object";
                },
                isPrimitive : function(v){
                    return Jon.isString(v) || Jon.isNumber(v) || Jon.isBoolean(v);
                },
                isBoolean: function(v){
                    return typeof v === 'boolean';
                },
                isEmpty : function(v, allowBlank){
                    return v === null || v === undefined || ((Jon.isArray(v) && !v.length)) || (!allowBlank ? v === '' : false);
                },
                isEmptyObject:function(v){
                    if(!Jon.isObject(v))
                      return true;
                   for(var n in v)
                      return false;
                 return true;  
              }, 
                isArray : function(v){
                    return toString.apply(v) === '[object Array]';
                },
                isDefined: function(v){
                    return typeof v !== 'undefined';
                }, 
                isFunction : function(v){
                              return toString.apply(v) === '[object Function]';
                },
                isNumber: function(v){
                    return typeof v === 'number' && isFinite(v);
                },
                each: function(array, fn, scope){
                    if(Jon.isEmpty(array, true)){
                        return;
                    }
                    if(!isIterable(array) || Jon.isPrimitive(array)){
                        array = [array];
                    }
                    for(var i = 0, len = array.length; i < len; i++){
                        if(fn.call(scope || array[i], array[i], i, array) === false){
                            return i;
                        };
                    }
                },
                toArray : function(){
                    return isIE ?
                        function(a, i, j, res){
                            res = [];
                            Jon.each(a, function(v) {
                                res.push(v);
                            });
                            return res.slice(i || 0, j || res.length);
                        } :
                        function(a, i, j){
                            return Array.prototype.slice.call(a, i || 0, j || a.length);
                        }
                }(),
                applyIf : function(o, c){
                        if(o){
                            for(var p in c){
                                if(Jon.isEmpty(o[p])){
                                    o[p] = c[p];
                                }
                            }
                        }
                        return o;
                 },
                urlEncode: function(o, pre){
                    var undef, buf = [], key, e = encodeURIComponent;

                    for(key in o){
                        undef = !Jon.isDefined(o[key]);
                        Jon.each(undef ? key : o[key], function(val, i){
                            buf.push("&", e(key), "=", (val != key || !undef) ? e(val) : "");
                        });
                    }
                    if(!pre){
                        buf.shift();
                        pre = "";
                    }
                    return pre + buf.join('');
                },
                formatNumber : function(fnumber, fdivide, fpoint, fround) {
					var reg = /^[0-9]|[.]+$/
						if (!reg.test(fnumber)) {
							return fnumber;
						}
						var fnum = fnumber + '';
						var revalue = "";
						if (fnum == null) {
							for (var i = 0; i < fpoint; i++)
								revalue += "0";
							return "0." + revalue;
						}
						fnum = fnum.replace(/^\s*|\s*$/g, '');
						if (fnum == "") {
							for (var i = 0; i < fpoint; i++)
								revalue += "0";
							return "0." + revalue;
						}

						fnum = fnum.replace(/,/g, "");

						if (fround) {
							var temp = "0.";
							for (var i = 0; i < fpoint; i++)
								temp += "0";
							temp += "5";
							fnum = Number(fnum) + Number(temp);
							fnum += '';
						}
						var arrayf = fnum.split(".");
						if (fdivide) {
							if (arrayf[0].length > 3) {
								while (arrayf[0].length > 3) {
									revalue = ","
											+ arrayf[0].substring(arrayf[0].length
															- 3, arrayf[0].length)
											+ revalue;
									arrayf[0] = arrayf[0].substring(0,
											arrayf[0].length - 3);
								}
							}
						}
						revalue = arrayf[0] + revalue;
						if (arrayf.length == 2 && fpoint != 0) {
							arrayf[1] = arrayf[1].substring(0,
									(arrayf[1].length <= fpoint)
											? arrayf[1].length
											: fpoint);

							if (arrayf[1].length < fpoint)
								for (var i = 0; i < fpoint - arrayf[1].length; i++)
									arrayf[1] += "0";
							revalue += "." + arrayf[1];
						} else if (arrayf.length == 1 && fpoint != 0) {
							revalue += ".";
							for (var i = 0; i < fpoint; i++)
								revalue += "0";
						}
						return revalue;
					},
                 isIE : isIE,
                 isIE6 : isIE6,
                 isIE7 : isIE7,
                 isIE8 : isIE8,
                 doc  :DOC
          });
           
})();

Jon.applyIf(String, { 
    format : function(format){
        var args = Jon.toArray(arguments, 1);
        return format.replace(/\{(\d+)\}/g, function(m, i){
            return args[i];
        });
    }
});

Jon.JSO=function(container){
  this.container=container||{};
}
Jon.apply(Jon.JSO.prototype,{  
       prefix:'#',
       DIV:'div',
       SPAN:'span',
       UL:'ul',
       LI:'li',
       P:'p',
       IMG:'img',
      get:function(){  
           return this.container;
       },
       set:function(container){
             if ( Jon.isObject(container)) {
                   this.container=container
             }
       },
       init:function () {
             this.container=this.createObject(this.DIV);
       },
       createObject:function(o){  
           o=o||{};
           if (Jon.isString(o)) {
               o={xtype:o.xtype||o};
           }                
            if (!Jon.isObject(o)) {
               return null;
            }
            var obj =Jon.doc.createElement(o.xtype); 
            for(var n in o){
                if (n=='fieldLabel')  continue;
                if (n=='xtype')  continue;
                if (n=='store')  continue;
                if (n=='text') {
                        obj.innerHTML=o[n];  
                        continue;   
                 } 
                 if (n=='cls') {
                    obj.setAttribute('class',o[n]);
                    continue;
                 }
                 if (n=='style') {
                   //obj.style= o[n];
                   continue;
                 }
                 obj.setAttribute(n,o[n]);
             }
            return obj;
       },
       appendChild:function(o){
           if (Jon.isObject(o)) {              
                 this.container.appendChild(o);
           }
           return this.container;
       },
       toHtml:function () { 
            return this.container.innerHTML;
       }   
});
 
 Jon.JSO.co=Jon.JSO.createObject;
 Jon.JSO.ac=Jon.JSO.appendChild;

Jon.Table=function (config) { 
       config=config||{};
       if (Jon.isString(config)) {
           config={id:config.id||config};
       }
      Jon.apply(this,config);
     this.initHead();
     this.initPaging(); 
} 
 Jon.extend(Jon.Table,Jon.JSO,{
       renderTo:'', 
       isAccident:false,
       error:'',
       single:'',
       pageSize:15,
       total:0,
       index:1,
       pageTotal:1,
       fields:[],
       items:[],
       paramObject:{},
       url:'',
       imgPath:'images/',
       firstImg:'first.gif',
       preImg:'btn_pre.gif',
       nextImg:'btn_next.gif',
       lastImg:'btn_last.gif',
       firstImgDis:'first_s.gif',
       preImgDis:'btn_pre_s.gif',
       nextImgDis:'btn_next_s.gif',
       lastImgDis:'btn_last_s.gif',
       firstId:'btnfirst',
       preId:'btnpre',
       nextId:'btnnext',
       lastId:'btnlast',
       goId:'gototxt',
       gotoId:'gotopg',
       totalId:'recordcount',
       indexId:'curpageindex',
       pageTotalId:'pgcount',
       statusCls:'cursor_s',
       gotoCls:'goBtn_s', 
       leftCluCls:'t_tdb',
       otherCluCls:'t_tde',
       isExpand:false,
       expandPrefix:'',
       expandWidth:'100%',
       exCls:'excls',
       rendered:false, 
       isInitHead:false,
       renderHead:'',
       rdbClick:null,
       getTotal:function () {
           return this.total;
       },
       getIndex:function () {
          return this.index;
       },
       getUrl:function () {
          return this.url;
       },
       getDataStore:function(){
          return this.items;
       },
       getPageTotal:function () {
         return this.pageTotal;
       },
       getParam:function () {
         return this.paramObject;
       },
       setParam:function (o) {
          if (Jon.isObject(o)) {
                this.paramObject=o; 
          }
       },
       queryData:function (index,total,callback) {  
              this.paramObject.pageSize=this.pageSize;
              this.paramObject.index=index || 1;
              this.paramObject.total=total || 0;
              this.get(this.renderTo).html("<center><img src='images/loading.gif' />Loading...</center>");
              var self =this;
              loadData(this.paramObject,this.url,function(data,record,success,error,single){  
		            if(success){		
			            self.total=record; 
			            self.items=data;   
                        self.load(self.items,self.paramObject.index,self.total); 
                        self.show();  
                        if(callback)
    		                  callback(data);
		            }else{
		            	self.init(); 
		            	self.total=0;
		            	self.rendered=true;  
		            	self.show();  
			            alert(error);
		            }
	            });
       },
       initPaging:function () {  
               var self=this;
               this.get(this.firstId).click(function () {self.firstPage();});
               this.get(this.nextId).click(function () {self.nextPage();}); 
               this.get(this.preId).click(function () {self.prePage();});
               this.get(this.lastId).click(function () {self.lastPage();});
               this.get(this.gotoId).click(function () { 
                    var n = self.get(self.goId).val(); 
                    self.gotoPage(parseInt(n,10));
              });  
       },
       headCls:'expression dg_header ',
       headFirstCls:'header_1',
       headOtherCls:'header_2',
       bodyTdCls:'Row_line',
       initHead:function () {
             if (this.isInitHead) {
                      this.init(); 
                       if (this.rendered || this.fields.length<=0  ) {           
                                return; 
                         }
                       var  table =this.createTable(),
                               co = this.createObject, 
                                field = this.fields;
                        this.appendChild(table);  
                        var tr =co({xtype:'tr',cls:this.headCls})
                      
                          //tr.style.top= expression(document.getElementById(this.renderHead).scrollTop) ;//String.format("expression(document.getElementById('{0}').scrollTop)",this.renderHead);
                        table.appendChild(tr) ;
                        for(var i=0,size=field.length;i<size;i++){
                                  var title='';
                                  if (Jon.isDefined(field[i].title)) {
                                         title =field[i].title; 
                                  }  
                                   var w = Jon.isDefined(field[i].width) ? field[i].width : '*'; 
                                   var cls =((i==0) ? this.headFirstCls : this.headOtherCls);
                                   if(Jon.isDefined(field[i].cls))
                                        cls +=' '+field[i].cls;
                                   var sort='';
                                   var sortId=this.getId();
                                   var isSort=false;
                                   if(Jon.isDefined(field[i].sort)){
                                	   isSort=true;
                                	   sort=String.format("{0}('{1}','{2}','{3}')",field[i].sort,sortId,field[i].name,field[i].sortType);
                                   }
                                  var td =co({xtype:'td',
                                	  					cls:cls,
                                	  					text:field[i].text,
                                	  					title:title,
                                	  					width:w,
                                	  					onclick:sort
                                	  					});
                                    if(isSort){
                                    	 var img=co({xtype:'img',
                                    		 					id:sortId,
                                    		 					alt:'Sort',
                                    		 					src:this.imgPath+'sort_desc.gif'});
                                    	 td.appendChild(img);
                                    }
                                    tr.appendChild(td);
                        }
                       var trBody =co({xtype:'tr'});
                        table.appendChild(trBody) ;
                       var tdBody = co({xtype:'td',cls:this.bodyTdCls,valign:'top',width:this.expandWidth,colspan:field.length,height:'100%'}) 
                       trBody.appendChild(tdBody);
                       var bodyDiv = co({xtype:this.DIV,id:this.renderTo,align:'left'});
                              bodyDiv.style.width='100%';
                              bodyDiv.style.height='100%';
                        tdBody.appendChild(bodyDiv);
                       
                        this.get(this.renderHead).html(this.toHeadHtml()); 
             }             
       },
       setImgSrc:function (id,img) {
          this.get(id).attr('src',this.imgPath+img);
       },
       setClass:function (id,cls,bool) { 
            if (bool) { 
                 this.get(id).addClass(cls);
                 
            }else{ 
                 this.get(id).removeClass(cls);
            }  
       }, 
       resetStatus:function () { 
                    var   i = this.index, 
                             t = this.pageTotal;  
                     this.setImgSrc(this.firstId, (i <= 1 || t<=1 ) ? this.firstImgDis  : this.firstImg );
                     this.setImgSrc(this.preId,  (i <= 1 || t<=1 ) ? this.preImgDis   : this.preImg ); 
                     this.setImgSrc(this.nextId,(t <= 1 ||  i==t )  ? this.nextImgDis : this.nextImg);
                     this.setImgSrc(this.lastId,  (t <= 1 ||  i==t)   ? this.lastImgDis  : this.lastImg);
                     this.setClass(this.firstId, this.statusCls,(i <= 1 || t<=1 ));   
                     this.setClass(this.preId,  this.statusCls,(i <= 1 || t<=1 ));  
                     this.setClass(this.nextId,this.statusCls,(t <= 1 ||  i==t ));     
                     this.setClass(this.lastId,  this.statusCls,(t <= 1 ||  i==t ));  
                     this.setClass(this.gotoId,this.gotoCls, (t<=1));  
                     //
                     this.get(this.totalId).text(this.total.toString());
                     this.get(this.indexId).text(this.index.toString());
                     this.get(this.pageTotalId).text(this.pageTotal.toString());
       },
       firstPage:function () { 
          if (this.index>1) {
              this.queryData(1,0);
          }
       },
       nextPage:function ( ) {
            if (this.index<this.pageTotal) {
                 this.queryData(++this.index,this.total);
            }
       },  
       prePage:function ( ) {
             if (this.index>1) {
                      this.queryData(--this.index,this.total);
             }
       },
       lastPage:function ( ) {
           if (this.index<this.pageTotal) {
                this.queryData(this.pageTotal,this.total);
           }
       },
       gotoPage:function (n) {
          if (Jon.isNumber(n) && n<=this.pageTotal) { 
              this.queryData(n,this.total) ;
          }
       },
       refresh:function (callback) {
           this.queryData (this.index,this.total);
       },
       getId:function () {
          return 'jon-table-'+(++Jon.AUTOID);
       },
       get:function(id){       
          return $(this.prefix+id);
       },
       load:function (ds,index,total) {  
          if (Jon.isObject(ds)) {
              this.items=ds;
              this.index = index;
              this.total =total;
              this.pageTotal = (this.total%this.pageSize==0) ? parseInt(this.total/this.pageSize,10) : parseInt(this.total/this.pageSize,10)+1;
              this.rendered=false;
              this.render();
          }
       },
       getColCls:function (i) { 
          return (i==0) ? this.leftCluCls :this.otherCluCls;
       },
       getRowCls:function (i) {
                    return (i%2==0)? 'dg_item2': 'dg_item';
       },
       createTable:function () {
                    var table=this.createObject('table');
                    table.style.width='100%', 
                    table.setAttribute('cellspacing','0');
                    table.setAttribute('cellpadding','0');
                    table.setAttribute('border','0');
                    return table; 
       },
       createTr:function (i,css,dbclickName) {
                   var id='jon-tr-'+i;
                   var dbName='',t='';
                   if(!Jon.isEmpty(dbclickName)){
                	   t='双击看看',
                	   dbName=String.format("{0}(event,{1},this)",dbclickName,i);
                   }	   
                   var tr = this.createObject({xtype:'tr',
                                                                  id:id,
                                                                  cls:css,
                                                                  onmousemove:"$(this).addClass('dtbg')",
                                                                  onmouseleave:"$(this).removeClass('dtbg')",
                                                                  onclick:"$('.clickbg').removeClass('clickbg');$(this).addClass('clickbg')",
                                                                  title:t,
                                                                  ondblclick:dbName});
                   return tr;
       },
       createTd:function (css,width,align,text) { 
              if (Jon.isEmpty(text)) {
                  text=''; 
              }
              var td = this.createObject({xtype:'td',cls:css,text:text,width:width}); 
              td.style.textAlign=align;
             return td 
       },
       render:function () { 
            this.init(); 
            if (this.rendered || !Jon.isObject(this.items) || this.items.length<=0 || this.fields.length<=0 || this.total<=0) {           
               this.rendered=true;  
               return; 
            }
           var table =this.createTable(),
                  item =this.items,
                  field = this.fields;
           this.appendChild(table);
           for(var i=0,size=item.length;i<size;i++){ 
               //验证数据的合法性 
               if(Jon.isEmptyObject(item[i])){
                   this.total=0; 
                   break; 
                }
                 var tr = this.createTr(i,this.getRowCls(i),this.rdbClick);
                 table.appendChild(tr); 
                 for(var c=0,cSize=field.length;c<cSize;c++){
                       //列名 
                        var text=item[i][field[c].name];
                        //宽 
                         var w=Jon.isDefined(field[c].width) ? field[c].width : '*';
                         //居左,右,中
                         var a =Jon.isDefined(field[c].align) ? field[c].align : 'center';
                          if (Jon.isDefined(field[c].renderer) && Jon.isFunction(field[c].renderer)) {  
                                 text= field[c].renderer(text,i,item);
                          } 
                         var cls=this.getColCls(c);
                           if (Jon.isDefined(field[c].rendercls) && Jon.isFunction(field[c].rendercls)) {  
                                 cls= field[c].rendercls(text,i,item);
                          } 
                        var td=this.createTd(cls, w, a,text); 
                        tr.appendChild(td);
                 } 
                //判断是否要扩展 
                if (this.isExpand) {
                      this.renderEx(table,i,field.length);
                } 
           } 
           this.rendered=true; 
       },  
       renderEx:function (table,index,colssize) {
                     var tr = this.createTr(index+10000,'dg_item_3');
                     table.appendChild(tr);  
                     tr.appendChild(this.createObject({xtype:'td',cls:this.exCls,colspan:colssize,width:this.expandWidth,id:this.expandPrefix+index,align:'left'}));
      },
       sort:function (id,field,type) {
             var way           = this.getOrder(id),
                    sortArray = new Array(),
                    item         = this.items.concat(),
                    size          =item.length,
                    acc           =null,
                    value;
              if (this.total<=1 || !Jon.isObject(item)) {
                  return ;
              }  
               if(this.isAccident){
            	    acc =this.items[size-1];
                    item.splice(size-1,1);
               }
                 size=item.length;
          
              if (type !='D') {
                 for(var i=0;i<size;i++){
                       value =item[i][field]; 
                       sortArray[i]=(Jon.isEmpty(value,true)) ? 'TMD' :((value==0) ? -1 : value);
                 }
              }else{
                   for(var j=0;j<size;j++){
                       sortArray[j] =this.getDateTime(item[j][field]);
                   }
              }
              if (sortArray.length>0) {
                    var indexArray = this.getSortArray(sortArray,type);
                     if (indexArray!=null) {
                           this.reSortItems(item,indexArray,way,acc);
                           this.rendered=false;
                           this.render(); 
                            this.show();                           
                     } 
              }              
       }, 
       reSortItems:function (items,indexArray,way,accident) {     
               var item = new Array();
               for(var i=0,size=items.length;i<size;i++){
                        var k=(way==0) ? indexArray[i] : indexArray[size-i-1];
                        item.push(items[k]); 
               }
               if(accident)
            	   item.push(accident);
               this.items= item; 
       },
       getSortArray:function (sortArray, type) {
              var  len =sortArray.length,
                      s     ='',
                      sortData=sortArray;
              for(var k=0;k<len;k++){
                      s +=Jon.isEmpty(s) ? sortArray[k]  : ('^%^' + sortArray[k]);                    
              }
              if (type!='N') {
                   sortData.sort();
              }else{
                    for(var i=0;i<len;i++){
                         var temp=0;
                         for(var j=i+1;j<len;j++){
                            if (parseInt(sortData[i],10) >parseInt(sortData[j],10)) {
                                  temp=sortData[i];
                                  sortData[i] = sortData[j];
                                  sortData[j] = temp; 
                            }
                         } 
                    }
              }
              if (s == 0 || s == -1) {
                 return null;
              }
              var tempSort = s.split('^%^'),
                     result = new Array();
              for(var i = 0; i<len; i++){
                    for(var j=0;j<len;j++){
                         if (sortData[i]==tempSort[j]) {
                              result[i] = j;
                              tempSort[j]='||||||||';
                              break; 
                         } 
                    }
              }
              return result;
       },
       getFormatTime:function (time) {
                if (Jon.isEmpty(time)) {
                      return '';
                }
                var y  = time.getFullYear(),
                       m = time.getMonth()+1,
			           d = d.getDate(),
			           h = d.getHours(),
			          mi = d.getMinutes(),
			          s	   = d.getSeconds(),
			          prefix='0'; 
			 return    String.format('{0}-{1}-{2} {3}:{4}:{5}',
			                                         y,
			                                         ((m<10) ? prefix + m : m),
			                                         ((d<10)  ? prefix + d  : d),
			                                         ((h<10)  ? prefix + h  : h),
			                                         ((mi<10)? prefix +mi :mi),
			                                         ((s<10)  ? prefix  +s  :s));   
       },
       getOrder:function (id) {
           var allName = this.get(id).attr('src'),
                               i  = allName.indexOf('.gif'),
                      name  = allName.substr(i-9,9),
                      result  =0;
           if (name=='sort_desc') {
                allName ='images/sort_ascc.gif';
           }else{
                allName ='images/sort_desc.gif';  
                result     =1           
           }
           this.get(id).attr('src',allName);
           return result;
       },  
       toHtml:function () {
           if (this.total==0) {
                this.appendChild(this.createObject({xtype:this.P,align:'center',cls:'t_nodata',text:'No Data'}))
             } 
            return this.container.innerHTML;
       } ,  
       toHeadHtml:function () { 
            return this.container.innerHTML;
       } ,
       show:function () {    
               if (this.rendered) {   
                     //alert(this.toHtml()) 
                     this.get(this.renderTo).html(this.toHtml()); 
                     this.resetStatus();   
               }         
       }
 }); 



removeClass = function(o, c) {
	o.className = c;
}
addClass = function(o, c) {
	o.className = c + " td_header";
}
getOptPower = function(code) {
	return true;
}
getPageSize = function() {
	return 20;
}
getPower = function(code) {
	return;
}
getCilentWidth = function() {
	return parseInt(document.body.clientWidth);
}
getCilentHeight = function() {
	return parseInt(document.body.clientHeight);
}
