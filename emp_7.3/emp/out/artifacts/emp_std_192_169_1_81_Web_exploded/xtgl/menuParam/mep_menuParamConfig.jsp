<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.entity.system.LfBusProcess"%>
<%@page import="com.montnets.emp.entity.corp.LfCorp"%>
<%@page import="com.montnets.emp.entity.tailnumber.LfSubnoAllot"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.sysuser.LfReviewSwitch"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.entity.corp.LfCorpConf"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.system.LfGlobalVariable"%>
<%@page import="com.montnets.emp.entity.tailmanage.GwTailctrl"%>
<%@page import="com.montnets.emp.entity.tailmanage.GwMsgtail"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String corpCode = request.getParameter("lgcorpcode");
	LfCorp corp = (LfCorp)session.getAttribute("loginCorp");
	LfSysuser sysuser = (LfSysuser)session.getAttribute("loginSysuser");
	
	@ SuppressWarnings("unchecked")
	List<LfReviewSwitch> swirchList=(List<LfReviewSwitch>)request.getAttribute("swirchList");
	List<LfCorpConf> lfCorpConfList=(List<LfCorpConf>)request.getAttribute("corpConf");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	String isShow = request.getAttribute("isShow").toString();
	//判断审批流是否开启  2.表示关闭  3.表示开启
	int isOpenFlow=2;
	if(swirchList != null && swirchList.size() > 0)
	{
		for(LfReviewSwitch swirch : swirchList)
		{
			if(swirch.getSwitchType()==1)
			{
				isOpenFlow=3;
				break;
			}
		}
	}
	String bit=""; 
	String number="";
	String character ="";
	String sign="";
	String modiftime="";
	String remind="";
	String errorlimt="";
	String logtime = "";
	String dynpwd = "";
	String isEditor = "";
	String protocol ="";
	String host = "";
	String port = "";
	String username= "";
	String password = "";
	String name= "";
	for(int i=0;i<lfCorpConfList.size();i++){
	LfCorpConf conf=lfCorpConfList.get(i);
	//位数
	
	if("pwd.count".equals(conf.getParamKey())){
	bit=conf.getParamValue();
	}
	//组合形式
	if("pwd.combtype".equals(conf.getParamKey())){
		String cunt=conf.getParamValue();
		if(cunt!=null){
		String[] per= cunt.split(",");
		if(per.length==1){
		number=per[0];
		}else if(per.length==2){
		number=per[0];
		character=per[1];
		}else if(per.length==3){
		number=per[0];
		character=per[1];
		sign=per[2];
		}
		}
	}
	//修改周期
	if("pwd.upcycle".equals(conf.getParamKey())){
		 modiftime=conf.getParamValue();;

	}
	//过期提醒
	if("pwd.pastalarm".equals(conf.getParamKey())){
		 remind=conf.getParamValue();;

	}
	//错误上限
	if("pwd.errlimit".equals(conf.getParamKey())){
		 errorlimt=conf.getParamValue();;

	}
	
	//日志保留时间
	LfGlobalVariable globalV = (LfGlobalVariable)request.getAttribute("globaV");	
	logtime = globalV.getGlobalValue().toString();
	
	//模板是否可编辑	
	isEditor = request.getAttribute("isEditor").toString();
	
	//手机动态口令
	if("pwd.dynpwd".equals(conf.getParamKey())){
		dynpwd=conf.getParamValue();;
	}
	
	//新增邮件配置
	if("email.protocol".equals(conf.getParamKey())){
		protocol=conf.getParamValue()==null?"":conf.getParamValue();
	}
	if("email.host".equals(conf.getParamKey())){
		host=conf.getParamValue()==null?"":conf.getParamValue();
	}
	if("email.port".equals(conf.getParamKey())){
		port=conf.getParamValue()==null?"":conf.getParamValue();
	}
	if("email.username".equals(conf.getParamKey())){
		username=conf.getParamValue()==null?"":conf.getParamValue();
	}
	if("email.password".equals(conf.getParamKey())){
		password=conf.getParamValue()==null?"":conf.getParamValue();
	}
	if("email.name".equals(conf.getParamKey())){
		name=conf.getParamValue()==null?"":conf.getParamValue();
	}
	
}
	if(modiftime==null||"null".equals(modiftime)){
		modiftime="";
	}
	if(errorlimt==null||"null".equals(errorlimt)){
		errorlimt="";
	}

	if(bit==null||"null".equals(bit)){
		bit="";
	}
	if(number==null||"null".equals(number)){
		number="";
	}
	if(character==null||"null".equals(character)){
		character="";
	}
	 if(sign==null||"null".equals(sign)){
		sign="";
	}
	
	if(remind==null||"null".equals(remind)){
		remind="";
	}
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get(request.getAttribute("rTitle"));
	
	int isBalance = corp.getIsBalance();
	
	int subnoLen = corp.getSubnoDigit();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String openTab = request.getParameter("openTab");
	
	//EMP标准版6.1 贴尾全局配置 新增
	String content = "";
	int othertailflag = -1;
	int overtailflag = -1;
	GwTailctrl tailctrl = (GwTailctrl)request.getAttribute("tailctrl");
	if(tailctrl != null){
		othertailflag = tailctrl.getOthertailflag();
		overtailflag = tailctrl.getOvertailflag();
	}
	GwMsgtail msgtail = (GwMsgtail)request.getAttribute("msgtail");
	if(msgtail != null){
		content = msgtail.getContent();
	}
	//end

    //签名同步状态标示 0：关闭  1：开启
    String actsyncflag = "0";
    if(request.getAttribute("actsyncflag") != null){
        actsyncflag = request.getAttribute("actsyncflag").toString();
    }
	String isOpen="0";
	if(request.getAttribute("isOpen") != null){
		isOpen = request.getAttribute("isOpen").toString();
	}	
	String cmdstr="";
	if(request.getAttribute("cmdstr") != null){
		cmdstr = request.getAttribute("cmdstr").toString();
	}	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
<title><%=titleMap.get(menuCode) %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/common.jsp" %>
<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
      type="text/css" />
<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
      type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=commonPath%>/common/css/tabs.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
      type="text/css" />
<link rel="stylesheet"
	href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
