package com.montnets.emp.bustype.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.bustype.biz.BusTypeCfgBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.monitor.LfMonBusbase;
import com.montnets.emp.entity.monitor.LfMonBusdata;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;

import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

public class mon_busTypeCfgSvt extends BaseServlet{
	
	final String empRoot="ptjk";
	final String basepath="/bustype";
	protected final SuperOpLog spLog = new SuperOpLog();
	private final BusTypeCfgBiz biz=new BusTypeCfgBiz();
	private final BaseBiz baseBiz = new BaseBiz();
	public static final String OPMODULE = "程序监控设置";
	
	/**
	 * 业务监控查询
	 * @description    
	 * @param request
	 * @param response       			 
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter=pageSet(pageInfo,request);
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		String areaCodes="";
		try {
			//初始化业务名称与编码
			List<LfBusManager> busList = biz.getLfBusManager(conditionMap);
			request.setAttribute("busList", busList);
			//公司编号
			String lgcorpcode = request.getParameter("lgcorpcode");
			if(!isFirstEnter){
				//业务名称
				String busname = request.getParameter("busName");
				//业务编码
				String buscode = request.getParameter("busCode");
				//监控状态
				String status=request.getParameter("status");
				//区域
				String area=request.getParameter("area");
				//开始时间
				String sendtime=request.getParameter("sendtime");
				//结束时间
				String recvtime=request.getParameter("recvtime");
				//区域
				areaCodes = request.getParameter("allAreas");
				conditionMap.put("busname", busname);
				conditionMap.put("buscode", buscode);
				conditionMap.put("status", status);
				conditionMap.put("area", area);
				conditionMap.put("sendtime", sendtime);
				conditionMap.put("recvtime", recvtime);
				conditionMap.put("areaCodes", areaCodes);
			}
			request.setAttribute("conditionMap", conditionMap);	
			conditionMap.put("lgcorpcode", lgcorpcode);
			List<LfMonBusbase> baseList = biz.getLfMonBusbase(conditionMap,pageInfo);
			LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
			condition.put("corpcode", lgcorpcode);
			List<LfMonBusdata> dataList = biz.getLfMonBusdata(condition);
			request.setAttribute("baseList", baseList);
			request.setAttribute("dataList", dataList);
			
			//页面区域处理
			Map<Long,String> nameMap=new HashMap<Long,String>();
			Map<Long,String> showNameMap=new HashMap<Long,String>();
			if(baseList!=null&&baseList.size()>0){
				for(int i=0;i<baseList.size();i++){
					String showName="";
					String name="";
					LfMonBusbase busbase=baseList.get(i);
					List<DynaBean> citys=biz.getProvinceList(busbase.getAreacode());
					if(citys!=null&&citys.size()==1){
						showName=citys.get(0).get("province").toString();
						name=showName;
					}else if(citys!=null&&citys.size()>1){
						showName=citys.get(0).get("province").toString()+"...";
						for(DynaBean city:citys){
							name=name+city.get("province").toString()+",";
						}
						if(name.lastIndexOf(",")>-1){
							name = name.substring(0,name.lastIndexOf(","));
						}
					}
					nameMap.put(busbase.getId(), name);
					showNameMap.put(busbase.getId(), showName);
				}
			}
			request.setAttribute("nameMap", nameMap);
			request.setAttribute("showNameMap", showNameMap);
			//区域表
			List<DynaBean> citys=biz.getProvinceList(null);
			request.setAttribute("citys", citys);
			
			//区域名称
			String areaName = request.getParameter("areaName");
			 String allAreas=request.getParameter("allAreas");
			 //保存之后跳转与修改之后跳转区域为默认全部区域
			if(request.getParameter("isupdate")!=null||request.getParameter("issave")!=null){
				areaName=null;
				allAreas=null;
			}
			 //初始化值为全部区域
			 String[] selectNames =new String[100];
			 if(allAreas==null){
				 selectNames=new String[1];
				 selectNames[0]="-1";
			 }else{
				 selectNames=allAreas.split(",");
			 }
			 //由于保存页面查询条件
			 request.setAttribute("areaName", areaName==null?MessageUtils.extractMessage("ptjk","ptjk_jkxq_yw_qbqy",request):areaName);
			 request.setAttribute("selectNames", selectNames);
			 request.setAttribute("areaCodes", areaCodes==null?"-1":areaCodes);
			 
//			List<AprovinceCity> selectCitys=biz.getProvinceList(areaCodes);
//			request.setAttribute("selectCitys", selectCitys);
			//查询出的数据的总数量
			int totalCount =  pageInfo.getTotalRec();
			//日志信息
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String opContent = "开始时间："+sdf.format(stratTime)+ " 耗时：" 
			+(System.currentTimeMillis()-stratTime) + "ms  数量："+totalCount;
			setLog(request, "业务监控设置", opContent, StaticValue.GET);
//			request.setAttribute("issave", request.getParameter("issave"));
//			request.setAttribute("isupdate", request.getParameter("isupdate"));
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(this.empRoot+basepath+"/mon_busTypeCfg.jsp").forward(request,
					response);
		
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务监控查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+basepath+"/mon_busTypeCfg.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"业务监控查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"业务监控查询异常！");
			}
		}
	}
	
	/**
	 * 跳转到新建信息页面
	 * @description    
	 * @param request
	 * @param response       			 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void toAdd(HttpServletRequest request, HttpServletResponse response ) 
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			List<LfBusManager> busList =biz.getLfBusManager(conditionMap);
			request.setAttribute("busList", busList);
			//区域表
			List<DynaBean> citys=biz.getProvinceList(null);
			request.setAttribute("citys", citys);
			request.getRequestDispatcher(this.empRoot+basepath+"/mon_addBusType.jsp").forward(request,
					response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"跳转到新建业务监控信息页面异常！");
		}
	}
	
	/**
	 * 新增保存
	 * @description    
	 * @param request
	 * @param response       			 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void addBusType(HttpServletRequest request, HttpServletResponse response ) 
	{
		

		//业务名称
		String busname = request.getParameter("name");
		//业务编码
		String buscode = request.getParameter("code");
		//告警手机号
		String monphone=request.getParameter("monphone");
		//告警邮箱
		String monemail=request.getParameter("monemail");
		//监控开始时间
		String begintime=request.getParameter("begintime");
		//监控截止时间
		String endtime=request.getParameter("endtime");
		//区域
		String allAreas = request.getParameter("allAreas");
		//公司编号
		String lgcorpcode = request.getParameter("lgcorpcode");
		//操作员ID
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		try {
			
			LfMonBusbase base=new LfMonBusbase();
			if(begintime!=null&&!"".equals(begintime)){
				base.setBegintime(Timestamp.valueOf(begintime+" 00:00:00"));	
			}
			if(lguserid!=null&&!"".equals(lguserid)){
				base.setModiuserid(Long.parseLong(lguserid));
			}
			//如果值为空表示全区域
			if(allAreas!=null&&!"".equals(allAreas)){
				//增加逗号，用于选择多个区域时候处理
				allAreas=","+allAreas;
				base.setAreacode(allAreas);			
			}else{
				base.setAreacode("-1");
			}
			base.setBuscode(buscode);
			base.setBusname(busname);
			if(lgcorpcode!=null&&!"".equals(lgcorpcode)){
				base.setCorpcode(lgcorpcode);
			}
			//默认为启用
			base.setMonstate(1);
			
			base.setCreatetime(new Timestamp(System.currentTimeMillis()));
			base.setUpdatetime(new Timestamp(System.currentTimeMillis()));
			if(endtime!=null&&!"".equals(endtime)){
				base.setEndtime(Timestamp.valueOf(endtime+" 23:59:59"));	
			}
			if(monphone!=null&&!"".equals(monphone)){
				base.setMonphone(monphone);	
			}else{
				//数据库是必输的
				base.setMonphone(" ");	
			}
			if(monemail!=null&&!"".equals(monemail)){
				base.setMonemail(monemail);	
			}else{
				//数据库是必输的
				base.setMonemail(" ");	
			}
			//保存方法
			boolean ret= biz.save(base, setLfMonBusdata(request,base,"add"));
//			request.getRequestDispatcher("/mon_busCfg.htm?method=find&issave="+ret+"&lgcorpcode="+lgcorpcode).forward(request,
//					response);
			response.getWriter().print(ret);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"保存业务出现异常！");
		}
	}
	
	/**
	 * 获得告警时间段阀值设置
	* @Description: TODO
	* @param @param request
	* @param @return
	* @return List<LfMonBusdata>
	 */
	 public List<LfMonBusdata> setLfMonBusdata(HttpServletRequest request,LfMonBusbase base,String flag){
		 List<LfMonBusdata> list=new ArrayList<LfMonBusdata>();
		 List<LfMonBusdata> beforeData=null;
		try {
			
			if("update".equals(flag)){
				LinkedHashMap<String, String> cond = new LinkedHashMap<String, String>();
				cond.put("corpcode", base.getCorpcode());
				cond.put("busbaseid", base.getId()+"");
				 beforeData=baseBiz.getByCondition(LfMonBusdata.class, cond, null);
			 }
		 
			 for(int i=1;i<=24;i++){
				 //时间段(开始)
				 String from=request.getParameter("selectfrom"+i);
				 //时间段(结束)
				 String to=request.getParameter("selectto"+i);
				 if(from!=null&&!"".equals(from)&&to!=null&&!"".equals(to)){
					 LfMonBusdata data=new LfMonBusdata();
					 
					 data.setBeginhour(Integer.parseInt(from));
					 data.setEndhour(Integer.parseInt(to));
					 
					 //如果当前时间在这个时间段之后，（也就是该时间段无效），设置为当前日期
					 // 否则为新增为20000101（表示需要监控）
					 java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
					 Date date=new Date();
					 if(data.getEndhour().intValue()<date.getHours()){
						 data.setMonlasttime(Integer.parseInt(df.format(date)));
					 }else{
						 data.setMonlasttime(20000101);				 
					 }
					 
					 //当修改时候需要如果没有变化，对原来已经告警的数据进行保留。
					 //如果修改时间大于等于当前时间，那么就要重新设置为需要监控：20000101
					 if("update".equals(flag)){
						 String id=request.getParameter("id"+i);
						 if(id!=null&&!"".equals(id)){
							 for(int k=0;k<beforeData.size();k++){
								 LfMonBusdata datab=beforeData.get(k);
								if(id.equals(datab.getId().toString())){
									data.setMonlasttime(datab.getMonlasttime());
									//如果同一条记录，只需要比较后面那个时间
									//只要大于当前时间就修改为需要告警(前提是要修改，如果没有修改，那么不做处理)
									if(!data.getEndhour().equals(datab.getEndhour())){
										if(data.getEndhour()>date.getHours()){
											data.setMonlasttime(20000101);	
									}
									}
								}
							 }
						 }

					 }
					 //偏离率(高于%)
					 String biger=request.getParameter("biger"+i);
					 if(biger!=null&&!"".equals(biger)){
						 data.setDeviathigh(Integer.parseInt(biger));
					 }
					 //偏离率(低于%)
					 String smaller=request.getParameter("smaller"+i);
					 if(smaller!=null&&!"".equals(smaller)){
						 data.setDeviatlow(Integer.parseInt(smaller));
					 }
					 String alarmCount=request.getParameter("alarmCount"+i);
					 //MT已发告警值(条)
					 if(alarmCount!=null&&!"".equals(alarmCount)){
						 data.setMthavesnd(Integer.parseInt(alarmCount));
					 }
					//公司编号
					String lgcorpcode = request.getParameter("lgcorpcode");
					if(lgcorpcode!=null&&!"".equals(lgcorpcode)){
						data.setCorpcode(lgcorpcode);
					}
					//操作员ID
					//String lguserid = request.getParameter("lguserid");
					 //漏洞修复 session里获取操作员信息
					 String lguserid = SysuserUtil.strLguserid(request);


					 if(lguserid!=null&&!"".equals(lguserid)){
						data.setModiuserid(Long.parseLong(lguserid));
					}
	
					 data.setCreatetime(new Timestamp(System.currentTimeMillis()));
					 list.add(data);
				 }
			 }
			} catch (Exception e) {
				EmpExecutionContext.error(e,"获得告警时间段阀值设置异常！");
			}
		return list;
		
	 }
	
	
	/**
	 * 跳转到修改界面(通过id查询出相应的记录)
	 * @description    
	 * @param request
	 * @param response       			 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void toUpdate(HttpServletRequest request, HttpServletResponse response ) 
	{
		
		try
		{
			//业务监控基础信息表ID
			String id=request.getParameter("id");
			//基础信息表
			LfMonBusbase baseInfo=baseBiz.getById(LfMonBusbase.class, id);
			request.setAttribute("baseInfo", baseInfo);
			
			//业务监控数据信息
			LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
			condition.put("busbaseid", id);
			List<LfMonBusdata> dataList=baseBiz.getByCondition(LfMonBusdata.class, condition, null);
			request.setAttribute("dataList", dataList);
			//区域表
			List<DynaBean> citys=biz.getProvinceList(null);
			request.setAttribute("citys", citys);
			
			//查询出选中区域的名称
			List<DynaBean> selectCitys=new ArrayList<DynaBean>();
			if(baseInfo!=null){
				 selectCitys=biz.getProvinceList(baseInfo.getAreacode());
			}
			request.setAttribute("selectCitys", selectCitys);

			
			request.getRequestDispatcher(this.empRoot+basepath+"/mon_updateBusType.jsp").forward(request,
					response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"跳转到新建业务监控信息页面异常！");
		}

	}
	
	/**
	 * 修改
	 * @description    
	 * @param request
	 * @param response       			 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void updateBusType(HttpServletRequest request, HttpServletResponse response ) 
	{
		//业务监控基础信息表ID
		String id=request.getParameter("id");
		//公司编号
		String lgcorpcode = request.getParameter("lgcorpcode");
		boolean ret=false;
		try {
			//业务监控数据信息
			LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
			condition.put("id", id);
			condition.put("corpcode", lgcorpcode);
			//基础信息表
			List<LfMonBusbase> baseInfo=baseBiz.getByCondition(LfMonBusbase.class, condition, null);
			if(baseInfo!=null&&baseInfo.size()>0){
				LfMonBusbase base=baseInfo.get(0);
				//告警手机号
				String monphone=request.getParameter("monphone");
				//告警邮箱
				String monemail=request.getParameter("monemail");
				//监控开始时间
				String begintime=request.getParameter("begintime");
				//监控截止时间
				String endtime=request.getParameter("endtime");
				//区域
				String areaCodes=request.getParameter("allAreas");
				//操作员ID
				//String lguserid = request.getParameter("lguserid");
				//漏洞修复 session里获取操作员信息
				String lguserid = SysuserUtil.strLguserid(request);

				//公司编号
				if(lgcorpcode!=null&&!"".equals(lgcorpcode)){
					base.setCorpcode(lgcorpcode);
				}
				if(monphone!=null&&!"".equals(monphone)){
					base.setMonphone(monphone);	
				}else{
					//数据库是必输的
					base.setMonphone(" ");	
				}
				if(monemail!=null&&!"".equals(monemail)){
					base.setMonemail(monemail);	
				}else{
					base.setMonemail(" ");	
				}
				if(begintime!=null&&!"".equals(begintime)){
					base.setBegintime(Timestamp.valueOf(begintime+" 00:00:00"));	
				}
				if(endtime!=null&&!"".equals(endtime)){
					base.setEndtime(Timestamp.valueOf(endtime+" 23:59:59"));	
				}
				if(lguserid!=null&&!"".equals(lguserid)){
					base.setModiuserid(Long.parseLong(lguserid));
				}
				//如果值为空表示全区域
				if(areaCodes!=null&&!"".equals(areaCodes)){
					areaCodes=","+areaCodes;
					base.setAreacode(areaCodes);			
				}else{
					base.setAreacode("-1");
				}
				base.setUpdatetime(new Timestamp(System.currentTimeMillis()));
				//保存方法
			 ret= biz.update(base, setLfMonBusdata(request,base,"update"));
			}
			response.getWriter().print(ret);
//			request.getRequestDispatcher("/mon_busCfg.htm?method=find&isupdate="+ret+"&lgcorpcode="+lgcorpcode).forward(request,
//					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"跳转到新建业务监控信息页面异常！");
		}
		
	}
	
	
	/**
	 *列表状态下，点击单个删除
	 * @description    
	 * @param request
	 * @param response       			 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response ) 
	{
			try {
				boolean result=false;
				String lgcorpcode=request.getParameter("lgcorpcode");
				String id=request.getParameter("id");
				if(id!=null&&!"".equals(id)){
					 result=biz.delete(id,lgcorpcode);
				}
				response.getWriter().print(result);
			} catch (IOException e) {
				EmpExecutionContext.error(e,"单个删除业务异常！");
			}
		
		
	}
	/**
	 * 点击关闭/开启
	* @Description: TODO
	* @param @param request
	* @param @param response
	* @return void
	 */
	public void changeStatus(HttpServletRequest request, HttpServletResponse response ) 
	{
		try {
			boolean result=false;
			String status=request.getParameter("status");
			String id=request.getParameter("id");
			String lgcorpcode=request.getParameter("lgcorpcode");
			if(id!=null&&!"".equals(id)){
				 result=biz.changeStatus(id,status,lgcorpcode);
			}
			response.getWriter().print(result);
		} catch (IOException e) {
			EmpExecutionContext.error(e,"点击关闭/开启业务异常！");
		}
	}
	
	/**
	 * 写日志
	 * 
	 * @param request
	 *        请求对象
	 * @param opModule
	 *        菜单名称
	 * @param opContent
	 *        操作内容
	 * @param opType
	 */
	public void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,opModule+opType+opContent+"日志写入异常");
		}
	}

}
