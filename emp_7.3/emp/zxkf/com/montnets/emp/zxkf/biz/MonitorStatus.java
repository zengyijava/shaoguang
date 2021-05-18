/**
 * @description  
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-18 下午12:55:59
 */
package com.montnets.emp.zxkf.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.znly.biz.CustomChatBiz;
import com.montnets.emp.znly.biz.CustomStatusBiz;
import com.montnets.emp.znly.biz.GroupChatBiz;

/**
 * @description
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-18 下午12:55:59
 */

public class MonitorStatus implements Runnable
{
    // 客服状态操作Biz类
    CustomStatusBiz customStatusBiz = new CustomStatusBiz();
    GroupChatBiz groupChatBiz = new GroupChatBiz();
    CustomChatBiz chatBiz = new CustomChatBiz();
    /**
     * 用于监控客服状态
     */
    public void run()
    {
        
        try
        {
            // 检查是否存在超时的客服请求
            chatBiz.checkServerTime();
            // 定时清除群组消息内存
            groupChatBiz.checkGroupInfo();
            // 监控客服在线状态
            customStatusBiz.monitorCustomStatusForBackstage();
        }
        catch (InterruptedException interruptedException)
        {
            EmpExecutionContext.error(interruptedException,"监控客服状态线程run方法发生异常！");
            Thread.currentThread().interrupt();
        }
        catch (Exception exception)
        {
        	EmpExecutionContext.error(exception,"监控客服状态线程run方法发生异常！");
        }  
    }
}