<link rel="stylesheet"
      href="<%=commonPath%>/common/css/c_param_config.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">

		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
    <%--<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.JSP_IMP_VERSION%>" />--%>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mep_menuParamConfig.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/mep_menuParamConfig.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
</head>
<body id="mep_menuParamConfig">
	<div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<div class="hiddenValueDiv2" id="hiddenValueDiv2"></div>
		<input class="skin" id="skin" value="<%=skin%>" type="hidden"/>
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
			<div id="PconfigDiv" class="PconfigDiv">

				<div id="tabContainer3" onclick="javascript:bycheck(3)"
					class="div_bg tabCont">
					<label><emp:message key="xtgl_cswh_mkcspz_mmaqsz" defVal="密码安全设置" fileName="xtgl"/></label> <a id="img3"
						class="<%=!"1".equals(openTab)?"un":"" %>fold fold-a">&nbsp;&nbsp;&nbsp;&nbsp;</a>
				</div>

				<div id="tabBody3"
				<%if(!"1".equals(openTab)){out.print("class='tabBody3_1'");}else{out.print("class='tabBody3_2'");}%>>
					<table class="tabBody">
						<tr>
							<td <%if(StaticValue.ZH_HK.equals(langName)){out.print("class='bit_up_td_1'");}else{out.print("class='bit_up_td_2'");}%> ><emp:message key="xtgl_cswh_mkcspz_wsyq_mh" defVal="位数要求：" fileName="xtgl"/></td>
							<td><input type="text" name="bit" id="bit" class="type_input"
								onkeyup="numberControl($(this))" onblur="numberControl($(this))"
								maxlength="8" value="<%=bit%>" />&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_wys" defVal="位以上" fileName="xtgl"/></td>
						</tr>

						<tr>
							<td <%if(StaticValue.ZH_HK.equals(langName)){out.print("class='zhxs_td_1'");}else{out.print("class='zhxs_td_2'");}%> ><emp:message key="xtgl_cswh_mkcspz_zhxs_mh" defVal="组合形式：" fileName="xtgl"/></td>
							<td><emp:message key="xtgl_cswh_mkcspz_zsbk" defVal="至少包括" fileName="xtgl"/>&nbsp;&nbsp;&nbsp;&nbsp; <input type="checkbox"
								name="number" id="number" <%="".equals(number)?"":"checked" %> />
								&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_sz" defVal="数字" fileName="xtgl"/> &nbsp;&nbsp; <input type="checkbox"
								name="character" id="character"
								<%="".equals(character)?"":"checked" %> />&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_zm" defVal="字母" fileName="xtgl"/>
								&nbsp;&nbsp; <input type="checkbox" name="sign" id="sign"
								<%="".equals(sign)?"":"checked" %> />&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_fh" defVal="符号" fileName="xtgl"/>&nbsp;&nbsp;
							</td>
						</tr>

						<tr>
							<td <%if(StaticValue.ZH_HK.equals(langName)){out.print("class='xgzq_td_1'");}else{out.print("class='xgzq_td_2'");}%> ><emp:message key="xtgl_cswh_mkcspz_xgzq_mh" defVal="修改周期：" fileName="xtgl"/></td>
							<td><input type="text" name="modiftime" id="modiftime" class="type_input"
								onkeyup="numberControl($(this))" onblur="isShow(this)"
								maxlength="8" value="<%=modiftime%>" />&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_t" defVal="天" fileName="xtgl"/></td>
						</tr>
						<tr>
							<td <%if(StaticValue.ZH_HK.equals(langName)){out.print("class='gqtx_td_1'");}else{out.print("class='gqtx_td_2'");}%> ><emp:message key="xtgl_cswh_mkcspz_gqtx_mh" defVal="过期提醒：" fileName="xtgl"/></td>
							<% 
      			 	if(modiftime!=null&&!"".equals(modiftime)&&Integer.parseInt(modiftime)!=0){%>
							<td><input type="text" name="remind" id="remind" class="type_input"
								onkeyup="numberControl($(this))" onblur="isBig(this)"
								maxlength="8" value="<%=remind%>" />&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_t" defVal="天" fileName="xtgl"/><font
								class="zhu">&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_mmzhyxqntx" defVal="密码最后有效期内提醒" fileName="xtgl"/></font>
							</td>
							<% } else{%>
							<td><input type="text" name="remind" id="remind"
								onkeyup="numberControl($(this))" onblur="isBig(this)"
								maxlength="8" value="" disabled />&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_t" defVal="天" fileName="xtgl"/><font class="zhu">&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_mmzhyxqntx" defVal="密码最后有效期内提醒" fileName="xtgl"/></font>
							</td>
							<%}%>
						</tr>
						<tr>
							<td <%if(StaticValue.ZH_HK.equals(langName)){out.print("class='cwsx_td_1'");}else{out.print("class='cwsx_td_2'");}%> ><emp:message key="xtgl_cswh_mkcspz_cwsx_mh" defVal="错误上限：" fileName="xtgl"/></td>
							<td><input type="text" name="errorlimt" id="errorlimt" class="type_input"
								onkeyup="numberControl($(this))" onblur="numberControl($(this))"
								maxlength="8" value="<%=errorlimt%>" />&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_c" defVal="次" fileName="xtgl"/><font
								class="zhu">&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_cccwsxzhsx" defVal="超出错误上限账号失效" fileName="xtgl"/></font>
							</td>
						</tr>
					</table>
				</div>
				<div class="tabContainer1_up_div"></div>
				<div id="tabContainer1" onclick="javascript:bycheck(1)"
					 class="div_bg tabContainer1">
					<label><emp:message key="xtgl_cswh_mkcspz_shqjpz" defVal="审核全局配置" fileName="xtgl"/></label> <a id="img1"
						class="<%="1".equals(openTab)?"un":"" %>fold  fold-a">&nbsp;&nbsp;&nbsp;&nbsp;</a>
				</div>
				<div id="tabBody1"
					<%if("1".equals(openTab)){out.print("class='tabBody1_1'");}else{out.print("class='tabBody1_2'");}%>  >
					<div class="info_list cf">


						<div class="tit"><emp:message key="xtgl_cswh_mkcspz_shkg_mh" defVal="审核开关：" fileName="xtgl"/></div>
						<div class="inp">
							<select name="select" id="switch1" class="input_com w_select"
								onchange="openAndClose(this.value)">
								<option value="1" <%=isOpenFlow==3?"selected":"" %>><emp:message key="xtgl_cswh_mkcspz_kq" defVal="开启" fileName="xtgl"/></option>
								<option value="2" <%=isOpenFlow==2?"selected":"" %>><emp:message key="xtgl_cswh_mkcspz_gb" defVal="关闭" fileName="xtgl"/></option>
							</select>
						</div>
					</div>
					<div class="info_list cf wtr">
						<div class="tit"><emp:message key="xtgl_cswh_mkcspz_shfw_mh" defVal="审核范围：" fileName="xtgl"/></div>
						<div class="inp">
							<ul class="exam_range">
								<%if(swirchList != null && swirchList.size() > 0)
				{
	    			String [] menu_num=StaticValue.getMenu_num().toString().split(",");
	    			LinkedHashMap<String,String> menMap=new LinkedHashMap<String,String>();
	    			if(menu_num!=null )
	    			{
		    			for(int i=0;i<menu_num.length;i++)
		    			{
		    				menMap.put(menu_num[i],"");
		    			}
	    			}
					for(LfReviewSwitch swirch : swirchList)
					{
						int infoType=swirch.getInfoType();
						//没有彩信模块不显示彩信信息
						if((infoType==2|| infoType==4)&& !menMap.containsKey("4"))
						{
							continue;
						}
						//没有网讯模块不显示网讯信息
						if(swirch.getInfoType()==6 && !menMap.containsKey("14"))
						{
							continue;
						}
						//隐藏第6个未知模块
						if(infoType == 7){
							continue;
						}
						
						String menuStr = "";
						switch(swirch.getInfoType())
						{
							case 1 :
								menuStr = MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_dxfs",request);
								break;
							case 2 :
								menuStr = MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_cxfs",request);
								break;
							case 3 :
								menuStr = MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_dxmb",request);
								break;
							case 4 :
								menuStr = MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_cxmb",request);
								break;
							case 6 :
								menuStr = MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_wxmb",request);
								break;
                            case 7 :
                                menuStr = MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_lldg",request);
                                break;
						}
						int switchType = swirch.getSwitchType();
						Integer msgCount = swirch.getMsgCount();
				%>
								<li><input type="hidden" id="swType"
									value="<%=swirch.getInfoType() %>" /> <input type="hidden"
									id="sid" value="<%=swirch.getId() %>" /> <input type="checkbox"
									id="isOpen" class="checkbox"
									<%=switchType==1?"checked='checked'":"" %> value="1" /> <label
									for="dx_send" class="choice"><%=menuStr %></label> <% if(msgCount - 0 >= 0)
							{
							%> <span class="exam_total <%=switchType==1?"qsrsz_span":""%>"
                                style="<%=switchType == 1? "" : "display : none"%>"> <input
										type="text" class="input_com w_input w_msgCount" οnkeyup="value=value.replace(/[^\d]/g,'') " 
										data-placeholder="<emp:message key='xtgl_cswh_mkcspz_qsrsz' defVal='请输入数字' fileName='xtgl'/>" value="<%=msgCount %>" maxlength="8"
										id="msgCount" /> <label for=""><emp:message key="xtgl_cswh_mkcspz_tjysxysh" defVal="条及以上需审核" fileName="xtgl"/></label> </span> <%	
							}else
							{
							%> <input  value="<%=msgCount %>" type="hidden" 
									  id="msgCount" class="w_msgCount msgCount" /> <%	
							}
							%>
								</li>

								<%
					}
				}
				%>

							</ul>
						</div>
					</div>
				</div>

				<%-- EMP标准版6.1 新需求“企业邮箱参数配置” --%>
				<div class='tabContainer5_up_div'></div>
				<div id="tabContainer5"  onclick="javascript:bycheck(5)"
					 class="div_bg tabContainer5">
					<label ><emp:message key="xtgl_cswh_mkcspz_qyyxpz" defVal="企业邮箱配置" fileName="xtgl"/></label> <a id="img5"
						class="<%="1".equals(openTab)?"un":"" %>fold  fold-a">&nbsp;&nbsp;&nbsp;&nbsp;</a>
				</div>
				<div id="tabBody5"
					class="<%="1".equals(openTab)?"tabBody5_1":"tabBody5_2"%>">
						<ul class="exam_range">
							<li><label for="dx_send" class="choice"><emp:message key="xtgl_cswh_mkcspz_yxzh_mh" defVal="邮箱账号：" fileName="xtgl"/></label> <div
								class="exam_mail username_div" > <input
									type="text" class="input_com w_input w_msgCount"
									value="<%=username%>" name="username" maxlength="25"
									id="username" /> <a onclick="javascript:checkEmail()">&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_yxyz" defVal="邮箱验证 " fileName="xtgl"/></a>
									<a id="email_help">&nbsp;&nbsp;<emp:message key="xtgl_cswh_mkcspz_bz" defVal="帮助" fileName="xtgl"/> </a >
									<div id="email_help_info">
									<h4><emp:message key="xtgl_cswh_mkcspz_bz_mh" defVal="帮助：" fileName="xtgl"/></h4>
									<emp:message key="xtgl_cswh_mkcspz_yxbzsm_1_1" defVal="1，需要输入正确格式的邮箱账号、密码。（如果是QQ邮箱或者网易邮箱（163、126），" fileName="xtgl"/><emp:message key="xtgl_cswh_mkcspz_yxbzsm_1_2" defVal="密码请输入邮箱的授权码，而非邮箱密码，在邮箱首页->设置->账户里面获取授权码）" fileName="xtgl"/><br />
									<emp:message key="xtgl_cswh_mkcspz_yxbzsm_2" defVal="2，邮箱发送协议为smtp协议，请在邮箱首页->设置->账户里面开启smtp协议。" fileName="xtgl"/><br />
									<emp:message key="xtgl_cswh_mkcspz_yxbzsm_3" defVal="3，邮件服务器选择需要和邮箱账号保持一致，例如邮箱12345678@qq.com,邮件服务器应选择smtp.qq.com；" fileName="xtgl"/><br />
									<emp:message key="xtgl_cswh_mkcspz_yxbzsm_4" defVal="4，邮件服务器默认端口为25，如果使用QQ邮箱或者网易邮箱（163、126）请选择465端口号。" fileName="xtgl"/><br />
									</div>
							</div>
								</li>
							<li><label for="dx_send" class="choice"><emp:message key="xtgl_cswh_sjypz_mm_mh" defVal="密码：" fileName="xtgl"/></label> <span
								class="exam_mail password_span"  > <input
									type="password" class="input_com w_input w_msgCount"
									value="<%=password%>" name="password" maxlength="20"
									id="password" /> 
									<input class="password_input" type="password">
									</span>
							</li>
							<li><label for="dx_send" class="choice"><emp:message key="xtgl_cswh_mkcspz_nc_mh" defVal="昵称：" fileName="xtgl"/></label> <span
								class="exam_mail name_span"  > <input
									type="text" class="input_com w_input w_msgCount"
									value="<%=name%>" name="name" maxlength="25"
									id="name" /> </span></li>
							<li><label for="dx_send" class="choice"><emp:message key="xtgl_cswh_mkcspz_fsxy" defVal="发送协议" fileName="xtgl"/>：&nbsp;&nbsp;&nbsp;</label><span>
								<select id="protocol"
									class="input_bd protocol"   name="protocol">
										<option value="smtp" <%=port.equals("smtp")?"selected":""%>>smtp</option>
								</select></span> </li>
							<li><label for="dx_send" class="choice"><emp:message key="xtgl_cswh_mkcspz_yjfwq_mh" defVal="邮件服务器：" fileName="xtgl"/></label> <span>
									<select id="host" class="input_bd host"  
									name="host">
										<option value="smtp.sina.com"
											<%=host.equals("smtp.sina.com") ? "selected" : ""%>>smtp.sina.com</option>
										<option value="smtp.qq.com"
											<%=host.equals("smtp.qq.com") ? "selected" : ""%>>smtp.qq.com</option>
										<option value="smtp.sohu.com"
											<%=host.equals("smtp.sohu.com") ? "selected" : ""%>>smtp.sohu.com</option>
										<option value="smtp.163.com"
											<%=host.equals("smtp.163.com") ? "selected" : ""%>>smtp.163.com</option>
										<option value="smtp.gmail.com"
											<%=host.equals("smtp.gmail.com") ? "selected" : ""%>>smtp.gmail.com</option>
										<option value="smtp.126.com"
											<%=host.equals("smtp.126.com") ? "selected" : ""%>>smtp.126.com</option>
										<option value="smtp.mail.yahoo.com"
											<%=host.equals("smtp.mail.yahoo.com") ? "selected" : ""%>>smtp.mail.yahoo.com</option>
										<%
											if(!host.equals("smtp.sina.com") && !host.equals("smtp.qq.com")
											&& !host.equals("smtp.sohu.com") && !host.equals("smtp.163.com")
											&& !host.equals("smtp.gmail.com") && !host.equals("smtp.126.com")
											&& !host.equals("smtp.mail.yahoo.com")){
										%> <option value="<%=host%>" selected><%=host%></option>
										<%
											}
										 %>
								</select>
								<span id="host_text_span"
								class="exam_mail host_text_span"  > <input
									type="text" class="input_com w_input w_msgCount"
									name="host_text" maxlength="25"
									id="host_text" /> </span>
									
								
								&nbsp;&nbsp;&nbsp;<span id="buttons_add"  class="buttons buttons_add">
									<a id="add" onclick="javascript:switch_selectText()"><emp:message key="xtgl_cswh_mkcspz_zj" defVal="增加" fileName="xtgl"/></a> 
									<input type="button" class="btnClass2 submit bcStyle save"  id="save" name="save" value="<emp:message key='xtgl_spgl_shlcgl_bc' defVal='保存' fileName='xtgl'/>" onclick="saveHost()"/>
								</span>
							</span></li>
							<li><label for="dx_send" class="choice"><emp:message key="xtgl_cswh_mkcspz_fwqdk_mh" defVal="服务器端口：" fileName="xtgl"/></label> <span>
								<select id="port"
									class="input_bd port"  name="port">
										<option value="25" <%=port.equals("25")?"selected":""%>>25</option>
										<option value="465" <%=port.equals("465")?"selected":""%>>465</option>
								</select>
							</span></li>
							
						</ul>

				</div>



				<%-- EMP标准版6.1 新需求“贴尾管理-参数配置”  pengj --%>
				<div class='tabContainer4_up_div'></div>
				<div id="tabContainer4" onclick="javascript:bycheck(4)"
					class="div_bg tabCont">
					<label><emp:message key="xtgl_cswh_mkcspz_twqjpz" defVal="贴尾全局配置" fileName="xtgl"/></label> <a id="img4"
						class="<%=!"1".equals(openTab)?"un":"" %>fold  fold-a">&nbsp;&nbsp;&nbsp;&nbsp;</a>
				</div>
				<div id="tabBody4"
					class="<%=!"1".equals(openTab)?"tabBody4_1":"tabBody4_2"%>" >
					<table class="qjtw_table">
						<tr>
							<td class="<%=StaticValue.ZH_HK.equals(langName)?"qjtw_td_1":"qjtw_td_2"%>" ><emp:message key="xtgl_cswh_mkcspz_qjtw_mh" defVal="全局贴尾：" fileName="xtgl"/></td>

							<td ><select id="overtailflag" class="input_bd overtailflag"  
								onchange="javascript:contentIsShow()">
									<option value="0" <%=overtailflag==0?"selected":"" %>><emp:message key="xtgl_cswh_mkcspz_gb" defVal="关闭" fileName="xtgl"/></option>
									<option value="1" <%=overtailflag==1?"selected":"" %>><emp:message key="xtgl_cswh_mkcspz_kq" defVal="开启" fileName="xtgl"/></option>
							</select></td>
						</tr>


						<tr>
							<td class="<%="1".equals(openTab)?"qjtwmb_td_1":"qjtwmb_td_2"%>" ><emp:message key="xtgl_cswh_mkcspz_qjtwmb_mh" defVal="全局贴尾模板：" fileName="xtgl"/></td>

							<td><textarea id="content" maxlength="64"
									class="content" rows="2" cols="10"><%=content %></textarea>
							</td>
						</tr>

						<tr>
							<td class="<%="1".equals(openTab)?"qttwlx_mh_td_1":"qttwlx_mh_td_2"%>"><emp:message key="xtgl_cswh_mkcspz_qttwlx_mh" defVal="其它贴尾类型：" fileName="xtgl"/></td>
							<td><select id="othertailflag" class="input_bd othertailflag"  >
									<option value="0" <%=othertailflag==0?"selected":"" %>><emp:message key="xtgl_cswh_mkcspz_gb" defVal="关闭" fileName="xtgl"/></option>
									<option value="1" <%=othertailflag==1?"selected":"" %>><emp:message key="xtgl_cswh_mkcspz_ywtw" defVal="业务贴尾" fileName="xtgl"/></option>
									<option value="2" <%=othertailflag==2?"selected":"" %>><emp:message key="xtgl_cswh_mkcspz_sptw" defVal="SP贴尾" fileName="xtgl"/></option>
							</select></td>
						</tr>
						<tr>
						<td colspan="2">
						<br/>
						<p class="zhu"><emp:message key="xtgl_cswh_mkcspz_twsm_1" defVal="注：1、在短信发送中，单次只能贴一种类型的贴尾内容；" fileName="xtgl"/></p><br/>
						<p class="zhu" class="<%=StaticValue.ZH_HK.equals(langName)?"twsm_2_p_1":"twsm_2_p_2"%>" ><emp:message key="xtgl_cswh_mkcspz_twsm_2" defVal="2、当全局贴尾与其它贴尾同时开启时，优先贴其它类型的贴尾内容；" fileName="xtgl"/></p><br/>
						<p class="zhu" class="<%=StaticValue.ZH_HK.equals(langName)?"twsm_3_p_1":"twsm_3_p_2"%>" ><emp:message key="xtgl_cswh_mkcspz_twsm_3" defVal="3、选择SP贴尾或者业务贴尾后，需要到贴尾管理模块新建贴尾模板。" fileName="xtgl"/></p>
						</td>
						</tr>
					</table>
				</div>


				<%-- END --%>




				<div class='tabContainer2_up_div'></div>
				<div id="tabContainer2" onclick="javascript:bycheck(2)"
					class="div_bg">
					<label><emp:message key="xtgl_cswh_mkcspz_qjcspz" defVal="全局参数配置" fileName="xtgl"/></label> <a id="img2"
						class="<%=!"1".equals(openTab)?"un":"" %>fold  fold-a">&nbsp;&nbsp;&nbsp;&nbsp;</a>
				</div>
				<div id="tabBody2" class="<%=!"1".equals(openTab)?"tabBody2_1":"tabBody2_2"%>" >
					<table class="<%=StaticValue.ZH_HK.equals(langName)?"tabBody2_table_1":"tabBody2_table_2"%>"  >
						<tr class="tzwhws_mh_tr">
							<td class="<%=StaticValue.ZH_HK.equals(langName)?"tzwhws_mh_td_1":"tzwhws_mh_td_2"%>" ><emp:message key="xtgl_cswh_mkcspz_tzwhws_mh" defVal="拓展尾号位数：" fileName="xtgl"/></td>

							<td><select id="subnoLen" class="input_bd">
									<option value="3" <%=subnoLen==3?"selected":"" %>>3</option>
									<option value="4" <%=subnoLen==4?"selected":"" %>>4</option>
									<option value="5" <%=subnoLen==5?"selected":"" %>>5</option>
									<option value="6" <%=subnoLen==6?"selected":"" %>>6</option>
							</select></td>
						</tr>
						<tr class="jgjfkg_mh_tr">
							<td class="<%=StaticValue.ZH_HK.equals(langName)?"jgjfkg_mh_td_1":"jgjfkg_mh_td_2"%>"  ><emp:message key="xtgl_cswh_mkcspz_jgjfkg_mh" defVal="机构计费开关：" fileName="xtgl"/></td>
							<td><select id="isBalance" class="input_bd">
									<option value="0" <%=isBalance==0?"selected":"" %>><emp:message key="xtgl_cswh_mkcspz_g" defVal="关" fileName="xtgl"/></option>
									<option value="1" <%=isBalance==1?"selected":"" %>><emp:message key="xtgl_cswh_mkcspz_k" defVal="开" fileName="xtgl"/></option>
							</select></td>
						</tr>
						<%--签名同步开关--%>
						<tr class="qmtbkg_mh_tr">
							<td class="<%=StaticValue.ZH_HK.equals(langName)?"qmtbkg_mh_td_1":"qmtbkg_mh_td_2"%>"  ><emp:message key="xtgl_cswh_mkcspz_qmtbkg_mh" defVal="签名同步开关：" fileName="xtgl"/></td>
							<td><select id="actsyncflag" oval="<%=actsyncflag%>"
								class="input_bd">
									<option value="0"
										<%if("0".equals(actsyncflag)){out.print("selected");}%>><emp:message key="xtgl_cswh_mkcspz_g" defVal="关" fileName="xtgl"/></option>
									<option value="1"
										<%if("1".equals(actsyncflag)){out.print("selected");}%>><emp:message key="xtgl_cswh_mkcspz_k" defVal="开" fileName="xtgl"/></option>
							</select></td>
						</tr>
						<%--上行指令加黑名单--%>
						<tr class="sxzljhmd_mh_tr">
							<td class="<%=StaticValue.ZH_HK.equals(langName)?"sxzljhmd_mh_td_1":"sxzljhmd_mh_td_2"%>" ><emp:message key="xtgl_cswh_mkcspz_sxzljhmd_mh" defVal="上行指令加黑名单：" fileName="xtgl"/></td>
							<td><select id="sendcmdflag" oval="<%=isOpen%>" class="input_bd" onchange="javascript:cmdIsShow()">
									<option value="0"
										<%if("0".equals(isOpen)){out.print("selected");}%>><emp:message key="xtgl_cswh_mkcspz_g" defVal="关" fileName="xtgl"/></option>
									<option value="1"
										<%if("1".equals(isOpen)){out.print("selected");}%>><emp:message key="xtgl_cswh_mkcspz_k" defVal="开" fileName="xtgl"/></option>
							</select></td>
						</tr>

						<tr><td class="<%=StaticValue.ZH_HK.equals(langName)?"sxjhmdzl_mh_td_1":"sxjhmdzl_mh_td_2"%>" ><emp:message key="xtgl_cswh_mkcspz_sxjhmdzl_mh" defVal="上行加黑名单指令：" fileName="xtgl"/></td>
							<td><textarea id="sendcmdcontent" maxlength="109"
									class="sendcmdcontent" rows="2" cols="10"><%=cmdstr %></textarea>
							</td>
						</tr>
						<tr  class="tjsyhy_up_tr">
						<td></td>
						<td>
						<font class="zhu"><emp:message key="xtgl_cswh_mkcspz_tjsyhy" defVal="推荐使用行业通用退订指令TD；" fileName="xtgl"/><br><emp:message key="xtgl_cswh_mkcspz_dgzl" defVal="多个指令以英文逗号隔开，字母不区分大小写。" fileName="xtgl"/></font>
						</td>
						</tr>
						<%
      			if((StaticValue.getCORPTYPE() == 0 && sysuser.getCorpCode().equals("100001")) ||
      					(StaticValue.getCORPTYPE() == 1 && sysuser.getCorpCode().equals("100000"))) {
      			%>
						<tr class="czrzblsj_mh_tr">
							<td class="czrzblsj_mh_td"><emp:message key="xtgl_cswh_mkcspz_czrzblsj_mh" defVal="操作日志保留时间：" fileName="xtgl"/></td>
							<td><select id="saveLog" class="input_bd">
									<option value="6" <%if("6".equals(logtime)){ %>
										selected="selected" <%} %>>6<emp:message key="xtgl_cswh_mkcspz_gy" defVal="个月" fileName="xtgl"/></option>
									<option value="12" <%if("12".equals(logtime)){ %>
										selected="selected" <%} %>>12<emp:message key="xtgl_cswh_mkcspz_gy" defVal="个月" fileName="xtgl"/></option>
									<option value="18" <%if("18".equals(logtime)){ %>
										selected="selected" <%} %>>18<emp:message key="xtgl_cswh_mkcspz_gy" defVal="个月" fileName="xtgl"/></option>
									<option value="24" <%if("24".equals(logtime)){ %>
										selected="selected" <%} %>>24<emp:message key="xtgl_cswh_mkcspz_gy" defVal="个月" fileName="xtgl"/></option>
									<option value="30" <%if("30".equals(logtime)){ %>
										selected="selected" <%} %>>30<emp:message key="xtgl_cswh_mkcspz_gy" defVal="个月" fileName="xtgl"/></option>
									<option value="36" <%if("36".equals(logtime)){ %>
										selected="selected" <%} %>>36<emp:message key="xtgl_cswh_mkcspz_gy" defVal="个月" fileName="xtgl"/></option>
							</select></td>
						</tr>
						<%
      			}  
			    %>

						<tr class="sjdt_tr">
							<td class="sjdt_td"><emp:message key="xtgl_cswh_mkcspz_sjdt" defVal="手机动态口令模板：" fileName="xtgl"/></td>

							<td>
								<%
				         	if(null == dynpwd || "".equals(dynpwd)) {
				         		dynpwd = MessageUtils.extractMessage("xtgl","xtgl_cswh_mkcspz_sjdtkl",request);
				         	} else {
				         		dynpwd = dynpwd.replace("#P_1#", "{#P#}");
				         	}
				         	%> <textarea id="dynPwd" onchange="javascript:nobfh();"
									maxlength="40" class="dynPwd" rows="2"
									cols="10"><%=dynpwd %></textarea></td>
						</tr>

						<tr valign="top">
							<td>&nbsp;</td>
							<td><font color="#F1F1F9;"><emp:message key="xtgl_cswh_mkcspz_dtklgs" defVal="动态口令参数格式为：{#参数#}" fileName="xtgl"/></font>
							</td>
						</tr>
						<tr class="<%=!"1".equals(isShow)?"yinc":"" %>">
							<td class="mbkbj_mh_td"><emp:message key="xtgl_cswh_mkcspz_mbkbj_mh" defVal="模板可编辑：" fileName="xtgl"/></td>
							<td><input name="isEditor" type="radio" value="1"
								<% if("1".equals(isEditor)){ %> checked="checked" <%} %> /><emp:message key="xtgl_cswh_mkcspz_s" defVal="是" fileName="xtgl"/>&nbsp;&nbsp;&nbsp;&nbsp;
								<input name="isEditor" type="radio" value="0"
								<% if("0".equals(isEditor)){ %> checked="checked" <%} %> /><emp:message key="xtgl_cswh_mkcspz_f" defVal="否" fileName="xtgl"/>&nbsp;&nbsp;&nbsp;&nbsp;
								<font color="#F1F1F9;"><emp:message key="xtgl_cswh_mkcspz_mbsx" defVal="(仅针对移动办公/移动客服的短信模板生效)" fileName="xtgl"/></font></td>
						</tr>
					</table>
				</div>
				<div class="modBottom">
					<span class="modABL"></span><span class="modABR"></span>
				</div>
				<div class="clear2"></div>
				<div class="b_F_btn qd_div"  >
					<input class="btnClass5" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确  定' fileName='xtgl'/>"
						onclick="javascript:btok();" />
				</div>
			</div>
		</div>
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
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath%>/common/js/tabs.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
    <%--<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script>--%>
	<script src="<%=iPath%>/js/mep_menuParamConfig.js?V=<%=StaticValue.getJspImpVersion() %>"
		type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function(){

	    /*$("#switch1,#protocol,#host,#port").isSearchSelectNew({'width':186,'height':22,'isInput':false});
        $("#overtailflag,#othertailflag,#subnoLen,#isBalance,#actsyncflag,#sendcmdflag,#saveLog").isSearchSelectNew({'width':316,'height':22,'isInput':false});*/

		noquot($("#dynPwd"));
		getLoginInfo("#hiddenValueDiv2");
		getLoginInfo("#hiddenValueDiv");
		
	    var kkResult = "<%=session.getAttribute("kkResult")%>";
		<%session.removeAttribute("kkResult");%>
	    if(kkResult == "true")
	    {
		    alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_21"));//企业快快参数配置成功！--%>模块
	    }else if(kkResult == "error" || kkResult == "false")
	    {
	    	alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_22"));//企业快快参数配置失败！--%>模块
	    	
	    }
	    var showIndex = "<%=request.getParameter("showIndex")%>";
	    if(showIndex == "1")
	    {
	    	$(".tabHead").find("li a").eq(1).trigger("click");
	    }
	    openAndClose('<%=isOpenFlow%>');

	    var theOvertailflag = $('#overtailflag').val();
		if(theOvertailflag == 1){
			$('#content').attr("disabled",false);
		}else{
			$('#content').attr("disabled",true);
		}
		cmdIsShow();
		$('#content').blur(function() { 
			if($('#content').val().length>64){
				  alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_23"));
				  $('#content').select();
				  return;
				} 
			});
			
			
		//显示帮助信息


		$("#email_help").hover(function() {
				$("#email_help_info").show();
			}, function() {
				$("#email_help_info").hide();
			});
			

		$("#username").blur(function() {
				var email_str = $("#username").val();
				var index = email_str.indexOf('@');
				var index_point = email_str.indexOf('.');
				if(index!=-1 && index_point!=-1){
					email_str = email_str.substring(index+1,index_point);
					$("#host").val("smtp."+email_str+".com");
					if(email_str=="qq" || email_str=="163" || email_str=="126"){
						$("#port").val("465");
					}else{
						$("#port").val("25");
					}

				}
			});
		});

		//确定
		function btok() {
			var theContent = $("#content").val();
			var pattern = /[`~@#\^\*_\+\\\/'\[\]]/im;
			if (pattern.test(theContent)) {
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_24"));
				$('#content').select();
				return;
			}
			if ($('#content').val().length > 64) {
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_25"));
				$('#content').select();
				return;
			}

			//如果全局贴尾内容部位空，且全部为空
			if (theContent.length > 0 && $.trim(theContent).length == 0) {
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_26"));
				$('#content').select();
				return;
			}

			if (isBig("")) {
				return;
			}
			$(".btnClass5").attr("disabled", "disabled");
			var paramstr = "";
			var flag=true;
			$(".wtr li").each(function() {
				
				paramstr = paramstr + $(this).find("#sid").val() + "@";
				paramstr = paramstr + $(this).find("#swType").val() + "@";
				if ($(this).find("#isOpen").attr("checked")) {
					paramstr = paramstr + "1@";
				} else {
					paramstr = paramstr + "2@";
				}

				var msgCount = $(this).find(".w_msgCount").val();
				if ($(this).find("#isOpen").attr("checked")) {
					if(msgCount=="请输入数字"){
						flag=false;
					}
				}
				
				if (msgCount == "") {
					msgCount = "0";
				}
				paramstr = paramstr + msgCount + "&";
			});
			
			if(!flag){
				alert("审核全局配置-审核条数不能为空！");
				$(".btnClass5").attr("disabled", false);
				return;
			}
			//由于之前是别人写的，为了不破坏原来的
			paramstr = paramstr + "%";
			paramstr = paramstr + $("#bit").val() + " @";

			if ($("#number").is(":checked")) {
				paramstr = paramstr + "1@";
			} else {
				paramstr = paramstr + " @";
			}
			if ($("#character").is(":checked")) {
				paramstr = paramstr + "2@";
			} else {
				paramstr = paramstr + " @";
			}
			if ($("#sign").is(":checked")) {
				paramstr = paramstr + "3@";
			} else {
				paramstr = paramstr + " @";
			}
			//paramstr=paramstr+$("#character").val()+"@";
			//paramstr=paramstr+$("#sign").val()+"@";
			paramstr = paramstr + $("#modiftime").val() + " @";
			paramstr = paramstr + $("#remind").val() + " @";
			paramstr = paramstr + $("#errorlimt").val() + " @";
			paramstr = paramstr + $("#saveLog").val() + " @";
			var zenze=/^[A-Za-z0-9,]+$/;
			var sendcmdflag = $('#sendcmdflag').val();
			if(sendcmdflag=='1'){
				var sendcmdcontent=$("#sendcmdcontent").val();
				if($.trim(sendcmdcontent)==''){
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_27"));
					$("#sendcmdcontent").select();
					$(".btnClass5").attr("disabled", false);
					return;
				}
				if(!zenze.test(sendcmdcontent))
				{
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_28"));
					$("#sendcmdcontent").select();
					$(".btnClass5").attr("disabled", false);
					return;
				}
				var arr=sendcmdcontent.split(',');
				if(arr.length>10){
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_29"));
					$("#sendcmdcontent").select();
					$(".btnClass5").attr("disabled", false);
					return;
				}
				if(sendcmdcontent.indexOf(',')>-1&&sendcmdcontent.lastIndexOf(',')==sendcmdcontent.length-1){
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_30"));
					$("#sendcmdcontent").select();
					$(".btnClass5").attr("disabled", false);
					return;
				}
				for(var k=0;k<arr.length;k++){
					if ($.trim(arr[k]) == "") {
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_31"));
					$("#sendcmdcontent").select();
					$(".btnClass5").attr("disabled", false);
					return;
					}
					if (arr[k].length>10) {
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_32"));
						$("#sendcmdcontent").select();
						$(".btnClass5").attr("disabled", false);
					return;
					}
				}
				var cont=sendcmdcontent.toUpperCase();
				var arrcontent=cont.split(',');
				var nary=arrcontent.sort(); 
				for(var i=0;i<nary.length;i++){ 
					if (nary[i]==nary[i+1]){ 
						alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_33"));
							$("#sendcmdcontent").select();
							$(".btnClass5").attr("disabled", false);
							return;
				}
					if(nary[i]=='1111'){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_34"));
							$("#sendcmdcontent").select();
							$(".btnClass5").attr("disabled", false);
							return;
					}
					if(nary[i]=='DG'){
							alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_35"));
							$("#sendcmdcontent").select();
							$(".btnClass5").attr("disabled", false);
							return;
					}
			} 
				
				
		}
			
			var dynPwd = $('#dynPwd').val();
			if ($.trim(dynPwd) == "") {
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_36"));
				$(".btnClass5").attr("disabled", false);
				return;
			}

			if (dynPwd.indexOf(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_37")) == -1) {
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_38"));
				$(".btnClass5").attr("disabled", false);
				return;
			} else if (dynPwd.indexOf(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_37")) != dynPwd.lastIndexOf(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_37"))) {
				alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_39"));
				$(".btnClass5").attr("disabled", false);
				return;
			}

			paramstr = paramstr + dynPwd + " @";
			//模板是否可编辑
			var isEditor = $("input[name='isEditor']:checked").val();
			paramstr = paramstr + isEditor + "@";
			var isBalance = $("#isBalance").val();
			//尾号位数
			var subnoLen = $("#subnoLen").val();
			var time =
	<%=System.currentTimeMillis()%>;

		//EMP标准版6.1 新需求：贴尾全局配置  pengj
		var overtailflag = $("#overtailflag").val();
		var content = $("#content").val();
		var othertailflag = $("#othertailflag").val();
		var lguserid = $("#lguserid").val();
		
		//企业邮件参数配置
		var protocol = $("#protocol").val();
		//默认域名
		var host_select = $("#host").val();
		//新添加域名
		var host_text = $("#host_text").val();
		if(host_text !=null){
			host_text = host_text.replace(/\s+/g,"");
		}
		
		var host = !host_text==""?host_text:host_select;
		
		var port = $("#port").val();
		var username = $("#username").val();
		var name = $("#name").val();
		var password = $("#password").val();
		//end


		if(confirm(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_40")))
		{
			time = time + 1;
			//屏蔽按钮，防止重复点击
			$(".btnClass5").attr("disabled",true);
			var lgcorpcode = $("#lgcorpcode").val();
			if(overtailflag == -1 || othertailflag == -1){
				alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_41"));
			}else{
                var postData = {method:"change",isBalance : isBalance,len:subnoLen,lgcorpcode:lgcorpcode,params:paramstr,overtailflag:overtailflag,content:content,othertailflag:othertailflag,sendcmdflag:sendcmdflag,sendcmdcontent:sendcmdcontent,lguserid:lguserid,"protocol":protocol,"host":host,"port":port,"username":username,"name":name,"password":password};
                //签名同步开关
                var actsyncflag = $('#actsyncflag').val();
                if(actsyncflag != $('#actsyncflag').attr('oval')){
                    $.extend(postData,{actsyncflag:actsyncflag});
                }
			$.post("<%=path%>/mep_menuParamConfig.htm?times="+time,postData,function(result){
				//撤销按钮disable属性
				$(".btnClass5").attr("disabled",false);
				if(result.indexOf("errorLen")==0)
				{
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_41")+result.split(":")[1]+getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_42"));
				}else if(result.indexOf("true;")>-1) {
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_40"));
					changeTopYe(isBalance);
					var skin = $("#skin").val();
					changePrivilege(isBalance, skin);
					//动态改变左边菜单的充值回收菜单
					var it=result.split(";");
					if(it.length>1){
						$("#passWordLimt",window.parent.parent.parent.document).html(it[1]);
					}
					location.href = "<%=path%>/mep_menuParamConfig.htm?showIndex=1&lgcorpcode="+lgcorpcode;
				}else
				{
					alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_41"));
				}
			});
			}
		}
		else
		{
			$(".btnClass5").attr("disabled",false);
		}
		    
	}

	function nobfh() {
    	var dynPwd = $('#dynPwd').val();
    	dynPwd = dynPwd.replaceAll("%", "");
		reg=/#p_(.*?)#/gi;
		dynPwd=dynPwd.replace(reg,''); 
		dynPwd = dynPwd.substring(0, 40);   	
    	$('#dynPwd').val(dynPwd);
    }

	function contentIsShow(){
		
		var theOvertailflag = $('#overtailflag').val();
		if(theOvertailflag == 1){
			$('#content').attr("disabled",false);
		}else{
			$('#content').attr("disabled",true);
		}
	}
	
	
	function cmdIsShow(){
		var sendcmdflag = $('#sendcmdflag').val();
		if(sendcmdflag == 1){
			$('#sendcmdcontent').attr("disabled",false);
		}else{
			$('#sendcmdcontent').attr("disabled",true);
		}
	}
	
	//验证邮箱是否有效
	function checkEmail()
	{
		var email = $('#username').val();	
		var arr = [ "ac", "com", "net", "org", "edu", "gov", "mil", "ac\.cn",
			"com\.cn", "net\.cn", "org\.cn", "edu\.cn" ];
		var temp_arr = arr.join("|");
		// reg
		var reg_str = "^[0-9a-zA-Z](\\w|-)*@\\w+\\.(" + temp_arr + ")$";
		var reg = new RegExp(reg_str);
		if (reg.test(email)) {
			
			//企业邮件参数配置
			var protocol = $("#protocol").val();
			//默认域名
			var host_select = $("#host").val();
			//新添加域名
			var host_text = $("#host_text").val();
			if(host_text!=null){
				host_text = $("#host_text").val().replace(/\s+/g,"");
			}
			var host = !host_text==""?host_text:host_select;
			
			var port = $("#port").val();
			var username = $("#username").val();
			var name = $("#name").val();
			var password = $("#password").val();
			
			var url = "<%=path%>/mep_menuParamConfig.htm";

			var postData = {method:"emailAuthenticator","protocol":protocol,"host":host,"port":port,"username":username,"name":name,"password":password};
			$.post(url,postData,function(result){
				if(result.indexOf("true")>-1){
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_43"));
				}else if(result.indexOf("code")>-1){
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_44"));
				}else{
					alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_45"));
				}
			});
		}else{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_46"));
		}
	}
	
	//邮件服务器下拉选择框与自动输入文本框切换
	function switch_selectText(){
		if($('#host').is(":visible")){
			$('#host').hide();
			$('#host_text_span').show();
			$('#buttons_add').css('marginLeft','250px');
			$('#add').text(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_47"));
			$('#save').show();
		}else{
			$('#host').show();
			$('#host_text_span').hide();
			$('#buttons_add').css('marginLeft','0px');
			$('#add').text(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_48"));
			$('#save').hide();
		}
		
	}
	
	//保存自定义邮件服务器
	function saveHost(){
		var opvalue = $.trim($('#host_text').val());
		if(opvalue!=null && opvalue !=""){
			var op = "<option value='"+opvalue+"'>"+opvalue+"</option>";
			$('#host').append(op);
			$('#host').show();
			$('#host_text_span').hide();
			$('#buttons_add').css('marginLeft','0px');
			$('#add').text(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_48"));
			$('#save').hide();
			$("#host").val(opvalue);
		}else{
			alert(getJsLocaleMessage("xtgl","xtgl_cswh_mkcspz_49"));
		}
		
	}
	
	</script>
</body>
</html>
