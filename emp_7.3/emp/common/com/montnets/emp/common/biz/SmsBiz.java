package com.montnets.emp.common.biz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.*;

import com.montnets.emp.entity.sms.LfDrafts;
import com.montnets.emp.entity.sms.LfSubDrafts;

import org.apache.commons.beanutils.DynaBean;

import sun.misc.BASE64Decoder;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.MonAlarmDsmParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasgrpbind.LfAccountBind;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.security.numsegment.OprNumSegmentBiz;
import com.montnets.emp.smstask.SmsUtil;
import com.montnets.emp.table.pasroute.TableGtPortUsed;
import com.montnets.emp.table.passage.TableXtGateQueue;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;

public class SmsBiz extends SuperBiz
{

	private SmsSpecialDAO	smsSpecialDAO	= new SmsSpecialDAO();

	private SmsUtil			smsUtil			= new SmsUtil();

	private String			line			= new Properties(System.getProperties()).getProperty("line.separator");
	
	private OprNumSegmentBiz oprNumSegmentBiz = new OprNumSegmentBiz();
	
	/**
	 * 统计该短信的内容拆分条数（暂不使用）
	 * 使用新方法countSmsNumber(String msg, String phone, String[] haoduan, List<GtPortUsed> gtPortsList)
	 * @param
	 * @param msg
	 *        发送内容
	 * @param phone
	 *        手机号码
	 * @param haoduan
	 *        号段
	 * @param
	 *
	 * @return
	 * @throws Exception
	 */
	public int countSmsNumber(String msg, String phone, String[] haoduan, List<GtPortUsed> gtPortsList) throws Exception
	{
		GtPortUsed gtPort = new GtPortUsed();

		int[] sendCount = new int[] {1,1,1};
		int len;
		int maxLen;
		int totalLen;
		int lastLen;
		int signLen;
		int index = 0;

		for (int g = 0; g < gtPortsList.size(); g++)
		{
			gtPort = gtPortsList.get(g);
			// 如果运营商是电信:21.则index为2，移动则为0，联通则为1
			index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();
			maxLen = gtPort.getMaxwords();
			totalLen = gtPort.getMultilen1();
			lastLen = gtPort.getMultilen2();
			signLen = gtPort.getSignlen() == 0 ? gtPort.getSignstr().trim().length() : gtPort.getSignlen();
			// 如果设定的短信签名长度为0则为实际短信签名内容的长度
			len = msg.length();
			if(len <= maxLen - signLen)
			{
				len = msg.getBytes("unicode").length - 2;
				if(len <= (totalLen - signLen + 3) * 2) sendCount[index] = 1;
				else sendCount[index] = 1 + (len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2);
			}
		}
		if(phone.length()>=7 && phone.length() <=21)
		{
			if("+".endsWith(phone.substring(0, 1)) || "00".equals(phone.substring(0, 2)))
			{
				return sendCount[0];
			}
			else
			{
				//获取号码归属运营商
				int phoneType = oprNumSegmentBiz.getphoneType(phone);
				//移动
				if(phoneType == 0)
				{
					return sendCount[0];
				}
				//联通
				if(phoneType == 1)
				{
					return sendCount[1];
				}
				//电信
				if(phoneType == 21)
				{
					return sendCount[2];
				}
			}
		}

		return 0;
	}

	/**
	 * 统计该短信的内容拆分条数(包括英文短信)
	 * 
	 * @param
	 * @param msg
	 *        发送内容
	 * @param phone
	 *        手机号码
	 * @param haoduan
	 *        号段
	 * @param
	 *
	 * @return
	 * @throws Exception
	 */
	public int countAllOprSmsNumber(String msg, String phone, String[] haoduan, String spUser) throws Exception
	{
		try {
			//获取各运营商信息发送条数
			int[] sendCount = getAllOprCoutnSms(spUser, msg);
			if(sendCount[0]+1 == 0)
			{
				EmpExecutionContext.error("统计短信发送条数失败。msg:"+msg+"，msg:"+phone+"，spUser："+spUser+"，错误码：" + IErrorCode.B20006);
				throw new EMPException(IErrorCode.B20006);
			}
			//运营商标识，移动：0；联通：1；电信:2；国外：3
			int index = getOprIndex(phone);
			if(index == -1)
			{
				return 0;
			}
			else
			{
				return sendCount[index];
			}
		} catch (Exception e) {
			EmpExecutionContext.error("统计短信发送条数失败。错误码：" + IErrorCode.B20006 
										+ "，phone:" + phone 
										+ "，spUser：" + spUser
										+ "，msg:"+msg);
			throw new EMPException(IErrorCode.B20006, e);
		}
	}
	
	/**
	 * 统计预发送条数,无绑定路由的情况下，统计提交信息总数按照和网关的规则一样，按1条统计（暂不使用）
	 * 使用新方法countAllOprSmsNumber(String sqUserid, String msg, Integer bmtType, String url, String phone)
	 * @param sqUserid
	 *        :发送账号
	 * @param msg
	 * @param bmtType
	 *        ：如果短信类型大于1，则传过来的值为2，否则则为1
	 * @param url
	 *        ：上传文件路径
	 * @param phone
	 * @return
	 * @throws EMPException
	 * @throws Exception
	 */
	public int countSmsNumber(String sqUserid, String msg, Integer bmtType, String url, String phone) throws EMPException
	{
		int sum = 0;
		BufferedReader reader = null;
		try
		{
			TxtFileUtil txtfileutil = new TxtFileUtil();
			// 根据发送账号获取路由信息
			List<GtPortUsed> gtPortsList = this.getPortByUserId(sqUserid);
			GtPortUsed gtPort = new GtPortUsed();

			// 文件发送
			if(url != null)
			{
				url = txtfileutil.getPhysicsUrl(url);
				File dist_File = new File(url);
				if(dist_File.exists() == false) return 0;
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(url), "GBK"));
			}
			else
			{
				if(phone != null)
				{
					phone = phone.replace(",", line);
					reader = new BufferedReader(new StringReader(phone));
				}
			}

			String tmp;

