package io.github.hsedjame.springreactivepgjson.data.projections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hsedjame.springreactivepgjson.data.entities.Distributor;

import java.util.List;
import java.util.Optional;


public record DistributorProjection(String name, String cities) {

    public Optional<Distributor> map() {
        try {
            List<String> cities = (List<String>) new ObjectMapper().readValue(this.cities, List.class);
            return Optional.of(new Distributor(this.name, cities));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
