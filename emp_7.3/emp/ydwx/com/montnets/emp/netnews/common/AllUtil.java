package com.montnets.emp.netnews.common;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import oracle.jdbc.OracleConnection;
import oracle.sql.CLOB;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 *
 * 类名称：AllUtil
 * 类描述：网讯中需要用的公共方法
 * 修改人：Administrator
 * 修改时间：2015-10-15 下午02:38:25
 * @version
 *
 */
public class AllUtil {

    private static final int iscluster = com.montnets.emp.common.constant.StaticValue.getISCLUSTER();

    /**
     * 获取配置文件信息
     *
     * @param str
     * @return
     */
    public static String getPropertyValue(String propertyName) {
        String value = ResourceBundle.getBundle(StaticValue.RESOURCE_PROPERTY).getString(propertyName);
        return value;
    }

    //替换超链接
    public static String link(String content) {
        int link_end = content.indexOf("My_Custom&#39;);");
        if (link_end > -1) {
            String[] link = content.split("My_Custom&#39;\\);");//link【0】得到 。。。。<a href="javascript:mylink(&quot;/netmsg/wx.nms?w=w7${h}&quot;,&quot;2055&quot;,&quot;

            int link_sta = link[0].lastIndexOf("javascript:mylink(");

            String[] jsln = link[0].split("javascript:mylink\\(");//jsln【1】得到&quot;/netmsg/wx.nms?w=w7${h}&quot;,&quot;2055&quot;,&quot;

            String varnk = jsln[1].replaceAll("&#39;", "");

            String _url = varnk.split(",")[0];
            //	/netmsg/wx.nms?w=2148

            if (_url.indexOf("w=") > -1) {
                String pageid = _url.split("w=")[1];
                boolean boo = isNomberNO(pageid);
                String jiam = "-1";
                if (boo) {
                    jiam = CompressEncodeing.CompressNumber(Long.parseLong(pageid), 6) + "-";
                }

                _url = _url.split("w=")[0] + "w=" + jiam + "${t}${h}";
            }

            content = content.replace(content.substring(link_sta, link_end + "My_Custom&#39;);".length()), _url);

            content = link(content);
        }
        return content;

    }

