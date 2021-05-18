<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.netnews.common.AllUtil" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
Object netid = request.getAttribute("netid");
String wx_netid = "''";
String yeardate = AllUtil.addDayDate(1);
if(netid!=null)
{
	wx_netid = netid.toString();
}

String lguserid = (String)request.getAttribute("lguserid");
String lgcorpcode = (String)request.getAttribute("lgcorpcode");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/common/common.jsp" %>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
	<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css" />
	<link rel="stylesheet" href="<%=skin%>/newjqueryui.css" type="text/css" >
	<link rel="stylesheet" type="text/css" href="<%=inheritPath%>/ueditor/themes/default/ueditor_<%=empLangName%>.css"/>
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js"></script>
	<script type="text/javascript" src="<%=inheritPath%>/ueditor/dialogs/jquery.form.js"></script>
	<script type="text/javascript" src="<%=inheritPath%>/ueditor/editor_config.js" charset="utf-8" ></script>
	<%--开发版--%>
	<script type="text/javascript" src="<%=inheritPath%>/ueditor/_examples/editor_api.js" charset="utf-8" >
	        paths = [
	            'editor.js',
	            'core/browser.js',
	            'core/utils.js',
	            'core/EventBase.js',
	            'core/dom/dom.js',
	            'core/dom/dtd.js',
	            'core/dom/domUtils.js',
	            'core/dom/Range.js',
	            'core/dom/Selection.js',
	            'core/Editor.js',
	            'commands/inserthtml.js',
	            'commands/image.js',
	            'commands/justify.js',
	            'commands/font.js',
	            'commands/link.js',
	            'commands/map.js',
	            'commands/iframe.js',
	             'commands/actividata.js',
	            'commands/removeformat.js',
	            'commands/blockquote.js',
	            'commands/indent.js',
	            'commands/print.js',
	            'commands/preview.js',
	            'commands/spechars.js',
	            'commands/emotion.js',
	            'commands/selectall.js',
	            'commands/paragraph.js',
	            'commands/directionality.js',
	            'commands/horizontal.js',
	            'commands/time.js',
	            'commands/rowspacing.js',
	            'commands/cleardoc.js',
	            'commands/anchor.js',
	            'commands/delete.js',
	            'commands/wordcount.js',
	            'commands/image.js',
	            'plugins/pagebreak/pagebreak.js',
	            'plugins/undo/undo.js',
	            'plugins/paste/paste.js',
	            'plugins/list/list.js',
	            'plugins/source/source.js',
	            'plugins/shortcutkeys/shortcutkeys.js',
	            'plugins/enterkey/enterkey.js',
	            'plugins/keystrokes/keystrokes.js',
	            'plugins/fiximgclick/fiximgclick.js',
	            'plugins/autolink/autolink.js',
	            'plugins/autoheight/autoheight.js',
	            'plugins/autofloat/autofloat.js',  //依赖UEditor UI,在IE6中，会覆盖掉body的背景图属性
	            'plugins/highlight/highlight.js',
	            'plugins/serialize/serialize.js',
	            'plugins/video/video.js',
	            'plugins/table/table.js',
	            'plugins/Interaction/Interaction.js',
	            
	            'plugins/downfile/downfile.js',
	             
	            'plugins/contextmenu/contextmenu.js',
	            'plugins/pagebreak/pagebreak.js',
	            'plugins/basestyle/basestyle.js',
	            'plugins/elementpath/elementpath.js',
	            'plugins/formatmatch/formatmatch.js',
	            'plugins/searchreplace/searchreplace.js',
	            'ui/ui.js',
	            'ui/uiutils.js',
	            'ui/uibase.js',
	            'ui/separator.js',
	            'ui/mask.js',
	            'ui/popup.js',
	            'ui/colorpicker.js',
	            'ui/tablepicker.js',
	            'ui/stateful.js',
	            'ui/button.js',
	            'ui/splitbutton.js',
	            'ui/colorbutton.js',
	            'ui/tablebutton.js',
	            'ui/toolbar.js',
	            'ui/menu.js',
	            'ui/combox.js',
	            'ui/dialog.js',
	            'ui/menubutton.js',
	            'ui/datebutton.js',
	            'ui/editorui.js',
	            'ui/editor.js',
	            'ui/multiMenu.js'
	        ];

	</script>

	<script type="text/javascript">
	
	$(document).ready(function(){
		getLoginInfo("#loginUser");
			
		$("#divBox").dialog({
			autoOpen: false,
			height:500,
			width: 300,
			modal: true,
			close:function(){
			}
		});
					
	});
	
		function back()
		{
			location.href="<%=path%>/wx_survey.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
		}
		
		//查看
		function Look(){
			//$("#nm_preview_common").attr("src","about:blank");
			//alert($(window.frames["nm_preview_common"].document).contents().html());
			//window.frames["nm_preview_common"].document).append("aaa");
			//$("#nm_preview_common").contents().find("HTML").append("<body>aaaa</body>");
			//$("#nm_preview_common").html("<body>aaaa</body>");
			//$("#nm_preview_common").contents("<html><body>aaaa</body></html>");
			//$("#nm_preview_common").attr("html","<body>aaaa</body>");
			//document.getElementById("nm_preview_common").innerHTML="<body>aaaa</body>";
			//$("#nm_preview_common").attr("src","file/wx/PAGE/wx_61.jsp");
			//$("#nm_preview_common").attr("src","file/wx/PAGE/wx_84.jsp");
			$(document.getElementById('nm_preview_common').contentWindow.document.body).html(editor.getContent());
			//alert($(document.getElementById('nm_preview_common').contentWindow.document.body).html());
			//alert($(window.frames["nm_preview_common"].document).contents().html());
			
			$("#divBox").dialog("open");
	      
	    }
		
	</script>





<%--  将鼠标放置于图片之上显示大图 end --%>

