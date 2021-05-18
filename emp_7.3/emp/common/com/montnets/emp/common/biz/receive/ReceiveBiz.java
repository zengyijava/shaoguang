package com.montnets.emp.common.biz.receive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.CommDataDealDAO;
import com.montnets.emp.entity.pasgroup.AcmdRoute;
import com.montnets.emp.entity.pasgrpbind.LfAccountBind;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.system.LfBusProcess;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.entity.system.LfReport;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.util.ChangeCharset;

/**
 * 
 * @author Administrator
 * 
 */
public class ReceiveBiz extends SuperBiz
{

	public ReceiveBiz()
	{
		// 获取所有注册记录
		initBusProcess();
	}

	//注册类集合
	private List<LfBusProcess> busProsList = null;
	
	CommDataDealDAO commDataDao = new CommDataDealDAO();

	/**
	 * 获取所有注册记录
	 */
	private void initBusProcess()
	{

		try
		{
			//获取所有注册记录
			busProsList = empDao.findListByCondition(LfBusProcess.class, null,
					null);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取所有注册记录异常。");
		}
	}

	/**
	 * 根据条件，获取注册记录
	 * 
	 * @param regType
	 *            注册类型
	 * @param codes
	 *            编码
	 * @param codeType
	 *            编码类型
	 * @return 返回获取到的注册记录
	 */
	private LfBusProcess getBusProcess(Integer regType, String codes,
			Integer codeType)
	{
		//注册类对象
		LfBusProcess bus = null;
		try
		{
			//注册类集合为空
			if (busProsList == null || busProsList.size() == 0)
			{
				// 获取所有注册记录
				initBusProcess();
			}
			//循环读取注册类集合并处理
			for (int i = 0; i < busProsList.size(); i++)
			{
				if (busProsList.get(i).getRegType().equals(regType)
						&& busProsList.get(i).getCodes().equals(codes)
						&& busProsList.get(i).getCodeType().equals(codeType))
				{
					//获取对应的注册类对象
					bus = busProsList.get(i);
					//跳出循环
					break;
				}
			}

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "根据条件，获取注册记录异常。");
		}
		//返回注册类对象
		return bus;
	}

	/**
	 * 保存上行信息
	 * @param moTask
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean addMotask(LfMotask moTask) throws Exception
	{
		//上行对象不能为空
		if (moTask == null)
		{
			return false;
		}
		//结果
		boolean result = false;
		try
		{
			//保存记录到数据库
			result = empDao.save(moTask);
			//根据全通道号获取尾号对象
			LfSubnoAllotDetail subnoDetail = this.getUpMsgSubnoDetail(moTask
					.getSpnumber());
			//尾号对象不能为空
			if (subnoDetail == null)
			{
				EmpExecutionContext.info("子号详细为空！");
				return false;
			}
			//根据尾号的编码获取对应的注册类对象
			LfBusProcess bus = this.getBusProcess(0, subnoDetail.getCodes(),
					subnoDetail.getCodeType());
			//获取不到注册类
			if (bus == null || bus.getClassName() == null)
			{
				EmpExecutionContext.debug("模块编码“" + subnoDetail.getMenuCode()
						+ "”无法找到对应的类名！");
				return false;
			}
			//反射初始化注册类的类
			Class<IBusinessStart> c = (Class<IBusinessStart>) Class.forName(bus.getClassName());
			IBusinessStart business = c.newInstance();
			//推送上行到对应的注册类
			business.start(moTask);
			result = true;

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "保存上行信息异常。");
			throw e;
		} finally
		{
		}
		//返回结果
		return result;
	}

	/**
	 * 保存状态报告
	 * @param report
	 * @return
	 * @throws Exception
	 */
	public boolean addReport(LfReport report)
	{
		//状态报告对象不能为空
		if (report == null)
		{
			EmpExecutionContext.debug("状态报告为空，推送失败！");
			return false;
		}
		EmpExecutionContext.debug("进入状态报告推送：mtmsgid:"+report.getMtmsgid());

		try
		{
			//未带模块编码，则不需推送
			if(report.getModuleId() == null)
			{
				EmpExecutionContext.info("rpt推送，未带模块id，不需要推送。" 
						+ "mtmsgid=" + report.getMtmsgid()
						+ ",mtstat=" + report.getMtstat()
						+ ",mterrorcode=" + report.getMterrorcode()
						+ ",spid=" + report.getSpid()
						+ ",phone=" + report.getPhone()
						+ ",msgid=" + report.getTaskId()
						+ ",moduleid=" + report.getModuleId()
				);
				return false;
			}
			
			//根据模块编码获取注册类
			LfBusProcess bus = this.getBusProcess(1, report.getModuleId().toString(), 0);
			//没找到对应的注册类
			if (bus == null)
			{
				EmpExecutionContext.error("rpt推送，根据模块id获取业务注册信息为空。" 
						+ "要推送不同的模块需要取出模块的配置信息，包括处理类路径等注册信息。"
						+ "模块id关系：10030-完美通知；10920-监控短信；12002-登录验证码短信。"
						+ "mtmsgid=" + report.getMtmsgid()
						+ ",mtstat=" + report.getMtstat()
						+ ",mterrorcode=" + report.getMterrorcode()
						+ ",spid=" + report.getSpid()
						+ ",phone=" + report.getPhone()
						+ ",msgid=" + report.getTaskId()
						+ ",moduleid=" + report.getModuleId()
				);
				return false;
			}
			
			if ((bus.getSendType()==null || bus.getSendType()-1==0) && bus.getClassName() == null) 
			{
				EmpExecutionContext.error("rpt推送，业务注册信息不正确，类路径为空。"
						+ "mtmsgid=" + report.getMtmsgid()
						+ ",mtstat=" + report.getMtstat()
						+ ",mterrorcode=" + report.getMterrorcode()
						+ ",spid=" + report.getSpid()
						+ ",phone=" + report.getPhone()
						+ ",msgid=" + report.getTaskId()
						+ ",moduleid=" + report.getModuleId()
						+ ",busSendType=" + bus.getSendType()
						+ ",busClassName=" + bus.getClassName()
				);
				return false;
			}
			else if (bus.getSendType()-2==0 && bus.getHttpUrl() == null) 
			{
				EmpExecutionContext.error("rpt推送，业务注册信息不正确，http地址为空。"
						+ "mtmsgid=" + report.getMtmsgid()
						+ ",mtstat=" + report.getMtstat()
						+ ",mterrorcode=" + report.getMterrorcode()
						+ ",spid=" + report.getSpid()
						+ ",phone=" + report.getPhone()
						+ ",msgid=" + report.getTaskId()
						+ ",moduleid=" + report.getModuleId()
						+ ",busSendType=" + bus.getSendType()
						+ ",busHttpUrl=" + bus.getHttpUrl()
				);
				return false;
			}

			if(bus.getSendType()==null || bus.getSendType()==0 || bus.getSendType()-1==0)
			{
				EmpExecutionContext.info("rpt推送，推送对应业务类。"
						+ "mtmsgid=" + report.getMtmsgid()
						+ ",mtstat=" + report.getMtstat()
						+ ",mterrorcode=" + report.getMterrorcode()
						+ ",spid=" + report.getSpid()
						+ ",phone=" + report.getPhone()
						+ ",msgid=" + report.getTaskId()
						+ ",moduleid=" + report.getModuleId()
						+ ",busSendType=" + bus.getSendType()
						+ ",busClassName=" + bus.getClassName()
				);
				Class<IReportStart> c = (Class<IReportStart>) Class.forName(bus.getClassName());
				IReportStart repStart = c.newInstance();
				boolean result = repStart.start(report);
				return result;
			}
			if (bus.getSendType()-2==0) 
			{
				//使用http方式把状态报告推送到业务配置的url
				EmpExecutionContext.info("rpt推送，推送对应业务Url。"
						+ "mtmsgid=" + report.getMtmsgid()
						+ ",mtstat=" + report.getMtstat()
						+ ",mterrorcode=" + report.getMterrorcode()
						+ ",spid=" + report.getSpid()
						+ ",phone=" + report.getPhone()
						+ ",msgid=" + report.getTaskId()
						+ ",moduleid=" + report.getModuleId()
						+ ",busSendType=" + bus.getSendType()
						+ ",busHttpUrl=" + bus.getHttpUrl()
				);
				String http = bus.getHttpUrl();
				StringBuffer paramEntity =new StringBuffer("command=RT_REQUEST")
				.append("&spid=").append(report.getSpid())
				.append("&mtmsgid=").append(report.getMtmsgid())
				.append("&mtstat=").append(report.getMterrorcode())
				.append("&sa=").append(report.getPhone())
				.append("&msgid=").append(report.getTaskId())
				.append("&mtstat=").append(report.getMtstat());
				
				EmpExecutionContext.debug("请求url:"+http+"------");
				EmpExecutionContext.debug("请求参数:"+paramEntity.toString()+"------");
				//发送请求
				String responseStr = requestHttp("POST",http,paramEntity.toString());
				if(responseStr == null || responseStr.trim().length() < 1)
				{
					EmpExecutionContext.error("rpt推送，推送对应业务Url，获取响应结果为空。"
							+ "mtmsgid=" + report.getMtmsgid()
							+ ",mtstat=" + report.getMtstat()
							+ ",mterrorcode=" + report.getMterrorcode()
							+ ",spid=" + report.getSpid()
							+ ",phone=" + report.getPhone()
							+ ",msgid=" + report.getTaskId()
							+ ",moduleid=" + report.getModuleId()
							+ ",busSendType=" + bus.getSendType()
							+ ",busHttpUrl=" + bus.getHttpUrl()
					);
					return false;
				}
				//处理响应
				int a=responseStr.indexOf("rtstat=");
				int b=responseStr.lastIndexOf("&");
				
				String stat = responseStr.substring(a+7,b);
				String errorcode = responseStr.substring(responseStr.lastIndexOf("=")+1);
				if(stat.trim().equals("ACCEPTD") && errorcode.trim().equals("000"))
				{
					//成功接收上行请求
					EmpExecutionContext.debug("成功接收状态报告");
					return true;
				}
				else 
				{
					EmpExecutionContext.debug("失败接收状态报告");
					return false;
				}
			}
			return false;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "rpt推送，异常。"
					+ "mtmsgid=" + report.getMtmsgid()
					+ ",mtstat=" + report.getMtstat()
					+ ",mterrorcode=" + report.getMterrorcode()
					+ ",spid=" + report.getSpid()
					+ ",phone=" + report.getPhone()
					+ ",msgid=" + report.getTaskId()
					+ ",moduleid=" + report.getModuleId()		
			);
			return false;
		}
	}
	
	/**
	 * 推送执行上行推送任务
	 * 2012-8-2
	 * @param moTask 上行推送任务
	 * @return 推送执行结果
	 * @throws Exception
	 * boolean
	 */
	public boolean addMotask1(LfMotask moTask) throws Exception
	{
		//上行对象不能为空
		if (moTask == null)
		{
			EmpExecutionContext.error("上行信息推送失败，上行对象为空");
			return false;
		}
		
		EmpExecutionContext.debug("上行信息ptmsgid:"+moTask.getPtMsgId()+"，开始处理");
		
		//条件
		try
		{
			//解码短信内容
			String msgContent = toStringHex(moTask.getMsgContent());
			if(msgContent == null)
			{
				moTask.setMsgContent("");
			}
			else
			{
				moTask.setMsgContent(msgContent);
			}
			
			boolean sendRes = false;
			boolean saveMoresult = false;
			//根据尾号推送
			if (moTask.getSubno() != null 
					&& moTask.getSubno().trim().length() > 0 
					&& !"*".equals(moTask.getSubno().trim()) 
					&& !"null".equals(moTask.getSubno().trim()) 
			)
			{
				sendRes =  processSubnoMo(moTask);
				//入库
				saveMoresult = addMoTask(moTask);
			}
			//根据指令推送
			else if(moTask.getMoOrder() != null 
					&& moTask.getMoOrder().trim().length() > 0
					&& !"0".equals(moTask.getMoOrder()) 
			)
			{
				sendRes =  this.sendOrder(moTask);
				//入库
				saveMoresult = addMoTask(moTask);
			}
			//在上行信息没带尾号的情况下，通过配对账号绑定的通道找尾号，如果有，则按尾号推送。避免网关没推送尾号来导致找不到
			else if(!"*".equals(moTask.getSubno()) && hasSubno(moTask))
			{
				sendRes =  processSubnoMo(moTask);
				//入库
				saveMoresult = addMoTask(moTask);
			}
			//无尾号无指令处理
			else
			{
				sendRes =  this.processNoSubnoMo(moTask);
				//无内容则不存库
				if(msgContent != null)
				{
					saveMoresult = addMoTask(moTask);
				}
				else
				{
					saveMoresult = true;
				}
			}
			
			//打印记录
			if(!saveMoresult)
			{
				EmpExecutionContext.error("上行推送，插入LF_MOTASK表失败。"
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",menuCode="+moTask.getMenuCode()	
						+ ",depId="+moTask.getDepId()
						+ ",taskId="+moTask.getTaskId()	
						+ ",busCode="+moTask.getBusCode()
						+ ",corpCode="+moTask.getCorpCode()
				);
			}
			
			return sendRes;
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "推送执行上行推送任务异常。"
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()	
					+ ",menuCode="+moTask.getMenuCode()	
					+ ",depId="+moTask.getDepId()
					+ ",taskId="+moTask.getTaskId()	
					+ ",busCode="+moTask.getBusCode()
					+ ",corpCode="+moTask.getCorpCode()
			);
			return false;
		}
	}
	
	/**
	 * 
	 * @description 异步入库
	 * @param moTask 上行信息对象
	 * @return 成功返回true
	 */
	private boolean addMoTask(LfMotask moTask)
	{
		MoDataStorage dataStorage = MoDataStorage.getInstance();
		return dataStorage.produce(moTask);
	}
	
	/**
	 * 将转码后的字符创还原为原先的字符创
	 * @param str
	 * @return 返回解码后的字符串，异常返回null
	 */
	private String toStringHex(String str)
	{
		try
		{
			if("0x".equals(str.substring(0, 2)))
			{
				str = str.substring(2);
			}
			byte[] baKeyword = new byte[str.length() / 2];
			for (int i = 0; i < baKeyword.length; i++)
			{
				baKeyword[i] = (byte) (0xff & Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16));
			}

			str = new String(baKeyword, "gbk");//
			return str;
		}
		catch (Exception e)
		{
			//这里不打堆栈，太多了
			EmpExecutionContext.error("字符串转码异常。string=" + str);
			return null;
		}

	}
	
	/**
	 * 通过配对账号绑定的通道找尾号，如果有，则按尾号推送
	 * @param moTask
	 * @return
	 */
	private boolean hasSubno(LfMotask moTask){
		try
		{
			if(moTask.getSpUser() == null || moTask.getSpUser().length() < 1)
			{
				EmpExecutionContext.error("上行信息未带尾号时自动获取尾号，发送账号为空。" 
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()
				);
				return false;
			}
			else if(moTask.getSpnumber() == null || moTask.getSpnumber().length() < 1)
			{
				EmpExecutionContext.error("上行信息未带尾号时自动获取尾号，通道号为空。"
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()
				);
				return false;
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", moTask.getSpUser());
			List<GtPortUsed> portsList = empDao.findListByCondition(GtPortUsed.class, conditionMap, null);
			if(portsList == null || portsList.size() == 0)
			{
				EmpExecutionContext.error("上行信息未带尾号时自动获取尾号，找不到账号通道配置。" 
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()
				);
				return false;
			}
			for(GtPortUsed port : portsList)
			{
				//账号绑定的主通道+子号
				String portSpnumber = port.getSpgate().trim() + (port.getCpno()==null?"":port.getCpno().trim());
				//上行信息通道长度和账号绑定通道+子号一样长或少，则没尾号
				if(moTask.getSpnumber().length() <= portSpnumber.length())
				{
					continue;
				}
				//上行信息的通道号，等于账号绑定的主通道+子号
				String moSpgate = moTask.getSpnumber().substring(0, portSpnumber.length());
				//上行信息的尾号
				String moSubno = moTask.getSpnumber().substring(portSpnumber.length());
				//有尾号，且主号相同
				if(moSubno != null && moSubno.length() > 0 && portSpnumber.equals(moSpgate))
				{
					moTask.setSubno(moSubno);
					return true;
				}
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行信息未带尾号时自动获取尾号，异常。"
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()		
			);
			return false;
		}
	}

	/**
	 * 上行信息未带尾号处理
	 * @param moTask 上行信息对象
	 * @return 处理成功返回true
	 */
	private boolean processNoSubnoMo(LfMotask moTask)
	{
		try
		{
			if(moTask.getMsgContent().length() < 1)
			{
				EmpExecutionContext.info("上行无需推送处理，信息无内容，不处理。"
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()		
				);
				return true;
			}
			EmpExecutionContext.info("上行无需推送处理。"
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()		
			);
			
			if(moTask.getSpUser() == null || moTask.getSpUser().trim().length() < 1)
			{
				EmpExecutionContext.error("上行无需推送处理，发送账号为空。"
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()		
				);
				return false;
			}
			
			//根据发送账号到操作员账号绑定表中查询，看账号是否被一个操作员绑定
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//绑定类型为操作员
			conditionMap.put("bindType", "1");
			conditionMap.put("spuserId", moTask.getSpUser());
			List<LfAccountBind> binds = empDao.findListByCondition(LfAccountBind.class, conditionMap, null);
			//只被一个操作员绑定
			if(binds != null && binds.size() == 1)
			{
				//设置上行信息的所有者为绑定该发送账号的操作员
				moTask.setUserGuid(binds.get(0).getSysGuid());
				moTask.setCorpCode(binds.get(0).getCorpCode());
				return true;//empDao.save(moTask);
			}
			else if(binds != null && binds.size() > 1)
			{
				//被多个操作员绑定
				moTask.setCorpCode(binds.get(0).getCorpCode());
				return true;//empDao.save(moTask);
			}
			else
			{
				//判断该账户是否只被一个机构绑定
				conditionMap.clear();
				conditionMap.put("bindType", "0");//机构
				conditionMap.put("spuserId", moTask.getSpUser());
				//mowaita里面没有corpcode这个字段
				//conditionMap.put("corpCode", moTask.getCorpCode());
				List<LfAccountBind> depBinds = empDao.findListByCondition(LfAccountBind.class, conditionMap, null);
				if(depBinds != null && depBinds.size() == 1)
				{
					//该发送账号只被一个机构绑定
					//LfAccountBind表中depCode字段存储的是string类型的depId
					moTask.setDepId(Long.valueOf(depBinds.get(0).getDepCode()));
					moTask.setCorpCode(depBinds.get(0).getCorpCode());
					return true;
				}
				else
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行无需推送处理，异常。"
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()		
			);
			return false;
		}
	}
	
	/**
	 * 上行尾号推送处理
	 * @param moTask 上行信息对象
	 * @return 成功返回true
	 */
	private boolean processSubnoMo(LfMotask moTask){

		try
		{
			EmpExecutionContext.info("上行尾号推送处理。"
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()		
			);
			if(moTask.getSpUser() == null || moTask.getSpUser().trim().length() < 1)
			{
				EmpExecutionContext.info("上行尾号推送处理，发送账号为空。"
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
				);
				return false;
			}
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//企业发送账号不可能相同
			conditionMap.put("spUser", moTask.getSpUser());
			List<LfSpDepBind> bindsList = empDao.findListByCondition(LfSpDepBind.class, conditionMap, null);
			if(bindsList == null || bindsList.size() < 1)
			{
				EmpExecutionContext.info("上行尾号推送处理，找不到发送账号绑定。"
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
				);
				return false;
			}
			LfSpDepBind bind = bindsList.get(0);
			
			conditionMap.clear();
			conditionMap.put("corpCode", bind.getCorpCode());
			conditionMap.put("usedExtendSubno", moTask.getSubno());
			List<LfSubnoAllotDetail> details = empDao.findListByCondition(LfSubnoAllotDetail.class, conditionMap, null);
			if(details == null || details.size() < 1)
			{
				EmpExecutionContext.error("上行尾号推送处理，在尾号详细表未找到该尾号的详细记录。" 
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",corpCode="+bind.getCorpCode()	
				);
				return false;
			}
			
			EmpExecutionContext.debug("上行推送，ptmsgid:" + moTask.getPtMsgId() + "，查询到尾号记录，进入处理");
			//根据尾号绑定信息设置上行记录所有者，并保存上行信息
			LfSubnoAllotDetail detail = details.get(0);
			if(detail.getLoginId() != null && !"".equals(detail.getLoginId()))
			{
				moTask.setUserGuid(Long.valueOf(detail.getLoginId()));
			}
			if(detail.getBusCode() != null && !"".equals(detail.getBusCode()))
			{
				moTask.setBusCode(detail.getBusCode());
			}
			if(detail.getDepId() != null && detail.getDepId() != 0)
			{
				moTask.setDepId(detail.getDepId());
			}
			if(detail.getMenuCode() != null && !"".equals(detail.getMenuCode()))
			{
				moTask.setMenuCode(detail.getMenuCode());
			}
			if(detail.getTaskId() != null && detail.getTaskId() != 0)
			{
				moTask.setTaskId(detail.getTaskId());
			}
			moTask.setCorpCode(detail.getCorpCode());
			//empDao.save(moTask);

			//判断尾号是否与发送任务绑定
			if(detail.getTaskId() != null && detail.getTaskId() != 0)
			{
				EmpExecutionContext.info("上行尾号推送处理，尾号与发送任务绑定。" 
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",TaskId="+detail.getTaskId()
				);
				return true;
			}
			
			//未带模块编码，则不需推送
			if(detail.getMenuCode() == null || detail.getMenuCode().length() < 1)
			{
				EmpExecutionContext.info("上行尾号推送处理，未带模块编码，不需要推送。" 
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",MenuCode="+detail.getMenuCode()
				);
				return true;
			}
			//根据尾号绑定记录中的编码到Lf_bus_process表获取对应的注册业务类信息
			conditionMap.clear();
			//模块编码
			conditionMap.put("codes", detail.getMenuCode());
			//注册类型0上行;1报告
			conditionMap.put("regType", "0");
			List<LfBusProcess> processes = empDao.findListByCondition(LfBusProcess.class, conditionMap, null);
			if(processes == null || processes.size() == 0)
			{
				EmpExecutionContext.info("上行尾号推送处理，根据模块编码未找到注册业务信息。" 
						+ "要推送不同的模块需要取出模块的配置信息，包括处理类路径等注册信息。"
						+ "模块id关系：10410-上行业务；10680-上行审批；10910-网讯上行审批；10030-完美通知。"
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",MenuCode="+detail.getMenuCode()
				);
				return true;
			}
			LfBusProcess busProcess = processes.get(0);
			//判断业务属于emp内置模块业务
			if(busProcess.getSendType() == 0 || busProcess.getSendType() == null || busProcess.getSendType() - 1 == 0)
			{
				EmpExecutionContext.info("上行尾号推送处理，推送对应业务。" 
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",MenuCode="+detail.getMenuCode()
						+ ",ClassName="+busProcess.getClassName()
				);
				//根据业务类信息反射初始化业务类并调用业务方法，返回状态
				Class<IBusinessStart> c = (Class<IBusinessStart>) Class.forName(busProcess.getClassName());
				IBusinessStart business = c.newInstance();
				boolean sendResult = business.start(moTask);
				return sendResult;
			}
			else
			{
				EmpExecutionContext.debug("上行信息ptmsgid:" + moTask.getPtMsgId() + "，为外部模块，由http方式推送");

				//使用http方式把上行信息推送到业务配置的url
				String http = busProcess.getHttpUrl();
				StringBuffer paramEntity = new StringBuffer("command=MO_REQUEST&spid=").append(moTask.getSpUser()).append("&momsgid=").append(moTask.getPtMsgId()).append("&da=").append(moTask.getSpnumber()).append("&sa=").append(moTask.getPhone()).append("&dc=").append(moTask.getMsgFmt()).append("&sm=").append(ChangeCharset.encodeHex(moTask.getMsgContent().getBytes(ChangeCharset.GBK))).append("&exno=").append(moTask.getSubno());

				String responseStr = requestHttp("POST", http, paramEntity.toString());

				if(responseStr.length() > 0)
				{
					//处理响应
					int a = responseStr.indexOf("mostat=");
					int b = responseStr.lastIndexOf("&");

					String mostat = responseStr.substring(a + 7, b);
					String moerrorcode = responseStr.substring(responseStr.lastIndexOf("=") + 1);
					if(mostat.trim().equals("ACCEPTD") && moerrorcode.trim().equals("000"))
					{
						//成功接收上行请求
						EmpExecutionContext.debug("上行信息ptmsgid:" + moTask.getPtMsgId() + "，成功接收");
						return true;
					}
					else
					{
						EmpExecutionContext.debug("上行信息ptmsgid:" + moTask.getPtMsgId() + "，接收失败");
						return false;
					}
				}
				return false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行尾号推送处理，处理异常。"
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()	
			);
			return false;
		}
	
	}
	
	/**
	 * 推送执行上行推送任务
	 * 2012-8-2
	 * @param moTask 上行推送任务
	 * @return 推送执行结果
	 * @throws Exception
	 * boolean
	 */
	public boolean sendOrder(LfMotask moTask) throws Exception
	{
		//上行对象不能为空
		if (moTask == null)
		{
			EmpExecutionContext.error("指令信息推送失败，上行对象为空。");
			return false;
		}
		
		//条件
		try
		{
			EmpExecutionContext.info("上行指令推送处理。"
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()
			);
			//企业发送账号不可能相同
			LfSpDepBind bind = this.getSpDepBind(moTask.getSpUser());
			if(bind == null)
			{
				EmpExecutionContext.error("上行指令推送处理，获取不到发送账号绑定对象。"
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
				);
				return false;
			}
			moTask.setCorpCode(bind.getCorpCode());
			
			String moClassName = "com.montnets.emp.engine.biz.MoOrderStart";
			// 是否加载移动业务模块，22：移动业务
			if(new SmsBiz().isWyModule("22"))
			{
				EmpExecutionContext.info("上行指令推送处理，推送移动业务。"
						+ "momsgid="+moTask.getPtMsgId()
						+ ",phone="+moTask.getPhone()
						+ ",spuser="+moTask.getSpUser()
						+ ",spnumber="+moTask.getSpnumber()
						+ ",exno="+moTask.getSubno()
						+ ",msg="+moTask.getMsgContent()
						+ ",cmdid="+moTask.getMoOrder()	
						+ ",corpCode="+moTask.getCorpCode()	
				);
				//指令类型，3：移动业务
				String tructType = "";
				//以ID为条件查询指令表
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("id", moTask.getMoOrder());
				List<AcmdRoute> acmdRouteList = empDao.findListByCondition(AcmdRoute.class, conditionMap,null);
				//上行指令在指令表中存在
				if(acmdRouteList != null && acmdRouteList.size() > 0)
				{
					//获取指令类型
					tructType = acmdRouteList.get(0).getTructtype()==null?"":acmdRouteList.get(0).getTructtype().toString();
					//指令为移动业务
					if(tructType != null && !"".equals(tructType) && "03".equals(tructType))
					{
						String className = "com.montnets.emp.mobilebus.biz.MobileBusMoOrderStart";
						//移动业务上行指令类存在
						if(checkClassName(className))
						{
							moClassName = className;
						}
					}
				}
			}
			//根据业务类信息反射初始化业务类并调用业务方法，返回状态
			Class<IOrderStart> c = (Class<IOrderStart>) Class.forName(moClassName);
			IOrderStart business = c.newInstance();
			boolean result = business.start(moTask);
			return result;
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "执行指令推送任务异常。"
					+ "momsgid="+moTask.getPtMsgId()
					+ ",phone="+moTask.getPhone()
					+ ",spuser="+moTask.getSpUser()
					+ ",spnumber="+moTask.getSpnumber()
					+ ",exno="+moTask.getSubno()
					+ ",msg="+moTask.getMsgContent()
					+ ",cmdid="+moTask.getMoOrder()		
			);
			return false;
		}
	}
	
	private LfSpDepBind getSpDepBind(String spUser){
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//企业发送账号不可能相同
			conditionMap.put("spUser", spUser);
			List<LfSpDepBind> bindsList = empDao.findListByCondition(LfSpDepBind.class, conditionMap, null);
			if(bindsList != null && bindsList.size() > 0){
				return bindsList.get(0);
			}
			return null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "推送时，根据发送账号获取绑定对象异常。");
			return null;
		}
		
	}

	
	private String requestHttp(String method, String destUrl, String param) { 
		
		HttpURLConnection huc = null;
		URL url = null;
		String result="";
		InputStream in = null;
		BufferedReader breader =null;
		try{
			//建立链接
			url = new URL(destUrl);
			
			huc = (HttpURLConnection) url.openConnection();
			
			huc.setDoInput(true);  
			huc.setDoOutput(true);
			huc.setConnectTimeout(60000);
			
			if("POST".equals(method)){
				huc.setUseCaches(false);
			}
			//设置请求方式
			huc.setRequestMethod(method);
			//建立连接
			huc.connect();
			//String cookie = huc.getHeaderField("Set-Cookie");
			//写参数
			if(param != null && !"".equals(param)){
				huc.getOutputStream().write(param.getBytes("utf-8"));
				huc.getOutputStream().flush();
				huc.getOutputStream().close();
			}
			
			//int code = huc.getResponseCode();
			// 获取页面内容
			in = huc.getInputStream();
			breader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String str = breader.readLine();
			while (str != null)
			{
				result += str;
				str = breader.readLine();
			}
			
/*			String cookieVal = null;  
	        String sessionId = "";  
	        String key=null;
	        for (int i = 1; (key = huc.getHeaderFieldKey(i)) != null; i++ ) {  
                if (key.equalsIgnoreCase("set-cookie")) {  
                    cookieVal = huc.getHeaderField(i);  
                    cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));  
                    sessionId = sessionId+cookieVal+";";  
                }  
            } */
			//关闭连接
			huc.disconnect();
		}catch(Exception e){
			EmpExecutionContext.error(e, "执行http异常。");
		}finally{
			//关闭
			try {
				if(breader!=null){
					breader.close();
				}
				if(in!=null){
					in.close();
				}
				if(huc != null){
					huc.disconnect();
				}
			} catch (IOException e) {
				EmpExecutionContext.error(e, "关闭资源异常。");
			}
			
		}
		//返回结果
		return result;
	}

	/**
	 * 
	 * @param kvKey
	 * @return
	 */
