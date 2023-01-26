package com.acimage.community.utils;

import com.acimage.common.model.domain.Topic;
import com.acimage.common.utils.redis.RedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.acimage.community.dao.TopicDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper mapper=new ObjectMapper();

    @Autowired
    TopicDao topicDao;

    @Autowired
    RedisUtils redisUtils;

    @Test
    void testStringRedisTemplate(){
        stringRedisTemplate.opsForValue().set("test:Name","xxxyyy");
        System.out.println(stringRedisTemplate.opsForValue().get("testName"));
    }

    @Test
    void testOpsForList(){
        final String KEY="recentHotTopics";
        String startTime="2022-09-23 00:00:00";
        List<Topic> topics = topicDao.selectTopicsWithUserImagesOrderByPageView(startTime,null);
        System.out.println(stringRedisTemplate.opsForList());
    }

    @Test
    void testOpsForValue(){
        final String KEY="recentHotTopics";
        String startTime="2022-09-23 00:00:00";
        List<Topic> topics = topicDao.selectTopicsWithUserImagesOrderByPageView(startTime,null);
        String jsonTopics=null;
        try {
            jsonTopics=mapper.writeValueAsString(topics);
            System.out.println(jsonTopics);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            List<Topic> topic2 =mapper.readValue(jsonTopics,new TypeReference<List<Topic>>() { });
            System.out.println(topic2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        stringRedisTemplate.opsForValue().set(KEY,topics.toString())
//        System.out.println(stringRedisTemplate.opsForList());
    }

    @Test
    void testRedisUtilsForList(){
        final String KEY="recentHotTopics";
        String startTime="2022-09-23 00:00:00";
        List<Topic> topics = topicDao.selectTopicsWithUserImagesOrderByPageView(startTime,null);

//        try {
//            RedisUtils.setListAsString(stringRedisTemplate,KEY,topics,5, TimeUnit.SECONDS);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        try {
            List<Topic> topics1=redisUtils.getListFromString(KEY, Topic.class);
            System.out.println(topics1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Test
    void testRedisUtilsForObject(){
        final String KEY="topics";
        String startTime="2022-09-23 00:00:00";
        long id=1574286298175799296L;
        Topic topic = topicDao.selectById(id);

        redisUtils.setObjectJson(KEY, topic,50, TimeUnit.SECONDS);


        Topic topic1 =redisUtils.getObjectFromString(KEY, Topic.class);
        System.out.println(topic1);

    }

    @Test
    void testZSet(){
        final String KEY="test-list";
        //往ordered set设置值
        stringRedisTemplate.opsForZSet().incrementScore(KEY,"value1",1);
        stringRedisTemplate.opsForZSet().incrementScore(KEY,"value2",5);
        stringRedisTemplate.opsForZSet().incrementScore(KEY,"value3",7);

        //查找
        Set<ZSetOperations.TypedTuple<String>> set= stringRedisTemplate.opsForZSet().rangeWithScores(KEY,0L,9999999999L);
        Iterator<ZSetOperations.TypedTuple<String>> iterator = set.iterator();
        while(iterator.hasNext()){
            ZSetOperations.TypedTuple<String> typedTuple=iterator.next();
            System.out.println(typedTuple.getScore().longValue());
            System.out.println(typedTuple.getValue());
        }
        String[] t=new String[]{"value1"};
        //删除
        stringRedisTemplate.opsForZSet().remove(KEY,t);
        Double d=stringRedisTemplate.opsForZSet().score(KEY,"value4");
        System.out.println(d);

    }

    @Test
    void testAddForZSet(){
        final String KEY="test:zset";
        stringRedisTemplate.opsForZSet().add(KEY,"v1",10);
        stringRedisTemplate.opsForZSet().add(KEY,"v1",20);

    }

    @Test
    void testRedisTransaction(){
        stringRedisTemplate.opsForValue().set("X","Y");
        stringRedisTemplate.setEnableTransactionSupport(true);
        stringRedisTemplate.multi();
        stringRedisTemplate.exec();
        stringRedisTemplate.setEnableTransactionSupport(false);
        System.out.println(redisUtils.delete("X"));
    }

    @Test
    void testHyperLogLog(){
        String key="HyperLogLog";
        long add=redisUtils.addForHyperLogLog(key,"v1","v2","v3");
        Long size=redisUtils.sizeForHyperLogLog(key+"ddd");
        System.out.println("add:"+add);
        System.out.println("size:"+size);
        redisUtils.deleteForHyperLogLog("hhhh");
    }

    @Test
    void testSet(){
        System.out.println(redisUtils.sizeForHyperLogLog("logKey"));

    }

    @Test
    void testExpire(){
        String key="test:expire";
        long timeout=100;
        stringRedisTemplate.opsForValue().set(key,"hahah");
        boolean flag=redisUtils.expire(key,timeout,TimeUnit.SECONDS);

        System.out.println(flag);
    }

    @Test
    void testHash(){
        stringRedisTemplate.opsForHash().increment("test:hash-key","star",10);
        System.out.println(stringRedisTemplate.opsForHash().get("test:hash-key","star"));
    }

    @Test
    void testLuaScriptIncrementIfPresent(){
        String key="test:lua1";
        redisUtils.setAsString(key,"0");
        System.out.println(redisUtils.incrementIfPresent("test:lua1",10086L));
    }
}
