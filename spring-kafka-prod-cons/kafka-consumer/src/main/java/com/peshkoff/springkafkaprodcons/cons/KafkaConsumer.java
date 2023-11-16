package com.peshkoff.springkafkaprodcons.cons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peshkoff.springkafkaprodcons.dto.Quote;
import com.peshkoff.springkafkaprodcons.services.QuoteAggregator;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
//@EnableKafka enables @KafkaListener annotations on any bean in the container
public class KafkaConsumer {
    private static Logger LOGGER = LoggerFactory.getLogger( KafkaConsumer.class);
    private static final String topicEURUSD = "EURUSD";
    private static final String topicGBPUSD = "GBPUSD";
    private static final String topicUSDJPY = "USDJPY";
    private static final String topicUSDCAD = "USDCAD";
    private static final String topicUSDCHF = "USDCHF";
    @Autowired
    private QuoteAggregator quoteAggregator;

    @KafkaListener( topicPartitions =
      { @TopicPartition(topic = topicEURUSD, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicGBPUSD, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDJPY, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDCAD, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDCHF, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")})
      })
    public void quoteListener( @Payload Quote quote,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topicName) {
        LOGGER.info( topicName+" : "+quote);
        quoteAggregator.addNextQuote( topicName, quote);
    }

/*    private String groupName = "javaConsumer";
    private String bootstrapAddress; // "localhost:9092";
    public KafkaConsumer( @Value( "${spring.kafka.consumer.bootstrap-servers}") String bootstrapAddress,
                          @Value( "${spring.kafka.consumer.group-id}") String groupName) {
        this.bootstrapAddress = bootstrapAddress;
        this.groupName = groupName;
    }*/
/*
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Quote> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Quote> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory( consumerFactory());
        return factory;
    }
    private ConsumerFactory<String, Quote> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put( ConsumerConfig.GROUP_ID_CONFIG,          groupName);
        //props.put( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class);
        //props.put( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        //props.put( ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); //false
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>( Quote.class));
        //return new DefaultKafkaConsumerFactory<>(props);
    }*/


/*
    @KafkaListener( topicPartitions = @TopicPartition(topic = topicName,
                    partitionOffsets = { @PartitionOffset(partition = "0", initialOffset = "0")}))
    public void quoteListener( @Payload Quote quote,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        LOGGER.info( String.format( "Kafka.quoteListener( %s); partition=%d", quote, partition));
    }
*/
 /*   @Component
    @KafkaListener( topicPartitions = @TopicPartition(topic = topicEURUSD,
                    partitionOffsets = { @PartitionOffset(partition = "0", initialOffset = "0")}))
    public static class MultiTypeKafkaListener {
        @KafkaHandler
        public void handleF( Quote quote) {
            LOGGER.info( topicEURUSD+": "+quote);
            quoteAggregator.addNextQuote( topicEURUSD, quote);
        }
        @KafkaHandler(isDefault = true)
        public void unknown( Object object) {
            LOGGER.error( "Unknown type topic:" + topicEURUSD+" : "+object);
        }
    }
*/
   // by def msg consumption runs in mainThread
   //@KafkaListener( topics=topicName/*,groupId=groupName,*/)
/*   @KafkaListener(topicPartitions = @TopicPartition(topic = topicName,
                  partitionOffsets = { @PartitionOffset(partition = "0", initialOffset = "0")}))
   public void consume( String msg) {
       LOGGER.info( String.format( "Kafka.msg: %s", msg));
   }
*/
/* @KafkaListener(containerFactory = "myKafkaListenerContainerFactory", topics = "myTopic")
   @KafkaListener(topics = "topic1, topic2", groupId = "foo")
   @KafkaListener(topics = "topicName")
   @KafkaListener(topicPartitions = @TopicPartition(topic = "topicName",
                                        partitionOffsets = {  @PartitionOffset(partition = "0", initialOffset = "0"),
                                                              @PartitionOffset(partition = "3", initialOffset = "0")}),
                   containerFactory = "partitionsKafkaListenerContainerFactory")
   @KafkaListener(topicPartitions = @TopicPartition( topic = "topicName", partitions = { "0", "1" }))
   public void listenWithHeaders( @Payload String message,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
       System.out.println( "Received Message: [" + message+"] from partition: " + partition);
   }*/

    /*@Bean   ??
    public JsonDeserializer jsonDeserializer() {
        return new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext context) throws IOException {
                return null;
            }
        };
    }*/
}
