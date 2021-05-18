package com.montnets.emp.rms.meditor.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: RegParamTool 
 * @Description: 根据规则生成正则参数格式
 * @author xuty
 * @date 2018-8-9下午3:55:01
 */
public class RegParamTool {
	
	//1.参数类型  paramType
	//2.是否定长
	//2.1 定长--> 固定长度
	//2.2 不定长 --> 最小长度 、最大长度

    /**
     *
     * @param type        参数类型
     * @param lengthRestrict 是否定长
     * @param minLenth    最小长度
     * @param maxLength   最大长度
     * @return
     */
    public static String parseParam2Reg(int type,int lengthRestrict,int minLenth,int maxLength,int fixLength){

    /*
    *
    * 参数类型有：type

    1、纯数字（d）{d}

    2、字母数字（w）

    3、金额（$）

    4、任意字符（c）

    5、日期（YYYY-MM-DD）

    6、日期（YYYY/MM/DD）

    7、 日期（MM-DD）

    8、日期（MM/DD）

    9、日期时间（YYYY-MM-DD hh：mm：ss）

    10、日期时间（YYYY/MM/DD hh：mm：ss）

    11、日期时间（MM-DD hh：mm：ss）

    12、日期时间（MM/DD hh：mm：ss）

    13、时间（hh：mm：ss）

    14、任意字符-不支持中文（z）

    15、单词字符（b）
    *
    * int type,boolean isFixLength,int minLenth,int maxLength,int fixLength
    * */
        StringBuffer sub = new StringBuffer("{");
    boolean isFixLength = false;
    if(lengthRestrict == 1){//定长
        isFixLength = true;
    }

       switch (type){//参数类型
           case 1://纯数字 d
               if(isFixLength){//定长
                   sub.append("d").append(fixLength);
               }else{//不定长
                   if(minLenth >= maxLength){
                       sub = new StringBuffer("");
                       sub.append("最小长度不能超过最大长度");
                       return sub.toString();
                   }
                   sub.append("d").append(minLenth).append("-").append(maxLength);
               }
               break;
           case 2://字母数字 w
               if(isFixLength){//定长
                   sub.append("w").append(fixLength);
               }else{//不定长
                   if(minLenth >= maxLength){
                       sub = new StringBuffer("");
                       sub.append("最小长度不能超过最大长度");
                       return sub.toString();
                   }
                   sub.append("w").append(minLenth).append("-").append(maxLength);
               }
               break;
           case 3://金额 $
               if(isFixLength){//定长
                   sub.append("$").append(fixLength);
               }else{//不定长
                   if(minLenth >= maxLength){
                       sub = new StringBuffer("");
                       sub.append("最小长度不能超过最大长度");
                       return sub.toString();
                   }
                   sub.append("$").append(minLenth).append("-").append(maxLength);
               }
               break;
           case 4://任意字符（c）
               if(isFixLength){//定长
                   sub.append("c").append(fixLength);
               }else{//不定长
                   if(minLenth >= maxLength){
                       sub = new StringBuffer("");
                       sub.append("最小长度不能超过最大长度");
                       return sub.toString();
                   }
                   sub.append("c").append(minLenth).append("-").append(maxLength);
               }
               break;
           case 5://日期（YYYY-MM-DD）
               sub.append("YYYY-MM-DD");
               break;
           case 6://日期（YYYY/MM/DD）
               sub.append("YYYY/MM/DD");
               break;
           case 7://日期（YYYY-MM-DD）
               sub.append("MM-DD");
               break;
           case 8://日期（MM/DD）
               sub.append("MM/DD");
               break;
           case 9://日期时间（YYYY-MM-DD hh：mm：ss）
               sub.append("YYYY-MM-DD hh：mm：ss");
               break;
           case 10://日期时间（YYYY/MM/DD hh：mm：ss）
               sub.append("YYYY/MM/DD hh：mm：ss");
               break;
           case 11://日期时间（MM-DD hh：mm：ss）
               sub.append("MM-DD hh：mm：ss");
               break;
           case 12://日期时间（MM/DD hh：mm：ss）
               sub.append("MM/DD hh：mm：ss");
               break;
           case 13://时间（hh：mm：ss）
               sub.append("hh：mm：ss");
               break;
           case 14://任意字符-不支持中文（z）
               if(isFixLength){//定长
                   sub.append("z").append(fixLength);
               }else{//不定长
                   if(minLenth >= maxLength){
                       sub = new StringBuffer("");
                       sub.append("最小长度不能超过最大长度");
                       return sub.toString();
                   }
                   sub.append("z").append(minLenth).append("-").append(maxLength);
               }
               break;
       }
        sub.append("}");
        return sub.toString();
    }

    /**
     * 替换参数字符串
     * @param str 替换前的字符串
     * @return 替换后字符串
     */
    public static String replaceParam(String str) {
        String regex = "\\{#参数\\d+#\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String no = str.substring(matcher.start() + 4, matcher.end() - 2);
            str = str.replaceFirst(regex, "#P_" + no + "#");
            matcher = pattern.matcher(str);
        }
        return str;
    }

    public static void main(String[] args) {
        System.out.println(parseParam2Reg(14,1,5,0,6));
    }
}
