package com.peshkoff.springkafkaprodcons.prod;

import com.peshkoff.springkafkaprodcons.dto.HstQuoteData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger( KafkaProducer.class);
    @Autowired
    private KafkaTemplate<String, HstQuoteData> kafkaTemplate;

    public void sendHstQuoteData( String topicName, HstQuoteData hstQuoteData) {
        CompletableFuture<SendResult<String, HstQuoteData>> future = kafkaTemplate.send( topicName, hstQuoteData);
        future.whenComplete((result, ex) -> {
            if (ex == null)
                 LOGGER.info( topicName+" offs="+result.getRecordMetadata().offset()+"; "+ hstQuoteData );
            else LOGGER.error( topicName+" error: " + ex.getMessage()+"; "+ hstQuoteData);
        });
    }

}

