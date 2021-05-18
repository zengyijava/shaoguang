package com.montnets.emp.common.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfPageField;

public class GlobConfigBiz
{
	//控件集合，key为控件id，value为控件对象
	private static Map<String,LfPageField> FieldsMAP = new HashMap<String,LfPageField>();
	//控件子项集合，key为控件id，value为该控件下的子项对象集合
	private static Map<String,List<LfPageField>> SubFieldsMap = new HashMap<String,List<LfPageField>>();
	
	private BaseBiz baseBiz = new BaseBiz();
	
	/**
	 * 获取显示的控件和控件下的子项
	 * @return
	 */
	public boolean getPageFieldList(){
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new  LinkedHashMap<String, String>();
			
			conditionMap.put("filedShow", "0");
			orderbyMap.put("fieldId", StaticValue.ASC);
			orderbyMap.put("sortValue", StaticValue.ASC);
			
			List<LfPageField> pageFieldsList= baseBiz.findListByCondition(LfPageField.class, conditionMap, orderbyMap);
			LfPageField pageField = null;
			
			for(int i=0;i<pageFieldsList.size();i++)
			{
				pageField = pageFieldsList.get(i);

				//是否为控件。0：是;1：否，即为控件子项
				if(pageField.getIsField()==0){
					FieldsMAP.put(pageField.getFieldId(), pageField);
				}else if(pageField.getIsField()==1){
					//添加到子项集合
					this.setSubFieldMap(pageField);
				}
				
			}
			return true;
		
		}catch(Exception e){
			EmpExecutionContext.error(e, "获取显示的控件和控件下的子项异常。");
			return false;
		}
	}
	
	/**
	 * 根据编号获取控件集合
	 * @param fieldId
	 * @return 返回空间集合，索引0为控件本身，从索引1开始是控件的子项，没有控件则返回null
	 */
	public List<LfPageField> getPageFieldById(String fieldId){
		try{
			if(FieldsMAP == null || FieldsMAP.size() == 0){
				this.getPageFieldList();
			}
			LfPageField pageField = FieldsMAP.get(fieldId);
			if(pageField == null){
				return null;
			}
			List<LfPageField> fieldsList = new ArrayList<LfPageField>();
			
			//第一位的是控件
			fieldsList.add(0, pageField);
			//没子项
			if(pageField.getSubField()!=0){
				return fieldsList;
			}
			
			if(SubFieldsMap == null || SubFieldsMap.size() == 0){
				this.getPageFieldList();
			}
			//子项集合
			List<LfPageField> subFieldsList = SubFieldsMap.get(fieldId);
			if(subFieldsList == null || subFieldsList.size() == 0){
				return fieldsList;
			}
			fieldsList.addAll(1, subFieldsList);
			return fieldsList;
			
		}catch(Exception e){
			EmpExecutionContext.error(e, "根据编号获取控件集合异常。");
			return null;
		}
	}
	
	/**
	 * 设置子项
	 * @param pageField
	 * @return
	 */
	private boolean setSubFieldMap(LfPageField pageField){
		
		try{
			List<LfPageField> subFieldList = SubFieldsMap.get(pageField.getFieldId());
			if(subFieldList == null){
				subFieldList = new ArrayList<LfPageField>();
			}
			subFieldList.add(pageField);
			SubFieldsMap.put(pageField.getFieldId(), subFieldList);
			return true;
		}catch(Exception e){
			EmpExecutionContext.error(e, "设置控件子项异常。");
			return false;
		}
	}
}
