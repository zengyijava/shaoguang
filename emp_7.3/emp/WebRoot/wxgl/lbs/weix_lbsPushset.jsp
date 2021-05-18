<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.lbs.LfLbsPushset"%>
<%@page import="com.montnets.emp.ottbase.constant.WXStaticValue"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("lbsManager");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    //获取该企业的默认推送设置
    LfLbsPushset pushsetObj= (LfLbsPushset)request.getAttribute("pushsetObj");
    
    String filePath = GlobalMethods.getWeixFilePath();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>">
		<style>
		#uploadPic,#filename{
			width:65px;
			height:25px;
			position: absolute;
			left:0;
			top:0;
		}
		#filename{
			left:-5px;
			filter: Alpha(opacity=0);
			opacity: 0;
		}
		</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			  		<input id="pathUrl" value="<%=path %>" type="hidden" />
					<div style="display:none" id="getloginUser"></div>
					<%-- 默认推送模式ID --%>
					<input id="pushid" value="<%=pushsetObj.getPushId() %>" type="hidden" />
					<input type="hidden" id="filePath" value="<%=filePath%>"/>
					<div id="tab" style="width: 830px;padding-left: 16px;height: 400px;">
				    <table height="35px;"  width="460px" style="font-size: 13px;">  
				      <tr align="center">
				      <td id="addone"  class="infotd1" onclick="javascript:changeinfo(1)"><emp:message key="wxgl_gzhgl_title_141" defVal="多图文模式" fileName="wxgl"/></td>
				      <td id="addall" class="infotd2" onclick="javascript:changeinfo(2)"><emp:message key="wxgl_gzhgl_title_142" defVal="页面交互模式" fileName="wxgl"/></td>
				      </tr>
				    </table> 
	                <div id="addoneDiv" class="block" style="width:820px;line-height: 35px !important; line-height: 20px;padding-left: 5px;padding-top: 30px;">
	                   <center>
						<form action="" method="post" id="addForm" name="addForm" >
							<% 
									String radius = "2000";
									String autoradius = "2";
									String automore = "2";
									String pushcount = "3";
									String note = "";
									String imgurl = "";
									//模式
									Integer pushtype = pushsetObj.getPushtype();
									if(pushsetObj != null && "1".equals(pushtype.toString())){
									    //服务半径
									    radius = pushsetObj.getRadius().toString();
									    //是否自动扩大
									    autoradius = pushsetObj.getAutoradius().toString();
									    //推送条数
									    pushcount = pushsetObj.getPushcount().toString();
									    //是否更多
									    automore = pushsetObj.getAutomore().toString();
									}else if(pushsetObj != null && "2".equals(pushtype.toString())){
									    if(pushsetObj.getNote() != null && !"".equals(pushsetObj.getNote())){
									        note = pushsetObj.getNote().replaceAll("<","&lt;").replaceAll(">","&gt;");
									        note=note.trim();
									    }
									    if(pushsetObj.getImgurl() != null && !"".equals(pushsetObj.getImgurl())){
									        imgurl = pushsetObj.getImgurl();
									    }
									}
								%>
							<table id="sysTable"  height="100%" style="font-size: 12px;width: 820px;">
										<tr>
											<td width="120px;">
												<b><emp:message key="wxgl_gzhgl_title_141" defVal="多图文模式" fileName="wxgl"/></b>：
											</td>
											<td align="left">
												<emp:message key="wxgl_gzhgl_title_143" defVal="用户在微信终端接收到的是多图文消息,系统将测算距离用户最近的内容推送给用户." fileName="wxgl"/>
												<a id="downlinks_1" style=" padding-bottom: 10px; position: relative; cursor: pointer; text-decoration: none;"><emp:message key="wxgl_button_11" defVal="预览" fileName="wxgl"/></a>
											</td>
										</tr>
										<tr>
											<td width="140px;">
												<emp:message key="wxgl_gzhgl_title_144" defVal="设置服务搜索半径：" fileName="wxgl"/>
											</td>
											<td align="left">
											  <div style="width:430px;">
												  <div style="width:90px;float: left;">
													<select id="radius" name="radius"  style="width: 90px;">
															<option value="2" <%if("2".equals(radius)){%> selected="selected"<%}%> >2000</option>
															<option value="5" <%if("5".equals(radius)){%> selected="selected"<%}%> >5000</option>
															<option value="8" <%if("8".equals(radius)){%> selected="selected"<%}%> >8000</option>
													</select>
													  </div>	
												   <div style="width:330px;float: right;">
														&nbsp;
														<input type="checkbox" id="autoradius" name="autoradius"  <%if("1".equals(autoradius)){%> checked="checked" <%} %>>
														<emp:message key="wxgl_gzhgl_title_145" defVal="如指定搜索的半径内无内容，自动扩大搜索半径。" fileName="wxgl"/>
													</div>	
											  </div>	
											</td>
										</tr>
										<tr>
											<td  width="140px;">
												<emp:message key="wxgl_gzhgl_title_146" defVal="多图文推送条目：" fileName="wxgl"/>
											</td>
											<td align="left">
											<div style="width:430px;">
												  <div style="width:90px;float: left;">
													<select id="pushcount" name="pushcount" style="width: 90px;">
															<option value="3" <%if("3".equals(pushcount)){%> selected="selected"<%}%> >3<emp:message key="wxgl_gzhgl_title_147" defVal="条" fileName="wxgl"/></option>
															<option value="4" <%if("4".equals(pushcount)){%> selected="selected"<%}%> >4<emp:message key="wxgl_gzhgl_title_147" defVal="条" fileName="wxgl"/></option>
															<option value="5" <%if("5".equals(pushcount)){%> selected="selected"<%}%> >5<emp:message key="wxgl_gzhgl_title_147" defVal="条" fileName="wxgl"/></option>
															<option value="6" <%if("6".equals(pushcount)){%> selected="selected"<%}%> >6<emp:message key="wxgl_gzhgl_title_147" defVal="条" fileName="wxgl"/></option>
															<option value="7" <%if("7".equals(pushcount)){%> selected="selected"<%}%> >7<emp:message key="wxgl_gzhgl_title_147" defVal="条" fileName="wxgl"/></option>
															<option value="8" <%if("8".equals(pushcount)){%> selected="selected"<%}%> >8<emp:message key="wxgl_gzhgl_title_147" defVal="条" fileName="wxgl"/></option>
													</select>
												  </div>	
												   <div style="width:330px;float: right;">
													&nbsp;
													<input type="checkbox" id="automore" name="automore"  <%if("1".equals(automore)){%> checked="checked" <%} %>>
													<emp:message key="wxgl_gzhgl_title_148" defVal="启用更多，连接到页面交互模式。" fileName="wxgl"/>
													</div>	
											  </div>
											</td>
										</tr>
								</table>
								<div style="width:820px;padding-top: 30px;" align="center">
									<input type="button" name="multibtn" id="multibtn" value="<emp:message key='wxgl_button_13' defVal='启用' fileName='wxgl'/>" class="btnClass5 mr23"  
									onclick="javascript:set('multi');" />
									<input type="button" id="backbtn1" value="<emp:message key='wxgl_button_8' defVal='返回' fileName='wxgl'/>" class="btnClass6" onclick="javascript:back();" />
								</div>
						</form>
						</center>
					</div>
					<div id="addallDiv" style="display:none;line-height: 40px;padding-left: 20px;padding-top: 10px;">
					  <center>
							<form action="<%=path %>/weix_lbsManager.htm?method=pushSetPage2" method="post" enctype="multipart/form-data" id="uploadForm" name="uploadForm" target="hidden_iframe">
								<iframe name="hidden_iframe" id="hidden_iframe" style="display: none"></iframe>
								<input type="hidden" name="imgurl" id="imgurl" value="<%=imgurl %>" />
								<table id="sysTable" width="100%" height="100%" style="font-size: 12px;" >
									<thead>
										<tr>
											<td width="100px;">
												<b><emp:message key="wxgl_gzhgl_title_142" defVal="页面交互模式" fileName="wxgl"/></b>：
											</td>
											<td align="left">
												 <emp:message key="wxgl_gzhgl_title_149" defVal="用户在微信终端接收到的是单图文消息,系统将测算距离用户最近的内容推送给用户." fileName="wxgl"/>
												<a id="downlinks_2" style=" padding-bottom: 10px; position: relative; cursor: pointer; text-decoration: none;"><emp:message key="wxgl_button_11" defVal="预览" fileName="wxgl"/></a>
											</td>
										</tr>
										<tr>
											<td width="100px;">
												<emp:message key="wxgl_gzhgl_title_150" defVal="图文图片：" fileName="wxgl"/>
											</td>
											<td>
											<div align="left"  > 
											<img  width="300"  id="prepic" src="<%=imgurl %>" >
											<input type="hidden" id="picurl" name="picurl" />
											</div>
											</td>
										</tr>
										
										<tr>
										<td width="100px;">
										
										</td>
										<td align="left">
											<div style="position: relative;height: 25px;overflow: hidden;width:65px;">
												<input type="button" value="<emp:message key='wxgl_button_14' defVal='浏览' fileName='wxgl'/>" class="btnClass2" id="uploadPic">
												<input type="file" value="<emp:message key='wxgl_button_15' defVal='上传' fileName='wxgl'/>" onchange="javascript:changePreImg();" id="filename" name="filename" style="width:70px;height: 25px;">
											</div>
										</td>
										</tr>
										<tr>
											<td width="100px;">
												&nbsp;
											</td>
											<td align="left">
												<emp:message key="wxgl_gzhgl_title_151" defVal="建议图片大小:720*480,格式*.jgp,*.png" fileName="wxgl"/>
											</td>
										</tr>
										
										<tr>
											<td width="100px;">
												<emp:message key="wxgl_gzhgl_title_152" defVal="内容简介：" fileName="wxgl"/>
											</td>
											<td align="left">
												<textarea name="note" id="note" style="width:508px;height:85px;" rows="5" class="input_bd div_bd" maxLength="250"><%=note%></textarea>
												<span id="note-span">0</span>/250
											</td>
										</tr>					
									</thead>
								</table>
								<div style="width:820px;padding-top: 30px;" align="center"><input type="button" name="singlebtn" id="singlebtn"  
									class="btnClass5 mr23" value="<emp:message key='wxgl_button_13' defVal='启用' fileName='wxgl'/>" onclick="javascript:set2('single');"  />
									
									<input type="button" id="backbtn2" value="<emp:message key='wxgl_button_8' defVal='返回' fileName='wxgl'/>" class="btnClass6" onclick="javascript:back();" />
								</div>
							</form>
							</center>
					</div>
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
			
			
			
			<%-- 格式提示 --%>
			 <div id="infomodel_1" class="infomodelclass" style="display: none;position: absolute;z-index: 5000;border: 1px solid #dae1cf;background-color:white;width:220px;height:315px;">
                   <center>
                    <table>
                      <tr>
                       <td>
                       	<div style="width:220px;height:315px;background:url('<%=iPath %>/img/mutil_<%=langName %>.png') no-repeat; ">
                       	</div>
                       </td>
                      </tr>
                    </table>
                  </center>
           </div>
			
			
			<div id="infomodel_2" class="infomodelclass" style="display: none;position: absolute;z-index: 5000;border: 1px solid #dae1cf;background-color: white;width:415px;height:315px;">
                   <center>
                    <table>
                      <tr>
                       <td>
                       	<div id="txtstyle" style="width:415px;height:315px;background:url('<%=iPath %>/img/single_<%=langName %>.png') no-repeat; ">
                       	</div>
                       </td>
                      </tr>
                    </table>
                  </center>
           </div>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/wxcommon/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
  	<script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?v=<%=WXStaticValue.OTT_VERSION %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"  ></script>
	<script type="text/javascript" src="<%=iPath %>/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript"  src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	
	
	<script>
  		$('#radius,#pushcount').isSearchSelect({'width':'90','isInput':false,'zindex':0});
			
			$(function() {
				floating("downlinks_1","infomodel_1");
				floating("downlinks_2","infomodel_2");
				getLoginInfo("#getloginUser");
				//进来设置时多图文模式1还是页面交互模式2
				<%
					Integer type = pushsetObj.getPushtype();
				%>
				changeinfo('<%=type%>');
				<%if(type == 2){%>    
					//showPushSet();
				<% }%>

				var imgurl = $("#imgurl").val();
				if(imgurl==null||imgurl==""){
					$("#prepic").hide();
				}
			});


			
			function changePreImg(){
				var imgtem = $("#filename").val();
		    	var filePath = $("#filePath").val();
		    	var imgurl = $("#imgurl").val();
		    	if(imgtem.length<1){
		    	    alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_20"));
			    	return ;	
			    }else{
			    	var c = ".jpg|.jpeg|.gif|.bmp|.png|";
			        var b = imgtem.substring(imgtem.lastIndexOf("."),imgtem.length);
		            if(c.indexOf(b)<0){
		                alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_21"));
			            return;
			        }
				}
		    	var pathUrl = $("#pathUrl").val();
				var lgcorpcode = $("#lgcorpcode").val();
				var pushid = $("#pushid").val();
				 $.ajaxFileUpload({
			            url: pathUrl+"/weix_lbsManager.htm?method=changeImg", //需要链接到服务器地址
			            secureuri: false,
			            dataType: "html",
			            fileElementId: "filename",                            //文件选择框的id属性
			            //服务器返回的格式，可以是json
			            success: function (data, textStatus) {
			            	           //相当于java中try语句块的用法
				            if('success'==textStatus){
				                	var message = "";
				                	try{
				                		message =$(data).text();
				               		}catch(e){
				                		message = data;
				                	}
				                	message = data;
					                message=message.substr(message.indexOf("@")+1);
					                var imgurl = message.split(",")[0].split(':')[1];

					                console.log(message);
					                var fieldName = message.split(",")[1].split(':')[1];

					                console.log(fieldName);
								    $("#prepic").attr({"src":filePath+imgurl}).show();
									$("#picurl").attr({"value":imgurl})
				            }else{
				            	alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_23"));
					        }
			            },
			            error: function (data, status, e) {           //相当于java中catch语句块的用法
			               // $('#imgPath').val('');
			            	alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_23"));
			            }
			        });

			}


			function set2(pushtype){
				var radius;
				//是否启用扩大     1启用 2不启用
				var autoradius = "2";
				//推送条数 
				var pushcount;
				//是否起用更多	1启用 2不启用
				var automore = "2";
				//文件名称 
				var filename;
				//简介 
				//var note;
				//图片格式png或者jpg
				var imgformat = "jpg";
				//上传图片情况    0默认状态     1是不需要上传图片,2是需要上传图片
				var uploadFlag = "0";
				//服务器图片路径
				var picurl = $("#picurl").val();
				handleBtn("disabled");
				var pathUrl = $("#pathUrl").val();
				var lgcorpcode = $("#lgcorpcode").val();
				var pushid = $("#pushid").val();
				var imgurl = $("#imgurl").val();
				var note=$("#note").val();
				if(note.length>250){
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_39"));
					handleBtn("enabled");
					return;
				}
				//表示存在已有图片
				if(imgurl != null && imgurl.length > 0){
					uploadFlag = "1";
				}
				//处理单图文
	            $("#uploadForm").attr("action",$("#uploadForm").attr("action")+"&pushtype="+pushtype+"&imgformat="+imgformat+"&pushid="+pushid+"&uploadFlag="+uploadFlag+"&picurl="+picurl);
				$("#uploadForm").submit();
			}
			


			

			//设置模式    single  网页交互/ multi 多图文
			function set(pushtype){
				//服务半径
				var radius;
				//是否启用扩大     1启用 2不启用
				var autoradius = "2";
				//推送条数 
				var pushcount;
				//是否起用更多	1启用 2不启用
				var automore = "2";
				//文件名称 
				var filename;
				//简介 
				//var note;
				//图片格式png或者jpg
				var imgformat = "jpg";
				//上传图片情况    0默认状态     1是不需要上传图片,2是需要上传图片
				var uploadFlag = "0";
				
				handleBtn("disabled");
				if(pushtype == "multi"){
					//半径
					radius = $("#radius").val();
					//是否启用扩大半径 
					var autoradiuslength = $("input[name='autoradius']:checked").length;
					if(autoradiuslength > 0){
						autoradius = "1";
					}
					//推送条目
					pushcount = $("#pushcount").val();
					//是否启用更多 
					var automorelength = $("input[name='automore']:checked").length;
					if(automorelength > 0){
						automore = "1";
					}
				}else if(pushtype == "single"){
					var imgurl = $("#imgurl").val();
					//表示存在已有图片
					if(imgurl != null && imgurl.length > 0){
						uploadFlag = "1";
					}
					filename = $("#filename").val();
					alert(filename);
					//没有上传过图片
					if(uploadFlag == "0" && filename == ""){
						alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_40"));
						handleBtn("enabled");
						return;
					}
					//判断     0默认状态     1是不需要上传图片,2是需要上传图片
					if(uploadFlag == "0" || (filename != "" && uploadFlag == "1")){
						filename = filename.substring(filename.lastIndexOf("\\")+1);
						filename = filename.toString().substring((filename.toString().lastIndexOf("."))+1).toUpperCase();
						if (filename != "" && !(filename == "JPG" || filename == "PNG")){
							alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_41"));
							handleBtn("enabled");
					        return;
					    }
						uploadFlag = "2";
						imgformat = filename.toLowerCase();
					}
				}else{
					return;
				}
				var pathUrl = $("#pathUrl").val();
				var lgcorpcode = $("#lgcorpcode").val();
				var pushid = $("#pushid").val();
				//处理多图文 
				if(pushtype == "multi"){
					$.post(pathUrl+"/weix_lbsManager.htm",{
						method:"pushSetGraphics",
						radius:radius,
						autoradius:autoradius,
						pushcount:pushcount,
						automore:automore,
						lgcorpcode:lgcorpcode,
						pushtype:pushtype,
						pushid:pushid,
						isAsync: "yes"
						},function(returnmsg){
								handleBtn("enabled");
								if (returnmsg == "outOfLogin") {
									window.location.href = pathUrl + "/common/logoutEmp.html";
									return;
								}else if(returnmsg == "success"){
									handleBtn("disabled");
									alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_42"));
									setTimeout('back(1)',1500); 
									return;
								}else if(returnmsg == "fail"){
									alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_43"));
									return;
								}else if(returnmsg == "error"){
									alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_44"));
									return;
								}else if(returnmsg == "paramsIsNull" || returnmsg == "objectIsNull"){
									alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_45"));
									return;
								}else{
									alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_43"));
									return;
								}
						});
				}else if(pushtype == "single"){
					//处理单图文
		            $("#uploadForm").attr("action",$("#uploadForm").attr("action")+"&pushtype="+pushtype+"&imgformat="+imgformat+"&pushid="+pushid+"&uploadFlag="+uploadFlag+"&picurl="+picurl);
					$("#uploadForm").submit();
				}
			}

			//卡片切换
			function changeinfo(type) {
				if (type == '1') {
					$("#addone").removeClass();
					$("#addall").removeClass();
					$("#addone").addClass("infotd1");
					$("#addall").addClass("infotd2");
					
					$("#addoneDiv").show();
					$("#addallDiv").hide();
				} else {
					$("#addone").removeClass();
					$("#addall").removeClass();
					$("#addall").addClass("infotd1");
					$("#addone").addClass("infotd2");

					$("#addallDiv").show();
					$("#addoneDiv").hide();
				}
			}

			//操作按钮
			function handleBtn(type){
				if(type == "enabled"){
					$("#multibtn").attr("disabled",false);
					$("#singlebtn").attr("disabled",false);
				}else if(type == "disabled"){
					$("#multibtn").attr("disabled","disabled");
					$("#singlebtn").attr("disabled","disabled");
				}
			}
			

			//返回
			function back(type){
				var lgcorpcode = $("#lgcorpcode").val();
				var pathUrl = $("#pathUrl").val();
				if(type == 1){
					doGo(pathUrl + "/weix_lbsManager.htm?method=find&lgcorpcode="+lgcorpcode);
				}else if(type == 2){
					doGo(pathUrl + "/weix_lbsManager.htm?method=find&lgcorpcode="+lgcorpcode);
				}
				return;
			}
			function doGo(url)
			{
				location.href = url;
			}
			//上传图片返回状态 
			<%--
			function showPushSet(pushsetResult){
				<% 
					String pushsetResult=(String)request.getAttribute("pushsetResult");
					if(pushsetResult!=null && pushsetResult.equals("success")){%>
						handleBtn("disabled");
						alert("启用成功",2);
						setTimeout('back(2)',1500); 
						return;
				<%}else if (pushsetResult!=null && pushsetResult.startsWith("fail")){%>
						handleBtn("enabled");
						alert("启用失败。");
				<%}else if(pushsetResult != null && pushsetResult.equals("oversize")){%>
						handleBtn("enabled");
						alert("图片超过128kb,请重新上传。");
				<%}else if(pushsetResult != null && pushsetResult.equals("uploadfail")){%>
						handleBtn("enabled");
						alert("图片上传失败。");
				<%}else if(pushsetResult != null && (pushsetResult.equals("paramsIsNull") || pushsetResult.equals("ObjectIsNull"))){%>
						handleBtn("enabled");
						alert("参数/对象获取失败。");
				<%}else if(pushsetResult != null){%>
						handleBtn("enabled");
						alert("启用失败。");
				<%}%>
			}
			
			--%>

			function showPushSet(pushsetResult){
				if(pushsetResult == "success"){
					handleBtn("disabled");
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_42"));
					setTimeout(back(2),1500); 
					return;
				}else if (pushsetResult != null && pushsetResult.indexOf("fail") > 0){
					handleBtn("enabled");
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_43"));
				}else if(pushsetResult != null && pushsetResult == "oversize"){
					handleBtn("enabled");
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_48"));
				}else if(pushsetResult != null && pushsetResult == "uploadfail"){
					handleBtn("enabled");
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_49"));
				}else if(pushsetResult != null && (pushsetResult == "paramsIsNull" || pushsetResult == "ObjectIsNull")){
					handleBtn("enabled");
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_45"));
				}else{
					handleBtn("enabled");
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_43"));
				}
			}

			//跳转回查询页面
			function back(){
				var lgcorpcode = $("#lgcorpcode").val();
				var pathUrl = $("#pathUrl").val();
				doGo(pathUrl + "/weix_lbsManager.htm?method=find&lgcorpcode="+lgcorpcode);
				return;
			}
			
		  function leftcount (obj) {
			  	obj = obj || '';
			  	//IE9及以下不支持textarea的maxlength属性
			  	if(obj.value.length>250){
			  		obj.value = obj.value.substring(0,250);
			  	}
			  	return obj.value.length;
		  }
		  	var span = document.getElementById('note-span');
		  	var text = document.getElementById('note');
		  	span.innerText = leftcount(text);
		  	if(text.addEventListener){
				text.addEventListener('input',function () {
		  			//firefox不支持 innerText
		  			if(span.innerText){
		  				span.innerText = leftcount(this);
		  			}else{
		  				span.textContent = leftcount(this);
		  			}
		  		},false);
		  	}else if(text.attachEvent){
				text.attachEvent('onpropertychange',function () {
					span.innerText = leftcount(text);
				})
		  	}

	</script>
	</body>
</html>