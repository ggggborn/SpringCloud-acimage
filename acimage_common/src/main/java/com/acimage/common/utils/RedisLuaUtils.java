package com.acimage.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
@Slf4j
public class RedisLuaUtils {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    private final static DefaultRedisScript<Long> incrementIfPresent = new DefaultRedisScript<>();
    private final static DefaultRedisScript<Long> incrementIfPresentZSet = new DefaultRedisScript<>();
    private final static DefaultRedisScript<String> getAndCombineAndDelete = new DefaultRedisScript<>();


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
}