//	private LfKeyValue getKeyValueByKey(String kvKey)
//	{
//
//		LfKeyValue keyvalue = null;
//		try
//		{
//			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//			conditionMap.put("kvKey", kvKey);
//
//			List<LfKeyValue> keyvaluesList = empDao.findListByCondition(
//					LfKeyValue.class, conditionMap, null);
//			if (keyvaluesList != null && keyvaluesList.size() > 0)
//			{
//				keyvalue = keyvaluesList.get(0);
//			}
//
//		} catch (Exception e)
//		{
//			EmpExecutionContext.error(e);
//		}
//		return keyvalue;
//	}

	/**
	 * 
	 * @param regType
	 * @param codes
	 * @param codeType
	 * @return
	 * @throws Exception
	 */
//	private LfBusProcess getBusProcByCode(Integer regType, String codes,
//			Integer codeType) throws Exception
//	{
//		LfBusProcess bus = null;
//		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//
//		conditionMap.put("regType", regType.toString());
//
//		conditionMap.put("codes", codes);
//		conditionMap.put("codeType", codeType.toString());
//
//		List<LfBusProcess> busList = empDao.findListByCondition(
//				LfBusProcess.class, conditionMap, null);
//		if (busList != null && busList.size() > 0)
//		{
//			bus = busList.get(0);
//		}
//
//		return bus;
//	}

	/**
	 * 
	 */
	private LfSubnoAllotDetail getUpMsgSubnoDetail(String spNumber)
			throws Exception
	{

		LfSubnoAllotDetail subnoDetail = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("spNumber", spNumber);
			List<LfSubnoAllotDetail> subnosList = empDao.findListByCondition(
					LfSubnoAllotDetail.class, conditionMap, null);
			if (subnosList != null && subnosList.size() != 0)
			{
				subnoDetail = subnosList.get(0);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取尾号异常。spNumber：" + spNumber);
			throw e;
		}
		return subnoDetail;
	}

	/**
	 * 检查类是否存在
	 * @description    
	 * @param className 类路径
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-13 下午01:54:49
	 */
	public boolean checkClassName(String className)
	{
		//类名不能为空
		if (className == null || "".equals(className.trim()))
		{
			EmpExecutionContext.error("检查类路径为空。");
			return false;
		}
		
		try
		{
			//反射初始化类
			if (null != Class.forName(className))
			{
				return true;
			}
			return false;
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "检查类是否存在异常。");
			return false;
		}
	}
}
