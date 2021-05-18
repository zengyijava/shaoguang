package com.montnets.emp.netnews.daoImpl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.netnews.common.FileJsp;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXDataBind;
import com.montnets.emp.netnews.entity.LfWXPAGE;

public class Wx_netMangerDaoImpl extends BaseBiz   {


	/**
	 * 复制PROC_WX_COPYMANAGER
	 */
	public boolean copyManager(String userid, String coid,HttpServletRequest request) throws Exception {
		try {
			LfWXBASEINFO baseInfo = getById(LfWXBASEINFO.class, Long.parseLong(coid));
			//1. 查询不到被复制的网讯模板则返回为false
			if(null == baseInfo)
			{
				return false;
			}
			//2. 复制网讯模板信息
			Timestamp date = new Timestamp(System.currentTimeMillis());
			baseInfo.setCREATDATE(date);
			baseInfo.setSTATUS(0);
			baseInfo.setCREATID(Long.parseLong(userid));
			//复制时候，取原来的动态表名
//			SimpleDateFormat formatDateTime = new SimpleDateFormat("yyMMddHHmmss");
//			String dtableName = "D" + userid + formatDateTime.format(new Date());
//			String ytableName = "Y" + userid + formatDateTime.format(new Date());
			baseInfo.setDataTableName(baseInfo.getDataTableName()); //设置互动项动态表
			baseInfo.setDynTableName(baseInfo.getDynTableName()); //设置参数动态表
			Long id = addObjReturnId(baseInfo);
			baseInfo.setNETID(id);
			baseInfo.setID(id);
			baseInfo.setOperAppStatus(0);//复制时候设置该状态为待审批
			baseInfo.setWxTYPE(1);//设置为非审核状态
			baseInfo.setSORT(new Long(0));
			updateObj(baseInfo);
			
			/*
			 * 3. 复制互动项表映射关系
			 */
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("netId", coid);
			List<LfWXDataBind> lstWXDataBind = getByCondition(LfWXDataBind.class, conditionMap, null);
			//遍历互动项映射关系并复制
			if(null != lstWXDataBind && lstWXDataBind.size() > 0)
			{
				LfWXDataBind dataBind = null;
				List<LfWXDataBind> dataBindList = new ArrayList<LfWXDataBind>();
				for (int i = 0; i < lstWXDataBind.size(); i++)
				{
					LfWXDataBind objCopyData = lstWXDataBind.get(i);
					
					dataBind = new LfWXDataBind();
					dataBind.setNetId(id);
					dataBind.setDId(objCopyData.getDId());
					
					dataBindList.add(dataBind);
				}
				// 添加复制互动项映射
				super.addList(LfWXDataBind.class, dataBindList);
			}
			conditionMap.clear(); // 清空条件值
			
			/*
			 * 4. 复制网讯模板页面 
			 */
			conditionMap.put("NETID", coid);
			List<LfWXPAGE> lstPages = getByCondition(LfWXPAGE.class, conditionMap, null);
			LfWXPAGE page = null;
			
			if(null != lstPages && lstPages.size() > 0)
			{
				for (int i = 0;  i < lstPages.size(); i++) 
				{
					page = lstPages.get(i);
					page.setNETID(id);
					page.setMODIFYID(Long.parseLong(userid));
					page.setCREATID(Long.parseLong(userid));
					page.setMODIFYDATE(date);
					page.setCREATDATE(date);
				}
				
				copyWXUdeitorPage(baseInfo, lstPages);
			}
			conditionMap.clear();
			
			conditionMap.put("NETID", String.valueOf(id));
			conditionMap.put("PARENTID&<>", "0");
			List<LfWXPAGE> pages2 = getByCondition(LfWXPAGE.class, conditionMap, null);
			if(pages2!=null && pages2.size()>0){
				LfWXPAGE page2 = null;
				//获取默认页面的id作为其他页面的parentid
				LinkedHashMap<String, String> conditionMap2 = new LinkedHashMap<String, String>();
				conditionMap2.put("NETID",  String.valueOf(id));
				conditionMap2.put("PARENTID", "0");
				Long parentid = getByCondition(LfWXPAGE.class, conditionMap2,null).get(0).getID();	
				//循环更新page对象的parentid
				for (int j = 0; j< pages2.size(); j++) 
				{
					page2 = pages2.get(j);
					page2.setPARENTID(parentid);
					updateObj(page2);
				}
			}
			
			//生成网讯静态页面模板
			Wx_getWapDaoImpl wx_getWapDao = new Wx_getWapDaoImpl();
			//此处状态为4是为了后面的图片处理
			//获取是否为集群常量
			int iIsCluster = com.montnets.emp.common.constant.StaticValue.getISCLUSTER();
			wx_getWapDao.getNetByJsp(String.valueOf(id),"0","copy",getServerUrl(request),request.getContextPath());
			if(iIsCluster==1){// 必须先生成本地文件，然后执行远程数据的COPY 上传
				CommonBiz biz=new CommonBiz();
				//获取JSP相对路径
				String oldsrc = "file/wx/PAGE/wx_" + coid + ".jsp";
				String newsrc = "file/wx/PAGE/wx_" + id + ".jsp";
				String result=biz.copyFile(oldsrc, newsrc);
				if(result=="error"){
					EmpExecutionContext.error("复制PROC_WX_COPYMANAGER");
					return false;
				}
			}
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"复制PROC_WX_COPYMANAGER");
			return false;
		} 
		return true;
	}
	
	/**
	 * DESC: 获取服务器请求地址
	 * @param request
	 * @return 请求地址
	 */
	public String getServerUrl(HttpServletRequest request){
		
		//String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName();
		String port = String.valueOf(request.getServerPort());
		if(null != port && port.length() > 0){
		  basePath = basePath + ":" + request.getServerPort()+request.getContextPath()+"/";
		}
		/*if(null != path && path.length() > 0){
		  basePath = basePath + path;
		}*/
		return basePath;
	}

	public List<LfWXPAGE> getnetByID(String netID) throws SQLException {
		List<LfWXPAGE> pages= null;
		try {
			LinkedHashMap<String, String> conditionMap  = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap  = new LinkedHashMap<String, String>();
			conditionMap.put("NETID", netID);
			orderbyMap.put("ID", "asc");
			pages = getByCondition(LfWXPAGE.class, conditionMap, orderbyMap);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询LF_WX_PAGE信息");
		}
		return pages;
	}

	
	/**
	 * DESC: 复制网讯模板PAGE信息
	 * @param base 网讯模板
	 * @param pageList 网讯页面
	 */
	public void copyWXUdeitorPage(LfWXBASEINFO baseInfo, List<LfWXPAGE> pageList) 
			throws Exception {
		
		//1. 检查数据有效性
		if(null == baseInfo || null == pageList || pageList.size() == 0){
			
			return ;
		}
		//Key = 被复制页面ID, value = 复制后页面ID
		Map<Long, Long> mapPageID = new HashMap<Long, Long>();
		List<Long> lstOrgPageID = new ArrayList<Long>();
		List<Long> lstCopyPageID = new ArrayList<Long>();
		LfWXPAGE page = null;
		//2. 将要复制的的页面保存数据库, 并记录复制页面的映射ID关系
		for (int i = 0; i < pageList.size(); i++)
		{
			page = pageList.get(i);
			lstOrgPageID.add(page.getID());
			Long lCopyPageID = super.addObjReturnId(page);
			lstCopyPageID.add(lCopyPageID);
			//映射ID关系
			mapPageID.put(page.getID(), lCopyPageID);
		}
		//3. 进行复制的页面ID替换,并更新到数据库
		for (int i = 0; i < lstCopyPageID.size(); i++)
		{
			page = pageList.get(i);
			Long lPageId = lstCopyPageID.get(i);	
			
//			String strPageContent = page.getCONTENT();
			String strPageContent =FileJsp.getContent( page.getID()+"");
			//保存到新的页面中取
			FileJsp.saveContent(lPageId+"",strPageContent);
			int iLinkSite = strPageContent.indexOf("javascript:mylink");
			int iLinkChind = strPageContent.indexOf("w=");
			
			if (iLinkSite == -1 || iLinkChind == -1 || iLinkChind <= iLinkSite) 
			{
				continue;
			}
			
			page.setID(lPageId);
			
			if(!"0".equals(page.getPARENTID().toString())) {
				
				page.setPARENTID(mapPageID.get(page.getPARENTID()));
			}
			//截取页面内容查找需要替换ID值
			for (int j = 0; j < lstOrgPageID.size(); j++)
			{
				Long lOrgPageId = lstOrgPageID.get(j);
				strPageContent = strPageContent.replaceAll("w=" + lOrgPageId, 
						"w=" + mapPageID.get(lOrgPageId));
			}
			//保存到新的页面中取
			FileJsp.saveContent(page.getID()+"",strPageContent);
			
			page.setCONTENT("");
			// 更新复制的页面
			super.updateObj(page);
		}
	}
	
	
}
