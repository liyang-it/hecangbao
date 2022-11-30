package com.mh.jishi;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * <h2>测试下载</h2>
 * <p>
 *
 * </p>
 *
 * @author Evan <1922802352@qq.com>
 * @since 2022年11月26日 10:15
 */
@SpringBootTest
public class TestDownload {
    @Test
    public void test11() throws FileNotFoundException {

        // 使用hutool 默认UTF-8编码，可以在构造中传入第二个参数做为编码
        /**
         * input.txt 内容
         * [{"IsMustVague":1,"FileName":"20221125185504925284.jpeg","IsImageCanDownload":2,"FileUrl":"https://s04.weimiquan.com.cn/r/20221125/9ca3c4e77f804461aef6c66f1d07e848.jpeg","FileIndex":0,"ImageHeight":2337,"IsCanRestoreRawImage":0,"FileType":".jpeg","ImageWidth":1080,"FileThumbnail":"https://s04.weimiquan.com.cn/r/20221125/t9ca3c4e77f804461aef6c66f1d07e848.jpeg","IsOrigin":0,"Id":10290415,"FileUrl2":"https://s04.weimiquan.com.cn/r/20221125/9ca3c4e77f804461aef6c66f1d07e848.jpeg","FileSize":199297,"ScanImageTag":""}]
         */
        FileReader fileReader = new FileReader("F:/囧静/input.txt");

        JSONArray imageList = JSONArray.parseArray(fileReader.readString());

        for (int i = 0, size = imageList.size(); i < size; i++) {
            String url = imageList.getJSONObject(i).getString("FileUrl2");
            Integer id = imageList.getJSONObject(i).getInteger("Id");
            long fileSize = HttpUtil.downloadFile(url, FileUtil.file("F:\\囧静-更新至2022-11-25"));
            System.out.printf("下载图片ID：%s%n", url);
            System.out.printf("下载图片链接：%s%n", id);
        }
        System.out.printf("此次下载数量：%s%n", imageList.size());
    }

    @Test
    public void test12() throws IOException {

        // 使用hutool 默认UTF-8编码，可以在构造中传入第二个参数做为编码

        FileReader fileReader = new FileReader("F:/囧静/input.txt");

        JSONArray DataList = JSONArray.parseArray(fileReader.readString());

        System.out.printf("Data字段数量：%s%n", DataList.size());

        FileReader reader;
        FileWriter fileWriter = null;

        for (int d = 0, dsize = DataList.size(); d < dsize; d++) {
            String createDateTime = DataList.getJSONObject(d).getString("CreateTime");
            // 创建 时间文件夹
            createDateTime = createDateTime.substring(0, createDateTime.indexOf(" "));
            String path = String.format("F:/囧静/%s/%s.txt", createDateTime, createDateTime);
            File file = new File(path);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            JSONArray imageList = DataList.getJSONObject(d).getJSONArray("ImageList");

            System.out.printf("日期：%s,图片数量：%s%n", createDateTime, imageList.size());

            System.out.printf("Data下标：%s  images数量: %s%n", d, imageList.size());

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0, size = imageList.size(); i < size; i++) {


                String url = imageList.getJSONObject(i).getString("FileUrl2");

                Integer id = imageList.getJSONObject(i).getInteger("Id");

                // 将图片保存在文件夹
                long fileSize = HttpUtil.downloadFile(url, parentFile);

                System.out.printf("下载图片ID：%s%n", url);

                System.out.printf("下载图片链接：%s%n", id);

                stringBuilder.append(url).append("\r\n");
            }
            // 同时将 图片url 写入到文件中
            // 先读取 文件内容，写入 避免被覆盖

            fileReader = new FileReader(file);
            String oldContent = fileReader.readString();
            if(StringUtils.isNotBlank(oldContent)){
                stringBuilder.insert(0, oldContent);
            }
            fileWriter = new FileWriter(file);
            fileWriter.write(stringBuilder.toString());
            fileWriter.flush();
        }
        if(fileWriter != null){
            fileWriter.close();
        }
    }
    @Test
    public void test13() throws IOException {

        // 使用 jdk 默认UTF-8编码，可以在构造中传入第二个参数做为编码
        File tmpFile = new File("F:/囧静/input.txt");

        long len = tmpFile.length();
        // 字节数组
        byte[] bytes = new byte[(int)len];

        FileInputStream inputStream = null;
        int readLength = 0;
        try {
            inputStream = new FileInputStream(tmpFile);
            readLength = inputStream.read(bytes);
            if(readLength < len){
               throw new RuntimeException();
            }
        }catch (Exception e){

        }finally {
            if(inputStream != null){
                inputStream.close();
            }
        }

        String readString = new String(bytes, StandardCharsets.UTF_8);

        System.out.printf("使用jdk io读取文件：%s%n", readString);

        JSONArray DataList = JSONArray.parseArray(readString);

        System.out.printf("Data字段数量：%s%n", DataList.size());


        java.io.FileReader fileReader = null;

        FileWriter fileWriter = null;


        for (int d = 0, dsize = DataList.size(); d < dsize; d++) {
            String createDateTime = DataList.getJSONObject(d).getString("CreateTime");
            // 创建 时间文件夹
            createDateTime = createDateTime.substring(0, createDateTime.indexOf(" "));

            String path = String.format("F:/囧静2/%s/%s.txt", createDateTime, createDateTime);

            File file = new File(path);

            File parentFile = file.getParentFile();

            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            JSONArray imageList = DataList.getJSONObject(d).getJSONArray("ImageList");

            System.out.printf("日期：%s,图片数量：%s%n", createDateTime, imageList.size());

            System.out.printf("Data下标：%s  images数量: %s%n", d, imageList.size());

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0, size = imageList.size(); i < size; i++) {


                String url = imageList.getJSONObject(i).getString("FileUrl2");

                Integer id = imageList.getJSONObject(i).getInteger("Id");

                // 将图片保存在文件夹
                long fileSize = HttpUtil.downloadFile(url, parentFile);

                System.out.printf("下载图片ID：%s%n", url);

                System.out.printf("下载图片链接：%s%n", id);

                stringBuilder.append(url).append("\r\n");
            }
            // 同时将 图片url 写入到文件中

            // 先读取 文件内容，写入 避免被覆盖

            fileReader = new java.io.FileReader(file);
            long fileLength = file.length();
            char[] fileChars = new char[(int)fileLength];
            int fileReadLen = fileReader.read(fileChars);
            if(fileReadLen == -1){
                String oldContent = new String(fileChars);
                if(StringUtils.isNotBlank(oldContent)){
                    stringBuilder.insert(0, oldContent);
                }
                fileWriter = new FileWriter(file);
                fileWriter.write(stringBuilder.toString());
                fileWriter.flush();
            }

        }
        if(fileWriter != null){
            fileWriter.close();
        }

        if(fileReader != null){
            fileReader.close();
        }

    }
}
