package com.acimage.user.global.config;

import com.acimage.user.web.websocket.MyWebSocketHandler;
import com.acimage.user.web.websocket.MyHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
//@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    MyHandshakeInterceptor interceptor;
    @Autowired
    MyWebSocketHandler myWebSocketHandler;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/websocket")
                .setAllowedOrigins("*")
                .addInterceptors(interceptor);

    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        System.out.println("注册阿萨德老好saddddd看");
//        registry.addEndpoint("/wsx.ws")
//                .addInterceptors(interceptor)//拦截器方式1,暂不用
//                .setAllowedOrigins("*");//开启socketJs
//    }



}

