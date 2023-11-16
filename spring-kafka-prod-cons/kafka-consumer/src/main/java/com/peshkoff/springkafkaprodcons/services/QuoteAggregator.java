package com.peshkoff.springkafkaprodcons.services;

import com.peshkoff.springkafkaprodcons.dto.HstQuoteData;
import com.peshkoff.springkafkaprodcons.dto.Quote;
import com.peshkoff.springkafkaprodcons.prod.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class QuoteAggregator {
    private static Logger LOGGER = LoggerFactory.getLogger( QuoteAggregator.class);
    private static final String[] INSTRUMENTS = { "EURUSD", "GBPUSD", "USDJPY", "USDCAD", "USDCHF"};
    private static final int[] SIGNS = {  4,  4,  2,  4,  4};
    private HstQuoteData[][] curHstQuoteData = new HstQuoteData[ INSTRUMENTS.length][4];
    private String[] INTERVALS = { "_M1", "_M5", "_H1", "_H4"};
    @Autowired
    private KafkaProducer kafkaProducer;
    public void addNextQuote( String instr, Quote quote) {
        int ind = -1;
        for( int i=0; i<INSTRUMENTS.length; i++) {
            if (INSTRUMENTS[i].equals(instr)) {
                ind = i;
                break;
            }
        }
        if( ind == -1) {
            throw new RuntimeException( "Invalid instrument name:"+instr);
        }

        HstQuoteData m1 = getNewMunute( ind, quote);
        if( m1 == null)
            return;

        LOGGER.info( "New M1: "+ m1);
        // Save to Kafka m1
        kafkaProducer.sendHstQuoteData( INSTRUMENTS[ind] + INTERVALS[0], m1);

        HstQuoteData m5 = getNewMunute5( ind, m1);
        if( m5 != null) {
            LOGGER.info("New M5: " + m5);
            // Save to Kafka m5
            kafkaProducer.sendHstQuoteData( INSTRUMENTS[ind] + INTERVALS[1], m5);
        }
        HstQuoteData h1 = getNewHour( ind, m1);
        if( h1 != null) {
            LOGGER.info("New H1: " + h1);
            // Save to Kafka H1
            kafkaProducer.sendHstQuoteData( INSTRUMENTS[ind] + INTERVALS[2], h1);
        }
        HstQuoteData h4 = getNewHour4( ind, m1);
        if( h4 != null) {
            LOGGER.info("New H4: " + h4);
            // Save to Kafka h4
            kafkaProducer.sendHstQuoteData( INSTRUMENTS[ind] + INTERVALS[3], h4);
        }
    }
    private HstQuoteData getNewMunute( int ind, Quote quote) {
        LocalDateTime qTime = quote.getDateTime();
        double qPrice = (quote.getAsk()+ quote.getBid())/2;
        qPrice = truncateTo( qPrice, SIGNS[ ind]);
        if( curHstQuoteData[ ind][0] == null) {
            curHstQuoteData[ ind][0] = new HstQuoteData( qPrice, qPrice, qPrice, qPrice, qTime);
            return null;
        }
        if( curHstQuoteData[ ind][0].getDateTime().getMinute() != qTime.getMinute()) {
            HstQuoteData oldHstQuoteData = curHstQuoteData[ ind][0];
            curHstQuoteData[ ind][0] = new HstQuoteData( qPrice, qPrice, qPrice, qPrice, qTime);
            return oldHstQuoteData;
        }
        if( curHstQuoteData[ ind][0].getMax() < qPrice)
            curHstQuoteData[ ind][0].setMax( qPrice);
        if( curHstQuoteData[ ind][0].getMin() > qPrice)
            curHstQuoteData[ ind][0].setMin( qPrice);
        curHstQuoteData[ ind][0].setOut( qPrice);
        curHstQuoteData[ ind][0].setDateTime( qTime);
        return null;
    }

    private HstQuoteData getNewMunute5( int ind, HstQuoteData m1Data) {
        if( curHstQuoteData[ ind][1] == null) {
            curHstQuoteData[ ind][1] = m1Data.clone();
            return null;
        }
        if( curHstQuoteData[ ind][1].getDateTime().getMinute()/5 != m1Data.getDateTime().getMinute()/5) {
            HstQuoteData oldHstQuoteData = curHstQuoteData[ ind][1];
            curHstQuoteData[ ind][1] = m1Data.clone();
            return oldHstQuoteData;
        }
        if( curHstQuoteData[ ind][1].getMax() < m1Data.getMax())
            curHstQuoteData[ ind][1].setMax( m1Data.getMax());
        if( curHstQuoteData[ ind][1].getMin() > m1Data.getMin())
            curHstQuoteData[ ind][1].setMin( m1Data.getMin());
        curHstQuoteData[ ind][1].setOut( m1Data.getOut());
        curHstQuoteData[ ind][1].setDateTime( m1Data.getDateTime());
        return null;
    }
    private HstQuoteData getNewHour( int ind, HstQuoteData m1Data) {
        if( curHstQuoteData[ ind][2] == null) {
            curHstQuoteData[ ind][2] = m1Data.clone();
            return null;
        }
        if( curHstQuoteData[ ind][2].getDateTime().getHour() != m1Data.getDateTime().getHour()) {
            HstQuoteData oldHstQuoteData = curHstQuoteData[ ind][2];
            curHstQuoteData[ ind][2] = m1Data.clone();
            return oldHstQuoteData;
        }
        if( curHstQuoteData[ ind][2].getMax() < m1Data.getMax())
            curHstQuoteData[ ind][2].setMax( m1Data.getMax());
        if( curHstQuoteData[ ind][2].getMin() > m1Data.getMin())
            curHstQuoteData[ ind][2].setMin( m1Data.getMin());
        curHstQuoteData[ ind][2].setOut( m1Data.getOut());
        curHstQuoteData[ ind][2].setDateTime( m1Data.getDateTime());
        return null;
    }
    private HstQuoteData getNewHour4( int ind, HstQuoteData m1Data) {
        if( curHstQuoteData[ ind][3] == null) {
            curHstQuoteData[ ind][3] = m1Data.clone();
            return null;
        }
        if( curHstQuoteData[ ind][3].getDateTime().getHour()/4 != m1Data.getDateTime().getHour()/4) {
            HstQuoteData oldHstQuoteData = curHstQuoteData[ ind][3];
            curHstQuoteData[ ind][3] = m1Data.clone();
            return oldHstQuoteData;
        }
        if( curHstQuoteData[ ind][3].getMax() < m1Data.getMax())
            curHstQuoteData[ ind][3].setMax( m1Data.getMax());
        if( curHstQuoteData[ ind][3].getMin() > m1Data.getMin())
            curHstQuoteData[ ind][3].setMin( m1Data.getMin());
        curHstQuoteData[ ind][3].setOut( m1Data.getOut());
        curHstQuoteData[ ind][3].setDateTime( m1Data.getDateTime());
        return null;
    }
    private static double truncateTo( double val, int dig) {
        String s = Double.toString( val);
        int p = s.indexOf( ".");
        if (p >= 0 && s.length() > (p + 1 + dig)) s = s.substring(0, p + 1 + dig);
        return Double.valueOf( s);
    }
}
