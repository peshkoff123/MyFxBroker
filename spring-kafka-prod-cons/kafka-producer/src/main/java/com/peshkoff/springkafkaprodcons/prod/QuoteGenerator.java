package com.peshkoff.springkafkaprodcons.prod;

import com.peshkoff.springkafkaprodcons.dto.Quote;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class QuoteGenerator {
    private static final String[] INSTRUMENTS = { "EURUSD", "GBPUSD", "USDJPY", "USDCAD", "USDCHF"};
    private static final double[] START_PRICE = {  1.05564,  1.21306,  150.105,  1.38025,  0.94640};
    private static final double[] SPREAD = {  0.00007,  0.00016,  0.1,  0.00025,  0.0001};
    private static final int[] SIGNS = {  4,  4,  2,  4,  4};
    private static final double[] genPrice = Arrays.copyOf( START_PRICE, START_PRICE.length);

    public static Quote genNextQuote( String instr) {
        int ind = -1;
        for( int i=0; i<INSTRUMENTS.length; i++) {
            if (INSTRUMENTS[i].equals(instr)) {
               ind = i;
               break;
            }
        }
        if( ind == -1) return null;

        int sign = (Math.random()>0.5) ? 10 : -10;
        double delta = Math.random()*sign/Math.pow( 10, SIGNS[ ind]);
        genPrice[ ind] += delta;
        double ask = truncateTo( genPrice[ ind] + SPREAD[ ind], SIGNS[ ind]),
               bid = truncateTo( genPrice[ ind] - SPREAD[ ind], SIGNS[ ind]);
        return new Quote( ask, bid, LocalDateTime.now());
    }

    private static double truncateTo( double val, int dig) {
        String s = Double.toString( val);
        int p = s.indexOf( ".");
        if (p >= 0 && s.length() > (p + 1 + dig)) s = s.substring(0, p + 1 + dig);
        return Double.valueOf( s);
    }
    public List<String> getInstruments() {
        return Arrays.asList( INSTRUMENTS);
    }
    public Quote getNextQuote(String instrument) {
        return genNextQuote( instrument);
    }

}
