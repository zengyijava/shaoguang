package com.montnets.emp.biztype.dao;

import com.montnets.emp.biztype.vo.LfBusManagerVo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.util.PageInfo;

import java.util.List;

/**
 *<p>project name p_xtgl</p>
 *<p>Title: bit_busTypeDao</p>
 *<p>Description: </p>
 *<p>Company: Montnets Technology CO.,LTD.</p>
 * @author dingzx
 * @date 2015-1-15下午03:24:05
 */
public class bit_busTypeDao extends SuperDAO
{
	/**
	 * 条件查询业务集合（带分页）
	 * @description
	 * @param lfBusManagerVo 查询条件
	 * @param pageInfo 分页信息
	 * @return
	 * @throws Exception
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-13 下午04:11:27
	 */
	public List<LfBusManagerVo> findLfBusManagerVo(LfBusManagerVo lfBusManagerVo, PageInfo pageInfo)throws Exception
	{
		//查询字段拼接
		String fieldSql = bit_busTypeSql.getFieldSql();
		//查询表拼接
		String tableSql = bit_busTypeSql.getTableSql();
		//组装过滤条件
		String conditionSql = bit_busTypeSql.getConditionSql(lfBusManagerVo);
		//根据机构组装下级机构
		if(lfBusManagerVo.getDepIds()!=null&&!"".equals(lfBusManagerVo.getDepIds()))
		{
			//包含子机构
			if(lfBusManagerVo.getIsContainsSun()!=null&&!"".equals(lfBusManagerVo.getIsContainsSun())&&"1".equals(lfBusManagerVo.getIsContainsSun())){
					LfDep lfDep=new DataAccessDriver().getEmpDAO().findObjectByID(LfDep.class, Long.parseLong(lfBusManagerVo.getDepIds()));
//					if(lfDep!=null&&lfDep.getDepLevel().intValue()==1){
//							lfBusManagerVo.setDepIds("");
//							lfBusManagerVo.setCorpCode(lfDep.getCorpCode());
//					}else{
						String depids=new DepDAO().getChildUserDepByParentID(Long.parseLong(lfBusManagerVo.getDepIds()),TableLfDep.DEP_ID);
						//lfBusManagerVo.setDepIds(depids);
						tableSql=tableSql+" where busmanager."+depids;
						//System.out.println(depid); DEP_ID in (23,22)
//					}
			}else{
				 //不包含子机构
				//lfBusManagerVo.setDepIds("DEP_ID="+lfBusManagerVo.getDepIds());
				tableSql=tableSql+" where busmanager.DEP_ID="+lfBusManagerVo.getDepIds();
//				String depIdCondition=TableLfDep.DEP_ID+"="+lfBusManagerVo.getDepIds();
//				lfBusManagerVo.setDepIds(depIdCondition);
			}
		}else{
//			if(lfBusManagerVo.getDepId()!=null&&!"".equals(lfBusManagerVo.getDepId())){
//				String depid=new DepDAO().getChildUserDepByParentID(lfBusManagerVo.getDepId(),TableLfDep.DEP_ID);
//				tableSql=tableSql+" and busmanager."+depid;
//			}
			conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}

		//排序条件拼接
		String orderBySql = bit_busTypeSql.getOrderBySql();
		String sql = fieldSql + tableSql + conditionSql + orderBySql;
		//组装统计SQL语句
		String countSql = "select count(*) totalcount" + tableSql + conditionSql;
//		System.out.println(sql);
		//调用查询语句
		return new DataAccessDriver().getGenericDAO().findPageVoListBySQL(LfBusManagerVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
	}
}
