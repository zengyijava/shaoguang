
 $(function(){
  		
	 $('body,html').bind('click',function(){
         $('.ac_results,.ac_results2').hide();

     })
	 	
		$('#birds').keyup(function(){
			showreturn();
			 $('.ac_results').show();
	
		});
		$('.ac_img').bind('click',function(e){
			//showreturn();
			 $('.ac_results').hide();
			 $('.ac_results2').toggle();
			 $('.ac_results2>li').hover(function(){
	    $(this).addClass('selected').siblings().removeClass('selected');
	  },function(){
	   $(this).removeClass('selected');
	  }).click(function(){
	    $('#birds').val($(this).text());
		$('.ac_results2').hide();
	  });
			 e.stopPropagation();  
		})
		
});

function showreturn(){
	var inputkey = $('#birds').val();
$('.ac_results2').hide();
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



