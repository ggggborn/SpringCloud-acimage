package com.acimage.common.utils.redis;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Pair;
import com.acimage.common.utils.ExceptionUtils;
import com.acimage.common.utils.SpringContextUtils;
import com.acimage.common.utils.common.BeanUtils;
import com.acimage.common.utils.common.JacksonUtils;
import com.acimage.common.utils.common.ListUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.acimage.common.global.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class RedisUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        //设置忽略空字段
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisLuaUtils redisLuaUtils;

    @Deprecated
    public <T> void setListAsString(String key, List<T> objectList, long timeout, TimeUnit unit) {
        String json = JacksonUtils.writeValueAsString(objectList);
        stringRedisTemplate.opsForValue().set(key, json, timeout, unit);

    }

    public <T> List<T> getListFromString(String key, Class<T> type) {
        String json = stringRedisTemplate.opsForValue().get(key);
        return JacksonUtils.getList(json, type);
    }

    public Object getFromString(String key, Class<?> parameterized, Class<?>... parametrizedTypes) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(parameterized, parametrizedTypes);
        String json = stringRedisTemplate.opsForValue().get(key);
        if (json == null) {
            return null;
        }
        Object result;
        try {
            //错误写法：result=mapper.readValue(json,new TypeReference<List<T>>() { });
            result = mapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            ExceptionUtils.printIfDev(e);
            log.error("redis数据反序列化异常,key：{} json：{}", key, json);
            throw new BusinessException("服务器出错了~~请稍后重试(>m<)");
        }
        return result;
    }

    public void setObjectJson(String key, @NotNull Object obj, long timeout, TimeUnit unit) {
        if (obj == null) {
            return;
        }
        String json = JacksonUtils.writeValueAsString(obj);
        stringRedisTemplate.opsForValue().set(key, json, timeout, unit);
    }

    public void setObjectJson(String key, @NotNull Object obj) {
        if (obj == null) {
            return;
        }
        String json = JacksonUtils.writeValueAsString(obj);
        stringRedisTemplate.opsForValue().set(key, json);
    }

    public <T> T getObjectFromString(String key, Class<T> targetType) {
        String json = stringRedisTemplate.opsForValue().get(key);
        return JacksonUtils.convert(json, targetType);
    }

    /**
     * @param targetType 仅支持Long、Double、Integer、String、Date，使用Date时set进去的数据需要是毫秒数
     */
    public <T> T getForString(String key, Class<T> targetType) {
        String str = stringRedisTemplate.opsForValue().get(key);
        if (str == null) {
            return null;
        }
        if (targetType == Date.class) {
            Long millis = Long.parseLong(str);
            return Convert.convert(targetType, millis);
        }
        return Convert.convert(targetType, str);
    }

    public String getForString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public List<String> multiGetForString(List<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    public void setAsString(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key, value.toString());
    }

    public void setAsString(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public Long increment(String key, long increment) {
        return stringRedisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * redis6.2版本之后才支持
     *
     * @param key
     */
    public String getAndDeleteForString(String key) {
        return stringRedisTemplate.opsForValue().getAndDelete(key);
    }

    public String getAndExpire(String key, long timeout, TimeUnit timeUnit) {
        return stringRedisTemplate.opsForValue().getAndExpire(key, timeout, timeUnit);
    }

    public void setIfPresent(String key, String value) {
        stringRedisTemplate.opsForValue().setIfPresent(key, value);
    }

    public void setIfPresent(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().setIfPresent(key, value, timeout, timeUnit);
    }

    public Long incrementIfPresent(String key, long increment) {
        return redisLuaUtils.incrementIfPresent(key, increment);
    }

    public Boolean setIfPresentForFieldKey(String key, String filedKey, String value) {
        return redisLuaUtils.setIfPresentForFieldKey(key, filedKey, value);
    }

    public Long getAndCombineAndDelete(String keyForIncrement, String hashKeyForBase, String field) {
        return redisLuaUtils.getAndCombineAndDelete(keyForIncrement, hashKeyForBase, field);
    }

    public void setObjectForHash(String key, Object javaBean, long timeOut, TimeUnit timeUnit) {
        if (javaBean == null) {
            return;
        }
        Map<String, String> fieldJsonMap = BeanUtils.beanToFieldJsonMap(javaBean);
        stringRedisTemplate.opsForHash().putAll(key, fieldJsonMap);
        stringRedisTemplate.expire(key, timeOut, timeUnit);
    }

    public <T> T getObjectForHash(String key, Class<T> targetType) {
        Map<Object, Object> fieldJsonMap = stringRedisTemplate.opsForHash().entries(key);
        if (fieldJsonMap.size() == 0) {
            return null;
        }
        Map<String, String> fieldJsonStringMap = new HashMap<>();
        for (Object hashKey : fieldJsonMap.keySet()) {
            fieldJsonStringMap.put(hashKey.toString(), fieldJsonMap.get(hashKey).toString());
        }
        return BeanUtils.fieldJsonMapToBean(fieldJsonStringMap, targetType);
    }

    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    public Long delete(List<String> key) {
        return stringRedisTemplate.delete(key);
    }

    public Boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return stringRedisTemplate.expire(key, timeout, timeUnit);
    }

    public Long getExpire(String key, TimeUnit timeUnit) {
        return stringRedisTemplate.getExpire(key, timeUnit);
    }

    public Long addForHyperLogLog(String key, String... values) {
        return stringRedisTemplate.opsForHyperLogLog().add(key, values);
    }

    public void deleteForHyperLogLog(String key) {
        stringRedisTemplate.opsForHyperLogLog().delete(key);
    }

    public Long sizeForHyperLogLog(String key) {
        return stringRedisTemplate.opsForHyperLogLog().size(key);
    }


    public Long addForSet(String key, Object value) {
        return stringRedisTemplate.opsForSet().add(key, value.toString());
    }

    public Boolean isMemberForSet(String key, String value) {
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    public Long removeForSet(String key, String... values) {
        return stringRedisTemplate.opsForSet().remove(key, values);
    }

    public <T> Long removeForSet(String key, List<T> valueList) {
        if (CollectionUtil.isEmpty(valueList)) {
            return 0L;
        }
        Object[] values = ListUtils.convert(valueList, String.class).toArray(new String[valueList.size()]);
        return stringRedisTemplate.opsForSet().remove(key, values);
    }

    public <T> List<T> membersForSet(String key, Class<T> targetType) {
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        if (members == null) {
            return null;
        }

        List<T> result = new ArrayList<>();
        for (String member : members) {
            result.add(Convert.convert(targetType, member));
        }
        return result;
    }

    public Boolean addForZSet(String key, String value, double score) {
        return stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    public Double scoreForZSet(String key, String value) {
        return stringRedisTemplate.opsForZSet().score(key, value);
    }

    public Long sizeForZSet(String key) {
        return stringRedisTemplate.opsForZSet().size(key);
    }

    public Long removeForZSet(String key, Object toStringValue) {
        return stringRedisTemplate.opsForZSet().remove(key, toStringValue.toString());
    }

    public List<String> randomMembersForZSet(String key, int count) {
        if (SpringContextUtils.isDev()) {
            //randomMembers 在redis 6.2之后才有，我开发环境redis是windows 3.x版本，先自己实现一个
            Long sizeLong = stringRedisTemplate.opsForZSet().size(key);
            if (sizeLong == null) {
                return new ArrayList<>();
            } else if (sizeLong <= 0) {
                return new ArrayList<>();
            }
            int size = sizeLong.intValue();
            Random random = new Random(System.currentTimeMillis());
            List<String> resultList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                int index = random.nextInt(size);
                Set<String> set = stringRedisTemplate.opsForZSet().reverseRange(key, index, index);
                String idString = null;
                if (!CollectionUtil.isEmpty(set)) {
                    for (String item : set) {
                        idString = item;
                    }
                }
                if (idString != null) {
                    resultList.add(idString);
                }
            }
            return resultList;
        } else {
            return stringRedisTemplate.opsForZSet().randomMembers(key, count);
        }
    }

    public Long incrementIfPresentForZSet(String key, String value, long increment) {
        return redisLuaUtils.incrementIfPresentForZSet(key, value, increment);
    }

    public Set<String> reverseRangeForZSet(String key, int start, int end) {
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    public Long incrementIfPresentForFieldKey(String key, String hashKey, long increment) {
        return redisLuaUtils.incrementIfPresentForFieldKey(key, hashKey, increment);
    }


    public List<Pair<String, Double>> reverseRangeWithScoreForZSet(String key, int start, int end) {
        Set<ZSetOperations.TypedTuple<String>> valueAnsScoreSet = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        if (valueAnsScoreSet == null) {
            return new ArrayList<>();
        }

        List<Pair<String, Double>> valueAndScoreList = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> item : valueAnsScoreSet) {
            Pair<String, Double> pair = new Pair<>(item.getValue(), item.getScore());
            valueAndScoreList.add(pair);
        }

        valueAndScoreList.sort((next, current) -> current.getValue().compareTo(next.getValue()));
        return valueAndScoreList;
    }

    public <T, V> List<Pair<T, V>> reverseRangeWithScoreForZSet(String key, int start, int end, Class<T> valueType, Class<V> scoreType) {
        Set<ZSetOperations.TypedTuple<String>> valueAnsScoreSet = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        if (valueAnsScoreSet == null) {
            return new ArrayList<>();
        }

        List<Pair<T, V>> valueAndScoreList = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> item : valueAnsScoreSet) {
            T value = Convert.convert(valueType, item.getValue());
            Object newScore = item.getValue();
            if (scoreType == Date.class) {
                assert newScore != null;
                newScore = Long.parseLong(newScore.toString());
            }

            V score = Convert.convert(scoreType, newScore);
            Pair<T, V> pair = new Pair<>(value, score);
            valueAndScoreList.add(pair);
        }
        return valueAndScoreList;
    }

    public List<Long> requestLimitScript(List<String> keys, List<Long> limitTimes, List<Long> expireSeconds, List<Long> penaltyTimes) {
        return redisLuaUtils.requestLimitScript(keys, limitTimes, expireSeconds, penaltyTimes);
    }
}
