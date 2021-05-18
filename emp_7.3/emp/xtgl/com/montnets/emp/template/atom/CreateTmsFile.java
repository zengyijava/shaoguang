/**
 *
 */
package com.montnets.emp.template.atom;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.TxtFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liujianjun <ljr0300@163.com>
 * @project montnets_biz
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-2 下午02:18:43
 * @description
 */

public class CreateTmsFile {

    private String mmsSaveBasePath;

    public CreateTmsFile() {
    }

    public CreateTmsFile(String mmsSaveBasePath) {
        this.mmsSaveBasePath = mmsSaveBasePath;
    }

    TxtFileUtil txtfileutil = new TxtFileUtil();
    String dirUrl = txtfileutil.getWebRoot();

    /**
     * 创建TMS文件
     *
     * @param frameItems
     * @return
     */
    public boolean createMMSFile(List<FrameItem> frameItems) throws Exception {
        //初始化对象
        TmsFile tmsfile = new TmsFile();
        //循环 新增贞
        for (int i = 0; i < frameItems.size(); i++) {
            tmsfile.addFrame(frameItems.get(i));
        }
        //获取该TMS文件的字节
        byte bytes[] = tmsfile.getTmsFileBytes();
        File file = new File(mmsSaveBasePath);
        OutputStream out = null;
        //写文件
        try {
            out = new FileOutputStream(file);
            out.write(bytes);
            //关闭流
            out.close();
            return true;
        } catch (IOException e) {
            //异常
            EmpExecutionContext.error(e, "创建TMS文件 出现异常！");
            //返回BOOLEAN
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "创建TMS文件关闭字符输出流异常！");
                }
            }
        }

    }


    /**
     * 获取TMS文件的信息
     *
     * @param filePath
     * @return
     */
    public String getTmsFileInfo(String filePath) {

        String fileName;
        //获取该文件的全路径名
        filePath = dirUrl + filePath;
        //获取 TMS的前缀名称
        String tmsFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        ParseTmsFile parseTms = new ParseTmsFile();
        //获取相关信息列表
        List<TmsSmilItem> tmsSmilItem = parseTms.Parse(filePath);
        StringBuffer tmsInfo = null;
        //String[] tmsInfosArray = new String[tmsSmilItem.size()];
        try {
            if (tmsSmilItem == null) {
                return null;
            }
            //[{totaltime:''},{img:'',sound'',text:'',time:},{img:'',sound'',text:'',time:}....]
            //初始化字符流
            tmsInfo = new StringBuffer("{");
            List<TmsSmilParItem> parItemsList = null;
            Integer intDur = 0;
            String strNYR = new TxtFileUtil().getCurNYR();
            //循环每一桢
            for (int i = 0; i < tmsSmilItem.size(); i++) {
                parItemsList = tmsSmilItem.get(i).getParItemsList();
                TmsSmilParItem parItem = null;
                for (int j = 0; j < parItemsList.size(); j++) {
                    parItem = parItemsList.get(j);
                    //获取图片地址
                    String src = parItem.getSrc().substring(parItem.getSrc().lastIndexOf(":") + 1);
                    //创建文件夹
                    new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_PREVIEW + strNYR);
                    //获取文件名称
                    fileName = StaticValue.MMS_PREVIEW + strNYR + tmsFileName + "_" + src;
                    writeFile(fileName, parItem.getContent());
                    //获取文件的类型   图片
                    if (parItem.getType().equals("image")) {

                        tmsInfo = tmsInfo.append("\"img\":\"").append(fileName).append("\",");
                        //tmsInfosArray[i] = tmsInfo.toString();
                        //文本
                    } else if (parItem.getType().equals("text")) {
                        String text = new String(parItem.getContent(), 0, parItem.getContent().length, "utf-8");
                        //斜杠转义（与双引号转义位置不能颠倒）
                        text = text.replace("\\", "\\\\");
                        //双引号转义
                        text = text.replace("\"", "\\\"");
                        text = text.replace(">", "&gt;");
                        text = text.replace("<", "&lt;");//by liujj
                        tmsInfo = tmsInfo.append("\"text\":\"").append(text).append("\",");
                        //tmsInfosArray[i] = tmsInfo.toString();
                    } else {
                        //声音
                        tmsInfo = tmsInfo.append("\"sound\":\"").append(fileName).append("\",");

                    }
                }
                //时间
                String dur = tmsSmilItem.get(i).getDur().substring(0, tmsSmilItem.get(i).getDur().indexOf("ms"));
                tmsInfo = tmsInfo.append("\"time\":\"").append(dur).append("\"}");

                intDur += Integer.valueOf(dur);

                //tmsInfosArray[i] = tmsInfo.toString();
                if (i != tmsSmilItem.size() - 1) {
                    tmsInfo = tmsInfo.append(">{");
                }
            }
            //tmsInfo = tmsInfo.append("]");
            //总时间
            StringBuffer totaltime = new StringBuffer("{\"totaltime\":\"").append(intDur).append("\"}>");
            tmsInfo = totaltime.append(tmsInfo);
            //返回该TMS的信息
        } catch (Exception e) {

            EmpExecutionContext.error(e, "获取TMS文件的信息 出现异常！");
            return null;
        }
        return tmsInfo.toString();
    }

    /**
     * 获取彩信动态模板信息
     *
     * @param filePath
     * @return
     */
    public String getDynTmsFileInfo(String filePath, String paramContent) {

        String fileName;
        //获取该文件的全路径名
        filePath = dirUrl + filePath;
        //获取 TMS的前缀名称
        String tmsFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        ParseTmsFile parseTms = new ParseTmsFile();
        //获取相关信息列表
        List<TmsSmilItem> tmsSmilItem = parseTms.Parse(filePath);
        StringBuffer tmsInfo = null;
        String paramArr[] = paramContent.split("，");
        try {
            if (tmsSmilItem == null) {
                return null;
            }
            //初始化字符流
            tmsInfo = new StringBuffer("{");
            List<TmsSmilParItem> parItemsList = null;
            Integer intDur = 0;
            String strNYR = new TxtFileUtil().getCurNYR();
            //循环每一桢
            for (int i = 0; i < tmsSmilItem.size(); i++) {
                parItemsList = tmsSmilItem.get(i).getParItemsList();
                TmsSmilParItem parItem = null;
                for (int j = 0; j < parItemsList.size(); j++) {
                    parItem = parItemsList.get(j);
                    //获取图片地址
                    String src = parItem.getSrc().substring(parItem.getSrc().lastIndexOf(":") + 1);
                    new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_PREVIEW + strNYR);
                    //获取文件名称
                    fileName = StaticValue.MMS_PREVIEW + strNYR + tmsFileName + "_" + src;
                    writeFile(fileName, parItem.getContent());
                    //获取文件的类型   图片
                    if (parItem.getType().equals("image")) {

                        tmsInfo = tmsInfo.append("\"img\":\"").append(fileName).append("\",");
                        //tmsInfosArray[i] = tmsInfo.toString();
                        //文本
                    } else if (parItem.getType().equals("text")) {
                        String text = new String(parItem.getContent(), 0, parItem.getContent().length, "utf-8");
                        // 查找彩信内容中的参数个数
                        String eg = "#[pP]_[1-9][0-9]*#";
                        Matcher m = Pattern.compile(eg,
                                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
                                .matcher(text);
                        String rstr = "";
                        String rstr2 = "";
                        while (m.find()) {
                            rstr = m.group();
                            rstr2 = rstr.toUpperCase();
                            String pc = rstr2.substring(rstr2.indexOf("#P_") + 3,
                                    rstr2.lastIndexOf("#"));
                            int pci = Integer.parseInt(pc);
                            text = text.replace(rstr, paramArr[pci - 1]);
                        }

                        //斜杠转义（与双引号转义位置不能颠倒）
                        text = text.replace("\\", "\\\\");
                        //双引号转义
                        text = text.replace("\"", "\\\"");
                        text = text.replace(">", "&gt;");
                        text = text.replace("<", "&lt;");//by liujj
                        tmsInfo = tmsInfo.append("\"text\":\"").append(text).append("\",");
                        //tmsInfosArray[i] = tmsInfo.toString();
                    } else {
                        //声音
                        tmsInfo = tmsInfo.append("\"sound\":\"").append(fileName).append("\",");

                    }
                }
                //时间
                String dur = tmsSmilItem.get(i).getDur().substring(0, tmsSmilItem.get(i).getDur().indexOf("ms"));
                tmsInfo = tmsInfo.append("\"time\":\"").append(dur).append("\"}");

                intDur += Integer.valueOf(dur);

                //tmsInfosArray[i] = tmsInfo.toString();
                if (i != tmsSmilItem.size() - 1) {
                    tmsInfo = tmsInfo.append(">{");
                }
            }
            //tmsInfo = tmsInfo.append("]");
            //总时间
            StringBuffer totaltime = new StringBuffer("{\"totaltime\":\"").append(intDur).append("\"}>");
            tmsInfo = totaltime.append(tmsInfo);
            //返回该TMS的信息
        } catch (Exception e) {

            EmpExecutionContext.error(e, "获取彩信动态模板信息出现异常！");
            return null;
        }
        return tmsInfo.toString();
    }

    /**
     * 写文件
     *
     * @param filePath
     * @param contents
     * @return
     */
    private boolean writeFile(String filePath, byte[] contents) {

        boolean result = false;
        FileOutputStream fos = null;
        try {
            //获取全路径信息
            filePath = dirUrl + filePath;
            File file = new File(filePath);
            //判断是否存在文件
            if (!file.exists()) {
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            }
            //初始化流
            fos = new FileOutputStream(filePath);
            //输出信息
            fos.write(contents, 0, contents.length);
            //关闭流
            fos.close();
            result = true;
            return result;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "写文件出现异常！");
            return result;
            //异常
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "写文件关闭字符输出流异常！");
                }
            }
        }

    }

    /**
     * 获取tms文件的文本内容
     *
     * @param filePath
     * @return
     */
    public String getTmsText(String filePath) {
        //获取该文件的全路径名
        filePath = dirUrl + filePath;
        ParseTmsFile parseTms = new ParseTmsFile();
        //获取相关信息列表
        List<TmsSmilItem> tmsSmilItem = parseTms.Parse(filePath);
        StringBuffer tmsContent = new StringBuffer();
        try {
            if (tmsSmilItem == null) {
                return null;
            }
            List<TmsSmilParItem> parItemsList = null;
            //循环每一桢
            for (int i = 0; i < tmsSmilItem.size(); i++) {
                parItemsList = tmsSmilItem.get(i).getParItemsList();
                TmsSmilParItem parItem = null;
                for (int j = 0; j < parItemsList.size(); j++) {
                    parItem = parItemsList.get(j);
                    if (parItem.getType().equals("text")) {
                        String text = new String(parItem.getContent(), 0, parItem.getContent().length, "utf-8");
                        tmsContent.append(text);
                    }
                }

            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取tms文件的文本内容出现异常！");
            return null;
        }
        return tmsContent.toString();
    }
}
