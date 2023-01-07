package com.acimage.common.utils.common;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    public static void packageFiles(File[] files, File zipFile) throws IOException {
        byte[] buffer = new byte[4096];
        ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()));
        for (int i = 0; i < files.length; i++) {
            FileInputStream fis = new FileInputStream(files[i]);
//            FileInputStream fis = new FileInputStream(files[i].getPath());
            out.putNextEntry(new ZipEntry(files[i].getName()));
            int len;
            // 读入需要下载的文件的内容，打包到zip文件
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            fis.close();
        }
        out.close();

    }

    public static void downloadFile(File file, HttpServletResponse response) throws IOException{
        response.setCharacterEncoding("utf-8");
        // response.setContentType("application/octet-stream");

        // 以流的形式下载文件。
        BufferedInputStream fis = new BufferedInputStream(Files.newInputStream(Paths.get(file.getPath())));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();

        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }

    public static String formatOf(String fileName){
        return StrUtil.subAfter(fileName,".",true);
    }

    public static List<String> formatsOf(MultipartFile[] multipartFiles){
        if(multipartFiles==null){
            return null;
        }else if(multipartFiles.length==0){
            return new ArrayList<>();
        }else{
            List<String> formatList=new ArrayList<>();
            for (MultipartFile multipartFile : multipartFiles) {
                formatList.add(formatOf(multipartFile.getOriginalFilename()));
            }
            return formatList;
        }
    }
}
