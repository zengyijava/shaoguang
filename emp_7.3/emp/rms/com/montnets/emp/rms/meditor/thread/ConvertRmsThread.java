package com.montnets.emp.rms.meditor.thread;


import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.meditor.entity.LfTempParam;
import com.montnets.emp.rms.meditor.entity.RmsV3Data;
import com.montnets.emp.rms.meditor.entity.RmsV3Element;
import com.montnets.emp.rms.meditor.tools.ParseV1ToV3Tool;
import com.montnets.emp.util.TxtFileUtil;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将1.0的富信转为3.0
 */
public class ConvertRmsThread implements Runnable {

    @Override
    public void run() {
        //获取数据库中的数据
        ParseV1ToV3Tool parseV1ToV3Tool = new ParseV1ToV3Tool();
        List<LfTemplate> lfTemplates = parseV1ToV3Tool.getV1Template();//获取V1.0的富信
        for(LfTemplate lfTemplate:lfTemplates){
            List<LfTempParam> paramList = new ArrayList<LfTempParam>();
            String allPath = lfTemplate.getTmMsg().substring(0,lfTemplate.getTmMsg().lastIndexOf("/"))+"/src/00.smil";
            String fileContent = getFileContent(allPath);
            if(fileContent == null){
                continue;
            }
            String tempContent = parseV3Rms(fileContent,allPath.substring(0,allPath.lastIndexOf("/")+1),lfTemplate.getTmid(),paramList);
            if(tempContent !=null){
                parseV1ToV3Tool.transTemplate(tempContent,paramList,lfTemplate);//存表
            }
        }
    }

    private List<LfTempParam> getTempParamList(List<LfTempParam> paramList,String paramText,Long tmId){
        List<String> textList = getParamList(paramText);
        for (String param : textList){
            LfTempParam lfTempParam = new LfTempParam();
            lfTempParam.setTmId(tmId);
            lfTempParam.setName(param);
            lfTempParam.setMaxLength(32);
            lfTempParam.setMinLength(0);
            lfTempParam.setType(4);
            lfTempParam.setLengthRestrict(0);
            lfTempParam.setFixLength(5);
            lfTempParam.setHasLength(1);
            lfTempParam.setRegContent("{c0-32}");
            paramList.add(lfTempParam);
        }
        return paramList;
    }


