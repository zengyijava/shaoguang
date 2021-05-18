package com.montnets.emp.servmodule.txgl.keywords.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.keywords.LfKeywords;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.keyword.dao.KeyWordAtomDAO;
import com.montnets.emp.servmodule.txgl.keywords.biz.KeyWordBiz;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 处理关键字想关方法的servlet
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description 
 */
@SuppressWarnings("serial")
public class key_keywordSvt extends BaseServlet 
{

	final String empRoot = "txgl";
	final String basePath = "/keywords";
	
	//关键字biz
	final KeyWordBiz kwBiz = new KeyWordBiz();
	//日志
	//操作模块
	final String opModule=StaticValue.PARAM_PRESERVE;
	//操作用户
	final String opSper = StaticValue.OPSPER;
	
	//关键字实体类
	final LfKeywords keyword=new LfKeywords();
	//基础数据库操作类
	final BaseBiz baseBiz=new BaseBiz();
	//日志业务逻辑类
	final SuperOpLog spLog=new SuperOpLog();
	
	/**
	 * 查询关键字的方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long stime = System.currentTimeMillis();
		List<LfKeywords> kwList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		PageInfo pageInfo=new PageInfo();
		try {
			
			//是否第一次打开
			boolean isFirstEnter = false;
			isFirstEnter=pageSet(pageInfo, request);
			//关键字
			String keyWord = request.getParameter("keyWord");
			//关键字状态
			String keyState = request.getParameter("keyState");
			//关键字类型
			String keyStype = request.getParameter("keyStype");
			//获取当前登录用户的企业编码
			//String corpCode = request.getParameter("lgcorpcode"); 
			//登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String corpCode = lfSysuser.getCorpCode();

			//页面非第一次加载
			if(!isFirstEnter){
				//关键字查询
				conditionMap.put("keyWord&like", keyWord.toUpperCase());
				//根据关键字状态查询
				conditionMap.put("kwState", keyState);
				
				if(keyStype!=null && keyStype != "")
				{
					if(keyStype.equals("00"))
					{
						conditionMap.put("corpCode", "100000");
					}
					else if(keyStype.equals("01"))
					{
						conditionMap.put("corpCode", corpCode);
					}
				}
				else{
					conditionMap.put("corpCode&in", "100000,"+corpCode);
				}
			}else
			{
				//如果非100000的用户登录，则需要查找100000及自己本身的关键字
				if(!"100000".equals(corpCode))
				{
					conditionMap.put("corpCode&in", "100000,"+corpCode);
				}
				else
				{
					conditionMap.put("corpCode", corpCode);
				}
			}
            orderMap.put("corpCode", "asc");
            orderMap.put("kwId", "asc");
			//获取关键字list
//			kwList = baseBiz.getByCondition(LfKeywords.class, null, conditionMap, orderMap, pageInfo);
			kwList = baseBiz.getByConditionNoCount(LfKeywords.class, null, conditionMap, orderMap, pageInfo);
			//加密后的集合
			Map<Long, String> keyIdMap = new HashMap<Long, String>();
			//ID加密
			if(kwList != null && kwList.size() > 0)
			{

				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMap(kwList, "KwId", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询关键字列表，参数加密失败。corpCode:"+corpCode);
						throw new Exception("查询关键字列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询关键字列表，从session中获取加密对象为空！");
					kwList.clear();
					throw new Exception("查询关键字列表，获取加密对象失败。");
				}
			}
			
			if(conditionMap.containsKey("keyWord&like"))
			{
				//关键字查询
				conditionMap.put("keyWord&like", keyWord);
			}
			//分页信息
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("kwList", kwList);
			request.setAttribute("keyIdMap", keyIdMap);
			request.setAttribute("lgcorpcode", corpCode);
			request.setAttribute("kwStype", keyStype);
			request.getRequestDispatcher(this.empRoot + this.basePath +"/key_keyword.jsp")
			.forward(request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"关键字设置","["+sDate+"]查询("+(kwList==null?0:kwList.size())+"/"+pageInfo.getTotalRec()+"),耗时："+(System.currentTimeMillis()-stime)+"ms",StaticValue.GET);
		} catch (Exception e) {
			//异常信息打印
			EmpExecutionContext.error(e,"关键字查询异常！");
			//分页信息
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			try {
				request.getSession(false).setAttribute("error", e);
				request.getRequestDispatcher(this.empRoot + this.basePath +"/key_keyword.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"关键字servlet查询异常");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"关键字servlet查询跳转异常");
			}
		}
	}

	/**
	 * 新建与修改关键字的方法
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void update(HttpServletRequest request, HttpServletResponse response)
	{
		String loginCorpCode="";
		String loginUserName="";
		
		try {
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			loginCorpCode=lfSysuser.getCorpCode();
			loginUserName=lfSysuser.getUserName();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String opType = request.getParameter("opType");
			String keywoed = request.getParameter("keywoed");
			String curKey = request.getParameter("curKey");
			//关键字状态
			String kwState = request.getParameter("kwState");
			//企业编码，从session中获取
			//String corpcode = request.getParameter("lgcorpcode");
			String corpcode = loginCorpCode;
			//操作员ID
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			if(lguserid==null||"".equals(lguserid)){
				lguserid="0";
			}
			  //操作类型
		    String oppType = null;
		    //操作内容
			String opContent =null;
			//记录操作日志的操作员
			String opUser="";
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, Long.parseLong(lguserid));
			opUser = sysuser==null?"":sysuser.getUserName();
		  
			int count=0;
			boolean result2=false;
			if(keywoed!=null&&!"".equals(keywoed))
			{
				//统一转换成大写
				keywoed=keywoed.toUpperCase();
			}
			keyword.setKeyWord(keywoed);
			keyword.setCorpCode(corpcode); 
			conditionMap.put("corpCode", corpcode);
			//单个新增
			if (opType != null && opType.equals("add"))
			{
				
				//日志操作类型
				oppType = StaticValue.ADD;
				opContent = "新建关键字（关键字名："+keyword.getKeyWord()+"）";
				keyword.setKwState(Integer.valueOf(kwState));
				
				try
				{
					//数据库中关键字最多只允许有5000个,超过5000则不添加(单个企业)
					List<LfKeywords> lfKeywordsList = baseBiz.getByCondition(LfKeywords.class, null, conditionMap, null);
					if (lfKeywordsList!= null && lfKeywordsList.size() >= 5000)
					{
						response.getWriter().print("cap");
					}
					else
					{
						//将关键字添加到数据库，并返回结果
						boolean result = (kwBiz.addKw(keyword)>0);
						if (result)
						{
							response.getWriter().print("true");
							
							EmpExecutionContext.info("企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员,"+opContent);
							
							spLog.logSuccessString(opUser, opModule, oppType, opContent,corpcode);
						}
					}
				} catch (Exception e)
				{

					response.getWriter().print("error");
					//保存日志
					spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,corpcode);
					EmpExecutionContext.error(e,"新增关键字异常！");
				}
				return;
			}
			//修改
			else if (opType!=null && opType.equals("edit"))
			{
				try
				{
					oppType=StaticValue.UPDATE;
					//加密ID
					String keyId = request.getParameter("keyId");
					String kwIdStr = "";
					//加密对象
					ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
					//加密对象不为空
					if(encryptOrDecrypt != null)
					{
						//解密
						kwIdStr = encryptOrDecrypt.decrypt(keyId);
						if(kwIdStr == null)
						{
							EmpExecutionContext.error("修改关键字信息，参数解密码失败，keyId:"+keyId);
							throw new Exception("修改关键字信息，参数解密码失败。");
						}
					}
					else
					{
						EmpExecutionContext.error("修改关键字信息，从session中获取加密对象为空！");
						throw new Exception("修改关键字信息，获取加密对象失败。");
					}
					//获取关键字信息，通过ID和企业编码查询
					//LfKeywords kw = baseBiz.getById(LfKeywords.class, kwId);
					conditionMap.clear();
					conditionMap.put("corpCode", corpcode);
					conditionMap.put("kwId", kwIdStr);
					List<LfKeywords> kw = baseBiz.getByCondition(LfKeywords.class, conditionMap, null);
					if(kw == null || kw.size() < 1)
					{
						EmpExecutionContext.error("修改关键字信息，通过ID和企业编码查询关键字信息为空！corpcode:"+corpcode+"，kwId:"+kwIdStr);
						//异常错误
						response.getWriter().print("error");
						spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, null,corpcode);
						return;
					}
					keyword.setKwState(kw.get(0).getKwState());
					keyword.setKwId(Long.valueOf(kwIdStr));
					opContent="修改关键字（关键字名："+keyword.getKeyWord()+"）";
					
					//验证管关键字是否存在
					if (keywoed != null && !"".equals(keywoed) && !curKey.equals(keywoed))
					{
						result2 = kwBiz.isKwExists(keywoed,corpcode);
					}
					if(!result2){
						//调用修关键字对象的方法，并返回结果
						boolean kwresult = kwBiz.editKeyword(keyword, curKey);
						if(kwresult)
						{
							response.getWriter().print("true2");
							
							EmpExecutionContext.info("企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员,"+opContent);
							
							//保存日志
							spLog.logSuccessString(opUser, opModule, oppType, opContent,corpcode);
						}
					}
					else{
						//关键字已存在
						response.getWriter().print("wordExists");
					}
				} catch (Exception e)
				{
					//异常错误
					response.getWriter().print("error");
					spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,corpcode);
					EmpExecutionContext.error(e,"修改关键字异常！");
				}
				return;
			}
			//批量导入
			else if(opType == null || opType.equals(""))
			{
				Pattern pattern = Pattern.compile("[#=$%@*<>\\\\/^?'_&,\"]");
				oppType = StaticValue.OTHER;
				//日志内容
				opContent = "上传关键字";
				
				//获取一个存放临时文件的目录
				String uploadPath = StaticValue.FILEDIRNAME;				
				DiskFileItemFactory factory = new DiskFileItemFactory();
				//设置一次上传文件的大小限制
				factory.setSizeThreshold(100 * 1024);
				String temp = new TxtFileUtil().getWebRoot()+uploadPath;
				//临时文件的存放设置
				factory.setRepository(new File(temp));
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> fileList = null;
				try
				{
					fileList = upload.parseRequest(request);
					
					
				} catch (FileUploadException e)
				{
					//保存日志
					spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,corpcode);
					EmpExecutionContext.error(e,"批量导入关键字获取文件流异常！");
				}
				Iterator<FileItem> it = fileList.iterator();
				while (it.hasNext())
				{
					FileItem fileItem = (FileItem) it.next();
				    if(fileItem.getFieldName().equals("lgcorpcode"))
					{
						//当前登录用户的企业编码
						corpcode = null == fileItem.getString("UTF-8")?"":fileItem.getString("UTF-8").toString();
					}
				    else if (!fileItem.isFormField()
							&& fileItem.getName().length() > 0)
					{
						//获取上传流
						InputStream instream = fileItem.getInputStream();
						/*//获取文本编码
						String charset = new ChangeCharset().get_charset(instream);
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(fileItem.getInputStream(),
										charset));
						if(charset.startsWith("UTF-"))
						{
							reader.read(new char[1]);
						}*/
						BufferedReader reader = new ChangeCharset().getReader(fileItem.getInputStream(), fileItem.getInputStream());
						String tmp;
						int size = 0;
						int sum = 0;
						HashSet<String> keyRepeatList = new HashSet<String>();
						List<LfKeywords> kwlist = new ArrayList<LfKeywords>();
						try
						{
							//当前系统的关键字集合(单个企业)
							conditionMap.clear();
							conditionMap.put("corpCode", corpcode);
							List<LfKeywords> lfKeywordsList = baseBiz.getByCondition(LfKeywords.class, null, conditionMap, null);
							if(lfKeywordsList!=null)
							{
								size = lfKeywordsList.size();
								for(LfKeywords keyw : lfKeywordsList)
								{
									keyRepeatList.add(keyw.getKeyWord());
								}
							}else
							{
								size = 0;
							}
							//读取内容
							while ((tmp = reader.readLine()) != null)
							{
								//超过5000则不导入
								if (size+sum >= 5000)
								{
									break;
								}
								//替换特殊符
								tmp=tmp.replace(" ","" );
								tmp=tmp.replace("　","" );
								tmp=tmp.replace("\r\n","" );
								tmp=tmp.replace("\t","" );
								tmp=tmp.toUpperCase();
								//如果关键字不为空
								if(tmp.equals("") || tmp.length() > 10)
								{
									continue;
								}
								//关键字合法性校验
								Matcher mat = pattern.matcher(tmp);
								if(mat.find()){
									continue;
								}
								
/*								if(tmp.lastIndexOf("&")>=0
										||tmp.lastIndexOf("\'")>=0
										||tmp.lastIndexOf("\"")>=0
										||tmp.lastIndexOf(",")>=0)
								{
									continue;
								}*/
								//验证关键字是否以已存在
								if (keyRepeatList.contains(tmp))
								{
										continue;
								}
								//如果关键字验证都通过，就添加保存
								if ((size+sum) < 5000 )
								{
									LfKeywords kw = new LfKeywords();
									kw.setKeyWord(tmp);
									kw.setKwState(1);
									kw.setCorpCode(corpcode);
									kwlist.add(kw);
									keyRepeatList.add(tmp);
									sum++;
								}
							}
							//调用批量保存的方法，并返回操作数
							count=kwBiz.addKwList(kwlist);
							//保存日志
							spLog.logSuccessString(opUser, opModule, oppType, opContent,corpcode);
							EmpExecutionContext.info("企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员,上传导入"+count+"个关键字成功！");
						} catch (Exception e)
						{
							//异常日志
							spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,corpcode);
							//打印异常
							EmpExecutionContext.error(e,"批量导入关键字异常！");
						} finally
						{
							//结果返回给前台
							if ((size+sum) >= 5000)
							{
								count = 0 - count;
							}
							request.getSession(false).setAttribute("kwcount", String.valueOf(count));							
							//关闭流
							reader.close();
							fileItem.delete();
						}
					}
				}
			}
			//跳转地址处理
			String s = request.getRequestURI();
			s = s+"?method=find&lgcorpcode="+corpcode;
			//重新加载缓存中的关键字
			new KeyWordAtomDAO().setAllKeyWord(KeyWordAtom.getKeyWordMap());
			//跳转
			response.sendRedirect(s);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"更新关键字异常！");
			request.setAttribute("error", e);			
		}finally {
			//重新加载缓存中的关键字
			new KeyWordAtomDAO().setAllKeyWord(KeyWordAtom.getKeyWordMap());
		}
	}
	/**
	 * 修改关键字状态
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//当前登录操作员信息
		String loginCorpCode="";
		String loginUserName="";
		
		//企业编码
		String corpcode ="";
		  //操作类型
	    String oppType = StaticValue.UPDATE;
	    //操作内容
		String opContent =null;
		//记录操作日志的操作员
		String opUser="";
		
		try {
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			loginCorpCode=lfSysuser.getCorpCode();
			loginUserName=lfSysuser.getUserName();
			
			//企业编码
			corpcode = loginCorpCode;
			opUser = lfSysuser.getUserName();
			//关键字状态
			String kwState = request.getParameter("kwState");			
			//加密ID
			String keyId = request.getParameter("keyId");
			String kwId = "";
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				kwId = encryptOrDecrypt.decrypt(keyId);
				if(kwId == null)
				{
					EmpExecutionContext.error("修改关键字状态，参数解密码失败，keyId:"+keyId);
					throw new Exception("修改关键字状态，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("修改关键字状态，从session中获取加密对象为空！");
				throw new Exception("修改关键字状态，获取加密对象失败。");
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", corpcode);
			conditionMap.put("kwId", kwId);
			List<LfKeywords> kw = baseBiz.getByCondition(LfKeywords.class, conditionMap, null);
			if(kw == null || kw.size() < 1)
			{
				EmpExecutionContext.error("修改关键字状态，通过ID和企业编码查询关键字信息为空！corpcode:"+corpcode+"，kwId:"+kwId);
				//异常错误
				response.getWriter().print("error");
				spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, null,corpcode);
				return;
			}
			 
			//LfKeywords kw = baseBiz.getById(LfKeywords.class, kwId);
			kw.get(0).setKwState(Integer.valueOf(kwState));
			
			if(kwState.equals("1"))
			{
				opContent="修改关键字：("+kw.get(0).getKeyWord()+")的状态为启用";
			}
			else
			{
				opContent="修改关键字：("+kw.get(0).getKeyWord()+")的状态为停用";
			}			
           
			//调用修关键字对象的方法，并返回结果
			boolean kwresult = kwBiz.changeState(kw.get(0));
			if(kwresult)
			{
				response.getWriter().print("true");
				//保存日志
				spLog.logSuccessString(opUser, opModule, oppType, opContent,corpcode);
			}
			//修改缓存中的关键字状态
			new KeyWordAtomDAO().setAllKeyWord(KeyWordAtom.getKeyWordMap());

			EmpExecutionContext.info("企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员,"+opContent);
		} catch (Exception e) {
			//异常错误
			response.getWriter().print("error");
			spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,corpcode);
			EmpExecutionContext.error(e,"企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员,修改关键字状态异常！");
		}
		return;
	}
	
	/**
	 * 删除关键字的方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//当前登录操作员信息
		String loginCorpCode="";
		String loginUserName="";
		
		  //操作类型
	    String oppType = null;
		//删除日志
		oppType=StaticValue.DELETE;
		String opType = request.getParameter("opType");
		//String userid = request.getParameter("lguserid");
		//String corpcode = request.getParameter("lgcorpcode");
		Long userid = 0L;
		String corpcode = "";
		
	    //操作内容
		String opContent =null;
		//记录操作日志的操作员
		String opUser="";
		try
		{
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(null != lfSysuser){
				loginCorpCode=lfSysuser.getCorpCode();
				loginUserName=lfSysuser.getUserName();
				//LfSysuser sysuser = baseBiz.getById(LfSysuser.class, Long.parseLong(userid));
	//			opUser = lfSysuser==null?"":lfSysuser.getUserName();
				opUser = lfSysuser.getUserName();
				userid = lfSysuser.getUserId();
			}
			corpcode = loginCorpCode;
			PageInfo pageInfo=new PageInfo();
			if (opType != null && "all".equals(opType))
			{
				opContent="删除所有关键字";
				List<LfKeywords> lfKeywordsList = baseBiz.getByCondition(LfKeywords.class, userid, null, null, pageInfo);
				
				if(lfKeywordsList.size() <= 0)
				{
					response.getWriter().print("NoExists");
					lfKeywordsList.clear();
					return;
				}
				//删除所有
				boolean result=baseBiz.deleteAll(LfKeywords.class);
				response.getWriter().print(result);
				spLog.logSuccessString(opUser, opModule, oppType, opContent,corpcode);
			}
			else if (opType != null && "s".equals(opType))
			{
				//获取要删除的关键字的id的字符串
				String kwIds = request.getParameter("kwIds");
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt == null)
				{
					EmpExecutionContext.error("删除关键字，从session中获取加密对象为空！");
					throw new Exception("删除关键字，获取加密对象失败。");		
				}
				//批量删除
				if(kwIds.indexOf(",") >= 0)
				{
					//拆分出来进行单个解密再拼接查询条件
					String[]kwIdClump = kwIds.split(",");
					StringBuffer wdSb = new StringBuffer();
					for(int i=0; i<kwIdClump.length; i++)
					{
						wdSb.append(encryptOrDecrypt.decrypt(kwIdClump[i])).append(",");
					}
					wdSb.deleteCharAt(wdSb.lastIndexOf(","));
					kwIds = wdSb.toString();
				}
				//单个删除
				else
				{
					//解密
					kwIds = encryptOrDecrypt.decrypt(kwIds);
				}
				String keyName = request.getParameter("keyName");
				opContent = "删除关键字（关键字名："+keyName+"）";
				//批量删除
				if(keyName==null) {
					opContent = "删除关键字（批量删除）";
				}
				//删除前需要判断一下是否是删除的10000用户的
				if(!"100000".equals(corpcode))
				{
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("corpCode", "100000");
					conditionMap.put("kwId&in", kwIds);
					//查询删除记录中是否包含10000的
					List<LfKeywords> lfKeywordsList = baseBiz.getByCondition(LfKeywords.class, null, conditionMap, null);
					if(lfKeywordsList!=null && lfKeywordsList.size()>0)
					{
						response.getWriter().print("-1");
						return;
					}					
				}
				//调用删除方法，并返回成功数
				int count = kwBiz.delKwList(kwIds);
				
				EmpExecutionContext.info("企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员,删除"+count+"个关键字成功。");
				
				//成功数返回给页面
				response.getWriter().print(count);
				spLog.logSuccessString(opUser, opModule, oppType, opContent,corpcode);
			}
		}
		catch (Exception e)
		{
			//失败
			response.getWriter().print("false");
			//保存日志
			spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,corpcode);
			//异常信息打印
			EmpExecutionContext.error(e,"企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员,删除关键字异常！");
		}finally {
			//重新加载缓存中的关键字
			new KeyWordAtomDAO().setAllKeyWord(KeyWordAtom.getKeyWordMap());
		}
	}
	
	/**
	 * 判断关键字是否已存在的方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void checkExist(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		boolean result = true;
		try
		{
			//获取关键字
			String keyWord = request.getParameter("keyword");
			String corpcode = request.getParameter("lgcorpcode");
			if (keyWord != null && !"".equals(keyWord))
			{
				//转换成大写
				keyWord=keyWord.toUpperCase();
				//判断并返回结果
				result = kwBiz.isKwExists(keyWord,corpcode);
				//将结果返回页面
				response.getWriter().print(result);
			}
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"判断关键字是否已存在异常！");
		}
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
				EmpExecutionContext.error("关键字设置，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "关键字设置，从session获取加密对象异常。");
			return null;
		}
	}
}
