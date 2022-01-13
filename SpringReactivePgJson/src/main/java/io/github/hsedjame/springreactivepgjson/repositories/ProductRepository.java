package io.github.hsedjame.springreactivepgjson.repositories;

import io.github.hsedjame.springreactivepgjson.data.entities.Product;

import io.github.hsedjame.springreactivepgjson.data.projections.CityProjection;
import io.github.hsedjame.springreactivepgjson.data.projections.DistributorProjection;
import io.github.hsedjame.springreactivepgjson.data.projections.ProductInfoProjection;
import io.github.hsedjame.springreactivepgjson.data.projections.ProductProjection;
import io.github.hsedjame.springreactivepgjson.services.ReactiveProductServices;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends ReactiveCrudRepository<Product, UUID> {

    @Query("SELECT * FROM products WHERE infos ->> 'name' = :name")
    Mono<ProductProjection> findByName(@Param("name") String productName);

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
          SELECT q.product ->> 'name' as name,
                  q.product -> 'distributors' as distributors,
                  cast(q.product ->> 'price' as DECIMAL) as price
            FROM
                (
                    SELECT p.infos as product,
                        ((json_array_elements(infos -> 'distributors') -> 'cities')::jsonb ?| array[:cities]) as result
                    FROM products p
                ) q
            WHERE q.result = true
          """)

    Flux<ProductInfoProjection> findDistributedProductsByCity(@Param("cities") String cities);
}
