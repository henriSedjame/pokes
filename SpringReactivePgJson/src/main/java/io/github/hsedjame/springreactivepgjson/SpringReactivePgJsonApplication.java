package io.github.hsedjame.springreactivepgjson;

import io.github.hsedjame.springreactivepgjson.data.entities.*;
import io.github.hsedjame.springreactivepgjson.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringReactivePgJsonApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactivePgJsonApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(ProductRepository repository, R2dbcEntityTemplate template) {
        return args -> {

            Distributor fnac = new Distributor("FNAC", List.of("Paris", "Poitiers", "Bordeaux"));
            Distributor darty = new Distributor("DARTY", List.of("Dijon", "Paris"));
            Distributor amazon = new Distributor("AMAZON", Collections.singletonList("Web"));
            Distributor micromania = new Distributor("MICROMANIA", List.of("Paris", "Marseille", "Bordeaux"));

            ProductInfo xbox = new ProductInfo("XBOX", BigDecimal.valueOf(499), List.of(fnac, darty, amazon));
            ProductInfo ps = new ProductInfo("PLAYSTATION", BigDecimal.valueOf(655), List.of(micromania, amazon));
            ProductInfo raspberrypi = new ProductInfo("RASPBERRY PI", BigDecimal.valueOf(150), List.of(amazon));
            ProductInfo mac = new ProductInfo("MAC BOOK", BigDecimal.valueOf(1700), List.of(fnac, amazon));
            ProductInfo iphone = new ProductInfo("IPHONE", BigDecimal.valueOf(299), List.of(fnac, amazon));

            repository.deleteAll().subscribe(
                    v -> {},
                    e -> {},
                    () ->
                        Stream.of(xbox, ps, raspberrypi, mac, iphone)
                                .map(Product::withInfos)
                                .map(Optional::orElseThrow)
                                .forEach(p -> template.insert(p).subscribe( sp -> System.out.printf("Product %s registered%n", sp.id().toString())))
            );
        };
    }

}
