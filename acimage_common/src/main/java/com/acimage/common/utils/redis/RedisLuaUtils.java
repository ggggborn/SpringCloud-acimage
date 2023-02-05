package com.acimage.common.utils.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class RedisLuaUtils {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    private final static DefaultRedisScript<Long> incrementIfPresent = new DefaultRedisScript<>();
    private final static DefaultRedisScript<List<Long>> a = new DefaultRedisScript<>();
    private final static DefaultRedisScript<Long> incrementIfPresentZSet = new DefaultRedisScript<>();
    private final static DefaultRedisScript<String> getAndCombineAndDelete = new DefaultRedisScript<>();

    private final static DefaultRedisScript<Long> ifpForHashKey = new DefaultRedisScript<>();

    private final static DefaultRedisScript<List> requestLimit = new DefaultRedisScript<>();


    @PostConstruct
    public void init() {

    }

    static {
        //设置忽略空字段
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 指定脚本文件
        incrementIfPresent.setLocation(new ClassPathResource("lua/incrementIfPresent.lua"));
        // 指定返回值
        incrementIfPresent.setResultType(Long.class);

        getAndCombineAndDelete.setLocation(new ClassPathResource("lua/getAndCombineAndDelete.lua"));
        getAndCombineAndDelete.setResultType(String.class);

        incrementIfPresentZSet.setLocation(new ClassPathResource("lua/incrementIfPresentZSet.lua"));
        incrementIfPresentZSet.setResultType(Long.class);

        ifpForHashKey.setLocation(new ClassPathResource("lua/incrementIfPresentForHashKey.lua"));
        ifpForHashKey.setResultType(Long.class);

        requestLimit.setLocation(new ClassPathResource("lua/requestLimit.lua"));
        requestLimit.setResultType(List.class);
    }

    public Long incrementIfPresent(String key, long increment) {
        return stringRedisTemplate.opsForValue().getOperations()
                .execute(incrementIfPresent, Collections.singletonList(key), Long.toString(increment));
    }

    /**
     * 如果keyForBase存在，则将redis中keyForIncrement的值增加到keyForBase中
     * 否则获取并删除keyForIncrement
     *
     * @return keyForIncrement对应的值
     */
    public Long getAndCombineAndDelete(String keyForIncrement, String hashKeyForBase, String fieldKey) {
        String result = stringRedisTemplate.opsForValue().getOperations()
                .execute(getAndCombineAndDelete, Arrays.asList(keyForIncrement, hashKeyForBase), fieldKey);
        if (result == null) {
            return null;
        }
        return Long.parseLong(result);
    }


    public Long incrementIfPresentForZSet(String key, String value, long increment) {
        return stringRedisTemplate.opsForValue().getOperations()
                .execute(incrementIfPresentZSet, Collections.singletonList(key), value, Long.toString(increment));
    }

    public Long incrementIfPresentForHashKey(String key, String hashKey, long increment) {
        return stringRedisTemplate.opsForValue().getOperations()
                .execute(ifpForHashKey, Collections.singletonList(key), hashKey, Long.toString(increment));
    }

    public List<Long> requestLimitScript(List<String> keys, List<Long> limitTimes, List<Long> expireSeconds, List<Long> penaltyTimes) {
        int len = 3 * keys.size();
        Object[] args = new Object[len];

        int i = 0;
        for (Long limitTime : limitTimes) {
            args[i] = limitTime.toString();
            i++;
        }
        for (Long expireSecond : expireSeconds) {
            args[i] = expireSecond.toString();
            i++;
        }
        for (Long penaltyTime : penaltyTimes) {
            args[i] = penaltyTime.toString();
            i++;
        }

        return stringRedisTemplate.execute(requestLimit, keys, args);
    }
}
