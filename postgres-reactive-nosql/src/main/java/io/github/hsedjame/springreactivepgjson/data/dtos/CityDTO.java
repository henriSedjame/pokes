package io.github.hsedjame.springreactivepgjson.data.dtos;

import io.github.hsedjame.springreactivepgjson.data.projections.CityProjection;


public record CityDTO(String name) {
    public static CityDTO fromProjection(CityProjection projection) {
        return new CityDTO(projection.name());
    }
}
