<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.sql.Timestamp"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.entity.approveflow.LfFlowRecord"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.montnets.emp.netnews.entity.LfWXBASEINFO"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	@SuppressWarnings("unchecked")
	List<LfFlowRecord> recordList= (List<LfFlowRecord>)request.getAttribute("recordList");
	LfWXBASEINFO temp = (LfWXBASEINFO)request.getAttribute("temp");
	@SuppressWarnings("unchecked")
	LinkedHashMap<Long,String> usernameMap=(LinkedHashMap<Long,String>)request.getAttribute("usernameMap");
	//当前审核信息
	LfFlowRecord curRecord = (LfFlowRecord)request.getAttribute("curRecord");
	
	boolean isLastCheck = true;
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
	String nowtime = df.format(new Date());
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String httpUrl = StaticValue.getFileServerViewurl();
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	String langName = (String)session.getAttribute("emp_lang");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head><%@include file="/common/common.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
	<style>
		.bb_none{border-bottom:0;}
		td.div_bd.div_bg{
			text-align: right;
			width: 100px;
		}
		textarea#content{  height: 100%; width: 420px; font-size: 12px;  }
		a#imga{text-decoration: none;margin-left:427px}
	</style>
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<style type="text/css">
		input#oks,input#rjs,input.btnClass6{
			letter-spacing: 0;
			text-indent: 0;
			text-align: center;
		}
		td.div_bd.div_bg{
			text-align: left;
			width: 120px;
			padding-left: 5px;
		}
		textarea#content{  height: 100%; width: 392px; font-size: 12px;  }
		a#imga{  text-decoration: none; margin-left:350px;  }
	</style>
	<%}%>
	</head>
	<body>
	<div id="container" class="container">
	<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
		<%-- header开始 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<div id="rContent" class="rContent">
		<div class="titletop" style="display: block">
			<table class="titletop_table" style="width:528px">
				<tr>
					<td class="titletop_td">
						<emp:message key="ydwx_wxshh_add_1" defVal="模板审批" fileName="ydwx"></emp:message>
					</td>
					<td align="right">
						<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;
						<emp:message key="ydwx_add_jsp_3" defVal="返回上一级" fileName="ydwx"></emp:message>
						</font>
					</td>
				</tr>
			</table>
		</div>
			  <form method="post" action="" name="form2">
			  <div  id="loginparams" style="display:none"></div>
					<input type="hidden" id="pathUrl" value="<%=path %>">
					<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
					<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
					<%-- 预发送条数--%>
					<div  id="PconfigDiv" style="padding-top:0px;">
						<div id="tabContainer1" onclick="changflod('a',this)"  style="width:520px;font-size: 13px;padding-left:0;text-indent:10px;margin-left:6px;font-weight: normal;" class="div_bg div_bd bb_none">	
	         			    <label><emp:message key="ydwx_wxshh_add_2" defVal="模板信息" fileName="ydwx"></emp:message></label>
	         			    <a id="imga" class="fold">&nbsp;&nbsp;&nbsp;&nbsp;</a>
						</div>
	        		</div>
					<div class="itemDiv div_bd" style="width:520px;margin-left: 22px;" id="msgdiva">
						<table style="width:100%" >
					   		<tr>
								<td   style=" height:22px;border-top: 0px;border-left: 0px;" class="div_bd div_bg">
									<emp:message key="ydwx_wxshh_add_3" defVal="提交人：" fileName="ydwx"></emp:message>
								</td>
								<td align="left" style="height:22px;border-top: 0px;border-left: 0px;border-right: 0px;padding-left: 20px;" class="div_bd">
									<%=usernameMap.get(temp.getCREATID()) %>
								</td>
							</tr>
							<tr>
								<td   style=" height:22px;border-top: 0px;border-left: 0px;" class="div_bd div_bg">
									<emp:message key="ydwx_wxshh_add_4" defVal="模板名称：" fileName="ydwx"></emp:message>
								</td>
								<td align="left" style="height:22px;border-top: 0px;border-left: 0px;border-right: 0px;padding-left: 20px;" class="div_bd">
									<%=temp.getNAME() %>
								</td>
							</tr>
							<tr>
								<td   style=" height:22px;border-top: 0px;border-left: 0px;" class="div_bd div_bg">
									<emp:message key="ydwx_common_time_chuangjianshijians" defVal="创建时间：" fileName="ydwx"></emp:message>
								</td>
								<td align="left" style="height:22px;border-top: 0px;border-left: 0px;border-right: 0px;padding-left: 20px;" class="div_bd">
									<%=df.format(temp.getCREATDATE()) %>
								</td>
							</tr>
								<tr>
									<td   style=" height:22px;border-width:0 1px 0 0;" class="div_bd div_bg">
										<emp:message key="ydwx_wxshh_add_5" defVal="模板内容：" fileName="ydwx"></emp:message>
									</td>
									<td>
									<a href="javascript:Look(<%=temp.getNETID() %>)" style="margin-left:20px;"><emp:message key="ydwx_wxshh_add_6" defVal="点击查看" fileName="ydwx"></emp:message></a>
									</td>
								</tr>
						</table>	
					</div>	
					
					<% 
						Integer level = curRecord.getRLevel();
						if(recordList != null && recordList.size()>0)
						{
							List<LfFlowRecord> records = new ArrayList<LfFlowRecord>();
							for(int i=1;i<=level;i++)
							{
								records.clear();
								for(int k=0;k<recordList.size();k++)
								{
									LfFlowRecord recordk = recordList.get(k);
									 if(recordk.getRLevel() == i ){
										 records.add(recordk);
									 }
								}
								if(records.size() == 0)
								{
									continue;
								}
								%>
								<div  id="PconfigDiv" >
									<div id="tabContainer1" onclick="changflod(<%=i%>,this)"  class="div_bg div_bd bb_none"
									style="width:520px;font-size: 13px;padding-left:0;text-indent:10px;margin-left:6px;font-weight: normal;"  >	
				         			    <label><emp:message key="ydwx_wxshh_add_7" defVal="第" fileName="ydwx"></emp:message><%=i%><emp:message key="ydwx_wxshh_add_8" defVal="级审批" fileName="ydwx"></emp:message></label>
				         			    <a id="img<%=i%>" class="fold" style="text-decoration: none;margin-left:<%="zh_HK".equals(empLangName)?367:420%>px">&nbsp;&nbsp;&nbsp;&nbsp;</a>
									</div>
				        		</div>
				        		<div class="itemDiv div_bd" style="width:520px;margin-left: 22px;" id="msgdiv<%=i%>">
									<table style="width:100%" >
										<% 
											for(int j=0;j<records.size();j++){
												LfFlowRecord record = records.get(j);
										 %>
											 	<tr>
													<td   style="height:22px;border-top: 0px;border-left: 0px;" class="div_bd div_bg">
														<emp:message key="ydwx_wxshh_add_9" defVal="审批人：" fileName="ydwx"></emp:message>
													</td>
													<td align="left" style="height:22px;border-top: 0px;border-left: 0px;border-right: 0px;padding-left: 20px;width:100px;" class="div_bd">
														<%
															String approverName = usernameMap.get(record.getUserCode());
															if(approverName != null) {
																if("无效用户".equals(approverName)){
																	approverName = "zh_HK".equals(empLangName)?"Invalid user":"zh_TW".equals(empLangName)?"無效用戶":"无效用户";
																}
															} else{
																approverName = "-";
														}%>
														<%=approverName%>
													</td>
													<td   style=" height:22px;border-top: 0px;" class="div_bd div_bg">
														<emp:message key="ydwx_wxshh_add_10" defVal="审批时间：" fileName="ydwx"></emp:message>
													</td>
													<td align="left" style="height:22px;border-top: 0px;border-left: 0px;border-right: 0px;padding-left: 20px;" class="div_bd">
														<%=df.format(record.getRTime())%>
													</td>
												</tr>
												<tr>
													<td   style="width:97px; height:22px;border-width:0 1px 0 0;" class="div_bd div_bg">
														<emp:message key="ydwx_wxshh_add_11" defVal="审批意见：" fileName="ydwx"></emp:message>
													</td>
													<td align="left" style="width:150px;height:22px;border-top: 0px;word-break: break-all;border-width:0 1px 0 0;padding-left: 20px;" class="div_bd" colspan="3">
														<%=record.getComments()==null?"":record.getComments().replaceAll("<","&lt;").replaceAll(">","&gt;")%>
													</td>
												</tr>
											<% 
											}
										 %>
									</table>	
									
								</div>
							 <% 	
							}
						}
					%>
						
					<%if(curRecord.getIsComplete() == 2 && curRecord.getRState() == -1) {%>
					
					<div class="itemDiv div_bd" style="width:520px;margin-left: 22px;margin-top:10px;">
						<table style="width:100%">
					   		<tr>
					   			<td  style="height:22px;border-top: 0;border-left: 0;" class="div_bd div_bg">
									<emp:message key="ydwx_wxshh_add_12" defVal="审批级别：" fileName="ydwx"></emp:message>
								</td>
								<td align="left" style="border-top: 0;border-left: 0;border-right: 0;padding-left: 20px;" class="div_bd" >
									<%=curRecord.getRLevel() %>/<%=curRecord.getRLevelAmount() %>
								</td>
					   		</tr>
							<tr>
								<td  style="height:22px;border-width:0 1px 0 0;" class="div_bd div_bg">
									<emp:message key="ydwx_wxshh_add_11" defVal="审批意见：" fileName="ydwx"></emp:message>
								</td>
								<td align="left" style="height:50px;border-width:0 1px 0 0;" class="div_bd" >
									<textarea id="content" name="content"></textarea>
								</td>
							</tr>
							
						</table>	
					</div>
					
					<div  class="itemDiv" style="width:545px;text-align: right;padding-top:15px;">	 
						<input type="button" value=" <emp:message key="ydwx_wxshh_add_13" defVal="同意" fileName="ydwx"></emp:message> " id="oks" 
						onclick="javascript:review(<%=curRecord.getFrId() %>,1);" class="btnClass5 mr23"/>
					
						<input type="button" value=" <emp:message key="ydwx_wxshh_add_14" defVal="拒绝" fileName="ydwx"></emp:message> " id="rjs"
						onclick="javascript:review(<%=curRecord.getFrId() %>,2);" class="btnClass6 mr23"/>
						
						<input type="button" value=" <emp:message key='ydwx_common_btn_fanhui' defVal='返回' fileName='ydwx'></emp:message>" 
						onclick="javascript:back()" class="btnClass6"/>
					</div>	
					
					
					<%}else{ %>
					<div  class="itemDiv" style="width:545px;text-align: right;padding-top:15px;">
						          <input type="button" value=" <emp:message key='ydwx_common_btn_fanhui' defVal='返回' fileName='ydwx'></emp:message>" onclick="javascript:back()" class="btnClass6"/>
				    </div>
					<% }%>
			 </form>
		 </div>
	</div> 
	<div id="divBox" style="display:none" title="<emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message>">
		<div  align="center" style='margin-right:10px;margin-top: 3px;'>
         	<emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
         	 <select class="ym" style="width:170px;" ></select>
       	</div>
		<div style="width:240px;height:460px;margin-left:30px; margin-top:-3px; background:url(<%=commonPath %>/common/img/iphone5.jpg);">
			<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="nm_preview_common" src=""></iframe>	
		</div>
	</div> 
  <%-- 加载JS --%>
  <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  <script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
  <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
  <script type="text/javascript">
	<%--显示隐藏信息--%>
	function changflod(num,obj){
		if($("#img"+num).hasClass("unfold")){
			 $("#msgdiv"+num).show();
			 $(obj).addClass('bb_none');
			 $("#img"+num).removeClass("unfold");
			 $("#img"+num).addClass("fold");
		}else if($("#img"+num).hasClass("fold")){
			 $("#msgdiv"+num).hide();
		     $("#img"+num).removeClass("fold");
			 $("#img"+num).addClass("unfold");
			 $(obj).removeClass('bb_none');
		}
	}

	function review(frId,state)
	{
	    $("#ok3").attr("disabled","disabled");
	    $("#rj3").attr("disabled","disabled");
	    var cont = $("#content").val();
	    if(cont.length>400){
				alert(getJsLocaleMessage("ydwx","ydwx_wxshh_1"));
				$("#ok3").attr("disabled","");
	            $("#rj3").attr("disabled","");
				return;
		}
		        if(cont.indexOf("'")!=-1  || outSpecialChar(cont)  ){
             	alert(getJsLocaleMessage("ydwx","ydwx_wxshh_2"));
             	return;
             }
	    $.post("<%=path%>/wx_check.htm?method=review",{frId: frId,rState:state,cont:cont},function(result){
	           if(result != null && result == "true") {
	              alert(getJsLocaleMessage("ydwx","ydwx_wxshh_3"));
	              $("#ok3").attr("disabled","");
	              $("#rj3").attr("disabled","");
	              back();
	           } else {
	              alert(getJsLocaleMessage("ydwx","ydwx_wxshh_4"));
	              $("#ok3").attr("disabled","");
	              $("#rj3").attr("disabled","");
	           }
	    });
	}
	function back(){
		 var url = "<%=path %>/wx_check.htm?method=find";
			var conditionUrl = "";
			if(url.indexOf("?")>-1){
				conditionUrl="&";
			}else{
				conditionUrl="?";
			}
			conditionUrl = conditionUrl +backfind("#loginparams");
         location.href=url+conditionUrl+"&skip=true";
	}
	$(document).ready(function() {
		getLoginInfo('#loginparams');
		 $("#divBox").dialog({
				autoOpen: false,
				height:510,
				width: 300,
				modal: true,
				close:function(){
				}
			});

		//页码改变时  div层内容变化的方法
        $(".ym").change(function(){
            var id=$(this).val();
            for(var i=0;i<listpage.length;i++){
                if(id==listpage[i].id){
                    // 此处为显示错误页面，避免进入登录页面
                    if(listpage[i].content=="notexists"){
                        $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
                    }else{
                        $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
                    }
                }
            }
        });
		
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
	      $("#divBox").dialog("open");
       });
    }	
</script>	
</body>
</html>

