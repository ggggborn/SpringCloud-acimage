package com.acimage.image.service.imagehash.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.image.dao.ImageHashDao;
import com.acimage.image.model.domain.ImageHash;
import com.acimage.image.service.imagehash.ImageHashWriteService;
import com.acimage.image.utils.BitUtils;
import com.acimage.image.utils.DhashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class ImageHashWriteServiceImpl implements ImageHashWriteService {
    @Autowired
    ImageHashDao imageHashDao;

    @Override
    public void removeImageHashes(List<Long> imageIds) {
        if (CollectionUtil.isEmpty(imageIds)) {
            return;
        }
        imageHashDao.deleteBatchIds(imageIds);
    }

    @Override
    public void HashImagesByDhash(InputStream imageInputStream, long imageId) {
        long hashValue;
        try {
            hashValue = DhashUtils.getImageDhashFrom(imageInputStream);
        } catch (IOException e) {
            log.error("imageId:{} 对应文件IO异常", imageId);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        int hashSum = BitUtils.sumOfBits(hashValue);

        try {
            imageHashDao.insert(new ImageHash(imageId, hashValue, hashSum));
        } catch (DuplicateKeyException e) {
            log.error("保存图片哈希值时，插入数据库imageId：{}重复", imageId);
        }
    }
}
