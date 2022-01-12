package io.github.hsedjame.springreactivepgjson.web;

import io.github.hsedjame.springreactivepgjson.data.entities.Distributor;
import io.github.hsedjame.springreactivepgjson.data.entities.ProductInfo;
import io.github.hsedjame.springreactivepgjson.data.projections.CityProjection;
import io.github.hsedjame.springreactivepgjson.data.projections.DistributorProjection;
import io.github.hsedjame.springreactivepgjson.data.projections.ProductInfoProjection;
import io.github.hsedjame.springreactivepgjson.data.projections.ProductProjection;
import io.github.hsedjame.springreactivepgjson.repositories.ProductRepository;
import io.github.hsedjame.springreactivepgjson.services.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public record ProductResources(ProductRepository repository, ProductService service) {

    @GetMapping("/{name}")
    public Mono<ProductProjection> byName(@PathVariable("name") String name){
        return repository.findByName(name);
    }

    @GetMapping("/{name}/distributors")
    public Flux<Distributor> distributors(@PathVariable("name") String name) {
        return repository.findDistributors(name)
                .map(DistributorProjection::map)
                .map(Optional::orElseThrow)
                .onErrorMap(e -> new Exception(e.getMessage()));
    }

    @GetMapping(value = "/{name}/distributions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> distributionCities(@PathVariable("name") String name){
        return repository.findDistributionCities(name)
                .map(CityProjection::name);
    }

    @GetMapping("/distributed")
    public Flux<ProductInfo> byCities(@RequestParam("cities") String cities) {
        return service.findDistributedProductsByCity(Arrays.asList(cities.split(",")))
                .map(ProductInfoProjection::map)
                .map(Optional::orElseThrow)
                .onErrorMap(e -> new Exception(e.getMessage()));
    }
}
