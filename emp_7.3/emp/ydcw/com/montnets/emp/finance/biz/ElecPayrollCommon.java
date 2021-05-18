package com.montnets.emp.finance.biz;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import jxl.Cell;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.GenericEmpDAO;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.finance.util.FinancialSMSEncrypt;
import com.montnets.emp.smstask.HttpSmsSend;

/**
 * 财务短信发送公共方法集合
 * 
 * @author Jinny.Ding (djhsun@sina.com)
 * @version 1.0
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @date Mar 29, 2012
 */
public class ElecPayrollCommon {

	public ElecPayrollCommon() {
	}

	protected FinancialSMSEncrypt eat = new FinancialSMSEncrypt();
	protected BaseBiz baseBiz = new BaseBiz();
	protected YdcwBiz ydcwBiz = new YdcwBiz();
	protected HttpSmsSend hss = new HttpSmsSend();
	protected GenericEmpDAO empDao=new GenericEmpDAO();

	/**
	 * countParameter method
	 * <p>
	 * Method Description:
	 * </p>
	 * 根据模板内容,计算类似#p_n参数个数
	 * 
	 * @param templateMsg
	 *            模板内容
	 * @return
	 * @throws Exception
	 * @return int
	 * @author Jinny.Ding
	 * @date Mar 29, 2012
	 */
	public int countParameter(String templateMsg) throws Exception {
		try {
			int templateCount = 0;
			String eg = "#[pP]_[1-9][0-9]*#";
			Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(templateMsg);
			while (m.find()) {
				String rstr=m.group();
				rstr = rstr.toUpperCase();
				String pc = rstr.substring(rstr.indexOf("#P_")+3,rstr.lastIndexOf("#"));
				int pci = Integer.parseInt(pc);
				if(pci > templateCount)
				{
					templateCount = pci;
				}
			}
			return templateCount;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"根据模板内容,计算类似#p_n参数个数异常");
			throw e;
		}

	}

	/**
	 * 
	 * 该方法可以取消（2012-3-31） spliceMessageParameter method
	 * <p>
	 * Method Description:
	 * </p>
	 * 拼接Excel参数(格式:para1,para2,para3)
	 * 
	 * @param messageParameterArray
	 *            获取参数数组
	 * @return
	 * @throws Exception
	 * @return String
	 * @author Jinny.Ding
	 * @date Mar 29, 2012
	 */
	public String spliceMessageParameter(Cell[] messageParameterArray)
			throws Exception {
		try {
			String parameterListed = "";
			for (int m = 1; m < messageParameterArray.length; m++) {
				// if ("".equals(messageParameterArray[m].getContents()))
				// continue;
				if (m == messageParameterArray.length - 1) {
					parameterListed += messageParameterArray[m].getContents();
				} else {
					parameterListed += messageParameterArray[m].getContents()
							+ ",";
				}
			}
			return parameterListed;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"信息参数转换异常");
			throw e;
		}
	}
	
	public String[] getMessageParameter(Cell[] messageParameterArray)
	throws Exception {
		try {
			String[] parameterArr = new String[messageParameterArray.length-1] ;
			for (int m = 1; m < messageParameterArray.length; m++) {
				parameterArr[m-1] = messageParameterArray[m].getContents();
			}
			return parameterArr;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获得信息参数异常");
			throw e;
		}
	}

	/**
	 * spliceMessageParameter method
	 * <p>
	 * Method Description:
	 * </p>
	 * 拼接Txt参数(格式:para1,para2,para3)
	 * 
	 * @param messageParameterArray
	 *            获取参数数组
	 * @return
	 * @throws Exception
	 * @return String
	 * @author Jinny.Ding
	 * @date Mar 29, 2012
	 */
	public String spliceMessageParameter(String[] messageParameterArray)
			throws Exception {
		try {
			String parameterListed = "";
			// 去除手机号码
			for (int m = 1; m < messageParameterArray.length; m++) {
				if (m == messageParameterArray.length - 1) {
					parameterListed += messageParameterArray[m];
				} else {
					parameterListed += messageParameterArray[m] + "&";
				}
			}
			return parameterListed;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"拼接Txt参数处理异常");
			throw e;
		}
	}

	/**
	 * getRandomPassword method
	 * <p>
	 * Method Description:
	 * </p>
	 * 随机获取一个密钥
	 * @throws Exception
	 * @return String
	 * @author Jinny.Ding
	 * @date Mar 29, 2012
	 */
	public String getRandomPassword() throws Exception {
		try {
			char[] arrChar = new char[] { 'a', 'b', 'd', 'c', 'e', 'f', 'g',
					'h', 'i', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 'q', 's', 't',
					'u', 'v', 'w', 'z', 'y', 'x', '0', '1', '2', '3', '4', '5',
					'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
					'I', 'J', 'K', 'L', 'M', 'N', 'Q', 'P', 'R', 'T', 'S', 'V',
					'U', 'W', 'X', 'Y', 'Z' };

			StringBuilder num = new StringBuilder();
			Random rnd = new Random(System.currentTimeMillis());
			for (int i = 0; i < 8; i++) {
				num.append(arrChar[rnd.nextInt(arrChar.length)]);
			}
			return num.toString();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"随机获取一个密钥异常");
			throw e;
		}
	}

	/**
	 * decryptMsg method
	 * <p>
	 * Method Description:
	 * </p>
	 * 短信内容解密
	 * 
	 * @param spAccount
	 *            SP帐号明文
	 * @param strVerifyCode随机验证码(长8字节)
	 * @param strInMsg
	 *            短信内容密文,最长不超过1024个字节
	 * @return
	 * @return String[]
	 * @author Jinny.Ding
	 * @date Mar 29, 2012
	 */
	public String[] decryptMsg(String spAccount, String strVerifyCode,
			String strInMsg) {

		return eat.DecryptMsg(spAccount, strVerifyCode, strInMsg, strInMsg
				.length());

	}

	/**
	 * encryptMsg method
	 * <p>
	 * Method Description:
	 * </p>
	 * 短信内容加密
	 * 
	 * @param spAccount
	 *            SP帐号明文
	 * @param strVerifyCode随机验证码(定长8字节)
	 * @param strInMsg
	 *            短信内容明文 最好不要超过320个汉字 640个字节
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return String[]
	 * @author Jinny.Ding
	 * @date Mar 29, 2012
	 */
	public String[] encryptMsg(String spAccount, String strVerifyCode,
			String strInMsg) throws UnsupportedEncodingException {

		return eat.EncryptMsg(spAccount, strVerifyCode, new String(strInMsg
				.getBytes(), "GBK"), (new String(strInMsg.getBytes(), "GBK")
				.getBytes().length));

	}

	/**
	 * genAuthenCode method
	 * <p>
	 * Method Description:
	 * </p>
	 * 生成鉴权码
	 * 
	 * @param spAccount
	 *            SP帐号明文
	 * @param spPassword
	 *            密码明文
	 * @param strVerifyCode
	 *            随机验证码 定长8字节
	 * @return
	 * @return String[]
	 * @author Jinny.Ding
	 * @date Mar 29, 2012
	 */
	public String[] genAuthenCode(String spAccount, String spPassword,
			String strVerifyCode) {

		return eat.GenAuthenCode(spAccount, spPassword, strVerifyCode);
	}

	/**
	 * getSpPassword method
	 * <p>
	 * Method Description:
	 * </p>
	 * 获得SP密码
	 * 
	 * @param spAccount
	 *            SP帐号
	 * @return
	 * @throws Exception
	 * @return String
	 * @author Jinny.Ding
	 * @date Mar 29, 2012
	 */
	public String getSpPassword(String spAccount) throws Exception {
		return ydcwBiz.getSpPwdBySpUserId(spAccount);
	}

	/**
	 * 获取财务模板
	 * 
	 * @param templateId
	 *            模板编号
	 * @return
	 * @throws Exception
	 * 
	 * @return String
	 * @author Jinny.Ding
	 * @date Apr 27, 2012
	 */
	public String getTemplateMsg(String templateId) throws Exception {
		try {
			String number = "";
			if (templateId == null || "".equals(templateId)) {
				return number;
			}
			LfTemplate lfTemplate = null;
			// 取得参数代码
			lfTemplate = baseBiz.getById(LfTemplate.class, templateId);
			
			String tempString = "";
			if (lfTemplate != null && lfTemplate.getTmMsg() != null && !"".equals(lfTemplate.getTmMsg())) {
				for (int i = 1; i <= 50; i++) {
					String regEx = "#P_" + i + "#";
					// 忽略大小写：Pattern
					// p=Pattern.compile(regEx,Pattern.CASE_INSENSITIVE);
					Pattern p = Pattern.compile(regEx);
					Matcher m = p.matcher(lfTemplate.getTmMsg());
					boolean result = m.find();
					if (result) {
						if ("".equals(number)) {
							number = String.valueOf(i);
						} else {
							number += "?" + String.valueOf(i);
						}
					}
				}
				tempString = lfTemplate.getTmMsg();
			}

			if (!"".equals(tempString) && !"".equals(number)) {
				tempString = tempString + "[?]" + number;
			}

			return tempString;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取财务模板异常");
			throw e;
		}
	}

	/**
	 * 统计三大运营商的有效短信条数
	 * @param smsContent 
	 * @param haoduan
	 * @throws Exception
	 * @return void 
	 * @author Jinny.Ding 
	 * @date May 12, 2012
	 */
	@SuppressWarnings("unchecked")
	public int countOfSms(String spAccount,String smsContent, String[] haoduan,Map<String, Map> map) throws Exception {
		try {
			int sum = 0;
		
			String yd = haoduan[0];
			String lt = haoduan[1];
			String dx = haoduan[2];
			
			int[] maxLens = new int[3];
			int[] totalLens = new int[3];
			int[] lastLens = new int[3];
			int[] signLens = new int[3];
			
			Map<String, int[]> routeMap = new HashMap<String, int[]>();
			Map<String, boolean[]> hasMap = new HashMap<String, boolean[]>();
			
			routeMap = map.get("route");
			hasMap = map.get("has");
			
			maxLens = routeMap.get("maxLens");
			totalLens = routeMap.get("totalLens");
			lastLens = routeMap.get("lastLens");
			signLens = routeMap.get("signLens");
			// 标示是否存在路由
			boolean[] hasRoute = new boolean[] { false, false, false };
			
			hasRoute = hasMap.get("hasRoute");
			
			int len = 0;
			String msg = "";
			msg = smsContent.substring(12);
			// 短信内容的长度
			len = msg.getBytes("unicode").length - 2;
			// 如果是属于移动的号码段
			if (yd.indexOf(smsContent.substring(0, 3)) > -1) {
				if (hasRoute[0] && msg.length() <= maxLens[0] - signLens[0]) {
					if (len <= (totalLens[0] - signLens[0] + 3) * 2)
						sum += 1;
					else
						sum += 1
								+ (len - lastLens[0] * 2 + totalLens[0] * 2 - 1)
								/ (totalLens[0] * 2);
				} else {
					sum += 1;
				}
			} else if (lt.indexOf(smsContent.substring(0, 3)) > -1){
				// 如果是属于联通的号码段
				if (hasRoute[1] && msg.length() <= maxLens[1] - signLens[1]) {
					if (len <= (totalLens[1] - signLens[1] + 3) * 2)
						sum += 1;
					else
						sum += 1
								+ (len - lastLens[1] * 2 + totalLens[1] * 2 - 1)
								/ (totalLens[1] * 2);
				} else {
					sum += 1;
				}
			} else if (dx.indexOf(smsContent.substring(0, 3)) > -1){
				// 如果是属于电信的号码段
				if (hasRoute[2] && msg.length() <= maxLens[2] - signLens[2]) {
					if (len <= (totalLens[2] - signLens[2] + 3) * 2)
						sum += 1;
					else
						sum += 1
								+ (len - lastLens[2] * 2 + totalLens[2] * 2 - 1)
								/ (totalLens[2] * 2);
				} else {
					sum += 1;
				}
			}
			return sum;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"统计三大运营商的有效短信条数异常");
			throw e;
		}
	}
	
	/**
	 * 根据发送账号获取路由信息
	 * @return
	 * @throws Exception
	 *
	 * @return Map<String,int[]> 
	 * @author Jinny.Ding 
	 * @date May 12, 2012
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map> getRouteBySpUserid(String spAccount)throws Exception {
		try {
			Map<String, int[]> routeMap = new HashMap<String, int[]>();
			Map<String, boolean[]> hasMap = new HashMap<String, boolean[]>();
			Map<String, Map> mapMap = new HashMap<String, Map>();
			int[] maxLens = new int[3];
			int[] totalLens = new int[3];
			int[] lastLens = new int[3];
			int[] signLens = new int[3];
			int index = 0;
			
			//根据发送账号获取路由信息
			GtPortUsed gtPort = new GtPortUsed();
			List<GtPortUsed> gtPortsList = this.getPortByUserId(spAccount);
			// 标示是否存在路由
			boolean[] hasRoute = new boolean[] { false, false, false };
			
			for (int g = 0; g < gtPortsList.size(); g++){
				gtPort = gtPortsList.get(g);
                //如果运营商是电信:21.则index为2，移动则为0，联通则为1
				index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();
				hasRoute[index] = true;
				//获取短信最大字数
				maxLens[index] = gtPort.getMaxwords();
				//获取1到n-1条短信内容的长度
				totalLens[index] = gtPort.getMultilen1();
				//获取最后一条短信内容的长度
				lastLens[index] = gtPort.getMultilen2();
				//如果设定的短信签名长度为0则为实际短信签名内容的长度
				signLens[index] = gtPort.getSignlen() == 0?gtPort.getSignstr().trim().length():gtPort.getSignlen();
			}
			routeMap.put("maxLens", maxLens);
			routeMap.put("totalLens", totalLens);
			routeMap.put("lastLens", lastLens);
			routeMap.put("signLens", signLens);
			hasMap.put("hasRoute", hasRoute);
			mapMap.put("route", routeMap);
			mapMap.put("has", hasMap);
			
			return 	mapMap;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"根据发送账号获取路由信息异常");
			throw e;
		}
	}
	/**
	 * 获取路由信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private List<GtPortUsed> getPortByUserId(String userId) throws Exception{
		
		List<GtPortUsed> portsList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String,String>();

		conditionMap.put("userId", userId);
		conditionMap.put("routeFlag&<>", "2");
		//if (condition) {
			
		//}
		portsList = empDao.findListBySymbolsCondition(GtPortUsed.class, conditionMap, orderbyMap);
		
		return portsList;
	}
	
	/**
	 * 清除session
	 * @param session
	 * @throws Exception
	 *
	 * @return void 
	 * @author Jinny.Ding 
	 * @date May 14, 2012
	 */
	public void clearSession(HttpSession session)throws Exception {
		try {
			//清除Session
			if (session.getAttribute("countMap") != null) {
				session.removeAttribute("countMap");
			}
			if (session.getAttribute("previewList") != null) {
				session.removeAttribute("previewList");
			}
			if (session.getAttribute("taskObj") != null) {
				session.removeAttribute("taskObj");
			}
			if (session.getAttribute("txtOrExcel") != null) {
				session.removeAttribute("txtOrExcel");
			}
			if (session.getAttribute("fileUrl") != null) {
				session.removeAttribute("fileUrl");
			}
			if (session.getAttribute("isCheckTime") != null) {
				session.removeAttribute("isCheckTime");
			}
			if (session.getAttribute("ErrorAll") != null) {
				session.removeAttribute("ErrorAll");				
			}
			if (session.getAttribute("badFilePath") != null) {
				session.removeAttribute("badFilePath");				
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"清除session异常");
			throw e;
		}
	}


}
