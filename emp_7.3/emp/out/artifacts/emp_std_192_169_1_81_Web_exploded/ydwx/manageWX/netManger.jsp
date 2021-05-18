<%@ page language="java" import="com.montnets.emp.common.constant.ViewParams" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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
PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");


//按钮权限
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String rTitle = (String)request.getAttribute("rTitle");
String menuCode = titleMap.get(rTitle);
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String flowId = request.getParameter("flowId");
String langName = (String)session.getAttribute("emp_lang");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
<style>
  #overlay1{position:absolute;top:0;left:0;width:100%;height:100%;background:white;opacity:0.1;filter:alpha(opacity=10);-moz-opacity:0.1;display:none;}
   #overlay2{position:absolute;top:0;left:0;width:100%;height:100%;background:white;opacity:0.1;filter:alpha(opacity=10);-moz-opacity:0.1;display:none;}
</style>
<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
<%if(StaticValue.ZH_HK.equals(langName)){%>
<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<style type="text/css">
	#submit,#SetWxTemp > table > tbody > tr:nth-child(5) > td:nth-child(2) > input.btnClass6{
		text-indent: 0px;
		text-align: center;
	}
</style>
<%}%>
<link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>"/>

<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=inheritPath%>/scripts/netManger.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/function.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
<script type="text/javascript" src="<%=empFramePath%>/js/dialogui.js"></script>
<script type="text/javascript">
			function cancleTemplate(id,act,path){
				if(confirm(getJsLocaleMessage("ydwx","ydwx_wxbj_1"))){
					$.post("wx_template.htm?method=cancelType",{netid:id},function(data){
					 if(data=="true"){
					    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_2"));
					    var lguserid = $("#lguserid").val();
						var path=$("#pathUrl").val();
						$("#pageForm").attr("action",path+"/wx_manger.htm?method=find&&lguserid="+lguserid+"&&pageIndex="+pageIndex+"&&pagesize="+pageSize);
						submitForm();
					 }else if(data=="false"){
	                    alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));  				   
					 }
					});
				}
			}  
    /*设置模板*/
	function setNetTemplate(id,act){
			$("#netTempId").val(id);
			if(act==1)
			{
					if(confirm(getJsLocaleMessage("ydwx","ydwx_wxbj_3")))
					{
					   var lgcorpcode = $("#lgcorpcode").val();
					   $.post("wx_manger.htm?method=getSort",{lgcorpcode:lgcorpcode},function(result){
					       	if(result!=""&&result.length>0)
					       	{
								 var data=eval("("+result+")");
								 var fs=$("#fType");
								 if(data.length>0)
								 {
								    //$("#pres").show();
								    $("#pres").dialog({
										autoOpen: false,
										height:200,
										width: <%="zh_HK".equals(empLangName)?460:400%>,
										modal: true,
										open:function(){
										},
										close:function(){
											doCancel();
										}
									});
								    $("#pres").dialog("open");
								    $(".ui-dialog-titlebar-close").hide();
						            $("#overlay1").toggle();
						            $("#fType").empty();
								    for(var i=0;i<data.length;i++)
								    {
								        var op=$("<option>").val(data[i].sortid).html(data[i].name).appendTo(fs);
								    }    
								 }
								 else
						       	 {
								    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_4"));
						       	 }
					       	 }
					       	else
					       	{
							    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_4"));
					       	}
						});
					}
					
			}else if(act==2)
			{
				if(confirm(getJsLocaleMessage("ydwx","ydwx_wxbj_1")))
				{
					$.post("wx_manger.htm?method=cancelType",{netid:id},function(data){
							 if(data=="true"){
							    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_2"));
							    var lguserid = $("#lguserid").val();
								var path=$("#pathUrl").val();
								$("#pageForm").attr("action",path+"/wx_manger.htm?method=find&&lguserid="+lguserid+"&&pageIndex="+pageIndex+"&&pagesize="+pageSize);
								submitForm();
							 }else if(data=="false"){
			                    alert(getJsLocaleMessage("ydwx","ydwx_common_czshb")); 				   
							 }
					});
				}
			}
	}
	
      function showaddtem(){
			<% String result=(String)session.getAttribute("msg");
				if(result!=null ){
					session.removeAttribute("msg");
					if(result.equals("图片上传异常，请重新上传！")){
					    result = MessageUtils.extractMessage("ydwx","ydwx_upload_1",request);
					}else if(result.equals("图片上传异常！")){
					    result = MessageUtils.extractMessage("ydwx","ydwx_upload_2",request);
					}else if(result.equals("网讯模板设置成功")){
					    result = MessageUtils.extractMessage("ydwx","ydwx_upload_3",request);
					}else if(result.equals("需上传小图与大图两张图片！")){
					    result = MessageUtils.extractMessage("ydwx","ydwx_upload_4",request);
					}else if(result.equals("程序处理异常！")){
					    result = MessageUtils.extractMessage("ydwx","ydwx_upload_6",request);
					}else if(result.startsWith("图片尺寸超过规定大小")){
					    result = result.replace("图片尺寸超过规定大小:",MessageUtils.extractMessage("ydwx","ydwx_upload_5",request))+result.replace("字节！",MessageUtils.extractMessage("ydwx","ydwx_wxgl_wxsc_zijie",request));
					}
				%>
				alert("<%=result%>");
			<%}%>
		}
      function setTemp()
    {
    	 
        var msg="jpg,JPG,bmp,BMP,gif,GIF,img,IMG,png,PNG,jpeg,JPEG";//图片类型
    	if($("#textfield1").val()=="")
    	{
    		alert(getJsLocaleMessage("ydwx","ydwx_wxbj_5"));
    		return false;
    	}
    	/* if($("#textfield2").val()=="")
    	{
    		alert("大图不能为空，请上传大图！");
    		return false;
    	}*/
    	var text1=$("#textfield1").val();
    	//var text2=$("#textfield2").val();
    	var str1=text1.split("\.");
    	var ss1=str1[str1.length-1];
    	//var str2=text2.split("\.");
    	//var ss2=str2[str2.length-1];
    	if(msg.indexOf(ss1)<0){
    	  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_6"));
    	  return false;
    	}
    	/*if(msg.indexOf(ss2)<0){
           alert("大图上传格式不匹配，请重新选择");  
           return false; 	 
        }*/
     	 $("#SetWxTemp").attr("action","<%=path%>/wx_manger.htm?method=SetType&pageIndex="+pageIndex+"&pagesize="+pageSize+"&netid="+$("#netTempId").val()+"&fType="+$("#fType").val()+"&lguserid="+$("#lguserid").val());
    }
    
      
      function toAdd(){
			location.href="wx_ueditor.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
		}
      function closediv()
      {
	      //$("#pres").hide();
	      $("#pres").dialog("close");
	      $("#textfield1").val("");
	      $("#textfield2").val("");
	      $("#overlay1").toggle();
      }
	$(document).ready(function() {
		getLoginInfo("#hiddenValueDiv");
		$('#search').click(function(){submitForm();});
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		$("#pres").hide();
     	showaddtem();
		$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
		loadNetmanger();
		      /* 转到第n页*/
		$(".wtime").each(function(){
	          var wtime=$(this).html();
	          if($(wtime.length==19)){
	               $(this).html(wtime.substr(0,11));
	          }
		});
		
		  //页码改变时  div层内容变化的方法
		 $(".ym").change(function(){
            var id=$(this).val();
            for(var i=0;i<listpage.length;i++){
                if(id==listpage[i].id){
                	if(listpage[i].content=="notexists"){
	   			          $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
				      }else{
				    	  $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
				      }
                }
            }
        });
		  $("#divBox").dialog({
				autoOpen: false,
				height:520,
				width: 300,
				modal: true,
				close:function(){
                    $('#nm_preview_common').attr('src','');
				}
			});
		$("#smsdetailinfo").dialog({
			autoOpen: false,
			modal:true,
			title:getJsLocaleMessage("ydwx","ydwx_wxbj_7"), 
			width:680,
			height: 'auto',
			minHeight:170,
			maxHeight:650,
			closeOnEscape: false,
			resizable:false,
			open:function(){
			},
			close:function(){
			}
		});	
		$("#reviewflowinfo").dialog({
				autoOpen: false,
				modal:true,
				title:getJsLocaleMessage("ydwx","ydwx_wxbj_8"), 
				width:880,
				height: 'auto',
				minHeight:170,
				maxHeight:750,
				closeOnEscape: false,
				resizable:false,
				open:function(){
				},
				close:function(){
				}
			});		
		//运营商审批状态查询回显
		$("#operStatus option[value='${operStatus}']").attr("selected","selected");
	});

	function Look(netId){
	    $("#netid").val(netId);
	    $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
	       data=eval("("+data+")");
	       listpage=data;
	       $(".ym").children().remove();
	       for(var i=0;i<listpage.length;i++)	{   
	           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
	      }
            // 此处为显示错误页面，避免进入登录页面
            if(listpage[0].content=="notexists"){
                $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
            }else{
                $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
            }
            
            resizeDialog($("#divBox"),"ydwxDialogJson","ydwx_netManager_divBox");
	      	$("#divBox").dialog("open");
       });
    }
    
    function showNotify(){
		var res = <%=(String)session.getAttribute("callbackResult")%>;
		var net_show_state="<%=session.getAttribute("net_show_state")%>";
		var tip="";
		if(net_show_state=="1"){
			tip=getJsLocaleMessage("ydwx","ydwx_wxbj_9");
			}
		if(res != null && res !=""){
			<%session.removeAttribute("callbackResult");%>
			<%session.removeAttribute("net_show_state");%>
			if(res){
				var showmsg=getJsLocaleMessage("ydwx","ydwx_common_czchg")+tip;
				alert(showmsg);
			}else {
				alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));
			}
		}
	}	
    	<%--模板审批详情--%>
	   function opentmpAudmsg(tmpid){
			 var pathUrl = $("#pathUrl").val();
			 var lguserid = $("#lguserid").val();
			 $.post(pathUrl+"/wx_manger.htm?method=getSmsTplDetail",{tmpid:tmpid,lguserid:lguserid},function(jsonObject){
				 var json = eval("("+jsonObject+")");
				//是否有 审批记录
				var haveRecord = json.haveRecord;
				var firstshowname = json.firstshowname;
				var firstcondition = json.firstcondition;
				var secondshowname = json.secondshowname;
				var secondcondition = json.secondcondition;
				var isshow = json.isshow;
				$("#recordTable").empty();

				var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_10")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_11")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_12")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_13")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_14")+"</td></tr>";
				if(haveRecord == "1"){
					//审批记录
					var recordList = json.members;
					 for(var i= 0;i<recordList.length;i++){
							var sms_Rlevel = recordList[i].smsRlevel;
							var sms_Reviname = recordList[i].smsReviname;
							var sms_Exstate = recordList[i].smsexstate;
							var sms_Comments = recordList[i].smsComments;
							var sms_rtime = recordList[i].smsrtime;
							msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+sms_Rlevel+"</td> "
			        		+"  <td align='center'  width='15%'  class='div_bd'>"+sms_Reviname.replaceAll("<","&lt;").replaceAll(">","&gt;")+"</td>"          
					        +"  <td align='center'  width='15%'  class='div_bd'>"+sms_Exstate+"</td> "
					        +"  <td align='left'  width='35%'  style='word-break: break-all;'  class='div_bd'><xmp style='word-break: break-all;white-space:normal;'>"
					        +sms_Comments.replaceAll("<","&lt;").replaceAll(">","&gt;")+"</xmp></td>" 
					        +" </td> <td align='center'  width='25%'  class='div_bd'>"+sms_rtime+"</td></tr>";
					}
				}else{
					msg = msg + "<tr class='div_bd' height='24px'> <td  align='center' class='div_bd' colspan='5'>无记录</td></tr>";
				}
				$("#recordTable").html(msg);
				$('#recordTableDiv').show();	
				$("#nextrecordmgs").empty();
				if(isshow == "1"){
					var recordmsg =getJsLocaleMessage("ydwx","ydwx_wxbj_15")+ "&nbsp;" + firstshowname;
					if(firstcondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_16");
					}else if(firstcondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_17");
					}
					if(secondshowname != "" && secondcondition != ""){
						recordmsg = recordmsg + "</br>"+getJsLocaleMessage("ydwx","ydwx_wxbj_18")+"&nbsp;" + secondshowname;
						if(secondcondition == "1"){
							recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_16");
						}else if(secondcondition == "2"){
							recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_17");
						}
					}
					$("#nextrecordmgs").html(recordmsg);
					$('#nextrecordmgs').show();	
				}
				$("#smsdetailinfo").dialog("open");
			 });
		   
	   }
	   //点详细，弹出框
