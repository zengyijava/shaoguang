$(document).ready(function() {
    $("#content tbody tr").hover(function() {
		$(this).addClass("hoverColor");
	}, function() {
		$(this).removeClass("hoverColor");
	});		
	$("#toggleDiv").toggle(function() {
		$("#condition").hide();
		$(this).addClass("collapse");
	}, function() {
		$("#condition").show();
		$(this).removeClass("collapse");
	});
	$('#search').click(function(){submitForm();});
	$("#detailDialog").dialog({
		autoOpen: false,
		height:450,
		width: 650,
		modal: true,
		resizable:false,
		close:function(){
		}
	});
	
	$('#modify').dialog({
		autoOpen: false,
		width:250,
	    height:200
	});
	
});

$(function(){
  
  $('#taskType').isSearchSelect({'width':'160','isInput':false,'zindex':0});
  $('#spUser').isSearchSelect({'width':'160','isInput':true,'zindex':0});
  $('#mtSendState').isSearchSelect({'width':'160','isInput':false,'zindex':0});
})

function modify(t)
{
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	$('#modify').dialog('open');
}

function deleteleftline2()
{
	$('#ccontent1 table tbody tr:last').addClass('tbody_last');
	$('#ccontent1 > table > tbody > tr').each(function(){$(this).find('td:last').addClass('no_r_b');}); 
	$('#ccontent1 > table > tbody > tr').each(function(){$(this).find('td:first').addClass('no_l_b');});
	$('#ccontent1 table th:first').addClass('th_l_b');
	$('#ccontent1 table th:last').addClass('th_r_b');	
}

