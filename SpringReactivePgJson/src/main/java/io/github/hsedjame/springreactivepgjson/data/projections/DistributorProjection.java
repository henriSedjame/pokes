package io.github.hsedjame.springreactivepgjson.data.projections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hsedjame.springreactivepgjson.data.entities.Distributor;

import java.util.List;
import java.util.Optional;


public record DistributorProjection(String name, String cities) {}
