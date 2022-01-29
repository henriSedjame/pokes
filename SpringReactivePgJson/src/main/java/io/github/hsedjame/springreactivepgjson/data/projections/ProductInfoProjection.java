package io.github.hsedjame.springreactivepgjson.data.projections;

import java.math.BigDecimal;

public record ProductInfoProjection(String name, BigDecimal price,  String distributors) {}
