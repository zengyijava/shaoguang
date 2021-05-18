<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.biz.CommonBiz"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
//加上集群判断
String imgpath="";
if(StaticValue.getISCLUSTER() ==1){
	//***********用于处理分布式集群文件管理服务器增加的地址
	String viewurl="";
	CommonBiz biz=new CommonBiz();
	String[] filePath=biz.getALiveFileServer();
	if(filePath!=null&&filePath.length>1){
		viewurl=filePath[1];
	}
	imgpath=viewurl;
}else{
	imgpath=path+"/";
}

String basePath = request.getScheme()+"://"+request.getServerName();
String port = String.valueOf(request.getServerPort());
if(null != port && port.length() > 0){
  basePath = basePath + ":" + request.getServerPort();
}

if(null != path && path.length() > 0){
  basePath = basePath + path + "/";
}

String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
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
        body {font-size: 12px;overflow:hidden;padding: 10px;margin:0;color: #838383}
        .tabWarper{width: 600px;height:370px;overflow: hidden;position: relative;}
        .head{height: 35px;line-height: 35px;position: relative;z-index: 2}
        .head span {width: 62px; height: 28px; line-height: 28px;display: block; float: left;text-align: center;margin-right: 1px;cursor: pointer}
        .head span.def {
            background: url("../../themes/default/images/dialog-title-bg.png") repeat-x;
            border: 1px solid #ccc;
        }
        .head span.act {background: #FFF; border: 1px solid #ccc; border-bottom: 1px solid #FFF}
        .tabContainer{width: 1250px;height:320px;position: relative;top: -6px;}
        .content{width: 588px;height: 320px;float: left; border: 1px solid #ddd;padding: 5px;margin-right: 20px;}
        .content table{margin-top: 5px;position: relative; }
        .content table tr {height: 35px;line-height: 35px;}
        .lable{text-align: center;}
        td input{ width: 125px;height: 21px;line-height: 21px; background: #FFF;border: 1px solid #d7d7d7; }
       td  div  input{ width: 100px;height: 21px;line-height: 21px; background: #FFF;border: 1px solid #d7d7d7; }
        #url {width: 534px;margin-bottom: 2px;}
        #preview {width: 330px; height: 252px;position: absolute;top: 50px;left: 246px;background: #eee; padding: 5px;overflow: auto}
        /*.start{z-index: 9999; position: absolute;border: 0;width:100px;height:28px;line-height:28px;*/
            /*background: #ddd url("../../themes/default/images/upload.png");top:315px;left:495px; cursor: pointer;*/
        /*}*/
        .start{float:right;width:100px;height:28px;line-height:28px;background: #ddd url("../../themes/default/images/upload_<%=empLangName%>.png");border: 0;cursor:pointer}
        .control{z-index: 9999; position: absolute;width:588px;height:36px;line-height:36px;top:315px;left:6px;}
        .align{float:left;height: 36px;}
        .batchlable{display:block;float:left; width:100px;height:36px;}
		#clipNormal 
		{
			BACKGROUND: url(<%=inheritPath%>/ueditor/dialogs/image/ClipNormal.gif);
		}
		#clipHover
		{
			BACKGROUND: url(<%=inheritPath%>/ueditor/dialogs/image/videoClipHover.gif);
		}
    </style>
    <%if(StaticValue.ZH_HK.equals(empLangName)){%>
    <link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <style type="text/css">
        .head span {width: 200px;}
    </style>
    <%}%>
    <script type="text/javascript" src="../internal.js"></script>
    <script type="text/javascript" src="../myjquery-f.js"></script>
    <script type="text/javascript" src="../jquery.form.js"></script>
    <script type="text/javascript" src="<%=basePath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=basePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
	<script type="text/javascript" src="<%=basePath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
</head>
<body onload="javascript:loadservice('1');">
<div class="tabWarper">
    <div id="head" class="head">
        <span name="tab1" onclick="switchTab(this,0)" class="act"><emp:message key='ydwx_wxbj_add_11' defVal='素材库图片' fileName='ydwx'></emp:message></span>
        <span name="tab2" onclick="switchTab(this,1)" class="def"><emp:message key='ydwx_wxbj_add_12' defVal='本地上传' fileName='ydwx'></emp:message></span>
    </div>
    <div id="tabs" class="tabContainer">
    	<input type="hidden" value="123234" name="testvalue" id="testvalue">
	    <div id="tab1" class="content">
	     	<div id="imgList" style="float:left;overflow:auto;">
	     	 <emp:message key="ydwx_wxbj_add_13" defVal="正在加载素材库。。。。。。" fileName="ydwx"></emp:message>
	     	 </div>
		</div>
        <div id="tab11" class="content" style="display:none;">
         <input id="url" type="hidden" value=""/>
         <input id="editorImgUrl" type="hidden" value=""/>
            <table cellpadding="0" cellspacing="0">
                 
                    
                 
                <tr style="display:none" id="originalSize"><td><emp:message key="ydwx_wxbj_add_14" defVal="原  始：" fileName="ydwx"></emp:message></td><td></td></tr>
                <tr>
                    <td class="lable"><emp:message key="ydwx_wxbj_add_15" defVal="宽  度：" fileName="ydwx"></emp:message></td>
                    <td><input type="text" id="imgWidth"/> px</td>
                </tr>
                <tr>
                    <td class="lable"><emp:message key="ydwx_wxbj_add_16" defVal="高  度：" fileName="ydwx"></emp:message></td>
                    <td><input type="text" id="imgHeight"/> px</td>
                </tr>
                <tr>
                    <td class="lable"><emp:message key="ydwx_wxbj_add_17" defVal="边 框：" fileName="ydwx"></emp:message></td>
                    <td><input type="text" id="imgBorder"/> px</td>
                </tr>
                <tr>
                    <td class="lable"><emp:message key="ydwx_wxbj_add_18" defVal="描  述：" fileName="ydwx"></emp:message></td>
                    <td><input type="text" id="imgTitle"/></td>
                </tr>
                <tr>
                    <td class="lable singlelable"><emp:message key='ydwx_common_alignment' defVal='对齐方式' fileName='ydwx'></emp:message></td>
                    <td id="singleFloat">
                        <img name="none" src="../../themes/default/images/none-c.jpg" alt="<emp:message key='ydwx_wxbj_add_19' defVal='默认' fileName='ydwx'></emp:message>" title="<emp:message key='ydwx_wxbj_add_19' defVal='默认' fileName='ydwx'></emp:message>" onclick="imgselect(this)"/>
                        <img name="left" src="../../themes/default/images/left.jpg" alt="<emp:message key='ydwx_wxbj_add_20' defVal='左浮动' fileName='ydwx'></emp:message>" title="<emp:message key='ydwx_wxbj_add_20' defVal='左浮动' fileName='ydwx'></emp:message>" onclick="imgselect(this)"/>
                        <img name="right" src="../../themes/default/images/right.jpg" alt="<emp:message key='ydwx_wxbj_add_21' defVal='右浮动' fileName='ydwx'></emp:message>" title="<emp:message key='ydwx_wxbj_add_21' defVal='右浮动' fileName='ydwx'></emp:message>" onclick="imgselect(this)"/>
                        <img name="center" src="../../themes/default/images/topbottom.jpg" alt="<emp:message key='ydwx_wxbj_add_22' defVal='居中' fileName='ydwx'></emp:message>" title="<emp:message key='ydwx_wxbj_add_22' defVal='居中' fileName='ydwx'></emp:message>" onclick="imgselect(this)"/>
                    </td>
                </tr>
            </table>
            <div id="preview"></div>

        </div>
        <div id="tab2" class="content"></div>
    </div>
    <div id="control" class="control" style="display: none">
        <span class="batchlable"><emp:message key='ydwx_common_alignment' defVal='对齐方式' fileName='ydwx'></emp:message></span>
        <span id="batchFloat"  class="align">
            <img name="none" src="../../themes/default/images/none-c.jpg" alt="<emp:message key='ydwx_wxbj_add_19' defVal='默认' fileName='ydwx'></emp:message>" title="<emp:message key='ydwx_wxbj_add_19' defVal='默认' fileName='ydwx'></emp:message>" onclick="imgselect(this)"/>
            <img name="left" src="../../themes/default/images/left.jpg" alt="<emp:message key='ydwx_wxbj_add_20' defVal='左浮动' fileName='ydwx'></emp:message>" title="<emp:message key='ydwx_wxbj_add_20' defVal='左浮动' fileName='ydwx'></emp:message>" onclick="imgselect(this)"/>
            <img name="right" src="../../themes/default/images/right.jpg" alt="<emp:message key='ydwx_wxbj_add_21' defVal='右浮动' fileName='ydwx'></emp:message>" title="<emp:message key='ydwx_wxbj_add_21' defVal='右浮动' fileName='ydwx'></emp:message>" onclick="imgselect(this)"/>
            <img name="center" src="../../themes/default/images/topbottom.jpg" alt="<emp:message key='ydwx_wxbj_add_22' defVal='居中' fileName='ydwx'></emp:message>" title="<emp:message key='ydwx_wxbj_add_22' defVal='居中' fileName='ydwx'></emp:message>" onclick="imgselect(this)"/>
        </span>
        <button id="start" class="start" style="display: none">上传</button>
    </div>

</div>
<script type="text/javascript" src="tangram.js"></script>

<%--完整的回调函数，有需要的可以完整取用--%>
<%--<script type="text/javascript" src="callbacks.js"></script>--%>

<script type="text/javascript">
	function changeImgs(){
		
			//	 document.getElementById("divCodes").innerHTML="<img src='<%=path %>/user/img/ico_valid.gif'>";
		
	}

/**
	初始化素材库
**/
	function loadservice(pagesize){
	var str ="<table border='0'><tr><td width='20px' ></td>";
			//发送请求，并将在onComplete选项中调用回调函数
			var lguserid = $(window.parent.document).find("#lguserid").val();
			$.post('<%=path %>/wx_ueditor.htm?method=getImgUeditor',{Action:"post",pageIndex:pagesize,lguserid:lguserid},function(result){
				//	alert(result.success);
					if(result.success){
							var totalpages = result.totalpages;  //总页数
							var pageno = result.pageno; 		//当前页
							var dataObj=result.msg;//转换为json对象 
							$.each(dataObj.root, function(idx,item){
								//输出每个root子对象的名称和值 
								
								str = str+"<td width='120px'>" +
								"<table id='clipNormal' style='FONT-SIZE:14px;width:120px;' border='0'" +
								" cellSpacing='5' cellPadding='0' " +
								"onMouseOver=this.id=this.id.replace('Normal','Hover'); " +
								"onMouseOut=this.id=this.id.replace('Hover','Normal');>" +
								"<tr height='50px' valign='center' align='center'>" +
								"<td width='135px' height='60px'>" +
								"<a href=javascript:onclick=chang('"+item.mg_url+"'); " +
								"hidefocus><img  class='high'  border='0' width='115px' height='70px' " +
								"src='<%=imgpath %>"+item.mg_url+"'/></a></td></tr><tr height='30px'>" +
								"<td id='clipTitle' align='center'>"+item.mg_name+"</td></tr></table></td>";
								
								if(((idx+1)%4)==0 && idx!=(dataObj.root.length-1)){
									str = str+"</tr><tr><td width='20px'></td>";
								}
							
							});  
							
							var pageUpper = Number(pageno)-Number(1);  // 上一页
							var pageLower = Number(pageno)+Number(1);  // 下一页
							var first; //首页
							var Upper ;  //上一页  
							var Lower;    // 下一页
							var tail; // 尾页
							if(pagesize<2){
								first=getJsLocaleMessage("ydwx","ydwx_wxbj_137")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
								Upper =getJsLocaleMessage("ydwx","ydwx_wxfstj_21")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							}else{
								first="<input type='button'  onclick='loadservice(1)'  value='"+getJsLocaleMessage("ydwx","ydwx_wxbj_137")+"' />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
								Upper ="<input type='button'  onclick='loadservice("+pageUpper+")'  value='"+getJsLocaleMessage("ydwx","ydwx_wxfstj_21")+"' />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							}
							if(totalpages <= pageno){
								Lower = getJsLocaleMessage("ydwx","ydwx_wxfstj_22")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
								tail = getJsLocaleMessage("ydwx","ydwx_wxbj_138")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							}else{
								Lower = "<input type='button'  onclick='loadservice("+pageLower+")' value='"+getJsLocaleMessage("ydwx","ydwx_wxfstj_22")+"' />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
								tail = "<input type='button'  onclick='loadservice("+totalpages+")' value='"+getJsLocaleMessage("ydwx","ydwx_wxbj_138")+"' />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							}
							total =pageno+"/"+totalpages+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							
							str=str+"</tr><tr><td width='20px'></td><td colspan=\"4\" style='width: 600px;'><div style='text-align:center;'>"+first+Upper+Lower+tail+total+"</div></td></tr></table>";
						document.getElementById("imgList").innerHTML=str;	
					}else{
					//	alert(result.success);
						//	alert("操作失败！"); 
							document.getElementById("imgList").innerHTML=getJsLocaleMessage("ydwx","ydwx_wxbj_139");
					}
				},"json");			

	}
	function chang(cha){
		 g("tab11").style.display = "";
		 g("tab1").style.display = "none";
		 g("url").value="<%=imgpath %>"+cha;
		 preloadImg();
		 document.getElementById("editorImgUrl").value=cha;
		 
	//	 alert('1');
	}
    /*-=-=-=-=-=-=-=全局变量模块-=-=-=-=-=-=-*/
    var imageUrls = [],        //从服务器返回的上传成功图片数组
        imageCount = 0;        //预览框中的图片数量，初始为0
        

    /*-=-=-=-=-=-=-=界面生成模块-=-=-=-=-=-=-*/
    
    function switchTab(obj,index){
//    alert('2');
        clearFocusClass();
        obj.className = "act";
        g("tabs").style.left = index * (-620) + "px";
        if(obj.getAttribute("name")=="tab2"){
            g("control").style.display = "";
            if(imageCount!=0){
                //dialog.buttons[0].setDisabled(true); //切回来时重新置灰按钮
            }
        }else{
            g("control").style.display = "none";
            dialog.buttons[0].setDisabled(false); //切回来时重新点亮按钮
        }
    }
    function clearFocusClass(){
 //   alert('3');
        var heads = g("head").children;
        for(var i= 0,j;j = heads[i++];){
            j.className = "def";
        }
    }
    /**
     * flash初始化配置
     */
    baidu.swf.create({
				id:"flash",
				url:"imageUploader.swf?r=" + new Date().getTime(),
				width:"588",
				height:"272",
				errorMessage:getJsLocaleMessage("ydwx","ydwx_wxbj_140"),
				wmode:"window",
				ver:"10.0.0",
				// 初始化的参数就是这些，
				vars:{
                    width:588,      //width是flash的宽
					height:272,		//height是flash的高
					gridWidth:115,  // gridWidth是每一个预览图片所占的宽度，应该为width的整除
					gridHeight:120,  // gridHeight是每一个预览图片所占的高度，应该为height的整除
					picWidth:100,	// 单张预览图片的宽度
					picHeight:100,	// 单张预览图片的高度
					uploadDataFieldName:"picdata",	// POST请求中图片数据的key
					picDescFieldName:"pictitle",    // POST请求中图片描述的key
					maxSize:2,	                // 文件的最大体积,单位M
					compressSize:1,               // 上传前如果图片体积超过该值，会先压缩,单位M
					maxNum:32,	                    // 最大上传多少个文件
                    backgroundUrl:"",               //背景图片
                    listBackgroundUrl:"",           //预览图背景
                    buttonUrl:"",                   //按钮背景
					compressLength:680,	            // 能接受的最大边长，超过该值Flash会自动等比压缩
					url:"../../server/upload/jsp/up.jsp",                                             // 上传处理页面的url地址
                    fileType:"{\"description\":\""+getJsLocaleMessage("ydwx","ydwx_wxbj_74")+"\", \"extension\":\"*.gif;*.jpeg;*.png;*jpg\"}"	  //上传文件格式限制
				}
			},"tab2");

	var flashObj = baidu.swf.getMovie("flash");
	var interval = setInterval(function(){
        if(flashObj && flashObj.flashInit){
            clearInterval(interval);
            //console.log("flash完成初始化");
            // 设置回调函数名
            var callback_func_names = [
                "selectFileCallback",	// 选择文件的回调
                "exceedFileCallback",	// 文件超出限制的最大体积时的回调
                "deleteFileCallback",	// 删除文件的回调
                "startUploadCallback",	// 开始上传某个文件时的回调
                "uploadCompleteCallback",	// 某个文件上传完成的回调
                "uploadErrorCallback",	// 某个文件上传失败的回调
                "allCompleteCallback",	// 全部上传完成时的回调
                "changeFlashHeight"		// 改变Flash的高度，mode==1的时候才有用
            ];
            flashObj.setJSFuncName(callback_func_names);
        }
	}, 500);
	

    /*-=-=-=-=-=-=-=逻辑处理模块-=-=-=-=-=-=-*/
    
    function startUploadCallback(){
    
    	var _imgType = document.getElementById("testvalue").value;
    	$.post('<%=path %>/wx_ueditor.htm?method=setImgTypeSession',{Action:"post",_imgType:_imgType,del:"0"},function(result){
    		if(!result.success){
    			alert(getJsLocaleMessage("ydwx","ydwx_wxbj_141"));
    			return false;
    		}
    	},"json");		
    	
    	
    }
    //-----------------核心流程------------------------------
    var img = editor.selection.getRange().getClosedNode(),oWidth,oHeight;
    if(!img) img = document.createElement("img");
    img.id="preImg";
    img.name="preImg";
    if(img.src){
        //图片编辑的情况
        showImageInfo();
    }else{
    	
        //粘贴图片地址的情况
        if(document.attachEvent){
        	
            g("url").onpropertychange = function() {
                if(g("url").value) preloadImg();//trace：解决IE下初始提示bug
            };
        } else {
            g("url").addEventListener("input", function() {
                preloadImg();
            }, false);
        }
    }
    //点击确定时的事件处理
    dialog.onok=function(){
   // alert('5');
        var actionTab = findActionTab();
        if(actionTab == "tab1"){
            return insertSingle();
        }else if(actionTab == "tab2"){
            return insertBatch();
        }

    };
    //----------------------------------------------------


    /**
     * 返回false时不会关闭对话框
     */
    function insertSingle(){
  //  alert('6');
        //未成功加载图片直接返回
        if(!img.src) return;
        var imgurl = img.src;
        //alert(imgurl);
        var imgObj={src:imgurl};
        var width = g("imgWidth").value;
        if(!width) width = oWidth;
        if(!isNum(width)) {
            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_142"));
            g("imgWidth").value="";
            g("imgWidth").focus();
            return false;
        }
        var height = g("imgHeight").value;
        if(!height) height = oHeight;
        if(!isNum(height)) {
            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_143"));
            g("imgHeight").value="";
            g("imgHeight").focus();
            return false;
        }
        var border = g("imgBorder").value;
        if(!border && border !=0 && !isNum(border)) {
            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_144"));
            g("imgBorder").value="";
            g("imgBorder").focus();
            return false;
        }
        imgObj.width  = width;
        imgObj.height = height;
        imgObj.border = border ;
        imgObj.title = g("imgTitle").value ||"";
        imgObj.floatStyle = getAlign("singleFloat");
        editor.execCommand("insertImage",imgObj);
        img = null;
    }
    
    /**
     * 返回false时不会关闭对话框
     */
     //此处加了在控件中上传时候  集群的处理 10-23 
    var pa="<%=imgpath%>";
    function insertBatch(){
        if(!imageCount) return;
        if(!imageUrls.length) return;
        var imgObjs = [],
            align = getAlign("batchFloat");

        for(var i=0,ci;ci=imageUrls[i++];){
            var tmpObj = {};
            
            //alert(ci.title);  alert(ci.url);
            tmpObj.title = ci.title;  
            tmpObj.floatStyle = align;
            //修正显示时候的地址数据
            tmpObj.src = pa+"" + ci.url.replace("..","");
            imgObjs.push(tmpObj);
        }
        editor.execCommand("insertImage",imgObjs);
    }


    function findActionTab(){
  //  alert('8');
        var heads = g("head").children;
        for(var i = 0,j;j = heads[i++];){
            var className = j.className;
         //   alert(className);
            if(className =="act"){
        //    alert(className+"1111");
                return j.getAttribute("name");
            }
        }
        return "";
    }

    //预加载图片
    function preloadImg() {
 //   alert('9');
        var imgType = new RegExp("/\.(png|gif|jpg|jpeg)$/ig"),
        urlFilter = "";
        var url = g("url").value,pimg,sourceSize,
            preview = g("preview");
      //      alert(url);
        if (!imgType.test(url) && url.indexOf(urlFilter) == -1) {
            preview.innerHTML = getJsLocaleMessage("ydwx","ydwx_wxbj_145");
            return;
        }
        preview.innerHTML = getJsLocaleMessage("ydwx","ydwx_wxbj_146");

        img.onload = function() {
            oWidth = this.width;
            oHeight = this.height;
            g("originalSize").lastChild.innerHTML = oWidth + "px * " + oHeight + "px </td>";
            g("originalSize").style.display = "";
            scale(this, 330);
            preview.innerHTML="";
            preview.appendChild(img);
            
        };
        img.onerror = function() {
            preview.innerHTML = getJsLocaleMessage("ydwx","ydwx_wxbj_147");
            img = url; 
        };
        //alert(url);
        img.src = url;
    }
    
    //显示图片信息
    function showImageInfo(){
     	g("tab11").style.display = "";
		g("tab1").style.display = "none";
 //   alert('10');
        //获取图片float方式
        var align =  editor.queryCommandValue('imagefloat');
        replaceImg(align);
        g("url").value = img.src.replace("&amp;", "&");
        g("imgWidth").value = img.width;
        g("imgHeight").value = img.height;
        g("imgBorder").value = img.getAttribute("border")||0;
        g("imgTitle").value = img.title;
        g("originalSize").lastChild.innerHTML = img.width + "px * " + img.height + "px </td>";
        g("originalSize").style.display = "";
        var tmp = img.cloneNode(true);
        tmp.width = img.width;
        tmp.height = img.height;
        scale(tmp, 330);
        g("preview").innerHTML='<img src="'+tmp.src+ '" width="'+tmp.width+'" height="' + tmp.height + '" border="' + img.style.border +'" />';
    }
    //更改float的提示图标
    function replaceImg(str){
 //   alert('11');
        var imgs = g("singleFloat").children;
        for(var i=0,j;j = imgs[i++];){
            imgs[0].src=imgs[0].src.replace("-c.jpg",".jpg")
        }
        for(var i=0,j;j = imgs[i++];){
            if(j.getAttribute("name") == str){
                j.src = j.src.replace(".jpg","-c.jpg");
                break;
            }
        }
    }

    /*-=-=-=-=-=-=-=批量上传控制模块-=-=-=-=-=-=-*/
     function upload(){
  //   alert('12');
		flashObj.call("upload");
         g("start").style.display ="none";
	 }
    /**
	 * 选择文件后的回调函数
	 * @param selectFiles
	 */
    function selectFileCallback(selectFiles){
 //   alert('13');
		//if(console)console.log("开始选择文件：");
		//may 解决有时候对话框的上传按钮不显示
        imageCount = imageCount+selectFiles.length;
        g("start").style.display ="";
        g("start").onclick = upload;
        dialog.buttons[0].setDisabled(true); //初始化时置灰确定按钮

	}
  /**
	 * 单个文件上传完成的回调函数
	 * @param	Object/String	服务端返回啥，参数就是啥
	 */
	function uploadCompleteCallback(data){
        //debugger
		//if(console)console.log("上传成功", data);
		 
		var datas = data.info.replace("'url'", "url").replace("'title'", "title").replace("'state'", "state") ;
	 
	//	alert('14---'+datas);
        var info = eval("(" + datas + ")");
     	 
   //  	alert(info.url);
        info && imageUrls.push(info);
	}

	//全部上传完成的回调函数
    function allCompleteCallback(){
  //  alert('15');
		//console.log("全部上传成功");
        dialog.buttons[0].setDisabled(false); //上传完毕后点亮按钮
	}
    /**
     * 删除文件后的回调函数
     * @param	Array
     */
    function deleteFileCallback(delFiles){
 //   alert('16');
        // 数组里单个元素为Object，{index:在多图上传的索引号, name:文件名, size:文件大小}
        // 其中size单位为Byte
        imageCount = imageCount - delFiles.length;
        if(imageCount ==0){
            g("start").style.display = "none";
            dialog.buttons[0].setDisabled(false); //上传完毕后点亮按钮
        }
    }


    /*-=-=-=-=-=-=-=公共方法模块-=-=-=-=-=-=-*/
     function g(id){
        return document.getElementById(id);
     }
     function isNum(n){
 //    alert('18');
        return /^[1-9]+[.]?\d*$/g.test(n);
     }
     /**
      * 获取csstext中的某个样式属性
      * @param cssText
      * @param style
      */
     function getStyleFromCssText(cssText, style) {
 //    alert('19');
        var reg = new RegExp(style + ":\\s*((\\w)*)", "ig");
        var arr = reg.exec(cssText);
        return arr ? arr[1] : "";
     }
     /**
      * 等比例缩放图片
      * @param img
      * @param max
      */
     function scale( img, max ) {
 //    alert('20');
         var width = 0,height = 0,percent;
         img.sWidth = img.width;
         img.sHeight = img.height;
         if ( img.width > max || img.height > max ) {
             if ( img.width >= img.height ) {
                 if ( width = img.width - max ) {
                     percent = (width / img.width).toFixed( 2 );
                     img.height = img.height - img.height * percent;
                     img.width = max;
                 }
             } else {
                 if ( height = img.height - max ) {
                     percent = (height / img.height).toFixed( 2 );
                     img.width = img.width - img.width * percent;
                     img.height = max;
                 }
             }
         }
     }
    function imgselect(simg){
 //    alert('21');
        var childs = simg.parentNode.children;
        for(var i=0,child;child = childs[i++];){
            if(/img/ig.test(child.tagName)){
                child.src = child.src.replace("-c.jpg",".jpg");
            }
        }
        simg.src = simg.src.replace(".jpg","-c.jpg");
    }
    //获取id下选中图片的name
    function getAlign(id){
 //    alert('22');
        var imgs = g(id).children;
        for(var i = 0,img;img = imgs[i++];){
            if(img.src.indexOf("-")!=-1){
                break;
            }
        }
        return i != imgs.length + 1 ? img.name : 'none';
    }
</script>
</body>
</html>