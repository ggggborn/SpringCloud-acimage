package com.acimage.community.global.enums;

import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.utils.LambdaUtils;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

public enum SortMode {
    NORMAL,
    CREATE_TIME,
    ACTIVITY_TIME,
    STAR_COUNT,
    PAGE_VIEW,
    COMMENT_COUNT;

    public String toColumn() {
        switch (this) {
            case NORMAL:
                return null;
            case CREATE_TIME:
                return LambdaUtils.columnOf(TopicIndex::getCreateTime);
            case ACTIVITY_TIME:
                return LambdaUtils.columnOf(TopicIndex::getActivityTime);
            case STAR_COUNT:
                return LambdaUtils.columnOf(TopicIndex::getStarCount);
            case PAGE_VIEW:
                return LambdaUtils.columnOf(TopicIndex::getPageView);
            case COMMENT_COUNT:
                return LambdaUtils.columnOf(TopicIndex::getCommentCount);
        }
        return null;
    }

    public FieldSortBuilder toSortBuilder(){
        if(this==NORMAL){
            return null;
        }
        if(this.toColumn()==null){
            return null;
        }
        return new FieldSortBuilder(this.toColumn())
                .order(SortOrder.DESC);
    }

    public static FieldSortBuilder toSortBuilder(SortMode sortMode){
        if(sortMode==null){
            return null;
        }
        if(sortMode==NORMAL){
            return null;
        }
        if(sortMode.toColumn()==null){
            return null;
        }
        return new FieldSortBuilder(sortMode.toColumn())
                .order(SortOrder.DESC);
    }
}
