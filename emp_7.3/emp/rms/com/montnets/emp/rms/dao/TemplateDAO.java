package com.montnets.emp.rms.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTemplate;
import com.montnets.emp.table.template.TableLfTmplRela;
import com.montnets.emp.util.PageInfo;

/**
 * @project sinolife
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-6-30 下午02:57:55
 * @description
 */
//彩信素材处理DAO
public class TemplateDAO extends SuperDAO
{
	/**
	 *   查询所有的彩信素材列表VO
	 * @param loginUserId	操作员用户ID
	 * @param lfTemplateVo	素材VO
	 * @param pageInfo	分页信息
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> findLfTemplateVoList(Long loginUserId,
			LfTemplateVo lfTemplateVo, PageInfo pageInfo) throws Exception
	{
		//获取查询SQL
		String fieldSql = getFieldSql();
		//获取表SQL
		String tableSql = getTableSql();
		//获取whereSQL
		String whereSql = getWhereSql();
		//获取审核的SQL
		String dominationSql = getDominationSql(loginUserId);
		//获取共享的SQL
		String shareSql = getShareSql(loginUserId);
		//获取查询SQL
		String conditionSql = getConditionSql(lfTemplateVo);
		//获取groupSQL
		String groupSql = getGroupBySql();
		//获取排序SQL
		String orderBySql = getOrderBySql();
		String innerSql=new StringBuffer("select lfalltemplate.ID,lftemplate.TM_ID")
		.append(tableSql).append(dominationSql).append(shareSql).append(")").append(conditionSql)
		.toString();
		String outerSql= new StringBuffer("select MIN(temp.ID) tm_id from (").
		append(innerSql).append(")").append(groupSql).toString();
		//拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).
		append(whereSql).append("(")
		.append(outerSql)
		.append(
				")").append(orderBySql)
				.toString();
		
		//获取总计
		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append(outerSql)
				.append(
						") mytemp")
				.toString();
		//查询数据
		List<LfTemplateVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfTemplateVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//返回
		return returnList;
	}
	/**
	 *  获取所有彩信素材列表
	 * @param loginUserId	操作员用户ID
	 * @param lfTemplateVo	素材VO
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> findLfTemplateVoList(Long loginUserId,
			LfTemplateVo lfTemplateVo) throws Exception
	{
		//获取查询SQL
		String fieldSql = getFieldSql();
		//获取表SQL
		String tableSql = getTableSql();
		//获取whereSQL
		String whereSql = getWhereSql();
		//获取审核的SQL
		String dominationSql = getDominationSql(loginUserId);
		//获取共享的SQL
		String shareSql = getShareSql(loginUserId);
		//获取查询SQL
		String conditionSql = getConditionSql(lfTemplateVo);
		//获取groupSQL
		String groupSql = getGroupBySql();
		//获取排序SQL
		String orderBySql = getOrderBySql();
		String innerSql=new StringBuffer("select lfalltemplate.ID,lftemplate.TM_ID")
		.append(tableSql).append(dominationSql).append(shareSql).append(conditionSql)
		.toString();
		String outerSql= new StringBuffer("select MIN(temp.ID) tm_id from (").
		append(innerSql).append(")").append(groupSql).toString();
		//拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).
		append(whereSql).append("(")
		.append(outerSql)
		.append(
				")").append(orderBySql)
				.toString();
		
		//查询数据
		List<LfTemplateVo> returnVoList = findVoListBySQL(LfTemplateVo.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	/**
	 * 获取查询字段
	 * @return
	 */
	private  String getFieldSql()
	{
		//拼接查询字段
		String fieldSql = new StringBuffer("select lfalltemplate.").append(TableLfTmplRela.shareType)
				.append(",lfalltemplate.").append(TableLfTmplRela.id)
				.append(",lftemplate.* ,sysuser.")
				.append(TableLfSysuser.NAME).append(",sysuser.").append(
						TableLfSysuser.USER_NAME).append(",sysuser.")
				.append(TableLfSysuser.USER_STATE)
				.append(",lfdep.").append(TableLfDep.DEP_NAME)
				.toString();
		//返回查询字段字符串
		return fieldSql;
	}

	/**
	 * 获取查询表名sql
	 * @return
	 */
	private  String getTableSql()
	{
		//拼接sql
		String tableSql = new StringBuffer(" from ").
		append(TableLfTmplRela.TABLE_NAME).append(" lfalltemplate inner join ")
		.append(
				TableLfTemplate.TABLE_NAME).append(" lftemplate on lfalltemplate.")
				.append(TableLfTmplRela.templId).append("= lftemplate.")
				.append(TableLfTemplate.TM_ID).append(" and lfalltemplate.")
				.append(TableLfTmplRela.templType).append("=2 left join ")
				.append(TableLfSysuser.TABLE_NAME).append(
						" sysuser  on lftemplate.").append(
						TableLfTemplate.USER_ID).append("= sysuser.").append(
						TableLfSysuser.USER_ID)
						.append(" left join ").append(
						TableLfDep.TABLE_NAME).append(" lfdep on sysuser.")
				.append(TableLfSysuser.DEP_ID).append("=lfdep.").append(
						TableLfDep.DEP_ID)
						.toString();
		//返回查询表名字符串
		return tableSql;
	}

