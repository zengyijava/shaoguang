package com.montnets.emp.wyquery.dao;

import java.util.Calendar;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.wyquery.vo.WyReportVo;

/**
 * Sp账号统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:43:00
 * @description
 */
public class GenericSpMtDataReportVoSQL {
	
	/**
	 * 以下是日报表用到的方法
	 * @return
	 */
	public  String getWyReportSql(WyReportVo wyrptvo) {
		
		//数据库的判空函数 默认为oracle的判断NULL函数
		//时间查询条件数组
		String[] timeStrCondition = new String[3];
		//格式化传入起始时间
		String sendtime = wyrptvo.getSendtime().replaceAll("-", "");
		String endtime=wyrptvo.getEndtime().replaceAll("-", "");
		
		//时间语句
		timeStrCondition[0] = " AND mtdatareport.IYMD>="+ sendtime + " ";
		timeStrCondition[1] = " AND mtdatareport.IYMD<="+ endtime +" ";
		timeStrCondition[2] = " AND mtdatareport.IYMD>="+ sendtime+ " AND mtdatareport.IYMD<="+ endtime + " ";
		
		String conttypestr="";
		if(wyrptvo!=null&&wyrptvo.getGateName()!=null
				&&!"".equals(wyrptvo.getGateName())){
			conttypestr=" AND u.GATENAME like '%"+wyrptvo.getGateName().trim()+"%' ";
		}
		//通道号
		if(wyrptvo!=null&&wyrptvo.getSpgate()!=null
				&&!"".equals(wyrptvo.getSpgate())){
			conttypestr=" AND mtdatareport.spgate = '"+wyrptvo.getSpgate()+"'";
		}
		
		//时间条件的查询
		if( (0 != wyrptvo.getSendtime().length() && 0 == wyrptvo.getEndtime().length()))
		{
			conttypestr += timeStrCondition[0];
		
		}else if ( 0 == wyrptvo.getSendtime().length() && 0 != wyrptvo.getEndtime().length() )
		{
			conttypestr += timeStrCondition[1];
		    
		}else if (0 != wyrptvo.getSendtime().length() && 0 != wyrptvo.getEndtime().length() )
		{
			conttypestr += timeStrCondition[2] ;
		}else
		{
			//如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();
			if( null != yearAndMonth )
			{
				conttypestr += " AND mtdatareport.Y="+yearAndMonth[0]+" AND mtdatareport.IMONTH="+yearAndMonth[1]+"";
			}
		}

		conttypestr = conttypestr.replaceFirst("^(\\s*)(?i)and", "$1where");
	
		String sql ="SELECT mtdatareport.spgate SPGATE,u.GATENAME,sum(mtdatareport.ICOUNT) ICOUNT,sum(mtdatareport.RSUCC) RSUCC,sum(mtdatareport.RFAIL1) RFAIL1,sum(mtdatareport.RFAIL2) RFAIL2,sum(mtdatareport.RNRET) RNRET " +
				"FROM MT_DATAREPORT mtdatareport inner join (SELECT GATENAME,SPGATE FROM XT_GATE_QUEUE WHERE SPGATE LIKE '200%' "
				+") u on mtdatareport.SPGATE=u.SPGATE ";
		String groupbystr=" GROUP BY mtdatareport.spgate,u.GATENAME";
		sql=sql+conttypestr;
		sql=sql+groupbystr;
		return sql;
	}
	
	
	
	
	
	/**
	 * 以下是日报表用到的方法
	 * @return
	 */
	public  String getWyDetailReportSql(WyReportVo wyrptvo,String reporttype) {
		
		if(wyrptvo.getSpgate()==null||"".equals(wyrptvo.getSpgate())||reporttype==null||"".equals(reporttype)){
			return null;
		}

		//分组
		String groupcloumnname="";
		if("2".equals(reporttype)){
			groupcloumnname=" Y,IMONTH ";
		}else{
			groupcloumnname=" IYMD ";
		}
		
		
		//数据库的判空函数 默认为oracle的判断NULL函数
		//时间查询条件数组
		String[] timeStrCondition = new String[3];
		//格式化传入起始时间
		String sendtime = wyrptvo.getSendtime().replaceAll("-", "");
		String endtime=wyrptvo.getEndtime().replaceAll("-", "");
		
		//时间语句
		timeStrCondition[0] = " AND mtdatareport.IYMD>="+ sendtime + " ";
		timeStrCondition[1] = " AND mtdatareport.IYMD<="+ endtime +" ";
		timeStrCondition[2] = " AND mtdatareport.IYMD>="+ sendtime+ " AND mtdatareport.IYMD<="+ endtime + " ";
		
		String conttypestr="";
		if(wyrptvo!=null&&wyrptvo.getGateName()!=null
				&&!"".equals(wyrptvo.getGateName())){
			conttypestr=" AND u.GATENAME like '%"+wyrptvo.getGateName().trim()+"%' ";
		}
		//通道号
		if(wyrptvo!=null&&wyrptvo.getSpgate()!=null
				&&!"".equals(wyrptvo.getSpgate())){
			conttypestr=" AND mtdatareport.spgate = '"+wyrptvo.getSpgate()+"'";
		}
		
		//时间条件的查询
		if( (0 != wyrptvo.getSendtime().length() && 0 == wyrptvo.getEndtime().length()))
		{
			conttypestr += timeStrCondition[0];
		
		}else if ( 0 == wyrptvo.getSendtime().length() && 0 != wyrptvo.getEndtime().length() )
		{
			conttypestr += timeStrCondition[1];
		    
		}else if (0 != wyrptvo.getSendtime().length() && 0 != wyrptvo.getEndtime().length() )
		{
			conttypestr += timeStrCondition[2] ;
		}else
		{
			//如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();
			if( null != yearAndMonth )
			{
				conttypestr += " AND mtdatareport.Y="+yearAndMonth[0]+" AND mtdatareport.IMONTH="+yearAndMonth[1]+"";
			}
		}

		conttypestr = conttypestr.replaceFirst("^(\\s*)(?i)and", "$1where");
		
		String sql ="SELECT "+groupcloumnname+",mtdatareport.spgate SPGATE,u.GATENAME,sum(mtdatareport.ICOUNT) ICOUNT,sum(mtdatareport.RSUCC) RSUCC,sum(mtdatareport.RFAIL1) RFAIL1,sum(mtdatareport.RFAIL2) RFAIL2,sum(mtdatareport.RNRET) RNRET " +
				"FROM MT_DATAREPORT mtdatareport inner join (SELECT GATENAME,SPGATE FROM XT_GATE_QUEUE WHERE SPGATE LIKE '200%' "
				+") u on mtdatareport.SPGATE=u.SPGATE ";
		String groupbystr=" GROUP BY "+groupcloumnname+",mtdatareport.spgate,u.GATENAME ";
		sql=sql+conttypestr;
		sql=sql+groupbystr;
		return sql;
	}
	
	
	
	
	/**
	 * 
	 * 获取当前的年份和月份的数组，用于限制查询当前月份的操作员报表
	 * 
	 * @return
	 */
	public String[] getYearAndMonth()
	{
		String[] datetime= new String[2];
		
		try{
			//获取当前时间对象
			Calendar cal = Calendar.getInstance();
			Integer month = cal.get(Calendar.MONTH);
			//月份加一
			month+=1;
			datetime[0] = String.valueOf(cal.get(Calendar.YEAR));
			datetime[1] = String.valueOf(month);
		}catch(Exception e){
			EmpExecutionContext.error(e,"操作员统计报表获取年份和月份数组异常");
		}
		
		
		return datetime;
	}
	
	
	
	
}
