package com.acimage.admin.service.comment.impl;

import cn.hutool.core.util.StrUtil;
import com.acimage.admin.dao.community.CommentDao;
import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.model.request.CommentQueryReq;
import com.acimage.admin.service.comment.CommentQueryService;
import com.acimage.common.model.domain.community.Comment;
import com.acimage.common.model.page.MyPage;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DS(ModuleConstants.COMMUNITY)
public class CommentQueryServiceImpl implements CommentQueryService {
    @Autowired
    CommentDao commentDao;

    @Override
    public MyPage<Comment> pageCommentsBy(CommentQueryReq commentQueryReq) {
        String keyword=commentQueryReq.getKeyword();
        Long topicId=commentQueryReq.getTopicId();

        int pageNo = commentQueryReq.getPageNo();
        int pageSize = commentQueryReq.getPageSize();
        IPage<Comment> page=new Page<>(pageNo,pageSize);

        LambdaQueryWrapper<Comment> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(Comment::getCreateTime);
        if(!StrUtil.isBlank(keyword)){
            qw.like(Comment::getContent,keyword);
        }
        if(topicId!=null&&topicId>0){
            qw.eq(Comment::getTopicId,topicId);
        }

        IPage<Comment> resultPage = commentDao.selectPage(page,qw);

        return MyPage.from(resultPage);
    }
}
