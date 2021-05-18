	$(document).ready(function(){
		$("#reportType").change(function(){
			var t = $("#tTime").val();
			var reportType = $(this).val();
			if (reportType == 1)
			{
				$("#countTime").val($('#startTime').val()||t.substring(0,4));
				//$('#countSpan').text('统计年月');
				$('#countSpan').text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
				$('.visible').removeClass('visible').addClass('novisible');
			}
			else if(reportType == 0)
			{
				$("#countTime").val($('#startTime').val()||t.substring(0,7));
				//$('#countSpan').text('统计年月');
				$('#countSpan').text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_1"));
				$('.visible').removeClass('visible').addClass('novisible');
			}
			else if(reportType == 2){
				//$('#countSpan').text('统计时间');
				$('#countSpan').text(getJsLocaleMessage("cxtj","cxtj_sjcx_jgtjbb_text_2"));
				var ts = t.replace(/\d+$/,'01');
				$("#countTime").val($('#startTime').val()||ts);
				$(".enddate").val($('#endTime').val()||t);
				$('.novisible').removeClass('novisible').addClass('visible');
				
			}
			$('#startTime').val('');
			$('#endTime').val('');
		});

		$('.enddate').click(function(){
			if(!$(this).hasClass('Wdate')){
				return;
			}
		    var max = "2099-12-31";
		    var v = $(this).parents("tr").find('.startdate').val();
			if(v.length != 0)
			{
			   
				min = v;
			}
			WdatePicker({dateFmt:'yyyy-MM-dd',minDate:v,maxDate:max,isShowClear:false});
		});
		
		$('.startdate').click(function(){
			var r = $("#reportType").val();
			if (r == 0)
			{
				WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});
			}
			else if(r == 1)
			{
				WdatePicker({dateFmt:'yyyy',isShowClear:false});
			}
			else
			{
				if(!$(this).hasClass('Wdate')){
					return;
				}
		        var max = "2099-12-31";
			    var v = $(this).parents("tr").find('.enddate').val();
			    var min = "1900-01-01";
				if(v.length != 0)
				{
					max = v;
				}
				WdatePicker({dateFmt:'yyyy-MM-dd',minDate:min,maxDate:max,isShowClear:false});
			}

		});
		
		function setValue(province){
			$('#province').val(province);
			$('#msType option[value="'+_msType+'"]').attr('selected',true);
			$('#reportType option[value="'+_reportType+'"]').attr('selected',true);
			$('#countTime').val(_startTime);
			$('input[name="endTime"]').val(_endTime);
			$('#isDes').val(1);
		}
		
		$('.des').click(function(){
			var province = $.trim($(this).parents('tr').children('td:eq(1)').text());
			setValue(province);
			document.forms['pageForm'].elements["pageSize"].value =15;
			$('#search').click();
		})
	});
	function showback(){
		 page_loading();
		window.location.href = 'rep_areaReport.htm?method=find&isback=1';
	}
			