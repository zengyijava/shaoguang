package com.montnets.emp.apimanage.util;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * 格式化工具
 * @author Administrator
 *
 */
public class FormatUtil {
	/**
	 * JSON格式化
	 * @param jsonStr
	 * @return
	 * @author   lizhgb
	 * @Date   2015-10-14 下午1:17:35
	 */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }
        /**
         * 添加space
         * @param sb
         * @param indent
         * @author   lizhgb
         * @Date   2015-10-14 上午10:38:04
         */
        private static void addIndentBlank(StringBuilder sb, int indent) {
            for (int i = 0; i < indent; i++) {
                sb.append('\t');
            }
        }
        
        /**
         * 格式化xml格式
         * @param str
         * @return
         */
        public static String XMLformat(String str) {
            // 注释：创建输出(目标)
            StringWriter out = new StringWriter();
			try {
            SAXReader reader = new SAXReader();
            // System.out.println(reader);
            // 注释：创建一个串的字符输入流
            StringReader in = new StringReader(str);
            Document doc = reader.read(in);
            // System.out.println(doc.getRootElement());
            // 注释：创建输出格式
            OutputFormat formater = OutputFormat.createPrettyPrint();
            //formater=OutputFormat.createCompactFormat();
            // 注释：创建输出流
            XMLWriter writer = new XMLWriter(out, formater);
            // 注释：输出格式化的串到目标中，执行后。格式化后的串保存在out中。
            writer.write(doc);

            writer.close();
			} catch (Exception e) {
			}
            // 注释：返回我们格式化后的结果
            return out.toString();
        }
        
     
        //如果存在子节点，比如：格式为MOsuccesMap:1;,result:0;MOsuccesMap,msgid:0;MOsuccesMap
        //MOsuccesMap为父节点，值为1，如果其他节点为子节点就是result:0;MOsuccesMap
        public String  getJsonNode(String str) {
        	//按照","将json字符串分割成String数组
        	String node="";
        	String[] keyValue = str.split(",");
        	str=str.replaceAll("/t", "");
			String mainNode="";//父节点名称
        	for(int i=0; i<keyValue.length; i++) {
        		String s = keyValue[i];
        		//找到":"所在的位置，然后截取
        		int index = s.indexOf(":");
        		//第一个字符串因带有json的"{"，需要特殊处理
        		if(i==0) {
        			String[] arr= s.split(":");
        			if(arr.length==2){
        				//截取：之前的部分，有的时候是数组（[{}]），有的时候不是数组({})
        				node=s.substring(s.indexOf("{")+1, index-1).replaceAll("\n","").replaceAll("\\s","")+node+":0;";//是否有下级节点，例如：0无子节点，1有下级子节点
        			}else if(arr.length>2){
            			node=node+arr[0].substring(s.indexOf("{")+1, index-1).replaceAll("\n","").replaceAll("\\s","")+":1;";
            			node=node+","+arr[1].substring(arr[1].indexOf("{")+1, arr[1].length()-1).replaceAll("\n","").replaceAll("\\s","")+":0;"+arr[0].substring(2, index-1).replaceAll("\n","").replaceAll("\\s","");
    					mainNode=arr[0].substring(s.indexOf("{")+1, index-1).replaceAll("\n","").replaceAll("\\s","");
        			}
        			
        		} else {
        			String temp=s.replaceAll("\\s","");

        			if(temp.indexOf("[")>-1){
        				String[] list= temp.split("\\[");
        				if(list.length==2){
        					node=node+","+list[0].substring(0, list[0].indexOf(":")).replaceAll("\n","").replaceAll("\\s","")+":1;";//是否有下级节点，例如：0无子节点，1有下级子节点
        					if(list[1].indexOf(":")==-1){
        						continue;
        					}
        					node=node+","+list[1].substring(list[1].indexOf("{")+1, list[1].indexOf(":")).replaceAll("\n","").replaceAll("\\s","")+":0;"+list[0].substring(0, list[0].indexOf(":")).replaceAll("\n","").replaceAll("\\s","");
        					mainNode=list[0].substring(0, list[0].indexOf(":")).replaceAll("\n","").replaceAll("\\s","");
        				}
        			}else{
        				//防止通过拆分之后，出现用户自己输入的不符合要求的格式。
        				if(s.indexOf(":")>-1){	
        					s=s.replaceAll("\\s","");
        					node=node+","+s.substring(1, s.indexOf(":")-1).replaceAll("\n","").replaceAll("\\s","")+":0;"+mainNode;
        				}
            			
        			}

        			
        			
        		}
        	}
        	node=node.replaceAll("\"", "");
            return node;
        }
        
    	/** 
    	* 判断是否是URLencode结构 
    	*/ 
    	public  String getURLencodeNode(String value) { 
    		String node="";
    		Set<String> dbSet = new HashSet<String>();
    		String[]  array=value.split("&");
    		for(int k=0;k<array.length;k++){
    			String every=array[k];
    			String[] keyvalue= every.split("=");
    			
    			if(k==0){
    				node=keyvalue[0]+node;
    				dbSet.add(keyvalue[0]);
    			}else{
    				if(dbSet.contains(keyvalue[0])){
    					continue;
    				}
    				dbSet.add(keyvalue[0]);
    				node=node+","+keyvalue[0];
    				
    			}
    		}
    		
    		return node; 
    	}
        

}
