package com.montnets.emp.rms.meditor.tools;

/**
 * 校验文件真实类型工具类
 * 读取二进制流文件的文件头，根据头标识来进行文件类型的判断
 */

import java.util.HashMap;
import java.util.Map;

public class FileTypeTool {

    protected final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

    private FileTypeTool(){}
    static{
        getAllFileType(); //初始化文件类型信息
    }

    /**
     * Discription:[getAllFileType,常见文件头信息]
     */
    private static void getAllFileType()
    {
        FILE_TYPE_MAP.put("FFD8FFE0", "jpg");
        FILE_TYPE_MAP.put("89504E47", "png");
        FILE_TYPE_MAP.put("47494638", "gif");
        FILE_TYPE_MAP.put("49492A00", "tif");
        FILE_TYPE_MAP.put("424D", "bmp");
        FILE_TYPE_MAP.put("41433130", "dwg"); // CAD
        FILE_TYPE_MAP.put("38425053", "psd");
        FILE_TYPE_MAP.put("7B5C727466", "rtf"); // 日记本
        FILE_TYPE_MAP.put("3C3F786D6C", "xml");
        FILE_TYPE_MAP.put("68746D6C3E", "html");
        FILE_TYPE_MAP.put("44656C69766572792D646174653A", "eml"); // 邮件
        FILE_TYPE_MAP.put("D0CF11E0", "doc");
        FILE_TYPE_MAP.put("5374616E64617264204A", "mdb");
        FILE_TYPE_MAP.put("252150532D41646F6265", "ps");
        FILE_TYPE_MAP.put("255044462D312E", "pdf");
        FILE_TYPE_MAP.put("504B0304", "docx");
        FILE_TYPE_MAP.put("52617221", "rar");
        FILE_TYPE_MAP.put("57415645", "wav");
        FILE_TYPE_MAP.put("52494646", "wave");
        FILE_TYPE_MAP.put("00000020", "mp4");
        FILE_TYPE_MAP.put("41564920", "avi");
        FILE_TYPE_MAP.put("2E524D46", "rm");
        FILE_TYPE_MAP.put("000001BA", "mpg");
        FILE_TYPE_MAP.put("000001B3", "mpg");
        FILE_TYPE_MAP.put("6D6F6F76", "mov");
        FILE_TYPE_MAP.put("3026B2758E66CF11", "asf");
        FILE_TYPE_MAP.put("4D546864", "mid");
        FILE_TYPE_MAP.put("1F8B08", "gz");
        FILE_TYPE_MAP.put("4D5A9000", "exe/dll");
        FILE_TYPE_MAP.put("75736167", "txt");
    }

    /**
     * 根据文件路径获取文件头信息
     *
     * @param  b
     *      文件路径
     * @return 文件头信息
     */
    public static String getFileType(byte[] b) {
        String bytes = getFileHeader(b);
// System.out.println("--"+bytes);
        if (bytes != null) {
            if (bytes.contains("FFD8FF")) {
                return "jpg";
            } else if (bytes.contains("89504E47")) {
                return "png";
            } else if (bytes.contains("47494638")) {
                return "gif";
            } else if (bytes.contains("49492A00")) {
                return "tif";
            } else if (bytes.contains("424D")) {
                return "bmp";
            } else if (bytes.contains("FFFB900C") || bytes.contains("FFF14C") || bytes.contains("49443303")) {
                //[ '.mp3','.aac', '.m4a', '.wma'],
                return "mp3";
            }else if (bytes.contains("000000")) {
                //['.wmv', '.3gp', '.avi', '.f4v', '.m4v', '.mp4', '.mpg', '.ogv', '.swf', '.vob']
                return "mp4";
            } else if (bytes.contains("52494646")) {
                return "wav";
            } else if (bytes.contains("4F676753")) {
                return "ogg";
            }
        }
        return null;
    }
    /**
     * 根据文件路径获取文件头信息
     *
     *      文件路径
     * @return 文件头信息
     */
    public static String getFileHeader(byte[] b) {
            return bytesToHexString(b);

    }
    /**
     * 将要读取文件头信息的文件的byte数组转换成string类型表示
     *
     * @param src
     *      要读取文件头信息的文件的byte数组
     * @return 文件头信息
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv = "";
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

}