    private List<String> getParamList(String str) {
        String  regex = "#参数\\d+#";
        List<String> list = new ArrayList<String>();
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                String no = str.substring(matcher.start()+1, matcher.end()-1);
                list.add(no);
                str = str.replaceFirst(regex, "#P_" + no + "#");
                matcher = pattern.matcher(str);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
        }
        return list;
    }

    /**
     * 将V1转为V3
     * @param fileContent
     * @return
     */
    private  String parseV3Rms(String fileContent,String rootPath,Long tmId,List<LfTempParam> paramList){
        String v3Content = null;
        try{
            //解析文本内容中的src
            Map<String,String> srcMap = getSmilSrc(fileContent);
            //根据src拼接json
            v3Content = parseV3Json(srcMap,rootPath,tmId,paramList);
        }catch(Exception e){
            EmpExecutionContext.error(e, "将V3转为V1异常");
        }
        return v3Content;
    }

    private String parseV3Json(Map<String,String> srcMap,String rootPath,Long tmId, List<LfTempParam> paramList){
            List <RmsV3Data>list = new ArrayList<RmsV3Data>();
            for(int i=0;i<srcMap.size();i++){//每个map的值是01.txt##type&&02.jpg&&
                String srcName = srcMap.get("par"+(i+1));//获取srcName,
                String[] name = srcName.split("&&");
                RmsV3Data rmsV3Data = new RmsV3Data();
                for(int j=0;j<name.length;j++){
/*                    if(j == name.length-1){//最后一次循环直接退出
                        break;
                    }*/
                    if(j == 0){//第一个名字就是该帧的类型
                       String nameAndType = name[j];//01.txt##type
                       String type = nameAndType.split("##")[1];//取得类型
                        String src = nameAndType.split("##")[0];//获取路径
                        rmsV3Data.setTag(type+"_"+(j+1));
                        rmsV3Data.setType(type);
                        if("text".equals(type)){
                            //src = new TxtFileUtil().getWebRoot()+src;
                            rmsV3Data.setText(getFileContent(rootPath+src));
                            //给paramList赋值
                            getTempParamList(paramList,rmsV3Data.getText(),tmId);
                            //设置ParamText
                            setRmsDataParamText(paramList,rmsV3Data);
                            rmsV3Data.setImage(new RmsV3Element());
                            //rmsV3Data.setChart(new RmsV3Element());
                            rmsV3Data.setActive(false);
                            rmsV3Data.setSrc("");
                        }else if("image".equals(type)){
                            rmsV3Data.setSrc(rootPath+src);
                            setRmsDataV3Attr(rootPath+src,rmsV3Data);
                            rmsV3Data.setBorderRadius(0);
                            rmsV3Data.setTextEditable(new ArrayList());
                            setRmsDataSize(rootPath+src,rmsV3Data);
                            rmsV3Data.setActive(false);
                            rmsV3Data.setIsShowImgText("hide");
                            rmsV3Data.setText("");
                            rmsV3Data.setParamText("");
                        }else if("audio".equals(type)){
                            rmsV3Data.setDuration(10.128);
                            rmsV3Data.setActive(false);
                            setRmsDataSize(rootPath+src,rmsV3Data);
                            rmsV3Data.setSrc(rootPath+src);
                        }else if("video".equals(type)){
                            rmsV3Data.setHeight("");
                            rmsV3Data.setWidth("");
                            rmsV3Data.setActive(true);
                            setRmsDataSize(rootPath+src,rmsV3Data);
                            rmsV3Data.setSrc(rootPath+src);
                            setRmsVideoAttr(rootPath+src,rmsV3Data);
                        }
                    }else{
                        String src = name[j];//02.txt
                        if("text".equals(rmsV3Data.getType())){//如果类型是文本,则是文配图,需要设置src
                            RmsV3Element rmsV3Element = new RmsV3Element();
                            //rmsV3Element.setSrc(rootPath.replace(new TxtFileUtil().getWebRoot(),"")+src);
                            rmsV3Element.setSrc(rootPath+src);
                            //设置文本内图片的比例宽,高和真实宽高
                            setRmsV3Attr(rootPath+src,rmsV3Element);
                            rmsV3Data.setImage(rmsV3Element);
                        }else if("image".equals(rmsV3Data.getType())){
                            rmsV3Data.setText(getFileContent(rootPath+src));
                            getTempParamList(paramList,rmsV3Data.getText(),tmId);
                            //设置ParamText
                            setRmsDataParamText(paramList,rmsV3Data);
                        }
                    }
                }
                list.add(rmsV3Data);
            }
            return JSONObject.toJSONString(list);
    }
    /**
     * 设置RmsV3Data的宽和高属性
     * @param pathSrc
     * @param rmsV3Data
     * @return
     */
    public RmsV3Data setRmsDataSize(String pathSrc,RmsV3Data rmsV3Data){
        try {
            File file =  new File(new TxtFileUtil().getWebRoot()+pathSrc);
            rmsV3Data.setSize(String.valueOf(file.length()));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "设置RmsV3Data的 size 属性");
        }
        return rmsV3Data;
    }

    /**
     * 设置RmsV3Data的ParamText值
     * @param paramList
     * @param rmsV3Data
     */
    private void setRmsDataParamText(List<LfTempParam> paramList,RmsV3Data rmsV3Data){
        if(paramList.size()>0){//如果文本中含有参数
            StringBuffer paramText = new StringBuffer();
            for(LfTempParam lfTempParam:paramList){
                paramText.append("<input class=\"param-input J-add-param\" style=\"width: 64px;\" type=\"text\" readonly=\"readonly\" value=\"{#");
                paramText.append(lfTempParam.getName());
                paramText.append("#}\">");
            }
            rmsV3Data.setParamText(paramText.toString());
        }else{
            rmsV3Data.setParamText(rmsV3Data.getText());
        }
    }

    /**
     * 设置视频宽高
     * @param pathSrc
     * @param rmsV3Data
     * @return
     */
    public RmsV3Data setRmsVideoAttr(String pathSrc,RmsV3Data rmsV3Data){
        try {
            File videoFile =  new File(new TxtFileUtil().getWebRoot()+pathSrc);
            //读取图片对象
            Encoder encoder = new Encoder();
            MultimediaInfo m = encoder.getInfo(videoFile);

            int width =  m.getVideo().getSize().getWidth();
            int height = m.getVideo().getSize().getHeight();
            rmsV3Data.setHeight(String.valueOf(height));
            rmsV3Data.setWidth(String.valueOf(width));

            //设置比例高度
            rmsV3Data.setW(260);//宽固定
            double ratio = 260/(double)width;//设置比例
            rmsV3Data.setH((int)(height*ratio));//设置比例高度
        } catch (Exception e) {
            EmpExecutionContext.error(e, "设置RmsV3Data的宽和高属性");
        }
        return rmsV3Data;
    }


    /**
     * 设置RmsV3Data的宽和高属性
     * @param pathSrc
     * @param rmsV3Data
     * @return
     */
    private RmsV3Data setRmsDataV3Attr(String pathSrc,RmsV3Data rmsV3Data){
        try {
            File imgFile =  new File(new TxtFileUtil().getWebRoot()+pathSrc);
            //读取图片对象
            BufferedImage img = ImageIO.read(imgFile);
            //获得图片的宽
            int width = img.getWidth();
            //获得图片的高
            int height = img.getHeight();
            rmsV3Data.setHeight(String.valueOf(height));
            rmsV3Data.setWidth(String.valueOf(width));

            //设置比例高度
            rmsV3Data.setW(260);//宽固定
            double ratio = 260/(double)width;//设置比例
            rmsV3Data.setH((int)(height*ratio));//设置比例高度
        } catch (Exception e) {
            EmpExecutionContext.error(e, "设置RmsV3Data的宽和高属性");
        }
        return rmsV3Data;
    }

    /**
     * 设置RmsV3Element的宽和高属性
     * @param pathSrc
     * @param rmsV3Element
     * @return
     */
    private RmsV3Element setRmsV3Attr(String pathSrc,RmsV3Element rmsV3Element){
        try {
           File imgFile =  new File(new TxtFileUtil().getWebRoot()+pathSrc);
            //读取图片对象
            BufferedImage img = ImageIO.read(imgFile);
            //获得图片的宽
            int width = img.getWidth();
            //获得图片的高
            int height = img.getHeight();
            rmsV3Element.setHeight(String.valueOf(height));
            rmsV3Element.setWidth(String.valueOf(width));

            //设置比例高度
            rmsV3Element.setW(260);//宽固定
            double ratio = 260/(double)width;//设置比例
            rmsV3Element.setH((int)(height*ratio));//设置比例高度
        } catch (Exception e) {
            EmpExecutionContext.error(e, "设置RmsV3Element的宽和高属性");
        }
        return rmsV3Element;
    }

    /**
     * 获取smil中的src
     * @param fileContent
     * @return
     */
    private Map<String,String> getSmilSrc(String fileContent){
        Map<String,String> map = new HashMap<String,String>();
        try {
            Document doc=(Document)DocumentHelper.parseText(fileContent);
            Element books = doc.getRootElement();
            //获取根节点下的子节点body
            Iterator iterss = books.elementIterator("body");
            // 遍历body节点
            while (iterss.hasNext()) {
                Element recordEless = (Element) iterss.next();
                // 获取子节点body下的子节点par
                Iterator itersElIterator = recordEless.elementIterator("par");
                int num = 1;
                StringBuffer fileSrc = new StringBuffer();
                // 遍历Header节点下的Response节点
                while (itersElIterator.hasNext()) {
                    Element itemEle = (Element) itersElIterator.next();
                    // 获取子节点par下的各个节点
                    //类型的话在src后面再加一个值
                    Element text = itemEle.element("text");
                    if(text != null && text.attributeValue("src") !=null){
                        if(fileSrc.length() ==0){
                            fileSrc.append(text.attributeValue("src")+"##text"+"&&");
                        }else{
                            fileSrc.append(text.attributeValue("src")+"&&");
                        }
                    }
                    Element img = itemEle.element("img");
                    if(img != null && img.attributeValue("src") !=null){
                        if(fileSrc.length() ==0){
                            fileSrc.append(img.attributeValue("src")+"##image"+"&&");
                        }else{
                            fileSrc.append(img.attributeValue("src")+"&&");
                        }
                    }
                    Element video = itemEle.element("video");
                    if(video != null && video.attributeValue("src") !=null){
                        if(fileSrc.length() ==0){
                            fileSrc.append(video.attributeValue("src")+"##video"+"&&");
                        }else{
                            fileSrc.append(video.attributeValue("src")+"&&");
                        }
                    }
                    Element audio = itemEle.element("audio");
                    if(audio != null && audio.attributeValue("src") !=null){
                        if(fileSrc.length() ==0){
                            fileSrc.append(audio.attributeValue("src")+"##audio"+"&&");
                        }else{
                            fileSrc.append(audio.attributeValue("src")+"&&");
                        }
                    }
                    if (fileSrc.length()>0){
                        map.put("par"+num,fileSrc.toString());
                        fileSrc.setLength(0);//清空fileSrc
                        num++;
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取SRC失败");
        }
        return map;
    }

    /**
     * 替换参数字符串
     * @param str 替换前的字符串
     * @return 替换后字符串
     */
    private String replaceParam(String str) {
        String regex = "#P_\\d+#";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String no = str.substring(matcher.start() + 3, matcher.end() - 1);
            str = str.replaceFirst(regex, "{#参数" + no + "#}");
            matcher = pattern.matcher(str);
        }
        return str;
    }

    /**
     * 读取文本的内容
     * @param path
     * @return
     */
    private String getFileContent(String path){
        StringBuffer fileContent = new StringBuffer();
        BufferedReader bufferedReader = null;
        InputStreamReader isr = null;
        try{
            File file = new File(new TxtFileUtil().getWebRoot()+path);// 指定要读取的文件
            if (!file.exists()){
                return null;
            }
            // 获得该文件的缓冲输入流
            isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            bufferedReader = new BufferedReader(isr);
            String line = "";// 用来保存每次读取一行的内容
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line);
            }

        }catch (Exception e) {
            EmpExecutionContext.error(e, "读取本地文件异常");
        }finally {
            // 如果bufferedReader不是null，才需要close()
            if (bufferedReader != null) {
                // 为了保证close()一定会执行，就放到这里了
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "关闭流异常");
                }
            }
            if(isr != null){
            	try {
					isr.close();
				} catch (IOException e) {
					  EmpExecutionContext.error(e, "关闭流异常");
				}
            }

        }
        return  replaceParam(fileContent.toString());
    }

}
