package com.montnets.emp.rms.table;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MT_DATAREPORT 表映射对象
 * @date 2018-6-13 13:51:24
 * @author Cheng
 */

public class TableMtDataReport {
    /**
     * 实体类字段与数据库字段实体类映射的map集合
     */
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 表名
     */
    public static final String TABLE_NAME = "MT_DATAREPORT";
    /**
     * 自增Id
     */
    public static final String ID = "ID";
    /**
     * SP账号
     */
    public static final String USERID = "USERID";
    /**
     * 任务Id
     */
    public static final String TASKID = "TASKID";
    /**
     * 主端口
     */
    public static final String SPGATE = "SPGATE";
    /**
     * 日期(年月日)
     */
    public static final String IYMD = "IYMD";
    /**
     * 小时
     */
    public static final String IHOUR = "IHOUR";
    /**
     * 平台编码
     */
    public static final String PTCODE = "PTCODE";
    /**
     * 月份
     */
    public static final String IMONTH = "IMONTH";
    /**
     * 发送总数
     */
    public static final String ICOUNT = "ICOUNT";
    /**
     * 成功数
     */
    public static final String SUCC = "SUCC";
    /**
     * 下载成功数
     */
    //public static final String DWSUCC = "DWSUCC";
    /**
     * 失败1数量
     */
    public static final String FAIL1 = "FAIL1";
    /**
     * 失败2数量
     */
    public static final String FAIL2 = "FAIL2";
    /**
     * 失败3数量
     */
    public static final String FAIL3 = "FAIL3";
    /**
     * 未返数
     */
    public static final String NRET = "NRET";
    /**
     * 成功数
     */
    public static final String RSUCC = "RSUCC";
    /**
     * R失败1数量
     */
    public static final String RFAIL1 = "RFAIL1";
    /**
     * R失败2数量
     */
    public static final String RFAIL2 = "RFAIL2";
    /**
     * 接收失败数
     */
    public static final String RECFAIL = "RECFAIL";
    /**
     * R未返数量
     */
    public static final String RNRET = "RNRET";
    /**
     * 备用
     */
    public static final String RELEASEFLAG = "RELEASEFLAG";
    /**
     * 开始时间
     */
    public static final String STARTTIME = "STARTTIME";
    /**
     * 结束时间
     */
    public static final String ENDTIME = "ENDTIME";
    /**
     * 年
     */
    public static final String Y = "Y";
    /**
     * 运营商 0 ：移动 1 ：联通 21：电信
     */
    public static final String SPISUNCM = "SPISUNCM";
    /**
     * SPID
     */
    public static final String SPID = "SPID";
    /**
     * 业务类型
     */
    public static final String SVRTYPE = "SVRTYPE";
    /**
     * 自定义参数1
     */
    public static final String P1 = "P1";
    /**
     * 自定义参数2
     */
    public static final String P2 = "P2";
    /**
     * 自定义参数3
     */
    public static final String P3 = "P3";
    /**
     * 自定义参数4
     */
    public static final String P4 = "P4";
    /**
     * 发送方式：1：人工批量 2：系统批量 3：人工实时 4：系统实时
     */
    public static final String SENDTYPE = "SENDTYPE";
    /**
     * 号码归属地
     */
    public static final String MOBILEAREA = "MOBILEAREA";
    /**
     * 国际区域代码
     */
    public static final String AREACODE = "AREACODE";
    /**
     * 网关对文件批量请求统一指定批次号
     */
    public static final String BATCHID = "BATCHID";
    /**
     * 档位信息
     */
    public static final String CHGRADE = "CHGRADE";
    /**
     * 类型
     */
    public static final String MSGTYPE = "MSGTYPE";

    static {
        columns.put("id", TableMtDataReport.ID);
        columns.put("userId", TableMtDataReport.USERID);
        columns.put("taskId", TableMtDataReport.TASKID);
        columns.put("spGate", TableMtDataReport.SPGATE);
        columns.put("iymd", TableMtDataReport.IYMD);
        columns.put("ihour", TableMtDataReport.IHOUR);
        columns.put("ptCode", TableMtDataReport.PTCODE);
        columns.put("icount", TableMtDataReport.ICOUNT);
        columns.put("succ", TableMtDataReport.SUCC);
        columns.put("fail1", TableMtDataReport.FAIL1);
        columns.put("fail2", TableMtDataReport.FAIL2);
        columns.put("fail3", TableMtDataReport.FAIL3);
        columns.put("rsucc", TableMtDataReport.RSUCC);
        columns.put("rfail1", TableMtDataReport.RFAIL1);
        columns.put("rfail2", TableMtDataReport.RFAIL2);
        columns.put("recfail", TableMtDataReport.RECFAIL);
        columns.put("rnret", TableMtDataReport.RNRET);
        columns.put("releaseFlag", TableMtDataReport.RELEASEFLAG);
        columns.put("startTime", TableMtDataReport.STARTTIME);
        columns.put("endTime", TableMtDataReport.ENDTIME);
        columns.put("imonth", TableMtDataReport.IMONTH);
        columns.put("y", TableMtDataReport.Y);
        columns.put("spisuncm", TableMtDataReport.SPISUNCM);
        columns.put("spId", TableMtDataReport.SPID);
        columns.put("svrType", TableMtDataReport.SVRTYPE);
        columns.put("p1", TableMtDataReport.P1);
        columns.put("p2", TableMtDataReport.P2);
        columns.put("p3", TableMtDataReport.P3);
        columns.put("p4", TableMtDataReport.P4);
        columns.put("sendType", TableMtDataReport.SENDTYPE);
        columns.put("mobileArea", TableMtDataReport.MOBILEAREA);
        columns.put("batchId", TableMtDataReport.BATCHID);
        columns.put("areaCode", TableMtDataReport.AREACODE);
        columns.put("chgrade", TableMtDataReport.CHGRADE);
        //columns.put("dwsucc", TableMtDataReport.DWSUCC);
        columns.put("msgType", TableMtDataReport.MSGTYPE);
    }

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return map集合
     */
    public static Map<String, String> getORM()
    {
        return columns;
    }

}
