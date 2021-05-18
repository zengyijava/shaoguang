package com.montnets.emp.servmodule.txgl.blacklist.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.blacklist.PbListBlack;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.servmodule.txgl.blacklist.biz.BlackListBiz;
import com.montnets.emp.util.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@SuppressWarnings("serial")
public class bla_mmsblacklistSvt extends BaseServlet {

	//操作模块
	final String opModule=StaticValue.PARAM_PRESERVE;

	//操作用户
	final String opSper = StaticValue.OPSPER;
	
	private final String empRoot="txgl";
	
	private final String basePath="/blacklist";
	
	final String excelPath= "file/excel/";
	
	final BlackListAtom blackBiz = new BlackListAtom();
	final WgMsgConfigBiz wb = new WgMsgConfigBiz();
	final BaseBiz baseBiz=new BaseBiz();
	final SuperOpLog spLog=new SuperOpLog();
	
	/**
	 * 查询彩信黑名单的方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long stime = System.currentTimeMillis();
		//黑名单状态
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//黑名单状态
		//获取当前登录用户的企业编码
		//String corpCode = request.getParameter("lgcorpcode");  	
		String corpCode = "";  		
		List<PbListBlack> mmsblackList = null;
		PageInfo pageInfo=new PageInfo();
		try {
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpCode = lfSysuser.getCorpCode();
			//是否第一次打开
			boolean isFirstEnter = false;
			//分页信息
			isFirstEnter=pageSet(pageInfo, request);
			//非第一次访问，则组装查询条件
			if(!isFirstEnter){
				//手机号码查询 (由于网关的phone是bigint类型所以现在改为不支持模糊查询)
				conditionMap.put("phone", request.getParameter("phone"));
			}			
			conditionMap.put("optype", "1");
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("blType", "2");
//			mmsblackList = baseBiz.getByCondition(PbListBlack.class, null, conditionMap, null, pageInfo);	
			mmsblackList = baseBiz.getByConditionNoCount(PbListBlack.class, null, conditionMap, null, pageInfo);
			//加密后的集合
			Map<Long, String> keyIdMap = new HashMap<Long, String>();
			//ID加密
			if(mmsblackList != null && mmsblackList.size() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMap(mmsblackList, "Id", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询彩信黑名单列表，参数加密失败。corpCode:"+corpCode);
						throw new Exception("查询彩信黑名单列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询彩信黑名单列表，从session中获取加密对象为空！");
					mmsblackList.clear();
					throw new Exception("查询彩信黑名单列表，获取加密对象失败。");
				}
			}
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("mmsblackList", mmsblackList);
			request.setAttribute("keyIdMap", keyIdMap);
			//跳转到页面
			request.getRequestDispatcher(empRoot + basePath +"/bla_mmsblacklist.jsp")
			.forward(request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"彩信黑名单","["+sDate+"]查询("+(mmsblackList==null?0:mmsblackList.size())+"/"+pageInfo.getTotalRec()+"),耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.GET);
		} catch (Exception e) {
			//异常处理逻辑
			EmpExecutionContext.error(e,"彩信黑名单查询异常");
			request.setAttribute("error", e);
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			try {
				request.getRequestDispatcher(empRoot + basePath +"/bla_mmsblacklist.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"彩信黑名单servlet查询异常");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"彩信黑名单servlet查询跳转异常");
			}
		}
	}
	
	
	/**
	 * 导出EXCEL格式的信息
	 * @param request
	 * @param response
	 */
	public void exportExcel(HttpServletRequest request, HttpServletResponse response)throws Exception{
		long stime = System.currentTimeMillis();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String sDate = sdf.format(stime);
		//获取当前登录用户的企业编码
		String corpCode = request.getParameter("lgcorpcode");
		PageInfo pageInfo = new PageInfo();
		// 获取导出文件路径
		String context = request.getSession(false).getServletContext().getRealPath(excelPath);
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//手机号码查询(由于网关的phone是bigint类型所以现在改为不支持模糊查询)
		conditionMap.put("phone", request.getParameter("phone"));
		Map<String,String> busMap = null;
		conditionMap.put("optype", "1");
		conditionMap.put("corpCode", corpCode);
		String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);
		conditionMap.put("blType", "2");
		Map<String, String> resultMap=new BlackListBiz().createMMSBlackExcel(conditionMap,context,busMap,pageInfo,langName);
		PrintWriter out = response.getWriter();
		// 获取导出内容
		if(resultMap != null && resultMap.size() > 0)
		{
            HttpSession session = request.getSession(false);
            session.setAttribute("mmblack_excel_export",resultMap);
            out.print("true");
			setLog(request,"彩信黑名单","["+sDate+"]excel导出("+resultMap.get("LIST_SIZE")+"),耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.OTHER);
		}else{
			out.print("false");
			setLog(request,"彩信黑名单","["+sDate+"]excel导出失败,耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.OTHER);
		}
		
	}
	/**
	 * 下载彩信黑名单(excel)导出文件
	 * @param request
	 * @param response
	 */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response)   {
        try
		{
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute("mmblack_excel_export");
			if(obj != null){
			    // 弹出下载页面。
			    DownloadFile dfs = new DownloadFile();
			    Map<String, String> resultMap = (Map<String, String>) obj;
			    String fileName = (String) resultMap.get("FILE_NAME");
			    String filePath = (String) resultMap.get("FILE_PATH");
			    dfs.downFile(request, response, filePath, fileName);
			}
			session.removeAttribute("mmblack_excel_export");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下载彩信黑名单(excel)导出文件异常！");
		}
    }
	
