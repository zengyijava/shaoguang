/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-14 下午04:11:20
 */
package com.montnets.emp.appmage.biz;


import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.appmage.dao.app_msgSendDAO;
import com.montnets.emp.appwg.bean.AppMessage;
import com.montnets.emp.appwg.biz.WgMwFileBiz;
import com.montnets.emp.appwg.initbuss.HandleMsgBiz;
import com.montnets.emp.appwg.initbuss.IInterfaceBuss;
import com.montnets.emp.common.atom.UserdataAtom;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.WGStatus;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.app.LfDfadvanced;
import com.montnets.emp.entity.appmage.LfAppMwClient;
import com.montnets.emp.entity.apptask.LfAppMttask;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * @description APP信息发送
 * @project emp_std_183
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-14 下午04:11:20
 */

public class app_msgSendBiz extends SuperBiz
{
	private final app_msgSendDAO	msgSendDAO	= new app_msgSendDAO();

	private final BaseBiz			baseBiz		= new BaseBiz();
	 

	/**
	 * APP信息发送
	 * 
	 * @description
	 * @param request
	 * @param response
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-14 下午05:08:35
	 */
	
    public String appMsgSend(HttpServletRequest request, HttpServletResponse response)
	{
		// 提交发送
		String result = "error";
		// 当前登录操作员id
		//String lgUserId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lgUserId = SysuserUtil.strLguserid(request);

		// 当前登录企业
		String lgCorpCode = request.getParameter("lgcorpcode");
		// APP发送账号(大写)
		String appAccount = request.getParameter("appAccount");
		// APP发送账号
		String baseAppAccount = request.getParameter("baseAppAccount");
		// 任务ID
		String taskIdStr = request.getParameter("taskId");
		Long taskId = 0L;
		// 发送主题
		String title = request.getParameter("title");
		// 发送客户账号
		String appCode = request.getParameter("appcode");
		// 发送客户群组
		String group = request.getParameter("group");
		// 发送APP企业账号
		String appCorpCode = request.getParameter("appcorpcode");
		// 消息类型，0-文字；1-多媒体
		String msgType = request.getParameter("sendType");
		// 消息内容
		String msg = request.getParameter("msg");
		// 上传文件地址
		String fileUrl = request.getParameter("fileUrl");
		// 本地服务器地址
		String fileLocalUrl = "";
		// 文件类型
		int fileType = 0;
		// 发送账号总数
		long appCodeCount = 0L;

		if(lgUserId == null || "".equals(lgUserId) || lgCorpCode == null || "".equals(lgCorpCode) 
				|| msgType == null || "".equals(msgType) || appAccount == null || "".equals(appAccount)
				|| baseAppAccount == null || "".equals(baseAppAccount))
		{
			EmpExecutionContext.error("APP消息发送服务器接收页面请求参数异常，lguserid：" + lgUserId + "，lgcorpcode：" + lgCorpCode + "，msgType:" + msgType+ "，appAccount：" + appAccount + "，baseAppAccount:" + baseAppAccount);
			result = "APP消息发送服务器接收页面请求参数失败！";
			return result;
		}
		if(taskIdStr == null || "".equals(taskIdStr))
		{
			taskId = new CommonBiz().getAvailableTaskId();
		}
		else
		{
			taskId = Long.parseLong(taskIdStr);
		}
		EmpExecutionContext.info("APP消息发送任务（任务名称："+ title+"）发送获取taskid：" + taskId +"，lguserid：" + lgUserId + "，lgcorpcode：" + lgCorpCode);
		// 发送对象
		LfAppMttask appMttask = new LfAppMttask();
		// 任务更新状态
		boolean updateStates = false;
		try
		{
			// 文字发送，消息内容不能为空
			if("0".equals(msgType))
			{
				if(msg == null || "".equals(msg))
				{
					result = "请输入发送内容！";
					return result;
				}
				else
				{
					msg = msg.replaceAll("•", "·").replace("¥", "￥");
				}
			}
			// 多媒体发送，上传文件地址不能为空
			else
			{
				if((fileUrl == null || "".equals(fileUrl)))
				{
					result = "上传多媒体文件地址错误！";
					return result;
				}
				else
				{
					// 获取文件类型
					fileType = getFileType(fileUrl);
					if(fileType == -1)
					{
						result = "上传多媒体文件格式错误！";
						return result;
					}
					fileLocalUrl = fileUrl.substring(fileUrl.indexOf("/file/") + 1, fileUrl.length());
					// 多媒体发送,清空消息内容
					msg = "";
				}
			}

			// 获取APP企业发送客户账号
			if(appCorpCode != null && appCorpCode.length() > 0 && !",".equals(appCorpCode))
			{
				// 去掉前面和最后一个逗号
				appCorpCode = appCorpCode.substring(1, appCorpCode.length() - 1);
				appCode += getAccountByAppCorpCode(appCorpCode);
			}
			// 获取APP客户群组发送客户账号
			if(group.length() > 0 && !",".equals(group))
			{
				// 去掉前面和最后一个逗号
				group = group.substring(1, group.length() - 1);
				// 通过机构id查找电话
				appCode += getAccountByGroup(group, appAccount);
			}
			if(appCode == null || appCode.length() < 1 || ",".equals(appCode))
			{
				result = "请选择发送对象！";
				return result;
			}
			// 有效的APP客户号
			LinkedHashSet<String> validPhoneSet = getValidAppCode(appCode);
			// 总数
			appCodeCount = validPhoneSet.size();
			if(appCodeCount < 1)
			{
				result = "没有有效的发送对象！";
				return result;
			}
			// 文件服务器地址
			String fileServiceUrl = "";
			// 多媒体消息发送,上传多媒体文至文件服务器
			if("1".equals(msgType))
			{
				// 文件上传
				fileServiceUrl = uploadFileToService(fileUrl, fileType, baseAppAccount);
				if(fileServiceUrl == null || fileServiceUrl.indexOf(".") == -1)
				{
					result = "上传多媒体文件失败！";
					EmpExecutionContext.error("APP消息发送,上传多媒体文件至文件服务器异常!返回编码:" + fileServiceUrl);
					return result;
				}
			}
			appMttask.setAppacount(baseAppAccount);
			appMttask.setBigintime(new Timestamp(System.currentTimeMillis()));
			appMttask.setCorpcode(lgCorpCode);
			appMttask.setMsg(msg);
			appMttask.setMsgtype(fileType);
			appMttask.setMsgurl(fileServiceUrl);
			appMttask.setMsglocalurl(fileLocalUrl);
			appMttask.setSendstate(0);
			appMttask.setSubcount(appCodeCount);
			appMttask.setTaskid(taskId);
			appMttask.setTitle(title);
			appMttask.setUserid(Long.parseLong(lgUserId));
			// 保存任务
			updateStates = baseBiz.addObj(appMttask);
			if(!updateStates)
			{
				result = "发送失败，更新任务信息出现异常！";
				EmpExecutionContext.error("APP信息发送更新任务表失败，lguserid：" + lgUserId + "，lgcorpcode：" + lgCorpCode);
				return result;
			}

			// 调用APP发送接口
			AppMessage AppMessage = new AppMessage();
			AppMessage.setTaskId(appMttask.getTaskid());
			AppMessage.setEcode(appMttask.getAppacount());
			AppMessage.setFromUserName(appMttask.getAppacount());
			AppMessage.setToUserNameSet(validPhoneSet);
			AppMessage.setToType(2);
			AppMessage.setMsgType(fileType);
			// 音频或视频时长
			long duration = -1;
			if("1".equals(msgType))
			{
				File file = new File(fileUrl);
				if(fileType == 2)
				{
					duration = getVideoTime(file);
				}
				else if(fileType == 3)
				{
					duration = getAmrDuration(file);
				}
				if(fileType !=1 && duration == -1)
				{
					EmpExecutionContext.error("APP消息发送获取音频或视频时长异常!fileUrl:" + fileUrl);
				}
			}
			AppMessage.setTime(duration);
			AppMessage.setTitle(appMttask.getTitle());
			AppMessage.setMsgContent(appMttask.getMsg());
			AppMessage.setUrl(fileServiceUrl);
			// AppMessage.setValidity(validity);
			AppMessage.setUserId(Long.parseLong(lgUserId));
			IInterfaceBuss wgbizBiz = new HandleMsgBiz();
			// 发送消息
			Integer reStatus = wgbizBiz.SendAppPublicMsg(AppMessage);
			EmpExecutionContext.info("APP消息发送调用接口返回状态:"+reStatus+"，发送总数："+appCodeCount+"，taskid：" + taskId + "lguserid：" + lgUserId + "，lgcorpcode：" + lgCorpCode);
			// 设置发送状态
			if(reStatus == null)
			{
				result = "向APP平台发送请求失败！";
				appMttask.setSendstate(2);
				EmpExecutionContext.error("提交APP消息失败，返回状态信息：" + reStatus + "，lgUserId:" + lgUserId + "，lgCorpCode:" + lgCorpCode);
			}
			else
			{
				if(reStatus == 1)
				{
					result = "000";
					appMttask.setSendstate(reStatus);
				}
				else
				{
					result = "向APP平台发送请求失败，" + new appImpResultStatus().resultInfoMap.get(reStatus);
					appMttask.setSendstate(2);
					EmpExecutionContext.error("提交APP消息失败，返回状态信息：" + reStatus + "，lgUserId:" + lgUserId + "，lgCorpCode:" + lgCorpCode);
				}
			}

			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.error("模块名称：APP信息发送，企业："+loginSysuser.getCorpCode()+"，"
				+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
				+"(APP信息发送：发送主题："+title+")成功。");
			}
			return result;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "APP信息发送异常！lgUserId:" + lgUserId + "，lgCorpCode:" + lgCorpCode);
			return result;
		}
		finally
		{
			if(updateStates)
			{
				// 更新任务状态
				for (int i = 0; i < 4; i++)
				{
					// 更新失败,重复更新,最多三次
					if(updateAppMttaskByTaskid(appMttask, i))
					{
						break;
					}
				}
			}
		}
	}

	/**
	 * 获取APP企业编码
	 * 
	 * @description
	 * @param corpcode
	 *        企业编码
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-14 下午05:08:21
	 */
	
    public List<DynaBean> getAppCorpCode(String corpcode)
	{
		return msgSendDAO.getAppCorpCode(corpcode);
	}

	/**
	 * 获取客户群组及未分组发送客户账号
	 * 
	 * @description
	 * @param group
	 *        APP客户群组
	 * @param appAccount
	 *        APP企业编码
	 * @return 客户发送账号
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-14 下午05:09:10
	 */
	
    public String getAccountByGroup(String group, String appAccount)
	{
		// 发送客户账号
		StringBuffer account = new StringBuffer("");
		// 客户群组发送客户账号
		String groupAccount = msgSendDAO.getAccountByGroup(group);
		if(groupAccount != null && !"".equals(groupAccount))
		{
			account.append(groupAccount);
		}
		// 未分组发送客户账号
		if(group.indexOf("-1") >= 0)
		{
			List<DynaBean> noGroupAccountList = msgSendDAO.getNoGroupAppCount(appAccount);
			if(noGroupAccountList != null && noGroupAccountList.size() > 0)
			{
				for (DynaBean noGroupAccount : noGroupAccountList)
				{
					account.append(noGroupAccount.get("account").toString()).append(",");
				}
			}
		}
		return account.toString();
	}

	/**
	 * 获取APP企业发送客户账号
	 * 
	 * @description
	 * @param appCorpCode
	 *        APP企业编码
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-14 下午05:09:10
	 */
	
    public String getAccountByAppCorpCode(String appCorpCode)
	{
		return msgSendDAO.getAccountByAppCorpCode(appCorpCode);
	}

	/**
	 * 上传文件至服务器
	 * 
	 * @description
	 * @param fileUrl
	 *        上传文件地址
	 * @param fileType
	 *        文件类型
	 * @param appAccount
	 *        APP企业编码
	 * @return 上传成功返回文件服务器地址,失败返回错误编码
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-14 下午05:14:40
	 */
	private String uploadFileToService(String fileUrl, int fileType, String appAccount)
	{
		String fileServiceUrl = "error";
		String fileTypeStr = "0";
		try
		{
			// 文件类型转为接口定义的类型，10：图片；11：语音；12：视频；13：普通文件
			if(fileType == 1)
			{
				fileTypeStr = "10";
			}
			else if(fileType == 2)
			{
				fileTypeStr = "12";
			}
			else if(fileType == 3)
			{
				fileTypeStr = "11";
			}
			// 上传文件至文件服务器
			fileServiceUrl = new WgMwFileBiz().uploadToMwFileSer(fileUrl, fileTypeStr, appAccount);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "APP消息发送,上传多媒体文至文件服务器异常! appAccount:" + appAccount + "，fileUrl:" + fileUrl);
			fileServiceUrl = "error";
		}
		return fileServiceUrl;
	}

	/**
	 * 获取文件类型
	 * 
	 * @description
	 * @param fileUrl
	 *        文件路径
	 * @return 文件类型,-1:异常文件;0：文本消息；1：图片消息；2：视频；3：语音；4：事件推送
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-14 下午05:34:44
	 */
	private int getFileType(String fileUrl)
	{
		int fileType = 0;
		try
		{
			String fileSuffix = fileUrl.substring(fileUrl.lastIndexOf(".")).toUpperCase();
			if(fileSuffix == null || "".equals(fileSuffix))
			{
				fileType = -1;
			}
			String validFileType = ".JPG.JPEG.PNG.BMP.AMR.3GP.MP4";
			if(validFileType.indexOf(fileSuffix) < 0)
			{
				return -1;
			}
			if(".3GP".equals(fileSuffix) || ".MP4".equals(fileSuffix))
			{
				fileType = 2;
			}
			else if(".AMR".equals(fileSuffix))
			{
				fileType = 3;
			}
			else
			{
				fileType = 1;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("获取APP发送文件类型异常，fileUrl：" + fileUrl);
			fileType = -1;
		}

		return fileType;
	}

	/**
	 * 获取客户群组信息
	 * 
	 * @description
	 * @param corpcode
	 *        企业编码
	 * @return 客户群组列表
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-17 上午10:09:09
	 */
	
    public List<DynaBean> getAppGroupList(String corpcode)
	{
		return msgSendDAO.getAppGroupList(corpcode);
	}

	/**
	 * 获取未分组客户数
	 * 
	 * @description
	 * @param appAccount
	 *        APP企业编码
	 * @return 未分组客户数
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-17 上午10:09:09
	 */
	
    public List<DynaBean> getNoGroupAppCount(String appAccount)
	{
		return msgSendDAO.getNoGroupAppCount(appAccount);
	}

	/**
	 * 获取APP客户信息
	 * 
	 * @description
	 * @param chooseType
	 *        类型：1：群组；2：企业
	 * @param id
	 *        群组ID或企业ID
	 * @param appAccount
	 *        APP企业编码
	 * @param pageInfo
	 *        分页对象
	 * @return APP成员列表
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-17 下午08:19:35
	 */
	
    public List<LfAppMwClient> getAppMembersList(String chooseType, String id, String appAccount, PageInfo pageInfo)
	{
		return msgSendDAO.getAppMembersList(chooseType, id, appAccount, pageInfo);
	}

	/**
	 * 获取所有群组或所有企业下的APP发送客户信息
	 * 
	 * @description
	 * @param appAccount
	 *        APP企业编码
	 * @param chooseType
	 *        类型：1：群组；2：企业
	 * @param epname
	 *        姓名
	 * @param pageInfo
	 *        分页对象
	 * @return APP成员列表
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-17 下午08:19:35
	 */
	
    public List<LfAppMwClient> getAllAppMembersList(String appAccount, String chooseType, String epname, PageInfo pageInfo)
	{
		return msgSendDAO.getAllAppMembersList(appAccount, chooseType, epname, pageInfo);
	}

	/**
	 * 获取有效APP客户
	 * 
	 * @description
	 * @param appCode
	 * @return 有效APP客户号
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-18 上午11:00:53
	 */
	
    public LinkedHashSet<String> getValidAppCode(String appCode)
	{
		// 有效号码集合
		LinkedHashSet<String> validPhoneSet = new LinkedHashSet<String>();
		String[] appCodeTemp = appCode.split(",");
		for (String code : appCodeTemp)
		{
			if(validCheck(code) == 1)
			{
				validPhoneSet.add(code);
			}
		}
		// 有效APP客户号
		return validPhoneSet;
	}

	/**
	 * 获取错误APP客户数
	 * 
	 * @description
	 * @param appCode
	 * @return 错误APP客户数
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-18 上午11:00:53
	 */
	
    public String getAppCodeFailCount(String appCode)
	{
		// 有效号码集合
		HashSet<String> validPhoneSet = new HashSet<String>();
		String[] appCodeTemp = appCode.split(",");
		// 格式非法
		int illegal = 0;
		for (String code : appCodeTemp)
		{
			if(validCheck(code) == 1)
			{
				validPhoneSet.add(code);
			}
			else
			{
				illegal++;
			}
		}
		// 错误的客户号
		return validPhoneSet.size() + "@@" + illegal;
	}

	/**
	 * 合法性校验
	 * 
	 * @description
	 * @param appCode
	 *        APP客户号
	 * @return 0:非法;1:合法
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-18 上午10:53:06
	 */
	
    public int validCheck(String appCode)
	{
		// 客户号长度
		int len = 0;
		try
		{
			if(appCode == null)
			{
				return 0;
			}
			else
			{
				len = appCode.length();
				if(len < 1 || len > 25)
				{
					return 0;
				}
			}
			// for (int k = len; --k >= 0;)
			// {
			// char single = appCode.charAt(k);
			// //增加根据Unicode编码判断是否为0-9的数字
			// if (!Character.isDigit(single) || (int)single > 57 || (int)single
			// < 48)
			// {
			// return 0;
			// }
			// }
			return 1;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("APP客户号合法性校验异常!appCode:" + appCode);
			return 0;
		}
	}

	/**
	 * 更新APP发送任务表
	 * 
	 * @description
	 * @param
	 *
	 * @return ture 更新成功;false 更新失败
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-18 上午11:44:54
	 */
	
    public boolean updateAppMttaskByTaskid(LfAppMttask appMttask, int updateTimes)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();

		conditionMap.put("taskid", appMttask.getTaskid().toString());
		objectMap.put("sendstate", appMttask.getSendstate().toString());
		try
		{
			return empDao.update(LfAppMttask.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新APP发送任务表异常!更新次数：" + updateTimes + "，taskId:" + appMttask.getTaskid());
			return false;
		}
	}

	/**
	 * 获取AMR音频时长
	 * @description    
	 * @param file 音频文件绝对路径
	 * @return
	 * @throws IOException       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-7 下午07:58:27
	 */
	public long getAmrDuration(File file)
	{
		long duration = -1;
		int[] packedSize = {12,13,15,17,19,20,26,31,5,0,0,0,0,0,0,0};
		RandomAccessFile randomAccessFile = null;
		try
		{
			randomAccessFile = new RandomAccessFile(file, "rw");
			long length = file.length();// 文件的长度
			int pos = 6;// 设置初始位置
			int frameCount = 0;// 初始帧数
			int packedPos = -1;
			// ///////////////////////////////////////////////////
			byte[] datas = new byte[1];// 初始数据值
			while(pos <= length)
			{
				randomAccessFile.seek(pos);
				if(randomAccessFile.read(datas, 0, 1) != 1)
				{
					duration = length > 0 ? ((length - 6) / 650) : 0;
					break;
				}
				packedPos = (datas[0] >> 3) & 0x0F;
				pos += packedSize[packedPos] + 1;
				frameCount++;
			}
			// ///////////////////////////////////////////////////
			duration += frameCount * 20;// 帧数*20
			//单位为秒
			duration = duration / 1000;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取AMR音频时长异常！file:" + file);
		}
		finally
		{
			if(randomAccessFile != null)
			{
				try
				{
					randomAccessFile.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "获取AMR音频时长关闭流异常！file:" + file);
				}
			}
		}
		return duration;
	}

	/**获取3GP视频长时
	 * @description
	 * @param file 视频文件绝对路径
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-7 下午07:51:24
	 */
	public long getVideoTime(File file)
	{
		long timeStr = -1;
		if(file.exists())
		{
			try
			{
				Encoder encoder = new Encoder();
				MultimediaInfo mInfo = encoder.getInfo(file);
				//单位为秒
				timeStr = mInfo.getDuration() / 1000;
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取视频长时异常！file:" + file);
			}
		}
		return timeStr;
	}
	
	
	
	/**
	 *  APP消息短信发送
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-29 上午11:33:18
	 */
	public String appSmsSend(HttpServletRequest request, HttpServletResponse response)
	{
		// 消息类型，0-文字；1-多媒体
		String msgType = request.getParameter("sendType");
		//是否同时发送短信
		String isSmsSend = request.getParameter("hidisSmsSend");
		String result = "";
		if(msgType != null && "0".equals(msgType) && isSmsSend != null && "1".equals(isSmsSend))
		{
			// 当前登录操作员id
			//String lgUserIdStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lgUserIdStr = SysuserUtil.strLguserid(request);

			// 当前登录企业
			String lgCorpCode = request.getParameter("lgcorpcode");
			// 发送账号
			String spUser = request.getParameter("spUser");
			// 发送主题
			String title = request.getParameter("title");
			// 消息内容
			String msg = request.getParameter("msg") != null? request.getParameter("msg"):"";
			// 业务编码
			String busCode = request.getParameter("busCode");
			// 尾号
			String subno = GlobalVariableBiz.getInstance().getValidSubno("10930", 0, lgCorpCode, new ErrorCodeParam());
			// 预览结果
			// 提交总数
			String hidSubCount = request.getParameter("hidSubCount");
			// 有效总数
			String hidEffCount = request.getParameter("hidEffCount");
			// 文件绝对路径
			String hidMobileUrl = request.getParameter("hidMobileUrl");
			// 预发送条数
			String hidPreSendCount = request.getParameter("hidPreSendCount");
			// 任务ID
			String taskIdStr = request.getParameter("taskId");
			Long taskId = 0L;
			if(taskIdStr == null || "".equals(taskIdStr))
			{
				taskId = new CommonBiz().getAvailableTaskId();
			}
			else
			{
				taskId = Long.parseLong(taskIdStr);
			}
			// 判断页面参数是否为空
			if(lgUserIdStr == null || lgCorpCode == null || spUser == null 
					|| msg == null || hidSubCount == null 
					|| hidEffCount == null || hidMobileUrl == null || hidPreSendCount == null)
			{
				EmpExecutionContext.error("APP消息短信发送获取参数异常，" + "lgUserId:" + lgUserIdStr + ";lgCorpCode:" 
						+ lgCorpCode + ";spUser:" + spUser + ";msg:" + msg + ";hidSubCount:" + hidSubCount + ";hidEffCount:" 
						+ hidEffCount + ";hidMobileUrl:" + hidMobileUrl + ";hidPreSendCount:" + hidPreSendCount);
			}
			EmpExecutionContext.info("APP消息短信发送（任务名称：" + title + "）发送获取taskid：" + taskId +"，操作员：" + lgUserIdStr + "，企业编码：" + lgCorpCode + "，提交总数："+hidSubCount+"，有效号码："+hidEffCount);

			LfMttask mttask = new LfMttask();
			
			Long lguserid = Long.valueOf(lgUserIdStr);

			// 初始化任务对象
			// 提交总数
			mttask.setSubCount(Long.valueOf(hidSubCount));
			// 有效总数
			mttask.setEffCount(Long.valueOf(hidEffCount));
			// 文件绝对路径
			mttask.setMobileUrl(hidMobileUrl);
			// 预发送条数
			mttask.setIcount(hidPreSendCount);
			// 任务主题
			mttask.setTitle(title);
			// sp账号
			mttask.setSpUser(spUser);
			// 提交类型
			mttask.setBmtType(1);
			// 定时时间
			mttask.setTimerStatus(0);
			// 根据发送类型判断 短信类型
			mttask.setMsgType(1);
			// 信息类型：1－短信 2－彩信
			mttask.setMsType(8);
			// 提交类型
			mttask.setSubState(2);
			// 业务类型
			mttask.setBusCode(busCode);
			//中文•不能识别,转为英文字符
			mttask.setMsg(msg == null ? "" : msg.replaceAll("•", "·").replace("¥", "￥"));
			// 发送状态
			mttask.setSendstate(0);
			// 企业编码
			mttask.setCorpCode(lgCorpCode);
			// 发送优先级
			mttask.setSendLevel(0);
			// 是否需要回复
			mttask.setIsReply(0);
			// 尾号
			mttask.setSubNo(subno);
			// 操作员id
			mttask.setUserId(lguserid);
			if(taskId != null)
			{
				mttask.setTaskId(taskId);
			}
			mttask.setTimerTime(mttask.getSubmitTime());
			UserdataAtom userdataAtom = new UserdataAtom();
			// 设置发送账户密码
			mttask.setSpPwd(userdataAtom.getUserPassWord(spUser));
			try
			{
				// 获取发送信息等缓存数据（是否计费、是否审核、用户编码）
				Map<String, String> infoMap =null;
				infoMap=new CommonBiz().checkMapNull(infoMap, lguserid, lgCorpCode);
				// 创建短信任务、判断审核流、扣费、判断创建定时任务、调用网关发送
				result = new SmsSendBiz().addSmsLfMttask(mttask, infoMap);
				EmpExecutionContext.info("APP消息短信发送（任务名称：" + title + "）发送获取taskid：" + taskId +"，提交网关返回状态：" + result);
				// 结果
				String reultClong = result;
				String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				// 根据错误编码从网关定义查找错误信息
				result = new WGStatus(langName).getInfoMap().get(result);
				// 如果返回状态网关中未定义，则重置为之前状态
				if(result == null)
				{
					result = reultClong;
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, lgUserIdStr, lgCorpCode, "创建短信任务失败", IErrorCode.V10012);
				return result;
			}
		}
		return result;
	}
	
	/**
	 * 高级设置存为默认
	 * @param conditionMap 删除原来设置条件
	 * @param lfDfadvanced 更新默认高级设置对象
	 * @return
	 */
	public String setDefault(LinkedHashMap<String, String> conditionMap, LfDfadvanced lfDfadvanced){
		String result = "fail";
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			
			//删除原有的设置
			empTransDao.delete(conn, LfDfadvanced.class, conditionMap);
			
			//新增默认高级设置信息
			boolean saveResult = empTransDao.save(conn, lfDfadvanced);
			
			//成功
			if(saveResult){
				result = "seccuss";
				empTransDao.commitTransaction(conn);
			}
			else{
				empTransDao.rollBackTransaction(conn);
			}
			return result;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "高级设置存为默认异常！");
			empTransDao.rollBackTransaction(conn);
			return result;
		}
		finally{
			// 关闭连接
			if(conn != null){
				empTransDao.closeConnection(conn);
			}
		}
	}
}
