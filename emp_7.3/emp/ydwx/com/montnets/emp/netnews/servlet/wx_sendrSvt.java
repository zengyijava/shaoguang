package com.montnets.emp.netnews.servlet;

import com.montnets.emp.common.atom.AddrBookAtom;
import com.montnets.emp.common.atom.SendSmsAtom;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sms.LfDrafts;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfOpratelog;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.biz.WXSendBiz;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.common.CompressEncodeing;
import com.montnets.emp.netnews.daoImpl.Wx_ueditorDaoImpl;
import com.montnets.emp.netnews.entity.LfDfadvanced;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.netnews.entity.LfWXTrustCols;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.*;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class wx_sendrSvt extends BaseServlet {


    /**
     *
     */
    private static final long serialVersionUID = -2489247682395829133L;

    //	LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
    private final BaseBiz baseBiz = new BaseBiz();
    final Wx_ueditorDaoImpl ueditorDao = new Wx_ueditorDaoImpl();
    final ResourceBundle bundle = ResourceBundle.getBundle("resourceBundle");
    private final String MovePhoneHead = bundle
            .getString("montnets.wx.MovePhoneHead"); // ??????
    private final String MovePhoneHeadURL = bundle
            .getString("montnets.wx.MovePhoneHead.url"); // ??????

    private final String UnionPhoneHead = bundle
            .getString("montnets.wx.UnionPhoneHead"); // ???????????????
    private final String UnionPhoneHeadURL = bundle
            .getString("montnets.wx.UnionPhoneHead.url"); // ???????????????

    private final String TelPhoneHead1 = bundle
            .getString("montnets.wx.TelPhoneHead1"); // ??????
    private final String TelPhoneHead1URL = bundle
            .getString("montnets.wx.TelPhoneHead1.url"); // ??????

    private final String TelPhone = bundle.getString("montnets.wx.Phone");
//	String uploadpath = bundle.getString("montnets.wx.filepath");


    final String empRoot = "ydwx";
    final SmsBiz smsBiz = new SmsBiz();
    private final WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();
    private final WXSendBiz wxSendBiz = new WXSendBiz();
    final BalanceLogBiz biz = new BalanceLogBiz();
    private final TxtFileUtil txtfileutil = new TxtFileUtil();
    private final AddrBookAtom addrBookAtom = new AddrBookAtom();
    private final PhoneUtil phoneUtil = new PhoneUtil();
    private final BlackListAtom blBiz = new BlackListAtom();
    final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

    public void find(HttpServletRequest request, HttpServletResponse response) {
        String lgcorpcode = null;
        Long lguserid = null;
        try {
            //??????????????????
            lgcorpcode = request.getParameter("lgcorpcode");
            //?????????????????????id
            //???????????? session????????????????????????
            if (request.getSession(false) == null || request.getSession(false).getAttribute("loginSysuser") == null) {
                lguserid = Long.parseLong(request.getParameter("lguserid"));
            } else {
                LfSysuser sysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
                lguserid = sysUser.getUserId();
            }

//			if(request.getParameter("lguserid")!=null)
//			{
//				lguserid = Long.valueOf(request.getParameter("lguserid"));
//			}else
            if (lguserid == null) {
                EmpExecutionContext.info("lguserid?????????");
                request.setAttribute("findresult", "-1");
                try {
                    request.getRequestDispatcher(empRoot + "/sendWX/wx_send.jsp").forward(request, response);
                    return;
                } catch (ServletException e1) {
                    EmpExecutionContext.error(e1, lguserid, lgcorpcode);
                    return;
                } catch (IOException e1) {
                    EmpExecutionContext.error(e1, lguserid, lgcorpcode);
                    return;
                }
            }

            //???????????????????????????
            LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userId", curSysuser.getUserName());
            //???????????????????????????
            List<LfFlow> flowList = baseBiz.getByCondition(LfFlow.class,
                    conditionMap, null);

            if (flowList != null && flowList.size() > 0) {
                request.setAttribute("isFlow", "true");
            } else {
                request.setAttribute("isFlow", "false");
            }

            LinkedHashMap<String, String> conditionbusMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
            conditionbusMap.put("corpCode&in", "0," + lgcorpcode);
            orconp.put("corpCode", "asc");

            //????????????????????????
            conditionbusMap.put("state", "0");
            //???????????????????????????+??????
            conditionbusMap.put("busType&in", "0,2");

            //????????????????????????????????????
            List<LfBusManager> busList = baseBiz.getByCondition(
                    LfBusManager.class, conditionbusMap, orconp);

            request.setAttribute("busList", busList);

            conditionMap.clear();
            if (busList != null && busList.size() > 0) {

            }
            conditionMap.put("platFormType", "1");
            conditionMap.put("corpCode", lgcorpcode);
            conditionMap.put("isValidate", "1");
            // ??????????????????????????????bindType

            List<Userdata> spUserList = smsBiz.getSpUserList(curSysuser);
            request.setAttribute("sendUserList", spUserList);

            //------------------------------begin??????????????????
            conditionMap.clear();
            //????????????
            conditionMap.put("tmpType", "3");
            //??????
            conditionMap.put("tmState", "1");
            //???????????????????????????
            conditionMap.put("isPass&in", "0,1");
            //????????????
            conditionMap.put("dsflag", "0");

            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            //??????id
            orderbyMap.put("tmid", "asc");
            //??????????????????????????????
            List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class, lguserid, conditionMap, orderbyMap);
            if (tmpList != null && tmpList.size() > 0) {
                request.setAttribute("tmpList", tmpList);
            }
            LfDep dep = baseBiz.getById(LfDep.class, curSysuser.getDepId());
            if (dep != null) {
                request.setAttribute("depSign", dep.getDepName());
            }
            //??????taskId
            CommonBiz commonBiz = new CommonBiz();
            Long taskId = commonBiz.getAvailableTaskId();
            String opContent = "??????taskid(" + taskId + ")??????";
            setLog(request, "??????????????????", opContent, StaticValue.GET);
            request.setAttribute("taskId", taskId.toString());
            request.setAttribute("lguserid", lguserid);

            //??????????????????????????????
            conditionMap.clear();
            conditionMap.put("userid", String.valueOf(lguserid));
            //7?????????????????????
            conditionMap.put("flag", "7");
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("id", StaticValue.DESC);
            List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);
            LfDfadvanced lfDfadvanced = null;
            if (lfDfadvancedList != null && lfDfadvancedList.size() > 0) {
                lfDfadvanced = lfDfadvancedList.get(0);
            }
            request.setAttribute("lfDfadvanced", lfDfadvanced);

            request.getRequestDispatcher(empRoot + "/sendWX/wx_send.jsp").forward(
                    request, response);

        } catch (Exception e) {
            EmpExecutionContext.error(e, lguserid, lgcorpcode);
            request.setAttribute("findresult", "-1");
            try {
                request.getRequestDispatcher(empRoot + "/sendWX/wx_send.jsp").forward(
                        request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1, lguserid, lgcorpcode);
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, lguserid, lgcorpcode);
            }
        }
    }


    // ?????????????????????????????????
    public void getLfTemplateBySms(HttpServletRequest request, HttpServletResponse response) {
        //????????????vo
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        PageInfo pageInfo = new PageInfo();

        try {
            pageSet(pageInfo, request);
            //????????????
            String tmName = request.getParameter("tmName");
            //????????????
            String tmMsg = request.getParameter("tmMsg");
            //??????????????????id
            String lgcorpcode = request.getParameter("lgcorpcode");
            //Long lguserid = Long.valueOf(request.getParameter("lguserid"));
            //???????????? session????????????????????????
            Long lguserid = SysuserUtil.longLguserid(request);


            Integer pageSize = 5;
            if (StringUtils.isNotEmpty(request.getParameter("pageSize"))) {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
            }
            if (tmName != null) {
                tmName = tmName.trim();
            }
            if (tmMsg != null) {
                tmMsg = tmMsg.trim();
            }
            tmName = (tmName == null ? "" : tmName);
            tmMsg = (tmMsg == null ? "" : tmMsg);
            conditionMap.put("tmName&like", tmName);
            conditionMap.put("tmMsg&like", tmMsg);
            conditionMap.put("corpCode", lgcorpcode);
            conditionMap.put("tmpType", "3");
            conditionMap.put("tmState", "1");
            conditionMap.put("isPass&in", "0,1");
            conditionMap.put("dsflag", request.getParameter("dsflag"));

            pageSize = 5;
            //?????????????????????
            pageInfo.setPageSize(pageSize);
            List<LfTemplate> temList = baseBiz.getByConditionNoCount(LfTemplate.class, lguserid, conditionMap, null, pageInfo);
            //????????????????????????????????????????????????
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("temList", temList);
            request.getRequestDispatcher(empRoot + "/sendWX/wx_contentTemplate.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????????????????????????????????????????????????????!");
        }
    }


    /**
     * ??????txt??????
     *
     * @param data
     * @param destFile
     * @return
     * @throws IOException
     */
    public boolean sortAndSave(List data, String destFile) {
        BufferedWriter writer = null;
        try {
            StringBuffer sb = new StringBuffer();
            writer = new BufferedWriter(new FileWriter(destFile));
            int dSize = data.size();
            for (int i = 0; i < dSize; i = i + 5) {
                int c = i;
                if (c + 5 <= dSize) {
                    c = c + 5;
                } else {
                    c = dSize;
                }
                for (int j = i; j < c; j++) {
                    sb.append(data.get(j));
                    sb.append("\n");
                }
                writer.write(sb.toString());
                sb.delete(0, sb.length());
            }

            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            EmpExecutionContext.error(e, "??????txt???????????????");
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "??????txt??????????????????????????????");
                }
            }
        }
    }

    /**
     * ????????? ?????????URL?????????URL ????????????URL
     */
    public String UrlRep(String phone, String url, String repUrl) {
        String str = "";
        if (MovePhoneHead.indexOf(phone.substring(0, 3)) > -1) {
            str = url.replace(repUrl, MovePhoneHeadURL);
        } else if (UnionPhoneHead.indexOf(phone.substring(0, 3)) > -1) {
            str = url.replace(repUrl, UnionPhoneHeadURL);
        } else if (TelPhoneHead1.indexOf(phone.substring(0, 3)) > -1) {
            str = url.replace(repUrl, TelPhoneHead1URL);
        } else {
            str = url;
        }
        return str;
    }

    /**
     * ?????????????????????
     *
     * @param netid
     * @param lguserid
     * @param request
     * @param taskId
     * @return
     */
    public String sendMsgInfo(String netid, String lguserid, HttpServletRequest request, String taskId) {
        String url = "";
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("NETID", netid);
            List<LfWXBASEINFO> li = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);

            conditionMap.put("CREATID", lguserid);
            List<LfWXPAGE> pages = baseBiz.getByCondition(LfWXPAGE.class, conditionMap, null);
            if (li != null && li.size() > 0) {
                LfWXBASEINFO base = li.get(0);
                if (pages != null && pages.get(0).getPARENTID() == 0) {
                    request.setAttribute("base", base);
                    request.setAttribute("netid", base.getNETID());
                    String w = CompressEncodeing.CompressNumber(pages.get(0).getID(), 6);
                    url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/w/" + w + "-";
                }
                String t = CompressEncodeing.CompressNumber(Long.valueOf(taskId), 6);
                url += t;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "???????????????????????????URL??????");
        }
        return url;
    }

    /**
     * ????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void upNumber(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = "";
        String phoneStr = "";
        String spUser = "";
        String depIdStr = "";
        String groupClient = "";
        //??????????????????
        String groupStr = "";
        String busCode = "";
        //???????????????
        Long effCount = 0l;
        //????????????
        Long subCount = 0l;
        //??????????????????
        Long badCount = 0l;
        //???????????????
        Long badModeCount = 0l;
        //?????????
        Long repeatCount = 0l;
        //???????????????
        Long blackCount = 0l;
        //??????
        String msg = "";
        //??????
        String smsTail = "";
        //***????????????????????????????????????***
        StringBuffer opContent = new StringBuffer();
        SendSmsAtom smsAtom = new SendSmsAtom();
        String filenamestr = "";
        int fileCount_log = 0;
        long startTime = System.currentTimeMillis();
        //????????????IDS
        String cliDepIds = "";
        //???????????????????????????
        TxtFileUtil txtfileutil = new TxtFileUtil();

        StringBuffer contentSb = new StringBuffer();
        StringBuffer badContentSb = new StringBuffer();
        //???????????????????????????
        String draftFilePath = "";
        //????????????????????????
        String containDraft = null;
        boolean isOverSize = false;
        long zipSize = StaticValue.ZIP_SIZE;
        long maxSize = StaticValue.MAX_SIZE;
        long allFileSize = 0L;
        String strlguserid = null;
        Long lguserid = null;
        String lgcorpcode = "";
        String[] phones = new String[1];
        // ???????????????????????????
        PreviewParams preParams = new PreviewParams();
        try {
            String[] haoduan = msgConfigBiz.getHaoduan();
            String repurl = request.getScheme() + "://" + request.getServerName();
            //?????????????????????id
            //strlguserid = request.getParameter("lguserid");
            //???????????? session????????????????????????
            strlguserid = SysuserUtil.strLguserid(request);


            String netid = request.getParameter("netid");
            String taskId = request.getParameter("taskId");
            //??????????????????

//			String neturl = request.getScheme()+ "://"+ request.getServerName()+ ":"+ request.getServerPort()+ request.getContextPath()+ "/w/";
            //?????????????????? may
            String neturl = SystemGlobals.getValue("wx.pageurl") + "/w/";
            String netInfo = wxSendBiz.sendMsgInfo(netid, taskId);
            if (netInfo == null || netInfo.trim().length() == 0) {

                EmpExecutionContext.error("?????????????????????????????????");
                goPreview("error", request, response);
                return;
            }
            //????????????????????????????????????????????????10????????????pageid???taskid??????????????????
            //????????????????????????????????????????????????????????????
            int netInfoLeng = netInfo.length();
            neturl += netInfo;
            lguserid = Long.valueOf(strlguserid == null ? "0" : strlguserid);
            String[] url = txtfileutil.getSaveUrl(lguserid);
            File zipUrl = null;
            HashSet<String> repeatList = new HashSet<String>();

            HashSet<Long> validPhone = new HashSet<Long>();
            DiskFileItemFactory factory = new DiskFileItemFactory();
            File Newfile = null;
            factory.setSizeThreshold(1024 * 1024);
            String temp = url[0].substring(0, url[0].lastIndexOf("/"));
            factory.setRepository(new File(temp));
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> fileList = null;
            try {
                //???????????????????????????
                fileList = upload.parseRequest(request);
            } catch (FileUploadException e) {
                EmpExecutionContext.error(e, lguserid, lgcorpcode);
                throw e;
            }

            Iterator<FileItem> it = fileList.iterator();
            List<BufferedReader> readerList = new ArrayList<BufferedReader>();
            FileItem fileItem = null;

//---------------------------------------------------------------------------------------------------
            int fileIndex = 0;
            int fileCount = 0;
            while (it.hasNext()) {
                fileItem = (FileItem) it.next();
                String fileName = fileItem.getFieldName();
                if (fileName.equals("phoneStr")) {
                    //????????????
                    phoneStr = fileItem.getString("UTF-8").toString();
                    if (",".equals(phoneStr)) {//????????????","??? ?????????????????????
                        phoneStr = "";
                    } else if (phoneStr.indexOf(",,") > -1) {//?????????????????????
                        phoneStr = phoneStr.replace(",,", ",");
                    }

                } else if (fileName.equals("empDepIds")) {
                    //??????id
                    depIdStr = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("cliDepIds")) {
                    //????????????ids
                    cliDepIds = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("groupClient")) {
                    //??????id
                    groupClient = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("groupIds")) {
                    //??????id
                    groupStr = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("spUser1")) {
                    //????????????
                    spUser = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("busCode1")) {
                    //????????????
                    busCode = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("smsTail")) {
                    //????????????
                    smsTail = fileItem.getString("UTF-8").toString().replace("\r\n", " ");
                } else if (fileName.equals("msg")) {
                    //??????
                    msg = fileItem.getString("UTF-8").toString().replace("\r\n", " ");
                    //??????????????????????????????
                    msg = smsBiz.smsContentFilter(msg);
                } else if (fileName.equals("lguserid")) {
                    //???????????????userid
                    lguserid = Long.parseLong(fileItem.getString("UTF-8").toString());
                } else if (fileName.equals("lgcorpcode")) {
                    //????????????
                    lgcorpcode = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("containDraft")) {
                    containDraft = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("draftFile")) {
                    draftFilePath = fileItem.getString("UTF-8").toString();

                }
                //??????????????????
                else if (!fileItem.isFormField() && fileItem.getName().length() > 0) {
                    //???????????????????????????????????????????????????????????????
                    allFileSize += fileItem.getSize();
                    if (allFileSize > maxSize) {
                        fileItem.delete();
                        fileItem = null;
                        isOverSize = true;
                        break;
                    }


                    //??????????????????????????????????????????????????????????????????????????????
                    fileCount++;

                    String fileCurName = fileItem.getName();
                    //??????????????????????????????
                    filenamestr = fileCurName + "," + filenamestr;
                    fileCount_log = fileCount_log + 1;

                    String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();
                    //??????????????????????????????
                    if (!fileType.equals(".rar") && !fileType.equals(".zip") && !fileType.equals(".xls")
                            && !fileType.equals(".et") && !fileType.equals(".xlsx") && !fileType.equals(".txt")) {
                        // ?????????????????????
                        EmpExecutionContext.error("??????????????????????????????????????????????????????????????????userId???" + lguserid + "???errCode:" + ErrorCodeInfo.V10003);
                        throw new EMPException(ErrorCodeInfo.V10003);
                    }
                    if (fileType.equals(".zip")) {
                        //???????????????????????????
                        if (fileItem.getSize() > zipSize) {
                            fileItem.delete();
                            fileItem = null;
                            isOverSize = true;
                            break;
                        }
                        //??????????????????
                        String zipFileStr = url[0].replace(".txt", "_" + fileCount + ".zip");
                        zipUrl = new File(zipFileStr);
                        //????????????????????????????????????
                        fileItem.write(zipUrl);
                        ZipFile zipFile = new ZipFile(zipUrl);
                        Enumeration zipEnum = zipFile.getEntries();
                        ZipEntry entry = null;
                        while (zipEnum.hasMoreElements()) {
                            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                            fileIndex++;
                            //???????????????????????????????????????????????????????????????
                            String fileIndexStr = String.valueOf(fileIndex) + "_" + String.valueOf(fileCount);
                            entry = (ZipEntry) zipEnum.nextElement();
                            //??????txt??????
                            if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".txt")) {
                                InputStream instream = zipFile.getInputStream(entry);
                                String charset = get_charset(instream);
                                BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), charset));
                                if (charset.startsWith("UTF-")) {
                                    reader.read(new char[1]);
                                }
                                readerList.add(reader);
                            } else if (!entry.isDirectory() && (entry.getName().toLowerCase().endsWith(".xls") || entry.getName().toLowerCase().endsWith(".et"))) {
                                readerList.add(jxExcel(url[0], Newfile, zipFile.getInputStream(entry), fileIndexStr, 1));
                            } else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xlsx")) {
                                readerList.add(jxExcel(url[0], Newfile, zipFile.getInputStream(entry), fileIndexStr, 2));
                            }
                        }
                    } else if (fileType.equals(".xls") || fileType.equals(".et")) {
                        //?????????????????????excel2003??????wps??????
                        String FileStr = url[0];
                        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + "_" + fileCount + ".txt";
                        Newfile = new File(FileStrTemp);
                        FileOutputStream fos = new FileOutputStream(Newfile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
                        BufferedWriter bw = new BufferedWriter(osw);
                        String phoneNum = "";
                        String Context = "";
                        try {
                            HSSFWorkbook workbook = new HSSFWorkbook(fileItem.getInputStream());
                            //???????????????
                            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                                HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                // ???????????????
                                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                    HSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    //??????????????????????????????
                                    phoneNum = getCellFormatValue(row.getCell(0));
                                    Context = "";
                                    //???????????????????????????,???????????????
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        HSSFCell cell = row.getCell(k);
                                        if (cell != null && cell.toString().length() > 0) {
                                            Context += "," + getCellFormatValue(cell);
                                        }

                                    }
                                    //?????????????????????????????????txt????????????
                                    bw.write(phoneNum + Context + line);
                                }
                            }
                            bw.close();
                            osw.close();
                            fos.close();
                            FileInputStream fis = new FileInputStream(Newfile);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
                            readerList.add(br);
                        } catch (FileNotFoundException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        }
                    } else if (fileType.equals(".xlsx")) {
                        String FileStr = url[0];
                        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + "_" + fileCount + ".txt";
                        Newfile = new File(FileStrTemp);
                        FileOutputStream fos = new FileOutputStream(Newfile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
                        BufferedWriter bw = new BufferedWriter(osw);
                        String phoneNum = "";
                        String Context = "";
                        try {
                            XSSFWorkbook workbook = new XSSFWorkbook(fileItem.getInputStream());
                            //???????????????
                            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                                XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                // ???????????????
                                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                    XSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    phoneNum = getCellFormatValue(row.getCell(0));
                                    Context = "";
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        XSSFCell cell = row.getCell(k);
                                        if (cell != null && cell.toString().length() > 0) {
                                            Context += "," + getCellFormatValue(cell);
                                        }
                                    }
                                    bw.write(phoneNum + Context + line);
                                }
                            }
                            bw.close();
                            osw.close();
                            fos.close();

                            FileInputStream fis = new FileInputStream(Newfile);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
                            readerList.add(br);
                        } catch (FileNotFoundException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        }
                    }// ??????zip???????????????
                    else if (fileType.equals(".rar")) {
                        String FileStr = url[0];
                        preParams.setPhoneFilePath(url);
                        readerList.addAll(smsAtom.parseRar(fileItem, FileStr, fileCount, preParams));
                    } else {
                        InputStream instream = fileItem.getInputStream();
                        String charset = get_charset(instream);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(), charset));
                        if (charset.startsWith("UTF-")) {
                            reader.read(new char[1]);
                        }
                        readerList.add(reader);
                    }
                }
            }// ????????????
            //-----------------------------------------------------------------------------------------------
            LfSysuser curSysuser = new LfSysuser();
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                curSysuser = (LfSysuser) loginSysuserObj;
            } else {
                curSysuser = baseBiz.getLfSysuserByUserId(lguserid);//???????????????????????????
            }

            //??????????????????SP????????????????????????????????????????????????
            boolean checkFlag = new CheckUtil().checkSysuserInCorp(curSysuser, lgcorpcode, spUser, null);
            if (!checkFlag) {
                EmpExecutionContext.error("???????????????????????????????????????????????????????????????????????????????????????checkFlag:" + checkFlag
                        + "???userid:" + curSysuser.getUserId()
                        + "???spuser:" + spUser);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(MessageUtils.extractMessage("common", "common_empLangName", request));
                result = info.getErrorInfo(IErrorCode.B20007);
                return;
            }


            //??????????????????
            if (!"".equals(smsTail)) {
                msg = msg + smsTail.trim();
            }
            //??????????????????
            String tmp;
            int mid = 0;
            //??????????????????
            int resultStatus = 0;
            BufferedReader reader;
            try {
                for (int r = 0; r < readerList.size(); r++) {
                    //????????????????????????500w?????????????????????????????????1500W???
                    if (effCount > StaticValue.MAX_PHONE_NUM) {
                        break;
                    }
                    reader = readerList.get(r);
                    while ((tmp = reader.readLine()) != null) {

                        subCount++;
                        mid = 0;
                        tmp = tmp.trim();
                        if (phoneUtil.getPhoneType(tmp, haoduan) == -1) {
                            //????????????
                            mid = 1;
                        } else if (blBiz.checkBlackList(lgcorpcode, tmp, busCode)) {
                            //????????????????????????
                            mid = 2;
                        } else if ((resultStatus = phoneUtil.checkRepeat(tmp, validPhone)) != 0) {
                            //??????1???????????????
                            if (resultStatus == 1) {
                                mid = 3;
                            }
                            //??????-1???????????????
                            else {
                                mid = 1;
                            }
                        } else {

                            //????????????????????????????????????????????????
                            String tempPhone = tmp;
                            String regx = "";//??????????????????????????????????????????????????????
                            if (tempPhone.length() > 2) {
                                if (tempPhone.indexOf("+") > -1) {
                                    String first = tempPhone.substring(1, 2);//?????????????????????????????????????????????????????????????????????
                                    tempPhone = tempPhone.substring(2);
                                    regx = "*0" + first + netInfoLeng;//????????????????????????
                                } else if (tempPhone.indexOf("00") > -1) {
                                    String first = tempPhone.substring(0, 2);//?????????????????????????????????????????????????????????????????????
                                    if ("00".equals(first)) {
                                        tempPhone = tempPhone.substring(2);
                                        regx = "*1" + netInfoLeng;//????????????????????????
                                    }
                                }
                            }
                            if ("1".equals(TelPhone.trim())) {
                                // ????????? ?????????URL?????????URL ????????????URL\
                                contentSb.append(tmp).append(",").append(msg).append(" ").append(UrlRep(tmp, neturl, repurl)).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                                //p_list.add(phone + "," + textSMS + " "+ UrlRep(phone, url, repurl)+ CompressEncodeing.JieMPhone(phone) + "  .");
                            } else {
                                //p_list.add(phone + "," + textSMS + " " + url+ CompressEncodeing.JieMPhone(phone) + "  .");
                                contentSb.append(tmp).append(",").append(msg).append(" ").append(neturl).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                            }
                            effCount++;

                        }

                        switch (mid) {
                            case 1:
                                /*???????????????*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(tmp).append(line);
                                badModeCount++;
                                badCount++;
                                break;
                            case 2:
                                /*??????????????????*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(tmp).append(line);
                                blackCount++;
                                badCount++;
                                break;
                            case 3:
                                /*???????????????*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(tmp).append(line);
                                repeatCount++;
                                badCount++;
                                break;
                        }
                        //?????????????????????
                        if (effCount % 10000 == 0 && effCount > 10000) {
                            txtfileutil.writeToTxtFile(url[0], contentSb
                                    .toString());
                            contentSb = null;
                            contentSb = new StringBuffer();
                        }
                        if (badCount % 1000 == 0 && badCount > 1000) {

                            txtfileutil.writeToTxtFile(url[2], badContentSb
                                    .toString());
                            badContentSb = new StringBuffer();
                        }
                    }

                    reader.close();
                }
            } catch (Exception e) {
                txtfileutil.deleteFile(url[0]);
                EmpExecutionContext.error(e, lguserid, lgcorpcode);
                throw e;
            } finally {
    			try{
    				IOUtils.closeReaders(getClass(), readerList);
    			}catch(IOException e){
    				EmpExecutionContext.error(e, "");
    			}
                readerList.clear();
                repeatList.clear();
                readerList = null;
                repeatList = null;
                if (zipUrl != null) {
                    if (!zipUrl.delete()) {
                    }
                    ;
                }
                String FileStr = url[0];
                String FileStrTemp;
                String fileStr2;
                File file = null;
                //??????????????????
                for (int j = 0; j < fileCount; j++) {
                    //???????????????????????????excel???????????????
                    int b = j + 1;
                    fileStr2 = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp_" + b + ".txt";
                    file = new File(fileStr2);
                    if (file.exists()) {
                        if (!file.delete()) {
                        }
                    }
                    //???????????????????????????????????????
                    for (int i = 0; i < fileIndex; i++) {
                        int a = i + 1;
                        FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + a + "_" + b + ".txt";
                        file = new File(FileStrTemp);
                        if (file.exists()) {
                            if (!file.delete()) {
                            }
                        }
                    }
                }

                if (Newfile != null) {
                    if (!Newfile.delete()) {
                    }
                }
            }
//=========================================================================================

            if (isOverSize) {
                result = "overSize";
            } else if (effCount > StaticValue.MAX_PHONE_NUM) {
                for (int i = 0; i < url.length; i++) {
                    txtfileutil.deleteFile(url[i]);
                }
                result = "overstep";
            } else {
                //????????????????????????????????????
                if (depIdStr.length() > 0) {
                    //????????????id????????????
                    phoneStr = phoneStr + getEmpByDepId2(depIdStr, lgcorpcode);
                    String tempdepIdStr = depIdStr.substring(0, depIdStr.length() - 1);
                    //???????????????e+id???,??????
                    if (tempdepIdStr.indexOf("e") > -1) {
                        tempdepIdStr = tempdepIdStr.replaceAll("e", "");
                    }
                    /*???????????????*/
                    opContent.append(MessageUtils.extractMessage("ydwx", "group_ydbg_xzqz_text_memberaddbook", request) + " id:" + tempdepIdStr).append("???");

                }
                //????????????????????????????????????
                if (!StringUtils.isEmpty(cliDepIds)) {
                    //????????????ID???????????????????????????
                    phoneStr = wxSendBiz.getClientPhoneStrByDepId(phoneStr, cliDepIds, lgcorpcode);
                    String tempcliDepIds = cliDepIds.substring(0, cliDepIds.length() - 1);
                    //???????????????e+id???,??????
                    if (tempcliDepIds.indexOf("e") > -1) {
                        tempcliDepIds = tempcliDepIds.replaceAll("e", "");
                    }
                    /*???????????????*/
                    opContent.append(MessageUtils.extractMessage("ydwx", "dxkf_ydkf_xzfsdx_text_khtxl", request) + " id:" + tempcliDepIds).append("???");

                }
                if (groupStr.length() > 0 || groupClient.length() > 0) {
                    //????????????????????????
                    phoneStr = phoneStr + getEmpByGroupStr(groupStr, groupClient);
                    if (groupClient.length() > 0) {
                        String tempgroupClient = groupClient.substring(0, groupClient.length() - 1);
                        /*????????????*/
                        opContent.append(MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_3", request) + " id:" + tempgroupClient).append("???");
                    }
                    if (groupStr.length() > 0) {
                        String tempgroupStr = groupStr.substring(0, groupStr.length() - 1);
                        /*????????????*/
                        opContent.append(MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_4", request) + " id:" + tempgroupStr).append("???");
                    }

                }


                String draft = "";
                // 0.?????????????????????
                if (containDraft != null && StringUtils.isNotBlank(draftFilePath)) {
                    TxtFileUtil txtFileUtil = new TxtFileUtil();
                    String webRoot = txtFileUtil.getWebRoot();
                    File draftFile = new File(webRoot, draftFilePath);
                    if (!draftFile.exists() && StaticValue.getISCLUSTER() == 1) {
                        CommonBiz comBiz = new CommonBiz();
                        String downloadRes = "error";
                        //??????????????????
                        int retryTime = 3;
                        while (!"success".equals(downloadRes) && retryTime-- > 0) {
                            downloadRes = comBiz.downloadFileFromFileCenter(draftFilePath);
                        }
                        if (!"success".equals(downloadRes)) {
                            EmpExecutionContext.error("????????????????????????????????????????????????????????????");
                        }
                    }
                    if (!draftFile.exists()) {
                        EmpExecutionContext.error("?????????????????????????????????????????????");
                        result = "error";
                        return;
                    }
                    BufferedReader bufferedReader = null;
                    try {
                        bufferedReader = new BufferedReader(new FileReader(draftFile));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            draft = line.trim() + "," + draft;
                        }
                    } catch (Exception e) {
                        EmpExecutionContext.error("????????????????????????????????????????????????");
                        result = "error";
                        return;
                    } finally {
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    }
                }
                if (!"".equals(draft)) {
                    phoneStr = phoneStr + draft;
                }
                if (phoneStr.length() > 0) {
                    phones = phoneStr.split(",");
                    for (String num : phones) {
                        subCount++;
                        if (num != null) {
                            num = num.trim();
                        }
                        if (num != null && num.length() > 6 && num.length() < 22) {
                            if (phoneUtil.getPhoneType(num, haoduan) == -1) {
                                //????????????
                                /*???????????????*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num)
                                        .append(line);
                                badModeCount++;
                                badCount++;
                                continue;
                            } else if (blBiz.checkBlackList(lgcorpcode, num, busCode)) {
                                /*??????????????????*/
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(num).append(line);
                                blackCount++;
                                badCount++;
                                continue;
                            } else if ((resultStatus = phoneUtil.checkRepeat(num, validPhone)) != 0) {
                                //??????1???????????????
                                if (resultStatus == 1) {
                                    /*???????????????*/
                                    badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(num).append(line);
                                    repeatCount++;
                                    badCount++;
                                    continue;
                                }
                                //??????-1???????????????
                                else {
                                    /*???????????????*/
                                    badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num)
                                            .append(line);
                                    badModeCount++;
                                    badCount++;
                                    continue;
                                }
                            }
                            //????????????????????????????????????????????????
                            String tempPhone = num;
                            String regx = "";//??????????????????????????????????????????????????????
                            if (tempPhone.length() > 2) {
                                if (tempPhone.indexOf("+") > -1) {  //???*0??????????????????,*1??????00?????????
                                    String first = tempPhone.substring(1, 2);//?????????????????????????????????????????????????????????????????????
                                    tempPhone = tempPhone.substring(2);
                                    regx = "*0" + first + netInfoLeng;//????????????????????????
                                } else if (tempPhone.indexOf("00") > -1) { //???*0??????????????????,*1??????00?????????
                                    String first = tempPhone.substring(0, 2);//?????????????????????????????????????????????????????????????????????
                                    if ("00".equals(first)) {
                                        tempPhone = tempPhone.substring(2);
                                        regx = "*1" + netInfoLeng;//????????????????????????
                                    }
                                }
                            }
                            if ("1".equals(TelPhone.trim())) {
                                // ????????? ?????????URL?????????URL ????????????URL\
                                contentSb.append(num).append(",").append(msg).append(" ").append(UrlRep(num, neturl, repurl)).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                            } else {
                                contentSb.append(num).append(",").append(msg).append(" ").append(neturl).append(CompressEncodeing.JieMPhone(tempPhone) + regx).append(" .").append(line);
                            }

                            effCount++;

                        } else if (num.length() < 7 || num.length() > 21) {
                            /*???????????????*/
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num).append(line);
                            badModeCount++;
                            badCount++;
                            continue;
                        }
                    }
                }
                txtfileutil.writeToTxtFile(url[0], contentSb.toString());
                //????????????????????????
                Long maxcount = 0L;
                int yct = 0;
                boolean isCharg = true; //???????????????????????????true;???????????????false;
                String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());


                try {
                    yct = smsBiz.countAllOprSmsNumber(spUser,
                            msg, 1, url[1], null);
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "????????????????????????????????????");
                    yct = 0;
                }
                //?????????SP????????????????????????????????????????????????????????????false?????????????????????????????????
                boolean isAllCharge = true;
                //???????????????????????????
                boolean IsChargings = biz.IsChargings(lguserid);
                if (IsChargings) {
                    EmpExecutionContext.info("date:" + dateStr + "    lfsysuser.corpCode:" + curSysuser.getCorpCode() + ";lfCorp.corpCode:" + lgcorpcode);
                    //???????????????????????????????????????????????????.
                    maxcount = biz.getAllowSmsAmount(curSysuser);
                    EmpExecutionContext.info("date:" + dateStr + "     ??????:" + maxcount);
                    if (maxcount == null) {
                        maxcount = 0L;
                    }
                    //??????????????????????????????
                    if (maxcount - yct < 0) {
                        //???????????????????????????
                        isAllCharge = false;
                        EmpExecutionContext.error("??????????????????????????????????????????????????????taskid:" + taskId
                                + "???corpCode:" + lgcorpcode
                                + "???userid???" + strlguserid
                                + "???depFeeCount:" + maxcount
                                + "???preSendCount" + yct);
                    }
                } else {
                    isCharg = false;
                }


                EmpExecutionContext.info("date:" + dateStr + "     ???????????????:" + yct);
                if (badContentSb.length() > 0) {
                    txtfileutil.writeToTxtFile(url[2], badContentSb.toString());
                }

                if (filenamestr.length() > 0) {
                    opContent.append("????????????" + fileCount_log + "???????????????" + filenamestr);
                }


                // SP????????????
                Long spUserAmount = -1L;
                Long feeFlag = 2L;
                if (isAllCharge) {
                    //??????SP????????????
                    feeFlag = biz.getSpUserFeeFlag(spUser, 1);
                    if (feeFlag == null || feeFlag < 0) {
                        EmpExecutionContext.error("????????????????????? ??????SP?????????????????????????????????spUser=" + spUser);
                    } else if (feeFlag == 1) {
                        //----??????SP????????????---
                        spUserAmount = biz.getSpUserAmount(spUser);
                        if (spUserAmount != null) {
                            if (spUserAmount - yct < 0) {
                                //???????????????????????????
                                isAllCharge = false;
                            }
                        }
                        //???????????????????????????

                        EmpExecutionContext.info("????????????????????? SP??????????????????????????????,spUser=" + spUser);
                    } else if (feeFlag == 2) {
                        EmpExecutionContext.info("????????????????????? SP??????????????????????????????,spUser=" + spUser);
                    }
                }

                String spFeeResult = "";
                //?????????????????????
                if (isAllCharge) {
                    try {
                        spFeeResult = biz.checkGwFee(spUser, yct, lgcorpcode, 1);
                    } catch (Exception ex) {
                        result = "error";
                        EmpExecutionContext.error(ex, lguserid, lgcorpcode);
                    }
                }

                // ****?????????????????????????????????*****
                //???????????????
                SimpleDateFormat sdfdate = new SimpleDateFormat("HH:mm:ss");
                String info = "??????S???" + sdfdate.format(startTime) + "????????????" + (System.currentTimeMillis() - startTime) + "ms??????????????????" + subCount + "???????????????" + effCount + "???";
                setLog(request, "??????????????????", info + opContent.toString() + " taskId???" + taskId, StaticValue.GET);

                if (subCount - 0 == 0) {
                    result = "noPhone";
                } else {
                    result = url[1] + "&" + String.valueOf(subCount)
                            + "&" + String.valueOf(effCount)
                            + "&" + String.valueOf(badModeCount)
                            + "&" + String.valueOf(repeatCount)
                            + "&" + String.valueOf(blackCount)
                            + "&" + url[1]
                            + "&" + String.valueOf(yct)
                            + "&" + String.valueOf(maxcount)
                            + "&" + String.valueOf(url[4]) + "&" + String.valueOf(isCharg) + "&" + spFeeResult + "&" + spUserAmount + "&" + feeFlag;
                }
            }
        } catch (Exception ex) {
            result = "error";
            EmpExecutionContext.error(ex, lguserid, lgcorpcode);
        } finally {
            goPreview(result, request, response);
        }
    }

    /**
     * ???????????????
     *
     * @param result
     * @param request
     * @param response
     */
    private void goPreview(String result, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("result", result);
            request.getRequestDispatcher(empRoot + "/sendWX/wx_preview.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "????????????????????????????????????!");
        }
    }

    //??????????????????
    public void add(HttpServletRequest request, HttpServletResponse response) {
        SmsSendBiz cfsb = new SmsSendBiz();
        SuperOpLog spLog = new SuperOpLog();
        String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
        LfOpratelog lfoplog = null;
        //??????
        String opModule = StaticValue.SMS_BOX;
        //?????????
        String opSper = StaticValue.OPSPER;
        //????????????
        String opType = StaticValue.ADD;
        //??????
        String opContent = "????????????????????????";
        //??????id
        String taskId = request.getParameter("taskId");
        EmpExecutionContext.info(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " ????????????????????????taskid???" + taskId);
        //????????????
        String spUser = request.getParameter("spUser");
        //????????????
        String title = request.getParameter("taskname");
        //????????????
        String msg = request.getParameter("msg");
        //????????????
        String smsTail = request.getParameter("smsTail");
        if (smsTail != null && !"".equals(smsTail)) {
            msg = msg + smsTail.trim();
        }
        //????????????
        String timerStatuss = request.getParameter("timerStatus");
        Integer timerStatus1 = (timerStatuss == null || "".equals(timerStatuss)) ? 0 : Integer.valueOf(timerStatuss);
        //????????????
        String timerTime = request.getParameter("timerTime");
        //????????????
        Integer subState = Integer.valueOf("2");

        String preStr = request.getParameter("preStr");
        //?????????????????????id
        //String strlguserid = request.getParameter("lguserid");
        //???????????? session????????????????????????
        String strlguserid = SysuserUtil.strLguserid(request);

        Long lguserid = null;
        //??????????????????
        String lgcorpcode = request.getParameter("lgcorpcode");
        String busCode = request.getParameter("busCode");
        String[] preStrArr = preStr.split("&");
        opContent = opContent + "??????????????????" + title + "???";
        String result = "";


        //??????????????????SP????????????????????????????????????????????????
        LfSysuser curSysuser = new LfSysuser();
        Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
        if (loginSysuserObj != null) {
            curSysuser = (LfSysuser) loginSysuserObj;
        } else {
            try {
                curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
            } catch (Exception e) {
                EmpExecutionContext.error("??????????????????????????????" + "lguserid:" + lguserid);
            }//???????????????????????????
        }
        boolean checkFlag = new CheckUtil().checkSysuserInCorp(curSysuser, lgcorpcode, spUser, null);
        if (!checkFlag) {
            EmpExecutionContext.error("?????????????????????????????????????????????????????????????????????????????????checkFlag:" + checkFlag
                    + "???userid:" + curSysuser.getUserId()
                    + "???spuser:" + spUser);
            result = "error";
            goFind(result, lguserid, lgcorpcode, taskId, request, response);
            return;
        }
        LfMttask mttask = new LfMttask();
        try {
            lguserid = Long.valueOf(strlguserid);
            //??????????????????,????????????(????????????)
            if (title != null && "???????????????????????????".equals(title.trim())) {
                EmpExecutionContext.error("???????????????????????????????????????" + "title:" + title + "???taskId???" + taskId);
                result = ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001);
                //??????
                goFind(result, lguserid, lgcorpcode, taskId, request, response);
                return;
            }
            //?????????????????????
            mttask.setSubCount(Long.valueOf(preStrArr[1]));
            mttask.setEffCount(Long.valueOf(preStrArr[2]));
            mttask.setMobileUrl(preStrArr[6]);

            mttask.setTitle(title);
            mttask.setSpUser(spUser);
            mttask.setBmtType(2);
            mttask.setTimerStatus(timerStatus1);
            mttask.setMsgType(2);
            //????????????
            mttask.setMsType(6);
            mttask.setSubState(subState);
            mttask.setBusCode(busCode);//??????????????????
            mttask.setMsg(msg);
            mttask.setSendstate(0);
            mttask.setCorpCode(lgcorpcode);
            mttask.setSendLevel(0);//??????????????? ??????0??????????????????
            mttask.setIsReply(0);//?????????????????????
            mttask.setTaskId(Long.valueOf(taskId));
            String netid = request.getParameter("netid");
            //????????????
            mttask.setTempid(Long.valueOf(netid));
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, strlguserid, lgcorpcode);

            lfoplog = new LfOpratelog();
            lfoplog.setOpUser(spUser);
            lfoplog.setOpModule(opModule);
            lfoplog.setOpAction(opType);
            lfoplog.setOpContent(opContent + opSper);
            lfoplog.setCorpCode(lgcorpcode);
            //????????????????????????
            logFail(lfoplog, e);
            //??????
            goFind(result, lguserid, lgcorpcode, taskId, request, response);
            return;
        }

        //????????????????????????icount
        int icount = 0;
        try {
            icount = smsBiz.countAllOprSmsNumber(mttask.getSpUser(),
                    mttask.getMsg(), mttask.getMsgType() > 1 ? 2 : 1, mttask.getMobileUrl(), null);
        } catch (Exception e) {
            icount = 0;
            EmpExecutionContext.error(e, strlguserid, lgcorpcode);
        }

        mttask.setIcount(String.valueOf(icount));

        try {
            //????????????
            if (timerStatus1 == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timerTime = timerTime + ":00";
                mttask.setTimerTime(new Timestamp(sdf.parse(timerTime).getTime()));
            } else {
                //???????????????
                mttask.setTimerTime(mttask.getSubmitTime());
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("mobileUrl", mttask.getMobileUrl());
            List<LfMttask> mtLists = baseBiz.getByCondition(LfMttask.class, conditionMap, null);
            if (mtLists != null && mtLists.size() > 0) {
                result = "fileerror";
            }
            if ("".equals(result)) {
                boolean flag = new CheckUtil().checkSysuserInCorp(curSysuser, lgcorpcode, spUser, null);
                if (!flag) {
                    throw new Exception("???????????????????????????????????????!");
                }
                mttask.setUserId(lguserid);
                //?????????????????????????????????????????????????????????????????????????????????
                Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
                //??????????????????
                result = cfsb.addSmsLfMttask(mttask, infoMap);
                String reultClong = result;

                //????????????
                result = new WGStatus(langName).getInfoMap().get(result);
                //???????????????????????????????????????????????????????????????
                if (result == null) {
                    result = reultClong;
                }

                //???????????? ??????????????????
                String draftId = request.getParameter("draftId");
                if (StringUtils.isNotBlank(draftId) && ("timerSuccess".equals(result) || "createSuccess".equals(result) || "000".equals(result))) {
                    baseBiz.deleteByIds(LfDrafts.class, draftId);
                }

                spLog.logSuccessString(spUser, opModule, opType, opContent, lgcorpcode);

                //??????????????????
                if (loginSysuserObj != null) {
                    LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                    EmpExecutionContext.info("??????????????????", loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), "?????????????????????[????????????,??????](" + title + "???" + result + "???", "OTHER");

                }
            }
        } catch (EMPException empex) {
            //?????????????????????
            ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
            //????????????????????????
            String desc = info.getErrorInfo(empex.getMessage());
            result = "empex" + desc;
            //????????????
            spLog.logFailureString(spUser, opModule, opType, opContent + opSper, empex, lgcorpcode);
            EmpExecutionContext.error(empex, "??????????????????????????????????????????!");
        } catch (Exception e) {
//			if(e.getClass().getName().equals("org.apache.http.conn.HttpHostConnectException"))
//			{
            if (e.getClass().isAssignableFrom(HttpHostConnectException.class)) {
                result = e.getLocalizedMessage();

                //????????????
                spLog.logSuccessString(spUser, opModule, opType, opContent, lgcorpcode);
            } else {
                result = "error";
                //????????????
                spLog.logFailureString(spUser, opModule, opType, opContent + opSper, e, lgcorpcode);
            }

            EmpExecutionContext.error(e, strlguserid, lgcorpcode);
        } finally {
            goFind(result, lguserid, lgcorpcode, taskId, request, response);
        }
    }

    /**
     * ???????????????????????????
     *
     * @param oplog
     * @param eo
     */
    private void logFail(LfOpratelog oplog, Exception eo) {
        try {
            SuperOpLog spLog = new SuperOpLog();
            //????????????
            spLog.logFailureString(oplog.getOpUser(), oplog.getOpModule(), oplog.getOpAction(), oplog.getOpContent(), eo, oplog.getCorpCode());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????!");
        }
    }

    private void goFind(String result, Long lguserid, String lgcorpcode, String taskId, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getSession(false).setAttribute("mcs_batchResult", result);
            String s = request.getRequestURI();
            s = s + "?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode + "&oldtaskId=" + taskId;
            response.sendRedirect(s);
        } catch (Exception e) {
            EmpExecutionContext.error(e, lguserid, lgcorpcode);
        }
    }

    /**
     * wap????????????
     *
     * @param request
     * @param response
     */
    public void interaction(HttpServletRequest request,
                            HttpServletResponse response) {
        try {
            String table = request.getParameter("table");

            String tablename = table.split("-")[0];
            String tableid = table.split("-")[1];

            String w = request.getParameter("w");
            String h = null, p = null;
            if (w != null && w.length() > 6) {
                Map<String, String> pdph = CompressEncodeing.UnJieM(w);
                p = pdph.get("p");
                h = pdph.get("h");
            }
            if (w != null && w.length() < 6) {
                p = CompressEncodeing.UnCompressNumber(w) + "";
                request.setAttribute("msg", "????????????");
                request.setAttribute("w", CompressEncodeing.CompressNumber(Long
                        .valueOf(p), 6));

                request.getRequestDispatcher("/wx_info.jsp").forward(request,
                        response);
                return;
            }

            int re = -1;
            String msg = "";
            List<LfWXTrustCols> cols = ueditorDao.getInteraction_cols(tableid);
            // ????????????????????????
            StringBuffer inserName = new StringBuffer();
            StringBuffer inserValue = new StringBuffer();

            if (cols != null && cols.size() > 0) {
                inserName.append("insert into ");
                inserName.append(tablename);
                inserName.append(" ( C0_SHOUJIHAOMA , PAGEID ,DATE_TIME  ,");
                if (h == null) {
                    h = "??????";
                }
                if (p == null) {
                    p = "0";
                }
                inserValue.append("  values( '" + h + "' ,'" + p + "' , '"
                        + new Date() + "',");
                for (int i = 0; i < cols.size(); i++) {
                    String[] colsNames = request.getParameterValues(cols.get(i)
                            .getColName());
                    inserName.append(cols.get(i).getColName());

                    if (colsNames != null && colsNames.length > 0) {
                        String colsvalues = "";

                        for (int j = 0; j < colsNames.length; j++) {
                            // ??????????????????
                            colsvalues = colsvalues + colsNames[j];
                            // ?????????????????????????????????????????????
                            if (cols.get(i).getColType() == 1) {
                                if (!AllUtil.isNomberfolatNO(colsvalues)
                                        && !AllUtil.isNomberNO(colsvalues)) {
                                    re = 1;
                                    msg = "????????????,???????????????";
                                }
                            } else if (cols.get(i).getColType() == 2) {
                                if (!AllUtil.isDataNO(colsvalues)) {
                                    re = 2;
                                    msg = "????????????,???????????????";
                                }
                            }

                            if (j != colsNames.length - 1) {
                                colsvalues = colsvalues + ",";
                            }
                        }

                        inserValue.append(" '" + colsvalues + "' "); // ????????????

                    } else {
                        inserValue.append("' '");
                    }
                    if (i != cols.size() - 1) {
                        inserName.append(" , ");
                        inserValue.append(" , ");
                    } else {
                        inserName.append(" ) ");
                        inserValue.append(" ) ");
                    }
                    if (re != -1) {
                        break;
                    }
                }

            }
            String sql = inserName.toString() + inserValue.toString();

            /**
             * @insertsql varchar(max),
             * @tablename varchar(50),
             * @phone varchar(15),
             * @mode varchar(2) output-- 0
             */
            // / 0????????????????????????1????????????????????????2?????????????????????
            String mode = ueditorDao.getsaveSql_mode(sql, tablename, h);
            if (mode != null && "1".equals(mode.trim())) {
                msg = "??????????????????,????????????";
            } else if (mode != null && "2".equals(mode.trim())) {
                msg = "??????????????????,????????????";
            } else if (re == -1 && mode != null && "0".equals(mode.trim())) {
                int i = ueditorDao.getsaveInteraction_cols(sql);
                if (i > 0) {
                    msg = "??????????????????,????????????";
                }
            } else {
                msg = msg + "??????????????????";
            }

            // phone = Base64.encode(phone.getBytes());
            request.setAttribute("msg", msg);
            request.setAttribute("w", CompressEncodeing.CompressNumber(Long
                    .valueOf(p), 6)
                    + CompressEncodeing.JieMPhone(h));
            request.getRequestDispatcher("/wx_info.jsp").forward(request,
                    response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????????????????!");
        }

    }


    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     */
    public void getTemplate(HttpServletRequest request,
                            HttpServletResponse response) {
        String output = new String();
        try {
            int sort = Integer.parseInt(request.getParameter("section"));
            int id = Integer.parseInt(request.getParameter("id"));
            output = ueditorDao.getTemplate(sort, id);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????");
        }
        response.setCharacterEncoding("utf-8");
        try {
            PrintWriter pw = response.getWriter();
            pw.println(output);
        } catch (IOException e1) {
            EmpExecutionContext.error(e1, "??????????????????!");
        }
    }

    /****
     * ??????????????????????????????JSON??????
     * @param request
     * @param response
     */
    public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) {
        try {
            String depId = request.getParameter("depId");
            String user = request.getParameter("userid");
            LfSysuser lfSysuser = getLoginUser(request);
            List<LfEmployeeDep> empDepList = wxSendBiz.getEmpSecondDepTreeByUserIdorDepId(user, depId, lfSysuser.getCorpCode());
            LfEmployeeDep dep = null;
            StringBuffer tree = new StringBuffer("");
            if (empDepList != null && empDepList.size() > 0) {
                tree.append("[");
                for (int i = 0; i < empDepList.size(); i++) {
                    dep = empDepList.get(i);
                    tree.append("{");
                    tree.append("id:'").append(dep.getDepId() + "'");
                    tree.append(",name:'").append(dep.getDepName()).append("'");
                    tree.append(",pId:'").append(dep.getParentId() + "'");
                    tree.append(",depcodethird:'").append(dep.getDepcodethird() + "'");
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != empDepList.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");
            }
            response.getWriter().print(tree.toString());
        } catch (Exception e) {
            try {
                response.getWriter().print("");
            } catch (IOException e1) {
                EmpExecutionContext.error(e, "?????????????????????");
            }
            EmpExecutionContext.error(e, "??????????????????????????????!");
        }
    }


    //??????OA?????????,?????????????????????????????????????????????????????????????????????????????????
    public void getDepAndEmpTree1(HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        try {
            PageInfo pageinfo = new PageInfo();
            // ??????
            String pageindex = request.getParameter("pageIndex");
            if (pageindex != null && !"".equals(pageindex)) {
                pageinfo.setPageIndex(Integer.parseInt(pageindex) + 1);
            } else {
                pageinfo.setPageIndex(1);
            }
            // ????????????50?????????
            pageinfo.setPageSize(50);
            String epname = "";
            //??????
            String depId = "";
            // ??????????????????
            String lgcorpcode = "";
            try {
                epname = request.getParameter("epname");
                // ??????
                depId = request.getParameter("depId");
                // ??????????????????
                lgcorpcode = request.getParameter("lgcorpcode");
                //???????????????????????????
                List<DynaBean> lfDepLoyeeList = new CommonBiz().getDepLoyee(epname, depId, lgcorpcode, pageinfo);
                // ??????html
                StringBuffer sb = new StringBuffer();
                if (lfDepLoyeeList != null && lfDepLoyeeList.size() > 0) {
                    for (DynaBean bean : lfDepLoyeeList) {
                        sb.append("<option value='").append("e_" + bean.get("employeeid").toString())
                                .append("' mobile='").append(bean.get("mobile").toString())
                                .append("'>").append(bean.get("name").toString())
                                .append(" [").append(bean.get("dep_name").toString()).append("]")
                                .append("</option>");
                    }
                }
                String pageStr = pageinfo.getTotalRec() + "@" + pageinfo.getTotalPage() + "@" + sb.toString();

                // ??????????????????
                response.getWriter().print(pageStr);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "???????????????????????????depId:" + depId + "???epname???" + epname + "???lgcorpcode???" + lgcorpcode);
            }
//			String epname = request.getParameter("epname");
//			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//			String depId = request.getParameter("depId");
//			StringBuffer sb = new StringBuffer();
//			//??????????????????
//			String lgcorpcode = request.getParameter("lgcorpcode");
//
//			//????????????????????????id
//			if(depId != null && !"".equals(depId.trim()))
//			{
//				conditionMap.put("depId", depId);
//			}
//
//			conditionMap.put("corpCode", lgcorpcode);
//			if(epname!=null&&!"".equals(epname.trim()))
//			{
//				conditionMap.put("name&like", epname);
//			}
//			//??????????????????
//			List<LfEmployee> lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
//
//			//??????html
//			if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
//				//?????????????????????id??????????????????key???depId
//				Map<Long,String> depIdMap = new HashMap<Long,String>();
//				for (LfEmployee user : lfEmployeeList)
//				{
//					depIdMap.put(user.getDepId(), "");
//				}
//
//				StringBuffer bufDepId = new StringBuffer();
//				//????????????????????????id????????????id1,id2,id3
//				Set<Long> keys = depIdMap.keySet();
//		        for (Iterator<Long> it = keys.iterator(); it.hasNext();)
//		        {
//		        	bufDepId.append(it.next()).append(",");
//		        }
//
//		        String strDepId = null;
//		        if(bufDepId != null && bufDepId.length() != 0)
//		        {
//		        	//???????????????,???
//		        	strDepId = bufDepId.substring(0, bufDepId.length()-1);
//		        }
//
//		        //?????????????????????
//		        conditionMap.clear();
//		        conditionMap.put("depId&in", strDepId);
//		        List<LfEmployeeDep> empDepsList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
//
//				for (LfEmployee user : lfEmployeeList)
//				{
//					sb.append("<option value='").append("e_"+user.getEmployeeId())
//					.append("' mobile='").append(user.getMobile()).append("'>")
//					.append(user.getName().trim());
//
//					for(LfEmployeeDep empDep : empDepsList)
//					{
//						if(user.getDepId().equals(empDep.getDepId()))
//						{
//							//??????????????????
//							sb.append(" [").append(empDep.getDepName()).append("]");
//						}
//					}
//
//					sb.append("</option>");
//				}
//			}
//			//??????????????????
//			response.getWriter().print(sb.toString());

        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????????????????!");
        }
    }

    //??????????????????????????????????????????????????????????????????????????????
    public void getDep(HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        try {
            String depId = request.getParameter("depId");
            String depName = request.getParameter("depName");
            String depIdsExist = request.getParameter("depIdsExist");
            //??????????????????
            String lgcorpcode = request.getParameter("lgcorpcode");

            String[] depIds = depIdsExist.split(",");
            StringBuffer depIdsTemp = new StringBuffer();
            for (int i = 0; i < depIds.length; i++) {
                if (depIds[i].indexOf("e") > -1) {
                    depIdsTemp.append(depIds[i].substring(1) + ",");
                } else if (depIds[i].equals(depId)) {
                    response.getWriter().print("depExist");
                    return;
                }
            }
            //??????????????????????????????????????????????????????????????????
            boolean result = isDepAcontainsDepB(depIdsTemp.toString(), depId, lgcorpcode);
            if (result) {
                response.getWriter().print("depExist");
                return;
            } else {
                response.getWriter().print("notExist");
                return;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "???????????????????????????!");
        }
    }

    //????????????????????????????????????????????????
    private boolean isDepAcontainsDepB(String depIdAs, String depIdB, String corpCode) {
        boolean result = false;
        List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();
        LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
        String[] depIdAsTemp = depIdAs.split(",");
        try {
            for (int a = 0; a < depIdAsTemp.length; a++) {
                if (depIdAsTemp[a] != null && !"".equals(depIdAsTemp[a])) {
                    /*lfEmployeeDepList = new GenericLfEmployeeVoDAO().findEmployeeDepsByDepId(corpCode,depIdAsTemp[a]);*/
                    lfEmployeeDepList = wxSendBiz.findEmpDepsByDepId(corpCode, depIdAsTemp[a]);
                    for (int i = 0; i < lfEmployeeDepList.size(); i++) {
                        depIdSet.add(lfEmployeeDepList.get(i).getDepId());
                    }
                }
            }
            result = depIdSet.contains(Long.valueOf(depIdB));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????????????????????????????????????????!");
        }
        return result;
    }

    //??????????????????????????????????????????????????????????????????
    public void isDepsContainedByDepB(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ismut = request.getParameter("ismut");
        String depId = request.getParameter("depId");
        if ("0".equals(ismut)) {
            String countttt = addrBookAtom.getEmployeeCountByDepId(depId);
            response.getWriter().print(countttt);
            return;
        }
        List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();
        LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
        String depIdsExist = request.getParameter("depIdsExist");
        String[] depIds = depIdsExist.split(",");
        List<String> list = Arrays.asList(depIds);
        //????????????????????????id????????????list??????(???????????????e?????????e??????depIdExistList??????)
        List<Long> depIdExistList = new ArrayList<Long>();
        for (int j = 0; j < depIds.length; j++) {
            if (depIds[j] != null && !"".equals(depIds[j])) {
                if (depIds[j].indexOf("e") > -1) {
                    if (!"".equals(depIds[j].substring(1))) {
                        depIdExistList.add(Long.valueOf(depIds[j].substring(1)));
                    }
                } else {
                    depIdExistList.add(Long.valueOf(depIds[j]));
                }

            }
        }
        //??????????????????
        String lgcorpcode = request.getParameter("lgcorpcode");

        //????????????????????????????????????????????????????????????set??????
        /*lfEmployeeDepList = new GenericLfEmployeeVoDAO().findEmployeeDepsByDepId(lgcorpcode,depId);*/
        lfEmployeeDepList = wxSendBiz.findEmpDepsByDepId(lgcorpcode, depId);
        List<Long> depIdListTemp = new ArrayList<Long>();
        for (int i = 0; i < lfEmployeeDepList.size(); i++) {
            depIdSet.add(lfEmployeeDepList.get(i).getDepId());
        }
        //????????????set????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????option?????????????????????select??????
        for (int a = 0; a < depIdExistList.size(); a++) {
            if (depIdSet.contains(depIdExistList.get(a))) {
                depIdListTemp.add(depIdExistList.get(a));
            }
        }
        //????????????????????????????????????????????????????????????????????????????????????????????????select???html
        String depids = depIdSet.toString();
        depids = depids.substring(1, depids.length() - 1);
        //??????????????????
        String countttt = addrBookAtom.getEmployeeCountByDepId(depids);
        if (depIdListTemp.size() > 0) {
            String tempDeps = depIdListTemp.toString();
            tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
            response.getWriter().print(countttt + "," + tempDeps);
            return;
        }
        //????????????????????????
        else {
            response.getWriter().print("notContains" + "&" + countttt);
            return;
        }
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     */
    public void getGroupList(HttpServletRequest request,
                             HttpServletResponse response) {
        //???????????????????????????
        StringBuffer buffer = new StringBuffer("");
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
        String employee = MessageUtils.extractMessage("common", "common_employee", request);
        String individual = MessageUtils.extractMessage("common", "common_individual", request);
        String shared = MessageUtils.extractMessage("common", "common_shared", request);
        String client = MessageUtils.extractMessage("common", "common_client", request);
        try {
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            String corpCode = lfSysuser.getCorpCode();
            //?????????userid
            //String userId =request.getParameter("lguserid");
            //???????????? session????????????????????????
            String userId = SysuserUtil.strLguserid(request);
            conditionMap.put("receiver", userId);
            conditionMap.put("gpAttribute", "0");
            orderByMap.put("udgName", "asc");

            //??????html????????????
            buffer.append("<select select-one name='groupList' id='groupList' " +
                    "size='15' style='height: 250px; width: 240px; border: 0;color: black;font-size: 12px;margin:-2px 0 0 -2px;'");
            buffer.append(" onclick='a()'>");

            //??????????????????????????????
            List<LfUdgroup> udgList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderByMap);
            if (udgList != null && udgList.size() > 0) {

                String udgIds = "";
                for (LfUdgroup udg : udgList) {
                    //udgIds += udg.getUdgId().toString()+",";
                    udgIds += udg.getGroupid().toString() + ",";
                }
                //????????????id?????????
                udgIds = udgIds.substring(0, udgIds.length() - 1);
                //Map<String,String> countMap = addrBookAtom.getEmployeeCount(udgIds);
                Map<String, String> countMap = new GroupBiz().getGroupMemberCount(udgIds, 1, corpCode);
                for (LfUdgroup udg : udgList) {
                    String mcount = countMap.get(udg.getGroupid().toString());
                    mcount = mcount == null ? "0" : mcount;
                    buffer.append("<option mcount='").append(mcount).append("' gtype='1'").append(" value='").append(udg.getUdgId()).append("'>");
                    buffer.append(udg.getUdgName().replace("<", "&lt;").replace(">", "&gt;")).append(udg.getSharetype() == 0 ? "[" + employee + "/" + individual + "]" : "[" + employee + "/" + shared + "]").append("</option>");
                }
            }

            //?????????????????????null
            udgList = null;
            //?????????????????????
            conditionMap.clear();
            //conditionMap.put("userId",userId);
            //???????????????ID
            conditionMap.put("receiver", userId);
            conditionMap.put("gpAttribute", "1");
            udgList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderByMap);
            if (udgList != null && udgList.size() > 0) {
                String udgIds = "";
                for (LfUdgroup udg : udgList) {
                    //udgIds += udg.getUdgId().toString()+",";
                    udgIds += udg.getGroupid().toString() + ",";
                }
                //????????????id?????????
                udgIds = udgIds.substring(0, udgIds.length() - 1);
                //Map<String,String> countMap1 = wxSendBiz.getGroupCount(udgIds, "2");
                Map<String, String> countMap1 = new GroupBiz().getGroupMemberCount(udgIds, 2, corpCode);
                for (LfUdgroup lfUdgroup : udgList) {
                    String shareType = "0";
                    if (lfUdgroup.getSharetype() != null && lfUdgroup.getSharetype() == 1) {
                        //??????
                        shareType = "1";
                    }

                    //String mcount = countMap.get(lfUdgroup.getUdgId().toString());
                    String mcount = countMap1.get(lfUdgroup.getGroupid().toString());
                    mcount = mcount == null ? "0" : mcount;
                    //shareType?????????0?????? 1??????   groupType1 ???????????????  2???????????????
                    buffer.append("<option  mcount='" + mcount + "' isdep='3' sharetype ='" + shareType + "' gtype='2'  value='" + lfUdgroup.getGroupid() + "' udgid='" + lfUdgroup.getUdgId() + "'  style='padding-left: 5px;'>")
                            .append(lfUdgroup.getUdgName().replace("<", "&lt;").replace(">", "&gt;"));
                    if (lfUdgroup.getSharetype() == 0) {
                        buffer.append(" [" + client + "/" + individual + "]");
                    } else if (lfUdgroup.getSharetype() == 1) {
                        buffer.append(" [" + client + "/" + shared + "]");
                    }
                    buffer.append("</option>");
                }
            }

            buffer.append("</select>");

        } catch (Exception e) {
            EmpExecutionContext.error(e, "????????????????????????!");
        }
        try {
            response.getWriter().print(buffer.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            EmpExecutionContext.error(e, "?????????????????????");
        }
    }


    public void getGroupMemberByNameAndId(HttpServletRequest request, HttpServletResponse response) {
        try {
            //????????????
            String name = request.getParameter("name");
            //??????id
            String udgId = request.getParameter("udgId");
            //????????????   1????????????  2????????????
            String groupType = request.getParameter("groupType");

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("name", name);
            conditionMap.put("udgId", udgId);
            conditionMap.put("groupType", groupType);
            //?????????????????????
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            List<DynaBean> dynaBeans = wxSendBiz.getGroupMemberByNameAndId(lfSysuser.getUserId(), conditionMap);
            List list = new ArrayList();
            for (DynaBean dynaBean : dynaBeans) {
                Map<String, Object> stringObjectMap = convertBeanToMap(dynaBean);
                list.add(stringObjectMap);
            }
            response.getWriter().print(com.alibaba.fastjson.JSONObject.toJSONString(list));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "????????????->??????????????????->????????????->?????????????????????Id??????????????????????????????!");
        }
    }

    public static Map<String, Object> convertBeanToMap(DynaBean bean) {
        Map<String, Object> map = null;
        if (bean != null) {
            map = new HashMap<String, Object>();
            DynaProperty[] dynaProperties = bean.getDynaClass().getDynaProperties();
            if (dynaProperties != null) {
                for (DynaProperty property : dynaProperties) {
                    map.put(property.getName(), bean.get(property.getName()));
                }
            }
        }
        return map;
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getGroupMember(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        try {
            //????????????
            String epname = request.getParameter("epname");
            //??????id
            String udgId = request.getParameter("udgId");
            //?????????????????????????????????????????????
            if ("".equals(udgId) || udgId.split(",").length > 1) {
                response.getWriter().print("");
                return;
            }
            //???????????????userid
            //String userId = request.getParameter("lguserid");
            //???????????? session????????????????????????
            String userId = SysuserUtil.strLguserid(request);

            udgId = (udgId != null && udgId.length() > 0) ? udgId.substring(0, udgId.length() - 1) : "";

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            if (epname != null && !"".equals(epname)) {
                conditionMap.put("name&like", epname);
            }
            //???????????????????????????
            LfUdgroup group = baseBiz.getById(LfUdgroup.class, udgId);
            List<LfEmployee> lfEmployeeList = null;
            if (group != null) {
                lfEmployeeList = wxSendBiz.getEmployeeListByUdgId(group.getGroupid().toString(), userId, conditionMap);
            }

            StringBuffer sb = new StringBuffer();
            //??????html????????????
            if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
                for (LfEmployee user : lfEmployeeList) {
                    if (user.getRecState() == 2) {
                        sb.append("<option value='").append("m_" + user.getGuId())
                                .append("' mobile='").append(user.getMobile()).append("'>");
                        sb.append(user.getName().trim()).append("</option>");

                    } else {
                        sb.append("<option value='").append("e_" + user.getEmployeeId())
                                .append("' mobile='").append(user.getMobile()).append("'>");
                        sb.append(user.getName().trim()).append("</option>");
                    }
                }
            }
            response.getWriter().print(sb.toString());

        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????????????????!");
        }
    }

    /**
     * ????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void filterPh(HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        String[] haoduan = msgConfigBiz.getHaoduan();
        String tmp = request.getParameter("tmp");
        PrintWriter out = response.getWriter();
        //????????????
        if (phoneUtil.getPhoneType(tmp, haoduan) == -1) {
            out.print("false");
        } else {
            out.print("true");
        }

    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     */
    public void goToFile(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getParameter("url");
        TxtFileUtil tfu = new TxtFileUtil();
        try {
            response.getWriter().print(tfu.checkFile(url));
        } catch (Exception e) {
            //????????????
            EmpExecutionContext.error(e, "??????????????????????????????!");
        }
    }

    public void getGtInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String spUser = request.getParameter("spUser");
        String[] gtInfos = new String[]{"", "", ""};
        int index = 0;
        int maxLen;
        int totalLen;
        int lastLen;
        int signLen;
        String info = "infos:";
        try {
            List<GtPortUsed> gtPortsList = smsBiz.getPortByUserId(spUser);
            for (GtPortUsed gtPort : gtPortsList) {
                index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();

                maxLen = gtPort.getMaxwords();
                totalLen = gtPort.getMultilen1();
                lastLen = gtPort.getMultilen2();
                signLen = gtPort.getSignlen() == 0 ? gtPort.getSignstr().trim().length() : gtPort.getSignlen();

                gtInfos[index] = String.valueOf(maxLen) + "," + String.valueOf(totalLen) +
                        "," + String.valueOf(lastLen) + "," + String.valueOf(signLen);
            }
            info += gtInfos[0] + "&" + gtInfos[1] + "&" + gtInfos[2] + "&";
        } catch (Exception e) {
            info = "error";
            EmpExecutionContext.error(e, "????????????????????????!");
        } finally {
            response.getWriter().print(info);
        }
    }

    /**
     * ??????????????????,??????????????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getSpGateConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String spUser = request.getParameter("spUser");
        String[] gtInfos = new String[]{"", "", "", ""};
        int index = 0;
        int maxLen;
        int totalLen;
        int lastLen;
        int signLen;
        int enmaxLen;
        int entotalLen;
        int enlastLen;
        int ensignLen;
        int gateprivilege = 0;
        int gatepri;
        //????????????,1:??????;0:??????
        int signLocation = 0;
        int ensinglelen;
        int msgLen = 900;

        String info = "infos:";
        try {
            // ????????????????????????????????????
            List<DynaBean> spGateList = smsBiz.getSpGateInfo(spUser);
            for (DynaBean spGate : spGateList) {
                gateprivilege = 0;
                //????????????????????????
                maxLen = Integer.parseInt(spGate.get("maxwords").toString());
                totalLen = Integer.parseInt(spGate.get("multilen1").toString());
                lastLen = Integer.parseInt(spGate.get("multilen2").toString());
                signLen = Integer.parseInt(spGate.get("signlen").toString());
                // ??????????????????????????????????????????0???????????????????????????????????????
                if (signLen == 0) {
                    signLen = spGate.get("signstr").toString().trim().length();
                }
                //????????????????????????
                enmaxLen = Integer.parseInt(spGate.get("enmaxwords").toString());
                entotalLen = Integer.parseInt(spGate.get("enmultilen1").toString());
                enlastLen = Integer.parseInt(spGate.get("enmultilen2").toString());
                ensinglelen = Integer.parseInt(spGate.get("ensinglelen").toString());

                //???????????????????????????1????????????0????????????
                gatepri = Integer.parseInt(spGate.get("gateprivilege").toString());
                index = Integer.parseInt(spGate.get("spisuncm").toString());
                //??????
                if (index == 21) {
                    index = 2;
                    //????????????
                } else if (index == 5) {
                    index = 3;
                    //???????????????????????????1????????????0????????????
                    if ((gatepri & 2) == 2) {
                        gateprivilege = 1;
                        //????????????????????????????????????(???????????????????????????100)
                        msgLen = enmaxLen - 100;
                    } else {
                        gateprivilege = 0;
                        //????????????????????????????????????(???????????????????????????90)
                        msgLen = maxLen - 90;
                    }
                }
                //????????????,1:??????;0:??????
                if ((gatepri & 4) == 4) {
                    signLocation = 1;
                } else {
                    signLocation = 0;
                }
                //??????????????????
                ensignLen = Integer.parseInt(spGate.get("ensignlen").toString());
                // ??????????????????????????????????????????0???????????????????????????????????????
                if (ensignLen == 0) {
                    if (index == 3) {
                        //????????????????????????????????????
                        ensignLen = smsBiz.getenSmsSignLen(spGate.get("ensignstr").toString().trim());
                    } else {
                        //????????????????????????????????????
                        ensignLen = spGate.get("ensignstr").toString().trim().length();
                    }
                }

                gtInfos[index] = new StringBuffer().append(maxLen).append(",").append(totalLen).append(",")
                        .append(lastLen).append(",").append(signLen).append(",").append(enmaxLen).append(",")
                        .append(entotalLen).append(",").append(enlastLen).append(",").append(ensignLen)
                        .append(",").append(gateprivilege).append(",").append(ensinglelen).append(",").append(signLocation).toString();
            }
            info += gtInfos[0] + "&" + gtInfos[1] + "&" + gtInfos[2] + "&" + gtInfos[3] + "&" + msgLen + "&";
        } catch (Exception e) {
            info = "error";
            EmpExecutionContext.error(e, "????????????????????????????????????????????????");
        } finally {
            response.getWriter().print(info);
        }
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     */
    public void checkBadWord1(HttpServletRequest request, HttpServletResponse response) {
        String tmMsg = request.getParameter("tmMsg");
        String corpCode = request.getParameter("corpCode");

        String words = new String();
        try {
            KeyWordAtom keyWordAtom = new KeyWordAtom();
            words = keyWordAtom.checkText(tmMsg, corpCode);
        } catch (Exception e) {
            words = "error";
            EmpExecutionContext.error(e, "???????????????????????????!");
        } finally {
            try {
                response.getWriter().print("@" + words);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "?????????????????????");
            }
        }
    }

    public void getTmMsg1(HttpServletRequest request, HttpServletResponse response) {
        //?????????????????????????????????????????????session????????????

        String result = null;
        //??????id
        String tmId = request.getParameter("tmId");
        try {
            if ("".equals(tmId)) {
                result = "";
            } else {
                //??????id??????????????????
                LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
                result = template.getTmMsg();
            }
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "??????????????????????????????!");
        } finally {
            //????????????????????????
            try {
                response.getWriter().print("@" + result);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "?????????????????????");
            }
        }
    }

    //??????txt????????????
    public String get_charset(InputStream inp) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            BufferedInputStream bis = new BufferedInputStream(inp);
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset;
            }
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "Unicode";
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                // checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                // checked = true;
            }
            bis.reset();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????txt?????????????????????");
        }
        return charset;
    }

    /**
     * ??????EXCEL??????????????????
     *
     * @param url       ????????????
     * @param Newfile   ???????????????
     * @param ins       ???????????????
     * @param fileIndex ?????????????????????
     * @param exceltype excel?????????1-xls???et???2-xlsx
     * @return
     */
    public BufferedReader jxExcel(String url, File Newfile, InputStream ins, String fileIndex, int exceltype) {
        String FileStr = url;//url[0];
        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + fileIndex + ".txt";
        File Newfiletemp=null;
        //TODO
        Newfiletemp = new File(FileStrTemp);
        if (Newfiletemp.exists()) {
            if (!Newfiletemp.delete()) {
            }
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
            fos = new FileOutputStream(Newfiletemp);
            osw = new OutputStreamWriter(fos, "GBK");
            bw = new BufferedWriter(osw);
            String phoneNum = "";
            String Context = "";

            if (exceltype == 1) {
                HSSFWorkbook workbook = new HSSFWorkbook(ins);
                //???????????????
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                    HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                    // ???????????????
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        HSSFRow row = sheet.getRow(rowNum);
                        if (row == null) {
                            continue;
                        }
                        //??????????????????????????????
                        phoneNum = getCellFormatValue(row.getCell(0));
                        Context = "";
                        //???????????????????????????,???????????????
                        for (int k = 1; k < row.getLastCellNum(); k++) {
                            HSSFCell cell = row.getCell(k);
                            if (cell != null && cell.toString().length() > 0) {
                                Context += "," + getCellFormatValue(cell);
                            }

                        }
                        //?????????????????????????????????txt????????????
                        bw.write(phoneNum + Context + line);
                    }
                }
            } else {

                XSSFWorkbook workbook = new XSSFWorkbook(ins);
                //???????????????
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                    XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                    // ???????????????
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        XSSFRow row = sheet.getRow(rowNum);
                        if (row == null) {
                            continue;
                        }
                        phoneNum = getCellFormatValue(row.getCell(0));
                        Context = "";
                        for (int k = 1; k < row.getLastCellNum(); k++) {
                            XSSFCell cell = row.getCell(k);
                            if (cell != null && cell.toString().length() > 0) {
                                Context += "," + getCellFormatValue(cell);
                            }
                        }
                        bw.write(phoneNum + Context + line);
                    }
                }
            }
            fis = new FileInputStream(Newfiletemp);
            br = new BufferedReader(new InputStreamReader(fis, "GBK"));

            return br;
        } catch (FileNotFoundException e) {
            EmpExecutionContext.error(e, "?????????EXCEL?????????!");
            return null;
        } catch (IOException e) {
            EmpExecutionContext.error(e, "??????EXCEL??????????????????!");
            return null;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????EXCEL??????!");
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "??????EXCEL????????????!?????????????????????");
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "??????EXCEL????????????!?????????????????????");
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "??????EXCEL????????????!?????????????????????");
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "??????EXCEL????????????!?????????????????????");
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "??????EXCEL????????????!?????????????????????");
                }
            }
        }

    }

    /**
     * ???????????????????????????????????????Excel2007
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(XSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // ????????????Cell???Type
            switch (cell.getCellType()) {
                // ????????????Cell???Type???NUMERIC
                case XSSFCell.CELL_TYPE_NUMERIC:
                case XSSFCell.CELL_TYPE_FORMULA: {
                    // ???????????????cell?????????Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // ?????????Date?????????????????????Cell???Date???
                        Date date = cell.getDateCellValue();
                        // ???Date?????????????????????????????????
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        if (c.get(Calendar.HOUR) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0) {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        }
                    }
                    // ??????????????????
                    else {
                        // ????????????Cell?????????
                        // ???????????????????????????????????????
                        if (Math.floor(cell.getNumericCellValue()) == cell.getNumericCellValue()) {
                            cellvalue = String.valueOf((long) cell.getNumericCellValue());
                        } else {
                            cellvalue = cell.getRawValue();
                        }

                    }
                    break;
                }
                // ????????????Cell???Type???STRIN
                case XSSFCell.CELL_TYPE_STRING:
                    // ???????????????Cell?????????
                    cellvalue = cell.getStringCellValue();
                    break;
                // ?????????Cell???
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    /**
     * ???????????????????????????????????????Excel2003
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // ????????????Cell???Type
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // ??????
                    // ???????????????cell?????????Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // ?????????Date?????????????????????Cell???Date???
                        Date date = cell.getDateCellValue();
                        // ???Date?????????????????????????????????
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        if (c.get(Calendar.HOUR) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0) {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        }
                    }
                    // ??????????????????
                    else {
                        // ???????????????????????????????????????
                        if (Math.floor(cell.getNumericCellValue()) == cell.getNumericCellValue()) {
                            cellvalue = String.valueOf((long) cell.getNumericCellValue());
                        } else {
                            cellvalue = String.valueOf(cell.getNumericCellValue());
                        }
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING: // ?????????
                    cellvalue = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_FORMULA: // ??????
                    cellvalue = cell.getCellFormula();
                    break;
                case HSSFCell.CELL_TYPE_BLANK: // ??????
                    cellvalue = " ";
                    break;
                case HSSFCell.CELL_TYPE_ERROR: // ??????
                    cellvalue = " ";
                    break;
                default:
                    cellvalue = " ";
                    break;
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    //??????????????????
    private boolean checkRepeat(HashSet<Long> aa, String ee) {
        if (aa.contains(Long.parseLong(ee))) {
            return false;
        } else {
            aa.add(Long.parseLong(ee));
        }
        return true;
    }

    //????????????????????????id???????????????","?????????
    private String getEmpByGroupStr(String groupStr, String groupClient) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            //???????????????????????????????????????
            if (groupStr != null && groupStr.length() > 1) {
                //?????????????????????
                String udgId = groupStr.substring(0, groupStr.length() - 1);
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("udgId&in", udgId);
                List<LfUdgroup> groupList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, null);
                StringBuffer buffer = new StringBuffer();
                if (groupList != null && groupList.size() > 0) {
                    for (int i = 0; i < groupList.size(); i++) {
                        buffer.append(groupList.get(i).getGroupid());
                        if (i != (groupList.size() - 1)) {
                            buffer.append(",");
                        }
                    }
                }
                /*List<LfEmployee> lfEmployeeList = new AddrBookBiz().getEmployeeListByUdgId(buffer.toString(), null, null);*/
                List<LfEmployee> lfEmployeeList = wxSendBiz.getEmployeeListByUdgId(buffer.toString(), null, null);

                if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
                    for (LfEmployee user : lfEmployeeList) {
                        sb.append(user.getMobile()).append(",");
                    }
                }
            }

            //???????????????????????????????????????
            if (groupClient != null && groupClient.length() > 1) {
                //?????????????????????
                String udgId = groupClient.substring(0, groupClient.length() - 1);
                String[] udgs = udgId.split(",");
                List<DynaBean> group = null;
                for (int i = 0; i < udgs.length; i++) {
                    group = wxSendBiz.findGroupClientByIds(udgs[i]);
                    if (group != null && group.size() > 0) {
                        for (DynaBean aa : group) {
                            sb.append(aa.get("mobile") + ",");
                        }
                    }
                }


            }


        } catch (Exception e) {
            //????????????
            EmpExecutionContext.error(e, "??????????????????????????????!");
        }

        return sb.toString();
    }

    //??????id?????????????????????????????????(???)depIds???,e1,3,10,e23,????????????????????????
    private String getEmpByDepId2(String depIds, String corpCode) {
        StringBuffer phones = new StringBuffer();
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            String[] tempDepIds = depIds.split(",");
            List<String> depIdsList = Arrays.asList(tempDepIds);
            List<String> depIdsList2 = new ArrayList<String>();
            List<String> depIdsList3 = new ArrayList<String>();

            for (int a = depIdsList.size() - 1; a >= 0; a--) {
                if (!"".equals(depIdsList.get(a))) {
                    if (depIdsList.get(a).indexOf("e") > -1) {
                        //?????????????????????
                        depIdsList3.add(depIdsList.get(a));
                    } else {
                        depIdsList2.add(depIdsList.get(a));
                    }
                }
            }
            StringBuffer buffer = new StringBuffer("");
            List<LfEmployee> employees = null;
            int j = 0;
            //??????????????????????????????
            for (int i = 0; i < depIdsList2.size(); i++) {
                if (depIdsList2.get(i) != null) {
                    buffer.append(depIdsList2.get(i) + ",");
                    j++;
                }
                if (j >= 999) {
                    j = 0;
                    conditionMap.put("depId&in", buffer.toString());
                    conditionMap.put("corpCode", corpCode);
                    employees = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
                    if (employees != null && employees.size() > 0) {
                        for (LfEmployee employee : employees) {
                            phones.append(employee.getMobile()).append(",");
                        }
                    }
                    buffer = new StringBuffer("");
                } else if (i == depIdsList2.size() - 1) {
                    conditionMap.put("depId&in", buffer.toString());
                    conditionMap.put("corpCode", corpCode);
                    employees = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
                    if (employees != null && employees.size() > 0) {
                        for (LfEmployee employee : employees) {
                            phones.append(employee.getMobile()).append(",");
                        }
                    }
                }
            }
            //???????????????????????????
            for (int y = 0; y < depIdsList3.size(); y++) {
                if (depIdsList3.get(y) != null && !"".equals(depIdsList3.get(y)) && depIdsList3.get(y).indexOf("e") > -1) {
                    /*					List<LfEmployee> employeeList = new GenericLfEmployeeVoDAO().findEmpMobileByDepIds(corpCode, depIdsList3.get(y).substring(1));
                     */
                    List<LfEmployee> employeeList = wxSendBiz.findEmpMobilesByDepIds(corpCode, depIdsList3.get(y).substring(1));

                    for (LfEmployee employee : employeeList) {
                        phones.append(employee.getMobile()).append(",");
                    }
                }
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????????????????!");
        }
        return phones.toString();
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void readSmsContent(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //??????????????????
        String url = request.getParameter("url");
        //????????????
        url = txtfileutil.getPhysicsUrl(url);
        //????????????
        String sendType = request.getParameter("sendType");
        BufferedReader reader = null;
        String tmp = "";
        String phoneStr = "";
        //
        String smsContent = "";
        int index;
        int x = 0;
        CommonVariables cv = new CommonVariables();
        int ishidephome = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(url)), "GBK"));
            Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
            if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                ishidephome = 1;
            }


            //????????????????????????
            if ("2".equals(sendType)) {
                /*?????? ???????????? ??????????????? ??????????????? ????????????*/
                response.getWriter().print("<thead><tr align='center'><th>" + MessageUtils.extractMessage("ydwx", "ydwx_wxgl_hdxgl_bianhao", request) + "</th><th><center><div style='width:89px'>" + MessageUtils.extractMessage("ydwx", "ydwx_wxcxtj_hftj_shoujihaoma", request) + "</div></center></th>" +
                        "<th>" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_1", request) + "</th><th>" + MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_2", request) + "</th>" +
                        "<th>" + MessageUtils.extractMessage("ydwx", "ydwx_wxcxtj_fwtj_dxnr", request) + "</th></tr></thead><tbody>");
                //????????????
                while ((tmp = reader.readLine()) != null) {
                    x++;
                    tmp = tmp.trim();
                    //????????????
                    String[] snum = tmp.split("MWHS]#");
                    //?????????????????????
                    String phone = snum[2] != null ? snum[2] : "";
                    if (phone != null && !"".equals(phone) && ishidephome == 0) {
                        phone = cv.replacePhoneNumber(phone);
                    }
                    response.getWriter().print("<tr align ='center'><td>" + x + "</td><td>" + phone + "</td>" +
                            "<td>" + snum[0] + "</td><td>" + snum[1] + "</td>" +
                            "<td><xmp style='word-break: break-all;white-space:normal;'>" + snum[3] + "</xmp></td></tr>");
                }
                response.getWriter().print("</tbody>");
            }
            //????????????????????????
            else {
                /*?????? ???????????? ????????????*/
                response.getWriter().print("<thead><tr align='center'><th>" + MessageUtils.extractMessage("ydwx", "ydwx_wxgl_hdxgl_bianhao", request) + "</th>" +
                        "<th><center><div style='width:89px'>" + MessageUtils.extractMessage("ydwx", "ydwx_wxcxtj_hftj_shoujihaoma", request) + "</div></center></th>" +
                        "<th>" + MessageUtils.extractMessage("ydwx", "ydwx_wxcxtj_fwtj_dxnr", request) + "</th></thead><tbody>");
                while ((tmp = reader.readLine()) != null) {
                    x++;
                    tmp = tmp.trim();
                    index = tmp.indexOf(",");
                    phoneStr = tmp.substring(0, index);
                    smsContent = tmp.substring(index + 1).trim();
                    //?????????????????????
                    String phone = phoneStr != null ? phoneStr : "";
                    if (phone != null && !"".equals(phone) && ishidephome == 0) {
                        phone = cv.replacePhoneNumber(phone);
                    }
                    response.getWriter().print("<tr align ='center'><td >" + x
                            + "</td><td>" + phone + "</td>" +
                            "<td ><xmp style='word-break: break-all;white-space:normal;'>"
                            + smsContent + "</xmp></td></tr>");
                    if (x == 10) {
                        break;
                    }
                }
                response.getWriter().print("</tbody>");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "???Session???????????????????????????");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     */
    public void getNetTemplate(HttpServletRequest request, HttpServletResponse response) {
        String pageSize = request.getParameter("pageSize");
        String lgcorpcode = request.getParameter("lgcorpcode");
        String name = request.getParameter("name");
        //String lguserid=request.getParameter("lguserid");
        //???????????? session????????????????????????
        Long lguserid = SysuserUtil.longLguserid(request);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        LfWXBASEINFO info = new LfWXBASEINFO();
        info.setNAME(name);
        info.setCORPCODE(lgcorpcode);
        info.setSTATUS(2);
        info.setTIMEOUT(now);
        info.setTempType(1);//?????????????????????

        //?????????????????????????????? 0???????????????????????????1?????????????????????
        if (StaticValue.getIsWxOperatorReview() == 1) {
            info.setOperAppStatus(1);//?????????????????????
        }

        if (lguserid != null) {
            info.setCREATID(lguserid);
        }
        try {
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(Integer.parseInt(pageSize));
            List<DynaBean> infos = new WXSendBiz().getNetTemplate(info, pageInfo);
            //List<LfWXBASEINFO> infos = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("baseInfos", infos);
            request.setAttribute("baseinfo", info);
            request.getRequestDispatcher(empRoot + "/sendWX/wx_netTemplate.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????????????????!");
        }
    }

    /**
     * ????????????????????? ????????? ????????????
     *
     * @param request
     * @param response
     */
    public void getClientSecondDepJson(HttpServletRequest request, HttpServletResponse response) {
        try {
            String depId = request.getParameter("depId");
            String userid = request.getParameter("userid");
            LfSysuser lfSysuser = getLoginUser(request);
            //??????????????????????????????
            List<LfClientDep> clientDepList = new WXSendBiz().getCliSecondDepTreeByUserIdorDepId(userid, depId, lfSysuser.getCorpCode());
            LfClientDep dep = null;
            StringBuffer tree = new StringBuffer("");
            if (clientDepList != null && clientDepList.size() > 0) {
                tree.append("[");
                for (int i = 0; i < clientDepList.size(); i++) {
                    dep = clientDepList.get(i);
                    tree.append("{");
                    tree.append("id:").append(dep.getDepId() + "");
                    tree.append(",name:'").append(dep.getDepName()).append("'");
                    tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
                    //???????????????????????????id
                    if (dep.getParentId() - 0 == 0) {
                        tree.append(",pId:").append(0);
                    } else {
                        tree.append(",pId:").append(dep.getParentId());
                    }
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if (i != clientDepList.size() - 1) {
                        tree.append(",");
                    }
                }
                tree.append("]");
            }
            response.getWriter().print(tree.toString());
        } catch (Exception e) {
            try {
                response.getWriter().print("");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "?????????????????????????????????");
            }
            EmpExecutionContext.error(e, "?????????????????????????????????!");
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getClientByDepId(HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        try {
            StringBuffer sb = new StringBuffer();
            String depId = request.getParameter("depId");
            String pageIndex = "10";
            if (request.getParameter("pageIndex") != null && !"".equals(request.getParameter("pageIndex"))) {
                pageIndex = request.getParameter("pageIndex");
            }
            String epname = request.getParameter("epname");
            String lgcorpcode = request.getParameter("lgcorpcode");
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(50);
            List<DynaBean> beanList = null;

            if (epname != null && !"".equals(epname.trim())) {
                //???????????????????????????????????? ??????
                pageInfo.setPageIndex(10);
                beanList = new WXSendBiz().getClients(epname, lgcorpcode, 2, pageInfo);
            } else {
                //????????????????????????
                LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
                pageInfo.setPageIndex(Integer.valueOf(pageIndex));
                beanList = new WXSendBiz().getClientsByDepId(clientDep, 2, pageInfo);
            }
            if (beanList != null && beanList.size() > 0) {
                sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
                for (DynaBean bean : beanList) {
                    sb.append("<option value='").append(String.valueOf(bean.get("guid")));
                    sb.append("' isdep='5' et=''  mobile='" + String.valueOf(bean.get("mobile")) + "'>");
                    sb.append(String.valueOf(bean.get("name")).replace("<", "&lt;").replace(">", "&gt;")).append("</option>");
                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "??????????????????????????????????????????!");
        }
    }

    /**
     * ???????????? ??????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getEmployeeByDepId(HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
            StringBuffer sb = new StringBuffer();
            String depId = request.getParameter("depId");
            String pageIndex = request.getParameter("pageIndex");
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(10);
            pageInfo.setPageIndex(Integer.valueOf(pageIndex));
            orderByMap.put("employeeId", StaticValue.ASC);
            conditionMap.put("depId", depId);
            List<LfEmployee> lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, null, conditionMap, orderByMap, pageInfo);
            if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
                sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
                for (LfEmployee user : lfEmployeeList) {
                    sb.append("<option value='").append(user.getGuId()).append("' isdep='4' et='' moblie='" + user.getMobile() + "'>");
                    sb.append(user.getName().trim().replace("<", "&lt;").replace(">", "&gt;")).append("</option>");
                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "???????????? ????????????????????????!");
        }
    }

    /**
     * ????????????ID???????????????/???????????? ?????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getGroupUserByGroupId(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        try {
            StringBuffer sb = new StringBuffer();
            String groupId = request.getParameter("depId");
            String pageIndex = request.getParameter("pageIndex");
            String type = request.getParameter("type");
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(10);
            pageInfo.setPageIndex(Integer.valueOf(pageIndex));
            List<GroupInfoVo> groupInfoList = null;
            groupInfoList = new WXSendBiz().getGroupUser(Long.valueOf(groupId), pageInfo, type);
            if (groupInfoList != null && groupInfoList.size() > 0) {
                sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
                for (GroupInfoVo user : groupInfoList) {
                    //????????????
                    String l2gtype = "0";
                    if (user.getL2gType() == 0) {
                        //??????
                        l2gtype = "4";
                    } else if (user.getL2gType() == 1) {
                        //??????
                        l2gtype = "5";
                    } else if (user.getL2gType() == 2) {
                        //?????????
                        l2gtype = "6";
                    }
                    if (user.getName() != null) {
                        sb.append("<option value='").append(user.getGuId()).append("' isdep='" + l2gtype + "' et='' mobile='" + user.getMobile() + "'>");
                        sb.append(user.getName().trim().replace("<", "&lt;").replace(">", "&gt;")).append("</option>");
                    } else {
                        continue;
                    }

                }
            }
            response.getWriter().print(sb.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "????????????ID????????????!");
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void isClientDepContained(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //???????????????ID
            String depId = request.getParameter("depId");
            //????????????????????????ID
            String clientDepIds = request.getParameter("cliDepIds");
            //??????IDS
            String[] depIds = clientDepIds.split(",");
            //????????????
            List<LfClientDep> lfClientDepList = null;
            //????????????????????????ID?????????
            LinkedHashSet<Long> depIdsSet = new LinkedHashSet<Long>();
            //??????????????????
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //????????????
            LfClientDep dep = null;
            //??????
            for (int i = 0; i < depIds.length; i++) {
                String id = depIds[i];
                //???????????????????????????????????????????????????
                if (id == null || "".equals(id)) {
                    continue;
                }
                //????????????????????????
                if (id.equals(depId)) {
                    response.getWriter().print("depExist");
                    return;
                    //??????????????????????????????????????????
                } else if (id.contains("e")) {
                    Long temp = Long.valueOf(id.substring(1));
                    conditionMap.clear();
                    dep = null;
                    lfClientDepList = null;
                    dep = baseBiz.getById(LfClientDep.class, temp);
                    if (dep != null) {
                        conditionMap.put("deppath&like", dep.getDeppath());
                        conditionMap.put("corpCode", dep.getCorpCode());
                        lfClientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
                        if (lfClientDepList != null && lfClientDepList.size() > 0) {
                            for (int j = 0; j < lfClientDepList.size(); j++) {
                                depIdsSet.add(lfClientDepList.get(j).getDepId());
                            }
                        }
                    }
                    //?????????????????????????????????
                } else {
                    dep = null;
                    dep = baseBiz.getById(LfClientDep.class, Long.valueOf(id));
                    if (dep != null) {
                        depIdsSet.add(dep.getDepId());
                    }
                }
            }
            boolean isFlag = false;
            //???????????????????????????
            if (depIdsSet.size() > 0) {
                Long tempDepId = Long.valueOf(depId);
                isFlag = depIdsSet.contains(tempDepId);
            }
            //??????
            if (isFlag) {
                response.getWriter().print("depExist");
                return;
            } else {
                response.getWriter().print("noExist");
                return;
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "???????????????????????????????????????!");
        }
    }

    /**
     * ???????????? ????????????????????????????????????????????????????????????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void isClientDepContaineDeps(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //?????????????????? 0?????????   1??????
            String ismut = request.getParameter("ismut");
            //??????ID
            String depId = request.getParameter("depId");
            //??????????????????
            if ("0".equals(ismut)) {
                //???????????????????????? ???????????????????????????????????????
                //String number = smsBiz.getClientCountByDepId(depId);
                LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
                if (clientDep == null) {
                    response.getWriter().print("nobody");
                    return;
                }
                String number = new WXSendBiz().getDepClientCount(clientDep, 2).toString();
                if (number != null && !"".equals(number)) {
                    if ("0".equals(number)) {
                        response.getWriter().print("nobody");
                    } else {
                        response.getWriter().print(number);
                    }
                } else {
                    response.getWriter().print("nobody");
                }
                return;
            }
            //???????????????????????????
            List<LfClientDep> lfClientDepList = null;
            LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
            //????????????
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //??????????????????
            String cliDepIds = request.getParameter("cliDepIds");
            String[] depIds = cliDepIds.split(",");
            List<Long> depIdExistList = new ArrayList<Long>();
            //??????
            for (int j = 0; j < depIds.length; j++) {
                String id = depIds[j];
                if (id != null && !"".equals(id)) {
                    if (id.indexOf("e") > -1) {
                        if (!"".equals(id.substring(1))) {
                            depIdExistList.add(Long.valueOf(id.substring(1)));
                        }
                    } else {
                        depIdExistList.add(Long.valueOf(id));
                    }
                }
            }
            //????????????????????????????????????????????????????????????set??????
            LfClientDep dep = baseBiz.getById(LfClientDep.class, Long.valueOf(depId));
            if (dep != null) {
                conditionMap.put("deppath&like", dep.getDeppath());
                conditionMap.put("corpCode", dep.getCorpCode());
                lfClientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
                if (lfClientDepList != null && lfClientDepList.size() > 0) {
                    for (int i = 0; i < lfClientDepList.size(); i++) {
                        depIdSet.add(lfClientDepList.get(i).getDepId());
                    }
                    //??????????????????????????????ID????????????
                    List<Long> depIdListTemp = new ArrayList<Long>();
                    for (int a = 0; a < depIdExistList.size(); a++) {
                        if (depIdSet.contains(depIdExistList.get(a))) {
                            depIdListTemp.add(depIdExistList.get(a));
                        }
                    }

                    //????????????????????????????????????????????????????????????????????????????????????????????????select???html
                    String depids = depIdSet.toString();
                    depids = depids.substring(1, depids.length() - 1);
                    //??????????????????
                    //String countttt = smsBiz.getClientCountByDepId(depids);

                    String countttt = new WXSendBiz().getDepClientCount(dep, 1).toString();

                    if (countttt != null && !"".equals(countttt)) {
                        if ("0".equals(countttt)) {
                            response.getWriter().print("nobody");
                            return;
                        } else if (depIdListTemp.size() > 0) {
                            String tempDeps = depIdListTemp.toString();
                            tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
                            response.getWriter().print(countttt + "," + tempDeps);
                            return;
                        } else {
                            response.getWriter().print("notContains" + "&" + countttt);
                            return;
                        }
                    } else {
                        response.getWriter().print("nobody");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "???????????????????????????????????????????????????!");
            response.getWriter().print("errer");
        }
    }

    /**
     * ???????????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getClientDepCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //?????????????????? 0?????????   1??????
            String ismut = request.getParameter("ismut");
            //??????ID
            String depId = request.getParameter("depId");
            //??????????????????
            if ("0".equals(ismut)) {
                //??????????????????????????????
                //???????????????????????? ???????????????????????????????????????
                //String number = smsBiz.getClientCountByDepId(depId);
                LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
                if (clientDep == null) {
                    response.getWriter().print("nobody");
                    return;
                }
                String number = new WXSendBiz().getDepClientCount(clientDep, 2).toString();
                if (number != null && !"".equals(number)) {
                    if ("0".equals(number)) {
                        response.getWriter().print("nobody");
                        return;
                    } else {
                        response.getWriter().print(number);
                        return;
                    }
                } else {
                    response.getWriter().print("nobody");
                    return;
                }
            } else if ("1".equals(ismut)) {
                LfClientDep dep = baseBiz.getById(LfClientDep.class, Long.valueOf(depId));
                if (dep != null) {
                    String number = new WXSendBiz().getDepClientCount(dep, 1).toString();
                    response.getWriter().print(number);
                    return;
                }
            }
            response.getWriter().print("nobody");
            return;
        } catch (Exception e) {
            response.getWriter().print("errer");
            EmpExecutionContext.error(e, "?????????????????????????????????!");
        }
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void setDefault(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //????????????
        String result = "fail";
        try {
            //String lguserid = request.getParameter("lguserid");
            //???????????? session????????????????????????
            String lguserid = SysuserUtil.strLguserid(request);


            String lgcorpcode = request.getParameter("lgcorpcode");
            String busCode = request.getParameter("busCode");
            String spUser = request.getParameter("spUser");
            String flag = request.getParameter("flag");
            if (flag == null || "".equals(flag)) {
                EmpExecutionContext.error("?????????????????????????????????????????????????????????");
                response.getWriter().print(result);
                return;
            }
            if (lguserid == null || "".equals(lguserid)) {
                EmpExecutionContext.error("?????????????????????????????????????????????????????????lguserid???" + lguserid);
                response.getWriter().print(result);
                return;
            }
            if (busCode == null || "".equals(busCode)) {
                EmpExecutionContext.error("?????????????????????????????????????????????????????????busCode???" + busCode);
                response.getWriter().print(result);
                return;
            }
            if (spUser == null || "".equals(spUser)) {
                EmpExecutionContext.error("?????????????????????????????????????????????????????????spUser???" + spUser);
                response.getWriter().print(result);
                return;
            }

            //?????????????????????
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userid", lguserid);
            conditionMap.put("flag", flag);

            //????????????
            LfDfadvanced lfDfadvanced = new LfDfadvanced();
            lfDfadvanced.setUserid(Long.parseLong(lguserid));
            lfDfadvanced.setBuscode(busCode);
            lfDfadvanced.setSpuserid(spUser);
            lfDfadvanced.setFlag(Integer.parseInt(flag));
            lfDfadvanced.setCreatetime(new Timestamp(System.currentTimeMillis()));

            result = wxSendBiz.setDefault(conditionMap, lfDfadvanced);

            //????????????
            String opResult = "???????????????????????????????????????????????????";
            if (result != null && "seccuss".equals(result)) {
                opResult = "???????????????????????????????????????????????????";
            }
            //???????????????
            LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
            //???????????????
            String opUser = sysuser == null ? "" : sysuser.getUserName();
            //??????????????????
            StringBuffer content = new StringBuffer();
            content.append("[???????????????SP??????](").append(busCode).append("???").append(spUser).append(")");

            //????????????
            EmpExecutionContext.info("??????????????????", lgcorpcode, lguserid, opUser, opResult + content.toString(), "OTHER");

            response.getWriter().print(result);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "???????????????????????????????????????????????????");
            response.getWriter().print(result);
        }
    }

    /**
     * ????????????
     *
     * @param request
     * @param response
     * @description
     * @author zhangsan <zhangsan@126.com>
     * @datetime 2016-1-18 ??????09:24:54
     */
    public void getTmpContext(HttpServletRequest request, HttpServletResponse response) {

        String lgcorpcode = request.getParameter("lgcorpcode");
        String name = request.getParameter("name");
        //String lguserid=request.getParameter("lguserid");
        //???????????? session????????????????????????
        String lguserid = SysuserUtil.strLguserid(request);


        Timestamp now = new Timestamp(System.currentTimeMillis());
        LfWXBASEINFO info = new LfWXBASEINFO();
        info.setNAME(name);
        info.setCORPCODE(lgcorpcode);
        info.setSTATUS(2);
        info.setTIMEOUT(now);
        info.setTempType(1);//?????????????????????
        if (lguserid != null && !"".equals(lguserid)) {
            info.setCREATID(new Long(lguserid));
        }
        try {
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            pageInfo.setPageSize(10);
            List<DynaBean> infos = new WXSendBiz().getNetTemplate(info, pageInfo);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("baseInfos", infos);
            request.setAttribute("baseinfo", info);
            request.getRequestDispatcher(empRoot + "/sendWX/wx_netContext.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????????????????????????????!");
        }

    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     */
    public void toDraft(HttpServletRequest request, HttpServletResponse response) {

        //?????????id
        String draftid = "";
        //????????????
        String taskname = "";
        //?????????ID
        Long lguserid = null;
        //?????????????????? ????????????
        String selDraftFilePath = "";
        // ????????????
        String lgcorpcode = "";
        //???????????????
        String phoneStr = "";
        //??????
        String smsTail = "";
        String depIdStr = "";
        String groupClient = "";
        //????????????IDS
        String cliDepIds = "";
        //??????????????????
        String groupStr = "";
        //??????????????????
        JSONObject json = new JSONObject();
        //***????????????????????????????????????***
        StringBuffer opContent = new StringBuffer();
        json.put("ok", "0");
        //??????????????????????????????
        List<String> phoneList = new ArrayList<String>();
        //???????????????
        Long effCount = 0l;
        StringBuffer contentSb = new StringBuffer();
        //??????
        String msg = "";
        String strlguserid = null;
        try {
            //strlguserid = request.getParameter("lguserid");
            //???????????? session????????????????????????
            strlguserid = SysuserUtil.strLguserid(request);

            lguserid = Long.valueOf(strlguserid == null ? "0" : strlguserid);
            String[] url = txtfileutil.getSaveUrl(lguserid);
            DiskFileItemFactory factory = new DiskFileItemFactory();
            File Newfile = null;
            factory.setSizeThreshold(1024 * 1024);
            String temp = url[0].substring(0, url[0].lastIndexOf("/"));
            factory.setRepository(new File(temp));
            ServletFileUpload upload = new ServletFileUpload(factory);
            File zipUrl = null;
            List<FileItem> fileList = null;
            try {
                //???????????????????????????
                fileList = upload.parseRequest(request);
            } catch (FileUploadException e) {
                EmpExecutionContext.error(e, "??????????????????");
                throw e;
            }

            Iterator<FileItem> it = fileList.iterator();
            List<BufferedReader> readerList = new ArrayList<BufferedReader>();
            FileItem fileItem = null;
            int fileIndex = 0;
            int fileCount = 0;
            while (it.hasNext()) {
                fileItem = (FileItem) it.next();
                String fileName = fileItem.getFieldName();
                if (fileName.equals("phoneStr")) {
                    //????????????
                    phoneStr = fileItem.getString("UTF-8").toString();
                    if (",".equals(phoneStr)) {//????????????","??? ?????????????????????
                        phoneStr = "";
                    } else if (phoneStr.indexOf(",,") > -1) {//?????????????????????
                        phoneStr = phoneStr.replace(",,", ",");
                    }

                } else if (fileName.equals("empDepIds")) {
                    //??????id
                    depIdStr = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("cliDepIds")) {
                    //????????????ids
                    cliDepIds = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("groupClient")) {
                    //??????id
                    groupClient = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("groupIds")) {
                    //??????id
                    groupStr = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("taskname")) {
                    //??????
                    taskname = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("draftFile")) {
                    selDraftFilePath = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("draftId")) {
                    draftid = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("msg")) {
                    //??????
                    msg = fileItem.getString("UTF-8").toString();
                    //??????????????????????????????
                    msg = msg + smsTail;
                    msg = smsBiz.smsContentFilter(msg);
                } else if (fileName.equals("lguserid")) {
                    //???????????????userid
                    lguserid = Long.parseLong(fileItem.getString("UTF-8").toString());
                } else if (fileName.equals("lgcorpcode")) {
                    //????????????
                    lgcorpcode = fileItem.getString("UTF-8").toString();
                }
                //??????????????????
                else if (!fileItem.isFormField() && fileItem.getName().length() > 0) {

                    //??????????????????????????????????????????????????????????????????????????????
                    fileCount++;

                    String fileCurName = fileItem.getName();


                    String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();

                    if (fileType.equals(".zip")) {

                        //??????????????????
                        String zipFileStr = url[0].replace(".txt", "_" + fileCount + ".zip");
                        zipUrl = new File(zipFileStr);
                        //????????????????????????????????????
                        fileItem.write(zipUrl);
                        ZipFile zipFile = new ZipFile(zipUrl);
                        Enumeration zipEnum = zipFile.getEntries();
                        ZipEntry entry = null;
                        while (zipEnum.hasMoreElements()) {
                            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                            fileIndex++;
                            //???????????????????????????????????????????????????????????????
                            String fileIndexStr = String.valueOf(fileIndex) + "_" + String.valueOf(fileCount);
                            entry = (ZipEntry) zipEnum.nextElement();
                            //??????txt??????
                            if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".txt")) {
                                InputStream instream = zipFile.getInputStream(entry);
                                String charset = get_charset(instream);
                                BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), charset));
                                if (charset.startsWith("UTF-")) {
                                    reader.read(new char[1]);
                                }
                                readerList.add(reader);
                            } else if (!entry.isDirectory() && (entry.getName().toLowerCase().endsWith(".xls") || entry.getName().toLowerCase().endsWith(".et"))) {
                                readerList.add(jxExcel(url[0], Newfile, zipFile.getInputStream(entry), fileIndexStr, 1));
                            } else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xlsx")) {
                                readerList.add(jxExcel(url[0], Newfile, zipFile.getInputStream(entry), fileIndexStr, 2));
                            }
                        }
                    } else if (fileType.equals(".xls") || fileType.equals(".et")) {
                        //?????????????????????excel2003??????wps??????
                        String FileStr = url[0];
                        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + "_" + fileCount + ".txt";
                        Newfile = new File(FileStrTemp);
                        FileOutputStream fos = new FileOutputStream(Newfile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
                        BufferedWriter bw = new BufferedWriter(osw);
                        String phoneNum = "";
                        String Context = "";
                        try {
                            HSSFWorkbook workbook = new HSSFWorkbook(fileItem.getInputStream());
                            //???????????????
                            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                                HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                // ???????????????
                                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                    HSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    //??????????????????????????????
                                    phoneNum = getCellFormatValue(row.getCell(0));
                                    Context = "";
                                    //???????????????????????????,???????????????
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        HSSFCell cell = row.getCell(k);
                                        if (cell != null && cell.toString().length() > 0) {
                                            Context += "," + getCellFormatValue(cell);
                                        }

                                    }
                                    //?????????????????????????????????txt????????????
                                    bw.write(phoneNum + Context + line);
                                }
                            }
                            bw.close();
                            osw.close();
                            fos.close();
                            FileInputStream fis = new FileInputStream(Newfile);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
                            readerList.add(br);
                        } catch (FileNotFoundException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        }
                    } else if (fileType.equals(".xlsx")) {
                        String FileStr = url[0];
                        String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + "_" + fileCount + ".txt";
                        Newfile = new File(FileStrTemp);
                        FileOutputStream fos = new FileOutputStream(Newfile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
                        BufferedWriter bw = new BufferedWriter(osw);
                        String phoneNum = "";
                        String Context = "";
                        try {
                            XSSFWorkbook workbook = new XSSFWorkbook(fileItem.getInputStream());
                            //???????????????
                            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                                XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                // ???????????????
                                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                                    XSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    phoneNum = getCellFormatValue(row.getCell(0));
                                    Context = "";
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        XSSFCell cell = row.getCell(k);
                                        if (cell != null && cell.toString().length() > 0) {
                                            Context += "," + getCellFormatValue(cell);
                                        }
                                    }
                                    bw.write(phoneNum + Context + line);
                                }
                            }
                            bw.close();
                            osw.close();
                            fos.close();

                            FileInputStream fis = new FileInputStream(Newfile);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "GBK"));
                            readerList.add(br);
                        } catch (FileNotFoundException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, lguserid, lgcorpcode);
                        }
                    } else {
                        InputStream instream = fileItem.getInputStream();
                        String charset = get_charset(instream);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(), charset));
                        if (charset.startsWith("UTF-")) {
                            reader.read(new char[1]);
                        }
                        readerList.add(reader);
                    }
                }
            }// ????????????
            BufferedReader reader;
            //??????????????????
            String tmp;
            try {
                for (int r = 0; r < readerList.size(); r++) {
                    reader = readerList.get(r);
                    while ((tmp = reader.readLine()) != null) {
                        tmp = tmp.trim();
                        phoneList.add(tmp);
                    }
                }
            } catch (Exception e) {
                txtfileutil.deleteFile(url[0]);
                EmpExecutionContext.error(e, lguserid, lgcorpcode);
                throw e;
            } finally {
    			try{
    				IOUtils.closeReaders(getClass(), readerList);
    			}catch(IOException e){
    				EmpExecutionContext.error(e, "");
    			}
                readerList.clear();

                readerList = null;
                if (zipUrl != null) {
                    if (!zipUrl.delete()) {
                    }
                    ;
                }
                String FileStr = url[0];
                String FileStrTemp;
                String fileStr2;
                File file = null;
                //??????????????????
                for (int j = 0; j < fileCount; j++) {
                    //???????????????????????????excel???????????????
                    int b = j + 1;
                    fileStr2 = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp_" + b + ".txt";
                    file = new File(fileStr2);
                    if (file.exists()) {
                        if (!file.delete()) {
                        }
                    }
                    //???????????????????????????????????????
                    for (int i = 0; i < fileIndex; i++) {
                        int a = i + 1;
                        FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt")) + "_temp" + a + "_" + b + ".txt";
                        file = new File(FileStrTemp);
                        if (file.exists()) {
                            if (!file.delete()) {
                            }
                        }
                    }
                }

                if (Newfile != null) {
                    if (!Newfile.delete()) {
                    }
                }
            }
            //????????????????????????????????????
            if (depIdStr.length() > 0) {
                //????????????id????????????
                phoneStr = phoneStr + getEmpByDepId2(depIdStr, lgcorpcode);
                String tempdepIdStr = depIdStr.substring(0, depIdStr.length() - 1);
                //???????????????e+id???,??????
                if (tempdepIdStr.indexOf("e") > -1) {
                    tempdepIdStr = tempdepIdStr.replaceAll("e", "");
                }
                opContent.append("???????????????id:" + tempdepIdStr).append("???");

            }
            //????????????????????????????????????
            if (!StringUtils.isEmpty(cliDepIds)) {
                //????????????ID???????????????????????????
                phoneStr = wxSendBiz.getClientPhoneStrByDepId(phoneStr, cliDepIds, lgcorpcode);
                String tempcliDepIds = cliDepIds.substring(0, cliDepIds.length() - 1);
                //???????????????e+id???,??????
                if (tempcliDepIds.indexOf("e") > -1) {
                    tempcliDepIds = tempcliDepIds.replaceAll("e", "");
                }
                opContent.append("???????????????id:" + tempcliDepIds).append("???");

            }
            if (groupStr.length() > 0 || groupClient.length() > 0) {
                //????????????????????????
                phoneStr = phoneStr + getEmpByGroupStr(groupStr, groupClient);
                if (groupClient.length() > 0) {
                    String tempgroupClient = groupClient.substring(0, groupClient.length() - 1);
                    /*????????????*/
                    opContent.append(MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_3", request) + " id:" + tempgroupClient).append("???");
                }
                if (groupStr.length() > 0) {
                    String tempgroupStr = groupStr.substring(0, groupStr.length() - 1);
                    /*????????????*/
                    opContent.append(MessageUtils.extractMessage("ydwx", "ydwx_src_sendDSM_4", request) + " id:" + tempgroupStr).append("???");
                }

            }

            if (phoneStr.length() > 0) {

                String[] phones = phoneStr.split(",");
                for (String num : phones) {
                    if (num != null) {
                        num = num.trim();
                    }
                    if (num != null && num.length() > 6 && num.length() < 22) {
                        //???????????????????????????????????????????????????????????????
                        phoneList.add(num.trim());
                    }
                }
            }


            // 0.?????????????????????
            if (StringUtils.isNotBlank(selDraftFilePath)) {
                TxtFileUtil txtFileUtil = new TxtFileUtil();
                String webRoot = txtFileUtil.getWebRoot();
                File draftFile = new File(webRoot, selDraftFilePath);
                if (!draftFile.exists() && StaticValue.getISCLUSTER() == 1) {
                    CommonBiz comBiz = new CommonBiz();
                    String downloadRes = "error";
                    //??????????????????
                    int retryTime = 3;
                    while (!"success".equals(downloadRes) && retryTime-- > 0) {
                        downloadRes = comBiz.downloadFileFromFileCenter(selDraftFilePath);
                    }
                    if (!"success".equals(downloadRes)) {
                        EmpExecutionContext.error("????????????????????????????????????????????????????????????");
                    }
                }
                if (!draftFile.exists()) {
                    EmpExecutionContext.error("?????????????????????????????????????????????");
                    //??????????????????????????????
                    json.put("ok", "-1");
                    return;
                }
                BufferedReader bufferedReader = null;
                try {
                    //??????????????? ????????????????????????
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(draftFile), "gbk"));
                    String tmps = null;
                    while ((tmps = bufferedReader.readLine()) != null) {
                        phoneList.add(tmps.trim());
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error("????????????????????????????????????????????????");
                    return;
                } finally {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                }
            }

            //???????????????
            LfDrafts drafts = new LfDrafts();

            //?????????????????????
            TxtFileUtil txtFileUtil = new TxtFileUtil();
            String physicsUrl = "";
            String webRoot = txtFileUtil.getWebRoot();
            String uploadPath = StaticValue.FILEDIRNAME + "drafttxt/";
            //???????????????????????????
            String dirPath = txtFileUtil.createDir(webRoot + uploadPath, Calendar.getInstance());
            GetSxCount sx = GetSxCount.getInstance();
            //???????????????
            String saveName = "draft" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(System.currentTimeMillis()) + sx.getCount() + ".txt";
            //??????????????????????????????????????????
            String newDraftFilePath = uploadPath + dirPath + saveName;
            //?????????
            physicsUrl = webRoot + newDraftFilePath;

            if (draftid == null || "".equals(draftid.trim())) {
                drafts.setCorpcode(lgcorpcode);
                drafts.setCreatetime(new Timestamp(System.currentTimeMillis()));
                //???????????????0??????????????????1??????????????????????????????2??????????????????????????????3????????????????????????4-?????????????????????5?????????????????????
                drafts.setDraftstype(4);
                drafts.setUserid(lguserid);
                drafts.setMobileurl(newDraftFilePath);
            } else {
                drafts.setId(Long.parseLong(draftid));
                drafts.setMobileurl(newDraftFilePath);
            }
            //??????????????? ??????????????? ?????? ???????????????
            txtFileUtil.emptyTxtFile(physicsUrl);

            //????????????????????????
            String line = System.getProperties().getProperty(StaticValue.LINE_SEPARATOR);
            StringBuffer contentS = new StringBuffer("");
            //???????????????
            int count = 0;
            for (String phone : phoneList) {
                contentS.append(phone + line);
                count++;
                //??????5000 ???????????????
                if (count % 5000 == 0) {
                    txtFileUtil.writeToTxtFile(physicsUrl, contentS.toString());
                    contentS.setLength(0);
                    count = 0;
                }
            }
            if (count > 0) {
                // ????????????????????????
                txtFileUtil.writeToTxtFile(physicsUrl, contentS.toString());
                contentS.setLength(0);
            }

            //????????????????????????????????????????????????
            if (StaticValue.getISCLUSTER() == 1) {
                //??????????????????????????????
                CommonBiz comBiz = new CommonBiz();
                boolean upFileRes = false;
                //??????????????????
                int retryTime = 3;
                while (!upFileRes && retryTime-- > 0) {
                    upFileRes = comBiz.upFileToFileServer(newDraftFilePath);
                }
                if (!upFileRes) {
                    EmpExecutionContext.error("????????????????????????????????????????????????????????????????????????" + IErrorCode.B20023);
                    return;
                }
            }

            boolean result = false;
            drafts.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            //??????????????? ??????????????????????????? ???oracle??????????????????
            drafts.setMsg(StringUtils.defaultIfEmpty(msg, " "));
            drafts.setTitle(StringUtils.defaultIfEmpty(taskname, " "));
            // ?????????taskname ?????????msg ????????????????????????physicsUrl ??????
            if (draftid == null || "".equals(draftid.trim())) {
                Long id = baseBiz.addObjReturnId(drafts);
                if (id != null && id > 0) {
                    drafts.setId(id);
                    result = true;
                }
            } else {
                result = baseBiz.updateObj(drafts);
                if (!result) {
                    //??????????????????????????????
                    json.put("ok", "-2");
                    return;
                }
            }

            json.put("draftid", drafts.getId());
            json.put("ok", result ? "1" : "0");
        } catch (EMPException empex) {
            EmpExecutionContext.error(empex, lguserid, lgcorpcode);
        } catch (Exception e) {
            EmpExecutionContext.error(e, lguserid, lgcorpcode);
        } finally {
            request.setAttribute("result", json.toString());
            try {
                request.getRequestDispatcher(empRoot + "/sendWX/wx_todraft.jsp").forward(request, response);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "???????????????????????????????????????");
            }
        }
    }


    /**
     * ?????????
     *
     * @param request   ????????????
     * @param opModule  ????????????
     * @param opContent ????????????
     * @param opType
     */
    public void setLog(HttpServletRequest request, String opModule, String opContent, String opType) {
        try {
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), opContent, opType);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, opModule + opType + opContent + "??????????????????");
        }
    }

    public void getDrafts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageInfo pageInfo = new PageInfo();
        List<DynaBean> list = new ArrayList();
        String lgcorpcode = null;
        String lguserid = null;

        try {
            Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
            pageSet(pageInfo, request);
            pageInfo.setPageSize(pageSize);
            String taskName = request.getParameter("taskname");
            String msg = request.getParameter("msg");
            String starttime = request.getParameter("starttime");
            String endtime = request.getParameter("endtime");
            String draftstype = request.getParameter("draftstype");
            LfSysuser sysuser = getLoginUser(request);
            lgcorpcode = sysuser.getCorpCode();
            lguserid = "" + sysuser.getUserId();
            LinkedHashMap<String, String> condMap = new LinkedHashMap();
            condMap.put("corpcode", lgcorpcode);
            condMap.put("userid", lguserid);
            condMap.put("draftstype", draftstype);
            if (StringUtils.isNotBlank(taskName)) {
                condMap.put("title", taskName.trim());
            }

            if (StringUtils.isNotBlank(msg)) {
                condMap.put("msg", msg.trim());
            }

            if (StringUtils.isNotBlank(starttime)) {
                condMap.put("starttime", starttime);
            }

            if (StringUtils.isNotBlank(endtime)) {
                condMap.put("endtime", endtime);
            }

            list = wxSendBiz.getUserDrafts(condMap, pageInfo);
        } catch (Exception var17) {
            EmpExecutionContext.error("?????????????????????URL:" + request.getRequestURI() + "???lgcorpcode:" + lgcorpcode + "???strlguserid???" + lguserid);
        } finally {
            request.setAttribute("list", list);
            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(empRoot + "/sendWX/wx_draftlist.jsp").forward(request, response);
        }
    }
}
