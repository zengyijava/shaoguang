$(document).ready(function(){
	getLoginInfo("#corpCode");
	var oYear=$('#year'),
	oMonth=$('#month'),
	omsType=$('#mstype'),
	oCorpcode=$('#lgcorpcode');
	var skin = $("#skin").val();
	var objInit= null;
	var myChart = echarts.init(document.getElementById('charts'),'shine');
	var barChart = echarts.init(document.getElementById('barCharts'),'macarons');
	if(skin.indexOf("frame4.0") > -1){
		$('#mstype').isSearchSelectNew(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode,myChart,barChart);
		});
		$('#year').isSearchSelectNew(objInit,function(){
			var year=oYear.val();
			var mstype=omsType.val();
			var imonth=oMonth.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode,myChart,barChart);
		});
		$('#month').isSearchSelectNew(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode,myChart,barChart);
		});
	}else {
		$('#mstype').isSearchSelect(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode,myChart,barChart);
		});
		$('#year').isSearchSelect(objInit,function(){
			var year=oYear.val();
			var mstype=omsType.val();
			var imonth=oMonth.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode,myChart,barChart);
		});
		$('#month').isSearchSelect(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode,myChart,barChart);
		});
	}
	createChart(ryear,rimonth,rmstype,rcorpcode,myChart,barChart);
});

function parseNumber(num) {

	if (num === undefined) {
		return '--';
	}
	if (!num) {
		return 0;
	}
	var isNatural = true;
	if (num < 0) {
		num = 0 - num;
		isNatural = false;
	}
	var res = "";

	if (num < 10000) {
		res = num;
	} else if (num < 100000000) {
		if (num < 1000000) {
			num = (num / 10000.0).toFixed(2);
		} else if (num < 10000000) {
			num = (num / 10000.0).toFixed(2);
		} else {
			num = (num / 10000.0).toFixed(2);
		}

		if (100 - num == 0) {
			//res = "100.0万";
			res = "100.0"+getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_1");
		} else if (1000 - num == 0) {
			res = "1000"+getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_1");
		} else if (10000 - num == 0) {
			res = "1.00"+getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_2");
		} else {
			res = num + getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_1");
		}
	} else {
		res = (num / 100000000.0).toFixed(2) + getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_2");
	}

	return (isNatural ? "" : "-") + res;
}

function createChart(year,imonth,mstype,corpcode,myChart,barChart){
	myChart.clear();
	var url =path+"/grpt_areaGreport.htm?method=createBbJson&year="+year+"&imonth="+imonth+"&mstype="+mstype+"&corpcode="+corpcode+"&type=map";
	$.getJSON(url,  function(jsonStr) {
	myChart.setOption(jsonStr);
	var option={
			 tooltip: {
					trigger: 'item',
					formatter: 
						function(data){
						var res=eval(jsonStr.tooltip.formatter);
						return res;
					},
			},
	};
	myChart.showLoading();
	myChart.setOption(option);
	myChart.hideLoading();
   });
	barChart.clear();
	var barUrl =path+"/grpt_areaGreport.htm?method=createBbJson&year="+year+"&imonth="+imonth+"&mstype="+mstype+"&corpcode="+corpcode+"&type=bar";
	$.getJSON(barUrl,  function(option) {
		option.tooltip.formatter = function(param){
			
			//return param[0].name +'<br/>'+'发送量：' +parseNumber(param[0].value);
			return param[0].name +'<br/>'+getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_3")+parseNumber(param[0].value);
		};
		option.series[0].itemStyle.normal.label.formatter = function(param){
				//return parseNumber(param.data)+"条";
			return parseNumber(param.data)+getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_4");
		};
		barChart.setOption(option);
   }); 
}
