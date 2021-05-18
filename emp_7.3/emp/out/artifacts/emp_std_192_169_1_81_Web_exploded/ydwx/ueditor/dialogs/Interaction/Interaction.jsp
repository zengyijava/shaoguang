<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%


String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String langName = (String)session.getAttribute("emp_lang");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%@include file="/common/common.jsp" %>
    <title></title>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
    <style type="text/css">
        body {font-size: 12px;overflow:hidden;padding: 10px;margin:0;
        
        overflow:auto;/*自动出现滚动条，如果要出现竖直滚动条则改成：overflow-y:auto，如果横向出现滚动条则改成：overflow-x:auto*/
				scrollbar-face-color:#ccc;/*滚动条凸出部分的颜色(前景色),包括两端的方形按钮、水平或竖直滑动的滑块的颜色*/
				scrollbar-track-color:#ccc;/*滚动条的背景颜色,如果省略的话将出现虚点，颜色将采用face-color的颜色*/
				scrollbar-arrow-color:#ccc;/*按钮(上下或者左右可以点击使滑块滚动的方形按钮)上三角箭头的颜色*/
				scrollbar-3dlight-color:#ccc;/*滚动条亮边的颜色,形成3D效果，有层次感，肉眼观察在滚动条左边及上边出现一条有色线（竖直方向滚动）*/
				scrollbar-darkshadow-color:#ccc;/*滚动条强阴影的颜色,肉眼观察出现滚动条下边及右边*/
				scrollbar-highlight-color:#ccc;/*滚动条空白部分的颜色,肉眼观察改变不明显，具体颜色改变出现在左边和上边空白处，介于face-color效果与3dlingt-color效果之间有个空白颜色（默认为白色）。此外，滚动条前景色有种凹陷的感觉，周边线条颜色凸起*/
				scrollbar-shadow-color:#006600;/*立体滚动条阴影的颜色，具体出现在滑块及方形按钮的右边及下边，形成阴影效果，颜色介于face-color效果和darkshadow-color效果之间，不是很明显*/
				/*scrollbar-base-color:#ccc;滚动条的基本颜色,当前面7个效果出现时，滚动条基本颜色设置肉眼很难观察到，如果不设置前面7个效果,系统将根据base-color自动设置，其中前景色，背景色(虚点表示)颜色一致，其他效果(阴影以黑色填充),没有什么要求时，建议不设置此效果*/
				 
				scrollbar-face-color:#ccc;
				scrollbar-track-color:#FFF; 
				scrollbar-arrow-color:#FFF;
        }
        .tabWarper{width: 500px;overflow: hidden;position: relative;}
        .head{height: 35px;line-height: 35px;position: relative;z-index: 2}
        .head span {width: 62px; height: 28px; line-height: 28px;display: block; float: left;text-align: center;margin-right: 1px;cursor: pointer}
        .head span.def {
            background: url("../../themes/default/images/dialog-title-bg.png") repeat-x;
            border: 1px solid #ccc;
        }
        .head span.act {background: #FFF; border: 1px solid #ccc; border-bottom: 1px solid #FFF}
        .tabContainer{width: 2080px; position: relative;top: -6px;}
        .content{width: 488px;height: 220px;float: left; padding: 5px;margin-right: 20px; 
				 
				overflow:auto;/*自动出现滚动条，如果要出现竖直滚动条则改成：overflow-y:auto，如果横向出现滚动条则改成：overflow-x:auto*/
				scrollbar-face-color:#ccc;/*滚动条凸出部分的颜色(前景色),包括两端的方形按钮、水平或竖直滑动的滑块的颜色*/
				scrollbar-track-color:#ccc;/*滚动条的背景颜色,如果省略的话将出现虚点，颜色将采用face-color的颜色*/
				scrollbar-arrow-color:#ccc;/*按钮(上下或者左右可以点击使滑块滚动的方形按钮)上三角箭头的颜色*/
				scrollbar-3dlight-color:#ccc;/*滚动条亮边的颜色,形成3D效果，有层次感，肉眼观察在滚动条左边及上边出现一条有色线（竖直方向滚动）*/
				scrollbar-darkshadow-color:#ccc;/*滚动条强阴影的颜色,肉眼观察出现滚动条下边及右边*/
				scrollbar-highlight-color:#ccc;/*滚动条空白部分的颜色,肉眼观察改变不明显，具体颜色改变出现在左边和上边空白处，介于face-color效果与3dlingt-color效果之间有个空白颜色（默认为白色）。此外，滚动条前景色有种凹陷的感觉，周边线条颜色凸起*/
				scrollbar-shadow-color:#006600;/*立体滚动条阴影的颜色，具体出现在滑块及方形按钮的右边及下边，形成阴影效果，颜色介于face-color效果和darkshadow-color效果之间，不是很明显*/
				/*scrollbar-base-color:#ccc;滚动条的基本颜色,当前面7个效果出现时，滚动条基本颜色设置肉眼很难观察到，如果不设置前面7个效果,系统将根据base-color自动设置，其中前景色，背景色(虚点表示)颜色一致，其他效果(阴影以黑色填充),没有什么要求时，建议不设置此效果*/
				 
				scrollbar-face-color:#ccc;
				scrollbar-track-color:#FFF; 
				scrollbar-arrow-color:#FFF;
				 
			}
			td{height:30px;}
			.btn_b {
					BORDER-RIGHT: #7b9ebd 1px solid;
					PADDING-RIGHT: 2px;
					BORDER-TOP: #7b9ebd 1px solid;
					PADDING-LEFT: 2px;
					FONT-SIZE: 12px;
					FILTER: progid : DXImageTransform . Microsoft .
						Gradient(GradientType = 0, StartColorStr = #ffffff, EndColorStr =
						#cecfde);
					BORDER-LEFT: #7b9ebd 1px solid;
					CURSOR: hand;
					COLOR: black;
					PADDING-TOP: 2px;
					BORDER-BOTTOM: #7b9ebd 1px solid
				}
			.p_col{color:red;}
    </style>
    
    <link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame.css" />
	<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/table.css" />
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css"/>
	<link rel="stylesheet" type="text/css" href="<%=path%>/common/widget/jqueryui/css/jquery.ui.all.css">
	<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css">
    <%if(StaticValue.ZH_HK.equals(empLangName)){%>
    <link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <%}%>
    <script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js"></script>
    <script type="text/javascript" src="../jquery.form.js"></script>
    <script type="text/javascript" src="<%=path %>/common/widget/jqueryui/myjquery-j.js"></script>
    <script type="text/javascript" src="<%=path%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
    <script type="text/javascript" src="<%=path%>/common/js/common.js"></script>
    <script type="text/javascript" src="../internal.js"></script>
    <script type="text/javascript" src="<%=path%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=path%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
	<script type="text/javascript" src="<%=path%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>






</head>
<body >
<div >
<input id="lgcorpcode"  type="hidden" value="" />
<font color="red"><span id="dbcz"></span></font>
<table id="dataTable">
        <tr>
            <td style="width:<%="zh_HK".equals(empLangName)?120:60%>px;"><emp:message key="ydwx_wxcxtj_hftj_hdlbs" defVal="互动类别：" fileName="ydwx"></emp:message></td>
         <td>
         <select id="datesource" style="width:200px;"></select>
         <a href="javascript:Look()" style="margin-left:5px;" ><emp:message key="ydwx_jsp_out_xzhdx" defVal="新增互动项" fileName="ydwx"></emp:message></a>
         </td>
        </tr>
		<tr  >
                <td ><emp:message key="ydwx_wxbj_add_2" defVal="互  动  项：" fileName="ydwx"></emp:message></td>
			<td><input type="checkbox" id="checkAll"/><emp:message key="ydwx_wxbj_add_1" defVal="全选" fileName="ydwx"></emp:message></td>
		</tr>
</table>
<div id="divBox2" class="hideDlg" style="display:none" title="<emp:message key="ydwx_jsp_out_xzhdx" defVal="新增互动项" fileName="ydwx"></emp:message>">
	
		<iframe style="background: white;width:485px;height:320px; overflow-y: auto;" frameborder="0" 
			 id="nm_preview_common" scrolling="auto" src=""></iframe>
	
</div>
    
</div>




<%--完整的回调函数，有需要的可以完整取用--%>
<%--<script type="text/javascript" src="callbacks.js"></script>--%>
<script type="text/javascript">
	
	$(document).ready(function(){
	
	 	$("#divBox2").dialog({
			autoOpen: false,
			width: 485,
			modal: true,
			close:function(){
				refreshActive();
			}
	  	});
	  	
	});

//选择类型
	function select_type(){
			document.getElementById("sp_msg").innerHTML=""; 
			var type=$("#_type").val();
			 
			if(type=="1")
				switchTab('tab1',0)
			else if(type=="2")
				switchTab('tab2',1)
			else if(type=="3")
				switchTab('tab3',2)
			else  
				switchTab('tab4',3)
	}

</script>
<script type="text/javascript">

   		  $(function(){
           
           //进来加载数据源
           var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
           $.post("<%=path %>/wx_ueditor.htm?method=getActiviTables",{lgcorpcode:lgcorpcode},function(data){
                    var list=eval("("+data+")");
                    var tname=$("#datesource");
                    $("<option>").val("0").html(getJsLocaleMessage("ydwx","ydwx_common_qxz")).appendTo(tname);
                    for(var i=0;i<list.length;i++){
                          var options=$("<option>").val(list[i].id).html(list[i].name).appendTo(tname);
                    }
                    
           });
           
           $("#datesource").change(function(){
				var dataTypeId=$("#datesource").val();
              	$.post("<%=path %>/wx_ueditor.htm?method=getActiviCols",{dataTypeId:dataTypeId},function(data){
              		//改变前清空原来的
              		clearTable();
                    var list=eval("("+data+")");
                    var cname=$("#dataTable");
                    var parDataId = $("#dataId",parent.document).val();
                    for(var i=0;i<list.length;i++){
                        var trid = dataTypeId + "_" + list[i].did;
                        var trhtml = $("#" + trid).html();
                        if (trhtml == null || trhtml.length == 0){
                    	    var htmlT = "<td></td><td><input type='checkbox' name='cbData' onclick='cancelChecked(this)' id='d_"+list[i].did+"' quesType='"+list[i].quesType+"' colName='"+list[i].colName+"' value='"+list[i].did+"'  /><span id='spq_"+list[i].did+"'>"+list[i].quesContent+"</span></td>";
                            var options=$("<tr id='" + trid + "' >").html(htmlT).appendTo(cname);
                        }
                    }
          	 	});
           });
 
      });
      
   function clearTable(){
		var trslength= $("#dataTable").find("tr").length;
		for(var i=trslength;i>=2;i--) //保留最前面两行！
		{
		    $("#dataTable").find("tr").eq(i).remove();
		}
   }
   		  
   function refreshActive(){
   	     var dataTypeId=$("#datesource").val();
         //清空列表
   		 $("#datesource").empty();
 		 //进来加载数据源
         var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
         $.post("<%=path %>/wx_ueditor.htm?method=getActiviTables",{lgcorpcode:lgcorpcode},function(data){
               var list=eval("("+data+")");
               var tname=$("#datesource");
               $("<option>").val("0").html(getJsLocaleMessage("ydwx","ydwx_common_qxz")).appendTo(tname);
               for(var i=0;i<list.length;i++){
                   var options=$("<option>").val(list[i].id).html(list[i].name).appendTo(tname);
               	   if(list[i].id == dataTypeId){
               	   	  options.attr("selected", true);
               	   }
               }
                  
         });
         
   }   
      

    dialog.onok = function (){ 
    	
    	var dId = "";
		$("input[name='cbData']:checkbox:checked").each(function(){
			if($(this).val() != null){
				dId += $(this).val()+",";
			}
			
		});
		
		if(dId == null || dId.length==0){
			alert(getJsLocaleMessage("ydwx","ydwx_wxbj_130"));
			return;
			
		}else{
			
			dId = dId.substring(0,dId.length-1);
		}
		
		var parDataId = $("#dataId",parent.document).val();
		if(parDataId != null && parDataId.length>0){
			parDataId = parDataId+",";
		}
		$("#dataId",parent.document).val(parDataId+dId);
		
		$.post("<%=path %>/wx_ueditor.htm?method=getDataHtml",{dId:dId},function(data){
			var list=eval("("+data+")");
			var tempDId=0;
			var quesText = "";
			var quesType = 0;
			var colName = "";
			var eHtml = "";
			$("input[name='cbData']:checkbox:checked").each(function(){
				tempDId = $(this).val();
				quesType = $(this).attr("quesType");
				colName = $(this).attr("colName");
				quesText = $("#spq_"+tempDId).html();
				eHtml += "<p>&nbsp;"+quesText+"<br>";
				if(quesType==1){
					eHtml += "&nbsp;<input id=\""+colName+"\" type=\"text\"  name=\""+colName+"\" /><br>";
				}
				else if(quesType==4){
					eHtml += "&nbsp;<select id=\""+colName+"\"  name=\""+colName+"\" >";
				}
				for(var i=0;i<list.length;i++){
					if(tempDId != null && tempDId == list[i].dId){
						eHtml += getQuesHtml(quesType, colName, list[i].name);
					}
				}
				if(quesType==4){
					eHtml += "</select>";
				}
				eHtml += "</p>"
			});
			
			var formHtml = "<form action=\"<%=path %>/wx_inter.hts?method=interaction\" method=\"post\">";
			var btnHtml = "<p><input type=\"submit\" value=\""+getJsLocaleMessage("common","common_confirm")+"\" name=\"inter\"></p><br/></form>";
			var content = editor.getContent();
			if(content.indexOf("name=\"inter\"")>-1 ){
				editor.execCommand('insertinteraction',eHtml);
			}else{
				editor.execCommand('insertinteraction',formHtml+eHtml+btnHtml);
				
			}
			dialog.close();
		});
         //editor.execCommand('insertinteraction',"<form action=\"<%=path %>/wx_ueditor.htm?method=interaction\" method=\"post\">"+document.getElementById("show").innerHTML+"<p><input type=\"button\" value=\""+getJsLocaleMessage("common","common_confirm")+"\" name=\"inter\"></p><br/></form>");
       	
    };
    
    function getQuesHtml(quesType, colName, content){
		var qhtml = "";
		if(quesType == 2){
			qhtml += "&nbsp;<input id=\""+colName+"\" type=\"radio\" name=\""+colName+"\" value=\""+content+"\"  />"+content+"<br>";
		}
		else if(quesType == 3){
			qhtml += "&nbsp;<input id=\""+colName+"\" type=\"checkbox\" name=\""+colName+"\" value=\""+content+"\"  />"+content+"<br>";
		}
		else if(quesType == 4){
			qhtml += "<option value=\""+content+"\" />"+content+"</option>";
		}
		return qhtml;
	}
    
    $("#checkAll").click(function(){
    	if(!$("#checkAll").attr("checked")){
    		$("input[type='checkbox']").removeAttr("checked");
    	}else{
    		if(($("input[type='checkbox'][name='cbData']").length) > 0){
    		    $("input[type='checkbox']").attr("checked","true");
    		}else{
    			$("#checkAll").removeAttr("checked");
    		}
    	}
		 
	});
	
	function cancelChecked(box){
		if(!$(box).attr("checked")){
		   
		   if($("#checkAll").attr("checked")){
			  
			  $("#checkAll").removeAttr("checked");
		   }
		}else{
		   var blAllcheck = true;
		   $("input[type='checkbox'][name='cbData']").each(function(){ 
		      if(!$(this).attr("checked")){ 
				blAllcheck = false;
			  }
		   });
		   
		   if(blAllcheck){
		      $("#checkAll").attr("checked","true");
		   }
		}
	}
    
    function checkQues(dId){
    	
    	var parDataId = $("#dataId",parent.document).val();
    	
    	if(parDataId.indexOf(dId+"")>-1 ){
    		$("#d_"+dId).removeAttr("checked");
    		alert(getJsLocaleMessage("ydwx","ydwx_wxbj_131"));
    		
    	}
    	
    }
    
	<%--弹出新增互动dialog --%>
	function Look(){
	    var lguserid = $(window.parent.document).find("#lguserid").val();
		var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
		$("#lgcorpcode").val(lgcorpcode);
		var url ="<%=path%>/wx_trustdata.htm?method=toAddInteraction&lgcorpcode="+ lgcorpcode + "&lguserid=" + lguserid;
		$("#nm_preview_common").attr("src",url);
		$("#divBox2").dialog("open");	
	}
	
	<%--关闭新增互动dialog --%>
	function closeDialog(){
		$("#nm_preview_common").attr("src","");
		$("#divBox2").dialog("close");	
	}
	<%-- 刷新互动项 --%>
	function refreshInterType(){
		var dataTypeId=$("#datesource").val();
        $.post("<%=path %>/wx_ueditor.htm?method=getActiviCols",{dataTypeId:dataTypeId},function(data){
            var list=eval("("+data+")");
            var cname=$("#dataTable");
            var parDataId = $("#dataId",parent.document).val();
            for(var i=0;i<list.length;i++){

				if(parDataId.indexOf(list[i].did)>-1 || $("#d_"+list[i].did).val() != null ){
					continue;
				}
				
          		var htmlT = "<td></td><td><input type='checkbox' name='cbData' id='d_"+list[i].did+"' quesType='"+list[i].quesType+"' colName='"+list[i].colName+"' value='"+list[i].did+"'  /><span id='spq_"+list[i].did+"'>"+list[i].quesContent+"</span></td>";
             	var options=$("<tr>").html(htmlT).appendTo(cname);
            }
    	 });
		
	}
	
</script>
</body>
</html>