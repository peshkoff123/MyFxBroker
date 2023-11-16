package com.peshkoff.springkafkaprodcons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class HstQuoteData implements Cloneable {
    private double in;
    private double out;
    private double max = Double.MIN_VALUE;
    private double min = Double.MAX_VALUE;
    private LocalDateTime dateTime;

    public HstQuoteData clone() {
        try {
            return (HstQuoteData)super.clone();
        } catch( Exception ex) { ex.printStackTrace();}
        return null;
    }
}
