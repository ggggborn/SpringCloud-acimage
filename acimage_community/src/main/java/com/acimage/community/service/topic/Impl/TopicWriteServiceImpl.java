package com.acimage.community.service.topic.Impl;


import com.acimage.common.global.context.UserContext;
import com.acimage.common.model.domain.Topic;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.community.dao.TopicDao;
import com.acimage.community.model.request.TopicModifyContentReq;
import com.acimage.community.service.topic.TopicSpAttrWriteService;
import com.acimage.community.service.topic.TopicWriteService;
import com.acimage.community.service.topic.consts.KeyConstants;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Slf4j
@Service
public class TopicWriteServiceImpl implements TopicWriteService {

    @Autowired
    TopicDao topicDao;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TopicSpAttrWriteService topicSpAttrWriteService;

    @Override
    public void save(Topic topic) {
        topicDao.insert(topic);
        log.info("用户：{} 插入 话题{}", UserContext.getUsername(), topic);
    }

    @Override
    public void remove(long id) {
        topicDao.deleteById(id);
        //删除redis数据
        redisUtils.delete(KeyConstants.HASHKP_TOPIC + id);
        log.info("用户：{} 删除 话题{}", UserContext.getUsername(), id);
    }

    @Override
    public void update(long id, String title, String content) {

        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setUpdateTime(new Date());
        topicDao.updateById(topic);

//        LambdaUpdateWrapper<Topic> qw=new LambdaUpdateWrapper<>();
//        qw.set(Topic::getContent,content)
//                .set(Topic::getTitle,title)
//                .eq(Topic::getId,id);
//        topicDao.update(null,qw);
//        topicDao.updateTopic(id, title, content);
        //删除redis数据

        redisUtils.delete(KeyConstants.HASHKP_TOPIC + id);
        log.info("用户：{} 修改 话题{}", UserContext.getUsername(), id);
    }


    @Override
    public void updateTitle(long id, String title) {
        log.info("user：{} 修改 话题标题{} title:{}", UserContext.getUsername(), id, title);

        LambdaUpdateWrapper<Topic> uw = new LambdaUpdateWrapper<>();
        uw.eq(Topic::getId, id)
                .set(Topic::getTitle, title)
                .set(Topic::getUpdateTime, new Date());
        topicDao.update(null, uw);

        //删除缓存
        redisUtils.delete(KeyConstants.HASHKP_TOPIC + id);
        //更新话题活跃时间
        topicSpAttrWriteService.changeActivityTime(id, new Date());

    }

    @Override
    public void updateContent(TopicModifyContentReq topicModifyContentReq) {

        long id = topicModifyContentReq.getId();
        String content = topicModifyContentReq.getHtml();
        log.info("user：{} 修改 话题标题{} content:{}", UserContext.getUsername(), id, content);

        LambdaUpdateWrapper<Topic> uw = new LambdaUpdateWrapper<>();
        uw.eq(Topic::getId, id)
                .set(Topic::getContent, content)
                .set(Topic::getUpdateTime, new Date());
        topicDao.update(null, uw);
        //删除缓存
        redisUtils.delete(KeyConstants.HASHKP_TOPIC + id);
        //更新话题活跃时间
        topicSpAttrWriteService.changeActivityTime(id, new Date());

    }

    @Override
    public void updateContent(long id,String content) {

        log.info("user：{} 修改 话题标题{} content:{}", UserContext.getUsername(), id, content);

        LambdaUpdateWrapper<Topic> uw = new LambdaUpdateWrapper<>();
        uw.eq(Topic::getId, id)
                .set(Topic::getContent, content)
                .set(Topic::getUpdateTime, new Date());
        topicDao.update(null, uw);
        //删除缓存
        redisUtils.delete(KeyConstants.HASHKP_TOPIC + id);
        //更新话题活跃时间
        topicSpAttrWriteService.changeActivityTime(id, new Date());
    }


}
