package com.montnets.emp.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.passage.TableXtGateQueue;
import com.montnets.emp.table.report.TableMtDatareport;
import com.montnets.emp.table.wy.TableAAreacode;

/**
 * 运营商各国发送详情
 * @Projevt: p_cxtj
 * @ClassName: ViewOperatorsAreaMtDataReportVo
 * @Description: TODO
 * @Company ShenZhen Montnets Technology CO.,LTD.
 * @author duanjl <duanjialin28827@163.com>
 * @date 2015-7-21 上午09:51:04
 */
public class ViewOperatorsAreaMtDataReportVo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("spgate", TableMtDatareport.SPGATE);
		columns.put("spisuncm", TableMtDatareport.SPISUNCM);
		//columns.put("iymd", TableSpMtDatareport.IYMD);
		columns.put("icount", TableMtDatareport.ICOUNT);
		columns.put("rfail1", TableMtDatareport.RFAIL1);
		columns.put("rfail2", TableMtDatareport.RFAIL2);
		columns.put("rnret", TableMtDatareport.RNRET);
		columns.put("rsucc", TableMtDatareport.RSUCC);
		columns.put("y", TableMtDatareport.Y);
		columns.put("imonth", TableMtDatareport.IMONTH);
		columns.put("spID", TableMtDatareport.SPID);
		columns.put("areacode", TableAAreacode.AREACODE);
		columns.put("areaname", TableAAreacode.AREANAME);
		columns.put("gatename", TableXtGateQueue.GATE_NAME);
		columns.put("iymd", TableMtDatareport.IYMD);
		columns.put("sendType", "SENDTYPE");
		columns.put("spType", TableUserdata.SPTYPE);
		//columns.put("staffName", TableUserdata.STAFF_NAME);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM()
	{
		return columns;
	}
}
