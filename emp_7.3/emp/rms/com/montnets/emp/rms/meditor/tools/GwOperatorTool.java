package com.montnets.emp.rms.meditor.tools;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.model.TempParams;

import java.util.List;

public class GwOperatorTool {
	/**
	 * type:类型  1-rms,2-h5,3-ott
	 * bytes 此类型模板文件的字节数组
	 * degree 类型模板文件所属档位
	 * degreeSize 类型模板文件占用字节数 KB
	 */
	public boolean getSubmitTmpList(int type,byte[] bytes,int degree,int degreeSize,int pnum,List<TempParams>list ){
		boolean flag = false;
		 try {
			TempParams tp = new TempParams();
			 tp.setType(type);
			 tp.setDegree(degree);//档位
//		 tp.setPnum(parmLegth);//pnum:表示参数个数,如果是静态模板填0,动态模板填参数个数
			 //根据参数个数判断传给审核中心的参数写何值
			 if(pnum > 0){//TODO 这里是实际多个参数只写1个还是说实际参数个数
				tp.setPnum(1);//pnum:表示参数个数,如果是静态模板填0,动态模板填参数个数 --V2.0改为1 ,所有参数从P1里面获取
			 }else if(pnum == 0){//静态模板
				tp.setPnum(0);
			 }
			 tp.setSize(degreeSize);//表示档位对应的文件大小,不包含参数 ; 档位*1024
			 tp.setContentByte(bytes);
			 list.add(tp);
			 flag = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"组装上传审核平台参数出现异常！");
		}
		return flag;
	}

}
