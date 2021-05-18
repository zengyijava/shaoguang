var redirectUrl=document.URL;
var urls=redirectUrl.split("/");
redirectUrl=urls[0]+"//"+urls[2]+"/"+urls[3]+"/";	
var smsTm;
//短信回复
$(document).ready(function(){

	getLoginInfo("#hiddenValueDiv");
				    $("#selectInfoBody").change(choiceInfoBody);               //点击信息体按钮事件
				    $("#delInfoBodyBtn").click(deleteInfoBodyVal);             //点击重置信息体按钮事件
				    //$("#seesee").click(getResult);                             //预览按钮事件
				    $("#subReply").click(function(){checkSubReplyBefore();});                   //提交表单事件
				   
	$('#modify').dialog({
		autoOpen: false,
		resizable: false,
		width:400,
	    height:200,
	    open:function(){
						$("#tempSel").css("display","none");
					},
					 close:function(){
						$("#tempSel").css("display","");
					}
	});
				    
});



function iniEditor()
{
	var ms = $.trim($("#msgMain").val());
	smsTm = $("#sTempEditor").mtsmseditor({
           editorId: "tempContent",
           editorPath:"../../system/",
           enable:true,
           insertParam:true
           });
	//smsTm.clearContent();
	//smsTm.setContent(ms);
}
		//步骤名称列表
        function choiceLoopResVal(){
        	    var loopResVal = $("#selectLoopResVal option:selected").val();            //获取步骤名称
        	    $("#result_collection").val(loopResVal);                             //设置步骤名称文本域的值
        	    var id = $("#selectLoopResVal option:selected").attr("id");
        	    if($("#msgLoopId").length > 0) {
    				$("#msgLoopId").remove();
    			}
    			$("#reply").append("<input type='hidden' id='msgLoopId' name='msgLoopId' value='" + id +"'/>");
        	   
            }

         //重置步骤名称
         function deleteLoopResVal(){
                 $("#result_collection").val("");                                     //清空信息体文本域的值
                 var id = $("#selectLoopResVal option:selected").attr("id");
                 if($("#msgLoopId").length > 0) {
     				$("#msgLoopId").remove();
     			 }
     			 var setDefLoopRes = document.getElementById("selectLoopResVal");    //获取步骤名称select标签的ID
        	     setDefLoopRes.options[0].selected=true;                             //把步骤名称select标签的option设置为默认的选项
             }


        //信息体列表
        
         function choiceInfoBody(){
         	var pathUrl = $("#pathUrl").val();
                 $.post(pathUrl+"/eng_mtProcess.htm?method=getTmMsg",
             		{tmId:$('#selectInfoBody').val(),isAsync:"yes"},
             		function(result)
             		{
             			if(result == "outOfLogin")
             			{
             				location.href=pathUrl+"/common/logoutEmp.html";
             			}else
             			{
             				$("#msgMain").val(result);
             			}
             		}
             	);             //获取信息体的值
             }

        //重置信息
    
        function deleteInfoBodyVal(){
        	$("#msgMain").val("");        //清空信息体的值   
        	var setDefInfoBody = document.getElementById("selectInfoBody");    //获取信息体select标签的ID
        	setDefInfoBody.options[0].selected=true;                         //把信息体select标签的option设置为默认的选项
          }
          
          //对步骤名称和信息体的判断,两者不能为空
          function checkBefore(){
               var loopResult = $("#msgLoopId").val();          //获取步骤名称文本域的值
               var msgBody = $.trim($("#msgMain").val());       //获取信息体文本域的值
               var regStr =/#W_(\d+)#/g;
               if(loopResult == "" ) {                         //判断步骤名称
                  //alert("数据源不能为空！");
                  alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_sjybnwk"));
                  return false;
               }
               if(msgBody == "" ){                //判断信息体
                   //alert("发送内容不能为空，请输入发送内容！");
                   alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fsnrbnwkqsrfsnr"));
                   return false;
               }
               if(msgBody.length > smsContentMaxLen)
				{
					//alert("发送内容长度大于短信最大长度限制，最大长度限制为："+smsContentMaxLen);
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fsnrcddydxzdcdxz")+smsContentMaxLen);
					return false;
				}
               if(smsContentMaxLen == 700)
				{
					if(!checkSmsContentLen(msgBody,smsContentMaxLen))
					{
						return false;
					}
				}
               if(regStr.test(msgBody) && msgBody.match(regStr).length > 1){
            	   //插入且插入了多个网讯，则不允许
            	   //alert("发送内容插入了多个网讯，只能插入一个网讯！");
            	   alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fsnrcrldgwx"));
            	   return false;
               }
               
               return true;
          }
          
          //提交之前对步骤名称和信息体的检查
          function checkSubReplyBefore(){
        	  var pathUrl = $("#pathUrl").val();
              if(checkBefore()){
            	  
                  //过滤关键字
                  var msg=getContentValX($("#msgMain"));  
                  $.post(pathUrl+"/eng_moService.htm?method=checkBadWord1",
                		  {tmMsg:msg,corpCode:$("#lgcorpcode").val()}
                  	,function(message){
	                	  if(message.indexOf("@")==-1){
	              			//alert("系统繁忙，请刷新页面或稍后重试！");
	              			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xtfmqsxymhshcs"));
	              			return;
	              		}
	              		message=message.substr(message.indexOf("@")+1);
						if(message != null && message!="")
						{
							//alert("短信模板内容包含如下违禁词组：\n    "+message+"\n请检查后重新输入。");
							alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dxmbnrbhrxwjcz")+"\n    "+message+"\n"+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qjchcxsr"));
						}else if(message=="error")
						{
							//alert("过滤关键字失败！");
							alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_glgjzsb"));
						}else
						{
							//if(confirm("确定提交吗?")) {
							if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qdtjm"))) {
								var repedMsg = getContentValX($("#msgMain"));
								$("#msgMain").val(repedMsg);
      							$("#subReply").attr("disabled","disabled");
          						$("#previous").attr("disabled","disabled");
          						// var msg = $("<span>").css("color", "green").text("正在提交中,请稍后.........");
         						//$("#previous").parent().append(msg);
         						$("#reply").attr("action",$("#reply").attr("action")+'&lgcorpcode='+$('#lgcorpcode').val()+'&lguserid='+$('#lguserid').val());
                      			$("#reply").submit();//提交
      						}
						}
					});	
              }
          }

		//发送异步请求，获取步骤名称执行后的结果
		function getResult() 
		{
			var pathUrl = $("#pathUrl").val();
			var result = "";
			var msgLoopId = $("#msgLoopId").val();
			var lgcorpcode=$("#lgcorpcode").val();
			//$("#btnPreview").attr("value","处理中..");
			$("#btnPreview").attr("value",getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_clz"));
			$("#btnPreview").attr("disabled","disabled");

			//预览之前先对步骤名称和信息体的判断
			if(checkBefore())
			{
				$.ajax({
				type:"post",
				url : pathUrl+"/eng_mtProcess.htm?method=previewSms", 
				data : {prId : msgLoopId,lgcorpcode:lgcorpcode},
				success : function(contents){
					$("#result").html="";
					var content=new Array();
					result = $("#msgMain").val();
					if(contents!="contentIsNull")
					{
						if(contents=="errorSms")
						{
							//alert("短信内容无法预览，请检查所使用的数据源是否有效或sql语句是否正确!");
							alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dxnrwfyl"));
							btnShow();
							return;
						}
						content = contents.split("|");
						for(var i=1;i<content.length+1;i=i+1){
							result = result.replaceAll("#p_"+i+"#", content[i-1]);
							result = result.replaceAll("#P_"+i+"#", content[i-1]);
							
							result = result.replace(/[\r\n]/g," ");
						}
					}
					else
					{
						//alert("select查询内容为空，请检查所使用的数据源是否有效，sql语句是否正确，是否有数据可查!");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_selectcxnrwk"));
						btnShow();
						return;
					}
					modify(result);
					btnShow();
				},
				error:function()
				{
					//alert("短信内容无法预览，请检查所使用的数据源是否有效或sql语句是否正确!");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_dxnrwfyl"));
					btnShow();
				}
				});
			}else{
				btnShow();
			}
		}
		
		function btnShow()
		{
			//$("#btnPreview").attr("value","预览");
			$("#btnPreview").attr("value",getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yl"));
			$("#btnPreview").removeAttr("disabled");
		}
		
		function modify(t)
		{
			$("#msg").children("xmp").empty();
			var tailcontents = $("#tailcontents").val();
			$("#msg").children("xmp").text(t+tailcontents);
			$('#modify').dialog('open');
		}
		
		//定义replaceAll方法
		String.prototype.replaceAll = function(s1,s2) {

		    return this.replace(new RegExp(s1,"gm"),s2);

		}