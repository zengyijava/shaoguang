/**
 * Program  : MnpManageBiz.java
 * Author   : zousy
 * Create   : 2014-3-24 下午01:36:10
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.wymanage.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wy.AMnp;
import com.montnets.emp.table.wy.TableAMnp;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.wymanage.dao.MnpManageDAO;
import com.montnets.emp.wymanage.tools.JExcelTool;
import org.apache.commons.fileupload.FileItem;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author zousy <zousy999@qq.com>
 * @version 1.0.0
 * @2014-3-24 下午01:36:10
 */
public class MnpManageBiz extends SuperBiz  {
    private final MnpManageDAO mnpManageDao = new MnpManageDAO();
    private final ChangeCharset charsetUtil = new ChangeCharset();

    
    public List<AMnp> getMnpList(PageInfo pageInfo, LinkedHashMap<String, String> conditionMap) {
        List<AMnp> list = null;
        try {
            list = mnpManageDao.getMnpList(pageInfo, conditionMap);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "携号转网号码管理查询异常！");
            list = null;
        }
        return list;
    }

    
    @SuppressWarnings("unchecked")
    public void delete(String ids) throws Exception {
        LinkedHashMap<String, String> cond = new LinkedHashMap<String, String>();
        cond.put("id&in", ids);
        //所有删除对象
        List<AMnp> list = empDao.findListBySymbolsCondition(AMnp.class, cond, null);
        for (AMnp mnp : list) {
            mnp.setOptType(1L);
//			mnp.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        //获取数据库连接
        Connection conn = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(conn);
            empTransDao.delete(conn, AMnp.class, ids);
            empTransDao.save(conn, list, AMnp.class);
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "携号转网增量删除异常！");
            throw new Exception("携号转网增量删除异常！");
        } finally {
            empTransDao.closeConnection(conn);
        }
    }

    /**
     * 手工添加
     *
     * @param lists
     * @return
     * @throws Exception
     * @description
     * @author zousy <zousy999@qq.com>
     * @datetime 2014-3-25 下午02:14:21
     */
    
    @SuppressWarnings("unchecked")
    public int saveMnpList(List<AMnp> lists) throws Exception {

        //获取数据库连接
        Connection conn = empTransDao.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Set<String> phones = new HashSet<String>();
            ps = conn.prepareStatement("select distinct phone from A_MNP where OPTTYPE = ?");
            ps.setInt(1, 0);
            rs = ps.executeQuery();
            while (rs.next()) {
                phones.add(rs.getString("phone"));
            }
            Iterator<AMnp> its = lists.iterator();
            while (its.hasNext()) {
                if (!phones.add(its.next().getPhone())) {
                    its.remove();
                }
            }
            if (lists.size() <= 0) {
                return 0;
            }
            return empTransDao.save(conn, lists, AMnp.class);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "手工添加数据异常");
            return 0;
        } finally {
            closeConnection(ps, rs);
            empTransDao.closeConnection(conn);
        }

    }

    /**
     * 修改
     *
     * @param mnp
     * @return
     * @throws Exception
     */
    
    @SuppressWarnings("unchecked")
    public int updateMnp(AMnp mnp) throws Exception {

        //获取数据库连接
        Connection conn = empTransDao.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            AMnp obj = empDao.findObjectByID(AMnp.class, mnp.getId());
            if (obj == null) {
                return -1;
            }
            empTransDao.beginTransaction(conn);
            Set<String> phones = new HashSet<String>();
            ps = conn.prepareStatement("select distinct PHONE from A_MNP where OPTTYPE = ? and ID != ?");
            ps.setInt(1, 0);
            ps.setLong(2, obj.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                phones.add(rs.getString(TableAMnp.PHONE));
            }
            //出现重复
            if (!phones.add(mnp.getPhone())) {
                return 0;
            }
//			obj.setCreateTime(new Timestamp(System.currentTimeMillis()));
            empTransDao.delete(conn, AMnp.class, String.valueOf(mnp.getId()));
            obj.setOptType(1L);
            obj.setId(null);
            empTransDao.save(conn, obj);
            obj.setPhone(mnp.getPhone());
            obj.setPhoneType(mnp.getPhoneType());
            obj.setUnicom(mnp.getUnicom());
            obj.setOptType(0L);
            empTransDao.save(conn, obj);
            empTransDao.commitTransaction(conn);
            return 1;
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "携号转网修改异常！");
            throw new Exception("携号转网修改异常！");
        } finally {
            closeConnection(ps, rs);
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
    
    public Map<String, String> parseFile(FileItem fileItem, int fileCount) throws Exception {
        Map<String, String> phoneMap = new HashMap<String, String>();
        // 文件名
        String fileCurName = fileItem.getName();
        // 通过文件名截取到文件类型
        String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();
        // 读取压缩文件流
        if (fileType.equals(".zip")) {
            phoneMap.putAll(parseZip(fileItem, fileCount));
        }
        // 解析2003及WPS格式的的excel文件
        else if (fileType.equals(".xls") || fileType.equals(".et")) {
            phoneMap.putAll(JExcelTool.jxExcel(fileItem.getInputStream(), 2, 1));
        }
        // 解析excel2007文件
        else if (fileType.equals(".xlsx")) {
            phoneMap.putAll(JExcelTool.jxExcel(fileItem.getInputStream(), 2, 2));
        }
        // 解析TXT文本
        else {
            phoneMap.putAll(JExcelTool.jxTxt(fileItem.getInputStream(), fileItem.getInputStream()));
        }
        return phoneMap;
    }

    /**
     * 解析zip文件，返回文件流
     *
     * @param fileItem
     * @param fileCount
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> parseZip(FileItem fileItem, int fileCount) throws Exception {
        Map<String, String> phoneMap = new HashMap<String, String>();
        File zipUrl = null;
        //获取一个存放临时文件的目录
        String uploadPath = StaticValue.FILEDIRNAME;
        String url = new TxtFileUtil().getWebRoot() + uploadPath + "/" + System.currentTimeMillis() + ".zip";
        try {
            // 创建临时文件，获取zip地址
            String zipFileStr = url.replace(".txt", ".zip");
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
                entry = (ZipEntry) zipEnum.nextElement();
                // 处理txt文件
                if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".txt")) {
                    phoneMap.putAll(JExcelTool.jxTxt(zipFile.getInputStream(entry),zipFile.getInputStream(entry)));
                } else if (!entry.isDirectory() && (entry.getName().toLowerCase().endsWith(".xls") || entry.getName().toLowerCase().endsWith(".et"))) {
                    phoneMap.putAll(JExcelTool.jxExcel(zipFile.getInputStream(entry), 2, 1));

                }
                // 如果是.xlsx文件
                else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xlsx")) {
                    phoneMap.putAll(JExcelTool.jxExcel(zipFile.getInputStream(entry), 2, 2));
                }
            }
            return phoneMap;
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

    
    public Long getUnicom(String str) {
        Pattern p = Pattern.compile("^(00|01|21)$");
        if (p.matcher(str).matches()) {
            return Long.valueOf(str);
        } else {
            return 255L;
        }
    }

    public String getOpStr(List<AMnp> aMnps) {
        StringBuffer sb = new StringBuffer();
        if (aMnps != null && aMnps.size() > 0) {
            for (int i = 0; i < aMnps.size(); i++) {
                AMnp amnp = aMnps.get(i);
                sb.append("id：").append(amnp.getId().toString()).append("， 号码类型：").append(amnp.getUnicom().toString()).append(amnp.getPhoneType().toString()).append("，手机号码：").append(amnp.getPhone()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public void closeConnection(PreparedStatement ps, ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
    }
}