<style type="text/css">
    .hideDlg
    {
        height:450px;width:239px;
        display:none;
    }
    .showDlg
    {
        overflow:scroll;
        background-color:white;
        border-width:3px;
        border-style:solid;
        height:480px;width:239px;
        position: absolute; 
        display:block;
        z-index:5;
        }
   .overlay{
    position:absolute;
    top:0;left:0;width:100%;height:100%;
    background:red;
    opacity:0.5;filter:alpha(opacity=50);display:none;}    
   .showNetDlg
    {
      　 background:#CCCCCC;
     　  background-color:white;
        border-width:2px;
        border-style:solid;
        border-color:#71B8FF;
        height:430px;width:234px;
        position: absolute; 
        display:block;
        z-index:5;
        }
        
       .showNetbox
    {
      　background:#CCCCCC;
        border-width:2px;
        border-style:solid;
        border-color:#71B8FF;
        height:430px;width:234px;
        position: absolute; 
        z-index:5;
        }
 .showNet
    {
         word-break:break-all;
         overflow-x:scroll;
         overflow-y:scroll;
           
         margin:0px auto;
         margin-right: 30px;
         margin-left: 20px;
         margin-top: 50px;
        
        background-color:white;
        border-width:2px;
        border-style:solid;
        border-color:#000000;
        height:300px;width:190px;
        position:relative; 
        display:block;

    }
     .showDlgImg
    {
        background-color:white;
        border-width:1px;
        border-style:none;
        height:415px;width:234px;
        position:relative; 
        display:block;
    }
   
    .showDeck {
        display:block;
        top:0px;
        left:0px;
        margin:0px;
        padding:0px;
        position:absolute;
        z-index:3;
        background:white;
    }
    .hideDeck 
    {
        display:none;
    }
     
