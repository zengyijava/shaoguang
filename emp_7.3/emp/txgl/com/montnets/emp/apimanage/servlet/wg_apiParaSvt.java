package com.montnets.emp.apimanage.servlet;

import com.montnets.emp.apimanage.biz.ApiParaBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.i18n.util.MessageUtils;
//import com.montnets.emp.ottbase.constant.StaticValue;
import com.montnets.emp.servmodule.txgl.entity.GwBasePara;
import com.montnets.emp.util.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * API接口参数管理
 * 
 * @author Administrator
 * 
 */
public class wg_apiParaSvt extends BaseServlet {
	//EMP根目录
	private final String empRoot = "txgl";
	
    //basePath路径
	private final String basePath = "/apimanage";

	/**
	 * API接口参数查询方法
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter;
		try {
			//设置分页信息
			isFirstEnter = pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			// 方法名称
			String funName = request.getParameter("funName");

			// 参数名称
			String argName = request.getParameter("argName");

			// 请求类型
			String cmdType = request.getParameter("cmdType");

			// 创建时间 开始时间
			String startdate = request.getParameter("startdate");

			// 创建时间 结束时间
			String enddate = request.getParameter("enddate");

			// 设置查询条件
			// 方法名称查询条件
			if (funName != null && !"".equals(funName.trim())) {
				conditionMap.put("funName", funName.trim());

			}
			// 参数名称查询条件
			if (argName != null && !"".equals(argName)) {
				conditionMap.put("argName&like", argName);
			}
			
			// 请求类型查询条件
			if (cmdType != null && !"".equals(cmdType)) {
				conditionMap.put("cmdType", cmdType);
			}
			
			// 创建时间 开始时间   查询条件
			if (startdate != null && !"".equals(startdate)) {
				conditionMap.put("createTime&>=", startdate);
			}
			
			// 创建时间 结束时间   查询条件
			if (enddate != null && !"".equals(enddate)) {
				conditionMap.put("createTime&<=", enddate);
			}
		    
			//设置排序条件
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("id", StaticValue.ASC);
			
			//查询API接口参数
			List<GwBasePara> gwBaseParaList = new ApiParaBiz().getGwBasePara(conditionMap, orderbyMap, pageInfo);

			/*国际化处理*/
			Map<String,String> paramNameDescription = new HashMap<String, String>();
			paramNameDescription.put("用户账号：长度最大6个字符，统一大写", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_userid",request));
			paramNameDescription.put("用户密码：定长小写32位字符", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_pwd",request));
			paramNameDescription.put("短信接收的手机号：只能填一个手机号", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_mobile",request));
			paramNameDescription.put("短信内容：最大支持1000个字", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_content",request));
			paramNameDescription.put("时间戳：24小时制格式：MMDDHHMMSS，定长10位", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_timestamp",request));
			paramNameDescription.put("业务类型：最大可支持32个长度的英文数字组合的字符串", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_svrtype",request));
			paramNameDescription.put("扩展号:长度不能超过6位", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_exno",request));
			paramNameDescription.put("用户自定义流水号", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_custid",request));
			paramNameDescription.put("自定义扩展数据", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_exdata",request));
			paramNameDescription.put("用户自定义参数，最大64个字节", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_param",request));
			paramNameDescription.put("模块ID，4字节整型正数（0~2^31-1）", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_moduleid",request));
			paramNameDescription.put("短信定时发送时间", MessageUtils.extractMessage("mwadmin","txgl_paramNameDescri_attime",request));

			for (GwBasePara gwBasePara:gwBaseParaList) {
					if(paramNameDescription.containsKey(gwBasePara.getArgDes())){
						gwBasePara.setArgDes(paramNameDescription.get(gwBasePara.getArgDes()));
					}
			}

			//设置 request参数
			request.setAttribute("gwBaseParaList", gwBaseParaList);
			request.setAttribute("pageInfo", pageInfo);
			//设置时间
			if (startdate != null && !"".equals(startdate)) {
				conditionMap.put("startdate", startdate);
			}
			if (enddate != null && !"".equals(enddate)) {
				conditionMap.put("enddate", enddate);
			}
			request.setAttribute("conditionMap", conditionMap);
			
			int corptype = StaticValue.getCORPTYPE();
			//单企业
			if(corptype==0)
			{
			//页面跳转
			request
					.getRequestDispatcher(
							empRoot + basePath + "/wg_apiPara.jsp").forward(
							request, response);
			}else
			{
				//页面跳转
				request
						.getRequestDispatcher(
								empRoot + basePath + "/wg_apiParaMulit.jsp").forward(
								request, response);
			}

		} catch (Exception e) {
			String str = "";
			//记录错误日志
			EmpExecutionContext.error(e, "查询API接口参数管理" + str + "时，产生异常!");
		}
	}
}
