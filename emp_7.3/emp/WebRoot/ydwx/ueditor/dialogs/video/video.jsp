
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String langName = (String)session.getAttribute("emp_lang");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head><%@include file="/common/common.jsp" %>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
        <title>插入视频</title>
        <script type="text/javascript" src="../internal.js"></script>
        
       	<script type="text/javascript" src="../myjquery-f.js"></script>
     	
        <script type="text/javascript" src="../jquery.form.js"></script>
    <style type="text/css">
        *{color: #838383}
        body {
            font-size: 12px;
            width:360px;
            overflow: hidden;
            margin:0px;padding:0px;
        }
        .content{
            padding: 9px 0px 0px 15px;
            height:100%;
        }
        .content table{padding:0px;margin:0px;width: 100%}
        .content table tr{padding:0px;margin:0px; list-style: none;height: 30px; line-height: 30px;}
        .content input{ width:60px;height:21px;background: #FFF;border:1px solid #d7d7d7;padding: 0px; margin: 0px; line-height:21px;}
        .content label{ width:60px;display:block;float:left;}
        #url{width:250px}
        .red{color:red;}
    </style>
    <%if(StaticValue.ZH_HK.equals(empLangName)){%>
    <link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <style type="text/css">
        body > div > table > tbody > tr:nth-child(2) > td > label{
            width: 85px;
        }
        #url{width:200px}
    </style>
    <%}%>
    </head>
    <body onload="javascript:loadfile('1');">
        <div class="content">
        <table >
            <tr>
                <td colspan="2"> <span><emp:message key="ydwx_wxbj_add_23" defVal="选择视频：" fileName="ydwx"></emp:message></span>
                    <select id="_video" onclick="chang()" style="width:200px;">
                    </select><br/>
                    <span id="msgfile" class="red"></span>
                </td>
            </tr>
            <tr>
                <td colspan="2"><label for="url"><emp:message key="ydwx_wxbj_add_27" defVal="视频地址：" fileName="ydwx"></emp:message></label> <input id="url" type="text" value="" disabled="disabled"/></td>
            </tr>

            <tr>
                <td><label for="width"><emp:message key="ydwx_wxbj_add_24" defVal="宽度：" fileName="ydwx"></emp:message></label><input id="width" type="text" value="240"/> px</td>
                <td><label for="height"><emp:message key="ydwx_wxbj_add_25" defVal="高度：" fileName="ydwx"></emp:message></label><input id="height" type="text" value="175"/> px</td>
            </tr>
            <tr>
                <td colspan="2"> <span><emp:message key="ydwx_wxbj_add_28" defVal="对齐方式：" fileName="ydwx"></emp:message></span>
                    <select id="float">
                        <option value=""><emp:message key="ydwx_wxbj_add_19" defVal="默认" fileName="ydwx"></emp:message></option>
                        <option value="float: left"><emp:message key="ydwx_wxbj_add_20" defVal="左浮动" fileName="ydwx"></emp:message></option>
                        <option value="float: right"><emp:message key="ydwx_wxbj_add_21" defVal="右浮动" fileName="ydwx"></emp:message></option>
                        <option value="display: block"><emp:message key="ydwx_wxbj_add_26" defVal="独占一行" fileName="ydwx"></emp:message></option>
                    </select>
                </td>
            </tr>

        </table>
      
        </div>
        <script type="text/javascript" src="<%=basePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=basePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=basePath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
        <script type="text/javascript">
   			function chang(data){
   				if($("select[id=_video] option[selected]").val()){
   					G("url").value=$("select[id=_video] option[selected]").val(); 
   				}
   				
   			}     
/**
	初始化素材库
**/
			function loadfile(pagesize){
	//		var str ="<table>"; 
					//发送请求，并将在onComplete选项中调用回调函数
					var lguserid = $(window.parent.document).find("#lguserid").val();
					var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
					$.post('<%=path %>/wx_ueditor.htm?method=getVideoUeditor',{Action:"post",pagesize:pagesize,filetype:"1",lguserid:lguserid,lgcorpcode:lgcorpcode},function(result){
						//	alert(result.success);
							if(result.success){
									var dataObj=result.msg;//转换为json对象  
									if(dataObj){
										$("<option>").appendTo($("#_video")).html(getJsLocaleMessage("ydwx","ydwx_common_qxz")).val(""); 
								 		$.each(dataObj.root, function(idx,item){
								 		//	var _url = item.file_url;
								 		//	_url = _url.substring(0,_url.lastIndexOf(".")); 
											$("<option>").appendTo($("#_video")).html(item.file_name).val(item.file_url); 
										});  
									}else{
										document.getElementById("msgfile").innerHTML=getJsLocaleMessage("ydwx","ydwx_wxbj_148");
									} 
									
									
									
									
							}else{
									alert(getJsLocaleMessage("ydwx","ydwx_common_czshb")); 
							}
						},"json");			
		
			}
        	
        
          function convert_url(s){
                s=s.replace(/http:\/\/www\.tudou\.com\/programs\/view\/([\w\-]+)\/?/i,"http://www.tudou.com/v/$1");
                s=s.replace(/http:\/\/www\.youtube\.com\/watch\?v=([\w\-]+)/i,"http://www.youtube.com/v/$1");
                s=s.replace(/http:\/\/v\.youku\.com\/v_show\/id_([\w\-=]+)\.html/i,"http://player.youku.com/player.php/sid/$1");
                s=s.replace(/http:\/\/www\.56\.com\/u\d+\/v_([\w\-]+)\.html/i, "http://player.56.com/v_$1.swf");
                s=s.replace(/http:\/\/www.56.com\/w\d+\/play_album\-aid\-\d+_vid\-([^.]+)\.html/i, "http://player.56.com/v_$1.swf");
                s=s.replace(/http:\/\/v\.ku6\.com\/.+\/([^.]+)\.html/i, "http://player.ku6.com/refer/$1/v.swf");
                return s;

            }
            function G (id){
                return document.getElementById(id);
            }
            G("width").onblur = function(){
                if(!/^[1-9]+[.]?\d*$/g.test(this.value)){
                    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_149"));
                    return false;
                }
            }
            G("height").onblur = function(){
                if(!/^[1-9]+[.]?\d*$/g.test(this.value)){
                    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_150"));
                    return false;
                }
            }
            dialog.onok = function (){
                var width = G("width").value;
                var height = G("height").value;
                if(!/^[1-9]+[.]?\d*$/g.test(width)){
                    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_149"));
                    return false;
                }
                if(!/^[1-9]+[.]?\d*$/g.test(height)){
                    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_150"));
                    return false;
                }
                var str = convert_url(G('url').value);
                if(str){
                    editor.execCommand('insertvideo', {
                        url: str,
                        width: width,
                        height: height,
                        style: G("float").value,
                        path:"<%=path %>"
                    });
                }else{
                    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_151"));
                    return false;
                }

            };
            (function(){
                var img = editor.selection.getRange().getClosedNode();
                if(img && img.className == "edui-faked-video"){
                    G("url").value = img.getAttribute("_url");
                    G("width").value = img.width;
                    G("height").value = img.height;
                    var float = img.style.styleFloat ? img.style.styleFloat : img.style.cssFloat;
                    var str = float ? ("float: "+float) : (img.style.display ? "display: "+img.style.display : "");
                    G("float").value = str;
                }
            })()
            var ipt = G('url');
            var isIE = !!window.ActiveXObject;
            if (isIE) {
                setTimeout(function (){
                    var r = ipt.createTextRange();
                    r.collapse(false);
                    r.select();
                });
            }
         //   ipt.focus() 
        </script>
    </body>
</html>
