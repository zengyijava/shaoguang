package com.montnets.emp.wxgl.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.lbs.LfLbsPios;
import com.montnets.emp.entity.lbs.LfLbsPushset;
import com.montnets.emp.entity.lbs.LfLbsUserPios;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.ottbase.constant.BaiduHttpUrl;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.param.BaiduMapParams;
import com.montnets.emp.ottbase.param.HttpReturnParams;
import com.montnets.emp.ottbase.service.HttpRequestService;
import com.montnets.emp.ottbase.util.GlobalMethods;
import com.montnets.emp.wxgl.base.message.MessageUtil;
import com.montnets.emp.wxgl.base.message.TextMessage;
import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author yejiangmin <282905282@qq.com>
 * @description 百度LBS逻辑处理类
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-29 下午02:30:14
 */
public class LbsPiosBiz extends SuperBiz {
    /**
     * 查询城市范围请求URL
     */
    private final String LOCATION_SERVCH = "http://api.map.baidu.com/place/v2/search?";
    /**
     * key openid+aid value LfLbsUserPios
     */
    public final Map<String, LfLbsUserPios> userpiosMapcontactAid = new HashMap<String, LfLbsUserPios>();
    /**
     * 三分钟
     */
    public final static long timeInterval = 3 * 60L * 1000L;

    /**
     * @param lat      纬度
     * @param lng      经度
     * @param openid   普通用户ID
     * @param uptime   上传地理位置时间
     * @param fromtype 来源 location 我的位置 menu 菜单 push 服务器推送
     * @param account  公众帐号对象
     * @return 返回对象或者null
     * @description 获取用户的地理位置信息保存起来
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-18 上午08:43:12
     */
    public synchronized void handleLbsUserPios(String lat, String lng, String openid, long uptime, String fromtype, LfWeiAccount account) {
        try {
            String key = openid + account.getAId();
            LfLbsUserPios userpios = userpiosMapcontactAid.get(key);
            // 如果map中不存在对象则新增
            if (userpios == null) {
                userpios = new LfLbsUserPios();
                userpios.setAId(account.getAId());
                userpios.setCorpCode(account.getCorpCode());
                userpios.setOpenid(openid);
                userpios.setModifytime(new Timestamp(uptime));
                userpios.setLng(lng);
                userpios.setLat(lat);
                userpiosMapcontactAid.put(key, userpios);
                EmpExecutionContext.error("add key success ,key:" + key);
            } else if ("location".equals(fromtype) || "menu".equals(fromtype) || "push".equals(fromtype)) {
                // 如果是位置或者菜单,则更新MAP中存在的值
                //TODO 这里可能需要做推送时间跟内存中保存的时间判断 
                userpiosMapcontactAid.remove(key);
                userpios.setModifytime(new Timestamp(uptime));
                userpios.setLat(lat);
                userpios.setLng(lng);
                userpiosMapcontactAid.put(key, userpios);
            } else if (System.currentTimeMillis() - userpios.getModifytime().getTime() - timeInterval > 0) {
                userpiosMapcontactAid.remove(key);
                EmpExecutionContext.error("remove key success ,key:" + key);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "LbsPiosBiz.handleLbsUserPios is error");
        }
    }

    /**
     * @param openid 普通用户opengid
     * @param aid    公众帐号ID
     * @return LfLbsUserPios 或者null值
     * @description 获取保存在内存中的用户的地理位置信息值 如果超过三分钟,则移出掉
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-18 上午08:35:24
     */
    public LfLbsUserPios getLbsUserPios(String openid, String aid) {
        LfLbsUserPios userpios = null;
        try {
            String key = openid + aid;
            userpios = userpiosMapcontactAid.get(key);
            if (userpios != null && System.currentTimeMillis() - userpios.getModifytime().getTime() - timeInterval > 0) {
                // 如果该值过期，则移出掉
                userpiosMapcontactAid.remove(key);
                EmpExecutionContext.error("remove key success ,key:" + key);
                userpios = null;
            }
            if (userpiosMapcontactAid != null && userpiosMapcontactAid.size() > 0 && (userpiosMapcontactAid.size() % 3 == 0)) {
                EmpExecutionContext.error("enter LbsPiosThread is success");
                // 启用线程，进行内存中用户信息时间判断
                new LbsPiosThread(userpiosMapcontactAid).start();
            }
        } catch (Exception e) {
            userpios = null;
            EmpExecutionContext.error(e, "LbsPiosBiz.getLbsUserPios is error");
        }
        return userpios;
    }

