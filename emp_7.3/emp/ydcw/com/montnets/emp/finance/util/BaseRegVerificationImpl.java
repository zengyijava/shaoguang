package com.montnets.emp.finance.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.montnets.emp.common.context.EmpExecutionContext;

public class BaseRegVerificationImpl implements BaseRegVerification {
	
    /**
     * 验证信息长度
     */
	public boolean isByteLengthWithFinancialMsg(String financialMsg)
			throws Exception {
		try {
			//不能大于320
			if (financialMsg.length() > 320) {
				return false;
			}
			return true;
		} catch (Exception e) {
			//异常抛出
			EmpExecutionContext.error(e,"验证信息长度异常");
			throw e;
		}
	}
	/**
	 * 验证手机号码
	 */
	public boolean isMobileNO(String mobiles) throws Exception {
		try {
			//必须是数字
			Pattern p = Pattern
					.compile("^(([1-9][1-9][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			return m.matches();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"验证手机号码异常");
			//异常抛出
			throw e;
		}
	}

}
