  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`
	$(document).ready(function() {
        var isPublic = $("#isPublic").val();
        if("false" === isPublic || "" === isPublic || null === isPublic){
            myTempCss();
            $("#isPublic").val("false");
        }else if("true" === isPublic){
            publicTempCss();
            $("#isPublic").val("true");
        }else {
            alert("选择模板有误，请刷新页面重试");
            page_loading();
        }

        $("#searchTemp").click(function () {
            document.forms['pageForm'].submit();
        });
		// $("#content select").empSelect({width:80});
		//初始化彩信预览DIV
		var h = 500;
		if (navigator.appName == "Netscape")
	    {
	    	h = 500;
	    }
		$("#tempView").dialog({
			autoOpen: false,
			height:h,
			width: 290,
			resizable:false,
			close:function(){
			    cplaytime = 0;
				nplaytime = -1;
				$("#screen").empty();
				clearInterval(ttimer); 
			}
		});
		$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
		$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
			}, function() {
			$(this).removeClass("hoverColor");
		});
	});

	//查看富信内容
	function doPreview(tplPath) {
		$(".ui-dialog-titlebar-close").show();
		var bmtype; 
		var msg = "";
		var tmpltype = $("#tmpltype").val();
		if(tmpltype === "0"){
			bmtype = 11;
		}else if(tmpltype === "1"){
			bmtype = 12;
		}
		inits();
		var pathUrl = $("#pathUrl").val();
		$.post(pathUrl+"/rms_rmsSameMms.htm?method=getTmMsgByBmtype",{tmUrl:msg,bmtype:bmtype,tplPath:tplPath},function(result){
			if (result != null && result !== "null" && result !== "") {
				arr = result.split(">");
				if(arr[0] != null && arr[0] != "")
				{
					var da = $.parseJSON(arr[0]);
					ttime = (da.totaltime/1000);
				}
				index = 1;
				$("#screen").empty();
				$("#pointer").empty();
				$("#nextpage").empty();
				$("#currentpage").empty();
				parmCount =null;
				$("#inputParamCnt1").empty();
				$("#tempView").dialog("open");
				var dsFlag = bmtype===12?1:0;
				play();
			} else {
                alert("内容文件不存在，无法预览！");
			 	//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_78"));
			}
		});
	}
	

	//彩信模板确认/
	function tempSure(obj) {
        /**
		 * 当点击立即使用时，需要根据当前选择模板是静态模板还是动态模板改变页面元素
		 * 1.发送号码 上传号码方式改变 （动态模板只上传文件）
		 * 2.格式提示
		 * 3.高级设置 （动态模板还有重号过滤）
         * 4.在右边展示预览页面
         * 5.清除号码内容（动态模板没有批量输入等）
         */
        var $selected = $(obj).parent();
        var _tempId = $selected.find("#_tempId").val();
		if(_tempId === undefined || _tempId === "" || _tempId == null) {
			//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_79"));
			alert("未选择富信模板，请重新选择！");
			return;
		}else {
            var _tempType = $selected.find("#_tempType").val();
            var _tempDegree = $selected.find("#_tempDegree").val();
            var _tempSize = $selected.find("#_tempSize").val();
            var _tempName = $selected.find("#_tempName").val();
            var _tempUrl = $selected.find("#_tempUrl").val();
            var _paramCount = $selected.find("#_paramCount").val();
			/*模板名字 模板类型 模板ID  模板URL 计费档位 容量大小 参数个数*/
			$(window.parent.document).find("#tempId").val(_tempId);
			$(window.parent.document).find("#tempUrl").val(_tempUrl);
            $(window.parent.document).find("#tempDegree").val(_tempDegree);
            $(window.parent.document).find("#tempSize").val(_tempSize);
            $(window.parent.document).find("#paramCount").val(_paramCount);
            $(window.parent.document).find("#tempType").val(_tempType);
            $(window.parent.document).find("#tempName").val(_tempName);
		}
        $(window.parent.document).find("#choose-list").css("display","");
        $(window.parent.document).find(".format-hint").attr("id","downlinks");
        if(_tempType === "1"){
		    //不同内容发送
            //发送方式
            $(window.parent.document).find("#choosePerson").css("display","none");
            $(window.parent.document).find("#bulkInput").css("display","none");
            $(window.parent.document).find("#phoneInput").css("display","none");
            //格式提示
            $(window.parent.document).find("#sameInfo").css("display","none");
            $(window.parent.document).find("#diffInfo").css("display","block");
            //高级设置
            $(window.parent.document).find("#checkRepeat").css("display","");
            //清除内容
            $(window.parent.document).find("#addressList").css("display","none");
            $(window.parent.document).find(".bultPhone").css("display","none");
            $(window.parent.document).find(".inputPhone").css("display","none");
            $(window.parent.document).find(".sameSendFile").css("display","none");
            $(window.parent.document).find(".diffSendFile").css("display","");
        }else if(_tempType === "0"){
            //相同内容发送
            //发送方式
            $(window.parent.document).find("#choosePerson").css("display","");
            $(window.parent.document).find("#bulkInput").css("display","");
            $(window.parent.document).find("#phoneInput").css("display","");
            //格式提示
            $(window.parent.document).find("#diffInfo").css("display","none");
            $(window.parent.document).find("#sameInfo").css("display","block");
            //高级设置
            $(window.parent.document).find("#checkRepeat").css("display","none");
            //清除内容
            $(window.parent.document).find("#addressList").css("display","");
            $(window.parent.document).find(".bultPhone").css("display","");
            $(window.parent.document).find(".inputPhone").css("display","");
            $(window.parent.document).find(".sameSendFile").css("display","");
            $(window.parent.document).find(".diffSendFile").css("display","none");
        }else {
            alert("选择富信模板有误，请重新选择！");
            return;
        }

        tempPreview(_tempUrl,_tempName);

        //在右边的手机预览窗口展示模板
        $(window.parent.document).find("#tempview").css("display","");



		//调用父窗口的函数/
		parent.closeDialog();
	}

	//关闭窗口
	function tempCancel(){
		//调用父窗口的函数/
		parent.closeDialog();
	}

  // 开启新的标签
  function openTemDiv(menuCode) {
      window.parent.openTemDiv(menuCode)
  }
  //点击我的场景与公共模板时切换
  function changeTemp(flag) {
      $("#isPublic").val(flag);
      //清空查询条件
      $("#tempId").val("");
      $("#tempName").val("");
      $("#tempType").val("");
      document.forms['pageForm'].submit();
  }


		   
  function myTempCss() {
      var $myTemp = $("#myTemp");
      var $publicTemp = $("#publicTemp");
      //$myTemp.css("background-color","#8fd0ec");
      $myTemp.find(".send_ac1").css("color","#02aa74");
      //$publicTemp.css("background-color","#f1f1f9");
      $publicTemp.find(".send_ac2").css("color","#000000");
  }

  function publicTempCss() {
      var $myTemp = $("#myTemp");
      var $publicTemp = $("#publicTemp");
      //$myTemp.css("background-color","#f1f1f9");
      $myTemp.find(".send_ac1").css("color","#000000");
      //$publicTemp.css("background-color","#8fd0ec");
      $publicTemp.find(".send_ac2").css("color","#02aa74");
  }

  //预览拼接好的模板
  function tempPreview(tempUrl,tempName,paramInFile) {
      var pathUrl = $("#pathUrl").val();
      $.post(pathUrl+"/mbgl_mytemplate.htm?method=getTmMsg",
          {
              tmUrl:tempUrl,
              tmName:tempName,
              paramInFile:paramInFile
          },
          function(result){
              if (result != null && result !== "null" && result !== "") {
                  $(window.parent.document).find("#cust_preview").html(result);
              } else {
                  alert("内容文件不存在，无法预览！");
                  $(window.parent.document).find("#cust_preview").html("");
              }
          });
  }