function openReviewFlow(mtId,userId,reviewType){
	$('#reviewTableDiv').hide();
	$.post("reviewflow.htm?method=getReviewFlow",{mtId:mtId,userId:userId,reviewType:reviewType},function(jsonObject){
		 var json = eval("("+jsonObject+")");
		//是否有 审批记录
		var haveRecord = json.haveRecord;

		var onelevel = json.onelevel;
		var onecondition = json.onecondition;
		var twolevel = json.twolevel;
		var twocondition = json.twocondition;
		var threelevel = json.threelevel;
		var threecondition = json.threecondition;
		var fourlevel = json.fourlevel;
		var fourcondition = json.fourcondition;
		var fivelevel = json.fivelevel;
		var fivecondition = json.fivecondition;
		var isshow = json.isshow;
		$("#reviewTable").empty();
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_10")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_11")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_19")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_12")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_13")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_14")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_15")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_common_caozuo")+"</td></tr>";
		if(haveRecord == "1"){
			//审批记录
			var recordList = json.members;
			 for(var i= 0;i<recordList.length;i++){
					var Rlevel = recordList[i].Rlevel;
					var Reviname = recordList[i].Reviname;
					var Exstate = recordList[i].exstate;
					var Exresult= recordList[i].exresult;
					var Comments = recordList[i].Comments;
					var rtime = recordList[i].rtime;
					var remindtime=recordList[i].remindtime;
					var allowremind=recordList[i].allowremind;
					var frid=recordList[i].flowid;
					var existreviewer=recordList[i].existreviewer;
					if(existreviewer=="1"){
						msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+Rlevel+"</td> "
		        		+"  <td align='center'  width='10%'  class='div_bd'>"+Reviname+"</td>"          
				        +"  <td align='center'  width='10%'  class='div_bd'>"+Exstate+"</td> "
				         +"  <td align='center'  width='10%'  class='div_bd'>"+Exresult+"</td> "
				        +"  <td align='left'  width='20%'  style='word-break: break-all;'  class='div_bd'>";
	
				        var view_Comments=Comments.length>17?(Comments.substr(0,17)+"..."):Comments;
				        msg=msg+"<a onclick='javascript:modify(this,2)' style='cursor: pointer;'><label style='display:none'><xmp>"+Comments+"</xmp></label>"
						+"<xmp>"+view_Comments+"</xmp></a>"
				        +"</td> <td align='center'  width='15%'  class='div_bd'>"+rtime+"</td>" 
				        +"</td> <td align='center'  width='15%'  class='div_bd'>"+remindtime+"</td>" ;
				        if(allowremind=="1"){
				        	msg=msg+"<td align='center'  width='10%'  class='div_bd'><a onclick='javascript:remind("+frid+")' style='cursor: pointer;color:blue;'>"+getJsLocaleMessage("ydwx","ydwx_wxbj_21")+"</a></td>" +"</tr>" ;
				        }else{
				        	msg=msg+"<td align='center'  width='10%'  class='div_bd'>"+allowremind+"</td>" +"</tr>" ;
				        }
			        }else{
			        	msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+Rlevel+"</td> "
		        		+"  <td colspan='6' align='center'  width='80%'  class='div_bd' style='color:red;'>"+Exstate+"</td>";          
				        msg=msg+"<td align='center'  width='10%'  class='div_bd'>-</td>" +"</tr>" ;
				        
			        }
			        
			}
		}else{
			msg = msg +	"<tr><td colspan='8' align='center'  class='div_bd' height='24px'>"+getJsLocaleMessage("ydwx","ydwx_common_wujilu")+"</td></tr>";
		}
		$("#reviewTable").html(msg);
		
		$('#reviewTableDiv').show();	
		$("#nextreviewmgs").empty();
		if(isshow == "1"){
			var recordmsg = "";
				if(onelevel=="1"){
					recordmsg=recordmsg+"1"+getJsLocaleMessage("ydwx","ydwx_wxbj_22");
					if(onecondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_16");
					}else if(onecondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_17");
					}
				}
				if(twolevel=="2"){
					recordmsg=recordmsg+"</br>2"+getJsLocaleMessage("ydwx","ydwx_wxbj_22");
					if(twocondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_16");
					}else if(twocondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_17");
					}
				}
				if(threelevel=="3"){
					recordmsg=recordmsg+"</br>3"+getJsLocaleMessage("ydwx","ydwx_wxbj_22");
					if(threecondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_16");
					}else if(threecondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_17");
					}
				}
				if(fourlevel=="4"){
					recordmsg=recordmsg+"</br>4"+getJsLocaleMessage("ydwx","ydwx_wxbj_22");
					if(fourcondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_16");
					}else if(fourcondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_17");
					}
				}
				if(fivelevel=="5"){
					recordmsg=recordmsg+"</br>5"+getJsLocaleMessage("ydwx","ydwx_wxbj_22");
					if(fivecondition == "1"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_16");
					}else if(fivecondition == "2"){
						recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxbj_17");
					}
				}
			$("#nextreviewmgs").html(recordmsg);
			$('#nextreviewmgs').show();	
		}
		 $("#reviewflowinfo").dialog("open");
	 });
}

	function remind(frid){
		$.post("reviewflow.htm?method=cuibanFlow&frid="+frid,
				{},
				function(result)
				{
				  if(result=="success")
				  {
					alert(getJsLocaleMessage("ydwx","ydwx_wxbj_23"));
				  }
				  else if(result=="getTaskFail")
				  {
				    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_24"));
				  }
				  else if(result=="getDcTempFail")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_25"));
				  }
				  else if(result=="getWxTempFail")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_26"));
				  }
				  else if(result=="noPhone")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_27"));
				  }
				  else if(result=="noContent")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_28"));
				  }
				  else if(result=="noAdmin")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_29"));
				  }
				  else if(result=="noAgree")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_30"));
				  }
				  else if(result=="noDisAgree")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_31"));
				  }
				  else if(result=="noSP")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_32"));
				  }
				  else if(result=="noSubNo")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_33"));
				  }
				  else if(result=="noSpNumber")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_34"));
				  }
				  else if(result=="validPhone")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_35"));
				  }
				  else if(result=="wgkoufeiFail")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_36"));
				  }else{
					  alert(getJsLocaleMessage("ydwx","ydwx_wxbj_37"));
				  }
				}
			);
	}
