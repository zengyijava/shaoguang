package com.montnets.emp.rms.rmsapi.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.biz.DegreeCountBiz;
import com.montnets.emp.rms.rmsapi.biz.impl.IDegreeCountBiz;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;

/**
 * 
 * 文件名称:DegreeCountUtil.java
 * 文件描述:档位计算工具类
 * 内容摘要:
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-22    qiyin     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   qiyin<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-1-22 下午12:06:46
 */
public class DegreeCountUtil
{

	private static DegreeCountUtil instance = new DegreeCountUtil();

	private DegreeCountUtil()
	{

	}

	public static synchronized DegreeCountUtil getInstance()
	{
		if (instance == null)
		{
			instance = new DegreeCountUtil();
		}
		return instance;
	}

	/**
	 * 档位计算
	 *@anthor qiyin<15112605627@163.com>
	 *@param kbSize 文件大小，单位：kb
	 *@return  返回计算档位
	 *@throws Exception
	 */
	public String countDegree(int kbSize)
	{
		DegreeCountBiz degreeBize = new IDegreeCountBiz();
		String result = RMSHttpConstant.DEFAULT_DEGREE;
		try
		{
			result = degreeBize.countDegree(kbSize);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "档位计算出错");
		}
		return result;
	}
	
	public Map<String,String> queryDegree()throws Exception{
		Map<String,String> degMap=new LinkedHashMap<String, String>();
		try
		{
			DegreeCountBiz degreeBize = new IDegreeCountBiz();
			degMap = degreeBize.queryDegree();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "档位计算出错");
		}
		return degMap;
	}

}
