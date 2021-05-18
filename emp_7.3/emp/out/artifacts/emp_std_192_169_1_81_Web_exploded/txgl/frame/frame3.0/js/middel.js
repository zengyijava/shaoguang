function switchSysBar($tdObj)
{ 
	$("#frmTitle").toggle();
	$tdObj.toggleClass("mid_toggle_X");
/*	var ssrc=$tdObj.find("span img ").attr("src");
	if(ssrc=="images/main_55.gif")
	{
		//document.all("img1").src="images/main_55_1.gif";
		document.all("frmTitle").style.display="none" ;
	} 
	else
	{ 
		//document.all("img1").src="images/main_55.gif";
		document.all("frmTitle").style.display="" ;
	} */
}
//导航标签个数大于等于1
var tabCount = 9;
$(document).ready(function(){
	bindTab();
	$("#middle_toggle").hover(function(){
		$(this).find(" > div > div").toggle();
		$(this).toggleClass("middle_toggle");
	},function(){
		$(this).find(" > div > div").toggle();
		$(this).toggleClass("middle_toggle");
	});
	
	var bodywidth = $("body").width();
	tabCount = Math.floor((bodywidth - 210)/112);
	//topLink('<%=path%>');
	//$('#topLink img').hover(function(){$(this).attr('src',$(this).attr('src').replace('.gif','_hover.gif'));},
			//function(){$(this).attr('src',$(this).attr('src').replace('_hover.gif','.gif'))});
});

var imcode = "1000-1400";
function addTabMenu(url,menucode,menuname)
{
	//如果在下拉的菜单中找到
	if($("#tb_child #h"+menucode).html()!= null)
	{
		removeTab();
		addTab(url,menucode,menuname);
		$("#tb_child #h"+menucode).remove();
		doSkin(menucode);
	}else
	//判断是否打开了该菜单
	if($("#topLink #m"+menucode).html()==null)
	{
		if($("#topLink .topMenu").length==(tabCount+1))
		{
			removeTab();
		}
		addTab(url,menucode,menuname);
		//parent.window.frames["cont"+menucode].location.href =;
	}else
	{
		//判断选中的菜单是否为要打开的菜单，通过比较ID
		if($("#topLink .cursel").attr("id") != "m"+menucode)
		{
			showIframe(menucode);
		}
	}
}
//标签点击事件
function showIframe(menucode)
{
	if($("#topLink .cursel").attr("id") != "m"+menucode)
	{
		//判断webIm
		if(imcode != menucode)
		{
			HideWebIM();
		}else
		{
			ShowWebIM();
		}
		$("#menuFrame iframe").each(function(){$(this).css("display","none");});
		$("#cont"+menucode).css("display","block");
		$("#topLink .cursel").removeClass("cursel");
		$("#topLink #m"+menucode).addClass("cursel");
		
		$("#leftIframe")[0].contentWindow.showTabMenClk(menucode);
		doSkin(menucode);
	}
	//经典皮肤下，监控视图做特殊处理（重新刷一下页面）
	if(menucode=="2800-1300")
	{
		var srcUrl = $("#menuFrame").find("#cont"+menucode).attr("src");
		$("#menuFrame").find("#cont"+menucode).attr("src",srcUrl);
	}
}

