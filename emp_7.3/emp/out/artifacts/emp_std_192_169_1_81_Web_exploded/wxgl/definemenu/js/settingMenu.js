var curname = "";
$(document).ready(function()
{
    $('#aid').isSearchSelect( {
        'width' : '262',
        'select_height' : '24',
        'isInput' : false,
        'zindex' : 0
    }, function(o)
    {
        showMenu();
    });
    showMenu();
    getLoginInfo("#logininfos");
    getLoginInfo("#hiddenValueDiv");
    // 添加一级菜单
    $("#add_nav_btn").click(function()
    {
        var aid = $("#aid").val();
        if (aid==null||aid == "")
        {
            alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_22"));
            return;
        }
        var size = $(".lev1menu").length;
        if (size >= 3)
        {
            alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_23"));
            return;
        }
        var lgcorpcode = $("#lgcorpcode").val();
        var mname = getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_24");
        var pid = 0;
        $.post("weix_defineMenu.htm", {
            method : "addMenu",
            size : size,
            mname : mname,
            pid : pid,
            lgcorpcode : lgcorpcode,
            aid : aid,
            isAsync : "yes"
        }, function(result)
        {
            /*if (result == "overlength")
            {
                alert("名称长度超过限制，不多于4个汉字或8个字母！");
                return;
            }*/
            if (result == "outOfLogin")
            {
                window.parent.showLogin(0);
                return;
            }
            if (result.indexOf("true") == 0)
            {
                pid = result.substr(4);
                appendNewTab(mname, size, pid, "", "", 0, true);
                $("#bizmenu > .lev1menu").eq(size).find(".edit_icon").trigger("click");
                $("#l1menuname").val("");
            } else
            {
                alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_25"));
            }
        });
    });

    // 添加一级菜单->"确认"
    $("#subButL1").click(
        function()
        {
            var size = $(".lev1menu").length;
            var mname = $.trim($(this).parents("#addMenu").find(
                    "#l1menuname").val());
            $("#l1menuname").val(mname);
            if (mname == "")
            {
                alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_26"));
                return;
            }
            var pid = 0;
            var aid = $("#aid").val();
            // 按钮不可点
            $("#subButL1").attr("disabled", true);
            
        });

        // 添加子菜单->"确认"
        $("#subButL2").click(function()
        {
            addL2Menu(this);
        });

        // 修改菜单名称
        $("#updateBtn").click(function()
        {
            updateMenuName(this);
        });

        // 返回
        $('#backgo').click(function()
        {
            $('#default_show').show().siblings().hide();
            $(this).hide();
        })

    });

