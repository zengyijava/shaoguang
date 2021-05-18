package com.montnets.emp.servmodule.txgl.blacklist.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.blacklist.PbListBlack;
import com.montnets.emp.entity.blacklist.PbListBlackDelrecord;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.servmodule.txgl.blacklist.biz.BlackListBiz;
import com.montnets.emp.util.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 黑名单处理方法类
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description 
 */
@SuppressWarnings("serial")
public class bla_blacklistSvt extends BaseServlet {

	//操作模块
	final String opModule=StaticValue.PARAM_PRESERVE;
	//操作用户
	final String opSper = StaticValue.OPSPER;
	final BlackListAtom blackBiz = new BlackListAtom();
	final BlackListBiz blBiz = new BlackListBiz();
	final WgMsgConfigBiz wb = new WgMsgConfigBiz();
    private final PhoneUtil phoneUtil		= new PhoneUtil();
    final List<XtGateQueue> mrXtList = null;
	
    final String empRoot="txgl";
	
    final String basePath="/blacklist";
    final String excelPath= "file/excel/";
	
    final BaseBiz baseBiz=new BaseBiz();
	
    final SuperOpLog spLog=new SuperOpLog();
	
	/**
	 * 查询黑名单的方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long stime = System.currentTimeMillis();
		PageInfo pageInfo=new PageInfo();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap=new LinkedHashMap<String, String>();//排序规则
		try {
			//业务类型
			String busCode = request.getParameter("busCode");
			//黑名单状态
			//String blState = "1";
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//获取当前登录用户的企业编码
			//String corpCode = request.getParameter("lgcorpcode");
			String corpCode = lfSysuser.getCorpCode();
			Map<String,String> busMap = null;
			//是否第一次打开
			boolean isFirstEnter = false;
			//分页信息
			isFirstEnter=pageSet(pageInfo, request);

			
			//获取所有业务类型 
			busMap = getBusMap(corpCode,request);
			request.setAttribute("busMap", busMap);
			//非第一次访问，则组装查询条件
			if(!isFirstEnter){
				//手机号码查询(由于网关的phone是bigint类型所以现在改为不支持模糊查询)
                String phone = blBiz.formatPhone(request.getParameter("phone"));
                if(phone !=null){
				    conditionMap.put("phone",phone);
                }
				if(null != busCode && 0 != busCode.length())
				{
					busCode = "@".equals(busCode)?" ":busCode;
					conditionMap.put("svrType", busCode);
				}
			}			
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("optype", "1");
			conditionMap.put("blType", "1");
			orderbyMap.put("optTime","DESC" );
			List<PbListBlack> pbListBlacks = baseBiz.getByCondition(PbListBlack.class,null, conditionMap, orderbyMap, pageInfo);
//			List<PbListBlack> pbListBlacks = baseBiz.getByConditionNoCount(PbListBlack.class,null, conditionMap, orderbyMap, pageInfo);
			//加密后的集合
			Map<Long, String> keyIdMap = new HashMap<Long, String>();
			//ID加密
			if(pbListBlacks != null && pbListBlacks.size() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMap(pbListBlacks, "Id", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询短信黑名单列表，参数加密失败。corpCode:"+corpCode);
						throw new Exception("查询短信黑名单列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询短信黑名单列表，从session中获取加密对象为空！");
					pbListBlacks.clear();
					throw new Exception("查询短信黑名单列表，获取加密对象失败。");
				}
			}
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("blackList", pbListBlacks);
			request.setAttribute("keyIdMap", keyIdMap);
			//跳转到页面
			request.getRequestDispatcher(empRoot + basePath +"/bla_blacklist.jsp")
			.forward(request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"短信黑名单","["+sDate+"]查询("+(pbListBlacks==null?0:pbListBlacks.size())+"/"+pageInfo.getTotalRec()+"),耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.GET);
		} catch (Exception e) {
			//异常处理逻辑
			EmpExecutionContext.error(e,"查询黑名单异常！");
			request.setAttribute("error", e);
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			try {
				request.getRequestDispatcher(empRoot + basePath +"/bla_blacklist.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"短信黑名单servlet查询异常");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"短信黑名单servlet查询跳转异常");
			}
		}
	}

	
	/**
	 * 查询黑名单删除记录的方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findAllDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long stime = System.currentTimeMillis();
		PageInfo pageInfo=new PageInfo();
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap=new LinkedHashMap<String, String>();//排序规则
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//获取当前登录用户的企业编码
			//String corpCode = request.getParameter("lgcorpcode");
			String corpCode = lfSysuser.getCorpCode();
			Map<String,String> busMap = null;
			//是否第一次打开
			boolean isFirstEnter = false;
			//分页信息
			isFirstEnter=pageSet(pageInfo, request);
			//获取所有业务类型 
			busMap = getBusMap(corpCode,request);
			request.setAttribute("busMap", busMap);
			String startSubmitTime=null;
			String endSubmitTime=null;
			String username=null;
			//非第一次访问，则组装查询条件
			if(!isFirstEnter){
				//手机号码查询(由于网关的phone是bigint类型所以现在改为不支持模糊查询)
                String phone = blBiz.formatPhone(request.getParameter("phone"));
                if(phone !=null){
				    conditionMap.put("phone",phone);
                }
                //查询时间的条件
                //开始时间
                startSubmitTime = request.getParameter("sendtime");
                //结束时间
                endSubmitTime = request.getParameter("recvtime");
                //操作用户id
                username=request.getParameter("username");
                //添加查询条件
                if(username!=null&&!"".equals(username)) {
                	conditionMap.put("operateId", username);
                }
                //解决时间查询无效的问题
                if(startSubmitTime!=null&&!"".equals(startSubmitTime)){
                    conditionMap.put("optTime&>=",startSubmitTime);
                }
				if(endSubmitTime!=null&&!"".equals(endSubmitTime)){
                    conditionMap.put("optTime&<=",endSubmitTime);
                }
			}			
			//按照操作时间降序
			orderbyMap.put("optTime","DESC" );
			List<PbListBlackDelrecord> pbListBlacks = baseBiz.getByCondition(PbListBlackDelrecord.class,null, conditionMap, orderbyMap, pageInfo);
			//加密后的集合
			Map<Long, String> keyIdMap = new HashMap<Long, String>();
			//ID加密
			if(pbListBlacks != null && pbListBlacks.size() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMap(pbListBlacks, "Id", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询短信黑名单删除记录列表，参数加密失败。corpCode:"+corpCode);
						throw new Exception("查询短信黑名单删除记录列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询短信黑名单删除记录列表，从session中获取加密对象为空！");
					pbListBlacks.clear();
					throw new Exception("查询短信黑名单删除记录列表，获取加密对象失败。");
				}
			}
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("blackList", pbListBlacks);
			//传递查询时间
			request.setAttribute("sendtime", startSubmitTime );
			request.setAttribute("recvtime", endSubmitTime );
			request.setAttribute("username", username );
			request.setAttribute("keyIdMap", keyIdMap);
			//跳转到页面
			request.getRequestDispatcher(empRoot + basePath +"/bla_blackdeletelist.jsp")
			.forward(request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"短信黑名单删除记录","["+sDate+"]查询("+(pbListBlacks==null?0:pbListBlacks.size())+"/"+pageInfo.getTotalRec()+"),耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.GET);
		} catch (Exception e) {
			//异常处理逻辑
			EmpExecutionContext.error(e,"查询黑名单删除记录异常！");
			request.setAttribute("error", e);
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			try {
				request.getRequestDispatcher(empRoot + basePath +"/bla_blackdeletelist.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"短信黑名单删除记录servlet查询异常");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"短信黑名单删除记录servlet查询跳转异常");
			}
		}
	}
	/**
	 * 新增黑名单的方法
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
		opContent="新增黑名单";
		//返回结果
		String result = null;
		//企业编码
        String corpCode = request.getParameter("lgcorpcode"); 
        String username = request.getParameter("lgusername");
		try
		{
			//当前系统的所有号段
			String[] haoduan = wb.getHaoduan();
			//通道
			//String spgate = null;
			//业务编码
			String busCode = request.getParameter("busCode");
 			List<String> cpnos = new LinkedList<String>();
			List<String> phones = new LinkedList<String>();
			//String memo = null;
			String mobile = request.getParameter("mobile");
			//Integer spisuncm = 0;
            HashSet<String> resultList = null;
            String msg="";
			//单个新增
			if (Type != null && Type.equals("add"))
			{
				//单个黑名单添加
				if(mobile != null && !"".equals(mobile))
				{
					phones.clear();
					phones.add(mobile.trim());
				}
				//接收备注
				msg=request.getParameter("msgNote");
				msg = StringUtils.defaultIfEmpty(msg, " ");
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
				BufferedReader reader =null;
				while (it.hasNext())
				{
					FileItem fileItem = (FileItem) it.next();
					if (fileItem.getFieldName().equals("cpno"))
					{
						cpnos.add(fileItem.getString("UTF-8").toString());
					} else if (fileItem.getFieldName().equals("mobile"))
					{
						//手机号
						mobile = fileItem.getString("UTF-8").toString();
					}/*else if (fileItem.getFieldName().equals("spgate"))
					{
						//通道
						spgate = fileItem.getString("UTF-8").toString();
					} else if (fileItem.getFieldName().equals("memo"))
					{
						memo = fileItem.getString("UTF-8").toString();
					} else if (fileItem.getFieldName().equals("spisuncm"))
					{
						spisuncm = Integer.valueOf(fileItem.getString("UTF-8").toString());
					}  */
					else if (fileItem.getFieldName().equals("uploadbusCode"))
					{
						//业务编码
						busCode =  null == fileItem.getString("UTF-8")?"":fileItem.getString("UTF-8").toString();
					}
					else if(fileItem.getFieldName().equals("lgcorpcode"))
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
						// 文件名
						String fileCurName = fileItem.getName();
						// 通过文件名截取到文件类型
						String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();
						InputStream instream = fileItem.getInputStream();
						if(fileType.equals(".xls") || fileType.equals(".et"))
						{
							phones=jxExcel(fileItem.getInputStream(),busCode,corpCode);
						}
						// 解析excel2007文件
						else if(fileType.equals(".xlsx"))
						{
							reader=new Excel2007Reader().fileParset(temp, fileItem.getInputStream());
							String charset = new ChangeCharset().get_charset(instream);
							if(charset.startsWith("UTF-"))
							{
								reader.read(new char[1]);
							}
							String tmp;
							LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
							conditionMap.put("corpCode", corpCode);
							conditionMap.put("busCode", busCode);
							//根据业务类型获取当前系统的黑名单
							resultList = getlfBlackMapBySpgateAndBusCode(busCode,corpCode);
							//获取运营商号码段
							haoduan=new WgMsgConfigBiz().getHaoduan();
							while ((tmp = reader.readLine()) != null)
							{
								tmp = tmp.trim();
								//验证手机号的合法性
								if(phoneUtil.getPhoneType(tmp, haoduan) + 1 != 0)
								{
									if(checkRepeat(resultList, tmp))
									{
										phones.add(tmp);
										
									}
								}
							}
							//关闭流
							reader.close();
						}
					// 解析TXT文本
					else{
						/*String charset = new ChangeCharset().get_charset(instream);
						 reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(),charset));
						if(charset.startsWith("UTF-"))
						{
							reader.read(new char[1]);
						}*/
						reader = new ChangeCharset().getReader(instream, fileItem.getInputStream());
					
						String tmp;
						LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
						conditionMap.put("corpCode", corpCode);
						conditionMap.put("busCode", busCode);
						//根据业务类型获取当前系统的黑名单
						resultList = getlfBlackMapBySpgateAndBusCode(busCode,corpCode);
						//获取运营商号码段
						haoduan=new WgMsgConfigBiz().getHaoduan();
						while ((tmp = reader.readLine()) != null)
						{
							tmp = tmp.trim();
							//验证手机号的合法性
							if(phoneUtil.getPhoneType(tmp, haoduan) + 1 != 0)
							{
								if(checkRepeat(resultList, tmp))
								{
									phones.add(tmp);
									
								}
							}
						}
						//关闭流
						reader.close();
					}
					}		
				}
			}
			
			//企业黑名单总数
			int blackListCount = new BlackListBiz().getCorpBlackListCount(corpCode);
			//新增号码数
			int addPhoneCount =0;
			if(phones!=null){
				addPhoneCount = phones.size();
			}

			//没有号码
			if(addPhoneCount==0)
			{
				result = "noPhone";
			}
			else if(addPhoneCount>200000)
			{
				result = "overCount";
			}
			//企业黑名单总数超过500万
			else if(blackListCount + addPhoneCount > StaticValue.getBlackMaxcount())
			{
				result = "outCount";
			}else
			{
                phones = blBiz.formatPhoneList(phones); //格式化手机号
				busCode = "@".equals(busCode)?" ":busCode;
//				boolean res=new BlackListBiz().saveAll(phones, busCode, corpCode);
				//新增短信黑名单
				Map<String,String> params=new HashMap<String,String>();
				params.put("busCode", busCode);
				params.put("corpCode", corpCode);
				params.put("msg",msg);
				boolean res=new BlackListBiz().saveAllNew(phones, params);
				if(res)
				{
					result = "true"+String.valueOf(addPhoneCount);
					//将添加成功的黑名单实时的添加到黑名单map里
					blackBiz.addBlackListByList(phones, busCode, corpCode);
					//保存日志
					spLog.logSuccessString(username, opModule, opType, opContent,corpCode);
					EmpExecutionContext.info("企业："+corpCode+"，操作员："+username+"，新增黑名单成功，新增黑名单数："+addPhoneCount
							+"，企业黑名单总数：" + (blackListCount + addPhoneCount));
				}else
				{
					result = "false";
					spLog.logFailureString(username, opModule, opType, opContent+opSper,null,corpCode);
					EmpExecutionContext.info("企业："+corpCode+"，操作员："+username+"，新增黑名单失败，新增黑名单数："+addPhoneCount);
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
			//将信息放回前台
			if (Type != null && Type.equals("add"))
			{
				response.getWriter().print(result);
			}
			else
			{
				String s = request.getRequestURI();
				s = s+"?method=find&lgcorpcode="+corpCode;
				//跳转
				try {
					//跳转地址处理
					request.getSession(false).setAttribute("uploadresult", result);
					response.sendRedirect(s);
				} catch (IOException e) {
					EmpExecutionContext.error(e,"新增黑名单跳转异常！");
				}
			}
			//writer.print("<script>parent.show('"+result+"');</script>");
		}
	}
	
	/**
	 * 旧版本的EXCEL
	 * @param ins
	 * @param busCode
	 * @param corpCode
	 * @return
	 */
	public List<String>  jxExcel(InputStream ins,String busCode,String corpCode){
		List<String> phones=new ArrayList<String>();	 
		try
		{

			String phoneNum="";
			HSSFWorkbook workbook = new HSSFWorkbook(ins);
			
			//根据业务类型获取当前系统的黑名单
			HashSet<String> resultList = getlfBlackMapBySpgateAndBusCode(busCode,corpCode);
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
						if(phoneUtil.getPhoneType(phoneNum, haoduan) + 1 != 0)
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
	 * 删除黑名单的方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 * 
	 * @update 2013-6-13 linzhihan
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		PrintWriter writer = response.getWriter();
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
		opContent="删除黑名单";
		//删除总数
		int delCount = 0;
		Integer result = 0;
		BlackListBiz blBiz = new BlackListBiz();
		try {
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpCode = lfSysuser.getCorpCode();
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt == null)
			{
				EmpExecutionContext.error("删除短信黑名单，从session中获取加密对象为空！");
				throw new Exception("查询短信黑名单信息，获取加密对象失败。");
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
			if (blList != null && delCount > 0) {
//				result = blBiz.delBlacks(blList);
				Map<String,Object> resultMap=blBiz.delBlacksNew(blList,username);
				result = (Integer)resultMap.get("delCount")==null?0:(Integer)resultMap.get("delCount");
				if(result >0){
					String busCode ="";
					for (PbListBlack pbListBlack : blList) {
						busCode = pbListBlack.getSvrType();
						//busCode = " ".equals(busCode)?"@":busCode;
						blackBiz.delBlackList(pbListBlack.getPhone(), busCode, corpCode);
					}
				}
			}
			EmpExecutionContext.info("企业："+corpCode+"，操作员："+username+"，删除黑名单成功，共删除黑名单数："+delCount);
			writer.print(result);
			spLog.logSuccessString(username, opModule, opType, opContent,
					corpCode);
		} catch (Exception e) {
			writer.print(false);
			spLog.logFailureString(username, opModule, opType, opContent
					+ opSper, e, corpCode);
			EmpExecutionContext.error(e,"企业："+corpCode+"，操作员："+username+"，删除黑名单异常！");
		}
		writer.flush();
		writer.close();
	}
	
	/**
	 * 检查黑名单号码的方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void checkMobile(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String phone = request.getParameter("mobile");
		String busCode = request.getParameter("busCode");
		String corpCode = request.getParameter("lgcorpcode");
		 
		String result = "checkBl:";
		try
		{
			//获取运营商号码段
			String[] haoduan=new WgMsgConfigBiz().getHaoduan();
			if(phoneUtil.getPhoneType(phone, haoduan) + 1 == 0)
			{
				result=result+"errorPhone";
			}
			else
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				//组装过滤条件
				conditionMap.put("corpCode", corpCode);
				busCode = "@".equals(busCode)?" ":busCode;
				conditionMap.put("svrType", busCode);
				conditionMap.put("phone", blBiz.formatPhone(phone).replaceAll("^0+", ""));
				//黑名单状态，1表示为新增的，2表示已删除的
				conditionMap.put("optype", "1");
				//黑名单类型。1：短信；2：彩信
				conditionMap.put("blType", "1");
				List<PbListBlack> blList = baseBiz.getByCondition(PbListBlack.class, conditionMap, null);
				if(blList != null && blList.size() > 0)
				{
					result += "phoneRepeat";
				}
			}
		} catch (Exception e)
		{
			result += "error";
			EmpExecutionContext.error(e,"判断黑名单是否重复异常！");
		}finally
		{
			response.getWriter().print(result);
		}
	}
	
	/**
	 * 跳到添加黑名单页面
	 * @param request
	 * @param response
	 */
	/*
	public void doAdd(HttpServletRequest request, HttpServletResponse response)
	{
		String corpCode = request.getParameter("lgcorpcode");
		try {
			List<GtPortUsed> gtList;
			//获取通道号
			if("100000".equals(corpCode))
			{
				gtList = blackBiz.getMtSpgates();
				
			}else
			{
				gtList = new BlackListDAO().findMtSpgateByCorp(corpCode);
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//只显示自定义业务
			conditionMap.put("corpCode&in","0,"+corpCode);
			//conditionMap.put("corpCode&in","0,1,2,"+corpCode);
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			//排序
			orderbyMap.put("busId", StaticValue.ASC);
			//调用查询方法
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orderbyMap);
			request.setAttribute("busList", busList);
			request.setAttribute("gtList", gtList);
			request.getRequestDispatcher(this.empRoot + this.basePath +"/p_addBlacklist.jsp")
			.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e);
			request.setAttribute("error", e);
		}
	}*/
	/**
	 * 获取业务类型
	 * @param corpCode  企业编码
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getBusMap(String corpCode, HttpServletRequest request)throws Exception{
		List<LfBusManager> busList = null;
		Map<String, String> busMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		// 这里是企业编码
		map.put("corpCode&in", "0," + corpCode);
		LinkedHashMap<String, String> ordermap = new LinkedHashMap<String, String>();
		ordermap.put("busName", "ASC");
		// 调用查询方法
		busList = baseBiz.getByCondition(LfBusManager.class, map, ordermap);
		// 查询返回结果不为空时
		if(null != busList && 0 != busList.size())
		{
			for (LfBusManager lfBusManager : busList)
			{
				busMap.put(lfBusManager.getBusCode(), lfBusManager.getBusName());
			}
		}
		busMap.put("@", MessageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_11", request));
		return busMap;
	}
	
	// 过滤重复号码
	private boolean checkRepeat(HashSet<String> aa, String ee)
	{
		ee = blBiz.formatPhone(ee);
		// 过滤掉字符串前面的零
		ee = ee.replaceAll("^0+", "");
		if(aa.contains(ee))
		{
			return false;
		}
		else
		{
			aa.add(ee);
		}
		return true;
	}
	
	
	/**
	 * 获取黑名单
	 * @param busCode   业务类型
	 * @param corpCode  企业编码
	 * @return
	 * @throws Exception
	 */
	public HashSet<String> getlfBlackMapBySpgateAndBusCode(String busCode,String corpCode) throws Exception{
		HashSet<String> resultList = new HashSet<String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		busCode = "@".equals(busCode)?" ":busCode;
		try{
            //组装过滤条件  			
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("svrType", busCode);
			conditionMap.put("optype", "1");
			conditionMap.put("blType", "1");
			//查询方法
			List<PbListBlack> lfBlacksList = baseBiz.getByCondition(PbListBlack.class, conditionMap, null);
			if(lfBlacksList == null || lfBlacksList.size() == 0){
				return resultList;
			}
			//循环查询结果
			PbListBlack blackList = null;
			Long userphone;
			for(int i = 0; i < lfBlacksList.size(); i++){
				blackList = lfBlacksList.get(i);
				userphone = blackList.getPhone();
				resultList.add(userphone.toString());
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取黑名单列表异常！");
			throw e;
		}
		//返回结果list
		return resultList;
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
        //业务类型
		String busCode = request.getParameter("busCode");
		//获取当前登录用户的企业编码
		String corpCode = request.getParameter("lgcorpcode");
		PageInfo pageInfo = new PageInfo();
		// 获取导出文件路径
		String context = request.getSession(false).getServletContext().getRealPath(excelPath);
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//手机号码查询(由于网关的phone是bigint类型所以现在改为不支持模糊查询)
        String phone =blBiz.formatPhone(request.getParameter("phone"));
        if(phone!=null){
            conditionMap.put("phone", phone);
        }
		if(null != busCode && 0 != busCode.length())
		{
			busCode = "@".equals(busCode)?" ":busCode;
			conditionMap.put("svrType", busCode);
		}
		Map<String,String> busMap = null;
		//获取所有业务类型 
		busMap = getBusMap(corpCode,request);
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("optype", "1");
		conditionMap.put("blType", "1");
		String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);
		Map<String, String> resultMap=new BlackListBiz().createBlackExcel(conditionMap,context,busMap,pageInfo,langName);
        response.setContentType("html/text");
        PrintWriter out = response.getWriter();
		// 获取导出内容
		if(resultMap != null && resultMap.size() > 0)
		{
            HttpSession session = request.getSession(false);
            session.setAttribute("bla_export_map",resultMap);
            out.print("true");
//			String fileName = (String) resultMap.get("FILE_NAME");
//			String filePath = (String) resultMap.get("FILE_PATH");
//			// 弹出下载页面。
//			DownloadFile dfs = new DownloadFile();
//			dfs.downFile(request, response, filePath, fileName);
			setLog(request,"短信黑名单","["+sDate+"]excel导出("+resultMap.get("LIST_SIZE")+"),耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.OTHER);
		}else{
            out.print("false");
			setLog(request,"短信黑名单","["+sDate+"]excel导出失败,耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.OTHER);
        }
	}

	/**
	 * 下载黑名单导出文件
	 * @param request
	 * @param response
	 */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response)   {
        try
		{
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute("bla_export_map");
			if(obj != null){
			    // 弹出下载页面。
			    DownloadFile dfs = new DownloadFile();
			    Map<String, String> resultMap = (Map<String, String>) obj;
			    String fileName = (String) resultMap.get("FILE_NAME");
			    String filePath = (String) resultMap.get("FILE_PATH");
			    dfs.downFile(request, response, filePath, fileName);
			}
			session.removeAttribute("bla_export_map");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下载黑名单导出文件异常！");
		}
    }
	
	/**
	 * 导出TXT格式的信息
	 * @param request
	 * @param response
	 */
	public void exportTxt(HttpServletRequest request, HttpServletResponse response) throws Exception{
		long stime = System.currentTimeMillis();
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String sDate = df.format(stime);
        //业务类型
		String busCode = request.getParameter("busCode");
		//获取当前登录用户的企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 获取导出文件路径
		String context = request.getSession(false).getServletContext().getRealPath(excelPath);
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//手机号码查询(由于网关的phone是bigint类型所以现在改为不支持模糊查询)
        String phone =blBiz.formatPhone(request.getParameter("phone"));
        if(phone!=null){
            conditionMap.put("phone", phone);
        }
		if(null != busCode && 0 != busCode.length())
		{
			busCode = "@".equals(busCode)?" ":busCode;
			conditionMap.put("svrType", busCode);
		}
		//防止导出较少数据时候，重复导出数据
		//Thread.sleep(500);
		Map<String,String> busMap = null;
		//获取所有业务类型 
		busMap = getBusMap(corpCode,request);
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("optype", "1");
		conditionMap.put("blType", "1");
		PageInfo pageInfo = new PageInfo();
		// 设置每个excel文件的行数
		pageInfo.setPageSize(50000);
		List<PbListBlack> pbListBlacks = baseBiz.getByCondition(PbListBlack.class,null, conditionMap, null,pageInfo);
		// 计算出文件数
		int fileCount = pageInfo.getTotalPage();
//		StringBuffer sb=new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		Date curDate = new Date();
		
		String fileName = context + File.separator + "shortmessage"
		+ File.separator +"shortmessage_"+ sdf.format(curDate)+".txt";
		boolean have=false;
		if(pbListBlacks!=null){
			StringBuffer sb=new StringBuffer();
			int sum=0;	
	        PhoneUtil phoneUtil = new PhoneUtil();
	        String[] haoduan=new WgMsgConfigBiz().getHaoduan();
			for (int f = 0; f < fileCount; f++) {
				if (f > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
				{
					pbListBlacks =baseBiz.getByCondition(PbListBlack.class,null, conditionMap, null, pageInfo);
				}
				pageInfo.setPageIndex(f + 2);// 定位下一页
				for(int k=0;k<pbListBlacks.size();k++){
					PbListBlack pb=pbListBlacks.get(k);
					String code = " ".equals(pb.getSvrType())?"@":pb.getSvrType();
					String name=busMap.get(code)==null?"-":busMap.get(code);
	                String phoneStr = pb.getPhone()+"";
	                if(phoneUtil.getPhoneType(phoneStr, haoduan)+1==0){
	                    phoneStr = "00"+ phoneStr;
	                }
					sb.append(phoneStr+","+name+"\r\n");
					sum=sum+1;
					
	                if(sum%500000==0){
	                    //避免数据量大比如百万左右数据库时候，出现内存溢出
	    				have=writeToTxtFile(fileName, sb.toString());
	                	 sb.setLength(0);
	                }
	                //避免数据量大比如百万左右数据库时候，出现内存溢出
	//				have=writeToTxtFile(fileName, phoneStr+","+name+"\r\n");
				}
	
			}
			if(sb.length()>0){
				have=writeToTxtFile(fileName, sb.toString());
				 sb.setLength(0);
			}
		}
		
        response.setContentType("html/text");
        PrintWriter out = response.getWriter();
        Map<String,String> resultMap = new HashMap<String, String>();
		String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);
		/*短信黑名单*/
		String mmsBlacklist = "zh_HK".equals(langName)?"smsBlacklist_":"zh_TW".equals(langName)?"短信黑名單":"短信黑名单";
		if(have){
			String name = mmsBlacklist+ sdf.format(curDate)+".txt";
            resultMap.put("FILE_NAME", name);
            resultMap.put("FILE_PATH", fileName );
            HttpSession session = request.getSession(false);
            session.setAttribute("bla_export_map", resultMap);
            out.print("true");
			setLog(request,"短信黑名单","["+sDate+"]txt导出("+pageInfo.getTotalRec()+"),耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.OTHER);
//			// 弹出下载页面。
//			DownloadFile dfs = new DownloadFile();
//			dfs.downFile(request, response, fileName, name);
		}else{
            out.print("false");
			setLog(request,"短信黑名单","["+sDate+"]txt导出失败,耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.OTHER);
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
	      File filee = new File(fileName);

	    	//如果文件夹不存在就创建
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

	      foss = new FileOutputStream(fileName, true);

	      ByteArrayInputStream baInput = new ByteArrayInputStream(content
	        .getBytes("GBK"));

	      byte[] buffer = new byte[1024];
	      int ch = 0;
	      while ((ch = baInput.read(buffer)) != -1)
	      {
	        foss.write(buffer, 0, ch);
	      }
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
				EmpExecutionContext.error("短信黑名单设置，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "短信黑名单设置，从session获取加密对象异常。");
			return null;
		}
	}
}
