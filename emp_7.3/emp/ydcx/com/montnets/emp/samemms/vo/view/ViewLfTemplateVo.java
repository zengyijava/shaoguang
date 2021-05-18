package com.montnets.emp.samemms.vo.view;

import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTemplate;
import com.montnets.emp.table.template.TableLfTmplRela;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-6-30 下午02:40:44
 * @description
 */

public class ViewLfTemplateVo
{
	//实体类字段与数据库字段实体类映射的map集合
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//id
		columns.put("id", TableLfTmplRela.id);
		columns.put("shareType", TableLfTmplRela.shareType);
		columns.put("tmid", TableLfTemplate.TM_ID);
		//操作员id
		columns.put("userId", TableLfTemplate.USER_ID);
		//名称
		columns.put("tmName", TableLfTemplate.TM_NAME);
		//编码
		columns.put("tmCode", TableLfTemplate.TM_CODE);
		//内容
		columns.put("tmMsg", TableLfTemplate.TM_MSG);
		//标志
		columns.put("dsflag", TableLfTemplate.DS_FLAG);
		//状态
		columns.put("tmState", TableLfTemplate.TM_STATE);
		//条件时间
		columns.put("addtime", TableLfTemplate.ADD_TIME);
		//是否审核
		columns.put("isPass", TableLfTemplate.ISPASS);
		//类型
		columns.put("tmpType", TableLfTemplate.TMP_TYPE);
		//姓名
		columns.put("name", TableLfSysuser.NAME);
		//用户名称
		columns.put("userName", TableLfSysuser.USER_NAME);
		//状态
		columns.put("userState", TableLfSysuser.USER_STATE);
		//机构名称
		columns.put("depName", TableLfDep.DEP_NAME);
		//模板参数个数
		columns.put("paramcnt", TableLfTemplate.PARAMCNT);
		//网关模板ID
		columns.put("sptemplid", TableLfTemplate.SP_TEMPLID);
		//网关审批状态
		columns.put("auditstatus", TableLfTemplate.AUDITSTATUS);
		//网关审批错误信息
		columns.put("errorcode", TableLfTemplate.ERROR_CODE);
		
		columns.put("submitstatus", TableLfTemplate.SUBMITSTATUS);
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
