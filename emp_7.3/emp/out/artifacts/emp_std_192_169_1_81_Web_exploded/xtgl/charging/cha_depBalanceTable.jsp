<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.charging.vo.LfDepBalanceVo"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.sysuser.LfDep"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("balanceMgr");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<LfDepBalanceVo> lfDepBalanceVos = (List<LfDepBalanceVo>)request.getAttribute("depBalanceVos");
	
	LfSysuser curSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	LfDep lfDep = (LfDep)request.getAttribute("lfDep");
	String depName = (String)request.getAttribute("depName");
	String balanceAllDepId = (String)request.getAttribute("balanceAllDepId");
	if(depName == null){
	   depName = "";
	}
	if(balanceAllDepId == null)
	{
		balanceAllDepId = "";
	}
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@ SuppressWarnings("unchecked")
	HashMap<String,Long> hashMap= (HashMap<String,Long>)request.getAttribute("hashMap");
	Long sms=0l;
	Long mms=0l;
	if(hashMap!=null){
		 sms=hashMap.get("sms");
		 mms=hashMap.get("mms");
	}
	//新增 获取后台传来的充值回收权限机构idList pengj
	
	List<Long> balancePriDepsIdsList = (List<Long>)request.getAttribute("balancePriDepsIdsList");
	// ------
