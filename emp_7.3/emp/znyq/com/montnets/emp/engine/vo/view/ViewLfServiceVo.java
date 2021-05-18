package com.montnets.emp.engine.vo.view;

import com.montnets.emp.table.engine.TableLfService;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sysuser.TableLfSysuser;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @project sinolife
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-31 上午11:28:16
 * @description
 */

public class ViewLfServiceVo
{
	//实体类字段与数据库字段实体类映射的map集合
	protected final static Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//id
		columns.put("serId", TableLfService.SER_ID);
		//业务名称
		columns.put("serName",TableLfService.SER_NAME);
		//运行状态
		columns.put("runState",TableLfService.RUN_STATE);
		//类型
		columns.put("serType",TableLfService.SER_TYPE);
		//指令代码
		columns.put("orderCode",TableLfService.ORDER_CODE);
		//创建时间
		columns.put("createTime",TableLfService.CREATE_TIME);
		//模块编码
		columns.put("menuCode",TableLfService.MENU_CODE);
		//命令
		columns.put("comments",TableLfService.COMMENTS);
		//用户名称
		columns.put("userName", TableLfSysuser.USER_NAME);
		//拥有者名称
		columns.put("ownerUserName", "OWNERUSER_NAME");		
		//姓名
		columns.put("name", TableLfSysuser.NAME);
		//拥有者姓名
		columns.put("ownerName", "OWNER_NAME");
		//状态
		columns.put("userState",TableLfSysuser.USER_STATE);		
		//姓名
		columns.put("staffName", TableUserdata.STAFF_NAME);
		//识别模式。空和1-使用尾号；2-使用指令
		columns.put("identifyMode", TableLfService.IDENTIFY_MODE);
		//企业编码
		columns.put("corpCode", TableLfService.CORP_CODE);
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
