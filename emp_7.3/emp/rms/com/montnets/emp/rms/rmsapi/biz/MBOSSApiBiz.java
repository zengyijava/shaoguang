package com.montnets.emp.rms.rmsapi.biz;

import java.util.Map;

import com.montnets.emp.rms.rmsapi.model.QueryHisRecordParams;
/**
 * 文件名称:MBOSSApiBiz.java
 * 文件描述:功能描述
 * 内容摘要:富信平台接口实现类
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-22    陈林艳     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   chenlinyan<15773241398@163.com>
 * @version:  0.1  
 * @Date:     2018-1-22 下午14:52:08
 *
 *
 */
public interface MBOSSApiBiz {
	
	/**
	 * 查询富信历史记录
	 * @author chenlinyan<15773241398@163.com>
	 * @param params 查询历史记录参数
	 * @return map{rstate:'',resultMsg:'',statuscode:'',scont:List<map<String,String>>}
	 * @throws Exception
	 */
	public Map<String,Object> queryHisRecord(QueryHisRecordParams params) throws Exception;
	
}
