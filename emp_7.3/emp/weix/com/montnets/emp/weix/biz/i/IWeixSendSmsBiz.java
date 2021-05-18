package com.montnets.emp.weix.biz.i;

import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.weix.LfWcUlink;
import com.montnets.emp.entity.weix.LfWcUserInfo;

/**
 * 发送验证码
 * 
 * @author Administrator
 */
public interface IWeixSendSmsBiz 
{

	/**
	 * 发送验证码的方法
	 * 
	 * @param lfExamineSms
	 *        null
	 * @param msgInfo
	 *        发送内容
	 * @param corpCode
	 *        企业编码
	 * @param sp
	 *        发送账户
	 * @param subno
	 *        尾号
	 * @param balanceBiz
	 * @param count
	 *        发送条数
	 * @param senderUser
	 *        发送者
	 * @param flag
	 *        是否计费
	 * @param Rptflag
	 *        是否需要返回状态报告
	 * @return
	 * @throws Exception
	 */
	public String sendOneSms(String phone, String msg, String sp, String subno, Integer count, LfSysuser senderUser, boolean flag, String Rptflag, String menucode) throws Exception;
	/**
	 * 保存手机关联客户提交过来的信息(未使用，以后会用到)
	 * 
	 * @param userInfo
	 * @param ulink
	 * @return
	 * @throws Exception1
	 */
	public boolean saveInfo(LfWcUserInfo userInfo, LfWcUlink ulink) throws Exception;
}
