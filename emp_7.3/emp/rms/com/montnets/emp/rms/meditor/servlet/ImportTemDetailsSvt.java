package com.montnets.emp.rms.meditor.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.meditor.biz.imp.ImportTempBiz;
import com.montnets.emp.rms.meditor.biz.imp.ImportTempDetailsBiz;
import com.montnets.emp.rms.meditor.dto.LfTempImportBatchDto;
import com.montnets.emp.rms.meditor.vo.LfTempImportDetailsVo;
import com.montnets.emp.rms.vo.LfTempImportBatchVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @description 批量导入详情
 * @author hsh
 * @datetime 2018-11-07 上午09:27:16
 */
@SuppressWarnings("serial")
public class ImportTemDetailsSvt extends BaseServlet {

    final String empRoot = "rms";
    final String basePath = "/meditor";
    

    final SuperOpLog spLog = new SuperOpLog();
    final BaseBiz baseBiz = new BaseBiz();
    private final String path = new TxtFileUtil().getWebRoot();

    //常用文件读写工具类
    final TxtFileUtil txtfileutil = new TxtFileUtil();

    final ImportTempDetailsBiz detailsBiz = new ImportTempDetailsBiz();
    final ImportTempBiz tempBiz  = new ImportTempBiz();
    
    public void find(HttpServletRequest request, HttpServletResponse response) {

        //短信biz
        List<LfTempImportBatchVo> tempBatchs = null;
        LfTempImportBatchDto batchDto = new LfTempImportBatchDto();
        //emp_sta/importTemplateDetails.htm
        String requestPath = request.getRequestURI(); 

        PageInfo pageInfo = new PageInfo();
        
      //任务批次
    	String batch= "";
    	//企业编码
    	String corpCode= "";
    	//企业名称
    	String corpName= "";
    	//状态
    	String processStatus="";
    	//开始时间
    	String addtimeStart = "";
    	//结束时间
    	String addtimeEnd = "";
    	
    	boolean isFirstEnter = true;

        try {
        	//任务批次
        	batch= request.getParameter("batch");
        	//企业编码
        	corpCode= request.getParameter("corpCode");
        	if (StaticValue.getCORPTYPE() == 0){
        		corpCode = "100001";
			}
        	//企业名称
        	corpName= request.getParameter("corpName");
        	//状态
        	processStatus=request.getParameter("processStatus");
        	//开始时间
        	addtimeStart = request.getParameter("addtimeStart");
        	//结束时间
        	addtimeEnd = request.getParameter("addtimeEnd");
        	
        	isFirstEnter = pageSet(pageInfo, request); 
        	if(!isFirstEnter){
        		//处理查询条件的值
            	if(batch!=null&&!"".equals(batch.trim())){
            		batchDto.setBatch(Long.valueOf(batch));
            	}
            	
            	if(corpCode!=null&&!"".equals(corpCode.trim()))
            	{
            		batchDto.setCorpCode(corpCode);
            	}
				if (StaticValue.getCORPTYPE() == 0){//标准版
					batchDto.setCorpCode("100001");
				}
            	
            	if(corpName!=null && !"".equals(corpName))
            	{
            		batchDto.setCorpName(corpName);
            	}

//            	if(processStatus!=null || "".equals(processStatus))
//            	{
//            		processStatus="0";
//            	}
            	
            	if(addtimeStart!=null && !"".equals(addtimeStart))
            	{
            		batchDto.setAddtimeStart(addtimeStart);
            	}
            	
            	if(addtimeEnd!=null && !"".equals(addtimeEnd))
            	{
            		batchDto.setAddtimeEnd(addtimeEnd);
            	}
            	
        	}
        	
        	//查询批量导入数据
        	tempBatchs = detailsBiz.findLfTempImportBatch(batchDto, pageInfo);

        	request.setAttribute("tempBatchs", tempBatchs);
            request.setAttribute("isFirstEnter", isFirstEnter);
            request.setAttribute("pageInfo", pageInfo);
           
            //页面跳转
            request.getRequestDispatcher(this.empRoot + this.basePath + "/importTemplateDetails.jsp").forward(request, response);
        } catch (Exception e) {
            try {
                request.getRequestDispatcher(this.empRoot + this.basePath + "/importTemplateDetails.jsp").forward(request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1, "富新应用批量导入详情serlvet异常！");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "富新应用批量导入详情serlvet跳转异常！");
            }
        }
    
    }
    
	public void getFails(HttpServletRequest request, HttpServletResponse response) {

        //短信biz
        List<LfTempImportDetailsVo> details = null;
        LfTempImportDetailsVo detailsDto = new LfTempImportDetailsVo();

        PrintWriter out = null;
      //任务批次
    	String batch= "";
    	String corpCode= "";
        try {
        	//任务批次
        	batch= request.getParameter("batch");
        	
        	
			//处理查询条件的值
            if(batch==null||"".equals(batch.trim())){
        		batch="";
        	}else{
        		try{
        			Long.parseLong(batch.trim());
        			batch=batch.trim();
        		}catch(Exception e){
        			EmpExecutionContext.error(e, "任务批次号转换异常!taskID:" + batch);
        			batch="";
        		}
        	}
            if(batch==null||"".endsWith(batch)){
            	detailsDto.setBatch(0l);
            }else{
            	detailsDto.setBatch(Long.valueOf(batch));
            }
            
            //任务批次
        	corpCode= request.getParameter("corp");
            if(corpCode!=null&&!"".equals(corpCode.trim())){
            	detailsDto.setCorpCode(corpCode);
            }
			if (StaticValue.getCORPTYPE() == 0){//标准版
				detailsDto.setCorpCode("100001");
			}
            
            //插入失败条件
            detailsDto.setImportStatus(0);
            
            out = response.getWriter();
        	//查询批量导入数据
            details = detailsBiz.findLfTempImportDetails(detailsDto, null);
            JSONArray array = null;
            if(details==null||details.size()==0){
            	out.write("");
            }else{
            	//将list对象转换成json
            	array = JSONArray.parseArray(JSON.toJSONString(details));
    			out.write(array.toString());
            }

        } catch (Exception e) {
            try {
                request.getRequestDispatcher(this.empRoot + this.basePath + "/importTemplateDetails.jsp").forward(request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1, "富新应用批量导入详情serlvet异常！");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "富新应用批量导入详情serlvet跳转异常！");
            }
        }
    
    }
	
    
    /**
     * 群发历史查询中发送详情信息查看
     *
     * @param request
     * @param response
     * @throws Exception
     */
    //@SuppressWarnings({"static-access", "deprecation"})
    public void findAllSendInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {


        //短信biz
        List<LfTempImportDetailsVo> details = null;
        LfTempImportDetailsVo detailsDto = new LfTempImportDetailsVo();
        
        List<LfTempImportBatchVo> tempBatchs = null;
        LfTempImportBatchDto tempBatchDto = new LfTempImportBatchDto();
		LfTempImportBatchVo tempBatch = null;
        PageInfo pageInfo = new PageInfo();
        boolean isFirstEnter = true;
        
      //任务批次
    	String batch= "";
    	//手机号码
    	String phoneNum= "";
    	//导入状态
    	String importStatus= "";
    	//审核状态
    	String auditstatus= "";
    	//企业编码
    	String corpCode= "";
    	
    	
        try {
        	//任务批次
        	batch= request.getParameter("batch");
        	//手机号码
        	phoneNum= request.getParameter("phoneNum");
        	//导入状态
        	importStatus= request.getParameter("importStatus");
        	//审核状态
        	auditstatus= request.getParameter("auditstatus");
        	
			//处理查询条件的值
            if(batch==null||"".equals(batch.trim())){
        		batch="";
        	}else{
        		try{
        			Long.parseLong(batch.trim());
        			batch=batch.trim();
        		}catch(Exception e){
        			EmpExecutionContext.error(e, "任务批次号转换异常!taskID:" + batch);
        			batch="";
        		}
        	}
            if(batch==null||"".endsWith(batch)){
            	detailsDto.setBatch(0l);
            	tempBatchDto.setBatch(0l);
            }else{
            	detailsDto.setBatch(Long.valueOf(batch));
            	tempBatchDto.setBatch(Long.valueOf(batch));
            }
            
          //企业编码
        	corpCode= request.getParameter("corp");
            if(corpCode!=null&&!"".equals(corpCode.trim())){
            	detailsDto.setCorpCode(corpCode);
            }
            if (StaticValue.getCORPTYPE() == 0){//标准版
				detailsDto.setCorpCode("100001");
			}
            
            isFirstEnter = pageSet(pageInfo, request); 
	        if(!isFirstEnter){
	            if(phoneNum!=null&&!"".equals(phoneNum.trim())){
	            	detailsDto.setPhoneNum(phoneNum);
	        	}
	            
	            if(importStatus!=null&&!"".equals(importStatus.trim())){
	            	detailsDto.setImportStatus(Integer.valueOf(importStatus));
	        	}
	            
	            if(auditstatus!=null&&!"".equals(auditstatus.trim())){
	            	detailsDto.setAuditstatus(Integer.valueOf(auditstatus));
	        	}
        	}
        	//查询批量导入数据
            tempBatchs = detailsBiz.findLfTempImportBatch(tempBatchDto, null);
            if(tempBatchs!=null&&tempBatchs.size()>0){
            	tempBatch = tempBatchs.get(0);
            }else{
            	tempBatch = null;
            }
            
            details = detailsBiz.findLfTempImportDetails(detailsDto, pageInfo);
            
            request.setAttribute("details", details);
            request.setAttribute("tempBatch", tempBatch);
            request.setAttribute("pageInfo", pageInfo);
            
            request.getRequestDispatcher(this.empRoot + this.basePath + "/importDetails.jsp").forward(request, response);
        } catch (Exception e) {
            try {
                request.getRequestDispatcher(this.empRoot + this.basePath + "/importDetails.jsp").forward(request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1, "富新应用批量导入详情serlvet异常！");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "富新应用批量导入详情serlvet跳转异常！");
            }
        }
    }
    
    
    
    public void smsReportAllExcel(HttpServletRequest request, HttpServletResponse response) {
            //短信biz
            List<LfTempImportDetailsVo> details = null;
            LfTempImportDetailsVo detailsDto = new LfTempImportDetailsVo();
            JSONObject exportTempJson = null;
            PrintWriter out = null;
          //任务批次
        	String batch= "";
        	//手机号码
        	String phoneNum= "";
        	//导入状态
        	String importStatus= "";
        	//审核状态
        	String auditstatus= "";
        	
            try {
            	//任务批次
            	batch= request.getParameter("batch");
            	//手机号码
            	phoneNum= request.getParameter("phoneNum");
            	//导入状态
            	importStatus= request.getParameter("importStatus");
            	//审核状态
            	auditstatus= request.getParameter("auditstatus");
            	
    			//处理查询条件的值
                if(batch==null||"".equals(batch.trim())){
            		batch="";
            	}else{
            		try{
            			Long.parseLong(batch.trim());
            			batch=batch.trim();
            		}catch(Exception e){
            			EmpExecutionContext.error(e, "任务批次号转换异常!taskID:" + batch);
            			batch="";
            		}
            	}
                if(batch==null||"".endsWith(batch)){
                	detailsDto.setBatch(0l);
                }else{
                	detailsDto.setBatch(Long.valueOf(batch));
                }

    	            if(phoneNum!=null&&!"".equals(phoneNum.trim())){
    	            	detailsDto.setPhoneNum(phoneNum);
    	        	}
    	            
    	            if(importStatus!=null&&!"".equals(importStatus.trim())){
    	            	detailsDto.setImportStatus(Integer.valueOf(importStatus));
    	        	}
    	            
    	            if(auditstatus!=null&&!"".equals(auditstatus.trim())){
    	            	detailsDto.setAuditstatus(Integer.valueOf(auditstatus));
    	        	}
            	
            	//查询批量导入数据 {"status":true,"data":{"src":"/D:/soft/tomcat/tomcat6/webapps/emp_sta/rms/meditor/file/download/importTemp/1541844955100.zip"}}
    	        exportTempJson = tempBiz.exportTempExcelByCondition(detailsDto, null);
    	        
    	        out = response.getWriter();
				out.write(exportTempJson.toString());

            } catch (Exception e) {

                EmpExecutionContext.error(e, "富新应用批量导入详情serlvet异常！");

            }
        }
    
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        
        JSONObject dataJson = null;
        PrintWriter out = null;
        DownloadFile downloadFile = new DownloadFile();
      //任务批次
    	String data = "";
    	String filePath = "";
    	String fileName = "";
        try {
        	//任务批次
        	data= request.getParameter("data");
        	if(data!=null){
//        		dataJson = JSONObject.parseObject(data); 
        		filePath = data;
        		if(filePath!=null&&!"".endsWith(filePath)){
        			fileName = filePath.substring(filePath.lastIndexOf("/")+1);
        		}	
        	}
			
        	downloadFile.downFile(request, response, filePath, fileName);
	           
        } catch (Exception e) {

            EmpExecutionContext.error(e, "富新应用批量导入详情serlvet异常！");

        }
    }

    public void smsSend(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //任务批次
    	String batch= "";  
    	String corpCode= "";
    	String phone= "";
    	//返回结果
		String result = "error";
		//获取操作员
		LfSysuser sysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		//lfMttask对象
		LfMttask mttask = new LfMttask();
		try {
        	//任务批次
        	batch = request.getParameter("batch");
			//企业编码
			corpCode = request.getParameter("corp");
			if (StaticValue.getCORPTYPE() == 0){
				corpCode = "100001";
			}
			//校验参数是否为空
			if(StringUtils.isBlank(batch) || !batch.matches("\\d+") || StringUtils.isBlank(corpCode)){
				throw new EMPException(IErrorCode.V10001);
			}

			//校验该批次任务是否有审核通过的号码，如果没有则直接报错
			long auditTemplate = checkAduitTemplate(batch);
			if(auditTemplate == 0){
				result = "noAuditTemplate";
				return;
			}

			//组装lf_mttask对象
			mttask.setTaskId(Long.parseLong(batch));
			mttask.setCorpCode(corpCode);
			mttask.setUserId(sysUser.getUserId());
			// 有效总数
			mttask.setEffCount(auditTemplate);

			//处理下行任务表
			tempBiz.handleLfMttask(mttask, sysUser);

            //调用发送接口
			result = tempBiz.sendMarathonRms(mttask);
        } catch (EMPException empex) {
			ErrorCodeInfo info = ErrorCodeInfo.getInstance();
			//获取自定义异常编码
			String message = empex.getMessage();
			//获取自定义异常提示信息
			result = info.getErrorInfo(message);
			EmpExecutionContext.error(empex, sysUser.getUserId().toString(), corpCode, info.getErrorInfo(message), message);
		} catch (Exception e) {
            EmpExecutionContext.error(e, "富新应用批量导入详情serlvet异常！");
        } finally {
			response.getWriter().print(result);
		}
    }

	private long checkAduitTemplate(String batch) {
    	int result = 0;
    	try {
			result = tempBiz.checkAduitTemplate(batch);
		}catch (Exception e){
			EmpExecutionContext.error("企业富信 > 数据查询 > 批量导入模板发送 获取审核通过的模板数异常");
		}
    	return result;
	}

}
