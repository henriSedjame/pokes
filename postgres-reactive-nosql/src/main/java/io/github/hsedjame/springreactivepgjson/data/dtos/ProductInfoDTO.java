package io.github.hsedjame.springreactivepgjson.data.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hsedjame.springreactivepgjson.data.projections.ProductInfoProjection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public record ProductInfoDTO(String name, BigDecimal price, List<DistributorDTO> distributors) {

    public static Optional<ProductInfoDTO> fromProjection(ProductInfoProjection projection) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<DistributorDTO> distributors = (List<DistributorDTO>)
                    objectMapper.readValue(projection.distributors(), List.class)
                            .stream()
                            .map(d -> objectMapper.convertValue(d, DistributorDTO.class))
                            .toList();

            return Optional.of(new ProductInfoDTO(projection.name(), projection.price(), distributors));

        } catch (JsonProcessingException e) {
            return Optional.empty();
        }

    }
}
