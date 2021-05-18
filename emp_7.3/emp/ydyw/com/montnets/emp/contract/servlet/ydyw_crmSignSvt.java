/**
 * Program  : ydyw_crmSignQuerySvt.java
 * Author   : zousy
 * Create   : 2015-1-5 下午02:43:44
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.contract.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.contract.biz.ydyw_crmSignBiz;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfClientDepSp;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.ydyw.LfBusTaoCan;
import com.montnets.emp.entity.ydyw.LfContract;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2015-1-5 下午02:43:44
 */
public class ydyw_crmSignSvt extends BaseServlet
{
	private static final long	serialVersionUID	= -4982505744135507389L;
	private final ydyw_crmSignBiz crmSignBiz = new ydyw_crmSignBiz();
	private final String empRoot = "ydyw";
    private final String basePath = "/contract";
    private final BaseBiz baseBiz = new BaseBiz();
    private final GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
	
    public LinkedHashMap<String, String> formatRequest(HttpServletRequest request, String[] params) {
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
        int len = params.length;
        for(int i = 0; i < len; ++i) {
            String param = params[i];
            String value = request.getParameter(param);
            if(StringUtils.isNotBlank(value)) {
				if("phone".equals(param)|| "_phone".equals(param)){
					value = crmSignBiz.formatPhone(value);
				}
                conditionMap.put(param, value.trim());
            }
        }

        return conditionMap;
    }

    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
    	PageInfo pageInfo = new PageInfo();
        List<DynaBean> lists = new ArrayList<DynaBean>();
        Map<String, List<LfBusTaoCan>> map = new HashMap<String, List<LfBusTaoCan>>();
        boolean isFirstEnter = false;
        
        
        //添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
        
        
        try {
            isFirstEnter = this.pageSet(pageInfo, request);
            LinkedHashMap<String,String> condMap = new LinkedHashMap<String,String>();
        	condMap = this.formatRequest(request, 
			new String[]{"lgcorpcode","mobile", "name", "tcName","tcCode", "chargesType","contractState","acctNo","opName","depName","isAll", "starttime", "endtime"});
        	LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        	condMap.put("userId", sysuser==null?null:String.valueOf(sysuser.getUserId()));
            lists = this.crmSignBiz.getcrmSignList(condMap, pageInfo);
            List<String> ids = new ArrayList<String>();
            for(DynaBean bean:lists){
            	ids.add(String.valueOf(bean.get("contract_id")));
            }
           List<DynaBean> tcList = crmSignBiz.getTaocans(ids);
           String cid = null;
           List<LfBusTaoCan> tcs = null;
           if(tcList!=null){
               for(DynaBean bean:tcList){
              		String id = String.valueOf(bean.get("contract_id"));
              		String tcName = String.valueOf(bean.get("taocan_name"));
              		String tcCode = String.valueOf(bean.get("taocan_code"));
              		String tcMoney = String.valueOf(bean.get("taocan_money"));
              		String tcType = String.valueOf(bean.get("taocan_type"));
              		String state = String.valueOf(bean.get("state"));
              		LfBusTaoCan tc = new LfBusTaoCan();
              		tc.setTaocan_code(tcCode);
              		tc.setTaocan_money(Long.valueOf(tcMoney));
              		tc.setTaocan_name(tcName);
              		tc.setTaocan_type(Integer.valueOf(tcType));
              		tc.setState(Integer.valueOf(state));
              		if(!id.equals(cid)){
              			tcs = new ArrayList<LfBusTaoCan>();
              			map.put(id, tcs);
              		}
              		tcs.add(tc);
              		cid = id;
              }
           }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询客户签约信息异常。");
        } finally {
        	
        	
        	//添加与日志相关 p
    		long endTimeByLog = System.currentTimeMillis();  //查询结束时间
    		long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
    		
    		//增加操作日志 p
    		Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
    		if(loginSysuserObj!=null){
    			LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
    			String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
    			
    			EmpExecutionContext.info("客户签约管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
    					loginSysuser.getUserName(), opContent, "GET");
    		}
        	
        	
            request.setAttribute("lists", lists);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("tcMap", map);
            request.setAttribute("isFirstEnter", isFirstEnter);
            request.getRequestDispatcher(this.empRoot + this.basePath + "/ydyw_crmSignQuery.jsp").forward(request, response);
        }

    }
    
    
    /**
     * @description   签约账号唯一性检测 
     * @author zousy <zousy999@qq.com>
     * @datetime 2015-1-15 上午09:13:47
     */
	public void isUniqueAcct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject json = new JSONObject();
    	try {
            LinkedHashMap<String,String> condMap = new LinkedHashMap<String,String>();
            String account = request.getParameter("account");
            String lgcorpcode = request.getParameter("lgcorpcode");
            if(StringUtils.isBlank(account) || StringUtils.isBlank(lgcorpcode)){
            	json.put("errcode", "-1");
            }else{
            	condMap.put("acctno", account);
            	condMap.put("corpcode", lgcorpcode);
                List<LfContract> lists = new BaseBiz().getByCondition(LfContract.class, condMap, null);
                if(lists == null || lists.size()<1){
                	json.put("errcode", "0");
                }else{
                	json.put("errcode", "1");
                }
            }
        } catch (Exception e) {
        	json.put("errcode", "-1");
            EmpExecutionContext.error(e, "查询客户签约信息异常。");
        } finally {
        	response.getWriter().print(json.toString());
        }

    }
	
	/**
	 * @description    客户编号唯一检查
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-15 下午03:19:49
	 */
	public void isUniqueClicode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject json = new JSONObject();
    	try {
            LinkedHashMap<String,String> condMap = new LinkedHashMap<String,String>();
            String cliCode = request.getParameter("cliCode");
            String lgcorpcode = request.getParameter("lgcorpcode");
            if(StringUtils.isBlank(cliCode) || StringUtils.isBlank(lgcorpcode)){
            	json.put("errcode", "-1");
            }else{
            	condMap.put("clientCode", cliCode.toUpperCase());
            	condMap.put("corpCode", lgcorpcode);
                List<LfClient> lists = new BaseBiz().getByCondition(LfClient.class, condMap, null);
                if(lists == null || lists.size()<1){
                	json.put("errcode", "0");
                }else{
                	json.put("errcode", "1");
                }
            }
        } catch (Exception e) {
        	json.put("errcode", "-1");
            EmpExecutionContext.error(e, "查询客户通讯录信息异常。");
        } finally {
        	response.getWriter().print(json.toString());
        }
    }
    
    /**
     * @description 更改签约状态  
     * @author zousy <zousy999@qq.com>
     * @datetime 2015-1-14 上午09:07:26
     */
    @SuppressWarnings("unchecked")
	public void updateState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject json = new JSONObject();
    	try {
            String id = request.getParameter("id");
            String state = request.getParameter("state");
            if(StringUtils.isBlank(id) || StringUtils.isBlank(state)){
            	json.put("errcode", "-1");
            }else{
            	boolean isTrue = crmSignBiz.updateState(id, state);
            	if(isTrue){
            		json.put("errcode", "0");
            	}else{
            		json.put("errcode", "-1");
            	}
    			//增加操作日志
    			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
    			if(loginSysuserObj!=null){
    				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
    				String opContent1 = "更改签约状态"+(isTrue==true?"成功":"失败")+"。[业务套餐ID，状态]" +
    						"("+id+"，"+state+")";
    				EmpExecutionContext.info("客户签约管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
    						loginSysuser.getUserName(), opContent1, "UPDATE");
    			}
            }
        } catch (Exception e) {
        	json.put("errcode", "-1");
            EmpExecutionContext.error(e, "更改客户签约状态异常。");
        } finally {
        	response.getWriter().print(json.toString());
        }

    }
    public void toAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(this.empRoot + this.basePath + "/ydyw_addCrmSign.jsp").forward(request, response);
   }
    /**
     * @description   新增签约 
     * @author zousy <zousy999@qq.com>
     * @datetime 2015-1-14 上午10:23:18
     */
    @SuppressWarnings("unchecked")
	public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Map<String, String> map = formatRequest(request, new String[]{"id","phone","_phone","cliname","cliCode","cardType","cardNo","guid","_guid"
    			,"depId","address","account","_account","accountName","feeType","feeAccount","feeAccountName","codes","moneys","types","names","lgcorpcode"});
    	String phone = map.get("phone");
    	String cliname = map.get("cliname");
    	String depId = map.get("depId");
    	String codes = map.get("codes");
    	String guid = map.get("guid");
    	PrintWriter out = response.getWriter();
    	JSONObject json = new JSONObject();
    	try {
			if(StringUtils.isBlank(phone) ||StringUtils.isBlank(cliname) || StringUtils.isBlank(depId) || StringUtils.isBlank(codes)){
				json.put("errcode", "-1");
				out.print(json.toString());
				return;
			}
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(sysuser == null)
			{
				out.print(json.toString());
				return;
			}
			map.put("sysuserId", String.valueOf(sysuser.getUserId()));
			map.put("sysdepId", String.valueOf(sysuser.getDepId()));
			if(guid == null) {
				guid = String.valueOf(globalBiz.getValueByKey("guid", 1));
				map.put("guid", guid);
				//客户通讯录不存在
				map.put("noexist", "1");
			}
			json =  crmSignBiz.saveOrUpdate(map);
			out.print(json.toString());
			
			String operation = null!=map.get("id")?"修改":"新增";
			String operation1 = null!=map.get("id")?"ADD":"UPDATE";
			
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = operation+"客户签约成功。[手机号码，姓名，机构，签约账号]" +
						"("+phone+"，"+cliname+"，"+depId+"，"+ map.get("account")+")";
				EmpExecutionContext.info("客户签约管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, operation1);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "新增签约异常！");
		}
   }
    
    public void toEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String id = request.getParameter("contractId");
    	LfContract contract = null;
    	try
		{
			contract = baseBiz.getById(LfContract.class, id);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查找客户签约异常！");
		}finally{
			request.setAttribute("contract", contract);
			request.getRequestDispatcher(this.empRoot + this.basePath + "/ydyw_addCrmSign.jsp").forward(request, response);
		}
   } 
    
	public void getClientDepTree(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			String depId = request.getParameter("depId");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			String cleid = request.getParameter("cleid");
			HashSet<String> depIdSet = new HashSet<String>();
			if(cleid != null && !"".equals(cleid)){
				LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
				conditionMap.put("clientId", cleid);
				List<LfClientDepSp> depSpList = new BaseBiz().getByCondition(LfClientDepSp.class, conditionMap, null);
				if(depSpList != null && depSpList.size()>0){
					for(LfClientDepSp temp:depSpList){
						depIdSet.add(String.valueOf(temp.getDepId()));
					}
				}
			}
			//此方法只查询两级机构
			List<LfClientDep> clientDepList = crmSignBiz.getCliSecondDepTreeByUserIdorDepId(lguserid, depId);
			LfClientDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(clientDepList != null && clientDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < clientDepList.size(); i++) {
					dep = clientDepList.get(i);
					//未知机构不显示
					if("-10".equals(dep.getDepId().toString()))
					{
						continue;
					}
					tree.append("{");
					tree.append("id:").append(dep.getDepId()+"");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					if(depIdSet.contains(dep.getDepId().toString())){
						tree.append(",checked:").append(true);
					}else{
						tree.append(",checked:").append(false);
					}
					tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
					//树数据中加入父机构id
					if(dep.getParentId()-0==0){
						tree.append(",pId:").append(0);
					}else{
						tree.append(",pId:").append(dep.getParentId());
					}
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != clientDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取客户机构信息出现异常！");
		}
	}
	/**
	 * @description  查找操作员机构树  该方法一次性获取全部后代机构
	 * @author zousy <zousy999@qq.com>
	 * @throws IOException 
	 * @datetime 2015-1-7 上午10:29:13
	 */
	public void getOptDepTree(HttpServletRequest request, HttpServletResponse response) throws IOException{
		StringBuffer tree = new StringBuffer("[]");
		try{
			//String userIdStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userIdStr = SysuserUtil.strLguserid(request);


			String depStr = request.getParameter("depId");
			Long userId = Long.parseLong(userIdStr);
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, userId);
			//session获取当前操作员对象
			LfSysuser lfSysuser = getLoginUser(request);
			if(sysuser != null && sysuser.getPermissionType() != 1)
			{
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps = new ArrayList<LfDep>();
				if(depStr == null){
					//lfDeps = depBiz.getAllDeps(userId);
					lfDeps = depBiz.getAllDepByUserIdAndCorpCode(userId,lfSysuser.getCorpCode());
				}else{
					Long depId = Long.parseLong(depStr);
					//lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,lfSysuser.getCorpCode());
				}
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取操作员机构树出现异常！");
		}finally{
			response.getWriter().print(tree.toString());
		}
	}
	@SuppressWarnings("unchecked")
	public void findClientByPhone(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String lgcorpcode = request.getParameter("lgcorpcode");
		String phone = request.getParameter("phone");
		phone = crmSignBiz.formatPhone(phone);
		if (phone == null || phone.length()==0) {
			return;
		}
		List<DynaBean> list = crmSignBiz.findClientInfoByPhone(phone, lgcorpcode);
		DynaBean lastBean = null;
		String dep_id = "";
		String dep_name = "";
		JSONArray array = new JSONArray();
		int len = list.size()-1;
		for(int i=0;i<= len;i++){//重复guid的对象合并机构名称 逗号分隔
			DynaBean bean = list.get(i);
			String curGuid = String.valueOf(bean.get("guid"));
			if(lastBean != null && !curGuid.equals(String.valueOf(lastBean.get("guid")))){//
				JSONObject json = new JSONObject();
				json.put("guid", lastBean.get("guid"));
				json.put("client_code", lastBean.get("client_code"));
				json.put("mobile", lastBean.get("mobile"));
				json.put("dep_id", dep_id.substring(1));
				json.put("name", lastBean.get("name"));
				json.put("dep_name", dep_name.substring(1));
				array.add(json);
				dep_id = dep_name ="";
			}
			dep_id += ","+String.valueOf(bean.get("dep_id"));
			dep_name += ","+String.valueOf(bean.get("dep_name"));
			lastBean = bean;
			if(i == len){
				JSONObject json = new JSONObject();
				json.put("guid", lastBean.get("guid"));
				json.put("client_code", lastBean.get("client_code"));
				json.put("mobile", lastBean.get("mobile"));
				json.put("dep_id", dep_id.substring(1));
				json.put("name", lastBean.get("name"));
				json.put("dep_name", dep_name.substring(1));
				array.add(json);
				dep_id = dep_name ="";
			}
		}
		response.getWriter().print(array.toString());
	}
	
	public void findClientDepsByGuid(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String guid = request.getParameter("guid");
		String corpCode = request.getParameter("lgcorpcode");
		PrintWriter out = response.getWriter();
		if(StringUtils.isBlank(guid)){
			out.print("");
			return;
		}
		JSONArray array = new JSONArray();
		List<DynaBean> lists = crmSignBiz.findClientDepsByGuid(guid, corpCode);
		if(lists!= null && lists.size()>0){
			for(DynaBean bean:lists){
				JSONObject json = new JSONObject();
				json.put("dep_id", String.valueOf(bean.get("dep_id")));
				json.put("dep_name", String.valueOf(bean.get("dep_name")));
				array.add(json);
			}
		}
		out.print(array.toString());
	}
	
	/**
	 * @description    签约界面搜索套餐
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-20 下午01:37:39
	 */
	public void findTaocans(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		LinkedHashMap<String, String> map = formatRequest(request, new String[]{"type","name","ids","lgcorpcode"});
		JSONArray array = new JSONArray();
		//语言格式
		String empLangName = request.getParameter("empLangName");
		map.put("state", "0");
		try
		{
			List<DynaBean> lists = crmSignBiz.findTaocans(map);
			if(lists!= null && lists.size()>0){
				for(DynaBean bean:lists){
					JSONObject json = new JSONObject();
					String type = String.valueOf(bean.get("taocan_type"));
					String money = String.valueOf(bean.get("taocan_money"));
					json.put("id", String.valueOf(bean.get("taocan_id")));
					json.put("code",String.valueOf(bean.get("taocan_code")));
					json.put("type", type);
					json.put("name", String.valueOf(bean.get("taocan_name")));
					json.put("money", money);
					json.put("zf", crmSignBiz.getZF(type, money, empLangName));
					array.add(json);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取套餐信息异常！");
		}
		out.print(array.toString());
	}
	
	/**
	 * @description    签约界面获取已选择的套餐
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-20 下午01:51:39
	 */
	public void findSelTaocans(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		LinkedHashMap<String, String> map = formatRequest(request, new String[]{"contractId","lgcorpcode"});
		JSONArray array = new JSONArray();
		//语言格式
		String empLangName = request.getParameter("empLangName");
		//map.put("state", "0");禁用启用全部
		map.put("isvalid", "0");//正常标示
		try
		{
			List<DynaBean> lists = crmSignBiz.findSelTaocans(map);
			if(lists!= null && lists.size()>0){
				for(DynaBean bean:lists){
					JSONObject json = new JSONObject();
					String type = String.valueOf(bean.get("taocan_type"));
					String money = String.valueOf(bean.get("taocan_money"));
					String state = String.valueOf(bean.get("state"));
					json.put("id", String.valueOf(bean.get("taocan_id")));
					json.put("code",String.valueOf(bean.get("taocan_code")));
					json.put("type", type);
					json.put("state", state);
					json.put("name", String.valueOf(bean.get("taocan_name")));
					json.put("money", money);
					json.put("zf", crmSignBiz.getZF(type, money, empLangName));
					array.add(json);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取当前已选择套餐信息异常！");
		}
		out.print(array.toString());
	}
}

