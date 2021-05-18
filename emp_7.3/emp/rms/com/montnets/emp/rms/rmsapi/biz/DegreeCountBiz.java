package com.montnets.emp.rms.rmsapi.biz;

import java.util.Map;

/**
 * 
 * 文件名称:DegreeBiz.java
 * 文件描述:档位管理biz
 * 内容摘要:
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-22    qiyin     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   qiyin<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-1-22 下午12:01:39
 */
public interface DegreeCountBiz
{

	/**
	 * 档位计算
	 *@anthor qiyin<15112605627@163.com>
	 *@param kbSize 文件大小，单位：kb
	 *@return  返回计算档位
	 *@throws Exception
	 */
	public String countDegree(int kbSize) throws Exception;
	
	
	/**
	 * 有效档位查询
	 *@anthor qiyin<15112605627@163.com>
	 *@return map[{'1':'0-50'},{'2':'51-100'}...]
	 *@throws Exception
	 */
	public Map<String,String> queryDegree()throws Exception;
}
