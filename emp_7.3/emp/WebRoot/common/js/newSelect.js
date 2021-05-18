/**
 * 因为原有组件有bug; 为了不影响原有的项目，所以在原有组件的基础上，进行重写;
 * by lianghuageng 2018-11-02
 **/
(function () {
    /*
     * @param opt 用来进行基础的初始化操作，包括样式等等；
     * @param callback 点击事件的回调函数；
     * @returns {*}
     */
    $.fn.isSearchSelectNew = function (opt, callback, init) {
        opt = $.extend({
            id : 'newSelect',
            selectBox: "c_selectBox",   // 最外层模拟select的div
            input: 'c_input',   // 显示数据用的input
            selectItem: 'c_result',
            selectImg: 'c_selectImg',   // 选择框小箭头的图片
            selectedItem: 'selected',   // 默认的选中项
            selectedItem_bg: 'div_bg',
            select_height: 20,
            select_bd: " div_bd",
            enableInput: true,  // 是否允许手动输入
            width: 200, // 模拟select的宽度
            width_ul: 0,    // ul的宽度
            inputWidth: 178,
            maxHeight: 300,    // select的最大高度
            zIndex: 20,     // 层级顺序
            isHideBorder: false,
            isHide: false//是否隐藏控件
        }, opt || {});

        $(this).hide(); //select默认隐藏
        // 兼容非 4.0 皮肤;
        if (CstlyeSkin.indexOf("frame4.0") == -1) {
            opt.selectImg = 'c_selectImg_02';
            opt.select_height = 24;
            opt.selectBox = "c_selectBox_02";
        }

        return this.each(function () {
            //将 self --> this;
            var self = this;

            var thisBox, thenableInputVal, thisSelectItem, thisSelectItem_li, thisSelect, data = {}, display = "",
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
            var html = "<div id = '" + opt.id + "' class='" + opt.selectBox + ' ' + isHideBorder + "' style=width:" + opt_width + "px;height:" + opt.select_height + "px;" + isHide + ">";
            if (opt.enableInput == false) {
                html += "<input name='input' unselectable='on' autocomplete='off' class=" + opt.input + " style='width:" + opt.inputWidth + "px;height:" + opt.input_height + "px;line-height:" + opt.input_height + "px;' value=''/><div class=" + opt.selectImg + "></div><ul class='" + opt.selectItem + " div_bd' style='width:" + opt.width_ul + "px;height:" + selectItem_height + "px;top:" + opt.select_height + "px;'>";
            } else {
                html += "<input name='input' autocomplete='off' class=" + opt.input + " style='width:" + opt.inputWidth + "px;height:" + opt.input_height + "px;line-height:" + opt.input_height + "px;' value=''/><div class=" + opt.selectImg + "></div><ul class='" + opt.selectItem + " div_bd' style='width:" + opt.width_ul + "px;height:" + selectItem_height + "px;top:" + opt.select_height + "px;'>";
            }

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
            thenableInputVal = thisBox.find('.' + opt.input);
            thisSelectItem = thisBox.find('.' + opt.selectItem);
            thisSelectItem_li = thisBox.find('.' + opt.selectItem).find('li');
            thisSelect = thisBox.find('.' + opt.selectImg);
            thenableInputVal.val($.trim($(self).find("option:selected").text()));
            data.box = {
                'selectBox': thisBox,
                'input': thenableInputVal,
                'selectItem': thisSelectItem,
                'selectItem_li': thisSelectItem_li,
                'selectImg': thisSelect,
                'self': self
            };
            if (opt.enableInput) {
                thenableInputVal.bind('click keyup', function (event) {//输入框输入查找
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
                thenableInputVal.bind('change', function (event) {//输入框输入查找
                    var text = inputKeyup(this, event);
                    data.event = "click";
                    data.value = text;
                    data.text = text;

                    var select_value = '';
                    var cIndex = 0;
                    var channel = $(self).find("option");
                    for (var i = 0; i < channel.length; i++) {
                        var channlVar = channel.eq(i).val();    //option中的值
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
                thisSelect.click(function (event) {
                    selectToggle(event);
                });
            } else {
                thenableInputVal.attr('readonly', true).css({'cursor': 'pointer'});
                thisBox.click(function (event) {
                    $(this).css({'z-index': opt.zIndex + 1});
                    selectToggle(event);
                })
            }

            thisSelectItem.width(opt_width);
            if (thisSelectItem.height() > opt.maxHeight) {
                thisSelectItem.height(opt.maxHeight);
            }

            thisSelectItem_li.bind('click', function (event) {
                $(this).addClass(opt.selectedItem + ' ' + opt.selectedItem_bg).siblings().removeClass(opt.selectedItem + ' ' + opt.selectedItem_bg);
                thenableInputVal.val($(this).text());
                cIndex = thisSelectItem.find('li').index(this);
                $(self).find('option').attr('selected', false);
                $(self).find('option').eq(cIndex).attr('selected', 'selected');
                thisSelectItem.hide();
                thisBox.css({'z-index': opt.zIndex});
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
                    thisBox.css({'z-index': opt.zIndex});
                }, 300);

            })

            function inputKeyup(obj, event) {
                var _this = obj;
                $('.' + opt.selectItem).hide();
                thisSelectItem_li.show();
                var text = $(_this).val();
                var reg = new RegExp(text, "i");
                thisSelectItem_li.each(function (i) {
                    var ithat = this;
                    var flag = reg.test($(ithat).text());
                    if (flag || text == '') {
                        $(ithat).show();
                    } else {
                        $(ithat).hide();
                    }
                })
                thisSelectItem.show();
                thisBox.css({'z-index': opt.zIndex + 1});
                return text;
                event.stopPropagation();
            }


            function selectToggle(event) {
                thisSelectItem_li.not('.hide').show();
                thisSelectItem.toggle();
                if (thisSelectItem.is(":visible")) {
                    thisBox.css({'z-index': opt.zIndex + 1});
                } else {
                    thisBox.css({'z-index': opt.zIndex});
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
})(jQuery);
