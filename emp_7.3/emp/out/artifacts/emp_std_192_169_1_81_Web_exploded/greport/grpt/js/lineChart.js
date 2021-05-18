$(document).ready(function(){
		var oYear=$('#iYear'),
			oMonth=$('#iMonth'),
			oDays=$('#iDays'),
			oDate=new Date();
		init(oYear.find('option').last().val());//初始化	
		oYear.bind('change',function(){
			var v=$(this).val();
			oMonth.show().find('option').first().attr('selected','selected');
			chooseYear(v);
		});

		oMonth.bind('change',function(){
			var iyear=oYear.val(),
				imonth=$(this).val();
			chooseMonth(iyear,imonth);
		})
	})


	function chooseYear(v){
		if(v!==''){
			var myChart = new JSChart('graph', 'line');
			myChart.setDataJSON('month.json');
			//myChart.setTitle(v+'年系统发送趋势图');
			myChart.setTitle(v+getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_8"));
			myChart.draw();
		}
		
	}

	function chooseMonth(iyear,imonth){
		if(iyear && (imonth!='')){
			var myChart = new JSChart('graph', 'line'),
			nDays=getDateInMonth(iyear,imonth);//获取当前月有多少天
			//myChart.setAxisValuesNumberX(nDays);//控制时间轴显示的天数
			myChart.setDataJSON('days.json');
			//myChart.setTitle(iyear+'年'+(parseInt(imonth)+1)+'月系统发送趋势图');
			myChart.setTitle(iyear+getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_9")+(parseInt(imonth)+1)+getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_10"));
			myChart.draw();
		}
	}

	function init(args){
		var myChart = new JSChart('graph', 'line');
		myChart.setDataJSON('data2.json');
		//myChart.setTitle(args+'年系统发送趋势图');
		myChart.setTitle(args+getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_8"));
		myChart.draw();
	}

	function getDateInMonth(iyear,imonth){
		var date=new Date(iyear,imonth,0);
		return date.getDate();
	}

	function toUpdateSel(iyear,imonth){
		//var str="<option value=''>请选择日期</option>",
		var str="<option value=''>"+getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_11")+"</option>",
			daysInmonth=getDateInMonth(iyear,imonth),
			oDays=document.getElementById('iDays');;
			
		for(var i=1;i<=parseInt(daysInmonth);i++){
			str+="<option value='"+i+"'>"+i+"</option>";
		}
		//console.log(str);
		oDays.innerHTML=str;
	}