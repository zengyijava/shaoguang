package com.montnets.emp.common.biz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.GenericLfBirthdaySetupVoDAO;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.vo.LfBirthdaySetupVo;
import com.montnets.emp.entity.birthwish.LfBirthdayMember;
import com.montnets.emp.entity.birthwish.LfBirthdaySetup;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sms.LfTask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 生日祝福
 * @author Administrator 
 */
public class SendBirthDateBiz{
	
	private IEmpTransactionDAO smsTransDao;

	private IEmpDAO smsDao;
	
	private SmsSpecialDAO  smsSpecialDAO ;

	public SendBirthDateBiz() {
		smsTransDao = new DataAccessDriver().getEmpTransDAO();
		smsDao = new DataAccessDriver().getEmpDAO();
		smsSpecialDAO = new SmsSpecialDAO();
	}

	/**
	 * 发送
	 * @param
	 * @return
	 * @throws Exception
	 */
	public String sendSms(Long id)  {		
		
		//发送生日祝福手机号码集合
		Map<String,String> phonesMap = null;
		//有效号码数
		Long effCount = 0l;
		String[] urlValuesArray = null;
		LfBirthdaySetup bdSetup = null;
		TxtFileUtil fileUtil = null;
		
		LfSysuser user = null;
		String birthdayLoggerInfo=null;
		//总流程1：获取生日祝福号码并生成号码文件
		
		FileOutputStream fos = null; 
		OutputStreamWriter write = null;
		BufferedWriter writer = null;
		WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
		BalanceLogBiz balanceBiz = new BalanceLogBiz();
		try
		{
			bdSetup = smsDao.findObjectByID(LfBirthdaySetup.class, id);
			if(bdSetup == null)
			{
				EmpExecutionContext.error("ID为"+id+"的生日祝福任务对象不存在！");
				return null;
			}
			if(bdSetup.getType().intValue()==1){
				//员工生日祝福
				birthdayLoggerInfo="[员工生日祝福，操作员ID："+bdSetup.getUserId()+"，企业编码："+bdSetup.getCorpCode()+"，生日祝福ID："+id+"]";
			}else{
				//客户生日祝福
				birthdayLoggerInfo="[客户生日祝福，操作员ID："+bdSetup.getUserId()+"，企业编码："+bdSetup.getCorpCode()+"，生日祝福ID："+id+"]";
			}
			//判断是否启用(1是；2否)
			if(bdSetup.getIsUse() == 2)
			{
				EmpExecutionContext.info(birthdayLoggerInfo+"生日祝福任务状态为禁用，不允许执行！");
				return null;
			}
			//获取创建操作员
			user = smsDao.findObjectByID(LfSysuser.class, bdSetup.getUserId());
			
			CheckUtil checkUtil = new CheckUtil();
			ErrorCodeParam errorCodeParam=new ErrorCodeParam();
			//验证操作员企业发送账户是否一致性
			boolean checkSendResult = checkUtil.checkSysuserInCorp(user, bdSetup.getCorpCode(), bdSetup.getSpUser(), errorCodeParam);
			if(!checkSendResult)
			{
				
				String info="";
				if("nosysuser".equals(errorCodeParam.getErrorCode()))
				{
					info="通过生日祝福任务的操作员ID["+bdSetup.getUserId()+"]获取不到操作员信息";
				}else if("nocorpcode".equals(errorCodeParam.getErrorCode()))
				{
					info="生日祝福任务的企业编码为空";
				}else if("sysusernotin".equals(errorCodeParam.getErrorCode()))
				{
					info="操作员的企业编码["+user.getCorpCode()+"]与生日祝福任务的企业编码["+bdSetup.getCorpCode().trim()+"]不一致";
				}else if("spusernotin".equals(errorCodeParam.getErrorCode()))
				{
					info="SP账号["+bdSetup.getSpUser()+"]未分配给企业["+bdSetup.getCorpCode().trim()+"]或SP账号和企业的绑定关系不可用";
				}else
				{
					info="检查SP账号["+bdSetup.getSpUser()+"]与企业["+bdSetup.getCorpCode().trim()+"]的绑定关系失败";
				}
				
				EmpExecutionContext.error(birthdayLoggerInfo+"生日祝福发送，"+info+"，当次执行取消！");
				return null;
			}
			
			//生日祝福验证企业是否禁用，禁用的企业不发送生日祝福
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
			//设置企业编码查询条件
			conditionMap.put("corpCode", bdSetup.getCorpCode());
			//获取企业信息
			List<LfCorp> corpList = new BaseBiz().getByCondition(LfCorp.class, conditionMap, null);
			if(corpList == null || corpList.size() == 0)
			{
				EmpExecutionContext.error(birthdayLoggerInfo+"定时执行生日祝福发送任务时，通过corpCode="+bdSetup.getCorpCode()+"获取不到企业对象！");
				return null;
			}
			//企业状态为禁用
			if(corpList.get(0).getCorpState() == 0)
			{
				EmpExecutionContext.error(birthdayLoggerInfo+"定时执行生日祝福发送任务时，企业状态为禁用，不允许发送，corpCode:"+bdSetup.getCorpCode());
				return null;
			}
			
			
			
			//当前登录操作员不存在时
			if(user == null)
			{
				EmpExecutionContext.error(birthdayLoggerInfo+"userId为"+bdSetup.getUserId()+"的操作员对象不存在！");
				return null;
			}
			else if(user.getUserState() == 0)//禁用的状态
			{
				EmpExecutionContext.info(birthdayLoggerInfo+"操作员状态为禁用，不允许执行！");
				return null;
			}			
			//员工生日祝福手机号
			if(bdSetup.getType().equals(1))
			{
				phonesMap = smsSpecialDAO.findEmployeePhones(bdSetup.getId());

			}
			else if(bdSetup.getType().equals(2))//客户生日祝福手机号
			{
				phonesMap = smsSpecialDAO.findClientPhones(bdSetup.getId());
			}
			//没有需要发送生日祝福的号码
			if(phonesMap == null || phonesMap.size() == 0)
			{
				EmpExecutionContext.info(birthdayLoggerInfo+"运行生日祝福："+new Date()+"，没有需要发送生日祝福的号码！");
				return null;
			}
			//0为物理地址，1为相对地址
			fileUtil = new TxtFileUtil();
			urlValuesArray = fileUtil.createUrlAndDir(1, "BD"+bdSetup.getId().toString());
			
			File file = new File(urlValuesArray[0]);
			if (!file.exists() != false) 
			{
				boolean state = file.createNewFile();
				if(!state){
					EmpExecutionContext.error("创建文件失败");
				}
			}

			fos = new FileOutputStream(urlValuesArray[0],true); 
			write = new OutputStreamWriter(fos,"GBK");
			writer = new BufferedWriter(write,1024);
			
			HttpSmsSend jobBiz = new HttpSmsSend();
			
			BlackListAtom blackBiz = new BlackListAtom();
			
			String[] haoduan = wgMsgBiz.getHaoduan();
			//Map<String,List<String>> blList =blackBiz.getlfBlackMapByBusCode(bdSetup.getBuscode(), 1, bdSetup.getSpUser(),bdSetup.getCorpCode());
			
			String content = null;
			//系统换行符
			String newline = System.getProperties().getProperty("line.separator");
			//短信内容
			String msg = null;
			
			PhoneUtil phoneUtil=new PhoneUtil();
			
			for(String phone : phonesMap.keySet())  
			{
				//增加国际号码处理
				if(wgMsgBiz.checkMobile(phone,haoduan) != 1&&!phoneUtil.isAreaCode(phone) )
				{//检测号段
					continue;
				}
				//黑名单过滤
				if (blackBiz.checkBlackList(bdSetup.getCorpCode(), phone, bdSetup.getBuscode()))
				{
					continue;
				}
				
				content = phone+",";
				msg = bdSetup.getMsg();//短信内容
				
				//是否需要尊称
				if (bdSetup.getIsAddName() != null && bdSetup.getIsAddName() == 1 && phonesMap.get(phone) != null && !"".equals(phonesMap.get(phone)))//有姓名，则生成尊称
				{
					msg = bdSetup.getAddName() + msg;//放入占位符
				}
				
				//是否需要签名
				if(bdSetup.getIsSignName() != null && bdSetup.getIsSignName() == 1)
				{
					msg = StaticValue.getBirthwishNameSignLeft() + user.getName() + StaticValue.getBirthwishNameSignRight() + msg;
				}
				//生成尊称
				content += jobBiz.combineContent(phonesMap.get(phone), msg)+ newline;
				//content += msg + newline;
				
				writer.write(content);
				effCount++;//有效号码数加1
			}
			
			write.flush();
			writer.flush();

		}
		catch (Exception e) 
		{
			//异常处理
			EmpExecutionContext.error(e, birthdayLoggerInfo+"生日祝福发送异常。");
			return null;
		} 
		finally 
		{
			//关闭输入流
			try {
				if(fos != null)
				{
					fos.close();
				}
				if(write != null)
				{
					write.close();
				}
				if(writer != null)
				{
					writer.close();
				}
				
			} catch (IOException e) 
			{
				EmpExecutionContext.error(e,birthdayLoggerInfo+"生日祝福发送，关闭资源异常。");
			}
		}
		
		
		//总流程2:扣费
		//返回结果
		String errorStr = null;
		BalanceLogBiz b = new BalanceLogBiz();
		String taskidStr=null;
		//获取连接
		Connection conn = smsTransDao.getConnection();
		//初始化发送任务
		LfMttask mttask = null;
		try
		{			
			//开启事务
			smsTransDao.beginTransaction(conn);
			
			mttask = new LfMttask();
			//设置任务类型 
			if(bdSetup.getType().intValue()==1){
				//员工生日祝福
				mttask.setMsType(9);
			}else{
				//客户生日祝福
				mttask.setMsType(10);
			}
			
			//提交数
			mttask.setSubCount(Long.valueOf(phonesMap.size()));
			//有效号码总数
			mttask.setEffCount(effCount);
			mttask.setMobileUrl(urlValuesArray[1]);
			//主题 
			mttask.setTitle(bdSetup.getTitle());
			//发送账号
			mttask.setSpUser(bdSetup.getSpUser());
			//（相同1，不同2，动模3）
			mttask.setBmtType(2);
			//是否定时发送  1-是 0-否
			mttask.setTimerStatus(0);
//			//(实时1，定时2)
//			mttask.setMsgType(2);
			//1：相同内容 2：动态模板  3：不同内容 modify by tanglili 20190104
			mttask.setMsgType(3);
			//信息类型 1-短信， 2-彩信，5-移动财务
			//mttask.setMsType(1);
			//提交状态(创建中1，提交2，取消3)
			mttask.setSubState(2);
			mttask.setBusCode(bdSetup.getBuscode());
			//mttask.setMsg(bdSetup.getMsg());
			//0是未发送，1是已发送,2发送失败,3(未使用),  4发送中,5超时未发送
			mttask.setSendstate(1);
			mttask.setCorpCode(bdSetup.getCorpCode());
			//mttask.setSendLevel(Integer.parseInt(priority));//发送优先级
			//是否重发  1-已重发 0-未重发
			mttask.setIsReply(Integer.valueOf(0));
			//mttask.setSubNo(request.getParameter("subNo"));
			CommonBiz commonBiz = new CommonBiz();
			//GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
			//taskid
			Long taskId = commonBiz.getAvailableTaskId();
			taskidStr="taskid："+String.valueOf(taskId)+"，";
			mttask.setTaskId(taskId);
			SmsBiz smsBiz = new SmsBiz();
			//统计预发送条数
			int icount = smsBiz.countAllOprSmsNumber(mttask.getSpUser(), mttask.getMsg(), mttask.getMsgType()>1?2:1, mttask.getMobileUrl(), null);
			mttask.setIcount(String.valueOf(icount));//发送短信总数(网关发送总数)
			mttask.setUserId(bdSetup.getUserId());
			//审批状态(无需审批-0，未审批-1，同意1，拒绝2)
			mttask.setReState(0);
			mttask.setSubmitTime(new Timestamp(Calendar.getInstance()
					.getTime().getTime()));
			//设置定时时间为当前时间
			mttask.setTimerTime(mttask.getSubmitTime());
		    //fileuri不允许为空，则设置本节点URI，后面文件上传了，再修改。
			mttask.setFileuri(StaticValue.BASEURL);
			Long mtId = smsDao.saveObjectReturnID(mttask);
			mttask.setMtId(mtId);
			
			//判断流程是否结束的标识
			boolean okToSend = true;
			//SP账号余额检查
			int spFeeResult = balanceBiz.checkSpUserFee(mttask.getSpUser(), Integer.parseInt(mttask.getIcount()), 1);
			//余额不足，或者检查异常
			if(spFeeResult < 0)
			{
            	errorStr = "error:"+spFeeResult;	            	
            	okToSend = false;
            	EmpExecutionContext.error(birthdayLoggerInfo+taskidStr+"SP账号余额不足" 
            			+ "，SP账号余额检查返回："+spFeeResult
            			+ "，spUser："+mttask.getSpUser()
            			+ "，sendCount："+mttask.getIcount()
            			+ "，userId："+bdSetup.getUserId()
            			);
            }
			
			//流程标识为true，表示前面流程正常，这里可以继续
			if(okToSend)
			{
				String wgresult = "";
				try
				{
					wgresult = balanceBiz.wgKoufei(mttask);
					
				}catch(Exception e)
				{
					EmpExecutionContext.error(e, birthdayLoggerInfo+taskidStr+"生日祝福："+IErrorCode.B20011);
					throw new EMPException("生日祝福："+IErrorCode.B20011);
				}
				if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || "lessgwfee".equals(wgresult.substring(0, 9)))
				{
					errorStr = "error:"+wgresult; 
					okToSend = false;
				}
			}
			if(okToSend && b.IsChargings(mttask.getUserId()))
			{
				int feeResult= b.sendSmsAmountByUserId(conn, mttask.getUserId(), Integer.parseInt(mttask.getIcount()));
				//0:短信扣费成功
				if(feeResult == 0)
	            {
					//提交事务
					smsTransDao.commitTransaction(conn);	            	
	            }
				//-2:短信余额不足
				else if(feeResult == -2)
				{
					smsTransDao.rollBackTransaction(conn);
					EmpExecutionContext.error(birthdayLoggerInfo+taskidStr+"短信余额不足");
					errorStr = "error:"+feeResult;
					
					okToSend = false;
				}
	            else
	            {
	            	//回滚事务
	            	smsTransDao.rollBackTransaction(conn);
	            	errorStr = "error:"+feeResult;	            	
	            	EmpExecutionContext.error(birthdayLoggerInfo+taskidStr+"短信扣费失败未知错误:"+feeResult);
	            	
	            	okToSend = false;
	            }
			}
			//判断是否使用集群
			if(StaticValue.getISCLUSTER() ==1 && okToSend)
			{
				CommonBiz commBiz = new CommonBiz();
				//上传文件到文件服务器
//				if("success".equals(commBiz.uploadFileToFileCenter(mttask.getMobileUrl())))
//				{
//					//删除本地文件
//					//commBiz.deleteFile(mttask.getMobileUrl());
//				}else
//				{
//					//上传失败则抛出异常
//					throw new Exception("上传文件到文件服务器失败");
//				}
				//使用文件服务器地址
				mttask.setFileuri(commBiz.uploadFileToFileServer(mttask.getMobileUrl()));
			}else
			{
				//使用本节点地址
				mttask.setFileuri(StaticValue.BASEURL);
			}
		}
		catch(Exception e)
		{
			//异常处理
			smsTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, birthdayLoggerInfo+taskidStr+"生日祝福发送异常。");
			return null;
		}
		finally
		{
			//关闭事务
			smsTransDao.closeConnection(conn);
		}		
		//总流程3：发送短信
		//获取连接
		conn = smsTransDao.getConnection();
		String resultStr = null;//返回结果
		
		try 
		{	
			//创建任务对象
			LfTask lfTask = new LfTask();
			lfTask.setMtId(mttask.getMtId());
			lfTask.setTaskType(1);
			lfTask.setSpUser(mttask.getSpUser());
			lfTask.setUserName(user.getUserName());
			lfTask.setDepId(user.getDepId());
			lfTask.setUserId(mttask.getUserId());
			lfTask.setCorpCode(mttask.getCorpCode());
			lfTask.setTaskId(mttask.getTaskId());
			//开启事务
			smsTransDao.beginTransaction(conn);
			//不为空，则前面有错误
			if(errorStr != null)
			{
				throw new Exception(birthdayLoggerInfo+taskidStr+"错误码："+errorStr);
			}
			//保存
			smsTransDao.save(conn, lfTask);

			//LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//conditionMap.put("userId", mttask.getSpUser());
			//List<Userdata> tempList = smsDao.findListByCondition(Userdata.class, conditionMap, null);//根据发送账号获取企业账户信息
			Userdata userData = wgMsgBiz.getSmsUserdataByUserid(mttask.getSpUser());
			//if (tempList != null && tempList.size() == 1) 
			//{
			//	userData = tempList.get(0);
			//}
			//调用发送接口，组装发送对象
			HttpSmsSend smsSend = new HttpSmsSend();
			WGParams params = new WGParams();
			//发送账号
			params.setSpid(mttask.getSpUser());
			params.setSppassword(userData.getUserPassword());
			//params.setSa("");
			params.setBmttype(mttask.getMsgType() > 1 ? "2" : "1");
			params.setTaskid(lfTask.getTaskId().toString());
			params.setTitle(mttask.getTitle());
			params.setContent(mttask.getMsg());
			params.setTaskid(mttask.getTaskId().toString());
			if(mttask.getSendLevel()!=null)
			{
				params.setPriority(mttask.getSendLevel().toString());
			}
			else
			{
				params.setPriority("1");
			}
			if((mttask.getIsReply()==1 || mttask.getIsReply() == 2) && mttask.getSubNo()!=null && !"".equals(mttask.getSubNo().trim()))
			{
					params.setSa(mttask.getSubNo());
			}
			//URL地址要设置全地址
			params.setUrl(mttask.getFileuri()+mttask.getMobileUrl());
			params.setParam1(user.getUserCode().toString());
			params.setSvrtype(mttask.getBusCode());
			params.setRptflag("0");
			resultStr = "";//由于下面要补费，如果不在发送的前一步赋值，下面发送抛异常后，这个变量将是null，导致无法补费
            //发送接口
			String result = smsSend.createbatchMtRequest(params);
			
			if(result == null)
			{
				resultStr = "";//由于下面要补费，所以为null时重新赋空值
			}
			
			//将返回状态记录到文件里
			fileUtil.writeSendResult(mttask.getTaskId(), result);
			//EmpExecutionContext.requestInfo(mttask.getTaskId(), result);
			int index = result.indexOf("mterrcode");
			if(index<0)
			{
				mttask.setSendstate(2);//下行状态设置为已发送失败					
				smsTransDao.rollBackTransaction(conn);

			}
			else
			{
				resultStr = result.substring(index + 10, index + 13);
                //发送成功
				if (resultStr.equals("000")) 
				{
					mttask.setSendstate(1);//下行状态设置为已发送成功
					smsTransDao.commitTransaction(conn);
					resultStr = "000";
					
				} else 
				{
					//运营商余额回收
					balanceBiz.huishouFee( Integer.parseInt(mttask.getIcount()), mttask.getSpUser(), mttask.getMsType());
                    //发送失败
					resultStr = result.substring(index - 8, index - 1);
					mttask.setSendstate(2);//下行状态设置为已发送失败	
					smsTransDao.rollBackTransaction(conn);
					
				}
			}
		} 
		catch (Exception e) 
		{
			//异常处理
			mttask.setSendstate(2);
			smsTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, birthdayLoggerInfo+taskidStr+"生日祝福发送异常。");
		}
		finally 
		{
			//关闭连接
			smsTransDao.closeConnection(conn);			
			//add by chenhong 2012.06.13
			if(b.IsChargings(mttask.getUserId()))
			{
				//如果发送状态不等于发送成功,则需要在此补回
				if(resultStr != null && !"000".equals(resultStr))
				{
					 b.sendSmsAmountByGuid(null, user.getGuId(), Integer.parseInt(mttask.getIcount())*-1);
				}
			}
			//add end
			
			mttask.setSucCount(null);
			mttask.setFaiCount(null);
			//lfMttask.setIcount(null);
			mttask.setErrorCodes(resultStr);//记录网关返回状态
			
			if(!"000".equals(resultStr))
			{
			
				int a = 1;
				while (a<4) {
					try 
					{
						if(smsDao.update(mttask))
						{
							a=4;
						}
						else 
						{
							a++;
						}
					} 
					catch (Exception e2) {
						EmpExecutionContext.error(e2, birthdayLoggerInfo+taskidStr+"生日祝福发送后，更新异常。");
						try 
						{
							Thread.sleep(500L);
						} catch (Exception e3) 
						{
							EmpExecutionContext.error(e3, birthdayLoggerInfo+taskidStr+"生日祝福发送后，更新异常。");
						}
						finally
						{
							a++;
						}
					}
				}
			}
		}		
		return resultStr;		
	}
	
	
	/**
	 * 查询生日祝福设置成员表记录
	 * @param member 查询条件
	 * @return 返回成员记录
	 * @throws Exception
	 */
	public List<LfBirthdayMember> getMember(LfBirthdayMember member) 
	{
		try
		{
			return smsSpecialDAO.findDBMember(member);
		}
		catch(Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "查询生日祝福设置成员表记录异常。");
		}
		return null;
	}
	/**
	 * 删除
	 * @param guIds
	 * @param corpCode
	 * @param type
	 * @return
	 */
	public int deleteAddrBirthMemberByGuIds(String guIds,String corpCode,String type)
	{
		int deleteCount=0;
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("type", type);
			conditionMap.put("memberId&in", guIds);
			conditionMap.put("corpCode", corpCode);
			deleteCount = smsDao.delete(LfBirthdayMember.class, conditionMap);
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e, "删除生日祝福成员异常。");
		}
		return deleteCount;
	}
	
	/**
	 * 注销生日祝福定时任务
	 * @param conn
	 * @param userId
	 * @return 成功返回true
	 */
	public boolean CanceledBirthDay(Connection conn, Long userId)
	{
		if(userId == null)
		{
			EmpExecutionContext.error("注销生日祝福失败，操作员id为空");
			return false;
		}
		boolean result = false;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userId.toString());
			List<LfBirthdaySetup> setUpsList = smsDao.findListByCondition(LfBirthdaySetup.class, conditionMap, null);
			
			//没记录，不需删除
			if(setUpsList == null || setUpsList.size() == 0)
			{
				return true;
			}
			
			TaskManagerBiz timerBiz = new TaskManagerBiz();
			LfBirthdaySetup setup = null;
			for(int i = 0; i < setUpsList.size(); i++)
			{
				setup = setUpsList.get(i);
				setup.setIsUse(2);
				smsTransDao.update(conn, setup);//更新成失效
				//删除定时器
				timerBiz.delTimerTask(conn, "BD|"+setup.getId());
			}
			result = true;
		} 
		catch (Exception e) 
		{
			//异常处理
			EmpExecutionContext.error(e, "注销生日祝福异常。");
			return false;
		}
		return result;
	}
	
	/**
	 * 获取生日祝福任务集合
	 * @param loginUserId
	 * @param vo
	 * @param pageInfo
	 * @return
	 */
	public List<LfBirthdaySetupVo> getBirthdaySetupVoList(Long loginUserId, LfBirthdaySetupVo vo,PageInfo pageInfo)
	{
		try
		{
			return new GenericLfBirthdaySetupVoDAO().findBirthdaySetupVoList(loginUserId, vo, pageInfo);
		}
		catch(Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取生日祝福任务集合异常。");
			return null;
		}
	}
	
	/**
	 * 删除成员
	 * @param depId
	 * @param corpCode
	 * @param type
	 * @param conn
	 * @return
	 */
	public int deleteAddrBirthMemberByDepId(String depId,String corpCode,Integer type,Connection conn)
	{
		Integer deleteCount=0;

		try 
		{
			deleteCount = new GenericLfBirthdaySetupVoDAO().deleteAddrBirthMemberByDepId(depId, corpCode, type, conn);
		} 
		catch (Exception e) 
		{
			//异常处理
			EmpExecutionContext.error(e, "根据机构id，删除生日祝福成员异常。");
		}
		
		return deleteCount;
	}
	/*public List<LfBirthdaySetupVo> getBirthdaySetupVoList(LfBirthdaySetupVo vo,PageInfo pageInfo) throws Exception
	{
		String countSql = "select count(*) totalcount";
		String sql = new StringBuffer("select birthdaySetup.*,sysuser.dep_id,sysuser.name,dep.dep_name ").toString();
		StringBuffer tableSql = new StringBuffer(" from ")
				.append(TableLfBirthdaySetup.TABLE_NAME).append(" birthdaySetup ").append(StaticValue.WITHNOLOCK)
				.append(" left join ").append(TableLfSysuser.TABLE_NAME).append(" sysuser").append(StaticValue.WITHNOLOCK).append(" on birthdaySetup.user_Id=sysuser.user_Id")
				.append(" left join ").append(TableLfDep.TABLE_NAME).append(" dep ").append(StaticValue.WITHNOLOCK).append(" on sysuser.dep_id=dep.dep_id")
				.append(" where ").append(TableLfBirthdaySetup.CORPCODE).append("=").append(vo.getCorpCode());
				
		String moreWhere = "";
		
		if(vo.getDepid()!=null)
		{
			moreWhere+=" and "+new GenericLfDepVoDAO().getChildUserDepByParentID(vo.getDepid(),"sysuser.dep_id");
		}
		if(vo.getUsername()!=null)
		{
			moreWhere+=" and sysuser.name like '%"+vo.getUsername()+"%'" ;
		}
		if(vo.getIsUse()!=null)
		{
			moreWhere+=" and birthdaySetup.IS_USE="+vo.getIsUse() ;
		}
		if(vo.getSpUser()!=null)
		{
			moreWhere+=" and birthdaySetup.SP_USER = '"+vo.getSpUser()+"'" ;
		}
		if(vo.getType()!=null)
		{
			moreWhere+=" and birthdaySetup.TYPE = "+vo.getType() ;
		}
		if(vo.getTitle()!=null)
		{
			moreWhere+=" and birthdaySetup.TITLE like '%"+vo.getTitle()+"%'" ;
		}
		
		sql = sql+tableSql+moreWhere;
		countSql=countSql+tableSql+moreWhere;
		List<LfBirthdaySetupVo> birthdaySetupVos =  new GenericDAO().findPageVoListBySQL(LfBirthdaySetupVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		return birthdaySetupVos;
	}*/
}
