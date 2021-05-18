package com.montnets.emp.shorturl.report.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.shorturl.comm.constant.UrlGlobals;
import com.montnets.emp.shorturl.report.constant.ShortUrlConstant;
import com.montnets.emp.shorturl.report.entity.VstDetail;
import com.montnets.emp.shorturl.report.util.DBConnectionUtil;

/**
 * 企业短链报表同步
 * 
 * @author litian
 * 
 */
public class SurlTransferSvt extends BaseServlet {
	final Map<String, Date> map = new HashMap<String, Date>();
	/**
	 * 
	 */
	private static final long serialVersionUID = -6181914776154782634L;

	private static boolean RUNTIMEJOB_isShutdown = false;
	
	public void shutdown_RUNTIMEJOB(){
		this.RUNTIMEJOB_isShutdown = true;
	}
	
	@Override
	public void init() throws ServletException {
		
		if(StaticValue.ISRUNTIMEJOB==1){
			/**
			 * 开启线程，执行同步报表操作
			 */
			new Thread(new Runnable() {
				public void run() {
					while (!RUNTIMEJOB_isShutdown) {
						try {
							if (map.size() > 5000) {
								map.clear();
							}
							exec();
							try {
								Thread.sleep(ShortUrlConstant.getDbTransferTime());
							} catch (InterruptedException e) {
								EmpExecutionContext.error(e, "企业短链报表同步线程休眠异常！");
								Thread.currentThread().interrupt();
							}
						} catch (Exception e) {
							EmpExecutionContext.error("企业短链报表同步线程运行异常！");
						}
					}
				}
			}).start();
			
			EmpExecutionContext.error("企业短链报表同步线程启动成功！");
		}else{
			EmpExecutionContext.error("企业短链报表同步线程未启用配置！");
		}

	}

	/**
	 * 执行同步报表操作
	 */
	private void exec() {
		String srcpt = UrlGlobals.getEMP_NUM();
		if (null == srcpt) {
			srcpt = "0";
		}

		try {
			long start = System.currentTimeMillis();
			long period = 0;
			List<VstDetail> list = getVisitDetail(srcpt);
			while (list != null && !list.isEmpty() && (period < ShortUrlConstant.getDbStopTime())) {
				doTransfer(list);
				list = getVisitDetail(srcpt);
				period = System.currentTimeMillis() - start;
			}
		} catch (SQLException e) {
			EmpExecutionContext.error(e, "企业短链报表同步线程循环执行异常！");
		}

	}

	/**
	 * 同步查询出的访问记录
	 * @param list
	 * @throws SQLException
	 */
	private void doTransfer(List<VstDetail> list) throws SQLException {

		// 获取短链对应的任务的发送时间，用于确定访问记录存储到EMP哪张表
		getSendTime(list);

		if (!list.isEmpty()) {
			Map<String, List<VstDetail>> toSave = new HashMap<String, List<VstDetail>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

			for (VstDetail vstDetail : list) {
				String visitTable = "VST_DETAIL" + sdf.format((Date) map.get(vstDetail.getCutid()));
				if (toSave.get(visitTable) == null) {
					toSave.put(visitTable, new ArrayList<VstDetail>());
				}
				toSave.get(visitTable).add(vstDetail);
			}

			for (Map.Entry<String, List<VstDetail>> entry : toSave.entrySet()) {
				String sids = saveVisitRecord(entry.getKey(), entry.getValue());
				if (!"".equals(sids)) {
					try{
						deleteTransferRecord(sids);
					}catch(SQLException e){
						EmpExecutionContext.error(e,"deleteTransferRecord异常");
					}
				}
			}

		}

	}

	private void getSendTime(List<VstDetail> list) {
		if (!list.isEmpty()) {
			for (VstDetail vstDetail : list) {
				if (map.get(vstDetail.getCutid()) == null) {
					findSendTime(vstDetail.getCutid());
				}
			}
		}
	}

