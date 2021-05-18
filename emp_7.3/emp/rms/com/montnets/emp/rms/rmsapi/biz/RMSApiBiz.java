package com.montnets.emp.rms.rmsapi.biz;

import java.util.List;
import java.util.Map;

import com.montnets.emp.rms.rmsapi.model.SendTempParams;
import com.montnets.emp.rms.rmsapi.model.TempParams;

/**
 * 
 * 文件名称:RMSApiBiz.java
 * 文件描述:功能描述
 * 内容摘要:富信平台api biz接口
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-10    qiyin     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   qiyin<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-1-10 上午11:30:08
 */
public interface RMSApiBiz
{

	/**
	 * 上传模板接口
	 *@anthor qiyin<15112605627@163.com>
	 *@param userid 用户账号
	 *@param pwd 用户密码
	 *@param tempList 上传文件参数
	 *@return map{result:'',tmplid:''}
	 *@throws Exception
	 */
	public Map<String,String> subTemplate(String userid,String pwd,List<TempParams> tempList) throws Exception;

	/**
	 *
	 * @param userid 用户账号
	 * @param pwd 用户密码
	 * @param tempList 上传文件参数
	 * @param smcontent 模版摘要，用于发送明文短链的内容。
	 * @param smparamreg 模版摘要短信内容参数规则
	 * @param title     短信模板主题
	 * @param templVer
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> subTemplate(String userid,String pwd,List<TempParams> tempList,String smcontent,String smparamreg,String title,String templVer) throws Exception;

	/**
	 * 删除模板
	 *@anthor qiyin<15112605627@163.com>
	 *@param userid 用户账号
	 *@param pwd 用户密码
	 *@param tempIds 模板ids，多个用英文半角逗号隔开
	 *@return map{result:''}
	 *@throws Exception
	 */
	public Map<String,String> deleteTemplates(String userid,String pwd,String tempIds) throws Exception;
	
	/**
	 * 查询模板审核装状态
	 *@anthor qiyin<15112605627@163.com>
	 *@param userid 用户账号
	 *@param pwd 用户密码
	 *@param tempIds 模板ids，多个用英文半角逗号隔开
	 *@return  map{result:'',status:List<Map<String,String>>}
	 *@throws Exception
	 */
	public Map<String,Object> queryTemplateStatus(String userid,String pwd,String tempIds) throws Exception;
	
	/**
	 * 
	 * 模板发送
	 *@anthor qiyin<15112605627@163.com>
	 *@param tempParams 发送参数
	 *@return  map{result:'',msgid:'',custid:''}
	 *@throws Exception
	 */
	public Map<String,String> sendTemplate (SendTempParams tempParams) throws Exception;
	
	/**
	 * 下载模板
	 * @author chenlinyan<15773241398@163.com>
	 * @param userid 用户账号
	 * @param pwd 密码
	 * @param tempIds 模板id
	 * @return  map{result:'',timestamp:'',sign:'',content:List<TempParams>}
	 * @throws Exception
	 */
	public Map<String,Object> getTemplate(String userid,String pwd,String tempIds)throws Exception;

	/**
	 * 同步下载RCOS平台创建的公共模板
	 * @param userid
	 * @param pwd
	 * @param tempIds
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getTemplateFromRCOS(String userid,String pwd,String tempIds)throws Exception;

	/**
	 * 同步下载RCOS平台创建的企业侧定制模板
	 * @param userid
	 * @param pwd
	 * @param tempIds
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> syncEcTemplateFromRCOS(String userid,String pwd,String tempIds)throws Exception;

	/**
	 * 查询RCOS 创建的公共模板的状态
	 * @param userid
	 * @param pwd
	 * @param tempIds
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> queryTemplateStatusFromRCOS(String userid,String pwd,String tempIds)throws Exception;
}
