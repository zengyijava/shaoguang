package com.montnets.emp.areaphno.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.areaphno.dao.AreaPhNoDao;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.MobileArea;
import com.montnets.emp.servmodule.txgl.entity.ProvinceCity;
import com.montnets.emp.servmodule.txgl.vo.AreaPhNoVo;
import com.montnets.emp.util.PageInfo;

public class AreaPhNoBiz extends SuperBiz {
	  private SuperDAO superDao;
	  public AreaPhNoBiz(){
		  superDao = new SuperDAO();
	  }

	public List<AreaPhNoVo> getAreaPhNoList(
			LinkedHashMap<String, String> conditionMap, PageInfo pageinfo)
			throws Exception {
		return new AreaPhNoDao().getAreaPhNoList(conditionMap, pageinfo);
	}
	
	  /**
	   * 添加运营商号段
	   * @param moTructVo
	   * @return
	   * @throws Exception
	   */
	  public boolean addAreaPhNo(MobileArea mobileArea)throws Exception{
		  boolean issuccess = false;
			try
			{	
				issuccess=empDao.save(mobileArea);
			} catch (Exception e)
			{
				EmpExecutionContext.error(new ErrorLoger().getErrorLog(e,"添加号段区域省份区域代码biz层异常！"));
				throw e;
			}
		
			return issuccess;
	  }
	  
	  /**
	   * 根据城市查找区域记录
	   * @param city
	   * @return
	   * @throws Exception
	   */
	  public List<ProvinceCity> findAreaCodeByCity(String city)throws Exception{
		  List<ProvinceCity> codeList = null;
		  if(!"".equals(city)){
			  String sql="select * from a_provincecity where  city='"+city+"'";
			  codeList  = superDao.findEntityListBySQL(ProvinceCity.class, sql, StaticValue.EMP_POOLNAME);
			
		  }
		  return codeList;
	  }
	  
	  /**
	   * 查找所有省份城市信息
	   * 
	   * @return
	   * @throws Exception
	   */
	  public List<ProvinceCity> findProvinceAndCity()throws Exception{
		  List<ProvinceCity> provinceCity = null;
		  try{
			  String sql="select * from a_provincecity";
			  provinceCity  = superDao.findEntityListBySQL(ProvinceCity.class, sql, StaticValue.EMP_POOLNAME);
		  }catch (Exception e) {
			  EmpExecutionContext.error(new ErrorLoger().getErrorLog(e,"查找所有省份城市信息异常！"));
				throw e;
		  }
		return provinceCity;
	  }
	  /**
	   * 查找数据库中所有区域号段信息
	   * @return
	   * @throws Exception
	   */
	  public List<MobileArea> findMobile()throws Exception{
		  List<MobileArea> mobileCity = null;
		  try{
			  String sql="select * from a_mobilearea";
			  mobileCity  = superDao.findEntityListBySQL(MobileArea.class, sql, StaticValue.EMP_POOLNAME);
			  
		  }catch (Exception e) {
			  EmpExecutionContext.error(new ErrorLoger().getErrorLog(e,"查找所有省份城市信息异常！"));
			  throw e;
		  }
		return mobileCity;
	  }
	  
	  /**
	   * 查找号段是否存在
	   * @return
	   * @throws Exception
	   */
	  public boolean mobileIsExit(String mobile)throws Exception{
		  List<MobileArea> mobileCity = null;
		  boolean result = false;
		  try{
			  String sql="select * from a_mobilearea where mobile="+mobile;
			  mobileCity  = superDao.findPartEntityListBySQL(MobileArea.class, sql, StaticValue.EMP_POOLNAME);
			  if(mobileCity!=null && mobileCity.size()>0){
				  //号段存在
				  result = true;
			  }
			  
		  }catch (Exception e) {
			  EmpExecutionContext.error(new ErrorLoger().getErrorLog(e,"查找所有省份城市信息异常！"));
			  throw e;
		  }
		return result;
	  }
	  /**
	   * 返回存储在数据库中的所有号段的List
	   * @return
	   * @throws Exception
	   */
//	  public List<String> mobileList() throws Exception{
//		  List<MobileArea> mobileL = null;
//		  String mobile=null;
//		  List<String> mobileList=null;
//		  try{
//			  mobileList=new ArrayList<String>();
//			  mobileL=this.findMobile();
//			  for(int i=0;i<mobileL.size();i++){
//				  mobile=mobileL.get(i).getMobile();
//				  mobileList.add(mobile);
//			  }
//		  }catch (Exception e) {
//			  EmpExecutionContext.error(new ErrorLoger().getErrorLog(e,"查找所有省份城市信息异常！"));
//			  throw e;
//		}
//		  return mobileList;
//	  }
	  /**
	   * 返回数据库中不重复的省份信息
	   * @return
	   * @throws Exception
	   */
	  public List <String> provinceList() throws Exception{
		  List<String> provinceL = new ArrayList<String>();
		  List<ProvinceCity> provinceCity = null;
		  String province=null;
		  try{
			  provinceCity=this.findProvinceAndCity();
			  for(int i=0;i<provinceCity.size();i++){
				province=provinceCity.get(i).getProvince();
				if(!provinceL.contains(province)){
					provinceL.add(province);
				}
			  }
		  }catch (Exception e) {
			  EmpExecutionContext.error(new ErrorLoger().getErrorLog(e,"查找所有省份城市信息异常！"));
			  throw e;
		  }
		return provinceL;
		  
	  }
	  /**
	   * 
	   * @return 返回键为省份，值为城市的list的map
	   * @throws Exception
	   */
	  public Map<String,List<String>> provinceCityMap() throws Exception{
		  Map<String,List<String>> map=new HashMap<String, List<String>>();
		  List<ProvinceCity> provinceCity = null;
		  String province=null;
		  String city=null;
		  List<String> cityList=null;
		  try{
			  provinceCity=this.findProvinceAndCity();
			  for(int i=0;i<provinceCity.size();i++){
				  province=provinceCity.get(i).getProvince();
				  city=provinceCity.get(i).getCity();
				  cityList=map.get(province);
				  if(cityList==null){
					  cityList=new ArrayList<String>();
					  cityList.add(city);
					  map.put(province, cityList);
				  }else{
					  cityList.add(city);
				  }
			  }
		  }catch (Exception e) {
			  throw e;
		}
		  return map;
	  }
	
	  /**
	   * 根据ID删除一条区域号段信息
	   * @return
	   * @throws Exception 
	   */
	  public int delete(String id) throws Exception{
		  
		  if(id==null||id.equals("")){
			  return 0;
		  }
		  int result =  empDao.delete(MobileArea.class, id);

		  return result;
	  }

}
