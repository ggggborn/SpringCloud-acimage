package com.acimage.community.mq.producer;

import com.acimage.common.model.mq.dto.EsAddDto;
import com.acimage.common.model.mq.dto.EsDeleteDto;
import com.acimage.common.model.mq.dto.EsUpdateDto;
import com.acimage.community.mq.config.SyncEsMqConfig;
import com.acimage.community.mq.consts.ExchangeConstants;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SyncEsMqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendAddMessage(Object entity) {
        EsAddDto esAddDto = new EsAddDto(entity);
        rabbitTemplate.convertAndSend(ExchangeConstants.SYNC_ES_EXCHANGE, SyncEsMqConfig.SYNC_ES_ROUTE, esAddDto);
    }

    public void sendUpdateMessage(Object entity, List<String> columns) {
        EsUpdateDto esUpdateDto = new EsUpdateDto();
        esUpdateDto.from(entity);
        esUpdateDto.setColumns(columns);
        rabbitTemplate.convertAndSend(ExchangeConstants.SYNC_ES_EXCHANGE, SyncEsMqConfig.SYNC_ES_ROUTE, esUpdateDto);
    }

    public void sendDeleteMessage(String id,Class<?> type) {
        EsDeleteDto esDeleteDto=new EsDeleteDto(id,type);
        rabbitTemplate.convertAndSend(ExchangeConstants.SYNC_ES_EXCHANGE, SyncEsMqConfig.SYNC_ES_ROUTE, esDeleteDto);
    }


}
