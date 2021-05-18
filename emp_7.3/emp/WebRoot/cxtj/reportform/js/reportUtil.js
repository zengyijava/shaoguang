/**
 * 操作员树的数据转化
 * @param tree
 * @param map
 * @returns {Array}
 */
function convertTree(tree, map) {
    var result = [];
    // 遍历 tree
    if (tree) {
        for (var i = 0; i < tree.length; i++) {
            var item = tree[i];
            var id = item[map.id];
            var label = item[map.label];
            var children = item[map.children];
            var disabled = false;
            if (id && id.indexOf('org') != -1) {
                disabled = true;
            }
            // 如果有子节点, 递归
            if (children) {
                children = convertTree(children, map);
            }
            result.push({
                id: id,
                label: label,
                children: children,
                disabled: disabled
            })
        }
        return result;
    }
}

// 判断浏览器版本
function browserData() {
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器
    var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器
    var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
    if (isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if (fIEVersion == 7) {
            return 7;
        } else if (fIEVersion == 8) {
            return 8;
        } else if (fIEVersion == 9) {
            return 9;
        } else if (fIEVersion == 10) {
            return 10;
        } else {
            return 6;//IE版本<=7
        }
    } else if (isEdge) {
        return 'edge';
    } else if (isIE11) {
        return 11; //IE11
    } else {
        return -1;//不是ie浏览器
    }
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

/*
* 时间格式化工具
* 把Long类型的1527672756454日期还原yyyy-MM-dd 00:00:00格式日期
*/
function datetimeFormat(longTypeDate) {
    var dateTypeDate = "";
    var date = new Date();
    date.setTime(longTypeDate);
    dateTypeDate += date.getFullYear();   //年
    dateTypeDate += "-" + getMonth(date); //月
    dateTypeDate += "-" + getDay(date);   //日
    dateTypeDate += " " + getHours(date);   //时
    dateTypeDate += ":" + getMinutes(date);     //分
    dateTypeDate += ":" + getSeconds(date);     //分
    return dateTypeDate;
}

/*
 * 时间格式化工具
 * 把Long类型的1527672756454日期还原yyyy-MM-dd格式日期
 */
function dateFormat(longTypeDate) {
    var dateTypeDate = "";
    var date = new Date();
    date.setTime(longTypeDate);
    dateTypeDate += date.getFullYear();   //年
    dateTypeDate += "-" + getMonth(date); //月
    dateTypeDate += "-" + getDay(date);   //日
    return dateTypeDate;
}

//返回 01-12 的月份值
function getMonth(date) {
    var month = "";
    month = date.getMonth() + 1; //getMonth()得到的月份是0-11
    if (month < 10) {
        month = "0" + month;
    }
    return month;
}

//返回01-30的日期
function getDay(date) {
    var day = "";
    day = date.getDate();
    if (day < 10) {
        day = "0" + day;
    }
    return day;
}

//小时
function getHours(date) {
    var hours = "";
    hours = date.getHours();
    if (hours < 10) {
        hours = "0" + hours;
    }
    return hours;
}

//分
function getMinutes(date) {
    var minute = "";
    minute = date.getMinutes();
    if (minute < 10) {
        minute = "0" + minute;
    }
    return minute;
}

//秒
function getSeconds(date) {
    var second = "";
    second = date.getSeconds();
    if (second < 10) {
        second = "0" + second;
    }
    return second;
}

/**
 * 获取表头
 */
function getTableColumns(data, module) {
    if (module) {
        return data[module];
    }
}

function getMenuItem(data, module) {
    var spType = (data.spType.indexOf(module) != -1);
    var messageType = (data.messageType.indexOf(module) != -1);
    var busType = (data.busType.indexOf(module) != -1);
    var provinces = (data.provinces.indexOf(module) != -1);
    var spisuncm = (data.spisuncm.indexOf(module) != -1);
    var spuserId = (data.spuserId.indexOf(module) != -1);
    var org = (data.org.indexOf(module) != -1);
    var user = (data.user.indexOf(module) != -1);
    var staffname = (data.staffname.indexOf(module) != -1);
    var msType = (data.msType.indexOf(module) != -1);
    var paramStr = (data.paramStr.indexOf(module) != -1);
    var reportType = (data.reportType.indexOf(module) != -1);
    var sendType = (data.sendType.indexOf(module) != -1);
    var data = {
        spType: spType,
        busType: busType,
        messageType: messageType,
        provinces: provinces,
        spisuncm: spisuncm,
        sendType: sendType,
        spuserId: spuserId,
        org: org,
        user: user,
        staffname: staffname,
        msType: msType,
        paramStr: paramStr,
        reportType: reportType
    }
    return data
}

function getDefaultCheckedData(menuItem) {
    var data = [];
    if (menuItem['busType']) {
        data.push("busType");
    }
    if (menuItem['user']) {
        data.push("user");
    }
    if (menuItem['org']) {
        data.push("org");
    }
    if (menuItem['spuserId']) {
        data.push("spuserId");
    }
    if (menuItem['spisuncm']) {
        data.push("spisuncm");
    }
    if (menuItem['provinces']) {
        data.push("provinces");
    }
    return data;
}

/**
 *   "provinces",
 "user",
 "org",
 "spuserId",
 "spisuncm",
 "busType"
 *  更多维度获取对应列表项, 如果取消选中的话, 对应的项要隐藏
 * @param checkedData
 */
function getMoreMenuItem(checkedData, menuItem, defaultCheckedData) {
    var busType = (checkedData.indexOf("busType") != -1) || (defaultCheckedData.indexOf("busType") != -1);
    var provinces = (checkedData.indexOf("provinces") != -1) || (defaultCheckedData.indexOf("provinces") != -1);
    var spisuncm = (checkedData.indexOf("spisuncm") != -1) || (defaultCheckedData.indexOf("spisuncm") != -1);
    var spuserId = (checkedData.indexOf("spuserId") != -1) || (defaultCheckedData.indexOf("spuserId") != -1);
    var org = (checkedData.indexOf("org") != -1) || (defaultCheckedData.indexOf("org") != -1);
    var user = (checkedData.indexOf("user") != -1) || (defaultCheckedData.indexOf("user") != -1);
    var data = {
        spType: menuItem.spType,
        busType: busType,
        messageType: menuItem.messageType,
        provinces: provinces,
        spisuncm: spisuncm,
        sendType: menuItem.sendType,
        spuserId: spuserId,
        org: org,
        user: user,
        staffname: menuItem.staffname,
        msType: menuItem.msType,
        paramStr: menuItem.paramStr,
        reportType: menuItem.reportType
    }
    return data
}
