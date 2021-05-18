package com.montnets.emp.netnews.daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXData;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.netnews.entity.LfWXSORT;
import com.montnets.emp.netnews.entity.LfWXTrustCols;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxmanger.dao.WxManagerDao;

public class Wx_ueditorDaoImpl extends BaseBiz {

	private IEmpTransactionDAO transactionDAO = new DataAccessDriver()
			.getEmpTransDAO();
	
	private WxManagerDao managerDao = new WxManagerDao();

	/**
	 * 
	 * @param sql
	 *            插入表的sql
	 * @param tablename
	 *            表名
	 * @param h
	 *            手机号
	 * @return 保存方式 0：多次回复有效、1：首次回复有效、2：末次回复有效
	 */
	public String getsaveSql_mode(String sql, String tablename, String h) {
		String mode = null;
		Connection conn = transactionDAO.getConnection();
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall("{call PROC_WX_getsaveSql_mode(?,?,?,?)}");

			cstmt.setString(1, sql);
			cstmt.setString(2, tablename);
			cstmt.setString(3, h);
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			cstmt.execute();
			mode = cstmt.getString(4);

		} catch (SQLException e) {
			EmpExecutionContext.error(e, "查询存储过程出错！");
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				EmpExecutionContext.error(e, "数据库关闭异常！");
			}
		}
		return mode;
	}

	/**
	 * 访问扣费写日志表
	 * 
	 * @param tablename
	 *            表名
	 * @return
	 */
	public int getsaveVisi(String sql) {
		Connection conn = transactionDAO.getConnection();
		CallableStatement cstmt = null;
		int i = -1;
		try {
			cstmt = conn.prepareCall("{call PROC_WX_getsaveVisi(?)}");

			cstmt.setString(1, sql);

			cstmt.execute();

			i = 1;
			return 1;
		} catch (SQLException e) {
			EmpExecutionContext.error(e, "插入sql出错:" + sql);
			i = -1;
			return -1;
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				EmpExecutionContext.error(e, "数据关闭异常！");
			}
		}
	}

	/**
	 * 业务数据表名查询业务数据结构表
	 * 
	 * @param tablename
	 *            表名
	 * @return
	 */
	public int getsaveInteraction_cols(String sql) {
		Connection conn = transactionDAO.getConnection();
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall("{call proc_wx_getsave_cols(?)}");

			cstmt.setString(1, sql);

			cstmt.execute();

			return 1;
		} catch (SQLException e) {
			EmpExecutionContext.error(e, "查询异常！");
			return -1;
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				EmpExecutionContext.error(e, "数据库关闭异常！");
			}
		}
	}

	/**
	 * 业务数据表名查询业务数据结构表
	 * 
	 * @param TRUSTID
	 *            表名
	 * @return
	 */
	public List<LfWXTrustCols> getInteraction_cols(String TRUSTID) {
		List<LfWXTrustCols> list = new ArrayList<LfWXTrustCols>();
		try {
			LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
			conditionMap.put("trustId", TRUSTID);
			conditionMap.put("isParam", "1");
			list=getByCondition(LfWXTrustCols.class, conditionMap, null);
			return list;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询业务数据结构表异常！");
			return null;
		} 
	}


	/**
	 * 根据任务ID 修改状态
	 * 
	 * @param taskId
	 * @param sTATUS
	 */
	public void getupdateSendMsgStatus(String taskId, int sTATUS) {
		Connection conn = transactionDAO.getConnection();
		CallableStatement cstmt = null;
		try {

			cstmt = conn.prepareCall("{call PROC_WX_updateSendMsgStatus(?,?)}");

			cstmt.setString(1, taskId);
			cstmt.setInt(2, sTATUS);

			cstmt.execute();

		} catch (SQLException e) {
			EmpExecutionContext.error(e, "查询PROC_WX_updateSendMsgStatus异常！");
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				EmpExecutionContext.error(e, "关闭数据库异常！");
			}
		}
	}

	/**
	 * 页面提交update网讯信息
	 * 
	 * @param ueditor
	 *            编辑器对象，
	 * @return
	 */
	public int getupdateByUeditor(LfWXBASEINFO baseInfo, LfWXPAGE pageTemp,
			List<LfWXPAGE> list) throws Exception {
		boolean boo = false;
		// 内容是否存在子页面链接
		Map<String, Long> map = null;
		List<String> temp = null;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				LfWXPAGE page = list.get(i);
				if (page.getCONTENT().indexOf("javascript:mylink") > -1
						&& page.getCONTENT().indexOf("#link#") > -1) {
					boo = true;
					map = new HashMap<String, Long>();
					temp = new ArrayList<String>();
					break;
				}
			}
		}
		try {
			updateObj(baseInfo);
			Long netid = baseInfo.getNETID();
			if (list != null && list.size() > 0) {

				Long iws = 0L;
				for (int i = 0; i < list.size(); i++) {
					LfWXPAGE page = list.get(i);
					if (page.getID() == -1) {
						page.setNETID(baseInfo.getNETID());
						page.setPARENTID(baseInfo.getID());
						page.setCREATID(baseInfo.getCREATID());
						iws = addObjReturnId(page);
					} else {
						page.setNETID(baseInfo.getNETID());
						page.setMODIFYID(baseInfo.getMODIFYID());
						updateObj(page);
						iws = page.getID();
					}

					if (boo) {
						map.put("#link#" + page.getLink() + "#link#", iws);
						temp.add("#link#" + page.getLink() + "#link#");
					}
				}
			}
			if (boo) {
				List<LfWXPAGE> temp1 = new ArrayList<LfWXPAGE>();
				for (int j = 0; j < list.size(); j++) {
					LfWXPAGE page = list.get(j);
					if (page.getID() == -1) {
						page.setID(map.get("#link#" + page.getLink() + "#link#"));
					}
					for (int i = 0; i < temp.size(); i++) {
						if (page.getCONTENT().indexOf(temp.get(i).toString()) > -1) {
							page
									.setCONTENT(page.getCONTENT().replaceAll(
											temp.get(i).toString(),
											map.get(temp.get(i).toString())
													.toString()));
						}
					}
					temp1.add(page);
				}

				getupdateByUeditor(baseInfo, pageTemp, temp1);
			}
			return 1;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "修改网讯失败！");
			return -1;
		}
	}

	/**
	 * 创建网讯
	 * 
	 * @param baseInfo
	 * @param page
	 * @return 返回网讯id
	 */
	public Long getSaveUdeitor(LfWXBASEINFO base, LfWXPAGE page,
			List<LfWXPAGE> pageList) {
		/**
		 * wx_name in varchar2, 网讯名称 ， creatId in NUMBER, 创建用户ID parenetid in
		 * NUMBER , 父结点 ， netid out number 输出 ：网讯id
		 */
		try {
			boolean boo = false;
			// 内容是否存在子页面链接
			if (page.getCONTENT().indexOf("javascript:mylink") > -1
					&& page.getCONTENT().indexOf("#link#") > -1) {
				boo = true;
			}
			Long baseId = addObjReturnId(base);
			base.setNETID(baseId);
			base.setID(baseId);
			updateObj(base);
			page.setCREATID(base.getCREATID());
			page.setNETID(baseId);
			Long pageid = addObjReturnId(page);

			// 内容是否存在子页面链接
			if (pageList != null && pageList.size() > 0) 
			{
				LfWXPAGE pageT = null;
				for (int i = 0; i < pageList.size(); i++) 
				{
					pageT = pageList.get(i);
					if (pageT.getCONTENT().indexOf("javascript:mylink") > -1 && pageT.getCONTENT().indexOf("#link#") > -1) {
						boo = true;
					}
				}
			}
			Map<String, Long> map = null;
			List<String> temp = null;
			if (boo) 
			{
				map = new HashMap<String, Long>();
				temp = new ArrayList<String>();
				map.put("#link#" + page.getLink() + "#link#", pageid);
				temp.add("#link#" + page.getLink() + "#link#");
			}

			if (pageList != null && pageList.size() > 0) 
			{
				Long id = 0L;
				LfWXPAGE pageTemp = null;
				for (int i = 0; i < pageList.size(); i++) {
					pageTemp = (LfWXPAGE) pageList.get(i);
					pageTemp.setNETID(baseId);// 关联网讯编号
					pageTemp.setPARENTID(pageid); // 页面父节点
					pageTemp.setCREATID(base.getCREATID());// 创建用户ID
					id = addObjReturnId(pageTemp);
					if (boo) {
						map.put("#link#" + pageTemp.getLink() + "#link#", id);
						temp.add("#link#" + pageTemp.getLink() + "#link#");
					}
				}
			}

			// 更新数据
			if (boo) {
				// 父结点用
				List<LfWXPAGE> templ = new ArrayList<LfWXPAGE>();
				for (int i = 0; i < temp.size(); i++) {
					if (page.getCONTENT().indexOf(temp.get(i).toString()) > -1) {
						page.setCONTENT(page.getCONTENT().replaceAll(temp.get(i).toString(),map.get(temp.get(i).toString()).toString()));
						page.setID(pageid);
						base.setNETID(baseId);
					}
				}
				LfWXPAGE page2 = null;
				for (int j = 0; j < pageList.size(); j++) {
					page2 = pageList.get(j);
					page2.setID(map.get("#link#" + page2.getLink() + "#link#"));
					for (int i = 0; i < temp.size(); i++) {
						if (page2.getCONTENT().indexOf(temp.get(i).toString()) > -1) {
							page2.setCONTENT(page2.getCONTENT().replaceAll(temp.get(i).toString(),map.get(temp.get(i).toString()).toString()));
						}
					}
					templ.add(page2);
				}
				templ.add(page2);

				getupdateByUeditor(base, page, templ);
			}

			return baseId;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "创建网讯失败！");
			return -1L;
		} 
	}

	/**
	 * 根据用户ID找出网讯列表，，树形结构
	 * 
	 * @param userid
	 * @return
	 */

	public LfWXBASEINFO getParentUdeitor(String netid, int userid) {
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("NETID", netid);
			//may 2013-9-3 显示全部模板，不是当前用户
			//conditionMap.put("CREATID", String.valueOf(userid));
			List<LfWXBASEINFO> infos = getByCondition(LfWXBASEINFO.class,
					conditionMap, null);
			LfWXBASEINFO baseInfo = null;
			if (infos != null && infos.size() > 0) {
				baseInfo = infos.get(0);
			}

			return baseInfo;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询Lf_WX_BASEINFO数据");
			return null;
		}
	}

	public List<DynaBean> getPages(String netid, String userid) {
		String sql = "select * from lf_wx_page where NETID = " + netid;
				//+ " and CREATID=" + userid;
		List<DynaBean> beans = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		return beans;
	}


	/*-------------------------------------------------YXD begin-----------------------*/
	public Map<Long, String> getPageImage(int section, int page) throws Exception 
	{
		LinkedHashMap<Long, String> map = new LinkedHashMap<Long, String>();
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("SORT", String.valueOf(section));
			conditionMap.put("wxTYPE", "1");
			PageInfo pageInfo = new PageInfo();
			pageInfo.setPageIndex(page);
			pageInfo.setPageSize(6);
			List<LfWXBASEINFO> infos=getByConditionNoCount(LfWXBASEINFO.class, null, conditionMap, null, pageInfo);
			for (LfWXBASEINFO info:infos) 
			{
				map.put(info.getID(),info.getIMAGE());
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询Lf_WX_BASEINFO数据");
			throw e;
		} 
		return map;
	}

	public String getTemplate(int sort, int id) throws Exception {
		String template = "";
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("NETID", String.valueOf(id));
			List<LfWXPAGE> pages = getByCondition(LfWXPAGE.class, conditionMap,
					null);
			if (pages != null && pages.size() > 0) {
				template = pages.get(0).getCONTENT();
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询LF_WX_PAGE数据");
			throw e;
		} finally {

		}
		return template;
	}

	public Map<Long, String> getTemplateSort() throws SQLException {
		Map<Long, String> map = new LinkedHashMap<Long, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			conditionMap.put("CREATID", "0");
			conditionMap.put("wxTYPE", "0");
			List<LfWXSORT> sorts = getByCondition(LfWXSORT.class, conditionMap, null);
			if (sorts != null && sorts.size() > 0) 
			{
				for (int i = 0; i < sorts.size(); i++) 
				{
					map.put(sorts.get(i).getID(), sorts.get(i).getNAME());
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询LF_WX_SORT数据");
		}
		return map;
	}

	public int getSectionSize(int section) {
		int totalImage = 0;
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("SORT", Integer.toString(section));
			List<LfWXBASEINFO> infos = getByCondition(LfWXBASEINFO.class,conditionMap, null);
			if (infos != null) {
				totalImage = infos.size();
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询LF_WX_BASEINFO数据");
		} finally {

		}
		return totalImage;
	}

	public Long getMinSection() {
		Long ret = 0L;
		try {
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("ID", "ASC");
			List<LfWXSORT> sorts = getByCondition(LfWXSORT.class, null,
					orderbyMap);
			if (sorts != null && sorts.size() > 0) {
				ret = sorts.get(0).getID();
			}

		} catch (Exception e) {
			EmpExecutionContext.error("取默认类别失败：" + e);
		} finally {

		}
		return ret;
	}
	
	/**
	 * 通过pageId获取网讯信息
	 * @param pageId
	 * @return
	 * @throws Exception
	 */
	public LfWXBASEINFO getNetInfoByPageId(String pageId){
		return managerDao.findNetInfoByPageId(pageId);
	}

	/**
	 * 通过netId获取网讯绑定的互动数据
	 * @param netId
	 * @return
	 * @throws Exception
	 */
	public List<LfWXData> getDataByNetId(String netId){
		return managerDao.findDataByNetId(netId);
	}

	/**
	 * 插入互动数据到表
	 * @param netId
	 * @return
	 * @throws Exception
	 */
	public boolean insertData(String netId, String pageId, String phone, Map<String,String[]> answerMap, String tableName, String taskId)
	{
		return managerDao.insertData(netId, pageId, phone, answerMap, tableName, taskId);
	}
	
	public Map<Integer,String> getDynDataInfo(String netId, String taskId, String phone, String tableName, String params)
	{
		return managerDao.getDynDataInfo(netId, taskId, phone, tableName, params);
	}
	
	/**
	 * 模板数据插入互动数据到表
	 * @param netId
	 * @return
	 * @throws Exception
	 */
	public boolean insertTempData(String netid, String taskId,String dtMsg,String fileContent)
	{
		return managerDao.insertTempData(netid, taskId, dtMsg, fileContent);
	}
}
