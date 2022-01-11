package io.github.hsedjame.springreactivepgjson.web;

import io.github.hsedjame.springreactivepgjson.models.Distributor;
import io.github.hsedjame.springreactivepgjson.models.ProductInfo;
import io.github.hsedjame.springreactivepgjson.repositories.CityProjection;
import io.github.hsedjame.springreactivepgjson.repositories.DistributorProjection;
import io.github.hsedjame.springreactivepgjson.repositories.ProductRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public record ProductResources(ProductRepository repository) {

    @GetMapping("/{name}/distributors")
    public Flux<Distributor> distributors(@PathVariable("name") String name) {
        return repository.findDistributors(name)
                .map(DistributorProjection::cast)
                .map(Optional::get);
    }

    @GetMapping("/{name}/distributions")
    public Flux<CityProjection> distributionCities(@PathVariable("name") String name){
        return repository.findDistributionCities(name);
    }

}
