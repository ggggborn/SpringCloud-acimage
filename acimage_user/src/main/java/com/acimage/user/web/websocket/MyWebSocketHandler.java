package com.acimage.user.web.websocket;

import cn.hutool.core.convert.Convert;
import com.acimage.common.global.context.UserContext;
import com.acimage.common.service.TokenService;
import com.acimage.common.utils.redis.RedisUtils;
import com.acimage.user.global.consts.WebSocketSessionConstants;
import com.acimage.user.service.usermsg.UserMsgQueryService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    public static final String WEBSOCKET_KEY = "acimage:user:websocket:online";
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserMsgQueryService userMsgQueryService;
    public static final ConcurrentMap<Long, WebSocketSession> sessionPool = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        long userId = getUserId(session);
        redisUtils.increment(WEBSOCKET_KEY, 1);
        log.info("websocket消息: 有新的连接，总数为:{} 用户id:{}", redisUtils.getForString(WEBSOCKET_KEY), userId);

        sessionPool.put(userId, session);
        Integer msgNum = userMsgQueryService.getMsgCount(userId);
        if (msgNum == null) {
            msgNum = 0;
        }
        sendMessage(userId, msgNum);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        long userId = getUserId(session);
        redisUtils.increment(WEBSOCKET_KEY, -1);
        log.info("websocket消息: 关闭连接，总数为:" + redisUtils.getForString(WEBSOCKET_KEY));

        try (WebSocketSession webSocketSession = sessionPool.remove(userId)) {
        }
    }

    private long getUserId(WebSocketSession session) {
        return Convert.convert(Long.class, session.getAttributes().get(WebSocketSessionConstants.KEY_USER_ID));
    }

    public void sendMessage(long userId, int msgNum) {
        WebSocketSession session = sessionPool.get(userId);
        if (session != null) {
            WebSocketMessage<?> message = new TextMessage(Integer.toString(msgNum));
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("websocket消息: 收到客户端消息:" + message.getPayload());
    }

}

