/**
 * Program  : par_indexCfgSvt.java
 * Author   : zousy
 * Create   : 2013-11-20 下午03:45:22
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.specfg.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.system.LfSpeUICfg;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Administrator <510061684@qq.com>
 * @version 1.0.0
 * @2013-11-20 下午03:45:22
 */
public class par_indexCfgSvt extends BaseServlet {
    private final String empRoot = "xtgl";
    private final String basePath = "/speuicfg";

    private final BaseBiz baseBiz = new BaseBiz();

    /**
     * 进入页面查询
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String corpCode = request.getParameter("lgcorpcode");
        try {
            LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
            condMap.put("corpCode", corpCode);
            List<LfSpeUICfg> cfgs = baseBiz.getByCondition(LfSpeUICfg.class, condMap, null);
            LfSpeUICfg cfg = null;
            if (cfgs != null && cfgs.size() > 0) {
                cfg = cfgs.get(0);
                cfg.setBgImg(downloadFile(cfg.getBgImg()));
                cfg.setCompanyLogo(downloadFile(cfg.getCompanyLogo()));
                cfg.setLoginLogo(downloadFile(cfg.getLoginLogo()));
            }
            request.setAttribute("cfg", cfg);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "个性化界面设置查找异常！");
        } finally {
            request.getRequestDispatcher(empRoot + basePath + "/par_indexConfig.jsp").forward(request, response);
        }
    }

    /*
     * 上传单图片
     */
    public void uploadImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 图片存放的根目录
        String baseFileDir = getServletContext().getRealPath("/file/cfg");
        // 上传文件最大大小
        long maxSize = 280L * 1024L;
        response.setContentType("text/html");
        // 构造出文件工厂，用于存放JSP页面中传递过来的文件
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置缓存大小，如果文件大于缓存大小时，则先把文件放到缓存中
        factory.setSizeThreshold(1024 * 1024);
        // 设置上传文件的保存路径
        File baseFile = new File(baseFileDir);
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
        factory.setRepository(baseFile);
        // 产生Servlet上传对象
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置可以上传文件大小的上界4MB
        upload.setSizeMax(4 * 1024L * 1024L);
        // 取得所有上传文件的信息
        List<FileItem> fileList = null;
        // 上传图片保存url路径
        String imgUrl = "";
        try {
            fileList = upload.parseRequest(request);
        } catch (FileUploadException e) {
            EmpExecutionContext.error(e, "个性化界面设置解析上传对象异常！");
            response.getWriter().print("overSize");
            return;
        }
        Iterator<FileItem> iter = fileList.iterator();
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            String fieldName = item.getFieldName();
            if (!item.isFormField() && item.getName().length() > 0) {
                // 文件名
                String fileName = item.getName();
                // 获得文件大小
                long fileSize = item.getSize();
                // 判断文件大小是否超过限制
                if (fileSize > maxSize) {
                    item.delete();
                    item = null;
                    response.getWriter().print("overSize");
                    return;
                }

                // 文件类型
                String allImgExt = ".jpg|.jpeg|.png|";
                String extName = fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();

                // 如果是图片文件
                if (allImgExt.indexOf(extName + "|") != -1) {
                    String dirpath = baseFileDir + File.separator;
                    Calendar cal = Calendar.getInstance();
                    // 年
                    int year = cal.get(Calendar.YEAR);
                    // 月
                    int month = cal.get(Calendar.MONTH) + 1;
                    // 日
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    String filepath = dirpath + File.separator + year + File.separator + month + File.separator + day;
                    File uf = new File(filepath);
                    // 更改文件的保存路径，以防止文件重名的现象出现
                    if (!uf.exists()) {
                        uf.mkdirs();
                    }
                    try {
                        String strid = UUID.randomUUID().toString();
                        String newFileName = strid.replaceAll("-", "") + StaticValue.getServerNumber() + extName;
                        File uploadedFile = new File(filepath, newFileName);
                        if (uploadedFile.exists()) {
                            if (!uploadedFile.delete()) {
                                //....
                            }
                        }
                        item.write(uploadedFile);

                        imgUrl = "file/cfg/" + year + "/" + month + "/" + day + "/" + newFileName;
                        fieldMap.put(fieldName, imgUrl);
                    } catch (Exception e) {
                        EmpExecutionContext.error(e, "个性化界面设置上传图像失败！");
                        return;
                    } finally {
                        //使用集群，将文件上传到文件服务器
                        if (StaticValue.getISCLUSTER() == 1) {
                            //上传文件到文件服务器
                            CommonBiz comBiz = new CommonBiz();
                            boolean upFileRes = comBiz.upFileToFileServer(imgUrl);
                            if (!upFileRes) {
                                EmpExecutionContext.error("个性化界面设置上传文件到服务器失败。错误码：" + IErrorCode.B20023);
                            }
                        }
                    }
                }
            } else {
                maxSize = Long.valueOf(item.getString("UTF-8").toString()) * 1024;
            }
        }
        response.getWriter().print(imgUrl);
    }

    /**
     * 更新个性化界面设置
     * @description
     * @param request
     * @param response
     * @author zousy <zousy999@qq.com>
     * @throws IOException
     * @datetime 2013-11-20 下午03:48:07
     */
    public void updateCfg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String def = request.getParameter("default");
        String loginLogo = request.getParameter("login_logo");
        String companyLogo = request.getParameter("company_logo");
        String back = request.getParameter("back");
        String company = request.getParameter("company");
        String param = request.getParameter("param");
        String corpCode = request.getParameter("lgcorpcode");
        String displayType = "0";
        response.setContentType("text/html");
        if (def != null && !"".equals(def.trim())) {
//			恢复默认修改为部分恢复默认
//			displayType="1";
            if ("0".equals(def)) {
                loginLogo = " ";
                back = " ";
                param = "11111000";
            } else {
                companyLogo = " ";
            }
        }

        String msg = "1";
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corpCode);
            List<LfSpeUICfg> cfgs = baseBiz.getByCondition(LfSpeUICfg.class, conditionMap, null);
            LfSpeUICfg cfg = null;
            if (cfgs.size() > 0) {
                cfg = cfgs.get(0);
                setObjMap(cfg, objectMap, loginLogo, companyLogo, back, company, param, displayType);
                baseBiz.update(LfSpeUICfg.class, objectMap, conditionMap);
            } else {
                cfg = new LfSpeUICfg();
                cfg.setCorpCode(corpCode);
                setValues(cfg, loginLogo, companyLogo, back, company, param, displayType);
                baseBiz.addObj(cfg);
            }
	/*2014-05-13 取消多企业的登录页个性化设置
	 * 		//不为单企业则使用cookie
			if(StaticValue.CORPTYPE!=0){
				//更新成功后 将更新 设置同步至cookie
				setCookies(response,loginLogo,back,param,displayType);
			}
	*/

            //增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent1 = "更新个性化设置成功。[登录界面LOGO图片，内页面LOGO图片，大背景图片]" +
                        "(" + loginLogo + "，" + companyLogo + "，" + back + ")";
                EmpExecutionContext.info("个性化界面设置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                        loginSysuser.getUserName(), opContent1, "UPDATE");
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "更新个性化设置失败！");
            msg = "0";
        } finally {
            response.getWriter().print(msg);
        }
    }

    public void setValues(LfSpeUICfg cfg, String loginLogo, String companyLogo, String back, String company, String param, String displayType) {
        if (loginLogo != null && !"".equals(loginLogo)) {
            cfg.setLoginLogo(loginLogo);
        }
        if (companyLogo != null && !"".equals(companyLogo)) {
            cfg.setCompanyLogo(companyLogo);
        }
        if (back != null && !"".equals(back)) {
            cfg.setBgImg(back);
        }
        if (company != null && !"".equals(company)) {
            cfg.setCompanyName(company.trim());
        }
        if (param != null && !"".equals(param)) {
            cfg.setDispContent(param);
        }
        if (cfg.getDispContent() == null || "".equals(cfg.getDispContent())) {
            cfg.setDispContent("11100000");
        }
        cfg.setDisplayType(Integer.valueOf(displayType));
    }

    public void setObjMap(LfSpeUICfg cfg, LinkedHashMap<String, String> objectMap, String loginLogo, String companyLogo, String back, String company, String param, String displayType) {
        if (loginLogo != null && !"".equals(loginLogo)) {
            objectMap.put("loginLogo", loginLogo);
        }
        if (companyLogo != null && !"".equals(companyLogo)) {
            objectMap.put("companyLogo", companyLogo);
        }
        if (back != null && !"".equals(back)) {
            objectMap.put("bgImg", back);
        }
        if (company != null && !"".equals(company)) {
            objectMap.put("companyName", company.trim());
        }
        if (param != null && !"".equals(param)) {
            objectMap.put("dispContent", param);
        }
        if (cfg.getDispContent() == null || "".equals(cfg.getDispContent())) {
            objectMap.put("dispContent", "11100000");
        }
        objectMap.put("displayType", displayType);
    }

    /**
     * 设置cookie
     * @description
     * @param response
     * @param loginLogo
     * @param back
     * @param param
     * @author zousy <zousy999@qq.com>
     * @datetime 2013-11-22 上午10:25:34
     */
    public void setCookies(HttpServletResponse response, String loginLogo, String back, String param, String displayType) {
        int maxAge = 7 * 24 * 3600;
        if (loginLogo != null && !"".equals(loginLogo)) {
            addCookie(response, "loginLogo", loginLogo, maxAge);
        }
        if (back != null && !"".equals(back)) {
            addCookie(response, "bgImg", back, maxAge);
        }
        if (param != null && !"".equals(param)) {
            addCookie(response, "dispContent", param, maxAge);
        }
        addCookie(response, "displayType", displayType, maxAge);
    }


    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    public String downloadFile(String url) {
        if (url != null && !"".equals(url.trim())) {
            String servletPath = new TxtFileUtil().getWebRoot();
            File f = new File(servletPath + url);
            //文件本地不存在 且为集群时 则 从文件服务器上下载文件
            if (!f.exists() && StaticValue.getISCLUSTER() == 1) {
                CommonBiz comBiz = new CommonBiz();
                String downMsg = comBiz.downloadFileFromFileCenter(url);
                if ("error".equals(downMsg)) {
                    EmpExecutionContext.error("从文件服务器下载文件" + url + "失败！");
                }
            }
            if (!f.exists()) {
                url = "";
            }
        } else {
            url = "";
        }
        return url;
    }
}

