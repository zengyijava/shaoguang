package com.montnets.emp.engine.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;

/**
 * 
 * @project emp_std_new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-8-1 下午02:47:46
 * @description 上行业务记录dao
 */
public class MoServiceDao extends SuperDAO
{
	/**
	 * 根据spuser获取其路由绑定
	 * @param spUser
	 * @return 返回绑定记录
	 */
	public List<DynaBean> getPortFromSpuser(String spUser){
		
		try{
			//select gate.ID as gateid, port.cpno from GT_PORT_USED port left join XT_GATE_QUEUE gate on port.spgate = gate.spgate and port.SpIsUncm=gate.SpIsUncm where port.userId=@spuser and port.gatetype=1 and gate.gatetype=1
			StringBuffer sql = new StringBuffer();
			sql.append("select gate.ID as gateid, port.cpno ")
					.append("from GT_PORT_USED port left join XT_GATE_QUEUE gate ")
					.append("on port.spgate = gate.spgate ")
					.append("and port.SpIsUncm=gate.SpIsUncm ")
					.append("where ")
					.append("port.userId='").append(spUser).append("' ")
					.append("and port.gatetype=1 ")
					.append("and gate.gatetype=1 ");
			
			
			List<DynaBean> protList = getListDynaBeanBySql(sql.toString());
			
			return protList;
		}catch(Exception e){
			EmpExecutionContext.error(e, "根据spuser获取其路由绑定异常。");
			return null;
		}
		
	}

	
	
}
