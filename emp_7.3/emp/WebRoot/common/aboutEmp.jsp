<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.gateway.AProInfo"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.montnets.emp.common.entity.LfChangeLog" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String empLangName = (String) session.getAttribute(StaticValue.LANG_KEY);
String langName = empLangName == null ? request.getParameter("empLangName") : empLangName;
AProInfo proInfo = (AProInfo) request.getAttribute("proInfo");
Integer validDay = null;
String statusInfo = null;
Integer sendSpeed = null;
if(proInfo != null)
{
	validDay = proInfo.getValidDays();
	statusInfo = proInfo.getStatusInfo();
	sendSpeed = proInfo.getSendSpeed();
}
//EMP总版本号
String ver = StaticValue.getEmpVersion();
//版本更新时间
String updateTime ="";

//各版本信息 1000:EMP-WEB,2000:E网关WBS,3000:网关SPGATE
Map<String,String[]> versions = (Map<String, String[]>) request.getAttribute("versions");
//数据库脚本信息
String dbVersion = (String) request.getAttribute("dbVersion");
if(versions != null){
	String[] verInfo = versions.get("1000");
	if(verInfo != null){
		ver = verInfo[0];
		if(!ver.contains("V")){
			ver = "V"+ver;
		}
		updateTime = verInfo[1];
		if(updateTime.length()>10){
			updateTime = updateTime.substring(0,10);
		}
	}
}

