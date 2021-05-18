$(document).ready(function(){
	getLoginInfo("#corpCode");
	var oYear=$('#year'),
	oMonth=$('#month'),
	omsType=$('#mstype'),
	oCorpcode=$('#lgcorpcode');
	var skin = $("#skin").val();
	var objInit={'width':'100','isInput':false,'zindex':0};
	if(skin.indexOf("frame4.0") > -1){
		$('#mstype').isSearchSelectNew(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode);
		});
		$('#year').isSearchSelectNew(objInit,function(){
			var year=oYear.val();
			var mstype=omsType.val();
			var imonth=oMonth.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode);
		});
		$('#month').isSearchSelectNew(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode);
		});
	}else {
		$('#mstype').isSearchSelect(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode);
		});
		$('#year').isSearchSelect(objInit,function(){
			var year=oYear.val();
			var mstype=omsType.val();
			var imonth=oMonth.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode);
		});
		$('#month').isSearchSelect(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,corpcode);
		});
	}
		createChart(ryear,rimonth,rmstype,rcorpcode);
});


function createChart(year,imonth,mstype,corpcode){
	var options = {
        chart: {
            renderTo: 'charts',
            type: 'pie'
        }
    };
	//var data={"credits":{"text":"","href":""},"title":{"text":"Browser market shares at a specific website, 2010"},"tooltip":{"pointFormat":"{series.name}: <b>{point.percentage:.1f}%<\/b>"},"plotOptions":{"pie":{"allowPointSelect":true,"cursor":"pointer","dataLabels":{"enabled":true,"color":"#000000","connectorColor":"#000000","format":"<b>{point.name}<\/b>: {point.x}w"}}},"series":[{"name":"Browser share","data":[{"name":"Firefox","y":45,"x":4500},{"name":"Ie","y":26.8,"x":"2680","sliced":true,"selected":true},{"name":"Chrome","y":12.8,"x":"1000"},{"name":"Safari","y":8.5,"x":"850"},{"name":"Opera","y":6.2,"x":"620"},{"name":"Others","y":0.7,"x":"70"}]}]};
	var url =path+"/grpt_areaGreport.htm?method=createBbJson&year="+year+"&imonth="+imonth+"&mstype="+mstype+"&corpcode="+corpcode;
	 $.getJSON(url,  function(data) {
       options=$.extend({},options,data);
       var chart = new Highcharts.Chart(options);
    });

}