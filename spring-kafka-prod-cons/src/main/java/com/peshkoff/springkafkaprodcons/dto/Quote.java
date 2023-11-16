package com.peshkoff.springkafkaprodcons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Quote {
    private String key;
    private double ask;
    private double bid;
}
