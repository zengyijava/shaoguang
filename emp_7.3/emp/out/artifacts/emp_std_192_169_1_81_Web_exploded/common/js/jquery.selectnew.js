;(function () {
    $.fn.isSearchSelectNew = function (opt, callback, init) {
        opt = $.extend({
            selectBox: "c_selectBox",//最外层模拟select的div
            input: 'c_input',//显示数据用的input
            selectItem: 'c_result',
            selectImg: 'c_selectimg',
            selectedItem: 'selected',
            selectedItem_bg: 'div_bg',
            select_height: 29,
            select_bd: " div_bd",
            isInput: true,//是否允许手动输入
            width: 200,//模拟select的宽度
            width_ul: 0,//ul的宽度
            inputWidth: 178,
            height: 300,//select的最大高度
            zindex: 20,//层级顺序
            isHideBorder: false,
            isHide: false,//是否隐藏控件
            isFixed: false //是否固定高度
        }, opt || {});

        $(this).hide();//select默认隐藏
        return this.each(function () {
            var self = this;

            var thisBox, thisInputVal, thisselectItem, thisselectItem_li, thisselect, data = {}, display = "",
                selectItem_height, isHideBorder = '', isHide = '';
            opt.select_height = opt.select_height == 0 ? $(self).height() : opt.select_height;
            var opt_width = (opt.width == 0) ? $(self).width() : opt.width;
            opt.width_ul = (opt.width_ul == 0) ? opt_width : opt.width_ul;
            opt.inputWidth = opt_width - 18;
            opt.input_height = opt.select_height - 4;
            if (opt.isFixed) {
                selectItem_height = 150; //c_result的高度
            } else {
                selectItem_height = $(self).find('option').size() * 24;//c_result的高度
            }
            if (opt.isHideBorder) {
                isHideBorder = '';
            } else {
                isHideBorder = 'div_bd';
            }
            if (opt.isHide) {
                isHide = 'display:none';
            } else {
                isHide = '';
            }
            var selectVal = $.trim($(self).find('option:selected').text());
            var html = "<div class='" + opt.selectBox + ' ' + isHideBorder + "' style=width:" + opt_width + "px;height:" + opt.select_height + "px;" + isHide + ">";
            if (opt.isInput == false) {
                html += "<input name='input' unselectable='on' autocomplete='off' class=" + opt.input + " style='width:" + opt.inputWidth + "px;height:" + opt.input_height + "px;line-height:" + opt.input_height + "px;' value=''/><div class=" + opt.selectImg + "></div><ul class='" + opt.selectItem + " div_bd' style='width:" + opt.width_ul + "px;height:" + selectItem_height + "px;top:" + opt.select_height + "px;'>";
            } else {
                html += "<input name='input' autocomplete='off' class=" + opt.input + " style='width:" + opt.inputWidth + "px;height:" + opt.input_height + "px;line-height:" + opt.input_height + "px;' value=''/><div class=" + opt.selectImg + "></div><ul class='" + opt.selectItem + " div_bd' style='width:" + opt.width_ul + "px;height:" + selectItem_height + "px;top:" + opt.select_height + "px;'>";
            }
            /* var html="<div class='"+opt.selectBox+' '+isHideBorder+"' style=width:"+opt_width+"px;height:"+opt.select_height+"px;"+isHide+">"+
             "<input name='input' autocomplete='off' class="+opt.input+" style='width:"+opt.inputWidth+"px;height:"+opt.input_height+"px;line-height:"+opt.input_height+"px;' value=''/><div class="+opt.selectImg+"></div><ul class='"+opt.selectItem+" div_bd' style='width:"+opt.width_ul+"px;height:"+selectItem_height+"px;top:"+opt.select_height+"px;'>";
       */	  //添加下拉框列表项


            $(self).find('option').each(function () {
                if ($(this).is(':selected')) {
                    html += "<li class='" + opt.selectedItem + display + "'>" + $.trim($(this).text()) + "</li>";

                } else {
                    html += "<li class='" + display + "'>" + $.trim($(this).text()) + "</li>";
                }

            })
            html += "</ul></div>";
            $(self).after(html);

            thisBox = $(self).next('.' + opt.selectBox);
            thisInputVal = thisBox.find('.' + opt.input);
            thisselectItem = thisBox.find('.' + opt.selectItem);
            thisselectItem_li = thisBox.find('.' + opt.selectItem).find('li');
            thisselect = thisBox.find('.' + opt.selectImg);
            thisInputVal.val($.trim($(self).find("option:selected").text()));
            data.box = {
                'selectBox': thisBox,
                'input': thisInputVal,
                'selectItem': thisselectItem,
                'selectItem_li': thisselectItem_li,
                'selectImg': thisselect,
                'self': self
            };
            if (opt.isInput) {
                thisInputVal.bind('click', function (event) {
                    thisselectItem.show();
                });
                thisInputVal.bind('keyup', function (event) {

                    var text = inputKeyup(this, event);
                    data.event = event.type;
                    data.value = text;
                    //存在selected未被初始化的情况 这里重新赋值
                    if (!data.selected) {
                        data.selected = $(self).find('option').eq(0);
                    }
                    //输入框触发事件
                    $.extend(data, {isSelect: false});
                    excuteFunc(callback, data);//外部调用
                });
                // thisInputVal.bind('click keyup', function (event) {//输入框输入查找
                //     var text = inputKeyup(this, event);
                //     data.event = event.type;
                //     data.value = text;
                //     //存在selected未被初始化的情况 这里重新赋值
                //     if (!data.selected) {
                //         data.selected = $(self).find('option').eq(0);
                //     }
                //     //输入框触发事件
                //     $.extend(data, {isSelect: false});
                //     excuteFunc(callback, data);//外部调用
                // })
                thisInputVal.bind('change', function (event) {//输入框输入查找
                    var text = inputKeyup(this, event);
                    data.event = "click";
                    data.value = text;
                    data.text = text;

                    var select_value = '';
                    var cIndex = 0;
                    var channel = $(self).find("option");
                    for (var i = 0; i < channel.length; i++) {
                        var channlVar = channel.eq(i).val();//option中的值
                        if (text == channlVar) {
                            select_value = text;
                            cIndex = i;
                            break;
                        }
                    }
                    $(self).find('option').attr('selected', false);
                    $(self).find('option').eq(cIndex).attr('selected', 'selected');
                    data.selected = $(self).find('option').eq(cIndex)
                    data.obj = $(self);
                    //下拉框触发事件
                    $.extend(data, {isSelect: true});
                    excuteFunc(callback, data);//外部调用
                    return;
                });
                //结束
                thisselect.click(function (event) {
                    selectToggle(event);
                });
            } else {
                thisInputVal.attr('readonly', true).css({'cursor': 'pointer'});
                thisBox.click(function (event) {
                    $(this).css({'z-index': opt.zindex + 1});
                    selectToggle(event);
                })


            }


            thisselectItem.width(opt_width);
            if (thisselectItem.height() > opt.height) {
                thisselectItem.height(opt.height);
            }

            thisselectItem_li.bind('click', function (event) {
                $(this).addClass(opt.selectedItem + ' ' + opt.selectedItem_bg).siblings().removeClass(opt.selectedItem + ' ' + opt.selectedItem_bg);
                thisInputVal.val($(this).text());
                cIndex = thisselectItem.find('li').index(this);
                $(self).find('option').attr('selected', false);
                $(self).find('option').eq(cIndex).attr('selected', 'selected');
                thisselectItem.hide();
                thisBox.css({'z-index': opt.zindex});
                data.event = event.type;
                data.value = $(self).find('option').eq(cIndex).val();
                data.text = $(self).find('option').eq(cIndex).text();
                data.selected = $(self).find('option').eq(cIndex);
                data.obj = $(self);
                //下拉框触发事件
                $.extend(data, {isSelect: true});
                excuteFunc(callback, data);//外部调用
                event.stopPropagation();
            }).hover(function () {
                $(this).addClass(opt.selectedItem + ' ' + opt.selectedItem_bg).siblings().removeClass(opt.selectedItem + ' ' + opt.selectedItem_bg);
            }, function () {
                $(this).removeClass(opt.selectedItem);
            })

            var timer;
            thisBox.mouseover(function () {
                clearTimeout(timer);
            }).mouseout(function () {
                var _this = this;
                timer = setTimeout(function () {
                    $(_this).find('.' + opt.selectItem).hide();
                    thisBox.css({'z-index': opt.zindex});
                }, 300);

            })

            function inputKeyup(obj, event) {
                var _this = obj;
                thisselectItem.hide();
                thisselectItem_li.show();
                var text = $(_this).val();
                var textTemp=text;
                if(text.indexOf("(")>=0){
                	textTemp=textTemp.replace("\(","\\(");
                }
                var reg = new RegExp(textTemp, "i");
                thisselectItem_li.each(function (i) {
                    var ithat = this;
                    var flag = reg.test($(ithat).text());
                    if (flag || text == '') {
                        $(ithat).show();
                    } else {
                        $(ithat).hide();
                    }
                })
                thisselectItem.show();
                // thisBox.css({'z-index': opt.zindex + 1});
                return text;
                event.stopPropagation();
            }


            function selectToggle(event) {
                thisselectItem_li.not('.hide').show();
                thisselectItem.toggle();
                if (thisselectItem.is(":visible")) {
                    thisBox.css({'z-index': opt.zindex + 1});
                } else {
                    thisBox.css({'z-index': opt.zindex});
                }
                event.stopPropagation();

            }

            function excuteFunc(fn, data) {//外部调用函数
                if (typeof fn == 'function') {
                    return fn(data);
                }
            }

            if (typeof init == 'function') {
                init(data);
            }


        })

    };

    $.fn.isSearchSelect = function (opt, callback, init) {
        if (CstlyeSkin.indexOf("frame4.0") != -1) {
            return;
        } else {
            opt = $.extend({
                selectBox: "c_selectBox",//最外层模拟select的div
                input: 'c_input',//显示数据用的input
                selectItem: 'c_result',
                selectImg: 'c_selectimg',
                selectedItem: 'selected',
                selectedItem_bg: 'div_bg',
                select_height: 20,
                select_bd: " div_bd",
                isInput: true,//是否允许手动输入
                width: 200,//模拟select的宽度
                width_ul: 0,//ul的宽度
                inputWidth: 178,
                height: 300,//select的最大高度
                zindex: 20,//层级顺序
                isHideBorder: false,
                isHide: false//是否隐藏控件
            }, opt || {});

            $(this).hide();//select默认隐藏
            return this.each(function () {
                var self = this;

                var thisBox, thisInputVal, thisselectItem, thisselectItem_li, thisselect, data = {}, display = "",
                    selectItem_height, isHideBorder = '', isHide = '';
                opt.select_height = opt.select_height == 0 ? $(self).height() : opt.select_height;
                var opt_width = (opt.width == 0) ? $(self).width() : opt.width;
                opt.width_ul = (opt.width_ul == 0) ? opt_width : opt.width_ul;
                opt.inputWidth = opt_width - 18;
                opt.input_height = opt.select_height - 4;
                selectItem_height = $(self).find('option').size() * 24;//c_result的高度
                if (opt.isHideBorder) {
                    isHideBorder = '';
                } else {
                    isHideBorder = 'div_bd';
                }
                if (opt.isHide) {
                    isHide = 'display:none';
                } else {
                    isHide = '';
                }
                var selectVal = $.trim($(self).find('option:selected').text());
                var html = "<div class='" + opt.selectBox + ' ' + isHideBorder + "' style='width:" + opt_width + "px;height:" + opt.select_height + "px;'" + isHide + ">" +
                    "<input name='input' autocomplete='off' class=" + opt.input + " style='width:" + opt.inputWidth + "px;height:" + opt.input_height + "px;line-height:" + opt.input_height + "px;' value=''/><div class=" + opt.selectImg + "></div><ul class='" + opt.selectItem + " div_bd' style='width:" + opt.width_ul + "px;height:" + selectItem_height + "px;top:" + opt.select_height + "px;'>";
                //添加下拉框列表项


                $(self).find('option').each(function () {
                    if ($(this).is(':selected')) {
                        html += "<li class='" + opt.selectedItem + display + "'>" + $.trim($(this).text()) + "</li>";

                    } else {
                        html += "<li class='" + display + "'>" + $.trim($(this).text()) + "</li>";
                    }

                })
                html += "</ul></div>";
                $(self).after(html);

                thisBox = $(self).next('.' + opt.selectBox);
                thisInputVal = thisBox.find('.' + opt.input);
                thisselectItem = thisBox.find('.' + opt.selectItem);
                thisselectItem_li = thisBox.find('.' + opt.selectItem).find('li');
                thisselect = thisBox.find('.' + opt.selectImg);
                thisInputVal.val($.trim($(self).find("option:selected").text()));
                data.box = {
                    'selectBox': thisBox,
                    'input': thisInputVal,
                    'selectItem': thisselectItem,
                    'selectItem_li': thisselectItem_li,
                    'selectImg': thisselect,
                    'self': self
                };
                if (opt.isInput) {
                    thisInputVal.bind('click keyup', function (event) {//输入框输入查找
                        var text = inputKeyup(this, event);
                        data.event = event.type;
                        data.value = text;
                        //存在selected未被初始化的情况 这里重新赋值
                        if (!data.selected) {
                            data.selected = $(self).find('option').eq(0);
                        }
                        //输入框触发事件
                        $.extend(data, {isSelect: false});
                        excuteFunc(callback, data);//外部调用
                    })
                    thisselect.click(function (event) {
                        selectToggle(event);
                    });
                } else {
                    thisInputVal.attr('readonly', true).css({'cursor': 'pointer'});
                    thisBox.click(function (event) {
                        $(this).css({'z-index': opt.zindex + 1});
                        selectToggle(event);
                    })


                }


                thisselectItem.width(opt_width);
                if (thisselectItem.height() > opt.height) {
                    thisselectItem.height(opt.height);
                }

                thisselectItem_li.bind('click', function (event) {
                    $(this).addClass(opt.selectedItem + ' ' + opt.selectedItem_bg).siblings().removeClass(opt.selectedItem + ' ' + opt.selectedItem_bg);
                    thisInputVal.val($(this).text());
                    cIndex = thisselectItem.find('li').index(this);
                    $(self).find('option').attr('selected', false);
                    $(self).find('option').eq(cIndex).attr('selected', 'selected');
                    thisselectItem.hide();
                    thisBox.css({'z-index': opt.zindex});
                    data.event = event.type;
                    data.value = $(self).find('option').eq(cIndex).val();
                    data.text = $(self).find('option').eq(cIndex).text();
                    data.selected = $(self).find('option').eq(cIndex);
                    data.obj = $(self);
                    //下拉框触发事件
                    $.extend(data, {isSelect: true});
                    excuteFunc(callback, data);//外部调用
                    event.stopPropagation();
                }).hover(function () {
                    $(this).addClass(opt.selectedItem + ' ' + opt.selectedItem_bg).siblings().removeClass(opt.selectedItem + ' ' + opt.selectedItem_bg);
                }, function () {
                    $(this).removeClass(opt.selectedItem);
                })

                var timer;
                thisBox.mouseover(function () {
                    clearTimeout(timer);
                }).mouseout(function () {
                    var _this = this;
                    timer = setTimeout(function () {
                        $(_this).find('.' + opt.selectItem).hide();
                        thisBox.css({'z-index': opt.zindex});
                    }, 300);

                })

                function inputKeyup(obj, event) {
                    var _this = obj;
                    $('.' + opt.selectItem).hide();
                    thisselectItem_li.show();
                    var text = $(_this).val();
                    var textTemp=text;
                    if(text.indexOf("(")>=0){
                    	textTemp=textTemp.replace("\(","\\(");
                    }
                    var reg = new RegExp(textTemp, "i");
                    thisselectItem_li.each(function (i) {
                        var ithat = this;
                        var flag = reg.test($(ithat).text());
                        if (flag || text == '') {
                            $(ithat).show();
                        } else {
                            $(ithat).hide();
                        }
                    })
                    thisselectItem.show();
                    thisBox.css({'z-index': opt.zindex + 1});
                    return text;
                    event.stopPropagation();
                }


                function selectToggle(event) {
                    thisselectItem_li.not('.hide').show();
                    thisselectItem.toggle();
                    if (thisselectItem.is(":visible")) {
                        thisBox.css({'z-index': opt.zindex + 1});
                    } else {
                        thisBox.css({'z-index': opt.zindex});
                    }
                    event.stopPropagation();

                }

                function excuteFunc(fn, data) {//外部调用函数
                    if (typeof fn == 'function') {
                        return fn(data);
                    }
                }

                if (typeof init == 'function') {
                    init(data);
                }
            });
        }
    };


    $.fn.isSearchOldSelect = function (opt, callback, init) {
        opt = $.extend({
            selectBox: "c_selectBox",//最外层模拟select的div
            input: 'c_input',//显示数据用的input
            selectItem: 'c_result',
            selectImg: 'c_selectimg',
            selectedItem: 'selected',
            selectedItem_bg: 'div_bg',
            select_height: 20,
            select_bd: " div_bd",
            isInput: true,//是否允许手动输入
            width: 200,//模拟select的宽度
            width_ul: 0,//ul的宽度
            inputWidth: 178,
            height: 300,//select的最大高度
            zindex: 20,//层级顺序
            isHideBorder: false,
            isHide: false//是否隐藏控件
        }, opt || {});

        $(this).hide();//select默认隐藏
        return this.each(function () {
            var self = this;

            var thisBox, thisInputVal, thisselectItem, thisselectItem_li, thisselect, data = {}, display = "",
                selectItem_height, isHideBorder = '', isHide = '';
            opt.select_height = opt.select_height == 0 ? $(self).height() : opt.select_height;
            var opt_width = (opt.width == 0) ? $(self).width() : opt.width;
            opt.width_ul = (opt.width_ul == 0) ? opt_width : opt.width_ul;
            opt.inputWidth = opt_width - 18;
            opt.input_height = opt.select_height - 4;
            selectItem_height = $(self).find('option').size() * 24;//c_result的高度
            if (opt.isHideBorder) {
                isHideBorder = '';
            } else {
                isHideBorder = 'div_bd';
            }
            if (opt.isHide) {
                isHide = 'display:none';
            } else {
                isHide = '';
            }
            var selectVal = $.trim($(self).find('option:selected').text());
            var html = "<div class='" + opt.selectBox + ' ' + isHideBorder + "' style=width:" + opt_width + "px;height:" + opt.select_height + "px;" + isHide + ">" +
                "<input name='input' autocomplete='off' class=" + opt.input + " style='width:" + opt.inputWidth + "px;height:" + opt.input_height + "px;line-height:" + opt.input_height + "px;' value=''/><div class=" + opt.selectImg + "></div><ul class='" + opt.selectItem + " div_bd' style='width:" + opt.width_ul + "px;height:" + selectItem_height + "px;top:" + opt.select_height + "px;'>";
            //添加下拉框列表项


            $(self).find('option').each(function () {
                if ($(this).is(':selected')) {
                    html += "<li class='" + opt.selectedItem + display + "'>" + $.trim($(this).text()) + "</li>";

                } else {
                    html += "<li class='" + display + "'>" + $.trim($(this).text()) + "</li>";
                }

            })
            html += "</ul></div>";
            $(self).after(html);

            thisBox = $(self).next('.' + opt.selectBox);
            thisInputVal = thisBox.find('.' + opt.input);
            thisselectItem = thisBox.find('.' + opt.selectItem);
            thisselectItem_li = thisBox.find('.' + opt.selectItem).find('li');
            thisselect = thisBox.find('.' + opt.selectImg);
            thisInputVal.val($.trim($(self).find("option:selected").text()));
            data.box = {
                'selectBox': thisBox,
                'input': thisInputVal,
                'selectItem': thisselectItem,
                'selectItem_li': thisselectItem_li,
                'selectImg': thisselect,
                'self': self
            };
            if (opt.isInput) {
                thisInputVal.bind('click keyup', function (event) {//输入框输入查找
                    var text = inputKeyup(this, event);
                    data.event = event.type;
                    data.value = text;
                    //存在selected未被初始化的情况 这里重新赋值
                    if (!data.selected) {
                        data.selected = $(self).find('option').eq(0);
                    }
                    //输入框触发事件
                    $.extend(data, {isSelect: false});
                    excuteFunc(callback, data);//外部调用
                })
                thisselect.click(function (event) {
                    selectToggle(event);
                });
            } else {
                thisInputVal.attr('readonly', true).css({'cursor': 'pointer'});
                thisBox.click(function (event) {
                    $(this).css({'z-index': opt.zindex + 1});
                    selectToggle(event);
                })


            }


            thisselectItem.width(opt_width);
            if (thisselectItem.height() > opt.height) {
                thisselectItem.height(opt.height);
            }

            thisselectItem_li.bind('click', function (event) {
                $(this).addClass(opt.selectedItem + ' ' + opt.selectedItem_bg).siblings().removeClass(opt.selectedItem + ' ' + opt.selectedItem_bg);
                thisInputVal.val($(this).text());
                cIndex = thisselectItem.find('li').index(this);
                $(self).find('option').attr('selected', false);
                $(self).find('option').eq(cIndex).attr('selected', 'selected');
                thisselectItem.hide();
                thisBox.css({'z-index': opt.zindex});
                data.event = event.type;
                data.value = $(self).find('option').eq(cIndex).val();
                data.text = $(self).find('option').eq(cIndex).text();
                data.selected = $(self).find('option').eq(cIndex);
                data.obj = $(self);
                //下拉框触发事件
                $.extend(data, {isSelect: true});
                excuteFunc(callback, data);//外部调用
                event.stopPropagation();
            }).hover(function () {
                $(this).addClass(opt.selectedItem + ' ' + opt.selectedItem_bg).siblings().removeClass(opt.selectedItem + ' ' + opt.selectedItem_bg);
            }, function () {
                $(this).removeClass(opt.selectedItem);
            })

            var timer;
            thisBox.mouseover(function () {
                clearTimeout(timer);
            }).mouseout(function () {
                var _this = this;
                timer = setTimeout(function () {
                    $(_this).find('.' + opt.selectItem).hide();
                    thisBox.css({'z-index': opt.zindex});
                }, 300);

            })

            function inputKeyup(obj, event) {
                var _this = obj;
                $('.' + opt.selectItem).hide();
                thisselectItem_li.show();
                var text = $(_this).val();
                var textTemp=text;
                if(text.indexOf("(")>=0){
                	textTemp=textTemp.replace("\(","\\(");
                }
                var reg = new RegExp(textTemp, "i");
                thisselectItem_li.each(function (i) {
                    var ithat = this;
                    var flag = reg.test($(ithat).text());
                    if (flag || text == '') {
                        $(ithat).show();
                    } else {
                        $(ithat).hide();
                    }
                })
                thisselectItem.show();
                thisBox.css({'z-index': opt.zindex + 1});
                return text;
                event.stopPropagation();
            }


            function selectToggle(event) {
                thisselectItem_li.not('.hide').show();
                thisselectItem.toggle();
                if (thisselectItem.is(":visible")) {
                    thisBox.css({'z-index': opt.zindex + 1});
                } else {
                    thisBox.css({'z-index': opt.zindex});
                }
                event.stopPropagation();

            }

            function excuteFunc(fn, data) {//外部调用函数
                if (typeof fn == 'function') {
                    return fn(data);
                }
            }

            if (typeof init == 'function') {
                init(data);
            }
        });
    };
})(jQuery);

//$(function(){
if (CstlyeSkin.indexOf("frame4.0") != -1) {
    var $select = $("#condition select");

    if ($select.length > 0) {
        $select.each(function (index, domEle) {
            var flag = domEle.getAttribute("isInput");
            if ("reportType" != domEle.id && flag != "false") {
                $("#" + domEle.id).isSearchSelectNew({'width': '198', 'isInput': true, 'zindex': 0});
            } else {
                $("#" + domEle.id).isSearchSelectNew({'width': '198', 'isInput': false, 'zindex': 0});
            }
        });

    }
}
if (CstlyeSkin.indexOf("frame4.0") != -1) {
    var $select = $("#selfSelect select");

    if ($select.length > 0) {
        $select.each(function (index, domEle) {
            var flag = domEle.getAttribute("isInput");
            if ("reportType" != domEle.id && flag != "false") {
                $("#" + domEle.id).isSearchSelectNew({'width': '198', 'isInput': true, 'zindex': 0});
            } else {
                $("#" + domEle.id).isSearchSelectNew({'width': '198', 'isInput': false, 'zindex': 0});
            }
        });

    }
}
//});
