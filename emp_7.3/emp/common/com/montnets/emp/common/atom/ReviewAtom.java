package com.montnets.emp.common.atom;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;

public class ReviewAtom extends SuperBiz
{
	
	/**
	 * 审批提醒
	 * @param mtId 任务Id
	 * @param userid 操作员Id
	 */
	public void doRevRemain(Long mtId,Long userid)
	{
		//审批提醒开关 
		String isre=SystemGlobals.getSysParam("isRemind");
    	//如果开启审批提醒
    	if("0".equals(isre))
    	{
    		LfFlowRecord record = new LfFlowRecord();
    		record.setInfoType(1);
    		record.setMtId(mtId);
    		record.setProUserCode(userid);
    		record.setRLevelAmount(1);
    		record.setRLevel(0);
    		ReviewRemindBiz rere=new ReviewRemindBiz();
    		//调用审批提醒方法
    		rere.flowReviewRemind(record);
    		rere.sendMail(record);
    	}
	}
	
	/**
	 * 根据用户userid得到其审批流
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public LfFlow getFlowBySysUserId(Long userId) throws Exception
	{
		LfFlow flow = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		String username = "";
        //得到当前用户信息
		LfSysuser lso = empDao.findObjectByID(LfSysuser.class, userId);
		if (lso != null)
		{
			username = lso.getUserName();
		}
		try
		{
		    //查找当前用户是否存在审批流信息
			conditionMap.put("userId", username);
			conditionMap.put("corpCode", lso == null ? null : lso.getCorpCode());
			conditionMap.put("FType", "1");
			List<LfFlow> flowsList = empDao.findListByCondition(LfFlow.class,
					conditionMap, null);
			if (flowsList != null && flowsList.size() == 1)
			{
				flow = flowsList.get(0);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取用户审核流异常，用户编号：" +userId);
			//异常处理
			throw e;
		}
		return flow;
	}

}
