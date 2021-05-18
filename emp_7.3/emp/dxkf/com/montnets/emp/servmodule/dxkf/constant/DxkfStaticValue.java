/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-11-28 下午12:41:13
 */
package com.montnets.emp.servmodule.dxkf.constant;

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

public class DxkfStaticValue
{
	// 生日祝福姓名签名，左边符号
	protected static  String	BIRTHWISH_NAME_SIGN_LEFT	= "(";

	// 生日祝福姓名签名，右边符号
	protected static String	BIRTHWISH_NAME_SIGN_RIGHT	= ")";

    public static String getBirthwishNameSignLeft() {
        return BIRTHWISH_NAME_SIGN_LEFT;
    }

    public static String getBirthwishNameSignRight() {
        return BIRTHWISH_NAME_SIGN_RIGHT;
    }

    static{
		try
		{
			String sql = "SELECT * FROM LF_GLOBAL_VARIABLE WHERE GLOBALID IN(36,37)";
			List<DynaBean> monConfigList = new SuperDAO().getListDynaBeanBySql(sql);
			for(DynaBean monConfig : monConfigList)
			{
				String globalId = monConfig.get("globalid").toString();
				String globalStrValue = monConfig.get("globalstrvalue").toString();
				if(globalId != null && globalStrValue != null)
				{
					//生日祝福姓名签名，左边符号
					if("36".equals(globalId))
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
