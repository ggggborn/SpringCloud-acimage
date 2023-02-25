package com.acimage.user.dao;

import com.acimage.common.model.domain.user.CommentMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMsgDao extends BaseMapper<CommentMsg> {

    List<CommentMsg> selectCommentMsgsWithUser(@Param("toUserId") long toUserId,@Param("startIndex") int startIndex,@Param("size") int size);
}
