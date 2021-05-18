package test;
import java.io.File;

import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

public class Hclient
{
public static void main(String args[])
{
   String targetURL = null;// TODO 指定URL
   File targetFile = null;// TODO 指定上传文件
  
   targetFile = new File("C:\\Users\\benwork\\Desktop\\temp\\1.jpg");
   targetURL = "http://192.168.0.63:8984/fileserver/UploadServlet"; //servleturl
   PostMethod filePost = new PostMethod(targetURL);
  
   try
   {

    //通过以下方法可以模拟页面参数提交
    //filePost.setParameter("name", "中文");
    //filePost.setParameter("pass", "1234");

   Part[] parts = { new FilePart(targetFile.getName(), targetFile) };
   Header header = new Header();
   filePost.setRequestHeader(header);
    filePost.setRequestEntity(new MultipartRequestEntity(parts,filePost.getParams()));
    HttpClient client = new HttpClient();
    client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
    int status = client.executeMethod(filePost);
    if (status == HttpStatus.SC_OK)
    {
     System.out.println("上传成功");
     // 上传成功
    }
    else
    {
     System.out.println("上传失败");
     // 上传失败
    }
   }
   catch (Exception ex)
   {
       EmpExecutionContext.error("上传异常。");
   }
   finally
   {
    filePost.releaseConnection();
   } 
}
}