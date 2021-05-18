//ie浏览器兼容性
var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0");
var isIE7 = isIE && (navigator.appVersion.match(/MSIE 7.0/i)=="MSIE 7.0");
//添加行
function addLine(){
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
			"<td style='width: 200px;'>"+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_15")+"<input type='text' style='width: 80px;' name='duration"+linecount+"' maxlength='4' id='duration"+linecount+"' value='' class='input_bd int' onkeyup='numberControl($(this))' onblur='numberControl($(this))' onchange='parseNumber($(this))'/>" +
			"<span style='color: #cccccc;font-size: 14px;' >"+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_16")+"</span></td>"+
			"<td   id='del'><a id='del'  onclick='javascript:del("+linecount+")'><span style='color:blue;cursor:pointer;' >"+getJsLocaleMessage("ptjk","ptjk_jkgl_spzh_17")+"</a></font></td></tr>");
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

//处理0开头的的情况
function parseNumber(obj){
	//obj.val(parseInt(obj.val()));
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
}

//检查当前选择行与其他行是否重复 
function check_line(index){
	var  arr=new Array(); 
	//排除本行，把其他行不是重复的值放在数组里面
	for(var i=1;i<5;i++){
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

//点击删除处理
function del(idtr) 
{
	$("#tr"+idtr).remove();
	var tbody = $("#tbody");
	var sontr=tbody.find("tr");
//	if(sontr.length<9){
//		$("#content").css("width","900px");
//		$("#opreate").css("width","152px");
//	}
}