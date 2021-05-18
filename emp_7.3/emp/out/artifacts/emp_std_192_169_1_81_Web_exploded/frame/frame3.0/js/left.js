	$(document).ready(function() {
		$(".mune_show > ul > li > a").click(function() {
			$(".higehLisght").removeClass("higehLisght");
			$(this).addClass("higehLisght");
		});
		var lastShowMn = null;
		var span_bg = null;
		$(".mune_show").each(

		function(index) {
			$(this).click(

			function() {
				var ele = $(this);
				if (ele.children(".mune_hidden").is(':visible')) return false;
				if (lastShowMn != null) {
					lastShowMn.css("display", "none");
				}
				lastShowMn = ele.children(".mune_hidden");
				span_bg = ele;
				lastShowMn.show();
				$(".open").removeClass("open");
				$(this).removeClass("li_a_hover");
				$(this).addClass("open");
			});
		});
		$(".mune_show > ul > li > a").hover(function() {
			$(this).addClass("li_a_hover");
		}, function() {
			$(this).removeClass("li_a_hover");
		});
		$(".mune_show").hover(function() {
			if ($(this).children(".mune_hidden").is(":hidden")) {
				$(this).addClass("li_a_hover");
			}
		}, function() {
			$(this).removeClass("li_a_hover");
		});
		getLoginInfo(".logininfo");
			//如果未打开标签页
			if($(window.parent.document).find("#topLink .cursel").length<1)
			{
				if("null"!=openMenuCode){
					var openMenu =openMenuCode;
					showTabMenClk(openMenu);
					$("a[id='ak"+openMenu+"']").trigger("click");
				}else{
					$("#mod"+selPriMenus).addClass("block");
					$(".mune_show:visible > ul > li > a").eq(0).trigger("click");
				}
				
			}else
			{
				//将当前的菜单显示为对应的标签
				showTabMenClk($(window.parent.document).find("#topLink .cursel").attr("menuCode"));
			}
	})

	function getLoginInfo(ids) {
		var $pa = $(window.parent.parent.document);
		var pahtm = $pa.find("#loginparams").html();
		$(ids).append(pahtm);
	}

	function showMod(selMod) {
		$(".block").removeClass("block");
		$("#mod" + selMod).addClass("block");
		$("#mod" + selMod).find(".mune_show").eq(0).trigger("click");
	}

	function showTabMenClk(menucode) {
		$(".block").removeClass("block");
		$(".higehLisght").removeClass("higehLisght");
		$("#ak" + menucode).addClass("higehLisght");
		$("#ak" + menucode).parents(".mune_show").trigger("click");
		$("#ak" + menucode).parents(".mune_show").trigger("click");
		$("#ak" + menucode).parents("table").addClass("block");
	}

	function doOpen(url, menucode, menuname) {
		try {
			//记录打开子菜单
			var value=$('#menuInfo', window.parent.parent.document).html();
			var newValue=value+"#"+url+","+menucode+","+menuname;
			$('#menuInfo', window.parent.parent.document).html(newValue);
			
			$(this).css("background-color", "#FFFFFF");
			if (url.indexOf("http://") > 0) {
				url = url.substr(1);
			} else if (url.indexOf(".jsp") > 0) {
				var menuStr = empRoot;
				var menuCode = "," + url.substring(url.indexOf("/") + 1, url.indexOf("_"));
				var modeUrl = menuStr.substr(menuStr.indexOf(menuCode) + menuCode.length);
				modeUrl = modeUrl.substring(1, modeUrl.indexOf(","));
				url = path + modeUrl + url;
			} else {
				url = path + url;
			}
			var conditionUrl = "";
			if (url.indexOf("?") > -1) {
				conditionUrl = "&";
			} else {
				conditionUrl = "?";
			}
			var $eles = $(".logininfo").find("input");
			if($eles.size()==0){return;}
			$eles.each(function() {
				conditionUrl = conditionUrl + $(this).attr("id") + "=" + $(this).val() + "&";
			});
			conditionUrl = conditionUrl + "timee=" + new Date().getTime();
			window.parent.addTabMenu(url + conditionUrl, menucode, menuname);

		} catch (error) {}
	}
    //提供给前端的方法
    function addShotCutTem(tmId,tmName){
        var name = tmName;
        var skin = $("#skin").val();
        tmName = encodeURIComponent(tmName);
        if(skin.indexOf("frame4.0") != -1||skin.indexOf("frame2.5") != -1){
            if(skin.indexOf("frame2.5") != -1){
                var oneMenu = $(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#sider");
                var secondMenu = oneMenu.children('ul').eq(0).children('li').eq(0).children('span');
                if(secondMenu.text() != getJsLocaleMessage("common","common_my_shortcut")){
                    var liprependTo = "<li class=\"mune_show\"><span>"+getJsLocaleMessage("common","common_my_shortcut")+"</span><ul class=\"mune_hidden div_bd\">"+
                        "<li  title='"+name+"' class=\" \" onmouseover=\"mouseOver1('"+tmId+"')\" onmouseout=\"mouseOut1('"+tmId+"')\"><a id='ak"+tmId+"' class=\" \" onmouseover=\"mouseOver('"+tmId+"')\" onmouseout=\"mouseOut('"+tmId+"')\" onclick=\"doOpen('/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmId+"','"+tmId+"','"+name+"')\">"+name+
                        "<input type=\"button\" id=\"closeLi"+tmId+"\" value=\"x\" style=\"border: 0;width: 20px;height:30px;position: absolute;left: 170px;cursor: pointer;background-color: #f1f1f9;display:none;\" onclick=\"deleteLi('"+tmId+"','"+name+"');\"></a></li></ul></li>";
                    //oneMenu.children('ul').eq(0).prepend(liprependTo);
                    oneMenu.children('ul').eq(0).prepend(liprependTo);
                }else{
                    var liprepend = "<li title='"+name+"' class=\" \" onmouseover=\"mouseOver1('"+tmId+"')\" onmouseout=\"mouseOut1('"+tmId+"')\"><a id='ak"+tmId+"' class=\" \"  onmouseover=\"mouseOver('"+tmId+"')\" onmouseout=\"mouseOut('"+tmId+"')\" onclick=\"doOpen('/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmId+"','"+tmId+"','"+name+"')\">"+name+
                        "<input type=\"button\" id=\"closeLi"+tmId+"\" value=\"x\" style=\"border: 0;width: 20px;height:30px;position: absolute;left: 170px;cursor: pointer;background-color: #f1f1f9;display:none;\" onclick=\"deleteLi('"+tmId+"','"+name+"');\"></a></li>";
                    //oneMenu.children('ul').eq(0).children('li').eq(0).children('ul').eq(0).prepend(liprepend);
                    oneMenu.children('ul').eq(0).children('li').eq(0).children('ul').eq(0).append(liprepend);
                }
            }else{
                var oneMenu = $(window.parent.document).find("#leftIframe").contents().find("#mod25");
                var secondMenu = oneMenu.children('ul').eq(0).children('li').eq(0).children('p');
                if(secondMenu.text() != getJsLocaleMessage("common","common_my_shortcut")){
                    if("zh_HK" == getJsLocaleMessage("common","common_empLangName")){
                        var width=oneMenu.children('ul').eq(0).width()+195;
                    } else {
                        var width=oneMenu.children('ul').eq(0).width()+165;
                    }
                    oneMenu.children('ul').eq(0).css({"width":width});
                    var liprependTo = "<li><p class=\"second-nav-title\">"+getJsLocaleMessage("common","common_my_shortcut")+"</p><ul class=\"third-nav-menu\">"+
                        "<li onmouseover=\"mouseOver('"+tmId+"')\" onmouseout=\"mouseOut('"+tmId+"')\" title='"+name+"' onclick=\"doOpen('/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmId+"','"+tmId+"','"+name+"')\"><a id='ak"+tmId+"'>"+name+"</a>"+
                        "<input type=\"button\" id=\"closeLi"+tmId+"\" value=\"x\" style=\"border: 0;width: 20px;height:20px;position: absolute;left: 130px;cursor: pointer;background-color: #f1f1f9;display:none;\" onclick=\"deleteLi('"+tmId+"','"+name+"');\"></li></ul></li>";
                    //oneMenu.children('ul').eq(0).prepend(liprependTo);
                    oneMenu.children('ul').eq(0).prepend(liprependTo);
                }else{
                    var liprepend = "<li  onmouseover=\"mouseOver('"+tmId+"')\" onmouseout=\"mouseOut('"+tmId+"')\" title='"+name+"' onclick=\"doOpen('/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmId+"','"+tmId+"','"+name+"')\"><a id='ak"+tmId+"'>"+name+"</a>"+
                        "<input type=\"button\" id=\"closeLi"+tmId+"\" value=\"x\" style=\"border: 0;width: 20px;height:20px;position: absolute;left: 130px;cursor: pointer;background-color: #f1f1f9;display:none;\" onclick=\"deleteLi('"+tmId+"','"+name+"');\"></li>";
                    //oneMenu.children('ul').eq(0).children('li').eq(0).children('ul').eq(0).prepend(liprepend);
                    oneMenu.children('ul').eq(0).children('li').eq(0).children('ul').eq(0).append(liprepend);
                }
            }
        }else{
            window.parent.document.getElementById('leftIframe').contentWindow.location.reload(true);
        }
        window.location.reload();

    }
    function delShotCutTem(tmId){
        var skin = $("#skin").val();
        if(skin.indexOf("frame4.0") != -1){
            var _secondMenu =$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tmId).parent().parent();
            var len = _secondMenu.children('li').length;
            if(len<2){
                var width = _secondMenu.parent().parent().width()-164;
                _secondMenu.parent().parent().css({"width":width});
                _secondMenu.parent().remove();
            }else{
                $(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tmId).parent().remove();
            }
        }else if(skin.indexOf("frame2.5") != -1){
            var _secondMenu =$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tmId).parent().parent();
            var len = _secondMenu.children('li').length;
            if(len<2){
                _secondMenu.parent().remove();
            }else{
                $(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tmId).parent().remove();
            }
        }else{
            $(window.parent.document).find("#m"+tmId).remove();
            window.parent.document.getElementById('leftIframe').contentWindow.location.reload(true);
        }
        // window.location.reload();
    }

    //立即发送
    function rmsSend(tmid,usecount){
        var path = $("#pathUrl").val();
        usecount = parseInt(usecount) + 1; //点击立即使用就增加1次
        $.post(path+"/rms_commTpl.htm?method=updateUseCount",{tmid:tmid,usecount:usecount},function(result){
            if(result != null && result == "success")
            {
                window.parent.openNewTab("5100-1000",path+ "/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmid);
                //window.location.href = path+ "/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmid;
            }
            else
            {
                alert(getJsLocaleMessage("ydcx","rms_myscene_runexception"));
            }
        });

    }
    //模板分享
    var pathUrl = $("#pathUrl").val();
    function shareTem(tmId, tmName) {
        var lguserid = $("#lguserid").val();
        var lgcorpcode = $("#lgcorpcode").val();
        tmName = tmName || '-';
        $.ajax({
            type: "GET",
            url: pathUrl + "/meditor/getTempById",
            data: {tmId: tmId},
            dataType: "json",
            success: function (result) {
                var template = result.data;
                var url = pathUrl + "/rms/template/tem_shareTemplate.jsp?lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode + "&tempId=" +template.tmid + "&userId=" +template.userId + "&tmCode=" + template.corpCode+ "&spTempId=" + template.sptemplid;
                window.parent.document.getElementById("flowFrame").setAttribute("src",url);
                window.parent.document.getElementById("flowFrame").setAttribute("attrid",tmId);
                window.parent.document.getElementById("flowFrame").setAttribute("tmpname",tmName);
                window.parent.document.getElementById("flowFrame").setAttribute("style", 'display:  !important');
                parent.$("#shareTmpDiv").dialog("open");
            }
        });
    }

	//跳转富信历史查询
	function turnHistoryTask(tmid){
		window.parent.openNewTab("5100-1400",path+ "/rmsTaskRecord.htm?method=find");
	}

	// 查询此模板是否设置定时任务
	function isScheduledTask(tmid){
		var isExit = false;
		$.ajax({
			type: 'POST',
			url: 'rms_rmsTaskHistory.htm?method=isScheduledTask',
			data: {tmid : tmid},
			async: false,
			success: function (result) {
				isExit = result;
			}
		});
		return isExit;
	}