<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import="com.montnets.emp.common.vo.SendedMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.sms.LfMttask" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
			
	@ SuppressWarnings("unchecked")
	List<SendedMttaskVo> mtList =(List<SendedMttaskVo>) request.getAttribute("mtList");
	
	LfMttask Lfmttask = (LfMttask)session.getAttribute("wxMttask");
	if(Lfmttask == null)
	{
		Lfmttask = new LfMttask();
	}
	String title = Lfmttask.getTitle();
	String message = Lfmttask.getMsg();
	if(Lfmttask.getBmtType() == null || Lfmttask.getBmtType() == 3)
	{
		message = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_1",request);
	}
	String sendtime = Lfmttask.getTimerTime()==null?"":sdf_yyyyMMddHHmmss.format(Lfmttask.getTimerTime());
	String sendinfo;
	if(Lfmttask.getIcount2() == null)
	{
		sendinfo=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_2",request);
	}
	else
	{
		//接收失败总数
		String r_count=String.valueOf((Lfmttask.getRfail2()==null?"0":Lfmttask.getRfail2()));
		//提交失败总数
		long fail_count = Lfmttask.getFaiCount()==null?0l:Long.parseLong(Lfmttask.getFaiCount());
		long icount = Lfmttask.getIcount2()==null?0l:Long.parseLong(Lfmttask.getIcount2());
		//发送成功数
		long suc = icount - fail_count;
		/*sendinfo ="发送总数为"+icount+"条，其中发送成功数为"+suc+"条，提交失败数为"+fail_count+"条，接收失败数为"+r_count+"条。";*/
		sendinfo =MessageUtils.extractMessage("ydwx","ydwx_jsp_out_3",request)+icount+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_4",request)+suc
				+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_5",request)+fail_count+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_6",request)+r_count+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_7",request);
	}
	String mtid = request.getParameter("mtid");
	String type= request.getParameter("type");
	String phone = request.getParameter("phone");
	String spisuncm = request.getParameter("spisuncm");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

	String menuCode = titleMap.get("wmsTaskRecord");
	menuCode=menuCode==null?"0-0-0":menuCode;
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	boolean isFirstEnter = (Boolean)request.getAttribute("isFirstEnter");
	String loadPageUrl = path+"/wx_wmsTaskRecord.htm?method=getDetailPageInfo";
	String langName = (String)session.getAttribute("emp_lang");
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
	</head>
	<body>
				<div id="container">
					<div class="top">
						<div id="top_right">
							<div id="top_left"></div>
							<div id="top_main">
								<%--[网讯查询统计]-[网讯发送统计]--%>
								<%--[<%=titleMap.get(menuCode.substring(0,menuCode.indexOf("-"))) %>]-[<%=titleMap.get(menuCode) %>]--%>
								<strong><emp:message key="ydwx_add_jsp_1" defVal="当前位置：" fileName="ydwx"></emp:message></strong>[<emp:message key="ydwx_common_menuTitle_1" defVal="网讯查询统计" fileName="ydwx"></emp:message>]-[<emp:message key="ydwx_common_menuTitle_2" defVal="网讯发送统计" fileName="ydwx"></emp:message>]
						  	</div>
						</div>
						<div class="abs_right">
							<span class="titletop_font"  onclick="javascript:showback()">&larr;&nbsp;
							<emp:message key="ydwx_add_jsp_3" defVal="返回上一级" fileName="ydwx"></emp:message></span>
						</div>
					</div>
					<div class="rContent">
						<%              		
							if(btnMap.get(menuCode+"-0")!=null)                       		
							{                        	
						%>
						<div class="titletop" style="padding-left: 0;padding-top: 0">
						<table class="titletop_table" style="width: 100%">
								<tr>
								    <td class="titletop_td">
								           <emp:message key="ydwx_add_jsp_2" defVal="发送详情查看" fileName="ydwx"></emp:message>     
						        	</td>
									<td align="right">
										<span class="titletop_font"   onclick="javascript:showback()">&larr;&nbsp;
										<emp:message key="ydwx_add_jsp_3" defVal="返回上一级" fileName="ydwx"></emp:message></span>
									</td>
								</tr>
							</table>
					   </div>
						<div id="u_o_c_explain" class="div_bg">
							<p>
								<emp:message key="ydwx_add_jsp_4" defVal="基本信息" fileName="ydwx"></emp:message>
							</p>
                            <table>
                              <tr>
                                 <td><b><emp:message key="ydwx_add_jsp_5" defVal="主题：" fileName="ydwx"></emp:message></b></td>
                              </tr>
                              <tr>
                                 <td><%=title==null?"":(title.replaceAll("<","&lt;").replaceAll(">","&gt;")) %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="ydwx_add_jsp_6" defVal="内容：" fileName="ydwx"></emp:message></b></td>
                              </tr>
                              <tr>
                                 <td style="word-wrap: break-word;word-break:break-all;"><%=message==null||"".equals(message)?"":message.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("#[pP]_(\\d+)#",MessageUtils.extractMessage("ydwx","ydwx_jsp_out_32",request)) %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="ydwx_common_time_fasongshijians" defVal="发送时间：" fileName="ydwx"></emp:message></b></td>
                              </tr>
                              <tr>
                                 <td><%=sendtime %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="ydwx_add_jsp_7" defVal="发送情况：" fileName="ydwx"></emp:message></b></td>
                              </tr>
                              <tr>
                                <td><%=sendinfo %>
                                <br/>
                                <font class="zhu"><emp:message key="ydwx_add_jsp_8" defVal="注：以上数据" fileName="ydwx"></emp:message>
                                <%=StaticValue.getCORPTYPE() ==0?MessageUtils.extractMessage("ydwx","ydwx_jsp_out_8",request):MessageUtils.extractMessage("ydwx","ydwx_jsp_out_9",request)%>
                                <emp:message key="ydwx_add_jsp_9" defVal="汇总一次,可能与下面的实时数据有一定误差。" fileName="ydwx"></emp:message></font>
                                </td>
                              </tr>
                            </table>
						</div>
						<form name="pageForm" action="wx_wmsTaskRecord.htm?method=findAllSendInfo" method="post">
						<div class="buttons" style="padding-top: 10px;">
						  <div id="toggleDiv">								
						  </div> 
						  <a id="exportCondition"><emp:message key='ydwx_common_btn_daochu' defVal='导出' fileName='ydwx'></emp:message></a>	
						  <span id="backgo" class="right mr5" onclick="javascript:showback()"><emp:message key='ydwx_common_btn_fanhui' defVal='返回' fileName='ydwx'></emp:message></span>			  					 			          
						</div>
						<div id="condition">
						 <input type="hidden" id="mtid" value="<%=mtid %>" name="mtid">
						 <table>
						    <tr>
						      <td><emp:message key='ydwx_add_jsp_10' defVal='运营商：' fileName='ydwx'></emp:message></td>
						      <td>
						           <select style="width:180px;"  id="spisuncm" name="spisuncm">
										<option value=""><emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message></option>
										<option value="0" <%="0".equals(spisuncm)?"selected":"" %>><emp:message key='ydwx_common_yidong' defVal='移动' fileName='ydwx'></emp:message></option>
										<option value="1" <%="1".equals(spisuncm)?"selected":"" %> ><emp:message key='ydwx_common_liantong' defVal='联通' fileName='ydwx'></emp:message></option>
										<option value="21" <%="21".equals(spisuncm)?"selected":"" %>><emp:message key='ydwx_common_dianxin' defVal='电信' fileName='ydwx'></emp:message></option>
										<option value="5" <%="5".equals(spisuncm)?"selected":"" %>><emp:message key='ydwx_common_guowai' defVal='国外' fileName='ydwx'></emp:message></option>
									</select>
						      </td>
						      <td><emp:message key='ydwx_add_jsp_11' defVal='手机号码：' fileName='ydwx'></emp:message></td>
						      <td>
						         <input type="text" style="width: 177px;" value='<%=phone==null?"":phone %>' id="phone" name="phone" maxlength="11" onkeyup="javascript:numberControl($(this))"/>
						      </td>
						      <td><emp:message key='ydwx_add_jsp_12' defVal='状态：' fileName='ydwx'></emp:message></td>
						      <td>
						          <select style="width:180px;"  id="type" name="type">
										<option value="1" <%="1".equals(type)?"selected":"" %>><emp:message key='ydwx_add_jsp_13' defVal='全部号码' fileName='ydwx'></emp:message></option>
										<%--
										<option value="5" <%="5".equals(type)?"selected":"" %> >发送成功</option>
										<option value="6" <%="6".equals(type)?"selected":"" %> >状态未返</option>										
										--%>
										<option value="2" <%="2".equals(type)?"selected":"" %> ><emp:message key='ydwx_add_jsp_14' defVal='接收失败' fileName='ydwx'></emp:message></option>
										<option value="3" <%="3".equals(type)?"selected":"" %>><emp:message key='ydwx_add_jsp_15' defVal='提交失败' fileName='ydwx'></emp:message></option>
								  </select>
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
										<emp:message key='ydwx_add_jsp_16' defVal='序号' fileName='ydwx'></emp:message>
									</th>
									<th>
									    <emp:message key='ydwx_add_jsp_17' defVal='运营商' fileName='ydwx'></emp:message> 
									</th>
									<th>
										<emp:message key='ydwx_add_jsp_18' defVal='手机号' fileName='ydwx'></emp:message>
									</th>
									<th>
										<emp:message key='ydwx_add_jsp_19' defVal='内容' fileName='ydwx'></emp:message>
									</th>
									<th>
										<emp:message key='ydwx_add_jsp_20' defVal='分条' fileName='ydwx'></emp:message>
									</th>
									<th>
										<emp:message key='ydwx_add_jsp_21' defVal='状态' fileName='ydwx'></emp:message>
									</th>
								</tr>
							</thead>
							<%
							if(type.equals("0"))
							{
							%>
							<tr><td colspan="6" align="center">
							<emp:message key='ydwx_common_qdjcxhqsj' defVal='请点击按钮获取数据' fileName='ydwx'></emp:message></td></tr>
							<%
							}else if(mtList!= null && mtList.size() > 0)
								{
								for(int i=0;i<mtList.size();i++)
								{
									SendedMttaskVo vo = mtList.get(i);
							%>
							<tr>
								<td>
									<%=i+1 %>
								</td> 
								<td>
				  					<%
									if(vo.getUnicom()-0==0){%>
									<emp:message key='ydwx_common_yidong' defVal='移动' fileName='ydwx'></emp:message>
									<%}else if(vo.getUnicom()-1==0){%>
									<emp:message key='ydwx_common_liantong' defVal='联通' fileName='ydwx'></emp:message>
									<%}else if(vo.getUnicom()-21==0){ %>
									<emp:message key='ydwx_common_dianxin' defVal='电信' fileName='ydwx'></emp:message>
									<%}else if(vo.getUnicom()-5==0){ %>
									<emp:message key='ydwx_common_guowai' defVal='国外' fileName='ydwx'></emp:message>
									<%} %>
								</td>
								<td>
									<%
									   String phones = CV.replacePhoneNumber(btnMap,vo.getPhone());
									   out.print(phones);									   
									 %>
								</td>
								<td class="textalign">									
									<%=vo.getMessage()==null||"".equals(vo.getMessage())?"":vo.getMessage().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
								</td>
								<td>
									<%=vo.getPknumber() %>/<%=vo.getPktotal() %>
								</td>
								<td class="textalign">
								<%if(vo.getErrorcode() != null){ %>
								    <%if(vo.getErrorcode().equals("DELIVRD")||vo.getErrorcode().trim().equals("0")){  %>
								         <label><emp:message key='ydwx_add_jsp_23' defVal='发送成功' fileName='ydwx'></emp:message></label>  
								    <%}else if(vo.getErrorcode().toString().trim().length()>0){%>
								         <label><emp:message key='ydwx_add_jsp_24' defVal='失败' fileName='ydwx'></emp:message>[<%=vo.getErrorcode() %>]</label>
								    <%}else { %>
								         <label>-</label>
								    <% }%>
								<%}else { %>
								    <label>-</label>
								<%} %>
								</td>

							</tr>
							<%
								}
								}
								else
								{
							%>
							<tr><td colspan="6" align="center"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
							<%} %>
							<tfoot>
							<tr >
								<td colspan="6">
								<div id="pageInfo"></div>
								</td>
							</tr>
							</tfoot>
						</table>
						<div id="smssendparams" class="hidden"></div>
						</form>
						<div class="clear"></div>
						<%
								}
						%>
						
					 <div id="probar" style="display: none;text-align: center;">
							<p >
								<emp:message key="ydwx_add_jsp_22" defVal="请耐心等待....." fileName="ydwx"></emp:message>
							</p>
							<div id="shows">
								<img src="<%=commonPath%>/common/img/loader.gif" />
							</div>							
					</div>					
					</div>
			<%--end rContent--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydwx_<%=empLangName%>.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		
		<script type="text/javascript">
		$(document).ready(function() {
		    getLoginInfo("#smssendparams");
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
			});
			initPageSyn(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,<%=pageInfo.getNeedNewData() %>, <%=isFirstEnter %>, "<%=loadPageUrl %>");
			$('#search').click(function(){submitForm();});
		    $("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});	

			//导出全部数据到excel
			$("#exportCondition").click(
			function()
			{
				
	   			var mtid=$.trim($("#mtid").val());
	   			var type='<%=type==null?"":type.trim() %>';
	   			var lgcorpcode =$("#lgcorpcode").val();
				var lguserid =$("#lguserid").val();
				var phone = '<%=phone==null?"":phone %>';
				var spisuncm = '<%=spisuncm==null?"":spisuncm %>';
			 	if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qrdchshjd")))
			 	{
			   		<%
			   		if(mtList!=null && mtList.size()>0 ){
			   		%>  
			   		    var IsexportAll = true;
			   		    if(confirm(getJsLocaleMessage("ydwx","ydwx_wxfstj_31")))
			   		    {
			   		        IsexportAll = false;
			   		    }  
   	
			   		<%		     
			   		  	if(pageInfo.getTotalRec()<=500000){
			   		%>	
							$.ajax({
								type: "POST",
								url: "wx_wmsTaskRecord.htm?method=smsReportAllExcel",
								data: {
								mtid:mtid,type:type,
								lgcorpcode:lgcorpcode,lguserid:lguserid,
								spisuncm:spisuncm,phone:phone,
								IsexportAll:IsexportAll,
								empLangName:"<%=empLangName%>"
								},
								beforeSend: function(){
									page_loading();
								},
				                complete:function () {
									page_complete();
				                },
								success: function(result){
									if(result=='true'){		
										download_href("wx_wmsTaskRecord.htm?method=downloadFile&down_session=smsReportAllExcel");	                    	
				                    }else{
				                        alert(getJsLocaleMessage("ydwx","ydwx_common_dcshb"));
				                    }
					   			}
							});

								
				   		//	location.href="<%=path%>/wx_wmsTaskRecord.htm?method=smsReportAllExcel&mtid="+mtid+"&type="+type+"&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&spisuncm="+spisuncm+"&phone="+phone+"&IsexportAll="+IsexportAll;
				   		    
				   		<%}else{
				   		%>				   		    
				   		     alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_3"));
				   		<%}
			   		}else{
			   		%>
			   			alert(getJsLocaleMessage("ydwx","ydwx_common_wshjdch"));
			   		<%
			   		}
			   		%>
			   	}				 
			});
			
		});

		//关闭加载窗口的方法		
		function closelog(){
		    var lguserid =$("#lguserid").val();
            $.post("<%=path%>/wx_wmsTaskRecord.htm?method=checkResult",{lguserid:lguserid},function(result){
        	    if(result != null && result =="over" )
        	    {
        	          $("#probar").dialog("close");
		              window.clearInterval(dd);
        	    }
        	});	
		}
</script>
		<script type="text/javascript" src="<%=iPath %>/js/wx_smsTaskAllSendRecord.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	</body>
</html>
