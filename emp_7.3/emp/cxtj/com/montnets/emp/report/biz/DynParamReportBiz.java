package com.montnets.emp.report.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.selfparam.LfWgParamConfig;
import com.montnets.emp.entity.selfparam.LfWgParmDefinition;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.report.bean.RptConfInfo;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.dao.GenericDynParamVoDAO;
import com.montnets.emp.report.vo.DynParmReportVo;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;

/**
 * 动态参数报表BIZ
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:20:42
 * @description
 */
public class DynParamReportBiz extends BaseBiz{


	//机构报表DAO
	protected GenericDynParamVoDAO reportVoDao;
	
	//构造函数
	public DynParamReportBiz()
	{
		reportVoDao  = new  GenericDynParamVoDAO();
		
	}
	
	/**
	 * 获取动态参数报表
	 * @param dynParmReportVo
	 * @param paramType
	 * @param paramSubNum
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<DynParmReportVo> getMtDataReportList(DynParmReportVo dynParmReportVo, String paramType, Integer paramSubNum, PageInfo pageInfo)throws Exception
	{
		List<DynParmReportVo>  mtReportList = new ArrayList<DynParmReportVo>();
        //调用查询方法         
		mtReportList = reportVoDao.getMtDataReportInfo(paramType, paramSubNum, dynParmReportVo, pageInfo);
		//返回结果
		return mtReportList;
	}
	
	
	/**
	 * 动态参数报表
	 * @param dynParmReportVo
	 * @param paramType
	 * @param paramSubNum
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<DynParmReportVo> getMtDataReportList_unpage(DynParmReportVo dynParmReportVo, String paramType, Integer paramSubNum)throws Exception
	{
		List<DynParmReportVo>  mtReportList = new ArrayList<DynParmReportVo>();		
        //调用查询方法
		mtReportList = reportVoDao.getMtDataReportInfo_unpage(paramType, paramSubNum, dynParmReportVo);
		//返回结果
		return mtReportList;
	}
	
	
	
	/**
	 * 
	 * 获取动态参数表信息（根据企业编码过滤）
	 * 
	 * @param corpCode
	 * 					企业编码
	 * @return
	 * 
	 * @throws Exception
	 * 
	 */
	
    public List<LfWgParmDefinition> getAllParamConfList(String corpCode)throws Exception{
		
		List<LfWgParmDefinition> paramList = new ArrayList<LfWgParmDefinition>();		
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();		
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
        //组装过滤条件		
		String P_propFile = "SystemGlobals";
		ResourceBundle rb = ResourceBundle.getBundle(P_propFile);
		String depcodethird2p2 = rb.getString("depcodethird2p2");
		if ("true".equals(depcodethird2p2)) {
			conditionMap.put("param&<>", "Param2");
		}
		conditionMap.put("corpCode", corpCode);
		orderbyMap.put("pid", "ASC");
		try{
		    //调用查询方法
			paramList = empDao.findListBySymbolsCondition(LfWgParmDefinition.class, conditionMap, orderbyMap);
			
		}catch(Exception e){
			EmpExecutionContext.error(e,"获取动态参数表信息（根据企业编码过滤）异常");
			throw e;
		}
		return paramList;
		
	}
	
	
	/**
	 * 报表总条数
	 * @param dynParmReportVo
	 * @param paramType
	 * @param paramSubNum
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public long[] findSumCount(DynParmReportVo dynParmReportVo, String paramType, Integer paramSubNum, PageInfo pageInfo) throws Exception{
		
       // 查询方法
		return reportVoDao.findSumCount(paramType, paramSubNum, dynParmReportVo,pageInfo);
	}
	
	 
	/**
	 * 
	 * 根据参数类型和参数段，获取参数值
	 * 
	 * @param paramType
	 * 
	 * @param paramSubNum
	 * 
	 * @return
	 * 
	 * @throws Exception
	 * 
	 */
	
