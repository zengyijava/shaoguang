$(document).ready(function() {
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
			showPageInfo2(total,pageIndex,pageSize,totalRec);
			$('#search').click(function(){submitForm();});
		});
		function modify(t,i)
		{
			$('#modify').dialog({
				autoOpen: false,
				resizable: false,
				width:250,
			    height:200
			});
			$("#msg").children("xmp").empty();
			$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
			if(i==1)
			{
				/*模板名称*/
				$('#modify').dialog('option','title',getJsLocaleMessage("common","common_smsTemplate_4"));
			}
			else
			{
				/*模板内容*/
				$('#modify').dialog('option','title',getJsLocaleMessage("common","common_smsTemplate_1"));
			}
			$('#modify').dialog('open');
		}