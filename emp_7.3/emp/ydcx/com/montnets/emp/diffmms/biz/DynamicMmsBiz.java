package com.montnets.emp.diffmms.biz;

import java.sql.Connection;

import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.MmsTemplate;

public class DynamicMmsBiz extends SuperBiz{

	/**
	 * 添加短彩信模板
	 * 
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public long addTemplate(MmsTemplate mmstemplate,LfTemplate template) throws Exception
	{
		Long id = 0L;		
		// 获取连接
		Connection conn = empTransDao.getConnection();
		try
		{
			// 开启事务
			empTransDao.beginTransaction(conn);
			ReviewBiz reviewBiz=new ReviewBiz();
			// 判断是否需要审核信息
			LfFlow flow = reviewBiz.getFlowBySysUserId(template.getUserId());
			// 如果是彩信存草稿也不需要审核
			if (flow == null
					|| (template.getTmpType() == 4 && template.getTmState() == 2))
			{

				template.setIsPass(0);
			}
			//如果是彩信模板
			if(mmstemplate != null)
			{
				//如果当前用户没有审批流且创建的是启用状态的模板，则需要保存网关平台的彩信模板
				if(flow == null && template.getTmState() == 1)
				{
					Long mmsId = empTransDao.saveObjectReturnID(conn, mmstemplate);
					//将网关平台的彩信模板id保存到系统中的关联字段中去。
					template.setMmstmplid(mmsId);
				}
			}
			// 保存数据库
			id = empTransDao.saveObjectReturnID(conn, template);
			boolean issuccess = false;
			if (flow != null && template.getTmState() != 2)
			{
				// 设置审批信息
				issuccess = reviewBiz.addFlowRecords(conn, id, template
						.getTmpType(), flow);

			}
			empTransDao.commitTransaction(conn);
			if (issuccess)
			{
				// 获取配置文件中的信息
				String isre = SystemGlobals.getSysParam("isRemind");
				if ("0".equals(isre))
				{
					new ReviewRemindBiz().reviewRemindTmp(null, id, 2);
				}
			}
		} catch (Exception e)
		{
			// 回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"彩信模板新增失败！");
			throw e;
		} finally
		{
			// 关闭连接
			empTransDao.closeConnection(conn);
		}
		return id.longValue();
	}

}
