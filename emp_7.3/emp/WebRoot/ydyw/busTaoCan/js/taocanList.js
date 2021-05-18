
//跳转到新增页面
function toAdd() {
    var lguserid = $("#lguserid").val();
    var pathUrl = $("#pathUrl").val();
    var lgcorpcode=$("#lgcorpcode").val();
    window.location.href = pathUrl
        + "/ydyw_busTaocanMgr.htm?method=toAdd&lguserid="
        + lguserid+"&lgcorpcode="+lgcorpcode;
}

//保存内容
function save(type){
    var name=$("#name").val();
    var code=$("#code").val();
    var lguserid=$("#lguserid").val();
    var lgcorpcode=$("#lgcorpcode").val();
    var startSubmitTime=$("#startSubmitTime").val();
    var endSubmitTime=$("#endSubmitTime").val();
    var taocan_type=$("#taocan_type").val();
    var buckle_date=$("#buckle_date").val();
    var backup_max=$("#backup_max").val();
    var backup_day=$("#backup_day").val();
    var free_days =$("#free_days").val();
    var timerTime=$("#timerTime").val();
    var selectdate=$("#selectdate").val();
    //隐藏域用于判断该数字是否修改过
    var hidden_buckle_date=$("#hidden_buckle_date").val();
    //是否启用
    var state=$("input:radio[name='state']:checked").val();
    var fee=$("#fee").val();
    //已经选择套餐的code
    var packagecodes=$("#packagecodes").val();
    var timerStatus=$("input:radio[name='timerStatus']:checked").val();

    name=$.trim(name);
    code=$.trim(code);

    if(name==""){
    	/*请输入套餐名称必填项！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_tczlsz_text_29"));
        return;
    }
    var pattern=/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;
    if(pattern.test(name)){
    	/*套餐名称不能有特殊字符！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_23"));
        return;
    }
    if(name.length>25){
    	/*套餐名称已超过文字上限25个字，不允许再输入！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_24"));
        return;
    }
    if(code==""){
    	/*请输入套餐编号必填项！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_25"));
        return;
    }
    var number=/[^a-zA-Z0-9]/g;
    if(pattern.test(code)){
    	/*套餐编号不能有特殊字符！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_26"));
        return;
    }

    if(number.test(code)){
    	/*套餐编号只能输入英文与数字！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_27"));
        return;
    }
    if(code.length>25){
    	/*套餐编号已超过文字上限25个字，不允许再输入！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_28"));
        return;
    }

    if(startSubmitTime==""){
    	/*请输入套餐有效期开始时间必填项！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_29"));
        return;
    }
    if(endSubmitTime==""){
    	/*请输入套餐有效期结束必填项！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_30"));
        return;
    }

    if(packagecodes==""){
    	/*请添加业务包！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_35"));
        return;
    }

    if(timerStatus=="1"&& timerTime == ""){
    	/*请填写自定义时间！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_31"));
        return;
    }
    if(taocan_type==""){
    	/*请选择计费类型！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_32"));
        return;
    }
    if(fee==""){
    	/*请输入计费金额！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_33"));
        return;
    }
    if(parseInt(buckle_date)==0||parseInt(buckle_date)>28){
    	/*扣费时间应该在1~28之间！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_34"));
        return;
    }

    if(taocan_type!="1"&&selectdate==""){
    	/*请选择扣费时间类型！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_36"));
        return;
    }
    if((selectdate=="1"||selectdate=="3")&&buckle_date==""){
    	/*请填入扣费日期！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_37"));
        return;
    }

    if(backup_max!=""&&parseInt(backup_max)>0){
        if(backup_day==""||parseInt(backup_day)==0){
        	/*补扣次数对应的补扣间隔时间应该填写！*/
            alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_38"));
            return;
        }
        if(parseInt(backup_day)<3){
        	/*补扣间隔时间须大于等于3天的默认时间！*/
            alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_39"));
            return;
        }
    }

    if(parseInt(backup_max)>5){
    	/*超过补扣次数5次上限！*/
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_40"));
        return;
    }

    $.ajax({
        type:"POST",
        url: "ydyw_busTaocanMgr.htm",
        data: {method: "save",
            type:type,
            name:name,
            code:code,
            hidden_buckle_date:hidden_buckle_date,
            startSubmitTime:startSubmitTime,
            endSubmitTime:endSubmitTime,
            taocan_type:taocan_type,
            buckle_date:buckle_date,
            backup_max:backup_max,
            backup_day:backup_day,
            free_days:free_days,
            selectdate:selectdate,
            timerTime:timerTime,
            timerStatus:timerStatus,
            state:state,
            fee:fee,
            lguserid:lguserid,
            packagecodes:packagecodes,
            lgcorpcode:lgcorpcode,
            isAsync:"yes"},
        success: function(result){
            if(result=='repeat'){
                alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_41"));
            }else if(result=='addsuccess'){
                alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_42"));
                toList();
            }else if(result=='addfail'){
                alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_43"));
            }else if(result=='dateNotAllow'){
                alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_44"));
            }else if(result=='timeError'){
                alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_45"));
            }else if(result=='updatesuccess'){
                alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_49"));
                toList();
            }else if(result=='updatefail'){
                alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_50"));
            }
        },
        error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
    })

}



