package io.github.hsedjame.springreactivepgjson.data.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hsedjame.springreactivepgjson.data.projections.DistributorProjection;

import java.util.List;
import java.util.Optional;

public record DistributorDTO(String name, List<String> cities) {

    public static Optional<DistributorDTO> fromProjection(DistributorProjection projection) {

        try {
            List<String> cities = (List<String>) new ObjectMapper().readValue(projection.cities(), List.class);
            return Optional.of(new DistributorDTO(projection.name(), cities));
        } catch (JsonProcessingException e) {
           return Optional.empty();
        }
    }
}
