	var zTree3;
	var zTree2;
	var setting3;
	var setting2;
	var zNodes2 = [];
	var zNodes3 = [];
	var htmName;

	/*设置定时器*/
    var percentProgress = window.setInterval(percentProgressBar, 3000);
    function percentProgressBar(){
        var sendPercent=document.getElementsByClassName('sendPercent');
        var procentValue = "";
        for (var i=0;i<sendPercent.length;i++)
        {
            procentValue +=sendPercent[i].attributes.value.nodeValue;
            procentValue +=",";
        }
        procentValue=procentValue.substring(0,procentValue.length-1)
        var data = {
            "taskId":procentValue
        };
        $.ajax({
            url:"smt_smsSendedBox.htm?method=getProgress",
            type:"post",
            dataType:"json",
            data:data,
            success:function(result){
            	if (result == null){
            		return;
				}
                for (var key in result)
                {
                    key = key+"";
                    var perCent = result[key].split("&")[0];
                    var endTime = result[key].split("&")[1];
                    setPercentBarWidth(key,perCent,endTime);
                }
            },
            error:function(xrq,textStatus,errorThrown){
                window.clearInterval(percentProgress);
            }
        });
	}


var progressbar={
	init:function(){
        percentProgressBar();
	}
};

progressbar.init();


    /**
	 * 设置进度条宽度
     * @param arg
     */
    function setPercentBarWidth(taskId,perCent,endTime){
		var endSendTime = document.getElementById("endSendTime_"+taskId);
        endSendTime.innerHTML = endTime;
		var percentContent = document.getElementById("percentContent_"+taskId);
		var num = parseInt(perCent.replace('%',' '));
		percentContent.innerHTML = perCent;
		var percentBar = document.getElementById("percentBar_"+taskId);
		if(num<10){
			ratio = parseFloat("0.0"+num);
		}else if(num == 100){
			ratio = parseFloat("1");
		}else{
            ratio = parseFloat("0."+num);
		}
		var width = accMul(parseInt(percentContent.offsetWidth),ratio);
		$("#"+"percentBar_"+taskId).animate({width:width+"px"});
		//percentBar.style.width = width+"px";
	}

    /**
	 * js乘法
     * @param arg1
     * @param arg2
     * @returns {number}
     */
    function accMul(arg1,arg2)
	{
		var m=0,s1=arg1.toString(),s2=arg2.toString();
		try{m+=s1.split(".")[1].length}catch(e){};
		try{m+=s2.split(".")[1].length}catch(e){};
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
	}

$(document).ready(function() {
	            getLoginInfo("#smssendparams");//加载头文件内容
				var windowheight = $(document).height();
				$("#content tbody tr #msgcont a").hover(function(e){
					var tooltip = "";
					tooltip = "<div id='tooltip' style='background:#FFFFE0;border:1px solid #000;'><table><tr><td word-break: break-all;><xmp style='word-break: break-all;white-space:normal;'>"
						+$(this).children("label").children("xmp").html()+"</xmp></td></tr></div>";
					setTimeout(function(){
							$('#tooltip').remove();
							$('body').append(tooltip);
  							var divwidth =document.getElementById("tooltip").offsetWidth+20;
							$('#tooltip')
								.css({
									"opacity":"1",
									"top":(e.pageY-20)+"px",
									"left":(e.pageX-divwidth)+"px"
								});
							//-----------调整弹出框位置
							var divtop = $("#tooltip").offset().top;
							var divleft = $("#tooltip").offset().left;
							var divheight = $("#tooltip").height();
							if((divtop+divheight)>windowheight){
								$('#tooltip')
								.css({
									"top":(windowheight-divheight-20)+"px",
									"left":divleft+"px"
								});
								}
							//----------
						},250);
					},function(){
						setTimeout(function(){
							if (null != document.getElementById("tooltip"))
							{

								document.body.removeChild(document.getElementById("tooltip"));
							}
						},250);
					}
				);

			     //var lgcorpcode =$("#lgcorpcode").val();
				 //var lguserid =$("#lguserid").val();
				var lgcorpcode=GlobalVars.lgcorpcode;
				var lguserid=GlobalVars.lguserid;
			    // var lgguid =$("#lgguid").val();
				//请求名称
				htmName=$("#htmName").val();
			    setting2.asyncUrl = htmName+"?method=createUserTree2&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
			    //机构树
			    setting3.asyncUrl = htmName+"?method=createDeptTree&lguserid="+lguserid;
				setting2.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
				zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);
				zTree2.expandAll(true);
				reloadTree(zNodes3);

			});


