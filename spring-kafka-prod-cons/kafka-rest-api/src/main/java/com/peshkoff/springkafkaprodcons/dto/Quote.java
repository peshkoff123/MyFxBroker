package com.peshkoff.springkafkaprodcons.dto;

import java.time.LocalDateTime;

public record Quote(double ask, double bid, LocalDateTime dateTime ) {}
