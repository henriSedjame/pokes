package io.github.hsedjame.springreactivepgjson.data.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hsedjame.springreactivepgjson.data.projections.ProductProjection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record ProductDTO(UUID id, ProductInfoDTO infos) {

    public static Optional<ProductDTO> fromProjection(ProductProjection projection) {
        try {
            ProductInfoDTO info = new ObjectMapper().readValue(projection.infos(), ProductInfoDTO.class);
            return Optional.of(new ProductDTO(projection.id(), info));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }

    }
}
