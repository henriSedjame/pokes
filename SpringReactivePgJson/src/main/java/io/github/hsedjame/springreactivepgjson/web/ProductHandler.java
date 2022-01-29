package io.github.hsedjame.springreactivepgjson.web;

import io.github.hsedjame.springreactivepgjson.data.dtos.CityDTO;
import io.github.hsedjame.springreactivepgjson.data.dtos.DistributorDTO;
import io.github.hsedjame.springreactivepgjson.data.dtos.ProductDTO;
import io.github.hsedjame.springreactivepgjson.data.dtos.ProductInfoDTO;
import io.github.hsedjame.springreactivepgjson.repositories.ProductRepository;
import io.github.hsedjame.springreactivepgjson.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Arrays;
import java.util.Optional;

@Component
public record ProductHandler(ProductRepository repository, ProductService service) {

    @NonNull
    public Mono<ServerResponse> findByName(ServerRequest request) {
        String name = request.pathVariable("name");
        return repository.findByName(name)
                .map(ProductDTO::fromProjection)
                .map(Optional::orElseThrow)
                .flatMap(p -> ServerResponse.ok().bodyValue(p))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    @NonNull
    public Mono<ServerResponse> findDistributors(ServerRequest request) {
        String name = request.pathVariable("name");
        return ServerResponse.ok().body(
                repository.findDistributors(name)
                        .map(DistributorDTO::fromProjection)
                        .map(Optional::orElseThrow),
                DistributorDTO.class
        ).onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    @NonNull
    public Mono<ServerResponse> findDistibutionCities(ServerRequest request) {
        String name = request.pathVariable("name");
        return ServerResponse.ok().body(
                repository.findDistributionCities(name)
                        .map(CityDTO::fromProjection),
                CityDTO.class
        ).onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

    @NonNull
    public Mono<ServerResponse> findDistributedIn(ServerRequest request) {
        String cities = request.queryParam("cities").orElse("");
        return ServerResponse.ok().body(
                service.findDistributedProductsByCity(Arrays.asList(cities.split(",")))
                        .map(ProductInfoDTO::fromProjection)
                        .map(Optional::orElseThrow),
                ProductInfoDTO.class
        ).onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }
}
