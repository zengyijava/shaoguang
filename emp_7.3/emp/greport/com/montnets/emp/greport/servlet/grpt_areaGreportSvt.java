package com.montnets.emp.greport.servlet;


import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.greport.bean.GreportParamValue;
import com.montnets.emp.greport.biz.AreaGreportBiz;
import com.montnets.emp.greport.biz.GreportBiz;
import com.montnets.emp.greport.vo.AreaGreportVo;
import com.montnets.emp.i18n.util.MessageUtils;

/**
 *  区域发送对比图形报表
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-20 下午04:52:40
 * @description
 */

@SuppressWarnings("serial")
public class grpt_areaGreportSvt extends BaseServlet
{
	 // 模块名称
	 private final String empRoot = "greport";

	 // 功能文件夹名
	 private final String base= "/grpt";

	 //区域发送对比biz
	 private final AreaGreportBiz areaGreportbiz = new AreaGreportBiz();
	 private final GreportBiz reportBiz = new GreportBiz();

	/**
	 * 区域发送对比
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		// 短彩类型
		String mstype = request.getParameter("mstype");
		// 企业编码
		String corpCode = reportBiz.getCorpCode(request);
		
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		try
		{
			//默认为短信
			if(mstype == null)
			{
				mstype = "0";
			}
			// 查询条件对象
			String year = request.getParameter("year");
			if(year==null||"".equals(year)){
				year =timeYear;
			}
			//获取年份
			List<String> ys=areaGreportbiz.getYears(Integer.parseInt(mstype));
			List<String> s=(List<String>)request.getSession(GreportParamValue.GET_SESSION_FALSE).getAttribute("ys");
			//判断年份是否有变化
			if(s!=null&&ys.size()<=s.size()){
				s=null;
			}else{
				request.getSession(GreportParamValue.GET_SESSION_FALSE).setAttribute("ys", ys);
			}
			//存储查询条件
			request.setAttribute("mstype", mstype);
			request.setAttribute("year", year);
			
			// 获取数据库短彩类型数据
			GlobConfigBiz gcBiz = new GlobConfigBiz();
			List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
			request.getSession(GreportParamValue.GET_SESSION_FALSE).setAttribute("pagefiledusers", pagefileds);
			String opContent = MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips8", request)+starthms+","+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips2", request)+":"+(System.currentTimeMillis()-startl)+"ms";
			setLog(request, MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips9", request), opContent, StaticValue.GET);
			request.getRequestDispatcher(this.empRoot + base + "/grpt_mapAreaGreport.jsp?lgcorpcode=" + corpCode).forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"总图形报表趋势sevlet查询异常");
		}
	}

	
	
	
	
	
	
	/**
	 * 输出区域图形报表所需的json
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createBbJson(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		// 短彩类型
		String msType = request.getParameter("mstype");
		// 企业编码
		String corpCode = reportBiz.getCorpCode(request);
		//图形类型 map或者bar
		String type=request.getParameter("type");
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		//动态bean结果集
		List<DynaBean> areatreportList = null;
		DecimalFormat dF=new DecimalFormat("00");
		try
		{
				//短信或彩信
				if(msType == null||"".equals(msType))
				{
					msType = "0";
				}

				// 查询条件对象
				AreaGreportVo areaGreportVo = new AreaGreportVo();
				//省份存储List
				List<String> provinceList=new ArrayList<String>();
				//柱形图数据存储list
				List<String> barList=new ArrayList<String>();
				//年
				String year = request.getParameter("year");
				//月
				String imonth = request.getParameter("imonth");
				if(year==null||"".equals(year)){
					year =timeYear;
				}
				//标题
				String titile="";
				if(imonth==null||"".equals(imonth)){
					titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips15", request);
				}else{
					//titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+imonth+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips16", request);
					String language  = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
					if (StaticValue.ZH_HK.equals(language)) {
						titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+getMonth(imonth)+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips16", request);
					}else{
						titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+imonth+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips16", request);
					}
				}
				// 短信类型
				areaGreportVo.setMstype(Integer.parseInt(msType));
				//年
				areaGreportVo.setY(year);
				areaGreportVo.setImonth(imonth);
				//企业编码
				if(StaticValue.getCORPTYPE() ==1){
					areaGreportVo.setCorpCode(corpCode);
				}
				//获取区域结果集
				areatreportList = areaGreportbiz.getAreaGreportByVo(areaGreportVo);
				provinceList=areaGreportbiz.getProvince();
				//总数
				long maxCount=0L;//最大发送量
				if(areatreportList!=null&&areatreportList.size()>0){
					for(int i=0;i<areatreportList.size();i++){
						DynaBean agvo=areatreportList.get(i);
						String province =agvo.get("province")==null?"未知":agvo.get("province").toString();//省名
						if(!province.equals("未知")){
							maxCount=Long.parseLong(agvo.get("icount")==null?"0":agvo.get("icount").toString());
							break;
						}
					}
					
				}     
				//拼接json
				StringBuffer mapJson=new StringBuffer();//地图的json
				StringBuffer barJson=new StringBuffer();//柱状图的json
				StringBuffer tempJson=new StringBuffer("\"data\":[");//柱状图的数据存储部分
				//地图json标题
				mapJson.append("{\"title\":{\"text\":\""+titile+"\",\"left\":\"center\"},");
				mapJson.append("\"tooltip\":{\"trigger\":\"item\", \"zlevel\":2,\"backgroundColor\":\"#696969\", \"borderColor\":\"#ccc\",\"borderWidth\":\"1px\", \"borderRadius\":\"4\"," +
						"\"formatter\":\"'"+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_ztfsqs_sf", request)+"：'+data.name+'<br/>'+'"+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips17", request)+":'+data.value%100+'<br/>'+'"+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips18", request)+"：'+parseNumber(parseInt(data.value/100))+'"+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_unit", request)+"';\"},");
				mapJson.append("\"visualMap\":{\"min\":0,\"max\":"+maxCount+",\"left\":\"left\",\"top\":\"bottom\",\"text\":[\""+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips19", request)+"\",\""+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips20", request)+"\"],\"calculable\":\"true\"},");
				mapJson.append("\"series\":[{\"name\":\""+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_yystjbb_dx", request)+"\",\"type\":\"map\",\"mapType\":\"china\",\"roam\":\"true\",\"label\":{\"normal\":{\"show\":\"true\"},\"emphpasis\":{\"show\":\"true\"}},\"mapLocation\":{\"width\":\"700\"},\"data\":[");
				//柱状图json标题	
				barJson.append("{\"tooltip\" : {\"trigger\": \"axis\",\"zlevel\":2,\"backgroundColor\":\"#696969\",\"borderColor\":\"#ccc\",\"borderWidth\":\"1px\", \"borderRadius\":\"4\"}, \"xAxis\":  {\"show\":false, \"type\": \"value\" },\"grid\":{\"x\":\"68\",\"right\":\"20%\" },\"yAxis\": { \"axisTick\":false,\"data\":[");
				//设置查询结果值
				int rankCount=0;
				if(areatreportList!=null&&areatreportList.size()>0){
					int barJsonCount=0;
					if(areatreportList.size()>=10){
						barJsonCount=10;
					}else{
						barJsonCount=areatreportList.size();
					}
					//地图json数据植入
					for(int i=0;i<areatreportList.size();i++){
						DynaBean agvo=areatreportList.get(i);
						//判断是否为空
						if(agvo!=null){
							String province =agvo.get("province")==null?"未知":agvo.get("province").toString();//省名
							//格式化数据
							String count=new DecimalFormat("#0.00").format((agvo.get("icount")==null?0:Long.parseLong(agvo.get("icount").toString())));//某省的发送量
							if(!province.equals("未知")){
								if(rankCount<barJsonCount){
									barList.add(province+","+count);
								}
								rankCount++;
							}
							String[] str=count.split("\\.");
							//拼接value的值使其个位和十位存储的是排名
							mapJson.append("{\"name\":\""+province+"\",\"value\":"+str[0]+dF.format(rankCount)+"."+str[1]+"},");
							provinceList.remove(province);//删除已有数据省份名
						}
					}
				}
				if(barList!=null&&barList.size()>0){
					for(int i=barList.size()-1;i>=0;i--){
						String []batArr=barList.get(i).split(",");
						String barProvince=batArr[0];
						String barCount=batArr[1];
						int barRank=i+1;
						//若是最后一个不用加逗号
						if(i==0){
							barJson.append("\""+barRank+". "+barProvince+"\"");
							tempJson.append(barCount);
						}else{
							tempJson.append(barCount+",");
							barJson.append("\""+barRank+". "+barProvince+"\",");
						}
					}
				}
				//为没有数据的省份添加默认数据
				for(int j=0;j<provinceList.size();j++){
					mapJson.append("{\"name\":\""+provinceList.get(j)+"\",\"value\":0},");
				}
				//去除尾部逗号
				if(mapJson.lastIndexOf(",")!=-1){
					mapJson.deleteCharAt(mapJson.lastIndexOf(","));
				}
				tempJson.append("]");
				barJson.append("]},");
				barJson.append("\"series\": [ { \"name\": \""+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips18", request)+"\", \"barWidth\":20,\"type\": \"bar\",\"itemStyle\":{ \"normal\": {\"label\" : {\"show\": true, \"position\": \"right\"}}},");
				barJson.append(tempJson.toString());
				barJson.append("}]}");
				mapJson.append("]}]}");
				String opContent = MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips8", request)+starthms+","+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips2", request)+":"+(System.currentTimeMillis()-startl)+"ms";
				setLog(request, MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips9", request), opContent, StaticValue.GET);
			//打印json字符串
				if(type.equals("bar")){
					response.getWriter().print(barJson.toString());	
				}else if(type.equals("map")){
					response.getWriter().print(mapJson.toString());
				}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取区域图形报表获取json异常");
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
			Object loginSysuserObj=request.getSession(GreportParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,opModule+opType+opContent+"日志写入异常");
		}
	}
	

	private String getMonth(String monthNum){
		if("1".equals(monthNum)){
			return "January";
		}
		if("2".equals(monthNum)){
			return "February";
		}
		if("3".equals(monthNum)){
			return "March";
		}
		if("4".equals(monthNum)){
			return "April";
		}
		if("5".equals(monthNum)){
			return "May";
		}
		if("6".equals(monthNum)){
			return "June";
		}
		if("7".equals(monthNum)){
			return "July";
		}
		if("8".equals(monthNum)){
			return "August";
		}
		if("9".equals(monthNum)){
			return "September";
		}
		if("10".equals(monthNum)){
			return "October";
		}
		if("11".equals(monthNum)){
			return "November";
		}
		if("12".equals(monthNum)){
			return "December";
		}
		return "";
	}

}

