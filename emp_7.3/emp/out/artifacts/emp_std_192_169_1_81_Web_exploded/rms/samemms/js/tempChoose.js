//查询分类，1-我的模板，2-公共模板
var queryConstantType = {myTemp:1,publicTemp:2};
var myTempPage = {"pageSize":"9","pageIndex":"1"};
var publicTempPage = {"pageSize":"9","pageIndex":"1"};
var myTempVo = {"isPublic":"0"};
var publicTempVo = {"isPublic":"1"};
var pathUrl = $("#pathUrl").val();
//var industryid;
//var useid;
var isFirstLoadPublic = true;
$(document).ready(function() {
	queryRequest($("#lguserid").val(),JSON.stringify(myTempPage),JSON.stringify(myTempVo),queryConstantType.myTemp);
	//queryRequest($("#lguserid").val(),JSON.stringify(publicTempPage),JSON.stringify(publicTempVo),queryConstantType.publicTemp);
	loadIndustryUse();
	
	$("#addMyTemp").click(function(){
		window.parent.parent.openNewTab("5100-1300",pathUrl+'/mbgl_mytemplate.htm?method=doAdd&type=3&lgcorpcode='+$("#lgcorpcode").val());
	});
	
	$("#addPublicTemp").click(function(){
		var lgcorpcode= $("#lgcorpcode").val();
		if(lgcorpcode == '100000'){
			window.parent.parent.openNewTab("5100-1350",pathUrl+"/mbgl_mytemplate.htm?method=doAdd&type=3&lgcorpcode="+lgcorpcode+"&source=commontemplate");
		}else{
			window.parent.parent.openNewTab("5100-1300",pathUrl+'/mbgl_mytemplate.htm?method=doAdd&type=3&lgcorpcode='+$("#lgcorpcode").val());
		}
	});
});
$("#resultDiv").scroll(function () {
	var h = $(this).height();//div可视区域的高度
    var sh = $(this)[0].scrollHeight;//滚动的高度，$(this)指代jQuery对象，而$(this)[0]指代的是dom节点
    var st = $(this)[0].scrollTop;//滚动条的高度，即滚动条的当前位置到div顶部的距离
    //上面的代码是判断滚动条滑到底部的代码
    if (h + st >= sh) {
    	var isActive = $("#myTemp").hasClass('active');
    	var page = {};
    	var vo = {};
    	var type ;
        var tempType = "";
        var tempId = "";
        var tempName = "";
        var isSearch = $("#isSearch").val();
    	if(isActive){
    		page = myTempPage;
    		vo = {"isPublic":"0"};
    		type = queryConstantType.myTemp;
    		if(isSearch === "1"){
                tempType = $("#my_tempType").next("div.c_selectBox").find(".c_input").val();
                if("静态场景" === tempType){
                    tempType = "0";
				}else if("动态场景" === tempType){
                    tempType = "1";
				}else {
                    tempType = "";
                }
                tempId = $("#my_tempId").val();
                tempName = $("#my_tempName").val();
            }
    	}else{
    		page = publicTempPage;
    		vo = {"isPublic":"1"};
    		type = queryConstantType.publicTemp;
            if(isSearch === "1"){
                tempType = $("#pub_tempType").next("div.c_selectBox").find(".c_input").val();
                if("静态场景" === tempType){
                    tempType = "0";
                }else if("动态场景" === tempType){
                    tempType = "1";
                }else {
                    tempType = "";
				}
                tempId = $("#pub_tempId").val();
                tempName = $("#pub_tempName").val();
            }
    	}
    	//如果查询条件不为空则带上查询条件
        if(tempType !== undefined && tempType !== ""){
            vo.dsflag = tempType;
        }
        if(tempId !== undefined && tempId !== ""){
            vo.sptemplid = tempId;
        }
        if(tempName !== undefined && tempName !== ""){
            vo.tmName = tempName;
        }
    	if(page.pageIndex === page.totalPage){
    		return;
        }
    	page.pageIndex = page.pageIndex+1;
    	queryRequest($("#lguserid").val(),JSON.stringify(page),JSON.stringify(vo),type);
    }
});

