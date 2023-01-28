package com.acimage.community.service.tag;

import com.acimage.common.model.domain.community.Tag;

import java.util.List;

public interface TagQueryService {
    List<Tag> listAll();

    List<Tag> checkAndListTags(List<Integer> tagIds);
}
