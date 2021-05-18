
 $(function(){
	$('#birds').keyup(function(){
		var inputkey = $('#birds').val();
		if(inputkey==''){
			$("#spgate>option[value='']").attr('selected','selected');
		}
		$('.ac_results').hide();
		$('#hidFr').hide();
		$('.ac_results').empty();
		$('#spgate>option').each(function(){
			var litxt = $(this).text();
			
			if(litxt.indexOf(inputkey) > -1)
			{
				var gid = $(this).attr("gateid");
				var gtype = $(this).attr("gatetype");
				 $('.ac_results').append('<li liid='+gid+' litype='+gtype+'>'+litxt+'</li>');
			}
		})
		 $('.ac_results>li').hover(function(){
		    $(this).addClass('div_hover_bg').siblings().removeClass('div_hover_bg');
		  },function(){
		   $(this).removeClass('div_hover_bg');
		  }).click(function(){
			  bindClick($(this));
		  });
		$('#hidFr').show();
		  $('.ac_results').show();

	});
	$('.ac_img').bind('click',function(e){
		
		$('#hidFr').toggle();
		$('.ac_results').toggle();
		if($('.ac_results').is(":visible"))
		{
			$('.ac_results').empty();
			$('#spgate > option').each(function(){
				
				var litxt = $(this).text();
				var gid = $(this).attr("gateid");
				var gtype = $(this).attr("gatetype");
				 $('.ac_results').append('<li liid='+gid+' litype='+gtype+'>'+litxt+'</li>');
			   
			});
		 $('.ac_results>li').hover(function(){
			    $(this).addClass('div_hover_bg').siblings().removeClass('div_hover_bg');
			  },function(){
			   $(this).removeClass('div_hover_bg');
			  }).click(function(){
				  bindClick($(this));
		  });
		 $('#hidFr').show();
		  $('.ac_results').show();
		}
		e.stopPropagation();
	})
	
	$("body,html").click(function(){
		$('.ac_results').hide();
		$('.ac_results2').hide();
		$('#hidFr').hide();
		
		
	})
	
	
	$('#birds2').keyup(function(){
		var inputkey = $('#birds2').val().toUpperCase();
		if(inputkey==''){
			$("#userid>option[value='']").attr('selected','selected');
		}
		$('.ac_results2').hide();
		$('.ac_results2').empty();
		$('#userid>option').each(function(){
			var litxt = $(this).text();
			if(litxt.indexOf(inputkey) > -1)
			{
				var gid = $(this).attr("uid");
				var gtype = $(this).attr("gatetype");
				 $('.ac_results2').append('<li liid='+gid+' litype='+gtype+'>'+litxt+'</li>');
			}
		})
		 $('.ac_results2>li').hover(function(){
		    $(this).addClass('div_hover_bg').siblings().removeClass('div_hover_bg');
		  },function(){
		   $(this).removeClass('div_hover_bg');
		  }).click(function(){
			  bindClick2($(this));
		  });
		  $('.ac_results2').show();

	});
	$('.ac_img2').bind('click',function(e){
		$('.ac_results2').toggle();
		if($('.ac_results2').is(":visible"))
		{
			$('.ac_results2').empty();
			$('#userid > option').each(function(){
				var litxt = $(this).text();
				var gid = $(this).attr("uid");
				var gtype = $(this).attr("gatetype");
				 $('.ac_results2').append('<li liid='+gid+' litype='+gtype+'>'+litxt+'</li>');
			});
		 $('.ac_results2>li').hover(function(){
			    $(this).addClass('div_hover_bg').siblings().removeClass('div_hover_bg');
			  },function(){
			   $(this).removeClass('div_hover_bg');
			  }).click(function(){
				  bindClick2($(this));
		  });
		  $('.ac_results2').show();
		  e.stopPropagation();
		}
	})
	
});
function bindClick($ob)
{
	 $('#birds').val($ob.text());
	    var gid = $ob.attr("liid");
	    var gtype = $ob.attr("litype");
	    $('.ac_results').hide();
		$('#hidFr').hide();
		$("#spgatetype").val(gtype);
		$('#spgate').val($('#spgate > option[gateid='+gid+']').attr('value'));
}
function bindClick2($ob)
{
	 $('#birds2').val($ob.text());
	    var gid = $ob.attr("liid");
	    var gtype = $ob.attr("litype");
		$('.ac_results2').hide();
		$("#accouttype").val(gtype);
		$('#userid').val($('#userid > option[uid='+gid+']').attr('value'));
}
function showreturn(){
	var inputkey = $('#birds').val();

	$('.ac_results').empty();
	$('.ac_results2>li').each(function(){
		var litxt = $(this).text();
		if(litxt.indexOf(inputkey) > -1)
		{
			 $('.ac_results').append('<li>'+litxt+'</li>');
		}
	});
	$('.ac_results>li').hover(function(){
	    $(this).addClass('selected').siblings().removeClass('selected');
	  },function(){
	   $(this).removeClass('selected');
	  }).click(function(){
	    $('#birds').val($(this).text());
		$('.ac_results').hide();
	  });
	 
}
function showreturnsp(){
	var inputkey = $('#birdssp').val();

	$('.ac_resultssp').empty();
	$('.ac_results2sp>li').each(function(){
		var litxt = $(this).text();
		if(litxt.indexOf(inputkey) > -1)
		{
			 $('.ac_resultssp').append('<li>'+litxt+'</li>');
		}
	});
	$('.ac_resultssp>li').hover(function(){
	    $(this).addClass('selected').siblings().removeClass('selected');
	  },function(){
	   $(this).removeClass('selected');
	  }).click(function(){
	    $('#birdssp').val($(this).text());
		$('.ac_resultssp').hide();
	  });
	 
}
function showreturnadd(){
	var inputkey = $('#birdsadd').val();

	$('.ac_resultsadd').empty();
	$('.ac_results2add>li').each(function(){
		var litxt = $(this).text();
		if(litxt.indexOf(inputkey) > -1)
		{
			 $('.ac_resultsadd').append('<li>'+litxt+'</li>');
		}
	});
	$('.ac_resultsadd>li').hover(function(){
	    $(this).addClass('selected').siblings().removeClass('selected');
	  },function(){
	   $(this).removeClass('selected');
	  }).click(function(){
	    $('#birdsadd').val($(this).text());
		$('.ac_resultsadd').hide();
	  });
	 
}



