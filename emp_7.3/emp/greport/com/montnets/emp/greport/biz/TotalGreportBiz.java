package com.montnets.emp.greport.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.greport.dao.GenericTotalGreportDAO;
import com.montnets.emp.greport.dao.GreportDAO;
import com.montnets.emp.greport.vo.TotalGreportVo;

/**
 * 总体发送趋势图形报表
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-19 上午10:35:38
 * @description
 */
public class TotalGreportBiz{
	/**
	 * 总体发送趋势报表
	 * @param totalGreportvo
	 * @return
	 * @throws Exception
	 */
	public Map<String,DynaBean> getTotalGreportByVo(TotalGreportVo totalGreportvo) throws Exception {
		//总体发送趋势报表集合
		Map<String,DynaBean> busReportVos=new HashMap<String, DynaBean>();

		try {
				List<DynaBean> busReportVolist = new GenericTotalGreportDAO().findTotalReportsByVo(totalGreportvo);
				if(busReportVolist!=null){
					for(DynaBean grvo:busReportVolist){
						if(totalGreportvo.getReporttype()==0){
								String imonth="";
								int month=Integer.parseInt(grvo.get("imonth")==null?"0":grvo.get("imonth").toString());
								String y=grvo.get("y")==null?"0":grvo.get("y").toString();
								if(month<10){
									imonth=y+"0"+month;
								}else{
									imonth=y+month;
								}
								busReportVos.put(imonth, grvo);
						}else if(totalGreportvo.getReporttype()==1){
							busReportVos.put(grvo.get("iymd")==null?"0":grvo.get("iymd").toString(), grvo);
						}
					}
				}
				

		} catch (Exception e) {
			EmpExecutionContext.error(e,"总趋势图形报表查询数据失败");
		}
		return busReportVos;
	}

	
	/**
	 * 总趋势图形报表获取年份
	 * @param mstype
	 * @return
	 */
	public List<String> getYears(Integer mstype){
		List<String> years =new ArrayList<String>();
		try {
			years= new GreportDAO().findYears(mstype);

		} catch (Exception e) {
			EmpExecutionContext.error(e,"总趋势图形报表查询年数失败");
		}
		return years;
	}

	
	
}
