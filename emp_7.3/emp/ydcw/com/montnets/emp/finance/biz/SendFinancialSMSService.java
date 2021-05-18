package com.montnets.emp.finance.biz;

import com.etool.ETool;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.finance.util.FinanceBasicData;
import com.montnets.emp.finance.util.YdcwErrorStatus;

/**
 * 导入参数文档进行财务短信发送处理类
 * 
 * @author Jinny.Ding (djhsun@sina.com)
 * @version 1.0
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @date Mar 29, 2012
 */
public class SendFinancialSMSService {
	protected MobFinancialBasics mfb = new MobFinancialBasics();
	protected SendFinancialSMS ssms = new SendFinancialSMS();
	
	/**
	 * 发送业务处理.
	 * @param fData
	 * @return
	 * @throws Exception
	 *
	 * @return boolean 
	 * @author Jinny.Ding 
	 * @date May 15, 2012
	 */
	public boolean sendFinancialSMS(FinanceBasicData fData,String empLangName) throws Exception{
		try {
			boolean result = false;
			
			// 1.存储定时
			if (fData.getSession().getAttribute("isCheckTime") !=null) {
				fData.getSession().removeAttribute("isCheckTime");
			}
			fData.getSession().setAttribute("isCheckTime", fData.getIsCheckTime());	
			
			// 2.解析TXT文档,装载并加密消息,处理消息过滤等业务操作
			// 3.将加密过后的短信内容,根据格式(手机号码,加密内容)写入临时文件,并得到临时文件路径
			int[] rowsArr = mfb.resolveDocument(fData,empLangName);
			if (rowsArr == null || rowsArr[0] == 0 || rowsArr[2] == 0 ) {
				return false;
			}

			// 4.获得鉴权码
			String[] genAuthenCode = null;
			try {
				/*genAuthenCode = fData.getEpcObject().genAuthenCode(fData.getSpAccount(),  
						fData.getEpcObject().getSpPassword(fData.getSpAccount()), fData.getVerifycode());*/
				genAuthenCode = new ETool().GenAuthenCode(fData.getSpAccount(), 
						fData.getEpcObject().getSpPassword(fData.getSpAccount()), fData.getVerifycode());
				if (genAuthenCode == null || genAuthenCode.length == 0) {
					fData.getSession().setAttribute("ErrorReport", "ECWB103");
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,YdcwErrorStatus.ECWB103);
				fData.getSession().setAttribute("ErrorReport", "ECWB103");
				throw e;
			}
			
			// 6.定时/非定时发送短信预处理(封装LfMttask对象之前要获得鉴权码 、验证码 、 加密之后的内容)
			result = ssms.sendFinancialSMSTask(fData.getEpcObject(), fData.getFilePath(), String.valueOf(rowsArr[0]) , String.valueOf(rowsArr[1]), fData.getSpAccount(), fData.getIsCheckTime(), fData.getDetermineTime(), genAuthenCode==null?"":genAuthenCode[1],
					fData.getVerifycode(), fData.getTemplate(), fData.getCorpCode(), fData.getUserId(), fData.getSmsTitle(),
					fData.getBusCode(), fData.getSession(), rowsArr[2],fData.getIsReply(),fData.getSubNo(),fData.getTaskId());

			return result;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送业务处理异常");
			throw e;
		}
	}
}
