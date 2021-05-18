package com.montnets.emp.report.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.report.bean.RptConfInfo;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.dao.GenericBusReportVoDAO;
import com.montnets.emp.report.vo.BusNationtVo;
import com.montnets.emp.report.vo.BusReportVo;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;

/**
 * 业务类型报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:20:18
 * @description
 */
public class BusReportBiz{
	/**
	 * 业务类型报表
	 * @param busReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<BusReportVo> getBusReportByVo(BusReportVo busReportVo, PageInfo pageInfo) throws Exception {
		//业务类型报表集合
		List<BusReportVo> busReportVos;

		try {
			//判断是否需要分页
			if (pageInfo == null) {
				busReportVos = new GenericBusReportVoDAO().findBusReportsByVo(busReportVo);
			} else {
				busReportVos = new GenericBusReportVoDAO().findBusReportsPageByVo(busReportVo,pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务类型报表查询异常");
			throw e;
		}
		return busReportVos;
	}

	/**
	 * 业务类型报表   合计
	 * @param busReportVo
	 * @return
	 * @throws Exception
	 */
	
    public long[] findSumCount(BusReportVo busReportVo) throws Exception {
		//获取合计数存数数组中
		long[] count = new GenericBusReportVoDAO().findSumCount(busReportVo);
		return count;
	}
	
	/**
	 * 业务类型报表(国家类型)
	 * @param busReportVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<BusNationtVo> getBusNationReportByVo(BusReportVo busReportVo, PageInfo pageInfo) throws Exception {
		//业务类型报表集合
		List<BusNationtVo> busReportVos;

		try {
			//判断是否需要分页
			if (pageInfo == null) {
				busReportVos = new GenericBusReportVoDAO().findBusNationReportsByVo(busReportVo);
			} else {
				busReportVos = new GenericBusReportVoDAO().findBusNationReportsPageByVo(busReportVo,pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务类型报表查询异常");
			throw e;
		}
		return busReportVos;
	}
	
	/**
	 * 生成业务类型统计报表Excel(未使用的方法)
	 * @param busreportvo
	 * @param busList
	 * @param faill
	 * @param succc
	 * @return
	 * @throws Exception
	 */
		
        public Map<String, String> createBusReportExcel(List<BusReportVo> busList, String succc, String faill, String showtime, String datasourcename)
				throws Exception {
			return null;
		}