	/**
	 * 取出一定数量的访问记录
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<VstDetail> getVisitDetail(String srcpt) throws SQLException {
		List<VstDetail> list = new ArrayList<VstDetail>();

		String sql = "select top " + ShortUrlConstant.getDbTransferPerData() + " * from VST_DETAIL where SRCPT = '" + srcpt + "'";
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnectionUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			// 获取平台历史库的部分访问记录
			while (rs.next()) {
				VstDetail vstDetail = new VstDetail();
				vstDetail.setPtmsgid(rs.getLong("PTMSGID"));
				vstDetail.setVstmsgid(rs.getLong("VSTMSGID"));
				vstDetail.setUserid(rs.getString("USERID"));
				vstDetail.setEcid(rs.getInt("ECID"));
				vstDetail.setPhone(rs.getString("PHONE"));
				vstDetail.setMobilearea(rs.getInt("MOBILEAREA"));
				vstDetail.setCttm(rs.getTimestamp("CTTM"));
				vstDetail.setVsttm(rs.getTimestamp("VSTTM"));
				vstDetail.setPtcode(rs.getString("PTCODE"));
				vstDetail.setWgno(rs.getInt("WGNO"));
				vstDetail.setSrcaddress(rs.getString("SRCADDRESS"));
				vstDetail.setXwapprof(rs.getString("XWAPPROF"));
				vstDetail.setXbrotype(rs.getString("XBROTYPE"));
				vstDetail.setDrs(rs.getString("DRS"));
				vstDetail.setTimes(rs.getLong("TIMES"));
				vstDetail.setCutid(rs.getString("CUSTID"));
				vstDetail.setSurl(rs.getString("SURL"));
				vstDetail.setLurl(rs.getString("LURL"));
				vstDetail.setHttpHeader(rs.getString("HTTPHEADER"));
				vstDetail.setExdata(rs.getString("EXDATA"));
				vstDetail.setSrcpt(rs.getString("SRCPT"));
				vstDetail.setSid(rs.getLong("ID"));
				list.add(vstDetail);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "企业短链报表同步-查询访问记录异常！");
		} finally {
			DBConnectionUtil.close(rs, ps, conn);
		}
		return list;
	}

	/**
	 * 删除已保存的访问记录
	 * 
	 * @param id
	 * @throws SQLException
	 */
	private void deleteTransferRecord(String ids) throws SQLException {
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			String deleteSql = "delete from VST_DETAIL where id in (" + ids.substring(0, ids.length() - 1) + ")";
			conn = DBConnectionUtil.getConnection();
			ps = conn.prepareStatement(deleteSql);
			ps.executeUpdate();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "企业短链报表同步-删除访问记录异常！记录id:" + ids);
		} finally {
			DBConnectionUtil.close(null, ps, conn);
		}
	}

	/**
	 * 保存访问记录
	 * 
	 * @param visitTable
	 * @param vstDetail
	 * @return
	 */
	private String saveVisitRecord(String visitTable, List<VstDetail> vstDetails) {
		String sids = "";
		String sql = "insert into " + visitTable
				+ "(PTMSGID,VSTMSGID,USERID,ECID,PHONE,MOBILEAREA,CTTM,VSTTM,PTCODE,WGNO,SRCADDRESS,XWAPPROF,XBROTYPE,DRS,TIMES,CUSTID,SURL,LURL,HTTPHEADER,EXDATA,SRCPT,SID)";
		sql = sql + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = new ConnectionManagerImp().getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			for (VstDetail vstDetail : vstDetails) {

				ps.setObject(1, vstDetail.getPtmsgid());
				ps.setObject(2, vstDetail.getVstmsgid());
				ps.setObject(3, vstDetail.getUserid());
				ps.setObject(4, vstDetail.getEcid());
				ps.setObject(5, vstDetail.getPhone());
				ps.setObject(6, vstDetail.getMobilearea());
				ps.setObject(7, vstDetail.getCttm());
				ps.setObject(8, vstDetail.getVsttm());
				ps.setObject(9, vstDetail.getPtcode());
				ps.setObject(10, vstDetail.getWgno());
				ps.setObject(11, vstDetail.getSrcaddress());
				ps.setObject(12, vstDetail.getXwapprof());
				ps.setObject(13, vstDetail.getXbrotype());
				ps.setObject(14, vstDetail.getDrs());
				ps.setObject(15, vstDetail.getTimes());
				ps.setObject(16, vstDetail.getCutid());
				ps.setObject(17, vstDetail.getSurl());
				ps.setObject(18, vstDetail.getLurl());
				ps.setObject(19, vstDetail.getHttpHeader());
				ps.setObject(20, vstDetail.getExdata());
				ps.setObject(21, vstDetail.getSrcpt());
				ps.setObject(22, vstDetail.getSid());
				ps.addBatch();
			}
			int[] is = ps.executeBatch();
			if (is.length > 0) {
				for (int j = 0; j <= is.length - 1; j++) {
					if (is[j] == 1) {
						sids += (vstDetails.get(j).getSid() + ",");
					} else {
						EmpExecutionContext.error("企业短链报表同步-批量保存访问记录失败！");
					}
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "企业短链报表同步-保存访问记录异常！");
		} finally {
			DBConnectionUtil.close(null, ps, conn);
		}
		return sids;
	}

	/**
	 * 根据任务id查询发送时间，用于判断访问记录插入哪个历史表
	 * 
	 * @param map
	 * @param taskId
	 */
	private void findSendTime(String taskId) {
		try {
			String querySql = "select TASKID,PLAN_TIME as sendTime from LF_URLTASK where TASKID = " + taskId;
			List<DynaBean> list = new SuperDAO().getListDynaBeanBySql(querySql);
			if (list != null && !list.isEmpty()) {
				Date sendTime = new Date(Timestamp.valueOf(list.get(0).get("sendtime") + "").getTime());
				map.put(taskId, sendTime);
			} else {
				EmpExecutionContext.error("短链访问记录转移失败，任务id为：" + taskId + "的访问记录找不到对应的访问任务！");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "企业短链报表同步-根据任务id查询发送时间异常！");
		}
	}
}
