package com.peshkoff.kafkarest;

import com.peshkoff.springkafkaprodcons.dto.HstQuoteData;
import com.peshkoff.springkafkaprodcons.dto.Quote;
import com.peshkoff.kafka.KafkaConsumer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class KafkaRestController {
    @GetMapping("/getInstrumentList")
    public List<String> getInstrumentList() {
        return KafkaConsumer.getListInstruments();
    }
    @GetMapping("/getQuotes/{instrumentList}")
    public List<Quote> getQuotes(@PathVariable String instrumentList) {
        return KafkaConsumer.getQuotes( instrumentList.split(","));
    }
    @GetMapping("/getHstData/{instrument}")
    public List<HstQuoteData> getHstData(@PathVariable String instrument) {
        return KafkaConsumer.getHstData( instrument);
    }

}
/**
 * localhost:8080/getInstrumentList :
 *   ["EURUSD","GBPUSD","USDJPY","USDCAD","USDCHF"]
 * localhost:8080/getQuotes/EURUSD,GBPUSD,USDJPY,USDCAD,USDCHF :
 *   [{"ask":0.0,"bid":0.0,"dateTime":"2023-10-29T16:42:52.0666172"},...]
 * localhost:8080/getHstData/EURUSD_M1 :
 *   [{"in":1.0549,"out":1.0549,"max":1.0549,"min":1.0549,"dateTime":"2023-10-26T18:42:34.2578125"},..]
 *
 * KafkaStreams contains GlobalMarket information( ASK, BID, VOLUME, TimeStamp)
 * Forex: EURUSD, GBPUSD, USDJPY, USDCHF
 * Shares
 * Indices
 * ...
 * KafkaStreams contains compacted market data for all instruments.
 * TimePeriods : M1, M5, H1,H4
 *
 * Standalone JavaApp SIMULATEs collection of MarketQuotes and stores data into apropos KafkaSteams.
 * Uses Springboot KafkaTemplate<String, Quote>.
 * public record Quote( double ask, double bid, LocalDateTime dateTime ) {}
 *
 * Standalone JavaApp consumes messages from MarketQuoteStreams, aggregates data on several timePeriods
 * and saves compacted market data into HistoryDataStreams.
 * public class HstQuoteData{
 *     private double in;
 *     private double out;
 *     private double max = Double.MIN_VALUE;
 *     private double min = Double.MAX_VALUE;
 *     private LocalDateTime dateTime;
 * }
 *
 * GlobalMarketProvider - outside Service provides MarketQuotes.
 * Is not REALLY used.
 *
 * **/
