package com.montnets.emp.global.servlet;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.dao.SuperDAO;

public class operPageFieldDAO extends SuperDAO{
	
	/**
	 * 查询数据库中fieldType类型fieldid最大值
	 * @param fieldType
	 * @return fieldid+1
	 */
	public String getMaxFieldId(String fieldType)
	{
		//返回结果
		String maxFieldId = "";
		
		String sql = "select max(fieldid) as fieldid from LF_PageField where fieldid like '" + fieldType + "%'";
		
		List<DynaBean> resultList = this.getListDynaBeanBySql(sql);
		
		
		if(resultList == null || resultList.size() == 0)
		{
			maxFieldId = fieldType + "00001";
		}
		else{
			if(resultList.get(0).get("fieldid")!=null && !"".equals(resultList.get(0).get("fieldid")) )
			{
				maxFieldId = Integer.toString((Integer.valueOf(resultList.get(0).get("fieldid").toString()) +1));
			}
			else
			{
				maxFieldId = fieldType + "00001";
			}
		}
		
		
		return maxFieldId;
	}

}
