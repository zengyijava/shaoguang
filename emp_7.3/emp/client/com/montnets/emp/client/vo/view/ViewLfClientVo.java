package com.montnets.emp.client.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;
import com.montnets.emp.table.client.TableLfClient;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-7 下午04:47:02
 * @description
 */

public class ViewLfClientVo
{
	//实体类字段与数据库字段实体类映射的map集合
	protected  static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//id
		columns.put("clientId", TableLfClient.CLIENT_ID);
		//机构id
		columns.put("depId", TableLfClient.DEP_ID);
		//手机号码
		columns.put("mobile", TableLfClient.MOBILE);
		//姓名
		columns.put("name", TableLfClient.NAME);
		//msn
		columns.put("msn",TableLfClient.MSN);
		//qq
		columns.put("qq",TableLfClient.QQ);
		//性别
		columns.put("sex",TableLfClient.SEX);
		//生日
		columns.put("birthDay",TableLfClient.BIRTHDAY);
		//电话
		columns.put("oph",TableLfClient.OPH);
		//邮件
		columns.put("email",TableLfClient.E_MAIL);
		//机构编码
		columns.put("depCode",TableLfClient.DEP_CODE);
		//guid
		columns.put("guId", TableLfClient.GUID);
		//客户编码
		columns.put("clientCode", TableLfClient.CLIENT_CODE);		
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
