package com.acimage.community.service.tag.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.model.domain.community.TagTopic;
import com.acimage.common.utils.IdGenerator;
import com.acimage.community.dao.TagTopicDao;
import com.acimage.community.service.tag.TagQueryService;
import com.acimage.community.service.tag.TagTopicWriteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TagTopicWriteServiceImpl implements TagTopicWriteService {
    @Autowired
    TagTopicDao tagTopicDao;
    @Autowired
    TagQueryService tagQueryService;

    @Override
    public void save(long topicId, List<Integer> tagIdList) {
//        //检查标签是否存在
//        List<Tag> tagList = tagQueryService.listAll();
//        List<Integer> allTagIds = ListUtils.extract(Tag::getId, tagList);
//        for (Integer tagId : tagIdList) {
//            if (!allTagIds.contains(tagId)) {
//                log.error("cmtyUser:{} error:标签不存在 tagId:{}", UserContext.getUsername(), tagId);
//                throw new BusinessException("标签不存在");
//            }
//        }

        if (CollectionUtil.isEmpty(tagIdList)) {
            return;
        }

        List<TagTopic> tagTopicList = new ArrayList<>();
        Date now = new Date();

        for (Integer tagId : tagIdList) {
            TagTopic tagTopic = TagTopic.builder()
                    .id(IdGenerator.getSnowflakeNextId())
                    .topicId(topicId)
                    .tagId(tagId)
                    .createTime(now)
                    .build();
            tagTopicList.add(tagTopic);
        }
        tagTopicDao.insertBatch(tagTopicList);
    }

    @Override
    public void remove(long topicId){
        LambdaQueryWrapper<TagTopic> qw=new LambdaQueryWrapper<>();
        qw.eq(TagTopic::getTopicId,topicId);
        tagTopicDao.delete(qw);

    }
}
