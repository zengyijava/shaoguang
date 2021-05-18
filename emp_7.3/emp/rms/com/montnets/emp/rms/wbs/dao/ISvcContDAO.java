package com.montnets.emp.rms.wbs.dao;

import java.util.List;

import com.montnets.emp.rms.wbs.model.SvcCont;
/**
 * 接口，主要完成对表LF_RMS_SYN_TEMP( MBOSS报表同步中间表)的数据操作标准
 * @ClassName ISvcContDAO
 * @Description TODO
 * @author zhouxiangxian 203492752@qq.com
 * @date 2018年1月11日
 */
public interface ISvcContDAO {
	/**
	 * 将MBOSS同步过来的数据添加到表LF_RMS_SYN_TEMP中，完成数据的批量插入
	* @Title: doInsertBatch 
	* @Description: TODO
	* @param @param svcConts    要插如的数据
	* @param @return   批量插入是否成功
	* @param @throws Exception    设定文件      有异常向上抛出
	* @return boolean    返回类型    批量插入数据是否成功
	* @throws
	 */
	public boolean doInsertBatch(List<SvcCont> svcConts)throws Exception;
	
	
	/**
	 * 完成单条数据的插入操作
	* @Title: doInsert 
	* @Description: TODO
	* @param @param svcCont    要插入的对象
	* @param @return
	* @param @throws Exception    设定文件     有异常向上抛出
	* @return boolean    返回类型 
	* @throws
	 */
	public boolean doInsert(SvcCont svcCont)throws Exception;

	
	/**
	 * 数据转移操作，调用存储过程完成
	* @Title: doTransferData 
	* @Description: TODO
	* @param @return    是否成功
	* @param @throws Exception    有异常交由调用处处理
	* @return boolean    返回类型 
	* @throws
	 */
	public boolean doTransferData()throws Exception;
	
	
	
}
