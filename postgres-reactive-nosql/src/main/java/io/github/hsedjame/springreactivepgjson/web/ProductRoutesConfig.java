package io.github.hsedjame.springreactivepgjson.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;


import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ProductRoutesConfig {

    @Bean
    RouterFunction<ServerResponse> routes(ProductHandler handler) {
        return RouterFunctions.nest(
                GET("/products"),
                RouterFunctions
                        .route(path("/distributed"),  handler::findDistributedIn)
                        .andRoute(path("/{name}"),   handler::findByName)
                        .andRoute(path("/{name}/distributors"), handler::findDistributors)
                        .andRoute(path("/{name}/distributions"), handler::findDistibutionCities)

        );
    }
}
