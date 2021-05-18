package com.montnets.emp.common.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.birthwish.TableLfBirthdaySetup;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-25 上午09:58:23
 * @description
 */

public class ViewLfBirthdaySetupVo
{
	//返回实体类字段与数据库字段实体类映射的map集合
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//id
		columns.put("id", TableLfBirthdaySetup.ID);
		//操作员id
		columns.put("userId", TableLfBirthdaySetup.USER_ID);
		//标题
		columns.put("title", TableLfBirthdaySetup.TITLE);
		//消息内容
		columns.put("msg", TableLfBirthdaySetup.MSG);
		//发送时间
		columns.put("sendTime", TableLfBirthdaySetup.SEND_TIME);
		//有尊称
		columns.put("isAddName", TableLfBirthdaySetup.IS_ADDNAME);
		//尊称
		columns.put("addName", TableLfBirthdaySetup.ADDNAME);
		//发送账号
		columns.put("spUser", TableLfBirthdaySetup.SP_USER);
		//是否使用
		columns.put("isUse", TableLfBirthdaySetup.IS_USE);
		//类型
		columns.put("type", TableLfBirthdaySetup.TYPE);
		//企业编码
		columns.put("corpCode", TableLfBirthdaySetup.CORPCODE);
		//业务编码
		columns.put("buscode", TableLfBirthdaySetup.BUS_CODE);
		//用户名称
		columns.put("username", TableLfSysuser.NAME);
		//机构id
		columns.put("depid", TableLfSysuser.DEP_ID);
		//机构名称
		columns.put("depname", TableLfDep.DEP_NAME);
		//是否签名
		columns.put("isSignName", TableLfBirthdaySetup.ISSIGNNAME);
		//签名
		columns.put("signName", TableLfBirthdaySetup.SIGNNAME);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM()
	{
		return columns;
	}
}
