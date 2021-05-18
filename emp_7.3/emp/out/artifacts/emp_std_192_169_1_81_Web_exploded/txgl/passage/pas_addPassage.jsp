<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfPageField"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.XtGateQueue"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	String txglFrame = skin.replace(commonPath, inheritPath);
	@SuppressWarnings("unchecked")
	List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pas_pagefileds");
	//加密ID
	String keyId =(String)request.getAttribute("keyId");
	//修改or 新增
	String updateorinsert =(String)request.getAttribute("updateorinsert");
	//title
	//String wwwtitle="新建通道";
	String wwwtitle= MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_xjtd", request);
	//String title="添加通道信息";
	String title= MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_tjtdxx", request);
	//通道号
	String spgateNum="";
	//通道名称
	String gatename="";
	//运营商
	String spiscumn="";
	//状态
	String status="0";
	//是否支持长短信
	String longsms="1";
	//通道优先级别
	String riselevel="0";
	//单条短信字数
	String singlelen="";
	//最大通道扩展位数
	String sublen="";
	//短信签名
	String signstr=null;
	//通道ID
	Long gateID=null;
	//短信签名长度
	String signlen2="0";
	//是否支持整条提交
	String endsplit ="0";
	//是否由运营商加签名
	String signdroptype="1";
	//短信签名长度模式 固定长度 自动计算
	String signMode="0";
	//是否按按长短信折分
	String splitRule="1";
	//是否逐条签名
	String eachSign="0";
	//费率
	String fee="";
	//短彩类型
	String gatetype="";
	//唯一id
	String id="";
	//前置后置 默认后置
	String gateprivilege="0";
	String ensignstr = "";
	String ensignlen = ""; 
	String ensinglelen = ""; 
	boolean isEn = false;
	boolean isGW = false;
	//后拆分信息内容最大字数
	String espLitMaxWd="";
	//后拆分纯英文信息内容最大字数
	String espLitEnMaxWd="";
	if("1".equals(updateorinsert)){
		
		XtGateQueue gateq=(XtGateQueue)request.getAttribute("ugate");
		isGW = gateq.getSpisuncm()-5==0;
		if(gateq!=null){
			//wwwtitle="修改通道";
			wwwtitle=MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_xgtd", request);
			//title="修改通道信息";
			title=MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_xgtdxx", request);
			//通道ID
			gateID=gateq.getId();
			//通道号
			spgateNum=gateq.getSpgate();
			//通道名称
			gatename=gateq.getGateName();
			//运营商
			spiscumn=gateq.getSpisuncm()!=null?gateq.getSpisuncm().toString():"";
			//状态
			status=gateq.getStatus()!=null?gateq.getStatus().toString():"";
			//是否支持长短信
			longsms=gateq.getLongSms()!=null?gateq.getLongSms().toString():"";
			//通道优先级别
			riselevel=gateq.getRiseLevel()!=null?gateq.getRiseLevel().toString():"";
			//单条短信字数
			singlelen=gateq.getSingleLen()!=null?gateq.getSingleLen().toString():"";
			ensinglelen=gateq.getEnsinglelen()!=null?gateq.getEnsinglelen().toString():"";
			//最大通道扩展位数
			sublen=gateq.getSublen()!=null?gateq.getSublen().toString():"";
			//短信签名
			signstr=gateq.getSignstr()!=null?gateq.getSignstr().trim():"";
			ensignstr=gateq.getEnsignstr()!=null?gateq.getEnsignstr().trim():"";
			//短信签名长度
			signlen2=gateq.getSignlen()!=null?gateq.getSignlen()>1?gateq.getSignlen().toString():signstr.length()+"":"";
			ensignlen=gateq.getEnsignlen()!=null?gateq.getEnsignlen()>1?gateq.getEnsignlen().toString():ensignstr.replaceAll("[\\[\\]\\|\\^\\{\\}\\~\\\\]", "**").length()+"":"";
			//是否支持整条提交
			endsplit =gateq.getEndSplit()!=null?gateq.getEndSplit().toString():"";
			//是否由运营商加签名
			signdroptype=gateq.getSignDropType()!=null?gateq.getSignDropType().toString():"";
			//后拆分信息内容最大字数
			espLitMaxWd=gateq.getEsplitmaxwd()!=null?gateq.getEsplitmaxwd().toString():"";
			//后拆分纯英文信息内容最大字数
			espLitEnMaxWd=gateq.getEsplitenmaxwd()!=null?gateq.getEsplitenmaxwd().toString():"";
			
			if("0".equals(signdroptype)){
				//短信签名长度模式 固定长度 自动计算
				signMode="1";
			}else{
				//短信签名长度模式 固定长度 自动计算
				signMode="0";
			}
			if("0".equals(signdroptype))
			{
				signdroptype="1";
			}else if("1".equals(signdroptype))
			{
				signdroptype="1";
			}else if("2".equals(signdroptype))
			{
				signdroptype="0";
			}
			
			
			//是否按按长短信折分
			splitRule=gateq.getSplitRule()!=null?gateq.getSplitRule().toString():"";
			//是否逐条签名
			eachSign=gateq.getEachSign()!=null?gateq.getEachSign().toString():"";
			//费率
			fee=gateq.getFee()!=null&&gateq.getFee()>0?gateq.getFee().toString():"";
			//短彩类型
			gatetype=gateq.getGateType()!=null?gateq.getGateType().toString():"";
			id=gateq.getId()!=null?gateq.getId().toString():"";
			int privilege = gateq.getGateprivilege()==null?0:gateq.getGateprivilege();
			//前置 后置 字符串
			gateprivilege = (privilege&4)==4?"1":"0";
			//是否支持英文短信
			isEn = (privilege & 2) == 2;
		}else{
			updateorinsert="0";
		}
	}
	//1显示同步签名按钮，0不显示同步签名按钮
	String isShowSync=(String)request.getAttribute("isShowSync");
	
	//同步签名
	String tbqm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_tbqm", request);
    //预览
    String yl = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_yl", request);
	//返回
	String fh = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_fh", request);
	//运营商
	String yys = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_yys", request);
    if(yys!=null&&yys.length()>1){
    	yys = yys.substring(0,yys.length()-1);
    }
	//通道号码
	String tdhm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_tdhm", request);
    if(tdhm!=null&&tdhm.length()>1){
    	tdhm = tdhm.substring(0,tdhm.length()-1);
    }
	//短信签名
	String dxqm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_dxqm", request);
    if(dxqm!=null&&dxqm.length()>1){
    	dxqm = dxqm.substring(0,dxqm.length()-1);
    }
	//签名位置
	String qmwz = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_qmwz", request);
    if(qmwz!=null&&qmwz.length()>1){
    	qmwz = qmwz.substring(0,qmwz.length()-1);
    }
	//单条短信字数
	String dtdxzs = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_dtdxzs", request);
    if(dtdxzs!=null&&dtdxzs.length()>1){
    	dtdxzs = dtdxzs.substring(0,dtdxzs.length()-1);
    }
	//通道类型 
	String tdlx = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_tdlx", request);
    if(tdlx!=null&&tdlx.length()>1){
    	tdlx = tdlx.substring(0,tdlx.length()-1);
    }
	//确定
	String qd = MessageUtils.extractMessage("common", "common_confirm", request);
	//取消
	String qx = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_qx", request);
	//提示
	String ts = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_ts", request);
	
%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/common/common.jsp" %>
<title><%=wwwtitle %></title>

