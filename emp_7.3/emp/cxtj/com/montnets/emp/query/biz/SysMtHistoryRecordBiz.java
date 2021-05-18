package com.montnets.emp.query.biz;

import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.query.dao.GenericSystemMtTask01_12VoDAO;
import com.montnets.emp.query.vo.SystemMtTask01_12Vo;
import com.montnets.emp.util.PageInfo;

/**
 * 系统下行历史记录查询
 * @author Administrator
 *
 */
public class SysMtHistoryRecordBiz
{

	//系统下行记录Dao
	private GenericSystemMtTask01_12VoDAO downHistory ;

	//构造函数
	public SysMtHistoryRecordBiz(){
		downHistory = new GenericSystemMtTask01_12VoDAO();
	}
	/**
	 * 查询下行历史记录
	 * @param curLongedUserId  当前登录用户
	 * @param mtTask01_12Vo  带条件的vo
	 * @param pageInfo  分页
	 * @return
	 * @throws Exception
	 */
	public List<SystemMtTask01_12Vo> getDownHisVos(SystemMtTask01_12Vo mtTask01_12Vo, PageInfo pageInfo) throws Exception
	{
		try
		{
			List<SystemMtTask01_12Vo> downHisVosList = downHistory.findSystemMtTask01_12Vo(mtTask01_12Vo, pageInfo);
			//没记录则清空一下pageinfo，因为会有缓存
			if(downHisVosList == null || downHisVosList.size() == 0)
			{
				pageInfo.setTotalRec(0);
				pageInfo.setTotalPage(1);
			}
			return downHisVosList;
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询下行历史记录异常。");
			return null;
		}
	}
	
	public boolean getDownHisPageInfo(SystemMtTask01_12Vo mtTask01_12Vo, PageInfo pageInfo)
	{
		return downHistory.findSystemMtTask01_12PageInfo(mtTask01_12Vo, pageInfo);
	}

	
	
	/**
	 * 获取分页信息
	 * @param pageIndex
	 * @param pageSize
	 * @param curLongedUserId
	 * @param mtTask01_12Vo
	 * @return
	 * @throws Exception
	 */
	public PageInfo getPageInfo(Integer pageIndex, int pageSize, Long curLongedUserId,
			SystemMtTask01_12Vo mtTask01_12Vo) throws Exception
	{
		//分页对象
		PageInfo pageInfo;
		try
		{

			//获取分页
			pageInfo = downHistory.getPageInfo(pageIndex, pageSize, mtTask01_12Vo);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取分页信息异常");
			throw e;
		}
		return pageInfo;
	}
}
