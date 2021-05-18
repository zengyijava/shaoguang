package com.montnets.emp.areaphno.servlet;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.areaphno.biz.AreaPhNoBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.MobileArea;
import com.montnets.emp.servmodule.txgl.entity.ProvinceCity;
import com.montnets.emp.servmodule.txgl.vo.AreaPhNoVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class seg_areaPhoneNoSvt extends BaseServlet {
	final ErrorLoger errorLoger = new ErrorLoger();
	//操作模块
	final String opModule=StaticValue.GATE_CONFIG;
	final String opSper = StaticValue.OPSPER;
	
	private final String empRoot="txgl";
	
	private final String basePath="/areaphno";
	
	private final AreaPhNoBiz biz=new AreaPhNoBiz();
	
	public void find(HttpServletRequest request, HttpServletResponse response){
		PageInfo pageInfo = new PageInfo();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		HttpSession session = request.getSession(false);
		pageSet(pageInfo, request);
		String mobile=request.getParameter("mobile");
		String areaCode = request.getParameter("areaCode");
		String province = request.getParameter("province");
		String servePro = request.getParameter("servePro");
		String city=request.getParameter("city");
		session.setAttribute("mobile", mobile);
		session.setAttribute("provincecode", areaCode);
		session.setAttribute("province", province);
		session.setAttribute("servePro", servePro);
		session.setAttribute("city", city);
		if(mobile!=null&&!"".equals(mobile))
		{
			conditionMap.put("mobile", mobile);
		}
		if(areaCode!=null&&!"".equals(areaCode))
		{
			conditionMap.put("provinceCode", areaCode);
		}
		if(province!=null&&!"".equals(province))
		{
			conditionMap.put("province", province);
		}
		if(servePro!=null&&!"".equals(servePro))
		{
			conditionMap.put("servePro", servePro);
		}
		if(city!=null&&!"".equals(city))
		{
			conditionMap.put("city", city);
		}
		try {
			List<AreaPhNoVo> areaList = biz.getAreaPhNoList(conditionMap, pageInfo);
			if(areaList != null && areaList.size() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncrypt(areaList, "Id", "KeyId");
					if(!result)
					{
						EmpExecutionContext.error("查询区域号段管理列表，参数加密失败。");
						throw new Exception("查询区域号段管理列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询区域号段列表，从session中获取加密对象为空！");
					areaList.clear();
					throw new Exception("查询区域号段列表，获取加密对象失败。");
				}
			}
			List<String> provinceList=biz.provinceList();
			Map<String,List<String>> provinceAndCityMap=biz.provinceCityMap();
			request.setAttribute("provinceList", provinceList);
			request.setAttribute("provinceAndCityMap", provinceAndCityMap);
			request.setAttribute("areaList", areaList);
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(this.empRoot + this.basePath + "/areaphno.jsp").forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"区域号段管理查询异常"));
		}
		
	}
	
	public void doAdd(HttpServletRequest request, HttpServletResponse response) {
		List<String> provinceList=null;
		Map<String,List<String>> provinceAndCityMap=null;
		try {
			provinceList=biz.provinceList();
			provinceAndCityMap=biz.provinceCityMap();
			request.setAttribute("provinceList", provinceList);
			request.setAttribute("provinceAndCityMap", provinceAndCityMap);
			request.getRequestDispatcher(this.empRoot  + this.basePath  + "/addAreaPhNo.jsp")
			.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"打开新增运营商号段页面异常！"));
		}
	}
	
	/**
	 * 添加一条号段记录
	 * 
	 * @param request
	 * @param response
	 */
	public void add(HttpServletRequest request, HttpServletResponse response){
		SuperOpLog spLog = new SuperOpLog();
		//日志内容变量
		String opContent="";
		//日志添加的数据
		StringBuffer newStr=new StringBuffer("");
		
		MobileArea mobileArea=new MobileArea();
		PrintWriter writer=null;
		try{
			writer=response.getWriter();
		    String mobile = request.getParameter("mobile");
	        String city = request.getParameter("city");
	        List<ProvinceCity> codeList=biz.findAreaCodeByCity(city);
	        String areaCode = codeList.get(0).getAreacode();
	      
    	    mobileArea.setAreacode(areaCode);
    	    mobileArea.setCreatetime(new Timestamp(System.currentTimeMillis()));
    	    mobileArea.setMobile(mobile);
    	    String result=this.checkMobile(request, response, mobile);
			if(result!=null&&result.equals("isExit")){
				writer.print(result);
				writer.flush();
				return;
			}
	    	boolean issuccess = biz.addAreaPhNo(mobileArea);
	    	//新添加的数据拼接
	    	if(issuccess==true){
	    		//新增成功
	    		writer.print("true");
	    	 }else{
	    		 //新增失败
	    		 writer.print("false");
	    	 }
	      writer.flush();
		}catch(Exception e1){
			//request.setAttribute("result", "false");
			//添加操作失败日志
			EmpExecutionContext.error(errorLoger.getErrorLog(e1,"新增区域号段异常！"));
			if(writer!=null){
				writer.print("false");
				writer.flush();
			}
		}
	}
	
    /**
	 * 删除一条指区域号段信息
	 * @param request
	 * @param response
	 */
	public void deleteAreaPhoneNo(HttpServletRequest request, HttpServletResponse response){
		PrintWriter writer = null;
		//日志内容变量
		String opContent=null;
		//日志修改前数据
		StringBuffer oldStr=new StringBuffer("");
		try {
			//String id = request.getParameter("id");
			String id;
			String keyId = request.getParameter("keyId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				id = encryptOrDecrypt.decrypt(keyId);
				if(id == null)
				{
					EmpExecutionContext.error("删除区域号段信息，参数解密码失败，keyId:"+keyId);
					throw new Exception("删除区域号段信息，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("删除区域号段信息，从session中获取加密对象为空！");
				throw new Exception("删除区域号段信息，参数解密码失败。");
			}
			writer = response.getWriter();
			//写日志需查数据
			MobileArea lab=new BaseBiz().getById(MobileArea.class, id);
			if(lab!=null){
				//旧字符串拼接
				 oldStr.append(id).append("，").append(lab.getAreacode()).append("，").append(lab.getCreatetime()).append("，")
	    		  .append("，").append(lab.getMobile());
			}
			int result = biz.delete(id);
			writer.print(result);
			if(result>0){
				opContent = "删除区域号段成功条数"+result+"条（" + oldStr + "）";
			}else{
				opContent = "删除区域号段失败条数"+result+"条（" + oldStr + "）";
			}
			//增加操作日志
			setLog(request, "区域号段日志", opContent, StaticValue.DELETE);
			writer.flush();
		} catch (Exception e) {
			
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"删除一条区域号段信息异常！"));
			if(writer!=null){
				writer.print("-1");
				writer.flush();
			}
		} 
		
		
	}
	public void addCity(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String,List<String>> provinceAndCityMap=null;
		try{
			String province = request.getParameter("province");
			String citySelected = request.getParameter("city");
			provinceAndCityMap = biz.provinceCityMap();
			List<String> cityList = provinceAndCityMap.get(province);
			if(cityList != null && cityList.size() > 0){
				response.getWriter().write(JSONObject.toJSONString(cityList)+"&"+citySelected);
			}else {
				response.getWriter().write("");
			}
		}catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 判断号段是否存在
	 * @param request
	 * @param response
	 * @param mobile 要新增的号段
	 * @return
	 * @throws Exception
	 */
	public String checkMobile(HttpServletRequest request, HttpServletResponse response,String mobile) throws Exception{
		//List<String> mobileList=null;
		String result=null;
		try {
//			mobileList=biz.mobileList();
//			if(mobileList!=null&&mobileList.size()>0){
//				if(mobileList.contains(mobile)){
//					result="isExit";
//				}
//			}
			if(biz.mobileIsExit(mobile)){
				result="isExit";
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
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
	private void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,opModule+opType+opContent+"日志写入异常"));
		}
	}
	/**
	 * 获取加密对象
	 * @description    
	 * @param request
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午07:29:28
	 */
	public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request)
	{
		try
		{
			ParamsEncryptOrDecrypt encryptOrDecrypt = null;
			//加密对象
			Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("区域号段管理,从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "区域号段管理,从session获取加密对象异常。");
			return null;
		}
	}
	
}
