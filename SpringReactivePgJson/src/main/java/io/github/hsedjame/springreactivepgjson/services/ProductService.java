package io.github.hsedjame.springreactivepgjson.services;

import io.github.hsedjame.springreactivepgjson.data.projections.ProductInfoProjection;
import reactor.core.publisher.Flux;

import java.util.List;

public sealed interface ProductService permits ReactiveProductServices {

    Flux<ProductInfoProjection> findDistributedProductsByCity(List<String> cities);
}