//开始时间
function rtime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#startSubmitTime").attr("value");
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
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

};
//时间控件处理
function stime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#endSubmitTime").attr("value");
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
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

};

//套餐开始时间
function setime(){
    WdatePicker({dateFmt:'yyyy-MM-dd',minDate:today});

};

//套餐结束时间
function retime(){
    var max = "2099-12-31";
    var v = $("#startSubmitTime").attr("value");

    WdatePicker({dateFmt:'yyyy-MM-dd',minDate:v,maxDate:max});
};


//列出套餐信息
function toList() {
    var lguserid = $("#lguserid").val();
    var pathUrl = $("#pathUrl").val();
    var lgcorpcode=$("#lgcorpcode").val();
    window.location.href = pathUrl+ "/ydyw_busTaocanMgr.htm?method=find&lguserid="+ lguserid+"&lgcorpcode="+lgcorpcode;
}

//添加业务包
function showtaocan(){
    var nameval=$("#name").val();
    $("#titlename").html(nameval);
    $("#msgtep").css("display","block");
    //为了处理查询无结果后，点击，防止，出现无记录的情况
    refresh();
    $("#msgtep").dialog({
        autoOpen: false,
        width: 620,
        resizable:false,
        modal: true,
        open:function(){

        },
        close:function(){
            $("#msgtep").css("display","none");
        }
    });

    $("#msgtep").dialog("open");
}

