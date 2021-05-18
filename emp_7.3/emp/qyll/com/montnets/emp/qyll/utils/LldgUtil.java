package com.montnets.emp.qyll.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.dao.CommonDao;
import com.montnets.emp.qyll.entity.*;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class LldgUtil extends SuperBiz {
	TxtFileUtil txtFileUtil = new TxtFileUtil();
	private final PhoneUtil		phoneUtil	= new PhoneUtil();
	private final CommonDao commonDao = new CommonDao();
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private final DepBiz			depBiz		= new DepBiz();
	private final BlackListAtom	blackBiz	= new BlackListAtom();
	// 写文件时候要的换行符
	String					line		= StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
	/**
	 * 解析上传的文本号码
	 * @throws IOException 
	 * @throws EMPException 
	 */
	public void parsePhone(List<BufferedReader> readerList, LlPreviewParam params,String lgcorpcode,String busCode,boolean needSMS,HttpServletRequest request) throws IOException, EMPException{
		// 运营商有效号码数
		int[] oprValidPhone = params.getOprValidPhone();
		
		String []  tcStrs = params.getProductIdStrs();
		
		// 运营商标识。0:移动号码 ;1:联通号码;2:电信号码;3:国际号码
		int index = 0;
		//号码返回状态
		int resultStatus = 0;
		// 解析号码文件
		String mobile;
		// 每批次的有效号码数，在该批号码写入数据库后重置为默认值
		int perEffCount = 0;
		// 每批次的无效号码数，在该批号码写入数据库后重置为默认值
		int perBadCount = 0;
		
		
		BufferedReader reader;
		
		//有效号码文件
		File perEffFile = null;
		//无效号码文件
		File perBadFile = null;
		//合法号码文件流
		FileOutputStream perEffOS = null;
		//无效号码文件流
		FileOutputStream perBadOS = null;
		
		// 有效号码
		StringBuffer contentSb = new StringBuffer();
		// 无效号码
		StringBuffer badContentSb = new StringBuffer();
		try {
			for (int r = 0; r < readerList.size(); r++){
				reader = readerList.get(r);
				while((mobile = reader.readLine()) != null){
					params.setSubCount(params.getSubCount()+1);//提交号码数+1
					mobile = mobile.trim();
					// 去掉号码中+86前缀
					mobile = StringUtils.parseMobile(mobile);
					// 检查号码合法性和号段
					if((index = phoneUtil.getPhoneType(mobile, null)) < 0){
						
						badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_36",request)).append("：").append(mobile).append(line);//"格式非法："
						params.setBadModeCount(params.getBadModeCount() + 1);
						params.setBadCount(params.getBadCount() + 1);
						perBadCount++;
					}else if((resultStatus = phoneUtil.checkRepeat(mobile, params.getValidPhone())) != 0){
						// 1为重复号码
						if(resultStatus == 1){
							badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_35",request)).append("：").append(mobile).append(line);//"重复号码："
							params.setRepeatCount(params.getRepeatCount() + 1);
							params.setBadCount(params.getBadCount() + 1);
							perBadCount++;
						}
						//-1为非法号码
						else{
							badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_36",request)).append("：").append(mobile).append(line);
							params.setBadModeCount(params.getBadModeCount() + 1);
							params.setBadCount(params.getBadCount() + 1);
							perBadCount++;
						}
					}else if(needSMS && blackBiz.checkBlackList(lgcorpcode, mobile, busCode)){//如果需要发送短信，判断黑名单
							// 检查是否是黑名单
							badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_33",request)).append("：").append(mobile).append(line);//"黑名单号码："
							params.setBlackCount(params.getBlackCount() + 1);
							params.setBadCount(params.getBadCount() + 1);
							perBadCount++;
					}else if("-1".equals(tcStrs[index]) ){//该号码无可供选择的套餐
						badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_37",request)).append("：").append(mobile).append(line);//"无套餐号码："
						params.setNoFlowPhone(params.getNoFlowPhone()+1);
						params.setBadCount(params.getBadCount() + 1);
						perBadCount++;
					}else{
						perEffCount++;
						// 有效号码
						contentSb.append(mobile).append(",").append(index).append(line);
						params.setEffCount(params.getEffCount() + 1);
						// 累加运营商有效号码数(区分运营商)
						oprValidPhone[index] += 1;
					}
					
					// 一千条存贮一次
					if(perEffCount >= StaticValue.PER_PHONE_NUM){
						if(perEffFile == null){
							//有效号码文件
							perEffFile = new File(params.getPhoneFilePath()[0]);
							//判断文件是否存在，不存在就新建一个
							if (!perEffFile.exists()){
                                boolean flag = perEffFile.createNewFile();
                                if (!flag) {
                                    EmpExecutionContext.error("创建文件失败！");
                                }
                            }
						}
						if(perEffOS == null){
							//合法号码文件输出流
							perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
						}
						//写入有效号码文件
						txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
						contentSb.setLength(0);
						perEffCount = 0;
					}
					if(perBadCount >= StaticValue.PER_PHONE_NUM){
						if(perBadFile == null){
							//非法号码文件
							perBadFile = new File(params.getPhoneFilePath()[2]);
							//判断文件是否存在，不存在就新建一个
							if (!perBadFile.exists()){
                                boolean flag = perBadFile.createNewFile();
                                if (!flag) {
                                    EmpExecutionContext.error("创建文件失败！");
                                }
							}
						}
						if(perBadOS == null){
							//非法号码文件输出流
							perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
						}
						//写入非法号码写文件
						txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
						badContentSb.setLength(0);
						perBadCount = 0;
					}
				}
				reader.close();
			}
			params.setOprValidPhone(oprValidPhone);
			
			if(contentSb.length()>0){
				if(perEffFile == null){
					//有效号码文件
					perEffFile = new File(params.getPhoneFilePath()[0]);
					//判断文件是否存在，不存在就新建一个
					if (!perEffFile.exists()){
                        boolean flag = perEffFile.createNewFile();
                        if (!flag) {
                            EmpExecutionContext.error("创建文件失败！");
                        }
                    }
				}
				if(perEffOS == null){
					//合法号码文件输出流
					perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
				}
				//写入有效号码文件
				txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
				contentSb.setLength(0);
				perEffCount = 0;
				if(perBadFile == null){
					//非法号码文件
					perBadFile = new File(params.getPhoneFilePath()[2]);
					//判断文件是否存在，不存在就新建一个
					if (!perBadFile.exists()){
                        boolean flag = perBadFile.createNewFile();
                        if (!flag) {
                            EmpExecutionContext.error("创建文件失败！");
                        }
                    }
				}
				if(perBadOS == null){
					//非法号码文件输出流
					perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
				}
				//写入非法号码写文件
				txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
				badContentSb.setLength(0);
				perBadCount = 0;
			}
		
		} catch (EMPException e) {
			txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
			EmpExecutionContext.error(e, lgcorpcode);
			throw e;
		} catch (Exception e) {
			txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
			EmpExecutionContext.error(e, lgcorpcode);
			throw new EMPException(IErrorCode.B20005, e);
		}finally{
			if(readerList != null){
				try{
					IOUtils.closeReaders(getClass(), readerList);
				}catch(IOException e){
					EmpExecutionContext.error(e, "");
				}
				readerList.clear();
				readerList = null;
			}
			if(perEffOS != null){
				try{
					perEffOS.close();
				}
				catch (Exception e){
					EmpExecutionContext.error(e, "企业流量订购，解析文本文件流关闭有效号码文件输入流异常，lguserid:"+lgcorpcode);
				}
			}
			if(perBadOS != null){
				try{
					perBadOS.close();
				}
				catch (Exception e){
					EmpExecutionContext.error(e, "企业流量订购，解析文本文件流关闭无效号码文件输入流异常，lguserid:"+lgcorpcode);
				}
			}
			// 执行删除临时文件的操作
			cleanTempFile(params);
		
		}
	}
	
	
	/**
	 * 删除临时文件
	 * @param params
	 *        传递参数类
	 */
	private void cleanTempFile(PreviewParams params){
		try{
			// 获取待删除的文件url集合
			List<String> delFileList = params.getDelFilePath();
			if(delFileList != null && delFileList.size() > 0){
				for (String fileUrl : delFileList){
					File file = new File(fileUrl);
					// 判断文件是否存在
					if(file.exists()){
                        boolean flag = file.delete();
                        if (!flag) {
                            EmpExecutionContext.error("删除文件失败！");
                        }
                    }
				}
			}
		}
		catch (Exception e){
			EmpExecutionContext.error(e, "删除临时文件异常");
		}
	}
	
	public void parseAddrAndInputPhone(LlPreviewParam params,String phoneStr,String depIdStr,String lgcorpcode,String groupStr,String busCode,boolean needSMS,HttpServletRequest request) throws EMPException{
		try {
			String []  tcStrs = params.getProductIdStrs();
			// 运营商有效号码数
			int[] oprValidPhone = params.getOprValidPhone();
			// 运营商标识。0:移动号码 ;1:联通号码;2:电信号码;3:国际号码
			int index = 0;
			//号码返回状态
			int resultStatus = 0;
			StringBuffer contentSb = new StringBuffer();
			StringBuffer badContentSb = new StringBuffer();
			//最后一位不为","时,增加","
			if(phoneStr != null && phoneStr.length() > 0 && !",".equals(phoneStr.substring(phoneStr.length() - 1, phoneStr.length()))){
				phoneStr += ",";
			}
			
			// 解析号码字符串
			if(depIdStr != null && depIdStr.length() > 0 && !",".equals(depIdStr)){
				// 通过机构id查找电话
				phoneStr = phoneStr + depBiz.getEmpByDepId(depIdStr, lgcorpcode);
			}
			
			if(groupStr != null && groupStr.length() > 0 && !",".equals(groupStr)){
				// 通过群组查找电话
				phoneStr = phoneStr + depBiz.getEmpByGroupStr(groupStr, lgcorpcode);
			}
			
			if(phoneStr.length() <= 0){
				return;
			}
			
			String[] phones = phoneStr.split(",");
			for (String num : phones){
				params.setSubCount(params.getSubCount() + 1);
				if(num != null){
					if(num.length() >= 7 && num.length() <= 21){
						// 去掉号码中+86前缀
						num = num.trim();
						num = StringUtils.parseMobile(num);
						if((index = phoneUtil.getPhoneType(num, null)) < 0){
							// 过滤号段
							badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_36",request)).append("：").append(num).append(line);//"格式非法："
							params.setBadModeCount(params.getBadModeCount() + 1);
							params.setBadCount(params.getBadCount() + 1);
							continue;
						}else if((resultStatus = phoneUtil.checkRepeat(num, params.getValidPhone())) !=0){
							//1为重复号码
							if(resultStatus == 1){
								badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_35",request)).append("：").append(num).append(line);//"重复号码："
								params.setRepeatCount(params.getRepeatCount() + 1);
								params.setBadCount(params.getBadCount() + 1);
								continue;	
							}else{
								// 过滤号段
								badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_36",request)).append("：").append(num).append(line);//"格式非法："
								params.setBadModeCount(params.getBadModeCount() + 1);
								params.setBadCount(params.getBadCount() + 1);
								continue;
							}

						}else if(needSMS && blackBiz.checkBlackList(lgcorpcode, num, busCode)){//如果需要发送短信，判断黑名单
								// 检查是否是黑名单
								badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_33",request)).append("：").append(num).append(line);//"黑名单号码："
								params.setBlackCount(params.getBlackCount() + 1);
								params.setBadCount(params.getBadCount() + 1);
								continue;
						}else if("-1".equals(tcStrs[index]) ){//该号码无可供选择的套餐
							badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_37",request)).append("：").append(num).append(line);//"无套餐号码："
							params.setNoFlowPhone(params.getNoFlowPhone()+1);
							params.setBadCount(params.getBadCount() + 1);
							continue;
						}
						
						contentSb.append(num).append(",").append(index).append(line);
						params.setEffCount(params.getEffCount() + 1);
						
						// 累加运营商有效号码数(区分运营商)
						oprValidPhone[index] += 1;
						}else{
							badContentSb.append(MessageUtils.extractMessage("qyll","qyll_common_36",request)).append("：").append(num).append(line);//"格式非法："
							params.setBadModeCount(params.getBadModeCount() + 1);
							params.setBadCount(params.getBadCount() + 1);
							continue;
					}
				}
			}
			
			// 设置各运营商有效号码数
			params.setOprValidPhone(oprValidPhone);
	
			if(contentSb.length() > 0){
				txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
				contentSb.setLength(0);
			}
	
			if(badContentSb.length() > 0){
				txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
				badContentSb.setLength(0);
			}
			
		} catch (EMPException e) {
			EmpExecutionContext.error(e, "企业流量订购，解析手工输入和通讯录的号码异常。");
			throw e;
		
		}catch (Exception e) {
			EmpExecutionContext.error(e, "企业流量订购，解析手工输入和通讯录的号码异常。");
			throw new EMPException(IErrorCode.B20005, e);
		}finally{
			if(phoneStr != null)
			{
				phoneStr = null;
			}
		
		}
	}
	
	public void packParams(LlPreviewParam param,Map<String,String> fieldInfo){
		String []  tcStrs = param.getProductIdStrs();
		String [] proIds = param.getProIds();
		param.setProductIdStrs(tcStrs);
		param.setEffCount(0);//设置总有效号码为0
		LlProduct product = new LlProduct();
		List<LlProduct> list = new ArrayList<LlProduct>();
		
		double totalPrice = 0;
		
		for(int i = 0; i < tcStrs.length; i++){
			//套餐信息查询
			if(tcStrs[i] != null && !"".equals(tcStrs[i]) && !"-1".equals(tcStrs[i])){//移动
				list  =  commonDao.findProductByPid(String.valueOf(proIds[i]));
				if(list == null || list.size() == 0){
					continue;
				}
				product = list.get(0);
				//设置移动套餐名称
				param.getFlowNames()[i] = product.getProductname();
				
				//计算移动套餐总价格
//				totalPrice += param.getOprValidPhone()[i] * product.getDiscprice();//需计算为折扣价格
				totalPrice = add(totalPrice,mul(param.getOprValidPhone()[i] ,product.getDiscprice()));
			}else{
				//改运营商未选择套餐，将其有效号码数变为0
				param.getOprValidPhone()[i] = 0;
			}
			//重新计算有效号码数
			param.setEffCount(param.getEffCount()+param.getOprValidPhone()[i]);
		}
		
		param.setFlowSumPrice(totalPrice);
		//设置预览的短信内容
//		param.setSmsContent(fieldInfo.get("smsContent"));
	}
	
	/**
	 * 精确的加法运算
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static double add(double num1 ,double num2){
		BigDecimal b1 = new BigDecimal(Double.toString(num1));
        BigDecimal b2 = new BigDecimal(Double.toString(num2));
        return b1.add(b2).doubleValue();
	}
	
	/**
	 * 精确的乘法运算
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static double mul(double num1 ,double num2){
		BigDecimal b1 = new BigDecimal(Double.toString(num1));
        BigDecimal b2 = new BigDecimal(Double.toString(num2));
        return b1.multiply(b2).doubleValue();
	}
	
	/**
	 * 查询当前企业的流量余额
	 * @param ecid
	 * @throws Exception 
	 */
	public String findBalance() throws Exception{
		LlCompInfoVo compInfoVo = new LlCompInfoBiz().getLlCompInfoBean();
		ReqMsgHeader req = new ReqMsgHeader();
		req.setBCode("EMI1004");
		req.setAck("1");
		req.setSqId(createSqId());
		req.setECID(compInfoVo.getCorpCode());
		
		JsonObject json = new JsonObject();
		json.addProperty("CorpCode", compInfoVo.getCorpCode());
		Gson gson = new Gson();
		
		String cnxt = EncryptOrDecrypt.encryptString(json.toString(), compInfoVo.getPassword());
		req.setCnxt(cnxt);
		String returnStr = HttpUtil.sendPost("http://"+compInfoVo.getIp()+":"+compInfoVo.getPort()+"/mdgg/MdosEcHttp.hts",gson.toJson(req));
		
		ResMsgHeader res = gson.fromJson(returnStr, ResMsgHeader.class);
		cnxt = EncryptOrDecrypt.decryptString(res.getCnxt(), compInfoVo.getPassword());
		
		EMI1004Response emi1004Res = gson.fromJson(cnxt, EMI1004Response.class);
		
		return emi1004Res.getBalance();
	}
	
	/**
	 * 短信发送
	 * @param request
	 * @param response
	 * @return
	 */
	/*
	public String send(HttpServletRequest request, HttpServletResponse response){
		long startTime = System.currentTimeMillis();
		Long lguserid = null;
		String result = "";
		LfMttask mttask = new LfMttask();

		// 操作内容
		String oprInfo = "企业流量订购短信";
		// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
		String sendType = "1";

		// 当前登录操作员id
		String strlguserid = request.getParameter("lguserid");
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		// 提交类型
//		String bmtType = request.getParameter("bmtType");
		// 发送账号
		String spUser = request.getParameter("spUser");
		// 任务主题
		String title = request.getParameter("taskname");
		// 信息内容
		String msg = request.getParameter("msg");
		// 业务编码
//		String busCode = request.getParameter("busCode");
		// 是否定时
		String timerStatus = request.getParameter("timerStatus");
		// 定时时间
		String timerTime = request.getParameter("timerTime");
		// 提交状态(已提交 2，已撤销3)
		Integer subState = 2;
		// 是否需要回复
		String isReply = request.getParameter("isReply");
		// 优先级
		String priority = "5";
		// 尾号
//		String subno = request.getParameter("subNo");
		// 预览结果
		// 提交总数
		String hidSubCount = request.getParameter("hidSubCount");
		// 有效总数
		String hidEffCount = request.getParameter("hidEffCount");
		// 文件绝对路径
		String hidMobileUrl = request.getParameter("hidMobileUrl");
		// 预发送条数
		String hidPreSendCount = request.getParameter("hidPreSendCount");
        //贴尾内容
//        String tailcontents = request.getParameter("tailcontents");
		// 判断页面参数是否为空
		if(strlguserid == null || lgcorpcode == null || spUser == null 
				|| msg == null || timerStatus == null || hidSubCount == null 
				|| hidEffCount == null || hidMobileUrl == null || hidPreSendCount == null)
		{
			EmpExecutionContext.error(oprInfo + "，发送获取参数异常，" + "strlguserid:" + strlguserid 
										+ ";lgcorpcode:" + lgcorpcode 
										+ ";spUser:" + spUser 
										+ ";msg:" + msg 
										+ ";timerStatus:" + timerStatus 
										+ ";hidSubCount:" + hidSubCount 
										+ ";hidEffCount:" + hidEffCount 
										+ ";hidMobileUrl:" + hidMobileUrl 
										+ ";hidPreSendCount:" + hidPreSendCount
										+"，errCode："+IErrorCode.V10001);
			result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(result);
			return result;
		}
		
		//登录操作员信息
		LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		//登录操作员信息为空
		if(lfSysuser == null)
		{
			EmpExecutionContext.error(oprInfo + "，从session获取登录操作员信息异常。lfSysuser为null，errCode："+IErrorCode.V10001);
			result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(result);
			return result;
		}
		// 任务id
		String taskId = request.getParameter("taskId");
		try
		{
			//操作员、企业编码、SP账号检查
			boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spUser, null);
			if(!checkFlag)
			{
				EmpExecutionContext.error(oprInfo + "，检查操作员、企业编码、发送账号不通过，，taskid:"+taskId
						+ "，corpCode:"+lgcorpcode
						+ "，userid："+strlguserid
						+ "，spUser："+spUser
						+ "，errCode:"+ IErrorCode.V10001);
				result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
				EmpExecutionContext.error(result);
				return result;
			}
			//判断任务ID是否合法
			if(!BalanceLogBiz.checkNumber(taskId) || "".equals(taskId))
			{
				EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId格式非法，taskId:" + taskId 
											+ "strlguserid:" + strlguserid 
											+ "，lgcorpcode:" + lgcorpcode
											+ "，errCode："+IErrorCode.V10001);
				result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
				return result;
			}
			else
			{
				//查询任务ID在是否在lf_mttask表已使用,false:存在；true:不存在
				if(!smsSpecialDAO.checkTaskIdNotUse(Long.parseLong(taskId.trim())))
				{
					EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId已使用:" + taskId 
												+ "strlguserid:" + strlguserid 
												+ "，lgcorpcode:" + lgcorpcode
												+ "，errCode："+IErrorCode.V10001);
					result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
					return result;
				}
				//任务ID是否已在网关任务表存在,true:存在；false:不存在
				if(smsSpecialDAO.isExistTask(taskId))
				{
					EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId在网关任务表已存在，taskId:" + taskId 
												+ "，strlguserid:" + strlguserid 
												+ "，lgcorpcode:" + lgcorpcode
												+ "，errCode："+IErrorCode.V10001);
					result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
					return result;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, oprInfo + "taskId合法性校验异常 ，taskId:" + taskId 
												+ "strlguserid:" + strlguserid 
												+ ";lgcorpcode:" + lgcorpcode
												+ "，errCode："+IErrorCode.V10012);
			result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10012);
			EmpExecutionContext.error(result);
			return result;
		}
		
		try
		{
			try
			{
				lguserid = Long.valueOf(strlguserid);
				
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
//				mttask.setBmtType(Integer.valueOf(bmtType));
				// 是否定时
				Integer timerStatus1 = (timerStatus == null || "".equals(timerStatus)) ? 0 : Integer.valueOf(timerStatus);
				// 定时时间
				mttask.setTimerStatus(timerStatus1);
				// 根据发送类型判断 短信类型
				mttask.setMsgType(Integer.valueOf(sendType));
				//不同内容动态模块，设置消息编码为35
				if("2".equals(sendType))
				{
					mttask.setMsgedcodetype(35);
				}
				// 信息类型：1－短信 2－彩信
				mttask.setMsType(1);
				// 提交类型
				mttask.setSubState(subState);
				// 业务类型
//				mttask.setBusCode(busCode);
				//中文•不能识别,转为英文字符
				msg = msg.replaceAll("•", "·");
				mttask.setMsg(msg);
				// 发送状态
				mttask.setSendstate(0);
				// 企业编码
				mttask.setCorpCode(lgcorpcode);
				// 发送优先级
				mttask.setSendLevel(Integer.valueOf(priority));
				// 是否需要回复
				mttask.setIsReply(Integer.valueOf(isReply));
				// 尾号
				mttask.setSubNo("");
				// 操作员id
				mttask.setUserId(lguserid);
				
				mttask.setTaskId(Long.valueOf(taskId));
				
				// 定时任务
				if(timerStatus1 == 1)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					timerTime = timerTime + ":00";
					mttask.setTimerTime(new Timestamp(sdf.parse(timerTime).getTime()));
				}
				else
				{
					// 非定时任务
					mttask.setTimerTime(mttask.getSubmitTime());
				}

				UserdataAtom userdataAtom = new UserdataAtom();
				// 设置发送账户密码
				mttask.setSpPwd(userdataAtom.getUserPassWord(spUser));
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, strlguserid, lgcorpcode, "初始化任务对象失败", IErrorCode.V10011);
				throw new EMPException(IErrorCode.V10011, e);
			}

			try
			{
				// 获取发送信息等缓存数据（是否计费、是否审核、用户编码）
				Map<String, String> infoMap =null;
				infoMap=new CommonBiz().checkMapNull(infoMap, lguserid, lgcorpcode);
				// 创建短信任务、判断审核流、扣费、判断创建定时任务、调用网关发送
				result = smsSendaddSmsLfMttask(mttask, infoMap);
				//操作员名称
				String userName = lfSysuser.getUserName();
				// 结果
				String reultClong = result;
				// 根据错误编码从网关定义查找错误信息
				result = new WGStatus().infoMap.get(result);
				// 如果返回状态网关中未定义，则重置为之前状态
				if(result == null)
				{
					result = reultClong;
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, strlguserid, lgcorpcode, "创建短信任务失败", IErrorCode.V10012);
				throw new EMPException(IErrorCode.V10012, e);
			}
		}
		catch (Exception e)
		{
			ErrorCodeInfo errInfo = ErrorCodeInfo.getInstance();
			result = errInfo.getErrorInfo(e.getMessage());
			// 未知异常
			if(result == null)
			{
				result = errInfo.getErrorInfo(IErrorCode.V10015);
			}
			// 失败日志
			EmpExecutionContext.error(e, strlguserid, lgcorpcode, result, "");
		}
		return result;
	}*/
	
	//生成订单编号
	public  synchronized String createOrderNumber(String taskId){
		if(taskId == null){
			taskId = "";
		}
		Date date = new Date();
		
		StringBuffer sb = new StringBuffer("EMP");
		sb.append(sdf.format(date));
		sb.append(taskId);
		return sb.toString();
	}
	
	public static synchronized String createSqId(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		StringBuffer sb = new StringBuffer();
		sb.append(sdf.format(date));
		for(int i = 0; i < 8;i++){
			sb.append((int)(Math.random()*10));
		}
		return sb.toString();
	}
	
	/**
	 * 判断是否需要审核
	 */
	public String checkFlow(String userId,String corpCode,String msgType,Long count,LLOrderTask orderTask){
		LfFlow flow = null;
		String returnStr = "";
		try{
			flow = checkUserFlow(Long.parseLong(userId), corpCode, msgType, count);
		}
		catch (EMPException e){
			//不是异常，不需要记录日志，在抛出前已记录INFO日志
			String desc = ErrorCodeInfo.getInstance().getErrorInfo(e.getMessage());
		}
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			if(flow != null){
				ReviewBiz revBiz = new ReviewBiz();
				// 保存短信审批记录
				boolean saveReviewResult = revBiz.addFlowRecords(conn, Long.valueOf(orderTask.getTaskid()), orderTask.getTopic(),orderTask.getSubmittm() , Integer.parseInt(msgType), flow, Long.valueOf(orderTask.getUser_id()), "1");
				if(saveReviewResult){
					empTransDao.commitTransaction(conn);
					// 提交事务
					returnStr = "createSuccess";
				}else{
					empTransDao.rollBackTransaction(conn);
					// 运营商余额回收
					EmpExecutionContext.error("添加审核任务失败，taskid:" + orderTask.getTaskid() + ",corpCode:"+corpCode+",userId:"+userId);
					returnStr = "createFail";
				}
			}else{
				returnStr = "noFlow";
			}
		} catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			returnStr = "createFail";
		}
		return returnStr;
		
	}
	
	/**
	 * 检查操作是否绑定了审核流程
	 * 
	 * @param userId
	 *        操作员ID
	 * @param corpCode
	 *        企业编码
	 * @param infoType
	 *        信息类型
	 * @return
	 * @throws EMPException
	 */
	public LfFlow checkUserFlow(Long userId, String corpCode, String infoType, Long count) throws EMPException
	{
		ReviewBiz revBiz = new ReviewBiz();
		return revBiz.checkUserFlow(userId, corpCode,infoType, count);
	}
	
}
