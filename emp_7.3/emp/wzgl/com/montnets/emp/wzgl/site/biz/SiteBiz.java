/**
 * @description
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2013-12-27 上午11:09:10
 */
package com.montnets.emp.wzgl.site.biz;

import java.io.File;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.site.LfSitInfo;
import com.montnets.emp.entity.site.LfSitPage;
import com.montnets.emp.entity.site.LfSitPlant;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.util.RandomStrUtil;
import com.montnets.emp.ottbase.util.StringUtils;
import com.montnets.emp.ottbase.util.TxtFileUtil;
import com.montnets.emp.util.GetSxCount;

/**
 * @description 微站管理
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2013-12-27 上午11:09:10
 */

public class SiteBiz extends BaseBiz
{
    /**
     * 日期格式
     */
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 删除微站
     * 
     * @description
     * @param sId
     *        微站ID
     * @return
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-28 下午03:39:15
     */
    public boolean deleteSiteInfo(String sId)
    {
        boolean result = false;
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

        // 获取事务连接
        Connection conn = empTransDao.getConnection();

        try
        {
            empTransDao.beginTransaction(conn);

            // 删除站点页面的控件信息
            conditionMap.put("sId", sId);
            Integer delPlantNum = empTransDao.delete(conn, LfSitPlant.class, conditionMap);
            if(delPlantNum == null || delPlantNum == 0)
            {
                return result;
            }

            // 删除站点的页面信息
            Integer delPageNum = empTransDao.delete(conn, LfSitPage.class, conditionMap);
            if(delPageNum == null || delPageNum == 0)
            {
                return result;
            }

            // 删除站点信息
            Integer delSitNum = empTransDao.delete(conn, LfSitInfo.class, sId);
            if(delSitNum == null || delSitNum == 0)
            {
                return result;
            }
            empTransDao.commitTransaction(conn);
        }
        catch (Exception e)
        {
            result = false;
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "微站管理-创建微站-删除微站发生异常！");
        }
        finally
        {
            empTransDao.closeConnection(conn);
        }

