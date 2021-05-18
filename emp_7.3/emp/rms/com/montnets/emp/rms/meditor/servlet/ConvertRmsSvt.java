package com.montnets.emp.rms.meditor.servlet;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.meditor.thread.ConvertRmsThread;
import org.apache.axis.i18n.RB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class ConvertRmsSvt extends HttpServlet {
    @Override
    public void init() throws ServletException {
        EmpExecutionContext.info("开启富信1.0转成3.0线程...");
        ConvertRmsThread convertRmsThread = new ConvertRmsThread();
        Thread thread = new Thread(convertRmsThread,"convertRms");
        thread.start();
        EmpExecutionContext.info("富信1.0转成3.0转换完成");
    }
}
