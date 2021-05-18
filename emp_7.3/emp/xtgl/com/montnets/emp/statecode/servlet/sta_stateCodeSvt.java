package com.montnets.emp.statecode.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.servlet.util.Excel2007Reader;
import com.montnets.emp.common.servlet.util.Excel2007VO;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.statecode.LfStateCode;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.statecode.biz.sta_stateCodeBiz;
import com.montnets.emp.statecode.util.StateCodeExcelTool;
import com.montnets.emp.statecode.util.ValidateUtil;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

@SuppressWarnings("serial")
public class sta_stateCodeSvt extends BaseServlet
{
	private final BaseBiz baseBiz=new BaseBiz();
	private final sta_stateCodeBiz stateCodeBiz=new sta_stateCodeBiz();

    private final String path = new TxtFileUtil().getWebRoot();
    //模板路径
    protected final String excelPath = path + "xtgl/statecode/file/";
    
    
	
	/**
	 * 状态码管理
	 * @Title: find
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException 
	 * @return void
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		List<LfStateCode> lfStateCodeList = null;
		LfStateCode lfStateCode = new LfStateCode();
		String stateCode = "";
		String mappingCode = "";
		String stateDes = "";
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap=new LinkedHashMap<String, String>();//排序规则
		
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		PageInfo pageInfo=new PageInfo();
		try
		{
			//是否第一次打开
			boolean isFirstEnter = false;
			
			isFirstEnter=pageSet(pageInfo, request);
			//分页设置
			if (!isFirstEnter)
			{
				//状态码
				stateCode = request.getParameter("stateCode");
				if(stateCode != null && !"".equals(stateCode)){
					//保存查询的状态,用于页面查询条件的回填处理
					lfStateCode.setStateCode(stateCode);
					conditionMap.put("stateCode&like",stateCode);
				}
				//映射码
				mappingCode = request.getParameter("mappingCode");
				if(mappingCode != null && !"".equals(mappingCode)){
					//保存查询的状态,用于页面查询条件的回填处理
					lfStateCode.setMappingCode(mappingCode);
					conditionMap.put("mappingCode&like",mappingCode);
				}
				
				//状态码说明
				stateDes = request.getParameter("stateDes");
				if(stateDes != null && !"".equals(stateDes)){
					//保存查询的状态,用于页面查询条件的回填处理
					lfStateCode.setStateDes(stateDes);
					conditionMap.put("stateDes&like",stateDes);
				}
			}
			String lgcorpcode = request.getParameter("lgcorpcode");
			lfStateCode.setCorpCode(lgcorpcode);
			conditionMap.put("corpCode", lgcorpcode);
			//只显示自定义状态码
//			lfStateCodeList = stateCodeBiz.getLfStateCode(lfStateCode, pageInfo);
			
			//排序条件
			orderbyMap.put("createTime","DESC" );
			orderbyMap.put("stateId","DESC" );
			//默认第一页查询总数
			//lfStateCodeList = baseBiz.getByConditionNoCount(LfStateCode.class,null, conditionMap, orderbyMap, pageInfo);
			//每页查询一次总数
			lfStateCodeList = baseBiz.getByCondition(LfStateCode.class,null, conditionMap, orderbyMap, pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"状态码查询失败！");
		}
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("lfStateCodeList", lfStateCodeList);
		request.setAttribute("lfStateCode", lfStateCode);
		//增加查询日志
		long end_time=System.currentTimeMillis();
		if(pageInfo!=null){
			String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
			opSucLog(request, "业务类型管理", opContent, "GET");
		}
		request.getRequestDispatcher("xtgl/statecode/sta_stateCode.jsp").forward(
				request, response);
	}
	/**
	 * 新增状态码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//状态码
		String stateCode = request.getParameter("stateCode");
		//映射码
		String mappingCode = request.getParameter("mappingCode");
		//状态码说明
		String stateDes = request.getParameter("stateDes");
		//当前登录操作员
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		String corpCode = lgcorpcode;
		try
		{
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("stateCode",stateCode);
			conditionMap.put("corpCode&in","0,1,2,"+corpCode);
			List<LfStateCode> stateCodeList = baseBiz.getByCondition(
					LfStateCode.class, conditionMap, null);
			//检查名称重复
			if (stateCodeList != null && stateCodeList.size() > 0)
			{
				response.getWriter().print("nameExists");
				return;
			}
			
			LfStateCode lfStateCode = new LfStateCode();
			//都不重复可以保存入库
			lfStateCode.setStateCode(stateCode);
			if("".equals(stateDes)||stateDes==null){
				lfStateCode.setStateDes(" ");
			}else{
				lfStateCode.setStateDes(stateDes);
			}
			//设置映射码条件
			boolean result =false;
			if("".equals(mappingCode)||mappingCode==null){
				lfStateCode.setMappingCode(stateCode);//映射码未填写，默认为状态码
			}else {
			     lfStateCode.setMappingCode(mappingCode);
			}
			lfStateCode.setCreateTime(new Timestamp(System.currentTimeMillis()));
			lfStateCode.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			lfStateCode.setCorpCode(corpCode);
			lfStateCode.setUserId(Long.parseLong(lguserid));
			result = baseBiz.addObj(lfStateCode);
			response.getWriter().print(result);
			
			/*
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "新建业务类型"+(result==true?"成功":"失败")+"。[业务名称，业务编码，业务类型，优先级别]" +
						"("+busName+"，"+busCode+"，"+busType+"，"+riseLevel+")";
				EmpExecutionContext.info("业务类型管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "ADD");
			}
			*/
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"新增状态码异常！");
		}
	}

	/**
	 * 删除状态码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//编号ID
		String stateId = request.getParameter("stateId");
		try
		{
			/*
			//查询操作之前记录
			LfBusManager befchgEntity = baseBiz.getById(LfBusManager.class, busId);
			String befchgCont = befchgEntity.getBusName()+"，"+befchgEntity.getBusCode()+"，"+befchgEntity.getBusType()+"，"
								+befchgEntity.getRiseLevel()+"，"+befchgEntity.getState();
			*/
			
			//异步返回删除结果
			String result = baseBiz.deleteByIds(LfStateCode.class, stateId) > 0 ? "true": "false";
			response.getWriter().print(result);

			/*
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "删除业务类型"+("true".equals(result)?"成功":"失败")+"。[业务名称，业务编码，业务类型，优先级别，状态]" +
						"("+befchgCont+")";
				EmpExecutionContext.info("业务类型管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "DELETE");
			}
			*/
			
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"删除状态码异常！");
		}
	}

	/**
	 * 修改状态码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//编号id
		String stateId = request.getParameter("stateId");
		//状态码
		String stateCode = request.getParameter("stateCode");
		//映射码
		String mappingCode = request.getParameter("mappingCode");
		//状态码说明
		String stateDes = request.getParameter("stateDes");
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		//当前登录操作员
		//String lguserid = request.getParameter("lguserid");
		// 漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		String corpCode = lgcorpcode;
		try
		{
			//LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			/*
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0,1,2,"+lgcorpcode);
			conditionMap.put("stateCode", stateCode);
			conditionMap.put("stateId&<>", stateId);
			*/
			/*
			List<LfStateCode> stateCodeList = baseBiz.getByCondition(
					LfStateCode.class, conditionMap, null);
			
			//检查状态码是否重复
			if (stateCodeList != null && stateCodeList.size() > 0)
			{
				response.getWriter().print("nameExists");
				return;
			}
			
			conditionMap.remove("stateCode");
			*/
			
			LfStateCode lfStateCode = baseBiz.getById(LfStateCode.class, stateId);
			/*
			//查询操作之前记录
			String befchgCont = bus.getBusName()+"，"+bus.getBusCode()+"，"+bus.getBusType()+"，"+bus.getRiseLevel()+"，"+bus.getState();
			*/
			lfStateCode.setStateCode(stateCode);
			if("".equals(stateDes)||stateDes==null){
				lfStateCode.setStateDes(" ");
			}else{
				lfStateCode.setStateDes(stateDes);
			}
			if("".equals(mappingCode)||mappingCode==null){
				lfStateCode.setMappingCode(" ");
			}else{
				lfStateCode.setMappingCode(mappingCode);
			}
			lfStateCode.setCorpCode(corpCode);
			lfStateCode.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			lfStateCode.setUserId(Long.parseLong(lguserid));
			//异步返回更新结果
			boolean result = baseBiz.updateObj(lfStateCode);
			response.getWriter().print(result+"#"+lfStateCode.getUpdateTime());
			/*
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "修改业务类型"+(result==true?"成功":"失败")+"。[业务名称，业务编码，业务类型，优先级别，状态]" +
						"("+befchgCont+")->("+busName+"，"+bus.getBusCode()+"，"+busType+"，"+riseLevel+"，"+state+")";
				EmpExecutionContext.info("业务类型管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}
			*/			
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"修改状态码异常！");
		}
	}
	
	/**
	 * 状态码文件导入操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void upload(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//状态码
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fileList = null;
		String resultMsg = "";
		String lgcorpcode = null;
		String lgguid = "";
		try {
			fileList = upload.parseRequest(request);
		} catch (FileUploadException e) {
			EmpExecutionContext.error(e,"状态码文件导入解析表单出现异常！");
			resultMsg = "error";
			return ;
		}
		LfSysuser sysuser =null;
		try{
			//获取登录sysuser
			sysuser =stateCodeBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("状态码文件导入,upload方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysuser.getCorpCode();
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("状态码文件导入,upload方法session中获取企业编码出现异常");
				return;
			}	
			lgguid=sysuser.getGuId()+"";
			if(lgguid==null||"".equals(lgguid)){
				EmpExecutionContext.error("状态码文件导入,upload方法session中获取GUID出现异常");
				return;
			}	
		}catch (Exception e) {
			EmpExecutionContext.error("状态码文件导入,upload方法获取登录sysuser出现异常");
		}
		
		Iterator<FileItem> it = fileList.iterator();
		while (it.hasNext()) {
			FileItem fileItem = (FileItem) it.next();
			String fileName = fileItem.getFieldName();
			if(fileName.equals("lgcorpcode")){
			}else if(fileName.equals("lgguid")){
			}else if (!fileItem.isFormField() && fileItem.getName().length() > 0) {
//				BufferedReader reader = null;
				try {
					String fileCurName = fileItem.getName();
					String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")+1);
					//判断是否为xls文件类型
					if ("xls".equals(fileType)){//xlsx
						//返回结果
						resultMsg = parseStateCodeByXls(request, resultMsg,lgcorpcode, sysuser, fileItem);
					} else if ("xlsx".equals(fileType)){
						//文件类型为xlsx
						resultMsg = parseStateCodeByXlsx(request, resultMsg, lgcorpcode, sysuser, fileItem);
					}
				} catch (Exception e) {
					EmpExecutionContext.error(e,"状态码文件导入时读取文件信息出现异常！");
					resultMsg = "error";
				} finally {
					try {
						fileItem.delete();
						String s = request.getRequestURI();
						s = s+"?method=find&lgguid="+lgguid+"&lgcorpcode="+lgcorpcode;
						request.getSession(false).setAttribute("ctrResult", resultMsg);
						response.sendRedirect(s);
					} catch (IOException e) {
						EmpExecutionContext.error(e,"状态码文件导入页面跳转出现异常！");
					}
					
				}
			}
		}
	}
	
	/**
	 * 获取xlsx文件单元格的值
	 * @param cell
	 * @return
	 */
	public static Object getCellFormatValue(XSSFCell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
            case XSSFCell.CELL_TYPE_NUMERIC:{
//                cellValue = String.valueOf(cell.getNumericCellValue());
            	DataFormatter dataFormatter=new DataFormatter();
            	cellValue = dataFormatter.formatCellValue(cell);
                break;
            }
            case XSSFCell.CELL_TYPE_FORMULA:{
                //判断cell是否为日期格式
                if(DateUtil.isCellDateFormatted(cell)){
                    //转换为日期格式YYYY-mm-dd
                    cellValue = cell.getDateCellValue();
                }else{
                    //数字
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            case XSSFCell.CELL_TYPE_STRING:{
                cellValue = cell.getRichStringCellValue().getString();
                break;
            }
            default:
                cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
	
	
	/**
	 * 解析并读取xlsx文件中数据
	 * @param request
	 * @param resultMsg
	 * @param lgcorpcode
	 * @param sysuser
	 * @param fileItem
	 * @return
	 */
	private String parseStateCodeByXlsx(HttpServletRequest request, String resultMsg, String lgcorpcode,
			LfSysuser sysuser, FileItem fileItem) {
		try
		{
			//excel有效行数
			int rows = 0;
			// 解析xlsx文件
			XSSFWorkbook xssfWorkBook = new XSSFWorkbook(fileItem.getInputStream());
			XSSFSheet xssfSheet = xssfWorkBook.getSheetAt(0);
			rows = xssfSheet.getLastRowNum() - 1;
			
			int maxNum = 1000;//单次导入只允许更新1000条
			
			// 一次导入的操作员超过最大数目
			if (rows < 1) {	//没有记录		
				resultMsg="noRecord";
				EmpExecutionContext.error("企业：" + lgcorpcode + "，没有记录导入");
			} else if(rows>maxNum) {//有效数据超过1000条
				//超过上限
				resultMsg="overCount";
				EmpExecutionContext.error("企业：" + lgcorpcode + "，导入超过上限");
			}else {
				//正常解析数据
				//获取标题行
				XSSFRow titleRow = xssfSheet.getRow(1);
				//判断是否使用正确的模板
				if(titleRow!=null&&titleRow.getPhysicalNumberOfCells()==3) {
				
					//1:查询出该企业已有的全部状态码信息
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("corpCode&in", "0,1,2," + lgcorpcode);
					List<LfStateCode> exisList = baseBiz.getByCondition(LfStateCode.class, conditionMap, null);
					//错误提示信息
					Map<Integer,String> errorMessage=new TreeMap<Integer,String>();
					//判断是否重复数据
					Set<String> repeatStateCode=new TreeSet<String>();
					//是否超过上限,默认为false,超过1000条数据时更新为true
					boolean overCount=false;
					//可以更新的状态码信息
					List<LfStateCode> updateList = new ArrayList<LfStateCode>();
					LfStateCode lfstatecode = null;
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					//int maxNum = 1000;//单次导入只允许更新1000条
					for (int k=2; k<rows+2; k++) {
						XSSFRow row = xssfSheet.getRow(k);
						StringBuffer errorBuffer=new StringBuffer();
						if (row == null) {
							//当前行没有任何的数据
	//						errorBuffer.append("状态码未填写").append("\t").append("映射码未填写");
							errorBuffer.append("状态码未填写");
							errorMessage.put(k+1, errorBuffer.toString());//保存当前行的错误信息
							//退出当前行的验证
							continue;
						}
						//状态码
						String stateCode = (String) getCellFormatValue(row.getCell(0));
						//映射码
						String mappingCode = (String) getCellFormatValue(row.getCell(1));
						//状态码说明
						String stateDes = (String) getCellFormatValue(row.getCell(2));
							
						
						//状态码不能为空
						if (stateCode.trim().length() < 1 )
						{
							errorBuffer.append("状态码未填写").append("\t");
						}
						//状态码超过7位
						if (stateCode.trim().length() > 7)
						{
							errorBuffer.append("状态码超过7位").append("\t");
						}
						//判断状态码是否存在
						Iterator<LfStateCode> iterator = exisList.iterator();
						while(iterator.hasNext()) {
							LfStateCode vo=iterator.next();
							if(vo.getStateCode().equals(stateCode)&&!"".equals(stateCode)) {
								errorBuffer.append("状态码已经存在").append("\t");
								break;
							}
						}
						if(!"".equals(stateCode)&&stateCode!=null) {
							//通过验证,可能存在重复的状态码
							int beforeSize=repeatStateCode.size();
							repeatStateCode.add(stateCode);
							int afterSize=repeatStateCode.size();
							if(afterSize==beforeSize) {
								//当前数据与之前的数据重复了
								errorBuffer.append("状态码重复").append("\t");
							}
						}
						//映射码验证
	//					if ("".equals(mappingCode) ) {
	//						errorBuffer.append("映射码未填写").append("\t");
	//					}
					
						if (!"".equals(mappingCode) && (mappingCode.length() > 7||!ValidateUtil.validate(mappingCode)))
						{
							errorBuffer.append("映射码只能输入字母或数字，且不超过7位").append("\t");
						}
						
						
						//状态码说明超过64位
						if (stateDes.trim().length() > 64)
						{
							errorBuffer.append("状态码说明超过64位").append("\t");
						}
						
						if(errorBuffer.length()>1) {
							//存在错误信息了，不进行下面操作
							errorMessage.put(k+1, errorBuffer.toString());
							continue;
						}
						//当前数据与之前数据不重复,可以添加
						lfstatecode = new LfStateCode();
						lfstatecode.setCorpCode(lgcorpcode);
						lfstatecode.setCreateTime(timestamp);
						lfstatecode.setMappingCode("".equals(mappingCode)?stateCode:mappingCode);
						lfstatecode.setStateCode(stateCode);
						lfstatecode.setStateDes(StringUtils.defaultIfEmpty(stateDes," "));
						lfstatecode.setUpdateTime(timestamp);
						lfstatecode.setUserId(sysuser.getUserId());
						updateList.add(lfstatecode);
						
				  }
				  resultMsg = createLastResultMsg(request, resultMsg, lgcorpcode, errorMessage, rows, overCount,updateList);
				}else {
					resultMsg="errorFile";
				}
			}
		} catch (Exception e){
			EmpExecutionContext.error(e, "处理xls文件出现异常！");
			resultMsg = "errorFile";
			/*EmpExecutionContext.error(e,"批量上传状态码出现异常！");
			// 判断是否因为上传了错误格式的xls文件
			if (e.getMessage().equals("Unable to recognize OLE stream")){
				request.setAttribute("result", "errorXls");
			}else{
				request.setAttribute("result", "false");
			}*/
		}
		return resultMsg;
	}
	
	
	
	public String parseStateCodeByXlsxOld(HttpServletRequest request, String resultMsg, String lgcorpcode,
			LfSysuser sysuser, FileItem fileItem) {
		try
		{
			
			//获取一个存放临时文件的目录
			String uploadPath = StaticValue.FILEDIRNAME;
			String temp = new TxtFileUtil().getWebRoot()+uploadPath;
			//close
			Excel2007Reader excel2007reader=new Excel2007Reader();
			Excel2007VO excel=excel2007reader.fileParset(temp, fileItem.getInputStream());
			if(excel==null||excel.getFirstline()==null||"".equals(excel.getFirstline())){//等于1表示标题
				//没有记录
				resultMsg = "noRecord";
				EmpExecutionContext.error("企业：" + lgcorpcode + "，没有记录导入");
			}else {
				//判断是否使用模板操作
				if(excel.getFirstline().split(",").length>2) {
					resultMsg="errorFile";
				}else {
					//新增状态信息
					//1:查询出该企业已有的全部状态码信息
					//存在的状态码list
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("corpCode&in", "0,1,2," + lgcorpcode);
					List<LfStateCode> exisList = baseBiz.getByCondition(LfStateCode.class, conditionMap, null);
					//错误提示信息
					Map<Integer,String> errorMessage=new HashMap<Integer,String>();
					//判断是否重复数据
					Set<String> repeatStateCode=new HashSet<String>();
					//导入的数据量
					int countNumber=0;
					//是否超过上限,默认为false,超过1000条数据时更新为true
					boolean overCount=false;
					//可以更新的状态码信息
					List<LfStateCode> updateList = new ArrayList<LfStateCode>();
					LfStateCode lfstatecode = null;
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					int maxNum = 1000;//单次导入只允许更新1000条
					BufferedReader reader = null;
					reader=excel.getReader();
					String charset = new ChangeCharset().get_charset(fileItem.getInputStream());
					if(charset.startsWith("UTF-"))
					{
						reader.read(new char[1]);
					}
					String tmp="";
					//计算行数
					int k=-1;
					// 获得excel工作簿对象
					String[] cells;
					while ((tmp = reader.readLine()) != null )
					{
						//需要每次都要初始化，不然如果下个没有值，就会为上个字段的值
						String stateCode = "";
						String mappingCode = "";
						String stateDes = "";
						// 循环获得单元行
						if("".equals(tmp)){
							continue;
						}
						k++;
						//标题跳过,提示信息跳过
						if(k<=4){
							continue;
						}
						cells = tmp.split(",");
						int size = cells.length;
						countNumber++;//文件数据量
						if(countNumber<maxNum) {
							
							//状态码
							if (size >1){
								stateCode =cells[1];
							}
							//映射码
							if (size >2){
								mappingCode =cells[2];
							}
							//状态码说明
							if (size >3){
								stateDes =cells[3];
							}
							
							//状态码不能为空
							if (stateCode.trim().length() < 1 )
							{
								errorMessage.put(k-2, "状态码未填写");//key表示行号,value:错误信息
								continue;
							}
							
							//状态码超过7位
							if (stateCode.trim().length() > 7)
							{
								errorMessage.put(k-2, "状态码超过7位");//key表示行号,value:错误信息
								continue;
							}
							
							//增加映射码不为空判断
							if(mappingCode==null||"".equals(mappingCode)) {
								errorMessage.put(k-2, "映射码不能为空");//key表示行号,value:错误信息,模板从第三行开始
								continue;
							}
							if (mappingCode != null && !"".equals(mappingCode) && (mappingCode.length() > 20||!ValidateUtil.validate(mappingCode)))
							{
								errorMessage.put(k-2, "映射码只能输入字母或数字，且不超过7位");//key表示行号,value:错误信息,模板从第三行开始
								continue;
							}
							//状态码说明超过64位
							if (stateDes.trim().length() > 64)
							{
								errorMessage.put(k-2, "状态码说明超过64位");//key表示行号,value:错误信息
								continue;
							}
							
							
							
							//判断状态码是否存在
							Iterator<LfStateCode> iterator = exisList.iterator();
							boolean exisFlag=false;	
							while(iterator.hasNext()) {
								LfStateCode vo=iterator.next();
								if(stateCode.equals(vo.getStateCode())) {
									//存在了
									exisFlag=true;
									errorMessage.put(k-2, "状态码已存在了");//key表示行号,value:错误信息
									break;
								}
							}
							if(exisFlag) {
								continue;
							}
							
							//通过验证,可能存在重复的状态码
							int beforeSize=repeatStateCode.size();
							repeatStateCode.add(stateCode);
							int afterSize=repeatStateCode.size();
							if(afterSize>beforeSize) {
								//当前数据与之前数据不重复,可以添加
								lfstatecode = new LfStateCode();
								lfstatecode.setCorpCode(lgcorpcode);
								lfstatecode.setCreateTime(timestamp);
								lfstatecode.setMappingCode(mappingCode);
								lfstatecode.setStateCode(stateCode);
								lfstatecode.setStateDes(StringUtils.defaultIfEmpty(stateDes," "));
								lfstatecode.setUpdateTime(timestamp);
								lfstatecode.setUserId(sysuser.getUserId());
								updateList.add(lfstatecode);
							}else {
								//当前数据与之前的数据重复了
								errorMessage.put(k-2, "状态码重复");//key表示行号,value:错误信息
							}
						}else {
							//超过上限
							overCount=true;
							break;
						}
					}
					if(reader!=null){
						//关闭流
						reader.close();
					}
					resultMsg = createLastResultMsg(request, resultMsg, lgcorpcode, errorMessage, countNumber, overCount,
							updateList);
				}
			}
			
		} catch (Exception e){
			EmpExecutionContext.error(e,"批量上传状态码出现异常！");
			// 判断是否因为上传了错误格式的xls文件
			if (e.getMessage().equals("Unable to recognize OLE stream")){
				request.setAttribute("result", "errorXls");
			}else{
				request.setAttribute("result", "false");
			}
		}
		return resultMsg;
	}
	
	
	private String createLastResultMsg(HttpServletRequest request, String resultMsg, String lgcorpcode,
			Map<Integer, String> errorMessage, int countNumber, boolean overCount, List<LfStateCode> updateList)
			throws Exception {
		//是否超过上限
		if(!overCount) {
			//没过上限，可以进行批量数据更新操作
			//修改数据库
			if (updateList.size() > 0) {
				Integer result = baseBiz.addList(LfStateCode.class, updateList);
				if (result > 0) {
					// 增加操作日志
					Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
					if (loginSysuserObj != null) {
						LfSysuser lfSysuser = (LfSysuser) loginSysuserObj;
						EmpExecutionContext.info("状态码管理", lfSysuser.getCorpCode(), lfSysuser.getUserId() + "",
								lfSysuser.getUserName(), "文件导入成功，成功数：" + updateList.size(), "OTHER");
					}
					Integer count = updateList.size();
					if(count==countNumber) {
						//全部成功
						resultMsg = "upload=" + count+"&fail=0";
					}else if(count<countNumber) {
						//部分失败,需生成文件提供用户下载查看错误的记录信息
						//Upload=1&fail=7&filePath=/D:/javatools/tomcat/apache-tomcat-6.0.35/webapps/emp_std/xtgl/statecode/file/\download\errormessage\StateCode_2018121217241755_1001.zip
						resultMsg = createErrorMessageFile(errorMessage, countNumber, count,request);
					}
				} else {
					resultMsg = "false";
					EmpExecutionContext.error("企业：" + lgcorpcode + "，状态码管理文件导入失败");
				}
			} else {
				if(countNumber>0) {
					//成功数为0;提交总数为countNumber
					resultMsg = createErrorMessageFile(errorMessage, countNumber, 0,request);
				}else {
					resultMsg = "noRecord";
					EmpExecutionContext.error("企业：" + lgcorpcode + "，没有记录导入");
				}
			}
		}else {
			resultMsg="overCount";//超过上限了
		}
		return resultMsg;
	}
	/**
	 * 状态码导入生成错误提示信息
	 * @param errorMessage  错误提示信息map
	 * @param countNumber   提交总数
	 * @param count         导入成功数
	 * @return
	 */
	private String createErrorMessageFile(Map<Integer, String> errorMessage, int countNumber, Integer count,HttpServletRequest request) {
		String resultMsg = null;
		OutputStreamWriter osw=null;
		FileOutputStream os = null;
		try {
			//模板路径
			String ROOT = new TxtFileUtil().getWebRoot();
			String BASEDIR =ROOT+ "xtgl/statecode/file/";
			// 产生下行报表路径
			String voucherPath = "download";
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// emp_std/xtgl/statecode/file/\download\errormessage\2018121217240539
			String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "errormessage" + File.separator + sdf.format(curDate);
			File fileTemp = new File(voucherFilePath);
			if (!fileTemp.exists()) {
			    fileTemp.mkdirs();
			}
			String fileName = "StateCode_" + sdf.format(curDate) + "_[errormessage]_" + StaticValue.getServerNumber() + ".txt";
			String filePath = voucherFilePath+ File.separator + fileName;
			os = new FileOutputStream(filePath);
			osw= new OutputStreamWriter(os);
			//遍历错误信息
			Set<Entry<Integer,String>> entrySet = errorMessage.entrySet();
			
			Iterator<Entry<Integer, String>> iterator = entrySet.iterator();
			while(iterator.hasNext()) {
				Entry<Integer, String> next = iterator.next();
				Integer key=next.getKey();
				String value=next.getValue();
					osw.write("第"+key+"行错误\t"+value+"\r\n");
			}
			osw.flush();
			osw.close();
			//ZipUtil.compress(voucherFilePath, filePath);
			//StateCodeExcelTool.deleteDir(fileTemp);
			
			//String temp=filePath.replace(BASEDIR+"\\", "xtgl/statecode/file/");
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("FILE_NAME", fileName);
		    resultMap.put("FILE_PATH", filePath);
		    HttpSession session = request.getSession(false);
		    session.setAttribute("errorMessageFile", resultMap);
			resultMsg = "upload=" + count+"&fail="+(countNumber-count)+"&filePath=errorMessageFile";
		} catch (FileNotFoundException e) {
			EmpExecutionContext.error(e, "导入状态码生成错误提示文件出现异常！");
		} catch (IOException e) {
			EmpExecutionContext.error(e, "导入状态码生成错误提示文件出现异常！");
		} catch (Exception e) {
			EmpExecutionContext.error(e, "导入状态码生成错误提示文件出现异常！");
		}finally {
			try {
				if(osw!=null) {
					osw.close();
				}
				if(os!=null) {
					os.close();
				}
			} catch (IOException e) {
				EmpExecutionContext.error(e, "导入状态码生成错误提示文件出现异常！");
			}
		}
		return resultMsg;
	}
	/**
	 * 解析并读取xls文件的数据
	 * @param request
	 * @param lgcorpcode
	 * @param sysuser
	 * @param fileItem
	 * @return
	 * @throws Exception
	 */
	private String parseStateCodeByXls(HttpServletRequest request, String resultMsg ,String lgcorpcode, LfSysuser sysuser,FileItem fileItem) throws Exception {
		List<LfStateCode> updateList = new ArrayList<LfStateCode>();
		LfStateCode lfstatecode = null;
		//有效行数
		int rows=0;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Workbook workBook = null;
		try {
			// 获得工作薄（Workbook）
			workBook = Workbook.getWorkbook(fileItem.getInputStream());

			// 获得工作薄（Workbook）中工作表（Sheet）
			Sheet sh = workBook.getSheet(0);
			rows = sh.getRows() - 2;
			//错误提示信息
			Map<Integer,String> errorMessage=new TreeMap<Integer,String>();
			//判断是否重复数据
			Set<String> repeatStateCode=new HashSet<String>();
			
			/*
			 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); Timestamp
			 * createTime = new Timestamp(sdf.parse(sdf.format(new Date())).getTime());
			 */
			//是否超过上限,默认为false,超过1000条数据时更新为true
			boolean overCount=false;
			//导入的数据量
			//int countNumber=0;
			int maxNum = 1000;//单次导入只允许更新1000条
			if (rows < 1) {	//没有记录		
				resultMsg="noRecord";
			} else if(rows>maxNum) {//有效数据超过1000条
				//超过上限
				resultMsg="overCount";
				//overCount=true;
			}else {
				//正常解析数据
				Cell[]  titles=sh.getRow(1);
				//判断是否使用正确的模板
				if(titles!=null&&titles.length==3) {
					for (int k = 2; k < sh.getRows(); k++) {
						Cell[] cells = sh.getRow(k);
						//错误信息可能有多种，可将每一行的错误信息使用StringBuffer保存起来
						StringBuffer errorBuffer=new StringBuffer();
						if (cells == null) {
							//当前行没有任何的数据
							//errorBuffer.append("状态码未填写").append("\t").append("映射码未填写");
							errorBuffer.append("状态码未填写");
							errorMessage.put(k+1, errorBuffer.toString());//保存当前行的错误信息
							//退出当前行的验证
							continue;
						}else {
							//当前行存在数据，进行数据的有效性验证
							int size = cells.length;
							String stateCode="";
							String mappingCode="";
							String stateDes="";
							if(size>0) {
								// 状态码，必须非空
								stateCode = cells[0].getContents().trim();
							}
							if(size>1) {
								// 映射码，必须非空
								mappingCode = cells[1].getContents().trim();
							}
							if(size>2) {
								// 状态码说明
								stateDes = cells[2].getContents().trim();
							}
							stateCode = stateCode == null ? "" : stateCode;
							mappingCode = mappingCode == null ? "" : mappingCode;
							stateDes = stateDes == null ? "" : stateDes;
							
	//						countNumber++;//文件数据量
	//						if(countNumber<maxNum) {//单次导入少于1000条
								
							// 验证状态码是否为空
							boolean isstateCodeNull = stateCode == null || "".equals(stateCode);
							if (isstateCodeNull) {
								//状态码为空
								errorBuffer.append("状态码未填写").append("\t");
							}
							// 判断状态码是否存在
							LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						    conditionMap.put("stateCode", stateCode);
							conditionMap.put("corpCode&in", "0,1,2," + lgcorpcode);
							List<LfStateCode> stateCodeList = baseBiz.getByCondition(LfStateCode.class, conditionMap, null);
							// 检查名称重复
							if (!"".equals(stateCode)&&stateCodeList != null && stateCodeList.size() > 0) {
								// 状态码存在不进行下一步的验证，开始验证下一条记录
								errorBuffer.append("状态码已经存在").append("\t");
							}
							
							if(!"".equals(stateCode)&&stateCode!=null) {
								//通过验证,可能存在重复的状态码
								int beforeSize=repeatStateCode.size();
								repeatStateCode.add(stateCode);
								int afterSize=repeatStateCode.size();
								if(afterSize==beforeSize) {
									//当前数据与之前的数据重复了
									errorBuffer.append("状态码重复").append("\t");
								}
							}
							
							//状态码超过7位
							if (stateCode.trim().length() > 7)
							{
								errorBuffer.append("状态码超过7位").append("\t");
							}
							//映射码验证
	//						if ("".equals(mappingCode) ) {
	//							errorBuffer.append("映射码未填写").append("\t");
	//						}
						
							if (!"".equals(mappingCode) && (mappingCode.length() > 7||!ValidateUtil.validate(mappingCode)))
							{
								errorBuffer.append("映射码只能输入字母或数字，且不超过7位").append("\t");
							}
							
							
							//状态码说明超过64位
							if (stateDes.trim().length() > 64)
							{
								errorBuffer.append("状态码说明超过64位").append("\t");
							}
							
							
							
							if(errorBuffer.length()>1) {
								//存在错误信息了，不进行下面操作
								errorMessage.put(k+1, errorBuffer.toString());
								continue;
							}
							// 通过验证
							lfstatecode = new LfStateCode();
							lfstatecode.setCorpCode(lgcorpcode);
							lfstatecode.setCreateTime(timestamp);
							lfstatecode.setMappingCode("".equals(mappingCode)?stateCode:mappingCode);//映射码未填则默认为状态码
							lfstatecode.setStateCode(stateCode);
							lfstatecode.setStateDes(StringUtils.defaultIfEmpty(stateDes," "));
							lfstatecode.setUpdateTime(timestamp);
							lfstatecode.setUserId(sysuser.getUserId());
							updateList.add(lfstatecode);
						}
					}
					resultMsg = createLastResultMsg(request, resultMsg, lgcorpcode, errorMessage, rows, overCount,updateList);
				}else {
					resultMsg="errorFile";
				}
			}
			
		} catch (BiffException e) {
			EmpExecutionContext.error(e, "处理xls文件出现异常！");
			resultMsg = "errorFile";
		} catch (IOException e) {
			resultMsg = "errorFile";
			EmpExecutionContext.error(e, "处理xls文件的输入输出异常！");
		}
		return resultMsg;
	}
	
	
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author pengj
	 * @datetime 2016-1-15 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志异常，session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
	
	
	 /**
     * 状态码的excel导出方法(导出全部)
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void ReportCurrPageExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String requestPath = request.getRequestURI();
//        String titlePath = requestPath.substring(requestPath.lastIndexOf("_") + 1, requestPath.lastIndexOf("."));
        String exportType = "1";
      //格式化时间
        SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
        long startTime = System.currentTimeMillis();
        
        List<LfStateCode> lfStateCodeList = null;
		LfStateCode lfStateCode = new LfStateCode();
		String stateCode = "";
		String mappingCode = "";
		String stateDes = "";
        
        try {
        	//状态码
        	stateCode = request.getParameter("stateCode");
        	if(stateCode != null && !"".equals(stateCode)){
        		lfStateCode.setStateCode(stateCode);
        	}
        	//映射码
        	mappingCode = request.getParameter("mappingCode");
        	if(mappingCode != null && !"".equals(mappingCode)){
        		lfStateCode.setMappingCode(mappingCode);
        	}
        	
        	//状态码说明
        	stateDes = request.getParameter("stateDes");
        	if(stateDes != null && !"".equals(stateDes)){
        		lfStateCode.setStateDes(stateDes);
        	}
            PageInfo pageInfo = new PageInfo();
            //分页信息
            pageSet(pageInfo, request);
            
            String lgcorpcode = request.getParameter("lgcorpcode");
			lfStateCode.setCorpCode(lgcorpcode);
			//只显示自定义状态码，查询出当前页的状态码信息
			lfStateCodeList = stateCodeBiz.getLfStateCode(lfStateCode, null);
			
			

            //返回状态
            String result = "false";
            //操作日志信息
            String opContent = "";
            if (lfStateCodeList != null && lfStateCodeList.size() > 0) {
                //生成excel文件
                StateCodeExcelTool et = new StateCodeExcelTool(excelPath);
//                Map<String, String> resultMap = et.createMtReportExcel(lfStateCodeList, exportType, request);
                Map<String, String> resultMap = et.createExcelByXls(lfStateCodeList, exportType, request);
                HttpSession session = request.getSession(false);
                session.setAttribute("smstaskrecord_export", resultMap);
                //操作日志信息
                opContent = "导出成功。";
                result = "true";
            } else {
                //操作日志信息
            	opContent = "导出失败。";
            }
            opContent += "开始：" + sdformat.format(startTime) + "，耗时：" + (System.currentTimeMillis() - startTime) / 1000 + "秒，总数：" + (lfStateCodeList!=null?lfStateCodeList.size():0);
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            if(lfSysuser!=null){
            	EmpExecutionContext.info("状态码数据导出", lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
            }else{
            	EmpExecutionContext.info("状态码数据导出", "", "", "", opContent, "GET");
            }
            response.getWriter().print(result);
        } catch (Exception e) {
            //异常打印
            EmpExecutionContext.error(e, "状态码数据的excel导出异常！");
            response.getWriter().print("false");
        }
    }

    
    
    /**
     * 状态码 EXCEL文件下载
     *
     * @param request
     * @param response
     * @throws Exception
     * @description
     * @author zhouxiangxian 
     * @datetime 2018-12-2 下午12:26:22
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //文件名
        String fileName = "";
        //文件路径
        String filePath = "";
        //保存session的参数
        String exportType = "";
        try {
            HttpSession session = request.getSession(false);
            exportType = request.getParameter("exporttype");
            Object obj = session.getAttribute(exportType);
            if (obj != null) {
                Map<String, String> resultMap = (Map<String, String>) obj;
                fileName = (String) resultMap.get("FILE_NAME");
                filePath = (String) resultMap.get("FILE_PATH");
                //弹出下载页面
                new DownloadFile().downFile(request, response, filePath, fileName);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "群发EXCEL文件下载失败，fileName:" + fileName + "，filePath:" + filePath + "，exportType:" + exportType);
        }
    }
	
    
    
    /**
     * 状态码支持错误文件下载
     * @param request
     * @param response
     * @throws Exception
     */
    public void downloadErrorMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //文件名
        String fileName = "";
        //文件路径
        String filePath = "";
        //保存session的参数
        String errorMessageFile = "";
        try {
            HttpSession session = request.getSession(false);
            errorMessageFile = request.getParameter("filepath");
            Object obj = session.getAttribute(errorMessageFile);
            if (obj != null) {
                Map<String, String> resultMap = (Map<String, String>) obj;
                fileName = (String) resultMap.get("FILE_NAME");
                filePath = (String) resultMap.get("FILE_PATH");
                //弹出下载页面
               downFile(request, response, filePath, fileName);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "状态码导入错误的提示信息文件下载失败，fileName:" + fileName + "，filePath:" + filePath + "，filepath:" + errorMessageFile);
        }
    }
    /**
     * 下载文件
     * @param request
     * @param response
     * @param filePath
     * @param fileName
     */
    private void downFile(HttpServletRequest request,
			HttpServletResponse response, String filePath, String fileName) {
		
		OutputStream os = null;
		FileInputStream fis = null;

		try {
			if (response != null) {

				response.reset();
				String utf8title = URLEncoder.encode(fileName, "UTF-8");
				response.setHeader("Content-Disposition", "attachment; filename=" + utf8title);// 设定输出文件头
				os = response.getOutputStream();
				fis = new FileInputStream(filePath);
				byte[] b = new byte[1024];
				int i = 0;
				while ((i = fis.read(b)) > 0) {
					os.write(b, 0, i);
				}
				os.flush();
			}

		} catch (UnsupportedEncodingException e) {

			EmpExecutionContext.error(e, "字符集编码错误！");

		} catch (IOException e) {

			EmpExecutionContext.error(e, "文件流输出异常！");

		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e) {
					EmpExecutionContext.error(e, "关闭文件输入流异常！");
				}
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
					EmpExecutionContext.error(e, "关闭文件输出流异常！");
				}
		}
	}
}
