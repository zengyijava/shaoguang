<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path=request.getContextPath();
String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head><%@include file="/common/common.jsp" %>
    <title></title>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
    <style type="text/css">
        body {font-size: 12px;overflow:hidden;padding: 10px;margin:0;color: #838383;
        
        }
        .tabWarper{width: 400px;overflow: hidden;position: relative;}
        .head{height: 35px;line-height: 35px;position: relative;z-index: 2}
        .head span {width: 62px; height: 28px; line-height: 28px;display: block; float: left;text-align: center;margin-right: 1px;cursor: pointer}
        .head span.def {
            background: url("../../themes/default/images/dialog-title-bg.png") repeat-x;
            border: 1px solid #ccc;
        }
        .head span.act {background: #FFF; border: 1px solid #ccc; border-bottom: 1px solid #FFF}
        .tabContainer{width: 400px; position: relative;top: -6px;}
        .content{width: 380px;height: 170px;float: left; padding: 5px;margin-right: 20px; 
				 
				 
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
	
	<link href="<%=inheritPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
	<link href="<%=inheritPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=inheritPath%>/common/css/empSelect.css" rel="stylesheet" type="text/css" />
	<link href="<%=skin%>/frame.css" rel="stylesheet" type="text/css" />
	<link href="<%=skin%>/table.css" rel="stylesheet" type="text/css" />
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
	<script type="text/javascript" src="<%=path%>/ydwx/scripts/survey.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=path%>/ydwx/ueditor/dialogs/myjquery-f.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=path%>/ydwx/ueditor/dialogs/jquery.form.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
</head>
<body>
<div class="tabWarper">

<table cellpadding="0" cellspacing="0">

          <tr>
          	<td><emp:message key="ydwx_leftInter_text_1" defVal="问题类型：" fileName="ydwx"></emp:message></td>
            <td > 
            <span>
             	 
             	 		<select style="width:305px;" id="_type" onchange="select_type()">
				        	<option value="1"><emp:message key="ydwx_wxgl_hdxgl_huida" defVal="回答类" fileName="ydwx"></emp:message></option>
							<option value="4"><emp:message key="ydwx_wxgl_hdxgl_danxuan" defVal="单选类" fileName="ydwx"></emp:message></option>
							<option value="2"><emp:message key="ydwx_wxgl_hdxgl_duoxuan" defVal="多选类" fileName="ydwx"></emp:message></option>
							<option value="3"><emp:message key="ydwx_wxgl_hdxgl_xialakuang" defVal="下拉框类" fileName="ydwx"></emp:message></option>
				         </select>
            </span>
         </td>
        </tr>
          <tr style="display:none;">
            <td colspan="2"> 
            <span>
             	<emp:message key="ydwx_leftInter_text_3" defVal="互&nbsp;&nbsp;动&nbsp;字&nbsp;段：" fileName="ydwx"></emp:message>
             	<select id="table_ZD" onchange="cols_name()" style="width:200px;"></select>
             </span><emp:message key="ydwx_leftInter_text_2" defVal="*同一字段只能使用一次" fileName="ydwx"></emp:message><div class="p_col" id="sp_msg"></div>
         </td>
        </tr>
	<tr>
		<td><emp:message key="ydwx_leftInter_text_4" defVal="问题内容：" fileName="ydwx"></emp:message></td>
		<td > 
			<span>
			
			<textarea name="showname" id="showname" style="width:300px;height:100px;overflow-y:auto;"></textarea>
			</span>
		</td>
	</tr>
		<tr>
		<td><emp:message key="ydwx_leftInter_text_5" defVal="是否换行：" fileName="ydwx"></emp:message></td>
		<td> 
			<input type="radio" name="br_name" id="br_name" value="0" checked=checked/><emp:message key="ydwx_leftInter_text_6" defVal="换行" fileName="ydwx"></emp:message>&nbsp;&nbsp;
			<input type="radio" name="br_name" id="br_name" value="1" /><emp:message key="ydwx_leftInter_text_7" defVal="不换行" fileName="ydwx"></emp:message>
		</td>
	</tr>
</table>
    <div id="tabs" class="tabContainer">
        <div id="tab1" class="content" >
         	<P>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         	<font color="#0000FF"><emp:message key="ydwx_leftInter_text_8" defVal="展示结果,例如：" fileName="ydwx"></emp:message><emp:message key="ydwx_leftInter_text_9" defVal="输入框:" fileName="ydwx"></emp:message><input type="text" name="_input" size="10" /></font></p>
        	<div id="tab1_div" style="">
       			<span id="value_1"></span>
       			   
       			 
        	</div>
        		
        </div>
        
        <div id="tab2" class="content" style="overflow-y:scroll;display:none;">
        
        	<p><a id="_name" href="javascript:showadd('_name','2');"><emp:message key="ydwx_wxgl_hdxgl_tjsx" defVal="添加选项" fileName="ydwx"></emp:message></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<font color="#0000FF"><emp:message key="ydwx_leftInter_text_8" defVal="展示结果,例如：" fileName="ydwx"></emp:message><emp:message key="ydwx_leftInter_text_10" defVal="多选框:" fileName="ydwx"></emp:message><input type="checkbox" name="_name" /><emp:message key="ydwx_leftInter_text_13" defVal="模型" fileName="ydwx"></emp:message>1&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="_name" /><emp:message key="ydwx_leftInter_text_13" defVal="模型" fileName="ydwx"></emp:message>2</font></p>
        		<div id="tab2_div" style="display:none;">
        			<span id="value_2" ></span>
        			
        		</div>
        </div>
        
        <div id="tab3" class="content" style="overflow-y:scroll;display:none;">
        	<p><a id="_select" href="javascript:showadd('_select','3');"><emp:message key="ydwx_wxgl_hdxgl_tjsx" defVal="添加选项" fileName="ydwx"></emp:message></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<font color="#0000FF"><emp:message key="ydwx_leftInter_text_8" defVal="展示结果,例如：" fileName="ydwx"></emp:message><emp:message key="ydwx_leftInter_text_11" defVal="下拉框：" fileName="ydwx"></emp:message><select name="_select" style="width:100px;"><option><emp:message key="ydwx_leftInter_text_13" defVal="模型" fileName="ydwx"></emp:message>1</option><option><emp:message key="ydwx_leftInter_text_13" defVal="模型" fileName="ydwx"></emp:message>2</option></select></font></p>
        	
        	<div id="tab3_div" style="display:none;">
       			<span id="value_3"></span>
       			  
       		</div>
        </div>
        
        <div id="tab4" class="content" style="overflow-y:scroll;display:none;">
        	 <p><a id="_radio" href="javascript:showadd('_radio','4');"><emp:message key="ydwx_wxgl_hdxgl_tjsx" defVal="添加选项" fileName="ydwx"></emp:message></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<font color="#0000FF"><emp:message key="ydwx_leftInter_text_8" defVal="展示结果,例如：" fileName="ydwx"></emp:message><emp:message key="ydwx_leftInter_text_12" defVal="单选框：" fileName="ydwx"></emp:message><input type="radio" name="_radio" value="" checked="checked"><emp:message key="ydwx_leftInter_text_13" defVal="模型" fileName="ydwx"></emp:message>1&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="_radio" value=""><emp:message key="ydwx_leftInter_text_13" defVal="模型" fileName="ydwx"></emp:message>2</font></p>
        	<div id="tab4_div" style="display:none;">
       			<span id="value_4"></span>
       			  
       		</div>
        </div>
        
    </div>
    <input type="button" value="<emp:message key="ydwx_leftInter_text_14" defVal="插入" fileName="ydwx"></emp:message>" onclick="_add('_name','1')" class="btnClass1" />
    <input type="button" value="<emp:message key="ydwx_leftInter_text_15" defVal="插入按钮" fileName="ydwx"></emp:message>" onclick="insertOk()" class="btnClass3" />
</div>
<div style="display:none">
	<emp:message key="ydwx_leftInter_text_16" defVal="效果展示：" fileName="ydwx"></emp:message>
	<div id="show"></div>
	
</div>




<%--完整的回调函数，有需要的可以完整取用--%>
<%--<script type="text/javascript" src="callbacks.js"></script>--%>
<script type="text/javascript">
//选择类型
	function select_type(){
			document.getElementById("sp_msg").innerHTML=""; 
			var type=$("#_type").val();

			if(type=="1")
			{
				$("#tab1").css("display","");
				changeDisp(1);
				//switchTab('tab1',0)
			}
			else if(type=="2")
			{
				$("#tab2").css("display","");
				changeDisp(2);
				//switchTab('tab2',1)
			}
			else if(type=="3")
			{
				$("#tab3").css("display","");
				changeDisp(3);
				//switchTab('tab3',2)
			}
			else if(type == "4")
			{
				$("#tab4").css("display","");
				changeDisp(4);
				//switchTab('tab4',3)
			}
				
			
	}

	function changeDisp(tab)
	{
		for(i=1;i<5;i++)
		{
			if(i == tab)
			{
				continue;
			}
			$("#tab"+i).css("display","none");
		}
	}
</script>
<script type="text/javascript">
	var content = editor.getContent(); 
	
	
	 var obj1= document.getElementById("datesource");
	 var index = 0; 

//得到id  还是表名  0 ---表名  1--id 
         var redata=function(obj,type){
                var data=obj.split("-");
                if(type==0){
                  return data[0];
                }
                else{
                    return data[1];
                }
         }

      $(function(){
           
        //进来加载数据源
           $.post("<%=path %>/wx_ueditor.htm?method=getActiviTables",{type:1},function(data){
                    var list=eval("("+data+")");
                    var tname=$("#datesource");
                    for(var i=0;i<list.length;i++){
                          var options=$("<option>").val(list[i].TABLENAME+"-"+list[i].ID).html(list[i].NAME).appendTo(tname);
                    }
                    if(list.length!=0){
		                 $.post("<%=path %>/wx_ueditor.htm?method=getActiviCols",{tid:list[0].ID},function(data){
		                    var list=eval("("+data+")");
		                    var cname=$("#table_ZD");
		                    document.getElementById("table_ZD").options.length=0;
		                    for(var i=0;i<list.length;i++){
		                     	  if(i==0){
                    	 			  	document.getElementById("showname").value = list[i].NAME;
                    	 		  }
		                          var options=$("<option>").val(list[i].COLNAME+"-"+list[i].COLTYPE).html(list[i].NAME).appendTo(cname);
		                    }
	                   
	             	 	});
                  	}   
           });
           
           $("#datesource").change(function(){
           			var show = document.getElementById("show").innerHTML;
					if(show!=''){
						obj1.options[index].selected = true;
						/*请先删除效果展示里的数据！*/
						alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_1"));
					}else{
						index = obj1.selectedIndex; 
					}
           
                     var oid=$(this).val();
                     var tid=redata(oid,1);
              	$.post("<%=path %>/wx_ueditor.htm?method=getActiviCols",{tid:tid},function(data){
                    var list=eval("("+data+")");
                    var cname=$("#table_ZD");
                    document.getElementById("table_ZD").options.length=0;
                    for(var i=0;i<list.length;i++){
                    	 if(i==0){
                    	 	document.getElementById("showname").value = list[i].NAME;
                    	 }
                          var options=$("<option>").val(list[i].COLNAME+"-"+list[i].COLTYPE).html(list[i].NAME).appendTo(cname);
                    }
                    
          	 	});
           
           });
          
        
               
      });
 
var zd_value = $("select[id=table_ZD] option[selected]").val(); 	 
	var table_string  ; 
	function table_str(){
		if(document.getElementById("table_ZD")&& document.getElementById("table_ZD").value==""){
		    /*数据源对应的数据字典不存在(互动字段)*/
			document.getElementById("sp_msg").innerHTML=getJsLocaleMessage("ydwx","ydwx_leftInter_text_2");
			return false;
		}
		zd_value = $("select[id=table_ZD] option[selected]").val();  
		table_string =  zd_value.split("-")[0];  
		var ta_name = document.getElementById(table_string);
		if(ta_name){
		    /*数据字典:   已存在*/
			alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_3")+"'"+ $("select[id=table_ZD] option[selected]").text()+"'"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_4"));
			return false;
		}else{ 
			return true; 
		}
	}
	
	var _help = ""  ; 
	//选择名称 默认显示值 
	function cols_name(){
		zd_value = $("select[id=table_ZD] option[selected]").val();
		//显示标题附值
		document.getElementById("showname").value =$("select[id=table_ZD] option[selected]").html();
		_help = zd_value.split("-")[1];
		type_cols = _help; //用来做数据类型验证
		if(_help=='1'){
		    /*输入数字*/
			_help = getJsLocaleMessage("ydwx","ydwx_leftInter_text_5") ;
		}else if(_help=='2'){
		    /*格式:1980-01-01*/
			_help = getJsLocaleMessage("ydwx","ydwx_leftInter_text_6");
		}else{
			_help = "";
		}
	}
	
	var tab1_div_conunt = 1;
	var tab2_div_conunt = 1;
	var tab3_div_conunt = 1;
	var tab4_div_conunt = 1;
	var type_cols ;  // 判断数据类型
	function showadd(date , tab){  //点添加

        if(_help=='1'){
            /*输入数字*/
            _help = getJsLocaleMessage("ydwx","ydwx_leftInter_text_5") ;
        }else if(_help=='2'){
            /*格式:1980-01-01*/
            _help = getJsLocaleMessage("ydwx","ydwx_leftInter_text_6");
        }else{
            _help = "";
        }
		if(tab=="1"){ 
			
			document.getElementById("tab1_div").style.display="";
	//		var value_1 =  document.getElementById("value_1").innerHTML;   
	//		if(tab2_div_conunt==1)
			if(tab1_div_conunt>1){
			    /*不能再添加*/
				alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_8"));
			}
				/*提&nbsp;示&nbsp;字&nbsp;符:*/
				document.getElementById("value_1").innerHTML = "<P>"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_9")+"<input type=\"text\" size=\"20\"  value =\"\"  /></P>";
	//		else
	//			document.getElementById("value_2").innerHTML = value_1+"<P>显示值：<input type=\"text\" size=\"5\"  id=\"tab2_div_value_2_"+tab2_div_conunt+"\" /><a id=\"value_2_"+date+"_"+tab+"\" href=\"javascript:del('value_2_"+date+"_"+tab+"','tab2_div_conunt');\">移除</a></P>";  
				
			tab1_div_conunt = Number(tab1_div_conunt)+Number(1);   
			
	//		$("#tab1_div_name_2_1").val(showname);  //显示名称 默认显示值
			
		}else if(tab=="2"){
		
			document.getElementById("tab2_div").style.display="";
			var value_2 =  document.getElementById("value_2").innerHTML;  
			if(tab2_div_conunt==1)
			    /*显&nbsp;&nbsp;示&nbsp;&nbsp;值：*/
				document.getElementById("value_2").innerHTML = value_2+getJsLocaleMessage("ydwx","ydwx_leftInter_text_10")+"<input type=\"text\" size=\"20\"  id=\"tab2_div_value_2_"+tab2_div_conunt+"\" /></P>";
			else
            	/*显&nbsp;&nbsp;示&nbsp;&nbsp;值：  移除*/
				document.getElementById("value_2").innerHTML = value_2+"<P>"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_10")+"<input type=\"text\" size=\"20\"  id=\"tab2_div_value_2_"+tab2_div_conunt+"\" /><a id=\"value_2_"+date+"_"+tab+"_"+tab2_div_conunt+"\" href=\"javascript:del('value_2_"+date+"_"+tab+"_"+tab2_div_conunt+"','tab2_div_conunt');\">"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_11")+"</a></P>";
				
			tab2_div_conunt = Number(tab2_div_conunt)+Number(1);  
			
	//		$("#tab2_div_name_2_1").val(showname);  //选择名称 默认显示值
			
		}else if(tab=="3"){
		
			document.getElementById("tab3_div").style.display="";
			var value_3 =  document.getElementById("value_3").innerHTML;  
			if(tab3_div_conunt==1)
            /*显&nbsp;&nbsp;示&nbsp;&nbsp;值：*/
				document.getElementById("value_3").innerHTML = value_3+"<P>"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_10")+"<input type=\"text\" size=\"20\"  id=\"tab3_div_value_2_"+tab3_div_conunt+"\" /></P>";
			else
            /*显&nbsp;&nbsp;示&nbsp;&nbsp;值：*/
				document.getElementById("value_3").innerHTML = value_3+"<P>"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_10")+"<input type=\"text\" size=\"20\"  id=\"tab3_div_value_2_"+tab3_div_conunt+"\" /><a id=\"value_3_"+date+"_"+tab+"_"+tab3_div_conunt+"\" href=\"javascript:del('value_3_"+date+"_"+tab+"_"+tab3_div_conunt+"','tab3_div_conunt');\">"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_11")+"</a></P>";
				
			tab3_div_conunt = Number(tab3_div_conunt)+Number(1);  
			
	//		$("#tab3_div_name_2_1").val(showname);  //显示名称 默认显示值
			
		}else{
			
			document.getElementById("tab4_div").style.display="";
			var value_4 =  document.getElementById("value_4").innerHTML;  
			if(tab4_div_conunt==1)
            /*显&nbsp;&nbsp;示&nbsp;&nbsp;值：*/
				document.getElementById("value_4").innerHTML = value_4+"<P>"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_10")+"<input type=\"text\" size=\"20\"  id=\"tab4_div_value_2_"+tab4_div_conunt+"\" /></P>";
			else
            /*显&nbsp;&nbsp;示&nbsp;&nbsp;值：*/
				document.getElementById("value_4").innerHTML = value_4+"<P>"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_10")+"<input type=\"text\" size=\"20\"  id=\"tab4_div_value_2_"+tab4_div_conunt+"\" /><a id=\"value_4_"+date+"_"+tab+"_"+tab4_div_conunt+"\" href=\"javascript:del('value_4_"+date+"_"+tab+"_"+tab4_div_conunt+"','tab4_div_conunt');\">"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_11")+"</a></P>";
				
			tab4_div_conunt = Number(tab4_div_conunt)+Number(1); 
			 
	//		$("#tab4_div_name_2_1").val(showname);  //显示名称 默认显示值
		
		}
		
	}
	//第二个checkbox_add增加
	function _add(date , tab){
		
		var showname ;
		if(document.getElementById("showname").value==""){
		    /*问题内容不能为空！*/
			alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_7"));
			return false;
		}
		showname = document.getElementById("showname").value;
		//判断是否换行
		var isNewLine = $("input[name='br_name']:checked").val();
		if(isNewLine == "0"){
			showname= showname+"：<br/>" ; 
		}else{
			showname= showname+"：" ; 
		}
		
			//var zd_value = $("#table_ZD").val();
			//table_string =  zd_value.split("-")[0];  
			var strNum = $('#colNum', window.parent.document).val();
			var colNum = parseInt(strNum)+1;
			var strColNum = "c"+colNum;
			
			var colValue = $('#colValue', window.parent.document).val();
			
			tab = $("#_type").val();
			
			var str = "<p>"+showname;   //$("#table_ZD").find("option:selected").text() 
			if(tab=="1"){ 
				
				colValue += strColNum + "," + tab + "," + "1024" + ";";
				str = str+"<input id=\""+strColNum+"\" type=\"text\" size=\"20\" name=\""+strColNum+"\" />";
				str =str +_help+"</p>";  
				
				var oldvalue = document.getElementById("show").innerHTML;
				
				document.getElementById("show").innerHTML=oldvalue+str;
				onokExc();
			}else if(tab=="2"){
				colValue += strColNum + "," + tab + "," + "250" + ";";
				var count_2 ;
			//	var str = "<p>"+document.getElementById("tab2_div_name_2_1").value+"：";
				var strv_2="";
				for(count_2 =  Number(tab2_div_conunt)-Number(1); count_2>0; count_2--){
					var value_if = document.getElementById("tab2_div_value_2_"+count_2);
					if(value_if){ 
					//	var name = document.getElementById("tab2_div_name_2_"+count).value; 
						var value = value_if.value;
						if(value==""){
						    /*显示值不能为空*/
							alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_12"));
							return false;
						}else{
							if(type_cols=="1"){
								if(isNaN(value)){
								    /*显示值只能为数字！*/
									alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_13"));
									return false;
								}
							}else if(type_cols=="2"){
                                /*日期格式字典只能用输入框表示！*/
                                alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_14"));
							}
						}
						strv_2 = "<input id=\""+strColNum+"\" type=\"checkbox\" name=\""+strColNum+"\" value=\""+value+"\"  />"+value+"&nbsp;"+strv_2; 
					}
				}
				str =str +strv_2 +"</p>"; 
				var oldvalue = document.getElementById("show").innerHTML;
				document.getElementById("show").innerHTML=oldvalue+str;
				onokExc();
				
			}else if(tab=="3"){
				colValue += strColNum + "," + tab + "," + "250" + ";";
				var count_3 ;
				var strv_3="";
		//	 	var str = "<p>"+document.getElementById("tab3_div_name_2_1").value+"：
				str =str +"<select id=\""+strColNum+"\"  name=\""+strColNum+"\"  style=\"width:120px;\">";  
				for(count_3 =  Number(tab3_div_conunt)-Number(1); count_3>0; count_3--){
					var value_if = document.getElementById("tab3_div_value_2_"+count_3);
					if(value_if){ 
						var value = value_if.value;
						if(value==""){
                            /*显示值不能为空*/
                            alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_12"));
							return false;
						}else{
							if(type_cols=="1"){
								if(isNaN(value)){
                                    /*显示值只能为数字！*/
                                    alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_13"));
									return false;
								}
							}else if(type_cols=="2"){
                                /*日期格式字典只能用输入框表示！*/
                                alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_14"));
									return false;
							}
						}
						strv_3 = "<option value=\""+value+"\" />"+value+"&nbsp;"+strv_3;  
					}
				}
				str =str+strv_3+"</select></p>"; 
				var oldvalue = document.getElementById("show").innerHTML;
				document.getElementById("show").innerHTML=oldvalue+str;
				onokExc();
			
			}else if(tab=="4"){
				colValue += strColNum + "," + tab + "," + "250" + ";";
				var count_4 ;
				var strv_4=""; 
		//		var str = "<p>"+document.getElementById("tab4_div_name_2_1").value+"："; 
				for(count_4 =  Number(tab4_div_conunt)-Number(1); count_4>0; count_4--){
					var value_if = document.getElementById("tab4_div_value_2_"+count_4);
					if(value_if){ 
						var value = value_if.value;
						if(value==""){
                            /*显示值不能为空*/
                            alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_12"));
							return false;
						}else{
							if(type_cols=="1"){
								if(isNaN(value)){
                                    /*显示值只能为数字！*/
                                    alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_13"));
									return false;
								}
							}else if(type_cols=="2"){
							    /*日期格式字典只能用输入框表示！*/
								alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_14"));
									return false;
							}
						}
						if(count_4=="1")
							strv_4 = "<input id=\""+strColNum+"\" type=\"radio\" name=\""+strColNum+"\" value=\""+value+"\" checked=\"checked\" />"+value+"&nbsp;"+strv_4; 
						else
							strv_4 = "<input id=\""+strColNum+"\" type=\"radio\" name=\""+strColNum+"\" value=\""+value+"\" />"+value+"&nbsp;"+strv_4;
					}
				}
				str =str+strv_4 +"</p>";  
				var oldvalue = document.getElementById("show").innerHTML; 
				document.getElementById("show").innerHTML=oldvalue+str;
				onokExc();
			}
		$('#colValue', window.parent.document).val(colValue);

		document.getElementById("value_2").innerHTML="";
		document.getElementById("tab2_div").style.display="none";
		document.getElementById("value_3").innerHTML="";
		document.getElementById("tab3_div").style.display="none";
       	document.getElementById("value_4").innerHTML="";
		document.getElementById("tab4_div").style.display="none";
		//document.getElementById("br_name").checked=false;
		
	}
	function add(date , tab){
		var addvalue = $("#"+date).parent().html();
		var showvalue= "<p>"+addvalue.split("<A")[0]+"<a id=\""+date+"_"+tab+"\" href=\"javascript:del('"+date+"_"+tab+"','"+tab+"');\">"+getJsLocaleMessage("ydwx","ydwx_leftInter_text_11")+"</a></p>";
		var oldvalue = document.getElementById("show").innerHTML;
		document.getElementById("show").innerHTML=oldvalue+showvalue;
		
	}
	function del(date , tab){
		$("#"+date).parent().remove();
	//	if(tab="tab2_div_conunt"){
	//		tab2_div_conunt = Number(tab2_div_conunt)-Number(1); 
	//	}
	}
     function g(id){
        return document.getElementById(id);
     }
    function switchTab(obj,index){
  //     clearFocusClass(); 
  //      obj.className = "act"; 
        g("tabs").style.left = index * (-520) + "px";
    }
    function clearFocusClass(){
        var heads = g("head").children;
        for(var i= 0,j;j = heads[i++];){
            j.className = "def";
        }
    }
    function onokExc(){ 
    	
    	var content = document.getElementById("show").innerHTML;
		//var edCont = editor.getContent();
         editor.execCommand('insertinteraction', content);
       	 //dialog.close();
       	 document.getElementById("show").innerHTML="";
       	 var strColNum = $('#colNum', window.parent.document).val();
       	 var colNum = parseInt(strColNum)+1;
       	 $('#colNum', window.parent.document).val(colNum);
    }
    function insertOk()
    {
    	var strOk = "<p><input type=\"button\" value=\""+getJsLocaleMessage("common","common_confirm")+"\" name=\"inter\"></p><br/>";
    	var edCont = editor.getContent();
    	var strForm = "";
    	var strFormEnd = "";
    	if(edCont == null || edCont.length == 0 || edCont.indexOf('form') == -1)
    	{
    		strForm ="<form action=\"<%=path %>/wx_ueditor.htm?method=interaction&table=aaa\" method=\"post\">";
    		strFormEnd = "</form>";
    	}
    	if(edCont != null && edCont.length > 0 && edCont.indexOf('name=\"inter\"') > -1)
    	{
    		strOk = "";
    		/*已添加按钮！*/
    		alert(getJsLocaleMessage("ydwx","ydwx_leftInter_text_15"));
    		return;
    	}
    	var content = strForm+edCont+strOk+strFormEnd;
    	editor.setContent(content);
		//editor.execCommand('insertinteraction', content);
    }

</script>
</body>
</html>