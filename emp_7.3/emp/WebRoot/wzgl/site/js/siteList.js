/**
 * 执行查询操作
 * @returns
 */
function doSearch(){
	var form = $("form[name='pageForm']");
	var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?lguserid=" +$("#lguserid").val() + "&lgcorpcode=" + $("#lgcorpcode").val();
	form.attr("action",url);
    document.forms['pageForm'].elements["pageIndex"].value =1;	// 回到第一页
    document.forms['pageForm'].submit();
}

/**
 * 新增微站
 * @return
 */
function doAdd(){
	var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=chooseSiteType&lgcorpcode=" + $("#lgcorpcode").val();
	 window.location.href = url;
}

/**
 * 编辑微站
 */
function doEdit(sId){
	var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=doEdit&lgcorpcode=" + $("#lgcorpcode").val() + "&sId=" + sId;
	window.location.href = url;
}

/**
 * 微站预览
 */
function doPreview(urlToken){
	url = $("#pathUrl").val() + "/wzgl_sitePreview.hts?from=pc&urlToken="+urlToken;
	showbox({src:url,mode:1});
}

//返回上一级
function doreturn()
{
	var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=find&isOperateReturn=true&lguserid="
	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
	window.location.href = url;
}

/**
 * 模板弹出框的预览
 * @param tptype(0:外链,1:微页面,2:表单)
 * @param tpvalue(tptype对应的值)
 * @return
 */
function showLink(tptype,tpvalue){
	//此处判断是为了在手机上也能进行跳转
	if ((navigator.userAgent.match(/(iPhone|iPod|Android|ios|iPad)/i)))
	{
		if (tptype == "0")
		{
			location.replace(tpvalue);
		}
		else if (tptype == "1") 
		{
			var link = $("#pathUrl").val() + "/wzgl_sitePreview.hts?urlToken="+tpvalue;
			location.replace(link);
		}
		else if (tptype == "2") 
		{
			var link = $("#pathUrl").val() + "/wzgl_formManager.hts?method=toAccessForm&formid=" + tpvalue;
			location.replace(link);
		}
	}
	else
	{
		if (tptype == "0")
		{
			$("iframe[id^='msgPreviewFrame']",parent.document.body).attr("src",tpvalue);
		}
		else if (tptype == "1") 
		{
			var link = $("#pathUrl").val() + "/wzgl_sitePreview.hts?urlToken="+tpvalue;
			$("iframe[id^='msgPreviewFrame']",parent.document.body).attr("src",link);
		}
		else if (tptype == "2") 
		{
			var link = $("#pathUrl").val() + "/wzgl_formManager.hts?method=toAccessForm&formid=" + tpvalue;
			$("iframe[id^='msgPreviewFrame']",parent.document.body).attr("src",link);
		}
	}
}

/**
 * 删除微站
 */
function doDelete(sId){
	var queryurl = $("#pathUrl").val() + "/wzgl_siteManager.htm?lgcorpcode="+$('#lgcorpcode').val();
	if(confirm(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_28"))){
		//删除页面请求
		var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=deleteSiteInfo";
		$.post(url,{sId:sId,isAsync:"yes"},function(data){
			if("success"==data){
				alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_29"));
				fresh(queryurl);
		        return false;
			}else{
	    		art.dialog({
	    		    time: 1,
	    		    content: getJsLocaleMessage("wzgl","wzgl_qywx_site_text_30")
	    		});
		        return false;
			}
		});
	}
}

/**
 * 删除成功刷新当前页
 * @param url
 * @return
 */
function fresh(url) {
	var form = $("form[name='pageForm']");
	form.attr("action",url);
//    document.forms['pageForm'].elements["pageIndex"].value =1;	// 回到第一页
    document.forms['pageForm'].submit();
}

/**
 * 微站统计
 */
function doTongji(sId){
	alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_34"));
	return false;
}

$(function(){
	//登录后页面初始值
	getLoginInfo("#hiddenValueDiv");
	//查询条件显示和隐藏
	$("#toggleDiv").toggle(function() {
		$("#condition").hide();
		$(this).addClass("collapse");
	}, function() {
		$("#condition").show();
		$(this).removeClass("collapse");
	});
	$("#content tbody tr").hover(function() {
		$(this).addClass("hoverColor");
	}, function() {
		$(this).removeClass("hoverColor");
	});
});

/**
 * 风格预览
 * @param typeid
 * @return
 */
function viewType()
{
    dlog = art.dialog.through({
		title: getJsLocaleMessage("wzgl","wzgl_qywx_site_text_35"),
	    content: document.getElementById('typeTmpDiv'),
	    id: 'typeTmpDiv',
	    lock: true
    });
}

/**
 * 确认创建微站
 * @return
 */
function nextPage(typeId)
{
	if(confirm(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_36"))){
		var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=createSiteByTemp&lgcorpcode=" + $("#lgcorpcode").val() + "&typeId=" + typeId;
		window.location.href = url;
        return false;
	}
}