function bindNavItem($ob)
{
    // 经过变色
    $ob.hover(function()
    {
        $(this).addClass('c_hoverBg');
    }, function()
    {
        $(this).removeClass('c_hoverBg');
    });
    $ob.click(function()
    {
        $(".c_selectedBg").removeClass("c_selectedBg");
        $ob.addClass("c_selectedBg");
        menuClick(this);
    });

}
function bindAction($ob)
{
    // 删除按钮
    $ob.find('.del_icon').click(function()
    {
        var $item = $(this).parents(".nav_item");
        
        art.dialog({
        	title:getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_68"),
            content: getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_27"),
            ok: function () {
            var mid = $item.attr("mid");
            $.post("weix_defineMenu.htm", {
                mid : mid,
                method : "delMenu"
            }, function(result)
            {
                if (result == "nomenu")
                {
                    alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_28"));
                    return;
                }
                if (result == "true")
                {
                    alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_29"));
                    $item.parent().remove();
                } else
                {
                    alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_23"));
                }
            });
                return true;
            },
            cancelVal: getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_30"),
            cancel: true //为true等价于function(){}    
        });

    });
    // 添加子菜单
    $ob.find('.add_icon').click(function()
    {
        // 添加子菜单前
            var p = $(this).parents(".nav_item");
            var pid = p.attr('mid');
            if (p.attr("pid") == 0 && p.attr("mtype") != 0)
            {
                art.dialog({
                    content: getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_31"),
                    ok: function () {
                        addl2Menu(pid);
                        p.attr("mtype","0");
                        return true;
                    },
                    cancelVal:getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_30") ,
                    cancel: true //为true等价于function(){}    
                });
            }else
            {
                addl2Menu(pid);
            }
        });
   function addl2Menu(pid)
   {
       var size = $(".lev2menu[pid=" + pid + "] > .l2son").length;
       if (size >= 5)
       {
           alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_32"));
           return;
       }
       
       var size = $(".lev2menu[pid=" + pid + "] > .l2son").length;
       var mname = getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_33")+(size+1);

       var lgcorpcode = $("#lgcorpcode").val();
       var aid = $("#aid").val();
       // 按钮不可点
       $("#subButL2").attr("disabled", true);
       $.post("weix_defineMenu.htm", {
           method : "addMenu",
           size : size,
           mname : mname,
           pid : pid,
           lgcorpcode : lgcorpcode,
           aid : aid
       }, function(result)
       {
           if (result.indexOf("true") == 0)
           {
               //alert("添加成功！");
               var p = $(".level1[mid=" + mid + "]");
               p.attr('tid', '');
               p.attr('mtype', 0);
               p.attr('murl', '');
               var mid = result.substr(4);
               appendNewL2Tab(mname, size, pid, mid, "", "", 0, true);
               $(".lev2menu[pid=" + pid + "]").find(".nav_item")
                   .eq(size).find(".edit_icon").trigger("click");
           } else
           {
               alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_34"));
           }
       });
   }
    // 改名
    $ob.find('.edit_icon').click(
            function()
            {
                var $nameo = $(this).parent().parent().parent().next();
                var $nav_item = $(this).parents('.nav_item');

                var pid = $nav_item.attr('pid');
                var mid = $nav_item.attr('mid');
                var nameh = $nameo.html();
                var namet = $nameo.find("a").text();
                var options = {};
                $nameo.children().hide();
                if (pid == 0 || pid == "0")
                {
                    $nameo.append("<input class='rname' value='" + namet
                            + "' maxlength='8' title='" + getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_35") + "'/>");
                } else
                {
                    $nameo.append("<input class='rname' value='" + namet
                            + "' maxlength='16' title='" + getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_36") + "'/>");
                }

                $nameo.find("input").select().click(function(e)
                {
                    e.stopPropagation();
                }).blur(function(e)
                {
                    e.stopPropagation();
                    var text = $.trim($(this).val());
                    if (text != "" && namet != text)
                    {
                        options = {
                            mid : mid,
                            mname : text,
                            method : "updateMenuName",
                            pid : pid,
                            isAsync : "yes"
                        };
                        $.post("weix_defineMenu.htm", options, function(result)
                        {
                            $("#updateBtn").attr("disabled", false);
                            if (result == "outOfLogin")
                            {
                                window.parent.showLogin(0);
                                return;
                            }
                            if (result == "overlength")
                            {
                                if (pid == 0)
                                {
                                	alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_37"));
                                } else
                                {
                                	alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_38"));
                                }
                                $nameo.html(nameh);
                                return;
                            }
                            if (result == "true")
                            {
                            	$nameo.find("a").text(text).attr("title",text).show().siblings().remove();
                                alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_39"));
                            } else
                            {
                            	alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_40"));
                            	$nameo.html(nameh);
                            }
                        });
                    } else
                    {
                        $nameo.html(nameh);
                    }
                });

            });
	
    // 上移
    $ob.find('.arrow_up').click(function()
    {
        uporder(this);
    });

    // 下移
    $ob.find('.arrow_down').click(function()
    {
        downorder(this);
    });
}
// 菜单操作-添加一级级菜单
function appendNewTab(mname, index, mid, tid, url, type, mkey, isclick)
{
    mname = escapeString(mname);
    var newmnue = "<div class='nav_item_wrapper lev1menu' id='level0_1'>"
            + $("#level1menu").html()
            + "<div class='sub_nav_list  lev2menu ui-sortable ui-sortable-disabled' pid='"
            + mid + "'></div>" + "</div>";
    $("#bizmenu").append(newmnue);
    $("#bizmenu > .lev1menu").eq(index).find("h4 > a").attr("title", mname);
    $("#bizmenu > .lev1menu").eq(index).find("h4 > a").html(mname);
    // $(".c_selectedBg").removeClass("c_selectedBg");
    // $("#bizmenu >
    // .lev1menu").eq(index).find(".nav_item").addClass("c_selectedBg");
    $("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("mid", mid);
    $("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("pid", "0");
    $("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("tid", tid);
    $("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("murl", url);
    $("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("mtype", type);
    $("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("mkey", mkey);
    bindAction($("#bizmenu > .lev1menu").eq(index));
    bindNavItem($("#bizmenu > .lev1menu").eq(index).find(".nav_item"));
    if (isclick)
    {
        $("#bizmenu > .lev1menu").eq(index).find(".nav_item").trigger("click");
    }
}
// 菜单操作-添加二级菜单
function appendNewL2Tab(mname, index, pid, mid, tid, url, type, mkey, isclick)
{
    mname = escapeString(mname);
    $(".lev2menu[pid=" + pid + "]").append($("#level2menu").html());
    $(".lev2menu[pid=" + pid + "]").find("h5 > a").eq(index).attr("title",
            mname);
    $(".lev2menu[pid=" + pid + "]").find("h5 > a").eq(index).html(mname);
    // $(".c_selectedBg").removeClass("c_selectedBg");
    // $(".lev2menu[pid="+pid+"]").find(".nav_item").eq(index).addClass("c_selectedBg");
    $(".lev2menu[pid=" + pid + "]").find(".nav_item").eq(index)
            .attr("mid", mid);
    $(".lev2menu[pid=" + pid + "]").find(".nav_item").eq(index)
            .attr("pid", pid);
    $(".lev2menu[pid=" + pid + "]").find(".nav_item").eq(index)
            .attr("tid", tid);
    $(".lev2menu[pid=" + pid + "]").find(".nav_item").eq(index).attr("murl",
            url);
    $(".lev2menu[pid=" + pid + "]").find(".nav_item").eq(index).attr("mtype",
            type);
    $(".lev2menu[pid=" + pid + "]").find(".nav_item").eq(index).attr("mkey",
            mkey);
    bindAction($(".lev2menu[pid=" + pid + "]").find(".nav_item").eq(index));
    bindNavItem($(".lev2menu[pid=" + pid + "]").find(".nav_item").eq(index));
    if (isclick)
    {
        $(".lev2menu[pid=" + pid + "]").find(".nav_item").eq(index).trigger(
                "click");
    }
}

// 菜单操作-通过公众帐号查找对应菜单
function showMenu()
{
    $('.default_content').show().siblings().hide();
    var aid = $("#aid").val();
    $("#bizmenu").html("");

    if (aid==null||aid == "")
    {
        $(".default_content").html(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_41"));
        $("#appid").val("");
        $("#secret").val("");
        return;
    }
    $(".default_content").html(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_42"));
    // 显示loading
    $("#pos_load").show();
    $.post("weix_defineMenu.htm", {
        method : "getMenuByAId",
        aid : aid,
        isAsync : "yes"
    }, function(result)
    {
        // 隐藏loading
            $("#pos_load").hide();
            if (result == "@")
            {
                $("#bizmenu").html("");
                return;
            }
            var indexx = result.indexOf("@");

            var appStr = "";
            if (indexx > 0)
            {
                appStr = result.substr(0, indexx);
            }
            if (appStr != "" && appStr.indexOf("&") >= 0)
            {
                var appArr = appStr.split("&");
                $("#appid").val(appArr[0]);
                if (appArr.length > 1)
                {
                    $("#secret").val(appArr[1]);
                }
            } else
            {
                $("#appid").val("");
                $("#secret").val("");
            }
            result = result.substr(indexx + 1);
            var array = eval("(" + result + ")");
            if (array.length == 0)
            {
                $("#bizmenu").html("");
                return;
            }
            for ( var i = 0; i < array.length; i = i + 1)
            {
                var menuArray = array[i];
                for ( var j = 0; j < menuArray.length; j = j + 1)
                {
                    var menuJson = menuArray[j];
                    if (menuJson.pid == 0)
                    {
                        appendNewTab(menuJson.mname, i, menuJson.mid,
                                menuJson.tid, menuJson.murl, menuJson.mtype,
                                menuJson.mkey, false);
                    } else
                    {
                        appendNewL2Tab(menuJson.mname, j - 1, menuJson.pid,
                                menuJson.mid, menuJson.tid, menuJson.murl,
                                menuJson.mtype, menuJson.mkey, false);
                    }
                }
            }
            //setTimeout("window.parent.reinitHeight()", 100);
        });
}

// 菜单操作-更新菜单名称
function updateMenuName(obj)
{
    var pid = $item.attr("pid");
    var mid = $(obj).parents("#edit_sub_Menu").find("#updateMid").val();
    var updateMname = $(obj).parents("#edit_sub_Menu").find("#updateMname")
            .val();
    var mname = $.trim(updateMname);
    $(obj).parents("#edit_sub_Menu").find("#updateMname").val(mname);
    if (mname == curname)
    {
        alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_43"));
        return;
    }
    if (mname == "")
    {
        alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_44"));
        return;
    }
    // 按钮不可点
    $("#updateBtn").attr("disabled", true);
    $.post("weix_defineMenu.htm", {
        mid : mid,
        mname : mname,
        method : "updateMenuName",
        pid : pid,
        isAsync : "yes"
    }, function(result)
    {
        $("#updateBtn").attr("disabled", false);
        if (result == "outOfLogin")
        {
            window.parent.showLogin(0);
            return;
        }
        if (result == "overlength")
        {
            if (pid == 0)
            {
                alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_45"));
            } else
            {
                alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_46"));
            }
            return;
        }
        if (result == "true")
        {
            alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_47"));
            var $item = $(".nav_item[mid=" + mid + "]");
            if (pid == 0)
            {
                $item.find("h4 > a").attr("title", mname);
                $item.find("h4 > a").text(mname);
            } else
            {
                $item.find("h5 > a").attr("title", mname);
                $item.find("h5 > a").text(mname);
            }
        } else
        {
            alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_48"));
        }
        closeDialog();
    });
}

function secapp(obj, reg)
{
    var val = $(obj).val();
    if (reg.test(val))
    {
        $(obj).val($(obj).val().replace(reg, ''));

    }
}

// 菜单操作-发布菜单
function publishMenu(path)
{
    var aid = $.trim($("#aid").val());
    $('#subBut1').attr("disabled", true);
    // 公众帐号
    if (aid == null || aid == "")
    {
        alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_49"));
        $('#subBut1').attr("disabled", false);
        return;
    }

    $.post(path + "/weix_defineMenu.htm", {
        method : "release",
        aid : aid,
        isAsync : "yes"
    }, function(message)
    {
    	//console.log("message" + message);
        if (message == "outOfLogin")
        {
            window.parent.showLogin(0);
            return;
        }
        returnResult(message);
        $('#subBut1').attr("disabled", false);
    });
}
    // 菜单操作-上移
    function uporder(obj)
    {
        var cur = $(obj).parents(".nav_item");
        var up = cur.parent().prev().children(".nav_item");
        $.post("weix_defineMenu.htm?method=updateOrder", {
            mid1 : cur.attr("mid"),
            mid2 : up.attr("mid"),
            order : '1',
            isAsync : "yes"
        }, function(data, textStatus)
        {
            if (data == "outOfLogin")
            {
                window.parent.showLogin(0);
                return;
            }
            if (data.indexOf('@') > -1)
            {
                var onthis = $(obj).closest('div.nav_item_wrapper');
                var getup = onthis.prev();
                $(getup).before(onthis);
            }
            if (data.indexOf('#') > -1)
            {
                alert(data.replace('#', ''));
            }

        });
    }

    // 菜单操作-下移
    function downorder(obj)
    {
        var cur = $(obj).parents(".nav_item");
        var down = cur.parent().next().children(".nav_item");
        $.post("weix_defineMenu.htm?method=updateOrder", {
            mid1 : cur.attr("mid"),
            mid2 : down.attr("mid"),
            order : '0',
            isAsync : "yes"
        }, function(data, textStatus)
        {
            if (data == "outOfLogin")
            {
                window.parent.showLogin(0);
                return;
            }
            if (data.indexOf('@') > -1)
            {
                var onthis = $(obj).closest('div.nav_item_wrapper');
                var getup = onthis.next();
                $(getup).after(onthis);
            }
            if (data.indexOf('#') > -1)
            {
                alert(data.replace('#', ''));
            }
        });
    }

