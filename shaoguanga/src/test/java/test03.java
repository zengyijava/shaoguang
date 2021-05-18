import java.io.*;

/**
 * @param
 * @ClassName test03
 * @Author zengyi
 * @Description
 * @Date 2021/4/14 14:24
 **/
public class test03 {
    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\dell\\Desktop\\test");
        File[] files = file.listFiles();
        BufferedReader reader = null;
        String line = null;
        for (File file1 : files) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
            System.out.println(sb.toString());


        }

    }
}
