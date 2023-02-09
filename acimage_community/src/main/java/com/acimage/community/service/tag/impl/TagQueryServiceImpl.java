package com.acimage.community.service.tag.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.community.Tag;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.community.dao.TagDao;
import com.acimage.community.service.tag.TagQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TagQueryServiceImpl implements TagQueryService {
    @Autowired
    TagDao tagDao;

    @Override
    public List<Tag> listAll() {
        return tagDao.selectList(null);
    }

    @Override
    public List<Tag> checkAndListTags(List<Integer> tagIds) {
        if (CollectionUtil.isEmpty(tagIds)) {
            return new ArrayList<>();
        }
        List<Tag> allTags = listAll();
        List<Integer> allTagIds = ListUtils.extract(Tag::getId, allTags);
        for (Integer tagId : tagIds) {
            if (!allTagIds.contains(tagId)) {
                log.error("cmtyUser:{} error:标签不存在 tagId:{}", UserContext.getUsername(), tagId);
                throw new BusinessException("标签不存在");
            }
        }
        int i = 0;
        List<Tag> tags = new ArrayList<>();
        while (i < tagIds.size()-1) {
            for (Tag item : allTags) {
                if (item.getId().equals(tagIds.get(i))) {
                    tags.add(item);
                    i++;
                    break;
                }
            }
        }

        return tags;
    }

}
