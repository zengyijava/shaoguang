/**
 * Program : ITemplateBiz.java
 * Author : zousy
 * Create : 2014-6-9 下午03:43:12
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.rms.meditor.biz;

import com.montnets.emp.common.vo.LfMaterialVo;
import com.montnets.emp.entity.mmsmate.LfMaterial;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.MmsTemplate;
import com.montnets.emp.rms.meditor.vo.LfTemplateVo;
import com.montnets.emp.util.PageInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-9 下午03:43:12
 */
public interface ITemplateBiz
{

	/**
	 * 根据条件获取该操作员的彩信素材信息
	 * 
	 * @param curLoginedUserId
	 * @param lfTemplateVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> getTemplateByCondition(Long curLoginedUserId, LfTemplateVo lfTemplateVo, PageInfo pageInfo) throws Exception;

	/**
	 * 添加短彩信模板
	 * 
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public long addTemplate(MmsTemplate mmstemplate, LfTemplate template) throws Exception;

	/**
	 * 彩信模板编辑（只有草稿状态的模板才能进行编辑）
	 * 
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public boolean updateTemplate(MmsTemplate mmstemplate, LfTemplate template) throws Exception;

	/**
	 * 检测模板名称是否重复
	 * 
	 * @param tmName
	 * @return
	 * @throws Exception
	 */
	/*public boolean isTemplateNameExists(String tmName, String corpCode)
			throws Exception
	{
		//结果
		boolean result = false;
		// 设置条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			// 设置条件信息
			conditionMap.put("tmName", tmName);
			conditionMap.put("corpCode", corpCode);
			// 数据库查询
			List<LfTemplate> tempList = empDao.findListByCondition(
					LfTemplate.class, conditionMap, null);
			// 判断是否重复
			if (tempList != null && tempList.size() > 0)
			{
				//返回
				result = true;
			}
		} catch (Exception e)
		{
			// 异常
			EmpExecutionContext.error(e);
			throw e;
		}
		//返回结果
		return result;
	}
	 */
	/**
	 * 删除彩信模板ID
	 * 
	 * @param tmId
	 * @return
	 * @throws Exception
	 */
	public Integer delTempByTmId(String[] tmId) throws Exception;

	/**
	 * 判断名称是否有效
	 * 
	 * @param afTmName
	 * @param beTmId
	 * @return
	 * @throws Exception
	 */
	public Long validate(String afTmName, Long beTmId) throws Exception;

	/**
	 * 
	 * 通过用户名、获取彩信模板
	 * 
	 * @param curOperId
	 *            用户 ID
	 * 
	 * @return
	 * @throws Exception
	 */
	/*public List<LfTemplate> getTemplateByUserId(Integer curOperId)
			throws Exception
	{
		//模板对象集合
		List<LfTemplate> lfTemplates = new ArrayList<LfTemplate>();
		// 设置条件MAP
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			// 设置条件
			// 进行数据库查询
			String userId = visualPrivs(curOperId);
			//操作员id
			conditionMap.put("userId", userId);
			//获取记录
			lfTemplates = empDao.findListByCondition(LfTemplate.class,
					conditionMap, null);
		} catch (Exception e)
		{
			// 进入异常
			lfTemplates = null;
			EmpExecutionContext.error(e);
			throw e;
		}
		//返回结果
		return lfTemplates;
	}
	 */
	/**
	 * 获取所有用户的模板
	 * 
	 * @param dsflag
	 * @param tmpType
	 * @param loginUserId
	 * @return
	 * @throws Exception
	 */
	/*
		public List<LfTemplate> getAllUsingTemp(Integer dsflag, Integer tmpType,
				Long loginUserId) throws Exception
		{
			//模板对象集合
			List<LfTemplate> templates = new ArrayList<LfTemplate>();
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			try
			{
				//不为空
				if (dsflag != null)
				{
					conditionMap.put("dsflag", String.valueOf(dsflag));
				}
				//类型不为空
				if (tmpType != null)
				{
					conditionMap.put("tmpType", String.valueOf(tmpType));
				}
				//状态
				conditionMap.put("tmState", "1");
				// 查询模板
				templates = empDao.findListBySymbolsCondition(loginUserId,
						LfTemplate.class, conditionMap, null);
			} catch (Exception e)
			{
				//异常处理
				EmpExecutionContext.error(e);
				throw e;
			}
			//返回结果
			return templates;
		}
	 */
	public String changeParam(String str) throws Exception;

	/**
	 * 获取素材树数据
	 * @param corpCode
	 * @return
	 */
	public String getMaterialJosnData2(String corpCode);

	/**
	 * 通过分类编码获取素材分类ID
	 * @param childCode
	 * @return
	 * @throws Exception
	 */
	public Integer getSortIdByChildCode(String childCode) throws Exception;

	/**
	 *  通过父编码获取子类的IDS
	 * @param parentCode
	 * @return
	 * @throws Exception
	 */
	public String getSortIdsByParentCode(String parentCode) throws Exception;

	/**
	 *  获取彩信素材信息
	 * @param loginUserId	操作员ID
	 * @param conditionMap	条件
	 * @param pageInfo	分页信息
	 * @return
	 * @throws Exception
	 */
	public List<LfMaterialVo> getMaterialInfos(Long loginUserId, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception;

	/**
	 *  通过用户ID 获取其用户名称
	 * @return
	 * @throws Exception
	 */
	public Map<Long, String> getUserNameByUserId(String corpCode) throws Exception;

	/**
	 *  获取彩信模板信息
	 * @param loginUserId	操作员用户ID
	 * @param conditionMap
	 * @param pageInfo	分页信息
	 * @return
	 * @throws Exception
	 */
	public List<LfMaterial> getMaterialInfo(Long loginUserId, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception;

	public String updateShareTemp(String depidstr, String useridstr, String tempId, String InfoType, LfSysuser lfsysuser);

}