	/**
	 * 导出TXT格式的信息
	 * @param request
	 * @param response
	 */
	public void exportTxt(HttpServletRequest request, HttpServletResponse response)throws Exception{
		long stime = System.currentTimeMillis();
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String sDate = df.format(stime);
		//获取当前登录用户的企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 获取导出文件路径
		String context = request.getSession(false).getServletContext().getRealPath(excelPath);
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);
		/*彩信黑名单*/
		String mmsBlacklist = "zh_HK".equals(langName)?"mmsBlacklist_":"zh_TW".equals(langName)?"彩信黑名單":"彩信黑名单";
		//手机号码查询(由于网关的phone是bigint类型所以现在改为不支持模糊查询)
		conditionMap.put("phone", request.getParameter("phone"));
		conditionMap.put("optype", "1");
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("blType", "2");
		//防止导出较少数据时候，重复导出数据
		//Thread.sleep(200);
		PageInfo pageInfo = new PageInfo();
		// 设置每个excel文件的行数
		pageInfo.setPageSize(50000);
		
		List<PbListBlack> mmsblackList = baseBiz.getByCondition(PbListBlack.class, null, conditionMap, null,pageInfo);
		// 计算出文件数
		int fileCount = pageInfo.getTotalPage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		Date curDate = new Date();
		String file_path = context + File.separator + "mms"+ File.separator+"mms_"+ sdf.format(curDate)+".txt";
		boolean have=false;
		

