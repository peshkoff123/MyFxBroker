package com.peshkoff.springkafkaprodcons.streams;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.stereotype.Service;


@Service
public class KafkaStreamsService {
    private static final Logger LOGGER = LoggerFactory.getLogger( KafkaStreamsService.class);
    private static final String topicName = "javaTopic";
    private static final String kafkaStreamsId = "javaStreams";
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
//private static final Serde<String> STRING_SERDE = Serdes.String();

    void buildPipeline() {
        Properties conf = new Properties();
        conf.put( StreamsConfig.APPLICATION_ID_CONFIG, kafkaStreamsId);   // unique name in Kafka Cluster
        conf.put( StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        conf.put( StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        conf.put( StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream( topicName);
        source//.foreach( ( key, val) -> { System.out.println( "Stream: key:"+key+"; val:"+val);})
              //.mapValues( ( key, val) -> { System.out.println( "Stream: key:"+key+"; val:"+val);
              //                             return String.valueOf( val.length());
              //                           })
              .map( ( key, val) -> { System.out.println( "Stream: key:"+key+"; val:"+val);
                                     return new KeyValue<>(key.toLowerCase(), val);
                                    })
              .to("my-output-topic");



        KafkaStreams streams = new KafkaStreams( builder.build(), conf);
        //https://kafka.apache.org/0100/documentation.html#newconsumerconfigs
        //ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"  auto.offset.reset
        //enable.auto.commit
        streams.start();
       // streams.close();

/*        KStream<String, String> source1 = builder.stream( topicName);
        source1.toTable().toStream().to( "table-topic");
        KafkaStreams streams1 = new KafkaStreams( builder.build(), conf);
        streams1.start();*/

        KTable<Integer, Integer> table = builder.table( topicName, Materialized.as("queryable-store-name"));
        KafkaStreams streams1 = new KafkaStreams( builder.build(), conf);
        streams1.start();

    //  KStream<String, PlayEvent> kStream = kBuilder.stream( Serdes.String(), playEventSerde, "play-events");

      //  KStream<String, String> messageStream = streamsBuilder.stream("input-topic", Consumed.with(STRING_SERDE, STRING_SERDE));
        /*KTable<String, Long> wordCounts = messageStream
                .mapValues((ValueMapper<String, String>) String::toLowerCase)
                .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
                .groupBy((key, word) -> word, Grouped.with(STRING_SERDE, STRING_SERDE))
                .count(Materialized.as("counts"));
        wordCounts.toStream().to("output-topic");*/
    }
}
