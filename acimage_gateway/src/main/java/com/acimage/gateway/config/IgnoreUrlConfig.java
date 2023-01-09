package com.acimage.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "ignore")
@Slf4j
public class IgnoreUrlConfig {

    private List<String> urls;

    private static List<String> urlPatterns;

    @PostConstruct
    private void init() {
        urlPatterns = urls;
        log.info("url白名单：{}", urlPatterns);
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public static Boolean isIgnoreUrl(String url) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        for (String urlRegex : urlPatterns) {
            if (antPathMatcher.match(urlRegex,url)) {
                return true;
            }
        }
        return false;
    }
}