    /**
     * 生成业务类型统计报表Excel
     * @param busList
     * @param spisuncm
     * @param beginTime
     * @param endTime
     * @param counTime
     * @param reportType
     * @param sumArray
     * @param datasourcename
     * @return
     * @throws Exception
     */
    public Map<String, String> createBusExcel_V1(List<BusReportVo> busList,String spisuncm,String beginTime,
    		String endTime,String counTime,int reportType,long[] sumArray,String datasourcename,HttpServletRequest request)
		throws Exception {
        String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
        String voucherPath = "download";
        String reportFileName = "Report";
        String reportName = "BusReport";

        Map<String, String> resultMap = new HashMap<String, String>();

        // 当前每页分页条数
        int intRowsOfPage = 500000;

        // 当前每页显示条数
        int intPagesCount = (busList.size() % intRowsOfPage == 0) ? (busList
                .size() / intRowsOfPage) : (busList.size() / intRowsOfPage + 1);

        int size = intPagesCount; // 生成的工作薄个数
        EmpExecutionContext.info("业务类型统计报表导出生成报表个数：" + size);
        if (size == 0) {
            EmpExecutionContext.info("无业务类型统计报表数据！");
            throw new Exception("无业务类型统计报表数据！");
        }

        // 产生报表文件的存储路径
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
        String voucherFilePath = BASEDIR + File.separator + voucherPath
                + File.separator + reportFileName + File.separator
                + sdf.format(curDate);
        File fileTemp = new File(voucherFilePath);
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
        // 报表文件名
        String fileName = null;
        String filePath = null;
        XSSFWorkbook workbook = null;

        OutputStream os = null;
        try {
            List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.BUS_RPT_CONF_MENU_ID);
            Map<String,String> sumCountMap = null;
            //报表列统计字段数
            int sumCountSize = rptConList.size();

            for (int j = 1; j <= size; j++) {

                fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j
                        + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
                // 工作表
                workbook = new XSSFWorkbook();
                // 表格样式
                XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook);
                XSSFSheet sheet = workbook.createSheet(reportName);
                // 读取模板工作表
                XSSFRow row = null;
                EmpExecutionContext.debug("工作薄创建与模板移除成功!");

                //当前工作薄写入行索引
                int index = 0;

                //动态列信息
                String[] dataArray = new String[sumCountSize];
                String[] colIds = new String[sumCountSize];

                for (int i = 0; i < sumCountSize; i++) {
                    RptConfInfo rptConfInfo = rptConList.get(i);
                    String temp = rptConfInfo.getName();
                    //dataArray[i] = rptConfInfo.getName();
                    dataArray[i] = MessageUtils.extractMessage("cxtj",temp,request);
                    colIds[i] = rptConfInfo.getColId();
                }

                row = sheet.createRow(index++);


                //setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{"时间","业务类型","发送类型","运营商"},dataArray));
                String time = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_sj", request);
                String bustype = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_ywlx", request);
                String sendtype = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_fslx", request);
                String yys = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_yys", request);
                setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{time,bustype,sendtype,yys},dataArray));
                // 总体流程
                // 根据记录，计算总的页数
                // 每页的处理 一页一个sheet
                // 写入页标题
                // 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
                // 写页尾

                int intCnt = busList.size() < j * intRowsOfPage ? busList
                        .size() : j * intRowsOfPage;

                String name = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_hj", request);

                for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

                    BusReportVo busreportVo = busList.get(k);
                    //业务类型名称
                    String busname=busreportVo.getBusName()==null?"-":busreportVo.getBusName();

                    sumCountMap = ReportBiz.getRptNums(busreportVo.getIcount(),busreportVo.getRsucc(),busreportVo.getRfail1(),busreportVo.getRfail2(),busreportVo.getRnret(),RptStaticValue.BUS_RPT_CONF_MENU_ID);
                    dataArray = getSumArrays(sumCountMap,colIds);

                    row = sheet.createRow(index++);

                    String spisuncmstr=  "";
                    if("".equals(spisuncm)){
                        spisuncmstr=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
                    }else if(!"".equals(spisuncm)){
                        if("5".equals(spisuncm)){
                            spisuncmstr=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gw", request);
                        }else if("100".equals(spisuncm)){
                            spisuncmstr=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gn", request);
                        }else{
                            spisuncmstr=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
                        }
                    }else{
                        spisuncmstr=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
                    }

                    // 设置单元格内容
                    String showTime = "";
                    String btime =beginTime;
                    String etime =endTime;
                    if(reportType==2){
//                    if( !"".equals(btime) && null != btime && 0 != btime.length())
//                    {
//                        String btemp[] = btime.split("-");
//
//                        btime = btemp[0]+"年"+btemp[1]+"月"+btemp[2]+"日";
//
//                    }
//
//                    if( !"".equals(etime) && null != etime && 0 != etime.length() )
//                    {
//                        String etemp[] = etime.split("-");
//
//                        etime = etemp[0]+"年"+etemp[1]+"月"+etemp[2]+"日";
//                    }
                    showTime = btime+" "+MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_z",request)+" "+etime;

                    }else if(reportType==0){
                        if(counTime!=null){
                            String times[]=counTime.split("-");
                            showTime=times[0]+"-"+times[1];
                        }else{
                            showTime=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
                        }
                    }else {
                        if(counTime!=null){
                            String times[]=counTime.split("-");
                            showTime=times[0];
                        }else{
                            showTime=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
                        }
                    }

                    datasourcename = datasourcename==null||"".equals(datasourcename)?"--":datasourcename;

                    setRowData(row,cellStyle[0], (String[]) ArrayUtils.addAll(new String[]{showTime,busname,datasourcename,spisuncmstr},dataArray));
                }

                row = sheet.createRow(index++);
                sumCountMap = ReportBiz.getRptNums(sumArray[0],sumArray[1],sumArray[2],sumArray[3],sumArray[4],RptStaticValue.BUS_RPT_CONF_MENU_ID);
                dataArray = getSumArrays(sumCountMap,colIds);
                setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{"","","",name},dataArray));

                // 输出到xlsx文件
                os = new FileOutputStream(voucherFilePath + File.separator
                        + fileName);
                // 写入Excel对象
                workbook.write(os);

            }


            fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_rpt_yybb", request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
            filePath = BASEDIR + File.separator + voucherPath + File.separator
                    + reportFileName + File.separator + fileName;
            // 压缩文件夹
            ZipUtil.compress(voucherFilePath, filePath);
            // 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"业务类型报表生成excel导出异常");
        } finally {
            // 清除对象
            workbook = null;
            SysuserUtil.closeStream(os);
        }
        resultMap.put("FILE_NAME", fileName);
        resultMap.put("FILE_PATH", filePath);
        return resultMap;

}

    /**
     * 生成业务类型统计报表Excel
     * @param busList
     * @param reportType
     * @param sumArray
     * @param datasourcename
     * @return
     * @throws Exception
     */
	public Map<String, String> createBusReportExcel(List<BusReportVo> busList,int reportType, long[] sumArray,String datasourcename,HttpServletRequest request)
			throws Exception {
        String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
        String voucherPath = "download";
        String reportFileName = "Report";
        String reportName = "BusReport";

        Map<String, String> resultMap = new HashMap<String, String>();

        // 当前每页分页条数
        int intRowsOfPage = 500000;

        // 当前每页显示条数
        int intPagesCount = (busList.size() % intRowsOfPage == 0) ? (busList
                .size() / intRowsOfPage) : (busList.size() / intRowsOfPage + 1);

        int size = intPagesCount; // 生成的工作薄个数
        EmpExecutionContext.info("业务类型统计报表导出生成报表个数：" + size);
        if (size == 0) {
            EmpExecutionContext.info("无业务类型统计报表数据！");
            throw new Exception("无业务类型统计报表数据！");
        }

        // 产生报表文件的存储路径
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
        String voucherFilePath = BASEDIR + File.separator + voucherPath
                + File.separator + reportFileName + File.separator
                + sdf.format(curDate);
        File fileTemp = new File(voucherFilePath);
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
        // 报表文件名
        String fileName = null;
        String filePath = null;
        XSSFWorkbook workbook = null;

        OutputStream os = null;
        try {
            List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.BUS_RPT_CONF_MENU_ID);
            Map<String,String> sumCountMap = null;
            //报表列统计字段数
            int sumCountSize = rptConList.size();

            for (int j = 1; j <= size; j++) {

                fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j
                        + "].xlsx";
                // 工作表
                workbook = new XSSFWorkbook();
                // 表格样式
                XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook);

                XSSFSheet sheet = workbook.createSheet(reportName);

                // 读取模板工作表
                XSSFRow row = null;

                EmpExecutionContext.debug("工作薄创建与模板移除成功!");

                //当前工作薄写入行索引
                int index = 0;

                //动态列信息
                String[] dataArray = new String[sumCountSize];
                String[] colIds = new String[sumCountSize];

                for (int i = 0; i < sumCountSize; i++) {
                    RptConfInfo rptConfInfo = rptConList.get(i);
                    String temp = rptConfInfo.getName();
                    //dataArray[i] = rptConfInfo.getName();
                    dataArray[i] = MessageUtils.extractMessage("cxtj",temp,request);
                    colIds[i] = rptConfInfo.getColId();
                }

                row = sheet.createRow(index++);

                //setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{"时间","业务类型","发送类型"},dataArray));
                String time = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_sj", request);
                String bustype = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_ywlx", request);
                String sendtype = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_fslx", request);
                setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{time,bustype,sendtype},dataArray));

                // 总体流程
                // 根据记录，计算总的页数
                // 每页的处理 一页一个sheet
                // 写入页标题
                // 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
                // 写页尾

                int intCnt = busList.size() < j * intRowsOfPage ? busList
                        .size() : j * intRowsOfPage;
                String name = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_hj", request);

                for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {
                    BusReportVo busreportVo = busList.get(k);
                    //业务类型名称
                    String busname=busreportVo.getBusName()==null?"-":busreportVo.getBusName();

                    sumCountMap = ReportBiz.getRptNums(busreportVo.getIcount(),busreportVo.getRsucc(),busreportVo.getRfail1(),busreportVo.getRfail2(),busreportVo.getRnret(),RptStaticValue.BUS_RPT_CONF_MENU_ID);
                    dataArray = getSumArrays(sumCountMap,colIds);

                    row = sheet.createRow(index++);

                    // 设置单元格内容
                    String showTime = "";
                    if(reportType==1){//如果选择的是年
                        if(busreportVo.getY()!=null&&busreportVo.getImonth()!=null){
                            showTime=busreportVo.getY()+"-"+busreportVo.getImonth();
                        }else if(busreportVo.getY()!=null){
                            showTime=busreportVo.getY();
                        }else{
                            showTime="-";
                        }
                    }else if(busreportVo.getIymd()!=null){
                        String date=busreportVo.getIymd()+"";
                        showTime=date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
                    }else {
                        showTime="-";
                    }

                    datasourcename = datasourcename==null||"".equals(datasourcename)?"--":datasourcename;
                    setRowData(row,cellStyle[0], (String[]) ArrayUtils.addAll(new String[]{showTime,busname,datasourcename},dataArray));

                }

                row = sheet.createRow(index++);

                sumCountMap = ReportBiz.getRptNums(sumArray[0],sumArray[1],sumArray[2],sumArray[3],sumArray[4],RptStaticValue.BUS_RPT_CONF_MENU_ID);
                dataArray = getSumArrays(sumCountMap,colIds);
                setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{"","",name},dataArray));

                // 输出到xlsx文件
                os = new FileOutputStream(voucherFilePath + File.separator
                        + fileName);
                // 写入Excel对象
                workbook.write(os);
            }

            fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_rpt_yybb", request) + sdf.format(curDate) + ".zip";
            filePath = BASEDIR + File.separator + voucherPath + File.separator
                    + reportFileName + File.separator + fileName;
            // 压缩文件夹
            ZipUtil.compress(voucherFilePath, filePath);
            // 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"业务类型报表生成excel导出异常");
        } finally {
            // 清除对象
            workbook = null;
            SysuserUtil.closeStream(os);
        }
        resultMap.put("FILE_NAME", fileName);
        resultMap.put("FILE_PATH", filePath);
        return resultMap;

	}

    /**
     * 生成业务类型统计报表Excel
     * @param busList
     * @param btime
     * @param etime
     * @param counTime
     * @param reportType
     * @param sumArray
     * @param showtime
     * @param datasourcename
     * @return
     * @throws Exception
     */
    public Map<String, String> createBusNationExcel(List<BusNationtVo> busList,String btime,String etime,String counTime,
    		int reportType, long[] sumArray,String showtime,String datasourcename,HttpServletRequest request)
            throws Exception {
        String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
        String voucherPath = "download";
        String reportFileName = "Report";
        String reportName = "BusReport";

        Map<String, String> resultMap = new HashMap<String, String>();

        // 当前每页分页条数
        int intRowsOfPage = 500000;

        // 当前每页显示条数
        int intPagesCount = (busList.size() % intRowsOfPage == 0) ? (busList
                .size() / intRowsOfPage) : (busList.size() / intRowsOfPage + 1);

        int size = intPagesCount; // 生成的工作薄个数
        EmpExecutionContext.info("业务类型统计报表国家详情生成报表个数：" + size);
        if (size == 0) {
            EmpExecutionContext.info("无业务类型统计报表国家详情数据！");
            throw new Exception("无业务类型统计报表国家详情数据！");
        }

        // 产生报表文件的存储路径
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
        String voucherFilePath = BASEDIR + File.separator + voucherPath
                + File.separator + reportFileName + File.separator
                + sdf.format(curDate);
        File fileTemp = new File(voucherFilePath);
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
        // 报表文件名
        String fileName = null;
        String filePath = null;
        XSSFWorkbook workbook1 = null;
        SXSSFWorkbook workbook = null;

        OutputStream os = null;
        try {
            List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.BUS_RPT_CONF_MENU_ID);
            Map<String,String> sumCountMap = null;
            //报表列统计字段数
            int sumCountSize = rptConList.size();

            for (int j = 1; j <= size; j++) {
                //文件名
                fileName = reportName + "_" + sdf.format(new Date()) + "_[" + j
                        + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";

                // 工作表
                workbook1 = new XSSFWorkbook();
                workbook1.createSheet(reportName);
                workbook = new SXSSFWorkbook(workbook1,10000);
                Sheet sheet=workbook.getSheetAt(0);
                // 表格样式
                XSSFCellStyle[] cellStyle = new ExcelTool().setLastCellStyle(workbook1);

                EmpExecutionContext.debug("工作薄创建与模板移除成功!");
                //当前工作薄写入行索引
                int index = 0;

                // 读取模板工作表
                Row row = null;

                //动态列信息
                String[] dataArray = new String[sumCountSize];
                String[] colIds = new String[sumCountSize];

                for (int i = 0; i < sumCountSize; i++) {
                    RptConfInfo rptConfInfo = rptConList.get(i);
                    String temp = rptConfInfo.getName();
                    //dataArray[i] = rptConfInfo.getName();
                    dataArray[i] = MessageUtils.extractMessage("cxtj",temp,request);
                    colIds[i] = rptConfInfo.getColId();
                }

                row = sheet.createRow(index++);

                //setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{"时间","国家/地区代码","国家名称","业务类型","通道号码","通道名称","发送类型"},dataArray));
                String time = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_sj",request);
                String gjdq = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_gjdqdm",request);
                String gjmc = MessageUtils.extractMessage("cxtj","cxtj_new_rpt_gjmc",request);
                String ywlx = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_ywlx",request);
                String tdhm = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_tdhm",request);
                String tdmc = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_tdmc",request);
                String fslx = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_fslx",request);
                setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{time,gjdq,gjmc,ywlx,tdhm,tdmc,fslx},dataArray));
                // 总体流程
                // 根据记录，计算总的页数
                // 每页的处理 一页一个sheet
                // 写入页标题
                // 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
                // 写页尾

                int intCnt = busList.size() < j * intRowsOfPage ? busList
                        .size() : j * intRowsOfPage;
                String name = MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_hj",request);

                for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

                    BusNationtVo busNationtVo = busList.get(k);
                    // 国家/地区代码
                    String nationcode = busNationtVo.getNationcode()==null?"-":busNationtVo.getNationcode();
                    //国家名称
                    String nationname=busNationtVo.getNationname()!=null?busNationtVo.getNationname():"-";
                    //SP账号
                    String busname=busNationtVo.getBusName()==null?"-":busNationtVo.getBusName();
                    //通道号码
                    String spgatecode=   busNationtVo.getSpgatecode()==null?"-":busNationtVo.getSpgatecode();
                    //通道名称
                    String spgatename=  busNationtVo.getSpgatename()==null?"-":busNationtVo.getSpgatename();
                    //发送类型
                    String sendtypename="";
                    if(busNationtVo.getSptype()!=null){
                        sendtypename=busNationtVo.getSptype()==1?MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_empyy", request):MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_empjr", request);
                        if(busNationtVo.getSendtype()!=null){
                            if(busNationtVo.getSendtype()==1){
                                sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_fs", request)+")";
                            }else if(busNationtVo.getSendtype()==2){
                                sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_httpjr", request)+")";
                            }else if(busNationtVo.getSendtype()==3){
                                sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_dbjr", request)+")";
                            }else if(busNationtVo.getSendtype()==4){
                                sendtypename=sendtypename+"("+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_zljr", request)+")";
                            }
                        }
                    }

                    sumCountMap = ReportBiz.getRptNums(busNationtVo.getIcount(),busNationtVo.getRsucc(),busNationtVo.getRfail1(),busNationtVo.getRfail2(),busNationtVo.getRnret(),RptStaticValue.BUS_RPT_CONF_MENU_ID);
                    dataArray = getSumArrays(sumCountMap,colIds);

                    // 设置单元格内容
                    String showTime = "";
                    String begin="";
                    String end="";
                    if(reportType==2){
                    if( !"".equals(btime) && null != btime && 0 != btime.length())
                    {
                        String btemp[] = btime.split("-");

                        begin = btemp[0]+"-"+btemp[1]+"-"+btemp[2];

                    }

                    if( !"".equals(etime) && null != etime && 0 != etime.length() )
                    {
                        String etemp[] = etime.split("-");

                        end = etemp[0]+"-"+etemp[1]+"-"+etemp[2];
                    }
                    showTime = begin+" - "+end;

                    }else if(reportType==0){
                        if(counTime!=null){
                            String times[]=counTime.split("-");
                            showTime=times[0]+"-"+times[1];
                        }else{
                            showTime=MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_wz",request);
                        }
                    }else {
                        if(counTime!=null){
                            String times[]=counTime.split("-");
                            showTime=times[0];
                        }else{
                            showTime=MessageUtils.extractMessage("cxtj","cxtj_sjcx_report_wz",request);
                        }
                    }

                    row = sheet.createRow(index++);

                    datasourcename = datasourcename==null||"".equals(datasourcename)?"--":datasourcename;
                    setRowData(row,cellStyle[0], (String[]) ArrayUtils.addAll(new String[]{showTime,nationcode,nationname,busname,spgatecode,spgatename,sendtypename},dataArray));
                }

                row = sheet.createRow(index++);
                sumCountMap = ReportBiz.getRptNums(sumArray[0],sumArray[1],sumArray[2],sumArray[3],sumArray[4],RptStaticValue.BUS_RPT_CONF_MENU_ID);
                dataArray = getSumArrays(sumCountMap,colIds);
                setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{"","","","","","",name},dataArray));

                // 输出到xlsx文件
                os = new FileOutputStream(voucherFilePath + File.separator
                        + fileName);
                // 写入Excel对象
                workbook.write(os);

            }

            fileName = MessageUtils.extractMessage("cxtj","cxtj_new_rpt_yyggxqbb",request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
            filePath = BASEDIR + File.separator + voucherPath + File.separator
                    + reportFileName + File.separator + fileName;
            // 压缩文件夹
            ZipUtil.compress(voucherFilePath, filePath);
            // 删除文件夹
            boolean status = FileUtils.deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"业务类型各国发送详情统计报表生成excel导出异常");
        } finally {
            // 清除对象
            workbook = null;
            SysuserUtil.closeStream(os);
        }
        resultMap.put("FILE_NAME", fileName);
        resultMap.put("FILE_PATH", filePath);
        return resultMap;

    }


    /**
     * 填充一行数据
     * @param row
     * @param cellStyle
     * @param data
     */
    public void setRowData(Row row,XSSFCellStyle cellStyle,String[] data)
    {
        int cols = data.length;
        Cell cell = null;
        for (int i = 0;i<cols;i++) {
            cell = row.createCell(i);
            cell.setCellValue(data[i]);
            cell.setCellStyle(cellStyle);
            Sheet sheet = row.getSheet();

            for(int j=0;j<cols;j++)
            {
                sheet.setColumnWidth(j,Math.max(256*(data[j].getBytes().length+2), sheet.getColumnWidth(j)));
            }
        }
    }

    /**
     * 从map中返回对应的数组元素的值
     * @param sumCountMap
     * @param colIds
     * @return
     */
    public String[] getSumArrays(Map<String,String> sumCountMap,String[] colIds)
    {
        int len = colIds.length;
        String[] arrays = new String[len];
        for (int i = 0;i< len;i++) {
            arrays[i] = sumCountMap.get(colIds[i]);
        }
        return arrays;
    }
}