    /***
     * 视频路径处理的方法
     */
    public static String video(String content, String smsgpath, String iphORhe) {
        //***********用于处理分布式集群文件管理服务器增加的地址
        String viewurl = "";
        CommonBiz biz = new CommonBiz();
        String[] filePath = biz.getALiveFileServer();
        if (filePath != null && filePath.length > 1) {
            viewurl = filePath[1];
        }
        String newcontent = content;
        if (content.indexOf("<embed") > -1) {
            int first = content.indexOf("<embed");

            content = content.substring(first, content.length());

            int end = content.indexOf("/>") + 2 + first;
            content = newcontent.substring(first, end);

            String width = "";
            String height = "";
            String src = "";
            String[] st = content.split("\" ");
            if (st != null && st.length > 0) {
                for (int i = 0; i < st.length; i++) {
                    if (st[i].indexOf("width=\"") > -1) {
                        width = st[i].split("width=\"")[1];
                    }

                    if (st[i].indexOf("height=\"") > -1) {
                        height = st[i].split("height=\"")[1];
                    }

                    if (st[i].indexOf("src=\"") > -1) {
                        src = st[i].split("src=\"")[1];
                    }
                }

            }
            StringBuffer insertTxt = new StringBuffer();
            String videoType = null;
            if (src.split("\\.").length > 0) {
                videoType = src.substring(src.lastIndexOf(".") + 1);
                src = src.substring(0, src.lastIndexOf("."));
                //	src = src.split("\\.")[src.split("\\.").length-2];
                src = src.replace(smsgpath, "");
            }
            File filepng = null;
            File filebmp = null;
            if (iscluster == 1) {
                src = viewurl + src;//访问远程文件
                filepng = new File(viewurl + src + ".png");
                //new TxtFileUtil().getWebRoot()
                filebmp = new File(viewurl + src + ".bmp");
            } else {
                filepng = new File(new TxtFileUtil().getWebRoot() + src + ".png");
                filebmp = new File(new TxtFileUtil().getWebRoot() + src + ".bmp");
            }

            // 增加了视频播放路径  may
            if ("swf".equalsIgnoreCase(videoType)) {

                insertTxt.append("<div class=\"video-js-box\">");
                if ("1".equals(iphORhe)) {
                    if (filepng.exists()) { //判断网讯文件是否存在
                        insertTxt.append("<video id=\"example_video_1\" class=\"video-js\" width=\"320\"  src=\"" + src + ".m3u8\" height=\"240\" controls=\"controls\" preload=\"auto\" poster=\"" + src + ".png\" />");
                    } else if (filebmp.exists()) {
                        insertTxt.append("<video id=\"example_video_1\" class=\"video-js\" width=\"320\"  src=\"" + src + ".m3u8\" height=\"240\" controls=\"controls\" preload=\"auto\" poster=\"" + src + ".bmp\" />");
                    } else {
                        insertTxt.append("<video id=\"example_video_1\" class=\"video-js\" width=\"320\"  src=\"" + src + ".m3u8\" height=\"240\" controls=\"controls\" preload=\"auto\" poster=\"<%=path %>/ydwx/wap/video/vedio.png\" />");
                    }
                } else {
                    if (filepng.exists()) { //判断网讯文件是否存在
                        insertTxt.append("<video id=\"example_video_1\" class=\"video-js\" width=\"" + width + "\" height=\"" + height + "\"  controls=\"controls\" preload=\"auto\" poster=\"" + src + ".png\">");
                    } else if (filebmp.exists()) {
                        insertTxt.append("<video id=\"example_video_1\" class=\"video-js\" width=\"" + width + "\" height=\"" + height + "\"  controls=\"controls\" preload=\"auto\" poster=\"" + src + ".bmp\">");
                    } else {
                        insertTxt.append("<video id=\"example_video_1\" class=\"video-js\" width=\"" + width + "\" height=\"" + height + "\"  controls=\"controls\" preload=\"auto\" poster=\"<%=path %>/ydwx/wap/video/vedio.png\">");
                    }
                }

                insertTxt.append("<source src=\"" + src + "." + videoType + "\"  type='video/mp4; codecs=\"avc1.42E01E, mp4a.40.2\"' />");
                insertTxt.append("<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" width=\"" + width + "\" height=\"" + height + "\" id=\"Sample\" align=\"middle\">");
                insertTxt.append("<param name=\"movie\" value=\"" + src + ".swf\" />");
                insertTxt.append("<param name=\"quality\" value=\"high\" />");
                insertTxt.append("<param name=\"bgcolor\" value=\"#ffffff\" />");
                insertTxt.append("<param name=\"play\" value=\"true\" />");
                insertTxt.append("<param name=\"loop\" value=\"true\" />");
                insertTxt.append("<param name=\"wmode\" value=\"window\" />");
                insertTxt.append("<param name=\"scale\" value=\"showall\" />");
                insertTxt.append("<param name=\"menu\" value=\"true\" />");
                insertTxt.append("<param name=\"devicefont\" value=\"false\" />");
                insertTxt.append("<param name=\"salign\" value=\"\" />");
                insertTxt.append("<param name=\"allowScriptAccess\" value=\"sameDomain\" />");
                insertTxt.append("<!--[if !IE]>-->");
                insertTxt.append("<object type=\"application/x-shockwave-flash\" data=\"" + src + ".swf\" width=\"" + width + "\" height=\"" + height + "\">");
                insertTxt.append("<param name=\"movie\" value=\"" + src + ".swf\" />");
                insertTxt.append("<param name=\"quality\" value=\"high\" />");
                insertTxt.append("<param name=\"bgcolor\" value=\"#ffffff\" />");
                insertTxt.append("<param name=\"play\" value=\"true\" />");
                insertTxt.append("<param name=\"loop\" value=\"true\" />");
                insertTxt.append("<param name=\"wmode\" value=\"window\" />");
                insertTxt.append("<param name=\"scale\" value=\"showall\" />");
                insertTxt.append("<param name=\"menu\" value=\"true\" />");
                insertTxt.append("<param name=\"devicefont\" value=\"false\" />");
                insertTxt.append("<param name=\"salign\" value=\"\" />");
                insertTxt.append("<param name=\"allowScriptAccess\" value=\"sameDomain\" />");
                insertTxt.append("<!--<![endif]-->");
                insertTxt.append("<a href=\"http://www.adobe.com/go/getflash\">");
                insertTxt.append("<img src=\"http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif\" alt=\"获得 Adobe Flash Player\" />");
                insertTxt.append("</a>");
                insertTxt.append("<!--[if !IE]>-->");
                insertTxt.append("</object>");
                insertTxt.append("<!--<![endif]-->");
                insertTxt.append("</object>");
                insertTxt.append("</video>");
            } else if ("mp4".equalsIgnoreCase(videoType)) {

                // 利用第三方控件  官方网站：http://www.ckplayer.com/
                insertTxt.append("<script type=\"text/javascript\" src=\"<%=path%>/ydwx/wap/js/offlights.js\"></script>");
                insertTxt.append("<div id=\"video\" style=\"position:relative;width:180px;height:180px;\"><div id=\"a1\"></div></div>");
                insertTxt.append("<script type=\"text/javascript\" src=\"<%=path%>/ydwx/wap/ckplayer/ckplayer.js\" charset=\"utf-8\"></script>");
                insertTxt.append("<script type=\"text/javascript\">");
                insertTxt.append("var flashvars={");
                insertTxt.append("f:'" + src + ".mp4',");
                insertTxt.append("c:0,");
                insertTxt.append("b:1");
                insertTxt.append("};");
                insertTxt.append("var params={bgcolor:'#FFF',allowFullScreen:true,allowScriptAccess:'always'};");
                insertTxt.append("CKobject.embedSWF('<%=path%>/ydwx/wap/ckplayer/ckplayer.swf','a1','ckplayer_a1','160','180',flashvars,params);");
				/*
				CKobject.embedSWF(播放器路径,容器id,播放器id/name,播放器宽,播放器高,flashvars的值,其它定义也可省略);
				下面三行是调用html5播放器用到的
				*/
                insertTxt.append("var video=['" + src + ".mp4->video/mp4'];");
                insertTxt.append("var support=['iPad','iPhone','ios','android+false','msie10+false','webKit'];");
                insertTxt.append("CKobject.embedHTML5('video','ckplayer_a1',160,180,video,flashvars,support);");
                insertTxt.append("</script>");
            }
            //判断文件是否存在
            String showsrc = src + "." + videoType;
            //分布式处理
            if (iscluster == 1) {
                URL serverUrl;
                try {
                    serverUrl = new URL(showsrc);
                    try {
                        HttpURLConnection urlcon = (HttpURLConnection) serverUrl.openConnection();
                        String message = urlcon.getHeaderField(0);
                        // 文件存在=‘HTTP/1.1 200 OK’; 文件不存在 = ‘HTTP/1.1 404 Not Found’
                        if (!StringUtils.isEmpty(message) && message.startsWith("HTTP/1.1 404")) {
                            showsrc = "<%=basePath %>ydwx/wap/404.jsp";
                        }
                        //关闭连接
                        urlcon.disconnect();
                    } catch (IOException e) {
                        EmpExecutionContext.error(e, "网讯判断文件存在异常");
                    }

                } catch (MalformedURLException e) {
                    EmpExecutionContext.error(e, "网讯判断文件存在异常");
                }


            } else {
                String temp = new TxtFileUtil().getWebRoot() + showsrc;
                File file = new File(temp);
                if (!file.exists()) {
                    showsrc = "<%=basePath %>ydwx/wap/404.jsp";
                }
            }

            //这样处理，是为了防止weblogic乱码，使用其他插件的播放器
            if ("mp4".equalsIgnoreCase(videoType)) {
                newcontent = newcontent.replace(newcontent.substring(first, end), insertTxt.toString() + "<font size=\"+1\"><a href=\"javascript:showAudio('" + showsrc + "');\"><p>如无法观看,</p><p>请点击这里播放</p></a></font>");
            } else {
                newcontent = newcontent.replace(newcontent.substring(first, end), insertTxt.toString() + "<font size=\"+1\"><a href=\"javascript:showSWF('" + showsrc + "');\"><p>如无法观看,</p><p>请点击这里播放</p></a></font>");
            }


            newcontent = newcontent + "</div>";
            if (newcontent.indexOf("<embed") > -1) {
                newcontent = video(newcontent, smsgpath, iphORhe);
            }
        }
        return newcontent;
    }