function addTab(url,menucode,menuname)
{
	//关闭标签的html
	var aht = "<a class='closeA' href='javascript:closeMenu(\""+menucode+"\")'>×</a>";
	//标签页
	var divht = "<div class='topMenu cursel' onmouseover='menuhover($(this))' "
		+" onmouseout='menuhover($(this))'  id='m"+menucode
		+ "' menucode='"+menucode+"'><span onclick='showIframe(\""+menucode+"\")' title='"+
		menuname+"'  class='menuText'>"+menuname+"</span>"+aht+"</div>";
	$("#topLink .cursel").removeClass("cursel");
	$("#topLink .topMenu:last").before(divht);

	//隐藏webIm的层
	if(imcode != menucode)
	{
		HideWebIM();
	}else
	{
		ShowWebIM();
	}
	//判断是否需要添加iframe
	if(url != null)
	{
		var framehtml = "<iframe name='cont"+menucode+"'  id='cont"+menucode+"'"+
		" height='100%' width='100%' border='0' frameborder='0' src='"+ url + "' > </iframe>";
		//隐藏其他iframe
		$("#menuFrame iframe").each(function(){$(this).css("display","none");});
		// 此处可考虑是否判断是否已经存在该iframe--%>
		//添加新的iframe
		$("#menuFrame").prepend(framehtml);
	}
}
function menuhover($menu)
{
	if(!$menu.hasClass())
	{
		$menu.toggleClass("curhover");
	}
}
//关闭标签事件
function closeMenu(menucode)
{
	var menulen = $("#topLink .topMenu").length;
	if(menulen > 2)
	{
		$("#m"+menucode).remove();
		// 此处可考虑是否判断删除该iframe，或改为隐藏--%>
		$("#cont"+menucode).remove();
		if($("#topLink .cursel").length<1)
		{
			showIframe($("#topLink .topMenu:last").prev().attr("menucode"));
		}
		//隐藏webIm的层
		if(imcode == menucode)
		{
			HideWebIM();
		}
	}
	var hmenulen = $("#tb_child li").length;
	if(menulen < (tabCount+2) && hmenulen > 1)
	{
		var $tab0 = $("#tb_child li:eq(1)");
		var tabText = $tab0.attr("title");
		var tabCode = $tab0.attr("menucode");
		$tab0.remove();

		//关闭标签的html
		var aht = "<a class='closeA' href='javascript:closeMenu(\""+tabCode+"\")'>×</a>";
		var divht = "<div class='topMenu'  id='m"+tabCode+
			"' menucode='"+tabCode+"'><span onclick='showIframe(\""+tabCode+"\")' title='"+
			tabText+"' class='menuText'>" +tabText+"</span>"+aht+"</div>";
		$("#topLink .topMenu:last").before(divht);
	}

}
//关闭全部事件
function closeAllTab()
{
	if($("#topLink .topMenu").length==2)
	{
		return ;
	}
	var $menulast = $("#topLink .topMenu:last");
	var $menusel = $("#topLink .cursel");
	$("#topLink").empty();
	$("#topLink").append($menusel);
	$("#topLink").append($menulast);

	
	var $lilast = $("#tb_child li:first");
	$("#tb_child").empty();
	$("#tb_child").append($lilast);

	var menucode_sel = $menusel.attr("menucode");
	if(imcode != menucode_sel)
	{
		HideWebIM();
	}
	$("#menuFrame iframe").each(function(){
		if($(this).attr("id") != "cont"+menucode_sel)
		{
			$(this).remove();
		}
	});
	//var framesel = $()
	//清除所有iframe
	//$("#menuFrame").empty();
	//var framehtml = "<iframe name='emptyframe'  id='emptyframe' height='100%'"+
	//	" width='100%' border='0' frameborder='0' src='/base/blank.jsp' > </iframe>";
	//添加新的iframe
	//$("#menuFrame").prepend(framesel);
	//$("#leftIframe").contents().find(".higehLisght").removeClass("higehLisght");
	
	//重新绑定下拉事件
	bindTab();
	
	
}

function removeTab()
{
	if($("#topLink .topMenu").length < (tabCount+1))
	{
		return;
	}
	//隐藏webIm的层
	HideWebIM();
	var $tab7 = $(".topMenu:eq("+(tabCount-1)+")");
	var tabText = $tab7.find("> span").attr("title");
	var tabCode = $tab7.attr("menucode");
	$tab7.remove();

	var hidMenu = "<li title='"+tabText+"' menucode='"+tabCode+"' id='h"+tabCode+
		"'><a  href='javascript:showHidMenu(\""+tabCode+"\",\""+tabText+"\")'>"+tabText+"</a></li>";
	$("#tb_child li:eq(0)").after(hidMenu);
	/*$("#tb_child li:eq(1)").hover(
        	function(){
            	$("#ifr").show();
            	$("#tab_hide").show();
            },
        	function(){
            	
                $("#tab_hide").hide();
                $("#ifr").hide();
            }
   	);*/
}
//点击下拉的菜单时
function showHidMenu(menucode,menuname)
{
	removeTab();
	if(imcode == menucode)
	{
		ShowWebIM();
	}
	$("#h"+menucode).remove();
	addTabMenu(null,menucode,menuname);
	$("#menuFrame iframe").each(function(){$(this).css("display","none");});
	$("#cont"+menucode).css("display","block");
	$("#leftIframe")[0].contentWindow.showTabMenClk(menucode);
}

