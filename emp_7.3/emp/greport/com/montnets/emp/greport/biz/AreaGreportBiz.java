package com.montnets.emp.greport.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.greport.dao.GenericAreaGreportDAO;
import com.montnets.emp.greport.dao.GreportDAO;
import com.montnets.emp.greport.vo.AreaGreportVo;

/**
 * 区域发送对比图形报表
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-19 下午03:56:49
 * @description
 */
public class AreaGreportBiz{
	/**
	 * 区域发送对比报表
	 * @param areaGreportvo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAreaGreportByVo(AreaGreportVo areaGreportvo) throws Exception {
		//区域发送对比报表集合
		List<DynaBean> areaGreportVos=new ArrayList<DynaBean>();
		try {
				areaGreportVos = new GenericAreaGreportDAO().findAreaReportsByVo(areaGreportvo);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"区域图形报表查询数据失败");
		}
		return areaGreportVos;
	}

	
	/**
	 * 获取图形报表的年份
	 * @param mstype
	 * @return
	 */
	public List<String> getYears(Integer mstype){
		List<String> years =new ArrayList<String>();
		try {
			years= new GreportDAO().findYears(mstype);

		} catch (Exception e) {
			EmpExecutionContext.error(e,"区域图形报表查询年数据失败");
		}
		return years;
	}

	
	/**
	 * 获取各省份颜色
	 * @return
	 */
	public Map<String,String> getColorMap(){
		Map<String, String> mapstr=new HashMap<String, String>();
		mapstr.put("安徽","#551588");
		mapstr.put("北京","#cf3030");
		mapstr.put("福建","#dfb067");
		mapstr.put("甘肃","#2240cf");
		mapstr.put("广东","#ff7171");
		mapstr.put("广西","#ff95d3");
		mapstr.put("贵州","#1c82fd");
		mapstr.put("海南","#51ef32");
		mapstr.put("河北","#57e1ff");
		mapstr.put("河南","#1f9769");
		mapstr.put("黑龙江","#7fb2ff");
		mapstr.put("湖北","#004b8a");
		mapstr.put("湖南","#94df2c");
		mapstr.put("吉林","#ffad77");
		mapstr.put("江苏","#ff008a");
		mapstr.put("江西","#ecfe31");
		mapstr.put("辽宁","#cd55ff");
		mapstr.put("内蒙古","#fffe4c");
		mapstr.put("宁夏","#99cdfc");
		mapstr.put("青海","#3356b9");
		mapstr.put("山东","#fed83d");
		mapstr.put("山西","#99cdfc");
		mapstr.put("陕西","#3356b9");
		mapstr.put("上海","#fed83d");
		mapstr.put("四川","#d2ff8a");
		mapstr.put("台湾","#a63434");
		mapstr.put("天津","#fd9c31");
		mapstr.put("西藏","#13cb00");
		mapstr.put("新疆","#fe491a");
		mapstr.put("云南","#757f00");
		mapstr.put("浙江","#ff2275");
		mapstr.put("重庆","#a4167a");
		mapstr.put("未知","#4973A8");
		return mapstr;
		
	}
	public List<String> getProvince(){
		List<String> provinceList=new ArrayList<String>();
		provinceList.add("北京");
		provinceList.add("天津");
		provinceList.add("上海");
		provinceList.add("重庆");
		provinceList.add("河北");
		provinceList.add("河南");
		provinceList.add("云南");
		provinceList.add("辽宁");
		provinceList.add("黑龙江");
		provinceList.add("湖南");
		provinceList.add("安徽");
		provinceList.add("山东");
		provinceList.add("新疆");
		provinceList.add("江苏");
		provinceList.add("浙江");
		provinceList.add("江西");
		provinceList.add("湖北");
		provinceList.add("广西");
		provinceList.add("甘肃");
		provinceList.add("山西");
		provinceList.add("内蒙古");
		provinceList.add("陕西");
		provinceList.add("吉林");
		provinceList.add("福建");
		provinceList.add("贵州"); 
		provinceList.add("广东"); 
		provinceList.add("青海"); 
		provinceList.add("西藏"); 
		provinceList.add("四川"); 
		provinceList.add("宁夏"); 
		provinceList.add("海南"); 
		provinceList.add("台湾"); 
		provinceList.add("香港"); 
		provinceList.add("澳门"); 
		provinceList.add("南海诸岛"); 
		return provinceList;
	}
	
	
}
