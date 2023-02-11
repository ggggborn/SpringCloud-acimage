package com.acimage.image.service.impl;


import cn.hutool.core.util.IdUtil;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.utils.ExceptionUtils;
import com.acimage.common.utils.common.FileUtils;
import com.acimage.image.global.consts.MyFileConst;
import com.acimage.image.global.context.DirectoryContext;
import com.acimage.image.service.FileService;
import com.acimage.image.service.imagehash.SearchImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Deprecated
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    SearchImageService searchImageService;

    @Override
    public void storeImageFiles(MultipartFile[] imageFiles, List<Long> imageIds) {
        List<String> imageNamesWithFormat = createImageNamesWithFormat(imageIds);
        File imagesDirectory = new File(DirectoryContext.IMAGES_DIRECTORY);
        if (!imagesDirectory.exists()) {
            log.warn("目录不存在:{}", imagesDirectory.getAbsoluteFile());
            boolean isSuccess = imagesDirectory.mkdir();
            if (!isSuccess) {
                log.error("创建目录失败:{}", imagesDirectory.getAbsolutePath());
            } else {
                log.info("创建目录成功:{}", imagesDirectory.getAbsolutePath());
            }
        }
        //先保存输入流
        List<InputStream> imageInputStreams=new ArrayList<>();
        for(int i = 0; i < imageFiles.length; i++){
            try {
                imageInputStreams.add(imageFiles[i].getInputStream());
            } catch (IOException e) {
                throw new BusinessException("获取输入流IO异常");
            }
        }

        // 保存图片文件
        for (int i = 0; i < imageFiles.length; i++) {
            String imagePath = DirectoryContext.IMAGES_DIRECTORY + "/" + imageNamesWithFormat.get(i);
            File file = new File(imagePath);
            try {
                imageFiles[i].transferTo(file);
            } catch (IOException e) {
                log.error("图片保存时出错:{}", imagePath);
                throw new BusinessException("图片保存时出错" + imagePath);
            }
        }

        //异步图片哈希处理，方便识图
        for(int i=0;i<imageFiles.length;i++){
            try {
                searchImageService.hashImageByDhashAlgorithm(imageFiles[i].getInputStream(),imageIds.get(i));
            } catch (IOException e) {
                log.error("IO异常");
                throw new BusinessException("服务繁忙，请稍后重试");
            }
        }

    }

    @Override
    public void storePhotoFiles(MultipartFile photoFile) {
        File photosDirectory = new File(DirectoryContext.PHOTOS_DIRECTORY);
        if (!photosDirectory.exists()) {
            log.warn("目录不存在:{}", photosDirectory.getAbsoluteFile());
            boolean isSuccess = photosDirectory.mkdir();
            if (!isSuccess) {
                log.error("创建目录失败:{}", photosDirectory.getAbsolutePath());
            } else {
                log.info("创建目录成功:{}", photosDirectory.getAbsolutePath());
            }
        }
        String photoNamesWithFormat = UserContext.getUserId() + "." + MyFileConst.IMAGE_FORMAT;
        String photoPath = DirectoryContext.PHOTOS_DIRECTORY + "/" + photoNamesWithFormat;
        File file = new File(photoPath);
        try {
            photoFile.transferTo(file);
        } catch (IOException e) {
            log.error("图片保存时出错:{}", photoPath);
            throw new BusinessException("图片保存时出错" + photoPath);
        }
    }

    public void removeImageFiles(List<Long> imageIds) {
        List<String> imageNamesWithFormat = createImageNamesWithFormat(imageIds);
        for (String name : imageNamesWithFormat) {
            String imagePath = DirectoryContext.IMAGES_DIRECTORY + "/" + name;
            File file = new File(imagePath);
            if (file.exists()) {
                file.delete();
            } else {
                log.error("删除 文件{} 错误：文件不存在", file.getAbsolutePath());
            }
        }
        searchImageService.removeImageHashes(imageIds);
    }

    public void downloadImageFiles(List<Long> imageIds, HttpServletResponse response) {

        List<String> imageNamesWithFormat = createImageNamesWithFormat(imageIds);
        File[] files = new File[imageIds.size()];
        for (int i = 0; i < files.length; i++) {
            files[i] = new File(DirectoryContext.IMAGES_DIRECTORY + "/" + imageNamesWithFormat.get(i));
            if (!files[i].exists()) {
                log.error("用户：{} 下载图片 错误：图片文件{}不存在", UserContext.getUsername(), imageNamesWithFormat.get(i));
                throw new BusinessException("图片不存在");
            }
        }

        //创建Zip文件
        String zipPath = DirectoryContext.IMAGES_DIRECTORY + "/" + IdUtil.simpleUUID() + "." + MyFileConst.ZIP_FORMAT;
        File zipFile = new File(zipPath);
        if (!zipFile.exists()) {
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                log.error("文件{} 创建异常", zipPath);
                throw new BusinessException("压缩包创建异常，请刷新重试");
            }
        }

        //将图片打包到该zip文件
        try {
            FileUtils.packageFiles(files, zipFile);
        } catch (IOException e) {
            ExceptionUtils.printIfDev(e);
            zipFile.delete();
            log.error("文件{} 打包时异常：{}", zipPath, e.getStackTrace());
            throw new BusinessException("压缩包打包异常，请刷新重试");
        }

        //客户端下载文件
        try {
            FileUtils.downloadFileForClient(zipFile, response);
        } catch (IOException e) {
            log.error("文件{} 下载时异常：{}", zipPath, e.getMessage());
            throw new BusinessException("压缩包下载异常，请刷新重试");
        } finally {
            zipFile.delete();
        }
    }

    private List<String> createImageNamesWithFormat(List<Long> imageIds) {
        List<String> imageNames = new ArrayList<>(imageIds.size());
        for (Long id : imageIds) {
            imageNames.add(id.toString() + "." + MyFileConst.IMAGE_FORMAT);
        }
        return imageNames;
    }



}
