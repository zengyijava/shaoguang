package com.montnets.emp.rms.meditor.servlet;

import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.meditor.thread.SynRcosTemplateStatusThread;
import com.montnets.emp.rms.meditor.thread.SyncEcTemplateThread;
import com.montnets.emp.rms.meditor.thread.SyncRcosThread;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * RCOS模板同步类
 */
public class SyncRcosSVT extends HttpServlet {
    /**
     *
     *   是否开启定时任务 0-不开启，1-开启
     */
    private int isruntimejob = SystemGlobals.getIntValue("montnets.rcos.template.syn.isruntimejob",1);
    @Override
    public void init() throws ServletException {

        if (isruntimejob == 1) {
            EmpExecutionContext.info("开启RCOS模板同步线程...");
            SyncRcosThread syncRcosThread = new SyncRcosThread();
            Thread thread = new Thread(syncRcosThread, "RCOS模板同步线程");
            thread.start();
            EmpExecutionContext.info("开启RCOS模板同步线程完成");

            EmpExecutionContext.info("开启RCOS企业侧模板同步线程...");
            SyncEcTemplateThread synecThread = new SyncEcTemplateThread();
            Thread thread2 = new Thread(synecThread, "RCOS企业侧模板同步线程");
            thread2.start();
            EmpExecutionContext.info("RCOS企业侧模板同步线程完成");

            EmpExecutionContext.info("开启RCOS公共模板状态同步线程...");
            SynRcosTemplateStatusThread synRcosTemplateStatusThread = new SynRcosTemplateStatusThread();
            Thread thread3 = new Thread(synRcosTemplateStatusThread, "RCOS公共模板状态同步线程");
            thread3.start();
            EmpExecutionContext.info("开启RCOS公共模板状态同步线程完成");
        }
    }
}