		if(mmsblackList!=null){
			StringBuffer sb=new StringBuffer();
			int sum=0;
			for (int f = 0; f < fileCount; f++) {
				if (f > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
				{
					mmsblackList = baseBiz.getByCondition(PbListBlack.class, null, conditionMap, null,pageInfo);
				}
				pageInfo.setPageIndex(f + 2);// 定位下一页
				
			for(int k=0;k<mmsblackList.size();k++){
				PbListBlack pb=mmsblackList.get(k);	
				sum=sum+1;
				//防止数据出现百万情况下，内存溢出的情况
					sb.append(pb.getPhone()+"\r\n");
	                if(sum%500000==0){
	                    //避免数据量大比如百万左右数据库时候，出现内存溢出
	    				have=writeToTxtFile(file_path, sb.toString());
	                	 sb.setLength(0);
	                }
			}
			}
			if(sb.length()>0){
				have=writeToTxtFile(file_path, sb.toString());
				 sb.setLength(0);
			}
		}
		
		

		PrintWriter out = response.getWriter();

		if(have){

			String name = mmsBlacklist+ sdf.format(curDate)+".txt";
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("FILE_NAME", name);
			resultMap.put("FILE_PATH", file_path);
            HttpSession session = request.getSession(false);
            session.setAttribute("mmblack_txt_export",resultMap);
            out.print("true");
			setLog(request,"彩信黑名单","["+sDate+"]txt导出("+pageInfo.getTotalRec()+"),耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.OTHER);
		}else{
			out.print("false");
			setLog(request,"彩信黑名单","["+sDate+"]txt导出失败,耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.OTHER);
		}
		
	}
	
	
	/**
	 * 下载彩信黑名单(txt)导出文件
	 * @param request
	 * @param response
	 */
    public void downloadTxtFile(HttpServletRequest request, HttpServletResponse response)   {
        try
		{
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute("mmblack_txt_export");
			if(obj != null){
			    // 弹出下载页面。
			    DownloadFile dfs = new DownloadFile();
			    Map<String, String> resultMap = (Map<String, String>) obj;
			    String fileName = (String) resultMap.get("FILE_NAME");
			    String filePath = (String) resultMap.get("FILE_PATH");
			    dfs.downFile(request, response, filePath, fileName);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下载彩信黑名单(txt)导出文件异常！");
		}
    }
	
	/**
	 * 写成txt文件
	 * @param fileName
	 * @param content
	 * @return
	 * @throws EMPException
	 */
	  public boolean writeToTxtFile(String fileName, String content)
	    throws EMPException
	  {
	    FileOutputStream foss = null;
	    try
	    {
	    	//如果文件夹不存在就创建
	      File filee = new File(fileName);
	      if (!filee.getParentFile().exists()) {
	    	  filee.getParentFile().mkdir();
			}
	      
	      //如果文件不存在就创建文件
	      if (!filee.exists())
	      {
              boolean flag = filee.createNewFile();
              if (!flag) {
                  EmpExecutionContext.error("创建文件失败！");
              }
	      }
//	      else{
//	    	  filee.delete();
//	    	  filee.createNewFile();
//	      }

	      foss = new FileOutputStream(fileName, true);

	      ByteArrayInputStream baInput = new ByteArrayInputStream(content
	        .getBytes("GBK"));

	      byte[] buffer = new byte[1024];
	      int ch = 0;
	      while ((ch = baInput.read(buffer)) != -1)
	      {
	        foss.write(buffer, 0, ch);
	      }
	      foss.flush(); // 全部写入缓存中的内容
	      baInput.close();
	    }
	    catch (Exception e) {
	      EmpExecutionContext.error(e, "将字符串内容写入文件异常！");
	      throw new EMPException("B10002", e);
	    }
	    finally {
	      if (foss != null)
	      {
	        try
	        {
	          foss.close();
	        } catch (IOException e) {
	          EmpExecutionContext.error(e, "关闭流异常！");
//	          throw new EMPException("B10003", e);
	        }
	      }
	    }
	    return true;
	  }
	
	
	/**
	 * 新增黑名单前的号码检测
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void checkMobile(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String phone = request.getParameter("mobile");
		String corpCode = request.getParameter("lgcorpcode");
		 
		String result = "checkBl:";
		try
		{
			//获取运营商号码段
			String[] haoduan=wb.getHaoduan();
			if(wb.checkMobile(phone,haoduan) != 1)
			{
				result=result+"errorPhone";
			}
			else
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				//组装过滤条件
				conditionMap.put("corpCode", corpCode);
				conditionMap.put("phone", phone);
				conditionMap.put("optype", "1");
				conditionMap.put("blType", "2");
				
				List<PbListBlack> blList = baseBiz.getByCondition(PbListBlack.class, conditionMap, null);
				if(blList != null && blList.size() > 0)
				{
					result += "phoneRepeat";
				}
			}
		} catch (Exception e)
		{
			result = "error";
			EmpExecutionContext.error(e,"判断彩信黑名单是否重复异常！");
		}finally
		{
			response.getWriter().print(result);
		}
	}
	/**
	 * 新增，导入，彩信黑名单
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String Type = request.getParameter("opType");
		//操作类型
	    String opType = null;
	    //操作内容
		String opContent =null;
		opType=StaticValue.ADD;
		opContent="新增彩信黑名单";
		//返回结果
		String result = null;
		//企业编码
        String corpCode = request.getParameter("lgcorpcode"); 
        String username = request.getParameter("lgusername");
        
		try
		{
			//当前系统的所有号段
			String[] haoduan = wb.getHaoduan();

			List<String> phones = new LinkedList<String>();
			String mobile = request.getParameter("mobile");
			HashSet<String> resultList = null;
            int phoneCount=0;
            
			//单个新增
			if (Type != null && Type.equals("add"))
			{
				//单个黑名单添加
				if(mobile != null && !"".equals(mobile))
				{
					phones.clear();
					phones.add(mobile.trim());
				}
			}
			else
			{
		        //获取一个存放临时文件的目录
	        	String uploadPath = StaticValue.FILEDIRNAME;				
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(100 * 1024);
				String temp = new TxtFileUtil().getWebRoot()+uploadPath;
				factory.setRepository(new File(temp));
				ServletFileUpload upload = new ServletFileUpload(factory);
				
				List<FileItem> fileList = null;
				fileList = upload.parseRequest(request);
				Iterator<FileItem> it = fileList.iterator();
				while (it.hasNext())
				{
					FileItem fileItem = (FileItem) it.next();
                    if(fileItem.getFieldName().equals("lgcorpcode"))
					{
						//当前登录用户的企业编码
						corpCode = null == fileItem.getString("UTF-8")?"":fileItem.getString("UTF-8").toString();
					}
					else if(fileItem.getFieldName().equals("lgusername"))
					{
						//当前登录用户的用户名
						username = null == fileItem.getString("UTF-8")?"":fileItem.getString("UTF-8").toString();
					}
					else if (!fileItem.isFormField()
							&& fileItem.getName().length() > 0)
					{
						InputStream instream = fileItem.getInputStream();
						// 文件名
						String fileCurName = fileItem.getName();
						// 通过文件名截取到文件类型
						String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();
						if(fileType.equals(".xls") || fileType.equals(".et"))
						{
							phones=jxExcel(fileItem.getInputStream(),corpCode);
						}
						// 解析excel2007文件
						else if(fileType.equals(".xlsx"))
						{
//							phones=jxExcel2007(fileItem.getInputStream(),corpCode);
							BufferedReader reader=new Excel2007Reader().fileParset(temp, fileItem.getInputStream());
							String charset =  new ChangeCharset().get_charset(instream);
							if(charset.startsWith("UTF-"))
							{
								reader.read(new char[1]);
							}
							String tmp;

							//根据企业编码获取当前企业下的所有彩信黑名单数据
							resultList = getlfmmsBlackMapByCorpCode(corpCode);

							while ((tmp = reader.readLine()) != null)
							{
								tmp = tmp.trim();
								//验证手机号的合法性
								if(wb.checkMobile(tmp,haoduan) == 1 && (tmp !=""))
								{
									if(checkRepeat(resultList, tmp))
									{
										phones.add(tmp);
										phoneCount++;
									}
								}
								//如果上传文件内号码号码数超过20万
								if(phoneCount>200000)
								{
									break;
								}
							}
							//关闭流
							reader.close();
						}
					// 解析TXT文本
					else{
						/*String charset =  new ChangeCharset().get_charset(instream);
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(fileItem.getInputStream(),charset));
						if(charset.startsWith("UTF-"))
						{
							reader.read(new char[1]);
						}*/
						BufferedReader reader = new ChangeCharset().getReader(instream, fileItem.getInputStream());
						String tmp;

						//根据企业编码获取当前企业下的所有彩信黑名单数据
						resultList = getlfmmsBlackMapByCorpCode(corpCode);

						while ((tmp = reader.readLine()) != null)
						{
							tmp = tmp.trim();
							//验证手机号的合法性
							if(wb.checkMobile(tmp,haoduan) == 1 && (tmp !=""))
							{
								if(checkRepeat(resultList, tmp))
								{
									phones.add(tmp);
									phoneCount++;
								}
							}
							//如果上传文件内号码号码数超过20万
							if(phoneCount>200000)
							{
								break;
							}
						}
						//关闭流
						reader.close();
					}
					}
				}
			}
		

			//没有号码
			if(phones.size()==0)
			{
				result = "noPhone";
			}else if(phones.size()>200000)
			{
				result = "overCount";
			}else	
			{
				boolean rest=new BlackListBiz().saveMSSBlack(phones,corpCode);
				
				if(rest)
				{
					result = "true"+String.valueOf(phones.size());
					//将添加成功的黑名单实时的添加到黑名单map里
					blackBiz.addmmsBlackListByList(phones,corpCode);
					//保存日志
					spLog.logSuccessString(username, opModule, opType, opContent,corpCode);
					EmpExecutionContext.info("企业："+corpCode+"，操作员："+username+"，新增黑名单成功，新增黑名单数："+phones.size());
				}else
				{
					result = "false";
					spLog.logFailureString(username, opModule, opType, opContent+opSper,null,corpCode);
					EmpExecutionContext.info("企业："+corpCode+"，操作员："+username+"，新增黑名单失败，新增黑名单数："+phones.size());
				}
			}
		} catch (Exception e)
		{
			result = "error";
			spLog.logFailureString(username, opModule, opType, opContent+opSper, e,corpCode);
			EmpExecutionContext.error(e,"企业："+corpCode+"，操作员："+username+"，新增黑名单异常！");
		}finally
		{
			//将信息放回前台
			if (Type != null && Type.equals("add"))
			{
				response.getWriter().print(result);
			}
			else
			{
				//跳转地址处理
				String s = request.getRequestURI();
				s = s+"?method=find&lgcorpcode="+corpCode;
				//跳转
				try {
					request.getSession(false).setAttribute("uploadresult", result);
					response.sendRedirect(s);
				} catch (IOException e) {
					EmpExecutionContext.error(e,"新增彩信黑名单跳转异常！");
				}
			}
		}
	}
	

	/**
	 * 旧版本的EXCEL
	 * @param ins
	 * @param busCode
	 * @param corpCode
	 * @return
	 */
	public List<String>  jxExcel(InputStream ins,String corpCode){
		List<String> phones=new ArrayList<String>();	 
		try
		{

			String phoneNum="";
			HSSFWorkbook workbook = new HSSFWorkbook(ins);
			
			//根据业务类型获取当前系统的黑名单
			HashSet<String> resultList = getlfmmsBlackMapByCorpCode(corpCode);
            String[] haoduan=new WgMsgConfigBiz().getHaoduan();
			//循环每张表
			for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
				 HSSFSheet sheet = workbook.getSheetAt(sheetNum);
				// 循环每一行
				 for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++)
				 {
						HSSFRow row = sheet.getRow(rowNum);
						if (row == null) {
							continue;
						}
						//得到第一列的电话号码
	                    phoneNum = getCellFormatValue(row.getCell(0));
	                    if(phoneNum != null)
	                    {
	                    	phoneNum = phoneNum.trim();
	                    }
						//验证手机号的合法性
						if(wb.checkMobile(phoneNum,haoduan) == 1)
						{
							if(checkRepeat(resultList, phoneNum))
							{
								phones.add(phoneNum);
								
							}
						}
				}
			}
			
		}catch (Exception e) {
			EmpExecutionContext.error(e,"解析EXCEL异常！");
		}

		return phones;
	}
	
	/**
	 * 遍历excel文件所有sheet
	 * 
	 * @description
	 * @param filename
	 * @throws Exception
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-6 下午03:15:33
	 */
	public List<String> jxExcel2007(InputStream filename,String corpCode) throws Exception
	{
		XSSFWorkbook xwb = new XSSFWorkbook(filename);

		// 读取第一章表格内容

		XSSFSheet sheet = xwb.getSheetAt(0);

		// 定义 row、cell

		XSSFRow row;
		List<String> phones=new ArrayList<String>();
		// 循环输出表格中的内容
		//根据业务类型获取当前系统的黑名单
		HashSet<String> resultList = getlfmmsBlackMapByCorpCode(corpCode);
        String[] haoduan=new WgMsgConfigBiz().getHaoduan();
		for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {

			row = sheet.getRow(i);

			for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
	
				// 通过 row.getCell(j).toString() 获取单元格内容，
		
				String phoneNum = getCellFormatValue(row.getCell(j));
				//验证手机号的合法性
				if(wb.checkMobile(phoneNum,haoduan) == 1)
				{
					if(checkRepeat(resultList, phoneNum))
					{
						phones.add(phoneNum);
						
					}
				}
	
			}

		}
		return phones;
		     
	}
	/**
	 * 2007版本的EXCEL解析
	 * @param cell
	 * @return
	 */

	private static String getCellFormatValue(XSSFCell cell)
    {
        String cellvalue = "";
        if (cell != null) 
         {
            // 判断当前Cell的Type
            switch (cell.getCellType()) 
            {
               // 如果当前Cell的Type为NUMERIC
               case XSSFCell.CELL_TYPE_NUMERIC: 
               case XSSFCell.CELL_TYPE_FORMULA: 
               {
                  // 判断当前的cell是否为Date
                  if (DateUtil.isCellDateFormatted(cell)) 
                  {
                     // 如果是Date类型则，取得该Cell的Date值
                     Date date = cell.getDateCellValue();
                     // 把Date转换成本地格式的字符串
                     Calendar c = Calendar.getInstance();
                     c.setTime(date);	                     
                     if(c.get(Calendar.HOUR)==0 && c.get(Calendar.MINUTE)==0 && c.get(Calendar.SECOND) ==0){
                     	cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                      }else {
                     	 cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
 					 }
                  }
                  // 如果是纯数字
                  else
                  {
                     // 取得当前Cell的数值
                	  // 是否有小数部分（分开处理）
                	  if(Math.floor(cell.getNumericCellValue())==cell.getNumericCellValue())
                	  {
                		  cellvalue=String.valueOf((long)cell.getNumericCellValue());
                	  }else {
                		  cellvalue = cell.getRawValue();
					  }
                   
                  }
                  break;
               }
               // 如果当前Cell的Type为STRIN
               case XSSFCell.CELL_TYPE_STRING:
                  // 取得当前的Cell字符串
                  cellvalue = cell.getStringCellValue();
                  break;
               // 默认的Cell值
               default:
                  cellvalue = " ";
            }
         }
         else 
         {
            cellvalue = "";
         }
        return cellvalue;
    }
	
	public  String getCellFormatValue(HSSFCell cell)
    {
		String cellvalue = "";
        if (cell != null) 
         {
            // 判断当前Cell的Type
            switch (cell.getCellType()) 
            {
	            case HSSFCell.CELL_TYPE_NUMERIC: // 数字   
	                // 判断当前的cell是否为Date
	                  if (DateUtil.isCellDateFormatted(cell)) 
	                  {
	                     // 如果是Date类型则，取得该Cell的Date值
	                     Date date = cell.getDateCellValue();
	                     // 把Date转换成本地格式的字符串
	                     Calendar c = Calendar.getInstance();
	                     c.setTime(date);	                     
	                     if(c.get(Calendar.HOUR)==0 && c.get(Calendar.MINUTE)==0 && c.get(Calendar.SECOND) ==0){
	                    	cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
	                     }else {
	                    	 cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
						 }
	                  }
	                  // 如果是纯数字
	                  else
	                  {
	                     // 是否有小数部分（分开处理）
	                	  if(Math.floor(cell.getNumericCellValue())==cell.getNumericCellValue())
	                	  {
	                		  cellvalue=String.valueOf((long)cell.getNumericCellValue());
	                	  }else {
	                		  cellvalue = String.valueOf(cell.getNumericCellValue());
						  }
	                	  //System.out.println(cellvalue);
	                  }
	                break;   
	            case HSSFCell.CELL_TYPE_STRING: // 字符串   
	                cellvalue = cell.getStringCellValue() ; 	                       
	                break;    
	            case HSSFCell.CELL_TYPE_FORMULA: // 公式   
	                cellvalue = cell.getCellFormula();   
	                break;   
	            case HSSFCell.CELL_TYPE_BLANK: // 空值   
	            	cellvalue = " "; 
	                break;   
	            case HSSFCell.CELL_TYPE_ERROR: // 故障   
	            	cellvalue = " ";
	                break;   
	            default:   
	            	cellvalue = " ";
	                break;  
            }
         }
         else 
         {
            cellvalue = "";
         }
        return cellvalue;
    }
	
	
	/**
	 * 删除彩信黑名单的方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//要删除的黑名单的id的拼接字符串
		String ids=request.getParameter("ids");
		//操作类型
	    String opType = null;
	    //操作内容
		String opContent =null;
		opType=StaticValue.DELETE;
		//String corpCode=request.getParameter("lgcorpcode");
		String corpCode="";
		String username = request.getParameter("lgusername");
		opContent="删除彩信黑名单";
		
		BlackListBiz blBiz = new BlackListBiz();
		//删除总数
		int delCount = 0;
		Integer result = 0;
		try
		{
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpCode = lfSysuser.getCorpCode();
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt == null)
			{
				EmpExecutionContext.error("删除彩短黑名单，从session中获取加密对象为空！");
				throw new Exception("删除彩信黑名单信息，获取加密对象失败。");
			}
			//批量删除
			if(ids.indexOf(",") >= 0)
			{
				//拆分出来进行单个解密再拼接查询条件
				String[]kwIdClump = ids.split(",");
				StringBuffer wdSb = new StringBuffer();
				for(int i=0; i<kwIdClump.length; i++)
				{
					wdSb.append(encryptOrDecrypt.decrypt(kwIdClump[i])).append(",");
				}
				wdSb.deleteCharAt(wdSb.lastIndexOf(","));
				ids = wdSb.toString();
			}
			//单个删除
			else
			{
				//解密
				ids = encryptOrDecrypt.decrypt(ids);
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("id&in", ids);
			conditionMap.put("corpCode", corpCode);
			List<PbListBlack> blList = baseBiz.getByCondition(
					PbListBlack.class, conditionMap, null);
			delCount = blList.size();
			if (blList != null && blList.size() > 0) {
				result = blBiz.delBlacks(blList);
				if(result >0){
					for (PbListBlack pbListBlack : blList) {
						blackBiz.delmmsBlackList(pbListBlack.getPhone(), corpCode);
					}
				}
			}
			
			EmpExecutionContext.info("企业："+corpCode+"，操作员："+username+"，删除黑名单成功，共删除黑名单数："+delCount);
			
			response.getWriter().print(result);
			spLog.logSuccessString(username, opModule, opType, opContent,corpCode);
		} catch (Exception e)
		{
			response.getWriter().print(false);
			spLog.logFailureString(username, opModule, opType, opContent+opSper, e,corpCode);
			EmpExecutionContext.error(e,"企业："+corpCode+"，操作员："+username+"，删除黑名单异常！");
		}
	}

	/**
	 * @description  过滤重复号码  
	 * @param set 合法号码集合
	 * @param str 手机号码
	 * @return boolean-没有重复，false-是重复号码      			 
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-11-4 上午10:40:00
	 */
	private boolean checkRepeat(HashSet<String> set,String str)
	{
		if(set.contains(str)){
			return false;
		}else{
			set.add(str);
		}
		return true;
	}
	/**
	 * 根据企业编码获取此企业下面的所有彩信黑名单
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public HashSet<String> getlfmmsBlackMapByCorpCode(String corpCode) throws Exception{
		HashSet<String> resultList = new HashSet<String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
            //组装过滤条件  			
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("optype", "1");
			conditionMap.put("blType", "2");
			
			//查询方法
			List<PbListBlack> lfmmsBlacksList = baseBiz.getByCondition(PbListBlack.class, conditionMap, null);
			if(lfmmsBlacksList == null || lfmmsBlacksList.size() == 0){
				return resultList;
			}
			//循环查询结果
			PbListBlack blackList = null;
			for(int i = 0; i < lfmmsBlacksList.size(); i++){
			    blackList = lfmmsBlacksList.get(i);
				String userphone = blackList.getPhone().toString();
				resultList.add(userphone);
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取彩信黑名单发生异常！");
			throw e;
		}
		//返回结果list
		return resultList;
	}

	private void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,opModule+opType+opContent+"日志写入异常");
		}
	}
	
	/**
	 * 获取加密对象
	 * @description    
	 * @param request
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午07:29:28
	 */
	public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request)
	{
		try
		{
			ParamsEncryptOrDecrypt encryptOrDecrypt = null;
			//加密对象
			Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("彩信黑名单设置，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "彩信黑名单设置，从session获取加密对象异常。");
			return null;
		}
	}
}
