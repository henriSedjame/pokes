package io.github.hsedjame.springreactivepgjson.services;

import io.github.hsedjame.springreactivepgjson.models.projections.ProductInfoProjection;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

public sealed interface ProductService permits ReactiveProductServices {

    Flux<ProductInfoProjection> findDistributedProductsByCity(List<String> cities);
}
