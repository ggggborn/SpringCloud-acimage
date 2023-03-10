package com.acimage.common.deprecated;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.utils.ExceptionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@ConditionalOnClass(Auth.class)
@ConfigurationProperties(prefix = "qiniu")
@Deprecated
public class QiniuUtilsBak {

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domain;

    private String uploadToken;
    UploadManager uploadManager;
    Auth auth;
    Configuration cfg;
    private static final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    private void init() {
        cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// ????????????????????????
        uploadManager = new UploadManager(cfg);
        auth = Auth.create(accessKey, secretKey);
        uploadToken = auth.uploadToken(bucket);
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @param multipartFile ???????????????
     */
    public void upload(@NotNull MultipartFile multipartFile, String urlWithoutDomain) {
        InputStream is;
        try {
            is = multipartFile.getInputStream();
        } catch (IOException e) {
            ExceptionUtils.printIfDev(e);
            log.error("error: multipartFile.getInputStream()??????:{}", e.getMessage());
            throw new RuntimeException(e);
        }

        putAndLog(is, urlWithoutDomain, uploadToken);
    }

    public void upload(@NotNull File file, String urlWithoutDomain) {
        putAndLog(file, urlWithoutDomain, uploadToken);
    }

    public void cover(@NotNull MultipartFile multipartFile, String urlWithoutDomain) {
        String coverToken = auth.uploadToken(bucket, urlWithoutDomain);
        InputStream is;
        try {
            is = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        putAndLog(is, urlWithoutDomain, coverToken);
        new Thread(() -> {
            refreshFile(urlWithoutDomain);
            refreshQuery(urlWithoutDomain);
        }).start();

    }


    public void refreshFile(String... urls) {
        int urlAmountLimit = 100;
        CdnManager c = new CdnManager(auth);
        if (urls.length > urlAmountLimit) {
            log.error("???????????????????????????100???");
            return;
        }

        //??????????????????
        for (int i = 0; i < urls.length; i++) {
            urls[i] = getTrueUrl(urls[i]);
        }

        try {
            //????????????????????????????????????????????????100???
            CdnResult.RefreshResult result = c.refreshUrls(urls);
            //???????????????????????????
        } catch (QiniuException e) {
            log.error("???????????????url?????? urls:{}", Arrays.asList(urls));
        }
    }


    public void refreshQuery(String url) {
        String trueUrl = getTrueUrl(url);
        StringMap str = auth.authorization(trueUrl);
        Client client = new Client();
        try {
            client.post(trueUrl, "", str);
        } catch (QiniuException e) {
            log.error("????????????oss url?????? url:{} error:{}", url,e.getMessage());
            ExceptionUtils.printIfDev(e);
        }
    }

    public void deleteFile(String url) {
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, url);
        } catch (QiniuException ex) {
            //???????????????????????????????????????
            log.error("????????????????????? url???{} ???????????????{} ?????????????????????{}", url, ex.code(), ex.response.toString());
        }
    }

    public void batchDelete(List<String> urlList) {
        if (CollectionUtil.isEmpty(urlList)) {
            return;
        }
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            //?????????????????????????????????????????????1000

            String[] urls = urlList.toArray(new String[0]);

            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, urls);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < urls.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = urls[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                }
            }
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }

    public String generateUrl(String suffix, Date uploadTime, @Nullable String prefix) {
        String formatPattern = "yyyy/MM/dd";
        String newPrefix = prefix == null ? "" : prefix + "/";
        SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
        return newPrefix + formatter.format(uploadTime) + "/" + suffix;
    }

    public String getTrueUrl(String urlWithoutDomain) {
        return domain + "/" + urlWithoutDomain;
    }

    public void download(String url, String toPath) {
        String encodedUrl = null;
        try {
            encodedUrl = URLEncoder.encode(url, "utf-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            ExceptionUtils.printIfDev(e);
            log.error("url???????????? error???{}", e.getMessage());
        }
        String publicUrl = domain + "/" + encodedUrl;
        long expireInSeconds = 3600;//1??????????????????????????????????????????

        HttpUtil.downloadFile(publicUrl, new File(toPath));//??????
    }

    private void putAndLog(Object inputStreamOrFile, String urlWithoutDomain, String token) {
        Response response = null;
        try {
            if (inputStreamOrFile instanceof InputStream) {
                InputStream is = (InputStream) inputStreamOrFile;
                response = uploadManager.put(is, urlWithoutDomain, token, null, null);
            } else if (inputStreamOrFile instanceof File) {
                File file = (File) inputStreamOrFile;
                response = uploadManager.put(file, urlWithoutDomain, token);
            } else {
                throw new IllegalArgumentException(String.format("??????inputStreamOrFile????????????:%s", inputStreamOrFile.getClass()));
            }

            if (response == null) {
                return;
            }

            //???????????????????????????
            DefaultPutRet putRet = mapper.readValue(response.bodyString(), DefaultPutRet.class);
            log.info("??????????????????????????? ????????????key???{} hash???{}", putRet.key, putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                String responseMsg = r.bodyString();
                log.error(responseMsg);
                throw new BusinessException("???????????????????????????");
            } catch (QiniuException ex2) {
                log.error("??????????????????????????????????????? url:{}", urlWithoutDomain);
                throw new BusinessException("?????????????????????");
                //ignore
            }
        } catch (JsonProcessingException e) {
            log.error("json?????????????????????????????????");
            throw new BusinessException("???????????????????????????????????????????????????");
        }

    }
}

