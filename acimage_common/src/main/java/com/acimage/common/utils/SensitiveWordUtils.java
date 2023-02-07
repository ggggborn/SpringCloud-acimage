package com.acimage.common.utils;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import toolgood.words.StringSearch;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnClass(StringSearch.class)
public class SensitiveWordUtils {

    private static StringSearch search;
    String sensitiveWordFileName="sensitive_word.txt";

    @PostConstruct
    private void init(){
//
        ClassPathResource classPathResource = new ClassPathResource(sensitiveWordFileName);
        InputStream inputStream = classPathResource.getStream();
        List<String> words = this.readLines(inputStream);
        search = new StringSearch();
        search.SetKeywords(words);
    }

    private List<String> readLines(InputStream inputStream){
        InputStreamReader isr = new InputStreamReader(inputStream);//传入InputStream in　键盘录入对象 in
        List<String> words=new ArrayList<>();
        //为了提高效率,将字符串进行缓冲区技术高效操作.使用BufferedReader
        BufferedReader bufr = new BufferedReader(isr);
        String line = null;
        try {
            while ((line = bufr.readLine())!=null)//判断　读取行　非空
            {
                words.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                bufr.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return words;

    }

    public static String filter(String str){
        return search.Replace(str,'*');
    }
}
