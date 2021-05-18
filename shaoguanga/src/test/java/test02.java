import java.io.*;

/**
 * @param
 * @ClassName test02
 * @Author zengyi
 * @Description
 * @Date 2021/4/20 14:33
 **/
public class test02 {
    public static void main(String[] args) throws IOException {
        String path="C:\\Users\\dell\\Desktop\\1.txt";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        StringBuffer sb=new StringBuffer();
        String line= null;
        while ( (line=br.readLine())!=null ){
            sb.append(line);
        }
        System.out.println(sb.toString());
    }
}
