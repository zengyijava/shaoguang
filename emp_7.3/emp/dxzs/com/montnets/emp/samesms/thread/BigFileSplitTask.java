package com.montnets.emp.samesms.thread;

import java.io.BufferedReader;
import java.util.List;

import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sms.LfBigFile;
import com.montnets.emp.samesms.biz.BigFileSplitBiz;


public class BigFileSplitTask extends Thread{

	LfBigFile bigFile ;
	List<BufferedReader> readerList;
	PreviewParams preParams;
	String langName;
	public BigFileSplitTask(LfBigFile bigFile, List<BufferedReader> readerList, PreviewParams preParams,String langName) {
		this.bigFile = bigFile;
		this.readerList = readerList;
		this.preParams = preParams;
		this.langName = langName;
		setName("超大文件处理线程："+bigFile.getFileName());
	}
	public void run() {
		try {
			new BigFileSplitBiz().splitBigFile(bigFile,readerList,preParams,langName);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "超大文件拆分线程异常，文件ID:"+bigFile.getId());
		}
		
	}

}
