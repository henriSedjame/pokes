package io.github.hsedjame.springreactivepgjson.web;

import io.github.hsedjame.springreactivepgjson.models.Distributor;
import io.github.hsedjame.springreactivepgjson.models.ProductInfo;
import io.github.hsedjame.springreactivepgjson.models.projections.CityProjection;
import io.github.hsedjame.springreactivepgjson.models.projections.DistributorProjection;
import io.github.hsedjame.springreactivepgjson.models.projections.ProductInfoProjection;
import io.github.hsedjame.springreactivepgjson.repositories.ProductRepository;
import io.github.hsedjame.springreactivepgjson.services.ProductService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public record ProductResources(ProductRepository repository, ProductService service) {

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

    @GetMapping("/{cities}")
    public Flux<ProductInfo> byCities(@PathVariable("cities") String cities) {
        return service.findDistributedProductsByCity(Arrays.asList(cities.split(",")))
                .map(ProductInfoProjection::map)
                .map(Optional::orElseThrow)
                .onErrorMap(e -> new Exception(e.getMessage()));
    }
}
