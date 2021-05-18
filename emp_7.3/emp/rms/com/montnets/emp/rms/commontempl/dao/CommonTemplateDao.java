package com.montnets.emp.rms.commontempl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.commontempl.entity.LfTemplate;
import com.montnets.emp.rms.commontempl.table.TableLfTemplate;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTmplRela;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

public class CommonTemplateDao extends SuperDAO{

	public List<LfTemplateVo> getCommonTempalateList(LfTemplateVo lfTemplateVo,PageInfo pageInfo,String key) {
//		// 调用查询语句
		List<LfTemplateVo> returnList = null;
		try {
			//查询字段拼接
			String fielSql = TemplateSql.getFiledSql();
			
			//查询表拼接
			String tableSql = TemplateSql.getTableSql();
			
			//查询条件拼接
			String conditionSql = TemplateSql.getConditionSql(lfTemplateVo,key);
			
			//排序sql
			String orderSql = TemplateSql.getOrderSql();
			//总sql
			String sql = new StringBuffer(fielSql).append(tableSql).append(conditionSql).append(orderSql).toString();
//System.out.println(sql);			
			// 组装统计SQL语句
			String countSql = new StringBuffer("select count(*) totalcount from (")
					.append(fielSql).append(tableSql).append(conditionSql).append(") A").toString();
			
			returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
					LfTemplateVo.class, sql, countSql, pageInfo,
							StaticValue.EMP_POOLNAME);
		
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询LF_template Dao层出现异常");
		}
		return returnList;
	}

	/**
	 * 获取行业-用途 管理
	 * @param lfIndustryUse
	 * @param pageInfo
	 * @return
	 */
	public List<LfIndustryUse> getIndustryUseList(LfIndustryUse lfIndustryUse,PageInfo pageInfo ){
		// 调用查询语句
		List<LfIndustryUse> lfTemplateList = null;
		try {
			StringBuffer sql = new StringBuffer("SELECT * FROM LF_INDUSTRY_USE WHERE 1=1");
			
			//行业-用途类型
			if((null !=lfIndustryUse) && (null !=lfIndustryUse.getType())){
				sql.append(" AND TYPE = "+lfIndustryUse.getType());
			}
			
			//行业-用途 名称
			if((null !=lfIndustryUse) && (!StringUtils.IsNullOrEmpty(lfIndustryUse.getName()))){
				sql.append( " AND  NAME ='"+lfIndustryUse.getName()+"'");
			}
			lfTemplateList = new SuperDAO().findEntityListBySQL(LfIndustryUse.class, sql.toString(), null);
		
	 	} catch (Exception e) {
		EmpExecutionContext.error(e, "查询 LF_INDUSTRY_USE Dao层出现异常");
	}
	return lfTemplateList;
	
}
	/**
	 * 添加行业-用途
	 * @param lfIndustryUse
	 * @return
	 */
	public boolean addIndustryOrUse(LfIndustryUse lfIndustryUse){
		Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		PreparedStatement ps = null;
		boolean flag = false;
		try {
			if(null == lfIndustryUse){
				return flag;
				}
			String sql = "INSERT INTO LF_INDUSTRY_USE(NAME,OPERATOR,TYPE,CREATETM,UPDATETM) VALUES(?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, lfIndustryUse.getName());
			ps.setLong(2, lfIndustryUse.getOperator());
			ps.setInt(3, lfIndustryUse.getType());
			ps.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
			ps.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
			
			if(ps.executeUpdate() > 0){
				flag = true;
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"添加行业-用途Dao层出现异常！");
		}finally{
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e1) {
					EmpExecutionContext.error(e1,"添加行业-用途Dao层关闭ps出现异常！");
				}
			}
			if(null !=conn ){
				try {
					conn.close();
				} catch (SQLException e) {
					EmpExecutionContext.error(e,"添加行业-用途Dao层关闭conn出现异常！");
				}
			}
		}
		
		return flag;
		
	}
	/**
	 * 行业-用途 修改
	 * @param lfIndustryUse
	 * @return
	 */
	public boolean updateIndustryOrUse(LfIndustryUse lfIndustryUse){
		Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		PreparedStatement ps = null;
		boolean flag = false;
		try {
			if(null == lfIndustryUse){
				return flag;
			}
			String sql = "UPDATE  LF_INDUSTRY_USE SET NAME = ?, OPERATOR = ? , UPDATETM = ? WHERE ID=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, lfIndustryUse.getName());
			ps.setLong(2, lfIndustryUse.getOperator());
			ps.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
			ps.setLong(4,lfIndustryUse.getId());
			
			if(ps.executeUpdate() > 0){
				flag = true;
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"修改行业-用途Dao层出现异常！");
		}finally{
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e1) {
					EmpExecutionContext.error(e1,"修改行业-用途Dao层关闭ps出现异常！");
				}
			}
			if(null !=conn ){
				try {
					conn.close();
				} catch (SQLException e) {
					EmpExecutionContext.error(e,"修改行业-用途Dao层关闭conn出现异常！");
				}
			}
		}
		
		return flag;
		
	}
	
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
	@SuppressWarnings("unused")
	public  String getConditionSql(LfTemplateVo lfTemplateVo)
	{
		StringBuffer conditionSql = new StringBuffer();

		//查询条件---类型
		if (lfTemplateVo.getDsflag() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.DSFLAG).append("=").append(
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
		//查询条件---模板ID
		if (lfTemplateVo.getSptemplid() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.SP_TEMPLID).append("=").append(
					lfTemplateVo.getSptemplid());
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
					TableLfTemplate.DSFLAG).append("=").append(
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
		//查询条件----是否为公共模板
		if (lfTemplateVo.getIsPublic() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.ISPUBLIC).append("=").append(
					lfTemplateVo.getIsPublic());
		}
		//查询条件 ----创建时间：开始时间
		if(lfTemplateVo.getAddStartm() != null && lfTemplateVo.getAddEndtm()==null ){
			conditionSql.append(" and lftemplate.").append(TableLfTemplate.ADDTIME).append(">=")
					//.append("to_date('" )
					.append("'"+lfTemplateVo.getAddStartm()+"'");
					//.append("','yyyy-MM-dd hh24:mi:ss')");
		}
		
		//查询条件 ----创建时间：结束时间
		if(lfTemplateVo.getAddStartm() == null && lfTemplateVo.getAddEndtm() != null){
			conditionSql.append(" and lftemplate.").append(TableLfTemplate.ADDTIME).append("<=")
					//.append("to_date('" )
					.append("'"+lfTemplateVo.getAddEndtm()+"'");
					//.append("','yyyy-MM-dd hh24:mi:ss')");
		}
		//查询条件 ----创建时间：结束时间、结束时间
		if(lfTemplateVo.getAddStartm() != null && lfTemplateVo.getAddEndtm() != null){
			
			conditionSql.append(" and lftemplate.").append(TableLfTemplate.ADDTIME)
					.append(">=")
				//	.append("to_date('" )
					.append("'"+lfTemplateVo.getAddStartm()+"'")
					//.append("','yyyy-MM-dd hh24:mi:ss')")
					.append(" and lftemplate.").append(TableLfTemplate.ADDTIME)
					.append("<=")
					//.append("to_date('" )
					.append("'"+lfTemplateVo.getAddEndtm()+"'");
					//.append("','yyyy-MM-dd hh24:mi:ss')");
					
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
				TableLfTemplate.ADDTIME).append(" desc").toString();
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
	
	/**
	 * 更新模板使用次数
	 * @param lfTemplate
	 * @return
	 */
	public boolean updateUseCount(LfTemplate lfTemplate){
		Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		PreparedStatement ps = null;
		boolean flag = false;
		try {
			if(null == lfTemplate){
				return flag;
			}
			String sql = "UPDATE  LF_TEMPLATE SET USECOUNT = ? WHERE TM_ID=?";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, lfTemplate.getUsecount());
			ps.setLong(2, lfTemplate.getTmid());
			
			if(ps.executeUpdate() > 0){
				flag = true;
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"修改行业-用途Dao层出现异常！");
		}finally{
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e1) {
					EmpExecutionContext.error(e1,"修改行业-用途Dao层关闭ps出现异常！");
				}
			}
			if(null !=conn ){
				try {
					conn.close();
				} catch (SQLException e) {
					EmpExecutionContext.error(e,"修改行业-用途Dao层关闭conn出现异常！");
				}
			}
		}
		
		return flag;
		
	}
}
