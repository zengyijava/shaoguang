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
				//$('#countSpan').text('统计年月1');
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
		    var max = "2099-12-31 00:00:00";
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
	});

		function setValue(spisuncm,staffId){
			$('#spisuncm option[value="'+spisuncm+'"]').attr('selected',true);
			$('#staffId option[value="'+staffId+'"]').attr('selected',true);
			$('#isDes').val(1);
		}

		$('.des').click(function(){
			var isDes = "1";
			var spuncm = $.trim($(this).parents('tr').children('td:eq(2)').text()).toString();
			var staffId = $.trim($(this).parents('tr').children('td:eq(1)').text()).toString();
			var spisuncm = "";
			//if(staffId == "未知")
			if(staffId == getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_6"))	
			{
				staffId = " ";
			}
			//if(spuncm=="移动"){
			if(spuncm==getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_7")){	
				spisuncm="0";
				setValue(spisuncm,staffId);
			//}else if (spuncm=="联通")
			}else if (spuncm==getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_8"))	
			{
				spisuncm="1";
				setValue(spisuncm,staffId);
			//}else if(spuncm=="电信")
			}else if(spuncm==getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_9"))	
			{
				spisuncm="21";
				setValue(spisuncm,staffId);
			//}else if(spuncm=="国外")
			}else if(spuncm==getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_10"))	
			{
				spisuncm="5";
				setValue(spisuncm,staffId);
			}
			document.forms['pageForm'].elements["pageSize"].value =15;
			$('#search').click();
		});
		
		$('.country').click(function(){
			var corpcode = $('#lgcorpcode').val();
			var msType = $('#msTypes').val();
			var countTime = $('#countTime').val();
			var reportType = $('#reportTypes').val();
			if(reportType == 2)
			{
				var startTime = _startTime;
				var endTime = _endTime;
			}
			var staffId = $.trim($(this).parents('tr').children('td:eq(1)').text()).toString();
			//if(staffId == "未知")
			if(staffId == getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_6"))	
			{
				staffId = " ";
			}
			var spisuncm = "5";
			window.location.href = "rep_spisuncmMtReport.htm?method=infoDetilByArea&lgcorpcode="+corpcode+"&msType="+msType+"&countTime="+countTime+"&startTime="+startTime+"&endTime="+endTime+"&staffId="+staffId+"&spisuncm="+spisuncm+"&reportType="+reportType+"";
		
		});			
		

	function back(){
		window.location.href = "rep_spisuncmMtReport.htm?method=find&isback=1";
	}