function myTempQuery(){
	$("#isSearch").val("1");
	var vo = {"isPublic":"0"};
	var page = {"pageSize":"9","pageIndex":"1"};
	$("#myResult").html("");
    var tempType = $("#my_tempType").next("div.c_selectBox").find(".c_input").val();
    if("静态场景" === tempType){
        tempType = "0";
    }else if("动态场景" === tempType){
        tempType = "1";
    }else {
        tempType = "";
    }
	var tempId = $("#my_tempId").val();
	var tempName = $("#my_tempName").val();
	if(tempType !== null && tempType !== ""){
		vo.dsflag = tempType;
	}
	if(tempId !== null && tempId !== ""){
		vo.sptemplid = tempId;
	}
	if(tempName != null && tempName !== ""){
		vo.tmName = tempName;
	}
	queryRequest($("#lguserid").val(),JSON.stringify(page),JSON.stringify(vo),queryConstantType.myTemp);
}
function publicTempQuery(){
    $("#isSearch").val("1");
    var vo = {"isPublic":"1"};
	var page = {"pageSize":"9","pageIndex":"1"};
	$("#publicResult").html("");
    var tempType = $("#pub_tempType").next("div.c_selectBox").find(".c_input").val();
    if("静态场景" === tempType){
        tempType = "0";
    }else if("动态场景" === tempType){
        tempType = "1";
    }else {
        tempType = "";
    }
	var tempId = $("#pub_tempId").val();
	var tempName = $("#pub_tempName").val();
	if(tempType !== "null" && tempType !== "" && tempType !== undefined){
		vo.dsflag = tempType;
	}
	if(tempId !== "null" && tempId !== "" && tempType !== undefined){
		vo.sptemplid = tempId;
	}
	if(tempName !== "null" && tempName !== "" && tempType !== undefined){
		vo.tmName = tempName;
	}
	queryRequest($("#lguserid").val(),JSON.stringify(page),JSON.stringify(vo),queryConstantType.publicTemp);
	publicTempVo = vo;
}

function industryContionQuery(industryid){
	var vo = {"isPublic":"1"};
	var page = {"pageSize":"9","pageIndex":"1"};
	$("#publicResult").html("");
	if(industryid!=null&&industryid!="all"){
		vo.industryid = industryid;
	}
	queryRequest($("#lguserid").val(),JSON.stringify(page),JSON.stringify(vo),queryConstantType.publicTemp);
	publicTempVo = vo;
}

function useConditionQuery(useid){
	var vo = {"isPublic":"1"};
	var page = {"pageSize":"9","pageIndex":"1"};
	$("#publicResult").html("");
	if(useid!=null&&useid!="all"){
		vo.useid = useid;
	}
	queryRequest($("#lguserid").val(),JSON.stringify(page),JSON.stringify(vo),queryConstantType.publicTemp);
	publicTempVo = vo;
}

function queryRequest(loginUserId,pageInfo,lfTemplateVo,type){
    if(type === queryConstantType.myTemp ){
        $.post(pathUrl+"/mbgl_mytemplate.htm?method=findTemplatePage",
            {
                "loginUserId":loginUserId,
                "pageInfo":pageInfo,
                "lfTemplateVo":lfTemplateVo
            },
            function(result){
                var data = eval('('+result+')');
                page = data.pageInfo;
                var record = data.record;
                myTempPage = page;
                loadTempHtml(record,"myResult");
                loadResultDiv(queryConstantType.myTemp);
            });
    }else if(type === queryConstantType.publicTemp){
        $.post(pathUrl+"/rms_rmsSameMms.htm?method=findPublicTemplatePage",
            {
                "loginUserId":loginUserId,
                "pageInfo":pageInfo,
                "lfTemplateVo":lfTemplateVo
            },
            function(result){
                var data = eval('('+result+')');
                page = data.pageInfo;
                var record = data.record;
                publicTempPage = page;
                loadTempHtml(record,"publicResult");
                loadResultDiv(queryConstantType.publicTemp);
            });
    }
}

