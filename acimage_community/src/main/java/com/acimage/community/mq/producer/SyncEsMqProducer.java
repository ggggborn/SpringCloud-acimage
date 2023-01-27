package com.acimage.community.mq.producer;

import com.acimage.common.model.mq.dto.EsAddDto;
import com.acimage.common.model.mq.dto.EsUpdateDto;
import com.acimage.community.mq.config.SyncEsMqConfig;
import com.acimage.community.mq.consts.ExchangeConstants;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SyncEsMqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendAddMessage(Object entity) {
        EsAddDto esAddDto = new EsAddDto(entity);
        rabbitTemplate.convertAndSend(ExchangeConstants.SYNC_ES_EXCHANGE, SyncEsMqConfig.SYNC_ES_ROUTE, esAddDto);
    }

    public void sendUpdateMessage(Object entity, SFunction<Object, Object>[] columns) {
        EsUpdateDto esUpdateDto = new EsUpdateDto();
        esUpdateDto.setObject(entity);
        esUpdateDto.setColumns(Arrays.asList(columns));
        rabbitTemplate.convertAndSend(ExchangeConstants.SYNC_ES_EXCHANGE, SyncEsMqConfig.SYNC_ES_ROUTE, esUpdateDto);
    }


}
