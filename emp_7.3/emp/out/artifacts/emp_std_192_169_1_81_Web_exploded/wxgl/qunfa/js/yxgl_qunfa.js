//点击选择模板
function chooseTemp()
{
	var pathUrl = $("#pathUrl").val();
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var frameSrc = pathUrl+"/weix_defaultReply.htm?method=getTemplates&dsflag=1&lguserid="
		+lguserid+"&lgcorpcode="+lgcorpcode+ "&onlyImgText=1&tempType=23"+"&nogif=1";
	var aboutConfig={
		content:getIframe(frameSrc,780,455,'chooseTemp'),
        title: getJsLocaleMessage("wxgl","wxgl_qywx_wxqf_text_1"),
        lock: true,
        opacity: 0.5,
        width:800,
        height:450,
        ok: function(){
				var iframe = $("#chooseTemp")[0].contentWindow;
				if(!iframe.document.body){
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_13"));
		        	return false;
		        };
		        var form = iframe.document.getElementById('pageForm');
				var checked = $(form).find("input[name='checklist']:checked");
				if(checked&&checked.size()>0){
						//将模板id赋值给隐藏域
						var tid = checked.val();
						var message = checked.next("xmp").text();
						
						$("#tid").val(tid);
						$("#radioDiv2").empty();
						var content = "\""+ message +"\"" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_13")+"<a onclick='javascript:toPreview("+tid+")' style='cursor:pointer' title='" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_14") + "'>" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_15")+ "</a>"
						var html = "<td style='vertical-align: top;text-align: right;'>" + getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_26") + "</td>";
						html = html + "<td colspan='3'>"+content+"</td>";
						$("#radioDiv2").show().html(html);
				}else{
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_wxqf_text_2"));
					return false;
				}
			},
			cancel: true
    };
    art.dialog( aboutConfig);	
    setTimeout(function(){$(".aui_content").css("padding","0");},200);
}
function getIframe(src,width,height,frameid)
{
	return '<iframe id="'+frameid+'" src = "'+src+'" frameborder="0" allowtransparency="true" style="width: '
		+width+'px; height: '+height+'px; border: 0px none;"></iframe>';
}

//浏览效果
function toPreview(tempId){
	url = $("#pathUrl").val() + "/weix_keywordReply.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
	showbox({src:url});
}