function loadTempHtml(data,putId){
	for(index in data){
		var url = data[index].tmMsg;
		url = url.replace("fuxin.rms","firstframe.jsp");
		url = pathUrl +"/" +url;
		//alert(url);
		var tempHtml = '<li class="module-li"><div class="module-li-img">';
		tempHtml += '<div id="img_'+data[index].tmid+'" class="img"></div><div class="li-views-btn">';
        //判断如果预览异常则不能预览
		var hiddenStr = "";
        if(data[index].previewError === 1){
            hiddenStr = "display:none;";
        }
		tempHtml += '<i class="view-icon" style="'+hiddenStr+'" urlMsg="'+data[index].tmMsg+'" tmName= "'+data[index].tmName+'" tmid="'+data[index].tmid+'" onclick="doPreview(this)"></i></div></div><div class="module-li-info"><div class="info-hd"><p class="max-width-id">ID:';
		var params = '<input type="hidden" id="_tempId" name="_tempId" value="'+data[index].sptemplid+'"/>';
		params += '<input type="hidden" id="_paramCount" name="_paramCount" value="'+data[index].paramcnt+'"/>';
		params += '<input type="hidden" id="_tempDegree" name="_tempDegree" value="'+data[index].degree+'"/>';
		params += '<input type="hidden" id="_tempSize" name="_tempSize" value="'+data[index].degreeSize+'"/>';
		params += '<input type="hidden" id="_tempName" name="_tempName" value="'+data[index].tmName+'"/>';
		params += '<input type="hidden" id="_tempUrl" name="_tempUrl" value="'+data[index].tmMsg+'"/>';
		params += '<input type="hidden" id="_tempType" name="_tempType" value="'+data[index].dsflag+'"/>';
		params += '<input type="hidden" id="_tmId" name="_tmId" value="'+data[index].tmid+'"/>';
        params += '<input type="hidden" id="_tempVer" name="_tempVer" value="'+data[index].ver+'"/>';

		tempHtml += data[index].sptemplid + '</p><p class="info-right max-width-text">';
		tempHtml += data[index].tmName + '</p></div><div class="info-ft"><p>大小：<span>';
		tempHtml += data[index].degreeSize + '</span>KB</p><p class="info-right">档位：<span>';
		var clickEvent = "javaScript:tempSure1('id_"+data[index].sptemplid+"')";
        //判断如果预览异常则不能发送
		var addClass = "";
        if(data[index].previewError === 1){
            clickEvent = "javaScript:void(0)";
            addClass = "preForbidden";
		}
		tempHtml += data[index].degree + '</span>挡</p></div><div class="info-hover">' +
			'<div class="hover-bg"></div>' +
			'<a id="id_'+data[index].sptemplid+'" href="'+ clickEvent +'" class="module-li-btn green-btn '+ addClass +'">立即使用</a>';
		tempHtml += params + '</div></div></li>';
		//$(".module-list").append(tempHtml);
		$("#"+putId).append(tempHtml);
		$("#img_"+data[index].tmid).load(url);
	}
	firstFrameHandle();
}

function selectTemp(id){
    $("#isSearch").val("0");
	if(id == "myTemp"){
		var isActive = $("#myTemp").hasClass('active');

		if(!isActive){
			$("#publicTemp").removeClass("active");
			$("#myTemp").addClass("active");
			$("#myCondition").show();
			$("#publicCondition").hide();
			loadResultDiv(queryConstantType.myTemp);
		}
	}else{
		if(isFirstLoadPublic){
			queryRequest($("#lguserid").val(),JSON.stringify(publicTempPage),JSON.stringify(publicTempVo),queryConstantType.publicTemp);
			isFirstLoadPublic = false;
		}
		var isActive = $("#myTemp").hasClass('active');
		if(isActive){
			$("#myTemp").removeClass("active");
			$("#publicTemp").addClass("active");
			$("#publicCondition").show();
			$("#myContion").hide();
			loadResultDiv(queryConstantType.publicTemp);
		}
	}
}

function loadResultDiv(select){
	var myTemp = $("#myResult").html().trim();
	var publicTemp = $("#publicResult").html().trim();
	if(select == queryConstantType.myTemp){
		if(myTemp != null && myTemp != ""){
			$("#myResult").show();
			$("#publicResult").hide();
			$("#noResultDiv").hide();
		}else{
			$("#myResult").hide();
			$("#publicResult").hide();
			$("#noResultDiv").show();
		}
	}else if(select == queryConstantType.publicTemp){
		if(publicTemp != null && publicTemp != ""){
			$("#myResult").hide();
			$("#publicResult").show();
			$("#noResultDiv").hide();
		}else{
			$("#myResult").hide();
			$("#publicResult").hide();
			$("#noResultDiv").show();
		}
	}
}


