/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-8-28 下午08:35:56
 */
package com.montnets.emp.appwg.initbuss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @description
 * @project emp_std_186
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-8-28 下午08:35:56
 */

public class HandleStaticValue
{
//	上行消息
	public static final StoreCache moReStoreCache = new StoreCache();
//	个人消息状态报告
	public static final StoreCache pmRptReStoreCache = new StoreCache();
//	公共消息状态报告
	public static final StoreCache emRptReStoreCache = new StoreCache();
}

