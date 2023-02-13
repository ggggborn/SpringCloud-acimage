package com.acimage.community.service.topic.Impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.model.domain.community.Category;
import com.acimage.common.model.domain.community.Topic;
import com.acimage.common.model.page.MyPage;
import com.acimage.common.utils.EsUtils;
import com.acimage.common.utils.LambdaUtils;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.community.global.enums.SortMode;
import com.acimage.community.model.request.TopicQueryByCategoryIdReq;
import com.acimage.community.model.request.TopicQueryBySortReq;
import com.acimage.community.model.request.TopicQueryByTagIdReq;
import com.acimage.community.model.request.TopicSearchReq;
import com.acimage.community.service.categoty.CategoryQueryService;
import com.acimage.community.service.tag.TagQueryService;
import com.acimage.community.service.topic.TopicEsSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TopicEsSearchServiceImpl implements TopicEsSearchService {
    @Autowired
    EsUtils esUtils;
    @Autowired
    ElasticsearchRestTemplate esTemplate;
    @Autowired
    CategoryQueryService categoryQueryService;
    @Autowired
    TagQueryService tagQueryService;

    @Override
    public List<Topic> searchSimilar(long topicId, int size) {
        List<String> columns = LambdaUtils.columnsFrom(Topic::getContent, Topic::getTitle);
        List<TopicIndex> topicIndices = esUtils.similarQuery(Long.toString(topicId), TopicIndex.class, columns, 1, size);
        return TopicIndex.toTopicList(topicIndices);
    }

    @Override
    public List<Topic> searchSimilarByTitle(long topicId, String title, int size) {
        String column = LambdaUtils.columnOf(TopicIndex::getTitle);
        float score = 0.2f;
        List<TopicIndex> topicIndices = esUtils.matchQuery(TopicIndex.class, column, title, 1, size + 1, score);
        List<Topic> topicList = TopicIndex.toTopicList(topicIndices).stream()
                .filter(o -> !o.getId().equals(topicId))
                .collect(Collectors.toList());
        if (topicList.size() == size + 1) {
            return topicList.subList(0, size);
        } else {
            return topicList;
        }
    }

    @Override
    public MyPage<Topic> searchByTagId(TopicQueryByTagIdReq queryReq) {
        Integer tagId=queryReq.getTagId();
        int pageNo=queryReq.getPageNo();
        int pageSize=queryReq.getPageSize();
        SortMode sortMode=queryReq.getSortMode();
        //检查tagId是否存在
        tagQueryService.checkAndListTags(Collections.singletonList(tagId));
        //获取sortBuilder
        FieldSortBuilder sortBuilder=SortMode.toSortBuilder(sortMode);

        String column = LambdaUtils.columnOf(Topic::getTagIds);
        MyPage<TopicIndex> topicIndexPage = esUtils.termQuery(column, tagId, TopicIndex.class, pageNo, pageSize,sortBuilder);
        return TopicIndex.toTopicPage(topicIndexPage);
    }

    @Override
    public MyPage<Topic> searchBySort(TopicQueryByCategoryIdReq queryReq) {
        int categoryId=queryReq.getCategoryId();
        int pageNo=queryReq.getPageNo();
        int pageSize=queryReq.getPageSize();
        SortMode sortMode=queryReq.getSortMode();

        FieldSortBuilder sortBuilder=SortMode.toSortBuilder(sortMode);

        //校验分类是否存在
        List<Integer> categoryIds = ListUtils.extract(Category::getId, categoryQueryService.listAll());
        if(!categoryIds.contains(categoryId)){
            log.warn("用户查询分类 id:{}不存在",categoryId);
            throw new BusinessException("分类不存在");
        }
        String column=LambdaUtils.columnOf(TopicIndex::getCategoryId);

        MyPage<TopicIndex> topicIndexPage=esUtils.termQuery(column,categoryId, TopicIndex.class,pageNo,pageSize,sortBuilder);
        return TopicIndex.toTopicPage(topicIndexPage);
    }

    @Override
    public MyPage<Topic> searchBySort(TopicQueryBySortReq queryReq) {
        int pageNo=queryReq.getPageNo();
        int pageSize=queryReq.getPageSize();
        SortMode sortMode=queryReq.getSortMode();

        FieldSortBuilder sortBuilder=SortMode.toSortBuilder(sortMode);

        MyPage<TopicIndex> topicIndexPage=esUtils.queryBySort(TopicIndex.class,pageNo,pageSize,sortBuilder);
        return TopicIndex.toTopicPage(topicIndexPage);
    }


    @Override
    public MyPage<Topic> search(TopicSearchReq topicSearchReq) {
        String search = topicSearchReq.getSearch();
        Integer categoryId = topicSearchReq.getCategoryId();
        Integer tagId = topicSearchReq.getTagId();
        Integer pageNo = topicSearchReq.getPageNo();
        SortMode sort = topicSearchReq.getSortMode();


        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        HighlightBuilder highlightBuilder = null;

        String titleColumn = LambdaUtils.columnOf(TopicIndex::getTitle);
        String contentColumn = LambdaUtils.columnOf(TopicIndex::getContent);

        if (!StrUtil.isBlank(search)) {
            //有关键词则高亮
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
            String tagIdsColumn = LambdaUtils.columnOf(TopicIndex::getTagIds);
            boolQuery.filter().add(QueryBuilders.termQuery(tagIdsColumn, tagId));
        }
        if (categoryId != null) {
            String categoryIdColumn = LambdaUtils.columnOf(TopicIndex::getCategoryId);
            boolQuery.filter().add(QueryBuilders.termQuery(categoryIdColumn, categoryId));
        }


        NativeSearchQueryBuilder nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(PageRequest.of(pageNo - 1, 10));
        if (highlightBuilder != null) {
            nativeSearchQuery.withHighlightBuilder(highlightBuilder);
        }
        if (sort != null && sort.toColumn() != null) {
            FieldSortBuilder sortBuilder = new FieldSortBuilder(Objects.requireNonNull(sort.toColumn()))
                    .order(SortOrder.DESC);
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
