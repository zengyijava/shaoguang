package com.montnets.emp.shorturl.report.biz;

import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.shorturl.report.dao.GenericVisitDetailReportDAO;
import com.montnets.emp.shorturl.report.vo.LinkReportQueryVo;
import com.montnets.emp.shorturl.report.vo.VisitDetailVo;
import com.montnets.emp.shorturl.report.vo.VisitReportVo;
import com.montnets.emp.util.PageInfo;

public class Surl_VisitDetailReportBiz {

	/**
	 * 获取访问明细报表
	 * 
	 * @param queryVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<VisitReportVo> getVisitReport(LinkReportQueryVo queryVo, PageInfo pageInfo) throws Exception {
		List<VisitReportVo> returnList;
		try {
			GenericVisitDetailReportDAO dao = new GenericVisitDetailReportDAO();
			returnList = dao.getVisitReport(queryVo, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取访问明细报表异常");
			throw e;
		}
		return returnList;
	}

	/**
	 * 根据任务id,手机号,发送年月查询访问明细
	 * 
	 * @param phone 手机号
	 * @param taskId 任务id
	 * @param yearMonth 短信发送年月
	 * @param pageInfo 分页信息
	 * @return
	 */
	public List<VisitDetailVo> getVisitDetail(String phone, String taskId, String yearMonth, PageInfo pageInfo) {

		GenericVisitDetailReportDAO dao = new GenericVisitDetailReportDAO();
		List<VisitDetailVo> returnList = null;
		try {
			returnList = dao.getVisitDetail(phone, taskId, yearMonth, pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据任务id,手机号,发送年月查询访问明细异常");
		}
		return returnList;

	}
}
