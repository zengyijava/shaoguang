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
<head>
	<%@include file="/common/common.jsp" %>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <script type="text/javascript" src="../internal.js"></script>
    <script type="text/javascript" src="../myjquery-f.js"></script>
    <script type="text/javascript" src="<%=basePath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=basePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
	<script type="text/javascript" src="<%=basePath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
    <style type="text/css">
        *{color: #838383}
        body {
            font-size: 12px;
            width:382px;
            height: 150px;
            overflow: hidden;
            margin:0px;padding:0px;
        }
        .table001 td{padding:3px 0;}
         span.txt{width: 60px;height:30px;line-height: 30px;display: block;float:left}
         span.txt1{width: 60px;height:30px;line-height: 30px;display: block;float:left}
        .content{
            padding: 9px 0px 0px 15px;
            height:100%;
        }
        #link_href,
        #title{width:286px;height:21px;background: #FFF;border:1px solid #d7d7d7;padding: 0px; margin: 0px; line-height:21px}
        #link_href{width:162px}
        .content table{padding:0px;margin:0px}
        .content table tr{padding:0px;margin:0px; list-style: none;height: 20px; line-height: 20px;}
        .red{color:red;}
    </style>
    <%if(StaticValue.ZH_HK.equals(empLangName)){%>
    <link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <style type="text/css">
        div table{
            width: 420px;
        }
        .table001 td{
            text-align: left;
        }
    </style>
    <%}%>
</head>
<body onload="addse()">
<div class="content">
    <table class="table001">
        <tr>
            <td align="right">
            	<emp:message key="ydwx_wxbj_add_29" defVal="链接地址：" fileName="ydwx"></emp:message>
            </td>
            <td>
          <select id="protocol" onchange="changehttp()" style="width:120px;">
                    <option value="http://">http://</option>
                    <option value="https://">https://</option>
                </select>
                	<input type="text"  id="link_href"  style="display:none;" /><br/>
            	<span id="msg2" class="red"></span>
            </td>
                
               
        </tr>
        <tr>
            <td align="right">
            	<emp:message key="ydwx_wxbj_add_6" defVal="标 题：" fileName="ydwx"></emp:message>
           </td>
           <td><input id="title" type="text"/></td>
        </tr>
        <tr>
             <td colspan="2"><emp:message key="ydwx_wxbj_add_30" defVal="是否在新窗口打开：" fileName="ydwx"></emp:message><input id="target" type="checkbox"/></td>
        </tr>
    </table>

</div>
<script type="text/javascript">
	 function addse(){
	 		document.getElementById("link_href").value="";
           	var treedata=window.parent.tree.getAllSubItems(0);
           	var a  ;
           	if(treedata.toString().indexOf(",")>-1){ 
           		a =treedata.split(",");
           	}else{
           		a =treedata+"";
           	}
           	$("#protocol").children().remove(); 
           	var trees=window.parent.tree;
        	var wxpageidstrx=window.parent.getwxpagestr('0');
           	$("<option>").val("javascript:mylink('<%=path %>/wx.nms?w=#link#01"+wxpageidstrx+"#link#','01"+wxpageidstrx+"','My_Custom');").html(trees.getItemText(0)).appendTo($("#protocol"));
            
            if(a.length>0){
            	if(a.length==1){
            		var wxpageidstr=window.parent.getwxpagestr(a);
            		$("<option>").val("javascript:mylink('<%=path %>/wx.nms?w=#link#01"+wxpageidstr+"#link#','01"+wxpageidstr+"','My_Custom');").html(trees.getItemText(a)).appendTo($("#protocol"));        
            	}else{ 
                	for(var i=0;i<a.length;i++){ 
                		var wxpageidstr=window.parent.getwxpagestr(a[i]);
                 		$("<option>").val("javascript:mylink('<%=path %>/wx.nms?w=#link#01"+wxpageidstr+"#link#','01"+wxpageidstr+"','My_Custom');").html(trees.getItemText(a[i])).appendTo($("#protocol"));        
            		}
            	}
        	}
        	$("<option>").val("http://").html("http://").appendTo($("#protocol"));       
        	$("<option>").val("https://").html("https://").appendTo($("#protocol"));   
			changehttp();
     }


 	function changehttp(){
 		
 		if(G("protocol").value!="http://" && G("protocol").value!="https://" ){
 			G("link_href").style.display = "none";
 		}else{
 			G("link_href").style.display = "";
 		}
 	}
    var a = editor.queryCommandValue("link"),link = {};
    if(a){
        link = a;
    }
    function G(id){
        return document.getElementById(id);
    }
    function jbind(obj,evt,fun){
        if(obj.addEventListener){  // firefox,w3c
            obj.addEventListener(evt,fun,false);
        }else{// ie
            obj.attachEvent("on"+evt,function(){fun.apply(obj);});
            
        }
    }
    
    
    function initpage(date){
    	var link_q = date;
		var link_boo = false;
    	if(link_q.indexOf("http://%7b")>-1 && link_q.indexOf("%7d.com/")>-1){ 
    	
    	var pageid=link_q.replace("%7b", "{").replace("%7d.com/","}");
  	  	var selObj=document.getElementById("protocol");
		var i ;
  		for(i= 0 ;i<selObj.options.length;i++){
  			if(selObj.options[i].value==pageid){
  			 	link_boo = true;
  			 	selObj.options[i].selected = true; 
  			 	G("link_href").style.display = "none"; 
  			 }
		}
		return link_boo;
  	}
    }
    function initIpt(){
    	var link_b = false ;
        if(link.href){
        	G("title").value = link.title ? link.title : "";   //得到TITLE
        	link_b = initpage(link.href); 
        	if(!link_b){
            	/^(?:http:\/\/)/ig.test(link.href) ? G("protocol").value="http://" : G("protocol").value="https://";
            	G("link_href").value = link.href.replace(/^(?:https?:\/\/)|(?:\/)$/ig,"");
            	G("link_href").style.display = ""; 
            }
            if(link.target == "_blank"){
                G("target").checked = true;
            }
        }
        if(!link_b){  //如果选中的不是页面，则启用焦点
	        var ipt = G("link_href");
	    //    ipt.focus(); 
	        ipt.style.cssText = 'border:1px solid #ccc;background-color:#fafafa;';
	        jbind(ipt,'focus',function(){
	            this.style.cssText = 'border:1px solid #ccc;background-color:#fff;';
	        });
	        jbind(ipt,'blur',function(){
	            this.style.cssText = 'border:1px solid #ccc;background-color:#fafafa;';
	        });
        }
    }
   
    function handleDialogOk(){
        var href = G('link_href').value || '';
        var obj = {},td,link;
        obj.href = href.replace(/^\s+|\s+$/g, '');
        if(G("protocol").value=="http://" || G("protocol").value=="https://" ){
 		 	
	        if(!obj.href){
	            G("msg2").innerHTML = getJsLocaleMessage("ydwx","ydwx_wxbj_152");
	            return false;
	        }
        }
        obj.href = G("protocol").value+obj.href;
        if(G("target").checked){
            obj["target"] = "_blank";
        } else {
            obj["target"] = "_self";
        }
        if(G("title").value.replace(/^\s+|\s+$/g, '')){
            obj["title"] = G("title").value.replace(/^\s+|\s+$/g, '');
        }
       
        editor.execCommand('link', obj);
        dialog.close();
    }
    var keyHandle = function(evt){
        evt = evt || window.event;
        if (evt.keyCode == 13) {
            handleDialogOk();
            return false;
        }
    }
    initIpt();
    dialog.onok = handleDialogOk;
    G('link_href').onkeydown = keyHandle;
    var ipt = G('link_href');
    var isIE = !!window.ActiveXObject;
    if (isIE) {
        setTimeout(function (){
            var r = ipt.createTextRange();
            r.collapse(false);
            r.select();
        });
    }
</script>
</body>
</html>