    /**
     * 将UTF-8编码转换成ISO-8859-1
     *
     * @param str
     * @return
     */
    public static String uTF8toIOS8859(String str) {
        String st = null;
        try {
            st = new String(str.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            // Auto-generated catch block
            EmpExecutionContext.error(e, "编码转换异常！");
        }
        return st;
    }

    /**
     * 将ISO-8859-1编码转换成UTF-8
     *
     * @param str
     * @return
     */
    public static String IOS8859touTF8(String str) {
        String st = null;
        try {
            st = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Auto-generated catch block
            EmpExecutionContext.error(e, "字符串编码转换异常！");
        }
        return st;
    }

    public static String datetoString(Date date) {
        SimpleDateFormat formatDateTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return formatDateTime.format(date);
    }

    /**
     * Date 返回 String类型 yyyy-MM-dd HH:mm:ss格式
     *
     * @return String
     */
    public static Date Stringtodate(String date) {
        SimpleDateFormat formatDateTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        try {
            // date = (date.length()>22)?date.substring(0, 22):date;
            return formatDateTime.parse(date);
        } catch (ParseException e) {
            EmpExecutionContext.error(e, "日期转换异常！");
            return null;
        }

    }

    /**
     * Timestamp转化为String类型 日期格式：yyyy-MM-dd HH:mm:ss
     *
     * @param timeStamp Timestamp日期格式
     * @return String DateTime日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(Date timeStamp) {
        SimpleDateFormat formatDateTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String dateTime = "";
        if (timeStamp != null) {

            dateTime = formatDateTime.format(timeStamp);

        }

        return dateTime;

    }

    /**
     * Timestamp转化为String类型 日期格式：yyyy-MM-dd HH:mm:ss
     *
     * @param timeStamp Timestamp日期格式
     * @return String DateTime日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(Timestamp timeStamp) {
        SimpleDateFormat formatDateTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String dateTime = "";
        if (timeStamp != null) {

            dateTime = formatDateTime.format(timeStamp);

        }

        return dateTime;

    }

    /**
     * Timestamp转化为String类型 日期格式：yyyy-MM-dd
     *
     * @param timeStamp Timestamp日期格式
     * @return String DateTime日期格式：yyyy-MM-dd
     */
    public static String getDate(Timestamp timeStamp) {
        SimpleDateFormat formatDate = new SimpleDateFormat(
                "yyyy-MM-dd");
        String date = "";
        if (timeStamp != null) {

            date = formatDate.format(timeStamp);
        }

        return date;

    }

    /**
     * String转化为Timestamp类型
     *
     * @param date 日期字符串
     * @return Timestamp timeStamp类型
     */
    public static Timestamp getTimeStamp(String date) {


        Timestamp timeStamp = null;

        if (!"".equals(date)) {
            timeStamp = Timestamp.valueOf(date);
        }

        return timeStamp;

    }

    /**
     * 获取当前系统时间 日期格式：yyyy-MM-dd HH:mm:ss
     *
     * @return String 日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrDateTime() {
        SimpleDateFormat formatDateTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return formatDateTime.format(new Date());

    }

    /**
     * 获取当前系统时间 日期格式：yyyy-MM-dd
     *
     * @return String 日期格式：yyyy-MM-dd
     */
    public static String getCurrDate() {
        SimpleDateFormat formatDate = new SimpleDateFormat(
                "yyyy-MM-dd");
        return formatDate.format(new Date());

    }

    /**
     * 日期格式：yyyy-MM-dd
     *
     * @return String 日期格式：yyyy-MM-dd
     */
    public static String getdatoString(Date da) {
        SimpleDateFormat formatDate = new SimpleDateFormat(
                "yyyy-MM-dd");
        return formatDate.format(da);

    }

    /**
     * 日期格式：yyyy-MM-dd
     *
     * @return DATE 日期格式：yyyy-MM-dd
     */
    public static Date getStringtoda(String da) {
        SimpleDateFormat formatDate = new SimpleDateFormat(
                "yyyy-MM-dd");
        try {
            return formatDate.parse(da);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            EmpExecutionContext.error(e, "日期转换异常！");
        }
        return null;

    }

    /**
     * 获取当前系统时间 日期格式：yyyy-MM-dd HH:mm:ss
     *
     * @return Timestamp 日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static Timestamp getCurrTimestamp() {

        return getTimeStamp(getCurrDateTime());

    }

    /**
     * 判断时间大小,前者大于等于后者返回true，否则返回false
     *
     * @param date1 前者时间
     * @param date2 后者时间
     * @return boolean 前者大于等于后者返回true，否则返回false
     */
    public static boolean compareDateTime(Date date1, Date date2) {

        boolean dateFlag = true;

        if (date1.getTime() - date2.getTime() < 0) {

            return false;

        }

        return dateFlag;

    }


    /**
     * 字符串转换
     *
     * @param strSource  源字符串
     * @param strDefault 默认字符串
     * @return String 转换后字符串
     */
    public static String toStringValue(String strSource, String strDefault) {
        try {
            if (strSource == null) {
                return strDefault;
            } else if (strSource.trim().equals("")) {
                return strDefault;
            } else {
                return strSource.trim();
            }
        } catch (Exception ex) {
            EmpExecutionContext.error(ex, "字符串转换异常");
            ex.printStackTrace(System.out);
            return strSource.trim();
        }
    }

    /**
     * Clob转换成String
     *
     * @param clob
     * @return
     */
    public final static String clob2String(Clob clob) {
        if (clob == null) {
            return null;
        }

        StringBuffer sb = new StringBuffer(65535);//64K
        Reader clobStream = null;//创建一个输入流对象
        try {
            clobStream = clob.getCharacterStream();
            char[] b = new char[60000];//每次获取60K
            int i = 0;
            while ((i = clobStream.read(b)) != -1) {
                sb.append(b, 0, i);
            }
        } catch (Exception ex) {
            EmpExecutionContext.error(ex, "Clob转换成String异常");
            sb = null;
        } finally {
            try {
                if (clobStream != null) {
                    clobStream.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "关闭读入流失败");
            }
        }
        if (sb == null) {
            return null;
        } else {
            return sb.toString();
        }
    }

    /**
     * String转换成Clob
     *
     * @param clobData 字符串
     * @param conn     数据库链接
     * @return
     */
    public static CLOB String2Clob(String clobData, Connection conn) throws Exception {
        CLOB tempClob = null;
        try {
            tempClob = new CLOB((OracleConnection) conn);
            tempClob = CLOB.createTemporary(conn, false, CLOB.DURATION_SESSION);
            tempClob.open(CLOB.MODE_READWRITE);

            Writer tempClobWriter = tempClob.getCharacterOutputStream();
            tempClobWriter.write(clobData);
            tempClobWriter.flush();
            tempClobWriter.close();
            tempClob.close();
        } catch (Exception exp) {
            EmpExecutionContext.error(exp, "String转换成Clob失败");
            throw exp;
        }
        return tempClob;
    }

    /**
     * 获取全部拼音
     *
     * @param src 原字符串
     * @return
     */

    public static String getFull(String src) {

        char[] srcChar = src.toCharArray();
        String[] srcArry = new String[srcChar.length];
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

        // 设置格式  
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        String result = "";
        try {
            for (int i = 0; i < srcChar.length; i++) {
                // 判断是否为汉字字符  
                if (Character.toString(srcChar[i])
                        .matches("[\\u4E00-\\u9FA5]+")) {
                    srcArry = PinyinHelper.toHanyuPinyinStringArray(srcChar[i],
                            format);
                    result += srcArry[0];
                } else {
                    result += Character.toString(srcChar[i]);
                }
            }
            return result;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            EmpExecutionContext.error(e1, "字符转换异常！");
        }
        return result;
    }

    //手机号码验证
    public static boolean isMobileNO(String mobiles) {
//		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile("^(13|14|15|18)\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //数字验证   匹配浮点数
    public static boolean isNomberfolatNO(String nomber) {
        //^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$
        Pattern p = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$");
        Matcher m = p.matcher(nomber);
        return m.matches();
    }

    // 数字验证    匹配整数
    public static boolean isNomberNO(String nomber) {
        Pattern p = Pattern.compile("^-?[1-9]\\d*$");
        Matcher m = p.matcher(nomber);
        return m.matches();
    }

    //日期验证
    public static boolean isDataNO(String date) {
        //^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");

        Matcher m = pattern.matcher(date);
        return m.matches();
    }

    public static SimpleDateFormat getFormatDateTime() {
        SimpleDateFormat formatDateTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return formatDateTime;
    }


    public static SimpleDateFormat getFormatDate() {
        SimpleDateFormat formatDate = new SimpleDateFormat(
                "yyyy-MM-dd");
        return formatDate;
    }

    /**
     * 获取当前时间流
     *
     * @param dateFormat 日期格式：yyyy-MM-dd HH:mm:ss
     * @return String
     */
    public static String getDateStream(String dateFormat) {
        String result = "";
        try {
            SimpleDateFormat formatDateTime = new SimpleDateFormat(dateFormat);
            result = formatDateTime.format(new Date());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "日期转换异常！");
        }

        return result;
    }

    /**
     * @param @param  str
     * @param @return
     * @return String
     * @Description: sql语句的关键字的移除
     */
    public static String removeSQLStr(String str) {
        if (str == null || "".equals(str)) {
            return str;
        }
        String SQL_injdata = "'|exec|insert|select|delete|update|count|master|truncate|\\*";
        Pattern pattern = Pattern.compile(SQL_injdata, Pattern.CASE_INSENSITIVE);
        //要替换两次，因为有空格
        Matcher matcher = pattern.matcher(str);
        str = matcher.replaceAll("");
        matcher = pattern.matcher(str);
        str = matcher.replaceAll("");
        return str;
    }

    /***
     *
     * @Description: 处理当前日期加时间
     * @param @param i
     * @param @return
     * @return String
     */
    public static String addDayDate(int i) {
        SimpleDateFormat formatDateTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(cal.YEAR, i); // 当前时间加5分钟
        Date dd = cal.getTime();
        return formatDateTime.format(dd);

    }

    /***
     *
     * @Description: 处理当前日期加月
     * @param @param i
     * @param @return
     * @return String
     */
    public static String addMonth(int i) {
        SimpleDateFormat formatDateTime = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(cal.MONTH, i); // 当前时间加5分钟
        Date dd = cal.getTime();
        return formatDateTime.format(dd);

    }

    /**
     * 获取加密对象
     *
     * @param request
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-10-26 下午07:29:28
     */
    public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request) {
        try {
            ParamsEncryptOrDecrypt encryptOrDecrypt = null;
            //加密对象
            Object encrypOrDecrypttobject = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("paramsEncryptOrDecrypt");
            //加密对象不为空
            if (encrypOrDecrypttobject != null) {
                //强转类型
                encryptOrDecrypt = (ParamsEncryptOrDecrypt) encrypOrDecrypttobject;
            } else {
                EmpExecutionContext.error("从session获取加密对象为空。");
            }
            return encryptOrDecrypt;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "从session获取加密对象异常。");
            return null;
        }
    }

}
