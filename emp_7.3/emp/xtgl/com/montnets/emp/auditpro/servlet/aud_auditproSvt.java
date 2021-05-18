package com.montnets.emp.auditpro.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.util.StringUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.auditpro.biz.AuditProBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfFlowBindObj;
import com.montnets.emp.entity.sysuser.LfFlowBindType;
import com.montnets.emp.entity.sysuser.LfReviewSwitch;
import com.montnets.emp.entity.sysuser.LfReviewer2level;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.i18n.util.MessageUtils;
/**
 * 审核流程管理
 * @功能概要：
 * @项目名称： p_xtgl
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-9-13 下午01:52:49
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class aud_auditproSvt extends BaseServlet {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 3566526212096090856L;
	
	//操作模块
	public static final String auditModule= "审批流程管理";

	//操作用户
	private final String opSper = StaticValue.OPSPER;

    private final String empRoot="xtgl";
	
    private final String basePath="/auditpro";
    
    private final BaseBiz baseBiz=new BaseBiz();
    
    private final AuditProBiz auditBiz = new AuditProBiz();
    
    private final SuperOpLog spLog=new SuperOpLog();
	/**
	 * 进入审核流程管理   列表
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-13 下午01:54:46
	 */
	@SuppressWarnings("unchecked")
	public void find(HttpServletRequest request, HttpServletResponse response){
		try{
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, "后台请求");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//若lguserid为空，再从session中获取一次，如获取不到则返回
			if(lguserid == null || "".equals(lguserid.trim()))
			{
				lguserid = String.valueOf(getUserIdFormSession(request));
				if(lguserid.equals("-1"))
				{
					EmpExecutionContext.error("跳转审核流程管理页面出现异常,lguserid为空！");
					return;
				}
			}
			LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			//日志开始时间
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			long begin_time=System.currentTimeMillis();
			//操作页面跳转
			String isOperateReturn = request.getParameter("isOperateReturn");
			PageInfo pageInfo=new PageInfo();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			if("true".equals(isOperateReturn))
			{
				pageInfo  = (PageInfo) request.getSession(false).getAttribute("auditPageInfo");
				conditionMap = (LinkedHashMap<String, String>) request.getSession(false).getAttribute("auditConditionMap");
			}
			else
			{
				//是否第一次打开
				boolean isFirstEnter = false;
				//分页信息
				isFirstEnter=pageSet(pageInfo, request);
				//流程名称
				String flowtask = "";
				//流程范围
				String flowtype = "";
				//流程创建者
				String flowname = "";
				if(!isFirstEnter)
				{
					flowtask = request.getParameter("flowtask");
					if(flowtask != null && !"".equals(flowtask)){
						conditionMap.put("flowtask", flowtask);
					}
					flowtype = request.getParameter("flowtype");
					if(flowtype != null && !"".equals(flowtype)){
						conditionMap.put("flowtype", flowtype);
					}
					flowname = request.getParameter("flowname");
					if(flowname != null && !"".equals(flowname)){
						conditionMap.put("flowname", flowname);
					}
				}
				request.getSession(false).setAttribute("auditPageInfo", pageInfo);
				request.getSession(false).setAttribute("auditConditionMap", conditionMap);
			}
			 List<DynaBean> beanList= auditBiz.findFlows(conditionMap, lfsysuser, pageInfo);
			 String fids = "";
			 LinkedHashMap<Long,String> flowTypeMap = new LinkedHashMap<Long, String>();
			 if(beanList != null && beanList.size()>0){
				 for(DynaBean bean:beanList){
					 fids = fids + bean.get("f_id")+",";
				 }
			 }
			 LinkedHashMap<String, String> flowMap = new LinkedHashMap<String, String>();
			 if(fids != null && !"".equals(fids)){
				 flowMap.put("FId&in", fids);
				 List<LfFlowBindType> flowList = baseBiz.getByCondition(LfFlowBindType.class, flowMap, null);
				 if(flowList != null && flowList.size()>0){
					 for(LfFlowBindType type:flowList){
						 //所对应的类型
						 Integer infotype = type.getInfoType();
						 //类型对应的信息
						 String typename = changeType(infotype,request);
						 //判断MAP是否存在
						 if(flowTypeMap.get(type.getFId()) != null && !"".equals(flowTypeMap.get(type.getFId()))){
							 //获取列表中的信息
							 String temp = flowTypeMap.get(type.getFId()) + typename;
							 //移除该键
							 flowTypeMap.remove(type.getFId());
							 //重新放入
							 flowTypeMap.put(type.getFId(), temp);
						 }else{
							 flowTypeMap.put(type.getFId(), typename);
						 }
					 }
				 }
			 }
			 
			request.setAttribute("beanList", beanList);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("flowTypeMap", flowTypeMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("lgcorpcode", lfsysuser.getCorpCode());
			//增加查询日志
			long end_time=System.currentTimeMillis();
			if(pageInfo!=null){
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
				opSucLog(request, "审核流程管理", opContent, "GET");
			}
			//跳转至“审核流程管理”页面
			request.getRequestDispatcher(this.empRoot + this.basePath +"/aud_auditpro.jsp")
			.forward(request, response);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"跳转审核流程管理页面出现异常！");
		}
	}

	/**
	 *   根据信息类型 返回所对应的 信息
	 * @param infoType
	 * @return
	 */
	private String changeType(Integer infoType,HttpServletRequest request){
		String type = "";
		//3：短信模板；4：彩信模板；1：短信发送；2：彩信发送；
		try{
			if(infoType == 1){
				//type = "短信发送、";
				type = MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_dxfs", request)+"、";
			}else if(infoType == 2){
				//type = "彩信发送、";
				type = MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_cxfs", request)+"、";				
			}else if(infoType == 3){
				//type = "短信模板、";
				type = MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_dxmb", request)+"、";
			}else if(infoType == 4){
				//type = "彩信模板、";
				type = MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_cxmb", request)+"、";
			}else if(infoType == 6){
				//type = "网讯模板、";
				type = MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_wxmb", request)+"、";
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"根据信息类型 返回所对应的 信息出现异常！");
		}
		return type;
	}
	
	
	/**
	 *   进入新增审核流程管理页面
	 * @param request
	 * @param response
	 */
	public void toAddAuditPro(HttpServletRequest request, HttpServletResponse response){
		try{
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, "后台请求");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//操作类型 界面跳转
			String pathtype = request.getParameter("pathtype");
			List<LfReviewer2level> reviewerList = null;
			//设置审核对象 中的上一步的审核流程
			LfFlow lfflow = null;
			//对应的审核流程ID
			String flowid = "";
			//选择的审核范围
			//StringBuffer audtypeBuffer = new StringBuffer("");
			HashSet<Integer> audtypeSet = new HashSet<Integer>();
			
			//System.err.println("to新增或修改页面：lguserid==>"+lguserid+"，请求方式："+request.getMethod());
			//审批人对应的信息
			LinkedHashMap<String, String> LevelAmountMap = new LinkedHashMap<String, String>();
			//审核信息
			LinkedHashMap<String, String> auditMap = new LinkedHashMap<String, String>();
			//如果是操作员  所对应的个数
			LinkedHashMap<String, Integer> auditUserCountMap = new LinkedHashMap<String, Integer>();
			LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			if(pathtype != null && "2".equals(pathtype)){
				flowid = request.getParameter("flowid");
				
				LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
				conditionMap.put("FId",flowid);
				conditionMap.put("corpCode",lfsysuser.getCorpCode());
				List<LfFlow> lfflowList = baseBiz.getByCondition(LfFlow.class, conditionMap, null);
				if(lfflowList != null && lfflowList.size()>0){
					lfflow = lfflowList.get(0);
					conditionMap.clear();
					conditionMap.put("FId",flowid);
					//审核范围
					List<LfFlowBindType> bindtypeList = baseBiz.getByCondition(LfFlowBindType.class, conditionMap, null);
					if(bindtypeList != null && bindtypeList.size()>0){
						for(LfFlowBindType temp:bindtypeList){
							//audtypeBuffer.append(temp.getInfoType()).append(",");
							audtypeSet.add(temp.getInfoType());
						}
					}
					orderMap.put("RLevel", StaticValue.ASC);
					reviewerList = baseBiz.getByCondition(LfReviewer2level.class, conditionMap, null);
					//该等级的所对应的ID   操作员ID 或者 机构ID
					StringBuffer idstr = new StringBuffer();
					String LevelAmountID = "";
					String auditAttr = "";
					String auditUserCount = "";
					if(reviewerList != null && reviewerList.size()>0){
						//循环该审核人的等级
						for(int i=1;i<=lfflow.getRLevelAmount();i++){
							  LevelAmountID = "LevelAmountID"+i;
							  auditAttr = "audit"+i;
							  auditUserCount = "auditCount"+i;
							  //标识审核类型
							  Integer objtype = null;
							  //审核条件
							  Integer objcondition = null;
							  String isFlag = "1";
							  for(int j=0;j<reviewerList.size();j++){
								  LfReviewer2level temp = reviewerList.get(j);
								  if(temp.getRLevel() != null && i != temp.getRLevel()){
									  continue;
								  }
								  if(i == temp.getRLevel()){
									  if("1".equals(isFlag)){
										  //审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
										  objtype = temp.getRType();
										  //1全部通过生效    2第一人审核生效
										  objcondition = temp.getRCondition();
										  auditMap.put(auditAttr, objtype+"_"+objcondition);
									  }
									  isFlag = "2";
									  idstr.append(temp.getUserId()).append(",");
								  }
							  }
							  StringBuffer namebuffer = new StringBuffer();
							  if(objtype == 1){
								  conditionMap.clear();
								  conditionMap.put("userId&in",idstr.toString());
								  conditionMap.put("corpCode",lfsysuser.getCorpCode());
								  List<LfSysuser> lfsysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
								  if(lfsysuserList != null && lfsysuserList.size()>0){
									  auditUserCountMap.put(auditUserCount, lfsysuserList.size());
									  for(int m=0;m<lfsysuserList.size();m++){
										  LfSysuser user = lfsysuserList.get(m);
										  String label = "<label id='u"+user.getUserId()+"'  attr='1' class='div_hover_bg div_bd' "
										  +" style='padding: 2px;margin-right: 3px;'  onclick='removelabel(this,"+i+")'>"
										  +user.getName()+"<font size='+1'>×</font></label>\n";
										  namebuffer.append(label);
									  }
								  }
								 
							  }else if(objtype == 4){
								  conditionMap.clear();
								  conditionMap.put("depId&in",idstr.toString());
								  conditionMap.put("corpCode",lfsysuser.getCorpCode());
								  List<LfDep> lfdepList = baseBiz.getByCondition(LfDep.class, conditionMap, null);
								  if(lfdepList != null && lfdepList.size()>0){
									  for(int m=0;m<lfdepList.size();m++){
										  LfDep dep = lfdepList.get(m);
										  String label = "<label id='"+dep.getDepId()+"'  attr='1' class='div_hover_bg div_bd'  "
										  +"style='padding: 2px;margin-right: 3px;'  onclick='removelabel(this,"+i+")'>"
										  +dep.getDepName()+"<font size='+1'>×</font></label>\n";
										  namebuffer.append(label);
									  }
								  }
							  }
								LevelAmountMap.put(LevelAmountID, namebuffer.toString());
								idstr = new StringBuffer();
								LevelAmountID = "";
								auditAttr = "";
						}
					}
				}
			}
			conditionMap.clear();
				//信息类型。1：短信发送；2：彩信发送；3：短信模板；4：彩信模板；
			conditionMap.put("corpCode",lfsysuser.getCorpCode());
			//控制审核范围的开关
			conditionMap.put("switchType","1");
			List<LfReviewSwitch> switchList = baseBiz.getByCondition(LfReviewSwitch.class, conditionMap, null);
			
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("flowid", flowid);
			request.setAttribute("lfflow", lfflow);
			//request.setAttribute("audtypeBuffer", audtypeBuffer.toString());
			request.setAttribute("audtypeSet",audtypeSet);
			
			request.setAttribute("LevelAmountMap", LevelAmountMap);
			request.setAttribute("auditMap", auditMap);
			request.setAttribute("auditUserCountMap", auditUserCountMap);
			request.setAttribute("switchList", switchList);
			request.setAttribute("pathtype", pathtype);
			
			LinkedHashMap<Integer, String> auditTypeMap = new LinkedHashMap<Integer, String>();
			//auditTypeMap.put(1, "短信发送");
			auditTypeMap.put(1, MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_dxfs", request));
			//auditTypeMap.put(2, "彩信发送");
			auditTypeMap.put(2, MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_cxfs", request));
			//auditTypeMap.put(3, "短信模板");
			auditTypeMap.put(3, MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_dxmb", request));
			//auditTypeMap.put(4, "彩信模板");
			auditTypeMap.put(4, MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_cxmb", request));
			//auditTypeMap.put(6, "网讯模板");
			auditTypeMap.put(6, MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_wxmb", request));
            //auditTypeMap.put(7, "流量订购");
            auditTypeMap.put(7, MessageUtils.extractMessage("xtgl", "xtgl_spgl_shlcgl_lldg", request));
			request.setAttribute("auditTypeMap", auditTypeMap);

			
			request.getRequestDispatcher(this.empRoot + this.basePath +"/aud_addAudit1.jsp")
			.forward(request, response);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"进入新增审核流程管理页面出现异常！");
		}
	}

	
	/**
	 *  或者操作员机构  以及 操作员树
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createsysdeptree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			//机构id
			String depId = request.getParameter("depId");
			String pathtype  = request.getParameter("pathtype");
			String flowid = "";
			String auditlevel = "";
			String label_idstr = "";
            LfSysuser sysuser = getLoginUser(request);
			if(pathtype != null && "2".equals(pathtype)){
				//审核流程的ID
				flowid = request.getParameter("flowid");
				//第几个审核等级
				auditlevel = request.getParameter("auditlevel");
			}else if(pathtype != null && "add".equals(pathtype)){
				//该DIV审批所选择的ID串  删除后再填 加操作 ， 点填加按钮处理的问题
				label_idstr = request.getParameter("labelidstr");
			}
			//获取机构树字符串
			String tree = getsysdepTreeJosnData(depId,sysuser.getUserId()+"",sysuser.getCorpCode(),pathtype,flowid,auditlevel,label_idstr);
			response.getWriter().print(tree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取机构树出现异常！");
		}
	}
	
	
	/**
	 *   获取操作员以及操作员机构组成的树
	 * @param depId	机构ID
	 * @return
	 */
	private String getsysdepTreeJosnData(String depId,String lguserid,String corpCode,String pathtype,String flowid,String auditlevel,String label_idstr){
		StringBuffer tree = new StringBuffer("[");
		try{
			List<LfSysuser> lfSysusers = null;
			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			//DEPID为空
			if(depId != null &&  !"".equals(depId)){
				//父及机构id
				conditionMap.put("superiorId",depId);
			}else if(depId == null || "".equals(depId)){
				conditionMap.put("depLevel", "1");
				conditionMap.put("corpCode", corpCode);
				LfDep temp = baseBiz.getByCondition(LfDep.class, conditionMap, null).get(0);
				depId = String.valueOf(temp.getDepId());
				conditionMap.clear();
				conditionMap.put("depLevel&in", "1,2");
			}
			//机构状态
			conditionMap.put("depState", "1");
			orderbyMap.put("depId","asc");
			//企业编码
			conditionMap.put("corpCode", corpCode);
			List<LfDep> lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
			LfDep lfDep = null;
			
			for (int i = 0; i < lfDeps.size(); i++) {
				lfDep = lfDeps.get(i);
				tree.append("{");
				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",depId:'").append(lfDep.getDepId()).append("'");
				tree.append(",isParent:").append(true);
				tree.append(",nocheck:").append(true);
				tree.append("}");
				if (i != lfDeps.size() - 1) {
					tree.append(",");
				}
			}
		
			conditionMap.clear();
			orderbyMap.clear();
			conditionMap.put("corpCode&=", corpCode);
			conditionMap.put("depId&=",depId);
			conditionMap.put("userState&<","2");
			orderbyMap.put("name","asc");
			lfSysusers = baseBiz.getByCondition(LfSysuser.class, conditionMap, orderbyMap);
			LfSysuser lfSysuser = null;
			//List<LfReviewer2level> reviewerList = null;
			HashSet<Long> idSet = new HashSet<Long>();
		/*	if(pathtype != null && "2".equals(pathtype)){
				conditionMap.clear();
				conditionMap.put("FId",flowid);
				conditionMap.put("RLevel",auditlevel);
				reviewerList = baseBiz.getByCondition(LfReviewer2level.class, conditionMap, null);
				if(reviewerList != null && reviewerList.size()>0){
					for(LfReviewer2level temp:reviewerList){
						idSet.add(temp.getUserId());
					}
				}
			}*/
			
			if(pathtype != null && "add".equals(pathtype)){
				if(label_idstr != null && !"".equals(label_idstr)){
					String[] arr = label_idstr.split("_");
					if(arr != null && arr.length>0){
						for(String temp:arr){
							if(temp != null && !"".equals(temp)){
								idSet.add(Long.valueOf(temp));
							}
						}
					}
				}
			}
			
			if(lfSysusers != null && !lfSysusers.isEmpty()){
				if(!tree.toString().equals("[")){
					tree.append(",");
				}		
				for (int i = 0; i < lfSysusers.size(); i++) {
					lfSysuser = lfSysusers.get(i);
					tree.append("{");
					tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
					tree.append(",name:'").append(lfSysuser.getName()).append("'");
					tree.append(",pId:").append(lfSysuser.getDepId());
					tree.append(",depId:'").append(lfSysuser.getDepId()+"'");
					tree.append(",isParent:").append(false);
					if("add".equals(pathtype) && idSet.size()>0 && idSet.contains(lfSysuser.getUserId())){
						tree.append(",checked:").append(true);
					}else{
						tree.append(",checked:").append(false);
					}
					tree.append(",hasReviewer:").append(lfSysuser.getIsReviewer());
					tree.append("}");
					if(i != lfSysusers.size()-1){
						tree.append(",");
					}
				}
			}
			tree.append("]");
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取机构树出现异常！");
			tree = new StringBuffer("");
		}
		return tree.toString();
	}
	
	
	/**
	 *  或者操作员机构  以及 操作员树
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createdeptree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			//机构id
			String depId = request.getParameter("depId");
			String pathtype  = request.getParameter("pathtype");
			String flowid = "";
			String auditlevel = "";
			String label_idstr = "";
            LfSysuser sysuser = getLoginUser(request);
			if(pathtype != null && "2".equals(pathtype)){
				//审核流程的ID
				flowid = request.getParameter("flowid");
				//第几个审核等级
				auditlevel = request.getParameter("auditlevel");
			}else if(pathtype != null && "add".equals(pathtype)){
				//该DIV审批所选择的ID串  删除后再填 加操作 ， 点填加按钮处理的问题
				label_idstr = request.getParameter("labelidstr");
			}
			String tree = getdepTreeJosnData(depId,sysuser.getUserId()+"",sysuser.getCorpCode(),pathtype,flowid,auditlevel,label_idstr);
			response.getWriter().print(tree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取机构树出现异常！");
		}
	}
	
	
	
	
	/**
	 *   获取操作员以及操作员机构组成的树
	 * @param depId	机构ID
	 * @return
	 */
	private String getdepTreeJosnData(String depId,String lguserid,String corpCode,String pathtype,String flowid,String auditlevel,String label_idstr){
		StringBuffer tree = new StringBuffer("[");
		try{
			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			//机构状态
			conditionMap.put("depState", "1");
			orderbyMap.put("depId","asc");
			//企业编码
			conditionMap.put("corpCode", corpCode);

			//根部门有审核权限的列表
            List<LfSysuser> auditList = null;
			//DEPID为空

            String rootdepId = depId;
            if(StringUtils.IsNullOrEmpty(depId)) {
                if(getRootDeptId(corpCode) != null)
                    rootdepId = getRootDeptId(corpCode).toString();
            }



			if(depId != null &&  !"".equals(depId)){
				//父及机构id
				conditionMap.put("superiorId",depId);
			}

            //获取根节点是否存在审核员
            LinkedHashMap<String, String> conditionMap2 = new LinkedHashMap<String, String>();
            conditionMap2.put("depId", rootdepId);
            conditionMap2.put("isReviewer", "1"); //1表示是审核人
            auditList = baseBiz.getByCondition(LfSysuser.class, conditionMap2, null);

			boolean rootHasReview = false;
			if(auditList != null && auditList.size() > 0)
			    rootHasReview = true;

			List<LfDep> lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);

            conditionMap.clear();
            conditionMap.put("corpCode", corpCode);
            //操作员状态启用
            conditionMap.put("userState", "1");
            //isReviewer机构审核人
            conditionMap.put("isReviewer", "1");
            //机构审核员的操作员集合
            List<LfSysuser> lfSysusers =  baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
            //具有机构审核员的机构ID集合
            Set<Long> depIds = new HashSet<Long>();
            for ( LfSysuser sysuser:lfSysusers){
                depIds.add(sysuser.getDepId());
            }
			LfDep lfDep = null;
			//List<LfReviewer2level> reviewerList = null;
			HashSet<Long> idSet = new HashSet<Long>();
		/*	if(pathtype != null && "2".equals(pathtype)){
				conditionMap.clear();
				conditionMap.put("FId",flowid);
				conditionMap.put("RLevel",auditlevel);
				reviewerList = baseBiz.getByCondition(LfReviewer2level.class, conditionMap, null);
				if(reviewerList != null && reviewerList.size()>0){
					for(LfReviewer2level temp:reviewerList){
						idSet.add(temp.getUserId());
					}
				}
			}else */
			if(pathtype != null && "add".equals(pathtype)){
				if(label_idstr != null && !"".equals(label_idstr)){
					String[] arr = label_idstr.split("_");
					if(arr != null && arr.length>0){
						for(String temp:arr){
							if(temp != null && !"".equals(temp)){
								idSet.add(Long.valueOf(temp));
							}
						}
					}
				}
			}
			for (int i = 0; i < lfDeps.size(); i++) {
				lfDep = lfDeps.get(i);
				tree.append("{");

				//新增对根节点审核权限的返回
                tree.append("rootHasReview:").append(rootHasReview).append(",");

				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",depId:'").append(lfDep.getDepId()).append("'");
				tree.append(",isParent:").append(true);
				if("add".equals(pathtype) && idSet.size()>0 && idSet.contains(lfDep.getDepId())){
					tree.append(",checked:").append(true);
				}else{
					tree.append(",checked:").append(false);
				}
                //不存在机构审核员的机构
                if(!depIds.contains(lfDep.getDepId())){
                    tree.append(",hasReviewer:").append(false);
                } else {
                    tree.append(",hasReviewer:").append(true);
                }
				tree.append("}");
				if (i != lfDeps.size() - 1) {
					tree.append(",");
				}
			}
			tree.append("]");
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取机构树出现异常！");
			tree = new StringBuffer("");
		}
		return tree.toString();
	
	}


	private Long getRootDeptId(String corpCode) {
	    List<LfDep> depList = null;
	    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
	    conditionMap.put("corpCode", corpCode);
        conditionMap.put("depLevel", "1");
        conditionMap.put("superiorId", "0");
        try {
            depList = baseBiz.getByCondition(LfDep.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, e.getMessage());
        }
        if(depList != null && depList.size() > 0) {
            return depList.get(0).getDepId();
        }
        return null;
    }

	
	/**
	 *   创建审核对象的机构树
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createinstalltree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			//机构id
			String depId = request.getParameter("depId");
			String flowid = request.getParameter("flowid");
            LfSysuser sysuser = getLoginUser(request);
			String tree = getinstallTree(depId,sysuser.getUserId()+"",sysuser.getCorpCode(),flowid);
			response.getWriter().print(tree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"创建审核对象的机构树出现异常！");
		}
	}
	
	
	private String getinstallTree(String depId,String lguserid,String corpCode,String flowid){
		StringBuffer tree = new StringBuffer("[");
		try{
			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			/*conditionMap.put("FId",flowid);
			//获取该审核流程的审核范围 
			List<LfFlowBindType> bindtypeList = baseBiz.getByCondition(LfFlowBindType.class, conditionMap, null);
			StringBuffer bindtypebuffer = new StringBuffer();
			if(bindtypeList != null && bindtypeList.size()>0){
				for(LfFlowBindType temp :bindtypeList){
					bindtypebuffer.append(temp.getInfoType()).append(",");
				}
			}*/
			//获取该审核流程所绑定的审核机构
			conditionMap.clear();
			conditionMap.put("FId",flowid);
			conditionMap.put("ObjType","2");
			List<LfFlowBindObj> bindobjList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
			//该流程所绑定的审核机构
			HashSet<Long> auditdepSet = new HashSet<Long>();
			if(bindobjList != null && bindobjList.size()>0){
				for(LfFlowBindObj temp:bindobjList){
					auditdepSet.add(Long.valueOf(temp.getObjCode()));
				}
			}
			
			//该流程审核范围内所绑定的机构
			/*HashSet<Long> depSet = new HashSet<Long>();
			if(bindtypebuffer != null && bindtypebuffer.length()>0){
				String buffer = bindtypebuffer.substring(0, bindtypebuffer.length()-1);
				conditionMap.clear();
				conditionMap.put("infoType&in",buffer);
				conditionMap.put("corpCode", lfsysuser.getCorpCode());
				conditionMap.put("ObjType","2");
				List<LfFlowBindObj> bindUserObj = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
				if(bindUserObj != null && bindUserObj.size()>0){
					for(LfFlowBindObj temp:bindUserObj){
						depSet.add(Long.valueOf(temp.getObjCode()));
					}
				}
			}*/
			
			//查询机构
			conditionMap.clear();
			orderbyMap.clear();
			//机构状态
			conditionMap.put("depState", "1");
			orderbyMap.put("depId",StaticValue.ASC);
			//企业编码
			conditionMap.put("corpCode", corpCode);
			//DEPID为空
			if(depId != null &&  !"".equals(depId)){
				//父及机构id
				conditionMap.put("superiorId",depId);
			}
			List<LfDep> lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
			LfDep lfDep = null;
			for (int i = 0; i < lfDeps.size(); i++) {
				lfDep = lfDeps.get(i);
				tree.append("{");
				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",depId:'").append(lfDep.getDepId()).append("'");
				tree.append(",isParent:").append(true);
				tree.append(",checked:").append(false);
			/*	if(depSet != null && depSet.size()>0 && depSet.contains(lfDep.getDepId())){
					//isdeporuser 1操作员  2机构   bindattr 绑定流程1   没有  2 isexistAud 1存在该流程    2不存在该流程
					tree.append(",bindattr:'1'");
				}else{
					tree.append(",bindattr:'2'");
				}*/
				if(auditdepSet != null && auditdepSet.size()>0 && auditdepSet.contains(lfDep.getDepId())){
					//isexistAud 1存在该流程    2不存在该流程
					tree.append(",isexistAud:'1'");
				}else{
					tree.append(",isexistAud:'2'");
				}
				tree.append("}");
				if (i != lfDeps.size() - 1) {
					tree.append(",");
				}
			}
			tree.append("]");
		}catch (Exception e) {
			EmpExecutionContext.error(e,"创建审核对象的机构树出现异常！");
			tree = new StringBuffer("");
		}
		return tree.toString();
	
	}
	
	
	
	
	/**
	 *   创建审核流程
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addauditpro(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LfSysuser lfsysuser = null;
		//操作类型
	    String opType = null;
	    //操作内容
		String opContent =null;
		String _opContent =null;
		try{
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, "后台请求");
			//审核流程名称
			String flowtask = request.getParameter("flowtask");
			//流程对象信息
			String audstr = request.getParameter("audstr");
			//审核范围
			String items = request.getParameter("items");
			//备注
			String comment = request.getParameter("comment");
			
			//String lguserid =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//审核等级数
			String divlevel =request.getParameter("divlevel");
			
			//判断传来的界面  1新建  2 是上一步操作
			String pathtype =request.getParameter("pathtype");
			//上一步传来的审核流程ID
			String flowid =request.getParameter("flowid");
			lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			//System.err.println("第一步：lguserid==>"+lguserid+"，请求方式："+request.getMethod());
			LfFlow lfflow = null;
			//修改审核流程时候 用的   新增的审核范围 
			String addbindtypestr = "";
			//减少的审核范围
			String cutbindtypestr = "";
			//该流程的审核对象
			List<LfFlowBindObj> flowbindList = new ArrayList<LfFlowBindObj>();
			String[] bindArr = null;
			if(pathtype != null && "2".equals(pathtype)){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("FId",flowid);
				conditionMap.put("isComplete","2");
				List<LfFlowRecord> recordList = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, null);
				if(recordList != null && recordList.size()>0){
					response.getWriter().print("haverecord");
					return;
				}
				conditionMap.clear();
				//获取该流程所绑定的审核范围
				conditionMap.put("FId",flowid);
				List<LfFlowBindType> bindTypeList = baseBiz.getByCondition(LfFlowBindType.class, conditionMap, null);
				HashSet<String> existBindSet = new HashSet<String>();
				if(bindTypeList != null && bindTypeList.size()>0){
					bindArr = new String[bindTypeList.size()];
					for(int i=0;i<bindTypeList.size();i++){
						LfFlowBindType temp = bindTypeList.get(i);
						existBindSet.add(String.valueOf(temp.getInfoType()));
						bindArr[i] = String.valueOf(temp.getInfoType());
					}
				}else{
					response.getWriter().print("nobindtype");
					return;
				}
				//这里是处理修改的审核范围STR
				String[] audittype = items.split(",");
				//保存修改后的审核范围集合
				HashSet<String> cutBindSet = new HashSet<String>();
				if(audittype != null && audittype.length>0){
					for(String temp:audittype){
						if(temp != null && !"".equals(temp)){
							cutBindSet.add(temp);
							if(!existBindSet.contains(temp)){
								addbindtypestr = addbindtypestr + temp + ",";
							}
						}
					}
				}
				//处理减少的审核范围
				if(bindArr != null){
					for(String temp :bindArr){
						if(temp != null && !"".equals(temp)){
							if(!cutBindSet.contains(temp)){
								cutbindtypestr = cutbindtypestr + temp + ",";
							}
						}
					}
				}
				conditionMap.clear();
				conditionMap.put("FId",flowid);
				conditionMap.put("corpCode",lfsysuser.getCorpCode());
				List<LfFlowBindObj> objList  = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
				//过滤后的机构 ID
				String depids = "";
				//过滤后的操作员 ID
				String userids = "";
				if(objList != null && objList.size()>0){
					//使查询出来的数据唯一
					HashSet<String> depSet = new HashSet<String>();
					HashSet<String> userSet = new HashSet<String>();
					for(LfFlowBindObj temp:objList){
						if(temp.getObjType() == 1 && !userSet.contains(temp.getObjCode())){
							flowbindList.add(temp);
							userSet.add(temp.getObjCode());
							userids = userids + temp.getObjCode() +",";
						}else if(temp.getObjType() == 2  && !depSet.contains(temp.getObjCode())){
							flowbindList.add(temp);
							depSet.add(temp.getObjCode());
							depids = depids + temp.getObjCode() +",";
						}
					}
				}
				//新增的审核范围是否有影响
				if(addbindtypestr != null && !"".equals(addbindtypestr)){
					addbindtypestr = addbindtypestr.substring(0, addbindtypestr.length()-1);
					if(objList != null && objList.size()>0){
						if(userids != null && !"".equals(userids)){
							userids = userids.substring(0, userids.length()-1);
							conditionMap.clear();
							conditionMap.put("infoType&in",addbindtypestr);
							conditionMap.put("ObjType","1");
							conditionMap.put("ObjCode&in",userids);
							conditionMap.put("corpCode",lfsysuser.getCorpCode());
							List<LfFlowBindObj> userflowList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
							if(userflowList != null && userflowList.size() > 0){
								response.getWriter().print("userexist");
								return;
							}
						}
						if(depids != null && !"".equals(depids)){
							depids = depids.substring(0, depids.length()-1);
							conditionMap.clear();
							conditionMap.put("infoType&in",addbindtypestr);
							conditionMap.put("ObjType","2");
							conditionMap.put("ObjCode&in",depids);
							conditionMap.put("corpCode",lfsysuser.getCorpCode());
							List<LfFlowBindObj> depflowList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
							if(depflowList != null && depflowList.size() > 0){
								response.getWriter().print("depexist");
								return;
							}
						}
					}
				}
				//这里是处理审核流程的
				try{
					conditionMap.clear();
					conditionMap.put("FId",flowid);
					conditionMap.put("corpCode",lfsysuser.getCorpCode());
					lfflow= baseBiz.getByCondition(LfFlow.class, conditionMap, null).get(0);
					lfflow.setRLevelAmount(Integer.valueOf(divlevel));
					lfflow.setComments(comment);
					lfflow.setFTask(flowtask);
				}catch (Exception e) {
					EmpExecutionContext.error(e,"更新审核流程记录出现异常 ！");
				}
			}
			String returnmsg = auditBiz.addAuditPro(flowtask, audstr, items, comment,divlevel,pathtype,lfflow, lfsysuser,addbindtypestr,cutbindtypestr,flowbindList);
			response.getWriter().print(returnmsg);
			if(returnmsg != null && !"".equals(returnmsg) && returnmsg.contains("success")){
				if(pathtype != null && "2".equals(pathtype)){
					opType=StaticValue.UPDATE;
					String bindstr = "";
					for(String str:bindArr){
						bindstr += str+",";
					}
					opContent = "修改审核流程(id:"+flowid+")";
					_opContent="修改审核流程(id:"+flowid+")成功。[流程名称|审核范围]（"+(lfflow!=null?lfflow.getFTask():"")+"|"+ bindstr +"）-->（"+flowtask+"|"+items+"）";
				}else{
					opType=StaticValue.ADD;
					String[] arr = returnmsg.split("#");
					opContent = "创建审核流程(流程名称:"+flowtask+")";
					_opContent="创建审核流程成功。(流程名称："+flowtask+"，审核对象："+audstr+"，审核范围："+items+")";
				}
				opSucLog(request, auditModule, _opContent,"OTHER");
				spLog.logSuccessString(lfsysuser.getUserName(), auditModule, opType, opContent,lfsysuser.getCorpCode());
			}
		}catch (Exception e) {
			spLog.logFailureString(lfsysuser.getUserName(), auditModule, opType, opContent+opSper, e,lfsysuser.getCorpCode());
			response.getWriter().print("");
			EmpExecutionContext.error(e,"创建审核流程失败！");
		}
	
	}

	
	/**
	 *  设置审核对象
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void toInstallObj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, "后台请求");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			String flowid = request.getParameter("flowid");
			LfFlow lfflow = null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			try{
				conditionMap.put("FId",flowid);
				conditionMap.put("corpCode",sysuser.getCorpCode());
				lfflow= baseBiz.getByCondition(LfFlow.class, conditionMap, null).get(0);
			}catch (Exception e) {
				EmpExecutionContext.error(e,"获取审核流程记录失败！");
			}
		
		
			//1是正常从创建审核流程管理跳转     2是从完成界面操作上一步跳转
			String pathtype = request.getParameter("pathtype");
			//从完成界面操作上一步跳转  查询所选择的审核对象
			StringBuffer sb = new StringBuffer("");
			if(pathtype != null && "2".equals(pathtype)){
				conditionMap.clear();
				LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
				conditionMap.put("FId",flowid);
				orderbyMap.put("id",StaticValue.ASC);
				List<LfFlowBindObj> bindobjList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, orderbyMap);
				//用户 SET
			    HashSet<String> userSet = new HashSet<String>();
			    //机构SET
			    HashSet<String> depSet = new HashSet<String>();
				if(bindobjList != null && bindobjList.size()>0){
					for(int i=0;i<bindobjList.size();i++){
						LfFlowBindObj temp = bindobjList.get(i);
						//，1-操作员，2--机构
						if(temp.getObjType() == 1 && !userSet.contains(temp.getObjCode())){
							userSet.add(temp.getObjCode());
							LfSysuser user = baseBiz.getById(LfSysuser.class, temp.getObjCode());
							sb.append("<option value=\'"+temp.getObjCode()+"\' isdeporuser='1'>["+MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_czy",request)+"]"+user.getName());
							if(user.getIsAudited() == 2){
								sb.append(" ["+MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_ms",request)+"]");
							}
							sb.append("</option>");
						}else if(temp.getObjType() == 2 && !depSet.contains(temp.getObjCode())){
							depSet.add(temp.getObjCode());
							LfDep dep = baseBiz.getById(LfDep.class, temp.getObjCode());
                            if(temp.getCtsubDep() - 1 != 0){
                                sb.append("<option value=\'"+temp.getObjCode()+"\' isdeporuser='2' iscontain='0'>["+MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_jg",request)+"]"+dep.getDepName()+"</option>");
                            }else{
                                sb.append("<option value=\'"+temp.getObjCode()+"\' isdeporuser='2' iscontain='1'>["+MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_jg",request)+"]"+dep.getDepName()+"（"+MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_bhzjg",request)+"）</option>");
                            }
						}
					}
				}
			}
			opSucLog(request, auditModule, "审核流程（ID："+flowid+"，名称："+lfflow.getFTask()+"）设置审核对象成功。","UPDATE");
			request.setAttribute("optionmsg", sb.toString());
			request.setAttribute("pathtype", pathtype);
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("flowid", flowid);
			request.setAttribute("lfflow", lfflow);
			//request.setAttribute("bindtypebuffer", bindtypebuffer.toString());
			request.setAttribute("lgcorpcode", sysuser.getCorpCode());
			request.getRequestDispatcher(this.empRoot + this.basePath +"/aud_addAudit2.jsp")
			.forward(request, response);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"设置审核对象失败！");
		}
	}

	/**
	 *  获取该机构下的操作员
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getSysuserByDepId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			//企业编码
			String lgcorpcode = request.getParameter("lgcorpcode");
			//机构ID
			String depId = request.getParameter("depId");
			//查询名称
			String searchname = request.getParameter("searchname");
			//审核流程ID
			String flowid = request.getParameter("flowid");
			//排序集合
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			//查询条件集合
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			conditionMap.put("FId",flowid);
			//获取该审核流程的审核范围 
			List<LfFlowBindType> bindtypeList = baseBiz.getByCondition(LfFlowBindType.class, conditionMap, null);
			StringBuffer bindtypebuffer = new StringBuffer();
			if(bindtypeList != null && bindtypeList.size()>0){
				for(LfFlowBindType temp :bindtypeList){
					bindtypebuffer.append(temp.getInfoType()).append(",");
				}
			}
			//获取该审核流程所绑定的审核人员
			conditionMap.put("ObjType","1");
			List<LfFlowBindObj> bindobjList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, orderbyMap);
			//该流程所绑定的审核对象
			HashSet<Long> audituserSet = new HashSet<Long>();
			if(bindobjList != null && bindobjList.size()>0){
				for(LfFlowBindObj temp:bindobjList){
					audituserSet.add(Long.valueOf(temp.getObjCode()));
				}
			}
			
			
			//该流程审核范围内所绑定的人员
			HashSet<Long> userSet = new HashSet<Long>();
			if(bindtypebuffer != null && bindtypebuffer.length()>0){
				String buffer = bindtypebuffer.substring(0, bindtypebuffer.length()-1);
				conditionMap.clear();
				conditionMap.put("infoType&in",buffer);
				conditionMap.put("corpCode", lgcorpcode);
				conditionMap.put("ObjType","1");
				List<LfFlowBindObj> bindUserObj = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
				if(bindUserObj != null && bindUserObj.size()>0){
					for(LfFlowBindObj temp:bindUserObj){
						userSet.add(Long.valueOf(temp.getObjCode()));
					}
				}
			}
			
			
			
			StringBuffer sb = new StringBuffer("suceess#");
			conditionMap.clear();
			conditionMap.put("corpCode", lgcorpcode);
			if(depId != null && !"".equals(depId)){
				conditionMap.put("depId",depId);
			}
			conditionMap.put("userState&<","2");
			orderbyMap.put("name","asc");
			if(searchname != null && !"".equals(searchname)){
				conditionMap.put("name&like",searchname);
			}
			
			List<LfSysuser> lfsysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, orderbyMap);
			if(lfsysuserList != null && lfsysuserList.size()>0){
				StringBuffer buffer = new StringBuffer();
				for(LfSysuser user:lfsysuserList){
					buffer.append(user.getUserId()).append(",");
					if(userSet != null && userSet.size()>0 && userSet.contains(user.getUserId())){
						//isdeporuser 1操作员  2机构   bindattr 绑定流程1   没有  2 isexistAud 1存在该流程    2不存在该流程
						sb.append("<option value='").append(user.getUserId()).append("' isdeporuser='1' bindattr='1' ");
					}else{
						sb.append("<option value='").append(user.getUserId()).append("' isdeporuser='1' bindattr='2' ");
					}
					if(audituserSet != null && audituserSet.size()>0 && audituserSet.contains(user.getUserId())){
						//isexistAud 1存在该流程    2不存在该流程
						sb.append(" isexistAud='1'>");
					}else{
						sb.append(" isexistAud='2'>");
					}
					sb.append(user.getName().trim().replace("<","&lt;").replace(">","&gt;"));
					if(user.getIsAudited() == 2){
						sb.append(" ["+MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_ms",request)+"]");
					}
					sb.append("</option>");
				}
			}
			response.getWriter().print(sb.toString());
		}catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取该机构下的操作员失败！");
		}
	}

	
	/**
	 *   保存 或下一步设置审核对象
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void installaudobj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LfSysuser lfsysuser = null;
		//操作类型
	    String opType = null;
	    //操作内容
		String opContent =null;
		String _opContent =null;
		try{
			//添加请求日志
			EmpExecutionContext.logRequestUrl(request, "后台请求");
			//用户ID
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			//所设置的机构对象
			String depidstr = request.getParameter("depidstr");
            //所设置的机构包含状态
            String depcontainstr = request.getParameter("depcontainstr");
			//所设置的操作员对象
			String useridstr = request.getParameter("useridstr");
			//该审核流程ID
			String flowid = request.getParameter("flowid");
			//还是1 创建审核流程的下一步  2是完成的上一步
			String pathtype = request.getParameter("pathtype");
			//System.err.println("第二步：lguserid==>"+lguserid+"，请求方式："+request.getMethod());
			//审核范围
			//String bindtypebuffer = request.getParameter("bindtypebuffer");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			StringBuffer bindtypebuffer = new StringBuffer();
			conditionMap.put("FId",flowid);
			List<LfFlowBindType> bindtypeList = baseBiz.getByCondition(LfFlowBindType.class, conditionMap, null);
			if(bindtypeList != null && bindtypeList.size()>0){
				for(LfFlowBindType temp :bindtypeList){
					bindtypebuffer.append(temp.getInfoType()).append(",");
				}
			}
			//修改前审核对象
			Set<String> depIdSet = new HashSet<String>();
			Set<String> userIdSet = new HashSet<String>();
			String _userIds = "";
			String _depIds = "";
			if(pathtype != null && "2".equals(pathtype)){
				List<LfFlowBindObj> bindObjList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
				if(bindObjList!=null){
					for (LfFlowBindObj bindObj:bindObjList)
					{
						if(bindObj.getObjType()==1){
							userIdSet.add(bindObj.getObjCode());
						}else if(bindObj.getObjType()==2){
							depIdSet.add(bindObj.getObjCode());
						}
					}
					for(String id:userIdSet){
						_userIds += id+",";
					}
					for(String id:depIdSet){
						_depIds += id+",";
					}
				}
			}
			String returnmsg = auditBiz.addupdateAuditObj(depidstr, depcontainstr, useridstr, flowid, pathtype,bindtypebuffer.toString(),lfsysuser);
			response.getWriter().print(returnmsg);
			if(pathtype != null && "2".equals(pathtype)){
				opType = StaticValue.UPDATE;
				_opContent = "修改审核对象(id:"+flowid+"，审核对象："+_userIds+"|"+_depIds+"-->"+useridstr+"|"+depidstr+")";
				opContent = "修改审核对象(id:"+flowid+")";
			}else{
				opType = StaticValue.ADD;
				_opContent = "设置审核对象(id:"+flowid+"，审核对象："+useridstr+"|"+depidstr+")";
				opContent = "设置审核对象(id:"+flowid+")";
			}
			if("success".equals(returnmsg)){
				opSucLog(request, auditModule, _opContent+"成功。","OTHER");
				spLog.logSuccessString(lfsysuser.getUserName(), auditModule, opType, opContent,lfsysuser.getCorpCode());
			}
		}catch (Exception e) {
			spLog.logFailureString(lfsysuser.getUserName(), auditModule, opType, opContent+opSper, e,lfsysuser.getCorpCode());
			EmpExecutionContext.error(e,"保存 或下一步设置审核对象出现异常！");
			 response.getWriter().print("");
		}
	}

	
	/**
	 *   完成审核设置
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void tofinishAudit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			String flowid = request.getParameter("flowid");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			LfSysuser lfsysuser  = baseBiz.getById(LfSysuser.class, lguserid);	
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			conditionMap.put("FId",flowid);
			conditionMap.put("corpCode",lfsysuser.getCorpCode());
			List<LfFlow> lfflowList = baseBiz.getByCondition(LfFlow.class, conditionMap, null);
			LfFlow lfflow = null;
			List<LfFlowBindType> bindtypeList = null;
			List<LfReviewer2level> reviewerList = null;
			//System.err.println("第三步：lguserid==>"+lguserid+"，请求方式："+request.getMethod());
			//审批人信息
			//StringBuffer buffer = new StringBuffer();
			//审批人对应的信息
			LinkedHashMap<String, String> LevelAmountMap = new LinkedHashMap<String, String>();
			//审核信息
			LinkedHashMap<String, String> auditMap = new LinkedHashMap<String, String>();

			if(lfflowList != null && lfflowList.size()>0){
				lfflow = lfflowList.get(0);
				conditionMap.clear();
				conditionMap.put("FId",flowid);
				bindtypeList = baseBiz.getByCondition(LfFlowBindType.class, conditionMap, null);
				orderMap.put("RLevel", StaticValue.ASC);
				reviewerList = baseBiz.getByCondition(LfReviewer2level.class, conditionMap, null);
				//该等级的所对应的ID   操作员ID 或者 机构ID
				StringBuffer idstr = new StringBuffer();
				String LevelAmountID = "";
				String auditAttr = "";
				if(reviewerList != null && reviewerList.size()>0){
					//循环该审核人的等级
					for(int i=1;i<=lfflow.getRLevelAmount();i++){
						  //审批等级
						  //buffer.append(i).append("_");
						  LevelAmountID = "LevelAmountID"+i;
						  auditAttr = "audit"+i;
						  //标识审核类型
						  Integer objtype = null;
						  //审核条件
						  Integer objcondition = null;
						  String isFlag = "1";
						  for(int j=0;j<reviewerList.size();j++){
							  LfReviewer2level temp = reviewerList.get(j);
							  if(temp.getRLevel() != null && i != temp.getRLevel()){
								  continue;
							  }
							  if(i == temp.getRLevel()){
								  if("1".equals(isFlag)){
									  //审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
									  objtype = temp.getRType();
									  //1全部通过生效    2第一人审核生效
									  objcondition = temp.getRCondition();
									  auditMap.put(auditAttr, objtype+"_"+objcondition);
								  }
								  isFlag = "2";
								  idstr.append(temp.getUserId()).append(",");
							  }
						  }
						 // buffer.append(objtype).append("_").append(objcondition).append("#");
						  StringBuffer namebuffer = new StringBuffer();
						  if(objtype != null && objtype == 1){
							  conditionMap.clear();
							  conditionMap.put("userId&in",idstr.toString());
							  conditionMap.put("corpCode",lfsysuser.getCorpCode());
							  List<LfSysuser> lfsysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
							  if(lfsysuserList != null && lfsysuserList.size()>0){
								  for(int m=0;m<lfsysuserList.size();m++){
									  LfSysuser user = lfsysuserList.get(m);
									  namebuffer.append(user.getName()).append("#");
								  }
							  }
							 
						  }else if(objtype != null && objtype == 4){
							  conditionMap.clear();
							  conditionMap.put("depId&in",idstr.toString());
							  conditionMap.put("corpCode",lfsysuser.getCorpCode());
							  List<LfDep> lfdepList = baseBiz.getByCondition(LfDep.class, conditionMap, null);
							  if(lfdepList != null && lfdepList.size()>0){
								  for(int m=0;m<lfdepList.size();m++){
									  LfDep dep = lfdepList.get(m);
									  namebuffer.append(dep.getDepName()).append("#");
								  }
							  }
						  }
							LevelAmountMap.put(LevelAmountID, namebuffer.toString());
							idstr = new StringBuffer();
							LevelAmountID = "";
							auditAttr = "";
					}
				}
			}
			opSucLog(request, auditModule, "审核流程（ID："+flowid+"）完成审核设置成功。","OTHER");
			request.setAttribute("lfflow", lfflow);
			request.setAttribute("bindtypeList", bindtypeList);
			request.setAttribute("reviewerList", reviewerList);
			request.setAttribute("LevelAmountMap", LevelAmountMap);
			request.setAttribute("auditMap", auditMap);
			request.setAttribute("flowid", flowid);
			request.setAttribute("lguserid", lguserid);
			request.getRequestDispatcher(this.empRoot + this.basePath +"/aud_addAudit3.jsp")
			.forward(request, response);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"完成审核设置失败！");
		}
	}
	
	
	/**
	 *   删除该条审核流程
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void deleteAudit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LfSysuser lfSysuser = null;
		//操作类型
	    String opType = null;
	    //操作内容
		String opContent =null;
		String msg = "";
		try{
			opType=StaticValue.DELETE;
			String flowid =request.getParameter("flowid");
			opContent="删除审核流程(id:"+flowid+")";
			//String lguserid =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			lfSysuser = baseBiz.getById(LfSysuser.class, lguserid);
			LfFlow flow = baseBiz.getById(LfFlow.class, flowid);
			String ftask = "";
			if(flow!=null){ftask = flow.getFTask();}
			msg = auditBiz.deleteAuditPro(flowid, lfSysuser);
			response.getWriter().print(msg);
			if("success".equals(msg)){
				spLog.logSuccessString(lfSysuser.getUserName(), auditModule, opType, opContent,lfSysuser.getCorpCode());
				opSucLog(request, auditModule, "删除审核流程（ID："+flowid+"，名称："+ftask+"）成功。","DELETE");
			}
		}catch (Exception e) {
			spLog.logFailureString(lfSysuser!=null?lfSysuser.getUserName():"", auditModule, opType, opContent+opSper, e,lfSysuser!=null?lfSysuser.getCorpCode():"");
			response.getWriter().print("");
			EmpExecutionContext.error(e,"删除审核流程失败！");
		}
	}
	
	/**
	 * 列表页面获取设置审核对象
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getAuditObj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			String flowid = request.getParameter("flowid");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			conditionMap.put("FId",flowid);
			orderbyMap.put("id",StaticValue.ASC);
			List<LfFlowBindObj> bindobjList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, orderbyMap);
			StringBuffer sb = new StringBuffer();
			//保存操作员的USERID
			HashSet<String> userSet = new HashSet<String>();
			//保存机构的USERID
			HashSet<String> depSet = new HashSet<String>();
			if(bindobjList != null && bindobjList.size()>0){
				for(int i=0;i<bindobjList.size();i++){
					LfFlowBindObj temp = bindobjList.get(i);
					//，1-操作员，2--机构
					if(temp.getObjType() == 1 && !userSet.contains(temp.getObjCode())){
						userSet.add(temp.getObjCode());
						LfSysuser user = baseBiz.getById(LfSysuser.class, temp.getObjCode());
						sb.append("<option value=\'"+temp.getObjCode()+"\' isdeporuser='1'>["+MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_czy",request)+"]"+user.getName());
						if(user.getIsAudited() == 2){
							sb.append(" ["+MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_ms",request)+"]");
						}
						sb.append("</option>");
					}else if(temp.getObjType() == 2 && !depSet.contains(temp.getObjCode())){
						depSet.add(temp.getObjCode());
						LfDep dep = baseBiz.getById(LfDep.class, temp.getObjCode());
                        if(temp.getCtsubDep() - 1 != 0){
                            sb.append("<option value=\'"+temp.getObjCode()+"\' isdeporuser='2' iscontain='0'>["+MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_jg",request)+"]"+dep.getDepName()+"</option>");
                        }else{
                            sb.append("<option value=\'"+temp.getObjCode()+"\' isdeporuser='2' iscontain='1'>["+MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_jg",request)+"]"+dep.getDepName()+"（"+MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_bhzjg",request)+"）</option>");
                        }

					}
				}
			}
			response.getWriter().print(sb.toString());
		}catch (Exception e) {
			EmpExecutionContext.error(e,"列表页面获取设置审核对象出现异常！");
			response.getWriter().print("");
		}
	}

	
	/**
	 *  获取审核流程信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void getAudit(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//审核流程ID
		String flowid = request.getParameter("flowid");
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		try
		{
			LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			conditionMap.put("FId",flowid);
			conditionMap.put("corpCode",lfsysuser.getCorpCode());
			List<LfFlow> lfflowList = baseBiz.getByCondition(LfFlow.class, conditionMap, null);
			JSONObject jsonObject = new JSONObject();
			if(lfflowList != null && lfflowList.size()>0){
				LfFlow lfflow = lfflowList.get(0);
				//防止符号被转义了,导致前端页面显示出错
				jsonObject.put("flowtask", StringEscapeUtils.unescapeHtml(lfflow.getFTask()));
				LfSysuser user = baseBiz.getById(LfSysuser.class, lfflow.getCreateUserId());
				jsonObject.put("flowuser", user.getName());
				jsonObject.put("flowlevel", lfflow.getRLevelAmount());
				JSONArray members = new JSONArray();  
				conditionMap.clear();
				conditionMap.put("FId",flowid);
				orderMap.put("RLevel", StaticValue.ASC);
				List<LfReviewer2level> reviewerList = baseBiz.getByCondition(LfReviewer2level.class, conditionMap, null);
				if(reviewerList != null && reviewerList.size()>0){
					JSONObject member = null; 
					StringBuffer idstr = new StringBuffer();
					for(int i=1;i<=lfflow.getRLevelAmount();i++){
						 member = new JSONObject(); 
						 //标识审核类型
						  Integer objtype = null;
						  //审核条件
						  Integer objcondition = null;
						  String isFlag = "1";
						  for(int j=0;j<reviewerList.size();j++){
							  LfReviewer2level temp = reviewerList.get(j);
							  if(temp.getRLevel() != null && i != temp.getRLevel()){
								  continue;
							  }
							  if(i == temp.getRLevel()){
								  if("1".equals(isFlag)){
									  //审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
									  objtype = temp.getRType();
									  //1全部通过生效    2第一人审核生效
									  objcondition = temp.getRCondition();
								  }
								  isFlag = "2";
								  idstr.append(temp.getUserId()).append(",");
							  }
						  }
						  StringBuffer namebuffer = new StringBuffer();
						  if(objtype != null && objtype == 1){
							  conditionMap.clear();
							  conditionMap.put("userId&in",idstr.toString());
							  conditionMap.put("corpCode",lfsysuser.getCorpCode());
							  List<LfSysuser> lfsysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
							  if(lfsysuserList != null && lfsysuserList.size()>0){
								  for(int m=0;m<lfsysuserList.size();m++){
									  LfSysuser temp = lfsysuserList.get(m);
									  namebuffer.append(temp.getName()).append("&nbsp;&nbsp;");
								  }
							  }
							 
						  }else if(objtype != null  && objtype == 4){
							  conditionMap.clear();
							  conditionMap.put("depId&in",idstr.toString());
							  conditionMap.put("corpCode",lfsysuser.getCorpCode());
							  List<LfDep> lfdepList = baseBiz.getByCondition(LfDep.class, conditionMap, null);
							  if(lfdepList != null && lfdepList.size()>0){
								  for(int m=0;m<lfdepList.size();m++){
									  LfDep dep = lfdepList.get(m);
									  namebuffer.append(dep.getDepName()).append("&nbsp;&nbsp;");
								  }
							  }
						  }
						  idstr = new StringBuffer();
						  member.put("objtype", objtype);
						  member.put("objmsg",namebuffer.toString());
						  member.put("objcondition",objcondition);
						  members.add(member);
					}
				}
				jsonObject.put("members",members);
			}
			response.getWriter().print(jsonObject.toString());
		}catch (Exception e){
			EmpExecutionContext.error(e,"获取审核流程信息出现异常！");
			response.getWriter().print("");
		}
	}
	
	
	/**
	 *   修改或者删除流程的条件
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void operationAuditCon(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try{
			String returnmsg = "";
			String operationType = request.getParameter("operType");
			String flowid = request.getParameter("flowid");
			//删除操作
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("FId",flowid);
			if(operationType != null && "delete".equals(operationType)){
				List<LfFlowBindObj> bindobjList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
				if(bindobjList != null && bindobjList.size()>0){
					returnmsg = "haveaudobj";
					response.getWriter().print(returnmsg);
					return;
				}
			}
			conditionMap.put("isComplete","2");

			List<LfFlowRecord> recordList = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, null);
			if(recordList != null && recordList.size()>0){
				returnmsg = "haverecord";
                HttpSession session = request.getSession(false);
                Object obj = session.getAttribute("loginSysuser");
                if(obj != null && "admin".equals(((LfSysuser)obj).getUserName())){
                    String infoTypes = "#";
                    int infoType ;
                    for (LfFlowRecord record : recordList) {
                        infoType = record.getInfoType();
                        if(!infoTypes.contains("#"+infoType+"#")){
                            infoTypes += infoType+"#";
                        }
                    }
                    returnmsg += infoTypes;
                }

                response.getWriter().print(returnmsg);
				return;
			}
			opSucLog(request, auditModule, "验证修改或者删除流程条件成功。","OTHER");
			response.getWriter().print("ok");
		}catch (Exception e) {
			EmpExecutionContext.error(e,"验证修改或者删除流程条件出现异常！");
			response.getWriter().print("");
		}
	}

	
	/**
	 *   设置审核机构的时候判断该机构是否可以设置
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getDepAuditMsg(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try{
			String flowid =request.getParameter("flowid");
			String lgcorpcode =request.getParameter("lgcorpcode");
			String depid =request.getParameter("depid");
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			//判断该流程下面是否存在该机构对象
			conditionMap.put("ObjCode",depid);
			conditionMap.put("corpCode",lgcorpcode);
			conditionMap.put("FId",flowid);
			conditionMap.put("ObjType","2");
			List<LfFlowBindObj> objList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
			if(objList != null && objList.size() > 0){
				response.getWriter().print("isexist");
				return;
			}
			conditionMap.clear();
			conditionMap.put("FId",flowid);
			//获取该审核流程的审核范围 
			List<LfFlowBindType> bindtypeList = baseBiz.getByCondition(LfFlowBindType.class, conditionMap, null);
			StringBuffer bindtypebuffer = new StringBuffer();
			if(bindtypeList != null && bindtypeList.size()>0){
				for(LfFlowBindType temp :bindtypeList){
					bindtypebuffer.append(temp.getInfoType()).append(",");
				}
			}
			//判断该流程范围下   是否存在该机构对象
			if(bindtypebuffer != null && bindtypebuffer.length()>0){
				String buffer = bindtypebuffer.toString().substring(0, bindtypebuffer.toString().length()-1);
				conditionMap.clear();
				conditionMap.put("infoType&in",buffer); 
				conditionMap.put("ObjType","2");
				conditionMap.put("ObjCode",depid);
				conditionMap.put("corpCode",lgcorpcode);
				List<LfFlowBindObj> flowobjList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
				if(flowobjList == null || flowobjList.size() ==0){
					response.getWriter().print("ok");
					return;
				}else{
					
					String fidStr = "";
					for(LfFlowBindObj bindObj:flowobjList)
					{
						fidStr=fidStr+bindObj.getFId()+",";
					}
					conditionMap.clear();
					conditionMap.put("FId&in", fidStr);
					List<LfFlow> flowList = baseBiz.getByCondition(LfFlow.class, conditionMap, null);
					String returnmsg="";
					if(flowList!=null && flowList.size()>0)
					{
						for (LfFlow flow:flowList)
						{
							returnmsg=returnmsg+"["+flow.getFId()+":"+(flow.getFTask()!=null?flow.getFTask():"-")+"]";
						}
					}
					response.getWriter().print("MWHS]#"+returnmsg);
					return;
				}
			}
			response.getWriter().print("fail");
			return;
		}catch (Exception e) {
			response.getWriter().print("fail");
			EmpExecutionContext.error(e,"设置审核机构出现异常！");
		}
	}
	
	
	/**
	 *   处理操作员是否 设置了审核流程对象
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getUserAuditMsg(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try{
			String flowid =request.getParameter("flowid");
			String lgcorpcode =request.getParameter("lgcorpcode");
			String userids =request.getParameter("userids");
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>(); 
			//获取该审核流程中所有操作员
			conditionMap.put("corpCode",lgcorpcode);
			conditionMap.put("FId",flowid);
			conditionMap.put("ObjType","1");
			List<LfFlowBindObj> allAuditUserList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
			HashSet<String> allAuditSet = new HashSet<String>();
			if(allAuditUserList != null && allAuditUserList.size() > 0){
				for(LfFlowBindObj temp:allAuditUserList){
					allAuditSet.add(temp.getObjCode());
				}
			}
			String notinIdStr = "";
			//获取所有不存在该流程下的流程
			if(userids != null && !"".equals(userids)){
				String[] arr = userids.split(",");
				for(String temp :arr){
					if(temp != null && !"".equals(temp)){
						if(!allAuditSet.contains(temp)){
							notinIdStr = notinIdStr + temp + ",";
						}
					}
				}
			}
			//这里获取的是未存在该审核流程的操作员IDS    判断这里操作员是否绑定了该审核流程下审核范围
			if(notinIdStr != null && !"".equals(notinIdStr)){
				conditionMap.clear();
				conditionMap.put("FId",flowid);
				//获取该审核流程的审核范围 
				List<LfFlowBindType> bindtypeList = baseBiz.getByCondition(LfFlowBindType.class, conditionMap, null);
				StringBuffer bindtypebuffer = new StringBuffer();
				if(bindtypeList != null && bindtypeList.size()>0){
					for(LfFlowBindType temp :bindtypeList){
						bindtypebuffer.append(temp.getInfoType()).append(",");
					}
				}
				//判断该流程范围下   是否存在该机构对象
				if(bindtypebuffer != null && bindtypebuffer.length()>0){
					String buffer = bindtypebuffer.toString().substring(0, bindtypebuffer.toString().length()-1);
					conditionMap.clear();
					conditionMap.put("infoType&in",buffer);
					conditionMap.put("ObjType","1");
					conditionMap.put("ObjCode&in",notinIdStr);
					conditionMap.put("corpCode",lgcorpcode);
					List<LfFlowBindObj> flowobjList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
					if(flowobjList == null || flowobjList.size() == 0){
						response.getWriter().print("ok");
						return;
					}else{
						conditionMap.clear();
						conditionMap.put("userId&in",notinIdStr);
						conditionMap.put("corpCode",lgcorpcode);
						List<LfSysuser> userList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
						/*LinkedHashMap<Long, String> userNameMap = new LinkedHashMap<Long, String>();
						if(userList != null && userList.size()>0){
							for(LfSysuser user:userList){
								userNameMap.put(user.getUserId(), user.getName());
							}
						}
						String returnmsg = "no&#&";
						HashSet<String> userIdSet = new HashSet<String>();
						for(LfFlowBindObj obj:flowobjList){
							if(!userIdSet.contains(obj.getObjCode())){
								String name = userNameMap.get(Long.valueOf(obj.getObjCode()));
								returnmsg = returnmsg + name + ",";
								userIdSet.add(obj.getObjCode());
							}
						}
						response.getWriter().print(returnmsg);*/
						String returnmsg= "";
						if(userList!=null && userList.size()>0)
						{
							conditionMap.clear();
							conditionMap.put("FId&in","select f_id from LF_FLOWBINDOBJ where obj_type=1 and obj_code='"+userList.get(0).getUserId()+"'");
							List<LfFlow> flowList = baseBiz.getByCondition(LfFlow.class, conditionMap, null);
							returnmsg=returnmsg+userList.get(0).getName()+"MWHS]#";
							if(flowList!=null && flowList.size()>0)
							{
								for (LfFlow flow:flowList)
								{
									returnmsg=returnmsg+"["+flow.getFId()+":"+(flow.getFTask()!=null?flow.getFTask():"-")+"]";
								}
							}
						}
						
						response.getWriter().print(returnmsg);
						return;
					}
				}
			}else{
				response.getWriter().print("isexist");
				return;
			}
		}catch (Exception e) {
			response.getWriter().print("fail");
			EmpExecutionContext.error(e,"处理操作员是否 设置了审核流程对象出现异常！");
		}
	}
	
	
	/**
	 * 修改关键字状态
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//企业编码
		LfSysuser lfSysuser = null;
		//操作类型
	    String opType = null;
	    //操作内容
		String opContent =null;
		try {
			opType=StaticValue.UPDATE;
			String flowid =request.getParameter("flowid");
			opContent="修改审核流程(id:"+flowid+")";
			String flowState = request.getParameter("flowState");
			//String lguserid =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			lfSysuser = baseBiz.getById(LfSysuser.class, lguserid);

            boolean isInAuditing = inAuditing(flowid);
            if(isInAuditing) {
                response.getWriter().print("auditing");
                return;
            }

			LinkedHashMap<String, String>  conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("FId", flowid);
			
			List<LfFlow> flowList = baseBiz.getByCondition(LfFlow.class, conditionMap, null);
			if(flowList!=null && flowList.size()>0)
			{
				LfFlow flow=flowList.get(0);
				flow.setFlowState(Integer.parseInt(flowState));
				baseBiz.updateObj(flow);
				response.getWriter().print("true");
				spLog.logSuccessString(lfSysuser.getUserName(), auditModule, opType, opContent,lfSysuser.getCorpCode());
				opContent="修改审批流：(ID:"+flowid+"，状态："+flowState+")成功。";
				opSucLog(request, auditModule, opContent,"UPDATE");
			}
			else
			{
				response.getWriter().print("fail");
			}

		} catch (Exception e) {
			//异常错误
			response.getWriter().print("error");
			spLog.logFailureString(lfSysuser!=null?lfSysuser.getUserName():"", auditModule, opType, opContent+opSper, e,lfSysuser!=null?lfSysuser.getCorpCode():"");
			EmpExecutionContext.error(e,"修改关键字状态出现异常！");
		}
		return;
	}

	private boolean inAuditing(String flowid) {
	    if(StringUtils.isEmpty(flowid))
	        return false;
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("FId", flowid);
        conditionMap.put("isComplete","2");
        List<LfFlowRecord> recordList = null;
        try {
            recordList = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "判断审核任务是否处于未完成审核流程发生错误" + e.getMessage());
        }

        if(recordList != null && recordList.size() > 0) {
            return true;
        }
        return false;
    }


	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志异常,session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
		
	}
	/**
	 * session获取用户Id
	 * @Title: getlgUserId
	 * @Description: TODO
	 * @param request
	 * @return Long
	 * @author duanjl <duanjialin28827@163.com>
	 * @date 2015-9-9 下午05:18:16
	 */
	public long getUserIdFormSession(HttpServletRequest request){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				if(loginSysuser!=null&& loginSysuser.getUserId() != null){
					return loginSysuser.getUserId();
				}else{
					return -1;
				}
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error("SESSION获取当前登录用户Id失败!");
			return -1;
		}
		return -1;
	}

    /**
     * 审核流程设置被审核对象判断是否包含子机构方法
     * @param request
     * @param response
     * @throws IOException
     */
    public void isAllowChildDeps(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //当前审核流程id
        String flowid = request.getParameter("flowid");
        //当前选择机构id
        String auditdepid = request.getParameter("auditdepid");
        //当前审核流程下 已选择的机构
        String selDeps = request.getParameter("selDeps");
        //企业编码
        String lgcorpcode = request.getParameter("lgcorpcode");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if(flowid == null || auditdepid == null || selDeps == null || lgcorpcode == null)
        {
            EmpExecutionContext.error("审核流程设置被审核对象判断是否包含子机构方法获取参数异常！flowid："+flowid+",auditdepid:"+auditdepid+",selDeps:"+selDeps+",lgcorpcode："+lgcorpcode);
            out.print("error");
            return;
        }
        try {
            //获取所有当前节点所有的机构id
            List<Long> depIds = new DepBiz().getChildDepIds(Long.parseLong(auditdepid),lgcorpcode);
            selDeps = selDeps.trim();
            if(selDeps.length()>0){
                String[] selDepIds = selDeps.split(",");
                //查找当前添加机构是否有子机构存在当前流程中
                for (String selDepId : selDepIds) {
                    long selId = Long.parseLong(selDepId);
                    if(depIds.contains(selId)){
                        out.print("no");
                        return;
                    }
                }
            }

            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("FId",flowid);
            //获取该审核流程的审核范围
            List<LfFlowBindType> bindtypeList = baseBiz.getByCondition(LfFlowBindType.class, conditionMap, null);
            StringBuffer bindtypebuffer = new StringBuffer();
            if(bindtypeList != null && bindtypeList.size()>0){
                for(LfFlowBindType temp :bindtypeList){
                    bindtypebuffer.append(temp.getInfoType()).append(",");
                }
            }
            String buffer = bindtypebuffer.toString().substring(0, bindtypebuffer.toString().length()-1);
            conditionMap.clear();
            conditionMap.put("infoType&in",buffer);
            conditionMap.put("ObjType","2");
            conditionMap.put("FId&<>",flowid);
            conditionMap.put("corpCode",lgcorpcode);
            //获取其他审核流程下的所有机构被审核对象集合
            List<LfFlowBindObj> flowobjList = baseBiz.getByCondition(LfFlowBindObj.class, conditionMap, null);
            //查找当前添加机构是否有子机构存在其他流程中
            for (LfFlowBindObj lfFlowBindObj : flowobjList) {
                long depId = Long.parseLong(lfFlowBindObj.getObjCode());
                if(depIds.contains(depId)){
                    out.print("no");
                    return;
                }
            }
            out.print("yes");
        } catch (Exception e) {
            out.print("error");
            EmpExecutionContext.error(e,"审核流程设置被审核对象判断是否包含子机构方法出现异常！");
        }

    }
}
