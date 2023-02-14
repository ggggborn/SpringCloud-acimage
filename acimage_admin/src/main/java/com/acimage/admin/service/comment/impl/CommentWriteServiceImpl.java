package com.acimage.admin.service.comment.impl;


import com.acimage.admin.global.consts.ModuleConstants;
import com.acimage.admin.service.comment.CommentWriteService;
import com.acimage.feign.client.CommentClient;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DS(ModuleConstants.COMMUNITY)
public class CommentWriteServiceImpl implements CommentWriteService {
    @Autowired
    private CommentClient client;

    @Override
    public void delete(long id){
        client.delete(id);
    }
}
