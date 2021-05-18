package com.montnets.emp.greport.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.greport.dao.GenericBusGreportDAO;
import com.montnets.emp.greport.dao.GreportDAO;
import com.montnets.emp.greport.vo.BusGreportVo;

/**
 * 业务类型发送对比图形报表
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-20 下午04:42:54
 * @description
 */
public class BusGreportBiz{
	/**
	 * 业务类型发送对比报表
	 * @param busGreportvo
	 * @return
	 * @throws Exception
	 */
	public Map<String,DynaBean> getBusGreportByVo(BusGreportVo busGreportvo) throws Exception {
		//业务类型发送对比报表集合
		Map<String,DynaBean> busReportVos=new HashMap<String, DynaBean>();

		try {
				List<DynaBean> busReportVolist = new GenericBusGreportDAO().findBusGreportsByVo(busGreportvo);
				if(busReportVolist!=null){
					for(DynaBean grvo:busReportVolist){
						String svrtype=grvo.get("svrtype")==null?"0":grvo.get("svrtype").toString();
						if(busGreportvo.getReporttype()==0){
								String imonth="";
								int month=Integer.parseInt(grvo.get("imonth")==null?"0":grvo.get("imonth").toString());
								if(month<10){
									imonth=svrtype+"0"+month;
								}else{
									imonth=svrtype+month;
								}
								busReportVos.put(imonth, grvo);
						}else if(busGreportvo.getReporttype()==1){
							String iymd=grvo.get("iymd")==null?"0":grvo.get("iymd").toString().substring(6);
							busReportVos.put(svrtype+iymd, grvo);
						}
					}
				}
				

		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务类型发送对比图形报表查询数据失败");
		}
		return busReportVos;
	}

	
	/**
	 * 业务类型统计报表获取年数据
	 * @param mstype
	 * @return
	 */
	public List<String> getYears(Integer mstype){
		List<String> years =new ArrayList<String>();
		try {
			years= new GreportDAO().findYears(mstype);

		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务类型发送对比图形报表查询年数失败");
		}
		return years;
	}

	
	
}
