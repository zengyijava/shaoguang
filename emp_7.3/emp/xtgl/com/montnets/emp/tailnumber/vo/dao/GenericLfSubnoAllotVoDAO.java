package com.montnets.emp.tailnumber.vo.dao;

import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfSubnoAllotVo;
import com.montnets.emp.tailnumber.view.ViewLfSubnoAllotVo;
import com.montnets.emp.util.PageInfo;

/**
 * @project sinolife
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-26 上午11:00:42
 * @description 
 */

public class GenericLfSubnoAllotVoDAO extends SuperDAO
{
	public List<LfSubnoAllotVo> findLfSubnoAllotVo(LfSubnoAllotVo subnoAllotVo , PageInfo pageInfo)
			throws Exception
	{
		//表名
		String tableSql = GenericLfSubnoAllotVoSQL.getTableSql(subnoAllotVo);
		//查询字段
		String fieldSql = GenericLfSubnoAllotVoSQL.getFieldSql(subnoAllotVo);
		//查询条件
		String conditionSql = GenericLfSubnoAllotVoSQL.getConditionSql(subnoAllotVo);
		//sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).toString();
		//分页sql
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql).toString();
		//列名
		Map<String, String> columns = null;
		if(subnoAllotVo.getCodeType() == 0){
			columns = ViewLfSubnoAllotVo.getMenuORM();
		}else if(subnoAllotVo.getCodeType() == 1){
			columns = ViewLfSubnoAllotVo.getBusORM();
		}else if(subnoAllotVo.getCodeType() == 2){
			columns = ViewLfSubnoAllotVo.getProORM();
		}else if(subnoAllotVo.getCodeType() == 3){
			columns = ViewLfSubnoAllotVo.getDepORM();
		}else if(subnoAllotVo.getCodeType() == 4){
			columns = ViewLfSubnoAllotVo.getUserORM();
		}
		//按条件查询
		List<LfSubnoAllotVo> subnoAllotVos = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(LfSubnoAllotVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, columns);
		//返回结果
		return subnoAllotVos;
	}
}
