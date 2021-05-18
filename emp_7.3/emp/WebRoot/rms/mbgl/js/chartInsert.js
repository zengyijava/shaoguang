var echartPie_chart;
var echartBar_chart;
var echartLine_chart;

$(function () {
    var $selectedLi = $("#chartType").next(".c_selectBox").find("ul.c_result li");
    $selectedLi.each(function () {
        var value = $(this).html();
        var intVal;
        switch (value){
            case getJsLocaleMessage("rms","rms_myscene_piechart"):intVal = 1;break;
            case getJsLocaleMessage("rms","rms_mbgl_barchart"):intVal = 2;break;
            case getJsLocaleMessage("rms","rms_mbgl_linechart"):intVal = 3;break;
            case getJsLocaleMessage("rms","rms_mbgl_salarybar"):intVal = 4;break;
            case getJsLocaleMessage("rms","rms_mbgl_blank"):intVal = 5;break;
            default:intVal = 1;
        }
        $(this).click(function () {
            chartChange(intVal);
            /*$("#chartType[value='"+ intVal +"']").css("selected",true);*/
            $("#chartType").val(intVal);
        });
    });
});

function ShowElement(element){
    var oldhtml = element.innerHTML;
    var newobj = document.createElement('input');
    //创建新的input元素
    newobj.type = 'text';
    newobj.style = 'width:40px';
    newobj.id ='addInput';
    newobj.value ='';
    var type=$('input[name="ptType"]:checked ').val();
    element.innerHTML = '';
    element.appendChild(newobj);
    newobj.focus();
    //为新增元素添加类型
    newobj.onblur = function(){
        element.innerHTML = this.value ? this.value : oldhtml;
    }
    newobj.onchange = function(){
        var tvalmum;
        //统计input输入字段的长度
        tvalnum = $(this).val().length;
        //如果大于8个字直接进行字符串截取
        if(tvalnum>15){
            var tval = $(this).val();
            tval = tval.substring(0,15);
            $(this).val(tval);
            alert(getJsLocaleMessage("rms","rms_mbgl_maxlength15"));
        }
        changes(this.value);
    }
}
var old_chartTye;
$("#chartType").click(function(){
    //存储select修改之前的值
    old_chartTye=$(this).val();
});

