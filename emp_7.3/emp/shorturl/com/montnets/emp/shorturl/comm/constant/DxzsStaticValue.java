/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-11-28 下午12:41:13
 */
package com.montnets.emp.shorturl.comm.constant;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import org.apache.commons.beanutils.DynaBean;

import java.util.List;

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
	protected static  String	PERFECT_SIGN_TIMER		= "(完美通知 ,%s次):";

	// 完美通知符号
	protected static String	PERFECT_SIGN_NAME		= "(完美通知 ,%s):";

	// 相同内容群发机构签名,左边符号
	protected static  String	SAMESMS_DEP_SIGN_LEFT	= "(";

	// 相同内容群发机构签名,右边符号
	protected static String	SAMESMS_DEP_SIGN_RIGHT	= ")";

	// 相同内容群发姓名签名,左边符号
	protected static String	SAMESMS_NAME_SIGN_LEFT		= "(";

	// 相同内容群发姓名签名,右边符号
	protected static String	SAMESMS_NAME_SIGN_RIGHT		= ")";

	// 生日祝福姓名签名，左边符号
	protected static String	BIRTHWISH_NAME_SIGN_LEFT	= "(";

	// 生日祝福姓名签名，右边符号
	protected static String	BIRTHWISH_NAME_SIGN_RIGHT	= ")";


    public static String getPerfectSignTimer() {
        return PERFECT_SIGN_TIMER;
    }

    public static String getPerfectSignName() {
        return PERFECT_SIGN_NAME;
    }

    public static String getSamesmsDepSignLeft() {
        return SAMESMS_DEP_SIGN_LEFT;
    }

    public static String getSamesmsDepSignRight() {
        return SAMESMS_DEP_SIGN_RIGHT;
    }

    public static String getSamesmsNameSignLeft() {
        return SAMESMS_NAME_SIGN_LEFT;
    }

    public static String getSamesmsNameSignRight() {
        return SAMESMS_NAME_SIGN_RIGHT;
    }

    public static String getBirthwishNameSignLeft() {
        return BIRTHWISH_NAME_SIGN_LEFT;
    }

    public static String getBirthwishNameSignRight() {
        return BIRTHWISH_NAME_SIGN_RIGHT;
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
