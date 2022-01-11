package io.github.hsedjame.springreactivepgjson.models;

import io.r2dbc.postgresql.codec.Json;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Optional;
import java.util.UUID;

@Table("PRODUCTS")
public record Product(@Id @Column("id") UUID id, @Column("infos") Json infos) {

    public static Optional<Product> withInfos(ProductInfo info) {
        return info.toJson()
                .map(i -> new Product(UUID.randomUUID(), i));
    }

}