function chartChange(val){
    var value = val !== "" && val !== "null" && val !== undefined ? val : $("#chartType").val();
    var str = "staticState,dynamic,staticState_bar,dynamic_bar,staticState_line,dynamic_line";
    var type=$('input[name="ptType"]:checked ').val();
    if(value == 1){
        $(".pie_Chart,#pie_Chart").css({"display":""});
        $(".bar_Chart,#bar_Chart").css({"display":"none"});
        $(".line_Chart,#line_Chart").css({"display":"none"});
        newPie(type,null,null,null);
        if(type == 1){
            exhibition(str,'0');
            dynamicData("1",null);
        }else{
            exhibition(str,'1');
            dynamicData("2",null);
        }
    }else if(value == 2){
        $(".pie_Chart,#pie_Chart").css({"display":"none"});
        $(".bar_Chart,#bar_Chart").css({"display":""});
        $(".line_Chart,#line_Chart").css({"display":"none"});
        newBarOrLine(type,"2",null,null);
        if(type == 1){
            exhibition(str,'2');
            bar_dynamicData('1',null);
        }else{
            exhibition(str,'3');
            bar_dynamicData('2',null);
        }
    }else if(value == 3){
        $(".pie_Chart,#pie_Chart").css({"display":"none"});
        $(".bar_Chart,#bar_Chart").css({"display":"none"});
        $(".line_Chart,#line_Chart").css({"display":""});
        newBarOrLine(type,"3",null,null);
        if(type == 1){
            exhibition(str,'4');
            line_dynamicData('1',null);
        }else{
            exhibition(str,'5');
            line_dynamicData('2',null);
        }
    }else{
        alert(getJsLocaleMessage("rms","rms_mbgl_developing"));
        $("#chartType").val(old_chartTye);
    }

}
function add(obj,val){
    var num = $(obj.parentNode).parent().parent().children("tr").length+1;
    if(num>12){
        alert(getJsLocaleMessage("rms","rms_mbgl_max12rows"));
        return
    }
    for(var i=0;i<num-1;i++){
        var value = $(obj.parentNode).parent().parent().children("tr").eq(i).children("td").eq(0).text();
        if(value.indexOf(num) != -1){
            num ++;
            break;
        }
    }
    if(val==1){
        $(obj.parentNode).parent().after("<tr><td ondblclick='ShowElement(this)' style='cursor: pointer;' style='cursor: pointer;'>" + getJsLocaleMessage("rms","rms_mbgl_rowtitle")+num+"</td><td ondblclick='ShowElement(this)' style='cursor: pointer;'>0</td><td><button onclick='add(this,"+val+");' title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick='removeRow(this,"+val+")' title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td></tr>");
        useEchart("1",null);
    }else if(val==2){
        var colValue = num+getJsLocaleMessage("rms","rms_report_row1col");
        $(obj.parentNode).parent().after("<tr><td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+getJsLocaleMessage("rms","rms_mbgl_rowtitle")+num+"</td><td style='color:#cccccc'>{#"+colValue+"#}</td><td><button onclick='add(this,"+val+");' title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick='removeRow(this,"+val+")' title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td></tr>");
        for(var i=0;i<num;i++){
            $(obj.parentNode).parent().parent().children("tr").eq(i).children("td").eq(1).html("{#"+(i+1)+getJsLocaleMessage("rms","rms_report_row1col")+"#}");
        }
        useEchart("2",null);
    }else{
        $(obj.parentNode).parent().after("<tr><td style='color:#cccccc'>{#"+getJsLocaleMessage("rms","rms_mbgl_rowtitle")+num+"#}</td><td style='color:#cccccc'>{#"+colValue+"#}</td><td><button onclick='add(this,"+val+");' title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick='removeRow(this,"+val+")' title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td></tr>");
        for(var i=0;i<num;i++){
            $(obj.parentNode).parent().parent().children("tr").eq(i).children("td").eq(0).html("{#"+getJsLocaleMessage("rms","rms_mbgl_rowtitle")+(i+1)+"#}");
            $(obj.parentNode).parent().parent().children("tr").eq(i).children("td").eq(1).html("{#"+(i+1)+getJsLocaleMessage("rms","rms_report_row1col")+"#}");
        }
        useEchart("2",null);
    }
}
function removeRow(obj,num){
    var type = $('input[name="ptType"]:checked ').val();
    var chartType = $("#chartType").val();
    var ids = "";
    if(chartType==1){
        if(type==1){
            ids = "staticStateTable";
        }else{
            ids = "dynamicTable";
        }
    }else if(chartType==2){
        if(type==1){
            ids = "staticState_bar_table";
        }else{
            ids = "dynamic_bar_table";
        }
    }else if(chartType==3){
        if(type==1){
            ids = "staticState_line_table";
        }else{
            ids = "dynamic_line_table";
        }
    }
    var trNum = $("#"+ids+" tr").length;
    var tdNum = $("#"+ids+" tr").eq(0).children("td").length;
    if((trNum==2&&chartType==1)||(trNum==3&&chartType==3)){
        alert(getJsLocaleMessage("rms","rms_mbgl_least2row"));
        return;
    }else if(trNum==3&&chartType!=1&&tdNum==3){
        alert(getJsLocaleMessage("rms","rms_mbgl_least2r1c"));
        return;
    }else if(trNum==2&&chartType==2&&tdNum>3){
        alert(getJsLocaleMessage("rms","rms_mbgl_least1r2c"));
        return;
    }
    var res = confirm(getJsLocaleMessage("rms","rms_mbgl_confirmdel2"));
    if(res == true){
        $(obj.parentNode).parent().remove();
        if(chartType==1){
            if(type==2||type==3){
                for(var i=0;i<trNum;i++){
                    if(type==3){
                        $("#"+ids+" tr").eq(i).children("td").eq(0).html("{#"+getJsLocaleMessage("rms","rms_mbgl_rowtitle")+(i+1)+"#}");
                    }
                    for(j=1;j<tdNum-1;j++){
                        $("#"+ids+" tr").eq(i).children("td").eq(j).html("{#"+(i+1)+getJsLocaleMessage("rms","rms_mbgl_row")+j+getJsLocaleMessage('rms','rms_mbgl_col')+"#}");
                    }
                }
            }
        }else{
            if(type==2||type==3){
                for(var i=1;i<trNum;i++){
                    if(type==3){
                        $("#"+ids+" tr").eq(i).children("td").eq(0).html("{#"+getJsLocaleMessage("rms","rms_mbgl_rowtitle")+i+"#}");
                    }
                    for(j=1;j<tdNum-1;j++){
                        $("#"+ids+" tr").eq(i).children("td").eq(j).html("{#"+i+getJsLocaleMessage("rms","rms_mbgl_row")+j+getJsLocaleMessage('rms','rms_mbgl_col')+"#}");
                    }
                }
            }
        }
        if(num == 1){
            useEchart("1",null);
        }else{
            useEchart("2",null);
        }
    }
}
//调用echart(type:数据类型，obj:更改的值)
function useEchart(type,obj){
    if($("#chartType").val()==1){
        dynamicData(type,obj);
    }else if($("#chartType").val()==2){
        bar_dynamicData(type,obj);
    }else if($("#chartType").val()==3){
        line_dynamicData(type,obj);
    }
}
//更改标题
function change(){
    var type=$('input[name="ptType"]:checked').val();
    useEchart(type,obj);
}
function changes(obj){
    var type=$('input[name="ptType"]:checked').val();
    useEchart(type,obj);
}

function toAction(){
    var type=$('input[name="ptType"]:checked').val();
    var str = "staticState,dynamic,staticState_bar,dynamic_bar,staticState_line,dynamic_line";
    if(type==1){
        if($("#chartType").val()==1){
            exhibition(str,'0');
            dynamicData("1",null);
            newPie(type,null,null,null);
        }else if($("#chartType").val()==2){
            exhibition(str,'2');
            bar_dynamicData("1",null);
            newBarOrLine(type,"2",null,null);
        }else if($("#chartType").val()==3){
            exhibition(str,'4');
            line_dynamicData("1",null);
            newBarOrLine(type,"3",null,null);
        }
    }else{
        if($("#chartType").val()==1){
            exhibition(str,'1');
            dynamicData("2",null);
            newPie(type,null,null,null);
        }else if($("#chartType").val()==2){
            exhibition(str,'3');
            bar_dynamicData("2",null);
            newBarOrLine(type,"2",null,null);
        }else if($("#chartType").val()==3){
            exhibition(str,'5');
            line_dynamicData("2",null);
            newBarOrLine(type,"3",null,null);
        }
    }
}
//饼状图动态加载数据
function dynamicData(num,obj){
    echartPie_chart = echarts.init(document.getElementById("echartPie"));
    var title = $("#chartTitle").val();
    echartPie.title.text=title;
    echartPie.series["0"].name=title;
    var a = new Array();
    //var colors = "#ffdb6b,#ef7f6a,#bed432,#c0b643,#7ac043,#28d96d,#18e0d4,#27e3ff,#00caf5,#5fb2ff,#00b4ff,#3b7eff,#4c58ed,#6d5bf5,#ac70f9,#ef7f6a,#b34eb1,#ff6b95,#f5505f";
    var colors = "#37a2da,#ff9f7e,#66e1e3,#ffdb5c,#fb7293,#97bfff,#e162af,#9fe7b9,#e791d1,#e7bdf3,#32c5e9,#9d97f5,#8378eb";
    var colorval = colors.split(",");
    if(num ==1){
        var trLen = $("#staticState table tr").length;
        var data = new Array();;
        for(i=0;i<trLen;i++){

            var val = $("#staticState table tr").eq(i).children("td").eq(1).text();
            if(val == ""){
                val = obj;
            }
            var nameval = $("#staticState table tr").eq(i).children("td").eq(0).text();
            if(nameval == ""){
                nameval = obj;
            }
            data.push(nameval);
            a.push({value:val,name:nameval,itemStyle:{normal:{color:colorval[i]}}});
        }
    }else{
        var trLen = $("#dynamic table tr").length;
        var data = new Array();
        for(i=0;i<trLen;i++){
            var nameval = $("#dynamic table tr").eq(i).children("td").eq(0).text();
            if(nameval == ""){
                nameval = obj;
            }
            data.push(nameval);
            a.push({value:1,name:nameval,itemStyle:{normal:{color:colorval[i]}}});
        }
    }
    echartPie.legend.data = data;
    echartPie.series["0"].data = a;
    $("#echartPie").css({"height":"365px","width":"308px"});
    echartPie_chart.setOption(echartPie, true);
}

//柱状图动态加载数据
function bar_dynamicData(num,obj){
    //var colors = "#ffdb6b,#ef7f6a,#bed432,#c0b643,#7ac043,#28d96d,#18e0d4,#27e3ff,#00caf5,#5fb2ff,#00b4ff,#3b7eff,#4c58ed,#6d5bf5,#ac70f9,#ef7f6a,#b34eb1,#ff6b95,#f5505f";
    var colors = "#37a2da,#ff9f7e,#66e1e3,#ffdb5c,#fb7293,#97bfff,#e162af,#9fe7b9,#e791d1,#e7bdf3,#32c5e9,#9d97f5,#8378eb";
    var color = colors.split(",");
    if(num ==1){
        var ids = "staticState_bar";
    }else{
        var ids = "dynamic_bar";
    }
	if(echartBar_chart == undefined){
        echartBar_chart = echarts.init(document.getElementById("echartBar"));
    }
    var title = $("#chartTitle").val();
    echartBar.title.text=title;
    var xAix = new Array();
    var yAix = new Array();
    var serieData = new Array();
    var trLen = $("#"+ids+" table tr").length;
    var tdLen = $("#"+ids+" table tr").eq(0).children("td").length;

    for(var i=1;i<trLen;i++){
        var xAixs = $("#"+ids+" table tr").eq(i).children("td").eq(0).text();
        if(xAixs==""){
            xAixs = obj
        }
        xAix.push(xAixs);
    }
    for(var j=1;j<tdLen-1;j++){
        var yAixs = $("#"+ids+" table tr").eq(0).children("td").eq(j).text();
        if(yAixs==""){
            yAixs = obj
        }
        yAix.push(yAixs);
        var datas = new Array();
        for(var i=1;i<trLen;i++){
            var rowData = $("#"+ids+" table tr").eq(i).children("td").eq(j).text();
            if(rowData==""){
                rowData = obj;
            }
            if(num==2||num==3){
                datas.push(1);
            }else{
                datas.push(rowData);
            }
        }
        var widthVal=24-trLen-tdLen;
        if(tdLen==3&&trLen<8){
            widthVal = 40-trLen;
        }else if(tdLen==3&&trLen<10){
            widthVal = 32-trLen;
        }else if(tdLen==3&&trLen<12){
            widthVal = 30-trLen;
        }else if(tdLen==3&&trLen<14){
            widthVal = 28-trLen;
        }else if(tdLen>7&&trLen>4){
            widthVal = 24-trLen-tdLen-4;
            if(widthVal<2){
                widthVal=2;
            }
        }else if(tdLen<7&&trLen>4){
            widthVal = 24-trLen-tdLen-4;
            if(widthVal<2){
                widthVal=2;
            }
        }
        serieData.push({itemStyle:{normal:{color:color[j-1]}},name:yAixs,type:'bar',barWidth:widthVal,data:datas,barGap:'1%'});
    }
    echartBar.xAxis["0"].data = xAix;
    echartBar.legend.data = yAix;
    echartBar.series = serieData;
    echartBar_chart.setOption(echartBar, true);
}

//折线图动态加载数据
function line_dynamicData(num,obj){
    //var colors = "#ffdb6b,#ef7f6a,#bed432,#c0b643,#7ac043,#28d96d,#18e0d4,#27e3ff,#00caf5,#5fb2ff,#00b4ff,#3b7eff,#4c58ed,#6d5bf5,#ac70f9,#ef7f6a,#b34eb1,#ff6b95,#f5505f";
    var colors = "#37a2da,#ff9f7e,#66e1e3,#ffdb5c,#fb7293,#97bfff,#e162af,#9fe7b9,#e791d1,#e7bdf3,#32c5e9,#9d97f5,#8378eb";
    var color = colors.split(",");
    if(num ==1){
        var ids = "staticState_line";
    }else{
        var ids = "dynamic_line";
    }
    if(echartLine_chart == undefined){
        echartLine_chart = echarts.init(document.getElementById("echartLine"));
    }
    var title = $("#chartTitle").val();
    echartLine.title.text=title;
    var xAix = new Array();
    var yAix = new Array();
    var serieData = new Array();
    var trLen = $("#"+ids+" table tr").length;
    var tdLen = $("#"+ids+" table tr").eq(0).children("td").length;

    for(var i=1;i<trLen;i++){
        var xAixs = $("#"+ids+" table tr").eq(i).children("td").eq(0).text();
        if(xAixs==""){
            xAixs = obj
        }
        xAix.push(xAixs);
    }
    for(var j=1;j<tdLen-1;j++){
        var yAixs = $("#"+ids+" table tr").eq(0).children("td").eq(j).text();
        if(yAixs==""){
            yAixs = obj
        }
        yAix.push(yAixs);
        var datas = new Array();
        for(var i=1;i<trLen;i++){
            var rowData = $("#"+ids+" table tr").eq(i).children("td").eq(j).text();
            if(rowData==""){
                rowData = obj;
            }
            if(num==2||num==3){
                datas.push(1);
            }else{
                datas.push(rowData);
            }
        }
        serieData.push({name:yAixs,type:'line',symbolSize:0,smooth:true,data:datas,itemStyle:{normal:{color:color[j-1]}}});
    }
    echartLine.xAxis["0"].data = xAix;
    echartLine.legend.data = yAix;
    echartLine.series = serieData;
    echartLine_chart.setOption(echartLine, true);
}

function exhibition(str,num){
    var strs = str.split(",");
    for(var i=0;i<strs.length;i++){
        if(i == num){
            $("#"+strs[i]).css({"display":""});
        }else{
            $("#"+strs[i]).css({"display":"none"});
        }
    }
    if($("#chartType").val()==1){
        $(".pie_chart,#pie_chart").show();
        $(".bar_chart,#bar_chart").hide();
        $(".line_chart,#line_chart").hide();
    }else if($("#chartType").val()==2){
        $(".pie_chart,#pie_chart").hide();
        $(".bar_chart,#bar_chart").show();
        $(".line_chart,#line_chart").hide();
    }else if($("#chartType").val()==3){
        $(".pie_chart,#pie_chart").hide();
        $(".bar_chart,#bar_chart").hide();
        $(".line_chart,#line_chart").show();
    }
}
function display(){
    $(".box").show();
}
function disappear(){
    $(".box").hide();
}
//增加列
function barAddrow(obj,val){
    var trLen=$("#"+obj+" tr").length;
    var tdLen = $("#"+obj+" tr").eq(0).children("td").length;
    var num = $("#"+obj+" tr").eq(0).children("td").length-1;
    if(num>12){
        alert(getJsLocaleMessage("rms","rms_mbgl_max12cols"));
        return;
    }
    for(var i=0;i<num;i++){
        var value = $("#"+obj+" tr").eq(0).children("td").eq(i).text();
        if(value.indexOf(num) != -1){
            num ++;
            break;
        }
    }
    if(val==1){//静态
        for(var i=0;i<trLen;i++){
            if(i==0){
                $("#"+obj+" tr").eq(i).children("td").eq(tdLen-2).after("<td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+getJsLocaleMessage("rms","rms_mbgl_coltitle")+num+"</td>");
            }else{
                $("#"+obj+" tr").eq(i).children("td").eq(tdLen-2).after("<td ondblclick='ShowElement(this)' style='cursor: pointer;'>0</td>");
            }
        }
        if($("#chartType").val()==2){
            bar_dynamicData('1',null);
        }else if($("#chartType").val()==3){
            line_dynamicData('1',null);
        }
    }else{
        for(var i=0;i<trLen;i++){
            if(i==0){//第一行（列标题）
                if(val==2){//数值动态
                    $("#"+obj+" tr").eq(i).children("td").eq(tdLen-2).after("<td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+getJsLocaleMessage("rms","rms_mbgl_coltitle")+num+"</td>");
                }else{//全动态
                    $("#"+obj+" tr").eq(i).children("td").eq(tdLen-2).after("<td style='color:#ccc'>{#"+getJsLocaleMessage("rms","rms_mbgl_coltitle")+num+"#}</td>");
                }
            }else{
                var rowVal = $("#"+obj+" tr").eq(i).children("td").eq(0).text();
                $("#"+obj+" tr").eq(i).children("td").eq(tdLen-2).after("<td style='color:#ccc'>{#"+i+getJsLocaleMessage("rms","rms_mbgl_row")+num+getJsLocaleMessage('rms','rms_mbgl_col')+"#}</td>");
            }
        }
        if($("#chartType").val()==2){
            bar_dynamicData('2',null);
        }else if($("#chartType").val()==3){
            line_dynamicData('2',null);
        }
    }
}
//增加行
function barAddcol(obj,val){
    var tdLen = $(obj.parentNode).parent().eq(0).children("td").length;
    var num =  $(obj.parentNode).parent().parent().children("tr").length;
    if(num>12){
        alert(getJsLocaleMessage("rms","rms_mbgl_max12rows"));
        return;
    }
    for(var i=0;i<num;i++){
        var value = $(obj.parentNode).parent().parent().children("tr").eq(0).children("td").eq(i).text();
        if(value.indexOf(num) != -1){
            num ++;
            break;
        }
    }
    if(val==1){//静态
        var col = "<tr><td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+getJsLocaleMessage("rms","rms_mbgl_rowtitle")+num+"</td>";
        for(var i=1;i<tdLen-1;i++){
            col += "<td ondblclick='ShowElement(this)' style='cursor: pointer;'>0</td>";
        }
        col +="<td><button onclick=\"barAddcol(this,'1');\" title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"removeRow(this,'1')\" title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td></tr>";
        $(obj.parentNode).parent().after(col);
        if($("#chartType").val()==2){
            bar_dynamicData('1',null);
        }else if($("#chartType").val()==3){
            line_dynamicData('1',null);
        }
    }else{
        if(val == 2){//数值动态
            var col = "<tr><td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+getJsLocaleMessage("rms","rms_mbgl_rowtitle")+num+"</td>";
        }else{//全动态
            var col = "<tr><td style='color:#ccc'>{#"+getJsLocaleMessage("rms","rms_mbgl_rowtitle")+num+"#}</td>";
        }
        for(var i=1;i<tdLen-1;i++){
            col += "<td style='color:#ccc'>{#"+num+getJsLocaleMessage("rms","rms_mbgl_row")+i+getJsLocaleMessage('rms','rms_mbgl_col')+"#}</td>";
        }
        col +="<td><button onclick=\"barAddcol(this,"+val+");\" title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"removeRow(this,"+val+")\" title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td></tr>";
        $(obj.parentNode).parent().after(col);
        var tdLen = $(obj.parentNode).parent().eq(0).children("td").length;
        var num =  $(obj.parentNode).parent().parent().children("tr").length;
        for(var i=1;i<num;i++){
            if(val==3){
                $(obj.parentNode).parent().parent().children("tr").eq(i).children("td").eq(0).html("{#"+getJsLocaleMessage("rms","rms_mbgl_rowtitle")+i+"#}");
            }
            for(var j=1;j<tdLen-1;j++){
                $(obj.parentNode).parent().parent().children("tr").eq(i).children("td").eq(j).html("{#"+i+getJsLocaleMessage("rms","rms_mbgl_row")+j+getJsLocaleMessage('rms','rms_mbgl_col')+"#}");
            }
        }
        if($("#chartType").val()==2){
            bar_dynamicData('2',null);
        }else if($("#chartType").val()==3){
            line_dynamicData('2',null);
        }
    }
}

//删除列
function barRemoverow(obj,num){
    var tdLen = $("#"+obj + " tr").eq(0).children("td").length;
    var trLen = $("#"+obj + " tr").length;
    if(trLen==3&&tdLen==2){
        alert(getJsLocaleMessage("rms","rms_mbgl_least2r1c"));
        return;
    }
    if(tdLen==3){
        alert(getJsLocaleMessage("rms","rms_report_least1coldata"));
        return;
    }
    if(trLen==2&&tdLen==4){
        alert(getJsLocaleMessage("rms","rms_mbgl_least1r2c"));
        return;
    }
    var res = confirm(getJsLocaleMessage("rms","rms_report_confirmdelcol"));
    if(res == true){
        for(var i=0;i<trLen;i++){
            $("#"+obj+" tr").eq(i).children("td").eq(tdLen-2).remove();
        }
        if(num == 1){
            if($("#chartType").val()==2){
                bar_dynamicData('1',null);
            }else if($("#chartType").val()==3){
                line_dynamicData('1',null);
            }
        }else{
            if($("#chartType").val()==2){
                bar_dynamicData('2',null);
            }else if($("#chartType").val()==3){
                line_dynamicData('2',null);
            }
        }
    }
}
//初始化饼状图
function newPie(ptType,rowValue,barColName,chartTitle){
    if((ptType==1||ptType==2)&&rowValue==null){
        barColName = getJsLocaleMessage('rms','rms_report_rowtitle1to4');
        rowValue = '400,500,600,700';
    }else if(ptType==3 && rowValue==null){
        barColName = getJsLocaleMessage('rms','rms_report_rowtitletpl1');
        rowValue = '400,500,600,700';
    }
    /*if(chartTitle==null){
        chartTitle = "标题";
    }
    $("#chartTitle").val(chartTitle);*/
    var barColNames = barColName.split(",");
    var rowValues = rowValue.split(",");
    if(ptType==1){
        $("#staticStateTable").empty();
        for(var i =0;i<barColNames.length;i++){
            $("#staticStateTable").append("<tr><td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+barColNames[i]+"</td><td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+rowValues[i]+"</td><td><button onclick=\"add(this,'1');\" title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"removeRow(this,'1')\" title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td></tr>");
        }
        useEchart("1",null);
    }else{
        $("#dynamicTable").empty();
        for(var i =0;i<barColNames.length;i++){
            if(ptType==2){
                var value = (i+1)+getJsLocaleMessage("rms","rms_report_row1col");
                $("#dynamicTable").append("<tr><td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+barColNames[i]+"</td><td style='color:#cccccc;'>{#"+value+"#}</td><td style='width:72px;'><button onclick=\"add(this,'2');\" title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"removeRow(this,'2')\" title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td></tr>");
            }else{
                var value = "{#"+(i+1)+getJsLocaleMessage("rms","rms_report_row1col")+"#}";
                $("#dynamicTable").append("<tr><td style='color:#ccc;'>"+barColNames[i]+"</td><td style='color:#ccc;'>"+value+"</td><td style='width:72px;'><button onclick=\"add(this,'3');\" title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"removeRow(this,'3')\" title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td></tr>");
            }
        }
        useEchart("2",obj);
    }
}
//初始化柱状图，折线图
function newBarOrLine(ptType,chartType,barTableVal,chartTitle){
    if(ptType==1){
        if(chartType == 2){
            var ids = "staticState_bar_table";
        }else{
            var ids = "staticState_line_table";
        }
    }else{
        if(chartType == 2){
            var ids = "dynamic_bar_table";
        }else{
            var ids = "dynamic_line_table";
        }
    }
    /*if(chartTitle==null){
        chartTitle = "标题";
    }
    $("#chartTitle").val(chartTitle);*/
    if(ptType==1&&barTableVal==null){
        barTableVal = getJsLocaleMessage('rms','rms_report_rowtitletpl2');
    }else if(ptType==2&&barTableVal==null){
        barTableVal = getJsLocaleMessage('rms','rms_report_rowtitletpl3');
    }else if(ptType==3&&barTableVal==null){
        barTableVal = getJsLocaleMessage('rms','rms_report_rowtitletpl4');
    }
    var trLen = barTableVal.split("@");
    $("#"+ids).empty();
    for(var i=0;i<trLen.length;i++){
        var tdLen = trLen[i].split(",");
        $("#"+ids).append("<tr></tr>");
        for(var j=0;j<tdLen.length;j++){
            if(ptType==1){//静态
                $("#"+ids +" tr").eq(i).append("<td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+tdLen[j]+"</td>");
            }else{
                if(i==0){//（列标题）
                    if(ptType==2){//数值动态
                        $("#"+ids +" tr").eq(i).append("<td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+tdLen[j]+"</td>");
                    }else{//全动态
                        $("#"+ids +" tr").eq(i).append("<td style='color:#ccc;'>"+tdLen[j]+"</td>");
                    }
                }else{
                    if(j==0){//行标题
                        if(ptType==2){
                            $("#"+ids +" tr").eq(i).append("<td ondblclick='ShowElement(this)' style='cursor: pointer;'>"+tdLen[j]+"</td>");
                        }else{
                            $("#"+ids +" tr").eq(i).append("<td style='color:#ccc;'>"+tdLen[j]+"</td>");
                        }

                    }else{//中间数值
                        var rowValue = i + getJsLocaleMessage("rms","rms_mbgl_row");
                        var colValue = j + getJsLocaleMessage("rms","rms_mbgl_col");
                        $("#"+ids +" tr").eq(i).append("<td style='color:#ccc'>{#"+rowValue+colValue+"#}</td>");
                    }
                }
            }
        }
        if(i==0){
            if(ptType==1){
                $("#"+ids +" tr td:last").after("<td><button onclick=\"barAddrow('"+ids+"','1');\" title="+getJsLocaleMessage('rms','rms_mbgl_addcol')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"barRemoverow('"+ids+"','1')\" title="+getJsLocaleMessage('rms','rms_mbgl_delcol')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td>");
            }else if(ptType==2){
                $("#"+ids +" tr td:last").after("<td><button onclick=\"barAddrow('"+ids+"','2');\" title="+getJsLocaleMessage('rms','rms_mbgl_addcol')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"barRemoverow('"+ids+"','2')\" title="+getJsLocaleMessage('rms','rms_mbgl_delcol')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td>");
            }else{
                $("#"+ids +" tr td:last").after("<td><button onclick=\"barAddrow('"+ids+"','3');\" title="+getJsLocaleMessage('rms','rms_mbgl_addcol')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"barRemoverow('"+ids+"','3')\" title="+getJsLocaleMessage('rms','rms_mbgl_delcol')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td>");
            }
        }else{
            if(ptType==1){
                $("#"+ids +" tr td:last").after("<td><button onclick=\"barAddcol(this,'1');\" title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"removeRow(this,'1')\" title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td>");
            }else if(ptType==2){
                $("#"+ids +" tr td:last").after("<td><button onclick=\"barAddcol(this,'2');\" title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"removeRow(this,'2')\" title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td>");
            }else{
                $("#"+ids +" tr td:last").after("<td><button onclick=\"barAddcol(this,'3');\" title="+getJsLocaleMessage('rms','rms_mbgl_addrow')+">"+getJsLocaleMessage('rms','rms_mbgl_add')+"</button><button onclick=\"removeRow(this,'3')\" title="+getJsLocaleMessage('rms','rms_mbgl_delrow')+">"+getJsLocaleMessage('rms','rms_mbgl_del')+"</button></td>");
            }
        }
    }
    if(ptType==1){
        useEchart("1",null);
    }else{
        useEchart("2",null);
    }
}
function addParm(){
    var chartTitle = $("#chartTitle").val();
    var len = chartTitle.length;
    if(len>14){
        alert(getJsLocaleMessage("rms","rms_mbgl_title14lmt"));
        return
    }
    if(chartTitle.indexOf(getJsLocaleMessage('rms','rms_report_rowtitletpl5')) != -1){
        alert(getJsLocaleMessage("rms","rms_mbgl_noadddrept"));
        return;
    }
    var title = chartTitle+getJsLocaleMessage('rms','rms_report_rowtitletpl5');
    $("#chartTitle").val(title);
    change();
}

function substrValue(obj){
    if(obj==null){
        var str = null;
    }else{
        if(obj.indexOf("{#") != -1){
            var str = "{#"+obj.split("#")[1]+"#},";
        }else{
            var str = obj;
        }
    }
    return str;
}
//图表隐藏域的控制
function displayDiv(){
    $("#chartPopup").css({"display":"none"});
    $("#popup_div_1").css({"display":"none"});
}

function insertInfo(){
    //图形类型 （1：饼状图，2：柱状图，3：折线图，4：工资条，5：表格，默认是1）
    var chartType  = $("#chartType").val();
    //图形标题
    var chartTitle = $("#chartTitle").val();
    //数据类型（1：静态，2：动态,默认是1）
    var ptType     = $('input[name="ptType"]:checked').val();
    //饼状图颜色
    var color;
    //第一列名称
    var barColName="";
    //行名
    var barRowName = "";
    //第二列数值
    var rowValue="";
    //动态参数的值
    var parmValue = "";
    //判断标题中是否有动态参数
    if(chartTitle.indexOf(getJsLocaleMessage('rms','rms_report_rowtitletpl5')) != -1){
        parmValue = parmValue+getJsLocaleMessage('rms','rms_report_rowtitletpl5')+",";
    }
    //行数
    var rowNum=0;
    //列数
    var colNum=0;

    if(chartType == 1){
        //获取每个动态参数的值
        var tdParmValue0="";//第一列
        var tdParmValue1="";//第二列
        //color = "#ffdb6b,#ef7f6a,#bed432,#c0b643,#7ac043,#28d96d,#18e0d4,#27e3ff,#00caf5,#5fb2ff,#00b4ff,#3b7eff,#4c58ed,#6d5bf5,#ac70f9,#ef7f6a,#b34eb1,#ff6b95,#f5505f";
        color = "#37a2da,#ff9f7e,#66e1e3,#ffdb5c,#fb7293,#97bfff,#e162af,#9fe7b9,#e791d1,#e7bdf3,#32c5e9,#9d97f5,#8378eb";
        colors = color.split(",");
        var colorVal = "";
        if(ptType == 1){
            var trLen = $("#staticState table tr").length;
            for(var i = 0;i<trLen;i++){
                barRowName = barRowName+$("#staticState table tr").eq(i).children("td").eq(0).text()+",";
                rowValue = rowValue+$("#staticState table tr").eq(i).children("td").eq(1).text()+",";
                colorVal = colorVal+colors[i]+",";
            }
        }else{
            var trLen = $("#dynamic table tr").length;
            for(var i = 0;i<trLen;i++){
                barRowName = barRowName+$("#dynamic table tr").eq(i).children("td").eq(0).text()+",";
                rowValue = rowValue+"1"+",";
                //获取每个动态参数的值
                tdParmValue0 =tdParmValue0+ $("#dynamic table tr").eq(i).children("td").eq(0).text()+",";
                tdParmValue1 =tdParmValue1+ $("#dynamic table tr").eq(i).children("td").eq(1).text()+",";
                colorVal = colorVal+colors[i]+",";
            }
            if(ptType == 2){
                parmValue = parmValue + tdParmValue1;
            }
            if(ptType == 3){
                parmValue = parmValue + tdParmValue0 + tdParmValue1;
            }
            rowNum=trLen;
            colNum=1;

        }
        colorVal = colorVal.substring(0,colorVal.length-1);
        barRowName = barRowName.substring(0,barRowName.length-1);
        rowValue = rowValue.substring(0,rowValue.length-1);
        parmValue = parmValue.substring(0,parmValue.length-1);
        $.ajax({
            //提交数据的类型 POST GET
            type:"POST",
            //提交的网址
            url:"mbgl_mytemplate.htm?method=createPicture",
            //提交的数据
            data:{
                chartType:chartType,
                chartTitle:chartTitle,
                ptType:ptType,
                color:colorVal,
                barRowName:barRowName,
                rowValue:rowValue,
                parmValue:parmValue,
                rowNum:rowNum,
                colNum:colNum
            },
            //返回数据的格式
            //成功返回之后调用的函数
            beforeSend:function(){
                $("#load-bg").show();
            },

            success:function(msg){
                $('#load-bg').hide();
                if(msg!="false"){
                    displayDiv();
                    FxEditor.fxEditorRenderObj.renderChartKeyFrame(msg, "chart");
                }else{
                    alert(getJsLocaleMessage("rms","rms_myscene_alert4")+msg);
                }

            },
            error:function(){
                alert(getJsLocaleMessage("rms","rms_myscene_alert9"));
                $('#load-bg').hide();
            }

        });

    }else if(chartType == 2||chartType == 3){
        if(ptType==1){
            if(chartType == 2){
                var ids = "staticState_bar";
            }else{
                var ids = "staticState_line";
            }
        }else{
            if(chartType == 2){
                var ids = "dynamic_bar";
            }else{
                var ids = "dynamic_line";
            }
        }
        //行名
        var barRowName = "";
        //列名
        var barColName = "";
        //数值(以行为单位，用“,”隔开，换行则用“*”隔开)
        var barValue = "";
        //表格所有数据
        var barTableVal = "";
        var trLen = $("#"+ids+" table tr").length;
        var tdLen = $("#"+ids+" table tr").eq(0).children("td").length;
        for(var i = 1;i<tdLen-1;i++){
            barColName = barColName+$("#"+ids+" table tr").eq(0).children("td").eq(i).text()+",";
        }
        barColName = (barColName.substring(barColName.length-1)==',')?barColName.substring(0,barColName.length-1):barColName;
        for(var i = 1;i<trLen;i++){
            barRowName =barRowName+$("#"+ids+" table tr").eq(i).children("td").eq(0).text()+",";
        }
        barRowName = (barRowName.substring(barRowName.length-1)==',')?barRowName.substring(0,barRowName.length-1):barRowName;
        for(var i=1;i<tdLen-1;i++){
            for(var j=1;j<trLen;j++){
                if(ptType !=1){
                    barValue = barValue +"1"+",";
                }else{
                    barValue = barValue+$("#"+ids+" table tr").eq(j).children("td").eq(i).text()+",";
                }
            }
            barValue = (barValue.substring(barValue.length-1)==',')?barValue.substring(0,barValue.length-1):barValue;
            barValue = barValue+"@";
        }
        barValue = (barValue.substring(barValue.length-1)=='@')?barValue.substring(0,barValue.length-1):barValue;
        for(var i=0;i<trLen;i++){
            for(var j=0;j<tdLen-1;j++){
                if(ptType !=1){
                    if(i==0){
                        barTableVal = barTableVal+$("#"+ids+" table tr").eq(i).children("td").eq(j).text()+","
                    }else{
                        if(j==0){
                            barTableVal = barTableVal +$("#"+ids+" table tr").eq(i).children("td").eq(j).text()+",";
                        }else{
                            barTableVal = barTableVal +"1"+",";
                        }
                    }

                }else{
                    barTableVal = barTableVal+$("#"+ids+" table tr").eq(i).children("td").eq(j).text()+",";
                }
            }
            barTableVal = (barTableVal.substring(barTableVal.length-1)==',')?barTableVal.substring(0,barTableVal.length-1):barTableVal;
            barTableVal = barTableVal+"@";
        }
        barTableVal = (barTableVal.substring(barTableVal.length-1)=='@')?barTableVal.substring(0,barTableVal.length-1):barTableVal;
        //获取每个动态参数的值
        if(ptType==2){
            for(var i=1;i<tdLen-1;i++){
                for(var j=1;j<trLen;j++){
                    var tdParmValue = $("#"+ids+" table tr").eq(j).children("td").eq(i).text();
                    parmValue = parmValue + substrValue(tdParmValue);
                }
            }
            rowNum=trLen-1;
            colNum=tdLen-2;
        }else if(ptType==3){
            for(var i=1;i<tdLen-1;i++){
                var rowParmValue = $("#"+ids+" table tr").eq(0).children("td").eq(i).text();
                parmValue = parmValue + substrValue(rowParmValue);
            }
            for(var i=1;i<trLen;i++){
                var colParmValue = $("#"+ids+" table tr").eq(i).children("td").eq(0).text();
                parmValue = parmValue + substrValue(colParmValue);
            }
            for(var i=1;i<tdLen-1;i++){
                for(var j=1;j<trLen;j++){
                    var tdParmValue = $("#"+ids+" table tr").eq(j).children("td").eq(i).text();
                    parmValue = parmValue + substrValue(tdParmValue);
                }
            }
            rowNum=trLen-1;
            colNum=tdLen-2;
        }
        parmValue = (parmValue.substring(parmValue.length-1)==',')?parmValue.substring(0,parmValue.length-1):parmValue;
        $.ajax({
            //提交数据的类型 POST GET
            type:"POST",
            //提交的网址
            url:"mbgl_mytemplate.htm?method=createPicture",
            //提交的数据
            data:{
                chartType:chartType,
                chartTitle:chartTitle,
                ptType:ptType,
                barColName:barColName,
                barRowName:barRowName,
                barValue:barValue,
                barTableVal:barTableVal,
                parmValue:parmValue,
                rowNum:rowNum,
                colNum:colNum
            },
            //返回数据的格式
            //成功返回之后调用的函数
            beforeSend:function(){
                $("#load-bg").show();
            },

            success:function(msg){
                $('#load-bg').hide();
                if(msg!="false"){
                    displayDiv();
                    FxEditor.fxEditorRenderObj.renderChartKeyFrame(msg, "chart");
                }else{
                    alert(getJsLocaleMessage("rms","rms_myscene_alert4")+msg);
                }

            },
            error:function(){
                alert(getJsLocaleMessage("rms","rms_myscene_alert9"));
                $('#load-bg').hide();
            }
        });
    }

}