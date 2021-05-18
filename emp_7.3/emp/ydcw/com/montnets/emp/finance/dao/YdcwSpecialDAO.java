package com.montnets.emp.finance.dao;


import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTemplate;
import com.montnets.emp.table.template.TableLfTmplRela;

public class YdcwSpecialDAO extends SuperDAO
{


	/**
	 *   移动财务的 模块调用    权限查询
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplate> getYdcwTemplate(LfTemplate template) throws Exception{
		
		/*select lftemplate.*  
		from LF_TEMPLATE lftemplate left join LF_SYSUSER sysuser  on lftemplate.USER_ID= sysuser.USER_ID
		left join LF_DEP lfdep on sysuser.DEP_ID=lfdep.DEP_ID 
		where (sysuser.USER_ID=2 
		or sysuser.DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=2)) 
		and lftemplate.TM_STATE=1 and lftemplate.TMP_TYPE=4 and lftemplate.ISPASS in (0,1) order by lftemplate.ADDTIME desc*/
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select distinct lftemplate.* from ").append(TableLfTmplRela.TABLE_NAME).append(" lfalltemplate ")
		.append(StaticValue.getWITHNOLOCK()).append(" inner join ").append(TableLfTemplate.TABLE_NAME).append(" lftemplate ")
		.append(StaticValue.getWITHNOLOCK()).append(" on lfalltemplate.").append(TableLfTmplRela.templId).append(" = lftemplate.")
		.append(TableLfTemplate.TM_ID).append(" and ").append(TableLfTmplRela.templType).append("=1")
		.append(" left join ").append(TableLfSysuser.TABLE_NAME).append(" sysuser ")
		.append(StaticValue.getWITHNOLOCK()).append(" on lftemplate.").append(TableLfTemplate.USER_ID).append(" = sysuser.")
		.append(TableLfSysuser.USER_ID).append(" left join ").append(TableLfDep.TABLE_NAME).append(" lfdep ")
		.append(StaticValue.getWITHNOLOCK()).append(" on sysuser.").append(TableLfSysuser.DEP_ID).append(" = ")
		.append(" lfdep.").append(TableLfDep.DEP_ID) .append(" where (sysuser.").append(TableLfSysuser.USER_ID)
		.append(" = ").append(template.getUserId()).append(" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in ")
		.append(" (select domination.").append(TableLfDomination.DEP_ID).append(" from ").append(TableLfDomination.TABLE_NAME)
		.append(" domination ").append(StaticValue.getWITHNOLOCK()).append(" where domination.").append(TableLfDomination.USER_ID)
		.append(" = ").append(template.getUserId()).append(" )").append(" or (lfalltemplate.").append(TableLfTmplRela.toUserType)
		.append(" = 1 and lfalltemplate.").append(TableLfTmplRela.toUser).append(" in (select ").append(TableLfSysuser.DEP_ID)
		.append(" from ").append(TableLfSysuser.TABLE_NAME).append(" where ").append(TableLfSysuser.USER_ID).append(" = ").append(template.getUserId())
		.append(")) or(lfalltemplate.")
		.append(TableLfTmplRela.toUserType).append(" = 2 and lfalltemplate.").append(TableLfTmplRela.toUser).append(" = ")
		.append(template.getUserId()).append("))");
		//模板类型(1.通用动态模块;0.通用静态模块;2.智能抓取模块;3.移动财务模块)
		if(template.getDsflag() != null){
			sqlStr.append(" and lftemplate.").append(TableLfTemplate.DS_FLAG).append(" = ").append(template.getDsflag());
		}
		//模板状态(1.启用，0.禁用,2.草稿)
		if(template.getTmState() != null){
			sqlStr.append(" and lftemplate.").append(TableLfTemplate.TM_STATE).append(" = ").append(template.getTmState());
		}
		//审批状态（-1.未审批；0.无需审批；1.审批通过；2.审批未通过）
		sqlStr.append(" and lftemplate.").append(TableLfTemplate.ISPASS).append(" in (0,1) ");
		//业务代码(电子工资单、报销提醒、回款通知)
		sqlStr.append(" and lftemplate.").append(TableLfTemplate.BIZ_CODE).append(" = '").append(template.getBizCode()).append("'");

		sqlStr.append(" and lftemplate.").append(TableLfTemplate.CORP_CODE).append(" = '").append(template.getCorpCode()).append("'");

		sqlStr.append("  order by lftemplate.").append(TableLfTemplate.TM_ID).append(" ").append(StaticValue.DESC);
		return findEntityListBySQL(LfTemplate.class, sqlStr.toString(),StaticValue.EMP_POOLNAME);
	}	
}
