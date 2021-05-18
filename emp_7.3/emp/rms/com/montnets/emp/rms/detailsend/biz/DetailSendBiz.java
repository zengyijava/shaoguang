package com.montnets.emp.rms.detailsend.biz;

import com.montnets.EMPException;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.accountpower.LfMtPri;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.detailsend.dao.RmsMtRecordDao;
import com.montnets.emp.rms.report.biz.OperatorDegreeRptBiz;
import com.montnets.emp.rms.vo.RmsMtRecordVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 企业富信-数据查询-发送明细查询的biz
 */
public class DetailSendBiz extends SuperBiz {
    /**
     * 企业顶级机构map，查询到的企业顶级机构放这里，key为企业编码，value为企业的顶级机构对象。
     */
    private static final ConcurrentHashMap<String, LfDep> topDepMap = new ConcurrentHashMap<String, LfDep>();
    private final RmsMtRecordDao mtRecordDao = new RmsMtRecordDao();
    private final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 从session中获取当前操作员对象
     *
     * @param request
     */
    public LfSysuser getCurrenUser(HttpServletRequest request) {
        try {
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj == null) {
                return null;
            }
            return (LfSysuser) loginSysuserObj;
        } catch (Exception e) {
            EmpExecutionContext.error("从SESSION获取操作员对象失败。");
            return null;
        }
    }

    /**
     * @param sysUser 操作员对象
     * @return 返回有权限看的操作员编码，格式为code1,code2；不需要考虑权限则返回空字符串，目前只有100000企业、admin和管辖范围是顶级机构不需要考虑权限；编码超过1000个、异常返回null
     * @description 获取有权限看的操作员编码
     */
    public String getPermissionUserCode(LfSysuser sysUser) {
        if ("100000".equals(sysUser.getCorpCode())) {
            return "";
        }
        //如果是admin管理员，则默认查全部
        else if ("admin".equals(sysUser.getUserName())) {
            return "";
        }
        //如果是个人权限，则只能查自己的。权限类型 1：个人权限  2：机构权限
        else if (sysUser.getPermissionType() == 1) {
            //返回当前操作员的编码
            return "'" + sysUser.getUserCode() + "'";
        }

        //机构权限，则需要查询出当前操作员可管辖的所有操作员。
        try {
            //如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码
            boolean domIsTopDep = checkDomIsTopDep(sysUser);
            if (domIsTopDep) {
                return "";
            }

            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("userCode", StaticValue.ASC);
            //查询有权限看的操作员
            List<LfSysuser> userList = empDao.findListBySymbolsCondition(sysUser.getUserId(), LfSysuser.class, null, orderbyMap);
            //没找到其他操作员，那就只能看他自己的
            if (userList == null || userList.size() < 1) {
                //返回当前操作员的编码
                return "'" + sysUser.getUserCode() + "'";
            }
            //操作员编码超过1000，则不使用in方式拼接查询
            if (userList.size() > 1000) {
                return null;
            }

            StringBuilder sbUserCode = new StringBuilder();
            for (int i = 0; i < userList.size(); i++) {
                sbUserCode.append("'").append(userList.get(i).getUserCode()).append("'");
                if (i < userList.size() - 1) {
                    sbUserCode.append(",");
                }
            }

            return sbUserCode.toString();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询，获取有权限看的操作员编码，异常"
                    + "。userCode=" + sysUser.getUserCode()
                    + ",userId=" + sysUser.getUserId()
                    + ",userName=" + sysUser.getUserName()
                    + ",corpCode=" + sysUser.getCorpCode()
            );
            //返回当前操作员的编码
            return null;
        }
    }

    /**
     * @param sysUser 操作员对象
     * @return true：管辖的机构即为企业顶级机构；false：不是顶级机构，或者没能找到，或者异常。
     * @description 检查管辖机构是否就是企业顶级机构
     */
    private boolean checkDomIsTopDep(LfSysuser sysUser) {
        //如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码

        //获取企业最顶级机构
        LfDep topDep = getTopDep(sysUser.getCorpCode());
        //没能找到企业顶级机构
        if (topDep == null) {
            EmpExecutionContext.error("企业富信-数据查询-发送明细查询，检查管辖机构是否就是企业顶级机构，获取不到顶级机构对象。corpCode=" + sysUser.getCorpCode());
            return false;
        }
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userId", sysUser.getUserId().toString());
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("depId", StaticValue.ASC);
            List<LfDomination> domList = empDao.findListByCondition(LfDomination.class, conditionMap, orderbyMap);
            if (domList == null || domList.size() < 1) {
                return false;
            }

            for (LfDomination lfDom : domList) {
                //管辖的机构即为企业顶级机构
                if (lfDom.getDepId().equals(topDep.getDepId())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询，检查管辖机构是否就是企业顶级机构，异常。");
            return false;
        }
    }

    /**
     * @param corpCode 企业编码
     * @return 企业顶级机构对象，没找到或者异常则返回null
     * @description 获取企业顶级机构
     */
    private LfDep getTopDep(String corpCode) {
        try {
            //企业顶级机构先从内存中拿
            if (topDepMap.get(corpCode) != null) {
                return topDepMap.get(corpCode);
            }

            //获取企业最顶级机构
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corpCode);
            //顶级机构
            conditionMap.put("depLevel", "1");
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("depId", StaticValue.ASC);
            List<LfDep> depList = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
            if (depList == null || depList.size() < 1) {
                EmpExecutionContext.error("企业富信-数据查询-发送明细查询，获取企业顶级机构，查询为空。corpCode=" + corpCode);
                return null;
            }

            //企业顶级机构放到内存中
            topDepMap.put(corpCode, depList.get(0));

            return depList.get(0);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询，获取企业顶级机构，异常。");
            return null;
        }
    }

    /**
     * @param sysUser 操作员对象
     * @return 返回有权限看的SP账号，格式为spuser1,spuser2；不需要考虑权限则返回空字符串，目前只有100000企业、admin和管辖范围是顶级机构不需要考虑权限；编码超过1000个、异常返回null
     * @description 获取有权限看的SP账号
     */
    public String getPermissionSpuserMtpri(LfSysuser sysUser) {
        if ("100000".equals(sysUser.getCorpCode())) {
            return "";
        } else if ("admin".equals(sysUser.getUserName())) {
            //如果是admin管理员，则默认查全部
            return "";
        }
        //机构权限，则需要查询出当前操作员可管辖的所有操作员。
        try {
            //如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码
            boolean domIsTopDep = checkDomIsTopDep(sysUser);
            if (domIsTopDep) {
                return "";
            }
            LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
            conditionmap.put("userid", sysUser.getUserId() + "");
            conditionmap.put("corpcode", sysUser.getCorpCode());
            //查询有权限看的sp账号
            List<LfMtPri> mtpris = empDao.findListByCondition(LfMtPri.class, conditionmap, null);
            //没找到其他操作员，那就只能看他自己的
            if (mtpris == null || mtpris.size() < 1) {
                //返回当前操作员的编码
                return "";
            }
            //操作员编码超过1000，则不使用in方式拼接查询
            if (mtpris.size() > 1000) {
                return null;
            }

            StringBuilder spusers = new StringBuilder();
            for (int i = 0; i < mtpris.size(); i++) {
                spusers.append("'").append(mtpris.get(i).getSpuserid()).append("'");
                if (i < mtpris.size() - 1) {
                    spusers.append(",");
                }
            }
            return spusers.toString();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "系统下行记录，获取有权限看的SP账号，异常"
                    + "。userCode=" + sysUser.getUserCode()
                    + ",userId=" + sysUser.getUserId()
                    + ",userName=" + sysUser.getUserName()
                    + ",corpCode=" + sysUser.getCorpCode()
            );
            //返回当前操作员的编码
            return null;
        }
    }

    /**
     * 根据conditionMap查询下行记录
     *
     * @param conditionMap 查询条件集合
     * @param pageInfo     分页信息对象
     * @return
     */
    public List<RmsMtRecordVo> getRmsMtRecords(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
        List<RmsMtRecordVo> rmsMtRecordVos = new ArrayList<RmsMtRecordVo>();
        try {
            //查询类型
            String recordType = conditionMap.get("recordType");
            if ("history".equals(recordType)) {
                // 查询历史记录
                //设置时间查询条件
                setTimeCondition(conditionMap, recordType);
                rmsMtRecordVos = mtRecordDao.findRecordHis(conditionMap, pageInfo);
            } else if ("realTime".equals(recordType)) {
                // 查询实时记录
                //设置时间查询条件
                setTimeCondition(conditionMap, recordType);
                rmsMtRecordVos = mtRecordDao.findRecordReal(conditionMap, pageInfo);
            } else {
                throw new EMPException("企业富信-数据查询-发送明细查询，获取查询类型异常,只能选择实时查询或历史查询！");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, e.getMessage());
        }
        return rmsMtRecordVos;
    }

    /**
     * 根据查询类型调整查询时间
     *
     * @param conditionMap
     * @param recordType
     */
    private void setTimeCondition(LinkedHashMap<String, String> conditionMap, String recordType) throws Exception {
        try {
            String startTime = "";
            String endTime = "";
            String start = conditionMap.get("sendTime");
            String end = conditionMap.get("recvTime");
            Calendar now = Calendar.getInstance();
            if ("realTime".equals(recordType)) {
                //实时查询
                //1.如果开始查询时间为空而结束时间不为空，则设置为end-3:end
                if (StringUtils.isEmpty(start) && StringUtils.isNotEmpty(end)) {
                    Date endDate = yyyyMMdd.parse(end);
                    now.setTime(endDate);
                    now.set(Calendar.DATE, now.get(Calendar.DATE) - 3);
                    startTime = yyyyMMdd.format(now.getTime());
                    conditionMap.put("sendTime", startTime);
                }
                //2.结束时间为空，开始时间不为空。结束时间即为当前系统时间
                if (StringUtils.isEmpty(end) && StringUtils.isNotEmpty(start)) {
                    endTime = yyyyMMdd.format(now.getTime());
                    conditionMap.put("recvTime", endTime);
                }
                //3.都为空则为当前时间减三天到当前时间
                if (StringUtils.isEmpty(end) && StringUtils.isEmpty(start)) {
                    endTime = yyyyMMdd.format(now.getTime());
                    now.set(Calendar.DATE, now.get(Calendar.DATE) - 3);
                    startTime = yyyyMMdd.format(now.getTime());
                    conditionMap.put("sendTime", startTime);
                    conditionMap.put("recvTime", endTime);
                }
            }
            if ("history".equals(recordType)) {
                //1.开始为空，结束不为空   开始时间设置为结束时间月的1号0时0分0秒
                if (StringUtils.isEmpty(start) && StringUtils.isNotEmpty(end)) {
                    Date endDate = yyyyMMdd.parse(end);
                    now.setTime(endDate);
                    now.set(Calendar.DAY_OF_MONTH, 1);
                    now.set(Calendar.HOUR_OF_DAY, 0);
                    now.set(Calendar.MINUTE, 0);
                    now.set(Calendar.SECOND, 0);
                    startTime = yyyyMMdd.format(now.getTime());
                    conditionMap.put("sendTime", startTime);
                }
                //2.结束时间为空，开始时间不为空。结束时间即为开始时间月的最后一天的23:59:59
                if (StringUtils.isEmpty(end) && StringUtils.isNotEmpty(start)) {
                    Date startDate = yyyyMMdd.parse(start);
                    now.setTime(startDate);
                    now.set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH));
                    now.set(Calendar.HOUR_OF_DAY, 23);
                    now.set(Calendar.MINUTE, 59);
                    now.set(Calendar.SECOND, 59);
                    now.set(Calendar.MILLISECOND, 999);
                    endTime = yyyyMMdd.format(now.getTime());
                    conditionMap.put("recvTime", endTime);
                }
                //3.都为空则start为当前月1号0时0分0秒，end为当前月最后一天
                if (StringUtils.isEmpty(end) && StringUtils.isEmpty(start)) {
                    now.set(Calendar.DAY_OF_MONTH, 1);
                    now.set(Calendar.HOUR_OF_DAY, 0);
                    now.set(Calendar.MINUTE, 0);
                    now.set(Calendar.SECOND, 0);
                    startTime = yyyyMMdd.format(now.getTime());
                    conditionMap.put("sendTime", startTime);

                    now.set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH));
                    now.set(Calendar.HOUR_OF_DAY, 23);
                    now.set(Calendar.MINUTE, 59);
                    now.set(Calendar.SECOND, 59);
                    now.set(Calendar.MILLISECOND, 999);
                    endTime = yyyyMMdd.format(now.getTime());
                    conditionMap.put("recvTime", endTime);
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询-根据查询类型调整查询时间异常");
            throw e;
        }
    }

    /**
     * 获取企业绑定发送账号。多企业时会去获取企业绑定发送账号，单企业则会返回null
     *
     * @param corpCode 企业编码
     * @return 返回企业绑定的发送账号，格式如sp1,sp2,sp3...。单企业直接返回null
     */
    public String getCorpBindSpusers(String corpCode) {
        //单企业则不需要拿绑定账号
        if (StaticValue.getCORPTYPE() != 1) {
            return null;
        }
        if (corpCode == null || corpCode.trim().length() < 1) {
            EmpExecutionContext.error("获取企业绑定发送账号，企业编码为空。");
            return null;
        }
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //不是十万的企业。如果是100000则查询所有企业发送账号
            if (!"100000".equals(corpCode)) {
                conditionMap.put("corpCode", corpCode);
            }

            // 短信账号
            List<LfSpDepBind> lfsp = empDao.findListByCondition(LfSpDepBind.class, conditionMap, null);
            if (lfsp == null || lfsp.size() < 1) {
                EmpExecutionContext.info("下行记录查询，企业未绑定SP账号。corpcode=" + corpCode);
                return null;
            }

            StringBuilder bindSpUsers = new StringBuilder();
            for (int i = 0; i < lfsp.size(); i++) {
                bindSpUsers.append("'").append(lfsp.get(i).getSpUser().toUpperCase()).append("'");
                if (i < lfsp.size() - 1) {
                    bindSpUsers.append(",");
                }
            }
            return bindSpUsers.toString();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取企业绑定发送账号，异常。corpCode=" + corpCode);
            return null;
        }
    }

    /**
     * 发送明细报表导出
     *
     * @param rmsMtRecords rmsMtRecords集合
     * @return 返回包含文件名字与路径的Map
     */
    public HashMap<String, String> createRptExcelFile(List<RmsMtRecordVo> rmsMtRecords, String isRptFlag, Map<String, String> map) throws Exception {
        //excel名字
        String excelName = "RmsSendDetailRpt";
        String baseDir = new TxtFileUtil().getWebRoot() + "rms/detailsend/file";
        HashMap<String, String> resultMap = new HashMap<String, String>(16);
        // 当前每页分页条数
        int rowsOfPage = 500000;
        // 生成的工作薄个数
        int sheetSize = (rmsMtRecords.size() % rowsOfPage == 0) ? (rmsMtRecords.size() / rowsOfPage) : (rmsMtRecords.size() / rowsOfPage + 1);
        // 产生报表文件的存储路径
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date();
        //实际生成文件路径
        String voucherFilePath = baseDir + File.separator + "download" + File.separator + "sendDetailRpt" + File.separator + sdf.format(curDate);
        //模板文件路径
        String voucherTemplatePath = baseDir + File.separator + "temp" + File.separator + "rms_sendDetailRpt.xlsx";
        File fileTemp = new File(voucherFilePath);
        if (!fileTemp.exists()) {
            boolean mkdirs = fileTemp.mkdirs();
            if (!mkdirs) {
                throw new EMPException("企业富信>数据查询>群发任务查看>导出功能实现异常，无法创建对应文件夹！");
            }
        }
        // 创建只读的Excel工作薄的对象, 此为模板文件
        File file = new File(voucherTemplatePath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        if (file.exists()) {
            if (!file.delete()) {
                EmpExecutionContext.error("删除原有已存在的模板文件发生错误！");
            }
        }
        boolean state = file.createNewFile();
        if (!state) {
            EmpExecutionContext.error("创建文件失败");
        }

        // 创建Excel的工作书册 Workbook,对应到一个excel文档
        XSSFWorkbook wb = new XSSFWorkbook();
        // 创建Excel的工作sheet,对应到一个excel文档的tab
        XSSFSheet xsheet = wb.createSheet("sheet1");
        XSSFCellStyle cellStyle2 = wb.createCellStyle();
        XSSFFont font2 = wb.createFont();
        // 字体名称
        font2.setFontName("TAHOMA");
        font2.setBold(true);
        // 字体大小
        font2.setFontHeight(11);
        cellStyle2.setFont(font2);
        // 竖直对齐
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平对齐
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setWrapText(true);
        // 创建Excel的sheet的一行
        XSSFRow xrow = xsheet.createRow(0);
        XSSFCell[] cell = new XSSFCell[17];

        for (int i = 0; i < cell.length; i++) {
            // 创建一个Excel的单元格
            cell[i] = xrow.createCell(i);
            // 设置单元格样式
            cell[i].setCellStyle(cellStyle2);
        }
        // 给Excel的单元格设置样式和赋值
        cell[0].setCellValue(map.get("spAccount"));
        cell[1].setCellValue(map.get("spGate"));
        cell[2].setCellValue(map.get("sendTopic"));
        cell[3].setCellValue(map.get("busType"));
        cell[4].setCellValue(map.get("fxTopic"));
        cell[5].setCellValue(map.get("sceneId"));
        cell[6].setCellValue(map.get("degree"));
        cell[7].setCellValue(map.get("operator"));
        cell[8].setCellValue(map.get("phone"));
        cell[9].setCellValue(map.get("taskbatch"));
        cell[10].setCellValue(map.get("recstatus"));
        if (!"3".equals(isRptFlag)) {
            cell[11].setCellValue(map.get("sendtime"));
            cell[12].setCellValue(map.get("recetime"));
            cell[13].setCellValue(map.get("selfseq"));
            cell[14].setCellValue(map.get("optseq"));
        } else {
            cell[11].setCellValue(map.get("dlstatus"));
            cell[12].setCellValue(map.get("sendtime"));
            cell[13].setCellValue(map.get("recetime"));
            cell[14].setCellValue(map.get("dltime"));
            cell[15].setCellValue(map.get("selfseq"));
            cell[16].setCellValue(map.get("optseq"));
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
        for (int i = 0; i < sheetSize; i++) {
            //文件名
            fileName = excelName + "_" + sdf.format(curDate) + "_" + (i + 1) + "_" + StaticValue.getServerNumber() + ".xlsx";
            //读取模板
            InputStream in = new FileInputStream(file);
            // 工作表
            xssfWorkbook = new XSSFWorkbook(in);
            sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook, 10000);
            Sheet sheet = sxssfWorkbook.getSheetAt(i);
            sxssfWorkbook.setSheetName(i, excelName);
            // 表格样式
            XSSFCellStyle cellStyle = new OperatorDegreeRptBiz().setCellStyle(xssfWorkbook);
            in.close();
            // 读取模板工作表
            Row row;
            Cell[] cells = new Cell[17];
            Integer k = 0;
            for (RmsMtRecordVo vo : rmsMtRecords) {
                row = sheet.createRow(k++ + 1);
                for (int j = 0; j < cells.length; j++) {
                    cells[j] = row.createCell(j);
                    cells[j].setCellStyle(cellStyle);
                }
                cells[0].setCellValue(StringUtils.defaultIfEmpty(vo.getSpUser(), ""));
                cells[1].setCellValue(StringUtils.defaultIfEmpty(vo.getSpGate(), ""));
                cells[2].setCellValue(StringUtils.defaultIfEmpty(vo.getSendSubject(), ""));
                cells[3].setCellValue(StringUtils.defaultIfEmpty(vo.getBusTypeName(), ""));
                cells[4].setCellValue(StringUtils.defaultIfEmpty(vo.getRmsSubject(), ""));
                cells[5].setCellValue(vo.getTmplId() == null ? "" : vo.getTmplId().toString());
                cells[6].setCellValue((vo.getDegree() == null ? "" : vo.getDegree().toString() + "档"));
                cells[7].setCellValue(StringUtils.defaultIfEmpty(vo.getUnicomName(), ""));
                cells[8].setCellValue(StringUtils.defaultIfEmpty(vo.getPhone(), ""));
                cells[9].setCellValue(vo.getTaskId() == null ? "" : vo.getTaskId().toString());
                cells[10].setCellValue(StringUtils.defaultIfEmpty(vo.getReceStatus(), ""));
                if (!"3".equals(isRptFlag)) {
                    cells[11].setCellValue(StringUtils.defaultIfEmpty(vo.getSendTime(), ""));
                    cells[12].setCellValue(StringUtils.defaultIfEmpty(vo.getRecvTime(), ""));
                    cells[13].setCellValue(StringUtils.defaultIfEmpty(vo.getCustId(), ""));
                    cells[14].setCellValue(vo.getPtmsgid() == null ? "" : vo.getPtmsgid().toString());
                } else {
                    cells[11].setCellValue(StringUtils.defaultIfEmpty(vo.getDownStatus(), ""));
                    cells[12].setCellValue(StringUtils.defaultIfEmpty(vo.getSendTime(), ""));
                    cells[13].setCellValue(StringUtils.defaultIfEmpty(vo.getRecvTime(), ""));
                    cells[14].setCellValue(StringUtils.defaultIfEmpty(vo.getDownTime(), ""));
                    cells[15].setCellValue(StringUtils.defaultIfEmpty(vo.getCustId(), ""));
                    cells[16].setCellValue(vo.getPtmsgid() == null ? "" : vo.getPtmsgid().toString());
                }
            }
            os = new FileOutputStream(voucherFilePath + File.separator + fileName);
            // 写入Excel对象
            sxssfWorkbook.write(os);
        }
        zipName = "发送明细详情查询" + sdf.format(curDate) + "_" + StaticValue.getServerNumber() + ".zip";
        filePath = baseDir + File.separator + "download" + File.separator + "sendDetailRpt" + File.separator + zipName;
        ZipUtil.compress(voucherFilePath, filePath);
        //递归删除文件夹
        boolean flag = OperatorDegreeRptBiz.deleteDir(fileTemp);
        if (!flag) {
            EmpExecutionContext.error("刪除文件失敗！");
        }
        //关闭资源
        if (os != null) {
            os.close();
        }
        sxssfWorkbook = null;
        xssfWorkbook = null;
        resultMap.put("fileName", zipName);
        resultMap.put("filePath", filePath);
        return resultMap;
    }
}
