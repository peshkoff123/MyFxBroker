package com.peshkoff.springkafkaprodcons.prod;

import com.peshkoff.springkafkaprodcons.dto.Quote;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger( KafkaProducer.class);
    private static final String topicName = "javaTopic";
    private static final String kafkaProducerId = "javaProducer";
    private String bootstrapAddress; // "localhost:9092";
    private KafkaTemplate<String, Quote> kafkaTemplate;

    public KafkaProducer( @Value( "${spring.kafka.bootstrap-servers}") String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
        kafkaTemplate = new KafkaTemplate<>( producerFactory()); //autoFlush=false by def
    }
    private ProducerFactory<String, Quote> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,  bootstrapAddress);
        props.put( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put( ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerId);
        return new DefaultKafkaProducerFactory<>( props);
    }

    public void sendQuote( String quoteTopicName, Quote quote) {
        CompletableFuture<SendResult<String, Quote>> future = kafkaTemplate.send( quoteTopicName, quote);
        future.whenComplete((result, ex) -> {
            if (ex == null)
                LOGGER.info( quoteTopicName+" offs="+result.getRecordMetadata().offset()+"; "+ quote );
            else LOGGER.error( quoteTopicName+" error: " + ex.getMessage()+"; "+ quote);
        });
    }
/*          // No necessarily - SpringBoot creates KafkaTopic under the hood
        @Bean   // to create topics defined in the application context.
        public KafkaAdmin kafkaAdmin() {
            Map<String,Object> config = new HashMap<>();
            config.put( AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
            return new KafkaAdmin( config);
        }
        @Bean  // to create NewTopic with parameters
        public NewTopic newTopic() { return new NewTopic( topicName, 1, (short)1);   }
    */

/*   for using: KafkaTemplate<String, String> + StringSerializer
     public void sendMessage( String msg) {
        LOGGER.info( String.format( "Kafka.sendMessage: %s", msg));
        CompletableFuture compFuture = kafkaTemplate.send( topicName, msg);
        try {
            Object res = compFuture.get();
            LOGGER.info( res.toString());
        }catch ( Exception ex) {
            LOGGER.error( ex.toString());
        }
    }
    public void sendMessage( String key, String msg) {
        LOGGER.info(String.format("Kafka.sendMessage: %s", msg));
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send( topicName, key, msg);
        future.whenComplete((result, ex) -> {
            if (ex == null)
                LOGGER.info("Kafka.sendMessage:=[" + msg + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            else LOGGER.error("Kafka.sendMessageError:=[" + msg + "] : " + ex.getMessage());
        });
    }
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void sendQuote( Quote quote) {
        try{ String msg = objectMapper.writeValueAsString( quote);
             sendMessage( quote.getKey(), msg);
        }catch( JsonProcessingException e) {
            LOGGER.error("Writing value to JSON failed: " + quote.toString());
        }
    }*/
}
