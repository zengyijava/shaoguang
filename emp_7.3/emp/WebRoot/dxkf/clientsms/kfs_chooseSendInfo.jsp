<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="dxkf_ydkf_xzfsdx_text_title" defVal="选择发送对象" fileName="dxkf"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=inheritPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=inheritPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link href="<%=skin%>/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/kfs_chooseSendInfo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_KeFuDuanXin.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="chooseSendInfo">
	<input type="hidden" id="pathUrl" value="<%=path %>">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center class="centerBack">
			<div class="dxkf_div1">
			<form method="post" id="selectForm">
				<input type="hidden" id="strUser" name="strUser" value=""/>
				<input type="hidden" id="ipath" value="<%=request.getContextPath()%>"/>	
				<select class="dxkf_display_none" id='tempOptions'></select>
				<select class="dxkf_display_none" id='rightSelectTemp'></select>
				<select class="dxkf_display_none" id='rightSelectTempAll'></select>
				<input type="hidden" id="depId" name="depId" value=""/>
				<input type="hidden" id="depName" name="depName" value=""/>
				<input type="hidden" id="user" name="user" value=""/>
				<input type="hidden" id="addType" name="addType" value="3"/>		
				<input type="hidden" id="pageIndex1" name="pageIndex1" value="1"/>
				<input type="hidden" id="totalPage1" name="totalPage1" value=""/>
				<table border="0" class="chooseBox">
					<tr>
						<td class="dxkf_td1" align="left" colspan="2">
						<div class="div_bd dxkf_div2">
						<select name="chooseType" id="chooseType" onchange="changeChooseType()">
							<option value="1" id="chooseType1"><emp:message key="dxkf_ydkf_xzfsdx_text_khtxl" defVal="客户通讯录" fileName="dxkf"></emp:message></option>
							<option value="2" id="chooseType2"><emp:message key="dxkf_ydkf_xzfsdx_text_grqz" defVal="个人群组" fileName="dxkf"></emp:message></option>
							<option value="3" id="chooseType2"><emp:message key="dxkf_ydkf_xzfsdx_text_khsx" defVal="客户属性" fileName="dxkf"></emp:message></option>
								<%
									if(StaticValue.getInniMenuMap().containsKey("/cwc_acctmanager.htm"))
									{
								%>
									<option value="4" id="chooseType4"><emp:message key="dxkf_ydkf_xzfsdx_text_wxyh" defVal="微信用户" fileName="dxkf"></emp:message></option>
								<%
									}
								%>
							<option value="5" id="chooseType5"><emp:message key="dxkf_ydkf_xzfsdx_text_qyyh" defVal="签约用户" fileName="dxkf"></emp:message></option>
						</select>
						</div>
						<div class="div_bd search_tx_choose">
						<table border="0">
						<tr>
						<td width="125px" height="22px">
						<input id="epname" name="epname" type="text" maxlength="16" class="graytext" value=""  onkeypress="if(event.keyCode==13) {zhijieSearch();event.returnValue=false;}"/>
						</td>
						<td width="28px">
					    <a onclick="zhijieSearch()"><img id="searchIcon" src="<%=skin%>/images/query.png" border="0"/></a>
						</td>
						</tr>
						</table>
					    </div>
						</td>
						<td class="dxkf_td2"><emp:message key="dxkf_ydkf_xzfsdx_text_sxzrs" defVal="所选总人数：" fileName="dxkf"></emp:message><label id="manCount">0</label></td>
					</tr>
					</table>
					<table border="0" class="chooseBox">
					<tr>
						<td>
							<table>
								<tr>
									<td>
										<div id="etree"  class="dept div_bd">
											<iframe id="sonFrame" name="sonFrame" frameborder="0" src="<%=iPath %>/kfs_clientDepTree.jsp?lguserid=<%=lguserid %>"></iframe>
										</div>
										<div id="egroup"  class="dept div_bd"></div>
										<div id="ecustfield"  class="dept div_bd"></div>
										<div id="wxuser"  class="dept div_bd"></div>
										<div id="ydywgroup"  class="dept div_bd"></div>
										<div class="div_bd div_bg dxkf_div3">
											<span id="showName">&nbsp;&nbsp;<emp:message key="dxkf_ydkf_xzfsdx_text_cylb" defVal="成员列表：" fileName="dxkf"></emp:message></span><span id="showUserName"><emp:message key="common_cAddressBook" defVal="当前通讯录：" fileName="common"></emp:message></span>
										</div>
										<div class="dept div_bd dxkf_div4" >
											<select  multiple name="left" id="left" size="15" onfocus="treeLoseFocus()" class="left_select_choose">
											</select>
										</div>
										
									</td>
								</tr>
							</table>
						</td>
						<td width="60" align="center">
						<br>
							<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="dxkf_common_opt_xuanze" defVal="选择" fileName="dxkf"></emp:message>"  onclick="javascript:router();">
							<br/>
							<br/>
							<input class="btnClass1" type="button" id="toRight" value="<emp:message key="dxkf_common_opt_shanchu" defVal="删除" fileName="dxkf"></emp:message>" onclick="javascript:moveRight();">
						</td>
						<td>
							<div id="rightDiv"   class="dept div_bd" >
								<select multiple name="right" id="right" size="20">
								</select>
								<ul id="getChooseMan">
								</ul>
							</div>
						</td>
						
					</tr>
					<tr>
					<td><div id="pageDiv">
										<input type="button" class="btnClass1" value="<emp:message key="dxkf_common_opt_prepage" defVal="上一页" fileName="dxkf"></emp:message>" id="lastPage1" onclick="javascript:goLastPage1()"/>
										<input type="button" class="btnClass1" value="<emp:message key="dxkf_common_opt_nextpage" defVal="下一页" fileName="dxkf"></emp:message>" id="nextPage1" onclick="javascript:goNextPage1()"/>
										<label id="showPage1"></label>
										<br/>
										</div></td>
					</tr>
				</table>
			</form>
		</div>
		</center>
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
   
   		<script language="javascript" src="<%=inheritPath %>/common/js/myjquery-a.js?V=116" type="text/javascript"></script>
		<script type="text/javascript" src="<%=inheritPath %>/common/widget/jqueryui/myjquery-j.js?V=116"></script>
		<script type="text/javascript" src="<%=inheritPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=116"></script>
		<script type="text/javascript" src="<%=inheritPath %>/common/js/jquery_Ul_Send.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/dxkf_<%=empLangName%>.js"></script>
        <script type="text/javascript" src="<%=inheritPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/clientChooseInfo_new.js?V=116"></script>
		<script type="text/javascript">
		var zTree;
		var ipathh = "<%=path %>";
		function aaa(){
			var types = $("#chooseType").attr("value");
			var manCount = parseInt($("#manCount").html());
			if(types == 2)
			{
				var tempVal="";
				var name ="";
				var groupStr= $(window.parent.document).find("#groupStr").val();
				if($("#groupList option:selected").size()==0)
				{
					// alert("请选择群组！");
					alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_choosegroup'));
					return;
				}
				$("#groupList option:selected").each(function() 
					{
					 	tempVal = $(this).val();
					 	name = $(this).text();
					 	if(groupStr.indexOf(","+tempVal+",") >= 0)
						{
							// alert("该群组已添加！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_gqzytj'));
							return;
						}else
						{
							if($(this).attr("mcount")=="0")
					    	{
								// alert("该群组下没有成员！");
								alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_mouserinthegroup'));
								return;
						    }
						    
							name = name +"  ["+ $(this).attr("mcount")+getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole')+"]";
							groupStr = groupStr+tempVal+",";
							$("#right").append("<option value='"+tempVal+"' et='4' mobile='' mcount='"+$(this).attr("mcount")+"'>[ " + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_group') + "] "+name+"</option>");
							$("#getChooseMan").append("<li dataval='"+tempVal+"' et='4' mobile='' mcount='"+$(this).attr("mcount")+"'>[ " + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_group') + "] "+name+"</li>");
							manCount +=parseInt($(this).attr("mcount"));
						}
					}
				);
				$("#manCount").html(manCount);
				$(window.parent.document).find("#groupStr").val(groupStr);
				return;
			}
			if(types == 5)
			{
				var tempVal="";
				var name ="";
				var groupStr= $(window.parent.document).find("#ydywGroupStr").val();
				if($("#ydywGroupList option:selected").size()==0)
				{
					// alert("请选择签约套餐！");
					alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_qxzqytc'));
					return;
				}
				$("#ydywGroupList option:selected").each(function() 
					{
					 	tempVal = $(this).val();
					 	name = $(this).text();
					 	if(groupStr.indexOf(","+tempVal+",") >= 0)
						{
							// alert("该签约套餐已添加！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_gqytcytj'));
							return;
						}else
						{
							if($(this).attr("mcount")=="0")
					    	{
								// alert("该签约套餐下没有成员！");
								alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_gqytcxmycy'));
								return;
						    }
						    
							name = name +"  ["+ $(this).attr("mcount") + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole')+"]";
							groupStr = groupStr+tempVal+",";
							$("#right").append("<option value='"+tempVal+"' et='9' mobile='' mcount='"+$(this).attr("mcount")+"'>[" + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_qytc') + "] "+name+"</option>");
							$("#getChooseMan").append("<li dataval='"+tempVal+"' et='9' mobile='' mcount='"+$(this).attr("mcount")+"'>[" + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_qytc') + "] "+name+"</li>");
							manCount +=parseInt($(this).attr("mcount"));
						}
					}
				);
				$("#manCount").html(manCount);
				$(window.parent.document).find("#ydywGroupStr").val(groupStr);
				return;
			}
			if(types ==3)
			{
				var tempVal="";
				var name ="";
				var groupStr= $(window.parent.document).find("#proIdStr").val();
				if($("#custfieldList option:selected").size()==0)
				{
					// alert("请选择客户属性！");
					alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_qxzkhsx'));
					return;
				}
				$("#custfieldList option:selected").each(function() 
					{
					 	tempVal = $(this).val();
					 	name = $(this).text();
					 	if(groupStr.indexOf(","+tempVal+",") >= 0)
						{
							// alert("该客户属性已添加！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_gkhsxytj'));
							return;
						}else
						{
							if($(this).attr("mcount")=="0")
					    	{
								// alert("该客户属性下没有成员！");
								alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_gkhsxxmycy'));
								return;
						    }
						    
							name = name +"  ["+ $(this).attr("mcount") + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole')+"]";
							groupStr = groupStr+tempVal+",";
							$("#right").append("<option value='"+tempVal+"' et='5' mobile='' mcount='"+$(this).attr("mcount")+"'>[ " + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_khsx') + "] "+name+"</option>");
							$("#getChooseMan").append("<li dataval='"+tempVal+"' et='5' mobile='' mcount='"+$(this).attr("mcount")+"'>[ " + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_khsx') + "] "+name+"</li>");
							manCount +=parseInt($(this).attr("mcount"));
						}
					}
				);
				$("#manCount").html(manCount);
				$(window.parent.document).find("#proIdStr").val(groupStr);
				return;
			}
			var depId;
			var depName;
			zTree = window.frames['sonFrame'].returnZTree();
			if(zTree.getSelectedNode()==null)
			{
				// alert("请选择机构！");
				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_choosedept'));
				return;
			}
			depId = zTree.getSelectedNode().id;
			depName = zTree.getSelectedNode().name;
			var rops = $("#right option");
			var depIdsExist= $(window.parent.document).find("#depIdStr").val();
			if(rops.length>0)
			{
				for(var i = 0;i<rops.length;i=i+1)
				{
					if(depId==rops.eq(i).attr("value") && rops.eq(i).attr("isdep") == 1)
					{
						// alert(rops[i].text + " 记录已添加！");
						alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_jlytj')+ " 记录已添加！");
						return;
					}
				}
			}
			var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
            var data = {lgcorpcode:lgcorpcode,depId:depId,depIdsExist:depIdsExist};
            EmpExecutionContext.log("/kfs_sendClientSMS.htm?method=getDep",data,"客户群组群发机构选择");
			$.post(ipathh+"/kfs_sendClientSMS.htm?method=getDep", data, function(result)
			{
				if(result=="depExist")
				{
					// alert("该机构已经包含在添加的机构内！");
					alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_gjgybhztjdjgn'));
					return;
			    }else if(result=="illegal"){
                    return;
                }
			    var ismut = 0;
			    // 点击“确定”包含所有子机构，“取消”只选择当前机构
			    if(confirm(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_ishavechilddept'))){
			    	ismut=1;
			    }
			    //检查要添加的机构是不是包含已经添加的机构，如果是则删除已经添加的子机构，如果不是则生成"[机构]...机构 (包含子机构)"
		    	$.post(ipathh+"/kfs_sendClientSMS.htm?method=isDepsContainedByDepB", 
			    	{
		    			//method : "isDepsContainedByDepB",
		    			depId : depId,
		    			lgcorpcode:lgcorpcode,
		    			depIdsExist : depIdsExist,
		    			depName : depName,
		    			ismut : ismut
	    			}, function(result2)
			    	{
				    	
				    	if(ismut==0)
	    				{
				    		if(result2=="0")
					    	{
								// alert("该机构下没有成员！");
								alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_nouserindept'));
								return;
						    }
						    
	    					$(window.parent.document).find("#depIdStr").val(depIdsExist+depId+",");
	    					$("#right").append("<option value=\'"+depId+"\' isdep='1'  et='2' mobile='' mcount='"+result2+"'>[ " + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_dept') + "] " + depName + " ["+result2 + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole') + "]</option>");
	    					$("#getChooseMan").append("<li dataval=\'"+depId+"\' isdep='1'  et='2' mobile='' mcount='"+result2+"'>[ " + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_dept') + "] " + depName + " ["+result2 + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole')+ "]</li>");
	    					manCount+=parseInt(result2);
	    					//return ;
	    	    		}else if(result2.indexOf("notContains")==0)
						{
							if(result2.substr(12)=="0")
							{
								// alert("该机构下没有成员！");
								alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_nouserindept'));
								return;
							}
							$(window.parent.document).find("#depIdStr").val(depIdsExist+"e"+depId+",");
							$("#right").append("<option value='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+result2.substr(12)+"'>[ " + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_dept') + "] "+depName+"(" + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_childdept')+")["+result2.substr(12) + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole') + "]</option>");
							$("#getChooseMan").append("<li dataval='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+result2.substr(12)+"'>[ " + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_dept') + "] "+depName+"(" + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_childdept') + ")["+result2.substr(12)+getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole') + "]</li>");
							manCount+=parseInt(result2.substr(12));
						}else{
							var strArr = result2.split(",");

							if(strArr[0]=="0")
							{
								// alert("该机构下没有成员！");
								alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_nouserindept'));
								return;
							}
							
							$(window.parent.document).find("#depIdStr").val(depIdsExist+"e"+depId+",");
							$("#right").append("<option value='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+strArr[0]+"'>[ " + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_dept') + "] "+depName+"(" + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_childdept')+")["+strArr[0] + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole') + "]</option>");
							$("#getChooseMan").append("<li dataval='"+depId+"' isdep='1'  et='3' mobile='' mcount='"+strArr[0]+"'>[ " + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_dept') + "] "+depName+"(" + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_childdept')+")["+strArr[0] + getJsLocaleMessage('dxkf','dxkf_dxqf_page2_pepole') + "]</li>");
							manCount+=parseInt(strArr[0]);
							for(var i=1;i<strArr.length;i=i+1)
							{
								var $aaa = $("#right").find("option[isdep='1'][value="+strArr[i]+"]");
								var $bbb = $("#getChooseMan").find("li[isdep='1'][dataval="+strArr[i]+"]");
								if($aaa.attr("et")==3)
								{
									var depidstr = $(window.parent.document).find("#depIdStr").val();
									$(window.parent.document).find("#depIdStr").val(depidstr.replace(",e"+$aaa.val()+",",","));
								}else if($aaa.attr("et")==2)
								{
									var depidstr = $(window.parent.document).find("#depIdStr").val();
									$(window.parent.document).find("#depIdStr").val(depidstr.replace(","+$aaa.val()+",",","));
								}
								manCount=manCount-$aaa.attr("mcount");
								$aaa.remove();
								$bbb.remove();
							}
						}
						$("#manCount").html(manCount);
				    }
				 );
			});
		}

		var curI = 1;
		function changeChooseType()
		{
			$("#left").html("");
			$('#pageIndex1,#totalPage1').val(1);
			$('#showPage1').html('');
			var i=$("#chooseType").val();
			if( curI == i)
			{
				return;
			}else
			{
				curI = i;
			}
			var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
			$("#etree").css("display","none");
			$("#egroup").css("display","none");
			$("#wxuser").css("display","none");
			$("#ydywgroup").css("display","none");
			$("#ecustfield").css("display","none");
			$("#pageDiv").css("visibility","hidden");
			if(i == 1)
			{
				$("#pageDiv").css("visibility","visible");
				$("#etree").css("display","block");
			}else if(i == 2)
			{
				$("#egroup").css("display","block");
                $("#pageDiv").css("visibility","visible");
				if($("#egroup").html() == "")
				{
					$.post("<%=path%>/kfs_sendClientSMS.htm?method=getGroupList",{
						//method:"getGroupList" ,
						lguserid:$(window.parent.document).find("#lguserid").val()
			   			},function(GroupList)
			   			{
							$("#egroup").html(GroupList);
						}
					);
				}else
				{
					grouponChange();
				}
			}else if(i == 3){
				$("#ecustfield").css("display","block");
				if($("#ecustfield").html() == "")
				{
					$.post("<%=path%>/kfs_sendClientSMS.htm?method=getProsList",{
						//method:"getProsList" ,
						lguserid:$(window.parent.document).find("#lguserid").val(),
						lgcorpcode:lgcorpcode
			   			},function(GroupList)
			   			{
							$("#ecustfield").html(GroupList);
						}
					);
				}else
				{
					custfieldonChange();
				}
			}else if(i == 4){
				$("#wxuser").css("display","block");
				var epname = $("#epname").val();
				$.post("<%=path%>/kfs_sendClientSMS.htm?method=getwxuser",{
					//method:"getwxuser" ,
					lgcorpcode:lgcorpcode,
					ename:epname
		   			},function(wxuser)
		   			{
						$("#left").html(wxuser);
					}
				);
			}else if(i==5){
			    $("#pageDiv").css("visibility","visible");
				$("#ydywgroup").css("display","block");
				if($("#ydywgroup").html() == "")
				{
					$.post("<%=path%>/kfs_sendClientSMS.htm?method=getYdywGroupList",{
						//method:"getYdywGroupList" ,
						lguserid:$(window.parent.document).find("#lguserid").val()
			   			},function(GroupList)
			   			{
							$("#ydywgroup").html(GroupList);
						}
					);
				}else
				{
					ydywGrouponChange();
				}
			}
		}

		function ydywGrouponChange(){
				var tccode=$("#ydywGroupList").val();
				var epname = $("#epname").val();
				$("#pageIndex1").val(1);
				var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
				$.post("<%=path%>/kfs_sendClientSMS.htm?method=getYdywGroupMember",{
					//method:"getYdywGroupMember" ,
					tccode:tccode ,
					epname : epname,
					lgcorpcode:lgcorpcode
		   			},function(groupMember){
		   				//第二个@出现位置的索引
		   				var index = groupMember.indexOf("@",groupMember.indexOf("@")+1);
		   				var tempStr = groupMember.substring(0,index);
						var strs =new Array();
						strs =  tempStr.split("@");
						$("#totalPage1").val(strs[1]);
						$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
						var tempStr2 = groupMember.substring(index+1);
						$("#left").html(tempStr2);
						//没有签约人员信息并且搜索条件不为空，则证明没有搜索到签约用户，提示用户
						if(tempStr2==""&&epname!=""){
							// alert("没有搜索到签约用户！");
							alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_nosignuser'));
						}
					}
				);
		}

		function grouponChange(pageIndex)
		 {
             var udgId=$('#groupList').val() || $('#groupList option[data-sel]').val();
             if(!udgId){return;}
             //标记选中的option 防止点击其他控件之后丢失选中状态 导致翻页获取不到之前选中的option
             var $option = $('#groupList option[value="'+udgId+'"]');
             $option.attr('data-sel','').siblings().removeAttr('data-sel');
             $option.attr('selected',true);
             //默认第一页
             pageIndex = pageIndex || 1;
            var epname = $("#epname").val();
             //记录当前页到页面
             $("#pageIndex1").val(pageIndex);
             var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
            $.post("<%=path%>/kfs_sendClientSMS.htm?method=getGroupMember&udgId="+udgId,{
                epname : epname,
                pageIndex : pageIndex,
                lgcorpcode:lgcorpcode
                },function(groupMember){
                //第二个@出现位置的索引
                var index = groupMember.indexOf("@",groupMember.indexOf("@")+1);
                var tempStr = groupMember.substring(0,index);
                var strs =  tempStr.split("@");
                $("#totalPage1").val(strs[1]);
                $("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
                var tempStr2 = groupMember.substring(index+1);
                $("#left").html(tempStr2);
                }
            );
		 }
		 
		 function custfieldonChange()
		 {
            var epname = $("#epname").val();
            var fieldId = $("#custfieldList option:selected").attr('fieldId');
             if(!fieldId){return;}
            var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();

            $.post("<%=path%>/kfs_sendClientSMS.htm?method=getCustFieldMember",{
                epname : epname,
                fieldId:fieldId,
                lgcorpcode:lgcorpcode
                },function(groupMember){
                    $("#left").html(groupMember);
                }
            );
		 }


		 //搜索按钮
		 function zhijieSearch()
		 {
			 var types = $("#chooseType").val();
			 var dqcheck;
			 var epname = $("#epname").val();
			 var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
			 if(types == 1)
			 {
				$("#left").empty();
				//var depId = $("#depId").val();
				var depId = "";
		        if(epname == null || $.trim(epname).length==0)
		        {
		        	return;
		       	}
		        var addTypes = $("#addType").val();
				$.post(ipathh+"/kfs_sendClientSMS.htm?method=getDepAndClientTree1", 
					{
						//method:"getDepAndClientTree1", 
						epname : epname,
						lgcorpcode:lgcorpcode,
						depId :depId,
						addTypes:addTypes
					}, function(result){
					$("#left").html(result);
				});
			 }
			 else if(types == 2)
			 {
				 grouponChange();
			 }else if(types == 3){
			 	custfieldonChange();
			 }else if(types == 4){
				$.post("<%=path%>/kfs_sendClientSMS.htm?method=getwxuser",{
					//method:"getwxuser" ,
					lgcorpcode:lgcorpcode,
					ename:epname
		   			},function(wxuser)
		   			{
						$("#left").html(wxuser);
					}
				);
			 }else{
			 	ydywGrouponChange();
			 }
		 }
			
		function goLastPage1()
		{
		    var types = $("#chooseType").attr("value"); 
		    var epname = $("#epname").val();
		    var tempVal= $("#ydywGroupList").val()+",";
		    
			var depId=$("#depId").val();
			var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
			var lguserid = $(window.parent.document).find("#lguserid").val();
			if(types==1&&depId=="")
			{
				// alert("请先选择机构！");
				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_choosedept'));
				return;
			}else if(types==5&&tempVal.length<2){
				// alert("请先选择业务套餐！");
				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_chooseywtc'));
				return;
			}
			var pageIndex1 = $("#pageIndex1").val();
			if(pageIndex1=="1")
			{
				// alert("第一页，没有上一页了！");
				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_noprepage'));
				return;
			}
			if(types==1){
				$.post("<%=path %>/kfs_sendClientSMS.htm?method=getDepAndClientTree1", {lguserid:lguserid,lgcorpcode:lgcorpcode,pageIndex1:pageIndex1,depId:depId,opType:"goLast"}, function(result)
				{
					$("#left").html(result);
					$("#pageIndex1").val(parseInt(pageIndex1)-1);
					$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
				});
			}else if(types==2){
                grouponChange(parseInt(pageIndex1)-1);
            }else{
				$.post("<%=path %>/kfs_sendClientSMS.htm?method=getYdywGroupMember",{tccode:tempVal,epname:epname,lguserid:lguserid,pageIndex1:pageIndex1,opType:"goLast"},function(result)
		   			{
					   	//第二个@出现位置的索引
	   					var index = result.indexOf("@",result.indexOf("@")+1);
						$("#left").html(result.substring(index+1));
						$("#pageIndex1").val(parseInt(pageIndex1)-1);
						$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
					});
			}
		}
		function goNextPage1()
		{
			var types = $("#chooseType").attr("value"); 
		    var epname = $("#epname").val();
		    var tempVal= $("#ydywGroupList").val()+",";
		    
			var depId=$("#depId").val();
			var lgcorpcode = $(window.parent.document).find("#lgcorpcode").val();
			var lguserid = $(window.parent.document).find("#lguserid").val();
			if(types==1&&depId=="")
			{
				// alert("请先选择机构！");
				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_choosedept'));
				return;
			}else if(types==5&&tempVal.length<2){
				// alert("请先选择业务套餐！");
				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_chooseywtc'));
				return;
			}
			var pageIndex1 = $("#pageIndex1").val();
			var totalPage1 = $("#totalPage1").val();
			if(pageIndex1==totalPage1)
			{
				// alert("已经最后一页了！");
				alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_nonextpage'));
				return;
			}
			if(types==1){
				$.post("<%=path %>/kfs_sendClientSMS.htm?method=getDepAndClientTree1", {lguserid:lguserid,lgcorpcode:lgcorpcode,pageIndex1:pageIndex1,depId:depId,opType:"goNext"}, function(result)
				{
					$("#left").html(result);
					$("#pageIndex1").val(parseInt(pageIndex1)+1);
					$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
				});
			}else if(types==2){
                grouponChange(parseInt(pageIndex1)+1);
            }else{
				$.post("<%=path %>/kfs_sendClientSMS.htm?method=getYdywGroupMember",{tccode:tempVal,epname:epname,lguserid:lguserid,pageIndex1:pageIndex1,opType:"goNext"},function(result)
					{
					   	//第二个@出现位置的索引
	   					var index = result.indexOf("@",result.indexOf("@")+1);
						$("#left").html(result.substring(index+1));
						$("#pageIndex1").val(parseInt(pageIndex1)+1);
						$("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
					});
			}
		}
		</script>
	</body>
</html>