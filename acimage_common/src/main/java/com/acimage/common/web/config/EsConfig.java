package com.acimage.common.web.config;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(RestHighLevelClient.class)
public class EsConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder.setHttpClientConfigCallback(requestConfig ->
                requestConfig.setKeepAliveStrategy((response, context) -> TimeUnit.MINUTES.toMillis(3))));
    }


}
