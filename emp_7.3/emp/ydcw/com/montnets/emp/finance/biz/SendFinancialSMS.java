package com.montnets.emp.finance.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.finance.util.YdcwErrorStatus;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

/**
 * 财务短信发送类,包含定时和非定时
 * 
 * @author Jinny.Ding (djhsun@sina.com)
 * @version 1.0
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @date Mar 29, 2012
 */
public class SendFinancialSMS {

	
//	 * 发送短信
//	 * 
//	 * @param epcObject
//	 *            ElecPayrollCommon 对象
//	 * @param filePath
//	 *            临时文件路径
//	 * @param count
//	 *            短信条数
//	 * @param spAccount
//	 *            发送账号
//	 * @param determineTime
//	 *            非定时值默认为NULL
//	 * @param spPassword
//	 *            鉴权码
//	 * @param verifycode
//	 *            验证码
//	 * @param template
//	 *            模板
//	 * @param corpCode
//	 *            企业编码
//	 * @param userId
//	 *            当前用户编号
//	 * @param smsTitle
//	 *            短信标题
//	 * @param busCode
//	 *            业务编码
//	 * @param session
//	 *            HttpSession 对象
//	 * @return
//	 * @throws Exception
//	 * @return boolean
//	 * @author Jinny.Ding
//	 * @date Mar 29, 2012
//	 
	public boolean sendFinancialSMSTask(ElecPayrollCommon epcObject,
			String filePath, String total,String effs, String spAccount,
			String isCheckTime, String determineTime, String spPassword,
			String verifycode, String template, String corpCode, String userId,
			String smsTitle, String busCode, HttpSession session,int rows,
			int isReply,String subNo,Long taskId)
			throws Exception {

		boolean result = false;
		try {
			// 1.组装LfMttask对象发送进入发送短信
			LfMttask task = new LfMttask();
			// 当前登录操作员ID
			task.setUserId(Long.valueOf(userId));
			// 短信标题[移动财务短信]
			task.setTitle(smsTitle);
			// 短信内容
			// task.setMsg("1");
			task.setMsg(template);
			// 当前时间
			task.setSubmitTime(new Timestamp(System.currentTimeMillis()));
			// 提交状态（创建中1，提交2，取消3）
			task.setSubState(2);
			// 审批状态（无需审核-0，未审核-1，同意1，拒绝2）
			task.setReState(0);
			// 1是已发送，0是未发送
			task.setSendstate(0);
			// 提交号码总数
			task.setSubCount(Long.parseLong(total));
			// 有效号码总数
			task.setEffCount(Long.parseLong(effs));
			//成功发送总数
			// task.setSucCount("");
			//失败发送总数
			// task.setFaiCount("");
			// （相同1，不同2，动模3）
			task.setBmtType(2);
			// 号码文件地址
			task.setMobileUrl(filePath);
			// 通道账户
			task.setSpUser(spAccount);
			// 短信类型:1为短信，2为彩信，3为短信模板，4为彩信模板 5为移动财务
			task.setMsType(5);
			// 业务编码
			task.setBusCode(busCode);
			// 1,相同内容，2不同内容;// 1,相同内容，2不同内容// 1,相同内容，2不同内容
			task.setMsgType(2);
			// 鉴权码
			task.setSpPwd(spPassword);
			// 验证码
			task.setParams(verifycode);
			// 企业编码
			task.setCorpCode(corpCode);
			// 模拟网关发送短信条数
			task.setIcount(String.valueOf(rows));
			task.setIsReply(isReply);
			if(isReply != 0){
				task.setSubNo(subNo);
			}
			task.setTaskId(taskId);
			//System.out.println("Mobile financial object attribute task Icount value is：" + task.getIcount());
			
			// 2.确认定时
			if (isCheckTime.equals("false")) {
				task.setTimerStatus(0);
				task.setTimerTime(task.getSubmitTime());
			} else {
				if (determineTime != null) {
					// 短信定时发送时间
					task.setTimerTime(Timestamp.valueOf(determineTime));
					// 是否定时发送 1是，0不是
					task.setTimerStatus(1);
				}
			}
			result = true;
			if (task != null) {
				session.setAttribute("taskObj", task);
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,YdcwErrorStatus.ECWB110);
			session.setAttribute("ErrorReport", "ECWB110");
			throw e;
		} finally {
			if (epcObject != null) {
				epcObject = null;
			}
		}
		return result;
	}

	/**
	 * 短信发送
	 * @param session
	 * @param lfMttask
	 * @return
	 * @throws Exception
	 *
	 * @return boolean 
	 * @author Jinny.Ding 
	 * @date May 15, 2012
	 */
	public int sendSms(HttpSession session,LfMttask lfMttask) throws Exception{
		int result = 0;
		try {
			/*if(!(lfMttask.getIsReply() == 1)){
				if(taskIdStr != null && !"".equals(taskIdStr)){
					this.deleteSubNo(taskIdStr);
				}
				taskId = null;
			}else{
				taskId = Long.valueOf(taskIdStr);
			}*/
			//清除session里的taskId
			if (lfMttask == null) {
				return result;
			}
			// 1.进入短信发送业务处理
			MobFinancialBiz mf = new MobFinancialBiz();
			String msg = mf.addSmsLfMttask(lfMttask);	
			
			// 2.返回网关返回的消息值,确定发送失败/成功
			if (msg.equals("000") || msg.equals("saveSuccess")
					|| msg.equals("timerSuccess")
					|| msg.equals("createSuccess")) {
				result = 1;
			} else if(msg.equals("noMoney")){
				result =2;
			}else if(msg.equals("payFailure")){
				result=3;
			}else if("subnoFailed".equals(msg)){
				result = 4;
			}
			else if("feeerror".equals(msg) || "feefail".equals(msg)|| "nogwfee".equals(msg)){
				result = 5;
			}
			else if(msg.indexOf("lessgwfee")==0){
				result = 6;
			}
			else if("noSpInfo".equals(msg) || "noSuffiSpFee".equals(msg)|| "spFail".equals(msg)){
				result = 7;
			}
			else{
				if (session.getAttribute("GatewayReport") != null) {
					session.removeAttribute("GatewayReport");
				}
				session.setAttribute("GatewayReport", msg);
				result = 0;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,YdcwErrorStatus.ECWB110);
			session.setAttribute("ErrorReport", "ECWB110");
			throw e;
		} 
		return result;
	}
}
