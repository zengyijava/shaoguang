<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.form.LfFomField"%>
<%@page import="com.montnets.emp.entity.form.LfFomInfo"%>
<%@page import="com.montnets.emp.entity.form.LfFomQuestion"%>
<%@page import="com.montnets.emp.entity.form.LfFomType"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("formManager");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
		
	// 请求类型    创建   add   编辑 edit 查看  preview
	String handletype = (String)request.getAttribute("handletype");
	boolean isshow = true;
	//查看 的状态  将屏蔽按钮操作
	if("preview".equals(handletype)){
	    isshow = false;
	}
	
	//表单信息
	LfFomInfo otFomInfo = (LfFomInfo)request.getAttribute("otFomInfo");
	//表单ID
	String fomid = "";
	//表单标题
	String form_title = "";
	//表单描述
	String form_note = "";
	if(otFomInfo != null && !"add".equals(handletype) && !"defined".equals(handletype)){
	    fomid = String.valueOf(otFomInfo.getFId());
	    form_title = otFomInfo.getTitle();
		form_note = otFomInfo.getNote();
	}
	//当edit状态时 ,获取问题
	@ SuppressWarnings("unchecked")
	List<LfFomQuestion> questionList = (List<LfFomQuestion>)request.getAttribute("questionList");
	int questioncount = 0;
	if(questionList != null){
	    questioncount =  questionList.size();
	}
	//当edit状态时 ,获取选项\
	@ SuppressWarnings("unchecked")
	List<LfFomField> fomFieldList = (List<LfFomField>)request.getAttribute("fomFieldList");
	int optioncount = 0;
	if(fomFieldList != null){
	    optioncount =  fomFieldList.size();
	}	
	
	//系统表单类型
	@ SuppressWarnings("unchecked")
	List<LfFomType> fomTypeList = (List<LfFomType>)request.getAttribute("fomTypeList");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=iPath%>/css/saveForm.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=commonPath%>/common/css/global.css?V=<%=StaticValue.getJspImpVersion() %>">
			<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<div id="container" class="container" style="height: auto;">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" style="height: auto;">
				<input type="hidden" id="pathUrl" value="<%=path %>"/>
				
				<input type="hidden" id="iPath" value="<%=iPath %>"/>
				<%-- 操作类型 --%>
				<input type="hidden" id="handletype" value="<%=handletype %>"/>
				<%-- 表单 ID --%>
				<input type="hidden" id="formid" value="<%=fomid %>"/>
				<div id="getloginUser" style="padding:5px;display:none;">
				</div>
				<div class="itemDiv">
					<table class="div_bg div_bd" style="height:23px;width: 566px;">
					   		<tr>
								<td>
									&nbsp;&nbsp;<emp:message key="wzgl_qywx_form_text_1" defVal="表单名称："
											fileName="wzgl" />
								</td>
								<td width="468px">
								<input class="graytext input_bd" style="width:468px;border-top: 0;border-bottom: 0;border-right: 0;" type="text" 
									name="form_title" id="form_title"  value='<%=form_title %>' 
									maxlength="64"   <%if(!isshow){%> readonly="readonly"  <%}%>/>
								</td>
							</tr>
					</table>
				</div>
	
				
				<% 
					if("defined".equals(handletype)){
				%>
				<div class="itemDiv" style="margin-top:10px">
					<table class="div_bg div_bd" style="height:23px;width: 566px;">
							 <tr>
								<td>
									&nbsp;&nbsp;<emp:message key="wzgl_qywx_form_text_15" defVal="表单类型："
											fileName="wzgl" />
								</td>
								<td width="468px">
									<select name="selectFormType" id="selectFormType">
										<%
											if(fomTypeList != null && fomTypeList.size() > 0){
											    LfFomType fomtype = null;
											    String name = "";
											    for(int m=0;m<fomTypeList.size();m++){
											        fomtype = fomTypeList.get(m);
											        name = fomtype.getName();
											        if(name != null && !"".equals(name) && name.length() > 25){
											            name = name.substring(0,25)+"...";
											        }else if(name != null && !"".equals(name) && name.length() <= 25){
											            
											        }else{
											            name = "-";
											        }
										%>
											<option value="<%=fomtype.getTypeId() %>" >
												<%=name.replaceAll("<","&lt;").replaceAll(">","&gt;") %></option>
										<%
											    }
											}
										%>
									</select>
								</td>
							</tr>
					</table>
				</div>
				<% 
					} 
				%>
				
				<div class="itemDiv" style="margin-top:10px">
					<table class="div_bg div_bd" style="height:23px;width: 566px;">
							 <tr>
								<td>
									&nbsp;&nbsp;<emp:message key="wzgl_qywx_form_text_16" defVal="表单描述："
											fileName="wzgl" />
								</td>
								<td width="468px">
								<input class="graytext input_bd" style="width:468px;border-top: 0;border-bottom: 0;border-right: 0;" type="text" 
									name="form_note" id="form_note"  value='<%=form_note%>' 
									maxlength="256"  <%if(!isshow){%> readonly="readonly"  <%}%>/>
								</td>
							</tr>
					</table>
				</div>
				
				
				
					<% 
						//判断是否有问题list
						if(questionList != null && questionList.size()>0 && !"add".equals(handletype)  && !"defined".equals(handletype)){
						    //问题实体类
						    LfFomQuestion fomQuestion = null;
						    //选项实体类
						    LfFomField fomField = null;
						    //问题的数目num的跟踪    循环一个问题加1
						    int question_num = 0;
						    //选项的数目count的跟踪    循环一个选项加1
						    int option_count = 0;
							//问题的DIV
							String question_num_div = "";
							//该问题对应的标签类型    1 单选   2多选  ,问题的标签类型ID
							String widget_type_num = "";
							//默认问题的标签类型是 单项  值
							String widget_type = "1";
							//问题的标题
							String title_num = "";
							//某个问题的增加选项的按钮ID
							String add_btn_tr_num = "";
							 //选项行 ID
							String tr_widget_option_count = "";
							// 单选ID
							String radio_option_count = "";
							//输入框ID
							String r_input_option_count = "";
							//问题radio的name属性  每一个radio类型的问题一个
							String radio_option_name_num = "";
							// 多选ID
							String checkbox_option_num = "";
							//输入框ID
							String c_input_option_count = "";
							//循环问题
						    for(int i=0;i<questionList.size();i++){
						        //取一个问题
						        fomQuestion = questionList.get(i);
						        //问题数目加1  ,页面唯一 序列自增
						        question_num = question_num + 1;
						        //初始化问题ID
						        question_num_div = "question_div_"+question_num;
						        //该问题的标签ID
						        widget_type_num = "widget_type_"+question_num;
						        //该问题是  1单 选    2双选
						        widget_type = fomQuestion.getFiledType();
						        //该问题的标题
						        title_num = "title_"+question_num;
						        //该问题的新增选项的ID
						        add_btn_tr_num = "add_btn_tr_"+question_num;
						        %>
						        <div class='itemDiv' sign='question' style='padding-top: 10px;height: auto;' id='<%=question_num_div %>' questionnum='<%=question_num %>'>
							        <input type='hidden' value='<%=widget_type %>' id='<%=widget_type_num %>'>
							        <table class='div_bg div_bd' style='height:120px;width:566px;' border='1'>
								        	<tr tr_count='-1'><td style='height:35px;'  align='center'><emp:message key="wzgl_qywx_form_text_17" defVal="问题"
											fileName="wzgl" /></td>
								        		<td width='468px'>
								        			<input class='graytext input_bd' style='width:360px;border-top: 0;border-bottom: 0;border-right: 0;' 
								        			name='<%=title_num %>' id='<%=title_num %>'  value='<%=fomQuestion.getTitle() %>' 
								        			maxlength='64'  <%if(!isshow){%> readonly="readonly"  <%}%>/> 
								        			<% 
								        				if(isshow){
								        			%>
								        				<%--
								        					<input type='button' value='删除' onclick='delQuestion("<%=question_num %>")'>
								        				--%>
								        				<img src="<%=iPath%>/img/deletex.png" onclick="delQuestion('<%=question_num %>')"  
								        				title='<emp:message key="wzgl_qywx_form_text_18" defVal="删除问题"
											fileName="wzgl" />' style="float: right;margin-right:20px;cursor: pointer;">
																&nbsp;&nbsp;
								        			<% 
								        				}
								        			%>
								        			</td>
								        	</tr>	
					      				 <%
					      				 	
					      				  if(fomFieldList != null && fomFieldList.size() > 0){
					      				     //循环问题选项
					      				 	 for(int j=0;j<fomFieldList.size();j++){
					      				 	      //取一个选项
					      				 		  fomField = fomFieldList.get(j);
					      				 	      //判断该选项是否属于这个问题
					      				 		  if(fomField.getQid().toString().equals(fomQuestion.getQId().toString())){
					      				 		    //将选项的数目+1
					      				 		 	option_count = option_count + 1;
					      				 		    //该选项的行ID
					      				 		 	tr_widget_option_count = "tr_widget_option_"+option_count;
				      				 	 			if("1".equals(widget_type)){
				      				 	 				// 单选ID
					      				 	 			radio_option_count = "radio_option_"+option_count;
					      				 	 			//输入框ID
					      				 	 			r_input_option_count = "r_input_option_"+option_count;
					      				 	 			//问题radio的name属性  每一个radio类型的问题一个
					    								radio_option_name_num = "radio_option_name_"+question_num;
								      				 %>
														  	<tr id='<%=tr_widget_option_count %>' tr_count='<%=option_count %>'>
														  		<td style='height:18px;' align='center'>
																<input name='<%=radio_option_name_num %>'  id='<%=radio_option_count %>' 
																type='radio' value='' />
																</td>
																<td width='468px'>
																<input class='graytext input_bd' 
																style='width:240px;border-top: 0;border-bottom: 0;border-right: 0;'
																name='<%=r_input_option_count %>' id='<%=r_input_option_count %>'  
																value='<%=fomField.getFieldValue() %>' maxlength='32'  <%if(!isshow){%> readonly="readonly"  <%}%>/>
																		<% 
											        						if(isshow){
													        			%>
													        			<%--
													        				<img src="<%=iPath%>/img/add.png" onclick="addOption('<%=question_num %>','<%=option_count %>')" 
																			title="增加"  class="addpic">&nbsp;&nbsp;
																			<img src="<%=iPath%>/img/del.png" onclick="delOption('<%=question_num %>','<%=option_count %>')"  
																			title="删除"  class="delpic">--%>&nbsp;&nbsp;
																			<a href="javascript:delOption('<%=question_num %>','<%=option_count %>');" 
																					class="nav_opr_btn" title="<emp:message key="common_btn_5" defVal="删除"
																				fileName="common" />">
																	       			<span class="opr_icon16 del_icon"></span>
																			</a>
																			 <a href="javascript:addOption('<%=question_num %>','<%=option_count %>');" 
																			 		class="nav_opr_btn" title="<emp:message key="wzgl_qywx_form_text_24" defVal="增加"
																				fileName="common" />">
																			       <span class="opr_icon16 add_icon"></span>
																			</a>
																		<% 
													        				}
										        						%>
																</td>
															</tr>
								       				 <%
				      				 	 					}else if("2".equals(widget_type)){
				      				 	 					// 多选ID
				      				 	 					 checkbox_option_num = "checkbox_option_"+question_num;
				      				 	 					//输入框ID
				      				 	 					 c_input_option_count = "c_input_option_"+option_count;
								       				 %>
							      				 		 	<tr id='<%=tr_widget_option_count %>' tr_count='<%=option_count %>'>
							      				 		 		<td style='height:18px;' align='center'>
																	<input type='checkbox' name='<%=checkbox_option_num %>' class='select_check' value=''/>
																</td>
																<td width='468px'>
																	<input class='graytext input_bd' style='width:240px;border-top: 0;border-bottom: 0;border-right: 0;'
																	name='<%=c_input_option_count %>' id='<%=c_input_option_count %>'  
																	value='<%=fomField.getFieldValue() %>' maxlength='32'  <%if(!isshow){%> readonly="readonly"  <%}%>/>
																		<%--
																			<a href='javascript:delOption("<%=option_count %>")'>删除</a>&nbsp;&nbsp;
																		--%>
																		<% 
											        						if(isshow){
													        			%>
													        				<%--
																			<img src="<%=iPath%>/img/add.png" onclick="addOption('<%=question_num %>','<%=option_count %>')" 
																			title="增加" class="addpic">
																			&nbsp;&nbsp;
																			<img src="<%=iPath%>/img/del.png" onclick="delOption('<%=question_num %>','<%=option_count %>')" 
																			title="删除"  class="delpic">
																			 --%>&nbsp;&nbsp;
																			<a href="javascript:delOption('<%=question_num %>','<%=option_count %>');" 
																					class="nav_opr_btn" title="<emp:message key="common_text_8" defVal="删除"
											fileName="common" />">
																	       			<span class="opr_icon16 del_icon"></span>
																			</a>
																			 <a href="javascript:addOption('<%=question_num %>','<%=option_count %>');" 
																			 		class="nav_opr_btn" title="<emp:message key="wzgl_qywx_form_text_24" defVal="增加"
											fileName="wzgl" />">
																			       <span class="opr_icon16 add_icon"></span>
																			</a>
																		<% 
													        				}
										        						%>
																</td>
															</tr>
							      				 	 <%
							      				 	 			}
				      				 	 					}
							      				 		}
							      				  }
							       				 %>
							       				 
							       	</table>
						        </div>
						     <%
						    }
						}
				%>
				
				<% 
       				if(isshow){
       			%>
				<div class="itemDiv" style="height: 30px;padding-top: 10px;" id="add_option">
					<table class="div_bg div_bd" style="width: 566px;height: 45px;line-height:45px">
					   		<tr>
								<td align="center" style="text-indent:10px;">
										<input name="isCheckForm"  id="isCheckFormSingle" type="radio" value="1" checked="checked"/>
									<emp:message key="wzgl_qywx_form_text_25" defVal="单选题"
											fileName="wzgl" /> &nbsp;&nbsp;&nbsp;
									 <input name="isCheckForm" id="isCheckFormMulit" type="radio" value="2" />
									<emp:message key="wzgl_qywx_form_text_26" defVal="多选题"
											fileName="wzgl" />&nbsp;
									<%-- <a href="javascript:addQuestion()">增加新问题</a> --%>
									&nbsp;&nbsp;&nbsp;
									<img src="<%=iPath%>/img/jiahao.jpg" onclick="addQuestion()" title='<emp:message key="wzgl_qywx_form_text_18" defVal="增加问题"
											fileName="wzgl" />' style="cursor: pointer;">
									
								</td>
							</tr>
					</table>
				</div>
				<% 
       				}
       			%>
				
				<div class="itemDiv " style="height: 30px;padding-top:25px;">
					<table style="width: 566px;height: 35px;">
					   		<tr>
								<td align="right">
										<% 
					        				if(isshow){
					        			%>
										<input type="button" id="savebtn" value="<emp:message key="common_btn_8" defVal="保存"
											fileName="common" />" class="btnClass5 mr10" 
										onclick="javascript:saveForm('<%=handletype %>');" />&nbsp;&nbsp;
										<% 
					        				}
					        			%>
										<input type="button" id="cancelbtn" value="<emp:message key="common_btn_10" defVal="返回"
											fileName="common" />" class="btnClass6" onclick="javascript:back('<%=handletype %>');" />
								</td>
							</tr>
					</table>
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
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wzgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	  	<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    	<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/artDialog.js?skin=default"></script>
  		<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script language="javascript"  src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=iPath%>/js/handleForm.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
		$('#selectFormType').isSearchSelect({'width':'120','isInput':false,'zindex':0});
		$(document).ready(function() {
		      getLoginInfo("#getloginUser");
			  $("tr").hover(function(){
			    $(this).addClass('delpic_hoverBg');
			  },function(){
			    $(this).removeClass('delpic_hoverBg');
			  });
		});
		//问题DIV的数量统计
		var num = <%=questioncount%>;
		//问题中选项的数量统计(整个页面)
		var count = <%=optioncount%>;
		</script>
	</body>
</html>