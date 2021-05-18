package com.montnets.emp.wxgl.svt;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.lbs.LfLbsPios;
import com.montnets.emp.entity.lbs.LfLbsPushset;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.param.BaiduMapParams;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxgl.biz.LbsPiosBiz;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

//调用znly模块中，ottbase包中的BaseBiz
//import com.montnets.emp.ottbase.biz.BaseBiz;
//调用公共的BaseBiz

/**
 * @author yejiangmin <282905282@qq.com>
 * @description LBS管理
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-28 下午03:10:27
 */
public class weix_lbsManagerSvt extends BaseServlet {
    /**
     * @description
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-28 下午03:09:37
     */
    private static final long serialVersionUID = -1597912459480572885L;

    private final String LBS_PATH = "/wxgl/lbs";

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description 进入LBS查询页面
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-28 下午03:10:39
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 分页信息
        PageInfo pageInfo = new PageInfo();

        //添加与日志相关
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        long startTimeByLog = System.currentTimeMillis();  //开始时间

        try {
            boolean isFirstEnter = pageSet(pageInfo, request);
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            if (lgcorpcode == null || "".equals(lgcorpcode)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.find.lgcorpcode:" + lgcorpcode);
                return;
            }

            BaseBiz baseBiz = new BaseBiz();
            // 存放查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            List<LfWeiAccount> accountList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);

            LinkedHashMap<Long, LfWeiAccount> accountMap = new LinkedHashMap<Long, LfWeiAccount>();
            if (accountList != null && accountList.size() > 0) {
                LfWeiAccount account = null;
                for (int i = 0; i < accountList.size(); i++) {
                    account = accountList.get(i);
                    accountMap.put(account.getAId(), account);
                }
            } else {
                accountMap = null;
            }
            // 公众帐号集合
            request.setAttribute("accountList", accountList);
            // AID对应的 公众帐号】
            request.setAttribute("accountMap", accountMap);
            conditionMap.clear();
            conditionMap.put("corpCode", lgcorpcode);
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("pid", WXStaticValue.DESC);
            if (!isFirstEnter) {
                String keyword = request.getParameter("keyword");
                if (keyword != null && !"".equals(keyword)) {
                    conditionMap.put("keyword&like", keyword);
                }
                String accountid = request.getParameter("pubAccounts");
                if (accountid != null && !"".equals(accountid)) {
                    conditionMap.put("AId", accountid);
                }
            }
            List<LfLbsPios> lbsPiosList = new ArrayList<LfLbsPios>();
            // 地址位置采集   分页查询首页查总数，后续翻页不查总数
            lbsPiosList = baseBiz.getByConditionNoCount(LfLbsPios.class, null, conditionMap, orderbyMap, pageInfo);
            request.setAttribute("lbsPiosList", lbsPiosList);
            request.setAttribute("conditionMap", conditionMap);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.find Is Error");
        } finally {

            //添加与日志相关
            long endTimeByLog = System.currentTimeMillis();  //查询结束时间
            long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时

            //增加操作日志
            Object loginSysuserObj = null;
            try {
                loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            } catch (Exception e) {
                EmpExecutionContext.error(e, "session为null");
            }
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String opContent = "查询开始时间：" + sdf.format(startTimeByLog) + "，耗时：" + consumeTimeByLog + "ms" + "，查询总数：" + pageInfo.getTotalRec();

                EmpExecutionContext.info("地理位置采集", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(),
                        loginSysuser.getUserName(), opContent, "GET");
            }

