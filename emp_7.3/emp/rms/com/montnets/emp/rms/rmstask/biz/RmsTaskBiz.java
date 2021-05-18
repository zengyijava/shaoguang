package com.montnets.emp.rms.rmstask.biz;

import com.montnets.EMPException;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.report.biz.RltcReportBiz;
import com.montnets.emp.rms.rmstask.dao.RmsTaskDao;
import com.montnets.emp.rms.rmstask.vo.DetailMtTaskVo;
import com.montnets.emp.rms.rmstask.vo.LfMttaskVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务层方法
 * @author Cheng
 * @date  2018-8-7 19:02:03
 */
public class RmsTaskBiz extends SuperBiz{
	private final RmsTaskDao rmsTaskDao = new RmsTaskDao();

	/**
	 * 获取结果
	 * @param mtVo vo对象
	 * @param pageInfo 分页对象 可以为null
	 * @return 结果集
	 */
	
    public List<LfMttaskVo> getMtTask(LfMttaskVo mtVo, PageInfo pageInfo) {
		List<LfMttaskVo> lfMttaskVos = new ArrayList<LfMttaskVo>();
		try {
			lfMttaskVos = rmsTaskDao.findMtTask(mtVo, pageInfo);
		}catch (Exception e){
			EmpExecutionContext.error(e, "企业富信-数据查询-"+ mtVo.getMenuName() +"查看异常！");
		}
		return lfMttaskVos;
	}

	/**
	 * 获取当前操作员对象
	 * @param request
	 * @return
	 */
	
