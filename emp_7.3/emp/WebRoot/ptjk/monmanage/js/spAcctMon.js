//设置阀值
function toEdit(spaccountid)
{
	if(spaccountid==null || spaccountid=="null" || spaccountid=="")
	{
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_1"));
	}
	$("#editSpAcct").find("input[name='spaccountid']").val(spaccountid);
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	$.post("mon_spAcctMonCfg.htm",
	{method:"toEdit",lgcorpcode:lgcorpcode,spaccountid:spaccountid,lguserid: lguserid,isAsync:"yes"},
	function(result){
		if(result == "outOfLogin")
		{
			$("#logoutalert").val(1);
			location.href=path+"/common/logoutEmp.html";
			return;
		}
		else if(result!="error")
		{
			var array = result.split("|");
			$("#editSpAcct").find("#rptrecvratio").val(array[0]);
			$("#editSpAcct").find("#morecvratio").val(array[1]);
			$("#editSpAcct").find("#mtremained").val(array[2]);
			$("#editSpAcct").find("#moremained").val(array[3]);
			$("#editSpAcct").find("#rptremained").val(array[4]);
			$("#editSpAcct").find("#monstatus option[value='"+array[5]+"']").attr('selected','selected');
			$("#editSpAcct").find("#monphone").val(array[6]);
			$("#editSpAcct").find("#monemail").val(array[20]);
			$("#editSpAcct").find("#offlinethreshd").val(array[7]);
			var selectfrom1=array[8];
			var selectto1=array[9];
			var duration1=array[10];
			var selectfrom2=array[11];
			var selectto2=array[12];
			var duration2=array[13];
			var selectfrom3=array[14];
			var selectto3=array[15];
			var duration3=array[16];
			var selectfrom4=array[17];
			var selectto4=array[18];
			var duration4=array[19];
			//移除之前的时间段
			$(".trClass").each(function(i)
			{
				$(this).remove();
			});
			if(selectto1!=0&&duration1!=0){
				appendLine(selectfrom1,selectto1,duration1);
			}
			if(selectto2!=0&&duration2!=0){
				appendLine(selectfrom2,selectto2,duration2);
			}
			if(selectto3!=0&&duration3!=0){
				appendLine(selectfrom3,selectto3,duration3);
			}
			if(selectto4!=0&&duration4!=0){
				appendLine(selectfrom4,selectto4,duration4);
			}
			$("#editSpAcct").dialog("open");
		}
		else
		{
			alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_2"));
		}
	});
}

