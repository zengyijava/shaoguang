package com.montnets.emp.statecode.biz;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.statecode.LfStateCode;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.statecode.dao.sta_stateCodeDao;
import com.montnets.emp.util.PageInfo;

/**
 * 
 *<p>project name p_xtgl</p>
 *<p>Title: sta_stateCodeBiz</p>
 *<p>Description: </p>
 *<p>Company: Montnets Technology CO.,LTD.</p>
 * @author pengj
 * @date 2016-1-15 上午09:51:25
 */
public class sta_stateCodeBiz extends SuperBiz {
	
	private sta_stateCodeDao stateCodeDao;
	public sta_stateCodeBiz()
	{
		stateCodeDao = new sta_stateCodeDao();
	}

	/**
	 * FunName:查询状态码集合
	 * Description:
	 * @param LfStateCode 过滤条件 ，pageInfo 分页信息
	 * @retuen List<LfBusManagerVo> 业务集合
	 */
	public List<LfStateCode> getLfStateCode(LfStateCode lfStateCode, PageInfo pageInfo)throws Exception 
	{
		List<LfStateCode> LfStateCodeList = null;
		try
		{
			if (pageInfo == null)
			{
				//busManagerVosList = busTypeDao.findLfMttaskVo(lfBusManagerVo);
				LfStateCodeList = stateCodeDao.findLfStateCode(lfStateCode);
			} else
			{
				LfStateCodeList = stateCodeDao.findLfStateCode(lfStateCode, pageInfo);
			}
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "状态码查询异常。");
			//异常处理
			throw e;
		}
		return LfStateCodeList;
	}
	/**
	 * 获取操作员对象
	 * @param request
	 * @return
	 */
	public LfSysuser getCurrenUser(HttpServletRequest request){
		try
		{
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj == null)
			{
				return null;
			}
			
			return (LfSysuser)loginSysuserObj;
			
		}catch(Exception e)
		{
			EmpExecutionContext.error("SESSION获取操作员对象失败。");
			return null;
		}
	}
	
}
