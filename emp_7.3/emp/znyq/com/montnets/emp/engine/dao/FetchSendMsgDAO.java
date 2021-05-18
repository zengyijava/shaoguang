package com.montnets.emp.engine.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;

/**
 * 
 * @功能概要：智能引擎下行业务DAO
 * @项目名称： emp_tgb
 * @初创作者： huangzb <huangzb@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2015-12-29 下午02:21:43
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class FetchSendMsgDAO extends SuperDAO
{
	/**
	 * 
	 * @description 根据企业编码查询企业状态
	 * @param corpCode 企业编码
	 * @return 0：禁用；1启用；-1和-2：获取不到企业状态；-9999：异常
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2015-12-29 下午01:59:40
	 */
	public int getCorpState(String corpCode)
	{
		try
		{
			String sql = "SELECT CORP_STATE from LF_CORP WHERE CORP_CODE = '"+corpCode+"'";
			List<DynaBean> corpList = getListDynaBeanBySql(sql);
			//获取不到企业状态
			if(corpList == null || corpList.size() < 1)
			{
				EmpExecutionContext.error("智能引擎，根据企业编码查询企业状态，查询不到企业状态。corpCode="+corpCode);
				return -1;
			}
			if(corpList.get(0).get("corp_state") == null)
			{
				EmpExecutionContext.error("智能引擎，根据企业编码查询企业状态，获取不到企业状态。corpCode="+corpCode);
				return -2;
			}
			return Integer.parseInt(corpList.get(0).get("corp_state").toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "智能引擎，根据企业编码查询企业状态，异常。corpCode="+corpCode);
			return -9999;
		}
	}
}
