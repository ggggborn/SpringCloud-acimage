package com.acimage.common.utils.minio;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@ConditionalOnClass(MinioClient.class)
public class MinioUtils {
    @Autowired
    MinioClient minioClient;
    @Autowired
    MinioProperties minioProperties;

    public String upload(MultipartFile multipartFile, String url) {
        InputStream in = null;
        try {
            in = multipartFile.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(url)
                    .stream(in, in.available(), -1)
                    .contentType(multipartFile.getContentType())
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return String.format("/%s/%s", minioProperties.getBucket(), url);
    }

    public String upload(InputStream is, String url, String contentType) {

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(url)
                    .contentType(contentType)
                    .stream(is, is.available(), -1)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return String.format("/%s/%s", minioProperties.getBucket(), url);
    }

    public void downloadTo(String url, String destSrc) {
        try {
            String slashBucket=String.format("/%s",minioProperties.getBucket());
            boolean isInnerUrl=url.startsWith(slashBucket);
            if(!isInnerUrl){
                return;
            }
            String innerUrl=url.substring(slashBucket.length());
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(innerUrl)
                            .filename(destSrc)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void download(String url, String toPath) {
//        String encodedUrl = null;
//        try {
//            encodedUrl = URLEncoder.encode(url, "utf-8").replace("+", "%20");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            log.error("url编码失败 error：{}", e.getLocalizedMessage());
//        }
        String publicUrl;
        if (url.startsWith("/")) {
            publicUrl = minioProperties.getEndpoint() + "/" + url;
        } else {
            publicUrl = minioProperties.getEndpoint() + url;
        }
        HttpUtil.downloadFile(publicUrl, new File(toPath));//下载
    }


    public void deleteFile(String totalUrl) {
        String slashBucket=String.format("/%s",minioProperties.getBucket());
        boolean isInnerUrl=totalUrl.startsWith(slashBucket);
        if(!isInnerUrl){
            return;
        }
        String innerUrl=totalUrl.substring(slashBucket.length());
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(innerUrl)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteFiles(List<String> urls) {
        try {
            List<DeleteObject> deleteObjectList = new ArrayList<>();
            for (String url : urls) {
                deleteObjectList.add(new DeleteObject(url));
            }
            minioClient.removeObjects(RemoveObjectsArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .objects(deleteObjectList)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String generateUrl(@Nullable String prefix, Date uploadTime, String suffix) {
        String formatPattern = "yyyy/MM/dd";

        String newPrefix = prefix == null ? "" : prefix + "/";
        SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
        return newPrefix + formatter.format(uploadTime) + "/" + suffix;
    }

}
