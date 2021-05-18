<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	String lgcorpcode= (String)request.getAttribute("lgcorpcode");
	String state= (String)request.getAttribute("state");
	@ SuppressWarnings("unchecked")
	List<DynaBean> infos = (List<DynaBean>)request.getAttribute("infos");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	//网讯查询条件
	Object wx_name=request.getAttribute("wx_name");
	String langName = (String)session.getAttribute("emp_lang");
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link href="<%=commonPath %>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/dynamicTemplate.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/wx_send.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
		<%if("zh_HK".equals(empLangName)){%>
		<style type="text/css">
			#condition > table > tbody > tr > td:nth-child(1){
				width: 	100px;
			}
		</style>
		<%}%>
	</head>
	<body id="ydwx_dynamicTemplate">
		<div id="container" class="container">
		<input type="hidden" id="ipathUrl" value="<%=inheritPath %>"/>
			<%-- 内容开始 --%>
			<form name="pageForm" action="<%=path%>/wx_senddsm.htm?method=getTemplate&lguserid=<%=lguserid %>&lgcorpcode=<%=lgcorpcode %>&state=<%=state%>" method="post"
					id="pageForm">
					<input type="hidden" name="dsFlag" value="1"/>
					<div style="display:none" id="hiddenValueDiv"></div>
			<div id="rContent" class="rContent ydwx_rContent">
					<div id="condition">
						<table>
							<tr>
								<td>
									<span><emp:message key="ydwx_wxfs_dtwxfs_xzjtwy_td1" defVal="模板名称：" fileName="ydwx"></emp:message></span>
								</td>
								<td>
									<input type="text" name="tmName" 
										id="tmName" value="<%=wx_name==null?"":wx_name.toString() %>" class="ydwx_tmName"  maxlength="50"/>
								</td>

								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
					  <tr>
							<th>
								<emp:message key="ydwx_common_btn_xuanze" defVal="选择" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_dtwxfs_xzjtwy_td2" defVal="模板" fileName="ydwx"></emp:message> ID
							</th>
							<th>
								<emp:message key="ydwx_wxfs_dtwxfs_xzjtwy_td3" defVal="模板名称" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_dtwxfs_xzjtwy_td4" defVal="模板内容" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_dtwxfs_xzjtwy_td5" defVal="创建人名称" fileName="ydwx"></emp:message>
							</th>
							<th>
							    <emp:message key="ydwx_common_time_chuangjianshijian" defVal="创建日期" fileName="ydwx"></emp:message>
							</th>
										
									</tr>
								</thead>
								<tbody>
								<%
								if (infos != null && infos.size() > 0)
								{
									for (DynaBean info : infos)
									{
								%>
									<tr>
										<td>
											<input type="radio" name="checklist" id="checklist" value="<%=info.get("id") %>" tmMsg="<%=info.get("name")%>"/>
											<xmp style="display:none"><%=info.get("id")%></xmp>
										</td>
										<td>
											<%=info.get("netid") %>
										</td>
										<td class="textalign" >
										<% 
										String st1 = "";
										 if(!"".equals(info.get("name"))&&info.get("name")!=null){
												
													if(info.get("name").toString().length()>8)
													{
														st1 = info.get("name").toString().substring(0,8)+"...";
													}else
													{
														st1 = info.get("name").toString();
													}
											
										}
										%>
									<a onclick=javascript:modify(this,1)>
								  <label style="display:none"><xmp><%=info.get("name")%>
								  </xmp></label>
								  <xmp><%=st1%></xmp>
								  </a> 					
										</td>
										<td class="textalign" >
										<% if (info.get("name") != null)
									 {
									%>
									<a onclick="javascript:Look('<%=info.get("id") %>')"><emp:message key="ydwx_common_yulan" defVal="预览" fileName="ydwx"></emp:message></a>
									<%} %>
										</td>
										<td class="textalign" >
											<%if(info.get("username")!=null){
											out.print(info.get("username")+"("+info.get("usercode")+")");
										}%>
										</td>
										<td>
											<%
												if(info.get("creatdate")!=null)
												{
													out.print(df.format(info.get("creatdate")));
												}
											%>
										</td>
									</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="12"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="12">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div class="ydwx_btns">
						<input class="btnClass5 mr23 ydwx_borderradius" onclick="tempSure()" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>" type="button"/>
						<input class="btnClass6 ydwx_borderradius" onclick="tempCancel()" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" type="button"/>
						<br/>
					</div>
					
			</div>
			<div id="tempView" title="<emp:message key="ydwx_wxfs_dtwxfs_xzjtwy_td6" defVal="彩信内容" fileName="ydwx"></emp:message>" style="display:none">
		
			<input type="hidden" id="tmmsId" value=""/>
			<div class="ydwx_preview_dv">
				<div id="mobile" class="ydwx_mobile">
					<center>
						<div id="pers" class="ydwx_pers">
							<div id="showtime" class="ydwx_showtime"></div>
							<div id='chart' class="ydwx_chart">
							</div>
						</div>
					</center>
					<div id="screen" class="ydwx_screen">
					</div>
					<center>
					<table>
					<tr>
					  <td>
					     <label id="pointer" style="vertical-align: bottom"></label>
					     <label id="nextpage" style="vertical-align: bottom"></label>
					  </td>
					</tr>
					<tr align="center">
						<td>
						   <label id="currentpage" style="vertical-align: bottom"></label>
						</td>
					</tr>
						</table>				
					</center>
				</div>
			</div>
			<div id="inputParamCnt1" class="ydwx_inputParamCnt1">
			</div>			
		</div>	
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			</form>
			<%-- foot结束 --%>
		</div>
		<div id="modify" title="<emp:message key='ydwx_wxfs_dtwxfs_xzjtwy_td4' defVal='模板内容' fileName='ydwx'></emp:message>" class="ydwx_modify">
				<div id="msg" class="ydwx_msg"><xmp></xmp></div>
		</div>
		<div class="clear"></div>
		<div id="divBox" class="hideDlg" style="display:none" title="<emp:message key='ydwx_common_mubanyulan' defVal='模板预览' fileName='ydwx'></emp:message>">
			<div  align="center" class="ydwx_nav">
          	<emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
          	 <select class="ym ydwx_ym"></select>
        	</div>
			<div class="ydwx_iphone_dv">
				<iframe class="ydwx_preview_common1" id="nm_preview_common1" src=""></iframe>	
			</div>
		</div>
		<script src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		<script src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		<script src="<%=commonPath %>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=inheritPath%>/scripts/mmsPreview.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript">
		//加载页面
	$(document).ready(function() {
			$("#content select").empSelect({width:80});
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
			var X = $("#tempView").offset().left+60;
           var Y = $("#tempView").offset().top+150;
			$("#tempView").dialog({
					autoOpen: false,
					height:490,
					width: 250,
					position:[X,Y],
					modal: true,
					resizable:false,
					close:function(){
					    cplaytime = 0;
						nplaytime = -1;
						$("#screen").empty();
						clearInterval(ttimer); 
				}
			});
        //页码改变时  div层内容变化的方法
        $(".ym").change(function(){
            var id=$(this).val();
            for(var i=0;i<listpage.length;i++){
                if(id==listpage[i].id){
                    // 此处为显示错误页面，避免进入登录页面
                    if(listpage[i].content=="notexists"){
                        $("#nm_preview_common1").attr("src","ydwx/wap/404.jsp");
                    }else{
                        $("#nm_preview_common1").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
                    }
                }
            }
        });
		$("#divBox").dialog({
				autoOpen: false,
				height:550,
				width: 300,
				modal: true,
				close:function(){
				}
			});
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[10]);
			$('#search').click(function(){submitForm();});
		});
		
		//显示列表状态下，模板名字详细信息
		function modify(t,i)
		{
			$('#modify').dialog({
				autoOpen: false,
				resizable: false,
				width:250,
			    height:200
			});
			$("#msg").children("xmp").empty();
			$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
			if(i==1)
			{
				$('#modify').dialog('option','title',getJsLocaleMessage("ydwx","ydwx_dtwxfs_93"));
			}
			else
			{
				$('#modify').dialog('option','title',getJsLocaleMessage("ydwx","ydwx_dtwxfs_94"));
			}
			$('#modify').dialog('open');
		}
	
	//查看
	function Look(netId){	
	    $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
	       data=eval("("+data+")");
	       listpage=data;
	       $(".ym").children().remove();
	       for(var i=0;i<listpage.length;i++)	{   
	           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
	      }
            // 此处为显示错误页面，避免进入登录页面
            if(listpage[0].content=="notexists"){
                $("#nm_preview_common1").attr("src","ydwx/wap/404.jsp");
            }else{
                $("#nm_preview_common1").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
            }
	      $("#divBox").dialog("open");
	     // changeSel();
       });
    }
    
		//预览		
		function doPreview(msg)
		{
			inits();
			$.post("<%=path%>/t_mmsExamine.htm?method=getTmMsg",{tmUrl:msg},function(result){
				if (result != null && result != "null" && result != "")
				{
					arr = result.split(">");
					if(arr[0] != null && arr[0] != "")
					{
						var da = $.parseJSON(arr[0]);
						ttime = (da.totaltime/1000);
					}
					index = 1;
					$("#screen").empty();
					$("#pointer").empty();
		        	$("#screen").html("<center style='padding-top:80px'><img src='<%=inheritPath%>/images/play.png' style='cursor:pointer;width:48;height:48' title='播放' onclick='javascript:setScreen();setPer();'/></center>");
					$("#tempView").dialog("open");
				}
				else
				{
		             alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_95"));
				}
			});
		}
	//点击取消
	function tempCancel() {
		window.parent.$("#tempDiv").dialog("close");
	}
	
	// 点击选择模板事件
	function tempSure()
	{
		var $ro = $("input[type='radio']:checked");
		if($ro.val() == undefined)
		{
			alert(getJsLocaleMessage("ydwx","ydwx_dtwxfs_96"));
			return;
		}else
		{
		var ipathUrl = "<%=inheritPath %>";
		var message = $ro.next("xmp").text();
		$(window.parent.document).find("#tempupfilediv").empty();
		var tmname = $ro.next("xmp").next("xmp").text();
		<%--选择的模板ID  模板URL--%>
		$(window.parent.document).find("#tempDiv").attr("disabled",true);
		var id=$ro.val();
		var name=$ro.attr("tmMsg");
		window.parent.choiceTmp(id,name);

		}
	}
		</script>
	</body>
</html>
