/**
 * 
 */
package com.montnets.emp.mmsmate.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfMaterialVo;
import com.montnets.emp.entity.mmsmate.LfMaterial;
import com.montnets.emp.entity.mmsmate.LfMaterialSort;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.mmsmate.biz.MaterialBiz;
import com.montnets.emp.servmodule.ydcx.constant.ServerInof;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 彩信素材页面
 * t_mmsMaterial.htm
 */
@SuppressWarnings("serial")
public class mat_mmsMaterialSvt extends BaseServlet {
	
	private final MaterialBiz biz = new MaterialBiz();
	private final BaseBiz baseBiz = new BaseBiz();
	
	//操作模块
	private final String opModule=StaticValue.TEMP_MANAGE;
	//操作用户
	private final String opSper = StaticValue.OPSPER;
	private final String empRoot = "ydcx";
	private final String basePath = "/mmsmate";
	
	//private LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
	//操作类型
    //String opType = "";
    //操作内容
	//String opContent ="";
	//private TxtFileUtil txtfileutil = new TxtFileUtil();
	//private String dirUrl = txtfileutil.getWebRoot();
	/**
	 *   查询页面
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		try {
			String skip=request.getParameter("skip");
			if("true".equals(skip)){
				request.getSession(false).setAttribute("mat_skip", skip);
			}
			request.getRequestDispatcher(empRoot  + basePath  + "/mat_mmsMaterial.jsp").forward(
					request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"跳转彩信素材页面出现异常！");
			request.getSession(false).setAttribute("error", e);
		}
	}
	//获取素材信息
	public void getTable(HttpServletRequest request, HttpServletResponse response) {
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		try {
			String childCode=null;
			String parentCode=null;
			//分页信息
			PageInfo pageInfo = new PageInfo();
			String skip=(String)request.getSession(false).getAttribute("mat_skip");
			boolean isFirstEnter =  pageSet(pageInfo,request);
			if("true".equals(skip)){
				pageInfo=(PageInfo)request.getSession(false).getAttribute("mat_pageInfo");
				isFirstEnter=false;
			}
			
			 Long id = null;
			try{
				//操作员用户ID
				 String userId =request.getParameter("userId");
					 id = Long.valueOf(userId);
			}catch (Exception e) {
				EmpExecutionContext.error(e,"彩信素材当前操作员lguserid转化失败！");
			}
			
			String lgcorpcode = request.getParameter("lgcorpcode");
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", lgcorpcode+",100000");
			//设置序列
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			orderbyMap.put("sortId", "asc");
			//查询
			String strSortIds = "";
			List<LfMaterialSort> lfMaterialSortList = baseBiz.getByCondition(LfMaterialSort.class, conditionMap, orderbyMap);
			for (int i = 0; i < lfMaterialSortList.size(); i++) {
				strSortIds+=lfMaterialSortList.get(i).getSortId();
				if(i != lfMaterialSortList.size()-1){
					strSortIds+=",";
				}
			}
			conditionMap.clear();
			if(null != strSortIds && !"".equals(strSortIds))
			{
				conditionMap.put("sortId&in",strSortIds);
			}
				
			
			if (!isFirstEnter) {
				
				 childCode = request.getParameter("childCode");
			     parentCode = null==request.getParameter("parentCodetemp")?"":request.getParameter("parentCodetemp");
				Integer sortId = null;
				String sortIds = null;
				String pchildSid = null;
				if("true".equals(skip)){
					childCode=(String)request.getSession(false).getAttribute("mat_childCode");
					parentCode=(String)request.getSession(false).getAttribute("mat_parentCode");
					request.getSession(false).setAttribute("mat_skip","false");
				}
				
				if(!"0".equals(parentCode))
				{
					pchildSid = biz.getSortIdsByParentCode(childCode);
					
					if(null != childCode && 0 != childCode.length()) 
					{
						sortId= biz.getSortIdByChildCode(childCode);
						sortIds = sortId.toString();
					}
					
					if( null != pchildSid && !"".equals(pchildSid))
					{
						//sortIds = pchildSid+sortId.toString();
						sortIds = pchildSid + String.valueOf(sortId);
					}
					//System.out.println(sortId);
					if(null != sortIds && !"".equals(parentCode))
					{
						conditionMap.put("sortId&in",sortIds);
						
					}
					
				}
				request.getSession(false).setAttribute("mat_childCode", childCode);
				request.getSession(false).setAttribute("mat_parentCode", parentCode);
			}
			conditionMap.put("corpCode", lgcorpcode);
			List<LfMaterialVo> lfMaterialSortVoList = biz.getMaterialInfos(id, conditionMap, pageInfo);
			request.setAttribute("lfMaterialSortVoList", lfMaterialSortVoList);
			request.setAttribute("pageInfo", pageInfo);
			request.getSession(false).setAttribute("mat_pageInfo", pageInfo);
			
			//设置服务器名称
			new ServerInof().setServerName(getServletContext().getServerInfo());
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"毫秒，数量："+pageInfo.getTotalRec();
				opSucLog(request, "移动彩讯-彩讯素材", opContent, "GET");
			}
			request.getRequestDispatcher(empRoot  + basePath  + "/mat_mmsMaterialTable.jsp").forward(
					request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取彩信素材列表出现异常！");
			request.getSession(false).setAttribute("error", e);
		}
	}
	//素材类别名称
	public void checkSortName(HttpServletRequest request, HttpServletResponse response) {
		//素材类别名称
		String sortName = request.getParameter("sortName");
		//企业编码
		String corpCode = request.getParameter("corpCode");
		
		
		boolean exists = false;
	//	String corpCode = "";
		try
		{
			//判断是否数据库存在素材名称
			//corpCode = this.getCorpCode();
			exists = biz.isSortNameExists(sortName,corpCode);
			response.getWriter().print(exists);
		} catch (Exception e)
		{ 
			EmpExecutionContext.error(e,"素材类别名称重复验证出现异常！");
			request.getSession(false).setAttribute("error", e);
		}
	}
	//判断素材名称
	public void checkMtalName(HttpServletRequest request, HttpServletResponse response) {
		//素材名称
		String mtalName = request.getParameter("mtalName");
		try
		{
			response.getWriter().print(biz.isMatlNameExists(mtalName));
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"素材名称重复验证出现异常！");
			request.getSession(false).setAttribute("error", e);
		}
 
	}
	//删除素材
	public void deleteMaterial(HttpServletRequest request, HttpServletResponse response) {
		try
		{
			//素材IDS
			String mtaIds = request.getParameter("ids");
			int delNum = -1;
			
			//查询删除的彩信素材集合
			 LinkedHashMap<String,String> logConditionMap=new LinkedHashMap<String, String>();
			 logConditionMap.put("mtalId&"+StaticValue.IN, mtaIds);
			 LinkedHashMap<String,String> logOrderMap=new LinkedHashMap<String, String>();
			 logOrderMap.put("mtalId", StaticValue.ASC);
			List<LfMaterial> deleteMaterialList=baseBiz.getByCondition(LfMaterial.class, logConditionMap, logOrderMap);
			
			//删除操作 
			delNum  = baseBiz.deleteByIds(LfMaterial.class, mtaIds);
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				//增加操作日志
				String deleteMsg="";
				for (int i = 0; i < deleteMaterialList.size(); i++)
				{
					deleteMsg+="["+deleteMaterialList.get(i).getMtalId()+"，"+deleteMaterialList.get(i).getMtalName()+"]，";
				}
				String contnet="删除彩信素材"+"成功。(总数："+deleteMaterialList.size()+")[素材ID，素材名称]("+deleteMsg+")";
				EmpExecutionContext.info("彩信素材", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "DELETE");
			}
			
			response.getWriter().print(delNum);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"删除素材出现异常！");
			request.getSession(false).setAttribute("error", e);
		}

	}
	//新增彩信素材
	public void addMaterial(HttpServletRequest request, HttpServletResponse response) {
		String opType = "";
		String opContent ="";
		HttpSession session  = request.getSession(false);
		//彩信名称
		String mtalName = request.getParameter("mtalName");
		//彩信素材类别编码
		String childCode = request.getParameter("childCode");
		
		//企业编码
		String corpCode = request.getParameter("corpCode");
		Long userId = null;
		//获取操作员用户ID
		//String id = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String id = SysuserUtil.strLguserid(request);


		String opUser ="";
		String corpcode = "";
		try{
				userId = Long.valueOf(id);

				LfSysuser sysuser = baseBiz.getById(LfSysuser.class, id);
				//如果sysuser为空，则返回错误！
				if(sysuser==null){
					response.getWriter().print(false);
					return;
				}
				opUser = sysuser==null?"":sysuser.getUserName();
				corpcode = sysuser.getCorpCode();
		}catch (Exception e) {
			EmpExecutionContext.error(e,"新增素材当前操作员lguserid转化失败！");
		}
	

		Integer sortId = null ;
		opContent = "新建彩信素材";
		opType = StaticValue.ADD;
		try
		{
			if(null != childCode && 0 != childCode.length())
			{
			   sortId = Integer.parseInt(childCode);// biz.getSortIdByChildCode(childCode);
 			}
			//备注
		//	String comments = null == request.getParameter("comments")?" ":request.getParameter("comments");
			//彩信素材地址
			String mtalAddress = request.getParameter("mtalAddress");
			String addrTemp2 = request.getParameter("addrTemp2");
			String mtalType = " ";
			String fileSize = null;
			Integer fileWidth = null;
			Integer fileHeight = null;
			if(null != mtalAddress && 0 != mtalAddress.length())
			{
 			    mtalType = mtalAddress.substring(mtalAddress.lastIndexOf(".")+1,mtalAddress.length());
			    fileSize = null == session.getAttribute("FILE_SIZE")?"":session.getAttribute("FILE_SIZE").toString();
 			}
			fileWidth = null == session.getAttribute("FILE_WIDTH")?0:Integer.parseInt(session.getAttribute("FILE_WIDTH").toString());
			fileHeight = null == session.getAttribute("FILE_HEIGHT")?0:Integer.parseInt(session.getAttribute("FILE_HEIGHT").toString());
			LfMaterial lfMaterial = new LfMaterial();
		//	lfMaterial.setComments(comments);
			lfMaterial.setMtalAddress(addrTemp2);
			if( fileSize!=null && 0 != fileSize.length())
			{
				lfMaterial.setMtalSize(fileSize);
			}
			lfMaterial.setMtalType(mtalType);
			lfMaterial.setMtalHeight(fileHeight);
			lfMaterial.setMtalWidth(fileWidth);
			lfMaterial.setSortId(sortId);
			lfMaterial.setUserId(userId);
			lfMaterial.setMtalName(mtalName);
			lfMaterial.setCorpCode(corpCode);
			 
			response.getWriter().print(baseBiz.addObj(lfMaterial));
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String contnet="新建彩信素材"+"成功。[素材名称，素材类型，素材分类ID]("+lfMaterial.getMtalName()+"，"+lfMaterial.getMtalType()+"，"+lfMaterial.getSortId()+")";
				EmpExecutionContext.info("彩信素材", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "ADD");
			}
		} catch (Exception e)
		{
			//进入异常
			EmpExecutionContext.error(e,"新增彩信素材失败！");
			//写日志
			new SuperOpLog().logFailureString(opUser, opModule, opType, opContent+opSper, e,corpcode);
			request.getSession(false).setAttribute("error", e);
		}
		new SuperOpLog().logSuccessString(opUser, opModule, opType, opContent,corpcode);

	}
	
	/**
	 * 生成树
	 * 
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public void createTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
 		String busProTree = (String)request.getAttribute("busProTree");
 		String corpcode = (String)request.getParameter("lgcorpcode");
		 busProTree = biz.getMaterialJosnData(corpcode);
		 request.setAttribute("busProTree",busProTree);
    	 response.getWriter().print(busProTree);
	}


	/**
	 * 生成树
	 * 
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public void getMatelTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
 		String busProTree = (String)request.getAttribute("busProTree");
		 String corpCode = request.getParameter("lgcorpcode");
		 busProTree = biz.getMaterialJosnData2(corpCode);
		 request.setAttribute("busProTree",busProTree);
    	 response.getWriter().print(busProTree);
	}
	/**
	 * 修改彩信素材
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void updateMaterial(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String opType = "";
		String opContent ="";
		HttpSession session  = request.getSession(false);
		//彩信素材ID
		String mtalId = request.getParameter("mtalId");
		//彩信类别ID
		String sortId = request.getParameter("sortId");
		//备注
	//	String comments = null == request.getParameter("comments")?" ":request.getParameter("comments");
		//素材名称
		String mtalName = request.getParameter("mtalName");
		//素材URL
		String mtalAddress = null == request.getParameter("mtalAddress")?" ":request.getParameter("mtalAddress");
		LinkedHashMap<String,String> objectMap = new LinkedHashMap<String,String>();
		String fileSize = null;
		Integer fileWidth = null;
		Integer fileHeight = null;
		opContent = "修改彩信素材";
		opType = StaticValue.UPDATE;
		
		LfMaterial material = baseBiz.getById(LfMaterial.class, mtalId);
		//老的素材名称
		String oldMtalName=material.getMtalName();
		if(null != mtalAddress && 0 != mtalAddress.length())
		{
			//mtalAddress = mtalAddress.substring(mtalAddress.lastIndexOf("/")+1,mtalAddress.length());
			 
            String mtalType = mtalAddress.substring(mtalAddress.lastIndexOf(".")+1,mtalAddress.length());
            fileSize = null==session.getAttribute("FILE_SIZE")?"":session.getAttribute("FILE_SIZE").toString();
            fileWidth = null == session.getAttribute("FILE_WIDTH")?0:Integer.parseInt(session.getAttribute("FILE_WIDTH").toString());
            fileHeight = null == session.getAttribute("FILE_HEIGHT")?0:Integer.parseInt(session.getAttribute("FILE_HEIGHT").toString());
            objectMap.put("mtalAddress", mtalAddress);
            objectMap.put("mtalType", mtalType);
            if( 0 != fileSize.length())
            {
            	 objectMap.put("mtalSize", fileSize.toString());
            }
           
            objectMap.put("mtalHeight", fileHeight.toString());
            objectMap.put("mtalWidth", fileWidth.toString());
            
            
            material.setMtalAddress(mtalAddress);
    		material.setMtalType(mtalType);
    		if(fileHeight != null && fileHeight != 0){
    			material.setMtalHeight(fileHeight);
    		}
    		if(fileWidth != null && fileWidth != 0){
    			material.setMtalWidth(fileWidth);
    		}
    		
		}
		
		objectMap.put("sortId", sortId);
		objectMap.put("mtalName", mtalName);
		//做修改操作
		try
		{
			//boolean updateok = biz.updateMaterial(objectMap, mtalId);
			material.setSortId(Integer.valueOf(sortId));
			material.setMtalName(mtalName);
			material.setMtalUptime(new Timestamp(System.currentTimeMillis()));
			if( 0 != fileSize.length())
			{
				material.setMtalSize(fileSize);
			}
			boolean updateok = baseBiz.updateObj(material);
			
			String result=null;
			if(updateok){
				result="成功";
			}else{
				result="失败";
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String content="修改彩信素材"+result+"。[素材ID，素材名称]("+mtalId+","+oldMtalName+")"+"-->("+mtalId+","+mtalName+")";
				EmpExecutionContext.info("彩信素材", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), content, "UPDATE");
			}
			
			response.getWriter().print(updateok);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"修改彩信素材出现异常！");
 			request.getSession(false).setAttribute("error", e);
		}

	}
	/**
	 * 新增素材分类
	 * 
	 * @param request
	 * @param response
	 */
	public void addMaterialSort(HttpServletRequest request, HttpServletResponse response) {
		String opContent ="";
		String opType = "";
		//彩信素材父类别ID
		String parentSortId = request.getParameter("childCode");
		//彩信素材名称
		String sortName = request.getParameter("sortName");
		String corpCode = request.getParameter("corpCode");
		opContent = "新建彩信素材分类";
		opType = StaticValue.ADD;
		LfMaterialSort lfMaterialSort = new LfMaterialSort();
		try
		{
			lfMaterialSort.setParentCode(parentSortId);   
			//上一级素材分类编码
			lfMaterialSort.setSortName(sortName);		
			//分类名称
			lfMaterialSort.setCorpCode(corpCode);	
			//企业编码
			//默认设值  
			lfMaterialSort.setChildCode("1000000000");
			response.getWriter().print(biz.addMaterialSort(lfMaterialSort));
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String contnet="新建彩信素材分类"+"成功。[素材分类名称，素材分类父ID]("+lfMaterialSort.getSortName()+"，"+lfMaterialSort.getParentCode()+")";
				EmpExecutionContext.info("彩信素材", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "ADD");
			}
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"新增素材分类出现异常！");
 			request.getSession(false).setAttribute("error", e);
		}
	 
	}
	
	/**
	 * 删除素材分类
	 * 
	 * @param request
	 * @param response
	 */
	public void delMaterialSort(HttpServletRequest request, HttpServletResponse response) {
		//彩信素材类别编码
	   String parentSortId =  request.getParameter("childCode") ;
	   String opContent ="";
	   opContent = "删除彩信素材分类";
	   String opType = "";
	   opType = StaticValue.DELETE;
		try
		{
			String materialSortName="";
			if (parentSortId != null && !"".equals(parentSortId))
			{
				//1:删除成功；0：有子机构不能删除；-1：删除失败;2:素材分类下存在素材
				LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
				
				int result = -1;
				 
				conditionMap.put("parentCode", parentSortId);
				List<LfMaterialSort> materialSortsList = baseBiz.getByCondition(LfMaterialSort.class, conditionMap, null);
				if(materialSortsList != null && materialSortsList.size() > 0){
					//0：有子机构不能删除
					result = 0;
					 
				}else
				{
					conditionMap.clear();
					conditionMap.put("sortId",parentSortId);
					List<LfMaterialSort> msList = baseBiz.getByCondition(LfMaterialSort.class, conditionMap, null);
					if(msList!=null&&msList.size()>0){
						materialSortName=msList.get(0).getSortName();
					}
					String sortId = "";
					for(int index = 0;index<msList.size();index++)
					{
						sortId+=msList.get(index).getSortId()+",";
					}
					if ( null != sortId && 0 != sortId.length() )
					{
						sortId = sortId.substring(0,sortId.lastIndexOf(","));
						
					}
					conditionMap.clear();
					conditionMap.put("sortId&in", sortId);
					List<LfMaterial> mList = baseBiz.getByCondition(LfMaterial.class, conditionMap,null);
					if( null != mList && 0 < mList.size())
					{
						result=2;
					}
				}
			    if(2 != result && 0 != result)
			    {
			    	result = biz.delMaterialSortDep(parentSortId);
			    } 
				response.getWriter().flush();
				response.getWriter().print(result);
				
				//增加操作日志
				if(result==1){
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						String contnet="删除彩信素材分类"+"成功。(总数：1)[素材分类ID，素材分类名称]("+parentSortId+"，"+materialSortName+")";
						EmpExecutionContext.info("彩信素材", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "DELETE");
					}
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"删除素材分类出现异常！");
 			request.getSession(false).setAttribute("error", e);
		}
	}
	 
	
	/**
	 * 上传附件
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void upload(HttpServletRequest request, HttpServletResponse response)
	{
		TxtFileUtil txtfileutil = new TxtFileUtil();
		String dirUrl = txtfileutil.getWebRoot();
		String fileType = request.getParameter("fileType");
		String mms = request.getParameter("mms");
		String lgcorpcode = request.getParameter("lgcorpcode");
	
		String corpCodeDir = lgcorpcode+"/";
		try {
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			//漏洞修复 session里获取操作员信息
			Long lguserid = SysuserUtil.longLguserid(request);

			//文件大小
			long size = 0L;
			//图片宽
			int width = 0;
			//图片高
			int height = 0;
			//图片路径
			String imgUrl = "";
			//声音路径
			String musicUrl = "";
			//先创建文件夹
			new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_MATERIAL + corpCodeDir);
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//设定了100KB的缓冲区
			factory.setSizeThreshold(100*1024);          
			factory.setRepository(new File(dirUrl + StaticValue.MMS_MATERIAL + corpCodeDir));
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request); 
			//解析request请求
			Iterator<FileItem> iter = items.iterator();
			boolean isOver = false;
			//上传文件服务器是否成功
			boolean isuploadFileServerSuccess=false;
			HttpSession session = request.getSession(false);
			DecimalFormat df = new  DecimalFormat("0.00");
			session.removeAttribute("FILE_SIZE");
			session.removeAttribute("FILE_WIDTH");
			session.removeAttribute("FILE_HEIGHT");
			double fileSize = 0.00;
			String fileName = "2_" + (int)(lguserid-0) + "_"
			+ (new SimpleDateFormat("yyyyMMddHHmmssS")).format(Calendar.getInstance().getTime());
			if (mms != null && !"".equals(mms))
			{
                boolean delete = new File(dirUrl + mms).delete();
                if (!delete) {
                    EmpExecutionContext.error("删除文件失败！");
                }
            }
			while (iter.hasNext()) {
			    FileItem fileItem = (FileItem) iter.next();
			    //如果文件大小大于50KB则不保存
			    if (fileItem.getSize() - 80*1024 > 0)
		    	{
		    		isOver = true;
		    	}
			    else
			    {
			    	 if(!fileItem.isFormField() && fileItem.getName().length() > 0 && "pic".equals(fileType))
					    { 
				    		imgUrl = fileName+fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
					    	String name = dirUrl + StaticValue.MMS_MATERIAL + corpCodeDir + imgUrl;
					    	fileItem .write(new File(name));
					        size = fileItem.getSize();
					        BufferedImage bi = ImageIO.read(new File(name));
					    	width = bi.getWidth();
					    	height = bi.getHeight();
					    	
					    	//判断是否使用集群
							if(StaticValue.getISCLUSTER() ==1){
								CommonBiz commBiz = new CommonBiz();
								//上传文件到文件服务器
								if("success".equals(commBiz.uploadFileToFileCenter(StaticValue.MMS_MATERIAL + corpCodeDir + imgUrl,false)))
								{
									//删除本地文件
									//commBiz.deleteFile(StaticValue.MMS_MATERIAL + corpCodeDir + imgUrl);
									isuploadFileServerSuccess=true;
								}
							}
					    }
					    else if(!fileItem.isFormField() && fileItem.getName().length() > 0 && "music".equals(fileType))
					    { 
					    	musicUrl = fileName+fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
					    	String name = dirUrl + StaticValue.MMS_MATERIAL + corpCodeDir + musicUrl;
					    	fileItem .write(new File(name));
					    	size = fileItem.getSize();
					    	
					    	//判断是否使用集群
							if(StaticValue.getISCLUSTER() ==1){
								CommonBiz commBiz = new CommonBiz();
								//上传文件到文件服务器
								if("success".equals(commBiz.uploadFileToFileCenter(StaticValue.MMS_MATERIAL + corpCodeDir + musicUrl,false)))
								{
									//删除本地文件
									//commBiz.deleteFile(StaticValue.MMS_MATERIAL + corpCodeDir + musicUrl);
									isuploadFileServerSuccess=true;
								}
							}
					    }
			    	    fileSize = new Double(size)/1024.00;
						session.setAttribute("FILE_SIZE",df.format(fileSize));
						session.setAttribute("FILE_WIDTH", width);
						session.setAttribute("FILE_HEIGHT", height);
			    }
			}
			if (isOver)
			{
				response.getWriter().print("{url:'false'}");
			}
			else
			{
				//是集群并且上传到文件服务器失败，则提示上传失败
				if(StaticValue.getISCLUSTER() ==1&&isuploadFileServerSuccess==false){
						response.getWriter().print("{url:'uploadfail'}");
				}else{
					if (!"".equals(imgUrl))
					{
						response.getWriter().print("{url:'" + StaticValue.MMS_MATERIAL + corpCodeDir + imgUrl+"',size:"+size+",width:"+width+",height:"+height+"}");
					}
					else if (!"".equals(musicUrl))
					{
						response.getWriter().print("{url:'" + StaticValue.MMS_MATERIAL + corpCodeDir + musicUrl+"',size:"+size+"}");
					}
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"彩信素材上传附件出现异常！");
		}
	}
	
	/**
	 * 重命名素材分类名称
	 * @param request
	 * @param response
	 */
	public void updateSortName(HttpServletRequest request, HttpServletResponse response){
		String opType = "";
		String opContent ="";
		//彩信类别名称
		String sortName = request.getParameter("sortName");
		String sortId = request.getParameter("childCode");
		boolean updateOk = false;
		opContent = "重命名素材分类名称";
		opType = StaticValue.UPDATE;
		try
		{
			LfMaterialSort lfMaterialSort= baseBiz.getById(LfMaterialSort.class, Long.parseLong(sortId));
			updateOk = biz.updateSortName(sortName, sortId);
			if(updateOk)
			{
				response.getWriter().print("1");
			}else
			{
				response.getWriter().print("0");
			}
			String result=null;
			if(updateOk){
				result="成功";
			}else{
				result="失败";
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String content="修改彩信素材分类"+result+"。[素材分类ID，素材分类名称]("+sortId+"，"+lfMaterialSort.getSortName()+")"+"-->("+sortId+"，"+sortName+")";
				EmpExecutionContext.info("彩信素材", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), content, "UPDATE");
			}
 		} catch (Exception e)
		{
 			EmpExecutionContext.error(e,"重命名素材分类名称出现异常！");
 			request.getSession(false).setAttribute("error", e);
		}
		
	}
	 
	/**
	 * 移动到指定的彩信素材分类
	 * @param request
	 * @param response
	 */
	public void updateSortId(HttpServletRequest request, HttpServletResponse response){
		String opType = "";
		String opContent ="";
		String ids = request.getParameter("ids");
		int count = 0;
		String sortId = request.getParameter("childCode");
		opContent = "移动到指定的彩信素材分类";
		opType = StaticValue.OTHER;
		try
		{
				//sortId = biz.getSortIdByChildCode(childCode);
				if( null != sortId && !"".equals(sortId))
				{
					String id[] = ids.split(",");
					
					boolean updateOk = false;
					
					LinkedHashMap<String,String> objectMap = new LinkedHashMap<String,String>();
					for(int i = 0;i<id.length;i++)
					{
						    String id2[] = id[i].split("&");
						    objectMap.put("sortId",sortId);
						    for(int j = 0;j< id2.length;j++)
						    {
						    	if( j %2 ==0)
						    	{
						    		updateOk = biz.updateMaterial(objectMap, id2[j]);
						    		if(updateOk)
									{
										count++;
									}
						    	}
								
						    }
						
						 
					}
				}
				response.getWriter().print(count);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"移动到指定的彩信素材分类出现异常！");
 			request.getSession(false).setAttribute("error", e);
		}

	}
	
	
	
	/**
	 * 
	 * 根据文件地址删除指定的资源文件
	 *  
	 * @param request
	 * 
	 * @param response
	 */
	public void delSource(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String addrTemp2 = request.getParameter("addrTemp2");
			String fileUrl = new TxtFileUtil().getWebRoot() + addrTemp2;
			File f = new File(fileUrl);
				 if (f.exists()) 
				 {
					 boolean r = f.delete();
					 if (r)
					 {
						 response.getWriter().print("true");
					 }
				 }else
				 {
					 response.getWriter().print("false");
				 }
			 
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"删除指定的资源文件出现异常！");
		}
	}
	/**
	 * 
	 * 根据文件地址删除指定的多个资源文件
	 *  
	 * @param request
	 * 
	 * @param response
	 */
	public void delMoreSource(HttpServletRequest request, HttpServletResponse response)
	{
		//文件地址
		String addrs  = request.getParameter("addrs");
		String webRoot = new TxtFileUtil().getWebRoot();
		String fileUrl = "";
		File f = null;
	    boolean r = false;
		int delnum = -1;
		try
		{
			if(null != addrs && 0 != addrs.length())
			{
				String[] fileTemp = addrs.split(",");
				
				for(int i = 0;i < fileTemp.length;i++)
				{
					fileUrl = webRoot + fileTemp[i];
					f = new File(fileUrl);
						 if (f.exists()) 
						 {
							 r = f.delete();
							 if (r)
							 {
								 delnum++;
							 }
						 }
				 } 
			}
			response.getWriter().print(delnum);
		}catch(Exception e){
			EmpExecutionContext.error(e,"通过文件地址删除指定的多个资源文件失败！");
			delnum = -1;
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
		Object obj = request.getSession(false).getAttribute("loginSysuser");
		if(obj == null) {
            return;
        }
		lfSysuser = (LfSysuser)obj;
		EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
	}
	
	/**
	 * 检测文件是否存在
	 * @param request
	 * @param response
	 */
	public void checkFile(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			TxtFileUtil tfu = new TxtFileUtil();
			String url = request.getParameter("url");
			//如果是集群，并且文件路径下没有彩信文件，则从文件服务器下载彩信文件。
			if(StaticValue.getISCLUSTER() ==1&&!tfu.checkFile(url)){
				CommonBiz commBiz=new CommonBiz();
				commBiz.downloadFileFromFileCenter(url);
			}
			response.getWriter().print(tfu.checkFile(url));
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"验证图片是否存在出现异常！");
		}
	}
}