function modify(t)
	{
		$('#modify').dialog({
			autoOpen: false,
			width:250,
			   height:200
		});
		$("#msgcont").children("xmp").empty();
		$("#msgcont").children("xmp").text($(t).children("label").children("xmp").html());
		$('#modify').dialog('open');
	}
	function rtime()
	{
	    var max = "2099-12-31 23:59:59";
	    var v = $("#sendtime").attr("value");
		//if(v.length != 0)
		//{
		//    var year = v.substring(0,4);
		//	var mon = v.substring(5,7);
		//	var day = 31;
		//	if (mon != "12")
		//	{
		//	    mon = String(parseInt(mon,10)+1);
		//	    if (mon.length == 1)
		//	    {
		//	        mon = "0"+mon;
		//	    }
		//	    switch(mon){
		//	    case "01":day = 31;break;
		//	    case "02":day = 28;break;
		//	    case "03":day = 31;break;
		//	    case "04":day = 30;break;
		//	    case "05":day = 31;break;
		//	    case "06":day = 30;break;
		//	    case "07":day = 31;break;
		//	    case "08":day = 31;break;
		//	    case "09":day = 30;break;
		//	    case "10":day = 31;break;
		//	    case "11":day = 30;break;
		//	    }
		//	}
		//	else
		//	{
		//	    year = String((parseInt(year,10)+1));
		//	    mon = "01";
		//	}
		//	max = year+"-"+mon+"-"+day+" 23:59:59"
	    //}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
    }

	function stime(){
	    var max = "2099-12-31 23:59:59";
	    var v = $("#recvtime").attr("value");
	    var min = "1900-01-01 00:00:00";
		//if(v.length != 0)
		//{
		//    max = v;
		//    var year = v.substring(0,4);
		//	var mon = v.substring(5,7);
		//	if (mon != "01")
		//	{
		//	    mon = String(parseInt(mon,10)-1);
		//	    if (mon.length == 1)
		//	    {
		//	        mon = "0"+mon;
		//	    }
		//	}
		//	else
		//	{
		//	    year = String((parseInt(year,10)-1));
		//	    mon = "12";
		//	}
		//	min = year+"-"+mon+"-01 00:00:00"
		//}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
	}

	//获取机构代码
	setting3 = {
			async : true,
			asyncUrl : htmName+"?method=createDeptTree", //获取节点数据的URL地址

			//checkable : true,
		    //checkStyle : "radio",
		    //checkType : { "Y": "s", "N": "s" },
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
				//zTree3.expandAll(false);
				}
			}
	};

	//获取人员代码
	/*setting2 = {
			checkable : true,
		    checkStyle : "checkbox",
		    checkType : { "Y": "s", "N": "s" },

			isSimpleData: true,
			rootPID : -1,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			callback: {
				beforeAsync: function(treeId, treeNode) {return false;},
				change: zTreeOnClick2,
				asyncSuccess:function(event, treeId, treeNode, msg){
				zTree2.expandAll(false);
				}
			}
	};*/

	 setting2 = {
			    checkable : true,
			    checkStyle : "checkbox",
			    checkType : { "Y": "s", "N": "s" },
			    async : true,
		        asyncUrl : htmName+"?method=createUserTree2", //获取节点数据的URL地址
				isSimpleData: true,
				rootPID : 0,
				treeNodeKey: "id",
				treeNodeParentKey: "pId",
				asyncParam: ["depId"],
				callback: {
					//beforeAsync: function(treeId, treeNode) {return false;},
					change: zTreeOnClick2,
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree2.getNodeByParam("level", 0);
						   zTree2.expandNode(rootNode, true, false);
						}
					}
				}

	 };

	//隐藏人员树形控件
	function showMenu() {
		hideMenu2();
		var sortSel = $("#depNam");
		var sortOffset = $("#depNam").offset();
		$("#dropMenu").toggle();
	}

	//失去焦点隐藏人员树形控件
	function hideMenuOnblur()
	{
		hideMenu2();
		var sortSel = $("#depNam");
		var sortOffset = $("#depNam").offset();
		$("#dropMenu").hide();
	}

	//隐藏机构树形控件
	function showMenu2() {
		hideMenu();
		var sortSel = $("#userName");
		var sortOffset = $("#userName").offset();
		$("#dropMenu2").toggle();
	}

	//隐藏人员树形控件
	function hideMenu() {
		$("#dropMenu").hide();
	}


	//隐藏人员树形控件
	function hideMenu2() {
		$("#dropMenu2").hide();
	}

	//选中的机构显示文本框
	function zTreeOnClick3(event, treeId, treeNode) {
		if (treeNode) {
			//var zTreeNodes3=zTree3.getChangeCheckedNodes();
			var pops="";
			var depts ="";
			//for(var i=0; i<zTreeNodes3.length; i++){
				//pops+=zTreeNodes3[i].name+";";
				//depts+=zTreeNodes3[i].depId+",";
			//}
			$("#depNam").attr("value", treeNode.name);
			$("#deptid").attr("value",treeNode.id);
			//if(zTreeNodes3.length==0){

			//$("#depNam").attr("value", "");
			//$("#deptid").attr("value","");
			//}
		}
	}


	//选中的机构显示文本框
	function zTreeOnClickOK3() {
		hideMenu();

		/*var zTreeNodes3=zTree3.getChangeCheckedNodes();

		var pops="";
		var deptids = "";
		for(var i=0; i<zTreeNodes3.length; i++){
			pops+=zTreeNodes3[i].name+";";
			deptids+=zTreeNodes3[i].depId+",";
		}
		$("#depNam").attr("value", pops);
		$("#deptid").attr("value",deptids);
		if(zTreeNodes3.length==0){
			$("#depNam").attr("value", "请选择");
			$("#deptid").attr("value","");
		}*/
		//treeNode=zTree3.getSelectedNode();
		//$("#depNam").attr("value", treeNode.name);
		//$("#deptid").attr("value",treeNode.id);
	}


	//选中的人员显示文本框
	function zTreeOnClick2(event, treeId, treeNode) {
		if (treeNode) {
			var zTreeNodes2=zTree2.getChangeCheckedNodes();

			var pops="";
			var userids = "";
			for(var i=0; i<zTreeNodes2.length; i++){

				pops+=zTreeNodes2[i].name+";";
				userids+=zTreeNodes2[i].id.replace("u","")+",";
			}
			$("#userName").attr("value", pops);
			$("#userid").attr("value",userids);
			if(zTreeNodes2.length==0){
			 $("#userid").attr("value","");
			 $("#userName").attr("value", "");
			}
		}
	}

	//选中的人员显示文本框
	function zTreeOnClickOK2() {

		    hideMenu2();
			var zTreeNodes2=zTree2.getChangeCheckedNodes();

			var pops="";
			var userids ="";
			for(var i=0; i<zTreeNodes2.length; i++){

				pops+=zTreeNodes2[i].name+";";
				userids+=zTreeNodes2[i].id.replace("u","")+",";
			}
			$("#userName").attr("value", pops);
			$("#userid").attr("value",userids);
			if(zTreeNodes2.length==0){
			 $("#userid").attr("value","");
			 $("#userName").attr("value", getJsLocaleMessage('dxzs','dxzs_ssend_alert_159'));
			}

	 }

	function cleanSelect_dep()
	{
		var checkNodes = zTree2.getCheckedNodes();
	    for(var i=0;i<checkNodes.length;i++){
	     checkNodes[i].checked=false;
	    }
	    zTree2.refresh();
	    $("#userid").attr("value","");
	    $("#userName").attr("value", getJsLocaleMessage('dxzs','dxzs_ssend_alert_159'));
	}

	function cleanSelect_dep3()
	{
		var checkNodes = zTree3.getCheckedNodes();
	    for(var i=0;i<checkNodes.length;i++){
	     checkNodes[i].checked=false;
	    }
	    zTree3.refresh();
	    $("#depNam").attr("value", getJsLocaleMessage('dxzs','dxzs_ssend_alert_159'));
		$("#deptid").attr("value","");
	}

		// 加载人员/机构树形控件
	function reloadTree(zNodes3) {
		hideMenu();
		hideMenu2();
		setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
		zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
		zTree3.expandAll(true);
		//zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);
	}

	function zTreeBeforeAsync(treeId, treeNode) {
		if (treeNode.id == 1)
			return false;
		return true;
	}

    /**
	 * 修改定时
     * @param mtId
     * @param msgType 1-相同内容短信 2－动态模板短信；3－文件内容短信
     */
    function changeTiming(mtId,msgType)
    {
        $("#changeTimingDiv").css("display","block");
        $("#changeTimingFrame").css("display","block");
        var height;
        var width;
        if(msgType != "1"){
        	height = 300;
            width = 500;
        }else {
            height = 400;
            width = 600;
		}
        $("#changeTimingDiv").dialog({
            autoOpen: true,
            height:height,
            width: width,
            resizable:false,
            modal: true,
            open:function(){
            },
            close:function(){
                $("#changeTimingDiv").css("display","none");
                $("#changeTimingFrame").css("display","none");
            }
        });
        //resizeDialog($("#changeTimingDiv"),"ydbgDialogJson","dxzhsh_xtnrqf_test2");

        $("#changeTimingFrame").attr("src","smt_smsSendedBox.htm?method=toChangeTiming&mtId="+mtId+"&msgType="+msgType);

        $("#changeTimingDiv").dialog("open");
    }
    function cancelChangeTaskdiv(){
        $("#changeTimingDiv").dialog("close");
        $("#changeTimingFrame").attr("src","");
    }

    function modifyNew(t,state)
    {
        if(state=="1")
        {
            $('#modify').dialog("option","title",getJsLocaleMessage('dxzs','dxzs_ssend_alert_217'));
        }
        else if(state=="2")
        {
            $('#modify').dialog("option","title",getJsLocaleMessage('dxzs','dxzs_ssend_alert_218'));
        }
        $("#msgcont").empty();
        //用label显示短信内容
        $("#msgcont").text($(t).children("xmp[name='msgXmp']").text());
        //修改成用textarea显示短信内容
        $('#modify').dialog('open');
    }