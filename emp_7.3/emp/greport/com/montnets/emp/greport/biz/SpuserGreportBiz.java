package com.montnets.emp.greport.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.greport.dao.GenericSpuserGreportDAO;
import com.montnets.emp.greport.dao.GreportDAO;
import com.montnets.emp.greport.vo.SpuserGreportVo;

/**
 *  Sp账号对比图形报表
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-20 下午04:44:44
 * @description
 */
public class SpuserGreportBiz{
	/**
	 *  Sp账号对比报表
	 * @param spuserGreportvo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getSpuserGreportByVo(SpuserGreportVo spuserGreportvo) throws Exception {
		//Sp账号对比报表集合
		List<DynaBean> spuserGreportVos=new ArrayList<DynaBean>();
		try {
				spuserGreportVos = new GenericSpuserGreportDAO().findSpuserGreportsByVo(spuserGreportvo);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"Sp账号图形报表查询数据失败");
		}
		return spuserGreportVos;
	}

	
	
	/**
	 * sp账号图形报表获取年份
	 * @param mstype
	 * @return
	 */
	public List<String> getYears(Integer mstype){
		List<String> years =new ArrayList<String>();
		try {
			years= new GreportDAO().findYears(mstype);

		} catch (Exception e) {
			EmpExecutionContext.error(e,"Sp账号图形报表查询年数据失败");
		}
		return years;
	}


	
}