    /**
     * @param paramsMap xmlmap
     * @param account   公众帐号
     * @description 服务器主动推送用户的地理位置上来
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-18 上午11:23:50
     */
    public void getPushUserPios(LinkedHashMap<String, String> paramsMap, LfWeiAccount account) {
        try {
            // 纬度
            String lat = paramsMap.get("Latitude");
            // 经度
            String lng = paramsMap.get("Longitude");
            // 上报时间
            String uptime = paramsMap.get("CreateTime");
            // 普通用户的openid
            String openid = paramsMap.get("FromUserName");
            if (lat == null || "".equals(lat) || lng == null || "".equals(lng) || uptime == null || "".equals(uptime) || openid == null || "".equals(openid)) {
                EmpExecutionContext.error("WxCommBiz.getPushUserPios.lat:" + lat + ",lng:" + lng + ",uptime:" + uptime + ",openid:" + openid);
                return;
            }
            // 将上报的用户地理位置增加到MAP去 add或者update
            handleLbsUserPios(lat, lng, openid, Long.valueOf(uptime), "push", account);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "LbsPiosBiz.getPushUserPios is error");
        }
    }

    /**
     * @param paramsMap 请求xmlkeyvalue的 map
     * @param account   公众帐号
     * @return 文本xml或者图文xml或者空字符串
     * @description 处理菜单中选择网点服务按扭的操作
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-18 上午10:56:19
     */
    public String getLocationMsgXmlByMenu(HashMap<String, String> paramsMap, LfWeiAccount account) {
        String returnmsg = "";
        try {
            String openid = paramsMap.get("FromUserName");
            LfLbsUserPios userPios = getLbsUserPios(openid, account.getAId().toString());
            // 如果没有用户地理位置则下发文本提示
            if (userPios == null) {
                TextMessage textMessage = new TextMessage();
                String coontent = "请发送您当前的位置,查看附近的服务网点.点击屏幕下方的键盘,点选右侧的\"+\"按钮中的\"位置\",即可发送您的位置";
                textMessage.setContent(coontent);
                textMessage.setCreateTime(System.currentTimeMillis());
                textMessage.setFromUserName("");
                textMessage.setToUserName("");
                textMessage.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_TEXT);
                returnmsg = MessageUtil.textMessageToXml(textMessage);
            } else {
                // 如果当前最新的用户地理位置 ,则下发对应的图文
                returnmsg = getLocationMsgXml(userPios.getLng(), userPios.getLat(), openid, "menu", account);
            }
        } catch (Exception e) {
            returnmsg = "";
            EmpExecutionContext.error(e, "LbsPiosBiz.getLocationMsgXmlByMenu is error");
        }
        return returnmsg;
    }

    /**
     * 参数传递转换
     *
     * @param params
     * @param mapParams
     * @throws Exception
     */
    public void changeParams(HttpReturnParams params, BaiduMapParams mapParams) throws Exception {
        try {
            mapParams.setErrCode(params.getErrCode());
            mapParams.setErrMsg(params.getErrMsg());
            if (mapParams.getErrCode() != null && "000".equals(mapParams.getErrCode())) {
                // 返回JSON格式
                if (params.getJsonObject() != null) {
                    mapParams.setJsonObj(params.getJsonObject());
                }
                // 返回XML格式
                if (params.getReturnXml() != null && !"".equals(params.getReturnXml())) {
                    mapParams.setReturnXml(params.getReturnXml());
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "LbsPiosBiz.changeParams Is Error");
        }
    }

    /**
     * 城市内检索方法
     *
     * @param mapParams
     * @return
     * @description
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-29 下午03:21:40
     */
    public BaiduMapParams searchPalce(BaiduMapParams mapParams) {
        try {
            String city = mapParams.getCity();
            if (city == null || "".equals(city)) {
                mapParams.setErrMsg("LbsPiosBiz.searchPalce.city Is Null");
                return mapParams;
            }
            String location = mapParams.getLocation();
            if (location == null || "".equals(location)) {
                mapParams.setLocation("LbsPiosBiz.searchPalce.location Is Null");
                return mapParams;
            }
            String url = LOCATION_SERVCH + "ak=" + WXStaticValue.BAIDU_MAP_AK + "&output=json&page_size=10&page_num=0&scope=1&region=" + city + "&query=" + location + "&time=" + System.currentTimeMillis();
            HttpReturnParams params = new HttpReturnParams();
            params.setUrl(url);
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            params.setRequestType("GET");
            params.setMenuCode("");
            params = new HttpRequestService().requestOttHttp(params);
            changeParams(params, mapParams);
        } catch (Exception e) {
            mapParams.setErrMsg("error");
            EmpExecutionContext.error(e, "LbsPiosBiz.searchPalce Is Error");
        }
        return mapParams;
    }

    /**
     * @param LonA
     * @param LatA
     * @param LonB
     * @param LatB
     * @return
     * @description 得到两点间的距离 KM
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-4 上午11:00:46
     */
    private Double getDistance(Double LonA, Double LatA, Double LonB, Double LatB) {
        // 东西经，南北纬处理，只在国内可以不处理(假设都是北半球，南半球只有澳洲具有应用意义)
        // 地球半径（千米）
        Double R = 6371.004;
        Double C = Math.sin(rad(LatA)) * Math.sin(rad(LatB)) + Math.cos(rad(LatA)) * Math.cos(rad(LatB)) * Math.cos(rad(LonA - LonB));
        return (R * Math.acos(C));
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 得到两点间的距离 M
     *
     * @param lat1 纬度1
     * @param lng1 经度1
     * @param lat2 纬度2
     * @param lng2 经度2
     * @return
     */
    public Long getDistanceOfMeter(Double lat1, Double lng1, Double lat2, Double lng2) {
        Double radLat1 = rad(lat1);
        Double radLat2 = rad(lat2);
        Double a = radLat1 - radLat2;
        Double b = rad(lng1) - rad(lng2);
        Double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        Double R = 6371.004;
        // s = Math.round(s * EARTH_RADIUS * 10000) / 10;
        Long d = Math.round(s * R * 10000) / 10;
        return d;
    }

    /**
     * @param lbsList 需要获取出的测距
     * @param lat     当前纬度
     * @param lng     当前经度
     * @param flag    是否需要排序
     * @return List<LfLbsPios>
     * @description 获取的 LIST中距离以及排序
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-16 上午09:36:01
     */
    public List<LfLbsPios> getDistanceMsg(List<LfLbsPios> lbsList, Double lat, Double lng, boolean flag) {
        List<LfLbsPios> disstanceLbsList = null;
        try {
            // 判断集合是否为空
            if (lbsList != null && lbsList.size() > 0) {
                disstanceLbsList = new ArrayList<LfLbsPios>();
                LfLbsPios lbspios = null;
                Double distance = null;
                Double doulat = 0.0;
                Double doulng = 0.0;
                for (int i = 0; i < lbsList.size(); i++) {
                    lbspios = lbsList.get(i);
                    doulat = Double.valueOf(lbspios.getLat());
                    doulng = Double.valueOf(lbspios.getLng());
                    // 测试距离
                    distance = getDistance(doulng, doulat, lng, lat);
                    lbspios.setDistance(distance);
                    disstanceLbsList.add(lbspios);
                    doulat = 0.0;
                    doulng = 0.0;
                }
                if (flag) {
                    /* 如果要按照升序排序 则o1 小于o2，返回-1（负数），相等返回0，01大于02返回1（正数） */
                    Collections.sort(disstanceLbsList, new Comparator<LfLbsPios>() {
                        @Override
                        public int compare(LfLbsPios lbs1, LfLbsPios lbs2) {
                            return lbs1.getDistance().compareTo(lbs2.getDistance());
                        }
                    });
                }

            }
        } catch (Exception e) {
            disstanceLbsList = null;
            EmpExecutionContext.error(e, "LbsPiosBiz.getDistanceMsg Is Error");
        }
        return disstanceLbsList;
    }

    /**
     * @param lng        纬度
     * @param lat        经度
     * @param openid     普通用户ID
     * @param clicktypes 点击类型 location 直接点击位置/ menu 点菜单
     * @param acctount   公众帐号
     * @return
     * @description 获取地理位置消息Msgxml
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-18 上午11:02:45
     */
    public String getLocationMsgXml(String lng, String lat, String openid, String clicktypes, LfWeiAccount acctount) {
        String msgXml = "";
        try {
            if (lat == null || "".equals(lat) || lng == null || "".equals(lng)) {
                EmpExecutionContext.error("LbsPiosBiz.getLocationMsgXml.lat:" + lat + ",lng:" + lng);
                return msgXml;
            }
            // 将用户的当前位置保存下来
            handleLbsUserPios(lat, lng, openid, System.currentTimeMillis(), clicktypes, acctount);

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", acctount.getCorpCode());
            conditionMap.put("AId", acctount.getAId().toString());
            List<LfLbsPios> lbsList = empDao.findListByCondition(LfLbsPios.class, conditionMap, null);
            if (lbsList == null || lbsList.size() == 0) {
                EmpExecutionContext.error("LbsPiosBiz.getLocationMsgXml.List<LfLbsPios> Is null");
                return msgXml;
            }
            conditionMap.clear();
            conditionMap.put("corpCode", acctount.getCorpCode());
            List<LfLbsPushset> pushSetList = empDao.findListByCondition(LfLbsPushset.class, conditionMap, null);
            if (pushSetList == null || pushSetList.size() == 0) {
                EmpExecutionContext.error("LbsPiosBiz.getLocationMsgXml.LfLbsPushset Is null");
                return msgXml;
            }
            LfLbsPushset pushset = pushSetList.get(0);
            List<LfLbsPios> otLbsPois = null;
            // 如果是多图文模式推送的话，那么需要测距于用户的距离 测算间距
            if (pushset.getPushtype() == 1) {
                otLbsPois = getDistanceMsg(lbsList, Double.valueOf(lat), Double.valueOf(lng), true);
                if (otLbsPois == null || otLbsPois.size() == 0) {
                    EmpExecutionContext.error("LbsPiosBiz.getLocationMsgXml.List<LfLbsPios> Is null");
                    return "";
                }
            }
            msgXml = createMsgXmlByLbsPois(otLbsPois, pushset, lng, lat, acctount.getAId().toString());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "LbsPiosBiz.getLocationMsgXml Is Error");
        }

        return msgXml;
    }

    /**
     * @param otLbsPois 采集点集合
     * @param pushset   下发模式设置对象
     * @param userlng   用户经度
     * @param userlat   用户的纬度
     * @return 下发图文
     * @description 创建lbs的图文
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-11 下午02:51:31
     */
    private String createMsgXmlByLbsPois(List<LfLbsPios> otLbsPois, LfLbsPushset pushset, String userlng, String userlat, String aid) {
        StringBuffer msgXml = new StringBuffer();
        try {
            if (pushset == null) {
                EmpExecutionContext.error("LbsPiosBiz.createMsgXmlByLbsPois.LfLbsPushset Is null");
                return "";
            }
            // 推送模式设置 1是多图文 2是页面模式
            Integer pushtype = pushset.getPushtype();
            if (pushtype == 1 && (otLbsPois == null || otLbsPois.size() == 0)) {
                EmpExecutionContext.error("LbsPiosBiz.createMsgXmlByLbsPois.List<LfLbsPios> Is null");
                return "";
            }
            // 图文的个数
            Integer artileCount = 0;
            List<LfLbsPios> sendLbsPiosList = new ArrayList<LfLbsPios>();
            LfLbsPios lbspios = null;
            // 多图文设置
            if (pushtype == 1) {
                // 服务半径
                double radius = Double.valueOf(pushset.getRadius());
                // 启用扩大半径搜索
                Integer autoRadius = pushset.getAutoradius();
                // 当推送条数大于采集点集合条数
                if (pushset.getPushcount() > otLbsPois.size()) {
                    artileCount = otLbsPois.size();
                } else {
                    // 当推送条数小于采集点集合条数
                    artileCount = pushset.getPushcount();
                }
                double piosradius = 0.0;
                for (int j = 0; j < artileCount; j++) {
                    if (artileCount == sendLbsPiosList.size()) {
                        break;
                    }
                    lbspios = otLbsPois.get(j);
                    // 采集点距离
                    piosradius = lbspios.getDistance();
                    // 在范围里面
                    if (radius > piosradius) {
                        // 如果没有到设置的条目，则add
                        sendLbsPiosList.add(otLbsPois.get(j));
                    } else if (autoRadius == 1 && artileCount > sendLbsPiosList.size()) {
                        // 默认增加3公里 是否自动扩大测距
                        int temp = Integer.valueOf(pushset.getRadius()) + 3;
                        radius = Double.valueOf(temp + "");
                        // 如果取出来的数据在服务半径
                        if (radius > piosradius) {
                            // 如果没有到设置的条目，则add
                            sendLbsPiosList.add(otLbsPois.get(j));
                        } else {
                            break;
                        }
                    } else if (autoRadius == 2) {
                        // 没有设置自动扩大服务半径，则取到的数据 是比服务半径大则直接跳出循环
                        EmpExecutionContext.error("LbsPiosBiz.createMsgXmlByLbsPois.autoRadius == 2");
                        break;
                    }
                    lbspios = null;
                    piosradius = 0.0;
                }

                // 如果没有相关采集点 则返回
                if (sendLbsPiosList == null || sendLbsPiosList.size() == 0) {
                    EmpExecutionContext.error("LbsPiosBiz.createMsgXmlByLbsPois.List<LfLbsPios> sendLbsPiosList Is null");
                    return "";
                }
                // 设置实际下发的条数
                artileCount = sendLbsPiosList.size();
                // 如果设置了启用发送更多的话，那么这里需要增加一个图文
                if (pushset.getAutomore() == 1) {
                    artileCount = artileCount + 1;
                }
            } else if (pushtype == 2) {
                // 单图文设置
                artileCount = 1;
            } else {
                EmpExecutionContext.error("LbsPiosBiz.createMsgXmlByLbsPois.pushtype Is error");
                return "";
            }
            msgXml.append("<xml>");
            msgXml.append("<ToUserName></ToUserName>");
            msgXml.append("<FromUserName></FromUserName>");
            msgXml.append("<CreateTime></CreateTime>");
            msgXml.append("<MsgType>news</MsgType>");
            msgXml.append("<ArticleCount>").append(String.valueOf(artileCount)).append("</ArticleCount>");
            msgXml.append("<Articles>");
            String picUrl = "";
            String note = "";
            String info = "";
            String link = "";

            String webpath = GlobalMethods.getWeixBasePath();
            // 多图文
            if (pushtype == 1) {
                String distance = "";
                for (int i = 0; i < sendLbsPiosList.size(); i++) {
                    lbspios = sendLbsPiosList.get(i);
                    String label = lbspios.getTitle();
                    String locationX = lbspios.getLat();
                    String locationY = lbspios.getLng();
                    distance = String.valueOf(lbspios.getDistance());
                    if (distance.contains(".")) {
                        int index = distance.indexOf(".");
                        distance = distance.substring(0, index) + distance.substring(index, index + 3);
                    } else {
                        EmpExecutionContext.error("LbsPiosBiz.createMsgXmlByLbsPois.distance Is error");
                        continue;
                    }
                    info = label + "  电话：" + lbspios.getTelephone() + " 距离:" + distance + " 公里";
                    picUrl = "http://api.map.baidu.com/staticimage?width=280&height=140&zoom=13&center=" + locationY + "," + locationX + "&markers=" + locationY + "," + locationX + "&markerStyles=l,A";
                    note = lbspios.getNote();
                    link = webpath + "weix_lbsManager.hts?method=toMultiFixedMap&pioslat=" + locationX + "&pioslng=" + locationY + "&userlat=" + userlat + "&userlng=" + userlng + "&pid=" + lbspios.getPid();

                    // 添加一个图文
                    msgXml.append("<item>");
                    msgXml.append("<Title><![CDATA[").append(info).append("]]></Title>");
                    msgXml.append("<PicUrl><![CDATA[").append(initnullString(picUrl)).append("]]></PicUrl>");
                    msgXml.append("<Description><![CDATA[").append(note).append("]]></Description>");
                    msgXml.append("<Url><![CDATA[").append(initnullString(link)).append("]]></Url>");
                    msgXml.append("</item>");
                }

            }
            // 页面交互 或者 如果是多图文模式 并且启用了更多 ，那么该条图文进入的就是页面交互模式
            if (pushtype == 2 || (pushset.getAutomore() == 1 && pushtype == 1)) {
                if (pushtype == 2) {
                    info = "我的位置 ";
                    note = pushset.getNote();
                    picUrl = webpath + pushset.getImgurl();
                } else if (pushset.getAutomore() == 1) {
                    info = "获取更多 ";
                    note = "页面交互模式";
                    // 图片地址
                    picUrl = "http://api.map.baidu.com/staticimage?width=280&height=140&zoom=13&center=" + userlng + "," + userlat + "&markers=" + userlng + "," + userlat + "&markerStyles=l,A";
                }
                // 连接地址
                link = webpath + "weix_lbsManager.hts?method=toSingleFixedMap&userlat=" + userlat + "&userlng=" + userlng + "&aid=" + aid + "&corpcode=" + pushset.getCorpCode();
                msgXml.append("<item>");
                msgXml.append("<Title><![CDATA[").append(info).append("]]></Title>");
                msgXml.append("<PicUrl><![CDATA[").append(initnullString(picUrl)).append("]]></PicUrl>");
                msgXml.append("<Description><![CDATA[").append(note).append("]]></Description>");
                msgXml.append("<Url><![CDATA[").append(initnullString(link)).append("]]></Url>");
                msgXml.append("</item>");

            }
            msgXml.append("</Articles>");
            msgXml.append("</xml>");
        } catch (Exception e) {
            msgXml.setLength(0);
            EmpExecutionContext.error("LbsPiosBiz.createMsgXmlByLbsPois Is error");
        }
        return msgXml.toString();
    }

    /**
     * 空字符的处理
     *
     * @param o
     * @return1
     */
    private static String initnullString(String o) {
        boolean b = GlobalMethods.isInvalidString(o);
        return b ? "" : o.toString();
    }

    /**
     * @param lat 纬度
     * @param lng 经度
     * @return 物理地址 或者空字符
     * @description 通过经度纬度获取物理地址
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-18 下午05:24:21
     */
    public String getAddressBylatlng(String lat, String lng) {
        String returnmsg = "";
        try {
            String url = BaiduHttpUrl.GEOCODER_LOCATION_URL + "ak=" + WXStaticValue.BAIDU_MAP_AK + "&location=" + lat + "," + lng + "&output=json&pois=1";
            HttpReturnParams params = new HttpReturnParams();
            params.setUrl(url);
            params.setRequestType("GET");
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            params = new HttpRequestService().requestOttHttp(params);
            JSONObject object = params.getJsonObject();
            if (object != null && "0".equals(object.get("status").toString())) {
                JSONObject obj = (JSONObject) object.get("result");
                if (obj != null) {
                    returnmsg = (String) obj.get("formatted_address");
                }
            }
        } catch (Exception e) {
            returnmsg = "";
            EmpExecutionContext.error(e, "LbsPiosBiz.getAddressBylatlng Is error");
        }
        return returnmsg;
    }

}