        return result;
    }

    /**
     * 生成微站控件值
     * 
     * @param sign
     *        （取值为：normal_head、normal_link、normal_list、normal_bottom）
     * @param corpCode
     *        集团编码
     * @param plantId
     *        版块ID
     * @param values
     *        页面传过来的值
     * @return 需要更新LfSitPlant对象
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-30 下午02:50:48
     */
    @SuppressWarnings("unchecked")
    private LfSitPlant createPlantValues(String sign, String corpCode, String plantId, ArrayList<HashMap<String, String>> values)
    {
        LfSitPlant otSitPlant = null;
        try
        {
            otSitPlant = empDao.findObjectByID(LfSitPlant.class, Long.valueOf(plantId));
            if(null != otSitPlant)
            {
                JSONObject jsonFatherObject = new JSONObject();
                JSONObject jsonSonObject = null;
                JSONArray jsonArray = new JSONArray();
                jsonFatherObject.put("plantId", plantId);
                if(null == sign || "".equals(sign))
                {
                    return null;
                }
                else
                {
                    jsonFatherObject.put("plantType", sign);

                    if(null != values && values.size() > 0)
                    {
                        jsonFatherObject.put("count", values.size());
                        for (Iterator iterator = values.iterator(); iterator.hasNext();)
                        {
                            HashMap<String, String> itemMap = (HashMap<String, String>) iterator.next();
                            jsonSonObject = new JSONObject();
                            jsonSonObject.putAll(itemMap);
                            jsonArray.add(jsonSonObject);
                        }
                        jsonFatherObject.put("items", jsonArray);
                    }
                    else
                    {
                        jsonFatherObject.put("count", 0);
                    }

                    otSitPlant.setFeildValues(jsonFatherObject.toString());
                    otSitPlant.setModitytime(new Timestamp(System.currentTimeMillis()));
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "微站管理-保存微站信息发生异常！");
        }
        return otSitPlant;
    }

    /**
     * 生成微站控件值
     * 
     * @param sign
     *        （取值为：normal_head、normal_link、normal_list、normal_bottom）
     * @param corpCode
     *        集团编码
     * @param plantId
     *        版块ID
     * @param values
     *        页面传过来的值
     * @return 操作结果 true 成功 false 失败
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-30 下午02:43:36
     */
    public boolean updatePlantValues(String sign, String corpCode, String plantId, ArrayList<HashMap<String, String>> values)
    {
        boolean result = false;
        // 获取事物连接
        Connection conn = null;
        try
        {
            LfSitPlant otSitPlant = createPlantValues(sign, corpCode, plantId, values);
            if(null != otSitPlant)
            {
                conn = empTransDao.getConnection();
                empTransDao.beginTransaction(conn);
                result = empTransDao.update(conn, otSitPlant);

                if(result)
                {
                    empTransDao.commitTransaction(conn);
                }
                else
                {
                    empTransDao.rollBackTransaction(conn);
                }
            }
        }
        catch (Exception e)
        {
            result = false;
            if(null != conn)
            {
                empTransDao.rollBackTransaction(conn);
            }
            EmpExecutionContext.error(e, "微站管理-保存微站信息发生异常！");
        }
        finally
        {
            if(null != conn)
            {
                empTransDao.closeConnection(conn);
            }
        }
        return result;
    }

    /**
     * 获取微站控件值
     * 
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2013-12-30 上午10:07:17
     */
    public JSONObject getPlantValues(String corpCode, String plantId)
    {
        JSONObject jsonObject = null;
        try
        {
            if(null == plantId || "".equals(plantId))
            {
                return jsonObject;
            }
            else
            {
                LfSitPlant otSitPlant = empDao.findObjectByID(LfSitPlant.class, Long.valueOf(plantId));
                if(null != otSitPlant)
                {
                    String feildValue = otSitPlant.getFeildValues();
                    jsonObject = StringUtils.parsJsonObj(feildValue);
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "微站管理-加载微站信息发生异常！");
        }
        return jsonObject;
    }

    /**
     * @description 微站预览(后台)
     * @param corpCode
     * @param pageId
     * @return
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-30 下午05:18:23
     */
    @SuppressWarnings("unchecked")
    public JSONObject previewSiteBySId(String pageType, String sId)
    {
        JSONObject jsonObject = new JSONObject();
        
        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("sId", sId);
            conditionMap.put("pageType", pageType);
            List<LfSitPage> otSitPageList = empDao.findListByCondition(LfSitPage.class, conditionMap, null);
            if(null != otSitPageList && otSitPageList.size() > 0)
            {
                LfSitPage otSitPage = otSitPageList.get(0);
                jsonObject.put("otSitPage", otSitPage);
                String pageId = String.valueOf(otSitPage.getPageId());
                conditionMap.clear();
                conditionMap.put("pageId", pageId);
            }
            List<LfSitPlant> otSitPlantList = empDao.findListByCondition(LfSitPlant.class, conditionMap, null);
            if(null != otSitPlantList && otSitPlantList.size() > 0)
            {
                jsonObject.put("otSitPlantList", otSitPlantList);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "预览微站信息发生异常！");
        }
        return jsonObject;

    }

    /**
     * @description 微站预览（手机端）
     * @param urlToken
     *        站点URL token或者page Url token (S开头为站点url，P开头为页面url)
     * @return JSONObject
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-7 上午09:54:01
     */
    @SuppressWarnings("unchecked")
    public JSONObject previewSiteByToken(String urlToken)
    {
        JSONObject jsonObject = new JSONObject();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try
        {
            if(null == urlToken)
            {
                urlToken = "";
                return null;
            }
            // 如果是站点url，默认进入站点首页（pageType为normal_page1的页面）
            if(urlToken.startsWith("S"))
            {
                conditionMap.put("url", urlToken);
                List<LfSitInfo> otSitInfoList = empDao.findListByCondition(LfSitInfo.class, conditionMap, null);
                if(null != otSitInfoList && otSitInfoList.size() > 0)
                {
                    LfSitInfo otSitInfo = otSitInfoList.get(0);
                    String sId = String.valueOf(otSitInfo.getSId());

                    conditionMap.clear();
                    conditionMap.put("sId", sId);
                    conditionMap.put("pageType", "normal_page1");
                }
            }
            else if(urlToken.startsWith("P"))
            {
                conditionMap.clear();
                conditionMap.put("url", urlToken);
            }

            // 根据url判断出站点还是页面，得到具体页面信息
            List<LfSitPage> otSitPageList = empDao.findListByCondition(LfSitPage.class, conditionMap, null);
            if(null != otSitPageList && otSitPageList.size() > 0)
            {
                LfSitPage otSitPage = otSitPageList.get(0);
                jsonObject.put("otSitPage", otSitPage);
                String pageId = String.valueOf(otSitPage.getPageId());
                conditionMap.clear();
                conditionMap.put("pageId", pageId);
                List<LfSitPlant> otSitPlantList = empDao.findListByCondition(LfSitPlant.class, conditionMap, null);
                if(null != otSitPlantList && otSitPlantList.size() > 0)
                {
                    jsonObject.put("otSitPlantList", otSitPlantList);
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "预览微站信息发生异常！");
        }
        return jsonObject;
    }
    
    /**
     * @description 通过token获取当前微站请求的访问页面
     * @param urlToken
     * 站点urlToken(S开头为站点标识，P开头为页面标识)
     * @return JSONObject
     * @author fangyt <fanglu@montnets.com>
     * @datetime 2014-3-11
     */
    @SuppressWarnings("unchecked")
    public LfSitPage getSitePageByToken(String urlToken)
    {
        //当前访问微站页面
        LfSitPage otSitPage  = null;
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try
        {
            if(null == urlToken)
            {
                urlToken = "";
                return null;
            }
            // 如果是站点url，默认进入站点首页（pageType为normal_page1的页面）
            if(urlToken.startsWith("S"))
            {
                conditionMap.put("url", urlToken);
                List<LfSitInfo> otSitInfoList = empDao.findListByCondition(LfSitInfo.class, conditionMap, null);
                if(null != otSitInfoList && otSitInfoList.size() > 0)
                {
                    LfSitInfo otSitInfo = otSitInfoList.get(0);
                    String sId = String.valueOf(otSitInfo.getSId());
                    conditionMap.clear();
                    conditionMap.put("sId", sId);
                    conditionMap.put("pageType", "normal_page1");
                }
            }
            else if(urlToken.startsWith("P"))
            {
                conditionMap.clear();
                conditionMap.put("url", urlToken);
            }else{
                return null;
            }

            // 根据url判断出站点还是页面，得到具体页面信息
            LinkedHashMap<String,String> orderMap = new LinkedHashMap<String,String>();
            orderMap.put("pageId", "ASC");
            List<LfSitPage> otSitPageList = empDao.findListByCondition(LfSitPage.class, conditionMap, orderMap);
            if(null != otSitPageList && otSitPageList.size() > 0)
            {
                otSitPage = otSitPageList.get(0);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "预览微站信息发生异常！");
        }
        return otSitPage;
    }
    
    /**
     * @description 上传图片到本地服务器
     * @param path
     *        创建文件路径 String[] path = this.getResourceUrl(msgtype); msgtype 为
     *        :图片（image）
     * @param msgtype
     *        图片类型
     * @param filename
     *        文件页面name的值
     * @param request
     *        请求
     * @return paraIsNull 参数不合法 oversize 文件超过大小 uoloadfail 上传失败 success 上传成功
     *         error出现异常
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-3 上午09:09:31
     */
    @SuppressWarnings("unchecked")
    public String uploadToServer(String[] path, String msgtype, String filename, HttpServletRequest request)
    {
        String returnmsg = "paraIsNull";
        try
        {
            if(path == null || path.length == 0)
            {
                EmpExecutionContext.error("WeixBiz.uploadToServer.path[] Is Null");
                return returnmsg;
            }
            if(msgtype == null || "".equals(msgtype) || filename == null || "".equals(filename) || !"image".equals(msgtype))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.uploadToServer.msgtype:" + msgtype + ",filename:" + filename);
                return returnmsg;
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(100 * 1024);
            // 去掉文件名称的文件绝对路径
            factory.setRepository(new File(path[2]));
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();
            while(iter.hasNext())
            {
                FileItem fileItem = (FileItem) iter.next();
                if(!this.compareResource(fileItem.getSize(), msgtype))
                {
                    // 超过文件要求的大小
                    returnmsg = "oversize";
                    EmpExecutionContext.error("WeixBiz.uploadToServer Is oversize");
                    return returnmsg;
                }
                else if(!fileItem.isFormField() && fileItem.getName().length() > 0 && filename.equals(fileItem.getFieldName()))
                {
                    // 将文件写到本地服务器上 文件的绝对路径
                    fileItem.write(new File(path[0]));
                    // 判断是否使用集群 上传文件到文件服务器 文件的相对路径
                    if(WXStaticValue.ISCLUSTER == 1 && !"success".equals(new CommonBiz().uploadFileToFileCenter(path[1])))
                    {
                        // 是集群并且上传到文件服务器失败，则提示上传失败
                        returnmsg = "uoloadfail";
                        EmpExecutionContext.error("WeixBiz.uploadToServer.uploadFile Is Fail");
                        return returnmsg;
                    }
                    returnmsg = "success";
                }
            }

        }
        catch (Exception e)
        {
            returnmsg = "error";
            EmpExecutionContext.error(e, "WeixBiz.uploadToServer Is Error");
        }
        return returnmsg;
    }

    /**
     * @description 微信 判断文件大小是否符合要求
     * @param resourceSize
     *        上传文件大小
     *        图片（image）: 128K，支持JPG格式
     * @param resourceType
     *        文件类型
     * @return 超过规定大小返回false， 符合要求返回true
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-3 上午09:09:52
     */
    public boolean compareResource(long resourceSize, String resourceType)
    {
        boolean isFlag = false;
        try
        {
            if("image".equals(resourceType))
            {
                if(resourceSize - 128 * 1024 > 0)
                {
                    EmpExecutionContext.error("WeixBiz.compareWeixResource.image.size > 128 * 1024");
                    return false;
                }
            }
            else
            {
                EmpExecutionContext.error("WeixBiz.compareWeixResource.resourceType Is Errer");
                return false;
            }
            isFlag = true;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.compareWeixResource Is Errer");
        }
        return isFlag;
    }

    /**
     * @description 微信资源文件
     * @param type
     *        image 图片
     * @param fileType
     *        文件类型
     * @return
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-3 上午09:27:03
     */
    public String[] getResourceUrl(String type)
    {
        String[] url = new String[3];
        try
        {
            String strNYR = "";
            TxtFileUtil txtFileUtil = new TxtFileUtil();
            try
            {
                strNYR = txtFileUtil.getCurNYR();
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "WeixBiz.getWeixResourceUrl.getCurNYR Is Errer");
                return null;
            }
            String fileDirUrl = "";
            String fileType = "";
            if("image".equals(type))
            {
                fileDirUrl = WXStaticValue.WZGL_IMG + strNYR;
                fileType = "jpg";
            }
            else
            {
                EmpExecutionContext.error("WeixBiz.getWeixResourceUrl.type Is Errer");
                return null;
            }
            String webRoot = txtFileUtil.getWebRoot();
            try
            {
                File file = new File(webRoot + fileDirUrl);
                if(!file.exists())
                {
                    file.mkdirs();
                }
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "WeixBiz.getWeixResourceUrl.mkdirs() Is Errer");
                return null;
            }
            GetSxCount sx = GetSxCount.getInstance();
            Date time = Calendar.getInstance().getTime();
            String saveName = type + "_" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time) + "_" + sx.getCount() + "." + fileType;
            String logicUrl = fileDirUrl + saveName;
            String physicsUrl = webRoot + logicUrl;
            url[0] = physicsUrl;
            url[1] = logicUrl;
            url[2] = webRoot + fileDirUrl;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.getWeixResourceUrl Is Errer");
        }
        return url;
    }

    /**
     * 点击“下一步”按钮，根据模板风格创建站点
     * 
     * @param corpCode
     *        集团编码
     * @param typeId
     *        站点风格ID
     * @return JSONObject
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-6 上午10:11:03
     */
    @SuppressWarnings("unchecked")
    public JSONObject createSiteByTemp(String corpCode, String typeId)
    {
        JSONObject jsonObject = new JSONObject();

        // 获取事物连接
        Connection conn = null;
        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LfSitInfo otSitInfo = null;
            LfSitPage otSitPage = null;
            LfSitPlant otSitPlant = null;
            // 新创建的page页面数据
            List<LfSitPage> newPageList = new ArrayList<LfSitPage>();

            // 找到模板站点信息，并根据模板站点创建一个新站点
            // 微站基本信息
            conditionMap.put("typeId", typeId);
            conditionMap.put("corpCode", corpCode);
            conditionMap.put("isSystem", "1");
            List<LfSitInfo> otSitInfoList = empDao.findListByCondition(LfSitInfo.class, conditionMap, null);

            if(null != otSitInfoList && otSitInfoList.size() > 0)
            {
                otSitInfo = otSitInfoList.get(0);
                // 此处在清空sid前先保存sid是为了下面有用，清空是为了能正确的插入数据库
                Long sid = otSitInfo.getSId();
                otSitInfo.setSId(null);
                otSitInfo.setIsSystem(0);
                otSitInfo.setUrl(RandomStrUtil.getUniqueneStr((char) 83, 20));
                otSitInfo.setCreatetime(new Timestamp(System.currentTimeMillis()));

                // 获取事物，开启事物
                conn = empTransDao.getConnection();
                empTransDao.beginTransaction(conn);

                // 插入数据到 微站基本信息表 （LF_SIT_INFO）
                Long addLong = empTransDao.saveObjProReturnID(conn, otSitInfo);

                if(null == addLong || addLong == 0L)
                {
                    EmpExecutionContext.error("新增站点失败！");
                    // 保存失败
                    empTransDao.rollBackTransaction(conn);
                }
                else
                {
                    // 更新微站的S_ID用于页面
                    otSitInfo.setSId(addLong);

                    // 微站页面信息
                    conditionMap.clear();
                    conditionMap.put("sId", String.valueOf(sid));
                    conditionMap.put("corpCode", corpCode);
                    List<LfSitPage> otSitPageList = empDao.findListByCondition(LfSitPage.class, conditionMap, null);
                    if(null != otSitPageList && otSitPageList.size() > 0)
                    {
                        //页面控件
                        List<LfSitPlant> otSitPlants = new ArrayList<LfSitPlant>();
                        
                        for (int i = 0; i < otSitPageList.size(); i++)
                        {
                            otSitPage = otSitPageList.get(i);
                            // 此处在清空pageId前先保存pageId是为了下面有用，清空是为了能正确的插入数据库
                            Long pageId = otSitPage.getPageId();
                            otSitPage.setPageId(null);
                            otSitPage.setCreatetime(Timestamp.valueOf(df.format(new Date())));
                            otSitPage.setSId(addLong);
                            otSitPage.setUrl(RandomStrUtil.getUniqueneStr((char) 80, 20));

                            // 插入数据到 微站页面表 （LF_SIT_PAGE）
                            Long newPageId = empTransDao.saveObjProReturnID(conn, otSitPage);
                            if(null == newPageId || newPageId == 0L)
                            {
                                EmpExecutionContext.error("新增站点失败！");
                                // 保存失败
                                empTransDao.rollBackTransaction(conn);
                                return null;
                            }

                            // 微站板块信息
                            conditionMap.clear();
                            conditionMap.put("pageId", String.valueOf(pageId));
                            conditionMap.put("corpCode", corpCode);
                            List<LfSitPlant> otSitPlantList = empDao.findListByCondition(LfSitPlant.class, conditionMap, null);
                            
                            if(null != otSitPlantList && otSitPlantList.size() > 0)
                            {
                                for (int j = 0; j < otSitPlantList.size(); j++)
                                {
                                    otSitPlant = otSitPlantList.get(j);
                                    otSitPlant.setCreatetime(Timestamp.valueOf(df.format(new Date())));
                                    otSitPlant.setPageId(newPageId);
                                    otSitPlant.setSId(addLong);                                    
                                    otSitPlants.add(otSitPlant);
                                }
                                // 设置创建后的pageId
                                otSitPage.setPageId(newPageId);
                                // 新建的page页面
                                newPageList.add(otSitPage);
                            }
                            else
                            {
                                EmpExecutionContext.error("新增站点失败！");
                                empTransDao.rollBackTransaction(conn);
                            }
                        }

                        // 插入数据到 微站板块表 （LF_SIT_PLANT）
                        int num = empTransDao.save(conn, otSitPlants, LfSitPlant.class);
                        if(num == 0)
                        {
                            EmpExecutionContext.error("新增站点失败！");
                            // 保存失败
                            empTransDao.rollBackTransaction(conn);
                        }
                        
                        // 需要传递到微站创建页面的数据
                        jsonObject.put("otSitInfo", otSitInfo);
                        jsonObject.put("otPageList", newPageList);
                    }
                    else
                    {
                        EmpExecutionContext.error("新增站点失败！");
                        empTransDao.rollBackTransaction(conn);
                    }
                }
                empTransDao.commitTransaction(conn);
            }
        }
        catch (Exception e)
        {
            if(null != conn)
            {
                empTransDao.rollBackTransaction(conn);
            }
            EmpExecutionContext.error(e, "根据风格模板创建微站失败！");
        }
        finally
        {
            if(null != conn)
            {
                empTransDao.closeConnection(conn);
            }
        }
        return jsonObject;
    }

    /**
     * 删除微站页面
     * 
     * @param pageId
     *        页面ID
     * @param corpCode
     *        集团编码
     * @return 删除结果 true：成功 false：失败
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-6 上午11:04:38
     */
    public boolean deletePageInfo(String pageId, String corpCode)
    {
        // 用来标识操作结果
        boolean result = false;
        // 获取事物连接
        Connection conn = null;
        try
        {
            if(null == pageId || "".equals(pageId))
            {
                EmpExecutionContext.error("获取站点页面信息失败！");
                return result;
            }

            conn = empTransDao.getConnection();
            empTransDao.beginTransaction(conn);

            // 删除站点页面plant
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("pageId", pageId);
            int deleteNum = empTransDao.delete(conn, LfSitPlant.class, conditionMap);
            if(deleteNum == 0)
            {
                EmpExecutionContext.error("删除站点页面plant失败！");
                empTransDao.rollBackTransaction(conn);
                return result;
            }

            // 删除站点页面
            int deletenum = empTransDao.delete(conn, LfSitPage.class, pageId);
            if(deletenum == 0)
            {
                // 删除失败
                EmpExecutionContext.error("删除站点页面失败！");
                empTransDao.rollBackTransaction(conn);
            }
            else
            {
                // 删除成功
                result = true;
                empTransDao.commitTransaction(conn);
            }
        }
        catch (Exception e)
        {
            // 删除失败
            result = false;
            EmpExecutionContext.error(e, "微站管理-编辑微站-删除微站页面名称发生异常！");
            if(null != conn)
            {
                empTransDao.rollBackTransaction(conn);
            }
        }
        finally
        {
            if(null != conn)
            {
                empTransDao.closeConnection(conn);
            }
        }
        return result;
    }

    /**
     * 复制微站页面
     * 
     * @param pageId
     *        被复制的页面ID
     * @param corpCode
     *        集团编码
     * @return JSONObject
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-6 上午11:24:10
     */
    @SuppressWarnings("unchecked")
    public LfSitPage copyPageInfo(String pageId, String corpCode)
    {
        //声明页面对象
        LfSitPage otSitPage  = null;
        // 获取事物连接
        Connection conn = null;
        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            if(null == pageId || "".equals(pageId))
            {
                EmpExecutionContext.error("获取站点页面信息失败！");
                return null;
            }
            else
            {
                // 微站页面信息
                otSitPage = empDao.findObjectByID(LfSitPage.class, Long.valueOf(pageId));

                if(null == otSitPage)
                {
                    EmpExecutionContext.error("获取站点页面信息失败！");
                }
                else
                {
                    // 此处在清空pageId前先保存pageId是为了下面有用，清空是为了能正确的插入数据库
                    otSitPage.setPageId(null);
                    otSitPage.setCreatetime(Timestamp.valueOf(df.format(new Date())));
                    otSitPage.setUrl(RandomStrUtil.getUniqueneStr((char) 80, 20));

                    // 获取事务，开启事务
                    conn = empTransDao.getConnection();
                    empTransDao.beginTransaction(conn);
                    // 插入数据到 微站页面表 （LF_SIT_PAGE）
                    Long addLong = empTransDao.saveObjProReturnID(conn, otSitPage);
                    if(null == addLong || addLong == 0L)
                    {
                        EmpExecutionContext.error("复制站点页面失败！");
                        // 保存失败
                        empTransDao.rollBackTransaction(conn);
                    }
                    else
                    {
                        // 微站板块信息
                        conditionMap.clear();
                        conditionMap.put("pageId", String.valueOf(pageId));
                        conditionMap.put("corpCode", corpCode);
                        List<LfSitPlant> otSitPlantList = empDao.findListByCondition(LfSitPlant.class, conditionMap, null);
                        List<LfSitPlant> otSitPlants = new ArrayList<LfSitPlant>();
                        LfSitPlant otSitPlant = null;
                        if(null != otSitPlantList && otSitPlantList.size() > 0)
                        {
                            for (int j = 0; j < otSitPlantList.size(); j++)
                            {
                                otSitPlant = otSitPlantList.get(j);
                                otSitPlant.setCreatetime(Timestamp.valueOf(df.format(new Date())));
                                otSitPlant.setPageId(addLong);
                                otSitPlants.add(otSitPlant);
                            }

                            // 插入数据到 微站板块表 （LF_SIT_PLANT）
                            int num = empTransDao.save(conn, otSitPlants, LfSitPlant.class);
                            if(num == 0)
                            {
                                EmpExecutionContext.error("复制站点页面失败！");
                                // 保存失败
                                empTransDao.rollBackTransaction(conn);
                            }
                            else
                            {
                                empTransDao.commitTransaction(conn);
                                otSitPage.setPageId(addLong);
                            }
                        }
                        else
                        {
                            EmpExecutionContext.error("复制站点页面失败！");
                            empTransDao.rollBackTransaction(conn);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            if(null != conn)
            {
                empTransDao.rollBackTransaction(conn);
            }
            EmpExecutionContext.error(e, "复制微站页面失败！");
        }
        finally
        {
            if(null != conn)
            {
                empTransDao.closeConnection(conn);
            }
        }
        return otSitPage;
    }
}