            request.setAttribute("pageInfo", pageInfo);
            request.getRequestDispatcher(LBS_PATH + "/weix_lbsManager.jsp").forward(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description 跳转新增/修改LBS页面
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-28 下午04:31:52
     */
    public void toAddUpdateLbs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String skiptype = "";
        try {
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 跳转方式 add / update
            skiptype = request.getParameter("skiptype");
            if (lgcorpcode == null || "".equals(lgcorpcode) || skiptype == null || "".equals(skiptype)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.toAddUpdateLbs.lgcorpcode:" + lgcorpcode + ",skiptype:" + skiptype);
                return;
            }
            // LBS采集自增ID
            String pid = "";
            // LBS采集
            LfLbsPios lbsPios = null;
            BaseBiz baseBiz = new BaseBiz();
            if ("update".equals(skiptype)) {
                pid = request.getParameter("pid");
                if (pid == null || "".equals(pid)) {
                    EmpExecutionContext.error("weix_lbsManagerSvt.toAddUpdateLbs.pid:" + pid);
                    return;
                }
                lbsPios = baseBiz.getById(LfLbsPios.class, pid);
                if (lbsPios == null) {
                    EmpExecutionContext.error("weix_lbsManagerSvt.toAddUpdateLbs.lbsPios Is NULL");
                    return;
                }
                request.setAttribute("lbsPios", lbsPios);
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", lgcorpcode);
            List<LfWeiAccount> weixAccountList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);
            request.setAttribute("weixAccountList", weixAccountList);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.toAddLbs出现异常");
        } finally {
            if (skiptype == null || "".equals(skiptype)) {
                skiptype = "add";
                EmpExecutionContext.error("weix_lbsManagerSvt.toAddLbs.skiptype is null");
            }
            request.setAttribute("skiptype", skiptype);
            request.getRequestDispatcher(LBS_PATH + "/weix_addupdateLbs.jsp").forward(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @description 新增/编辑 地理位置采集 add / update
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-28 下午07:29:02
     */
    public void addupdateLbs(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // 公众帐号
            String account = request.getParameter("account");
            // 标题
            String title = request.getParameter("title");
            // 关键字
            String keyword = request.getParameter("keyword");
            // 描述
            String note = request.getParameter("note");
            // 地址
            String address = request.getParameter("address");
            // 电话
            String telephone = request.getParameter("telephone");
            // 纬度
            String lat = request.getParameter("lat");
            // 经度
            String lng = request.getParameter("lng");
            // 企业编码
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 跳转方式 新增 / 修改
            String skiptype = request.getParameter("skiptype");
            if (account == null || "".equals(account) || title == null || "".equals(title) || keyword == null || "".equals(keyword) || address == null || "".equals(address) || telephone == null || "".equals(telephone) || lat == null || "".equals(lat) || lng == null || "".equals(lng) || lgcorpcode == null || "".equals(lgcorpcode) || skiptype == null || "".equals(skiptype)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.addupdateLbs.account:" + account + ",title:" + title + ",keyword:" + keyword + ",note:" + note + ",address:" + address + ",telephone:" + telephone + ",lat:" + lat + ",lng:" + lng + ",lgcorpcode:" + lgcorpcode + ",skiptype:" + skiptype);
                response.getWriter().print("paramsIsNull");
                return;
            }
            LfLbsPios lbsPios = null;
            BaseBiz baseBiz = new BaseBiz();
            if ("update".equals(skiptype)) {
                String pid = request.getParameter("pid");
                if (pid == null || "".equals(pid)) {
                    EmpExecutionContext.error("weix_lbsManagerSvt.addupdateLbs.pid:" + pid);
                    response.getWriter().print("paramsIsNull");
                    return;
                }
                lbsPios = baseBiz.getById(LfLbsPios.class, pid);
                if (lbsPios == null) {
                    EmpExecutionContext.error("weix_lbsManagerSvt.addupdateLbs.LfLbsPios Is NULL");
                    response.getWriter().print("paramsIsNull");
                    return;
                }
                lbsPios.setModifytime(new Timestamp(System.currentTimeMillis()));
            } else if ("add".equals(skiptype)) {
                lbsPios = new LfLbsPios();
                lbsPios.setCorpCode(lgcorpcode);
                lbsPios.setCreatetime(new Timestamp(System.currentTimeMillis()));
            } else {
                EmpExecutionContext.error("weix_lbsManagerSvt.addupdateLbs.skiptype:" + skiptype);
                response.getWriter().print("paramsIsNull");
                return;
            }
            lbsPios.setAddress(address);
            lbsPios.setTitle(title);
            lbsPios.setKeyword(keyword);
            lbsPios.setLat(lat);
            lbsPios.setLng(lng);
            lbsPios.setNote(note);
            lbsPios.setTelephone(telephone);
            lbsPios.setAId(account);
            lbsPios.setCityid(1L);
            boolean isFlag = false;
            if ("update".equals(skiptype)) {
                isFlag = baseBiz.updateObj(lbsPios);
            } else {
                isFlag = baseBiz.addObj(lbsPios);
            }
            if (isFlag) {
                response.getWriter().print("success");
            } else {
                response.getWriter().print("fail");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.addupdateLbs Is Error");
            response.getWriter().print("error");
            return;
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @description 通过 接口定位 城市 以及详细地址 查询符合条件的信息 ， 调用百度api
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-11 上午10:25:08
     */
    public void searchLocation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // 城市
            String city = request.getParameter("city");
            // 详细地址
            String location = request.getParameter("location");
            if (city == null || "".equals(city) || location == null || "".equals(location)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.searchLocation.city:" + city + ",location:" + location);
                response.getWriter().print("paramsIsNull");
                return;
            }
            BaiduMapParams mapParams = new BaiduMapParams();
            mapParams.setCity(city);
            mapParams.setLocation(location);
            mapParams = new LbsPiosBiz().searchPalce(mapParams);
            if ("000".equals(mapParams.getErrCode()) && "success".equals(mapParams.getErrMsg())) {
                if (mapParams.getJsonObj() == null || "".equals(mapParams.getJsonObj().toJSONString())) {
                    EmpExecutionContext.error("weix_lbsManagerSvt.searchLocation.mapParams.getJsonObj() Is Null");
                    response.getWriter().print("jsonIsNull");
                    return;
                }
                JSONObject json = (JSONObject) mapParams.getJsonObj();
                response.getWriter().print(json.toString());
                return;
            } else {
                EmpExecutionContext.error("weix_lbsManagerSvt.searchLocation.errcode：" + mapParams.getErrCode());
                response.getWriter().print("fail");
                return;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.searchLocation Is Error");
            response.getWriter().print("error");
            return;
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @description 删除LBS采集点
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-29 下午06:39:58
     */
    public void delLbs(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // 采集点ID
            String pid = request.getParameter("pid");
            if (pid == null || "".equals(pid)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.delLbs.pid:" + pid);
                response.getWriter().print("paramsIsNull");
                return;
            }
            int delCount = new BaseBiz().deleteByIds(LfLbsPios.class, pid);
            if (delCount > 0) {
                response.getWriter().print("success");
                return;
            } else {
                response.getWriter().print("fail");
                return;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.delLbs Is Error");
            response.getWriter().print("error");
            return;
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @description 跳转进入设置模式页面
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-10 上午10:30:10
     */
    public void toPushSetLayout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            if (lgcorpcode == null || "".equals(lgcorpcode)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.toPushSetLayout.lgcorpcode:" + lgcorpcode);
                return;
            }
            // 存放查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            // 查询该企业的默认推送设置
            conditionMap.put("corpCode", lgcorpcode);
            List<LfLbsPushset> pushSetList = new BaseBiz().getByCondition(LfLbsPushset.class, conditionMap, null);
            LfLbsPushset pushsetObj = null;
            if (pushSetList != null && pushSetList.size() > 0) {
                pushsetObj = pushSetList.get(0);
            }
            request.setAttribute("pushsetObj", pushsetObj);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.toPushSetLayout Is Error");
        } finally {
            request.getRequestDispatcher(LBS_PATH + "/weix_lbsPushset.jsp").forward(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @description 处理多图文模式设置
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-9 下午04:10:01
     */
    public void pushSetGraphics(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // 半径
            String radius = request.getParameter("radius");
            // 是否开启半径扩大
            String autoradius = request.getParameter("autoradius");
            // 推送条数
            String pushcount = request.getParameter("pushcount");
            // 是否启用更多
            String automore = request.getParameter("automore");
            // 企业编码
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 推送设置企业对应的ID
            String pushid = request.getParameter("pushid");
            if (radius == null || "".equals(radius) || autoradius == null || "".equals(autoradius) || pushcount == null || "".equals(pushcount) || automore == null || "".equals(automore) || lgcorpcode == null || "".equals(lgcorpcode) || pushid == null || "".equals(pushid)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.pushSetGraphics.radius:" + radius + ",autoradius:" + autoradius + ",pushcount:" + pushcount + ",automore:" + automore + ",lgcorpcode:" + lgcorpcode + ",pushid:" + pushid);
                response.getWriter().print("paramsIsNull");
                return;
            }
            BaseBiz baseBiz = new BaseBiz();
            LfLbsPushset lbsPushset = baseBiz.getById(LfLbsPushset.class, pushid);
            if (lbsPushset == null) {
                response.getWriter().print("objectIsNull");
                return;
            }
            // 推送模式 1多图文 2页面交互
            lbsPushset.setPushtype(1);
            lbsPushset.setRadius(radius);
            lbsPushset.setAutoradius(Integer.valueOf(autoradius));
            lbsPushset.setPushcount(Integer.valueOf(pushcount));
            lbsPushset.setAutomore(Integer.valueOf(automore));
            lbsPushset.setImgurl("");
            lbsPushset.setNote("");
            lbsPushset.setModitytime(new Timestamp(System.currentTimeMillis()));
            if (baseBiz.updateObj(lbsPushset)) {
                response.getWriter().print("success");
            } else {
                response.getWriter().print("fail");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.pushSetGraphics Is Error");
            response.getWriter().print("error");
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @description 设置页面交互处理
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-9 下午04:43:55
     */
    @SuppressWarnings("unchecked")
    public void pushSetPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String returnmsg = "";
        try {
            String imgformat = request.getParameter("imgformat");
            // 推送设置企业对应的ID
            String pushid = request.getParameter("pushid");
            // 0默认状态 1是不需要上传图片,2是需要上传图片
            String uploadFlag = request.getParameter("uploadFlag");
            if (imgformat == null || "".equals(imgformat) || pushid == null || "".equals(pushid) || uploadFlag == null || "".equals(uploadFlag)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.pushSetPage.imgformat:" + imgformat + ",pushid:" + pushid + ",uploadFlag:" + uploadFlag);
                returnmsg = "paramsIsNull";
                return;
            }
            BaseBiz baseBiz = new BaseBiz();
            LfLbsPushset lbsPushset = baseBiz.getById(LfLbsPushset.class, pushid);
            if (lbsPushset == null) {
                returnmsg = "ObjectIsNull";
                return;
            }
            WeixBiz weixBiz = new WeixBiz();
            String[] path = weixBiz.getWeixResourceUrl(imgformat);
            if (path == null || path.length == 0) {
                EmpExecutionContext.error("weix_lbsManagerSvt.pushSetPage.path[] Is Null");
                returnmsg = "paramsIsNull";
                return;
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(128 * 1024);
            // 去掉文件名称的文件绝对路径
            factory.setRepository(new File(path[2]));
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();
            String fileName = "";
            String fileValue = "";
            Map<String, String> fieldInfo = new HashMap<String, String>();
            while (iter.hasNext()) {
                FileItem fileItem = iter.next();
                if (!fileItem.isFormField() && fileItem.getName().length() > 0 && ("filename").equals(fileItem.getFieldName()) && "2".equals(uploadFlag)) {
                    // 图片大小为128KB 判断其大小
                    if (!weixBiz.compareWeixResource(fileItem.getSize(), "image")) {
                        returnmsg = "oversize";
                        break;
                    }
                    // 将文件写到本地服务器上 文件的绝对路径
                    fileItem.write(new File(path[0]));
                    // 判断是否使用集群 上传文件到文件服务器 文件的相对路径
                    if (WXStaticValue.ISCLUSTER == 1 && !"success".equals(new CommonBiz().uploadFileToFileCenter(path[1]))) {
                        // 是集群并且上传到文件服务器失败，则提示上传失败
                        EmpExecutionContext.error("weix_lbsManagerSvt.pushSetPage.uploadFile Is Fail");
                        returnmsg = "uploadfail";
                    }
                } else {
                    fileName = fileItem.getFieldName();
                    fileValue = fileItem.getString("UTF-8").toString();
                    fieldInfo.put(fileName, fileValue);
                }
            }
            if (!"".equals(returnmsg)) {
                return;
            }

            lbsPushset.setPushtype(2);
            // 如果 uploadFlag ==2 ，则上传上来一张图片
            if ("2".equals(uploadFlag)) {
                lbsPushset.setImgurl(path[1]);
            }
            String note = fieldInfo.get("note");
            if (note != null) {
                lbsPushset.setNote(note);
            } else {
                lbsPushset.setNote("");
            }
            fieldInfo.clear();
            // 将设置为默认值
            lbsPushset.setRadius("");
            lbsPushset.setAutoradius(0);
            lbsPushset.setPushcount(0);
            lbsPushset.setAutomore(0);
            lbsPushset.setModitytime(new Timestamp(System.currentTimeMillis()));
            if (baseBiz.updateObj(lbsPushset)) {
                returnmsg = "success";
            } else {
                returnmsg = "fail";
            }
            request.setAttribute("pushsetObj", lbsPushset);
        } catch (Exception e) {
            returnmsg = "error";
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.pushSetPage Is Error");
        } finally {
            request.setAttribute("pushsetResult", returnmsg);
            request.getRequestDispatcher(LBS_PATH + "/weix_lbsUpload.jsp").forward(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @description 图文下发 的链接地址
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-12 上午10:45:42
     */
    public void toMultiFixedMap(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // 采集点位置
            String pioslng = request.getParameter("pioslng");
            String pioslat = request.getParameter("pioslat");
            // 用户位置
            String userlng = request.getParameter("userlng");
            String userlat = request.getParameter("userlat");
            // 采集点PID
            String pid = request.getParameter("pid");
            if (pioslng == null || "".equals(pioslng) || pioslat == null || "".equals(pioslat) || userlng == null || "".equals(userlng) || userlat == null || "".equals(userlat) || pid == null || "".equals(pid)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.toMultiFixedMap.pioslng:" + pioslng + ",pioslat:" + pioslat
                        + ",userlng:" + userlng + ",userlat:" + userlat + ",pid:" + pid);
                return;
            }
            LfLbsPios oblbsPios = new BaseBiz().getById(LfLbsPios.class, pid);
            if (oblbsPios == null) {
                EmpExecutionContext.error("weix_lbsManagerSvt.toMultiFixedMap.LfLbsPios Is null");
                return;
            }
            request.setAttribute("oblbsPios", oblbsPios);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.toMultiFixedMap Is Error");
        } finally {
            request.getRequestDispatcher(LBS_PATH + "/weix_lbsMultiMap.jsp").forward(request, response);
        }
    }


    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @description 图文下发 的链接地址
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-12 上午10:45:42
     */
    public void toSingleFixedMap(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // 用户位置  经度 
            String userlng = request.getParameter("userlng");
            //纬度
            String userlat = request.getParameter("userlat");
            //公众帐号ID
            String aid = request.getParameter("aid");
            //公众帐号
            String corpcode = request.getParameter("corpcode");
            if (userlng == null || "".equals(userlng) || userlat == null || "".equals(userlat)
                    || aid == null || "".equals(aid) || corpcode == null || "".equals(corpcode)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.toSingleFixedMap.userlng:" + userlng + ",userlat:"
                        + userlat + ",aid:" + aid + ",corpcode:" + corpcode);
                return;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.toSingleFixedMap Is Error");
        } finally {
            request.getRequestDispatcher(LBS_PATH + "/weix_lbsSingleMap.jsp").forward(request, response);
        }
    }


    /**
     * @param request
     * @param response
     * @throws IOException
     * @description 单图文页面查询
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-16 上午09:22:50
     */
    @SuppressWarnings("unchecked")
    public void searchSingleFixed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //地址
            String address = request.getParameter("address");
            //企业编码
            String corpcode = request.getParameter("corpcode");
            //公众帐号 ID
            String aid = request.getParameter("aid");
            //纬度
            String lat = request.getParameter("lat");
            //经度 
            String lng = request.getParameter("lng");
            if (address == null || "".equals(address) || lng == null || "".equals(lng)
                    || aid == null || "".equals(aid) || lat == null || "".equals(lat)
                    || corpcode == null || "".equals(corpcode)) {
                response.getWriter().print("paramsIsNull");
                EmpExecutionContext.error("weix_lbsManagerSvt.searchSingleFixed.address:" + address
                        + ",lat:" + lat + ",lng:" + lng + ",corpcode:" + corpcode);
                return;
            }
            address = address.replaceAll("'", "");
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corpcode);
            conditionMap.put("AId", aid);
            conditionMap.put("address&like", address);
            List<LfLbsPios> lbspiosList = new BaseBiz().getByCondition(LfLbsPios.class, conditionMap, null);
            if (lbspiosList == null || lbspiosList.size() == 0) {
                response.getWriter().print("norecord");
                EmpExecutionContext.error("weix_lbsManagerSvt.searchSingleFixed.List<LfLbsPios> Is null");
                return;
            }
            List<LfLbsPios> piosList = new LbsPiosBiz().getDistanceMsg(lbspiosList, Double.valueOf(lat), Double.valueOf(lng), true);
            if (piosList == null || piosList.size() == 0) {
                response.getWriter().print("norecord");
                EmpExecutionContext.error("weix_lbsManagerSvt.searchSingleFixed.List<LfLbsPios> piosList Is null");
                return;
            }
            LfLbsPios lbspios = null;
            JSONObject obj = null;
            JSONArray members = new JSONArray();
            for (int i = 0; i < piosList.size(); i++) {
                lbspios = piosList.get(i);
                obj = new JSONObject();
                obj.put("lat", lbspios.getLat());
                obj.put("lng", lbspios.getLng());
                obj.put("distance", lbspios.getDistance());
                obj.put("telephone", lbspios.getTelephone());
                obj.put("address", lbspios.getAddress());
                obj.put("title", lbspios.getTitle());
                members.add(obj);
                obj = null;
            }
            obj = new JSONObject();
            obj.put("total", piosList.size());
            obj.put("members", members);
            response.getWriter().print(obj.toString());
        } catch (Exception e) {
            response.getWriter().print("error");
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.searchSingleFixed Is Error");
        }
    }

    @SuppressWarnings("unchecked")
    public void changeImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        String message = "";
        String imgurl = "";

        // 图片存放的根目录
        String baseFileDir = getServletContext().getRealPath(WXStaticValue.WEIX_RIMG);
        long maxSize = 256L * 1024L;
        try {
            // 构造出文件工厂，用于存放JSP页面中传递过来的文件
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置缓存大小，如果文件大于缓存大小时，则先把文件放到缓存中
            factory.setSizeThreshold(1024 * 1024);
            // 设置上传文件的保存路径
            factory.setRepository(new File(baseFileDir));
            // 产生Servlet上传对象
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置可以上传文件大小的上界
            upload.setSizeMax(maxSize);
            // 取得所有上传文件的信息
            List<FileItem> fileList = upload.parseRequest(request);
            Iterator<FileItem> iter = fileList.iterator();

            while (iter.hasNext()) {
                FileItem item = iter.next();
                // 获得此表单元素的name属性
                String fieldName = item.getFieldName();
                if (!item.isFormField()) {
                    // 文件名
                    String fileName = item.getName();
                    // 文件类型
                    String allImgExt = ".jpg|.jpeg|.gif|.bmp|.png|";
                    String extName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                    if (allImgExt.indexOf(extName + "|") != -1) {
                        String dirpath = baseFileDir + File.separator;

                        Calendar cal = Calendar.getInstance();
                        // 年
                        int year = cal.get(Calendar.YEAR);
                        // 月
                        int month = cal.get(Calendar.MONTH) + 1;
                        // 日
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        String filepath = dirpath + year + File.separator + month + File.separator + day;

                        File uf = new File(filepath);
                        // 更改文件的保存路径，以防止文件重名的现象出现
                        if (!uf.exists()) {
                            uf.mkdirs();
                        }

                        try {
                            String strid = UUID.randomUUID().toString().replaceAll("-", "");
                            String shortstrid = strid.substring(0, 10);
                            String newFileName = shortstrid + WXStaticValue.SERVER_NUMBER + extName;
                            File uploadedFile = new File(filepath, newFileName);
                            if (uploadedFile.exists()) {
                                if (!uploadedFile.delete()) {
                                    throw new FileUploadException();
                                }
                            }
                            item.write(uploadedFile);

                            imgurl = WXStaticValue.WEIX_RIMG + year + "/" + month + "/" + day + "/" + newFileName;
                            message = "imgurl:" + imgurl + "," + "fieldName:" + fieldName;
                        } catch (FileUploadException e) {
                            message = "error";
                            EmpExecutionContext.error(e, "回复管理-图片上传失败！");
                        } finally {
                            //使用集群，将文件上传到文件服务器
                            if (WXStaticValue.ISCLUSTER == 1) {
                                //上传文件到文件服务器
                                CommonBiz comBiz = new CommonBiz();
                                boolean upFileRes = comBiz.upFileToFileServer(imgurl);
                                if (!upFileRes) {
                                    EmpExecutionContext.error("上传文件到服务器失败。错误码：" + IErrorCode.B20023);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.pushSetGraphics Is Error");
            response.getWriter().print("@" + "errror");
        } finally {
            // 异步返回结果
            response.getWriter().print("@" + message);
        }
    }


    public void pushSetPage2(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String returnmsg = "";
        try {
            String picUrl = request.getParameter("picurl");

            String imgformat = request.getParameter("imgformat");
            // 推送设置企业对应的ID
            String pushid = request.getParameter("pushid");
            // 0默认状态 1是不需要上传图片,2是需要上传图片
            String uploadFlag = request.getParameter("uploadFlag");

            if (imgformat == null || "".equals(imgformat) || pushid == null || "".equals(pushid) || uploadFlag == null || "".equals(uploadFlag)) {
                EmpExecutionContext.error("weix_lbsManagerSvt.pushSetPage.imgformat:" + imgformat + ",pushid:" + pushid + ",uploadFlag:" + uploadFlag);
                returnmsg = "paramsIsNull";
                return;
            }
            BaseBiz baseBiz = new BaseBiz();
            LfLbsPushset lbsPushset = baseBiz.getById(LfLbsPushset.class, pushid);
            if (lbsPushset == null) {
                returnmsg = "ObjectIsNull";
                return;
            }
            WeixBiz weixBiz = new WeixBiz();
            String[] path = weixBiz.getWeixResourceUrl(imgformat);
            if (path == null || path.length == 0) {
                EmpExecutionContext.error("weix_lbsManagerSvt.pushSetPage.path[] Is Null");
                returnmsg = "paramsIsNull";
                return;
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(128 * 1024);
            // 去掉文件名称的文件绝对路径
            factory.setRepository(new File(path[2]));
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();
            String fileName = "";
            String fileValue = "";
            Map<String, String> fieldInfo = new HashMap<String, String>();
            while (iter.hasNext()) {
                FileItem fileItem = iter.next();
                if (!fileItem.isFormField() && fileItem.getName().length() > 0 && ("filename").equals(fileItem.getFieldName()) && "2".equals(uploadFlag)) {
                    // 图片大小为128KB 判断其大小
                    if (!weixBiz.compareWeixResource(fileItem.getSize(), "image")) {
                        returnmsg = "oversize";
                        break;
                    }
                    // 将文件写到本地服务器上 文件的绝对路径
                    fileItem.write(new File(path[0]));
                    // 判断是否使用集群 上传文件到文件服务器 文件的相对路径
                    if (WXStaticValue.ISCLUSTER == 1 && !"success".equals(new CommonBiz().uploadFileToFileCenter(path[1]))) {
                        // 是集群并且上传到文件服务器失败，则提示上传失败
                        EmpExecutionContext.error("weix_lbsManagerSvt.pushSetPage.uploadFile Is Fail");
                        returnmsg = "uploadfail";
                    }
                } else {
                    fileName = fileItem.getFieldName();
                    fileValue = fileItem.getString("UTF-8").toString();
                    fieldInfo.put(fileName, fileValue);
                }
            }
            if (!"".equals(returnmsg)) {
                return;
            }
            lbsPushset.setPushtype(2);
            // 如果 uploadFlag ==2 ，则上传上来一张图片
            if ("2".equals(uploadFlag)) {
                lbsPushset.setImgurl(path[1]);
            }
            if (picUrl != null && picUrl != "") {
                lbsPushset.setImgurl(picUrl);
            }
            String note = fieldInfo.get("note");
            if (note != null) {
                lbsPushset.setNote(note);
            } else {
                lbsPushset.setNote("");
            }
            fieldInfo.clear();
            // 将设置为默认值
            lbsPushset.setRadius("");
            lbsPushset.setAutoradius(0);
            lbsPushset.setPushcount(0);
            lbsPushset.setAutomore(0);
            lbsPushset.setModitytime(new Timestamp(System.currentTimeMillis()));
            if (baseBiz.updateObj(lbsPushset)) {
                returnmsg = "success";
            } else {
                returnmsg = "fail";
            }
            request.setAttribute("pushsetObj", lbsPushset);
        } catch (Exception e) {
            returnmsg = "error";
            EmpExecutionContext.error(e, "weix_lbsManagerSvt.pushSetPage Is Error");
        } finally {
            request.setAttribute("pushsetResult", returnmsg);
            request.getRequestDispatcher(LBS_PATH + "/weix_lbsUpload.jsp").forward(request, response);
        }
    }


}
