package com.acimage.community.mq.producer;

import com.acimage.common.global.consts.MqConstants;
import com.acimage.common.model.mq.dto.EsAddDto;
import com.acimage.common.model.mq.dto.EsDeleteDto;
import com.acimage.common.model.mq.dto.EsUpdateByIdDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SyncEsMqProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendAddMessage(Object entity) {
        EsAddDto esAddDto = new EsAddDto(entity);
        rabbitTemplate.convertAndSend(MqConstants.SYNC_ES_EXCHANGE, MqConstants.SYNC_ES_ROUTE, esAddDto);
    }

    public void sendUpdateMessage(Object entity, List<String> columns) {
        EsUpdateByIdDto esUpdateDto = new EsUpdateByIdDto();
        esUpdateDto.with(entity);
        esUpdateDto.setColumns(columns);
        rabbitTemplate.convertAndSend(MqConstants.SYNC_ES_EXCHANGE, MqConstants.SYNC_ES_ROUTE, esUpdateDto);
    }

    public void sendDeleteMessage(String id,Class<?> type) {
        EsDeleteDto esDeleteDto=new EsDeleteDto(id,type);
        rabbitTemplate.convertAndSend(MqConstants.SYNC_ES_EXCHANGE, MqConstants.SYNC_ES_ROUTE, esDeleteDto);
    }


}
