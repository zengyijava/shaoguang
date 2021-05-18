
function delPro(prId,prName) { //删除短信模板配置
	//if (confirm("确定删除此步骤吗 ?")) {
	if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdsccbzm"))) {
		$('#prId').val(prId);
		var xmpPrName = $("#xmpPrName_"+prId).text();
		$('#prName').val(xmpPrName);
		$("#form").submit();
	}
}

$(document).ready(function(){
	getLoginInfo("#hiddenValueDiv");
	$('#u_o_c_explain').find('> p').next().hide();
	$('#u_o_c_explain').click(function(){$(this).find('> p').next().toggle();});
	show();
	
	$("#infoDiv").dialog({
			autoOpen: false,
			height:600,
			width: 630,
			resizable:false,
			modal: true,
			open:function(){
				//$(".ui-dialog-titlebar-close").hide();
				//alert("open");
			},
			close:function(){
				$("#flowFrame").attr("src","");
			}
		});
});

function closediv()
{
	$("#infoDiv").dialog("close");
	$("#flowFrame").attr("src","");
	
}

function doOk() 
{
	$(window.frames['flowFrame'].document).find("#submitcheck").click();
}

function disableALink()
{
	//alert("请先新增数据库查询!");
	alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qxxzsjkcx"));
}