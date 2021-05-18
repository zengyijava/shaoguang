package com.montnets.emp.template.atom;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.montnets.emp.common.context.EmpExecutionContext;

public class ParseTmsFile
{
    
    private List<TmsFileItem> fileItemsList; 
    private Document doc = null;

    
	/**
	 * @param filePath
	 * @return
	 */
	public List<TmsSmilItem> Parse(String filePath)
    {
		//boolean result = true;
		InputStream in = null;
		List<TmsSmilItem> smilItemsList = null;
        try
        {
            if( !new File(filePath).exists() )
            {
                throw new IOException("TMS文件不存在！");
            }
            in = new FileInputStream(filePath);
            byte[] bytes = new byte[1024*1024];
            int c=in.read(bytes);
             
            int index = 0;
            if( bytes[0] != (byte)161 || bytes[1] != (byte)100 )
            {
                throw new IOException("文件不是可以识别的版本格式！");
            }

            
            index += 2;
/*            int totalLength = (int)(bytes[index + 3] | bytes[index + 2] << 8 | bytes[index + 1] << 16 | bytes[index] << 24);
            System.out.println(bytes.length);
            if( totalLength != bytes.length )
            {
                throw new IOException(String.format("文件总长度不正确！{0:X}!={1}", totalLength, bytes.length));
            }*/

             index += 4;
            int fileCount = (int)bytes[index];
            if( fileCount <= 0 )
            {
                throw new IOException("tms包含的文件数量不能少于等于0");
            }

            this.fileItemsList = new ArrayList<TmsFileItem>(fileCount);

            index += 1;
            //TODO
            //int titleLength = (int)(bytes[index + 1] | bytes[index] << 8);
            int titleLength = (int)(bytes[index + 1]& 0xff | bytes[index] << 8);
            
            
            index += 2;

            index += titleLength;

            for( int i = 0; i < fileCount; i++ )
            {
               
                int fileLength = (int)bytes[index];
                index += 1;

                 
                String fileName = new String(bytes, index, fileLength, "utf-8");
                index += fileLength;

                
                int fileContentLength=0;
                int index1Value=bytes[index+1]<0?(256+bytes[index + 1]):bytes[index+1];
                int index2Value=bytes[index+2]<0?(256+bytes[index + 2]):bytes[index+2];
                fileContentLength = (int)(bytes[index] << 16 | index1Value << 8 | index2Value);
                index += 3;
                
                byte[] content = new byte[fileContentLength];
                System.arraycopy(bytes, index, content, 0, fileContentLength);
                index += fileContentLength;

                TmsFileItem tmsItem = new TmsFileItem();
                tmsItem.setFileName(fileName);
                tmsItem.setContent(content);
                fileItemsList.add(tmsItem);
                
/*                if(i == 0){
                	List<TmsSmilItem> tmsSmil = this.viewXML(content);
                }*/
                
            }
            
            int count = 0;
            for(int i = 0; i < fileItemsList.size();i++){
            	String tempFileName = fileItemsList.get(i).getFileName();
            	if(tempFileName.substring(tempFileName.lastIndexOf(".")).equals(".smil")){
            		count++;
            	}
            }
            
            if( count != 1 )
            {
                throw new Exception("tms文件中必须包含一个smil文件，并且只能包含一个smil文件！");
            }

            smilItemsList = this.viewXML(fileItemsList.get(0).getContent(), fileItemsList);
            
            
           
            return smilItemsList;  
        }
        catch( Exception e )
        {
        	EmpExecutionContext.error(e,"解析tms文件出现异常！");
        	 return smilItemsList;  
        }finally{
			if(in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析tms文件关闭字符输出流异常！");
				}
			}
		}

                 
    }



	
	
 /*   private void CheckFiles(TmsFileItem smilFile)
    {
    	
    	SAXParserFactory sf = SAXParserFactory.newInstance(); 
    	SAXParser sp = sf.newSAXParser(); 
    	MyXMLReader reader = new MyXMLReader(); 
    	sp.parse(f, dh)
    	
        using (StreamReader reader = new StreamReader(new MemoryStream(smilFile.Content)))
        {
            System.Xml.XmlDocument dodd = new System.Xml.XmlDocument();
            dodd.Load(reader);
            XDocument doc = XDocument.Load(reader);
            IEnumerable<XElement> elements = doc.Descendants("par");
            if( elements == null || elements.Count() == 0 )
            {
                throw new TmsFileException("彩信文件未包含帧！");
            }

            if( elements.Count() > 10 )
            {
                throw new TmsFileException("彩信文件的帧不能超过10个！");
            }

            foreach( XElement el in elements )
            {
                var nodes = el.Elements();
                foreach( var e in nodes )
                {
                    string src = e.Attribute("src").Value;
                    if( src.StartsWith("cid:") )
                    {
                        src = src.Substring(4);
                    }

                    if( !FileItems.Any(f => f.FileName == src) )
                    {
                        throw new TmsFileException(String.Format("smil文件中需要资源{0},但tms包中没有该文件！",src));
                    }
                }
            }
        }
        
    }*/

    
	/**
	 * @param bytesArray
	 * @throws Exception
	 */
	private void init(byte[] bytesArray) throws Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		InputStream is = new ByteArrayInputStream(bytesArray);

		doc = db.parse(is);
	}
    
	 
	/**
	 * @param bytesArray
	 * @param fileItemsList
	 * @return
	 * @throws Exception
	 */
	private List<TmsSmilItem> viewXML(byte[] bytesArray, List<TmsFileItem> fileItemsList) throws Exception
	{
		this.init(bytesArray);

		NodeList nodeList = doc.getElementsByTagName("body");

		Node fatherNode = nodeList.item(0);

		NodeList childNodes = fatherNode.getChildNodes();
		
		Node childNode = null;
		List<TmsSmilItem> smilItemsList = new ArrayList<TmsSmilItem>();
		TmsSmilItem smilItem = null;
		for (int j = 0; j < childNodes.getLength(); j++)
		{
			childNode = childNodes.item(j);

			if (childNode instanceof Element)
			{
				NamedNodeMap attributes = childNode.getAttributes();
				Node attribute = attributes.item(0);
				
				smilItem = new TmsSmilItem(); 
				smilItem.setDur(attribute.getNodeValue()); 
				
 				NodeList parNodeList = childNode.getChildNodes();
				List<TmsSmilParItem> parItemsList = new ArrayList<TmsSmilParItem>(parNodeList.getLength());
				smilItem.setParItemsList(parItemsList);

				Node parChildNode = null;
				TmsSmilParItem parItem = null;
				for(int i = 0; i < parNodeList.getLength(); i++){
					parChildNode = parNodeList.item(i);
					NamedNodeMap parAttributes = parChildNode.getAttributes();
					
					parItem = new TmsSmilParItem();
					parItem.setSrc(parAttributes.getNamedItem("src").getNodeValue()); 
					if(parAttributes.getNamedItem("region") != null){
						parItem.setType(parAttributes.getNamedItem("region").getNodeValue()); 
					}else{
						parItem.setType("sound"); 
					}
					for(int n = 0; n < fileItemsList.size(); n++){ 
						if(parItem.getSrc().equals("cid:"+fileItemsList.get(n).getFileName())){
							parItem.setContent(fileItemsList.get(n).getContent());
							break;
						}
					}
					parItemsList.add(parItem);
				}
				
			}
			smilItemsList.add(smilItem);
		}
		return smilItemsList;
	}
    
	public static void main(String[] args) throws Exception
	{
		try{
			ParseTmsFile parseTms = new ParseTmsFile();
			List<TmsSmilItem> tmsSmilItem = parseTms.Parse("D:/test/test3.tms");
			//List<TmsFileItem> tmsFileItem = parseTms.getFileItemsList();
			//List<TmsSmilItem> tmsSmilItem = parseTms.viewXML(tmsFileItem.get(0).getContent(), tmsFileItem);
			if(tmsSmilItem == null){
				return;
			}
			
			TmsSmilItem smilItem = null;
			List<TmsSmilParItem> parItemsList = null;
			for(int i = 0; i < tmsSmilItem.size(); i++){
				smilItem = tmsSmilItem.get(i);
				parItemsList = smilItem.getParItemsList();
				if(parItemsList == null){
					continue;
				}
			}
		}catch(Exception e){
		}

	}
    

}

