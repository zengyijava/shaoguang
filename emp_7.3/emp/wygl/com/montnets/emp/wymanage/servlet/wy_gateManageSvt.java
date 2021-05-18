package com.montnets.emp.wymanage.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wy.AIpcominfo;
import com.montnets.emp.entity.wy.ASiminfo;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.wymanage.biz.GateManageBiz;
import com.montnets.emp.wymanage.vo.ASiminfoVo;
import com.montnets.emp.wymanage.vo.GateManageVo;
import com.montnets.emp.wymanage.vo.GateShowVo;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class wy_gateManageSvt extends BaseServlet
{
	private final BaseBiz baseBiz=new BaseBiz();
	private final SuperOpLog spLog=new SuperOpLog();
	// 模块名称
	private final String empRoot = "wygl";
	// 功能文件夹名
	private final String base = "/wymanage";

	
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long stime = System.currentTimeMillis();
		//定义网优通道list集合
		List<DynaBean> ipcomList = null;
		//存储sim卡信息
		Map<Integer,List<ASiminfoVo>> icomsimmap=new HashMap<Integer, List<ASiminfoVo>>(); 
		//分页对象
		PageInfo pageInfo=new PageInfo();
		//通道名称
		String gatename=request.getParameter("gatename");
		//ip地址
		String ipadress=request.getParameter("ipadress");
		//端口号
		String portnum=request.getParameter("portnum");
		//SIM卡号
		String phoneno=request.getParameter("phoneno");
		//运营商
		String simunicom=request.getParameter("simunicom");
		//网优通道biz
		GateManageBiz gmbiz=new GateManageBiz();
		//vo对象
		GateManageVo gatemangevo=new GateManageVo();
		try
		{
			//分页初始化
			pageSet(pageInfo, request);
			//通道名称
			gatemangevo.setGatename(gatename);
			//IP地址
			gatemangevo.setIp(ipadress);
			//端口号
			gatemangevo.setPort(portnum);
			//SIM卡号
			gatemangevo.setPhoneno(phoneno);
			//运营商
			gatemangevo.setUnicom(simunicom);
			//获取IPCOMM账户列表
			ipcomList=gmbiz.getAIpcomsByVO(gatemangevo, icomsimmap, pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("ipcomList", ipcomList);
			request.setAttribute("icomsimmap", icomsimmap);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"网优通道管理","("+sDate+")查询", StaticValue.GET);
		} catch (Exception e)
		{
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e,"网优通道账户查询失败！");
		}finally
		{
			//跳转
			request.getRequestDispatcher(this.empRoot + base + "/wy_gateManage.jsp").forward(request, response);
		}
	}

	/**
	 * 新增网优通道账户
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String opModule = "网优通道管理";
		String opContent;
		String opType = "ADD";
		String opStatus = "失败";
		String result = null;
		//通道名称
 		String agatename = request.getParameter("agatename");
		//IP地址
		String aipadress = request.getParameter("aipadress");
		//内部平台IP地址
		String ainneripadress = request.getParameter("ainneripadress");
		//端口号
		String aportnum = request.getParameter("aportnum");
		//内部平台端口号
		String ainnerportnum = request.getParameter("ainnerportnum");
		//企业签名
		String corpsign = request.getParameter("corpsign");
		//备注
		String acommon = request.getParameter("acommon");
		//sim卡详情
		String simjsonstr=request.getParameter("arrSimDetailVal");

		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("gatename",agatename);
			conditionMap.put("ip",aipadress);
			conditionMap.put("port",aportnum);
			List<AIpcominfo> aipcomList = baseBiz.getByCondition(
					AIpcominfo.class, conditionMap, null);
			WgMsgConfigBiz wb=new WgMsgConfigBiz();
			//检查网优通道名称ip端口有重复或者与通道名称重复
			if (aipcomList != null && aipcomList.size() > 0&&wb.isGateNameExist(agatename,"1")) {
				result = "ipcomExists";
				return;
			}
			//拼接SIM卡号
			String phonenos="";
			if(simjsonstr!=null&&!"".equals(simjsonstr)&&!"@".equals(simjsonstr)){
				if(simjsonstr.contains("@@")){
//					response.getWriter().print("simExists");
					result = "simExists";
					return;
				}
				//拆分拼接的sim卡号
				String[] simrow=simjsonstr.split("@");
				for (String srow : simrow)
				{
					String[] coln=srow.split(",");
					if(coln.length==4){
						phonenos = phonenos +  coln[1] + ",";
						//判断号码不能为空
						if("".equals(coln[1])){
							result = "simExists";
							return;
						}else{
							String[] haoduan = new WgMsgConfigBiz().getHaoduan();
							PhoneUtil p=new PhoneUtil();
							if(p.getPhoneType(coln[1], haoduan)==-1){
								result = "simExists";
								return;
							}
						}
					}
				}
				//去除后面的逗号
				if(!"".equals(phonenos)){
					phonenos=phonenos.substring(0, phonenos.length()-1);
				}else{
					result = "simExists";
					return;
				}
			}else{
				result  = "simExists";
				return;
			}
			conditionMap.clear();
			conditionMap.put("phoneno&in",phonenos);
			List<ASiminfo> simList = baseBiz.getByCondition(ASiminfo.class, conditionMap,null);
			//检查SIM卡编码重复
			if (simList != null && simList.size() > 0)
			{
				String phones="\n";
				for(ASiminfo sims:simList){
					phones=phones+sims.getPhoneno()+"\n";
				}
//				response.getWriter().print("simExists:"+phones);
				result = "simExists:"+phones;
				return;
			}
			AIpcominfo aipcom = new AIpcominfo();
			//都不重复可以保存入库
			aipcom.setCommon(acommon);
			aipcom.setCorpsign(corpsign);
			aipcom.setGatename(agatename);
			aipcom.setIp(aipadress);
			aipcom.setPort(Integer.parseInt(aportnum));
			aipcom.setPtip(ainneripadress);
			aipcom.setPtport(Integer.parseInt(ainnerportnum));
			
			GateManageBiz gmbiz=new GateManageBiz();
			GateShowVo showvo=gmbiz.addWyGate(aipcom, simjsonstr);
			boolean opFlag = false;
			
			if(showvo!=null&&showvo.getCorpsign()!=null&&showvo.getGatename()!=null&&showvo.getGatenum()!=null&&showvo.getGateusername()!=null){
				opFlag =  true;
				opStatus = "成功";
			}
			if(opFlag){
				String str=showvo.getGatename()+","+showvo.getGatenum()+","+showvo.getGateusername()+","+showvo.getCorpsign();
				result = "true|"+str;
			}else{
				result = ERROR;
			}
		} catch (Exception e) {
			result = ERROR;
			EmpExecutionContext.error(e,"新增网优通道异常！");
		}finally{
			//增加操作日志
			opContent = "新增网优通道"+opStatus+"。（通道名称："+agatename+"，EMP网关IP及端口："+ainneripadress+":"+ainnerportnum+"，运营商网关IP及端口：："+aipadress+":"+aportnum+"，短信签名："+corpsign+"，SIM卡信息："+simjsonstr+"，备注："+acommon+")";
			EmpExecutionContext.info(opModule, SysuserUtil.getCorpcode(request), SysuserUtil.strLguserid(request), SysuserUtil.getSysUserName(request), opContent, opType);
			response.getWriter().print(result);
		}
	}
	
	/**
	 * 打开编辑网优通道账户
	 * @param request
	 * @param response
	 */
	public void doEdit(HttpServletRequest request, HttpServletResponse response)
	{
		
		try
		{
			//网优通道账户ID
			String ipcomid = request.getParameter("ipcomid");
			List<ASiminfoVo> simvolist = null;
			if (ipcomid != null && !"".equals(ipcomid))
			{
				AIpcominfo aipcominfo =this.baseBiz.getById(AIpcominfo.class, ipcomid);
				GateManageBiz gmbiz=new GateManageBiz();
				//获取通道账户sim卡集合
				simvolist = gmbiz.getSimsBygateId(aipcominfo.getGateid().toString());
				
				request.setAttribute("simvolist", simvolist);
				request.setAttribute("aipcominfo", aipcominfo);
				request.getRequestDispatcher(this.empRoot + base + "/wy_editgateManage.jsp")
				.forward(request, response);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"打开编辑网优通道修改页面异常！");
		}
	}
	
	
	/**
	 * 修改网优通道账户
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response){
		String opModule = "网优通道管理";
		String opContent;
		String opType = "UPDATE";
		String result = null;
		//网优通道账户ID
		String ipcommid = request.getParameter("ipcomid");
		//通道名称
		String agatename = request.getParameter("agatename");
		//IP地址
		String aipadress = request.getParameter("aipadress");
		//内部平台IP地址
		String ainneripadress = request.getParameter("ainneripadress");
		//端口号
		String aportnum = request.getParameter("aportnum");
		//内部平台端口号
		String ainnerportnum = request.getParameter("ainnerportnum");
		//企业签名
		String corpsign = request.getParameter("corpsign");
		//备注
		String acommon = request.getParameter("acommon");
		//sim卡详情
		String simjsonstr=request.getParameter("arrSimDetailVal");
		String opStatus = null;
		String oldAgatename = null;
		String oldAinneripadress = null;
		String oldAinnerportnum = null;
		String oldAipadress = null;
		String oldAportnum = null;
		String oldCorpsign = null;
		String oldSimjsonstr = null;
		String oldAcommon = null;
		try
		{
			//通过ID获取IPComm对象
			AIpcominfo ipcomm=this.baseBiz.getById(AIpcominfo.class, ipcommid);
			oldAgatename = ipcomm.getGatename();
			oldAinneripadress = ipcomm.getPtip();
			oldAinnerportnum = ipcomm.getPtport().toString();
			oldAipadress = ipcomm.getIp();
			oldAportnum = ipcomm.getPort().toString();
			oldCorpsign = ipcomm.getCorpsign();
			oldAcommon = ipcomm.getCommon();
			//通过网关id查询出原来的sim卡信息
			//通道id
			String gateid=ipcomm.getGateid().toString();
			GateManageBiz gmbiz=new GateManageBiz();
			List<ASiminfoVo> simvolist = gmbiz.getSimsBygateId(gateid);
			if (simvolist !=null && simvolist.size()>0)
			{
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < simvolist.size(); i++)
				{
					ASiminfoVo sinInfo = simvolist.get(i);
					sb.append("@").append(i+1).append(",").append(sinInfo.getPhoneno()).append(",").append(sinInfo.getAreaname()).append(",").append(sinInfo.getUnicom()); 
				}
				oldSimjsonstr = sb.toString();
			}
			
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//验证通道是否重复 --通道名称
			conditionMap.put("gatename",agatename);
			//IP地址
			conditionMap.put("ip",aipadress);
			//端口号
			conditionMap.put("port",aportnum);
			//不等于当前通道id
			conditionMap.put("gateid&<>", gateid);
			List<AIpcominfo> aipcomList = baseBiz.getByCondition(
					AIpcominfo.class, conditionMap, null);
			conditionMap.clear();
			//不等于当前通道id
			conditionMap.put("id&<>", gateid);
			//通道名称
			conditionMap.put("gateName", agatename);
			//通道类型
			conditionMap.put("gateType", "1");
			List<XtGateQueue> gatesList = baseBiz.getByCondition(XtGateQueue.class, conditionMap, null);
			
			//检查网优通道名称ip端口有重复或者与通道名称重复
			if (aipcomList != null && aipcomList.size() > 0&&gatesList != null && gatesList.size() > 0) {
				result = "ipcomExists";
				return;
			}
			String phonenos="";
			if(simjsonstr!=null&&!"".equals(simjsonstr)){
				if(simjsonstr.contains("@@")){
					result = "simExists";
					return;
				}
				String[] simrow=simjsonstr.split("@");
				for (String srow : simrow) {
					String[] coln=srow.split(",");
					if(coln.length==4){
						phonenos = phonenos +  coln[1] + ",";
						if("".equals(coln[1])){
							result = "simExists";
							return;
						}else{
							String[] haoduan = new WgMsgConfigBiz().getHaoduan();
							PhoneUtil p=new PhoneUtil();
							if(p.getPhoneType(coln[1], haoduan)==-1){
								result = "simExists";
								return;
							}
						}
					}
				}
				if(!"".equals(phonenos)){
					phonenos=phonenos.substring(0, phonenos.length()-1);
				}else{
					result = "simExists";
					return;
				}
			}else{
				result = "simExists";
				return;
			}
			

			conditionMap.clear();
			conditionMap.put("phoneno&in",phonenos);
			conditionMap.put("gateid&<>", gateid);
			List<ASiminfo> simList = baseBiz.getByCondition(ASiminfo.class, conditionMap,null);
			//检查SIM卡编码重复
			if (simList != null && simList.size() > 0) {
				//拼接重复的号码
				String phones="\n";
				for(ASiminfo sims:simList){
					phones=phones+sims.getPhoneno()+"\n";
				}
				result = "simExists:"+phones;
				return;
			}
			
			//都不重复可以保存入库
			ipcomm.setCommon(acommon);
			ipcomm.setCorpsign(corpsign);
			ipcomm.setGatename(agatename);
			ipcomm.setIp(aipadress);
			ipcomm.setPort(Integer.parseInt(aportnum));
			ipcomm.setPtip(ainneripadress);
			ipcomm.setPtport(Integer.parseInt(ainnerportnum));
			
			gmbiz.updateWyGate(ipcomm, simjsonstr);

			result = "true";
		} catch (Exception e) {
			result=ERROR;
			opStatus = "失败";
		}finally{
			opContent = "修改网优通道"+opStatus+"。（通道名称："+oldAgatename+"，EMP网关IP及端口："+oldAinneripadress+":"+oldAinnerportnum+"，运营商网关IP及端口：："+oldAipadress+":"+oldAportnum+"，短信签名："+oldCorpsign+"，SIM卡信息："+oldSimjsonstr+"，备注："+oldAcommon+")-->（通道名称："+agatename+"，EMP网关IP及端口："+ainneripadress+":"+ainnerportnum+"，运营商网关IP及端口：："+aipadress+":"+aportnum+"，短信签名："+corpsign+"，SIM卡信息："+simjsonstr+"，备注："+acommon+")";
			EmpExecutionContext.info(opModule, SysuserUtil.getCorpcode(request), SysuserUtil.strLguserid(request), SysuserUtil.getSysUserName(request), opContent, opType);
			try {
				response.getWriter().print(result);
			} catch (IOException e1) {
				EmpExecutionContext.error(e1,"修改通道账户异常！");
			}
		}
		
	
	}

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
			EmpExecutionContext.error(e,opModule+opType+opContent+"日志写入异常");
		}
	}
}
