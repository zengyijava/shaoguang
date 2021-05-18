/**
 * Program : ITemplateDAO.java
 * Author : zousy
 * Create : 2014-6-9 下午03:43:26
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.rms.meditor.dao;

import com.montnets.emp.rms.meditor.vo.LfTemplateVo;
import com.montnets.emp.util.PageInfo;

import java.util.List;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-9 下午03:43:26
 */
public interface ITemplateDAO
{

	/**
	 *   查询所有的彩信素材列表VO
	 * @param loginUserId	操作员用户ID
	 * @param lfTemplateVo	素材VO
	 * @param pageInfo	分页信息
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> findLfTemplateVoList(Long loginUserId, LfTemplateVo lfTemplateVo, PageInfo pageInfo) throws Exception;

	/**
	 *  获取所有彩信素材列表
	 * @param loginUserId	操作员用户ID
	 * @param lfTemplateVo	素材VO
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> findLfTemplateVoList(Long loginUserId, LfTemplateVo lfTemplateVo) throws Exception;

}
