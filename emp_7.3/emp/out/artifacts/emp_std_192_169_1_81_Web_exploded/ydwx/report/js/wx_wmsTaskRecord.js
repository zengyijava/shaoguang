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
			
			
			//查看
			function Look(netId){	
			    //$("#netid").val(netId);
			    $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
			       data=eval("("+data+")");
			       listpage=data;
			       $(".ym").children().remove();
			       for(var i=0;i<listpage.length;i++)	{   
			           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
			      }
                    // 此处为显示错误页面，避免进入登录页面
                    if(listpage[0].content=="notexists"){
                        $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
                    }else{
                        $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
                    }
			      $("#divBox").dialog("open");
		       });
		    }