	/**
	 * 机构权限sql拼接
	 * @param loginUserId
	 * @return
	 */
	private  String getDominationSql(Long loginUserId)
	{
		//拼接sql
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(loginUserId);
		String dominationSql = new StringBuffer(" where (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(loginUserId).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append(")").toString();
		return dominationSql;
	}
	
	//获得共享模板sql
	private  String getShareSql(Long loginUserId)
	{
		//拼接sql
		StringBuffer share = new StringBuffer("select ").append(
			TableLfSysuser.DEP_ID).append(" from ").append(
			TableLfSysuser.TABLE_NAME).append(" where ").append(
			TableLfSysuser.USER_ID).append("=").append(loginUserId);
		String shareSql = new StringBuffer(" or (lfalltemplate.").append(
				TableLfTmplRela.toUserType).append("=").append(1).append(
				" and lfalltemplate.").append(TableLfTmplRela.toUser).append(" in (")
				.append(share).append("))").
				append(" or (lfalltemplate.").append(
				TableLfTmplRela.toUserType).append("=").append(2).append(
				" and lfalltemplate.").append(TableLfTmplRela.toUser).append(" =")
				.append(loginUserId).append(")").toString();
		return shareSql;
	}
	
	/**
	 * 查询条件
	 * @param lfTemplateVo
	 * @return
	 */
	private  String getConditionSql(LfTemplateVo lfTemplateVo)
	{
		StringBuffer conditionSql = new StringBuffer();

		//查询条件---类型
		if (lfTemplateVo.getDsflag() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.DS_FLAG).append("=").append(
					lfTemplateVo.getDsflag());
		}
		//查询条件---状态
		if (lfTemplateVo.getTmState() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_STATE).append("=").append(
					lfTemplateVo.getTmState());
		}
		//查询条件---姓名
		if (lfTemplateVo.getName() != null
				&& !"".equals(lfTemplateVo.getName())) {
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME)
					.append(" like '%").append(lfTemplateVo.getName().trim())
					.append("%'");
		}
		//查询条件---模板内容
		if (lfTemplateVo.getTmMsg() != null
				&& !"".equals(lfTemplateVo.getTmMsg())) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_MSG).append(" like '%").append(
					lfTemplateVo.getTmMsg()).append("%'");
		}
		//查询条件---模板名称
		if (lfTemplateVo.getTmName() != null
				&& !"".equals(lfTemplateVo.getTmName())) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_NAME).append(" like '%").append(
					lfTemplateVo.getTmName()).append("%'");
		}
		//查询条件---模板编码
		if (lfTemplateVo.getTmCode() != null
				&& !"".equals(lfTemplateVo.getTmCode())) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_CODE).append(" like '%").append(
					lfTemplateVo.getTmCode()).append("%'");
		}
		
		//查询条件---类型
		if (lfTemplateVo.getTmpType() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TMP_TYPE).append("=").append(
					lfTemplateVo.getTmpType());
		}
		//查询条件---审核是否通过
		if (lfTemplateVo.getIsPass() != null) {
			if (lfTemplateVo.getIsPass() == 5) {
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ISPASS).append(" in (0,1)");
			} else {
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ISPASS).append("=").append(
						lfTemplateVo.getIsPass());
			}
		}
		//查询条件---操作员id
		if (lfTemplateVo.getUserId() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.USER_ID).append("=").append(
					lfTemplateVo.getUserId());
		}
		//查询条件---模板类型
		if (lfTemplateVo.getDsflag() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.DS_FLAG).append("=").append(
					lfTemplateVo.getDsflag());
		}
		//查询条件----运营商审批状态
		if (lfTemplateVo.getAuditstatus() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.AUDITSTATUS).append("=").append(
					lfTemplateVo.getAuditstatus());
		}
		//查询条件----运营商提交状态
		if (lfTemplateVo.getSubmitstatus() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.SUBMITSTATUS).append("=").append(
					lfTemplateVo.getSubmitstatus());
		}
        if(lfTemplateVo.getFlowID() != null){
            conditionSql.append(" and lftemplate.").append(TableLfTemplate.TM_ID)
                    .append(" in ( SELECT DISTINCT MT_ID FROM LF_FLOWRECORD WHERE F_ID = ").append(lfTemplateVo.getFlowID())
            .append(" and INFO_TYPE = ").append(lfTemplateVo.getTmpType()).append(" ) ");
        }
		//返回拼接sql
		return conditionSql.toString();
	}

	/**
	 * 获取排序sql
	 * @return
	 */
	private  String getOrderBySql()
	{
		//拼接sql
		String orderBySql = new StringBuffer(" order by lftemplate.").append(
				TableLfTemplate.ADD_TIME).append(" desc").toString();
		return orderBySql;
	}
	/**
	 * 获取分组sql
	 * @return
	 */
	private  String getGroupBySql()
	{
		//拼接sql
		String groupBySql = new StringBuffer("temp group by temp.").append(
				TableLfTemplate.TM_ID).toString();
		return groupBySql;
	}
	/**
	 * 获取where sql
	 * @return
	 */
	private  String getWhereSql()
	{
		//拼接sql
		String groupBySql = new StringBuffer(" where lfalltemplate.").append(TableLfTmplRela.id).append(" IN ").toString();
		return groupBySql;
	}

}
