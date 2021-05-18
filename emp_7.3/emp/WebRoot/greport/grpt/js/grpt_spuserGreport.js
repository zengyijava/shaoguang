$(document).ready(function(){
	getLoginInfo("#corpCode");
	var oYear=$('#year'),
	oMonth=$('#month'),
	omsType=$('#mstype'),
	ooderby=$('#orderby'),
	oCorpcode=$('#lgcorpcode');
	var skin = $("#skin").val();
	var objInit={'width':'120','isInput':false,'zindex':0};
	if(skin.indexOf("frame4.0") > -1){
		$('#mstype').isSearchSelectNew(objInit,function(data){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=data.value;
			var orderby=ooderby.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,orderby,corpcode);
		});
		$('#year').isSearchSelectNew(objInit,function(data){
			var year=data.value;
			var mstype=omsType.val();
			var imonth=oMonth.val();
			var orderby=ooderby.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,orderby,corpcode);
		});
		$('#month').isSearchSelectNew(objInit,function(data){
			var year=oYear.val(),
				imonth=data.value;
			var mstype=omsType.val();
			var orderby=ooderby.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,orderby,corpcode);

		});
		$('#orderby').isSearchSelectNew(objInit,function(data){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var orderby=data.value;
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,orderby,corpcode);

		});
	}else {
		$('#mstype').isSearchSelect(objInit,function(data){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=data.value;
			var orderby=ooderby.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,orderby,corpcode);
		});
		$('#year').isSearchSelect(objInit,function(data){
			var year=data.value;
			var mstype=omsType.val();
			var imonth=oMonth.val();
			var orderby=ooderby.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,orderby,corpcode);
		});
		$('#month').isSearchSelect(objInit,function(data){
			var year=oYear.val(),
				imonth=data.value;
			var mstype=omsType.val();
			var orderby=ooderby.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,orderby,corpcode);

		});
		$('#orderby').isSearchSelect(objInit,function(data){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var orderby=data.value;
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,orderby,corpcode);

		});
	}
	createChart(ryear,rimonth,rmstype,rorderby,rcorpcode);
});



function createChart(year,imonth,mstype,orderby,corpcode){
	var options = {
        chart: {
            renderTo: 'charts',
            type: 'column'
        },
        plotOptions:{
        		column:{
        			dataLabels: {
        				enabled: true
        			},
        			pointPadding:0.2,
        			borderWidth:0
        		},
        		series : {
        			events : {
        				legendItemClick: function(event) {
        	                               return false;
        				}
        			}
        		}
        }
    };
	//var data={"credits":{"text":"","href":""},"title":{"text":"Browser market shares at a specific website, 2010"},"xAxis":{"categories":["sp1","sp2","sp3","sp4","sp5","sp6","sp7","sp8","sp9","sp10","sp11","sp12","sp13","sp14","sp15","sp16","sp17","sp18","sp19","sp20","sp21","sp22","sp23","sp24","sp25","sp26","sp27","sp28888888888888"],"labels":{"rotation":-45,"align":"right","style":{"fontSize":"12px","fontFamily":"Verdana, sans-serif"}}},"yAxis":{"min":0,"title":{"text":"Rainfall (mm)"}},"tooltip":{"pointFormat":"\u53d1\u9001\u6761\u6570: <b>{point.y:.1f}\u4e07\u6761<\/b>"},"plotOptions":{"column":{"pointPadding":0.2,"borderWidth":0}},"series":[{"name":"Browser share","data":[49.9,71.5,106.4,129.2,144,176,135.6,148.5,216.4,194.1,95.6,54.4,49.9,71.5,106.4,129.2,144,176,135.6,148.5,216.4,194.1,95.6,54.4,216.4,194.1,95.6,54.4]}]};
	var url=path+"/grpt_spuserGreport.htm?method=createBbJson&year="+year+"&imonth="+imonth+"&mstype="+mstype+"&orderby="+orderby+"&corpcode="+corpcode;
	$.getJSON(url,  function(data) {
       options=$.extend({},options,data);
       var chart = new Highcharts.Chart(options);
    });

}
