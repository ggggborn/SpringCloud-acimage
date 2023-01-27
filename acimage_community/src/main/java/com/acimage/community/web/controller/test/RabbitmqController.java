package com.acimage.community.web.controller.test;


import com.acimage.common.model.domain.community.Star;
import com.acimage.community.mq.config.HashImageMqConfig;
import com.acimage.community.mq.consts.ExchangeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@Validated
@RequestMapping("/api")
public class RabbitmqController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/testRabbitmq")
    @ResponseBody
    public String rabbitmq() {
        Map<String,Object> map=new HashMap<>();
        map.put("createTime",new Date());
        log.info("即将发送信息");

        Star star=new Star(1L,256L);
        rabbitTemplate.convertAndSend(ExchangeConstants.TOPIC_IMAGES_EXCHANGE, HashImageMqConfig.HASH_IMAGE_ROUTE,star);

        return "send success";
    }



}
