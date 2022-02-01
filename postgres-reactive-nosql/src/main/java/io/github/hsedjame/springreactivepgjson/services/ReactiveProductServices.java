package io.github.hsedjame.springreactivepgjson.services;

import io.github.hsedjame.springreactivepgjson.data.projections.ProductInfoProjection;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;

@Service
public record ReactiveProductServices(R2dbcEntityTemplate template) implements ProductService {

    public static final String SQL = """
            SELECT q.product ->> 'name' as name,
                   q.product -> 'distributors' as distributors,
                   cast(q.product ->> 'price' as DECIMAL) as price
            FROM
                (
                    SELECT p.infos as product,
                        ((json_array_elements(infos -> 'distributors') -> 'cities')::jsonb ?| array[%s]) as result
                    FROM products p
                ) q
            WHERE q.result = true
            """;

    private String sql(List<String> cities) {

        String join = String.join(",", cities.stream().map(s -> String.format("'%s'", s)).toList());

        return String.format(SQL, join);
    }

    @Override
    public Flux<ProductInfoProjection> findDistributedProductsByCity(List<String> cities) {

        String sql = sql(cities);

        return template.getDatabaseClient()
                .sql(sql)
                .map(row ->
                        new ProductInfoProjection(
                                row.get("name", String.class),
                                row.get("price", BigDecimal.class),
                                row.get("distributors", String.class))
                )
                .all();
    }
}
