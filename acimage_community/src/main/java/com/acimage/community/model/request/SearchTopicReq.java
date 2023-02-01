package com.acimage.community.model.request;

import com.acimage.common.model.Index.TopicIndex;
import com.acimage.common.utils.LambdaUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchTopicReq {
    public enum SortBy {
        NORMAL,
        TIME,
        STAR_COUNT,
        PAGE_VIEW,
        COMMENT_COUNT;

        public String toColumn() {
            switch (this) {
                case NORMAL:
                    return null;
                case TIME:
                    return LambdaUtils.columnNameOf(TopicIndex::getCreateTime);
                case STAR_COUNT:
                    return LambdaUtils.columnNameOf(TopicIndex::getStarCount);
                case PAGE_VIEW:
                    return LambdaUtils.columnNameOf(TopicIndex::getPageView);
                case COMMENT_COUNT:
                    return LambdaUtils.columnNameOf(TopicIndex::getCommentCount);
            }
            return null;
        }
    }

    private Integer categoryId;
    private Integer tagId;
    @Positive
    @NotNull
    @Max(30)
    private Integer pageNo;
    @Size(max = 15, message = "搜索字数不超过15")
    private String search;
    private SortBy sortBy;
}


