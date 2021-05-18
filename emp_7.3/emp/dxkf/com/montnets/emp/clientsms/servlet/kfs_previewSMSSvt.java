package com.montnets.emp.clientsms.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.montnets.emp.clientsms.biz.ClientSmsBiz;
import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.TxtFileUtil;

@SuppressWarnings("serial")
public class kfs_previewSMSSvt extends BaseServlet
{
	private final BalanceLogBiz	balancebiz		= new BalanceLogBiz();

	private final SmsBiz			smsBiz			= new SmsBiz();

	private final ClientSmsBiz	clientSmsBiz	= new ClientSmsBiz();

	private static String	PATH			= "/dxkf/clientsms";

	private final TxtFileUtil		txtfileutil		= new TxtFileUtil();

	
	private final WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();
	
	/**
	 * 预览方法
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void preview(HttpServletRequest request, HttpServletResponse response)
	{
		String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
		//返回结果字符串
		String result = "";

		// SP账号
		String spUser = request.getParameter("spUser");
		// 业务类型
		String busCode = request.getParameter("busCode");
		//操作员ID
		//String lguserid=request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 客户群组id集合
		String groupStr = request.getParameter("groupStr");
		//签约套餐id集合
		String ydywGroupStr=request.getParameter("ydywGroupStr");
		// 客户机构id集合
		String depIdStr = request.getParameter("depIdStr");
		// 客户属性id集合
		String proIdStr = request.getParameter("proIdStr");
		// 客户属性值id集合
		String proValueIdStr = request.getParameter("proValueIdStr");
		
		// 高级搜索参数
		// 选择状态,是否全选,true是全选，false是未全选
		String selectAllStatus = request.getParameter("selectAllStatus");
		// 未全选时的手机号码
		String phoneStr12 = request.getParameter("phoneStr12");
		// 全选时的未选中客户ID
		String unChioceUserIds = request.getParameter("unChioceUserIds");
		// 全选时的查询条件
		String conditionsqlTemp = request.getParameter("conditionsqlTemp");
		// 发送内容
		String msg = request.getParameter("msg");

		// 预览参数传递变量类
		PreviewParams preParams = new PreviewParams();
        //草稿箱文件相对路径
        String draftFilePath = request.getParameter("containDraft");
        //处理预览内容带上贴尾
//        String tailcontents = new SmsSendBiz().getTailContents(busCode,spUser,corpCode);
        String tailcontents = request.getParameter("tailcontents");
        if(tailcontents != null){
            msg += tailcontents;
        }
		try
		{
			//判断发送的sp账号是否为该企业可用的sp账号
			LfSysuser checkSysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(!new CheckUtil().checkSysuserInCorp(checkSysUser, corpCode, spUser, null)){
				EmpExecutionContext.error("客戶群组群发预览，检查操作员、企业编码、发送账号不通过。corpCode："+ corpCode+",spUser:"+spUser+ "，errCode:"+ IErrorCode.B20007);
				ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
				result = "empex"+info.getErrorInfo(IErrorCode.B20007);
				return ;
			}

            // 是否有预览号码权限.
            Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
            if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null)
            {
                //号码是否可见，0：不可见，1：可见
                preParams.setIshidephone(1);
            }

            //发送的手机号码的集合
            List<String> phoneList=new ArrayList<String>();

            // 0.处理草稿箱文件
            if(StringUtils.isNotBlank(draftFilePath)){
                TxtFileUtil txtFileUtil = new TxtFileUtil();
                String webRoot = txtFileUtil.getWebRoot();
                File draftFile = new File(webRoot,draftFilePath);
                if(!draftFile.exists() && StaticValue.getISCLUSTER() ==1){
                        CommonBiz comBiz = new CommonBiz();
                        String downloadRes = "error";
                        //最大尝试次数
                        int retryTime = 3;
                        while (!"success".equals(downloadRes) && retryTime-- >0){
                            downloadRes = comBiz.downloadFileFromFileCenter(draftFilePath);
                        }
                        if(!"success".equals(downloadRes)){
                            EmpExecutionContext.error("客户群组群发草稿箱文件从文件服务器下载失败。");
                        }
                }
                if(!draftFile.exists()){
                    EmpExecutionContext.error("客户群组群发未找到草稿箱发送文件！");
                    result = "nodraft";
                    return;
                }
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(new FileReader(draftFile));
                    String tmp = null;
                    while ((tmp = bufferedReader.readLine()) != null){
                        phoneList.add(tmp.trim());
                    }
                } catch (Exception e){
                    EmpExecutionContext.error("客户群组群发读取草稿箱发送文件异常！");
                    result = "error";
                    return;
                }finally {
                    if(bufferedReader != null){
                        bufferedReader.close();
                    }
                }
            }

            // 1.界面传过来的手机号码
            String phoneViewStr = request.getParameter("phoneStr1");
            if(phoneViewStr != null && !"".equals(phoneViewStr))
            {
                clientSmsBiz.phoneStrAddList(phoneList, phoneViewStr);
            }

            // 2.处理微信用户手机号码
            String wxphoneStr = request.getParameter("wxphoneStr");
            if(wxphoneStr != null && !"".equals(wxphoneStr))
            {
                clientSmsBiz.phoneStrAddList(phoneList, wxphoneStr);
            }

            //8.处理签约用户手机号码
            String ydywPhoneStr=request.getParameter("ydywPhoneStr");
            if(ydywPhoneStr!=null&&!"".equals(ydywPhoneStr)){
                clientSmsBiz.phoneStrAddList(phoneList, ydywPhoneStr);
            }

			//获取操作员ID
			Long userId = Long.parseLong(lguserid);
			// 获取号码文件url
			String[] phoneFilePath = txtfileutil.getSaveUrl(userId);
			if (phoneFilePath == null || phoneFilePath.length < 5)
			{
				EmpExecutionContext.error("客户群组群发预览获取发送文件路径失败。操作员："+ lguserid +"，错误码:" + IErrorCode.V10013);
				//拼接前台自定义异常标识
				ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
				result = "empex"+info.getErrorInfo(IErrorCode.V10013);
				return;
			} 
			else
			{
				//号码文件是否已存在
				File file = new File(phoneFilePath[0]);
				//判断文件是否存在，存在则返回
				if (file.exists())
				{
					EmpExecutionContext.error("客户群组群发预览，发送文件路径已存在，文件路径："+phoneFilePath[0]);
					//拼接前台自定义异常标识
					result = "error";
					return;
				}
				//将号码文件url设置到预览参数中去
				preParams.setPhoneFilePath(phoneFilePath);
			}
			StringBuffer opContent=new StringBuffer("客户群组群发预览，");
			// 3.获取客户机构的手机号码
			if(depIdStr!=null&&depIdStr.length() > 1&&!",".equals(depIdStr))
			{
				opContent.append("机构ID：")
				.append(depIdStr.substring(1, depIdStr.length()-1));
				// 通过机构id查找电话
				clientSmsBiz.getClientPhoneStrByDepId(phoneList,depIdStr,corpCode);
			}
			// 4.获取客户群组的手机号码
			if(groupStr!=null&&groupStr.length() > 0&&!",".equals(groupStr))
			{
				opContent.append("，群组ID：")
				.append(groupStr.substring(1, groupStr.length()-1));
				// 通过群组查找电话
				clientSmsBiz.getClientByGroupStr(phoneList,groupStr,corpCode);
			}
			// 5.获取客户属性的手机号码
			if(proIdStr!=null&&proIdStr.length() > 0&&!",".equals(proIdStr))
			{
				opContent.append("，客户属性ID：")
				.append(proIdStr.substring(1, proIdStr.length()-1));
				clientSmsBiz.getClientByProIdStr(phoneList,corpCode, proIdStr);
			}
			// 6.获取客户属性值的手机号码
			if(proValueIdStr!=null&&proValueIdStr.length() > 0&&!",".equals(proValueIdStr))
			{
				opContent.append("，客户属性值ID：")
				.append(proValueIdStr.substring(1, proValueIdStr.length()-1));
				clientSmsBiz.getClientByProValueIdStr(phoneList,corpCode, proValueIdStr);
			}
			
			//9.获取签约套餐的手机号码
			if(ydywGroupStr!=null&&ydywGroupStr.length() > 0&&!",".equals(ydywGroupStr))
			{
				opContent.append("，套餐编号：")
				.append(ydywGroupStr.substring(1, ydywGroupStr.length()-1));
				// 通过套餐编码查找电话
				clientSmsBiz.getClientByYdywGroupStr(phoneList,ydywGroupStr,corpCode);
			}

			// 7.高级搜索的手机号码
			// 7.(1)高级搜索全选的手机号码
			if("true".equals(selectAllStatus))
			{
				opContent.append("，高级搜索查询条件：")
				.append(conditionsqlTemp);
				// 高级搜索全选时，通过传递过来的查询条件，找到符合条件的客户，然后将手机号拼接成字符串
				clientSmsBiz.getClientByConditionSqlNew(phoneList,corpCode, conditionsqlTemp, unChioceUserIds);
			}
			//7.(2)高级搜索未全选的手机号码
			else
			{
				// 高级搜索未全选，直接拼接手机号码
				if(phoneStr12 != null && !"".equals(phoneStr12))
				{
					clientSmsBiz.phoneStrAddList(phoneList, phoneStr12);
				}
			}
			
			//获取号段
			String[] haoduan = msgConfigBiz.getHaoduan();
			
			// 解析号码集合，验证号码合法性、过滤黑名单、过滤重号、生成有效号码文件和无效号码文件
			clientSmsBiz.parsePhone(phoneList, preParams, haoduan,
					lguserid, corpCode, busCode, MessageUtils.extractMessage("common","common_empLangName",request));
			//替换短信内的特殊字符
			msg = smsBiz.smsContentFilter(msg);
			//预发送条数
			int preSendCount = 0;
			//获取预发送条数
			//preSendCount = smsBiz.countSmsNumber2(spUser, msg, sendType, preParams.getPhoneFilePath()[1], null);
			preSendCount = smsBiz.getCountSmsSend(spUser, msg, preParams.getOprValidPhone());
			
			//判断包含机构扣费标志的map			
			Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			if(infoMap == null)
			{
				new CommonBiz().setSendInfo(request, response, corpCode, userId);
				infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			}
			//登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//余额是否足够标识
			boolean hasBalance = true;
			// 机构计费标志，计费则为true，不计费则为false。
			boolean isCharge = "true".equals(infoMap.get("feeFlag")); 
			// 机构余额
			Long depFeeCount = 0L;
			//如果机构扣费，才会去查询机构的余额。
			if(isCharge)
			{
				depFeeCount = balancebiz.getAllowSmsAmount(lfSysuser);
				if(depFeeCount == null){
					throw new EMPException(IErrorCode.B20024);
				}
				if(depFeeCount<preSendCount){
					hasBalance = false;
				}
			}
			//检查sp余额
			Long spFee = 0L;
			//sp账号计费类型
			Long spChargetype = 2L;
			String spIsCheck = "nocheck";
			//机构余额充足才查询sp余额
			if(hasBalance){
				spIsCheck = "ischeck";
				spChargetype = balancebiz.getSpUserFeeFlag(spUser,1);
				//sp预付费
				if(spChargetype==1){
					//sp账号余额查询
					spFee = balancebiz.getSpUserAmount(spUser);
					EmpExecutionContext.info("sp账号可用余额:"+spFee+",spuser:"+spUser);
					if(spFee==null){
						throw new EMPException(IErrorCode.B20045);
					}
				}//后付费账号
				else if(spChargetype==2){
					EmpExecutionContext.info("spuser:"+spUser+",该账号为后付费账号");
				}
				else{
					//获取sp账号计费类型失败
					EmpExecutionContext.error("获取sp账号计费类型失败,spuser:"+spUser);
					throw new EMPException(IErrorCode.B20044);
				}
				//sp预付费并且余额不足
				if(spChargetype==1 && spFee<preSendCount){
					hasBalance = false;
				}
			}
			//运营商余额
			String spFeeResult = "nocheck";
			if(hasBalance){
				//检查运营商余额
				spFeeResult = balancebiz.checkGwFee(spUser, preSendCount, corpCode, 1);
			}
			
			Map<String,Object> objMap = new HashMap<String,Object>();
			objMap.put("preSendCount", preSendCount);
			objMap.put("depFeeCount", depFeeCount);
			objMap.put("isCharge", isCharge);
			objMap.put("spFeeResult", spFeeResult);
			objMap.put("spChargetype", spChargetype);
			objMap.put("spFee", spFee);
			objMap.put("spIsCheck", spIsCheck);
			
			// sp账号绑定了的运营商通道，用于预览显示
            XtGateQueue xtGateQueue0 = smsBiz.getXtGateQueue(spUser, "0");
            if (null != xtGateQueue0) {
                String sign = (null != xtGateQueue0.getSignstr() && !"".equals(xtGateQueue0.getSignstr().trim()) ? xtGateQueue0.getSignstr() : "-");
                objMap.put("td0", sign);
            }
            XtGateQueue xtGateQueue1 = smsBiz.getXtGateQueue(spUser, "1");
            if (null != xtGateQueue1) {
                String sign = (null != xtGateQueue1.getSignstr() && !"".equals(xtGateQueue1.getSignstr().trim()) ? xtGateQueue1.getSignstr() : "-");
                objMap.put("td1", sign);
            }
            XtGateQueue xtGateQueue21 = smsBiz.getXtGateQueue(spUser, "21");
            if (null != xtGateQueue21) {
                String sign = (null != xtGateQueue21.getSignstr() && !"".equals(xtGateQueue21.getSignstr().trim()) ? xtGateQueue21.getSignstr() : "-");
                objMap.put("td21", sign);
            }
            XtGateQueue xtGateQueue5 = smsBiz.getXtGateQueue(spUser, "5");
            if (null != xtGateQueue5) {
            	String sign = (null != xtGateQueue5.getSignstr() && !"".equals(xtGateQueue5.getSignstr().trim()) ? xtGateQueue5.getSignstr() : "-");
            	sign = (null != xtGateQueue5.getEnsignstr() && !"".equals(xtGateQueue5.getEnsignstr().trim()) ? sign +"/"+xtGateQueue5.getEnsignstr() : sign + "/-");
            	objMap.put("td5", sign);
            }
			
			String phoneSign = preParams.getPreviewPhone();
			String[] phoneArr = phoneSign.split(";");
            phoneSign = "";
            String splitChar = StaticValue.MSG_SPLIT_CHAR;
            if (phoneArr.length > 0&&!"".equals(preParams.getPreviewPhone())) {
                for (String phone : phoneArr) {
                    String[] strs = phone.split(splitChar);
                    String phoneNumber = strs[0];
                    // 运营商标识。0:移动号码 ;1:联通号码;2:电信号码;3:国际号码
                    String spisuncm = strs[1];
                    XtGateQueue xtGateQueue;
                    if ("2".equals(strs[1])) {
                        xtGateQueue = smsBiz.getXtGateQueue(spUser, "21");
                    } else if("3".equals(strs[1])){
                        xtGateQueue = smsBiz.getXtGateQueue(spUser, "5");
                    }else{
                    	xtGateQueue = smsBiz.getXtGateQueue(spUser, spisuncm);
                    }
                    // 是否配置了对应运营商的通道
                    if (null != xtGateQueue) {
                    	// 优先显示中文签名，其次显示英文签名
                        String sign = (null != xtGateQueue.getSignstr()&&!"".equals(xtGateQueue.getSignstr().trim()) ? xtGateQueue.getSignstr() : (null != xtGateQueue5 ? (null != xtGateQueue5.getEnsignstr() &&!"".equals(xtGateQueue5.getEnsignstr().trim()) ? xtGateQueue5.getEnsignstr() : "") : ""));
                        // 签名是否前置
                        String isfront = ((xtGateQueue.getGateprivilege() != null && (xtGateQueue.getGateprivilege()&4)==4)?"true":"false");
                        if ("0".equals(spisuncm)) {
                            phoneSign += phoneNumber + splitChar+"移动"+splitChar + sign +splitChar + isfront + ";";
                        } else if ("1".equals(spisuncm)) {
                            phoneSign += phoneNumber + splitChar+"联通"+splitChar + sign + splitChar + isfront + ";";
                        } else if ("2".equals(spisuncm)) {
                            phoneSign += phoneNumber + splitChar+"电信"+splitChar + sign + splitChar + isfront + ";";
                        }else if ("3".equals(spisuncm)) {
                            phoneSign += phoneNumber + splitChar+"国外"+splitChar + sign + splitChar + isfront + ";";
                        } else {
                            phoneSign += phoneNumber + splitChar+"其他"+splitChar+splitChar+";";
                        }
                    } else {
                        if ("0".equals(spisuncm)) {
                            phoneSign += phoneNumber + splitChar+"移动"+splitChar+splitChar+";";
                        } else if ("1".equals(spisuncm)) {
                            phoneSign += phoneNumber + splitChar+"联通"+splitChar+splitChar+";";
                        } else if ("2".equals(spisuncm)) {
                            phoneSign += phoneNumber + splitChar+"电信"+splitChar+splitChar+";";
                        }else if ("3".equals(spisuncm)) {
                            phoneSign += phoneNumber + splitChar+"国外"+splitChar+splitChar+";";
                        } else {
                            phoneSign += phoneNumber + splitChar+"其他"+splitChar+splitChar+";";
                        }

                    }
                }
            }
            preParams.setPreviewPhone(phoneSign);
			
			//预览信息 json
			result = preParams.getJsonStr(objMap);
			//EmpExecutionContext.error("客户群组群发预览，提交总数："+preParams.getSubCount()+"，有效号码："+preParams.getEffCount()+"，操作员："+ lguserid);
			
			//操作日志信息
			opContent.append("，提交总数："+preParams.getSubCount()+"，有效号码："+preParams.getEffCount());
			//操作员名称
			String userName = " ";
			if(lfSysuser != null)
			{
				userName = lfSysuser.getUserName();
			}
			EmpExecutionContext.info("客户群组群发", corpCode, lguserid, userName, opContent.toString(), "OTHER");
		} catch (EMPException empex)
		{
			ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
			//获取自定义异常提示信息
			String desc = info.getErrorInfo(empex.getMessage());
			//拼接前台自定义异常标识
			result = desc;
			EmpExecutionContext.error(empex, lguserid, corpCode);
		}
		catch (Exception e)
		{
			result = ERROR;
			EmpExecutionContext.error(e, lguserid, corpCode);
		}
		finally
		{
			request.setAttribute("result", "%" + result);
			preParams.getValidPhone().clear();
			preParams.setValidPhone(null);
			preParams = null;
			try
			{
				request.getRequestDispatcher(PATH + "/kfs_preview.jsp").forward(request, response);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"客户群组群发预览请求转发异常！");
			}
		}
	}
	
}
