package com.montnets.emp.apimanage.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;  
import org.jdom.Element;  
import org.jdom.JDOMException;  
import org.jdom.Namespace;  
import org.jdom.input.SAXBuilder;  
import org.xml.sax.InputSource;  

import com.montnets.emp.common.context.EmpExecutionContext;

public class DuXMLDoc {  
    public String xmlElements(String xmlDoc) { 
        //创建一个新的字符串  
        StringReader read = new StringReader(xmlDoc);  
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入  
        InputSource source = new InputSource(read);  
        //创建一个新的SAXBuilder  
        SAXBuilder sb = new SAXBuilder();
        String node="";
        
        String str="";
        try {  
            //通过输入源构造一个Document  
            Document doc = sb.build(source);  
            //取的根元素  
            Element root = doc.getRootElement();
            node=root.getName()+":1;";
            str=root.getName()+":1;";
            //级别，节点名
            //得到根元素所有子元素的集合  
            Element et = null;  
            List jiedian = root.getChildren(); 
            for(int i=0;i<jiedian.size();i++){
                et = (Element) jiedian.get(i);
              //没有子节点，就是0，有子节点就是1 格式为：当前节点：是否有子节点+;+父节点
                List zjiedian = et.getChildren();
                String flag1="0";
                if(zjiedian!=null&&zjiedian.size()>0){
                	flag1="1";
                }
                str=str+","+et.getName()+":"+flag1+";"+root.getName();
                
                for(int j=0;zjiedian!=null&&j<zjiedian.size();j++){ 
                    Element xet = (Element) zjiedian.get(j); 
                    List listall = xet.getChildren();
                    String flag2="0";
                    if(listall!=null&&listall.size()>0){
                    	flag2="1";
                    }
                    str=str+","+xet.getName()+":"+flag2+";"+et.getName();
                    if(listall!=null&&listall.size()>0){
                    	for(int n=0;n<listall.size();n++){ 
                    		Element last = (Element) listall.get(n);
                    		List lastit = last.getChildren();
                            String flag3="0";
                            if(lastit!=null&&lastit.size()>0){
                            	flag3="1";
                            }
                            str=str+","+last.getName()+":"+flag3+";"+xet.getName();

                    	}
                    }
                }
               
                
            }
            
//            for(int i=0;i<jiedian.size();i++){  
//                et = (Element) jiedian.get(i);
//                if(k>1){
//                	node=node+","+et.getName()+":1;"+root.getName();
//                }else{
//                	node=node+","+et.getName()+":0;"+root.getName();
//                }
//                
//                List zjiedian = et.getChildren();
//                for(int j=0;j<zjiedian.size();j++){  
//                    Element xet = (Element) zjiedian.get(j); 
//                    if(k>2){
//                    	node=node+","+xet.getName()+":1;"+et.getName();
//                    }else{
//                    	node=node+","+xet.getName()+":0;"+et.getName();
//                    }
//                    List listall = xet.getChildren();
//                    for(int n=0;n<listall.size();n++){  
//                        Element nextnode = (Element) listall.get(n);
//                        node=node+","+nextnode.getName()+":0;"+xet.getName();
//                    } 
//                }
//                
//            }  

              
 
        } catch (JDOMException e) {  
        	EmpExecutionContext.error(e,"解析xml异常！");
        } catch (IOException e) {  
            // TODO 自动生成 catch 块  
        	EmpExecutionContext.error(e,"解析xml异常！");
        } 
        return str;  
    }   
}  
