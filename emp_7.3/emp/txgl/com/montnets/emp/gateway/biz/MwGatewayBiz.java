package com.montnets.emp.gateway.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.gateway.AcmdQueue;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.gateway.dao.MwGatewayDAO;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.dao.SuperTxglDAO;
import com.montnets.emp.servmodule.txgl.entity.AgwAccount;
import com.montnets.emp.servmodule.txgl.entity.GWCluSpBind;
import com.montnets.emp.servmodule.txgl.entity.GwGateconninfo;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

/**
 * 通道账户管理biz
 * 
 * @description
 * @project p_txgl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-10-16 下午04:53:59
 */
public class MwGatewayBiz extends SuperBiz
{

	ErrorLoger	errorLoger	= new ErrorLoger();

	/**
	 * 添加通道账户是保存userdata表并返回uid
	 * 
	 * @description
	 * @param userdata
	 * @return uid
	 * @author zhangmin
	 * @datetime 2013-10-16 下午04:59:26
	 */
	public Long addUserdataReturnId(Userdata userdata)
	{
		Long uid = 0L;
		try
		{
			if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				uid = new SuperTxglDAO().saveObjectReturnIDWithTri(userdata);
			}
			else
			{
				uid = empDao.saveObjectReturnID(userdata);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "添加通道账户biz异常！"));
		}
		return uid;
	}

	/**
	 * 修改通道账户
	 * 
	 * @description
	 * @param request
	 * @return
	 * @author zhangmin
	 * @datetime 2013-12-19 下午02:16:05
	 */
	public Boolean editUserdata(HttpServletRequest request)
	{
		Connection conn = empTransDao.getConnection();
		String corpcode = "";
		String opUser = "";
		SuperOpLog spLog = new SuperOpLog();
		// 日志内容
		String opContent = "";
		//修改前日志数据
		StringBuffer oldStr=new StringBuffer("");
		//修改后日志数据
		StringBuffer newStr=new StringBuffer("");
		try
		{
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpcode = lfSysuser.getCorpCode();
			opUser = lfSysuser.getUserName();
			String uid;
			String keyId = request.getParameter("keyId");
			// 加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
			// 加密对象不为空
			if(encryptOrDecrypt != null)
			{
				// 解密
				uid = encryptOrDecrypt.decrypt(keyId);
				if(uid == null)
				{
					EmpExecutionContext.error("修改通道账户，参数解密码失败，keyId:" + keyId + "，企业:" + corpcode + ",操作员:" + opUser + "，spUser:" + request.getParameter("userid"));
					throw new Exception("修改通道账户，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("修改通道账户，从session中获取加密对象为空！企业:" + corpcode + ",操作员:" + opUser + "，spUser:" + request.getParameter("userid"));
				throw new Exception("修改通道账户，获取加密对象失败。");
			}

			empTransDao.beginTransaction(conn);
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.clear();
			conditionMap.put("ptAccUid", uid);
			List<AgwAccount> agwAccountList = empDao.findListByCondition(AgwAccount.class, conditionMap, null);
			boolean result=false;
			if(agwAccountList != null && agwAccountList.size() > 0)
			{
				objectMap.clear();
				objectMap.put("spPort", request.getParameter("spPort"));
				objectMap.put("spIp", request.getParameter("spIp"));
				//修改前就数据日志字符串
				AgwAccount oldAcc=agwAccountList.get(0);
				if(oldAcc!=null){
					//，EMP网关IP地址，EMP网关IP地址端口，运营商账户ID，运营商账户密码，运营商IP地址，运营商IP端口
					//，业务类型，SP企业代码，计费用户类型，技术合作商，通讯协议，通讯协议参数，账户计费类型，余额查看URL
					oldStr.append(oldAcc.getSpIp()).append("，").append(oldAcc.getSpPort());
				}
				result = empTransDao.update(conn,AgwAccount.class, objectMap, conditionMap);
				//保存日志
				opContent="mwadmin修改通道账户[运营商IP地址，运营商IP端口]";
				newStr.append(objectMap.get("spIp")).append("，").append(objectMap.get("spPort"));
				opContent = opContent + "（" + oldStr + "-->" + newStr + "）";
				String sucname="";
				if(result){
					sucname="成功";
				}else{
					sucname="失败";
				}
				setLog(request, "通道账户管理", opContent+sucname, StaticValue.UPDATE);
			}
			if(result){
				// 含有主备信息的ip端口字符串
				String zbIP = request.getParameter("zbIP");
				if(zbIP != null && !"".equals(zbIP))
				{
					if("1".equals(request.getParameter("accountType")))
					{
						/*
						 * 将运营商ip端口 存于连路表
						 */
						String resultstr = this.editGWGateconninfo(zbIP, request.getParameter("userid"),uid);
						if(!"true".equals(resultstr))
						{
							opContent = "修改通道账户（通道账户账号：" + request.getParameter("userid") + "，运营商账户ID：" + request.getParameter("SPACCID") + "）运营商IP及端口传入的参数处理异常 zbIP:" + zbIP;
							EmpExecutionContext.error("企业：" + corpcode + ",操作员：" + opUser + opContent);
							return false;
						}
					}
				}
				else
				{
					// 保存日志
					opContent = "修改通道账户（通道账户账号：" + request.getParameter("userid") + "，运营商账户ID：" + request.getParameter("SPACCID") + "）运营商IP及端口未填";
					EmpExecutionContext.error("企业：" + corpcode + ",操作员：" + opUser + opContent);
					return false;
				}
				opContent = "MWADMIN修改通道账户（通道账户账号：" + request.getParameter("userid") + "，运营商账户ID：" + request.getParameter("SPACCID") + ",zbIP:" + zbIP+")成功";
				setLog(request, "通道账户管理", opContent , StaticValue.UPDATE);
				spLog.logSuccessString(opUser, "网关配置", StaticValue.UPDATE, opContent, corpcode);
			}
				
			empTransDao.commitTransaction(conn);
			return true;

		}
		catch (Exception e)
		{
			// 保存日志
			opContent = "修改通道账户（通道账户账号：" + request.getParameter("userid") + "）";
			spLog.logFailureString(opUser, "网关配置", StaticValue.UPDATE, opContent, e, corpcode);
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "企业：" + corpcode + ",操作员：" + opUser + "，通道账户管理修改通道账户biz层异常！"));
			return false;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 修改通道账户高级配置
	 * 
	 * @description
	 * @param request
	 * @return
	 * @author zhangmin
	 * @datetime 2013-12-19 下午02:16:05
	 */
	public Boolean editAdvanceConf(HttpServletRequest request)
	{
		boolean result = false;
		String corpcode = "";
		String opUser = "";
		SuperOpLog spLog = new SuperOpLog();
		// 日志内容
		String opContent = "";
		try
		{
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpcode = lfSysuser.getCorpCode();
			opUser = lfSysuser.getUserName();
			String uid;
			String keyId = request.getParameter("keyId");
			// 加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
			// 加密对象不为空
			if(encryptOrDecrypt != null)
			{
				// 解密
				uid = encryptOrDecrypt.decrypt(keyId);
				if(uid == null)
				{
					EmpExecutionContext.error("mwadmin修改通道账户高级配置，参数解密码失败，keyId:" + keyId+"，spUser:" + request.getParameter("userid"));
					throw new Exception("mwadmin修改通道账户高级配置，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("mwadmin修改通道账户高级配置，从session中获取加密对象为空！，spUser:" + request.getParameter("userid"));
				throw new Exception("mwadmin修改通道账户高级配置，获取加密对象失败。");
			}

			if("1".equals(request.getParameter("accouttype")))
			{
				String userid=request.getParameter("userid");
				LinkedHashMap<String, String> setmap = new LinkedHashMap<String, String>();
				String keepconn=request.getParameter("keepconn");
				String linkcnt=request.getParameter("linkcnt");
				if(keepconn != null && keepconn.length() > 0)
				{
					setmap.put("keepconn", keepconn);
				}
				if(linkcnt != null && linkcnt.length() > 0)
				{
					setmap.put("linkcnt", linkcnt);
				}
				if("0".equals(keepconn)){
					String switchmainip=request.getParameter("switchmainip");
					if(switchmainip != null && switchmainip.length() > 0)
					{
						setmap.put("switchmainip", switchmainip);
					}
					String reconncnt=request.getParameter("reconncnt");
					if(reconncnt!=null&&reconncnt.length()>0){
						setmap.put("reconncnt", reconncnt);
					}
					String relogincnt=request.getParameter("relogincnt");
					if(relogincnt!=null&&relogincnt.length()>0){
						setmap.put("relogincnt", relogincnt);
					}
				}
				LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
				conditionmap.put("ptaccid", userid);
				result=this.empDao.update(GwGateconninfo.class, setmap, conditionmap);
				if(!result)
				{
					opContent = "mwadmin修改通道账户高级配置（通道账户账号：" + request.getParameter("userid");
					EmpExecutionContext.error("企业：" + corpcode + ",操作员：" + opUser + opContent);
					return false;
				}
				
				conditionmap.clear();
				conditionmap.put("ptaccuid", uid);
				List<GWCluSpBind> spbs=this.empDao.findListByCondition(GWCluSpBind.class, conditionmap, null);
				if(spbs!=null&&spbs.size()>0){
					List<AcmdQueue> cmdq=new ArrayList<AcmdQueue>();
					for(GWCluSpBind gcs:spbs){
						AcmdQueue aq=new AcmdQueue();
						aq.setGwNo(gcs.getGwno());
						aq.setGwType(3000);
						aq.setCmdType(3000);
						cmdq.add(aq);
					}
					if(cmdq != null && cmdq.size() > 0)
					{
						this.empDao.save(cmdq, AcmdQueue.class);
					}
				}
				
			}

			setLog(request, "通道账户管理", opContent , StaticValue.UPDATE);
			spLog.logSuccessString(opUser, "网关配置", StaticValue.UPDATE, opContent, corpcode);
			return result;
		}
		catch (Exception e)
		{
			// 保存日志
			opContent = "mwadmin修改通道账户高级配置（通道账户账号：" + request.getParameter("userid") + "）";
			spLog.logFailureString(opUser, "网关配置", StaticValue.UPDATE, opContent, e, corpcode);
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "mwadmin修改通道账户高级配置修改通道账户biz层异常！"));
			return false;
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
	public void setLog(HttpServletRequest request, String opModule, String opContent, String opType)
	{
		try
		{
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj != null)
			{
				LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), opContent, opType);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, opModule + opType + opContent + "日志写入异常"));
		}
	}

	/**
	 * 后端账号查询
	 * 
	 * @param conditionMap
	 * @param pageinfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAccountList(LinkedHashMap<String, String> conditionMap, PageInfo pageinfo) throws Exception
	{

		return new MwGatewayDAO().getAccountList(conditionMap, pageinfo);
	}

	/**
	 * 设置通讯协议参数
	 * 
	 * @description
	 * @param protocolParam
	 *        通讯协议参数
	 * @param byIp
	 *        运营商IP地址及端口
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-17 下午04:03:37
	 */
	public String setProtocolParam(String protocolParam, String byIp)
	{
		try
		{
			if(protocolParam == null || protocolParam.length() == 0)
			{
				protocolParam = " ";
			}
			String[] proArr = protocolParam.split(";");
			String proStr = "";
			for (int i = 0; i < proArr.length; i++)
			{
				if(proArr[i].indexOf("backupip=") == -1)
				{
					proStr = proStr + proArr[i] + ";";
				}
			}
			if(byIp != null && byIp.length() > 0)
			{
				protocolParam = proStr + "backupip=" + byIp.substring(0, byIp.lastIndexOf(","));
			}
			else if(proStr.length() > 0)
			{
				protocolParam = proStr.substring(0, proStr.length() - 1);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置通讯协议参数异常!");
			return null;
		}
		return protocolParam;
	}

	/**
	 * 拼接主备节点信息
	 * 
	 * @param ptIp
	 *        主节点ip
	 * @param ptPort
	 *        主节点端口
	 * @param clusterAddrs
	 * @return
	 */
	public String getPtNode(String ptIp, String ptPort, String[] clusterAddrs)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("{0|0").append(":" + ptIp).append(":" + ptPort + "}");
		if(clusterAddrs != null)
		{
			for (int i = 0; i < clusterAddrs.length; i++)
			{
				sb.append(",");
				sb.append("{").append(i + 1).append("|0").append(":").append(clusterAddrs[i]).append("}");
			}
		}
		return "node=" + sb.toString();
	}

	/**
	 * 添加主备链路 信息
	 * 
	 * @description
	 * @param zbIP
	 * @param spType
	 * @param userid
	 * @return
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2017-4-19 下午05:39:44
	 */
	public String addGWGateconninfo(String zbIP, String spType, String userid)
	{
		try
		{
			List<GwGateconninfo> gwgcinfolist = new ArrayList<GwGateconninfo>();
			String[] zbIps = zbIP.split(",");
			for (int i = 0; i < zbIps.length; i++)
			{
				String byipstr = zbIps[i];
				if(byipstr != null && byipstr.length() > 0 && byipstr.contains(":"))
				{
					String[] coninfos = byipstr.split(":");
					if(coninfos.length == 3)
					{
						GwGateconninfo gwgcinfo = new GwGateconninfo();
						// 关联sp账号
						gwgcinfo.setPtaccid(userid);
						// 主用/备用
						gwgcinfo.setLinklevel(Integer.parseInt(coninfos[0]));
						gwgcinfo.setIp(coninfos[1]);
						gwgcinfo.setPort(Integer.parseInt(coninfos[2]));
						if("0".equals(spType))
						{
							// 主备多链路同时连接
							gwgcinfo.setConntype(1);
							gwgcinfo.setKeepconn(1);
						}
						else
						{
							// 单链路连接
							gwgcinfo.setConntype(0);
							gwgcinfo.setKeepconn(0);
						}
						gwgcinfolist.add(gwgcinfo);
					}
				}
			}
			if(gwgcinfolist != null && gwgcinfolist.size() > 0)
			{
				this.empDao.save(gwgcinfolist, GwGateconninfo.class);
			}
			return "true";
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "新增主备链路ip端口失败");
			return "nobyip";
		}
	}

	/**
	 * 修改主备链路 信息
	 * 
	 * @description
	 * @param zbIp
	 * @param spType
	 * @param userid
	 * @return
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2017-4-19 下午05:39:44
	 */
	public String editGWGateconninfo(String zbIp, String userid,String uid)
	{
		Connection connection = null;
		try
		{
			List<GwGateconninfo> gwgcinfolist = new ArrayList<GwGateconninfo>();
			LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
			conditionmap.put("ptaccid", userid);
			List<GwGateconninfo> gwgates = this.empDao.findListByCondition(GwGateconninfo.class, conditionmap, null);
			// 模板配置
			GwGateconninfo mbgate = null;
			if(gwgates != null && gwgates.size() > 0)
			{
				mbgate = gwgates.get(0);
			}
			String[] zbips = zbIp.split(",");
			for (int i = 0; i < zbips.length; i++)
			{
				String byipstr = zbips[i];
				if(byipstr != null && byipstr.length() > 0 && byipstr.contains(":"))
				{
					String[] coninfos = byipstr.split(":");
					if(coninfos.length == 4)
					{
						GwGateconninfo gwgcinfo = new GwGateconninfo();
						if(mbgate != null)
						{
							// 拷贝原有数据属性
							BeanUtils.copyProperties(gwgcinfo, mbgate);
						}
						// 关联sp账号
						gwgcinfo.setPtaccid(userid);
						// 主用/备用
						gwgcinfo.setLinklevel(Integer.parseInt(coninfos[0]));
						gwgcinfo.setIp(coninfos[1]);
						gwgcinfo.setPort(Integer.parseInt(coninfos[2]));
						//0 启用 2停用
						gwgcinfo.setLinkstatus(Integer.parseInt(coninfos[3]));
						gwgcinfolist.add(gwgcinfo);
					}
				}
			}
			connection = empTransDao.getConnection();
			empTransDao.beginTransaction(connection);
			empTransDao.delete(connection, GwGateconninfo.class, conditionmap);
			if(gwgcinfolist != null && gwgcinfolist.size() > 0)
			{
				this.empTransDao.save(connection, gwgcinfolist, GwGateconninfo.class);
			}
			
			conditionmap.clear();
			conditionmap.put("ptaccuid", uid);
			List<GWCluSpBind> spbs=this.empDao.findListByCondition(GWCluSpBind.class, conditionmap, null);
			if(spbs!=null&&spbs.size()>0){
				List<AcmdQueue> cmdq=new ArrayList<AcmdQueue>();
				for(GWCluSpBind gcs:spbs){
					AcmdQueue aq=new AcmdQueue();
					aq.setGwNo(gcs.getGwno());
					aq.setGwType(3000);
					aq.setCmdType(3000);
					cmdq.add(aq);
				}
				if(cmdq != null && cmdq.size() > 0)
				{
					this.empTransDao.save(connection, cmdq, AcmdQueue.class);
				}
			}
			
			empTransDao.commitTransaction(connection);

			return "true";
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "修改主备链路ip端口失败,zbIp:" + zbIp);
			return "nobyip";
		}
		finally
		{
			if(connection != null)
			{
				empTransDao.closeConnection(connection);
			}
		}
	}
}
