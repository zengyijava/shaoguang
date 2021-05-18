/**
 * 自<a href="http://mrthink.net/jquery-select-isimulateselect/" >copy过来稍加修改
 */
(function($){
    /*
     * 统一select样式并实现样式高度自定义的jQuery插件@Mr.Think(http://mrthink.net/)
     */
    $.fn.empSelect=function(iSet){
        iSet=$.extend({
            selectBoxCls:'emp_selectbox', //string类型,外围class名
            curSCls:'emp_currentselected',//string类型,默认显示class名
            optionCls:'emp_selectoption',//string类型,下拉列表class名
            selectedCls:'emp_selected',//string类型,当前选中class名
            optionHover:'emp_selectOption_hover',//string类型,当前选中class名
            width:0,//number类型，模拟select的宽度
            height:300,//number类型，模拟select的最大高度
            zindex:20,//层级顺序
            onchangefun: true,
            selectHeight: 21,//模拟select的显示高度
            align:"left",
            xx: function(){}
        },iSet||{});
        this.hide();
        return this.each(function(){
           var self=this;
           var thisCurVal,thisSelect,cIndex=0;
           //计算模拟select宽度
           var width=100;
           if(iSet.width==0){
              //iSet.width=$(self).width();
        	   width=$(self).width();
           }
           else
           {
        	   width=iSet.width;
           }
           var widthCont=width-22;
           var style="height: 20px;width: 20px;float: right;display: block;margin:0;padding:0;"
           var  html=""
           html=html+'<div class="'+iSet.selectBoxCls+'" style="z-index:'+iSet.zindex+';width:'+width+'px">'
           +'<div class="'+iSet.curSCls+'" style="width:'+width+'px"><div class="'+iSet.curSCls+"Div"+'" style="width:'+widthCont+'px;">'+$(self).find('option:selected').text()+'</div>'
           +'<span class="emp_currentselected_arrows" style="'+style+'"></span></div>'
           +'<div class="'+iSet.optionCls+'" style="display:none;width:'+width+'px;">'
           +'<table style="width:'+width+'px"> ';
           //判断select中是否有optgroup
           //用dt替代optgroup，用dd替代option
           if($(self).find('optgroup').size()>0){
               $(this).find('optgroup').each(function(){
                   html+='<dt>'+$(this).attr('label')+'</dt>';
                   $(this).find('option').each(function(){
                        if($(this).is(':selected')){
                          html+='<dd class="'+iSet.selectedCls+'">'+$(this).text()+'</dd>';
                       }else{
                          html+='<dd>'+$(this).text()+'</dd>';
                       }
                   });
               });
           }else{
               $(this).find('option').each(function(){
                   if($(this).is(':selected')){
                      html+='<tr><td class="'+iSet.selectedCls+'">'+$(this).text()+'</td></tr>';
                   }else{
                      html+='<tr><td>'+$(this).text()+'</td></tr>';
                   }
               });
           }
           html+='</table></div>';
        	   html=html+"";
           //将模拟dl插入到select后面
           $(self).after(html);
           //当前模拟select外围box元素
           thisBox=$(self).next('.'+iSet.selectBoxCls);
           //当前模拟select初始值元素
           thisCurVal=thisBox.find('.'+iSet.curSCls);
           //当前模拟select列表
           thisSelect=thisBox.find('.'+iSet.optionCls);
           /*
           *若同页面还有其他原生select,请前往https://github.com/brandonaaron/bgiframe下载bgiframe，同时在此处调用thisSelect.bgiframe()
            */
           //thisSelect.bgiframe();
 
           //弹出模拟下拉列表
           thisCurVal.click(function(){
        	   iSet.xx.apply();
//              $('.'+iSet.optionCls).hide();
        	   //点击触发onclick事件
        	   $(self).triggerHandler("click");
              $('.'+iSet.selectBoxCls).css('zIndex',iSet.zindex);
              $(self).next('.'+iSet.selectBoxCls).css('zIndex',iSet.zindex+1);
              $('.'+iSet.curSCls).find("span").attr("class","emp_currentselected_arrows");
              if(thisSelect.css("display")=="none")
              {
            	  $('.'+iSet.optionCls).hide();
            	  thisCurVal.find("span").attr("class","emp_currentselected_hover");
            	  thisSelect.show();
              }
              else
              {
            	  $('.'+iSet.optionCls).hide();
            	  thisSelect.hide();
              }
           });
           //若模拟select高度超出限定高度，则自动overflow-y:auto
           if(thisSelect.height()>iSet.height){
               thisSelect.height(iSet.height);
           }
           //模拟列表点击事件-赋值-改变y坐标位置-...
           thisSelect.find('td').click(function(){
        	   thisSelect.find('td').removeClass(iSet.selectedCls);
               $(this).addClass(iSet.selectedCls).siblings().removeClass(iSet.selectedCls);
               cIndex=thisSelect.find('td').index(this);
          	   $(self).find('option').removeAttr('selected');
               $(self).find('option').eq(cIndex).attr('selected','selected');
               if($(this).text()!=thisCurVal.find("div").text())
               {
            	   $(self).triggerHandler("change");
            	   /*if(iSet.onchangefun)
            	   {
            		   alert( $(self).next(".emp_selectbox").html());
            		   $(self).next(".emp_selectbox").remove();
            		   alert( $(self).next(".emp_selectbox").html());
            		   $(self).empSelect();
            	   }*/
               }
               thisCurVal.find("div").text($(this).text());
               thisCurVal.find("span").attr("class","emp_currentselected_arrows");
               $('.'+iSet.selectBoxCls).css('zIndex',iSet.zindex);
               thisSelect.hide();
           });
           thisSelect.find('td').hover(
        	   function()
        	   {
        		   var classNames=$(this).attr("class")
        		   if(iSet.selectedCls!=$.trim(classNames))
        		   {
        			   $(this).addClass(iSet.optionHover);
        		   }
        	   },
        	   function()
        	   {
        		   $(this).removeClass(iSet.optionHover);
        	   }
           );
           //非模拟列表处点击隐藏模拟列表
           //$(document)点击事件不兼容部分移动设备
           $('html,body').click(function(e){
                if(e.target.className.indexOf(iSet.curSCls)==-1){
                	 thisCurVal.find("span").attr("class","emp_currentselected_arrows");
                  thisSelect.hide();
                  $('.'+iSet.selectBoxCls).css('zIndex',iSet.zindex);
              }
           });
           //取消模块列表处取消默认事件
           thisSelect.click(function(e){
              e.stopPropagation();
           });
        });
    }
})(jQuery);


//重新设置select,objiect:select的jquery对象 ，width：select的宽度可以设，不想设值，就是默认select本身定义的宽度
function changeEmpSelect($obj,width)
{
	$obj.next(".emp_selectbox").remove();
	$obj.empSelect({width:width});
}