function edit()
{
	var spaccountid = $("#editSpAcct").find("input[name='spaccountid']").val();
	if(spaccountid==null || spaccountid=="null" || spaccountid=="")
	{
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_1"));
	}
	if(!validate($("#editSpAcct").children(".edit_spacct"))){
		return false;
	}
	var offlinethreshd=$("#editSpAcct").find("#offlinethreshd").val();
	if(offlinethreshd!=null&&offlinethreshd>1440)
	{
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_3"));
		return false;
	}
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	var rptrecvratio=$("#editSpAcct").find("#rptrecvratio").val();
	var morecvratio=$("#editSpAcct").find("#morecvratio").val();
	var mtremained=$("#editSpAcct").find("#mtremained").val();
	var moremained=$("#editSpAcct").find("#moremained").val();
	var rptremained=$("#editSpAcct").find("#rptremained").val();
	var monstatus=$("#editSpAcct").find("#monstatus").val();
	var monphone=$("#editSpAcct").find("#monphone").val();
	var monemail=$("#editSpAcct").find("#monemail").val();
	
	if(monemail==" "){monemail="";}
	//告警邮箱为空 则 进行确认
	if((monemail==null || monemail.length==0) && !confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_3"))){
		return false;
	}
	//检查时间段
	if(!check_time()){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_4"));
		return false;
	}
	var selectfrom1=$("#editSpAcct").find("#selectfrom1").val();
	var selectto1=$("#editSpAcct").find("#selectto1").val();
	var duration1=$("#editSpAcct").find("#duration1").val();
	var selectfrom2=$("#editSpAcct").find("#selectfrom2").val();
	var selectto2=$("#editSpAcct").find("#selectto2").val();
	var duration2=$("#editSpAcct").find("#duration2").val();
	var selectfrom3=$("#editSpAcct").find("#selectfrom3").val();
	var selectto3=$("#editSpAcct").find("#selectto3").val();
	var duration3=$("#editSpAcct").find("#duration3").val();
	var selectfrom4=$("#editSpAcct").find("#selectfrom4").val();
	var selectto4=$("#editSpAcct").find("#selectto4").val();
	var duration4=$("#editSpAcct").find("#duration4").val();
	if(selectfrom1!=''&&selectfrom1!=undefined&&selectto1!=''&&selectto1!=undefined){
		var duration1max=(parseInt(selectto1)-parseInt(selectfrom1))*60;
		if(duration1!=''&&duration1!=undefined){
			var duration1num=parseInt(duration1);
			if(duration1num<1)
			{
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_5"));
				return false;
			}else if(duration1num>duration1max){
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_6")+duration1max+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_7"));
				return false;
			}
		}else
		{
			alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_8"));
			return false;
		}
	}
	if(selectfrom2!=''&&selectfrom2!=undefined&&selectto2!=''&&selectto2!=undefined){
		var duration2max=(parseInt(selectto2)-parseInt(selectfrom2))*60;
		if(duration2!=''&&duration2!=undefined){
			var duration2num=parseInt(duration2);
			if(duration2num<1)
			{
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_5"));
				return false;
			}else if(duration2num>duration2max){
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_6")+duration2max+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_7"));
				return false;
			}
		}else
		{
			alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_8"));
			return false;
		}
	}
	if(selectfrom3!=''&&selectfrom3!=undefined&&selectto3!=''&&selectto3!=undefined){
		var duration3max=(parseInt(selectto3)-parseInt(selectfrom3))*60;
		if(duration3!=''&&duration3!=undefined){
			var duration3num=parseInt(duration3);
			if(duration3num<1)
			{
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_5"));
				return false;
			}else if(duration3num>duration3max){
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_6")+duration3max+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_7"));
				return false;
			}
		}else
		{
			alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_8"));
			return false;
		}
	}
	if(selectfrom4!=''&&selectfrom4!=undefined&&selectto4!=''&&selectto4!=undefined){
		var duration4max=(parseInt(selectto4)-parseInt(selectfrom4))*60;
		if(duration4!=''&&duration4!=undefined){
			var duration4num=parseInt(duration4);
			if(duration4num<1)
			{
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_5"));
				return false;
			}else if(duration4num>duration4max){
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_6")+duration4max+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_7"));
				return false;
			}
		}else
		{
			alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_8"));
			return false;
		}
	}
	
	
	$("#editSpAcct").find("input type='button'").attr("disabled","disabled");
	$.post("mon_spAcctMonCfg.htm",
	{method:"edit",
	lgcorpcode:lgcorpcode,
	lguserid: lguserid,
	spaccountid:spaccountid,
	rptrecvratio:rptrecvratio,
	morecvratio:morecvratio,
	mtremained:mtremained,
	moremained:moremained,
	rptremained:rptremained,
	monstatus:monstatus,
	monphone:monphone,
	monemail:monemail,
	offlinethreshd:offlinethreshd,
	selectfrom1:selectfrom1,
	selectto1:selectto1,
	duration1:duration1,
	selectfrom2:selectfrom2,
	selectto2:selectto2,
	duration2:duration2,
	selectfrom3:selectfrom3,
	selectto3:selectto3,
	duration3:duration3,
	selectfrom4:selectfrom4,
	selectto4:selectto4,
	duration4:duration4,
	isAsync:"yes"},
	function(result){
		$("#editSpAcct").find("input type='button'").attr("disabled","");
		if(result == "outOfLogin")
		{
			$("#logoutalert").val(1);
			location.href=path+"/common/logoutEmp.html";
			return;
		}
		else if(result!="error")
		{
			if(result=="phoneError")
			{
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_4"));
			}else if(result=="emailError"){
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_5"));
			}
			else
			{
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_9"));
				$("#editSpAcct").dialog("close");
				$("#pageForm").find("#search").click();
			}
		}
		else
		{
			alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_10"));
		}
	});
}

