package com.peshkoff.kafka;

import com.peshkoff.springkafkaprodcons.dto.HstQuoteData;
import com.peshkoff.springkafkaprodcons.dto.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class KafkaConsumer {
    private static Logger LOGGER = LoggerFactory.getLogger( KafkaConsumer.class);
    private static final String[] INSTRUMENTS = { "EURUSD", "GBPUSD", "USDJPY", "USDCAD", "USDCHF"};
    private static final HashMap<String, Quote> curQuoteMap = new HashMap<>();
    private static final String topicEURUSD = "EURUSD";
    private static final String topicGBPUSD = "GBPUSD";
    private static final String topicUSDJPY = "USDJPY";
    private static final String topicUSDCAD = "USDCAD";
    private static final String topicUSDCHF = "USDCHF";
    private static final String ALL_TOPICS = topicEURUSD+","+topicGBPUSD+","+topicUSDJPY+","+topicUSDCAD+","+topicUSDCHF;
    private static final String topicEURUSD_M1 = "EURUSD_M1";
    private static final String topicEURUSD_M5 = "EURUSD_M5";
    private static final String topicEURUSD_H1 = "EURUSD_H1";
    private static final String topicEURUSD_H4 = "EURUSD_H4";
    private static final String topicGBPUSD_M1 = "GBPUSD_M1";
    private static final String topicGBPUSD_M5 = "GBPUSD_M5";
    private static final String topicGBPUSD_H1 = "GBPUSD_H1";
    private static final String topicGBPUSD_H4 = "GBPUSD_H4";
    private static final String topicUSDJPY_M1 = "USDJPY_M1";
    private static final String topicUSDJPY_M5 = "USDJPY_M5";
    private static final String topicUSDJPY_H1 = "USDJPY_H1";
    private static final String topicUSDJPY_H4 = "USDJPY_H4";
    private static final String topicUSDCAD_M1 = "USDCAD_M1";
    private static final String topicUSDCAD_M5 = "USDCAD_M5";
    private static final String topicUSDCAD_H1 = "USDCAD_H1";
    private static final String topicUSDCAD_H4 = "USDCAD_H4";
    private static final String topicUSDCHF_M1 = "USDCHF_M1";
    private static final String topicUSDCHF_M5 = "USDCHF_M5";
    private static final String topicUSDCHF_H1 = "USDCHF_H1";
    private static final String topicUSDCHF_H4 = "USDCHF_H4";
    private static final HashMap<String, List<HstQuoteData>> hstQuoteDataMap = new HashMap<>();

    private static final List<String> instrList = Arrays.asList( INSTRUMENTS);
    public static List<String> getListInstruments() {
        return instrList;
    }

    static {
        for( String instr: INSTRUMENTS) {
            curQuoteMap.put( instr, new Quote( 0.0, 0.0, LocalDateTime.now()));
        }
    }
    public static List<Quote> getQuotes( String[] instrArr) {
        List<Quote> res = new ArrayList<>();
        for(String instr : instrArr) {
            res.add( curQuoteMap.get( instr));
        }
        return res;
    }
    public static List<HstQuoteData> getHstData( String instrument) {
        return hstQuoteDataMap.get( instrument);
    }

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
      curQuoteMap.put( topicName, quote);
    }

    @KafkaListener( topicPartitions =
      { @TopicPartition(topic = topicEURUSD_M1, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicEURUSD_M5, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicEURUSD_H1, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicEURUSD_H4, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
              @TopicPartition(topic = topicGBPUSD_M1, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
              @TopicPartition(topic = topicGBPUSD_M5, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
              @TopicPartition(topic = topicGBPUSD_H1, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
              @TopicPartition(topic = topicGBPUSD_H4, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDJPY_M1, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDJPY_M5, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDJPY_H1, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDJPY_H4, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
              @TopicPartition(topic = topicUSDCAD_M1, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
              @TopicPartition(topic = topicUSDCAD_M5, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
              @TopicPartition(topic = topicUSDCAD_H1, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
              @TopicPartition(topic = topicUSDCAD_H4, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDCHF_M1, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDCHF_M5, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDCHF_H1, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}),
        @TopicPartition(topic = topicUSDCHF_H4, partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")})
      })
    public void hstDataListener( @Payload HstQuoteData hstQuoteData,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topicName) {
      LOGGER.info( topicName+" : "+hstQuoteData);
      hstQuoteDataMap.computeIfAbsent( topicName, tName->new ArrayList<HstQuoteData>()).add( hstQuoteData);
    }

}
