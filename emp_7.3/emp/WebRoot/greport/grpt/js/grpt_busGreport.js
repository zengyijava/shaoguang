 $(document).ready(function(){
    getLoginInfo("#corpCode");
		var oYear=$('#year'),
			oMonth=$('#month'),
			oDays=$('#iDays'),
			omsType=$('#mstype'),
			obuscode=$('.buscode'),
			oCorpcode=$('#lgcorpcode'),
			oDate=new Date();
	 var skin = $("#skin").val();
		//选择框js模拟--依赖jquery.selectnew.js与select.css
	var objInit={'width':'100','isInput':false,'zindex':0};
	 if(skin.indexOf("frame4.0") > -1){
		 $('#year,#month,#iDays,#mstype').isSearchSelectNew(objInit,function(){
			 ParamsVal();
		 });
	 }else {
		 $('#year,#month,#iDays,#mstype').isSearchSelect(objInit,function(){
			 ParamsVal();
		 });
	 }

		obuscode.click(function(){
			ParamsVal();
		});
		createChart(year,imonth,mstype,buscode,rcorpcode);
		function ParamsVal(){
			var year=oYear.val(),
						imonth=oMonth.val(),
						mstype=omsType.val(),
						corpcode=oCorpcode.val(),
						codeVal="";
		            $("input[name='buscode']:checkbox").each(function(){ 
		                if($(this).attr("checked")){
		                    codeVal += $(this).val()+","
		                }
		            })
		            codeVal=codeVal.slice(0,codeVal.length-1);
		            createChart(year,imonth,mstype,codeVal,corpcode);
		}
    });
function createChart(year,imonth,mstype,buscode,corpcode){
	var options = {
        chart: {
            renderTo: 'charts',
            type: 'spline'
        },
        plotOptions:{
        	series : {
        		events : {
					legendItemClick: function(event) {
                               return false;
					}
				}
        	}
        }
    };
    var url =  path+"/grpt_busGreport.htm?method=createBbJson&year="+year+"&imonth="+imonth+"&mstype="+mstype+"&buscode="+buscode+"&corpcode="+corpcode;

    $.getJSON(url,  function(data) {
       options=$.extend({},options,data);
       var chart = new Highcharts.Chart(options);
    });
}