function loadIndustryUse(){
	$.post(pathUrl+"/rms_commTpl.htm?method=loadIndustryUse",
		{},function(data){
			var indUse = eval("("+data+")");
			loadIndUseHtml(indUse)
		}
	);

}

function loadIndUseHtml(indUse){
	var industry = indUse.industry;
	var use = indUse.use;
	var induHtml = "";
	var useHtml = "";
	for(index in industry){
		induHtml += '<li number="'+industry[index].id+'"><span>'+industry[index].name+'</span></li>'
	}
	for(index in use){
		useHtml += '<li number="'+use[index].id+'"><span>'+use[index].name+'</span></li>'
	}
	$("#industry").append(induHtml);
	$("#use").append(useHtml);
	$("#industry").children().click(function(){
		//industryid=$(this).attr("number");
		$("#industry").children(".active").removeClass("active");
		$(this).addClass("active");
		industryContionQuery($(this).attr("number"));
	});
	$("#use").children().click(function(){
		//useid=$(this).attr("number");
		$("#use").children(".active").removeClass("active");
		$(this).addClass("active");
		useConditionQuery($(this).attr("number"));
	});

}

function tempSure1(idStr) {
	var pageSource = $("#pageSource").val();
    var obj = $('#'+idStr);
    var source =$("#source").val();

    /**
	 * 当点击立即使用时，需要根据当前选择模板是静态模板还是动态模板改变页面元素
	 * 1.发送号码 上传号码方式改变 （动态模板只上传文件）
	 * 2.格式提示
	 * 3.高级设置 （动态模板还有重号过滤）
     * 4.在右边展示预览页面
     * 5.清除号码内容（动态模板没有批量输入等）
	 * 6.显示号码文件下载(不同内容)
	 *
	 *
	 *
	 * 新增兼容V1.0
     */
    var $selected = $(obj).parent();
    var _tmId = $selected.find("#_tmId").val();
    var _tempVer = $selected.find("#_tempVer").val();
    var _tempId = $selected.find("#_tempId").val();
    var _tempType = $selected.find("#_tempType").val();
    var _tempDegree = $selected.find("#_tempDegree").val();
    var _tempSize = $selected.find("#_tempSize").val();
    var _tempName = $selected.find("#_tempName").val();
    var _tempUrl = $selected.find("#_tempUrl").val();
    var _paramCount = $selected.find("#_paramCount").val();
    if(pageSource==1){
    	var pathUrl = $("#pathUrl").val();
    	$(window.parent.document).find("#popup_div").css({"display":"none"});
		$(window.parent.document).find("#tempFrames").css({"display":"none"});
		window.parent.location.href=pathUrl+"/mbgl_mytemplate.htm?method=doCopy&tmId="+_tmId+"&opType=copy"+"&tmUrl="+_tempUrl+"&industryId=-1"+"&useId=-2&&source="+source;
    	return;
    }

	if(_tempId === undefined || _tempId === "" || _tempId == null) {
		//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_79"));
		alert("未选择富信模板，请重新选择！");
		return;
	}else {
    	//富信主题默认为模板名字
        $(window.parent.document).find("#tempInputName").val(_tempName);
		/*模板自增Id 模板名字 模板类型 模板ID  模板URL 计费档位 容量大小 参数个数 模板版本*/
		$(window.parent.document).find("#tmId").val(_tmId);
		$(window.parent.document).find("#tempId").val(_tempId);
		$(window.parent.document).find("#tempUrl").val(_tempUrl);
        $(window.parent.document).find("#tempDegree").val(_tempDegree);
        $(window.parent.document).find("#tempSize").val(_tempSize);
        $(window.parent.document).find("#paramCount").val(_paramCount);
        $(window.parent.document).find("#tempType").val(_tempType);
        $(window.parent.document).find("#tempName").val(_tempName);
        $(window.parent.document).find("#tempVer").val(_tempVer);
	}
    $(window.parent.document).find("#choose-list").css("display","");
	//清除掉第一次进入的提示语
    $(window.parent.document).find("#firstInInfo").css("display","none");
    $(window.parent.document).find("#firstInInfo").text("");
    //加载格式提示处的悬浮框
    //$(window.parent.document).find("#reminder").css("bottom","420px");

    /*$(window.parent.document).find("#downlinks").hover(function(){
        $(window.parent.document).find("#reminder").css("display","block");
	},function () {
        $(window.parent.document).find("#reminder").css("display","none");
    });*/

    //加载格式提示悬浮框
    parent.showReminder("downlinks","reminder", 1);

    if(_tempType === "1"){
	    //不同内容发送
        $(window.parent.document).find("#choosePerson").css("display","none");
        $(window.parent.document).find("#bulkInput").css("display","none");
        $(window.parent.document).find("#phoneInput").css("display","none");
        //格式提示
        $(window.parent.document).find("#sameInfo").css("display","none");
		if(_tempVer === "V1.0"){
            $(window.parent.document).find("#withArrow").css("display","");
            $(window.parent.document).find("#diffInfo").css("display","none");
            $(window.parent.document).find("#diffInfo_V1").css("display","");
            //隐藏号码格式文件下载
            $(window.parent.document).find("#downExcel").css("display","none");
        }else if(_tempVer === "V2.0"){
            //$(window.parent.document).find("#diffInfo_V1").css("display","none");
            $(window.parent.document).find("#diffInfo").css("display","");
            $(window.parent.document).find("#withArrow").css("display","none");
            //显示号码格式文件下载
            $(window.parent.document).find("#downExcel").css("display","");
        }
        //高级设置
        $(window.parent.document).find("#checkRepeat").css("display","");
        //清除内容
        $(window.parent.document).find("#addressList").css("display","none");
        $(window.parent.document).find(".bultPhone").css("display","none");
        $(window.parent.document).find(".inputPhone").css("display","none");
        $(window.parent.document).find(".sameSendFile").css("display","none");
        $(window.parent.document).find(".diffSendFile").css("display","");
    }else if(_tempType === "0"){
        //相同内容发送
        //发送方式
        $(window.parent.document).find("#choosePerson").css("display","");
        $(window.parent.document).find("#bulkInput").css("display","");
        $(window.parent.document).find("#phoneInput").css("display","");
        //格式提示
        $(window.parent.document).find("#withArrow").css("display","");
        $(window.parent.document).find("#diffInfo").css("display","none");
        $(window.parent.document).find("#diffInfo_V1").css("display","none");
        $(window.parent.document).find("#sameInfo").css("display","");
        //高级设置
        $(window.parent.document).find("#checkRepeat").css("display","none");
        //显示号码文件下载
        $(window.parent.document).find("#downExcel").css("display","none");
        //清除内容
        $(window.parent.document).find("#addressList").css("display","");
        $(window.parent.document).find(".bultPhone").css("display","");
        $(window.parent.document).find(".inputPhone").css("display","");
        $(window.parent.document).find(".sameSendFile").css("display","");
        $(window.parent.document).find(".diffSendFile").css("display","none");
    }else {
        alert("选择富信模板有误，请重新选择！");
        return;
    }

    parent.tempPreview(_tempUrl,_tempName,_tmId);

    //在右边的手机预览窗口展示模板
    $(window.parent.document).find("#tempview").css("display","");



	//调用父窗口的函数/
	parent.closeDialog();
}