function changeTopMenu(newTabCount)
{
	if(newTabCount > tabCount)
	{
		var tcoun = newTabCount - tabCount;
		var tblength = $("#tb_child li").length;
		if(tblength < 2)
		{
			tabCount = newTabCount;
			return;
		}
		if(tblength-1 < tcoun)
		{
			tcoun = tblength-1;
		}
		for(var i=tcoun;i>0;i=i-1)
		{
			var $tbc =$("#tb_child li").eq(i);
			var menucode = $tbc.attr("menucode");
			var menuname = $tbc.attr("title");
			var aht = "<a class='closeA' href='javascript:closeMenu(\""+menucode+"\")'>×</a>";
			//标签页
			var divht = "<div class='topMenu' onmouseover='menuhover($(this))' "
				+" onmouseout='menuhover($(this))'  id='m"+menucode
				+ "' menucode='"+menucode+"'><span onclick='showIframe(\""+menucode+"\")' title='"+
				menuname+"'  class='menuText'>"+menuname+"</span>"+aht+"</div>";
			$("#topLink .topMenu:last").before(divht);
			$tbc.remove();
		}
	}else if(newTabCount < tabCount)
	{
		
		var tmlength = $("#topLink .topMenu").length;
		var cccount = tmlength - 1 - newTabCount;
		if(cccount > 0)
		{
			
			var isgoon = true;
			for(var i=tmlength-2;cccount>0 && isgoon;i=i-1)
			{
				var $tab7 = $(".topMenu:eq("+(i)+")");
				if(!$tab7.hasClass("cursel"))
				{
					var tabText = $tab7.find("> span").attr("title");
					var tabCode = $tab7.attr("menucode");
					$tab7.remove();
	
					var hidMenu = "<li title='"+tabText+"' menucode='"+tabCode+"' id='h"+tabCode+
						"'><a  href='javascript:showHidMenu(\""+tabCode+"\",\""+tabText+"\")'>"+tabText+"</a></li>";
					$("#tb_child li:eq(0)").after(hidMenu);
					cccount = cccount - 1;
				}
			}
		}
	}
	tabCount = newTabCount;
}

function bindTab()
{
	$(".putdown").hover(
    	function(){
   			if($("#topLink .topMenu").length <= 2)
   			{
   				return;
   			}
    		var top=$(".putdown").offset().top+28;
    		var left=$(".putdown").offset().left-50;
        	$("#hid_ifr").css("top",top+"px");
        	$("#hid_ifr").css("left",left+"px");
        	//$("#uChildren").slideDown("slow");;
        	$("#tab_hide").css("top",top+"px");
        	$("#tab_hide").css("left",left+"px");
        	//$("#uChildren").slideDown("slow");;
        	$("#hid_ifr").css("height",$("#tab_hide").height()+"px");
        	$("#hid_ifr").show();
        	$("#tab_hide").show();
        	//zhezhao();
        	$(this).addClass("downhover");
        },
    	function(){
        	$("#tab_hide").hide();
        	$(this).removeClass("downhover");
        	$("#hid_ifr").hide();
        }
	);
	$("#tab_hide").hover(
        	function(){
        		$("#hid_ifr").show();
        		$("#tab_hide").show();
        		$(".putdown").addClass("downhover");
            },
        	function(){
            	
            	$("#tab_hide").hide();
            	$("#hid_ifr").hide();
               	$(".putdown").removeClass("downhover");
            }
   	);
}



function ResizeIMFrame(width,height,left)
{
	var div = $("#IMDiv");
	div.css("width",width);     
	div.css("height",height);      
	div.css("left",left);
}
function ShowWebIM()
{
//debugger
	var frame = $(window);
	var w = frame.width();
	var h = frame.height();
	w = w - 195;
	h = h - 117; 
	if($("#IMFrame").attr("src") == "")
	{	
		
		var path = $("#basePathUrl").val();
		$("#IMFrame").attr("src",$("#path").val()+"/im_office.htm");
	}
	ResizeIMFrame(w,h,187);
	$("#IMDiv").show();
}
function HideWebIM()
{
	$("#IMDiv").css("width","0").hide();
}
//判断webim是否为打开状态
function imOpening(){
	if($("#IMDiv").css("display")=='block'){
		return true;
	}else{
		return false;
	}
}

function openNewTab(menuCode,url)
{
   	var $lf = $("iframe[name='I1']");
	if($lf.contents().find("a[id='ak"+menuCode+"']").length==0)
   	{
   		alert("没有集成相应模块，无法打开新的标签");
   		return;
   	}
   	$lf[0].contentWindow.showTabMenClk(menuCode);
   
   	$lf.contents().find("a[id='ak"+menuCode+"']").trigger("click");
   	if(url != "")
   	{
   		$("#cont"+menuCode).attr("src",url);
   	}
}

function doSkin(menucode)
{
	var $hcss = $("#cont"+menucode).contents().find("link");
	var skincode = "";
	var forboo = true;
	for(var i = 0;i<$hcss.length && forboo;i=i+1)
	{
		var csshref = $hcss.eq(i).attr("href");
		if(csshref.indexOf("/skin/")>0)
		{
			csshref = csshref.substring(csshref.indexOf("/skin/")+6);
			skincode = csshref.substring(0,csshref.indexOf("/"));
			forboo = false;
		}
	}
	if(skincode != "")
	{
		parent.window.checkMenuSkin(skincode);
	}
}
