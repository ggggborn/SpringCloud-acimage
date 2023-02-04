package com.acimage.community.service.topic.Impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.utils.EsUtils;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.community.model.request.SearchTopicReq;
import com.acimage.community.service.topic.TopicSearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TopicSearchServiceImpl implements TopicSearchService {
    @Autowired
    EsUtils esUtils;
    @Autowired
    ElasticsearchRestTemplate esTemplate;


    @Override
    public MyPage<Topic> searchByTagId(Integer tagId, int pageNo, int pageSize) {
        String column = LambdaUtils.columnNameOf(Topic::getTagIds);
        MyPage<TopicIndex> topicIndexPage = esUtils.termQuery(column, tagId, TopicIndex.class, pageNo, pageSize);
        List<Topic> topicList = TopicIndex.toTopicList(topicIndexPage.getDataList());
        return new MyPage<>(topicIndexPage.getTotalCount(), topicList);
    }

    @Override
    public List<Topic> searchSimilar(long topicId, int size) {
        List<String> columns = LambdaUtils.columnsFrom(Topic::getContent, Topic::getTitle);
        List<TopicIndex> topicIndices = esUtils.similarQuery(Long.toString(topicId), TopicIndex.class, columns, 1, size);
        return TopicIndex.toTopicList(topicIndices);
    }

    @Override
    public List<Topic> searchSimilarByTitle(long topicId, String title, int size) {
        String column = LambdaUtils.columnNameOf(TopicIndex::getTitle);
        float score = 2;
        List<TopicIndex> topicIndices = esUtils.matchQuery(TopicIndex.class, column, title, 1, size + 1, score);
        return TopicIndex.toTopicList(topicIndices).stream()
                .filter(o -> !o.getId().equals(topicId))
                .collect(Collectors.toList());
    }


    @Override
    public MyPage<Topic> search(SearchTopicReq searchTopicReq) {
        String search = searchTopicReq.getSearch();
        Integer categoryId = searchTopicReq.getCategoryId();
        Integer tagId = searchTopicReq.getTagId();
        Integer pageNo = searchTopicReq.getPageNo();
        SearchTopicReq.SortBy sort = searchTopicReq.getSortBy();


        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        HighlightBuilder highlightBuilder = null;

        String titleColumn = LambdaUtils.columnNameOf(TopicIndex::getTitle);
        String contentColumn = LambdaUtils.columnNameOf(TopicIndex::getContent);

        if (!StrUtil.isBlank(search)) {
            HighlightBuilder.Field titleField = new HighlightBuilder.Field(titleColumn);

            HighlightBuilder.Field contentField = new HighlightBuilder.Field(contentColumn);

            MultiMatchQueryBuilder matchQuery = QueryBuilders.multiMatchQuery(search, titleColumn, contentColumn);
            highlightBuilder = new HighlightBuilder()
                    .field(titleField)
                    .field(contentField)
                    .preTags("<span style='color:red'>")
                    .postTags("</span>");
            boolQuery.must().add(matchQuery);
        }
        if (tagId != null) {
            String tagIdsColumn = LambdaUtils.columnNameOf(TopicIndex::getTagIds);
            boolQuery.filter().add(QueryBuilders.termQuery(tagIdsColumn, tagId));
        }
        if (categoryId != null) {
            String categoryIdColumn = LambdaUtils.columnNameOf(TopicIndex::getCategoryId);
            boolQuery.filter().add(QueryBuilders.termQuery(categoryIdColumn, categoryId));
        }


        NativeSearchQueryBuilder nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(pageNo - 1, 10));

        if (highlightBuilder != null) {
            nativeSearchQuery.withHighlightBuilder(highlightBuilder);
        }
        if (sort != null && sort.toColumn() != null) {
            FieldSortBuilder sortBuilder = new FieldSortBuilder(Objects.requireNonNull(sort.toColumn()));
            nativeSearchQuery.withSort(sortBuilder);
        }

        //整合结果
        List<TopicIndex> topicIndexList = new ArrayList<>();
        SearchHits<TopicIndex> searchHits = esTemplate.search(nativeSearchQuery.build(), TopicIndex.class);
        int total = (int) searchHits.getTotalHits();

        for (SearchHit<TopicIndex> searchHit : searchHits.getSearchHits()) {
            TopicIndex topicIndex = searchHit.getContent();

            if (!StrUtil.isBlank(search)) {
                //title高亮
                StringBuilder newTitle = new StringBuilder();
                List<String> titleHighlights = searchHit.getHighlightFields().get(titleColumn);
                if (!CollectionUtil.isEmpty(titleHighlights)) {
                    if (titleHighlights.size() > 0) {
                        for (String highlight : titleHighlights) {
                            newTitle.append(highlight);
                        }
                    }
                    topicIndex.setTitle(newTitle.toString());
                }
                //content高亮
                StringBuilder newContent = new StringBuilder();
                List<String> contentHighlights = searchHit.getHighlightFields().get(contentColumn);
                if (!CollectionUtil.isEmpty(contentHighlights)) {
                    if (contentHighlights.size() > 0) {
                        for (String highlight : contentHighlights) {
                            newContent.append(highlight)
                                    .append(" ");
                        }
                    }
                    topicIndex.setContent(newContent.toString());
                }
            }
            topicIndexList.add(topicIndex);
        }

        return new MyPage<>(total, TopicIndex.toTopicList(topicIndexList));
    }
}