%>
<html>
	<head>
		<link href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cha_depBalanceTable.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cha_depBalanceTable.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="cha_depBalanceTable">
		<div id="loginUser" class="hidden"></div>
	        <input type="hidden" id="depId"/>
	        <input type="hidden" name = "balanceAllDepId" id="balanceAllDepId" value="<%=balanceAllDepId %>"/>
			<div id="addBalance" title="机构充值" class="hidden addBalance" >
				<center>
			    <table>
			    <tr><td class="xxlx_up_tr_td"></td></tr>
			        <tr>
			           <td class="xxlx_td">信息类型 ： </td>
			           <td align="left" >
			               <input name="addMsgType" align="left" type="radio" id="addSms" value="1" checked="checked">短信&nbsp; &nbsp;<input name="addMsgType" type="radio" value="2">彩信
			            </td>
			        </tr>
			        <tr><td class="czts_up_tr_td"></td></tr>
			        <tr>
			           <td>充值条数 ： </td>
			           <td align="left"><input type="text" id="addCount" class="input_bd" maxlength="9" onkeyup="if(value!=value.replace(/[^\w\/]/g,''))value=value.replace(/[^\w\/]/g,'')"/></td>
			        </tr>
			        <tr><td class="bz_up_tr_td"></td></tr>
			         <tr>
			           <td>备&nbsp;&nbsp;&nbsp;&nbsp;注 ： </td>
			           <td><textarea  id="addMark" cols="20" rows="5" class="addMark"></textarea></td>
			        </tr>
			        <tr><td class="cz_up_tr_td"></td></tr>
			         <tr>
			           <td colspan="2" class="cz_td"><center><input type="button" class="btnClass5 mr23" value="充值" onclick="chongZhi();">
			           <input type="button" class="btnClass6" value="取消" onclick="quXiao('1');"></center></td>
			        </tr>
			    </table>
			    </center>
			</div>
			<div id="delBalance" title="机构回收" class="hidden delBalance" >
			<center>
			    <table>
			    <tr><td class="xxlx2_up_tr_td"></td></tr>
			        <tr>
			           <td>信息类型 ： </td>
			           <td  align="left">
			               <input name="recMsgType"  align="left" type="radio" id="addMms" value="1" checked="checked">短信&nbsp; &nbsp; <input type="radio" name="recMsgType" value="2">彩信
			            </td>
			        </tr>
			        <tr><td class="hsts2_up_tr_td"></td></tr>
			        <tr>
			           <td>回收条数 ： </td>
			           <td><input type="text" id="recCount" class="input_bd" maxlength="9" onkeyup="if(value!=value.replace(/[^\w\/]/g,''))value=value.replace(/[^\w\/]/g,'')"/></td>
			        </tr>
			        <tr><td class="bz2_up_tr_td"></td></tr>
			        <tr>
			           <td>备&nbsp;&nbsp;&nbsp;&nbsp;注 ： </td>
			           <td><textarea id="recMark" class="recMark"  cols="20" rows="5"></textarea></td>
			        </tr>
			         <tr><td class="hs2_up_tr_td"></td></tr>
			        <tr>
			           <td colspan="2" class="hs2_td"><center><input type="button" class="btnClass5 mr23" value="回收" onclick="javascript:huiShou();">
			           <input type="button" class="btnClass6" value="取消" onclick="javascript:quXiao('2');"></center></td>
			        </tr>
			    </table>
			    </center>
			</div>
			<%-- 设置阀值 --%>
			<div id="setAlarm" title="告警阀值设置" class="hidden setAlarm" >
			<center>
			    <table class="setAlarm_table">
			    <tr><td class="smsAlarm_up_tr_td"></td></tr>
			        <tr>
			           <td>短信告警阀值： </td>
			           <td><input id="smsAlarm" name="smsAlarm" value="" maxlength="9" class="inpstyle"/>&nbsp;条</td>
			        </tr>
			        <tr><td class="mmsAlarm_up_tr_td"></td></tr>
			        <tr>
			           <td>彩信告警阀值： </td>
			           <td><input id="mmsAlarm" name="mmsAlarm" value="" maxlength="9"  class="inpstyle"/>&nbsp;条</td>
			        </tr>
			        <tr><td class="alarmName_up_tr_td"></td></tr>
			        <tr>
			           <td>通知人姓名 ： </td>
			           <td><input id="alarmName" name="alarmName" value="" maxlength="6" class="inpstyle"/></td>
			        </tr>
			        <tr><td class="alarmPhone_up_tr_td"></td></tr>
			        <tr>
			           <td>手机号码 ： </td>
			           <td><input id="alarmPhone" name="alarmPhone" value="" maxlength="21" class="inpstyle"/></td>
			        </tr>
			         <tr><td class="alarmButton_up_tr_td"></td></tr>
			        <tr>
			           <td colspan="2" class="alarmButton_td"><center><input id="alarmButton" type="button" class="btnClass5 mr23" value="确定" onclick="alarm();">
			           <input id="deleteAlarmButton" type="button" class="btnClass5 mr23" value="撤销" onclick="deleteAlarm();">
			           <input type="button" class="btnClass6" value="取消" onclick="closeAlarm()"></center></td>
			        </tr>
			    </table>
			    </center>
			</div>
			<%-- 批量充值 --%>
			<div id="addBalanceAllDiv" title="批量充值" class="addBalanceAllDiv" style="display: none;">
				<iframe id="addBalanceAllFrame" name="addBalanceAllFrame" class="addBalanceAllFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
			</div>
			<div id="singledetail" class="singledetail">
				<div id="msg" class="msg"><xmp></xmp></div>
			</div>
				 <table class="rzck_table">
					<tbody>
						<tr >
							<td class="rzck_td">
							<input type="button" class="btnClass4" onclick="javascript:toLog()" value="日志查看"/>
							<input type="button" class="btnClass4" onclick="javascript:addBalanceAll()" value="批量充值"/>
							</td>
							<td class="search_td">
								<input type="text" id="depName" onfocus="javascript:clearDefault()"
									   onblur="javascript:clearDefault()" class = "input_bd div_bg depName"
									   value="<%="".equals(depName)?"输入名称":depName %>" name="depName"/>
								<input type="button" id="search" name="search" class="depBalanceMgrSearchBtn"/>
							</td>
							</tr>	
					</tbody>
				</table>
					<table id="content">
					
						<thead>
							<tr>
								<th>
									机构名称
								</th>
								<th>
									短信余额(条)
								</th>
								<th>
									彩信余额(条)
								</th>
								<th>
									告警阀值
								</th>
								<th>
									通知人
								</th>
								<th>
									操作
								</th>
							</tr>
						</thead>
						<tbody>
						<%
							if(lfDepBalanceVos != null && lfDepBalanceVos.size()>0)
							{
								for(LfDepBalanceVo lfDepBalanceVo : lfDepBalanceVos)
								{
									String name=lfDepBalanceVo.getAlarmName();
									String phone=lfDepBalanceVo.getAlarmPhone();
									Long smsAlarm=lfDepBalanceVo.getSmsAlarm();
									Long mmsAlarm=lfDepBalanceVo.getMmsAlarm();
						%>
									
							<tr>
								<td>
									<%=lfDepBalanceVo.getDepName() %>
								</td>
								<%--<td>
									<%=dep.getDepCodeThird() %>
								</td> --%>
								<td>
									<%=lfDepBalanceVo.getSmsBalance() %>
								</td>
								<td>
									<%=lfDepBalanceVo.getMmsBalance() %>
								</td>
								<td>
									短信：<%=smsAlarm==null?"-":smsAlarm %>&nbsp;&nbsp;彩信：<%=mmsAlarm==null?"-":mmsAlarm  %>
								</td>
								<td>
									<%=(name!=null&&!"".equals(name.trim()))?"<b>"+name+"</b>":"-" %><%=(phone!=null && !"".equals(phone.trim()))?" ["+phone+"]":"" %>
								</td>
								<td>
								    <%
								    	
								    	if("admin".equals(curSysuser.getUserName())){
									        if(lfDep != null && lfDep.getDepId().equals(lfDepBalanceVo.getDepId())){
									        	out.print("<a>-</a>");
									        }else{
									             if(btnMap.get(menuCode+"-1")!=null) {
									               %>
									                <a href="javascript:addBalance('<%=lfDepBalanceVo.getDepId() %>','<%=lfDepBalanceVo.getDepName() %>')">充值</a>
									               <%
									             }
									             if(btnMap.get(menuCode+"-2")!=null) {
									                %>
									               <a href="javascript:delBalance('<%=lfDepBalanceVo.getDepId() %>','<%=lfDepBalanceVo.getDepName() %>')">回收</a>
									               <%
									             }
									             if(btnMap.get(menuCode+"-4")!=null) {
										           if(lfDepBalanceVo.getSmsBalance()+lfDepBalanceVo.getMmsBalance()>0) {  
									        	   %>
										               <a href="javascript:setAlarm('<%=lfDepBalanceVo.getDepId() %>','<%=lfDepBalanceVo.getDepName()%>','<%=smsAlarm==null?"":smsAlarm%>','<%=mmsAlarm==null?"":mmsAlarm%>','<%=name==null?"":name.trim()%>','<%=phone==null?"":phone.trim()%>')">阀值</a>
										               <%
										           }
									            }
									        }
									     // EMP5.7新需求：增加对操作员充值和回收权限   by pengj
								    	}else{  //如果不是admin，那么没有充值回收权限的机构显示”-“
								    		if(lfDep != null && lfDep.getDepId().equals(lfDepBalanceVo.getDepId())){
									        	out.print("<a>-</a>");
									        }else{
									    		if(balancePriDepsIdsList!=null && balancePriDepsIdsList.size()>0 && !balancePriDepsIdsList.contains(lfDepBalanceVo.getDepId())){
										        	out.print("<a>-</a>");
										        }else if(balancePriDepsIdsList==null || balancePriDepsIdsList.size()==0){
										        	out.print("<a>-</a>");
										        }else{
										             if(btnMap.get(menuCode+"-1")!=null) {
										               %>
										                <a href="javascript:addBalance('<%=lfDepBalanceVo.getDepId() %>','<%=lfDepBalanceVo.getDepName() %>')">充值</a>
										               <%
										             }
										             if(btnMap.get(menuCode+"-2")!=null) {
										                %>
										               <a href="javascript:delBalance('<%=lfDepBalanceVo.getDepId() %>','<%=lfDepBalanceVo.getDepName() %>')">回收</a>
										               <%
										             }
										             if(btnMap.get(menuCode+"-4")!=null) {
											           if(lfDepBalanceVo.getSmsBalance()+lfDepBalanceVo.getMmsBalance()>0) {  
										        	   %>
											               <a href="javascript:setAlarm('<%=lfDepBalanceVo.getDepId() %>','<%=lfDepBalanceVo.getDepName()%>','<%=smsAlarm==null?"":smsAlarm%>','<%=mmsAlarm==null?"":mmsAlarm%>','<%=name==null?"":name.trim()%>','<%=phone==null?"":phone.trim()%>')">阀值</a>
											               <%
											           }
										           }
										        }
									    	}
								    	}
								    	// end
								     %>
									
								</td>
							</tr>
						<%			
								}%>
								<%if(btnMap.get(menuCode+"-3")!=null) { %>
									<tr>
									<td align="left"><b>企业所有机构总余额：</b></td>
                                    <td><b><font id="shortMsg"><%=sms %></font></b></td> 
                                    <td><b><font id="MMS"><%=mms %></font></b></td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
								</tr>
								<%}%>
								
								<%}else{%>
								<tr>
									<td colspan="7">无记录</td>
								</tr>
								<%} %>
						</tbody>
						
						<tfoot>
							<tr>
								<td colspan="7">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div>
					</div>
		<script type="text/javascript"	src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/cha_depBalanceTable.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script>
		$(document).ready(function(){
			//getLoginInfo("#loginUser");
			reloadTree();
			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});
			
			$("#toggleDiv").toggle(function() {
			$("#condition").animate( {
				opacity : 'toggle'
			}, "fast");
			$('#searchIcon').attr('src', '<%=inheritPath %>/images/toggle_expand.png');
			$('#searchIcon').attr('title', '收缩查询条件');
			}, function() {
				$("#condition").animate( {
					opacity : 'toggle'
				}, "fast");
				$('#searchIcon').attr('src', '<%=inheritPath %>/images/toggle_collapse.png');
				$('#searchIcon').attr('title', '展开查询条件');
			});
            showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[5,10]);
		     $('#search').click(function(){submitForm();});
		     noyinhao("#addCount");
		     noyinhao("#recCount");
			$("#addBalance").dialog({
				autoOpen: false,
				maxWidth: 350,
				maxHeight: 200,
				modal: true,
				open:function(){
					$("#addSms").attr("checked","checked");
				},
				close:function(){
				    $('#addCount').val("");
				   	$('#addMark').val("");
				    $(".ui-dialog-buttonpane button").attr("disabled",false);
				}
			});
			$("#delBalance").dialog({
				autoOpen: false,
				maxWidth: 350,
				maxHeight: 200,
				modal: true,
				open:function(){
					$("#addMms").attr("checked","checked");
				},
				close:function(){
				    $('#recCount').val("");
				    $('#recMark').val("");
				    $(".ui-dialog-buttonpane button").attr("disabled",false);
				}
			});
			$("#setAlarm").dialog({
				autoOpen: false,
				width: 370,
				height: 250,
				modal: true,
				open:function(){
				},
				close:function(){
				    $("#alarmButton").attr("disabled",false);
				}
			});
		});
		/*评审后，决定不添加默认充值权限按钮
		//为了显示默认的充值回收机构 pengj
		function showDefaultDepTable()
		{
			$("#operatorBalancePri").val('defaultBalancePri');
			//reloadTree();
			//showDepTable();
			location.reload();
		}
		*/
		</script>
	</body>
</html>