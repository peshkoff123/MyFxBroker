package com.peshkoff.springkafkaprodcons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

public record Quote( double ask, double bid, LocalDateTime dateTime ) {}