function doreturn()
{
	$("#editSpAcct").dialog("close");
}
function validate($table){
	var isContinue=true;
	//文本框各值验证
	$table.find(":text").each(function(){
		var value=$(this).val();
		if(value.length>0&&$(this).hasClass("percent")&&value>100){
			isContinue = false;
			alert($(this).parent().prev().text().replace(/[:： ]/g,'')+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_11"));
			return false;
		}
	});
	if(!isContinue){
		return false;
	}
	//判断手机号码格式
	var phone=$("#monphone").val();
	if(!validatePhones($("#monphone"))){
		return false;
	}
	//告警手机号为空 则 进行确认
	if(phone.length==0&&!confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_9"))){
		return false;
	}
	
	return true;
}

//检查所有行是否存在重复
function check_time(){
	var  arr=new Array(); 
	for(var i=1;i<5;i++){
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

function appendLine(selectfrom,selectto,duration){
	var tbody = $("#tbody");
	var sontr=tbody.find("tr");
	if(sontr.length==4){
		alert(getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_12"));
		return;
	}
	//计算行数
	var linecount=sontr.length+1;
	tbody.append("<tr id='tr"+linecount+"' class='trClass' style='height: 24px;'><td  style='width: 10px;'></td><td style='width: 170px;'>"+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_13") +
			"<select id='selectfrom"+linecount+"' name='selectfrom"+linecount+"' style='width: 50px;' onchange='from_change(this)'><option>0</option>" +
			"<option>1</option><option>2</option><option>3</option>" +
			"<option>4</option><option>5</option><option>6</option>" +
			"<option>7</option><option>8</option><option>9</option>" +
			"<option>10</option><option>11</option>	<option>12</option>" +
			"<option>13</option><option>14</option><option>15</option>" +
			"<option>16</option><option>17</option><option>18</option>" +
			"<option>19</option><option>20</option><option>21</option>" +
			"<option>22</option></select>"+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_14") +
			"<select id='selectto"+linecount+"' name='selectto"+linecount+"' style='width: 50px;' onchange='to_change(this)'><option>1</option>" +
			"<option>2</option><option>3</option><option>4</option>" +
			"<option>5</option><option>6</option>" +
			"<option>7</option><option>8</option><option>9</option>" +
			"<option>10</option><option>11</option>" +
			"<option>12</option><option>13</option>" +
			"<option>14</option><option>15</option>" +
			"<option>16</option><option>17</option>" +
			"<option>18</option><option>19</option>" +
			"<option>20</option><option>21</option>" +
			"<option>22</option><option>23</option></select></td>"+
			"<td style='width: 200px;'>"+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_15")+"<input type='text' style='width: 80px;' name='duration"+linecount+"' maxlength='4' id='duration"+linecount+"' value='"+duration+"' class='input_bd int' onkeyup='numberControl($(this))' onblur='numberControl($(this))' onchange='parseNumber($(this))'/>" +
			"<span style='color: #cccccc;font-size: 14px;' >"+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_16")+"</span></td>"+
			"<td   id='del'><a id='del'  onclick='javascript:del("+linecount+")'><span style='color:blue;cursor:pointer;' >"+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_17")+"</a></font></td></tr>");
	$('#selectfrom'+linecount).val(selectfrom);
	$('#selectto'+linecount).val(selectto);
	$("#selectto"+linecount+" option[value='"+selectto+"']").attr("selected", true);
	$(".trClass").each(function(i){
		   $(this).children("td").eq(0).text((i+1)+"、");
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
}
