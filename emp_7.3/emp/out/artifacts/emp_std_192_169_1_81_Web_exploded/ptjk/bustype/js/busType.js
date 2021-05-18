// 

//ie浏览器兼容性
var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0");
var isIE7 = isIE && (navigator.appVersion.match(/MSIE 7.0/i)=="MSIE 7.0");
//添加行
function addLine(){
	var tbody = $("#tbody");
	var sontr=tbody.find("tr");
	if(sontr.length==23){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_yw_2"));
		return;
	}
	//计算行数
	var linecount=sontr.length+1;
	tbody.append("<tr id='tr"+linecount+"' class='trClass' style='height: 24px;'><td style='width: 150px;'></td><td style='width: 154px;'>" +
			"<select id='selectfrom"+linecount+"' name='selectfrom"+linecount+"' style='width: 40px;' onchange='from_change(this)'><option>0</option>" +
			"<option>1</option><option>2</option><option>3</option>" +
			"<option>4</option><option>5</option><option>6</option>" +
			"<option>7</option><option>8</option><option>9</option>" +
			"<option>10</option><option>11</option>	<option>12</option>" +
			"<option>13</option><option>14</option><option>15</option>" +
			"<option>16</option><option>17</option><option>18</option>" +
			"<option>19</option><option>20</option><option>21</option>" +
			"<option>22</option></select>" +getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_14")+
			"<select id='selectto"+linecount+"' name='selectto"+linecount+"' style='width: 40px;' onchange='to_change(this)'><option>1</option>" +
			"<option>2</option><option>3</option><option>4</option>" +
			"<option>5</option><option>6</option>" +
			"<option>7</option><option>8</option><option>9</option>" +
			"<option>10</option><option>11</option>" +
			"<option>12</option><option>13</option>" +
			"<option>14</option><option>15</option>" +
			"<option>16</option><option>17</option>" +
			"<option>18</option><option>19</option>" +
			"<option>20</option><option>21</option>" +
			"<option>22</option><option>23</option></select></td><td style='width: 152px;'>" +
			"<input type='text' id='alarmCount"+linecount+"' name='alarmCount"+linecount+"' style='width:80px;height:15px;' maxlength='9' value='' onkeyup='numberControl($(this))' onblur='numberControl($(this))' onchange='parseNumber($(this))'>" +
			"</td><td style='width: 154px;'><input type='text' id='biger"+linecount+"' name='biger"+linecount+"' style='width:30px;height:15px;'  maxlength='3' value='' onkeyup='numberControl($(this))' onblur='numberControl($(this))' onchange='checkNumber($(this))'>&nbsp;%</td>" +
			"<td style='width: 154px;'><input type='text' id='smaller"+linecount+"' name='smaller"+linecount+"' style='width:30px;height:15px;'  maxlength='3' value='' onkeyup='numberControl($(this))' onblur='numberControl($(this))' onchange='checkNumber($(this))'>&nbsp;%</td>" +
			"<td  style='width:155px' id='del'><a id='del'  onclick='javascript:del("+linecount+")'>"+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_17")+"</a></td></tr>");
	$(".trClass").each(function(i){
		   $(this).children("td").eq(0).text(i+1);
		   //如果是超过9行，表格会变不整齐
		 });
	if(sontr.length>7){
		$("#content").css("width","900px");
		////ie浏览器兼容性判断
		if(isIE6||isIE7){
			$("#opreate").css("width","150px");
		}else{
			$("#opreate").css("width","170px");
		}

	}
	//当只是点击增加行时候，下拉框有值，相当于初始化这行数组
//	arr[linecount]="0:1";//根据页面上新增时候，不做任何操作下拉框的值
}

//点击删除处理
function del(idtr) 
{
	$("#tr"+idtr).remove();
//	for(var i=1;i<24;i++){
//		var from=$("#selectfrom"+i).val();
//		var to=$("#selectto"+i).val();
//		if(from!=''&&from!=undefined&&to!=''&&to!=undefined){
//			arr=new Array(); 
//			arr[i]==from+":"+to;
//		}
//	}
	var tbody = $("#tbody");
	var sontr=tbody.find("tr");
	$(".trClass").each(function(i){
		   $(this).children("td").eq(0).text(i+1);
		 });
	if(sontr.length<9){
		$("#content").css("width","900px");
		$("#opreate").css("width","152px");
	}
}

function finishIntercept(){
	$("#sub").attr("disabled",true);
	finish();
	$("#sub").attr("disabled",false);
}

//保存事件
function finish(){
	var isrepeat=false;
	//判断业务是否选择
	var busName=$("#busName").val();
	if(busName==""){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_yw_3"));
		return;
	}
	
	//判断手机号码格式
	var phone=$("#monphone").val();
	
	//告警手机号为空 则 进行确认
	if(phone.length==0&&!confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_9"))){
		return false;
	}
	
	if(!validatePhones($("#monphone"))){
		return false;
	}
	
	//判断邮箱格式
	var email=$("#monemail").val();
	if(email==" "){email="";}
	//告警邮箱为空 则 进行确认
	if(email!=null){
		if(email.length==0 && !confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_3"))){
			return false;
		}
		if(!email.length==0 && !checkEmail(email)){
				alert(getJsLocaleMessage("ptjk","ptjk_wljk_web_2"));
				return false;
		}
	}
	var begintime=$("#begintime").val();
	var endtime=$("#endtime").val();
	if(begintime==''){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_yw_4"));
		return;
	}
	if(endtime==''){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_yw_5"));
		return;
	}
	var num;
	var isnull=false;
	$(".trClass").each(function(i){
		
		num=i;
		var td3=$(this).children("td").eq(2);
		if(td3.find("input").val()==''){
			isnull=true;
			return false;
		}
		var td4=$(this).children("td").eq(3);
		if(td4.find("input").val()==''){
			isnull=true;
			return false;
		}
		var td5=$(this).children("td").eq(4);
		if(td5.find("input").val()==''){
			isnull=true;
			return false;
		}
		 });
	if(num==undefined){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_yw_6"));
		return;
	}
//	for(var i=1;i<=(repeatArr.length+1);i++){
//		if(repeatArr[i]){
//			isrepeat=true;
//			break;
//		}
//	}
	if(!check_time()){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_4"));
		return;
	}
	if(isnull){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_yw_7"));
		return;
	} 
	
//	if(isrepeat){
//		alert("时间段已重复，不能保存，请重新设置！");
//		return;
//	}
	//document.forms[0].submit();
    var optype = $("#optype").val();
    var data = $("#addForm").serializeArray();
    if(optype=="add"){
		$.ajax({
		        url: pathUrl+'/mon_busCfg.htm?method=addBusType',
		        data:data,
		        type: 'post',
		        error: function(){
		           alert(getJsLocaleMessage("ptjk","ptjk_common_xzsb"));		
		        },
		        success: function(resultMsg){
                  if(resultMsg != null && resultMsg == "true")
                  {
                  		alert(getJsLocaleMessage("ptjk","ptjk_common_xzcg"));
                  		doreturn();
                  }else if(resultMsg != null && resultMsg == "false"){
                  		alert(getJsLocaleMessage("ptjk","ptjk_common_xzsb"));
                  }else{
                	  alert(getJsLocaleMessage("ptjk","ptjk_common_xzsb"));
                  }
		        }
		    });
		
    }else{
		$.ajax({
	        url: pathUrl+'/mon_busCfg.htm?method=updateBusType',
	        data:data,
	        type: 'post',
	        error: function(){
	           alert(getJsLocaleMessage("ptjk","ptjk_common_xgsb"));		
	        },
	        success: function(resultMsg){

              if(resultMsg != null && resultMsg == "true")
              {
              		alert(getJsLocaleMessage("ptjk","ptjk_common_xgcg"));
              		doreturn();
              }else if(resultMsg != null && resultMsg == "false"){
              		alert(getJsLocaleMessage("ptjk","ptjk_common_xgsb"));
              }else{
            	  alert(getJsLocaleMessage("ptjk","ptjk_common_xgsb"));
              }
	        }
	    });
    }
		
}

//检查所有行是否存在重复
function check_time(){
	var  arr=new Array(); 
	for(var i=1;i<24;i++){
		var from=$("#selectfrom"+i).val();
		var to=$("#selectto"+i).val();
		if(from!=''&&from!=undefined&&to!=''&&to!=undefined){
			var fromInt=parseInt(from);
			var toInt=parseInt(to);
			for(var k=fromInt;k<toInt;k++){
				if($.inArray(k, arr)==-1){
					arr.push(k);
				}else{
					return false;
				}
				
			}
		}
	}
	return true;
}

//检查当前选择行与其他行是否重复 
function check_line(index){
	var  arr=new Array(); 
	//排除本行，把其他行不是重复的值放在数组里面
	for(var i=1;i<24;i++){
		if(i==index){
			continue;
		}
		var from=$("#selectfrom"+i).val();
		var to=$("#selectto"+i).val();
		if(from!=''&&from!=undefined&&to!=''&&to!=undefined){
			var fromInt=parseInt(from);
			var toInt=parseInt(to);
			for(var k=fromInt;k<toInt;k++){
				if($.inArray(k, arr)==-1){
					arr.push(k);
				}else{
					continue;
				}
				
			}
		}
	}
	//然后，当前行判断是否重复，如果重复，就返回false
	var fromselect=$("#selectfrom"+index).val();
	var toselect=$("#selectto"+index).val();
	var fromInt=parseInt(fromselect);
	var toInt=parseInt(toselect);
	for(var k=fromInt;k<toInt;k++){
		if($.inArray(k, arr)==-1){
			arr.push(k);
		}else{
			return false;
		}
		
	}
	return true;
}

//选择下拉列表处理
function from_change(obj){
	var va=$(obj).val();
	var to_val=0;
	//找到下个节点
	var nextpoit=$(obj).next();
	 var text=nextpoit.find("option:selected").text();

	//把内容清空
	nextpoit.empty();
	var tem=parseInt(va)+1;
	
	for(var i=tem;i<=23;i++){//开始的范围是0-22的值 另外一个下拉框，大于当前值
			nextpoit.append("<option>"+i+"</option>");
	}
	nextpoit.find("option[text='"+text+"']").attr("selected",true);
	var col=$(obj).parent().prev();
	var index=$.trim(col.text());
	
	var ret=check_line(index);
	if(!ret){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_18"));
		return;
	}
	
	//第一次选择，第一个下拉框选择，如果第二个下拉框不赋值，那么值就为空的
	//如果用户只是选择了第一个，第二个不选择，那么就会出现存值错误的情况
//	var isSelect=nextpoit.find("option[text='"+text+"']").attr("selected");
//	if(isSelect==null){
//		to_val=tem;
//	}else{
//		to_val=text;
//	}
//	for(var i=1;i<arr.length;i++){
//		//循环取值，如果存在交集，给出提示(传入的最小值大于最大值)
//		if(index==i||arr[i]==undefined){
//			continue;	
//		}
//		var temp=arr[i].split(":");
//		if(temp.length==2){
//			var start=temp[0];
//			var end=temp[1];
//			if((parseInt(va)>parseInt(start)&&parseInt(va)<parseInt(end))||(parseInt(to_val)>parseInt(start)&&parseInt(to_val)<parseInt(end))){
//				alert("时间段已重复，请重新设置！");
//				repeatArr[index]=true;
//				return;
//			}else if((parseInt(va)<parseInt(start)&&parseInt(to_val)>parseInt(end))||
//					(parseInt(va)<parseInt(start)&&parseInt(to_val)>=parseInt(end))||
//							(parseInt(va)<=parseInt(start)&&parseInt(to_val)>parseInt(end))||
//							(parseInt(va)==parseInt(start)&&parseInt(to_val)==parseInt(end))){
//				alert("时间段已重复，请重新设置！");
//				repeatArr[index]=true;
//				return;
//			}
//		}
//	}
//
//	arr[index]=va+":"+to_val;
//	repeatArr[index]=false;
}

//处理0开头的的情况,增加校验
function checkNumber(obj){
	obj.val(parseInt(obj.val()));
	if(obj.val()>100){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_yw_8"));
		obj.val("");
	}
}

//处理0开头的的情况
function parseNumber(obj){
	obj.val(parseInt(obj.val()));
}


//选择下拉列表处理
function to_change(obj){
	var va=$(obj).val();
	//找到下个节点
	var point=$(obj).prev();
	 var text=point.find("option:selected").text();
	//把内容清空
	point.empty();
	for(var i=0;i<va;i++){//1到23
		point.append("<option>"+i+"</option>");
	}
	//保持原来选中状态
	point.find("option[text='"+text+"']").attr("selected",true);
	var col=$(obj).parent().prev();
	var index=$.trim(col.text());
	
	var ret=check_line(index);
	if(!ret){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_18"));
		return;
	}
//	for(var i=1;i<arr.length;i++){
//		//循环取值，如果存在交集，给出提示(传入的最小值大于最大值)
//		if(index==i||arr[i]==undefined){
//			continue;	
//		}
//		var temp=arr[i].split(":");
//
//		if(temp.length==2){
//			var start=temp[0];
//			var end=temp[1];
//			if((parseInt(va)>parseInt(start)&&parseInt(va)<parseInt(end))||parseInt(text)>parseInt(start)&&parseInt(text)<parseInt(end)){
//				repeatArr[index]=true;
//				alert("时间段已重复，请重新设置！");
//				return;
//			}else if((parseInt(text)<parseInt(start)&&parseInt(va)>parseInt(end))||(parseInt(text)<parseInt(start)&&parseInt(va)>=parseInt(end))||(parseInt(text)<=parseInt(start)&&parseInt(va)>parseInt(end))||(parseInt(text)==parseInt(start)&&parseInt(va)==(end))){
//				repeatArr[index]=true;
//				alert("时间段已重复，请重新设置！");
//				return;
//			}
//		}
//	}
//	arr[index]=text+":"+va;
//	repeatArr[index]=false;
}

function subText($obj){
	var value=$obj.val();
	if(/[^\d\,\+]/.test(value)){
		value=value.replace(/[^\d\,\+]/g,'');
		$obj.val(value);
	}
	if(value.length>119){
		$obj.val(value.substr(0,219));
	}
}

//弹出区域列表
function openAreaChoose() {
	//$('#areaDiv').attr('display','');
	$('#areaDiv').dialog('open');

}

//点击确定赋值处理
function doArea() {
	$('#areaDiv').dialog('close');
	fillAreaName();
}
//赋值处理
function fillAreaName(){
	var areaNameStr = "";
	var codeStr="";
	$('input[name="roleId"]:checked').each(function(){    
   		areaNameStr +=$(this).next().html()+",";
   		codeStr+=$(this).val()+",";
 	}); 
 	if(areaNameStr != ""){
 	    areaNameStr = areaNameStr.substring(0,areaNameStr.lastIndexOf(","));
 	    $("#areaName").attr("allAreaName",areaNameStr);
 	    $("#allAreaName").html(areaNameStr);
 	    var arr = areaNameStr.split(",");
 	    if(arr.length > 1){
 	    	areaNameStr = arr[0]+"...";
 	    }
 		$("#areaName").val(areaNameStr.replaceAll("&lt;","<").replaceAll("&gt;",">"));
 		$("#areaName").removeClass("fontColor");
 		$("#allAreas").val(codeStr);
 	}else{
 		$("#areaName").val(getJsLocaleMessage("ptjk","ptjk_jkxq_yw_13"));
 		$("#areaName").addClass("fontColor");
 		$("#areaName").attr("allRoleName","");
 		$("#allAreas").val("");
 	}
}

//检查手机的有效性
function validatePhones(obj){
	var phone = obj.val();
	if(phone.length>0){
		var tip = obj.parent().prev().text().replace(/[:： ]/g,'');
		var reg=/^1[3458][0-9]{9}(\,1[3458][0-9]{9}){0,9}$/;
		if(phone.replaceAll(",","").length<=6||/[^\d\,\+]/.test(phone)){
			isContinue = false;
			alert(tip+getJsLocaleMessage("ptjk","ptjk_wljk_web_9"));
			return false;
		}else{
			//处理+号
			var phoneStr=phone.replaceAll("\\+","s");
			var p = phone.split(',');
			var msg = '';
			if(p.length>10)
			{
				alert(tip+getJsLocaleMessage("ptjk","ptjk_wljk_web_10"));
				return false;
			}
			for(var i=0;i<p.length;i++){
				if(!checkPhone(p[i]))
				{
					alert(tip+getJsLocaleMessage("ptjk","ptjk_wljk_web_9"));
					return false;
				}
				var pp=p[i].replaceAll("\\+","s");
				//var matchs = phoneStr.match(new RegExp(pp+",|,"+pp+"$",'g'));
				if(msg.indexOf(p[i])==-1){
					var countRep=0;
					for(var j=0;j<p.length;j++)
					{
						if(pp==p[j].replaceAll("\\+","s"))
						{
							countRep++;
						}
						if(countRep>1)
						{
							if(msg.length>0){
								msg+="，";
							}
							msg+=p[i];
						}
					}
				}
			}
			if(msg.length>0){
				msg=msg.replaceAll("s","\+");
				alert(tip+msg+getJsLocaleMessage("ptjk","ptjk_wljk_web_11"));
				return false;
			}
		}
	}
	return true;
}

//验证邮箱是否有效
function checkEmail(email_str)
{
	var arr = [ "ac", "com", "net", "org", "edu", "gov", "mil", "ac\.cn",
		"com\.cn", "net\.cn", "org\.cn", "edu\.cn" ];
	var temp_arr = arr.join("|");
	// reg
	var reg_str = "^[0-9a-zA-Z](\\w|-)*@\\w+\\.(" + temp_arr + ")$";
	var reg = new RegExp(reg_str);
	if (reg.test(email_str)) {
		return true;
	}else{
		return false;
	}
}

//返回
function doreturn()
{
	var lguserid = GlobalVars.lguserid;
	var lgcorpcode = GlobalVars.lgcorpcode;
	window.location.href="mon_busCfg.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
}

//联动处理
function name_change()
{
//keyup click触发事件
var code=$('#busName option:selected').val();
var name=$('#busName option:selected').text();
$("#busCode").find("option[value='"+code+"']").attr("selected",true);
$('#code').val(code);
$('#name').val(name)
}

//联动处理
function code_change(){
	//keyup click触发事件
	var code=$('#busCode option:selected').val();
	$("#busName").find("option[value='"+code+"']").attr("selected",true);
	var name=$('#busName option:selected').text();
	$('#code').val(code);
	$('#name').val(name);
	
}



//时间控件 结束时间
function rtime()
{
    var max = "2099-12-31 23:59:59";
    var v = $("#begintime").attr("value");
	if(v.length != 0)
	{
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		var day = 31;
		if (mon != "12")
		{
		    mon = String(parseInt(mon,10)+1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		    switch(mon){
		    case "01":day = 31;break;
		    case "02":day = 28;break;
		    case "03":day = 31;break;
		    case "04":day = 30;break;
		    case "05":day = 31;break;
		    case "06":day = 30;break;
		    case "07":day = 31;break;
		    case "08":day = 31;break;
		    case "09":day = 30;break;
		    case "10":day = 31;break;
		    case "11":day = 30;break;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)+1));
		    mon = "01";
		}
		max = year+"-"+mon+"-"+day+" 23:59:59"
    }
	WdatePicker({dateFmt:'yyyy-MM-dd',minDate:v});
}

//时间控件 开始时间
function stime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#endtime").attr("value");
    var min = "1900-01-01 00:00:00";
	if(v.length != 0)
	{
	    max = v;
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		if (mon != "01")
		{
		    mon = String(parseInt(mon,10)-1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)-1));
		    mon = "12";
		}
		min = year+"-"+mon+"-01 00:00:00"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:max});
}

