package com.montnets.emp.shorturl.report.biz;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.shorturl.report.dao.GenericLinkVisitReportDAO;
import com.montnets.emp.shorturl.report.vo.LinkDetailVo;
import com.montnets.emp.shorturl.report.vo.LinkReportQueryVo;
import com.montnets.emp.shorturl.report.vo.LinkReportVo;
import com.montnets.emp.util.PageInfo;

public class Surl_LinkVisitReportBiz {

	/**
	 * 获取链接访问统计报表
	 * @param queryVo 链接报表查询对象
	 * @param pageInfo 分页对象
	 * @return 链接访问统计报表
	 * @throws Exception
	 */
	public List<LinkReportVo> getLinkReport(LinkReportQueryVo queryVo, PageInfo pageInfo) throws Exception {

		List<LinkReportVo> returnList;
		try {
			GenericLinkVisitReportDAO dao = new GenericLinkVisitReportDAO();
			// 获取长链接列表
			if (pageInfo == null) {
				returnList = dao.getLinkReport(queryVo);
			} else {
				returnList = dao.getLinkReport(queryVo, pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "链接访问报表查询异常！");
			throw e;
		}
		return returnList;
	}

	
	/**
	 * 获取链接访问详情
	 * @param queryVo
	 * @param pageInfo
	 * @return
	 */
	public List<LinkDetailVo> getLinkDetail(LinkReportQueryVo queryVo, PageInfo pageInfo){
		List<LinkDetailVo> returnList = new ArrayList<LinkDetailVo>();
		GenericLinkVisitReportDAO dao = new GenericLinkVisitReportDAO();
		try {
			
			if (pageInfo == null) {
				returnList = dao.getLinkDetail(queryVo);
			} else {
				returnList = dao.getLinkDetail(queryVo, pageInfo);
			}
		} catch (Exception e) {EmpExecutionContext.error(e, "获取链接访问详情异常");}
		return returnList;
	}
}
