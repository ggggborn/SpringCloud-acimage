package com.acimage.common.utils.sensitiveword;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass(SensitiveWordBs.class)
public class SensitiveWordConfig {

    @Autowired
    MyDdWordAllow wordAllow;

    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        SensitiveWordBs sensitiveWordBs = SensitiveWordBs.newInstance()
                .wordAllow(WordAllows.chains(WordAllows.system(), wordAllow))
                // 各种其他配置
                .init();

        return sensitiveWordBs;
    }
}
