package io.github.hsedjame.springreactivepgjson.models.projections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hsedjame.springreactivepgjson.models.Distributor;
import io.github.hsedjame.springreactivepgjson.models.ProductInfo;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public record ProductInfoProjection(String name, BigDecimal price,  String distributors) {

    public Optional<ProductInfo> map() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<LinkedHashMap<String, List<String>>> distributors = objectMapper.readValue(this.distributors, List.class);

            return Optional.of(
                    new ProductInfo(this.name, this.price,
                            distributors.stream().map(m -> objectMapper.convertValue(m, Distributor.class)).toList())
            );
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
