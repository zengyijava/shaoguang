package com.montnets.emp.ottbase.util;

import com.montnets.emp.common.context.EmpExecutionContext;
import it.sauronsoftware.jave.*;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

public class WavFileUtil
{

    private static String webRoot = "";
    
    static
    {
        webRoot = new TxtFileUtil().getWebRoot();
    }
    
    public static int getLength(String fileName)
    {
        try
        {
            File ff = new File(webRoot+fileName);
            long fsize = ff.length();
            
            AudioInputStream ain = AudioSystem.getAudioInputStream(ff);
            
            AudioFormat format=ain.getFormat();
            
            float rate = format.getFrameRate();
            
            int size = format.getFrameSize();
            
            ain.close();
            Float count = Float.valueOf(fsize)/Float.valueOf(size);
            Float second = count/rate;
            return (int) Math.round(second);
            
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "发现异常！");
        }
        return 0;
    }
    
    public static void playWav(String fileName,int second) throws Exception
    {
        InputStream in = new FileInputStream(webRoot+fileName);
        // 从输入流中创建一个AudioStream对象
        AudioStream as = new AudioStream(in);
        AudioPlayer.player.start(as);// 用静态成员player.start播放音乐
        Thread.sleep(second * 1000L);
    }
    /** 
     * 得到amr的时长 
     *  
     * @param filename 
     * @return 
     * @throws IOException 
     */  
    public static Integer getAmrDuration(String filename) throws IOException {  
    	File file = new File(webRoot+filename);
        long duration = -1;  
        int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };  
        RandomAccessFile randomAccessFile = null;  
        try {  
            randomAccessFile = new RandomAccessFile(file, "rw");  
            long length = file.length();//文件的长度  
            int pos = 6;//设置初始位置  
            int frameCount = 0;//初始帧数  
            int packedPos = -1;  
            /////////////////////////////////////////////////////  
            byte[] datas = new byte[1];//初始数据值  
            while (pos <= length) {  
                randomAccessFile.seek(pos);  
                if (randomAccessFile.read(datas, 0, 1) != 1) {  
                    duration = length > 0 ? ((length - 6) / 650) : 0;  
                    break;  
                }  
                packedPos = (datas[0] >> 3) & 0x0F;  
                pos += packedSize[packedPos] + 1;  
                frameCount++;  
            }  
            /////////////////////////////////////////////////////  
            duration += frameCount * 20;//帧数*20  
        } finally {  
            if (randomAccessFile != null) {  
                randomAccessFile.close();  
            }  
        }
        Integer second = Math.round(duration/1000f);
        return second==0?1:second;  
    }  
    /**
     * 转换AMR为WAV
     * @description    
     * @param fileName
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-17 下午04:50:57
     */
    public static String converToWav(String fileName)
    {
        File source = new File(webRoot + fileName);
        StringBuffer ssb = new StringBuffer(fileName);
        String resultFile = ssb.replace(fileName.lastIndexOf("."),fileName.length(),".wav").toString();
        File target = new File(webRoot + resultFile);
        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();
        
        audio.setCodec("pcm_s16le");
        //单通道3000频率
        audio.setChannels(new Integer(1));
        audio.setSamplingRate(new Integer(3000));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("wav");
        attrs.setAudioAttributes(audio);
        
        try
        {
            encoder.encode(source, target, attrs);
        }
        catch (EncoderException e)
        {
            // 不做处理
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"转换wav文件失败");
        }
        return resultFile;
    }
    
    
    /**
     * 转换AMR为WAV
     * @description    
     * @param fileName
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-17 下午04:50:57
     */
    public static void converToWav2(File target,File source)
    {
        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();
        
        audio.setCodec("libmp3lame");
        //双通道3000频率
        audio.setChannels(new Integer(1));
        audio.setSamplingRate(new Integer(3000));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);
        
        try
        {
            encoder.encode(source, target, attrs);
        }
        catch (EncoderException e)
        {
            // 不做处理
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"转换wav文件失败");
        }
    }
    
    /**
     * 转换AMR为WAV
     * @description    
     * @param fileName
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-17 下午04:50:57
     */
    public static void converToWav3(File target,File source)
    {
        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();
        
        audio.setCodec("pcm_s16be");
        //双通道3000频率
        audio.setChannels(new Integer(1));
        audio.setSamplingRate(new Integer(3000));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("au");
        attrs.setAudioAttributes(audio);
        
        try
        {
            encoder.encode(source, target, attrs);
        }
        catch (EncoderException e)
        {
            // 不做处理
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"转换wav文件失败");
        }
    }
    
    /**
     * 转换AMR为WAV
     * @description    
     * @param fileName
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-17 下午04:50:57
     */
    public static void converToWav4(File target,File source)
    {
        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();
        
        audio.setCodec("pcm_s32le");
        //双通道3000频率
        audio.setChannels(new Integer(1));
        audio.setSamplingRate(new Integer(3000));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("wav");
        attrs.setAudioAttributes(audio);
        
        try
        {
            encoder.encode(source, target, attrs);
        }
        catch (EncoderException e)
        {
            // 不做处理
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"转换wav文件失败");
        }
    }
    
    /**
     * 修改MP4视频参数
     * @description    
     * @throws IllegalArgumentException
     * @throws InputFormatException
     * @throws EncoderException       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-6-10 下午05:10:18
     */
    public static boolean changeMP4(String souceFile,String resultFile) 
    {
        File source = new File(souceFile);
        File target = new File(resultFile);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libfaac");
        audio.setBitRate(new Integer(128000));
        audio.setSamplingRate(new Integer(44100));
        audio.setChannels(new Integer(1));
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setBitRate(new Integer(160000));
        video.setFrameRate(new Integer(15));
        video.setSize(new VideoSize(180,320));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        try
        {
            encoder.encode(source, target, attrs);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"修改MP4视频参数失败");
            return false;
        }
        return true;
    }
}
