package com.montnets.emp.gateway.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.gateway.AcmdQueue;
import com.montnets.emp.entity.gateway.AgwParamConf;
import com.montnets.emp.entity.gateway.AgwParamValue;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.gateway.biz.GatewayBiz;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.GwCluStatus;
import com.montnets.emp.util.PageInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 网关运行参数配置
 * @project p_txgl
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-3-4 下午02:52:33
 * @description
 */
@SuppressWarnings("serial")
public class gat_webgateASvt extends BaseServlet {

    private final String empRoot="txgl";

    private final String basePath="/gateway";

	private final ErrorLoger errorLoger = new ErrorLoger();
	/**
	 * 网关运行参数配置查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		PageInfo pageInfo = new PageInfo();
		BaseBiz baseBiz = new BaseBiz();
		try
		{
			//网关编号
			String gwNo=request.getParameter("gwNo");
			//网关编号为空，则填默认值
			if(gwNo==null||"".equals(gwNo))
			{
				gwNo="99";
			}

			pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			// conditionMap.put("paramAttribute", "0");
			conditionMap.put("gwType", "4000");
			orderbyMap.put("paramItem", StaticValue.ASC);
			List<AgwParamConf> paramList = baseBiz.getByCondition(AgwParamConf.class, conditionMap, orderbyMap);

			conditionMap.clear();
			//网关编号根据传递的参数来
			conditionMap.put("gwNo",gwNo);
			List<AgwParamValue> valueList = baseBiz.getByCondition(AgwParamValue.class, conditionMap, orderbyMap);
			LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
			if(valueList != null && valueList.size()>0)
			{
				for(AgwParamValue value : valueList)
				{
					valueMap.put(value.getParamItem(), value.getParamValue());
				}
			}

			//查询网关
			conditionMap.clear();
			orderbyMap.clear();
			//设置查询条件
			conditionMap.put("gwType", "4000");
			//设置排序条件
			orderbyMap.put("gwNo", StaticValue.DESC);
			List<GwCluStatus> gwCluStatusList =baseBiz.getByCondition(GwCluStatus.class, conditionMap, orderbyMap);
			request.setAttribute("gwCluStatusList",gwCluStatusList);
			request.setAttribute("gwNo",gwNo);
			//加密ID
			String keyId = "";
			//ID加密
			if(gwNo != null && gwNo.trim().length() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					keyId = encryptOrDecrypt.encrypt(gwNo);
					if(keyId == null)
					{
						EmpExecutionContext.error("网关运行参数配置信息列表，参数加密失败。");
						throw new Exception("网关运行参数配置信息列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("网关运行参数配置信息列表，从session中获取加密对象为空！");
					throw new Exception("网关运行参数配置信息列表，获取加密对象失败。");
				}
			}
			request.setAttribute("keyId", keyId);
			request.setAttribute("valueMap", valueMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("paramList", paramList);
			request.getRequestDispatcher(empRoot + basePath + "/gat_webgate.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"网关运行参数配置查询失败！"));
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(empRoot + basePath +"/gat_webgate.jsp")
				.forward(request, response);
			} catch (ServletException e1) {
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"网关运行参数配置查询servlet异常！"));
			} catch (IOException e1) {
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"网关运行参数配置查询servlet跳转异常！"));
			}
		}
	}

	/**
	 * 修改网关运行参数配置
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response)
	{
		List<AgwParamValue> paramList = new ArrayList<AgwParamValue>();
		HashMap<String,String> valueMap = new HashMap<String,String>();
		BaseBiz baseBiz = new BaseBiz();
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			//String gwNo = request.getParameter("gwNo");
			String gwNo;
			String keyId = request.getParameter("keyId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				gwNo = encryptOrDecrypt.decrypt(keyId);
				if(gwNo == null)
				{
					EmpExecutionContext.error("保存网关运行参数配置/通道运行参数配置，参数解密码失败，keyId:"+keyId);
					throw new Exception("保存网关运行参数配置/通道运行参数配置，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("保存网关运行参数配置/通道运行参数配置，从session中获取加密对象为空！");
				throw new Exception("保存网关运行参数配置/通道运行参数配置，获取加密对象失败。");
			}
			String gwType = request.getParameter("gwType");
			String allParamItems = request.getParameter("allParamItems");
			String allParamValues = request.getParameter("allParamValues");

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("gwNo", gwNo);
			List<AgwParamValue> valueList = baseBiz.getByCondition(AgwParamValue.class, conditionMap, null);

			boolean isFirst = false;
			if(valueList == null || valueList.size() == 0)
			{
				isFirst = true;
			}else
			{
				for(AgwParamValue paramValue : valueList)
				{
					valueMap.put(paramValue.getParamItem(), paramValue.getParamValue());
				}
			}

			String[] itemArray = allParamItems.split(",");
			String[] valueArray = allParamValues.split("&");

			if(isFirst)
			{
				for(int i = 0; i<itemArray.length;i++)
				{
					AgwParamValue agwParam = new AgwParamValue();
					if(itemArray[i] != null && !"".equals(itemArray[i])){
						agwParam.setGwNo(Integer.valueOf(gwNo));
						agwParam.setGwType(Integer.valueOf(gwType));
						agwParam.setParamItem(itemArray[i]);
						if(itemArray[i].equals("RESNDRPTERCODE") && (valueArray[i] == null || "".equals(valueArray[i]))){
							agwParam.setParamValue(" ");
						}else{
							//agwParam.setParamValue(valueArray[i].replace("$$", "&"));
							//界面控制不允许输入&符号
							//如果值为空，则填入空格 modify by tanglili 20160617
							String valueTemp=(valueArray[i]==null||"".equals(valueArray[i]))?" ":valueArray[i];
							agwParam.setParamValue(valueTemp);
						}
						paramList.add(agwParam);
					}
				}
				/*conditionMap.clear();
				conditionMap.put("gwType", gwType);
				conditionMap.put("paramAttribute", "1");
				List<AgwParamConf> confList = baseBiz.getByCondition(AgwParamConf.class, conditionMap, null);
				
				if(confList != null && confList.size() > 0)
				{
					for(AgwParamConf conf : confList)
					{
						AgwParamValue agwParam = new AgwParamValue();
						
						agwParam.setGwNo(Integer.valueOf(gwNo));
						agwParam.setGwType(Integer.valueOf(gwType));
						agwParam.setParamItem(conf.getParamItem());
						agwParam.setParamValue(conf.getDefaultValue());
						
						paramList.add(agwParam);
					}
				}
				*/
				int count = baseBiz.addList(AgwParamValue.class, paramList);
				if(count > 0)
				{
					writer.print("true");
					String opContent="保存网关运行参数配置/通道运行参数配置成功["+allParamItems+"]（"+allParamValues.replace("&", ",")+"）";
		 			setLog(request, "网关运行参数配置/通道运行参数配置", opContent, StaticValue.ADD);
				}else
				{
					writer.print("false");
					String opContent="保存网关运行参数配置/通道运行参数配置失败["+allParamItems+"]（"+allParamValues.replace("&", ",")+"）";
		 			setLog(request, "网关运行参数配置/通道运行参数配置", opContent, StaticValue.ADD);
				}
			}else
			{

				//该段代码考虑到网关升级问题。如果发现隐藏的网关运行参数在数值表中没有，则拷贝过来。
//				conditionMap.clear();
//				conditionMap.put("gwType", "4000");
//				conditionMap.put("paramItem", "RESNDRPTERCODE");
//				valueList= baseBiz.getByCondition(AgwParamValue.class, conditionMap, null);
//				
//				if(valueList == null || valueList.size() ==0)
//				{
//					paramList = new ArrayList<AgwParamValue>();
//					conditionMap.clear();
//					conditionMap.put("gwType", "4000");
//					conditionMap.put("paramAttribute", "2");
//					List<AgwParamConf> confList = baseBiz.getByCondition(AgwParamConf.class, conditionMap, null);
//					for(AgwParamConf conf : confList)
//					{
//						AgwParamValue agwParam = new AgwParamValue();
//						
//						agwParam.setGwNo(Integer.valueOf(gwNo));
//						agwParam.setGwType(Integer.valueOf("4000"));
//						agwParam.setParamItem(conf.getParamItem());
//						agwParam.setParamValue(conf.getDefaultValue());
//						
//						paramList.add(agwParam);
//					}
//					baseBiz.addList(AgwParamValue.class, paramList);
//				}


				String cmdParam = "";
				String paramValue = "";
	 			for(int i = 0; i<itemArray.length;i++)
				{

					String item = itemArray[i];
					if(valueMap.get(item)!= null)
					{
						LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
						conditionMap.clear();
						conditionMap.put("gwNo", gwNo);
						if(item != null && !"".equals(item)
								&& !valueArray[i].trim().equals(valueMap.get(item).trim())){
							//界面控制不允许输入&符号
//							paramValue = valueArray[i].replace("$$", "&");
							paramValue = valueArray[i];
							if("".equals(paramValue) || paramValue == null)
							{
								paramValue = " ";
							}
							else
							{
								//有值，去空格
								paramValue=paramValue.trim();
								//去掉空格后，还是有可能为空字符串，ORACLE不允许空字符串，所以需要填空格。
								if("".equals(paramValue))
								{
									paramValue = " ";
								}
							}
							objectMap.put("paramValue",paramValue);
							conditionMap.put("paramItem", itemArray[i]);
							//如果参数存在空格，要去空格
		 					cmdParam += itemArray[i] + "=" + objectMap.get("paramValue").trim() + "&";

		 					baseBiz.update(AgwParamValue.class, objectMap, conditionMap);
						}
					}else
					{
						AgwParamValue agwParam = new AgwParamValue();

						agwParam.setGwNo(Integer.valueOf(gwNo));
						agwParam.setGwType(Integer.valueOf("4000"));
						agwParam.setParamItem(item);
						agwParam.setParamValue(valueArray[i]);

						paramList.add(agwParam);
					}
				}
	 			if(paramList != null && paramList.size() > 0)
	 			{
	 				baseBiz.addList(AgwParamValue.class, paramList);
	 			}

	 			if(!"".equals(cmdParam)) {
					if(cmdParam.lastIndexOf("&")>-1){
						cmdParam = cmdParam.substring(0,cmdParam.length()-1);
					}

					AcmdQueue queue = new AcmdQueue();

					queue.setGwNo(Integer.valueOf(gwNo));
					queue.setGwType(Integer.valueOf(gwType));
					queue.setCmdType(5000);
					queue.setCmdParam(cmdParam);
					queue.setId(0);

					baseBiz.addObj(queue);
				}

	 			writer.print("true");
	 			//增加操作日志
	 			String opContent="保存网关运行参数配置/通道运行参数配置（此处为存在很多添加修改操作不出异常即为成功）成功["+allParamItems+"]（"+allParamValues.replace("&", ",")+"）";
	 			setLog(request, "网关运行参数配置/通道运行参数配置", opContent, StaticValue.UPDATE);
			}
		} catch (Exception e) {
			writer.print("error");
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"修改网关运行参数配置异常！"));
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

	/**
	 * 新建网关
	 * @description
	 * @param request
	 * @param response
	 * @author tanglili <jack860127@126.com>
	 * @datetime 2016-4-29 上午11:33:55
	 */
	public void add(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			BaseBiz baseBiz=new BaseBiz();
			writer = response.getWriter();
			int gwNo=0;
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("gwType", "4000");
			orderbyMap.put("gwNo", StaticValue.ASC);
			List<GwCluStatus> gwCluStatusList =baseBiz.getByCondition(GwCluStatus.class, conditionMap, orderbyMap);
			if(gwCluStatusList != null && gwCluStatusList.size() > 0)
			{
				//设置网关编号
				gwNo = gwCluStatusList.get(0).getGwNo() - 1;
				GwCluStatus gwCluStatus=new GwCluStatus();
				//设置类型
				gwCluStatus.setGwType(4000);
				//主用网关编号
				gwCluStatus.setPriGwNo(99);
				//运行状态
				gwCluStatus.setRunstatus(0);

				//网关编号
				gwCluStatus.setGwNo(gwNo);
				//网关权值
				gwCluStatus.setGweight(gwNo);
				//
				gwCluStatus.setRunweight(0);
				gwCluStatus.setUpdtime(new Timestamp(System.currentTimeMillis()));


				conditionMap.clear();
				orderbyMap.clear();
				conditionMap.put("gwType", "4000");
				orderbyMap.put("paramItem", StaticValue.ASC);
				List<AgwParamConf> paramConfList = baseBiz.getByCondition(AgwParamConf.class, conditionMap, orderbyMap);
				List<AgwParamValue> paramValueList = new ArrayList<AgwParamValue>();
				for(AgwParamConf conf : paramConfList)
				{
					AgwParamValue agwParam = new AgwParamValue();

					agwParam.setGwNo(gwNo);
					agwParam.setGwType(Integer.valueOf("4000"));
					agwParam.setParamItem(conf.getParamItem());
					agwParam.setParamValue(conf.getDefaultValue());
					paramValueList.add(agwParam);
				}
				//调用业务层方法初始化网关
				GatewayBiz gatewayBiz=new GatewayBiz();
				boolean flag=gatewayBiz.addGW(gwCluStatus, paramValueList);
				if(flag)
				{
					writer.print("true");
				}
				else
				{
					writer.print("false");
				}
			}else
			{
				writer.print("false");
			}

		}
		catch (Exception e)
		{
			if(writer!=null){
				writer.print("error");
			}
			EmpExecutionContext.error(e,"新建网关失败！");
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
				EmpExecutionContext.error("网关运行参数配置，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网关运行参数配置，从session获取加密对象异常。");
			return null;
		}
	}
}
