$(document).ready(function(){
	getLoginInfo("#corpCode");
	var oYear=$('#year'),
			oMonth=$('#month'),
			omsType=$('#mstype'),
			oCorpcode=$('#lgcorpcode');
	//选择框js模拟--依赖jquery.selectnew.js与select.css
	var objInit={'width':'100','isInput':false,'zindex':0};
	var skin = $("#skin").val();
	if(skin.indexOf("frame4.0") > -1){
		$('#mstype').isSearchSelectNew(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,'',false,corpcode);
		});
		$('#year').isSearchSelectNew(objInit,function(){
			var year=oYear.val();
			var mstype=omsType.val();
			var imonth=oMonth.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,'',false,corpcode);
		});
		$('#month').isSearchSelectNew(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			if(imonth===''){
				$('.month1').next().hide();
			}else{
				$('.month1').next().show();
			}
			createChart(year,imonth,mstype,'',false,corpcode);
		});
	}else {
		$('#mstype').isSearchSelect(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,'',false,corpcode);
		});
		$('#year').isSearchSelect(objInit,function(){
			var year=oYear.val();
			var mstype=omsType.val();
			var imonth=oMonth.val();
			var corpcode=oCorpcode.val();
			createChart(year,imonth,mstype,'',false,corpcode);
		});
		$('#month').isSearchSelect(objInit,function(){
			var year=oYear.val(),
				imonth=oMonth.val();
			var mstype=omsType.val();
			var corpcode=oCorpcode.val();
			if(imonth==''){
				$('.month1').next().hide();
			}else{
				$('.month1').next().show();
			}
			createChart(year,imonth,mstype,'',false,corpcode);
		});
	}
	//图表初始化
	createChart(ryear,rimonth,rmstype,stempval,false,rcorpcode);

		//选择框js模拟--依赖jquery.selectnew.js与select.css
		$('#all_option select').isSearchSelect({'width':'100','isInput':false,'zindex':0});
		//与其他时间对比---新增按钮事件
		var all_option=$('#all_option');
		$('.todo').live('click',function(){
			var yearLen=$("#year option[value!='']").length,
				oUlLen=all_option.find('.addOption').length;
			if($(this).hasClass('addPara') && (($('#month').val()==''&&yearLen>oUlLen)||($('#month').val()!=''&&yearLen*12>oUlLen))){
				var oUl=all_option.find('.addOption:last').clone();
				oUl.find('select').each(function(){
					$(this).find('option').eq(0).attr('selected','selected');
					$(this).next().remove();
				})
				oUl.find('select').isSearchSelect(objInit);
				all_option.append(oUl);
				var monthVal=$('#month').val();
				if(monthVal==''){
					$('.month1').next().hide();
				}else{
					$('.month1').next().show();
				}	
				$(this).addClass('reducePara').removeClass('addPara');
				
			}
			else if($(this).hasClass('reducePara')){
				$(this).parent().parent().remove();
			}
		})
		//与其他时间对比按钮
		$('.compare').click(function(){
			var monthVal=$('#month').val();
			if(monthVal==''){
				$('.month1').next().hide();
			}else{
				$('.month1').next().show();
			}
			$('#other_time').toggle();
		})
		//关闭按钮
		$('.close,#cancel').click(function(){
			$('#other_time').hide();
		})
		//与其他时间对比提交
		$('#confirm').click(function(){
			//获取之前选择的年月
			var yVal=$('#year').val();
			var mVal=$('#month').val();
			var mstype=omsType.val();
			var imonth=oMonth.val();
			var corpcode=oCorpcode.val();
			var selVal={},arr=[],
			oSel=$('#all_option .addOption select');
			
			if(oSel.size()>0){
				var sTempVal='';
				$('#all_option ul.addOption').each(function(){
					$(this).find('select').each(function(i){
						var sVal=$(this).val();
						sVal=sVal=='' ? 'null' : sVal;
						if(i%2==0){
							sTempVal+=sVal+',';
						}else{
							sTempVal+=sVal;
						}
					})
					sTempVal+='a';
					var s1=yVal=='' ? 'null' : yVal;
					var s2=mVal=='' ? 'null' : mVal;
					sTempVal+=s1+','+s2+'a';
				})
				var aTemp=sTempVal.split('a');
				sTempVal=distinctArray(aTemp).join('a')+'a';
				if(sTempVal.substr(-10)=='null,nulla'){
					sTempVal=sTempVal.substr(0,sTempVal.length-10);
				}
				//只记录年
				var sYear="";
				var sMonth="";
				/*$('#all_option .year1').each(function(){
					var sYear1=$(this).val()=='' ? 'null' : $(this).val();
					sYear+=sYear1+',';
				})*/
				var tempArr=sTempVal.split('a');
				for(var i=0;i<tempArr.length-1;i++){
					sYear+=tempArr[i].split(',')[0]+',';
					sMonth+=tempArr[i].split(',')[1]+',';
				}
				
				//只记录月
				
				/*$('#all_option .month1').each(function(){
					var sMonth1=$(this).val()=='' ? 'null' : $(this).val();
					sMonth+=sMonth1+',';
				})*/
				//如果只选择年份情况
				if(yVal!='' && mVal==''){
					if(sYear.indexOf('null')!=-1){
						//alert('您还有未选择的年份');
						alert(getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_5"));
					}else{
						createChart('',imonth,mstype,sTempVal,true,corpcode);
					}
				}else if(yVal!='' && mVal!=''){//如果同时选择了年月
					if(sYear.substr(0,4)=='null' && sMonth.substr(0,4)=='null'){
						//alert('您还有未选择的年份与月份');
						alert(getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_6"));
					}else if(sYear.indexOf('null')!=-1 && sMonth.indexOf('null')!=-1){
						//alert('您还有未选择的年份与月份');
						alert(getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_6"));
					}else if(sYear.indexOf('null')!=-1 && sMonth.indexOf('null')==-1){
						//alert('您还有未选择的年份');
						alert(getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_5"));
					}else if(sYear.indexOf('null')==-1 && sMonth.indexOf('null')!=-1){
						//alert('您还有未选择的月份');
						alert(getJsLocaleMessage("cxtj","cxtj_sjfx_ztfsqs_text_7"));
					}else{
						createChart('',imonth,mstype,sTempVal,true,corpcode);
					}
				}
				
				
			}
			
			
		})
		
});

function distinctArray(arr){
	var tempArr=[],obj={};
	for(var i=0;i<arr.length;i++){
		if(!obj[arr[i]] && arr[i]!==''){
			tempArr.push(arr[i]);
			obj[arr[i]]=true;
		}
	}
	return tempArr;
}
function createChart(year,imonth,mstype,estempval,flag,corpcode){
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
    var url =  path+"/grpt_totalGreport.htm?method=createBbJson&year="+year+"&imonth="+imonth+"&mstype="+mstype+"&stempval="+estempval+"&corpcode="+corpcode;

    $.getJSON(url,  function(data) {
    	if(flag===true){
    		$('#other_time').hide();
    	}
       options=$.extend({},options,data);
       var chart = new Highcharts.Chart(options);
    });
}