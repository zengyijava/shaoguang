package com.montnets.emp.engine.vo.view;

import com.montnets.emp.table.engine.TableLfMoService;
import com.montnets.emp.table.engine.TableLfService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @project sinolife
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-31 上午11:28:16
 * @description
 */

public class ViewLfMoServiceVo
{
	//实体类字段与数据库字段实体类映射的map集合
	protected final static Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//id
		columns.put("msId", TableLfMoService.MS_ID);
		//业务名称
		columns.put("spUser",TableLfMoService.SP_USER);
		//运行状态
		columns.put("spnumber",TableLfMoService.SPNUMBER);
		//类型
		columns.put("msgContent",TableLfMoService.MSGCONTENT);
		//指令代码
		columns.put("deliverTime",TableLfMoService.DELIVERTIME);
		//创建时间
		columns.put("phone",TableLfMoService.PHONE);
		//模块编码
		columns.put("prId",TableLfMoService.PR_ID);
		//命令
		columns.put("serId",TableLfMoService.SER_ID);
		//用户名称
		columns.put("replyState", TableLfMoService.REPLY_STATE);
		//拥有者名称
		columns.put("replyUrl", TableLfMoService.REPLY_URL);		
		//姓名
		columns.put("corpCode", TableLfMoService.CORP_CODE);
		
		columns.put("serName", TableLfService.SER_NAME);
		columns.put("orderCode", TableLfService.ORDER_CODE);
		
		columns.put("createrName", "createrName");
		
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
