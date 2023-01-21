package com.acimage.common.utils.sensitiveword;

import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnClass(SensitiveWordBs.class)
public class MyDdWordAllow implements IWordAllow {
    @Override
    public List<String> allow() {
        List<String> list = new ArrayList<>();
        list.add("党");
        return list;
    }
}
