package com.montnets.emp.qyll.biz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.qyll.dao.LldgDao;
import com.montnets.emp.qyll.entity.LLOrderTask;
import com.montnets.emp.qyll.entity.LlOrderDetail;

/**
 * 
 * @author xiebk
 *
 */
public class QyllOrderBiz {
	LldgDao llDao = new LldgDao();
	
	/**
	 * 订购流量
	 * @param task
	 * @param detail
	 * @return
	 */
	public String order(LLOrderTask orderTask,LlOrderDetail orderDetail,Map<String,String> paraMap,HttpServletRequest request ){
		String result = "";
		//保存订购详情
		String filePath = paraMap.get("filePath");
		List<LlOrderDetail> list = new ArrayList<LlOrderDetail>();
		BufferedReader br = null;
		try {
			File file = new File(filePath);
			if(!file.isFile()){
				result = MessageUtils.extractMessage("qyll","qyll_lldg_msg8",request);//"有效号码文件目录不存在";
				return result;
			}
			br = new BufferedReader(new FileReader(file));
			String line = "";
			String [] params = null;
			while((line = br.readLine()) != null){
				LlOrderDetail lod = (LlOrderDetail) orderDetail.clone();
				params = line.split(",");
				lod.setMobile(params[0]);
				lod.setProductId(lod.getProIds()[Integer.parseInt(params[1])]);
				list.add(lod);
			}
			if(list.size() > 0){
				if(!llDao.insertLlOrderDetail(list)){
					result = MessageUtils.extractMessage("qyll","qyll_lldg_msg9",request);//"流量订购：数据库操作失败！";
					return result;
				}
				list.clear();
			}
			
			//订购详情全部入库成功，保存流量订购任务
			if(!llDao.insertLlTask(orderTask)){
				result = MessageUtils.extractMessage("qyll","qyll_lldg_msg9",request); //"流量订购：数据库操作失败！";
				return result;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"流量订购：读取有效号码文件存入数据库失败！");
			result = MessageUtils.extractMessage("qyll","qyll_lldg_msg9",request);//"流量订购：读取有效号码文件存入数据库失败！";
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"流量订购：关闭流失败！");
				}
			}
		}
		return result;
	}
	
	
}
