package com.montnets.emp.rms.templmanage.biz;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.LfTmplRela;
import com.montnets.emp.entity.template.MmsTemplate;
import com.montnets.emp.rms.templmanage.dao.MbglTemplateDAO;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.table.template.TableLfTmplRela;
import com.montnets.emp.util.PageInfo;

public class MbglTemplateBiz extends SuperBiz
{
	
	
	
	/**
	 * 通过模板名称获取模板ID
	 * 
	 * @param tmName
	 * @return
	 * @throws Exception
	 */
	private Long getTmIdByTmName(String tmName) throws Exception
	{
		Long tmId = null;
		// 设置条件的MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			// 设置其条件
			conditionMap.put("tmName", tmName);
			// 进行查询
			List<LfTemplate> tmNames = empDao.findListByCondition(
					LfTemplate.class, conditionMap, null);
			LfTemplate template = null;
			// 返回ID
			if (tmNames != null && tmNames.size() != 0)
			{
				//获取第一条记录
				template = tmNames.get(0);
				//获取id
				tmId = template.getTmid();
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取彩信模板出现异常！");
			throw e;
		}
		//返回结果
		return tmId;
	}
	
	/**
	 * 根据条件获取该操作员的彩信素材信息
	 * 
	 * @param curLoginedUserId
	 * @param lfTemplateVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> getTemplateByCondition(Long curLoginedUserId,
			LfTemplateVo lfTemplateVo, PageInfo pageInfo) throws Exception
	{
		//当前登录操作员id
		if (curLoginedUserId == null)
		{
			//返回
			return null;
		}
		//模板对象集合
		List<LfTemplateVo> templateVosList = null;
		try
		{
			//分页不为空，需要分页
			if (pageInfo != null)
			{
				//按条件获取记录，分页
				templateVosList = new MbglTemplateDAO().findLfTemplateVoList(curLoginedUserId, lfTemplateVo,
								pageInfo);
			} else
			{
				//按条件获取所有记录
				templateVosList = new MbglTemplateDAO().findLfTemplateVoList(curLoginedUserId, lfTemplateVo);
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取该操作员的彩信素材信息失败！");
			throw e;
		}
		//返回结果
		return templateVosList;
	}
	
	
	public boolean updateTemplate(LfTemplate lmt){
		return  new MbglTemplateDAO().updateTemplate(lmt);
		
	}

	public LfTemplate getTmplateByTmid(long tmid) {
		return  new MbglTemplateDAO().getTmplateByTmid(tmid);
	}
	
	
	/**
	 * 添加富信模板
	 * 
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public long addTemplate(MmsTemplate mmstemplate,LfTemplate template) throws Exception
	{
		Long id = 0L;		
		boolean issuccess = false;
		LfTmplRela shareTemp=new LfTmplRela();
		// 获取连接
		Connection conn = empTransDao.getConnection();
		try
		{
			// 开启事务
			empTransDao.beginTransaction(conn);
			ReviewBiz reviewBiz=new ReviewBiz();
			// 判断是否需要审核信息
			LfFlow flow = reviewBiz.checkUserFlow(template.getUserId()
					,template.getCorpCode(),template.getTmpType().toString());
			// 如果是彩信存草稿也不需要审核
			if (flow == null
					|| (template.getTmpType() == 4 && template.getTmState() == 2))
			{

				template.setIsPass(0);
			}
			//如果是彩信模板
			if(mmstemplate != null)
			{
				shareTemp.setTemplType(2);
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
			//添加模板到lf_all_template
			shareTemp.setCorpCode(template.getCorpCode());
			shareTemp.setCreaterId(template.getUserId());
			shareTemp.setCreateTime(new Timestamp(System.currentTimeMillis()));
			shareTemp.setTemplId(id);
			shareTemp.setToUser(template.getUserId());
			empTransDao.save(conn, shareTemp);
			if (flow != null && template.getTmState() != 2)
			{
				// 设置审批信息
				issuccess = reviewBiz.addFlowRecordChild(conn, id, 
					template.getTmName(), template.getAddtime(), template.getTmpType(), 
					flow.getFId(), flow.getRLevelAmount(), template.getUserId(),
					"1", Integer.valueOf(template.getDsflag().toString()),null);

			}
			empTransDao.commitTransaction(conn);
		
		} catch (Exception e)
		{
			// 回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"添加彩信模板失败！");
			throw e;
		} finally
		{
			// 关闭连接
			empTransDao.closeConnection(conn);
			if (issuccess)
			{
				// 获取配置文件中的信息
				String isre = SystemGlobals.getSysParam("isRemind");
				if ("0".equals(isre))
				{
					LfFlowRecord record = new LfFlowRecord();
            		record.setInfoType(template.getTmpType());
            		record.setMtId(id);
            		record.setProUserCode(template.getUserId());
            		record.setRLevelAmount(1);
            		record.setRLevel(0);
            		ReviewRemindBiz rere=new ReviewRemindBiz();
            		//调用审批提醒方法
            		rere.flowReviewRemind(record);
            		rere.sendMail(record);
				}
			}
		}
		return id.longValue();
	}
	
	
	/**
	 * 删除富信模板ID
	 * 
	 * @param tmId
	 * @return
	 * @throws Exception
	 */
	public Integer delTempByTmId(String[] tmId) throws Exception
	{
		Integer delNum = 0;
		Connection conn = empTransDao.getConnection();
		PreparedStatement ps = null;
		try
		{
			empTransDao.beginTransaction(conn);
			// 循环获取模板ID
			String ids = "";
			//循环处理id
			for (int index = 0; index < tmId.length; index++)
			{
				ids += tmId[index] + ",";
			}
			//id不为空
			if (ids != null && ids.length() != 0)
			{
				//截取最后一个逗号
				ids = ids.substring(0, ids.lastIndexOf(","));
			} else
			{
				//返回
				return null;
			}
			// 进行数据库删除
			//delNum = empDao.delete(LfTemplate.class, ids);
			delNum = empTransDao.delete(conn, LfTemplate.class, ids);
//			//删除关联的模板共享表
//			//彩信模板type为2
			if(ids.length()>0){
				String delSql="delete from "+TableLfTmplRela.TABLE_NAME+" where "+TableLfTmplRela.templId+" in("+ids+") and "+TableLfTmplRela.templType+"= 2";
				ps = conn.prepareStatement(delSql);
				ps.executeUpdate();
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			conditionMap.put("mtId", ids);
			conditionMap.put("infoType", "3,4");
			objectMap.put("isComplete", "1");
			//empDao.update(LfFlowRecord.class, objectMap, conditionMap);
			empTransDao.update(conn, LfFlowRecord.class, objectMap, conditionMap);
			empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			//异常处理
			delNum = 0;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "删除富信模板失败！");
		}finally{
			if(ps != null){
				ps.close();
			}
			empTransDao.closeConnection(conn);
			
		}
		//返回结果
		return delNum;
	}
	
}
