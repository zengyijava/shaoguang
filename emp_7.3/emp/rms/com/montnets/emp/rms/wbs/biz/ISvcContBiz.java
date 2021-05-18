package com.montnets.emp.rms.wbs.biz;

import java.util.List;
import java.util.Map;

import com.montnets.emp.rms.wbs.model.SvcCont;

/**
 * 业务层接口
 * @ClassName ISvcContBiz
 * @Description TODO
 * @author zhouxiangxian 203492752@qq.com
 * @date 2018年1月11日
 */
public interface ISvcContBiz {
	/**
	 *业务层操作，完成数据的批量插入
	 */
	public boolean insertBatch(List<SvcCont> svcConts)throws Exception;
	
	/**
	 * 业务层操作，单条数据的插入操作
	 */
	public boolean insertSimple(SvcCont svcCont)throws Exception;

	
	/**
	 * 数据转移操作，调用存储过程完成
	* @Title: transferDate 
	* @Description: TODO
	* @param @throws Exception   有异常交友调用处处理
	* @return void    返回类型 
	* @throws
	 */
	public void  transferDate()throws Exception;
	
	/**
	 * 查询sp账号与企业编码的关系
	* @Title: findSpAndCorpCode 
	* @Description: TODO
	* @param @return    
	* @param @throws Exception    设定文件    有异常交由调用处处理
	* @return Map<String,String>    返回类型    将查询结果存放在Map中
	* <li>key=sp账号,value=企业编码
	* @throws
	 */
	public Map<String,String> findSpAndCorpCode()throws Exception;
}