//点击查询
function refresh(){
    var epname = $("#epname").val();
    //left
    $.ajax({
        type:"POST",
        url: "ydyw_busTaocanMgr.htm",
        data: {method: "search",epname:epname,isAsync:"yes"},
        success: function(result){
            var data = eval("("+result+")");
            if(data.length==0){
                $("#left").html("");
                return;
            }
            $("#left").html("");
            for(var i= 0;i<data.length;i++){
                var package_code = data[i].package_code;
                var package_name = data[i].package_name;
                var package_id = data[i].package_id;
                $("#left").append("<option value=\'"+package_id+"\' taocan_name=\'"+package_name+"\' title=\'"+package_name+"("+package_code+")\' taocan_code=\'"+package_code+"\' >"+package_name+"("+package_code+")</option>");
            }
        },
        error: function(){getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51")}
    })
}


//点击查询
function searchName(){
    var epname = $("#epname").val();
    var pattern=/[`~!@#\$%\^\&\*\(\)_\+<>\?:"\{\},\.\\\/;'\[\]]/im;
    if(pattern.test(epname)){
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_46"));
        return;
    }
    //去掉空格
    epname=$.trim(epname);
    $("#epname").val(epname);
    //left
    $.ajax({
        type:"POST",
        url: "ydyw_busTaocanMgr.htm",
        data: {method: "search",epname:epname,isAsync:"yes"},
        success: function(result){
            var data = eval("("+result+")");
            if(data.length==0){
                $("#left").html("");
                alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_18"));
                return;
            }
            $("#left").html("");
            for(var i= 0;i<data.length;i++){
                var package_code = data[i].package_code;
                var package_name = data[i].package_name;
                var package_id = data[i].package_id;
                $("#left").append("<option value=\'"+package_id+"\' taocan_name=\'"+package_name+"\' title=\'"+package_name+"("+package_code+")\' taocan_code=\'"+package_code+"\' >"+package_name+"("+package_code+")</option>");
            }
        },
        /*请求异常！*/
        error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
    })
}

//保存已经保存的套餐
function bindTaoCan(){
    if ($("#getChooseMan li").size() < 1) {
        alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_14"));
        return;
    }
    var busscodes="";
    //
    var htmltable="";
    $("#getChooseMan li").each(function() {
        var dataval = $(this).attr("dataval");
        if (dataval != null && dataval != "") {

            var pagtxt=$(this).text();
            //名称
            var name =$(this).attr("taocan_name");
            //编码
            var code =$(this).attr("taocan_code");
            busscodes=busscodes + code + ",";
            htmltable=htmltable+"<tr class='title_bd'><td class='trcss'>"+name+"</td><td class='trcss'>"+code+"</td></tr>";
        }
    });
    $("#packagecodes").val(busscodes);
    if(busscodes!=""){
        $("#nopag").remove();
    }
    $("#packageTable").empty();
    $("#packageTable").append(htmltable);
    alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_47"));
    $("#msgtep").dialog("close");
}

function closeDiv(){
    $("#msgtep").dialog("close");
    $("#epname").val("");
}
//新增的时候，点击取消按钮，其他信息还原
function closeAddDiv(){
    $("#msgtep").dialog("close");
    $("#epname").val("");
    $("#right").empty();
    $("#getChooseMan").empty();
    $("#rightSelectTemp").empty();
    $("#manCount").html(0);
}


//选择计费类型时候的处理
function changestate(){
    var taocan_type=$("#taocan_type").val();
    //如果是免费，那么其他输入项，不可输入
    if(taocan_type=="1"){
        $("#fee").val("0");
        $("#fee").attr("disabled",true);
        $("#selectdate").val("");
        $("#selectdate").attr("disabled",true);
        $("#buckle_date").val("");
        $("#buckle_date").attr("disabled",true);
        $("#fee_div").hide();

        $("#backup_max").val("");
        $("#backup_max").attr("disabled",true);
        $("#backup_day").val("");
        $("#backup_day").attr("disabled",true);
        $("#free_days").val("");
        $("#free_days").attr("disabled",true);
        $("#sendNow").attr("disabled",true);
        $("#sendOther").attr("disabled",true);
        $("#sendNow").attr("checked","checked");
        $('#time2').hide();
        $("#strlen").html(getJsLocaleMessage("common","text_month"));
    }else{
        $("#fee").val("");
        $("#selectdate").find("option[text='"+getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_13")+"']").attr("selected",true);
        $("#fee_div").hide();
        $("#fee").attr("disabled",false);
        $("#buckle_date").val("");
        $("#selectdate").attr("disabled",false);
        $("#backup_max").attr("disabled",false);
        $("#backup_day").attr("disabled",false);
        $("#free_days").attr("disabled",false);
        $("#sendNow").attr("disabled",false);
        $("#sendOther").attr("disabled",false);
        if(taocan_type=="3"){
            $("#strlen").html(getJsLocaleMessage("common","text_season"));
        }else if(taocan_type=="4"){
            $("#strlen").html(getJsLocaleMessage("common","text_year"));
        }else{
            $("#strlen").html(getJsLocaleMessage("common","text_month"));
        }
    }

}

//切换是否当前扣费
function changeType(){
    var selectdate=$("#selectdate").val();
    if(selectdate=="2"||selectdate==""){
        $("#buckle_date").val("");
        $("#buckle_date").attr("disabled",true);
        $("#fee_div").hide();
    }else{
        $("#buckle_date").attr("disabled",false);
        $("#fee_div").show();
    }
}
//获取机构代码
setting3 = {
    async : true,
    asyncUrl : "ydyw_depBillQuery.htm?method=createDeptTree", //获取节点数据的URL地址
    isSimpleData : true,
    rootPID : 0,
    treeNodeKey : "id",
    treeNodeParentKey : "pId",
    asyncParam: ["depId"],

    callback: {

        click: zTreeOnClick3,
        asyncSuccess:function(event, treeId, treeNode, msg){
            if(!treeNode){
                var rootNode = zTree3.getNodeByParam("level", 0);
                zTree3.expandNode(rootNode, true, false);
            }
        }
    }
};

//选中的机构显示文本框
function zTreeOnClick3(event, treeId, treeNode) {
    if (treeNode) {
        var pops="";
        var depts ="";
        $("#depNam").attr("value", treeNode.name);
        $("#deptid").attr("value",treeNode.id);
    }
}


//隐藏树形控件
function showMenu() {
    var sortSel = $("#depNam");
    var sortOffset = $("#depNam").offset();
    $("#dropMenu").toggle();
}
//选中的机构显示文本框
function zTreeOnClickOK3() {
    hideMenu();
}
//隐藏树形控件
function hideMenu() {
    $("#dropMenu").hide();
}

function cleanSelect_dep3()
{
    var checkNodes = zTree3.getCheckedNodes();
    for(var i=0;i<checkNodes.length;i++){
        checkNodes[i].checked=false;
    }
    zTree3.refresh();
    $("#depNam").attr("value", getJsLocaleMessage("common","common_pleaseSelect"));
    $("#deptid").attr("value","");
}

function reloadTree(zNodes3) {
    hideMenu();
    setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
    zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
    zTree3.expandAll(true);
}

//跳转到修改页面
function toModif(code){
    var lguserid = $("#lguserid").val();
    var pathUrl = $("#pathUrl").val();
    var lgcorpcode=$("#lgcorpcode").val();
    window.location.href = pathUrl
        + "/ydyw_busTaocanMgr.htm?method=toEdit&lguserid="
        + lguserid+"&lgcorpcode="+lgcorpcode+"&taocan_code="+code;
}

//跳转到详细页面
function moreInfo(code,empLangName){
    var lguserid = $("#lguserid").val();
    var pathUrl = $("#pathUrl").val();
    var lgcorpcode=$("#lgcorpcode").val();
    var empLangName = empLangName;
    $("#moreInfoDiv").css("display","block");
    $("#moreInfoFrame").css("display","block");

    $("#moreInfoDiv").dialog({
        autoOpen: false,
        height:440,
        width:empLangName == "zh_HK"?810:740,
        resizable:false,
        modal: true,
        open:function(){

        },
        close:function(){
            $("#moreInfoDiv").css("display","none");
            $("#moreInfoFrame").css("display","none");
        }
    });

    //新增
    $("#moreInfoFrame").attr("src",pathUrl+"/ydyw_busTaocanMgr.htm?method=moreInfo&lguserid="+ lguserid+"&lgcorpcode="+lgcorpcode+"&taocan_code="+code+"&time="+new Date().getTime());
    $("#moreInfoDiv").dialog("open");
}
//关闭详细列表框
function closeInfo(){
    $("#moreInfoDiv").dialog("close");
    $("#moreInfoFrame").attr("src","");
}




//套餐名称
function showName(t)
{
    $('#codeInfo').dialog('close');
    $('#buslist').dialog('close');
    $('#nameInfo').dialog({
        autoOpen: false,
        width:250,
        height:200
    });
    $("#nameMsg").empty();
    $("#nameMsg").text($(t).children("label").children("xmp").text());
    $('#nameInfo').dialog('open');
}
//套餐编号
function showCode(t)
{
    $('#nameInfo').dialog('close');
    $('#buslist').dialog('close');

    $('#codeInfo').dialog({
        autoOpen: false,
        width:250,
        height:200
    });
    $("#codeMsg").empty();
    $("#codeMsg").text($(t).children("label").children("xmp").text());
    $('#codeInfo').dialog('open');
}



//适用业务
function showTaoCan(t)
{
    $('#nameInfo').dialog('close');
    $('#codeInfo').dialog('close');

    $('#buslist').dialog({
        autoOpen: false,
        width:265,
        height:200
    });
    $("#msgshow2").empty();
    $("#msgshow2").text($(t).children("label").children("xmp").text());
    $('#buslist').dialog('open');
}
	