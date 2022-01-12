package io.github.hsedjame.springreactivepgjson.data.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public record ProductInfo(String name, BigDecimal price, List<Distributor> distributors) implements Serializable {

    public static Optional<ProductInfo> fromJson(Json json) {
        try {
            return Optional.of(new ObjectMapper().readValue(json.asString(), ProductInfo.class));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public Optional<Json> toJson() {
        try {
            return Optional.of(Json.of(new ObjectMapper().writeValueAsString(this)));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
