package com.montnets.emp.shorturl.surlmanage.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.shorturl.surlmanage.table.TableLfDomain;
import com.montnets.emp.shorturl.surlmanage.table.TableLfDomainCorp;
import com.montnets.emp.table.corp.TableLfCorp;



public class ViewLfDomainCorpVo {

	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	
	/**
     * 加载字段
     */
	static
	{
		columns.put("corpID", TableLfCorp.CORP_ID);
		columns.put("corpCode", TableLfCorp.CORP_CODE);
		columns.put("corpName",TableLfCorp.CORP_NAME);
		columns.put("id", TableLfDomain.ID);
		columns.put("domain",TableLfDomain.DOMAIN);
		columns.put("flag",TableLfDomainCorp.FLAG);
		columns.put("createUid", TableLfDomainCorp.CREATE_UID);
		columns.put("createTm",TableLfDomainCorp.CREATE_TM);
		columns.put("updateTm",TableLfDomainCorp.UPDATE_TM);
		columns.put("createUser",TableLfDomainCorp.CREATE_USER);
	}
	
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