<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">

	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<style type="text/css">
		.syncBtnClass{
			width: 80px;
			text-indent: 5px;
		}
	</style>
	<%}%>
	
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/pas_addPassage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/pas_addPassage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
</head>
<body id="pas_addPassage">
    <div id="container">
    	<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,"2000-1300",title) %>
    	<div class="rContent">
				<div class="titletop">
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
								<%=title %>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="back()">&larr;&nbsp;<emp:message key="txgl_wghdpz_tdyxcsgl_fhsyj" defVal="返回上一级" fileName="txgl"/></font>
							</td>
						</tr>
					</table>
				</div>
    		<div id="detail_Info" class="detail_Info">

                <form action="<%=path %>/w_passage.htm?method=update" method="post" id="addpassageForm" name="addpassageForm" >
				<input type="hidden" id="path" value="<%=path %>"/>
                 <input id="hidOpType" name="hidOpType" type="hidden" value="add"/>
                <input id="hidGateNameMsg" type="hidden" value=""/>
                <input type="hidden" value="0" id="isname"/>
                <input type="hidden" value="0" id="isnum"/>
                <input type="hidden" value="0" id="isNeedUpdateSign"/>
                 <input type="hidden" value="<%=keyId %>" id="keyId"/>
             	<table class="tdlx_table">
   			 	<thead>
   			 		<%
						String typename = "";
						if(pagefileds!=null&&pagefileds.size()>0){
							LfPageField first=pagefileds.get(0);
							typename=first.getField()+"：";
						} 
					%>
   			 	 	<tr>
                      <td><span><emp:message key="txgl_wghdpz_tdgl_tdlxx" defVal="通道类型：" fileName="txgl"/></span></td>
                        <td class="dux_td">
                       
                        <% 
                       // String gtypestr="短信";
                        String gtypestr = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dux", request);
                        if("1".equals(updateorinsert)){
                        		
		                   		if("1".equals(gatetype)){
        							//gtypestr="短信";  
        							 gtypestr=MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dux", request);             			
                         		}else if("2".equals(gatetype)){
                         			gtypestr=MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_cx", request);      
                         		}
                        %> 
                        	<label><%=gtypestr %></label>
                        	<input type="hidden" id="gatetype" name="gatetype" value="<%=gatetype %>" />
                        <%
                        }else{ %>
                        	<label>
	                        <select name="gatetype" id="gatetype"  class="input_bd gatetype"  >
	                        	<option value=""><emp:message key="txgl_wghdpz_tdgl_qxz" defVal="请选择" fileName="txgl"/></option>
	                  			<%
								if(pagefileds!=null&&pagefileds.size()>1){
									for(int i=1;i<pagefileds.size();i++){
									LfPageField pagefid=pagefileds.get(i);
										String channelType = pagefid.getSubFieldName();
										if("短信".equals(channelType)){
											channelType = "zh_HK".equals(empLangName)?"SMS":"短信";
										}
										if("彩信".equals(channelType)){
											channelType = "zh_HK".equals(empLangName)?"MMS":"彩信";
										}
								%>
								     <option value="<%=pagefid.getSubFieldValue() %>" <% if(pagefid.getSubFieldValue()!=null&&pagefid.getSubFieldValue().equals(gatetype)){ %> selected="selected" <% } %> ><%=channelType%></option>
								<% 
									}
								}
								
								%>
							</select></label>
						<%} %>
                        </td>
                         <td><%if(!"1".equals(updateorinsert)){ %><font color="red">*</font><%} %></td>
                        </tr>
                        <tr>
	                       <td><span><emp:message key="txgl_wghdpz_tdgl_yys" defVal="运营商：" fileName="txgl"/></span></td>
	                        <td>
	                    <% 
	                        if("1".equals(updateorinsert)){
	                        		String spiscumnstr="";
			                   		if("0".equals(spiscumn)){
	        							//spiscumnstr="移动"; 
	        							  spiscumnstr=MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_yd", request);              			
	                         		}else if("1".equals(spiscumn)){
	                         			//spiscumnstr="联通";  
	                         			 spiscumnstr=MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_lt", request);     
	                         		}else if("21".equals(spiscumn)){
	                         			//spiscumnstr="电信";
	                         			spiscumnstr=MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dx", request);
	                         		}else if("5".equals(spiscumn)){
	                         			//spiscumnstr="国外";
	                         			spiscumnstr=MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_gw", request);
	                         		}
                        %> 
                        	<label><%=spiscumnstr %></label>
                        	<input type="hidden" id="spiscumn" name="spiscumn" value="<%=spiscumn %>" />
                        <%
                        }else{ %>
		                        <label>
		                        <select name="spiscumn" id="spiscumn"  <% if("1".equals(updateorinsert)){ %> disabled="disabled" <%} %>  class="input_bd spiscumn"  >
		                        	<option value=""><emp:message key="txgl_wghdpz_tdgl_qxz" defVal="请选择" fileName="txgl"/></option>
		                  			<option value="0" <%if("0".equals(spiscumn)){ %> selected="selected" <% } %>><emp:message key="txgl_wghdpz_wgyxcspz_yd" defVal="移动" fileName="txgl"/></option>
		                  			<option value="1" <%if("1".equals(spiscumn)){ %> selected="selected" <% } %>><emp:message key="txgl_wghdpz_wgyxcspz_lt" defVal="联通" fileName="txgl"/></option>
		                  			<option value="21" <%if("21".equals(spiscumn)){ %> selected="selected" <% } %>><emp:message key="txgl_wghdpz_wgyxcspz_dx" defVal="电信" fileName="txgl"/></option>
		                  			<option value="5" <%if("5".equals(spiscumn)){ %> selected="selected" <% } %>><emp:message key="txgl_wghdpz_wgyxcspz_gw" defVal="国外" fileName="txgl"/></option>
								</select>
		                        </label>
		                 <% 
		                 }
		                 %>
	                        
	                        </td>
	                        <td><%if(!"1".equals(updateorinsert)){ %><font color="red">*</font><%} %></td>
                     </tr>
                        <tr>
                        <td><span><emp:message key="txgl_wghdpz_tdgl_tdmc" defVal="通道名称：" fileName="txgl"/></span></td>
                     	<td>
                     	<label><input type="text" name="gatename" id="gatename"  maxlength="26" value="<%=gatename %>"  class="input_bd gatename"  />
                     	 <input type="hidden" name="porttype" id="porttype" value="0"/>
                     	 <input type="hidden" name="updateorinsert" id="updateorinsert" value="<%=updateorinsert %>"/>
                     	 <input type="hidden" name="id" id="id" value="<%=id %>"/></label>
                     	</td>
                     	<td><font color="red">*</font></td>
                     </tr>
           	  		 <tr>
           	  		 
                        <td><span><emp:message key="txgl_wghdpz_tdgl_tdhm" defVal="通道号码：" fileName="txgl"/></span></td>
                        <td>
                         <% 
	                        if("1".equals(updateorinsert)){
			                   		
                        %> 
                        	<label><%=spgateNum %></label>
                        	<input type="hidden" id=spageteNum name="spgate" value="<%=spgateNum %>" />
                        <%
                        }else{ %>
                        <label><input type="text" name="spgate" id="spageteNum"  <% if("1".equals(updateorinsert)){ %> readonly="readonly" <%} %>  onkeyup="NumCount();" value="<%=spgateNum %>"  maxlength="21" class="input_bd spageteNum"  />
                        <%} %></label>
                        </td>
                        <td><%if(!"1".equals(updateorinsert)){ %><font color="red">*</font><%} %>
                        &nbsp;&nbsp;<font class="font_size"><emp:message key="txgl_wghdpz_tdgl_tdcd" defVal="通道长度：" fileName="txgl"/><label class="numlength" id="numlength"><%=spgateNum!=null?spgateNum.length():0 %></label></font></td>
                        </tr>
 						<tr>
                    	 	<td><span><emp:message key="txgl_wghdpz_tdgl_zdkzws" defVal="最大扩展位数：" fileName="txgl"/></span></td>
                     		<td><label><input type="text" name="sublen" id="sublen" value="<%=sublen %>" class="input_bd sublen"  maxlength="2" /></label></td>
                     		<td><font color="red">*</font>&nbsp; <span class="tdhmcdkzwsjybcg_span" ><emp:message key="txgl_wghdpz_tdgl_tdhmcdkzwsjybcg" defVal="通道号码长度+扩展位数建议不超过21位" fileName="txgl"/></span></td>
                    	</tr>
                       <tr>
                     	<td><span id="spanname"><%=gtypestr %><emp:message key="txgl_wghdpz_tdgl_qm" defVal="签名：" fileName="txgl"/></span></td>
                        <td>
							<label>
								<input type="text" onfocus="setzhu();" name="signstr" value="<%=isGW?"":(signstr==null?"":signstr)%>" id="signstr" class="input_bd signstr"  maxlength="20" />&nbsp;&nbsp;
							</label>
						</td>
                        <td>
							<%if(signstr!=null&&"1".equals(isShowSync)){
							    if("1".equals(gatetype)){%>
								<input type="button" onclick="confirmSign();" class="syncBtnClass" id="syncsign" name="syncsign" value="<%=tbqm %>"/>
								<%}else if("2".equals(gatetype)){%>
								<input type="button" onclick="confirmMmsSign();" class="syncBtnClass" id="syncsign" name="syncsign" value="<%=tbqm %>"/>
							<%}}%>
							<span class="qmgsbhbjzkhrmwkj_span" >
								<emp:message key="txgl_wghdpz_tdgl_qmgsbhbjzkhrmwkj" defVal="签名格式包含半角中括号,如:[梦网科技]" fileName="txgl"/>
							</span>
						</td>
                     </tr>
                      <tr class="worldSign isEN">
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_zcywdx" defVal="支持英文短信：" fileName="txgl"/></span></td>
                        <td>
                        	&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="isSupportEn" value="1" <%if(isEn){ %>checked <%} %>/><emp:message key="txgl_wghdpz_tdgl_s" defVal="是" fileName="txgl"/>
                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="isSupportEn" value="0" <%if(!isEn){ %>checked <%}%> /><emp:message key="txgl_wghdpz_tdgl_f" defVal="否" fileName="txgl"/>
                        </td>
                     </tr>
                              <%-- 国外通道增加 中英文签名 --%>
                     <tr class="worldSign">
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_zwdxqm" defVal="中文短信签名：" fileName="txgl"/></span></td>
                     	<td>
	                     	<label>
	                     		<input type="text" name="cnSign" id="cnSign" value="<%=signstr==null?"":signstr %>" class="input_bd sign cnSign"  maxlength="20" />
	                     		<select name="cnsignlen" id="cnsignlen" class="input_bd signlen cnsignlen"  >
                     				<%for(int s=3;s<=20;s++){%><option value="<%=s %>" <%if((s+"").equals(signlen2)&&isGW){%> selected="selected" <%} %>><%=s %></option><%}  %>
                     			</select>
	                     	</label>
                     	</td>
                   		<td class="td-relative"><label  class="signlenCount cnsignlenCount" id="cnsignlenCount">0</label>&nbsp;<%if(signstr!=null&&"1".equals(isShowSync)){%><input type="button" onclick="confirmSign();" class="syncBtnClass" id="syncsign" name="syncsign" value="<%=tbqm %>"/><%}%><span class="rmwkj_span" ><emp:message key="txgl_wghdpz_tdgl_qmgsbhbjzkhrmwkj" defVal="短信签名格式包含半角中括号，如：[梦网科技]" fileName="txgl"/></span></td>
                     </tr>
                     <tr class="worldSign">
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_ywdxqm" defVal="英文短信签名：" fileName="txgl"/></span></td>
                     	<td>
	                     	<label>
	                     		<input type="text" name="enSign" id="enSign" value="<%=ensignstr==null?"":ensignstr %>" class="input_bd sign enSign"   maxlength="20" />
	                     		<select name="ensignlen" id="ensignlen" class="input_bd signlen ensignlen"  >
                     				<%for(int s=5;s<=20;s++){%><option value="<%=s %>" <%if((s+"").equals(ensignlen)&&isGW){%> selected="selected" <%} %>><%=s %></option><%}  %>
                     			</select>
	                     	</label>
                     	</td>
                   		<td class="td-relative"><label   class="signlenCount ensignlenCount" id="ensignlenCount">0</label>&nbsp;<span class="rmont_span" ><emp:message key="txgl_wghdpz_tdgl_dxqmgsbhbjzkhrmont" defVal="短信签名格式包含半角中括号，如：[montnets]" fileName="txgl"/></span></td>
                     </tr>
                     <%
                     if(!"1".equals(updateorinsert)||!"2".equals(gatetype)){
                      %>
                      <tr id="treight">
                         <td><span><emp:message key="txgl_wghdpz_tdgl_qmcdms" defVal="签名长度模式：" fileName="txgl"/></span></td>
                        <td>
                       		&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="signMode" value="0" <% if("0".equals(signMode)){ %> checked="checked" <%} %> onclick="changeSignMode(this.value)" />
                        	<emp:message key="txgl_wghdpz_tdgl_zdjs" defVal="自动计算" fileName="txgl"/>
                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="signMode" value="1" <% if("1".equals(signMode)){ %> checked="checked" <%} %> onclick="changeSignMode(this.value)" />
                        	<emp:message key="txgl_wghdpz_tdgl_gdcd" defVal="固定长度" fileName="txgl"/>
                         <input type="hidden" name="eachSign" id="eachSign" value="<%=eachSign %>" />
                        </td>           
                        <td><span id="signlenname" <% if("1".equals(signMode)){ %> class="hidden signlenname" <%} else{%> class="signlenname"  <%} %>><emp:message key="txgl_wghdpz_tdgl_qmcd" defVal="签名长度：" fileName="txgl"/><label class="signlen" id="signlen"><%=signlen2==null||"".equals(signlen2)?"0":isGW?"0":signlen2 %></label></span></td>
                     </tr>
                      <tr id="trnine" <% if("0".equals(signMode)){ %> class="hidden" <%} %>>
                      <td><span><emp:message key="txgl_wghdpz_tdgl_qmcd" defVal="签名长度：" fileName="txgl"/></span></td>
                     	<td>
                     	<label><select name="signlen" id="signlen2" class="signlen2">
                     	<%for(int s=3;s<=20;s++){%><option value="<%=s %>" <% if((s+"").equals(signlen2)&&!isGW){ %> selected="selected" <%} %>><%=s %></option><%}  %>
                     	</select></label>
                     	</td> 
                     	<td></td>
                     	</tr>
                      <tr id="trfour"> 
                        <td><span><emp:message key="txgl_wghdpz_tdgl_qmwzz" defVal="签名位置：" fileName="txgl"/></span></td>
                        <td>
                        	&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="gateprivilege" value="1" <% if("1".equals(gateprivilege)){ %> checked="checked" <%} %> /><emp:message key="txgl_wghdpz_tdgl_qz" defVal="前置" fileName="txgl"/>
                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="gateprivilege" value="0" <% if("0".equals(gateprivilege)){ %> checked="checked" <%} %> /><emp:message key="txgl_wghdpz_tdgl_hz" defVal="后置" fileName="txgl"/>
                        </td>
                     </tr>
                     <tr id="trseven">
                      <td><span><emp:message key="txgl_wghdpz_tdgl_yyysjqm" defVal="由运营商加签名：" fileName="txgl"/></span></td>
                        <td>
                        	&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="signdroptype" value="1" <% if("1".equals(signdroptype)){ %> checked="checked" <%} %> />
                        	<emp:message key="txgl_wghdpz_tdgl_s" defVal="是" fileName="txgl"/>
                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        	<%if(!"1".equals(updateorinsert)||("1".equals(updateorinsert)&&"0".equals(signMode))){ %>
                        	<input type="radio"  name="signdroptype" value="0" <% if("0".equals(signdroptype)){ %> checked="checked" <%} %> />
                        	<emp:message key="txgl_wghdpz_tdgl_f" defVal="否" fileName="txgl"/>
                        	<%} %>
                        <%--<label>
                        	<select name="signdroptype"  id="signdroptype"  class="input_bd" style="width:265px;">
                        		<option value="1" <% if("1".equals(signdroptype)){ %> selected="selected" <% } %>>是</option>
                  				<option value="0"<% if("0".equals(signdroptype)){ %> selected="selected" <% } %>>否</option>
                        	</select>
                        	</label>&nbsp;
                        --%>
                        </td>
                        <td></td>
                        </tr>
                     <tr id="trone">
                       <td><span><emp:message key="txgl_wghdpz_tdgl_zccdx" defVal="支持长短信：" fileName="txgl"/></span></td>
                        <td>
                        	&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="longsms" value="1" <% if("1".equals(longsms)){ %> checked="checked" <%} %> />
                        	<emp:message key="txgl_wghdpz_tdgl_s" defVal="是" fileName="txgl"/>
                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="longsms" value="0" <% if("0".equals(longsms)){ %> checked="checked" <%} %> />
                        	<emp:message key="txgl_wghdpz_tdgl_f" defVal="否" fileName="txgl"/>
                        <%--<label>
                        <select name="longsms" id="longsms"  class="input_bd" style="width:265px;" >
                  			<option value="1" <%if("1".equals(longsms)){ %> selected="selected" <% } %>>是</option>
                  			<option value="0" <%if("0".equals(longsms)){ %> selected="selected" <% } %>>否</option>
						</select></label>
                        --%>
                        </td>
                        <td></td>
                        </tr>
                        
                          <tr id="trtwo">
                      <td><span><emp:message key="txgl_wghdpz_tdgl_acdxcf" defVal="按长短信拆分：" fileName="txgl"/></span></td>
                        <td>
                        	&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="splitRule" value="1" <% if("1".equals(splitRule)){ %> checked="checked" <%} %> />
                        	<emp:message key="txgl_wghdpz_tdgl_s" defVal="是" fileName="txgl"/>
                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="splitRule" value="0" <% if("0".equals(splitRule)){ %> checked="checked" <%} %> />
                        	<emp:message key="txgl_wghdpz_tdgl_f" defVal="否" fileName="txgl"/>
                        <%--<label>
                        <select name="splitRule" id="splitRule" class="input_bd" style="width:265px;" >
                        	<option value="1"  <% if("1".equals(splitRule)){ %> selected="selected" <% } %>>是</option>
                        	<option value="0"  <% if("0".equals(splitRule)){ %> selected="selected" <% } %>>否</option>
						</select></label>
                        --%>
                        </td>
                        <td></td>
                     </tr>
                        
                          <tr id="trthree">
                      <td><span><emp:message key="txgl_wghdpz_tdgl_zczttj" defVal="支持整条提交：" fileName="txgl"/></span></td>
                        <td>
                        	&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="endsplit" value="1" <% if("1".equals(endsplit)){ %> checked="checked" <%} %> />
                        	<emp:message key="txgl_wghdpz_tdgl_s" defVal="是" fileName="txgl"/>
                        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        	<input type="radio"  name="endsplit" value="0" <% if("0".equals(endsplit)){ %> checked="checked" <%} %> />
                        	<emp:message key="txgl_wghdpz_tdgl_f" defVal="否" fileName="txgl"/>
                        <%--<label>
                        <select name="endsplit" id="endsplit"  class="input_bd" style="width:265px;" >
                        	<option value="0" <% if("0".equals(endsplit)){ %> selected="selected" <% } %>>否</option>
                  			<option value="1" <% if("1".equals(endsplit)){ %> selected="selected" <% } %>>是</option>
						</select></label>
                        --%>
                        </td>
                        <td></td>
                        </tr>
                        
                         <tr id="trsix">
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_dtdxzss" defVal="单条短信字数：" fileName="txgl"/></span></td>
                        <td><label><input type="text" value="<%=singlelen==null||"".equals(singlelen)? "70" :singlelen %>" name="singlelen" id="singlelen" class="input_bd singlelen"   maxlength="3" />&nbsp;&nbsp;</label></td>
                        <%--<td><span>流速：</span></td>
                        <td id="liusu"><input type="text" name="speed" id="speed" value="0.1" />条/秒</td>
                     	--%>
                     	<td><font color="red">*</font>
                        <font class="font_red_size"><emp:message key="txgl_wghdpz_tdgl_gxddxcfgzqayystg" defVal="关系到短信拆分规则,请按运营商提供的字数填写" fileName="txgl"/></font></td>
                     </tr>
                     <%-- 国外通道新增短信字数 --%>
                     <tr class="worldSingle">
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_zwdtdxzs" defVal="中文单条短信字数：" fileName="txgl"/></span></td>
                        <td><label><input type="text" value="<%=singlelen==null||"".equals(singlelen)? "70" :singlelen %>" name="cnsinglelen" id="cnsinglelen" class="input_bd cnsinglelen"  maxlength="2" />&nbsp;&nbsp;</label></td>
                     	<td><font color="red">*</font>
                        <font class="font_red_size"><emp:message key="txgl_wghdpz_tdgl_gxddxcfgzqayystg" defVal="关系到短信拆分规则,请按运营商提供的字数填写" fileName="txgl"/></font></td>
                     </tr>
                     <tr class="worldSingle">
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_ywdtdxzs" defVal="英文单条短信字数：" fileName="txgl"/></span></td>
                        <td>
	                        <label>
	                        <select name="ensinglelen" id="ensinglelen" class="input_bd ensinglelen"  >
	                        	<option value="160" <%if("160".equals(ensinglelen)){out.print("selected");} %>>160</option>
	                        	<option value="140" <%if("140".equals(ensinglelen)){out.print("selected");} %>>140</option>
	                        </select>
	                        </label>
                        </td>
                     	<td><font color="red">*</font>
                        <font class="font_red_size"><emp:message key="txgl_wghdpz_tdgl_gxddxcfgzqayystg" defVal="关系到短信拆分规则,请按运营商提供的字数填写" fileName="txgl"/></font></td>
                     </tr>
                     
                     <tr>
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_hcfzwzdzs" defVal="后拆分中文最大字数：" fileName="txgl"/></span></td>
                        <td><label><input type="text" value="<%=espLitMaxWd==null||"".equals(espLitMaxWd)? "360" :espLitMaxWd %>" name="esplitmaxwd" id="esplitmaxwd" class="input_bd esplitmaxwd"   maxlength="4" />&nbsp;&nbsp;</label></td>
                     	<td><font color="red">*</font>
                        <font class="font_red_size"><emp:message key="txgl_wghdpz_tdgl_ztdjptzwdxzdzsqzfw67" defVal="整条递交平台中文短信最大字数，取值范围（67－1000字）" fileName="txgl"/></font></td>
                     </tr>
                     
                     <tr class="enmaxwd">
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_hcfywzdzs" defVal="后拆分英文最大字数：" fileName="txgl"/></span></td>
                        <td><label><input type="text" value="<%=espLitEnMaxWd==null||"".equals(espLitEnMaxWd)? "720" :espLitEnMaxWd %>" name="esplitenmaxwd" id="esplitenmaxwd" class="input_bd esplitenmaxwd"  maxlength="4" />&nbsp;&nbsp;</label></td>
                     	<td><font color="red">*</font>
                        <font class="font_red_size"><emp:message key="txgl_wghdpz_tdgl_ztdjptzwdxzdzsqzfw143" defVal="整条递交平台英文短信最大字数，取值范围（143-2000字）" fileName="txgl"/></font></td>
                     </tr>
                     
                     <%
                     }
                      %>
                       <tr>
                       	<td><span><emp:message key="txgl_wghdpz_tdgl_fl" defVal="费率：" fileName="txgl"/></span></td>
                        <td><label><input type="text" name="fee" id="fee" value="<%=fee %>"  class="input_bd fee"  maxlength="4"/></label></td>
                        <td><font class="yt_font"><emp:message key="txgl_wghdpz_tdgl_yt" defVal="元/条" fileName="txgl"/></font></td>
                        <%-- 通道优先级 --%>
                        <input type="hidden" name="riselevel" id="riselevel" value="<%=riselevel %>" />
                        <input type="hidden" name="maxwords" value="1000" id="maxwords"  />
                   		<input type="hidden"  name="status" value="<%=status %>" id="status"  />
                     </tr>
                     
                     <tr class="zhu_tr">
                     	<td></td>
					<td colspan="2"><span id="zhu" class="zhu"><emp:message key="txgl_wghdpz_tdgl_zystdxxqatdyystg" defVal="注：以上通道信息请按通道运营商提供的准确数据录入" fileName="txgl"/></span></td>
                     </tr>
                     <tr align="right"  class="passageOk_tr">
                     	<td colspan="2" id="btn">
                     		<input type="button" id="passageOk"  value="<%=yl %>" onclick="checkAddPassage()" class="btnClass5 mr23"/>
                     		<input type="button" onclick="back()" value="<%=fh %>" class="btnClass6"/>
                     	</td>
                     	<td></td>		
                     </tr>
              </thead>
            </table>
            </form>
          </div>
    	</div>
    	<div id="gateyl" title="<emp:message key="txgl_wghdpz_tdgl_yl" defVal="预览" fileName="txgl"/>" class="gateyl">
    		<p class="td-tips div_bd div_bg">
				<emp:message key="txgl_wghdpz_tdgl_qnrzhdlrdtdxxsfzq" defVal="请您认真核对录入的通道信息是否准确，特别是" fileName="txgl"/>
				<span class="red"><emp:message key="txgl_wghdpz_tdgl_tdlx" defVal="通道类型" fileName="txgl"/></span>、
				<span class="red"><%=yys %></span>、
				<span class="red"><%=tdhm %></span>&nbsp;
				<emp:message key="txgl_wghdpz_tdgl_ydlrbchbrxxg" defVal="一旦录入保存后不允许修改；" fileName="txgl"/>
				<span class="red"><%=dxqm%></span>、
				<span class="red"><emp:message key="txgl_wghdpz_tdgl_qmwz" defVal="签名位置" fileName="txgl"/></span>、
				<span class="red"><emp:message key="txgl_wghdpz_tdgl_dtdxzs" defVal="单条短信字数" fileName="txgl"/></span>&nbsp;
				<emp:message key="txgl_wghdpz_tdgl_sjdbbdzqwbtxzq" defVal="涉及到报表对账，请务必填写准确！" fileName="txgl"/>
			</p>
			<table class="tdlxx_table">
   			 	<thead>
   			 		<tr></tr>
   			 	 	<tr>
                     <td><span class="span_color"><emp:message key="txgl_wghdpz_tdgl_tdlxx" defVal="通道类型：" fileName="txgl"/></span></td>
                        <td>
                        <label id="ygatetype" class="span_color"></label>
                      	</td>
                       <td><span class="span_color"><emp:message key="txgl_wghdpz_tdgl_yys" defVal="运营商：" fileName="txgl"/></span></td>
                        <td>
                         <label id="yspisuncm" class="span_color"></label>
                       </td>
                     </tr>
           	  		 <tr>
           	  		  <td><span><emp:message key="txgl_wghdpz_tdgl_tdmc" defVal="通道名称：" fileName="txgl"/></span></td>
                      <td><label id="ygatename" class="maxWidth"></label></td>
                      <td><span  class="span_color"><emp:message key="txgl_wghdpz_tdgl_tdhm" defVal="通道号码：" fileName="txgl"/></span></td>
                      <td><label id="yspgate" class="span_color"></label></td>
                     </tr>
                     <tr>
                     <td><span><emp:message key="txgl_wghdpz_tdgl_zdkzws" defVal="最大扩展位数：" fileName="txgl"/></span></td>
                     	<td><label id="ysublen"></label><emp:message key="txgl_wghdpz_tdgl_w" defVal="位" fileName="txgl"/></td>
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_zcywdx" defVal="支持英文短信：" fileName="txgl"/></span></td>
                        <td><label id="yisen"></label></td>
                                  
                     </tr>
                      <tr id="ytrone">
	                     <td><span   class="rpLang span_color"><emp:message key="txgl_wghdpz_tdgl_dxqm" defVal="短信签名：" fileName="txgl"/></span></td>
	                     <td><label id="ysignstr"  class="maxWidth span_color"></label></td>
	                     <td><span><emp:message key="txgl_wghdpz_tdgl_qmcdms" defVal="签名长度模式：" fileName="txgl"/></span></td>
	                        <td>
	                        <label id="ysigntype"></label>
	                     </td>    
                     </tr>
                       <tr id="ytrthree">
                        <td><span class="span_color"><emp:message key="txgl_wghdpz_tdgl_qmwzz" defVal="签名位置：" fileName="txgl"/></span></td>
                        <td>
                        <label id="ygateprivilege" class="span_color"></label>
                        </td>
                       <td><span><emp:message key="txgl_wghdpz_tdgl_yyysjqm" defVal="由运营商加签名：" fileName="txgl"/></span></td>
                        <td>
                         <label id="ysigndroptype"></label>
                        </td>
                     </tr>
                       <tr id="ytrfour">
                       <td><span><emp:message key="txgl_wghdpz_tdgl_zccdx" defVal="支持长短信：" fileName="txgl"/></span></td>
                        <td> <label id="ylongsms"></label>
                        </td>
                        <td><span><emp:message key="txgl_wghdpz_tdgl_acdxcf" defVal="按长短信拆分：" fileName="txgl"/></span></td>
                        <td><label id="ysplitrule"></label>
                        </td>
                     </tr>
                     <tr>
                      	<td><span><emp:message key="txgl_wghdpz_tdgl_zczttj" defVal="支持整条提交：" fileName="txgl"/></span></td>
                        <td><label id="yendsplit"></label></td>
                        <td><span  class="rpLang span_color"><emp:message key="txgl_wghdpz_tdgl_dtdxzss" defVal="单条短信字数：" fileName="txgl"/></span></td>
                        <td><label id="ysinglelen" class="span_color"></label></td>
                     </tr>
                     <tr>
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_fl" defVal="费率：" fileName="txgl"/></span></td>
                        <td><label id="yfee"></label><emp:message key="txgl_wghdpz_tdgl_yt" defVal="元/条" fileName="txgl"/></td>
                        <td><span  class="rpLang span_color"><emp:message key="txgl_wghdpz_tdgl_hcfdzdzs" defVal="后拆分最大字数：" fileName="txgl"/></span></td>
                        <td><label id="esplitmaxwdlen" class="span_color"></label></td>
                     </tr>
					<tr>
					 <td colspan="4" class="bt1_td" align="center">
			    		<input id="bt1" class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="submitForm();"/>
					    <input id="bt2" onclick="btcancel();" class="btnClass6" type="button" value="<%=qx %>" />
					    <br/>
			    	</td>
					</tr>
              </thead>
            </table>
			
			</div>
			
			<div id="gateylc" title="<emp:message key="txgl_wghdpz_tdgl_yl" defVal="预览" fileName="txgl"/>" class="gateylc">
			<p class="td-tips div_bd div_bg">
					<emp:message key="txgl_wghdpz_tdgl_qnrzhdlrdtdxxsfzq" defVal="请您认真核对录入的通道信息是否准确，特别是" fileName="txgl"/><span class="red"><%=tdlx %></span>、<span class="red"><%=yys %></span>、<span class="red"><%=tdhm %></span><emp:message key="txgl_wghdpz_tdgl_ydlrbchbrxxg" defVal="一旦录入保存后不允许修改；" fileName="txgl"/>
				</p>
			<center>
			<table class="tdgl_tdlx_table">
   			 	<thead>
   			 		<tr></tr>
   			 	 	<tr>
                     <td><span class="span_color"><emp:message key="txgl_wghdpz_tdgl_tdlx" defVal="通道类型：" fileName="txgl"/></span></td>
                        <td>
                        <label id="ycgatetype" class="span_color"></label>
                      	</td>
                       <td><span class="span_color"><emp:message key="txgl_wghdpz_tdgl_yys" defVal="运营商：" fileName="txgl"/></span></td>
                        <td>
                         <label id="ycspisuncm" class="span_color"></label>
                       </td>
                     </tr>
           	  		 <tr>
           	  		  <td><span><emp:message key="txgl_wghdpz_tdgl_tdmc" defVal="通道名称：" fileName="txgl"/></span></td>
                      <td><label id="ycgatename" class="maxWidth"></label></td>
                      <td><span class="span_color"><emp:message key="txgl_wghdpz_tdgl_tdhm" defVal="通道号码：" fileName="txgl"/></span></td>
                      <td><label id="ycspgate" class="span_color"></label></td>
                     </tr>
                     <tr>
                     <td><span><emp:message key="txgl_wghdpz_tdgl_zdkzws" defVal="最大通道扩展位数：" fileName="txgl"/></span></td>
                     	<td><label id="ycsublen"></label><emp:message key="txgl_wghdpz_tdgl_w" defVal="位" fileName="txgl"/></td>
                     	<td><span class="rpLang"><emp:message key="txgl_wghdpz_tdgl_cxqm" defVal="彩信签名：" fileName="txgl"/></span></td>
                        <td><label id="ycsignstr" class="maxWidth"></label></td>
                                  
                     </tr>
                     <tr>
                     	<td><span><emp:message key="txgl_wghdpz_tdgl_fl" defVal="费率：" fileName="txgl"/></span></td>
                        <td><label id="ycfee"></label><emp:message key="txgl_wghdpz_tdgl_yt" defVal="元/条" fileName="txgl"/></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                     </tr>
					<tr>
					 <td colspan="4" class="bt3_td" align="center">
			    		<input id="bt3" class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="submitForm();"/>
					    <input id="bt4" onclick="btcancel();" class="btnClass6" type="button" value="<%=qx %>" />
					    <br/>
			    	</td>
					</tr>
              </thead>
            </table>
			</center>
			</div>
			
		    <div id="syncsigntip" title="<%=ts %>" class="syncsigntip">
			<center>
			<table class="tdgl_bd_table" border="0">
   			 	<thead>
   			 	    <tr>
   			 	       <td><emp:message key="txgl_wghdpz_tdgl_bd" defVal="本地" fileName="txgl"/></td>
   			 	       <td></td>
   			 	       <td><%=yys %></td>
   			 	       <td></td>
   			 	    </tr> 
   			 	    <tr>
                     <td><span><emp:message key="txgl_wghdpz_tdgl_zcywdx" defVal="支持英文短信：" fileName="txgl"/></span></td>
                        <td>
                        <label id="localsupporten" ></label>
                      	</td>
                       <td><span ><emp:message key="txgl_wghdpz_tdgl_zcywdx" defVal="支持英文短信：" fileName="txgl"/></span></td>
                        <td>
                         <label id="yyssupporten"></label>
                       </td>
                     </tr>  
   			 	 	<tr>
                     <td><span><emp:message key="txgl_wghdpz_tdgl_zwdxqm" defVal="中文短信签名：" fileName="txgl"/></span></td>
                        <td>
                        <label id="localcnsign" ></label>
                      	</td>
                       <td><span ><emp:message key="txgl_wghdpz_tdgl_zwdxqm" defVal="中文短信签名：" fileName="txgl"/></span></td>
                        <td>
                         <label id="yyscnsign"></label>
                       </td>
                     </tr>
                     <tr class="en-tr">
                     <td><span><emp:message key="txgl_wghdpz_tdgl_ywdxqm" defVal="英文短信签名：" fileName="txgl"/></span></td>
                        <td>
                        <label id="localensign" ></label>
                      	</td>
                       <td><span ><emp:message key="txgl_wghdpz_tdgl_ywdxqm" defVal="英文短信签名：" fileName="txgl"/></span></td>
                        <td>
                         <label id="yysensign"></label>
                       </td>
                     </tr>
                      <tr>
                     <td><span><emp:message key="txgl_wghdpz_tdgl_zwqmcd" defVal="中文签名长度：" fileName="txgl"/></span></td>
                     	<td><label id="localcnlength"></label></td>
                     	<td><span ><emp:message key="txgl_wghdpz_tdgl_zwqmcd" defVal="中文签名长度：" fileName="txgl"/></span></td>
                        <td><label id="yyscnlength"></label></td>
                                  
                     </tr>
                     <tr class="en-tr">
                     <td><span><emp:message key="txgl_wghdpz_tdgl_ywqmcd" defVal="英文签名长度：" fileName="txgl"/></span></td>
                     	<td><label id="localenlength"></label></td>
                     	<td><span ><emp:message key="txgl_wghdpz_tdgl_ywqmcd" defVal="英文签名长度：" fileName="txgl"/></span></td>
                        <td><label id="yysenlength"></label></td>
                                  
                     </tr>
                       <tr>
                     <td><span ><emp:message key="txgl_wghdpz_tdgl_qmcdms" defVal="签名长度模式：" fileName="txgl"/></span></td>
                        <td>
                        <label id="locallenmode" ></label>
                      	</td>
                       <td><span><emp:message key="txgl_wghdpz_tdgl_qmcdms" defVal="签名长度模式：" fileName="txgl"/></span></td>
                        <td>
                         <label id="yyslenmode"></label>
                       </td>
                     </tr>
           	  		 <tr>
           	  		  <td><span><emp:message key="txgl_wghdpz_tdgl_qmwz" defVal="签名位置：" fileName="txgl"/></span></td>
                      <td><label id="localqmwz"></label></td>
                      <td><span><emp:message key="txgl_wghdpz_tdgl_qmwz" defVal="签名位置：" fileName="txgl"/></span></td>
                      <td><label id="yysqmwz" ></label></td>
                     </tr>
                      <tr>
           	  		  <td colspan="4"><label id="tishi"></label></td>
                 
                     </tr>
					<tr>
					 <td colspan="4" class="bt3_td" align="center">
			    		<input id="bt3" class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="syncSign();"/>
					    <input id="bt4" onclick="btcancel();" class="btnClass6" type="button" value="<%=qx %>" />
					    <br/>
			    	</td>
					</tr>
              </thead>
            </table>
			</center>
			</div>
			
		   <div id="syncmmssigntip" title="<%=ts %>" class="syncmmssigntip">
			<center>
			<table class="tdgl_bd_table" border="0">
   			 	<thead>
   			 	    <tr>
   			 	       <td><emp:message key="txgl_wghdpz_tdgl_bd" defVal="本地" fileName="txgl"/></td>
   			 	       <td></td>
   			 	       <td><%=yys %></td>
   			 	       <td></td>
   			 	    </tr>   
   			 	 	<tr>
                     <td><span><emp:message key="txgl_wghdpz_tdgl_cxqm" defVal="彩信签名：" fileName="txgl"/></span></td>
                        <td>
                        <label id="localmmscnsign" ></label>
                      	</td>
                       <td><span ><emp:message key="txgl_wghdpz_tdgl_cxqm" defVal="彩信签名：" fileName="txgl"/></span></td>
                        <td>
                         <label id="yysmmscnsign"></label>
                       </td>
                     </tr>
                      <tr>
           	  		  <td colspan="4"><label id="mmstishi"></label></td>
                     </tr>
					<tr>
					 <td colspan="4" class="bt3_td" align="center">
			    		<input id="bt3" class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="syncMmsSign();"/>
					    <input id="bt4" onclick="btcancel();" class="btnClass6" type="button" value="<%=qx %>" />
					    <br/>
			    	</td>
					</tr>
              </thead>
            </table>
			</center>
			</div>
			
    	<div class="bottom"><div id="bottom_right"><div id="bottom_left"></div></div></div>
	</div><%--end round_content--%>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=iPath%>/js/passage.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	
	<script>
	//取消
		function btcancel()
		{
			$('#gateyl').dialog('close');	
			$('#gateylc').dialog('close');	
			$('#syncsigntip').dialog('close');
			$('#syncmmssigntip').dialog('close');	
		}
	
	$(document).ready(function(){
		$("input:text").keyup(function(){
			if($(this).val()!="" && $(this).attr("id")!="signstr")
			{
				//$("#zhu").html("注：以上通道信息请按通道运营商提供的准确数据录入");
				$("#zhu").html(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_1"));
			}
		});
		
		$('#gateylc').dialog({
				autoOpen: false,
				width:700,
				height:300,
				modal:true,
				resizable:false
		});
		
		$('#gateyl').dialog({
				autoOpen: false,
				width:<%="zh_HK".equals(empLangName)?800:760%>,
				height:<%="zh_HK".equals(empLangName)?420:400%>,
				modal:true,
				resizable:false
		});
		
		$('#syncsigntip').dialog({
				autoOpen: false,
				width:700,
				height:400,
				modal:true,
				resizable:false,
                open:function(){
                    //不支持英文短信 则同步对比弹出框 不展示英文短信内容和长度模式
                    //if(!isEn){
                        //$('#syncsigntip .en-tr').hide();
                    //}
                }
		});
		
		$('#syncmmssigntip').dialog({
				autoOpen: false,
				width:700,
				height:200,
				modal:true,
				resizable:false
		});
		
		$("#gatetype").change(function(){
			setSign();
		});
	});
	
	function back(){
		location.href='<%=path %>/pas_passage.htm?method=find&isback=1&lguserid=<%=request.getParameter("lguserid") %>&lgcorpcode=<%=request.getParameter("lgcorpcode") %>';
	}
	
	function syncSign()
	{
	    var isNeedUpdateSign=$("#isNeedUpdateSign").val();
	    if(isNeedUpdateSign==0){
	    	//alert("本地签名信息和运营商签名信息一致，无需同步。");
	    	alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_13"));
	    	return;
	    }else if(isNeedUpdateSign==1)
	    {
	       //if(!confirm("本地签名信息和运营商签名信息不一致，是否同步运营商的签名？"))
	        if(!confirm(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_3")))
	        {
	        	return;
	        }
	    }else
	    {
	    	return;
	    }
	    //运营商
		var spiscumn=$('#spiscumn').val();
		//通道号码
		var spageteNum=$('#spageteNum').val();
		 $.post("pas_passage.htm?method=syncSign&spageteNum="+spageteNum+"&spiscumn="+spiscumn+"&gateid=<%=gateID%>",
				{},
				function(result)
				{
					if(result!=""&&result=="success")
				    {
					    //alert("同步签名成功！");
					     alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_4"));
					     location.href = location.href;
				    }else if(result!=""&&result=="fail")
				    {
				    	 //alert("同步签名失败！");
				    	  alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_5"));
				    }else if(result!=""&&result=="noBind")
				    {
				         //alert("该通道未和通道账户绑定，无法同步签名！");
				         alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_6"));
				    }else{
				    	 //alert("同步签名失败！");
				    	 alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_4"));
				    }
				}
			);
	}
	
	function confirmSign()
	{  
			 //运营商
			var spiscumn=$('#spiscumn').val();
			//通道号码
			var spageteNum=$('#spageteNum').val();
			$.post("pas_passage.htm?method=confirmSign&spageteNum="+spageteNum+"&spiscumn="+spiscumn+"&gateid=<%=gateID%>",
			{},
			function(result){
				if(result!=""&&result.indexOf("success")==0)
				    {
				         var isNeedUpdateSign=0;
				    	 var signArr = result.split("|");
				    	 
				    	 //--------------处理是否支持英文--------------
				    	 //本地是否支持英文
				    	 var localisEn = <%=isEn%>;
				    	 //运营商是否支持英文
				    	 var yysisEn=true;
				    	 //大于0，运营商支持英文
				    	 if(signArr[7]!=0)
				    	 {
				    	 	yysisEn=true;
				    	 }else
				    	 {
				    	 	yysisEn=false;
				    	 }
				    	 //设置
				    	 //运营商和本地都不支持英文。
				    	 var isEn=true;
				    	 if(!yysisEn&&!localisEn){
				    	    //运营商和本地都不支持英文。
				    	 	isEn=false;
				    	 }else{
				    	 	isEn=true;
				    	 }
				    	 //如果都为false，则不显示英文签名及英文签名长度
				    	 if(!isEn)
				    	 {
                        	$('#syncsigntip .en-tr').hide();
                   		 }
                   		 
                   		 //本地是否支持英文显示
                   		 if(localisEn)
                   		 {
                   		 	 //$("#localsupporten").text("是");
                   		 	 $("#localsupporten").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_7"));
                   		 }
                   		 else
                   		 {
                   		 	 //$("#localsupporten").text("否");
                   		 	 $("#localsupporten").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_8"));
                   		 }
                   		 //运营商是否支持英文显示
                   		 if(yysisEn)
                   		 {
                   		 	 //$("#yyssupporten").text("是");
                   		 	 $("#yyssupporten").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_7"));
                   		 }
                   		 else
                   		 {
                   		 	 //$("#yyssupporten").text("否");
                   		 	 $("#yyssupporten").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_8"));
                   		 }
                   		 //如果本地和运营商不一致，则显示红色，提示
                   		 if((localisEn&&!yysisEn)||(!localisEn&&yysisEn))
                   		 {
                   		 	isNeedUpdateSign=1;
				    	  	$("#localsupporten").css('color','red');
				    	  	$("#yyssupporten").css('color','red');
                   		 }
                   		 //--------------处理是否支持英文--------------
				    	 
				    	 
				    	 
				    	 //运营商中文短信签名
				    	 $("#yyscnsign").text(signArr[6]);
					    	 //本地中文短信签名
					     $("#localcnsign").text("<%=signstr==null?"":signstr.replaceAll("\\\\","\\\\\\\\") %>");
				    	 
				    	  if(signArr[6]!="<%=signstr%>")
				    	  {
				    	    isNeedUpdateSign=1;
				    	  	$("#yyscnsign").css('color','red');
				    	  	$("#localcnsign").css('color','red');
				    	  }

				    	 if(spiscumn==5){
				    	    //本地英文短信签名
				    	    if(localisEn)
				    	    {
				    	 	$("#localensign").text("<%=ensignstr==null?"":ensignstr.replaceAll("\\\\","\\\\\\\\") %>");
				    	 	}else
				    	 	{
				    	 	$("#localensign").text("-");
				    	 	}
				    	 	 //运营商英文短信签名
				    	 	if(yysisEn)
				    	 	{
				    	    $("#yysensign").text(signArr[8]);
				    	    }else
				    	    {
				    	    $("#yysensign").text("-");
				    	    }

				    	     if(isEn && signArr[8]!="<%=ensignstr%>")
					    	{
					    	    isNeedUpdateSign=1;
					    	  	$("#yysensign").css('color','red');
					    	  	$("#localensign").css('color','red');
					    	}
				    	 }

				    	var yyslenmode;
				    	//本地英文签名长度
				    	var localenlength=0;
				    	//运营商英文签名长度
				    	var yysenlength=0;
				    	  if(signArr[5]!=0)
				    	 {
				    	 	 yyslenmode="1";
				    	 	 //运营商签名长度模式
				    	 	 //$("#yyslenmode").text("固定长度");
				    	 	 $("#yyslenmode").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_9"));
				    	 	 //运营商中文签名长度
				    	 	 $("#yyscnlength").text(signArr[5]);
				    	 	 if(spiscumn==5&&yysisEn){
					    	 	 //运营商英文签名长度
					    	 	 $("#yysenlength").text(signArr[5]*2);
					    	 	 yysenlength=signArr[5]*2;
				    	 	 }else{
				    	 	 	 //运营商英文签名长度
					    	 	 $("#yysenlength").text("-");
					    	 	 yysenlength=0;
				    	 	 }
				    	 }else
				    	 {
				    	     yyslenmode="0";
				    	     //运营商签名长度模式
				    	 	 //$("#yyslenmode").text("自动计算");
				    	 	 $("#yyslenmode").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_10"));
				    	 	 //运营商中文签名长度
				    	 	 $("#yyscnlength").text(signArr[6].length);
				    	 	  if(spiscumn==5&&yysisEn){
					    	 	 //运营商英文签名长度
                                  yysenlength=signArr[8].replace(/[\[\]\|\^\{\}\\\~]/g, "**").length;
					    	 	 $("#yysenlength").text(yysenlength);

				    	 	 }else{
				    	 	 	 //运营商英文签名长度
					    	 	 $("#yysenlength").text("-");
					    	 	  yysenlength=0;
				    	 	 }
				    	 }
				    	 
				    	 <% if("0".equals(signMode)){ %>
				    	     //本地签名长度模式
				    	 	 //$("#locallenmode").text("自动计算");
				    	 	 $("#locallenmode").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_10"));
				    	 <%}else{%>
				    	     //本地签名长度模式
				    	     //$("#locallenmode").text("固定长度");
				    	     $("#locallenmode").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_9"));
				    	 <%}%>
				    	  //本地中文签名长度
				    	  $("#localcnlength").text("<%=signlen2%>");
				    	   if(spiscumn==5&&localisEn){
					    	  //本地英文签名长度
					    	  $("#localenlength").text("<%=ensignlen%>");
					    	  localenlength="<%=ensignlen%>";
				    	  }else{
				    	  	   $("#localenlength").text("-");
				    	  	   localenlength=0;
				    	  }
				    	  
				    	if(yyslenmode!="<%=signMode%>")
				    	{
				    	    isNeedUpdateSign=1;
				    	  	$("#yyslenmode").css('color','red');
				    	  	$("#yyscnlength").css('color','red');
				    	  	$("#locallenmode").css('color','red');
				    	  	$("#localcnlength").css('color','red');
				    	  	if(isEn && yysenlength!=localenlength)
				    	  	{
				    	  		isNeedUpdateSign=1;
				    	  		$("#yysenlength").css('color','red');
				    	  	    $("#localenlength").css('color','red');
				    	  	}
				    	}else
				    	{
				    	    //自动计算
				    		if(yyslenmode=="0")
				    		{
				    			if(signArr[6].length!="<%=signlen2%>")
				    			{
				    			    isNeedUpdateSign=1;
				    				$("#yyscnlength").css('color','red');
				    				$("#localcnlength").css('color','red');
				    			}
				    			
				    			if(isEn && yysenlength!=localenlength)
				    			{
				    			    isNeedUpdateSign=1;
				    				$("#yysenlength").css('color','red');
				    				$("#localenlength").css('color','red');
				    			}
				    			
				    		}else{
				    			if(signArr[5]!="<%=signlen2%>")
				    			{
				    			    isNeedUpdateSign=1;
				    				$("#yyscnlength").css('color','red');
				    				$("#localcnlength").css('color','red');
				    			}
				    			
				    			if(isEn && yysenlength!=localenlength)
				    			{
				    			    isNeedUpdateSign=1;
				    				$("#yysenlength").css('color','red');
				    				$("#localenlength").css('color','red');
				    			}
				    		}
				    	}
				    	var yysqmwzStr="";
				    	 //运营商签名位置
				    	 if(signArr[9]==0)
				    	 {
				    	 	 //$("#yysqmwz").text("后置");
				    	 	 $("#yysqmwz").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_11"));
				    	 	 yysqmwzStr="0";
				    	 }else
				    	 {
				    	 	 //$("#yysqmwz").text("前置");
				    	 	 $("#yysqmwz").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_12"));
				    	 	 yysqmwzStr="1";
				    	 }
				    	 
				    	 //本地签名位置
				    	 <% if("1".equals(gateprivilege)){ %>
				    	 	 //$("#localqmwz").text("前置");
				    	 	 $("#localqmwz").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_12"));
				    	 <%}else{%>
				    	      //$("#localqmwz").text("后置");
				    	     $("#localqmwz").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_11"));
				    	 <%}%>
				    	 
				    	 if(yysqmwzStr!="<%=gateprivilege%>")
				    	{
				    	    isNeedUpdateSign=1;
				    	  	$("#yysqmwz").css('color','red');
				    	  	$("#localqmwz").css('color','red');
				    	}
				    	$("#isNeedUpdateSign").val(isNeedUpdateSign);
				    	
						if(isNeedUpdateSign==0)
						{
						   //$("#tishi").text("本地签名信息和运营商签名信息一致，无需同步。");
						   $("#tishi").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_13"));
						   $("#tishi").css('color','green');
						}else
						{
						    //$("#tishi").text("本地签名信息和运营商签名信息不一致，请仔细核对，再点击确定按钮同步。");
						    $("#tishi").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_14"));
						    $("#tishi").css('color','red');
						}
					     $('#syncsigntip').dialog('open');
					     
				    }else if(result!=""&&result=="fail")
				    {
				    	 //alert("同步签名失败！");
				    	 alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_5"));
				    }else if(result!=""&&result=="noBind")
				    {
				         //alert("该通道未和通道账户绑定，无法同步签名！");
				         alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_6"));
				    }else if(result!=""&&result=="getSignFail")
				    {
				        // alert("获取运营商签名失败！");
				        alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_17")); 
				    }else{
				    	 //alert("同步签名失败！");
				    	 alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_5")); 
				    }
		});
	}
	
	function syncMmsSign()
	{
	    var isNeedUpdateSign=$("#isNeedUpdateSign").val();
	    if(isNeedUpdateSign==0){
	    	//alert("本地签名信息和运营商签名信息一致，无需同步。");
	    	alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_13")); 
	    	return;
	    }else if(isNeedUpdateSign==1)
	    {
	       // if(!confirm("本地签名信息和运营商签名信息不一致，是否同步运营商的签名？"))
	       if(!confirm(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_3")))
	        {
	        	return;
	        }
	    }else
	    {
	    	return;
	    }
	    //运营商
		var spiscumn=$('#spiscumn').val();
		//通道号码
		var spageteNum=$('#spageteNum').val();
		 $.post("pas_passage.htm?method=syncSign&spageteNum="+spageteNum+"&spiscumn="+spiscumn+"&gateid=<%=gateID%>",
				{},
				function(result)
				{
					if(result!=""&&result=="success")
				    {
					    // alert("同步签名成功！");
					    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_4"));  
					            location.href = location.href;
				    }else if(result!=""&&result=="fail")
				    {
				    	 //alert("同步签名失败！");
				    	 alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_5")); 
				    }else if(result!=""&&result=="noBind")
				    {
				         //alert("该通道未和通道账户绑定，无法同步签名！");
				         alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_6")); 
				    }else{
				    	 //alert("同步签名失败！");
				    	 alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_5")); 
				    }
				}
			);
	}
	function confirmMmsSign()
	{  
			 //运营商
			var spiscumn=$('#spiscumn').val();
			//通道号码
			var spageteNum=$('#spageteNum').val();
			$.post("pas_passage.htm?method=confirmSign&spageteNum="+spageteNum+"&spiscumn="+spiscumn+"&gateid=<%=gateID%>",
			{},
			function(result){
				if(result!=""&&result.indexOf("success")==0)
				    {
				         var isNeedUpdateSign=0;
				    	 var signArr = result.split("|");
				    	 
				    	 //运营商彩信签名
				    	 $("#yysmmscnsign").text(signArr[6]);
					    	 //本地中文短信签名
					     $("#localmmscnsign").text("<%=signstr==null?"":signstr%>");
				    	 
				    	  if(signArr[6]!="<%=signstr%>")
				    	  {
				    	    isNeedUpdateSign=1;
				    	  	$("#yysmmscnsign").css('color','red');
				    	  	$("#localmmscnsign").css('color','red');
				    	  }
				    	$("#isNeedUpdateSign").val(isNeedUpdateSign);
				    	
						if(isNeedUpdateSign==0)
						{
						   //$("#mmstishi").text("本地签名信息和运营商签名信息一致，无需同步。");
						   $("#mmstishi").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_13"));
						   $("#mmstishi").css('color','green');
						}else
						{
						    //$("#mmstishi").text("本地签名信息和运营商签名信息不一致，请仔细核对，再点击确定按钮同步。");
						    $("#mmstishi").text(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_14"));
						    $("#mmstishi").css('color','red');
						}
					     $('#syncmmssigntip').dialog('open');
					     
				    }else if(result!=""&&result=="fail")
				    {
				    	 //alert("同步签名失败！");
				    	 alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_5"));
				    }else if(result!=""&&result=="noBind")
				    {
				         //alert("该通道未和通道账户绑定，无法同步签名！");
				         alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_6"));
				    }else if(result!=""&&result=="getSignFail")
				    {
				         //alert("获取运营商签名失败！");
				         alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_17"));
				    }else{
				    	 //alert("同步签名失败！");
				    	  alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdgl_text_5"));
				    }
		});
	}
	</script>
</body>
</html>
