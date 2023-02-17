package com.acimage.common.global.config;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

@Profile("prod")
@Configuration
@ConditionalOnClass(RestHighLevelClient.class)
public class EsProdConfig {
    @Value("${spring.elasticsearch.username}")
    private String username;
    @Value("${spring.elasticsearch.password}")
    private String password;
    @Bean
    public RestHighLevelClient prodRestHighLevelClient(@Autowired RestClientBuilder restClientBuilder) {


        return new RestHighLevelClient(restClientBuilder.setHttpClientConfigCallback(requestConfig -> {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            requestConfig.setKeepAliveStrategy((response, context) -> TimeUnit.MINUTES.toMillis(3));
            return requestConfig.setDefaultCredentialsProvider(credentialsProvider);
        }));
    }
}