</script>
<style>

</style>
<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/netManger.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>"/>
</head>
<body onload="showNotify()" id="ydwx_netManager">
<input type="hidden" id="pathUrl" value="<%=path %>" />
<input id="b" type="hidden" value="<%=request.getParameter("b")%>">
<input type="hidden" id="ba" value="<%=request.getParameter("ba")%>">
<input type="hidden" id="corp" value="${corp}">
<%
	if(CstlyeSkin.contains("frame4.0")){
%>
	<input id='hasBeenBind' value='1' type='hidden'/>
<%
	}
%>
<div id="frame">
	<%-- header开始 --%>
	<%=ViewParams.getPosition(langName,menuCode) %>
	<%-- header结束 --%>
	<%-- 内容开始 --%>
	<div id="rContent" class="rContent ydwx_rContent">
	<form name="pageForm" action="<%=path%>/wx_manger.htm?method=find" method="post" id="pageForm">
    <div id="overlay2"></div>
		
		<div class="buttons">
			<div id="toggleDiv">
			</div>
			
		<%
			if(btnMap.get(menuCode+"-1")!=null){
		%>
			<a id="add" onclick="javascript:toAdd()"><font><emp:message key="ydwx_common_btn_xinjian" defVal="新建" fileName="ydwx"><font></emp:message></a>
		<%
			}
		%>
		</div>
		<div id="condition">
				
				<div style="display:none" id="hiddenValueDiv"></div>
				<table>
					<tr>
						<td>
							<span><emp:message key="ydwx_wxgl_wxbh" defVal="网讯编号：" fileName="ydwx"></emp:message></span>
						</td>
						<td align="left">
							<input type="text" onkeyup="numberControl($(this))" name="wxid" id="wxid" value='<%=request.getParameter("wxid")==null?"": (String)request.getParameter("wxid")%>' class="ydwx_wxid"/>
						</td>
						<td>
							<span> <emp:message key="ydwx_wxgl_wxmc" defVal="网讯名称：" fileName="ydwx"></emp:message></span>
						</td>
						<td align="left">
							<input type="text" name="wxname" id="wxname" class="ydwx_wxname" value='<%=request.getParameter("wxname")==null?"": (String)request.getParameter("wxname")%>'>
						</td>
						<td>
							<emp:message key="ydwx_wxgl_EMPspzt" defVal="EMP审批状态：" fileName="ydwx"></emp:message>
						</td>
						<td align="left">
							<select id="rState" name="rState" class="input_bd ydwx_rState" isInput="false">
								<option value="">
									<emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message>
								</option>
								<option value="0" <%="0".equals(request.getAttribute("zhuangtai")) ? "selected" : ""%>>
									<emp:message key="ydwx_wxgl_EMPspzt_options2" defVal="草稿" fileName="ydwx"></emp:message>
								</option>
								<option value="1" <%="1".equals(request.getAttribute("zhuangtai")) ? "selected" : ""%>>
									<emp:message key="ydwx_wxgl_EMPspzt_options3" defVal="待审批" fileName="ydwx"></emp:message>
								</option>
								<option value="2" <%="2".equals(request.getAttribute("zhuangtai")) ? "selected" : ""%>>
									<emp:message key="ydwx_wxgl_EMPspzt_options4" defVal="审批通过" fileName="ydwx"></emp:message>
								</option>
								<option value="3" <%="3".equals(request.getAttribute("zhuangtai")) ? "selected" : ""%>>
									<emp:message key="ydwx_wxgl_EMPspzt_options5" defVal="审批未通过" fileName="ydwx"></emp:message>
								</option>
								<option value="4" <%="4".equals(request.getAttribute("zhuangtai")) ? "selected" : ""%>>
									<emp:message key="ydwx_wxgl_EMPspzt_options6" defVal="无需审批" fileName="ydwx"></emp:message>
								</option>
							</select>
						</td>
						<% if(StaticValue.getIsWxOperatorReview() ==1){ %>
						<td>
							<emp:message key="ydwx_wxgl_yysspzt" defVal="运营商审批状态：" fileName="ydwx"></emp:message>
						</td>
						<td align="left">
							<select id="operStatus" name="operStatus"  class="input_bd ydwx_operStatus">
								<option value=""><emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message></option>
								<option value="0" <%="0".equals(request.getAttribute("operStatus")) ? "selected" : ""%>><emp:message key="ydwx_wxgl_EMPspzt_options3" defVal="待审批" fileName="ydwx"></emp:message></option>
								<option value="1" <%="1".equals(request.getAttribute("operStatus")) ? "selected" : ""%>><emp:message key="ydwx_wxgl_EMPspzt_options4" defVal="审批通过" fileName="ydwx"></emp:message></option>
								<option value="2" <%="2".equals(request.getAttribute("operStatus")) ? "selected" : ""%>><emp:message key="ydwx_wxgl_EMPspzt_options5" defVal="审批未通过" fileName="ydwx"></emp:message></option>
							</select>
						</td>
						<% }%>
						<td  class="tdSer">
							<center><a id="search"></a></center>
						</td>
					</tr>
					<tr>
					<td>
						<emp:message key="ydwx_wxgl_mblx" defVal="模板类型：" fileName="ydwx"></emp:message>
					</td>
					<td align="left">
						<select id="temptype" name="temptype" class="input_bd ydwx_temptype" isInput="false">
							<option value="">
								<emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message>
							</option>
							<option value="1" <%="1".equals(request.getAttribute("temptype")) ? "selected" : ""%> >
								<emp:message key="ydwx_wxgl_mblx_options1" defVal="静态模板" fileName="ydwx"></emp:message>
							</option>
							<option value="2" <%="2".equals(request.getAttribute("temptype")) ? "selected" : ""%> >
								<emp:message key="ydwx_wxgl_mblx_options2" defVal="动态模板" fileName="ydwx"></emp:message>
							</option>
						</select>
					</td>
					<td>
						<emp:message key="ydwx_common_time_chuangjianriqi" defVal="创建日期：" fileName="ydwx"></emp:message>
					</td>
					<td align="left">
						<input type="text" name="startdate" id="startdate"
							class="Wdate ydwx_startdate" readonly="readonly"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
							value='<%=request.getParameter("startdate")==null?"": (String)request.getParameter("startdate")%>'/>
					</td>	
					<td>
						<emp:message key="ydwx_common_time_zhi" defVal="至：" fileName="ydwx"></emp:message>
					</td>
					<td>	
						<input type="text" name="enddate" id="enddate"
							class="Wdate ydwx_enddate" readonly="readonly"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
							value='<%=request.getParameter("enddate")==null?"": (String)request.getParameter("enddate")%>'/>
					</td>
					<td colspan="1"></td>
					</tr>
                    <tr>
                        <td><%if(flowId != null){out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_22",request));}%></td>
                        <td colspan="6">
                            <%if(flowId != null){
                            %>
                            <input type="text" name="flowId" class="ydwx_flowId" value="<%=flowId%>" onkeyup="javascript:numberControl($(this))" maxlength="19"/>
                            <% }%>
                        </td>
                    </tr>
				</table>
			
		</div>
		<table width="100%">
			<tr>
				<td align="left" style="display: none">
					&nbsp;
					<input type="button" class="btn_b" value="<emp:message key="ydwx_common_btn_shanqu" defVal="删 除" fileName="ydwx"></emp:message>"
					 class="ydwx_deleBtn"  onclick="alldel(1);" />
				</td>
			</tr>
		</table>
		<div class="ydwx_content_dv">
			<table id="content" >
				<thead>
					<tr>
						<th style="display: none">
							<input type="checkbox" name="checkall" id="checkall"
								value="checkbox" onclick="checkAlls(this,'checklist');" />
						</th>
						<th>
							<emp:message key="ydwx_wxgl_wxbhs" defVal="网讯编号" fileName="ydwx"></emp:message>
						</th>
						<th >
							<emp:message key="ydwx_wxgl_wxmcs" defVal="网讯名称" fileName="ydwx"></emp:message>
						</th>
						<th >
							<emp:message key="ydwx_wxgl_wxnr" defVal="网讯内容" fileName="ydwx"></emp:message>
						</th>
						<th >
							<emp:message key="ydwx_wxgl_lx" defVal="类型" fileName="ydwx"></emp:message>
						</th>
						<th >
							<emp:message key="ydwx_wxgl_EMPspzts" defVal="EMP审批状态" fileName="ydwx"></emp:message>
						</th>
						<% if(StaticValue.getIsWxOperatorReview() ==1){ %>
						<th >
							<emp:message key="ydwx_wxgl_yysspzts" defVal="运营商审批状态" fileName="ydwx"></emp:message>
						</th>
						<%} %>
						<th>
							<emp:message key="ydwx_wxgl_cjr" defVal="创建人" fileName="ydwx"></emp:message>
						</th>
						<th >
							<emp:message key="ydwx_common_time_chuangjianriqis" defVal="创建日期" fileName="ydwx"></emp:message>
						</th>
						<th >
							<emp:message key="ydwx_common_time_youxiaoriqi" defVal="有效日期" fileName="ydwx"></emp:message>
						</th>
						
						<th colspan="5" >
							<emp:message key="ydwx_common_caozuo" defVal="操作" fileName="ydwx"></emp:message>
						</th>
					</tr>
				</thead>
				<tbody id="tbs">
				<%
				@SuppressWarnings("unchecked")
				List<DynaBean> beans = (ArrayList<DynaBean>)request.getAttribute("pagebaseinfo");
				if(beans!=null && beans.size()>0)
				{
					for(DynaBean bean:beans)
					{
				%>
							<tr>
								<td style="display: none">
									<input type="checkbox" name="checklist" id="checklist"
										value='<%=bean.get("netid") %>' />
								</td>
								<td class="no_l_b">
									<%=bean.get("netid") %>
								</td>
								<td title='<%=bean.get("name") %>' class="ydwx_wxname_td">
										<% 
										String st1 = "";
										 if(!"".equals(bean.get("name"))&&bean.get("name")!=null){
												
													if(bean.get("name").toString().length()>8)
													{
														st1 = bean.get("name").toString().substring(0,8)+"...";
													}else
													{
														st1 = bean.get("name").toString();
													}
											
										}
										%>
								<a onclick=javascript:modify(this,1)>
								  <label style="display:none"><xmp><%=bean.get("name")%>
								  </xmp></label>
								  <xmp><%=st1%></xmp>
								  </a> 
									
								</td>
								<td>
									<%
									if(btnMap.get(menuCode+"-0")!=null){
									%>
										<a href="javascript:Look('<%=bean.get("netid") %>');"><emp:message key="ydwx_common_yulan" defVal="预览" fileName="ydwx"></emp:message></a>
									<%
									}else{
									out.print("-");
									}
									%>
									</td>
								<td>
								<% 
									 Short temptype =Short.valueOf(bean.get("temptype").toString());
									 if(temptype == 1){
										out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_23",request));
										//out.print("<emp:message key='ydwx_wxgl_mblx_options1' defVal='静态模板' fileName='ydwx'></emp:message>");
									 }else if(temptype == 2){
										out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_24",request));
										//out.print("<emp:message key='ydwx_wxgl_mblx_options2' defVal='动态模板' fileName='ydwx'></emp:message>");
									 }
								%>
								</td>
								<td>
								<%
								Integer sta =Integer.valueOf(bean.get("status").toString());
								boolean flag = false;
								String s = "";
								if(sta==0)
								{
									s=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_30",request);
									//out.print("草稿");
								}else if(sta==1)
								{
									s=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_25",request);
									//out.print("未审批");
									flag = true;
								}else if(sta==2)
								{	
									s=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_17",request);
									//out.print("审批通过");
									flag = true;
								}else if(sta==3)
								{
									s=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_31",request);
									//out.print("审批未通过");
									flag = true;
								}else if(sta==4)
								{
									s=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_19",request);
									//out.print("无需审批");
								}
								%>
											<%
											if(sta==2||sta==3){
												%>
													<a href="javascript:opentmpAudmsg('<%=bean.get("netid") %>')"><%=s%></a>
												<%
											}else if(sta==1){
												%>
													<a href="javascript:openReviewFlow('<%=bean.get("netid")%>','<%=bean.get("creatid")%>','6')"><%=s%></a>
												<%
											}else{
												out.print(s);
											}
											%>
								</td>
								<% if(StaticValue.getIsWxOperatorReview() ==1){%>
								<td>

									<%
										int operStatus=0;
										
										if(bean.get("operappstatus")!=null){
											operStatus=Integer.valueOf(bean.get("operappstatus").toString());
										}
										switch(operStatus){
											case 0:
												out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_25",request));
												break;
											case 1:
												out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_17",request));
												break;
											case 2:
												out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_31",request));
												break;
											default:
												out.print("-");
										}
										
									%>
								</td>
								<% }%>
								<td title='<%=bean.get("creatname") %>' class="ydwx_creatname_td">
									<%=bean.get("creatname")%>(<%=bean.get("creatcode")%>)
								</td>

								<td class="ydwx_creatdate_td">
									<% String time=bean.get("creatdate")==null?"":bean.get("creatdate")+"";
									time=time.substring(0,time.lastIndexOf("."));
									%>
									<%=time %>
									<%--<%=bean.get("creatdate") %>--%>
								</td>
								<td>
									<b class="ydwx_" style="font-weight: normal">
									<% String timeout=bean.get("timeout")==null?"":bean.get("timeout")+"";
									timeout=timeout.substring(0,timeout.lastIndexOf("."));
									%>
									<%=timeout %></b>
									<input type="text" name="wxTime" id="wxTime"
										value='<%=timeout %>' style="display: none;">
								</td>
								
								<td nowrap="nowrap">
								<%
								if(btnMap.get(menuCode+"-3")!=null)                       		
								{  
								if(sta==0 || sta==3)
								{
									String userId = (String)request.getAttribute("userId");
									if(userId.equals(String.valueOf(bean.get("creatid"))))
									{
								%>
								    <a href='<%=path%>/wx_ueditor.htm?method=findBYid&&netid=<%=bean.get("netid")%>&&pageI=<%=pageInfo.getPageIndex()%>&&pageS=<%=pageInfo.getPageSize() %>'><emp:message key='ydwx_common_btn_bianji' defVal='编辑' fileName='ydwx'></emp:message></a>
								<%  }
									else{
										out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_26",request));
										//out.print("<emp:message key='ydwx_common_btn_bianji' defVal='编辑' fileName='ydwx'></emp:message>");
								    }
								}else if(sta==1 || sta==2 || sta==4)
								{
									out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_26",request));
									//out.print("<emp:message key='ydwx_common_btn_bianji' defVal='编辑' fileName='ydwx'></emp:message>");
								}
								}
								%>
								</td>
									<%
										if(btnMap.get(menuCode+"-5")!=null){
										%>
								<td nowrap="nowrap">
									<a href="javascript:copy('<%=bean.get("netid")%>');"><emp:message key='ydwx_common_btn_fuzhi' defVal='复制' fileName='ydwx'></emp:message></a>
								</td>
										<%
										}
										%>
								<td nowrap="nowrap">
								<%
								if(btnMap.get(menuCode+"-2")!=null){
								if(sta==1 || sta==2 || sta == 4)
								{
									out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_27",request));
									//out.print("<emp:message key='common_delete' defVal='删除' fileName='common'></emp:message>");
								}else if(sta==0 || sta==3){
								%>
									<a href="javascript:del('<%=bean.get("netid")%>',1);"><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a>
								<%	
								}
								}
								%>
								</td>
								<%--
								<td nowrap="nowrap">
									<%
									if(sta==2)
									{
									%>
										<a href="javascript:checkwxTime('<%=bean.get("timetype") %>','<%=bean.get("netid") %>}');">发送</a>
									<%
									}else
									{
										out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_28",request));
									}
									%>
								</td>
								 --%>
								 	<%
										if(btnMap.get(menuCode+"-6")!=null){
										%>
								<td nowrap="nowrap">
								<%
								Integer type = Integer.valueOf(bean.get("wxtype").toString());
								if(type==1)
								{
									if(sta==2 || sta == 4){
								%>
										<a class="SetTemplate" href="javascript:setNetTemplate('<%=bean.get("netid") %>',1);"><emp:message key='ydwx_wxgl_szmb' defVal='设置模板' fileName='ydwx'></emp:message> </a>
								<% }else
									{
										out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_29",request));
										//out.print("<emp:message key='ydwx_wxgl_szmb' defVal='设置模板' fileName='ydwx'></emp:message>");
									}
								}else if(type==0){
								%>
								<a href="javascript:cancleTemplate('<%=bean.get("netid") %>',2,'<%=path%>');"><emp:message key='ydwx_wxgl_qxmb' defVal='取消模板' fileName='ydwx'></emp:message></a>
								<%}
								%>
								</td>
									<%
										}
									%>
							</tr>
					<%
					}
				}else{
					%>
						<tr>
						<td colspan="15">
							<emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message>
						</td>
						</tr>
					<%} %>
				</tbody>
				<tfoot>
				<tr>
				<td colspan="15">
					<div id="pageInfo"></div>
				</td>
				</tr>
				</tfoot>
			</table>
		</div>
		</form>
	</div>
	<div id="divBox" style="display:none" title="<emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message>">
	<div  align="center" class="ydwx_nav_dv">
         <emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
         <select class="ym ydwx_ym"></select>
	</div>
		<div class="ydwx_iframe_dv">
			<iframe class="ydwx_iframe" id="nm_preview_common" src=""></iframe>	
		</div>
	</div>   
	<%-- 设置模板弹出层 --%>
		<div id="pres" class='hidden' title="<emp:message key='ydwx_wxgl_szmb' defVal='设置模板' fileName='ydwx'></emp:message>">
			<form onsubmit="return setTemp()" method="post" id="SetWxTemp"
				name="SetWxTemp" enctype="multipart/form-data">
				<table class="ydwx_pres_tabl">
					<tr>
						<td>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key='ydwx_wxgl_xzmb' defVal='选择模板：' fileName='ydwx'></emp:message>
						</td>
						<td>
							 <select id="fType" class="ydwx_fType"></select>
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					<tr>
						<td nowrap="nowrap">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key='ydwx_wxgl_qscxt' defVal='请上传小图：' fileName='ydwx'></emp:message>
						</td>
						<td>
							<div class="file-box_1">
								<input type="hidden" name="netTempId" id="netTempId">
								<input type='text' name='textfield1' id='textfield1'
									class='txt_1' />
								&nbsp;&nbsp;
								<a href="###"> <input type='button' class='btn_1'
										value='<emp:message key='common_browser' defVal='浏览...' fileName='common'></emp:message>' />
								</a>
								<input type="file" name="fileField1" class="file_1"
									id="fileField1" size="28"
									onChange="document.getElementById('textfield1').value=this.value" />

							</div>
						</td>
					</tr>
					<tr><td>&nbsp;</td></tr>
					<%--
					<tr>
						<td nowrap="nowrap">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请上传大图：
						</td>
						<td>
							<div class="file-box_1">
								<input type='text' name='textfield2' id='textfield2'
									class='txt_1' />
								&nbsp;&nbsp;
								<a href="###"> <input type='button' class='btn_1'
										value='浏览...' />
								</a>
								<input type="file" name="fileField2" class="file_1"
									id="fileField2" size="28"
									onChange="document.getElementById('textfield2').value=this.value" />
							</div>
						</td>
					</tr>
					<tr><td>&nbsp;</td></tr>--%>
					<tr>
						<td class="ydwx_btns_td" colspan="2">
							<input type="submit" name="submit"
								id="submit" class="btnClass5 mr23 ywdx_submit" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>">
							<input type="button"  name="" class="btnClass6" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" onclick="closediv();">
						</td>
						<td>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div id="smsdetailinfo" title="<emp:message key='ydwx_wxgl_xxxx' defVal='详细信息' fileName='ydwx'></emp:message>" style="display:none;overflow: auto;">
			<div class="ydwx_recordTableDiv" id="recordTableDiv" align="center">
				<table width="95%" id="recordTable" class="ydwx_recordTable">
				</table>
			</div>
			<div class="ydwx_nextrecordmgs" id="nextrecordmgs" align="left">
			</div>
		</div>	
		<div id="reviewflowinfo" title="<emp:message key='ydwx_wxgl_EMPspzt_options3' defVal='待审批' fileName='ydwx'></emp:message>" style="display:none;overflow: auto;">
				<div class="ydwx_reviewTableDiv" id="reviewTableDiv" align="center">
						<table id="reviewTable" class="ydwx_reviewTable">
						</table>
				</div>
				<div class="ydwx_nextreviewmgs" id="nextreviewmgs" align="left">
				</div>
		</div>	
		<div id="modify" title="<emp:message key="ydwx_wxgl_wxmcs" defVal="网讯名称" fileName="ydwx"></emp:message>" class="ydwx_modify">
				<div id="msg" class="ydwx_msg"><xmp></xmp></div>
		</div>
	<div class="bottom">
		<div id="bottom_right">
			<div id="bottom_left"></div>
			<div id="bottom_main">
			</div>
		</div>
	</div>
</div>

<%-- foot结束 --%>
<div class="clear"></div>
 <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 <script type="text/javascript">
	$(function(){
	  $('#temptype,#rState,#operStatus').isSearchSelect({'width':'180','isInput':false,'zindex':0});
   })
	</script>
	</body>
</html>