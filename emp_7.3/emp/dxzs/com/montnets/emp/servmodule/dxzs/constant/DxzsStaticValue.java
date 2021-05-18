/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-11-28 下午12:41:13
 */
package com.montnets.emp.servmodule.dxzs.constant;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;

/**
 * @description
 * @project emp_std_119
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-11-28 下午12:41:13
 */

public class DxzsStaticValue
{
	// 完美通知符号,带次数
	private static String	PERFECT_SIGN_TIMER		= "(完美通知 ,%s次):";

	// 完美通知符号
	private static String	PERFECT_SIGN_NAME		= "(完美通知 ,%s):";

	// 相同内容群发机构签名,左边符号
	private static String	SAMESMS_DEP_SIGN_LEFT	= "(";

	// 相同内容群发机构签名,右边符号
	private static String	SAMESMS_DEP_SIGN_RIGHT	= ")";

	// 相同内容群发姓名签名,左边符号
	private static String	SAMESMS_NAME_SIGN_LEFT		= "(";

	// 相同内容群发姓名签名,右边符号
	private static String	SAMESMS_NAME_SIGN_RIGHT		= ")";

	// 生日祝福姓名签名，左边符号
	private static String	BIRTHWISH_NAME_SIGN_LEFT	= "(";

	// 生日祝福姓名签名，右边符号
	private static String	BIRTHWISH_NAME_SIGN_RIGHT	= ")";
	
	
	public static String getPERFECT_SIGN_TIMER() {
		return PERFECT_SIGN_TIMER;
	}


	public static void setPERFECT_SIGN_TIMER(String pERFECTSIGNTIMER) {
		PERFECT_SIGN_TIMER = pERFECTSIGNTIMER;
	}


	public static String getPERFECT_SIGN_NAME() {
		return PERFECT_SIGN_NAME;
	}


	public static void setPERFECT_SIGN_NAME(String pERFECTSIGNNAME) {
		PERFECT_SIGN_NAME = pERFECTSIGNNAME;
	}


	public static String getSAMESMS_DEP_SIGN_LEFT() {
		return SAMESMS_DEP_SIGN_LEFT;
	}


	public static void setSAMESMS_DEP_SIGN_LEFT(String sAMESMSDEPSIGNLEFT) {
		SAMESMS_DEP_SIGN_LEFT = sAMESMSDEPSIGNLEFT;
	}


	public static String getSAMESMS_DEP_SIGN_RIGHT() {
		return SAMESMS_DEP_SIGN_RIGHT;
	}


	public static void setSAMESMS_DEP_SIGN_RIGHT(String sAMESMSDEPSIGNRIGHT) {
		SAMESMS_DEP_SIGN_RIGHT = sAMESMSDEPSIGNRIGHT;
	}


	public static String getSAMESMS_NAME_SIGN_LEFT() {
		return SAMESMS_NAME_SIGN_LEFT;
	}


	public static void setSAMESMS_NAME_SIGN_LEFT(String sAMESMSNAMESIGNLEFT) {
		SAMESMS_NAME_SIGN_LEFT = sAMESMSNAMESIGNLEFT;
	}


	public static String getSAMESMS_NAME_SIGN_RIGHT() {
		return SAMESMS_NAME_SIGN_RIGHT;
	}


	public static void setSAMESMS_NAME_SIGN_RIGHT(String sAMESMSNAMESIGNRIGHT) {
		SAMESMS_NAME_SIGN_RIGHT = sAMESMSNAMESIGNRIGHT;
	}


	public static String getBIRTHWISH_NAME_SIGN_LEFT() {
		return BIRTHWISH_NAME_SIGN_LEFT;
	}


	public static void setBIRTHWISH_NAME_SIGN_LEFT(String bIRTHWISHNAMESIGNLEFT) {
		BIRTHWISH_NAME_SIGN_LEFT = bIRTHWISHNAMESIGNLEFT;
	}


	public static String getBIRTHWISH_NAME_SIGN_RIGHT() {
		return BIRTHWISH_NAME_SIGN_RIGHT;
	}


	public static void setBIRTHWISH_NAME_SIGN_RIGHT(String bIRTHWISHNAMESIGNRIGHT) {
		BIRTHWISH_NAME_SIGN_RIGHT = bIRTHWISHNAMESIGNRIGHT;
	}


	static{
		try
		{
			String sql = "SELECT * FROM LF_GLOBAL_VARIABLE WHERE GLOBALID IN(30,31,32,33,34,35,36,37)";
			List<DynaBean> monConfigList = new SuperDAO().getListDynaBeanBySql(sql);
			for(DynaBean monConfig : monConfigList)
			{
				String globalId = monConfig.get("globalid").toString();
				String globalStrValue = monConfig.get("globalstrvalue").toString();
				if(globalId != null && globalStrValue != null)
				{
					//完美通知符号,带次数
					if("30".equals(globalId))
					{
						PERFECT_SIGN_TIMER = globalStrValue;
					}
					//完美通知符号
					else if("31".equals(globalId))
					{
						PERFECT_SIGN_NAME = globalStrValue;
					}
					//相同内容群发机构签名,左边符号
					else if("32".equals(globalId))
					{
						SAMESMS_DEP_SIGN_LEFT = globalStrValue;
					}
					//相同内容群发机构签名,右边符号
					else if("33".equals(globalId))
					{
						SAMESMS_DEP_SIGN_RIGHT = globalStrValue;
					}
					//相同内容群发姓名签名,左边符号
					else if("34".equals(globalId))
					{
						SAMESMS_NAME_SIGN_LEFT = globalStrValue;
					}
					//相同内容群发姓名签名,右边符号
					else if("35".equals(globalId))
					{
						SAMESMS_NAME_SIGN_RIGHT = globalStrValue;
					}
					//生日祝福姓名签名，左边符号
					else if("36".equals(globalId))
					{
						BIRTHWISH_NAME_SIGN_LEFT = globalStrValue;
					}
					//生日祝福姓名签名，右边符号
					else if("37".equals(globalId))
					{
						BIRTHWISH_NAME_SIGN_RIGHT = globalStrValue;
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "签名符号信息初始化异常！");
		}
	}

}
