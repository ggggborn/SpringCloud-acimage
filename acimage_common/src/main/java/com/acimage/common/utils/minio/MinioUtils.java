package com.acimage.common.utils.minio;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
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
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        return String.format("/%s/%s",minioProperties.getBucket(),url);
    }

    public void downloadTo(String url, String destSrc) {
        try {
            minioClient.downloadObject(
                    DownloadObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(url)
                            .filename(destSrc)
                            .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void deleteFile(String url) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(url)
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

    public String generateUrl(@Nullable String prefix, Date uploadTime,String suffix) {
        String formatPattern = "yyyy/MM/dd";
        String newPrefix = prefix == null ? "" : prefix + "/";
        SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
        return newPrefix + formatter.format(uploadTime) + "/" + suffix;
    }

}
