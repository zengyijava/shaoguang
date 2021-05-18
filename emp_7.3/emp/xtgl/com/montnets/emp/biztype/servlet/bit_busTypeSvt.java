package com.montnets.emp.biztype.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringEscapeUtils;

import com.montnets.emp.biztype.biz.BitTypeUploadXlsBiz;
import com.montnets.emp.biztype.biz.bit_busTypeBiz;
import com.montnets.emp.biztype.vo.LfBusManagerVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.pasgrpbind.LfAccountBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

@SuppressWarnings("serial")
public class bit_busTypeSvt extends BaseServlet
{
	private final BaseBiz baseBiz=new BaseBiz();
	private final bit_busTypeBiz busTypeBiz=new bit_busTypeBiz();
	private final BitTypeUploadXlsBiz bitUpload = new BitTypeUploadXlsBiz();
	
	/**
	 * 业务类型管理
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
		List<LfBusManagerVo> busVoList = null;
		LfBusManagerVo busVo = new LfBusManagerVo();
		String state = "";
		String riseLevel = "";
		String busName = "";
		String busCode = "";
		String name = "";
		String depIds = "";
		String busType = "";
		String  userId = "";
		String departmentTree = "";
		String depNam = "";
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		PageInfo pageInfo=new PageInfo();
		try
		{
			//userId =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			userId = SysuserUtil.strLguserid(request);


			//是否第一次打开
			boolean isFirstEnter = false;
			
			isFirstEnter=pageSet(pageInfo, request);
			//是否包含子机构
            String isContainsSun=request.getParameter("isContainsSun");
            if (isContainsSun != null && !"".equals(isContainsSun))
			{
            	busVo.setIsContainsSun(isContainsSun);
			}        
			//分页设置
			if (!isFirstEnter)
			{
				//状态
				state = request.getParameter("state");
				if(state != null && !"".equals(state)){
					busVo.setState(Integer.parseInt(state));
				}
				//优先级
				riseLevel = request.getParameter("riseLevel");
				if(riseLevel != null && !"".equals(riseLevel)){
					busVo.setRiseLevel(Integer.parseInt(riseLevel));
				}
				//业务类型
				busType = request.getParameter("busType");
				if(busType != null && !"".equals(busType)){
					busVo.setBusType(Integer.parseInt(busType));
				}
				//读取查询条件
				//业务名称
				busName = request.getParameter("busName");
				if (busName != null && !"".equals(busName))
				{
					busName = busName.trim();
					busVo.setBusName(busName.toUpperCase());
				}
				//业务编码
				busCode = request.getParameter("busCode");
				if (busCode != null && !"".equals(busCode))
				{
					busCode = busCode.trim();
					busVo.setBusCode(busCode.toUpperCase());
				}
				//操作员名称
				name = request.getParameter("name");
				if (name != null && !"".equals(name))
				{
					name = name.trim(); 
					busVo.setName(name);
				}
				//机构id集合（包含子机构id）
				depIds = request.getParameter("depid");
				if (depIds != null && !"".equals(depIds))
				{
					//情况登录操作员机构id查询条件
					busVo.setDepId(null);
					busVo.setDepIds(depIds);
				}
				//机构名称
				depNam = request.getParameter("depNam");
				if (depNam != null && !"".equals(depNam))
				{
					busVo.setDepName(depNam);
				}
				//创建起始时间
				String startSubmitTime = request.getParameter("sendtime");
				if(startSubmitTime != null && !"".equals(startSubmitTime)){
					busVo.setStartSubmitTime(startSubmitTime);
				}
				//创建结束时间
				String endSubmitTime = request.getParameter("recvtime");
				if(endSubmitTime != null && !"".equals(endSubmitTime)){
					busVo.setEndSubmitTime(endSubmitTime);
				}
				//是否包含子机构
				if(isContainsSun==null||"".equals(isContainsSun)){
					request.setAttribute("isContainsSun", "0");
				}else{
					request.setAttribute("isContainsSun", "1");
				}
			}
			String lgcorpcode = request.getParameter("lgcorpcode");
			busVo.setCorpCode(lgcorpcode);
			//加载机构树  全局机构树
//			departmentTree = busTypeBiz.getDepartmentJosnData(Long.parseLong(userId));
			departmentTree = busTypeBiz.getDepartmentJosnData(2L,lgcorpcode);
			//当前登录企业
			//只显示自定义业务
			busVoList = busTypeBiz.getLfBusManagerVo(busVo, pageInfo);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"业务类型查询失败！");
		}
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("busVoList", busVoList);
		request.setAttribute("busVo", busVo);
		request.setAttribute("departmentTree", departmentTree);
		//增加查询日志
		long end_time=System.currentTimeMillis();
		if(pageInfo!=null){
			String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
			opSucLog(request, "业务类型管理", opContent, "GET");
		}
		request.getRequestDispatcher("xtgl/biztype/bit_busType.jsp").forward(
				request, response);
	}
	/**
	 * 加载机构树
	 * @Title: createDeptTree
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @return void
	 */
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try
		{
			Long depId = null;
			Long userid=null;
			//部门iD
			String depStr = request.getParameter("depId");
			//操作员账号
//			String userStr = request.getParameter("lguserid");
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
//			if(userStr != null && !"".equals(userStr.trim())){
//				userid = Long.parseLong(userStr);
//			}
			//从session获取企业编码
			LfSysuser lfSysuser = getLoginUser(request);
//			String corpCode = lfSysuser.getCorpCode();
			String departmentTree = busTypeBiz.getDepartmentJosnData2(depId, lfSysuser);
			response.getWriter().print(departmentTree);		
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"群发历史或群发任务查询条件中的机构树加载方法异常");
		}
	}
	/**
	 *  绑定内置业务类型（或模块）与发送账号
	 * @param request
	 * @param response
	 */
	public void bindSP(HttpServletRequest request, HttpServletResponse response) 
	{
		//绑定内置业务类型（或模块）与发送账号
		try {
			boolean result=false;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 发送账号名称
			String SPUserID = request.getParameter("SPUserID");
			// 业务编码
			String busCode = request.getParameter("busCode").trim();
					
			if (busCode != null) {
				conditionMap.put("busCode", busCode);
				conditionMap.put("bindType", "2");
			}
			List<LfAccountBind> bindList = baseBiz.getByCondition(
					LfAccountBind.class, conditionMap, null);

			// 模块编码
			String menuCode = StaticValue.getMenuCode(busCode);
			Timestamp createTime = new Timestamp(new Date().getTime());
			String description = request.getParameter("description").trim();
			//当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			//当前操作员guid
			Long lgguid = Long.valueOf(request.getParameter("lgguid"));
			
			LfAccountBind bind = new LfAccountBind();
			// 绑定类型为业务发送账号绑定
			bind.setBindType(2);
			//业务编码
			bind.setBusCode(busCode);
			//创建者
			bind.setCreaterGuid(lgguid);
			//创建时间
			bind.setCreatetime(createTime);
			//企业编码
			bind.setCorpCode(lgcorpcode);
			//描述
			if (description!=null && !"".equals(description)) {
				bind.setDescription(description);
			}
			bind.setSpuserId(SPUserID);
			if (menuCode != null && !"".equals(menuCode)) {
				bind.setMenuCode(menuCode);
			}
			if(bindList.size()>0){//判断是新建还是更新
				bind.setId(bindList.get(0).getId());
				result = baseBiz.updateObj(bind);
			}else{
				result = baseBiz.addObj(bind);
			}
			if (result) {
				response.getWriter().print("true");
			} else {
				response.getWriter().print("false");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务类型绑定发送账号异常！");
		}
	}
	
	/**
	 * 新增业务类型
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//业务名称
		String busName = request.getParameter("busName").toUpperCase();
		//业务编码
		String busCode = request.getParameter("busCode").toUpperCase();
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		//当前登录操作员
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		//业务类型
		String busType = request.getParameter("busType");
		//业务优先级
		String riseLevel = request.getParameter("riseLevel");
		
		String corpCode = lgcorpcode;
		try
		{
			LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			Long depId = lfsysuser.getDepId();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("busName",busName);
			conditionMap.put("corpCode&in","0,1,2,"+corpCode);
			List<LfBusManager> busList = baseBiz.getByCondition(
					LfBusManager.class, conditionMap, null);
			//检查名称重复
			if (busList != null && busList.size() > 0)
			{
				response.getWriter().print("nameExists");
				return;
			}
			conditionMap.clear();
			conditionMap.put("busCode",busCode);
			conditionMap.put("corpCode&in","0,1,2,"+corpCode);
			busList = baseBiz.getByCondition(LfBusManager.class, conditionMap,
					null);
			//检查业务编码重复
			if (busList != null && busList.size() > 0)
			{
				response.getWriter().print("codeExists");
				return;
			}
			LfBusManager busManager = new LfBusManager();
			//都不重复可以保存入库
			busManager.setBusCode(busCode);
			busManager.setBusName(busName);
			busManager.setBusType(Integer.parseInt(busType));
			busManager.setRiseLevel(Integer.parseInt(riseLevel));
			busManager.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			busManager.setCreateTime(new Timestamp(System.currentTimeMillis()));
			busManager.setUserId(Long.parseLong(lguserid));
			busManager.setDepId(depId);
			busManager.setCorpCode(corpCode);
			busManager.setState(0);
			busManager.setBusDescription(request.getParameter("busDescription"));

			boolean result = baseBiz.addObj(busManager);
			response.getWriter().print(result);
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "新建业务类型"+(result==true?"成功":"失败")+"。[业务名称，业务编码，业务类型，优先级别]" +
						"("+busName+"，"+busCode+"，"+busType+"，"+riseLevel+")";
				EmpExecutionContext.info("业务类型管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "ADD");
			}
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"新增业务类型异常！");
		}
	}

	/**
	 * 删除业务类型
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//业务类型ID
		String busId = request.getParameter("busId");
		try
		{
			
			//查询操作之前记录
			LfBusManager befchgEntity = baseBiz.getById(LfBusManager.class, busId);
			String befchgCont = befchgEntity.getBusName()+"，"+befchgEntity.getBusCode()+"，"+befchgEntity.getBusType()+"，"
								+befchgEntity.getRiseLevel()+"，"+befchgEntity.getState();
			
			//异步返回删除结果
			String result = baseBiz.deleteByIds(LfBusManager.class, busId) > 0 ? "true": "false";
			response.getWriter().print(result);

			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "删除业务类型"+("true".equals(result)?"成功":"失败")+"。[业务名称，业务编码，业务类型，优先级别，状态]" +
						"("+befchgCont+")";
				EmpExecutionContext.info("业务类型管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "DELETE");
			}				
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"删除业务类型异常！");
		}
	}

	/**
	 * 修改业务类型
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//业务类型名称
		//String busName = StringEscapeUtils.unescapeHtml(request.getParameter("busName")).toUpperCase().trim();
		String busName = request.getParameter("busName").toUpperCase().trim();
		//业务编码
		//String busCode = request.getParameter("busCode").toUpperCase().trim();
		//业务id
		String busId = request.getParameter("busId");
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		//当前登录操作员
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		//业务类型
		String busType = request.getParameter("busType");
		//业务优先级
		String riseLevel = request.getParameter("riseLevel");
		//状态
		String state = request.getParameter("state");
		String corpCode = lgcorpcode;
		try
		{
			LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			Long depId = lfsysuser.getDepId();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0,1,2,"+lgcorpcode);
			conditionMap.put("busName", busName);
			conditionMap.put("busId&<>", busId);
			List<LfBusManager> busList = baseBiz.getByCondition(
					LfBusManager.class, conditionMap, null);
			//检查业务名称是否重复
			if (busList != null && busList.size() > 0)
			{
				response.getWriter().print("nameExists");
				return;
			}
			conditionMap.remove("busName");
			//conditionMap.put("busCode", busCode);
			//busList = baseBiz.getByCondition(LfBusManager.class, conditionMap,null);
			//检查业务编码是否重复
			//if (busList != null && busList.size() > 0)
			//{
			//	response.getWriter().print("codeExists");
			//	return;
			//}
			LfBusManager bus = baseBiz.getById(LfBusManager.class, busId);
			
			//查询操作之前记录
			String befchgCont = bus.getBusName()+"，"+bus.getBusCode()+"，"+bus.getBusType()+"，"+bus.getRiseLevel()+"，"+bus.getState();
			
			bus.setBusName(busName);
			bus.setBusType(Integer.parseInt(busType));
			bus.setRiseLevel(Integer.parseInt(riseLevel));
			bus.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			bus.setUserId(Long.parseLong(lguserid));
			bus.setDepId(depId);
			bus.setCorpCode(corpCode);
			bus.setState(Integer.parseInt(state));
			bus.setBusDescription(request.getParameter("busDescription"));
			//异步返回更新结果
			boolean result = baseBiz.updateObj(bus);
			response.getWriter().print(result);
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "修改业务类型"+(result==true?"成功":"失败")+"。[业务名称，业务编码，业务类型，优先级别，状态]" +
						"("+befchgCont+")->("+busName+"，"+bus.getBusCode()+"，"+busType+"，"+riseLevel+"，"+state+")";
				EmpExecutionContext.info("业务类型管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}			
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"修改业务类型异常！");
		}
	}
	
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
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
	
	
	public void uploadBusManage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//添加与日志相关
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
				long startTimeByLog = System.currentTimeMillis();  //解析上传文件开始时间
				//获取当前登录操作员名称
				String lguserid = SysuserUtil.strLguserid(request);
				String cropcode =null;
				LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			    Long  userId=lfsysuser.getUserId();
			    Long  depId = lfsysuser.getDepId();
				cropcode = lfsysuser.getCorpCode();
				lguserid=lfsysuser.getGuId()+"";
				//创建DiskFileItemFactory
				DiskFileItemFactory factory = new DiskFileItemFactory();
				//创建文件上传解析器
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> fileList = null;
				if(cropcode==null||"".equals(cropcode)){
					EmpExecutionContext.error("业务类型管理上传文件,获取当前登录对象出现异常");
					return;
				}	
				if(lguserid==null||"".equals(lguserid)){
					EmpExecutionContext.error("业务类型管理上传文件,获取当前登录对象出现异常");
					return;
				}	
				try
				{
					//解析上传数据
					fileList = upload.parseRequest(request);		
					//添加日志，上传文件有时会出现“FileUploadException:Read timed out”文件上传超时的异常，因此添加日志记录解析上传文件的耗时，看看耗时多久会出现超时的情况，从而进一步排查问题
					long endTimeByLog = System.currentTimeMillis();  //解析上传文件结束时间
					long consumeTimeByLog = endTimeByLog - startTimeByLog;  //解析上传文件耗时
					Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
						String opContent1 = "业务类型管理，上传文件解析开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms";
						EmpExecutionContext.info("业务类型管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
								loginSysuser.getUserName(), opContent1, "GET");
					}
					
				} catch (FileUploadException e)
				{
					//添加日志，上传文件有时会出现“FileUploadException:Read timed out”文件上传超时的异常，因此添加日志记录解析上传文件的耗时，看看耗时多久会出现超时的情况，从而进一步排查问题
					long endTimeByLog = System.currentTimeMillis();  //解析上传文件结束时间
					long consumeTimeByLog = endTimeByLog - startTimeByLog;  //解析上传文件耗时
					String opContent1 = "上传业务类型管理文件出现异常 ！，上传文件解析开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms";			
					EmpExecutionContext.error(e, opContent1);
				}
				Iterator<FileItem> it = fileList.iterator();
				Timestamp startTime= new Timestamp(startTimeByLog);
				while (it.hasNext())
				{
					FileItem fileItem =  it.next();
					//对上传文件进行处理
					if (!fileItem.isFormField()
							&& fileItem.getName().length() > 0)
					{
						
						//上传文件名称
						String fileCurName = fileItem.getName();
						String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
						//如果是.xls文件
						if(fileType.equals(".xls")){
						bitUpload.uploadXlsType(request, userId, depId, cropcode, startTime, fileItem);
						//new CopyOfBitTypeUploadXlsBiz().uploadXlsType(request, userId, depId, cropcode, startTime, fileItem);
						}else if(fileType.equals(".xlsx")){
							bitUpload.uploadXlsxType(request, userId, depId, cropcode, startTime, fileItem);
					
						}
					}
				}
		
		//			System.out.println("机构已有员工数："+employeeSize+" 文件上传数据条数："+(rows)+"成功添加员工数："+resultCount+" 整个过程用时："+(System.currentTimeMillis()-start));
			find(request, response);
		
	}

	
	
}
