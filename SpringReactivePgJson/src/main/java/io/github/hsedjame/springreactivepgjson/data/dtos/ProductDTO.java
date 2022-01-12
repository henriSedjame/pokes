package io.github.hsedjame.springreactivepgjson.data.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductDTO(UUID id, String name, BigDecimal price, List<DistributorDTO> distributors) {
}