			switch (bmtType)
			{
				case 1:
					int[] sendCount = new int[] {1,1,1};
					int len;
					int maxLen;
					int totalLen;
					int lastLen;
					int signLen;
					int index = 0;
					for (int g = 0; g < gtPortsList.size(); g++)
					{
						gtPort = gtPortsList.get(g);
						// 如果运营商是电信:21.则index为2，移动则为0，联通则为1
						index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();
						// 获取短信最大字数
						maxLen = gtPort.getMaxwords();
						// 获取1到n-1条短信内容的长度
						totalLen = gtPort.getMultilen1();
						// 获取最后一条短信内容的长度
						lastLen = gtPort.getMultilen2();
						// 如果设定的短信签名长度为0则为实际短信签名内容的长度
						signLen = gtPort.getSignlen() == 0 ? gtPort.getSignstr().trim().length() : gtPort.getSignlen();
						// 发送短信的长度
						len = msg.length();

						if(len <= maxLen - signLen)
						{
							len = msg.getBytes("unicode").length - 2;
							if(len <= (totalLen - signLen + 3) * 2) sendCount[index] = 1;
							else sendCount[index] = 1 + (len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2);
						}
					}

					while((tmp = reader.readLine()) != null)
					{
						if(tmp.length() > 1 && tmp.indexOf(",") > -1 )
						{
							tmp = tmp.substring(0, tmp.indexOf(","));
						}
						if(tmp.length()>=7 && tmp.length() <=21)
						{
							//国际号码使用移动短信拆分规则
							if("+".endsWith(tmp.substring(0, 1)) || "00".equals(tmp.substring(0, 2)))
							{
								sum += sendCount[0];
								continue;
							}
							else
							{
								//获取号码归属运营商
								int phoneType = oprNumSegmentBiz.getphoneType(tmp);
								//移动
								if(phoneType == 0)
								{
									sum += sendCount[0];
									continue;
								}
								//联通
								if(phoneType == 1)
								{
									sum += sendCount[1];
									continue;
								}
								//电信
								if(phoneType == 21)
								{
									sum += sendCount[2];
									continue;
								}
							}
						}
					}
					reader.close();
					break;
				default:
					int[] maxLens = new int[3];
					int[] totalLens = new int[3];
					int[] lastLens = new int[3];
					int[] signLens = new int[3];

					// 标示是否存在路由
					boolean[] hasRoute = new boolean[] {false,false,false};

					for (int g = 0; g < gtPortsList.size(); g++)
					{
						gtPort = gtPortsList.get(g);
						// 如果运营商是电信:21.则index为2，移动则为0，联通则为1
						index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();

						hasRoute[index] = true;

						// 获取短信最大字数
						maxLens[index] = gtPort.getMaxwords();
						// 获取1到n-1条短信内容的长度
						totalLens[index] = gtPort.getMultilen1();
						// 获取1到n-1条短信内容的长度
						lastLens[index] = gtPort.getMultilen2();
						signLens[index] = gtPort.getSignlen() == 0 ? gtPort.getSignstr().trim().length() : gtPort.getSignlen();
						;// 如果设定的短信签名长度为0则为实际短信签名内容的长度
					}

					while((tmp = reader.readLine()) != null)
					{
						if(tmp.length() > 1 && tmp.indexOf(",") > -1 )
						{
							String temp = tmp;
							tmp = temp.substring(0, temp.indexOf(","));
							msg = temp.substring(temp.indexOf(",")+1);
						}
						else if(tmp.length() > 11)
						{
							msg = tmp.substring(12);
							tmp = tmp.substring(0,11);
						}
						else
						{
							continue;
						}
						// 短信内容的长度
						len = msg.getBytes("unicode").length - 2;
						if(tmp.length() >= 7 && tmp.length() <= 21)
						{
							//国际号码使用移动短信拆分规则
							if("+".endsWith(tmp.substring(0, 1)) || "00".equals(tmp.substring(0, 2)))
							{
								if(hasRoute[0] && msg.length() <= maxLens[0] - signLens[0])
								{
									if(len <= (totalLens[0] - signLens[0] + 3) * 2) sum += 1;
									else sum += 1 + (len - lastLens[0] * 2 + totalLens[0] * 2 - 1) / (totalLens[0] * 2);
								}
								else
								{
									sum += 1;
								}
							}
							else
							{
								//获取号码归属运营商
								int phoneType = oprNumSegmentBiz.getphoneType(tmp);
								// 如果是属于移动的号码段
								if(phoneType == 0)
								{
									if(hasRoute[0] && msg.length() <= maxLens[0] - signLens[0])
									{
										if(len <= (totalLens[0] - signLens[0] + 3) * 2) sum += 1;
										else sum += 1 + (len - lastLens[0] * 2 + totalLens[0] * 2 - 1) / (totalLens[0] * 2);
									}
									else
									{
										sum += 1;
									}
								}
								// 如果是属于联通的号码段
								else if(phoneType == 1)
								{
									if(hasRoute[1] && msg.length() <= maxLens[1] - signLens[1])
									{
										if(len <= (totalLens[1] - signLens[1] + 3) * 2) sum += 1;
										else sum += 1 + (len - lastLens[1] * 2 + totalLens[1] * 2 - 1) / (totalLens[1] * 2);
									}
									else
									{
										sum += 1;
									}
								}
								// 如果是属于电信的号码段
								else if(phoneType == 21)
								{
									if(hasRoute[2] && msg.length() <= maxLens[2] - signLens[2])
									{
										if(len <= (totalLens[2] - signLens[2] + 3) * 2) sum += 1;
										else sum += 1 + (len - lastLens[2] * 2 + totalLens[2] * 2 - 1) / (totalLens[2] * 2);
									}
									else
									{
										sum += 1;
									}
								}
							}
						}
					}
					break;
			}
		}
		catch (Exception ex)
		{
			EmpExecutionContext.error(ex, "统计预发送条数失败。错误码：" + IErrorCode.B20006);
			throw new EMPException(IErrorCode.B20006, ex);
		}
		finally
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "统计预发送条数关闭流失败。错误码：" + IErrorCode.B20006);
				}
			}
		}
		return sum;
	}

	/**
	 * 统计预发送条数,无绑定路由的情况下，统计提交信息总数按照和网关的规则一样，按1条统计（完美通知使用）
	 * 使用新方法countAllOprSmsNumber(String sqUserid, String msg, Integer bmtType, String url, String phone)
	 * @param sqUserid
	 *        :发送账号
	 * @param msg
	 * @param bmtType
	 *        ：如果短信类型大于1，则传过来的值为2，否则则为1
	 * @param url
	 *        ：上传文件路径
	 * @param phone
	 * @return
	 * @throws EMPException
	 * @throws Exception
	 */
	public int countSmsNumberByPer(String sqUserid, String msg, Integer bmtType, String url, String phone) throws EMPException
	{
		int sum = 0;
		BufferedReader reader = null;
		try
		{
			TxtFileUtil txtfileutil = new TxtFileUtil();
			// 根据发送账号获取路由信息
			List<GtPortUsed> gtPortsList = this.getPortByUserId(sqUserid);
			GtPortUsed gtPort = new GtPortUsed();

			// 文件发送
			if(url != null)
			{
				url = txtfileutil.getPhysicsUrl(url);
				File dist_File = new File(url);
				if(dist_File.exists() == false) return 0;
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(url), "GBK"));
			}
			else
			{
				if(phone != null)
				{
					phone = phone.replace(",", line);
					reader = new BufferedReader(new StringReader(phone));
				}
			}

			String tmp;

			int[] sendCount = new int[] {1,1,1,1};
			int len;
			int maxLen;
			int totalLen;
			int lastLen;
			int signLen;
			int index = 0;
			for (int g = 0; g < gtPortsList.size(); g++)
			{
				gtPort = gtPortsList.get(g);
				// 运营商，移动为0，联通为1，电信为2，国外为3
				index = gtPort.getSpisuncm();
				//电信
				if(index == 21)
				{
					index = 2;
				//国外通道
				}
				else if(index == 5)
				{
					index = 3;
				}
				// 获取短信最大字数
				maxLen = gtPort.getMaxwords();
				// 获取1到n-1条短信内容的长度
				totalLen = gtPort.getMultilen1();
				// 获取最后一条短信内容的长度
				lastLen = gtPort.getMultilen2();
				// 如果设定的短信签名长度为0则为实际短信签名内容的长度
				signLen = gtPort.getSignlen() == 0 ? gtPort.getSignstr().trim().length() : gtPort.getSignlen();
				// 发送短信的长度
				len = msg.length();

				if(len <= maxLen - signLen)
				{
					len = msg.getBytes("unicode").length - 2;
					if(len <= (totalLen - signLen + 3) * 2) sendCount[index] = 1;
					else sendCount[index] = 1 + (len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2);
				}
			}

			while((tmp = reader.readLine()) != null)
			{
				if(tmp.length() > 1 && tmp.indexOf(",") > -1 )
				{
					tmp = tmp.substring(0, tmp.indexOf(","));
				}
				if(tmp.length()>=7 && tmp.length() <=21)
				{
					//国际号码使用移动短信拆分规则
					if("+".endsWith(tmp.substring(0, 1)) || "00".equals(tmp.substring(0, 2)))
					{
						sum += sendCount[3];
						continue;
					}
					else
					{
						//获取号码归属运营商
						int phoneType = oprNumSegmentBiz.getphoneType(tmp);
						//移动
						if(phoneType == 0)
						{
							sum += sendCount[0];
							continue;
						}
						//联通
						if(phoneType == 1)
						{
							sum += sendCount[1];
							continue;
						}
						//电信
						if(phoneType == 21)
						{
							sum += sendCount[2];
							continue;
						}
					}
				}
			}
			reader.close();
		}
		catch (Exception ex)
		{
			EmpExecutionContext.error(ex, "统计预发送条数失败。错误码："+ IErrorCode.B20006
											+ "，phone:" + phone 
											+ "，sqUserid：" + sqUserid
											+ "，msg:" + msg
											+ "，bmtType:" + bmtType
											+ "，url:" + url);
			throw new EMPException(IErrorCode.B20006, ex);
		}
		finally
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "统计预发送条数关闭流失败。错误码：" + IErrorCode.B20006);
				}
			}
		}
		return sum;
	}
	
	/**
	 * 统计预发送条数，包括国外运营商，无绑定路由的情况下，统计提交信息总数按照和网关的规则一样，按1条统计
	 * 
	 * @param sqUserid
	 *        :发送账号
	 * @param msg
	 * @param bmtType
	 *        ：如果短信类型大于1，则传过来的值为2，否则则为1
	 * @param url
	 *        ：上传文件路径
	 * @param phone
	 * @return
	 * @throws EMPException
	 * @throws Exception
	 */
	public int countAllOprSmsNumber(String sqUserid, String msg, Integer bmtType, String url, String phone) throws EMPException
	{
		int sum = 0;
		BufferedReader reader = null;
		try
		{
			TxtFileUtil txtfileutil = new TxtFileUtil();

			// 文件发送
			if(url != null)
			{
				url = txtfileutil.getPhysicsUrl(url);
				File dist_File = new File(url);
				if(dist_File.exists() == false) return 0;
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(url), "GBK"));
			}
			else
			{
				if(phone != null)
				{
					phone = phone.replace(",", line);
					reader = new BufferedReader(new StringReader(phone));
				}
			}

			String tmp;

			switch (bmtType)
			{
				case 1:
					//运营商短信拆分条数
					int[] sendCount = getAllOprCoutnSms(sqUserid, msg);
					if(sendCount[0]+1 == 0)
					{
						EmpExecutionContext.error("统计短信发送条数失败。错误码：" + IErrorCode.B20006
												+ "，phone:" + phone 
												+ "，sqUserid：" + sqUserid
												+ "，msg:" + msg
												+ "，bmtType:" + bmtType
												+ "，url:" + url);
						throw new EMPException(IErrorCode.B20006);
					}
					while((tmp = reader.readLine()) != null)
					{
						if(tmp.length() > 1 && tmp.indexOf(",") > -1 )
						{
							tmp = tmp.substring(0, tmp.indexOf(","));
						}
						//运营商标识，移动：0；联通：1；电信:2；国外：3
						int index = getOprIndex(tmp);
						if(index != -1)
						{
							sum += sendCount[index];
						}
						continue;
					}
					reader.close();
					break;
				default:
					//根据文件内容统计短信发送条数
					sum = getCountSmsSendByFile(reader, sqUserid);
					if(sum+1 == 0)
					{
						EmpExecutionContext.error("统计短信发送条数失败。错误码：" + IErrorCode.B20006
												+ "，phone:" + phone 
												+ "，sqUserid：" + sqUserid
												+ "，msg:" + msg
												+ "，bmtType:" + bmtType
												+ "，url:" + url);
						throw new EMPException(IErrorCode.B20006);
					}
					break;
			}
		}
		catch (Exception ex)
		{
			EmpExecutionContext.error(ex, "统计预发送条数失败。错误码：" + IErrorCode.B20006
									+ "，phone:" + phone 
									+ "，sqUserid：" + sqUserid
									+ "，msg:" + msg
									+ "，bmtType:" + bmtType
									+ "，url:" + url);
			throw new EMPException(IErrorCode.B20006, ex);
		}
		finally
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "统计预发送条数关闭流失败。错误码：" + IErrorCode.B20006
											+ "，phone:" + phone 
											+ "，sqUserid：" + sqUserid
											+ "，msg:" + msg
											+ "，bmtType:" + bmtType
											+ "，url:" + url);
				}
			}
		}
		return sum;
	}
	
	/**
	 * 统计预发送条数，包括国外运营商，无绑定路由的情况下，统计提交信息总数按照和网关的规则一样，按1条统计
	 * (不同内容发送调用)
	 * 
	 * @param sqUserid
	 *        :发送账号
	 * @param msg
	 * @param bmtType
	 *        ：2:动态模板发送，3：文件内容发送
	 * @param url
	 *        ：上传文件路径
	 * @param phone
	 * @return
	 * @throws EMPException
	 * @throws Exception
	 */
	public int countAllOprSmsNumByDsm(String sqUserid, String msg, Integer bmtType, String url, String phone) throws EMPException
	{
		int sum = 0;
		BufferedReader reader = null;
		try
		{
			TxtFileUtil txtfileutil = new TxtFileUtil();

			// 文件发送
			if(url != null)
			{
				url = txtfileutil.getPhysicsUrl(url);
				File dist_File = new File(url);
				if(dist_File.exists() == false) return 0;
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(url), "GBK"));
			}
			else
			{
				if(phone != null)
				{
					phone = phone.replace(",", line);
					reader = new BufferedReader(new StringReader(phone));
				}
			}

			String tmp;

			switch (bmtType)
			{
				case 1:
					//运营商短信拆分条数
					int[] sendCount = getAllOprCoutnSms(sqUserid, msg);
					if(sendCount[0]+1 == 0)
					{
						EmpExecutionContext.error("统计短信发送条数失败。错误码：" + IErrorCode.B20006
												+ "，phone:" + phone 
												+ "，sqUserid：" + sqUserid
												+ "，msg:" + msg
												+ "，bmtType:" + bmtType
												+ "，url:" + url);
						throw new EMPException(IErrorCode.B20006);
					}
					while((tmp = reader.readLine()) != null)
					{
						if(tmp.length() > 1 && tmp.indexOf(",") > -1 )
						{
							tmp = tmp.substring(0, tmp.indexOf(","));
						}
						//运营商标识，移动：0；联通：1；电信:2；国外：3
						int index = getOprIndex(tmp);
						if(index != -1)
						{
							sum += sendCount[index];
						}
						continue;
					}
					reader.close();
					break;
				default:
					//根据文件内容统计短信发送条数
					sum = getCountSmsSendByFile(reader, sqUserid, bmtType);
					if(sum+1 == 0)
					{
						EmpExecutionContext.error("统计短信发送条数失败。错误码：" + IErrorCode.B20006
												+ "，phone:" + phone 
												+ "，sqUserid：" + sqUserid
												+ "，msg:" + msg
												+ "，bmtType:" + bmtType
												+ "，url:" + url);
						throw new EMPException(IErrorCode.B20006);
					}
					break;
			}
		}
		catch (Exception ex)
		{
			EmpExecutionContext.error(ex, "统计预发送条数失败。错误码：" + IErrorCode.B20006
									+ "，phone:" + phone 
									+ "，sqUserid：" + sqUserid
									+ "，msg:" + msg
									+ "，bmtType:" + bmtType
									+ "，url:" + url);
			throw new EMPException(IErrorCode.B20006, ex);
		}
		finally
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "统计预发送条数关闭流失败。错误码：" + IErrorCode.B20006
											+ "，phone:" + phone 
											+ "，sqUserid：" + sqUserid
											+ "，msg:" + msg
											+ "，bmtType:" + bmtType
											+ "，url:" + url);
				}
			}
		}
		return sum;
	}
	
	/**
	 * 根据文件内容统计短信发送条数
	 * @param reader
	 * @param sqUserid
	 * @return
	 */
	public int getCountSmsSendByFile(BufferedReader reader, String sqUserid)
	{
		//发送条数
		int sum = 0;
		//运营商短信条数
		int oprSmsCount;
		//运营商总数
		int oprCount = 4;
		/*中文配置参数*/
		int[] maxLens = new int[oprCount];
		int[] totalLens = new int[oprCount];
		int[] lastLens = new int[oprCount];
		int[] signLens = new int[oprCount];
		/*英文配置参数*/		
		int[] enmaxLens = new int[oprCount];
		int[] entotalLens = new int[oprCount];
		int[] enlastLens = new int[oprCount];
		int[] ensignLens = new int[oprCount];
		int[] ensinglelen = new int[oprCount];
		//是否支持英文短信
		int[] supporten = new int[oprCount];
		//签名位置:1前置;0:后置
		int[] signLocation = new int[oprCount];
		//短信内容是否为中文短信,false:英文;true:中文
		boolean isChinese = false;
		// 运营商标识，移动：0；联通：1；电信:2；国外：3
		int index = 0;
		//号码所属运营商标识,移动：0；联通：1；电信:2；国外：3
		int phoneType = 0;
		//短信内容
		String msg = "";
		//短信长度
		int len = 0;
		// 发送中文短信长度,转unicode编码，会加上BOM头，为[-2, -1]，这里长度要减去2位
		int msgLenUnicode = 0;
		//发送英文短信长度
		int enLen = 0;
		// 标示是否存在路由
		boolean[] hasRoute = new boolean[] {false,false,false,false};
		
		try {
			// 根据发送账号获取路由信息
			List<DynaBean> spGateList = this.getSpGateInfo(sqUserid);
			if(spGateList != null && spGateList.size() > 0){
				for (DynaBean spGate : spGateList)
				{
					index =Integer.parseInt(spGate.get("spisuncm").toString());
					//电信
					if(index == 21)
					{
						index = 2;
					}
					//国外通道
					else if(index == 5)
					{
						index = 3;
					}
					
					hasRoute[index] = true;
					
					//是否支持英文短信发送
					int gateprivilege = Integer.parseInt(spGate.get("gateprivilege").toString());
					if((gateprivilege&2)==2){
						supporten[index] = 1;
					}
					else{
						supporten[index] = 0;
					}
					//签名位置:1前置;0:后置
					if((gateprivilege&4)==4){
						signLocation[index] = 1;
					}
					
					// 获取英文短信最大字数
					enmaxLens[index] = Integer.parseInt(spGate.get("enmaxwords").toString());
					// 获取1到n-1条英文短信内容的长度
					entotalLens[index] = Integer.parseInt(spGate.get("enmultilen1").toString());
					// 获取最后一条英文短信内容的长度
					enlastLens[index] = Integer.parseInt(spGate.get("enmultilen2").toString());
					//英文短信签名长度
					ensignLens[index] = Integer.parseInt(spGate.get("ensignlen").toString());
					// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
					if(ensignLens[index] == 0)
					{
						//国外营运商,特殊字符按2字算
						if(index == 3)
						{
							//英文短信签名长度
							ensignLens[index] = getenSmsSignLen(spGate.get("ensignstr").toString().trim());
						}
						else
						{
							//中文短信签名长度
							ensignLens[index] = spGate.get("ensignstr").toString().trim().length();
						}
					}
					
					//单条英文短信字数
					ensinglelen[index] = Integer.parseInt(spGate.get("ensinglelen").toString());
					
					// 获取中文短信最大字数
					maxLens[index] = Integer.parseInt(spGate.get("maxwords").toString());
					// 获取1到n-1条中文短信内容的长度
					totalLens[index] = Integer.parseInt(spGate.get("multilen1").toString());
					// 获取最后一条中文短信内容的长度
					lastLens[index] = Integer.parseInt(spGate.get("multilen2").toString());
					//中文短信签名长度
					signLens[index] = Integer.parseInt(spGate.get("signlen").toString());
					// 如果设定的中文短信签名长度为0则为实际短信签名内容的长度
					if(signLens[index] == 0){
						signLens[index] = spGate.get("signstr").toString().trim().length();
					}
				}
			
			}
			
			//文件内容
			String tmp;
			while((tmp = reader.readLine()) != null)
			{
				if(tmp.length() > 1 && tmp.indexOf(",") > -1 )
				{
					String temp = tmp;
					tmp = temp.substring(0, temp.indexOf(","));
					msg = temp.substring(temp.indexOf(",")+1);
				}
				else if(tmp.length() > 11)
				{
					msg = tmp.substring(12);
					tmp = tmp.substring(0,11);
				}
				else
				{
					continue;
				}
				//重置英文短信长度
				enLen = 0;
				//是否中文短信
				isChinese = false;
				//运营商标识，移动：0；联通：1；电信:2；国外：3
				phoneType = getOprIndex(tmp);
				if(phoneType != -1)
				{
					//获取短信长度
					len = msg.length();
					//是否支持英文短信
					if(supporten[phoneType] == 1)
					{
						// 单条短信字数
						int singlelen = ensinglelen[phoneType];
						//英文短信签名长度
						int signLen = ensignLens[phoneType];
						// 英文短信内容长短信首条的长度
						int longSmsFirstLen = entotalLens[phoneType];
						//签名前置
						if(signLocation[phoneType] == 1)
						{
							longSmsFirstLen = longSmsFirstLen - signLen;
						}
						//记录英文短信短短信长度
						int enMsgShortLen  = 0;
						//字符ASCII码
						int charAscii;
						//是否为中文短信
						for(int i=0; i<len; i++)
						{
							enLen += 1;
							enMsgShortLen += 1;
							charAscii = (int)msg.charAt(i);
							//是否存在中文
							if(charAscii > 127 )
							{
								isChinese = true;
								break;
							}
							//是特殊字符
							if(StaticValue.SMSCONTENT_SPECIALCHAR.containsKey(charAscii))
							{
								//为当前长度为长短信边界值
								if(enLen % longSmsFirstLen == 0)
								{
									//条数加2
									enLen += 2;
								}
								else
								{
									//不为长短信边界值，加1
									enLen += 1;
								}
								//短短信条数加1
								enMsgShortLen += 1;
							}
						}
						//如果为短短信
						if(enMsgShortLen <= (singlelen - signLen))
						{
							enLen = enMsgShortLen;
						}
					}
					//运营商通道存在
					if(hasRoute[phoneType]){
						//英文短信并且支持英文短信
						if(!isChinese && supporten[phoneType] == 1){
							//国内通道英文短信,特殊字符按1个计算
							if(phoneType != 3)
							{
								enLen = len;
							}
							//英文短信条数计算
							oprSmsCount = englishSmsCount(enmaxLens[phoneType], entotalLens[phoneType], enlastLens[phoneType], ensignLens[phoneType], ensinglelen[phoneType], enLen);
							if(oprSmsCount == -1){
								EmpExecutionContext.error("英文短信条数计算异常!sqUserid:"+sqUserid);
								return -1;
							}
							else{
								sum += oprSmsCount;
							}
						}
						else{
							// 中文短信内容的长度
							msgLenUnicode = msg.getBytes("unicode").length - 2;
							//中文短信条数计算
							oprSmsCount = chineseSmsCount(maxLens[phoneType], totalLens[phoneType], lastLens[phoneType], signLens[phoneType], len, msgLenUnicode);
							if(oprSmsCount == -1){
								EmpExecutionContext.error("英文短信条数计算异常!sqUserid:"+sqUserid);
								return -1;
							}
							else{
								sum += oprSmsCount;
							}
						}
					}
					else{
						sum += 1;
					}
				}
			}
			return sum;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据文件内容统计短信发送条数异常!sqUserid:"+sqUserid);
			return -1;

		}
	}
	
	
	/**
	 * 根据文件内容统计短信发送条数
	 * @description    
	 * @param alarmDsmList
	 * @param sqUserid
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午03:05:10
	 */
	public int getCountDSmsSend(List<MonAlarmDsmParams> alarmDsmList, String sqUserid)
	{
		//发送条数
		int sum = 0;
		//运营商短信条数
		int oprSmsCount;
		//运营商总数
		int oprCount = 4;
		/*中文配置参数*/
		int[] maxLens = new int[oprCount];
		int[] totalLens = new int[oprCount];
		int[] lastLens = new int[oprCount];
		int[] signLens = new int[oprCount];
		/*英文配置参数*/		
		int[] enmaxLens = new int[oprCount];
		int[] entotalLens = new int[oprCount];
		int[] enlastLens = new int[oprCount];
		int[] ensignLens = new int[oprCount];
		int[] ensinglelen = new int[oprCount];
		//是否支持英文短信
		int[] supporten = new int[oprCount];
		//签名位置:1前置;0:后置
		int[] signLocation = new int[oprCount];
		//短信内容是否为中文短信,false:英文;true:中文
		boolean isChinese = false;
		// 运营商标识，移动：0；联通：1；电信:2；国外：3
		int index = 0;
		//号码所属运营商标识,移动：0；联通：1；电信:2；国外：3
		int phoneType = 0;
		//短信长度
		int len = 0;
		// 发送中文短信长度,转unicode编码，会加上BOM头，为[-2, -1]，这里长度要减去2位
		int msgLenUnicode = 0;
		//发送英文短信长度
		int enLen = 0;
		// 标示是否存在路由
		boolean[] hasRoute = new boolean[] {false,false,false,false};
		
		try {
			// 根据发送账号获取路由信息
			List<DynaBean> spGateList = this.getSpGateInfo(sqUserid);
			if(spGateList != null && spGateList.size() > 0){
				for (DynaBean spGate : spGateList)
				{
					index =Integer.parseInt(spGate.get("spisuncm").toString());
					//电信
					if(index == 21)
					{
						index = 2;
					}
					//国外通道
					else if(index == 5)
					{
						index = 3;
					}
					
					hasRoute[index] = true;
					
					//是否支持英文短信发送
					int gateprivilege = Integer.parseInt(spGate.get("gateprivilege").toString());
					if((gateprivilege&2)==2){
						supporten[index] = 1;
					}
					else{
						supporten[index] = 0;
					}
					//签名位置:1前置;0:后置
					if((gateprivilege&4)==4){
						signLocation[index] = 1;
					}
					
					// 获取英文短信最大字数
					enmaxLens[index] = Integer.parseInt(spGate.get("enmaxwords").toString());
					// 获取1到n-1条英文短信内容的长度
					entotalLens[index] = Integer.parseInt(spGate.get("enmultilen1").toString());
					// 获取最后一条英文短信内容的长度
					enlastLens[index] = Integer.parseInt(spGate.get("enmultilen2").toString());
					//英文短信签名长度
					ensignLens[index] = Integer.parseInt(spGate.get("ensignlen").toString());
					// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
					if(ensignLens[index] == 0)
					{
						//国外营运商,特殊字符按2字算
						if(index == 3)
						{
							//英文短信签名长度
							ensignLens[index] = getenSmsSignLen(spGate.get("ensignstr").toString().trim());
						}
						else
						{
							//中文短信签名长度
							ensignLens[index] = spGate.get("ensignstr").toString().trim().length();
						}
					}
					
					//单条英文短信字数
					ensinglelen[index] = Integer.parseInt(spGate.get("ensinglelen").toString());
					
					// 获取中文短信最大字数
					maxLens[index] = Integer.parseInt(spGate.get("maxwords").toString());
					// 获取1到n-1条中文短信内容的长度
					totalLens[index] = Integer.parseInt(spGate.get("multilen1").toString());
					// 获取最后一条中文短信内容的长度
					lastLens[index] = Integer.parseInt(spGate.get("multilen2").toString());
					//中文短信签名长度
					signLens[index] = Integer.parseInt(spGate.get("signlen").toString());
					// 如果设定的中文短信签名长度为0则为实际短信签名内容的长度
					if(signLens[index] == 0){
						signLens[index] = spGate.get("signstr").toString().trim().length();
					}
				}
			
			}
			//手机号
			String phone = "";
			//短信内容
			String msg = "";
			for(MonAlarmDsmParams alarmDsm:alarmDsmList)
			{
				phone = alarmDsm.getPhone();
				msg = alarmDsm.getMsg();
				
				//重置英文短信长度
				enLen = 0;
				//是否中文短信
				isChinese = false;
				//运营商标识，移动：0；联通：1；电信:2；国外：3
				phoneType = getOprIndex(phone);
				if(phoneType != -1)
				{
					//获取短信长度
					len = msg.length();
					//是否支持英文短信
					if(supporten[phoneType] == 1)
					{
						// 单条短信字数
						int singlelen = ensinglelen[phoneType];
						//英文短信签名长度
						int signLen = ensignLens[phoneType];
						// 英文短信内容长短信首条的长度
						int longSmsFirstLen = entotalLens[phoneType];
						//签名前置
						if(signLocation[phoneType] == 1)
						{
							longSmsFirstLen = longSmsFirstLen - signLen;
						}
						//记录英文短信短短信长度
						int enMsgShortLen  = 0;
						//字符ASCII码
						int charAscii;
						//是否为中文短信
						for(int i=0; i<len; i++)
						{
							enLen += 1;
							enMsgShortLen += 1;
							charAscii = (int)msg.charAt(i);
							//是否存在中文
							if(charAscii > 127 )
							{
								isChinese = true;
								break;
							}
							//是特殊字符
							if(StaticValue.SMSCONTENT_SPECIALCHAR.containsKey(charAscii))
							{
								//为当前长度为长短信边界值
								if(enLen % longSmsFirstLen == 0)
								{
									//条数加2
									enLen += 2;
								}
								else
								{
									//不为长短信边界值，加1
									enLen += 1;
								}
								//短短信条数加1
								enMsgShortLen += 1;
							}
						}
						//如果为短短信
						if(enMsgShortLen <= (singlelen - signLen))
						{
							enLen = enMsgShortLen;
						}
					}
					//运营商通道存在
					if(hasRoute[phoneType]){
						//英文短信并且支持英文短信
						if(!isChinese && supporten[phoneType] == 1){
							//国内通道英文短信,特殊字符按1个计算
							if(phoneType != 3)
							{
								enLen = len;
							}
							//英文短信条数计算
							oprSmsCount = englishSmsCount(enmaxLens[phoneType], entotalLens[phoneType], enlastLens[phoneType], ensignLens[phoneType], ensinglelen[phoneType], enLen);
							if(oprSmsCount == -1){
								EmpExecutionContext.error("英文短信条数计算异常!sqUserid:"+sqUserid);
								return -1;
							}
							else{
								sum += oprSmsCount;
							}
						}
						else{
							// 中文短信内容的长度
							msgLenUnicode = msg.getBytes("unicode").length - 2;
							//中文短信条数计算
							oprSmsCount = chineseSmsCount(maxLens[phoneType], totalLens[phoneType], lastLens[phoneType], signLens[phoneType], len, msgLenUnicode);
							if(oprSmsCount == -1){
								EmpExecutionContext.error("英文短信条数计算异常!sqUserid:"+sqUserid);
								return -1;
							}
							else{
								sum += oprSmsCount;
							}
						}
					}
					else{
						sum += 1;
					}
				}
			}
			return sum;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据内容统计短信发送条数异常!sqUserid:"+sqUserid);
			return -1;

		}
	}
	
	/**
	 * 根据文件内容统计短信发送条数，动态模板短信内容需要进行BASE64解码
	 * @description    
	 * @param reader 文件流
	 * @param sqUserid  SP账号
	 * @param bmtType 2:动态模板发送，3：文件内容发送
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-1-19 上午11:57:26
	 */
	public int getCountSmsSendByFile(BufferedReader reader, String sqUserid, Integer bmtType)
	{
		//发送条数
		int sum = 0;
		//运营商短信条数
		int oprSmsCount;
		//运营商总数
		int oprCount = 4;
		/*中文配置参数*/
		int[] maxLens = new int[oprCount];
		int[] totalLens = new int[oprCount];
		int[] lastLens = new int[oprCount];
		int[] signLens = new int[oprCount];
		/*英文配置参数*/		
		int[] enmaxLens = new int[oprCount];
		int[] entotalLens = new int[oprCount];
		int[] enlastLens = new int[oprCount];
		int[] ensignLens = new int[oprCount];
		int[] ensinglelen = new int[oprCount];
		//是否支持英文短信
		int[] supporten = new int[oprCount];
		//签名位置:1前置;0:后置
		int[] signLocation = new int[oprCount];
		//短信内容是否为中文短信,false:英文;true:中文
		boolean isChinese = false;
		// 运营商标识，移动：0；联通：1；电信:2；国外：3
		int index = 0;
		//号码所属运营商标识,移动：0；联通：1；电信:2；国外：3
		int phoneType = 0;
		//短信内容
		String msg = "";
		//短信长度
		int len = 0;
		// 发送中文短信长度,转unicode编码，会加上BOM头，为[-2, -1]，这里长度要减去2位
		int msgLenUnicode = 0;
		//发送英文短信长度
		int enLen = 0;
		// 标示是否存在路由
		boolean[] hasRoute = new boolean[] {false,false,false,false};
		
		try {
			// 根据发送账号获取路由信息
			List<DynaBean> spGateList = this.getSpGateInfo(sqUserid);
			if(spGateList != null && spGateList.size() > 0){
				for (DynaBean spGate : spGateList)
				{
					index =Integer.parseInt(spGate.get("spisuncm").toString());
					//电信
					if(index == 21)
					{
						index = 2;
					}
					//国外通道
					else if(index == 5)
					{
						index = 3;
					}
					
					hasRoute[index] = true;
					
					//是否支持英文短信发送
					int gateprivilege = Integer.parseInt(spGate.get("gateprivilege").toString());
					if((gateprivilege&2)==2){
						supporten[index] = 1;
					}
					else{
						supporten[index] = 0;
					}
					//签名位置:1前置;0:后置
					if((gateprivilege&4)==4){
						signLocation[index] = 1;
					}
					
					// 获取英文短信最大字数
					enmaxLens[index] = Integer.parseInt(spGate.get("enmaxwords").toString());
					// 获取1到n-1条英文短信内容的长度
					entotalLens[index] = Integer.parseInt(spGate.get("enmultilen1").toString());
					// 获取最后一条英文短信内容的长度
					enlastLens[index] = Integer.parseInt(spGate.get("enmultilen2").toString());
					//英文短信签名长度
					ensignLens[index] = Integer.parseInt(spGate.get("ensignlen").toString());
					// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
					if(ensignLens[index] == 0)
					{
						//国外营运商,特殊字符按2字算
						if(index == 3)
						{
							//英文短信签名长度
							ensignLens[index] = getenSmsSignLen(spGate.get("ensignstr").toString().trim());
						}
						else
						{
							//中文短信签名长度
							ensignLens[index] = spGate.get("ensignstr").toString().trim().length();
						}
					}
					
					//单条英文短信字数
					ensinglelen[index] = Integer.parseInt(spGate.get("ensinglelen").toString());
					
					// 获取中文短信最大字数
					maxLens[index] = Integer.parseInt(spGate.get("maxwords").toString());
					// 获取1到n-1条中文短信内容的长度
					totalLens[index] = Integer.parseInt(spGate.get("multilen1").toString());
					// 获取最后一条中文短信内容的长度
					lastLens[index] = Integer.parseInt(spGate.get("multilen2").toString());
					//中文短信签名长度
					signLens[index] = Integer.parseInt(spGate.get("signlen").toString());
					// 如果设定的中文短信签名长度为0则为实际短信签名内容的长度
					if(signLens[index] == 0){
						signLens[index] = spGate.get("signstr").toString().trim().length();
					}
				}
			
			}
			
			//文件内容
			String tmp;
			while((tmp = reader.readLine()) != null)
			{
				if(tmp.length() > 1 && tmp.indexOf(",") > -1 )
				{
					String temp = tmp;
					tmp = temp.substring(0, temp.indexOf(","));
					msg = temp.substring(temp.indexOf(",")+1);
				}
				else if(tmp.length() > 11)
				{
					msg = tmp.substring(12);
					tmp = tmp.substring(0,11);
				}
				else
				{
					continue;
				}
				//重置英文短信长度
				enLen = 0;
				//是否中文短信
				isChinese = false;
				//运营商标识，移动：0；联通：1；电信:2；国外：3
				phoneType = getOprIndex(tmp);
				if(phoneType != -1)
				{
					//动态模板，进行BASE64解码
					if(bmtType == 2)
					{
						msg = contentBase64Decoder(msg);
					}
					//获取短信长度
					len = msg.length();
					//是否支持英文短信
					if(supporten[phoneType] == 1)
					{
						// 单条短信字数
						int singlelen = ensinglelen[phoneType];
						//英文短信签名长度
						int signLen = ensignLens[phoneType];
						// 英文短信内容长短信首条的长度
						int longSmsFirstLen = entotalLens[phoneType];
						//签名前置
						if(signLocation[phoneType] == 1)
						{
							longSmsFirstLen = longSmsFirstLen - signLen;
						}
						//记录英文短信短短信长度
						int enMsgShortLen  = 0;
						//字符ASCII码
						int charAscii;
						//是否为中文短信
						for(int i=0; i<len; i++)
						{
							enLen += 1;
							enMsgShortLen += 1;
							charAscii = (int)msg.charAt(i);
							//是否存在中文
							if(charAscii > 127 )
							{
								isChinese = true;
								break;
							}
							//是特殊字符
							if(StaticValue.SMSCONTENT_SPECIALCHAR.containsKey(charAscii))
							{
								//为当前长度为长短信边界值
								if(enLen % longSmsFirstLen == 0)
								{
									//条数加2
									enLen += 2;
								}
								else
								{
									//不为长短信边界值，加1
									enLen += 1;
								}
								//短短信条数加1
								enMsgShortLen += 1;
							}
						}
						//如果为短短信
						if(enMsgShortLen <= (singlelen - signLen))
						{
							enLen = enMsgShortLen;
						}
					}
					//运营商通道存在
					if(hasRoute[phoneType]){
						//英文短信并且支持英文短信
						if(!isChinese && supporten[phoneType] == 1){
							//国内通道英文短信,特殊字符按1个计算
							if(phoneType != 3)
							{
								enLen = len;
							}
							//英文短信条数计算
							oprSmsCount = englishSmsCount(enmaxLens[phoneType], entotalLens[phoneType], enlastLens[phoneType], ensignLens[phoneType], ensinglelen[phoneType], enLen);
							if(oprSmsCount == -1){
								EmpExecutionContext.error("英文短信条数计算异常!sqUserid:"+sqUserid);
								return -1;
							}
							else{
								sum += oprSmsCount;
							}
						}
						else{
							// 中文短信内容的长度
							msgLenUnicode = msg.getBytes("unicode").length - 2;
							//中文短信条数计算
							oprSmsCount = chineseSmsCount(maxLens[phoneType], totalLens[phoneType], lastLens[phoneType], signLens[phoneType], len, msgLenUnicode);
							if(oprSmsCount == -1){
								EmpExecutionContext.error("英文短信条数计算异常!sqUserid:"+sqUserid);
								return -1;
							}
							else{
								sum += oprSmsCount;
							}
						}
					}
					else{
						sum += 1;
					}
				}
			}
			return sum;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据文件内容统计短信发送条数异常!sqUserid:"+sqUserid);
			return -1;

		}
	}
	
	/**
	 * 统计短信发送条数
	 * 
	 * @param sqUserid
	 *        发送账号
	 * @param msg
	 *        短信内容
	 * @param oprValidPhone
	 *        各运营商发送号码数
	 *        0:移动号码 ;1:联通号码;2:电信号码;国际号码
	 * @return SendCount 返回预发送短信条数
	 */
	public int getCountSmsSend(String sqUserid, String msg, int[] oprValidPhone)
	{
		try {
			// 预发送短信条数
			int preSendCount = 0;
			//各运营商短信发送条数
			int[] sendCount = getAllOprCoutnSms(sqUserid, msg);
			
			if(sendCount[0]+1 == 0)
			{
				EmpExecutionContext.error("统计短信发送条数失败。错误码：" + IErrorCode.B20006);
				preSendCount = -1;
				return preSendCount;
			}
			// 发送号码运营商个数
			int oprValidPhonelen = oprValidPhone.length;
			// 发送内容运营商个数
			int sendCountLen = sendCount.length;
			// 发送号码运营商个数与发送内容运营商个数相同
			if(oprValidPhonelen - sendCountLen == 0)
			{
				// 获取预发送短信条数
				for (int i = 0; i < oprValidPhonelen; i++)
				{
					// 各运营商发送号码数乘于运营商发送短信条数累加
					preSendCount += (oprValidPhone[i] * sendCount[i]);
				}
			}
			return preSendCount;
		} catch (Exception e) {
			EmpExecutionContext.error("统计短信发送条数异常。错误码：" + IErrorCode.B20006 + "，sqUserid:"+sqUserid+"，msg:"+msg);
			return -1;
		}
	}

	/**
	 * 获取各运营商信息发送条数
	 * 
	 * @param sqUserid
	 *        发送账号
	 * @param msg
	 *        短信内容
	 * @return sendCount 各运营商信息短信发送条数
	 *         数组下标说明:0:移动短信条数 ;1:联通短信条数;2:电信短信条数;3:国际号码条数
	 *         异常sendCount[0]返回-1
	 */
	public int[] getOprCoutnSms(String sqUserid, String msg)
	{
		//各运营商短信发送条数
		int[] sendCount = new int[] {1,1,1,1};
		// 发送短信长度
		int msgLen = 0;
		// 短信内容最大长度
		int maxLen;
		// 单条短信内容最大长度
		int totalLen;
		// 最后一条短信内容长度
		int lastLen;
		// 短信签名内容长度
		int signLen;
		try
		{
			// 根据发送账号获取路由信息
			List<GtPortUsed> gtPortsList = this.getPortByUserId(sqUserid);
			GtPortUsed gtPort = new GtPortUsed();
			// 运营商标识，移动：0；联通：1；电信:2
			int index = 0;
			for (int g = 0; g < gtPortsList.size(); g++)
			{
				gtPort = gtPortsList.get(g);
				// 运营商标识，移动：0；联通：1；电信:2
				index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();
				// 获取短信最大字数
				maxLen = gtPort.getMaxwords();
				// 获取1到n-1条短信内容的长度
				totalLen = gtPort.getMultilen1();
				// 获取最后一条短信内容的长度
				lastLen = gtPort.getMultilen2();
				// 如果设定的短信签名长度为0则为实际短信签名内容的长度
				signLen = gtPort.getSignlen() == 0 ? gtPort.getSignstr().trim().length() : gtPort.getSignlen();
				// 发送短信的长度
				msgLen = msg.length();

				if(msgLen <= maxLen - signLen)
				{
					msgLen = msg.getBytes("unicode").length - 2;
					if(msgLen <= (totalLen - signLen + 3) * 2) sendCount[index] = 1;
					else sendCount[index] = 1 + (msgLen - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("获取各运营商信息发送条数方法调用参数，sqUserid:" + sqUserid + "，msg:" + msg);
			EmpExecutionContext.error(e, "获取各运营商信息发送条数失败。");
			//异常sendCount[0]返回-1
			sendCount[0] = -1;
			return sendCount;
		}
		//国际号码使用移动短信拆分规则
		sendCount[3] = sendCount[0];
		return sendCount;
	}

	/**
	 * 获取各运营商信息发送条数，包括国外运营商
	 * 
	 * @param sqUserid
	 *        发送账号
	 * @param msg
	 *        短信内容
	 * @return sendCount 各运营商信息短信发送条数
	 *         数组下标说明:0:移动短信条数 ;1:联通短信条数;2:电信短信条数;3:国际号码条数
	 *         异常sendCount[0]返回-1
	 */
	public int[] getAllOprCoutnSms(String sqUserid, String msg)
	{
		//各运营商短信发送条数
		int[] sendCount = new int[] {1,1,1,1};
		//短信内容是否为中文短信,false:英文;true:中文
		boolean isChinese = false;
		try
		{
			// 发送短信长度
			int msgLen = msg.length();
			// 发送中文短信长度,转unicode编码，会加上BOM头，为[-2, -1]，这里长度要减去2位
			int msgLenUnicode = msg.getBytes("unicode").length - 2;
			//英文短信长度
			int enMsgLen = 0;
			// 根据发送账号获取路由信息
			List<DynaBean> spGateList = this.getSpGateInfo(sqUserid);
			if(spGateList != null && spGateList.size() > 0){
				
				// 运营商标识，移动：0；联通：1；电信:2；国外：3
				int index = 0;
				//短信条数
				int smsCount = 0;
				for (DynaBean spGate : spGateList)
				{
					index =Integer.parseInt(spGate.get("spisuncm").toString());
					//电信
					if(index == 21)
					{
						index = 2;
					//国外通道
					}
					else if(index == 5)
					{
						index = 3;
					}
					
					//是否支持英文短信发送
					int gateprivilege = Integer.parseInt(spGate.get("gateprivilege").toString());
					//支持英文短信
					if(!isChinese && (gateprivilege&2)==2){
						//计算英文短信长度,-1为中文短信
						enMsgLen = countEnSmsLen(spGate, index, msg);
						if(enMsgLen == -1)
						{
							isChinese = true;
						}
					}
					//短信内容为英文并且支持英文短信，使用英文拆分规则
					if(!isChinese && (gateprivilege&2)==2)
					{
						//国内通道,特殊字符还是按一个字计算
						if(index != 3)
						{
							//计算英文短信条数
							enMsgLen = msgLen;
						}
						//计算英文短信条数
						smsCount = englishSms(spGate, enMsgLen, index);
						if(smsCount != -1)
						{
							sendCount[index] = smsCount;
						}
						else
						{
							sendCount[0] = -1;
							EmpExecutionContext.error("获取各运营商信息发送条数方法调用参数，sqUserid:" + sqUserid + "，msg:" + msg);
							return sendCount;
						}
					}
					//中文短信
					else
					{
						//计算中文短信条数
						smsCount = chineseSms(spGate, msgLen, msgLenUnicode);
						if(smsCount != -1)
						{
							sendCount[index] = smsCount;
						}
						else
						{
							sendCount[0] = -1;
							EmpExecutionContext.error("获取各运营商信息发送条数方法调用参数，sqUserid:" + sqUserid + "，msg:" + msg);
							return sendCount;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("获取各运营商信息发送条数方法调用参数，sqUserid:" + sqUserid + "，msg:" + msg);
			EmpExecutionContext.error(e, "获取各运营商信息发送条数失败。");
			//异常sendCount[0]返回-1
			sendCount[0] = -1;
			return sendCount;
		}
		return sendCount;
	}
	
	/**
	 * 英文短信拆分
	 * @param spGate 通道对象
	 * @param enMsgLen 发送英文短信长度
	 * @param enMsgLen 运营商
	 * @return 短信条数
	 */
	public int englishSms(DynaBean spGate, int enMsgLen, int spisuncm)
	{
		//短信条数
		int smsCount = -1;
		try {
			// 短信内容最大长度
			int maxLen;
			// 单条短信内容最大长度
			int totalLen;
			// 最后一条短信内容长度
			int lastLen;
			// 短信签名内容长度
			int signLen;
			// 单条短信字数
			int singlelen;
			// 获取英文短信最大字数
			maxLen = Integer.parseInt(spGate.get("enmaxwords").toString());
			// 获取1到n-1条英文短信内容的长度
			totalLen = Integer.parseInt(spGate.get("enmultilen1").toString());
			// 获取最后一条英文短信内容的长度
			lastLen = Integer.parseInt(spGate.get("enmultilen2").toString());
			//英文短信签名长度
			signLen = Integer.parseInt(spGate.get("ensignlen").toString());
			// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
			if(signLen == 0)
			{
				//国外营运商,特殊字符按2字算
				if(spisuncm == 3)
				{
					//国外通道英文短信签名长度
					signLen = getenSmsSignLen(spGate.get("ensignstr").toString().trim());
				}
				else
				{
					//国内通道英文短信签名长度
					signLen = spGate.get("ensignstr").toString().trim().length();
				}
			}
			
			// 单条短信字数
			singlelen = Integer.parseInt(spGate.get("ensinglelen").toString());
			//英文短信条数计算
			smsCount = englishSmsCount(maxLen, totalLen, lastLen, signLen, singlelen, enMsgLen);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取通道英文短信配置信息异常!");
		}
		return smsCount;

	}
	
	/**
	 * 中文短信拆分
	 * @param spGate 通道对象
	 * @param msgLen 发送短信长度
	 * @param msgLenUnicode 发送短信unicode编码长度
	 * @return
	 */
	public int chineseSms(DynaBean spGate, int msgLen, int msgLenUnicode)
	{
		//短信条数
		int smsCount = -1;
		try {
			// 短信内容最大长度
			int maxLen;
			// 单条短信内容最大长度
			int totalLen;
			// 最后一条短信内容长度
			int lastLen;
			// 短信签名内容长度
			int signLen;
			// 获取中文短信最大字数
			maxLen = Integer.parseInt(spGate.get("maxwords").toString());
			// 获取1到n-1条中文短信内容的长度
			totalLen = Integer.parseInt(spGate.get("multilen1").toString());
			// 获取最后一条中文短信内容的长度
			lastLen = Integer.parseInt(spGate.get("multilen2").toString());
			//中文短信签名长度
			signLen = Integer.parseInt(spGate.get("signlen").toString());
			// 如果设定的中文短信签名长度为0则为实际短信签名内容的长度
			if(signLen == 0)
			{
				signLen = spGate.get("signstr").toString().trim().length();
			}
			//中文短信条数计算
			smsCount = chineseSmsCount(maxLen, totalLen, lastLen, signLen, msgLen, msgLenUnicode);
			
		} catch (NumberFormatException e) {
			EmpExecutionContext.error(e, "获取通道中文短信配置信息异常!");
		}
		return smsCount;
	
	}
	
	/**
	 * 英文短信条数计算
	 * @param maxLen 短信内容最大长度
	 * @param totalLen 单条短信内容最大长度
	 * @param lastLen 最后一条短信内容长度
	 * @param signLen 短信签名内容长度
	 * @param singlelen 单条短信字数
	 * @param enMsgLen 发送英文短信长度
	 * @return 短信条数
	 */
	public int englishSmsCount(int maxLen, int totalLen, int lastLen, int signLen, int singlelen, int enMsgLen)
	{
		try {
			int smsCount = 1;
			if(enMsgLen <= maxLen - signLen)
			{
				//计算短信条数
				if(enMsgLen <= (singlelen - signLen))
				{
					smsCount = 1;
				} 
				else
				{
					smsCount = 1 + (enMsgLen - lastLen + totalLen - 1) / (totalLen);
				} 
			}
			return smsCount;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "英文短信条数计算异常！");
			return -1;
		}
	}
	
	/**
	 * 中文短信条数计算
	 * @param maxLen 短信内容最大长度
	 * @param totalLen 单条短信内容最大长度
	 * @param lastLen 最后一条短信内容长度
	 * @param signLen 短信签名内容长度
	 * @param msgLen 发送短信长度
	 * @return 短信条数
	 */
	public int chineseSmsCount(int maxLen, int totalLen, int lastLen, int signLen, int msgLen, int msgLenUnicode)
	{
		try {
			int smsCount = 1;
			if(msgLen <= maxLen - signLen)
			{
				//计算短信条数
				if(msgLenUnicode <= (totalLen - signLen + 3) * 2) 
				{
					smsCount = 1;
				}
				else{
					smsCount = 1 + (msgLenUnicode - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2);
				} 
			}
			return smsCount;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "中文短信条数计算异常！");
			return -1;
		}
	}
	
	/**
	 * 获取运营商标识
	 * @param phone 号码
	 * @return 运营商标识，移动：0；联通：1；电信:2；国外：3
	 */
	public int getOprIndex(String phone)
	{
		try {
			if(phone.length()>=7 && phone.length() <=21)
			{
				if("+".endsWith(phone.substring(0, 1)) || "00".equals(phone.substring(0, 2)))
				{
					return 3;
				}
				else
				{
					//获取号码归属运营商
					int phoneType = oprNumSegmentBiz.getphoneType(phone);

					//电信
					if(phoneType == 21)
					{
						phoneType = 2;
					}
					return phoneType;
				}
			}
			else
			{
				return -1;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取运营商标识异常！phone:"+phone);
			return -1;
		}
	}
	
	/**
	 * 撤销发送任务
	 * @description    
	 * @param mt 短信任务
	 * @param subState 任务提交状态
	 * @return
	 * @throws Exception       			 
	 * @author zhangmin 
	 * @datetime 2013-10-18 下午04:14:16（修改：定时任务表关联查询由mtId改为taskId）
	 */
	public String cancelSmsTask(LfMttask mt, Integer subState) throws Exception
	{
		String returnStr = "";
		

		// 如果此条任务已撤销，则不需要在进行撤销操作
		if(mt.getSubState() == 3)
		{
			returnStr = "cancelSuccess";
			return returnStr;
		}
		// 设置状态为3（已撤消）
		mt.setSubState(subState);

		// 如果是未发送且是定时任务，则需要取消定时器中的任务
		if(mt.getSendstate() == 0 && mt.getTimerStatus() == 1)
		{
			boolean flag = empDao.update(mt);
			if(flag)
			{
				// 取消定时任务成功，则更新状态
				// smsDao.update(mt);
				cancelLfMttask(mt.getTaskId());// 取消定时信息
				returnStr = "cancelSuccess";
			}
			else
			{
				returnStr = "cancelFail";
			}
		}
		else
		{
			boolean flag = empDao.update(mt);
			if(flag)
			{
				returnStr = "cancelSuccess";
			}
			else
			{
				returnStr = "cancelFail";
			}
		}

		if("cancelSuccess".equals(returnStr))
		{
			// 2012.06.13 如果是撤消操作,则需要补回计费记录
			BalanceLogBiz b = new BalanceLogBiz();
			if(b.IsChargings(mt.getUserId()))
			{
				b.sendSmsAmountByUserId(null, mt.getUserId(), Integer.parseInt(mt.getIcount()) * -1);
			}
			// add end
		}

		return returnStr;
	}

	/**
	 * 取消定时器任务(可直接调用定时器内方法代替)
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public boolean cancelLfMttask(Long taskId) throws Exception
	{
		TaskManagerBiz taskManagerBiz = new TaskManagerBiz();
		List<LfTimer> lfTimerList = taskManagerBiz.getTaskByExpression(String.valueOf(taskId));
		LfTimer lfTimer = null;
		if(lfTimerList != null && lfTimerList.size() > 0)
		{
			lfTimer = lfTimerList.get(0);
		}
		else
		{
			return true;
		}
		// 删除定时器
		boolean flag = taskManagerBiz.stopTask(lfTimer.getTimerTaskId());
		return flag;
	}

	/**
	 * 发送模块发送账号的查找(用于富信发送)
	 * <1、首先查询操作员绑定的发送账号，若无，则查询操作员所在机构
	 * 绑定的发送账号，若无-->查询上级机构绑定的发送账号，依此类推
	 * 若顶级机构仍没有绑定发送账号，则查询全局账号（全局账号指没有绑定操作员或机构的发送账号）
	 * >
	 *
	 * @param lfSysuser
	 *        当前登录的操作员对象
	 * @return List<Userdata> 发送账号Userdata集合
	 * @throws Exception
	 */
	public List<Userdata> getSpUserList(LfSysuser lfSysuser,boolean flag) throws Exception
	{
		List<Userdata> userdatas = smsSpecialDAO.getAccountBinds(lfSysuser, false,flag);
		if(userdatas == null || userdatas.size() == 0)
		{
			List<String> distinctFieldList = new ArrayList<String>();
			distinctFieldList.add("spuserId");
			List<LfAccountBind> binds = empDao.findDistinctListBySymbolsCondition(LfAccountBind.class, distinctFieldList, null, null);
			StringBuffer bindsSpUser = new StringBuffer();
			if(binds != null && binds.size() > 0)
			{
				for (LfAccountBind lfAccountBind : binds)
				{
					bindsSpUser.append("'").append(lfAccountBind.getSpuserId()).append("',");
				}
				bindsSpUser = bindsSpUser.length() > 0 ? bindsSpUser.deleteCharAt(bindsSpUser.lastIndexOf(",")) : bindsSpUser;
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lfSysuser.getCorpCode());
			conditionMap.put("isValidate", "1");
			conditionMap.put("platFormType", "1");
			if(bindsSpUser.length() > 0)
			{
				conditionMap.put("spUser&not in ", bindsSpUser.toString());
			}
			userdatas = smsSpecialDAO.getSpDepBindWhichUserdataIsOk(conditionMap, null, null, false,true);
		}
		List<Userdata> userdatas2 = new ArrayList<Userdata>();
		// /企业快快Sp账号配置，如果为0，则排除企业快快配置的账号
		if(StaticValue.getIsContainKKSp() == 0)
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("menuCode", StaticValue.QYKKCODE);
			List<LfSubnoAllot> subnoAllots = empDao.findListByCondition(LfSubnoAllot.class, conditionMap, null);
			LfSubnoAllot subnoAllot = (subnoAllots != null && subnoAllots.size() > 0) ? subnoAllots.get(0) : null;
			String excludeSpUser = (subnoAllot != null && subnoAllot.getSpUser() != null) ? subnoAllot.getSpUser() : "";
			if(!"".equals(excludeSpUser))
			{
				for (Userdata userdata : userdatas)
				{
					if(userdata.getUserId().equals(excludeSpUser))
					{
						continue;
					}
					userdatas2.add(userdata);
				}
			}
			else
			{
				userdatas2 = userdatas;
			}
		}
		else
		{
			userdatas2 = userdatas;
		}
		return userdatas2;
	}

	/**
	 * 发送模块发送账号的查找
	 * <1、首先查询操作员绑定的发送账号，若无，则查询操作员所在机构
	 * 绑定的发送账号，若无-->查询上级机构绑定的发送账号，依此类推
	 * 若顶级机构仍没有绑定发送账号，则查询全局账号（全局账号指没有绑定操作员或机构的发送账号）
	 * >
	 * 
	 * @param lfSysuser
	 *        当前登录的操作员对象
	 * @return List<Userdata> 发送账号Userdata集合
	 * @throws Exception
	 */
	public List<Userdata> getSpUserList(LfSysuser lfSysuser) throws Exception
	{
		List<Userdata> userdatas = smsSpecialDAO.getAccountBinds(lfSysuser, false);
		if(userdatas == null || userdatas.size() == 0)
		{
			List<String> distinctFieldList = new ArrayList<String>();
			distinctFieldList.add("spuserId");
			List<LfAccountBind> binds = empDao.findDistinctListBySymbolsCondition(LfAccountBind.class, distinctFieldList, null, null);
			StringBuffer bindsSpUser = new StringBuffer();
			if(binds != null && binds.size() > 0)
			{
				for (LfAccountBind lfAccountBind : binds)
				{
					bindsSpUser.append("'").append(lfAccountBind.getSpuserId()).append("',");
				}
				bindsSpUser = bindsSpUser.length() > 0 ? bindsSpUser.deleteCharAt(bindsSpUser.lastIndexOf(",")) : bindsSpUser;
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lfSysuser.getCorpCode());
			conditionMap.put("isValidate", "1");
			conditionMap.put("platFormType", "1");
			if(bindsSpUser.length() > 0)
			{
				conditionMap.put("spUser&not in ", bindsSpUser.toString());
			}
			userdatas = smsSpecialDAO.getSpDepBindWhichUserdataIsOk(conditionMap, null, null, false);
		}
		List<Userdata> userdatas2 = new ArrayList<Userdata>();
		// /企业快快Sp账号配置，如果为0，则排除企业快快配置的账号
		if(StaticValue.getIsContainKKSp() == 0)
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("menuCode", StaticValue.QYKKCODE);
			List<LfSubnoAllot> subnoAllots = empDao.findListByCondition(LfSubnoAllot.class, conditionMap, null);
			LfSubnoAllot subnoAllot = (subnoAllots != null && subnoAllots.size() > 0) ? subnoAllots.get(0) : null;
			String excludeSpUser = (subnoAllot != null && subnoAllot.getSpUser() != null) ? subnoAllot.getSpUser() : "";
			if(!"".equals(excludeSpUser))
			{
				for (Userdata userdata : userdatas)
				{
					if(userdata.getUserId().equals(excludeSpUser))
					{
						continue;
					}
					userdatas2.add(userdata);
				}
			}
			else
			{
				userdatas2 = userdatas;
			}
		}
		else
		{
			userdatas2 = userdatas;
		}
		return userdatas2;
	}

	/**
	 * 审批提醒发送账号的查找，发送账号必须配置了上行URL
	 * <1、首先查询操作员绑定的发送账号，若无，则查询操作员所在机构
	 * 绑定的发送账号，若无-->查询上级机构绑定的发送账号，依此类推
	 * 若顶级机构仍没有绑定发送账号，则查询全局账号（全局账号指没有绑定操作员或机构的发送账号）
	 * >
	 * 
	 * @param lfSysuser
	 *        当前登录的操作员对象
	 * @return List<Userdata> 发送账号Userdata集合
	 * @throws Exception
	 */
	public List<Userdata> getSpUserListForReviewRemind(LfSysuser lfSysuser) throws Exception
	{
		// 找操作员绑定的账号
		List<Userdata> userdatas = smsSpecialDAO.getAccountBinds(lfSysuser, true);

		if(userdatas == null || userdatas.size() == 0)
		{
			List<String> distinctFieldList = new ArrayList<String>();
			distinctFieldList.add("spuserId");
			//
			List<LfAccountBind> binds = empDao.findDistinctListBySymbolsCondition(LfAccountBind.class, distinctFieldList, null, null);
			StringBuffer bindsSpUser = new StringBuffer();
			if(binds != null && binds.size() > 0)
			{
				for (LfAccountBind lfAccountBind : binds)
				{
					bindsSpUser.append("'").append(lfAccountBind.getSpuserId()).append("',");
				}
				bindsSpUser = bindsSpUser.length() > 0 ? bindsSpUser.deleteCharAt(bindsSpUser.lastIndexOf(",")) : bindsSpUser;
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lfSysuser.getCorpCode());
			conditionMap.put("isValidate", "1");
			conditionMap.put("platFormType", "1");
			if(bindsSpUser.length() > 0)
			{
				conditionMap.put("spUser&not in ", bindsSpUser.toString());
			}
			userdatas = smsSpecialDAO.getSpDepBindWhichUserdataIsOk(conditionMap, null, null, true);
		}
		List<Userdata> userdatas2 = new ArrayList<Userdata>();
		// /企业快快Sp账号配置，如果为0，则排除企业快快配置的账号
		//if(StaticValue.isContainKKSp == 0)
		if(StaticValue.getIsContainKKSp() == 0)
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("menuCode", StaticValue.QYKKCODE);
			List<LfSubnoAllot> subnoAllots = empDao.findListByCondition(LfSubnoAllot.class, conditionMap, null);
			LfSubnoAllot subnoAllot = (subnoAllots != null && subnoAllots.size() > 0) ? subnoAllots.get(0) : null;
			String excludeSpUser = (subnoAllot != null && subnoAllot.getSpUser() != null) ? subnoAllot.getSpUser() : "";
			if(!"".equals(excludeSpUser))
			{
				for (Userdata userdata : userdatas)
				{
					if(userdata.getUserId().equals(excludeSpUser))
					{
						continue;
					}
					userdatas2.add(userdata);
				}
			}
			else
			{
				userdatas2 = userdatas;
			}
		}
		else
		{
			userdatas2 = userdatas;
		}
		return userdatas2;
	}

	/**
	 * 发送账号查找
	 * <1、首先查询操作员绑定的发送账号，若无，则查询操作员所在机构
	 * 绑定的发送账号，若无-->查询上级机构绑定的发送账号，依此类推
	 * 若顶级机构仍没有绑定发送账号，则查询全局账号（全局账号指没有绑定操作员或机构的发送账号）
	 * >
	 * 
	 * @param lfSysuser
	 *        当前登录的操作员对象
	 * @return List<Userdata> 发送账号Userdata集合
	 * @throws Exception
	 */
	public List<Userdata> getSpUserListByAdmin(LfSysuser lfSysuser) throws Exception
	{
		// 找操作员绑定的账号
		List<Userdata> userdatas = smsSpecialDAO.getAccountBinds(lfSysuser, false);

		if(userdatas == null || userdatas.size() == 0)
		{
			List<String> distinctFieldList = new ArrayList<String>();
			distinctFieldList.add("spuserId");
			//
			List<LfAccountBind> binds = empDao.findDistinctListBySymbolsCondition(LfAccountBind.class, distinctFieldList, null, null);
			StringBuffer bindsSpUser = new StringBuffer();
			if(binds != null && binds.size() > 0)
			{
				for (LfAccountBind lfAccountBind : binds)
				{
					bindsSpUser.append("'").append(lfAccountBind.getSpuserId()).append("',");
				}
				bindsSpUser = bindsSpUser.length() > 0 ? bindsSpUser.deleteCharAt(bindsSpUser.lastIndexOf(",")) : bindsSpUser;
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lfSysuser.getCorpCode());
			conditionMap.put("isValidate", "1");
			conditionMap.put("platFormType", "1");
			if(bindsSpUser.length() > 0)
			{
				conditionMap.put("spUser&not in ", bindsSpUser.toString());
			}
			userdatas = smsSpecialDAO.getSpDepBindWhichUserdataIsOk(conditionMap, null, null, false);
		}
		List<Userdata> userdatas2 = new ArrayList<Userdata>();
		// /企业快快Sp账号配置，如果为0，则排除企业快快配置的账号
		//if(StaticValue.isContainKKSp == 0)
		if(StaticValue.getIsContainKKSp() == 0)
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("menuCode", StaticValue.QYKKCODE);
			List<LfSubnoAllot> subnoAllots = empDao.findListByCondition(LfSubnoAllot.class, conditionMap, null);
			LfSubnoAllot subnoAllot = (subnoAllots != null && subnoAllots.size() > 0) ? subnoAllots.get(0) : null;
			String excludeSpUser = (subnoAllot != null && subnoAllot.getSpUser() != null) ? subnoAllot.getSpUser() : "";
			if(!"".equals(excludeSpUser))
			{
				for (Userdata userdata : userdatas)
				{
					if(userdata.getUserId().equals(excludeSpUser))
					{
						continue;
					}
					userdatas2.add(userdata);
				}
			}
			else
			{
				userdatas2 = userdatas;
			}
		}
		else
		{
			userdatas2 = userdatas;
		}
		return userdatas2;
	}
	
	/**
	 * @param userdata
	 * @param moUrlNeed
	 *        1:需要上行url，2：不需要上行url
	 * @param rptUrlNeed
	 *        1:需要状态报告url，2：不需要状态报告url
	 * @return
	 *         checksuccess：检查通过，可用；checkfail:检查不通过，不可用；nouserid：账号为空；nomourl：没配置上行url
	 *         ；norpturl：没配置状态报告url；checkerror：检查异常；moconnfail:上行url检测连接失败;
	 *         rptconnfail:rpturl检测连接失败
	 */
	public String checkSpUser(Userdata userdata, Integer moUrlNeed, Integer rptUrlNeed)
	{
		try
		{
			if(userdata == null || userdata.getUserId() == null || "".equals(userdata.getUserId().trim()))
			{
				// 账号id为空
				return "nouserid";
			}

			// 验证mourl结果
			boolean moUrlResult = false;
			if(moUrlNeed == 1)
			{
				// 需要上行
				if(userdata.getMoUrl() == null || "".equals(userdata.getMoUrl().trim()))
				{
					// 账号没有上行url
					return "nomourl";
				}
				// 取出配置emp访问地址url
				String checkUrlResult = smsUtil.checkHttpUrl(userdata.getMoUrl(), 1);
				if("recsuccess".equals(checkUrlResult))
				{
					// 连通成功
					moUrlResult = true;
				}
				else
				{
					// 失败
					moUrlResult = false;
					return "moconnfail";
				}
			}
			else
			{
				// 不需要上行
				moUrlResult = true;
			}

			// 验证rpturl结果
			boolean rptUrlResult = false;
			if(rptUrlNeed == 1)
			{
				// 需要状态报告
				if(userdata.getRptUrl() == null || "".equals(userdata.getRptUrl().trim()))
				{
					// 账号没有上行url
					return "norpturl";
				}
				// 取出配置emp访问地址url
				String checkUrlResult = smsUtil.checkHttpUrl(userdata.getRptUrl(), 2);
				if("recsuccess".equals(checkUrlResult))
				{
					// 连通成功
					rptUrlResult = true;
				}
				else
				{
					// 失败
					rptUrlResult = false;
					return "rptconnfail";
				}
			}
			else
			{
				// 不需要状态报告
				rptUrlResult = true;
			}

			if(moUrlResult && rptUrlResult)
			{
				// 验证通过
				return "checksuccess";
			}
			else
			{
				return "checkfail";
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "检查sp账号是否配置可用url异常。");
			return "checkerror";
		}
	}

	/**
	 * 检查账号是否可用
	 * 
	 * @param userId
	 *        账号id
	 * @param moUrlNeed
	 *        1:需要上行url，2：不需要上行url
	 * @param rptUrlNeed
	 *        1:需要状态报告url，2：不需要状态报告url
	 * @return
	 *         checksuccess：检查通过，可用；checkfail:检查不通过，不可用；nouserid：账号为空；nomourl：没配置上行url
	 *         ；norpturl：没配置状态报告url；checkerror：检查异常；moconnfail:上行url检测连接失败;
	 *         rptconnfail:rpturl检测连接失败
	 */
	public String checkSpUser(String userId, Integer moUrlNeed, Integer rptUrlNeed)
	{
		String result = "checkerror";
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userId);
			conditionMap.put("accouttype", "1");
			List<Userdata> userdataList = empDao.findListByCondition(Userdata.class, conditionMap, null);
			if(userdataList == null || userdataList.size() == 0)
			{
				// 返回没账号错误
				return "nouserid";
			}
			Userdata userdata = userdataList.get(0);
			result = this.checkSpUser(userdata, moUrlNeed, rptUrlNeed);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "检查账号是否可用异常。");
			return "checkerror";
		}
		return result;
	}

	/**
	 * 根据userid获取账户通道配置信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<GtPortUsed> getPortByUserId(String userId) throws Exception
	{
		List<GtPortUsed> portsList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("userId", userId);
			conditionMap.put("routeFlag&<>", "2"); // 路由标志(0:上下行路由不分 1:下行路由
			// 2:上行路由)
			orderbyMap.put("spisuncm", StaticValue.ASC); // 按运营商排序
			portsList = empDao.findListBySymbolsCondition(GtPortUsed.class, conditionMap, orderbyMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取账户通道配置信息异常。userId:" + userId);
			// 异常处理
			throw e;
		}
		return portsList;
	}
	

	/**
	 * 是否有模块权限
	 * @description    
	 * @param menuNum 模块编号
	 * @return    false 无加载; true 加载    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-8 下午06:56:01
	 */
	public boolean isWyModule(String menuNum)
	{
		//是否加载网优模块,false 无加载; true 加载     
		boolean isWyModule = false;
		try
		{
			if(menuNum != null && !"".equals(menuNum))
			{
				//模块权限
				//String menu_num = StaticValue.menu_num.toString();
				String menu_num = StaticValue.getMenu_num().toString();
				if(!"".equals(menu_num) && menu_num.indexOf(",") > -1)
				{
					String [] menuModule=menu_num.split(",");
					for(String menuId : menuModule)
					{
						if(!"".equals(menuId) && menuNum.equals(menuId))
						{
							isWyModule = true;
							break;
						}
					}
					return isWyModule;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "判断是否有模块权限异常！");
		}
		return isWyModule;
	}
	
	/**
	 * 获取通道信息
	 * @param sqUserid
	 * @return
	 */
	public List<DynaBean> getSpGateInfo(String sqUserid){
		List<DynaBean> SpGateList = null;
		try {
			if(sqUserid != null && !"".equals(sqUserid)){
				SpGateList = smsSpecialDAO.getSpGateInfo(sqUserid);
			}
			return SpGateList;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取通道信息失败！");
			return null;
		}

	}
	
	/**
	 * 短信内容特殊字符替换为空格
	 * @param msg 短信内容
	 * @return 处理后的短信内容
	 */
	public String smsContentFilter(String msg)
	{
		try {
			if(msg != null && msg.length() > 0)
			{
				//短信长度
				int msgLen = msg.length();
				//短信内容字符ASCII码
				int msgChar;
				for(int i=0; i<msgLen; i++)
				{
					msgChar = (int)msg.charAt(i);
					//字符属于特殊字符，替换为空格
					if(StaticValue.SMSCONTENT_REPLACECHAR.containsKey(msgChar))
					{
						msg = msg.substring(0, i) +" "+msg.substring(i+1);
					}
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error("短信内容特殊字符替换为空格异常！");
		}
		return msg;
	}
	
	/**
	 * 短信内容是否为英文短信
	 * @param msg 短信内容
	 * @return true:是英文短信；false:不是英文短信
	 */
	public boolean isEnglishSms(String msg)
	{
		boolean englishSms = true;
		try {
			if(msg != null && msg.length() > 0)
			{
				for(int i=0; i<msg.length(); i++)
				{
					//短信内容字符ASCII码
					if((int)msg.charAt(i) > 127)
					{
						englishSms = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "判断短信内容是否为英文件短信异常！");
		}
		return englishSms;
	}
	
	/**
	 * 获取英文签名短信长度
	 * @param msg
	 * @return 英文短信签名长度
	 */
	public int getenSmsSignLen(String msg)
	{
		int englishSmsLen = 0;
		try {
			if(msg != null && msg.length() > 0)
			{
				int msgLen = msg.length();
				englishSmsLen = msgLen;
				//短信内容字符ASCII码
				int msgChar;
				for(int i=0; i<msgLen; i++)
				{
					msgChar = (int)msg.charAt(i);
					if(StaticValue.SMSCONTENT_SPECIALCHAR.containsKey(msgChar))
					{
						englishSmsLen += 1;
					}
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error("获取英文短信签名长度异常！");
		}
		return englishSmsLen;
	}
	/**
	 * 通过一次短信内容遍历,获取短信类型、特殊字符替换及英文短信长度
	 * @param msg
	 * @return 数组,下标0(0:英文短信;1:中文短信);下标1(处理后的短信内容);下标2(英文短信长度)
	 */
	public String[] getSmsContentInfo(String msg, int longSmsFirstLen, int singlelen, int signLen, int gateprivilege)
	{
		String[] smsContent = {"1", "", "0"};
		//0:英文短信;1:中文短信
		String SmsCharType = "0";
		//短信长度
		int smsLen = 0;
		//英文短短信长度
		int enMsgShortLen = 0;
		try {
			if(msg != null && msg.length() > 0)
			{
				int msgLen = msg.length();
				//短信内容字符ASCII码
				int msgChar;
				for(int i=0; i<msgLen; i++)
				{
					msgChar = (int)msg.charAt(i);
					//支持英文短信
					if(gateprivilege == 1)
					{
						//条数加1
						smsLen += 1;
						enMsgShortLen += 1;
						//存在中文字符
						if("0".equals(SmsCharType) && msgChar > 127)
						{
							SmsCharType = "1";
						}
						//计算英文短信长度
						if("0".equals(SmsCharType))
						{
							if(StaticValue.SMSCONTENT_SPECIALCHAR.containsKey(msgChar))
							{
								//为当前长度为长短信边界值
								if(smsLen % longSmsFirstLen == 0)
								{
									//条数加2
									smsLen += 2;
								}
								else
								{
									//不为长短信边界值，加1
									smsLen += 1;
								}
								//短短信条数加1
								enMsgShortLen += 1;
							}
						}
					}
					//字符属于特殊字符，替换为空格
					if(StaticValue.SMSCONTENT_REPLACECHAR.containsKey(msgChar))
					{
						msg = msg.substring(0, i) +" "+msg.substring(i+1);
					}
				}
				//不支持英文短信或短信内容为中文短信
				if(gateprivilege == 0 || "1".equals(SmsCharType))
				{
					smsLen = msgLen;
					SmsCharType = "1";
				}
				else
				{
					//如果为短短信
					if(enMsgShortLen <= (singlelen - signLen))
					{
						smsLen = enMsgShortLen;
					}
				}
				//短信类型,0:英文短信;1:中文短信
				smsContent[0] = SmsCharType;
				//处理后的短信内容
				smsContent[1] = msg;
				//英文短信长度
				smsContent[2] = String.valueOf(smsLen);
			}
		} catch (Exception e) {
			EmpExecutionContext.error("获取短信类型、特殊字符替换及英文短信长度异常！");
		}
		return smsContent;
	}
	
	/**
	 * 计算英文短信长度
	 * @param spGate
	 * @param isuncm
	 * @param msg
	 * @return 英文短信长度，-1为非英文短信
	 */
	public int countEnSmsLen(DynaBean spGate, int isuncm, String msg)
	{
		try {
			// 获取1到n-1条英文短信内容的长度
			int longSmsFirstLen = Integer.parseInt(spGate.get("enmultilen1").toString());
			// 单条短信字数
			int singlelen = Integer.parseInt(spGate.get("ensinglelen").toString());
			//英文短信签名长度
			int signLen = Integer.parseInt(spGate.get("ensignlen").toString());
			// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
			if(signLen == 0)
			{
				//国外营运商,特殊字符按2字算
				if(isuncm == 3)
				{
					//国外通道英文短信签名长度
					signLen = getenSmsSignLen(spGate.get("ensignstr").toString().trim());
				}
				else
				{
					//国内通道英文短信签名长度
					signLen = spGate.get("ensignstr").toString().trim().length();
				}
			}
			//签名位置
			int signLocation = Integer.parseInt(spGate.get("gateprivilege").toString());
			//前置
			if((signLocation&4)==4)
			{
				longSmsFirstLen = longSmsFirstLen - signLen;
			}
			
			//记录英文短信短短信长度
			int enMsgShortLen  = 0;
			//英文短信长度
			int enMsgLen = 0;
			//字符ASCII码
			int charAscii;
			//是否为中文短信
			for(int i=0; i<msg.length(); i++)
			{
				//条数加1
				enMsgLen += 1;
				enMsgShortLen += 1;
				charAscii = (int)msg.charAt(i);
				//是否中文短信
				if(charAscii > 127)
				{
					//中文短信
					return -1;
				}
				//是否特殊字符
				if(StaticValue.SMSCONTENT_SPECIALCHAR.containsKey(charAscii))
				{
					//为当前长度为长短信边界值
					if(enMsgLen % longSmsFirstLen == 0)
					{
						//条数加2
						enMsgLen += 2;
					}
					else
					{
						//不为长短信边界值，加1
						enMsgLen += 1;
					}
					//短短信条数加1
					enMsgShortLen += 1;
				}
			}
			//如果为短短信
			if(enMsgShortLen <= (singlelen - signLen))
			{
				enMsgLen = enMsgShortLen;
			}
			return enMsgLen;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "计算英文短信长度异常！msg:" + msg);
			return 0;
		}
	}
	
	/**
	 * 获取国外通道信息
	 * @param spUser
	 * @return
	 */
	public DynaBean getInterSpGateInfo(String spUser)
	{
		try {
			// 根据发送账号获取路由信息
			List<DynaBean> spGateList = getSpGateInfo(spUser);
			int index = 0;
			for(DynaBean spGate : spGateList)
			{
				//运营商标识
				index =Integer.parseInt(spGate.get("spisuncm").toString());

				//国外通道
				if(index == 5)
				{
					return spGate;
				}
			}
			return null;
		} catch (NumberFormatException e) {
			EmpExecutionContext.error(e, "获取国外通道信息异常！");
			return null;
		}
	}
	
	/**
	 * 短信内容BASE64解码
	 * @description    
	 * @param msg 短信内容
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-1-19 上午11:52:06
	 */
	public String contentBase64Decoder(String msg)
	{
		String content = "";
		try
		{
			if(msg != null && msg.trim().length() > 0)
			{
				content = new String(new BASE64Decoder().decodeBuffer(msg),"GBK");
			}
			return content;
		}
		catch (IOException e)
		{
			EmpExecutionContext.error(e, "不同内容动态模板预览统计发送条数，短信内容BASE64解码异常！msg:"+msg);
			return null;
		}
	}

    /**
     * @func 根据主表数据保存或者更新子草稿箱(短链有多余字段保存，LfDrafts保存不下)
     * @param drafts 主表LfDrafts 数据
     * @param extraParams 剩余需要保存在字表的参数
     * @return 更新子草稿箱的记录ID
     */
    public long saveSubDrafts(LfDrafts drafts, Map<String, Object> extraParams) {
        long count = 0;
        if(drafts.getId() != null && extraParams != null) {
            LfSubDrafts lfSubDrafts = new LfSubDrafts();
            Timestamp timestamp = new Timestamp(new Date().getTime());
            lfSubDrafts.setDraftId(drafts.getId());
            lfSubDrafts.setDomainId(Long.valueOf(extraParams.get("domainId").toString()));
            lfSubDrafts.setNetUrlId(Long.valueOf(extraParams.get("netUrlId").toString()));
            lfSubDrafts.setType(Integer.valueOf(extraParams.get("validays").toString()));
            lfSubDrafts.setUpdatetime(timestamp);

            boolean addFlag = (Boolean)extraParams.get("addFlag");
            try {
                if (addFlag) {
                    lfSubDrafts.setCreatetime(timestamp);
                    count = smsSpecialDAO.saveSubDrafts(lfSubDrafts);
                } else {
                    String sql = "SELECT ID,CREATE_TIME FROM LF_SUB_DRAFTS WHERE DRAFTID=" + drafts.getId();
                    List<LfSubDrafts> subDraftsList = smsSpecialDAO.findPartEntityListBySQL(LfSubDrafts.class, sql, StaticValue.EMP_POOLNAME);
                    if(subDraftsList != null && subDraftsList.size() > 0) {
                        lfSubDrafts.setId(subDraftsList.get(0).getId());
                        lfSubDrafts.setCreatetime(subDraftsList.get(0).getCreatetime());
                        boolean result = smsSpecialDAO.updateSubDrafts(lfSubDrafts);
                        if(result) {
                            count = 1;
                        }
                    } else {
                        lfSubDrafts.setCreatetime(timestamp);
                        count = smsSpecialDAO.saveSubDrafts(lfSubDrafts);
                    }
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, e.getMessage());
            }
        }
        return count;
    }

    /**
     * 获取sp账号所绑定的运营商通道信息，不存在返回null
     * @param userId sp账号
     * @param spisuncm 运营商(0-移动，1-联通，21-电信，5-国外)
     * @return
     */
	public XtGateQueue getXtGateQueue(String userId, String spisuncm) {
		String sql = "SELECT A.* FROM "+TableXtGateQueue.TABLE_NAME+" A, "+TableGtPortUsed.TABLE_NAME+" B WHERE A."+TableXtGateQueue.SPGATE+" = B."+TableGtPortUsed.SPGATE+" AND A."+TableXtGateQueue.SPISUNCM+" = B."+TableGtPortUsed.SPISUNCM+" AND A."+TableXtGateQueue.SPISUNCM+" = " + spisuncm + " AND B."+TableGtPortUsed.USER_ID+" = '" + userId + "'";
		try {
			List<XtGateQueue> list = new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(XtGateQueue.class, sql, null, new PageInfo(), StaticValue.EMP_POOLNAME);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "发送预览页面获取通道签名异常：userId=" + userId + ",spisuncm=" + spisuncm);
		}
		return null;
	}
}
