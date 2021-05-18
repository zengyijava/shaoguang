package com.montnets.emp.bustype.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.bustype.biz.BusDeatilBiz;
import com.montnets.emp.bustype.biz.BusTypeCfgBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.monitor.LfMonBusinfo;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

public class mon_busDeatilSvt extends BaseServlet{

	
	final String empRoot="ptjk";
	final String base="/bustype";
	protected final SuperOpLog spLog = new SuperOpLog();
	private final BaseBiz baseBiz = new BaseBiz();
	private final BusDeatilBiz biz=new BusDeatilBiz();
	public static final String OPMODULE = "程序监控详情";
	
	/**
	 * 业务监控详情
	 * @description    
	 * @param request
	 * @param response       			 
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter=pageSet(pageInfo,request);
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		String areaCodes="";
		try {
			//初始化业务名称与编码
			List<LfBusManager> busList = new BusTypeCfgBiz().getLfBusManager(conditionMap);
			request.setAttribute("busList", busList);
			//公司编号
			String lgcorpcode = request.getParameter("lgcorpcode");
			if(!isFirstEnter){
				//业务名称
				String busname = request.getParameter("busName");
				if(busname!=null){
					busname=busname.trim();
				}
				//业务编码
				String buscode = request.getParameter("busCode");
				//监控状态
				String level=request.getParameter("level");
				//区域
				areaCodes = request.getParameter("allAreas");
				//开始时间
				String sendtime=request.getParameter("sendtime");
				//结束时间
				String recvtime=request.getParameter("recvtime");
				conditionMap.put("busname", busname);
				conditionMap.put("buscode", buscode);
				conditionMap.put("level", level);
				conditionMap.put("areaCodes", areaCodes);
				conditionMap.put("sendtime", sendtime);
				conditionMap.put("recvtime", recvtime);
			}
			request.setAttribute("conditionMap", conditionMap);	
			conditionMap.put("lgcorpcode", lgcorpcode);
			List<LfMonBusinfo> infoList=biz.getDeatilInfo(conditionMap, pageInfo);
			request.setAttribute("infoList", infoList);
			request.setAttribute("pageInfo", pageInfo);
			//页面区域处理
			Map<Long,String> nameMap=new HashMap<Long,String>();
			Map<Long,String> showNameMap=new HashMap<Long,String>();
			if(infoList!=null&&infoList.size()>0){
				for(int i=0;i<infoList.size();i++){
					String showName="";
					String name="";
					LfMonBusinfo busbase=infoList.get(i);
					List<DynaBean> citys=new BusTypeCfgBiz().getProvinceList(busbase.getAreacode());
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
			List<DynaBean> citys=new BusTypeCfgBiz().getProvinceList(null);
			request.setAttribute("citys", citys);
			
			//区域名称
			String areaName = request.getParameter("areaName");
			 String allAreas=request.getParameter("allAreas");
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
			 
//			List<DynaBean> selectCitys=new BusTypeCfgBiz().getProvinceList(areaCodes);
//			request.setAttribute("selectCitys", selectCitys);
			
			//***日志部分***
			//查询出的数据的总数量
			int totalCount =  pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间："+sdf.format(stratTime)+ " 耗时：" 
			+(System.currentTimeMillis()-stratTime) + "ms  数量："+totalCount;
			setLog(request, "业务监控详情查询", opContent, StaticValue.GET);
			request.getRequestDispatcher(this.empRoot+base+"/mon_busDeatil.jsp").forward(request,response);
		
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务监控详情查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_busDeatil.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"业务监控详情查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"业务监控详情查询异常！");
			}
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
