<%@ page language="java" import="java.util.List" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.netnews.bean.*" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser "%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@include file="/common/common.jsp" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));


	LfSysuser curSysuser=(LfSysuser)session.getAttribute("loginSysuser");
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	String bookType=(String)request.getAttribute("bookType");
	@SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("employeeBook");
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

	
	CommonVariables commonVariables = new CommonVariables();
	List<SUCAtable> sucaList =(List<SUCAtable>) session.getAttribute("sucatalbeList");
	String langName = (String)session.getAttribute("emp_lang");
%>
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
	<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/webexbook.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
	<script type="text/javascript">
        $(document).ready(function(){

            setTimeout(function () {
                $('#addeployee').unbind('hover');
                $('#delepl').unbind('hover');
                $('#addeployee').hover(function () {
                    $(this).css('background-position', '0px -62px');
                }, function () {
                    $(this).css('background-position', '0px 0px');
                });
                $('#delepl').hover(function () {
                    $(this).css('background-position', '-402px -62px');
                }, function () {
                    $(this).css('background-position', '-402px 0px');
                });
            }, 10);

            $("#content tbody tr").hover(function() {
                $(this).addClass("hoverColor");
            }, function() {
                $(this).removeClass("hoverColor");
            });

            //初始化翻页
            initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		});
		// 导出EXCEL
		function importExcel()
		 {
			  if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qrdchshjd")))
			   {
			   		var depId = $("#depId").val();
			   		
			   		var name = $("#name").val();
			   		
			   		var phone = $("#phone").val();
			   		<%
			   		if("employee".equals(bookType)){
			   		    if(curSysuser.getUserName().equals("admin")){
			   		    
					   		if(sucaList !=null && sucaList.size() != 0){%>
					   		      location.href="<%=path%>/wx_attached.htm?method=exportEmployeeExcel&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val()+"&lgguid="+$("#lgguid").val();
						
					   		<%}else{
					   		%>
					   		    alert(getJsLocaleMessage("ydwx","ydwx_common_wshjdch"));
					   		<%
					   		}
			   			}else{
			   			%>
					   		    alert(getJsLocaleMessage("ydwx","ydwx_wxsc_15"));
					   	<%}
			   		}%>
			   }
		  }
		  
		//删除素材
		function delSuca(obj){
			if(confirm(getJsLocaleMessage("ydwx","ydwx_wxsc_3"))){
				$.post("<%=path%>/wx_attached.htm?method=doDelsucai",
					{bookIds:obj},
					function(r){
		            		 if(r!=null&&r=="1")
		                     {
		                     	alert(getJsLocaleMessage("ydwx","ydwx_common_shchchg"));
		                     	submitForm();
		                     }
		                     else if(r!=null && r=="0")
		                     {
		                       alert(getJsLocaleMessage("ydwx","ydwx_wxsc_4"));
		                     }
		                     else
		                     {
		                         alert(getJsLocaleMessage("ydwx","ydwx_wxsc_5"))
		                     }
		              
		               });
			}
		}
		
		//素材编辑层显示
		function editSuca(id,name,des){
			$("#editSucai").dialog({
					autoOpen: false,
					height:270,
					width: <%="zh_HK".equals(empLangName)?540:440%>,
					modal: true,
					open:function(){
			
					},
					close:function(){
						doCancel();
					}
				});
		$("#fileId").val(id);
		$("#fileName").val(name);
		$("#fileDes").val(des);
		$("#editSucai").dialog("open");
		
		}
		//网讯素材编辑
		function subEdit(){
			var id = $("#fileId").val();
			var name = $.trim($("#fileName").val());
			var desc=$.trim($("#fileDes").val());
			//增加了全部是空格而不输入内容的判断
			name=name.replace(/(^\s*)|(\s*$)/g, ""); 
			if(name.length>25){
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_6"));
				return;
			}
			desc=desc.replace(/(^\s*)|(\s*$)/g, ""); 
			if(desc.length>50){
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_7"));
				return;
			}
		   	    var pattern = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）—|{}【】‘；：”“'。，、？]"; 
			    for(var i = 0 ; i< pattern.length; i++){
			  		if(name.replace(/(\s*$)/g, "").indexOf(pattern.charAt(i))>-1){
						alert(getJsLocaleMessage("ydwx","ydwx_wxsc_8"));
				    	$("#fileName").select();
			    	  	return false;
					}		

			    }
		  		if(yinhao(getJsLocaleMessage("ydwx","ydwx_wxsc_9"),name)){
					return;
			  	}
			    for(var i = 0 ; i< pattern.length; i++){
			  		if(desc.replace(/(\s*$)/g, "").indexOf(pattern.charAt(i))>-1){
						alert(getJsLocaleMessage("ydwx","ydwx_wxsc_10"));
				    	$("#fileDes").select();
			    	  	return false;
					}
		
			    }
		  		if(yinhao(getJsLocaleMessage("ydwx","ydwx_wxsc_11"),desc)){
					return;
			  	}	
			var des = $("#fileDes").val();
			if(name!=""){
				$.post("<%=path%>/wx_attached.htm?method=doEdisucai",
						{
							bookId:id,
							filename:name,
							filedes:des
						},
						function(r){
			            		 if(r!=null&&r=="1")
			                     {
			                     	alert(getJsLocaleMessage("ydwx","ydwx_common_xgchg"));
			                     	submitForm();
			                     }
			                     else if(r!=null && r=="0")
			                     {
			                       alert(getJsLocaleMessage("ydwx","ydwx_wxsc_12"));
			                     }
			                     else
			                     {
			                         alert(getJsLocaleMessage("ydwx","ydwx_wxsc_13"))
			                     }
			                     endEdit();
			              
			               });
			}else{
				alert(getJsLocaleMessage("ydwx","ydwx_wxsc_14"));
			}
			
		}
		function endEdit(){
			$("#editSucai").dialog("close");
		}
		</script>
		<table id="content" width="100%;">
			<thead>
				<tr>
					 <th width="5%" >
						<input type="checkbox" name="dels" value=""
							onclick="checkAlls(this,'delBook')" />
					</th>
					<th width="20%">
						<emp:message key="ydwx_wxgl_wxsc_mingchen" defVal="素材名称" fileName="ydwx"></emp:message>
					</th>
					<th width="25%">
						<emp:message key="ydwx_wxgl_wxsc_miaoshu" defVal="素材描述" fileName="ydwx"></emp:message>
					</th>
					<th width="10%">
						<emp:message key="ydwx_wxgl_wxsc_daxiao" defVal="素材大小" fileName="ydwx"></emp:message>
					</th>
					<th width="10%">
						<emp:message key="ydwx_wxgl_wxsc_liexing" defVal="素材类型" fileName="ydwx"></emp:message>
					</th>
			
					<th width="20%">
						<emp:message key="ydwx_common_time_shangchuanriqi" defVal="上传日期" fileName="ydwx"></emp:message>
					</th>

					<th colspan="2" width="10%">
						<emp:message key="ydwx_common_caozuo" defVal="操作" fileName="ydwx"></emp:message>
					</th>
	
				</tr>
			</thead>
			<tbody>
			<%
				@SuppressWarnings("unchecked")
				List<DynaBean> beans = (ArrayList<DynaBean>)request.getAttribute("pagebaseinfo");
				if(beans!=null && beans.size()>0)
				{
					for(DynaBean bean:beans)
					{
					    String file = (String)bean.get("type");
						if("文件".equals(file)||file.matches("[Ff]ile")){
							file = MessageUtils.extractMessage("ydwx","ydwx_common_file",request);
						}else if(file.matches("[pP]icture")||file.matches("[圖图]片")){
							file = MessageUtils.extractMessage("ydwx","ydwx_common_picture",request);
						}else if(file.matches("视频|視頻")||file.matches("[vV]ideo")){
							file = MessageUtils.extractMessage("ydwx","ydwx_common_video",request);
						}else if(file.matches("[oO]thers?")||file.matches("其他")){
							file = MessageUtils.extractMessage("ydwx","ydwx_common_other",request);
						}
				%>
				<tr>
					<td>
						<input type="checkbox" name="delBook" id="<%=bean.get("id") %>" value="<%=bean.get("id") %>"/>
					</td> 
					 <td id="oldName"><xmp><%=bean.get("filename")==null?"":bean.get("filename")%></xmp></td>
					 <td id="oldDes"><xmp><%=bean.get("filedesc")==null?"":bean.get("filedesc") %></xmp></td>
					 <td id="filesize"><%=bean.get("filesize")==null?"0"+MessageUtils.extractMessage("ydwx","ydwx_wxgl_wxsc_zijie",request):bean.get("filesize")+MessageUtils.extractMessage("ydwx","ydwx_wxgl_wxsc_zijie",request)%></td>
					 <td><%=(bean.get("type")==null)?"-":file %></td>
					 <td><%=bean.get("creatdate").toString().replaceAll("\\.\\d*", "")%></td>
					<td>
					  <a href="javascript:void(0)" onclick='editSuca("<%=bean.get("id") %>","<%=bean.get("filename")  %>","<%=bean.get("filedesc")==null?"":bean.get("filedesc")  %>")'><emp:message key='ydwx_common_btn_bianji' defVal='编辑' fileName='ydwx'></emp:message></a>
					 
				    </td>
				    <td> <a href="javascript:void(0)" onclick="delSuca(<%=bean.get("id")  %>)"><emp:message key='ydwx_common_btn_shanqu' defVal='删除' fileName='ydwx'></emp:message></a></td>
				</tr>
			<%
				}}
				if(beans==null || beans.size()==0){%>
					<tr><td colspan="9"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
			<%
				}

			%>

			</tbody>
			<tfoot>
				<tr>
					<td colspan="9">
						<div id="pageInfo"></div>
					</td>
				</tr>
			</tfoot>
		</table>
		<%--修改素材开始	 --%>
	<div id="editSucai" title="<emp:message key="ydwx_wxgl_wxsc_bianji" defVal="编辑素材" fileName="ydwx"></emp:message>" class="hidden ydwx_editSucai">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
					<tr><td>&nbsp;</td></tr>       
					<tr><td><input id="fileId" type="hidden"/></td></tr>                          
				  <tr>
   				  	 <td nowrap="nowrap">
   				  		<emp:message key="ydwx_wxgl_wxsc_mingchens" defVal="素材名称：" fileName="ydwx"></emp:message>
					 </td>
					 <td>
						 <input id="fileName" name="AttachmentName" type="text" class="ydwx_fileName"/>
						 <font color="red">*</font>（<emp:message key="ydwx_wxgl_wxsc_bitianxiang" defVal="必填项" fileName="ydwx"></emp:message>）
					 </td>
  				 </tr>
  				 <tr><td>&nbsp;</td></tr>
 			   <tr>
   				  <td valign="top" nowrap="nowrap">
   				  		<emp:message key="ydwx_wxgl_wxsc_miaoshus" defVal="素材描述：" fileName="ydwx"></emp:message>
				  </td>
				   <td>
					   <textarea id="fileDes" cols="30" rows="5" class="ydwx_fileDes"></textarea>
				   </td>
 			   </tr>
				<tr><td>&nbsp;</td></tr>

				<tr><td>&nbsp;</td></tr>

				<tr>
					<td colspan="2">
							<div id="cover"></div>
							<div id="progressBar" class="ydwx_progressBar">
								<div id="theMeter">
									<div id="progressBarText"></div>
									<div id="progressBarBox">
										<div id="progressBarBoxContent"></div>
									</div>
								</div>
						   </div>
					</td>
				</tr>
				<tr><td height="5px"></td></tr>
							<tr><td colspan="2" align="center">
								<input type="button" onclick="subEdit()" class="btnClass5 mr23" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>"/>
								<input type="button" onclick="endEdit()" class="btnClass6" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>"/>
								<br/>
							</td></tr>
			</table>

					
	</div>
	<%--修改素材结束 --%>
