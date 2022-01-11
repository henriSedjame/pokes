package io.github.hsedjame.springreactivepgjson.repositories;

import io.github.hsedjame.springreactivepgjson.models.Distributor;
import io.github.hsedjame.springreactivepgjson.models.Product;

import io.github.hsedjame.springreactivepgjson.models.ProductInfo;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends ReactiveCrudRepository<Product, UUID> {

    @Query("SELECT * FROM products WHERE infos ->> 'name' = :name")
    Mono<Product> findByName(@Param("name") String productName);

    @Query("""
           SELECT
                  json_array_elements(infos -> 'distributors') ->> 'name' as name,
                  json_array_elements(infos -> 'distributors') -> 'cities' as cities
           FROM products
           WHERE infos ->> 'name' = :name
           """)
    Flux<DistributorProjection> findDistributors(@Param("name") String productName);

    @Query("""
            SELECT distinct json_array_elements_text(
                json_array_elements(infos -> 'distributors') -> 'cities'
           ) as name FROM products
            WHERE infos ->> 'name' = :name
           """)
    Flux<CityProjection> findDistributionCities(@Param("name") String productName);

    @Query("""
            SELECT q.product FROM
            (
            SELECT infos as product,
                     ((json_array_elements(infos -> 'distributors') -> 'cities')::jsonb ?& array['Web']) as result
            FROM products
            ) q
            WHERE q.result = true;
           """)
    Flux<ProductInfo> findDistributedProductsByCity(@Param("name") String cityName);
}
