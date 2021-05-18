/*公众帐号管理-列表页面*/
//搜索
function doSearch(){
	 var form = $("form[name='pageForm']");
	 var url = pathUrl+"/weix_acctManager.htm?lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
   $("#pageForm").attr("action",url);
   document.forms['pageForm'].elements["pageIndex"].value =1;	// 回到第一页
   document.forms['pageForm'].submit();
}

//--添加公众帐号弹出框-start--
function showAddAccountTmp()
{
  var src = pathUrl+"/weix_acctManager.htm?method=doAdd&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&time="+ new Date().getTime();
  var aboutConfig={
		content:getIframe(src,690,370,'addFrame'),
	  	id:'add-Public-Account',
        title: getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_22"),
        lock: true,
        opacity: 0.5,
        ok:function(){
	  		var iframe = $("#addFrame")[0].contentWindow;
			if(!iframe.document.body){
				alert(getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_23"));
	        	return false;
	        };
	  		$("#addFrame").contents().find("#subBut").click();
	        //iframe.document.getElementById('subBut').click();
	        return false;
  	    },
        cancel:true
    };
  dlog = art.dialog(aboutConfig);
}
function getIframe(src,width,height,frameid)
{
	return '<iframe id="'+frameid+'" src = "'+src+'" frameborder="0" allowtransparency="true" style="width: '
		+width+'px; height: '+height+'px; border: 0px none;"></iframe>';
}
//--添加公众帐号弹出框-end--

//打开微信公告提示
function openWeiTips(txt){
	window.parent.parent.wei_tips_dialog(txt);
}

//--编辑公众帐号弹出框-start--
function showEditAcctTmp(tmId)
{
  var src = pathUrl+"/weix_acctManager.htm?method=doEdit&tmId="+tmId+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
  var aboutConfig={
		content:getIframe(src,690,370,'artFrame'),		  
	  	id:'edit-Public-Account',
        title: getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_24"),
        lock: true,
        background: "#000",
        opacity: 0.5,
//        width:625,
//        okVal:getJsLocaleMessage("common","common_btn_7"),
//        cancelVal:getJsLocaleMessage("common","common_btn_16"),
//        height:450,
        ok:function(){
	  	var iframe = $("#artFrame")[0].contentWindow;
		if(!iframe.document.body){
			alert(getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_23"))
		      	return false;
		      };
	  		$("#artFrame").contents().find("#subBut").click();
	       return false;
	     },
    	cancel:true
    };
  dlog = art.dialog(aboutConfig);
}

//--编辑公众帐号弹出框-end--
	   
$(function(){

	//列表页面需要执行的
	getLoginInfo("#hiddenValueDiv");
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
	
	//分页
	initPage(currentTotalPage,currentPageIndex,currentPageSize,currentTotalRec);
	//复制效果
	$('.copy').zclip({
	path: $("#iPathUrl").val() + '/js/ZeroClipboard.swf',
	copy:function(){
		var _this=this;
		return $(_this).prev('p').find("span").text();
	},
	afterCopy: function(){
		alert($(this).prev('p').find("span").text() + getJsLocaleMessage("wxgl","wxgl_qywx_zhgl_text_25"));
	}
	});

	//复制按钮的显示与隐藏
	$('.getUrl,.getToken').hover(function(){
		$(this).find('.zcopy').addClass('blueColor');
		$(this).find('.sd_popcard').removeClass('visible_hidden');
	},function(){
		$(this).find('.zcopy').removeClass('blueColor');
		$(this).find('.sd_popcard').addClass('visible_hidden');
	});
	
	$('#search').click(function(){
        var flag=true;
        var title=$("#title").val();
        if(title=="%" || title=="_" || title=="%_" || title=="_%"){
           // alert("%和_必须包括别的字符");
            alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_27"));
            flag=false;
        }
        if(flag){
            submitForm();
        }
	});
});
	