function doPreview(obj)
{
	var msg = $(obj).attr("urlMsg");
	var tmName = $(obj).attr("tmName");
	var tmId = $(obj).attr("tmId");
	// tmName = encodeURI(tmName);
	//var pathUrl = $("#pathUrl").val();
	$.post(pathUrl+"/mbgl_mytemplate.htm?method=getTmMsg",{tmUrl:msg,tmName:tmName,tmid:tmId},function(result){
		if (result != null && result != "null" && result != ""){
			document.getElementById("cust_preview").innerHTML = result;
			$('#myView').dialog({
				autoOpen: false,
                width:358,
                height:670,
                closeOnEscape:true
			});
			$("#myView").dialog("open");
            $(".ui-dialog-titlebar").css("height","50px");
            $(".ui-dialog-titlebar").css("background-color","#f7fafc");
            $(".ui-dialog-titlebar").css("border-bottom","1px solid #ecf1f2");
		}else{
			alert("内容文件不存在，无法预览！");
		}
	});
}


function firstFrameHandle(){
	//IE版本小于9
    if(navigator.appName == "Microsoft Internet Explorer" && parseInt(navigator.appVersion.split(";")[1].replace(/[ ]/g, "").replace("MSIE","")) < 9){
        	zoomIE();
		}else{//
			zoom();
		}
}
//IE9以下浏览器等比缩放
function zoomIE(){
	 //计算图参位置进行比例缩放
	 var _widthProportion = 208/385,
	 		_addModuleEL = $(".module-li").find(".J-add-module");

	 $.each(_addModuleEL, function(key){
	 	var __addModuleELTop = _addModuleEL.eq(key).css("top"),
			 __addModuleELLeft = _addModuleEL.eq(key).css("left"),
			 __addModuleELWidth = _addModuleEL.eq(key).find(".editor-resize-text").css("width"),
			 __addModuleELFontsize = _addModuleEL.eq(key).find(".editor-resize-text").css("font-size"),
			 __addModuleELLineHeight = _addModuleEL.eq(key).find(".editor-resize-text").css("line-height");

		  __addModuleELTop = parseInt(__addModuleELTop);
		 __addModuleELLeft = parseInt(__addModuleELLeft);
		 __addModuleELWidth= parseInt(__addModuleELWidth);
		 __addModuleELFontsize = parseInt(__addModuleELFontsize);
		 __addModuleELLineHeight = parseInt(__addModuleELLineHeight);
		 _addModuleEL.eq(key).css({
			 "top": (__addModuleELTop*_widthProportion)+1+"px",
			 "left": (__addModuleELLeft*_widthProportion)+1+"px",
		 });

		 _addModuleEL.eq(key).find(".editor-resize-text").css({
			 "width": (__addModuleELWidth*_widthProportion)+2+"px",
			 "font-size": (__addModuleELFontsize*_widthProportion)+1+"px",
			 "line-height": (__addModuleELLineHeight*_widthProportion)+1+"px",
		 });
	 });
}
//非IE9一下浏览器等比缩放
function zoom(){
	 //计算图参位置进行比例缩放
	 var _widthProportion = 208/385,
	 		_addModuleEL = $(".module-li").find(".J-add-module");

	 $.each(_addModuleEL, function(key){
	 	var __addModuleELTop = _addModuleEL.eq(key).css("top"),
			 __addModuleELLeft = _addModuleEL.eq(key).css("left"),
			 __addModuleELWidth = _addModuleEL.eq(key).find(".editor-resize-text").css("width"),
			 __addModuleELFontsize = _addModuleEL.eq(key).find(".editor-resize-text").css("font-size"),
			 __addModuleELLineHeight = _addModuleEL.eq(key).find(".editor-resize-text").css("line-height");

		  __addModuleELTop = parseInt(__addModuleELTop);
		 __addModuleELLeft = parseInt(__addModuleELLeft);
		 __addModuleELWidth= parseInt(__addModuleELWidth);
		 __addModuleELFontsize = parseInt(__addModuleELFontsize);
		 __addModuleELLineHeight = parseInt(__addModuleELLineHeight);
		 _addModuleEL.eq(key).css({
			 "top": parseInt(__addModuleELTop*_widthProportion)+1+"px",
			 "left": parseInt(__addModuleELLeft*_widthProportion)+1+"px",
			 "transform-origin": "left top",
			 "-ms-transform-origin": "left top",
			 "-moz-transform-origin": "left top",
			 "-webkit-transform-origin": "left top",
			 "-o-transform-origin": "left top",
			 "transform": "scale("+_widthProportion+")",
			 "-ms-transform": "scale("+_widthProportion+")",
			 "-moz-transform": "scale("+_widthProportion+")",
			 "-webkit-transform": "scale("+_widthProportion+")",
			 "-o-transform": "scale("+_widthProportion+")"
		 });
	 });
}