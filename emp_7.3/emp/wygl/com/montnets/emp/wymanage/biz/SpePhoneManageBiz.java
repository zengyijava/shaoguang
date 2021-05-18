package com.montnets.emp.wymanage.biz;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wy.ASpePhone;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.wymanage.dao.SpePhoneManageDAO;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 特殊号码管理biz
 *
 * @author zhangmin
 * @description
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-3-24 下午04:25:29
 */
public class SpePhoneManageBiz extends SuperBiz{

    private final ChangeCharset charsetUtil = new ChangeCharset();

    private final WyExcelBiz excelbiz = new WyExcelBiz();


    /**
     * 特殊号码管理查询
     *
     * @param pageInfo
     * @param conditionMap
     * @return
     * @description
     * @author zhangmin
     * @datetime 2014-3-24 下午04:29:30
     */
    
    public List<DynaBean> getSpePhone(PageInfo pageInfo, LinkedHashMap<String, String> conditionMap) {

        return new SpePhoneManageDAO().getSpePhone(pageInfo, conditionMap);
    }

    /**
     * 添加号码
     *
     * @param phoneList
     * @return
     * @throws Exception
     * @description
     * @author zhangmin
     * @datetime 2014-3-25 下午04:45:19
     */
    
    public void add(List<ASpePhone> phoneList) throws Exception {
        //获取数据库连接
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            List<ASpePhone> addList = new ArrayList<ASpePhone>();
            for (int i = 0; i < phoneList.size(); i++) {

                addList.add(phoneList.get(i));

                if (i > 0 && i / 3000 == 0) {
                    empTransDao.save(conn, addList, ASpePhone.class);
                    addList.clear();
                }
            }
            if (addList.size() > 0) {
                empTransDao.save(conn, addList, ASpePhone.class);
            }
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "添加特殊号码biz层异常！");
            throw e;
        } finally {
            empTransDao.closeConnection(conn);
        }
    }

    /**
     * 删除号码
     *
     * @param id
     * @return
     * @throws Exception
     */
    
    public int del(String id) throws Exception {
        //获取数据库连接
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
            conMap.put("id&in", id);
            List<ASpePhone> aspList = new BaseBiz().getByCondition(ASpePhone.class, conMap, null);
            conMap.clear();
            conMap.put("id", id);
            int delcount = empTransDao.delete(conn, ASpePhone.class, conMap);
            List<ASpePhone> delList = new ArrayList<ASpePhone>();
            for (int i = 0; i < aspList.size(); i++) {
                ASpePhone asPhone = aspList.get(i);
                asPhone.setId(null);
                asPhone.setCreatetime(new Timestamp(System.currentTimeMillis()));
                asPhone.setOpttype(1);
                delList.add(asPhone);

            }
            empTransDao.save(conn, delList, ASpePhone.class);
            empTransDao.commitTransaction(conn);
            return delcount;
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "删除特殊号码biz层异常！");
            throw e;
        } finally {
            empTransDao.closeConnection(conn);
        }
    }

    /**
     * 删除号码
     *
     * @param id
     * @return
     * @throws Exception
     */
    public String getSphoneList(String id) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
            conMap.put("id&in", id);
            List<ASpePhone> aspList = new BaseBiz().getByCondition(ASpePhone.class, conMap, null);
            if (aspList != null && aspList.size() > 0) {
                for (int i = 0; i < aspList.size(); i++) {
                    ASpePhone asPhone = aspList.get(i);
                    sb.append("ID：").append(asPhone.getId()).append("，手机号码：").append(asPhone.getPhone()).append("，所属运营商：").append(asPhone.getUnicom()).append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询特殊号码biz层异常！");
            throw e;
        }
        return sb.toString();
    }

    /**
     * 修改特殊号码
     *
     * @param id
     * @param unicom
     * @param phone
     * @throws Exception
     */
    
    public void edit(String id, int unicom, String phone) throws Exception {
        //获取数据库连接
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
            conMap.put("id", id);
            List<ASpePhone> aspList = new BaseBiz().findListByCondition(ASpePhone.class, conMap, null);
            int delcount = empTransDao.delete(conn, ASpePhone.class, conMap);
            for (int i = 0; i < aspList.size(); i++) {
                ASpePhone asPhone = aspList.get(i);
                asPhone.setId(null);
                asPhone.setCreatetime(new Timestamp(System.currentTimeMillis()));
                asPhone.setOpttype(1);

                empTransDao.save(conn, asPhone);
            }
            ASpePhone aspPhone = new ASpePhone();
            aspPhone.setCreatetime(new Timestamp(System.currentTimeMillis()));
            aspPhone.setOpttype(0);
            aspPhone.setPhone(phone);
            aspPhone.setUserid("000000");
            aspPhone.setUnicom(unicom);
            aspPhone.setCustid(0);
            aspPhone.setSpectype(10);

            empTransDao.save(conn, aspPhone);
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "修改特殊号码biz层异常！");
            throw e;
        } finally {
            empTransDao.closeConnection(conn);
        }
    }

    /**
     * 解析上传文件，（txt,et,xls,zip），返回文件流
     *
     * @param fileItem  文件对象
     * @param fileCount
     * @return
     * @throws Exception
     */
    
    public List<String> parseFile(FileItem fileItem, int fileCount) throws Exception {
        List<String> phoneList = new ArrayList<String>();
        try {
            // 返回文件流
            List<BufferedReader> readerList = new ArrayList<BufferedReader>();
            // 文件名
            String fileCurName = fileItem.getName();
            // 通过文件名截取到文件类型
            String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();
            // 读取压缩文件流
            if (fileType.equals(".zip")) {
                phoneList.addAll(parseZip(fileItem, fileCount));
            }
            // 解析2003及WPS格式的的excel文件
            else if (fileType.equals(".xls") || fileType.equals(".et")) {
                phoneList.addAll(excelbiz.jxExcel(fileItem.getInputStream(), "_" + String.valueOf(fileCount), 1));
            }
            // 解析excel2007文件
            else if (fileType.equals(".xlsx")) {
                phoneList.addAll(excelbiz.jxExcel(fileItem.getInputStream(), "_" + String.valueOf(fileCount), 2));
            }
            // 解析TXT文本
            else {
                /*InputStream instream = fileItem.getInputStream();
                String charset = charsetUtil.get_charset(instream);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(), charset));
                if (charset.startsWith("UTF-")) {
                    reader.read(new char[1]);
                }*/
                BufferedReader reader = charsetUtil.getReader(fileItem.getInputStream(), fileItem.getInputStream());
                String tmp = "";
                while ((tmp = reader.readLine()) != null) {
                    phoneList.add(tmp);
                }
            }
            return phoneList;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "解析上传文件异常。");
            throw e;
        }
    }

    /**
     * 解析zip文件，返回文件流
     *
     * @param fileItem
     * @param fileCount
     * @return 返回文件流
     * @throws Exception
     */
    private List<String> parseZip(FileItem fileItem, int fileCount) throws Exception {
        List<String> phoneList = new ArrayList<String>();
        File zipUrl = null;
        //获取一个存放临时文件的目录
        String uploadPath = StaticValue.FILEDIRNAME;
        String url = new TxtFileUtil().getWebRoot() + uploadPath + "/" + System.currentTimeMillis() + ".zip";
        try {
            // 返回结果
            List<BufferedReader> returnList = new ArrayList<BufferedReader>();
            // 创建临时文件，获取zip地址
            String zipFileStr = url.replace(".txt", ".zip");
            // TODO:不需要写文件，直接解析
            zipUrl = new File(zipFileStr);
            // 将上传的文件写入临时文件
            fileItem.write(zipUrl);
            ZipFile zipFile = new ZipFile(zipUrl);
            Enumeration zipEnum = zipFile.getEntries();
            ZipEntry entry = null;
            int fileIndex = 0;
            while (zipEnum.hasMoreElements()) {
                // 这个变量的作用是，如果压缩包里面有多个文件，就需要多个临时文件，另外删除临时文件也可以用到
                fileIndex++;

                // 解压缩后的文件名的标签序号，用于删除文件时
                String fileIndexStr = String.valueOf(fileIndex) + "_" + String.valueOf(fileCount);
                entry = (ZipEntry) zipEnum.nextElement();
                // 处理txt文件
                if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".txt")) {
                    /*InputStream instream = zipFile.getInputStream(entry);
                    String charset = charsetUtil.get_charset(instream);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), charset));
                    if (charset.startsWith("UTF-")) {
                        reader.read(new char[1]);
                    }*/
                    BufferedReader reader = charsetUtil.getReader(zipFile.getInputStream(entry), zipFile.getInputStream(entry));
                    String tmp = "";
                    while ((tmp = reader.readLine()) != null) {
                        phoneList.add(tmp);
                    }
                } else if (!entry.isDirectory() && (entry.getName().toLowerCase().endsWith(".xls") || entry.getName().toLowerCase().endsWith(".et"))) {
                    phoneList.addAll(excelbiz.jxExcel(zipFile.getInputStream(entry), fileIndexStr, 1));

                }
                // 如果是.xlsx文件
                else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xlsx")) {
                    phoneList.addAll(excelbiz.jxExcel(zipFile.getInputStream(entry), fileIndexStr, 2));
                }
            }
            return phoneList;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "解析zip文件异常。");
            throw e;
        } finally {
            // 删除临时zip
            if (zipUrl != null) {
                if (!zipUrl.delete()) {
                    // file delete failed; take appropriate action
                }
            }
        }

    }

}
