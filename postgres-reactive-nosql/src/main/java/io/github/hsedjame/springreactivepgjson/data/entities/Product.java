package io.github.hsedjame.springreactivepgjson.data.entities;

import io.r2dbc.postgresql.codec.Json;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Optional;
import java.util.UUID;

@Table("PRODUCTS")
public record Product(@Id UUID id, Json infos) {

    public static Optional<Product> withInfos(ProductInfo info) {
        return info.toJson()
                .map(i -> new Product(UUID.randomUUID(), i));
    }
}
