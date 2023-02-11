package com.acimage.image.service.imagehash.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.model.domain.image.Image;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.utils.ExceptionUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.feign.client.TopicClient;
import com.acimage.image.dao.ImageHashDao;
import com.acimage.image.global.context.DirectoryContext;
import com.acimage.common.model.domain.image.ImageHash;
import com.acimage.image.service.image.ImageQueryService;
import com.acimage.image.service.imagehash.SearchImageService;
import com.acimage.image.utils.BitUtils;
import com.acimage.image.utils.DhashUtils;
import com.acimage.image.utils.ImageFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Slf4j
@Service
public class SearchImageServiceImpl implements SearchImageService {
    @Autowired
    ImageHashDao imageDhashDao;
    @Autowired
    ImageQueryService imageQueryService;
    @Autowired
    TopicClient topicClient;

    /**
     * 利用dhash算法处理未被处理过的图片文件，保存到数据库中，在高并发环境下可能会有重复处理的问题
     * 废弃原因：图片存到七牛云，而不是本地了
     */
    @Override
    @Deprecated
    public void processImagesHashForNotProcessedImages() {
        //获取所有保存的图片
        String directoryPath = DirectoryContext.IMAGES_DIRECTORY;
        File directory = new File(directoryPath);
        File[] files;
        if (directory.isDirectory()) {
            files = directory.listFiles();
        } else {
            log.error("{} 不是目录或不存在", directoryPath);
            return;
        }
        if (files == null) {
            return;
        }

        //获取数据库中已经被处理过的图片
        List<ImageHash> imageHashList = imageDhashDao.selectList(null);
        List<Long> imageIdsInDb = ListUtils.extract(ImageHash::getImageId, imageHashList);
        List<Long> imageIdsInDirectory = new ArrayList<>();
        for (File file : files) {
            String stringId = StrUtil.subBefore(file.getName(), '.', true);
            if (stringId.length() > 12) {
                //排除掉默认图片0.jpeg等
                imageIdsInDirectory.add(Long.parseLong(stringId));
            }
        }

        //得到没被处理过的图片
        List<Long> differenceList = ListUtils.differenceSetOf(imageIdsInDirectory, imageIdsInDb);

        //将图片dhash值保存到数据库中
        for (Long imageId : differenceList) {
            String filePath = ImageFileUtils.imageIdToImagePath(imageId);
            File file = new File(filePath);
            try {
                //此处是同步进行，因为该方法非代理对象的方法
                hashImageByDhashAlgorithm(new FileInputStream(file), imageId);
            } catch (FileNotFoundException e1) {
                log.error("提取图片dhash时 文件：{}不存在", filePath);
            }
        }
    }


    @Override
    public void hashImageByDhashAlgorithm(InputStream imageInputStream, long imageId) {
        long hashValue;
        try {
            hashValue = DhashUtils.getImageDhashFrom(imageInputStream);
        } catch (IOException e) {
            log.error("imageId:{} 对应文件IO异常", imageId);
            ExceptionUtils.printIfDev(e);
            throw new RuntimeException(e);
        }
        int hashSum = BitUtils.sumOfBits(hashValue);

        try {
            imageDhashDao.insert(new ImageHash(imageId, hashValue, hashSum));
        } catch (DuplicateKeyException e) {
            log.error("保存图片dhash时，插入数据库imageId：{}重复", imageId);
        }
    }

    @Override
    public List<Image> searchMostSimilarImages(MultipartFile imageFile) {
        int rankEnd = 10;
        final int threshold = 20;

        InputStream inputStream;
        try {
            inputStream = imageFile.getInputStream();
        } catch (IOException e) {
            log.error("用户：{} 以图搜图 错误：传入文件getInputStream异常", UserContext.getUsername());
            throw new BusinessException("文件IO异常");
        }
        long hashValue;
        try {
            hashValue = DhashUtils.getImageDhashFrom(inputStream);
        } catch (IOException e) {
            throw new BusinessException("文件IO异常");
        }
        List<ImageHash> imageHashList = imageDhashDao.selectList(null);
        List<ImageHash> resultList = new ArrayList<>();
        for (ImageHash imageHash : imageHashList) {
            int distance = DhashUtils.distanceBetween(hashValue, imageHash.getHashValue());
            imageHash.setDistance(distance);
            if (distance <= threshold) {
                resultList.add(imageHash);
            }
        }
        resultList.sort(Comparator.comparing(ImageHash::getDistance));
        int toIndex = Math.min(rankEnd, resultList.size());
        log.info("搜索结果：{}", resultList);

        //找到图片对象
        List<Long> imageIds=ListUtils.extract(ImageHash::getImageId, resultList.subList(0, toIndex));
        List<Image> images=imageQueryService.listImagesByImageIdsInOrder(imageIds);
        if(CollectionUtil.isEmpty(images)){
            return new ArrayList<>();
        }
        //找到对应话题
        List<Long> topicIds=ListUtils.extract(Image::getTopicId,images);
        List<Topic> topics=topicClient.queryTopics(topicIds).getData();
        List<Image> imageWithTopics=new ArrayList<>();
        for (Image image : images) {
            for (Topic topic : topics) {
                if (topic.getId().equals(image.getTopicId())) {
                    image.setTopic(topic);
                    imageWithTopics.add(image);
                }
            }
        }
        return imageWithTopics;
    }

    @Override
    public void removeImageHashes(List<Long> imageIds) {
        imageDhashDao.deleteBatchIds(imageIds);
    }
}