    public String getParamValue(String paramType, Integer paramSubNum) throws Exception
	{
		String paramValues = "";
		
		try{
			
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			List<LfWgParamConfig> paramConfList = null;
			//组装过滤条件 
			if( null != paramType && 0 != paramType.length() && null != paramSubNum  )
			{
				conditionMap.put("param", paramType);
				conditionMap.put("paramSubNum", paramSubNum.toString());	
				orderbyMap.put("pid","DESC" );
				//调用查询方法
				paramConfList = empDao.findListByCondition(LfWgParamConfig.class, conditionMap, orderbyMap);
				
			}
			if( null != paramConfList && 0 != paramConfList.size())
			{
				for ( LfWgParamConfig paramConf :paramConfList )
				{
					paramValues += "'"+paramConf.getParamValue()+"',";
				} 				
			}
			//处理查询结果
			if( null != paramValues && 0 != paramValues.length()&& paramValues.contains(","))
			{
				paramValues = paramValues.substring(0,paramValues.lastIndexOf(","));
			}	
			
		}catch(Exception e){
			EmpExecutionContext.error(e,"根据参数类型和参数段，获取参数值异常");
			throw e;
		}
		//返回结果
		return paramValues;
	}	
	
	/**
	 * @description：生成动态参数统计报表Excel
	 * @param fileName
	 *            文件名
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author wuxiaotao
	 * @see wuxiaotao 2012-1-9 创建
	 */

	
    public Map<String, String> createDynParamReportExcel(
			List<DynParmReportVo> mtdList, long[] sumArray,
			String showTime, String paramTitle,HttpServletRequest request) throws Exception {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/report/file";
		String voucherPath = "download";
		//String reportName = "自定参数统计报表";
		String reportName = "DynParamReport";
		String reportFileName = "Report";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("动态参数统计报表导出生成报表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无动态参数统计报表数据！");
			throw new Exception("无动态参数统计报表数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName+ File.separator
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
            List<RptConfInfo> rptConList = RptConfBiz.getRptConfMap().get(RptStaticValue.DYN_RPT_CONF_MENU_ID);
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

                //setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{"时间",paramTitle,"参数值"},dataArray));
                String time = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_sj", request);
                String para = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zdcstjbb_csz", request);
                setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{time,paramTitle,para},dataArray));


                // 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;

				String name = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_hj", request);

				for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

					DynParmReportVo dynParmReportVo = mtdList.get(k);
                    sumCountMap = ReportBiz.getRptNums(dynParmReportVo.getIcount(),dynParmReportVo.getRsucc(),dynParmReportVo.getRfail1(),dynParmReportVo.getRfail2(),dynParmReportVo.getRnret(),RptStaticValue.DYN_RPT_CONF_MENU_ID);
                    dataArray = getSumArrays(sumCountMap,colIds);


					// 时间
					// String showTime = "-";
					if (null != dynParmReportVo.getIymd() && 0 != dynParmReportVo.getIymd().length()
							&& dynParmReportVo.getIymd().contains("-")) {
						String[] showTimeArray = dynParmReportVo.getIymd().split("-");
						Integer dTime = Integer.parseInt(showTimeArray[2]
								.substring(0, 2).toString());// 显示天数
						Integer monthtime = Integer.parseInt(showTimeArray[1]
								.toString());// 显示月份

						showTime = showTimeArray[0] + "-" + monthtime + "-"
								+ dTime ;

					}
					String sendTime = showTime;

					String paramName = null == dynParmReportVo.getParamName() ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request) : dynParmReportVo
							.getParamName();
					String paranValue = null == dynParmReportVo.getPa() ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request) : dynParmReportVo.getPa();

					// 设置单元格内容
                    row = sheet.createRow(index++);
                    setRowData(row,cellStyle[0], (String[]) ArrayUtils.addAll(new String[]{sendTime,paramName,paranValue},dataArray));

				}

                row = sheet.createRow(index++);
                sumCountMap = ReportBiz.getRptNums(sumArray[0],sumArray[1],sumArray[2],sumArray[3],sumArray[4],RptStaticValue.DYN_RPT_CONF_MENU_ID);
                dataArray = getSumArrays(sumCountMap,colIds);
                setRowData(row,cellStyle[1], (String[]) ArrayUtils.addAll(new String[]{"","",name},dataArray));

                // 输出到xlsx文件
                os = new FileOutputStream(voucherFilePath + File.separator
                        + fileName);
                // 写入Excel对象
                workbook.write(os);
            }

			fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_rpt_zdybb", request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
			FileUtils.deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"自定义参数统计报表生成excel导出异常");
		} finally {
			// 清除对象
			workbook = null;
			if(os!=null){
				os.close();
			}
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
