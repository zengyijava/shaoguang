package com.montnets.emp.bustaocan.servlet;

import com.montnets.emp.bustaocan.biz.BusTaoCanBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.ydyw.LfBusPkgeTaoCan;
import com.montnets.emp.entity.ydyw.LfBusTaoCan;
import com.montnets.emp.entity.ydyw.LfProCharges;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.mobilebus.constant.MobileBusStaticValue;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class ydyw_busTaocanMgrSvt extends BaseServlet  {

	private static final String PATH = "/ydyw/busTaoCan";
	private final BaseBiz baseBiz=new BaseBiz();
	private final BusTaoCanBiz biz=new BusTaoCanBiz();
	
	/**
	 *  查询业务套餐管理
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {

		try {
			
			//添加与日志相关 p
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
			long startTimeByLog = System.currentTimeMillis();  //开始时间
			
			
			PageInfo pageInfo=new PageInfo();
			String lgcorpcode=request.getParameter("lgcorpcode");
			//String lguserid=request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//是否第一次
			boolean isFirstEnter = false;
			isFirstEnter=pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				//套餐名称
				String taocan_name = request.getParameter("taocan_name");
				if(taocan_name != null && !"".equals(taocan_name)){
					conditionMap.put("taocan_name",taocan_name.trim());
				}
				//套餐编号
				String taocan_code = request.getParameter("taocan_code");
				if(taocan_code != null && !"".equals(taocan_code)){
					conditionMap.put("taocan_code",taocan_code.trim());
				}
				//操作
				String user = request.getParameter("user");
				if(user != null && !"".equals(user)){
					conditionMap.put("user",user.trim());
				}
				//状态
				String state = request.getParameter("state");
				if(state != null && !"".equals(state)){
					conditionMap.put("state",state.trim());
				}
				// 计费类型
				String freeType = request.getParameter("freeType");
				if(freeType != null && !"".equals(freeType)){
					conditionMap.put("freeType",freeType.trim());
				}
				
				//机构
				String deptid = request.getParameter("deptid");
				if(deptid != null && !"".equals(deptid)){
					conditionMap.put("deptid",deptid);
				}
				
				// 业务包编号 
				String pageckcode = request.getParameter("pageckcode");
				if(pageckcode != null && !"".equals(pageckcode)){
					conditionMap.put("pageckcode",pageckcode.trim());
				}
				
				
				//提交起始时间
				String submitSartTime = request.getParameter("startSubmitTime");
				if(submitSartTime != null && !"".equals(submitSartTime)){
					conditionMap.put("startSubmitTime",submitSartTime);
				}
				//提交结束时间
				String submitEndTime = request.getParameter("endSubmitTime");
				if(submitEndTime != null && !"".equals(submitEndTime)){
					conditionMap.put("endSubmitTime",submitEndTime);
				}
		        //是否包含子机构
		        String isContainsSun=request.getParameter("isContainsSun");
		        //条件也默认增加
				if(isFirstEnter){
						conditionMap.put("isContainsSun", "1");
				}else{
					if(isContainsSun==null||"".equals(isContainsSun)){
						conditionMap.put("isContainsSun", "0");
					}else{
						conditionMap.put("isContainsSun", "1");
					}
				}
				// 获取当前登录操作员对象
				LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
				//当前操作员所拥有的权限
				Integer permissionType = (sysuser != null && sysuser.getPermissionType() != null) ? sysuser.getPermissionType():null;
				if(permissionType != null && permissionType == 1)
				{
					conditionMap.put("permissionType","1");
					conditionMap.put("permissionUserName",sysuser.getUserName().toString());
				}
				conditionMap.put("userId", sysuser==null?null:String.valueOf(sysuser.getUserId()));
				//加企业编码
				conditionMap.put("corpCode", lgcorpcode);
			List<DynaBean> recordList=biz.getTaoCanRecord(conditionMap,pageInfo);
			
			LinkedHashMap<String, LinkedHashMap> taocanList=new LinkedHashMap<String, LinkedHashMap>();
			//翻页的情况，最多有15条记录
			for(int k=0;k<recordList.size();k++){
				DynaBean dynb=recordList.get(k);
				String taocancode=dynb.get("taocan_code").toString();
				List<DynaBean> selectList=biz.getListByCode(taocancode);
				LinkedHashMap<String, String> 		all=new LinkedHashMap<String, String> ();
				if(selectList!=null&&selectList.size()>0){
					for(int i=0;i<selectList.size();i++){
						DynaBean db=selectList.get(i);
						String package_name=db.get("package_name").toString();
						String package_code=db.get("package_code").toString();
						all.put(package_code, package_name);
					}
					taocanList.put(taocancode, all);
				}
			}
			request.setAttribute("taocanList",taocanList);
			
			request.setAttribute("recordList",recordList);
			request.setAttribute("conditionMap",conditionMap);
			//默认是勾选包含子机构的
			if(isFirstEnter){
				request.setAttribute("isContainsSun", "1");
			}else{
				if(isContainsSun==null||"".equals(isContainsSun)){
					request.setAttribute("isContainsSun", "0");
				}else{
					request.setAttribute("isContainsSun", "1");
				}
			}
			
			//添加与日志相关 p
			long endTimeByLog = System.currentTimeMillis();  //查询结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
			
			//增加操作日志 p
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
				
				EmpExecutionContext.info("业务套餐管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "GET");
			}
			
			request.setAttribute("pageInfo",pageInfo);
			request.getRequestDispatcher(PATH+ "/ydyw_taoCanList.jsp?lgcorpcode="+lgcorpcode+"&lguserid="+lguserid).forward(request,response);
		} catch (Exception e1) {
			EmpExecutionContext.error(e1,"业务套餐管理页面跳转出现异常");
		}
	}
	
	/**
	 * 跳转到新增页面
	 * @param request
	 * @param response
	 */
	public void toAdd(HttpServletRequest request, HttpServletResponse response) {
		try {
			//初始化套餐列表
			List<DynaBean> packageList=biz.getTCList(null);
			//初始化下拉列表
			Map<String, String> typeMap=MobileBusStaticValue.getTaoCanType();
			request.setAttribute("typeMap", typeMap);
			request.setAttribute("packageList", packageList);
			String lgcorpcode=request.getParameter("lgcorpcode");
			//String lguserid=request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			request.getRequestDispatcher(PATH+ "/ydyw_addTaoCan.jsp?lgcorpcode="+lgcorpcode+"&lguserid="+lguserid).forward(request,response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务套餐新增页面跳转出现异常");
		}
	}
	
	
	/***
	 * 保存业务套餐
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//套餐名称
		String name=request.getParameter("name");
		//用户ID
		//String lguserid=request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		String lgcorpcode=request.getParameter("lgcorpcode");
		String startSubmitTime=request.getParameter("startSubmitTime");
		String endSubmitTime=request.getParameter("endSubmitTime");
		String taocan_type=request.getParameter("taocan_type");

		String free_days=request.getParameter("free_days");
		String fee=request.getParameter("fee");
		String timerStatus=request.getParameter("timerStatus");
		String code= request.getParameter("code");
		String timerTime=request.getParameter("timerTime");
		//界面上选择业务包的code字符串
		String packagecodes=request.getParameter("packagecodes");
		//类型，是新增，还是修改
		String type=request.getParameter("type");
		String buckle_date=request.getParameter("buckle_date");
		String backup_max=request.getParameter("backup_max");
		String backup_day=request.getParameter("backup_day");
		//保存状态
		String state=request.getParameter("state");
		LfBusTaoCan busTaoCan =null;
		LfProCharges lfProCharges=null;
		BaseBiz baseBiz = new BaseBiz();
		
		//查询操作之前记录
		String befchgCont= "";
		
		//修改
		if("update".equals(type)){
			
			//查询套餐规则相关信息
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("taocan_code", code);
			List<LfBusTaoCan> taocanList;
			try {
				taocanList = baseBiz.getByCondition(LfBusTaoCan.class, conditionMap, null);
				if(taocanList!=null&&taocanList.size()>0){
					busTaoCan=taocanList.get(0);
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"查询业务套餐异常！");
			}
			
			//查询操作之前记录
			LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
			conditionMap1.put("taocanCode", busTaoCan.getTaocan_code());
			conditionMap1.put("associateType", "0");
			List<LfBusPkgeTaoCan> oldptcList = baseBiz.getByCondition(LfBusPkgeTaoCan.class, conditionMap1, null);
			String pkgCodes = "";
			if(oldptcList.size()>0){
				for(int k=0;k<oldptcList.size();k++){
					pkgCodes +=oldptcList.get(k).getPackageCode()+",";
				}
				pkgCodes = pkgCodes.substring(0, pkgCodes.length()-1);
			}
			
			befchgCont = busTaoCan.getTaocan_name()+"，"+busTaoCan.getTaocan_code()+"，"+busTaoCan.getStart_date()+"-"+busTaoCan.getEnd_date()+"，["
							+pkgCodes+"]，"+busTaoCan.getTaocan_type()+"，"+busTaoCan.getState();
			
			if(state!=null&&!"".equals(state)){
				busTaoCan.setState(Integer.parseInt(state));
			}
			//查询套餐规则相关信息
			LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
			condition.put("taocancode", code);
			List<LfProCharges> charges;
			try {
				charges = baseBiz.getByCondition(LfProCharges.class, condition, null);
				if(charges!=null&&charges.size()>0){
					lfProCharges=charges.get(0);
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"查询业务套餐规则异常！");
			}
			lfProCharges.setUpdatetime(new Timestamp(System.currentTimeMillis()));
			busTaoCan.setUpdate_time(new Timestamp(System.currentTimeMillis()));

			//隐藏域用于判断该数字是否修改过
			String hidden_buckle_date=request.getParameter("hidden_buckle_date");
			
			//修改时候,首先判断该值是否修改过，如果没有修改就不处理
			if(hidden_buckle_date!=null&&!"".equals(hidden_buckle_date)){
			if(buckle_date!=null&&!"".equals(buckle_date)&&(!hidden_buckle_date.equals(buckle_date))){//两者不相同就判断
				
				//获得当前时间，并且判断
				Date date=new Date();
				int day=date.getDate();
				boolean allow=true;
				//修改的日期大于之前的日期，允许修改
				if(Integer.parseInt(buckle_date)>lfProCharges.getBuckledate()){
					allow=true;
					//修改的日期小于之前日期，但是大于当前时间允许修改
				}else if(Integer.parseInt(buckle_date)<lfProCharges.getBuckledate()&&Integer.parseInt(buckle_date)>day){
					allow=true;
				}else{
					//否则不允许修改
					allow=false;
				}
				if(!allow){
					response.getWriter().print("dateNotAllow");
					return;
				}
				
					lfProCharges.setBuckledate(Integer.parseInt(buckle_date));
			}
			//表示之前是当天，现在修改为当月或者下个月（处理---订购生效当月0号开始扣费--这个问题）
			}else if(buckle_date!=null&&!"".equals(buckle_date)){
				lfProCharges.setBuckledate(Integer.parseInt(buckle_date));
			}
			

			
		}else{
			//***************新增处理**************
			//新增时候，判断编号是否重复
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("taocan_code", code);
			List<LfBusTaoCan> taocanList;
			try {
				taocanList = baseBiz.getByCondition(LfBusTaoCan.class, conditionMap, null);
				if(taocanList!=null&&taocanList.size()>0){
					busTaoCan=taocanList.get(0);
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"查询业务套餐异常！");
			}
			
			//保存状态
			//String state=request.getParameter("state");
			if(state!=null&&!"".equals(state)){
				busTaoCan.setState(Integer.parseInt(state));
			}
			if(busTaoCan!=null){
				response.getWriter().print("repeat");
				return;
			}
			
			busTaoCan =new LfBusTaoCan();
			lfProCharges=new LfProCharges();
			lfProCharges.setCreatetime(new Timestamp(System.currentTimeMillis()));
			busTaoCan.setCreate_time(new Timestamp(System.currentTimeMillis()));
			if(lgcorpcode!=null&&!"".equals(lgcorpcode)){
				busTaoCan.setCorp_code(lgcorpcode);
			}
			busTaoCan.setTaocan_code(code);
			//新增时候默认为启用
			busTaoCan.setState(0);
			if(lguserid!=null&&!"".equals(lguserid)){
				busTaoCan.setUser_id(Long.parseLong(lguserid));
			}
			
			if(lguserid!=null&&!"".equals(lguserid)){
				lfProCharges.setUserid(Long.parseLong(lguserid));
			}
			lfProCharges.setTaocancode(code);
			
			if(lguserid!=null&&!"".equals(lguserid)){
				lfProCharges.setCorpcode(Long.parseLong(lgcorpcode));
			}
			//新增时候不做判断
			if(buckle_date!=null&&!"".equals(buckle_date)){
				lfProCharges.setBuckledate(Integer.parseInt(buckle_date));
			}else{
				//默认给个值
				lfProCharges.setBuckledate(0);
			}
			
		}
		//欠费补扣最大次数
		if(backup_max!=null&&!"".equals(backup_max)){
			lfProCharges.setBuckupmaxtimer(Integer.parseInt(backup_max));
		}else{
			lfProCharges.setBuckupmaxtimer(0);
		}
		//补扣间隔时间
		if(backup_day!=null&&!"".equals(backup_day)){
			lfProCharges.setBuckupintervalday(Integer.parseInt(backup_day));
		}else{
			lfProCharges.setBuckupintervalday(0);
		}
		//如果是选择了时间类型
		String selectdate=request.getParameter("selectdate");
		if(selectdate!=null&&!"".equals(selectdate)){
			int select=Integer.parseInt(selectdate);
			lfProCharges.setBuckletype(select);
			
		}else{
			lfProCharges.setBuckletype(0);
		}
		//修改时候，校验  扣费时间  补扣次数   补扣间隔时间三者的关系十分符合正确的逻辑
		//获取当前时间，在此基础上根据选择（当前月份，或者下个月发）计算三者关系
		if(lfProCharges.getBuckletype()==1){//订购生效次月
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM");		
			Date date=new Date();
			date.setMonth(date.getMonth()+1);
			String yearMonth=sdfTime.format(date);
			String datestr=yearMonth+"-"+lfProCharges.getBuckledate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
			Date first =null;//订购生效次月日期
		    try {
				 first = sdf.parse(datestr);
				 
			} catch (ParseException e) {
				EmpExecutionContext.error(e,"转换时间出错！");
			} 
			
			//补扣总天数=补扣次数*补扣间隔时间
			int total=lfProCharges.getBuckupmaxtimer()*lfProCharges.getBuckupintervalday();
			first.setDate(first.getDate()+total);
			
			
			
			//下个月（相对于这个月）
			Date nextdate=new Date();
			nextdate.setMonth(date.getMonth()+2);
			String nextyearMonth=sdfTime.format(nextdate);
			String nextdatestr=nextyearMonth+"-"+lfProCharges.getBuckledate();
			Date next =null;//订购生效次月日期
		    try {
		    	next = sdf.parse(nextdatestr);
				 
			} catch (ParseException e) {
				EmpExecutionContext.error(e,"转换时间出错！");
			} 
			//如果经过计算，计算后的结果超过下个月，给出提示，不允许这样
			if(first.after(next)){
				response.getWriter().print("timeError");
				return;
			}
			
		}else if(lfProCharges.getBuckletype()==3){//订购生效当月
			//订购生效次月
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM");		
			Date date=new Date();
			String yearMonth=sdfTime.format(date);
			String datestr=yearMonth+"-"+lfProCharges.getBuckledate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
			Date first =null;//订购生效次月日期
		    try {
				 first = sdf.parse(datestr);
				 
			} catch (ParseException e) {
				EmpExecutionContext.error(e,"转换时间出错！");
			} 
			
			//补扣总天数=补扣次数*补扣间隔时间
			int total=lfProCharges.getBuckupmaxtimer()*lfProCharges.getBuckupintervalday();
			first.setDate(first.getDate()+total);
			//下个月（相对于这个月）
			Date nextdate=new Date();
			nextdate.setMonth(date.getMonth()+1);
			String nextyearMonth=sdfTime.format(nextdate);
			String nextdatestr=nextyearMonth+"-"+lfProCharges.getBuckledate();
			Date next =null;//订购生效次月日期
		    try {
		    	next = sdf.parse(nextdatestr);
				 
			} catch (ParseException e) {
				EmpExecutionContext.error(e,"转换时间出错！");
			} 
			//如果经过计算，计算后的结果超过下个月，给出提示，不允许这样
			if(first.after(next)){
				response.getWriter().print("timeError");
				return;
			}
		}
		
		
		
		if(startSubmitTime!=null&&startSubmitTime.indexOf("00:00:00")==-1){
			startSubmitTime=startSubmitTime+" 00:00:00";
		}
		
		if(endSubmitTime!=null&&endSubmitTime.indexOf("00:00:00")==-1){
			endSubmitTime=endSubmitTime+" 00:00:00";
		}
		
		busTaoCan.setStart_date(Timestamp.valueOf(startSubmitTime));
		busTaoCan.setEnd_date(Timestamp.valueOf(endSubmitTime));
		if(taocan_type!=null&&!"".equals(taocan_type)){
			busTaoCan.setTaocan_type(Integer.parseInt(taocan_type));	
		}
		
		
		busTaoCan.setTaocan_name(name);
		if(fee!=null&&!"".equals(fee)){
			busTaoCan.setTaocan_money(Long.parseLong(fee));
		}



		

		
		//免费试用开始日期
		if(timerStatus==null||"".equals(timerStatus)){
			lfProCharges.setTrytype(0);
		}else{
			lfProCharges.setTrytype(Integer.parseInt(timerStatus));
		}

		//如果选择其他时间，需要保持开始时间与结束时间，当天就不会保存开始于结束时间
		if(lfProCharges.getTrytype()==1){
			if(timerTime!=null&&timerTime.indexOf("00:00:00")==-1){
				timerTime=timerTime+" 00:00:00";
			}
			Timestamp start = Timestamp.valueOf(timerTime);  
			lfProCharges.setTrystartdate(start);
			//如果不填，结束时间就是开始时间
			if(free_days==null||"".equals(free_days)){
				lfProCharges.setTryenddate(start);
			}else{
				int free=Integer.parseInt(free_days);
				Timestamp end = Timestamp.valueOf(timerTime);
				end.setDate(start.getDate()+free);
				lfProCharges.setTryenddate(end);
			}
		}
		
		if(free_days==null||"".equals(free_days)){
			lfProCharges.setTrydays(0);
		}else{
			int free=Integer.parseInt(free_days);
			lfProCharges.setTrydays(free);
		}
		//选择订购生效当天 ，那么另外一个值为0
		if(lfProCharges.getBuckletype()==2){
			lfProCharges.setBuckledate(0);
		}
		
		
		//修改
		if("update".equals(type)){
			//新增规则
			boolean sa=biz.update(busTaoCan,lfProCharges,packagecodes);
			if(sa){
				response.getWriter().print("updatesuccess");
			}else{
				response.getWriter().print("updatefail");
			}
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "修改业务套餐"+(sa==true?"成功":"失败")+"。[套餐名称，套餐编号，套餐有效期，业务包配置，计费类型，状态]" +
						"("+befchgCont+")->("+name+"，"+code+","+startSubmitTime+"-"+endSubmitTime+"，["+packagecodes+"]，"+taocan_type+"，"+state+")";
				EmpExecutionContext.info("业务套餐管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}			
		}else{
			//新增规则
			boolean sa=biz.save(busTaoCan,lfProCharges,packagecodes);
			if(sa){
				response.getWriter().print("addsuccess");
			}else{
				response.getWriter().print("addfail");
			}
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "新建业务套餐"+(sa==true?"成功":"失败")+"。[套餐名称，套餐编号，套餐有效期，业务包配置，计费类型]" +
						"("+name+"，"+code+","+startSubmitTime+"-"+endSubmitTime+"，["+packagecodes+"]，"+taocan_type+")";
				EmpExecutionContext.info("业务套餐管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "ADD");
			}
		}
		
	}
	/**
	 * 跳转到修改页面，初始化一些值
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response)throws Exception{
		try {
			//套餐编码
			String taocan_code=request.getParameter("taocan_code");
			//初始化套餐列表
			List<DynaBean> packageList=biz.getTCList(null);
			List<DynaBean> selectList=biz.getListByCode(taocan_code);
			Map<String, String> typeMap=MobileBusStaticValue.getTaoCanType();
			//初始化下拉列表
			request.setAttribute("typeMap", typeMap);
			//初始化所有的列表
			request.setAttribute("packageList", packageList);
			//初始化已经选择的
			request.setAttribute("selectList", selectList);
			int count =0;
			String option="";
			String getChooseMan="";
			//用于处理隐藏的codes用于保存修改时候需要的关键的code
			String pckCodes="";
			String forbidden="";
			if(selectList.size()>0){
				count=selectList.size();
				for(int i=0;i<count;i++){
					DynaBean db=selectList.get(i);
					if(db.get("package_state")!=null){
						if("1".equals(db.get("package_state").toString())){
							forbidden="<font color='red'>"+ MessageUtils.extractMessage("ydyw","ydyw_ywgl_ywbgl_text_5",request)+"</font>";
						}
					}
					option= option+"<option value='"+db.get("package_id")+"'  taocan_name='"+db.get("package_name")+"' taocan_code='"+db.get("package_code")+"' >"+db.get("package_name")+"("+db.get("package_code")+")"+"</option>";
					getChooseMan= getChooseMan+"<li dataval='"+db.get("package_id")+"' taocan_name='"+db.get("package_name")+"' taocan_code='"+db.get("package_code")+"' >"+db.get("package_name")+"("+db.get("package_code")+")&nbsp;"+forbidden+"</li>";
					pckCodes=pckCodes+db.get("package_code")+",";
				}
			}
			//此处三个值给页面用的，用于页面上再次增加，修改业务包
			request.setAttribute("count", count);
			request.setAttribute("option", option);
			request.setAttribute("getChooseMan", getChooseMan);
			request.setAttribute("pckCodes", pckCodes);
			//查询套餐相关信息
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("taocan_code", taocan_code);
			BaseBiz baseBiz = new BaseBiz();
			List<LfBusTaoCan> taocanList= baseBiz.getByCondition(LfBusTaoCan.class, conditionMap, null);
			if(taocanList!=null&&taocanList.size()>0){
				request.setAttribute("taocan", taocanList.get(0));
			}
			//查询套餐规则相关信息
			LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
			condition.put("taocancode", taocan_code);
			List<LfProCharges> charges= baseBiz.getByCondition(LfProCharges.class, condition, null);
			if(charges!=null&&charges.size()>0){
				request.setAttribute("charges", charges.get(0));
			}
			String lgcorpcode=request.getParameter("lgcorpcode");
			//String lguserid=request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			request.getRequestDispatcher(PATH+ "/ydyw_editTaoCan.jsp?lgcorpcode="+lgcorpcode+"&lguserid="+lguserid).forward(request,response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务套餐修改页面跳转出现异常");
		}
	}
	
	/**
	 * 查询详细信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void moreInfo(HttpServletRequest request, HttpServletResponse response)throws Exception{
		try {
			//初始化套餐列表
			List<DynaBean> packageList=biz.getTCList(null);
			request.setAttribute("packageList", packageList);
			String taocan_code=request.getParameter("taocan_code");
			//查询套餐相关信息
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("taocan_code", taocan_code);
			BaseBiz baseBiz = new BaseBiz();
			List<LfBusTaoCan> taocanList= baseBiz.getByCondition(LfBusTaoCan.class, conditionMap, null);
			if(taocanList!=null&&taocanList.size()>0){
				request.setAttribute("taocan", taocanList.get(0));
			}
			//查询套餐规则相关信息
			LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
			condition.put("taocancode", taocan_code);
			List<LfProCharges> charges= baseBiz.getByCondition(LfProCharges.class, condition, null);
			if(charges!=null&&charges.size()>0){
				request.setAttribute("charges", charges.get(0));
			}
			List<DynaBean> selectList=biz.getListByCode(taocan_code);
			String str="";
			if(selectList!=null&&selectList.size()>0){
				for(int i=0;i<selectList.size();i++){
					DynaBean db=selectList.get(i);
					String package_name=db.get("package_name").toString();
					String package_code=db.get("package_code").toString();
					if(i==selectList.size()-1){
						str=str+package_name+"("+package_code+")";
					}else{
						str=str+package_name+"("+package_code+")"+"，&nbsp;&nbsp;";
					}
				}
			}

			
			request.setAttribute("packageInfo", str);
			
			
			String lgcorpcode=request.getParameter("lgcorpcode");
			//String lguserid=request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			request.getRequestDispatcher(PATH+ "/ydyw_msgInfo.jsp?lgcorpcode="+lgcorpcode+"&lguserid="+lguserid).forward(request,response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务套餐详细信息跳转出现异常");
		}
	}
	
	  
	/**
	 * 点击查询按钮，根据名称查询业务
	 * @param request
	 * @param response
	 */

	public void search(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String name = request.getParameter("epname");
		if(name==null){
			name="";
		}
		List<DynaBean> nameList = biz.getTCList(name);
		StringBuffer sb=new StringBuffer("[");
		if(nameList!=null){
			for( int i=0;i<nameList.size();i++){
				DynaBean db=nameList.get(i);
				String package_code=db.get("package_code").toString();
				String package_name=db.get("package_name").toString();
				String package_id=db.get("package_id").toString();
				sb.append("{package_code:'"+package_code+"',package_name:'"+package_name+"',package_id:'"+package_id+"'}");
				if(i !=nameList.size()-1){
					sb.append(",");
				}
			}
		}
		sb.append("]");
		response.getWriter().print(sb.toString());
	}
	
	// 输出机构代码数据
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			Long depId = null;
			Long userid = null;
			// 部门iD
			String depStr = request.getParameter("depId");
			// 操作员账号
			//String userStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);

			if(depStr != null && !"".equals(depStr.trim()))
			{
				depId = Long.parseLong(depStr);
			}
			if(userStr != null && !"".equals(userStr.trim()))
			{
				userid = Long.parseLong(userStr);
			}
			//session获取企业编码
			LfSysuser sysuser = getLoginUser(request);
			String corpCode = sysuser != null?sysuser.getCorpCode():"";
			String departmentTree = getDepartmentJosnData(depId, userid,corpCode);
			response.getWriter().print(departmentTree);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "后台异常");
		}
	}
	
	// 异步加载机构的主方法
	private String getDepartmentJosnData(Long depId, Long userid,String corpCode)
	{

		StringBuffer tree = null;
		try
		{
			// 当前登录操作员
			BaseBiz baseBiz = new BaseBiz();
			LfSysuser curUser = baseBiz.getLfSysuserByUserId(userid);
			// 判断是否个人权限
			if(curUser.getPermissionType() == 1)
			{
				tree = new StringBuffer("[]");
			}
			else
			{
				// 机构biz
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps;

				lfDeps = null;

				if(curUser.getCorpCode().equals("100000"))
				{
					if(depId == null)
					{
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
					}
					else
					{
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				}
				else
				{
					if(depId == null)
					{
						lfDeps = new ArrayList<LfDep>();
						//LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userid, corpCode).get(0);
						lfDeps.add(lfDep);
					}
					else
					{
						//lfDeps = new DepBiz().getDepsByDepSuperId(depId);
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
					}
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++)
				{
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size() - 1)
					{
						tree.append(",");
					}
				}
				tree.append("]");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "后台异常");
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}
	// 异步加载机构的主方法
	private String getDepartmentJosnData(Long depId, Long userid)
	{

		StringBuffer tree = null;
		try
		{
			// 当前登录操作员
			BaseBiz baseBiz = new BaseBiz();
			LfSysuser curUser = baseBiz.getLfSysuserByUserId(userid);
			// 判断是否个人权限
			if(curUser.getPermissionType() == 1)
			{
				tree = new StringBuffer("[]");
			}
			else
			{
				// 机构biz
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps;

				lfDeps = null;

				if(curUser.getCorpCode().equals("100000"))
				{
					if(depId == null)
					{
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
					}
					else
					{
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				}
				else
				{
					if(depId == null)
					{
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						lfDeps.add(lfDep);
					}
					else
					{
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++)
				{
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size() - 1)
					{
						tree.append(",");
					}
				}
				tree.append("]");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "后台异常");
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}
}
