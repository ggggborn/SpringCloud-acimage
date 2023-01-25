package com.acimage.common.utils;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import toolgood.words.StringSearch;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

@Component
@ConditionalOnClass(StringSearch.class)
public class SensitiveWordUtils {

    private static StringSearch search;
    String sensitiveWordFileName="sensitive_word.txt";

    @PostConstruct
    private void init(){
        File file = new ClassPathResource(sensitiveWordFileName).getFile();
        List<String> words = new FileReader(file).readLines();
        search = new StringSearch();
        search.SetKeywords(words);
    }

    public static String filter(String str){
        return search.Replace(str,'*');
    }
}
