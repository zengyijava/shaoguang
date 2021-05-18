<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.netnews.entity.LfWXBASEINFO"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo  = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<DynaBean> infos = (List<DynaBean>)request.getAttribute("baseInfos");
	LfWXBASEINFO baseinfo = (LfWXBASEINFO)request.getAttribute("baseinfo");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	//区分是静态还是动态
	String tmpltype = request.getParameter("tmpltype");
	String langName = (String)session.getAttribute("emp_lang");
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=commonPath%>/ydwx/sendWX/css/wx_netTemplate.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin%>/wx_send.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	</head>
	<body class="wx_netTemplate" id="ydwx_netTemplate">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form name="pageForm" action="wx_send.htm?method=getNetTemplate&lguserid=<%=lguserid %>" method="post"
					id="pageForm">
					<div id="hiddenValueDiv"></div>
					<input type="hidden" id="ipathUrl" value="<%=iPath %>"/>
					<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
					<input type="hidden" id="skinType" value="<%=skin %>"/>
					<input id="pathUrl" value="<%=path%>" type="hidden" />
					<input type="hidden" id="tmpltype" name="tmpltype" value="<%=tmpltype %>"/>
					
			<div id="rContent" class="rContent">
					<div id="condition">
						<table>
							<tr>
								<td width="<%="zh_HK".equals(empLangName)?"100px":"60px"%>">
									<span><emp:message key="ydwx_wxfs_jtwxfs_dxnr_xzjtwx_td2" defVal="网讯名称：" fileName="ydwx"></emp:message></span>
								</td>
								<td>
									<input type="text" name="name" 
										id="name" value="<%=null!=baseinfo.getNAME()?baseinfo.getNAME():"" %>" maxlength="50"/>
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
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr_xzjtwx_td3" defVal="选择" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr_xzjtwx_td4" defVal="网讯名称" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr_xzjtwx_td5" defVal="网讯内容" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr_xzjtwx_td6" defVal="创建人名称" fileName="ydwx"></emp:message>
							</th>
							<th>
							    <emp:message key="ydwx_common_time_chuangjianshijian" defVal="创建时间" fileName="ydwx"></emp:message> 
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
											<input type="radio" name="checklist" id="checklist" value="<%=info.get("id") %>" 
											netid="<%=info.get("netid") %>" netName="<%=info.get("name") %>"/>
										</td>
										<td><% 
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
											<a onclick=modify(this)>
								  <label style="display:none"><xmp><%=info.get("name")%></xmp></label>
								  <xmp><%=st1 %></xmp>
								  </a>
										</td>
										<td>
											<a onclick="Look(<%=info.get("netid") %>)">
												<emp:message key="ydwx_common_yulan" defVal="预览" fileName="ydwx"></emp:message>
											</a> 		
										</td>
										<td>
										<%if(info.get("username")!=null){
											out.print(info.get("username")+"("+info.get("usercode")+")");
										}%>
										</td>
										<td>
											<%=info.get("creatdate")!=null?df.format(info.get("creatdate")):"" %>
										</td>
									</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="5"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="5">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					
					<div class="foot-btn">
						<input class="btnClass5 mr23 ydwx_borderradius" onclick="tempSure()" value="<emp:message key='ydwx_common_btn_xuanze' defVal='选择' fileName='ydwx'></emp:message>" type="button"/>
						<input class="btnClass6 ydwx_borderradius" onclick="tempCancel()" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" type="button"/>
						<br>
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
		<div id="divBox" class="hideDlg" title="<emp:message key="ydwx_common_mubanyulan" defVal="模板预览" fileName="ydwx"></emp:message>">
			<div  class="pageNavigation" align="center">
          		<emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
          	 	<select class="ym"></select>
        	</div>
			<div class="backgroundPhone">
				<iframe id="nm_preview_common1" src=""></iframe>
			</div>
		</div>
				<div id="modify" title="<emp:message key="ydwx_wxfs_jtwxfs_dxnr_xzjtwx_td4" defVal="网讯名称" fileName="ydwx"></emp:message>">
				<div id="msg"><xmp></xmp></div>
		</div>
		
		<div class="clear"></div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
		<script type="text/javascript">
		//页面加载，初始化相关数据
		$(document).ready(function() {
			$("#content select").empSelect({width:80});
			$("#tempView").dialog({
				autoOpen: false,
				height:510,
				width: 290,
				resizable:false,
				close:function(){
				    cplaytime = 0;
					nplaytime = -1;
					$("#screen").empty();
					clearInterval(ttimer); 
				}
			});
			$("#divBox").dialog({
					autoOpen: false,
					height:535,
					width: 300,
					modal: true,
					close:function(){
					}
				});
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
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[5]);
			$('#search').click(function(){submitForm();});
		});
		/*彩信模板确认*/
		function tempSure()
		{
			var tem = $("input[type='radio']:checked").val();
			var $ro = $("input[type='radio']:checked");
			var commonPath = $("#commonPath").val();
			var skinType = $("#skinType").val();
			if(tem == undefined || tem == "" || tem == null)
			{
				alert(getJsLocaleMessage("ydwx","ydwx_jtwxfs_88"));
				return;
			}else
			{
				var netid = $ro.attr("netid");
				var tmname  =$ro.attr("netName");
				/*选择的模板ID  模板URL*/
				$(window.parent.document).find("#netid").val(netid);
				/*选择的网讯模板模板*/
				$(window.parent.document).find("#teplortms").val("1");
	         var ss = "<div style='float:left;' class='div_bg'>"
					+"<label><img border='0' src='<%=commonPath %>/common/img/fileimg.png'></img>&nbsp;"+tmname+"</label>"
					+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font onclick='Look("+netid+")' class='pre-btn'>"+getJsLocaleMessage("ydwx","ydwx_jtwxfs_89")+"</font>&nbsp;&nbsp;"
					+"<font onclick='tempNo()' class='pre-btn'>"+getJsLocaleMessage("ydwx","ydwx_common_shanchu")+"</font></div>";
				$(window.parent.document).find("#templatepre").empty();
				$(window.parent.document).find("#templatepre").html(ss);
				$(window.parent.document).find("#templatepre").show();


			}
			parent.closeDialog();
		}

		//关闭窗口
		function tempCancel(){
			parent.closeDialog();
		}
		// 点击查看网讯
		function Look(netId){
		    //$("#netid").val(netId);
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
	       });
	    }

		//显示模板名称详细信息
		function modify(t) {
			$('#modify').dialog({
				autoOpen: false,
				resizable: false,
				width:250,
			    height:200
			});
			$("#msg").children("xmp").empty();
			$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
			$('#modify').dialog('open');
		}
		</script>
	</body>
</html>