    public LfSysuser getCurrenUser(HttpServletRequest request) {
		try {
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj == null) {
				return null;
			}
			return (LfSysuser)loginSysuserObj;
		}catch(Exception e) {
			EmpExecutionContext.error("从SESSION获取操作员对象失败。");
			return null;
		}
	}

	public List<LfSysuser> getAllSysusersOfSmsTaskRecordByDep(Long userId,Long depId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {
			if (null != userId) {
				lfSysuserList = new RmsTaskDao().findDomUserBySysuserIDOfSmsTaskRecordByDep(userId.toString(), depId.toString());
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询用户编号及机构编号对应关系异常。");
			throw e;
		}
		return lfSysuserList;
	}

	/**
	 * 获取树的方法
	 * @param depId 机构ID
	 * @param userId 操作员ID
	 * @param loginSysuser 当前登录操作员
	 * @return 返回生成树的字符串
	 */
	public String getDepartmentJosnData2(Long depId,Long userId,LfSysuser loginSysuser){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			//当前登录操作员
			sysuser = loginSysuser;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发历史或群发任务查询操作员信息异常！");
			tree = new StringBuffer("[]");
		}
		//判断当前登录操作员的权限，个人权限不能显示机构树
		if(sysuser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;

			try {
				lfDeps = null;
				 //10000企业查询
				 if ((sysuser != null) && (sysuser.getCorpCode().equals("100000"))) {
			          if (depId == null) {
			        	 //设置查询条件
			            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			            LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
					    conditionMap.put("depState", "1");
					    conditionMap.put("superiorId", "0");
			            orderbyMap.put("depId", "ASC");
			            orderbyMap.put("deppath", "ASC");
			            lfDeps = new BaseBiz().getByCondition(LfDep.class, conditionMap, orderbyMap);
			          } else {
			            lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			          }
			        }
			        else if (depId == null) {
			          lfDeps = new ArrayList();
			          //根据操作员ID和企业编码，查询操作员所管辖的机构
			          LfDep lfDep = (LfDep)depBiz.getAllDepByUserIdAndCorpCode(userId,sysuser.getCorpCode()).get(0);
			          lfDeps.add(lfDep);
			        }
			        else {
			          //根据机构ID、企业编码查询出所有子机构
			          lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,sysuser.getCorpCode());
			        }

				//组装字符串，返回前台
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()).append("'");
					tree.append(",dlevel:'").append(lfDep.getDepLevel()).append("'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			} catch (Exception e) {
				//异常信息记录日志
				EmpExecutionContext.error(e,"群发历史或群发任务操作员树加载biz层异常！");
				tree = new StringBuffer("[]");
			}
		}
		//返回值
		return tree.toString();
	}

	/**
	 * 根据任务Id查询滞留数
	 * @param taskids 任务Id集合
	 * @return
	 */
	
    public Map<Long, Long> getTaskRemains(String taskids){
		return rmsTaskDao.getTaskRemains(taskids);
	}

	
    public List<DetailMtTaskVo> getSendDetailMtTask(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
		List<DetailMtTaskVo> detailMtTaskVos = new ArrayList<DetailMtTaskVo>();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			//当前时间
			Calendar nowDate = Calendar.getInstance();
			nowDate.setTime(sdf.parse(sdf.format(new Date())));
			//发送时间
			Calendar sendDate = Calendar.getInstance();
			sendDate.setTime(sdf.parse(conditionMap.get("sendTime").substring(0,11)+"00:00:00"));
			sendDate.add(Calendar.DATE, 4);
			//计算时间，当前时间减去发送时间小于3天,查实时表GW_MT_TASK_BAK，否则查对应月的历史表
			if(sendDate.after(nowDate)){
				conditionMap.put("tableName","GW_MT_TASK_BAK");
			}else {
				sendDate.add(Calendar.DATE, -4);
				String month = sendDate.get(Calendar.MONTH) + 1 < 10 ? "0" + (sendDate.get(Calendar.MONTH) + 1):(sendDate.get(Calendar.MONTH) + 1)+"";
				String tableName = "MTTASK" + sendDate.get(Calendar.YEAR) + month;
				conditionMap.put("tableName",tableName);
			}
			detailMtTaskVos = rmsTaskDao.findSendDeatilMt(conditionMap, pageInfo);
		}catch (Exception e){
			EmpExecutionContext.error(e,"企业富信-群发历史查询-发送详情查看异常,Biz层查询异常！");
		}
		return detailMtTaskVos;
	}

	
    public void handleDetailMtTaskVo(List<DetailMtTaskVo> detailMtTaskVos, Map<String, String> langMap, Map<String, String> btnMap, boolean isDes) {
		CommonVariables cv = new CommonVariables();
		for(DetailMtTaskVo vo:detailMtTaskVos){
			switch (vo.getUnicom()){
				case 0:vo.setUnicomName(langMap.get("yd"));break;
				case 1:vo.setUnicomName(langMap.get("lt"));break;
				case 21:vo.setUnicomName(langMap.get("dx"));break;
				default:vo.setUnicomName(langMap.get("unknown"));
			}
			if(StringUtils.isEmpty(vo.getErrorCode()) || "".equals(vo.getErrorCode().trim())){
				if(isDes){
					vo.setSendStatus("--");
				}else {
					vo.setSendStatus(langMap.get("noReturn"));
				}
			}else if("DELIVRD".equals(vo.getErrorCode())){
				vo.setSendStatus(langMap.get("succeed"));
			}else {
				vo.setSendStatus(langMap.get("fail") + "[" + vo.getErrorCode() + "]");
			}

			if(StringUtils.isEmpty(vo.getErrorCode2()) || "".equals(vo.getErrorCode2().trim())){
				vo.setDownStatus(langMap.get("noReturn"));
			}else if("DELIVRD".equals(vo.getErrorCode2())){
				vo.setDownStatus(langMap.get("succeed"));
			}else {
				vo.setDownStatus(langMap.get("fail") + "[" + vo.getErrorCode2() + "]");
			}
			//处理手机号权限问题
			//手机号
			String phoneNum = cv.replacePhoneNumber(btnMap, vo.getPhone());
			vo.setPhone(phoneNum);
		}
	}

	
    public HashMap<String, String> createRptExcelFile(List<LfMttaskVo> mtVoList, String menuName, Map<String, String> langMap) throws Exception{
		//excel名字
		//menuName为rmsTask或者rmsTaskHis
		String excelName = "rmsTask".equals(menuName) ? "RmsTaskRpt" : "RmsTaskHisRpt";
		String baseDir = new TxtFileUtil().getWebRoot() + "rms/rmstask/file";
		// 创建Excel的工作书册 Workbook,对应到一个excel文档
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFCellStyle cellStyle2 = wb.createCellStyle();
		HashMap<String, String> resultMap = new HashMap<String, String>(16);
		// 当前每页分页条数
		int rowsOfPage = 500000;
		// 生成的工作薄个数
		int sheetSize = (mtVoList.size() % rowsOfPage == 0) ? (mtVoList.size() / rowsOfPage) : (mtVoList.size() / rowsOfPage + 1);
		// 产生报表文件的存储路径
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date();
		//实际生成文件路径
		String voucherFilePath = baseDir + File.separator + "download" + File.separator+"rmsTask"+File.separator + sdf.format(curDate);
		//模板文件名字
		String tempExcelName = "rmsTask".equals(menuName) ? "rms_sendedTaskRpt":"rms_sendedHisTaskRpt";
		String voucherTemplatePath = baseDir + File.separator + "temp" + File.separator + tempExcelName + "_" + langMap.get("langName") + ".xlsx";
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			boolean mkdirs = fileTemp.mkdirs();
			if(!mkdirs){
				throw new EMPException("企业富信>数据查询>群发任务/群发历史>导出功能实现异常，无法创建对应文件夹！");
			}
		}
		//判断模板文件是否存在，不存在则手动生成
		// 创建只读的Excel工作薄的对象, 此为模板文件
		File file = new File(voucherTemplatePath);
		if(!file.exists()){
			// 创建Excel的工作sheet,对应到一个excel文档的tab
			XSSFSheet xsheet = wb.createSheet("sheet1");
			XSSFFont font2 = wb.createFont();
			// 字体名称
			font2.setFontName("TAHOMA");
			font2.setBold(true);
			// 竖直对齐
			cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
			// 字体大小
			font2.setFontHeight(11);
			cellStyle2.setFont(font2);
			// 水平对齐
			cellStyle2.setAlignment(HorizontalAlignment.CENTER);
			cellStyle2.setWrapText(true);
			// 创建Excel的sheet的一行
			XSSFRow xrow = xsheet.createRow(0);
			XSSFCell[] cell;
			//群发任务
			if("rmsTask".equals(menuName)){
				cell = new XSSFCell[12];
			}else {
				cell = new XSSFCell[15];
			}

			for (int i = 0; i < cell.length; i++) {
				// 创建一个Excel的单元格
				cell[i]=xrow.createCell(i);
				// 设置单元格样式
				cell[i].setCellStyle(cellStyle2);
			}
			// 给Excel的单元格设置样式和赋值

			cell[0].setCellValue(langMap.get("operator"));
			cell[1].setCellValue(langMap.get("organization"));
			cell[2].setCellValue(langMap.get("spUser"));
			cell[3].setCellValue(langMap.get("busType"));
			if("rmsTask".equals(menuName)){
				cell[4].setCellValue(langMap.get("tempId"));
				cell[5].setCellValue(langMap.get("rmsTopic"));
				cell[6].setCellValue(langMap.get("taskId"));
				//cell[7].setCellValue(langMap.get("degree"));
				cell[7].setCellValue(langMap.get("validPhoneNum"));
				cell[8].setCellValue(langMap.get("taskStatus"));
				cell[9].setCellValue(langMap.get("createTime"));
				cell[10].setCellValue(langMap.get("sendTime"));
			}else {
				cell[4].setCellValue(langMap.get("rmsTopic"));
				cell[5].setCellValue(langMap.get("sendTopic"));
				cell[6].setCellValue(langMap.get("taskId"));
				cell[7].setCellValue(langMap.get("sendTime"));
				cell[8].setCellValue(langMap.get("sendStatus"));
				//cell[9].setCellValue(langMap.get("degree"));
				cell[9].setCellValue(langMap.get("phoneNum"));
				cell[10].setCellValue(langMap.get("succSend"));
				cell[11].setCellValue(langMap.get("failSubmit"));
				cell[12].setCellValue(langMap.get("receiveFail"));
				cell[13].setCellValue(langMap.get("delayNum"));
			}
			FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
			wb.write(xos);
			xos.close();
		}
		// 报表文件名
		String fileName = "";
		String zipName = null;
		String filePath = null;
		OutputStream os = null;
		XSSFWorkbook xssfWorkbook;
		SXSSFWorkbook sxssfWorkbook;
		for(int i = 0;i< sheetSize;i++){
			//文件名
			fileName = excelName +"_"+ sdf.format(curDate) + "_" + (i+1) + "_"+ StaticValue.getServerNumber() +".xlsx";
			//读取模板
			InputStream in = new FileInputStream(file);
			// 工作表
			xssfWorkbook = new XSSFWorkbook(in);
			sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook,10000);
			Sheet sheet = sxssfWorkbook.getSheetAt(i);
			sxssfWorkbook.setSheetName(i, excelName);
			// 表格样式
			XSSFCellStyle cellStyle = RltcReportBiz.setCellStyle(xssfWorkbook);
			in.close();
			// 读取模板工作表
			Row row;
			Cell[] cells;
			if("rmsTask".equals(menuName)){
				cells = new Cell[12];
			}else {
				cells = new Cell[15];
			}
			Integer k = 0;
			for(LfMttaskVo rpt : mtVoList) {
				row = sheet.createRow(k++ + 1);
				for (int j = 0; j < cells.length; j++) {
					cells[j] = row.createCell(j);
					cells[j].setCellStyle(cellStyle);
				}
				cells[0].setCellValue(StringUtils.defaultString(rpt.getName(),""));
				cells[1].setCellValue(StringUtils.defaultString(rpt.getDepName(),""));
				cells[2].setCellValue(StringUtils.defaultString(rpt.getSpUser(),""));
				cells[3].setCellValue(StringUtils.defaultString(rpt.getBusName(),""));
				if("rmsTask".equals(menuName)){
					cells[4].setCellValue(rpt.getTempId() == null ? 0L : rpt.getTempId());
					cells[5].setCellValue(StringUtils.defaultString(rpt.getTmName(),""));
					cells[6].setCellValue(rpt.getTaskId() == null ? 0L : rpt.getTaskId());
					//cells[7].setCellValue(rpt.getDegree() == null ? 0 + langMap.get("level"): rpt.getDegree() + langMap.get("level"));
					Long effNum = rpt.getTaskType() == 1 ? (rpt.getEffCount() == null ? 0L:rpt.getEffCount()) : 0L;
					cells[7].setCellValue(effNum);
					cells[8].setCellValue(StringUtils.defaultString(rpt.getSendStatus(),""));
					String createTime = rpt.getSubmitTime() == null ? "":rpt.getSubmitTime().toString().substring(0,19);
					cells[9].setCellValue(createTime);
					String sendTime = rpt.getTimerTime() == null ? "":rpt.getTimerTime().toString().substring(0,19);
					cells[10].setCellValue(sendTime);
				}else {
					cells[4].setCellValue(StringUtils.defaultString(rpt.getTmName(),""));
					cells[5].setCellValue(StringUtils.defaultString(rpt.getTitle(),""));
					cells[6].setCellValue(rpt.getTaskId() == null ? 0L : rpt.getTaskId());
					String sendTime = rpt.getTimerTime() == null ? "":rpt.getTimerTime().toString().substring(0,19);
					cells[7].setCellValue(StringUtils.defaultString(sendTime));
					cells[8].setCellValue(StringUtils.defaultString(rpt.getSendStatus(),""));
					//cells[9].setCellValue(rpt.getDegree() == null ? 0 + langMap.get("level"): rpt.getDegree() + langMap.get("level"));
					long effNum = rpt.getTaskType() == 1 ? (rpt.getEffCount() == null ? 0L:rpt.getEffCount()) : 0L;
					cells[9].setCellValue(effNum);
					String succSendNum = "";
					if(rpt.getSendState() != null){
						succSendNum = rpt.getSendState() == 2 ? "0" : String.valueOf(rpt.getIcount2() - rpt.getFaiCount());
					}
					cells[10].setCellValue(succSendNum);
					String failSubmit;
					String faileRecevNum;
					if(rpt.getSendState() == 2){
						failSubmit = rpt.getIcount() == null ? "":rpt.getIcount().toString();
						faileRecevNum = "0";
					}else{
						failSubmit = rpt.getIcount2() == null ? "":rpt.getFaiCount().toString();
						if(rpt.getIcount2() == null){
							faileRecevNum = "0";
						}else {
							faileRecevNum = rpt.getRfail2() == null ? "" : rpt.getRfail2().toString();
						}
					}
					cells[11].setCellValue(failSubmit);
					cells[12].setCellValue(faileRecevNum);
					cells[13].setCellValue(rpt.getDelayNum() == null ? 0L : rpt.getDelayNum());
				}
			}
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			sxssfWorkbook.write(os);
		}
		String zipTitle = "rmsTask".equals(menuName) ? langMap.get("rmsTask") : langMap.get("rmsHisTask");
				zipName  = zipTitle + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() + ".zip";
		filePath = baseDir + File.separator + "download" + File.separator + "rmsTask" + File.separator + zipName;
		ZipUtil.compress(voucherFilePath, filePath);
		//递归删除文件夹
        boolean flag = RltcReportBiz.deleteDir(fileTemp);
        if (!flag) {
            EmpExecutionContext.error("刪除文件失敗！");
        }
		//关闭资源
		if(os != null){
			os.close();
		}
		sxssfWorkbook = null;
		xssfWorkbook = null;
		resultMap.put("fileName", zipName);
		resultMap.put("filePath", filePath);
		return resultMap;
	}

	
    public void handleMtVoList(List<LfMttaskVo> mtVoList, Map<String, String> langMap) {
		//滞留条数
		Map<Long, Long> taskRemains = new HashMap<Long, Long>(16);
		//群发历史查询中增加滞留条数列
		StringBuilder taskIds = new StringBuilder();
		if(mtVoList != null && mtVoList.size() > 0){
			//遍历第一次取得所有的taskId集合
			for(LfMttaskVo mttaskVo:mtVoList){
				taskIds.append(",").append(mttaskVo.getTaskId());
			}
			if(taskIds.length()>0){
				taskIds.delete(0, 1);
				taskRemains = getTaskRemains(taskIds.toString());
			}
			//遍历第二次设置滞留数与任务状态
			for(LfMttaskVo vo:mtVoList){
				//设置滞留数
				if(taskRemains != null && taskRemains.size() > 0){
					Long delayNum = taskRemains.get(vo.getTaskId()) == null ? 0L : taskRemains.get(vo.getTaskId());
					vo.setDelayNum(delayNum);
				}
				//设置任务状态
				String taskStatus = "";
				if(vo.getSendState() == 1){
					taskStatus = langMap.get("succSubmit");
				}else if(vo.getSendState() == 2 && vo.getCurrentCount() > 0){
					taskStatus = langMap.get("partfail");
				}else if(vo.getSendState() == 2 && vo.getCurrentCount() == 0){
					taskStatus = langMap.get("allFail");
				}else if(vo.getTimerStatus() == 1 && vo.getSendState() == 0 && vo.getSubState() != 3){
					taskStatus = langMap.get("timer");
				} else if(vo.getTimerStatus() == 1 && vo.getSubState() == 3){
					taskStatus = langMap.get("cancelled");
				}else if(vo.getTimerStatus() == 1 && vo.getSendState() == 5){
					taskStatus = langMap.get("overtime");
				}else if(vo.getTimerStatus() == 1 && vo.getSubState() == 4){
					taskStatus = langMap.get("freezon");
				}
				vo.setSendStatus(taskStatus);
			}
		}
	}

	public HashMap<String, String> createRptExcelDetailFile(List<DetailMtTaskVo> detailMtTaskVos, Boolean rptFlag, Map<String, String> langMap) throws Exception{
		//excel名字
		String excelName = "RmsTaskSendDetailRpt";
		String baseDir = new TxtFileUtil().getWebRoot() + "rms/rmstask/file";
		HashMap<String, String> resultMap = new HashMap<String, String>(16);
		// 当前每页分页条数
		int rowsOfPage = 500000;
		// 生成的工作薄个数
		int sheetSize = (detailMtTaskVos.size() % rowsOfPage == 0) ? (detailMtTaskVos.size() / rowsOfPage) : (detailMtTaskVos.size() / rowsOfPage + 1);
		// 产生报表文件的存储路径
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 创建Excel的工作书册 Workbook,对应到一个excel文档
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFCellStyle cellStyle2 = wb.createCellStyle();
		Date curDate = new Date();
		//实际生成文件路径
		String voucherFilePath = baseDir + File.separator + "download" + File.separator+"TaskSendDetailRpt"+File.separator + sdf.format(curDate);
		//模板文件路径
		String voucherTemplatePath = baseDir + File.separator + "temp" + File.separator + "rms_taskSendDetailRpt_"+ langMap.get("langName") +".xlsx";
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			boolean mkdirs = fileTemp.mkdirs();
			if(!mkdirs){
				throw new EMPException("企业富信>数据查询>群发历史查询>发送详情查看导出功能实现异常，无法创建对应文件夹！");
			}
		}
		// 创建只读的Excel工作薄的对象, 此为模板文件
		File file = new File(voucherTemplatePath);
		if(file.exists()){
			if(!file.delete()){
				throw new EMPException("企业富信>数据查询>群发历史查询>发送详情查看导出功能实现异常，无法删除对应模板文件！");
			}
		}
		// 创建Excel的工作sheet,对应到一个excel文档的tab
		XSSFSheet xsheet = wb.createSheet("sheet1");
		XSSFFont font2 = wb.createFont();
		// 字体名称
		font2.setFontName("TAHOMA");
		font2.setBold(true);
		// 竖直对齐
		cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
		// 字体大小
		font2.setFontHeight(11);
		cellStyle2.setFont(font2);
		// 水平对齐
		cellStyle2.setAlignment(HorizontalAlignment.CENTER);
		cellStyle2.setWrapText(true);
		// 创建Excel的sheet的一行
		XSSFRow xrow = xsheet.createRow(0);
		XSSFCell[] cell;
		//群发任务
		if(rptFlag){
			cell = new XSSFCell[5];
		}else {
			cell = new XSSFCell[4];
		}
		for (int i = 0; i < cell.length; i++) {
			// 创建一个Excel的单元格
			cell[i]=xrow.createCell(i);
			// 设置单元格样式
			cell[i].setCellStyle(cellStyle2);
		}
		// 给Excel的单元格设置样式和赋值
		cell[0].setCellValue(langMap.get("number"));
		cell[1].setCellValue(langMap.get("unicom"));
		cell[2].setCellValue(langMap.get("phone"));
		cell[3].setCellValue(langMap.get("sendStatus"));
		if(rptFlag){
			cell[4].setCellValue(langMap.get("downStatus"));
		}
		FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
		wb.write(xos);
		xos.close();

		// 报表文件名
		String fileName = "";
		String zipName = null;
		String filePath = null;
		OutputStream os = null;
		XSSFWorkbook xssfWorkbook;
		SXSSFWorkbook sxssfWorkbook;
		for(int i = 0;i< sheetSize;i++){
			//文件名
			fileName = excelName +"_"+ sdf.format(curDate) + "_" + (i+1) + "_"+ StaticValue.getServerNumber() +".xlsx";
			//读取模板
			InputStream in = new FileInputStream(file);
			// 工作表
			xssfWorkbook = new XSSFWorkbook(in);
			sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook,10000);
			Sheet sheet = sxssfWorkbook.getSheetAt(i);
			sxssfWorkbook.setSheetName(i, excelName);
			// 表格样式
			XSSFCellStyle cellStyle = RltcReportBiz.setCellStyle(xssfWorkbook);
			in.close();
			// 读取模板工作表
			Row row;
			Cell[] cells;
			if(rptFlag){
				cells = new Cell[5];
			}else {
				cells = new Cell[4];
			}
			Integer k = 0;
			for(DetailMtTaskVo rpt : detailMtTaskVos) {
				row = sheet.createRow(k++ + 1);
				for (int j = 0; j < cells.length; j++) {
					cells[j] = row.createCell(j);
					cells[j].setCellStyle(cellStyle);
				}
				cells[0].setCellValue(k);
				cells[1].setCellValue(StringUtils.defaultIfEmpty(rpt.getUnicomName(),""));
				cells[2].setCellValue(StringUtils.defaultIfEmpty(rpt.getPhone(),""));
				cells[3].setCellValue(StringUtils.defaultIfEmpty(rpt.getSendStatus(),""));
				if(rptFlag){
					cells[4].setCellValue(StringUtils.defaultIfEmpty(rpt.getDownStatus(),""));
				}
			}
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			sxssfWorkbook.write(os);
		}
		String zipTitle = langMap.get("rmsTaskTitle");
		zipName  = zipTitle + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() + ".zip";
		filePath = baseDir + File.separator + "download" + File.separator + "TaskSendDetailRpt" + File.separator + zipName;
		ZipUtil.compress(voucherFilePath, filePath);
		//递归删除文件夹
        boolean flag = RltcReportBiz.deleteDir(fileTemp);
        if (!flag) {
            EmpExecutionContext.error("刪除文件失敗！");
        }
		//关闭资源
		if(os != null){
			os.close();
		}
		sxssfWorkbook = null;
		xssfWorkbook = null;
		resultMap.put("fileName", zipName);
		resultMap.put("filePath", filePath);
		return resultMap;
	}
}