</style>
<style>

	        #editor { width: 99.5%;height:420px;} 
	        .and_back{width: 100%;border:1px solid #ccc;text-align:left;font-size:12px;line-height:25px;height:450px;} 
	
				a{color:#002BE8;}
		        .div_all{
		        	border-left:1px solid #ccc;
		        	border-right:1px solid #ccc;
		        	border-bottom:1px solid #ccc;
		        	text-align:left;font-size:12px;line-height:25px;
		        	padding-top:10px;margin 0px;background: url(<%=path%>/ueditor/_examples/head.jpg)  ; 
		        	background-repeat:repeat-y;
					background-repeat:repeat-x;
					width: 99.5%;
		        }
		         .div_all1{
		        	border-left:1px solid #ccc;
		        	border-right:1px solid #ccc;
		        	border-bottom:1px solid #ccc;
		        	text-align:left;font-size:12px;line-height:25px;
		        	padding-top:10px;margin 0px;background: url(<%=path%>/ueditor/_examples/head.jpg)  ; 
		        	background-repeat:repeat-y;
					background-repeat:repeat-x;
					width: 98.6%;
		        }
		        .div_all12{
		        	text-align:left;font-size:12px;line-height:25px;
		        	padding-top:10px;margin 0px;background: url(<%=path%>/ueditor/_examples/head.jpg)  ; 
		        	background-repeat:repeat-y;
					background-repeat:repeat-x;
					height:439px;
					width:200px;
		        }
		        .scroll{
		        	display:none;
				}
	
</style>



</head>
<body>

<div id="container" class="container">

	<div id="rContent" class="rContent">
	
		<div class="titletop">
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
							<%
								if("''".equals(wx_netid)){
							%>
								<emp:message key="ydwx_survey_5" defVal="新建问卷" fileName="ydwx"></emp:message>
							<%
								}else{
							%>
								<emp:message key="ydwx_survey_6" defVal="修改问卷" fileName="ydwx"></emp:message>
							<%
								}
							%>
							</td>
							<td align="right">
								<span class="titletop_font" onclick="javascript:back()"><emp:message key="ydwx_wxgl_add_fhshyj" defVal="返回上一级" fileName="ydwx"></emp:message></span>
							</td>
						</tr>
					</table>
		</div>
	
	<%-- 网讯预览DIV --%>
<div id="overlay1"></div>				
<div id="showdivBox" class="overlay" style="width: 290px;height: 550px;background-image: url('<%=inheritPath %>/images/a.png');border: 2px solid #D2E8FD;">
	<div  style="height: 30px;" onmouseover="Move_obj2('divBoxtop')" style="cursor:move;" id="divBoxtop">
		<b style="margin-left: 10px;font-size: 14px;height: 28px;margin-top: 10px;position: absolute;color: cornflowerblue;"><emp:message key="ydwx_wxgl_add_wxchk" defVal="网讯查看" fileName="ydwx"></emp:message></b>
		<b style="background: url('<%=inheritPath %>/ueditor/themes/default/images/icons-all.gif') repeat-x 6px -82px;width: 22px;height: 25px;margin-left: 265px;position: absolute;cursor: pointer;" onclick="cancelDiv()"></b>
	</div>
	<hr>
		<div id="img" style="height:430px;background:url(<%=inheritPath %>/images/phone3.png);margin-left: 20px;width: 248px;">
			<div id="netInfoDiv" style="width:204px;height: 363px;margin-top: 30px;" class="showNet"></div>
		</div>
	<hr>
</div>
<form  name="Form" action="<%=path %>/wx_survey.htm?method=add" method="post"  onsubmit="return submitui();">
	<div id="loginUser" class="hidden"></div>
	<input type="hidden" name="wx_id" id="wx_id" value=""  size="60" >
	<input type="hidden" name="wx_timeType" id="wx_timeType" value="1" >
	<input type="hidden" name="lguserid" id="lguserid" value="<%=lguserid %>" >
	<input type="hidden" name="lgcorpcode" id="lgcorpcode" value="<%=lgcorpcode %>" >
	<input type="hidden" name="colNum" id="colNum" value="0" >
	<input type="hidden" name="colValue" id="colValue" value="c0,0,14;" >
	<input type="hidden" id="wx_netid" name="wx_netid" value="" />
	<input type='hidden' name='checkboxzym' id='input0' value='<emp:message key="ydwx_survey_7" defVal="默认网讯页面" fileName="ydwx"></emp:message><#!!#>0<#!!#><p></p><#!!#>0<#!!#>010' >
	<table >
		<tr style="height:50px;">
			<td colspan=2>&nbsp;&nbsp;&nbsp;<emp:message key="ydwx_survey_1" defVal="问卷名称：" fileName="ydwx"></emp:message><input type="text" style="width:330px;" class="input_bd" name="wx_name" id="wx_name" size="60" value=""></td>
		</tr>
		<tr >
			<td  valign="top" style="width:420px;">
				<div style="width:400px;height:425px;border: 1px solid #bec3d1;" >
					<iframe id="interFrame" name="interFrame" src="<%=inheritPath %>/wxsurvey/leftInter.jsp" style="border: 0;width:400px;height:425px;" marginwidth="0" scrolling="no" frameborder="no"></iframe>
					
				</div>
							
			</td>
			<td valign="top" width="400px">
				<textarea id="editor" ></textarea>
				
				<input type="hidden" name="wx_STATUS" id="wx_STATUS" value="0"  >
			</td>
		</tr>
		<tr>
			<td colspan=2 style="text-align:center;">
				<br/>
				<input type="submit" value="<emp:message key="ydwx_survey_4" defVal="定稿" fileName="ydwx"></emp:message>"  class="btnClass5 mr10" name="saveAndList"/>
				<input type="submit" value="<emp:message key="ydwx_survey_8" defVal="暂存" fileName="ydwx"></emp:message>" class="btnClass5 mr10" name="save" id="save" />
				<input type="button" value="<emp:message key="ydwx_common_yulan" defVal="预览" fileName="ydwx"></emp:message>" class="btnClass5 " onclick="Look()" />
				<%-- <input type="button" value="网讯预览"  class="btn_b"  style="width: 100px;height: 25px;" onclick="showNetDlg();"/>  --%>
			</td>
		</tr>		
	</table>
</form>
	
	</div>

<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
</div>
<div class="clear"></div>

	<div id="divBox" class="hideDlg" style="display:none" title="<emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message>">
		<div style="width:240px;height:460px;margin-left:30px; margin-top:-3px; background:url(<%=commonPath %>/common/img/iphone5.jpg);">
			<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="nm_preview_common" name="nm_preview_common" src="ydwx/wxsurvey/wx_preview.jsp" ></iframe>	
		</div>
	</div> 

</body>
</html>
			<script type="text/javascript">
				
			 
				  
				var editor = new baidu.editor.ui.Editor({
			        autoHeightEnabled:false, 
			        toolbars: [
						        ['Undo','Redo','|',  //'Source','|',
						         'Bold','Underline','RemoveFormat','|',
						         'ForeColor','BackColor','InsertOrderedList','InsertUnorderedList','|',
						         'Horizontal','InsertTable','DeleteTable','MergeCells','SplittoCells','SelectAll','ClearDoc','|',
						         'FontFamily','FontSize','|',
						         'ImageNone','ImageLeft','ImageRight','ImageCenter','|',
						         'InsertDownfile','InsertImage','InsertVideo','Link']
								],
			    });
			    editor.render('editor');
			    
			
			    
			    
			    
			</script>
			
			
<%--  将鼠标放置于图片之上显示大图 begin --%>
<script type="text/javascript">

function enableTooltips(id){
	
	var links,i,h;
	if(!document.getElementById || !document.getElementsByTagName) return;
	
	h=document.createElement("span");
	h.id="btc";
	h.setAttribute("id","btc");
	h.style.position="absolute";
	document.getElementsByTagName("body")[0].appendChild(h);
	if(id==null) links=document.getElementsByTagName("img");
	else links=document.getElementById(id).getElementsByTagName("img");
	for(i=0;i<links.length;i++){
		PrepareImg(links[i]);
	}
}

function PrepareImg(el){
	var tooltip,t,b,s,l;
	t=el.getAttribute("message");
	if(t==null || t.length==0) return ;
	el.removeAttribute("message");
	tooltip=CreateEl("span","tooltip");
	s=CreateEl("span","top");
	s.innerHTML = t;
	tooltip.appendChild(s);
	//alert(el.getAttribute("src"));
	//l=el.getAttribute("href");
	l=el.getAttribute("src");
	if(l.length>30) l=l.substr(0,27)+"...";
	setOpacity(tooltip);
	el.tooltip=tooltip;
	el.onmouseover=showTooltip;
	el.onmouseout=hideTooltip;
	el.onmousemove=Locate;
}


function showTooltip(e){
	document.getElementById("btc").appendChild(this.tooltip);
}


function hideTooltip(e){
	var d=document.getElementById("btc");
	if(d.childNodes.length>0) d.removeChild(d.firstChild);
	//alert("mouse leave!!");
}

function setOpacity(el){
	el.style.filter="alpha(opacity:95)";
	el.style.KHTMLOpacity="0.95";
	el.style.MozOpacity="0.95";
	el.style.opacity="0.95";
}


function CreateEl(t,c){
	var x=document.createElement(t);
	x.className=c;
	x.style.display="block";
	return(x);
}

function Locate(e){	
	var posx=0,posy=0;
	var myVarX,myVarY;
	if(e==null) e=window.event;
	if(e.pageX || e.pageY){
		posx=e.pageX; posy=e.pageY;
		myVarX=document.documentElement.scrollLeft;
		myVarY=document.documentElement.scrollTop;
	}
	else if(e.clientX || e.clientY){
		if(document.documentElement.scrollTop){
			posx=e.clientX+document.documentElement.scrollLeft;
			posy=e.clientY+document.documentElement.scrollTop;
			myVarX=document.documentElement.scrollLeft;
			myVarY=document.documentElement.scrollTop;
		}
		else{
			posx=e.clientX+document.body.scrollLeft;
			posy=e.clientY+document.body.scrollTop;
			myVarX=document.body.scrollLeft;
			myVarY=document.body.scrollTop;
		}
	}
	//if(document.body.offsetWidth<posx-myVarX+220){
		posx=posx-270;
	//}
	if(document.body.offsetHeight<posy-myVarY+550){
		posy=posy-(posy-myVarY+500-document.body.offsetHeight)-100;
	}
	//alert(myVarX+" "+myVarY);
	document.getElementById("btc").style.top=posy+"px";
	document.getElementById("btc").style.left=posx+"px";//(posx)
	
}

</script>

<script language='javascript'>
	enableTooltips(null);
</script>
			
			
			<script type="text/javascript">
			
			function is_save(da){
					if(da==1){
						document.getElementById("save").disabled="disabled"; 
					}else{
						document.getElementById("save").disabled="";
					}
				}
			
			var drag_=false
			var D=new Function('obj','return document.getElementById(obj);')
			var oevent=new Function('e','if (!e) e = window.event;return e')
					
          
           function Move_obj(obj){
			     var x,y;
				D(obj).onmousedown=function(e){
					drag_=true;
					with(this){
						   x = oevent(e).clientX - parseInt(D("muchose").style.left); 
			               y = oevent(e).clientY - parseInt(D("muchose").style.top); 
						 document.onmousemove=function(e){
							if(!drag_)return false;
							with(D("muchose")){
							   
								D("muchose").style.left=oevent(e).clientX-x+"px";
								D("muchose").style.top=oevent(e).clientY-y+"px";
							}
						}
					}
					document.onmouseup=new Function("drag_=false");
				}
				
			}
           
           function Move_obj2(obj){
			     var x,y;
				D(obj).onmousedown=function(e){
					drag_=true;
					with(this){
						   x = oevent(e).clientX - parseInt(D("showdivBox").style.left); 
			               y = oevent(e).clientY - parseInt(D("showdivBox").style.top); 
						 document.onmousemove=function(e){
							if(!drag_)return false;
							with(D("showdivBox")){
								D("showdivBox").style.left=oevent(e).clientX-x+"px";
								D("showdivBox").style.top=oevent(e).clientY-y+"px";
							}
						}
					}
					document.onmouseup=new Function("drag_=false");
				}
				
			}
			
			function Move_obj3(obj){
			     var x,y;
				D(obj).onmousedown=function(e){
					drag_=true;
					with(this){
						   x = oevent(e).clientX - parseInt(D("divBox").style.left); 
			               y = oevent(e).clientY - parseInt(D("divBox").style.top); 
						 document.onmousemove=function(e){
							if(!drag_)return false;
							with(D("divBox")){
								D("divBox").style.left=oevent(e).clientX-x+"px";
								D("divBox").style.top=oevent(e).clientY-y+"px";
							}
						}
					}
					document.onmouseup=new Function("drag_=false");
				}
				
			}
								
								
								   var   data = editor.getContent();
									/*默认网讯页面*/
								    var pagexml = "<tree id='0' name='"+getJsLocaleMessage("ydwx","ydwx_wxbj_38")+"'></tree>";
									var oldvalue ="input0";
									var oldlink="10";  
									var olddiv ="div0";
									
									var modid=null;
									var creatid="";
									var parentID = 0 ;
									var pageid=0;
						    		 var showcount = 0;
									function submitui(){ 
										var contnet = editor.getContent();
										var wx_name = document.getElementById("wx_name").value;
										
									   var pattern = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）—|{}【】‘；：”“'。，、？]"; 
										if(wx_name==""){
										    /*请输入问卷名称！*/
											alert(getJsLocaleMessage("ydwx","ydwx_survey_text_1"));
											return false; 
										}else if(wx_name.length>50){
										    /*问卷名称长度不能大于50！*/
                                            alert(getJsLocaleMessage("ydwx","ydwx_survey_text_2"));
											return false; 
										}else{
											
											 for(var i = 0 ; i< pattern.length; i++){
	  	                                      	if(wx_name.replace(/(\s*$)/g, "").indexOf(pattern.charAt(i))>-1){
												/*问卷名称不能为特殊字符，请重新输入！*/
                                                    alert(getJsLocaleMessage("ydwx","ydwx_survey_text_3"));
		    	                              $("#wx_name").select();
	    	                                 	return false;
												}
											}
										}
										
										
										if(contnet==""){
										    /*内容为空！*/
											alert(getJsLocaleMessage("ydwx","ydwx_wxbj_47"));
											return false; 
										}
									//	if(contnet.length>4000){
									//		alert("内容超出4000！"); 
									//		return false; 
									//	}
										
										var chevalue = document.getElementById(oldvalue).value.split("<#!!#>"); 
										var	content = editor.getContent();
										//保存最后操作的值
										document.getElementById(oldvalue).value=chevalue[0]+"<#!!#>"+chevalue[1]+"<#!!#>"+content+"<#!!#>"+chevalue[3]+"<#!!#>"+chevalue[4];  //保存上次的网讯名称,值 
										
										var inp = document.getElementById("input0").value;
									
										var invalue = inp.split("<#!!#>")[2];
											//判断默认页面内容不能为空
										invalue = invalue.replace(new RegExp(/(&nbsp;)/g),'')
										if(invalue=="" || invalue=="<p></p>"){
										    /*默认网讯内容不能为空！*/
											alert(getJsLocaleMessage("ydwx","ydwx_wxbj_49"));
											return false; 
										}
										
										
										return true ; 
									}
									
									
									function removezym(){
									
										var d1=document.getElementById(olddiv);
										d1.parentNode.removeChild(d1);
										//删除完，显示默认页面，，，同时初始化数据
										oldvalue ="input0";
										olddiv ="div0";
										oldlink="010";
										
										var valueandname = document.getElementById(oldvalue).value;
										pageid = valueandname.split("<#!!#>")[3];
										editor.setContent(valueandname.split("<#!!#>")[2]); //获得当前网讯的值
										
										 
										
									}
						    		
						    	//	function showzym(){ 
						    			
						    	//		document.getElementById("zym").innerHTML=document.getElementById("zym").innerHTML+"&nbsp;&nbsp;&nbsp;&nbsp;<div id='div"+showcount+"'><a href=javascript:UpdateUeditor('input"+showcount+"','a"+showcount+"','div"+showcount+"'); id='a"+showcount+"' >页面"+showcount+"</a><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value='页面"+showcount+"<#!!#>1<#!!#><p></p><#!!#>-1'></p></div>";
								//		showcount = showcount+1;
						    	//	}
							     //
							  
							
							     function IDbyInfo(){
										var loadzym = "";
										var mycars=new Array();
										var main_va ;
										//发送请求，并将在onComplete选项中调用回调函数
										$.post('<%=path %>/wx_ueditor.htm?method=getParentUdeitor',{Action:"post",netid:<%=wx_netid %>,lguserid:<%=lguserid %>},function(result){
												if(result.success){
														var dataObj=result.msg;//转换为json对象 
														var baseObj=result.base;//转换为json对象 
														 
														$.each(baseObj.root, function(idx,item){
																 
															
																main_va = item.page ;
																 document.getElementById("wx_name").value=item.wx_name;
																 document.getElementById("wx_netid").value=item.netid;
																 editor.setContent(item.page.split("<#!!#>")[2]);
																 pageid = item.page.split("<#!!#>")[3];
																 
														});
														
														  
														 
														if(dataObj){	
															$.each(dataObj.root, function(idx,item){
																showcount= showcount+1;
																	//输出每个root子对象的名称和值 
																loadzym=loadzym+"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value=''></p></div>";
															//	document.getElementById('input1').value=item.page;    
																pagexml =pagexml +"<item id='"+showcount+"' text='"+item.name+"'></item>"; 
																mycars[idx]="input"+showcount+"<#>"+item.page+"<#!!#>01"+showcount;  
															});
															
															document.getElementById("zym").innerHTML=loadzym; 
															var i=0
															for (i=0;i<mycars.length;i++)
															{
															 	var temp = mycars[i];
															 	document.getElementById(temp.split("<#>")[0]).value=temp.split("<#>")[1];
															}
															
														 }
														//到这里才来保存 input0 的原因是，如果在上面保存，里面的&quot;会被替换成“号，会影响到预览，所以放到 这里来
														 document.getElementById("input0").value = main_va+"<#!!#>010"; 
														
														
												}else{
                                                    /*操作失败！*/
                                                    alert(getJsLocaleMessage("common","common_operateFailed"));
												}
											},"json");	
									}
									
									function UpdateUeditor(value,name,divrm,link){
									 
										var edContent = editor.getContent(); 
										var parentli = document.getElementById(oldvalue).value.split("<#!!#>");  //取出旧的数据
										if(oldvalue!=value ){ //&& oldname!=name 不是input0 
												
											var valueandname = document.getElementById(value).value;  //取出当前的数据
											//保存上次的网讯名称,父结点0 子结点为父结点ID 新增子结点-1 ，内容，pageid 新增 父结点0 子结点-1  ，1+showcount	 
											document.getElementById(oldvalue).value=parentli[0]+"<#!!#>"+parentli[1]+"<#!!#>"+edContent+"<#!!#>"+parentli[3]+"<#!!#>"+parentli[4];    
													
										//	if(valueandname!=""){
											editor.setContent(valueandname.split("<#!!#>")[2]); //获得当前网讯的值  
										//	}else{ 
										//			parentID = 1 ;
										//			 pageid=1;
										//			if(oldvalue=="input0"){
										//				parentID = 0;
										//				 pageid=1; 
										//			}
												
										//			document.getElementById(oldvalue).value=parentli[0]+"<#!!#>"+parentID+"<#!!#>"+edContent+"<#!!#>"+pageid;    //保存上次的网讯名称,值
										//			editor.setContent(valueandname); //获得当前网讯的值
											oldvalue = value;
									//		oldname = name ; 
											oldlink = link;
											olddiv=divrm;		
										}
										
									//	}
										
									}
									
									
								</script>
<script>
		
		
		var type=null;
		var node=0;
		
		function correctSizes(){
			document.getElementById('tabbarconteiner').style.height = (document.body.offsetHeight - 70)+'px'
		}
		
		/* init tree */
		var tree;
		  $("#muchose").hide();
		var tree_smpl
		function loadTree(xmlstring){
		      <% if(netid==null){ %>
				document.getElementById("zym").innerHTML =document.getElementById("zym").innerHTML +"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value='"+getJsLocaleMessage("ydwx","ydwx_wxbj_38")+"<#!!#>0<#!!#><p></p><#!!#>0<#!!#>010'></p></div>";
			<%}%>
			tree=new dhtmlXTreeObject("doctree_box","100%","100%",-1);
			tree.setImagePath("<%=path%>/dhtmlxTree/docsExplorer/codebase/imgs/");
			tree.setOnClickHandler(function(id){openPathDocs(id);});
			tree.attachEvent("onOpenEnd",updateTreeSize);
			//tree.loadXML("<%=path%>/dhtmlxTree/docsExplorer/xml/a.xml",autoselectNode);
			tree.loadXMLString(xmlstring,autoselectNode);
		}
		
	  function changeName(){
		      $("#cname").toggle();   
		      $("#doctree_box").css({
		           "background-color":"black",width:"18%",height:"420px","filter":"alpha(opacity=10)"
		     });
		      
		}
		$("#pageName").keyup(function(){
		         if($(this).val().length>20){
		            /*名字过长*/
		           alert(getJsLocaleMessage("ydwx","ydwx_survey_text_4"));
		           $(this).val(getJsLocaleMessage("ydwx",""));
		           return;
		         }
		});  
		
		$("#cOK").click(function(){
		       var nodeid=tree.getSelectedItemId();
		        if(nodeid==""){
		         /*请选择节点*/
		         alert(getJsLocaleMessage("ydwx","ydwx_wxbj_55"));
		         return;
		      }
		      else{
		      var node=tree._globalIdStorageFind(nodeid);
		      node.span.innerHTML=$("#pageName").val();
		      var Nodevalue = document.getElementById("input"+nodeid).value;
			  var namelist = Nodevalue.split("<#!!#>"); 
			  document.getElementById("input"+nodeid).value = $("#pageName").val()+"<#!!#>"+namelist[1]+"<#!!#>"+namelist[2]+"<#!!#>"+namelist[3]+"<#!!#>"+namelist[4] ; 
		
		      $("#cname").toggle();
		      $("#doctree_box").css({
		           "background-color":"white",width:"18%",height:"420px","filter":"alpha(opacity=100)"
		       });
		      $("#pageName").val("");
		      }
		});
		$("#cNO").click(function(){
		     $("#pageName").val("");
		      $("#cname").toggle();
		      $("#doctree_box").css({
		           "background-color":"white",width:"18%",height:"420px","filter":"alpha(opacity=100)"
		     });
		
		});
	  
	    //点击事件
		function openPathDocs(id){
		  
		      UpdateUeditor('input'+id,'a'+id,'div'+id,'01'+id);
			
		}
		
		function updateTreeSize(){
			this.allTree.style.overflow = "visible";
			this.allTree.style.height = this.allTree.scrollHeight+"px";
			
		}
		
		function autoselectNode(){
			
				tree.selectItem(node,true);tree.openItem(node)
			
		}  
		
		
		//添加子节点
		function newTree(){
		   var nodeid=tree._globalIdStorageFind("0");  //根据iD 获取节点
		   var text=nodeid.span.innerHTML;               //得到节点的名称
		   var count=tree._getLeafCount(nodeid);   //得到节点下子节点数
		   if(count>8){
		       /*最大限度10个页面*/
		       alert(getJsLocaleMessage("ydwx","ydwx_survey_text_5"));
		       return;
		   }
		   else{
		   showcount = showcount+1; 
		   tree.insertNewItem("0",showcount,"page"+(showcount));   //增加子节点  第五个参数超链接用，以1+showcount
	       document.getElementById("zym").innerHTML=document.getElementById("zym").innerHTML+"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value='page"+showcount+"<#!!#>-1<#!!#><p></p><#!!#>-1<#!!#>01"+showcount+"'></p></div>"; 
			
		   }
		   								
		}
		
		
		//删除子节点
		function delNode(){
		
		      var nodeid=tree.getSelectedItemId();
		       if(nodeid==""){
                   /*请选择节点*/
                   alert(getJsLocaleMessage("ydwx","ydwx_wxbj_55"));
		         return;
		       }
		      if(nodeid==0){
		           /*默认页面不能删除*/
		         alert(getJsLocaleMessage("ydwx","ydwx_wxbj_59"));
		         return;
		      }
		      else{
		         removezym();
		        tree.deleteItem(nodeid,true);
		        
		      }
		}
		
		$(function(){
                     <% if(netid!=null){ %>
							IDbyInfo();
							
						<% }%>
		})
 
	 
	// 用来得到网讯预览中有引用子页面  
		function mylink(s,c,Custom){ 
			var chevalue = document.getElementById(oldvalue).value.split("<#!!#>"); 
			var	content = editor.getContent();
			//保存最后操作的值
			document.getElementById(oldvalue).value=chevalue[0]+"<#!!#>"+chevalue[1]+"<#!!#>"+content+"<#!!#>"+chevalue[3]+"<#!!#>"+chevalue[4];  //保存上次的网讯名称,值 
			
			for(li = 0 ;li<=showcount;li++){ 
				if(document.getElementById("input"+li)){
					
					var _v = document.getElementById("input"+li).value;
					
					if(_v.split("<#!!#>")[4].replace(new RegExp("#link#","g"),"")==c || s.indexOf("w="+_v.split("<#!!#>")[3])>-1 ){
						
						document.getElementById("netInfoDiv").innerHTML=_v.split("<#!!#>")[2];   
					}
				}
				
			}
		}
</script>





<%--yuxd begin  --%>
<%-- 屏蔽掉JS的错误 begin
<script language="javascript"> 
	function killerrors() { return true; } window.onerror = killerrors;   
</script>  --%>
<%-- 屏蔽掉JS的错误 END --%>
<%--ajax begin--%>
<script language='javascript'>
var currentPageNum=1;
var currentSectionNum="";
function GetXmlHttpObject()
{
  var xmlHttp=null;
  try
    {
    // Firefox, Opera 8.0+, Safari
    xmlHttp=new XMLHttpRequest();
    }
  catch (e)
    {
    // Internet Explorer
    try
      {
      xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
      }
    catch (e)
      {
      xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
    }
  return xmlHttp;
}


function stateChanged() 
{ 
  if (xmlHttp.readyState==4)
  { 
	document.getElementById("mainPageTemplateContainer").innerHTML=xmlHttp.responseText;
	enableTooltips();
	hideTooltip(null);
	
  }
}
function templateStateChanged() 
{ 

  if (xmlHttp.readyState==4)
  {  	
	var text = xmlHttp.responseText;
  	// document.getElementById("editor").innerHTML=text;
  	editor.setContent(text);
  	
	//enableTooltips();暂时注释掉，以后可能有用
	
  }
}
 function templateApplyConfirm(str){

  $("#muchose").show();
  $("#overlay1").show();
  if(document.getElementById("deck")!="" && document.getElementById("deck")!=null){
   cancel();
   }
    modid=str;
    $.post("<%=path %>/wx_manger.htm?method=findCreatid",{"netid":str},function(data){
       creatid=data;
    });
 }
  function closemudiv(){
    $("#muchose").hide();
    $("#overlay1").hide();
  }

	//选择模板
	function muConfirm(s){
			 $("#muchose").hide();
			 $("#overlay1").toggle();
	     var str= modid;
		var loadzym =document.getElementById("zym").innerHTML;
        var mycars=new Array();
        var d ="";
        var nodeid=tree._globalIdStorageFind("0");
         var count=tree._getLeafCount(nodeid);   //得到节点下子节点数
		if(s=='3'){
			if(document.getElementById("deck")!="" && document.getElementById("deck")!=null)
				cancel();
			xmlHttp=GetXmlHttpObject()
 	    	if (xmlHttp==null)
    		{
    		    /*您的浏览器不支持AJAX！*/
    			alert (getJsLocaleMessage("ydwx","ydwx_wxbj_42"));
    			return;
    		}
	   //发送请求，并将在onComplete选项中调用回调函数
	 $.post('<%=path %>/wx_ueditor.htm?method=getParentUdeitor',{Action:"post",netid:str,"creteid":creatid},function(result){
		    
			if(result.success){
			       
			            var s=$("#input0").val();
			           openPathDocs("0");
			           showcount=0;
			            tree.deleteChildItems(0);
					   loadzym="";
					var dataObj=result.msg;//转换为json对象 
					var baseObj=result.base;//转换为json对象 
					$.each(baseObj.root, function(idx,item){
					   var sst;
					sst= s.split("<#!!#>")[0]+ "<#!!#>"+s.split("<#!!#>")[1]+"<#!!#>"+item.page.split("<#!!#>")[2]+"<#!!#>"+ s.split("<#!!#>")[3]+"<#!!#>010";
					   document.getElementById("zym").innerHTML="<div id='div0'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input0' value='"+sst+"'></p></div>";  
					  editor.setContent(item.page.split("<#!!#>")[2]);
					  pageid=s.split("<#!!#>")[3];
					});
					if(dataObj){	
						$.each(dataObj.root, function(idx,item){
						 var ss;
						 showcount= showcount+1;
					  if(item.page.split("<#!!#>")[0]=='null'){
					    ss='page'+showcount;
					  }else{
					    ss=item.page.split("<#!!#>")[0];
					  }
							
								//输出每个root子对象的名称和值 
							loadzym=document.getElementById("zym").innerHTML+"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value='"+ss+"<#!!#>1<#!!#>"+item.page.split("<#!!#>")[2]+"<#!!#>-1<#!!#>01"+showcount+"'></p></div>";  
						 tree.insertNewItem("0",showcount,ss);   //增加子节点
	                      document.getElementById("zym").innerHTML=loadzym;
						});
						
					 }
				//	document.getElementById('input1').value=item.page; 
			}else{
                /*操作失败！*/
                alert(getJsLocaleMessage("common","common_operateFailed"));
			}
		},"json");	
		}
		else{
		if(document.getElementById("deck")!="" && document.getElementById("deck")!=null)
				cancel();
			xmlHttp=GetXmlHttpObject()
 	    	if (xmlHttp==null)
    		{
                /*您的浏览器不支持AJAX！*/
                alert (getJsLocaleMessage("ydwx","ydwx_wxbj_42"));
    			return;
    		}
    	 $.post('<%=path %>/wx_ueditor.htm?method=getParentUdeitor',{Action:"post",netid:str,"creteid":creatid},function(result){
			if(result.success){
					var dataObj=result.msg;//转换为json对象 
					var baseObj=result.base;//转换为json对象 
					if(tree._getLeafCount(nodeid)<9){
					$.each(baseObj.root, function(idx,item){
					   var ss;
					    showcount= showcount+1;
					  if(item.page.split("<#!!#>")[0]=='null'){
					    ss='page'+showcount;
					  }else{
					    ss=item.page.split("<#!!#>")[0];
					  }
					 
								//输出每个root子对象的名称和值 
							loadzym=loadzym+"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value='"+ss+"<#!!#>1<#!!#>"+item.page.split("<#!!#>")[2]+"<#!!#>-1<#!!#>01"+showcount+"'></p></div>"; 
						 tree.insertNewItem("0",showcount,ss);   //增加子节点
	                      document.getElementById("zym").innerHTML=loadzym;
					});
					if(tree._getLeafCount(nodeid)<9){
					if(dataObj){	
						$.each(dataObj.root, function(idx,item){
						count=tree._getLeafCount(nodeid);
						if(count>8){
						    /*最大限度10个页面，模板其它页面不能加载*/
					       alert(getJsLocaleMessage("ydwx","ydwx_wxbj_43"));
					       return false;
						   }
						 var ss;
						 showcount= showcount+1;
					  if(item.page.split("<#!!#>")[0]=='null'){
					    ss='page'+showcount;
					  }else{
					    ss=item.page.split("<#!!#>")[0];
					  }
							
								//输出每个root子对象的名称和值 
							loadzym=loadzym+"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value='"+ss+"<#!!#>1<#!!#>"+item.page.split("<#!!#>")[2]+"<#!!#>-1<#!!#>01"+showcount+"'></p></div>"; 
						 tree.insertNewItem("0",showcount,ss);   //增加子节点
	                      document.getElementById("zym").innerHTML=loadzym;
						
						});
						
					 }
					 }else{
                        /*最大限度10个页面，模板其它页面不能加载*/
                        alert(getJsLocaleMessage("ydwx","ydwx_wxbj_43"));
					 }
					 }else{
					    /*最大限度10个页面，模板页面不能加载*/
					 alert(getJsLocaleMessage("ydwx","ydwx_wxbj_127"));
					 }
				//	document.getElementById('input1').value=item.page; 
			}else{
                /*操作失败！*/
                alert(getJsLocaleMessage("common","common_operateFailed"));
			}
		},"json");	
    		
		}
	}
	
	function templateApply(str){
		//document.all.templateID.value=str;
		document.getElementById('templateID').value=str;
		//alert(document.all.templateImage.src);
		document.getElementById('templateImage').src="<%=request.getContextPath()%>"+"/images/templateImage/"+str+".jpg";
		showDlg();
		
	}
	
	
	//显示选择的类别的选择页面的数据
	function showPage(showPageNum){
		if(showPageNum==currentPageNum) return;
		xmlHttp=GetXmlHttpObject()
 	    if (xmlHttp==null)
    	{
            /*您的浏览器不支持AJAX！*/
            alert (getJsLocaleMessage("ydwx","ydwx_wxbj_42"));
    		return;
    	}

		var url="<%=request.getContextPath()%>/jsp/template.jsp";
		url=url+"?section="+currentSectionNum;
		url=url+"&pageNum="+showPageNum;
		xmlHttp.onreadystatechange=stateChanged;
		xmlHttp.open("GET",url,true);
		xmlHttp.send(null);
		currentPageNum = showPageNum;//这个时候要将当前页码值进行设置
	}
	
	function showPreviousPage(){
		showPage(currentPageNum-1);//不用考虑最后一页和第一页的问题，由image.jsp处理
	}
	
	function showNextPage(){
		showPage(currentPageNum+1);
	}
	
	function selectTemplate(showSectionNum){
		showSectionNum =showSectionNum.value;
		if(currentSectionNum==showSectionNum) return;
		xmlHttp=GetXmlHttpObject()
 	    if (xmlHttp==null)
    	{
            /*您的浏览器不支持AJAX！*/
            alert (getJsLocaleMessage("ydwx","ydwx_wxbj_42"));
    		return;
    	}

		var url="<%=request.getContextPath()%>/jsp/template.jsp";
		url=url+"?section="+showSectionNum;
		url=url+"&pageNum="+1;
		xmlHttp.onreadystatechange=stateChanged;
		xmlHttp.open("GET",url,true);
		xmlHttp.send(null);
		currentSectionNum = showSectionNum;//这个时候要将当前类别进行设置
		currentPageNum=1;
	}
	 
</script>
<%-- ajax end --%>
<%-- popup div begin --%>


<script type="text/javascript">
    function showDlg()
    {
        //显示遮盖的层
        var objDeck = document.getElementById("deck");
        if(!objDeck)
        {
            objDeck = document.createElement("div");
            objDeck.id="deck";
            document.body.appendChild(objDeck);
        }
        objDeck.className="showDeck";
        objDeck.style.filter="alpha(opacity=50)";
        objDeck.style.opacity=40/100;
        objDeck.style.MozOpacity=40/100;
        //显示遮盖的层end
        
        //禁用select
        hideOrShowSelect(true);
        
        //改变样式
        document.getElementById('divBox').className='showDlg';
        
        //调整位置至居中
        adjustLocation();
       $("#overlay1").toggle();
        
    }
    
    function cancel()
    {
        document.getElementById('divBox').className='hideDlg';
        document.getElementById("deck").className="hideDeck";
        hideOrShowSelect(false);
        $("#overlay1").toggle();
    }
    
    function hideOrShowSelect(v)
    {
        var allselect = document.getElementsByTagName("select");
        for (var i=0; i<allselect.length; i++)
        {
            //allselect[i].style.visibility = (v==true)?"hidden":"visible";
            allselect[i].disabled =(v==true)?"disabled":"";
        }
    }
    
    function adjustLocation()
    {
        var obox=document.getElementById('divBox');
        if (obox !=null && obox.style.display !="none")
        {
            var w=239;
            var h=450;
            var oLeft,oTop;
            
            if (window.innerWidth)
            {
                oLeft=window.pageXOffset+(window.innerWidth-w)/2 +"px";
                oTop=window.pageYOffset+(window.innerHeight-h)/2 +"px";
            }
            else
            {
                var dde=document.documentElement;
                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2 +"px";
                oTop=dde.scrollTop+(dde.offsetHeight-h)/2 +"px";
            }
            
            obox.style.left=oLeft;
            obox.style.top=oTop;
        }
    }
    
</script>

    <%-- popup div end --%>
<%-- yuxd end --%>
<%--网讯预览 --%>
<script type="text/javascript">
    function showNetDlg()
    {
    
   		document.getElementById("netInfoDiv").innerHTML=editor.getContent();

        //禁用select
     	hideOrShowSelect();
        //改变样式
        document.getElementById('showdivBox').className='showNetbox';
             
        //调整位置至居中
      // Location();
    ShowLocationNet();
   $("#overlay1").toggle();
    }
    
     //20111215调整居中
     function ShowLocationNet()
    {
  
        var netbox=document.getElementById('showdivBox');
        var imgs = document.getElementById('img');
        
        var netInfo = document.getElementById('netInfoDiv');
      
         if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){
    
			if (netbox !=null && netbox.style.display !="none")
	        {
	
	        
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	         
            if (window.innerWidth)
            {
                oLeft=window.pageXOffset+(window.innerWidth-w)/2.5 +"px";
                oTop=window.pageYOffset+(window.innerHeight-h)/2.5 +"px";
            }
            else
            {
           
                var dde=document.documentElement;
                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2.5 +"px";
                oTop=dde.scrollTop+(dde.offsetHeight-h)/2.5 +"px";
            }
            
                netInfo.style.left="0px";
                netInfo.style.top="31px";
                
                imgs.style.left="0px";
	            imgs.style.top="-45px"; 
	            netbox.style.left=oLeft;
	            netbox.style.top=oTop;
	        }
	        
		}else{
	
	        if (netbox !=null && netbox.style.display !="none")
	        {
	 
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	            
	            if (window.innerWidth)
	            {
	      
	                oLeft=window.pageXOffset+(window.innerWidth-w)/2.5 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/2.5 +"px";
	                
	                
	            }
	            else
	            {
	       
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2.5 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/2.5 +"px";
	                
	            }
	            
	           
	           imgs.style.left="0.8px";
	           imgs.style.top="3px"; 
	            netbox.style.left=oLeft;
	            netbox.style.top=oTop;
	        }
	    }
    }
    
    
    function cancelDiv()
    {
        document.getElementById('showdivBox').className='hideDlg';
       
        hideOrShowSelect(false);
        $("#overlay1").toggle();
    }
    
    function hideOrShowSelect(v)
    {
        var allselect = document.getElementsByTagName("select");
        for (var i=0; i<allselect.length; i++)
        {
            //allselect[i].style.visibility = (v==true)?"hidden":"visible";
            allselect[i].disabled =(v==true)?"disabled":"";
        }
    }
    
    function Location()
    {
        var netbox=document.getElementById('showdivBox');
        var imgs = document.getElementById('img');
         if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){
			if (netbox !=null && netbox.style.display !="none")
	        {
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	            
	            if (window.innerWidth)
	            {
	                oLeft=window.pageXOffset+(window.innerWidth-w)/2 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/2 +"px";
	            }
	            else
	            {
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/2 +"px";
	            }
	            netbox.style.left=oLeft;
	            netbox.style.top=oTop;
	        }
	        
		}else{
	        if (netbox !=null && netbox.style.display !="none")
	        {
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	            
	            if (window.innerWidth)
	            {
	                oLeft=window.pageXOffset+(window.innerWidth-w)/2 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/2 +"px";
	                
	                
	            }
	            else
	            {
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/2 +"px";
	                
	            }
	            
	           
	            imgs.style.left="0.8px";
	            imgs.style.top="3px"; 
	            netbox.style.left=oLeft;
	            netbox.style.top=oTop;
	        }
	    }
    }
    
</script>
 
 