String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
   <title></title>
	  <style>
		  #ver-info{
			  line-height: 22px;
			  margin: 2px;
		  }
		  #ver-info dt h3{
			  display: inline;
			  margin-right: 24px;
			  font-size: 12px;

		  }
          #ver-info dt
          {
              margin-top: 16px;
          }
		  #ver-info li{
			  list-style: none;
			  padding-left: 4px;
		  }
		  #ver-info dd{
			  margin: 0;
		  }
		  #ver-info ul{
			  padding-left: 0;
			  margin: 0;
		  }
		  div table td{vertical-align: top;}
		  html { overflow-x:hidden;font-size: 12px;}
		  .f-blod{font-weight: bold;width:72px;text-align: left;}
		  .indent{text-indent:1em;  }
		  #versions ol{padding-left: 0;margin: 0;}
		  #versions li{list-style: none;line-height: 18px;height:18px;}
		  #versions li label{float: left;width:112px;}
		  #proInfo{
			  display: block;
		  }
		  #version_table td {
             padding: 0 6px;
             text-align:center;
              border: 1px solid #bdc7d7;
              padding: 4px 4px;
		  }
          #version_table{
              width:100%;
              border-collapse: collapse;
          }
          #version_table td .version-note{
              text-align:left;
              line-height: 14px;
              padding-right: 0;
          }
          #versionDiv{
              padding-bottom: 20px;
          }
		</style>
	  <%if(StaticValue.ZH_HK.equals(langName)){%>
	  <link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	  <style type="text/css">
		  body > div:nth-child(1) > table > tbody > tr > td:nth-child(1){
			  text-indent: 0;
			  width: 160px;
		  }
	  </style>
	  <%}%>
  </head>


  <body onload="showVer()" style="background: url(common/img/about.png) right center no-repeat #eef2fd;padding:0;margin:0;background-position-y:10px; " scroll= "auto">
  	<div style="line-height:20px;padding-top: 2px;padding-left: 20px;">
		<input type="hidden" value="<%=langName%>" id="langName"/>
  		<table border="0px;" style="width:486px;">
  		   <tr>
  		     <td class="f-blod indent" id="ver_num_p"><emp:message key="common_aboutEmp_2" defVal="版本号：" fileName="common"></emp:message></td>
  		     <td><label id="lab"><%=ver %></label>&nbsp;&nbsp;&nbsp;&nbsp;
  		     <a id="version_a" style="color:#0000ff;cursor: pointer;" href="#"><emp:message key="common_aboutEmp_11" defVal="版本详情" fileName="common"></emp:message></a></td>
  		   </tr>
			<%--<%if(versions != null){%>--%>
			<tr id="versions">
				<td></td>
				<td>
					<ol>
						<%if(versions != null){
							Iterator<String> its = versions.keySet().iterator();
							while(its.hasNext()){
								String key = its.next();
								if("1000".equals(key)){continue;}
								String[] infos = versions.get(key);
								out.print("<li><label>&bull;"+infos[2]+"：</label>"+infos[0]+"</li>");
							}
						}%>
						<li><label id="DB_script">&bull;<emp:message key="common_aboutEmp_4" defVal="数据库脚本：" fileName="common"></emp:message></label><%=StringUtils.defaultIfEmpty(dbVersion,"")%></li>
					</ol>

				</td>
			</tr>
			<%--<%}%>--%>
  		   <tr>
  		     <td class="f-blod" id="Copyright_company"><emp:message key="common_aboutEmp_5" defVal="版权公司：" fileName="common"></emp:message></td>
  		     <td><a style="color:#0000ff;cursor: pointer;" href="http://www.montnets.com" target="_blank"  rel="noopener noreferrer nofollow" id="montnets"><emp:message key="common_aboutEmp_6" defVal="深圳市梦网科技发展有限公司" fileName="common"></emp:message></a></td>
  		   </tr>
  		   <tr>
  		     <td class="f-blod" id="service_tel"><emp:message key="common_aboutEmp_7" defVal="客服电话：" fileName="common"></emp:message></td>
  		     <td>0755-86017780&nbsp;&nbsp;400-700-100-9</td>
  		   </tr>
  		   <tr>
  		       <td class="f-blod indent" id="validity_date"><emp:message key="common_aboutEmp_8" defVal="有效期：" fileName="common"></emp:message></td>
  		       <td><label id="youxiaoday"></label></td>
  		   </tr>
  		   <tr>
  		     <td class="f-blod" id="status"><emp:message key="common_aboutEmp_9" defVal="认证状态：" fileName="common"></emp:message></td>
  		     <td><label id="proInfo"></label></td>
  		   </tr>
  		   <tr>
  		     <td class="f-blod" id="speed"><emp:message key="common_aboutEmp_10" defVal="发送速度：" fileName="common"></emp:message></td>
  		     <td><label id="sendspeed"></label></td>
  		   </tr>
  		</table>
  		<input type="hidden" id="validDay" value="<%=validDay%>">
  		<input type="hidden" id="statusInfo" value="<%=statusInfo%>">
  		<input type="hidden" id="sendSpeed" value="<%=sendSpeed%>">
  	</div>
  	<div id="versionDiv">
  		<hr/>
		<table id="version_table">
		   <tr>
		   		<th colspan="3" style="text-align: left;padding-left: 2em;" id="version_a1"><emp:message key="common_aboutEmp_11" defVal="版本详情" fileName="common"></emp:message></th>
           </tr>
           <tr>
  		     <td style="width:70px;" id="Upgrade_date"><emp:message key="common_aboutEmp_12" defVal="升级时间" fileName="common"></emp:message></td>
  		     <td style="width:70px;" id="ver_num"><emp:message key="common_aboutEmp_1" defVal="版本号" fileName="common"></emp:message></td>
  		     <td id="Version_features"><emp:message key="common_aboutEmp_13" defVal="版本功能" fileName="common"></emp:message></td>
           </tr>
  		     <%
                List<LfChangeLog> changeLogs = (List<LfChangeLog>) request.getAttribute("changeLogs");
                if(changeLogs != null)
                {
                    int i=0;
                    for (LfChangeLog changeLog : changeLogs) {
					i++;
            %>
  		   <tr>
  		     <td><%=changeLog.getReleasetime()%></td>
  		     <td><%=changeLog.getMajorversion()%></td>
			 <%
			 	String releaseNote = changeLog.getReleaseNote();
				 if(releaseNote != null && releaseNote.startsWith("1、[通信管理]")){
					 releaseNote = MessageUtils.extractMessage("common","common_aboutEmp_14",request);
				 }else if(releaseNote != null && releaseNote.startsWith("1、EMP接入需支持SMPP3.4")){
					releaseNote = MessageUtils.extractMessage("common","common_aboutEmp_15",request);
				}else if(releaseNote != null && releaseNote.startsWith("1、EMP多语言版本支持")){
					 releaseNote = MessageUtils.extractMessage("common","common_aboutEmp_16",request);
				 }
			 %>
  		     <td><div class="version-note releaseNote"><%=releaseNote%></div></td>
			</tr>
            <%
                    }
                }
            %>
			
  		</table>		
    </div>
	<script type="text/javascript" src="<%=path%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=path%>/common/i18n/i18nUtil.js"></script>
  	<script type="text/javascript" src="<%=path %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
  	<script type="text/javascript" src="<%=path %>/common/commonJs/aboutEmp.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
  	<script type="text/javascript" src="<%=path %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<%if(!"zh_CN".equals(request.getParameter("empLangName")) && "1".equals(request.getParameter("isLogin"))){%>
		<script type="text/javascript" src="<%=path %>/common/commonJs/aboutEmp_login.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<%}%>
	<script type="text/javascript">
		$(document).ready(function(){

            $('#version_a').click(function(){
                $('#versionDiv').toggle(function(){
                    var offset = $(this).offset();
                    $(document).scrollTop(offset.top);
                });

            });
            $('#version_table td .version-note').each(function(){
                var $div = $(this);
                if($div.outerHeight() > 42)
                {
                    $div.css({'height':42,'overflow':'hidden',
                    'cursor': 'pointer','color': '#0000ff'});
                    $div.click(function(){
                        if($(this).height() == 42)
                        {
                            $(this).height('auto');
                        }else{
                            $(this).height(42);
                        }
                        $(document).scrollTop($(this).offset().top);
                    })
                }
            });
            $('#versionDiv').hide();
		});
    </script>
  </body>
</html>
