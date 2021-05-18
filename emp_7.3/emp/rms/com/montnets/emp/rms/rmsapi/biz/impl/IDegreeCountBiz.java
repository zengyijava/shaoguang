package com.montnets.emp.rms.rmsapi.biz.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.montnets.emp.rms.degree.biz.DegreeBiz;
import com.montnets.emp.rms.degree.vo.LfDegreeManageVo;
import com.montnets.emp.rms.rmsapi.biz.DegreeCountBiz;

public class IDegreeCountBiz implements DegreeCountBiz
{
	/**
	 * 档位计算
	 *@anthor zengy2
	 *@param kbSize 文件大小，单位：kb
	 *@return  返回计算档位
	 *@throws Exception
	 */
	public String countDegree(int kbSize) throws Exception
	{
		//返回档位
		String countDegree = "";
		//容量开始值和容量结束值
		int degreeBegin = -1,degreeEnd = -1;
		
		//获得查询结果的集合
		Map<String, String> map = queryDegree();
		
		if(map != null && !map.isEmpty()) {
			Set<String> set = map.keySet();
			
			Iterator<String> it = set.iterator();
			//遍历查询结果
			while(it.hasNext()){
				//map的key值
				String key = it.next();
				//map的value值
				String value = map.get(key);
				//将得到的value值进行拆分
				String degreeStr[] = value.split("-");
				
				//将容量开始值的字符串转化成数字
				if(degreeStr[0] != null && !"".equals(degreeStr[0].trim())) {
					degreeBegin = Integer.parseInt(degreeStr[0]);
				}
				//将容量结束值的字符串转化成数字
				if(degreeStr[1] != null && !"".equals(degreeStr[1].trim())) {
					degreeEnd = Integer.parseInt(degreeStr[1]);
				}
				
				//对kbSize与容量开始结束值进行比较
				if(degreeBegin < kbSize && kbSize <= degreeEnd && kbSize > 0) {
					countDegree = key;
					break;
				}
			}
		}
		
		return countDegree;
	}

	/**
	 * 有效档位查询
	 *@anthor zengy2
	 *@return map[{'1':'0-50'},{'2':'51-100'}...]
	 *@throws Exception
	 */
	public Map<String, String> queryDegree() throws Exception
	{
		//map
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		LfDegreeManageVo lfDegreeManageVo = new LfDegreeManageVo();
		//状态设置为已启用
		lfDegreeManageVo.setStatus(0);
		DegreeBiz degreeBiz = new DegreeBiz();
		LfDegreeManageVo vo = new LfDegreeManageVo();
		//拿到所有信息的集合
		List<LfDegreeManageVo> list = degreeBiz.getDegreeBiz(lfDegreeManageVo, "asc");
		//遍历
		if(list != null && list.size()>0){			
			for(int i=0; i<list.size(); i++) {
				vo = list.get(i);
				//将档位和容量开始，结束存入map中
				map.put(vo.getDegree().toString(), vo.getDegreeBegin()+"-"+vo.getDegreeEnd());
			}
		}
		return map;
	}

	
}
