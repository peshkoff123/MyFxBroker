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
public class Quote {
    private double ask;
    private double bid;
    private LocalDateTime dateTime;
}
